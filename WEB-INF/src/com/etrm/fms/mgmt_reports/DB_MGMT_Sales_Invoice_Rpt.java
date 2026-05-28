package com.etrm.fms.mgmt_reports;

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

public class DB_MGMT_Sales_Invoice_Rpt
{
String db_src_file_name="DB_MGMT_Sales_Invoice_Rpt.java";
	
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

	String queryString="";
	String queryString0="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";
	String queryString6="";
	String queryString7="";
	String gas_dt="";
	
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
	    			if(callFlag.equalsIgnoreCase("INVOICE_DEVIATION"))
	    			{
	    				getInvoiceDeviation();
	    				getCounterpartyList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("ALLOCATION_TO_CUSTOMER_PLANT"))
	    			{
	    				getCustomerMasterForAllocToCust();
	    				getContractList();
	    				getDealMapping();
	    				getPlantName();
	    				getBUList();
	    				getAllocToCustomerData();
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
	
	private void getCounterpartyList() 
	{
		String function_nm = "getCounterpartyList()";
		try 
		{
			String end_dt="";
			Vector FIRST = new Vector();
			Vector FIRST_WEEK = new Vector();
			Vector SECOND_WEEK = new Vector();
			Vector THIRD_WEEK = new Vector();
			Vector VDUE_FLG = new Vector();
			Vector VBILLING_FLAG = new Vector();
			Vector VEXCHNG_FLAG = new Vector();
			Vector VCOUNTERPTY_CD = new Vector();
			Vector VAGMT_NO = new Vector();
			Vector VCONT_NO = new Vector();
			Vector VINVOICE_DT = new Vector();
			Vector VINVOICE_NO = new Vector();
			Vector VCONTRACT_TYPE = new Vector();
			Vector VPERIOD_START_DT = new Vector();
			Vector VPLANT_SEQ = new Vector();
			Vector VOTH_PERIOD_END_DT = new Vector();
			Vector VEXCHG_RATE_DT = new Vector();
			Vector VINVOICE_DUE_DT = new Vector();
			Vector VPERIOD_END_DT = new Vector();
			Vector VEND_DT = new Vector();
			Vector VCOUNTERPTY_ABBR = new Vector();
			Vector VCOUNTERPTY_NM = new Vector();
			  queryString = "SELECT DISTINCT(COUNTERPARTY_CD), COMPANY_CD,AGMT_NO,CONT_NO,CONTRACT_TYPE,INVOICE_NO,"//6
			 		+ "TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),FREQ,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),"
			 		+ "TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'), "
			 		+ "TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT-1,'DD/MM/YYYY'),PLANT_SEQ "
			 		+ "FROM FMS_INVOICE_MST WHERE INVOICE_DT BETWEEN "
			 		+ "TO_DATE(?, 'DD/MM/YYYY') AND TO_DATE(?, 'DD/MM/YYYY')"
			 		+ "AND CONTRACT_TYPE IN ('S','L','X','Q','O') AND COMPANY_CD = ? ";
			 stmt = conn.prepareStatement(queryString);
			 stmt.setString(1, from_dt);
			 stmt.setString(2, to_dt);
			 stmt.setString(3, comp_cd);
			 
			 rset = stmt.executeQuery();
			 while(rset.next())
			 {
				 VCOUNTERPTY_CD.add(rset.getString(1)==null?"":rset.getString(1));
				 VAGMT_NO.add(rset.getString(3)==null?"":rset.getString(3));
				 VCONT_NO.add(rset.getString(4)==null?"":rset.getString(4));
				 VCONTRACT_TYPE.add(rset.getString(5)==null?"":rset.getString(5));
//				 VINVOICE_NO.add(rset.getString(6)==null?"":rset.getString(6));
				 VINVOICE_DT.add(rset.getString(7)==null?"":rset.getString(7));
				 VPERIOD_END_DT.add(rset.getString(10)==null?"":rset.getString(10));
				 end_dt = rset.getString(10)==null?"":rset.getString(10);
				 VINVOICE_DUE_DT.add(rset.getString(11)==null?"":rset.getString(11));
				 VEXCHG_RATE_DT.add(rset.getString(12)==null?"":rset.getString(12));
				 VOTH_PERIOD_END_DT.add(rset.getString(13)==null?"":rset.getString(13));
				 VPLANT_SEQ.add(rset.getString(14)==null?"":rset.getString(14));
				 VPERIOD_START_DT.add(rset.getString(9)==null?"":rset.getString(9));
				 queryString1="SELECT TO_CHAR(LAST_DAY(TO_DATE(?,'DD/MM/YYYY')),'DD/MM/YYYY') from DUAL";
				 stmt1 = conn.prepareStatement(queryString1);
				 stmt1.setString(1, end_dt);
				 rset1 = stmt1.executeQuery();
				 if(rset1.next())
				 {
				    VEND_DT.add(rset1.getString(1)==null?"":rset1.getString(1));
				    String end_days = rset1.getString(1)==null?"":rset1.getString(1);
				    FIRST.add("15"+end_days.substring(2));
					FIRST_WEEK.add("07"+end_days.substring(2));
					SECOND_WEEK.add("14"+end_days.substring(2));
					THIRD_WEEK.add("21"+end_days.substring(2)); 
				 }
				 else
				 {
				    VEND_DT.add("");
					FIRST.add("");
					FIRST_WEEK.add("");
					SECOND_WEEK.add("");
					THIRD_WEEK.add(""); 
				 }
				 stmt1.close();
				 rset1.close();
			 }
			 rset.close();
			 stmt.close();
			 
			 
			 for(int i=0;i<VCOUNTERPTY_CD.size();i++)
			 {
				  queryString1 = "SELECT DISTINCT(A.COUNTERPARTY_NM),A.COUNTERPARTY_ABBR "
			    			+ "FROM FMS_COUNTERPARTY_MST A "
			    			+ "WHERE A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_MST B WHERE B.COUNTERPARTY_CD=A.COUNTERPARTY_CD) AND "
			    			+ "A.COUNTERPARTY_CD = ? ORDER BY A.COUNTERPARTY_NM ";
			    	stmt1 = conn.prepareStatement(queryString1);
			    	stmt1.setString(1, ""+VCOUNTERPTY_CD.elementAt(i));
			    	rset1 = stmt1.executeQuery();
			    	if(rset1.next())
			    	{
			    		VCOUNTERPTY_NM.add(rset1.getString(1)==null?"":rset1.getString(1));
			    		VCOUNTERPTY_ABBR.add(rset1.getString(2)==null?"":rset1.getString(2));
			    	}
			    	else
			    	{
			    		VCOUNTERPTY_NM.add("");
			    		VCOUNTERPTY_ABBR.add("");
			    	}
			    	stmt1.close();
					rset1.close();
					
					queryString="SELECT DISTINCT(COUNTERPARTY_CD), BILLING_FREQ,DUE_DATE,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,"
							+ "EXCL_SAT_MAP,BILLING_DAYS,SECOND_DUE_DT,HOLIDAY_STATE "
							+ "FROM FMS_SUPPLY_BILLING_DTL "
							+ "WHERE COUNTERPARTY_CD = ? AND AGMT_NO = ? AND CONT_NO = ? "
							+ "AND PLANT_SEQ_NO = ? AND CONTRACT_TYPE = ? AND COMPANY_CD = ? ";
					queryString+=" UNION ";
					queryString+="SELECT DISTINCT(COUNTERPARTY_CD),BILLING_FREQ,DUE_DATE,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,"
							+ "EXCL_SAT_MAP,BILLING_DAYS,SECOND_DUE_DT,HOLIDAY_STATE "
							+ "FROM FMS_LTCORA_CONT_BILLING_DTL WHERE COUNTERPARTY_CD = ? AND AGMT_NO = ? AND CONT_NO = ? "
							+ "AND PLANT_SEQ_NO = ? AND CONTRACT_TYPE = ? AND COMPANY_CD = ? ";
					
					 stmt = conn.prepareStatement(queryString);
					 stmt.setString(1, ""+VCOUNTERPTY_CD.elementAt(i));
					 stmt.setString(2, ""+VAGMT_NO.elementAt(i));
					 stmt.setString(3, ""+VCONT_NO.elementAt(i));
					 stmt.setString(4, ""+VPLANT_SEQ.elementAt(i));
					 stmt.setString(5, ""+VCONTRACT_TYPE.elementAt(i));
					 stmt.setString(6, comp_cd);
					 stmt.setString(7, ""+VCOUNTERPTY_CD.elementAt(i));
					 stmt.setString(8, ""+VAGMT_NO.elementAt(i));
					 stmt.setString(9, ""+VCONT_NO.elementAt(i));
					 stmt.setString(10, ""+VPLANT_SEQ.elementAt(i));
					 stmt.setString(11, ""+VCONTRACT_TYPE.elementAt(i));
					 stmt.setString(12, comp_cd);
					 rset = stmt.executeQuery();
					 if(rset.next())
					 {
						 String due_date = "";
						 String sec_due_date = "";
						 String bill_freq = rset.getString(2)==null?"":rset.getString(2);
						 String days = rset.getString(3)==null?"0":rset.getString(3);
						 String exchng_rt_cal = rset.getString(4)==null?"":rset.getString(4);
						 String exchng_rt_crt = rset.getString(5)==null?"":rset.getString(5);
						 String exclude_sat = rset.getString(6)==null?"":rset.getString(6);
						 String due_days = rset.getString(8)==null?"0":rset.getString(8);
						 String bill_days = rset.getString(7)==null?"0":rset.getString(7);
						 String state = rset.getString(9)==null?"":rset.getString(9);
						 int sat_length1 = 0;
						 if(exclude_sat.contains("-"))
						 {
							 sat_length1 = exclude_sat.split("-").length;
						 }
						 else if(exclude_sat.equals(""))
						 {
							 sat_length1 = 0;
						 }
						 else if(exclude_sat.equals("") && (!exclude_sat.contains("-")))
						 {
							 sat_length1 = 1;
						 }
						 String element = ""+VPERIOD_START_DT.elementAt(i);
						 String[] parts = element.split("/");
						 String year = parts[2];
						 String mnth = parts[1];

						 int hold_cnt=0;
						 queryString1 = "SELECT COUNT(*) "
						 		+ "FROM FMS_HOLIDAY_DTL "
						 		+ "WHERE STATE_TIN = ? AND FLAG ='Y' "
						 		+ "AND HOLIDAY_DT BETWEEN TO_DATE(?,'DD/MM/YYYY') "
						 		+ "AND TO_DATE(?,'DD/MM/YYYY') ";
						 stmt1 = conn.prepareStatement(queryString1);
						 stmt1.setString(1, state);
						 stmt1.setString(2, ""+VPERIOD_START_DT.elementAt(i));
						 stmt1.setString(3, ""+VPERIOD_START_DT.elementAt(i));
						 rset1 = stmt1.executeQuery();
						 if(rset1.next())
						 {
							 hold_cnt = rset1.getInt(1); 
						 }
						 else
						 {
							 hold_cnt =0; 
						 }
						 rset1.close();
						 stmt1.close();
						 
						 int total = Integer.parseInt(days) + sat_length1+hold_cnt;
						 int total1 = Integer.parseInt(due_days)+total+hold_cnt+sat_length1;
						 
						 
						 queryString1 = "SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY')+"+total+",'DD/MM/YYYY') FROM DUAL ";
						 stmt1 = conn.prepareStatement(queryString1);
						 stmt1.setString(1, ""+VINVOICE_DT.elementAt(i));
						 rset1 = stmt1.executeQuery();
						 if(rset1.next())
						 {
							 due_date = rset1.getString(1)==null?"":rset1.getString(1);
						 }
						 stmt1.close();
						 rset1.close();
						 
						 queryString1 = "SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY')+"+total1+",'DD/MM/YYYY') FROM DUAL ";
						 stmt1 = conn.prepareStatement(queryString1);
						 stmt1.setString(1, ""+VINVOICE_DT.elementAt(i));
						 rset1 = stmt1.executeQuery();
						 if(rset1.next())
						 {
							 sec_due_date = rset1.getString(1)==null?"":rset1.getString(1);
						 }
						 stmt1.close();
						 rset1.close();
						 //for due_date deviation
						 if((due_date.equals(""+VINVOICE_DUE_DT.elementAt(i))) || (sec_due_date.equals(""+VINVOICE_DUE_DT.elementAt(i))))
						 {
							 VDUE_FLG.add("N");
						 }
						 else
						 {
							 VDUE_FLG.add("Y");
						 }
						 
						 
						 //for exchng_rate_dt deviation
						 if(exchng_rt_cal.equalsIgnoreCase("D"))
						 {
							 if(exchng_rt_crt.equalsIgnoreCase("INV"))
							 {
								 if(((""+VINVOICE_DT.elementAt(i)).equals((""+VEXCHG_RATE_DT.elementAt(i)))))
								 {
									 VEXCHNG_FLAG.add("N");
								 }
								 else
								 {
									 VEXCHNG_FLAG.add("Y");
								 }
							 }
							 else if(exchng_rt_crt.equalsIgnoreCase("PRE"))
							 {
								 if(((""+VOTH_PERIOD_END_DT.elementAt(i)).equals((""+VEXCHG_RATE_DT.elementAt(i)))))
								 {
									 VEXCHNG_FLAG.add("N");
								 }
								 else
								 {
									 VEXCHNG_FLAG.add("Y");
								 }
							 }
							 else if(exchng_rt_crt.equalsIgnoreCase("LST"))
							 {
								 if(((""+VPERIOD_END_DT.elementAt(i)).equals((""+VEXCHG_RATE_DT.elementAt(i)))))
								 {
									 VEXCHNG_FLAG.add("N");
								 }
								 else
								 {
									 VEXCHNG_FLAG.add("Y");
								 }
							 }
							 else
							 {
								 VEXCHNG_FLAG.add("N");
							 }
						 }
						 else
						 {
							 VEXCHNG_FLAG.add("N");
						 }
						 
						 
						 //billin_frequency date deviation
						 if(bill_freq.equalsIgnoreCase("F"))
						 {
							 if((""+VEND_DT.elementAt(i)).equals((""+VPERIOD_END_DT.elementAt(i))) || 
							   (""+FIRST.elementAt(i)).equals((""+VPERIOD_END_DT.elementAt(i))) )
							 {
								 VBILLING_FLAG.add("N");
							 }
							 else
							 {
								 VBILLING_FLAG.add("Y");
							 }
						 }
						 else if(bill_freq.equalsIgnoreCase("M"))
						 {
							 if((""+VEND_DT.elementAt(i)).equals((""+VPERIOD_END_DT.elementAt(i))))
							 {
								 VBILLING_FLAG.add("N");
							 }
							 else
							 {
								 VBILLING_FLAG.add("Y");
							 }
						 }
						 else if(bill_freq.equalsIgnoreCase("W"))
						 {
							 if((""+VEND_DT.elementAt(i)).equals((""+VPERIOD_END_DT.elementAt(i))) || 
							   (""+FIRST_WEEK.elementAt(i)).equals((""+VPERIOD_END_DT.elementAt(i))) ||
							   (""+SECOND_WEEK.elementAt(i)).equals((""+VPERIOD_END_DT.elementAt(i))) || 
							   (""+THIRD_WEEK.elementAt(i)).equals((""+VPERIOD_END_DT.elementAt(i))))
							 {
								 VBILLING_FLAG.add("N");
							 }
							 else
							 {
								 VBILLING_FLAG.add("Y");
							 }
						 }
						 else if(bill_freq.equalsIgnoreCase("O"))
						 {
							 String other_date = "";
							 queryString1 = "SELECT TO_CHAR(dt, 'DD/MM/YYYY') as dt "
							 		+ "FROM (SELECT TO_DATE(?, 'DD/MM/YYYY') + (? - 1) + (LEVEL - 1) * ? AS dt "
							 		+ "FROM dual CONNECT BY LEVEL <= 1) "
							 		+ "WHERE EXTRACT(MONTH FROM dt) = ? "
							 		+ "AND EXTRACT(YEAR FROM dt) = ? "
							 		+ "ORDER BY dt ";
							 stmt1 = conn.prepareStatement(queryString1);
							 stmt1.setString(1,""+VPERIOD_START_DT.elementAt(i) );
							 stmt1.setString(2, bill_days);
							 stmt1.setString(3, bill_days);
							 stmt1.setString(4, mnth);
							 stmt1.setString(5, year);
							 rset1 = stmt1.executeQuery();
							 if(rset1.next())
							 {
								 other_date = rset1.getString(1)==null?"":rset1.getString(1);
							 }
							 rset1.close();
							 stmt1.close();
							 
							 if(other_date.equals(""+VPERIOD_END_DT.elementAt(i)))
							 {
								 VBILLING_FLAG.add("N");
							 }
							 else
							 {
								 VBILLING_FLAG.add("Y");
							 }
						 }
						 else
						 {
							 VBILLING_FLAG.add("N");
						 }
					 }
					 else
					 {
						    VDUE_FLG.add("N");
							VEXCHNG_FLAG.add("N");
							VBILLING_FLAG.add("N");
							
					 }
					 String dueFlag     = "" + VDUE_FLG.elementAt(i);
					 String exchFlag    = "" + VEXCHNG_FLAG.elementAt(i);
					 String billFreqFlag= "" + VBILLING_FLAG.elementAt(i);

					 if(dueFlag.equalsIgnoreCase("Y") ||
					    exchFlag.equalsIgnoreCase("Y") ||
					    billFreqFlag.equalsIgnoreCase("Y"))
					 {
						 VCOUNTERPARTY_CD.add(rset.getString(1)==null?"":rset.getString(1));  
					 
					 queryString1 = "SELECT DISTINCT A.COUNTERPARTY_NM,A.COUNTERPARTY_ABBR "
				    			+ "FROM FMS_COUNTERPARTY_MST A "
				    			+ "WHERE A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_MST B WHERE B.COUNTERPARTY_CD=A.COUNTERPARTY_CD) AND "
				    			+ "A.COUNTERPARTY_CD = ? ORDER BY A.COUNTERPARTY_NM ";
				    	stmt1 = conn.prepareStatement(queryString1);
				    	stmt1.setString(1, ""+VCOUNTERPARTY_CD.elementAt(i));
				    	rset1 = stmt1.executeQuery();
				    	if(rset1.next())
				    	{
				    		VCOUNTERPARTY_NM.add(rset1.getString(1)==null?"":rset1.getString(1));
				    		VCOUNTERPARTY_ABBR.add(rset1.getString(2)==null?"":rset1.getString(2));
				    	}
				    	stmt1.close();
						rset1.close();
					 }
					 else
					 {
						 VCOUNTERPARTY_CD.add(""); 
						 VCOUNTERPARTY_NM.add("");
				    	 VCOUNTERPARTY_ABBR.add("");
					 }
					 stmt.close();
					 rset.close();
                     
				 }
			 Set<String> cd = new LinkedHashSet<>(VCOUNTERPARTY_CD);  
			 VCOUNTERPARTY_CD.clear();
			 VCOUNTERPARTY_CD.addAll(cd);
			 
			 Set<String> name = new LinkedHashSet<>(VCOUNTERPARTY_NM);  
			 VCOUNTERPARTY_NM.clear();
			 VCOUNTERPARTY_NM.addAll(name);
			 
			 Set<String> abbr = new LinkedHashSet<>(VCOUNTERPARTY_ABBR);  
			 VCOUNTERPARTY_ABBR.clear();
			 VCOUNTERPARTY_ABBR.addAll(abbr);
		}
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getInvoiceDeviation()
	{
		String function_nm = "getInvoiceDeviation()";
		String nm="";
		try
		{
			String end_dt="";
			Vector FIRST_FORTNIGHT = new Vector();
			Vector FIRST_WEEK_DAYS = new Vector();
			Vector SECOND_WEEK_DAYS = new Vector();
			Vector THIRD_WEEK_DAYS = new Vector();
			
			  queryString = "SELECT DISTINCT(COUNTERPARTY_CD), COMPANY_CD,AGMT_NO,CONT_NO,CONTRACT_TYPE,INVOICE_NO,"//6
			 		+ "TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),FREQ,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),"
			 		+ "TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'), "
			 		+ "TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT-1,'DD/MM/YYYY'),PLANT_SEQ "
			 		+ "FROM FMS_INVOICE_MST WHERE INVOICE_DT BETWEEN "
			 		+ "TO_DATE(?, 'DD/MM/YYYY') AND TO_DATE(?, 'DD/MM/YYYY')"
			 		+ "AND CONTRACT_TYPE IN ('S','L','X','Q','O') AND COMPANY_CD = ? ";
			  if (!counterparty_cd.equals("") && !counterparty_cd.equals("0")) 
				{
					queryString += "AND COUNTERPARTY_CD=? ";
				}
			 stmt = conn.prepareStatement(queryString);
			 stmt.setString(1, from_dt);
			 stmt.setString(2, to_dt);
			 stmt.setString(3, comp_cd);
			 if (!counterparty_cd.equals("") && !counterparty_cd.equals("0")) 
			 {
				 stmt.setString(4, counterparty_cd);
			 }
			 
			 rset = stmt.executeQuery();
			 while(rset.next())
			 {
				 VCOUNTERPTY_CD.add(rset.getString(1)==null?"":rset.getString(1));
				 VAGMT_NO.add(rset.getString(3)==null?"":rset.getString(3));
				 VCONT_NO.add(rset.getString(4)==null?"":rset.getString(4));
				 VCONTRACT_TYPE.add(rset.getString(5)==null?"":rset.getString(5));
				 VINVOICE_NO.add(rset.getString(6)==null?"":rset.getString(6));
				 VINVOICE_DT.add(rset.getString(7)==null?"":rset.getString(7));
				 VPERIOD_END_DT.add(rset.getString(10)==null?"":rset.getString(10));
				 end_dt = rset.getString(10)==null?"":rset.getString(10);
				 VINVOICE_DUE_DT.add(rset.getString(11)==null?"":rset.getString(11));
				 VEXCHG_RATE_DT.add(rset.getString(12)==null?"":rset.getString(12));
				 VOTH_PERIOD_END_DT.add(rset.getString(13)==null?"":rset.getString(13));
				 VPLANT_SEQ.add(rset.getString(14)==null?"":rset.getString(14));
				 VPERIOD_START_DT.add(rset.getString(9)==null?"":rset.getString(9));
				 queryString1="SELECT TO_CHAR(LAST_DAY(TO_DATE(?,'DD/MM/YYYY')),'DD/MM/YYYY') from DUAL";
				 stmt1 = conn.prepareStatement(queryString1);
				 stmt1.setString(1, end_dt);
				 rset1 = stmt1.executeQuery();
				 if(rset1.next())
				 {
				    VEND_DT.add(rset1.getString(1)==null?"":rset1.getString(1));
				    String end_days = rset1.getString(1)==null?"":rset1.getString(1);
				    FIRST_FORTNIGHT.add("15"+end_days.substring(2));
					FIRST_WEEK_DAYS.add("07"+end_days.substring(2));
					SECOND_WEEK_DAYS.add("14"+end_days.substring(2));
					THIRD_WEEK_DAYS.add("21"+end_days.substring(2)); 
				 }
				 else
				 {
				    VEND_DT.add("");
					FIRST_FORTNIGHT.add("");
					FIRST_WEEK_DAYS.add("");
					SECOND_WEEK_DAYS.add("");
					THIRD_WEEK_DAYS.add(""); 
				 }
				 stmt1.close();
				 rset1.close();
			 }
			 rset.close();
			 stmt.close();
			 
			 for(int i=0;i<VCOUNTERPTY_CD.size();i++)
			 {
				  queryString1 = "SELECT DISTINCT(A.COUNTERPARTY_NM),A.COUNTERPARTY_ABBR "
			    			+ "FROM FMS_COUNTERPARTY_MST A "
			    			+ "WHERE A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_MST B WHERE B.COUNTERPARTY_CD=A.COUNTERPARTY_CD) AND "
			    			+ "A.COUNTERPARTY_CD = ? ORDER BY A.COUNTERPARTY_NM ";
			    	stmt1 = conn.prepareStatement(queryString1);
			    	stmt1.setString(1, ""+VCOUNTERPTY_CD.elementAt(i));
			    	rset1 = stmt1.executeQuery();
			    	if(rset1.next())
			    	{
			    		VCOUNTERPTY_NM.add(rset1.getString(1)==null?"":rset1.getString(1));
			    		VCOUNTERPTY_ABBR.add(rset1.getString(2)==null?"":rset1.getString(2));
			    	}
			    	else
			    	{
			    		VCOUNTERPTY_NM.add("");
			    		VCOUNTERPTY_ABBR.add("");
			    	}
			    	stmt1.close();
					rset1.close();
					
					queryString="SELECT DISTINCT(COUNTERPARTY_CD), BILLING_FREQ,DUE_DATE,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,"
							+ "EXCL_SAT_MAP,BILLING_DAYS,SECOND_DUE_DT,HOLIDAY_STATE "
							+ "FROM FMS_SUPPLY_BILLING_DTL "
							+ "WHERE COUNTERPARTY_CD = ? AND AGMT_NO = ? AND CONT_NO = ? "
							+ "AND PLANT_SEQ_NO = ? AND CONTRACT_TYPE = ? AND COMPANY_CD = ? ";
					queryString+=" UNION ";
					queryString+="SELECT DISTINCT(COUNTERPARTY_CD),BILLING_FREQ,DUE_DATE,EXCHNG_RATE_CAL,EXCHNG_CRITERIA,"
							+ "EXCL_SAT_MAP,BILLING_DAYS,SECOND_DUE_DT,HOLIDAY_STATE "
							+ "FROM FMS_LTCORA_CONT_BILLING_DTL WHERE COUNTERPARTY_CD = ? AND AGMT_NO = ? AND CONT_NO = ? "
							+ "AND PLANT_SEQ_NO = ? AND CONTRACT_TYPE = ? AND COMPANY_CD = ? ";
					if (!counterparty_cd.equals("") && !counterparty_cd.equals("0")) 
					{
						queryString += "AND COUNTERPARTY_CD=? ";
					}
					
					 stmt = conn.prepareStatement(queryString);
					 stmt.setString(1, ""+VCOUNTERPTY_CD.elementAt(i));
					 stmt.setString(2, ""+VAGMT_NO.elementAt(i));
					 stmt.setString(3, ""+VCONT_NO.elementAt(i));
					 stmt.setString(4, ""+VPLANT_SEQ.elementAt(i));
					 stmt.setString(5, ""+VCONTRACT_TYPE.elementAt(i));
					 stmt.setString(6, comp_cd);
					 stmt.setString(7, ""+VCOUNTERPTY_CD.elementAt(i));
					 stmt.setString(8, ""+VAGMT_NO.elementAt(i));
					 stmt.setString(9, ""+VCONT_NO.elementAt(i));
					 stmt.setString(10, ""+VPLANT_SEQ.elementAt(i));
					 stmt.setString(11, ""+VCONTRACT_TYPE.elementAt(i));
					 stmt.setString(12, comp_cd);
					 if (!counterparty_cd.equals("") && !counterparty_cd.equals("0")) 
					 {
						 stmt.setString(13, counterparty_cd);
					 }
					 rset = stmt.executeQuery();
					 if(rset.next())
					 {
						 String due_date = "";
						 String sec_due_date = "";
						 String bill_freq = rset.getString(2)==null?"":rset.getString(2);
						 String days = rset.getString(3)==null?"0":rset.getString(3);
						 String exchng_rt_cal = rset.getString(4)==null?"":rset.getString(4);
						 String exchng_rt_crt = rset.getString(5)==null?"":rset.getString(5);
						 String exclude_sat = rset.getString(6)==null?"":rset.getString(6);
						 String due_days = rset.getString(8)==null?"0":rset.getString(8);
						 String bill_days = rset.getString(7)==null?"0":rset.getString(7);
						 String state = rset.getString(9)==null?"":rset.getString(9);
						 int sat_length1 = 0;
						 if(exclude_sat.contains("-"))
						 {
							 sat_length1 = exclude_sat.split("-").length;
						 }
						 else if(exclude_sat.equals(""))
						 {
							 sat_length1 = 0;
						 }
						 else if(exclude_sat.equals("") && (!exclude_sat.contains("-")))
						 {
							 sat_length1 = 1;
						 }
						 String element = ""+VPERIOD_START_DT.elementAt(i);
						 String[] parts = element.split("/");
						 String year = parts[2];
						 String mnth = parts[1];

						 int hold_cnt=0;
						 queryString1 = "SELECT COUNT(*) "
						 		+ "FROM FMS_HOLIDAY_DTL "
						 		+ "WHERE STATE_TIN = ? AND FLAG ='Y' "
						 		+ "AND HOLIDAY_DT BETWEEN TO_DATE(?,'DD/MM/YYYY') "
						 		+ "AND TO_DATE(?,'DD/MM/YYYY') ";
						 stmt1 = conn.prepareStatement(queryString1);
						 stmt1.setString(1, state);
						 stmt1.setString(2, ""+VPERIOD_START_DT.elementAt(i));
						 stmt1.setString(3, ""+VPERIOD_START_DT.elementAt(i));
						 rset1 = stmt1.executeQuery();
						 if(rset1.next())
						 {
							 hold_cnt = rset1.getInt(1); 
						 }
						 else
						 {
							 hold_cnt =0; 
						 }
						 rset1.close();
						 stmt1.close();
						 
						 int total = Integer.parseInt(days) + sat_length1+hold_cnt;
						 int total1 = Integer.parseInt(due_days)+total+hold_cnt+sat_length1;
						 
						 
						 queryString1 = "SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY')+"+total+",'DD/MM/YYYY') FROM DUAL ";
						 stmt1 = conn.prepareStatement(queryString1);
						 stmt1.setString(1, ""+VINVOICE_DT.elementAt(i));
						 rset1 = stmt1.executeQuery();
						 if(rset1.next())
						 {
							 due_date = rset1.getString(1)==null?"":rset1.getString(1);
						 }
						 stmt1.close();
						 rset1.close();
						 
						 queryString1 = "SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY')+"+total1+",'DD/MM/YYYY') FROM DUAL ";
						 stmt1 = conn.prepareStatement(queryString1);
						 stmt1.setString(1, ""+VINVOICE_DT.elementAt(i));
						 rset1 = stmt1.executeQuery();
						 if(rset1.next())
						 {
							 sec_due_date = rset1.getString(1)==null?"":rset1.getString(1);
						 }
						 stmt1.close();
						 rset1.close();
						 //for due_date deviation
						 if((due_date.equals(""+VINVOICE_DUE_DT.elementAt(i))) || (sec_due_date.equals(""+VINVOICE_DUE_DT.elementAt(i))))
						 {
							 VDUE_DT_FLG.add("N");
							 VDUE_DT_REMARK.add("");
						 }
						 else
						 {
							 VDUE_DT_FLG.add("Y");
							 if(due_days.equals("0"))
							 {
							    VDUE_DT_REMARK.add("Invoice Due Date is "+VINVOICE_DUE_DT.elementAt(i)+" instead of Date: "+due_date);
							 }
							 else
							 {
								 VDUE_DT_REMARK.add("Invoice Due Date is "+VINVOICE_DUE_DT.elementAt(i)+" instead of Date: "+due_date+"or second date: "+sec_due_date); 
							 }
						 }
						 
						 
						 //for exchng_rate_dt deviation
						 if(exchng_rt_cal.equalsIgnoreCase("D"))
						 {
							 if(exchng_rt_crt.equalsIgnoreCase("INV"))
							 {
								 if(((""+VINVOICE_DT.elementAt(i)).equals((""+VEXCHG_RATE_DT.elementAt(i)))))
								 {
									 VEXCHNG_RATE_FLAG.add("N");
									 VEXCHNG_DT_REMARK.add("");
								 }
								 else
								 {
									 VEXCHNG_RATE_FLAG.add("Y");
									 VEXCHNG_DT_REMARK.add("Deviation in Exchange rate date: "+VEXCHG_RATE_DT.elementAt(i)+", "+ "It should be Invoice date: "+VINVOICE_DT.elementAt(i));
								 }
							 }
							 else if(exchng_rt_crt.equalsIgnoreCase("PRE"))
							 {
								 if(((""+VOTH_PERIOD_END_DT.elementAt(i)).equals((""+VEXCHG_RATE_DT.elementAt(i)))))
								 {
									 VEXCHNG_RATE_FLAG.add("N");
									 VEXCHNG_DT_REMARK.add("");
								 }
								 else
								 {
									 VEXCHNG_RATE_FLAG.add("Y");
									 VEXCHNG_DT_REMARK.add("Deviation in Exchange rate date: "+VEXCHG_RATE_DT.elementAt(i)+", "
									 		+ "It should be Invoice date: "+VOTH_PERIOD_END_DT.elementAt(i));
								 }
							 }
							 else if(exchng_rt_crt.equalsIgnoreCase("LST"))
							 {
								 if(((""+VPERIOD_END_DT.elementAt(i)).equals((""+VEXCHG_RATE_DT.elementAt(i)))))
								 {
									 VEXCHNG_RATE_FLAG.add("N");
									 VEXCHNG_DT_REMARK.add("");
								 }
								 else
								 {
									 VEXCHNG_RATE_FLAG.add("Y");
									 VEXCHNG_DT_REMARK.add("Deviation in Exchange rate date: "+VEXCHG_RATE_DT.elementAt(i)+", "
									 		+ "It should be Invoice date: "+VINVOICE_DT.elementAt(i));
								 }
							 }
							 else
							 {
								 VEXCHNG_RATE_FLAG.add("N");
								 VEXCHNG_DT_REMARK.add(""); 
							 }
						 }
						 else
						 {
							 VEXCHNG_RATE_FLAG.add("N");
							 VEXCHNG_DT_REMARK.add("");
						 }
						 
						 
						 //billin_frequency date deviation
						 if(bill_freq.equalsIgnoreCase("F"))
						 {
							 if((""+VEND_DT.elementAt(i)).equals((""+VPERIOD_END_DT.elementAt(i))) || 
							   (""+FIRST_FORTNIGHT.elementAt(i)).equals((""+VPERIOD_END_DT.elementAt(i))) )
							 {
								 VBILLING_FREQ_FLAG.add("N");
								 VBILLING_DT_REMARK.add("");
							 }
							 else
							 {
								 VBILLING_FREQ_FLAG.add("Y");
								 VBILLING_DT_REMARK.add("There is Deviation in Fortnightly Billing Frequency Date: "+VPERIOD_END_DT.elementAt(i)+
										 ", Instead It should be Either Date: "+FIRST_FORTNIGHT.elementAt(i)+" Or Date: "+VEND_DT.elementAt(i));  
							 }
						 }
						 else if(bill_freq.equalsIgnoreCase("M"))
						 {
							 if((""+VEND_DT.elementAt(i)).equals((""+VPERIOD_END_DT.elementAt(i))))
							 {
								 VBILLING_FREQ_FLAG.add("N");
								 VBILLING_DT_REMARK.add("");
							 }
							 else
							 {
								 VBILLING_FREQ_FLAG.add("Y");
								 VBILLING_DT_REMARK.add("There is Deviation in Monthly Billing Frequency Date: "+VPERIOD_END_DT.elementAt(i)+
										 ", Instead It should be "+VEND_DT.elementAt(i)); 
							 }
						 }
						 else if(bill_freq.equalsIgnoreCase("W"))
						 {
							 if((""+VEND_DT.elementAt(i)).equals((""+VPERIOD_END_DT.elementAt(i))) || 
							   (""+FIRST_WEEK_DAYS.elementAt(i)).equals((""+VPERIOD_END_DT.elementAt(i))) ||
							   (""+SECOND_WEEK_DAYS.elementAt(i)).equals((""+VPERIOD_END_DT.elementAt(i))) || 
							   (""+THIRD_WEEK_DAYS.elementAt(i)).equals((""+VPERIOD_END_DT.elementAt(i))))
							 {
								 VBILLING_FREQ_FLAG.add("N");
								 VBILLING_DT_REMARK.add("");
							 }
							 else
							 {
								 VBILLING_FREQ_FLAG.add("Y");
								 VBILLING_DT_REMARK.add("There is Deviation in Weekly Billing Frequency Date: "+VPERIOD_END_DT.elementAt(i)+
										 ", Instead It should be either date : "+FIRST_WEEK_DAYS.elementAt(i)+" or date: "+SECOND_WEEK_DAYS.elementAt(i)+"                              or date: "
										 + ""+THIRD_WEEK_DAYS.elementAt(i)+" or date: "+VEND_DT.elementAt(i)); 
							 }
						 }
						 else if(bill_freq.equalsIgnoreCase("O"))
						 {
							 String other_date = "";
							 queryString1 = "SELECT TO_CHAR(dt, 'DD/MM/YYYY') as dt "
							 		+ "FROM (SELECT TO_DATE(?, 'DD/MM/YYYY') + (? - 1) + (LEVEL - 1) * ? AS dt "
							 		+ "FROM dual CONNECT BY LEVEL <= 1) "
							 		+ "WHERE EXTRACT(MONTH FROM dt) = ? "
							 		+ "AND EXTRACT(YEAR FROM dt) = ? "
							 		+ "ORDER BY dt ";
							 stmt1 = conn.prepareStatement(queryString1);
							 stmt1.setString(1,""+VPERIOD_START_DT.elementAt(i) );
							 stmt1.setString(2, bill_days);
							 stmt1.setString(3, bill_days);
							 stmt1.setString(4, mnth);
							 stmt1.setString(5, year);
							 rset1 = stmt1.executeQuery();
							 if(rset1.next())
							 {
								 other_date = rset1.getString(1)==null?"":rset1.getString(1);
							 }
							 rset1.close();
							 stmt1.close();
							 
							 if(other_date.equals(""+VPERIOD_END_DT.elementAt(i)))
							 {
								 VBILLING_FREQ_FLAG.add("N");
								 VBILLING_DT_REMARK.add("");
							 }
							 else
							 {
								 VBILLING_FREQ_FLAG.add("Y");
								 VBILLING_DT_REMARK.add("There is Deviation in Other Billing Frequency Date: "+VPERIOD_END_DT.elementAt(i)+
										 ", Instead It should be "+other_date); 
							 }
						 }
						 else
						 {
							 VBILLING_FREQ_FLAG.add("N");
							 VBILLING_DT_REMARK.add("");
						 }
					 }
					 else
					 {
							VDUE_DT_FLG.add("N");
							VEXCHNG_RATE_FLAG.add("N");
							VBILLING_FREQ_FLAG.add("N");
							
							VDUE_DT_REMARK.add("");
							VBILLING_DT_REMARK.add("");
							VEXCHNG_DT_REMARK.add("");	
							
					 }
					 stmt.close();
					 rset.close();
                     
				 }
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name,nm,e);
		}
	}
	
	
	
	public void getCustomerMasterForAllocToCust() 
	{
		String function_nm = "getCustomerMasterForAllocToCust()";
		try 
		{
			String queryString = "SELECT DISTINCT COUNTERPARTY_CD " 
					+ "FROM FMS_DAILY_ALLOCATION_DTL A "
					+ "WHERE COMPANY_CD=? " 
					+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
					+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
					+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, from_dt);
			stmt.setString(3, to_dt);
			rset = stmt.executeQuery();
			while (rset.next()) 
			{
				String cust_cd = rset.getString(1) == null ? "" : rset.getString(1);
				String cust_abbr = "" + utilBean.getCounterpartyABBR(conn,cust_cd);
				String cust_nm = "" + utilBean.getCounterpartyName(conn,cust_cd);

				VMST_COUNTERPARTY_CD.add(cust_cd);
				VMST_COUNTERPARTY_ABBR.add(cust_abbr);
				VMST_COUNTERPARTY_NM.add(cust_nm);
			}
			rset.close();
			stmt.close();
		} 
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	
	public void getContractList() 
	{
		String function_nm = "getContractList()";
		try 
		{
			 String companyCd ="",countpty_cd="",agmt="",agmt_rev="",cont="",cont_type="",cont_rev="";
			 String deal_map = "",cont_ref="";
			 queryString ="SELECT DISTINCT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO, "
					+ "A.AGMT_REV,A.CONT_NO,A.CONTRACT_TYPE "
					+ "FROM FMS_DAILY_ALLOCATION_DTL A "
					+ "WHERE A.COMPANY_CD = ? AND A.GAS_DT >= TO_DATE(?, 'DD/MM/YYYY') "
					+ "AND A.GAS_DT <= TO_DATE(?, 'DD/MM/YYYY') AND A.COUNTERPARTY_CD = ? "
					+ "AND A.NOM_REV_NO = ( "
					+ "SELECT MAX(B.NOM_REV_NO) "
					+ "FROM FMS_DAILY_ALLOCATION_DTL B "
					+ "WHERE B.COMPANY_CD = A.COMPANY_CD "
					+ "AND B.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
					+ "AND B.AGMT_NO = A.AGMT_NO "
					+ "AND B.AGMT_REV = A.AGMT_REV "
					+ "AND B.CONT_NO = A.CONT_NO AND B.PLANT_SEQ = A.PLANT_SEQ "
					+ "AND B.TRANSPORTER_CD = A.TRANSPORTER_CD AND B.TRANS_SEQ = A.TRANS_SEQ "
					+ "AND B.BU_SEQ = A.BU_SEQ AND B.GAS_DT = A.GAS_DT "
					+ "AND B.CONTRACT_TYPE = A.CONTRACT_TYPE AND B.CARGO_NO = A.CARGO_NO) ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, from_dt);
					stmt.setString(3, to_dt);
					stmt.setString(4, counterparty_cd);
					rset = stmt.executeQuery();
					while (rset.next()) 
					{
						 companyCd = rset.getString(1) == null ? "" : rset.getString(1);
						 countpty_cd = rset.getString(2) == null ? "" : rset.getString(2);
						 agmt = rset.getString(3) == null ? "" : rset.getString(3);
						 agmt_rev = rset.getString(4) == null ? "" : rset.getString(4);
						 cont = rset.getString(5) == null ? "" : rset.getString(5);
						 cont_type = rset.getString(6) == null ? "" : rset.getString(6);
						
	
						VDIS_CONT_MAPPING.add(utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, agmt_rev, cont, "", cont_type, ""));
						VAGMT_NO.add(agmt);
						VAGMT_REV.add(agmt_rev);
						VCONT_NO.add(cont);
						VCONTRACT_TYPE.add(cont_type);
						VCARGO_NO.add("0");
						VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_type));
								 
						String queryString1 = "SELECT A.CONT_REF_NO,A.CONT_REV "
								+ "FROM FMS_SUPPLY_CONT_MST A "
								+ "WHERE A.COMPANY_CD = ? AND A.COUNTERPARTY_CD = ? "
								+ "AND A.AGMT_NO = ? AND A.AGMT_REV = ? "
								+ "AND A.CONT_NO = ? AND A.CONTRACT_TYPE = ? "
								+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) "
								+ "FROM FMS_SUPPLY_CONT_MST B "
								+ "WHERE B.COMPANY_CD = A.COMPANY_CD "
								+ "AND B.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
								+ "AND B.AGMT_NO = A.AGMT_NO "
								+ "AND B.AGMT_REV = A.AGMT_REV "
								+ "AND B.CONT_NO  = A.CONT_NO "
								+ "AND B.CONTRACT_TYPE = A.CONTRACT_TYPE) "
								+ "UNION "
								+ "SELECT A.CONT_REF_NO,A.CONT_REV "
								+ "FROM FMS_LTCORA_CONT_MST A "
								+ "WHERE A.COMPANY_CD = ? AND A.COUNTERPARTY_CD = ? "
								+ "AND A.AGMT_NO = ? AND A.AGMT_REV = ? "
								+ "AND A.CONT_NO = ? AND A.CONTRACT_TYPE = ? "
								+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) "
								+ "FROM FMS_LTCORA_CONT_MST B "
								+ "WHERE B.COMPANY_CD = A.COMPANY_CD "
								+ "AND B.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
								+ "AND B.AGMT_NO = A.AGMT_NO "
								+ "AND B.AGMT_REV = A.AGMT_REV "
								+ "AND B.CONT_NO  = A.CONT_NO "
								+ "AND B.CONTRACT_TYPE = A.CONTRACT_TYPE)";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, countpty_cd);
						stmt1.setString(3, agmt);
						stmt1.setString(4, agmt_rev);
						stmt1.setString(5, cont);
						stmt1.setString(6, cont_type);
						stmt1.setString(7, comp_cd);
						stmt1.setString(8, countpty_cd);
						stmt1.setString(9, agmt);
						stmt1.setString(10, agmt_rev);
						stmt1.setString(11, cont);
						stmt1.setString(12, cont_type);
						rset1 = stmt1.executeQuery();
						if(rset1.next())
						{
							cont_ref = rset1.getString(1)==null?"":rset1.getString(1);
							cont_rev = rset1.getString(2)==null?"":rset1.getString(2);
							VCONT_REF.add(cont_ref);
							VCONT_REV.add(cont_rev);
						}
						rset1.close();
						stmt1.close();
					}
					rset.close();
					stmt.close();
		} 
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	
	public void getDealMapping()
	{
		String function_nm = "getDealMapping()";
		try
		{
			if(!counterparty_cd.equals("0") && !counterparty_cd.equals("") && !deal_no.equals("0") && !deal_no.equals(""))
			{
				
				String queryString1 = "SELECT A.CONT_REF_NO,A.AGMT_BASE "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE A.COMPANY_CD = ? AND A.COUNTERPARTY_CD = ? "
						+ "AND A.AGMT_NO = ? AND A.AGMT_REV = ? "
						+ "AND A.CONT_NO = ? AND A.CONTRACT_TYPE = ? "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) "
						+ "FROM FMS_SUPPLY_CONT_MST B "
						+ "WHERE B.COMPANY_CD = A.COMPANY_CD "
						+ "AND B.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
						+ "AND B.AGMT_NO = A.AGMT_NO "
						+ "AND B.AGMT_REV = A.AGMT_REV "
						+ "AND B.CONT_NO  = A.CONT_NO "
						+ "AND B.CONTRACT_TYPE = A.CONTRACT_TYPE) "
						+ "UNION "
						+ "SELECT A.CONT_REF_NO,A.AGMT_BASE "
						+ "FROM FMS_LTCORA_CONT_MST A "
						+ "WHERE A.COMPANY_CD = ? AND A.COUNTERPARTY_CD = ? "
						+ "AND A.AGMT_NO = ? AND A.AGMT_REV = ? "
						+ "AND A.CONT_NO = ? AND A.CONTRACT_TYPE = ? "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) "
						+ "FROM FMS_LTCORA_CONT_MST B "
						+ "WHERE B.COMPANY_CD = A.COMPANY_CD "
						+ "AND B.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
						+ "AND B.AGMT_NO = A.AGMT_NO "
						+ "AND B.AGMT_REV = A.AGMT_REV "
						+ "AND B.CONT_NO  = A.CONT_NO "
						+ "AND B.CONTRACT_TYPE = A.CONTRACT_TYPE)";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, agmt_no);
				stmt1.setString(4, agmt_rev);
				stmt1.setString(5, cont_no);
				stmt1.setString(6, cont_type);
				stmt1.setString(7, comp_cd);
				stmt1.setString(8, counterparty_cd);
				stmt1.setString(9, agmt_no);
				stmt1.setString(10, agmt_rev);
				stmt1.setString(11, cont_no);
				stmt1.setString(12, cont_type);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					cont_ref_no = rset1.getString(1)==null?"":rset1.getString(1);
					agmt_base = rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
			   deal_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev, cont_no, "", cont_type, "");
			}
		}
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
	}
	
	public void getPlantName()
	{
		String function_nm = "getPlantName";
		
		try 
		{
				String queryString = "SELECT DISTINCT A.PLANT_SEQ " 
						+ "FROM FMS_DAILY_ALLOCATION_DTL A "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD  = ? " 
						+ "AND A.AGMT_NO = ? AND A.AGMT_REV = ? AND A.CONT_NO = ? AND A.CONTRACT_TYPE = ? "
						+ "AND A.GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND A.GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.NOM_REV_NO=(SELECT MAX(B.NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt_no);
				stmt.setString(4, agmt_rev);
				stmt.setString(5, cont_no);
				stmt.setString(6, cont_type);
				stmt.setString(7, from_dt);
				stmt.setString(8, to_dt);
				rset = stmt.executeQuery();
				while (rset.next()) 
				{
					String plant_seq = rset.getString(1) == null ? "" : rset.getString(1);
					String plant_name = utilBean.getCounterpartyPlantName(conn, counterparty_cd, comp_cd, plant_seq, "C");
					VPLANT_SEQ_NO.add(plant_seq);
					VPLANT_NM.add(plant_name);
					
				}
				rset.close();
				stmt.close();
				VCOUNTERPARTY_PLANT_SEQ.add(VPLANT_SEQ_NO);
				VCOUNTERPARTY_PLANT_NM.add(VPLANT_NM);
		} 
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getBUList() 
	{
		String function_nm = "getBUList()";
		try 
		{
			int count=0;
			String queryString_bu = "SELECT DISTINCT BU_SEQ FROM "
					+ "( "
					+ "SELECT BU_SEQ "
					+ "FROM FMS_DAILY_ALLOCATION_DTL A  "
					+ "WHERE A.COMPANY_CD = ? "
					+ "AND A.GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND A.GAS_DT<=TO_DATE(?,'DD/MM/YYYY') ";
			if(!counterparty_cd.equals("0") && (!counterparty_cd.equals(""))) 
			{
				queryString_bu +=" AND COUNTERPARTY_CD=? ";
			}
			if(!deal_no.equals("0") && (!deal_no.equals(""))) 
			{
				queryString_bu +=" AND CONT_NO = ? AND CONTRACT_TYPE = ? ";
			}
			queryString_bu +=" UNION "
					+ "SELECT BU_SEQ "
					+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
					+ "WHERE A.COMPANY_CD = ?  "
					+ "AND A.GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND A.GAS_DT<=TO_DATE(?,'DD/MM/YYYY')";
			if(!counterparty_cd.equals("0") && (!counterparty_cd.equals(""))) 
			{
			  queryString_bu +=" AND COUNTERPARTY_CD=? ";
			}
			if(!deal_no.equals("0") && (!deal_no.equals(""))) 
			{
				queryString_bu +=" AND CONT_NO = ? AND CONTRACT_TYPE = ? ";
			}
			queryString_bu +=" )";
			
			stmt = conn.prepareStatement(queryString_bu);
			stmt.setString(++count, comp_cd);
			stmt.setString(++count, from_dt);
			stmt.setString(++count, to_dt);
			if(!counterparty_cd.equals("0") && (!counterparty_cd.equals(""))) 
			{
				stmt.setString(++count, counterparty_cd);
			}
			if(!deal_no.equals("0") && (!deal_no.equals(""))) 
			{
				stmt.setString(++count, cont_no);
			    stmt.setString(++count, cont_type); 
			}
			stmt.setString(++count, comp_cd);
			stmt.setString(++count, from_dt);
			stmt.setString(++count, to_dt);
			if(!counterparty_cd.equals("0") && (!counterparty_cd.equals(""))) 
			{
		    	stmt.setString(++count, counterparty_cd);
			}
			if(!deal_no.equals("0") && (!deal_no.equals(""))) 
			{
			    stmt.setString(++count, cont_no);
			    stmt.setString(++count, cont_type);
			}
			rset = stmt.executeQuery();
			while (rset.next()) 
			{
				String bu_cd = rset.getString(1) == null ? "" : rset.getString(1);
				VBU_PLANT_SEQ.add(bu_cd);
				VBU_PLANT_ABBR.add("" + utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_cd, "B"));
			}
			rset.close();
			stmt.close();
		} 
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	
	}
	
	
public void getAllocToCustomerData() 
{
	String function_nm = "getAllocToCustomerData()";
	try 
    {
		VSEGMENT.clear();
		VSEGMENT.add("RLNG");
		VSEGMENT.add("LTCORA");
		

		Vector TEMP_CUST_CD = new Vector();
		Vector TEMP_CUST_ABBR = new Vector();
		Vector TEMP_CUST_NM = new Vector();
		Vector VDELV_FLG = new Vector();
		Vector VDELV_DEAL_MAP = new Vector();

		if (!counterparty_cd.equals("") && !counterparty_cd.equals("0")) 
		{
			if (VMST_COUNTERPARTY_CD.contains(counterparty_cd)) 
			{
				TEMP_CUST_CD.add(VMST_COUNTERPARTY_CD.get(VMST_COUNTERPARTY_CD.indexOf(counterparty_cd)));
				TEMP_CUST_ABBR.add(VMST_COUNTERPARTY_ABBR.get(VMST_COUNTERPARTY_CD.indexOf(counterparty_cd)));
				TEMP_CUST_NM.add(VMST_COUNTERPARTY_NM.get(VMST_COUNTERPARTY_CD.indexOf(counterparty_cd)));
			}
		} 
		else 
		{
			TEMP_CUST_CD = VMST_COUNTERPARTY_CD;
			TEMP_CUST_ABBR = VMST_COUNTERPARTY_ABBR;
			TEMP_CUST_NM = VMST_COUNTERPARTY_NM;
		}

		VCOUNTERPARTY_CD.addAll(TEMP_CUST_CD);
		VCOUNTERPARTY_ABBR.addAll(TEMP_CUST_ABBR);
		VCOUNTERPARTY_NM.addAll(TEMP_CUST_NM);
		
		for (int i = 0; i < TEMP_CUST_CD.size(); i++) 
		{
			
			String countpty_cd = "" + TEMP_CUST_CD.elementAt(i);
			
			int index = 0;
			double mmbtu = 0;
			double scm = 0;
			String temp_mmbtu = "";

			String queryString = "SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
					+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
					+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
					+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, from_dt);
			stmt.setString(2, to_dt);
			rset = stmt.executeQuery();
			while (rset.next()) 
			{
				index += 1;
				gas_dt = rset.getString(1) == null ? "" : rset.getString(1);
				VGAS_DT.add(gas_dt);

				int count=0;
				String cont_map = countpty_cd + "-%-%-%-%-%";
				String cont_map1 = countpty_cd + "-"+ cont_type +"-"+ agmt_no +"-"+agmt_rev +"-"+ cont_no+"-%";
				String queryString1 = "SELECT SUM(ALLOC_QTY), SUM(ALLOC_SCM) " 
						+ "FROM ( ";
					queryString1+= " SELECT SUM(EXIT_QTY_MMBTU) ALLOC_QTY, SUM(EXIT_QTY_SCM) ALLOC_SCM"
							+ " FROM FMS_DAILY_TRANSPORTER_ALLOC A " 
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') " ;
					if(!bu_seq.equals("0") && (!bu_seq.equals(""))) 
					{
					   queryString1+= " AND BU_SEQ=? ";		
					}
//							
					queryString1+= " AND SELL_CONT_MAP LIKE ? "
							+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) "
							+ "FROM FMS_DAILY_TRANSPORTER_ALLOC B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
							+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) "
							+ "UNION ";
					queryString1+=" SELECT SUM(QTY_MMBTU) ALLOC_QTY, SUM(QTY_SCM) ALLOC_SCM "
							+ "FROM FMS_DAILY_ALLOCATION_DTL A " 
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') ";
					if(!deal_no.equals("0") && (!deal_no.equals(""))) 
					{
					   queryString1+= " AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONTRACT_TYPE= ? ";		
					}
					if (!bu_seq.equals("0") && !bu_seq.equals("")) 
				    {
				        queryString1 += " AND BU_SEQ=? ";
				    }
				    if(!bu_plant.equals("0") && (!bu_plant.equals(""))) 
					{
				    queryString1+= " AND PLANT_SEQ = ? ";		
					}
					queryString1+= " AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) "
							+ "FROM FMS_DAILY_ALLOCATION_DTL B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
							+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
							+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) " 
							+ "AND (A.COUNTERPARTY_CD || '-' || A.CONTRACT_TYPE || '-' || A.AGMT_NO || '-' || A.AGMT_REV || '-' || A.CONT_NO || '-' || A.CONT_REV) NOT IN (SELECT B.SELL_CONT_MAP "
							+ "FROM FMS_DAILY_TRANSPORTER_ALLOC B WHERE A.COMPANY_CD = B.COMPANY_CD "
							+ "AND B.SELL_CONT_MAP LIKE ? "
							+ "AND A.GAS_DT = B.GAS_DT ";
					
					if (!bu_seq.equals("0") && !bu_seq.equals("")) 
				    {
				        queryString1 += " AND BU_SEQ=? ";
				    }
				    if(!bu_plant.equals("0") && (!bu_plant.equals(""))) 
					{
				    queryString1+= " AND PLANT_SEQ = ? ";		
					}
					queryString1+= " ))";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(++count, comp_cd);
					stmt1.setString(++count, "C");
					stmt1.setString(++count, gas_dt);
					if(!bu_seq.equals("0") && (!bu_seq.equals(""))) 
					{
					  stmt1.setString(++count, bu_seq);		
					}
					stmt1.setString(++count, cont_map1);
					stmt1.setString(++count, comp_cd);
					stmt1.setString(++count, countpty_cd);
					stmt1.setString(++count, gas_dt);
					if(!deal_no.equals("0") && (!deal_no.equals(""))) 
					{
					  stmt1.setString(++count, agmt_no);		
					  stmt1.setString(++count, agmt_rev);		
					  stmt1.setString(++count, cont_no);		
					  stmt1.setString(++count, cont_type);		
					}
					if(!bu_seq.equals("0") && (!bu_seq.equals(""))) 
					{
					  stmt1.setString(++count, bu_seq);		
					}
					if(!bu_plant.equals("0") && (!bu_plant.equals(""))) 
					{
					  stmt1.setString(++count, bu_plant);		
					}
					
					stmt1.setString(++count, cont_map);
					if(!bu_seq.equals("0") && (!bu_seq.equals(""))) 
					{
					   stmt1.setString(++count, bu_seq);		
					}
					if(!bu_plant.equals("0") && (!bu_plant.equals(""))) 
					{
					  stmt1.setString(++count, bu_plant);		
					}
					rset1 = stmt1.executeQuery();
					while (rset1.next()) 
					{
						temp_mmbtu = rset1.getString(1) == null ? "" : rset1.getString(1);
						mmbtu = rset1.getDouble(1);
						scm = rset1.getDouble(2);

						if (temp_mmbtu.equals("")) 
						{
							VQTY_MMBTU.add("-");
							VQTY_SCM.add("-");

						} 
						else 
						{
							VQTY_MMBTU.add(nf.format(mmbtu));
							VQTY_SCM.add(nf.format(scm));
						}
					}
					rset1.close();
					stmt1.close();
				
					if(!deal_no.equals("0") && !deal_no.equals(""))
					{
						for (int j = 0; j < ((Vector) VCOUNTERPARTY_PLANT_SEQ.elementAt(i)).size(); j++) 
						{
							String plantSeq = "" + ((Vector) VCOUNTERPARTY_PLANT_SEQ.elementAt(i)).elementAt(j);
							cont_map = countpty_cd + "-%-%-%-%-%";
							cont_map1 = countpty_cd + "-"+ cont_type +"-"+ agmt_no +"-"+agmt_rev +"-"+ cont_no+"-%";
							String exit_point = "C-" + countpty_cd + "-" + plantSeq;
							int count1 =0;
							
							queryString1 = "SELECT SUM(ALLOC_QTY), SUM(ALLOC_SCM) " 
									+ "FROM "
									+ "(";
								queryString1+= "SELECT SUM(EXIT_QTY_MMBTU) ALLOC_QTY, SUM(EXIT_QTY_SCM) ALLOC_SCM "
										+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A " 
										+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
										+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') " ;
								if(!bu_seq.equals("0") && (!bu_seq.equals(""))) 
								{
								    queryString1+= " AND BU_SEQ=? ";		
								}
								queryString1+= " AND EXIT_PT_MAPPING_ID=? "
										+ "AND SELL_CONT_MAP LIKE ? "
										+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) "
										+ "FROM FMS_DAILY_TRANSPORTER_ALLOC B "
										+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
										+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
										+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
										+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) "
										+ "UNION ";
							queryString1+= " SELECT SUM(QTY_MMBTU) ALLOC_QTY, SUM(QTY_SCM) ALLOC_SCM "
									+ "FROM FMS_DAILY_ALLOCATION_DTL A " 
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND PLANT_SEQ=? ";
							
							if(!deal_no.equals("0") && (!deal_no.equals(""))) 
							{
							   queryString1+= " AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONTRACT_TYPE= ? ";		
							}
							if (!bu_seq.equals("0") && !bu_seq.equals("")) 
						    {
						        queryString1 += " AND BU_SEQ=? ";
						    }
						    if(!bu_plant.equals("0") && (!bu_plant.equals(""))) 
							{
							   queryString1+= " AND PLANT_SEQ = ? ";		
							}
					
					
							queryString1+= " AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) "
									+ "FROM FMS_DAILY_ALLOCATION_DTL B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) " 
									+ "AND (A.COUNTERPARTY_CD || '-' || A.CONTRACT_TYPE || '-' || A.AGMT_NO || '-' || A.AGMT_REV || '-' || A.CONT_NO || '-' || A.CONT_REV) NOT IN (SELECT B.SELL_CONT_MAP "
									+ "FROM FMS_DAILY_TRANSPORTER_ALLOC B WHERE A.COMPANY_CD = B.COMPANY_CD "
									+ "AND B.SELL_CONT_MAP LIKE ? "
									+ "AND A.GAS_DT = B.GAS_DT ";
							
							if (!bu_seq.equals("0") && !bu_seq.equals("")) 
						    {
						        queryString1 += " AND BU_SEQ=? ";
						    }
						    if(!bu_plant.equals("0") && (!bu_plant.equals(""))) 
							{
							   queryString1+= " AND PLANT_SEQ = ? ";		
							}
							queryString1+= " ))";
							stmt1 = conn.prepareStatement(queryString1);
							stmt1.setString(++count1, comp_cd);
							stmt1.setString(++count1, "C");
							stmt1.setString(++count1, gas_dt);
							if(!bu_seq.equals("0") && (!bu_seq.equals(""))) 
							{
							 stmt1.setString(++count1, bu_seq);		
							}
							stmt1.setString(++count1, exit_point);
							stmt1.setString(++count1, cont_map1);
							stmt1.setString(++count1, comp_cd);
							stmt1.setString(++count1, countpty_cd);
							stmt1.setString(++count1, gas_dt);
							stmt1.setString(++count1, plantSeq);
							
							if(!deal_no.equals("0") && (!deal_no.equals(""))) 
							{
							  stmt1.setString(++count1, agmt_no);		
							  stmt1.setString(++count1, agmt_rev);		
							  stmt1.setString(++count1, cont_no);		
							  stmt1.setString(++count1, cont_type);		
							}
							if(!bu_seq.equals("0") && (!bu_seq.equals(""))) 
							{
							 stmt1.setString(++count1, bu_seq);		
							}
							if(!bu_plant.equals("0") && (!bu_plant.equals(""))) 
							{
							  stmt1.setString(++count1, bu_plant);		
							}
							
							stmt1.setString(++count1, cont_map);
							if(!bu_seq.equals("0") && (!bu_seq.equals(""))) 
							{
							 stmt1.setString(++count1, bu_seq);		
							}
							if(!bu_plant.equals("0") && (!bu_plant.equals(""))) 
							{
							  stmt1.setString(++count1, bu_plant);		
							}
							rset1 = stmt1.executeQuery();
							while (rset1.next())
							{
								temp_mmbtu = rset1.getString(1) == null ? "" : rset1.getString(1);
								mmbtu = rset1.getDouble(1);

								scm = rset1.getDouble(2);

								if (temp_mmbtu.equals("")) 
								{
									VQTY_MMBTU.add("-");
									VQTY_SCM.add("-");
								} 
								else 
								{
									VQTY_MMBTU.add(nf.format(mmbtu));
									VQTY_SCM.add(nf.format(scm));
								}
							}
							rset1.close();
							stmt1.close();
						}
					  }
					}
					VINDEX.add(index);
					rset.close();
					stmt.close();

			double total_mmbtu = 0;
			double total_scm = 0;
			double temp_total_mmbtu = 0;
			double temp_total_scm = 0;
			// TOTAL FOR plant WISE
			int count=0;

			String cont_map = countpty_cd + "-%-%-%-%-%";
			String cont_map1 = countpty_cd + "-"+ cont_type +"-"+ agmt_no +"-"+agmt_rev +"-"+ cont_no+"-%";
			String queryString1 = "SELECT SUM(ALLOC_QTY), SUM(ALLOC_SCM) " 
					+ "FROM "
					+ "(";
				queryString1+= "SELECT SUM(EXIT_QTY_MMBTU) ALLOC_QTY, SUM(EXIT_QTY_SCM) ALLOC_SCM "
						+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A " 
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') ";
				if(!bu_seq.equals("0") && (!bu_seq.equals(""))) 
				{
				   queryString1+= " AND BU_SEQ=?";		
				}
				queryString1+= " AND SELL_CONT_MAP LIKE ? "
						+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) "
						+ "FROM FMS_DAILY_TRANSPORTER_ALLOC B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) "
						+ "UNION ";
				queryString1+= " SELECT SUM(QTY_MMBTU) ALLOC_QTY, SUM(QTY_SCM) ALLOC_SCM "
						+ "FROM FMS_DAILY_ALLOCATION_DTL A " 
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') ";
				if(!deal_no.equals("0") && (!deal_no.equals(""))) 
				{
				   queryString1+= " AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONTRACT_TYPE= ? ";		
				}
				if (!bu_seq.equals("0") && !bu_seq.equals("")) 
			    {
			        queryString1 += " AND BU_SEQ=? ";
			    }
			    if(!bu_plant.equals("0") && (!bu_plant.equals(""))) 
				{
				   queryString1+= " AND PLANT_SEQ = ? ";		
				} 
				
				queryString1+= " AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) "
						+ "FROM FMS_DAILY_ALLOCATION_DTL B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) " 
						+ "AND (A.COUNTERPARTY_CD || '-' || A.CONTRACT_TYPE || '-' || A.AGMT_NO || '-' || A.AGMT_REV || '-' || A.CONT_NO || '-' || A.CONT_REV) NOT IN (SELECT B.SELL_CONT_MAP "
						+ "FROM FMS_DAILY_TRANSPORTER_ALLOC B WHERE A.COMPANY_CD = B.COMPANY_CD "
						+ "AND B.SELL_CONT_MAP LIKE ? "
						+ "AND A.GAS_DT = B.GAS_DT ";
				if (!bu_seq.equals("0") && !bu_seq.equals("")) 
			    {
			        queryString1 += " AND BU_SEQ=? ";
			    }
			    if(!bu_plant.equals("0") && (!bu_plant.equals(""))) 
				{
				   queryString1+= " AND PLANT_SEQ = ? ";		
				} 
			    
				queryString1+= " ))";
				stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(++count, comp_cd);
					stmt1.setString(++count, "C");
					stmt1.setString(++count, from_dt);
					stmt1.setString(++count, to_dt);
					if(!bu_seq.equals("0") && (!bu_seq.equals(""))) 
					{
					  stmt1.setString(++count, bu_seq);		
					}
					stmt1.setString(++count, cont_map1);
				stmt1.setString(++count, comp_cd);
				stmt1.setString(++count, countpty_cd);
				stmt1.setString(++count, from_dt);
				stmt1.setString(++count, to_dt);
				
				if(!deal_no.equals("0") && (!deal_no.equals(""))) 
				{
				  stmt1.setString(++count, agmt_no);		
				  stmt1.setString(++count, agmt_rev);		
				  stmt1.setString(++count, cont_no);		
				  stmt1.setString(++count, cont_type);		
				}
				if(!bu_seq.equals("0") && (!bu_seq.equals(""))) 
				{
				   stmt1.setString(++count, bu_seq);		
				}
				if(!bu_plant.equals("0") && (!bu_plant.equals(""))) 
				{
				  stmt1.setString(++count, bu_plant);		
				}
				
				stmt1.setString(++count, cont_map);
				if(!bu_seq.equals("0") && (!bu_seq.equals(""))) 
				{
				   stmt1.setString(++count, bu_seq);		
				}
				if(!bu_plant.equals("0") && (!bu_plant.equals(""))) 
				{
				  stmt1.setString(++count, bu_plant);		
				}
				rset1 = stmt1.executeQuery();
				while (rset1.next()) 
				{
					total_mmbtu = rset1.getDouble(1);
					total_scm = rset1.getDouble(2);

					if (Double.doubleToRawLongBits(total_mmbtu) != Double.doubleToRawLongBits(0)) 
					{
						temp_total_mmbtu = total_mmbtu;
						temp_total_scm = total_scm;
					}
					else
					{
						temp_total_mmbtu = 0.00;
						temp_total_scm = 0.00;
					}
				}
				rset1.close();
				stmt1.close();
				VTOTAL_QTY_MMBTU.add(nf.format(temp_total_mmbtu));
				VTOTAL_QTY_SCM.add(nf.format(temp_total_scm));

				if(!deal_no.equals("0") && !deal_no.equals(""))
				{
					for (int k = 0; k < ((Vector) VCOUNTERPARTY_PLANT_SEQ.elementAt(i)).size(); k++) 
					{
						String plantSeq = "" + ((Vector) VCOUNTERPARTY_PLANT_SEQ.elementAt(i)).elementAt(k);
						String exit_point = "C-" + countpty_cd + "-" + plantSeq;
						cont_map = countpty_cd + "-%-%-%-%-%";
						cont_map1 = countpty_cd + "-"+ cont_type +"-"+ agmt_no +"-"+agmt_rev +"-"+ cont_no+"-%";

						int count1=0;
						
						queryString1 = "SELECT SUM(ALLOC_QTY), SUM(ALLOC_SCM) " 
								+ "FROM "
								+ "(";
							queryString1+= "SELECT SUM(EXIT_QTY_MMBTU) ALLOC_QTY, SUM(EXIT_QTY_SCM) ALLOC_SCM "
									+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A " 
									+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
									+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') ";
							if(!bu_seq.equals("0") && (!bu_seq.equals(""))) 
							{
							   queryString1+= " AND BU_SEQ=?";		
							}
							
							queryString1+= " AND EXIT_PT_MAPPING_ID=? " 
									+ "AND SELL_CONT_MAP LIKE ? "
									+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) "
									+ "FROM FMS_DAILY_TRANSPORTER_ALLOC B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
									+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) "
									+ "UNION ";
						queryString1+= " SELECT SUM(QTY_MMBTU) ALLOC_QTY, SUM(QTY_SCM) ALLOC_SCM "
								+ "FROM FMS_DAILY_ALLOCATION_DTL A " 
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') AND PLANT_SEQ=? ";
						
						if(!deal_no.equals("0") && (!deal_no.equals(""))) 
						{
						   queryString1+= " AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONTRACT_TYPE= ? ";		
						}
						if(!bu_seq.equals("0") && (!bu_seq.equals(""))) 
						{
						    queryString1+= " AND BU_SEQ=?";		
						}
						if(!bu_plant.equals("0") && (!bu_plant.equals(""))) 
						{
						   queryString1+= " AND PLANT_SEQ = ? ";		
						}
						
						queryString1+= " AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) "
								+ "FROM FMS_DAILY_ALLOCATION_DTL B "
								+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
								+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
								+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
								+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
								+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) " 
								+ "AND (A.COUNTERPARTY_CD || '-' || A.CONTRACT_TYPE || '-' || A.AGMT_NO || '-' || A.AGMT_REV || '-' || A.CONT_NO || '-' || A.CONT_REV) NOT IN (SELECT B.SELL_CONT_MAP "
								+ "FROM FMS_DAILY_TRANSPORTER_ALLOC B WHERE A.COMPANY_CD = B.COMPANY_CD "
								+ "AND B.SELL_CONT_MAP LIKE ? "
								+ "AND A.GAS_DT = B.GAS_DT ";
						if(!bu_seq.equals("0") && (!bu_seq.equals(""))) 
						{
						    queryString1+= " AND BU_SEQ=?";		
						}
						if(!bu_plant.equals("0") && (!bu_plant.equals(""))) 
						{
						   queryString1+= " AND PLANT_SEQ = ? ";		
						}
						
						queryString1+= " ))";
						stmt1 = conn.prepareStatement(queryString1);
							stmt1.setString(++count1, comp_cd);
							stmt1.setString(++count1, "C");
							stmt1.setString(++count1, from_dt);
							stmt1.setString(++count1, to_dt);
							if(!bu_seq.equals("0") && (!bu_seq.equals(""))) 
							{
							   stmt1.setString(++count1, bu_seq);		
							}
							stmt1.setString(++count1, exit_point);
							stmt1.setString(++count1, cont_map1);
						stmt1.setString(++count1, comp_cd);
						stmt1.setString(++count1, countpty_cd);
						stmt1.setString(++count1, from_dt);
						stmt1.setString(++count1, to_dt);
						stmt1.setString(++count1, plantSeq);
						
						if(!deal_no.equals("0") && (!deal_no.equals(""))) 
						{
						  stmt1.setString(++count1, agmt_no);		
						  stmt1.setString(++count1, agmt_rev);		
						  stmt1.setString(++count1, cont_no);		
						  stmt1.setString(++count1, cont_type);		
						}
						if(!bu_seq.equals("0") && (!bu_seq.equals(""))) 
						{
						   stmt1.setString(++count1, bu_seq);		
						}
						if(!bu_plant.equals("0") && (!bu_plant.equals(""))) 
						{
						  stmt1.setString(++count1, bu_plant);		
						}
						
						stmt1.setString(++count1, cont_map);
						if(!bu_seq.equals("0") && (!bu_seq.equals(""))) 
						{
						   stmt1.setString(++count1, bu_seq);		
						}
						if(!bu_plant.equals("0") && (!bu_plant.equals(""))) 
						{
						  stmt1.setString(++count1, bu_plant);		
						}
						rset1 = stmt1.executeQuery();
						while (rset1.next()) 
						{
							total_mmbtu = rset1.getDouble(1);
							total_scm = rset1.getDouble(2);

							if (Double.doubleToRawLongBits(total_mmbtu) != Double.doubleToRawLongBits(0)) 
							{
								temp_total_mmbtu = total_mmbtu;
								temp_total_scm = total_scm;
							}
							else
							{
								temp_total_mmbtu = 0.00;
								temp_total_scm = 0.00;
							}
							VTOTAL_QTY_MMBTU.add(nf.format(temp_total_mmbtu));
							VTOTAL_QTY_SCM.add(nf.format(temp_total_scm));
						}
						rset1.close();
						stmt1.close();
					}
				  }
				}
			}
			catch (Exception e) 
			{
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			}
	}
	
	String callFlag = "";
	public void setCallFlag(String callFlag) {this.callFlag = callFlag;}
	String comp_cd = "";
	public void setComp_cd(String comp_cd) {this.comp_cd = comp_cd;}
	
	String from_dt="";
	String to_dt="";
	String counterparty_cd = "";
	String segmentType = "";
	String bu_seq = "";
	String bu_plant = "";
	String agmt_no = "";
	String agmt_rev = "";
	String cont_no = "";
	String cont_rev = "";
	String cont_type = "";
	String deal_no = "";
	String deal_map = "";
	String cont_ref_no = "";
	String agmt_base = "";
	
	
	public void setFrom_dt(String from_dt) {this.from_dt = from_dt;}
	public void setTo_dt(String to_dt) {this.to_dt = to_dt;} 
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setSegmentType(String segmentType) {this.segmentType = segmentType;}
	public void setBu(String bu_seq) {this.bu_seq = bu_seq;}
	public void setBu_plant(String bu_plant) {this.bu_plant = bu_plant;}
	public void setAgmt_no(String agmt_no) {this.agmt_no = agmt_no;}
	public void setAgmt_rev(String agmt_rev) {this.agmt_rev = agmt_rev;}
	public void setCont_no(String cont_no) {this.cont_no = cont_no;}
	public void setCont_rev(String cont_rev) {this.cont_rev = cont_rev;}
	public void setCont_type(String cont_type) {this.cont_type = cont_type;}
	public void setDeal_no(String deal_no) {this.deal_no = deal_no;}
	
	public String getDeal_map() {return deal_map;}
	public String getCont_ref_no() {return cont_ref_no;}
	public String getAgmt_base() {return agmt_base;}
    
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VCOUNTERPTY_CD = new Vector();
	Vector VCOUNTERPTY_ABBR = new Vector();
	Vector VCOUNTERPTY_NM = new Vector();
	Vector VPERIOD_START_DT = new Vector();
	Vector VPERIOD_END_DT = new Vector();
	Vector VINVOICE_DT = new Vector();
	Vector VINVOICE_DUE_DT = new Vector();
	Vector VDUE_DT_FLG = new Vector();
	Vector VDUE_DT_REMARK = new Vector();
	Vector VEXCHG_RATE_DT = new Vector();
	Vector VEXCHNG_DT_REMARK = new Vector();
	Vector VBILLING_DT_REMARK = new Vector();
	Vector VCONT_NO = new Vector();
	Vector VAGMT_NO = new Vector();
	Vector VPLANT_SEQ = new Vector();
	Vector VCONTRACT_TYPE = new Vector();
	Vector VEXCHNG_RATE_FLAG = new Vector();
	Vector VBILLING_FREQ_FLAG = new Vector();
	Vector VOTH_PERIOD_END_DT = new Vector();
	Vector VEND_DT = new Vector();
	Vector VINVOICE_NO = new Vector();
	Vector VCHK_FLG = new Vector();
	
	Vector VSEGMENT = new Vector();
	Vector VSEGMENT_TYPE = new Vector();
	Vector VTEMP_SEGMENT_TYPE = new Vector();
	Vector VTEMP_SEGMENT = new Vector();
	Vector VDIS_CONT_MAPPING = new Vector();
	Vector VCONT_REF = new Vector();
	Vector VSTART_DT = new Vector();
	Vector VAGMT_BASE = new Vector();
	Vector VCARGO_NO = new Vector();
	Vector VCONTRACT_TYPE_NM = new Vector();
	Vector VBU_PLANT_ABBR = new Vector();
	Vector VBU_PLANT_SEQ = new Vector();
	Vector VMST_COUNTERPARTY_CD = new Vector();
	Vector VMST_COUNTERPARTY_ABBR = new Vector();
	Vector VMST_COUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_PLANT_SEQ = new Vector();
	Vector VCOUNTERPARTY_PLANT_NM = new Vector();
	Vector VGAS_DT = new Vector();
	Vector VQTY_MMBTU = new Vector();
	Vector VQTY_SCM = new Vector();
	Vector VTOTAL_QTY_MMBTU = new Vector();
	Vector VTOTAL_QTY_SCM = new Vector();
	Vector VINDEX = new Vector();
	Vector VINDEX1 = new Vector();
	
	Vector VPLANT_SEQ_NO = new Vector();
	Vector VPLANT_NM = new Vector();
	Vector VCONT_REV = new Vector();
	Vector VAGMT_REV = new Vector();
	
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVCOUNTERPTY_CD() {return VCOUNTERPTY_CD;}
	public Vector getVCOUNTERPTY_ABBR() {return VCOUNTERPTY_ABBR;}
	public Vector getVCOUNTERPTY_NM() {return VCOUNTERPTY_NM;}
	public Vector getVPERIOD_START_DT() {return VPERIOD_START_DT;}
	public Vector getVPERIOD_END_DT() {return VPERIOD_END_DT;}
	public Vector getVINVOICE_DT() {return VINVOICE_DT;}
	public Vector getVINVOICE_DUE_DT() {return VINVOICE_DUE_DT;}
	public Vector getVDUE_DT_FLG() {return VDUE_DT_FLG;}
	public Vector getVDUE_DT_REMARK() {return VDUE_DT_REMARK;}
	public Vector getVEXCHG_RATE_DT() {return VEXCHG_RATE_DT;}
	public Vector getVEXCHNG_DT_REMARK() {return VEXCHNG_DT_REMARK;}
	public Vector getVBILLING_DT_REMARK() {return VBILLING_DT_REMARK;}
	public Vector getVCONT_NO() {return VCONT_NO;}
	public Vector getVAGMT_NO() {return VAGMT_NO;}
	public Vector getVPLANT_SEQ() {return VPLANT_SEQ;}
	public Vector getVCONTRACT_TYPE() {return VCONTRACT_TYPE;}
	public Vector getVEXCHNG_RATE_FLAG() {return VEXCHNG_RATE_FLAG;}
	public Vector getVBILLING_FREQ_FLAG() {return VBILLING_FREQ_FLAG;}
	public Vector getVOTH_PERIOD_END_DT() {return VOTH_PERIOD_END_DT;}
	public Vector getVEND_DT() {return VEND_DT;}
	public Vector getVINVOICE_NO() {return VINVOICE_NO;}
	public Vector getVCHK_FLG() {return VCHK_FLG;}
	
	public Vector getVSEGMENT() {return VSEGMENT;}
	public Vector getVSEGMENT_TYPE() {return VSEGMENT_TYPE;}
	public Vector getVTEMP_SEGMENT() {return VTEMP_SEGMENT;}
	public Vector getVTEMP_SEGMENT_TYPE() {return VTEMP_SEGMENT_TYPE;}
	public Vector getVDIS_CONT_MAPPING() {return VDIS_CONT_MAPPING;}
	public Vector getVCONT_REF() {return VCONT_REF;}
	public Vector getVSTART_DT() {return VSTART_DT;}
	public Vector getVAGMT_BASE() {return VAGMT_BASE;}
	public Vector getVCARGO_NO() {return VCARGO_NO;}
	public Vector getVCONTRACT_TYPE_NM() {return VCONTRACT_TYPE_NM;}
	public Vector getVBU_PLANT_SEQ() {return VBU_PLANT_SEQ;}
	public Vector getVBU_PLANT_ABBR() {return VBU_PLANT_ABBR;}
	public Vector getVMST_COUNTERPARTY_CD() {return VMST_COUNTERPARTY_CD;}
	public Vector getVMST_COUNTERPARTY_ABBR() {return VMST_COUNTERPARTY_ABBR;}
	public Vector getVMST_COUNTERPARTY_NM() {return VMST_COUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_PLANT_SEQ() {return VCOUNTERPARTY_PLANT_SEQ;}
	public Vector getVCOUNTERPARTY_PLANT_NM() {return VCOUNTERPARTY_PLANT_NM;}
	public Vector getVGAS_DT() {return VGAS_DT;}
	public Vector getVQTY_MMBTU() {return VQTY_MMBTU;}
	public Vector getVQTY_SCM() {return VQTY_SCM;}
	public Vector getVTOTAL_QTY_MMBTU() {return VTOTAL_QTY_MMBTU;}
	public Vector getVTOTAL_QTY_SCM() {return VTOTAL_QTY_SCM;}
	public Vector getVINDEX() {return VINDEX;}
	public Vector getVINDEX1() {return VINDEX1;}
	
	public Vector getVPLANT_SEQ_NO() {return VPLANT_SEQ_NO;}
	public Vector getVPLANT_NM() {return VPLANT_NM;}
	public Vector getVCONT_REV() {return VCONT_REV;}
	public Vector getVAGMT_REV() {return VAGMT_REV;}
	
	
	
}