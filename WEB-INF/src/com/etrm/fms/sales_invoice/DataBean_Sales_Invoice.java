package com.etrm.fms.sales_invoice;

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

import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DB_AllocationUtil;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.TaxCalculator;
import com.etrm.fms.util.UtilBean;

//Coded By          : Harsh Patel
//Code Reviewed by	:  
//CR Date			: 29/11/2022 
//Status	  		: Developing
public class DataBean_Sales_Invoice 
{
	String db_src_file_name="DataBean_Sales_Invoice.java";
	
	Connection conn; 
	PreparedStatement stmt;
	PreparedStatement stmt0;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt5;
	PreparedStatement stmt6;
	PreparedStatement stmt7;
	PreparedStatement stmt_tmp;
	ResultSet rset;
	ResultSet rset0;
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset5;
	ResultSet rset6;
	ResultSet rset7;
	ResultSet rset_tmp;
	String queryString="";
	String queryString0="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";
	String queryString6="";
	String queryString7="";
	String queryString_tmp="";
	
	UtilBean utilBean = new UtilBean();
	DateUtil utilDate = new DateUtil();
	TaxCalculator TaxCalc = new TaxCalculator(); 
	DB_AllocationUtil utilAlloc = new DB_AllocationUtil();
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf3 = new DecimalFormat("###########0.000");
	NumberFormat nf0 = new DecimalFormat("###########0.0");
	
	double transaction_limit=5000000;
	//double tcs_factor=0.075;
	double tcs_factor=0;
	public double getTcs_factor() {return tcs_factor;}
	
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
	    			if(callFlag.equalsIgnoreCase("SALES_INVOICE_PREPARATION_LIST"))
	    			{
	    				VINVOICE_LIST_ABBR.add("INV_HEAD");
	    				VINVOICE_LIST_ABBR.add("LTCORA_INV_HEAD");
	    				
	    				VINVOICE_LIST_NAME.add("Sales Invoice Generation");
	    				VINVOICE_LIST_NAME.add("LTCORA Invoice Generation");
	    				
	    				if(billing_cycle.equals("0"))
	    				{
	    					forAllBillingOption();
	    				}
	    				else
	    				{
	    					getBillingCyclePeriod();
	    					inv_index=0;
	    					if(billing_cycle.equals("11"))
	    					{
	    						billing_flag="T";
	    						getTCQCompletionSalesInvoicePreparationList();
	    					}
	    					else
	    					{
	    						billing_flag="B";
	    						getSalesInvoicePreparationList();
	    					}
	    					VINDEX.add(inv_index);
	    					
	    					inv_index=0;
	    					getLTCORAInvoicePreparationList();
	    					VINDEX.add(inv_index);
	    					
	    					getFFlowInvoiceList();
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("SALES_INVOICE_GENERATION"))
	    			{
	    				getContractDetail();
	    				getContractBillingDetail();
	    				getContactPerson();
	    				getBuContactPerson();
	    				getExistingInvoiceDtl();
	    				getReceiptVoucher();
	    				getInvoiceDateCalc();
	    				getInvoiceCalculation();
	    				if(!operation.equals("MODIFY") && !submission_chk)
	    				{
	    					getRemarks();
	    				}
	    				
	    				if(!inv_flag.equals("UG"))
	    				{
		    				getTurnoverDetail();
		    				if((operation.equals("INSERT") && !submission_chk) || operation.equals("MODIFY"))
		    				{
		    					checkTCS_TDSApplicable();
		    				}
		    				
		    				if(contract_type.equals("S") || contract_type.equals("L"))
		    				{
		    					getTcsTdsInvDtl();
		    				}
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("SALES_INVOICE_REPORT"))
	    			{
	    				getInvoiceDetail();
	    				if(activityFlag.equals("APPROVE"))
	    				{
	    					getInvoiceNumber();
	    				}
	    				
	    				if(is_attachment.equals("1"))
	    				{
	    					if(inv_flag.equals("UG"))
	    					{
	    						getSug_Attachment1();
	    					}
	    					else if(inv_flag.equals("ST"))
	    					{
	    						getStorage_Attachment1();
	    					}
	    					else
	    					{
	    						getAttachment1();
	    					}
	    				}
	    				else if(is_attachment.equals("2"))
	    				{
	    					if(inv_flag.equals("UG"))
	    					{
	    						getSug_Attachment2();
	    					}
	    					else
	    					{
	    						getAttachment2();
	    					}
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("SALES_INVOICE_DETAIL"))
	    			{
	    				getInvoiceDetailForViewBeforeSub();
	    			}
	    			else if(callFlag.equalsIgnoreCase("VIEW_ALL_PDF"))
	    			{
	    				getAllPdfFileName();
	    			}
	    			else if(callFlag.equalsIgnoreCase("F_FLOW_INVOICE"))
	    			{
	    				couterpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
	    				getBillingCyclePeriod();
	    				getFilterContractList();
	    				getCustomerCounterpartyList();
	    				getContractList();
	    				getPlantDetail();
	    				getAddressType();
	    				getOtherInvoiceContactPerson();
	    				getBuUnit();
	    				getBuContactPerson();
	    				if(opration.equalsIgnoreCase("MODIFY"))
	    				{
	    					getExixtingF_FLowInvoice();
	    					getF_FlowAnnexureDetail();
	    				}
	    				else
	    				{
	    					getF_FlowInvoice();
	    				}
	    				getInvoiceNo();
	    			}
	    			else if(callFlag.equalsIgnoreCase("SALES_FFLOW_INVOICE_REPORT"))
	    			{
	    				getF_FlowInvoiceDetail();
	    				if(activityFlag.equals("APPROVE"))
	    				{
	    					getF_FlowInvoiceNumber();
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("SEND_INVOICE_MAIL"))
	    			{
	    				getSendInvoiceMailDetail();
	    			}
	    			else if(callFlag.equalsIgnoreCase("LTCORA_SUG_INV_PREPARATION_LIST"))
	    			{
	    				getLtcoraSugInvoicePreparationList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("LTCORA_STORAGE_INV_PREPARATION_LIST"))
	    			{
	    				getLtcoraStorageInvoicePreparationList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("EXISTING_CRDR_LIST"))
	    			{
	    				VINVOICE_LIST_ABBR.add("INV_HEAD");
	    				VINVOICE_LIST_ABBR.add("LTCORA_INV_HEAD");
	    				
	    				VINVOICE_LIST_NAME.add("Sales Credit/Debit Generation");
	    				VINVOICE_LIST_NAME.add("LTCORA Credit/Debit Generation");
	    				
	    				for(int i=0; i<VINVOICE_LIST_ABBR.size(); i++)
	    				{
	    					inv_index=0;
	    					String contType="'S','L','X'";
	    					if(VINVOICE_LIST_ABBR.elementAt(i).toString().equals("LTCORA_INV_HEAD"))
	    					{
	    						contType="'O','Q'";
	    					}
	    					getCreditDebitNoteList(contType);
	    					VINDEX.add(inv_index);
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("CRDR_PREPARATION_LIST"))
	    			{
	    				getCustomerListforCrDr();
	    				getInvoiceNoListforCrDr();
	    				getCriteriaList();
	    				getCommonInvoiceDetailforCrDr();
	    				getInvoiceDetailforCrDr();
	    				getCrDrInvoiceDetail();
	    				getNewCrDrInvoiceDetail();
	    				getContactPerson();
	    				getBuContactPerson();
	    				getCrDrAttachmentDetail();
	    			}
	    			else if(callFlag.equalsIgnoreCase("CRDR_INVOICE_REPORT"))
	    			{
	    				getInvoiceDetailCrDr();
	    				if(activityFlag.equals("APPROVE"))
	    				{
	    					getCrDrInvoiceNumber();
	    				}
	    				
	    				if(is_attachment.equals("1"))
	    				{
	    					getCommonInvoiceDetailforCrDr();
	    					getInvoiceDetailforCrDr();
	    					getCrDrInvoiceDetail();
	    					getNewCrDrInvoiceDetail();
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("LP_INVOICE_PREPARATION_LIST"))
	    			{
	    				VINVOICE_LIST_ABBR.add("INV_HEAD");
	    				VINVOICE_LIST_ABBR.add("LTCORA_INV_HEAD");
	    				//VINVOICE_LIST_ABBR.add("FFLOW_INV_HEAD");
	    				
	    				VINVOICE_LIST_NAME.add("Sales (SN | LOA | IGX)");
	    				VINVOICE_LIST_NAME.add("LTCORA");
	    				//VINVOICE_LIST_NAME.add("Sales (FFlow) Invoice Generation");
	    				
	    				inv_index=0;
	    				getLpSalesInvoicePreparationList();
    					VINDEX.add(inv_index);
    					
    					inv_index=0;
    					getLpLTCORAInvoicePreparationList();
    					VINDEX.add(inv_index);
    					
						/*inv_index=0;
						getLpFFlowInvoiceList();
						VINDEX.add(inv_index);*/
	    			}
	    			else if(callFlag.equalsIgnoreCase("LP_INVOICE_GENERATION"))
	    			{
	    				getContractDetail();
	    				getContactPerson();
	    				getRefrenceInvoiceDtl();
	    				getContractBillingDetail();
	    				getExistingLpInvoiceDtl();
	    				getLpInvoiceDateCalc();
	    				getInvoiceCalculation();
	    				if(!operation.equals("MODIFY") && !submission_chk)
	    				{
	    					if(invoice_dt.equals("") || invoice_dt.isEmpty())
	    					{
	    						invoice_dt = utilDate.getSysdate();
	    					}
	    					getRemarks();
	    				}

	    				if(!inv_flag.equals("UG"))
	    				{
		    				getTurnoverDetail();
		    				if((operation.equals("INSERT") && !submission_chk) || operation.equals("MODIFY"))
		    				{
		    					if(gross_include_transport_tariff.equals(""))
		    					{
		    						gross_include_transport_tariff = ori_inv_net_amt;
		    					}
		    					checkTCS_TDSApplicable();
		    				}
		    				
		    				if(contract_type.equals("S") || contract_type.equals("L"))
		    				{
		    					getTcsTdsInvDtl();
		    				}
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("SALES_LP_INVOICE_REPORT"))
	    			{
	    				getRefrenceInvoiceDtl();
	    				getLpInvoiceDetail();
	    				if(activityFlag.equals("APPROVE"))
	    				{
	    					getLpInvoiceNumber();
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("LP_INVOICE_DETAIL"))
	    			{
	    				getLPInvoiceDetailForViewBeforeSub();
	    				getRefrenceInvoiceDtl();
	    			}
	    			else if(callFlag.equalsIgnoreCase("SALES_INVOICE_CHANGE_REQUEST_APPROVAL"))
	    			{
	    				VINVOICE_LIST_ABBR.add("INV_HEAD");
	    				VINVOICE_LIST_ABBR.add("CRDR_HEAD");
	    				VINVOICE_LIST_ABBR.add("LP_HEAD");
	    				
	    				VINVOICE_LIST_NAME.add("Sales Invoice");
	    				VINVOICE_LIST_NAME.add("Sales Credit/Debit Invoice");
	    				VINVOICE_LIST_NAME.add("Sales Late Payment Invoice");
	    				
	    				getSalesInvoiceChangeList();
	    				
	    				VINVOICE_LIST_ABBR.add("FFLOW_HEAD");
    					VINVOICE_LIST_NAME.add("Sales FreeFlow Invoice");
    					
    					getSalesfflowInvoiceChangeList();
	    			}
	    			
	    			conn.close();
					conn = null;
	    		}
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
	    	if(rset7 != null){try{rset7.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_tmp != null){try{rset_tmp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt0 != null){try{stmt0.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt6 != null){try{stmt6.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt7 != null){try{stmt7.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_tmp != null){try{stmt_tmp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
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
			inv_index=0;
			//for(int i=1; i<=9; i++)
			for(int i=1; i<=11; i++) //ADDING 10 FOR TCQ Completion
			{
				if(i!=10)
				{
					billing_cycle=""+i;
					getBillingCyclePeriod();
					if(billing_cycle.equals("11"))
					{
						billing_flag="T";
						getTCQCompletionSalesInvoicePreparationList();
					}
					else
					{
						billing_flag="B";
						getSalesInvoicePreparationList();
					}
				}
			}
			VINDEX.add(inv_index);
			
			inv_index=0;
			//for(int i=1; i<=9; i++)
			for(int i=1; i<=11; i++) //ADDING 10 FOR TCQ Completion
			{
				if(i!=10)
				{
					billing_cycle=""+i;
					getBillingCyclePeriod();
					if(billing_cycle.equals("11"))
					{
						//billing_flag="T";
						//getTCQCompletionSalesInvoicePreparationList();
					}
					else
					{
						billing_flag="B";
						getLTCORAInvoicePreparationList();
					}
				}
			}
			VINDEX.add(inv_index);
			
			billing_cycle="8";
			getBillingCyclePeriod();
			getFFlowInvoiceList();
			
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
			else if(billing_cycle.equals("11")) //FOR TCQ Completion
			{
				billing_freq="T";
				billing_freq_nm="TCQ Completion";
				period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
				period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSalesInvoicePreparationList()
	{
		String function_nm="getSalesInvoicePreparationList()";

		try
		{
			//financial_year=utilDate.getFinancialYear(period_end_dt);
			
			if(billing_cycle.equals("8"))
			{
				String temp_period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
				String temp_period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
				
				/*queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,"
						+ "A.TRADE_REF_NO,TO_CHAR(C.EFF_DT,'DD/MM/YYYY'),C.BILLING_DAYS "
						+ "FROM FMS_SUPPLY_CONT_MST A, FMS_SUPPLY_BILLING_DTL C "
						+ "WHERE A.COMPANY_CD='"+comp_cd+"' AND A.IS_ALLOCATED='Y' AND A.FCC_FLAG='Y' "
						+ "AND A.START_DT<=TO_DATE('"+temp_period_end_dt+"','DD/MM/YYYY') AND A.END_DT>=TO_DATE('"+temp_period_start_dt+"','DD/MM/YYYY') "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "
						+ "AND A.AGMT_NO=C.AGMT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND C.BILLING_FREQ='"+billing_freq+"' "
						+ "AND C.EFF_DT=(SELECT MAX(E.EFF_DT) FROM FMS_SUPPLY_BILLING_DTL E WHERE C.COMPANY_CD=E.COMPANY_CD "
						+ "AND C.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND C.AGMT_NO=E.AGMT_NO AND C.CONT_NO=E.CONT_NO "
						+ "AND C.CONTRACT_TYPE=E.CONTRACT_TYPE AND E.EFF_DT<=TO_DATE('"+temp_period_start_dt+"','DD/MM/YYYY')) ";*/
				
				queryString="SELECT DISTINCT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,"
						+ "A.TRADE_REF_NO,TO_CHAR(C.EFF_DT,'DD/MM/YYYY'),C.BILLING_DAYS,A.AGMT_BASE,A.FCC_FLAG,C.EFF_DT "
						+ "FROM FMS_SUPPLY_CONT_MST A, FMS_SUPPLY_BILLING_DTL C "
						+ "WHERE A.COMPANY_CD=? AND A.IS_ALLOCATED=? " //AND A.FCC_FLAG='Y' " REMOVED AS DISCUSSED WITH MAM
						+ "AND A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.CONTRACT_TYPE IN ('S','L','X') "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "
						+ "AND A.AGMT_NO=C.AGMT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND C.BILLING_FREQ=? AND C.BILLING_FLAG=? "
						+ "AND ((C.EFF_DT>=TO_DATE(?,'DD/MM/YYYY') AND C.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) "
						+ "OR C.EFF_DT=(SELECT MAX(E.EFF_DT) FROM FMS_SUPPLY_BILLING_DTL E WHERE C.COMPANY_CD=E.COMPANY_CD "
						+ "AND C.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND C.AGMT_NO=E.AGMT_NO AND C.CONT_NO=E.CONT_NO "
						+ "AND C.CONTRACT_TYPE=E.CONTRACT_TYPE AND E.EFF_DT<TO_DATE(?,'DD/MM/YYYY'))) "
						+ "ORDER BY C.EFF_DT";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, "Y");
				stmt.setString(3, temp_period_end_dt);
				stmt.setString(4, temp_period_start_dt);
				stmt.setString(5, billing_freq);
				stmt.setString(6, billing_flag);
				stmt.setString(7, temp_period_start_dt);
				stmt.setString(8, temp_period_end_dt);
				stmt.setString(9, temp_period_start_dt);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String agmtno=rset.getString(3)==null?"0":rset.getString(3);
					String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					String cargo_no="0";
					String st_dt=rset.getString(7)==null?"":rset.getString(7);
					String end_dt=rset.getString(8)==null?"":rset.getString(8);
					String contRef=rset.getString(10)==null?"":rset.getString(10);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					String tradeRef=rset.getString(12)==null?"":rset.getString(12);
					String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, "");
					
					if(cont_type.equals("X"))
					{
						contRef=tradeRef;
					}
					String agmt_base=rset.getString(15)==null?"":rset.getString(15);
					String fcc=rset.getString(16)==null?"":rset.getString(16);
					
					String billing_eff_dt=rset.getString(13)==null?"":rset.getString(13);
					String billing_days=rset.getString(14)==null?"1":rset.getString(14);
					
					int isGreter=utilDate.getDays(billing_eff_dt, temp_period_start_dt);
					
					String issue_st_dt=temp_period_start_dt;
					String issue_end_dt=temp_period_end_dt;
					
					String temp_st_dt="";
					String temp_end_dt="";
					String diff_color="";
					if(isGreter>1)
					{
						temp_st_dt=billing_eff_dt;
					}
					else
					{
						temp_st_dt=temp_period_start_dt;
					}
					
					int temp_count=utilDate.getDays(end_dt,issue_end_dt);
					if(temp_count <= 0)
					{
						issue_end_dt=end_dt;
					}
					
					/*temp_count=utilDate.getDays(temp_period_start_dt,st_dt);
					if(temp_count <= 1)
					{
						temp_period_start_dt=st_dt;
					}*/
					
					String temp_dt = utilDate.getDate(temp_st_dt,"-1");
					int tot_row=1;
					for(int i=0;i<tot_row;i++)
					{
						temp_st_dt=utilDate.getDate(temp_dt,"1");
						temp_dt=utilDate.getDate(temp_dt,billing_days);
						
						//int checkMthEnd=utilDate.getDays(temp_period_end_dt,temp_st_dt);
						int checkMthEnd=utilDate.getDays(issue_end_dt,temp_st_dt);
						boolean isBreak=false;
						if(Integer.parseInt(billing_days) <= checkMthEnd)
						{
							temp_end_dt = temp_dt;
							//isBreak=false;
						}
						else
						{
							//temp_end_dt = temp_period_end_dt;
							temp_end_dt = issue_end_dt;
							//isBreak=true;
							break;
						}
						
						if(utilDate.getDays(end_dt, temp_end_dt)<=0)
						{
							temp_end_dt=end_dt;
							diff_color="blue";
						}
						
						String innerBillingEffDt=billing_eff_dt; //defaualt added
						String innerBillingDays="";
						String innerBillingFreq="";
						queryString1="SELECT DISTINCT TO_CHAR(EFF_DT,'DD/MM/YYYY'),BILLING_DAYS,BILLING_FREQ "
								+ "FROM FMS_SUPPLY_BILLING_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND CONT_NO=? "
								+ "AND CONTRACT_TYPE=? "
								+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_SUPPLY_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
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
						
						//System.out.println(billing_eff_dt+"==="+innerBillingEffDt);
						if(!billing_eff_dt.equals(innerBillingEffDt))
						{
							/*if(billing_freq.equals(innerBillingFreq))
							{
								billing_days=innerBillingDays;
								tot_row+=1;
							}
							else*/
							{
								//System.out.println("billing before break!");
								break;
							}
						}
						else
						{
							tot_row+=1;
						}
						
						queryString0="SELECT PLANT_SEQ_NO "
								+ "FROM FMS_SUPPLY_CONT_PLANT "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
						stmt0=conn.prepareStatement(queryString0);
						stmt0.setString(1, comp_cd);
						stmt0.setString(2, countpty_cd);
						stmt0.setString(3, contno);
						stmt0.setString(4, contrev);
						stmt0.setString(5, agmtno);
						stmt0.setString(6, agmtrev);
						stmt0.setString(7, cont_type);
						rset0=stmt0.executeQuery();
						while(rset0.next())
						{
							String plant_seq = rset0.getString(1)==null?"":rset0.getString(1);
							String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "C");
							
							queryString2="SELECT PLANT_SEQ_NO "
									+ "FROM FMS_SUPPLY_CONT_BU "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
							stmt2=conn.prepareStatement(queryString2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, countpty_cd);
							stmt2.setString(3, contno);
							stmt2.setString(4, contrev);
							stmt2.setString(5, agmtno);
							stmt2.setString(6, agmtrev);
							stmt2.setString(7, cont_type);
							rset2=stmt2.executeQuery();
							while(rset2.next())
							{
								String bu_plant_seq = rset2.getString(1)==null?"":rset2.getString(1);
								String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
								
								String state_code=utilBean.getState_TIN(conn, own_cd, own_cd, "B", bu_plant_seq);
								
								int isInvExist=isInvoiceExist(own_cd,countpty_cd, contno, agmtno,cont_type, plant_seq, 
										bu_plant_seq, billing_cycle, temp_st_dt, temp_end_dt, state_code,cargo_no,"F");
								if(isInvExist > 0 && !fcc.equals("Y"))
								{
									fcc="Y";
								}
								
								//double qtyMMBTU=utilAlloc.getSupplyAllocationQty(own_cd, countpty_cd, agmtno, contno, cont_type, plant_seq, bu_plant_seq, period_start_dt, period_end_dt);
								double qtyMMBTU=0;
								
								if(agmt_base.equals("D"))
								{
									qtyMMBTU=utilAlloc.getDeliverdSupplyAllocationQty(conn, own_cd, countpty_cd, agmtno, contno, cont_type, plant_seq, bu_plant_seq, temp_st_dt, temp_end_dt);
								}
								else
								{
									qtyMMBTU=utilAlloc.getSupplyAllocationQty(conn, own_cd, countpty_cd, agmtno, contno, cont_type, plant_seq, bu_plant_seq, temp_st_dt, temp_end_dt,"0");
								}
								if((qtyMMBTU > 0 && fcc.equals("Y")) || (qtyMMBTU <= 0 && fcc.equals("Y") && agmt_base.equals("D"))) //ALLOW QTY IS <= 0 DLV BASE CONTRACT
								{
									inv_index=inv_index+1;
									
									VBU_STATE_TIN.add(state_code);							
									//VFINANCIAL_YEAR.add(financial_year);
									VCOUNTERPTY_CD.add(countpty_cd);
									VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
									VAGMT_NO.add(agmtno);
									VAGMT_REV_NO.add(agmtrev);
									VCONT_NO.add(contno);
									VCONT_REV_NO.add(contrev);
									VCARGO_NO.add(cargo_no);
									VSTART_DT.add(rset.getString(7)==null?"":rset.getString(7));
									VEND_DT.add(rset.getString(8)==null?"":rset.getString(8));
									VCONT_NAME.add(rset.getString(9)==null?"":rset.getString(9));
									VCONT_REF_NO.add(contRef);
									VCONTRACT_TYPE.add(cont_type);
									VDEAL_NO.add(deal_no);
									
									VPLANT_SEQ.add(plant_seq);
									VPLANT_ABBR.add(plant_abbr);
									VBU_PLANT_SEQ.add(bu_plant_seq);
									VBU_PLANT_ABBR.add(bu_plant_abbr);
									//VPERIOD_START_DT.add(period_start_dt);
									//VPERIOD_END_DT.add(period_end_dt);
									VPERIOD_START_DT.add(temp_st_dt);
									VPERIOD_END_DT.add(temp_end_dt);
									VTEMP_PERIOD_START_DT.add(temp_st_dt);
									VTEMP_PERIOD_END_DT.add(temp_end_dt);
									VALLOC_QTY.add(nf.format(qtyMMBTU));
									VAGMT_BASE.add(agmt_base);
									
									VSTATUS.add("");
									
									VBILLING_FREQ_FLAG.add(billing_cycle);
									VBILLING_FREQ_NM.add(billing_freq_nm);
									
									VDIFF_COLOR.add(diff_color); //FOR IS GREATER
									
									String inv_flag="F";
									VINV_FLAG.add(inv_flag);
									getInvDetailForPreparationList(own_cd,countpty_cd, contno, agmtno,cont_type, plant_seq, 
											bu_plant_seq, billing_cycle, temp_st_dt, temp_end_dt, state_code,cargo_no,inv_flag);
				
								}
							}
							rset2.close();
							stmt2.close();
						}
						rset0.close();
						stmt0.close();
						
						/*if(isBreak)
						{
							//System.out.println("End Date Break!");
							break;
						}*/
						//System.out.println(tot_row+"-->");
					}
				}
				rset.close();
				stmt.close();
			}
			else
			{
				/*queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,"
						+ "A.TRADE_REF_NO,TO_CHAR(C.EFF_DT,'DD/MM/YYYY'),A.AGMT_BASE,A.FCC_FLAG "
						+ "FROM FMS_SUPPLY_CONT_MST A, FMS_SUPPLY_BILLING_DTL C "
						+ "WHERE A.COMPANY_CD=? AND A.IS_ALLOCATED=? " //AND A.FCC_FLAG='Y' " REMOVED AS DISCUSSED WITH MAM
						+ "AND A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "
						+ "AND A.AGMT_NO=C.AGMT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND C.BILLING_FREQ=? "
						+ "AND C.EFF_DT=(SELECT MAX(E.EFF_DT) FROM FMS_SUPPLY_BILLING_DTL E WHERE C.COMPANY_CD=E.COMPANY_CD "
						+ "AND C.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND C.AGMT_NO=E.AGMT_NO AND C.CONT_NO=E.CONT_NO "
						+ "AND C.CONTRACT_TYPE=E.CONTRACT_TYPE AND E.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) "
						+ "AND (SELECT NVL(SUM(QTY_MMBTU),0) FROM FMS_DAILY_ALLOCATION_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
						+ "AND B.GAS_DT >=TO_DATE(?,'DD/MM/YYYY') AND B.GAS_DT <=TO_DATE(?,'DD/MM/YYYY')"
						+ "AND B.NOM_REV_NO=(SELECT MAX(C.NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND C.CONT_NO=B.CONT_NO AND C.AGMT_NO=B.AGMT_NO AND C.CONTRACT_TYPE=B.CONTRACT_TYPE AND C.GAS_DT=B.GAS_DT "
						+ "AND C.PLANT_SEQ=B.PLANT_SEQ AND C.TRANSPORTER_CD=B.TRANSPORTER_CD AND C.TRANS_SEQ=B.TRANS_SEQ)) > 0 ";
				*/
				
				//String cont_map=counterparty_cd+"-"+contract_type+"-"+agmt_no+"-%-"+cont_no+"-%";
				//String exit_point="C-"+counterparty_cd+"-"+plant_seq;
				
				queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,"
						+ "A.TRADE_REF_NO,TO_CHAR(C.EFF_DT,'DD/MM/YYYY'),A.AGMT_BASE,A.FCC_FLAG,D.PLANT_SEQ_NO,E.PLANT_SEQ_NO "
						+ "FROM FMS_SUPPLY_CONT_MST A, "
							+ "FMS_SUPPLY_BILLING_DTL C, "
							+ "FMS_SUPPLY_CONT_PLANT D, "
							+ "FMS_SUPPLY_CONT_BU E "
						+ "WHERE A.COMPANY_CD=? AND A.IS_ALLOCATED=? " //AND A.FCC_FLAG='Y' " REMOVED AS DISCUSSED WITH MAM
						+ "AND A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY') AND A.CONTRACT_TYPE IN ('S','L','X') "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ ""
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "
						+ "AND A.AGMT_NO=C.AGMT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND D.PLANT_SEQ_NO=C.PLANT_SEQ_NO AND C.BILLING_FREQ=? AND C.BILLING_FLAG=? "
						+ "AND C.EFF_DT=(SELECT MAX(E.EFF_DT) FROM FMS_SUPPLY_BILLING_DTL E WHERE C.COMPANY_CD=E.COMPANY_CD "
							+ "AND C.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND C.AGMT_NO=E.AGMT_NO AND C.CONT_NO=E.CONT_NO "
							+ "AND C.CONTRACT_TYPE=E.CONTRACT_TYPE AND E.PLANT_SEQ_NO=C.PLANT_SEQ_NO AND E.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) "
						+ ""
						+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.CONT_NO=D.CONT_NO AND A.CONT_REV=D.CONT_REV "
						+ "AND A.AGMT_NO=D.AGMT_NO AND A.AGMT_REV=D.AGMT_REV AND A.CONTRACT_TYPE=D.CONTRACT_TYPE "
						+ ""
						+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO AND A.CONT_REV=E.CONT_REV "
						+ "AND A.AGMT_NO=E.AGMT_NO AND A.AGMT_REV=E.AGMT_REV AND A.CONTRACT_TYPE=E.CONTRACT_TYPE "
						+ ""
						+ "AND (CASE WHEN A.AGMT_BASE='D' THEN "
						+ "(SELECT NVL(SUM(EXIT_QTY_MMBTU),0) "
		  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC F "
		  				+ "WHERE F.COMPANY_CD=A.COMPANY_CD AND F.CONTRACT_TYPE=? "
		  				+ "AND F.GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND F.GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND F.EXIT_PT_MAPPING_ID='C-'||A.COUNTERPARTY_CD||'-'||D.PLANT_SEQ_NO "
						+ "AND F.SELL_CONT_MAP LIKE A.COUNTERPARTY_CD||'-'||A.CONTRACT_TYPE||'-'||A.AGMT_NO||'-%-'||A.CONT_NO||'-%' AND F.BU_SEQ=E.PLANT_SEQ_NO "
						+ "AND F.ALLOC_REV_NO=(SELECT MAX(B.ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
							+ "WHERE B.CONT_NO=F.CONT_NO AND B.AGMT_NO=F.AGMT_NO AND B.COMPANY_CD=F.COMPANY_CD AND B.COUNTERPARTY_CD=F.COUNTERPARTY_CD "
							+ "AND B.CONTRACT_TYPE=F.CONTRACT_TYPE AND B.SELL_CONT_MAP=F.SELL_CONT_MAP AND F.BU_SEQ=B.BU_SEQ "
							+ "AND B.GAS_DT=F.GAS_DT AND F.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND F.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID)) "
						+ "+ (SELECT NVL(SUM(QTY_MMBTU),0) FROM FMS_DAILY_ALLOCATION_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND D.PLANT_SEQ_NO=B.PLANT_SEQ AND E.PLANT_SEQ_NO=B.BU_SEQ "
						+ "AND B.GAS_DT >=TO_DATE(?,'DD/MM/YYYY') AND B.GAS_DT <=TO_DATE(?,'DD/MM/YYYY')"
						+ "AND B.NOM_REV_NO=(SELECT MAX(C.NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL C "
							+ "WHERE B.CONT_NO=C.CONT_NO AND B.AGMT_NO=C.AGMT_NO AND B.COMPANY_CD=C.COMPANY_CD AND B.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
							+ "AND B.TRANSPORTER_CD=C.TRANSPORTER_CD AND B.TRANS_SEQ=C.TRANS_SEQ AND B.PLANT_SEQ=C.PLANT_SEQ AND B.CONTRACT_TYPE=C.CONTRACT_TYPE AND B.BU_SEQ=C.BU_SEQ "
							+ "AND B.GAS_DT=C.GAS_DT AND B.CARGO_NO=C.CARGO_NO))"
						+ " ELSE "
						+ "(SELECT NVL(SUM(QTY_MMBTU),0) FROM FMS_DAILY_ALLOCATION_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND D.PLANT_SEQ_NO=B.PLANT_SEQ AND E.PLANT_SEQ_NO=B.BU_SEQ "
						+ "AND B.GAS_DT >=TO_DATE(?,'DD/MM/YYYY') AND B.GAS_DT <=TO_DATE(?,'DD/MM/YYYY')"
						+ "AND B.NOM_REV_NO=(SELECT MAX(C.NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL C "
							+ "WHERE B.CONT_NO=C.CONT_NO AND B.AGMT_NO=C.AGMT_NO AND B.COMPANY_CD=C.COMPANY_CD AND B.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
							+ "AND B.TRANSPORTER_CD=C.TRANSPORTER_CD AND B.TRANS_SEQ=C.TRANS_SEQ AND B.PLANT_SEQ=C.PLANT_SEQ AND B.CONTRACT_TYPE=C.CONTRACT_TYPE AND B.BU_SEQ=C.BU_SEQ "
							+ "AND B.GAS_DT=C.GAS_DT AND B.CARGO_NO=C.CARGO_NO)) END) > 0 "
						+ "ORDER BY (SELECT Z.COUNTERPARTY_ABBR FROM FMS_COUNTERPARTY_MST Z WHERE A.COUNTERPARTY_CD=Z.COUNTERPARTY_CD AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST Y WHERE Y.COUNTERPARTY_CD=Z.COUNTERPARTY_CD))";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, "Y");
				stmt.setString(3, period_end_dt);
				stmt.setString(4, period_start_dt);
				stmt.setString(5, billing_freq);
				stmt.setString(6, billing_flag);
				stmt.setString(7, period_end_dt);
				stmt.setString(8, "C");
				stmt.setString(9, period_start_dt);
				stmt.setString(10, period_end_dt);
				stmt.setString(11, period_start_dt);
				stmt.setString(12, period_end_dt);
				stmt.setString(13, period_start_dt);
				stmt.setString(14, period_end_dt);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String agmtno=rset.getString(3)==null?"0":rset.getString(3);
					String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					String cargo_no="0";
					String end_dt=rset.getString(8)==null?"":rset.getString(8);
					String contRef=rset.getString(10)==null?"":rset.getString(10);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					String tradeRef=rset.getString(12)==null?"":rset.getString(12);
					String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, "");
					
					if(cont_type.equals("X"))
					{
						contRef=tradeRef;
					}
					
					String billing_eff_dt=rset.getString(13)==null?"":rset.getString(13);
					String agmt_base=rset.getString(14)==null?"":rset.getString(14);
					String fcc=rset.getString(15)==null?"":rset.getString(15);
					
					String plant_seq = rset.getString(16)==null?"":rset.getString(16);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "C");
					
					String bu_plant_seq = rset.getString(17)==null?"":rset.getString(17);
					String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
						
					int isGreter=utilDate.getDays(billing_eff_dt, period_start_dt);
					
					String temp_st_dt="";
					String temp_end_dt="";
					String diff_color="";
					if(isGreter>1)
					{
						temp_st_dt=billing_eff_dt;
						temp_end_dt=period_end_dt;
						diff_color="blue";
					}
					else
					{
						temp_st_dt=period_start_dt;
						temp_end_dt=period_end_dt;
						diff_color="";
					}
					
					if(utilDate.getDays(end_dt, temp_end_dt)<=0)
					{
						temp_end_dt=end_dt;
						diff_color="blue";
					}
					
					String state_code=utilBean.getState_TIN(conn, own_cd, own_cd, "B", bu_plant_seq);
					
					int isInvExist=isInvoiceExist(own_cd,countpty_cd, contno, agmtno,cont_type, plant_seq, 
							bu_plant_seq, billing_cycle, temp_st_dt, temp_end_dt, state_code,cargo_no,"F");
					if(isInvExist > 0 && !fcc.equals("Y"))
					{
						fcc="Y";
					}
					
					//double qtyMMBTU=utilAlloc.getSupplyAllocationQty(own_cd, countpty_cd, agmtno, contno, cont_type, plant_seq, bu_plant_seq, period_start_dt, period_end_dt);
					double qtyMMBTU=0;
					
					if(agmt_base.equals("D"))
					{
						qtyMMBTU=utilAlloc.getDeliverdSupplyAllocationQty(conn, own_cd, countpty_cd, agmtno, contno, cont_type, plant_seq, bu_plant_seq, temp_st_dt, temp_end_dt);
					}
					else
					{
						qtyMMBTU=utilAlloc.getSupplyAllocationQty(conn, own_cd, countpty_cd, agmtno, contno, cont_type, plant_seq, bu_plant_seq, temp_st_dt, temp_end_dt,"0");
					}
					if((qtyMMBTU > 0 && fcc.equals("Y")) || (qtyMMBTU <= 0 && fcc.equals("Y") && agmt_base.equals("D"))) //ALLOW QTY IS <= 0 DLV BASE CONTRACT
					{
						inv_index=inv_index+1;
						
						VBU_STATE_TIN.add(state_code);							
						//VFINANCIAL_YEAR.add(financial_year);
						VCOUNTERPTY_CD.add(countpty_cd);
						VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
						VAGMT_NO.add(agmtno);
						VAGMT_REV_NO.add(agmtrev);
						VCONT_NO.add(contno);
						VCONT_REV_NO.add(contrev);
						VCARGO_NO.add(cargo_no);
						VSTART_DT.add(rset.getString(7)==null?"":rset.getString(7));
						VEND_DT.add(rset.getString(8)==null?"":rset.getString(8));
						VCONT_NAME.add(rset.getString(9)==null?"":rset.getString(9));
						VCONT_REF_NO.add(contRef);
						VCONTRACT_TYPE.add(cont_type);
						VDEAL_NO.add(deal_no);
						
						VPLANT_SEQ.add(plant_seq);
						VPLANT_ABBR.add(plant_abbr);
						VBU_PLANT_SEQ.add(bu_plant_seq);
						VBU_PLANT_ABBR.add(bu_plant_abbr);
						//VPERIOD_START_DT.add(period_start_dt);
						//VPERIOD_END_DT.add(period_end_dt);
						VPERIOD_START_DT.add(temp_st_dt);
						VPERIOD_END_DT.add(temp_end_dt);
						VTEMP_PERIOD_START_DT.add(temp_st_dt);
						VTEMP_PERIOD_END_DT.add(temp_end_dt);
						VALLOC_QTY.add(nf.format(qtyMMBTU));
						VAGMT_BASE.add(agmt_base);
						
						VSTATUS.add("");
						
						VBILLING_FREQ_FLAG.add(billing_cycle);
						VBILLING_FREQ_NM.add(billing_freq_nm);
						
						VDIFF_COLOR.add(diff_color); //FOR IS GREATER
						
						String inv_flag="F";
						VINV_FLAG.add(inv_flag);
						getInvDetailForPreparationList(own_cd,countpty_cd, contno, agmtno,cont_type, plant_seq, 
								bu_plant_seq, billing_cycle, temp_st_dt, temp_end_dt, state_code,cargo_no,inv_flag);
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
	
	public void getLTCORAInvoicePreparationList()
	{
		String function_nm="getLTCORAInvoicePreparationList()";
		try
		{
			if(billing_cycle.equals("8"))
			{
				String temp_period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
				String temp_period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
				
				queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),TO_CHAR(TO_DATE(B.ACTUAL_RECPT_DT,'DD/MM/YY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0),'DD/MM/YYYY'),"
						+ "A.CONT_NAME,B.CARGO_REF,A.CONTRACT_TYPE,B.CARGO_NO,D.PLANT_SEQ_NO,E.PLANT_SEQ_NO,A.FCC_FLAG,C.BILLING_DAYS "
						+ ""
						+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_CARGO_DTL B,"
							+ "FMS_LTCORA_CONT_BILLING_DTL C,"
							+ "FMS_LTCORA_CONT_BU D,"
							+ "FMS_LTCORA_CONT_PLANT E "
						+ "WHERE A.COMPANY_CD=? AND A.BUY_SALE=? AND A.FCC_FLAG=? AND B.CARGO_STATUS=? AND A.AGMT_TYPE=? "
						+ "AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0) >=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
						+ ""
						+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.CONT_NO=D.CONT_NO AND A.CONT_REV=D.CONT_REV "
						+ "AND A.AGMT_NO=D.AGMT_NO AND A.AGMT_REV=D.AGMT_REV AND A.CONTRACT_TYPE=D.CONTRACT_TYPE AND A.BUY_SALE=D.BUY_SALE AND A.AGMT_TYPE=D.AGMT_TYPE "
						+ ""
						+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO AND A.CONT_REV=E.CONT_REV "
						+ "AND A.AGMT_NO=E.AGMT_NO AND A.AGMT_REV=E.AGMT_REV AND A.CONTRACT_TYPE=E.CONTRACT_TYPE AND A.BUY_SALE=E.BUY_SALE AND A.AGMT_TYPE=E.AGMT_TYPE "
						+ ""
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE "
						+ ""
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "//AND A.CONT_REV=C.CONT_REV "
						+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.BUY_SALE=C.BUY_SALE AND A.AGMT_TYPE=C.AGMT_TYPE "
						+ "AND E.PLANT_SEQ_NO=C.PLANT_SEQ_NO AND C.BILLING_FREQ=? "
						+ ""
						+ "AND (SELECT NVL(SUM(QTY_MMBTU),0) FROM FMS_DAILY_ALLOCATION_DTL F WHERE A.COMPANY_CD=F.COMPANY_CD AND A.COUNTERPARTY_CD=F.COUNTERPARTY_CD "
						+ "AND A.CONT_NO=F.CONT_NO AND A.AGMT_NO=F.AGMT_NO AND A.CONTRACT_TYPE=F.CONTRACT_TYPE AND E.PLANT_SEQ_NO=F.PLANT_SEQ AND D.PLANT_SEQ_NO=F.BU_SEQ AND F.CARGO_NO=B.CARGO_NO "
						+ "AND F.GAS_DT >=TO_DATE(?,'DD/MM/YYYY') AND F.GAS_DT <=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL C "
							+ "WHERE F.CONT_NO=C.CONT_NO AND F.AGMT_NO=C.AGMT_NO AND F.COMPANY_CD=C.COMPANY_CD AND F.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
							+ "AND F.TRANSPORTER_CD=C.TRANSPORTER_CD AND F.TRANS_SEQ=C.TRANS_SEQ AND F.PLANT_SEQ=C.PLANT_SEQ AND F.CONTRACT_TYPE=C.CONTRACT_TYPE AND F.BU_SEQ=C.BU_SEQ "
							+ "AND F.GAS_DT=C.GAS_DT AND F.CARGO_NO=C.CARGO_NO)) > 0 "
						+ "ORDER BY (SELECT Z.COUNTERPARTY_ABBR FROM FMS_COUNTERPARTY_MST Z WHERE A.COUNTERPARTY_CD=Z.COUNTERPARTY_CD AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST Y WHERE Y.COUNTERPARTY_CD=Z.COUNTERPARTY_CD))";
				//HP20250402 REMOVING CONT_REV CHECKING FOR BILLING DETAIL FROM THE QUERY CONDITION AS CONFIRMED BY JD MAM
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, "C");
				stmt.setString(3, "Y");
				stmt.setString(4, "Y");
				stmt.setString(5, "A");
				stmt.setString(6, period_end_dt);
				stmt.setString(7, period_start_dt);
				stmt.setString(8, billing_freq);
				stmt.setString(9, period_start_dt);
				stmt.setString(10, period_end_dt);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String countpty_abbr=""+utilBean.getCounterpartyABBR(conn,countpty_cd);
					String agmtno=rset.getString(3)==null?"0":rset.getString(3);
					String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					String start_dt = rset.getString(7)==null?"":rset.getString(7);
					String end_dt = rset.getString(8)==null?"":rset.getString(8);
					String cont_ref_no=rset.getString(10)==null?"":rset.getString(10);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					String cargo_no=rset.getString(12)==null?"0":rset.getString(12);
					
					String bu_plant_seq = rset.getString(13)==null?"":rset.getString(13);
					String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
					
					String plant_seq=rset.getString(14)==null?"":rset.getString(14);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "C");
					
					String fcc=rset.getString(15)==null?"":rset.getString(15);
					String billing_days=rset.getString(16)==null?"1":rset.getString(16);
					
					//String deal_no=cont_type+""+contno;
					String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, cargo_no);
					
					String state_code=utilBean.getState_TIN(conn, own_cd, own_cd, "B", bu_plant_seq);
					
					System.out.println("\n"+countpty_abbr+" -- "+deal_no+" :: "+start_dt+" - "+end_dt+" :: Bill Eff "+start_dt+" :: "+temp_period_start_dt+" - "+temp_period_end_dt);
					
					String periodStartDate="";
					String periodEndDate="";
					
					int isGreter=utilDate.getDays(start_dt, temp_period_start_dt);
					if(isGreter>1)
					{
						periodStartDate=start_dt;
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
						
						/*NOT REQUIRED AT THIS POINT OF TIME
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
						*/
						
						int rem_checkMthEnd=utilDate.getDays(periodEndDate,utilDate.getDate(temp_end_dt,"1"));
						System.out.println(j+" - "+checkMthEnd+" : "+temp_st_dt+" :: "+temp_end_dt+" Rem Day : "+rem_checkMthEnd);
						tot_row+=1;
						
						int isInvExist=isInvoiceExist(own_cd,countpty_cd, contno, agmtno,cont_type, plant_seq, 
								bu_plant_seq, billing_cycle, temp_st_dt, temp_end_dt, state_code,cargo_no,"F");
						if(isInvExist > 0 && !fcc.equals("Y"))
						{
							fcc="Y";
						}
						
						String diff_color="blue";
					
						double qtyMMBTU=utilAlloc.getSupplyAllocationQty(conn, own_cd, countpty_cd, agmtno, contno, cont_type, plant_seq, bu_plant_seq, temp_st_dt, temp_end_dt,cargo_no);
						if((qtyMMBTU > 0 && fcc.equals("Y"))) 
						{
							inv_index=inv_index+1;
							
							VBU_STATE_TIN.add(state_code);							
							//VFINANCIAL_YEAR.add(financial_year);
							VCOUNTERPTY_CD.add(countpty_cd);
							VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
							VAGMT_NO.add(agmtno);
							VAGMT_REV_NO.add(agmtrev);
							VCONT_NO.add(contno);
							VCONT_REV_NO.add(contrev);
							VCARGO_NO.add(cargo_no);
							VSTART_DT.add(rset.getString(7)==null?"":rset.getString(7));
							VEND_DT.add(rset.getString(8)==null?"":rset.getString(8));
							VCONT_NAME.add(rset.getString(9)==null?"":rset.getString(9));
							VCONT_REF_NO.add(cont_ref_no);
							VCONTRACT_TYPE.add(cont_type);
							VDEAL_NO.add(deal_no);
							
							VPLANT_SEQ.add(plant_seq);
							VPLANT_ABBR.add(plant_abbr);
							VBU_PLANT_SEQ.add(bu_plant_seq);
							VBU_PLANT_ABBR.add(bu_plant_abbr);
							//VPERIOD_START_DT.add(period_start_dt);
							//VPERIOD_END_DT.add(period_end_dt);
							VPERIOD_START_DT.add(temp_st_dt);
							VPERIOD_END_DT.add(temp_end_dt);
							VTEMP_PERIOD_START_DT.add(temp_st_dt);
							VTEMP_PERIOD_END_DT.add(temp_end_dt);
							VALLOC_QTY.add(nf.format(qtyMMBTU));
							VAGMT_BASE.add(agmt_base);
							
							VSTATUS.add("");
							
							VBILLING_FREQ_FLAG.add(billing_cycle);
							VBILLING_FREQ_NM.add(billing_freq_nm);
							
							VDIFF_COLOR.add(diff_color); //FOR IS GREATER
							
							String inv_flag="F";
							VINV_FLAG.add(inv_flag);
							getInvDetailForPreparationList(own_cd,countpty_cd, contno, agmtno,cont_type, plant_seq, 
									bu_plant_seq, billing_cycle, temp_st_dt, temp_end_dt, state_code,cargo_no,inv_flag);
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
				queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),TO_CHAR(TO_DATE(B.ACTUAL_RECPT_DT,'DD/MM/YY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0),'DD/MM/YYYY'),"
						+ "A.CONT_NAME,B.CARGO_REF,A.CONTRACT_TYPE,B.CARGO_NO,D.PLANT_SEQ_NO,E.PLANT_SEQ_NO,A.FCC_FLAG "
						+ ""
						+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_CARGO_DTL B,"
							+ "FMS_LTCORA_CONT_BILLING_DTL C,"
							+ "FMS_LTCORA_CONT_BU D,"
							+ "FMS_LTCORA_CONT_PLANT E "
						+ "WHERE A.COMPANY_CD=? AND A.BUY_SALE=? AND A.FCC_FLAG=? AND B.CARGO_STATUS=? AND A.AGMT_TYPE=? "
						+ "AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0) >=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
						+ ""
						+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.CONT_NO=D.CONT_NO AND A.CONT_REV=D.CONT_REV "
						+ "AND A.AGMT_NO=D.AGMT_NO AND A.AGMT_REV=D.AGMT_REV AND A.CONTRACT_TYPE=D.CONTRACT_TYPE AND A.BUY_SALE=D.BUY_SALE AND A.AGMT_TYPE=D.AGMT_TYPE "
						+ ""
						+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO AND A.CONT_REV=E.CONT_REV "
						+ "AND A.AGMT_NO=E.AGMT_NO AND A.AGMT_REV=E.AGMT_REV AND A.CONTRACT_TYPE=E.CONTRACT_TYPE AND A.BUY_SALE=E.BUY_SALE AND A.AGMT_TYPE=E.AGMT_TYPE "
						+ ""
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE "
						+ ""
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "//AND A.CONT_REV=C.CONT_REV "
						+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.BUY_SALE=C.BUY_SALE AND A.AGMT_TYPE=C.AGMT_TYPE "
						+ "AND E.PLANT_SEQ_NO=C.PLANT_SEQ_NO AND C.BILLING_FREQ=? "
						+ ""
						+ "AND (SELECT NVL(SUM(QTY_MMBTU),0) FROM FMS_DAILY_ALLOCATION_DTL F WHERE A.COMPANY_CD=F.COMPANY_CD AND A.COUNTERPARTY_CD=F.COUNTERPARTY_CD "
						+ "AND A.CONT_NO=F.CONT_NO AND A.AGMT_NO=F.AGMT_NO AND A.CONTRACT_TYPE=F.CONTRACT_TYPE AND E.PLANT_SEQ_NO=F.PLANT_SEQ AND D.PLANT_SEQ_NO=F.BU_SEQ AND F.CARGO_NO=B.CARGO_NO "
						+ "AND F.GAS_DT >=TO_DATE(?,'DD/MM/YYYY') AND F.GAS_DT <=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL C "
							+ "WHERE F.CONT_NO=C.CONT_NO AND F.AGMT_NO=C.AGMT_NO AND F.COMPANY_CD=C.COMPANY_CD AND F.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
							+ "AND F.TRANSPORTER_CD=C.TRANSPORTER_CD AND F.TRANS_SEQ=C.TRANS_SEQ AND F.PLANT_SEQ=C.PLANT_SEQ AND F.CONTRACT_TYPE=C.CONTRACT_TYPE AND F.BU_SEQ=C.BU_SEQ "
							+ "AND F.GAS_DT=C.GAS_DT AND F.CARGO_NO=C.CARGO_NO)) > 0 "
						+ "ORDER BY (SELECT Z.COUNTERPARTY_ABBR FROM FMS_COUNTERPARTY_MST Z WHERE A.COUNTERPARTY_CD=Z.COUNTERPARTY_CD AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST Y WHERE Y.COUNTERPARTY_CD=Z.COUNTERPARTY_CD))";
				//HP20250402 REMOVING CONT_REV CHECKING FOR BILLING DETAIL FROM THE QUERY CONDITION AS CONFIRMED BY JD MAM
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, "C");
				stmt.setString(3, "Y");
				stmt.setString(4, "Y");
				stmt.setString(5, "A");
				stmt.setString(6, period_end_dt);
				stmt.setString(7, period_start_dt);
				stmt.setString(8, billing_freq);
				stmt.setString(9, period_start_dt);
				stmt.setString(10, period_end_dt);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String countpty_abbr=""+utilBean.getCounterpartyABBR(conn,countpty_cd);
					String agmtno=rset.getString(3)==null?"0":rset.getString(3);
					String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					String start_dt = rset.getString(7)==null?"":rset.getString(7);
					String end_dt = rset.getString(8)==null?"":rset.getString(8);
					String cont_ref_no=rset.getString(10)==null?"":rset.getString(10);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					String cargo_no=rset.getString(12)==null?"0":rset.getString(12);
					
					String bu_plant_seq = rset.getString(13)==null?"":rset.getString(13);
					String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
					
					String plant_seq=rset.getString(14)==null?"":rset.getString(14);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "C");
					
					String fcc=rset.getString(15)==null?"":rset.getString(15);
					
					//String deal_no=cont_type+""+contno;
					String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, cargo_no);
					
					//int isGreter=utilDate.getDays(billing_eff_dt, period_start_dt);
					int isGreter=utilDate.getDays(start_dt, period_start_dt);
					
					String temp_st_dt="";
					String temp_end_dt="";
					String diff_color="";
					if(isGreter>1)
					{
						//temp_st_dt=billing_eff_dt;
						temp_st_dt=start_dt;
						temp_end_dt=period_end_dt;
						diff_color="blue";
					}
					else
					{
						temp_st_dt=period_start_dt;
						temp_end_dt=period_end_dt;
						diff_color="";
					}
					
					if(utilDate.getDays(end_dt, temp_end_dt)<=0)
					{
						temp_end_dt=end_dt;
						diff_color="blue";
					}
					
					String state_code=utilBean.getState_TIN(conn, own_cd, own_cd, "B", bu_plant_seq);
					
					String max_dt="";
					int max_count=0;
					queryString2="SELECT TO_CHAR(MAX(PERIOD_END_DT),'DD/MM/YYYY'),COUNT(*) "
							+ "FROM FMS_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_UNIT=? "
							+ "AND FREQ=? "
							+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT < TO_DATE(?,'DD/MM/YYYY') "
							+ "AND BU_STATE_TIN=? AND CARGO_NO=? AND INV_FLAG=? ";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, own_cd);
					stmt2.setString(2, countpty_cd);
					stmt2.setString(3, contno);
					stmt2.setString(4, agmtno);
					stmt2.setString(5, plant_seq);
					stmt2.setString(6, cont_type);
					stmt2.setString(7, bu_plant_seq);
					stmt2.setString(8, billing_cycle);
					stmt2.setString(9, temp_st_dt);
					stmt2.setString(10, temp_end_dt);
					stmt2.setString(11, state_code);
					stmt2.setString(12, cargo_no);
					stmt2.setString(13, "F");
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						max_dt=rset2.getString(1)==null?"":rset2.getString(1);
						max_count=rset2.getInt(2);
					}
					rset2.close();
					stmt2.close();
					
					
					Vector VTEMP_ST_DT=new Vector();
					Vector VTEMP_END_DT=new Vector();
					if(max_count > 0)
					{
						String dt=utilDate.getDate(max_dt, "1");
						
						VTEMP_ST_DT.add(temp_st_dt);
						VTEMP_END_DT.add(max_dt);
						
						VTEMP_ST_DT.add(dt);
						VTEMP_END_DT.add(temp_end_dt);
					}
					else
					{
						VTEMP_ST_DT.add(temp_st_dt);
						VTEMP_END_DT.add(temp_end_dt);
					}
					
					for(int i=0; i<VTEMP_ST_DT.size(); i++)
					{
						temp_st_dt=""+VTEMP_ST_DT.elementAt(i);
						temp_end_dt=""+VTEMP_END_DT.elementAt(i);
						
						int isInvExist=isInvoiceExist(own_cd,countpty_cd, contno, agmtno,cont_type, plant_seq, 
								bu_plant_seq, billing_cycle, temp_st_dt, temp_end_dt, state_code,cargo_no,"F");
						if(isInvExist > 0 && !fcc.equals("Y"))
						{
							fcc="Y";
						}
						
						double qtyMMBTU=utilAlloc.getSupplyAllocationQty(conn, own_cd, countpty_cd, agmtno, contno, cont_type, plant_seq, bu_plant_seq, temp_st_dt, temp_end_dt,cargo_no);
						if((qtyMMBTU > 0 && fcc.equals("Y"))) 
						{
							inv_index=inv_index+1;
							
							VBU_STATE_TIN.add(state_code);							
							//VFINANCIAL_YEAR.add(financial_year);
							VCOUNTERPTY_CD.add(countpty_cd);
							VCOUNTERPTY_ABBR.add(countpty_abbr);
							VAGMT_NO.add(agmtno);
							VAGMT_REV_NO.add(agmtrev);
							VCONT_NO.add(contno);
							VCONT_REV_NO.add(contrev);
							VCARGO_NO.add(cargo_no);
							VSTART_DT.add(start_dt);
							VEND_DT.add(end_dt);
							VCONT_NAME.add(rset.getString(9)==null?"":rset.getString(9));
							VCONT_REF_NO.add(cont_ref_no);
							VCONTRACT_TYPE.add(cont_type);
							VDEAL_NO.add(deal_no);
							
							VPLANT_SEQ.add(plant_seq);
							VPLANT_ABBR.add(plant_abbr);
							VBU_PLANT_SEQ.add(bu_plant_seq);
							VBU_PLANT_ABBR.add(bu_plant_abbr);
							//VPERIOD_START_DT.add(period_start_dt);
							//VPERIOD_END_DT.add(period_end_dt);
							VPERIOD_START_DT.add(temp_st_dt);
							VPERIOD_END_DT.add(temp_end_dt);
							VTEMP_PERIOD_START_DT.add(temp_st_dt);
							VTEMP_PERIOD_END_DT.add(temp_end_dt);
							VALLOC_QTY.add(nf.format(qtyMMBTU));
							VAGMT_BASE.add(agmt_base);
							
							VSTATUS.add("");
							
							VBILLING_FREQ_FLAG.add(billing_cycle);
							VBILLING_FREQ_NM.add(billing_freq_nm);
							
							VDIFF_COLOR.add(diff_color); //FOR IS GREATER
							
							String inv_flag="F";
							VINV_FLAG.add(inv_flag);
							getInvDetailForPreparationList(own_cd,countpty_cd, contno, agmtno,cont_type, plant_seq, 
									bu_plant_seq, billing_cycle, temp_st_dt, temp_end_dt, state_code,cargo_no,inv_flag);
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
	
	public void getTCQCompletionSalesInvoicePreparationList()
	{
		String function_nm="getTCQCompletionSalesInvoicePreparationList()";

		try
		{
			String allocDate="(SELECT MAX(GAS_DT) FROM FMS_DAILY_ALLOCATION_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL C "
						+ "WHERE B.CONT_NO=C.CONT_NO AND B.AGMT_NO=C.AGMT_NO AND B.COMPANY_CD=C.COMPANY_CD AND B.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=C.TRANSPORTER_CD AND B.TRANS_SEQ=C.TRANS_SEQ AND B.PLANT_SEQ=C.PLANT_SEQ AND B.CONTRACT_TYPE=C.CONTRACT_TYPE AND B.BU_SEQ=C.BU_SEQ "
						+ "AND B.GAS_DT=C.GAS_DT AND B.CARGO_NO=C.CARGO_NO))";
			
			String plantWiseAllocMMBTUQry="(SELECT NVL(SUM(QTY_MMBTU),0) FROM FMS_DAILY_ALLOCATION_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND D.PLANT_SEQ_NO=B.PLANT_SEQ AND E.PLANT_SEQ_NO=B.BU_SEQ "
					+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL C "
						+ "WHERE B.CONT_NO=C.CONT_NO AND B.AGMT_NO=C.AGMT_NO AND B.COMPANY_CD=C.COMPANY_CD AND B.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=C.TRANSPORTER_CD AND B.TRANS_SEQ=C.TRANS_SEQ AND B.PLANT_SEQ=C.PLANT_SEQ AND B.CONTRACT_TYPE=C.CONTRACT_TYPE AND B.BU_SEQ=C.BU_SEQ "
						+ "AND B.GAS_DT=C.GAS_DT AND B.CARGO_NO=C.CARGO_NO)) ";
			
			String contWiseAllocMMBTUQry="(SELECT NVL(SUM(QTY_MMBTU),0) FROM FMS_DAILY_ALLOCATION_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL C "
						+ "WHERE B.CONT_NO=C.CONT_NO AND B.AGMT_NO=C.AGMT_NO AND B.COMPANY_CD=C.COMPANY_CD AND B.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=C.TRANSPORTER_CD AND B.TRANS_SEQ=C.TRANS_SEQ AND B.PLANT_SEQ=C.PLANT_SEQ AND B.CONTRACT_TYPE=C.CONTRACT_TYPE AND B.BU_SEQ=C.BU_SEQ "
						+ "AND B.GAS_DT=C.GAS_DT AND B.CARGO_NO=C.CARGO_NO)) ";
			
			queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
					+ "TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.CONT_NAME,A.CONT_REF_NO,A.CONTRACT_TYPE,"
					+ "A.TRADE_REF_NO,TO_CHAR(C.EFF_DT,'DD/MM/YYYY'),A.AGMT_BASE,A.FCC_FLAG,D.PLANT_SEQ_NO,E.PLANT_SEQ_NO,TO_CHAR("+allocDate+",'DD/MM/YYYY') "
					+ "FROM FMS_SUPPLY_CONT_MST A, "
						+ "FMS_SUPPLY_BILLING_DTL C, "
						+ "FMS_SUPPLY_CONT_PLANT D, "
						+ "FMS_SUPPLY_CONT_BU E "
					+ "WHERE A.COMPANY_CD=? AND A.IS_ALLOCATED=? " //AND A.FCC_FLAG='Y' " REMOVED AS DISCUSSED WITH MAM
					+ "AND A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND "+allocDate+">=TO_DATE(?,'DD/MM/YYYY') AND A.CONTRACT_TYPE IN ('S','L','X') "
					+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
					+ ""
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "
					+ "AND A.AGMT_NO=C.AGMT_NO AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND D.PLANT_SEQ_NO=C.PLANT_SEQ_NO AND C.BILLING_FLAG=? "
					+ "AND C.EFF_DT=(SELECT MAX(E.EFF_DT) FROM FMS_SUPPLY_BILLING_DTL E WHERE C.COMPANY_CD=E.COMPANY_CD "
						+ "AND C.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND C.AGMT_NO=E.AGMT_NO AND C.CONT_NO=E.CONT_NO "
						+ "AND C.CONTRACT_TYPE=E.CONTRACT_TYPE AND E.PLANT_SEQ_NO=C.PLANT_SEQ_NO AND E.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) "
					+ ""
					+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.CONT_NO=D.CONT_NO AND A.CONT_REV=D.CONT_REV "
					+ "AND A.AGMT_NO=D.AGMT_NO AND A.AGMT_REV=D.AGMT_REV AND A.CONTRACT_TYPE=D.CONTRACT_TYPE "
					+ ""
					+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO AND A.CONT_REV=E.CONT_REV "
					+ "AND A.AGMT_NO=E.AGMT_NO AND A.AGMT_REV=E.AGMT_REV AND A.CONTRACT_TYPE=E.CONTRACT_TYPE "
					+ ""
					+ "AND "+plantWiseAllocMMBTUQry+" > 0 "
					+ "AND "+contWiseAllocMMBTUQry+" >= A.TCQ "
					+ "ORDER BY (SELECT Z.COUNTERPARTY_ABBR FROM FMS_COUNTERPARTY_MST Z WHERE A.COUNTERPARTY_CD=Z.COUNTERPARTY_CD AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST Y WHERE Y.COUNTERPARTY_CD=Z.COUNTERPARTY_CD))";
			String temp_queryString=queryString;
			stmt=conn.prepareStatement(temp_queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "Y");
			stmt.setString(3, period_end_dt);
			stmt.setString(4, period_start_dt);
			stmt.setString(5, billing_flag);
			stmt.setString(6, period_end_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String agmtno=rset.getString(3)==null?"0":rset.getString(3);
				String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
				String contno=rset.getString(5)==null?"0":rset.getString(5);
				String contrev=rset.getString(6)==null?"0":rset.getString(6);
				String cargo_no="0";
				String st_dt=rset.getString(7)==null?"":rset.getString(7);
				String end_dt=rset.getString(8)==null?"":rset.getString(8);
				String contRef=rset.getString(10)==null?"":rset.getString(10);
				String cont_type=rset.getString(11)==null?"":rset.getString(11);
				String tradeRef=rset.getString(12)==null?"":rset.getString(12);
				String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, "");
				
				if(cont_type.equals("X"))
				{
					contRef=tradeRef;
				}
				
				String billing_eff_dt=rset.getString(13)==null?"":rset.getString(13);
				String agmt_base=rset.getString(14)==null?"":rset.getString(14);
				String fcc=rset.getString(15)==null?"":rset.getString(15);
				
				String plant_seq = rset.getString(16)==null?"":rset.getString(16);
				String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "C");
				
				String bu_plant_seq = rset.getString(17)==null?"":rset.getString(17);
				String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
				
				String billPeriodSt_dt=st_dt;
				String billPeriodEnd_dt=rset.getString(18)==null?"":rset.getString(18);
					
				int isGreter=utilDate.getDays(billing_eff_dt, period_start_dt);
				
				String temp_st_dt=billPeriodSt_dt;
				String temp_end_dt=billPeriodEnd_dt;
				String state_code=utilBean.getState_TIN(conn, own_cd, own_cd, "B", bu_plant_seq);
				
				int isInvExist=isInvoiceExist(own_cd,countpty_cd, contno, agmtno,cont_type, plant_seq, 
						bu_plant_seq, billing_cycle, temp_st_dt, temp_end_dt, state_code,cargo_no,"F");
				if(isInvExist > 0 && !fcc.equals("Y"))
				{
					fcc="Y";
				}
				
				double qtyMMBTU=0;
				
				if(agmt_base.equals("D"))
				{
					qtyMMBTU=utilAlloc.getDeliverdSupplyAllocationQty(conn, own_cd, countpty_cd, agmtno, contno, cont_type, plant_seq, bu_plant_seq);
				}
				else
				{
					qtyMMBTU=utilAlloc.getSupplyAllocationQty(conn, own_cd, countpty_cd, agmtno, contno, cont_type, plant_seq, bu_plant_seq, temp_st_dt, temp_end_dt,"0");
				}
				if((qtyMMBTU > 0 && fcc.equals("Y")) || (qtyMMBTU <= 0 && fcc.equals("Y") && agmt_base.equals("D"))) //ALLOW QTY IS <= 0 DLV BASE CONTRACT
				{
					inv_index=inv_index+1;
					
					VBU_STATE_TIN.add(state_code);							
					VCOUNTERPTY_CD.add(countpty_cd);
					VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
					VAGMT_NO.add(agmtno);
					VAGMT_REV_NO.add(agmtrev);
					VCONT_NO.add(contno);
					VCONT_REV_NO.add(contrev);
					VCARGO_NO.add(cargo_no);
					VSTART_DT.add(st_dt);
					VEND_DT.add(end_dt);
					VCONT_NAME.add(rset.getString(9)==null?"":rset.getString(9));
					VCONT_REF_NO.add(contRef);
					VCONTRACT_TYPE.add(cont_type);
					VDEAL_NO.add(deal_no);
					
					VPLANT_SEQ.add(plant_seq);
					VPLANT_ABBR.add(plant_abbr);
					VBU_PLANT_SEQ.add(bu_plant_seq);
					VBU_PLANT_ABBR.add(bu_plant_abbr);
					VPERIOD_START_DT.add(temp_st_dt);
					VPERIOD_END_DT.add(temp_end_dt);
					VTEMP_PERIOD_START_DT.add(temp_st_dt);
					VTEMP_PERIOD_END_DT.add(temp_end_dt);
					VALLOC_QTY.add(nf.format(qtyMMBTU));
					VAGMT_BASE.add(agmt_base);
					
					VSTATUS.add("");
					
					VBILLING_FREQ_FLAG.add(billing_cycle);
					VBILLING_FREQ_NM.add(billing_freq_nm);
					
					VDIFF_COLOR.add(""); //FOR IS GREATER
					
					String inv_flag="F";
					VINV_FLAG.add(inv_flag);
					getInvDetailForPreparationList(own_cd,countpty_cd, contno, agmtno,cont_type, plant_seq, 
							bu_plant_seq, billing_cycle, temp_st_dt, temp_end_dt, state_code,cargo_no,inv_flag);
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
	
	public int isInvoiceExist(String own_cd,String countpty_cd, String contno, String agmtno,String cont_type, String plant_seq, 
			String bu_plant_seq, String billing_cycle, String period_start_dt, String period_end_dt,String state_code,String cargo_no,String invFlag)
	{
		String function_nm="isInvoiceExist()";
		int isInvExist=0;
		try
		{
			queryString4="SELECT COUNT(*) "
					+ "FROM FMS_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND AGMT_NO=? AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_UNIT=? "
					+ "AND FREQ=? AND BU_STATE_TIN=? AND CARGO_NO=? ";
			if(!billing_cycle.equals("11"))
			{
				queryString4+="AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') ";
			} 
			queryString4+= "AND PDF_INV_DTL IS NOT NULL AND INV_FLAG IN (?) "; //AND FINANCIAL_YEAR=? 
			int cnt=0;
			stmt4=conn.prepareStatement(queryString4);
			stmt4.setString(++cnt, own_cd);
			stmt4.setString(++cnt, countpty_cd);
			stmt4.setString(++cnt, contno);
			stmt4.setString(++cnt, agmtno);
			stmt4.setString(++cnt, plant_seq);
			stmt4.setString(++cnt, cont_type);
			stmt4.setString(++cnt, bu_plant_seq);
			stmt4.setString(++cnt, billing_cycle);
			stmt4.setString(++cnt, state_code);
			stmt4.setString(++cnt, cargo_no);
			if(!billing_cycle.equals("11"))
			{
				stmt4.setString(++cnt, period_start_dt);
				stmt4.setString(++cnt, period_end_dt);
			}
			stmt4.setString(++cnt, invFlag);
			//stmt4.setString(12, financial_year);
			
			rset4=stmt4.executeQuery();
			if(rset4.next())
			{
				isInvExist=rset4.getInt(1);
			}
			rset4.close();
			stmt4.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return isInvExist;
	}

	public void getInvDetailForPreparationList(String own_cd,String countpty_cd, String contno, String agmtno,String cont_type, String plant_seq, 
			String bu_plant_seq, String billing_cycle, String period_start_dt, String period_end_dt,String state_code,String cargo_no, String inv_flag)
	{
		String function_nm="getInvDetailForPreparationList()";
		try
		{
			String inv_no="";
			String inv_seq="";
			String checked_flag="";
			String approved_flag="";
			String pdf_flg="";
			String pdf_ori="";
			String pdf_tri="";
			String pdf_dup="";
			String sap_approved_flag="";
			String fiscal_yr="";
			String irn_no="";
			String inv_dt="";
			
			queryString5="SELECT INVOICE_NO,CHECKED_FLAG,APPROVED_FLAG,INVOICE_SEQ,PDF_INV_DTL,"
					+ "PRINT_BY_ORI,PRINT_BY_TRI,PRINT_BY_DUP,SAP_APPROVAL,FINANCIAL_YEAR,TO_CHAR(INVOICE_DT,'DD/MM/YYYY') "
					+ "FROM FMS_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND AGMT_NO=? AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_UNIT=? "
					+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_STATE_TIN=? AND CARGO_NO=? AND INV_FLAG=? ";
					//+ "AND FINANCIAL_YEAR=?";
			stmt5=conn.prepareStatement(queryString5);
			stmt5.setString(1, own_cd);
			stmt5.setString(2, countpty_cd);
			stmt5.setString(3, contno);
			stmt5.setString(4, agmtno);
			stmt5.setString(5, plant_seq);
			stmt5.setString(6, cont_type);
			stmt5.setString(7, bu_plant_seq);
			stmt5.setString(8, billing_cycle);
			stmt5.setString(9, period_start_dt);
			stmt5.setString(10, period_end_dt);
			stmt5.setString(11, state_code);
			//stmt5.setString(12, financial_year);
			stmt5.setString(12, cargo_no);
			stmt5.setString(13, inv_flag);
			rset5=stmt5.executeQuery();
			if(rset5.next())
			{
				inv_no=rset5.getString(1)==null?"":rset5.getString(1);
				checked_flag=rset5.getString(2)==null?"":rset5.getString(2);
				approved_flag=rset5.getString(3)==null?"":rset5.getString(3);
				inv_seq=rset5.getString(4)==null?"":rset5.getString(4);
				pdf_flg=rset5.getString(5)==null?"":rset5.getString(5);
				pdf_ori=rset5.getString(6)==null?"":rset5.getString(6);
				pdf_tri=rset5.getString(7)==null?"":rset5.getString(7);
				pdf_dup=rset5.getString(8)==null?"":rset5.getString(8);
				sap_approved_flag=rset5.getString(9)==null?"":rset5.getString(9);
				fiscal_yr=rset5.getString(10)==null?"":rset5.getString(10);
				inv_dt=rset5.getString(11)==null?"":rset5.getString(11);
				
				VINVOICE_EXIST.add("Y");
			}
			else
			{
				VINVOICE_EXIST.add("N");
			}
			rset5.close();
			stmt5.close();
			
			VINVOICE_DT.add(inv_dt);
			
			if(cont_type.equals("O") || cont_type.equals("Q"))
			{
				queryString5="SELECT IRN_NO "
						+ "FROM FMS_INVOICE_IRN_DTL "
						+ "WHERE COMPANY_CD=? AND INVOICE_NO=? ";
				stmt5=conn.prepareStatement(queryString5);
				stmt5.setString(1, comp_cd);
				stmt5.setString(2, inv_no);
				rset5=stmt5.executeQuery();
				if(rset5.next())
				{
					irn_no=rset5.getString(1)==null?"":rset5.getString(1);
				}
				rset5.close();
				stmt5.close();
				
				VIS_IRN_GENERATED.add(irn_no.equals("")?"N":"Y");
			}
			else
			{
				VIS_IRN_GENERATED.add("Y"); //other then LTCORA, Default 'Y'
			}
			
			
			VINVOICE_NO.add(inv_no);
			VINVOICE_SEQ.add(inv_seq);
			VINV_CHECKED_FLAG.add(checked_flag);
			VINV_APPROVED_FLAG.add(approved_flag);
			VPDF_INV_FLAG.add(pdf_flg);
			VSAP_APPROVAL_FLAG.add(sap_approved_flag);
			VFINANCIAL_YEAR.add(fiscal_yr);
			
			if(print_pdf_type.equals("O") && !pdf_ori.equals(""))
			{
				VPDF_TYPE.add(print_pdf_type);
			}
			else if(print_pdf_type.equals("D") && !pdf_dup.equals(""))
			{
				VPDF_TYPE.add(print_pdf_type);
			}
			else if(print_pdf_type.equals("T") && !pdf_tri.equals(""))
			{
				VPDF_TYPE.add(print_pdf_type);
			}
			else if(print_pdf_type.equals("All"))
			{
				String allPdfType="";
				allPdfType+=pdf_ori.equals("")?allPdfType.equals("")?"O":",O":"";
				allPdfType+=pdf_dup.equals("")?allPdfType.equals("")?"D":",D":"";
				allPdfType+=pdf_tri.equals("")?allPdfType.equals("")?"T":",T":"";
				
				if(allPdfType.equals(""))
				{
					allPdfType="All";
				}
				VPDF_TYPE.add(allPdfType);
			}
			else
			{
				VPDF_TYPE.add("");
			}
			
			if(view_pdf_type.equals("All"))
			{
				queryString6="SELECT COUNT(*) "
						+ "FROM FMS_INV_FILE_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
	 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
	 	        		+ "AND PDF_TYPE IN ('O','T','D') ";
				stmt6=conn.prepareStatement(queryString6);
				stmt6.setString(1, own_cd);
				stmt6.setString(2, state_code);
				stmt6.setString(3, inv_seq);
				//stmt6.setString(4, financial_year);
				stmt6.setString(4, fiscal_yr);
				rset6=stmt6.executeQuery();
				if(rset6.next())
				{
					if(rset6.getInt(1)>0)
					{
						VPDF_FILE_NAME.add("All");
						VPDF_FILE_PATH.add("");
					}
					else
					{
						VPDF_FILE_NAME.add("");
						VPDF_FILE_PATH.add("");
					}
				}
				else
				{
					VPDF_FILE_NAME.add("");
					VPDF_FILE_PATH.add("");
				}
				rset6.close();
				stmt6.close();
			}
			else
			{
				queryString6="SELECT FILE_NAME,PDF_SIGNED "
						+ "FROM FMS_INV_FILE_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
	 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
	 	        		+ "AND PDF_TYPE=?";
				stmt6=conn.prepareStatement(queryString6);
				stmt6.setString(1, own_cd);
				stmt6.setString(2, state_code);
				stmt6.setString(3, inv_seq);
				//stmt6.setString(4, financial_year);
				stmt6.setString(4, fiscal_yr);
				stmt6.setString(5, view_pdf_type);
				rset6=stmt6.executeQuery();
				if(rset6.next())
				{
					String pdf_signed=rset6.getString(2)==null?"":rset6.getString(2);
					
					VPDF_FILE_NAME.add(rset6.getString(1)==null?"":rset6.getString(1));
					if(pdf_signed.equals("Y"))
					{
						VPDF_FILE_PATH.add(CommonVariable.signed_sales_inv_path);
					}
					else
					{
						VPDF_FILE_PATH.add(CommonVariable.sales_inv_path);
					}
				}
				else
				{
					VPDF_FILE_NAME.add("");
					VPDF_FILE_PATH.add("");
				}
				rset6.close();
				stmt6.close();
			}
			
			if(mail_pdf_type.equals("All"))
			{
				String AllMailPdf="";
				queryString6="SELECT PDF_TYPE "
						+ "FROM FMS_INV_FILE_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
	 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
	 	        		+ "AND PDF_SIGNED=? AND PDF_TYPE IN ('O','T','D') ";
				stmt6=conn.prepareStatement(queryString6);
				stmt6.setString(1, own_cd);
				stmt6.setString(2, state_code);
				stmt6.setString(3, inv_seq);
				//stmt6.setString(4, financial_year);
				stmt6.setString(4, fiscal_yr);
				stmt6.setString(5, "Y");
				rset6=stmt6.executeQuery();
				while(rset6.next())
				{
					String temp=rset6.getString(1)==null?"":rset6.getString(1);
					if(AllMailPdf.equals(""))
					{
						AllMailPdf=temp;
					}
					else
					{
						AllMailPdf+=","+temp;
					}
					
				}
				rset6.close();
				stmt6.close();
				
				VPDF_SIGNED_FLAG.add("All");
				VSIGN_PDF_TYPE.add(AllMailPdf);
			}
			else
			{
				queryString6="SELECT FILE_NAME,PDF_SIGNED "
						+ "FROM FMS_INV_FILE_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
	 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
	 	        		+ "AND PDF_TYPE=?";
				stmt6=conn.prepareStatement(queryString6);
				stmt6.setString(1, own_cd);
				stmt6.setString(2, state_code);
				stmt6.setString(3, inv_seq);
				//stmt6.setString(4, financial_year);
				stmt6.setString(4, fiscal_yr);
				stmt6.setString(5, view_pdf_type);
				rset6=stmt6.executeQuery();
				if(rset6.next())
				{
					String pdf_signed=rset6.getString(2)==null?"":rset6.getString(2);
					VPDF_SIGNED_FLAG.add(pdf_signed);
					if(pdf_signed.equals("Y"))
					{
						VSIGN_PDF_TYPE.add(mail_pdf_type);
					}
					else
					{
						VSIGN_PDF_TYPE.add("");
					}
				}
				else
				{
					VPDF_SIGNED_FLAG.add("");
					VSIGN_PDF_TYPE.add("");
				}
				rset6.close();
				stmt6.close();
			}
			
			int re_print_count=isRePrintPDFRequested(comp_cd,state_code,inv_seq,fiscal_yr,"RLNG","REPRINT_PDF","A");
			VRE_PRINT_PDF.add(re_print_count>0?"Y":"N");
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getFFlowInvoiceList()
	{
		String function_nm="getFFlowInvoiceList()";
		try
		{
			/*if(billing_cycle.equals("8"))
			{
				period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
				period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
			}*/
			
			String temp_period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
			String temp_period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
			
			queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE, "
					+ "ADDR_FLAG,BU_UNIT,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),"
					+ "INVOICE_NO,CHECKED_FLAG,APPROVED_FLAG,INVOICE_SEQ,FINANCIAL_YEAR,INVOICE_TYPE,"
					+ "PDF_INV_DTL,BU_STATE_TIN, "
					+ "PRINT_BY_ORI,PRINT_BY_TRI,PRINT_BY_DUP,SAP_APPROVAL,CARGO_NO,INVOICE_CATEGORY,TAX_STRUCT_CD,TO_CHAR(INVOICE_DT,'DD/MM/YYYY') "
					+ "FROM FMS_FFLOW_INV_MST A "
					+ "WHERE COMPANY_CD=? ";//AND FREQ=? ";
			/*if(billing_cycle.equals("8"))
			{*/
				queryString+=" AND TO_DATE(?,'DD/MM/YYYY') <=INVOICE_DT AND TO_DATE(?,'DD/MM/YYYY') >= INVOICE_DT ";
			/*}
			else
			{
				queryString+=" AND PERIOD_START_DT =TO_DATE('"+period_start_dt+"','DD/MM/YYYY') AND PERIOD_END_DT =TO_DATE('"+period_end_dt+"','DD/MM/YYYY') ";
			}*/
				queryString+="ORDER BY (SELECT Z.COUNTERPARTY_ABBR FROM FMS_COUNTERPARTY_MST Z WHERE A.COUNTERPARTY_CD=Z.COUNTERPARTY_CD AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST Y WHERE Y.COUNTERPARTY_CD=Z.COUNTERPARTY_CD))";
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
				String cargo_no=rset.getString(24)==null?"":rset.getString(24);
				String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, cargo_no);
				
				String address_flag=rset.getString(8)==null?"":rset.getString(8);
				String plant_seq = "";
				String plant_abbr = "";
				if(address_flag.equals("R"))
				{
					plant_seq=address_flag;
					plant_abbr="Registered";
				}
				else if(address_flag.equals("C"))
				{
					plant_seq=address_flag;
					plant_abbr="Correspondence";
				}
				else if(address_flag.equals("B"))
				{
					plant_seq=address_flag;
					plant_abbr="Billing";
				}
				else if(!address_flag.equals("") && address_flag.startsWith("P"))
				{
					plant_seq=address_flag.substring(1,address_flag.length());
					plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "C");
				}

				String bu_plant_seq = rset.getString(9)==null?"":rset.getString(9);
				String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");

				String p_st_dt=rset.getString(10)==null?"":rset.getString(10);
				String p_end_dt=rset.getString(11)==null?"":rset.getString(11);
				String inv_dt=rset.getString(27)==null?"":rset.getString(27);

				VOTH_COUNTERPTY_CD.add(countpty_cd);
				VOTH_COUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VOTH_AGMT_NO.add(agmtno);
				VOTH_AGMT_REV_NO.add(agmtrev);
				VOTH_CONT_NO.add(contno);
				VOTH_CONT_REV_NO.add(contrev);

				VOTH_CONTRACT_TYPE.add(cont_type);
				VOTH_DEAL_NO.add(deal_no);

				VOTH_PLANT_SEQ.add(address_flag);
				VOTH_PLANT_ABBR.add(plant_abbr);
				VOTH_BU_PLANT_SEQ.add(bu_plant_seq);
				VOTH_BU_PLANT_ABBR.add(bu_plant_abbr);
				VOTH_PERIOD_START_DT.add(p_st_dt);
				VOTH_PERIOD_END_DT.add(p_end_dt);
				VOTH_INVOICE_DT.add(inv_dt);
				VOTH_BILLING_FREQ_FLAG.add(billing_cycle);
				VOTH_BILLING_FREQ_NM.add(billing_freq_nm);

				String max_contrev="0";

				queryString1="SELECT TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_REF_NO,TRADE_REF_NO,CONT_REV "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
						+ "AND AGMT_REV=? AND CONT_NO=? AND CONTRACT_TYPE=? "
						+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE B.COMPANY_CD=A.COMPANY_CD "
						+ "AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD AND B.AGMT_NO=A.AGMT_NO AND B.AGMT_REV=A.AGMT_REV "
						+ "AND B.CONT_NO=A.CONT_NO AND B.CONTRACT_TYPE=A.CONTRACT_TYPE) ";
				queryString1+="UNION ALL ";
				queryString1+="SELECT TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),TO_CHAR(TO_DATE(B.ACTUAL_RECPT_DT,'DD/MM/YY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0),'DD/MM/YYYY'),"
						+ "A.CONT_NAME,B.CARGO_REF,NULL,A.CONT_REV "
						+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_CARGO_DTL B "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
						+ "AND B.CARGO_NO=? AND A.BUY_SALE=? AND A.AGMT_TYPE=? "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, own_cd);
				stmt1.setString(2, countpty_cd);
				stmt1.setString(3, agmtno);
				stmt1.setString(4, agmtrev);
				stmt1.setString(5, contno);
				stmt1.setString(6, cont_type);
				stmt1.setString(7, own_cd);
				stmt1.setString(8, countpty_cd);
				stmt1.setString(9, agmtno);
				stmt1.setString(10, agmtrev);
				stmt1.setString(11, contno);
				stmt1.setString(12, cont_type);
				stmt1.setString(13, cargo_no);
				stmt1.setString(14, "C");
				stmt1.setString(15, "A");
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					VOTH_START_DT.add(rset1.getString(1)==null?"":rset1.getString(1));
					VOTH_END_DT.add(rset1.getString(2)==null?"":rset1.getString(2));
					VOTH_CONT_NAME.add(rset1.getString(3)==null?"":rset1.getString(3));
					
					String cont_ref_no=rset1.getString(4)==null?"":rset1.getString(4);
					String trade_ref_no=rset1.getString(5)==null?"":rset1.getString(5);
					max_contrev =rset1.getString(6)==null?"0":rset1.getString(6);
					
					if(cont_type.equals("X"))
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
				if(cont_type.equals("O") || cont_type.equals("Q")) {
					mapping_id+="-"+cargo_no;
				}
				VMAPPING_ID.add(mapping_id);

				String inv_no="";
				String inv_seq="";
				String checked_flag="";
				String approved_flag="";
				String pdf_flg="";
				String pdf_ori="";
				String pdf_tri="";
				String pdf_dup="";
				String sap_approved_flag="";
				String irn_no="";
				String inv_cetegory=rset.getString(25)==null?"":rset.getString(25);
				String tax_struct_cd=rset.getString(26)==null?"":rset.getString(26);

				inv_no=rset.getString(12)==null?"":rset.getString(12);
				checked_flag = rset.getString(13)==null?"":rset.getString(13);
				approved_flag = rset.getString(14)==null?"":rset.getString(14);
				inv_seq = rset.getString(15)==null?"":rset.getString(15);
				String fin_yr=rset.getString(16)==null?"":rset.getString(16);
				String inv_type=rset.getString(17)==null?"":rset.getString(17);
				pdf_flg=rset.getString(18)==null?"":rset.getString(18);
				String bu_state_tin=rset.getString(19)==null?"":rset.getString(19);
				pdf_ori=rset.getString(20)==null?"":rset.getString(20);
				pdf_tri=rset.getString(21)==null?"":rset.getString(21);
				pdf_dup=rset.getString(22)==null?"":rset.getString(22);
				sap_approved_flag=rset.getString(23)==null?"":rset.getString(23);
				
				VOTH_INVOICE_NO.add(inv_no);
				VOTH_STATUS.add("");
				VOTH_INVOICE_EXIST.add("Y");
				VOTH_INVOICE_TYPE.add(inv_type);
				
				VOTH_INVOICE_SEQ.add(inv_seq);
				VOTH_FINANCIAL_YEAR.add(fin_yr);
				VOTH_INV_CHECKED_FLAG.add(checked_flag);
				VOTH_INV_APPROVED_FLAG.add(approved_flag);
				VOTH_PDF_INV_FLAG.add(pdf_flg);
				VOTH_BU_STATE_TIN.add(bu_state_tin);
				VOTH_SAP_APPROVAL_FLAG.add(sap_approved_flag);
				
				if(cont_type.equals("O") || cont_type.equals("Q"))
				{
					if(inv_cetegory.equals("S") && !tax_struct_cd.equals(""))
					{
						if(Integer.parseInt(tax_struct_cd) > 0)
						{
							queryString5="SELECT IRN_NO "
									+ "FROM FMS_INVOICE_IRN_DTL "
									+ "WHERE COMPANY_CD=? AND INVOICE_NO=? ";
							stmt5=conn.prepareStatement(queryString5);
							stmt5.setString(1, comp_cd);
							stmt5.setString(2, inv_no);
							rset5=stmt5.executeQuery();
							if(rset5.next())
							{
								irn_no=rset5.getString(1)==null?"":rset5.getString(1);
							}
							rset5.close();
							stmt5.close();
							
							VOTH_IS_IRN_GENERATED.add(irn_no.equals("")?"N":"Y");
						}
						else
						{
							VOTH_IS_IRN_GENERATED.add("NA");
						}
					}
					else
					{
						VOTH_IS_IRN_GENERATED.add("NA");
					}
				}
				else
				{
					VOTH_IS_IRN_GENERATED.add("NA");
				}
				
				if(ff_print_pdf_type.equals("O") && !pdf_ori.equals(""))
				{
					VOTH_PDF_TYPE.add(ff_print_pdf_type);
				}
				else if(ff_print_pdf_type.equals("D") && !pdf_dup.equals(""))
				{
					VOTH_PDF_TYPE.add(ff_print_pdf_type);
				}
				else if(ff_print_pdf_type.equals("T") && !pdf_tri.equals(""))
				{
					VOTH_PDF_TYPE.add(ff_print_pdf_type);
				}
				else if(ff_print_pdf_type.equals("All"))
				{
					String allPdfType="";
					allPdfType+=pdf_ori.equals("")?allPdfType.equals("")?"O":",O":"";
					allPdfType+=pdf_dup.equals("")?allPdfType.equals("")?"D":",D":"";
					allPdfType+=pdf_tri.equals("")?allPdfType.equals("")?"T":",T":"";
						
					if(allPdfType.equals(""))
					{
						allPdfType="All";
					}
					VOTH_PDF_TYPE.add(allPdfType);
				}
				else
				{
					VOTH_PDF_TYPE.add("");
				}
				
				if(ff_view_pdf_type.equals("All"))
				{
					queryString3="SELECT COUNT(*) "
							+ "FROM FMS_FFLOW_INV_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
		 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
		 	        		+ "AND INVOICE_TYPE=? AND PDF_TYPE IN ('O', 'D', 'T')";
					stmt3=conn.prepareStatement(queryString3);
					stmt3.setString(1, own_cd);
					stmt3.setString(2, bu_state_tin);
					stmt3.setString(3, inv_seq);
					stmt3.setString(4, fin_yr);
					stmt3.setString(5, inv_type);
					rset3=stmt3.executeQuery();
					if(rset3.next())
					{
						if(rset3.getInt(1)>0)
						{
							VOTH_PDF_FILE_NAME.add("All");
							VOTH_PDF_FILE_PATH.add("");
						}
						else
						{
							VOTH_PDF_FILE_NAME.add("");
							VOTH_PDF_FILE_PATH.add("");
						}
					}
					else
					{
						VOTH_PDF_FILE_NAME.add("");
						VOTH_PDF_FILE_PATH.add("");
					}
					rset3.close();
					stmt3.close();
				}
				else
				{
					queryString3="SELECT FILE_NAME,PDF_SIGNED "
							+ "FROM FMS_FFLOW_INV_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
		 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
		 	        		+ "AND PDF_TYPE=? AND INVOICE_TYPE=?";
					stmt3=conn.prepareStatement(queryString3);
					stmt3.setString(1, own_cd);
					stmt3.setString(2, bu_state_tin);
					stmt3.setString(3, inv_seq);
					stmt3.setString(4, fin_yr);
					stmt3.setString(5, ff_view_pdf_type);
					stmt3.setString(6, inv_type);
					rset3=stmt3.executeQuery();
					if(rset3.next())
					{
						String pdf_signed=rset3.getString(2)==null?"":rset3.getString(2);
						
						VOTH_PDF_FILE_NAME.add(rset3.getString(1)==null?"":rset3.getString(1));
						
						if(pdf_signed.equals("Y"))
						{
							VOTH_PDF_FILE_PATH.add(CommonVariable.signed_freeflow_inv_path);
						}
						else
						{
							VOTH_PDF_FILE_PATH.add(CommonVariable.freeflow_inv_path);
						}
					}
					else
					{
						VOTH_PDF_FILE_NAME.add("");
						VOTH_PDF_FILE_PATH.add("");
					}
					rset3.close();
					stmt3.close();
				}
				
				if(ff_mail_pdf_type.equals("All"))
				{
					String AllMailPdf="";
					queryString3="SELECT PDF_TYPE "
							+ "FROM FMS_FFLOW_INV_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
		 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
		 	        		+ "AND INVOICE_TYPE=? AND PDF_SIGNED=? AND PDF_TYPE!=?";
					stmt3=conn.prepareStatement(queryString3);
					stmt3.setString(1, own_cd);
					stmt3.setString(2, bu_state_tin);
					stmt3.setString(3, inv_seq);
					stmt3.setString(4, fin_yr);
					stmt3.setString(5, inv_type);
					stmt3.setString(6, "Y");
					stmt3.setString(7, "X");
					rset3=stmt3.executeQuery();
					while(rset3.next())
					{
						String temp=rset3.getString(1)==null?"":rset3.getString(1);
						if(AllMailPdf.equals(""))
						{
							AllMailPdf=temp;
						}
						else
						{
							AllMailPdf+=","+temp;
						}
					}
					rset3.close();
					stmt3.close();
					
					VOTH_PDF_SIGNED_FLAG.add("All");
					VOTH_SIGN_PDF_TYPE.add(AllMailPdf);
				}
				else
				{
					queryString3="SELECT FILE_NAME,PDF_SIGNED "
							+ "FROM FMS_FFLOW_INV_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
		 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
		 	        		+ "AND PDF_TYPE=? AND INVOICE_TYPE=? ";
					stmt3=conn.prepareStatement(queryString3);
					stmt3.setString(1, own_cd);
					stmt3.setString(2, bu_state_tin);
					stmt3.setString(3, inv_seq);
					stmt3.setString(4, fin_yr);
					stmt3.setString(5, ff_mail_pdf_type);
					stmt3.setString(6, inv_type);
					rset3=stmt3.executeQuery();
					if(rset3.next())
					{
						String pdf_signed=rset3.getString(2)==null?"":rset3.getString(2);
						VOTH_PDF_SIGNED_FLAG.add(pdf_signed);
						if(pdf_signed.equals("Y"))
						{
							VOTH_SIGN_PDF_TYPE.add(ff_mail_pdf_type);
						}
						else
						{
							VOTH_SIGN_PDF_TYPE.add("");
						}
					}
					else
					{
						VOTH_PDF_SIGNED_FLAG.add("");
						VOTH_SIGN_PDF_TYPE.add("");
					}
					rset3.close();
					stmt3.close();
				}
				
				int re_print_count=isRePrintPDFRequestedFflow(comp_cd,bu_state_tin,inv_seq,fin_yr,"RLNG","REPRINT_PDF","A",inv_type);
				VOTH_RE_PRINT_PDF.add(re_print_count>0?"Y":"N");
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	int storage_ext_day=0;
	public void getContractDetail()
	{
		String function_nm="getContractDetail()";
		try
		{
			couterpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
			couterpty_nm=""+utilBean.getCounterpartyName(conn,counterparty_cd);
			plant_abbr=utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C");
			bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_unit, "B");
			
			if(contract_type.equals("O") || contract_type.equals("Q"))
			{
				String ship_nm="";
				String boe_number="";
				String boe_date="";
				
				queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),TO_CHAR(TO_DATE(B.ACTUAL_RECPT_DT,'DD/MM/YYYY')+NVL(B.STORAGE_DAYS-1,0)+NVL(B.STORAGE_EXT_DAYS,0),'DD/MM/YYYY'),"
						+ "A.CONT_NAME,B.CARGO_REF,A.CONTRACT_TYPE,A.LTCORA_TARIFF,A.LTCORA_TARIFF_UNIT,"
						+ "A.SUG,B.SHIP_CD,B.BOE_NO,TO_CHAR(B.BOE_DT,'DD-MON-YY'),A.DISCOUNT_DAYS,"
						+ "NVL(B.STORAGE_DAYS-1,0),NVL(B.STORAGE_EXT_DAYS,0),A.STORAGE_TARIFF_UNIT,A.STORAGE_TARIFF,A.ADV_ADJUST,TO_CHAR(B.ACTUAL_RECPT_DT,'DD-MON-YY') "
						+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_CARGO_DTL B "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? "
						+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.AGMT_TYPE=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND B.CARGO_NO=? "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
						+ ""
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, "C");
				stmt.setString(4, agmt_no);
				stmt.setString(5, agmt_rev_no);
				stmt.setString(6, "A");
				stmt.setString(7, cont_no);
				stmt.setString(8, contract_type);
				stmt.setString(9, cargo_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					String agmtno=rset.getString(3)==null?"0":rset.getString(3);
					String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					cont_start_dt = rset.getString(7)==null?"":rset.getString(7);
					String contRef=rset.getString(10)==null?"":rset.getString(10);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					
					contract_ref=contRef;
					agmt_base="";
					deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmtno, agmtrev, contno, contrev, cont_type, cargo_no)+" ["+contRef+"]";
					
					price_cd = rset.getString(13)==null?"2":rset.getString(13);
					price = utilBean.RateNumberFormat(rset.getDouble(12), price_cd);
					price_cd_nm=""+utilBean.getRateUnitNm(conn,price_cd);
					
					sug_percentage = rset.getString(14)==null?"":nf.format(rset.getDouble(14));
					
					String ship_cd=rset.getString(15)==null?"":rset.getString(15);
					ship_nm=utilBean.getShipName(conn,ship_cd);
					boe_number = rset.getString(16)==null?"":rset.getString(16);
					boe_date=rset.getString(17)==null?"":rset.getString(17);
					
					discount_days=rset.getString(18)==null?"":rset.getString(18);
					
					int storage_day=rset.getInt(19);
					storage_ext_day=rset.getInt(20);
					advance_adj_flag=rset.getString(23)==null?"":rset.getString(23);
					String actualArrival_Dt=boe_date=rset.getString(24)==null?"":rset.getString(24);
					
					storage_start_dt=utilDate.getDate(cont_start_dt, ""+(storage_day+1));
					storage_end_dt=utilDate.getDate(storage_start_dt, ""+(storage_ext_day-1));
					
					queryString1="SELECT LTCORA_TARIFF,LTCORA_TARIFF_UNIT,SUG "
							+ "FROM FMS_LTCORA_CONT_CARGO_MOD "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=? AND CONT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=? ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, "C");
					stmt1.setString(4, agmt_no);
					stmt1.setString(5, agmt_rev_no);
					stmt1.setString(6, "A");
					stmt1.setString(7, cont_no);
					stmt1.setString(8, contract_type);
					stmt1.setString(9, cargo_no);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						price_cd = rset1.getString(2)==null?"2":rset1.getString(2);
						price = utilBean.RateNumberFormat(rset1.getDouble(1), price_cd);
						price_cd_nm=""+utilBean.getRateUnitNm(conn,price_cd);
						
						sug_percentage = rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
					}
					rset1.close();
					stmt1.close();
					//APPLICABLE FOR LTCORA MAIN, SUG
					//remark_2="Cargo "+ship_nm+" Dated "+boe_date+"";
					remark_2="Cargo "+ship_nm+" Dated "+actualArrival_Dt+""; //AS DISCUSSED WITH VIJAY ON 20260109, PASS ACTUAL RECEIPT DATE INSTAED OF BOE DATE
					if(boe_number.equals(""))
					{
						remark_2="Cargo "+ship_nm;
					}
					
					if(inv_flag.equals("ST"))
					{
						price_cd = rset.getString(21)==null?"":rset.getString(21);
						price = utilBean.RateNumberFormat(rset.getDouble(22), price_cd);
						price_cd_nm=""+utilBean.getRateUnitNm(conn,price_cd);
					}
				}
				else
				{
					price="0.00";
					price_cd="2";
					price_cd_nm=""+utilBean.getRateUnitNm(conn,price_cd);
				}
				rset.close();
				stmt.close();
				
				if(inv_flag.equals("UG")) //FOR SUG INVOICE WILL BE GENERATED ON INR ONLY
				{
					price="";
					price_cd="1";
					
					price_cd_nm=""+utilBean.getRateUnitNm(conn,price_cd);
				}
				
				//for counterparty plant
				queryString = "SELECT A.STAT_NO, TO_CHAR(A.EFF_DT,'DD/MM/YYYY'), B.STAT_NM "
						+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B "
						+ "WHERE A.COUNTERPARTY_CD=? AND A.ENTITY=? "
						+ "AND A.PLANT_SEQ_NO=? AND A.COMPANY_CD=? AND B.STAT_CD='1003' " //GSTIN Number cd
						+ "AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, counterparty_cd);
				stmt.setString(2, "C");
				stmt.setString(3, plant_seq);
				stmt.setString(4, comp_cd);
				rset = stmt.executeQuery();
				if(rset.next())
				{
					String no = rset.getString(1)==null?"":rset.getString(1);
					String nm = rset.getString(3)==null?"":rset.getString(3);
					
					plant_gstin_no=no;
				}
				rset.close();
				stmt.close();
				
				//for bu unit
				queryString = "SELECT A.STAT_NO, TO_CHAR(A.EFF_DT,'DD/MM/YYYY'), B.STAT_NM "
						+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B "
						+ "WHERE A.COUNTERPARTY_CD=? AND A.ENTITY=? "
						+ "AND A.PLANT_SEQ_NO=? AND A.COMPANY_CD=? AND B.STAT_CD='1003' " //GSTIN Number cd
						+ "AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, "B");
				stmt.setString(3, bu_unit);
				stmt.setString(4, comp_cd);
				rset = stmt.executeQuery();
				if(rset.next())
				{
					String no = rset.getString(1)==null?"":rset.getString(1);
					String nm = rset.getString(3)==null?"":rset.getString(3);
					
					bu_gstin_no=no;
				}
				rset.close();
				stmt.close();
			}
			else
			{
				queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
						+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_REF_NO,CONTRACT_TYPE,"
						+ "RATE,RATE_UNIT,TRADE_REF_NO,AGMT_BASE "
						+ "FROM FMS_SUPPLY_CONT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONT_NO=? AND CONT_REV=? "
						+ "AND CONTRACT_TYPE=?";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt_no);
				stmt.setString(4, agmt_rev_no);
				stmt.setString(5, cont_no);
				stmt.setString(6, cont_rev_no);
				stmt.setString(7, contract_type);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					String agmtno=rset.getString(3)==null?"0":rset.getString(3);
					String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					cont_start_dt = rset.getString(7)==null?"":rset.getString(7);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					String contRef=rset.getString(10)==null?"":rset.getString(10);
					String tradeRef=rset.getString(14)==null?"":rset.getString(14);
					agmt_base=rset.getString(15)==null?"":rset.getString(15);
					
					if(cont_type.equals("X"))
					{
						contRef=tradeRef;
					}
					contract_ref=contRef;
					deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmtno, agmtrev, contno, contrev, cont_type, "")+" ["+contRef+"]";
					
					price_cd = rset.getString(13)==null?"2":rset.getString(13);
					price = utilBean.RateNumberFormat(rset.getDouble(12), price_cd);
					price_cd_nm=""+utilBean.getRateUnitNm(conn,price_cd);
				}
				else
				{
					price="0.00";
					price_cd="2";
					price_cd_nm=""+utilBean.getRateUnitNm(conn,price_cd);
				}
				rset.close();
				stmt.close();
				
				queryString1="SELECT CHARGE_RATE,CHARGE_ABBR "
						+ "FROM FMS_SUPPLY_CONT_PLANT_CHRG A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONT_NO=? AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? "
						+ "AND A.EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_SUPPLY_CONT_PLANT_CHRG B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.CHARGE_ABBR=B.CHARGE_ABBR "
						+ "AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, agmt_no);
				stmt1.setString(4, agmt_rev_no);
				stmt1.setString(5, cont_no);
				stmt1.setString(6, contract_type);
				stmt1.setString(7, plant_seq);
				stmt1.setString(8, period_end_dt);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					String charge_abbr=rset1.getString(2)==null?"":rset1.getString(2);
					if(charge_abbr.equals("TC"))
					{
						transportation_charges=rset1.getString(1)==null?"":nf2.format(rset1.getDouble(1));
					}
					else if(charge_abbr.equals("OC"))
					{
						other_charges=rset1.getString(1)==null?"":nf2.format(rset1.getDouble(1));
					}
					else if(charge_abbr.equals("MM"))
					{
						marketing_margin=rset1.getString(1)==null?"":nf2.format(rset1.getDouble(1));
					}
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
	
	public void getContractBillingDetail()
	{
		String function_nm="getContractBillingDetail()";
		try
		{
			if(contract_type.equals("O") || contract_type.equals("Q"))
			{
				queryString="SELECT INVOICE_CUR_CD,PAYMENT_CUR_CD,DUE_DATE,EXCHNG_RATE_CD,EXCHNG_CRITERIA,EXCHNG_RATE_CAL,DUE_DT_IN,"
						+ "EXCL_SAT_MAP,EXCHG_VAL,HOLIDAY_STATE,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE "
						+ "FROM FMS_LTCORA_CONT_BILLING_DTL A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev_no+"' "
						+ "AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? AND BUY_SALE=? AND AGMT_TYPE=? ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt_no);
				stmt.setString(4, agmt_rev_no);
				stmt.setString(5, cont_no);
				stmt.setString(6, contract_type);
				stmt.setString(7, plant_seq);
				stmt.setString(8, "C");
				stmt.setString(9, "A");
				rset=stmt.executeQuery();
				if(rset.next())
				{
					invoice_raised_in = rset.getString(1)==null?"2":rset.getString(1);
					invoice_raised_in_nm=""+utilBean.getRateUnitNm(conn,invoice_raised_in);
					//payment_done_in = rset.getString(2)==null?"2":rset.getString(2);
					//payment_done_in_nm=""+utilBean.getRateUnitNm(payment_done_in);
					due_days = rset.getString(3)==null?"0":rset.getString(3);
					exchng_rate_cd = rset.getString(4)==null?"":rset.getString(4);
					exchang_criteria = rset.getString(5)==null?"":rset.getString(5);
					exchng_rate_cal = rset.getString(6)==null?"":rset.getString(6);
					
					consider_due_dt_in=rset.getString(7)==null?"C":rset.getString(7);
					exclude_sat=rset.getString(8)==null?"":rset.getString(8);
					
					fixed_exchng_val=nf2.format(rset.getDouble(9));
					
					holiday_state=rset.getString(10)==null?"":rset.getString(10);

					int_cal_rate_cd=rset.getString(11)==null?"":rset.getString(11);
					int_cal_sign=rset.getString(12)==null?"":rset.getString(12);
					int_cal_percentage=rset.getString(13)==null?"":rset.getString(13);
					
					String queryString1 = "SELECT INT_RATE_CD,INT_RATE_NM,BANK_ABBR,FLAG "
							+ "FROM FMS_INT_RATE_MST "
							+ "WHERE FLAG=? AND INT_RATE_CD=? "
							+ "ORDER BY INT_RATE_NM";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, "Y");
					stmt1.setString(2, int_cal_rate_cd);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						int_cal_rate_nm = (rset1.getString(2)==null?"":rset1.getString(2));
					}
					rset1.close();
					stmt1.close();

					String queryString2 = "SELECT INT_VAL "
							+ "FROM FMS_INT_PAY_RATE_ENTRY X "
							+ "WHERE INT_RATE_CD=? AND FLAG=? "
							+ "AND EFF_DT = (SELECT MAX(EFF_DT) "
							+ "FROM FMS_INT_PAY_RATE_ENTRY Y "
							+ "WHERE X.INT_RATE_CD = Y.INT_RATE_CD AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY')) ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, int_cal_rate_cd);
					stmt2.setString(2, "Y");
					stmt2.setString(3, ori_inv_due_dt);
					rset2=stmt2.executeQuery();
					while(rset2.next())
					{
						int_cal_rate_value = (rset2.getString(1)==null?"":rset2.getString(1));
					}
					rset2.close();
					stmt2.close();
					
					int_total_percentage = getTotalAnnualInterestRate(int_cal_rate_value,int_cal_sign,int_cal_percentage);
				}
				else
				{	
					invoice_raised_in="2";
					invoice_raised_in_nm=""+utilBean.getRateUnitNm(conn,invoice_raised_in);
					
					consider_due_dt_in="C";
					exclude_sat="";
					holiday_state="";
					
					fixed_exchng_val=nf2.format(0);
				}
				rset.close();
				stmt.close();
				
				if(inv_flag.equals("UG")) //FOR SUG INVOICE WILL BE GENERATED ON INR ONLY
				{
					invoice_raised_in="1";
					invoice_raised_in_nm=""+utilBean.getRateUnitNm(conn,invoice_raised_in);
				}
			}
			else
			{
				queryString="SELECT INVOICE_CUR_CD,PAYMENT_CUR_CD,DUE_DATE,EXCHNG_RATE_CD,EXCHNG_CRITERIA,EXCHNG_RATE_CAL,DUE_DT_IN,"
						+ "EXCL_SAT_MAP,EXCHG_VAL,HOLIDAY_STATE,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE "
						+ "FROM FMS_SUPPLY_BILLING_DTL A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
						+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev_no+"' "
						+ "AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_SUPPLY_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt_no);
				stmt.setString(4, cont_no);
				stmt.setString(5, contract_type);
				stmt.setString(6, plant_seq);
				stmt.setString(7, period_end_dt);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					invoice_raised_in = rset.getString(1)==null?"2":rset.getString(1);
					invoice_raised_in_nm=""+utilBean.getRateUnitNm(conn,invoice_raised_in);
					//payment_done_in = rset.getString(2)==null?"2":rset.getString(2);
					//payment_done_in_nm=""+utilBean.getRateUnitNm(payment_done_in);
					due_days = rset.getString(3)==null?"0":rset.getString(3);
					exchng_rate_cd = rset.getString(4)==null?"":rset.getString(4);
					exchang_criteria = rset.getString(5)==null?"":rset.getString(5);
					exchng_rate_cal = rset.getString(6)==null?"":rset.getString(6);
					
					consider_due_dt_in=rset.getString(7)==null?"C":rset.getString(7);
					exclude_sat=rset.getString(8)==null?"":rset.getString(8);
					
					fixed_exchng_val=nf2.format(rset.getDouble(9));
					
					holiday_state=rset.getString(10)==null?"":rset.getString(10);
					
					int_cal_rate_cd=rset.getString(11)==null?"":rset.getString(11);
					int_cal_sign=rset.getString(12)==null?"":rset.getString(12);
					int_cal_percentage=rset.getString(13)==null?"":rset.getString(13);
					
					String queryString1 = "SELECT INT_RATE_CD,INT_RATE_NM,BANK_ABBR,FLAG "
							+ "FROM FMS_INT_RATE_MST "
							+ "WHERE FLAG=? AND INT_RATE_CD=? "
							+ "ORDER BY INT_RATE_NM";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, "Y");
					stmt1.setString(2, int_cal_rate_cd);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						int_cal_rate_nm = (rset1.getString(2)==null?"":rset1.getString(2));
					}
					rset1.close();
					stmt1.close();

					String queryString2 = "SELECT INT_VAL "
							+ "FROM FMS_INT_PAY_RATE_ENTRY X "
							+ "WHERE INT_RATE_CD=? AND FLAG=? "
							+ "AND EFF_DT = (SELECT MAX(EFF_DT) "
							+ "FROM FMS_INT_PAY_RATE_ENTRY Y "
							+ "WHERE X.INT_RATE_CD = Y.INT_RATE_CD AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY'))";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, int_cal_rate_cd);
					stmt2.setString(2, "Y");
					stmt2.setString(3, ori_inv_due_dt);
					rset2=stmt2.executeQuery();
					while(rset2.next())
					{
						int_cal_rate_value = (rset2.getString(1)==null?"":rset2.getString(1));
					}
					rset2.close();
					stmt2.close();
					
					int_total_percentage = getTotalAnnualInterestRate(int_cal_rate_value,int_cal_sign,int_cal_percentage);
				}
				else
				{	
					invoice_raised_in="2";
					invoice_raised_in_nm=""+utilBean.getRateUnitNm(conn,invoice_raised_in);
					
					consider_due_dt_in="C";
					exclude_sat="";
					holiday_state="";
					
					fixed_exchng_val=nf2.format(0);
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
	
	public void getContactPerson()
	{
		String function_nm="getContactPerson()";
		try
		{
			queryString="SELECT CONTACT_PERSON, SEQ_NO "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND ADDR_FLAG=? AND INV_FLAG=? "
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.TYPE=B.TYPE "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, "C");
			stmt.setString(4, "P"+plant_seq);
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
					+ "AND ENTITY=? AND ADDR_FLAG=? AND INV_FLAG=? "
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
	
	public void getReceiptVoucher()
	{
		String function_nm="getReceiptVoucher()";
		try
		{
			if((contract_type.equals("Q") || contract_type.equals("O")) && advance_adj_flag.equals("Y") && (inv_flag.equals("F") || inv_flag.equals("ST")))
			{
				boolean flag=false;
				if(operation.equals("MODIFY"))
				{
					flag=true;
					queryString="SELECT C.SEC_INT_REF "
							+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B, FMS_SECURITY_FILE_DTL C "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.GX=? AND C.FILE_TYPE=? "
							+ "AND B.CONTRACT_TYPE=? AND B.AGMT_NO=? AND B.CONT_NO=? AND C.SEC_INT_REF IS NOT NULL "
							+ "AND A.CR_DR=? AND A.BU_UNIT=? "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.SEQ_NO=B.SEQ_NO AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX "
							+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
							+ "AND A.SEQ_NO=C.SEQ_NO AND A.SEQ_REV_NO=C.SEQ_REV_NO AND A.GX=C.GX "
							+ "AND (NVL(A.VALUE,0) - (NVL((SELECT SUM(AMOUNT) FROM FMS_INV_ADV_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND C.SEC_INT_REF=B.SEC_INT_REF "
							+ "AND NOT (B.BU_STATE_TIN=? AND B.FINANCIAL_YEAR=? AND B.INVOICE_SEQ=?)),0) "
							+ "+ NVL((SELECT H.VALUE FROM FMS_SECURITY_MST H WHERE A.COMPANY_CD=H.COMPANY_CD AND H.GX=A.GX AND H.RECPT_SEC_REF=C.SEC_INT_REF),0))"
							+ ") > 0";
				}
				else if(operation.equals("INSERT") && !submission_chk)
				{
					flag=true;
					queryString="SELECT C.SEC_INT_REF "
							+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B, FMS_SECURITY_FILE_DTL C "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.GX=? AND C.FILE_TYPE=? "
							+ "AND B.CONTRACT_TYPE=? AND B.AGMT_NO=? AND B.CONT_NO=? AND C.SEC_INT_REF IS NOT NULL "
							+ "AND A.CR_DR=? AND A.BU_UNIT=? "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.SEQ_NO=B.SEQ_NO AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX "
							+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
							+ "AND A.SEQ_NO=C.SEQ_NO AND A.SEQ_REV_NO=C.SEQ_REV_NO AND A.GX=C.GX "
							+ "AND (NVL(A.VALUE,0) - (NVL((SELECT SUM(AMOUNT) FROM FMS_INV_ADV_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND C.SEC_INT_REF=B.SEC_INT_REF),0) "
							+ "+ NVL((SELECT H.VALUE FROM FMS_SECURITY_MST H WHERE A.COMPANY_CD=H.COMPANY_CD AND H.GX=A.GX AND H.RECPT_SEC_REF=C.SEC_INT_REF),0))"
							+ ") > 0";
				}
				if(flag)
				{
					int st=0;
					stmt=conn.prepareStatement(queryString);
					stmt.setString(++st, comp_cd);
					stmt.setString(++st, counterparty_cd);
					stmt.setString(++st, "K");
					stmt.setString(++st, "PDF");
					stmt.setString(++st, contract_type);
					stmt.setString(++st, agmt_no);
					stmt.setString(++st, cont_no);
					stmt.setString(++st, "CR");
					stmt.setString(++st, bu_unit);
					if(operation.equals("MODIFY"))
					{
						stmt.setString(++st, bu_state_tin);
						stmt.setString(++st, exist_financial_year);
						stmt.setString(++st, invoice_seq);
					}
					rset=stmt.executeQuery();
					while(rset.next())
					{
						VRECEIPT_VOUCHER_MST.add(rset.getString(1)==null?"":rset.getString(1));
					}
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
	
	public void getInvoiceCalculation()
	{
		String function_nm="getInvoiceCalculation()";
		try
		{
			//if(operation.equals("INSERT"))
			if((operation.equals("INSERT") && !submission_chk))// || operation.equals("MODIFY"))
			{
				/*GENERATING ON SUBMISSION
				String inv_seq="1";
				queryString="SELECT MAX(INVOICE_SEQ) "
						+ "FROM FMS_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
						+ "AND BU_STATE_TIN=?";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, financial_year);
				stmt.setString(3, bu_state_tin);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					inv_seq = ""+(rset.getInt(1)+1);
				}
				rset.close();
				stmt.close();
				invoice_seq=inv_seq;
				*/
				
				/*queryString1="SELECT SEQ_NO "
						+ "FROM FMS_ENTITY_CONTACT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND ENTITY=? AND ADDR_FLAG=? AND INV_FLAG=? "
						+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND TO_INV=? AND TYPE=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.TYPE=B.TYPE "
						+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, "C");
				stmt1.setString(4, "P"+plant_seq);
				stmt1.setString(5, "Y");
				stmt1.setString(6, "Y");
				stmt1.setString(7, "Y");
				stmt1.setString(8, "Y");
				stmt1.setString(9, "RLNG");
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					contact_person_cd=rset1.getString(1)==null?"0":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();*/
				
				String temp_contact_person_cd=utilBean.getEntityContactPersonCd(conn,comp_cd,counterparty_cd,"C","P"+plant_seq, "INV", "RLNG","Y");
				contact_person_cd=temp_contact_person_cd.equals("")?"0":temp_contact_person_cd;
				
				/*queryString2="SELECT SEQ_NO "
						+ "FROM FMS_ENTITY_CONTACT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND ENTITY=? AND ADDR_FLAG=? AND INV_FLAG=? "
						+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND TO_INV=? AND TYPE=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.TYPE=B.TYPE "
						+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, comp_cd);
				stmt2.setString(3, "B");
				stmt2.setString(4, "P"+bu_unit);
				stmt2.setString(5, "Y");
				stmt2.setString(6, "Y");
				stmt2.setString(7, "Y");
				stmt2.setString(8, "Y");
				stmt2.setString(9, "RLNG");
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					bu_contact_person_cd=rset2.getString(1)==null?"0":rset2.getString(1);
				}
				rset2.close();
				stmt2.close();*/
				
				String temp_bu_contact_person_cd=utilBean.getEntityContactPersonCd(conn,comp_cd,comp_cd,"B","P"+bu_unit, "INV", "RLNG","Y");
				bu_contact_person_cd=temp_bu_contact_person_cd.equals("")?"0":temp_bu_contact_person_cd;
			}
			
			if(inv_flag.equals("UG"))
			{
				if(!allocQty.equals(""))
				{
					qty_mmbtu=nf.format(Double.parseDouble(allocQty));
				}
				if(!temp_price.equals(""))
				{
					price=utilBean.RateNumberFormat(Double.parseDouble(temp_price), price_cd);
				}
				
				if((operation.equals("INSERT") && !submission_chk) || operation.equals("MODIFY"))
				{
					if(!qty_mmbtu.equals("") && !sug_percentage.equals("") && !price.equals(""))
					{
						if(Double.parseDouble(qty_mmbtu)>0)
						{
							sug_qty=nf.format(Double.parseDouble(qty_mmbtu)*Double.parseDouble(sug_percentage)/100);
							
							double temp_gross_amt=Double.parseDouble(sug_qty) * Double.parseDouble(price);
							gross_amt = nf.format(Math.round(temp_gross_amt));
							gross_amt1 = gross_amt;
						}
						else
						{
							gross_amt1="0.00";
						}
					}
					else
					{
						gross_amt1="0.00";
					}
				
					Vector temp = new Vector();
					//temp=TaxCalc.ServiceTaxAmountCalculationInRupeesWithInfo(conn,comp_cd, counterparty_cd, "C", plant_seq, bu_unit, period_start_dt, inv_flag, gross_amt1);
					temp=TaxCalc.ServiceTaxAmountCalculationInRupeesWithInfo(conn,comp_cd, counterparty_cd, "C", plant_seq, bu_unit, cont_start_dt, inv_flag, gross_amt1); //TAKE TAX DETAIL BASED ON CRAGO ACTUAL RECEIPT DT AS DISCUSSED WITH VIJAY ON 20250926
				
					tax_amt = nf.format(Math.round(Double.parseDouble(""+temp.elementAt(0))));
					tax_struct_cd = ""+temp.elementAt(1);
					tax_struct_dt = ""+temp.elementAt(2);
					tax_info = ""+temp.elementAt(3);
					tax_struct_dtl = ""+temp.elementAt(4);
					tax_factor = ""+temp.elementAt(5);
					
					VMULTI_TAX_STRUCT.add(temp.elementAt(6));
					
					if(tax_struct_cd.equals(""))
					{
						tax_amt="";
					}
					
					invoice_amt = tax_amt;
					net_payable = invoice_amt;
				}
			}
			else if(inv_flag.equals("ST"))
			{
				if((operation.equals("INSERT") && !submission_chk) || operation.equals("MODIFY"))
				{
					double tcq=0;
					queryString="SELECT SUM(ADQ_QTY) "
							+ "FROM FMS_LTCORA_CONT_CARGO_ADQ "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CARGO_NO=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND BUY_SALE=? AND AGMT_TYPE=? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, cargo_no);
					stmt.setString(4, cont_no);
					stmt.setString(5, agmt_no);
					stmt.setString(6, contract_type);
					stmt.setString(7, "C");
					stmt.setString(8, "A");
					rset = stmt.executeQuery();
					if(rset.next())
					{
						tcq=rset.getDouble(1);
					}
					stmt.close();
					rset.close();
					
					if(tcq > 0 && !sug_percentage.equals(""))
					{
						tcq= tcq - (tcq * Double.parseDouble(sug_percentage) / 100);
					}
							
					double tot_qty=0; 
					queryString="SELECT SUM(QTY_MMBTU) "
			  				+ "FROM FMS_DAILY_ALLOCATION_DTL A "
			  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
							+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONTRACT_TYPE=? "
							+ "AND GAS_DT < TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
							+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
							+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
							+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, cont_no);
					stmt.setString(2, agmt_no);
					stmt.setString(3, comp_cd);
					stmt.setString(4, counterparty_cd);
					stmt.setString(5, contract_type);
					stmt.setString(6, storage_start_dt);
					stmt.setString(7, cargo_no);
					rset = stmt.executeQuery();
					if(rset.next())
					{
						tot_qty=rset.getDouble(1);
					}
					stmt.close();
					rset.close();
					
					if(price_cd.equals("2") && exchng_rate_cd.equals("0")) //FOR FIXED EXCHANGE RATE //JD20230929 00:47
					{
						double exchng_rate=Double.parseDouble(fixed_exchng_val);
						exchang_rate_dt="";
						exchang_rate=nf2.format(exchng_rate);
					}
					
					double storage_qty=tcq;
					//String init_dt = utilDate.getDate(storage_start_dt, "-1");
					//System.out.println("init_dt :: "+init_dt);
					//System.out.println("actual storage duration :: "+storage_start_dt+" - "+storage_end_dt);
					
					String init_dt = utilDate.getDate(period_start_dt, "-1");
					
					String sysdate=utilDate.getSysdate();
					//int sysDays=utilDate.getDays(sysdate, storage_end_dt);
					//System.out.println(sysdate+" - "+ storage_end_dt+" :: " +sysDays);
					
					int sysDays=utilDate.getDays(sysdate, period_end_dt);
					
					//String temp_dt=storage_end_dt;
					String temp_dt=period_end_dt;
					if(sysDays <= 0)
					{
						temp_dt=sysdate;
					}
					
					//int days=utilDate.getDays(temp_dt, storage_start_dt);
					//System.out.println(storage_start_dt+" -- "+temp_dt);
					//System.out.println(days);
					
					int days=utilDate.getDays(temp_dt, period_start_dt);
					
					for(int k=1; k<=days; k++)
					{
						String date=utilDate.getDate(init_dt, ""+k);
						
						int count_day=utilDate.getDays(date,storage_start_dt);
						//System.out.println(storage_start_dt+" -- "+date+" :: "+count_day);
						
						String variable_st_chrg="";
						queryString="SELECT STORAGE_RATE "
								+ "FROM FMS_LTCORA_CONT_STRG_CRG "
								+ "WHERE CONT_NO=? AND AGMT_NO=? "
								+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
								+ "AND BUY_SALE=? AND AGMT_TYPE=? "
								+ "AND FROM_DAYS <= ? AND TO_DAYS >= ? ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, cont_no);
						stmt.setString(2, agmt_no);
						stmt.setString(3, comp_cd);
						stmt.setString(4, counterparty_cd);
						stmt.setString(5, contract_type);
						stmt.setString(6, "C");
						stmt.setString(7, "A");
						stmt.setInt(8, count_day);
						stmt.setInt(9, count_day);
						rset = stmt.executeQuery();
						if(rset.next())
						{
							variable_st_chrg=rset.getString(1)==null?"":rset.getString(1);
							
						}
						rset.close();
						stmt.close();
						
						if(variable_st_chrg.equals(""))
						{
							variable_st_chrg=price;
						}
						else
						{
							variable_st_chrg=utilBean.RateNumberFormat(Double.parseDouble(variable_st_chrg), price_cd);
						}
						
						queryString="SELECT SUM(QTY_MMBTU),TO_CHAR(GAS_DT,'DD/MM/YYYY'),GAS_DT "
				  				+ "FROM FMS_DAILY_ALLOCATION_DTL A "
				  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
								+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
								+ "AND GAS_DT = TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
								//+ "AND A.BU_SEQ=? "
								//+ "AND A.PLANT_SEQ=? "
								+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
								+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
								+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
								+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
								+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
								+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) "
								+ "GROUP BY TO_CHAR(GAS_DT,'DD/MM/YYYY'),GAS_DT "
								+ "ORDER BY GAS_DT ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, cont_no);
						stmt.setString(2, agmt_no);
						stmt.setString(3, comp_cd);
						stmt.setString(4, counterparty_cd);
						stmt.setString(5, contract_type);
						stmt.setString(6, date);
						//stmt.setString(7, storage_end_dt);
						stmt.setString(7, cargo_no);
						//stmt.setString(9, bu_unit);
						//stmt.setString(8, plant_seq);
						rset = stmt.executeQuery();
						if(rset.next())
						{
							double offtake_qty=tot_qty;
							storage_qty=storage_qty-offtake_qty;
							
							tot_qty=rset.getDouble(1);
							String dt = rset.getString(2)==null?"":rset.getString(2);
							
							VSTORAGE_DATE.add(dt);
							VSTORAGE_INVENTORY.add(nf.format(storage_qty));
							VOFFTAKE_QTY.add(nf.format(tot_qty));
							VSTORAGE_CHARGE.add(variable_st_chrg);
							VUSER_DEFINE.add("");
							VDISCOUNT_FLAG.add("N");
							
							double storage_amt=0;
							if(!variable_st_chrg.equals(""))
							{
								storage_amt=storage_qty*Double.parseDouble(variable_st_chrg);
							}
							
							VSTORAGE_AMT.add(nf.format(storage_amt));
							
							if(!gross_amt.equals(""))
							{
								gross_amt = nf.format(Double.parseDouble(gross_amt) + storage_amt);
							}
							else
							{
								gross_amt =  nf.format(storage_amt);
							}
						}
						else
						{
							double offtake_qty=tot_qty;
							storage_qty=storage_qty-offtake_qty;
							
							tot_qty=0;
							String dt = date;
							
							VSTORAGE_DATE.add(dt);
							VSTORAGE_INVENTORY.add(nf.format(storage_qty));
							VOFFTAKE_QTY.add(nf.format(tot_qty));
							VSTORAGE_CHARGE.add(variable_st_chrg);
							VUSER_DEFINE.add("");
							VDISCOUNT_FLAG.add("N");
							
							double storage_amt=0;
							if(!variable_st_chrg.equals(""))
							{
								storage_amt=storage_qty*Double.parseDouble(variable_st_chrg);
							}
							
							VSTORAGE_AMT.add(nf.format(storage_amt));
							
							if(!gross_amt.equals(""))
							{
								gross_amt = nf.format(Double.parseDouble(gross_amt) + storage_amt);
							}
							else
							{
								gross_amt =  nf.format(storage_amt);
							}
						}
						stmt.close();
						rset.close();
					}
				}
				
				if(price_cd.equals("2"))
				{
					if(exchng_rate_cal.equals("A")) //AS THIS FUNCTIONALITY IS NOT SUPPORTED IN FMS8, SO PASSING 'D' DEFAULT VALUE
					{
						exchng_rate_cal="D";
						exchang_criteria="INV";
					}
					
					getExchangeRateMaster();
					getExchangeRateCalculation();
				}
				else
				{
					gross_amt1 = gross_amt;
				}
				
				if((operation.equals("INSERT") && !submission_chk) || operation.equals("MODIFY"))
				{
					Vector temp = new Vector();
					
					gross_include_transport_tariff=gross_amt1;
					temp=TaxCalc.ServiceTaxAmountCalculationWithInfo(conn,comp_cd, counterparty_cd, "C", plant_seq, bu_unit, period_start_dt, "ST", gross_amt1);
					
					tax_amt = nf.format(Double.parseDouble(""+temp.elementAt(0)));
					tax_struct_cd = ""+temp.elementAt(1);
					tax_struct_dt = ""+temp.elementAt(2);
					tax_info = ""+temp.elementAt(3);
					tax_struct_dtl = ""+temp.elementAt(4);
					tax_factor = ""+temp.elementAt(5);
					
					VMULTI_TAX_STRUCT.add(temp.elementAt(6));
				}
				
				if(tax_struct_cd.equals(""))
				{
					tax_amt="";
					invoice_amt = nf.format(Double.parseDouble(gross_include_transport_tariff));
				}
				else
				{
					//invoice_amt = nf.format(Double.parseDouble(gross_amt1) + Double.parseDouble(tax_amt));
					invoice_amt = nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(tax_amt)); //20230911 THIS IS NOT TESTED ERLIER BUT I THINK IT SHOULDE BE PLUSE WITH GROSS_INCLUDE_TRANS_TARIFF
				}
				
				if((operation.equals("INSERT") && !submission_chk) || operation.equals("MODIFY"))
				{
					net_payable=invoice_amt;
				}
			}
			else if(inv_flag.equals("LP"))
			{
				//if(Double.parseDouble(qty_mmbtu) > 0)
				{
					//int lateDays = utilDate.getDays(ori_inv_payrecv_dt, ori_inv_due_dt)-1;
					if(submission_chk && operation.equals("INSERT")) 
					{
						//HM20260129:No Calculation will be done for View state
					}
					else 
					{
						int lateDays = utilDate.getDays(ori_inv_payrecv_dt, ori_inv_due_dt)-1;
						
						float calcInvAmt = ((Float.parseFloat(ori_inv_net_amt) * (lateDays * Float.parseFloat(int_total_percentage)))/(100*365));
						gross_amt = ""+calcInvAmt;
						
						gross_amt1 = gross_amt;
						
						if((operation.equals("INSERT") && !submission_chk) || operation.equals("MODIFY"))
						{
							Vector temp = new Vector();
							if(contract_type.equals("O") || contract_type.equals("Q"))
							{
								gross_include_transport_tariff=gross_amt1;
								temp=TaxCalc.ServiceTaxAmountCalculationWithInfo(conn,comp_cd, counterparty_cd, "C", plant_seq, bu_unit, cont_start_dt, "SI", gross_amt1);
							}
							else
							{
								gross_include_transport_tariff=gross_amt1;
								temp=TaxCalc.TaxAmountCalculationWithInfo(conn, comp_cd, counterparty_cd, "C", plant_seq, bu_unit, cont_start_dt, gross_include_transport_tariff);
							}
							
							tax_amt = nf.format(Double.parseDouble(""+temp.elementAt(0)));
							tax_struct_cd = ""+temp.elementAt(1);
							tax_struct_dt = ""+temp.elementAt(2);
							tax_info = ""+temp.elementAt(3);
							tax_struct_dtl = ""+temp.elementAt(4);
							tax_factor = ""+temp.elementAt(5);
							
							VMULTI_TAX_STRUCT.add(temp.elementAt(6));
						}
						
						if(tax_struct_cd.equals(""))
						{
							tax_amt="";
							invoice_amt = nf.format(Double.parseDouble(gross_include_transport_tariff));
						}
						else
						{
							//invoice_amt = nf.format(Double.parseDouble(gross_amt1) + Double.parseDouble(tax_amt));
							invoice_amt = nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(tax_amt)); //20230911 THIS IS NOT TESTED ERLIER BUT I THINK IT SHOULDE BE PLUSE WITH GROSS_INCLUDE_TRANS_TARIFF
						}
						
						if((operation.equals("INSERT") && !submission_chk) || operation.equals("MODIFY"))
						{
							net_payable=invoice_amt;
						}
					}
				}
			}
			else
			{
				if((operation.equals("INSERT") && !submission_chk) || operation.equals("MODIFY"))
				{
					//GET SALES PRICE
					String new_price="";
					queryString = "SELECT DISTINCT NEW_SALE_PRICE "
							+ "FROM FMS_SUPPLY_ALLOC_REVISED A "
							+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND CONT_NO=? "
							+ "AND COUNTERPARTY_CD=? AND FLAG=? AND CONTRACT_TYPE=? "
							+ "AND MODIFICATION_SEQ_NO = (SELECT MAX(MODIFICATION_SEQ_NO) FROM FMS_SUPPLY_ALLOC_REVISED B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.FLAG=B.FLAG AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
							+ "AND B.NEW_PRICE_EFF_DT <=TO_DATE(?,'DD/MM/YYYY'))";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, agmt_no);
					stmt.setString(3, cont_no);
					stmt.setString(4, counterparty_cd);
					stmt.setString(5, "A");
					stmt.setString(6, contract_type);
					stmt.setString(7, period_end_dt);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						new_price=rset.getString(1)==null?"":rset.getString(1);
						if(!new_price.equals(""))
						{
							price=utilBean.RateNumberFormat(rset.getDouble(1), price_cd);
						}
					}
					rset.close();
					stmt.close();
					
					if(agmt_base.equals("D"))
					{
						qty_mmbtu=nf.format(utilAlloc.getDeliverdSupplyAllocationQty(conn, comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, plant_seq, bu_unit, temp_period_start_dt, temp_period_end_dt));
					}
					else
					{
						qty_mmbtu=nf.format(utilAlloc.getSupplyAllocationQty(conn, comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, plant_seq, bu_unit, temp_period_start_dt, temp_period_end_dt,cargo_no));
					}
				}
				
				if(price_cd.equals("2") && exchng_rate_cd.equals("0")) //FOR FIXED EXCHANGE RATE //JD20230929 00:47
				{
					double exchng_rate=Double.parseDouble(fixed_exchng_val);
					exchang_rate_dt="";
					exchang_rate=nf2.format(exchng_rate);
				}
				
				//if(Double.parseDouble(qty_mmbtu) > 0)
				{
					if((operation.equals("INSERT") && !submission_chk) || operation.equals("MODIFY"))
					{
						gross_amt = nf.format(Double.parseDouble(qty_mmbtu) * Double.parseDouble(price));
					}
					
					if(price_cd.equals("2"))
					{
						getExchangeRateMaster();
						getExchangeRateCalculation();
					}
					else
					{
						gross_amt1 = gross_amt;
					}
					
					if((operation.equals("INSERT") && !submission_chk) || operation.equals("MODIFY"))
					{
						Vector temp = new Vector();
						if(contract_type.equals("O") || contract_type.equals("Q"))
						{
							gross_include_transport_tariff=gross_amt1;
							temp=TaxCalc.ServiceTaxAmountCalculationWithInfo(conn,comp_cd, counterparty_cd, "C", plant_seq, bu_unit, period_start_dt, "SI", gross_amt1);
						}
						else
						{
							gross_include_transport_tariff=gross_amt1;
							if(!transportation_charges.equals(""))
							{
								transportation_amount=nf.format(Double.parseDouble(qty_mmbtu) * Double.parseDouble(transportation_charges));
								gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(transportation_amount));
								isGrossIncTriff=true;
							}
							if(!marketing_margin.equals(""))
							{
								marketing_margin_amount=nf.format(Double.parseDouble(qty_mmbtu) * Double.parseDouble(marketing_margin));
								gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(marketing_margin_amount));
								isGrossIncTriff=true;
							}
							
							if(!other_charges.equals(""))
							{
								other_charges_amount=nf.format(Double.parseDouble(qty_mmbtu) * Double.parseDouble(other_charges));
								gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(other_charges_amount));
								isGrossIncTriff=true;
							}
							
							temp=TaxCalc.TaxAmountCalculationWithInfo(conn, comp_cd, counterparty_cd, "C", plant_seq, bu_unit, period_start_dt, gross_include_transport_tariff);
						}
						
						tax_amt = nf.format(Double.parseDouble(""+temp.elementAt(0)));
						tax_struct_cd = ""+temp.elementAt(1);
						tax_struct_dt = ""+temp.elementAt(2);
						tax_info = ""+temp.elementAt(3);
						tax_struct_dtl = ""+temp.elementAt(4);
						tax_factor = ""+temp.elementAt(5);
						
						VMULTI_TAX_STRUCT.add(temp.elementAt(6));
					}
					
					if(tax_struct_cd.equals(""))
					{
						tax_amt="";
						invoice_amt = nf.format(Double.parseDouble(gross_include_transport_tariff));
					}
					else
					{
						//invoice_amt = nf.format(Double.parseDouble(gross_amt1) + Double.parseDouble(tax_amt));
						invoice_amt = nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(tax_amt)); //20230911 THIS IS NOT TESTED ERLIER BUT I THINK IT SHOULDE BE PLUSE WITH GROSS_INCLUDE_TRANS_TARIFF
					}
					
					if((operation.equals("INSERT") && !submission_chk) || operation.equals("MODIFY"))
					{
						net_payable=invoice_amt;
					}
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
			queryString = "SELECT EXC_RATE_CD,EXC_RATE_NM,FLAG "
					+ "FROM FMS_EXCHG_RATE_MST "
					+ "ORDER BY EXC_RATE_NM";
			stmt=conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VEXCHNG_RATE_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VEXCHNG_RATE_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VEXCHNG_RATE_FLAG.add(rset.getString(3)==null?"":rset.getString(3));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getExchangeRateCalculation()
	{
		String function_nm="getExchangeRateCalculation()";
		try
		{
			double exchng_rate=0;
			if(exchng_rate_cd.equals("0")) //FOR FIXED EXCHANGE RATE
			{
				exchng_rate=Double.parseDouble(fixed_exchng_val);
				exchang_rate_dt="";
				exchang_rate=nf2.format(exchng_rate);
				gross_amt1 = nf.format(Double.parseDouble(gross_amt) * exchng_rate);
			}
			else
			{
				if(exchng_rate_cal.equals("A"))
				{
					if(!sel_exchng_cd.equals(""))
					{
						exchng_rate_cd=sel_exchng_cd;
					}
					String tot_qty="0.00";
					String tot_gross_amt_inr="0.00";
					String tot_gross_amt_usd="0.00";
					String temp_dates="";
					/*queryString="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
							+ "FROM ALL_OBJECTS,(SELECT TO_DATE('"+period_start_dt+"','DD/MM/YYYY')-1 START_DATE,TO_DATE('"+period_end_dt+"','DD/MM/YYYY') END_DATE FROM DUAL) TD "
							+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
							+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";*/
					
					queryString="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
							+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
							+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
							+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, temp_period_start_dt);
					stmt.setString(2, temp_period_end_dt);
					rset=stmt.executeQuery();
					while(rset.next())
					{
						String date = rset.getString(1)==null?"":rset.getString(1);
						VALLOCATION_DT.add(date);
						
						String qtyMMBTU="";
						if(agmt_base.equals("D"))
						{
							qtyMMBTU=nf.format(utilAlloc.getDeliverdSupplyAllocationQty(conn, comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, plant_seq, bu_unit, date, date));
						}
						else
						{
							qtyMMBTU=nf.format(utilAlloc.getSupplyAllocationQty(conn, comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, plant_seq, bu_unit, date, date,cargo_no));
						}
						
						double temp_exchng=0;
						String sel_exchngRateCd=exchng_rate_cd;
						boolean exist_exchng_rate=false;
						for(int i=0; i<VEXCHNG_RATE_CD.size(); i++)
						{
							String exchng_rate_cd=""+VEXCHNG_RATE_CD.elementAt(i);
							String daily_exchng_rate="";
							if(exchng_rate_cd.equals(sel_exchngRateCd) && operation.equals("INSERT") && submission_chk)
							{
								queryString1="SELECT EXCHG_RATE_VALUE "
										+ "FROM FMS_INVOICE_DTL "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND CONT_NO=? AND AGMT_NO=? "
										+ "AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
										+ "AND BU_STATE_TIN=? AND ALLOCATION_DT=TO_DATE(?,'DD/MM/YYYY') "
										+ "AND EXCHG_RATE_CD=? AND FINANCIAL_YEAR=? AND CARGO_NO=? AND INVOICE_SEQ=? ";
								stmt1=conn.prepareStatement(queryString1);
								stmt1.setString(1, comp_cd);
								stmt1.setString(2, counterparty_cd);
								stmt1.setString(3, cont_no);
								stmt1.setString(4, agmt_no);
								stmt1.setString(5, plant_seq);
								stmt1.setString(6, bu_unit);
								stmt1.setString(7, contract_type);
								stmt1.setString(8, bu_state_tin);
								stmt1.setString(9, date);
								stmt1.setString(10, sel_exchngRateCd);
								//stmt1.setString(11, financial_year);
								stmt1.setString(11, exist_financial_year);
								stmt1.setString(12, cargo_no);
								stmt1.setString(13, invoice_seq);
								rset1=stmt1.executeQuery();
								if(rset1.next())
								{
									daily_exchng_rate=nf2.format(rset1.getDouble(1));
									exist_exchng_rate=true;
									if(daily_exchng_rate.equals(""))
									{
										VEXCHNG_RATE_CAL_COLOR.add("#cccccc");
									}
									else
									{
										VEXCHNG_RATE_CAL_COLOR.add("#ccffff");
									}
								}
								else
								{
									VEXCHNG_RATE_CAL_COLOR.add("#cccccc");
								}
								rset1.close();
								stmt1.close();
							}
							else
							{
								queryString1="SELECT EXCHG_VAL "
										+ "FROM FMS_EXCHG_RATE_ENTRY A "
										+ "WHERE EXCHG_RATE_CD=? "
										+ "AND EFF_DT= TO_DATE(?,'DD/MM/YYYY')";
								stmt1=conn.prepareStatement(queryString1);
								stmt1.setString(1, exchng_rate_cd);
								stmt1.setString(2, date);
								rset1=stmt1.executeQuery();
								if(rset1.next())
								{
									daily_exchng_rate=nf2.format(rset1.getDouble(1));
									exist_exchng_rate=true;
									if(daily_exchng_rate.equals(""))
									{
										VEXCHNG_RATE_CAL_COLOR.add("#cccccc");
									}
									else
									{
										VEXCHNG_RATE_CAL_COLOR.add("#99ffcc");
									}
								}
								else
								{
									queryString2="SELECT EXCHG_VAL "
											+ "FROM FMS_EXCHG_RATE_ENTRY A "
											+ "WHERE EXCHG_RATE_CD=? "
											+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B "
											+ "WHERE A.EXCHG_RATE_CD=B.EXCHG_RATE_CD AND B.EFF_DT <= TO_DATE(?,'DD/MM/YYYY'))";
									stmt2=conn.prepareStatement(queryString2);
									stmt2.setString(1, exchng_rate_cd);
									stmt2.setString(2, date);
									rset2=stmt2.executeQuery();
									if(rset2.next())
									{
										daily_exchng_rate=nf2.format(rset2.getDouble(1));
									}
									rset2.close();
									stmt2.close();
									exist_exchng_rate=false;
									VEXCHNG_RATE_CAL_COLOR.add("#cccccc");
								}
								rset1.close();
								stmt1.close();
							}
							VEXCHNG_RATE_CAL_CD.add(exchng_rate_cd);
							VEXCHNG_RATE_CAL_VAL.add(daily_exchng_rate);
							
							if(sel_exchngRateCd.equals(exchng_rate_cd))
							{
								if(!daily_exchng_rate.equals(""))
								{
									temp_exchng=Double.parseDouble(daily_exchng_rate);
								}
							}
						}
						
						VPRICE.add(price);
						VALLOCATION_QTY.add(qtyMMBTU);
						
						String temp_gross_amt = nf.format(Double.parseDouble(qtyMMBTU) * Double.parseDouble(price));
						VAMOUNT_USD.add(temp_gross_amt);
						String temp_gross_amt1 = "0.00";
						if(price_cd.equals("2"))
						{	
							if(temp_exchng > 0)
							{
								temp_gross_amt1 = nf.format(Double.parseDouble(temp_gross_amt) * temp_exchng);
								VAMOUNT_INR.add(temp_gross_amt1);
							}
							else if(temp_exchng <= 0 && Double.parseDouble(qtyMMBTU) > 0 && Double.parseDouble(temp_gross_amt1)<=0)
							{
								correction_needed=true;
								VAMOUNT_INR.add(temp_gross_amt1);
								
								if(temp_dates.equals(""))
								{
									temp_dates+=date;
								}
								else
								{
									temp_dates+=", "+date;
								}
							}
							else
							{
								VAMOUNT_INR.add(temp_gross_amt1);
							}
						}
						tot_gross_amt_inr = nf.format(Double.parseDouble(tot_gross_amt_inr)+Double.parseDouble(temp_gross_amt1));
						tot_qty = nf.format(Double.parseDouble(qtyMMBTU)+Double.parseDouble(tot_qty));
						
					}
					rset.close();
					stmt.close();
					
					tot_gross_amt_usd=nf.format(Double.parseDouble(qty_mmbtu) * Double.parseDouble(price));
					gross_amt1=tot_gross_amt_inr;
					
					daily_tot_amt_inr=tot_gross_amt_inr;
					daily_tot_amt_usd=tot_gross_amt_usd;
					daily_tot_qty=tot_qty;
					
					if(correction_needed)
					{
						correction_msg="Correction Needed! For non-zero Daily Qty, Daily Gross zero(0) on "+temp_dates+"";
					}
					
					exchang_rate=nf2.format(Double.parseDouble(gross_amt1)/Double.parseDouble(gross_amt));
				}
				else if(exchng_rate_cal.equals("D"))
				{
					if(submission_chk && exchange_rate_mapping.equals(""))
					{
						user_defined_dt=exchang_rate_dt;
						exchange_rate_mapping="U-"+exchng_rate_cd;
					}
					else if(user_defined_dt.equals(""))
					{
						user_defined_dt=period_end_dt;
					}
					queryString="SELECT TO_CHAR(EFF_DT,'DD/MM/YYYY') "
							+ "FROM FMS_EXCHG_RATE_ENTRY A "
							+ "WHERE EXCHG_RATE_CD=? "
							+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B "
							+ "WHERE A.EXCHG_RATE_CD=B.EXCHG_RATE_CD AND B.EFF_DT<TO_DATE(?,'DD/MM/YYYY')) ";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, exchng_rate_cd);
					stmt.setString(2, invoice_dt);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						last_avlb_exchng_dt=rset.getString(1)==null?"":rset.getString(1);
					}
					rset.close();
					stmt.close();
					
					getExchangeRateEntry("P", last_avlb_exchng_dt);
					
					if(exchang_criteria.equals("INV"))
					{
						getExchangeRateEntry("B", invoice_dt);
						lable_inv_criteria="Invoice Day";
						lable_inv_date=invoice_dt;
					}
					else if(exchang_criteria.equals("LST"))
					{
						getExchangeRateEntry("B", period_end_dt);
						lable_inv_criteria="Last Day of Billing Cycle";
						lable_inv_date=period_end_dt;
					}
					else if(exchang_criteria.equals("PRE"))
					{
						lable_inv_date=""+utilDate.getDate(period_end_dt, "-1");
						getExchangeRateEntry("B", lable_inv_date);
						lable_inv_criteria="Previous Day of Billing Cycle";
					}
					getExchangeRateEntry("U", user_defined_dt);
					
					if(!exchange_rate_mapping.equals(""))
					{
						String temp[] = exchange_rate_mapping.split("-");
						
						String dateType=temp[0];
						String exc_cd=temp[1];
						String exc_dt="";
						if(dateType.equals("P"))
						{
							exc_dt=last_avlb_exchng_dt;
						}
						else if(dateType.equals("B"))
						{
							exc_dt=lable_inv_date;
						}
						else if(dateType.equals("U"))
						{
							exc_dt=user_defined_dt;
						}
						
						queryString1="SELECT NVL(EXCHG_VAL,0),TO_CHAR(EFF_DT,'DD/MM/YYYY') "
								+ "FROM FMS_EXCHG_RATE_ENTRY A "
								+ "WHERE EXCHG_RATE_CD=? "
								+ "AND EFF_DT = TO_DATE(?,'DD/MM/YYYY')";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, exc_cd);
						stmt1.setString(2, exc_dt);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							exchng_rate_cd=exc_cd;
							exchng_rate=rset1.getDouble(1);
							exchang_rate_dt=rset1.getString(2)==null?"":rset1.getString(2);
						}
						rset1.close();
						stmt1.close();
					}
					else
					{
						if(!submission_chk)
						{
							queryString1="SELECT NVL(EXCHG_VAL,0),TO_CHAR(EFF_DT,'DD/MM/YYYY') "
									+ "FROM FMS_EXCHG_RATE_ENTRY A "
									+ "WHERE EXCHG_RATE_CD=? "
									+ "AND EFF_DT = TO_DATE(?,'DD/MM/YYYY')";
							stmt1=conn.prepareStatement(queryString1);
							stmt1.setString(1, exchng_rate_cd);
							stmt1.setString(2, invoice_dt);
							rset1=stmt1.executeQuery();
							if(rset1.next())
							{
								exchng_rate=rset1.getDouble(1);
								exchang_rate_dt=rset1.getString(2)==null?"":rset1.getString(2);
							}
							rset1.close();
							stmt1.close();
						}
						else
						{
							exchng_rate=Double.parseDouble(exchang_rate);
						}
						
						exchange_rate_mapping="B-"+exchng_rate_cd;
					}
					
					exchang_rate=nf2.format(exchng_rate);
					if((operation.equals("INSERT") && !submission_chk) || operation.equals("MODIFY"))
					{
						gross_amt1 = nf.format(Double.parseDouble(gross_amt) * exchng_rate);
					}
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getExchangeRateEntry(String dateType, String date)
	{
		String function_nm="getExchangeRateEntry()";
		try
		{
			for(int i=0; i<VEXCHNG_RATE_CD.size(); i++)
			{
				String exchng_rate_cd=""+VEXCHNG_RATE_CD.elementAt(i);
				String exchng_rate="";
				queryString="SELECT EXCHG_VAL "
						+ "FROM FMS_EXCHG_RATE_ENTRY A "
						+ "WHERE EXCHG_RATE_CD=? "
						+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, exchng_rate_cd);
				stmt.setString(2, date);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					exchng_rate=nf2.format(rset.getDouble(1)); 
				}
				rset.close();
				stmt.close();
				
				if(dateType.equals("P"))
				{
					if(!exchng_rate.equals(""))
					{
						VP_EXCHNG_RATE_CD.add(exchng_rate_cd);
						VP_EXCHNG_RATE_VALUE.add(exchng_rate);
						VP_BG_COLOR.add("#99ffcc");
					}
					else
					{
						VP_EXCHNG_RATE_CD.add(exchng_rate_cd);
						VP_EXCHNG_RATE_VALUE.add("");
						VP_BG_COLOR.add("#cccccc");
					}
				}
				if(dateType.equals("B"))
				{
					if(!exchng_rate.equals(""))
					{
						VB_EXCHNG_RATE_CD.add(exchng_rate_cd);
						VB_EXCHNG_RATE_VALUE.add(exchng_rate);
						VB_BG_COLOR.add("#99ffcc");
					}
					else
					{
						VB_EXCHNG_RATE_CD.add(exchng_rate_cd);
						VB_EXCHNG_RATE_VALUE.add("");
						VB_BG_COLOR.add("#cccccc");
					}
				}
				if(dateType.equals("U"))
				{
					if(!exchng_rate.equals(""))
					{
						VU_EXCHNG_RATE_CD.add(exchng_rate_cd);
						VU_EXCHNG_RATE_VALUE.add(exchng_rate);
						VU_BG_COLOR.add("#99ffcc");
					}
					else
					{
						VU_EXCHNG_RATE_CD.add(exchng_rate_cd);
						VU_EXCHNG_RATE_VALUE.add("");
						VU_BG_COLOR.add("#cccccc");
					}
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getRemarks()
	{
		String function_nm="getRemarks()";
		try
		{
			String signing_dt="";
			String agmt_signing_dt="";
			queryString="SELECT TO_CHAR(SIGNING_DT,'ddth Month, yyyy') "
					+ "FROM FMS_SUPPLY_CONT_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
					+ "AND AGMT_REV=? AND CONT_NO=? AND CONT_REV=? AND CONTRACT_TYPE=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, agmt_rev_no);
			stmt.setString(5, cont_no);
			stmt.setString(6, cont_rev_no);
			stmt.setString(7, contract_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				signing_dt=rset.getString(1)==null?"":rset.getString(1);
			}
			rset.close();
			stmt.close();
			
			String rmk = "";
			
			if(contract_type.equals("S"))
			{
				
				queryString1="SELECT TO_CHAR(SIGNING_DT,'ddth Month, yyyy') "
						+ "FROM FMS_AGMT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
						+ "AND AGMT_REV=? AND AGMT_TYPE=?";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, agmt_no);
				stmt1.setString(4, agmt_rev_no);
				stmt1.setString(5, "F");
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					agmt_signing_dt=rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
				rmk = "Framework Gas Sales Agreement executed on "+agmt_signing_dt+" and Supply Notice executed on "+signing_dt+"";
			}
			else if(contract_type.equals("L"))
			{
				rmk = "Latter Of Agreement executed on "+signing_dt+"";
			}
			else if(contract_type.equals("X"))
			{
				rmk = "IGX Gas Sales Contract executed on "+signing_dt+"";
			}
			
			//String bank_formula="";
			String bank_formula=utilBean.getEntityBankFormula(conn, comp_cd, counterparty_cd, "C", "RLNG", invoice_dt);
			/*queryString2="SELECT COUNTERPARTY_CD,ENTITY,TO_CHAR(EFF_DT,'DD/MM/YYYY'),BANK_NAME,ACCOUNT_ID,IFSC_CODE,BRANCH_NAME,"
					+ "STATE_NM  "
					+ "FROM FMS_ENTITY_BANK_MST A "
					+ "WHERE ENTITY=? AND COUNTERPARTY_CD=? AND COMPANY_CD=? AND CATEGORY=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_BANK_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.CATEGORY=B.CATEGORY)";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, "B");
			stmt2.setString(2, comp_cd);
			stmt2.setString(3, comp_cd);
			stmt2.setString(4, "RLNG");
			stmt2.setString(5, invoice_dt);
			rset2=stmt2.executeQuery();
			if(rset2.next())
			{
				String bank_eff_dt = rset2.getString(3)==null?"":rset2.getString(3);
				String bank_name=rset2.getString(4)==null?"":rset2.getString(4);
				String bank_account_no=rset2.getString(5)==null?"":rset2.getString(5);
				String ifsc_code=rset2.getString(6)==null?"":rset2.getString(6);
				String bank_branch=rset2.getString(7)==null?"":rset2.getString(7);
				String bank_state=rset2.getString(8)==null?"":rset2.getString(8);
				
				bank_formula=bank_name+", "+bank_branch+", "+bank_state+", A/C No : "+bank_account_no+", IFSC Code : "+ifsc_code;
			}
			rset2.close();
			stmt2.close();*/
			
			remark_1 ="Please pay the invoiced amount by wire transfer at our Bank Account : "+bank_formula;
			
			if(callFlag.equalsIgnoreCase("LP_INVOICE_GENERATION"))
			{
				String lpDueDate = utilDate.getDateFormatDD_MOM_YY(utilDate.getDate(utilDate.getSysdate(), due_days));
				
				remark_1 += "\nAnnualized Interest rate is "+int_cal_rate_nm+" ("+int_cal_rate_value+") "+int_cal_sign+" "+int_cal_percentage+" = "+int_total_percentage+" %";
				remark_1 += "\nInvoice Due Date "+lpDueDate+"";
			}
			//COMMENTED AS REQUESTED BY VIJAY ON 31/102023 
			//remark_2 ="Subject to the terms and conditions of the "+rmk+" between "+utilBean.getCompanyName(comp_cd)+" and "+utilBean.getCounterpartyName(counterparty_cd, comp_cd);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public Double getTransactionAmount(String comp_cd,String counterparty_cd, String financial_year,String invoice_dt)
	{
		String function_nm="getTransactionAmount()";
		double transc_fee=0;
		try
		{
			//RLNG INVOICE
			queryString="SELECT NVL(SUM(INVOICE_AMT),0) "
					+ "FROM FMS_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE IN (?,?) "
					+ "AND FINANCIAL_YEAR=? AND APPROVED_FLAG=? AND INVOICE_DT <=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND INV_FLAG IN ('F','DR')";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, "S");
			stmt.setString(4, "L");
			stmt.setString(5, financial_year);
			stmt.setString(6, "Y");
			stmt.setString(7, invoice_dt);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				transc_fee=rset.getDouble(1);
			}
			rset.close();
			stmt.close();
			
			queryString1="SELECT NVL(SUM(INVOICE_AMT),0) "
					+ "FROM FMS_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE IN (?,?) "
					+ "AND FINANCIAL_YEAR=? AND APPROVED_FLAG=? AND INVOICE_DT <=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND INVOICE_TYPE IN (?,?,?)";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_cd);
			stmt1.setString(3, "S");
			stmt1.setString(4, "L");
			stmt1.setString(5, financial_year);
			stmt1.setString(6, "Y");
			stmt1.setString(7, invoice_dt);
			stmt1.setString(8, "CDR");
			stmt1.setString(9, "DR");
			stmt1.setString(10, "S");
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				transc_fee+=rset1.getDouble(1);
			}
			rset1.close();
			stmt1.close();
			
			//DLNG INVOICE
			queryString="SELECT NVL(SUM(INVOICE_AMT),0) "
					+ "FROM FMS_DLNG_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE IN (?,?) "
					+ "AND FINANCIAL_YEAR=? AND APPROVED_FLAG=? AND INVOICE_DT <=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND INV_FLAG IN ('F','DR')";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, "F");
			stmt.setString(4, "E");
			stmt.setString(5, financial_year);
			stmt.setString(6, "Y");
			stmt.setString(7, invoice_dt);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				transc_fee+=rset.getDouble(1);
			}
			rset.close();
			stmt.close();
			
			queryString1="SELECT NVL(SUM(INVOICE_AMT),0) "
					+ "FROM FMS_DLNG_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE IN (?,?) "
					+ "AND FINANCIAL_YEAR=? AND APPROVED_FLAG=? AND INVOICE_DT <=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND INVOICE_TYPE IN (?,?,?)";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_cd);
			stmt1.setString(3, "E");
			stmt1.setString(4, "F");
			stmt1.setString(5, financial_year);
			stmt1.setString(6, "Y");
			stmt1.setString(7, invoice_dt);
			stmt1.setString(8, "CDR");
			stmt1.setString(9, "DR");
			stmt1.setString(10, "S");
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				transc_fee+=rset1.getDouble(1);
			}
			rset1.close();
			stmt1.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return transc_fee;
	}
	
	public void getTurnoverDetail()
	{
		String function_nm="getTurnoverDetail()";
		try
		{
			//previousFinancialYear=""+utilDate.getPreviousFinancialYear(period_end_dt);
			previousFinancialYear=""+utilDate.getPreviousFinancialYear(invoice_dt);
			
			queryString="SELECT TURNOVER_FLAG "
					+ "FROM FMS_ENTITY_TURNOVER_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND FINANCIAL_YEAR=? AND TURNOVER_CD=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, "C");
			stmt.setString(4, previousFinancialYear);
			stmt.setString(5, "1");
			rset=stmt.executeQuery();
			if(rset.next())
			{
				buyerTurnoverFlag=rset.getString(1)==null?"":rset.getString(1);
			}
			rset.close();
			stmt.close();
			
			queryString1="SELECT TURNOVER_FLAG "
					+ "FROM FMS_ENTITY_TURNOVER_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND FINANCIAL_YEAR=? AND TURNOVER_CD=? ";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, comp_cd);
			stmt1.setString(3, "B");
			stmt1.setString(4, previousFinancialYear);
			stmt1.setString(5, "1");
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				sellerTurnoverFlag=rset1.getString(1)==null?"":rset1.getString(1);
			}
			rset1.close();
			stmt1.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void checkTCS_TDSApplicable()
	{
		String function_nm="checkTCS_TDSApplicable()";
		try
		{
			if(contract_type.equals("Q") || contract_type.equals("O"))
			{
				applicable_flag="N";
				applicable_abbr="TDS";
				
				//ReGas Invoice TDS will be picked up
				Vector temp_tcs = new Vector();
				if(inv_flag.equals("ST"))
				{
					temp_tcs=TaxCalc.TCS_TDSAmountCalculationWithInfo(conn, comp_cd, counterparty_cd, "C", "TDS", inv_flag,"S",period_end_dt, gross_include_transport_tariff);
				}
				else if(inv_flag.equals("LP")) 
				{
					temp_tcs=TaxCalc.TCS_TDSAmountCalculationWithInfo(conn, comp_cd, counterparty_cd, "C", "TDS", inv_flag,"S",cont_end_dt, gross_include_transport_tariff);
				}
				else
				{
					temp_tcs=TaxCalc.TCS_TDSAmountCalculationWithInfo(conn, comp_cd, counterparty_cd, "C", "TDS", "SI","S",period_end_dt, gross_include_transport_tariff);
				}
				tds_amt = nf.format(Double.parseDouble(""+temp_tcs.elementAt(0)));
				tds_struct_cd = ""+temp_tcs.elementAt(1);
				tds_struct_dt = ""+temp_tcs.elementAt(2);
				//tax_info = ""+temp_tcs.elementAt(3);
				//tax_struct_dtl = ""+temp_tcs.elementAt(4);
				tds_factor = ""+temp_tcs.elementAt(5);
				
				if(Double.doubleToRawLongBits(Double.parseDouble(tds_amt))==Double.doubleToRawLongBits(0))
				{
					tds_amt="";
				}
				if(!tds_factor.equals(""))
				{
					if(Double.doubleToRawLongBits(Double.parseDouble(tds_factor))==Double.doubleToRawLongBits(0))
					{
						tds_factor="";
					}
				}
				
				net_payable=invoice_amt;
			}
			else
			{
				//Trunover_prev_finantial_yr(Seller<10cr, Buyer<10cr) && Trans_current_finantial_yr > 50L ==> TCS/TDS Applicable : NA
				//Trunover_prev_finantial_yr(S>10cr, B<10cr) && Trans_current_finantial_yr > 50L ==> TCS/TDS Applicable : TCS
				//Trunover_prev_finantial_yr(S<10cr, B>10cr) && Trans_current_finantial_yr > 50L ==> TCS/TDS Applicable : TDS
				//Trunover_prev_finantial_yr(S>10cr, B>10cr) && Trans_current_finantial_yr > 50L ==> TCS/TDS Applicable : TDS
				
				if(contract_type.equals("X"))
				{
					applicable_abbr="NA";
					
					applicable_amt ="";
					tcs_struct_cd = "";
					tcs_struct_dt = "";
					TCS_factor="";
					
					tds_amt = "";
					tds_struct_cd = "";
					tds_struct_dt = "";
					tds_factor = "";
				}
				else if(inv_flag.equals("LP"))// BHAUMIK20251205 FOR LP
				{
					applicable_abbr="TDS";
					
					Vector temp_tcs = new Vector();
					
					temp_tcs=TaxCalc.TCS_TDSAmountCalculationWithInfo(conn, comp_cd, counterparty_cd, "C", "TDS", inv_flag,"P",cont_end_dt, gross_include_transport_tariff);
					
					tds_amt = nf.format(Double.parseDouble(""+temp_tcs.elementAt(0)));
					tds_struct_cd = ""+temp_tcs.elementAt(1);
					tds_struct_dt = ""+temp_tcs.elementAt(2);
					//tax_info = ""+temp_tcs.elementAt(3);
					//tax_struct_dtl = ""+temp_tcs.elementAt(4);
					tds_factor = ""+temp_tcs.elementAt(5);
					
					if(Double.doubleToRawLongBits(Double.parseDouble(tds_amt))==Double.doubleToRawLongBits(0))
					{
						tds_amt="";
					}
					if(!tds_factor.equals(""))
					{
						if(Double.doubleToRawLongBits(Double.parseDouble(tds_factor))==Double.doubleToRawLongBits(0))
						{
							tds_factor="";
						}
					}
				}
				else
				{
							
					total_transaction_amt=""+getTransactionAmount(comp_cd, counterparty_cd, financial_year, invoice_dt);
					//System.out.println("total_transaction_amt="+total_transaction_amt);
					//System.out.println("invoice_amt="+invoice_amt);
					if(sellerTurnoverFlag.equals("Y") && buyerTurnoverFlag.equals("Y"))
					{
						applicable_abbr="TDS";
					}
					else if(sellerTurnoverFlag.equals("N") && buyerTurnoverFlag.equals("Y"))
					{
						applicable_abbr="TDS";
					}
					else if(sellerTurnoverFlag.equals("Y") && buyerTurnoverFlag.equals("N"))
					{
						applicable_abbr="TCS";
					}
					else
					{
						applicable_abbr="NA";
					}
					
					//System.out.println(applicable_abbr);
					double total_difference=0;
					if(applicable_abbr.equals("TCS"))
					{
						if(Double.parseDouble(total_transaction_amt)>transaction_limit)
						{
							applicable_flag="Y";
							if(!invoice_amt.equals(""))
							{
								total_difference=Double.parseDouble(invoice_amt);
							}
						}
						else if(!invoice_amt.equals(""))
						{
							total_transaction_amt=""+(Double.parseDouble(total_transaction_amt)+Double.parseDouble(invoice_amt));
							if(Double.parseDouble(total_transaction_amt)>transaction_limit)
							{
								applicable_flag="Y";
								total_difference=Double.parseDouble(total_transaction_amt)-transaction_limit;
							}
							else
							{
								total_difference=0;
								applicable_abbr="NA";
							}
						}
						else
						{
							total_difference=0;
							applicable_abbr="NA";
						}
						//System.out.println(total_difference);
						if(total_difference>0)
						{
							Vector temp_tcs = new Vector();
							temp_tcs=TaxCalc.TCS_TDSAmountCalculationWithInfo(conn, comp_cd, counterparty_cd, "C", "TCS", "S","P",period_end_dt, nf.format(total_difference));
			
							applicable_amt = nf.format(Double.parseDouble(""+temp_tcs.elementAt(0)));
							tcs_struct_cd = ""+temp_tcs.elementAt(1);
							tcs_struct_dt = ""+temp_tcs.elementAt(2);
							//tax_info = ""+temp_tcs.elementAt(3);
							//tax_struct_dtl = ""+temp_tcs.elementAt(4);
							tcs_factor = Double.parseDouble(""+temp_tcs.elementAt(5));
							
							net_payable=nf.format(Double.parseDouble(net_payable)+Double.parseDouble(applicable_amt));
							
							/*if(Double.parseDouble(applicable_amt)==0)
							{
								applicable_amt="";
							}*/
						}
						
						/*if(tcs_factor==0)
						{
							TCS_factor="";
						}
						else*/
						{
							TCS_factor=nf3.format(tcs_factor);
						}
					}
					else if(applicable_abbr.equals("TDS"))
					{
						if(Double.parseDouble(total_transaction_amt)>transaction_limit)
						{
							//20230911 FOLLOWING IS NOT TESTED ERLIER, AMOUNT SHOULD BE CONSIDERED INCLUDING CHARGES
							/*if(!gross_amt1.equals(""))
							{
								total_difference=Double.parseDouble(gross_amt1);
							}*/
							
							if(!gross_include_transport_tariff.equals(""))
							{
								total_difference=Double.parseDouble(gross_include_transport_tariff);
							}
						}
						else if(!gross_amt1.equals(""))
						{
							//20230911 total_transaction_amt=""+(Double.parseDouble(total_transaction_amt)+Double.parseDouble(gross_amt1));
							
							total_transaction_amt=""+(Double.parseDouble(total_transaction_amt)+Double.parseDouble(gross_include_transport_tariff));
							
							if(Double.parseDouble(total_transaction_amt)>transaction_limit)
							{
								total_difference=Double.parseDouble(total_transaction_amt)-transaction_limit;
							}
							else
							{
								total_difference=0;
								applicable_abbr="NA";
							}
						}
						else
						{
							total_difference=0;
							applicable_abbr="NA";
						}
						
						if(total_difference>0)
						{
							Vector temp_tcs = new Vector();
							temp_tcs=TaxCalc.TCS_TDSAmountCalculationWithInfo(conn, comp_cd, counterparty_cd, "C", "TDS", "S","P",period_end_dt, nf.format(total_difference));
			
							tds_amt = nf.format(Double.parseDouble(""+temp_tcs.elementAt(0)));
							tds_struct_cd = ""+temp_tcs.elementAt(1);
							tds_struct_dt = ""+temp_tcs.elementAt(2);
							//tax_info = ""+temp_tcs.elementAt(3);
							//tax_struct_dtl = ""+temp_tcs.elementAt(4);
							tds_factor = ""+temp_tcs.elementAt(5);
							
							if(Double.doubleToRawLongBits(Double.parseDouble(tds_amt))==Double.doubleToRawLongBits(0))
							{
								tds_amt="";
							}
							if(!tds_factor.equals(""))
							{
								if(Double.doubleToRawLongBits(Double.parseDouble(tds_factor))==Double.doubleToRawLongBits(0))
								{
									tds_factor="";
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
	
	public void getTcsTdsInvDtl()
	{
		String function_nm="getTcsTdsInvDtl()";
		try
		{
			double total=0;
			queryString="SELECT AGMT_NO,CONT_NO,CONTRACT_TYPE,INVOICE_AMT,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY') "
					+ "FROM FMS_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE IN (?,?) "
					+ "AND FINANCIAL_YEAR=? AND APPROVED_FLAG=? AND INVOICE_DT <=TO_DATE(?,'DD/MM/YYYY') AND INV_FLAG IN ('F','DR') "
					+ "UNION ALL "
					+ "SELECT AGMT_NO,CONT_NO,CONTRACT_TYPE,INVOICE_AMT,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY') "
					+ "FROM FMS_DLNG_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE IN (?,?) "
					+ "AND FINANCIAL_YEAR=? AND APPROVED_FLAG=? AND INVOICE_DT <=TO_DATE(?,'DD/MM/YYYY') AND INV_FLAG IN ('F','DR') ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, "S");
			stmt.setString(4, "L");
			stmt.setString(5, financial_year);
			stmt.setString(6, "Y");
			stmt.setString(7, invoice_dt);
			stmt.setString(8, comp_cd);
			stmt.setString(9, counterparty_cd);
			stmt.setString(10, "F");
			stmt.setString(11, "E");
			stmt.setString(12, financial_year);
			stmt.setString(13, "Y");
			stmt.setString(14, invoice_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String agmtno=rset.getString(1)==null?"":rset.getString(1);
				String contno=rset.getString(2)==null?"":rset.getString(2);
				String cont_type=rset.getString(3)==null?"0":rset.getString(3);
				double inv_amt=rset.getDouble(4);
				String inv_no=rset.getString(5)==null?"":rset.getString(5);
				String inv_dt=rset.getString(6)==null?"":rset.getString(6);
				
				String deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmtno, "", contno, "",cont_type, "");
				
				String contRef="";
				queryString1="SELECT CONT_REF_NO  "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND CONT_NO=? AND AGMT_NO=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, cont_type);
				stmt1.setString(3, contno);
				stmt1.setString(4, agmtno);
				stmt1.setString(5, counterparty_cd);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					contRef=rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
				
				VDEAL_NO.add(deal_no);
				VCONT_REF_NO.add(contRef);
				VINVOICE_AMT.add(nf.format(inv_amt));
				VINVOICE_NO.add(inv_no);
				VINVOICE_DT.add(inv_dt);
				
				total+=inv_amt;
			}
			rset.close();
			stmt.close();
			
			queryString2="SELECT AGMT_NO,CONT_NO,CONTRACT_TYPE,INVOICE_AMT,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY') "
					+ "FROM FMS_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE IN (?,?) "
					+ "AND FINANCIAL_YEAR=? AND APPROVED_FLAG=? AND INVOICE_DT <=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND INVOICE_TYPE IN (?,?,?)";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, counterparty_cd);
			stmt2.setString(3, "S");
			stmt2.setString(4, "L");
			stmt2.setString(5, financial_year);
			stmt2.setString(6, "Y");
			stmt2.setString(7, invoice_dt);
			stmt2.setString(8, "CDR");
			stmt2.setString(9, "DR");
			stmt2.setString(10, "S");
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				String agmtno=rset2.getString(1)==null?"":rset2.getString(1);
				String contno=rset2.getString(2)==null?"":rset2.getString(2);
				String cont_type=rset2.getString(3)==null?"0":rset2.getString(3);
				double inv_amt=rset2.getDouble(4);
				String inv_no=rset2.getString(5)==null?"":rset2.getString(5);
				String inv_dt=rset2.getString(6)==null?"":rset2.getString(6);
				
				String deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmtno, "", contno, "",cont_type, "");
				
				String contRef="";
				queryString1="SELECT CONT_REF_NO  "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND CONT_NO=? AND AGMT_NO=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, cont_type);
				stmt1.setString(3, contno);
				stmt1.setString(4, agmtno);
				stmt1.setString(5, counterparty_cd);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					contRef=rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
				
				VDEAL_NO.add(deal_no);
				VCONT_REF_NO.add(contRef);
				VINVOICE_AMT.add(nf.format(inv_amt));
				VINVOICE_NO.add(inv_no);
				VINVOICE_DT.add(inv_dt);
				
				total+=inv_amt;
			}
			rset2.close();
			stmt2.close();
			
			total_InvoiceAmt=nf.format(total);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getExistingInvoiceDtl()
	{
		String function_nm="getExistingInvoiceDtl()";
		try
		{
			queryString="SELECT BU_CONTACT_PERSON_CD,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),"
					+ "EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),INVOICE_AMT,NET_PAYABLE_AMT,"
					+ "REMARK_1,REMARK_2,TCS_TDS,TCS_AMT,TCS_FACTOR,TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,TCS_STRUCT_CD,TO_CHAR(TCS_EFF_DT,'DD/MM/YYYY'),"
					+ "TDS_GROSS_AMT,TDS_GROSS_PERCENT,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),MARKET_MARGIN,MARKET_MARGIN_AMT,OTHER_CHARGES,OTHER_CHARGES_AMT,"
					+ "TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(APPROVED_DT,'DD/MM/YYYY HH24:MI:SS'),FINANCIAL_YEAR,SUG_QTY,SUG_PERCENT "
					+ "FROM FMS_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
					+ "AND BU_STATE_TIN=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND FINANCIAL_YEAR=? AND CARGO_NO=? AND INV_FLAG=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, agmt_no);
			stmt.setString(5, plant_seq);
			stmt.setString(6, bu_unit);
			stmt.setString(7, contract_type);
			stmt.setString(8, bu_state_tin);
			stmt.setString(9, period_start_dt);
			stmt.setString(10, period_end_dt);
			stmt.setString(11, exist_financial_year);
			stmt.setString(12, cargo_no);
			stmt.setString(13, inv_flag);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				submission_chk=true;
				
				bu_contact_person_cd=rset.getString(1)==null?"0":rset.getString(1);
				contact_person_cd=rset.getString(2)==null?"0":rset.getString(2);
				invoice_seq=rset.getString(3)==null?"":rset.getString(3);
				invoice_no=rset.getString(4)==null?"":rset.getString(4);
				invoice_dt=rset.getString(5)==null?"":rset.getString(5);
				invoice_due_dt=rset.getString(6)==null?"":rset.getString(6);
				
				exchng_rate_cd=rset.getString(11)==null?"":rset.getString(11);
				exchang_rate_dt=rset.getString(12)==null?"":rset.getString(12);
				exchang_rate=rset.getString(13)==null?"":nf2.format(rset.getDouble(13));
				
				if(operation.equals("INSERT"))
				{
					qty_mmbtu=rset.getString(7)==null?"":nf.format(rset.getDouble(7));
					price=rset.getString(8)==null?"":nf.format(rset.getDouble(8));
					price_cd=rset.getString(9)==null?"":rset.getString(9);
					if(!price.equals(""))
					{
						price=utilBean.RateNumberFormat(rset.getDouble(8), price_cd);
					}
					gross_amt=rset.getString(10)==null?"":nf.format(rset.getDouble(10));
					
					invoice_raised_in=rset.getString(14)==null?"":rset.getString(14);
					gross_amt1=rset.getString(15)==null?"":nf.format(rset.getDouble(15));
					tax_amt=rset.getString(16)==null?"":nf.format(rset.getDouble(16));
					tax_struct_cd=rset.getString(17)==null?"":rset.getString(17);
					tax_struct_dt=rset.getString(18)==null?"":rset.getString(18);
					invoice_amt=rset.getString(19)==null?"":nf.format(rset.getDouble(19));
					net_payable=rset.getString(20)==null?"":nf.format(rset.getDouble(20));
					
					applicable_abbr=rset.getString(23)==null?"":rset.getString(23);
					applicable_amt=rset.getString(24)==null?"":rset.getString(24);
					TCS_factor=rset.getString(25)==null?"":rset.getString(25);
					
					tax_struct_dtl=utilBean.getTaxDescr(conn, tax_struct_cd);
					
					transportation_charges=rset.getString(26)==null?"":nf2.format(rset.getDouble(26));
					transportation_amount=rset.getString(27)==null?"":nf.format(rset.getDouble(27));
					
					tcs_struct_cd=rset.getString(28)==null?"":rset.getString(28);
					tcs_struct_dt=rset.getString(29)==null?"":rset.getString(29);
					
					tds_amt=rset.getString(30)==null?"":nf.format(rset.getDouble(30));
					tds_factor=rset.getString(31)==null?"":nf.format(rset.getDouble(31));
					tds_struct_cd=rset.getString(32)==null?"":rset.getString(32);
					tds_struct_dt=rset.getString(33)==null?"":rset.getString(33);
					
					marketing_margin=rset.getString(34)==null?"":nf2.format(rset.getDouble(34));
					marketing_margin_amount=rset.getString(35)==null?"":nf.format(rset.getDouble(35));
					other_charges=rset.getString(36)==null?"":nf2.format(rset.getDouble(36));
					other_charges_amount=rset.getString(37)==null?"":nf.format(rset.getDouble(37));
					inv_entered_on = rset.getString(38)==null?"":rset.getString(38);
					inv_approved_on = rset.getString(39)==null?"":rset.getString(39);
					
					String fiscal_yr=rset.getString(40)==null?"":rset.getString(40);
					
					gross_include_transport_tariff=nf.format(Double.parseDouble(gross_amt1));
					if(!transportation_amount.equals(""))
					{
						gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(transportation_amount));
						isGrossIncTriff=true;
					}
					if(!marketing_margin_amount.equals(""))
					{
						gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(marketing_margin_amount));
						isGrossIncTriff=true;
					}
					if(!other_charges_amount.equals(""))
					{
						gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(other_charges_amount));
						isGrossIncTriff=true;
					}
					
					Vector VTAX_CODE = new Vector();
					Vector VTAX_DESCR = new Vector();
					Vector VTAX_AMT = new Vector();
					Vector VTAX_BASE_AMT = new Vector();
					Vector VTAX_FACTOR = new Vector();
					queryString1="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
							+ "FROM FMS_INV_TAX_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, bu_state_tin);
					//stmt1.setString(3, financial_year);
					stmt1.setString(3, fiscal_yr);
					stmt1.setString(4, invoice_seq);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						VTAX_CODE.add(rset1.getString(1)==null?"":rset1.getString(1));
						VTAX_DESCR.add(rset1.getString(2)==null?"":rset1.getString(2));
						VTAX_AMT.add(rset1.getString(3)==null?"":nf.format(rset1.getDouble(3)));
						VTAX_BASE_AMT.add(rset1.getString(4)==null?"":nf.format(rset1.getDouble(4)));
						VTAX_FACTOR.add("");
					}
					rset1.close();
					stmt1.close();
					
					Vector VTEMP_TAX_DTL = new Vector();
					
					VTEMP_TAX_DTL.add(VTAX_CODE);
					VTEMP_TAX_DTL.add(VTAX_DESCR);
					VTEMP_TAX_DTL.add(VTAX_AMT);
					VTEMP_TAX_DTL.add(VTAX_BASE_AMT);
					VTEMP_TAX_DTL.add(VTAX_FACTOR);
					
					VMULTI_TAX_STRUCT.add(VTEMP_TAX_DTL);
					
					queryString1="SELECT TO_CHAR(STORAGE_DT,'DD/MM/YYYY'),OPEN_BALANCE_QTY,OFFTAKE_QTY,RATE,RATE_TYPE,DAY_DISCOUNT "
							+ "FROM FMS_INV_STORAGE_CRG_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=?";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, bu_state_tin);
					//stmt1.setString(3, financial_year);
					stmt1.setString(3, fiscal_yr);
					stmt1.setString(4, invoice_seq);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						String storageInv=rset1.getString(2)==null?"":nf.format(rset1.getDouble(2));
						VSTORAGE_DATE.add(rset1.getString(1)==null?"":rset1.getString(1));
						VSTORAGE_INVENTORY.add(storageInv);
						VOFFTAKE_QTY.add(rset1.getString(3)==null?"":nf.format(rset1.getDouble(3)));
						
						String rate=rset1.getString(4)==null?"":utilBean.RateNumberFormat(rset1.getDouble(4),price_cd);
						String rate_type=rset1.getString(5)==null?"":rset1.getString(5);
						String discount_day=rset1.getString(6)==null?"":rset1.getString(6);
						VRATE_TYPE.add(rate_type);
						VUSER_DEFINE.add(rate_type.equals("U")?rate:"");
						VSTORAGE_CHARGE.add(rate_type.equals("U")?"":rate);
						
						String storage_amt="";
						if(!storageInv.equals("") && !rate.equals("") && discount_day.equals("N"))
						{
							storage_amt=nf.format(Double.parseDouble(storageInv) * Double.parseDouble(rate));
						}
						
						VSTORAGE_AMT.add(storage_amt);
						VDISCOUNT_FLAG.add(discount_day);
					}
					rset1.close();
					stmt1.close();
					
					queryString1="SELECT DISTINCT SEC_INT_REF "
							+ "FROM FMS_INV_ADV_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? "
							+ "ORDER BY SEC_INT_REF";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, bu_state_tin);
					//stmt1.setString(3, financial_year);
					stmt1.setString(3, fiscal_yr);
					stmt1.setString(4, invoice_seq);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						VEXISTING_RECEIPT_VOUCHER.add(rset1.getString(1)==null?"":rset1.getString(1));
						if(operation.equals("INSERT") && submission_chk)
						{
							VRECEIPT_VOUCHER_MST.add(rset1.getString(1)==null?"":rset1.getString(1));
						}
					}
					rset1.close();
					stmt1.close();
					
					
				}
				
				if(inv_flag.equals("UG"))
				{
					qty_mmbtu=rset.getString(7)==null?"":nf.format(rset.getDouble(7));
					price=rset.getString(8)==null?"":nf.format(rset.getDouble(8));
					price_cd=rset.getString(9)==null?"":rset.getString(9);
					if(!price.equals(""))
					{
						price=utilBean.RateNumberFormat(rset.getDouble(8), price_cd);
					}
					
					sug_qty=rset.getString(41)==null?"":nf.format(rset.getDouble(41));
					sug_percentage=rset.getString(42)==null?"":nf.format(rset.getDouble(42));
					
				}
				
				remark_1=rset.getString(21)==null?"":rset.getString(21);
				//remark_2=rset.getString(22)==null?"":rset.getString(22);
				
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getInvoiceDateCalc()
	{
		String function_nm="getInvoiceDateCalc()";
		try
		{
			if((operation.equals("INSERT") && !submission_chk) || operation.equals("MODIFY"))
			{
				if(inv_dt.equals(""))
				{
					if(operation.equals("INSERT"))
					{
						invoice_dt=period_end_dt;
					}
				}
				else
				{
					invoice_dt=inv_dt;
				}
				
				financial_year=utilDate.getFinancialYear(invoice_dt);
				
				//HP202301011 AS DISCUSSED WITH VIJAY, INVOICE DUE DATE SHOULD BE CALCULATED BASED ON SYSTEM DATE
				String systemDt=utilDate.getSysdate();
				invoice_due_dt=""+utilBean.DueDateCalculation(conn,systemDt, due_days,consider_due_dt_in,exclude_sat,comp_cd,holiday_state);
			}
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
			String invDt="";
			String periodEndDt="";
			queryString="SELECT BU_CONTACT_PERSON_CD,CONTACT_PERSON_CD,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD-MON-YY'),"
					+ "TO_CHAR(DUE_DT,'DD-MON-YY'),TO_CHAR(PERIOD_START_DT,'DD-MON-YY'),TO_CHAR(PERIOD_END_DT,'DD-MON-YY'),REMARK_1,REMARK_2,"
					+ "ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_VALUE,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),"
					+ "INVOICE_AMT,NET_PAYABLE_AMT,TCS_TDS,TCS_AMT,TCS_FACTOR,CHECKED_FLAG,APPROVED_FLAG, "
					+ "INVOICE_ID_SEQ,TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),EXCHG_RATE_TYPE,"
					+ "INVOICE_RAISED_IN,MARKET_MARGIN,MARKET_MARGIN_AMT,OTHER_CHARGES,OTHER_CHARGES_AMT,SUG_QTY,SUG_PERCENT,"
					+ "TDS_GROSS_AMT,TDS_GROSS_PERCENT,TDS_STRUCT_CD,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY') "
					+ "FROM FMS_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
					+ "AND BU_STATE_TIN=? "
					+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND FINANCIAL_YEAR=? "
					+ "AND INVOICE_SEQ=? AND CARGO_NO=? AND INV_FLAG=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, agmt_no);
			stmt.setString(5, plant_seq);
			stmt.setString(6, bu_unit);
			stmt.setString(7, contract_type);
			stmt.setString(8, bu_state_tin);
			stmt.setString(9, period_start_dt);
			stmt.setString(10, period_end_dt);
			stmt.setString(11, financial_year);
			stmt.setString(12, invoice_seq);
			stmt.setString(13, cargo_no);
			stmt.setString(14, inv_flag);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				bu_contact_person_cd=rset.getString(1)==null?"0":rset.getString(1);
				contact_person_cd=rset.getString(2)==null?"0":rset.getString(2);
				
				invoice_id_seq=rset.getString(26)==null?"":rset.getString(26);
				invoice_no=rset.getString(3)==null?"":rset.getString(3);
				invoice_dt=rset.getString(4)==null?"":rset.getString(4);
				invoice_due_dt=rset.getString(5)==null?"":rset.getString(5);
				inv_period_start_dt=rset.getString(6)==null?"":rset.getString(6);
				inv_period_end_dt=rset.getString(7)==null?"":rset.getString(7);
				
				remark_1=rset.getString(8)==null?"":rset.getString(8);
				remark_2=rset.getString(9)==null?"":rset.getString(9);
				
				qty_mmbtu=nf.format(rset.getDouble(10));
				price_cd=rset.getString(12)==null?"":rset.getString(12);
				price=utilBean.RateNumberFormat(rset.getDouble(11), price_cd);
				gross_amt=nf.format(rset.getDouble(13));
				exchang_rate=nf2.format(rset.getDouble(14));
				gross_amt1=nf.format(rset.getDouble(15));
				tax_amt=nf.format(rset.getDouble(16));
				tax_struct_cd=rset.getString(17)==null?"":rset.getString(17);
				tax_struct_dt=rset.getString(18)==null?"":rset.getString(18);
				invoice_amt=nf.format(rset.getDouble(19));
				net_payable=nf.format(rset.getDouble(20));
				applicable_abbr=rset.getString(21)==null?"":rset.getString(21);
				applicable_amt=rset.getString(22)==null?"":rset.getString(22);
				TCS_factor=rset.getString(23)==null?"":rset.getString(23);
				if(!TCS_factor.equals(""))
				{
					TCS_factor=nf3.format(rset.getDouble(23));
				}
				
				if(activityFlag.equals("CHECK")) {
					activity_value=rset.getString(24)==null?"":rset.getString(24);
				}else if(activityFlag.equals("APPROVE")) {
					activity_value=rset.getString(25)==null?"":rset.getString(25);
				}
				
				tax_struct_dtl=utilBean.getTaxDescr(conn, tax_struct_cd);
				
				transportation_charges=rset.getString(27)==null?"":nf2.format(rset.getDouble(27));
				transportation_amount=rset.getString(28)==null?"":nf.format(rset.getDouble(28));
				
				exchng_rate_cd=rset.getString(29)==null?"":rset.getString(29);
				exchang_rate_dt=rset.getString(30)==null?"":rset.getString(30);
				exchang_rate_type=rset.getString(31)==null?"":rset.getString(31);
				invoice_raised_in=rset.getString(32)==null?"":rset.getString(32);
				
				marketing_margin=rset.getString(33)==null?"":nf2.format(rset.getDouble(33));
				marketing_margin_amount=rset.getString(34)==null?"":nf.format(rset.getDouble(34));
				other_charges=rset.getString(35)==null?"":nf2.format(rset.getDouble(35));
				other_charges_amount=rset.getString(36)==null?"":nf.format(rset.getDouble(36));
				
				sug_qty=rset.getString(37)==null?"":nf.format(rset.getDouble(37));
				sug_percentage=rset.getString(38)==null?"":nf.format(rset.getDouble(38));
				
				tds_amt=rset.getString(39)==null?"":nf.format(rset.getDouble(39));
				tds_factor=rset.getString(40)==null?"":nf.format(rset.getDouble(40));
				tds_struct_cd=rset.getString(41)==null?"":rset.getString(41);
				
				invDt=rset.getString(42)==null?"":rset.getString(42);
				periodEndDt=rset.getString(43)==null?"":rset.getString(43);
				
				gross_include_transport_tariff=nf.format(Double.parseDouble(gross_amt1));
				if(!transportation_amount.equals(""))
				{
					if(Double.parseDouble(transportation_amount) > 0)
					{
						gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(transportation_amount));
						isGrossIncTriff=true;
					}
				}
				if(!marketing_margin_amount.equals(""))
				{
					if(Double.parseDouble(marketing_margin_amount) > 0)
					{
						gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(marketing_margin_amount));
						isGrossIncTriff=true;
					}
				}
				if(!other_charges_amount.equals(""))
				{
					if(Double.parseDouble(other_charges_amount) > 0)
					{
						gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(other_charges_amount));
						isGrossIncTriff=true;
					}
				}
			}
			rset.close();
			stmt.close();
			
			///////////////TDS RECHECK CALCULATION//////////////////////////////////////////
			if(contract_type.equals("Q") || contract_type.equals("O"))
			{
				if(inv_flag.equals("UG"))
				{
					new_applicable_abbr="NA";
					
					new_tds_amt = "";
					new_tds_struct_cd = "";
					new_tds_struct_dt = "";
					new_tds_factor = "";
				}
				else
				{
					new_applicable_abbr="TDS";
					
					//ReGas Invoice TDS will be picked up
					Vector temp_tcs = new Vector();
					if(inv_flag.equals("ST"))
					{
						temp_tcs=TaxCalc.TCS_TDSAmountCalculationWithInfo(conn, comp_cd, counterparty_cd, "C", "TDS", inv_flag,"S",periodEndDt, gross_include_transport_tariff);
					}
					else
					{
						temp_tcs=TaxCalc.TCS_TDSAmountCalculationWithInfo(conn, comp_cd, counterparty_cd, "C", "TDS", "SI","S",periodEndDt, gross_include_transport_tariff);
					}
					
					new_tds_amt = nf.format(Double.parseDouble(""+temp_tcs.elementAt(0)));
					new_tds_struct_cd = ""+temp_tcs.elementAt(1);
					new_tds_struct_dt = ""+temp_tcs.elementAt(2);
					//tax_info = ""+temp_tcs.elementAt(3);
					//tax_struct_dtl = ""+temp_tcs.elementAt(4);
					new_tds_factor = ""+temp_tcs.elementAt(5);
					
					if(Double.doubleToRawLongBits(Double.parseDouble(new_tds_amt))==Double.doubleToRawLongBits(0))
					{
						new_tds_amt="";
					}
					if(!new_tds_factor.equals(""))
					{
						if(Double.doubleToRawLongBits(Double.parseDouble(new_tds_factor))==Double.doubleToRawLongBits(0))
						{
							new_tds_factor="";
						}
					}
				}
			}
			else if(contract_type.equals("X"))
			{
				new_applicable_abbr="NA";
				
				new_tds_amt = "";
				new_tds_struct_cd = "";
				new_tds_struct_dt = "";
				new_tds_factor = "";
			}
			else
			{
				previousFinancialYear=""+utilDate.getPreviousFinancialYear(invDt);
				
				queryString="SELECT TURNOVER_FLAG "
						+ "FROM FMS_ENTITY_TURNOVER_DTL "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND ENTITY=? AND FINANCIAL_YEAR=? AND TURNOVER_CD=? ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, "C");
				stmt.setString(4, previousFinancialYear);
				stmt.setString(5, "1");
				rset=stmt.executeQuery();
				if(rset.next())
				{
					buyerTurnoverFlag=rset.getString(1)==null?"":rset.getString(1);
				}
				rset.close();
				stmt.close();
				
				queryString1="SELECT TURNOVER_FLAG "
						+ "FROM FMS_ENTITY_TURNOVER_DTL "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND ENTITY=? AND FINANCIAL_YEAR=? AND TURNOVER_CD=? ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, comp_cd);
				stmt1.setString(3, "B");
				stmt1.setString(4, previousFinancialYear);
				stmt1.setString(5, "1");
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					sellerTurnoverFlag=rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
				
				double total_difference=0;
				total_transaction_amt=""+getTransactionAmount(comp_cd, counterparty_cd, financial_year, invDt);
				
				if(sellerTurnoverFlag.equals("Y") && buyerTurnoverFlag.equals("Y"))
				{
					new_applicable_abbr="TDS";
				}
				else if(sellerTurnoverFlag.equals("N") && buyerTurnoverFlag.equals("Y"))
				{
					new_applicable_abbr="TDS";
				}
				else if(sellerTurnoverFlag.equals("Y") && buyerTurnoverFlag.equals("N"))
				{
					new_applicable_abbr="TCS";
				}
				else
				{
					new_applicable_abbr="NA";
				}
				
				if(new_applicable_abbr.equals("TCS"))
				{
					/*
					if(Double.parseDouble(total_transaction_amt)>transaction_limit)
					{
						applicable_flag="Y";
						if(!invoice_amt.equals(""))
						{
							total_difference=Double.parseDouble(invoice_amt);
						}
					}
					else if(!invoice_amt.equals(""))
					{
						total_transaction_amt=""+(Double.parseDouble(total_transaction_amt)+Double.parseDouble(invoice_amt));
						if(Double.parseDouble(total_transaction_amt)>transaction_limit)
						{
							applicable_flag="Y";
							total_difference=Double.parseDouble(total_transaction_amt)-transaction_limit;
						}
						else
						{
							total_difference=0;
							applicable_abbr="NA";
						}
					}
					else
					{
						total_difference=0;
						applicable_abbr="NA";
					}
					//System.out.println(total_difference);
					if(total_difference>0)
					{
						Vector temp_tcs = new Vector();
						temp_tcs=TaxCalc.TCS_TDSAmountCalculationWithInfo(conn, comp_cd, counterparty_cd, "C", "TCS", "S","P",period_end_dt, nf.format(total_difference));
		
						applicable_amt = nf.format(Double.parseDouble(""+temp_tcs.elementAt(0)));
						tcs_struct_cd = ""+temp_tcs.elementAt(1);
						tcs_struct_dt = ""+temp_tcs.elementAt(2);
						//tax_info = ""+temp_tcs.elementAt(3);
						//tax_struct_dtl = ""+temp_tcs.elementAt(4);
						tcs_factor = Double.parseDouble(""+temp_tcs.elementAt(5));
						
						net_payable=nf.format(Double.parseDouble(net_payable)+Double.parseDouble(applicable_amt));
					}
					
					TCS_factor=nf3.format(tcs_factor);
					*/
				}
				else if(new_applicable_abbr.equals("TDS"))
				{
					if(Double.parseDouble(total_transaction_amt)>transaction_limit)
					{
						if(!gross_include_transport_tariff.equals(""))
						{
							total_difference=Double.parseDouble(gross_include_transport_tariff);
						}
					}
					else if(!gross_include_transport_tariff.equals(""))
					{
						total_transaction_amt=""+(Double.parseDouble(total_transaction_amt)+Double.parseDouble(gross_include_transport_tariff));
						
						if(Double.parseDouble(total_transaction_amt)>transaction_limit)
						{
							total_difference=Double.parseDouble(total_transaction_amt)-transaction_limit;
						}
						else
						{
							total_difference=0;
							new_applicable_abbr="NA";
						}
					}
					else
					{
						total_difference=0;
						new_applicable_abbr="NA";
					}
					
					if(total_difference>0)
					{
						Vector temp_tcs = new Vector();
						temp_tcs=TaxCalc.TCS_TDSAmountCalculationWithInfo(conn, comp_cd, counterparty_cd, "C", "TDS", "S","P",periodEndDt, nf.format(total_difference));
		
						new_tds_amt = nf.format(Double.parseDouble(""+temp_tcs.elementAt(0)));
						new_tds_struct_cd = ""+temp_tcs.elementAt(1);
						new_tds_struct_dt = ""+temp_tcs.elementAt(2);
						//tax_info = ""+temp_tcs.elementAt(3);
						//tax_struct_dtl = ""+temp_tcs.elementAt(4);
						new_tds_factor = ""+temp_tcs.elementAt(5);
						
						if(Double.doubleToRawLongBits(Double.parseDouble(new_tds_amt))==Double.doubleToRawLongBits(0))
						{
							new_tds_amt="";
						}
						if(!new_tds_factor.equals(""))
						{
							if(Double.doubleToRawLongBits(Double.parseDouble(new_tds_factor))==Double.doubleToRawLongBits(0))
							{
								new_tds_factor="";
							}
						}
					}
				}
			}
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			String boe_number="";
			String boe_date="";
			String ship_nm="";

			if(contract_type.equals("O") || contract_type.equals("Q"))
			{
				queryString="SELECT A.CONT_REF_NO,TO_CHAR(A.DDA_DT,'DD-MON-YY'),A.CONT_REF_NO,B.SHIP_CD,B.BOE_NO,TO_CHAR(B.BOE_DT,'DD-MON-YY'),TO_CHAR(SIGNING_DT,'DD-MON-YY'),"
						+ "B.CSOC_QTY,A.MDCQ_PERCENTAGE,TO_CHAR(B.ACTUAL_RECPT_DT,'DD-MON-YY'),TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY') "
						+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_CARGO_DTL B "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? "
						+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.AGMT_TYPE=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND B.CARGO_NO=? "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
						+ ""
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, "C");
				stmt.setString(4, agmt_no);
				stmt.setString(5, agmt_rev_no);
				stmt.setString(6, "A");
				stmt.setString(7, cont_no);
				stmt.setString(8, contract_type);
				stmt.setString(9, cargo_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					contRef=rset.getString(1)==null?"":rset.getString(1);
					dda_dt=rset.getString(2)==null?"":rset.getString(2);
					//cargoRef=rset.getString(3)==null?"":rset.getString(3);
					
					String ship_cd=rset.getString(4)==null?"":rset.getString(4);
					ship_nm=utilBean.getShipName(conn,ship_cd);
					boe_number = rset.getString(5)==null?"":rset.getString(5);
					boe_date=rset.getString(6)==null?"":rset.getString(6);
					
					signingDt=rset.getString(7)==null?"":rset.getString(7);
					dcq=rset.getString(8)==null?"":rset.getString(8);
					mdcq_percentage=rset.getDouble(9);
					if(Double.doubleToRawLongBits(mdcq_percentage)==Double.doubleToRawLongBits(0))
					{
						mdcq_percentage=100;
					}
					
					arrivalDt=rset.getString(10)==null?"":rset.getString(10);
				}
				rset.close();
				stmt.close();
				
				queryString1="SELECT TO_CHAR(SIGNING_DT,'DD-MON-YY')  "
						+ "FROM FMS_LTCORA_AGMT_MST A "
						+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
						+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_LTCORA_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.BUY_SALE=B.BUY_SALE) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, "A");
				stmt1.setString(3, agmt_no);
				stmt1.setString(4, counterparty_cd);
				stmt1.setString(5, "C");
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					agmtSigningDt=rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
			}
			else
			{
				queryString1="SELECT CONT_REF_NO,TO_CHAR(DDA_DT,'DD-MON-YY'),TRADE_REF_NO,TO_CHAR(SIGNING_DT,'DD-MON-YY'),AGMT_BASE,DCQ,MDCQ_PERCENTAGE  "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND CONT_NO=? AND AGMT_NO=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, contract_type);
				stmt1.setString(3, cont_no);
				stmt1.setString(4, agmt_no);
				stmt1.setString(5, counterparty_cd);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					contRef=rset1.getString(1)==null?"":rset1.getString(1);
					dda_dt=rset1.getString(2)==null?"":rset1.getString(2);
					String tradeRef=rset1.getString(3)==null?"":rset1.getString(3);
					signingDt=rset1.getString(4)==null?"":rset1.getString(4);
					agmt_base=rset1.getString(5)==null?"":rset1.getString(5);
					dcq=rset1.getString(6)==null?"":rset1.getString(6);
					
					mdcq_percentage=rset1.getDouble(7);
					if(Double.doubleToRawLongBits(mdcq_percentage)==Double.doubleToRawLongBits(0))
					{
						mdcq_percentage=100;
					}
					
					if(contract_type.equals("X"))
					{
						contRef=tradeRef;
					}
				}
				rset1.close();
				stmt1.close();
				
				if(contract_type.equals("S"))
				{
					queryString2="SELECT TO_CHAR(SIGNING_DT,'DD-MON-YY')  "
							+ "FROM FMS_AGMT_MST A "
							+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
							+ "AND AGMT_NO=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) ";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, "F");
					stmt2.setString(3, agmt_no);
					stmt2.setString(4, counterparty_cd);
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						agmtSigningDt=rset2.getString(1)==null?"":rset2.getString(1);
					}
					rset2.close();
					stmt2.close();
				}
			}
			
			queryString2="SELECT CONTACT_PERSON "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND SEQ_NO=? AND ADDR_FLAG=? AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.TYPE=B.TYPE "
					+ "AND EFF_DT <= TO_DATE(?,'DD-MON-YY'))";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, counterparty_cd);
			stmt2.setString(3, "C");
			stmt2.setString(4, contact_person_cd);
			stmt2.setString(5, "P"+plant_seq);
			stmt2.setString(6, "RLNG");
			stmt2.setString(7, invoice_dt);
			rset2=stmt2.executeQuery();
			if(rset2.next())
			{
				contact_person_nm=rset2.getString(1)==null?"":rset2.getString(1);
			}
			rset2.close();
			stmt2.close();
			
			queryString3="SELECT CONTACT_PERSON "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND SEQ_NO=? AND ADDR_FLAG=? AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.TYPE=B.TYPE "
					+ "AND EFF_DT <= TO_DATE(?,'DD-MON-YY'))";
			stmt3=conn.prepareStatement(queryString3);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, comp_cd);
			stmt3.setString(3, "B");
			stmt3.setString(4, bu_contact_person_cd);
			stmt3.setString(5, "P"+bu_unit);
			stmt3.setString(6, "RLNG");
			stmt3.setString(7, invoice_dt);
			rset3=stmt3.executeQuery();
			if(rset3.next())
			{
				bu_contact_person_nm=rset3.getString(1)==null?"":rset3.getString(1);
			}
			rset3.close();
			stmt3.close();
			
			HashMap bu_plant_detail=utilBean.getCounterpartyPlantDetail(conn,comp_cd, "B", comp_cd, bu_unit);
			bu_plantAddress=""+bu_plant_detail.get("plant_address");
			bu_plantCity=""+bu_plant_detail.get("plant_city");
			bu_plantState=""+bu_plant_detail.get("plant_state");
			bu_plantPin=""+bu_plant_detail.get("plant_pin");
			bu_plantNm=""+bu_plant_detail.get("plant_name");
			
			HashMap plant_detail=utilBean.getCounterpartyPlantDetail(conn,comp_cd, "C", counterparty_cd, plant_seq);
			plantAddress=""+plant_detail.get("plant_address");
			plantCity=""+plant_detail.get("plant_city");
			plantState=""+plant_detail.get("plant_state");
			plantPin=""+plant_detail.get("plant_pin");
			
			if(contract_type.equals("O") || contract_type.equals("Q"))
			{
				tax_info="State : "+plantState;
				tax_info+="<br>State Code : "+utilBean.getState_TIN(conn, comp_cd, counterparty_cd, "C", plant_seq);
				//String temp_tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "C", counterparty_cd, plant_seq);
				//tax_info+=temp_tax_info.replaceAll("\n", "<br>");
				
				queryString = "SELECT A.STAT_NO, TO_CHAR(A.EFF_DT,'DD/MM/YYYY'), B.STAT_NM "
						+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B "
						+ "WHERE A.COUNTERPARTY_CD=? AND A.ENTITY=? "
						+ "AND A.PLANT_SEQ_NO=? AND A.COMPANY_CD=? "
						+ "AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL AND B.STAT_CD IN ('1003','1001')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, counterparty_cd);
				stmt.setString(2, "C");
				stmt.setString(3, plant_seq);
				stmt.setString(4, comp_cd);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					String no = rset.getString(1)==null?"":rset.getString(1);
					String nm = rset.getString(3)==null?"":rset.getString(3);
	
					tax_info+="<br>"+nm+" : "+no;
				}
				stmt.close();
				rset.close();
				
				bu_tax_info="State : "+bu_plantState;
				bu_tax_info+="<br>State Code : "+utilBean.getState_TIN(conn, comp_cd, comp_cd, "B", bu_unit);
				//String temp_bu_tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "B", comp_cd, bu_unit);
				//bu_tax_info+=temp_bu_tax_info.replaceAll("\n", "<br>");
				
				queryString = "SELECT A.STAT_NO, TO_CHAR(A.EFF_DT,'DD/MM/YYYY'), B.STAT_NM "
						+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B "
						+ "WHERE A.COUNTERPARTY_CD=? AND A.ENTITY=? "
						+ "AND A.PLANT_SEQ_NO=? AND A.COMPANY_CD=? "
						+ "AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL AND B.STAT_CD IN ('1003','1001')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, "B");
				stmt.setString(3, bu_unit);
				stmt.setString(4, comp_cd);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					String no = rset.getString(1)==null?"":rset.getString(1);
					String nm = rset.getString(3)==null?"":rset.getString(3);
	
					bu_tax_info+="<br>"+nm+" : "+no;
				}
				stmt.close();
				rset.close();
				
				bu_tax_info+="<br>SAC : 999799"
						+ "<br>Description of Service : Other Miscellaneous services - Other Services n.e.c."
						+ "<br>Place Of Supply : "+plantState; //WILL BE CUSTOMER PLANT STATE AS DISCUSSED BY JAYASRI MAM WITH VIJAY ON 20250725
			}
			else
			{
				tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "C", counterparty_cd, plant_seq);
				tax_info=tax_info.replaceAll("\n", "<br>");
				
				bu_tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "B", comp_cd, bu_unit);
				bu_tax_info=bu_tax_info.replaceAll("\n", "<br>");
			}
			
			couterpty_nm=""+utilBean.getCounterpartyName(conn,counterparty_cd);
			int srno=0;
			
			String parameter="counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+""
					+ "&cont_rev="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev_no+"&plant_seq="+plant_seq+"&billing_cycle="+billing_cycle+""
					+ "&period_start_dt="+period_start_dt+"&period_end_dt="+period_end_dt+"&bu_unit="+bu_unit+"&financial_year="+financial_year+""
					+ "&bu_state_tin="+bu_state_tin+"&invoice_seq="+invoice_seq+"&activityFlag="+activityFlag+"&cargo_no="+cargo_no+"&inv_flag="+inv_flag;

			price_cd_nm=""+utilBean.getRateUnitNm(conn,price_cd);
			
			if(inv_flag.equals("UG"))
			{
				String mnthYr="";
				if(!period_end_dt.equals(""))
				{
					mnthYr=period_end_dt.substring(3,period_end_dt.length());
					String[] split= mnthYr.split("/");
					mnthYr=utilDate.getMonthName(period_end_dt)+"-"+split[1];
				}
				
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("Actual Quantity of LNG discharged during month of "+mnthYr+"");
				VPDF_COL3.add("<a onclick=openAtt1('rpt_view_attachment1.jsp?"+parameter+"')>Att1</a>");
				VPDF_COL4.add("");
				VPDF_COL5.add(qty_mmbtu);
				VPDF_COL6.add("");
				VPDF_COL7.add("");
				
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add(sug_percentage+"% of above as SUG (System Use Gas)");
				VPDF_COL3.add("");
				VPDF_COL4.add("");
				VPDF_COL5.add(sug_qty);
				VPDF_COL6.add("");
				VPDF_COL7.add("");
				
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("Value of SUG (only for the purpose of GST payment on LTCORA Services)");
				VPDF_COL3.add("<a onclick=openAtt2('rpt_view_attachment2.jsp?"+parameter+"')>Att2</a>");
				VPDF_COL4.add(""+utilBean.getRateUnitNm(conn,price_cd));
				VPDF_COL5.add("");
				VPDF_COL6.add(price);
				VPDF_COL7.add(gross_amt);
			}
			else
			{
				if(contract_type.equals("O") || contract_type.equals("Q"))
				{
					if(inv_flag.equals("ST"))
					{
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("Storage Charges For The Extended Storage Duration For Cargo Arrived on "+arrivalDt);
						VPDF_COL3.add("<a onclick=openAtt1('rpt_view_attachment1.jsp?"+parameter+"')>Att1</a>");
						VPDF_COL4.add(""+price_cd_nm);
						VPDF_COL5.add("<a onclick=openAtt1('rpt_view_attachment1.jsp?"+parameter+"')>As per Att1</a>");
						VPDF_COL6.add("<a onclick=openAtt1('rpt_view_attachment1.jsp?"+parameter+"')>As per Att1</a>");
						VPDF_COL7.add(""+gross_amt);
					}
					else
					{
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("Natural Gas (Regasified)");
						VPDF_COL3.add("<a onclick=openAtt1('rpt_view_attachment1.jsp?"+parameter+"')>Att1</a>");
						VPDF_COL4.add("");
						VPDF_COL5.add(qty_mmbtu);
						VPDF_COL6.add("");
						VPDF_COL7.add("");
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("LTCORA Tariff");
						VPDF_COL3.add("");
						VPDF_COL4.add(price_cd_nm+"/MMBTU");
						VPDF_COL5.add("");
						VPDF_COL6.add(price);
						VPDF_COL7.add("");
						
						if(price_cd.equals("2"))
						{
							srno+=1;
							VPDF_COL1.add(srno);
							VPDF_COL2.add("Gross Amount");
							VPDF_COL3.add("");
							VPDF_COL4.add(price_cd_nm);
							VPDF_COL5.add("");
							VPDF_COL6.add("");
							VPDF_COL7.add(gross_amt);
						}
					}
				}
				else
				{
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("Natural Gas");
					VPDF_COL3.add("<a onclick=openAtt1('rpt_view_attachment1.jsp?"+parameter+"')>Att1</a>");
					VPDF_COL4.add(price_cd_nm);
					VPDF_COL5.add(qty_mmbtu);
					VPDF_COL6.add(price);
					VPDF_COL7.add(gross_amt);
				}
				
			}
			
			if(price_cd.equals("2"))
			{
				/*srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("Gross Amount");
				VPDF_COL3.add("");
				VPDF_COL4.add("USD");
				VPDF_COL5.add("");
				VPDF_COL6.add("");
				VPDF_COL7.add(gross_amt);
				*/
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("Exchange Rate");
				VPDF_COL3.add("<a onclick=openAtt2('rpt_view_attachment2.jsp?"+parameter+"')>Att2</a>");
				VPDF_COL4.add("INR/USD");
				VPDF_COL5.add("");
				VPDF_COL6.add(exchang_rate);
				VPDF_COL7.add("");
			}
			
			if(!inv_flag.equals("UG"))
			{
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("Gross Amount");
				VPDF_COL3.add("");
				VPDF_COL4.add("INR");
				VPDF_COL5.add("");
				VPDF_COL6.add("");
				VPDF_COL7.add(gross_amt1);
			}
			
			//if(agmt_base.equals("D"))
			if(!transportation_charges.equals(""))
			{
				if(Double.parseDouble(transportation_amount) > 0)
				{
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("Transportation Tariff");
					VPDF_COL3.add("");
					VPDF_COL4.add("INR");
					VPDF_COL5.add("");
					VPDF_COL6.add(transportation_charges);
					VPDF_COL7.add(transportation_amount);
				}
			}
			
			if(!marketing_margin.equals(""))
			{
				if(Double.parseDouble(marketing_margin_amount) > 0)
				{
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("Marketing Margin");
					VPDF_COL3.add("");
					VPDF_COL4.add("INR");
					VPDF_COL5.add("");
					VPDF_COL6.add(marketing_margin);
					VPDF_COL7.add(marketing_margin_amount);
				}
			}
			
			if(!other_charges.equals(""))
			{
				if(Double.parseDouble(other_charges_amount) > 0)
				{
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("Other Charges");
					VPDF_COL3.add("");
					VPDF_COL4.add("INR");
					VPDF_COL5.add("");
					VPDF_COL6.add(other_charges);
					VPDF_COL7.add(other_charges_amount);
				}
			}
			
			if(isGrossIncTriff)
			{
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("Total Gross Amount");
				VPDF_COL3.add("");
				VPDF_COL4.add("INR");
				VPDF_COL5.add("");
				VPDF_COL6.add("");
				VPDF_COL7.add(gross_include_transport_tariff);
			}
			
			srno+=1;
			VPDF_COL1.add(srno);
			VPDF_COL2.add("Tax ("+tax_struct_dtl+")");
			VPDF_COL3.add("");
			VPDF_COL4.add("INR");
			VPDF_COL5.add("");
			VPDF_COL6.add("");
			VPDF_COL7.add(tax_amt);
			
			double temp_srno=srno;
			queryString1="SELECT COUNT(*) "
					+ "FROM FMS_INV_TAX_DTL "
					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
					+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? ";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, bu_state_tin);
			stmt1.setString(3, financial_year);
			stmt1.setString(4, invoice_seq);
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				if(rset1.getInt(1)>1)
				{
					queryString="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
							+ "FROM FMS_INV_TAX_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? ";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, bu_state_tin);
					stmt.setString(3, financial_year);
					stmt.setString(4, invoice_seq);
					rset=stmt.executeQuery();
					while(rset.next())
					{
						temp_srno=temp_srno+0.1;
						VPDF_COL1.add(nf0.format(temp_srno));
						VPDF_COL2.add(rset.getString(2)==null?"":rset.getString(2));
						VPDF_COL3.add("");
						VPDF_COL4.add("INR");
						VPDF_COL5.add("");
						VPDF_COL6.add("");
						VPDF_COL7.add(rset.getString(3)==null?"":nf.format(rset.getDouble(3)));
					}
					rset.close();
					stmt.close();
				}
			}
			rset1.close();
			stmt1.close();
			
			String invAmtLbl=inv_flag.equals("UG")?"Invoice Amount - GST on SUG":"Invoice Amount";
			srno+=1;
			VPDF_COL1.add(srno);
			VPDF_COL2.add(invAmtLbl);
			VPDF_COL3.add("");
			VPDF_COL4.add("INR");
			VPDF_COL5.add("");
			VPDF_COL6.add("");
			VPDF_COL7.add(invoice_amt);
			
			if(applicable_abbr.equals("TCS"))
			{
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("TCS");
				VPDF_COL3.add("");
				VPDF_COL4.add("INR");
				VPDF_COL5.add("");
				VPDF_COL6.add(TCS_factor+"%");
				VPDF_COL7.add(applicable_amt);
			}
			
			int adv_count=0;
			queryString1="SELECT COUNT(*) "
					+ "FROM FMS_INV_ADV_DTL "
					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
					+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? ";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, bu_state_tin);
			stmt1.setString(3, financial_year);
			stmt1.setString(4, invoice_seq);
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				adv_count=rset1.getInt(1);
			}
			rset1.close();
			stmt1.close();
			
			if(adv_count > 0)
			{
				double adjted_amt=0;
				queryString2="SELECT SUM(AMOUNT), LISTAGG(SEC_INT_REF, ',') WITHIN GROUP (ORDER BY SEC_INT_REF) AS SEC_INT_REF_LIST "
						+ "FROM FMS_INV_ADV_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
						+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? AND INV_COMPONENT=? ";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, bu_state_tin);
				stmt2.setString(3, financial_year);
				stmt2.setString(4, invoice_seq);
				stmt2.setString(5, "GROSS");
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					adjted_amt+=rset2.getDouble(1);
					String receVouchMap=rset2.getString(2)==null?"":rset2.getString(2);
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("Adjustment for Advance Gross paid against Receipt Voucher No "+receVouchMap);
					VPDF_COL3.add("");
					VPDF_COL4.add("INR");
					VPDF_COL5.add("");
					VPDF_COL6.add("");
					VPDF_COL7.add(rset2.getString(1)==null?"":nf.format(rset2.getDouble(1)));
				}
				rset2.close();
				stmt2.close();
				
				queryString="SELECT TAX_DESCR "
						+ "FROM FMS_INV_TAX_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
						+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, bu_state_tin);
				stmt.setString(3, financial_year);
				stmt.setString(4, invoice_seq);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String taxDesc=rset.getString(1)==null?"":rset.getString(1);
					String taxAbbr="";
					if(!taxDesc.equals(""))
					{
						String[] split=taxDesc.split(" ");
						taxAbbr=split[0];
					}
					
					queryString2="SELECT SUM(AMOUNT), LISTAGG(SEC_INT_REF, ',') WITHIN GROUP (ORDER BY SEC_INT_REF) AS SEC_INT_REF_LIST "
							+ "FROM FMS_INV_ADV_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? AND INV_COMPONENT=? ";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, bu_state_tin);
					stmt2.setString(3, financial_year);
					stmt2.setString(4, invoice_seq);
					stmt2.setString(5, taxAbbr);
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						adjted_amt+=rset2.getDouble(1);
						String receVouchMap=rset2.getString(2)==null?"":rset2.getString(2);
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("Adjustment for Advance "+taxAbbr+" paid against Receipt Voucher No "+receVouchMap);
						VPDF_COL3.add("");
						VPDF_COL4.add("INR");
						VPDF_COL5.add("");
						VPDF_COL6.add("");
						VPDF_COL7.add(rset2.getString(1)==null?"":nf.format(rset2.getDouble(1)));
					}
					rset2.close();
					stmt2.close();
				}
				rset.close();
				stmt.close();
				
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("Net Amount Payable");
				VPDF_COL3.add("");
				VPDF_COL4.add("INR");
				VPDF_COL5.add("");
				VPDF_COL6.add("");
				VPDF_COL7.add(nf.format(Double.parseDouble(net_payable) - adjted_amt));
			}
			else
			{
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("Net Amount Payable");
				VPDF_COL3.add("");
				VPDF_COL4.add("INR");
				VPDF_COL5.add("");
				VPDF_COL6.add("");
				VPDF_COL7.add(net_payable);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getInvoiceNumber()
	{
		String function_nm="getInvoiceNumber()";
		try
		{
			String invoice_prefix=utilBean.getInvoicePrefix(conn,comp_cd);
			
			String fin_yr="";
			if(!financial_year.equals(""))
			{
				String[] temp = financial_year.split("-");
				fin_yr=temp[0].substring(2,temp[0].length())+"-"+temp[1].substring(2,temp[1].length());
			}
			
			String state_abbr="";
			String state_code=bu_state_tin;
			if(state_code.length()<=1)
			{
				state_code="0"+state_code;
			}
			
			if(!invoice_id_seq.equals(""))
			{
				VINVOICE_ID_SEQ.add(invoice_id_seq);
				VINVOICE_NO.add(invoice_no);
			}
			
			String contType="";
			String invSeries="";
			String ff_invType="";
			String invFlags="''";
			if(contract_type.equals("Q") || contract_type.equals("O"))
			{
				contType="'Q','O'";
				invSeries="L";
				invFlags="'F','UG','ST'";
				ff_invType="'SI','UG','ST','DI'";
			}
			else
			{
				contType="'S','L','X'";
				invSeries="S";
				invFlags="'F'";
				ff_invType="'S'";
			}
			
			if(!invoice_prefix.equals(""))
			{
				int no_inv_no=10;
				for(int i=1;i<=no_inv_no;i++)
				{
					String invoice_id_seq=""+i;
					int count=0;
					queryString="SELECT SUM(CNT) AS TOTAL_COUNT FROM ("
							+ "SELECT COUNT(*) AS CNT "
								+ "FROM FMS_INVOICE_MST "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
								+ "AND BU_STATE_TIN=? AND INVOICE_ID_SEQ=? AND CONTRACT_TYPE IN ("+contType+") "
								+ "AND INV_FLAG IN ("+invFlags+") "
							/*+ "UNION ALL "
							+ "SELECT COUNT(*) AS CNT "
								+ "FROM FMS_DLNG_SVC_INVOICE_MST "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
								+ "AND BU_STATE_TIN=? AND INVOICE_ID_SEQ=? AND CONTRACT_TYPE IN ("+contType+") "
								+ "AND INV_FLAG IN ("+invFlags+") "*/
							+ "UNION ALL "
							+ "SELECT COUNT(*) AS CNT "
								+ "FROM FMS_FFLOW_INV_MST "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND BU_STATE_TIN=? "
								+ "AND INVOICE_TYPE IN ("+ff_invType+") "
								+ "AND INVOICE_ID_SEQ=? "
							+ "UNION ALL "
							+ "SELECT COUNT(*) AS CNT "
								+ "FROM FMS_DLNG_FFLOW_INV_MST "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? AND BU_STATE_TIN=? "
								+ "AND INVOICE_TYPE IN ('TLU') "
								+ "AND INVOICE_ID_SEQ=? "
							+ ") A";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, financial_year);
					stmt.setString(3, bu_state_tin);
					stmt.setString(4, invoice_id_seq);
					stmt.setString(5, comp_cd);
					stmt.setString(6, financial_year);
					stmt.setString(7, bu_state_tin);
					stmt.setString(8, invoice_id_seq);
					stmt.setString(9, comp_cd);
					stmt.setString(10, financial_year);
					stmt.setString(11, bu_state_tin);
					stmt.setString(12, invoice_id_seq);
					/*stmt.setString(13, comp_cd);
					stmt.setString(14, financial_year);
					stmt.setString(15, bu_state_tin);
					stmt.setString(16, invoice_id_seq);*/
					rset=stmt.executeQuery();
					if(rset.next())
					{
						count += rset.getInt(1);
					}
					rset.close();
					stmt.close();
					
					/*if(invSeries.equals("S") || invSeries.equals("L"))
					{
						queryString1="SELECT COUNT(*) "
								+ "FROM FMS_FFLOW_INV_MST "
								+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
								+ "AND FINANCIAL_YEAR=? "
								+ "AND INVOICE_TYPE IN ("+ff_invType+") "
								+ "AND INVOICE_ID_SEQ=? ";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, bu_state_tin);
						stmt1.setString(3, financial_year);
						stmt1.setString(4, invoice_id_seq);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							count += rset1.getInt(1);
						}
						rset1.close();
						stmt1.close();
						
						if(invSeries.equals("L"))
						{
							queryString1="SELECT COUNT(*) "
									+ "FROM FMS_DLNG_FFLOW_INV_MST "
									+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
									+ "AND FINANCIAL_YEAR=? "
									+ "AND INVOICE_TYPE IN ('TLU') "
									+ "AND INVOICE_ID_SEQ=? ";
							stmt1=conn.prepareStatement(queryString1);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, bu_state_tin);
							stmt1.setString(3, financial_year);
							stmt1.setString(4, invoice_id_seq);
							rset1=stmt1.executeQuery();
							if(rset1.next())
							{
								count += rset1.getInt(1);
							}
							rset1.close();
							stmt1.close();
						}
					}*/
					
					if(count==0)
					{	
						queryString2="SELECT STATE_CODE "
								+ "FROM FMS_STATE_MST "
								+ "WHERE TIN=?";
						stmt2=conn.prepareStatement(queryString2);
						stmt2.setString(1, state_code);
						rset2=stmt2.executeQuery();
						if(rset2.next())
						{
							state_abbr=rset2.getString(1)==null?"":rset2.getString(1);
						}
						rset2.close();
						stmt2.close();
						String invoice_no="";
						invoice_no=invoice_prefix+""+invSeries+""+state_abbr+""+utilBean.PrePaddingZero(invoice_id_seq, 4)+"/"+fin_yr;
						
						VINVOICE_ID_SEQ.add(invoice_id_seq);
						VINVOICE_NO.add(invoice_no);
					}
					else
					{
						no_inv_no+=1;
					}
				}
			}
			
			if(invoice_id_seq.equals(""))
			{
				if(VINVOICE_ID_SEQ.size()>0)
				{
					invoice_id_seq=""+VINVOICE_ID_SEQ.elementAt(0);
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getInvoiceDetailForViewBeforeSub()
	{
		String function_nm="getInvoiceDetailForViewBeforeSub()";
		try
		{
			couterpty_nm=""+utilBean.getCounterpartyName(conn,counterparty_cd);
			price_cd_nm=""+utilBean.getRateUnitNm(conn,price_cd);
			invoice_raised_in_nm=""+utilBean.getRateUnitNm(conn,invoice_raised_in);
			
			String ship_nm="";
			String boe_number="";
			String boe_date="";
			
			if(contract_type.equals("O") || contract_type.equals("Q"))
			{
				queryString="SELECT A.CONT_REF_NO,TO_CHAR(A.DDA_DT,'DD-MON-YY'),A.CONT_REF_NO,B.SHIP_CD,B.BOE_NO,TO_CHAR(B.BOE_DT,'DD-MON-YY'),TO_CHAR(SIGNING_DT,'DD-MON-YY'),TO_CHAR(B.ACTUAL_RECPT_DT,'DD-MON-YY') "
						+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_CARGO_DTL B "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? "
						+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.AGMT_TYPE=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND B.CARGO_NO=? "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
						+ ""
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, "C");
				stmt.setString(4, agmt_no);
				stmt.setString(5, agmt_rev_no);
				stmt.setString(6, "A");
				stmt.setString(7, cont_no);
				stmt.setString(8, contract_type);
				stmt.setString(9, cargo_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					contRef=rset.getString(1)==null?"":rset.getString(1);
					dda_dt=rset.getString(2)==null?"":rset.getString(2);
					//cargoRef=rset.getString(3)==null?"":rset.getString(3);
					
					String ship_cd=rset.getString(4)==null?"":rset.getString(4);
					ship_nm=utilBean.getShipName(conn,ship_cd);
					boe_number = rset.getString(5)==null?"":rset.getString(5);
					boe_date=rset.getString(6)==null?"":rset.getString(6);
					
					signingDt=rset.getString(7)==null?"":rset.getString(7);
					arrivalDt=rset.getString(8)==null?"":rset.getString(8);
				}
				rset.close();
				stmt.close();
				
				queryString1="SELECT TO_CHAR(SIGNING_DT,'DD-MON-YY')  "
						+ "FROM FMS_LTCORA_AGMT_MST A "
						+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
						+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_LTCORA_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.BUY_SALE=B.BUY_SALE) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, "A");
				stmt1.setString(3, agmt_no);
				stmt1.setString(4, counterparty_cd);
				stmt1.setString(5, "C");
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					agmtSigningDt=rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
			}
			else
			{
				queryString="SELECT CONT_REF_NO,TO_CHAR(DDA_DT,'DD-MON-YY'),TRADE_REF_NO,TO_CHAR(SIGNING_DT,'DD-MON-YY')  "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND CONT_NO=? AND AGMT_NO=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
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
					dda_dt=rset.getString(2)==null?"":rset.getString(2);
					String tradeRef=rset.getString(3)==null?"":rset.getString(3);
					signingDt=rset.getString(4)==null?"":rset.getString(4);
					
					if(contract_type.equals("X"))
					{
						contRef=tradeRef;
					}
				}
				rset.close();
				stmt.close();
				
				if(contract_type.equals("S"))
				{
					queryString1="SELECT TO_CHAR(SIGNING_DT,'DD-MON-YY')  "
							+ "FROM FMS_AGMT_MST A "
							+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
							+ "AND AGMT_NO=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, "F");
					stmt1.setString(3, agmt_no);
					stmt1.setString(4, counterparty_cd);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						agmtSigningDt=rset1.getString(1)==null?"":rset1.getString(1);
					}
					rset1.close();
					stmt1.close();
				}
			}
			
			queryString1="SELECT CONTACT_PERSON "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND SEQ_NO=? AND ADDR_FLAG=? AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.TYPE=B.TYPE "
					+ "AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY'))";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_cd);
			stmt1.setString(3, "C");
			stmt1.setString(4, contact_person_cd);
			stmt1.setString(5, "P"+plant_seq);
			stmt1.setString(6, "RLNG");
			stmt1.setString(7, invoice_dt);
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
					+ "AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY'))";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, comp_cd);
			stmt2.setString(3, "B");
			stmt2.setString(4, bu_contact_person_cd);
			stmt2.setString(5, "P"+bu_unit);
			stmt2.setString(6, "RLNG");
			stmt2.setString(7, invoice_dt);
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
			
			HashMap plant_detail=utilBean.getCounterpartyPlantDetail(conn,comp_cd, "C", counterparty_cd, plant_seq);
            plantAddress=""+plant_detail.get("plant_address");
			plantCity=""+plant_detail.get("plant_city");
			plantState=""+plant_detail.get("plant_state");
			plantPin=""+plant_detail.get("plant_pin");
			plantNm=""+plant_detail.get("plant_name");
			
			if(contract_type.equals("O") || contract_type.equals("Q"))
			{
				tax_info="State : "+plantState;
				tax_info+="<br>State Code : "+utilBean.getState_TIN(conn, comp_cd, counterparty_cd, "C", plant_seq);
				//String temp_tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "C", counterparty_cd, plant_seq);
				//tax_info+=temp_tax_info.replaceAll("\n", "<br>");
				
				queryString = "SELECT A.STAT_NO, TO_CHAR(A.EFF_DT,'DD/MM/YYYY'), B.STAT_NM "
						+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B "
						+ "WHERE A.COUNTERPARTY_CD=? AND A.ENTITY=? "
						+ "AND A.PLANT_SEQ_NO=? AND A.COMPANY_CD=? "
						+ "AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL AND B.STAT_CD IN ('1003','1001')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, counterparty_cd);
				stmt.setString(2, "C");
				stmt.setString(3, plant_seq);
				stmt.setString(4, comp_cd);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					String no = rset.getString(1)==null?"":rset.getString(1);
					String nm = rset.getString(3)==null?"":rset.getString(3);
	
					tax_info+="<br>"+nm+" : "+no;
				}
				stmt.close();
				rset.close();
				
				bu_tax_info="State : "+bu_plantState;
				bu_tax_info+="<br>State Code : "+utilBean.getState_TIN(conn, comp_cd, comp_cd, "B", bu_unit);
				//String temp_bu_tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "B", comp_cd, bu_unit);
				//bu_tax_info+=temp_bu_tax_info.replaceAll("\n", "<br>");
				
				queryString = "SELECT A.STAT_NO, TO_CHAR(A.EFF_DT,'DD/MM/YYYY'), B.STAT_NM "
						+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B "
						+ "WHERE A.COUNTERPARTY_CD=? AND A.ENTITY=? "
						+ "AND A.PLANT_SEQ_NO=? AND A.COMPANY_CD=? "
						+ "AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL AND B.STAT_CD IN ('1003','1001')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, "B");
				stmt.setString(3, bu_unit);
				stmt.setString(4, comp_cd);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					String no = rset.getString(1)==null?"":rset.getString(1);
					String nm = rset.getString(3)==null?"":rset.getString(3);
	
					bu_tax_info+="<br>"+nm+" : "+no;
				}
				stmt.close();
				rset.close();
				
				bu_tax_info+="<br>SAC : 999799"
						+ "<br>Description of Service : Other Miscellaneous services - Other Services n.e.c."
						+ "<br>Place Of Supply : "+plantState; //WILL BE CUSTOMER PLANT STATE AS DISCUSSED BY JAYASRI MAM WITH VIJAY ON 20250725
			}
			else
			{
				tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "C", counterparty_cd, plant_seq);
				tax_info=tax_info.replaceAll("\n", "<br>");
				
				bu_tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "B", comp_cd, bu_unit);
				bu_tax_info=bu_tax_info.replaceAll("\n", "<br>");
			}	
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getAllPdfFileName()
	{
		String function_nm="getAllPdfFileName()";
		try
		{
			if(flag.equals("FF"))
			{
				queryString3="SELECT FILE_NAME,PDF_SIGNED "
						+ "FROM FMS_FFLOW_INV_FILE_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
	 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
	 	        		+ "AND INVOICE_TYPE=? AND PDF_TYPE IN (?,?,?) ";
				stmt3=conn.prepareStatement(queryString3);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, bu_state_tin);
				stmt3.setString(3, invoice_seq);
				stmt3.setString(4, financial_year);
				stmt3.setString(5, invoice_type);
				stmt3.setString(6, "O");
				stmt3.setString(7, "D");
				stmt3.setString(8, "T");
				rset3=stmt3.executeQuery();
				while(rset3.next())
				{
					VPDF_FILE_NAME.add(rset3.getString(1)==null?"":rset3.getString(1));
					String pdf_signed=rset3.getString(2)==null?"":rset3.getString(2);
					
					if(pdf_signed.equals("Y"))
					{
						VPDF_FILE_PATH.add(CommonVariable.signed_freeflow_inv_path);
					}
					else
					{
						VPDF_FILE_PATH.add(CommonVariable.freeflow_inv_path);
					}
				}
				rset3.close();
				stmt3.close();
			}
			else
			{
				queryString3="SELECT FILE_NAME,PDF_SIGNED "
						+ "FROM FMS_INV_FILE_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
	 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
	 	        		+ "AND PDF_TYPE IN (?,?,?) ";
				stmt3=conn.prepareStatement(queryString3);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, bu_state_tin);
				stmt3.setString(3, invoice_seq);
				stmt3.setString(4, financial_year);
				stmt3.setString(5, "O");
				stmt3.setString(6, "D");
				stmt3.setString(7, "T");
				rset3=stmt3.executeQuery();
				while(rset3.next())
				{
					VPDF_FILE_NAME.add(rset3.getString(1)==null?"":rset3.getString(1));
					String pdf_signed=rset3.getString(2)==null?"":rset3.getString(2);
					
					if(pdf_signed.equals("Y"))
					{
						VPDF_FILE_PATH.add(CommonVariable.signed_sales_inv_path);
					}
					else
					{
						VPDF_FILE_PATH.add(CommonVariable.sales_inv_path);
					}
				}
				rset3.close();
				stmt3.close();
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
			//utilBean.getEffectiveCustomerCounterpartyList(comp_cd);
			utilBean.getAllEntityCounterpartyList(conn,comp_cd,"C");
			VCOUNTERPARTY_CD= utilBean.getCOUNTERPARTY_CD();
			VCOUNTERPARTY_NM = utilBean.getCOUNTERPARTY_NM();
			VCOUNTERPARTY_ABBR = utilBean.getCOUNTERPARTY_ABBR();
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
			VFILTER_CONT_TYPE.add("S");
			VFILTER_CONT_NAME.add(utilBean.getContractTypeName("S"));
			
			VFILTER_CONT_TYPE.add("L");
			VFILTER_CONT_NAME.add(utilBean.getContractTypeName("L"));
			
			VFILTER_CONT_TYPE.add("X");
			VFILTER_CONT_NAME.add(utilBean.getContractTypeName("X"));
			
			VFILTER_CONT_TYPE.add("O");
			VFILTER_CONT_NAME.add(utilBean.getContractTypeName("O"));
			
			VFILTER_CONT_TYPE.add("Q");
			VFILTER_CONT_NAME.add(utilBean.getContractTypeName("Q"));
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
			queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_REF_NO,CONTRACT_TYPE,"
					+ "TRADE_REF_NO,NULL "
					+ "FROM FMS_SUPPLY_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE IN ('S','L','X') "
					+ "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND COUNTERPARTY_CD=? "
					+ "AND CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			if(!filter_cont_type.equals("")) {
				queryString+="AND CONTRACT_TYPE=? ";
			}
			queryString+= " UNION ";
			queryString+="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
					+ "TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),TO_CHAR(TO_DATE(B.ACTUAL_RECPT_DT,'DD/MM/YY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0),'DD/MM/YYYY'),"
					+ "A.CONT_NAME,B.CARGO_REF,A.CONTRACT_TYPE,NULL,B.CARGO_NO "
					+ "FROM FMS_LTCORA_CONT_MST A,"
						+ "FMS_LTCORA_CONT_CARGO_DTL B "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? AND B.CARGO_STATUS=? AND A.AGMT_TYPE=? "
					+ "AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0) >=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE ";
			if(!filter_cont_type.equals("")) {
				queryString+="AND A.CONTRACT_TYPE=? ";
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
			stmt.setString(++cnt, comp_cd);
			stmt.setString(++cnt, counterparty_cd);
			stmt.setString(++cnt, "C");
			stmt.setString(++cnt, "Y");
			stmt.setString(++cnt, "A");
			stmt.setString(++cnt, period_end_dt);
			stmt.setString(++cnt, period_start_dt);
			if(!filter_cont_type.equals("")) {
				stmt.setString(++cnt, filter_cont_type);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String agmtno=rset.getString(3)==null?"0":rset.getString(3);
				String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
				String contno=rset.getString(5)==null?"0":rset.getString(5);
				String contrev=rset.getString(6)==null?"0":rset.getString(6);
				String cont_st_dt=rset.getString(7)==null?"":rset.getString(7);
				String cont_end_dt=rset.getString(8)==null?"":rset.getString(8);
				String cont_ref=rset.getString(10)==null?"":rset.getString(10);
				String cont_type=rset.getString(11)==null?"":rset.getString(11);
				String trade_ref=rset.getString(12)==null?"":rset.getString(12);
				String cargono=rset.getString(13)==null?"":rset.getString(13);
				
				if(cont_type.equals("X"))
				{
					cont_ref=trade_ref;
				}
				
				String deal_no = utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, cargono);
				if(!cont_ref.equals(""))
				{
					deal_no+=" ["+cont_ref+"]";
				}
				deal_no+=" ["+cont_st_dt+"-"+cont_end_dt+"]";

				String mapping_id=cont_type+"-"+agmtno+"-"+agmtrev+"-"+contno+"-"+contrev;
				if(cont_type.equals("O") || cont_type.equals("Q")) {
					mapping_id+="-"+cargono;
				}

				VDEAL_NO.add(deal_no);
				VMAPPING_ID.add(mapping_id);
			}
			rset.close();
			stmt.close();
			
			if(!contract_type.equals(""))
			{
				queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
						+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_NAME,CONT_REF_NO,CONTRACT_TYPE,"
						+ "TRADE_REF_NO,NULL "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND CONT_NO=? AND AGMT_NO=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				queryString+= " UNION ";
				queryString+="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
						+ "TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),TO_CHAR(TO_DATE(B.ACTUAL_RECPT_DT,'DD/MM/YY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0),'DD/MM/YYYY'),"
						+ "A.CONT_NAME,B.CARGO_REF,A.CONTRACT_TYPE,NULL,B.CARGO_NO "
						+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_CARGO_DTL B "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? AND B.CARGO_STATUS=? AND A.AGMT_TYPE=? "
						+ "AND A.AGMT_NO=? AND A.CONT_NO=? AND B.CARGO_NO=? AND A.CONTRACT_TYPE=? "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, contract_type);
				stmt.setString(3, cont_no);
				stmt.setString(4, agmt_no);
				stmt.setString(5, counterparty_cd);
				stmt.setString(6, comp_cd);
				stmt.setString(7, counterparty_cd);
				stmt.setString(8, "C");
				stmt.setString(9, "Y");
				stmt.setString(10, "A");
				stmt.setString(11, agmt_no);
				stmt.setString(12, cont_no);
				stmt.setString(13, cargo_no);
				stmt.setString(14, contract_type);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String agmtno=rset.getString(3)==null?"0":rset.getString(3);
					String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
					String contno=rset.getString(5)==null?"0":rset.getString(5);
					String contrev=rset.getString(6)==null?"0":rset.getString(6);
					String cont_st_dt=rset.getString(7)==null?"":rset.getString(7);
					String cont_end_dt=rset.getString(8)==null?"":rset.getString(8);
					String cont_ref=rset.getString(10)==null?"":rset.getString(10);
					String cont_type=rset.getString(11)==null?"":rset.getString(11);
					String trade_ref=rset.getString(12)==null?"":rset.getString(12);
					String cargono=rset.getString(13)==null?"":rset.getString(13);
					
					if(cont_type.equals("X"))
					{
						cont_ref=trade_ref;
					}
					
					contRef=cont_ref;
					
					String deal_no = utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, cargono);
					if(!cont_ref.equals(""))
					{
						deal_no+=" ["+cont_ref+"]";
					}
					deal_no+=" ["+cont_st_dt+"-"+cont_end_dt+"]";

					String mapping_id=cont_type+"-"+agmtno+"-"+agmtrev+"-"+contno+"-"+contrev;
					if(cont_type.equals("O") || cont_type.equals("Q")) {
						mapping_id+="-"+cargono;
					}

					if(!VMAPPING_ID.contains(mapping_id))
					{
						VDEAL_NO.add(deal_no);
						VMAPPING_ID.add(mapping_id);
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
	
	public void getPlantDetail()
	{
		String function_nm="getPlantDetail()";
		try
		{
			utilBean.getEffectiveCounterpartyPlantList(conn,counterparty_cd, "C", comp_cd);
			VPLANT_SEQ = utilBean.getPLANT_SEQ_NO();
			VPLANT_ABBR = utilBean.getPLANT_ABBR();
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
			queryString="SELECT DISTINCT ADDRESS_TYPE "
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
			stmt.close();

			for(int i=0; i<VPLANT_SEQ.size(); i++)
			{
				VADDRESS_TYPE.add("P"+VPLANT_SEQ.elementAt(i));
				VADDRESS_NAME.add(VPLANT_ABBR.elementAt(i));
			}
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
			queryString="SELECT CONTACT_PERSON, SEQ_NO "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND ADDR_FLAG=? AND INV_FLAG=? "
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.TYPE=B.TYPE "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, "C");
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
	
	public void getBuUnit()
	{
		String function_nm="getBuUnit()";
		try
		{
			queryString="SELECT COMPANY_CD,PLANT_SEQ_NO "
					+ "FROM FMS_SUPPLY_CONT_BU "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONTRACT_TYPE=? ";
			queryString+="UNION ";
			queryString+="SELECT COMPANY_CD,PLANT_SEQ_NO "
					+ "FROM FMS_LTCORA_CONT_BU "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND BUY_SALE=? AND AGMT_TYPE=? AND CONT_NO=? "
					+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONTRACT_TYPE=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, cont_rev_no);
			stmt.setString(5, agmt_no);
			stmt.setString(6, agmt_rev_no);
			stmt.setString(7, contract_type);
			stmt.setString(8, comp_cd);
			stmt.setString(9, counterparty_cd);
			stmt.setString(10, "C");
			stmt.setString(11, "A");
			stmt.setString(12, cont_no);
			stmt.setString(13, cont_rev_no);
			stmt.setString(14, agmt_no);
			stmt.setString(15, agmt_rev_no);
			stmt.setString(16, contract_type);
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
	
	public void getF_FlowInvoice()
	{
		String function_nm="getF_FlowInvoice()";
		try
		{
			/*queryString="SELECT SEQ_NO "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND ADDR_FLAG=? AND INV_FLAG=? "
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND TO_INV=? AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.TYPE=B.TYPE "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, "C");
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
			stmt.close();*/
			
			String temp_contact_person_cd=utilBean.getEntityContactPersonCd(conn,comp_cd,counterparty_cd,"C",address_type, "INV", "RLNG","Y");
			contact_person_cd=temp_contact_person_cd.equals("")?"0":temp_contact_person_cd;

			/*queryString1="SELECT SEQ_NO "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND ADDR_FLAG=? AND INV_FLAG=? "
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND TO_INV=? AND TYPE=? "
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
			
			String temp_bu_contact_person_cd=utilBean.getEntityContactPersonCd(conn,comp_cd,comp_cd,"B","P"+bu_unit, "INV", "RLNG","Y");
			bu_contact_person_cd=temp_bu_contact_person_cd.equals("")?"0":temp_bu_contact_person_cd;
			
			//financial_year=utilDate.getFinancialYear(period_end_dt);
			
			String state_code=utilBean.getState_TIN(conn, comp_cd, comp_cd, "B", bu_unit);
			bu_state_tin=state_code;
			
			/*queryString2="SELECT NVL(MAX(INVOICE_SEQ),0) "
					+ "FROM FMS_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? "
					+ "AND FINANCIAL_YEAR=? "
					+ "AND INVOICE_TYPE=? AND BU_STATE_TIN=?";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, financial_year);
			stmt2.setString(3, invoice_type);
			stmt2.setString(4, state_code);
			rset2=stmt2.executeQuery();
			if(rset2.next())
			{
				invoice_seq=""+(rset2.getInt(1)+1);
			}
			rset2.close();
			stmt2.close();*/
			
			String bank_formula=utilBean.getEntityBankFormula(conn, comp_cd, counterparty_cd, "C", "RLNG", utilDate.getSysdate());
			/*queryString2="SELECT COUNTERPARTY_CD,ENTITY,TO_CHAR(EFF_DT,'DD/MM/YYYY'),BANK_NAME,ACCOUNT_ID,IFSC_CODE,BRANCH_NAME,"
					+ "STATE_NM  "
					+ "FROM FMS_ENTITY_BANK_MST A "
					+ "WHERE ENTITY=? AND COUNTERPARTY_CD=? AND COMPANY_CD=? AND CATEGORY=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_BANK_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.CATEGORY=B.CATEGORY)";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, "B");
			stmt2.setString(2, comp_cd);
			stmt2.setString(3, comp_cd);
			stmt2.setString(4, "RLNG");
			rset2=stmt2.executeQuery();
			if(rset2.next())
			{
				String bank_eff_dt = rset2.getString(3)==null?"":rset2.getString(3);
				String bank_name=rset2.getString(4)==null?"":rset2.getString(4);
				String bank_account_no=rset2.getString(5)==null?"":rset2.getString(5);
				String ifsc_code=rset2.getString(6)==null?"":rset2.getString(6);
				String bank_branch=rset2.getString(7)==null?"":rset2.getString(7);
				String bank_state=rset2.getString(8)==null?"":rset2.getString(8);
				
				bank_formula=bank_name+", "+bank_branch+", "+bank_state+", A/C No : "+bank_account_no+", IFSC Code : "+ifsc_code;
			}
			rset2.close();
			stmt2.close();*/
			
			note ="Please pay the invoiced amount by wire transfer at our Bank Account : "+bank_formula;

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
	
	public void getExixtingF_FLowInvoice()
	{
		String function_nm="getExixtingF_FLowInvoice()";
		try
		{
			queryString="SELECT INVOICE_SEQ, INVOICE_NO, INVOICE_NO, TO_CHAR(INVOICE_DT,'DD/MM/YYYY'), TO_CHAR(DUE_DT,'DD/MM/YYYY'),"
					+ "INVOICE_CATEGORY, INVOICE_TYPE, "
					+ "FINANCIAL_YEAR, NUM_LINE, LINKED_INVOICE, NOTE, "
					+ "CONTACT_PERSON_CD,BU_CONTACT_PERSON_CD, "
					+ "CHECKED_FLAG, CHECKED_BY, TO_CHAR(CHECKED_DT,'DD/MM/YYYY HH24:MI:SS'), "
					+ "APPROVED_FLAG, APPROVED_BY, TO_CHAR(APPROVED_DT,'DD/MM/YYYY HH24:MI:SS'),"
					+ "OTHER_INV_STR,GROSS_AMT_USD,EXCHG_RATE_VALUE,GROSS_AMT_INR,TAX_AMT,INVOICE_AMT,ADJUST_AMT,NET_PAYABLE_AMT,"
					+ "INVOICE_RAISED_IN,AMT_WORD,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),"
					+ "TDS_GROSS_AMT,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),"
					+ "TCS_AMT,TCS_STRUCT_CD,TO_CHAR(TCS_EFF_DT,'DD/MM/YYYY'),"
					+ "TCS_TDS,ALLOC_QTY,SUB_INV_TYPE "
					+ "FROM FMS_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND BU_STATE_TIN=? "
					+ "AND FINANCIAL_YEAR=? AND INVOICE_TYPE=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_seq);
			stmt.setString(3, bu_state_tin);
			stmt.setString(4, financial);
			stmt.setString(5, invoice_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				submission_chk=true;

				invoice_seq=rset.getString(1)==null?"":rset.getString(1);
				invoice_no=rset.getString(2)==null?"":rset.getString(2);
				//invoice_ref=rset.getString(3)==null?"":rset.getString(3);
				invoice_dt=rset.getString(4)==null?"":rset.getString(4);
				invoice_due_dt=rset.getString(5)==null?"":rset.getString(5);
				invoice_category=rset.getString(6)==null?"":rset.getString(6);
				invoice_type=rset.getString(7)==null?"":rset.getString(7);
				financial_year=rset.getString(8)==null?"":rset.getString(8);
				num_line=rset.getString(9)==null?"":rset.getString(9);
				linked_invoice=rset.getString(10)==null?"":rset.getString(10);
				note=rset.getString(11)==null?"":rset.getString(11);
				contact_person_cd=rset.getString(12)==null?"0":rset.getString(12);
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
				invoice_aprv_flag=rset.getString(17)==null?"":rset.getString(17);
				String aprv_by=rset.getString(18)==null?"":rset.getString(18);
				invoice_aprv_by=utilBean.getEmpName(conn,aprv_by);
				invoice_aprv_dt=rset.getString(19)==null?"":rset.getString(19);
				if(invoice_aprv_flag.equals("A"))
				{
					invoice_aprv_nm="Approved";
				}
				else if(invoice_aprv_flag.equals("R"))
				{
					invoice_aprv_nm="Rejected";
				}

				other_inv_str =rset.getString(20)==null?"":rset.getString(20);

				gross_amt =rset.getString(21)==null?"":rset.getString(21);
				exchang_rate =rset.getString(22)==null?"":rset.getString(22);
				gross_amt1 =rset.getString(23)==null?"":rset.getString(23);
				tax_amt =rset.getString(24)==null?"":rset.getString(24);
				invoice_amt =rset.getString(25)==null?"":rset.getString(25);
				invoice_adj_amt =rset.getString(26)==null?"":rset.getString(26);
				net_payable =rset.getString(27)==null?"":rset.getString(27);
				invoice_raised_in =rset.getString(28)==null?"":rset.getString(28);
				amount_in_word =rset.getString(29)==null?"":rset.getString(29);
				tax_struct_cd =rset.getString(30)==null?"":rset.getString(30);
				tax_struct_dt =rset.getString(31)==null?"":rset.getString(31);
				
				tds_amt =rset.getString(32)==null?"":nf.format(rset.getDouble(32));
				tds_struct_cd =rset.getString(33)==null?"":rset.getString(33);
				tds_struct_dt =rset.getString(34)==null?"":rset.getString(34);
				
				applicable_amt =rset.getString(35)==null?"":nf.format(rset.getDouble(35));
				tcs_struct_cd =rset.getString(36)==null?"":rset.getString(36);
				tcs_struct_dt =rset.getString(37)==null?"":rset.getString(37);
				
				applicable_abbr =rset.getString(38)==null?"":rset.getString(38);
				qty_mmbtu =rset.getString(39)==null?"":nf.format(rset.getDouble(39));
				sub_inv_type =rset.getString(40)==null?"":rset.getString(40);
				
				tax_struct_info=utilBean.getTaxDescr(conn,tax_struct_cd);
				tcs_struct_info=utilBean.getTaxDescr(conn,tcs_struct_cd);
				tds_struct_info=utilBean.getTaxDescr(conn,tds_struct_cd);
				
				Vector VTAX_CODE = new Vector();
				Vector VTAX_DESCR = new Vector();
				Vector VTAX_AMT = new Vector();
				Vector VTAX_BASE_AMT = new Vector();
				queryString4="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
						+ "FROM FMS_FFLOW_INV_TAX_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
						+ "AND INVOICE_TYPE=? "
						+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? ";
				stmt4=conn.prepareStatement(queryString4);
				stmt4.setString(1, comp_cd);
				stmt4.setString(2, bu_state_tin);
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
					+ "FROM FMS_FFLOW_INV_DTL "
					+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND BU_STATE_TIN=? "
					+ "AND FINANCIAL_YEAR=? AND INVOICE_TYPE=?";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, inv_seq);
			stmt1.setString(3, bu_state_tin);
			stmt1.setString(4, financial);
			stmt1.setString(5, invoice_type);
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
	
	public void getF_FlowAnnexureDetail()
	{
		String function_nm="getF_FlowAnnexureDetail()";
		try
		{
			queryString="SELECT ANNEXURE_SEQ,FILE_NAME "
					+ "FROM FMS_FFLOW_INV_ANNEXURE_DTL "
					+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND BU_STATE_TIN=? "
					+ "AND FINANCIAL_YEAR=? AND INVOICE_TYPE=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_seq);
			stmt.setString(3, bu_state_tin);
			stmt.setString(4, financial);
			stmt.setString(5, invoice_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VANNEXURE_SEQ.add(rset.getString(1)==null?"":rset.getString(1));
				VANNEXURE_FILE_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VANNEXURE_FOLDER.add(invoice_type+"_"+financial+"_"+bu_state_tin+"_"+inv_seq);
			}
			rset.close();
			stmt.close();
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
			queryString="SELECT INVOICE_NO "
					+ "FROM FMS_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "AND BU_UNIT=? "
					//+ "AND TO_DATE(TO_CHAR(PERIOD_START_DT,'MM/YYYY'),'MM/YYYY')=TO_DATE(?,'MM/YYYY') "
					//+ "AND TO_DATE(TO_CHAR(PERIOD_END_DT,'MM/YYYY'),'MM/YYYY')=TO_DATE(?,'MM/YYYY') "
					+ "AND APPROVED_FLAG=? AND PDF_INV_DTL IS NOT NULL";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, cont_no);
			stmt.setString(5, contract_type);
			stmt.setString(6, bu_unit);
			//stmt.setString(7, month+"/"+year);
			//stmt.setString(8, month+"/"+year);
			stmt.setString(7, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VLINK_INVOICE_NO.add(rset.getString(1)==null?"":rset.getString(1));
			}
			rset.close();
			stmt.close();

			queryString="SELECT INVOICE_NO "
					+ "FROM FMS_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "AND BU_UNIT=? "
					//+ "AND TO_DATE(TO_CHAR(PERIOD_START_DT,'MM/YYYY'),'MM/YYYY')=TO_DATE('"+month+"/"+year+"','MM/YYYY') "
					//+ "AND TO_DATE(TO_CHAR(PERIOD_END_DT,'MM/YYYY'),'MM/YYYY')=TO_DATE('"+month+"/"+year+"','MM/YYYY') "
					+ "AND APPROVED_FLAG=? AND PDF_INV_DTL IS NOT NULL";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, cont_no);
			stmt.setString(5, contract_type);
			stmt.setString(6, bu_unit);
			//stmt.setString(7, month+"/"+year);
			//stmt.setString(8, month+"/"+year);
			stmt.setString(7, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VLINK_INVOICE_NO.add(rset.getString(1)==null?"":rset.getString(1));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getF_FlowInvoiceDetail()
	{
		String function_nm="getF_FlowInvoiceDetail()";
		try
		{
			queryString="SELECT INVOICE_SEQ, INVOICE_NO, INVOICE_NO, TO_CHAR(INVOICE_DT,'DD-MON-YY'), TO_CHAR(DUE_DT,'DD-MON-YY'),"
					+ "INVOICE_CATEGORY, INVOICE_TYPE, "
					+ "FINANCIAL_YEAR, NUM_LINE, LINKED_INVOICE, NOTE, "
					+ "CONTACT_PERSON_CD,BU_CONTACT_PERSON_CD, "
					+ "CHECKED_FLAG, CHECKED_BY, TO_CHAR(CHECKED_DT,'DD/MM/YYYY HH24:MI:SS'), "
					+ "APPROVED_FLAG, APPROVED_BY, TO_CHAR(APPROVED_DT,'DD/MM/YYYY HH24:MI:SS'),"
					+ "OTHER_INV_STR,GROSS_AMT_USD,EXCHG_RATE_VALUE,GROSS_AMT_INR,TAX_AMT,INVOICE_AMT,ADJUST_AMT,NET_PAYABLE_AMT,"
					+ "INVOICE_RAISED_IN,AMT_WORD,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(PERIOD_START_DT,'DD-MON-YY'),TO_CHAR(PERIOD_END_DT,'DD-MON-YY'),INVOICE_ID_SEQ,"
					+ "TCS_TDS,TCS_AMT,TCS_STRUCT_CD,TO_CHAR(TCS_EFF_DT,'DD/MM/YYYY') "
					+ "FROM FMS_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND BU_STATE_TIN=? "
					+ "AND FINANCIAL_YEAR=? AND INVOICE_TYPE=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, invoice_seq);
			stmt.setString(3, bu_state_tin);
			stmt.setString(4, financial_year);
			stmt.setString(5, invoice_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				invoice_no=rset.getString(2)==null?"":rset.getString(2);
				//invoice_ref=rset.getString(3)==null?"":rset.getString(3);
				invoice_dt=rset.getString(4)==null?"":rset.getString(4);
				invoice_due_dt=rset.getString(5)==null?"":rset.getString(5);
				invoice_category=rset.getString(6)==null?"":rset.getString(6);
				invoice_type=rset.getString(7)==null?"":rset.getString(7);
				num_line=rset.getString(9)==null?"":rset.getString(9);
				linked_invoice=rset.getString(10)==null?"":rset.getString(10);
				note=rset.getString(11)==null?"":rset.getString(11);
				contact_person_cd=rset.getString(12)==null?"0":rset.getString(12);
				bu_contact_person_cd=rset.getString(13)==null?"0":rset.getString(13);

				other_inv_str =rset.getString(20)==null?"":rset.getString(20);

				gross_amt =rset.getString(21)==null?"":rset.getString(21);
				exchang_rate =rset.getString(22)==null?"":rset.getString(22);
				gross_amt1 =rset.getString(23)==null?"":rset.getString(23);
				tax_amt =rset.getString(24)==null?"":rset.getString(24);
				invoice_amt =rset.getString(25)==null?"":rset.getString(25);
				invoice_adj_amt =rset.getString(26)==null?"":rset.getString(26);
				net_payable =rset.getString(27)==null?"":rset.getString(27);
				invoice_raised_in =rset.getString(28)==null?"":rset.getString(28);
				amount_in_word =rset.getString(29)==null?"":rset.getString(29);
				tax_struct_cd =rset.getString(30)==null?"":rset.getString(30);
				tax_struct_dt =rset.getString(31)==null?"":rset.getString(31);
				
				inv_period_start_dt=rset.getString(32)==null?"":rset.getString(32);
				inv_period_end_dt=rset.getString(33)==null?"":rset.getString(33);
				invoice_id_seq=rset.getString(34)==null?"":rset.getString(34);
				
				applicable_abbr=rset.getString(35)==null?"":rset.getString(35);
				applicable_amt=rset.getString(36)==null?"":nf.format(rset.getDouble(36));
				tcs_struct_cd=rset.getString(37)==null?"":rset.getString(37);
				tcs_struct_dt=rset.getString(38)==null?"":rset.getString(38);
				
				if(activityFlag.equals("CHECK")) {
					activity_value=rset.getString(14)==null?"":rset.getString(14);
				}else if(activityFlag.equals("APPROVE")) {
					activity_value=rset.getString(17)==null?"":rset.getString(17);
				}
				
				tax_struct_info=utilBean.getTaxDescr(conn,tax_struct_cd);
				tcs_struct_info=utilBean.getTaxDescr(conn,tcs_struct_cd);
			}
			rset.close();
			stmt.close();
			
			if(!address_type.equals("R") && !address_type.equals("B") && !address_type.equals("C"))
			{
				plant_seq=address_type.substring(1,address_type.length());
			}
			
			if(contract_type.equals("O") || contract_type.equals("Q"))
			{
				queryString="SELECT A.CONT_REF_NO,TO_CHAR(A.DDA_DT,'DD-MON-YY'),A.CONT_REF_NO,B.SHIP_CD,B.BOE_NO,TO_CHAR(B.BOE_DT,'DD-MON-YY'),TO_CHAR(SIGNING_DT,'DD-MON-YY'),"
						+ "B.CSOC_QTY,A.MDCQ_PERCENTAGE "
						+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_CARGO_DTL B "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? "
						+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.AGMT_TYPE=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND B.CARGO_NO=? "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
						+ ""
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, "C");
				stmt.setString(4, agmt_no);
				stmt.setString(5, agmt_rev_no);
				stmt.setString(6, "A");
				stmt.setString(7, cont_no);
				stmt.setString(8, contract_type);
				stmt.setString(9, cargo_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					contRef=rset.getString(1)==null?"":rset.getString(1);
					dda_dt=rset.getString(2)==null?"":rset.getString(2);
					//cargoRef=rset.getString(3)==null?"":rset.getString(3);
					
					String ship_cd=rset.getString(4)==null?"":rset.getString(4);
					//ship_nm=utilBean.getShipName(conn,ship_cd);
					//boe_number = rset.getString(5)==null?"":rset.getString(5);
					//boe_date=rset.getString(6)==null?"":rset.getString(6);
					
					signingDt=rset.getString(7)==null?"":rset.getString(7);
					dcq=rset.getString(8)==null?"":rset.getString(8);
					mdcq_percentage=rset.getDouble(9);
					if(Double.doubleToRawLongBits(mdcq_percentage)==Double.doubleToRawLongBits(0))
					{
						mdcq_percentage=100;
					}
				}
				rset.close();
				stmt.close();
				
				queryString1="SELECT TO_CHAR(SIGNING_DT,'DD-MON-YY')  "
						+ "FROM FMS_LTCORA_AGMT_MST A "
						+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
						+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_LTCORA_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.BUY_SALE=B.BUY_SALE) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, "A");
				stmt1.setString(3, agmt_no);
				stmt1.setString(4, counterparty_cd);
				stmt1.setString(5, "C");
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					agmtSigningDt=rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
			}
			else
			{
				queryString1="SELECT CONT_REF_NO,TO_CHAR(DDA_DT,'DD-MON-YY'),TRADE_REF_NO,TO_CHAR(SIGNING_DT,'DD-MON-YY')  "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND CONT_NO=? AND AGMT_NO=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, contract_type);
				stmt1.setString(3, cont_no);
				stmt1.setString(4, agmt_no);
				stmt1.setString(5, counterparty_cd);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					contRef=rset1.getString(1)==null?"":rset1.getString(1);
					dda_dt=rset1.getString(2)==null?"":rset1.getString(2);
					String tradeRef=rset1.getString(3)==null?"":rset1.getString(3);
					signingDt=rset1.getString(4)==null?"":rset1.getString(4);
					
					if(contract_type.equals("X"))
					{
						contRef=tradeRef;
					}
				}
				rset1.close();
				stmt1.close();
				
				if(contract_type.equals("S"))
				{
					queryString2="SELECT TO_CHAR(SIGNING_DT,'DD-MON-YY')  "
							+ "FROM FMS_AGMT_MST A "
							+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
							+ "AND AGMT_NO=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) ";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, "F");
					stmt2.setString(3, agmt_no);
					stmt2.setString(4, counterparty_cd);
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						agmtSigningDt=rset2.getString(1)==null?"":rset2.getString(1);
					}
					rset2.close();
					stmt2.close();
				}
			}
			
			queryString2="SELECT CONTACT_PERSON "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND SEQ_NO=? AND ADDR_FLAG=? AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.TYPE=B.TYPE "
					+ "AND EFF_DT <= TO_DATE(?,'DD-MON-YY'))";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, counterparty_cd);
			stmt2.setString(3, "C");
			stmt2.setString(4, contact_person_cd);
			stmt2.setString(5, address_type);
			stmt2.setString(6, "RLNG");
			stmt2.setString(7, invoice_dt);
			rset2=stmt2.executeQuery();
			if(rset2.next())
			{
				contact_person_nm=rset2.getString(1)==null?"":rset2.getString(1);
			}
			rset2.close();
			stmt2.close();
			
			queryString3="SELECT CONTACT_PERSON "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND SEQ_NO=? AND ADDR_FLAG=? AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.TYPE=B.TYPE "
					+ "AND EFF_DT <= TO_DATE(?,'DD-MON-YY'))";
			stmt3=conn.prepareStatement(queryString3);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, comp_cd);
			stmt3.setString(3, "B");
			stmt3.setString(4, bu_contact_person_cd);
			stmt3.setString(5, "P"+bu_unit);
			stmt3.setString(6, "RLNG");
			stmt3.setString(7, invoice_dt);
			rset3=stmt3.executeQuery();
			if(rset3.next())
			{
				bu_contact_person_nm=rset3.getString(1)==null?"":rset3.getString(1);
			}
			rset3.close();
			stmt3.close();
			
			HashMap bu_plant_detail=utilBean.getCounterpartyPlantDetail(conn,comp_cd, "B", comp_cd, bu_unit);
            bu_plantAddress=""+bu_plant_detail.get("plant_address");
			bu_plantCity=""+bu_plant_detail.get("plant_city");
			bu_plantState=""+bu_plant_detail.get("plant_state");
			bu_plantPin=""+bu_plant_detail.get("plant_pin");
			bu_plantNm=""+bu_plant_detail.get("plant_name");
			
			HashMap plant_detail=utilBean.getCounterpartyPlantDetail(conn,comp_cd, "C", counterparty_cd, plant_seq);
            plantAddress=""+plant_detail.get("plant_address");
			plantCity=""+plant_detail.get("plant_city");
			plantState=""+plant_detail.get("plant_state");
			plantPin=""+plant_detail.get("plant_pin");
			plantNm=""+plant_detail.get("plant_name");
			
			if(contract_type.equals("O") || contract_type.equals("Q"))
			{
				tax_info="State : "+plantState;
				tax_info+="<br>State Code : "+utilBean.getState_TIN(conn, comp_cd, counterparty_cd, "C", plant_seq);
				//String temp_tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "C", counterparty_cd, plant_seq);
				//tax_info+=temp_tax_info.replaceAll("\n", "<br>");
				
				queryString = "SELECT A.STAT_NO, TO_CHAR(A.EFF_DT,'DD/MM/YYYY'), B.STAT_NM "
						+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B "
						+ "WHERE A.COUNTERPARTY_CD=? AND A.ENTITY=? "
						+ "AND A.PLANT_SEQ_NO=? AND A.COMPANY_CD=? "
						+ "AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL AND B.STAT_CD IN ('1003','1001')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, counterparty_cd);
				stmt.setString(2, "C");
				stmt.setString(3, plant_seq);
				stmt.setString(4, comp_cd);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					String no = rset.getString(1)==null?"":rset.getString(1);
					String nm = rset.getString(3)==null?"":rset.getString(3);
	
					tax_info+="<br>"+nm+" : "+no;
				}
				stmt.close();
				rset.close();
				
				bu_tax_info="State : "+bu_plantState;
				bu_tax_info+="<br>State Code : "+utilBean.getState_TIN(conn, comp_cd, comp_cd, "B", bu_unit);
				//String temp_bu_tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "B", comp_cd, bu_unit);
				//bu_tax_info+=temp_bu_tax_info.replaceAll("\n", "<br>");
				
				queryString = "SELECT A.STAT_NO, TO_CHAR(A.EFF_DT,'DD/MM/YYYY'), B.STAT_NM "
						+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B "
						+ "WHERE A.COUNTERPARTY_CD=? AND A.ENTITY=? "
						+ "AND A.PLANT_SEQ_NO=? AND A.COMPANY_CD=? "
						+ "AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL AND B.STAT_CD IN ('1003','1001')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, "B");
				stmt.setString(3, bu_unit);
				stmt.setString(4, comp_cd);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					String no = rset.getString(1)==null?"":rset.getString(1);
					String nm = rset.getString(3)==null?"":rset.getString(3);
	
					bu_tax_info+="<br>"+nm+" : "+no;
				}
				stmt.close();
				rset.close();
				
				if(!tax_amt.equals(""))
				{
					if(contract_type.equals("O") || contract_type.equals("Q"))
					{
						if(invoice_type.equals("DI"))
						{
							bu_tax_info+="<br>SAC : 999794"
									+ "<br>Description of Service : Agreeing to tolerate an act"
									+ "<br>Place Of Supply : "+plantState; //WILL BE CUSTOMER PLANT STATE AS DISCUSSED BY JAYASRI MAM WITH VIJAY ON 20250725
						}
						else
						{
							bu_tax_info+="<br>SAC : 999799"
									+ "<br>Description of Service : Other Miscellaneous services - Other Services n.e.c."
									+ "<br>Place Of Supply : "+plantState; //WILL BE CUSTOMER PLANT STATE AS DISCUSSED BY JAYASRI MAM WITH VIJAY ON 20250725
						}
					}
				}
			}
			else
			{
				tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "C", counterparty_cd, plant_seq);
				tax_info=tax_info.replaceAll("\n", "<br>");
				
				bu_tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "B", comp_cd, bu_unit);
				bu_tax_info=bu_tax_info.replaceAll("\n", "<br>");
			}
			
			//tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "C", counterparty_cd, plant_seq);
			//tax_info=tax_info.replaceAll("\n", "<br>");
			
			//bu_tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "B", comp_cd, bu_unit);
			//bu_tax_info=bu_tax_info.replaceAll("\n", "<br>");
			
			couterpty_nm=""+utilBean.getCounterpartyName(conn,counterparty_cd);
			int srno=0;
			
			queryString0="SELECT LINE_NO,LINE_DESC,UNIT,QTY,RATE,AMOUNT "
					+ "FROM FMS_FFLOW_INV_DTL "
					+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND BU_STATE_TIN=? "
					+ "AND FINANCIAL_YEAR=? AND INVOICE_TYPE=? "
					+ "ORDER BY LINE_NO";
			stmt0=conn.prepareStatement(queryString0);
			stmt0.setString(1, comp_cd);
			stmt0.setString(2, invoice_seq);
			stmt0.setString(3, bu_state_tin);
			stmt0.setString(4, financial_year);
			stmt0.setString(5, invoice_type);
			rset0=stmt0.executeQuery();
			while(rset0.next())
			{
				srno+=1;
				VPDF_COL1.add(rset0.getString(1)==null?"":rset0.getString(1));
				VPDF_COL2.add(rset0.getString(2)==null?"":rset0.getString(2));
				VPDF_COL3.add("");
				VPDF_COL4.add(rset0.getString(3)==null?"":rset0.getString(3));
				VPDF_COL5.add(rset0.getString(4)==null?"":rset0.getString(4));
				VPDF_COL6.add(rset0.getString(5)==null?"":rset0.getString(5));
				VPDF_COL7.add(rset0.getString(6)==null?"":rset0.getString(6));
			}
			rset0.close();
			stmt0.close();
			
			if(!gross_amt.equals(""))
			{
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("Gross Amount");
				VPDF_COL3.add("");
				VPDF_COL4.add("USD");
				VPDF_COL5.add("");
				VPDF_COL6.add("");
				VPDF_COL7.add(gross_amt);
			}	
			
			if(!exchang_rate.equals(""))
			{
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("Exchange Rate");
				VPDF_COL3.add("Att2");
				VPDF_COL4.add("INR/USD");
				VPDF_COL5.add("");
				VPDF_COL6.add(exchang_rate);
				VPDF_COL7.add("");
			}
			
			if(!gross_amt1.equals(""))
			{
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("Gross Amount");
				VPDF_COL3.add("");
				VPDF_COL4.add("INR");
				VPDF_COL5.add("");
				VPDF_COL6.add("");
				VPDF_COL7.add(gross_amt1);
			}
			
			if(!tax_amt.equals(""))
			{
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("Tax ("+tax_struct_info+")");
				VPDF_COL3.add("");
				VPDF_COL4.add("INR");
				VPDF_COL5.add("");
				VPDF_COL6.add("");
				VPDF_COL7.add(tax_amt);
			}
			
			double temp_srno=srno;
			queryString1="SELECT COUNT(*) "
					+ "FROM FMS_FFLOW_INV_TAX_DTL "
					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
					+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? "
					+ "AND INVOICE_TYPE=?";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, bu_state_tin);
			stmt1.setString(3, financial_year);
			stmt1.setString(4, invoice_seq);
			stmt1.setString(5, invoice_type);
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				if(rset1.getInt(1)>1)
				{
					queryString="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
							+ "FROM FMS_FFLOW_INV_TAX_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? "
							+ "AND INVOICE_TYPE=?";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, bu_state_tin);
					stmt.setString(3, financial_year);
					stmt.setString(4, invoice_seq);
					stmt.setString(5, invoice_type);
					rset=stmt.executeQuery();
					while(rset.next())
					{
						temp_srno=temp_srno+0.1;
						VPDF_COL1.add(nf0.format(temp_srno));
						VPDF_COL2.add(rset.getString(2)==null?"":rset.getString(2));
						VPDF_COL3.add("");
						VPDF_COL4.add("INR");
						VPDF_COL5.add("");
						VPDF_COL6.add("");
						VPDF_COL7.add(rset.getString(3)==null?"":nf.format(rset.getDouble(3)));
					}
					rset.close();
					stmt.close();
				}
			}
			rset1.close();
			stmt1.close();
			
			if(!invoice_amt.equals(""))
			{
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("Invoice Amount");
				VPDF_COL3.add("");
				VPDF_COL4.add("INR");
				VPDF_COL5.add("");
				VPDF_COL6.add("");
				VPDF_COL7.add(invoice_amt);
			}
			
			if(applicable_abbr.equals("TCS"))
			{
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("TCS("+tcs_struct_info+")");
				VPDF_COL3.add("");
				VPDF_COL4.add("INR");
				VPDF_COL5.add("");
				VPDF_COL6.add("");
				VPDF_COL7.add(applicable_amt);
			}
			
			if(!invoice_adj_amt.equals(""))
			{
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("Adjustment Amount");
				VPDF_COL3.add("");
				VPDF_COL4.add("INR");
				VPDF_COL5.add("");
				VPDF_COL6.add("");
				VPDF_COL7.add(invoice_adj_amt);
			}
			
			if(!net_payable.equals(""))
			{
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("Net Amount Payable");
				VPDF_COL3.add("");
				VPDF_COL4.add("INR");
				VPDF_COL5.add("");
				VPDF_COL6.add("");
				VPDF_COL7.add(net_payable);
			}	
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getF_FlowInvoiceNumber()
	{
		String function_nm="getF_FlowInvoiceNumber()";
		try
		{
			String invoice_prefix=utilBean.getInvoicePrefix(conn,comp_cd);
			
			String fin_yr="";
			if(!financial_year.equals(""))
			{
				String[] temp = financial_year.split("-");
				fin_yr=temp[0].substring(2,temp[0].length())+"-"+temp[1].substring(2,temp[1].length());
			}
			
			String state_abbr="";
			String state_code=bu_state_tin;
			if(state_code.length()<=1)
			{
				state_code="0"+state_code;
			}
			
			if(!invoice_id_seq.equals(""))
			{
				VINVOICE_ID_SEQ.add(invoice_id_seq);
				VINVOICE_NO.add(invoice_no);
			}
			
			if(!invoice_prefix.equals(""))
			{
				int no_inv_no=10;
				for(int i=1;i<=no_inv_no;i++)
				{
					String contType="";
					String invSeries="";
					String inv_type="";
					String invFlags="";
					
					if(contract_type.equals("Q") || contract_type.equals("O"))
					{
						contType="'Q','O'";
						if(invoice_type.equals("SI") 
								|| invoice_type.equals("UG") 
								|| invoice_type.equals("ST") 
								|| invoice_type.equals("DI"))
						{
							invSeries="L";
							inv_type="'SI','UG','ST','DI'";
							invFlags="'F','UG','ST'";
						}
						else if(!invoice_type.equals("CCR") && !invoice_type.equals("CDR"))
						{
							invSeries="L"+invoice_type;
							inv_type="'"+invoice_type+"'";
							invFlags="'"+invoice_type+"'";
						}
						else
						{
							inv_type="'"+invoice_type+"'";
							invSeries=invoice_type;	
							invFlags="'"+invoice_type+"'";
						}
						
					}
					else
					{
						contType="'S','L','X'";
						invSeries=invoice_type;
						inv_type="'"+invoice_type+"'";
						invFlags="'"+invoice_type+"'";
						if(invoice_type.equals("S"))
						{
							invFlags="'F'";
						}
					}
					
					String invoice_id_seq=""+i;
					int count=0;
					
					//EMS CR DR Series Blocking for SEIPL Profile to handle Absence of FMS8 migrated data
					if(comp_cd.equals("2") && (contract_type.equals("S") || contract_type.equals("L") || contract_type.equals("X")))
					{
						if(invoice_type.equals("CR"))
						{
							if(bu_state_tin.equals("27")) //MH
							{
								if(i <= 6)
								{
									count=count+1;
								}
							}
							else if(bu_state_tin.equals("28")) //AP
							{
								if(i <= 4)
								{
									count=count+1;
								}
							}
							else if(bu_state_tin.equals("24")) //GJ
							{
								if(i <= 3)
								{
									count=count+1;
								}
							}
						}
						else if(invoice_type.equals("DR"))
						{
							if(bu_state_tin.equals("27")) //MH
							{
								if(i <= 11)
								{
									count=count+1;
								}
							}
							else if(bu_state_tin.equals("28")) //AP
							{
								if(i <= 3)
								{
									count=count+1;
								}
							}
							else if(bu_state_tin.equals("24")) //GJ
							{
								if(i <= 3)
								{
									count=count+1;
								}
							}
						}
					}
					/////////////////////////////////////////////////////////
					
					if(invoice_type.equals("CCR") || invoice_type.equals("CDR"))
					{
						queryString="SELECT COUNT(*) "
								+ "FROM FMS_FFLOW_INV_MST "
								+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
								+ "AND FINANCIAL_YEAR=? AND INVOICE_TYPE IN ("+inv_type+") "
								+ "AND INVOICE_ID_SEQ=? ";
						stmt=conn.prepareStatement(queryString);
						stmt.setString(1, comp_cd);
						stmt.setString(2, bu_state_tin);
						stmt.setString(3, financial_year);
						stmt.setString(4, invoice_id_seq);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							count += rset.getInt(1);
						}
						rset.close();
						stmt.close();
						
						queryString="SELECT COUNT(*) "
								+ "FROM FMS_DLNG_FFLOW_INV_MST "
								+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
								+ "AND FINANCIAL_YEAR=? AND INVOICE_TYPE IN ("+inv_type+") "
								+ "AND INVOICE_ID_SEQ=? ";
						stmt=conn.prepareStatement(queryString);
						stmt.setString(1, comp_cd);
						stmt.setString(2, bu_state_tin);
						stmt.setString(3, financial_year);
						stmt.setString(4, invoice_id_seq);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							count += rset.getInt(1);
						}
						rset.close();
						stmt.close();
					}
					else
					{
						queryString="SELECT COUNT(*) "
								+ "FROM FMS_FFLOW_INV_MST "
								+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
								+ "AND FINANCIAL_YEAR=? AND INVOICE_TYPE IN ("+inv_type+") "
								+ "AND INVOICE_ID_SEQ=? AND CONTRACT_TYPE IN ("+contType+") ";
						stmt=conn.prepareStatement(queryString);
						stmt.setString(1, comp_cd);
						stmt.setString(2, bu_state_tin);
						stmt.setString(3, financial_year);
						stmt.setString(4, invoice_id_seq);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							count += rset.getInt(1);
						}
						rset.close();
						stmt.close();
					}
					
					
					if(invoice_type.equals("S") 
							|| invoice_type.equals("SI") 
							|| invoice_type.equals("UG") 
							|| invoice_type.equals("ST") 
							|| invoice_type.equals("DI")
							|| invoice_type.equals("CR") 
							|| invoice_type.equals("DR"))
					{
						queryString1="SELECT SUM(CNT) AS TOTAL_COUNT FROM ("
								+ "SELECT COUNT(*) AS CNT "
									+ "FROM FMS_INVOICE_MST "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND BU_STATE_TIN=? AND INVOICE_ID_SEQ=? AND CONTRACT_TYPE IN ("+contType+") "
									+ "AND INV_FLAG IN ("+invFlags+") "
								/*+ "UNION ALL "
								+ "SELECT COUNT(*) AS CNT "
									+ "FROM FMS_DLNG_SVC_INVOICE_MST "
									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
									+ "AND BU_STATE_TIN=? AND INVOICE_ID_SEQ=? AND CONTRACT_TYPE IN ("+contType+") "
									+ "AND INV_FLAG IN ("+invFlags+") "*/
								+ ") A";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, financial_year);
						stmt1.setString(3, bu_state_tin);
						stmt1.setString(4, invoice_id_seq);
						/*stmt1.setString(5, comp_cd);
						stmt1.setString(6, financial_year);
						stmt1.setString(7, bu_state_tin);
						stmt1.setString(8, invoice_id_seq);*/
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							count += rset1.getInt(1);
						}
						rset1.close();
						stmt1.close();
						
						if(invoice_type.equals("SI") 
								|| invoice_type.equals("UG") 
								|| invoice_type.equals("ST") 
								|| invoice_type.equals("DI"))
						{
							queryString="SELECT COUNT(*) "
									+ "FROM FMS_DLNG_FFLOW_INV_MST "
									+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
									+ "AND FINANCIAL_YEAR=? AND INVOICE_TYPE IN ('TLU') "
									+ "AND INVOICE_ID_SEQ=? AND CONTRACT_TYPE IN ("+contType+") ";
							stmt=conn.prepareStatement(queryString);
							stmt.setString(1, comp_cd);
							stmt.setString(2, bu_state_tin);
							stmt.setString(3, financial_year);
							stmt.setString(4, invoice_id_seq);
							rset=stmt.executeQuery();
							if(rset.next())
							{
								count += rset.getInt(1);
							}
							rset.close();
							stmt.close();
						}
					}
					
					if(count==0)
					{	
						queryString1="SELECT STATE_CODE "
								+ "FROM FMS_STATE_MST "
								+ "WHERE TIN=?";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, state_code);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							state_abbr=rset1.getString(1)==null?"":rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
						
						String invoice_no="";
						//invoice_no=invoice_prefix+""+invSeries+""+invoice_type+state_abbr+""+utilBean.PrePaddingZero(invoice_id_seq, 4)+"/"+fin_yr;
						invoice_no=invoice_prefix+""+invSeries+""+state_abbr+""+utilBean.PrePaddingZero(invoice_id_seq, 4)+"/"+fin_yr;
						
						VINVOICE_ID_SEQ.add(invoice_id_seq);
						VINVOICE_NO.add(invoice_no);
					}
					else
					{
						no_inv_no+=1;
					}
				}
			}
			
			if(invoice_id_seq.equals(""))
			{
				if(VINVOICE_ID_SEQ.size()>0)
				{
					invoice_id_seq=""+VINVOICE_ID_SEQ.elementAt(0);
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getAttachment1()
	{
		String function_nm="getAttachment1()";
		try
		{
			String temp_dcq=dcq;
            double mdcq_qty=0;
            double CONST_RE_QTY = 28952;
            
            double total_dcq=0;
            double total_buy_nom=0;
            double total_sell_pnq=0;
            double total_sell_regas=0;
            
            double total_delv_pnq=0;
            double total_delv_regas=0;
            double total_delv_qty=0;
            
            double totalBuyerNom=0;
            
            String cont_map=counterparty_cd+"-"+contract_type+"-"+agmt_no+"-%-"+cont_no+"-%";
			String exit_point="C-"+counterparty_cd+"-"+plant_seq;
            
            double TotalDelvQtyBfr_InvStDt=0;
            if(agmt_base.equals("D"))
            {
            	queryString1="SELECT SUM(EXIT_QTY_MMBTU) "
		  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
		  				+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
		  				+ "AND GAS_DT < TO_DATE(?,'DD/MM/YYYY') "
						+ "AND SELL_CONT_MAP LIKE ? "
						+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
            	stmt1=conn.prepareStatement(queryString1);
	            stmt1.setString(1, comp_cd);
	            stmt1.setString(2, "C");
	            stmt1.setString(3, temp_period_start_dt);
	            stmt1.setString(4, cont_map);
            }
            else
            {
	            queryString1="SELECT SUM(QTY_MMBTU) "
		  				+ "FROM FMS_DAILY_ALLOCATION_DTL A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONTRACT_TYPE=? "
						+ "AND GAS_DT < TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
						+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
	            stmt1=conn.prepareStatement(queryString1);
	            stmt1.setString(1, cont_no);
	            stmt1.setString(2, agmt_no);
	            stmt1.setString(3, comp_cd);
	            stmt1.setString(4, counterparty_cd);
	            stmt1.setString(5, contract_type);
	            stmt1.setString(6, temp_period_start_dt);
	            stmt1.setString(7, cargo_no);
            }
            rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				TotalDelvQtyBfr_InvStDt=rset1.getDouble(1);
			}
			rset1.close();
			stmt1.close();
			
			TotalDelvQtyBfr_InvStDt+=utilBean.getContractMigrationOffSetQty(conn,comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, "S");
			
			queryString="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
					+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
					+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
					+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, temp_period_start_dt);
			stmt.setString(2, temp_period_end_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String date = rset.getString(1)==null?"":rset.getString(1);
				
				String variable_dcq="";
				if(contract_type.equals("Q") || contract_type.equals("O"))
				{
					variable_dcq=utilBean.getCargoVariableCSOC(conn, comp_cd, counterparty_cd,"C", agmt_no, cont_no, contract_type, cargo_no, date);
				}
				else
				{
					variable_dcq=utilBean.getContVariableDCQ(conn,comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, date);
				}
				if(!variable_dcq.equals(""))
				{
					dcq=variable_dcq;
					if(!dcq.equals(""))
					{
						dcq=nf.format(Double.parseDouble(""+dcq));
					}
				}
				else
				{
					dcq=temp_dcq;
				}
				total_dcq+=Double.parseDouble(""+dcq);
				mdcq_qty=(Double.parseDouble(""+dcq)*mdcq_percentage)/100;
				
				queryString1="SELECT SUM(QTY_MMBTU) "
		  				+ "FROM FMS_DAILY_BUYER_NOM A "
		  				+ "WHERE COMPANY_CD=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_BUYER_NOM B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
						+ "AND B.GAS_DT=A.GAS_DT AND A.CARGO_NO=B.CARGO_NO) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, date);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					totalBuyerNom=rset1.getDouble(1);
				}
				rset1.close();
				stmt1.close();
				
				String buy_nom="0.00";
				String sell_nom="0.00";
				String alloc_qty="0.00";
				queryString2="SELECT SUM(QTY_MMBTU) "
		  				+ "FROM FMS_DAILY_BUYER_NOM A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND BU_SEQ=? "
						+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_BUYER_NOM B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
						+ "AND B.GAS_DT=A.GAS_DT AND A.CARGO_NO=B.CARGO_NO) ";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, cont_no);
				stmt2.setString(2, agmt_no);
				stmt2.setString(3, comp_cd);
				stmt2.setString(4, counterparty_cd);
				stmt2.setString(5, bu_unit);
				stmt2.setString(6, plant_seq);
				stmt2.setString(7, contract_type);
				stmt2.setString(8, date);
				stmt2.setString(9, cargo_no);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					buy_nom=nf.format(rset2.getDouble(1));
				}
				rset2.close();
				stmt2.close();
				
				total_buy_nom+=Double.parseDouble(buy_nom);
				
				queryString3="SELECT SUM(QTY_MMBTU) "
		  				+ "FROM FMS_DAILY_SELLER_NOM A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND BU_SEQ=? "
						+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_SELLER_NOM B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
						+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				stmt3=conn.prepareStatement(queryString3);
				stmt3.setString(1, cont_no);
				stmt3.setString(2, agmt_no);
				stmt3.setString(3, comp_cd);
				stmt3.setString(4, counterparty_cd);
				stmt3.setString(5, bu_unit);
				stmt3.setString(6, plant_seq);
				stmt3.setString(7, contract_type);
				stmt3.setString(8, date);
				stmt3.setString(9, cargo_no);
				rset3=stmt3.executeQuery();
				if(rset3.next())
				{
					sell_nom=nf.format(rset3.getDouble(1));
				}
				rset3.close();
				stmt3.close();
				
				double PNQ_Qty=0;
				double Seller_RE_Qty=0;
				double Gas_Delv_RE_Qty=0;
				/*if(totalBuyerNom < CONST_RE_QTY)
				{
					PNQ_Qty = 0;
					Seller_RE_Qty = Double.parseDouble(""+sell_nom);
				}
				else*/
				{
					if(Double.parseDouble(""+buy_nom)<mdcq_qty)
					{
						PNQ_Qty = Double.parseDouble(""+buy_nom);
					}
					else
					{
						PNQ_Qty = mdcq_qty;
					}
					
					PNQ_Qty = Double.parseDouble(nf.format(PNQ_Qty));
				
					Seller_RE_Qty = (Double.parseDouble(""+sell_nom) - PNQ_Qty);
					
					if(Seller_RE_Qty<0)
					{
						Seller_RE_Qty = 0;
					}
				}
				
				total_sell_pnq+=PNQ_Qty;
				total_sell_regas+=Seller_RE_Qty;
				
				VATT1_DATE.add(date);
				VATT1_DCQ.add(dcq);
				VATT1_BUYNOM.add(buy_nom);
				VATT1_SELLNOM_PNG.add(nf.format(PNQ_Qty));
				VATT1_SELLNOM_REGAS.add(nf.format(Seller_RE_Qty));
				
				if(agmt_base.equals("D"))
				{
					queryString4="SELECT SUM(EXIT_QTY_MMBTU) "
			  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
			  				+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
			  				+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND EXIT_PT_MAPPING_ID=? "
							+ "AND SELL_CONT_MAP LIKE ? AND BU_SEQ=? "
							+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
							+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				}
				else
				{
					queryString4="SELECT SUM(QTY_MMBTU) "
			  				+ "FROM FMS_DAILY_ALLOCATION_DTL A "
			  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
							+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND BU_SEQ=? "
							+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
							+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
							+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
							+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
							+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				}
				stmt4=conn.prepareStatement(queryString4);
				if(agmt_base.equals("D"))
				{
					stmt4.setString(1, comp_cd);
					stmt4.setString(2, "C");
					stmt4.setString(3, date);
					stmt4.setString(4, exit_point);
					stmt4.setString(5, cont_map);
					stmt4.setString(6, bu_unit);
				}
				else
				{
					stmt4.setString(1, cont_no);
					stmt4.setString(2, agmt_no);
					stmt4.setString(3, comp_cd);
					stmt4.setString(4, counterparty_cd);
					stmt4.setString(5, bu_unit);
					stmt4.setString(6, plant_seq);
					stmt4.setString(7, contract_type);
					stmt4.setString(8, date);
					stmt4.setString(9, cargo_no);
				}
				rset4=stmt4.executeQuery();
				if(rset4.next())
				{
					alloc_qty=nf.format(rset4.getDouble(1));
				}
				rset4.close();
				stmt4.close();
				
				if(Double.parseDouble(alloc_qty)<mdcq_qty)
				{
					PNQ_Qty = Double.parseDouble(alloc_qty);
				}
				else
				{
					PNQ_Qty = mdcq_qty;
				}
									
				Gas_Delv_RE_Qty = Double.parseDouble(alloc_qty) - PNQ_Qty;
				
				if(Gas_Delv_RE_Qty<0)
				{
					Gas_Delv_RE_Qty = 0;
				}
				total_delv_pnq+=PNQ_Qty;
				total_delv_regas+=Gas_Delv_RE_Qty;
				
				total_delv_qty+=Double.parseDouble(alloc_qty);
				
				double TotalDelvQty_InvDayWise=0;
				if(agmt_base.equals("D"))
				{
					queryString5="SELECT SUM(EXIT_QTY_MMBTU) "
			  				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
			  				+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
			  				+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND SELL_CONT_MAP LIKE ? "
							+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
							+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
				}
				else
				{
					queryString5="SELECT SUM(QTY_MMBTU) "
			  				+ "FROM FMS_DAILY_ALLOCATION_DTL A "
			  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
							+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONTRACT_TYPE=? "
							+ "AND GAS_DT = TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
							+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
							+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
							+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				}
				stmt5=conn.prepareStatement(queryString5);
				if(agmt_base.equals("D"))
				{
					stmt5.setString(1, comp_cd);
					stmt5.setString(2, "C");
					stmt5.setString(3, date);
					stmt5.setString(4, cont_map);
				}
				else
				{
					stmt5.setString(1, cont_no);
					stmt5.setString(2, agmt_no);
					stmt5.setString(3, comp_cd);
					stmt5.setString(4, counterparty_cd);
					stmt5.setString(5, contract_type);
					stmt5.setString(6, date);
					stmt5.setString(7, cargo_no);
				}
				rset5=stmt5.executeQuery();
				if(rset5.next())
				{
					TotalDelvQty_InvDayWise=rset5.getDouble(1);
				}
				rset5.close();
				stmt5.close();
				
				TotalDelvQtyBfr_InvStDt+=TotalDelvQty_InvDayWise;
				
				
				VATT1_NG_PNG.add(nf.format(PNQ_Qty));
				VATT1_NG_REGAS.add(nf.format(Gas_Delv_RE_Qty));
				VATT1_NG_TOT_DLV_GAS.add(nf.format(Double.parseDouble(alloc_qty)));
				VATT1_CUMULATIVE_QTY_BILLING_PERIOD.add(nf.format(total_delv_qty));
				VATT1_CUMULATIVE_QTY_TRANSCT_SUPPLY_PERIOD.add(nf.format(TotalDelvQtyBfr_InvStDt));
				
				/*total_dcq+=Double.parseDouble(dcq);
	            total_buy_nom+=Double.parseDouble(buy_nom);
	            total_sell_pnq+=PNQ_Qty;
	            total_sell_regas+=Seller_RE_Qty;
	            total_delv_pnq+=PNQ_Qty;
	            total_delv_regas+=Gas_Delv_RE_Qty;
	            total_delv_qty+=Double.parseDouble(alloc_qty);*/
			}
			rset.close();
			stmt.close();
			
			this.total_dcq=nf.format(total_dcq);
			this.total_buy_nom=nf.format(total_buy_nom);
			this.total_sell_pnq=nf.format(total_sell_pnq);
			this.total_sell_regas=nf.format(total_sell_regas);
			this.total_delv_pnq=nf.format(total_delv_pnq);
			this.total_delv_regas=nf.format(total_delv_regas);
			this.total_delv_qty=nf.format(total_delv_qty);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSug_Attachment1()
	{
		String function_nm="getSug_Attachment1()";
		try
		{
			String boe_number="";
			String boe_date="";
			String ship_nm="";
			
			queryString="SELECT A.CONT_REF_NO,TO_CHAR(A.DDA_DT,'DD-MON-YY'),A.CONT_REF_NO,B.SHIP_CD,B.BOE_NO,TO_CHAR(B.BOE_DT,'DD-MON-YY'),TO_CHAR(SIGNING_DT,'DD-MON-YY'),"
					+ "B.CSOC_QTY,A.MDCQ_PERCENTAGE "
					+ "FROM FMS_LTCORA_CONT_MST A,"
						+ "FMS_LTCORA_CONT_CARGO_DTL B "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? "
					+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.AGMT_TYPE=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND B.CARGO_NO=? "
					+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
					+ ""
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, "C");
			stmt.setString(4, agmt_no);
			stmt.setString(5, agmt_rev_no);
			stmt.setString(6, "A");
			stmt.setString(7, cont_no);
			stmt.setString(8, contract_type);
			stmt.setString(9, cargo_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				//contRef=rset.getString(1)==null?"":rset.getString(1);
				//dda_dt=rset.getString(2)==null?"":rset.getString(2);
				//cargoRef=rset.getString(3)==null?"":rset.getString(3);
				
				String ship_cd=rset.getString(4)==null?"":rset.getString(4);
				ship_nm=utilBean.getShipName(conn,ship_cd);
				boe_number = rset.getString(5)==null?"":rset.getString(5);
				boe_date=rset.getString(6)==null?"":rset.getString(6);
				
				//signingDt=rset.getString(7)==null?"":rset.getString(7);
				//dcq=rset.getString(8)==null?"":rset.getString(8);
				//mdcq_percentage=rset.getDouble(9);
				//if(mdcq_percentage==0)
				{
					//mdcq_percentage=100;
				}
			}
			rset.close();
			stmt.close();
			
			double total=0;
			queryString="SELECT INVOICE_NO,ALLOC_QTY,PLANT_SEQ "
					+ "FROM FMS_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=? AND INV_FLAG=? AND PDF_INV_DTL IS NOT NULL "
					+ "ORDER BY INVOICE_NO ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, agmt_no);
			stmt.setString(5, contract_type);
			stmt.setString(6, cargo_no);
			stmt.setString(7, inv_flag);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VATT_INVOICE_NO.add(rset.getString(1)==null?"":rset.getString(1));
				VQTY.add(nf.format(rset.getDouble(2)));
				String plantSeq=rset.getString(3)==null?"":rset.getString(3);
				VPLANT_ABBR.add(utilBean.getCounterpartyPlantABBR(conn, counterparty_cd, comp_cd, plantSeq, "C"));
				VBOE_ENTRY.add(boe_number+" Dated "+boe_date);
				VCARGO_NM.add(ship_nm);
				
				total+=rset.getDouble(2);
			}
			rset.close();
			stmt.close();
			
			VATT_INVOICE_NO.add(invoice_no);
			VQTY.add(qty_mmbtu);
			VPLANT_ABBR.add(utilBean.getCounterpartyPlantABBR(conn, counterparty_cd, comp_cd, plant_seq, "C"));
			VBOE_ENTRY.add(boe_number+" Dated "+boe_date);
			VCARGO_NM.add(ship_nm);
			
			if(!qty_mmbtu.equals(""))
			{
				total+=Double.parseDouble(qty_mmbtu);
			}
			
			total_delv_qty=nf.format(total);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getStorage_Attachment1()
	{
		String function_nm="getStorage_Attachment1()";
		try
		{
			queryString1="SELECT TO_CHAR(STORAGE_DT,'DD-MON-YY'),OPEN_BALANCE_QTY,OFFTAKE_QTY,RATE,RATE_TYPE,DAY_DISCOUNT "
					+ "FROM FMS_INV_STORAGE_CRG_DTL "
					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
					+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=?";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, bu_state_tin);
			stmt1.setString(3, financial_year);
			stmt1.setString(4, invoice_seq);
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				String storageInv=rset1.getString(2)==null?"":nf.format(rset1.getDouble(2));
				VSTORAGE_DATE.add(rset1.getString(1)==null?"":rset1.getString(1));
				VSTORAGE_INVENTORY.add(storageInv);
				VOFFTAKE_QTY.add(rset1.getString(3)==null?"":nf.format(rset1.getDouble(3)));
				
				String rate=rset1.getString(4)==null?"":utilBean.RateNumberFormat(rset1.getDouble(4),price_cd);
				String rate_type=rset1.getString(5)==null?"":rset1.getString(5);
				String discount_day=rset1.getString(6)==null?"":rset1.getString(6);
				rate=discount_day.equals("Y")?"0.00":rate;
				VRATE_TYPE.add(rate_type);
				VSTORAGE_CHARGE.add(rate);
				
				String storage_amt="0.00";
				if(!storageInv.equals("") && !rate.equals("") && discount_day.equals("N"))
				{
					storage_amt=nf.format(Double.parseDouble(storageInv) * Double.parseDouble(rate));
				}
				
				VSTORAGE_AMT.add(storage_amt);
				VDISCOUNT_FLAG.add(discount_day);
			}
			rset1.close();
			stmt1.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	public void getAttachment2()
	{
		String function_nm="getAttachment2()";
		try
		{
			if(exchng_rate_cd.equals("0"))
            {
				VATT2_EXCHANGE_DESC.add("Fixed Exchange Rate Applicable(INR/USD)");
				VATT2_RATE.add(nf2.format(Double.parseDouble(""+exchang_rate)));
            }
			else
			{
				String component_flag="";
            	String component1="";
            	String component2="";
            	String exchang_rate_nm="";
            	String component1_rate_nm="";
            	String component2_rate_nm="";
            	String component1_rate="";
            	String component2_rate="";
            	
            	queryString="SELECT COMPONENT_FLAG,COMPONENT1,COMPONENT2,EXC_RATE_NM,BANK_ABBR "
						+ "FROM FMS_EXCHG_RATE_MST "
						+ "WHERE EXC_RATE_CD=?";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, exchng_rate_cd);
            	rset=stmt.executeQuery();
				if(rset.next())
				{
					component_flag=rset.getString(1)==null?"":rset.getString(1);
					component1=rset.getString(2)==null?"":rset.getString(2);
					component2=rset.getString(3)==null?"":rset.getString(3);
					exchang_rate_nm=rset.getString(4)==null?"":rset.getString(4);
					source=rset.getString(5)==null?"":rset.getString(5);
				}
				rset.close();
				stmt.close();
				
				if(exchang_rate_type.equals("A"))
				{
					queryString1="SELECT EXCHG_RATE_VALUE,TO_CHAR(ALLOCATION_DT,'DD-MON-YY') "
							+ "FROM FMS_INVOICE_DTL "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND BU_STATE_TIN=? AND INVOICE_SEQ=? "
							+ "ORDER BY ALLOCATION_DT";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, financial_year);
					stmt1.setString(3, bu_state_tin);
					stmt1.setString(4, invoice_seq);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						String ExRate=nf2.format(rset1.getDouble(1));
						String allocDt=rset1.getString(2)==null?"":rset1.getString(2);
						
						VATT2_EXCHANGE_DESC.add(exchang_rate_nm+".. On "+allocDt+" (INR/USD)");
						VATT2_RATE.add(nf2.format(Double.parseDouble(""+ExRate)));
					}
					rset1.close();
					stmt1.close();
				}
				else if(component_flag.equals("Y"))
				{
					queryString1="SELECT EXC_RATE_NM "
							+ "FROM FMS_EXCHG_RATE_MST "
							+ "WHERE EXC_RATE_CD=?";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, component1);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						component1_rate_nm=rset1.getString(1)==null?"":rset1.getString(1);
					}
					rset1.close();
					stmt1.close();
					
					queryString2="SELECT EXC_RATE_NM "
							+ "FROM FMS_EXCHG_RATE_MST "
							+ "WHERE EXC_RATE_CD=?";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, component2);
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						component2_rate_nm=rset2.getString(1)==null?"":rset2.getString(1);
					}
					rset2.close();
					stmt2.close();
					
					queryString3="SELECT EXCHG_VAL "
							+ "FROM FMS_EXCHG_RATE_ENTRY "
							+ "WHERE EXCHG_RATE_CD=? "
							+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY')";
					stmt3=conn.prepareStatement(queryString3);
					stmt3.setString(1, component1);
					stmt3.setString(2, exchang_rate_dt);
					rset3=stmt3.executeQuery();
					if(rset3.next())
					{
						component1_rate=nf2.format(rset3.getDouble(1));
					}
					rset3.close();
					stmt3.close();
					
					queryString4="SELECT EXCHG_VAL "
							+ "FROM FMS_EXCHG_RATE_ENTRY "
							+ "WHERE EXCHG_RATE_CD=? "
							+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY')";
					stmt4=conn.prepareStatement(queryString4);
					stmt4.setString(1, component2);
					stmt4.setString(2, exchang_rate_dt);
					rset4=stmt4.executeQuery();
					if(rset4.next())
					{
						component2_rate=nf2.format(rset4.getDouble(1));
					}
					rset4.close();
					stmt4.close();

					VATT2_EXCHANGE_DESC.add(component1_rate_nm+".. On "+exchang_rate_dt+" (INR/USD)");
					VATT2_RATE.add(nf2.format(Double.parseDouble(""+component1_rate)));
					
					VATT2_EXCHANGE_DESC.add(component2_rate_nm+".. On "+exchang_rate_dt+" (INR/USD)");
					VATT2_RATE.add(nf2.format(Double.parseDouble(""+component2_rate)));
				}
				else
				{
					VATT2_EXCHANGE_DESC.add(exchang_rate_nm+".. On "+exchang_rate_dt+" (INR/USD)");
					VATT2_RATE.add(nf2.format(Double.parseDouble(""+exchang_rate)));
				}
			}
			
			total_exchng_label="Exchange Rate Applicable(INR/USD)";
			total_exchng_label_rate=exchang_rate;
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSug_Attachment2()
	{
		String function_nm="getSug_Attachment2()";
		try
		{
			String boe_number="";
			String boe_date="";
			String ship_nm="";
			
			queryString="SELECT A.CONT_REF_NO,TO_CHAR(A.DDA_DT,'DD-MON-YY'),A.CONT_REF_NO,B.SHIP_CD,B.BOE_NO,TO_CHAR(B.BOE_DT,'DD-MON-YY'),TO_CHAR(SIGNING_DT,'DD-MON-YY'),"
					+ "B.CSOC_QTY,A.MDCQ_PERCENTAGE "
					+ "FROM FMS_LTCORA_CONT_MST A,"
						+ "FMS_LTCORA_CONT_CARGO_DTL B "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? "
					+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.AGMT_TYPE=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND B.CARGO_NO=? "
					+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
					+ ""
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, "C");
			stmt.setString(4, agmt_no);
			stmt.setString(5, agmt_rev_no);
			stmt.setString(6, "A");
			stmt.setString(7, cont_no);
			stmt.setString(8, contract_type);
			stmt.setString(9, cargo_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				//contRef=rset.getString(1)==null?"":rset.getString(1);
				//dda_dt=rset.getString(2)==null?"":rset.getString(2);
				//cargoRef=rset.getString(3)==null?"":rset.getString(3);
				
				String ship_cd=rset.getString(4)==null?"":rset.getString(4);
				ship_nm=utilBean.getShipName(conn,ship_cd);
				boe_number = rset.getString(5)==null?"":rset.getString(5);
				boe_date=rset.getString(6)==null?"":rset.getString(6);
				
				//signingDt=rset.getString(7)==null?"":rset.getString(7);
				//dcq=rset.getString(8)==null?"":rset.getString(8);
				//mdcq_percentage=rset.getDouble(9);
				//if(mdcq_percentage==0)
				{
					//mdcq_percentage=100;
				}
			}
			rset.close();
			stmt.close();
			
			//VINVOICE_NO.add(invoice_no);
			VPRICE.add(price);
			//VPLANT_ABBR.add(utilBean.getCounterpartyPlantABBR(conn, counterparty_cd, comp_cd, plant_seq, "C"));
			VBOE_ENTRY.add(boe_number+" Dated "+boe_date);
			VCARGO_NM.add(ship_nm);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSendInvoiceMailDetail()
	{
		String function_nm="getSendInvoiceMailDetail()";
		try
		{
			String ownAddress="";
			String ownCity="";
			String ownState="";
			String ownPin="";
			String ownCountry="";
			String ownPhone="";
			String ownEmail="";
			
			queryString="SELECT ADDR,CITY,STATE,PIN,COUNTRY,PHONE,EMAIL "
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
			
			String bu_contact_person_cd="";
			String contact_person_cd="";
			String invoiceNo="";
			String invoiceDt="";
			String dueDate="";
			String other_inv_str="";
			String invFlag="";
			
			if(mail_inv_type.equals("F"))
			{
				queryString1="SELECT BU_CONTACT_PERSON_CD,CONTACT_PERSON_CD,INVOICE_NO,TO_CHAR(DUE_DT,'DD/MM/YYYY'),"
						+ "TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),OTHER_INV_STR "
						+ "FROM FMS_FFLOW_INV_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND BU_STATE_TIN=? AND FINANCIAL_YEAR=? "
						+ "AND INVOICE_SEQ=? AND INVOICE_TYPE=?";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, bu_state_tin);
				stmt1.setString(4, financial_year);
				stmt1.setString(5, invoice_seq);
				stmt1.setString(6, invoice_type);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					bu_contact_person_cd=rset1.getString(1)==null?"0":rset1.getString(1);
					contact_person_cd=rset1.getString(2)==null?"0":rset1.getString(2);
					invoiceNo=rset1.getString(3)==null?"":rset1.getString(3);
					dueDate=rset1.getString(4)==null?"":rset1.getString(4);
					invoiceDt=rset1.getString(5)==null?"":rset1.getString(5);
					other_inv_str=rset1.getString(6)==null?"":rset1.getString(6);
				}
				rset1.close();
				stmt1.close();
			}
			else
			{
				queryString1="SELECT BU_CONTACT_PERSON_CD,CONTACT_PERSON_CD,INVOICE_NO,TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),INV_FLAG "
						+ "FROM FMS_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND BU_STATE_TIN=? AND FINANCIAL_YEAR=? "
						+ "AND INVOICE_SEQ=?";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, bu_state_tin);
				stmt1.setString(4, financial_year);
				stmt1.setString(5, invoice_seq);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					bu_contact_person_cd=rset1.getString(1)==null?"0":rset1.getString(1);
					contact_person_cd=rset1.getString(2)==null?"0":rset1.getString(2);
					invoiceNo=rset1.getString(3)==null?"":rset1.getString(3);
					dueDate=rset1.getString(4)==null?"":rset1.getString(4);
					invoiceDt=rset1.getString(5)==null?"":rset1.getString(5);
					invFlag=rset1.getString(6)==null?"":rset1.getString(6);
				}
				rset1.close();
				stmt1.close();
			}
			
			
			String[] pdf_type=mail_pdf_type.split(",");
			
			for(int i=0; i<pdf_type.length; i++)
			{
				String temp_pdf_type=""+pdf_type[i];
				
				VMAIL_PDF_TYPE.add(temp_pdf_type);
				
				String temp_pdf_type_nm="";
				if(temp_pdf_type.equals("O"))
				{
					temp_pdf_type_nm="Original";
					VMAIL_PDF_TYPE_NM.add("Original Invoice");
				}
				else if(temp_pdf_type.equals("D"))
				{
					temp_pdf_type_nm="Duplicate";
					VMAIL_PDF_TYPE_NM.add("Duplicate Invoice");
				}
				else if(temp_pdf_type.equals("T"))
				{
					temp_pdf_type_nm="Triplicate";
					VMAIL_PDF_TYPE_NM.add("Triplicate Invoice");
				}
				else
				{
					temp_pdf_type_nm="Triplicate";
					VMAIL_PDF_TYPE_NM.add("");
				}
				
				String companyAbbr=utilBean.getCompanyAbbr(conn,comp_cd);
				String customerNm=utilBean.getCounterpartyName(conn,counterparty_cd);
				String subject="";
				String type="";
				if(invoice_type.equals("DR"))
				{
					//type="DEBIT NOTE";
					type=other_inv_str;
				}
				else if(invoice_type.equals("CR"))
				{
					//type="CREDIT NOTE";
					type=other_inv_str;
				}
				else if(invoice_type.equals("CDR"))
				{
					//type="DEBIT NOTE";
					type=other_inv_str;
				}
				else if(invoice_type.equals("CCR"))
				{
					//type="CREDIT NOTE";
					type=other_inv_str;
				}
				else if(invoice_type.equals("LP"))
				{
					//type="LATE PAYMENT INVOICE";
					type=other_inv_str;
				}
				else if(invFlag.equals("LP"))
				{
					type="LATE PAYMENT INVOICE";
//					type=other_inv_str;
				}
				else if(invoice_type.equals("OR"))
				{
					//type="OTHER";
					type=other_inv_str;
				}
				else if(invoice_type.equals("S"))
				{
					//type="OTHER";
					type=other_inv_str;
				}
				else if(invFlag.equals("DR"))
				{
					type="DEBIT NOTE";
				}
				else if(invFlag.equals("CR"))
				{
					type="CREDIT NOTE";
				}
				else if(invoice_type.equals("UG") || invFlag.equals("UG")) //DhvaniT25051121 added this condition for Incident#BUG 2510058
				{
					type="SUG";
				}
				else
				{
					type="SALES";
				}
				
				if(invFlag.equals("LP")) 
				{
					subject=companyAbbr+"/"+customerNm+"/"+type+"/"+temp_pdf_type_nm+"/"+invoiceNo;
				}
				else
				{
					subject=companyAbbr+"/"+customerNm+"/"+type+"/"+temp_pdf_type_nm+"/"+dueDate.replaceAll("/", "-")+"/"+invoiceNo;
				}
				
				String to_list="";
				String cc_list="";
				
				if(temp_pdf_type.equals("T"))
				{
					to_list=utilBean.getEntityContactPersonEmailList(conn, comp_cd, comp_cd, "B", "P"+bu_unit, "INV", "RLNG","Y");
				}
				else
				{
					if(mail_inv_type.equals("F"))
					{
						to_list=utilBean.getEntityContactPersonEmailList(conn, comp_cd, counterparty_cd, "C", plant_seq, "INV", "RLNG","Y");
						cc_list=utilBean.getEntityContactPersonEmailList(conn, comp_cd, counterparty_cd, "C", plant_seq, "INV", "RLNG","N");
					}
					else
					{
						to_list=utilBean.getEntityContactPersonEmailList(conn, comp_cd, counterparty_cd, "C", "P"+plant_seq, "INV", "RLNG","Y");
						cc_list=utilBean.getEntityContactPersonEmailList(conn, comp_cd, counterparty_cd, "C", "P"+plant_seq, "INV", "RLNG","N");
					}
				}
				
				String tmpCcList=utilBean.getEntityContactPersonEmailList(conn, comp_cd, comp_cd, "B", "P"+bu_unit, "INV", "RLNG","N");
				cc_list+=cc_list.equals("")?tmpCcList:","+tmpCcList;
				
				//get BCc list
				String bcc_list=utilBean.getEntityContactPersonEmailList(conn, comp_cd, comp_cd, "B", "P"+bu_unit, "INV", "RLNG","B");
				
				if(temp_pdf_type.equals("T") && to_list.equals(""))
				{
					to_list=cc_list;
				}
	
				if(!temp_pdf_type.equals("T"))
				{
					if(contract_type.equals("X")) //ADDITIONAL EMAIL IDs FOR IGX ONLY
					{
						queryString3="SELECT AGMT_REV,CONT_REV "
								+ "FROM FMS_SUPPLY_CONT_MST A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND CONT_NO=? AND AGMT_NO=? AND CONTRACT_TYPE=? "
								+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
								+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
						stmt3=conn.prepareStatement(queryString3);
						stmt3.setString(1, comp_cd);
						stmt3.setString(2, counterparty_cd);
						stmt3.setString(3, cont_no);
						stmt3.setString(4, agmt_no);
						stmt3.setString(5, contract_type);
						rset3=stmt3.executeQuery();
						if(rset3.next())
						{
							String agmtRev=rset3.getString(1)==null?"":rset3.getString(1);
							String contRev=rset3.getString(2)==null?"":rset3.getString(2);
							
							queryString4="SELECT GX_COUNTERPARTY_CD,GX_BU_SEQ_NO "
									+ "FROM FMS_SUPPLY_CONT_GX_BU "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
									+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
									+ "AND CONTRACT_TYPE=?";
							stmt4=conn.prepareStatement(queryString4);
							stmt4.setString(1, comp_cd);
							stmt4.setString(2, counterparty_cd);
							stmt4.setString(3, cont_no);
							stmt4.setString(4, contRev);
							stmt4.setString(5, agmt_no);
							stmt4.setString(6, agmtRev);
							stmt4.setString(7, contract_type);
							rset4=stmt4.executeQuery();
							while(rset4.next())
							{
								String gx_cd=rset4.getString(1)==null?"":rset4.getString(1);
								String gx_bu_cd=rset4.getString(2)==null?"":rset4.getString(2);
								
								String tmpToList=utilBean.getEntityContactPersonEmailList(conn, comp_cd, gx_cd, "G", "B"+gx_bu_cd, "INV", "RLNG","Y");
								to_list+=to_list.equals("")?tmpToList:","+tmpToList;
								
								tmpCcList=utilBean.getEntityContactPersonEmailList(conn, comp_cd, gx_cd, "G", "B"+gx_bu_cd, "INV", "RLNG","N");
								cc_list+=cc_list.equals("")?tmpCcList:","+tmpCcList;	
							}
							rset4.close();
							stmt4.close();
						}
						rset3.close();
						stmt3.close();
					}
				}
				
				String attachment="";
				Vector VTEMP_MAIL_ATTACHMENT = new Vector(); 
				Vector VTEMP_MAIL_ANNEXURE_ATTACHMENT_FLAG = new Vector();
				int st_count=0;
				if(mail_inv_type.equals("F"))
				{
					queryString3="SELECT FILE_NAME "
							+ "FROM FMS_FFLOW_INV_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
		 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
		 	        		+ "AND PDF_TYPE=? AND PDF_SIGNED=? "
		 	        		+ "AND INVOICE_TYPE=?";
				}
				else
				{
					queryString3="SELECT FILE_NAME "
							+ "FROM FMS_INV_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
		 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
		 	        		+ "AND PDF_TYPE=? AND PDF_SIGNED=?";
				}
				stmt3=conn.prepareStatement(queryString3);
				stmt3.setString(++st_count, comp_cd);
				stmt3.setString(++st_count, bu_state_tin);
				stmt3.setString(++st_count, invoice_seq);
				stmt3.setString(++st_count, financial_year);
				stmt3.setString(++st_count, temp_pdf_type);
				stmt3.setString(++st_count, "Y");
				if(mail_inv_type.equals("F"))
				{
					stmt3.setString(++st_count, invoice_type);
				}
				rset3=stmt3.executeQuery();
				if(rset3.next())
				{
					//attachment=rset.getString(1)==null?"":rset.getString(1);
					VTEMP_MAIL_ATTACHMENT.add(rset3.getString(1)==null?"":rset3.getString(1));
					VTEMP_MAIL_ANNEXURE_ATTACHMENT_FLAG.add("");
				}
				rset3.close();
				stmt3.close();
				
				if(mail_inv_type.equals("F"))
				{
					queryString4="SELECT FILE_NAME "
							+ "FROM FMS_FFLOW_INV_ANNEXURE_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
		 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
		 	        		+ "AND INVOICE_TYPE=?";
					stmt4=conn.prepareStatement(queryString4);
					stmt4.setString(1, comp_cd);
					stmt4.setString(2, bu_state_tin);
					stmt4.setString(3, invoice_seq);
					stmt4.setString(4, financial_year);
					stmt4.setString(5, invoice_type);
					rset4=stmt4.executeQuery();
					while(rset4.next())
					{
						//attachment=rset.getString(1)==null?"":rset.getString(1);
						VTEMP_MAIL_ATTACHMENT.add(rset4.getString(1)==null?"":rset4.getString(1));
						VTEMP_MAIL_ANNEXURE_ATTACHMENT_FLAG.add("Y");
					}
					rset4.close();
					stmt4.close();
				}
				else
				{
					//annexure for dr/cr imb/ship/unauth
					queryString4="SELECT FILE_NAME "
							+ "FROM FMS_INV_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
		 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
		 	        		+ "AND PDF_TYPE=? ";
					stmt4=conn.prepareStatement(queryString4);
					stmt4.setString(1, comp_cd);
					stmt4.setString(2, bu_state_tin);
					stmt4.setString(3, invoice_seq);
					stmt4.setString(4, financial_year);
					stmt4.setString(5, "A");
					rset4=stmt4.executeQuery();
					if(rset4.next())
					{
						//attachment=rset.getString(1)==null?"":rset.getString(1);
						VTEMP_MAIL_ATTACHMENT.add(rset4.getString(1)==null?"":rset4.getString(1));
						VTEMP_MAIL_ANNEXURE_ATTACHMENT_FLAG.add("Y");
					}
					rset4.close();
					stmt4.close();
				}
				
				String companyNm=utilBean.getCompanyName(conn,comp_cd);
				String mail_body="Dear Sir/Madam,"
						+ "\n\nPlease find enclosed Invoice# "+invoiceNo+" dated "+invoiceDt.replaceAll("/", "-")+".";
				if(!invoice_type.equals("CR") && !invoice_type.equals("CCR") && !invFlag.equals("CR") && !invFlag.equals("LP"))
				{
					mail_body+= "\n\nYou are requested to pay on or before the due date "+dueDate+". If already paid or no amount is due kindly ignore this message.";
				}		
				mail_body+= "\n\nIn case of any query, please contact us at "+ownEmail+""				
						+ "\n\nNOTE : Bank Account changes should not be made without re-confirmation with your usual SHELL Contact."
						+ "\nUnless communicated by your usual Shell Contact/Authorized Representative, Bank Account changes should not be made. Any proposed change should always be confirmed initially by Phone and then Electronically (Email) with your usual SHELL Contact. Most Importantly check for valid Domain Name (@SHELL.COM) in the E-mail Address."
						+ "\n\n\nThank You,"
						+ "\n\n"+companyNm+""
						+ "\n"+ownAddress+", "
						+ "\n"+ownCity+", "+ownState+" - "+ownPin+""
						+ "\nEmail: "+ownEmail+""
						+ "\nPh: "+ownPhone+""
						+ "\n\nThis is an auto-generated email from the system, please do not reply to this email.";	
				
				VMAIL_FROM_LIST.add(utilBean.getFromMailId(conn));
				VMAIL_TO_LIST.add(to_list);
				VMAIL_CC_LIST.add(cc_list);
				VMAIL_BCC_LIST.add(bcc_list);
				VMAIL_SUBJECT.add(subject);
				//VMAIL_ATTACHMENT.add(attachment);
				VMAIL_ATTACHMENT.add(VTEMP_MAIL_ATTACHMENT);
				VMAIL_ANNEXURE_ATTACHMENT_FLAG.add(VTEMP_MAIL_ANNEXURE_ATTACHMENT_FLAG);

				if(mail_inv_type.equals("F"))
				{
					VMAIL_ATTACHMENT_PATH.add(CommonVariable.signed_freeflow_inv_path);
				}
				else
				{
					VMAIL_ATTACHMENT_PATH.add(CommonVariable.signed_sales_inv_path);
				}
				
				String annexure_folder="";
				if(!mail_inv_type.equals("F"))
				{
					annexure_folder=comp_cd+"_"+financial_year+"_"+bu_state_tin+"_"+invoice_seq;
					VMAIL_ANNEXURE_ATTACHMENT_PATH.add(CommonVariable.crdr_annexure_path+""+annexure_folder+"/");
				}
				else
				{
					annexure_folder=invoice_type+"_"+financial_year+"_"+bu_state_tin+"_"+invoice_seq;
					VMAIL_ANNEXURE_ATTACHMENT_PATH.add(CommonVariable.freeflow_annexure_path+""+annexure_folder+"/");
				}
				VMAIL_BODY.add(mail_body);	
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getLtcoraSugInvoicePreparationList()
	{
		String function_nm="getLtcoraSugInvoicePreparationList()";
		try
		{
			String temp_period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
			String temp_period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
			
			queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
					+ "A.CONTRACT_TYPE,B.CARGO_NO,B.CARGO_REF  "
					+ "FROM FMS_LTCORA_CONT_MST A,"
						+ "FMS_LTCORA_CONT_CARGO_DTL B "
					+ "WHERE A.COMPANY_CD=? AND A.BUY_SALE=? AND A.FCC_FLAG=? AND B.CARGO_STATUS=? AND A.AGMT_TYPE=? "
					+ "AND TO_CHAR(B.QQ_DT,'MM/YYYY')=? "
					+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
					+ ""
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "C");
			stmt.setString(3, "Y");
			stmt.setString(4, "Y");
			stmt.setString(5, "A");
			stmt.setString(6, month+"/"+year);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String agmtno=rset.getString(3)==null?"0":rset.getString(3);
				String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
				String contno=rset.getString(5)==null?"0":rset.getString(5);
				String contrev=rset.getString(6)==null?"0":rset.getString(6);
				String cont_type = rset.getString(7)==null?"":rset.getString(7);
				String cargo_no = rset.getString(8)==null?"0":rset.getString(8);
				String contRef=rset.getString(9)==null?"":rset.getString(9);
				
				String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, cargo_no);
				String countpty_nm=utilBean.getCounterpartyName(conn,countpty_cd);
				String countpty_abbr=utilBean.getCounterpartyABBR(conn,countpty_cd);
				
				VINVOICE_LIST_ABBR.add(deal_no);
				VINVOICE_LIST_NAME.add(countpty_nm+" ["+contRef+"] "+deal_no);
				
				int index=0;
				queryString1="SELECT D.PLANT_SEQ_NO,E.PLANT_SEQ_NO "
						+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_BU D,"
							+ "FMS_LTCORA_CONT_PLANT E "
						+ "WHERE A.COMPANY_CD=? AND A.BUY_SALE=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? AND A.CONT_REV=? "
						+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.AGMT_TYPE=? AND A.CONTRACT_TYPE=? "
						+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.CONT_NO=D.CONT_NO AND A.CONT_REV=D.CONT_REV "
						+ "AND A.AGMT_NO=D.AGMT_NO AND A.AGMT_REV=D.AGMT_REV AND A.CONTRACT_TYPE=D.CONTRACT_TYPE AND A.BUY_SALE=D.BUY_SALE AND A.AGMT_TYPE=D.AGMT_TYPE "
						+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO AND A.CONT_REV=E.CONT_REV "
						+ "AND A.AGMT_NO=E.AGMT_NO AND A.AGMT_REV=E.AGMT_REV AND A.CONTRACT_TYPE=E.CONTRACT_TYPE AND A.BUY_SALE=E.BUY_SALE AND A.AGMT_TYPE=E.AGMT_TYPE ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, own_cd);
				stmt1.setString(2, "C");
				stmt1.setString(3, countpty_cd);
				stmt1.setString(4, contno);
				stmt1.setString(5, contrev);
				stmt1.setString(6, agmtno);
				stmt1.setString(7, agmtrev);
				stmt1.setString(8, "A");
				stmt1.setString(9, cont_type);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					index+=1;
					
					String bu_plant_seq = rset1.getString(1)==null?"":rset1.getString(1);
					String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
					
					String state_code = utilBean.getState_TIN(conn, own_cd, own_cd, "B", bu_plant_seq);
					
					String plant_seq=rset1.getString(2)==null?"":rset1.getString(2);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "C");
					
					VBU_STATE_TIN.add(state_code);
					VCOUNTERPTY_CD.add(countpty_cd);
					VCOUNTERPTY_ABBR.add(countpty_abbr);
					VAGMT_NO.add(agmtno);
					VAGMT_REV_NO.add(agmtrev);
					VCONT_NO.add(contno);
					VCONT_REV_NO.add(contrev);
					VCARGO_NO.add(cargo_no);
					//VBOE_NO.add("");
					//VBOE_NM.add("");
					VCONT_REF_NO.add(contRef);
					VCONTRACT_TYPE.add(cont_type);
					VDEAL_NO.add(deal_no);
					VAGMT_BASE.add("");

					VPLANT_SEQ.add(plant_seq);
					VPLANT_ABBR.add(plant_abbr);
					VBU_PLANT_SEQ.add(bu_plant_seq);
					VBU_PLANT_ABBR.add(bu_plant_abbr);
					VPERIOD_START_DT.add(temp_period_start_dt);
					VPERIOD_END_DT.add(temp_period_end_dt);
					VTEMP_PERIOD_START_DT.add(temp_period_start_dt);
					VTEMP_PERIOD_END_DT.add(temp_period_end_dt);
					//VALLOC_QTY.add(nf.format(qtyMMBTU));
					
					//VSTATUS.add("");
					String inv_flag="UG";
					VINV_FLAG.add(inv_flag);
					getInvDetailForPreparationList(own_cd,countpty_cd, contno, agmtno,cont_type, plant_seq, 
							bu_plant_seq, "0", temp_period_start_dt, temp_period_end_dt, state_code,cargo_no,inv_flag);
					
					VBILLING_FREQ_FLAG.add("0");
					VBILLING_FREQ_NM.add("");
				}
				rset1.close();
				stmt1.close();
				
				VINDEX.add(index);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getLtcoraStorageInvoicePreparationList()
	{
		String function_nm="getLtcoraStorageInvoicePreparationList()";
		try
		{
			String temp_period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
			String temp_period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
			
			//AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')+NVL(STORAGE_DAYS-1,0)+1 <=TO_DATE(?,'DD/MM/YYYY')
			queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,"
					+ "A.CONTRACT_TYPE,B.CARGO_NO,B.CARGO_REF,D.PLANT_SEQ_NO,E.PLANT_SEQ_NO,"
					+ "TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'), NVL(B.STORAGE_DAYS-1,0),NVL(B.STORAGE_EXT_DAYS,0) "
					+ "FROM FMS_LTCORA_CONT_MST A,"
						+ "FMS_LTCORA_CONT_CARGO_DTL B,"
						+ "FMS_LTCORA_CONT_BILLING_DTL C,"
						+ "FMS_LTCORA_CONT_BU D,"
						+ "FMS_LTCORA_CONT_PLANT E "
					+ "WHERE A.COMPANY_CD=? AND A.BUY_SALE=? AND A.FCC_FLAG=? AND B.CARGO_STATUS=? AND A.AGMT_TYPE=? "
					+ "AND B.STORAGE_EXT_DAYS IS NOT NULL AND B.STORAGE_EXT_DAYS > 0 AND A.EXTEND_STORAGE=? "
					+ "AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')+NVL(STORAGE_DAYS-1,0)+1 <= TO_DATE(TO_CHAR(SYSDATE-1,'DD/MM/YYYY'),'DD/MM/YYYY') "
					+ "AND CASE WHEN TO_DATE(TO_CHAR(SYSDATE-1,'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0) "
					+ "THEN TO_CHAR(TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0),'MM/YYYY') "
					+ "ELSE TO_CHAR(SYSDATE-1,'MM/YYYY') END =? " //INC#2600001 SYSDATE-1
					+ ""
					+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
					+ ""
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE "
					+ ""
					+ ""
					+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.CONT_NO=D.CONT_NO AND A.CONT_REV=D.CONT_REV "
					+ "AND A.AGMT_NO=D.AGMT_NO AND A.AGMT_REV=D.AGMT_REV AND A.CONTRACT_TYPE=D.CONTRACT_TYPE AND A.BUY_SALE=D.BUY_SALE AND A.AGMT_TYPE=D.AGMT_TYPE "
					+ ""
					+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.CONT_NO=E.CONT_NO AND A.CONT_REV=E.CONT_REV "
					+ "AND A.AGMT_NO=E.AGMT_NO AND A.AGMT_REV=E.AGMT_REV AND A.CONTRACT_TYPE=E.CONTRACT_TYPE AND A.BUY_SALE=E.BUY_SALE AND A.AGMT_TYPE=E.AGMT_TYPE "
					+ ""
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.CONT_NO=C.CONT_NO "//AND A.CONT_REV=C.CONT_REV "
					+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.BUY_SALE=C.BUY_SALE AND A.AGMT_TYPE=C.AGMT_TYPE "
					+ "AND E.PLANT_SEQ_NO=C.PLANT_SEQ_NO "
					+ "ORDER BY (SELECT Z.COUNTERPARTY_ABBR FROM FMS_COUNTERPARTY_MST Z WHERE A.COUNTERPARTY_CD=Z.COUNTERPARTY_CD AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST Y WHERE Y.COUNTERPARTY_CD=Z.COUNTERPARTY_CD))"					
					+ "";
					/*+ "AND (SELECT NVL(SUM(QTY_MMBTU),0) FROM FMS_DAILY_ALLOCATION_DTL F WHERE A.COMPANY_CD=F.COMPANY_CD AND A.COUNTERPARTY_CD=F.COUNTERPARTY_CD "
					+ "AND A.CONT_NO=F.CONT_NO AND A.AGMT_NO=F.AGMT_NO AND A.CONTRACT_TYPE=F.CONTRACT_TYPE AND E.PLANT_SEQ_NO=F.PLANT_SEQ AND D.PLANT_SEQ_NO=F.BU_SEQ AND F.CARGO_NO=B.CARGO_NO "
					+ "AND F.GAS_DT >=TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')+NVL(STORAGE_DAYS-1,0)+1 "
					+ "AND F.GAS_DT <=TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0) "
					+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL C "
						+ "WHERE F.CONT_NO=C.CONT_NO AND F.AGMT_NO=C.AGMT_NO AND F.COMPANY_CD=C.COMPANY_CD AND F.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
						+ "AND F.TRANSPORTER_CD=C.TRANSPORTER_CD AND F.TRANS_SEQ=C.TRANS_SEQ AND F.PLANT_SEQ=C.PLANT_SEQ AND F.CONTRACT_TYPE=C.CONTRACT_TYPE AND F.BU_SEQ=C.BU_SEQ "
						+ "AND F.GAS_DT=C.GAS_DT AND F.CARGO_NO=C.CARGO_NO)) > 0 ";*/
			
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "C");
			stmt.setString(3, "Y");
			stmt.setString(4, "Y");
			stmt.setString(5, "A");
			stmt.setString(6, "Y");
			stmt.setString(7, month+"/"+year);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String agmtno=rset.getString(3)==null?"0":rset.getString(3);
				String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
				String contno=rset.getString(5)==null?"0":rset.getString(5);
				String contrev=rset.getString(6)==null?"0":rset.getString(6);
				String cont_type = rset.getString(7)==null?"":rset.getString(7);
				String cargo_no = rset.getString(8)==null?"0":rset.getString(8);
				String contRef=rset.getString(9)==null?"":rset.getString(9);
				
				String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, cargo_no);
				String countpty_nm=utilBean.getCounterpartyName(conn,countpty_cd);
				String countpty_abbr=utilBean.getCounterpartyABBR(conn,countpty_cd);
				
				String bu_plant_seq = rset.getString(10)==null?"":rset.getString(10);
				String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
				
				String state_code = utilBean.getState_TIN(conn, own_cd, own_cd, "B", bu_plant_seq);
				
				String plant_seq=rset.getString(11)==null?"":rset.getString(11);
				String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "C");
				
				String actual_receipt_dt=rset.getString(12)==null?"":rset.getString(12);
				int storage_day=rset.getInt(13);
				int storage_ext_day=rset.getInt(14);
				
				String storage_start_dt=utilDate.getDate(actual_receipt_dt, ""+(storage_day+1));
				String storage_end_dt=utilDate.getDate(storage_start_dt, ""+(storage_ext_day-1));
				
				String inv_flag="ST";
				
				queryString1="SELECT TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY') "
						+ "FROM FMS_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=? AND INV_FLAG=? "
						+ "AND BU_UNIT=? AND PLANT_SEQ=? "
						+ "ORDER BY PERIOD_END_DT ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, own_cd);
				stmt1.setString(2, countpty_cd);
				stmt1.setString(3, contno);
				stmt1.setString(4, agmtno);
				stmt1.setString(5, cont_type);
				stmt1.setString(6, cargo_no);
				stmt1.setString(7, inv_flag);
				stmt1.setString(8, bu_plant_seq);
				stmt1.setString(9, plant_seq);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					String periodStart_dt=rset1.getString(1)==null?"":rset1.getString(1);
					String periodEnd_dt=rset1.getString(2)==null?"":rset1.getString(2);
					
					VBU_STATE_TIN.add(state_code);
					VCOUNTERPTY_CD.add(countpty_cd);
					VCOUNTERPTY_ABBR.add(countpty_abbr);
					VAGMT_NO.add(agmtno);
					VAGMT_REV_NO.add(agmtrev);
					VCONT_NO.add(contno);
					VCONT_REV_NO.add(contrev);
					VCARGO_NO.add(cargo_no);
					//VBOE_NO.add("");
					//VBOE_NM.add("");
					VCONT_REF_NO.add(contRef);
					VCONTRACT_TYPE.add(cont_type);
					VDEAL_NO.add(deal_no);
					VAGMT_BASE.add("");

					VPLANT_SEQ.add(plant_seq);
					VPLANT_ABBR.add(plant_abbr);
					VBU_PLANT_SEQ.add(bu_plant_seq);
					VBU_PLANT_ABBR.add(bu_plant_abbr);
					VPERIOD_START_DT.add(periodStart_dt);
					VPERIOD_END_DT.add(periodEnd_dt);
					VTEMP_PERIOD_START_DT.add(periodStart_dt);
					VTEMP_PERIOD_END_DT.add(periodEnd_dt);
					//VALLOC_QTY.add(nf.format(qtyMMBTU));
					
					//VSTATUS.add("");
					
					VINV_FLAG.add(inv_flag);
					getInvDetailForPreparationList(own_cd,countpty_cd, contno, agmtno,cont_type, plant_seq, 
							bu_plant_seq, "0", periodStart_dt, periodEnd_dt, state_code,cargo_no,inv_flag);
					
					VBILLING_FREQ_FLAG.add("0");
					VBILLING_FREQ_NM.add("");
				}
				rset1.close();
				stmt1.close();
				
				String max_invoiced_dt="";
				queryString2="SELECT TO_CHAR(MAX(PERIOD_END_DT),'DD/MM/YYYY') "
						+ "FROM FMS_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=? AND INV_FLAG=? ";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, own_cd);
				stmt2.setString(2, countpty_cd);
				stmt2.setString(3, contno);
				stmt2.setString(4, agmtno);
				stmt2.setString(5, cont_type);
				stmt2.setString(6, cargo_no);
				stmt2.setString(7, "ST");
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					max_invoiced_dt=rset2.getString(1)==null?"":rset2.getString(1);
				}
				rset2.close();
				stmt2.close();
				
				if(!max_invoiced_dt.equals(storage_end_dt))
				{
					if(!max_invoiced_dt.equals(""))
					{
						storage_start_dt=utilDate.getDate(max_invoiced_dt, "1");
					}
					
					//String sysdate=utilDate.getSysdate(); //INC#2600001
					String prvdate=utilDate.getPreviousDate();
					int c = utilDate.getDays(storage_start_dt, prvdate);
					if(c<=1)
					{
						int sysDays=utilDate.getDays(prvdate, storage_end_dt);
						
						String temp_periodStart_dt=storage_start_dt;
						String temp_periodEnd_dt=storage_end_dt;
						if(sysDays <= 0)
						{
							temp_periodEnd_dt=prvdate;
						}
							
						VBU_STATE_TIN.add(state_code);
						VCOUNTERPTY_CD.add(countpty_cd);
						VCOUNTERPTY_ABBR.add(countpty_abbr);
						VAGMT_NO.add(agmtno);
						VAGMT_REV_NO.add(agmtrev);
						VCONT_NO.add(contno);
						VCONT_REV_NO.add(contrev);
						VCARGO_NO.add(cargo_no);
						//VBOE_NO.add("");
						//VBOE_NM.add("");
						VCONT_REF_NO.add(contRef);
						VCONTRACT_TYPE.add(cont_type);
						VDEAL_NO.add(deal_no);
						VAGMT_BASE.add("");
		
						VPLANT_SEQ.add(plant_seq);
						VPLANT_ABBR.add(plant_abbr);
						VBU_PLANT_SEQ.add(bu_plant_seq);
						VBU_PLANT_ABBR.add(bu_plant_abbr);
						VPERIOD_START_DT.add(temp_periodStart_dt);
						VPERIOD_END_DT.add(temp_periodEnd_dt);
						VTEMP_PERIOD_START_DT.add(temp_periodStart_dt);
						VTEMP_PERIOD_END_DT.add(temp_periodEnd_dt);
						//VALLOC_QTY.add(nf.format(qtyMMBTU));
						
						//VSTATUS.add("");
						
						VINV_FLAG.add(inv_flag);
						getInvDetailForPreparationList(own_cd,countpty_cd, contno, agmtno,cont_type, plant_seq, 
								bu_plant_seq, "0", temp_periodStart_dt, temp_periodEnd_dt, state_code,cargo_no,inv_flag);
						
						VBILLING_FREQ_FLAG.add("0");
						VBILLING_FREQ_NM.add("");
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
	
	public void getCustomerListforCrDr()
	{
		String function_nm="getCustomerListforCrDr()";
		try
		{
			if(crdr_gen_type.equals("CRDR_IMB"))
			{
				queryString="SELECT DISTINCT COUNTERPARTY_CD "
						+ "FROM FMS_INVOICE_MST A "
						+ "WHERE COMPANY_CD=? "
						+ "AND TO_CHAR(INVOICE_DT,'MM/YYYY') = TO_CHAR(TO_DATE(?, 'MM/YYYY'), 'MM/YYYY') "
						+ "AND INVOICE_NO IS NOT NULL AND INV_FLAG=? "
						+ "ORDER BY COUNTERPARTY_CD ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, month+"/"+year);
				stmt.setString(3, "F");
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String cd = rset.getString(1)==null?"":rset.getString(1);
					VCOUNTERPARTY_CD.add(cd);
					VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn, cd));
					VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn, cd));
				}
				rset.close();
				stmt.close();
			}
			else
			{
				queryString="SELECT DISTINCT COUNTERPARTY_CD "
						+ "FROM FMS_INVOICE_MST A "
						+ "WHERE COMPANY_CD=? "
						+ "AND TO_CHAR(INVOICE_DT,'MM/YYYY') = TO_CHAR(TO_DATE(?, 'MM/YYYY'), 'MM/YYYY') "
						+ "AND INVOICE_NO IS NOT NULL AND INV_FLAG=? "
						+ "AND NVL((SELECT COUNT(*) FROM FMS_INVOICE_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND B.REF_NO=A.INVOICE_NO AND B.INV_FLAG IN (?,?)),0) = 0 "
						+ "ORDER BY COUNTERPARTY_CD ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, month+"/"+year);
				stmt.setString(3, "F");
				stmt.setString(4, "CR");
				stmt.setString(5, "DR");
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String cd = rset.getString(1)==null?"":rset.getString(1);
					VCOUNTERPARTY_CD.add(cd);
					VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn, cd));
					VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn, cd));
				}
				rset.close();
				stmt.close();
			}
			
			if(!VCOUNTERPARTY_CD.contains(counterparty_cd) && !counterparty_cd.equals(""))
			{
				VCOUNTERPARTY_CD.add(counterparty_cd);
				VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn, counterparty_cd));
				VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn, counterparty_cd));
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getInvoiceNoListforCrDr()
	{
		String function_nm="getInvoiceNoListforCrDr()";
		try
		{
			if(operation.equals("PREPARE"))
			{
				if(crdr_gen_type.equals("CRDR_IMB"))
				{
					queryString="SELECT INVOICE_NO "
							+ "FROM FMS_INVOICE_MST A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND TO_CHAR(INVOICE_DT,'MM/YYYY') = TO_CHAR(TO_DATE(?, 'MM/YYYY'), 'MM/YYYY') "
							+ "AND INVOICE_NO IS NOT NULL AND INV_FLAG=? AND CONTRACT_TYPE NOT IN ('Q','O')"
							+ "ORDER BY COUNTERPARTY_CD ";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, month+"/"+year);
					stmt.setString(4, "F");
					rset=stmt.executeQuery();
					while(rset.next())
					{
						VINVOICE_NO_LIST.add(rset.getString(1)==null?"":rset.getString(1));
					}
					rset.close();
					stmt.close();
				}
				else
				{
					queryString="SELECT INVOICE_NO "
							+ "FROM FMS_INVOICE_MST A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND TO_CHAR(INVOICE_DT,'MM/YYYY') = TO_CHAR(TO_DATE(?, 'MM/YYYY'), 'MM/YYYY') "
							+ "AND INVOICE_NO IS NOT NULL AND INV_FLAG=? "
							+ "AND NVL((SELECT COUNT(*) FROM FMS_INVOICE_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND B.REF_NO=A.INVOICE_NO AND B.INV_FLAG IN (?,?) "
							+ "AND CRITERIA NOT LIKE '%IMB%' AND CRITERIA NOT LIKE '%SHIP%' AND CRITERIA NOT LIKE '%UNAUTH%'),0) = 0 "
							+ "ORDER BY COUNTERPARTY_CD ";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, month+"/"+year);
					stmt.setString(4, "F");
					stmt.setString(5, "CR");
					stmt.setString(6, "DR");
					rset=stmt.executeQuery();
					while(rset.next())
					{
						VINVOICE_NO_LIST.add(rset.getString(1)==null?"":rset.getString(1));
					}
					rset.close();
					stmt.close();
				}
			}
			else
			{
				VINVOICE_NO_LIST.add(sel_inv_no);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCriteriaList()
	{
		String function_nm="getCriteriaList()";
		try
		{
			
			String inv_rais_in="";
			String trans_chrg="";
			String market_chrg="";
			String oth_chrg="";
			String priceCd="";
			String contType="";
			
			queryString="SELECT INVOICE_RAISED_IN,TRANSPORTATION_CHARGE,MARKET_MARGIN,OTHER_CHARGES,SALE_PRICE_UNIT,CONTRACT_TYPE "
					+ "FROM FMS_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND INVOICE_NO=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, sel_inv_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				inv_rais_in=rset.getString(1)==null?"":rset.getString(1);
				trans_chrg=rset.getString(2)==null?"":rset.getString(2);
				market_chrg=rset.getString(3)==null?"":rset.getString(3);
				oth_chrg=rset.getString(4)==null?"":rset.getString(4);
				priceCd=rset.getString(5)==null?"":rset.getString(5);
				contType=rset.getString(6)==null?"":rset.getString(6);
			}
			rset.close();
			stmt.close();
			
			if(crdr_gen_type.equals("CRDR_IMB"))
			{
				VCRITERIA_FLAG.add("IMB");
				VCRITERIA_NAME.add("Imbalance");
				VCRITERIA_HIDE.add((contType.equals("O") || contType.equals("Q"))?"Y":"N");
				
				VCRITERIA_FLAG.add("UNAUTH");
				VCRITERIA_NAME.add("Unauthorized Overrun");
				VCRITERIA_HIDE.add((contType.equals("O") || contType.equals("Q"))?"Y":"N");
				
				VCRITERIA_FLAG.add("SHIP");
				VCRITERIA_NAME.add("Ship or Pay Charges");
				VCRITERIA_HIDE.add((contType.equals("O") || contType.equals("Q"))?"Y":"N");
			}
			else
			{
				VCRITERIA_FLAG.add("QTY");
				VCRITERIA_NAME.add("Change in Quantity");
				VCRITERIA_HIDE.add("N");
				
				VCRITERIA_FLAG.add("PRICE");
				VCRITERIA_NAME.add("Change in Price");
				VCRITERIA_HIDE.add("N");
				
				VCRITERIA_FLAG.add("EXCHG");
				VCRITERIA_NAME.add("Change in Exchange Rate");
				VCRITERIA_HIDE.add(inv_rais_in.equals("1") && priceCd.equals("2")?"N":"Y");
				
				VCRITERIA_FLAG.add("TC");
				VCRITERIA_NAME.add("Change in Transportation Tariff");
				VCRITERIA_HIDE.add(trans_chrg.equals("")?"Y":"N");
				
				VCRITERIA_FLAG.add("MM");
				VCRITERIA_NAME.add("Change in Marketing Margin");
				VCRITERIA_HIDE.add(market_chrg.equals("")?"Y":"N");
				
				VCRITERIA_FLAG.add("OC");
				VCRITERIA_NAME.add("Change in Other Charges");
				VCRITERIA_HIDE.add(oth_chrg.equals("")?"Y":"N");
				
				VCRITERIA_FLAG.add("TAXP");
				VCRITERIA_NAME.add("Change in Tax %");
				VCRITERIA_HIDE.add("N");
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCommonInvoiceDetailforCrDr()
	{
		String function_nm="getCommonInvoiceDetailforCrDr()";
		try
		{
			couterpty_abbr=utilBean.getCounterpartyABBR(conn, counterparty_cd);
			couterpty_nm=utilBean.getCounterpartyName(conn, counterparty_cd);
			
			queryString="SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CARGO_NO,BU_UNIT,PLANT_SEQ,"
					+ "TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),SALE_PRICE_UNIT,INVOICE_RAISED_IN,"
					+ "EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),BU_CONTACT_PERSON_CD,CONTACT_PERSON_CD "
					+ "FROM FMS_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND INVOICE_NO=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, sel_inv_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				String agmt=rset.getString(1)==null?"":rset.getString(1);
				String agmt_rev=rset.getString(2)==null?"":rset.getString(2);
				String cont=rset.getString(3)==null?"":rset.getString(3);
				String cont_rev=rset.getString(4)==null?"":rset.getString(4);
				String cont_type=rset.getString(5)==null?"":rset.getString(5);
				String cargo=rset.getString(6)==null?"0":rset.getString(6);
				bu_unit=rset.getString(7)==null?"":rset.getString(7);
				plant_seq=rset.getString(8)==null?"":rset.getString(8);
				
				agmt_no=agmt; //
				agmt_rev_no=agmt_rev; //
				cont_no=cont; //
				cont_rev_no=cont_rev; //
				contract_type=cont_type; //
				cargo_no=cargo; //
				
				String contRef="";
				queryString1="SELECT CONT_REF_NO,TRADE_REF_NO,AGMT_BASE  "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND CONT_NO=? AND AGMT_NO=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "UNION ALL "
						+ "SELECT A.CONT_REF_NO,NULL,NULL "
						+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_CARGO_DTL B "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? "
						+ "AND A.AGMT_NO=? AND A.AGMT_TYPE=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND B.CARGO_NO=? "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
						+ ""
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, cont_type);
				stmt1.setString(3, cont);
				stmt1.setString(4, agmt);
				stmt1.setString(5, counterparty_cd);
				stmt1.setString(6, comp_cd);
				stmt1.setString(7, counterparty_cd);
				stmt1.setString(8, "C");
				stmt1.setString(9, agmt);
				stmt1.setString(10, "A");
				stmt1.setString(11, cont);
				stmt1.setString(12, cont_type);
				stmt1.setString(13, cargo);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					contRef=rset1.getString(1)==null?"":rset1.getString(1);
					String tradeRef=rset1.getString(2)==null?"":rset1.getString(2);
					if(cont_type.equals("X"))
					{
						contRef=tradeRef;
					}
					agmt_base=rset1.getString(3)==null?"":rset1.getString(3);
					
					contract_ref=contRef;
				}
				rset1.close();
				stmt1.close();
				
				deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, cargo)+" ["+contRef+"]"; //
				
				bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn, comp_cd, comp_cd, bu_unit, "B"); //
				plant_abbr=utilBean.getCounterpartyPlantABBR(conn, counterparty_cd, comp_cd, plant_seq, "C"); //
				period_start_dt=rset.getString(9)==null?"":rset.getString(9); //
				period_end_dt=rset.getString(10)==null?"":rset.getString(10); //
				price_cd=rset.getString(11)==null?"":rset.getString(11);//
				price_cd_nm=utilBean.getRateUnitNm(conn, price_cd);
				invoice_raised_in=rset.getString(12)==null?"":rset.getString(12);//
				invoice_raised_in_nm=utilBean.getRateUnitNm(conn, invoice_raised_in);//
				exchng_rate_cd=rset.getString(13)==null?"":rset.getString(13);//
				exchang_rate_dt=rset.getString(14)==null?"":rset.getString(14);//
				bu_contact_person_cd=rset.getString(15)==null?"0":rset.getString(15);//
				contact_person_cd=rset.getString(16)==null?"0":rset.getString(16);//
				
				if(crdr_gen_type.equals("CRDR_IMB"))
				{
					price_cd="1";
					price_cd_nm=utilBean.getRateUnitNm(conn, price_cd);
					
					invoice_raised_in="1";
					invoice_raised_in_nm=utilBean.getRateUnitNm(conn, invoice_raised_in);//
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
	
	public void getInvoiceDetailforCrDr()
	{
		String function_nm="getInvoiceDetailforCrDr()";
		try
		{
			
			String inv_rais_in="";
			String trans_chrg="";
			String market_chrg="";
			String oth_chrg="";
			
			queryString="SELECT BU_CONTACT_PERSON_CD,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'),"
					+ "ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),EXCHG_RATE_VALUE,"
					+ "INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),INVOICE_AMT,NET_PAYABLE_AMT,"
					+ "REMARK_1,REMARK_2,TCS_TDS,TCS_AMT,TCS_FACTOR,TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,TCS_STRUCT_CD,TO_CHAR(TCS_EFF_DT,'DD/MM/YYYY'),"
					+ "TDS_GROSS_AMT,TDS_GROSS_PERCENT,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),MARKET_MARGIN,MARKET_MARGIN_AMT,OTHER_CHARGES,OTHER_CHARGES_AMT,"
					+ "TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(APPROVED_DT,'DD/MM/YYYY HH24:MI:SS'),FINANCIAL_YEAR,SUG_QTY,SUG_PERCENT,"
					+ "AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,PLANT_SEQ,"
					+ "TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),BU_STATE_TIN,CARGO_NO "
					+ "FROM FMS_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND INVOICE_NO=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, sel_inv_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{	
				//bu_contact_person_cd=rset.getString(1)==null?"0":rset.getString(1);//
				//contact_person_cd=rset.getString(2)==null?"0":rset.getString(2);//
				//invoice_seq=rset.getString(3)==null?"":rset.getString(3);
				String invoice_seq=rset.getString(3)==null?"":rset.getString(3);
				//invoice_no=rset.getString(4)==null?"":rset.getString(4);//
				main_invoice_dt=rset.getString(5)==null?"":rset.getString(5);
				main_invoice_due_dt=rset.getString(6)==null?"":rset.getString(6);
				
				//exchng_rate_cd=rset.getString(11)==null?"":rset.getString(11);//
				//exchang_rate_dt=rset.getString(12)==null?"":rset.getString(12);//
				main_exchang_rate=rset.getString(13)==null?"":nf2.format(rset.getDouble(13));
			
				main_qty_mmbtu=rset.getString(7)==null?"":nf.format(rset.getDouble(7));
				main_price=rset.getString(8)==null?"":nf.format(rset.getDouble(8));
				//price_cd=rset.getString(9)==null?"":rset.getString(9);//
				//price_cd_nm=utilBean.getRateUnitNm(conn, price_cd);
				if(!main_price.equals(""))
				{
					main_price=utilBean.RateNumberFormat(rset.getDouble(8), price_cd);
				}
				main_gross_amt=rset.getString(10)==null?"":nf.format(rset.getDouble(10));
				
				//invoice_raised_in=rset.getString(14)==null?"":rset.getString(14);//
				//invoice_raised_in_nm=utilBean.getRateUnitNm(conn, invoice_raised_in);//
				main_gross_amt1=rset.getString(15)==null?"":nf.format(rset.getDouble(15));
				main_tax_amt=rset.getString(16)==null?"":nf.format(rset.getDouble(16));
				main_tax_struct_cd=rset.getString(17)==null?"":rset.getString(17);
				tax_struct_cd=rset.getString(17)==null?"":rset.getString(17);
				new_tax_struct_cd=rset.getString(17)==null?"":rset.getString(17);
				main_tax_struct_dt=rset.getString(18)==null?"":rset.getString(18);
				main_invoice_amt=rset.getString(19)==null?"":nf.format(rset.getDouble(19));
				main_net_payable=rset.getString(20)==null?"":nf.format(rset.getDouble(20));
				
				applicable_abbr=rset.getString(23)==null?"":rset.getString(23);//
				main_applicable_amt=rset.getString(24)==null?"":rset.getString(24);
				main_TCS_factor=rset.getString(25)==null?"":rset.getString(25);
				
				main_tax_struct_dtl=utilBean.getTaxDescr(conn, main_tax_struct_cd);
				
				main_transportation_charges=rset.getString(26)==null?"":nf2.format(rset.getDouble(26));
				main_transportation_amount=rset.getString(27)==null?"":nf.format(rset.getDouble(27));
				
				main_tcs_struct_cd=rset.getString(28)==null?"":rset.getString(28);
				main_tcs_struct_dt=rset.getString(29)==null?"":rset.getString(29);
				
				main_tds_amt=rset.getString(30)==null?"":nf.format(rset.getDouble(30));
				main_tds_factor=rset.getString(31)==null?"":nf.format(rset.getDouble(31));
				main_tds_struct_cd=rset.getString(32)==null?"":rset.getString(32);
				main_tds_struct_dt=rset.getString(33)==null?"":rset.getString(33);
				
				main_marketing_margin=rset.getString(34)==null?"":nf2.format(rset.getDouble(34));
				main_marketing_margin_amount=rset.getString(35)==null?"":nf.format(rset.getDouble(35));
				main_other_charges=rset.getString(36)==null?"":nf2.format(rset.getDouble(36));
				main_other_charges_amount=rset.getString(37)==null?"":nf.format(rset.getDouble(37));
				//inv_entered_on = rset.getString(38)==null?"":rset.getString(38); //
				//inv_approved_on = rset.getString(39)==null?"":rset.getString(39);//
				
				String fiscal_yr=rset.getString(40)==null?"":rset.getString(40);
				
				main_gross_include_transport_tariff=nf.format(Double.parseDouble(main_gross_amt1));
				if(!main_transportation_amount.equals(""))
				{
					main_gross_include_transport_tariff=nf.format(Double.parseDouble(main_gross_include_transport_tariff) + Double.parseDouble(main_transportation_amount));
					isGrossIncTriff=true;
				}
				if(!main_marketing_margin_amount.equals(""))
				{
					main_gross_include_transport_tariff=nf.format(Double.parseDouble(main_gross_include_transport_tariff) + Double.parseDouble(main_marketing_margin_amount));
					isGrossIncTriff=true;
				}
				if(!main_other_charges_amount.equals(""))
				{
					main_gross_include_transport_tariff=nf.format(Double.parseDouble(main_gross_include_transport_tariff) + Double.parseDouble(main_other_charges_amount));
					isGrossIncTriff=true;
				}
				
				/*if(inv_flag.equals("UG"))
				{
					qty_mmbtu=rset.getString(7)==null?"":nf.format(rset.getDouble(7));
					price=rset.getString(8)==null?"":nf.format(rset.getDouble(8));
					price_cd=rset.getString(9)==null?"":rset.getString(9);
					if(!price.equals(""))
					{
						price=utilBean.RateNumberFormat(rset.getDouble(8), price_cd);
					}
					
					sug_qty=rset.getString(41)==null?"":nf.format(rset.getDouble(41));
					sug_percentage=rset.getString(42)==null?"":nf.format(rset.getDouble(42));
					
				}*/
				
				bu_state_tin =rset.getString(52)==null?"":rset.getString(52); //
				
				//remark_1=rset.getString(21)==null?"":rset.getString(21);
				//remark_2=rset.getString(22)==null?"":rset.getString(22);
				
				Vector VTAX_CODE = new Vector();
				Vector VTAX_DESCR = new Vector();
				Vector VTAX_AMT = new Vector();
				Vector VTAX_BASE_AMT = new Vector();
				Vector VTAX_FACTOR = new Vector();
				
				Vector VTEMP_TAX_CODE = new Vector();
				Vector VTEMP_TAX_DESCR = new Vector();
				Vector VTEMP_TAX_AMT = new Vector();
				Vector VTEMP_TAX_BASE_AMT = new Vector();
				Vector VTEMP_TAX_FACTOR = new Vector();
				queryString1="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
						+ "FROM FMS_INV_TAX_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
						+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, bu_state_tin);
				//stmt1.setString(3, financial_year);
				stmt1.setString(3, fiscal_yr);
				stmt1.setString(4, invoice_seq);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					VTAX_CODE.add(rset1.getString(1)==null?"":rset1.getString(1));
					VTAX_DESCR.add(rset1.getString(2)==null?"":rset1.getString(2));
					VTAX_AMT.add(rset1.getString(3)==null?"":nf.format(rset1.getDouble(3)));
					VTAX_BASE_AMT.add(rset1.getString(4)==null?"":nf.format(rset1.getDouble(4)));
					VTAX_FACTOR.add("");
					
					VTEMP_TAX_CODE.add(rset1.getString(1)==null?"":rset1.getString(1));
					VTEMP_TAX_DESCR.add(rset1.getString(2)==null?"":rset1.getString(2));
					VTEMP_TAX_AMT.add("");
					VTEMP_TAX_BASE_AMT.add("");
					VTEMP_TAX_FACTOR.add("");
				}
				rset1.close();
				stmt1.close();
				
				Vector VTEMP_TAX_DTL = new Vector();
				
				VTEMP_TAX_DTL.add(VTAX_CODE);
				VTEMP_TAX_DTL.add(VTAX_DESCR);
				VTEMP_TAX_DTL.add(VTAX_AMT);
				VTEMP_TAX_DTL.add(VTAX_BASE_AMT);
				VTEMP_TAX_DTL.add(VTAX_FACTOR);
				
				Vector VTEMP_TAX_DTL_1 = new Vector();
				
				VTEMP_TAX_DTL_1.add(VTEMP_TAX_CODE);
				VTEMP_TAX_DTL_1.add(VTEMP_TAX_DESCR);
				VTEMP_TAX_DTL_1.add(VTEMP_TAX_AMT);
				VTEMP_TAX_DTL_1.add(VTEMP_TAX_BASE_AMT);
				VTEMP_TAX_DTL_1.add(VTEMP_TAX_FACTOR);
				
				VMAIN_MULTI_TAX_STRUCT.add(VTEMP_TAX_DTL);
				
				if(operation.equals("PREPARE"))
				{
					VNEW_MULTI_TAX_STRUCT.add(VTEMP_TAX_DTL_1);
					VMULTI_TAX_STRUCT.add(VTEMP_TAX_DTL_1);
				}
				
				queryString1="SELECT TO_CHAR(STORAGE_DT,'DD/MM/YYYY'),OPEN_BALANCE_QTY,OFFTAKE_QTY,RATE,RATE_TYPE,DAY_DISCOUNT "
						+ "FROM FMS_INV_STORAGE_CRG_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
						+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=?";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, bu_state_tin);
				//stmt1.setString(3, financial_year);
				stmt1.setString(3, fiscal_yr);
				stmt1.setString(4, invoice_seq);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					String storageInv=rset1.getString(2)==null?"":nf.format(rset1.getDouble(2));
					VSTORAGE_DATE.add(rset1.getString(1)==null?"":rset1.getString(1));
					VSTORAGE_INVENTORY.add(storageInv);
					VOFFTAKE_QTY.add(rset1.getString(3)==null?"":nf.format(rset1.getDouble(3)));
					
					String rate=rset1.getString(4)==null?"":utilBean.RateNumberFormat(rset1.getDouble(4),price_cd);
					String rate_type=rset1.getString(5)==null?"":rset1.getString(5);
					String discount_day=rset1.getString(6)==null?"":rset1.getString(6);
					VRATE_TYPE.add(rate_type);
					VUSER_DEFINE.add(rate_type.equals("U")?rate:"");
					VSTORAGE_CHARGE.add(rate_type.equals("U")?"":rate);
					
					String storage_amt="";
					if(!storageInv.equals("") && !rate.equals("") && discount_day.equals("N"))
					{
						storage_amt=nf.format(Double.parseDouble(storageInv) * Double.parseDouble(rate));
					}
					
					VSTORAGE_AMT.add(storage_amt);
					VDISCOUNT_FLAG.add(discount_day);
				}
				rset1.close();
				stmt1.close();
				
				queryString1="SELECT DISTINCT SEC_INT_REF "
						+ "FROM FMS_INV_ADV_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
						+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? "
						+ "ORDER BY SEC_INT_REF";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, bu_state_tin);
				//stmt1.setString(3, financial_year);
				stmt1.setString(3, fiscal_yr);
				stmt1.setString(4, invoice_seq);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					VEXISTING_RECEIPT_VOUCHER.add(rset1.getString(1)==null?"":rset1.getString(1));
					if(operation.equals("INSERT") && submission_chk)
					{
						VRECEIPT_VOUCHER_MST.add(rset1.getString(1)==null?"":rset1.getString(1));
					}
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
	
	public void getCrDrInvoiceDetail()
	{
		String function_nm="getCrDrInvoiceDetail()";
		try
		{
			
			String inv_rais_in="";
			String trans_chrg="";
			String market_chrg="";
			String oth_chrg="";
			
			queryString="SELECT BU_CONTACT_PERSON_CD,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'),"
					+ "ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),EXCHG_RATE_VALUE,"
					+ "INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),INVOICE_AMT,NET_PAYABLE_AMT,"
					+ "REMARK_1,REMARK_2,TCS_TDS,TCS_AMT,TCS_FACTOR,TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,TCS_STRUCT_CD,TO_CHAR(TCS_EFF_DT,'DD/MM/YYYY'),"
					+ "TDS_GROSS_AMT,TDS_GROSS_PERCENT,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),MARKET_MARGIN,MARKET_MARGIN_AMT,OTHER_CHARGES,OTHER_CHARGES_AMT,"
					+ "TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(APPROVED_DT,'DD/MM/YYYY HH24:MI:SS'),FINANCIAL_YEAR,SUG_QTY,SUG_PERCENT,"
					+ "AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,PLANT_SEQ,"
					+ "TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),BU_STATE_TIN,CARGO_NO,CRITERIA,"
					+ "IMB_AMT,IMB_QTY,SHIPAY_AMT,SHIPAY_QTY,OVRUN_AMT,OVRUN_QTY "
					+ "FROM FMS_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND BU_STATE_TIN=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, invoice_seq);
			stmt.setString(4, financial_year);
			stmt.setString(5, bu_state_tin);
			rset=stmt.executeQuery();
			if(rset.next())
			{	
				//bu_contact_person_cd=rset.getString(1)==null?"0":rset.getString(1);
				//contact_person_cd=rset.getString(2)==null?"0":rset.getString(2);
				//invoice_seq=rset.getString(3)==null?"":rset.getString(3);
				//invoice_no=rset.getString(4)==null?"":rset.getString(4);
				invoice_dt=rset.getString(5)==null?"":rset.getString(5);
				invoice_due_dt=rset.getString(6)==null?"":rset.getString(6);
				
				//exchng_rate_cd=rset.getString(11)==null?"":rset.getString(11);
				//exchang_rate_dt=rset.getString(12)==null?"":rset.getString(12);
				exchang_rate=rset.getString(13)==null?"":nf2.format(rset.getDouble(13));
			
				qty_mmbtu=rset.getString(7)==null?"":nf.format(rset.getDouble(7));
				price=rset.getString(8)==null?"":nf.format(rset.getDouble(8));
				//price_cd=rset.getString(9)==null?"":rset.getString(9);
				//price_cd_nm=utilBean.getRateUnitNm(conn, price_cd);
				if(!price.equals(""))
				{
					price=utilBean.RateNumberFormat(rset.getDouble(8), price_cd);
				}
				gross_amt=rset.getString(10)==null?"":nf.format(rset.getDouble(10));
				
				//invoice_raised_in=rset.getString(14)==null?"":rset.getString(14);
				//invoice_raised_in_nm=utilBean.getRateUnitNm(conn, invoice_raised_in);
				gross_amt1=rset.getString(15)==null?"":nf.format(rset.getDouble(15));
				tax_amt=rset.getString(16)==null?"":nf.format(rset.getDouble(16));
				tax_struct_cd=rset.getString(17)==null?"":rset.getString(17);
				tax_struct_dt=rset.getString(18)==null?"":rset.getString(18);
				invoice_amt=rset.getString(19)==null?"":nf.format(rset.getDouble(19));
				net_payable=rset.getString(20)==null?"":nf.format(rset.getDouble(20));
				
				applicable_abbr=rset.getString(23)==null?"":rset.getString(23);
				applicable_amt=rset.getString(24)==null?"":rset.getString(24);
				TCS_factor=rset.getString(25)==null?"":rset.getString(25);
				
				tax_struct_dtl=utilBean.getTaxDescr(conn, tax_struct_cd);
				
				transportation_charges=rset.getString(26)==null?"":nf2.format(rset.getDouble(26));
				transportation_amount=rset.getString(27)==null?"":nf.format(rset.getDouble(27));
				
				tcs_struct_cd=rset.getString(28)==null?"":rset.getString(28);
				tcs_struct_dt=rset.getString(29)==null?"":rset.getString(29);
				
				tds_amt=rset.getString(30)==null?"":nf.format(rset.getDouble(30));
				tds_factor=rset.getString(31)==null?"":nf.format(rset.getDouble(31));
				tds_struct_cd=rset.getString(32)==null?"":rset.getString(32);
				tds_struct_dt=rset.getString(33)==null?"":rset.getString(33);
				
				marketing_margin=rset.getString(34)==null?"":nf2.format(rset.getDouble(34));
				marketing_margin_amount=rset.getString(35)==null?"":nf.format(rset.getDouble(35));
				other_charges=rset.getString(36)==null?"":nf2.format(rset.getDouble(36));
				other_charges_amount=rset.getString(37)==null?"":nf.format(rset.getDouble(37));
				//inv_entered_on = rset.getString(38)==null?"":rset.getString(38);
				//inv_approved_on = rset.getString(39)==null?"":rset.getString(39);
				
				String fiscal_yr=rset.getString(40)==null?"":rset.getString(40);
				
				gross_include_transport_tariff=nf.format(Double.parseDouble(gross_amt1));
				if(!transportation_amount.equals(""))
				{
					gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(transportation_amount));
					isGrossIncTriff=true;
				}
				if(!marketing_margin_amount.equals(""))
				{
					gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(marketing_margin_amount));
					isGrossIncTriff=true;
				}
				if(!other_charges_amount.equals(""))
				{
					gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(other_charges_amount));
					isGrossIncTriff=true;
				}
				
				/*if(inv_flag.equals("UG"))
				{
					qty_mmbtu=rset.getString(7)==null?"":nf.format(rset.getDouble(7));
					price=rset.getString(8)==null?"":nf.format(rset.getDouble(8));
					price_cd=rset.getString(9)==null?"":rset.getString(9);
					if(!price.equals(""))
					{
						price=utilBean.RateNumberFormat(rset.getDouble(8), price_cd);
					}
					
					sug_qty=rset.getString(41)==null?"":nf.format(rset.getDouble(41));
					sug_percentage=rset.getString(42)==null?"":nf.format(rset.getDouble(42));
					
				}*/
				
				bu_state_tin =rset.getString(52)==null?"":rset.getString(52);
				criteri_formula =rset.getString(54)==null?"":rset.getString(54);
				
				imb_amt=rset.getString(55)==null?"":nf.format(rset.getDouble(55));
				imb_qty=rset.getString(56)==null?"":nf.format(rset.getDouble(56));
				ship_or_pay_amt=rset.getString(57)==null?"":nf.format(rset.getDouble(57));
				ship_or_pay_qty=rset.getString(58)==null?"":nf.format(rset.getDouble(58));
				ovrun_amt=rset.getString(59)==null?"":nf.format(rset.getDouble(59));
				ovrun_qty=rset.getString(60)==null?"":nf.format(rset.getDouble(60));
				
				remark_1=rset.getString(21)==null?"":rset.getString(21);
				//remark_2=rset.getString(22)==null?"":rset.getString(22);
				
				Vector VTAX_CODE = new Vector();
				Vector VTAX_DESCR = new Vector();
				Vector VTAX_AMT = new Vector();
				Vector VTAX_BASE_AMT = new Vector();
				Vector VTAX_FACTOR = new Vector();
				queryString1="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
						+ "FROM FMS_INV_TAX_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
						+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, bu_state_tin);
				//stmt1.setString(3, financial_year);
				stmt1.setString(3, fiscal_yr);
				stmt1.setString(4, invoice_seq);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					VTAX_CODE.add(rset1.getString(1)==null?"":rset1.getString(1));
					VTAX_DESCR.add(rset1.getString(2)==null?"":rset1.getString(2));
					VTAX_AMT.add(rset1.getString(3)==null?"":nf.format(rset1.getDouble(3)));
					VTAX_BASE_AMT.add(rset1.getString(4)==null?"":nf.format(rset1.getDouble(4)));
					VTAX_FACTOR.add("");
				}
				rset1.close();
				stmt1.close();
				
				Vector VTEMP_TAX_DTL = new Vector();
				
				VTEMP_TAX_DTL.add(VTAX_CODE);
				VTEMP_TAX_DTL.add(VTAX_DESCR);
				VTEMP_TAX_DTL.add(VTAX_AMT);
				VTEMP_TAX_DTL.add(VTAX_BASE_AMT);
				VTEMP_TAX_DTL.add(VTAX_FACTOR);
				
				VMULTI_TAX_STRUCT.add(VTEMP_TAX_DTL);
				
				queryString1="SELECT TO_CHAR(STORAGE_DT,'DD/MM/YYYY'),OPEN_BALANCE_QTY,OFFTAKE_QTY,RATE,RATE_TYPE,DAY_DISCOUNT "
						+ "FROM FMS_INV_STORAGE_CRG_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
						+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=?";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, bu_state_tin);
				//stmt1.setString(3, financial_year);
				stmt1.setString(3, fiscal_yr);
				stmt1.setString(4, invoice_seq);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					String storageInv=rset1.getString(2)==null?"":nf.format(rset1.getDouble(2));
					VSTORAGE_DATE.add(rset1.getString(1)==null?"":rset1.getString(1));
					VSTORAGE_INVENTORY.add(storageInv);
					VOFFTAKE_QTY.add(rset1.getString(3)==null?"":nf.format(rset1.getDouble(3)));
					
					String rate=rset1.getString(4)==null?"":utilBean.RateNumberFormat(rset1.getDouble(4),price_cd);
					String rate_type=rset1.getString(5)==null?"":rset1.getString(5);
					String discount_day=rset1.getString(6)==null?"":rset1.getString(6);
					VRATE_TYPE.add(rate_type);
					VUSER_DEFINE.add(rate_type.equals("U")?rate:"");
					VSTORAGE_CHARGE.add(rate_type.equals("U")?"":rate);
					
					String storage_amt="";
					if(!storageInv.equals("") && !rate.equals("") && discount_day.equals("N"))
					{
						storage_amt=nf.format(Double.parseDouble(storageInv) * Double.parseDouble(rate));
					}
					
					VSTORAGE_AMT.add(storage_amt);
					VDISCOUNT_FLAG.add(discount_day);
				}
				rset1.close();
				stmt1.close();
				
				queryString1="SELECT DISTINCT SEC_INT_REF "
						+ "FROM FMS_INV_ADV_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
						+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? "
						+ "ORDER BY SEC_INT_REF";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, bu_state_tin);
				//stmt1.setString(3, financial_year);
				stmt1.setString(3, fiscal_yr);
				stmt1.setString(4, invoice_seq);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					VEXISTING_RECEIPT_VOUCHER.add(rset1.getString(1)==null?"":rset1.getString(1));
					if(operation.equals("INSERT") && submission_chk)
					{
						VRECEIPT_VOUCHER_MST.add(rset1.getString(1)==null?"":rset1.getString(1));
					}
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
	
	public void getNewCrDrInvoiceDetail()
	{
		String function_nm="getNewCrDrInvoiceDetail()";
		try
		{
			if(criteri_formula.contains("QTY"))
			{
				if(agmt_base.equals("D"))
				{
					changed_qty_mmbtu=nf.format(utilAlloc.getDeliverdSupplyAllocationQty(conn, comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, plant_seq, bu_unit, period_start_dt, period_end_dt));
				}
				else
				{
					changed_qty_mmbtu=nf.format(utilAlloc.getSupplyAllocationQty(conn, comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, plant_seq, bu_unit, period_start_dt, period_end_dt,cargo_no));
				}
			}
			
			queryString="SELECT ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,EXCHG_RATE_DT,EXCHG_RATE_VALUE,"
					+ "INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,INVOICE_AMT,NET_PAYABLE_AMT,"
					+ "TCS_TDS,TCS_AMT,TCS_FACTOR,TDS_GROSS_AMT,TDS_GROSS_PERCENT,"
					+ "TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,MARKET_MARGIN,MARKET_MARGIN_AMT,OTHER_CHARGES,OTHER_CHARGES_AMT "
					+ "FROM FMS_INV_CRDR_REF "
					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? AND INVOICE_SEQ=? AND FINANCIAL_YEAR=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, bu_state_tin);
			stmt.setString(3, invoice_seq);
			stmt.setString(4, financial_year);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				if(criteri_formula.contains("QTY") && operation.equals("MODIFY"))
				{
					new_qty_mmbtu=changed_qty_mmbtu;
				}
				else
				{
					new_qty_mmbtu=rset.getString(1)==null?"":nf.format(rset.getDouble(1));
				}
				new_price=rset.getString(2)==null?"":nf.format(rset.getDouble(2));
				if(!new_price.equals(""))
				{
					new_price=utilBean.RateNumberFormat(rset.getDouble(2), price_cd);
				}
				new_gross_amt=rset.getString(4)==null?"":nf.format(rset.getDouble(4));
				new_exchang_rate=rset.getString(7)==null?"":nf2.format(rset.getDouble(7));
				new_gross_amt1=rset.getString(9)==null?"":nf.format(rset.getDouble(9));
				new_tax_amt=rset.getString(10)==null?"":nf.format(rset.getDouble(10));
				new_tax_struct_cd=rset.getString(11)==null?"":rset.getString(11);
				new_tax_struct_dtl=utilBean.getTaxDescr(conn, new_tax_struct_cd);
				new_tax_struct_dt="";
				new_invoice_amt=rset.getString(12)==null?"":nf.format(rset.getDouble(12));
				new_net_payable=rset.getString(13)==null?"":nf.format(rset.getDouble(13));
				
				//applicable_abbr=rset.getString(14)==null?"":rset.getString(14);
				new_applicable_amt=rset.getString(15)==null?"":rset.getString(15);
				new_TCS_factor=rset.getString(16)==null?"":rset.getString(16);
				new_tds_amt=rset.getString(17)==null?"":nf.format(rset.getDouble(17));
				new_tds_factor=rset.getString(18)==null?"":nf.format(rset.getDouble(18));
				
				new_transportation_charges=rset.getString(19)==null?"":nf2.format(rset.getDouble(19));
				new_transportation_amount=rset.getString(20)==null?"":nf.format(rset.getDouble(20));
				new_marketing_margin=rset.getString(21)==null?"":nf2.format(rset.getDouble(21));
				new_marketing_margin_amount=rset.getString(22)==null?"":nf.format(rset.getDouble(22));
				new_other_charges=rset.getString(23)==null?"":nf2.format(rset.getDouble(23));
				new_other_charges_amount=rset.getString(24)==null?"":nf.format(rset.getDouble(24));
				
				new_gross_include_transport_tariff=nf.format(Double.parseDouble(new_gross_amt1));
				if(!new_transportation_amount.equals(""))
				{
					new_gross_include_transport_tariff=nf.format(Double.parseDouble(new_gross_include_transport_tariff) + Double.parseDouble(new_transportation_amount));
					isGrossIncTriff=true;
				}
				if(!new_marketing_margin_amount.equals(""))
				{
					new_gross_include_transport_tariff=nf.format(Double.parseDouble(new_gross_include_transport_tariff) + Double.parseDouble(new_marketing_margin_amount));
					isGrossIncTriff=true;
				}
				if(!new_other_charges_amount.equals(""))
				{
					new_gross_include_transport_tariff=nf.format(Double.parseDouble(new_gross_include_transport_tariff) + Double.parseDouble(new_other_charges_amount));
					isGrossIncTriff=true;
				}
				
				Vector VTAX_CODE = new Vector();
				Vector VTAX_DESCR = new Vector();
				Vector VTAX_AMT = new Vector();
				Vector VTAX_BASE_AMT = new Vector();
				Vector VTAX_FACTOR = new Vector();
				queryString1="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
						+ "FROM FMS_INV_CRDR_TAX_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
						+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, bu_state_tin);
				stmt1.setString(3, financial_year);
				stmt1.setString(4, invoice_seq);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					VTAX_CODE.add(rset1.getString(1)==null?"":rset1.getString(1));
					VTAX_DESCR.add(rset1.getString(2)==null?"":rset1.getString(2));
					VTAX_AMT.add(rset1.getString(3)==null?"":nf.format(rset1.getDouble(3)));
					VTAX_BASE_AMT.add(rset1.getString(4)==null?"":nf.format(rset1.getDouble(4)));
					VTAX_FACTOR.add("");
				}
				rset1.close();
				stmt1.close();
				
				Vector VTEMP_TAX_DTL = new Vector();
				
				VTEMP_TAX_DTL.add(VTAX_CODE);
				VTEMP_TAX_DTL.add(VTAX_DESCR);
				VTEMP_TAX_DTL.add(VTAX_AMT);
				VTEMP_TAX_DTL.add(VTAX_BASE_AMT);
				VTEMP_TAX_DTL.add(VTAX_FACTOR);
				
				VNEW_MULTI_TAX_STRUCT.add(VTEMP_TAX_DTL);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCreditDebitNoteList(String contType)
	{
		String function_nm="getCreditDebitNoteList()";
		try
		{			
			String temp_period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
			String temp_period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
			
			queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CARGO_NO,BU_UNIT,PLANT_SEQ,"
					+ "BU_STATE_TIN,FINANCIAL_YEAR,INVOICE_SEQ,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),"
					+ "INVOICE_NO,REF_NO,INV_FLAG,CHECKED_FLAG,APPROVED_FLAG,PDF_INV_DTL,PRINT_BY_ORI,PRINT_BY_TRI,PRINT_BY_DUP,SAP_APPROVAL,"
					+ "TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),CRITERIA "
					+ "FROM FMS_INVOICE_MST A "
					+ "WHERE COMPANY_CD=? AND INVOICE_DT >= TO_DATE(?,'DD/MM/YYYY') AND INVOICE_DT <= TO_DATE(?,'DD/MM/YYYY') "
					+ "AND CONTRACT_TYPE IN ("+contType+") AND INV_FLAG IN ('CR','DR') "
					+ "ORDER BY (SELECT Z.COUNTERPARTY_ABBR FROM FMS_COUNTERPARTY_MST Z WHERE A.COUNTERPARTY_CD=Z.COUNTERPARTY_CD AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST Y WHERE Y.COUNTERPARTY_CD=Z.COUNTERPARTY_CD))";
			int st_count=0;
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++st_count, comp_cd);
			stmt.setString(++st_count, temp_period_start_dt);
			stmt.setString(++st_count, temp_period_end_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{	
				inv_index++;
				
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String agmt=rset.getString(3)==null?"":rset.getString(3);
				String agmt_rev=rset.getString(4)==null?"":rset.getString(4);
				String cont=rset.getString(5)==null?"":rset.getString(5);
				String cont_rev=rset.getString(6)==null?"":rset.getString(6);
				String cont_type=rset.getString(7)==null?"0":rset.getString(7);
				String cargo_no=rset.getString(8)==null?"":rset.getString(8);
				
				String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, cargo_no);
				
				String bu_plant_seq=rset.getString(9)==null?"":rset.getString(9);
				String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
				String plant_seq=rset.getString(10)==null?"":rset.getString(10);
				String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "C");
				String buStateTin=rset.getString(11)==null?"":rset.getString(11);
				String financial_year=rset.getString(12)==null?"":rset.getString(12);
				String inv_seq=rset.getString(13)==null?"":rset.getString(13);
				String temp_st_dt=rset.getString(14)==null?"":rset.getString(14);
				String temp_end_dt=rset.getString(15)==null?"":rset.getString(15);
				String inv_no=rset.getString(16)==null?"":rset.getString(16);
				String ref_no=rset.getString(17)==null?"":rset.getString(17);
				String invType=rset.getString(18)==null?"":rset.getString(18);
				String checked_flag=rset.getString(19)==null?"":rset.getString(19);
				String approved_flag=rset.getString(20)==null?"":rset.getString(20);
				String pdf_flg=rset.getString(21)==null?"":rset.getString(21);
				String pdf_ori=rset.getString(22)==null?"":rset.getString(22);
				String pdf_tri=rset.getString(23)==null?"":rset.getString(23);
				String pdf_dup=rset.getString(24)==null?"":rset.getString(24);
				String sap_approved_flag=rset.getString(25)==null?"":rset.getString(25);
				String inv_dt=rset.getString(26)==null?"":rset.getString(26);
				String criteria=rset.getString(27)==null?"":rset.getString(27);
				criteria=criteria.replace("#", "<br>");
				
				VCOUNTERPTY_CD.add(countpty_cd);
				VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VCOUNTERPTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				VAGMT_NO.add(agmt);
				VAGMT_REV_NO.add(agmt_rev);
				VCONT_NO.add(cont);
				VCONT_REV_NO.add(cont_rev);
				VCARGO_NO.add(cargo_no);
				VCONTRACT_TYPE.add(cont_type);
				VDEAL_NO.add(deal_no);
				VPLANT_SEQ.add(plant_seq);
				VPLANT_ABBR.add(plant_abbr);
				VBU_PLANT_SEQ.add(bu_plant_seq);
				VBU_PLANT_ABBR.add(bu_plant_abbr);
				VBU_STATE_TIN.add(buStateTin);							
				VFINANCIAL_YEAR.add(financial_year);
				VINVOICE_SEQ.add(inv_seq);
				VPERIOD_START_DT.add(temp_st_dt);
				VPERIOD_END_DT.add(temp_end_dt);
				VCREDIT_DEBIT_NO.add(inv_no);
				VREF_NO.add(ref_no);
				VINV_FLAG.add(invType);
				VINVOICE_TYPE.add(invType);
				VINVOICE_TYPE_NM.add(invType.equals("CR")?"Credit Note":invType.equals("DR")?"Debit Note":"");
				VINV_CHECKED_FLAG.add(checked_flag);
				VINV_APPROVED_FLAG.add(approved_flag);
				VPDF_INV_FLAG.add(pdf_flg);
				VSAP_APPROVAL_FLAG.add(sap_approved_flag);
				VINVOICE_DT.add(inv_dt);
				VCRDR_CRITERIA.add(criteria);
				
				String crdrgen_type="CRDR";
				if(criteria.contains("IMB") || criteria.contains("SHIP") || criteria.contains("UNAUTH"))
				{
					crdrgen_type="CRDR_IMB";
				}
				VCRDR_GEN_TYPE.add(crdrgen_type);
				
				if(print_pdf_type.equals("O") && !pdf_ori.equals(""))
				{
					VPDF_TYPE.add(print_pdf_type);
				}
				else if(print_pdf_type.equals("D") && !pdf_dup.equals(""))
				{
					VPDF_TYPE.add(print_pdf_type);
				}
				else if(print_pdf_type.equals("T") && !pdf_tri.equals(""))
				{
					VPDF_TYPE.add(print_pdf_type);
				}
				else if(print_pdf_type.equals("All"))
				{
					String allPdfType="";
					allPdfType+=pdf_ori.equals("")?allPdfType.equals("")?"O":",O":"";
					allPdfType+=pdf_dup.equals("")?allPdfType.equals("")?"D":",D":"";
					allPdfType+=pdf_tri.equals("")?allPdfType.equals("")?"T":",T":"";
					
					if(allPdfType.equals(""))
					{
						allPdfType="All";
					}
					VPDF_TYPE.add(allPdfType);
				}
				else
				{
					VPDF_TYPE.add("");
				}
				
				if(view_pdf_type.equals("All"))
				{
					queryString6="SELECT COUNT(*) "
							+ "FROM FMS_INV_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
		 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
		 	        		+ "AND PDF_TYPE IN (?,?,?)";
					stmt6=conn.prepareStatement(queryString6);
					stmt6.setString(1, own_cd);
					stmt6.setString(2, buStateTin);
					stmt6.setString(3, inv_seq);
					stmt6.setString(4, financial_year);
					stmt6.setString(5, "O");
					stmt6.setString(6, "D");
					stmt6.setString(7, "T");
					rset6=stmt6.executeQuery();
					if(rset6.next())
					{
						if(rset6.getInt(1)>0)
						{
							VPDF_FILE_NAME.add("All");
							VPDF_FILE_PATH.add("");
						}
						else
						{
							VPDF_FILE_NAME.add("");
							VPDF_FILE_PATH.add("");
						}
					}
					else
					{
						VPDF_FILE_NAME.add("");
						VPDF_FILE_PATH.add("");
					}
					rset6.close();
					stmt6.close();
				}
				else
				{
					queryString6="SELECT FILE_NAME,PDF_SIGNED "
							+ "FROM FMS_INV_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
		 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
		 	        		+ "AND PDF_TYPE=?";
					stmt6=conn.prepareStatement(queryString6);
					stmt6.setString(1, own_cd);
					stmt6.setString(2, buStateTin);
					stmt6.setString(3, inv_seq);
					stmt6.setString(4, financial_year);
					stmt6.setString(5, view_pdf_type);
					rset6=stmt6.executeQuery();
					if(rset6.next())
					{
						String pdf_signed=rset6.getString(2)==null?"":rset6.getString(2);
						
						VPDF_FILE_NAME.add(rset6.getString(1)==null?"":rset6.getString(1));
						if(pdf_signed.equals("Y"))
						{
							VPDF_FILE_PATH.add(CommonVariable.signed_sales_inv_path);
						}
						else
						{
							VPDF_FILE_PATH.add(CommonVariable.sales_inv_path);
						}
					}
					else
					{
						VPDF_FILE_NAME.add("");
						VPDF_FILE_PATH.add("");
					}
					rset6.close();
					stmt6.close();
				}
				
				if(mail_pdf_type.equals("All"))
				{
					String AllMailPdf="";
					String emailSent="";
					String emailSentInfo="";
					queryString6="SELECT PDF_TYPE,EMAIL_SENT "
							+ "FROM FMS_INV_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
		 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
		 	        		+ "AND PDF_SIGNED=? AND PDF_TYPE IN (?,?,?)";
					stmt6=conn.prepareStatement(queryString6);
					stmt6.setString(1, own_cd);
					stmt6.setString(2, buStateTin);
					stmt6.setString(3, inv_seq);
					stmt6.setString(4, financial_year);
					stmt6.setString(5, "Y");
					stmt6.setString(6, "O");
					stmt6.setString(7, "D");
					stmt6.setString(8, "T");
					rset6=stmt6.executeQuery();
					while(rset6.next())
					{
						String temp=rset6.getString(1)==null?"":rset6.getString(1);
						if(AllMailPdf.equals(""))
						{
							AllMailPdf=temp;
						}
						else
						{
							AllMailPdf+=","+temp;
						}
						
						String email_temp=rset6.getString(2)==null?"":rset6.getString(2);
						if(email_temp.equals("Y"))
						{
							emailSent="Y";
							emailSentInfo+=emailSentInfo.equals("")?temp:","+temp;
						}
					}
					rset6.close();
					stmt6.close();
					
					//AllMailPdf=AllMailPdf.equals("")?"":AllMailPdf+" Signed";
					emailSentInfo=emailSentInfo.equals("")?"":emailSentInfo+" Sent";
					VPDF_SIGNED_FLAG.add("All");
					VSIGN_PDF_TYPE.add(AllMailPdf);
					VEMAIL_SENT.add(emailSent);
					VEMAIL_SENT_INFO.add(emailSentInfo);
				}
				else
				{
					queryString6="SELECT FILE_NAME,PDF_SIGNED,EMAIL_SENT "
							+ "FROM FMS_INV_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
		 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
		 	        		+ "AND PDF_TYPE=?";
					stmt6=conn.prepareStatement(queryString6);
					stmt6.setString(1, own_cd);
					stmt6.setString(2, buStateTin);
					stmt6.setString(3, inv_seq);
					stmt6.setString(4, financial_year);
					stmt6.setString(5, view_pdf_type);
					rset6=stmt6.executeQuery();
					if(rset6.next())
					{
						String pdf_signed=rset6.getString(2)==null?"":rset6.getString(2);
						String email_sent=rset6.getString(3)==null?"":rset6.getString(3);
						VPDF_SIGNED_FLAG.add(pdf_signed);
						if(pdf_signed.equals("Y"))
						{
							VSIGN_PDF_TYPE.add(mail_pdf_type);//+" Signed"
						}
						else
						{
							VSIGN_PDF_TYPE.add("");
						}
						VEMAIL_SENT.add(email_sent);
						if(email_sent.equals("Y"))
						{
							VEMAIL_SENT_INFO.add(mail_pdf_type+" Sent");
						}
						else
						{
							VEMAIL_SENT_INFO.add("");
						}
					}
					else
					{
						VPDF_SIGNED_FLAG.add("");
						VSIGN_PDF_TYPE.add("");
						VEMAIL_SENT.add("");
						VEMAIL_SENT_INFO.add("");
					}
					rset6.close();
					stmt6.close();
				}
				
				if(cont_type.equals("O") || cont_type.equals("Q"))
				{
					String irn_no="";
					queryString5="SELECT IRN_NO "
							+ "FROM FMS_INVOICE_IRN_DTL "
							+ "WHERE COMPANY_CD=? AND INVOICE_NO=? ";
					stmt5=conn.prepareStatement(queryString5);
					stmt5.setString(1, comp_cd);
					stmt5.setString(2, inv_no);
					rset5=stmt5.executeQuery();
					if(rset5.next())
					{
						irn_no=rset5.getString(1)==null?"":rset5.getString(1);
					}
					rset5.close();
					stmt5.close();
					
					VIS_IRN_GENERATED.add(irn_no.equals("")?"N":"Y");
				}
				else
				{
					VIS_IRN_GENERATED.add("Y"); //other then LTCORA, Default 'Y'
				}
				
				int re_print_count=isRePrintPDFRequested(comp_cd,buStateTin,inv_seq,financial_year,"RLNG","REPRINT_PDF","A");
				VRE_PRINT_PDF.add(re_print_count>0?"Y":"N");
				
				String contRef="";
				String agmt_base="";
				queryString1="SELECT CONT_REF_NO,TRADE_REF_NO,AGMT_BASE  "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND CONT_NO=? AND AGMT_NO=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "UNION ALL "
						+ "SELECT A.CONT_REF_NO,NULL,NULL "
						+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_CARGO_DTL B "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? "
						+ "AND A.AGMT_NO=? AND A.AGMT_TYPE=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND B.CARGO_NO=? "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
						+ ""
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, cont_type);
				stmt1.setString(3, cont);
				stmt1.setString(4, agmt);
				stmt1.setString(5, countpty_cd);
				stmt1.setString(6, comp_cd);
				stmt1.setString(7, countpty_cd);
				stmt1.setString(8, "C");
				stmt1.setString(9, agmt);
				stmt1.setString(10, "A");
				stmt1.setString(11, cont);
				stmt1.setString(12, cont_type);
				stmt1.setString(13, cargo_no);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					contRef=rset1.getString(1)==null?"":rset1.getString(1);
					String tradeRef=rset1.getString(2)==null?"":rset1.getString(2);
					if(cont_type.equals("X"))
					{
						contRef=tradeRef;
					}
					agmt_base=rset1.getString(3)==null?"":rset1.getString(3);
				}
				rset1.close();
				stmt1.close();
				
				VCONT_REF_NO.add(contRef);
				VAGMT_BASE.add(agmt_base);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCrDrAttachmentDetail()
	{
		String function_nm="getCrDrAttachmentDetail()";
		try
		{
			queryString="SELECT FILE_NAME "
					+ "FROM FMS_INV_FILE_DTL "
					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, bu_state_tin);
			stmt.setString(3, invoice_seq);
			stmt.setString(4, financial_year);
			stmt.setString(5, "A");
			rset=stmt.executeQuery();
			if(rset.next())
			{
				att_file_name=rset.getString(1)==null?"":rset.getString(1);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getInvoiceDetailCrDr()
	{
		String function_nm="getInvoiceDetailCrDr()";
		try
		{
			queryString="SELECT BU_CONTACT_PERSON_CD,CONTACT_PERSON_CD,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD-MON-YY'),"
					+ "TO_CHAR(DUE_DT,'DD-MON-YY'),TO_CHAR(PERIOD_START_DT,'DD-MON-YY'),TO_CHAR(PERIOD_END_DT,'DD-MON-YY'),REMARK_1,REMARK_2,"
					+ "ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_VALUE,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),"
					+ "INVOICE_AMT,NET_PAYABLE_AMT,TCS_TDS,TCS_AMT,TCS_FACTOR,CHECKED_FLAG,APPROVED_FLAG, "
					+ "INVOICE_ID_SEQ,TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),EXCHG_RATE_TYPE,"
					+ "INVOICE_RAISED_IN,MARKET_MARGIN,MARKET_MARGIN_AMT,OTHER_CHARGES,OTHER_CHARGES_AMT,SUG_QTY,SUG_PERCENT,CRITERIA,"
					+ "IMB_AMT,IMB_QTY,SHIPAY_AMT,SHIPAY_QTY,OVRUN_AMT,OVRUN_QTY "
					+ "FROM FMS_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? AND FINANCIAL_YEAR=? AND INVOICE_SEQ=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, bu_state_tin);
			stmt.setString(3, financial_year);
			stmt.setString(4, invoice_seq);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				bu_contact_person_cd=rset.getString(1)==null?"0":rset.getString(1);
				contact_person_cd=rset.getString(2)==null?"0":rset.getString(2);
				
				invoice_id_seq=rset.getString(26)==null?"":rset.getString(26);
				invoice_no=rset.getString(3)==null?"":rset.getString(3);
				invoice_dt=rset.getString(4)==null?"":rset.getString(4);
				invoice_due_dt=rset.getString(5)==null?"":rset.getString(5);
				inv_period_start_dt=rset.getString(6)==null?"":rset.getString(6);
				inv_period_end_dt=rset.getString(7)==null?"":rset.getString(7);
				
				remark_1=rset.getString(8)==null?"":rset.getString(8);
				remark_2=rset.getString(9)==null?"":rset.getString(9);
				
				qty_mmbtu=nf.format(rset.getDouble(10));
				price_cd=rset.getString(12)==null?"":rset.getString(12);
				price=utilBean.RateNumberFormat(rset.getDouble(11), price_cd);
				gross_amt=nf.format(rset.getDouble(13));
				exchang_rate=nf2.format(rset.getDouble(14));
				gross_amt1=nf.format(rset.getDouble(15));
				tax_amt=nf.format(rset.getDouble(16));
				tax_struct_cd=rset.getString(17)==null?"":rset.getString(17);
				tax_struct_dt=rset.getString(18)==null?"":rset.getString(18);
				invoice_amt=nf.format(rset.getDouble(19));
				net_payable=nf.format(rset.getDouble(20));
				applicable_abbr=rset.getString(21)==null?"":rset.getString(21);
				applicable_amt=rset.getString(22)==null?"":rset.getString(22);
				TCS_factor=rset.getString(23)==null?"":rset.getString(23);
				if(!TCS_factor.equals(""))
				{
					TCS_factor=nf3.format(rset.getDouble(23));
				}
				
				if(activityFlag.equals("CHECK")) {
					activity_value=rset.getString(24)==null?"":rset.getString(24);
				}else if(activityFlag.equals("APPROVE")) {
					activity_value=rset.getString(25)==null?"":rset.getString(25);
				}
				
				//tax_struct_dtl=utilBean.getEntityTaxStructureDtl(conn,comp_cd, counterparty_cd, "C", plant_seq, bu_unit, period_start_dt);
				tax_struct_dtl=utilBean.getTaxDescr(conn, tax_struct_cd);
				
				transportation_charges=rset.getString(27)==null?"":nf2.format(rset.getDouble(27));
				transportation_amount=rset.getString(28)==null?"":nf.format(rset.getDouble(28));
				
				exchng_rate_cd=rset.getString(29)==null?"":rset.getString(29);
				exchang_rate_dt=rset.getString(30)==null?"":rset.getString(30);
				exchang_rate_type=rset.getString(31)==null?"":rset.getString(31);
				invoice_raised_in=rset.getString(32)==null?"":rset.getString(32);
				
				marketing_margin=rset.getString(33)==null?"":nf2.format(rset.getDouble(33));
				marketing_margin_amount=rset.getString(34)==null?"":nf.format(rset.getDouble(34));
				other_charges=rset.getString(35)==null?"":nf2.format(rset.getDouble(35));
				other_charges_amount=rset.getString(36)==null?"":nf.format(rset.getDouble(36));
				
				sug_qty=rset.getString(37)==null?"":nf.format(rset.getDouble(37));
				sug_percentage=rset.getString(38)==null?"":nf.format(rset.getDouble(38));
				
				criteri_formula=rset.getString(39)==null?"":rset.getString(39);
				
				imb_amt=rset.getString(40)==null?"":nf.format(rset.getDouble(40));
				imb_qty=rset.getString(41)==null?"":nf.format(rset.getDouble(41));
				ship_or_pay_amt=rset.getString(42)==null?"":nf.format(rset.getDouble(42));
				ship_or_pay_qty=rset.getString(43)==null?"":nf.format(rset.getDouble(43));
				ovrun_amt=rset.getString(44)==null?"":nf.format(rset.getDouble(44));
				ovrun_qty=rset.getString(45)==null?"":nf.format(rset.getDouble(45));
				
				gross_include_transport_tariff=nf.format(Double.parseDouble(gross_amt1));
				if(!transportation_amount.equals(""))
				{
					if(Double.parseDouble(transportation_amount) > 0)
					{
						gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(transportation_amount));
						isGrossIncTriff=true;
					}
				}
				if(!marketing_margin_amount.equals(""))
				{
					if(Double.parseDouble(marketing_margin_amount) > 0)
					{
						gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(marketing_margin_amount));
						isGrossIncTriff=true;
					}
				}
				if(!other_charges_amount.equals(""))
				{
					if(Double.parseDouble(other_charges_amount) > 0)
					{
						gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(other_charges_amount));
						isGrossIncTriff=true;
					}
				}
			}
			rset.close();
			stmt.close();
			
			queryString="SELECT ALLOC_QTY "
					+ "FROM FMS_INV_CRDR_REF "
					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? AND FINANCIAL_YEAR=? AND INVOICE_SEQ=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, bu_state_tin);
			stmt.setString(3, financial_year);
			stmt.setString(4, invoice_seq);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				new_qty_mmbtu=nf.format(rset.getDouble(1));
			}
			rset.close();
			stmt.close();
			
			//SELECTED INVOICE
			String main_inv_dt="";
			queryString1="SELECT ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_VALUE,GROSS_AMT,TO_CHAR(INVOICE_DT,'DD-MON-YY') "
					+ "FROM FMS_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND INVOICE_NO=? ";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_cd);
			stmt1.setString(3, sel_inv_no);
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				main_qty_mmbtu=nf.format(rset1.getDouble(1));
				main_price_cd=rset1.getString(3)==null?"":rset1.getString(3);
				main_price=utilBean.RateNumberFormat(rset1.getDouble(2), price_cd);
				main_gross_amt=nf.format(rset1.getDouble(4));
				main_exchang_rate=nf2.format(rset1.getDouble(5));
				main_gross_amt1=nf.format(rset1.getDouble(6));
				main_inv_dt=rset1.getString(7)==null?"":rset1.getString(7);
			}
			rset1.close();
			stmt1.close();
			//
			
			String boe_number="";
			String boe_date="";
			String ship_nm="";
			
			if(contract_type.equals("O") || contract_type.equals("Q"))
			{
				queryString="SELECT A.CONT_REF_NO,TO_CHAR(A.DDA_DT,'DD-MON-YY'),A.CONT_REF_NO,B.SHIP_CD,B.BOE_NO,TO_CHAR(B.BOE_DT,'DD-MON-YY'),TO_CHAR(SIGNING_DT,'DD-MON-YY'),"
						+ "B.CSOC_QTY,A.MDCQ_PERCENTAGE,TO_CHAR(B.ACTUAL_RECPT_DT,'DD-MON-YY') "
						+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_CARGO_DTL B "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? "
						+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.AGMT_TYPE=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND B.CARGO_NO=? "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
						+ ""
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, "C");
				stmt.setString(4, agmt_no);
				stmt.setString(5, agmt_rev_no);
				stmt.setString(6, "A");
				stmt.setString(7, cont_no);
				stmt.setString(8, contract_type);
				stmt.setString(9, cargo_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					contRef=rset.getString(1)==null?"":rset.getString(1);
					dda_dt=rset.getString(2)==null?"":rset.getString(2);
					//cargoRef=rset.getString(3)==null?"":rset.getString(3);
					
					String ship_cd=rset.getString(4)==null?"":rset.getString(4);
					ship_nm=utilBean.getShipName(conn,ship_cd);
					boe_number = rset.getString(5)==null?"":rset.getString(5);
					boe_date=rset.getString(6)==null?"":rset.getString(6);
					
					signingDt=rset.getString(7)==null?"":rset.getString(7);
					dcq=rset.getString(8)==null?"":rset.getString(8);
					mdcq_percentage=rset.getDouble(9);
					if(Double.doubleToRawLongBits(mdcq_percentage)==Double.doubleToRawLongBits(0))
					{
						mdcq_percentage=100;
					}
					
					arrivalDt=rset.getString(10)==null?"":rset.getString(10);
				}
				rset.close();
				stmt.close();
				
				queryString1="SELECT TO_CHAR(SIGNING_DT,'DD-MON-YY')  "
						+ "FROM FMS_LTCORA_AGMT_MST A "
						+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
						+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_LTCORA_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.BUY_SALE=B.BUY_SALE) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, "A");
				stmt1.setString(3, agmt_no);
				stmt1.setString(4, counterparty_cd);
				stmt1.setString(5, "C");
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					agmtSigningDt=rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
			}
			else
			{
				queryString1="SELECT CONT_REF_NO,TO_CHAR(DDA_DT,'DD-MON-YY'),TRADE_REF_NO,TO_CHAR(SIGNING_DT,'DD-MON-YY'),AGMT_BASE,DCQ,MDCQ_PERCENTAGE  "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND CONT_NO=? AND AGMT_NO=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, contract_type);
				stmt1.setString(3, cont_no);
				stmt1.setString(4, agmt_no);
				stmt1.setString(5, counterparty_cd);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					contRef=rset1.getString(1)==null?"":rset1.getString(1);
					dda_dt=rset1.getString(2)==null?"":rset1.getString(2);
					String tradeRef=rset1.getString(3)==null?"":rset1.getString(3);
					signingDt=rset1.getString(4)==null?"":rset1.getString(4);
					agmt_base=rset1.getString(5)==null?"":rset1.getString(5);
					dcq=rset1.getString(6)==null?"":rset1.getString(6);
					
					mdcq_percentage=rset1.getDouble(7);
					if(Double.doubleToRawLongBits(mdcq_percentage)==Double.doubleToRawLongBits(0))
					{
						mdcq_percentage=100;
					}
					
					if(contract_type.equals("X"))
					{
						contRef=tradeRef;
					}
				}
				rset1.close();
				stmt1.close();
				
				if(contract_type.equals("S"))
				{
					queryString2="SELECT TO_CHAR(SIGNING_DT,'DD-MON-YY')  "
							+ "FROM FMS_AGMT_MST A "
							+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
							+ "AND AGMT_NO=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) ";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, "F");
					stmt2.setString(3, agmt_no);
					stmt2.setString(4, counterparty_cd);
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						agmtSigningDt=rset2.getString(1)==null?"":rset2.getString(1);
					}
					rset2.close();
					stmt2.close();
				}
			}
			
			if(criteri_formula.contains("QTY"))
			{
				if(agmt_base.equals("D"))
				{
					changed_qty_mmbtu=nf.format(utilAlloc.getDeliverdSupplyAllocationQty(conn, comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, plant_seq, bu_unit, period_start_dt, period_end_dt));
				}
				else
				{
					changed_qty_mmbtu=nf.format(utilAlloc.getSupplyAllocationQty(conn, comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, plant_seq, bu_unit, period_start_dt, period_end_dt,cargo_no));
				}
			}
			
			queryString2="SELECT CONTACT_PERSON "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND SEQ_NO=? AND ADDR_FLAG=? AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.TYPE=B.TYPE "
					+ "AND EFF_DT <= TO_DATE(?,'DD-MON-YY'))";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, counterparty_cd);
			stmt2.setString(3, "C");
			stmt2.setString(4, contact_person_cd);
			stmt2.setString(5, "P"+plant_seq);
			stmt2.setString(6, "RLNG");
			stmt2.setString(7, invoice_dt);
			rset2=stmt2.executeQuery();
			if(rset2.next())
			{
				contact_person_nm=rset2.getString(1)==null?"":rset2.getString(1);
			}
			rset2.close();
			stmt2.close();
			
			queryString3="SELECT CONTACT_PERSON "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND SEQ_NO=? AND ADDR_FLAG=? AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.TYPE=B.TYPE "
					+ "AND EFF_DT <= TO_DATE(?,'DD-MON-YY'))";
			stmt3=conn.prepareStatement(queryString3);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, comp_cd);
			stmt3.setString(3, "B");
			stmt3.setString(4, bu_contact_person_cd);
			stmt3.setString(5, "P"+bu_unit);
			stmt3.setString(6, "RLNG");
			stmt3.setString(7, invoice_dt);
			rset3=stmt3.executeQuery();
			if(rset3.next())
			{
				bu_contact_person_nm=rset3.getString(1)==null?"":rset3.getString(1);
			}
			rset3.close();
			stmt3.close();
			
			HashMap bu_plant_detail=utilBean.getCounterpartyPlantDetail(conn,comp_cd, "B", comp_cd, bu_unit);
			bu_plantAddress=""+bu_plant_detail.get("plant_address");
			bu_plantCity=""+bu_plant_detail.get("plant_city");
			bu_plantState=""+bu_plant_detail.get("plant_state");
			bu_plantPin=""+bu_plant_detail.get("plant_pin");
			bu_plantNm=""+bu_plant_detail.get("plant_name");
			
			HashMap plant_detail=utilBean.getCounterpartyPlantDetail(conn,comp_cd, "C", counterparty_cd, plant_seq);
			plantAddress=""+plant_detail.get("plant_address");
			plantCity=""+plant_detail.get("plant_city");
			plantState=""+plant_detail.get("plant_state");
			plantPin=""+plant_detail.get("plant_pin");
			
			if(contract_type.equals("O") || contract_type.equals("Q"))
			{
				tax_info="State : "+plantState;
				tax_info+="<br>State Code : "+utilBean.getState_TIN(conn, comp_cd, counterparty_cd, "C", plant_seq);
				//String temp_tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "C", counterparty_cd, plant_seq);
				//tax_info+=temp_tax_info.replaceAll("\n", "<br>");
				
				queryString = "SELECT A.STAT_NO, TO_CHAR(A.EFF_DT,'DD/MM/YYYY'), B.STAT_NM "
						+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B "
						+ "WHERE A.COUNTERPARTY_CD=? AND A.ENTITY=? "
						+ "AND A.PLANT_SEQ_NO=? AND A.COMPANY_CD=? "
						+ "AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL AND B.STAT_CD IN ('1003','1001')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, counterparty_cd);
				stmt.setString(2, "C");
				stmt.setString(3, plant_seq);
				stmt.setString(4, comp_cd);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					String no = rset.getString(1)==null?"":rset.getString(1);
					String nm = rset.getString(3)==null?"":rset.getString(3);
	
					tax_info+="<br>"+nm+" : "+no;
				}
				stmt.close();
				rset.close();
				
				bu_tax_info="State : "+bu_plantState;
				bu_tax_info+="<br>State Code : "+utilBean.getState_TIN(conn, comp_cd, comp_cd, "B", bu_unit);
				//String temp_bu_tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "B", comp_cd, bu_unit);
				//bu_tax_info+=temp_bu_tax_info.replaceAll("\n", "<br>");
				
				queryString = "SELECT A.STAT_NO, TO_CHAR(A.EFF_DT,'DD/MM/YYYY'), B.STAT_NM "
						+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B "
						+ "WHERE A.COUNTERPARTY_CD=? AND A.ENTITY=? "
						+ "AND A.PLANT_SEQ_NO=? AND A.COMPANY_CD=? "
						+ "AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL AND B.STAT_CD IN ('1003','1001')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, "B");
				stmt.setString(3, bu_unit);
				stmt.setString(4, comp_cd);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					String no = rset.getString(1)==null?"":rset.getString(1);
					String nm = rset.getString(3)==null?"":rset.getString(3);
	
					bu_tax_info+="<br>"+nm+" : "+no;
				}
				stmt.close();
				rset.close();
				
				bu_tax_info+="<br>SAC : 999799"
						+ "<br>Description of Service : Other Miscellaneous services - Other Services n.e.c."
						+ "<br>Place Of Supply : "+plantState; //WILL BE CUSTOMER PLANT STATE AS DISCUSSED BY JAYASRI MAM WITH VIJAY ON 20250725
			}
			else
			{
				tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "C", counterparty_cd, plant_seq);
				tax_info=tax_info.replaceAll("\n", "<br>");
				
				bu_tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "B", comp_cd, bu_unit);
				bu_tax_info=bu_tax_info.replaceAll("\n", "<br>");
			}
			
			couterpty_nm=""+utilBean.getCounterpartyName(conn,counterparty_cd);
			int srno=0;
			
			String parameter="counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+""
					+ "&cont_rev="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev_no+"&plant_seq="+plant_seq+"&billing_cycle="+billing_cycle+""
					+ "&period_start_dt="+period_start_dt+"&period_end_dt="+period_end_dt+"&bu_unit="+bu_unit+"&financial_year="+financial_year+""
					+ "&bu_state_tin="+bu_state_tin+"&invoice_seq="+invoice_seq+"&activityFlag="+activityFlag+"&cargo_no="+cargo_no+"&inv_flag="+inv_flag+"&sel_inv_no="+sel_inv_no;

			price_cd_nm=""+utilBean.getRateUnitNm(conn,price_cd);
			
			String NormalFont="";
			if(inv_flag.equals("CR")) 
			{
				NormalFont="Credit Note";
			}
			else if(inv_flag.equals("DR"))
			{ 
				NormalFont="Debit Note";
			}
			
			if(criteri_formula.contains("IMB") || criteri_formula.contains("SHIP") || criteri_formula.contains("UNAUTH"))
			{
				if(criteri_formula.contains("IMB"))
				{
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("Imbalance Charges");
					VPDF_COL3.add("");
					VPDF_COL4.add("INR");
					VPDF_COL5.add("Att1");
					VPDF_COL6.add("");
					VPDF_COL7.add(imb_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(imb_amt))));
				}
				
				if(criteri_formula.contains("UNAUTH"))
				{
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("Unauthorized Overrun Charges");
					VPDF_COL3.add("");
					VPDF_COL4.add("INR");
					VPDF_COL5.add("Att1");
					VPDF_COL6.add("");
					VPDF_COL7.add(ovrun_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(ovrun_amt))));
				}
				
				if(criteri_formula.contains("SHIP"))
				{
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("Ship or Pay Charges");
					VPDF_COL3.add("");
					VPDF_COL4.add("INR");
					VPDF_COL5.add("Att1");
					VPDF_COL6.add("");
					VPDF_COL7.add(ship_or_pay_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(ship_or_pay_amt))));
				}
				
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("Gross Amount");
				VPDF_COL3.add("");
				VPDF_COL4.add("INR");
				VPDF_COL5.add("");
				VPDF_COL6.add("");
				VPDF_COL7.add(gross_include_transport_tariff.equals("")?"":Double.doubleToRawLongBits(Double.parseDouble(gross_include_transport_tariff))==Double.doubleToRawLongBits(0)?"":nf.format(Math.abs(Double.parseDouble(gross_include_transport_tariff))));
			}
			else
			{
				srno+=1;
				VPDF_COL1.add(srno);
				/*if(contract_type.equals("O") || contract_type.equals("Q"))
				{
					VPDF_COL2.add("Natural Gas (Regasified) as per Invoice No : "+sel_inv_no+" dated "+main_inv_dt);
				}
				else
				{*/
					//VPDF_COL2.add("Natural Gas (Delivered) as per Invoice No : "+sel_inv_no+" dated "+main_inv_dt);
				VPDF_COL2.add("Reference Att-1 "+NormalFont+" Note against Invoice No : "+sel_inv_no+" dated "+main_inv_dt);
				//}
				VPDF_COL3.add("");
				VPDF_COL4.add("");//+utilBean.getRateUnitNm(conn,main_price_cd));
				VPDF_COL5.add("");//main_qty_mmbtu);
				VPDF_COL6.add("");
				VPDF_COL7.add("");
				
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("Gross Amount");
				VPDF_COL3.add("<a onclick=openAtt1('rpt_crdr_view_attachment1.jsp?"+parameter+"')>Att1</a>");
				VPDF_COL4.add("INR");
				VPDF_COL5.add("");
				VPDF_COL6.add("");
				VPDF_COL7.add(gross_include_transport_tariff.equals("")?"":Double.doubleToRawLongBits(Double.parseDouble(gross_include_transport_tariff))==Double.doubleToRawLongBits(0)?"":nf.format(Math.abs(Double.parseDouble(gross_include_transport_tariff))));
			}
			
			srno+=1;
			VPDF_COL1.add(srno);
			VPDF_COL2.add("Tax ("+tax_struct_dtl+")");
			VPDF_COL3.add("");
			VPDF_COL4.add("INR");
			VPDF_COL5.add("");
			VPDF_COL6.add("");
			VPDF_COL7.add(tax_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(tax_amt))));
			
			double temp_srno=srno;
			queryString1="SELECT COUNT(*) "
					+ "FROM FMS_INV_TAX_DTL "
					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
					+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? ";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, bu_state_tin);
			stmt1.setString(3, financial_year);
			stmt1.setString(4, invoice_seq);
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				if(rset1.getInt(1)>1)
				{
					queryString="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
							+ "FROM FMS_INV_TAX_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? ";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, bu_state_tin);
					stmt.setString(3, financial_year);
					stmt.setString(4, invoice_seq);
					rset=stmt.executeQuery();
					while(rset.next())
					{
						temp_srno=temp_srno+0.1;
						VPDF_COL1.add(nf0.format(temp_srno));
						VPDF_COL2.add(rset.getString(2)==null?"":rset.getString(2));
						VPDF_COL3.add("");
						VPDF_COL4.add("INR");
						VPDF_COL5.add("");
						VPDF_COL6.add("");
						VPDF_COL7.add(rset.getString(3)==null?"":nf.format(Math.abs(rset.getDouble(3))));
					}
					rset.close();
					stmt.close();
				}
			}
			rset1.close();
			stmt1.close();
			
			String invAmtLbl=inv_flag.equals("UG")?"Invoice Amount - GST on SUG":NormalFont+" Amount";
			srno+=1;
			VPDF_COL1.add(srno);
			VPDF_COL2.add(invAmtLbl);
			VPDF_COL3.add("");
			VPDF_COL4.add("INR");
			VPDF_COL5.add("");
			VPDF_COL6.add("");
			VPDF_COL7.add(invoice_amt.equals("")?"":nf.format(Math.abs(Double.parseDouble(invoice_amt))));
			
			if(applicable_abbr.equals("TCS"))
			{
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("TCS");
				VPDF_COL3.add("");
				VPDF_COL4.add("INR");
				VPDF_COL5.add("");
				VPDF_COL6.add(TCS_factor+"%");
				VPDF_COL7.add(applicable_amt);
			}
			
			srno+=1;
			VPDF_COL1.add(srno);
			VPDF_COL2.add("Net Amount Payable");
			VPDF_COL3.add("");
			VPDF_COL4.add("INR");
			VPDF_COL5.add("");
			VPDF_COL6.add("");
			VPDF_COL7.add(net_payable.equals("")?"":nf.format(Math.abs(Double.parseDouble(net_payable))));
			
			
			reason="Change in ";
			if(!criteri_formula.equals(""))
			{
				String[] split_criteri_formula = criteri_formula.split("#");
				for(int i=0; i<split_criteri_formula.length; i++)
				{
					if(i!=0)
					{
						if((i+1) == split_criteri_formula.length)
						{
							reason+=" and ";
						}
						else
						{
							reason+=", ";
						}
					}
					
					if(split_criteri_formula[i].toString().equals("QTY"))
					{
						reason+="Quantity";
					}
					else if(split_criteri_formula[i].toString().equals("PRICE"))
					{
						reason+="Price";
					}
					else if(split_criteri_formula[i].toString().equals("EXCHG"))
					{
						reason+="Exchange Rate";
					}
					else if(split_criteri_formula[i].toString().equals("TC"))
					{
						reason+="Transportation Tariff";
					}
					else if(split_criteri_formula[i].toString().equals("MM"))
					{
						reason+="Marketing Margin";
					}
					else if(split_criteri_formula[i].toString().equals("OC"))
					{
						reason+="Other Charges";
					}
					else if(split_criteri_formula[i].toString().equals("TAXP"))
					{
						reason+="Tax %";
					}
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCrDrInvoiceNumber()
	{
		String function_nm="getCrDrInvoiceNumber()";
		try
		{
			String invoice_prefix=utilBean.getInvoicePrefix(conn,comp_cd);
			
			String fin_yr="";
			if(!financial_year.equals(""))
			{
				String[] temp = financial_year.split("-");
				fin_yr=temp[0].substring(2,temp[0].length())+"-"+temp[1].substring(2,temp[1].length());
			}
			
			String state_abbr="";
			String state_code=bu_state_tin;
			if(state_code.length()<=1)
			{
				state_code="0"+state_code;
			}
			
			if(!invoice_id_seq.equals(""))
			{
				VINVOICE_ID_SEQ.add(invoice_id_seq);
				VINVOICE_NO.add(invoice_no);
			}
			
			String contType="";
			String invSeries="";
			String ff_invType="";
			if(contract_type.equals("Q") || contract_type.equals("O"))
			{
				contType="'Q','O'";
				invSeries="L"+inv_flag;
				
				//ff_invType="'SI','UG','ST','DI'";
			}
			else
			{
				contType="'S','L','X'";
				invSeries=inv_flag;
				
				//ff_invType="'S'";
			}
			
			if(!invoice_prefix.equals(""))
			{
				int no_inv_no=10;
				for(int i=1;i<=no_inv_no;i++)
				{
					String invoice_id_seq=""+i;
					int count=0;
					
					//EMS CR DR Series Blocking for SEIPL Profile to handle Absence of FMS8 migrated data
					if(comp_cd.equals("2") && (contract_type.equals("S") || contract_type.equals("L") || contract_type.equals("X")))
					{
						if(inv_flag.equals("CR"))
						{
							if(bu_state_tin.equals("27")) //MH
							{
								if(i <= 6)
								{
									count=count+1;
								}
							}
							else if(bu_state_tin.equals("28")) //AP
							{
								if(i <= 4)
								{
									count=count+1;
								}
							}
							else if(bu_state_tin.equals("24")) //GJ
							{
								if(i <= 3)
								{
									count=count+1;
								}
							}
						}
						else if(inv_flag.equals("DR"))
						{
							if(bu_state_tin.equals("27")) //MH
							{
								if(i <= 11)
								{
									count=count+1;
								}
							}
							else if(bu_state_tin.equals("28")) //AP
							{
								if(i <= 3)
								{
									count=count+1;
								}
							}
							else if(bu_state_tin.equals("24")) //GJ
							{
								if(i <= 3)
								{
									count=count+1;
								}
							}
						}
					}
					/////////////////////////////////////////////////////////
					
					queryString="SELECT COUNT(*) "
							+ "FROM FMS_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND BU_STATE_TIN=? AND INVOICE_ID_SEQ=? AND CONTRACT_TYPE IN ("+contType+") "
							+ "AND INV_FLAG=? ";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, financial_year);
					stmt.setString(3, bu_state_tin);
					stmt.setString(4, invoice_id_seq);
					stmt.setString(5, inv_flag);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						count += rset.getInt(1);
					}
					rset.close();
					stmt.close();
					
					queryString1="SELECT COUNT(*) "
							+ "FROM FMS_FFLOW_INV_MST "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_TYPE=? AND CONTRACT_TYPE IN ("+contType+") "
							+ "AND INVOICE_ID_SEQ=? ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, bu_state_tin);
					stmt1.setString(3, financial_year);
					stmt1.setString(4, inv_flag);
					stmt1.setString(5, invoice_id_seq);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						count += rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					
					/*if(invSeries.equals("S") || invSeries.equals("L"))
					{
						if(invSeries.equals("L"))
						{
							queryString1="SELECT COUNT(*) "
									+ "FROM FMS_DLNG_FFLOW_INV_MST "
									+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
									+ "AND FINANCIAL_YEAR=? "
									//+ "AND INVOICE_TYPE=? "
									+ "AND INVOICE_TYPE IN ('TLU') "
									+ "AND INVOICE_ID_SEQ=? ";
							stmt1=conn.prepareStatement(queryString1);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, bu_state_tin);
							stmt1.setString(3, financial_year);
							//stmt1.setString(4, "S");
							stmt1.setString(4, invoice_id_seq);
							rset1=stmt1.executeQuery();
							if(rset1.next())
							{
								count += rset1.getInt(1);
							}
							rset1.close();
							stmt1.close();
						}
					}*/
					
					if(count==0)
					{	
						queryString2="SELECT STATE_CODE "
								+ "FROM FMS_STATE_MST "
								+ "WHERE TIN=?";
						stmt2=conn.prepareStatement(queryString2);
						stmt2.setString(1, state_code);
						rset2=stmt2.executeQuery();
						if(rset2.next())
						{
							state_abbr=rset2.getString(1)==null?"":rset2.getString(1);
						}
						rset2.close();
						stmt2.close();
						String invoice_no="";
						invoice_no=invoice_prefix+""+invSeries+""+state_abbr+""+utilBean.PrePaddingZero(invoice_id_seq, 4)+"/"+fin_yr;
						
						VINVOICE_ID_SEQ.add(invoice_id_seq);
						VINVOICE_NO.add(invoice_no);
					}
					else
					{
						no_inv_no+=1;
					}
				}
			}
			
			if(invoice_id_seq.equals(""))
			{
				if(VINVOICE_ID_SEQ.size()>0)
				{
					invoice_id_seq=""+VINVOICE_ID_SEQ.elementAt(0);
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getLpSalesInvoicePreparationList()
	{
		String function_nm="getLpSalesInvoicePreparationList()";

		try
		{
			String temp_period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
			String temp_period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
			
			queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CARGO_NO,"
					+ "CONTRACT_TYPE,NULL,BU_UNIT,BU_STATE_TIN,PLANT_SEQ,INVOICE_SEQ,INVOICE_NO,"
					+ "TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'),PAY_RECV_AMT,TO_CHAR(PAY_RECV_DT,'DD/MM/YYYY'),"
					+ "ALLOC_QTY,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),FINANCIAL_YEAR,INV_FLAG "
					+ "FROM FMS_INVOICE_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND PAY_RECV_DT >= TO_DATE(?,'DD/MM/YYYY') AND PAY_RECV_DT <= TO_DATE(?,'DD/MM/YYYY') "
					+ "AND APPROVED_FLAG=? AND INVOICE_NO IS NOT NULL AND PAY_RECV_DT IS NOT NULL "
					+ "AND PAY_RECV_DT > DUE_DT AND CONTRACT_TYPE IN ('S','L','X') "
					+ "AND PAY_RECV_AMT IS NOT NULL AND PAY_RECV_DT IS NOT NULL "
					+ "AND ((NVL(NET_PAYABLE_AMT,0)-NVL(TDS_GROSS_AMT,0)-NVL(TDS_TAX_AMT,0)) - NVL(PAY_RECV_AMT,0)) <= "+CommonVariable.receivable_tolerance+" "
					+ "ORDER BY (SELECT Z.COUNTERPARTY_ABBR FROM FMS_COUNTERPARTY_MST Z WHERE A.COUNTERPARTY_CD=Z.COUNTERPARTY_CD AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST Y WHERE Y.COUNTERPARTY_CD=Z.COUNTERPARTY_CD))";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, temp_period_start_dt);
			stmt.setString(3, temp_period_end_dt);
			stmt.setString(4, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String agmtno=rset.getString(3)==null?"0":rset.getString(3);
				String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
				String contno=rset.getString(5)==null?"0":rset.getString(5);
				String contrev=rset.getString(6)==null?"0":rset.getString(6);
				String cargo_no=rset.getString(7)==null?"0":rset.getString(7);
				String contract_type=rset.getString(8)==null?"":rset.getString(8);
				String bu_plant_seq=rset.getString(10)==null?"":rset.getString(10);
				String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
				String state_code=rset.getString(11)==null?"":rset.getString(11);
				String plant_seq=rset.getString(12)==null?"":rset.getString(12);
				String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "C");
				String invoice_seq=rset.getString(13)==null?"":rset.getString(13);
				String invoice_no=rset.getString(14)==null?"":rset.getString(14);
				String invoice_dt=rset.getString(15)==null?"":rset.getString(15);
				String due_dt = rset.getString(16)==null?"":rset.getString(16);
				String pay_recv_amt = rset.getString(17)==null?"":rset.getString(17);
				String pay_recv_dt = rset.getString(18)==null?"":rset.getString(18);
				double qtyMMBTU = rset.getDouble(19);
				String period_start_dt = rset.getString(20)==null?"":rset.getString(20);
				String period_end_dt = rset.getString(21)==null?"":rset.getString(21);
				String financial_year = rset.getString(22)==null?"":rset.getString(22);
				String inv_flag = rset.getString(23)==null?"":rset.getString(23);
				String cont_start_dt="",cont_end_dt="";
				
				String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, contract_type, cargo_no);
				
				String cont_ref="";
				queryString1="SELECT CONT_REF_NO,TRADE_REF_NO, TO_CHAR(START_DT,'DD/MM/YYYY'), TO_CHAR(END_DT,'DD/MM/YYYY')  "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND CONT_NO=? AND AGMT_NO=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, own_cd);
				stmt1.setString(2, contract_type);
				stmt1.setString(3, contno);
				stmt1.setString(4, agmtno);
				stmt1.setString(5, countpty_cd);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					cont_ref=rset1.getString(1)==null?"":rset1.getString(1);
					String tradeRef=rset1.getString(2)==null?"":rset1.getString(2);
					cont_start_dt = rset1.getString(3)==null?"":rset1.getString(3);
					cont_end_dt = rset1.getString(4)==null?"":rset1.getString(4);
					if(contract_type.equals("X"))
					{
						cont_ref=tradeRef;
					}
				}
				rset1.close();
				stmt1.close();
				
				inv_index=inv_index+1;
				
				VBU_STATE_TIN.add(state_code);							
				VCOUNTERPTY_CD.add(countpty_cd);
				VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VAGMT_NO.add(agmtno);
				VAGMT_REV_NO.add(agmtrev);
				VCONT_NO.add(contno);
				VCONT_REV_NO.add(contrev);
				VCARGO_NO.add(cargo_no);
				VCONT_NAME.add(rset.getString(9)==null?"":rset.getString(9));
				VCONT_REF_NO.add(cont_ref);
				VCONTRACT_TYPE.add(contract_type);
				VDEAL_NO.add(deal_no);
				
				VPLANT_SEQ.add(plant_seq);
				VPLANT_ABBR.add(plant_abbr);
				VBU_PLANT_SEQ.add(bu_plant_seq);
				VBU_PLANT_ABBR.add(bu_plant_abbr);
				VALLOC_QTY.add(nf.format(qtyMMBTU));
				VAGMT_BASE.add(agmt_base);
				
				VSTATUS.add("");
				
				VORI_INV_SEQ.add(invoice_seq);
				VORI_INVOICE_NO.add(invoice_no);
				VORI_INVOICE_DT.add(invoice_dt);
				VORI_INVOICE_DUE_DT.add(due_dt);
				VORI_INVOICE_PAYRECV_DT.add(pay_recv_dt);
				
				VBILLING_FREQ_FLAG.add(billing_cycle);
				VBILLING_FREQ_NM.add(billing_freq_nm);

				VPERIOD_START_DT.add(period_start_dt);
				VPERIOD_END_DT.add(period_end_dt);
				
				VSTART_DT.add(cont_start_dt);
				VEND_DT.add(cont_end_dt);
				
				VORI_FINANCIAL_YEAR.add(financial_year);
				
				
				String lp_inv_flag="LP";
				VINV_FLAG.add(lp_inv_flag);
				VORI_INV_FLAG.add(inv_flag);
				getLpInvDetailForPreparationList(own_cd,countpty_cd, contno, agmtno,contract_type, plant_seq, 
						bu_plant_seq, billing_cycle, cont_start_dt, cont_end_dt, state_code,cargo_no,lp_inv_flag,invoice_no);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getLpLTCORAInvoicePreparationList()
	{
		String function_nm="getLpLTCORAInvoicePreparationList()";
		
		try
		{
			String temp_period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
			String temp_period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
			
			queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CARGO_NO,"
					+ "CONTRACT_TYPE,NULL,BU_UNIT,BU_STATE_TIN,PLANT_SEQ,INVOICE_SEQ,INVOICE_NO,"
					+ "TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'),PAY_RECV_AMT,TO_CHAR(PAY_RECV_DT,'DD/MM/YYYY'),"
					+ "ALLOC_QTY,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),FINANCIAL_YEAR,INV_FLAG "
					+ "FROM FMS_INVOICE_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND PAY_RECV_DT >= TO_DATE(?,'DD/MM/YYYY') AND PAY_RECV_DT <= TO_DATE(?,'DD/MM/YYYY') "
					+ "AND APPROVED_FLAG=? AND INVOICE_NO IS NOT NULL AND PAY_RECV_DT IS NOT NULL "
					+ "AND PAY_RECV_DT > DUE_DT AND CONTRACT_TYPE IN ('O','Q') "
					+ "AND PAY_RECV_AMT IS NOT NULL AND PAY_RECV_DT IS NOT NULL "
					+ "AND ((NVL(NET_PAYABLE_AMT,0)-NVL(TDS_GROSS_AMT,0)-NVL(TDS_TAX_AMT,0)) - NVL(PAY_RECV_AMT,0)) <= "+CommonVariable.receivable_tolerance+" "
					+ "ORDER BY (SELECT Z.COUNTERPARTY_ABBR FROM FMS_COUNTERPARTY_MST Z WHERE A.COUNTERPARTY_CD=Z.COUNTERPARTY_CD AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST Y WHERE Y.COUNTERPARTY_CD=Z.COUNTERPARTY_CD))";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, temp_period_start_dt);
			stmt.setString(3, temp_period_end_dt);
			stmt.setString(4, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String agmtno=rset.getString(3)==null?"0":rset.getString(3);
				String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
				String contno=rset.getString(5)==null?"0":rset.getString(5);
				String contrev=rset.getString(6)==null?"0":rset.getString(6);
				String cargo_no=rset.getString(7)==null?"0":rset.getString(7);
				String contract_type=rset.getString(8)==null?"":rset.getString(8);
				String bu_plant_seq=rset.getString(10)==null?"":rset.getString(10);
				String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
				String state_code=rset.getString(11)==null?"":rset.getString(11);
				String plant_seq=rset.getString(12)==null?"":rset.getString(12);
				String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "C");
				String invoice_seq=rset.getString(13)==null?"":rset.getString(13);
				String invoice_no=rset.getString(14)==null?"":rset.getString(14);
				String invoice_dt=rset.getString(15)==null?"":rset.getString(15);
				String due_dt = rset.getString(16)==null?"":rset.getString(16);
				String pay_recv_amt = rset.getString(17)==null?"":rset.getString(17);
				String pay_recv_dt = rset.getString(18)==null?"":rset.getString(18);
				double qtyMMBTU = rset.getDouble(19);
				String period_start_dt = rset.getString(20)==null?"":rset.getString(20);
				String period_end_dt = rset.getString(21)==null?"":rset.getString(21);
				String financial_year = rset.getString(22)==null?"":rset.getString(22);
				String inv_flag=rset.getString(23)==null?"":rset.getString(23);
				String cont_start_dt="",cont_end_dt="";
				
				String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, contract_type, cargo_no);
				
				String cont_ref="";
				int selCnt =0;
				queryString1="SELECT CARGO_REF, TO_CHAR(A.ACTUAL_RECPT_DT,'DD/MM/YYYY'),"
						+ "TO_CHAR(A.ACTUAL_RECPT_DT+NVL(A.STORAGE_DAYS-1,0)+NVL(A.STORAGE_EXT_DAYS,0),'DD/MM/YYYY') "
						+ "FROM FMS_LTCORA_CONT_CARGO_DTL A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND AGMT_NO=? AND CONTRACT_TYPE=? AND BUY_SALE=? AND CARGO_NO=? "
						+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.CARGO_NO=B.CARGO_NO)";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(++selCnt, own_cd);
				stmt1.setString(++selCnt, countpty_cd);
				stmt1.setString(++selCnt, contno);
				stmt1.setString(++selCnt, agmtno);
				stmt1.setString(++selCnt, contract_type);
				stmt1.setString(++selCnt, "C");
				stmt1.setString(++selCnt, cargo_no);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					cont_ref=rset1.getString(1)==null?"":rset1.getString(1);
					cont_start_dt=rset1.getString(2)==null?"":rset1.getString(2);
					cont_end_dt=rset1.getString(3)==null?"":rset1.getString(3);
				}
				rset1.close();
				stmt1.close();
				
				inv_index=inv_index+1;
				
				VBU_STATE_TIN.add(state_code);							
				VCOUNTERPTY_CD.add(countpty_cd);
				VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VAGMT_NO.add(agmtno);
				VAGMT_REV_NO.add(agmtrev);
				VCONT_NO.add(contno);
				VCONT_REV_NO.add(contrev);
				VCARGO_NO.add(cargo_no);
				VCONT_NAME.add(rset.getString(9)==null?"":rset.getString(9));
				VCONT_REF_NO.add(cont_ref);
				VCONTRACT_TYPE.add(contract_type);
				VDEAL_NO.add(deal_no);
				
				VPLANT_SEQ.add(plant_seq);
				VPLANT_ABBR.add(plant_abbr);
				VBU_PLANT_SEQ.add(bu_plant_seq);
				VBU_PLANT_ABBR.add(bu_plant_abbr);
				VALLOC_QTY.add(nf.format(qtyMMBTU));
				VAGMT_BASE.add(agmt_base);
				
				VSTATUS.add("");
				
				VORI_INV_SEQ.add(invoice_seq);
				VORI_INVOICE_NO.add(invoice_no);
				VORI_INVOICE_DT.add(invoice_dt);
				VORI_INVOICE_DUE_DT.add(due_dt);
				VORI_INVOICE_PAYRECV_DT.add(pay_recv_dt);
				
				VBILLING_FREQ_FLAG.add(billing_cycle);
				VBILLING_FREQ_NM.add(billing_freq_nm);
				
				VPERIOD_START_DT.add(period_start_dt);
				VPERIOD_END_DT.add(period_end_dt);
				
				VSTART_DT.add(cont_start_dt);
				VEND_DT.add(cont_end_dt);
				
				VORI_FINANCIAL_YEAR.add(financial_year);
				String lp_inv_flag="LP";
				VINV_FLAG.add(lp_inv_flag);
				VORI_INV_FLAG.add(inv_flag);
				
				getLpInvDetailForPreparationList(own_cd,countpty_cd, contno, agmtno,contract_type, plant_seq, 
						bu_plant_seq, billing_cycle, cont_start_dt, cont_end_dt, state_code,cargo_no,lp_inv_flag,invoice_no);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	/*
	 * HM : Will need to handle this later
	 * 
	 * public void getLpFFlowInvoiceList()
	{
		String function_nm="getLpFFlowInvoiceList()";
		
		try
		{
			String temp_period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
			String temp_period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
			
			queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CARGO_NO,"
					+ "CONTRACT_TYPE,NULL,BU_UNIT,BU_STATE_TIN,ADDR_FLAG,INVOICE_SEQ,INVOICE_NO,"
					+ "TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'),PAY_RECV_AMT,TO_CHAR(PAY_RECV_DT,'DD/MM/YYYY'),"
					+ "ALLOC_QTY,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),FINANCIAL_YEAR "
					+ "FROM FMS_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? "
					+ "AND INVOICE_DT >= TO_DATE(?,'DD/MM/YYYY') AND INVOICE_DT <= TO_DATE(?,'DD/MM/YYYY') "
					+ "AND APPROVED_FLAG=? AND INVOICE_NO IS NOT NULL AND PAY_RECV_DT IS NOT NULL "
					+ "AND PAY_RECV_DT > DUE_DT";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, temp_period_start_dt);
			stmt.setString(3, temp_period_end_dt);
			stmt.setString(4, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String agmtno=rset.getString(3)==null?"0":rset.getString(3);
				String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
				String contno=rset.getString(5)==null?"0":rset.getString(5);
				String contrev=rset.getString(6)==null?"0":rset.getString(6);
				String cargo_no=rset.getString(7)==null?"0":rset.getString(7);
				String contract_type=rset.getString(8)==null?"":rset.getString(8);
				String bu_plant_seq=rset.getString(10)==null?"":rset.getString(10);
				String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
				String state_code=rset.getString(11)==null?"":rset.getString(11);
				String address_type=rset.getString(12)==null?"":rset.getString(12);
				String plant_seq="";
				if(!address_type.equals("R") && !address_type.equals("B") && !address_type.equals("C"))
				{
					plant_seq=address_type.substring(1,address_type.length());
				}
				String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "C");
				String invoice_seq=rset.getString(13)==null?"":rset.getString(13);
				String invoice_no=rset.getString(14)==null?"":rset.getString(14);
				String invoice_dt=rset.getString(15)==null?"":rset.getString(15);
				String due_dt = rset.getString(16)==null?"":rset.getString(16);
				String pay_recv_amt = rset.getString(17)==null?"":rset.getString(17);
				String pay_recv_dt = rset.getString(18)==null?"":rset.getString(18);
				double qtyMMBTU = rset.getDouble(19);
				String period_start_dt = rset.getString(20)==null?"":rset.getString(20);
				String period_end_dt = rset.getString(21)==null?"":rset.getString(21);
				String financial_year = rset.getString(22)==null?"":rset.getString(22);
				
				String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, contract_type, cargo_no);
				
				String cont_ref="";
				
				if(contract_type.equals("O") || contract_type.equals("Q"))
				{
					queryString1="SELECT CONT_REF_NO "
							+ "FROM FMS_LTCORA_CONT_MST A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND AGMT_NO=? AND CONTRACT_TYPE=? AND BUY_SALE=? "
							+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
							+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE)";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, own_cd);
					stmt1.setString(2, countpty_cd);
					stmt1.setString(3, contno);
					stmt1.setString(4, agmtno);
					stmt1.setString(5, contract_type);
					stmt1.setString(6, "C");
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						cont_ref=rset1.getString(1)==null?"":rset1.getString(1);
					}
					rset1.close();
					stmt1.close();
				}
				else 
				{
					queryString1="SELECT CONT_REF_NO,TRADE_REF_NO  "
							+ "FROM FMS_SUPPLY_CONT_MST A "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND CONT_NO=? AND AGMT_NO=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, own_cd);
					stmt1.setString(2, contract_type);
					stmt1.setString(3, contno);
					stmt1.setString(4, agmtno);
					stmt1.setString(5, countpty_cd);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						cont_ref=rset1.getString(1)==null?"":rset1.getString(1);
						String tradeRef=rset1.getString(2)==null?"":rset1.getString(2);
						
						if(contract_type.equals("X"))
						{
							cont_ref=tradeRef;
						}
					}
					rset1.close();
					stmt1.close();
				}
				
				inv_index=inv_index+1;
				
				VBU_STATE_TIN.add(state_code);							
				VCOUNTERPTY_CD.add(countpty_cd);
				VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VAGMT_NO.add(agmtno);
				VAGMT_REV_NO.add(agmtrev);
				VCONT_NO.add(contno);
				VCONT_REV_NO.add(contrev);
				VCARGO_NO.add(cargo_no);
				VCONT_NAME.add(rset.getString(9)==null?"":rset.getString(9));
				VCONT_REF_NO.add(cont_ref);
				VCONTRACT_TYPE.add(contract_type);
				VDEAL_NO.add(deal_no);
				
				VPLANT_SEQ.add(plant_seq);
				VPLANT_ABBR.add(plant_abbr);
				VBU_PLANT_SEQ.add(bu_plant_seq);
				VBU_PLANT_ABBR.add(bu_plant_abbr);
				VALLOC_QTY.add(nf.format(qtyMMBTU));
				VAGMT_BASE.add(agmt_base);
				
				VSTATUS.add("");
				
				VORI_INVOICE_NO.add(invoice_no);
				VORI_INVOICE_DT.add(invoice_dt);
				VORI_INVOICE_DUE_DT.add(due_dt);
				VORI_INVOICE_PAYRECV_DT.add(pay_recv_dt);
				
				VBILLING_FREQ_FLAG.add(billing_cycle);
				VBILLING_FREQ_NM.add(billing_freq_nm);
				
				VPERIOD_START_DT.add(period_start_dt);
				VPERIOD_END_DT.add(period_end_dt);
				
				VFINANCIAL_YEAR.add(financial_year);
				
				String inv_flag="LP";
				VINV_FLAG.add(inv_flag);
				getLpInvDetailForPreparationList(own_cd,countpty_cd, contno, agmtno,contract_type, plant_seq, 
						bu_plant_seq, billing_cycle, temp_period_start_dt, temp_period_end_dt, state_code,cargo_no,inv_flag,invoice_no);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}*/
	

	public void getLpInvDetailForPreparationList(String own_cd,String countpty_cd, String contno, String agmtno,String cont_type, String plant_seq, 
			String bu_plant_seq, String billing_cycle, String period_start_dt, String period_end_dt,String state_code,String cargo_no, String inv_flag,String invoice_no)
	{
		String function_nm="getLpInvDetailForPreparationList()";
		try
		{
			String inv_no="";
			String inv_seq="";
			String checked_flag="";
			String approved_flag="";
			String pdf_flg="";
			String pdf_ori="";
			String pdf_tri="";
			String pdf_dup="";
			String sap_approved_flag=""; 
			String fiscal_yr="";
			String irn_no="";
			String inv_dt="";
			
			queryString5="SELECT INVOICE_NO,CHECKED_FLAG,APPROVED_FLAG,INVOICE_SEQ,PDF_INV_DTL,"
					+ "PRINT_BY_ORI,PRINT_BY_TRI,PRINT_BY_DUP,SAP_APPROVAL,FINANCIAL_YEAR,TO_CHAR(INVOICE_DT,'DD/MM/YYYY') "
					+ "FROM FMS_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND AGMT_NO=? AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_UNIT=? "
//					+ "AND FREQ=? "
					+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_STATE_TIN=? AND CARGO_NO=? AND INV_FLAG=? "
					+ "AND REF_NO=?";
			stmt5=conn.prepareStatement(queryString5);
			stmt5.setString(1, own_cd);
			stmt5.setString(2, countpty_cd);
			stmt5.setString(3, contno);
			stmt5.setString(4, agmtno);
			stmt5.setString(5, plant_seq);
			stmt5.setString(6, cont_type);
			stmt5.setString(7, bu_plant_seq);
			stmt5.setString(8, period_start_dt);	
			stmt5.setString(9, period_end_dt);
			stmt5.setString(10, state_code);
			stmt5.setString(11, cargo_no);
			stmt5.setString(12, inv_flag);
			stmt5.setString(13, invoice_no);
			rset5=stmt5.executeQuery();
			if(rset5.next())
			{
				inv_no=rset5.getString(1)==null?"":rset5.getString(1);
				checked_flag=rset5.getString(2)==null?"":rset5.getString(2);
				approved_flag=rset5.getString(3)==null?"":rset5.getString(3);
				inv_seq=rset5.getString(4)==null?"":rset5.getString(4);
				pdf_flg=rset5.getString(5)==null?"":rset5.getString(5);
				pdf_ori=rset5.getString(6)==null?"":rset5.getString(6);
				pdf_tri=rset5.getString(7)==null?"":rset5.getString(7);
				pdf_dup=rset5.getString(8)==null?"":rset5.getString(8);
				sap_approved_flag=rset5.getString(9)==null?"":rset5.getString(9);
				fiscal_yr=rset5.getString(10)==null?"":rset5.getString(10);
				inv_dt=rset5.getString(11)==null?"":rset5.getString(11);
				
				VINVOICE_EXIST.add("Y");
			}
			else
			{
				VINVOICE_EXIST.add("N");
			}
			rset5.close();
			stmt5.close();
			
			VINVOICE_DT.add(inv_dt);
			
			if(cont_type.equals("O") || cont_type.equals("Q"))
			{
				queryString5="SELECT IRN_NO "
						+ "FROM FMS_INVOICE_IRN_DTL "
						+ "WHERE COMPANY_CD=? AND INVOICE_NO=? ";
				stmt5=conn.prepareStatement(queryString5);
				stmt5.setString(1, comp_cd);
				stmt5.setString(2, inv_no);
				rset5=stmt5.executeQuery();
				if(rset5.next())
				{
					irn_no=rset5.getString(1)==null?"":rset5.getString(1);
				}
				rset5.close();
				stmt5.close();
				
				VIS_IRN_GENERATED.add(irn_no.equals("")?"N":"Y");
			}
			else
			{
				VIS_IRN_GENERATED.add("Y"); //other then LTCORA, Default 'Y'
			}
			
			
			VINVOICE_NO.add(inv_no);
			VINVOICE_SEQ.add(inv_seq);
			VINV_CHECKED_FLAG.add(checked_flag);
			VINV_APPROVED_FLAG.add(approved_flag);
			VPDF_INV_FLAG.add(pdf_flg);
			VSAP_APPROVAL_FLAG.add(sap_approved_flag);
			VFINANCIAL_YEAR.add(fiscal_yr);
			
			if(print_pdf_type.equals("O") && !pdf_ori.equals(""))
			{
				VPDF_TYPE.add(print_pdf_type);
			}
			else if(print_pdf_type.equals("D") && !pdf_dup.equals(""))
			{
				VPDF_TYPE.add(print_pdf_type);
			}
			else if(print_pdf_type.equals("T") && !pdf_tri.equals(""))
			{
				VPDF_TYPE.add(print_pdf_type);
			}
			else if(print_pdf_type.equals("All"))
			{
				String allPdfType="";
				allPdfType+=pdf_ori.equals("")?allPdfType.equals("")?"O":",O":"";
				allPdfType+=pdf_dup.equals("")?allPdfType.equals("")?"D":",D":"";
				allPdfType+=pdf_tri.equals("")?allPdfType.equals("")?"T":",T":"";
				
				if(allPdfType.equals(""))
				{
					allPdfType="All";
				}
				VPDF_TYPE.add(allPdfType);
			}
			else
			{
				VPDF_TYPE.add("");
			}
			
			if(view_pdf_type.equals("All"))
			{
				queryString6="SELECT COUNT(*) "
						+ "FROM FMS_INV_FILE_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
	 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
	 	        		+ "AND PDF_TYPE IN ('O','T','D') ";
				stmt6=conn.prepareStatement(queryString6);
				stmt6.setString(1, own_cd);
				stmt6.setString(2, state_code);
				stmt6.setString(3, inv_seq);
				//stmt6.setString(4, financial_year);
				stmt6.setString(4, fiscal_yr);
				rset6=stmt6.executeQuery();
				if(rset6.next())
				{
					if(rset6.getInt(1)>0)
					{
						VPDF_FILE_NAME.add("All");
						VPDF_FILE_PATH.add("");
					}
					else
					{
						VPDF_FILE_NAME.add("");
						VPDF_FILE_PATH.add("");
					}
				}
				else
				{
					VPDF_FILE_NAME.add("");
					VPDF_FILE_PATH.add("");
				}
				rset6.close();
				stmt6.close();
			}
			else
			{
				queryString6="SELECT FILE_NAME,PDF_SIGNED "
						+ "FROM FMS_INV_FILE_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
	 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
	 	        		+ "AND PDF_TYPE=?";
				stmt6=conn.prepareStatement(queryString6);
				stmt6.setString(1, own_cd);
				stmt6.setString(2, state_code);
				stmt6.setString(3, inv_seq);
				//stmt6.setString(4, financial_year);
				stmt6.setString(4, fiscal_yr);
				stmt6.setString(5, view_pdf_type);
				rset6=stmt6.executeQuery();
				if(rset6.next())
				{
					String pdf_signed=rset6.getString(2)==null?"":rset6.getString(2);
					
					VPDF_FILE_NAME.add(rset6.getString(1)==null?"":rset6.getString(1));
					if(pdf_signed.equals("Y"))
					{
						VPDF_FILE_PATH.add(CommonVariable.signed_sales_inv_path);
					}
					else
					{
						VPDF_FILE_PATH.add(CommonVariable.sales_inv_path);
					}
				}
				else
				{
					VPDF_FILE_NAME.add("");
					VPDF_FILE_PATH.add("");
				}
				rset6.close();
				stmt6.close();
			}
			
			if(mail_pdf_type.equals("All"))
			{
				String AllMailPdf="";
				String emailSent="";
				String emailSentInfo="";
				queryString6="SELECT PDF_TYPE,EMAIL_SENT "
						+ "FROM FMS_INV_FILE_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
	 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
	 	        		+ "AND PDF_SIGNED=? AND PDF_TYPE IN ('O','T','D') ";
				stmt6=conn.prepareStatement(queryString6);
				stmt6.setString(1, own_cd);
				stmt6.setString(2, state_code);
				stmt6.setString(3, inv_seq);
				//stmt6.setString(4, financial_year);
				stmt6.setString(4, fiscal_yr);
				stmt6.setString(5, "Y");
				rset6=stmt6.executeQuery();
				while(rset6.next())
				{
					String temp=rset6.getString(1)==null?"":rset6.getString(1);
					if(AllMailPdf.equals(""))
					{
						AllMailPdf=temp;
					}
					else
					{
						AllMailPdf+=","+temp;
					}

					String email_temp=rset6.getString(2)==null?"":rset6.getString(2);
					if(email_temp.equals("Y"))
					{
						emailSent="Y";
						emailSentInfo+=emailSentInfo.equals("")?temp:","+temp;
					}
				}
				rset6.close();
				stmt6.close();
				
				emailSentInfo=emailSentInfo.equals("")?"":emailSentInfo+" Sent";
				VPDF_SIGNED_FLAG.add("All");
				VSIGN_PDF_TYPE.add(AllMailPdf);
				VEMAIL_SENT.add(emailSent);
				VEMAIL_SENT_INFO.add(emailSentInfo);
			}
			else
			{
				queryString6="SELECT FILE_NAME,PDF_SIGNED,EMAIL_SENT "
						+ "FROM FMS_INV_FILE_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
	 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
	 	        		+ "AND PDF_TYPE=?";
				stmt6=conn.prepareStatement(queryString6);
				stmt6.setString(1, own_cd);
				stmt6.setString(2, state_code);
				stmt6.setString(3, inv_seq);
				//stmt6.setString(4, financial_year);
				stmt6.setString(4, fiscal_yr);
				stmt6.setString(5, view_pdf_type);
				rset6=stmt6.executeQuery();
				if(rset6.next())
				{
					String pdf_signed=rset6.getString(2)==null?"":rset6.getString(2);
					String email_sent=rset6.getString(3)==null?"":rset6.getString(3);
					VPDF_SIGNED_FLAG.add(pdf_signed);
					if(pdf_signed.equals("Y"))
					{
						VSIGN_PDF_TYPE.add(mail_pdf_type);
					}
					else
					{
						VSIGN_PDF_TYPE.add("");
					}
					VEMAIL_SENT.add(email_sent);
					if(email_sent.equals("Y"))
					{
						VEMAIL_SENT_INFO.add(mail_pdf_type+" Sent");
					}
					else
					{
						VEMAIL_SENT_INFO.add("");
					}
				}
				else
				{
					VPDF_SIGNED_FLAG.add("");
					VSIGN_PDF_TYPE.add("");
					VEMAIL_SENT.add("");
					VEMAIL_SENT_INFO.add("");
				}
				rset6.close();
				stmt6.close();
			}
			
			int re_print_count=isRePrintPDFRequested(comp_cd,state_code,inv_seq,fiscal_yr,"RLNG","REPRINT_PDF","A");
			VRE_PRINT_PDF.add(re_print_count>0?"Y":"N");
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getExistingLpInvoiceDtl()
	{
		String function_nm="getExistingLpInvoiceDtl()";
		try
		{
			queryString="SELECT BU_CONTACT_PERSON_CD,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),"
					+ "EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),INVOICE_AMT,NET_PAYABLE_AMT,"
					+ "REMARK_1,REMARK_2,TCS_TDS,TCS_AMT,TCS_FACTOR,TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,TCS_STRUCT_CD,TO_CHAR(TCS_EFF_DT,'DD/MM/YYYY'),"
					+ "TDS_GROSS_AMT,TDS_GROSS_PERCENT,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),MARKET_MARGIN,MARKET_MARGIN_AMT,OTHER_CHARGES,OTHER_CHARGES_AMT,"
					+ "TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(APPROVED_DT,'DD/MM/YYYY HH24:MI:SS'),FINANCIAL_YEAR,SUG_QTY,SUG_PERCENT,DISCOUNT_DAYS "
					+ "FROM FMS_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
					+ "AND BU_STATE_TIN=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND FINANCIAL_YEAR=? AND CARGO_NO=? AND INV_FLAG=? AND REF_NO=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, agmt_no);
			stmt.setString(5, plant_seq);
			stmt.setString(6, bu_unit);
			stmt.setString(7, contract_type);
			stmt.setString(8, bu_state_tin);
			stmt.setString(9, cont_start_dt);
			stmt.setString(10, cont_end_dt);
			stmt.setString(11, lp_financial_year);
			stmt.setString(12, cargo_no);
			stmt.setString(13, "LP");
			stmt.setString(14, ori_invoice_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				submission_chk=true;
				
				bu_contact_person_cd=rset.getString(1)==null?"0":rset.getString(1);
				contact_person_cd=rset.getString(2)==null?"0":rset.getString(2);
				invoice_seq=rset.getString(3)==null?"":rset.getString(3);
				invoice_no=rset.getString(4)==null?"":rset.getString(4);
				invoice_dt=rset.getString(5)==null?"":rset.getString(5);
				invoice_due_dt=rset.getString(6)==null?"":rset.getString(6);
				
				exchng_rate_cd=rset.getString(11)==null?"":rset.getString(11);
				exchang_rate_dt=rset.getString(12)==null?"":rset.getString(12);
				exchang_rate=rset.getString(13)==null?"":nf2.format(rset.getDouble(13));
				
				if(operation.equals("INSERT"))
				{
					qty_mmbtu=rset.getString(7)==null?"":nf.format(rset.getDouble(7));
					price=rset.getString(8)==null?"":nf.format(rset.getDouble(8));
					price_cd=rset.getString(9)==null?"":rset.getString(9);
					if(!price.equals(""))
					{
						price=utilBean.RateNumberFormat(rset.getDouble(8), price_cd);
					}
					gross_amt=rset.getString(10)==null?"":nf.format(rset.getDouble(10));
					
					invoice_raised_in=rset.getString(14)==null?"":rset.getString(14);
					gross_amt1=rset.getString(15)==null?"":nf.format(rset.getDouble(15));
					tax_amt=rset.getString(16)==null?"":nf.format(rset.getDouble(16));
					tax_struct_cd=rset.getString(17)==null?"":rset.getString(17);
					tax_struct_dt=rset.getString(18)==null?"":rset.getString(18);
					invoice_amt=rset.getString(19)==null?"":nf.format(rset.getDouble(19));
					net_payable=rset.getString(20)==null?"":nf.format(rset.getDouble(20));
					
					applicable_abbr=rset.getString(23)==null?"":rset.getString(23);
					applicable_amt=rset.getString(24)==null?"":rset.getString(24);
					TCS_factor=rset.getString(25)==null?"":rset.getString(25);
					
					tax_struct_dtl=utilBean.getTaxDescr(conn, tax_struct_cd);
					
					transportation_charges=rset.getString(26)==null?"":nf2.format(rset.getDouble(26));
					transportation_amount=rset.getString(27)==null?"":nf.format(rset.getDouble(27));
					
					tcs_struct_cd=rset.getString(28)==null?"":rset.getString(28);
					tcs_struct_dt=rset.getString(29)==null?"":rset.getString(29);
					
					tds_amt=rset.getString(30)==null?"":nf.format(rset.getDouble(30));
					tds_factor=rset.getString(31)==null?"":nf.format(rset.getDouble(31));
					tds_struct_cd=rset.getString(32)==null?"":rset.getString(32);
					tds_struct_dt=rset.getString(33)==null?"":rset.getString(33);
					
					marketing_margin=rset.getString(34)==null?"":nf2.format(rset.getDouble(34));
					marketing_margin_amount=rset.getString(35)==null?"":nf.format(rset.getDouble(35));
					other_charges=rset.getString(36)==null?"":nf2.format(rset.getDouble(36));
					other_charges_amount=rset.getString(37)==null?"":nf.format(rset.getDouble(37));
					inv_entered_on = rset.getString(38)==null?"":rset.getString(38);
					inv_approved_on = rset.getString(39)==null?"":rset.getString(39);
					System.out.println("gross_amt1 :: "+ gross_amt1);
					System.out.println("invoice_amt :: "+ invoice_amt);
					System.out.println("net_payable :: "+ net_payable);
					String fiscal_yr=rset.getString(40)==null?"":rset.getString(40);
					
					gross_include_transport_tariff=nf.format(Double.parseDouble(gross_amt1));
					if(!transportation_amount.equals(""))
					{
						gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(transportation_amount));
						isGrossIncTriff=true;
					}
					if(!marketing_margin_amount.equals(""))
					{
						gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(marketing_margin_amount));
						isGrossIncTriff=true;
					}
					if(!other_charges_amount.equals(""))
					{
						gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(other_charges_amount));
						isGrossIncTriff=true;
					}
					
					Vector VTAX_CODE = new Vector();
					Vector VTAX_DESCR = new Vector();
					Vector VTAX_AMT = new Vector();
					Vector VTAX_BASE_AMT = new Vector();
					Vector VTAX_FACTOR = new Vector();
					queryString1="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
							+ "FROM FMS_INV_TAX_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, bu_state_tin);
					//stmt1.setString(3, financial_year);
					stmt1.setString(3, fiscal_yr);
					stmt1.setString(4, invoice_seq);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						VTAX_CODE.add(rset1.getString(1)==null?"":rset1.getString(1));
						VTAX_DESCR.add(rset1.getString(2)==null?"":rset1.getString(2));
						VTAX_AMT.add(rset1.getString(3)==null?"":nf.format(rset1.getDouble(3)));
						VTAX_BASE_AMT.add(rset1.getString(4)==null?"":nf.format(rset1.getDouble(4)));
						VTAX_FACTOR.add("");
					}
					rset1.close();
					stmt1.close();
					
					Vector VTEMP_TAX_DTL = new Vector();
					
					VTEMP_TAX_DTL.add(VTAX_CODE);
					VTEMP_TAX_DTL.add(VTAX_DESCR);
					VTEMP_TAX_DTL.add(VTAX_AMT);
					VTEMP_TAX_DTL.add(VTAX_BASE_AMT);
					VTEMP_TAX_DTL.add(VTAX_FACTOR);
					
					VMULTI_TAX_STRUCT.add(VTEMP_TAX_DTL);
					
					queryString1="SELECT TO_CHAR(STORAGE_DT,'DD/MM/YYYY'),OPEN_BALANCE_QTY,OFFTAKE_QTY,RATE,RATE_TYPE,DAY_DISCOUNT "
							+ "FROM FMS_INV_STORAGE_CRG_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=?";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, bu_state_tin);
					//stmt1.setString(3, financial_year);
					stmt1.setString(3, fiscal_yr);
					stmt1.setString(4, invoice_seq);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						String storageInv=rset1.getString(2)==null?"":nf.format(rset1.getDouble(2));
						VSTORAGE_DATE.add(rset1.getString(1)==null?"":rset1.getString(1));
						VSTORAGE_INVENTORY.add(storageInv);
						VOFFTAKE_QTY.add(rset1.getString(3)==null?"":nf.format(rset1.getDouble(3)));
						
						String rate=rset1.getString(4)==null?"":utilBean.RateNumberFormat(rset1.getDouble(4),price_cd);
						String rate_type=rset1.getString(5)==null?"":rset1.getString(5);
						String discount_day=rset1.getString(6)==null?"":rset1.getString(6);
						VRATE_TYPE.add(rate_type);
						VUSER_DEFINE.add(rate_type.equals("U")?rate:"");
						VSTORAGE_CHARGE.add(rate_type.equals("U")?"":rate);
						
						String storage_amt="";
						if(!storageInv.equals("") && !rate.equals("") && discount_day.equals("N"))
						{
							storage_amt=nf.format(Double.parseDouble(storageInv) * Double.parseDouble(rate));
						}
						
						VSTORAGE_AMT.add(storage_amt);
						VDISCOUNT_FLAG.add(discount_day);
					}
					rset1.close();
					stmt1.close();
					
					queryString1="SELECT DISTINCT SEC_INT_REF "
							+ "FROM FMS_INV_ADV_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? "
							+ "ORDER BY SEC_INT_REF";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, bu_state_tin);
					//stmt1.setString(3, financial_year);
					stmt1.setString(3, fiscal_yr);
					stmt1.setString(4, invoice_seq);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						VEXISTING_RECEIPT_VOUCHER.add(rset1.getString(1)==null?"":rset1.getString(1));
						if(operation.equals("INSERT") && submission_chk)
						{
							VRECEIPT_VOUCHER_MST.add(rset1.getString(1)==null?"":rset1.getString(1));
						}
					}
					rset1.close();
					stmt1.close();
				}
				
				if(inv_flag.equals("UG"))
				{
					qty_mmbtu=rset.getString(7)==null?"":nf.format(rset.getDouble(7));
					price=rset.getString(8)==null?"":nf.format(rset.getDouble(8));
					price_cd=rset.getString(9)==null?"":rset.getString(9);
					if(!price.equals(""))
					{
						price=utilBean.RateNumberFormat(rset.getDouble(8), price_cd);
					}
					
					sug_qty=rset.getString(41)==null?"":nf.format(rset.getDouble(41));
					sug_percentage=rset.getString(42)==null?"":nf.format(rset.getDouble(42));
					
				}
				
				remark_1=rset.getString(21)==null?"":rset.getString(21);
				//remark_2=rset.getString(22)==null?"":rset.getString(22);
				discount_days = rset.getString(43)==null?"0":rset.getString(43);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getRefrenceInvoiceDtl()
	{
		String function_nm="getRefrenceInvoiceDtl()";
		try
		{
			queryString="SELECT TO_CHAR(DUE_DT,'DD/MM/YYYY'),NET_PAYABLE_AMT,PAY_RECV_AMT,TO_CHAR(PAY_RECV_DT,'DD/MM/YYYY'),TO_CHAR(INVOICE_DT,'DD-MON-YY'), "
					+ "GROSS_AMT,TAX_AMT,TCS_TDS,TCS_AMT,TCS_FACTOR,TDS_GROSS_AMT,TDS_GROSS_PERCENT "
					+ "FROM FMS_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
					+ "AND BU_STATE_TIN=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND FINANCIAL_YEAR=? AND CARGO_NO=? AND INV_FLAG=? "
					+ "AND INVOICE_SEQ = ?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, agmt_no);
			stmt.setString(5, plant_seq);
			stmt.setString(6, bu_unit);
			stmt.setString(7, contract_type);
			stmt.setString(8, bu_state_tin);
			stmt.setString(9, period_start_dt);
			stmt.setString(10, period_end_dt);
			stmt.setString(11, exist_financial_year);
			stmt.setString(12, cargo_no);
			stmt.setString(13, ori_inv_flag);
			stmt.setString(14, ori_invoice_seq);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				ori_inv_due_dt=rset.getString(1)==null?"":rset.getString(1);
				ori_inv_net_amt=rset.getString(2)==null?"":nf.format(rset.getDouble(2));
				ori_inv_payrecv_amt=rset.getString(3)==null?"":nf.format(rset.getDouble(3));
				ori_inv_payrecv_dt=rset.getString(4)==null?"":rset.getString(4);
				ori_inv_dt=rset.getString(5)==null?"":rset.getString(5);
				ori_gross_amt= rset.getString(6)==null?"":nf.format(rset.getDouble(6));
				ori_tax_amt= rset.getString(7)==null?"":nf.format(rset.getDouble(7));
				ori_tcs_tds= rset.getString(8)==null?"":rset.getString(8);
				ori_tcs_amt= rset.getString(9)==null?"":nf.format(rset.getDouble(9));
				ori_tcs_per= rset.getString(10)==null?"":nf.format(rset.getDouble(10));
				ori_tds_amt= rset.getString(11)==null?"":nf.format(rset.getDouble(11));
				ori_tds_per= rset.getString(12)==null?"":nf.format(rset.getDouble(12));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public String getTotalAnnualInterestRate(String int_cal_rate_value,String int_cal_sign,String int_cal_percentage) 
	{
		double total = 0;

		if(!int_cal_rate_value.isEmpty() &&
				!int_cal_sign.isEmpty() &&
				!int_cal_percentage.isEmpty())
		{
			double rateValue = Double.parseDouble(int_cal_rate_value);
		    double percentage = Double.parseDouble(int_cal_percentage);

		    // Apply the sign (+ or -)
		    if ("+".equals(int_cal_sign))
		    {
		        total = rateValue + percentage;
		    } else if ("-".equals(int_cal_sign))
		    {
		        total = rateValue - percentage;
		    }
		}

	    return String.format("%.2f", total);
	}
	

	public void getLpInvoiceDetail()
	{
		String function_nm="getLpInvoiceDetail()";
		try
		{
			String ori_inv_no= "",int_rate="";
			queryString="SELECT BU_CONTACT_PERSON_CD,CONTACT_PERSON_CD,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD-MON-YY'),"
					+ "TO_CHAR(DUE_DT,'DD-MON-YY'),TO_CHAR(PERIOD_START_DT,'DD-MON-YY'),TO_CHAR(PERIOD_END_DT,'DD-MON-YY'),REMARK_1,REMARK_2,"
					+ "ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_VALUE,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),"
					+ "INVOICE_AMT,NET_PAYABLE_AMT,TCS_TDS,TCS_AMT,TCS_FACTOR,CHECKED_FLAG,APPROVED_FLAG, "
					+ "INVOICE_ID_SEQ,TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),EXCHG_RATE_TYPE,"
					+ "INVOICE_RAISED_IN,MARKET_MARGIN,MARKET_MARGIN_AMT,OTHER_CHARGES,OTHER_CHARGES_AMT,SUG_QTY,SUG_PERCENT,DISCOUNT_DAYS,REF_NO,INT_RATE "
					+ "FROM FMS_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
					+ "AND BU_STATE_TIN=? "
					+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND FINANCIAL_YEAR=? "
					+ "AND INVOICE_SEQ=? AND CARGO_NO=? AND INV_FLAG=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, agmt_no);
			stmt.setString(5, plant_seq);
			stmt.setString(6, bu_unit);
			stmt.setString(7, contract_type);
			stmt.setString(8, bu_state_tin);
			stmt.setString(9, cont_start_dt);
			stmt.setString(10, cont_end_dt);
			stmt.setString(11, financial_year);
			stmt.setString(12, invoice_seq);
			stmt.setString(13, cargo_no);
			stmt.setString(14, inv_flag);
			
			rset=stmt.executeQuery();
			if(rset.next())
			{
				bu_contact_person_cd=rset.getString(1)==null?"0":rset.getString(1);
				contact_person_cd=rset.getString(2)==null?"0":rset.getString(2);
				
				invoice_id_seq=rset.getString(26)==null?"":rset.getString(26);
				invoice_no=rset.getString(3)==null?"":rset.getString(3);
				invoice_dt=rset.getString(4)==null?"":rset.getString(4);
				invoice_due_dt=rset.getString(5)==null?"":rset.getString(5);
				inv_period_start_dt=rset.getString(6)==null?"":rset.getString(6);
				inv_period_end_dt=rset.getString(7)==null?"":rset.getString(7);
				
				remark_1=rset.getString(8)==null?"":rset.getString(8);
				remark_2=rset.getString(9)==null?"":rset.getString(9);
				
				qty_mmbtu=nf.format(rset.getDouble(10));
				price_cd=rset.getString(12)==null?"":rset.getString(12);
				price=utilBean.RateNumberFormat(rset.getDouble(11), price_cd);
				gross_amt=nf.format(rset.getDouble(13));
				exchang_rate=nf2.format(rset.getDouble(14));
				gross_amt1=nf.format(rset.getDouble(15));
				tax_amt=nf.format(rset.getDouble(16));
				tax_struct_cd=rset.getString(17)==null?"":rset.getString(17);
				tax_struct_dt=rset.getString(18)==null?"":rset.getString(18);
				invoice_amt=nf.format(rset.getDouble(19));
				net_payable=nf.format(rset.getDouble(20));
				applicable_abbr=rset.getString(21)==null?"":rset.getString(21);
				applicable_amt=rset.getString(22)==null?"":rset.getString(22);
				TCS_factor=rset.getString(23)==null?"":rset.getString(23);
				if(!TCS_factor.equals(""))
				{
					TCS_factor=nf3.format(rset.getDouble(23));
				}
				
				if(activityFlag.equals("CHECK")) {
					activity_value=rset.getString(24)==null?"":rset.getString(24);
				}else if(activityFlag.equals("APPROVE")) {
					activity_value=rset.getString(25)==null?"":rset.getString(25);
				}
				
				//tax_struct_dtl=utilBean.getEntityTaxStructureDtl(conn,comp_cd, counterparty_cd, "C", plant_seq, bu_unit, period_start_dt);
				tax_struct_dtl=utilBean.getTaxDescr(conn, tax_struct_cd);
				
				transportation_charges=rset.getString(27)==null?"":nf2.format(rset.getDouble(27));
				transportation_amount=rset.getString(28)==null?"":nf.format(rset.getDouble(28));
				
				exchng_rate_cd=rset.getString(29)==null?"":rset.getString(29);
				exchang_rate_dt=rset.getString(30)==null?"":rset.getString(30);
				exchang_rate_type=rset.getString(31)==null?"":rset.getString(31);
				invoice_raised_in=rset.getString(32)==null?"":rset.getString(32);
				
				marketing_margin=rset.getString(33)==null?"":nf2.format(rset.getDouble(33));
				marketing_margin_amount=rset.getString(34)==null?"":nf.format(rset.getDouble(34));
				other_charges=rset.getString(35)==null?"":nf2.format(rset.getDouble(35));
				other_charges_amount=rset.getString(36)==null?"":nf.format(rset.getDouble(36));
				
				sug_qty=rset.getString(37)==null?"":nf.format(rset.getDouble(37));
				sug_percentage=rset.getString(38)==null?"":nf.format(rset.getDouble(38));
				
				discount_days=rset.getString(39)==null?"0":rset.getString(39);
				ori_inv_no=rset.getString(40)==null?"":rset.getString(40);
				int_rate=rset.getString(41)==null?"":nf.format(rset.getDouble(41));
				
				gross_include_transport_tariff=nf.format(Double.parseDouble(gross_amt1));
				if(!transportation_amount.equals(""))
				{
					if(Double.parseDouble(transportation_amount) > 0)
					{
						gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(transportation_amount));
						isGrossIncTriff=true;
					}
				}
				if(!marketing_margin_amount.equals(""))
				{
					if(Double.parseDouble(marketing_margin_amount) > 0)
					{
						gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(marketing_margin_amount));
						isGrossIncTriff=true;
					}
				}
				if(!other_charges_amount.equals(""))
				{
					if(Double.parseDouble(other_charges_amount) > 0)
					{
						gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(other_charges_amount));
						isGrossIncTriff=true;
					}
				}
				
				/*queryString1="SELECT TAX_STRUCT_DTL "
						+ "FROM FMS_ENTITY_TAX_STRUCT_DTL A "
						+ "WHERE A.COMPANY_CD='"+comp_cd+"' AND A.TAX_STRUCT_CD='"+tax_struct_cd+"' "
						+ "AND A.ENTITY='C' AND A.COUNTERPARTY_CD='"+counterparty_cd+"' AND A.PLANT_SEQ_NO='"+plant_seq+"' AND A.BU_UNIT='"+bu_unit+"' "
						+ "AND A.TAX_STRUCT_DT=(SELECT MAX(D.TAX_STRUCT_DT) FROM FMS_ENTITY_TAX_STRUCT_DTL D WHERE A.COMPANY_CD=D.COMPANY_CD "
						+ "AND A.ENTITY=D.ENTITY AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.PLANT_SEQ_NO=D.PLANT_SEQ_NO "
						+ "AND D.TAX_STRUCT_DT <= TO_DATE('"+tax_struct_dt+"','DD/MM/YYYY') AND A.BU_UNIT=D.BU_UNIT) ";
				rset1=stmt1.executeQuery(queryString1);
				if(rset1.next())
				{
					tax_struct_dtl=rset1.getString(1)==null?"":rset1.getString(1);
				}*/
			}
			rset.close();
			stmt.close();
			
			String boe_number="";
			String boe_date="";
			String ship_nm="";
			
			if(contract_type.equals("O") || contract_type.equals("Q"))
			{
				queryString="SELECT A.CONT_REF_NO,TO_CHAR(A.DDA_DT,'DD-MON-YY'),A.CONT_REF_NO,B.SHIP_CD,B.BOE_NO,TO_CHAR(B.BOE_DT,'DD-MON-YY'),TO_CHAR(SIGNING_DT,'DD-MON-YY'),"
						+ "B.CSOC_QTY,A.MDCQ_PERCENTAGE,TO_CHAR(B.ACTUAL_RECPT_DT,'DD-MON-YY') "
						+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_CARGO_DTL B "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? "
						+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.AGMT_TYPE=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND B.CARGO_NO=? "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
						+ ""
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, "C");
				stmt.setString(4, agmt_no);
				stmt.setString(5, agmt_rev_no);
				stmt.setString(6, "A");
				stmt.setString(7, cont_no);
				stmt.setString(8, contract_type);
				stmt.setString(9, cargo_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					contRef=rset.getString(1)==null?"":rset.getString(1);
					dda_dt=rset.getString(2)==null?"":rset.getString(2);
					//cargoRef=rset.getString(3)==null?"":rset.getString(3);
					
					String ship_cd=rset.getString(4)==null?"":rset.getString(4);
					ship_nm=utilBean.getShipName(conn,ship_cd);
					boe_number = rset.getString(5)==null?"":rset.getString(5);
					boe_date=rset.getString(6)==null?"":rset.getString(6);
					
					signingDt=rset.getString(7)==null?"":rset.getString(7);
					dcq=rset.getString(8)==null?"":rset.getString(8);
					mdcq_percentage=rset.getDouble(9);
					if(Double.doubleToRawLongBits(mdcq_percentage)==Double.doubleToRawLongBits(0))
					{
						mdcq_percentage=100;
					}
					
					arrivalDt=rset.getString(10)==null?"":rset.getString(10);
				}
				rset.close();
				stmt.close();
				
				queryString1="SELECT TO_CHAR(SIGNING_DT,'DD-MON-YY')  "
						+ "FROM FMS_LTCORA_AGMT_MST A "
						+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
						+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_LTCORA_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.BUY_SALE=B.BUY_SALE) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, "A");
				stmt1.setString(3, agmt_no);
				stmt1.setString(4, counterparty_cd);
				stmt1.setString(5, "C");
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					agmtSigningDt=rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
			}
			else
			{
				queryString1="SELECT CONT_REF_NO,TO_CHAR(DDA_DT,'DD-MON-YY'),TRADE_REF_NO,TO_CHAR(SIGNING_DT,'DD-MON-YY'),AGMT_BASE,DCQ,MDCQ_PERCENTAGE  "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND CONT_NO=? AND AGMT_NO=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, contract_type);
				stmt1.setString(3, cont_no);
				stmt1.setString(4, agmt_no);
				stmt1.setString(5, counterparty_cd);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					contRef=rset1.getString(1)==null?"":rset1.getString(1);
					dda_dt=rset1.getString(2)==null?"":rset1.getString(2);
					String tradeRef=rset1.getString(3)==null?"":rset1.getString(3);
					signingDt=rset1.getString(4)==null?"":rset1.getString(4);
					agmt_base=rset1.getString(5)==null?"":rset1.getString(5);
					dcq=rset1.getString(6)==null?"":rset1.getString(6);
					
					mdcq_percentage=rset1.getDouble(7);
					if(Double.doubleToRawLongBits(mdcq_percentage)==Double.doubleToRawLongBits(0))
					{
						mdcq_percentage=100;
					}
					
					if(contract_type.equals("X"))
					{
						contRef=tradeRef;
					}
				}
				rset1.close();
				stmt1.close();
				
				if(contract_type.equals("S"))
				{
					queryString2="SELECT TO_CHAR(SIGNING_DT,'DD-MON-YY')  "
							+ "FROM FMS_AGMT_MST A "
							+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
							+ "AND AGMT_NO=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) ";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, "F");
					stmt2.setString(3, agmt_no);
					stmt2.setString(4, counterparty_cd);
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						agmtSigningDt=rset2.getString(1)==null?"":rset2.getString(1);
					}
					rset2.close();
					stmt2.close();
				}
			}
			
			queryString2="SELECT CONTACT_PERSON "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND SEQ_NO=? AND ADDR_FLAG=? AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.TYPE=B.TYPE "
					+ "AND EFF_DT <= TO_DATE(?,'DD-MON-YY'))";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, counterparty_cd);
			stmt2.setString(3, "C");
			stmt2.setString(4, contact_person_cd);
			stmt2.setString(5, "P"+plant_seq);
			stmt2.setString(6, "RLNG");
			stmt2.setString(7, invoice_dt);
			rset2=stmt2.executeQuery();
			if(rset2.next())
			{
				contact_person_nm=rset2.getString(1)==null?"":rset2.getString(1);
			}
			rset2.close();
			stmt2.close();
			
			queryString3="SELECT CONTACT_PERSON "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND SEQ_NO=? AND ADDR_FLAG=? AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.TYPE=B.TYPE "
					+ "AND EFF_DT <= TO_DATE(?,'DD-MON-YY'))";
			stmt3=conn.prepareStatement(queryString3);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, comp_cd);
			stmt3.setString(3, "B");
			stmt3.setString(4, bu_contact_person_cd);
			stmt3.setString(5, "P"+bu_unit);
			stmt3.setString(6, "RLNG");
			stmt3.setString(7, invoice_dt);
			rset3=stmt3.executeQuery();
			if(rset3.next())
			{
				bu_contact_person_nm=rset3.getString(1)==null?"":rset3.getString(1);
			}
			rset3.close();
			stmt3.close();
			
			HashMap bu_plant_detail=utilBean.getCounterpartyPlantDetail(conn,comp_cd, "B", comp_cd, bu_unit);
			bu_plantAddress=""+bu_plant_detail.get("plant_address");
			bu_plantCity=""+bu_plant_detail.get("plant_city");
			bu_plantState=""+bu_plant_detail.get("plant_state");
			bu_plantPin=""+bu_plant_detail.get("plant_pin");
			bu_plantNm=""+bu_plant_detail.get("plant_name");
			
			HashMap plant_detail=utilBean.getCounterpartyPlantDetail(conn,comp_cd, "C", counterparty_cd, plant_seq);
			plantAddress=""+plant_detail.get("plant_address");
			plantCity=""+plant_detail.get("plant_city");
			plantState=""+plant_detail.get("plant_state");
			plantPin=""+plant_detail.get("plant_pin");
			
			if(contract_type.equals("O") || contract_type.equals("Q"))
			{
				tax_info="State : "+plantState;
				tax_info+="<br>State Code : "+utilBean.getState_TIN(conn, comp_cd, counterparty_cd, "C", plant_seq);
				//String temp_tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "C", counterparty_cd, plant_seq);
				//tax_info+=temp_tax_info.replaceAll("\n", "<br>");
				
				queryString = "SELECT A.STAT_NO, TO_CHAR(A.EFF_DT,'DD/MM/YYYY'), B.STAT_NM "
						+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B "
						+ "WHERE A.COUNTERPARTY_CD=? AND A.ENTITY=? "
						+ "AND A.PLANT_SEQ_NO=? AND A.COMPANY_CD=? "
						+ "AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL AND B.STAT_CD IN ('1003','1001')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, counterparty_cd);
				stmt.setString(2, "C");
				stmt.setString(3, plant_seq);
				stmt.setString(4, comp_cd);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					String no = rset.getString(1)==null?"":rset.getString(1);
					String nm = rset.getString(3)==null?"":rset.getString(3);
	
					tax_info+="<br>"+nm+" : "+no;
				}
				stmt.close();
				rset.close();
				
				bu_tax_info="State : "+bu_plantState;
				bu_tax_info+="<br>State Code : "+utilBean.getState_TIN(conn, comp_cd, comp_cd, "B", bu_unit);
				//String temp_bu_tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "B", comp_cd, bu_unit);
				//bu_tax_info+=temp_bu_tax_info.replaceAll("\n", "<br>");
				
				queryString = "SELECT A.STAT_NO, TO_CHAR(A.EFF_DT,'DD/MM/YYYY'), B.STAT_NM "
						+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B "
						+ "WHERE A.COUNTERPARTY_CD=? AND A.ENTITY=? "
						+ "AND A.PLANT_SEQ_NO=? AND A.COMPANY_CD=? "
						+ "AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL AND B.STAT_CD IN ('1003','1001')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, "B");
				stmt.setString(3, bu_unit);
				stmt.setString(4, comp_cd);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					String no = rset.getString(1)==null?"":rset.getString(1);
					String nm = rset.getString(3)==null?"":rset.getString(3);
	
					bu_tax_info+="<br>"+nm+" : "+no;
				}
				stmt.close();
				rset.close();
				
				bu_tax_info+="<br>SAC : 999799"
						+ "<br>Description of Service : Other Miscellaneous services - Other Services n.e.c."
						+ "<br>Place Of Supply : "+plantState;
			}
			else
			{
				tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "C", counterparty_cd, plant_seq);
				tax_info=tax_info.replaceAll("\n", "<br>");
				
				bu_tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "B", comp_cd, bu_unit);
				bu_tax_info=bu_tax_info.replaceAll("\n", "<br>");
			}
			
			couterpty_nm=""+utilBean.getCounterpartyName(conn,counterparty_cd);
			int srno=0;
			
			String parameter="counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&cont_no="+cont_no+""
					+ "&cont_rev="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev_no+"&plant_seq="+plant_seq+"&billing_cycle="+billing_cycle+""
					+ "&period_start_dt="+period_start_dt+"&period_end_dt="+period_end_dt+"&bu_unit="+bu_unit+"&financial_year="+financial_year+""
					+ "&bu_state_tin="+bu_state_tin+"&invoice_seq="+invoice_seq+"&activityFlag="+activityFlag+"&cargo_no="+cargo_no+"&inv_flag="+inv_flag;

			price_cd_nm=""+utilBean.getRateUnitNm(conn,price_cd);
			
			float int_days = utilDate.getDays(ori_inv_payrecv_dt, ori_inv_due_dt)-1 - Float.parseFloat(discount_days);
			
			srno+=1;
			VPDF_COL1.add(srno);
			VPDF_COL2.add("Delayed Payment Invoice Generated Against Invoice No : <br><b>"+ori_inv_no+"</b> Dated " +ori_inv_dt );
			VPDF_COL3.add("");
			VPDF_COL4.add("");
			VPDF_COL5.add("");
			VPDF_COL6.add("");
			VPDF_COL7.add("");
			
			srno+=1;
			VPDF_COL1.add(srno);
			VPDF_COL2.add("Net Amount");
			VPDF_COL3.add(""); 
			VPDF_COL4.add("INR");
			VPDF_COL5.add("");
			VPDF_COL6.add("");
			VPDF_COL7.add(ori_inv_net_amt);
			
			srno+=1;
			VPDF_COL1.add(srno);
			VPDF_COL2.add("Due Date");
			VPDF_COL3.add(ori_inv_due_dt);
			VPDF_COL4.add("");
			VPDF_COL5.add("");
			VPDF_COL6.add("");
			VPDF_COL7.add("");
				
			srno+=1;
			VPDF_COL1.add(srno);
			VPDF_COL2.add("Payment Received Date");
			VPDF_COL3.add(ori_inv_payrecv_dt);
			VPDF_COL4.add("");
			VPDF_COL5.add("");
			VPDF_COL6.add("");
			VPDF_COL7.add(ori_inv_payrecv_amt);
			
			srno+=1;
			VPDF_COL1.add(srno);
			VPDF_COL2.add("No Of Days For Late Payment Invoice");
			VPDF_COL3.add(nf.format(int_days) + " Days");
			VPDF_COL4.add("");
			VPDF_COL5.add("");
			VPDF_COL6.add("");
			VPDF_COL7.add("");
			
			srno+=1;
			VPDF_COL1.add(srno);
			VPDF_COL2.add("Interest Rate");
			VPDF_COL3.add("");
			VPDF_COL4.add("");
			VPDF_COL5.add("");
			VPDF_COL6.add(int_rate + " %");
			VPDF_COL7.add("");
			
			srno+=1;
			VPDF_COL1.add(srno);
			VPDF_COL2.add("Payable Amount");
			VPDF_COL3.add("");
			VPDF_COL4.add("INR");
			VPDF_COL5.add("");
			VPDF_COL6.add("");
			VPDF_COL7.add(invoice_amt);
			
			if(contract_type.equals("O") || contract_type.equals("Q")) {
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("Tax ("+tax_struct_dtl+")");
				VPDF_COL3.add("");
				VPDF_COL4.add("INR");
				VPDF_COL5.add("");
				VPDF_COL6.add("");
				VPDF_COL7.add(tax_amt);
				
				double temp_srno=srno;
				queryString1="SELECT COUNT(*) "
						+ "FROM FMS_INV_TAX_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
						+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, bu_state_tin);
				stmt1.setString(3, financial_year);
				stmt1.setString(4, invoice_seq);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					if(rset1.getInt(1)>1)
					{
						queryString="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
								+ "FROM FMS_INV_TAX_DTL "
								+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
								+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? ";
						stmt=conn.prepareStatement(queryString);
						stmt.setString(1, comp_cd);
						stmt.setString(2, bu_state_tin);
						stmt.setString(3, financial_year);
						stmt.setString(4, invoice_seq);
						rset=stmt.executeQuery();
						while(rset.next())
						{
							temp_srno=temp_srno+0.1;
							VPDF_COL1.add(nf0.format(temp_srno));
							VPDF_COL2.add(rset.getString(2)==null?"":rset.getString(2));
							VPDF_COL3.add("");
							VPDF_COL4.add("INR");
							VPDF_COL5.add("");
							VPDF_COL6.add("");
							VPDF_COL7.add(rset.getString(3)==null?"":nf.format(rset.getDouble(3)));
						}
						rset.close();
						stmt.close();
					}
				}
				rset1.close();
				stmt1.close();
			}
			
			if(applicable_abbr.equals("TCS"))
			{
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("TCS");
				VPDF_COL3.add("");
				VPDF_COL4.add("INR");
				VPDF_COL5.add("");
				VPDF_COL6.add(TCS_factor+"%");
				VPDF_COL7.add(applicable_amt);
			}
			
			
			srno+=1;
			VPDF_COL1.add(srno);
			VPDF_COL2.add("Net Amount Payable");
			VPDF_COL3.add("");
			VPDF_COL4.add("INR");
			VPDF_COL5.add("");
			VPDF_COL6.add("");
			VPDF_COL7.add(net_payable);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getLpInvoiceNumber()
	{
		String function_nm="getLpInvoiceNumber()";
		try
		{
			String invoice_prefix=utilBean.getInvoicePrefix(conn,comp_cd);
			
			String fin_yr="";
			if(!financial_year.equals(""))
			{
				String[] temp = financial_year.split("-");
				fin_yr=temp[0].substring(2,temp[0].length())+"-"+temp[1].substring(2,temp[1].length());
			}
			
			String state_abbr="";
			String state_code=bu_state_tin;
			if(state_code.length()<=1)
			{
				state_code="0"+state_code;
			}
			
			if(!invoice_id_seq.equals(""))
			{
				VINVOICE_ID_SEQ.add(invoice_id_seq);
				VINVOICE_NO.add(invoice_no);
			}
			
			String contType="";
			String invSeries="";
			String ff_invType="";
			String invFlags="''";
			if(contract_type.equals("Q") || contract_type.equals("O"))
			{
				contType="'Q','O'";
				invSeries="L";
				invFlags="'LP'";
				ff_invType="'LP'";
			}
			else
			{
				contType="'S','L','X','E','W','F'";
				invSeries="";
				invFlags="'LP'";
				ff_invType="'LP'";
			}
			invSeries += "LP";
			
			if(!invoice_prefix.equals(""))
			{
				int no_inv_no=10;
				for(int i=1;i<=no_inv_no;i++)
				{
					String invoice_id_seq=""+i;
					int count=0;
					queryString="SELECT COUNT(*) "
							+ "FROM FMS_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND BU_STATE_TIN=? AND INVOICE_ID_SEQ=? AND CONTRACT_TYPE IN ("+contType+") "
							+ "AND INV_FLAG IN ("+invFlags+")";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, financial_year);
					stmt.setString(3, bu_state_tin);
					stmt.setString(4, invoice_id_seq);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						count += rset.getInt(1);
					}
					rset.close();
					stmt.close();
					
					if(!invSeries.equals("LLP")) {
						
						queryString="SELECT COUNT(*) "
								+ "FROM FMS_DLNG_INVOICE_MST "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
								+ "AND BU_STATE_TIN=? AND INVOICE_ID_SEQ=? AND CONTRACT_TYPE IN ("+contType+") "
								+ "AND INV_FLAG IN ("+invFlags+")";
						stmt=conn.prepareStatement(queryString);
						stmt.setString(1, comp_cd);
						stmt.setString(2, financial_year);
						stmt.setString(3, bu_state_tin);
						stmt.setString(4, invoice_id_seq);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							count += rset.getInt(1);
						}
						rset.close();
						stmt.close();
					}
					
					queryString1="SELECT COUNT(*) "
							+ "FROM FMS_FFLOW_INV_MST "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_TYPE IN ("+ff_invType+") "
							+ "AND INVOICE_ID_SEQ=? ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, bu_state_tin);
					stmt1.setString(3, financial_year);
					stmt1.setString(4, invoice_id_seq);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						count += rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					
					if(invSeries.equals("LLP"))
					{
						queryString1="SELECT COUNT(*) "
								+ "FROM FMS_DLNG_FFLOW_INV_MST "
								+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
								+ "AND FINANCIAL_YEAR=? "
								+ "AND INVOICE_TYPE IN ('LP') "
								+ "AND INVOICE_ID_SEQ=? ";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, bu_state_tin);
						stmt1.setString(3, financial_year);
						stmt1.setString(4, invoice_id_seq);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							count += rset1.getInt(1);
						}
						rset1.close();
						stmt1.close();
					}
					
					if(count==0)
					{	
						queryString2="SELECT STATE_CODE "
								+ "FROM FMS_STATE_MST "
								+ "WHERE TIN=?";
						stmt2=conn.prepareStatement(queryString2);
						stmt2.setString(1, state_code);
						rset2=stmt2.executeQuery();
						if(rset2.next())
						{
							state_abbr=rset2.getString(1)==null?"":rset2.getString(1);
						}
						rset2.close();
						stmt2.close();
						String invoice_no="";
						invoice_no=invoice_prefix+""+invSeries+""+state_abbr+""+utilBean.PrePaddingZero(invoice_id_seq, 4)+"/"+fin_yr;
						
						VINVOICE_ID_SEQ.add(invoice_id_seq);
						VINVOICE_NO.add(invoice_no);
					}
					else
					{
						no_inv_no+=1;
					}
				}
			}
			
			if(invoice_id_seq.equals(""))
			{
				if(VINVOICE_ID_SEQ.size()>0)
				{
					invoice_id_seq=""+VINVOICE_ID_SEQ.elementAt(0);
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getLpInvoiceDateCalc()
	{
		String function_nm="getInvoiceDateCalc()";
		try
		{
			String sysdate = utilDate.getSysdate();
			
			if((operation.equals("INSERT") && !submission_chk) || operation.equals("MODIFY"))
			{
				if(invoice_dt.equals(""))
				{
					invoice_dt=sysdate;
				}

				financial_year=utilDate.getFinancialYear(invoice_dt);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	

	public void getLPInvoiceDetailForViewBeforeSub()
	{
		String function_nm="getLPInvoiceDetailForViewBeforeSub()";
		try
		{
			couterpty_nm=""+utilBean.getCounterpartyName(conn,counterparty_cd);
			price_cd_nm=""+utilBean.getRateUnitNm(conn,price_cd);
			invoice_raised_in_nm=""+utilBean.getRateUnitNm(conn,invoice_raised_in);
			
			String ship_nm="";
			String boe_number="";
			String boe_date="";
			
			if(contract_type.equals("O") || contract_type.equals("Q"))
			{
				queryString="SELECT A.CONT_REF_NO,TO_CHAR(A.DDA_DT,'DD-MON-YY'),A.CONT_REF_NO,B.SHIP_CD,B.BOE_NO,TO_CHAR(B.BOE_DT,'DD-MON-YY'),TO_CHAR(SIGNING_DT,'DD-MON-YY'),TO_CHAR(B.ACTUAL_RECPT_DT,'DD-MON-YY') "
						+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_CARGO_DTL B "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? "
						+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.AGMT_TYPE=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND B.CARGO_NO=? "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
						+ ""
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, "C");
				stmt.setString(4, agmt_no);
				stmt.setString(5, agmt_rev_no);
				stmt.setString(6, "A");
				stmt.setString(7, cont_no);
				stmt.setString(8, contract_type);
				stmt.setString(9, cargo_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					contRef=rset.getString(1)==null?"":rset.getString(1);
					dda_dt=rset.getString(2)==null?"":rset.getString(2);
					//cargoRef=rset.getString(3)==null?"":rset.getString(3);
					
					String ship_cd=rset.getString(4)==null?"":rset.getString(4);
					ship_nm=utilBean.getShipName(conn,ship_cd);
					boe_number = rset.getString(5)==null?"":rset.getString(5);
					boe_date=rset.getString(6)==null?"":rset.getString(6);
					
					signingDt=rset.getString(7)==null?"":rset.getString(7);
					arrivalDt=rset.getString(8)==null?"":rset.getString(8);
				}
				rset.close();
				stmt.close();
				
				queryString1="SELECT TO_CHAR(SIGNING_DT,'DD-MON-YY')  "
						+ "FROM FMS_LTCORA_AGMT_MST A "
						+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
						+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_LTCORA_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.BUY_SALE=B.BUY_SALE) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, "A");
				stmt1.setString(3, agmt_no);
				stmt1.setString(4, counterparty_cd);
				stmt1.setString(5, "C");
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					agmtSigningDt=rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
			}
			else
			{
				queryString="SELECT CONT_REF_NO,TO_CHAR(DDA_DT,'DD-MON-YY'),TRADE_REF_NO,TO_CHAR(SIGNING_DT,'DD-MON-YY')  "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND CONT_NO=? AND AGMT_NO=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
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
					dda_dt=rset.getString(2)==null?"":rset.getString(2);
					String tradeRef=rset.getString(3)==null?"":rset.getString(3);
					signingDt=rset.getString(4)==null?"":rset.getString(4);
					
					if(contract_type.equals("X"))
					{
						contRef=tradeRef;
					}
				}
				rset.close();
				stmt.close();
				
				if(contract_type.equals("S"))
				{
					queryString1="SELECT TO_CHAR(SIGNING_DT,'DD-MON-YY')  "
							+ "FROM FMS_AGMT_MST A "
							+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
							+ "AND AGMT_NO=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, "F");
					stmt1.setString(3, agmt_no);
					stmt1.setString(4, counterparty_cd);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						agmtSigningDt=rset1.getString(1)==null?"":rset1.getString(1);
					}
					rset1.close();
					stmt1.close();
				}
			}
			
			queryString1="SELECT CONTACT_PERSON "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND SEQ_NO=? AND ADDR_FLAG=? AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.TYPE=B.TYPE "
					+ "AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY'))";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_cd);
			stmt1.setString(3, "C");
			stmt1.setString(4, contact_person_cd);
			stmt1.setString(5, "P"+plant_seq);
			stmt1.setString(6, "RLNG");
			stmt1.setString(7, invoice_dt);
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
					+ "AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY'))";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, comp_cd);
			stmt2.setString(3, "B");
			stmt2.setString(4, bu_contact_person_cd);
			stmt2.setString(5, "P"+bu_unit);
			stmt2.setString(6, "RLNG");
			stmt2.setString(7, invoice_dt);
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
			
			HashMap plant_detail=utilBean.getCounterpartyPlantDetail(conn,comp_cd, "C", counterparty_cd, plant_seq);
            plantAddress=""+plant_detail.get("plant_address");
			plantCity=""+plant_detail.get("plant_city");
			plantState=""+plant_detail.get("plant_state");
			plantPin=""+plant_detail.get("plant_pin");
			plantNm=""+plant_detail.get("plant_name");
			
			if(contract_type.equals("O") || contract_type.equals("Q"))
			{
				tax_info="State : "+plantState;
				tax_info+="<br>State Code : "+utilBean.getState_TIN(conn, comp_cd, counterparty_cd, "C", plant_seq);
				//String temp_tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "C", counterparty_cd, plant_seq);
				//tax_info+=temp_tax_info.replaceAll("\n", "<br>");
				
				queryString = "SELECT A.STAT_NO, TO_CHAR(A.EFF_DT,'DD/MM/YYYY'), B.STAT_NM "
						+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B "
						+ "WHERE A.COUNTERPARTY_CD=? AND A.ENTITY=? "
						+ "AND A.PLANT_SEQ_NO=? AND A.COMPANY_CD=? "
						+ "AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL AND B.STAT_CD IN ('1003','1001')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, counterparty_cd);
				stmt.setString(2, "C");
				stmt.setString(3, plant_seq);
				stmt.setString(4, comp_cd);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					String no = rset.getString(1)==null?"":rset.getString(1);
					String nm = rset.getString(3)==null?"":rset.getString(3);
	
					tax_info+="<br>"+nm+" : "+no;
				}
				stmt.close();
				rset.close();
				
				bu_tax_info="State : "+bu_plantState;
				bu_tax_info+="<br>State Code : "+utilBean.getState_TIN(conn, comp_cd, comp_cd, "B", bu_unit);
				//String temp_bu_tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "B", comp_cd, bu_unit);
				//bu_tax_info+=temp_bu_tax_info.replaceAll("\n", "<br>");
				
				queryString = "SELECT A.STAT_NO, TO_CHAR(A.EFF_DT,'DD/MM/YYYY'), B.STAT_NM "
						+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B "
						+ "WHERE A.COUNTERPARTY_CD=? AND A.ENTITY=? "
						+ "AND A.PLANT_SEQ_NO=? AND A.COMPANY_CD=? "
						+ "AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL AND B.STAT_CD IN ('1003','1001')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, "B");
				stmt.setString(3, bu_unit);
				stmt.setString(4, comp_cd);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					String no = rset.getString(1)==null?"":rset.getString(1);
					String nm = rset.getString(3)==null?"":rset.getString(3);
	
					bu_tax_info+="<br>"+nm+" : "+no;
				}
				stmt.close();
				rset.close();
				
				bu_tax_info+="<br>SAC : 999799"
						+ "<br>Description of Service : Other Miscellaneous services - Other Services n.e.c."
						+ "<br>Place Of Supply : "+plantState; //WILL BE CUSTOMER PLANT STATE AS DISCUSSED BY JAYASRI MAM WITH VIJAY ON 20250725
			}
			else
			{
				tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "C", counterparty_cd, plant_seq);
				tax_info=tax_info.replaceAll("\n", "<br>");
				
				bu_tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "B", comp_cd, bu_unit);
				bu_tax_info=bu_tax_info.replaceAll("\n", "<br>");
			}	
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	
	public void getSalesInvoiceChangeList()
	{
		String function_nm="getSalesInvoiceChangeList()";
		try
		{	
			for(int i=0; i<VINVOICE_LIST_ABBR.size(); i++)
			{
				String invFlag="";
				if(VINVOICE_LIST_ABBR.elementAt(i).equals("INV_HEAD"))
				{
					invFlag = "'F'";
				}
				else if(VINVOICE_LIST_ABBR.elementAt(i).equals("LP_HEAD"))
				{
					invFlag = "'LP'";
				}
				else if(VINVOICE_LIST_ABBR.elementAt(i).equals("CRDR_HEAD"))
				{
					invFlag = "'CR','DR'";
				}
				inv_index=0;
				String temp_period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
				String temp_period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
				
				queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CARGO_NO,BU_UNIT,PLANT_SEQ,"
						+ "BU_STATE_TIN,FINANCIAL_YEAR,INVOICE_SEQ,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),"
						+ "INVOICE_NO,INV_FLAG,TO_CHAR(INVOICE_DT,'DD/MM/YYYY') "
						+ "FROM FMS_INVOICE_MST A "
						+ "WHERE COMPANY_CD=? AND INVOICE_DT >= TO_DATE(?,'DD/MM/YYYY') AND INVOICE_DT <= TO_DATE(?,'DD/MM/YYYY') "
						+ "AND CONTRACT_TYPE NOT IN ('O','Q') "
						+ "AND APPROVED_FLAG=? AND PDF_INV_DTL IS NOT NULL AND PRINT_BY_ORI IS NOT NULL "
						+ "AND PRINT_BY_TRI IS NOT NULL AND PRINT_BY_DUP IS NOT NULL AND INV_FLAG IN ("+invFlag+") "
						+ "ORDER BY (SELECT Z.COUNTERPARTY_ABBR FROM FMS_COUNTERPARTY_MST Z WHERE A.COUNTERPARTY_CD=Z.COUNTERPARTY_CD AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST Y WHERE Y.COUNTERPARTY_CD=Z.COUNTERPARTY_CD))";
				int st_count=0;
				stmt=conn.prepareStatement(queryString);
				stmt.setString(++st_count, comp_cd);
				stmt.setString(++st_count, temp_period_start_dt);
				stmt.setString(++st_count, temp_period_end_dt);
				stmt.setString(++st_count, "Y");
				rset=stmt.executeQuery();
				while(rset.next())
				{	
					inv_index++;
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String agmt=rset.getString(3)==null?"":rset.getString(3);
					String agmt_rev=rset.getString(4)==null?"":rset.getString(4);
					String cont=rset.getString(5)==null?"":rset.getString(5);
					String cont_rev=rset.getString(6)==null?"":rset.getString(6);
					String cont_type=rset.getString(7)==null?"0":rset.getString(7);
					String cargo_no=rset.getString(8)==null?"":rset.getString(8);
					
					String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, cargo_no);
					
					String bu_plant_seq=rset.getString(9)==null?"":rset.getString(9);
					String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
					String plant_seq=rset.getString(10)==null?"":rset.getString(10);
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "C");
					String buStateTin=rset.getString(11)==null?"":rset.getString(11);
					String financial_year=rset.getString(12)==null?"":rset.getString(12);
					String inv_seq=rset.getString(13)==null?"":rset.getString(13);
					String temp_st_dt=rset.getString(14)==null?"":rset.getString(14);
					String temp_end_dt=rset.getString(15)==null?"":rset.getString(15);
					String inv_no=rset.getString(16)==null?"":rset.getString(16);
					String invType=rset.getString(17)==null?"":rset.getString(17);
					String inv_dt=rset.getString(18)==null?"":rset.getString(18);
					String inv_type=""; 
					
					VCOUNTERPTY_CD.add(countpty_cd);
					VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
					VCOUNTERPTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
					//VAGMT_NO.add(agmt);
					//VAGMT_REV_NO.add(agmt_rev);
					//VCONT_NO.add(cont);
					//VCONT_REV_NO.add(cont_rev);
					//VCARGO_NO.add(cargo_no);
					//VCONTRACT_TYPE.add(cont_type);
					VDEAL_NO.add(deal_no);
					VPLANT_SEQ.add(plant_seq);
					VPLANT_ABBR.add(plant_abbr);
					VBU_PLANT_SEQ.add(bu_plant_seq);
					VBU_PLANT_ABBR.add(bu_plant_abbr);
					VBU_STATE_TIN.add(buStateTin);							
					VFINANCIAL_YEAR.add(financial_year);
					VINVOICE_SEQ.add(inv_seq);
					VPERIOD_START_DT.add(temp_st_dt);
					VPERIOD_END_DT.add(temp_end_dt);
					VINVOICE_NO.add(inv_no);
					VINV_FLAG.add(invType);
					VINVOICE_TYPE.add(invType);
					VINVOICE_TYPE_NM.add(invType.equals("CR")?"Credit Note"
									:invType.equals("DR")?"Debit Note"
									:invType.equals("LP")?"Late Payment"
									:invType.equals("F")?"Sales Invoice"
									:"");
					VINVOICE_DT.add(inv_dt);
					VOTH_INVOICE_TYPE.add(inv_type);
					
					String contRef="";
					String agmt_base="";
					queryString1="SELECT CONT_REF_NO,TRADE_REF_NO,AGMT_BASE  "
							+ "FROM FMS_SUPPLY_CONT_MST A "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND CONT_NO=? AND AGMT_NO=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
							+ "UNION ALL "
							+ "SELECT A.CONT_REF_NO,NULL,NULL "
							+ "FROM FMS_LTCORA_CONT_MST A,"
								+ "FMS_LTCORA_CONT_CARGO_DTL B "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? "
							+ "AND A.AGMT_NO=? AND A.AGMT_TYPE=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND B.CARGO_NO=? "
							+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
							+ ""
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, cont_type);
					stmt1.setString(3, cont);
					stmt1.setString(4, agmt);
					stmt1.setString(5, countpty_cd);
					stmt1.setString(6, comp_cd);
					stmt1.setString(7, countpty_cd);
					stmt1.setString(8, "C");
					stmt1.setString(9, agmt);
					stmt1.setString(10, "A");
					stmt1.setString(11, cont);
					stmt1.setString(12, cont_type);
					stmt1.setString(13, cargo_no);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						contRef=rset1.getString(1)==null?"":rset1.getString(1);
						String tradeRef=rset1.getString(2)==null?"":rset1.getString(2);
						if(cont_type.equals("X"))
						{
							contRef=tradeRef;
						}
						agmt_base=rset1.getString(3)==null?"":rset1.getString(3);
					}
					rset1.close();
					stmt1.close();
					
					VCONT_REF_NO.add(contRef);
					VAGMT_BASE.add(agmt_base);
					
					String flag="";
					String seqNo="";
					queryString2="SELECT FLAG,SEQ_NO "
							+ "FROM FMS_INVOICE_CHANGE_DTL A "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND SEGMENT=? AND CHANGE_TYPE=? "
							+ "AND SEQ_NO=(SELECT MAX(B.SEQ_NO) FROM FMS_INVOICE_CHANGE_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR "
							+ "AND A.SEGMENT=B.SEGMENT AND A.CHANGE_TYPE=B.CHANGE_TYPE)";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, buStateTin);
					stmt2.setString(3, inv_seq);
					stmt2.setString(4, financial_year);
					stmt2.setString(5, "RLNG");
					stmt2.setString(6, "REPRINT_PDF");
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						flag=rset2.getString(1)==null?"":rset2.getString(1);
						seqNo=rset2.getString(2)==null?"":rset2.getString(2);
					}
					rset2.close();
					stmt2.close();
					
					VACTION_FLAG.add(flag);
					VSEQ_NO.add(seqNo);
					
					int days=utilDate.getDays(utilDate.getSysdate(),inv_dt);
					int allowable_day=30;
					int remDay=allowable_day-days;
					VREMAINING_DAYS.add(remDay<0?0:remDay);
					
					int count=0;
					queryString2="SELECT COUNT(*) "
							+ "FROM FMS_INVOICE_CHANGE_DTL A "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND SEGMENT=? AND CHANGE_TYPE=? "
							+ "AND FLAG IN (?,?) ";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, buStateTin);
					stmt2.setString(3, inv_seq);
					stmt2.setString(4, financial_year);
					stmt2.setString(5, "RLNG");
					stmt2.setString(6, "REPRINT_PDF");
					stmt2.setString(7, "A");
					stmt2.setString(8, "P");
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						count=rset2.getInt(1);
					}
					rset2.close();
					stmt2.close();
					
					VCOUNT.add(count);
				}
				rset.close();
				stmt.close();
				
				VINDEX.add(inv_index);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	

	public void getSalesfflowInvoiceChangeList()
	{
		String function_nm="getSalesfflowInvoiceChangeList()";
		try
		{		
				inv_index=0;
				String temp_period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
				String temp_period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
				
				String queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_UNIT,ADDR_FLAG,"
						+ "BU_STATE_TIN,FINANCIAL_YEAR,INVOICE_SEQ,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),"
						+ "INVOICE_NO,INVOICE_CATEGORY,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),INVOICE_TYPE "
						+ "FROM FMS_FFLOW_INV_MST A "
						+ "WHERE COMPANY_CD=? AND INVOICE_DT >= TO_DATE(?,'DD/MM/YYYY') AND INVOICE_DT <= TO_DATE(?,'DD/MM/YYYY') "
						+ "AND CONTRACT_TYPE NOT IN ('O','Q') "
						+ "AND APPROVED_FLAG=? AND INVOICE_CATEGORY=? AND PDF_INV_DTL IS NOT NULL AND PRINT_BY_ORI IS NOT NULL "
						+ "AND PRINT_BY_TRI IS NOT NULL AND PRINT_BY_DUP IS NOT NULL "
						+ "ORDER BY (SELECT Z.COUNTERPARTY_ABBR FROM FMS_COUNTERPARTY_MST Z WHERE A.COUNTERPARTY_CD=Z.COUNTERPARTY_CD AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST Y WHERE Y.COUNTERPARTY_CD=Z.COUNTERPARTY_CD))";
				int st_count=0;
				stmt=conn.prepareStatement(queryString);
				stmt.setString(++st_count, comp_cd);
				stmt.setString(++st_count, temp_period_start_dt);
				stmt.setString(++st_count, temp_period_end_dt);
				stmt.setString(++st_count, "Y");
				stmt.setString(++st_count, "P");
				rset=stmt.executeQuery();
				while(rset.next())
				{	
					inv_index++;
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String agmt=rset.getString(3)==null?"":rset.getString(3);
					String agmt_rev=rset.getString(4)==null?"":rset.getString(4);
					String cont=rset.getString(5)==null?"":rset.getString(5);
					String cont_rev=rset.getString(6)==null?"":rset.getString(6);
					String cont_type=rset.getString(7)==null?"0":rset.getString(7);
					String cargo_no="0";
					
					String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, cargo_no);
					
					String bu_plant_seq=rset.getString(8)==null?"":rset.getString(8);
					String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
					
					String address_flag=rset.getString(9)==null?"":rset.getString(9);
					String plant_seq = "";
					String plant_abbr = "";
					if(address_flag.equals("R"))
					{
						plant_seq=address_flag;
						plant_abbr="Registered";
					}
					else if(address_flag.equals("C"))
					{
						plant_seq=address_flag;
						plant_abbr="Correspondence";
					}
					else if(address_flag.equals("B"))
					{
						plant_seq=address_flag;
						plant_abbr="Billing";
					}
					else if(!address_flag.equals("") && address_flag.startsWith("P"))
					{
						plant_seq=address_flag.substring(1,address_flag.length());
						plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "C");
					}
					
					String buStateTin=rset.getString(10)==null?"":rset.getString(10);
					String financial_year=rset.getString(11)==null?"":rset.getString(11);
					String inv_seq=rset.getString(12)==null?"":rset.getString(12);
					String temp_st_dt=rset.getString(13)==null?"":rset.getString(13);
					String temp_end_dt=rset.getString(14)==null?"":rset.getString(14);
					String inv_no=rset.getString(15)==null?"":rset.getString(15);
					String invType=rset.getString(16)==null?"":rset.getString(16);
					String inv_dt=rset.getString(17)==null?"":rset.getString(17); 
					String inv_type=rset.getString(18)==null?"":rset.getString(18); 
					
					VCOUNTERPTY_CD.add(countpty_cd);
					VCOUNTERPTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
					VCOUNTERPTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
					//VAGMT_NO.add(agmt);
					//VAGMT_REV_NO.add(agmt_rev);
					//VCONT_NO.add(cont);
					//VCONT_REV_NO.add(cont_rev);
					//VCARGO_NO.add(cargo_no);
					//VCONTRACT_TYPE.add(cont_type);
					VDEAL_NO.add(deal_no);
					VPLANT_SEQ.add(plant_seq);
					VPLANT_ABBR.add(plant_abbr);
					VBU_PLANT_SEQ.add(bu_plant_seq);
					VBU_PLANT_ABBR.add(bu_plant_abbr);
					VBU_STATE_TIN.add(buStateTin);							
					VFINANCIAL_YEAR.add(financial_year);
					VINVOICE_SEQ.add(inv_seq);
					VPERIOD_START_DT.add(temp_st_dt);
					VPERIOD_END_DT.add(temp_end_dt);
					VINVOICE_NO.add(inv_no);
					VINV_FLAG.add(invType);
					VINVOICE_TYPE.add(invType);
					VINVOICE_TYPE_NM.add(invType.equals("P")?"Sales FFLOW Invoice"
									:"");
					VINVOICE_DT.add(inv_dt);
					VOTH_INVOICE_TYPE.add(inv_type);
					
					String contRef="";
					String agmt_base="";
					String queryString1="SELECT CONT_REF_NO,TRADE_REF_NO,AGMT_BASE  "
							+ "FROM FMS_SUPPLY_CONT_MST A "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND CONT_NO=? AND AGMT_NO=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, cont_type);
					stmt1.setString(3, cont);
					stmt1.setString(4, agmt);
					stmt1.setString(5, countpty_cd);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						contRef=rset1.getString(1)==null?"":rset1.getString(1);
						String tradeRef=rset1.getString(2)==null?"":rset1.getString(2);
						if(cont_type.equals("W"))
						{
							contRef=tradeRef;
						}
						agmt_base=rset1.getString(3)==null?"":rset1.getString(3);
					}
					rset1.close();
					stmt1.close();
					
					VCONT_REF_NO.add(contRef);
					VAGMT_BASE.add(agmt_base);
					
					String flag="";
					String seqNo="";
					queryString2="SELECT FLAG,SEQ_NO "
							+ "FROM FMS_FFLOW_INV_CHANGE_DTL A "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND SEGMENT=? AND CHANGE_TYPE=? AND INVOICE_TYPE=? "
							+ "AND SEQ_NO=(SELECT MAX(B.SEQ_NO) FROM FMS_FFLOW_INV_CHANGE_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR "
							+ "AND A.SEGMENT=B.SEGMENT AND A.CHANGE_TYPE=B.CHANGE_TYPE AND A.INVOICE_TYPE=B.INVOICE_TYPE)";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, buStateTin);
					stmt2.setString(3, inv_seq);
					stmt2.setString(4, financial_year);
					stmt2.setString(5, "RLNG");
					stmt2.setString(6, "REPRINT_PDF");
					stmt2.setString(7, inv_type);
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						flag=rset2.getString(1)==null?"":rset2.getString(1);
						seqNo=rset2.getString(2)==null?"":rset2.getString(2);
					}
					rset2.close();
					stmt2.close();
					
					VACTION_FLAG.add(flag);
					VSEQ_NO.add(seqNo);
					
					int days=utilDate.getDays(utilDate.getSysdate(),inv_dt);
					int allowable_day=30;
					int remDay=allowable_day-days;
					VREMAINING_DAYS.add(remDay<0?0:remDay);
					
					int count=0;
					queryString2="SELECT COUNT(*) "
							+ "FROM FMS_FFLOW_INV_CHANGE_DTL A "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
							+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND SEGMENT=? AND CHANGE_TYPE=? "
							+ "AND FLAG IN (?,?) AND INVOICE_TYPE=? ";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, buStateTin);
					stmt2.setString(3, inv_seq);
					stmt2.setString(4, financial_year);
					stmt2.setString(5, "RLNG");
					stmt2.setString(6, "REPRINT_PDF");
					stmt2.setString(7, "A");
					stmt2.setString(8, "P");
					stmt2.setString(9, inv_type);
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						count=rset2.getInt(1);
					}
					rset2.close();
					stmt2.close();
					
					VCOUNT.add(count);
				}
				rset.close();
				stmt.close();
				
				VINDEX.add(inv_index);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	
	public int isRePrintPDFRequested(String comp_cd,String buStateTin,String inv_seq,String financial_year,String segment,String chnage_type,String flag)
	{
		String function_nm="isRePrintPDFRequested()";
		int re_print_count=0;
		try
		{
			queryString_tmp="SELECT COUNT(*) "
					+ "FROM FMS_INVOICE_CHANGE_DTL A "
					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
					+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND SEGMENT=? AND CHANGE_TYPE=? AND FLAG=? "
					+ "AND SEQ_NO=(SELECT MAX(B.SEQ_NO) FROM FMS_INVOICE_CHANGE_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR "
					+ "AND A.SEGMENT=B.SEGMENT AND A.CHANGE_TYPE=B.CHANGE_TYPE)";
			stmt_tmp=conn.prepareStatement(queryString_tmp);
			stmt_tmp.setString(1, comp_cd);
			stmt_tmp.setString(2, buStateTin);
			stmt_tmp.setString(3, inv_seq);
			stmt_tmp.setString(4, financial_year);
			stmt_tmp.setString(5, segment);
			stmt_tmp.setString(6, chnage_type);
			stmt_tmp.setString(7, flag);
			rset_tmp=stmt_tmp.executeQuery();
			if(rset_tmp.next())
			{
				re_print_count=rset_tmp.getInt(1);
			}
			rset_tmp.close();
			stmt_tmp.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return re_print_count;
	}
	

	public int isRePrintPDFRequestedFflow(String comp_cd,String buStateTin,String inv_seq,String financial_year,String segment,String chnage_type,String flag,String inv_type)
	{
		String function_nm="isRePrintPDFRequestedFflow()";
		int re_print_count=0;
		try
		{
			queryString_tmp="SELECT COUNT(*) "
					+ "FROM FMS_FFLOW_INV_CHANGE_DTL A "
					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
					+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND SEGMENT=? AND CHANGE_TYPE=? AND FLAG=? AND INVOICE_TYPE=? "
					+ "AND SEQ_NO=(SELECT MAX(B.SEQ_NO) FROM FMS_FFLOW_INV_CHANGE_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR "
					+ "AND A.SEGMENT=B.SEGMENT AND A.CHANGE_TYPE=B.CHANGE_TYPE AND A.INVOICE_TYPE=B.INVOICE_TYPE)";
			stmt_tmp=conn.prepareStatement(queryString_tmp);
			stmt_tmp.setString(1, comp_cd);
			stmt_tmp.setString(2, buStateTin);
			stmt_tmp.setString(3, inv_seq);
			stmt_tmp.setString(4, financial_year);
			stmt_tmp.setString(5, segment);
			stmt_tmp.setString(6, chnage_type);
			stmt_tmp.setString(7, flag);
			stmt_tmp.setString(8, inv_type);
			rset_tmp=stmt_tmp.executeQuery();
			if(rset_tmp.next())
			{
				re_print_count=rset_tmp.getInt(1);
			}
			rset_tmp.close();
			stmt_tmp.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return re_print_count;
	}
	
	String callFlag = "";
	public void setCallFlag(String callFlag) {this.callFlag = callFlag;}
	String comp_cd = "";
	public void setComp_cd(String comp_cd) {this.comp_cd = comp_cd;}
	String operation = "";
	public void setOperation(String operation) {this.operation = operation;}
	String opration = "";
	public void setOpration(String opration) {this.opration = opration;}
	
	String month = "";
	String year = "";
	String billing_cycle = "";
	String billing_freq="";
	String billing_flag = "";
	String period_start_dt="";
	String period_end_dt="";
	String temp_period_start_dt="";
	String temp_period_end_dt="";
	String counterparty_cd = "";
	String cont_no = "";
	String cont_rev_no = "";
	String agmt_no = "";
	String agmt_rev_no = "";
	String cargo_no = "";
	String contract_type = "";
	String plant_seq = "";
	String bu_unit = "";
	String inv_type = "";
	String address_type = "";
	String invoice_type = "";
	String refresh_flg = "";
	String inv_seq="";
	String inv_no="";
	String inv_dt="";
	String user_defined_dt="";
	String financial="";
	String sel_exchng_cd="";
	String activityFlag="";
	String financial_year="";
	String exist_financial_year="";
	String lp_financial_year="";
	String bu_state_tin="";
	String file_nm="";
	String emp_cd="";
	String print_pdf_type="";
	String view_pdf_type="";
	String mail_pdf_type="";
	String ff_print_pdf_type="";
	String ff_view_pdf_type="";
	String ff_mail_pdf_type="";
	String flag="";
	String is_attachment="";
	String mail_inv_type="";
	String exchange_rate_mapping="";
	String filter_cont_type="";
	String inv_flag="";
	String ori_inv_flag="";
	String allocQty="";
	String temp_price="";
	String sel_inv_no="";
	String crdr_gen_type="";
	
	public void setMonth(String month) {this.month = month;}
	public void setYear(String year) {this.year = year;}
	public void setBilling_cycle(String billing_cycle) {this.billing_cycle = billing_cycle;}
	public void setPeriod_start_dt(String period_start_dt) {this.period_start_dt = period_start_dt;}
	public void setPeriod_end_dt(String period_end_dt) {this.period_end_dt = period_end_dt;}
	public void setTemp_period_start_dt(String temp_period_start_dt) {this.temp_period_start_dt = temp_period_start_dt;}
	public void setTemp_period_end_dt(String temp_period_end_dt) {this.temp_period_end_dt = temp_period_end_dt;}
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setCont_no(String cont_no) {this.cont_no = cont_no;}
	public void setCont_rev_no(String cont_rev_no) {this.cont_rev_no = cont_rev_no;}
	public void setAgmt_no(String agmt_no) {this.agmt_no = agmt_no;}
	public void setAgmt_rev_no(String agmt_rev_no) {this.agmt_rev_no = agmt_rev_no;}
	public void setCargo_no(String cargo_no) {this.cargo_no = cargo_no;}
	public void setContract_type(String contract_type) {this.contract_type = contract_type;}
	public void setPlant_seq(String plant_seq) {this.plant_seq = plant_seq;}
	public void setBu_unit(String bu_unit) {this.bu_unit = bu_unit;}
	public void setInv_type(String inv_type) {this.inv_type = inv_type;}
	public void setAddress_type(String address_type) {this.address_type = address_type;}
	public void setInvoice_type(String invoice_type) {this.invoice_type = invoice_type;}
	public void setRefresh_flg(String refresh_flg) {this.refresh_flg = refresh_flg;}
	public void setInv_seq(String inv_seq) {this.inv_seq = inv_seq;}
	public void setInv_no(String inv_no) {this.inv_no = inv_no;}
	public void setInv_dt(String inv_dt) {this.inv_dt = inv_dt;}
	public void setUser_defined_dt(String user_defined_dt) {this.user_defined_dt = user_defined_dt;}
	public void setFinancial(String financial) {this.financial = financial;}
	public void setSel_exchng_cd(String sel_exchng_cd) {this.sel_exchng_cd = sel_exchng_cd;}
	public void setActivityFlag(String activityFlag) {this.activityFlag = activityFlag;}
	public void setFinancial_year(String financial_year) {this.financial_year = financial_year;}
	public void setExist_financial_year(String exist_financial_year) {this.exist_financial_year = exist_financial_year;}
	public void setLp_financial_year(String lp_financial_year) {this.lp_financial_year = lp_financial_year;}
	public void setBu_state_tin(String bu_state_tin) {this.bu_state_tin = bu_state_tin;}
	public void setInvoice_seq(String invoice_seq) {this.invoice_seq = invoice_seq;}
	public void setFlag(String flag) {this.flag = flag;}
	public void setFilter_cont_type(String filter_cont_type) {this.filter_cont_type = filter_cont_type;}
	public void setInv_flag(String inv_flag) {this.inv_flag = inv_flag;}
	public void setOri_inv_flag(String ori_inv_flag) {this.ori_inv_flag = ori_inv_flag;}
	public void setAllocQty(String allocQty) {this.allocQty = allocQty;}
	public void setTemp_price(String temp_price) {this.temp_price = temp_price;}
	public void setSel_inv_no(String sel_inv_no) {this.sel_inv_no = sel_inv_no;}
	public void setCrdr_gen_type(String crdr_gen_type) {this.crdr_gen_type = crdr_gen_type;}
	
	public String getPeriod_start_dt() {return period_start_dt;}
	public String getPeriod_end_dt() {return period_end_dt;}
	public String getTemp_period_start_dt() {return temp_period_start_dt;}
	public String getTemp_period_end_dt() {return temp_period_end_dt;}
	public String getUser_defined_dt() {return user_defined_dt;}
	public String getFinancial_year() {return financial_year;}
	public String getBu_state_tin() {return bu_state_tin;}
	public String getBu_unit() {return bu_unit;}
	public String getPlant_seq() {return plant_seq;}
	public String getAgmt_no() {return agmt_no;}
	public String getAgmt_rev_no() {return agmt_rev_no;}
	public String getCont_no() {return cont_no;}
	public String getCont_rev_no() {return cont_rev_no;}
	public String getContract_type() {return contract_type;}
	public String getCargo_no() {return cargo_no;}
	
	public void setContact_person_cd(String contact_person_cd) {this.contact_person_cd = contact_person_cd;}
	public void setBu_contact_person_cd(String bu_contact_person_cd) {this.bu_contact_person_cd = bu_contact_person_cd;}
	public void setPrice_cd(String price_cd) {this.price_cd = price_cd;}
	public void setInvoice_raised_in(String invoice_raised_in) {this.invoice_raised_in = invoice_raised_in;}
	public void setInvoice_dt(String invoice_dt) {this.invoice_dt = invoice_dt;}
	
	public void setFile_nm(String file_nm) {this.file_nm = file_nm;}
	public void setEmp_cd(String emp_cd) {this.emp_cd = emp_cd;}
	public void setPrint_pdf_type(String print_pdf_type) {this.print_pdf_type = print_pdf_type;}
	public void setView_pdf_type(String view_pdf_type) {this.view_pdf_type = view_pdf_type;}
	public void setMail_pdf_type(String mail_pdf_type) {this.mail_pdf_type = mail_pdf_type;}
	public void setFf_print_pdf_type(String ff_print_pdf_type) {this.ff_print_pdf_type = ff_print_pdf_type;}
	public void setFf_view_pdf_type(String ff_view_pdf_type) {this.ff_view_pdf_type = ff_view_pdf_type;}
	public void setFf_mail_pdf_type(String ff_mail_pdf_type) {this.ff_mail_pdf_type = ff_mail_pdf_type;}
	public void setIs_attachment(String is_attachment) {this.is_attachment = is_attachment;}
	public void setMail_inv_type(String mail_inv_type) {this.mail_inv_type = mail_inv_type;}
	
	public void setExchange_rate_mapping(String exchange_rate_mapping) {this.exchange_rate_mapping = exchange_rate_mapping;}
	
	public void setCont_start_dt(String cont_start_dt) {this.cont_start_dt = cont_start_dt;}
	public void setCont_end_dt(String cont_end_dt) {this.cont_end_dt = cont_end_dt;}
	
	public String getExchange_rate_mapping() {return exchange_rate_mapping;}
	
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
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
	Vector VPLANT_SEQ = new Vector();
	Vector VPLANT_ABBR = new Vector();
	Vector VBU_PLANT_SEQ = new Vector();
	Vector VBU_PLANT_ABBR = new Vector();
	Vector VDEAL_NO = new Vector();
	Vector VPERIOD_START_DT = new Vector();
	Vector VPERIOD_END_DT = new Vector();
	Vector VTEMP_PERIOD_START_DT = new Vector();
	Vector VTEMP_PERIOD_END_DT = new Vector();
	Vector VSEL_BU_CD = new Vector();
	Vector VSEL_BU_PLANT_SEQ_NO = new Vector();
	Vector VSEL_BU_PLANT_ABBR = new Vector();
	Vector VCONTACT_PERSON = new Vector();
	Vector VCONTACT_PERSON_CD = new Vector();
	Vector VBU_CONTACT_PERSON = new Vector();
	Vector VBU_CONTACT_PERSON_CD = new Vector();
	Vector VINVOICE_DT = new Vector();
	Vector VINVOICE_DUE_DT = new Vector();
	Vector VINVOICE_PAYRECV_DT = new Vector();
	Vector VLP_INVOICE_NO = new Vector();
	Vector VINVOICE_NO = new Vector();
	Vector VINVOICE_SEQ = new Vector();
	Vector VFREQ = new Vector();
	Vector VFREQ_NM = new Vector();
	Vector VALLOC_QTY = new Vector();
	Vector VEXCHNAGE_RATE = new Vector();
	Vector VSALES_PRICE = new Vector();
	Vector VSALES_PRICE_UNIT = new Vector();
	Vector VRATE_NM = new Vector();
	Vector VGROSS_AMT = new Vector();
	Vector VTAX_AMT = new Vector();
	Vector VINVOICE_AMT = new Vector();
	Vector VADJ_SIGN = new Vector();
	Vector VADJ_AMT = new Vector();
	Vector VNET_PAYABLE = new Vector();
	Vector VSALES_PRICE_USD = new Vector();
	Vector VSALES_PRICE_UNIT_USD = new Vector();
	Vector VRATE_NM_USD = new Vector();
	Vector VGROSS_AMT_USD = new Vector();
	Vector VTAX_AMT_USD = new Vector();
	Vector VINVOICE_AMT_USD = new Vector();
	Vector VADJ_SIGN_USD = new Vector();
	Vector VADJ_AMT_USD = new Vector();
	Vector VNET_PAYABLE_USD = new Vector();
	Vector VMAPPING_ID = new Vector();
	Vector VADDRESS_TYPE = new Vector();
	Vector VADDRESS_NAME = new Vector();
	Vector VLINK_INVOICE_NO = new Vector();
	Vector VSTATUS = new Vector();
	Vector VINVOICE_TYPE = new Vector();
	Vector VINVOICE_TYPE_NM = new Vector();
	Vector VINVOICE_CATEGORY = new Vector();
	Vector VFINANCIAL_YEAR = new Vector();
	Vector VINVOICE_REF = new Vector();
	Vector VLINE_NO = new Vector();
	Vector VLINE_DESC = new Vector();
	Vector VUNIT = new Vector();
	Vector VQTY = new Vector();
	Vector VRATE = new Vector();
	Vector VAMOUNT = new Vector();
	Vector VBILLING_FREQ_FLAG = new Vector();
	Vector VBILLING_FREQ_NM = new Vector();
	Vector VEXCHNG_RATE_CD = new Vector();
	Vector VEXCHNG_RATE_NM = new Vector();
	Vector VEXCHNG_RATE_FLAG = new Vector();
	Vector VINV_CHECKED_FLAG = new Vector();
	Vector VINV_APPROVED_FLAG = new Vector();
	Vector VBU_STATE_TIN = new Vector();
	Vector VINVOICE_EXIST = new Vector();
	Vector VAGMT_BASE = new Vector();
	Vector VMULTI_TAX_STRUCT = new Vector();
	Vector VMAIN_MULTI_TAX_STRUCT = new Vector();
	Vector VNEW_MULTI_TAX_STRUCT = new Vector();
	Vector VEMAIL_SENT = new Vector();
	Vector VEMAIL_SENT_INFO = new Vector();
	
	Vector VP_EXCHNG_RATE_CD = new Vector();
	Vector VP_EXCHNG_RATE_VALUE = new Vector();
	Vector VP_BG_COLOR = new Vector();
	Vector VB_EXCHNG_RATE_CD = new Vector();
	Vector VB_EXCHNG_RATE_VALUE = new Vector();
	Vector VB_BG_COLOR = new Vector();
	Vector VU_EXCHNG_RATE_CD = new Vector();
	Vector VU_EXCHNG_RATE_VALUE = new Vector();
	Vector VU_BG_COLOR = new Vector();
	Vector VEXCHNG_RATE_CAL_CD = new Vector();
	Vector VEXCHNG_RATE_CAL_VAL = new Vector();
	Vector VEXCHNG_RATE_CAL_COLOR = new Vector();
	
	Vector VALLOCATION_DT = new Vector();
	Vector VPRICE = new Vector();
	Vector VALLOCATION_QTY = new Vector();
	Vector VAMOUNT_USD = new Vector();
	Vector VAMOUNT_INR = new Vector();
	
	Vector VPDF_COL1 = new Vector();
	Vector VPDF_COL2 = new Vector();
	Vector VPDF_COL3 = new Vector();
	Vector VPDF_COL4 = new Vector();
	Vector VPDF_COL5 = new Vector();
	Vector VPDF_COL6 = new Vector();
	Vector VPDF_COL7 = new Vector();
	
	Vector VINVOICE_ID_SEQ = new Vector();
	Vector VDIFF_COLOR = new Vector();
	Vector VPDF_INV_FLAG = new Vector();
	Vector VPDF_TYPE = new Vector();
	Vector VPDF_FILE_NAME=new Vector();
	Vector VPDF_FILE_PATH=new Vector();
	Vector VPDF_SIGNED_FLAG=new Vector();
	Vector VSIGN_PDF_TYPE = new Vector();
	
	Vector VOTH_COUNTERPTY_CD = new Vector();
	Vector VOTH_COUNTERPTY_ABBR = new Vector();
	Vector VOTH_CONT_NO = new Vector();
	Vector VOTH_CONT_REV_NO = new Vector();
	Vector VOTH_AGMT_NO = new Vector();
	Vector VOTH_AGMT_REV_NO = new Vector();
	Vector VOTH_CONTRACT_TYPE = new Vector();
	Vector VOTH_DEAL_NO = new Vector();
	Vector VOTH_PLANT_SEQ = new Vector();
	Vector VOTH_PLANT_ABBR = new Vector();
	Vector VOTH_BU_PLANT_SEQ = new Vector();
	Vector VOTH_BU_PLANT_ABBR = new Vector();
	Vector VOTH_START_DT = new Vector();
	Vector VOTH_END_DT = new Vector();
	Vector VOTH_CONT_NAME = new Vector();
	Vector VOTH_CONT_REF_NO = new Vector();
	Vector VOTH_PERIOD_START_DT = new Vector();
	Vector VOTH_PERIOD_END_DT = new Vector();
	Vector VOTH_INVOICE_NO = new Vector();
	Vector VOTH_DIS_INVOICE_NO = new Vector();
	Vector VOTH_STATUS = new Vector();
	Vector VOTH_BILLING_FREQ_FLAG = new Vector();
	Vector VOTH_BILLING_FREQ_NM = new Vector();
	Vector VOTH_INVOICE_SEQ = new Vector();
	Vector VOTH_INVOICE_EXIST = new Vector();
	Vector VOTH_INV_CHECKED_FLAG = new Vector();
	Vector VOTH_INV_APPROVED_FLAG = new Vector();
	Vector VOTH_PDF_INV_FLAG = new Vector();
	Vector VOTH_PDF_TYPE = new Vector();
	Vector VOTH_PDF_FILE_NAME = new Vector();
	Vector VOTH_PDF_FILE_PATH=new Vector();
	Vector VOTH_PDF_SIGNED_FLAG=new Vector();
	Vector VOTH_SIGN_PDF_TYPE = new Vector();
	Vector VOTH_FINANCIAL_YEAR = new Vector();
	Vector VOTH_INVOICE_TYPE = new Vector();
	Vector VOTH_BU_STATE_TIN = new Vector();
	
	Vector VATT1_DATE = new Vector();
	Vector VATT1_DCQ = new Vector();
	Vector VATT1_BUYNOM = new Vector();
	Vector VATT1_SELLNOM_PNG = new Vector();
	Vector VATT1_SELLNOM_REGAS = new Vector();
	Vector VATT1_NG_PNG = new Vector();
	Vector VATT1_NG_REGAS = new Vector();
	Vector VATT1_NG_TOT_DLV_GAS = new Vector();
	Vector VATT1_CUMULATIVE_QTY_BILLING_PERIOD = new Vector();
	Vector VATT1_CUMULATIVE_QTY_TRANSCT_SUPPLY_PERIOD = new Vector();
	
	Vector VATT2_EXCHANGE_DESC = new Vector();
	Vector VATT2_RATE = new Vector();
	
	Vector VMAIL_PDF_TYPE = new Vector();
	Vector VMAIL_PDF_TYPE_NM = new Vector();
	Vector VMAIL_FROM_LIST = new Vector();
	Vector VMAIL_TO_LIST = new Vector();
	Vector VMAIL_CC_LIST = new Vector();
	Vector VMAIL_BCC_LIST = new Vector();
	Vector VMAIL_SUBJECT = new Vector();
	Vector VMAIL_ATTACHMENT = new Vector();
	Vector VMAIL_ANNEXURE_ATTACHMENT_FLAG = new Vector();
	Vector VMAIL_ATTACHMENT_PATH = new Vector();
	Vector VMAIL_ANNEXURE_ATTACHMENT_PATH = new Vector();
	Vector VMAIL_BODY = new Vector();
	
	Vector VANNEXURE_SEQ = new Vector();
	Vector VANNEXURE_FILE_NM = new Vector();
	Vector VANNEXURE_FOLDER = new Vector();
	
	Vector VSAP_APPROVAL_FLAG = new Vector();
	Vector VOTH_SAP_APPROVAL_FLAG = new Vector();
	Vector VFILTER_CONT_TYPE = new Vector();
	Vector VFILTER_CONT_NAME = new Vector();
	
	Vector VINVOICE_LIST_ABBR = new Vector();
	Vector VINVOICE_LIST_NAME = new Vector();
	Vector VINDEX = new Vector();
	Vector VCARGO_NO = new Vector();
	Vector VIS_IRN_GENERATED = new Vector();
	Vector VOTH_IS_IRN_GENERATED = new Vector();
	Vector VINV_FLAG = new Vector();
	Vector VBOE_ENTRY = new Vector();
	Vector VCARGO_NM = new Vector();
	Vector VATT_INVOICE_NO = new Vector();

	Vector VORI_INV_SEQ = new Vector();
	Vector VORI_INVOICE_NO = new Vector();
	Vector VORI_INVOICE_DT = new Vector();
	Vector VORI_INVOICE_DUE_DT = new Vector();
	Vector VORI_INVOICE_PAYRECV_DT = new Vector();
	Vector VORI_FINANCIAL_YEAR = new Vector();
	Vector VORI_INV_FLAG = new Vector();
	
	Vector VSTORAGE_DATE = new Vector();
	Vector VSTORAGE_INVENTORY = new Vector();
	Vector VOFFTAKE_QTY = new Vector();
	Vector VUSER_DEFINE = new Vector();
	Vector VSTORAGE_CHARGE = new Vector();
	Vector VSTORAGE_AMT = new Vector();
	Vector VRATE_TYPE = new Vector();
	Vector VDISCOUNT_FLAG = new Vector();
	Vector VRECEIPT_VOUCHER_MST = new Vector();
	Vector VEXISTING_RECEIPT_VOUCHER = new Vector();
	
	Vector VINVOICE_NO_LIST = new Vector();
	Vector VCRITERIA_FLAG = new Vector();
	Vector VCRITERIA_NAME = new Vector();
	Vector VCRITERIA_HIDE = new Vector();
	Vector VCREDIT_DEBIT_NO = new Vector();
	Vector VREF_NO = new Vector();
	Vector VCRDR_CRITERIA = new Vector();
	Vector VCRDR_GEN_TYPE = new Vector();
	Vector VACTION_FLAG = new Vector();
	Vector VSEQ_NO = new Vector();
	Vector VRE_PRINT_PDF = new Vector();
	Vector VOTH_RE_PRINT_PDF = new Vector();
	Vector VREMAINING_DAYS = new Vector();
	Vector VCOUNT = new Vector();
	
	Vector VOTH_INVOICE_DT = new Vector();
	
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
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
	public Vector getVPLANT_SEQ() {return VPLANT_SEQ;}
	public Vector getVPLANT_ABBR() {return VPLANT_ABBR;}
	public Vector getVBU_PLANT_SEQ() {return VBU_PLANT_SEQ;}
	public Vector getVBU_PLANT_ABBR() {return VBU_PLANT_ABBR;}
	public Vector getVDEAL_NO() {return VDEAL_NO;}
	public Vector getVPERIOD_START_DT() {return VPERIOD_START_DT;}
	public Vector getVPERIOD_END_DT() {return VPERIOD_END_DT;}
	public Vector getVTEMP_PERIOD_START_DT() {return VTEMP_PERIOD_START_DT;}
	public Vector getVTEMP_PERIOD_END_DT() {return VTEMP_PERIOD_END_DT;}
	public Vector getVSEL_BU_CD() {return VSEL_BU_CD;}
	public Vector getVSEL_BU_PLANT_SEQ_NO() {return VSEL_BU_PLANT_SEQ_NO;}
	public Vector getVSEL_BU_PLANT_ABBR() {return VSEL_BU_PLANT_ABBR;}
	public Vector getVCONTACT_PERSON() {return VCONTACT_PERSON;}
	public Vector getVCONTACT_PERSON_CD() {return VCONTACT_PERSON_CD;}
	public Vector getVBU_CONTACT_PERSON() {return VBU_CONTACT_PERSON;}
	public Vector getVBU_CONTACT_PERSON_CD() {return VBU_CONTACT_PERSON_CD;}
	public Vector getVINVOICE_DT() {return VINVOICE_DT;}
	public Vector getVINVOICE_DUE_DT() {return VINVOICE_DUE_DT;}
	public Vector getVINVOICE_PAYRECV_DT() {return VINVOICE_PAYRECV_DT;}
	public Vector getVLP_INVOICE_NO() {return VLP_INVOICE_NO;}
	public Vector getVINVOICE_NO() {return VINVOICE_NO;}
	public Vector getVINVOICE_SEQ() {return VINVOICE_SEQ;}
	public Vector getVFREQ() {return VFREQ;}
	public Vector getVFREQ_NM() {return VFREQ_NM;}
	public Vector getVALLOC_QTY() {return VALLOC_QTY;}
	public Vector getVEXCHNAGE_RATE() {return VEXCHNAGE_RATE;}
	public Vector getVSALES_PRICE() {return VSALES_PRICE;}
	public Vector getVSALES_PRICE_UNIT() {return VSALES_PRICE_UNIT;}
	public Vector getVRATE_NM() {return VRATE_NM;}
	public Vector getVGROSS_AMT() {return VGROSS_AMT;}
	public Vector getVTAX_AMT() {return VTAX_AMT;}
	public Vector getVINVOICE_AMT() {return VINVOICE_AMT;}
	public Vector getVADJ_SIGN() {return VADJ_SIGN;}
	public Vector getVADJ_AMT() {return VADJ_AMT;}
	public Vector getVNET_PAYABLE() {return VNET_PAYABLE;}
	public Vector getVSALES_PRICE_USD() {return VSALES_PRICE_USD;}
	public Vector getVSALES_PRICE_UNIT_USD() {return VSALES_PRICE_UNIT_USD;}
	public Vector getVRATE_NM_USD() {return VRATE_NM_USD;}
	public Vector getVGROSS_AMT_USD() {return VGROSS_AMT_USD;}
	public Vector getVTAX_AMT_USD() {return VTAX_AMT_USD;}
	public Vector getVINVOICE_AMT_USD() {return VINVOICE_AMT_USD;}
	public Vector getVADJ_SIGN_USD() {return VADJ_SIGN_USD;}
	public Vector getVADJ_AMT_USD() {return VADJ_AMT_USD;}
	public Vector getVNET_PAYABLE_USD() {return VNET_PAYABLE_USD;}
	public Vector getVMAPPING_ID() {return VMAPPING_ID;}
	public Vector getVADDRESS_TYPE() {return VADDRESS_TYPE;}
	public Vector getVADDRESS_NAME() {return VADDRESS_NAME;}
	public Vector getVLINK_INVOICE_NO() {return VLINK_INVOICE_NO;}
	public Vector getVSTATUS() {return VSTATUS;}
	public Vector getVINVOICE_TYPE() {return VINVOICE_TYPE;}
	public Vector getVINVOICE_TYPE_NM() {return VINVOICE_TYPE_NM;}
	public Vector getVINVOICE_CATEGORY() {return VINVOICE_CATEGORY;}
	public Vector getVFINANCIAL_YEAR() {return VFINANCIAL_YEAR;}
	public Vector getVINVOICE_REF() {return VINVOICE_REF;}
	public Vector getVLINE_NO() {return VLINE_NO;}
	public Vector getVLINE_DESC() {return VLINE_DESC;}
	public Vector getVUNIT() {return VUNIT;}
	public Vector getVQTY() {return VQTY;}
	public Vector getVRATE() {return VRATE;}
	public Vector getVAMOUNT() {return VAMOUNT;}
	public Vector getVBILLING_FREQ_FLAG() {return VBILLING_FREQ_FLAG;}
	public Vector getVBILLING_FREQ_NM() {return VBILLING_FREQ_NM;}
	public Vector getVEXCHNG_RATE_CD() {return VEXCHNG_RATE_CD;}
	public Vector getVEXCHNG_RATE_NM() {return VEXCHNG_RATE_NM;}
	public Vector getVEXCHNG_RATE_FLAG() {return VEXCHNG_RATE_FLAG;}
	public Vector getVINV_CHECKED_FLAG() {return VINV_CHECKED_FLAG;}
	public Vector getVINV_APPROVED_FLAG() {return VINV_APPROVED_FLAG;}
	public Vector getVBU_STATE_TIN() {return VBU_STATE_TIN;}
	public Vector getVINVOICE_EXIST() {return VINVOICE_EXIST;}
	public Vector getVAGMT_BASE() {return VAGMT_BASE;}
	public Vector getVMULTI_TAX_STRUCT() {return VMULTI_TAX_STRUCT;}
	public Vector getVMAIN_MULTI_TAX_STRUCT() {return VMAIN_MULTI_TAX_STRUCT;}
	public Vector getVNEW_MULTI_TAX_STRUCT() {return VNEW_MULTI_TAX_STRUCT;}
	public Vector getVEMAIL_SENT() {return VEMAIL_SENT;}
	public Vector getVEMAIL_SENT_INFO() {return VEMAIL_SENT_INFO;}
	
	public Vector getVP_EXCHNG_RATE_CD() {return VP_EXCHNG_RATE_CD;}
	public Vector getVP_EXCHNG_RATE_VALUE() {return VP_EXCHNG_RATE_VALUE;}
	public Vector getVP_BG_COLOR() {return VP_BG_COLOR;}
	public Vector getVB_EXCHNG_RATE_CD() {return VB_EXCHNG_RATE_CD;}
	public Vector getVB_EXCHNG_RATE_VALUE() {return VB_EXCHNG_RATE_VALUE;}
	public Vector getVB_BG_COLOR() {return VB_BG_COLOR;}
	public Vector getVU_EXCHNG_RATE_CD() {return VU_EXCHNG_RATE_CD;}
	public Vector getVU_EXCHNG_RATE_VALUE() {return VU_EXCHNG_RATE_VALUE;}
	public Vector getVU_BG_COLOR() {return VU_BG_COLOR;}
	public Vector getVEXCHNG_RATE_CAL_CD() {return VEXCHNG_RATE_CAL_CD;}
	public Vector getVEXCHNG_RATE_CAL_VAL() {return VEXCHNG_RATE_CAL_VAL;}
	public Vector getVEXCHNG_RATE_CAL_COLOR() {return VEXCHNG_RATE_CAL_COLOR;}
	
	public Vector getVALLOCATION_DT() {return VALLOCATION_DT;}
	public Vector getVPRICE() {return VPRICE;}
	public Vector getVALLOCATION_QTY() {return VALLOCATION_QTY;}
	public Vector getVAMOUNT_USD() {return VAMOUNT_USD;}
	public Vector getVAMOUNT_INR() {return VAMOUNT_INR;}
	
	public Vector getVPDF_COL1() {return VPDF_COL1;}
	public Vector getVPDF_COL2() {return VPDF_COL2;}
	public Vector getVPDF_COL3() {return VPDF_COL3;}
	public Vector getVPDF_COL4() {return VPDF_COL4;}
	public Vector getVPDF_COL5() {return VPDF_COL5;}
	public Vector getVPDF_COL6() {return VPDF_COL6;}
	public Vector getVPDF_COL7() {return VPDF_COL7;}
	
	public Vector getVINVOICE_ID_SEQ() {return VINVOICE_ID_SEQ;}
	public Vector getVDIFF_COLOR() {return VDIFF_COLOR;}
	public Vector getVPDF_INV_FLAG() {return VPDF_INV_FLAG;}
	public Vector getVPDF_TYPE() {return VPDF_TYPE;}
	public Vector getVPDF_FILE_NAME() {return VPDF_FILE_NAME;}
	public Vector getVPDF_FILE_PATH() {return VPDF_FILE_PATH;}
	public Vector getVPDF_SIGNED_FLAG() {return VPDF_SIGNED_FLAG;}
	public Vector getVSIGN_PDF_TYPE() {return VSIGN_PDF_TYPE;}
	
	public Vector getVOTH_COUNTERPTY_CD() {return VOTH_COUNTERPTY_CD;}
	public Vector getVOTH_COUNTERPTY_ABBR() {return VOTH_COUNTERPTY_ABBR;}
	public Vector getVOTH_CONT_NO() {return VOTH_CONT_NO;}
	public Vector getVOTH_CONT_REV_NO() {return VOTH_CONT_REV_NO;}
	public Vector getVOTH_AGMT_NO() {return VOTH_AGMT_NO;}
	public Vector getVOTH_AGMT_REV_NO() {return VOTH_AGMT_REV_NO;}
	public Vector getVOTH_CONTRACT_TYPE() {return VOTH_CONTRACT_TYPE;}
	public Vector getVOTH_DEAL_NO() {return VOTH_DEAL_NO;}
	public Vector getVOTH_PLANT_SEQ() {return VOTH_PLANT_SEQ;}
	public Vector getVOTH_PLANT_ABBR() {return VOTH_PLANT_ABBR;}
	public Vector getVOTH_BU_PLANT_SEQ() {return VOTH_BU_PLANT_SEQ;}
	public Vector getVOTH_BU_PLANT_ABBR() {return VOTH_BU_PLANT_ABBR;}
	public Vector getVOTH_START_DT() {return VOTH_START_DT;}
	public Vector getVOTH_END_DT() {return VOTH_END_DT;}
	public Vector getVOTH_CONT_NAME() {return VOTH_CONT_NAME;}
	public Vector getVOTH_CONT_REF_NO() {return VOTH_CONT_REF_NO;}
	public Vector getVOTH_PERIOD_START_DT() {return VOTH_PERIOD_START_DT;}
	public Vector getVOTH_PERIOD_END_DT() {return VOTH_PERIOD_END_DT;}
	public Vector getVOTH_INVOICE_NO() {return VOTH_INVOICE_NO;}
	public Vector getVOTH_DIS_INVOICE_NO() {return VOTH_DIS_INVOICE_NO;}
	public Vector getVOTH_STATUS() {return VOTH_STATUS;}
	public Vector getVOTH_BILLING_FREQ_FLAG() {return VOTH_BILLING_FREQ_FLAG;}
	public Vector getVOTH_BILLING_FREQ_NM() {return VOTH_BILLING_FREQ_NM;}
	public Vector getVOTH_INVOICE_SEQ() {return VOTH_INVOICE_SEQ;}
	public Vector getVOTH_INVOICE_EXIST() {return VOTH_INVOICE_EXIST;}
	public Vector getVOTH_INV_CHECKED_FLAG() {return VOTH_INV_CHECKED_FLAG;}
	public Vector getVOTH_INV_APPROVED_FLAG() {return VOTH_INV_APPROVED_FLAG;}
	public Vector getVOTH_PDF_INV_FLAG() {return VOTH_PDF_INV_FLAG;}
	public Vector getVOTH_PDF_TYPE() {return VOTH_PDF_TYPE;}
	public Vector getVOTH_PDF_FILE_NAME() {return VOTH_PDF_FILE_NAME;}
	public Vector getVOTH_PDF_FILE_PATH() {return VOTH_PDF_FILE_PATH;}
	public Vector getVOTH_PDF_SIGNED_FLAG() {return VOTH_PDF_SIGNED_FLAG;}
	public Vector getVOTH_SIGN_PDF_TYPE() {return VOTH_SIGN_PDF_TYPE;}
	public Vector getVOTH_INVOICE_TYPE() {return VOTH_INVOICE_TYPE;}
	public Vector getVOTH_FINANCIAL_YEAR() {return VOTH_FINANCIAL_YEAR;}
	public Vector getVOTH_BU_STATE_TIN() {return VOTH_BU_STATE_TIN;}
	
	public Vector getVATT1_DATE() {return VATT1_DATE;}
	public Vector getVATT1_DCQ() {return VATT1_DCQ;}
	public Vector getVATT1_BUYNOM() {return VATT1_BUYNOM;}
	public Vector getVATT1_SELLNOM_PNG() {return VATT1_SELLNOM_PNG;}
	public Vector getVATT1_SELLNOM_REGAS() {return VATT1_SELLNOM_REGAS;}
	public Vector getVATT1_NG_PNG() {return VATT1_NG_PNG;}
	public Vector getVATT1_NG_REGAS() {return VATT1_NG_REGAS;}
	public Vector getVATT1_NG_TOT_DLV_GAS() {return VATT1_NG_TOT_DLV_GAS;}
	public Vector getVATT1_CUMULATIVE_QTY_BILLING_PERIOD() {return VATT1_CUMULATIVE_QTY_BILLING_PERIOD;}
	public Vector getVATT1_CUMULATIVE_QTY_TRANSCT_SUPPLY_PERIOD() {return VATT1_CUMULATIVE_QTY_TRANSCT_SUPPLY_PERIOD;}
	
	public Vector getVATT2_EXCHANGE_DESC() {return VATT2_EXCHANGE_DESC;}
	public Vector getVATT2_RATE() {return VATT2_RATE;}
	
	public Vector getVMAIL_PDF_TYPE() {return VMAIL_PDF_TYPE;}
	public Vector getVMAIL_PDF_TYPE_NM() {return VMAIL_PDF_TYPE_NM;}
	public Vector getVMAIL_FROM_LIST() {return VMAIL_FROM_LIST;}
	public Vector getVMAIL_TO_LIST() {return VMAIL_TO_LIST;}
	public Vector getVMAIL_CC_LIST() {return VMAIL_CC_LIST;}
	public Vector getVMAIL_BCC_LIST() {return VMAIL_BCC_LIST;}
	public Vector getVMAIL_SUBJECT() {return VMAIL_SUBJECT;}
	public Vector getVMAIL_ATTACHMENT() {return VMAIL_ATTACHMENT;}
	public Vector getVMAIL_ANNEXURE_ATTACHMENT_FLAG() {return VMAIL_ANNEXURE_ATTACHMENT_FLAG;}
	public Vector getVMAIL_ATTACHMENT_PATH() {return VMAIL_ATTACHMENT_PATH;}
	public Vector getVMAIL_ANNEXURE_ATTACHMENT_PATH() {return VMAIL_ANNEXURE_ATTACHMENT_PATH;}
	public Vector getVMAIL_BODY() {return VMAIL_BODY;}
	
	public Vector getVANNEXURE_SEQ() {return VANNEXURE_SEQ;}
	public Vector getVANNEXURE_FILE_NM() {return VANNEXURE_FILE_NM;}
	public Vector getVANNEXURE_FOLDER() {return VANNEXURE_FOLDER;}
	
	public Vector getVSAP_APPROVAL_FLAG() {return VSAP_APPROVAL_FLAG;}
	public Vector getVOTH_SAP_APPROVAL_FLAG() {return VOTH_SAP_APPROVAL_FLAG;}
	public Vector getVFILTER_CONT_TYPE() {return VFILTER_CONT_TYPE;}
	public Vector getVFILTER_CONT_NAME() {return VFILTER_CONT_NAME;}
	
	public Vector getVINVOICE_LIST_ABBR() {return VINVOICE_LIST_ABBR;}
	public Vector getVINVOICE_LIST_NAME() {return VINVOICE_LIST_NAME;}
	public Vector getVINDEX() {return VINDEX;}
	public Vector getVCARGO_NO() {return VCARGO_NO;}
	public Vector getVIS_IRN_GENERATED() {return VIS_IRN_GENERATED;}
	public Vector getVOTH_IS_IRN_GENERATED() {return VOTH_IS_IRN_GENERATED;}
	public Vector getVINV_FLAG() {return VINV_FLAG;}
	public Vector getVBOE_ENTRY() {return VBOE_ENTRY;}
	public Vector getVCARGO_NM() {return VCARGO_NM;}
	public Vector getVATT_INVOICE_NO() {return VATT_INVOICE_NO;}

	public Vector getVORI_INV_SEQ() {return VORI_INV_SEQ;}
	public Vector getVORI_INVOICE_NO() {return VORI_INVOICE_NO;}
	public Vector getVORI_INVOICE_DT() {return VORI_INVOICE_DT;}
	public Vector getVORI_INVOICE_DUE_DT() {return VORI_INVOICE_DUE_DT;}
	public Vector getVORI_INVOICE_PAYRECV_DT() {return VORI_INVOICE_PAYRECV_DT;}
	public Vector getVORI_FINANCIAL_YEAR() {return VORI_FINANCIAL_YEAR;}
	public Vector getVORI_INV_FLAG() {return VORI_INV_FLAG;}
	
	public Vector getVSTORAGE_DATE() {return VSTORAGE_DATE;}
	public Vector getVSTORAGE_INVENTORY() {return VSTORAGE_INVENTORY;}
	public Vector getVOFFTAKE_QTY() {return VOFFTAKE_QTY;}
	public Vector getVSTORAGE_CHARGE() {return VSTORAGE_CHARGE;}
	public Vector getVSTORAGE_AMT() {return VSTORAGE_AMT;}
	public Vector getVUSER_DEFINE() {return VUSER_DEFINE;}
	public Vector getVRATE_TYPE() {return VRATE_TYPE;}
	public Vector getVDISCOUNT_FLAG() {return VDISCOUNT_FLAG;}
	public Vector getVRECEIPT_VOUCHER_MST() {return VRECEIPT_VOUCHER_MST;}
	public Vector getVEXISTING_RECEIPT_VOUCHER() {return VEXISTING_RECEIPT_VOUCHER;}
	
	public Vector getVINVOICE_NO_LIST() {return VINVOICE_NO_LIST;}
	public Vector getVCRITERIA_FLAG() {return VCRITERIA_FLAG;}
	public Vector getVCRITERIA_NAME() {return VCRITERIA_NAME;}
	public Vector getVCRITERIA_HIDE() {return VCRITERIA_HIDE;}
	public Vector getVCREDIT_DEBIT_NO() {return VCREDIT_DEBIT_NO;}
	public Vector getVREF_NO() {return VREF_NO;}
	public Vector getVCRDR_CRITERIA() {return VCRDR_CRITERIA;}
	public Vector getVCRDR_GEN_TYPE() {return VCRDR_GEN_TYPE;}
	public Vector getVACTION_FLAG() {return VACTION_FLAG;}
	public Vector getVSEQ_NO() {return VSEQ_NO;}
	public Vector getVRE_PRINT_PDF() {return VRE_PRINT_PDF;}
	public Vector getVOTH_RE_PRINT_PDF() {return VOTH_RE_PRINT_PDF;}
	public Vector getVREMAINING_DAYS() {return VREMAINING_DAYS;}
	public Vector getVCOUNT() {return VCOUNT;}
	
	public Vector getVOTH_INVOICE_DT() {return VOTH_INVOICE_DT;}
	
	String billing_freq_nm="";
	String couterpty_abbr="";
	String couterpty_nm="";
	String deal_no="";
	String plant_abbr="";
	String bu_plant_abbr="";
	String qty_mmbtu="";
	String price="";
	String price_cd="";
	String price_cd_nm="";
	String cont_start_dt="";
	String contact_person_cd="0";
	String contact_person_nm="";
	String bu_contact_person_cd="0";
	String bu_contact_person_nm="";
	String invoice_raised_in="";
	String invoice_raised_in_nm="";
	String payment_done_in="";
	String payment_done_in_nm="";
	String due_days="";
	String exchng_rate_cd="";
	String exchng_rate_cal="";
	String exchang_rate="";
	String exchang_rate_dt="";
	String exchang_rate_type="";
	String exchang_criteria="";
	String invoice_seq="";
	String invoice_no="";
	String invoice_ref="";
	String invoice_dt = "";
	String invoice_due_dt = "";
	String consider_due_dt_in="";
	String exclude_sat="";
	String holiday_state="";
	String gross_amt="";
	String gross_amt1="";
	String remark_1="";
	String remark_2="";
	String last_avlb_exchng_dt="";
	String lable_inv_criteria="";
	String lable_inv_date="";
	String correction_msg="";
	String daily_tot_amt_inr="";
	String daily_tot_amt_usd="";
	String daily_tot_qty="";
	String tax_amt="";
	String tax_struct_cd="";
	String tax_struct_dt="";
	String tax_struct_dtl="";
	String tax_info="";
	String tax_factor="";
	String invoice_amt="";
	String net_payable="";
	String previousFinancialYear="";
	String total_transaction_amt="";
	String applicable_flag="";
	String applicable_amt="";
	String applicable_abbr="";
	String sellerTurnoverFlag="";
	String buyerTurnoverFlag="";
	String TCS_factor="";
	String total_InvoiceAmt="";
	String inv_period_start_dt="";
	String inv_period_end_dt="";
	String signingDt="";
	String agmtSigningDt="";
	String contract_ref="";
	String tcs_struct_cd="";
	String tcs_struct_dt="";
	String tcs_struct_info="";
	String tds_amt="";
	String tds_factor="";
	String tds_struct_cd="";
	String tds_struct_dt="";
	String tds_struct_info="";
	
	String int_cal_rate_cd="";
	String int_cal_rate_nm="";
	String int_cal_rate_value="";
	String int_cal_sign="";
	String int_cal_percentage="";
	String int_total_percentage="";
	
	String ori_invoice_seq="";
	public void setOri_invoice_seq(String ori_invoice_seq) {this.ori_invoice_seq = ori_invoice_seq;}
	String ori_invoice_no="";
	public void setOri_invoice_no(String ori_invoice_no) {this.ori_invoice_no = ori_invoice_no;}

	String ori_inv_due_dt="";
	String ori_inv_net_amt="";
	String ori_inv_payrecv_amt="";
	String ori_inv_payrecv_dt="";
	String ori_inv_dt="";
	String ori_gross_amt= "";
	String ori_tax_amt= "";
	String ori_tcs_tds= "";
	String ori_tcs_amt= "";
	String ori_tcs_per= "";
	String ori_tds_amt= "";
	String ori_tds_per= "";
	
	String cont_end_dt="";
	
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
	String dda_dt="";
	
	String invoice_id_seq="";
	String fixed_exchng_val="";
	
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
	String invoice_category="";
	String num_line="1";
	String linked_invoice="";
	String note="";
	String other_inv_str="";
	String amount_in_word="";
	String tax_struct_info="";
	String invoice_adj_amt = "";
	
	String agmt_base = "";
	String transportation_charges = "";
	String transportation_amount = "";
	String gross_include_transport_tariff = "";
	String marketing_margin = "";
	String marketing_margin_amount = "";
	String other_charges = "";
	String other_charges_amount = "";
	
	String dcq = "";
	
	String total_exchng_label = "";
	String total_exchng_label_rate = "";
	String source = "";
	
	String inv_entered_on="";
	String inv_approved_on="";
	
	String sub_inv_type="";
	
	String plant_gstin_no="";
	String bu_gstin_no="";
	String sug_percentage="";
	String sug_qty="";
	String discount_days="";
	String storage_start_dt="";
	String storage_end_dt="";
	String arrivalDt="";
	String advance_adj_flag="";
	String criteri_formula="";
	String reason="";
	
	String imb_qty="";
	String imb_amt="";
	String ship_or_pay_qty="";
	String ship_or_pay_amt="";
	String ovrun_qty="";
	String ovrun_amt="";
	String att_file_name="";
	
	public String getBilling_freq_nm() {return billing_freq_nm;}
	public String getCouterpty_abbr() {return couterpty_abbr;}
	public String getCouterpty_nm() {return couterpty_nm;}
	public String getDeal_no() {return deal_no;}
	public String getPlant_abbr() {return plant_abbr;}
	public String getBu_plant_abbr() {return bu_plant_abbr;}
	public String getQty_mmbtu() {return qty_mmbtu;}
	public String getPrice() {return price;}
	public String getPrice_cd() {return price_cd;}
	public String getPrice_cd_nm() {return price_cd_nm;}
	public String getCont_start_dt() {return cont_start_dt;}
	public String getCont_end_dt() {return cont_end_dt;}
	public String getContact_person_cd() {return contact_person_cd;}
	public String getContact_person_nm() {return contact_person_nm;}
	public String getBu_contact_person_cd() {return bu_contact_person_cd;}
	public String getBu_contact_person_nm() {return bu_contact_person_nm;}
	public String getInvoice_raised_in() {return invoice_raised_in;}
	public String getInvoice_raised_in_nm() {return invoice_raised_in_nm;}
	public String getPayment_done_in() {return payment_done_in;}
	public String getPayment_done_in_nm() {return payment_done_in_nm;}
	public String getDue_days() {return due_days;}
	public String getExchng_rate_cd() {return exchng_rate_cd;}
	public String getExchng_rate_cal() {return exchng_rate_cal;}
	public String getExchang_rate() {return exchang_rate;}
	public String getExchang_rate_dt() {return exchang_rate_dt;}
	public String getExchang_rate_type() {return exchang_rate_type;}
	public String getExchang_criteria() {return exchang_criteria;}
	public String getInvoice_seq() {return invoice_seq;}
	public String getInvoice_no() {return invoice_no;}
	public String getInvoice_ref() {return invoice_ref;}
	public String getInvoice_dt() {return invoice_dt;}
	public String getInvoice_due_dt() {return invoice_due_dt;}
	public String getConsider_due_dt_in() {return consider_due_dt_in;}
	public String getExclude_sat() {return exclude_sat;}
	public String getHoliday_state() {return holiday_state;}

	public String getInt_cal_rate_cd() {return int_cal_rate_cd;}
	public String getInt_cal_rate_nm() {return int_cal_rate_nm;}
	public String getInt_cal_rate_value() {return int_cal_rate_value;}
	public String getInt_cal_sign() {return int_cal_sign;}
	public String getInt_cal_percentage() {return int_cal_percentage;}
	public String getInt_total_percentage() {return int_total_percentage;}

	public String getOri_invoice_no() {return ori_invoice_no;}
	public String getOri_inv_net_amt() {return ori_inv_net_amt;}
	public String getOri_inv_payrecv_amt() {return ori_inv_payrecv_amt;}
	public String getOri_inv_due_dt() {return ori_inv_due_dt;}
	public String getOri_inv_payrecv_dt() {return ori_inv_payrecv_dt;}
	public String getOri_inv_dt() {return ori_inv_dt;}
	public String getOri_gross_amt() {return ori_gross_amt;}
	public String getOri_tax_amt() {return ori_tax_amt;}
	public String getOri_tcs_tds() {return ori_tcs_tds;}
	public String getOri_tcs_amt() {return ori_tcs_amt;}
	public String getOri_tcs_per() {return ori_tcs_per;}
	public String getOri_tds_amt() {return ori_tds_amt;}
	public String getOri_tds_per() {return ori_tds_per;}
	
	public String getGross_amt() {return gross_amt;}
	public String getGross_amt1() {return gross_amt1;}
	public String getRemark_1() {return remark_1;}
	public String getRemark_2() {return remark_2;}
	public String getLast_avlb_exchng_dt() {return last_avlb_exchng_dt;}
	public String getLable_inv_criteria() {return lable_inv_criteria;}
	public String getLable_inv_date() {return lable_inv_date;}
	public String getCorrection_msg() {return correction_msg;}
	public String getDaily_tot_amt_inr() {return daily_tot_amt_inr;}
	public String getDaily_tot_amt_usd() {return daily_tot_amt_usd;}
	public String getDaily_tot_qty() {return daily_tot_qty;}
	public String getTax_amt() {return tax_amt;}
	public String getTax_struct_cd() {return tax_struct_cd;}
	public String getTax_struct_dt() {return tax_struct_dt;}
	public String getTax_struct_dtl() {return tax_struct_dtl;}
	public String getTax_info() {return tax_info;}
	public String getTax_factor() {return tax_factor;}
	public String getInvoice_amt() {return invoice_amt;}
	public String getNet_payable() {return net_payable;}
	public String getPreviousFinancialYear() {return previousFinancialYear;}
	public String getTotal_transaction_amt() {return total_transaction_amt;}
	public String getApplicable_flag() {return applicable_flag;}
	public String getApplicable_amt() {return applicable_amt;}
	public String getApplicable_abbr() {return applicable_abbr;}
	public String getSellerTurnoverFlag() {return sellerTurnoverFlag;}
	public String getBuyerTurnoverFlag() {return buyerTurnoverFlag;}
	public String getTCS_factor() {return TCS_factor;}
	public String getTotal_InvoiceAmt() {return total_InvoiceAmt;}
	public String getInv_period_start_dt() {return inv_period_start_dt;}
	public String getInv_period_end_dt() {return inv_period_end_dt;}
	public String getSigningDt() {return signingDt;}
	public String getAgmtSigningDt() {return agmtSigningDt;}
	public String getContract_ref() {return contract_ref;}
	public String getTcs_struct_cd() {return tcs_struct_cd;}
	public String getTcs_struct_dt() {return tcs_struct_dt;}
	public String getTcs_struct_info() {return tcs_struct_info;}
	public String getTds_amt() {return tds_amt;}
	public String getTds_factor() {return tds_factor;}
	public String getTds_struct_cd() {return tds_struct_cd;}
	public String getTds_struct_dt() {return tds_struct_dt;}
	public String getTds_struct_info() {return tds_struct_info;}
	
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
	public String getDda_dt() {return dda_dt;}
	
	public String getInvoice_id_seq() {return invoice_id_seq;}
	
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
	public String getInvoice_category() {return invoice_category;}
	public String getNum_line() {return num_line;}
	public String getLinked_invoice() {return linked_invoice;}
	public String getNote() {return note;}
	public String getOther_inv_str() {return other_inv_str;}
	public String getAmount_in_word() {return amount_in_word;}
	public String getTax_struct_info() {return tax_struct_info;}
	public String getInvoice_adj_amt() {return invoice_adj_amt;}
	
	public String getAgmt_base() {return agmt_base;}
	public String getTransportation_charges() {return transportation_charges;}
	public String getTransportation_amount() {return transportation_amount;}
	public String getGross_include_transport_tariff() {return gross_include_transport_tariff;}
	public String getMarketing_margin() {return marketing_margin;}
	public String getMarketing_margin_amount() {return marketing_margin_amount;}
	public String getOther_charges() {return other_charges;}
	public String getOther_charges_amount() {return other_charges_amount;}
	
	public String getDcq() {return dcq;}
	
	public String getTotal_exchng_label() {return total_exchng_label;}
	public String getTotal_exchng_label_rate() {return total_exchng_label_rate;}
	public String getSource() {return source;}
	
	public String getInv_entered_on() {return inv_entered_on;}
	public String getInv_approved_on() {return inv_approved_on;}
	
	public String getSub_inv_type() {return sub_inv_type;}
	public String getPlant_gstin_no() {return plant_gstin_no;}
	public String getBu_gstin_no() {return bu_gstin_no;}
	public String getSug_percentage() {return sug_percentage;}
	public String getSug_qty() {return sug_qty;}
	public String getDiscount_days() {return discount_days;}
	public String getStorage_start_dt() {return storage_start_dt;}
	public String getStorage_end_dt() {return storage_end_dt;}
	public String getArrivalDt() {return arrivalDt;}
	public String getAdvance_adj_flag() {return advance_adj_flag;}
	public String getCriteri_formula() {return criteri_formula;}
	public String getReason() {return reason;}
	
	public String getImb_qty() {return imb_qty;}
	public String getImb_amt() {return imb_amt;}
	public String getShip_or_pay_qty() {return ship_or_pay_qty;}
	public String getShip_or_pay_amt() {return ship_or_pay_amt;}
	public String getOvrun_qty() {return ovrun_qty;}
	public String getOvrun_amt() {return ovrun_amt;}
	public String getAtt_file_name() {return att_file_name;}

	boolean submission_chk = false;
	boolean correction_needed = false;
	double mdcq_percentage=100;
	boolean isGrossIncTriff = false;
	
	public boolean getSubmission_chk() {return submission_chk;}
	public boolean getCorrection_needed() {return correction_needed;}
	public boolean getIsGrossIncTriff() {return isGrossIncTriff;}
	
	String total_dcq="";
	String total_buy_nom="";
	String total_sell_pnq="";
	String total_sell_regas="";
	String total_delv_pnq="";
	String total_delv_regas="";
    String total_delv_qty="";
    
    public String getTotal_dcq() {return total_dcq;}
	public String getTotal_buy_nom() {return total_buy_nom;}
	public String getTotal_sell_pnq() {return total_sell_pnq;}
	public String getTotal_sell_regas() {return total_sell_regas;}
	public String getTotal_delv_pnq() {return total_delv_pnq;}
	public String getTotal_delv_regas() {return total_delv_regas;}
	public String getTotal_delv_qty() {return total_delv_qty;}
	
	
	//String new_couterpty_abbr = "";
	//String new_couterpty_nm = "";
	//String new_deal_no = "";
	//String new_plant_abbr = "";
	//String new_bu_plant_abbr = "";
	String new_qty_mmbtu = "";
	String new_price = "";
	String new_price_cd = "";
	String new_price_cd_nm = "";
	//String new_contact_person_cd = "0";
	//String new_bu_contact_person_cd = "0";
	String new_invoice_raised_in = "";
	String new_invoice_raised_in_nm = "";
	String new_exchng_rate_cd = "";
	String new_exchng_rate_cal = "";
	String new_exchang_rate = "";
	String new_exchang_rate_dt = "";
	String new_exchang_rate_type = "";
	String new_exchang_criteria = "";
	String new_invoice_seq = "";
	String new_invoice_no = "";
	String new_invoice_dt = "";
	String new_invoice_due_dt = "";
	String new_gross_amt = "";
	String new_gross_amt1 = "";
	String new_remark_1 = "";
	String new_remark_2 = "";
	String new_last_avlb_exchng_dt = "";
	String new_lable_inv_criteria = "";
	String new_lable_inv_date = "";
	String new_correction_msg = "";
	String new_daily_tot_amt_inr = "";
	String new_daily_tot_amt_usd = "";
	String new_daily_tot_qty = "";
	String new_tax_amt = "";
	String new_tax_struct_cd = "";
	String new_tax_struct_dt = "";
	String new_tax_struct_dtl = "";
	String new_tax_info = "";
	String new_tax_factor = "";
	String new_invoice_amt = "";
	String new_net_payable = "";
	String new_applicable_flag = "";
	String new_applicable_amt = "";
	String new_applicable_abbr = "";
	String new_TCS_factor = "";
	String new_contract_ref = "";
	String new_tcs_struct_cd = "";
	String new_tcs_struct_dt = "";
	String new_tds_amt = "";
	String new_tds_factor = "";
	String new_tds_struct_cd = "";
	String new_tds_struct_dt = "";
	String new_invoice_id_seq = "";
	String new_agmt_base = "";
	String new_transportation_charges = "";
	String new_transportation_amount = "";
	String new_gross_include_transport_tariff = "";
	String new_marketing_margin = "";
	String new_marketing_margin_amount = "";
	String new_other_charges = "";
	String new_other_charges_amount = "";
	
	//public String getNew_couterpty_abbr() { return new_couterpty_abbr; }
    //public String getNew_couterpty_nm() { return new_couterpty_nm; }
    //public String getNew_deal_no() { return new_deal_no; }
    //public String getNew_plant_abbr() { return new_plant_abbr; }
    //public String getNew_bu_plant_abbr() { return new_bu_plant_abbr; }
    public String getNew_qty_mmbtu() { return new_qty_mmbtu; }
    public String getNew_price() { return new_price; }
    public String getNew_price_cd() { return new_price_cd; }
    public String getNew_price_cd_nm() { return new_price_cd_nm; }
    //public String getNew_contact_person_cd() { return new_contact_person_cd; }
    //public String getNew_bu_contact_person_cd() { return new_bu_contact_person_cd; }
    public String getNew_invoice_raised_in() { return new_invoice_raised_in; }
    public String getNew_invoice_raised_in_nm() { return new_invoice_raised_in_nm; }
    public String getNew_exchng_rate_cd() { return new_exchng_rate_cd; }
    public String getNew_exchng_rate_cal() { return new_exchng_rate_cal; }
    public String getNew_exchang_rate() { return new_exchang_rate; }
    public String getNew_exchang_rate_dt() { return new_exchang_rate_dt; }
    public String getNew_exchang_rate_type() { return new_exchang_rate_type; }
    public String getNew_exchang_criteria() { return new_exchang_criteria; }
    public String getNew_invoice_seq() { return new_invoice_seq; }
    public String getNew_invoice_no() { return new_invoice_no; }
    public String getNew_invoice_dt() { return new_invoice_dt; }
    public String getNew_invoice_due_dt() { return new_invoice_due_dt; }
    public String getNew_gross_amt() { return new_gross_amt; }
    public String getNew_gross_amt1() { return new_gross_amt1; }
    public String getNew_remark_1() { return new_remark_1; }
    public String getNew_remark_2() { return new_remark_2; }
    public String getNew_last_avlb_exchng_dt() { return new_last_avlb_exchng_dt; }
    public String getNew_lable_inv_criteria() { return new_lable_inv_criteria; }
    public String getNew_lable_inv_date() { return new_lable_inv_date; }
    public String getNew_correction_msg() { return new_correction_msg; }
    public String getNew_daily_tot_amt_inr() { return new_daily_tot_amt_inr; }
    public String getNew_daily_tot_amt_usd() { return new_daily_tot_amt_usd; }
    public String getNew_daily_tot_qty() { return new_daily_tot_qty; }
    public String getNew_tax_amt() { return new_tax_amt; }
    public String getNew_tax_struct_cd() { return new_tax_struct_cd; }
    public String getNew_tax_struct_dt() { return new_tax_struct_dt; }
    public String getNew_tax_struct_dtl() { return new_tax_struct_dtl; }
    public String getNew_tax_info() { return new_tax_info; }
    public String getNew_tax_factor() { return new_tax_factor; }
    public String getNew_invoice_amt() { return new_invoice_amt; }
    public String getNew_net_payable() { return new_net_payable; }
    public String getNew_applicable_flag() { return new_applicable_flag; }
    public String getNew_applicable_amt() { return new_applicable_amt; }
    public String getNew_applicable_abbr() { return new_applicable_abbr; }
    public String getNew_TCS_factor() { return new_TCS_factor; }
    public String getNew_contract_ref() { return new_contract_ref; }
    public String getNew_tcs_struct_cd() { return new_tcs_struct_cd; }
    public String getNew_tcs_struct_dt() { return new_tcs_struct_dt; }
    public String getNew_tds_amt() { return new_tds_amt; }
    public String getNew_tds_factor() { return new_tds_factor; }
    public String getNew_tds_struct_cd() { return new_tds_struct_cd; }
    public String getNew_tds_struct_dt() { return new_tds_struct_dt; }
    public String getNew_invoice_id_seq() { return new_invoice_id_seq; }
    public String getNew_agmt_base() { return new_agmt_base; }
    public String getNew_transportation_charges() { return new_transportation_charges; }
    public String getNew_transportation_amount() { return new_transportation_amount; }
    public String getNew_gross_include_transport_tariff() { return new_gross_include_transport_tariff; }
    public String getNew_marketing_margin() { return new_marketing_margin; }
    public String getNew_marketing_margin_amount() { return new_marketing_margin_amount; }
    public String getNew_other_charges() { return new_other_charges; }
    public String getNew_other_charges_amount() { return new_other_charges_amount; }
    
    //String main_couterpty_abbr = "";
    //String main_couterpty_nm = "";
    //String main_deal_no = "";
    //String main_plant_abbr = "";
    //String main_bu_plant_abbr = "";
    String main_qty_mmbtu = "";
    String main_price = "";
    String main_price_cd = "";
    String main_price_cd_nm = "";
    //String main_contact_person_cd = "0";
    //String main_bu_contact_person_cd = "0";
    String main_invoice_raised_in = "";
    String main_invoice_raised_in_nm = "";
    String main_exchng_rate_cd = "";
    String main_exchng_rate_cal = "";
    String main_exchang_rate = "";
    String main_exchang_rate_dt = "";
    String main_exchang_rate_type = "";
    String main_exchang_criteria = "";
    String main_invoice_seq = "";
    String main_invoice_no = "";
    String main_invoice_dt = "";
    String main_invoice_due_dt = "";
    String main_gross_amt = "";
    String main_gross_amt1 = "";
    String main_remark_1 = "";
    String main_remark_2 = "";
    String main_last_avlb_exchng_dt = "";
    String main_lable_inv_criteria = "";
    String main_lable_inv_date = "";
    String main_correction_msg = "";
    String main_daily_tot_amt_inr = "";
    String main_daily_tot_amt_usd = "";
    String main_daily_tot_qty = "";
    String main_tax_amt = "";
    String main_tax_struct_cd = "";
    String main_tax_struct_dt = "";
    String main_tax_struct_dtl = "";
    String main_tax_info = "";
    String main_tax_factor = "";
    String main_invoice_amt = "";
    String main_net_payable = "";
    String main_applicable_flag = "";
    String main_applicable_amt = "";
    String main_applicable_abbr = "";
    String main_TCS_factor = "";
    String main_contract_ref = "";
    String main_tcs_struct_cd = "";
    String main_tcs_struct_dt = "";
    String main_tds_amt = "";
    String main_tds_factor = "";
    String main_tds_struct_cd = "";
    String main_tds_struct_dt = "";
    String main_invoice_id_seq = "";
    String main_agmt_base = "";
    String main_transportation_charges = "";
    String main_transportation_amount = "";
    String main_gross_include_transport_tariff = "";
    String main_marketing_margin = "";
    String main_marketing_margin_amount = "";
    String main_other_charges = "";
    String main_other_charges_amount = "";

    // Getter methods
    //public String getMain_couterpty_abbr() { return main_couterpty_abbr; }
    //public String getMain_couterpty_nm() { return main_couterpty_nm; }
    //public String getMain_deal_no() { return main_deal_no; }
    //public String getMain_plant_abbr() { return main_plant_abbr; }
    //public String getMain_bu_plant_abbr() { return main_bu_plant_abbr; }
    public String getMain_qty_mmbtu() { return main_qty_mmbtu; }
    public String getMain_price() { return main_price; }
    public String getMain_price_cd() { return main_price_cd; }
    public String getMain_price_cd_nm() { return main_price_cd_nm; }
    //public String getMain_contact_person_cd() { return main_contact_person_cd; }
    //public String getMain_bu_contact_person_cd() { return main_bu_contact_person_cd; }
    public String getMain_invoice_raised_in() { return main_invoice_raised_in; }
    public String getMain_invoice_raised_in_nm() { return main_invoice_raised_in_nm; }
    public String getMain_exchng_rate_cd() { return main_exchng_rate_cd; }
    public String getMain_exchng_rate_cal() { return main_exchng_rate_cal; }
    public String getMain_exchang_rate() { return main_exchang_rate; }
    public String getMain_exchang_rate_dt() { return main_exchang_rate_dt; }
    public String getMain_exchang_rate_type() { return main_exchang_rate_type; }
    public String getMain_exchang_criteria() { return main_exchang_criteria; }
    public String getMain_invoice_seq() { return main_invoice_seq; }
    public String getMain_invoice_no() { return main_invoice_no; }
    public String getMain_invoice_dt() { return main_invoice_dt; }
    public String getMain_invoice_due_dt() { return main_invoice_due_dt; }
    public String getMain_gross_amt() { return main_gross_amt; }
    public String getMain_gross_amt1() { return main_gross_amt1; }
    public String getMain_remark_1() { return main_remark_1; }
    public String getMain_remark_2() { return main_remark_2; }
    public String getMain_last_avlb_exchng_dt() { return main_last_avlb_exchng_dt; }
    public String getMain_lable_inv_criteria() { return main_lable_inv_criteria; }
    public String getMain_lable_inv_date() { return main_lable_inv_date; }
    public String getMain_correction_msg() { return main_correction_msg; }
    public String getMain_daily_tot_amt_inr() { return main_daily_tot_amt_inr; }
    public String getMain_daily_tot_amt_usd() { return main_daily_tot_amt_usd; }
    public String getMain_daily_tot_qty() { return main_daily_tot_qty; }
    public String getMain_tax_amt() { return main_tax_amt; }
    public String getMain_tax_struct_cd() { return main_tax_struct_cd; }
    public String getMain_tax_struct_dt() { return main_tax_struct_dt; }
    public String getMain_tax_struct_dtl() { return main_tax_struct_dtl; }
    public String getMain_tax_info() { return main_tax_info; }
    public String getMain_tax_factor() { return main_tax_factor; }
    public String getMain_invoice_amt() { return main_invoice_amt; }
    public String getMain_net_payable() { return main_net_payable; }
    public String getMain_applicable_flag() { return main_applicable_flag; }
    public String getMain_applicable_amt() { return main_applicable_amt; }
    public String getMain_applicable_abbr() { return main_applicable_abbr; }
    public String getMain_TCS_factor() { return main_TCS_factor; }
    public String getMain_contract_ref() { return main_contract_ref; }
    public String getMain_tcs_struct_cd() { return main_tcs_struct_cd; }
    public String getMain_tcs_struct_dt() { return main_tcs_struct_dt; }
    public String getMain_tds_amt() { return main_tds_amt; }
    public String getMain_tds_factor() { return main_tds_factor; }
    public String getMain_tds_struct_cd() { return main_tds_struct_cd; }
    public String getMain_tds_struct_dt() { return main_tds_struct_dt; }
    public String getMain_invoice_id_seq() { return main_invoice_id_seq; }
    public String getMain_agmt_base() { return main_agmt_base; }
    public String getMain_transportation_charges() { return main_transportation_charges; }
    public String getMain_transportation_amount() { return main_transportation_amount; }
    public String getMain_gross_include_transport_tariff() { return main_gross_include_transport_tariff; }
    public String getMain_marketing_margin() { return main_marketing_margin; }
    public String getMain_marketing_margin_amount() { return main_marketing_margin_amount; }
    public String getMain_other_charges() { return main_other_charges; }
    public String getMain_other_charges_amount() { return main_other_charges_amount; }
    
    String changed_qty_mmbtu = "";
    
    public String getChanged_qty_mmbtu() { return changed_qty_mmbtu; }
}
