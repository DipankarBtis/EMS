package com.etrm.fms.derivatives;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Vector;

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
import com.etrm.fms.util.DB_AllocationUtil;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.TaxCalculator;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.XmlUtilBean;

public class DB_Derivatives_Invoice 
{
String db_src_file_name="DB_Derivatives_Invoice.java";
	
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
	PreparedStatement stmt_temp;
	ResultSet rset;
	ResultSet rset0;
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset5;
	ResultSet rset6;
	ResultSet rset7;
	ResultSet rset_temp;
	String queryString="";
	String queryString0="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";
	String queryString6="";
	String queryString7="";
	
	UtilBean utilBean = new UtilBean();
	DateUtil dateUtil = new DateUtil();
	TaxCalculator TaxCalc = new TaxCalculator(); 
	DB_AllocationUtil utilAlloc = new DB_AllocationUtil();
	XmlUtilBean xmlUtil = new XmlUtilBean();
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf3 = new DecimalFormat("###########0.000");
	NumberFormat nf0 = new DecimalFormat("###########0.0");
	
	NumberFormat df3 = new DecimalFormat("###########0.000");
	
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
	    			nf3.setRoundingMode(RoundingMode.HALF_UP);
	    			if(callFlag.equalsIgnoreCase("DERIVATIVES_INVOICE_PREPARATION_LIST"))
	    			{
	    				getCurveMapDtl();
	    				getDervContTraderList();
	    				getDervContBuPlantList();
	    				getDervContPlantList();
	    				getDervContCurveList();
	    				getDervDealDtl();
	    				
	    				String queryString = "SELECT TO_CHAR(MAX(REPORT_DT),'DD/MM/YYYY')  "
								+ "FROM FMS_MR_EXPO_EOD_MST "
								+ "WHERE COMPANY_CD=? AND REPORT_DT<TO_DATE(?,'DD/MM/YYYY') AND CONTRACT_TYPE='V' ";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, comp_cd);
						stmt.setString(2, report_dt);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							eod_procLastDt=rset.getString(1)==null?"":rset.getString(1);
						}
						rset.close();
						stmt.close();
						
						getMarketExposureDealWiseReportView(eod_procLastDt);
						getMarketExposureDealWiseReportViewCurrentDate();
	    			}
	    			else if(callFlag.equalsIgnoreCase("DERIVATIVES_INVOICE_DTL") || callFlag.equalsIgnoreCase("DERV_INVOICE_MODIFICATION"))
	    			{
	    				comp_name=""+utilBean.getCompanyName(conn, comp_cd);
	    				bu_plant_abbr=""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B");
	    				plant_abbr=""+utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
	    				counterparty_abbr=""+utilBean.getCounterpartyABBR(conn, counterparty_cd);
	    				getDervDealInvDtl();
	    			}
	    			else if(callFlag.equalsIgnoreCase("DERIVATIVES_INVOICE_MST"))
	    			{
	    				getInvType();
	    				getDervDealInvMst();
	    			}
	    			else if(callFlag.equalsIgnoreCase("DERV_INVOICE_REPORT"))
	    			{
	    				comp_name=""+utilBean.getCompanyName(conn, comp_cd);
	    				bu_plant_abbr=""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B");
	    				plant_abbr=""+utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
	    				counterparty_abbr=""+utilBean.getCounterpartyABBR(conn, counterparty_cd);
	    				counterparty_nm=""+utilBean.getCounterpartyName(conn, counterparty_cd);
	    				getDervInvoiceDtl();
	    				if(activityFlag.equals("APPROVE"))
	    				{
	    					String sysDt=dateUtil.getSysdate();
	    					fy_year=dateUtil.getFinancialYear(sysDt);
	    					if(inv_type.equals("I"))
	    					{
	    						getInvoiceNumber();
	    					}
	    					else if(inv_type.equals("R"))
	    					{
	    						getRemNumber();
	    					}
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("DERV_SAP_XML"))
	    			{
	    				generateDervInvoiceXML();
	    				parseSAP_XMLfile();
	    			}
	    			else if(callFlag.equalsIgnoreCase("PARSE_SAP_XML"))
	    			{
	    				getSapInvoiceApprovalDetail();
	    				parseSAP_XMLfile();
	    			}
	    			else if(callFlag.equalsIgnoreCase("SEND_DERV_INVOICE_MAIL"))
	    			{
	    				getSendDervInvoiceMailDetail();
	    			}
	    			else if(callFlag.equalsIgnoreCase("DERV_INV_APPROVAL"))
	    			{
	    				getInvType();
	    				getDervActualReport();
	    			}
	    			else if(callFlag.equalsIgnoreCase("DERV_ACCRUAL_ACCOUNTING"))
	    			{
	    				queryString="SELECT COUNT(*) "
	    						+ "FROM FMS_DERV_ACCRUAL_DTL "
	    						+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') AND IS_MTM NOT IN ('Y')";
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
	    					getAccrualCounterpartyList();
		    				getAccrualActiveContractList();
	    					getAccrualFreezedData();
	    					isFreezed="Y";
	    					if(automation_flag.equals("Y"))
		    				{
		    					if(isGenerateXML.equals("Y"))
		    					{
		    						generateDervAccrualXML();
		    					}
		    				}
	    				}
	    				else
	    				{
	    					isFreezed="N";
		    				getAccrualList();
		    				if(automation_flag.equals("Y"))
		    				{
		    					freezAccrualData();
		    					if(isGenerateXML.equals("Y"))
		    					{
		    						generateDervAccrualXML();
		    					}
		    				}
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("DERV_MTM_ACCRUAL_ACCOUNTING"))
	    			{
	    				queryString="SELECT COUNT(*) "
	    						+ "FROM FMS_DERV_ACCRUAL_DTL "
	    						+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') AND IS_MTM='Y'";
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
	    					getAccrualCounterpartyList();
		    				getAccrualActiveContractList();
	    					getAccrualFreezedData();
	    					isFreezed="Y";
	    					if(automation_flag.equals("Y"))
		    				{
		    					if(isGenerateXML.equals("Y"))
		    					{
		    						generateDervMTMXML();
		    					}
		    				}
	    				}
	    				else
	    				{
	    					isFreezed="N";
		    				getMtmList();
		    				
		    				if(automation_flag.equals("Y"))
		    				{
		    					freezAccrualData();
		    					if(isGenerateXML.equals("Y"))
		    					{
		    						generateDervMTMXML();
		    					}
		    				}
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("EXISTING_CRDR_LIST"))
	    			{
	    				getInvType();
	    				getCRDRPreparationList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("DERV_CRDR_PREPARATION_LIST"))
	    			{
	    				getDervCRDRTraderList();
	    				getInvoiceNoListforCrDr();
	    				getCriteriaList();
	    				getSelectedInvDtl();
	    				getSelectedInvInstrumentDtl();
	    				getRevisedInstrumentDtl();
	    				getSubmittedInstruCRDRDtl();
	    				
	    				comp_name=""+utilBean.getCompanyName(conn, comp_cd);
	    				counterparty_abbr=""+utilBean.getCounterpartyABBR(conn, counterparty_cd);
	    			}
	    			else if(callFlag.equalsIgnoreCase("DERV_CRDR_CHK_APRV"))
	    			{
	    				comp_name=""+utilBean.getCompanyName(conn, comp_cd);
	    				bu_plant_abbr=""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B");
	    				plant_abbr=""+utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
	    				counterparty_abbr=""+utilBean.getCounterpartyABBR(conn, counterparty_cd);
	    				counterparty_nm=""+utilBean.getCounterpartyName(conn, counterparty_cd);
	    				getDervInvoiceDetailCrDr();
	    				if(operation.equals("APPROVE"))
	    				{
	    					String sysDt=dateUtil.getSysdate();
	    					fy_year=dateUtil.getFinancialYear(sysDt);
	    					if(invoice_type.equals("I"))
	    					{
	    						getCrDrInvoiceNumber();
	    					}
	    					else if(invoice_type.equals("R"))
	    					{
	    						getCrDrRemNumber();
	    					}
	    				}
	    				getSelectedInvInstrumentDtlForChk();
	    				getNewInvInstrumentDtl();
	    			}
	    			else if(callFlag.equalsIgnoreCase("CRDR_DERV_SAP_XML"))
	    			{
	    				generateDervCrDrXML();
	    				parseSAP_XMLfile();
	    			}
	    			else if(callFlag.equalsIgnoreCase("CRDR_PARSE_SAP_XML"))
	    			{
	    				getSapCrDrApprovalDetail();
	    				parseSAP_XMLfile();
	    			}
	    			else if(callFlag.equalsIgnoreCase("SEND_CRDR_DERV_INVOICE_MAIL"))
	    			{
	    				getSendDervCrDrMailDetail();
	    			}
	    			else if(callFlag.equalsIgnoreCase("VIEW_ALL_PDF"))
	    			{
	    				getAllPdfFileName();
	    			}
	    			else if(callFlag.equalsIgnoreCase("DERV_INVOICE_CHANGE_REQUEST_APPROVAL"))
	    			{
	    				VMST_INV_TYPE_FLG.add("I");
	    				VMST_INV_TYPE_FLG.add("R");
	    				VMST_INV_TYPE_FLG.add("CRDR");
	    				
	    				VMST_INV_TYPE.add("Derivative Invoice");
	    				VMST_INV_TYPE.add("Derivative Remittance");
	    				VMST_INV_TYPE.add("Derivative Credit/Debit Invoice");
	    				
	    				getDervInvoiceChangeList();
	    				
    					
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
	
	public double ExposureCalculation()
	{
		String function_nm="ExposureCalculation()";
		double var_eff_rate=0;
		try
		{
			double tot_dcq=0;
			double tot_actual_qty=0;
			
			double tot_total=0;
			double tot_phy_expo_realized=0;
			double tot_phy_expo_unrealized=0;
			
			double tot_phy_expo_ori=0;
			double tot_fin_expo_ori=0;
			
			double tot_fin_expo_realized=0;
			double tot_fin_expo_unrealized=0;
			
			double tot_phy_unrealized_leg=0;
			double tot_fin_unrealized_leg=0;
			double tot_fin_realized_leg=0;
			
			double sign=-1;
			if(account.equals("BUY"))
			{
				sign=1;
			}
			int sr=0;
			String queryString2="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
					+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
					+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
					+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, start_dt);
			stmt2.setString(2, end_dt);
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				String gas_dt=rset2.getString(1)==null?"":rset2.getString(1);
				String cont_month="01/"+gas_dt.substring(3,gas_dt.length());
				
				String contPriceMapping=counterparty_cd+"-"+agmt_no+"-"+cont_no;
				
				String var_color="blue";
				String var_dcq=utilBean.getContVariableTAQ(conn,comp_cd, counterparty_cd, agmt_no, cont_no, cont_type, gas_dt);
				if(var_dcq.equals(""))
				{
					 var_color="black";
					if(account.equals("Sell"))
					{
						var_dcq=utilBean.getContVariableDCQ(conn,comp_cd, counterparty_cd, agmt_no, cont_no, cont_type, gas_dt);
						if(var_dcq.equals(""))
						{
							var_dcq=quantity;
						}
					}
					else
					{
						var_dcq=utilBean.getPurchaseContVariableDCQ(conn,comp_cd, counterparty_cd, agmt_no, cont_no, cont_type, gas_dt);
						if(var_dcq.equals(""))
						{
							var_dcq=quantity;
						}
					}
				}
				double temp_var_dcq=Double.parseDouble(var_dcq);
				var_dcq=nf.format(Double.parseDouble(var_dcq) * sign);
				
				double actual_qty=0;
				double temp_actual_qty=0;
				double weighted_average=0;
				if(cont_type.equals("V"))
				{
					temp_actual_qty=temp_var_dcq;
					actual_qty=temp_var_dcq;
				}
				
				
				temp_actual_qty=actual_qty;
				if(Double.doubleToRawLongBits(temp_actual_qty)!=Double.doubleToRawLongBits(0))
				{
					temp_actual_qty=sign*temp_actual_qty;
				}
				
				if(Double.doubleToRawLongBits(actual_qty)!=Double.doubleToRawLongBits(0))
				{
					actual_qty=sign*actual_qty;
				}
				
				String variable_rate="";
				String price_type="";
				String curve_nm="";
				String slope="1";
				String constant="0";
				String phys_curve_nm="";
				String price_range="";
				String price_day="";
				String price_start_dt="";
				String price_end_dt="";
				String curve_logic="";
				String price_seq_no="";
				String formula_desc="";
				
				String queryString1="SELECT RATE,RATE_UNIT,PRICE_TYPE,CURVE_NM,SLOPE,CONST,PHYS_CURVE_NM,"
						+ "PRICE_RANGE,TO_CHAR(PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(PRICE_END_DT,'DD/MM/YYYY'),"
						+ "CURVE_LOGIC,SEQ_NO,FORMULA "
						+ "FROM FMS_CONT_PRICE_DTL "
						+ "WHERE COMPANY_CD=? AND MAPPING_ID=? AND CONTRACT_TYPE=? "
						+ "AND FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND TO_DT>=TO_DATE(?,'DD/MM/YYYY')";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, contPriceMapping);
				stmt1.setString(3, cont_type);
				stmt1.setString(4, gas_dt);
				stmt1.setString(5, gas_dt);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					variable_rate=nf.format(rset1.getDouble(1));
					String var_rate_unit=rset1.getString(2)==null?"":rset1.getString(2);
					price_type=rset1.getString(3)==null?"F":rset1.getString(3);
					curve_nm=rset1.getString(4)==null?"":rset1.getString(4);
					slope=rset1.getString(5)==null?"1":rset1.getString(5);
					constant=rset1.getString(6)==null?"0":rset1.getString(6);
					phys_curve_nm=rset1.getString(7)==null?"":rset1.getString(7);
					price_range=rset1.getString(8)==null?"A":rset1.getString(8);
					price_start_dt=rset1.getString(9)==null?"":rset1.getString(9);
					price_end_dt=rset1.getString(10)==null?"":rset1.getString(10);
					curve_logic=rset1.getString(11)==null?"":rset1.getString(11);
					price_seq_no=rset1.getString(12)==null?"":rset1.getString(12);
					formula_desc=rset1.getString(13)==null?"":rset1.getString(13);
				}
				rset1.close();
				stmt1.close();
				
				
				String cont_price="";
				
				if(cont_price.equals(""))
				{
					cont_price=rate;
				}
				cont_price=nf2.format(Double.parseDouble(cont_price)+weighted_average);
				
				if(variable_rate.equals(""))
				{
					variable_rate=cont_price;
				}
				else
				{
					variable_rate=nf2.format(Double.parseDouble(variable_rate)+weighted_average);
				}
				
				Vector VTEMP_DCQ = new Vector();
				Vector VTEMP_ALLOC_QTY= new Vector();
				Vector VTEMP_CURVE = new Vector();
				Vector VTEMP_CURVE_NM = new Vector();
				Vector VTEMP_CURVE_EOD = new Vector();
				Vector VTEMP_FIN_MMYYYY= new Vector();
				Vector VTEMP_SETTLE_START_DT= new Vector();
				Vector VTEMP_SETTLE_END_DT= new Vector();
				Vector VTEMP_FIN_CONT_MONTH= new Vector();
				Vector VTEMP_SLOPE = new Vector();
				Vector VTEMP_CONST= new Vector();
				Vector VTEMP_FIN_RU= new Vector();
				Vector VTEMP_PHY_RU= new Vector();
				Vector VTEMP_SETTLE_PRICE= new Vector();
				Vector VTEMP_AVG_SETTLE_PRICE= new Vector();
				Vector VTEMP_EFF_PRICE= new Vector();
				Vector VTEMP_EFF_PRICE_INFO= new Vector();
				
				
				double avgSettlePrice=0;
				double avgPrice=0;
				String avgPriceInfo="";
				
				int phyCountRu=dateUtil.getDays(gas_dt, report_dt);
				String phyRu="";
				if(phyCountRu<=1)
				{
					phyRu="R";
				}
				else
				{
					phyRu="U";
				}
				
				if(cont_type.equals("V"))
				{
					//FIXED SIDE
					VTEMP_CURVE.add("Fixed");
					VTEMP_CURVE_EOD.add("Fixed");
					VTEMP_CURVE_NM.add("");
					VTEMP_FIN_MMYYYY.add("");
					VTEMP_SETTLE_START_DT.add("");
					VTEMP_SETTLE_END_DT.add("");
					VTEMP_FIN_CONT_MONTH.add("");
					VTEMP_SLOPE.add("1");
					VTEMP_CONST.add("0");
					VTEMP_DCQ.add(var_dcq);
					VTEMP_ALLOC_QTY.add(nf.format(actual_qty));
					
					VTEMP_FIN_RU.add("");
					VTEMP_PHY_RU.add("");
					VTEMP_SETTLE_PRICE.add("");
					VTEMP_AVG_SETTLE_PRICE.add("");
					VTEMP_EFF_PRICE.add("");
					VTEMP_EFF_PRICE_INFO.add("");
					
					//FLOAT SIDE			
					var_dcq=nf.format(Double.parseDouble(var_dcq) * -1);
					if(Double.doubleToRawLongBits(actual_qty)!=Double.doubleToRawLongBits(0))
					{
						actual_qty=-1*actual_qty;
					}
		
					curve_nm=derivative_curve_nm;
					VTEMP_CURVE.add("Float(SINGLE) : "+curve_nm);
					VTEMP_CURVE_EOD.add("Float(SINGLE)");
					VTEMP_CURVE_NM.add(curve_nm);
					
					Vector temp=SettlementDateCalculation(curve_nm, cont_month, price_range, price_start_dt, price_end_dt);
					VTEMP_FIN_MMYYYY.add(temp.elementAt(0));
					VTEMP_SETTLE_START_DT.add(temp.elementAt(1));
					VTEMP_SETTLE_END_DT.add(temp.elementAt(2));
					VTEMP_FIN_CONT_MONTH.add(temp.elementAt(4));
					VTEMP_SLOPE.add(slope);
					VTEMP_CONST.add(constant);
					VTEMP_DCQ.add(var_dcq);
					VTEMP_ALLOC_QTY.add(nf.format(actual_qty));
					VTEMP_FIN_RU.add("");
					VTEMP_PHY_RU.add("");
					VTEMP_SETTLE_PRICE.add("");
					VTEMP_AVG_SETTLE_PRICE.add("");
					VTEMP_EFF_PRICE.add("");
					VTEMP_EFF_PRICE_INFO.add("");
				}
				else if(price_type.equals("M"))
				{
					if(curve_logic.equals("SINGLE"))
					{
						VTEMP_CURVE.add("Float("+curve_logic+") : "+curve_nm);
						VTEMP_CURVE_EOD.add("Float("+curve_logic+")");
						VTEMP_CURVE_NM.add(curve_nm);
						
						Vector temp=SettlementDateCalculation(curve_nm, cont_month, price_range, price_start_dt, price_end_dt);
						VTEMP_FIN_MMYYYY.add(temp.elementAt(0));
						VTEMP_SETTLE_START_DT.add(temp.elementAt(1));
						VTEMP_SETTLE_END_DT.add(temp.elementAt(2));
						VTEMP_FIN_CONT_MONTH.add(temp.elementAt(4));
						VTEMP_SLOPE.add(slope);
						VTEMP_CONST.add(constant);
						VTEMP_DCQ.add(var_dcq);
						VTEMP_ALLOC_QTY.add(nf.format(actual_qty));
						VTEMP_FIN_RU.add("");
						VTEMP_PHY_RU.add("");
						VTEMP_SETTLE_PRICE.add("");
						VTEMP_AVG_SETTLE_PRICE.add("");
						VTEMP_EFF_PRICE.add("");
						VTEMP_EFF_PRICE_INFO.add("");
					}
					else if(curve_logic.equals("MIN") || curve_logic.equals("MAX") || curve_logic.equals("AVG"))
					{
						String split[]=formula_desc.substring(4,formula_desc.length()).split("@");
						String temp_dcq=nf.format(Double.parseDouble(var_dcq)/split.length);
						String temp_alloc_qty=temp_alloc_qty=nf.format(actual_qty/split.length);
						
						queryString1="SELECT CURVE_NM,SLOPE,CONST,"
								+ "PRICE_RANGE,TO_CHAR(PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(PRICE_END_DT,'DD/MM/YYYY') "
								+ "FROM FMS_CONT_PRICE_MIN_DTL "
								+ "WHERE COMPANY_CD=? AND MAPPING_ID=? AND CONTRACT_TYPE=? "
								+ "AND SEQ_NO=?";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, contPriceMapping);
						stmt1.setString(3, cont_type);
						stmt1.setString(4, price_seq_no);
						rset1=stmt1.executeQuery();
						while(rset1.next())
						{
							String min_curve_nm=rset1.getString(1)==null?"":rset1.getString(1);
							String min_slope=rset1.getString(2)==null?"1":rset1.getString(2);
							String min_constant=rset1.getString(3)==null?"0":rset1.getString(3);
							String min_price_range=rset1.getString(4)==null?"A":rset1.getString(4);
							String min_price_start_dt=rset1.getString(5)==null?"":rset1.getString(5);
							String min_price_end_dt=rset1.getString(6)==null?"":rset1.getString(6);
							
							VTEMP_CURVE.add("Float("+curve_logic+") : "+min_curve_nm);
							VTEMP_CURVE_EOD.add("Float("+curve_logic+")");
							VTEMP_CURVE_NM.add(min_curve_nm);
							VTEMP_SLOPE.add(min_slope);
							VTEMP_CONST.add(min_constant);
							VTEMP_DCQ.add(temp_dcq);
							VTEMP_ALLOC_QTY.add(temp_alloc_qty);
							
							String temp_avgPriceInfo=min_curve_nm;
							
							Vector temp=SettlementDateCalculation(min_curve_nm, cont_month, min_price_range, min_price_start_dt, min_price_end_dt);
							VTEMP_FIN_MMYYYY.add(temp.elementAt(0));
							VTEMP_SETTLE_START_DT.add(temp.elementAt(1));
							VTEMP_SETTLE_END_DT.add(temp.elementAt(2));
							VTEMP_FIN_CONT_MONTH.add(temp.elementAt(4));
							
							int count_ru=dateUtil.getDays(""+temp.elementAt(2), report_dt);
							String ru="R";
							if(count_ru>1)
							{
								ru="U";
							}
							
							VTEMP_FIN_RU.add(ru);
							VTEMP_PHY_RU.add(phyRu);
							double settle_price=0;
							if(min_curve_nm.equals("PPAC"))
							{
								settle_price=getForwardPrice(min_curve_nm, report_dt, ""+temp.elementAt(4),"Forward","Financial");
								
							}
							else
							{
								settle_price=getSettledPrice(min_curve_nm, ""+temp.elementAt(1), ""+temp.elementAt(2));
							}
							
							double fin_fwd_price=getForwardPrice(min_curve_nm, report_dt, ""+temp.elementAt(4),"Forward","Financial");
							
							int fin_count_ru=dateUtil.getDays(""+temp.elementAt(2), report_dt);
							
							int count_u=0;
							int count_r=0;
							
							if(fin_count_ru>1)
							{	
								int isSettlmentPeriodActive=dateUtil.isDateWithinPeriod(""+temp.elementAt(1),""+temp.elementAt(2),report_dt);
								
								if(isSettlmentPeriodActive==1)
								{
									String nextdt=dateUtil.getDate(report_dt, "1"); 
									count_u=getPriceCurveWorkingDays(min_curve_nm, nextdt, ""+temp.elementAt(2));
									count_r=getPriceCurveWorkingDays(min_curve_nm, ""+temp.elementAt(1), report_dt); 	
								}
								else
								{
									count_u=getPriceCurveWorkingDays(min_curve_nm, ""+temp.elementAt(1), ""+temp.elementAt(2));
									count_r=0;
								}						
							}
							else
							{	
								count_r=getPriceCurveWorkingDays(min_curve_nm, ""+temp.elementAt(1), ""+temp.elementAt(2));
							}
							
							double eff_rate=((((count_u * fin_fwd_price) + (count_r * settle_price))/(count_r+count_u)) * Double.parseDouble(min_slope)) + Double.parseDouble(min_constant);
							if(Double.isNaN(eff_rate)) {
								eff_rate=0;
							}
							
							temp_avgPriceInfo+="= {((#Fwd. days * Fwd. Price) + (#Settled days * Settle Price)) / (#Fwd. days + #Settled days)} * slope + const.\n"
									+ "= {(( "+count_u+" * "+nf2.format(fin_fwd_price)+" ) + ("+count_r+" * "+nf2.format(settle_price)+" )) / ( "+count_u+" + "+count_r+")} * "+min_slope+" + "+min_constant+"\n"
									+ "= "+nf2.format(eff_rate);
							avgPriceInfo=avgPriceInfo.equals("")?temp_avgPriceInfo:avgPriceInfo+"\n"+temp_avgPriceInfo;
							temp_avgPriceInfo+="\n\n= "+nf2.format(eff_rate)+" + "+nf2.format(weighted_average)+" (WA Charges)";
							eff_rate+=weighted_average;
							temp_avgPriceInfo+="\n= "+nf2.format(eff_rate);
							
							VTEMP_SETTLE_PRICE.add(nf2.format(settle_price));
							VTEMP_AVG_SETTLE_PRICE.add("");
							VTEMP_EFF_PRICE.add(nf2.format(eff_rate));
							VTEMP_EFF_PRICE_INFO.add(temp_avgPriceInfo);
						}
						rset1.close();
						stmt1.close();
					}
					else
					{
						if(curve_logic.equals("MULTI_LEG") && !formula_desc.equals(""))
						{	
							String split[]=formula_desc.split("@");
							String m0=split[1];
							String m1=split[2];
							String m2=split[3];
							
							String temp_dcq=nf.format(Double.parseDouble(var_dcq)/((Integer.parseInt(m2)-Integer.parseInt(m1))+1));
							String temp_alloc_qty=temp_alloc_qty=nf.format(actual_qty/((Integer.parseInt(m2)-Integer.parseInt(m1))+1));

							int settle_count_withoutzero=0;
							double totalPrice=0;
							double totalSettlePrice=0;
							
							int count = Integer.parseInt(m1)+(Integer.parseInt(m2)-Integer.parseInt(m1))+1;
							int multiply_factor=-1;
							if(m0.equals("Forward"))
							{
								multiply_factor=1;
							}
							
							avgPriceInfo="Eff. Price = Eff. Settle Price * Slope + Const.";
							
							for(int i=Integer.parseInt(m1);i<=Integer.parseInt(m2);i++)
							{
								VTEMP_CURVE.add("Float("+curve_logic+") : "+curve_nm);
								VTEMP_CURVE_EOD.add("Float("+curve_logic+")");
								VTEMP_CURVE_NM.add(curve_nm);
								VTEMP_SLOPE.add(slope);
								VTEMP_CONST.add(constant);
								VTEMP_DCQ.add(temp_dcq);
								VTEMP_ALLOC_QTY.add(temp_alloc_qty);
								
								int temp_i=i*multiply_factor;
								
								String multi_cont_month="";
								queryString1="SELECT TO_CHAR(ADD_MONTHS(TO_DATE(?,'DD/MM/YYYY'),?),'MM/YYYY') "
										+ "FROM DUAL";
								stmt1=conn.prepareStatement(queryString1);
								stmt1.setString(1, gas_dt);
								stmt1.setInt(2, temp_i);
								rset1=stmt1.executeQuery();
								if(rset1.next())
								{
									multi_cont_month="01/"+(rset1.getString(1)==null?"":rset1.getString(1));
								}
								rset1.close();
								stmt1.close();
								
								Vector temp=SettlementDateCalculation(curve_nm, multi_cont_month, price_range, price_start_dt, price_end_dt);
								VTEMP_FIN_MMYYYY.add(temp.elementAt(0));
								VTEMP_SETTLE_START_DT.add(temp.elementAt(1));
								VTEMP_SETTLE_END_DT.add(temp.elementAt(2));
								VTEMP_FIN_CONT_MONTH.add(temp.elementAt(4));
								
								int count_ru=dateUtil.getDays(""+temp.elementAt(2), report_dt);
								String ru="R";
								if(count_ru>1)
								{
									ru="U";
								}
								
								VTEMP_FIN_RU.add(ru);
								VTEMP_PHY_RU.add(phyRu);
								
								double settle_price=0;
								if(curve_nm.equals("PPAC"))
								{
									settle_price=getForwardPrice(curve_nm, report_dt, ""+temp.elementAt(4),"Forward","Financial");
									
								}
								else
								{
									settle_price=getSettledPrice(curve_nm, ""+temp.elementAt(1), ""+temp.elementAt(2));
								}
								if(settle_price>0)
								{
									totalSettlePrice+=settle_price;
									settle_count_withoutzero+=1;
								}
								
								VTEMP_SETTLE_PRICE.add(nf2.format(settle_price));
								VTEMP_AVG_SETTLE_PRICE.add("");
								VTEMP_EFF_PRICE.add("");
								VTEMP_EFF_PRICE_INFO.add("");
							}
							if(settle_count_withoutzero>0) {
								avgSettlePrice=totalSettlePrice/settle_count_withoutzero; 
							}
							avgPrice = avgSettlePrice * Double.parseDouble(slope) + Double.parseDouble(constant); 
							avgPriceInfo+="\n= [Sum(Settle Price)/#Month] * Slope + Const \n= ["+nf2.format(totalSettlePrice)+"/"+settle_count_withoutzero+"] * "+slope+" + "+constant+"\n= ["+nf2.format(avgSettlePrice)+"] * "+slope+" + "+constant+"\n= "+nf2.format(avgPrice);
							avgPriceInfo+="\n\n= "+nf2.format(avgPrice)+" + "+nf2.format(weighted_average)+"(Charges)";
							avgPrice+=weighted_average;
							avgPriceInfo+="\n= "+nf2.format(avgPrice);
						}
						else
						{
							VTEMP_CURVE.add("Float(SINGLE) : "+curve_nm);
							VTEMP_CURVE_EOD.add("Float(SINGLE)");
							VTEMP_CURVE_NM.add(curve_nm);
						
							Vector temp=SettlementDateCalculation(curve_nm, cont_month, price_range, price_start_dt, price_end_dt);
							VTEMP_FIN_MMYYYY.add(temp.elementAt(0));
							VTEMP_SETTLE_START_DT.add(temp.elementAt(1));
							VTEMP_SETTLE_END_DT.add(temp.elementAt(2));
							VTEMP_FIN_CONT_MONTH.add(temp.elementAt(4));
							VTEMP_SLOPE.add(slope);
							VTEMP_CONST.add(constant);
							VTEMP_DCQ.add(var_dcq);
							VTEMP_ALLOC_QTY.add(nf.format(actual_qty));
							
							VTEMP_FIN_RU.add("");
							VTEMP_PHY_RU.add("");
							VTEMP_SETTLE_PRICE.add("");
							VTEMP_AVG_SETTLE_PRICE.add("");
							VTEMP_EFF_PRICE.add("");
							VTEMP_EFF_PRICE_INFO.add("");
						}
					}
				}
				else
				{
					VTEMP_CURVE.add("Fixed");
					VTEMP_CURVE_EOD.add("Fixed");
					VTEMP_CURVE_NM.add("");
					VTEMP_FIN_MMYYYY.add("");
					VTEMP_SETTLE_START_DT.add("");
					VTEMP_SETTLE_END_DT.add("");
					VTEMP_FIN_CONT_MONTH.add("");
					VTEMP_SLOPE.add("1");
					VTEMP_CONST.add("0");
					VTEMP_DCQ.add(var_dcq);
					VTEMP_ALLOC_QTY.add(nf.format(actual_qty));
					
					VTEMP_FIN_RU.add("");
					VTEMP_PHY_RU.add("");
					VTEMP_SETTLE_PRICE.add("");
					VTEMP_AVG_SETTLE_PRICE.add("");
					VTEMP_EFF_PRICE.add("");
					VTEMP_EFF_PRICE_INFO.add("");
				}
				
				if(curve_logic.equals("MIN") || curve_logic.equals("MAX"))
				{
					int min_index=0;
					double price=Double.parseDouble(""+VTEMP_EFF_PRICE.elementAt(min_index));
					for(int i=1;i<VTEMP_EFF_PRICE.size();i++)
					{	
						if(curve_logic.equals("MAX"))
						{
							if(Double.parseDouble(""+VTEMP_EFF_PRICE.elementAt(i)) > price)
							{
								price=Double.parseDouble(""+VTEMP_EFF_PRICE.elementAt(i));
								min_index=i;
							}
						}
						else
						{
							if(Double.parseDouble(""+VTEMP_EFF_PRICE.elementAt(i)) < price)
							{
								price=Double.parseDouble(""+VTEMP_EFF_PRICE.elementAt(i));
								min_index=i;
							}
						}
					}
					
					for(int i=0;i<VTEMP_EFF_PRICE.size();i++)
					{
						if(VTEMP_PHY_RU.contains("U"))
						{
							if(min_index==i)
							{
								VTEMP_DCQ.remove(i);
								VTEMP_DCQ.add(i, nf.format(Double.parseDouble(var_dcq)));
							}
							else
							{
								VTEMP_DCQ.remove(i);
								VTEMP_DCQ.add(i, nf.format(0));
							}
						}
						else
						{
							if(min_index==i)
							{
								VTEMP_ALLOC_QTY.remove(i);
								VTEMP_ALLOC_QTY.add(i, nf.format(actual_qty));
							}
							else
							{
								VTEMP_ALLOC_QTY.remove(i);
								VTEMP_ALLOC_QTY.add(i, nf.format(0));
							}
						}
					}
				}
				
				if(curve_logic.equals("MULTI_LEG") && !VTEMP_FIN_RU.contains("U") && !VTEMP_PHY_RU.contains("U"))
				{
					for(int i=0;i<VTEMP_SETTLE_PRICE.size();i++)
					{
						if(i==0)
						{
							VTEMP_ALLOC_QTY.remove(i);
							VTEMP_ALLOC_QTY.add(i, nf.format(actual_qty));
							
							VTEMP_EFF_PRICE.remove(i);
							VTEMP_EFF_PRICE.add(i, nf2.format(avgPrice));
							
							VTEMP_EFF_PRICE_INFO.remove(i);
							VTEMP_EFF_PRICE_INFO.add(i, avgPriceInfo);
							
							VTEMP_AVG_SETTLE_PRICE.remove(i);
							VTEMP_AVG_SETTLE_PRICE.add(i, nf2.format(avgSettlePrice));
						}
						else
						{
							VTEMP_ALLOC_QTY.remove(i);
							VTEMP_ALLOC_QTY.add(i, nf.format(0));
							
							VTEMP_EFF_PRICE.remove(i);
							VTEMP_EFF_PRICE.add(i, nf2.format(0));
							
							VTEMP_EFF_PRICE_INFO.remove(i);
							VTEMP_EFF_PRICE_INFO.add(i, "");
							
							VTEMP_AVG_SETTLE_PRICE.remove(i);
							VTEMP_AVG_SETTLE_PRICE.add(i, nf2.format(0));
						}
					}
				}
				
				for(int i=0;i<VTEMP_CURVE.size();i++)
				{
					curve_nm=""+VTEMP_CURVE_NM.elementAt(i);
					slope=""+VTEMP_SLOPE.elementAt(i);
					constant=""+VTEMP_CONST.elementAt(i);
					if(cont_type.equals("V"))
					{
						sr=sr + 1;
						if(i>0)
						{
							price_type="M";
						}
					}
					else if(i==0)
					{
						sr=sr + 1;
						tot_dcq+=Double.parseDouble(var_dcq);
						tot_actual_qty+=temp_actual_qty;
					}
					var_dcq=""+VTEMP_DCQ.elementAt(i);
					actual_qty=Double.parseDouble(""+VTEMP_ALLOC_QTY.elementAt(i));
					
					double settle_price=0;
					String fin_mmyyyy=""+VTEMP_FIN_MMYYYY.elementAt(i);
					String fin_cont_month=""+VTEMP_FIN_CONT_MONTH.elementAt(i);
					String settle_start_dt=""+VTEMP_SETTLE_START_DT.elementAt(i);
					String settle_end_dt=""+VTEMP_SETTLE_END_DT.elementAt(i);
					String settle_dt="";
					int phy_count_ru=0;
					int fin_count_ru=0;
					double eff_rate=0;
					String eff_rate_info="";
					
					String phy_expo_ori="";
					String fin_expo_ori="";
					String phy_expo_ori_info="Original Exposure (Physical Leg)\n = ";
					String fin_expo_ori_info="Original Exposure (Financial Leg)\n = ";
					
					phy_count_ru=dateUtil.getDays(gas_dt, report_dt);
					String phy_ru="";
					String phy_expo_r="";
					String phy_expo_u="";
					String phy_expo_r_info="Realized Exposure (Physical Leg)\n = ";
					String phy_expo_u_info="Unrealized Exposure (Physical Leg)\n = ";
					
					String fin_ru="";
					String fin_expo_r="";
					String fin_expo_u=nf.format(0);
					String fin_expo_r_info="Realized Exposure (Financial Leg)\n = ";
					String fin_expo_u_info="Unrealized Exposure (Financial Leg)\n = ";
					
					double fin_fwd_price=0;
					
					if(phy_count_ru<=1)
					{
						phy_ru="R";
						phy_expo_ori=nf.format(actual_qty);
						phy_expo_ori_info+= "Allocated MMBTU for ("+phy_ru+") \n = "+phy_expo_ori;
						phy_expo_u=nf.format(0);
						phy_expo_u_info+="ZERO for ("+phy_ru+")\n ="+phy_expo_u;
					}
					else
					{
						phy_ru="U";
						phy_expo_ori=nf.format(Double.parseDouble(var_dcq));
						phy_expo_ori_info+= "DCQ for ("+phy_ru+") \n = "+phy_expo_ori;
						phy_expo_u= phy_expo_ori;
						phy_expo_u_info+="DCQ for ("+phy_ru+")\n ="+phy_expo_u;
					}
					
					if(price_type.equals("M"))
					{
						fin_count_ru=dateUtil.getDays(settle_end_dt, report_dt);
						
						int count_u=0;
						int count_r=0;
						
						// NOTE: This is actual formula given by Kris. But we found some issue with specific case when we switched to 
						// U and R specific codes which are currently commented
						if(Double.doubleToRawLongBits(Double.parseDouble(phy_expo_ori))!=Double.doubleToRawLongBits(0))
						{
							fin_expo_ori=nf.format(Double.parseDouble(phy_expo_ori) * -1 * Double.parseDouble(slope));
						}
						else
						{
							fin_expo_ori=nf.format(0);
						}
						fin_expo_ori_info+="Original Exposure (Physical Leg) * (-1) * Slope \n = "+phy_expo_ori+" * (-1) * "+slope+"\n = "+fin_expo_ori;
						
						
						fin_ru="R";
						if(fin_count_ru>1)
						{	
							fin_ru="U";
							int isSettlmentPeriodActive=dateUtil.isDateWithinPeriod(settle_start_dt,settle_end_dt,report_dt);
							
							if(isSettlmentPeriodActive==1)
							{
								String nextdt=dateUtil.getDate(report_dt, "1"); 
								count_u=getPriceCurveWorkingDays(curve_nm, nextdt, settle_end_dt);
								count_r=getPriceCurveWorkingDays(curve_nm, settle_start_dt, report_dt); 	
							}
							else
							{
								count_u=getPriceCurveWorkingDays(curve_nm, settle_start_dt, settle_end_dt);
								count_r=0;
							}
							
							double temp_dcq=Double.parseDouble(fin_expo_ori)/(count_r+count_u);
							fin_expo_u=nf.format(temp_dcq * count_u);						
						}
						else
						{
							count_r=getPriceCurveWorkingDays(curve_nm, settle_start_dt, settle_end_dt);
						}
						
						fin_expo_u_info+="Original Exposure (Finacial Leg) * {Workdays(U)/ Workdays(U+R)} "
								+ "\n = "+fin_expo_ori+" * {"+count_u+"/("+count_u+"+"+count_r+")} \n = "+fin_expo_u;
						if(curve_nm.equals("PPAC"))
						{
							settle_price=getForwardPrice(curve_nm, report_dt, fin_cont_month,"Forward","Financial");
							
						}
						else
						{
							settle_price=getSettledPrice(curve_nm, settle_start_dt, settle_end_dt);
						}
						fin_fwd_price=getForwardPrice(curve_nm, report_dt, fin_cont_month,"Forward","Financial");
						
						if(!VTEMP_EFF_PRICE.elementAt(i).equals("")) //THIS IS SPECIAL FOR MULTI LEG
						{
							eff_rate=Double.parseDouble(""+VTEMP_EFF_PRICE.elementAt(i));
							eff_rate_info=""+VTEMP_EFF_PRICE_INFO.elementAt(i);
						}
						else
						{
							eff_rate=((((count_u * fin_fwd_price) + (count_r * settle_price))/(count_r+count_u)) * Double.parseDouble(slope)) + Double.parseDouble(constant);
							if(Double.isNaN(eff_rate))
							{
								eff_rate=0;
							}
							eff_rate_info="= {((#Fwd. days * Fwd. Price) + (#Settled days * Settle Price)) / (#Fwd. days + #Settled days)} * slope + const.\n"
									+ " = {(( "+count_u+" * "+nf2.format(fin_fwd_price)+" ) + ("+count_r+" * "+nf2.format(settle_price)+" )) / ( "+count_u+" + "+count_r+")} * "+slope+" + "+constant+"\n"
									+ " = "+nf2.format(eff_rate);
							eff_rate_info+="\n\n= "+nf2.format(eff_rate)+" + "+nf2.format(weighted_average)+"(Charges)";
							eff_rate+=weighted_average;
							eff_rate_info+="\n= "+nf2.format(eff_rate);
							
						}
					}
					else
					{
						if(cont_type.equals("Z")) //MAKING ORI FIN EXPO AS ZERO(0) FRO STORAGE AS DISCUSSED WITH RS ON 20240919
						{
							fin_ru="";
							fin_expo_ori=nf.format(0);
						}
						else 
						{
							fin_ru="R";
							if(Double.doubleToRawLongBits(Double.parseDouble(phy_expo_ori))!=Double.doubleToRawLongBits(0))
							{
								fin_expo_ori=nf.format(Double.parseDouble(phy_expo_ori) * -1);
							}
							else
							{
								fin_expo_ori=nf.format(0);
							}
						}
						
						fin_expo_ori_info+="Original Exposure (Physical Leg) * (-1) \n = "+fin_expo_ori;
						fin_expo_u_info+="ZERO \n = "+fin_expo_u;
						
						eff_rate=Double.parseDouble(variable_rate);
						eff_rate_info=" = "+nf2.format(eff_rate);
					}
					
					var_eff_rate=eff_rate;
				}
			}
			rset2.close();
			stmt2.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return var_eff_rate;
	}
	
	/*public double ExposureCalculation()
	{
		String function_nm="ExposureCalculation()";
		double eff_rate=0;
		try
		{
			double tot_dcq=0;
			double tot_actual_qty=0;
			
			double tot_total=0;
			double tot_phy_expo_realized=0;
			double tot_phy_expo_unrealized=0;
			
			double tot_phy_expo_ori=0;
			double tot_fin_expo_ori=0;
			
			double tot_fin_expo_realized=0;
			double tot_fin_expo_unrealized=0;
			
			double tot_phy_unrealized_leg=0;
			double tot_fin_unrealized_leg=0;
			double tot_fin_realized_leg=0;
			
			int sr=0;
			String queryString2="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
					+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
					+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
					+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, start_dt);
			stmt2.setString(2, end_dt);
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				String gas_dt=rset2.getString(1)==null?"":rset2.getString(1);
				String cont_month="01/"+gas_dt.substring(3,gas_dt.length());
				
				String contPriceMapping=counterparty_cd+"-"+agmt_no+"-"+cont_no;
				
				double temp_var_dcq=Double.parseDouble(quantity);
				
				double actual_qty=0;
				double temp_actual_qty=0;
				double weighted_average=0;
				//if(contract_type.equals("V"))
				{
					temp_actual_qty=temp_var_dcq;
					actual_qty=temp_var_dcq;
				}
				
				temp_actual_qty=actual_qty;
				
				String variable_rate="";
				String price_type="";
				String curve_nm="";
				String slope="1";
				String constant="0";
				String phys_curve_nm="";
				String price_range="";
				String price_day="";
				String price_start_dt="";
				String price_end_dt="";
				String curve_logic="";
				String price_seq_no="";
				String formula_desc="";
				
				String queryString1="SELECT RATE,RATE_UNIT,PRICE_TYPE,CURVE_NM,SLOPE,CONST,PHYS_CURVE_NM,"
						+ "PRICE_RANGE,TO_CHAR(PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(PRICE_END_DT,'DD/MM/YYYY'),"
						+ "CURVE_LOGIC,SEQ_NO,FORMULA "
						+ "FROM FMS_CONT_PRICE_DTL "
						+ "WHERE COMPANY_CD=? AND MAPPING_ID=? AND CONTRACT_TYPE=? "
						+ "AND FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND TO_DT>=TO_DATE(?,'DD/MM/YYYY')";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, contPriceMapping);
				stmt1.setString(3, cont_type);
				stmt1.setString(4, gas_dt);
				stmt1.setString(5, gas_dt);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					variable_rate=nf.format(rset1.getDouble(1));
					String var_rate_unit=rset1.getString(2)==null?"":rset1.getString(2);
					price_type=rset1.getString(3)==null?"F":rset1.getString(3);
					curve_nm=rset1.getString(4)==null?"":rset1.getString(4);
					slope=rset1.getString(5)==null?"1":rset1.getString(5);
					constant=rset1.getString(6)==null?"0":rset1.getString(6);
					phys_curve_nm=rset1.getString(7)==null?"":rset1.getString(7);
					price_range=rset1.getString(8)==null?"A":rset1.getString(8);
					price_start_dt=rset1.getString(9)==null?"":rset1.getString(9);
					price_end_dt=rset1.getString(10)==null?"":rset1.getString(10);
					curve_logic=rset1.getString(11)==null?"":rset1.getString(11);
					price_seq_no=rset1.getString(12)==null?"":rset1.getString(12);
					formula_desc=rset1.getString(13)==null?"":rset1.getString(13);
					
				}
				rset1.close();
				stmt1.close();
				
				String cont_price="";
				if(cont_price.equals(""))
				{
					cont_price=rate;
				}
				cont_price=nf2.format(Double.parseDouble(cont_price)+weighted_average);
				
				if(variable_rate.equals(""))
				{
					variable_rate=cont_price;
				}
				else
				{
					variable_rate=nf2.format(Double.parseDouble(variable_rate)+weighted_average);
				}
				
				Vector VTEMP_DCQ = new Vector();
				Vector VTEMP_ALLOC_QTY= new Vector();
				Vector VTEMP_CURVE = new Vector();
				Vector VTEMP_CURVE_NM = new Vector();
				Vector VTEMP_CURVE_EOD = new Vector();
				Vector VTEMP_FIN_MMYYYY= new Vector();
				Vector VTEMP_SETTLE_START_DT= new Vector();
				Vector VTEMP_SETTLE_END_DT= new Vector();
				Vector VTEMP_FIN_CONT_MONTH= new Vector();
				Vector VTEMP_SLOPE = new Vector();
				Vector VTEMP_CONST= new Vector();
				Vector VTEMP_FIN_RU= new Vector();
				Vector VTEMP_PHY_RU= new Vector();
				Vector VTEMP_SETTLE_PRICE= new Vector();
				Vector VTEMP_AVG_SETTLE_PRICE= new Vector();
				Vector VTEMP_EFF_PRICE= new Vector();
				Vector VTEMP_EFF_PRICE_INFO= new Vector();
				
				
				double avgSettlePrice=0;
				double avgPrice=0;
				String avgPriceInfo="";
				
				int phyCountRu=dateUtil.getDays(gas_dt, report_dt);
				String phyRu="";
				if(phyCountRu<=1)
				{
					phyRu="R";
				}
				else
				{
					phyRu="U";
				}
				String var_dcq=""+temp_var_dcq;
				//if(contract_type.equals("V"))
				{
					//FIXED SIDE
					VTEMP_CURVE.add("Fixed");
					VTEMP_CURVE_EOD.add("Fixed");
					VTEMP_CURVE_NM.add("");
					VTEMP_FIN_MMYYYY.add("");
					VTEMP_SETTLE_START_DT.add("");
					VTEMP_SETTLE_END_DT.add("");
					VTEMP_FIN_CONT_MONTH.add("");
					VTEMP_SLOPE.add("1");
					VTEMP_CONST.add("0");
					VTEMP_DCQ.add(var_dcq);
					VTEMP_ALLOC_QTY.add(nf.format(actual_qty));
					
					VTEMP_FIN_RU.add("");
					VTEMP_PHY_RU.add("");
					VTEMP_SETTLE_PRICE.add("");
					VTEMP_AVG_SETTLE_PRICE.add("");
					VTEMP_EFF_PRICE.add("");
					VTEMP_EFF_PRICE_INFO.add("");
					
					//FLOAT SIDE	
					
					if(actual_qty!=0)
					{
						actual_qty=-1*actual_qty;
					}
		
					curve_nm=derivative_curve_nm;
					VTEMP_CURVE.add("Float(SINGLE) : "+curve_nm);
					VTEMP_CURVE_EOD.add("Float(SINGLE)");
					VTEMP_CURVE_NM.add(curve_nm);
					
					Vector temp=SettlementDateCalculation(curve_nm, cont_month, price_range, price_start_dt, price_end_dt);
					VTEMP_FIN_MMYYYY.add(temp.elementAt(0));
					VTEMP_SETTLE_START_DT.add(temp.elementAt(1));
					VTEMP_SETTLE_END_DT.add(temp.elementAt(2));
					VTEMP_FIN_CONT_MONTH.add(temp.elementAt(4));
					VTEMP_SLOPE.add(slope);
					VTEMP_CONST.add(constant);
					VTEMP_DCQ.add(var_dcq);
					VTEMP_ALLOC_QTY.add(nf.format(actual_qty));
					VTEMP_FIN_RU.add("");
					VTEMP_PHY_RU.add("");
					VTEMP_SETTLE_PRICE.add("");
					VTEMP_AVG_SETTLE_PRICE.add("");
					VTEMP_EFF_PRICE.add("");
					VTEMP_EFF_PRICE_INFO.add("");
				}
				
				if(curve_logic.equals("MIN") || curve_logic.equals("MAX"))// && !VTEMP_FIN_RU.contains("U"))
				{
					int min_index=0;
					double price=Double.parseDouble(""+VTEMP_EFF_PRICE.elementAt(min_index));
					for(int i=1;i<VTEMP_EFF_PRICE.size();i++)
					{	
						if(curve_logic.equals("MAX"))
						{
							if(Double.parseDouble(""+VTEMP_EFF_PRICE.elementAt(i)) > price)
							{
								price=Double.parseDouble(""+VTEMP_EFF_PRICE.elementAt(i));
								min_index=i;
							}
						}
						else
						{
							if(Double.parseDouble(""+VTEMP_EFF_PRICE.elementAt(i)) < price)
							{
								price=Double.parseDouble(""+VTEMP_EFF_PRICE.elementAt(i));
								min_index=i;
							}
						}
					}
					
					for(int i=0;i<VTEMP_EFF_PRICE.size();i++)
					{
						if(VTEMP_PHY_RU.contains("U"))
						{
							if(min_index==i)
							{
								VTEMP_DCQ.remove(i);
								VTEMP_DCQ.add(i, nf.format(Double.parseDouble(var_dcq)));
							}
							else
							{
								VTEMP_DCQ.remove(i);
								VTEMP_DCQ.add(i, nf.format(0));
							}
						}
						else
						{
							if(min_index==i)
							{
								VTEMP_ALLOC_QTY.remove(i);
								VTEMP_ALLOC_QTY.add(i, nf.format(actual_qty));
							}
							else
							{
								VTEMP_ALLOC_QTY.remove(i);
								VTEMP_ALLOC_QTY.add(i, nf.format(0));
							}
						}
					}
				}
				
				for(int i=0;i<VTEMP_CURVE.size();i++)
				{
					curve_nm=""+VTEMP_CURVE_NM.elementAt(i);
					slope=""+VTEMP_SLOPE.elementAt(i);
					constant=""+VTEMP_CONST.elementAt(i);
					
					
					var_dcq=""+VTEMP_DCQ.elementAt(i);
					actual_qty=Double.parseDouble(""+VTEMP_ALLOC_QTY.elementAt(i));
					
					VPRICE_TYPE.add(VTEMP_CURVE.elementAt(i));
					
					double settle_price=0;
					String fin_mmyyyy=""+VTEMP_FIN_MMYYYY.elementAt(i);
					String fin_cont_month=""+VTEMP_FIN_CONT_MONTH.elementAt(i);
					String settle_start_dt=""+VTEMP_SETTLE_START_DT.elementAt(i);
					String settle_end_dt=""+VTEMP_SETTLE_END_DT.elementAt(i);
					String settle_dt="";
					int phy_count_ru=0;
					int fin_count_ru=0;
					
					String eff_rate_info="";
					
					String phy_expo_ori="";
					String fin_expo_ori="";
					
					phy_count_ru=dateUtil.getDays(gas_dt, report_dt);
					String phy_ru="";
					String phy_expo_r="";
					String phy_expo_u="";
					
					String fin_ru="";
					String fin_expo_r="";
					String fin_expo_u=nf.format(0);
					
					double fin_fwd_price=0;
					
					if(phy_count_ru<=1)
					{
						phy_ru="R";
						phy_expo_ori=nf.format(actual_qty);
						phy_expo_u=nf.format(0);
					}
					else
					{
						phy_ru="U";
						phy_expo_ori=nf.format(Double.parseDouble(var_dcq));
						phy_expo_u= phy_expo_ori;
					}
					
					if(price_type.equals("M"))
					{
						fin_count_ru=dateUtil.getDays(settle_end_dt, report_dt);
						
						int count_u=0;
						int count_r=0;
						
						// NOTE: This is actual formula given by Kris. But we found some issue with specific case when we switched to 
						// U and R specific codes which are currently commented
						if(Double.parseDouble(phy_expo_ori)!=0)
						{
							fin_expo_ori=nf.format(Double.parseDouble(phy_expo_ori) * -1 * Double.parseDouble(slope));
						}
						else
						{
							fin_expo_ori=nf.format(0);
						}
						
						
						fin_ru="R";
						if(fin_count_ru>1)
						{	
							fin_ru="U";
							int isSettlmentPeriodActive=dateUtil.isDateWithinPeriod(settle_start_dt,settle_end_dt,report_dt);
							
							if(isSettlmentPeriodActive==1)
							{
								String nextdt=dateUtil.getDate(report_dt, "1"); 
								count_u=getPriceCurveWorkingDays(curve_nm, nextdt, settle_end_dt);
								count_r=getPriceCurveWorkingDays(curve_nm, settle_start_dt, report_dt); 	
							}
							else
							{
								count_u=getPriceCurveWorkingDays(curve_nm, settle_start_dt, settle_end_dt);
								count_r=0;
							}
							
							double temp_dcq=Double.parseDouble(fin_expo_ori)/(count_r+count_u);
							fin_expo_u=nf.format(temp_dcq * count_u);						
						}
						else
						{
							count_r=getPriceCurveWorkingDays(curve_nm, settle_start_dt, settle_end_dt);
						}
						
						if(curve_nm.equals("PPAC"))
						{
							settle_price=getForwardPrice(curve_nm, report_dt, fin_cont_month,"Forward","Financial");
							
						}
						else
						{
							settle_price=getSettledPrice(curve_nm, settle_start_dt, settle_end_dt);
						}
						fin_fwd_price=getForwardPrice(curve_nm, report_dt, fin_cont_month,"Forward","Financial");
						
						//eff_rate=(settle_price * Double.parseDouble(slope)) + Double.parseDouble(constant);
						eff_rate=((((count_u * fin_fwd_price) + (count_r * settle_price))/(count_r+count_u)) * Double.parseDouble(slope)) + Double.parseDouble(constant);
						if(Double.isNaN(eff_rate))
						{
							eff_rate=0;
						}
						eff_rate_info="= {((#Fwd. days * Fwd. Price) + (#Settled days * Settle Price)) / (#Fwd. days + #Settled days)} * slope + const.\n"
								+ " = {(( "+count_u+" * "+nf2.format(fin_fwd_price)+" ) + ("+count_r+" * "+nf2.format(settle_price)+" )) / ( "+count_u+" + "+count_r+")} * "+slope+" + "+constant+"\n"
								+ " = "+nf2.format(eff_rate);
						eff_rate_info+="\n\n= "+nf2.format(eff_rate)+" + "+nf2.format(weighted_average)+"(Charges)";
						eff_rate+=weighted_average;
						eff_rate_info+="\n= "+nf2.format(eff_rate);
					}
				}
			}
			rset2.close();
			stmt2.close();
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return eff_rate;
	}*/
	
	public Double getSettledPrice(String curve_nm, String date1, String date2)
	{
		String function_nm="getSettledPrice()";
		double price=0;
		try
		{
			String queryString_temp="SELECT AVG(SETTLE_PRICE) "
					+ "FROM FMS_SPOT_PRICE_DTL "
					+ "WHERE ACTUAL_CURVE=? "
					+ "AND SETTLE_PRICE IS NOT NULL AND SETTLE_PRICE > 0 AND CURVE_TYPE=? "
					+ "AND TO_CHAR(CURVE_DT,'DD/MM/YYYY') IN (SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY')+RNUM,'DD/MM/YYYY') "
					+ "FROM (SELECT ROWNUM-1 RNUM FROM ALL_OBJECTS WHERE  ROWNUM <= TO_DATE(?,'DD/MM/YYYY')-TO_DATE(?,'DD/MM/YYYY')+1) "
					+ "WHERE TO_CHAR(TO_DATE(?,'DD/MM/YYYY')+RNUM,'DY') NOT IN ('SAT','SUN')) "
					+ "AND TO_CHAR(CURVE_DT,'DD/MM/YYYY') NOT IN (SELECT TO_CHAR(HOLIDAY_DT,'DD/MM/YYYY') FROM FMS_CURVE_HOLIDAY_CALND "
					+ "WHERE CURVE_NM=? AND STATUS=? "
					+ "AND HOLIDAY_DT>=TO_DATE(?,'DD/MM/YYYY') AND HOLIDAY_DT<=TO_DATE(?,'DD/MM/YYYY'))";
			stmt_temp=conn.prepareStatement(queryString_temp);
			stmt_temp.setString(1, curve_nm);
			stmt_temp.setString(2, "Spot");
			stmt_temp.setString(3, date1);
			stmt_temp.setString(4, date2);
			stmt_temp.setString(5, date1);
			stmt_temp.setString(6, date1);
			stmt_temp.setString(7, curve_nm);
			stmt_temp.setString(8, "Y");
			stmt_temp.setString(9, date1);
			stmt_temp.setString(10, date2);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				price=rset_temp.getDouble(1);
			}
			rset_temp.close();
			stmt_temp.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return price;
	}
	
	public Double getForwardPrice(String curve_nm, String report_dt, String cont_month, String curve_type,String phys_fin)
	{
		String function_nm="getForwardPrice()";
		double price=0;
		try
		{
			String queryString_temp="SELECT SETTLE_PRICE "
					+ "FROM FMS_FORWARD_PRICE_DTL A	"
					+ "WHERE CURVE_DT=TO_DATE(?,'DD/MM/YYYY') AND CURVE_NM=? "
					+ "AND PHYS_FIN=? AND CURVE_TYPE=? "
					+ "AND REPORT_DT=(SELECT MAX(REPORT_DT) FROM FMS_FORWARD_PRICE_DTL B "
					+ "WHERE B.REPORT_DT<=TO_DATE(?,'DD/MM/YYYY'))";
			stmt_temp=conn.prepareStatement(queryString_temp);
			stmt_temp.setString(1, cont_month);
			stmt_temp.setString(2, curve_nm);
			stmt_temp.setString(3, phys_fin);
			stmt_temp.setString(4, curve_type);
			stmt_temp.setString(5, report_dt);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				price=rset_temp.getDouble(1);
			}
			rset_temp.close();
			stmt_temp.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return price;
	}
	
	public int getPriceCurveWorkingDays(String curve_nm, String date1, String date2)
	{
		String function_nm="getPriceCurveWorkingDays()";
		int days=0;
		try
		{
			String queryString_temp="SELECT COUNT(*) "
					+ "FROM (SELECT ROWNUM-1 RNUM FROM ALL_OBJECTS WHERE  ROWNUM <= TO_DATE(?,'DD/MM/YYYY')-TO_DATE(?,'DD/MM/YYYY')+1) "
					+ "WHERE TO_CHAR(TO_DATE(?,'DD/MM/YYYY')+RNUM,'DY') NOT IN ('SAT','SUN') "
					+ "AND TO_CHAR(TO_DATE(?,'DD/MM/YYYY')+RNUM,'DD/MM/YYYY') NOT IN (SELECT TO_CHAR(HOLIDAY_DT,'DD/MM/YYYY') FROM FMS_CURVE_HOLIDAY_CALND "
					+ "WHERE CURVE_NM=? AND STATUS=? AND HOLIDAY_DT>=TO_DATE(?,'DD/MM/YYYY') AND HOLIDAY_DT<=TO_DATE(?,'DD/MM/YYYY'))";
			stmt_temp=conn.prepareStatement(queryString_temp);
			stmt_temp.setString(1, date2);
			stmt_temp.setString(2, date1);
			stmt_temp.setString(3, date1);
			stmt_temp.setString(4, date1);
			stmt_temp.setString(5, curve_nm);
			stmt_temp.setString(6, "Y");
			stmt_temp.setString(7, date1);
			stmt_temp.setString(8, date2);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				days=rset_temp.getInt(1);
			}
			rset_temp.close();
			stmt_temp.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return days;
	}
	
	public Vector SettlementDateCalculation(String curve_nm,String cont_month,String price_range, String price_start_dt, String price_end_dt)
	{
		String function_nm="SettlementDateCalculation()";
		Vector temp=new Vector();
		try
		{
			String fin_mmyyyy="";
			String fin_cont_month="";
			String settle_start_dt="";
			String settle_end_dt="";
			String settle_dt="";
			
			String queryString_temp="SELECT TO_CHAR(CONT_MONTH,'MM/YYYY'),TO_CHAR(SETTLE_START_DT,'DD/MM/YYYY'),TO_CHAR(SETTLE_END_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(SETTLE_DT,'DD/MM/YYYY'),TO_CHAR(CONT_MONTH,'DD/MM/YYYY') "
					+ "FROM FMS_CURVE_SETTLE_CALND "
					+ "WHERE CURVE_NM=? "
					+ "AND CONT_MONTH=TO_DATE(?,'DD/MM/YYYY')";
			stmt_temp=conn.prepareStatement(queryString_temp);
			stmt_temp.setString(1, curve_nm);
			stmt_temp.setString(2, cont_month);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				fin_mmyyyy=rset_temp.getString(1)==null?"":rset_temp.getString(1);
				settle_start_dt=rset_temp.getString(2)==null?"":rset_temp.getString(2);
				settle_end_dt=rset_temp.getString(3)==null?"":rset_temp.getString(3);
				settle_dt=rset_temp.getString(4)==null?"":rset_temp.getString(4);
				fin_cont_month=rset_temp.getString(5)==null?"":rset_temp.getString(5);
			}
			rset_temp.close();
			stmt_temp.close();
			
			if(price_range.length()>1 && price_range.startsWith("O"))
			{
				String avgDay=price_range.substring(1,price_range.length());
				avgDay=""+(-1*(Integer.parseInt(avgDay)-1));
				settle_start_dt=dateUtil.getDate(settle_dt, avgDay);
				settle_end_dt=settle_dt;
			}
			else if(price_range.equals("F"))
			{
				settle_start_dt=settle_dt;
				settle_end_dt=settle_dt;
			}
			else if(price_range.equals("D") && !price_start_dt.equals("") && !price_end_dt.equals(""))
			{
				settle_start_dt=price_start_dt;
				settle_end_dt=price_end_dt;
			}
			
			temp.add(fin_mmyyyy);
			temp.add(settle_start_dt);
			temp.add(settle_end_dt);
			temp.add(settle_dt);
			temp.add(fin_cont_month);
		}
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return temp;
	}
	
	public void getMtmList()
	{
		String function_nm="getMtmList()";
		try
		{
			String report_end_dt="";
			String report_start_dt="";
			if(!report_dt.equals(""))
			{
				String split_dt[]=report_dt.split("/");
				month=split_dt[1];
				year=split_dt[2];
				
				report_start_dt=""+dateUtil.getFirstDateOfMonth(month, year);
				report_end_dt=""+dateUtil.getLastDateOfMonth(month, year);
			}
			
			fy_year=dateUtil.getFinancialYear(report_dt);
			if(!cont_mapp.equals(""))
			{
				String[] cont_split = cont_mapp.split("-");
				cont_type=cont_split[0];
				agmt_no=cont_split[1];
				cont_no=cont_split[2];
			}
			
			int count=0;
			double tmp_tot_gross_amt=0;
			double tmp_tot_buy_amt=0;
			double tmp_tot_sell_amt=0;
			double tmp_tot_qty=0;
			String queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,A.CONT_NO,A.CONT_REV,A.CONT_NAME,A.CONT_REF_NO,"
					+ "B.INSTRUMENT_NO,B.INSTRUMENT_TYPE,B.BUY_SELL,B.STATUS,B.QTY,B.QTY_UNIT,B.RATE,B.RATE_UNIT,"
					+ "B.PRODUCT_NM,B.CURVE_NM,B.PROJ_METHOD,TO_CHAR(B.CONT_DD_MM_YR,'DD/MM/YYYY'),"
					+ "TO_CHAR(B.PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(B.PRICE_END_DT,'DD/MM/YYYY'),B.CONV_FACTOR,TO_CHAR(A.TRADE_DT,'DD/MM/YYYY'),C.PLANT_SEQ_NO,D.PLANT_SEQ_NO AS BU_PLANT,"
					+ "E.INVOICE_CUR_CD,E.EXCHNG_RATE_CD,E.EXCHNG_RATE_CAL,E.EXCHG_VAL,E.HOLIDAY_STATE,E.DUE_DATE,E.DUE_DT_IN,E.EXCLUDE_SAT "
					+ "FROM FMS_DERV_CONT_MST A,"
					+ "FMS_DERV_INSTRUMENT_MST B,"
					+ "FMS_DERV_CONT_PLANT C,"
					+ "FMS_DERV_CONT_BU D, "
					+ "FMS_DERV_CONT_BILLING_DTL E "
					+ "WHERE A.COMPANY_CD=? ";
			if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
			{
				queryString+="AND A.COUNTERPARTY_CD=? ";
			}
			if(!cont_mapp.equals(""))
			{
				queryString+="AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? ";
			}
				queryString+= "AND B.PRICE_END_DT>=TO_DATE(?,'DD/MM/YYYY') AND B.STATUS='Y' AND A.CONT_STATUS!='X' "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO "
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.AGMT_TYPE=C.AGMT_TYPE AND A.AGMT_NO=C.AGMT_NO "
					+ "AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.CONT_NO=C.CONT_NO "
					+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.AGMT_TYPE=D.AGMT_TYPE AND A.AGMT_NO=D.AGMT_NO "
					+ "AND A.CONTRACT_TYPE=D.CONTRACT_TYPE AND A.CONT_NO=D.CONT_NO "
					+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.AGMT_TYPE=E.AGMT_TYPE AND A.AGMT_NO=E.AGMT_NO "
					+ "AND A.CONTRACT_TYPE=E.CONTRACT_TYPE AND A.CONT_NO=E.CONT_NO AND C.PLANT_SEQ_NO=E.PLANT_SEQ_NO";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(++count, comp_cd);
			if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
			{
				stmt.setString(++count, counterparty_cd);
			}
			if(!cont_mapp.equals(""))
			{
				stmt.setString(++count, agmt_no);
				stmt.setString(++count, cont_no);
				stmt.setString(++count, cont_type);
			}
			stmt.setString(++count, report_dt);
			//stmt.setString(++count, report_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				counterparty_cd=countpty_cd;
				String agmtno=rset.getString(3)==null?"0":rset.getString(3);
				agmt_no=agmtno;
				String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
				cont_type=rset.getString(5)==null?"":rset.getString(5);
				String contno=rset.getString(6)==null?"0":rset.getString(6);
				cont_no=contno;
				String contrev=rset.getString(7)==null?"0":rset.getString(7);
				String contRef=rset.getString(9)==null?"":rset.getString(9);
				String instrument_no=rset.getString(10)==null?"":rset.getString(10);
				String buy_sell=rset.getString(12)==null?"":rset.getString(12);
				account=buy_sell;
				
				String cont_month_year=rset.getString(21)==null?"":rset.getString(21);
				String cont_start_dt=rset.getString(22)==null?"":rset.getString(22);
				String cont_end_dt=rset.getString(23)==null?"":rset.getString(23);
				start_dt=cont_start_dt;
				end_dt=cont_end_dt;
				
				String split_dt[]=cont_month_year.split("/");
				String month=split_dt[1];
				String year=split_dt[2];
				
				String month_nm="";
				if(month.equals("01")) {month_nm="January";}
				else if(month.equals("02")) {month_nm="February";}
				else if(month.equals("03"))	{month_nm="March";}
				else if(month.equals("04")) {month_nm="April";}
				else if(month.equals("05")) {month_nm="May";}
				else if(month.equals("06")) {month_nm="June";}
				else if(month.equals("07")) {month_nm="July";}
				else if(month.equals("08")) {month_nm="August";}
				else if(month.equals("09")) {month_nm="September";}
				else if(month.equals("10")) {month_nm="October";}
				else if(month.equals("11")) {month_nm="November";}
				else if(month.equals("12")) {month_nm="December";}
				
				String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, instrument_no);
				
				String due_days=rset.getString(33)==null?"":rset.getString(33);
				String consider_due_dt_in=rset.getString(34)==null?"":rset.getString(34);
				String exclude_sat=rset.getString(35)==null?"":rset.getString(35);
				
				double qty=rset.getDouble(14);
				quantity=nf.format(qty);
				String qty_unit_cd=rset.getString(15)==null?"":rset.getString(15);
				String qty_unit = utilBean.getEnergyUnitNm(conn, qty_unit_cd);
				//String price=rset.getString(20)==null?"":rset.getString(20);
				String price_unit=rset.getString(17)==null?"":rset.getString(17);
				String price_unit_nm=utilBean.getRateUnitNm(conn,price_unit);
				String product_nm = rset.getString(18)==null?"":rset.getString(18);
				String curve_nm = rset.getString(19)==null?"":rset.getString(19);
				derivative_curve_nm=curve_nm;
				double price = Double.parseDouble(nf2.format(rset.getDouble(16)));
				rate=""+price;
				
				double float_price=ExposureCalculation();
				BigDecimal float_val=new BigDecimal(""+float_price) ;
				float_price=Double.parseDouble(df3.format(float_val));
				
				double sell_amt=0;
				double buy_amt=0;
				
				if(buy_sell.equals("BUY"))
				{
					sell_amt=qty*float_price;
					buy_amt=qty*price;
				}
				else
				{
					buy_amt=qty*float_price;
					sell_amt=qty*price;
				}
				
				String inv_typ_flg="";
				double totalamt=sell_amt-buy_amt;
				if(totalamt<0)
				{
					inv_typ_flg="R";
					//totalamt=totalamt*(-1);
				}
				else
				{
					inv_typ_flg="I";
				}
				String invoice_raise_in=rset.getString(28)==null?"2":rset.getString(28);
				
				String holiday_state = rset.getString(32)==null?"":rset.getString(32);
				
				String plant_seq = rset.getString(26)==null?"":rset.getString(26);
				String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "T");
				
				String bu_plant_seq = rset.getString(27)==null?"":rset.getString(27);
				String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
				
				String temp_st_dt="";
				String temp_end_dt="";
				String diff_color="";
				temp_st_dt=period_start_dt;
				temp_end_dt=period_end_dt;
				diff_color="";
				
				if(dateUtil.getDays(cont_end_dt, temp_end_dt)<=0)
				{
					temp_end_dt=cont_end_dt;
					diff_color="blue";
				}
				
				String state_code=utilBean.getState_TIN(conn, own_cd, own_cd, "B", bu_plant_seq);
				
				int isInvExist=0;
				queryString3="SELECT COUNT(*) "
						+ "FROM FMS_DERV_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND AGMT_NO=? AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_UNIT=? "
						+ "AND BU_STATE_TIN=? "
						+ "AND FINANCIAL_YEAR=? AND INSTRUMENT_NO=? AND PDF_INV_DTL IS NOT NULL AND INV_FLAG='F'";
				stmt0 = conn.prepareStatement(queryString3);
				stmt0.setString(1, own_cd);
				stmt0.setString(2, countpty_cd);
				stmt0.setString(3, contno);
				stmt0.setString(4, agmtno);
				stmt0.setString(5, plant_seq);
				stmt0.setString(6, cont_type);
				stmt0.setString(7, bu_plant_seq);
				stmt0.setString(8, state_code);
				stmt0.setString(9, fy_year);
				stmt0.setString(10, instrument_no);
				rset0=stmt0.executeQuery();
				if(rset0.next())
				{
					isInvExist=rset0.getInt(1);
				}
				rset0.close();
				stmt0.close();
				
				if(isInvExist==0)
				{
					if(qty>0)
					{
						VBU_STATE_TIN.add(state_code);							
						VFIN_YEAR.add(fy_year);
						VCOUNTERPARTY_CD.add(countpty_cd);
						VAGMT_NO.add(agmtno);
						VAGMT_REV.add(agmtrev);
						VCONT_NO.add(contno);
						VCONT_REV.add(contrev);
						VINSTRUMENT_NO.add(instrument_no);
						VCONT_TYPE.add(cont_type);
						VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
						VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
						VCONT_START_DT.add(cont_start_dt);
						VCONT_END_DT.add(cont_end_dt);
						VDEAL_MAPPING.add(deal_no);
						VCONT_REF.add(contRef);
						VPLANT_SEQ.add(plant_seq);
						VPLANT_ABBR.add(plant_abbr);
						VBU_PLANT_SEQ.add(bu_plant_seq);
						VBU_PLANT_ABBR.add(bu_plant_abbr);
						VPERIOD_START_DT.add(cont_start_dt);
						VPERIOD_END_DT.add(cont_end_dt);
						VBILLING_FREQ_FLAG.add(billing_cycle);
						VBILLING_FREQ.add("");
						VPRODUCTION_MONTH.add(month+"/"+year);
						VINVOICE_DUE_DT.add(utilBean.DueDateCalculation(conn,temp_end_dt, due_days,consider_due_dt_in,exclude_sat,comp_cd,holiday_state));
						
						VCASH_FLOW.add("Derivatives");
							
						VACCRUAL_QTY.add(nf.format(qty));
						VQTY_UNIT.add(qty_unit);
						VQTY_UNIT_CD.add(qty_unit_cd);
						VACCRUAL_AMT.add(nf.format(totalamt));
						VSELL_PRICE_CD.add(price_unit);
						VSELL_PRICE_NM.add(price_unit_nm);
						VINVOICE_RAISED_IN.add(invoice_raise_in);
						
						VGROSS_AMT.add(nf.format(totalamt));
						VBUY_SELL.add(buy_sell);
						VINVOICE_TYPE.add(inv_typ_flg);
						VSELL_AMT.add(nf.format(sell_amt));
						VBUY_AMT.add(nf.format(buy_amt));
						VDEAL_PROD_NM.add(product_nm);
						VFIXED_PRICE.add(nf2.format(price));
						VFLOAT_PRICE.add(nf3.format(float_price));
						VMONTH.add(month_nm);
						VYEAR.add(year);
						
						tmp_tot_qty += qty;
						tmp_tot_gross_amt += totalamt;
						tmp_tot_buy_amt += buy_amt;
						tmp_tot_sell_amt += sell_amt;
					}
				}
			}
			rset.close();
			stmt.close();
			
			tot_qty=nf.format(tmp_tot_qty);
			tot_gross_amt=nf.format(tmp_tot_gross_amt);
			tot_buy_amt=nf.format(tmp_tot_buy_amt);
			tot_sell_amt=nf.format(tmp_tot_sell_amt);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getAccrualCounterpartyList()
	{
		String function_nm="getAccrualCounterpartyList()";
		try
		{
			int stcount=0;
			if(freez_count>0)
			{
				queryString="SELECT DISTINCT COUNTERPARTY_CD "
						+ "FROM FMS_DERV_ACCRUAL_DTL "
						+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') ";
					if(callFlag.equalsIgnoreCase("DERV_ACCRUAL_ACCOUNTING"))
					{
						queryString+="AND IS_MTM NOT IN ('Y') ";
					}
					else if(callFlag.equalsIgnoreCase("DERV_MTM_ACCRUAL_ACCOUNTING"))
					{
						queryString+="AND IS_MTM='Y' ";
					}
			}
			else
			{
				queryString="SELECT DISTINCT COUNTERPARTY_CD "
						+ "FROM FMS_DERV_CONT_MST "
						+ "WHERE COMPANY_CD=?";
			}
			stmt = conn.prepareStatement(queryString);
			if(freez_count>0)
			{
				stmt.setString(++stcount, comp_cd);
				stmt.setString(++stcount, report_dt);
			}
			else
			{
				stmt.setString(++stcount, comp_cd);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String cd = rset.getString(1)==null?"":rset.getString(1);
				VMST_COUNTERPARTY_CD.add(cd);
				VMST_COUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,cd));
				VMST_COUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,cd));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getAccrualActiveContractList()
	{
		String function_nm="getAccrualActiveContractList()";
		try
		{
			if(freez_count>0)
			{
				String queryString="SELECT DISTINCT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CONT_REF_NO "
						+ "FROM FMS_DERV_ACCRUAL_DTL "
						+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND COUNTERPARTY_CD=? ";
				if(callFlag.equalsIgnoreCase("DERV_ACCRUAL_ACCOUNTING"))
				{
					queryString+="AND IS_MTM NOT IN ('Y') ";
				}
				else if(callFlag.equalsIgnoreCase("DERV_MTM_ACCRUAL_ACCOUNTING"))
				{
					queryString+="AND IS_MTM='Y' ";
				}
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, report_dt);
				stmt.setString(3, counterparty_cd);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String agmtno=rset.getString(1)==null?"0":rset.getString(1);
					String agmtrev=rset.getString(2)==null?"0":rset.getString(2);
					String contno=rset.getString(3)==null?"0":rset.getString(3);
					String contrev=rset.getString(4)==null?"0":rset.getString(4);
					String cont_type=rset.getString(5)==null?"":rset.getString(5);
					String cont_ref=rset.getString(6)==null?"":rset.getString(6);
				
					String cont_map=cont_type+"-"+agmtno+"-"+contno;
					String dealNo=utilBean.getDisplayDealMapping(agmtno, agmtrev, contno, contrev, cont_type);
					if(!cont_ref.equals(""))
					{
						dealNo+=" ["+cont_ref+"]";
					}
					
					VCONT_MAP_LIST.add(cont_map);
					VDIS_CONT_MAP_LIST.add(dealNo);
				}
				rset.close();
				stmt.close();
			}
			else
			{
				String queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_TYPE,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,A.CONT_NO,A.CONT_REV,A.CONT_NAME,A.CONT_REF_NO,"
						+ "B.INSTRUMENT_NO,B.INSTRUMENT_TYPE,B.BUY_SELL,B.STATUS,B.QTY,B.QTY_UNIT,B.RATE,B.RATE_UNIT,"
						+ "B.PRODUCT_NM,B.CURVE_NM,B.PROJ_METHOD,TO_CHAR(B.CONT_DD_MM_YR,'DD/MM/YYYY'),"
						+ "TO_CHAR(B.PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(B.PRICE_END_DT,'DD/MM/YYYY'),B.CONV_FACTOR,TO_CHAR(A.TRADE_DT,'DD/MM/YYYY') "
						+ "FROM FMS_DERV_CONT_MST A,FMS_DERV_INSTRUMENT_MST B "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? ";
					queryString+= "AND B.PRICE_END_DT<=TO_DATE(?,'DD/MM/YYYY') AND B.STATUS='Y' "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, report_dt);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String agmtno=rset.getString(4)==null?"0":rset.getString(4);
					String agmtrev=rset.getString(5)==null?"0":rset.getString(5);
					String contno=rset.getString(7)==null?"0":rset.getString(7);
					String contrev=rset.getString(8)==null?"0":rset.getString(8);
					String cont_type=rset.getString(6)==null?"":rset.getString(6);
					String instrument_no=rset.getString(11)==null?"":rset.getString(11);
					String cont_ref=rset.getString(10)==null?"":rset.getString(10);
					
					String cont_map=cont_type+"-"+agmtno+"-"+contno;
					String dealNo=utilBean.NewDealMappingId(comp_cd,counterparty_cd,agmtno, agmtrev, contno, contrev, cont_type,instrument_no);
					if(!cont_ref.equals(""))
					{
						dealNo+=" ["+cont_ref+"]";
					}
					
					VCONT_MAP_LIST.add(cont_map);
					VDIS_CONT_MAP_LIST.add(dealNo);
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
	
	public void getAccrualFreezedData()
	{
		String function_nm="getAccrualFreezedData()";
		try
		{
			double tmp_tot_gross_amt=0;
			double tmp_tot_buy_amt=0;
			double tmp_tot_sell_amt=0;
			double tmp_tot_qty=0;
			queryString="SELECT COUNTERPARTY_CD,FINANCIAL_YEAR,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
					+ "BU_UNIT,BU_STATE_TIN,PLANT_SEQ,TO_CHAR(INVOICE_DUE_DT,'DD/MM/YYYY'),TO_CHAR(PROD_MONTH,'DD/MM/YYYY'),FREQ,"
					+ "TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),ACCRUAL_QTY,ACCRUAL_AMT,RATE_IN,"
					+ "EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),EXCHG_RATE_VALUE,INVOICE_RAISED_IN,GROSS_AMT,CONT_REF_NO,CASH_FLOW,"
					+ "TO_CHAR(CONT_START_DT,'DD/MM/YYYY'),TO_CHAR(CONT_END_DT,'DD/MM/YYYY'),TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI:SS'),"
					+ "INSTRUMENT_NO,ACCRUAL_QTY_UNIT,BUY_AMOUNT,SELL_AMOUNT,BUY_SELL,FIXED_RATE,FLOAT_RATE "
					+ "FROM FMS_DERV_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') ";
			if(callFlag.equalsIgnoreCase("DERV_ACCRUAL_ACCOUNTING"))
			{
				queryString+="AND IS_MTM NOT IN ('Y') ";
			}
			else if(callFlag.equalsIgnoreCase("DERV_MTM_ACCRUAL_ACCOUNTING"))
			{
				queryString+="AND IS_MTM='Y' ";
			}
			if(!counterparty_cd.equals(""))
			{
				queryString+="AND COUNTERPARTY_CD=? ";
			}
			stmt = conn.prepareStatement(queryString);
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
				
				String instrument_no = rset.getString(29)==null?"":rset.getString(29);
				String buy_sell = rset.getString(33)==null?"":rset.getString(33);
				String fixed_rate = rset.getString(34)==null?"":nf2.format(rset.getDouble(34));
				BigDecimal float_val=new BigDecimal(rset.getString(35)==null?"0":rset.getString(35)) ;
				String float_rate = df3.format(float_val);
				
				String deal_no=utilBean.NewDealMappingId(comp_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, instrument_no);
				
				VBU_STATE_TIN.add(state_code);							
				VFIN_YEAR.add(financial_year);
				VCOUNTERPARTY_CD.add(countpty_cd);
				VAGMT_NO.add(agmtno);
				VAGMT_REV.add(agmtrev);
				VCONT_NO.add(contno);
				VCONT_REV.add(contrev);
				VCONT_TYPE.add(cont_type);
				VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				VCONT_START_DT.add(rset.getString(26)==null?"":rset.getString(26));
				VCONT_END_DT.add(rset.getString(27)==null?"":rset.getString(27));
				VDEAL_MAPPING.add(deal_no);
				VCONT_REF.add(rset.getString(24)==null?"":rset.getString(24));
				VPLANT_SEQ.add(plant_seq);
				VPLANT_ABBR.add(""+utilBean.getCounterpartyPlantABBR(conn,countpty_cd, comp_cd, plant_seq, "T"));
				VBU_PLANT_SEQ.add(bu_plant_seq);
				VBU_PLANT_ABBR.add(""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B"));
				VPERIOD_START_DT.add(rset.getString(14)==null?"":rset.getString(14));
				VPERIOD_END_DT.add(rset.getString(15)==null?"":rset.getString(15));
				VBILLING_FREQ_FLAG.add(freq);
				VBILLING_FREQ.add(utilBean.getBillingFreqNm(freq));
				VPRODUCTION_MONTH.add(prod_month);
				VINVOICE_DUE_DT.add(rset.getString(11)==null?"":rset.getString(11));
				
				VCASH_FLOW.add(rset.getString(25)==null?"":rset.getString(25));
				
				VACCRUAL_QTY.add(rset.getString(16)==null?"":nf.format(rset.getDouble(16)));
				VQTY_UNIT.add(utilBean.getEnergyUnitNm(conn,(rset.getString(30)==null?"":rset.getString(30))));
				VACCRUAL_AMT.add(rset.getString(17)==null?"":nf.format(rset.getDouble(17)));
				
				String price_unit=rset.getString(18)==null?"":rset.getString(18);

				String exchng_rate_cd=rset.getString(19)==null?"":rset.getString(19);
				String exchng_rate_dt=rset.getString(20)==null?"":rset.getString(20);
				String exchng_rate=rset.getString(21)==null?"":nf2.format(rset.getDouble(21));
				String invoice_raise_in=rset.getString(22)==null?"":rset.getString(22);
				
				VSELL_PRICE_CD.add(price_unit);
				VSELL_PRICE_NM.add(utilBean.getRateUnitNm(conn,price_unit));
				VINVOICE_RAISED_IN.add(invoice_raise_in);
				
				if(price_unit.equals("2"))
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
				}
				tmp_tot_qty+=rset.getDouble(16);
				tmp_tot_gross_amt+=rset.getDouble(23);
				tmp_tot_buy_amt+=rset.getDouble(31);
				tmp_tot_sell_amt+=rset.getDouble(32);
				VGROSS_AMT.add(rset.getString(23)==null?"":nf.format(rset.getDouble(23)));
				VBUY_AMT.add(rset.getString(31)==null?"":nf.format(rset.getDouble(31)));
				VSELL_AMT.add(rset.getString(32)==null?"":nf.format(rset.getDouble(32)));
				VBUY_SELL.add(rset.getString(33)==null?"":rset.getString(33));
				VFIXED_PRICE.add(fixed_rate);
				VFLOAT_PRICE.add(float_rate);
				
				eodProcessDoneOn=rset.getString(28)==null?"":rset.getString(28);
				
				String product_nm="";
				String cont_month_year="";
				queryString2="SELECT PRODUCT_NM,TO_CHAR(CONT_DD_MM_YR,'DD/MM/YYYY') "
						+ "FROM FMS_DERV_INSTRUMENT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
						+ "AND CONTRACT_TYPE=? AND INSTRUMENT_NO=? AND BUY_SELL=?";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, countpty_cd);
				stmt2.setString(3, agmtno);
				stmt2.setString(4, contno);
				stmt2.setString(5, cont_type);
				stmt2.setString(6, instrument_no);
				stmt2.setString(7, buy_sell);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					product_nm=rset2.getString(1)==null?"":rset2.getString(1);
					cont_month_year=rset2.getString(2)==null?"":rset2.getString(2);
				}
				rset2.close();
				stmt2.close();
				VDEAL_PROD_NM.add(product_nm);
				
				String split_dt[]=cont_month_year.split("/");
				String month=split_dt[1];
				String year=split_dt[2];
				
				String month_nm="";
				if(month.equals("01")) {month_nm="January";}
				else if(month.equals("02")) {month_nm="February";}
				else if(month.equals("03"))	{month_nm="March";}
				else if(month.equals("04")) {month_nm="April";}
				else if(month.equals("05")) {month_nm="May";}
				else if(month.equals("06")) {month_nm="June";}
				else if(month.equals("07")) {month_nm="July";}
				else if(month.equals("08")) {month_nm="August";}
				else if(month.equals("09")) {month_nm="September";}
				else if(month.equals("10")) {month_nm="October";}
				else if(month.equals("11")) {month_nm="November";}
				else if(month.equals("12")) {month_nm="December";}
				
				VMONTH.add(month_nm);
				VYEAR.add(year);
			}
			
			tot_qty=nf.format(tmp_tot_qty);
			tot_gross_amt=nf.format(tmp_tot_gross_amt);
			tot_buy_amt=nf.format(tmp_tot_buy_amt);
			tot_sell_amt=nf.format(tmp_tot_sell_amt);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getAccrualList()
	{
		String function_nm="getAccrualList()";
		try
		{
			String report_end_dt="";
			String report_start_dt="";
			if(!report_dt.equals(""))
			{
				String split_dt[]=report_dt.split("/");
				month=split_dt[1];
				year=split_dt[2];
				
				report_start_dt=""+dateUtil.getFirstDateOfMonth("01", year);
				report_end_dt=report_dt;
			}
			
			//fy_year=dateUtil.getFinancialYear(report_dt);
			
			if(!cont_mapp.equals(""))
			{
				String[] cont_split = cont_mapp.split("-");
				cont_type=cont_split[0];
				agmt_no=cont_split[1];
				cont_no=cont_split[2];
			}
			
			int count=0;
			double tmp_tot_gross_amt=0;
			double tmp_tot_buy_amt=0;
			double tmp_tot_sell_amt=0;
			double tmp_tot_qty=0;
			String queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,A.CONT_NO,A.CONT_REV,A.CONT_NAME,A.CONT_REF_NO,"
					+ "B.INSTRUMENT_NO,B.INSTRUMENT_TYPE,B.BUY_SELL,B.STATUS,B.QTY,B.QTY_UNIT,B.RATE,B.RATE_UNIT,"
					+ "B.PRODUCT_NM,B.CURVE_NM,B.PROJ_METHOD,TO_CHAR(B.CONT_DD_MM_YR,'DD/MM/YYYY'),"
					+ "TO_CHAR(B.PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(B.PRICE_END_DT,'DD/MM/YYYY'),B.CONV_FACTOR,TO_CHAR(A.TRADE_DT,'DD/MM/YYYY'),C.PLANT_SEQ_NO,D.PLANT_SEQ_NO AS BU_PLANT,"
					+ "E.INVOICE_CUR_CD,E.EXCHNG_RATE_CD,E.EXCHNG_RATE_CAL,E.EXCHG_VAL,E.HOLIDAY_STATE,E.DUE_DATE,E.DUE_DT_IN,E.EXCLUDE_SAT "
					+ "FROM FMS_DERV_CONT_MST A,"
					+ "FMS_DERV_INSTRUMENT_MST B,"
					+ "FMS_DERV_CONT_PLANT C,"
					+ "FMS_DERV_CONT_BU D, "
					+ "FMS_DERV_CONT_BILLING_DTL E "
					+ "WHERE A.COMPANY_CD=? ";
			if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
			{
				queryString+="AND A.COUNTERPARTY_CD=? ";
			}
			if(!cont_mapp.equals(""))
			{
				queryString+="AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? ";
			}
				queryString+= "AND B.PRICE_END_DT>TO_DATE(?,'DD/MM/YYYY') AND B.PRICE_END_DT<TO_DATE(?,'DD/MM/YYYY') AND B.STATUS='Y' AND A.CONT_STATUS!='X' "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO "
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.AGMT_TYPE=C.AGMT_TYPE AND A.AGMT_NO=C.AGMT_NO "
					+ "AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.CONT_NO=C.CONT_NO "
					+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.AGMT_TYPE=D.AGMT_TYPE AND A.AGMT_NO=D.AGMT_NO "
					+ "AND A.CONTRACT_TYPE=D.CONTRACT_TYPE AND A.CONT_NO=D.CONT_NO "
					+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.AGMT_TYPE=E.AGMT_TYPE AND A.AGMT_NO=E.AGMT_NO "
					+ "AND A.CONTRACT_TYPE=E.CONTRACT_TYPE AND A.CONT_NO=E.CONT_NO AND C.PLANT_SEQ_NO=E.PLANT_SEQ_NO "
					+ "ORDER BY B.CONT_DD_MM_YR";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(++count, comp_cd);
			if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
			{
				stmt.setString(++count, counterparty_cd);
			}
			if(!cont_mapp.equals(""))
			{
				stmt.setString(++count, agmt_no);
				stmt.setString(++count, cont_no);
				stmt.setString(++count, cont_type);
			}
			stmt.setString(++count, report_start_dt);
			stmt.setString(++count, report_end_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				String agmtno=rset.getString(3)==null?"0":rset.getString(3);
				String agmtrev=rset.getString(4)==null?"0":rset.getString(4);
				String cont_type=rset.getString(5)==null?"":rset.getString(5);
				String contno=rset.getString(6)==null?"0":rset.getString(6);
				String contrev=rset.getString(7)==null?"0":rset.getString(7);
				String contRef=rset.getString(9)==null?"":rset.getString(9);
				String instrument_no=rset.getString(10)==null?"":rset.getString(10);
				String buy_sell=rset.getString(12)==null?"":rset.getString(12);
				
				String cont_month_year=rset.getString(21)==null?"":rset.getString(21);
				String cont_start_dt=rset.getString(22)==null?"":rset.getString(22);
				String cont_end_dt=rset.getString(23)==null?"":rset.getString(23);
				fy_year=dateUtil.getFinancialYear(cont_end_dt);
				String split_dt[]=cont_month_year.split("/");
				String month=split_dt[1];
				String year=split_dt[2];
				
				String month_nm="";
				if(month.equals("01")) {month_nm="January";}
				else if(month.equals("02")) {month_nm="February";}
				else if(month.equals("03"))	{month_nm="March";}
				else if(month.equals("04")) {month_nm="April";}
				else if(month.equals("05")) {month_nm="May";}
				else if(month.equals("06")) {month_nm="June";}
				else if(month.equals("07")) {month_nm="July";}
				else if(month.equals("08")) {month_nm="August";}
				else if(month.equals("09")) {month_nm="September";}
				else if(month.equals("10")) {month_nm="October";}
				else if(month.equals("11")) {month_nm="November";}
				else if(month.equals("12")) {month_nm="December";}
				
				String deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, instrument_no);
				
				String due_days=rset.getString(33)==null?"":rset.getString(33);
				String consider_due_dt_in=rset.getString(34)==null?"":rset.getString(34);
				String exclude_sat=rset.getString(35)==null?"":rset.getString(35);
				
				double qty=rset.getDouble(14);
				String qty_unit_cd=rset.getString(15)==null?"":rset.getString(15);
				String qty_unit = utilBean.getEnergyUnitNm(conn, qty_unit_cd);
				//String price=rset.getString(20)==null?"":rset.getString(20);
				String price_unit=rset.getString(17)==null?"":rset.getString(17);
				String price_unit_nm=utilBean.getRateUnitNm(conn,price_unit);
				String product_nm = rset.getString(18)==null?"":rset.getString(18);
				String curve_nm = rset.getString(19)==null?"":rset.getString(19);
				double price = Double.parseDouble(nf2.format(rset.getDouble(16)));
				
				double float_price=0;
				queryString2 = "SELECT AVG(SETTLE_PRICE) FROM FMS_SPOT_PRICE_DTL "
						+ "WHERE SETTLE_PRICE>0 AND CURVE_DT BETWEEN TO_DATE(?,'dd/mm/yyyy') AND TO_DATE(?,'dd/mm/yyyy') "
						+ "AND CURVE_TYPE='Spot' AND CURVE_NM=? AND ACTUAL_CURVE=?";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, cont_start_dt);
				stmt2.setString(2, cont_end_dt);
				stmt2.setString(3, product_nm);
				stmt2.setString(4, curve_nm);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					BigDecimal float_val=new BigDecimal(rset2.getString(1)==null?"0":rset2.getString(1)) ;
					float_price=Double.parseDouble(df3.format(float_val));						
				}
				rset2.close();
				stmt2.close();
				
				double sell_amt=0;
				double buy_amt=0;
				
				if(buy_sell.equals("BUY"))
				{
					sell_amt=qty*float_price;
					buy_amt=qty*price;
				}
				else
				{
					buy_amt=qty*float_price;
					sell_amt=qty*price;
				}
				
				String inv_typ_flg="";
				double totalamt=sell_amt-buy_amt;
				if(totalamt<0)
				{
					inv_typ_flg="R";
					//totalamt=totalamt*(-1);
				}
				else
				{
					inv_typ_flg="I";
				}
				
				String invoice_raise_in=rset.getString(28)==null?"":rset.getString(28);
				
				String holiday_state = rset.getString(32)==null?"":rset.getString(32);
				
				String plant_seq = rset.getString(26)==null?"":rset.getString(26);
				String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, own_cd, plant_seq, "T");
				
				String bu_plant_seq = rset.getString(27)==null?"":rset.getString(27);
				String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,own_cd, own_cd, bu_plant_seq, "B");
				
				String temp_st_dt="";
				String temp_end_dt="";
				String diff_color="";
				temp_st_dt=period_start_dt;
				temp_end_dt=period_end_dt;
				diff_color="";
				
				if(dateUtil.getDays(cont_end_dt, temp_end_dt)<=0)
				{
					temp_end_dt=cont_end_dt;
					diff_color="blue";
				}
				
				String state_code=utilBean.getState_TIN(conn, own_cd, own_cd, "B", bu_plant_seq);
				
				int isInvExist=0;
				queryString3="SELECT COUNT(*) "
						+ "FROM FMS_DERV_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND AGMT_NO=? AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_UNIT=? "
						+ "AND BU_STATE_TIN=? "
						+ "AND FINANCIAL_YEAR=? AND INSTRUMENT_NO=? AND PDF_INV_DTL IS NOT NULL AND INV_FLAG='F'";
				stmt0 = conn.prepareStatement(queryString3);
				stmt0.setString(1, own_cd);
				stmt0.setString(2, countpty_cd);
				stmt0.setString(3, contno);
				stmt0.setString(4, agmtno);
				stmt0.setString(5, plant_seq);
				stmt0.setString(6, cont_type);
				stmt0.setString(7, bu_plant_seq);
				stmt0.setString(8, state_code);
				stmt0.setString(9, fy_year);
				stmt0.setString(10, instrument_no);
				rset0=stmt0.executeQuery();
				if(rset0.next())
				{
					isInvExist=rset0.getInt(1);
				}
				rset0.close();
				stmt0.close();
				
				if(isInvExist==0)
				{
					if(qty>0)
					{
						VBU_STATE_TIN.add(state_code);							
						VFIN_YEAR.add(fy_year);
						VCOUNTERPARTY_CD.add(countpty_cd);
						VAGMT_NO.add(agmtno);
						VAGMT_REV.add(agmtrev);
						VCONT_NO.add(contno);
						VCONT_REV.add(contrev);
						VINSTRUMENT_NO.add(instrument_no);
						VCONT_TYPE.add(cont_type);
						VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
						VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
						VCONT_START_DT.add(cont_start_dt);
						VCONT_END_DT.add(cont_end_dt);
						VDEAL_MAPPING.add(deal_no);
						VCONT_REF.add(contRef);
						VPLANT_SEQ.add(plant_seq);
						VPLANT_ABBR.add(plant_abbr);
						VBU_PLANT_SEQ.add(bu_plant_seq);
						VBU_PLANT_ABBR.add(bu_plant_abbr);
						VPERIOD_START_DT.add(cont_start_dt);
						VPERIOD_END_DT.add(cont_end_dt);
						VBILLING_FREQ_FLAG.add(billing_cycle);
						VBILLING_FREQ.add("");
						VPRODUCTION_MONTH.add(cont_month_year.toString().substring(3));
						VINVOICE_DUE_DT.add(utilBean.DueDateCalculation(conn,temp_end_dt, due_days,consider_due_dt_in,exclude_sat,comp_cd,holiday_state));
						
						VCASH_FLOW.add("Derivatives");
							
						VACCRUAL_QTY.add(nf.format(qty));
						VQTY_UNIT.add(qty_unit);
						VQTY_UNIT_CD.add(qty_unit_cd);
						VACCRUAL_AMT.add(nf.format(totalamt));
						VSELL_PRICE_CD.add(price_unit);
						VSELL_PRICE_NM.add(price_unit_nm);
						VINVOICE_RAISED_IN.add(invoice_raise_in);
						
						VGROSS_AMT.add(nf.format(totalamt));
						VBUY_SELL.add(buy_sell);
						VINVOICE_TYPE.add(inv_typ_flg);
						VSELL_AMT.add(nf.format(sell_amt));
						VBUY_AMT.add(nf.format(buy_amt));
						VDEAL_PROD_NM.add(product_nm);
						VFIXED_PRICE.add(nf2.format(price));
						VFLOAT_PRICE.add(nf3.format(float_price));
						VMONTH.add(month_nm);
						VYEAR.add(year);
						
						tmp_tot_qty += qty;
						tmp_tot_gross_amt += totalamt;
						tmp_tot_buy_amt += buy_amt;
						tmp_tot_sell_amt += sell_amt;
					}
				}
			}
			rset.close();
			stmt.close();
			
			tot_qty=nf.format(tmp_tot_qty);
			tot_gross_amt=nf.format(tmp_tot_gross_amt);
			tot_buy_amt=nf.format(tmp_tot_buy_amt);
			tot_sell_amt=nf.format(tmp_tot_sell_amt);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void freezAccrualData()
	{
		String function_nm="freezAccrualData()";
		try
		{
			int count=0;
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_DERV_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') ";
			if(callFlag.equalsIgnoreCase("DERV_ACCRUAL_ACCOUNTING"))
			{
				queryString+="AND IS_MTM NOT IN ('Y') ";
			}
			else if(callFlag.equalsIgnoreCase("DERV_MTM_ACCRUAL_ACCOUNTING"))
			{
				queryString+="AND IS_MTM='Y' ";
			}
			if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
			{
				queryString+= "AND COUNTERPARTY_CD=? ";
			}
			stmt = conn.prepareStatement(queryString);
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
				queryString="DELETE FROM FMS_DERV_ACCRUAL_DTL "
						+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') ";
				if(callFlag.equalsIgnoreCase("DERV_ACCRUAL_ACCOUNTING"))
				{
					queryString+="AND IS_MTM NOT IN ('Y') ";
				}
				else if(callFlag.equalsIgnoreCase("DERV_MTM_ACCRUAL_ACCOUNTING"))
				{
					queryString+="AND IS_MTM='Y' ";
				}
				if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
				{
					queryString+= "AND COUNTERPARTY_CD=? ";
				}
				stmt1 = conn.prepareStatement(queryString);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, report_dt);
				if(!counterparty_cd.equals("0") && !counterparty_cd.equals(""))
				{
					stmt1.setString(3, counterparty_cd);
				}
				stmt1.executeUpdate();
				
				stmt1.close();
			}
			for(int i=0;i<VCOUNTERPARTY_CD.size();i++)
			{		
				String prod_month="01/"+VPRODUCTION_MONTH.elementAt(i);
				
				int cnt=0;
				queryString="INSERT INTO FMS_DERV_ACCRUAL_DTL(COMPANY_CD,REPORT_DT,COUNTERPARTY_CD,FINANCIAL_YEAR,"
						+ "AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,"
						+ "CONTRACT_TYPE,BUY_SELL,BU_UNIT,BU_STATE_TIN,PLANT_SEQ,"
						+ "INVOICE_DUE_DT,PROD_MONTH,FREQ,"
						+ "PERIOD_START_DT,PERIOD_END_DT,"
						+ "ACCRUAL_QTY,ACCRUAL_QTY_UNIT,ACCRUAL_AMT,RATE_IN,INVOICE_RAISED_IN,"
						+ "GROSS_AMT,"
						+ "CONT_REF_NO,CASH_FLOW,CONT_START_DT,CONT_END_DT,"
						+ "INSTRUMENT_NO,INV_TYPE,BUY_AMOUNT,SELL_AMOUNT,IS_MTM,FIXED_RATE,FLOAT_RATE) "
						+ "VALUES(?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
						+ "?,?,?,?,"
						+ "?,?,?,?,?,"
						+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,"
						+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
						+ "?,?,?,?,?,"
						+ "?,"
						+ "?,?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
						+ "?,?,?,?,?,?,?)";
				stmt1 = conn.prepareStatement(queryString);
				stmt1.setString(++cnt, comp_cd);
				stmt1.setString(++cnt, report_dt);
				stmt1.setString(++cnt, ""+VCOUNTERPARTY_CD.elementAt(i));
				stmt1.setString(++cnt, ""+VFIN_YEAR.elementAt(i));
				stmt1.setString(++cnt, ""+VAGMT_NO.elementAt(i));
				stmt1.setString(++cnt, ""+VAGMT_REV.elementAt(i));
				stmt1.setString(++cnt, ""+VCONT_NO.elementAt(i));
				stmt1.setString(++cnt, ""+VCONT_REV.elementAt(i));
				stmt1.setString(++cnt, ""+VCONT_TYPE.elementAt(i));
				stmt1.setString(++cnt, ""+VBUY_SELL.elementAt(i));
				stmt1.setString(++cnt, ""+VBU_PLANT_SEQ.elementAt(i));
				stmt1.setString(++cnt, ""+VBU_STATE_TIN.elementAt(i));
				stmt1.setString(++cnt, ""+VPLANT_SEQ.elementAt(i));
				stmt1.setString(++cnt, ""+VINVOICE_DUE_DT.elementAt(i));
				stmt1.setString(++cnt, prod_month);
				stmt1.setString(++cnt, "12");
				stmt1.setString(++cnt, ""+VPERIOD_START_DT.elementAt(i));
				stmt1.setString(++cnt, ""+VPERIOD_END_DT.elementAt(i));
				stmt1.setString(++cnt, ""+VACCRUAL_QTY.elementAt(i));
				stmt1.setString(++cnt, ""+VQTY_UNIT_CD.elementAt(i));
				stmt1.setString(++cnt, ""+VACCRUAL_AMT.elementAt(i));
				stmt1.setString(++cnt, ""+VSELL_PRICE_CD.elementAt(i));
				stmt1.setString(++cnt, ""+VINVOICE_RAISED_IN.elementAt(i));
				stmt1.setString(++cnt, ""+VGROSS_AMT.elementAt(i));
				stmt1.setString(++cnt, ""+VCONT_REF.elementAt(i));
				stmt1.setString(++cnt, ""+VCASH_FLOW.elementAt(i));
				stmt1.setString(++cnt, ""+VCONT_START_DT.elementAt(i));
				stmt1.setString(++cnt, ""+VCONT_END_DT.elementAt(i));
				stmt1.setString(++cnt, ""+VINSTRUMENT_NO.elementAt(i));
				stmt1.setString(++cnt, ""+VINVOICE_TYPE.elementAt(i));
				stmt1.setString(++cnt, ""+VBUY_AMT.elementAt(i));
				stmt1.setString(++cnt, ""+VSELL_AMT.elementAt(i));
				if(callFlag.equalsIgnoreCase("DERV_ACCRUAL_ACCOUNTING"))
				{
					stmt1.setString(++cnt, "N");
				}
				else if(callFlag.equalsIgnoreCase("DERV_MTM_ACCRUAL_ACCOUNTING"))
				{
					stmt1.setString(++cnt, "Y");
				}
				stmt1.setString(++cnt, ""+VFIXED_PRICE.elementAt(i));
				stmt1.setString(++cnt, ""+VFLOAT_PRICE.elementAt(i));
				stmt1.executeUpdate();
				
				stmt1.close();
				
				isFreezed="Y";
			}
			conn.commit();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void generateDervMTMXML()
	{
		String function_nm="generateDervMTMXML()";
		try
		{
			String sysdate=dateUtil.getSysdate();
			String sysdateWithTime=dateUtil.getSysdateWithTime24hr();
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
			
			String queryString_temp="SELECT DISTINCT COUNTERPARTY_CD,INV_TYPE "
					+ "FROM FMS_DERV_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? "
					+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND IS_MTM='Y' ";
			stmt_temp=conn.prepareStatement(queryString_temp);
			stmt_temp.setString(1, comp_cd);
			stmt_temp.setString(2, report_dt);
			rset_temp=stmt_temp.executeQuery();
			while(rset_temp.next())
			{
				String counterpty_cd=rset_temp.getString(1)==null?"":rset_temp.getString(1);
				String inv_type=rset_temp.getString(2)==null?"":rset_temp.getString(2);
				
				String xml_seq="1";
				String xml_num="";
				String queryString2="SELECT NVL(MAX(XML_SEQ),0) "
						+ "FROM FMS_DERV_ACCRUAL_DTL "
						+ "WHERE COMPANY_CD=? "
						+ "AND TO_DATE(TO_CHAR(REPORT_DT,'MM/YYYY'),'MM/YYYY')=TO_DATE(?,'MM/YYYY') ";
				queryString2+="AND IS_MTM='Y' ";
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
					
				xml_num=invoice_prefix+"MT"+utilBean.PrePaddingZero(xml_seq, 4)+"/"+accountingPeriodMonth+""+accountingPeriodYear;
				
				fms_MessageId=xml_num.replaceAll("/", "-");
				
				DocumentBuilderFactory docFactory = xmlUtil.dcoumentBuilderFactory();
			    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
			    Document doc = docBuilder.newDocument();
			    
			    Element fmsng = doc.createElement("EmsSAPGeneralLedgerMessage");
			    doc.appendChild(fmsng);
		
			    Element Header = doc.createElement("Header");
			    fmsng.appendChild(Header);
			    Element Subledger = doc.createElement("Subledger");
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
			    Scope.appendChild(doc.createTextNode(""+utilBean.getCompanySAPcode(conn, comp_cd)+"Accounting"));
			    DateTimeStamp.appendChild(doc.createTextNode(date_timestamp));
			    DataSource.appendChild(doc.createTextNode(CommonVariable.app_name));
			    
			    Element SubledgerHeader = doc.createElement("SubledgerHeader");
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
				DocumentType.appendChild(doc.createTextNode("X5"));
			    DocumentDate.appendChild(doc.createTextNode(documentDate));
			    PostingDate.appendChild(doc.createTextNode(xml_sysdate));
			    AccountingPeriodMonth.appendChild(doc.createTextNode(accountingPeriodMonth));
			    AccountingPeriodYear.appendChild(doc.createTextNode(accountingPeriodYear));
			    InternalLegalEntity.appendChild(doc.createTextNode(""+utilBean.getCompanySAPcode(conn, comp_cd)));
			    DocHeaderText.appendChild(doc.createTextNode(docHeaderText));
			    RefNum.appendChild(doc.createTextNode(xml_num));
			    EmsRefNum.appendChild(doc.createTextNode(xml_num));
		    	Currency.appendChild(doc.createTextNode("USD"));
			    String account=utilBean.getCounterpartySAPcode(conn,counterpty_cd);
			    String countpty_category=""+utilBean.getCounterpartyCategory(conn,counterpty_cd);
			    
			    String iny_type="";
			    String prod_month="";
			    int i=0;
				String queryString="SELECT DISTINCT AGMT_NO,CONT_NO,CONTRACT_TYPE,PLANT_SEQ,BU_UNIT,"
						+ "FINANCIAL_YEAR,TO_CHAR(PROD_MONTH,'DD/MM/YYYY'),FREQ,INSTRUMENT_NO,INV_TYPE "
						+ "FROM FMS_DERV_ACCRUAL_DTL "
						+ "WHERE COMPANY_CD=? "
						+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND IS_MTM='Y' AND COUNTERPARTY_CD=? AND INV_TYPE=?";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, report_dt);
				stmt.setString(3, counterpty_cd);
				stmt.setString(4, inv_type);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String agmtno=rset.getString(1)==null?"":rset.getString(1);
					String contno=rset.getString(2)==null?"":rset.getString(2);
					String cont_type=rset.getString(3)==null?"":rset.getString(3);
					String plant_seq=rset.getString(4)==null?"":rset.getString(4);
					String buSeq=rset.getString(5)==null?"":rset.getString(5);
					String financialYear=rset.getString(6)==null?"":rset.getString(6);
					prod_month=rset.getString(7)==null?"":rset.getString(7);
					String bill_freq=rset.getString(8)==null?"":rset.getString(8);
					String instrument_no=rset.getString(9)==null?"":rset.getString(9);
					iny_type=rset.getString(10)==null?"":rset.getString(10);
					
					String buStateNm="";
					String buAbbr="";
					String queryString1 = "SELECT PLANT_STATE,PLANT_ABBR "
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
					
				    String tempAccount="";
			    	String tempAccount2="";
			    	String pk=""; 
			    	String pk2="";		    	
			    	String sign = "";
			    	String sign2 = "";
			    	
					if(iny_type.equals("I"))
					{
						sign = "";
				    	sign2 = "-";
				    	pk="40";
				    	pk2="50";
					}
		    		else
		    		{
		    			sign = "-";
				    	sign2 = "";
				    	pk="50";
				    	pk2="40";
		    		}
			    	
					tempAccount="2661599";
		    		tempAccount2="6815000";
			    	
			    	String plantNm=utilBean.getCounterpartyPlantName(conn,counterpty_cd, comp_cd, plant_seq, "T");
			    	
			    	String material_code="";
			    	//String assignmentNo="T"+counterpty_cd+cont_type+contno;
			    	String assignmentNo=utilBean.NewDealMappingId(comp_cd, counterpty_cd, agmtno, "", contno, "", cont_type, instrument_no);
			    	String businessAreaCode=utilBean.getBusinessAreaSAPcode(conn, comp_cd, "T", cont_type, report_dt);
			    	String contPty_name=utilBean.getCounterpartyName(conn, counterpty_cd);
			    	
			    	//int i=0;
					String queryString4="SELECT TO_CHAR(INVOICE_DUE_DT,'DD/MM/YYYY'),ACCRUAL_QTY,ACCRUAL_AMT,GROSS_AMT,CASH_FLOW,INV_TYPE,ACCRUAL_QTY_UNIT "
							+ "FROM FMS_DERV_ACCRUAL_DTL "
							+ "WHERE COMPANY_CD=? "
							+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') AND PROD_MONTH=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
							+ "AND CONTRACT_TYPE=? AND PLANT_SEQ=? AND BU_UNIT=? "
							+ "AND FINANCIAL_YEAR=? AND FREQ=? AND INSTRUMENT_NO=? AND IS_MTM='Y' ";
					stmt4=conn.prepareStatement(queryString4);
					stmt4.setString(1, comp_cd);
					stmt4.setString(2, report_dt);
					stmt4.setString(3, prod_month);
					stmt4.setString(4, counterpty_cd);
					stmt4.setString(5, agmtno);
					stmt4.setString(6, contno);
					stmt4.setString(7, cont_type);
					stmt4.setString(8, plant_seq);
					stmt4.setString(9, buSeq);
					stmt4.setString(10, financialYear);
					stmt4.setString(11, bill_freq);
					stmt4.setString(12, instrument_no);
					rset4=stmt4.executeQuery();
					if(rset4.next())
					{
						String invoice_dueDt=rset4.getString(1)==null?"":rset4.getString(1);
						String accrual_qty=nf.format(rset4.getDouble(2));
						//3
						double temp_grossAmt=rset4.getDouble(4);
						
						String cashFlow=rset4.getString(5)==null?"":rset4.getString(5);
						iny_type=rset4.getString(6)==null?"":rset4.getString(6);
						String qty_unit_cd=(rset4.getString(7)==null?"":rset4.getString(7));
						String qty_unit = utilBean.getEnergyUnitNm(conn, qty_unit_cd);
						String qty_unit_sap=utilBean.getSAPEnergyUnitNm(conn, qty_unit_cd);
						
						if(temp_grossAmt<0)
						{
							temp_grossAmt=temp_grossAmt*(-1);
						}
						String grossAmt=nf.format(temp_grossAmt);
						
						String monthNm = dateUtil.getShortMonthName(prod_month);
				    	String monthId="";
						String yearNm = "";
				    	if(!prod_month.equals(""))
						{
							String[] temp_split=prod_month.split("/");
							monthId=temp_split[1];
							yearNm=temp_split[2];		
						}
								
				    	String itemText="";
				    	
				    	String itemText2="M2M Swaps/CFDs";
				    	
						if(!invoice_dueDt.equals(""))
						{
							String[] temp_split=invoice_dueDt.split("/");
							invoice_dueDt=temp_split[2]+""+temp_split[1]+""+temp_split[0];
						}
						
						for(int j=0; j<2;j++)
						{	    
					    	i+=1;
					    	Element SubledgerEntry = doc.createElement("SubledgerEntry");
					    	Subledger.appendChild(SubledgerEntry);
					    	
					    	Element VendorId;
					    	if(iny_type.equals("I"))
					    	{
					    		VendorId  = doc.createElement("CustomerId");//
					    	}
					    	else
					    	{
					    		VendorId  = doc.createElement("VendorId");//
					    	}
					    	
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
						   
						    if (i%2 == 0) 
						    {
						    	//VendorId.appendChild(doc.createTextNode(utilBean.PrePaddingZero(tempAccount2, 10)));
						    	PostingKey.appendChild(doc.createTextNode(pk2));
						    	Account.appendChild(doc.createTextNode(utilBean.PrePaddingZero(tempAccount2, 10)));
						    	ItemText.appendChild(doc.createTextNode(itemText2));
						    	CurrencyAmount.appendChild(doc.createTextNode(sign2+""+grossAmt));
						    	ReferenceKey1.appendChild(doc.createTextNode(account));
						    }
						    else
						    {
						    	//VendorId.appendChild(doc.createTextNode(account));//PADDING ZERO NOT REQUIRED FOR VENDOR ID
						    	PostingKey.appendChild(doc.createTextNode(pk));
					    		Account.appendChild(doc.createTextNode(utilBean.PrePaddingZero(tempAccount, 10))); // PURCHASE ETRM ACCRUALS
						    	ItemText.appendChild(doc.createTextNode(itemText));
						    	CurrencyAmount.appendChild(doc.createTextNode(sign+""+grossAmt));
						    	ReferenceKey1.appendChild(doc.createTextNode(assignmentNo));
						    }
					    	LineSeqNo.appendChild(doc.createTextNode(""+i));
					    	
					    	//Material.appendChild(doc.createTextNode(utilBean.PrePaddingZero(material_code, 18))); //NATURAL GAS MATERIAL CODE
					    	BusinessArea.appendChild(doc.createTextNode(businessAreaCode));
					    	ProductionPeriod.appendChild(doc.createTextNode(yearNm+""+monthId));
					    	AssignmentNumber.appendChild(doc.createTextNode(assignmentNo));
					    	Volume.appendChild(doc.createTextNode(accrual_qty));
				    		VolumeUnit.appendChild(doc.createTextNode(qty_unit_sap));
					    	PaymentTerms.appendChild(doc.createTextNode("ZB00")); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN
					    	PaymentDueDate.appendChild(doc.createTextNode(invoice_dueDt)); //AS PER SUNIDHI MAIL 16/08/2023
					    	PaymentBlock.appendChild(doc.createTextNode("A"));
					    	Plant.appendChild(doc.createTextNode(plantNm+" - Plant"));
						}
					}
					rset4.close();
					stmt4.close();
				}
				rset.close();
				stmt.close();
				
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
					if(iny_type.equals("I"))
					{
			    		xmlFileNm="ARGL_MTM_"+fms_MessageId+"_"+datetime+".xml";
					}
			    	else
			    	{
			    		xmlFileNm="APGL_MTM_"+fms_MessageId+"_"+datetime+".xml";
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
						String queryString5="UPDATE FMS_DERV_ACCRUAL_DTL SET XML_SEQ=?,XML_NUM=? "
								+ "WHERE COMPANY_CD=? "
								+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND COUNTERPARTY_CD=? AND IS_MTM='Y' AND INV_TYPE=?";
						stmt5=conn.prepareStatement(queryString5);
						stmt5.setString(1, xml_seq);
						stmt5.setString(2, xml_num);
						stmt5.setString(3, comp_cd);
						stmt5.setString(4, report_dt);
						stmt5.setString(5, counterpty_cd);
						stmt5.setString(6, inv_type);
						stmt5.executeUpdate();
						
						conn.commit();
						stmt5.close();
					}
				}
			}
			rset_temp.close();
			stmt_temp.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void generateDervAccrualXML()
	{
		String function_nm="generateDervAccrualXML()";
		try
		{
			String sysdate=dateUtil.getSysdate();
			String sysdateWithTime=dateUtil.getSysdateWithTime24hr();
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
			
			String queryString="SELECT DISTINCT COUNTERPARTY_CD,AGMT_NO,CONT_NO,CONTRACT_TYPE,PLANT_SEQ,BU_UNIT,"
					+ "FINANCIAL_YEAR,TO_CHAR(PROD_MONTH,'DD/MM/YYYY'),FREQ,INSTRUMENT_NO,INV_TYPE "
					+ "FROM FMS_DERV_ACCRUAL_DTL "
					+ "WHERE COMPANY_CD=? "
					+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') AND IS_MTM NOT IN ('Y') ";
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
				String plant_seq=rset.getString(5)==null?"":rset.getString(5);
				String buSeq=rset.getString(6)==null?"":rset.getString(6);
				String financialYear=rset.getString(7)==null?"":rset.getString(7);
				String prod_month=rset.getString(8)==null?"":rset.getString(8);
				String bill_freq=rset.getString(9)==null?"":rset.getString(9);
				String instrument_no=rset.getString(10)==null?"":rset.getString(10);
				String iny_type=rset.getString(11)==null?"":rset.getString(11);
				
				String buStateNm="";
				String buAbbr="";
				String queryString1 = "SELECT PLANT_STATE,PLANT_ABBR "
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
				String queryString2="SELECT NVL(MAX(XML_SEQ),0) "
						+ "FROM FMS_DERV_ACCRUAL_DTL "
						+ "WHERE COMPANY_CD=? "
						+ "AND TO_DATE(TO_CHAR(REPORT_DT,'MM/YYYY'),'MM/YYYY')=TO_DATE(?,'MM/YYYY') AND IS_MTM NOT IN ('Y') ";
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
				xml_num=invoice_prefix+"T"+utilBean.PrePaddingZero(xml_seq, 4)+"/"+accountingPeriodMonth+""+accountingPeriodYear;
				
				fms_MessageId=xml_num.replaceAll("/", "-");
				
				DocumentBuilderFactory docFactory = xmlUtil.dcoumentBuilderFactory();
			    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
			    Document doc = docBuilder.newDocument();
			    
			    Element fmsng = doc.createElement("EmsSAPGeneralLedgerMessage");
			    doc.appendChild(fmsng);
		
			    Element Header = doc.createElement("Header");
			    fmsng.appendChild(Header);
			    Element Subledger = doc.createElement("Subledger");
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
			    Scope.appendChild(doc.createTextNode(""+utilBean.getCompanySAPcode(conn, comp_cd)+"Accounting"));
			    DateTimeStamp.appendChild(doc.createTextNode(date_timestamp));
			    DataSource.appendChild(doc.createTextNode(CommonVariable.app_name));
			    
			    Element SubledgerHeader = doc.createElement("SubledgerHeader");
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
			    
		    	if(iny_type.equals("I"))
				{
			    	DocumentType.appendChild(doc.createTextNode("X4"));
				}
			    else
			    {
			    	DocumentType.appendChild(doc.createTextNode("X3"));
			    }
			    
			    DocumentDate.appendChild(doc.createTextNode(documentDate));
			    PostingDate.appendChild(doc.createTextNode(xml_sysdate));
			    AccountingPeriodMonth.appendChild(doc.createTextNode(accountingPeriodMonth));
			    AccountingPeriodYear.appendChild(doc.createTextNode(accountingPeriodYear));
			    InternalLegalEntity.appendChild(doc.createTextNode(""+utilBean.getCompanySAPcode(conn, comp_cd)));
			    DocHeaderText.appendChild(doc.createTextNode(docHeaderText));
			    RefNum.appendChild(doc.createTextNode(xml_num));
			    EmsRefNum.appendChild(doc.createTextNode(xml_num));
		    	Currency.appendChild(doc.createTextNode("USD"));
			    String account=utilBean.getCounterpartySAPcode(conn,counterpty_cd);
			    String countpty_category=""+utilBean.getCounterpartyCategory(conn,counterpty_cd);
		    	
			    String tempAccount="";
		    	String tempAccount2="";
		    	String pk=""; 
		    	String pk2="";		    	
		    	String sign = "";
		    	String sign2 = "";
		    	
	    		if(iny_type.equals("I"))
				{
					sign = "";
			    	sign2 = "-";
			    	pk="01";
			    	pk2="50";
				}
	    		else
	    		{
	    			sign = "-";
			    	sign2 = "";
			    	pk="31";
			    	pk2="40";
	    		}
		    	
		    	
	    		tempAccount="G1897";
	    		tempAccount2="6841000";
		    	
		    	
		    	String plantNm=utilBean.getCounterpartyPlantName(conn,counterpty_cd, comp_cd, plant_seq, "T");
		    	
		    	String material_code="";
		    	//String assignmentNo="T"+counterpty_cd+cont_type+contno;
		    	String assignmentNo=utilBean.NewDealMappingId(comp_cd, counterpty_cd, agmtno, "", contno, "", cont_type, instrument_no);
		    	String businessAreaCode=utilBean.getBusinessAreaSAPcode(conn, comp_cd, "T", cont_type, report_dt);
		    	String contPty_name=utilBean.getCounterpartyName(conn, counterpty_cd);
		    	
		    	int i=0;
				String queryString4="SELECT TO_CHAR(INVOICE_DUE_DT,'DD/MM/YYYY'),ACCRUAL_QTY,ACCRUAL_AMT,GROSS_AMT,CASH_FLOW,INV_TYPE,ACCRUAL_QTY_UNIT "
						+ "FROM FMS_DERV_ACCRUAL_DTL "
						+ "WHERE COMPANY_CD=? "
						+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') AND PROD_MONTH=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
						+ "AND CONTRACT_TYPE=? AND PLANT_SEQ=? AND BU_UNIT=? "
						+ "AND FINANCIAL_YEAR=? AND FREQ=? AND INSTRUMENT_NO=? AND IS_MTM NOT IN ('Y') ";
				stmt4=conn.prepareStatement(queryString4);
				stmt4.setString(1, comp_cd);
				stmt4.setString(2, report_dt);
				stmt4.setString(3, prod_month);
				stmt4.setString(4, counterpty_cd);
				stmt4.setString(5, agmtno);
				stmt4.setString(6, contno);
				stmt4.setString(7, cont_type);
				stmt4.setString(8, plant_seq);
				stmt4.setString(9, buSeq);
				stmt4.setString(10, financialYear);
				stmt4.setString(11, bill_freq);
				stmt4.setString(12, instrument_no);
				rset4=stmt4.executeQuery();
				if(rset4.next())
				{
					String invoice_dueDt=rset4.getString(1)==null?"":rset4.getString(1);
					String accrual_qty=nf.format(rset4.getDouble(2));
					//3
					double temp_grossAmt=rset4.getDouble(4);
					
					String cashFlow=rset4.getString(5)==null?"":rset4.getString(5);
					iny_type=rset4.getString(6)==null?"":rset4.getString(6);
					String qty_unit_cd=(rset4.getString(7)==null?"":rset4.getString(7));
					String qty_unit = utilBean.getEnergyUnitNm(conn, qty_unit_cd);
					String qty_unit_sap=utilBean.getSAPEnergyUnitNm(conn, qty_unit_cd);
					
					if(temp_grossAmt<0)
					{
						temp_grossAmt=temp_grossAmt*(-1);
					}
					String grossAmt=nf.format(temp_grossAmt);
					
					String monthNm = dateUtil.getShortMonthName(prod_month);
			    	String monthId="";
					String yearNm = "";
			    	if(!prod_month.equals(""))
					{
						String[] temp_split=prod_month.split("/");
						monthId=temp_split[1];
						yearNm=temp_split[2];		
					}
							
			    	String itemText="";
			    	
			    	String itemText2="Swaps & CFDs";
			    	
					if(!invoice_dueDt.equals(""))
					{
						String[] temp_split=invoice_dueDt.split("/");
						invoice_dueDt=temp_split[2]+""+temp_split[1]+""+temp_split[0];
					}
					
					for(int j=0; j<2;j++)
					{	    
				    	i+=1;
				    	Element SubledgerEntry = doc.createElement("SubledgerEntry");
				    	Subledger.appendChild(SubledgerEntry);
				    	
				    	Element VendorId;
				    	if(iny_type.equals("I"))
				    	{
				    		VendorId  = doc.createElement("CustomerId");//
				    	}
				    	else
				    	{
				    		VendorId  = doc.createElement("VendorId");//
				    	}
				    	
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
					    	VendorId.appendChild(doc.createTextNode(account));//PADDING ZERO NOT REQUIRED FOR VENDOR ID
					    	PostingKey.appendChild(doc.createTextNode(pk));
					    	ItemText.appendChild(doc.createTextNode(itemText));
					    	CurrencyAmount.appendChild(doc.createTextNode(sign+""+grossAmt));
					    }
				    	LineSeqNo.appendChild(doc.createTextNode(""+i));
				    	
				    	//Material.appendChild(doc.createTextNode(utilBean.PrePaddingZero(material_code, 18))); //NATURAL GAS MATERIAL CODE
				    	BusinessArea.appendChild(doc.createTextNode(businessAreaCode));
				    	ReferenceKey1.appendChild(doc.createTextNode(assignmentNo));
				    	ProductionPeriod.appendChild(doc.createTextNode(yearNm+""+monthId));
				    	AssignmentNumber.appendChild(doc.createTextNode(assignmentNo));
				    	Volume.appendChild(doc.createTextNode(accrual_qty));
				    	
			    		VolumeUnit.appendChild(doc.createTextNode(qty_unit_sap));
				    	PaymentTerms.appendChild(doc.createTextNode("ZB00")); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN
				    	PaymentDueDate.appendChild(doc.createTextNode(invoice_dueDt)); //AS PER SUNIDHI MAIL 16/08/2023
				    	PaymentBlock.appendChild(doc.createTextNode("A"));
				    	Plant.appendChild(doc.createTextNode(plantNm+" - Plant"));
					}
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
		    		if(iny_type.equals("I"))
					{
			    		xmlFileNm="ARGL_"+fms_MessageId+"_"+datetime+".xml";
					}
			    	else
			    	{
			    		xmlFileNm="APGL_"+fms_MessageId+"_"+datetime+".xml";
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
						String queryString5="UPDATE FMS_DERV_ACCRUAL_DTL SET XML_SEQ=?,XML_NUM=? "
								+ "WHERE COMPANY_CD=? "
								+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') AND PROD_MONTH=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? "
								+ "AND CONTRACT_TYPE=? AND PLANT_SEQ=? AND BU_UNIT=? "
								+ "AND FINANCIAL_YEAR=? AND FREQ=? AND INSTRUMENT_NO=? AND IS_MTM NOT IN ('Y') ";
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
						stmt5.setString(10, plant_seq);
						stmt5.setString(11, buSeq);
						stmt5.setString(12, financialYear);
						stmt5.setString(13, bill_freq);
						stmt5.setString(14, instrument_no);
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
	
	public void getDervActualReport()
	{
		String function_nm="getDervActualReport()";
		
		try
		{
			for(int i=0; i<VMST_INV_TYPE_FLG.size(); i++)
			{
				int index=0;
				String queryString1="SELECT DISTINCT INVOICE_SEQ,BU_STATE_TIN,COUNTERPARTY_CD,INV_FLAG "
						+ "FROM FMS_DERV_INVOICE_MST "
						+ "WHERE COMPANY_CD=? "
						+ "AND INVOICE_DT>=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_DT<=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND INV_TYPE=? "
						+ "AND APPROVED_FLAG='Y' AND INVOICE_ID_SEQ IS NOT NULL AND INVOICE_AMT IS NOT NULL "
						+ "AND SAP_APPROVAL='Y' ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, from_dt);
				stmt1.setString(3, to_dt);
				stmt1.setString(4, ""+VMST_INV_TYPE_FLG.elementAt(i));
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{	
					index+=1;
					
					String inv_seq=rset1.getString(1)==null?"":rset1.getString(1);
					String contPtyCd=rset1.getString(3)==null?"":rset1.getString(3);
					String buStTin=rset1.getString(2)==null?"":rset1.getString(2);
					String inv_flag=rset1.getString(4)==null?"":rset1.getString(4);
					String companyCd="";
					String countpty_cd="";
					String agmtno="";
					String agmtrev="";
					String contno="";
					String contrev="";
					String cont_type="";
					String instrument_no="";
					String deal_no="";
					String plant_seq="";
					String bu_plant_seq="";
					String billing_cycle="";
					String fin_year="";
					String invoice_no="";
					String invoice_seq="";
					String checked_flag="";
					String approved_flag="";
					String pdf_flg="";
					String pdf_ori="";
					String sap_approved_flag="";
					String bu_state_tin="";
					String period_start_dt="";
					String period_end_dt="";
					String invoice_dt="";
					String invoice_due_dt="";
					String authorized_flag="";
					String invoice_type="";
					double sales_price = 0;
					double sales_amt = 0;
					double gross_amt = 0;
					String deal_map="";
					double alloc_qty=0;
					String inv_ref_no="";
					
					double invoice_amt = 0;
					double net_payable = 0;
					
					String temp_pay_recv_amt = "";
					double pay_recv_amt = 0;
					String temp_pay_recv_dt = "";
					String contRef="";
					String sap_approval_flg="";
					String inv_raised_in="";
					String qty_unit="";
					String fin_sys="";
					
					int count=0;
					String queryString = "SELECT COMPANY_CD,INVOICE_SEQ, INVOICE_REF_NO, TO_CHAR(INVOICE_DT,'DD/MM/YYYY'), "
						    + "TO_CHAR(DUE_DT,'DD/MM/YYYY'), TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'), TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'), "
						    + "FREQ, ALLOC_QTY, SALE_PRICE, SALE_PRICE_UNIT, INVOICE_AMT, INVOICE_AMT,NET_PAYABLE_AMT, "
						    + "INVOICE_RAISED_IN, TO_CHAR(INVOICE_DT, 'Month'), COUNTERPARTY_CD, CONT_NO, CONTRACT_TYPE, "
						    + "BU_UNIT, CHECKED_FLAG, AUTHORIZED_FLAG, APPROVED_FLAG,INV_TYPE, AGMT_NO, INVOICE_NO,INSTRUMENT_NO,FINANCIAL_YEAR,"
						    + "SAP_APPROVAL,INVOICE_RAISED_IN,PAY_RECV_AMT,PAY_RECV_DT,PLANT_SEQ,FIN_SYS "
						    + "FROM FMS_DERV_INVOICE_MST A "
						    + "WHERE COMPANY_CD = ? "
						    + "AND INVOICE_DT >= TO_DATE(?,'DD/MM/YYYY') "
						    + "AND INVOICE_DT <= TO_DATE(?,'DD/MM/YYYY') "
						    + "AND CONTRACT_TYPE IN ('V') AND INV_TYPE=? AND INVOICE_SEQ=? AND COUNTERPARTY_CD=? AND BU_STATE_TIN=? "
						    + "AND APPROVED_FLAG=? AND INVOICE_ID_SEQ IS NOT NULL AND INVOICE_AMT IS NOT NULL "
							+ "AND SAP_APPROVAL=? AND INV_FLAG=? "
						    + "ORDER BY INVOICE_DT";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(++count, comp_cd);
					stmt.setString(++count, from_dt);
					stmt.setString(++count, to_dt);
					stmt.setString(++count, ""+VMST_INV_TYPE_FLG.elementAt(i));
					stmt.setString(++count, inv_seq);
					stmt.setString(++count, contPtyCd);
					stmt.setString(++count, buStTin);
					stmt.setString(++count, "Y");
					stmt.setString(++count, "Y");
					stmt.setString(++count, inv_flag);
					rset=stmt.executeQuery();
					while(rset.next())
					{
						companyCd = rset.getString(1)==null?"":rset.getString(1);
						countpty_cd = rset.getString(17)==null?"":rset.getString(17);
						agmtno = rset.getString(25)==null?"":rset.getString(25);
						contno = rset.getString(18)==null?"":rset.getString(18);
						cont_type = rset.getString(19)==null?"":rset.getString(19);
						instrument_no = rset.getString(27)==null?"":rset.getString(27);
						fin_year = rset.getString(28)==null?"":rset.getString(28);
						
						bu_state_tin = rset1.getString(2)==null?"":rset1.getString(2);
						invoice_seq = rset.getString(2)==null?"":rset.getString(2);
						invoice_no = rset.getString(26)==null?"":rset.getString(26);
						inv_ref_no = rset.getString(3)==null?"":rset.getString(3);
						invoice_dt = rset.getString(4)==null?"":rset.getString(4);
						period_start_dt = rset.getString(6)==null?"":rset.getString(6);
						period_end_dt = rset.getString(7)==null?"":rset.getString(7);
						invoice_due_dt = rset.getString(5)==null?"":rset.getString(5);
						sales_price=rset.getDouble(10);
						alloc_qty+=rset.getDouble(9);
						
						sales_amt += rset.getDouble(12);
						gross_amt += rset.getDouble(13);
						
						invoice_amt += rset.getDouble(13);
						net_payable += rset.getDouble(14);
						
						temp_pay_recv_amt = rset.getString(31)==null?"":rset.getString(31);
						pay_recv_amt = rset.getDouble(31);
						temp_pay_recv_dt = rset.getString(32)==null?"":rset.getString(32);
						
						plant_seq = rset.getString(33)==null?"":rset.getString(33);
						bu_plant_seq=rset.getString(20)==null?"":rset.getString(20);
						String dealMap=utilBean.NewDealMappingId(companyCd, countpty_cd, agmtno, agmtrev, contno, contrev, cont_type, instrument_no);
						sap_approval_flg=rset.getString(29)==null?"":rset.getString(29);
						inv_raised_in=rset.getString(30)==null?"":rset.getString(30);
						invoice_type=rset.getString(24)==null?"":rset.getString(24);
						
						fin_sys = rset.getString(34)==null?"":rset.getString(34);
						
						if(deal_map.equals(""))
						{
							deal_map=dealMap;
						}
						else
						{
							deal_map=deal_map+", "+dealMap;
						}
						
						String contRefNo="";
						String queryString2="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_TYPE,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,A.CONT_NO,A.CONT_REV,A.CONT_NAME,A.CONT_REF_NO,"
								+ "B.INSTRUMENT_NO,B.INSTRUMENT_TYPE,B.BUY_SELL,B.STATUS,B.QTY,B.QTY_UNIT,B.RATE,B.RATE_UNIT,"
								+ "B.PRODUCT_NM,B.CURVE_NM,B.PROJ_METHOD,TO_CHAR(B.CONT_DD_MM_YR,'DD/MM/YYYY'),"
								+ "TO_CHAR(B.PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(B.PRICE_END_DT,'DD/MM/YYYY'),B.CONV_FACTOR,TO_CHAR(A.TRADE_DT,'DD/MM/YYYY') "
								+ "FROM FMS_DERV_CONT_MST A,FMS_DERV_INSTRUMENT_MST B "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
								+ "AND B.STATUS='Y' AND B.INSTRUMENT_NO=? "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
								+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO";
					  	stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, companyCd);
						stmt2.setString(2, countpty_cd);
						stmt2.setString(3, agmtno);
						stmt2.setString(4, contno);
						stmt2.setString(5, cont_type);
						stmt2.setString(6, instrument_no);
						rset2=stmt2.executeQuery();
						while(rset2.next())
						{
							contRefNo=rset2.getString(10)==null?"":rset2.getString(10);
							String qty_unit_cd=rset2.getString(16)==null?"":rset2.getString(16);
							qty_unit = utilBean.getEnergyUnitNm(conn, qty_unit_cd);
						}
						rset2.close();
						stmt2.close();
						
						if(contRef.equals(""))
						{
							contRef=contRefNo;
						}
						else
						{
							contRef=contRef+", "+contRefNo;
						}
						
					}
					rset.close();
					stmt.close();
					
					if(sales_amt<0)
					{
						sales_amt=sales_amt*(-1);
					}
					if(gross_amt<0)
					{
						gross_amt=gross_amt*(-1);
					}
					if(invoice_amt<0)
					{
						invoice_amt=invoice_amt*(-1);
					}
					if(net_payable<0)
					{
						net_payable=net_payable*(-1);
					}
					
					if(!temp_pay_recv_amt.equals(""))
					{
						VPAY_RECV_AMT.add(nf.format(pay_recv_amt));
					}
					else
					{
						VPAY_RECV_AMT.add("");
					}
					VPAY_RECV_DT.add(temp_pay_recv_dt);
					VINV_REF.add(inv_ref_no);
					VCOUNTERPARTY_CD.add(countpty_cd);
					VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
					VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
					VAGMT_NO.add(agmtno);
					VAGMT_REV.add(agmt_rev);
					VCONT_NO.add(contno);
					VCONT_REV.add(cont_rev);
					VCONT_TYPE.add(cont_type);
					VDEAL_MAPPING.add(deal_map);
					VFIN_YEAR.add(fin_year);
					VBU_STATE_TIN.add(bu_state_tin);
					VINVOICE_SEQ.add(inv_seq);
					VINVOICE_NO.add(invoice_no);
					VINVOICE_DT.add(invoice_dt);
					VPERIOD_START_DT.add(period_start_dt);
					VPERIOD_END_DT.add(period_end_dt);
					VINVOICE_DUE_DT.add(invoice_due_dt);
					VSELL_RATE.add(""+utilBean.RateNumberFormat(sales_price, "2"));
					VSELL_PRICE_CD.add("2");
					VSELL_PRICE_NM.add(""+utilBean.getRateUnitNm(conn,"2"));
					VGROSS_AMT.add(nf.format(gross_amt));
					VINVOICE_AMT.add(nf.format(invoice_amt));
					
					VNET_PAYABLE_AMT.add(nf.format(net_payable));
					double short_received = net_payable - pay_recv_amt;
					VSHORT_RECEIVED.add(nf.format(short_received));
					
					VBU_PLANT_ABBR.add(""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B"));
					VSAP_APPROVAL_FLG.add(sap_approval_flg);
					
					VALLOC_QTY.add(nf.format(alloc_qty));
					VALLOC_QTY_UNIT.add(qty_unit);
					VINVOICE_RAISED_IN.add(""+utilBean.getRateUnitNm(conn,inv_raised_in));
					VPAYMENT_DONE_IN.add("USD");
					VSELL_AMT.add(nf.format(sales_amt));
					VINVOICE_TYPE.add(invoice_type);
					VCONT_REF.add(contRef);
					VCASH_FLOW.add("Derivatives");
					VFIN_SYS.add(fin_sys);
				}
				rset1.close();
				stmt1.close();
				VINDEX.add(index);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSendDervInvoiceMailDetail()
	{
		String function_nm="getSendDervInvoiceMailDetail()";
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
			
			queryString1="SELECT BU_CONTACT_PERSON_CD,CONTACT_PERSON_CD,INVOICE_NO,TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(INVOICE_DT,'DD/MM/YYYY') "
					+ "FROM FMS_DERV_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND BU_STATE_TIN=? AND FINANCIAL_YEAR=? "
					+ "AND INVOICE_SEQ=? AND INV_TYPE=? AND INV_FLAG='F'";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_cd);
			stmt1.setString(3, bu_state_tin);
			stmt1.setString(4, fy_year);
			stmt1.setString(5, invoice_seq);
			stmt1.setString(6, inv_type);
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
			
			if(mail_pdf_type.equals(""))
			{
				mail_pdf_type="O";
			}
			String[] pdf_type=mail_pdf_type.split(",");
			
			for(int i=0; i<pdf_type.length; i++)
			{
				String temp_pdf_type=""+pdf_type[i];
				
				VMAIL_PDF_TYPE.add(temp_pdf_type);
				
				String temp_pdf_type_nm="";
				if(!temp_pdf_type.equals(""))
				{
					temp_pdf_type_nm="Original";
					if(inv_type.equals("R"))
					{
						VMAIL_PDF_TYPE_NM.add("Original Remittance");
					}
					else
					{
						VMAIL_PDF_TYPE_NM.add("Original Invoice");
					}
				}
				
				String companyAbbr=utilBean.getCompanyAbbr(conn,comp_cd);
				String customerNm=utilBean.getCounterpartyName(conn,counterparty_cd);
				String subject="";
				String type="DERIVATIVES";
				
				String contact_inv_typ="RM"; //as discussed with Vijay, There is no INV option for Trader in entity Contact Master, consider RM for that
				
				
				subject=companyAbbr+"/"+customerNm+"/"+type+"/"+temp_pdf_type_nm+"/"+dueDate.replaceAll("/", "-")+"/"+invoiceNo;
				String to_list=utilBean.getEntityContactPersonEmailList(conn, comp_cd, counterparty_cd, "T", "P"+plant_seq, contact_inv_typ, "RLNG","Y");
				String cc_list=utilBean.getEntityContactPersonEmailList(conn, comp_cd, counterparty_cd, "T", "P"+plant_seq, contact_inv_typ, "RLNG","N");
				
				String tmpCcList=utilBean.getEntityContactPersonEmailList(conn, comp_cd, comp_cd, "B", "P"+plant_seq, contact_inv_typ, "RLNG","N");
				cc_list+=cc_list.equals("")?tmpCcList:","+tmpCcList;
				
				//get BCc list
				String bcc_list=utilBean.getEntityContactPersonEmailList(conn, comp_cd, comp_cd, "B", "P"+plant_seq, contact_inv_typ, "RLNG","B");
	
				String attachment="";
				Vector VTEMP_MAIL_ATTACHMENT = new Vector(); 
				Vector VTEMP_MAIL_ANNEXURE_ATTACHMENT_FLAG = new Vector();
				int st_count=0;
				
				queryString3="SELECT FILE_NAME "
						+ "FROM FMS_DERV_INV_FILE_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
	 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
	 	        		+ "AND PDF_TYPE NOT IN ('X') "
	 	        		+ "AND INV_TYPE=?";
				if(inv_type.equals("I"))
				{
					queryString3+="AND PDF_SIGNED=? ";
				}
				stmt3=conn.prepareStatement(queryString3);
				stmt3.setString(++st_count, comp_cd);
				stmt3.setString(++st_count, bu_state_tin);
				stmt3.setString(++st_count, invoice_seq);
				stmt3.setString(++st_count, fy_year);
				//stmt3.setString(++st_count, temp_pdf_type);
				stmt3.setString(++st_count, inv_type);
				if(inv_type.equals("I"))
				{
					stmt3.setString(++st_count, "Y");
				}
				rset3=stmt3.executeQuery();
				while(rset3.next())
				{
					//attachment=rset.getString(1)==null?"":rset.getString(1);
					VTEMP_MAIL_ATTACHMENT.add(rset3.getString(1)==null?"":rset3.getString(1));
					VTEMP_MAIL_ANNEXURE_ATTACHMENT_FLAG.add("");
				}
				rset3.close();
				stmt3.close();
				
				String companyNm=utilBean.getCompanyName(conn,comp_cd);
				String mail_body="";
				
				if(inv_type.equals("R"))
				{
					mail_body="Dear Sir/Madam,"
							+ "\n\nPlease find enclosed Remittance for " +customerNm+" Remittance : "+invoiceNo
							+ " dated "+invoiceDt.replaceAll("/", "-")+"."
							+ "\nPlease note payment will be processed on or before "+dueDate.replaceAll("/", "-")
							+ "\n\n\nThank You,"
							+ "\n\n"+companyNm+""
							+ "\n"+ownAddress+", "
							+ "\n"+ownCity+", "+ownState+" - "+ownPin+""
							+ "\nEmail: "+ownEmail+""
							+ "\nPh: "+ownPhone+""	
							+ "\n\nThis is an auto-generated email from the system, please do not reply to this email.";
				}
				else
				{
					mail_body="Dear Sir/Madam,"
							+ "\n\nPlease find enclosed Invoice# "+invoiceNo+" dated "+invoiceDt.replaceAll("/", "-")+".";
					mail_body+= "\n\nYou are requested to pay on or before the due date "+dueDate+". If already paid or no amount is due kindly ignore this message.";
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
				}
				
				VMAIL_FROM_LIST.add(utilBean.getFromMailId(conn));
				VMAIL_TO_LIST.add(to_list);
				VMAIL_CC_LIST.add(cc_list);
				VMAIL_BCC_LIST.add(bcc_list);
				VMAIL_SUBJECT.add(subject);
				//VMAIL_ATTACHMENT.add(attachment);
				VMAIL_ATTACHMENT.add(VTEMP_MAIL_ATTACHMENT);
				VMAIL_ANNEXURE_ATTACHMENT_FLAG.add(VTEMP_MAIL_ANNEXURE_ATTACHMENT_FLAG);
				
				if(inv_type.equals("I"))
				{
					VMAIL_ATTACHMENT_PATH.add(CommonVariable.signed_derv_inv_path);
				}
				else
				{
					VMAIL_ATTACHMENT_PATH.add(CommonVariable.derv_remittance_path);
				}
				
				String annexure_folder=inv_type+"_"+fy_year+"_"+bu_state_tin+"_"+invoice_seq;
				VMAIL_ANNEXURE_ATTACHMENT_PATH.add(CommonVariable.freeflow_annexure_path+""+annexure_folder+"/");
				VMAIL_BODY.add(mail_body);	
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getMarketExposureDealWiseReportViewCurrentDate()
	{
		String function_nm="getMarketExposureDealWiseReportViewCurrentDate()";
		try
		{
			
			for(int i=0; i<VCOUNTERPARTY_CD.size(); i++)
			{
				int Count=0;
				String buy_sell="";
				if(VBUY_SELL.elementAt(i).equals("BUY"))
				{
					buy_sell="Buy";
				}
				else
				{
					buy_sell="Sell";
				}
				String map_id=""+VCOUNTERPARTY_CD.elementAt(i)+"-"+VAGMT_NO.elementAt(i)+"-"+VCONT_NO.elementAt(i)+"-"+VINSTRUMENT_NO.elementAt(i);
				String queryString = "SELECT COUNT(*), SUM(TOT_MTM_TOTAL) "
						+ "FROM FMS_MR_EXPO_EOD_MST "
						+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE='V' AND BUY_SELL=? "
						+ "AND MAPPING_ID=? AND FIN_CURVE=?";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, report_dt);
				stmt.setString(3, ""+VCOUNTERPARTY_CD.elementAt(i));
				stmt.setString(4, buy_sell);
				stmt.setString(5, map_id);
				stmt.setString(6, ""+VDEAL_PRICE_CURVE.elementAt(i));
				rset=stmt.executeQuery();
				if(rset.next())
				{
					Count = rset.getInt(1);
				}
				
				if(Count>0)
				{
					VEXPOTOTALPNL.add(nf.format(rset.getDouble(2)));	
				}
				else
				{
					VEXPOTOTALPNL.add("");	
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
	
	public void getMarketExposureDealWiseReportView(String reportDt)
	{
		String function_nm="getMarketExposureDealWiseReportView()";
		try
		{
			
			for(int i=0; i<VCOUNTERPARTY_CD.size(); i++)
			{
				int Count=0;
				String buy_sell="";
				if(VBUY_SELL.elementAt(i).equals("BUY"))
				{
					buy_sell="Buy";
				}
				else
				{
					buy_sell="Sell";
				}
				String map_id=""+VCOUNTERPARTY_CD.elementAt(i)+"-"+VAGMT_NO.elementAt(i)+"-"+VCONT_NO.elementAt(i)+"-"+VINSTRUMENT_NO.elementAt(i);
				
				String queryString = "SELECT COUNT(*), SUM(TOT_MTM_TOTAL) "
						+ "FROM FMS_MR_EXPO_EOD_MST "
						+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE='V' AND BUY_SELL=? "
						+ "AND MAPPING_ID=? AND FIN_CURVE=?";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, reportDt);
				stmt.setString(3, ""+VCOUNTERPARTY_CD.elementAt(i));
				stmt.setString(4, buy_sell);
				stmt.setString(5, map_id);
				stmt.setString(6, ""+VDEAL_PRICE_CURVE.elementAt(i));
				rset=stmt.executeQuery();
				if(rset.next())
				{
					Count = rset.getInt(1);			
					if(Count>0)
					{
						VEXPOFREEZEESTATUS.add(""+Count);	
						VEXPOFREEZEETOTALPNL.add(nf.format(rset.getDouble(2)));	
					}
					else
					{
						VEXPOFREEZEESTATUS.add(""+Count);	
						VEXPOFREEZEETOTALPNL.add("");
					}
				}
				else
				{
					VEXPOFREEZEESTATUS.add(""+Count);	
					VEXPOFREEZEETOTALPNL.add("");	
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
	
	public void getSapInvoiceApprovalDetail()
	{
		String function_nm="getSapInvoiceApprovalDetail()";
		try
		{
			queryString ="SELECT SAP_APPROVAL,SAP_APPROVED_BY,TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY') "
					+ "FROM FMS_DERV_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
					+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? AND INV_TYPE=? AND INV_FLAG='F'";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
 	        stmt.setString(2, fy_year);
 	        stmt.setString(3, invoice_seq);
 	        stmt.setString(4, bu_state_tin);
 	        stmt.setString(5, inv_type);
 	        rset=stmt.executeQuery();
			if(rset.next())
			{
				String sap_app_by=rset.getString(2)==null?"":rset.getString(2);
				sap_approved_by=utilBean.getEmpName(conn,sap_app_by);
				sap_approved_dt=rset.getString(3)==null?"":rset.getString(3);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void generateDervInvoiceXML()
	{
		String function_nm="generateDervInvoiceXML()";
		
		try
		{
			String sysdate=dateUtil.getSysdate();
			String sysdateWithTime=dateUtil.getSysdateWithTime24hr();
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
			String account=utilBean.getCounterpartySAPcode(conn,counterparty_cd);
			String taxCode="";
			String monthNm="";
			String paymentDueDt="";
			
			String netPayable="";
			String cont_no="";
			String agmt_no="";
			String instrument_no="";
			String qty="";
			String invoiceAmt="";
			
			String fms_MessageId="";
			
			String plant_seq="";
		    String plantAddress="";
		    String plantCity="";
		    String plantState="";
		    String plantPin="";
		    String plantNm="";
		    
		    String cash_flow="";
		    
		    String documentDate="";
		    String postingDate=xml_sysdate; //AS DISCUSSED WITH VIJAY AND DIVYA ON 20250811 IN WORKSHOP CHENNAI, POSTING DATE WILL BE XML APPROVAL DATE
			String invoice_typ="";
			String invDt="";
			String contract_typ="";
			double temp_net_payable=0;
			String assignmentNo="";
			Vector VNET_PAYABLE = new Vector();
			Vector VDEAL_NO = new Vector();
			Vector VCONT_REF = new Vector();
			
					
			queryString="SELECT TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),BU_UNIT,INVOICE_NO,NET_PAYABLE_AMT,"
					+ "CONT_NO,ALLOC_QTY,"
					+ "INVOICE_RAISED_IN,TO_CHAR(DUE_DT,'DD/MM/YYYY'),INVOICE_AMT,PLANT_SEQ,"
					+ "AGMT_NO,"
					+ "TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(APPROVED_DT,'DD/MM/YYYY'),INSTRUMENT_NO,INV_TYPE,CONTRACT_TYPE "
					+ "FROM FMS_DERV_INVOICE_MST A "
					+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
					+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? AND INV_TYPE=? AND INV_FLAG='F'";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, fy_year);
			stmt.setString(3, invoice_seq);
			stmt.setString(4, bu_state_tin);
			stmt.setString(5, inv_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String period_end=rset.getString(1)==null?"":rset.getString(1);
				if(!period_end.equals(""))
				{
					String[] temp_split=period_end.split("/");
					productionPeriodMonth=temp_split[1];
					productionPeriodYear=temp_split[2];		
				}
				monthNm=""+dateUtil.getShortMonthName(period_end);
				
				String buUnit=rset.getString(2)==null?"":rset.getString(2);
				
				String buStateNm="";
				String buAbbr="";
				queryString1 = "SELECT PLANT_STATE,PLANT_ABBR "
						+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
						+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND COMPANY_CD=? AND SEQ_NO=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) ";
				stmt1 = conn.prepareStatement(queryString1);
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
				
				cont_no=rset.getString(5)==null?"":rset.getString(5);
				qty=rset.getString(6)==null?"":nf.format(rset.getDouble(6));
				String inv_raised_in=rset.getString(7)==null?"":rset.getString(7);
				netPayable=rset.getString(4)==null?"":nf.format(rset.getDouble(4));
				VNET_PAYABLE.add(netPayable);
				temp_net_payable+=rset.getDouble(4);
				paymentDueDt=rset.getString(8)==null?"":rset.getString(8);
				if(!paymentDueDt.equals(""))
				{
					String splitPayDt[]=paymentDueDt.split("/");
					paymentDueDt=splitPayDt[2]+""+splitPayDt[1]+""+splitPayDt[0];
				}
				
				invoiceAmt=rset.getString(9)==null?"":nf.format(rset.getDouble(9));
				plant_seq=rset.getString(10)==null?"":rset.getString(10);
				agmt_no=rset.getString(11)==null?"":rset.getString(11);
				
				cash_flow="Commodity";
				
				String invoiceDt=rset.getString(12)==null?"":rset.getString(12);
				invDt=invoiceDt;
				if(!invoiceDt.equals(""))
				{
					String[] temp_split=invoiceDt.split("/");
					documentDate=temp_split[2]+""+temp_split[1]+""+temp_split[0];
				}
				
				/*String approve_dt=rset.getString(13)==null?"":rset.getString(13);
				if(!approve_dt.equals(""))
				{
					String[] temp_split=approve_dt.split("/");
					postingDate=temp_split[2]+""+temp_split[1]+""+temp_split[0];
				}*/
				
				instrument_no=rset.getString(14)==null?"":rset.getString(14);
				invoice_typ=rset.getString(15)==null?"":rset.getString(15);
				contract_typ=rset.getString(16)==null?"":rset.getString(16);
				String cont_ref="";
				String queryString2="SELECT A.CONT_REF_NO "
						+ "FROM FMS_DERV_CONT_MST A,FMS_DERV_INSTRUMENT_MST B "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.CONT_NO=? AND B.INSTRUMENT_NO=? "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, counterparty_cd);
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
				
				assignmentNo=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, "", cont_no, "", contract_typ, instrument_no);
				VDEAL_NO.add(assignmentNo);
				VCONT_REF.add(cont_ref);
			}
			rset.close();
			stmt.close();
			
			HashMap plant_detail=utilBean.getCounterpartyPlantDetail(conn,comp_cd, "T", counterparty_cd, plant_seq);
            plantAddress=""+plant_detail.get("plant_address");
			plantCity=""+plant_detail.get("plant_city");
			plantState=""+plant_detail.get("plant_state");
			plantPin=""+plant_detail.get("plant_pin");
			plantNm=""+plant_detail.get("plant_name");
			
			
			String material_code="1168001";
	    	
			fms_MessageId=refNum.replaceAll("/", "-");
	    	
	    	String UserID = ""+utilBean.getUserName(conn,emp_cd);
	    	String businessAreaCode=utilBean.getBusinessAreaSAPcode(conn, comp_cd, "T", contract_typ, invDt);
		    
			DocumentBuilderFactory docFactory = xmlUtil.dcoumentBuilderFactory();
		    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		    Document doc = docBuilder.newDocument();
		    
		    String arAp="";
		    if(inv_type.equals("R"))
	    	{
		    	arAp="Ap";
	    	}
		    else
		    {
		    	arAp="Ar";
		    }
		    //root fmsng
		    Element fmsng = doc.createElement("EmsSAP"+arAp+"Message");
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
		    if(inv_type.equals("R"))
	    	{
		    	DocumentType.appendChild(doc.createTextNode("X1"));
	    	}
		    else
		    {
		    	 DocumentType.appendChild(doc.createTextNode("X2"));
		    }
		    DocumentDate.appendChild(doc.createTextNode(documentDate));
		    PostingDate.appendChild(doc.createTextNode(postingDate));
		    AccountingPeriodMonth.appendChild(doc.createTextNode(accountingPeriodMonth));
		    AccountingPeriodYear.appendChild(doc.createTextNode(accountingPeriodYear));
		    InternalLegalEntity.appendChild(doc.createTextNode(utilBean.getCompanySAPcode(conn, comp_cd))); //NEED TO CHEACK FOR COMPANY BASE LOGIN JD-20230728
		    DocHeaderText.appendChild(doc.createTextNode(docHeaderText));
		    RefNum.appendChild(doc.createTextNode(refNum));
		    EmsRefNum.appendChild(doc.createTextNode(refNum));
		    Currency.appendChild(doc.createTextNode("USD")); //NO NEED TO SEND OTHER TYPE OF CURRENCY - JD
		    
		    AddressLine1.appendChild(doc.createTextNode(plantNm));
		    AddressLine2.appendChild(doc.createTextNode(plantAddress));
		    AddressLine3.appendChild(doc.createTextNode(plantState));
		    AddressLine4.appendChild(doc.createTextNode(plantCity));
		    AddressLine5.appendChild(doc.createTextNode(plantPin));
		    
		    UserName.appendChild(doc.createTextNode(UserID));
		    
		    int i=0;
		    if(!netPayable.equals(""))
		    {
		    	String sign = "";
		    	String pk = "01";
		    	if(inv_type.equals("R"))
		    	{
		    		pk="31";
		    	}
		    	
		    	i+=1;
		    	Element InvoiceDetail = doc.createElement("InvoiceDetail");
		    	Invoice.appendChild(InvoiceDetail);
		    	
		    	String vendor_cust="";
		    	if(inv_type.equals("R"))
		    	{
		    		vendor_cust="VendorId";
		    	}
		    	else if(inv_type.equals("I"))
		    	{
		    		vendor_cust="CustomerId";
		    	}
		    	Element VendorId  = doc.createElement(vendor_cust);
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
		    	CurrencyAmount.appendChild(doc.createTextNode(sign+""+nf.format(temp_net_payable)));
		    	TaxCode.appendChild(doc.createTextNode(taxCode));
		    	//BusinessArea.appendChild(doc.createTextNode(utilBean.getCompanySAPcode(conn, comp_cd))); //NEED TO CHEACK FOR COMPANY BASE LOGIN JD-20230728
		    	BusinessArea.appendChild(doc.createTextNode(businessAreaCode));
		    	Volume.appendChild(doc.createTextNode(qty));
		    	VolumeUnit.appendChild(doc.createTextNode("MMB"));
		    	
		    	ItemText.appendChild(doc.createTextNode(refNum));

		    	//ReferenceKey1.appendChild(doc.createTextNode(counterparty_abbr+"-"+contract_type+cont_no));
		    	ReferenceKey1.appendChild(doc.createTextNode(assignmentNo));
		    	ReferenceKey2.appendChild(doc.createTextNode(UserID));
		    	
		    	ProductionPeriod.appendChild(doc.createTextNode(productionPeriodYear+""+productionPeriodMonth));
		    	AssignmentNumber.appendChild(doc.createTextNode(refNum));
		    	PaymentTerms.appendChild(doc.createTextNode("ZB00")); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN
		    	PaymentDueDate.appendChild(doc.createTextNode(paymentDueDt)); //AS PER SUNIDHI MAIL 16/08/2023
		    	
		    	Material.appendChild(doc.createTextNode(utilBean.PrePaddingZero(material_code, 18))); //it was not added erlier
		    }
		    for(int k=0; k<VNET_PAYABLE.size(); k++)
		    {
			    if(!VNET_PAYABLE.elementAt(k).equals(""))
			    {
			    	String sign = "-";
			    	String pk = "50";
			    	String net_payable=""+VNET_PAYABLE.elementAt(k);
			    	assignmentNo = ""+VDEAL_NO.elementAt(k);
			    	String cont_ref = ""+VCONT_REF.elementAt(k);
			    	if(Double.parseDouble(net_payable) < 0)
			    	{
			    		pk = "40";
			    		sign = "";
			    		net_payable = net_payable.replace("-", "");
			    	}
			    	i+=1;
			    	Element InvoiceDetail = doc.createElement("InvoiceDetail");
			    	Invoice.appendChild(InvoiceDetail);
			    	String vendor_cust="";
			    	if(inv_type.equals("R"))
			    	{
			    		vendor_cust="VendorId";
			    	}
			    	else if(inv_type.equals("I"))
			    	{
			    		vendor_cust="CustomerId";
			    	}
			    	Element VendorId  = doc.createElement(vendor_cust);
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
				    
				    InvoiceDetail.appendChild(VendorId);
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
				    
				    VendorId.appendChild(doc.createTextNode(account));
			    	LineSeqNo.appendChild(doc.createTextNode(""+i));
			    	PostingKey.appendChild(doc.createTextNode(pk));
			    	
			    	String countpty_category=""+utilBean.getCounterpartyCategory(conn,counterparty_cd);
			    	
			    	String tempAccount="";
			    	String itemText="";
			    		tempAccount="6841000";
			    		itemText="Swaps IG";
			    	Account.appendChild(doc.createTextNode(utilBean.PrePaddingZero(tempAccount, 10))); //IG OR NG
			    	CurrencyAmount.appendChild(doc.createTextNode(sign+""+net_payable));
			    	TaxCode.appendChild(doc.createTextNode(taxCode));
			    	BusinessArea.appendChild(doc.createTextNode(businessAreaCode));
			    	
			    	ItemText.appendChild(doc.createTextNode(itemText+" "+monthNm+" "+productionPeriodYear+" ("+cont_ref+")"));
			    	
			    	//CurrencyAmount.appendChild(doc.createTextNode(sign+""+net_payable));
			    	//TaxCode.appendChild(doc.createTextNode(taxCode));
			    	Volume.appendChild(doc.createTextNode(qty));
			    	VolumeUnit.appendChild(doc.createTextNode("MMB"));
			    	
			    	ReferenceKey1.appendChild(doc.createTextNode(assignmentNo));
			    	ReferenceKey2.appendChild(doc.createTextNode(UserID));
			    	
			    	ProductionPeriod.appendChild(doc.createTextNode(productionPeriodYear+""+productionPeriodMonth));
			    	AssignmentNumber.appendChild(doc.createTextNode(refNum));
			    	PaymentTerms.appendChild(doc.createTextNode("ZB00"));
			    	PaymentDueDate.appendChild(doc.createTextNode(paymentDueDt));
			    	
			    	Material.appendChild(doc.createTextNode(utilBean.PrePaddingZero(material_code, 18)));
			    }
			}
		    
		    
		    TransformerFactory transformerFactory = TransformerFactory.newInstance();
			//transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD","");
			//transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet","");
			
		    Transformer transformer = transformerFactory.newTransformer();
		    DOMSource source = new DOMSource(doc);
		    
		    String xmlFileNm="";
		    String datetime="";
		    datetime=splitSys[0].replaceAll("/", "")+""+splitSys[1].replaceAll(":", "");
		    
		    if(inv_type.equals("R"))
		    {
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
			    else //ADDED WHEN INVOICE NUMBER IS NOT GENERATED
		    	{
		    		xmlFileNm="AP_"+fms_MessageId+".xml";
		    	}
		    }
		    else
		    {
		    	if(!fms_MessageId.equals(""))
			    {
			    	if(sap_approval_flag.equals("Y"))
			        {
			    		xmlFileNm="AR_"+fms_MessageId+"_"+datetime+".xml";
			        }
			    	else
			    	{
			    		xmlFileNm="AR_"+fms_MessageId+".xml";
			    	}
			    }
			    else //ADDED WHEN INVOICE NUMBER IS NOT GENERATED
		    	{
		    		xmlFileNm="AR_"+fms_MessageId+".xml";
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
				        queryString="SELECT COUNT(*) "
				        		+ "FROM FMS_DERV_INV_FILE_DTL "
				        		+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
				        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND INV_TYPE=?";
				        stmt2 = conn.prepareStatement(queryString);
		    	        stmt2.setString(1, comp_cd);
		    	        stmt2.setString(2, bu_state_tin);
		    	        stmt2.setString(3, invoice_seq);
		    	        stmt2.setString(4, fy_year);
		    	        stmt2.setString(5, "X");
		    	        stmt2.setString(6, inv_type);
		    	        rset2=stmt2.executeQuery();
				        if(rset2.next())
				        {
				        	count=rset2.getInt(1);
				        }
				        rset2.close();
				        stmt2.close();
			
				        if(count > 0)
				        {
				        	queryString="UPDATE FMS_DERV_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
				        			+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
					        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND INV_TYPE=?";
				        	stmt3 = conn.prepareStatement(queryString);
			    	        stmt3.setString(1, xmlFileNm);
			    	        stmt3.setString(2, emp_cd);
			    	        stmt3.setString(3, comp_cd);
			    	        stmt3.setString(4, bu_state_tin);
			    	        stmt3.setString(5, invoice_seq);
			    	        stmt3.setString(6, fy_year);
			    	        stmt3.setString(7, "X");
			    	        stmt3.setString(8, inv_type);
		    	        	stmt3.executeUpdate();
		    	        	
		    	        	stmt3.close();
				        }
				        else
				        {
				        	queryString="INSERT INTO FMS_DERV_INV_FILE_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,"
				        			+ "FILE_NAME,ENT_BY,ENT_DT,INV_TYPE) "
				        			+ "VALUES(?,?,?,?,?,"
				        			+ "?,?,SYSDATE,?)";
				        	stmt3 = conn.prepareStatement(queryString);
			    	        stmt3.setString(1, comp_cd);
			    	        stmt3.setString(2, bu_state_tin);
			    	        stmt3.setString(3, invoice_seq);
			    	        stmt3.setString(4, fy_year);
			    	        stmt3.setString(5, "X");
			    	        stmt3.setString(6, xmlFileNm);
			    	        stmt3.setString(7, emp_cd);
			    	        stmt3.setString(8, inv_type);
		    	        	stmt3.executeUpdate();
		    	        	
		    	        	stmt3.close();
				        }
	    	
				        conn.commit();
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
        		queryString="SELECT FILE_NAME "
		        		+ "FROM FMS_DERV_INV_FILE_DTL "
		        		+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
		        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=?";
        		stmt = conn.prepareStatement(queryString);
    	        stmt.setString(1, comp_cd);
    	        stmt.setString(2, bu_state_tin);
    	        stmt.setString(3, invoice_seq);
    	        stmt.setString(4, fy_year);
    	        stmt.setString(5, "X");
    	        rset=stmt.executeQuery();
		        if(rset.next())
		        {
		        	xmlfile_name=rset.getString(1)==null?"":rset.getString(1);
				}
		        rset.close();
    	        stmt.close();
			}
			
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
					
				    DocumentBuilderFactory dbFactory = xmlUtil.dcoumentBuilderFactory();
				    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				    Document doc = dBuilder.parse(file);
				    
				    doc.getDocumentElement().normalize();
				    
				    zeroTotal="0.00";
				    String arAp="";
				    if(inv_type.equals("R"))
			    	{
				    	arAp="Ap";
			    	}
				    else
				    {
				    	arAp="Ar";
				    }
					NodeList nList = doc.getElementsByTagName("EmsSAP"+arAp+"Message");
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
											else if(childTag2.equals("DocumentDate")){
												documentDate=nodes2.item(k).getTextContent();											
											}
											else if(childTag2.equals("DocumentNo")){
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
											else if(childTag2.equals("Account") || childTag2.equals("CustomerId") || childTag2.equals("VendorId")) 
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
										currencyamount = currencyamount.replaceFirst("(\\.[0-9]+).*", "$1");
										if(!currencyamount.equals(""))
										{
											zeroTotal=nf.format(Double.parseDouble(zeroTotal)+Double.parseDouble(currencyamount));
										}
										
										VLINESEQNO.add(lineseqno);
										VPOSTINGKEY.add(postingkey);
										VACCOUNT.add(account);
										VCURRENCYAMOUNT.add(nf.format(Double.parseDouble(currencyamount)));
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
	
	public void getDervInvoiceDtl()
	{
		String function_nm="getDervInvoiceDtl()";
		
		try
		{
			String[] all_agmt_no = agmt_no.split(", ");
			String[] all_agmt_rev = agmt_rev.split(", ");
			String[] all_cont_type = cont_type.split(", ");
			String[] all_cont_no = cont_no.split(", ");
			String[] all_cont_rev = cont_rev.split(", ");
			String[] all_instrument_no = instrument_no.split(", ");
			String[] all_period_end_dt = period_end_dt.split(", ");
			String fin_year="";
			for(int i=0;i<all_cont_no.length;i++)
			{
				fin_year=dateUtil.getFinancialYear(all_period_end_dt[i]);
				int cnt1=0;
				String queryString1="SELECT INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
						+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,BUY_PRICE,BUY_PRICE_UNIT,BUY_AMT,"
						+ "INVOICE_RAISED_IN,INVOICE_AMT,NET_PAYABLE_AMT,"
						+ "REMARK_1,REMARK_2,FINANCIAL_YEAR,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),"
						+ "INVOICE_REF_NO,INVOICE_ID_SEQ,CHECKED_FLAG,AUTHORIZED_FLAG,APPROVED_FLAG "
						+ "FROM FMS_DERV_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
						+ "AND BU_STATE_TIN=? "
						+ "AND FINANCIAL_YEAR=? AND INSTRUMENT_NO=? AND INV_TYPE=? AND INV_FLAG='F'";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(++cnt1, comp_cd);
				stmt1.setString(++cnt1, counterparty_cd);
				stmt1.setString(++cnt1, all_cont_no[i]);
				stmt1.setString(++cnt1, all_agmt_no[i]);
				stmt1.setString(++cnt1, plant_seq);
				stmt1.setString(++cnt1, bu_plant_seq);
				stmt1.setString(++cnt1, all_cont_type[i]);
				stmt1.setString(++cnt1, bu_state_tin);
				stmt1.setString(++cnt1, fin_year);
				stmt1.setString(++cnt1, all_instrument_no[i]);
				stmt1.setString(++cnt1, inv_type);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					invoice_seq=rset1.getString(1)==null?"":rset1.getString(1);
					invoice_no=rset1.getString(2)==null?"":rset1.getString(2);
					inv_dt=rset1.getString(3)==null?"":rset1.getString(3);
					inv_due_dt=rset1.getString(4)==null?"":rset1.getString(4);
					double alloc_qty=rset1.getDouble(5);
					String sell_price=rset1.getString(6)==null?"":rset1.getString(6);
					String sell_price_unit=rset1.getString(7)==null?"":rset1.getString(7);
					String sell_amt=rset1.getString(8)==null?"":rset1.getString(8);
					String buy_price=rset1.getString(9)==null?"":rset1.getString(9);
					String buy_price_unit=rset1.getString(10)==null?"":rset1.getString(10);
					String buy_amt=rset1.getString(11)==null?"":rset1.getString(11);
					String inv_raised_in=rset1.getString(12)==null?"":rset1.getString(12);
					String invoice_amt=rset1.getString(13)==null?"":rset1.getString(13);
					String net_payable_amt=rset1.getString(14)==null?"":rset1.getString(14);
					remark1=rset1.getString(15)==null?"":rset1.getString(15);
					remark2=rset1.getString(16)==null?"":rset1.getString(16);
					String financial_year=rset1.getString(17)==null?"":rset1.getString(17);
					period_start_dt=rset1.getString(18)==null?"":rset1.getString(18);
					period_end_dt=rset1.getString(19)==null?"":rset1.getString(19);
					inv_ref=rset1.getString(20)==null?"":rset1.getString(20);
					invoice_id_seq=rset1.getString(21)==null?"":rset1.getString(21);
					temp_tot_inv_amt+=Double.parseDouble(invoice_amt);
					
					if(activityFlag.equals("CHECK")) 
					{
						activity_value=rset1.getString(22)==null?"":rset1.getString(22);
					}
					else if(activityFlag.equals("AUTHORIZE")) 
					{
						activity_value=rset1.getString(23)==null?"":rset1.getString(23);
					}
					else if(activityFlag.equals("APPROVE")) 
					{
						activity_value=rset1.getString(24)==null?"":rset1.getString(24);
					}
					
					VCOUNTERPARTY_CD.add(counterparty_cd);
					VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn, counterparty_cd));
					VAGMT_TYPE.add(agmt_type);
					VAGMT_NO.add(all_agmt_no[i]);
					VAGMT_REV.add(all_agmt_rev[i]);
					VCONT_TYPE.add(all_cont_type[i]);
					VCONT_NO.add(all_cont_no[i]);
					VCONT_REV.add(all_cont_rev[i]);
					VDEAL_MAPPING.add(""+utilBean.NewDealMappingId(comp_cd, counterparty_cd, all_agmt_no[i], all_agmt_rev[i], all_cont_no[i], all_cont_rev[i], all_cont_type[i], all_instrument_no[i]));
					
					VINSTRUMENT_NO.add(all_instrument_no[i]);
					VBUY_SELL.add(buy_sell);
					VPERIOD_START_DT.add(period_start_dt);
					VPERIOD_END_DT.add(period_end_dt);
					VBOOKED_MMBTU.add(nf.format(alloc_qty));
				  	VCONT_START_DT.add(period_start_dt);
				  	VCONT_END_DT.add(period_end_dt);
				  	VDEAL_PRICE_CURVE.add(financial_curve);
				  	VINVOICE_AMT.add(nf.format(Double.parseDouble(invoice_amt)));
				  	
				  	int cnt2=0;
				  	String cont_ref="";
				  	String trade_dt="";
				  	String instrument_volume_unit="";
				  	String buySell="";
				  	String instrument_type="";
				  	String curve_nm="";
				  	String instru_duration="";
				  	queryString2="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_TYPE,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,A.CONT_NO,A.CONT_REV,A.CONT_NAME,A.CONT_REF_NO,"
							+ "B.INSTRUMENT_NO,B.INSTRUMENT_TYPE,B.BUY_SELL,B.STATUS,B.QTY,B.QTY_UNIT,B.RATE,B.RATE_UNIT,"
							+ "B.PRODUCT_NM,B.CURVE_NM,B.PROJ_METHOD,TO_CHAR(B.CONT_DD_MM_YR,'DD/MM/YYYY'),"
							+ "TO_CHAR(B.PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(B.PRICE_END_DT,'DD/MM/YYYY'),B.CONV_FACTOR,TO_CHAR(A.TRADE_DT,'DD/MM/YYYY') "
							+ "FROM FMS_DERV_CONT_MST A,FMS_DERV_INSTRUMENT_MST B "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
							+ "AND B.STATUS='Y' AND B.INSTRUMENT_NO=? "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO";
				  	stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(++cnt2, comp_cd);
					stmt2.setString(++cnt2, counterparty_cd);
					stmt2.setString(++cnt2, all_agmt_no[i]);
					stmt2.setString(++cnt2, all_cont_no[i]);
					stmt2.setString(++cnt2, all_cont_type[i]);
					stmt2.setString(++cnt2, all_instrument_no[i]);
					rset2=stmt2.executeQuery();
					while(rset2.next())
					{
						cont_ref=rset2.getString(10)==null?"":rset2.getString(10);
						trade_dt=rset2.getString(26)==null?"":rset2.getString(26);
						String instrument_volume_unit_cd = rset2.getString(16)==null?"":rset2.getString(16);
						instrument_volume_unit = utilBean.getEnergyUnitNm(conn, instrument_volume_unit_cd);
						buySell = rset2.getString(13)==null?"":rset2.getString(13);
						instrument_type = rset2.getString(12)==null?"":rset2.getString(12);
						curve_nm = rset2.getString(20)==null?"":rset2.getString(20);
						String start_dt = rset2.getString(23)==null?"":rset2.getString(23);
						String end_dt = rset2.getString(24)==null?"":rset2.getString(24);
						instru_duration = start_dt+"-"+end_dt;
					}
					rset2.close();
					stmt2.close();
					VCONT_REF.add(cont_ref);
					VTRADE_DT.add(trade_dt);
					VQTY_UNIT.add(instrument_volume_unit);
					if(buySell.equals("BUY"))
					{
						BigDecimal float_val=new BigDecimal(sell_price);
						VSELL_RATE.add(df3.format(float_val));
					}
					else
					{
						VSELL_RATE.add(nf2.format(Double.parseDouble(sell_price)));
					}
				  	VSELL_AMT.add(sell_amt);
				  	if(buySell.equals("SELL"))
					{
				  		BigDecimal float_val=new BigDecimal(buy_price);
				  		VBUY_RATE.add(df3.format(float_val));
					}
				  	else
				  	{
				  		VBUY_RATE.add(nf2.format(Double.parseDouble(buy_price)));
				  	}
				  	
				  	VBUY_AMT.add(buy_amt);
				  	VINSTRUMENT_TYPE.add(instrument_type);
				  	VCURVE_NM.add(curve_nm);
				  	VINSTRUMENT_DURATION.add(instru_duration);
				}
				rset1.close();
				stmt1.close();
			}
			HashMap bu_plant_detail=utilBean.getCounterpartyPlantDetail(conn,comp_cd, "B", comp_cd, bu_plant_seq);
			bu_plantAddress=""+bu_plant_detail.get("plant_address");
			bu_plantCity=""+bu_plant_detail.get("plant_city");
			bu_plantState=""+bu_plant_detail.get("plant_state");
			bu_plantPin=""+bu_plant_detail.get("plant_pin");
			bu_plantNm=""+bu_plant_detail.get("plant_name");
			
			HashMap plant_detail=utilBean.getCounterpartyPlantDetail(conn,comp_cd, "T", counterparty_cd, plant_seq);
			plantAddress=""+plant_detail.get("plant_address");
			plantCity=""+plant_detail.get("plant_city");
			plantState=""+plant_detail.get("plant_state");
			plantPin=""+plant_detail.get("plant_pin");
			
			tax_info=getCounterpartyPlantTaxInfo(comp_cd, "T", counterparty_cd, plant_seq);
			tax_info=tax_info.replaceAll("\n", "<br>");
			
			bu_tax_info=getCounterpartyPlantTaxInfo(comp_cd, "B", comp_cd, bu_plant_seq);
			bu_tax_info=bu_tax_info.replaceAll("\n", "<br>");
			
			if(temp_tot_inv_amt<0)
			{
				temp_tot_inv_amt=temp_tot_inv_amt*(-1);
			}
			total_inv_amt=nf.format(temp_tot_inv_amt);
			
			bank_formula=utilBean.getEntityBankFormula(conn, comp_cd, counterparty_cd, "T", "DERV", inv_dt);
			/*String queryString="SELECT COUNTERPARTY_CD,ENTITY,TO_CHAR(EFF_DT,'DD/MM/YYYY'),BANK_NAME,ACCOUNT_ID,IFSC_CODE,BRANCH_NAME,"
					+ "STATE_NM,CATEGORY  "
					+ "FROM FMS_ENTITY_BANK_MST A "
					+ "WHERE ENTITY=? AND COUNTERPARTY_CD=? AND COMPANY_CD=? AND CATEGORY=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_BANK_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.CATEGORY=B.CATEGORY)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, "B");
			stmt.setString(2, comp_cd);
			stmt.setString(3, comp_cd);
			stmt.setString(4, "DERV");
			rset=stmt.executeQuery();
			if(rset.next())
			{
				String bank_eff_dt = rset.getString(3)==null?"":rset.getString(3);
				String bank_name=rset.getString(4)==null?"":rset.getString(4);
				String bank_account_no=rset.getString(5)==null?"":rset.getString(5);
				String ifsc_code=rset.getString(6)==null?"":rset.getString(6);
				String bank_branch=rset.getString(7)==null?"":rset.getString(7);
				String bank_state=rset.getString(8)==null?"":rset.getString(8);
				String bank_category=rset.getString(9)==null?"":rset.getString(9);
				
				bank_formula=bank_name+", "+bank_branch+", "+bank_state+", A/C No : "+bank_account_no+", IFSC Code : "+ifsc_code;
			}
			rset.close();
			stmt.close();*/
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public String getCounterpartyPlantTaxInfo(String comp_cd, String entity, String counterparty_cd, String plant_seq) throws Exception
	{
		String function_nm="getCounterpartyPlantTaxInfo()";
		String tax_info="";
		try
		{
			String queryString3 = "SELECT A.STAT_NO, TO_CHAR(A.EFF_DT,'DD/MM/YYYY'), B.STAT_NM "
					+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B "
					+ "WHERE A.COUNTERPARTY_CD=? AND A.ENTITY=? "
					+ "AND A.PLANT_SEQ_NO=? AND A.COMPANY_CD=? "
					+ "AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL AND B.STAT_NM NOT IN ('VAT TIN','CST TIN')";
			stmt3 = conn.prepareStatement(queryString3);
			stmt3.setString(1, counterparty_cd);
			stmt3.setString(2, entity);
			stmt3.setString(3, plant_seq);
			stmt3.setString(4, comp_cd);
			rset3 = stmt3.executeQuery();
			while(rset3.next())
			{
				String no = rset3.getString(1)==null?"":rset3.getString(1);
				String nm = rset3.getString(3)==null?"":rset3.getString(3);

				tax_info+="\n"+nm+" : "+no;
			}
			stmt3.close();
			rset3.close();
				
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return tax_info;
	}
	
	public void getRemNumber()
	{
		String function_nm="getRemNumber()";
		try
		{
			String invoice_prefix=utilBean.getInvoicePrefix(conn,comp_cd);
			
			String fin_yr="";
			if(!fy_year.equals(""))
			{
				String[] temp = fy_year.split("-");
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
			
			String contType="V";
			if(!invoice_prefix.equals(""))
			{
				String invoice_id_seq="";
				String queryString1="SELECT MAX(INVOICE_ID_SEQ) "
						+ "FROM FMS_DERV_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
						+ "AND CONTRACT_TYPE IN ('V') AND INV_TYPE='R' AND INV_FLAG='F'";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, fy_year);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					invoice_id_seq = ""+(rset1.getInt(1)+1);
				}
				rset1.close();
				stmt1.close();
					
				int count=0;
				queryString="SELECT COUNT(*) "
						+ "FROM FMS_DERV_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
						+ "AND INVOICE_ID_SEQ=? AND CONTRACT_TYPE IN ('V') AND INV_TYPE='R' AND INV_FLAG='F'";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, fy_year);
				stmt.setString(3, invoice_id_seq);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					count += rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
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
					invoice_no=invoice_prefix+""+contType+"S"+utilBean.PrePaddingZero(invoice_id_seq, 4)+"/"+fin_yr;
					
					VINVOICE_ID_SEQ.add(invoice_id_seq);
					VINVOICE_NO.add(invoice_no);
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
	
	public void getInvoiceNumber()
	{
		String function_nm="getInvoiceNumber()";
		try
		{
			String invoice_prefix=utilBean.getInvoicePrefix(conn,comp_cd);
			
			String fin_yr="";
			if(!fy_year.equals(""))
			{
				String[] temp = fy_year.split("-");
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
			
			contType="'V'";
			invSeries="V";
			
			if(!invoice_prefix.equals(""))
			{
				int no_inv_no=10;
				for(int i=1;i<=no_inv_no;i++)
				{
					String invoice_id_seq=""+i;
					int count=0;
					queryString="SELECT COUNT(*) "
							+ "FROM FMS_DERV_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND BU_STATE_TIN=? AND INVOICE_ID_SEQ=? AND CONTRACT_TYPE IN ("+contType+") AND INV_TYPE='I' AND INV_FLAG='F'";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, fy_year);
					stmt.setString(3, bu_state_tin);
					stmt.setString(4, invoice_id_seq);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						count += rset.getInt(1);
					}
					rset.close();
					stmt.close();
					
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
	
	public void getInvType()
	{
		String function_nm="getInvType()";
		
		try
		{
			VMST_INV_TYPE_FLG.add("I");
			VMST_INV_TYPE_FLG.add("R");
			
			VMST_INV_TYPE.add("Invoice");
			VMST_INV_TYPE.add("Remittance");
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getDervDealInvMst()
	{
		String function_nm="getDervDealInvMst()";
		
		try
		{
			queryString = "SELECT MIN(TO_CHAR(INVOICE_DT,'YYYY')) "
					+" FROM FMS_DERV_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND INV_FLAG='F'";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				min_fy_year=rset.getString(1)==null?"":rset.getString(1);
			}
			rset.close();
			stmt.close();
			
			String temp_period_start_dt=""+dateUtil.getFirstDateOfMonth(month, year);
			String temp_period_end_dt=""+dateUtil.getLastDateOfMonth(month, year);
			
			for(int i=0; i<VMST_INV_TYPE_FLG.size(); i++)
			{
				int index=0;
						
				String queryString="SELECT DISTINCT INVOICE_SEQ,COUNTERPARTY_CD,BU_STATE_TIN "
						+ "FROM FMS_DERV_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND TO_DATE(?,'DD/MM/YYYY') <=INVOICE_DT AND TO_DATE(?,'DD/MM/YYYY') >= INVOICE_DT AND INV_TYPE=? AND INV_FLAG='F'";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, temp_period_start_dt);
				stmt.setString(3, temp_period_end_dt);
				stmt.setString(4, ""+VMST_INV_TYPE_FLG.elementAt(i));
				rset=stmt.executeQuery();
				while(rset.next())
				{
					index+=1;
					
					String inv_seq=rset.getString(1)==null?"":rset.getString(1);
					String contPtyCd=rset.getString(2)==null?"":rset.getString(2);
					String buStTin=rset.getString(3)==null?"":rset.getString(3);
					String own_cd="";
					String countpty_cd="";
					String agmtno="";
					String agmtrev="";
					String contno="";
					String contrev="";
					String cont_type="";
					String instrument_no="";
					String deal_no="";
					String plant_seq="";
					String bu_plant_seq="";
					String billing_cycle="";
					String fin_year="";
					String invoice_no="";
					String invoice_seq="";
					String checked_flag="";
					String approved_flag="";
					String pdf_flg="";
					String pdf_ori="";
					String sap_approved_flag="";
					String bu_state_tin="";
					String period_start_dt="";
					String period_end_dt="";
					String invoice_dt="";
					String authorized_flag="";
					String invoice_type="";
					String contRef="";
					
					String queryString1="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE, "
							+ "BU_UNIT,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),"
							+ "INVOICE_NO,CHECKED_FLAG,APPROVED_FLAG,INVOICE_SEQ,FINANCIAL_YEAR,INV_TYPE,"
							+ "PDF_INV_DTL,BU_STATE_TIN, "
							+ "PRINT_BY_ORI,SAP_APPROVAL,INSTRUMENT_NO,PLANT_SEQ,FREQ,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),AUTHORIZED_FLAG,INV_TYPE "
							+ "FROM FMS_DERV_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND TO_DATE(?,'DD/MM/YYYY') <=INVOICE_DT AND TO_DATE(?,'DD/MM/YYYY') >= INVOICE_DT "
							+ "AND INV_TYPE=? AND INVOICE_SEQ=? AND COUNTERPARTY_CD=? AND BU_STATE_TIN=? AND INV_FLAG='F' "
							+ "ORDER BY INVOICE_SEQ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, temp_period_start_dt);
					stmt1.setString(3, temp_period_end_dt);
					stmt1.setString(4, ""+VMST_INV_TYPE_FLG.elementAt(i));
					stmt1.setString(5, inv_seq);
					stmt1.setString(6, contPtyCd);
					stmt1.setString(7, buStTin);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						own_cd=rset1.getString(1)==null?"":rset1.getString(1);
						countpty_cd=rset1.getString(2)==null?"":rset1.getString(2);
						String temp_agmtno=rset1.getString(3)==null?"0":rset1.getString(3);
						String temp_agmtrev=rset1.getString(4)==null?"0":rset1.getString(4);
						String temp_contno=rset1.getString(5)==null?"0":rset1.getString(5);
						String temp_contrev=rset1.getString(6)==null?"0":rset1.getString(6);
						String temp_cont_type=rset1.getString(7)==null?"V":rset1.getString(7);
						String temp_instrument_no=rset1.getString(21)==null?"":rset1.getString(21);
						String tmp_period_start_dt=rset1.getString(9)==null?"":rset1.getString(9);
						String tmp_period_end_dt=rset1.getString(10)==null?"":rset1.getString(10);
						if(contno.equals(""))
						{
							agmtno=temp_agmtno;
							agmtrev=temp_agmtrev;
							contno=temp_contno;
							contrev=temp_contrev;
							cont_type=temp_cont_type;
							instrument_no=temp_instrument_no;
							period_start_dt=tmp_period_start_dt;
							period_end_dt=tmp_period_end_dt;
						}
						else
						{
							agmtno+=", "+temp_agmtno;
							agmtrev+=", "+temp_agmtrev;
							contno+=", "+temp_contno;
							contrev+=", "+temp_contrev;
							cont_type+=", "+temp_cont_type;
							instrument_no+=", "+temp_instrument_no;
							period_start_dt+=", "+tmp_period_start_dt;
							period_end_dt+=", "+tmp_period_end_dt;
						}
						
						if(deal_no.equals(""))
						{
							deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, temp_agmtno, temp_agmtrev, temp_contno, temp_contrev, temp_cont_type, temp_instrument_no);
						}
						else
						{
							deal_no+=", "+utilBean.NewDealMappingId(own_cd, countpty_cd, temp_agmtno, temp_agmtrev, temp_contno, temp_contrev, temp_cont_type, temp_instrument_no);
						}
						
						plant_seq=rset1.getString(22)==null?"":rset1.getString(22);
						bu_plant_seq=rset1.getString(8)==null?"":rset1.getString(8);
						billing_cycle=rset1.getString(23)==null?"":rset1.getString(23);
						fin_year=rset1.getString(15)==null?"":rset1.getString(15);
						invoice_no=rset1.getString(11)==null?"":rset1.getString(11);
						invoice_seq=inv_seq;
						checked_flag=rset1.getString(12)==null?"":rset1.getString(12);
						authorized_flag=rset1.getString(25)==null?"":rset1.getString(25);
						approved_flag=rset1.getString(13)==null?"":rset1.getString(13);
						pdf_flg=rset1.getString(17)==null?"":rset1.getString(17);
						pdf_ori=rset1.getString(19)==null?"":rset1.getString(19);
						sap_approved_flag=rset1.getString(20)==null?"":rset1.getString(20);
						bu_state_tin=rset1.getString(18)==null?"":rset1.getString(18);
						
						invoice_dt=rset1.getString(24)==null?"":rset1.getString(24);
						invoice_type=rset1.getString(26)==null?"":rset1.getString(26);
						
						String contRefNo="";
						String queryString2="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_TYPE,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,A.CONT_NO,A.CONT_REV,A.CONT_NAME,A.CONT_REF_NO,"
								+ "B.INSTRUMENT_NO,B.INSTRUMENT_TYPE,B.BUY_SELL,B.STATUS,B.QTY,B.QTY_UNIT,B.RATE,B.RATE_UNIT,"
								+ "B.PRODUCT_NM,B.CURVE_NM,B.PROJ_METHOD,TO_CHAR(B.CONT_DD_MM_YR,'DD/MM/YYYY'),"
								+ "TO_CHAR(B.PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(B.PRICE_END_DT,'DD/MM/YYYY'),B.CONV_FACTOR,TO_CHAR(A.TRADE_DT,'DD/MM/YYYY') "
								+ "FROM FMS_DERV_CONT_MST A,FMS_DERV_INSTRUMENT_MST B "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
								+ "AND B.STATUS='Y' AND B.INSTRUMENT_NO=? "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
								+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO";
					  	stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, countpty_cd);
						stmt2.setString(3, temp_agmtno);
						stmt2.setString(4, temp_contno);
						stmt2.setString(5, temp_cont_type);
						stmt2.setString(6, temp_instrument_no);
						rset2=stmt2.executeQuery();
						while(rset2.next())
						{
							contRefNo=rset2.getString(10)==null?"":rset2.getString(10);
						}
						rset2.close();
						stmt2.close();
						
						if(contRef.equals(""))
						{
							contRef=contRefNo;
						}
						else
						{
							contRef=contRef+", "+contRefNo;
						}
					}
					rset1.close();
					stmt1.close();
					
					VCOUNTERPARTY_CD.add(countpty_cd);
					VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
					VAGMT_NO.add(agmtno);
					VAGMT_REV.add(agmtrev);
					VCONT_NO.add(contno);
					VCONT_REV.add(contrev);
					VCONT_TYPE.add(cont_type);
					VINSTRUMENT_NO.add(instrument_no);
					VDEAL_MAPPING.add(deal_no);
					VPLANT_SEQ.add(plant_seq);
					VPLANT_ABBR.add(""+utilBean.getCounterpartyPlantABBR(conn,countpty_cd, comp_cd, plant_seq, "T"));
					VBU_PLANT_SEQ.add(bu_plant_seq);
					VBU_PLANT_ABBR.add(""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B"));
					VBILLING_FREQ.add(billing_cycle);
					VFIN_YEAR.add(fin_year);
					VINVOICE_NO.add(invoice_no);
					VINVOICE_SEQ.add(invoice_seq);
					VINV_CHECKED_FLG.add(checked_flag);
					VINV_AUTHORIZED_FLG.add(authorized_flag);
					VINV_APPROVED_FLG.add(approved_flag);
					VINV_PDF_FLG.add(pdf_flg);
					VINV_PDF_TYPE.add(pdf_flg);
					VINV_SAP_APPROVAL_FLAG.add(sap_approved_flag);
					VBU_STATE_TIN.add(bu_state_tin);
					VINVOICE_EXIST.add("Y");
					VIS_IRN_GENERATED.add("Y");
					VPERIOD_START_DT.add(period_start_dt);
					VPERIOD_END_DT.add(period_end_dt);
					VINVOICE_DT.add(invoice_dt);
					VINVOICE_TYPE.add(invoice_type);
					VCONT_REF.add(contRef);
					
					int upload_count=0;
					queryString6="SELECT COUNT(*) "
							+ "FROM FMS_DERV_INV_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
		 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
		 	        		+ "AND PDF_TYPE=? AND INV_TYPE=?";
					stmt6=conn.prepareStatement(queryString6);
					stmt6.setString(1, comp_cd);
					stmt6.setString(2, bu_state_tin);
					stmt6.setString(3, inv_seq);
					stmt6.setString(4, fin_year);
					stmt6.setString(5, "P");
					stmt6.setString(6, ""+VMST_INV_TYPE_FLG.elementAt(i));
					rset6=stmt6.executeQuery();
					if(rset6.next())
					{
						upload_count=rset6.getInt(1);
					}
					rset6.close();
					stmt6.close();
					VFILE_UPLOAD_COUNT.add(upload_count);
					
					queryString6="SELECT FILE_NAME,PDF_SIGNED "
							+ "FROM FMS_DERV_INV_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
		 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
		 	        		+ "AND PDF_TYPE=? AND INV_TYPE=?";
					stmt6=conn.prepareStatement(queryString6);
					stmt6.setString(1, comp_cd);
					stmt6.setString(2, bu_state_tin);
					stmt6.setString(3, inv_seq);
					//stmt6.setString(4, financial_year);
					stmt6.setString(4, fin_year);
					stmt6.setString(5, print_pdf_type);
					stmt6.setString(6, ""+VMST_INV_TYPE_FLG.elementAt(i));
					rset6=stmt6.executeQuery();
					if(rset6.next())
					{
						String pdf_signed=rset6.getString(2)==null?"":rset6.getString(2);
						
						VPDF_FILE_NAME.add(rset6.getString(1)==null?"":rset6.getString(1));
						
						if(VMST_INV_TYPE_FLG.elementAt(i).equals("R"))
						{
							VPDF_FILE_PATH.add(CommonVariable.derv_remittance_path);
						}
						else
						{
							if(pdf_signed.equals("Y"))
							{
								VPDF_FILE_PATH.add(CommonVariable.signed_derv_inv_path);
							}
							else
							{
								VPDF_FILE_PATH.add(CommonVariable.derv_inv_path);
							}
						}
					}
					else
					{
						VPDF_FILE_NAME.add("");
						VPDF_FILE_PATH.add("");
					}
					rset6.close();
					stmt6.close();
				
					queryString6="SELECT FILE_NAME,PDF_SIGNED,EMAIL_SENT "
							+ "FROM FMS_DERV_INV_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
		 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
		 	        		+ "AND PDF_TYPE=? AND INV_TYPE=? ";
					stmt6=conn.prepareStatement(queryString6);
					stmt6.setString(1, own_cd);
					stmt6.setString(2, bu_state_tin);
					stmt6.setString(3, inv_seq);
					//stmt6.setString(4, financial_year);
					stmt6.setString(4, fin_year);
					stmt6.setString(5, "O");
					stmt6.setString(6, ""+VMST_INV_TYPE_FLG.elementAt(i));
					rset6=stmt6.executeQuery();
					if(rset6.next())
					{
						String pdf_signed=rset6.getString(2)==null?"":rset6.getString(2);
						String email_sent=rset6.getString(3)==null?"":rset6.getString(3);
						VPDF_SIGNED_FLAG.add(pdf_signed);
						if(pdf_signed.equals("Y"))
						{
							VSIGN_PDF_TYPE.add("O");
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
					
					int re_print_count=isRePrintPDFRequested(comp_cd,bu_state_tin,inv_seq,fin_year,"DERV","REPRINT_PDF","A",invoice_type);
					VRE_PRINT_PDF.add(re_print_count>0?"Y":"N");
				}
				rset.close();
				stmt.close();
				
				VINDEX.add(index);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getDervInvDetailForPreparationList(String own_cd,String countpty_cd, String contno, String agmtno,String cont_type, String plant_seq, 
			String bu_plant_seq, String billing_cycle, String period_start_dt, String period_end_dt,String state_code,String instrument_no, String invoice_type)
	{
		String function_nm="getDervInvDetailForPreparationList()";
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
			
			queryString5="SELECT INVOICE_NO,CHECKED_FLAG,APPROVED_FLAG,INVOICE_SEQ,PDF_INV_DTL,"
					+ "PRINT_BY_ORI,PRINT_BY_TRI,PRINT_BY_DUP,SAP_APPROVAL,FINANCIAL_YEAR "
					+ "FROM FMS_DERV_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND AGMT_NO=? AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_UNIT=? "
					+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_STATE_TIN=? AND INSTRUMENT_NO=? AND INV_TYPE=? AND INV_FLAG='F' ";
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
			stmt5.setString(12, instrument_no);
			stmt5.setString(13, invoice_type);
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
				
				VINVOICE_EXIST.add("Y");
			}
			else
			{
				VINVOICE_EXIST.add("N");
			}
			rset5.close();
			stmt5.close();
			
			VINVOICE_NO.add(inv_no);
			VINVOICE_SEQ.add(inv_seq);
			VINV_CHECKED_FLG.add(checked_flag);
			VINV_APPROVED_FLG.add(approved_flag);
			VPDF_INV_FLG.add(pdf_flg);
			VSAP_APPROVAL_FLG.add(sap_approved_flag);
			VFIN_YEAR.add(fiscal_yr);
			
			if(print_pdf_type.equals("O") && !pdf_ori.equals(""))
			{
				VPDF_TYPE.add(print_pdf_type);
			}
			else
			{
				VPDF_TYPE.add("");
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getDervDealInvDtl()
	{
		String function_nm="getDervDealInvDtl()";
		
		try
		{
			int count=0;
			String[] all_agmt_no = null;
			String[] all_agmt_rev = null;
			String[] all_cont_type = null;
			String[] all_cont_no = null;
			String[] all_cont_rev = null;
			String[] all_instrument_no = null;
			String[] all_buy_sell = null;
			String[] all_period_end_dt = null;
			//System.out.println("period_end_dt...."+period_end_dt);
			if(cont_no.contains("@@"))
			{
				all_agmt_no = agmt_no.split("@@");
				all_agmt_rev = agmt_rev.split("@@");
				all_cont_type = cont_type.split("@@");
				all_cont_no = cont_no.split("@@");
				all_cont_rev = cont_rev.split("@@");
				all_instrument_no = instrument_no.split("@@");
				all_buy_sell = buy_sell.split("@@");
				all_period_end_dt = period_end_dt.split("@@");
			}
			else
			{
				all_agmt_no = agmt_no.split(", ");
				all_agmt_rev = agmt_rev.split(", ");
				all_cont_type = cont_type.split(", ");
				all_cont_no = cont_no.split(", ");
				all_cont_rev = cont_rev.split(", ");
				all_instrument_no = instrument_no.split(", ");
				all_buy_sell = buy_sell.split(", ");
				all_period_end_dt = period_end_dt.split(", ");
			}
			
			for(int i=0;i<all_cont_no.length;i++)
			{
				if(!period_end_dt.equals(""))
				{
					fy_year=dateUtil.getFinancialYear(all_period_end_dt[i]);
				}
				int cnt=0;
				queryString="SELECT COUNT(*) "
						+ "FROM FMS_DERV_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND INSTRUMENT_NO=? "
						+ "AND FINANCIAL_YEAR=? AND BU_UNIT=? AND PLANT_SEQ=? AND INV_FLAG='F' ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(++cnt, comp_cd);
				stmt.setString(++cnt, counterparty_cd);
				stmt.setString(++cnt, all_agmt_no[i]);
				stmt.setString(++cnt, all_cont_no[i]);
				stmt.setString(++cnt, all_cont_type[i]);
				stmt.setString(++cnt, all_instrument_no[i]);
				stmt.setString(++cnt, fy_year);
				stmt.setString(++cnt, bu_plant_seq);
				stmt.setString(++cnt, plant_seq);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					count+=rset.getInt(1);
				}
				rset.close();
				stmt.close();
			}
			if(count>0)
			{
				for(int i=0;i<all_cont_no.length;i++)
				{
					fy_year=dateUtil.getFinancialYear(all_period_end_dt[i]);
					int cnt1=0;
					String queryString1="SELECT INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
							+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,BUY_PRICE,BUY_PRICE_UNIT,BUY_AMT,"
							+ "INVOICE_RAISED_IN,INVOICE_AMT,NET_PAYABLE_AMT,"
							+ "REMARK_1,REMARK_2,FINANCIAL_YEAR,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),INVOICE_REF_NO,CHECKED_FLAG "
							+ "FROM FMS_DERV_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
							+ "AND BU_STATE_TIN=? "
							//+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND FINANCIAL_YEAR=? AND INSTRUMENT_NO=? AND INV_FLAG='F' ";
							//+ "AND INVOICE_SEQ=? ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(++cnt1, comp_cd);
					stmt1.setString(++cnt1, counterparty_cd);
					stmt1.setString(++cnt1, all_cont_no[i]);
					stmt1.setString(++cnt1, all_agmt_no[i]);
					stmt1.setString(++cnt1, plant_seq);
					stmt1.setString(++cnt1, bu_plant_seq);
					stmt1.setString(++cnt1, all_cont_type[i]);
					stmt1.setString(++cnt1, bu_state_tin);
					//stmt1.setString(++cnt1, period_start_dt);
					//stmt1.setString(++cnt1, period_end_dt);
					stmt1.setString(++cnt1, fy_year);
					stmt1.setString(++cnt1, all_instrument_no[i]);
					//stmt1.setString(++cnt1, invoice_seq);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						invoice_seq=rset1.getString(1)==null?"":rset1.getString(1);
						String inv_no=rset1.getString(2)==null?"":rset1.getString(2);
						inv_dt=rset1.getString(3)==null?"":rset1.getString(3);
						inv_due_dt=rset1.getString(4)==null?"":rset1.getString(4);
						double alloc_qty=rset1.getDouble(5);
						String sell_price=rset1.getString(6)==null?"":rset1.getString(6);
						String sell_price_unit=rset1.getString(7)==null?"":rset1.getString(7);
						String sell_amt=rset1.getString(8)==null?"":rset1.getString(8);
						String buy_price=rset1.getString(9)==null?"":rset1.getString(9);
						String buy_price_unit=rset1.getString(10)==null?"":rset1.getString(10);
						String buy_amt=rset1.getString(11)==null?"":rset1.getString(11);
						String inv_raised_in=rset1.getString(12)==null?"":rset1.getString(12);
						String invoice_amt=rset1.getString(13)==null?"":rset1.getString(13);
						String net_payable_amt=rset1.getString(14)==null?"":rset1.getString(14);
						remark1=rset1.getString(15)==null?"":rset1.getString(15);
						remark2=rset1.getString(16)==null?"":rset1.getString(16);
						String financial_year=rset1.getString(17)==null?"":rset1.getString(17);
						period_start_dt=rset1.getString(18)==null?"":rset1.getString(18);
						period_end_dt=rset1.getString(19)==null?"":rset1.getString(19);
						inv_ref=rset1.getString(20)==null?"":rset1.getString(20);
						chk_flg=rset1.getString(21)==null?"":rset1.getString(21);
						
						VPERIOD_START_DT.add(period_start_dt);
						VPERIOD_END_DT.add(period_end_dt);
						VCOUNTERPARTY_CD.add(counterparty_cd);
						VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn, counterparty_cd));
						VAGMT_TYPE.add(agmt_type);
						VAGMT_NO.add(all_agmt_no[i]);
						VAGMT_REV.add(all_agmt_rev[i]);
						VCONT_TYPE.add(all_cont_type[i]);
						VCONT_NO.add(all_cont_no[i]);
						VCONT_REV.add(all_cont_rev[i]);
						VDEAL_MAPPING.add(""+utilBean.NewDealMappingId(comp_cd, counterparty_cd, all_agmt_no[i], all_agmt_rev[i], all_cont_no[i], all_cont_rev[i], all_cont_type[i], all_instrument_no[i]));
						
						VINSTRUMENT_NO.add(all_instrument_no[i]);
						
						VBOOKED_MMBTU.add(nf.format(alloc_qty));
					  	VCONT_START_DT.add(period_start_dt);
					  	VCONT_END_DT.add(period_end_dt);
					  	VDEAL_PRICE_CURVE.add(financial_curve);
					  	
					  	int cnt2=0;
					  	String cont_ref="";
					  	String trade_dt="";
					  	String instrument_volume_unit="";
					  	String buySell="";
					  	String instrument_type="";
					  	String curve_nm="";
					  	String instru_duration="";
					  	queryString2="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_TYPE,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,A.CONT_NO,A.CONT_REV,A.CONT_NAME,A.CONT_REF_NO,"
								+ "B.INSTRUMENT_NO,B.INSTRUMENT_TYPE,B.BUY_SELL,B.STATUS,B.QTY,B.QTY_UNIT,B.RATE,B.RATE_UNIT,"
								+ "B.PRODUCT_NM,B.CURVE_NM,B.PROJ_METHOD,TO_CHAR(B.CONT_DD_MM_YR,'DD/MM/YYYY'),"
								+ "TO_CHAR(B.PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(B.PRICE_END_DT,'DD/MM/YYYY'),B.CONV_FACTOR,TO_CHAR(A.TRADE_DT,'DD/MM/YYYY') "
								+ "FROM FMS_DERV_CONT_MST A,FMS_DERV_INSTRUMENT_MST B "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
								+ "AND B.STATUS='Y' AND B.INSTRUMENT_NO=? "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
								+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO";
					  	stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(++cnt2, comp_cd);
						stmt2.setString(++cnt2, counterparty_cd);
						stmt2.setString(++cnt2, all_agmt_no[i]);
						stmt2.setString(++cnt2, all_cont_no[i]);
						stmt2.setString(++cnt2, all_cont_type[i]);
						//stmt2.setString(++cnt2, buy_sell);
						stmt2.setString(++cnt2, all_instrument_no[i]);
						rset2=stmt2.executeQuery();
						while(rset2.next())
						{
							cont_ref=rset2.getString(10)==null?"":rset2.getString(10);
							trade_dt=rset2.getString(26)==null?"":rset2.getString(26);
							String instrument_volume_unit_cd = rset2.getString(16)==null?"":rset2.getString(16);
							instrument_volume_unit = utilBean.getEnergyUnitNm(conn, instrument_volume_unit_cd);
							buySell = rset2.getString(13)==null?"":rset2.getString(13);
							instrument_type = rset2.getString(12)==null?"":rset2.getString(12);
							curve_nm = rset2.getString(20)==null?"":rset2.getString(20);
							String start_dt = rset2.getString(23)==null?"":rset2.getString(23);
							String end_dt = rset2.getString(24)==null?"":rset2.getString(24);
							instru_duration = start_dt+"-"+end_dt;
						}
						rset2.close();
						stmt2.close();
						VCONT_REF.add(cont_ref);
						VTRADE_DT.add(trade_dt);
						VQTY_UNIT.add(instrument_volume_unit);
						VBUY_SELL.add(buySell);
						
						if(buySell.equals("BUY"))
						{
							BigDecimal float_val=new BigDecimal(sell_price);
							VSELL_RATE.add(df3.format(float_val));
						}
						else
						{
							VSELL_RATE.add(nf2.format(Double.parseDouble(sell_price)));
						}
					  	VSELL_AMT.add(sell_amt);
					  	if(buySell.equals("SELL"))
						{
					  		BigDecimal float_val=new BigDecimal(buy_price);
					  		VBUY_RATE.add(df3.format(float_val));
						}
					  	else
					  	{
					  		VBUY_RATE.add(nf2.format(Double.parseDouble(buy_price)));
					  	}
					  	
					  	VBUY_AMT.add(buy_amt);
					  	VINSTRUMENT_TYPE.add(instrument_type);
					  	VCURVE_NM.add(curve_nm);
					  	VINSTRUMENT_DURATION.add(instru_duration);
					}
					rset1.close();
					stmt1.close();
				}
				
				bank_formula=utilBean.getEntityBankFormula(conn, comp_cd, counterparty_cd, "T", "DERV", inv_dt);
				/*String queryString="SELECT COUNTERPARTY_CD,ENTITY,TO_CHAR(EFF_DT,'DD/MM/YYYY'),BANK_NAME,ACCOUNT_ID,IFSC_CODE,BRANCH_NAME,"
						+ "STATE_NM,CATEGORY  "
						+ "FROM FMS_ENTITY_BANK_MST A "
						+ "WHERE ENTITY=? AND COUNTERPARTY_CD=? AND COMPANY_CD=? AND CATEGORY=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_BANK_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.CATEGORY=B.CATEGORY)";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, "B");
				stmt.setString(2, comp_cd);
				stmt.setString(3, comp_cd);
				stmt.setString(4, "DERV");
				rset=stmt.executeQuery();
				if(rset.next())
				{
					String bank_eff_dt = rset.getString(3)==null?"":rset.getString(3);
					String bank_name=rset.getString(4)==null?"":rset.getString(4);
					String bank_account_no=rset.getString(5)==null?"":rset.getString(5);
					String ifsc_code=rset.getString(6)==null?"":rset.getString(6);
					String bank_branch=rset.getString(7)==null?"":rset.getString(7);
					String bank_state=rset.getString(8)==null?"":rset.getString(8);
					String bank_category=rset.getString(9)==null?"":rset.getString(9);
					
					bank_formula=bank_name+", "+bank_branch+", "+bank_state+", A/C No : "+bank_account_no+", IFSC Code : "+ifsc_code;
				}
				rset.close();
				stmt.close();*/
			}
			else
			{
				Vector VDUE_DT = new Vector();
				for(int i=0;i<all_cont_no.length;i++)
				{
					int cnt1=0;
					String queryString1="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_TYPE,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,A.CONT_NO,A.CONT_REV,A.CONT_NAME,A.CONT_REF_NO,"
							+ "B.INSTRUMENT_NO,B.INSTRUMENT_TYPE,B.BUY_SELL,B.STATUS,B.QTY,B.QTY_UNIT,B.RATE,B.RATE_UNIT,"
							+ "B.PRODUCT_NM,B.CURVE_NM,B.PROJ_METHOD,TO_CHAR(B.CONT_DD_MM_YR,'DD/MM/YYYY'),"
							+ "TO_CHAR(B.PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(B.PRICE_END_DT,'DD/MM/YYYY'),B.CONV_FACTOR,TO_CHAR(A.TRADE_DT,'DD/MM/YYYY') "
							+ "FROM FMS_DERV_CONT_MST A,FMS_DERV_INSTRUMENT_MST B "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND B.BUY_SELL=? AND B.CURVE_NM=? "
							+ "AND B.PRICE_END_DT<=TO_DATE(?,'DD/MM/YYYY') AND B.STATUS='Y' AND B.AGMT_NO=? AND B.CONT_NO=? AND B.CONTRACT_TYPE=? AND B.INSTRUMENT_NO=? "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(++cnt1, comp_cd);
					stmt1.setString(++cnt1, counterparty_cd);
					stmt1.setString(++cnt1, all_buy_sell[i]);
					stmt1.setString(++cnt1, financial_curve);
					stmt1.setString(++cnt1, report_dt);
					stmt1.setString(++cnt1, all_agmt_no[i]);
					stmt1.setString(++cnt1, all_cont_no[i]);
					stmt1.setString(++cnt1, all_cont_type[i]);
					stmt1.setString(++cnt1, all_instrument_no[i]);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						String countpty_cd=rset1.getString(2)==null?"":rset1.getString(2);
						String agmt_type=rset1.getString(3)==null?"":rset1.getString(3);
						String agmt_no=rset1.getString(4)==null?"":rset1.getString(4);
						String agmt_rev=rset1.getString(5)==null?"":rset1.getString(5);
						String contract_type=rset1.getString(6)==null?"":rset1.getString(6);
						String cont_no=rset1.getString(7)==null?"":rset1.getString(7);
						String cont_rev=rset1.getString(8)==null?"":rset1.getString(8);
						String cont_name=rset1.getString(9)==null?"":rset1.getString(9);
						String cont_ref=rset1.getString(10)==null?"":rset1.getString(10);
						String trade_dt=rset1.getString(26)==null?"":rset1.getString(26);	
						
						String instrument_no = rset1.getString(11)==null?"":rset1.getString(11);
						String instrument_type = rset1.getString(12)==null?"":rset1.getString(12);
						String buy_sell = rset1.getString(13)==null?"":rset1.getString(13);
						String instrument_status = rset1.getString(14)==null?"":rset1.getString(14);
						double instrument_volume = rset1.getDouble(15);
						String instrument_volume_unit_cd = rset1.getString(16)==null?"":rset1.getString(16);
						String instrument_volume_unit = utilBean.getEnergyUnitNm(conn, instrument_volume_unit_cd);
						
						String instrument_rate_unit = rset1.getString(18)==null?"":rset1.getString(18);
						
						double temp_instrument_rate = rset1.getDouble(17);
						String instrument_rate=""+utilBean.RateNumberFormat(temp_instrument_rate, instrument_rate_unit);
						
						String product_nm = rset1.getString(19)==null?"":rset1.getString(19);
						String curve_nm = rset1.getString(20)==null?"":rset1.getString(20);
						String proj_method = rset1.getString(21)==null?"":rset1.getString(21);
						String cont_dd_mm_yr = rset1.getString(22)==null?"":rset1.getString(22);
						
						String price_start_dt = rset1.getString(23)==null?"":rset1.getString(23);
						String price_end_dt = rset1.getString(24)==null?"":rset1.getString(24);
						
						VCOUNTERPARTY_CD.add(countpty_cd);
						VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn, countpty_cd));
						VAGMT_TYPE.add(agmt_type);
						VAGMT_NO.add(agmt_no);
						VAGMT_REV.add(agmt_rev);
						VCONT_TYPE.add(contract_type);
						VCONT_NO.add(cont_no);
						VCONT_REV.add(cont_rev);
						VCONT_NAME.add(cont_name);
						VCONT_REF.add(cont_ref);
						VDEAL_MAPPING.add(""+utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt_no, agmt_rev, cont_no, cont_rev, contract_type, instrument_no));
						
						VINSTRUMENT_NO.add(instrument_no);
						VINSTRUMENT_TYPE.add(instrument_type);
						VBUY_SELL.add(buy_sell);
						VPERIOD_START_DT.add(price_start_dt);
						VPERIOD_END_DT.add(price_end_dt);
						
						VINSTRUMENT_DURATION.add(price_start_dt+"-"+price_end_dt);
						VBOOKED_MMBTU.add(nf.format(instrument_volume));
						VQTY_UNIT.add(instrument_volume_unit);
					  	VTRADE_DT.add(trade_dt);
					  	VRATE.add(instrument_rate);
					  	VCONT_START_DT.add(price_start_dt);
					  	VCONT_END_DT.add(price_end_dt);
					  	VDEAL_PRICE_CURVE.add(curve_nm);
					  	VCURVE_NM.add(curve_nm);
					  	VDEAL_PROD_NM.add(product_nm);
					  	
					  	VPLANT_SEQ.add(plant_seq);
						VPLANT_ABBR.add(plant_abbr);
						VBU_PLANT_SEQ.add(bu_plant_seq);
						VBU_PLANT_ABBR.add(bu_plant_abbr);
						
						double CountDays=0; String DayDiff="";
						queryString2 = "SELECT TO_DATE(?,'DD/MM/YYYY')-TO_DATE(?,'DD/MM/YYYY') FROM DUAL";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, report_dt);
						stmt2.setString(2, price_end_dt);
						rset2=stmt2.executeQuery();
						if(rset2.next())
						{
							DayDiff = rset2.getString(1)==null?"":rset2.getString(1);
							CountDays=rset2.getDouble(1);
						}
						rset2.close();
						stmt2.close();
						
						String float_rate="";
						if(CountDays>=0)
						{
							queryString2 = "SELECT AVG(SETTLE_PRICE) FROM FMS_SPOT_PRICE_DTL "
									+ "WHERE SETTLE_PRICE>0 AND CURVE_DT BETWEEN TO_DATE(?,'dd/mm/yyyy') AND TO_DATE(?,'dd/mm/yyyy') "
									+ "AND CURVE_TYPE='Spot' AND CURVE_NM=? AND ACTUAL_CURVE=?";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, price_start_dt);
							stmt2.setString(2, price_end_dt);
							stmt2.setString(3, product_nm);
							stmt2.setString(4, curve_nm);
							rset2=stmt2.executeQuery();
							if(rset2.next())
							{
								BigDecimal float_val=new BigDecimal(rset2.getString(1)==null?"0":rset2.getString(1));
								float_rate=df3.format(float_val);
							}
							else
							{
								float_rate="0";
							}	
							rset2.close();
							stmt2.close();
						}
						else
						{
							float_rate="";
						}
						
						if(buy_sell.equals("BUY"))
						{
							VSELL_RATE.add(float_rate);
						  	VSELL_AMT.add("");
						  	VBUY_RATE.add(nf2.format(temp_instrument_rate));
						  	VBUY_AMT.add("");
						}
						else if(buy_sell.equals("SELL"))
						{
							VSELL_RATE.add(nf2.format(temp_instrument_rate));
						  	VSELL_AMT.add("");
						  	VBUY_RATE.add(float_rate);
						  	VBUY_AMT.add("");
						}
						
						String due_days="";
						String consider_due_dt_in="";
						String exclude_sat="";
						String holiday_state="";
						String invoice_due_dt="";
						queryString3="SELECT DUE_DATE,DUE_DT_IN,"
								+ "EXCL_SAT_MAP,HOLIDAY_STATE "
								+ "FROM FMS_DERV_CONT_BILLING_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
								+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev_no+"' "
								+ "AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? ";
						stmt3=conn.prepareStatement(queryString3);
						stmt3.setString(1, comp_cd);
						stmt3.setString(2, counterparty_cd);
						stmt3.setString(3, all_agmt_no[i]);
						stmt3.setString(4, all_cont_no[i]);
						stmt3.setString(5, all_cont_type[i]);
						stmt3.setString(6, plant_seq);
						rset3=stmt3.executeQuery();
						if(rset3.next())
						{
							due_days = rset3.getString(1)==null?"0":rset3.getString(1);
							consider_due_dt_in=rset3.getString(2)==null?"C":rset3.getString(2);
							exclude_sat=rset3.getString(3)==null?"":rset3.getString(3);
							holiday_state=rset3.getString(4)==null?"":rset3.getString(4);
						}
						else
						{	
							consider_due_dt_in="C";
							exclude_sat="";
							holiday_state="";
						}
						rset3.close();
						stmt3.close();
						
						String systemDt=dateUtil.getSysdate();
						invoice_due_dt=""+utilBean.DueDateCalculation(conn,systemDt, due_days,consider_due_dt_in,exclude_sat,comp_cd,holiday_state);
						VDUE_DT.add(invoice_due_dt);
					}
					rset1.close();
					stmt1.close();
				}
				
				LocalDate maxDate = null;
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		        for (int i = 0; i < VCONT_END_DT.size(); i++) 
		        {
		            String dateStr = "" + VCONT_END_DT.get(i);
		            LocalDate date = LocalDate.parse(dateStr, formatter);
		            if (maxDate == null || date.isAfter(maxDate)) {
		                maxDate = date;
		                inv_dt = date.format(formatter);
		            }
		        }
		        
		        for (int i = 0; i < VDUE_DT.size(); i++) 
		        {
		        	String dateStr = "" + VDUE_DT.get(i);
		        	LocalDate date = LocalDate.parse(dateStr, formatter);
		        	if (maxDate == null || date.isAfter(maxDate)) {
		        		maxDate = date;
		        		inv_due_dt = date.format(formatter);
		        	}
		        }
		        
		        bank_formula=utilBean.getEntityBankFormula(conn, comp_cd, counterparty_cd, "T", "DERV", inv_dt);
				/*String queryString="SELECT COUNTERPARTY_CD,ENTITY,TO_CHAR(EFF_DT,'DD/MM/YYYY'),BANK_NAME,ACCOUNT_ID,IFSC_CODE,BRANCH_NAME,"
						+ "STATE_NM,CATEGORY  "
						+ "FROM FMS_ENTITY_BANK_MST A "
						+ "WHERE ENTITY=? AND COUNTERPARTY_CD=? AND COMPANY_CD=? AND CATEGORY=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_BANK_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.CATEGORY=B.CATEGORY)";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, "B");
				stmt.setString(2, comp_cd);
				stmt.setString(3, comp_cd);
				stmt.setString(4, "DERV");
				rset=stmt.executeQuery();
				if(rset.next())
				{
					String bank_eff_dt = rset.getString(3)==null?"":rset.getString(3);
					String bank_name=rset.getString(4)==null?"":rset.getString(4);
					String bank_account_no=rset.getString(5)==null?"":rset.getString(5);
					String ifsc_code=rset.getString(6)==null?"":rset.getString(6);
					String bank_branch=rset.getString(7)==null?"":rset.getString(7);
					String bank_state=rset.getString(8)==null?"":rset.getString(8);
					String bank_category=rset.getString(9)==null?"":rset.getString(9);
					
					bank_formula=bank_name+", "+bank_branch+", "+bank_state+", A/C No : "+bank_account_no+", IFSC Code : "+ifsc_code;
				}
				rset.close();
				stmt.close();*/
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCurveMapDtl()
	{
		String function_nm="getCurveMapDtl()";
		
		try
		{
			queryString="SELECT PROD_TYPE,CURVE_TYPE "
					+ "FROM FMS_PROD_CURVE_MAP";
			stmt = conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VINDEX_NM.add(rset.getString(1)==null?"":rset.getString(1));
				VCURVE_TYPE.add(rset.getString(2)==null?"":rset.getString(2));
			}
			rset.close();
			stmt.close();
			
			int count=0;
			for(int i=0; i<VINDEX_NM.size(); i++)
			{
				count=0;
				queryString2="SELECT COUNT(PHYS_FIN) FROM FMS_SPOT_PRICE_DTL WHERE CURVE_NM=? AND PHYS_FIN ='Financial' ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, ""+VINDEX_NM.elementAt(i));
				rset2 = stmt2.executeQuery();
				if(rset2.next())
				{
					count=rset2.getInt(1);
				}
				rset2.close();
				stmt2.close();
				if(count>0)
				{
					queryString3="UPDATE FMS_SPOT_PRICE_DTL SET ACTUAL_CURVE=? WHERE CURVE_NM=? AND PHYS_FIN ='Financial' ";
					stmt3 = conn.prepareStatement(queryString3);
					stmt3.setString(1, ""+VCURVE_TYPE.elementAt(i));
					stmt3.setString(2, ""+VINDEX_NM.elementAt(i));
					stmt3.executeUpdate();
					stmt3.close();
					conn.commit();
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getDervDealDtl()
	{
		String function_nm="getDervDealDtl()";
		
		try
		{
			int yearCnt=0;
			
			String tempGen_Dt[]=report_dt.split("/");
			String gen_month=tempGen_Dt[1]; 
			String new_fy_year=tempGen_Dt[2]; 
		
			queryString = "SELECT MIN(TO_CHAR(TRADE_DT,'YYYY')) "
					+" FROM FMS_DERV_CONT_MST "
					+ "WHERE COMPANY_CD=? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				min_fy_year=rset.getString(1)==null?"":rset.getString(1);
			}
			rset.close();
			stmt.close();
			
			if(Integer.parseInt(gen_month)>3)
			{
				max_fy_year=""+(Integer.parseInt(new_fy_year)+1);
			}
			else
			{
				max_fy_year=new_fy_year;
			}
			
			if(min_fy_year.equals(""))
			{
				min_fy_year=""+(Integer.parseInt(new_fy_year)-1);
			}
			
			if(!fy_year.equals("0"))
			{
				yearCnt=Integer.parseInt(fy_year)-1;
				String startMthYrPrev="01/01/"+yearCnt; 
				String endMthYrPrev="31/12/"+yearCnt; 
				String queryString1="SELECT TO_CHAR(A.TRADE_DT,'YYYY') "
						+ "FROM FMS_DERV_CONT_MST A,FMS_DERV_INSTRUMENT_MST B "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND B.BUY_SELL=? AND A.TRADE_DT BETWEEN TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(?,'DD/MM/YYYY') AND B.STATUS='Y' "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, buy_sell);
				stmt1.setString(4, startMthYrPrev);
				stmt1.setString(5, endMthYrPrev);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					prev_year_trade_cnt=rset1.getString(1)==null?"0":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
			}
			yearCnt=Integer.parseInt(fy_year)+1;
			String startMthYr="01/04/"+fy_year; String endMthYr="31/03/"+yearCnt; 
			if(fy_year.equals("0"))
			{
				startMthYr="01/04/"+min_fy_year;
				endMthYr="31/03/2030"; 
			}
			
			int cnt=0;
			String queryString1="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_TYPE,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,A.CONT_NO,A.CONT_REV,A.CONT_NAME,A.CONT_REF_NO,"
					+ "B.INSTRUMENT_NO,B.INSTRUMENT_TYPE,B.BUY_SELL,B.STATUS,B.QTY,B.QTY_UNIT,B.RATE,B.RATE_UNIT,"
					+ "B.PRODUCT_NM,B.CURVE_NM,B.PROJ_METHOD,TO_CHAR(B.CONT_DD_MM_YR,'DD/MM/YYYY'),"
					+ "TO_CHAR(B.PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(B.PRICE_END_DT,'DD/MM/YYYY'),B.CONV_FACTOR,TO_CHAR(A.TRADE_DT,'DD/MM/YYYY'),C.PLANT_SEQ_NO,D.PLANT_SEQ_NO "
					+ "FROM FMS_DERV_CONT_MST A,FMS_DERV_INSTRUMENT_MST B,FMS_DERV_CONT_PLANT C,FMS_DERV_CONT_BU D "
					+ "WHERE A.COMPANY_CD=? ";
				if(!counterparty_cd.equals("0"))
				{
					queryString1+= "AND A.COUNTERPARTY_CD=? ";
				}
				if(!buy_sell.equals("0"))
				{
					queryString1+= "AND B.BUY_SELL=? ";
				}
				if(!financial_curve.equals("0"))
				{
					queryString1+= "AND B.CURVE_NM=? ";
				}
				if(!plant_seq.equals("0"))
				{
					queryString1+="AND C.PLANT_SEQ_NO=? ";
				}
				if(!bu_plant_seq.equals("0"))
				{
					queryString1+="AND D.PLANT_SEQ_NO=?";
				}
				queryString1+= "AND B.PRICE_END_DT<=TO_DATE(?,'DD/MM/YYYY') AND B.STATUS='Y' AND A.CONT_STATUS!='X' "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO "
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.AGMT_TYPE=C.AGMT_TYPE AND A.AGMT_NO=C.AGMT_NO "
					+ "AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.CONT_NO=C.CONT_NO "
					+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.AGMT_TYPE=D.AGMT_TYPE AND A.AGMT_NO=D.AGMT_NO "
					+ "AND A.CONTRACT_TYPE=D.CONTRACT_TYPE AND A.CONT_NO=D.CONT_NO "
					+ "ORDER BY "
					+ "CASE "
					+ "WHEN EXISTS ("
					+ "SELECT 1 FROM FMS_DERV_INVOICE_MST E "
					+ "WHERE E.COMPANY_CD = A.COMPANY_CD "
					+ "AND E.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
					+ "AND E.CONT_NO = A.CONT_NO "
					+ "AND E.AGMT_NO = A.AGMT_NO "
					+ "AND E.CONTRACT_TYPE = A.CONTRACT_TYPE "
					+ "AND E.INSTRUMENT_NO = B.INSTRUMENT_NO "
					+ "AND E.PLANT_SEQ = C.PLANT_SEQ_NO "
					+ "AND E.BU_UNIT = D.PLANT_SEQ_NO "
					+ "AND E.INV_FLAG='F' "
					+ ") THEN 1 "
					+ "ELSE 0 "
					+ "END";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(++cnt, comp_cd);
			if(!counterparty_cd.equals("0"))
			{
				stmt1.setString(++cnt, counterparty_cd);
			}
			if(!buy_sell.equals("0"))
			{
				stmt1.setString(++cnt, buy_sell);
			}
			if(!financial_curve.equals("0"))
			{
				stmt1.setString(++cnt, financial_curve);
			}
			if(!plant_seq.equals("0"))
			{
				stmt1.setString(++cnt, plant_seq);
			}
			if(!bu_plant_seq.equals("0"))
			{
				stmt1.setString(++cnt, bu_plant_seq);
			}
			stmt1.setString(++cnt, report_dt);
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{		
				String countpty_cd=rset1.getString(2)==null?"":rset1.getString(2);
				String agmt_type=rset1.getString(3)==null?"":rset1.getString(3);
				String agmt_no=rset1.getString(4)==null?"":rset1.getString(4);
				String agmt_rev=rset1.getString(5)==null?"":rset1.getString(5);
				String contract_type=rset1.getString(6)==null?"":rset1.getString(6);
				String cont_no=rset1.getString(7)==null?"":rset1.getString(7);
				String cont_rev=rset1.getString(8)==null?"":rset1.getString(8);
				String cont_name=rset1.getString(9)==null?"":rset1.getString(9);
				String cont_ref=rset1.getString(10)==null?"":rset1.getString(10);
				String trade_dt=rset1.getString(26)==null?"":rset1.getString(26);	
				
				String instrument_no = rset1.getString(11)==null?"":rset1.getString(11);
				String instrument_type = rset1.getString(12)==null?"":rset1.getString(12);
				String buy_sell = rset1.getString(13)==null?"":rset1.getString(13);
				String instrument_status = rset1.getString(14)==null?"":rset1.getString(14);
				double instrument_volume = rset1.getDouble(15);
				String instrument_volume_unit_cd = rset1.getString(16)==null?"":rset1.getString(16);
				String instrument_volume_unit = utilBean.getEnergyUnitNm(conn, instrument_volume_unit_cd);
				
				String instrument_rate_unit = rset1.getString(18)==null?"":rset1.getString(18);
				
				double temp_instrument_rate = rset1.getDouble(17);
				String instrument_rate=""+utilBean.RateNumberFormat(temp_instrument_rate, instrument_rate_unit);
				
				String product_nm = rset1.getString(19)==null?"":rset1.getString(19);
				String curve_nm = rset1.getString(20)==null?"":rset1.getString(20);
				String proj_method = rset1.getString(21)==null?"":rset1.getString(21);
				String cont_dd_mm_yr = rset1.getString(22)==null?"":rset1.getString(22);
				
				String price_start_dt = rset1.getString(23)==null?"":rset1.getString(23);
				String price_end_dt = rset1.getString(24)==null?"":rset1.getString(24);
				
				String plant_seq = rset1.getString(27)==null?"":rset1.getString(27);
				String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, comp_cd, plant_seq, "T");
				
				String bu_plant_seq = rset1.getString(28)==null?"":rset1.getString(28);
				String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B");
				
				String state_code=utilBean.getState_TIN(conn, comp_cd, comp_cd, "B", bu_plant_seq);
				
				VCOUNTERPARTY_CD.add(countpty_cd);
				VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn, countpty_cd));
				VAGMT_TYPE.add(agmt_type);
				VAGMT_NO.add(agmt_no);
				VAGMT_REV.add(agmt_rev);
				VCONT_TYPE.add(contract_type);
				VCONT_NO.add(cont_no);
				VCONT_REV.add(cont_rev);
				VCONT_NAME.add(cont_name);
				VCONT_REF.add(cont_ref);
				VDEAL_MAPPING.add(""+utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt_no, agmt_rev, cont_no, cont_rev, contract_type, instrument_no));
				
				VINSTRUMENT_NO.add(instrument_no);
				VINSTRUMENT_TYPE.add(instrument_type);
				VBUY_SELL.add(buy_sell);
				
				VCARGO_ARRIVAL_DT.add(price_start_dt+"-"+price_end_dt);
				VBOOKED_MMBTU.add(nf.format(instrument_volume));
				VQTY_UNIT.add(instrument_volume_unit);
			  	VTRADE_DT.add(trade_dt);
			  	VRATE.add(nf2.format(temp_instrument_rate));
			  	VCONT_START_DT.add(price_start_dt);
			  	VCONT_END_DT.add(price_end_dt);
			  	VDEAL_PRICE_CURVE.add(curve_nm);
			  	VDEAL_PROD_NM.add(product_nm);
			  	
			  	VPLANT_SEQ.add(plant_seq);
				VPLANT_ABBR.add(plant_abbr);
				VBU_PLANT_SEQ.add(bu_plant_seq);
				VBU_PLANT_ABBR.add(bu_plant_abbr);
				VBU_STATE_TIN.add(state_code);
			  	
			  	int isInvExist=isInvoiceExist(comp_cd,countpty_cd, cont_no, agmt_no,contract_type, 
			  			plant_seq, bu_plant_seq, state_code, 
			  			trade_dt, trade_dt,instrument_no);
			  	if(isInvExist>0)
			  	{
			  		VINVGEN_FLAG.add("G");
			  	}
			  	else
			  	{
			  		VINVGEN_FLAG.add("P");
			  	}
				
				double CountDays=0; String DayDiff="";
				queryString2 = "SELECT TO_DATE(?,'DD/MM/YYYY')-TO_DATE(?,'DD/MM/YYYY') FROM DUAL";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, report_dt);
				stmt2.setString(2, price_end_dt);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					DayDiff = rset2.getString(1)==null?"":rset2.getString(1);
					CountDays=rset2.getDouble(1);
				}
				rset2.close();
				stmt2.close();
				
				if(CountDays>=0)
				{
					queryString2 = "SELECT AVG(SETTLE_PRICE) FROM FMS_SPOT_PRICE_DTL "
							+ "WHERE SETTLE_PRICE>0 AND CURVE_DT BETWEEN TO_DATE(?,'dd/mm/yyyy') AND TO_DATE(?,'dd/mm/yyyy') "
							+ "AND CURVE_TYPE='Spot' AND CURVE_NM=? AND ACTUAL_CURVE=?";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, price_start_dt);
					stmt2.setString(2, price_end_dt);
					stmt2.setString(3, product_nm);
					stmt2.setString(4, curve_nm);
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						BigDecimal float_val=new BigDecimal(rset2.getString(1)==null?"0":rset2.getString(1)) ;
						VFLOAT_RATE.add(df3.format(float_val));						
					}
					else
					{
						VFLOAT_RATE.add("0");
					}	
					rset2.close();
					stmt2.close();
				}
				else
				{
					VFLOAT_RATE.add("");
				}
				
				String invoice_no="";
				String invoice_dt="";
				int cnt1=0;
				String queryString4="SELECT INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY') "
						+ "FROM FMS_DERV_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? "
						+ "AND BU_UNIT=? AND PLANT_SEQ=? AND BU_STATE_TIN=? "
						+ "AND INSTRUMENT_NO=? AND INV_FLAG='F'";
				stmt4=conn.prepareStatement(queryString4);
				stmt4.setString(++cnt1, comp_cd);
				stmt4.setString(++cnt1, countpty_cd);
				stmt4.setString(++cnt1, cont_no);
				stmt4.setString(++cnt1, agmt_no);
				stmt4.setString(++cnt1, contract_type);
				stmt4.setString(++cnt1, bu_plant_seq);
				stmt4.setString(++cnt1, plant_seq);
				stmt4.setString(++cnt1, state_code);
				stmt4.setString(++cnt1, instrument_no);
				rset4=stmt4.executeQuery();
				if(rset4.next())
				{
					invoice_no=rset4.getString(1)==null?"":rset4.getString(1);
					invoice_dt=rset4.getString(2)==null?"":rset4.getString(2);
				}
				rset4.close();
				stmt4.close();
				VINVOICE_NO.add(invoice_no);
				VINVOICE_DT.add(invoice_dt);
			}	
			rset1.close();
			stmt1.close();
			
			for(int i=0; i<VCOUNTERPARTY_CD.size(); i++)
			{
				VPRICE_TYPE.add("FIXED/FLOAT SWAP");
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public int isInvoiceExist(String own_cd,String countpty_cd, String contno, String agmtno,String cont_type, String plant_seq, 
			String bu_plant_seq, String state_code, String period_start_dt, String period_end_dt,String instrument_no)
	{
		String function_nm="isInvoiceExist()";
		int isInvExist=0;
		try
		{
			queryString4="SELECT COUNT(*) "
					+ "FROM FMS_DERV_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
					+ "AND AGMT_NO=? AND CONTRACT_TYPE=? "
					+ "AND BU_UNIT=? AND PLANT_SEQ=? AND BU_STATE_TIN=? "
					+ "AND INSTRUMENT_NO=? AND INV_FLAG='F'";
			//queryString4+="AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') ";
			//queryString4+= "AND PDF_INV_DTL IS NOT NULL";
			int cnt=0;
			stmt4=conn.prepareStatement(queryString4);
			stmt4.setString(++cnt, own_cd);
			stmt4.setString(++cnt, countpty_cd);
			stmt4.setString(++cnt, contno);
			stmt4.setString(++cnt, agmtno);
			stmt4.setString(++cnt, cont_type);
			stmt4.setString(++cnt, bu_plant_seq);
			stmt4.setString(++cnt, plant_seq);
			stmt4.setString(++cnt, state_code);
			stmt4.setString(++cnt, instrument_no);
			//stmt4.setString(++cnt, period_start_dt);
			//stmt4.setString(++cnt, period_end_dt);
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
	
	public void getDervContTraderList()
	{
		String function_nm="getDervContTraderList()";
		
		try
		{
			String queryString="SELECT DISTINCT(COUNTERPARTY_CD) FROM FMS_DERV_CONT_MST WHERE COMPANY_CD=?";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String countpty_cd = rset.getString(1)==null?"":rset.getString(1);
				
				VMST_COUNTERPARTY_CD.add(countpty_cd);
				VMST_COUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,countpty_cd));
				VMST_COUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,countpty_cd));
			}
			rset.close();
			stmt.close();
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getDervCRDRTraderList()
	{
		String function_nm="getDervContTraderList()";
		
		try
		{
			queryString="SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_DERV_INVOICE_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND TO_CHAR(INVOICE_DT,'MM/YYYY') = TO_CHAR(TO_DATE(?, 'MM/YYYY'), 'MM/YYYY') "
					+ "AND INVOICE_NO IS NOT NULL AND INV_FLAG=? AND PDF_INV_DTL IS NOT NULL "
					+ "AND NVL((SELECT COUNT(*) FROM FMS_DERV_INVOICE_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND B.REF_NO=A.INVOICE_NO AND B.INV_FLAG IN (?,?)),0) = 0 "
					+ "ORDER BY COUNTERPARTY_CD ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, month+"/"+year);
			stmt.setString(3, "F");
			stmt.setString(4, "CR");
			stmt.setString(5, "DR");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String countpty_cd = rset.getString(1)==null?"":rset.getString(1);
				
				VMST_COUNTERPARTY_CD.add(countpty_cd);
				VMST_COUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,countpty_cd));
				VMST_COUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,countpty_cd));
			}
			rset.close();
			stmt.close();
			
			if(!VCOUNTERPARTY_CD.contains(counterparty_cd) && !counterparty_cd.equals(""))
			{
				VMST_COUNTERPARTY_CD.add(counterparty_cd);
				VMST_COUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn, counterparty_cd));
				VMST_COUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn, counterparty_cd));
			}
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getDervContPlantList()
	{
		String function_nm="getDervContPlantList()";
		
		try
		{
			String queryString="SELECT DISTINCT(PLANT_SEQ_NO),COUNTERPARTY_CD FROM FMS_DERV_CONT_PLANT WHERE COMPANY_CD=? ";
			if(!counterparty_cd.equals("0"))
			{
				queryString+="AND COUNTERPARTY_CD=?";
			}
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(2, counterparty_cd);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String plant_seq = rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd = rset.getString(2)==null?"":rset.getString(2);
				
				VMST_PLANT_SEQ.add(plant_seq);
				VMST_PLANT_ABBR.add(utilBean.getCounterpartyPlantABBR(conn,countpty_cd, comp_cd, plant_seq, "T"));
			}
			rset.close();
			stmt.close();
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getDervContBuPlantList()
	{
		String function_nm="getDervContBuPlantList()";
		
		try
		{
			String queryString="SELECT DISTINCT(PLANT_SEQ_NO) FROM FMS_DERV_CONT_BU WHERE COMPANY_CD=? ";
			if(!counterparty_cd.equals("0"))
			{
				queryString+="AND COUNTERPARTY_CD=?";
			}
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(2, counterparty_cd);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String bu_plant_seq = rset.getString(1)==null?"":rset.getString(1);
				
				VMST_BU_PLANT_SEQ.add(bu_plant_seq);
				VMST_BU_PLANT_ABBR.add(utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B"));
			}
			rset.close();
			stmt.close();
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getDervContCurveList()
	{
		String function_nm="getDervContCurveList()";
		
		try
		{
			String queryString="SELECT DISTINCT(CURVE_NM) FROM FMS_DERV_INSTRUMENT_MST WHERE COMPANY_CD=? "
					+ "AND PRICE_END_DT<=TO_DATE(?,'DD/MM/YYYY') AND STATUS='Y' ";
			if(!counterparty_cd.equals("0"))
			{
				queryString+="AND COUNTERPARTY_CD=?";
			}
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, report_dt);
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(3, counterparty_cd);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String curve_nm = rset.getString(1)==null?"":rset.getString(1);
				
				VMST_CURVE_NM.add(curve_nm);
			}
			rset.close();
			stmt.close();
			
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
			if(operation.equals("INSERT"))
			{
				queryString="SELECT DISTINCT(INVOICE_NO) "
						+ "FROM FMS_DERV_INVOICE_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND TO_CHAR(INVOICE_DT,'MM/YYYY') = TO_CHAR(TO_DATE(?, 'MM/YYYY'), 'MM/YYYY') "
						+ "AND INVOICE_NO IS NOT NULL AND INV_FLAG=? AND PDF_INV_DTL IS NOT NULL "
						+ "AND NVL((SELECT COUNT(*) FROM FMS_DERV_INVOICE_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND B.REF_NO=A.INVOICE_NO AND B.INV_FLAG IN (?,?)),0) = 0 "
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
			VCRITERIA_FLAG.add("QTY");
			VCRITERIA_NAME.add("Change in Quantity");
			VCRITERIA_HIDE.add("N");
			
			VCRITERIA_FLAG.add("FIXEDPRICE");
			VCRITERIA_NAME.add("Change in Fixed Price");
			VCRITERIA_HIDE.add("N");
			
			VCRITERIA_FLAG.add("FLOATPRICE");
			VCRITERIA_NAME.add("Change in Float Price");
			VCRITERIA_HIDE.add("N");
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSelectedInvDtl()
	{
		String function_nm="getSelectedInvDtl()";
		try
		{
			fy_year=dateUtil.getFinancialYear("01/"+month+"/"+year);
			queryString="SELECT DISTINCT BU_UNIT,PLANT_SEQ,BU_STATE_TIN "
					+ "FROM FMS_DERV_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND INVOICE_NO=? AND INV_FLAG='F'";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, sel_inv_no);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				bu_plant_seq=rset.getString(1)==null?"":rset.getString(1);
				plant_seq=rset.getString(2)==null?"":rset.getString(2);
				bu_state_tin=rset.getString(3)==null?"":rset.getString(3);
			}
			rset.close();
			stmt.close();
			
			bu_plant_abbr=""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B");
			plant_abbr=""+utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "T");
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSelectedInvInstrumentDtl()
	{
		String function_nm="getSelectedInvInstrumentDtl()";
		try
		{
			queryString="SELECT INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,BUY_PRICE,BUY_PRICE_UNIT,BUY_AMT,"
					+ "INVOICE_RAISED_IN,INVOICE_AMT,NET_PAYABLE_AMT,"
					+ "REMARK_1,REMARK_2,FINANCIAL_YEAR,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),INVOICE_REF_NO,CHECKED_FLAG,"
					+ "INSTRUMENT_NO,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,INV_TYPE "
					+ "FROM FMS_DERV_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND INVOICE_NO=? AND INV_FLAG='F'";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, sel_inv_no);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				invoice_seq=rset.getString(1)==null?"":rset.getString(1);
				String inv_no=rset.getString(2)==null?"":rset.getString(2);
				inv_dt=rset.getString(3)==null?"":rset.getString(3);
				inv_due_dt=rset.getString(4)==null?"":rset.getString(4);
				double alloc_qty=rset.getDouble(5);
				String sell_price=rset.getString(6)==null?"":rset.getString(6);
				String sell_price_unit=rset.getString(7)==null?"":rset.getString(7);
				double sell_amt=rset.getDouble(8);
				String buy_price=rset.getString(9)==null?"":rset.getString(9);
				String buy_price_unit=rset.getString(10)==null?"":rset.getString(10);
				double buy_amt=rset.getDouble(11);
				String inv_raised_in=rset.getString(12)==null?"":rset.getString(12);
				String invoice_amt=rset.getString(13)==null?"":rset.getString(13);
				String net_payable_amt=rset.getString(14)==null?"":rset.getString(14);
				String financial_year=rset.getString(17)==null?"":rset.getString(17);
				period_start_dt=rset.getString(18)==null?"":rset.getString(18);
				period_end_dt=rset.getString(19)==null?"":rset.getString(19);
				//inv_ref=rset.getString(20)==null?"":rset.getString(20);
				chk_flg=rset.getString(21)==null?"":rset.getString(21);
				
				String instrument_no = rset.getString(22)==null?"":rset.getString(22);
				String agmt_no = rset.getString(23)==null?"":rset.getString(23);
				String agmt_rev_no = rset.getString(24)==null?"":rset.getString(24);
				String cont_no = rset.getString(25)==null?"":rset.getString(25);
				String cont_rev_no = rset.getString(26)==null?"":rset.getString(26);
				String cont_type = rset.getString(27)==null?"":rset.getString(27);
				invoice_type = rset.getString(28)==null?"":rset.getString(28);
				
				VPERIOD_START_DT.add(period_start_dt);
				VPERIOD_END_DT.add(period_end_dt);
				VCOUNTERPARTY_CD.add(counterparty_cd);
				VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn, counterparty_cd));
				VAGMT_NO.add(agmt_no);
				VAGMT_REV.add(agmt_rev_no);
				VCONT_TYPE.add(cont_type);
				VCONT_NO.add(cont_no);
				VCONT_REV.add(cont_rev_no);
				VINVOICE_NO.add(inv_no);
				VALL_INSTRUMENT_NO.add(""+utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, cont_type, instrument_no));
				
				VINSTRUMENT_NO.add(instrument_no);
				
				VMAIN_BOOKED_MMBTU.add(nf.format(alloc_qty));
			  	VCONT_START_DT.add(period_start_dt);
			  	VCONT_END_DT.add(period_end_dt);
			  	VDEAL_PRICE_CURVE.add(financial_curve);
			  	
			  	int cnt2=0;
			  	String cont_ref="";
			  	String trade_dt="";
			  	String instrument_volume_unit="";
			  	String buySell="";
			  	String instrument_type="";
			  	String curve_nm="";
			  	String instru_duration="";
			  	queryString2="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_TYPE,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,A.CONT_NO,A.CONT_REV,A.CONT_NAME,A.CONT_REF_NO,"
						+ "B.INSTRUMENT_NO,B.INSTRUMENT_TYPE,B.BUY_SELL,B.STATUS,B.QTY,B.QTY_UNIT,B.RATE,B.RATE_UNIT,"
						+ "B.PRODUCT_NM,B.CURVE_NM,B.PROJ_METHOD,TO_CHAR(B.CONT_DD_MM_YR,'DD/MM/YYYY'),"
						+ "TO_CHAR(B.PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(B.PRICE_END_DT,'DD/MM/YYYY'),B.CONV_FACTOR,TO_CHAR(A.TRADE_DT,'DD/MM/YYYY') "
						+ "FROM FMS_DERV_CONT_MST A,FMS_DERV_INSTRUMENT_MST B "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
						+ "AND B.STATUS='Y' AND B.INSTRUMENT_NO=? "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO";
			  	stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(++cnt2, comp_cd);
				stmt2.setString(++cnt2, counterparty_cd);
				stmt2.setString(++cnt2, agmt_no);
				stmt2.setString(++cnt2, cont_no);
				stmt2.setString(++cnt2, cont_type);
				//stmt2.setString(++cnt2, buy_sell);
				stmt2.setString(++cnt2, instrument_no);
				rset2=stmt2.executeQuery();
				while(rset2.next())
				{
					cont_ref=rset2.getString(10)==null?"":rset2.getString(10);
					trade_dt=rset2.getString(26)==null?"":rset2.getString(26);
					String instrument_volume_unit_cd = rset2.getString(16)==null?"":rset2.getString(16);
					instrument_volume_unit = utilBean.getEnergyUnitNm(conn, instrument_volume_unit_cd);
					buySell = rset2.getString(13)==null?"":rset2.getString(13);
					instrument_type = rset2.getString(12)==null?"":rset2.getString(12);
					curve_nm = rset2.getString(20)==null?"":rset2.getString(20);
					String start_dt = rset2.getString(23)==null?"":rset2.getString(23);
					String end_dt = rset2.getString(24)==null?"":rset2.getString(24);
					instru_duration = start_dt+"-"+end_dt;
				}
				rset2.close();
				stmt2.close();
				VCONT_REF.add(cont_ref);
				VTRADE_DT.add(trade_dt);
				VQTY_UNIT.add(instrument_volume_unit);
				VMAIN_BUY_SELL.add(buySell);
				
				if(buySell.equals("BUY"))
				{
					VINV_FIXED_PRICE.add(buy_price);
				}
				else if(buySell.equals("SELL"))
				{
					VINV_FIXED_PRICE.add(sell_price);
				}
				if(buySell.equals("BUY"))
				{
					BigDecimal float_val=new BigDecimal(sell_price);
					VMAIN_SELL_RATE.add(df3.format(float_val));
				}
				else
				{
					VMAIN_SELL_RATE.add(nf2.format(Double.parseDouble(sell_price)));
				}
				
				if(Double.doubleToRawLongBits(sell_amt)<Double.doubleToRawLongBits(0))
				{
					sell_amt=(-1)*sell_amt;
				}
			  	VMAIN_SELL_AMT.add(nf.format(sell_amt));
			  	if(buySell.equals("SELL"))
				{
			  		BigDecimal float_val=new BigDecimal(buy_price);
			  		VMAIN_BUY_RATE.add(df3.format(float_val));
				}
			  	else
			  	{
			  		VMAIN_BUY_RATE.add(nf2.format(Double.parseDouble(buy_price)));
			  	}
			  	
			  	if(Double.doubleToRawLongBits(buy_amt)<Double.doubleToRawLongBits(0))
				{
			  		buy_amt=(-1)*buy_amt;
				}
			  	VMAIN_BUY_AMT.add(nf.format(buy_amt));
			  	VINSTRUMENT_TYPE.add(instrument_type);
			  	VCURVE_NM.add(curve_nm);
			  	VINSTRUMENT_DURATION.add(instru_duration);
			  	VMAIN_TOTAL_AMT.add(nf.format(sell_amt-buy_amt));
			
			}
			rset.close();
			stmt.close();
			
			bank_formula=utilBean.getEntityBankFormula(conn, comp_cd, counterparty_cd, "T", "DERV", inv_dt);
			
			/*String queryString="SELECT COUNTERPARTY_CD,ENTITY,TO_CHAR(EFF_DT,'DD/MM/YYYY'),BANK_NAME,ACCOUNT_ID,IFSC_CODE,BRANCH_NAME,"
					+ "STATE_NM,CATEGORY  "
					+ "FROM FMS_ENTITY_BANK_MST A "
					+ "WHERE ENTITY=? AND COUNTERPARTY_CD=? AND COMPANY_CD=? AND CATEGORY=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_BANK_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.CATEGORY=B.CATEGORY)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, "B");
			stmt.setString(2, comp_cd);
			stmt.setString(3, comp_cd);
			stmt.setString(4, "DERV");
			rset=stmt.executeQuery();
			if(rset.next())
			{
				String bank_eff_dt = rset.getString(3)==null?"":rset.getString(3);
				String bank_name=rset.getString(4)==null?"":rset.getString(4);
				String bank_account_no=rset.getString(5)==null?"":rset.getString(5);
				String ifsc_code=rset.getString(6)==null?"":rset.getString(6);
				String bank_branch=rset.getString(7)==null?"":rset.getString(7);
				String bank_state=rset.getString(8)==null?"":rset.getString(8);
				String bank_category=rset.getString(9)==null?"":rset.getString(9);
				
				bank_formula=bank_name+", "+bank_branch+", "+bank_state+", A/C No : "+bank_account_no+", IFSC Code : "+ifsc_code;
			}
			rset.close();
			stmt.close();*/
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSelectedInvInstrumentDtlForChk()
	{
		String function_nm="getSelectedInvInstrumentDtlForChk()";
		try
		{
			for(int i=0; i<VDEAL_MAPPING.size(); i++)
			{
				queryString="SELECT INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
						+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,BUY_PRICE,BUY_PRICE_UNIT,BUY_AMT,"
						+ "INVOICE_RAISED_IN,INVOICE_AMT,NET_PAYABLE_AMT,"
						+ "REMARK_1,REMARK_2,FINANCIAL_YEAR,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),INVOICE_REF_NO,CHECKED_FLAG,"
						+ "INSTRUMENT_NO,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,INV_TYPE "
						+ "FROM FMS_DERV_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND INVOICE_NO=? AND INV_FLAG='F' AND CONT_NO=? AND AGMT_NO=? AND CONTRACT_TYPE=? AND INSTRUMENT_NO=?";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, sel_inv_no);
				stmt.setString(4, ""+VCONT_NO.elementAt(i));
				stmt.setString(5, ""+VAGMT_NO.elementAt(i));
				stmt.setString(6, ""+VCONT_TYPE.elementAt(i));
				stmt.setString(7, ""+VINSTRUMENT_NO.elementAt(i));
				rset=stmt.executeQuery();
				while(rset.next())
				{
					invoice_seq=rset.getString(1)==null?"":rset.getString(1);
					String inv_no=rset.getString(2)==null?"":rset.getString(2);
					inv_dt=rset.getString(3)==null?"":rset.getString(3);
					inv_due_dt=rset.getString(4)==null?"":rset.getString(4);
					double alloc_qty=rset.getDouble(5);
					String sell_price=rset.getString(6)==null?"":rset.getString(6);
					String sell_price_unit=rset.getString(7)==null?"":rset.getString(7);
					double sell_amt=rset.getDouble(8);
					String buy_price=rset.getString(9)==null?"":rset.getString(9);
					String buy_price_unit=rset.getString(10)==null?"":rset.getString(10);
					double buy_amt=rset.getDouble(11);
					String inv_raised_in=rset.getString(12)==null?"":rset.getString(12);
					String invoice_amt=rset.getString(13)==null?"":rset.getString(13);
					String net_payable_amt=rset.getString(14)==null?"":rset.getString(14);
					String financial_year=rset.getString(17)==null?"":rset.getString(17);
					period_start_dt=rset.getString(18)==null?"":rset.getString(18);
					period_end_dt=rset.getString(19)==null?"":rset.getString(19);
					//inv_ref=rset.getString(20)==null?"":rset.getString(20);
					chk_flg=rset.getString(21)==null?"":rset.getString(21);
					
					String instrument_no = rset.getString(22)==null?"":rset.getString(22);
					String agmt_no = rset.getString(23)==null?"":rset.getString(23);
					String agmt_rev_no = rset.getString(24)==null?"":rset.getString(24);
					String cont_no = rset.getString(25)==null?"":rset.getString(25);
					String cont_rev_no = rset.getString(26)==null?"":rset.getString(26);
					String cont_type = rset.getString(27)==null?"":rset.getString(27);
					invoice_type = rset.getString(28)==null?"":rset.getString(28);
					
					VPERIOD_START_DT.add(period_start_dt);
					VPERIOD_END_DT.add(period_end_dt);
					VCOUNTERPARTY_CD.add(counterparty_cd);
					VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn, counterparty_cd));
					VINVOICE_NO.add(inv_no);
					VMAIN_BOOKED_MMBTU.add(nf.format(alloc_qty));
				  	VCONT_START_DT.add(period_start_dt);
				  	VCONT_END_DT.add(period_end_dt);
				  	VDEAL_PRICE_CURVE.add(financial_curve);
				  	
				  	int cnt2=0;
				  	String cont_ref="";
				  	String trade_dt="";
				  	String instrument_volume_unit="";
				  	String buySell="";
				  	String instrument_type="";
				  	String curve_nm="";
				  	String instru_duration="";
				  	queryString2="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_TYPE,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,A.CONT_NO,A.CONT_REV,A.CONT_NAME,A.CONT_REF_NO,"
							+ "B.INSTRUMENT_NO,B.INSTRUMENT_TYPE,B.BUY_SELL,B.STATUS,B.QTY,B.QTY_UNIT,B.RATE,B.RATE_UNIT,"
							+ "B.PRODUCT_NM,B.CURVE_NM,B.PROJ_METHOD,TO_CHAR(B.CONT_DD_MM_YR,'DD/MM/YYYY'),"
							+ "TO_CHAR(B.PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(B.PRICE_END_DT,'DD/MM/YYYY'),B.CONV_FACTOR,TO_CHAR(A.TRADE_DT,'DD/MM/YYYY') "
							+ "FROM FMS_DERV_CONT_MST A,FMS_DERV_INSTRUMENT_MST B "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
							+ "AND B.STATUS='Y' AND B.INSTRUMENT_NO=? "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO";
				  	stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(++cnt2, comp_cd);
					stmt2.setString(++cnt2, counterparty_cd);
					stmt2.setString(++cnt2, agmt_no);
					stmt2.setString(++cnt2, cont_no);
					stmt2.setString(++cnt2, cont_type);
					//stmt2.setString(++cnt2, buy_sell);
					stmt2.setString(++cnt2, instrument_no);
					rset2=stmt2.executeQuery();
					while(rset2.next())
					{
						cont_ref=rset2.getString(10)==null?"":rset2.getString(10);
						trade_dt=rset2.getString(26)==null?"":rset2.getString(26);
						String instrument_volume_unit_cd = rset2.getString(16)==null?"":rset2.getString(16);
						instrument_volume_unit = utilBean.getEnergyUnitNm(conn, instrument_volume_unit_cd);
						buySell = rset2.getString(13)==null?"":rset2.getString(13);
						instrument_type = rset2.getString(12)==null?"":rset2.getString(12);
						curve_nm = rset2.getString(20)==null?"":rset2.getString(20);
						String start_dt = rset2.getString(23)==null?"":rset2.getString(23);
						String end_dt = rset2.getString(24)==null?"":rset2.getString(24);
						instru_duration = start_dt+"-"+end_dt;
					}
					rset2.close();
					stmt2.close();
					VCONT_REF.add(cont_ref);
					VTRADE_DT.add(trade_dt);
					VQTY_UNIT.add(instrument_volume_unit);
					VMAIN_BUY_SELL.add(buySell);
					
					if(buySell.equals("BUY"))
					{
						VINV_FIXED_PRICE.add(buy_price);
					}
					else if(buySell.equals("SELL"))
					{
						VINV_FIXED_PRICE.add(sell_price);
					}
					if(buySell.equals("BUY"))
					{
						BigDecimal float_val=new BigDecimal(sell_price);
						VMAIN_SELL_RATE.add(df3.format(float_val));
					}
					else
					{
						VMAIN_SELL_RATE.add(nf2.format(Double.parseDouble(sell_price)));
					}
					
					if(Double.doubleToRawLongBits(sell_amt)<Double.doubleToRawLongBits(0))
					{
						sell_amt=(-1)*sell_amt;
					}
				  	VMAIN_SELL_AMT.add(nf.format(sell_amt));
				  	if(buySell.equals("SELL"))
					{
				  		BigDecimal float_val=new BigDecimal(buy_price);
				  		VMAIN_BUY_RATE.add(df3.format(float_val));
					}
				  	else
				  	{
				  		VMAIN_BUY_RATE.add(nf2.format(Double.parseDouble(buy_price)));
				  	}
				  	
				  	if(Double.doubleToRawLongBits(buy_amt)<Double.doubleToRawLongBits(0))
					{
				  		buy_amt=(-1)*buy_amt;
					}
				  	VMAIN_BUY_AMT.add(nf.format(buy_amt));
				  	VINSTRUMENT_TYPE.add(instrument_type);
				  	VCURVE_NM.add(curve_nm);
				  	VINSTRUMENT_DURATION.add(instru_duration);
				  	VMAIN_TOTAL_AMT.add(nf.format(sell_amt-buy_amt));
				
				}
				rset.close();
				stmt.close();
			}
			bank_formula=utilBean.getEntityBankFormula(conn, comp_cd, counterparty_cd, "T", "DERV", inv_dt);
			
			/*String queryString="SELECT COUNTERPARTY_CD,ENTITY,TO_CHAR(EFF_DT,'DD/MM/YYYY'),BANK_NAME,ACCOUNT_ID,IFSC_CODE,BRANCH_NAME,"
					+ "STATE_NM,CATEGORY  "
					+ "FROM FMS_ENTITY_BANK_MST A "
					+ "WHERE ENTITY=? AND COUNTERPARTY_CD=? AND COMPANY_CD=? AND CATEGORY=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_BANK_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.CATEGORY=B.CATEGORY)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, "B");
			stmt.setString(2, comp_cd);
			stmt.setString(3, comp_cd);
			stmt.setString(4, "DERV");
			rset=stmt.executeQuery();
			if(rset.next())
			{
				String bank_eff_dt = rset.getString(3)==null?"":rset.getString(3);
				String bank_name=rset.getString(4)==null?"":rset.getString(4);
				String bank_account_no=rset.getString(5)==null?"":rset.getString(5);
				String ifsc_code=rset.getString(6)==null?"":rset.getString(6);
				String bank_branch=rset.getString(7)==null?"":rset.getString(7);
				String bank_state=rset.getString(8)==null?"":rset.getString(8);
				String bank_category=rset.getString(9)==null?"":rset.getString(9);
				
				bank_formula=bank_name+", "+bank_branch+", "+bank_state+", A/C No : "+bank_account_no+", IFSC Code : "+ifsc_code;
			}
			rset.close();
			stmt.close();*/
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getRevisedInstrumentDtl()
	{
		String function_nm="getRevisedInstrumentDtl()";
		try
		{
			for(int i=0; i<VALL_INSTRUMENT_NO.size(); i++)
			{
				queryString = "SELECT INSTRUMENT_NO,ALLOC_QTY,SALE_PRICE,BUY_PRICE,BU_STATE_TIN,FINANCIAL_YEAR,INVOICE_SEQ,INV_TYPE,"
						+ "AGMT_NO,CONT_NO,CONTRACT_TYPE "
						+ "FROM FMS_DERV_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND REF_NO=? AND INSTRUMENT_NO=? AND INV_FLAG IN ('CR','DR') AND CONT_NO=? AND AGMT_NO=? AND CONTRACT_TYPE=?";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, sel_inv_no);
				stmt.setString(4, ""+VINSTRUMENT_NO.elementAt(i));
				stmt.setString(5, ""+VCONT_NO.elementAt(i));
				stmt.setString(6, ""+VAGMT_NO.elementAt(i));
				stmt.setString(7, ""+VCONT_TYPE.elementAt(i));
				rset=stmt.executeQuery();
				if(rset.next())
				{
					String tmp_instru_no=rset.getString(1)==null?"":rset.getString(1);
					String tmp_allocQty = rset.getString(2)==null?"":rset.getString(2);
					String tmp_sellRate = rset.getString(3)==null?"":rset.getString(3);
					String tmp_buyRate = rset.getString(4)==null?"":rset.getString(4);
					String tmp_buStateTin = rset.getString(5)==null?"":rset.getString(5);
					String tmp_finYear = rset.getString(6)==null?"":rset.getString(6);
					String tmp_invSeq = rset.getString(7)==null?"":rset.getString(7);
					String tmp_invType = rset.getString(8)==null?"":rset.getString(8);
					String tmp_agmtNo = rset.getString(9)==null?"":rset.getString(9);
					String tmp_contNo = rset.getString(10)==null?"":rset.getString(10);
					String tmp_contType = rset.getString(11)==null?"":rset.getString(11);
					
					queryString1="SELECT INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
							+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,BUY_PRICE,BUY_PRICE_UNIT,BUY_AMT,"
							+ "INVOICE_RAISED_IN,INVOICE_AMT,NET_PAYABLE_AMT,"
							+ "REMARK_1,REMARK_2,FINANCIAL_YEAR,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),INVOICE_REF_NO,CHECKED_FLAG,"
							+ "INSTRUMENT_NO,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,INV_TYPE "
							+ "FROM FMS_DERV_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND INVOICE_NO=? AND INSTRUMENT_NO=? AND INV_FLAG IN ('F') AND CONT_NO=? AND AGMT_NO=?";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, sel_inv_no);
					stmt1.setString(4, tmp_instru_no);
					stmt1.setString(5, tmp_contNo);
					stmt1.setString(6, tmp_agmtNo);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						String inv_no=rset1.getString(2)==null?"":rset1.getString(2);
						String sell_price=rset1.getString(6)==null?"":rset1.getString(6);
						String sell_price_unit=rset1.getString(7)==null?"":rset1.getString(7);
						String buy_price=rset1.getString(9)==null?"":rset1.getString(9);
						String buy_price_unit=rset1.getString(10)==null?"":rset1.getString(10);
						String inv_raised_in=rset1.getString(12)==null?"":rset1.getString(12);
						String financial_year=rset1.getString(17)==null?"":rset1.getString(17);
						
						String instrument_no = rset1.getString(22)==null?"":rset1.getString(22);
						String agmt_no = rset1.getString(23)==null?"":rset1.getString(23);
						String agmt_rev_no = rset1.getString(24)==null?"":rset1.getString(24);
						String cont_no = rset1.getString(25)==null?"":rset1.getString(25);
						String cont_rev_no = rset1.getString(26)==null?"":rset1.getString(26);
						String cont_type = rset1.getString(27)==null?"":rset1.getString(27);
						
					  	int cnt2=0;
					  	String cont_ref="";
					  	String trade_dt="";
					  	String instrument_volume_unit="";
					  	String buySell="";
					  	String instrument_type="";
					  	String curve_nm="";
					  	String instru_duration="";
					  	String instru_qty="";
					  	String fixed_price="";
					  	queryString2="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_TYPE,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,A.CONT_NO,A.CONT_REV,A.CONT_NAME,A.CONT_REF_NO,"
								+ "B.INSTRUMENT_NO,B.INSTRUMENT_TYPE,B.BUY_SELL,B.STATUS,B.QTY,B.QTY_UNIT,B.RATE,B.RATE_UNIT,"
								+ "B.PRODUCT_NM,B.CURVE_NM,B.PROJ_METHOD,TO_CHAR(B.CONT_DD_MM_YR,'DD/MM/YYYY'),"
								+ "TO_CHAR(B.PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(B.PRICE_END_DT,'DD/MM/YYYY'),B.CONV_FACTOR,TO_CHAR(A.TRADE_DT,'DD/MM/YYYY') "
								+ "FROM FMS_DERV_CONT_MST A,FMS_DERV_INSTRUMENT_MST B "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
								+ "AND B.STATUS='Y' AND B.INSTRUMENT_NO=? "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
								+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO";
					  	stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(++cnt2, comp_cd);
						stmt2.setString(++cnt2, counterparty_cd);
						stmt2.setString(++cnt2, agmt_no);
						stmt2.setString(++cnt2, cont_no);
						stmt2.setString(++cnt2, cont_type);
						//stmt2.setString(++cnt2, buy_sell);
						stmt2.setString(++cnt2, instrument_no);
						rset2=stmt2.executeQuery();
						while(rset2.next())
						{
							cont_ref=rset2.getString(10)==null?"":rset2.getString(10);
							trade_dt=rset2.getString(26)==null?"":rset2.getString(26);
							instru_qty=nf.format(rset2.getDouble(15));
							String instrument_volume_unit_cd = rset2.getString(16)==null?"":rset2.getString(16);
							instrument_volume_unit = utilBean.getEnergyUnitNm(conn, instrument_volume_unit_cd);
							fixed_price = nf2.format(rset2.getDouble(17));
							buySell = rset2.getString(13)==null?"":rset2.getString(13);
							instrument_type = rset2.getString(12)==null?"":rset2.getString(12);
							curve_nm = rset2.getString(20)==null?"":rset2.getString(20);
							String start_dt = rset2.getString(23)==null?"":rset2.getString(23);
							String end_dt = rset2.getString(24)==null?"":rset2.getString(24);
							instru_duration = start_dt+"-"+end_dt;
						}
						rset2.close();
						stmt2.close();
						
						String tmp_buy_price="";
						String tmp_sell_price="";
						int cnt3=0;
						queryString3="SELECT BUY_PRICE,SALE_PRICE "
								+ "FROM FMS_DERV_INV_CRDR_REF "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BU_STATE_TIN=? AND FINANCIAL_YEAR=? "
								+ "AND INVOICE_SEQ=? AND INSTRUMENT_NO=? AND CONT_NO=? AND AGMT_NO=? AND CONTRACT_TYPE=? AND INV_TYPE=?";
						stmt3 = conn.prepareStatement(queryString3);
						stmt3.setString(++cnt3, comp_cd);
						stmt3.setString(++cnt3, counterparty_cd);
						stmt3.setString(++cnt3, tmp_buStateTin);
						stmt3.setString(++cnt3, tmp_finYear);
						stmt3.setString(++cnt3, tmp_invSeq);
						stmt3.setString(++cnt3, tmp_instru_no);
						stmt3.setString(++cnt3, tmp_contNo);
						stmt3.setString(++cnt3, tmp_agmtNo);
						stmt3.setString(++cnt3, tmp_contType);
						stmt3.setString(++cnt3, tmp_invType);
						rset3=stmt3.executeQuery();
						while(rset3.next())
						{
							tmp_buy_price = rset3.getString(1)==null?"":rset3.getString(1);
							tmp_sell_price = rset3.getString(2)==null?"":rset3.getString(2);
						}
						rset3.close();
						stmt3.close();
						
						VNEW_QTY.add(instru_qty);
						VNEW_QTY_UNIT.add(instrument_volume_unit);
						
						String sell_rate="";
						if(!tmp_sell_price.equals("") || !tmp_sell_price.equals("0.00"))
						{
							sell_rate=tmp_sell_price;
						}
						else
						{
							sell_rate=sell_price;
						}
						String tmp_sell_rate = "";
						double new_sale_amt=0;
						
						if(buySell.equals("BUY"))
						{
							if(!sell_rate.equals(""))
							{
								BigDecimal float_val=new BigDecimal(sell_rate);
								tmp_sell_rate=df3.format(float_val);
							}
							else
							{
								tmp_sell_rate=sell_price;
							}
						}
						else
						{
							tmp_sell_rate=fixed_price;
						}
						new_sale_amt=Double.parseDouble(instru_qty)*Double.parseDouble(tmp_sell_rate);
						VNEW_SELL_RATE.add(tmp_sell_rate);
					  	VNEW_SELL_AMT.add(nf.format(new_sale_amt));
					  	
					  	String buy_rate="";
						if(!tmp_buy_price.equals("") || !tmp_buy_price.equals("0.00"))
						{
							buy_rate=tmp_buy_price;
						}
						else
						{
							buy_rate=buy_price;
						}
						
					  	String tmp_buy_rate = "";
					  	double new_buy_amt=0;
					  	if(buySell.equals("SELL"))
						{
					  		if(!buy_rate.equals(""))
							{
						  		BigDecimal float_val=new BigDecimal(buy_rate);
						  		tmp_buy_rate=df3.format(float_val);
							}
					  		else
					  		{
					  			tmp_buy_rate=buy_price;
					  		}
						}
					  	else
					  	{
					  		tmp_buy_rate=fixed_price;
					  	}
					  	new_buy_amt=Double.parseDouble(instru_qty)*Double.parseDouble(tmp_buy_rate);
					  	VNEW_BUY_RATE.add(tmp_buy_rate);
					  	VNEW_BUY_AMT.add(nf.format(new_buy_amt));
					  	
					  	double new_total_amt=new_sale_amt-new_buy_amt;
					  	VNEW_TOTAL_AMT.add(nf.format(new_total_amt));
					}
					rset1.close();
					stmt1.close();
				}
				else
				{
					VNEW_QTY.add("");
					VNEW_QTY_UNIT.add("");
					VNEW_SELL_RATE.add("");
				  	VNEW_SELL_AMT.add("");
				  	VNEW_BUY_RATE.add("");
				  	VNEW_BUY_AMT.add("");
				  	VNEW_TOTAL_AMT.add("");
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
	
	public void getSubmittedInstruCRDRDtl()
	{
		String function_nm="getSubmittedInstruCRDRDtl()";
		try
		{
			for(int i=0; i<VALL_INSTRUMENT_NO.size(); i++)
			{
				queryString="SELECT INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
						+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,BUY_PRICE,BUY_PRICE_UNIT,BUY_AMT,"
						+ "INVOICE_RAISED_IN,INVOICE_AMT,NET_PAYABLE_AMT,"
						+ "REMARK_1,REMARK_2,FINANCIAL_YEAR,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),INVOICE_REF_NO,CHECKED_FLAG,"
						+ "INSTRUMENT_NO,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CRITERIA,INV_FLAG,REF_NO "
						+ "FROM FMS_DERV_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND INSTRUMENT_NO=? "
						+ "AND REF_NO=? AND INV_FLAG IN ('CR','DR') AND CONT_NO=? AND AGMT_NO=? AND CONTRACT_TYPE=? ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, ""+VINSTRUMENT_NO.elementAt(i));
				stmt.setString(4, sel_inv_no);
				stmt.setString(5, ""+VCONT_NO.elementAt(i));
				stmt.setString(6, ""+VAGMT_NO.elementAt(i));
				stmt.setString(7, ""+VCONT_TYPE.elementAt(i));
				rset=stmt.executeQuery();
				if(rset.next())
				{
					crdr_dt=rset.getString(3)==null?"":rset.getString(3);
					crdr_due_dt=rset.getString(4)==null?"":rset.getString(4);
					double alloc_qty=rset.getDouble(5);
					String sell_price=rset.getString(6)==null?"":rset.getString(6);
					String sell_price_unit=rset.getString(7)==null?"":rset.getString(7);
					String sell_amt=nf.format(rset.getDouble(8));
					String buy_price=rset.getString(9)==null?"":rset.getString(9);
					String buy_price_unit=rset.getString(10)==null?"":rset.getString(10);
					String buy_amt=nf.format(rset.getDouble(11));
					String inv_raised_in=rset.getString(12)==null?"":rset.getString(12);
					String invoice_amt=rset.getString(13)==null?"":rset.getString(13);
					String net_payable_amt=rset.getString(14)==null?"":rset.getString(14);
					crdr_remark=rset.getString(15)==null?"":rset.getString(15);
					String financial_year=rset.getString(17)==null?"":rset.getString(17);
					
					String instrument_no = rset.getString(22)==null?"":rset.getString(22);
					String agmt_no = rset.getString(23)==null?"":rset.getString(23);
					String agmt_rev_no = rset.getString(24)==null?"":rset.getString(24);
					String cont_no = rset.getString(25)==null?"":rset.getString(25);
					String cont_rev_no = rset.getString(26)==null?"":rset.getString(26);
					String cont_type = rset.getString(27)==null?"":rset.getString(27);
					criteri_formula = rset.getString(28)==null?"":rset.getString(28);
					crdr_type = rset.getString(29)==null?"":rset.getString(29);
					sel_inv_no = rset.getString(30)==null?"":rset.getString(30);
					inv_ref = rset.getString(20)==null?"":rset.getString(20);
					
					VBOOKED_MMBTU.add(nf.format(alloc_qty));
				  	
				  	int cnt2=0;
				  	String cont_ref="";
				  	String trade_dt="";
				  	String instrument_volume_unit="";
				  	String buySell="";
				  	String instrument_type="";
				  	String curve_nm="";
				  	String instru_duration="";
				  	queryString2="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_TYPE,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,A.CONT_NO,A.CONT_REV,A.CONT_NAME,A.CONT_REF_NO,"
							+ "B.INSTRUMENT_NO,B.INSTRUMENT_TYPE,B.BUY_SELL,B.STATUS,B.QTY,B.QTY_UNIT,B.RATE,B.RATE_UNIT,"
							+ "B.PRODUCT_NM,B.CURVE_NM,B.PROJ_METHOD,TO_CHAR(B.CONT_DD_MM_YR,'DD/MM/YYYY'),"
							+ "TO_CHAR(B.PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(B.PRICE_END_DT,'DD/MM/YYYY'),B.CONV_FACTOR,TO_CHAR(A.TRADE_DT,'DD/MM/YYYY') "
							+ "FROM FMS_DERV_CONT_MST A,FMS_DERV_INSTRUMENT_MST B "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
							+ "AND B.STATUS='Y' AND B.INSTRUMENT_NO=? "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO";
				  	stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(++cnt2, comp_cd);
					stmt2.setString(++cnt2, counterparty_cd);
					stmt2.setString(++cnt2, agmt_no);
					stmt2.setString(++cnt2, cont_no);
					stmt2.setString(++cnt2, cont_type);
					//stmt2.setString(++cnt2, buy_sell);
					stmt2.setString(++cnt2, instrument_no);
					rset2=stmt2.executeQuery();
					while(rset2.next())
					{
						cont_ref=rset2.getString(10)==null?"":rset2.getString(10);
						trade_dt=rset2.getString(26)==null?"":rset2.getString(26);
						String instrument_volume_unit_cd = rset2.getString(16)==null?"":rset2.getString(16);
						instrument_volume_unit = utilBean.getEnergyUnitNm(conn, instrument_volume_unit_cd);
						buySell = rset2.getString(13)==null?"":rset2.getString(13);
						instrument_type = rset2.getString(12)==null?"":rset2.getString(12);
						curve_nm = rset2.getString(20)==null?"":rset2.getString(20);
						String start_dt = rset2.getString(23)==null?"":rset2.getString(23);
						String end_dt = rset2.getString(24)==null?"":rset2.getString(24);
						instru_duration = start_dt+"-"+end_dt;
					}
					rset2.close();
					stmt2.close();
					VBUY_SELL.add(buySell);
					
					if(!sell_price.equals(""))
					{
						if(buySell.equals("BUY"))
						{
							BigDecimal float_val=new BigDecimal(sell_price);
							VSELL_RATE.add(df3.format(float_val));
						}
						else
						{
							VSELL_RATE.add(nf2.format(Double.parseDouble(sell_price)));
						}
					}
					else
					{
						VSELL_RATE.add("");
					}
				  	VSELL_AMT.add(sell_amt);
				  	
				  	if(!buy_price.equals(""))
				  	{
					  	if(buySell.equals("SELL"))
						{
					  		BigDecimal float_val=new BigDecimal(buy_price);
					  		VBUY_RATE.add(df3.format(float_val));
						}
					  	else
					  	{
					  		VBUY_RATE.add(nf2.format(Double.parseDouble(buy_price)));
					  	}
				  	}
				  	else
				  	{
				  		VBUY_RATE.add("");
				  	}
				  	
				  	VBUY_AMT.add(buy_amt);
				  	IS_CRDR_SUBMITTED.add("Y");
				}
				else
				{
					VBOOKED_MMBTU.add("");
					VBUY_SELL.add("");
					VSELL_RATE.add("");
					VSELL_AMT.add("");
					VBUY_RATE.add("");
					VBUY_AMT.add("");
					IS_CRDR_SUBMITTED.add("N");
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
	
	public void getCRDRPreparationList()
	{
		String function_nm="getCRDRPreparationList()";
		try
		{
			String temp_period_start_dt=""+dateUtil.getFirstDateOfMonth(month, year);
			String temp_period_end_dt=""+dateUtil.getLastDateOfMonth(month, year);
			
			for(int i=0; i<VMST_INV_TYPE_FLG.size(); i++)
			{
				int index=0;
						
				String queryString="SELECT DISTINCT INVOICE_SEQ,COUNTERPARTY_CD,BU_STATE_TIN "
						+ "FROM FMS_DERV_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND TO_DATE(?,'DD/MM/YYYY') <=INVOICE_DT AND TO_DATE(?,'DD/MM/YYYY') >= INVOICE_DT AND INV_TYPE=? "
						+ "AND INV_FLAG IN ('CR','DR')";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, temp_period_start_dt);
				stmt.setString(3, temp_period_end_dt);
				stmt.setString(4, ""+VMST_INV_TYPE_FLG.elementAt(i));
				rset=stmt.executeQuery();
				while(rset.next())
				{
					index+=1;
					
					String inv_seq=rset.getString(1)==null?"":rset.getString(1);
					String contPtyCd=rset.getString(2)==null?"":rset.getString(2);
					String buStTin=rset.getString(3)==null?"":rset.getString(3);
					String own_cd="";
					String countpty_cd="";
					String agmtno="";
					String agmtrev="";
					String contno="";
					String contrev="";
					String cont_type="";
					String instrument_no="";
					String deal_no="";
					String plant_seq="";
					String bu_plant_seq="";
					String billing_cycle="";
					String fin_year="";
					String invoice_no="";
					String invoice_seq="";
					String checked_flag="";
					String approved_flag="";
					String pdf_flg="";
					String pdf_ori="";
					String pdf_dup="";
					String pdf_tri="";
					String sap_approved_flag="";
					String bu_state_tin="";
					String period_start_dt="";
					String period_end_dt="";
					String invoice_dt="";
					String authorized_flag="";
					String invoice_type="";
					String contRef="";
					String ref_no="";
					String crdr_type="";
					String criteria="";
					String disp_criteria="";
					
					String queryString1="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE, "
							+ "BU_UNIT,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),"
							+ "INVOICE_NO,CHECKED_FLAG,APPROVED_FLAG,INVOICE_SEQ,FINANCIAL_YEAR,INV_TYPE,"
							+ "PDF_INV_DTL,BU_STATE_TIN, "
							+ "PRINT_BY_ORI,SAP_APPROVAL,INSTRUMENT_NO,PLANT_SEQ,FREQ,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),AUTHORIZED_FLAG,INV_TYPE,"
							+ "INV_FLAG,REF_NO,CRITERIA,PRINT_BY_DUP,PRINT_BY_TRI "
							+ "FROM FMS_DERV_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND TO_DATE(?,'DD/MM/YYYY') <=INVOICE_DT AND TO_DATE(?,'DD/MM/YYYY') >= INVOICE_DT "
							+ "AND INV_TYPE=? AND INVOICE_SEQ=? AND COUNTERPARTY_CD=? AND BU_STATE_TIN=? AND INV_FLAG IN ('CR','DR') "
							+ "ORDER BY INVOICE_SEQ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, temp_period_start_dt);
					stmt1.setString(3, temp_period_end_dt);
					stmt1.setString(4, ""+VMST_INV_TYPE_FLG.elementAt(i));
					stmt1.setString(5, inv_seq);
					stmt1.setString(6, contPtyCd);
					stmt1.setString(7, buStTin);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						own_cd=rset1.getString(1)==null?"":rset1.getString(1);
						countpty_cd=rset1.getString(2)==null?"":rset1.getString(2);
						String temp_agmtno=rset1.getString(3)==null?"0":rset1.getString(3);
						String temp_agmtrev=rset1.getString(4)==null?"0":rset1.getString(4);
						String temp_contno=rset1.getString(5)==null?"0":rset1.getString(5);
						String temp_contrev=rset1.getString(6)==null?"0":rset1.getString(6);
						String temp_cont_type=rset1.getString(7)==null?"V":rset1.getString(7);
						String temp_instrument_no=rset1.getString(21)==null?"":rset1.getString(21);
						String tmp_period_start_dt=rset1.getString(9)==null?"":rset1.getString(9);
						String tmp_period_end_dt=rset1.getString(10)==null?"":rset1.getString(10);
						if(contno.equals(""))
						{
							agmtno=temp_agmtno;
							agmtrev=temp_agmtrev;
							contno=temp_contno;
							contrev=temp_contrev;
							cont_type=temp_cont_type;
							instrument_no=temp_instrument_no;
							period_start_dt=tmp_period_start_dt;
							period_end_dt=tmp_period_end_dt;
						}
						else
						{
							agmtno+=", "+temp_agmtno;
							agmtrev+=", "+temp_agmtrev;
							contno+=", "+temp_contno;
							contrev+=", "+temp_contrev;
							cont_type+=", "+temp_cont_type;
							instrument_no+=", "+temp_instrument_no;
							period_start_dt+=", "+tmp_period_start_dt;
							period_end_dt+=", "+tmp_period_end_dt;
						}
						
						if(deal_no.equals(""))
						{
							deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, temp_agmtno, temp_agmtrev, temp_contno, temp_contrev, temp_cont_type, temp_instrument_no);
						}
						else
						{
							deal_no+=", "+utilBean.NewDealMappingId(own_cd, countpty_cd, temp_agmtno, temp_agmtrev, temp_contno, temp_contrev, temp_cont_type, temp_instrument_no);
						}
						
						plant_seq=rset1.getString(22)==null?"":rset1.getString(22);
						bu_plant_seq=rset1.getString(8)==null?"":rset1.getString(8);
						billing_cycle=rset1.getString(23)==null?"":rset1.getString(23);
						fin_year=rset1.getString(15)==null?"":rset1.getString(15);
						invoice_no=rset1.getString(11)==null?"":rset1.getString(11);
						invoice_seq=inv_seq;
						checked_flag=rset1.getString(12)==null?"":rset1.getString(12);
						authorized_flag=rset1.getString(25)==null?"":rset1.getString(25);
						approved_flag=rset1.getString(13)==null?"":rset1.getString(13);
						pdf_flg=rset1.getString(17)==null?"":rset1.getString(17);
						pdf_ori=rset1.getString(19)==null?"":rset1.getString(19);
						pdf_tri=rset1.getString(31)==null?"":rset1.getString(31);
						pdf_dup=rset1.getString(30)==null?"":rset1.getString(30);
						sap_approved_flag=rset1.getString(20)==null?"":rset1.getString(20);
						bu_state_tin=rset1.getString(18)==null?"":rset1.getString(18);
						
						invoice_dt=rset1.getString(24)==null?"":rset1.getString(24);
						invoice_type=rset1.getString(26)==null?"":rset1.getString(26);
						crdr_type=rset1.getString(27)==null?"":rset1.getString(27);
						ref_no=rset1.getString(28)==null?"":rset1.getString(28);
						criteria=rset1.getString(29)==null?"":rset1.getString(29);
						disp_criteria=criteria.replace("#", "<br>");
						
						String contRefNo="";
						String queryString2="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_TYPE,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,A.CONT_NO,A.CONT_REV,A.CONT_NAME,A.CONT_REF_NO,"
								+ "B.INSTRUMENT_NO,B.INSTRUMENT_TYPE,B.BUY_SELL,B.STATUS,B.QTY,B.QTY_UNIT,B.RATE,B.RATE_UNIT,"
								+ "B.PRODUCT_NM,B.CURVE_NM,B.PROJ_METHOD,TO_CHAR(B.CONT_DD_MM_YR,'DD/MM/YYYY'),"
								+ "TO_CHAR(B.PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(B.PRICE_END_DT,'DD/MM/YYYY'),B.CONV_FACTOR,TO_CHAR(A.TRADE_DT,'DD/MM/YYYY') "
								+ "FROM FMS_DERV_CONT_MST A,FMS_DERV_INSTRUMENT_MST B "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
								+ "AND B.STATUS='Y' AND B.INSTRUMENT_NO=? "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
								+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO";
					  	stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, countpty_cd);
						stmt2.setString(3, temp_agmtno);
						stmt2.setString(4, temp_contno);
						stmt2.setString(5, temp_cont_type);
						stmt2.setString(6, temp_instrument_no);
						rset2=stmt2.executeQuery();
						while(rset2.next())
						{
							contRefNo=rset2.getString(10)==null?"":rset2.getString(10);
						}
						rset2.close();
						stmt2.close();
						
						if(contRef.equals(""))
						{
							contRef=contRefNo;
						}
						else
						{
							contRef=contRef+", "+contRefNo;
						}
					}
					rset1.close();
					stmt1.close();
					
					VCOUNTERPARTY_CD.add(countpty_cd);
					VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
					VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
					VAGMT_NO.add(agmtno);
					VAGMT_REV.add(agmtrev);
					VCONT_NO.add(contno);
					VCONT_REV.add(contrev);
					VCONT_TYPE.add(cont_type);
					VINSTRUMENT_NO.add(instrument_no);
					VDEAL_MAPPING.add(deal_no);
					VPLANT_SEQ.add(plant_seq);
					VPLANT_ABBR.add(""+utilBean.getCounterpartyPlantABBR(conn,countpty_cd, comp_cd, plant_seq, "T"));
					VBU_PLANT_SEQ.add(bu_plant_seq);
					VBU_PLANT_ABBR.add(""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B"));
					VBILLING_FREQ.add(billing_cycle);
					VFIN_YEAR.add(fin_year);
					VCREDIT_DEBIT_NO.add(invoice_no);
					VINVOICE_SEQ.add(invoice_seq);
					VINV_CHECKED_FLG.add(checked_flag);
					VINV_AUTHORIZED_FLG.add(authorized_flag);
					VINV_APPROVED_FLG.add(approved_flag);
					VINV_PDF_FLG.add(pdf_flg);
					//VINV_PDF_TYPE.add(pdf_flg);
					VINV_SAP_APPROVAL_FLAG.add(sap_approved_flag);
					VBU_STATE_TIN.add(bu_state_tin);
					VINVOICE_EXIST.add("Y");
					VIS_IRN_GENERATED.add("Y");
					VPERIOD_START_DT.add(period_start_dt);
					VPERIOD_END_DT.add(period_end_dt);
					VINVOICE_DT.add(invoice_dt);
					VINVOICE_TYPE.add(invoice_type);
					VCONT_REF.add(contRef);
					VREF_NO.add(ref_no);
					VINV_FLAG.add(crdr_type);
					VINV_FLAG_NM.add(crdr_type.equals("CR")?"Credit Note":crdr_type.equals("DR")?"Debit Note":"");
					VCRDR_CRITERIA.add(criteria);
					VDISP_CRDR_CRITERIA.add(disp_criteria);
					
					
					int upload_count=0;
					queryString6="SELECT COUNT(*) "
							+ "FROM FMS_DERV_INV_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
		 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
		 	        		+ "AND PDF_TYPE=? AND INV_TYPE=?";
					stmt6=conn.prepareStatement(queryString6);
					stmt6.setString(1, comp_cd);
					stmt6.setString(2, bu_state_tin);
					stmt6.setString(3, inv_seq);
					stmt6.setString(4, fin_year);
					stmt6.setString(5, "P");
					stmt6.setString(6, ""+VMST_INV_TYPE_FLG.elementAt(i));
					rset6=stmt6.executeQuery();
					if(rset6.next())
					{
						upload_count=rset6.getInt(1);
					}
					rset6.close();
					stmt6.close();
					VFILE_UPLOAD_COUNT.add(upload_count);
					
					if(print_pdf_type.equals("O") && !pdf_ori.equals(""))
					{
						VINV_PDF_TYPE.add(print_pdf_type);
					}
					else if(print_pdf_type.equals("D") && !pdf_dup.equals(""))
					{
						VINV_PDF_TYPE.add(print_pdf_type);
					}
					else if(print_pdf_type.equals("T") && !pdf_tri.equals(""))
					{
						VINV_PDF_TYPE.add(print_pdf_type);
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
						VINV_PDF_TYPE.add(allPdfType);
					}
					else
					{
						VINV_PDF_TYPE.add("");
					}
					
					if(view_pdf_type.equals("All"))
					{
						queryString6="SELECT COUNT(*) "
								+ "FROM FMS_DERV_INV_FILE_DTL "
								+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
			 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
			 	        		+ "AND PDF_TYPE IN (?,?,?) AND INV_TYPE=?";
						stmt6=conn.prepareStatement(queryString6);
						stmt6.setString(1, own_cd);
						stmt6.setString(2, bu_state_tin);
						stmt6.setString(3, inv_seq);
						stmt6.setString(4, fin_year);
						stmt6.setString(5, "O");
						stmt6.setString(6, "D");
						stmt6.setString(7, "T");
						stmt6.setString(8, invoice_type);
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
								+ "FROM FMS_DERV_INV_FILE_DTL "
								+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
			 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
			 	        		+ "AND PDF_TYPE=? AND INV_TYPE=?";
						stmt6=conn.prepareStatement(queryString6);
						stmt6.setString(1, own_cd);
						stmt6.setString(2, bu_state_tin);
						stmt6.setString(3, inv_seq);
						stmt6.setString(4, fin_year);
						stmt6.setString(5, view_pdf_type);
						stmt6.setString(6, invoice_type);
						rset6=stmt6.executeQuery();
						if(rset6.next())
						{
							String pdf_signed=rset6.getString(2)==null?"":rset6.getString(2);
							
							VPDF_FILE_NAME.add(rset6.getString(1)==null?"":rset6.getString(1));
							if(VMST_INV_TYPE_FLG.elementAt(i).equals("R"))
							{
								VPDF_FILE_PATH.add(CommonVariable.derv_remittance_path);
							}
							else
							{
								if(pdf_signed.equals("Y"))
								{
									VPDF_FILE_PATH.add(CommonVariable.signed_derv_inv_path);
								}
								else
								{
									VPDF_FILE_PATH.add(CommonVariable.derv_inv_path);
								}
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
								+ "FROM FMS_DERV_INV_FILE_DTL "
								+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
			 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
			 	        		+ "AND PDF_SIGNED=? AND PDF_TYPE IN (?,?,?) AND INV_TYPE=?";
						stmt6=conn.prepareStatement(queryString6);
						stmt6.setString(1, own_cd);
						stmt6.setString(2, bu_state_tin);
						stmt6.setString(3, inv_seq);
						stmt6.setString(4, fin_year);
						stmt6.setString(5, "Y");
						stmt6.setString(6, "O");
						stmt6.setString(7, "D");
						stmt6.setString(8, "T");
						stmt6.setString(9, invoice_type);
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
								+ "FROM FMS_DERV_INV_FILE_DTL "
								+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
			 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
			 	        		+ "AND PDF_TYPE=? AND INV_TYPE=?";
						stmt6=conn.prepareStatement(queryString6);
						stmt6.setString(1, own_cd);
						stmt6.setString(2, bu_state_tin);
						stmt6.setString(3, inv_seq);
						stmt6.setString(4, fin_year);
						stmt6.setString(5, view_pdf_type);
						stmt6.setString(6, invoice_type);
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
					
					int re_print_count=isRePrintPDFRequested(comp_cd,bu_state_tin,inv_seq,fin_year,"DERV","REPRINT_PDF","A",invoice_type);
					VRE_PRINT_PDF.add(re_print_count>0?"Y":"N");
				}
				rset.close();
				stmt.close();
				
				VINDEX.add(index);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getDervInvoiceDetailCrDr()
	{
		String function_nm="getDervInvoiceDetailCrDr()";
		try
		{
			queryString="SELECT INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,BUY_PRICE,BUY_PRICE_UNIT,BUY_AMT,"
					+ "INVOICE_RAISED_IN,INVOICE_AMT,NET_PAYABLE_AMT,"
					+ "REMARK_1,REMARK_2,FINANCIAL_YEAR,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),INVOICE_REF_NO,CHECKED_FLAG,"
					+ "INSTRUMENT_NO,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CRITERIA,INV_FLAG,REF_NO,INV_TYPE,INVOICE_ID_SEQ,INVOICE_NO,APPROVED_FLAG "
					+ "FROM FMS_DERV_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND REF_NO=? AND INV_FLAG IN ('CR','DR')";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, sel_inv_no);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				crdr_seq=rset.getString(1)==null?"":rset.getString(1);
				crdr_dt=rset.getString(3)==null?"":rset.getString(3);
				crdr_due_dt=rset.getString(4)==null?"":rset.getString(4);
				double alloc_qty=rset.getDouble(5);
				String sell_price=rset.getString(6)==null?"":rset.getString(6);
				String sell_price_unit=rset.getString(7)==null?"":rset.getString(7);
				String sell_amt=nf.format(rset.getDouble(8));
				String buy_price=rset.getString(9)==null?"":rset.getString(9);
				String buy_price_unit=rset.getString(10)==null?"":rset.getString(10);
				String buy_amt=nf.format(rset.getDouble(11));
				String inv_raised_in=rset.getString(12)==null?"":rset.getString(12);
				String invoice_amt=rset.getString(13)==null?"":rset.getString(13);
				String net_payable_amt=rset.getString(14)==null?"":rset.getString(14);
				crdr_remark=rset.getString(15)==null?"":rset.getString(15);
				fy_year=rset.getString(17)==null?"":rset.getString(17);
				String tmp_period_start_dt=rset.getString(18)==null?"":rset.getString(18);
				String tmp_period_end_dt=rset.getString(19)==null?"":rset.getString(19);
				
				String instrument_no = rset.getString(22)==null?"":rset.getString(22);
				String agmt_no = rset.getString(23)==null?"":rset.getString(23);
				String agmt_rev_no = rset.getString(24)==null?"":rset.getString(24);
				String cont_no = rset.getString(25)==null?"":rset.getString(25);
				String cont_rev_no = rset.getString(26)==null?"":rset.getString(26);
				String cont_type = rset.getString(27)==null?"":rset.getString(27);
				criteri_formula = rset.getString(28)==null?"":rset.getString(28);
				crdr_type = rset.getString(29)==null?"":rset.getString(29);
				sel_inv_no = rset.getString(30)==null?"":rset.getString(30);
				if(!invoice_amt.equals(""))
				{
					temp_tot_inv_amt+=Double.parseDouble(invoice_amt);
				}
				invoice_type = rset.getString(31)==null?"":rset.getString(31);
				invoice_id_seq=rset.getString(32)==null?"":rset.getString(32);
				invoice_no=rset.getString(33)==null?"":rset.getString(33);
				inv_ref=rset.getString(20)==null?"":rset.getString(20);
				String deal_no=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, cont_type, instrument_no);
				
				if(operation.equals("CHECK")) 
				{
					activity_value=rset.getString(21)==null?"":rset.getString(21);
				}
				else if(operation.equals("APPROVE")) 
				{
					activity_value=rset.getString(34)==null?"":rset.getString(34);
				}
				
				VAGMT_TYPE.add(agmt_type);
				VAGMT_NO.add(agmt_no);
				VAGMT_REV.add(agmt_rev_no);
				VCONT_TYPE.add(cont_type);
				VCONT_NO.add(cont_no);
				VCONT_REV.add(cont_rev_no);
				VINSTRUMENT_NO.add(instrument_no);
				VBOOKED_MMBTU.add(nf.format(alloc_qty));
				VPERIOD_START_DT.add(tmp_period_start_dt);
				VPERIOD_END_DT.add(tmp_period_end_dt);
			  	
			  	int cnt2=0;
			  	String cont_ref="";
			  	String trade_dt="";
			  	String instrument_volume_unit="";
			  	String buySell="";
			  	String instrument_type="";
			  	String curve_nm="";
			  	String instru_duration="";
			  	String instru_qty="";
			  	String fixed_price="";
			  	
			  	queryString2="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_TYPE,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,A.CONT_NO,A.CONT_REV,A.CONT_NAME,A.CONT_REF_NO,"
						+ "B.INSTRUMENT_NO,B.INSTRUMENT_TYPE,B.BUY_SELL,B.STATUS,B.QTY,B.QTY_UNIT,B.RATE,B.RATE_UNIT,"
						+ "B.PRODUCT_NM,B.CURVE_NM,B.PROJ_METHOD,TO_CHAR(B.CONT_DD_MM_YR,'DD/MM/YYYY'),"
						+ "TO_CHAR(B.PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(B.PRICE_END_DT,'DD/MM/YYYY'),B.CONV_FACTOR,TO_CHAR(A.TRADE_DT,'DD/MM/YYYY') "
						+ "FROM FMS_DERV_CONT_MST A,FMS_DERV_INSTRUMENT_MST B "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
						+ "AND B.STATUS='Y' AND B.INSTRUMENT_NO=? "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO";
			  	stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(++cnt2, comp_cd);
				stmt2.setString(++cnt2, counterparty_cd);
				stmt2.setString(++cnt2, agmt_no);
				stmt2.setString(++cnt2, cont_no);
				stmt2.setString(++cnt2, cont_type);
				//stmt2.setString(++cnt2, buy_sell);
				stmt2.setString(++cnt2, instrument_no);
				rset2=stmt2.executeQuery();
				while(rset2.next())
				{
					instru_qty = rset2.getString(15)==null?"":rset2.getString(15);
					fixed_price = rset2.getString(17)==null?"":rset2.getString(17);
					cont_ref=rset2.getString(10)==null?"":rset2.getString(10);
					trade_dt=rset2.getString(26)==null?"":rset2.getString(26);
					String instrument_volume_unit_cd = rset2.getString(16)==null?"":rset2.getString(16);
					instrument_volume_unit = utilBean.getEnergyUnitNm(conn, instrument_volume_unit_cd);
					buySell = rset2.getString(13)==null?"":rset2.getString(13);
					instrument_type = rset2.getString(12)==null?"":rset2.getString(12);
					curve_nm = rset2.getString(20)==null?"":rset2.getString(20);
					String start_dt = rset2.getString(23)==null?"":rset2.getString(23);
					String end_dt = rset2.getString(24)==null?"":rset2.getString(24);
					instru_duration = start_dt+"-"+end_dt;
				}
				rset2.close();
				stmt2.close();
				VBUY_SELL.add(buySell);
				VTRADE_DT.add(trade_dt);
				VDEAL_MAPPING.add(deal_no);
				VCONT_REF.add(cont_ref);
				VINSTRUMENT_TYPE.add(instrument_type);
				VCURVE_NM.add(curve_nm);
				VINSTRUMENT_DURATION.add(instru_duration);
				VQTY_UNIT.add(instrument_volume_unit);
				
				if(!sell_price.equals(""))
				{
					if(buySell.equals("BUY"))
					{
						BigDecimal float_val=new BigDecimal(sell_price);
						VSELL_RATE.add(df3.format(float_val));
					}
					else
					{
						VSELL_RATE.add(nf2.format(Double.parseDouble(sell_price)));
					}
				}
				else
				{
					VSELL_RATE.add("");
				}
			  	VSELL_AMT.add(sell_amt);
			  	
			  	if(!buy_price.equals(""))
			  	{
				  	if(buySell.equals("SELL"))
					{
				  		BigDecimal float_val=new BigDecimal(buy_price);
				  		VBUY_RATE.add(df3.format(float_val));
					}
				  	else
				  	{
				  		VBUY_RATE.add(nf2.format(Double.parseDouble(buy_price)));
				  	}
			  	}
			  	else
			  	{
			  		VBUY_RATE.add("");
			  	}
			  	
			  	VBUY_AMT.add(buy_amt);
			  	if(!invoice_amt.equals(""))
			  	{
			  		VINVOICE_AMT.add(nf.format(Double.parseDouble(invoice_amt)));
			  	}
			  	else
			  	{
			  		VINVOICE_AMT.add("");
			  	}
			  	
			  	IS_CRDR_SUBMITTED.add("Y");
			}
			
			HashMap bu_plant_detail=utilBean.getCounterpartyPlantDetail(conn,comp_cd, "B", comp_cd, bu_plant_seq);
			bu_plantAddress=""+bu_plant_detail.get("plant_address");
			bu_plantCity=""+bu_plant_detail.get("plant_city");
			bu_plantState=""+bu_plant_detail.get("plant_state");
			bu_plantPin=""+bu_plant_detail.get("plant_pin");
			bu_plantNm=""+bu_plant_detail.get("plant_name");
			
			HashMap plant_detail=utilBean.getCounterpartyPlantDetail(conn,comp_cd, "T", counterparty_cd, plant_seq);
			plantAddress=""+plant_detail.get("plant_address");
			plantCity=""+plant_detail.get("plant_city");
			plantState=""+plant_detail.get("plant_state");
			plantPin=""+plant_detail.get("plant_pin");
			
			attach_total_inv_amt = nf.format(temp_tot_inv_amt);
			if(temp_tot_inv_amt<0)
			{
				temp_tot_inv_amt=temp_tot_inv_amt*(-1);
			}
			total_inv_amt=nf.format(temp_tot_inv_amt);
			
			tax_info=getCounterpartyPlantTaxInfo(comp_cd, "T", counterparty_cd, plant_seq);
			tax_info=tax_info.replaceAll("\n", "<br>");
			
			bu_tax_info=getCounterpartyPlantTaxInfo(comp_cd, "B", comp_cd, bu_plant_seq);
			bu_tax_info=bu_tax_info.replaceAll("\n", "<br>");
			
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
					else if(split_criteri_formula[i].toString().equals("FIXEDPRICE"))
					{
						reason+="Fixed Price";
					}
					else if(split_criteri_formula[i].toString().equals("FLOATPRICE"))
					{
						reason+="Float Price";
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
			if(!fy_year.equals(""))
			{
				String[] temp = fy_year.split("-");
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
			
			contType="'V'";
			invSeries="V"+crdr_type;
			
			if(!invoice_prefix.equals(""))
			{
				int no_inv_no=10;
				for(int i=1;i<=no_inv_no;i++)
				{
					String invoice_id_seq=""+i;
					int count=0;
					queryString="SELECT COUNT(*) "
							+ "FROM FMS_DERV_INVOICE_MST "
							+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
							+ "AND BU_STATE_TIN=? AND INVOICE_ID_SEQ=? AND CONTRACT_TYPE IN ("+contType+") AND INV_TYPE='I' AND INV_FLAG=?";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, fy_year);
					stmt.setString(3, bu_state_tin);
					stmt.setString(4, invoice_id_seq);
					stmt.setString(5, crdr_type);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						count += rset.getInt(1);
					}
					rset.close();
					stmt.close();
					
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
	
	public void getCrDrRemNumber()
	{
		String function_nm="getCrDrRemNumber()";
		try
		{
			String invoice_prefix=utilBean.getInvoicePrefix(conn,comp_cd);
			
			String fin_yr="";
			if(!fy_year.equals(""))
			{
				String[] temp = fy_year.split("-");
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
			
			String contType="V";
			if(!invoice_prefix.equals(""))
			{
				String invoice_id_seq="";
				String queryString1="SELECT MAX(INVOICE_ID_SEQ) "
						+ "FROM FMS_DERV_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
						+ "AND CONTRACT_TYPE IN ('V') AND INV_TYPE='R' AND INV_FLAG=? ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, fy_year);
				stmt1.setString(3, crdr_type);
				//stmt1.setString(4, sel_inv_no);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					invoice_id_seq = ""+(rset1.getInt(1)+1);
				}
				rset1.close();
				stmt1.close();
					
				int count=0;
				queryString="SELECT COUNT(*) "
						+ "FROM FMS_DERV_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
						+ "AND INVOICE_ID_SEQ=? AND CONTRACT_TYPE IN ('V') AND INV_TYPE='R' AND INV_FLAG=? ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, fy_year);
				stmt.setString(3, invoice_id_seq);
				stmt.setString(4, crdr_type);
				//stmt.setString(5, sel_inv_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					count += rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
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
					invoice_no=invoice_prefix+""+contType+"S"+crdr_type+utilBean.PrePaddingZero(invoice_id_seq, 4)+"/"+fin_yr;
					
					VINVOICE_ID_SEQ.add(invoice_id_seq);
					VINVOICE_NO.add(invoice_no);
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
	
	public void getNewInvInstrumentDtl()
	{
		String function_nm="getNewInvInstrumentDtl()";
		try
		{
			for(int i=0; i<VDEAL_MAPPING.size(); i++)
			{
				queryString="SELECT INVOICE_AMT,BUY_AMT,BUY_PRICE,SALE_AMT,SALE_PRICE,ALLOC_QTY,"
						+ "INSTRUMENT_NO,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE "
						+ "FROM FMS_DERV_INV_CRDR_REF "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND FINANCIAL_YEAR=? AND BU_STATE_TIN=? "
						+ "AND INV_TYPE=? "
						+ "AND INVOICE_SEQ=? AND CONT_NO=? AND AGMT_NO=? AND CONTRACT_TYPE=? AND INSTRUMENT_NO=?";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, fy_year);
				stmt.setString(4, bu_state_tin);
				stmt.setString(5, invoice_type);
				stmt.setString(6, crdr_seq);
				stmt.setString(7, ""+VCONT_NO.elementAt(i));
				stmt.setString(8, ""+VAGMT_NO.elementAt(i));
				stmt.setString(9, ""+VCONT_TYPE.elementAt(i));
				stmt.setString(10, ""+VINSTRUMENT_NO.elementAt(i));
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String new_tot_amt = rset.getString(1)==null?"":rset.getString(1);
					String new_buy_amt = nf.format(rset.getDouble(2));
					String new_buy_price = rset.getString(3)==null?"":rset.getString(3);
					String new_sell_amt = nf.format(rset.getDouble(4));
					String new_sell_price = rset.getString(5)==null?"":rset.getString(5);
					String new_alloc_qty = nf.format(rset.getDouble(6));
					String instrument_no = rset.getString(7)==null?"":rset.getString(7);
					String agmt_no = rset.getString(8)==null?"":rset.getString(8);
					String agmt_rev_no = rset.getString(9)==null?"":rset.getString(9);
					String cont_no = rset.getString(10)==null?"":rset.getString(10);
					String cont_rev_no = rset.getString(11)==null?"":rset.getString(11);
					String cont_type = rset.getString(12)==null?"":rset.getString(12);
					
					int cnt2=0;
					String buySell="";
					queryString2="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_TYPE,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,A.CONT_NO,A.CONT_REV,A.CONT_NAME,A.CONT_REF_NO,"
							+ "B.INSTRUMENT_NO,B.INSTRUMENT_TYPE,B.BUY_SELL,B.STATUS,B.QTY,B.QTY_UNIT,B.RATE,B.RATE_UNIT,"
							+ "B.PRODUCT_NM,B.CURVE_NM,B.PROJ_METHOD,TO_CHAR(B.CONT_DD_MM_YR,'DD/MM/YYYY'),"
							+ "TO_CHAR(B.PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(B.PRICE_END_DT,'DD/MM/YYYY'),B.CONV_FACTOR,TO_CHAR(A.TRADE_DT,'DD/MM/YYYY') "
							+ "FROM FMS_DERV_CONT_MST A,FMS_DERV_INSTRUMENT_MST B "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
							+ "AND B.STATUS='Y' AND B.INSTRUMENT_NO=? "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO";
				  	stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(++cnt2, comp_cd);
					stmt2.setString(++cnt2, counterparty_cd);
					stmt2.setString(++cnt2, agmt_no);
					stmt2.setString(++cnt2, cont_no);
					stmt2.setString(++cnt2, cont_type);
					stmt2.setString(++cnt2, instrument_no);
					rset2=stmt2.executeQuery();
					while(rset2.next())
					{
						buySell = rset2.getString(13)==null?"":rset2.getString(13);
					}
					rset2.close();
					stmt2.close();
					
					VNEW_QTY.add(new_alloc_qty);
					
					if(buySell.equals("BUY"))
					{
						VNEW_FIXED_PRICE.add(new_buy_price);
					}
					else if(buySell.equals("SELL"))
					{
						VNEW_FIXED_PRICE.add(new_sell_price);
					}
					
					if(!new_sell_price.equals(""))
					{
						if(buySell.equals("BUY"))
						{
							BigDecimal float_val=new BigDecimal(new_sell_price);
							VNEW_SELL_RATE.add(df3.format(float_val));
						}
						else
						{
							VNEW_SELL_RATE.add(nf2.format(Double.parseDouble(new_sell_price)));
						}
					}
					else
					{
						VNEW_SELL_RATE.add("");
					}
				  	VNEW_SELL_AMT.add(new_sell_amt);
				  	
				  	if(!new_buy_price.equals(""))
				  	{
					  	if(buySell.equals("SELL"))
						{
					  		BigDecimal float_val=new BigDecimal(new_buy_price);
					  		VNEW_BUY_RATE.add(df3.format(float_val));
						}
					  	else
					  	{
					  		VNEW_BUY_RATE.add(nf2.format(Double.parseDouble(new_buy_price)));
					  	}
				  	}
				  	else
				  	{
				  		VNEW_BUY_RATE.add("");
				  	}
				  	
				  	VNEW_BUY_AMT.add(new_buy_amt);
				  	
				  	if(!new_tot_amt.equals(""))
				  	{
				  		VNEW_TOTAL_AMT.add(nf.format(Double.parseDouble(new_tot_amt)));
				  	}
				  	else
				  	{
				  		VNEW_TOTAL_AMT.add("");
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
	
	public void generateDervCrDrXML()
	{
		String function_nm="generateDervCrDrXML()";
		
		try
		{
			String sysdate=dateUtil.getSysdate();
			String sysdateWithTime=dateUtil.getSysdateWithTime24hr();
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
			String account=utilBean.getCounterpartySAPcode(conn,counterparty_cd);
			String taxCode="";
			String monthNm="";
			String paymentDueDt="";
			
			String netPayable="";
			String cont_no="";
			String agmt_no="";
			String instrument_no="";
			String qty="";
			String invoiceAmt="";
			
			String fms_MessageId="";
			
			String plant_seq="";
		    String plantAddress="";
		    String plantCity="";
		    String plantState="";
		    String plantPin="";
		    String plantNm="";
		    
		    String cash_flow="";
		    
		    String documentDate="";
		    String postingDate=xml_sysdate; //AS DISCUSSED WITH VIJAY AND DIVYA ON 20250811 IN WORKSHOP CHENNAI, POSTING DATE WILL BE XML APPROVAL DATE
			String invoice_typ="";
			String invDt="";
			String contract_typ="";
			double temp_net_payable=0;
			String assignmentNo="";
			Vector VNET_PAYABLE = new Vector();
			Vector VDEAL_NO = new Vector();
			Vector VCONT_REF = new Vector();
			
					
			queryString="SELECT TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),BU_UNIT,INVOICE_NO,NET_PAYABLE_AMT,"
					+ "CONT_NO,ALLOC_QTY,"
					+ "INVOICE_RAISED_IN,TO_CHAR(DUE_DT,'DD/MM/YYYY'),INVOICE_AMT,PLANT_SEQ,"
					+ "AGMT_NO,"
					+ "TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(APPROVED_DT,'DD/MM/YYYY'),INSTRUMENT_NO,INV_TYPE,CONTRACT_TYPE "
					+ "FROM FMS_DERV_INVOICE_MST A "
					+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
					+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? AND INV_TYPE=? AND INV_FLAG=? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, fy_year);
			stmt.setString(3, invoice_seq);
			stmt.setString(4, bu_state_tin);
			stmt.setString(5, inv_type);
			stmt.setString(6, crdr_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String period_end=rset.getString(1)==null?"":rset.getString(1);
				if(!period_end.equals(""))
				{
					String[] temp_split=period_end.split("/");
					productionPeriodMonth=temp_split[1];
					productionPeriodYear=temp_split[2];		
				}
				monthNm=""+dateUtil.getShortMonthName(period_end);
				
				String buUnit=rset.getString(2)==null?"":rset.getString(2);
				
				String buStateNm="";
				String buAbbr="";
				queryString1 = "SELECT PLANT_STATE,PLANT_ABBR "
						+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
						+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND COMPANY_CD=? AND SEQ_NO=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) ";
				stmt1 = conn.prepareStatement(queryString1);
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
				
				cont_no=rset.getString(5)==null?"":rset.getString(5);
				qty=rset.getString(6)==null?"":nf.format(rset.getDouble(6));
				String inv_raised_in=rset.getString(7)==null?"":rset.getString(7);
				netPayable=rset.getString(4)==null?"":nf.format(rset.getDouble(4));
				VNET_PAYABLE.add(netPayable);
				temp_net_payable+=rset.getDouble(4);
				paymentDueDt=rset.getString(8)==null?"":rset.getString(8);
				if(!paymentDueDt.equals(""))
				{
					String splitPayDt[]=paymentDueDt.split("/");
					paymentDueDt=splitPayDt[2]+""+splitPayDt[1]+""+splitPayDt[0];
				}
				
				invoiceAmt=rset.getString(9)==null?"":nf.format(rset.getDouble(9));
				plant_seq=rset.getString(10)==null?"":rset.getString(10);
				agmt_no=rset.getString(11)==null?"":rset.getString(11);
				
				cash_flow="Commodity";
				
				String invoiceDt=rset.getString(12)==null?"":rset.getString(12);
				invDt=invoiceDt;
				if(!invoiceDt.equals(""))
				{
					String[] temp_split=invoiceDt.split("/");
					documentDate=temp_split[2]+""+temp_split[1]+""+temp_split[0];
				}
				
				/*String approve_dt=rset.getString(13)==null?"":rset.getString(13);
				if(!approve_dt.equals(""))
				{
					String[] temp_split=approve_dt.split("/");
					postingDate=temp_split[2]+""+temp_split[1]+""+temp_split[0];
				}*/
				
				instrument_no=rset.getString(14)==null?"":rset.getString(14);
				invoice_typ=rset.getString(15)==null?"":rset.getString(15);
				contract_typ=rset.getString(16)==null?"":rset.getString(16);
				String cont_ref="";
				String queryString2="SELECT A.CONT_REF_NO "
						+ "FROM FMS_DERV_CONT_MST A,FMS_DERV_INSTRUMENT_MST B "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.CONT_NO=? AND B.INSTRUMENT_NO=? "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, counterparty_cd);
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
				
				assignmentNo=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, "", cont_no, "", contract_typ, instrument_no);
				VDEAL_NO.add(assignmentNo);
				VCONT_REF.add(cont_ref);
				
				if(crdr_type.equals("CR"))
				{
					cash_flow="Credit Note";
				}
				else if(crdr_type.equals("DR"))
				{
					cash_flow="Debit Note";
				}
			}
			rset.close();
			stmt.close();
			
			HashMap plant_detail=utilBean.getCounterpartyPlantDetail(conn,comp_cd, "T", counterparty_cd, plant_seq);
            plantAddress=""+plant_detail.get("plant_address");
			plantCity=""+plant_detail.get("plant_city");
			plantState=""+plant_detail.get("plant_state");
			plantPin=""+plant_detail.get("plant_pin");
			plantNm=""+plant_detail.get("plant_name");
			
			
			String material_code="1168001";
	    	
			fms_MessageId=refNum.replaceAll("/", "-");
	    	
	    	String UserID = ""+utilBean.getUserName(conn,emp_cd);
	    	String businessAreaCode=utilBean.getBusinessAreaSAPcode(conn, comp_cd, "T", contract_typ, invDt);
		    
			DocumentBuilderFactory docFactory = xmlUtil.dcoumentBuilderFactory();
		    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		    Document doc = docBuilder.newDocument();
		    
		    String arAp="";
		    if(inv_type.equals("R"))
	    	{
		    	arAp="Ap";
	    	}
		    else
		    {
		    	arAp="Ar";
		    }
		    //root fmsng
		    Element fmsng = doc.createElement("EmsSAP"+arAp+"Message");
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
		    if(inv_type.equals("R"))
	    	{
		    	DocumentType.appendChild(doc.createTextNode("X1"));
	    	}
		    else
		    {
		    	 DocumentType.appendChild(doc.createTextNode("X2"));
		    }
		    DocumentDate.appendChild(doc.createTextNode(documentDate));
		    PostingDate.appendChild(doc.createTextNode(postingDate));
		    AccountingPeriodMonth.appendChild(doc.createTextNode(accountingPeriodMonth));
		    AccountingPeriodYear.appendChild(doc.createTextNode(accountingPeriodYear));
		    InternalLegalEntity.appendChild(doc.createTextNode(utilBean.getCompanySAPcode(conn, comp_cd))); //NEED TO CHEACK FOR COMPANY BASE LOGIN JD-20230728
		    DocHeaderText.appendChild(doc.createTextNode(docHeaderText));
		    RefNum.appendChild(doc.createTextNode(refNum));
		    EmsRefNum.appendChild(doc.createTextNode(refNum));
		    Currency.appendChild(doc.createTextNode("USD")); //NO NEED TO SEND OTHER TYPE OF CURRENCY - JD
		    
		    AddressLine1.appendChild(doc.createTextNode(plantNm));
		    AddressLine2.appendChild(doc.createTextNode(plantAddress));
		    AddressLine3.appendChild(doc.createTextNode(plantState));
		    AddressLine4.appendChild(doc.createTextNode(plantCity));
		    AddressLine5.appendChild(doc.createTextNode(plantPin));
		    
		    UserName.appendChild(doc.createTextNode(UserID));
		    
		    int i=0;
		    if(!netPayable.equals(""))
		    {
		    	String sign = "";
		    	String pk = "01";
		    	if(crdr_type.equals("CR"))
		    	{
		    		pk="11";
		    	}
		    	
		    	if(inv_type.equals("R"))
		    	{
		    		if(crdr_type.equals("CR"))
			    	{
		    			pk="21";
			    	}
		    		else if(crdr_type.equals("DR"))
			    	{
		    			pk="31";
			    	}
		    	}
		    	
		    	i+=1;
		    	Element InvoiceDetail = doc.createElement("InvoiceDetail");
		    	Invoice.appendChild(InvoiceDetail);
		    	
		    	String vendor_cust="";
		    	if(inv_type.equals("R"))
		    	{
		    		vendor_cust="VendorId";
		    	}
		    	else if(inv_type.equals("I"))
		    	{
		    		vendor_cust="CustomerId";
		    	}
		    	Element VendorId  = doc.createElement(vendor_cust);
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
		    	CurrencyAmount.appendChild(doc.createTextNode(sign+""+nf.format(temp_net_payable)));
		    	TaxCode.appendChild(doc.createTextNode(taxCode));
		    	//BusinessArea.appendChild(doc.createTextNode(utilBean.getCompanySAPcode(conn, comp_cd))); //NEED TO CHEACK FOR COMPANY BASE LOGIN JD-20230728
		    	BusinessArea.appendChild(doc.createTextNode(businessAreaCode));
		    	Volume.appendChild(doc.createTextNode(qty));
		    	VolumeUnit.appendChild(doc.createTextNode("MMB"));
		    	
		    	ItemText.appendChild(doc.createTextNode(refNum));

		    	//ReferenceKey1.appendChild(doc.createTextNode(counterparty_abbr+"-"+contract_type+cont_no));
		    	ReferenceKey1.appendChild(doc.createTextNode(assignmentNo));
		    	ReferenceKey2.appendChild(doc.createTextNode(UserID));
		    	
		    	ProductionPeriod.appendChild(doc.createTextNode(productionPeriodYear+""+productionPeriodMonth));
		    	AssignmentNumber.appendChild(doc.createTextNode(refNum));
		    	PaymentTerms.appendChild(doc.createTextNode("ZB00")); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN
		    	PaymentDueDate.appendChild(doc.createTextNode(paymentDueDt)); //AS PER SUNIDHI MAIL 16/08/2023
		    	
		    	Material.appendChild(doc.createTextNode(utilBean.PrePaddingZero(material_code, 18))); //it was not added erlier
		    }
		    for(int k=0; k<VNET_PAYABLE.size(); k++)
		    {
			    if(!VNET_PAYABLE.elementAt(k).equals(""))
			    {
			    	String sign = "-";
			    	String pk = "50";
			    	String net_payable=""+VNET_PAYABLE.elementAt(k);
			    	assignmentNo = ""+VDEAL_NO.elementAt(k);
			    	String cont_ref = ""+VCONT_REF.elementAt(k);
			    	if(Double.parseDouble(net_payable) < 0)
			    	{
			    		pk = "40";
			    		sign = "";
			    		net_payable = net_payable.replace("-", "");
			    	}
			    	i+=1;
			    	Element InvoiceDetail = doc.createElement("InvoiceDetail");
			    	Invoice.appendChild(InvoiceDetail);
			    	String vendor_cust="";
			    	if(inv_type.equals("R"))
			    	{
			    		vendor_cust="VendorId";
			    	}
			    	else if(inv_type.equals("I"))
			    	{
			    		vendor_cust="CustomerId";
			    	}
			    	Element VendorId  = doc.createElement(vendor_cust);
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
				    
				    InvoiceDetail.appendChild(VendorId);
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
				    
				    VendorId.appendChild(doc.createTextNode(account));
			    	LineSeqNo.appendChild(doc.createTextNode(""+i));
			    	PostingKey.appendChild(doc.createTextNode(pk));
			    	
			    	String countpty_category=""+utilBean.getCounterpartyCategory(conn,counterparty_cd);
			    	
			    	String tempAccount="";
			    	String itemText="";
			    		tempAccount="6841000";
			    		itemText="Swaps IG";
			    	Account.appendChild(doc.createTextNode(utilBean.PrePaddingZero(tempAccount, 10))); //IG OR NG
			    	CurrencyAmount.appendChild(doc.createTextNode(sign+""+net_payable));
			    	TaxCode.appendChild(doc.createTextNode(taxCode));
			    	BusinessArea.appendChild(doc.createTextNode(businessAreaCode));
			    	
			    	ItemText.appendChild(doc.createTextNode(itemText+" "+monthNm+" "+productionPeriodYear+" ("+cont_ref+")"));
			    	
			    	//CurrencyAmount.appendChild(doc.createTextNode(sign+""+net_payable));
			    	//TaxCode.appendChild(doc.createTextNode(taxCode));
			    	Volume.appendChild(doc.createTextNode(qty));
			    	VolumeUnit.appendChild(doc.createTextNode("MMB"));
			    	
			    	ReferenceKey1.appendChild(doc.createTextNode(assignmentNo));
			    	ReferenceKey2.appendChild(doc.createTextNode(UserID));
			    	
			    	ProductionPeriod.appendChild(doc.createTextNode(productionPeriodYear+""+productionPeriodMonth));
			    	AssignmentNumber.appendChild(doc.createTextNode(refNum));
			    	PaymentTerms.appendChild(doc.createTextNode("ZB00"));
			    	PaymentDueDate.appendChild(doc.createTextNode(paymentDueDt));
			    	
			    	Material.appendChild(doc.createTextNode(utilBean.PrePaddingZero(material_code, 18)));
			    }
			}
		    
		    
		    TransformerFactory transformerFactory = TransformerFactory.newInstance();
			//transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD","");
			//transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet","");
			
		    Transformer transformer = transformerFactory.newTransformer();
		    DOMSource source = new DOMSource(doc);
		    
		    String xmlFileNm="";
		    String datetime="";
		    datetime=splitSys[0].replaceAll("/", "")+""+splitSys[1].replaceAll(":", "");
		    
		    if(inv_type.equals("R"))
		    {
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
			    else //ADDED WHEN INVOICE NUMBER IS NOT GENERATED
		    	{
		    		xmlFileNm="AP_"+fms_MessageId+".xml";
		    	}
		    }
		    else
		    {
		    	if(!fms_MessageId.equals(""))
			    {
			    	if(sap_approval_flag.equals("Y"))
			        {
			    		xmlFileNm="AR_"+fms_MessageId+"_"+datetime+".xml";
			        }
			    	else
			    	{
			    		xmlFileNm="AR_"+fms_MessageId+".xml";
			    	}
			    }
			    else //ADDED WHEN INVOICE NUMBER IS NOT GENERATED
		    	{
		    		xmlFileNm="AR_"+fms_MessageId+".xml";
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
				        queryString="SELECT COUNT(*) "
				        		+ "FROM FMS_DERV_INV_FILE_DTL "
				        		+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
				        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND INV_TYPE=?";
				        stmt2 = conn.prepareStatement(queryString);
		    	        stmt2.setString(1, comp_cd);
		    	        stmt2.setString(2, bu_state_tin);
		    	        stmt2.setString(3, invoice_seq);
		    	        stmt2.setString(4, fy_year);
		    	        stmt2.setString(5, "X");
		    	        stmt2.setString(6, inv_type);
		    	        rset2=stmt2.executeQuery();
				        if(rset2.next())
				        {
				        	count=rset2.getInt(1);
				        }
				        rset2.close();
				        stmt2.close();
			
				        if(count > 0)
				        {
				        	queryString="UPDATE FMS_DERV_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
				        			+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
					        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND INV_TYPE=?";
				        	stmt3 = conn.prepareStatement(queryString);
			    	        stmt3.setString(1, xmlFileNm);
			    	        stmt3.setString(2, emp_cd);
			    	        stmt3.setString(3, comp_cd);
			    	        stmt3.setString(4, bu_state_tin);
			    	        stmt3.setString(5, invoice_seq);
			    	        stmt3.setString(6, fy_year);
			    	        stmt3.setString(7, "X");
			    	        stmt3.setString(8, inv_type);
		    	        	stmt3.executeUpdate();
		    	        	
		    	        	stmt3.close();
				        }
				        else
				        {
				        	queryString="INSERT INTO FMS_DERV_INV_FILE_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,"
				        			+ "FILE_NAME,ENT_BY,ENT_DT,INV_TYPE) "
				        			+ "VALUES(?,?,?,?,?,"
				        			+ "?,?,SYSDATE,?)";
				        	stmt3 = conn.prepareStatement(queryString);
			    	        stmt3.setString(1, comp_cd);
			    	        stmt3.setString(2, bu_state_tin);
			    	        stmt3.setString(3, invoice_seq);
			    	        stmt3.setString(4, fy_year);
			    	        stmt3.setString(5, "X");
			    	        stmt3.setString(6, xmlFileNm);
			    	        stmt3.setString(7, emp_cd);
			    	        stmt3.setString(8, inv_type);
		    	        	stmt3.executeUpdate();
		    	        	
		    	        	stmt3.close();
				        }
	    	
				        conn.commit();
			        }
		        }
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSapCrDrApprovalDetail()
	{
		String function_nm="getSapCrDrApprovalDetail()";
		try
		{
			queryString ="SELECT SAP_APPROVAL,SAP_APPROVED_BY,TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY') "
					+ "FROM FMS_DERV_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
					+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? AND INV_TYPE=? AND INV_FLAG=?";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
 	        stmt.setString(2, fy_year);
 	        stmt.setString(3, invoice_seq);
 	        stmt.setString(4, bu_state_tin);
 	        stmt.setString(5, inv_type);
 	        stmt.setString(6, crdr_type);
 	        rset=stmt.executeQuery();
			if(rset.next())
			{
				String sap_app_by=rset.getString(2)==null?"":rset.getString(2);
				sap_approved_by=utilBean.getEmpName(conn,sap_app_by);
				sap_approved_dt=rset.getString(3)==null?"":rset.getString(3);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSendDervCrDrMailDetail()
	{
		String function_nm="getSendDervCrDrMailDetail()";
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
			
			queryString1="SELECT BU_CONTACT_PERSON_CD,CONTACT_PERSON_CD,INVOICE_NO,TO_CHAR(DUE_DT,'DD/MM/YYYY'),TO_CHAR(INVOICE_DT,'DD/MM/YYYY') "
					+ "FROM FMS_DERV_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND BU_STATE_TIN=? AND FINANCIAL_YEAR=? "
					+ "AND INVOICE_SEQ=? AND INV_TYPE=? AND INV_FLAG=?";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_cd);
			stmt1.setString(3, bu_state_tin);
			stmt1.setString(4, fy_year);
			stmt1.setString(5, invoice_seq);
			stmt1.setString(6, inv_type);
			stmt1.setString(7, crdr_type);
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
			
			if(mail_pdf_type.equals(""))
			{
				mail_pdf_type="O";
			}
			String[] pdf_type=mail_pdf_type.split(",");
			
			String crdr_name="";
			if(crdr_type.equals("CR"))
			{
				crdr_name = "Credit Note";
			}
			else if(crdr_type.equals("DR"))
			{
				crdr_name = "Debit Note";
			}
			
			for(int i=0; i<pdf_type.length; i++)
			{
				String temp_pdf_type=""+pdf_type[i];
				
				VMAIL_PDF_TYPE.add(temp_pdf_type);
				
				String temp_pdf_type_nm="";
				if(!temp_pdf_type.equals(""))
				{
					temp_pdf_type_nm="Original";
					if(inv_type.equals("R"))
					{
						VMAIL_PDF_TYPE_NM.add("Original Remittance");
					}
					else
					{
						VMAIL_PDF_TYPE_NM.add("Original Invoice");
					}
				}
				
				String companyAbbr=utilBean.getCompanyAbbr(conn,comp_cd);
				String customerNm=utilBean.getCounterpartyName(conn,counterparty_cd);
				String subject="";
				String type="DERIVATIVES";
				
				String contact_inv_typ="RM"; //as discussed with Vijay, There is no INV option for Trader in entity Contact Master, consider RM for that
				
				
				subject=companyAbbr+"/"+customerNm+"/"+type+"/"+crdr_name+"/"+dueDate.replaceAll("/", "-")+"/"+invoiceNo;
				String to_list=utilBean.getEntityContactPersonEmailList(conn, comp_cd, counterparty_cd, "T", "P"+plant_seq, contact_inv_typ, "RLNG","Y");
				String cc_list=utilBean.getEntityContactPersonEmailList(conn, comp_cd, counterparty_cd, "T", "P"+plant_seq, contact_inv_typ, "RLNG","N");
				
				String tmpCcList=utilBean.getEntityContactPersonEmailList(conn, comp_cd, comp_cd, "B", "P"+plant_seq, contact_inv_typ, "RLNG","N");
				cc_list+=cc_list.equals("")?tmpCcList:","+tmpCcList;
				
				//get BCc list
				String bcc_list=utilBean.getEntityContactPersonEmailList(conn, comp_cd, comp_cd, "B", "P"+plant_seq, contact_inv_typ, "RLNG","B");
	
				String attachment="";
				Vector VTEMP_MAIL_ATTACHMENT = new Vector(); 
				Vector VTEMP_MAIL_ANNEXURE_ATTACHMENT_FLAG = new Vector();
				int st_count=0;
				
				queryString3="SELECT FILE_NAME "
						+ "FROM FMS_DERV_INV_FILE_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
	 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
	 	        		+ "AND PDF_TYPE NOT IN ('X') "
	 	        		+ "AND INV_TYPE=?";
				if(inv_type.equals("I"))
				{
					queryString3+="AND PDF_SIGNED=? ";
				}
				stmt3=conn.prepareStatement(queryString3);
				stmt3.setString(++st_count, comp_cd);
				stmt3.setString(++st_count, bu_state_tin);
				stmt3.setString(++st_count, invoice_seq);
				stmt3.setString(++st_count, fy_year);
				//stmt3.setString(++st_count, temp_pdf_type);
				stmt3.setString(++st_count, inv_type);
				if(inv_type.equals("I"))
				{
					stmt3.setString(++st_count, "Y");
				}
				rset3=stmt3.executeQuery();
				while(rset3.next())
				{
					//attachment=rset.getString(1)==null?"":rset.getString(1);
					VTEMP_MAIL_ATTACHMENT.add(rset3.getString(1)==null?"":rset3.getString(1));
					VTEMP_MAIL_ANNEXURE_ATTACHMENT_FLAG.add("");
				}
				rset3.close();
				stmt3.close();
				
				String companyNm=utilBean.getCompanyName(conn,comp_cd);
				String mail_body="";
				
				if(inv_type.equals("R"))
				{
					mail_body="Dear Sir/Madam,"
							+ "\n\nPlease find enclosed Remittance for " +customerNm+" Remittance : "+invoiceNo
							+ " dated "+invoiceDt.replaceAll("/", "-")+"."
							+ "\nPlease note payment will be processed on or before "+dueDate.replaceAll("/", "-")
							+ "\n\n\nThank You,"
							+ "\n\n"+companyNm+""
							+ "\n"+ownAddress+", "
							+ "\n"+ownCity+", "+ownState+" - "+ownPin+""
							+ "\nEmail: "+ownEmail+""
							+ "\nPh: "+ownPhone+""	
							+ "\n\nThis is an auto-generated email from the system, please do not reply to this email.";
				}
				else
				{
					mail_body="Dear Sir/Madam,"
							+ "\n\nPlease find enclosed Invoice# "+invoiceNo+" dated "+invoiceDt.replaceAll("/", "-")+".";
					mail_body+= "\n\nYou are requested to pay on or before the due date "+dueDate+". If already paid or no amount is due kindly ignore this message.";
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
				}
				
				VMAIL_FROM_LIST.add(utilBean.getFromMailId(conn));
				VMAIL_TO_LIST.add(to_list);
				VMAIL_CC_LIST.add(cc_list);
				VMAIL_BCC_LIST.add(bcc_list);
				VMAIL_SUBJECT.add(subject);
				//VMAIL_ATTACHMENT.add(attachment);
				VMAIL_ATTACHMENT.add(VTEMP_MAIL_ATTACHMENT);
				VMAIL_ANNEXURE_ATTACHMENT_FLAG.add(VTEMP_MAIL_ANNEXURE_ATTACHMENT_FLAG);
				
				if(inv_type.equals("I"))
				{
					VMAIL_ATTACHMENT_PATH.add(CommonVariable.signed_derv_inv_path);
				}
				else
				{
					VMAIL_ATTACHMENT_PATH.add(CommonVariable.derv_remittance_path);
				}
				
				String annexure_folder=inv_type+"_"+fy_year+"_"+bu_state_tin+"_"+invoice_seq;
				VMAIL_ANNEXURE_ATTACHMENT_PATH.add(CommonVariable.freeflow_annexure_path+""+annexure_folder+"/");
				VMAIL_BODY.add(mail_body);	
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
			queryString3="SELECT FILE_NAME,PDF_SIGNED "
					+ "FROM FMS_DERV_INV_FILE_DTL "
					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
 	        		+ "AND PDF_TYPE IN (?,?,?) AND INV_TYPE=?";
			stmt3=conn.prepareStatement(queryString3);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, bu_state_tin);
			stmt3.setString(3, invoice_seq);
			stmt3.setString(4, fy_year);
			stmt3.setString(5, "O");
			stmt3.setString(6, "D");
			stmt3.setString(7, "T");
			stmt3.setString(8, inv_type);
			rset3=stmt3.executeQuery();
			while(rset3.next())
			{
				VPDF_FILE_NAME.add(rset3.getString(1)==null?"":rset3.getString(1));
				String pdf_signed=rset3.getString(2)==null?"":rset3.getString(2);
				
				if(pdf_signed.equals("Y"))
				{
					VPDF_FILE_PATH.add(CommonVariable.signed_derv_inv_path);
				}
				else
				{
					VPDF_FILE_PATH.add(CommonVariable.derv_inv_path);
				}
			}
			rset3.close();
			stmt3.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	

	public void getDervInvoiceChangeList()
	{
		String function_nm="getDervInvoiceChangeList()";
		try
		{	
			for(int i=0; i<VMST_INV_TYPE_FLG.size(); i++)
			{
				String invFlag="",invoiceType="",inv_nm="";
				if(VMST_INV_TYPE_FLG.elementAt(i).equals("I"))
				{
					invFlag = "'F'";
					invoiceType="'I'";
					inv_nm ="Derivatives Invoice";
				}
				else if(VMST_INV_TYPE_FLG.elementAt(i).equals("R"))
				{
					invFlag = "'F'";
					invoiceType="'R'";
					inv_nm ="Derivatives Remittance";
				}
				else if(VMST_INV_TYPE_FLG.elementAt(i).equals("CRDR"))
				{
					invFlag = "'CR','DR'";
					invoiceType="'I','R'";
					inv_nm ="Derivatives ";
				}
				inv_index=0;
				String temp_period_start_dt=""+dateUtil.getFirstDateOfMonth(month, year);
				String temp_period_end_dt=""+dateUtil.getLastDateOfMonth(month, year);
				
				queryString1="SELECT DISTINCT INVOICE_SEQ,COUNTERPARTY_CD,BU_STATE_TIN "
						+ "FROM FMS_DERV_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND TO_DATE(?,'DD/MM/YYYY') <=INVOICE_DT AND TO_DATE(?,'DD/MM/YYYY') >= INVOICE_DT "
						+ "AND APPROVED_FLAG=? AND PDF_INV_DTL IS NOT NULL AND PRINT_BY_ORI IS NOT NULL "
						+ "AND INV_TYPE IN ("+invoiceType+") AND INV_FLAG IN ("+invFlag+")";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, temp_period_start_dt);
				stmt1.setString(3, temp_period_end_dt);
				stmt1.setString(4, "Y");
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					inv_index++;
					
					String inv_seq=rset1.getString(1)==null?"":rset1.getString(1);
					String contPtyCd=rset1.getString(2)==null?"":rset1.getString(2);
					String buStTin=rset1.getString(3)==null?"":rset1.getString(3);
					String own_cd="";
					String countpty_cd="";
					String agmtno="";
					String agmtrev="";
					String contno="";
					String contrev="";
					String cont_type="";
					String instrument_no="";
					String deal_no="";
					String plant_seq="";
					String bu_plant_seq="";
					String billing_cycle="";
					String financial_year="";
					String invoice_no="";
					String invoice_seq="";
					String checked_flag="";
					String approved_flag="";
					String pdf_flg="";
					String pdf_ori="";
					String sap_approved_flag="";
					String buStateTin="";
					String period_start_dt="";
					String period_end_dt="";
					String invoice_dt="";
					String authorized_flag="";
					String invoice_type="";
					String contRef="";
					String inv_no="";
					String inv_dt="";
					String inv_flag="";
					String invType="";
				
					queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,NULL,BU_UNIT,PLANT_SEQ,"
							+ "BU_STATE_TIN,FINANCIAL_YEAR,INVOICE_SEQ,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),"
							+ "INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),INV_FLAG,INV_TYPE,INSTRUMENT_NO "
							+ "FROM FMS_DERV_INVOICE_MST A "
							+ "WHERE COMPANY_CD=? "
//							+ "AND INVOICE_DT >= TO_DATE(?,'DD/MM/YYYY') AND INVOICE_DT <= TO_DATE(?,'DD/MM/YYYY') "
							+ "AND APPROVED_FLAG=? AND PDF_INV_DTL IS NOT NULL AND PRINT_BY_ORI IS NOT NULL "
							+ "AND INV_FLAG IN ("+invFlag+") AND INV_TYPE IN ("+invoiceType+") "
							+ "AND INVOICE_SEQ=? AND COUNTERPARTY_CD=? AND BU_STATE_TIN=? "
							+ "ORDER BY INVOICE_SEQ";
					int st_count=0;
					stmt=conn.prepareStatement(queryString);
					stmt.setString(++st_count, comp_cd);
//					stmt.setString(++st_count, temp_period_start_dt);
//					stmt.setString(++st_count, temp_period_end_dt);
					stmt.setString(++st_count, "Y");
					stmt.setString(++st_count, inv_seq);
					stmt.setString(++st_count, contPtyCd);
					stmt.setString(++st_count, buStTin);
					rset=stmt.executeQuery();
					while(rset.next())
					{	
						own_cd=rset.getString(1)==null?"":rset.getString(1);
						countpty_cd=rset.getString(2)==null?"":rset.getString(2);
						String temp_agmtno=rset.getString(3)==null?"":rset.getString(3);
						String temp_agmtrev=rset.getString(4)==null?"":rset.getString(4);
						String temp_contno=rset.getString(5)==null?"":rset.getString(5);
						String temp_contrev=rset.getString(6)==null?"":rset.getString(6);
						String temp_cont_type=rset.getString(7)==null?"0":rset.getString(7);
						String cargo_no="0";
						String temp_instrument_no=rset.getString(20)==null?"":rset.getString(20);
						String tmp_period_start_dt=rset.getString(14)==null?"":rset.getString(14);
						String tmp_period_end_dt=rset.getString(15)==null?"":rset.getString(15);
						
						if(contno.equals(""))
						{
							agmtno=temp_agmtno;
							agmtrev=temp_agmtrev;
							contno=temp_contno;
							contrev=temp_contrev;
							cont_type=temp_cont_type;
							instrument_no=temp_instrument_no;
							period_start_dt=tmp_period_start_dt;
							period_end_dt=tmp_period_end_dt;
						}
						else
						{
							agmtno+=", "+temp_agmtno;
							agmtrev+=", "+temp_agmtrev;
							contno+=", "+temp_contno;
							contrev+=", "+temp_contrev;
							cont_type+=", "+temp_cont_type;
							instrument_no+=", "+temp_instrument_no;
							period_start_dt+=", "+tmp_period_start_dt;
							period_end_dt+=", "+tmp_period_end_dt;
						}
						
						if(deal_no.equals(""))
						{
							deal_no=utilBean.NewDealMappingId(own_cd, countpty_cd, temp_agmtno, temp_agmtrev, temp_contno, temp_contrev, temp_cont_type, temp_instrument_no);
						}
						else
						{
							deal_no+=", "+utilBean.NewDealMappingId(own_cd, countpty_cd, temp_agmtno, temp_agmtrev, temp_contno, temp_contrev, temp_cont_type, temp_instrument_no);
						}
						
						bu_plant_seq=rset.getString(9)==null?"":rset.getString(9);
						plant_seq=rset.getString(10)==null?"":rset.getString(10);
						buStateTin=rset.getString(11)==null?"":rset.getString(11);
						financial_year=rset.getString(12)==null?"":rset.getString(12);
						inv_seq=rset.getString(13)==null?"":rset.getString(13);
						
						inv_no=rset.getString(16)==null?"":rset.getString(16);
						inv_dt=rset.getString(17)==null?"":rset.getString(17);
						inv_flag=rset.getString(18)==null?"":rset.getString(18);
						invType=rset.getString(19)==null?"":rset.getString(19);
						
						//VAGMT_NO.add(agmt);
						//VAGMT_REV_NO.add(agmt_rev);
						//VCONT_NO.add(cont);
						//VCONT_REV_NO.add(cont_rev);
						//VCARGO_NO.add(cargo_no);
						//VCONTRACT_TYPE.add(cont_type);
						
						String contRefNo="";
						String queryString2="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_TYPE,A.AGMT_NO,A.AGMT_REV,A.CONTRACT_TYPE,A.CONT_NO,A.CONT_REV,A.CONT_NAME,A.CONT_REF_NO,"
								+ "B.INSTRUMENT_NO,B.INSTRUMENT_TYPE,B.BUY_SELL,B.STATUS,B.QTY,B.QTY_UNIT,B.RATE,B.RATE_UNIT,"
								+ "B.PRODUCT_NM,B.CURVE_NM,B.PROJ_METHOD,TO_CHAR(B.CONT_DD_MM_YR,'DD/MM/YYYY'),"
								+ "TO_CHAR(B.PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(B.PRICE_END_DT,'DD/MM/YYYY'),B.CONV_FACTOR,TO_CHAR(A.TRADE_DT,'DD/MM/YYYY') "
								+ "FROM FMS_DERV_CONT_MST A,FMS_DERV_INSTRUMENT_MST B "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
								+ "AND B.STATUS='Y' AND B.INSTRUMENT_NO=? "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
								+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO";
					  	stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, countpty_cd);
						stmt2.setString(3, temp_agmtno);
						stmt2.setString(4, temp_contno);
						stmt2.setString(5, temp_cont_type);
						stmt2.setString(6, temp_instrument_no);
						rset2=stmt2.executeQuery();
						while(rset2.next())
						{
							contRefNo=rset2.getString(10)==null?"":rset2.getString(10);
						}
						rset2.close();
						stmt2.close();
						
						if(contRef.equals(""))
						{
							contRef=contRefNo;
						}
						else
						{
							contRef=contRef+", "+contRefNo;
						}
					}
					rset.close();
					stmt.close();
					
					VCOUNTERPARTY_CD.add(countpty_cd);
					VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
					VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
					VDEAL_MAPPING.add(deal_no);
					VPLANT_SEQ.add(plant_seq);
					VPLANT_ABBR.add(""+utilBean.getCounterpartyPlantABBR(conn,countpty_cd, comp_cd, plant_seq, "T"));
					VBU_PLANT_SEQ.add(bu_plant_seq);
					VBU_PLANT_ABBR.add(""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B"));
					VBU_STATE_TIN.add(buStateTin);							
					VFIN_YEAR.add(financial_year);
					VINVOICE_SEQ.add(inv_seq);
					VPERIOD_START_DT.add(period_start_dt);
					VPERIOD_END_DT.add(period_end_dt);
					VINVOICE_NO.add(inv_no);
					VINV_FLAG.add(inv_flag);
					VINVOICE_TYPE.add(invType);
					inv_nm = inv_flag.equals("CR") && invType.equals("I")?"Derivatives Credit Note Invoice"
							:inv_flag.equals("CR") && invType.equals("R")?"Derivatives Credit Note Remittance"
							:inv_flag.equals("DR") && invType.equals("I")?"Derivatives Debit Note Invoice"
							:inv_flag.equals("DR") && invType.equals("R")?"Derivatives Debit Note Remittance":"";
					VINVOICE_TYPE_NM.add(inv_nm);
					VINVOICE_DT.add(inv_dt);
					
					VCONT_REF.add(contRef);
					
					String flag="";
					String seqNo="";
					queryString2="SELECT FLAG,SEQ_NO "
							+ "FROM FMS_INVOICE_CHANGE_DTL  A "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
							+ "AND SEGMENT=? AND CHANGE_TYPE=? "
							+ "AND SEQ_NO=(SELECT MAX(B.SEQ_NO) FROM FMS_INVOICE_CHANGE_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR "
							+ "AND A.SEGMENT=B.SEGMENT AND A.CHANGE_TYPE=B.CHANGE_TYPE)";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, buStateTin);
					stmt2.setString(3, inv_seq);
					stmt2.setString(4, financial_year);
					stmt2.setString(5, "DERV"+invType);
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
					
					int days=dateUtil.getDays(dateUtil.getSysdate(),inv_dt);
					int allowable_day=30;
					int remDay=allowable_day-days;
					VREMAINING_DAYS.add(remDay<0?0:remDay);
					
					int count=0;
					queryString2="SELECT COUNT(*) "
							+ "FROM FMS_INVOICE_CHANGE_DTL A "
							+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
							+ "AND SEGMENT=? AND CHANGE_TYPE=? "
							+ "AND FLAG IN (?,?) ";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, buStateTin);
					stmt2.setString(3, inv_seq);
					stmt2.setString(4, financial_year);
					stmt2.setString(5, "DERV"+invType);
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
				rset1.close();
				stmt1.close();
				
				VINDEX.add(inv_index);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	

	public int isRePrintPDFRequested(String comp_cd,String buStateTin,String inv_seq,String financial_year,String segment,String chnage_type,String flag,String inv_type)
	{
		String function_nm="isRePrintPDFRequested()";
		int re_print_count=0;
		try
		{
			String queryString_tmp="SELECT COUNT(*) "
					+ "FROM FMS_INVOICE_CHANGE_DTL A "
					+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
					+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND SEGMENT=? AND CHANGE_TYPE=? AND FLAG=? "
					+ "AND SEQ_NO=(SELECT MAX(B.SEQ_NO) FROM FMS_INVOICE_CHANGE_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR "
					+ "AND A.SEGMENT=B.SEGMENT AND A.CHANGE_TYPE=B.CHANGE_TYPE)";
			stmt_temp=conn.prepareStatement(queryString_tmp);
			stmt_temp.setString(1, comp_cd);
			stmt_temp.setString(2, buStateTin);
			stmt_temp.setString(3, inv_seq);
			stmt_temp.setString(4, financial_year);
			stmt_temp.setString(5, segment+inv_type);
			stmt_temp.setString(6, chnage_type);
			stmt_temp.setString(7, flag);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				re_print_count=rset_temp.getInt(1);
			}
			rset_temp.close();
			stmt_temp.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return re_print_count;
	}
	
	
	public HttpServletRequest request = null;
	public void setRequest(HttpServletRequest request) {this.request = request;}
	
	String callFlag = "";
	public void setCallFlag(String callFlag) {this.callFlag = callFlag;}
	String comp_cd = "";
	public void setComp_cd(String comp_cd) {this.comp_cd = comp_cd;}
	String operation = "";
	public void setOperation(String operation) {this.operation = operation;}
	String opration = "";
	public void setOpration(String opration) {this.opration = opration;}
	
	double temp_tot_inv_amt=0;
	String quantity="";
	String start_dt="";
	String end_dt="";
	String account="";
	String derivative_curve_nm="";
	String rate="";
	
	int freez_count=0;
	String counterparty_cd = "";
	String inv_status_typ = "";
	String fy_year = "";
	String buy_sell = "";
	String report_dt = "";
	String plant_seq = "";
	String bu_plant_seq = "";
	String financial_curve = "";
	String year = "";
	String month = "";
	String agmt_type = "";
	String agmt_no = "";
	String agmt_rev = "";
	String cont_type = "";
	String cont_no = "";
	String cont_rev = "";
	String instrument_no = "";
	String period_start_dt = "";
	String period_end_dt = "";
	String invoice_seq = "";
	String bu_state_tin = "";
	String billing_cycle = "";
	String temp_period_start_dt = "";
	String temp_period_end_dt = "";
	String exist_financial_year = "";
	String inv_dt = "";
	String inv_type = "";
	String activityFlag = "";
	String print_pdf_type = "";
	String view_pdf_type = "";
	String sap_approval_flag = "";
	String xmlfile_name = "";
	String file_path = "";
	String emp_cd = "";
	String invoice_no = "";
	String mail_pdf_type = "";
	String from_dt = "";
	String to_dt = "";
	
	String cont_mapp = "";
	String automation_flag = "";
	String isGenerateXML = "";
	
	String sel_inv_no = "";
	String crdr_gen_type = "";
	String crdr_type = "";
	
	public void setInv_status_typ(String inv_status_typ) {this.inv_status_typ = inv_status_typ;}
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setFy_year(String fy_year) {this.fy_year = fy_year;}
	public void setBuy_sell(String buy_sell) {this.buy_sell = buy_sell;}
	public void setReport_dt(String report_dt) {this.report_dt = report_dt;}
	public void setPlant_seq(String plant_seq) {this.plant_seq = plant_seq;}
	public void setBu_plant_seq(String bu_plant_seq) {this.bu_plant_seq = bu_plant_seq;}
	public void setFinancial_curve(String financial_curve) {this.financial_curve = financial_curve;}
	public void setYear(String year) {this.year = year;}
	public void setMonth(String month) {this.month = month;}
	public void setAgmt_type(String agmt_type) {this.agmt_type = agmt_type;}
	public void setAgmt_no(String agmt_no) {this.agmt_no = agmt_no;}
	public void setAgmt_rev(String agmt_rev) {this.agmt_rev = agmt_rev;}
	public void setCont_type(String cont_type) {this.cont_type = cont_type;}
	public void setCont_no(String cont_no) {this.cont_no = cont_no;}
	public void setCont_rev(String cont_rev) {this.cont_rev = cont_rev;}
	public void setInstrument_no(String instrument_no) {this.instrument_no = instrument_no;}
	public void setPeriod_start_dt(String period_start_dt) {this.period_start_dt = period_start_dt;}
	public void setPeriod_end_dt(String period_end_dt) {this.period_end_dt = period_end_dt;}
	public void setInvoice_seq(String invoice_seq) {this.invoice_seq = invoice_seq;}
	public void setBu_state_tin(String bu_state_tin) {this.bu_state_tin = bu_state_tin;}
	public void setBilling_cycle(String billing_cycle) {this.billing_cycle = billing_cycle;}
	public void setTemp_period_start_dt(String temp_period_start_dt) {this.temp_period_start_dt = temp_period_start_dt;}
	public void setTemp_period_end_dt(String temp_period_end_dt) {this.temp_period_end_dt = temp_period_end_dt;}
	public void setExist_financial_year(String exist_financial_year) {this.exist_financial_year = exist_financial_year;}
	public void setInv_dt(String inv_dt) {this.inv_dt = inv_dt;}
	public void setInv_type(String inv_type) {this.inv_type = inv_type;}
	public void setActivityFlag(String activityFlag) {this.activityFlag = activityFlag;}
	public void setPrint_pdf_type(String print_pdf_type) {this.print_pdf_type = print_pdf_type;}
	public void setView_pdf_type(String view_pdf_type) {this.view_pdf_type = view_pdf_type;}
	public void setSap_approval_flag(String sap_approval_flag) {this.sap_approval_flag = sap_approval_flag;}
	public void setXmlfile_name(String xmlfile_name) {this.xmlfile_name = xmlfile_name;}
	public void setFile_path(String file_path) {this.file_path = file_path;}
	public void setEmp_cd(String emp_cd) {this.emp_cd = emp_cd;}
	public void setInvoice_no(String invoice_no) {this.invoice_no = invoice_no;}
	public void setMail_pdf_type(String mail_pdf_type) {this.mail_pdf_type = mail_pdf_type;}
	public void setFrom_dt(String from_dt) {this.from_dt = from_dt;}
	public void setTo_dt(String to_dt) {this.to_dt = to_dt;}
	
	public void setCont_mapp(String cont_mapp) {this.cont_mapp = cont_mapp;}
	public void setAutomation_flag(String automation_flag) {this.automation_flag = automation_flag;}
	public void setIsGenerateXML(String isGenerateXML) {this.isGenerateXML = isGenerateXML;}
	
	public void setSel_inv_no(String sel_inv_no) {this.sel_inv_no = sel_inv_no;}
	public void setCrdr_gen_type(String crdr_gen_type) {this.crdr_gen_type = crdr_gen_type;}
	public void setCrdr_type(String crdr_type) {this.crdr_type = crdr_type;}
	
	String max_fy_year="";
	String min_fy_year="";
	String prev_year_trade_cnt="";
	String tot_commitment="";
	String tot_commitment_mmscm="";
	String comp_name="";
	String plant_abbr="";
	String bu_plant_abbr="";
	String counterparty_abbr="";
	String counterparty_nm="";
	String remark1="";
	String remark2="";
	String inv_due_dt="";
	String inv_ref="";
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
	String tax_info="";
	String bu_tax_info="";
	String invoice_id_seq="";
	String total_inv_amt="";
	String attach_total_inv_amt="";
	String activity_value="";
	String zeroTotal ="";
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
	String sap_approved_by="";
	String sap_approved_dt="";
	String eod_procLastDt="";
	String bank_formula="";
	String chk_flg="";
	String invoice_type="";
	String criteri_formula="";
	String crdr_dt="";
	String crdr_due_dt="";
	String crdr_remark="";
	String reason="";
	String crdr_seq="";
	
	String isFreezed="";
	String eodProcessDoneOn="";
	String tot_qty="";
	String tot_gross_amt="";
	String tot_sell_amt="";
	String tot_buy_amt="";
	
	public String getMax_fy_year() {return max_fy_year;}
	public String getMin_fy_year() {return min_fy_year;}
	public String getPrev_year_trade_cnt() {return prev_year_trade_cnt;}
	public String getTot_commitment() {return tot_commitment;}
	public String getTot_commitment_mmscm() {return tot_commitment_mmscm;}
	public String getComp_name() {return comp_name;}
	public String getPlant_abbr() {return plant_abbr;}
	public String getBu_plant_abbr() {return bu_plant_abbr;}
	public String getCounterparty_abbr() {return counterparty_abbr;}
	public String getCounterparty_nm() {return counterparty_nm;}
	public String getRemark1() {return remark1;}
	public String getRemark2() {return remark2;}
	public String getInv_dt() {return inv_dt;}
	public String getInv_due_dt() {return inv_due_dt;}
	public String getPeriod_start_dt() {return period_start_dt;}
	public String getPeriod_end_dt() {return period_end_dt;}
	public String getInv_ref() {return inv_ref;}
	public String getFy_year() {return fy_year;}
	public String getInvoice_seq() {return invoice_seq;}
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
	public String getTax_info() {return tax_info;}
	public String getBu_tax_info() {return bu_tax_info;}
	public String getInvoice_no() {return invoice_no;}
	public String getInvoice_id_seq() {return invoice_id_seq;}
	public String getActivity_value() {return activity_value;}
	public String getTotal_inv_amt() {return total_inv_amt;}
	public String getAttach_total_inv_amt() {return attach_total_inv_amt;}
	public String getZeroTotal() {return zeroTotal;}
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
	public String getXmlfile_name() {return xmlfile_name;}
	public String getSap_approved_by() {return sap_approved_by;}
	public String getSap_approved_dt() {return sap_approved_dt;}
	public String getEod_procLastDt() {return eod_procLastDt;}
	public String getBank_formula() {return bank_formula;}
	public String getChk_flg() {return chk_flg;}
	public String getBu_plant_seq() {return bu_plant_seq;}
	public String getPlant_seq() {return plant_seq;}
	public String getBu_state_tin() {return bu_state_tin;}
	public String getInvoice_type() {return invoice_type;}
	public String getCriteri_formula() {return criteri_formula;}
	public String getSel_inv_no() {return sel_inv_no;}
	public String getCrdr_type() {return crdr_type;}
	public String getCrdr_dt() {return crdr_dt;}
	public String getCrdr_due_dt() {return crdr_due_dt;}
	public String getCrdr_remark() {return crdr_remark;}
	public String getReason() {return reason;}
	public String getTot_qty() {return tot_qty;}
	public String getTot_gross_amt() {return tot_gross_amt;}
	public String getTot_buy_amt() {return tot_buy_amt;}
	public String getTot_sell_amt() {return tot_sell_amt;}
	
	public String getIsFreezed() {return isFreezed;}
	public String getEodProcessDoneOn() {return eodProcessDoneOn;}
	
	Vector VMST_COUNTERPARTY_CD = new Vector();
	Vector VMST_COUNTERPARTY_NM = new Vector();
	Vector VMST_COUNTERPARTY_ABBR = new Vector();
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VCURVE_TYPE = new Vector();
	Vector VINDEX_NM = new Vector();
	Vector VAGMT_TYPE = new Vector();
	Vector VAGMT_NO = new Vector();
	Vector VAGMT_REV = new Vector();
	Vector VCONT_TYPE = new Vector();
	Vector VCONT_NO = new Vector();
	Vector VCONT_REV = new Vector();
	Vector VCONT_NAME = new Vector();
	Vector VCONT_REF = new Vector();
	Vector VDEAL_MAPPING = new Vector();
	Vector VINSTRUMENT_NO = new Vector();
	Vector VINSTRUMENT_TYPE = new Vector();
	Vector VBUY_SELL = new Vector();
	Vector VCARGO_ARRIVAL_DT = new Vector();
	Vector VBOOKED_MMBTU = new Vector();
	Vector VBOOKED_SCM = new Vector();
	Vector VQTY_UNIT = new Vector();
	Vector VTRADE_DT = new Vector();
	Vector VRATE = new Vector();
	Vector VCONT_START_DT = new Vector();
	Vector VCONT_END_DT = new Vector();
	Vector VDEAL_PRICE_CURVE = new Vector();
	Vector VDEAL_PROD_NM = new Vector();
	Vector VFLOAT_RATE = new Vector();
	Vector VINVGEN_FLAG = new Vector();
	Vector VPLANT_SEQ = new Vector();
	Vector VPLANT_ABBR = new Vector();
	Vector VBU_PLANT_SEQ = new Vector();
	Vector VBU_PLANT_ABBR = new Vector();
	Vector VPRICE_TYPE = new Vector();
	Vector VMST_PLANT_SEQ = new Vector();
	Vector VMST_PLANT_ABBR = new Vector();
	Vector VMST_BU_PLANT_SEQ = new Vector();
	Vector VMST_BU_PLANT_ABBR = new Vector();
	Vector VMST_CURVE_NM = new Vector();
	Vector VBU_STATE_TIN = new Vector();
	Vector VSELL_RATE = new Vector();
	Vector VSELL_AMT = new Vector();
	Vector VBUY_RATE = new Vector();
	Vector VBUY_AMT = new Vector();
	Vector VMST_INV_TYPE = new Vector();
	Vector VMST_INV_TYPE_FLG = new Vector();
	Vector VINDEX = new Vector();
	Vector VBILLING_FREQ = new Vector();
	Vector VFIN_YEAR = new Vector();
	Vector VINVOICE_NO = new Vector();
	Vector VINVOICE_SEQ = new Vector();
	Vector VINV_CHECKED_FLG = new Vector();
	Vector VINV_AUTHORIZED_FLG = new Vector();
	Vector VINV_APPROVED_FLG = new Vector();
	Vector VINV_PDF_FLG = new Vector();
	Vector VINV_PDF_TYPE = new Vector();
	Vector VINV_SAP_APPROVAL_FLAG = new Vector();
	Vector VINVOICE_EXIST = new Vector();
	Vector VIS_IRN_GENERATED = new Vector();
	Vector VPERIOD_END_DT = new Vector();
	Vector VPERIOD_START_DT = new Vector();
	Vector VINVOICE_DT = new Vector();
	Vector VINVOICE_DUE_DT = new Vector();
	Vector VINVOICE_ID_SEQ = new Vector();
	Vector VINVOICE_AMT = new Vector();
	Vector VPDF_INV_FLG = new Vector();
	Vector VSAP_APPROVAL_FLG = new Vector();
	Vector VPDF_TYPE = new Vector();
	Vector VPDF_FILE_NAME = new Vector();
	Vector VPDF_FILE_PATH = new Vector();
	Vector VSIGN_PDF_TYPE = new Vector();
	Vector VPDF_SIGNED_FLAG = new Vector();
	Vector VLINESEQNO = new Vector();
	Vector VPOSTINGKEY = new Vector();
	Vector VACCOUNT = new Vector();
	Vector VCURRENCYAMOUNT = new Vector();
	Vector VBUSINESSAREA = new Vector();
	Vector VITEMTEXT = new Vector();
	Vector VSHORTTEXT = new Vector();
	Vector VEXPOFREEZEESTATUS = new Vector();
	Vector VEXPOFREEZEETOTALPNL = new Vector();
	Vector VEXPOTOTALPNL = new Vector();
	Vector VINVOICE_TYPE = new Vector();
	Vector VCURVE_NM = new Vector();
	Vector VINSTRUMENT_DURATION = new Vector();
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
	Vector VSELL_PRICE_CD = new Vector();
	Vector VSELL_PRICE_NM = new Vector();
	Vector VGROSS_AMT = new Vector();
	Vector VNET_PAYABLE_AMT = new Vector();
	Vector VSHORT_RECEIVED = new Vector();
	Vector VALLOC_QTY = new Vector();
	Vector VINVOICE_RAISED_IN = new Vector();
	Vector VPAYMENT_DONE_IN = new Vector();
	Vector VCASH_FLOW = new Vector();
	Vector VPAY_RECV_AMT = new Vector();
	Vector VPAY_RECV_DT = new Vector();
	Vector VINV_REF = new Vector();
	
	Vector VPRODUCTION_MONTH = new Vector();
	Vector VACCRUAL_QTY = new Vector();
	Vector VACCRUAL_AMT = new Vector();
	Vector VEXCHNG_RATE = new Vector();
	Vector VCONT_MAP_LIST = new Vector();
	Vector VDIS_CONT_MAP_LIST = new Vector();
	Vector VBILLING_FREQ_FLAG = new Vector();
	Vector VEXCHNG_RATE_CD = new Vector();
	Vector VEXCHNG_RATE_DT = new Vector();
	Vector VALLOC_QTY_UNIT = new Vector();
	Vector VFIXED_PRICE = new Vector();
	Vector VFLOAT_PRICE = new Vector();
	Vector VMONTH = new Vector();
	Vector VYEAR = new Vector();
	Vector VFILE_UPLOAD_COUNT = new Vector();
	Vector VQTY_UNIT_CD = new Vector();
	Vector VFIN_SYS = new Vector();
	
	Vector VINV_TYPE = new Vector();
	Vector VINVOICE_NO_LIST = new Vector();
	Vector VCRITERIA_FLAG = new Vector();
	Vector VCRITERIA_NAME = new Vector();
	Vector VCRITERIA_HIDE = new Vector();
	Vector VALL_INSTRUMENT_NO = new Vector();
	
	Vector VMAIN_BOOKED_MMBTU = new Vector();
	Vector VMAIN_BUY_SELL = new Vector();
	Vector VMAIN_BUY_RATE = new Vector();
	Vector VMAIN_SELL_RATE = new Vector();
	Vector VMAIN_SELL_AMT = new Vector();
	Vector VMAIN_BUY_AMT = new Vector();
	Vector VMAIN_TOTAL_AMT = new Vector();
	Vector VCREDIT_DEBIT_NO = new Vector();
	Vector VREF_NO = new Vector();
	Vector VINV_FLAG = new Vector();
	Vector VCRDR_CRITERIA = new Vector();
	Vector VINV_FLAG_NM = new Vector();
	
	Vector VNEW_QTY = new Vector();
	Vector VNEW_QTY_UNIT = new Vector();
	Vector VNEW_SELL_RATE = new Vector();
	Vector VNEW_SELL_AMT = new Vector();
	Vector VNEW_BUY_RATE = new Vector();
	Vector VNEW_BUY_AMT = new Vector();
	Vector VNEW_TOTAL_AMT = new Vector();
	Vector IS_CRDR_SUBMITTED = new Vector();
	Vector VDISP_CRDR_CRITERIA = new Vector();
	Vector VINV_FIXED_PRICE = new Vector();
	Vector VINV_BOOKED_MMBTU = new Vector();
	Vector VNEW_BOOKED_MMBTU = new Vector();
	Vector VNEW_FIXED_PRICE = new Vector();
	Vector VEMAIL_SENT = new Vector();
	Vector VEMAIL_SENT_INFO = new Vector();
	
	Vector VINVOICE_TYPE_NM = new Vector();
	Vector VACTION_FLAG = new Vector();
	Vector VSEQ_NO = new Vector();
	Vector VRE_PRINT_PDF = new Vector();
	Vector VREMAINING_DAYS = new Vector();
	Vector VCOUNT = new Vector();
	
	public Vector getVMST_COUNTERPARTY_CD() {return VMST_COUNTERPARTY_CD;}
	public Vector getVMST_COUNTERPARTY_NM() {return VMST_COUNTERPARTY_NM;}
	public Vector getVMST_COUNTERPARTY_ABBR() {return VMST_COUNTERPARTY_ABBR;}
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVINDEX_NM() {return VINDEX_NM;}
	public Vector getVCURVE_TYPE() {return VCURVE_TYPE;}
	public Vector getVAGMT_TYPE() {return VAGMT_TYPE;}
	public Vector getVAGMT_NO() {return VAGMT_NO;}
	public Vector getVAGMT_REV() {return VAGMT_REV;}
	public Vector getVCONT_TYPE() {return VCONT_TYPE;}
	public Vector getVCONT_NO() {return VCONT_NO;}
	public Vector getVCONT_REV() {return VCONT_REV;}
	public Vector getVCONT_NAME() {return VCONT_NAME;}
	public Vector getVCONT_REF() {return VCONT_REF;}
	public Vector getVDEAL_MAPPING() {return VDEAL_MAPPING;}
	public Vector getVINSTRUMENT_NO() {return VINSTRUMENT_NO;}
	public Vector getVINSTRUMENT_TYPE() {return VINSTRUMENT_TYPE;}
	public Vector getVBUY_SELL() {return VBUY_SELL;}
	public Vector getVCARGO_ARRIVAL_DT() {return VCARGO_ARRIVAL_DT;}
	public Vector getVBOOKED_MMBTU() {return VBOOKED_MMBTU;}
	public Vector getVBOOKED_SCM() {return VBOOKED_SCM;}
	public Vector getVQTY_UNIT() {return VQTY_UNIT;}
	public Vector getVTRADE_DT() {return VTRADE_DT;}
	public Vector getVRATE() {return VRATE;}
	public Vector getVCONT_START_DT() {return VCONT_START_DT;}
	public Vector getVCONT_END_DT() {return VCONT_END_DT;}
	public Vector getVDEAL_PRICE_CURVE() {return VDEAL_PRICE_CURVE;}
	public Vector getVDEAL_PROD_NM() {return VDEAL_PROD_NM;}
	public Vector getVFLOAT_RATE() {return VFLOAT_RATE;}
	public Vector getVINVGEN_FLAG() {return VINVGEN_FLAG;}
	public Vector getVPLANT_SEQ() {return VPLANT_SEQ;}
	public Vector getVPLANT_ABBR() {return VPLANT_ABBR;}
	public Vector getVBU_PLANT_SEQ() {return VBU_PLANT_SEQ;}
	public Vector getVBU_PLANT_ABBR() {return VBU_PLANT_ABBR;}
	public Vector getVPRICE_TYPE() {return VPRICE_TYPE;}
	public Vector getVMST_PLANT_SEQ() {return VMST_PLANT_SEQ;}
	public Vector getVMST_PLANT_ABBR() {return VMST_PLANT_ABBR;}
	public Vector getVMST_BU_PLANT_SEQ() {return VMST_BU_PLANT_SEQ;}
	public Vector getVMST_BU_PLANT_ABBR() {return VMST_BU_PLANT_ABBR;}
	public Vector getVMST_CURVE_NM() {return VMST_CURVE_NM;}
	public Vector getVBU_STATE_TIN() {return VBU_STATE_TIN;}
	public Vector getVSELL_RATE() {return VSELL_RATE;}
	public Vector getVSELL_AMT() {return VSELL_AMT;}
	public Vector getVBUY_RATE() {return VBUY_RATE;}
	public Vector getVBUY_AMT() {return VBUY_AMT;}
	public Vector getVMST_INV_TYPE() {return VMST_INV_TYPE;}
	public Vector getVMST_INV_TYPE_FLG() {return VMST_INV_TYPE_FLG;}
	public Vector getVINDEX() {return VINDEX;}
	public Vector getVBILLING_FREQ() {return VBILLING_FREQ;}
	public Vector getVFIN_YEAR() {return VFIN_YEAR;}
	public Vector getVINVOICE_NO() {return VINVOICE_NO;}
	public Vector getVINVOICE_SEQ() {return VINVOICE_SEQ;}
	public Vector getVINV_CHECKED_FLG() {return VINV_CHECKED_FLG;}
	public Vector getVINV_AUTHORIZED_FLG() {return VINV_AUTHORIZED_FLG;}
	public Vector getVINV_APPROVED_FLG() {return VINV_APPROVED_FLG;}
	public Vector getVINV_PDF_FLG() {return VINV_PDF_FLG;}
	public Vector getVINV_PDF_TYPE() {return VINV_PDF_TYPE;}
	public Vector getVINV_SAP_APPROVAL_FLAG() {return VINV_SAP_APPROVAL_FLAG;}
	public Vector getVINVOICE_EXIST() {return VINVOICE_EXIST;}
	public Vector getVIS_IRN_GENERATED() {return VIS_IRN_GENERATED;}
	public Vector getVPERIOD_START_DT() {return VPERIOD_START_DT;}
	public Vector getVPERIOD_END_DT() {return VPERIOD_END_DT;}
	public Vector getVINVOICE_DT() {return VINVOICE_DT;}
	public Vector getVINVOICE_DUE_DT() {return VINVOICE_DUE_DT;}
	public Vector getVINVOICE_ID_SEQ() {return VINVOICE_ID_SEQ;}
	public Vector getVINVOICE_AMT() {return VINVOICE_AMT;}
	public Vector getVPDF_INV_FLG() {return VPDF_INV_FLG;}
	public Vector getVSAP_APPROVAL_FLG() {return VSAP_APPROVAL_FLG;}
	public Vector getVPDF_TYPE() {return VPDF_TYPE;}
	public Vector getVPDF_FILE_NAME() {return VPDF_FILE_NAME;}
	public Vector getVPDF_FILE_PATH() {return VPDF_FILE_PATH;}
	public Vector getVSIGN_PDF_TYPE() {return VSIGN_PDF_TYPE;}
	public Vector getVPDF_SIGNED_FLAG() {return VPDF_SIGNED_FLAG;}
	public Vector getVLINESEQNO() {return VLINESEQNO;}
	public Vector getVPOSTINGKEY() {return VPOSTINGKEY;}
	public Vector getVACCOUNT() {return VACCOUNT;}
	public Vector getVCURRENCYAMOUNT() {return VCURRENCYAMOUNT;}
	public Vector getVBUSINESSAREA() {return VBUSINESSAREA;}
	public Vector getVITEMTEXT() {return VITEMTEXT;}
	public Vector getVSHORTTEXT() {return VSHORTTEXT;}
	public Vector getVEXPOFREEZEESTATUS() {return VEXPOFREEZEESTATUS;}
	public Vector getVEXPOFREEZEETOTALPNL() {return VEXPOFREEZEETOTALPNL;}
	public Vector getVEXPOTOTALPNL() {return VEXPOTOTALPNL;}
	public Vector getVINVOICE_TYPE() {return VINVOICE_TYPE;}
	public Vector getVINSTRUMENT_DURATION() {return VINSTRUMENT_DURATION;}
	public Vector getVCURVE_NM() {return VCURVE_NM;}
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
	public Vector getVSELL_PRICE_CD() {return VSELL_PRICE_CD;}
	public Vector getVSELL_PRICE_NM() {return VSELL_PRICE_NM;}
	public Vector getVGROSS_AMT() {return VGROSS_AMT;}
	public Vector getVNET_PAYABLE_AMT() {return VNET_PAYABLE_AMT;}
	public Vector getVSHORT_RECEIVED() {return VSHORT_RECEIVED;}
	public Vector getVALLOC_QTY() {return VALLOC_QTY;}
	public Vector getVINVOICE_RAISED_IN() {return VINVOICE_RAISED_IN;}
	public Vector getVPAY_RECV_AMT() {return VPAY_RECV_AMT;}
	public Vector getVPAY_RECV_DT() {return VPAY_RECV_DT;}
	public Vector getVPAYMENT_DONE_IN() {return VPAYMENT_DONE_IN;}
	public Vector getVCASH_FLOW() {return VCASH_FLOW;}
	public Vector getVINV_REF() {return VINV_REF;}
	
	public Vector getVPRODUCTION_MONTH() {return VPRODUCTION_MONTH;}
	public Vector getVACCRUAL_QTY() {return VACCRUAL_QTY;}
	public Vector getVACCRUAL_AMT() {return VACCRUAL_AMT;}
	public Vector getVEXCHNG_RATE() {return VEXCHNG_RATE;}
	public Vector getVCONT_MAP_LIST() {return VCONT_MAP_LIST;}
	public Vector getVDIS_CONT_MAP_LIST() {return VDIS_CONT_MAP_LIST;}
	public Vector getVBILLING_FREQ_FLAG() {return VBILLING_FREQ_FLAG;}
	public Vector getVEXCHNG_RATE_CD() {return VEXCHNG_RATE_CD;}
	public Vector getVEXCHNG_RATE_DT() {return VEXCHNG_RATE_DT;}
	public Vector getVALLOC_QTY_UNIT() {return VALLOC_QTY_UNIT;}
	public Vector getVFIXED_PRICE() {return VFIXED_PRICE;}
	public Vector getVFLOAT_PRICE() {return VFLOAT_PRICE;}
	public Vector getVYEAR() {return VYEAR;}
	public Vector getVMONTH() {return VMONTH;}
	public Vector getVFILE_UPLOAD_COUNT() {return VFILE_UPLOAD_COUNT;}
	public Vector getVQTY_UNIT_CD() {return VQTY_UNIT_CD;}
	public Vector getVFIN_SYS() {return VFIN_SYS;}
	
	public Vector getVINV_TYPE() {return VINV_TYPE;}
	public Vector getVINVOICE_NO_LIST() {return VINVOICE_NO_LIST;}
	public Vector getVCRITERIA_FLAG() {return VCRITERIA_FLAG;}
	public Vector getVCRITERIA_NAME() {return VCRITERIA_NAME;}
	public Vector getVCRITERIA_HIDE() {return VCRITERIA_HIDE;}
	public Vector getVALL_INSTRUMENT_NO() {return VALL_INSTRUMENT_NO;}
	
	public Vector getVMAIN_BOOKED_MMBTU() {return VMAIN_BOOKED_MMBTU;}
	public Vector getVMAIN_BUY_SELL() {return VMAIN_BUY_SELL;}
	public Vector getVMAIN_SELL_RATE() {return VMAIN_SELL_RATE;}
	public Vector getVMAIN_SELL_AMT() {return VMAIN_SELL_AMT;}
	public Vector getVMAIN_BUY_RATE() {return VMAIN_BUY_RATE;}
	public Vector getVMAIN_BUY_AMT() {return VMAIN_BUY_AMT;}
	public Vector getVMAIN_TOTAL_AMT() {return VMAIN_TOTAL_AMT;}
	public Vector getVCREDIT_DEBIT_NO() {return VCREDIT_DEBIT_NO;}
	public Vector getVREF_NO() {return VREF_NO;}
	public Vector getVINV_FLAG() {return VINV_FLAG;}
	public Vector getVCRDR_CRITERIA() {return VCRDR_CRITERIA;}
	public Vector getVINV_FLAG_NM() {return VINV_FLAG_NM;}
	
	public Vector getVNEW_QTY() {return VNEW_QTY;}
	public Vector getVNEW_QTY_UNIT() {return VNEW_QTY_UNIT;}
	public Vector getVNEW_SELL_RATE() {return VNEW_SELL_RATE;}
	public Vector getVNEW_SELL_AMT() {return VNEW_SELL_AMT;}
	public Vector getVNEW_BUY_RATE() {return VNEW_BUY_RATE;}
	public Vector getVNEW_BUY_AMT() {return VNEW_BUY_AMT;}
	public Vector getVNEW_TOTAL_AMT() {return VNEW_TOTAL_AMT;}
	public Vector getIS_CRDR_SUBMITTED() {return IS_CRDR_SUBMITTED;}
	public Vector getVDISP_CRDR_CRITERIA() {return VDISP_CRDR_CRITERIA;}
	public Vector getVINV_FIXED_PRICE() {return VINV_FIXED_PRICE;}
	public Vector getVINV_BOOKED_MMBTU() {return VINV_BOOKED_MMBTU;}
	public Vector getVNEW_BOOKED_MMBTU() {return VNEW_BOOKED_MMBTU;}
	public Vector getVNEW_FIXED_PRICE() {return VNEW_FIXED_PRICE;}
	public Vector getVEMAIL_SENT_INFO() {return VEMAIL_SENT_INFO;}
	public Vector getVEMAIL_SENT() {return VEMAIL_SENT;}
	
	public Vector getVINVOICE_TYPE_NM() {return VINVOICE_TYPE_NM;} 
	public Vector getVACTION_FLAG() {return VACTION_FLAG;} 
	public Vector getVSEQ_NO() {return VSEQ_NO;} 
	public Vector getVRE_PRINT_PDF() {return VRE_PRINT_PDF;} 
	public Vector getVREMAINING_DAYS() {return VREMAINING_DAYS;} 
	public Vector getVCOUNT() {return VCOUNT;} 
	
}
