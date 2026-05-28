package com.etrm.fms.sales_invoice;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DB_AllocationUtil;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.TaxCalculator;
import com.etrm.fms.util.UtilBean;

public class DataBean_Sales_Drcr_note 
{
	String db_src_file_name="DataBean_Sales_Drcr_note.java";
	
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
	ResultSet rset;
	ResultSet rset0;
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset5;
	ResultSet rset6;
	ResultSet rset7;
	String queryString="";
	String queryString0="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";
	String queryString6="";
	String queryString7="";
	Vector VATT2_EXCHANGE_DESC = new Vector();
	
	UtilBean utilBean = new UtilBean();
	DateUtil utilDate = new DateUtil();
	TaxCalculator TaxCalc = new TaxCalculator(); 
	DB_AllocationUtil utilAlloc = new DB_AllocationUtil();
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf3 = new DecimalFormat("###########0.000");
	NumberFormat nf0 = new DecimalFormat("###########0.0");

	
	String criteria_desc="";
	
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
	    			if(callFlag.equalsIgnoreCase("SET_DR_CR_NOTE"))   //Deep20250819
	    			{
	    				setHeaderDisplaySegment();
	    				//setDisplaySegment();
	    				getBillingCyclePeriod();
	    				getCustomerCounterpartyList();
	    				getDrCrNoteList(); 
	    			}
	    			else if(callFlag.equalsIgnoreCase("DEBIT_CREDIT_NOTE_LIST"))   //Deep20250829
	    			{
	    				
	    				VINVOICE_LIST_ABBR.add("INV_HEAD");
	    				VINVOICE_LIST_ABBR.add("LTCORA_INV_HEAD");
	    				
	    				VINVOICE_LIST_NAME.add("RLNG Debit/Credit Note Generation");
	    				VINVOICE_LIST_NAME.add("LTCORA Debit/Credit Note Generation");
	    				
	    				getBillingCyclePeriod();
	    				getDrCrNote();
	    			}
	    			else if(callFlag.equalsIgnoreCase("DEBIT_CREDIT_NOTE_GENERATION")) //Deep20250829
	    			{
	    				getContractDetail();
	    				getContractBillingDetail();
	    				getContactPerson();
	    				getBuContactPerson();
	    				getDrcrExistingNoteDtl();
	    				if(!operation.equals("MODIFY") && !submission_chk)
	    				{
	    					getRemarks();
	    				}
	    			} 																	//Deep20250819
	    			else if(callFlag.equalsIgnoreCase("DRCR_NOTE_REPORT"))
	    			{
	    				getDrcrNoteDetail();
	    				if(activityFlag.equals("APPROVE"))
	    				{
	    					getDrcrInvoiceNumber();
	    					//getAttachment1();
	    				}
	    				
//	    				if(is_attachment.equals("1"))
//	    				{
//	    					if(inv_flag.equals("UG"))
//	    					{
//	    						getSug_Attachment1();
//	    					}
//	    					else if(inv_flag.equals("ST"))
//	    					{
//	    						getStorage_Attachment1();
//	    					}
//	    					else
//	    					{
//	    						getAttachment1();
//	    					}
//	    				}
//	    				else if(is_attachment.equals("2"))
//	    				{
//	    					if(inv_flag.equals("UG"))
//	    					{
//	    						getSug_Attachment2();
//	    					}
//	    					else
//	    					{
//	    						getAttachment2();
//	    					}
//	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("SEND_INVOICE_MAIL"))
	    			{
	    				getSendInvoiceMailDetail();
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
	    	if(rset7 != null){try{rset7.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt0 != null){try{stmt0.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt6 != null){try{stmt6.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt7 != null){try{stmt7.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}
	
//	public void forAllBillingOption()
//	{
//		String function_nm="forAllBillingOption()";
//		try
//		{
//			/*VBILLING_FREQ_FLAG.add("0"); 
//			VBILLING_FREQ_FLAG.add("1"); 
//			VBILLING_FREQ_FLAG.add("2"); 
//			VBILLING_FREQ_FLAG.add("3"); 
//			VBILLING_FREQ_FLAG.add("4"); 
//			VBILLING_FREQ_FLAG.add("5"); 
//			VBILLING_FREQ_FLAG.add("6"); 
//			VBILLING_FREQ_FLAG.add("9"); 
//			VBILLING_FREQ_FLAG.add("7"); 
//			VBILLING_FREQ_FLAG.add("8"); 
//
//			VBILLING_FREQ_FLAG.add("All");
//			VBILLING_FREQ_FLAG.add("1st-Fortnight");
//			VBILLING_FREQ_FLAG.add("2nd-Fortnight");
//			VBILLING_FREQ_FLAG.add("1st-Weekly");
//			VBILLING_FREQ_FLAG.add("2nd-Weekly");
//			VBILLING_FREQ_FLAG.add("3rd-Weekly");
//			VBILLING_FREQ_FLAG.add("4th-Weekly");
//			VBILLING_FREQ_FLAG.add("5th-Weekly");
//			VBILLING_FREQ_FLAG.add("Monthly");
//			VBILLING_FREQ_FLAG.add("Other");*/
//			
//			String temp_billing_cycle=billing_cycle;
//			inv_index=0;
//			//for(int i=1; i<=9; i++)
//			for(int i=1; i<=11; i++) //ADDING 10 FOR TCQ Completion
//			{
//				if(i!=10)
//				{
//					billing_cycle=""+i;
//					getBillingCyclePeriod();
//					if(billing_cycle.equals("11"))
//					{
//						billing_flag="T";
//						getTCQCompletionSalesInvoicePreparationList();
//					}
//					else
//					{
//						billing_flag="B";
//						getSalesInvoicePreparationList();
//					}
//				}
//			}
//			VINDEX.add(inv_index);
//			
//			inv_index=0;
//			//for(int i=1; i<=9; i++)
//			for(int i=1; i<=11; i++) //ADDING 10 FOR TCQ Completion
//			{
//				if(i!=10)
//				{
//					billing_cycle=""+i;
//					getBillingCyclePeriod();
//					if(billing_cycle.equals("11"))
//					{
//						//billing_flag="T";
//						//getTCQCompletionSalesInvoicePreparationList();
//					}
//					else
//					{
//						billing_flag="B";
//						getLTCORAInvoicePreparationList();
//					}
//				}
//			}
//			VINDEX.add(inv_index);
//			
//			billing_cycle="8";
//			getBillingCyclePeriod();
//			getFFlowInvoiceList();
//			
//			billing_cycle=temp_billing_cycle;
//		}
//		catch (Exception e) 
//		{
//			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
//		}
//	}
	
	public void getBillingFreq_nm(String freq)
	{
		String function_nm="getBillingFreq_nm()";
		try
		{
			if(freq.equals("1"))
			{
				billing_freq_nm="1st-Fortnight";
			}
			else if(freq.equals("2"))
			{
				billing_freq_nm="2nd-Fortnight";
			}
		}
		catch(Exception e)
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
						+ "NVL(B.STORAGE_DAYS-1,0),NVL(B.STORAGE_EXT_DAYS,0),STORAGE_TARIFF_UNIT,STORAGE_TARIFF "
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
					remark_2="Cargo "+ship_nm+" Dated "+boe_date+"";
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
						+ "EXCL_SAT_MAP,EXCHG_VAL,HOLIDAY_STATE "
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
						+ "EXCL_SAT_MAP,EXCHG_VAL,HOLIDAY_STATE "
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
			
			String bank_formula="";
			queryString2="SELECT COUNTERPARTY_CD,ENTITY,TO_CHAR(EFF_DT,'DD/MM/YYYY'),BANK_NAME,ACCOUNT_ID,IFSC_CODE,BRANCH_NAME,"
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
			stmt2.close();
			
			remark_1 ="Please pay the invoiced amount by wire transfer at our Bank Account : "+bank_formula;
			//COMMENTED AS REQUESTED BY VIJAY ON 31/102023 
			//remark_2 ="Subject to the terms and conditions of the "+rmk+" between "+utilBean.getCompanyName(comp_cd)+" and "+utilBean.getCounterpartyName(counterparty_cd, comp_cd);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public Double getTransactionAmount(String comp_cd,String counterparty_cd, String financial_year)
	{
		String function_nm="getTransactionAmount()";
		double transc_fee=0;
		try
		{
			//RLNG INVOICE
			queryString="SELECT NVL(SUM(INVOICE_AMT),0) "
					+ "FROM FMS_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE IN (?,?) "
					+ "AND FINANCIAL_YEAR=? AND APPROVED_FLAG=? AND INVOICE_DT <=TO_DATE(?,'DD/MM/YYYY')";
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
					+ "AND FINANCIAL_YEAR=? AND APPROVED_FLAG=? AND INVOICE_DT <=TO_DATE(?,'DD/MM/YYYY')";
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
				else
				{
							
					total_transaction_amt=""+getTransactionAmount(comp_cd, counterparty_cd, financial_year);
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
					+ "AND FINANCIAL_YEAR=? AND APPROVED_FLAG=? AND INVOICE_DT <=TO_DATE(?,'DD/MM/YYYY') "
					+ "UNION ALL "
					+ "SELECT AGMT_NO,CONT_NO,CONTRACT_TYPE,INVOICE_AMT,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY') "
					+ "FROM FMS_DLNG_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE IN (?,?) "
					+ "AND FINANCIAL_YEAR=? AND APPROVED_FLAG=? AND INVOICE_DT <=TO_DATE(?,'DD/MM/YYYY') ";
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
	
//	public void getExistingInvoiceDtl()
//	{
//		String function_nm="getExistingInvoiceDtl()";
//		try
//		{
//			queryString="SELECT BU_CONTACT_PERSON_CD,CONTACT_PERSON_CD,INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
//					+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),"
//					+ "EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),INVOICE_AMT,NET_PAYABLE_AMT,"
//					+ "REMARK_1,REMARK_2,TCS_TDS,TCS_AMT,TCS_FACTOR,TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,TCS_STRUCT_CD,TO_CHAR(TCS_EFF_DT,'DD/MM/YYYY'),"
//					+ "TDS_GROSS_AMT,TDS_GROSS_PERCENT,TDS_STRUCT_CD,TO_CHAR(TDS_EFF_DT,'DD/MM/YYYY'),MARKET_MARGIN,MARKET_MARGIN_AMT,OTHER_CHARGES,OTHER_CHARGES_AMT,"
//					+ "TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(APPROVED_DT,'DD/MM/YYYY HH24:MI:SS'),FINANCIAL_YEAR,SUG_QTY,SUG_PERCENT "
//					+ "FROM FMS_INVOICE_MST "
//					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
//					+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
//					+ "AND BU_STATE_TIN=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
//					+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND FINANCIAL_YEAR=? AND CARGO_NO=? AND INV_FLAG=? ";
//			stmt=conn.prepareStatement(queryString);
//			stmt.setString(1, comp_cd);
//			stmt.setString(2, counterparty_cd);
//			stmt.setString(3, cont_no);
//			stmt.setString(4, agmt_no);
//			stmt.setString(5, plant_seq);
//			stmt.setString(6, bu_unit);
//			stmt.setString(7, contract_type);
//			stmt.setString(8, bu_state_tin);
//			stmt.setString(9, period_start_dt);
//			stmt.setString(10, period_end_dt);
//			stmt.setString(11, exist_financial_year);
//			stmt.setString(12, cargo_no);
//			stmt.setString(13, inv_flag);
//			rset=stmt.executeQuery();
//			if(rset.next())
//			{
//				submission_chk=true;
//				
//				bu_contact_person_cd=rset.getString(1)==null?"0":rset.getString(1);
//				contact_person_cd=rset.getString(2)==null?"0":rset.getString(2);
//				invoice_seq=rset.getString(3)==null?"":rset.getString(3);
//				invoice_no=rset.getString(4)==null?"":rset.getString(4);
//				invoice_dt=rset.getString(5)==null?"":rset.getString(5);
//				invoice_due_dt=rset.getString(6)==null?"":rset.getString(6);
//				exchng_rate_cd=rset.getString(11)==null?"":rset.getString(11);
//				exchang_rate_dt=rset.getString(12)==null?"":rset.getString(12);
//				exchang_rate=rset.getString(13)==null?"":nf2.format(rset.getDouble(13));
//				
//				if(operation.equals("INSERT"))
//				{
//					qty_mmbtu=rset.getString(7)==null?"":nf.format(rset.getDouble(7));
//					price=rset.getString(8)==null?"":nf.format(rset.getDouble(8));
//					price_cd=rset.getString(9)==null?"":rset.getString(9);
//					if(!price.equals(""))
//					{
//						price=utilBean.RateNumberFormat(rset.getDouble(8), price_cd);
//					}
//					gross_amt=rset.getString(10)==null?"":nf.format(rset.getDouble(10));
//					
//					invoice_raised_in=rset.getString(14)==null?"":rset.getString(14);
//					gross_amt1=rset.getString(15)==null?"":nf.format(rset.getDouble(15));
//					tax_amt=rset.getString(16)==null?"":nf.format(rset.getDouble(16));
//					tax_struct_cd=rset.getString(17)==null?"":rset.getString(17);
//					tax_struct_dt=rset.getString(18)==null?"":rset.getString(18);
//					invoice_amt=rset.getString(19)==null?"":nf.format(rset.getDouble(19));
//					net_payable=rset.getString(20)==null?"":nf.format(rset.getDouble(20));
//					
//					applicable_abbr=rset.getString(23)==null?"":rset.getString(23);
//					applicable_amt=rset.getString(24)==null?"":rset.getString(24);
//					TCS_factor=rset.getString(25)==null?"":rset.getString(25);
//					
//					//tax_struct_dtl=utilBean.getEntityTaxStructureDtl(conn,comp_cd, counterparty_cd, "C", plant_seq, bu_unit, period_start_dt);
//					tax_struct_dtl=utilBean.getTaxDescr(conn, tax_struct_cd);
//					
//					/*queryString1="SELECT TAX_STRUCT_DTL "
//							+ "FROM FMS_ENTITY_TAX_STRUCT_DTL A "
//							+ "WHERE A.COMPANY_CD='"+comp_cd+"' AND A.TAX_STRUCT_CD='"+tax_struct_cd+"' "
//							+ "AND A.ENTITY='C' AND A.COUNTERPARTY_CD='"+counterparty_cd+"' AND A.PLANT_SEQ_NO='"+plant_seq+"' AND A.BU_UNIT='"+bu_unit+"' "
//							+ "AND A.TAX_STRUCT_DT=(SELECT MAX(D.TAX_STRUCT_DT) FROM FMS_ENTITY_TAX_STRUCT_DTL D WHERE A.COMPANY_CD=D.COMPANY_CD "
//							+ "AND A.ENTITY=D.ENTITY AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.PLANT_SEQ_NO=D.PLANT_SEQ_NO "
//							+ "AND D.TAX_STRUCT_DT <= TO_DATE('"+tax_struct_dt+"','DD/MM/YYYY') AND A.BU_UNIT=D.BU_UNIT) ";
//					rset1=stmt1.executeQuery(queryString1);
//					if(rset1.next())
//					{
//						tax_struct_dtl=rset1.getString(1)==null?"":rset1.getString(1);
//					}*/
//					
//					transportation_charges=rset.getString(26)==null?"":nf2.format(rset.getDouble(26));
//					transportation_amount=rset.getString(27)==null?"":nf.format(rset.getDouble(27));
//					
//					tcs_struct_cd=rset.getString(28)==null?"":rset.getString(28);
//					tcs_struct_dt=rset.getString(29)==null?"":rset.getString(29);
//					
//					tds_amt=rset.getString(30)==null?"":nf.format(rset.getDouble(30));
//					tds_factor=rset.getString(31)==null?"":nf.format(rset.getDouble(31));
//					tds_struct_cd=rset.getString(32)==null?"":rset.getString(32);
//					tds_struct_dt=rset.getString(33)==null?"":rset.getString(33);
//					
//					marketing_margin=rset.getString(34)==null?"":nf.format(rset.getDouble(34));
//					marketing_margin_amount=rset.getString(35)==null?"":nf.format(rset.getDouble(35));
//					other_charges=rset.getString(36)==null?"":nf.format(rset.getDouble(36));
//					other_charges_amount=rset.getString(37)==null?"":nf.format(rset.getDouble(37));
//					inv_entered_on = rset.getString(38)==null?"":rset.getString(38);
//					inv_approved_on = rset.getString(39)==null?"":rset.getString(39);
//					
//					String fiscal_yr=rset.getString(40)==null?"":rset.getString(40);
//					
//					gross_include_transport_tariff=nf.format(Double.parseDouble(gross_amt1));
//					if(!transportation_amount.equals(""))
//					{
//						gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(transportation_amount));
//						isGrossIncTriff=true;
//					}
//					if(!marketing_margin_amount.equals(""))
//					{
//						gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(marketing_margin_amount));
//						isGrossIncTriff=true;
//					}
//					if(!other_charges_amount.equals(""))
//					{
//						gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(other_charges_amount));
//						isGrossIncTriff=true;
//					}
//					
//					
//					
//					Vector VTAX_CODE = new Vector();
//					Vector VTAX_DESCR = new Vector();
//					Vector VTAX_AMT = new Vector();
//					Vector VTAX_BASE_AMT = new Vector();
//					Vector VTAX_FACTOR = new Vector();
//					queryString1="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
//							+ "FROM FMS_INV_TAX_DTL "
//							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
//							+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? ";
//					stmt1=conn.prepareStatement(queryString1);
//					stmt1.setString(1, comp_cd);
//					stmt1.setString(2, bu_state_tin);
//					//stmt1.setString(3, financial_year);
//					stmt1.setString(3, fiscal_yr);
//					stmt1.setString(4, invoice_seq);
//					rset1=stmt1.executeQuery();
//					while(rset1.next())
//					{
//						VTAX_CODE.add(rset1.getString(1)==null?"":rset1.getString(1));
//						VTAX_DESCR.add(rset1.getString(2)==null?"":rset1.getString(2));
//						VTAX_AMT.add(rset1.getString(3)==null?"":nf.format(rset1.getDouble(3)));
//						VTAX_BASE_AMT.add(rset1.getString(4)==null?"":nf.format(rset1.getDouble(4)));
//						VTAX_FACTOR.add("");
//					}
//					rset1.close();
//					stmt1.close();
//					
//					Vector VTEMP_TAX_DTL = new Vector();
//					
//					VTEMP_TAX_DTL.add(VTAX_CODE);
//					VTEMP_TAX_DTL.add(VTAX_DESCR);
//					VTEMP_TAX_DTL.add(VTAX_AMT);
//					VTEMP_TAX_DTL.add(VTAX_BASE_AMT);
//					VTEMP_TAX_DTL.add(VTAX_FACTOR);
//					
//					VMULTI_TAX_STRUCT.add(VTEMP_TAX_DTL);
//					
//					queryString1="SELECT TO_CHAR(STORAGE_DT,'DD/MM/YYYY'),OPEN_BALANCE_QTY,OFFTAKE_QTY,RATE,RATE_TYPE,DAY_DISCOUNT "
//							+ "FROM FMS_INV_STORAGE_CRG_DTL "
//							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
//							+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=?";
//					stmt1=conn.prepareStatement(queryString1);
//					stmt1.setString(1, comp_cd);
//					stmt1.setString(2, bu_state_tin);
//					//stmt1.setString(3, financial_year);
//					stmt1.setString(3, fiscal_yr);
//					stmt1.setString(4, invoice_seq);
//					rset1=stmt1.executeQuery();
//					while(rset1.next())
//					{
//						String storageInv=rset1.getString(2)==null?"":nf.format(rset1.getDouble(2));
//						VSTORAGE_DATE.add(rset1.getString(1)==null?"":rset1.getString(1));
//						VSTORAGE_INVENTORY.add(storageInv);
//						VOFFTAKE_QTY.add(rset1.getString(3)==null?"":nf.format(rset1.getDouble(3)));
//						
//						String rate=rset1.getString(4)==null?"":utilBean.RateNumberFormat(rset1.getDouble(4),price_cd);
//						String rate_type=rset1.getString(5)==null?"":rset1.getString(5);
//						String discount_day=rset1.getString(6)==null?"":rset1.getString(6);
//						VRATE_TYPE.add(rate_type);
//						VUSER_DEFINE.add(rate_type.equals("U")?rate:"");
//						VSTORAGE_CHARGE.add(rate_type.equals("U")?"":rate);
//						
//						String storage_amt="";
//						if(!storageInv.equals("") && !rate.equals("") && discount_day.equals("N"))
//						{
//							storage_amt=nf.format(Double.parseDouble(storageInv) * Double.parseDouble(rate));
//						}
//						
//						VSTORAGE_AMT.add(storage_amt);
//						VDISCOUNT_FLAG.add(discount_day);
//					}
//					rset1.close();
//					stmt1.close();
//				}
//				
//				if(inv_flag.equals("UG"))
//				{
//					qty_mmbtu=rset.getString(7)==null?"":nf.format(rset.getDouble(7));
//					price=rset.getString(8)==null?"":nf.format(rset.getDouble(8));
//					price_cd=rset.getString(9)==null?"":rset.getString(9);
//					if(!price.equals(""))
//					{
//						price=utilBean.RateNumberFormat(rset.getDouble(8), price_cd);
//					}
//					
//					sug_qty=rset.getString(41)==null?"":nf.format(rset.getDouble(41));
//					sug_percentage=rset.getString(42)==null?"":nf.format(rset.getDouble(42));
//					
//				}
//				
//				remark_1=rset.getString(21)==null?"":rset.getString(21);
//				//remark_2=rset.getString(22)==null?"":rset.getString(22);
//				
//			}
//			rset.close();
//			stmt.close();
//		}
//		catch(Exception e)
//		{
//			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
//		}
//	}
	
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
			queryString="SELECT BU_CONTACT_PERSON_CD,CONTACT_PERSON_CD,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD-MON-YY'),"
					+ "TO_CHAR(DUE_DT,'DD-MON-YY'),TO_CHAR(PERIOD_START_DT,'DD-MON-YY'),TO_CHAR(PERIOD_END_DT,'DD-MON-YY'),REMARK_1,REMARK_2,"
					+ "ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_VALUE,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),"
					+ "INVOICE_AMT,NET_PAYABLE_AMT,TCS_TDS,TCS_AMT,TCS_FACTOR,CHECKED_FLAG,APPROVED_FLAG, "
					+ "INVOICE_ID_SEQ,TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),EXCHG_RATE_TYPE,"
					+ "INVOICE_RAISED_IN,MARKET_MARGIN,MARKET_MARGIN_AMT,OTHER_CHARGES,OTHER_CHARGES_AMT,SUG_QTY,SUG_PERCENT "
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
				
				//tax_struct_dtl=utilBean.getEntityTaxStructureDtl(conn,comp_cd, counterparty_cd, "C", plant_seq, bu_unit, period_start_dt);
				tax_struct_dtl=utilBean.getTaxDescr(conn, tax_struct_cd);
				
				transportation_charges=rset.getString(27)==null?"":nf2.format(rset.getDouble(27));
				transportation_amount=rset.getString(28)==null?"":nf.format(rset.getDouble(28));
				
				exchng_rate_cd=rset.getString(29)==null?"":rset.getString(29);
				exchang_rate_dt=rset.getString(30)==null?"":rset.getString(30);
				exchang_rate_type=rset.getString(31)==null?"":rset.getString(31);
				invoice_raised_in=rset.getString(32)==null?"":rset.getString(32);
				
				marketing_margin=rset.getString(33)==null?"":nf.format(rset.getDouble(33));
				marketing_margin_amount=rset.getString(34)==null?"":nf.format(rset.getDouble(34));
				other_charges=rset.getString(35)==null?"":nf.format(rset.getDouble(35));
				other_charges_amount=rset.getString(36)==null?"":nf.format(rset.getDouble(36));
				
				sug_qty=rset.getString(37)==null?"":nf.format(rset.getDouble(37));
				sug_percentage=rset.getString(38)==null?"":nf.format(rset.getDouble(38));
				
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
						VPDF_COL4.add(""+utilBean.getRateUnitNm(conn,price_cd)+"/MMBTU");
						VPDF_COL5.add("");
						VPDF_COL6.add(price);
						VPDF_COL7.add("");
						
						if(price_cd.equals("2"))
						{
							srno+=1;
							VPDF_COL1.add(srno);
							VPDF_COL2.add("Gross Amount(USD)");
							VPDF_COL3.add("");
							VPDF_COL4.add(""+utilBean.getRateUnitNm(conn,price_cd));
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
					VPDF_COL4.add(""+utilBean.getRateUnitNm(conn,price_cd));
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
	
	
	public void getDrcrInvoiceNumber()
	{
		String function_nm="getDrcrInvoiceNumber()";
		try
		{
			String invoice_prefix=utilBean.getInvoicePrefix(conn,comp_cd);
			
			String fin_yr="";
			if(!drcr_fin_yr.equals(""))
			{
				String[] temp = drcr_fin_yr.split("-");
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
				VINVOICE_NO.add(drcr_invoice_no);
			}
			
			String contType="";
			String invSeries="";
			String ff_invType="";
			String drcr = "";
			if(contract_type.equals("Q") || contract_type.equals("O"))
			{
				contType="'Q','O'";
				invSeries="L";
				
				ff_invType="'SI','UG','ST','DI'";				
			}
			else
			{
				contType="'S','L','X'";
				invSeries="S";
				
				ff_invType="'S'";
			}
			
			if(drcr_flag.equals("DR")) {
				drcr = "DR";
			}
			else {
				drcr = "CR";
			}
			
			if(!invoice_prefix.equals("") && activityFlag.equals("APPROVE"))
			{
				int no_inv_no=10;
				for(int i=1;i<=no_inv_no;i++)
				{
					String invoice_id_seq=""+i;
					int count=0;
						
						queryString="SELECT COUNT(*) "
								+ "FROM FMS_SALES_DR_CR_MST "
								+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
								+ "AND BU_STATE_TIN=? AND INVOICE_ID_SEQ=? AND CONTRACT_TYPE IN ("+contType+") ";
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
						
						
						if(invSeries.equals("S") || invSeries.equals("L"))
						{
							queryString1="SELECT COUNT(*) "
									+ "FROM FMS_FFLOW_INV_MST "
									+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
									+ "AND FINANCIAL_YEAR=? "
									//+ "AND INVOICE_TYPE=? "
									+ "AND INVOICE_TYPE IN ("+ff_invType+") "
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
							String drcr_no="";
							drcr_no=invoice_prefix+""+drcr+""+state_abbr+""+utilBean.PrePaddingZero(invoice_id_seq, 4)+"/"+fin_yr;
							
							VINVOICE_ID_SEQ.add(invoice_id_seq);
							VINVOICE_NO.add(drcr_no);
							
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
	 	        		+ "AND INVOICE_TYPE=? AND PDF_TYPE!=?";
				stmt3=conn.prepareStatement(queryString3);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, bu_state_tin);
				stmt3.setString(3, invoice_seq);
				stmt3.setString(4, financial_year);
				stmt3.setString(5, invoice_type);
				stmt3.setString(6, "X");
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
	 	        		+ "AND PDF_TYPE!=?";
				stmt3=conn.prepareStatement(queryString3);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, bu_state_tin);
				stmt3.setString(3, invoice_seq);
				stmt3.setString(4, financial_year);
				stmt3.setString(5, "X");
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
			utilBean.getEffectiveEntityCounterpartyList(conn,comp_cd,"C");
			VCOUNTERPARTY_CD= utilBean.getCOUNTERPARTY_CD();
			VCOUNTERPARTY_NM = utilBean.getCOUNTERPARTY_NM();
			VCOUNTERPARTY_ABBR = utilBean.getCOUNTERPARTY_ABBR();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
//	public void getFilterContractList()
//	{
//		String function_nm="getFilterContractList()";
//		try
//		{
//			VFILTER_CONT_TYPE.add("S");
//			VFILTER_CONT_NAME.add(utilBean.getContractTypeName("S"));
//			
//			VFILTER_CONT_TYPE.add("L");
//			VFILTER_CONT_NAME.add(utilBean.getContractTypeName("L"));
//			
//			VFILTER_CONT_TYPE.add("X");
//			VFILTER_CONT_NAME.add(utilBean.getContractTypeName("X"));
//			
//			VFILTER_CONT_TYPE.add("O");
//			VFILTER_CONT_NAME.add(utilBean.getContractTypeName("O"));
//			
//			VFILTER_CONT_TYPE.add("Q");
//			VFILTER_CONT_NAME.add(utilBean.getContractTypeName("Q"));
//		}
//		catch(Exception e)
//		{
//			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
//		}
//	}
	
	
	public void getSelectedDrCrCriteria(String criteria)
	{
		String function_nm="getSelectedDrCrCriteria()";
		try
		{
			criteria_desc = "CHANGE IN ";
			
			if(criteria.contains("@")) {
				String item_desc[] = criteria.split("@");
				
				for(int i = 0; i< item_desc.length; i++) 
				{
					if(item_desc[i].equals("1")) 
					{
						criteria_desc += "EXCHNG RATE/ ";
					}
					else if(item_desc[i].equals("2")) 
					{
						criteria_desc += "PRICE/ ";
					}
					else if(item_desc[i].equals("3")) 
					{
						criteria_desc += "QTY/ ";
					}
					else if(item_desc[i].equals("4")) 
					{
						criteria_desc += "TRANSPORTATION TARIFF/ ";
					}
					else if(item_desc[i].equals("5")) 
					{
						criteria_desc += "MARKET MARGIN/ ";
					}
					else if(item_desc[i].equals("6")) 
					{
						criteria_desc += "QTHER CHARGES/ ";
					}
					else if(item_desc[i].equals("7")) 
					{
						criteria_desc += "TAX %/ ";
					}
					else if(item_desc[i].contains("8")) 
					{
						criteria_desc += "TAX STRUCTURE/ ";
					}
					else if(item_desc[i].contains("9")) 
					{
						criteria_desc += "BU/ ";
					}				
				}
				if (criteria_desc.length()>2) {
					criteria_desc = criteria_desc.substring(0,criteria_desc.length() - 2);
				}
			}
			else {
				if(criteria.equals("1")) 
				{
					criteria_desc += "EXCHNG RATE";
				}
				else if(criteria.equals("2")) 
				{
					criteria_desc += "PRICE";
				}
				else if(criteria.equals("3")) 
				{
					criteria_desc += "QTY";
				}
				else if(criteria.equals("4")) 
				{
					criteria_desc += "TRANSPORTATION TARIFF";
				}
				else if(criteria.equals("5")) 
				{
					criteria_desc += "MARKET MARGIN";
				}
				else if(criteria.equals("6")) 
				{
					criteria_desc += "QTHER CHARGES";
				}
				else if(criteria.equals("7")) 
				{
					criteria_desc += "TAX %";
				}
				else if(criteria.equals("8")) 
				{
					criteria_desc += "TAX STRUCTURE";
				}
				else if(criteria.equals("9")) 
				{
					criteria_desc += "BU";
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
			
			
				queryString1="SELECT BU_CONTACT_PERSON_CD,CONTACT_PERSON_CD,DR_CR_NO,TO_CHAR(DR_CR_DUE_DT,'DD/MM/YYYY'),TO_CHAR(DR_CR_DT,'DD/MM/YYYY') "
						+ "FROM FMS_SALES_DR_CR_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND BU_STATE_TIN=? AND DR_CR_FIN_YR=? AND DR_CR_SEQ = ? AND CRITERIA = ? AND DR_CR_FLAG = ?";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, bu_state_tin);
				stmt1.setString(4, drcr_fin_yr);
				stmt1.setString(5, drcr_seq);
				stmt1.setString(6, criteria);
				stmt1.setString(7, drcr_flag);
				
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					bu_contact_person_cd=rset1.getString(1)==null?"0":rset1.getString(1);
					contact_person_cd=rset1.getString(2)==null?"0":rset1.getString(2);
					invoiceNo=rset1.getString(3)==null?"":rset1.getString(3);
					dueDate=rset1.getString(4)==null?"":rset1.getString(4);
					invoiceDt=rset1.getString(5)==null?"":rset1.getString(5);
				}
				rset1.close();
				stmt1.close();
			
			
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
				
				
				if(drcr_flag.equals("DR"))
				{
					//type="DEBIT NOTE";
					type="DEBIT NOTE";
				}
				else if(drcr_flag.equals("CR"))
				{
					//type="CREDIT NOTE";
					type="CREDIT NOTE";
				}
//				else if(invoice_type.equals("CDR"))
//				{
//					//type="DEBIT NOTE";
//					type=other_inv_str;
//				}
//				else if(invoice_type.equals("CCR"))
//				{
//					//type="CREDIT NOTE";
//					type=other_inv_str;
//				}
//				else if(invoice_type.equals("LP"))
//				{
//					//type="LATE PAYMENT INVOICE";
//					type=other_inv_str;
//				}
//				else if(invoice_type.equals("OR"))
//				{
//					//type="OTHER";
//					type=other_inv_str;
//				}
//				else if(invoice_type.equals("S"))
//				{
//					//type="OTHER";
//					type=other_inv_str;
//				}
//				else
//				{
//					type="SALES";
//				}
				
				subject=companyAbbr+"/"+customerNm+"/"+type+"/"+temp_pdf_type_nm+"/"+dueDate.replaceAll("/", "-")+"/"+invoiceNo;
				
				String to_list="";
				String cc_list="";
				
				if(temp_pdf_type.equals("T"))
				{
					to_list=utilBean.getEntityContactPersonEmailList(conn, comp_cd, comp_cd, "B", "P"+bu_unit, "INV", "RLNG","Y");
				}
				else
				{
						to_list=utilBean.getEntityContactPersonEmailList(conn, comp_cd, counterparty_cd, "C", "P"+plant_seq, "INV", "RLNG","Y");
						cc_list=utilBean.getEntityContactPersonEmailList(conn, comp_cd, counterparty_cd, "C", "P"+plant_seq, "INV", "RLNG","N");				
				}
				
				String tmpCcList=utilBean.getEntityContactPersonEmailList(conn, comp_cd, comp_cd, "B", "P"+bu_unit, "INV", "RLNG","N");
				cc_list+=cc_list.equals("")?tmpCcList:","+tmpCcList;
				
				//get BCc list
				String bcc_list=utilBean.getEntityContactPersonEmailList(conn, comp_cd, comp_cd, "B", "P"+bu_unit, "INV", "RLNG","B");
	
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
			
					queryString3="SELECT FILE_NAME "
							+ "FROM FMS_INV_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
		 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
		 	        		+ "AND PDF_TYPE=? AND PDF_SIGNED=?";

				stmt3=conn.prepareStatement(queryString3);
				stmt3.setString(++st_count, comp_cd);
				stmt3.setString(++st_count, bu_state_tin);
				stmt3.setString(++st_count, drcr_seq);
				stmt3.setString(++st_count, drcr_fin_yr);
				stmt3.setString(++st_count, temp_pdf_type);
				stmt3.setString(++st_count, "Y");
				rset3=stmt3.executeQuery();
				if(rset3.next())
				{
					//attachment=rset.getString(1)==null?"":rset.getString(1);
					VTEMP_MAIL_ATTACHMENT.add(rset3.getString(1)==null?"":rset3.getString(1));
					VTEMP_MAIL_ANNEXURE_ATTACHMENT_FLAG.add("");
				}
				rset3.close();
				stmt3.close();
			
				
				String companyNm=utilBean.getCompanyName(conn,comp_cd);
				String mail_body="Dear Sir/Madam,"
						+ "\n\nPlease find enclosed "+type+"# "+invoiceNo+" dated "+invoiceDt.replaceAll("/", "-")+".";
				if(!invoice_type.equals("CR") && !invoice_type.equals("CCR"))
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
				
				VMAIL_ATTACHMENT_PATH.add(CommonVariable.signed_drcr_inv_path);
		
	
				String annexure_folder=invoice_type+"_"+financial_year+"_"+bu_state_tin+"_"+invoice_seq;
				VMAIL_ANNEXURE_ATTACHMENT_PATH.add(CommonVariable.freeflow_annexure_path+""+annexure_folder+"/");
				VMAIL_BODY.add(mail_body);	
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	
	//Deep20250818	
	public void getDrCrNoteList()
	{
		String function_nm="getDrCrNoteList()";
		try
		{
			
			if(billing_cycle.equals("0")){
				period_start_dt=""+utilDate.getFirstDateOfMonth(month, year);
				period_end_dt=""+utilDate.getLastDateOfMonth(month, year);
			}
			
			// SagarB20250906
			Map<String,Integer> index_count=new HashMap<String,Integer>();
			String contract_type1="";
			if(segment.equals("")) {
				 index_count.put("RLNG", 0);
				 index_count.put("LTCORA", 0);
			}
			else {
				index_count.put(segment, 0);
			}
			
			
			String cont_ref_no="",deal_no="";
		
			queryString5="SELECT INVOICE_NO,INVOICE_SEQ,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'),COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CARGO_NO "
					+ "FROM FMS_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND PERIOD_START_DT>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND PERIOD_END_DT<=TO_DATE(?,'DD/MM/YYYY') AND APPROVED_FLAG=? AND INV_FLAG = 'F' ";
			if(!billing_cycle.equals("0")){
			queryString5 += "AND FREQ=? ";
			}
			stmt5=conn.prepareStatement(queryString5);
			stmt5.setString(1, comp_cd);
			stmt5.setString(2, counterparty_cd);
			stmt5.setString(3, period_start_dt);
			stmt5.setString(4, period_end_dt);
			stmt5.setString(5, "Y");
			if(!billing_cycle.equals("0")){
				stmt5.setString(6, billing_cycle);
			}
			rset5=stmt5.executeQuery();
			
			while(rset5.next())
			{
				String own_cd=rset5.getString(7)==null?"":rset5.getString(7);
				String countpty_cd=rset5.getString(8)==null?"":rset5.getString(8);
				String agmt_no = rset5.getString(9)==null?"":rset5.getString(9);
				String agmt_rev_no = rset5.getString(10)==null?"":rset5.getString(10);
				String cont_no = rset5.getString(11)==null?"":rset5.getString(11);
				String cont_rev_no = rset5.getString(12)==null?"":rset5.getString(12);
				String contract_type = rset5.getString(13)==null?"":rset5.getString(13);
				String cargo_no = rset5.getString(14)==null?"":rset5.getString(14);
			
				if(contract_type.equals("S") || contract_type.equals("L") || contract_type.equals("X")) 
				{
					
					// SagarB20250906
					if (segment.equals("")) {
						index_count.put("RLNG", index_count.get("RLNG")+1);
					}
					else if (segment.equals("RLNG")) {
						index_count.put(segment, index_count.get(segment)+1);
					}
					
					deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
					
					queryString2 = "SELECT CONT_REF_NO FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? "
							+ "AND CONT_REV = ? AND CONTRACT_TYPE = ?";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, own_cd);
					stmt2.setString(2, countpty_cd);
					stmt2.setString(3, agmt_no);
					stmt2.setString(4, agmt_rev_no);
					stmt2.setString(5, cont_no);
					stmt2.setString(6, cont_rev_no);
					stmt2.setString(7, contract_type);
					
					rset2 = stmt2.executeQuery();
					if(rset2.next()) {
						cont_ref_no = rset2.getString(1)==null?"":rset2.getString(1);
					}
					rset2.close();
					stmt2.close();
				}
				else 
				{

					// SagarB20250906
					if (segment.equals("")) {
						index_count.put("LTCORA", index_count.get("LTCORA")+1);
					}
					else if (segment.equals("LTCORA")) {
						index_count.put(segment, index_count.get(segment)+1);
					}
					
					deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, cargo_no);
					
					queryString2 = "SELECT CARGO_REF FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? "
							+ "AND CONT_REV = ? AND CONTRACT_TYPE = ? AND CARGO_NO = ?";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, own_cd);
					stmt2.setString(2, countpty_cd);
					stmt2.setString(3, agmt_no);
					stmt2.setString(4, agmt_rev_no);
					stmt2.setString(5, cont_no);
					stmt2.setString(6, cont_rev_no);
					stmt2.setString(7, contract_type);
					stmt2.setString(8, cargo_no);
								
					rset2 = stmt2.executeQuery();
					if(rset2.next()) {
						cont_ref_no = rset2.getString(1)==null?"":rset2.getString(1);
					}
					rset2.close();
					stmt2.close();	
				}

				VDRCR_INVOICE_NO.add(rset5.getString(1)==null?"":rset5.getString(1));
				VDRCR_INVOICE_SEQ.add(rset5.getString(2)==null?"":rset5.getString(2));
				VDRCR_START_DT.add(rset5.getString(3)==null?"":rset5.getString(3));
				VDRCR_END_DT.add(rset5.getString(4)==null?"":rset5.getString(4));		
				VDRCR_INVOICE_DT.add(rset5.getString(5)==null?"":rset5.getString(5));		
				VDRCR_DUE_DT.add(rset5.getString(6)==null?"":rset5.getString(6));
//				VINVOICE_CURR.add("2");	
				VDRCR_REMARK.add("");
				VDEAL_NO.add(deal_no);
				VDEAL_CONT_REF_NO.add(cont_ref_no);
				
			}
			rset5.close();
			stmt5.close();

			// Sagar/b20250906
			if (segment.equals("")) {
				VINDEX.add(index_count.get("RLNG"));
				VINDEX.add(index_count.get("LTCORA"));
			}
			else {
				VINDEX.add(index_count.get(segment));
			}
			
//			//DRCR_LIST
//			queryString5="SELECT INVOICE_NO,INVOICE_SEQ,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),"
//					+ "TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(INV_DUE_DT,'DD/MM/YYYY'),AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE "
//					+ "FROM FMS_SALES_DR_CR_MST "
//					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
//					+ "AND PERIOD_START_DT>=TO_DATE(?,'DD/MM/YYYY') "
//					+ "AND PERIOD_END_DT<=TO_DATE(?,'DD/MM/YYYY') AND APPROVED_FLAG=? ";
//			if(!billing_cycle.equals("0")){
//				queryString5 += "AND FREQ=? ";
//			}
//			stmt5=conn.prepareStatement(queryString5);
//			stmt5.setString(1, comp_cd);
//			stmt5.setString(2, counterparty_cd);
//			stmt5.setString(3, period_start_dt);
//			stmt5.setString(4, period_end_dt);
//			stmt5.setString(5, "Y");
//			if(!billing_cycle.equals("0")){
//				stmt5.setString(6, billing_cycle);
//			}
//			
//			rset5=stmt5.executeQuery();
//			while(rset5.next())
//			{
//				String own_cd=rset5.getString(7)==null?"":rset5.getString(7);
//				String countpty_cd=rset5.getString(8)==null?"":rset5.getString(8);
//				String agmt_no = rset5.getString(9)==null?"":rset5.getString(9);
//				String agmt_rev_no = rset5.getString(10)==null?"":rset5.getString(10);
//				String cont_no = rset5.getString(11)==null?"":rset5.getString(11);
//				String cont_rev_no = rset5.getString(12)==null?"":rset5.getString(12);
//				String contract_type = rset5.getString(13)==null?"":rset5.getString(13);
//				String cargo_no = rset5.getString(14)==null?"":rset5.getString(14);
//				
//				if(contract_type.equals("S") || contract_type.equals("L") || contract_type.equals("X")) 
//				{
//					deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
//					
//					queryString2 = "SELECT CONT_REF_NO FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? "
//							+ "AND CONT_REV = ? AND CONTRACT_TYPE = ?";
//					stmt2 = conn.prepareStatement(queryString2);
//					stmt2.setString(1, own_cd);
//					stmt2.setString(2, countpty_cd);
//					stmt2.setString(3, agmt_no);
//					stmt2.setString(4, agmt_rev_no);
//					stmt2.setString(5, cont_no);
//					stmt2.setString(6, cont_rev_no);
//					stmt2.setString(7, contract_type);
//					
//					rset2 = stmt2.executeQuery();
//					if(rset2.next()) {
//						cont_ref_no = rset2.getString(1)==null?"":rset2.getString(1);
//					}
//					rset2.close();
//					stmt2.close();
//				}
//				else 
//				{					
//					deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, cargo_no);
//					
//					queryString2 = "SELECT CARGO_REF FROM FMS_LTCORA_CONT_CARGO_DTL WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? "
//							+ "AND CONT_REV = ? AND CONTRACT_TYPE = ? AND CARGO_NO=?";
//					stmt2 = conn.prepareStatement(queryString2);
//					stmt2.setString(1, own_cd);
//					stmt2.setString(2, countpty_cd);
//					stmt2.setString(3, agmt_no);
//					stmt2.setString(4, agmt_rev_no);
//					stmt2.setString(5, cont_no);
//					stmt2.setString(6, cont_rev_no);
//					stmt2.setString(7, contract_type);
//					stmt2.setString(8, cargo_no);
//								
//					rset2 = stmt2.executeQuery();
//					if(rset2.next()) {
//						cont_ref_no = rset2.getString(1)==null?"":rset2.getString(1);
//					}
//					rset2.close();
//					stmt2.close();					
//				}
//				
//				DRCR_INVOICE_NO.add(rset5.getString(1)==null?"":rset5.getString(1));
////				DRCR_INVOICE_SEQ.add(rset5.getString(2)==null?"":rset5.getString(2));
//				DRCR_PERIOD_START_DT.add(rset5.getString(3)==null?"":rset5.getString(3));
//				DRCR_PERIOD_END_DT.add(rset5.getString(4)==null?"":rset5.getString(4));		
//				DRCR_DT.add(rset5.getString(5)==null?"":rset5.getString(5));		
//				DRCR_DUE_DT.add(rset5.getString(5)==null?"":rset5.getString(6));
//				VINVOICE_CURR.add("2");	
//				VDRCR_REMARK.add("");
//				VDEAL_NO.add(deal_no);
//				VDEAL_CONT_REF_NO.add(cont_ref_no);
//			}
//			rset5.close();
//			stmt5.close();

		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
		
	
	//Deep 23082025
		public void setHeaderDisplaySegment()
		{
			String function_nm="setHeaderDisplaySegment()";
			try
			{
				if(segment.equals("RLNG") || segment.equals("LTCORA") || segment.equals("DLNG"))
				{
					VDRCR_LIST_ABBR.add(segment);
					VDRCR_LIST_NM.add(""+getHeaderSegmentNm(segment));
				}
				else
				{
					VDRCR_LIST_ABBR.add("RLNG");
					VDRCR_LIST_NM.add(""+getHeaderSegmentNm("RLNG"));
					
					VDRCR_LIST_ABBR.add("LTCORA");
					VDRCR_LIST_NM.add(""+getHeaderSegmentNm("LTCORA"));

				}
			}
			catch(Exception e)
			{
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			}
		}
		public String getHeaderSegmentNm(String seg_ty)
		{
			String function_nm="getSegmentNm()";
			String nm="";
			try
			{
				if(seg_ty.equals("RLNG"))
				{
					nm="RLNG";
				}
				else if(seg_ty.equals("LTCORA"))
				{
					nm="LTCORA";
				}
			}
			catch(Exception e)
			{
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			}
			return nm;
		}
		
//		public void setDisplaySegment()
//		{
//			String function_nm="setDisplaySegment()";
//			try
//			{
//				int segment_index=0;
//				for(int i=0; i<VHEADER_DISPLAY_SEGMENT_TYP.size(); i++)
//				{
//					if(VHEADER_DISPLAY_SEGMENT_TYP.elementAt(i).equals("D"))
//					{
//						VDRCR_LIST_ABBR.add("D");
//						VDRCR_LIST_NM.add(""+getSegmentNm("D")+" Purchase Register");
//						
//						VDRCR_LIST_ABBR.add("T");
//						VDRCR_LIST_NM.add(""+getSegmentNm("T")+" Purchase Register");
//						segment_index=2;
//					}
//					else if(VHEADER_DISPLAY_SEGMENT_TYP.elementAt(i).equals("I"))
//					{
//						VDRCR_LIST_ABBR.add("I");
//						VDRCR_LIST_NM.add(""+getSegmentNm("I")+" Purchase Register");
//						segment_index=1;
//					}
//					else if(VHEADER_DISPLAY_SEGMENT_TYP.elementAt(i).equals("L"))
//					{
//						VDRCR_LIST_ABBR.add("L");
//						VDRCR_LIST_NM.add(""+getSegmentNm("L")+" Purchase Register");
//						segment_index=1;
//					}
//					else if(VHEADER_DISPLAY_SEGMENT_TYP.elementAt(i).equals("N"))
//					{
//						VDRCR_LIST_ABBR.add("N");
//						VDRCR_LIST_NM.add(""+getSegmentNm("N")+" Purchase Register");
//						
//						VDRCR_LIST_ABBR.add("CD");
//						VDRCR_LIST_NM.add(""+getSegmentNm("CD")+" Purchase Register");
//						
//						VDRCR_LIST_ABBR.add("Y");
//						VDRCR_LIST_NM.add(""+getSegmentNm("Y")+" Purchase Register");
//						
//						VDRCR_LIST_ABBR.add("A");
//						VDRCR_LIST_NM.add(""+getSegmentNm("A")+" Purchase Register");
//						
//						VDRCR_LIST_ABBR.add("H");
//						VDRCR_LIST_NM.add(""+getSegmentNm("H")+" Purchase Register");
//						segment_index=5;
//					}
//					VSEGMENT_INDEX.add(segment_index);
//				}
//			}
//			catch(Exception e)
//			{
//				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
//			}
//		}
		public String getSegmentNm(String seg_ty)
		{
			String function_nm="getSegmentNm()";
			String nm="";
			try
			{
				if(seg_ty.equals("RLNG"))
				{
					nm="RLNG";
				}
				else if(seg_ty.equals("LTCORA"))
				{
					nm="LTCORA";
				}
				else if(seg_ty.equals("DLNG"))
				{
					nm="DLNG";
				}
			}
			catch(Exception e)
			{
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			}
			return nm;
		}
	
	
	
	public void getDrCrNote() 
	{
		
		String function_nm="getDrCrNote()";
		try
		{
		
			
			String cont_ref_no="",deal_no="",start_dt="",end_dt="";
		
			for (int i = 0; i < VINVOICE_LIST_NAME.size(); i++) {
				queryString5 = "SELECT INVOICE_NO,INVOICE_SEQ,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),"
						+ "TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'),COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
						+ "CONTRACT_TYPE,CARGO_NO,FINANCIAL_YEAR,BU_UNIT,BU_STATE_TIN,PLANT_SEQ,INV_FLAG,FREQ "
						+ "FROM FMS_INVOICE_MST " 
						+ "WHERE COMPANY_CD=? "
						+ "AND INVOICE_DT>=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND INVOICE_DT<=TO_DATE(?,'DD/MM/YYYY') AND APPROVED_FLAG=? ";
				if(VINVOICE_LIST_NAME.elementAt(i).equals("RLNG Debit/Credit Note Generation")) {
					queryString5 += "AND CONTRACT_TYPE IN ('S','L','X')"; 
				}
				else {
					queryString5 += "AND CONTRACT_TYPE IN ('Q','O')";
				}
				stmt5 = conn.prepareStatement(queryString5);
				stmt5.setString(1, comp_cd);
				stmt5.setString(2, from_dt);
				stmt5.setString(3, to_dt);
				stmt5.setString(4, "Y");
				rset5 = stmt5.executeQuery();
				while (rset5.next()) {
					
					String own_cd = rset5.getString(7) == null ? "" : rset5.getString(7);
					String countpty_cd = rset5.getString(8) == null ? "" : rset5.getString(8);
					String agmt_no = rset5.getString(9) == null ? "" : rset5.getString(9);
					String agmt_rev_no = rset5.getString(10) == null ? "" : rset5.getString(10);
					String cont_no = rset5.getString(11) == null ? "" : rset5.getString(11);
					String cont_rev_no = rset5.getString(12) == null ? "" : rset5.getString(12);
					String contract_type = rset5.getString(13) == null ? "" : rset5.getString(13);
					String cargo_no = rset5.getString(14) == null ? "" : rset5.getString(14);
					String fin_yr = rset5.getString(15) == null ? "" : rset5.getString(15);
					String bu_plant_seq = rset5.getString(16) == null ? "" : rset5.getString(16);
					String bu_state_tin = rset5.getString(17) == null ? "" : rset5.getString(17);
					String plant_seq = rset5.getString(18) == null ? "" : rset5.getString(18);
					String inv_flag = rset5.getString(19) == null ? "" : rset5.getString(19);
					String freq = rset5.getString(20) == null ? "" : rset5.getString(20);
					

					if (contract_type.equals("S") || contract_type.equals("L") || contract_type.equals("X")) 
					{
						deal_no = utilBean.NewDealMappingId(own_cd, countpty_cd, agmt_no, agmt_rev_no, cont_no,
								cont_rev_no, contract_type, "");

						queryString2 = "SELECT CONT_REF_NO,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY') FROM FMS_SUPPLY_CONT_MST WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? "
								+ "AND CONT_REV = ? AND CONTRACT_TYPE = ?";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, own_cd);
						stmt2.setString(2, countpty_cd);
						stmt2.setString(3, agmt_no);
						stmt2.setString(4, agmt_rev_no);
						stmt2.setString(5, cont_no);
						stmt2.setString(6, cont_rev_no);
						stmt2.setString(7, contract_type);

						rset2 = stmt2.executeQuery();
						if (rset2.next()) {
							cont_ref_no = rset2.getString(1) == null ? "" : rset2.getString(1);
							start_dt = rset2.getString(2) == null ? "" : rset2.getString(2);
							end_dt = rset2.getString(3) == null ? "" : rset2.getString(3);
						}
						rset2.close();
						stmt2.close();
						
						queryString2 = "SELECT DR_CR_SEQ,DR_CR_REF_NO,DR_CR_FLAG,CRITERIA,REMARK FROM FMS_SALES_DR_CR_NOTE WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
								+ "AND FINANCIAL_YEAR = ? AND INVOICE_NO = ? AND INVOICE_SEQ = ? AND CONTRACT_TYPE = ?";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, own_cd);
						stmt2.setString(2, countpty_cd);
						stmt2.setString(3, fin_yr);
						stmt2.setString(4, rset5.getString(1));
						stmt2.setString(5, rset5.getString(2));
						stmt2.setString(6, contract_type);

						rset2 = stmt2.executeQuery();
						while (rset2.next()) {

							inv_index = inv_index + 1;
							VDRCR1_SEQ.add(rset2.getString(1) == null ? "" : rset2.getString(1)); 
							VDRCR_REF.add(rset2.getString(2) == null ? "" : rset2.getString(2));
							VDRCR_FLAG.add(rset2.getString(3) == null ? "" : rset2.getString(3));
							
							criteria = rset2.getString(4) == null ? "" : rset2.getString(4);
							
							getSelectedDrCrCriteria(criteria);
							
							VDRCR_ITEM_DESC.add(criteria_desc);
							VDRCR_CRITERIA.add(criteria);
							
							//VDRCR_ITEM_DESC.add(rset2.getString(4) == null ? "" : rset2.getString(4));
							VDRCR_REMARK.add(rset2.getString(5) == null ? "" : rset2.getString(5));
							VCOUNTERPARTY_CD.add(countpty_cd);

							VCOUNTERPARTY_ABBR.add("" + utilBean.getCounterpartyABBR(conn, countpty_cd));
							
							getBillingFreq_nm(freq);
							VDRCR_BILLING_FREQ_NM.add(billing_freq_nm);
							VDRCR_BILLING_FREQ_FLAG.add(freq);

							VDRCR_CONTRACT_TYPE.add(contract_type);
							VDRCR_AGMT_NO.add(agmt_no);
							VDRCR_AGMT_REV_NO.add(agmt_rev_no);
							VDRCR_CONT_NO.add(cont_no);
							VDRCR_CONT_REV_NO.add(cont_rev_no);

							VDRCR_BU_PLANT_SEQ.add(bu_plant_seq);
							VDRCR_BU_STATE_TIN.add(bu_state_tin);
							VDRCR_PLANT_SEQ.add(plant_seq);
							VDRCR_PLANT_ABBR.add("" + utilBean.getCounterpartyPlantABBR(conn, countpty_cd, own_cd, plant_seq, "C"));
							VDRCR_BU_PLANT_ABBR.add("" + utilBean.getCounterpartyPlantABBR(conn, own_cd, own_cd, bu_plant_seq, "B"));
							VDRCR_INVOICE_NO.add(rset5.getString(1) == null ? "" : rset5.getString(1));
							VDRCR_INVOICE_SEQ.add(rset5.getString(2) == null ? "" : rset5.getString(2));
							period_start_dt = rset5.getString(3) == null ? "" : rset5.getString(3);
							VDRCR_PERIOD_START_DT.add(period_start_dt);
							period_end_dt = rset5.getString(4) == null ? "" : rset5.getString(4);
							VDRCR_PERIOD_END_DT.add(period_end_dt);
							VDRCR_INVOICE_DT.add(rset5.getString(5) == null ? "" : rset5.getString(5));
							VDRCR_DUE_DT.add(rset5.getString(6) == null ? "" : rset5.getString(6));
							VFINANCIAL_YEAR.add(rset5.getString(15) == null ? "" : rset5.getString(15));
							VINVOICE_CURR.add("2");
							VDRCR_START_DT.add(start_dt);
							VDRCR_END_DT.add(end_dt);
							VDRCR_CARGO_NO.add(cargo_no);
							VDRCR_INV_FLAG.add(inv_flag);

							VDEAL_NO.add(deal_no);
							VDEAL_CONT_REF_NO.add(cont_ref_no);
							
							getDrcrNoteDtlforpreparationlist(own_cd,countpty_cd, cont_no, agmt_no,contract_type, plant_seq, 
									bu_plant_seq, freq, period_start_dt, period_end_dt, bu_state_tin);
							
						}
						rset2.close();
						stmt2.close();
						
						
					} 
					else 
					{
						deal_no = utilBean.NewDealMappingId(own_cd, countpty_cd, agmt_no, agmt_rev_no, cont_no,
								cont_rev_no, contract_type, cargo_no);

						queryString2 = "SELECT A.CARGO_REF,TO_CHAR(B.START_DT,'DD/MM/YYYY'),TO_CHAR(B.END_DT,'DD/MM/YYYY') "
								+ "FROM FMS_LTCORA_CONT_CARGO_DTL A, FMS_LTCORA_CONT_MST B "
								+ "WHERE A.COMPANY_CD = ? AND A.COUNTERPARTY_CD = ? AND A.AGMT_NO = ? AND A.AGMT_REV = ? AND A.CONT_NO = ? "
								+ "AND A.CONT_REV = ? AND A.CONTRACT_TYPE = ? AND A.CARGO_NO = ? "
								+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND A.AGMT_NO = B.AGMT_NO "
								+ "AND A.AGMT_REV = B.AGMT_REV AND A.CONT_NO = B.CONT_NO AND A.CONT_REV= B.CONT_REV AND A.CONTRACT_TYPE = B.CONTRACT_TYPE";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, own_cd);
						stmt2.setString(2, countpty_cd);
						stmt2.setString(3, agmt_no);
						stmt2.setString(4, agmt_rev_no);
						stmt2.setString(5, cont_no);
						stmt2.setString(6, cont_rev_no);
						stmt2.setString(7, contract_type);
						stmt2.setString(8, cargo_no);

						rset2 = stmt2.executeQuery();
						if (rset2.next()) {
							cont_ref_no = rset2.getString(1) == null ? "" : rset2.getString(1);
							start_dt = rset2.getString(2) == null ? "" : rset2.getString(2);
							end_dt = rset2.getString(3) == null ? "" : rset2.getString(3);
						}
						rset2.close();
						stmt2.close();
						
						
						queryString2 = "SELECT DR_CR_SEQ,DR_CR_REF_NO,DR_CR_FLAG,CRITERIA,REMARK FROM FMS_SALES_DR_CR_NOTE WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? "
								+ "AND FINANCIAL_YEAR = ? AND INVOICE_NO = ? AND INVOICE_SEQ = ? AND CONTRACT_TYPE = ?";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, own_cd);
						stmt2.setString(2, countpty_cd);
						stmt2.setString(3, fin_yr);
						stmt2.setString(4, rset5.getString(1));
						stmt2.setString(5, rset5.getString(2));
						stmt2.setString(6, contract_type);

						rset2 = stmt2.executeQuery();
						while (rset2.next()) {

							inv_index = inv_index + 1;
							VDRCR1_SEQ.add(rset2.getString(1) == null ? "" : rset2.getString(1)); 
							VDRCR_REF.add(rset2.getString(2) == null ? "" : rset2.getString(2));
							VDRCR_FLAG.add(rset2.getString(3) == null ? "" : rset2.getString(3));
							criteria = rset2.getString(4) == null ? "" : rset2.getString(4);
							getSelectedDrCrCriteria(criteria);
							
							VDRCR_ITEM_DESC.add(criteria_desc);
							VDRCR_CRITERIA.add(criteria);
							
							//VDRCR_ITEM_DESC.add(rset2.getString(4) == null ? "" : rset2.getString(4));
							VDRCR_REMARK.add(rset2.getString(5) == null ? "" : rset2.getString(5));
							VCOUNTERPARTY_CD.add(countpty_cd);

							VCOUNTERPARTY_ABBR.add("" + utilBean.getCounterpartyABBR(conn, countpty_cd));
							getBillingFreq_nm(freq);
							VDRCR_BILLING_FREQ_NM.add(billing_freq_nm);
							VDRCR_BILLING_FREQ_FLAG.add(freq);

							VDRCR_CONTRACT_TYPE.add(contract_type);
							VDRCR_AGMT_NO.add(agmt_no);
							VDRCR_AGMT_REV_NO.add(agmt_rev_no);
							VDRCR_CONT_NO.add(cont_no);
							VDRCR_CONT_REV_NO.add(cont_rev_no);

							VDRCR_BU_PLANT_SEQ.add(bu_plant_seq);
							VDRCR_BU_STATE_TIN.add(bu_state_tin);
							VDRCR_PLANT_SEQ.add(plant_seq);
							VDRCR_PLANT_ABBR.add("" + utilBean.getCounterpartyPlantABBR(conn, countpty_cd, own_cd, plant_seq, "C"));
							VDRCR_BU_PLANT_ABBR.add("" + utilBean.getCounterpartyPlantABBR(conn, own_cd, own_cd, bu_plant_seq, "B"));
							VDRCR_INVOICE_NO.add(rset5.getString(1) == null ? "" : rset5.getString(1));
							VDRCR_INVOICE_SEQ.add(rset5.getString(2) == null ? "" : rset5.getString(2));
							period_start_dt = rset5.getString(3) == null ? "" : rset5.getString(3);
							VDRCR_PERIOD_START_DT.add(period_start_dt);
							period_end_dt = rset5.getString(4) == null ? "" : rset5.getString(4);
							VDRCR_PERIOD_END_DT.add(period_end_dt);
							VDRCR_INVOICE_DT.add(rset5.getString(5) == null ? "" : rset5.getString(5));
							VDRCR_DUE_DT.add(rset5.getString(6) == null ? "" : rset5.getString(6));
							VFINANCIAL_YEAR.add(rset5.getString(15) == null ? "" : rset5.getString(15));
							VINVOICE_CURR.add("2");
							VDRCR_START_DT.add(start_dt);
							VDRCR_END_DT.add(end_dt);
							VDRCR_CARGO_NO.add(cargo_no);
							VDRCR_INV_FLAG.add(inv_flag);

							VDEAL_NO.add(deal_no);
							VDEAL_CONT_REF_NO.add(cont_ref_no);
							
							getDrcrNoteDtlforpreparationlist(own_cd,countpty_cd, cont_no, agmt_no,contract_type, plant_seq, 
									bu_plant_seq, freq, period_start_dt, period_end_dt, bu_state_tin);
						}
						rset2.close();
						stmt2.close();
					}
				}
				rset5.close();
				stmt5.close();
				
				VINDEX.add(inv_index);
				inv_index = 0;
				
			}			
		}
		catch (Exception e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getDrcrNoteDtlforpreparationlist(String own_cd,String countpty_cd, String contno, String agmtno,String cont_type, String plant_seq, 
			String bu_plant_seq, String billing_cycle, String period_start_dt, String period_end_dt,String state_code)
	{
		String function_nm="getDrcrNoteDtlforpreparationlist()";
		try
		{
			String inv_no="";
			String drcr_seq="";
			String checked_flag="";
			String approved_flag="";
			String pdf_flg="";
			String pdf_ori="";
			String pdf_tri="";
			String pdf_dup="";
			String sap_approved_flag="";
			String fiscal_yr="";
			String irn_no="";
			String in_no="";

			queryString1="SELECT DR_CR_NO,CHECKED_FLAG,APPROVED_FLAG,DR_CR_SEQ,PDF_INV_DTL,"
					+ "PRINT_BY_ORI,PRINT_BY_TRI,PRINT_BY_DUP,SAP_APPROVAL,DR_CR_FIN_YR,INVOICE_NO "
					+ "FROM FMS_SALES_DR_CR_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND AGMT_NO=? AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_UNIT=? "
					+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_STATE_TIN=? AND CONTRACT_TYPE = ?";
					//+ "AND FINANCIAL_YEAR=?";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, own_cd);
			stmt1.setString(2, countpty_cd);
			stmt1.setString(3, contno);
			stmt1.setString(4, agmtno);
			stmt1.setString(5, plant_seq);
			stmt1.setString(6, cont_type);
			stmt1.setString(7, bu_plant_seq);
			stmt1.setString(8, billing_cycle);
			stmt1.setString(9, period_start_dt);
			stmt1.setString(10, period_end_dt);
			stmt1.setString(11, state_code);
			stmt1.setString(12, cont_type);
			//stmt5.setString(12, financial_year);
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				inv_no=rset1.getString(1)==null?"":rset1.getString(1);
				checked_flag=rset1.getString(2)==null?"":rset1.getString(2);
				approved_flag=rset1.getString(3)==null?"":rset1.getString(3);
				drcr_seq=rset1.getString(4)==null?"":rset1.getString(4);
				pdf_flg=rset1.getString(5)==null?"":rset1.getString(5);
				pdf_ori=rset1.getString(6)==null?"":rset1.getString(6);
				pdf_tri=rset1.getString(7)==null?"":rset1.getString(7);
				pdf_dup=rset1.getString(8)==null?"":rset1.getString(8);
				sap_approved_flag=rset1.getString(9)==null?"":rset1.getString(9);
				fiscal_yr=rset1.getString(10)==null?"":rset1.getString(10);
				in_no =rset1.getString(11)==null?"":rset1.getString(11);
				
				VDRCR_INVOICE_EXIST.add("Y");
			}
			else
			{
				VDRCR_INVOICE_EXIST.add("N");
			}
			rset1.close();
			stmt1.close();
			
			if(cont_type.equals("O") || cont_type.equals("Q"))
			{
				queryString1="SELECT IRN_NO "
						+ "FROM FMS_INVOICE_IRN_DTL "
						+ "WHERE COMPANY_CD=? AND INVOICE_NO=? ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, in_no);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					irn_no=rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
				
				VDRCR_IS_IRN_GENERATED.add(irn_no.equals("")?"N":"Y");
			}
			else
			{
				VDRCR_IS_IRN_GENERATED.add("Y"); //other then LTCORA, Default 'Y'
			}
			
			VDRCR_SEQ.add(drcr_seq);
			VDRCR_NO.add(inv_no);
			VDRCR_INV_CHECKED_FLAG.add(checked_flag);
			VDRCR_INV_APPROVED_FLAG.add(approved_flag);
			VDRCR_PDF_INV_FLAG.add(pdf_flg);
			VDRCR_SAP_APPROVAL_FLAG.add(sap_approved_flag);
			VDRCR_FINANCIAL_YEAR.add(fiscal_yr);
			
			if(print_pdf_type.equals("O") && !pdf_ori.equals(""))
			{
				VDRCR_PDF_TYPE.add(print_pdf_type);
			}
			else if(print_pdf_type.equals("D") && !pdf_dup.equals(""))
			{
				VDRCR_PDF_TYPE.add(print_pdf_type);
			}
			else if(print_pdf_type.equals("T") && !pdf_tri.equals(""))
			{
				VDRCR_PDF_TYPE.add(print_pdf_type);
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
				VDRCR_PDF_TYPE.add(allPdfType);
			}
			else
			{
				VDRCR_PDF_TYPE.add("");
			}
			
			if(view_pdf_type.equals("All"))
			{
				queryString6="SELECT COUNT(*) "
						+ "FROM FMS_INV_FILE_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
	 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
	 	        		+ "AND PDF_TYPE!=?";
				stmt6=conn.prepareStatement(queryString6);
				stmt6.setString(1, own_cd);
				stmt6.setString(2, state_code);
				stmt6.setString(3, drcr_seq);
				//stmt6.setString(4, financial_year);
				stmt6.setString(4, fiscal_yr);
				stmt6.setString(5, "X");
				rset6=stmt6.executeQuery();
				if(rset6.next())
				{
					if(rset6.getInt(1)>0)
					{
						VDRCR_PDF_FILE_NAME.add("All");
						VDRCR_PDF_FILE_PATH.add("");
					}
					else
					{
						VDRCR_PDF_FILE_NAME.add("");
						VDRCR_PDF_FILE_PATH.add("");
					}
				}
				else
				{
					VDRCR_PDF_FILE_NAME.add("");
					VDRCR_PDF_FILE_PATH.add("");
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
				stmt6.setString(3, drcr_seq);
				//stmt6.setString(4, financial_year);
				stmt6.setString(4, fiscal_yr);
				stmt6.setString(5, view_pdf_type);
				rset6=stmt6.executeQuery();
				if(rset6.next())
				{
					String pdf_signed=rset6.getString(2)==null?"":rset6.getString(2);
					
					VDRCR_PDF_FILE_NAME.add(rset6.getString(1)==null?"":rset6.getString(1));
					if(pdf_signed.equals("Y"))
					{
						VDRCR_PDF_FILE_PATH.add(CommonVariable.signed_drcr_inv_path);
					}
					else
					{
						VDRCR_PDF_FILE_PATH.add(CommonVariable.drcr_inv_path);
					}
				}
				else
				{
					VDRCR_PDF_FILE_NAME.add("");
					VDRCR_PDF_FILE_PATH.add("");
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
	 	        		+ "AND PDF_SIGNED=? AND PDF_TYPE!=?";
				stmt6=conn.prepareStatement(queryString6);
				stmt6.setString(1, own_cd);
				stmt6.setString(2, state_code);
				stmt6.setString(3, drcr_seq);
				//stmt6.setString(4, financial_year);
				stmt6.setString(4, fiscal_yr);
				stmt6.setString(5, "Y");
				stmt6.setString(6, "X");
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
				
				VDRCR_PDF_SIGNED_FLAG.add("All");
				VDRCR_SIGN_PDF_TYPE.add(AllMailPdf);
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
				stmt6.setString(3, drcr_seq);
				//stmt6.setString(4, financial_year);
				stmt6.setString(4, fiscal_yr);
				stmt6.setString(5, view_pdf_type);
				rset6=stmt6.executeQuery();
				if(rset6.next())
				{
					String pdf_signed=rset6.getString(2)==null?"":rset6.getString(2);
					VDRCR_PDF_SIGNED_FLAG.add(pdf_signed);
					if(pdf_signed.equals("Y"))
					{
						VDRCR_SIGN_PDF_TYPE.add(mail_pdf_type);
					}
					else
					{
						VDRCR_SIGN_PDF_TYPE.add("");
					}
				}
				else
				{
					VDRCR_PDF_SIGNED_FLAG.add("");
					VDRCR_SIGN_PDF_TYPE.add("");
				}
				rset6.close();
				stmt6.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	
	public void getDrcrExistingNoteDtl()
	{
		String function_nm="getDrcrExistingNoteDtl()";
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
				submission_chk=false;
				
				bu_contact_person_cd=rset.getString(1)==null?"0":rset.getString(1);
				contact_person_cd=rset.getString(2)==null?"0":rset.getString(2);
				invoice_seq=rset.getString(3)==null?"":rset.getString(3);
				invoice_no=rset.getString(4)==null?"":rset.getString(4);
				invoice_dt=rset.getString(5)==null?"":rset.getString(5);
				invoice_due_dt=rset.getString(6)==null?"":rset.getString(6);
				exchng_rate_cd=rset.getString(11)==null?"":rset.getString(11);
				exchang_rate_dt=rset.getString(12)==null?"":rset.getString(12);
				exchang_rate=rset.getString(13)==null?"":nf2.format(rset.getDouble(13));
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
				
				transportation_charges=rset.getString(26)==null?"":nf2.format(rset.getDouble(26));
				transportation_amount=rset.getString(27)==null?"":nf.format(rset.getDouble(27));
				marketing_margin=rset.getString(34)==null?"":nf.format(rset.getDouble(34));
				marketing_margin_amount=rset.getString(35)==null?"":nf.format(rset.getDouble(35));
				other_charges=rset.getString(36)==null?"":nf.format(rset.getDouble(36));
				other_charges_amount=rset.getString(37)==null?"":nf.format(rset.getDouble(37));
				
				//tax_struct_dtl=utilBean.getEntityTaxStructureDtl(conn,comp_cd, counterparty_cd, "C", plant_seq, bu_unit, period_start_dt);
				tax_struct_dtl=utilBean.getTaxDescr(conn, tax_struct_cd);
			
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

				
				queryString1="SELECT A.BU_CONTACT_PERSON_CD,A.CONTACT_PERSON_CD,A.DR_CR_NO,TO_CHAR(A.DR_CR_DT,'DD/MM/YYYY'),"
						+ "TO_CHAR(A.DR_CR_DUE_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(A.PERIOD_END_DT,'DD/MM/YYYY'),"
						+ "B.ITEM_DIFF_VALUE,A.TOTAL_GROSS,A.TOTAL_TAX,A.TOTAL_AMT,A.APPROVED_FLAG,A.INVOICE_NO,A.CRITERIA,A.DR_CR_SEQ,B.ITEM_AMT,B.ITEM_DIFF_AMT, "
						+ "TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),TO_CHAR(A.APPROVED_DT,'DD/MM/YYYY HH24:MI:SS') "
						+ "FROM FMS_SALES_DR_CR_MST A, FMS_SALES_DR_CR_DTL B "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? "
						+ "AND A.AGMT_NO=? AND A.PLANT_SEQ=? AND A.BU_UNIT=? AND A.CONTRACT_TYPE=? "
						+ "AND A.BU_STATE_TIN=? "
						+ "AND A.PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND A.PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND A.DR_CR_FIN_YR=? "
						+ "AND A.INVOICE_SEQ=? AND A.CARGO_NO=? AND A.INV_FLAG=? AND A.DR_CR_SEQ = ? AND A.DR_CR_FLAG = ? "
						+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO = B.CONT_NO AND A.AGMT_NO = B.AGMT_NO "
						+ "AND A.CONTRACT_TYPE = B.CONTRACT_TYPE AND A.PLANT_SEQ = B.PLANT_SEQ AND A.BU_UNIT = B.BU_UNIT AND A.DR_CR_FLAG = B.DR_CR_FLAG "
						+ "AND A.DR_CR_SEQ = B.DR_CR_SEQ AND A.CRITERIA = B.CRITERIA ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, cont_no);
				stmt1.setString(4, agmt_no);
				stmt1.setString(5, plant_seq);
				stmt1.setString(6, bu_unit);
				stmt1.setString(7, contract_type);
				stmt1.setString(8, bu_state_tin);
				stmt1.setString(9, period_start_dt);
				stmt1.setString(10, period_end_dt);
				stmt1.setString(11, drcr_fin_yr);
				stmt1.setString(12, invoice_seq);
				stmt1.setString(13, cargo_no);
				stmt1.setString(14, inv_flag);
				stmt1.setString(15, drcr_seq);
				stmt1.setString(16, drcr_flag);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
				
						submission_chk=true;
						
						
						drcr_invoice_no = rset1.getString(3)==null?"":rset1.getString(3); 
						drcr_dt = rset1.getString(4)==null?"":rset1.getString(4); 
						drcr_due_dt = rset1.getString(5)==null?"":rset1.getString(5); 
						
						if(criteria.equals("3")) {
							drcr_alloc_qty = rset1.getString(17)==null?"":rset1.getString(17); 
						}
						else if(criteria.equals("2")) {
							drcr_price = rset1.getString(17)==null?"":rset1.getString(17);
						}
						else if(criteria.equals("1")) {
							drcr_exchng = rset1.getString(17)==null?"":rset1.getString(17);
						}
//						drcr_price = nf2.format(rset1.getDouble(8) == 0 ? "" : rset1.getDouble(8));
						drcr_gross_amt1 = rset1.getString(9)==null?"":rset1.getString(9); 
						drcr_tax_amt = rset1.getString(10)==null?"":rset1.getString(10); 
						drcr_invoice_amt = nf.format(rset1.getDouble(11));
						drcr_net_payable = nf.format(rset1.getDouble(11));
						drcr_inv_entered_on = rset1.getString(18)==null?"":rset1.getString(18); 
						drcr_inv_approved_on = rset1.getString(19)==null?"":rset1.getString(19); 
						
						if(operation.equals("INSERT"))
						{
							

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
							

							
//							tcs_struct_cd=rset.getString(28)==null?"":rset.getString(28);
//							tcs_struct_dt=rset.getString(29)==null?"":rset.getString(29);
//							
//							tds_amt=rset.getString(30)==null?"":nf.format(rset.getDouble(30));
//							tds_factor=rset.getString(31)==null?"":nf.format(rset.getDouble(31));
//							tds_struct_cd=rset.getString(32)==null?"":rset.getString(32);
//							tds_struct_dt=rset.getString(33)==null?"":rset.getString(33);
						
//							inv_entered_on = rset.getString(38)==null?"":rset.getString(38);
//							inv_approved_on = rset.getString(39)==null?"":rset.getString(39);
							
							
							
							
							queryString1 ="SELECT CRITERIA,DR_CR_REF_NO FROM FMS_SALES_DR_CR_NOTE WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND FINANCIAL_YEAR = ? "
									+ "AND INVOICE_SEQ = ? AND INVOICE_NO = ? AND INVOICE_DT = TO_DATE(?,'DD/MM/YYYY') AND CONTRACT_TYPE = ?";
							stmt2=conn.prepareStatement(queryString1);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, counterparty_cd);
							stmt2.setString(3, exist_financial_year);
							stmt2.setString(4, invoice_seq);
							stmt2.setString(5, invoice_no);
							stmt2.setString(6, invoice_dt);
							stmt2.setString(7, contract_type);
							rset2 = stmt2.executeQuery();
							if(rset2.next()) {
								criteria_desc = rset2.getString(1);
								drcr_ref = rset2.getString(2);
							}
							else {
								criteria_desc = "0";
							}
							rset2.close();
							stmt2.close();
							
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
	
	
	public void getDrcrNoteDetail()
	{
		String function_nm="getDrcrNoteDetail()";
		try
		{
			
			String criteria="",item_value="";
			
			String component_flag="";
        	String component1="";
        	String component2="";
        	String exchang_rate_nm="";
        	String component1_rate_nm="";
        	String component2_rate_nm="";
        	String component1_rate="";
        	String component2_rate="";

        	
			queryString="SELECT ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_VALUE,INVOICE_ID_SEQ,EXCHG_RATE_CD,"
					+ "TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),EXCHG_RATE_TYPE,INVOICE_RAISED_IN,TAX_STRUCT_CD,TO_CHAR(INVOICE_DT,'DD-MON-YY'),REMARK_1,REMARK_2 "
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
				
				qty_mmbtu=nf.format(rset.getDouble(1));
				price_cd=rset.getString(3)==null?"":rset.getString(3);
				price=utilBean.RateNumberFormat(rset.getDouble(2), price_cd);
				gross_amt=nf.format(rset.getDouble(4));
				exchang_rate=nf2.format(rset.getDouble(5));
				exchng_rate_cd=rset.getString(7)==null?"":rset.getString(7);
				exchang_rate_dt=rset.getString(8)==null?"":rset.getString(8);
				exchang_rate_type=rset.getString(9)==null?"":rset.getString(9);
				invoice_raised_in=rset.getString(10)==null?"":rset.getString(10);
				tax_struct_cd=rset.getString(11)==null?"":rset.getString(11);
				invoice_dt=rset.getString(12)==null?"":rset.getString(12);
				remark_1=rset.getString(13)==null?"":rset.getString(13);
				remark_2=rset.getString(14)==null?"":rset.getString(14);
				
		     			
				
				queryString1="SELECT A.BU_CONTACT_PERSON_CD,A.CONTACT_PERSON_CD,A.DR_CR_NO,TO_CHAR(A.DR_CR_DT,'DD-MON-YY'),"
						+ "TO_CHAR(A.DR_CR_DUE_DT,'DD-MON-YY'),TO_CHAR(A.PERIOD_START_DT,'DD-MON-YY'),TO_CHAR(A.PERIOD_END_DT,'DD-MON-YY'),"
						+ "B.ITEM_DIFF_VALUE,A.TOTAL_GROSS,A.TOTAL_TAX,A.TOTAL_AMT,A.APPROVED_FLAG,A.INVOICE_NO,A.CRITERIA,A.DR_CR_SEQ,B.ITEM_AMT,B.ITEM_DIFF_AMT,A.INVOICE_ID_SEQ "
						+ "FROM FMS_SALES_DR_CR_MST A, FMS_SALES_DR_CR_DTL B "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? "
						+ "AND A.AGMT_NO=? AND A.PLANT_SEQ=? AND A.BU_UNIT=? AND A.CONTRACT_TYPE=? "
						+ "AND A.BU_STATE_TIN=? "
						+ "AND A.PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND A.PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND A.DR_CR_FIN_YR=? "
						+ "AND A.INVOICE_SEQ=? AND A.CARGO_NO=? AND A.INV_FLAG=? AND A.DR_CR_SEQ = ? AND A.DR_CR_FLAG = ? "
						+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO = B.CONT_NO AND A.AGMT_NO = B.AGMT_NO "
						+ "AND A.CONTRACT_TYPE = B.CONTRACT_TYPE AND A.PLANT_SEQ = B.PLANT_SEQ AND A.BU_UNIT = B.BU_UNIT AND A.DR_CR_FLAG = B.DR_CR_FLAG "
						+ "AND A.DR_CR_SEQ = B.DR_CR_SEQ AND A.CRITERIA = B.CRITERIA ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, cont_no);
				stmt1.setString(4, agmt_no);
				stmt1.setString(5, plant_seq);
				stmt1.setString(6, bu_unit);
				stmt1.setString(7, contract_type);
				stmt1.setString(8, bu_state_tin);
				stmt1.setString(9, period_start_dt);
				stmt1.setString(10, period_end_dt);
				stmt1.setString(11, drcr_fin_yr);
				stmt1.setString(12, invoice_seq);
				stmt1.setString(13, cargo_no);
				stmt1.setString(14, inv_flag);
				stmt1.setString(15, drcr_seq);
				stmt1.setString(16, drcr_flag);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{		
					
					bu_contact_person_cd=rset1.getString(1)==null?"0":rset1.getString(1);
					contact_person_cd=rset1.getString(2)==null?"0":rset1.getString(2);
					drcr_invoice_no=rset1.getString(3)==null?"":rset1.getString(3);
					drcr_dt=rset1.getString(4)==null?"":rset1.getString(4);
					invoice_due_dt=rset1.getString(5)==null?"":rset1.getString(5);
					inv_period_start_dt=rset1.getString(6)==null?"":rset1.getString(6);
					inv_period_end_dt=rset1.getString(7)==null?"":rset1.getString(7);
					gross_amt1=nf.format(rset1.getDouble(9));
					tax_amt=nf.format(rset1.getDouble(10));
					invoice_amt=nf.format(rset1.getDouble(11));
					net_payable=nf.format(rset1.getDouble(11));
					invoice_no=rset1.getString(13)==null?"":rset1.getString(13);
					criteria = rset1.getString(14)==null?"":rset1.getString(14);
					invoice_id_seq=rset1.getString(18)==null?"":rset1.getString(18);
					
					
					if(criteria.equals("1")) {
						item_value = nf2.format(rset1.getDouble(8));
					}
					else if(criteria.equals("2")) {
						item_value = nf2.format(rset1.getDouble(8));
					}
					else if(criteria.equals("3")) {
						item_value = nf2.format(rset1.getDouble(8));
					}
					
					
					if (is_attachment.equals("1")) {
						
						if(criteria.equals("1")) {
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
							
//							if(exchang_rate_type.equals("A"))
//							{
//								queryString1="SELECT EXCHG_RATE_VALUE,TO_CHAR(ALLOCATION_DT,'DD-MON-YY') "
//										+ "FROM FMS_INVOICE_DTL "
//										+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
//										+ "AND BU_STATE_TIN=? AND INVOICE_SEQ=? "
//										+ "ORDER BY ALLOCATION_DT";
//								stmt1=conn.prepareStatement(queryString1);
//								stmt1.setString(1, comp_cd);
//								stmt1.setString(2, financial_year);
//								stmt1.setString(3, bu_state_tin);
//								stmt1.setString(4, invoice_seq);
//								rset1=stmt1.executeQuery();
//								while(rset1.next())
//								{
//									String ExRate=nf2.format(rset1.getDouble(1));
//									String allocDt=rset1.getString(2)==null?"":rset1.getString(2);
//									
//									VATT2_EXCHANGE_DESC.add(exchang_rate_nm+".. On "+allocDt+" (INR/USD)");
//									VATT2_RATE.add(nf2.format(Double.parseDouble(""+ExRate)));
//								}
//								rset1.close();
//								stmt1.close();
//							}
//							else if(component_flag.equals("Y"))
//							{
//								queryString1="SELECT EXC_RATE_NM "
//										+ "FROM FMS_EXCHG_RATE_MST "
//										+ "WHERE EXC_RATE_CD=?";
//								stmt1=conn.prepareStatement(queryString1);
//								stmt1.setString(1, component1);
//								rset1=stmt1.executeQuery();
//								if(rset1.next())
//								{
//									component1_rate_nm=rset1.getString(1)==null?"":rset1.getString(1);
//								}
//								rset1.close();
//								stmt1.close();
//								
//								queryString2="SELECT EXC_RATE_NM "
//										+ "FROM FMS_EXCHG_RATE_MST "
//										+ "WHERE EXC_RATE_CD=?";
//								stmt2=conn.prepareStatement(queryString2);
//								stmt2.setString(1, component2);
//								rset2=stmt2.executeQuery();
//								if(rset2.next())
//								{
//									component2_rate_nm=rset2.getString(1)==null?"":rset2.getString(1);
//								}
//								rset2.close();
//								stmt2.close();
//								
//								queryString3="SELECT EXCHG_VAL "
//										+ "FROM FMS_EXCHG_RATE_ENTRY "
//										+ "WHERE EXCHG_RATE_CD=? "
//										+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY')";
//								stmt3=conn.prepareStatement(queryString3);
//								stmt3.setString(1, component1);
//								stmt3.setString(2, exchang_rate_dt);
//								rset3=stmt3.executeQuery();
//								if(rset3.next())
//								{
//									component1_rate=nf2.format(rset3.getDouble(1));
//								}
//								rset3.close();
//								stmt3.close();
//								
//								queryString4="SELECT EXCHG_VAL "
//										+ "FROM FMS_EXCHG_RATE_ENTRY "
//										+ "WHERE EXCHG_RATE_CD=? "
//										+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY')";
//								stmt4=conn.prepareStatement(queryString4);
//								stmt4.setString(1, component2);
//								stmt4.setString(2, exchang_rate_dt);
//								rset4=stmt4.executeQuery();
//								if(rset4.next())
//								{
//									component2_rate=nf2.format(rset4.getDouble(1));
//								}
//								rset4.close();
//								stmt4.close();
//								
//								VATT2_EXCHANGE_DESC.add(component1_rate_nm+".. On "+exchang_rate_dt);
//								VATT2_RATE.add(nf2.format(Double.parseDouble(""+component1_rate)));
//								
//							}
//							else
//							{
								VATT2_EXCHANGE_DESC.add(exchang_rate_nm+".. On "+exchang_rate_dt);
								VATT2_RATE.add(nf2.format(Double.parseDouble(""+exchang_rate))+ " &nbsp USD");
//							}
							
							VATT2_EXCHANGE_DESC.add("Applicable Exchange Rate");
							VATT2_RATE.add(nf2.format(rset1.getDouble(17) == 0 ? "" : rset1.getDouble(17))+ " &nbsp USD");
							VATT2_EXCHANGE_DESC.add("<b>Difference in Exchange Rate</b>");
							VATT2_RATE.add("<b>" + nf2.format(rset1.getDouble(8) == 0 ? "" : rset1.getDouble(8))+ "</b> &nbsp USD");
						}
						else if (criteria.equals("2")) {
							item_value = nf2.format(rset1.getDouble(8));
							
							
							VATT2_EXCHANGE_DESC.add("Invocie Sales Price Rate");
							VATT2_RATE.add(nf2.format(rset1.getDouble(16) == 0 ? "" : rset1.getDouble(16))+ " &nbsp USD");
							VATT2_EXCHANGE_DESC.add("Applicable Sales Price Rate");
							VATT2_RATE.add(nf2.format(rset1.getDouble(17) == 0 ? "" : rset1.getDouble(17))+ " &nbsp USD");
							VATT2_EXCHANGE_DESC.add("<b>Difference in Sales Price Rate</b>");
							VATT2_RATE.add("<b>" + nf2.format(rset1.getDouble(8) == 0 ? "" : rset1.getDouble(8))+ "</b> &nbsp USD");
						}
						else if(criteria.equals("3")) {
							item_value = nf.format(rset1.getDouble(8));
							
							VATT2_EXCHANGE_DESC.add("Invoice Quantity(MMBTU)");
							VATT2_RATE.add(nf.format(rset1.getDouble(16) == 0 ? "" : rset1.getDouble(16)));
							VATT2_EXCHANGE_DESC.add("Applicable (MMBTU)");
							VATT2_RATE.add(nf.format(rset1.getDouble(17) == 0 ? "" : rset1.getDouble(17)));
							VATT2_EXCHANGE_DESC.add("<b>Difference in Quantity</b>");
							VATT2_RATE.add("<b>" + nf.format(rset1.getDouble(8) == 0 ? "" : rset1.getDouble(8))+ "</b>");
						}
						
					}
					
					
					if(activityFlag.equals("APPROVE")) {
						activity_value=rset1.getString(12)==null?"":rset1.getString(12);
					}
				
					tax_struct_dtl=utilBean.getTaxDescr(conn, tax_struct_cd);
			

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
				rset1.close();
				stmt1.close();
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
					+ "&bu_state_tin="+bu_state_tin+"&invoice_seq="+invoice_seq+"&activityFlag="+activityFlag+"&cargo_no="+cargo_no+"&inv_flag="+inv_flag+""
					+ "&drcr_flag="+drcr_flag+"&drcr_seq="+drcr_seq+"&criteria="+criteria+"&drcr_fin_yr="+drcr_fin_yr;

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
//					if(inv_flag.equals("ST"))
//					{
//						srno+=1;
//						VPDF_COL1.add(srno);
//						VPDF_COL2.add("Storage Charges For The Extended Storage Duration For Cargo Arrived on "+arrivalDt);
//						VPDF_COL3.add("<a onclick=openAtt1('rpt_view_attachment1.jsp?"+parameter+"')>Att1</a>");
//						VPDF_COL4.add(""+price_cd_nm);
//						VPDF_COL5.add("<a onclick=openAtt1('rpt_view_attachment1.jsp?"+parameter+"')>As per Att1</a>");
//						VPDF_COL6.add("<a onclick=openAtt1('rpt_view_attachment1.jsp?"+parameter+"')>As per Att1</a>");
//						VPDF_COL7.add(""+gross_amt);
//					}
//					else
//					{
					
//					srno+=1;
//					VPDF_COL1.add(srno);
//					VPDF_COL2.add("Natural Gas (Regasified)");
//					VPDF_COL3.add("<a onclick=openAtt1('rpt_view_attachment1.jsp?"+parameter+"')>Att1</a>");
//					VPDF_COL4.add("");
//					VPDF_COL5.add(qty_mmbtu);
//					VPDF_COL6.add("");
//					VPDF_COL7.add("");
//					
//					srno+=1;
//					VPDF_COL1.add(srno);
//					VPDF_COL2.add("LTCORA Tariff");
//					VPDF_COL3.add("");
//					VPDF_COL4.add(""+utilBean.getRateUnitNm(conn,price_cd)+"/MMBTU");
//					VPDF_COL5.add("");
//					VPDF_COL6.add(price);
//					VPDF_COL7.add("");
//					
//					if(price_cd.equals("2"))
//					{
//						srno+=1;
//						VPDF_COL1.add(srno);
//						VPDF_COL2.add("Gross Amount(USD)");
//						VPDF_COL3.add("");
//						VPDF_COL4.add(""+utilBean.getRateUnitNm(conn,price_cd));
//						VPDF_COL5.add("");
//						VPDF_COL6.add("");
//						VPDF_COL7.add(gross_amt);
//					}
//					}
					
					if(criteria.equals("3")) 
					{
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("Natural Gas (Delivered) as per Invoice ref "+invoice_no+ " dated "+invoice_dt);
						VPDF_COL3.add("");
						VPDF_COL4.add(""+utilBean.getRateUnitNm(conn,price_cd));
						VPDF_COL5.add(qty_mmbtu);
						VPDF_COL6.add(price);
						VPDF_COL7.add("");
						
						
						BigDecimal price1 = BigDecimal.valueOf(Double.parseDouble(price));
						BigDecimal quantity = BigDecimal.valueOf(Double.parseDouble(item_value));
						
						BigDecimal diff_gross = new BigDecimal(0);
						diff_gross = price1.multiply(quantity);
						
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("Difference in Quantity");
						VPDF_COL3.add("<a onclick=openAtt1('rpt_drcr_view_attachment1.jsp?"+parameter+"')>Att1</a>");
						VPDF_COL4.add(""+utilBean.getRateUnitNm(conn,price_cd));
						VPDF_COL5.add(item_value);
						VPDF_COL6.add("");
						VPDF_COL7.add("");
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("Gross Amount");
						VPDF_COL3.add("");
						VPDF_COL4.add(""+utilBean.getRateUnitNm(conn,price_cd));
						VPDF_COL5.add("");
						VPDF_COL6.add("");
						VPDF_COL7.add(diff_gross);
					}
					else {
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("Natural Gas (Delivered) as per Invoice ref "+invoice_no+ " dated "+invoice_dt);
						VPDF_COL3.add("");
						VPDF_COL4.add(""+utilBean.getRateUnitNm(conn,price_cd));
						VPDF_COL5.add(qty_mmbtu);
						VPDF_COL6.add(price);
						VPDF_COL7.add(gross_amt);
					}
					


				}
				else
				{
//					double diff_gross = Double.parseDouble(price)*Double.parseDouble(item_value);
//		            String diff_gross1 = nf.format(diff_gross);
					if(criteria.equals("3")) 
					{
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("Natural Gas (Delivered) as per Invoice ref "+invoice_no+ " dated "+invoice_dt);
						VPDF_COL3.add("");
						VPDF_COL4.add(""+utilBean.getRateUnitNm(conn,price_cd));
						VPDF_COL5.add(qty_mmbtu);
						VPDF_COL6.add(price);
						VPDF_COL7.add("");
						
						
						BigDecimal price1 = BigDecimal.valueOf(Double.parseDouble(price));
						BigDecimal quantity = BigDecimal.valueOf(Double.parseDouble(item_value));
						
						BigDecimal diff_gross = new BigDecimal(0);
						diff_gross = price1.multiply(quantity);
						
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("Difference in Quantity");
						VPDF_COL3.add("<a onclick=openAtt1('rpt_drcr_view_attachment1.jsp?"+parameter+"')>Att1</a>");
						VPDF_COL4.add(""+utilBean.getRateUnitNm(conn,price_cd));
						VPDF_COL5.add(item_value);
						VPDF_COL6.add("");
						VPDF_COL7.add("");
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("Gross Amount");
						VPDF_COL3.add("");
						VPDF_COL4.add(""+utilBean.getRateUnitNm(conn,price_cd));
						VPDF_COL5.add("");
						VPDF_COL6.add("");
						VPDF_COL7.add(diff_gross);
					}
					else {
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("Natural Gas (Delivered) as per Invoice ref "+invoice_no+ " dated "+invoice_dt);
						VPDF_COL3.add("");
						VPDF_COL4.add(""+utilBean.getRateUnitNm(conn,price_cd));
						VPDF_COL5.add(qty_mmbtu);
						VPDF_COL6.add(price);
						VPDF_COL7.add(gross_amt);
					}
				}
				
			}
			
			if(price_cd.equals("2"))
			{
				if (!criteria.equals("1")) {
					srno += 1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("Exchange Rate");
					VPDF_COL3.add("");
					VPDF_COL4.add("INR/USD");
					VPDF_COL5.add("");
					VPDF_COL6.add(exchang_rate);
					VPDF_COL7.add("");
				}
				else if(criteria.equals("1")) {
					srno += 1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("Difference in Exchange Rate");
					VPDF_COL3.add("<a onclick=openAtt1('rpt_drcr_view_attachment1.jsp?"+parameter+"')>Att1</a>");
					VPDF_COL4.add("INR/USD");
					VPDF_COL5.add("");
					VPDF_COL6.add(item_value);
					VPDF_COL7.add("");
				}
			}
			
			if(!inv_flag.equals("UG"))
			{
				total_exchng_label="Sales Price Rate";
				total_exchng_label_rate=exchang_rate;
				if (criteria.equals("2")) {
					srno += 1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("Difference In Sales Rate");
					VPDF_COL3.add("<a onclick=openAtt1('rpt_drcr_view_attachment1.jsp?" + parameter + "')>Att1</a>");
					VPDF_COL4.add("USD");
					VPDF_COL5.add("");
					VPDF_COL6.add(item_value);
					VPDF_COL7.add("");
				}
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
//			if(!transportation_charges.equals(""))
//			{
//				if(Double.parseDouble(transportation_amount) > 0)
//				{
//					srno+=1;
//					VPDF_COL1.add(srno);
//					VPDF_COL2.add("Transportation Tariff");
//					VPDF_COL3.add("");
//					VPDF_COL4.add("INR");
//					VPDF_COL5.add("");
//					VPDF_COL6.add(transportation_charges);
//					VPDF_COL7.add(transportation_amount);
//				}
//			}
//			
//			if(!marketing_margin.equals(""))
//			{
//				if(Double.parseDouble(marketing_margin_amount) > 0)
//				{
//					srno+=1;
//					VPDF_COL1.add(srno);
//					VPDF_COL2.add("Marketing Margin");
//					VPDF_COL3.add("");
//					VPDF_COL4.add("INR");
//					VPDF_COL5.add("");
//					VPDF_COL6.add(marketing_margin);
//					VPDF_COL7.add(marketing_margin_amount);
//				}
//			}
//			
//			if(!other_charges.equals(""))
//			{
//				if(Double.parseDouble(other_charges_amount) > 0)
//				{
//					srno+=1;
//					VPDF_COL1.add(srno);
//					VPDF_COL2.add("Other Charges");
//					VPDF_COL3.add("");
//					VPDF_COL4.add("INR");
//					VPDF_COL5.add("");
//					VPDF_COL6.add(other_charges);
//					VPDF_COL7.add(other_charges_amount);
//				}
//			}
//			
//			if(isGrossIncTriff)
//			{
//				srno+=1;
//				VPDF_COL1.add(srno);
//				VPDF_COL2.add("Total Gross Amount");
//				VPDF_COL3.add("");
//				VPDF_COL4.add("INR");
//				VPDF_COL5.add("");
//				VPDF_COL6.add("");
//				VPDF_COL7.add(gross_include_transport_tariff);
//			}
//			
			srno+=1;
			VPDF_COL1.add(srno);
			VPDF_COL2.add("Tax ("+tax_struct_dtl+")");
			VPDF_COL3.add("");
			VPDF_COL4.add("INR");
			VPDF_COL5.add("");
			VPDF_COL6.add("");
			VPDF_COL7.add(tax_amt);
			
//			double temp_srno=srno;
//			if(tax_struct_dtl.contains(",")) {
//				BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_amt));
//				BigDecimal factor = new BigDecimal(2);
//				for(int i = 0; i<tax_struct_dtl.split(",").length;i++) {
//					BigDecimal sub_tax_amt = tax_amt1.divide(factor, 5, RoundingMode.HALF_UP);
//					temp_srno=temp_srno+0.1;
//					VPDF_COL1.add(nf0.format(temp_srno));
//					VPDF_COL2.add(rset.getString(2)==null?"":rset.getString(2));
//					VPDF_COL3.add("");
//					VPDF_COL4.add("INR");
//					VPDF_COL5.add("");
//					VPDF_COL6.add("");
//					VPDF_COL7.add(sub_tax_amt);
//				}
//			}
			
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
					BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_amt));
					BigDecimal factor = new BigDecimal(2);
					
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
						BigDecimal sub_tax_amt = tax_amt1.divide(factor, 2, RoundingMode.HALF_UP);
						temp_srno=temp_srno+0.1;
						VPDF_COL1.add(nf0.format(temp_srno));
						VPDF_COL2.add(rset.getString(2)==null?"":rset.getString(2));
						VPDF_COL3.add("");
						VPDF_COL4.add("INR");
						VPDF_COL5.add("");
						VPDF_COL6.add("");
						VPDF_COL7.add(sub_tax_amt);
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
			
//			if(applicable_abbr.equals("TCS"))
//			{
//				srno+=1;
//				VPDF_COL1.add(srno);
//				VPDF_COL2.add("TCS");
//				VPDF_COL3.add("");
//				VPDF_COL4.add("INR");
//				VPDF_COL5.add("");
//				VPDF_COL6.add(TCS_factor+"%");
//				VPDF_COL7.add(applicable_amt);
//			}
//			
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
	String dr_cr_flag =  "";
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
	String drcr_fin_yr="";
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
	String allocQty="";
	String temp_price="";
	

	//Deep260825
	String drcr_invoice_no = "",drcr_seq = "",drcr_ref="",criteria="",drcr_flag="";
	String drcr_dt="",drcr_due_dt="",drcr_alloc_qty="",drcr_price="",drcr_gross_amt1="",drcr_tax_amt="",drcr_exchng="";
	String drcr_inv_entered_on="",drcr_invoice_amt="",drcr_net_payable="",drcr_inv_approved_on="";
	String segment="";
	//Deep260825
	
	
	public void setdrcr_seq(String drcr_seq) {this.drcr_seq = drcr_seq;}
	public void setdrcr_flag(String drcr_flag) {this.drcr_flag = drcr_flag;}
	public void setdrcr_dt(String drcr_dt) {this.drcr_flag = drcr_dt;}
	public void setcriteria(String criteria) {this.criteria = criteria;}
	
	
	public void setMonth(String month) {this.month = month;}
	public void setYear(String year) {this.year = year;}
	public void setBilling_cycle(String billing_cycle) {this.billing_cycle = billing_cycle;}
	public void setPeriod_start_dt(String period_start_dt) {this.period_start_dt = period_start_dt;}
	public void setPeriod_end_dt(String period_end_dt) {this.period_end_dt = period_end_dt;}
	public void setdr_cr_flag(String dr_cr_flag) {this.dr_cr_flag = dr_cr_flag;}
	
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
	public void setdrcr_fin_yr(String drcr_fin_yr) {this.drcr_fin_yr = drcr_fin_yr;}
	public void setBu_state_tin(String bu_state_tin) {this.bu_state_tin = bu_state_tin;}
	public void setInvoice_seq(String invoice_seq) {this.invoice_seq = invoice_seq;}
	public void setFlag(String flag) {this.flag = flag;}
	public void setFilter_cont_type(String filter_cont_type) {this.filter_cont_type = filter_cont_type;}
	public void setInv_flag(String inv_flag) {this.inv_flag = inv_flag;}
	public void setAllocQty(String allocQty) {this.allocQty = allocQty;}
	public void setTemp_price(String temp_price) {this.temp_price = temp_price;}
	
	public String getPeriod_start_dt() {return period_start_dt;}
	public String getPeriod_end_dt() {return period_end_dt;}
	public String getTemp_period_start_dt() {return temp_period_start_dt;}
	public String getTemp_period_end_dt() {return temp_period_end_dt;}
	public String getUser_defined_dt() {return user_defined_dt;}
	public String getFinancial_year() {return financial_year;}
	public String getBu_state_tin() {return bu_state_tin;}
	
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
	
	public String getExchange_rate_mapping() {return exchange_rate_mapping;}
	
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VCOUNTERPTY_CD = new Vector();
	Vector VCOUNTERPTY_ABBR = new Vector();
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
	
//	Vector VOTH_COUNTERPTY_CD = new Vector();
//	Vector VOTH_COUNTERPTY_ABBR = new Vector();
//	Vector VOTH_CONT_NO = new Vector();
//	Vector VOTH_CONT_REV_NO = new Vector();
//	Vector VOTH_AGMT_NO = new Vector();
//	Vector VOTH_AGMT_REV_NO = new Vector();
//	Vector VOTH_CONTRACT_TYPE = new Vector();
//	Vector VOTH_DEAL_NO = new Vector();
//	Vector VOTH_PLANT_SEQ = new Vector();
//	Vector VOTH_PLANT_ABBR = new Vector();
//	Vector VOTH_BU_PLANT_SEQ = new Vector();
//	Vector VOTH_BU_PLANT_ABBR = new Vector();
//	Vector VOTH_START_DT = new Vector();
//	Vector VOTH_END_DT = new Vector();
//	Vector VOTH_CONT_NAME = new Vector();
//	Vector VOTH_CONT_REF_NO = new Vector();
//	Vector VOTH_PERIOD_START_DT = new Vector();
//	Vector VOTH_PERIOD_END_DT = new Vector();
//	Vector VOTH_INVOICE_NO = new Vector();
//	Vector VOTH_DIS_INVOICE_NO = new Vector();
//	Vector VOTH_STATUS = new Vector();
//	Vector VOTH_BILLING_FREQ_FLAG = new Vector();
//	Vector VOTH_BILLING_FREQ_NM = new Vector();
//	Vector VOTH_INVOICE_SEQ = new Vector();
//	Vector VOTH_INVOICE_EXIST = new Vector();
//	Vector VOTH_INV_CHECKED_FLAG = new Vector();
//	Vector VOTH_INV_APPROVED_FLAG = new Vector();
//	Vector VOTH_PDF_INV_FLAG = new Vector();
//	Vector VOTH_PDF_TYPE = new Vector();
//	Vector VOTH_PDF_FILE_NAME = new Vector();
//	Vector VOTH_PDF_FILE_PATH=new Vector();
//	Vector VOTH_PDF_SIGNED_FLAG=new Vector();
//	Vector VOTH_SIGN_PDF_TYPE = new Vector();
//	Vector VOTH_FINANCIAL_YEAR = new Vector();
//	Vector VOTH_INVOICE_TYPE = new Vector();
//	Vector VOTH_BU_STATE_TIN = new Vector();
	
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
	
//	Vector VSAP_APPROVAL_FLAG = new Vector();
//	Vector VOTH_SAP_APPROVAL_FLAG = new Vector();
//	Vector VFILTER_CONT_TYPE = new Vector();
//	Vector VFILTER_CONT_NAME = new Vector();
	
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
	
	Vector VSTORAGE_DATE = new Vector();
	Vector VSTORAGE_INVENTORY = new Vector();
	Vector VOFFTAKE_QTY = new Vector();
	Vector VUSER_DEFINE = new Vector();
	Vector VSTORAGE_CHARGE = new Vector();
	Vector VSTORAGE_AMT = new Vector();
	Vector VRATE_TYPE = new Vector();
	Vector VDISCOUNT_FLAG = new Vector();
	
	
	//For Debit/Credit Note   //Deep20250818
	Vector VDRCR_BU_STATE_TIN = new Vector();	
	Vector VDRCR_AGMT_NO = new Vector();
	Vector VDRCR_CONT_NO = new Vector();
	Vector VDRCR_CARGO_NO = new Vector();
	Vector VDRCR_START_DT = new Vector();
	Vector VDRCR_END_DT = new Vector();
	Vector VDRCR_CONT_REF_NO = new Vector();
	Vector VDRCR_CONTRACT_TYPE = new Vector();
	Vector VDRCR_CONT_NAME = new Vector();
	Vector VDRCR_DEAL_NO = new Vector();
	Vector VDRCR_PLANT_SEQ = new Vector();
	Vector VDRCR_PLANT_ABBR = new Vector();
	Vector VDRCR_BU_PLANT_SEQ = new Vector();
	Vector VDRCR_BU_PLANT_ABBR = new Vector();
	Vector VDRCR_PERIOD_START_DT = new Vector();
	Vector VDRCR_PERIOD_END_DT = new Vector();
	Vector VDRCR_TEMP_PERIOD_START_DT = new Vector();
	Vector VDRCR_TEMP_PERIOD_END_DT = new Vector();
	Vector VDRCR_INVOICE_DT = new Vector();
	Vector VDRCR_DUE_DT = new Vector();
	Vector VDRCR_AGMT_BASE = new Vector();
	Vector VDRCR_STATUS = new Vector();
	Vector VDRCR_BILLING_FREQ_FLAG = new Vector();
	Vector VDRCR_BILLING_FREQ_NM = new Vector();
	Vector VDRCR_DIFF_COLOR = new Vector();
	Vector VDRCR_ALLOC_QTY = new Vector();
	Vector VDRCR_INV_FLAG = new Vector();
	Vector VDRCR_INVOICE_NO = new Vector();
	Vector VDRCR_INVOICE_SEQ = new Vector();
	Vector VDRCR_INV_APPROVED_FLAG = new Vector();
	Vector VDRCR_INV_CHECKED_FLAG = new Vector();
	Vector VDRCR_PDF_INV_FLAG = new Vector();
	Vector VDRCR_SAP_APPROVAL_FLAG = new Vector();
	Vector VDRCR_PDF_TYPE = new Vector();
	Vector VDRCR_PDF_FILE_NAME = new Vector();
	Vector VDRCR_PDF_FILE_PATH = new Vector();
	Vector VDRCR_PDF_SIGNED_FLAG = new Vector();
	Vector VDRCR_SIGN_PDF_TYPE = new Vector();
	Vector VDRCR_ITEM_DESC = new Vector();
	Vector VDRCR_FINANCIAL_YEAR = new Vector();
	Vector VDRCR_AGMT_REV_NO = new Vector();
	Vector VDRCR_CONT_REV_NO = new Vector();
	Vector VDRCR_INVOICE_EXIST = new Vector();
	Vector VDRCR_IS_IRN_GENERATED = new Vector();
	Vector VDRCR_NO = new Vector();
	Vector VDRCR_CRITERIA = new Vector();
	//Vector Disable_citeria=new Vector();
	Vector VINVOICE_CURR = new Vector();
	Vector VDRCR_REMARK = new Vector();
	Vector DRCR_DUE_DT = new Vector();
	Vector DRCR_DT = new Vector();
	Vector DRCR_PERIOD_START_DT = new Vector();
	Vector DRCR_PERIOD_END_DT = new Vector();
	Vector DRCR_INVOICE_NO = new Vector();
	Vector VDRCR_SEQ = new Vector();
	Vector VDRCR_REF = new Vector();
	Vector VDRCR_FLAG = new Vector();
	Vector VDEAL_CONT_REF_NO = new Vector();
	Vector VDRCR1_SEQ = new Vector();
	
	//HashMap enable_criteria=new HashMap();
	
	Vector VDRCR_LIST_ABBR=new Vector();
	Vector VDRCR_LIST_NM=new Vector();
	Vector VDRCR_SYS_INV_NO=new Vector();
	Vector VHEADER_DISPLAY_SEGMENT_TYP = new Vector();
	Vector VHEADER_DISPLAY_SEGMENT = new Vector();
	Vector VSEGMENT_INDEX = new Vector();
	
	public Vector getVDRCR_LIST_ABBR() {return VDRCR_LIST_ABBR;}
	public Vector getVDRCR_LIST_NM() {return VDRCR_LIST_NM;}
	public Vector getVDRCR_SYS_INV_NO() {return VDRCR_SYS_INV_NO;}
	public Vector getVHEADER_DISPLAY_SEGMENT_TYP() {return VHEADER_DISPLAY_SEGMENT_TYP;}
	public Vector getVHEADER_DISPLAY_SEGMENT() {return VHEADER_DISPLAY_SEGMENT;}
	public Vector getVSEGMENT_INDEX() {return VSEGMENT_INDEX;}
	
	

	//For Debit/Credit Note   //Deep20250818
	public Vector getVDRCR_BU_STATE_TIN() {
		return VDRCR_BU_STATE_TIN;
	}

	public Vector getVDRCR_AGMT_NO() {
		return VDRCR_AGMT_NO;
	}

	public Vector getVDRCR_CONT_NO() {
		return VDRCR_CONT_NO;
	}

	public Vector getVDRCR_CARGO_NO() {
		return VDRCR_CARGO_NO;
	}

	public Vector getVDRCR_START_DT() {
		return VDRCR_START_DT;
	}

	public Vector getVDRCR_END_DT() {
		return VDRCR_END_DT;
	}

	public Vector getVDRCR_CONT_REF_NO() {
		return VDRCR_CONT_REF_NO;
	}

	public Vector getVDRCR_CONTRACT_TYPE() {
		return VDRCR_CONTRACT_TYPE;
	}

	public Vector getVDRCR_CONT_NAME() {
		return VDRCR_CONT_NAME;
	}

	public Vector getVDRCR_DEAL_NO() {
		return VDRCR_DEAL_NO;
	}

	public Vector getVDRCR_PLANT_SEQ() {
		return VDRCR_PLANT_SEQ;
	}

	public Vector getVDRCR_PLANT_ABBR() {
		return VDRCR_PLANT_ABBR;
	}

	public Vector getVDRCR_BU_PLANT_SEQ() {
		return VDRCR_BU_PLANT_SEQ;
	}

	public Vector getVDRCR_BU_PLANT_ABBR() {
		return VDRCR_BU_PLANT_ABBR;
	}

	public Vector getVDRCR_PERIOD_START_DT() {
		return VDRCR_PERIOD_START_DT;
	}

	public Vector getVDRCR_PERIOD_END_DT() {
		return VDRCR_PERIOD_END_DT;
	}

	public Vector getVDRCR_TEMP_PERIOD_START_DT() {
		return VDRCR_TEMP_PERIOD_START_DT;
	}

	public Vector getVDRCR_TEMP_PERIOD_END_DT() {
		return VDRCR_TEMP_PERIOD_END_DT;
	}
	
	
	public Vector getVDRCR_INVOICE_DT() {
		return VDRCR_INVOICE_DT;
	}

	public Vector getVDRCR_DUE_DT() {
		return VDRCR_DUE_DT;
	}

	public Vector getVDRCR_AGMT_BASE() {
		return VDRCR_AGMT_BASE;
	}

	public Vector getVDRCR_STATUS() {
		return VDRCR_STATUS;
	}

	public Vector getVDRCR_BILLING_FREQ_FLAG() {
		return VDRCR_BILLING_FREQ_FLAG;
	}

	public Vector getVDRCR_BILLING_FREQ_NM() {
		return VDRCR_BILLING_FREQ_NM;
	}

	public Vector getVDRCR_DIFF_COLOR() {
		return VDRCR_DIFF_COLOR;
	}
	
	public Vector getVDRCR_ALLOC_QTY() {
		return VDRCR_ALLOC_QTY;
	}

	public Vector getVDRCR_INV_FLAG() {
		return VDRCR_INV_FLAG;
	}

	public Vector getVDRCR_INVOICE_NO() {
		return VDRCR_INVOICE_NO;
	}

	public Vector getVDRCR_INVOICE_SEQ() {
		return VDRCR_INVOICE_SEQ;
	}

	public Vector getVDRCR_INV_APPROVED_FLAG() {
		return VDRCR_INV_APPROVED_FLAG;
	}

	public Vector getVDRCR_INV_CHECKED_FLAG() {
		return VDRCR_INV_CHECKED_FLAG;
	}

	public Vector getVDRCR_PDF_INV_FLAG() {
		return VDRCR_PDF_INV_FLAG;
	}

	public Vector getVDRCR_SAP_APPROVAL_FLAG() {
		return VDRCR_SAP_APPROVAL_FLAG;
	}

	public Vector getVDRCR_PDF_TYPE() {
		return VDRCR_PDF_TYPE;
	}

	public Vector getVDRCR_PDF_FILE_NAME() {
		return VDRCR_PDF_FILE_NAME;
	}

	public Vector getVDRCR_PDF_FILE_PATH() {
		return VDRCR_PDF_FILE_PATH;
	}

	public Vector getVDRCR_PDF_SIGNED_FLAG() {
		return VDRCR_PDF_SIGNED_FLAG;
	}

	public Vector getVDRCR_SIGN_PDF_TYPE() {
		return VDRCR_SIGN_PDF_TYPE;
	}

	public Vector getVDRCR_ITEM_DESC() {
		return VDRCR_ITEM_DESC;
	}

	public Vector getVDRCR_FINANCIAL_YEAR() {
		return VDRCR_FINANCIAL_YEAR;
	}

	public Vector getVDRCR_AGMT_REV_NO() {
		return VDRCR_AGMT_REV_NO;
	}

	public Vector getVDRCR_CONT_REV_NO() {
		return VDRCR_CONT_REV_NO;
	}

	public Vector getVDRCR_INVOICE_EXIST() {
		return VDRCR_INVOICE_EXIST;
	}

	public Vector getVDRCR_IS_IRN_GENERATED() {
		return VDRCR_IS_IRN_GENERATED;
	}
	public Vector getVDRCR_NO() {
		return VDRCR_NO;
	}
	
	public Vector getDRCR_DUE_DT() {
		return DRCR_DUE_DT;
	}

	public Vector getDRCR_DT() {
		return DRCR_DT;
	}

	public Vector getDRCR_PERIOD_START_DT() {
		return DRCR_PERIOD_START_DT;
	}

	public Vector getDRCR_PERIOD_END_DT() {
		return DRCR_PERIOD_END_DT;
	}

	public Vector getDRCR_INVOICE_NO() {
		return DRCR_INVOICE_NO;
	}
	
//	public Vector getVINVOICE_CURR() {
//		return VINVOICE_CURR;
//	}
	
	public Vector getVDRCR_SEQ() {
		return VDRCR_SEQ;
	}
	
	public Vector getVDRCR_FLAG() {
		return VDRCR_FLAG;
	}
	
	public Vector getVDRCR_REF() {
		return VDRCR_REF;
	}
	
	public Vector getVDRCR_CRITERIA() {
		return VDRCR_CRITERIA;
	}
	
	public Vector getVDRCR_REMARK() {
		return VDRCR_REMARK;
	}
	
	public Vector getVDEAL_CONT_REF_NO() {
		return VDEAL_CONT_REF_NO;
	}
	
	public Vector getVDRCR1_SEQ() {
		return VDRCR1_SEQ;
	}
	
	
	public Vector getVINVOICE_ID_SEQ() {return VINVOICE_ID_SEQ;}
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVCOUNTERPTY_CD() {return VCOUNTERPTY_CD;}
	public Vector getVCOUNTERPTY_ABBR() {return VCOUNTERPTY_ABBR;}
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
	
//	public Vector getVSAP_APPROVAL_FLAG() {return VSAP_APPROVAL_FLAG;}
//	public Vector getVOTH_SAP_APPROVAL_FLAG() {return VOTH_SAP_APPROVAL_FLAG;}
//	public Vector getVFILTER_CONT_TYPE() {return VFILTER_CONT_TYPE;}
//	public Vector getVFILTER_CONT_NAME() {return VFILTER_CONT_NAME;}
	
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
	
	public Vector getVSTORAGE_DATE() {return VSTORAGE_DATE;}
	public Vector getVSTORAGE_INVENTORY() {return VSTORAGE_INVENTORY;}
	public Vector getVOFFTAKE_QTY() {return VOFFTAKE_QTY;}
	public Vector getVSTORAGE_CHARGE() {return VSTORAGE_CHARGE;}
	public Vector getVSTORAGE_AMT() {return VSTORAGE_AMT;}
	public Vector getVUSER_DEFINE() {return VUSER_DEFINE;}
	public Vector getVRATE_TYPE() {return VRATE_TYPE;}
	public Vector getVDISCOUNT_FLAG() {return VDISCOUNT_FLAG;}
	
	
	
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
	
	String from_dt ="";
	public void setFrom_dt(String from_dt) {this.from_dt = from_dt;}
	String to_dt ="";
	public void setTo_dt(String to_dt) {this.to_dt = to_dt;}
	
	//Deep20250901
	public String getdrcr_invoice_no() {return drcr_invoice_no;}
	public String getdrcr_seq() {return drcr_seq;}
	public String getdrcr_ref() {return drcr_ref;}
	public String getcriteria() {return criteria;}
	public String getdrcr_flag() {return drcr_flag;}
	public String getdrcr_dt() {return drcr_dt;}
	public String getdrcr_due_dt() {return drcr_due_dt;}
	public String getdrcr_alloc_qty() {return drcr_alloc_qty;}
	public String getdrcr_gross_amt1() {return drcr_gross_amt1;}
	public String getdrcr_price() {return drcr_price;}
	public String getdrcr_tax_amt() {return drcr_tax_amt;}
	public String getdrcr_invoice_amt() {return drcr_invoice_amt;}
	public String getdrcr_net_payable() {return drcr_net_payable;}
	public String getdrcr_exchng() {return drcr_exchng;}
	//Deep20250901
	
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
	
	public String getTotal_exchng_label() {return total_exchng_label;}
	public String getTotal_exchng_label_rate() {return total_exchng_label_rate;}
	public String getSource() {return source;}
	
	public String getInv_entered_on() {return inv_entered_on;}
	public String getInv_approved_on() {return inv_approved_on;}
	
	
	//Deep260825
	public String getDrcr_Inv_entered_on() {return drcr_inv_entered_on;}
	public String getdrcr_inv_approved_on() {return drcr_inv_approved_on;}
	public String getCriteria_desc() {return criteria_desc;}

	
	public String getSub_inv_type() {return sub_inv_type;}
	public String getPlant_gstin_no() {return plant_gstin_no;}
	public String getBu_gstin_no() {return bu_gstin_no;}
	public String getSug_percentage() {return sug_percentage;}
	public String getSug_qty() {return sug_qty;}
	public String getDiscount_days() {return discount_days;}
	public String getStorage_start_dt() {return storage_start_dt;}
	public String getStorage_end_dt() {return storage_end_dt;}
	public String getArrivalDt() {return arrivalDt;}
	
	boolean submission_chk = false;
	boolean correction_needed = false;
	double mdcq_percentage=100;
	boolean isGrossIncTriff = false;
	
	public boolean getSubmission_chk() {return submission_chk;}
	public boolean getCorrection_needed() {return correction_needed;}
	public boolean getIsGrossIncTriff() {return isGrossIncTriff;}
	
	public void setSegment(String segment) {this.segment = segment;}	
	

}
