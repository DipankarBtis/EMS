package automation;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.TaxCalculator;
import com.etrm.fms.util.UtilBean;

public class Auto_CreditRisk 
{
	public static void main(String[] args) 
	{
		String report_param=args.length>0?args[0]:"";
		System.out.println(report_param);
		CR_Automation cra = new CR_Automation();
		cra.init(report_param);
	}
}

class CR_Automation
{
	String db_src_file_name="Auto_CreditRisk.java";
	Connection conn;
	PreparedStatement stmt,stmt1,stmt2,stmt3,stmt4,stmt_temp,stmt_temp1;
	ResultSet rset,rset1,rset2,rset3,rset4,rset_temp,rset_temp1;
	String queryString="",queryString1="",queryString2="",queryString3="",queryString4="",queryString5="",queryString_temp="",queryString_temp1="";
	String report_dt="";
	String expo_type="R";
	String file_path="";
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf3 = new DecimalFormat("###########0.000");
	
	//String comp_cd="1";
	//String comp_abbr="";
	String context="";
	String emp_cd="0";
	
	HashMap<String, Double> limit_value = new HashMap<String, Double>();
	HashMap<String, Double> parent_limit_value = new HashMap<String, Double>();
	HashMap<String,Double> temp_limit_info = new HashMap<String,Double>();
	
	UtilBean utilBean = new UtilBean();
	DateUtil utilDate = new DateUtil();
	TaxCalculator TaxCalc = new TaxCalculator();
	Auto_MailDelivery mail = new Auto_MailDelivery();
	
	public void init(String report_param)
	{ 
		String function_nm="init()";
        try
        {
        	conn=new Auto_DB_Connection().db_conn();
        	if(conn != null)
            {    			
    			//comp_abbr=utilBean.getCompanyAbbr(conn, comp_cd);
    			context=utilBean.getAutomationKeyDetail(conn, "ENV_CONTEXT");
    			file_path=utilBean.getAutomationKeyDetail(conn, "DAILY_RPT_PATH");
    			
    			if(report_param.equals("ExposureTracking"))
    			{
	    			report_dt=utilDate.getPreviousDate();
	    			getExpoxureHeading();
					getActiveSellContractInformation();
					getChargeMaster();
					ExposureCalculation();
					CR_LimitCalculation();
					CR_IGX_Suummary();
					ExposureTrackingDealWiseFreezeData();
					SendExcelForExposureTrackingReport();
    			}
    			else if(report_param.equals("ExceedCredit"))
    			{
					report_dt=utilDate.getPreviousDate();
					getExpoxureHeading();
					getActiveSellContractInformation();
					getChargeMaster();
					ExposureCalculation();
					CR_LimitCalculation();
					CR_IGX_Suummary();
					getExccedCreditReason();
					SendExcelForExeedCreditReport();
    			}
            }
        	conn.close();
			conn = null;
        }
        catch(Exception e)
        {
        	new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
        }
        finally
	    {
	    	if(rset != null){try{rset.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset1 != null){try{rset1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset2 != null){try{rset2.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset3 != null){try{rset3.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset4 != null){try{rset4.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_temp != null){try{rset_temp.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_temp1 != null){try{rset_temp1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp != null){try{stmt_temp.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp1 != null){try{stmt_temp1.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}
	
	public void getExpoxureHeading()
	{
		String function_nm="getExpoxureHeading()";
		try
		{
			VEXPOSURE_HEADING.add("KYC Deal Exposure");
			VEXPOSURE_HEADING.add("IGX Deal Exposure");
			
			VEXPOSURE_HEADING_FLAG.add("K");
			VEXPOSURE_HEADING_FLAG.add("I");
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getActiveSellContractInformation()
	{
		String function_nm="getActiveSellContractInformation()";
		try
		{
			//SELL SIDE
			
			for(int i=0;i<VEXPOSURE_HEADING_FLAG.size();i++)
			{
				String expoTy=""+VEXPOSURE_HEADING_FLAG.elementAt(i); //THIS IS NOTHING BUT GX SO,DONT CHANGE FLAG VALUE
				
				String contType="";
				if(expoTy.equals("K"))
				{
					contType="CONTRACT_TYPE IN ('S','L')";
				}
				else if(expoTy.equals("I"))
				{
					contType="CONTRACT_TYPE IN ('X')";
				}
				
				int index=0;
				int count=0;
				queryString="SELECT COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CONT_REF_NO,TRADE_REF_NO,"
						+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),"
						+ "RATE,RATE_UNIT,TCQ,DCQ,AGMT_BASE,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),COMPANY_CD "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE "+contType+" ";
				if(isCreditExceedRpt.equals("Y"))
				{
					queryString+= "AND ((SIGNING_DT = TO_DATE(?,'DD/MM/YYYY') AND END_DT >= TO_DATE(?,'DD/MM/YYYY')) "
								+ "OR (START_DT <= TO_DATE(?,'DD/MM/YYYY') AND END_DT >= TO_DATE(?,'DD/MM/YYYY')) "
								+ "OR (SIGNING_DT = TO_DATE(?,'DD/MM/YYYY') AND START_DT <= TO_DATE(?,'DD/MM/YYYY') AND END_DT >= TO_DATE(?,'DD/MM/YYYY'))) "; 
				}
				else
				{
					queryString+= "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
				}
				queryString+= "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "ORDER BY START_DT";
				stmt = conn.prepareStatement(queryString);
				if(isCreditExceedRpt.equals("Y"))
				{
					stmt.setString(++count, report_dt);
					stmt.setString(++count, report_dt);
					stmt.setString(++count, report_dt);
					stmt.setString(++count, report_dt);
					stmt.setString(++count, report_dt);
					stmt.setString(++count, report_dt);
					stmt.setString(++count, report_dt);
				}
				else
				{
					stmt.setString(++count, report_dt);
					stmt.setString(++count, report_dt);
				}
				rset=stmt.executeQuery();
				while(rset.next())
				{
					index+=1;
					VACCOUNT.add("Sell");
					VGX.add(expoTy);
					
					String countpty_cd=rset.getString(1)==null?"":rset.getString(1);
					String agmt=rset.getString(2)==null?"":rset.getString(2);
					String agmt_rev=rset.getString(3)==null?"":rset.getString(3);
					String cont=rset.getString(4)==null?"":rset.getString(4);
					String cont_rev=rset.getString(5)==null?"":rset.getString(5);
					String cont_type=rset.getString(6)==null?"":rset.getString(6);
					String cont_ref=rset.getString(7)==null?"":rset.getString(7);
					String trade_ref=rset.getString(8)==null?"":rset.getString(8); //THIS NOT REQUIRED BCUZ IGX DEAL NOT COME HERE
					String start_dt=rset.getString(9)==null?"":rset.getString(9);
					String end_dt=rset.getString(10)==null?"":rset.getString(10);
					
					String companyCd=rset.getString(17)==null?"":rset.getString(17);
					
					if(cont_type.equals("X"))
					{
						cont_ref=trade_ref;
					}
					
					double rate=rset.getDouble(11);
					String rate_unit=rset.getString(12)==null?"":rset.getString(12);
					//13
					//14
					String agmt_base=rset.getString(15)==null?"":rset.getString(15);
					String nm =utilBean.getCounterpartyName(conn,countpty_cd);
					VCOUNTERPARTY_CD.add(countpty_cd);
					VCOUNTERPARTY_NM.add(nm);
					VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
					VLEGAL_ENTITY.add(companyCd);
					VLEGAL_ENTITY_ABBR.add(utilBean.getCompanyAbbr(conn,companyCd));
					
					VAGMT_NO.add(agmt);
					VAGMT_REV_NO.add(agmt_rev);
					VCONT_NO.add(cont);
					VCONT_REV_NO.add(cont_rev);
					VCONTRACT_TYPE.add(cont_type);
					
					//String displayDealNum=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
					String displayDealNum=utilBean.NewDealMappingId(companyCd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
					VMAPPING_ID.add(displayDealNum);
					if(agmt_base.equals("D"))
					{
						displayDealNum+="[DLV]";
					}
					VHEADING_INFO.add(nm+" ("+displayDealNum+")");
					if(!cont_ref.equals(""))
					{
						displayDealNum+="["+cont_ref+"]";
					}
					VDISPLAY_DEAL_MAP.add(displayDealNum);
					VDIS_CONTRACT_TYPE.add(utilBean.getContractTypeName(cont_type));
					
					VCONT_REF.add(cont_ref);
					
					VSTART_DT.add(start_dt);
					VEND_DT.add(end_dt);
					VRATE.add(utilBean.RateNumberFormat(rate, rate_unit));
					VRATE_UNIT.add(rate_unit);
					VRATE_UNIT_NM.add(utilBean.getRateUnitNm(conn,rate_unit));
					VTCQ.add(nf.format(rset.getDouble(13)));
					VDCQ.add(nf.format(rset.getDouble(14)));
					VAGMT_BASE.add(agmt_base);
					VSIGNING_DT.add(rset.getString(16)==null?"":rset.getString(16));
				}
				rset.close();
				stmt.close();
				VINDEX.add(index);
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getActiveBuyContractInformation()
	{
		String function_nm="getActiveBuyContractInformation()";
		try
		{
			//BUY SIDE
			queryString="SELECT COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CONT_REF_NO,TRADE_REF_NO,"
					+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),"
					+ "RATE,RATE_UNIT,TCQ,DCQ,AGMT_BASE,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),COMPANY_CD "
					+ "FROM FMS_TRADER_CONT_MST A "
					+ "WHERE CONTRACT_TYPE NOT IN (?) "
					+ "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
					+ "ORDER BY START_DT";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, "I");
			stmt.setString(2, report_dt);
			stmt.setString(3, report_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VACCOUNT.add("Buy");
				
				String countpty_cd=rset.getString(1)==null?"":rset.getString(1);
				String agmt=rset.getString(2)==null?"":rset.getString(2);
				String agmt_rev=rset.getString(3)==null?"":rset.getString(3);
				String cont=rset.getString(4)==null?"":rset.getString(4);
				String cont_rev=rset.getString(5)==null?"":rset.getString(5);
				String cont_type=rset.getString(6)==null?"":rset.getString(6);
				String cont_ref=rset.getString(7)==null?"":rset.getString(7);
				String trade_ref=rset.getString(8)==null?"":rset.getString(8); //THIS NOT REQUIRED BCUZ IGX DEAL NOT COME HERE
				String start_dt=rset.getString(9)==null?"":rset.getString(9);
				String end_dt=rset.getString(10)==null?"":rset.getString(10);
				
				String companyCd=rset.getString(17)==null?"":rset.getString(17);
				
				double rate=rset.getDouble(11);
				String rate_unit=rset.getString(12)==null?"":rset.getString(12);
				//13
				//14
				String agmt_base=rset.getString(15)==null?"":rset.getString(15); //NOT REQUIRED FOR BUY DEAL
				String nm=utilBean.getCounterpartyName(conn,countpty_cd);
				VCOUNTERPARTY_CD.add(countpty_cd);
				VCOUNTERPARTY_NM.add(""+nm);
				VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VLEGAL_ENTITY.add(companyCd);
				VLEGAL_ENTITY_ABBR.add(utilBean.getCompanyAbbr(conn,companyCd));
				
				VAGMT_NO.add(agmt);
				VAGMT_REV_NO.add(agmt_rev);
				VCONT_NO.add(cont);
				VCONT_REV_NO.add(cont_rev);
				VCONTRACT_TYPE.add(cont_type);
				
				String displayDealNum=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
				VMAPPING_ID.add(displayDealNum);
				VHEADING_INFO.add(nm+" ("+displayDealNum+")");
				if(!cont_ref.equals(""))
				{
					displayDealNum+="["+cont_ref+"]";
				}
				VDISPLAY_DEAL_MAP.add(displayDealNum);
				VDIS_CONTRACT_TYPE.add(utilBean.getContractTypeName(cont_type));
				
				VCONT_REF.add(cont_ref);
				
				VSTART_DT.add(start_dt);
				VEND_DT.add(end_dt);
				VRATE.add(utilBean.RateNumberFormat(rate, rate_unit));
				VRATE_UNIT.add(rate_unit);
				VRATE_UNIT_NM.add(utilBean.getRateUnitNm(conn,rate_unit));
				VTCQ.add(nf.format(rset.getDouble(13)));
				VDCQ.add(nf.format(rset.getDouble(14)));
				VAGMT_BASE.add("");//NOT REQUIRED FOR BUY DEAL	
				VSIGNING_DT.add(rset.getString(16)==null?"":rset.getString(16));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void ExposureCalculation()
	{
		String function_nm="ExposureCalculation()";
		try
		{
			for(int i=0;i<VCOUNTERPARTY_CD.size();i++)
			{
				String companyCd=""+VLEGAL_ENTITY.elementAt(i);
				String countpty_cd=""+VCOUNTERPARTY_CD.elementAt(i);
				String agmt=""+VAGMT_NO.elementAt(i);
				String agmt_rev=""+VAGMT_REV_NO.elementAt(i);
				String cont=""+VCONT_NO.elementAt(i);
				String cont_rev=""+VCONT_REV_NO.elementAt(i);
				String cont_type=""+VCONTRACT_TYPE.elementAt(i);
				String tcq = ""+VTCQ.elementAt(i);
				String dcq = ""+VDCQ.elementAt(i);
				String agmt_base = ""+VAGMT_BASE.elementAt(i);
				String rate = ""+VRATE.elementAt(i);
				String rate_unit = ""+VRATE_UNIT.elementAt(i);
				String start_dt = ""+VSTART_DT.elementAt(i);
				String end_dt = ""+VEND_DT.elementAt(i);
				String gx = ""+VGX.elementAt(i);
				
				if(expo_type.equals("R"))
				{
					//GET LAST BILLED PERIOD END DATE
					/*String last_billed_dt="";
					queryString="SELECT TO_CHAR(MAX(PERIOD_END_DT),'DD/MM/YYYY') "
							+ "FROM FMS_INVOICE_MST "
							+ "WHERE COMPANY_CD='"+comp_cd+"' AND COUNTERPARTY_CD='"+countpty_cd+"' "
							+ "AND AGMT_NO='"+agmt+"' AND CONT_NO='"+cont+"' "
							+ "AND CONTRACT_TYPE='"+cont_type+"' AND INVOICE_DT<=TO_DATE('"+report_dt+"','DD/MM/YYYY') "
							+ "AND PDF_INV_DTL IS NOT NULL"; 
					rset=stmt.executeQuery(queryString);
					if(rset.next())
					{
						last_billed_dt=rset.getString(1)==null?"":rset.getString(1);
					}
					else
					{
						last_billed_dt="";
					}*/
					
					String contPriceMapping=countpty_cd+"-"+agmt+"-"+cont;
					String price_type="";
					String variable_rate="";
					String curve_nm="";
					
					queryString="SELECT PHYS_CURVE_NM,PRICE_TYPE,RATE,RATE_UNIT,CURVE_NM "
							+ "FROM FMS_CONT_PRICE_DTL "
							+ "WHERE COMPANY_CD=? AND MAPPING_ID=? AND CONTRACT_TYPE=? "
							+ "AND FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND TO_DT>=TO_DATE(?,'DD/MM/YYYY')";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, companyCd);
					stmt.setString(2, contPriceMapping);
					stmt.setString(3, cont_type);
					stmt.setString(4, report_dt);
					stmt.setString(5, report_dt);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						//phys_curve_nm=rset.getString(1)==null?"":rset.getString(1);
						price_type=rset.getString(2)==null?"":rset.getString(2);						
						curve_nm=rset.getString(5)==null?"":rset.getString(5);
					}
					
					if(price_type.equals("F"))
					{
						VPRICE_TYPE.add("FIXED : Variable");
					}
					else if(price_type.equals("M"))
					{
						VPRICE_TYPE.add("FLOAT : "+curve_nm);
					}
					else
					{
						VPRICE_TYPE.add("FIXED");
					}
					rset.close();
					stmt.close();
					
					double billedAmt=getAccountReceivable(companyCd, countpty_cd, agmt, cont, cont_type, report_dt);
					double unbilledAmt=getUnbilledReceivable(companyCd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, agmt_base,report_dt,start_dt,end_dt,rate,rate_unit);
					String totalDlvQty=nf.format(billedQty+unbilledQty);
					double undeliveredCurrentMonthAmt = getUndeliveredCurrentMonth(companyCd, countpty_cd, agmt, cont, cont_type, agmt_base, report_dt,start_dt,end_dt,rate,rate_unit,dcq,tcq,totalDlvQty);
					totalDlvQty=nf.format(billedQty+unbilledQty+undeliveredQty);
					double forwardNotionalNextMonthAmt = getForwardNotionalNextMonth(companyCd, countpty_cd, agmt, cont, cont_type, agmt_base, report_dt,start_dt,end_dt,rate,rate_unit,dcq,tcq,totalDlvQty);
					
					double gross_exposure = billedAmt+unbilledAmt;
					double gross_exposure_incl_tax=billedAmtWithTax+unbilledAmtWithTax;
					double exchng_rate=getExchangeRate(companyCd, countpty_cd, agmt, cont, cont_type, report_dt);
					
					double collateral_value=0;
					String collateral_info="";
					double LC_AMT = 0;//STORE SEPARTE LC_AMT FOR GEM REPORT
					double OTH_COLLAT = 0; //STORE SEPARTE OTH_COLLAT FOR GEM REPORT
					
					double cash_collateral_value=0;
					String cash_collateral_info="";
					
					double pcg_value=0;
					double pcg_uncapped_value=0;
					double pcg_deal_value=0;
					String pcg_info="";
					
					if(gx.equals("K"))
					{
						queryString ="SELECT NVL(A.VALUE,0),A.CURRENCY,A.SEC_TYPE,A.SEC_REF_NO,TO_CHAR(A.EXPIRE_DT,'DD/MM/YYYY'),"
								+ "B.SHARE_PERCENT "
								+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.GX=? "
								+ "AND B.AGMT_NO=? AND B.CONT_NO=? AND B.CONTRACT_TYPE=? "
								+ "AND A.SEC_CATEGORY='R' AND SEC_TYPE IN ('LC','BG') AND STATUS IN ('O','C','R') "
								+ "AND ISSUE_DT<=to_date(?,'DD/MM/YYYY') AND (EXPIRE_DT>=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND (TO_DATE(TO_CHAR(CANCEL_DT,'DD/MM/YYYY'),'DD/MM/YYYY')-1 >= TO_DATE(?,'DD/MM/YYYY') OR CANCEL_DT IS NULL)) "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.SEQ_NO=B.SEQ_NO "
								+ "AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX";
						stmt = conn.prepareStatement(queryString);
						stmt.setString(1, companyCd);
						stmt.setString(2, countpty_cd);
						stmt.setString(3, gx);
						stmt.setString(4, agmt);
						stmt.setString(5, cont);
						stmt.setString(6, cont_type);
						stmt.setString(7, report_dt);
						stmt.setString(8, report_dt);
						stmt.setString(9, report_dt);
						rset=stmt.executeQuery();
						while(rset.next())
						{
							double secuVal=rset.getDouble(1);
							double oriSecuVal=secuVal;
							String currency=rset.getString(2)==null?"":rset.getString(2);
							String secType=rset.getString(3)==null?"":rset.getString(3);
							String secRef=rset.getString(4)==null?"":rset.getString(4);
							
							double split_percent=rset.getDouble(6);
							String splitPercent=rset.getString(6)==null?"":rset.getString(6);
							
							if(collateral_info.equals(""))
							{
								collateral_info = secType+" : "+secRef;
							}
							else
							{
								collateral_info += "\n"+secType+" : "+secRef;
							}
							
							if(!splitPercent.equals(""))
							{
								secuVal=(secuVal*split_percent)/100;
								if(currency.equals("2"))
								{
									collateral_info +="("+split_percent+"% of "+nf.format(oriSecuVal)+" USD)";
								}
								else
								{
									collateral_info +="("+split_percent+"% of "+nf.format(oriSecuVal)+" INR)";
								}
							}
							
							double amount=0;
							if(currency.equals("2"))
							{
								amount=secuVal*exchng_rate;
								collateral_info +=" = ( "+nf.format(secuVal)+" USD * "+nf2.format(exchng_rate)+") = "+nf.format(amount)+" INR";
							}
							else
							{
								amount=secuVal;
								collateral_info +=" = "+nf.format(amount)+" INR";
							}
							
							collateral_value+=amount;
							
							//FOR GEM REPORT
							if(secType.equalsIgnoreCase("LC"))
							{
								LC_AMT += amount;
							}
							else if(secType.equalsIgnoreCase("BG"))
							{
								OTH_COLLAT += amount;
							}
						}
						rset.close();
						stmt.close();
						
						queryString2 ="SELECT NVL(A.VALUE,0),A.CURRENCY,A.SEC_TYPE,A.SEC_REF_NO,TO_CHAR(A.EXPIRE_DT,'DD/MM/YYYY'),"
								+ "B.SHARE_PERCENT,A.GUARANTOR_CD "
								+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.GX=? "
								+ "AND B.AGMT_NO=? AND B.CONT_NO=? AND B.CONTRACT_TYPE=? "
								+ "AND A.SEC_CATEGORY='R' AND SEC_TYPE IN ('PCG') AND STATUS IN ('O','C','R') "
								+ "AND ISSUE_DT<=to_date(?,'DD/MM/YYYY') AND (EXPIRE_DT>=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND (TO_DATE(TO_CHAR(CANCEL_DT,'DD/MM/YYYY'),'DD/MM/YYYY')-1 >= TO_DATE(?,'DD/MM/YYYY') OR CANCEL_DT IS NULL)) "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.SEQ_NO=B.SEQ_NO "
								+ "AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, companyCd);
						stmt2.setString(2, countpty_cd);
						stmt2.setString(3, gx);
						stmt2.setString(4, agmt);
						stmt2.setString(5, cont);
						stmt2.setString(6, cont_type);
						stmt2.setString(7, report_dt);
						stmt2.setString(8, report_dt);
						stmt2.setString(9, report_dt);
						rset2=stmt2.executeQuery();
						while(rset2.next())
						{
							double secuVal=rset2.getDouble(1);
							double oriSecuVal=secuVal;
							String currency=rset2.getString(2)==null?"":rset2.getString(2);
							String secType=rset2.getString(3)==null?"":rset2.getString(3);
							String secRef=rset2.getString(4)==null?"":rset2.getString(4);
							secRef=companyCd+"-"+secRef;
							
							double split_percent=rset2.getDouble(6);
							String splitPercent=rset2.getString(6)==null?"":rset2.getString(6);
							String guarantor_cd=rset2.getString(7)==null?"":rset2.getString(7);
							
							if(pcg_info.equals(""))
							{
								pcg_info = secType+" : "+secRef;
							}
							else
							{
								pcg_info += "\n"+secType+" : "+secRef;
							}
							
							if(!splitPercent.equals(""))
							{
								secuVal=(secuVal*split_percent)/100;
								if(currency.equals("2"))
								{
									pcg_info +="("+split_percent+"% of "+nf.format(oriSecuVal)+" USD)";
								}
								else
								{
									pcg_info +="("+split_percent+"% of "+nf.format(oriSecuVal)+" INR)";
								}
							}
							
							double amount=0;
							if(currency.equals("2"))
							{
								amount=secuVal*exchng_rate;
								pcg_info +=" = ( "+nf.format(secuVal)+" USD * "+nf2.format(exchng_rate)+") = "+nf.format(amount)+" INR";
							}
							else
							{
								amount=secuVal;
								pcg_info +=" = "+nf.format(amount)+" INR";
							}
							
							pcg_value+=amount;
							
							if(guarantor_cd.equals(""))
							{
								pcg_deal_value+=amount;
							}
							else
							{
								pcg_uncapped_value+=amount;
							}
							
							if(secType.equalsIgnoreCase("PCG"))
							{
								OTH_COLLAT += amount;
							}
						}
						rset2.close();
						stmt2.close();
						
						queryString3 ="SELECT NVL(A.VALUE,0),A.CURRENCY,A.SEC_TYPE,A.SEC_REF_NO,TO_CHAR(A.RECEIPT_DT,'DD/MM/YYYY'),"
								+ "B.SHARE_PERCENT,A.CR_DR,NVL(A.TDS_AMT,0) "
								+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.GX=? "
								+ "AND B.AGMT_NO=? AND B.CONT_NO=? AND B.CONTRACT_TYPE=? "
								+ "AND SEC_TYPE IN ('ADV','DPT') AND STATUS IN ('O') "
								+ "AND RECEIPT_DT<=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.SEQ_NO=B.SEQ_NO "
								+ "AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX";
						stmt3 = conn.prepareStatement(queryString3);
						stmt3.setString(1, companyCd);
						stmt3.setString(2, countpty_cd);
						stmt3.setString(3, gx);
						stmt3.setString(4, agmt);
						stmt3.setString(5, cont);
						stmt3.setString(6, cont_type);
						stmt3.setString(7, report_dt);
						rset3=stmt3.executeQuery();
						while(rset3.next())
						{
							double secuVal=rset3.getDouble(1);
							double tdsamt=rset3.getDouble(8);
							
							secuVal=secuVal+tdsamt;
							
							double oriSecuVal=secuVal;
							String currency=rset3.getString(2)==null?"":rset3.getString(2);
							String secType=rset3.getString(3)==null?"":rset3.getString(3);
							String secRef=rset3.getString(4)==null?"":rset3.getString(4);
							secRef=companyCd+"-"+secRef;
							
							double split_percent=rset3.getDouble(6);
							String splitPercent=rset3.getString(6)==null?"":rset3.getString(6);
							String crdr=rset3.getString(7)==null?"":rset3.getString(7);
							
							if(cash_collateral_info.equals(""))
							{
								cash_collateral_info = secType+" : "+secRef;
							}
							else
							{
								cash_collateral_info += "\n"+secType+" : "+secRef;
							}
							
							if(crdr.equals("CR"))
							{
								cash_collateral_info +=" CR(+) ";
							}
							else if(crdr.equals("DR"))
							{
								cash_collateral_info +=" DR(-) ";
								secuVal*=-1;
							}
							
							if(!splitPercent.equals(""))
							{
								secuVal=(secuVal*split_percent)/100;
								if(currency.equals("2"))
								{
									cash_collateral_info +="("+split_percent+"% of "+nf.format(oriSecuVal)+" USD)";
								}
								else
								{
									cash_collateral_info +="("+split_percent+"% of "+nf.format(oriSecuVal)+" INR)";
								}
							}
							
							double amount=0;
							if(currency.equals("2"))
							{
								amount=secuVal*exchng_rate;
								cash_collateral_info +=" = ( "+nf.format(secuVal)+" USD * "+nf2.format(exchng_rate)+") = "+nf.format(amount)+" INR";
							}
							else
							{
								amount=secuVal;
								cash_collateral_info +=" = "+nf.format(amount)+" INR";
							}
							
							cash_collateral_value+=amount;
						}
						rset3.close();
						stmt3.close();
					}
					
					VCOLLATERAL_INFO.add(collateral_info);
					VLC_AMT.add(nf.format(LC_AMT));
					VOTH_COLLAT.add(nf.format(OTH_COLLAT));
					
					VCASH_COLLATERAL_INFO.add(cash_collateral_info);
					VPCG_INFO.add(pcg_info);
					
					double net_exposure = gross_exposure_incl_tax - (collateral_value + cash_collateral_value);
					if(net_exposure<0)
					{
						net_exposure=0;
					}
					double limit=0;
					double credit_exceed=net_exposure-limit;
					if(credit_exceed<0)
					{
						credit_exceed=0;
					}
					
					VBILLED_AMT.add(nf.format(billedAmt));
					VBILLED_QTY.add("Billed MMBTU : "+nf.format(billedQty));
					VBILLED_AMT_INFO.add(billedAmtInfo);
					VUNBILLED_AMT.add(nf.format(unbilledAmt));
					VUNBILLED_AMT_INFO.add(unbilledAmtInfo);
					VUNBILLED_CURRENT_MONTH.add(nf.format(undeliveredCurrentMonthAmt));
					VUNBILLED_CURRENT_MONTH_INFO.add(undeliveredAmtInfo);
					VFORWARD_NOTIONAL.add(nf.format(forwardNotionalNextMonthAmt));
					VFORWARD_NOTIONAL_INFO.add(fwdNotionalAmtInfo);
					VGROSS_EXPOSURE.add(nf.format(gross_exposure));
					VGROSS_EXPOSURE_INFO.add("Gross Exposure = "+nf.format(billedAmt)+" + "+nf.format(unbilledAmt)+" = "+nf.format(gross_exposure));
					VGROSS_EXPOSURE_TAX.add(nf.format(gross_exposure_incl_tax));
					VGROSS_EXPOSURE_TAX_INFO.add("Account Recivable(Incl. Tax) : "+nf.format(billedAmtWithTax)+"\nUnbilled Recivable(Incl. Tax) : "+nf.format(unbilledAmtWithTax)+"\n\nGross Exposure incl Tax : "+nf.format(gross_exposure_incl_tax));
					VCOLLATERAL_VALUE.add(nf.format(collateral_value));
					VCASH_COLLATERAL_VALUE.add(nf.format(cash_collateral_value));
					VNET_EXPOSURE.add(nf.format(net_exposure));
					VNET_EXPOSURE_INFO.add("Net Exposure = "+nf.format(gross_exposure_incl_tax)+" - ("+nf.format(collateral_value)+" + "+nf.format(cash_collateral_value)+") = "+nf.format(net_exposure));
					VPCG_VALUE.add(nf.format(pcg_value));
					VPCG_UNCAPPED_VALUE.add(nf.format(pcg_uncapped_value));
					VPCG_DEAL_VALUE.add(nf.format(pcg_deal_value));
					VEXCHG_RATE.add(exchng_rate);
					
					double limit_amt=0;
					String gx_counterparty_cd="";
					if(gx.equals("I")) //FOR IGX DEAL ONLY
					{
						gx_counterparty_cd=utilBean.getGasExchangeCd(conn,"IGX");
						limit_value.put(""+gx_counterparty_cd+"-"+gx, limit_amt);
					}
					else
					{
						limit_value.put(""+countpty_cd+"-"+gx, limit_amt);
					}
					VGX_COUNTERPARTY_CD.add(gx_counterparty_cd);
					
					double limitValue=0;
					double percentageOwnship = 0;double parentlimitValue=0; 
					String parent_cd = "",limitParentFlag="N"; 
					if(!gx.equals("I")) //FOR IGX - PARENT LIMIT IS NOT APPLICABLE
					{
						queryString2="SELECT PARENT_OWNSHIP_CD "
								+ "FROM FMS_LIMIT_MST"
								+ " WHERE COUNTERPARTY_CD=? AND GX=? "
								+ "AND PARENT_ENT_DT <= TO_DATE(?,'DD/MM/YYYY') AND (PARENT_EXIT_DT >= TO_DATE(?,'DD/MM/YYYY') OR PARENT_EXIT_DT IS NULL)";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(1, countpty_cd);
						stmt2.setString(2, gx);
						stmt2.setString(3, report_dt);
						stmt2.setString(4, report_dt);
						rset2=stmt2.executeQuery();
						if(rset2.next())
						{
							parent_cd = rset2.getString(1)==null?"":rset2.getString(1);
							limitParentFlag="Y";
						}
						rset2.close();
						stmt2.close();
						
						queryString3 ="SELECT SUM(AMT) "
								+ "FROM FMS_LIMIT_DTL "
								+ "WHERE COUNTERPARTY_CD=? AND GX=? "
								+ "AND EFF_DT<=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND ((EXP_DT IS NULL AND INACTIVATION_DT IS NULL) OR (EXP_DT >= TO_DATE(?,'DD/MM/YYYY') "
								+ "AND INACTIVATION_DT IS NULL) OR ((EXP_DT >= TO_DATE(?,'DD/MM/YYYY') OR EXP_DT IS NULL) "
								+ "AND INACTIVATION_DT-1 >= TO_DATE(?,'DD/MM/YYYY'))) "
								+ "AND ((ACTION_TYPE=?) OR (ACTION_TYPE=? AND LIMIT_TYPE != ?))";
						stmt3 = conn.prepareStatement(queryString3);
						stmt3.setString(1, parent_cd);
						stmt3.setString(2, gx);
						stmt3.setString(3, report_dt);
						stmt3.setString(4, report_dt);
						stmt3.setString(5, report_dt);
						stmt3.setString(6, report_dt);
						stmt3.setString(7, "Adjust Limit");
						stmt3.setString(8, "Adjust Usage");
						stmt3.setString(9, "Unsecured");
						rset3=stmt3.executeQuery();
						if(rset3.next())
						{
							parentlimitValue = rset3.getDouble(1);
						}
						rset3.close();
						stmt3.close();
					}
					
					VPARENT_CD.add(parent_cd);
					VLIMIT_PARENT_FLAG.add(limitParentFlag);
					
					if(!parent_cd.equals("")) 
					{
						parent_limit_value.put(parent_cd+"-"+gx, parentlimitValue);
					}
					
					if(parentlimitValue > 0)
					{
						VPARENT_LIMIT_VALUE.add(nf.format(parentlimitValue));
					}else{
						VPARENT_LIMIT_VALUE.add("");
					}
					VLIMIT_VALUE_LINKED.add(nf.format(limitValue));
				}
				else if(expo_type.equals("P"))
				{
					VBILLED_AMT.add("");
					VUNBILLED_AMT.add("");
					VUNBILLED_CURRENT_MONTH.add("");
					VFORWARD_NOTIONAL.add("");
					VGROSS_EXPOSURE.add("");
					VGROSS_EXPOSURE_TAX.add("");
					VCOLLATERAL_VALUE.add("");
					VCASH_COLLATERAL_VALUE.add("");
					VNET_EXPOSURE.add("");
					VLIMIT.add("");
					VCREDIT_EXCEED.add("");
					VNET_EXPOSURE_USD.add("");
					VCREDIT_EXCEED_USD.add("");
				}
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	double billedQty=0;
	double billedAmtWithTax=0;
	String billedAmtInfo="";
	public double getAccountReceivable(String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String cont_type, String reportDt)
	{
		String function_nm="getAccountReceivable()";
		double billed_amt=0;
		billedQty=0;
		billedAmtInfo="";
		billedAmtWithTax=0;
		try
		{
			double total_gross_amt=0;
			double total_tax_amt=0;
			double total_tcs_amt=0;
			double total_tds_amt=0;
			double total_pay_recev_amt=0;
			double total_invoice_amt=0;
			double total_net_payable=0;
			double total_qty=0;
			double total_short_amt=0;
			double temp_total_short_amt=0;
			
			billedAmtInfo+="<thead>";
			billedAmtInfo+="<tr style='font-weight:bold; background:#cff4fc;'>";
			billedAmtInfo+="<th align='center' colspan='13'>Invoice Details till "+reportDt+"</th>";
			billedAmtInfo+="</tr>";
			billedAmtInfo+="<tr>";
			billedAmtInfo+="<th align='center'>Invoice Date</th>";
			billedAmtInfo+="<th align='center'>Invoice#</th>";
			billedAmtInfo+="<th align='center'>MMBTU</th>";
			billedAmtInfo+="<th align='center'>Gross Amount</th>";
			billedAmtInfo+="<th align='center'>Tax</th>";
			billedAmtInfo+="<th align='center'>Invoice Amount</th>";
			billedAmtInfo+="<th align='center'>TCS(+)</th>";
			billedAmtInfo+="<th align='center'>TDS(-)</th>";
			billedAmtInfo+="<th align='center'>Net Amount</th>";
			billedAmtInfo+="<th align='center'>Due Date</th>";
			billedAmtInfo+="<th align='center'>Payment Date</th>";
			billedAmtInfo+="<th align='center'>Payment Recv. Amount</th>";
			billedAmtInfo+="<th align='center'>Short Amount</th>";
			billedAmtInfo+="</tr>";
			billedAmtInfo+="</thead>";
			billedAmtInfo+="<tbody>";
			queryString="SELECT (NVL(GROSS_AMT,0) + NVL(TRANSPORTATION_AMOUNT,0) + NVL(MARKET_MARGIN_AMT,0) + NVL(OTHER_CHARGES_AMT,0)),"
					+ "TAX_AMT,TCS_AMT,TDS_GROSS_AMT,PAY_RECV_AMT,TO_CHAR(PAY_RECV_DT,'DD/MM/YYYY'),"
					+ "INVOICE_AMT,NET_PAYABLE_AMT,ALLOC_QTY,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),INVOICE_NO,"
					+ "TO_CHAR(DUE_DT,'DD/MM/YYYY') "
					+ "FROM FMS_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND CONT_NO=? "
					+ "AND CONTRACT_TYPE=? AND INVOICE_DT<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND PDF_INV_DTL IS NOT NULL "
					+ "ORDER BY INVOICE_DT DESC "; 
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, cont_no);
			stmt.setString(5, cont_type);
			stmt.setString(6, reportDt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				billedAmtInfo+="<tr>";
				double gross_amt=rset.getDouble(1);
				double tax_amt=rset.getDouble(2);
				double tcs_amt=rset.getDouble(3);
				double tds_amt=rset.getDouble(4);
				double pay_recev_amt=rset.getDouble(5);
				String pay_recev_dt=rset.getString(6)==null?"":rset.getString(6);
				double invoice_amt=rset.getDouble(7);
				double net_payable=rset.getDouble(8);
				double qty=rset.getDouble(9);
				String inv_dt=rset.getString(10)==null?"":rset.getString(10);
				String inv_no=rset.getString(11)==null?"":rset.getString(11);
				String due_dt=rset.getString(12)==null?"":rset.getString(12);
				billedQty+=qty;
				
				net_payable=net_payable-tds_amt;
				
				total_gross_amt+=gross_amt;
				total_tax_amt+=tax_amt;
				total_tcs_amt+=tcs_amt;
				total_tds_amt+=tds_amt;
				total_pay_recev_amt+=pay_recev_amt;
				total_invoice_amt+=invoice_amt;
				total_net_payable+=net_payable;
				total_qty+=qty;
				
				billedAmtInfo+="<td align='center'>"+inv_dt+"</td>";
				billedAmtInfo+="<td align='center'>"+inv_no+"</td>";
				
				billedAmtInfo+="<td align='right'>"+nf.format(qty)+"</td>";
				billedAmtInfo+="<td align='right'>"+nf.format(gross_amt)+"</td>";
				billedAmtInfo+="<td align='right'>"+nf.format(tax_amt)+"</td>";
				billedAmtInfo+="<td align='right'>"+nf.format(invoice_amt)+"</td>";
				billedAmtInfo+="<td align='right'>"+nf.format(tcs_amt)+"</td>";
				billedAmtInfo+="<td align='right'>"+nf.format(tds_amt)+"</td>";
				billedAmtInfo+="<td align='right'>"+nf.format(net_payable)+"</td>";
				billedAmtInfo+="<td align='center'>"+due_dt+"</td>";
				billedAmtInfo+="<td align='center'>"+pay_recev_dt+"</td>";
				billedAmtInfo+="<td align='right'>"+nf.format(pay_recev_amt)+"</td>";			
				
				double due_amt=0;
				double short_amt=0;
				int pay_recev_count=utilDate.getDays(pay_recev_dt, reportDt);
				
				if(pay_recev_count<=1 && !pay_recev_dt.equals(""))
				{
					due_amt=(gross_amt) - ((pay_recev_amt+tds_amt) - (tax_amt + tcs_amt));
					short_amt=net_payable - pay_recev_amt;
				}
				else
				{
					due_amt=gross_amt;
					short_amt=net_payable;
				}
				
				if(short_amt<=CommonVariable.receivable_tolerance) //THIS FOR GROSS EXPO INCL. TAX
				{
					temp_total_short_amt+=0;
				}
				else
				{
					temp_total_short_amt+=short_amt;
				}
				
				total_short_amt+=short_amt;
				billedAmtInfo+="<td align='right'>"+nf.format(short_amt)+"</td>";
				
				if(due_amt<=CommonVariable.receivable_tolerance)
				{
					due_amt=0;
				}
				
				billed_amt+=due_amt;
				
				billedAmtInfo+="</tr>";
			}
			rset.close();
			stmt.close();
			
			//FROM F_FLOW
			queryString="SELECT GROSS_AMT_INR,TAX_AMT,TCS_AMT,TDS_GROSS_AMT,PAY_RECV_AMT,TO_CHAR(PAY_RECV_DT,'DD/MM/YYYY'),"
					+ "INVOICE_AMT,INVOICE_TYPE,NET_PAYABLE_AMT,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),INVOICE_NO,"
					+ "TO_CHAR(DUE_DT,'DD/MM/YYYY'),ALLOC_QTY "
					+ "FROM FMS_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND CONT_NO=? "
					+ "AND CONTRACT_TYPE=? AND INVOICE_DT<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND PDF_INV_DTL IS NOT NULL AND INVOICE_TYPE IN (?,?,?,?)";
			stmt1 = conn.prepareStatement(queryString);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_cd);
			stmt1.setString(3, agmt_no);
			stmt1.setString(4, cont_no);
			stmt1.setString(5, cont_type);
			stmt1.setString(6, reportDt);
			stmt1.setString(7, "CDR");
			stmt1.setString(8, "DR");
			stmt1.setString(9, "LP");
			stmt1.setString(10, "S");
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				billedAmtInfo+="<tr>";
				double gross_amt=rset1.getDouble(1);
				double tax_amt=rset1.getDouble(2);
				double tcs_amt=rset1.getDouble(3);
				double tds_amt=rset1.getDouble(4);
				double pay_recev_amt=rset1.getDouble(5);
				String pay_recev_dt=rset1.getString(6)==null?"":rset1.getString(6);
				double invoice_amt=rset1.getDouble(7);
				String invoice_type=rset1.getString(8)==null?"":rset1.getString(8);
				double net_payable=rset1.getDouble(9);
				String inv_dt=rset1.getString(10)==null?"":rset1.getString(10);
				String inv_no=rset1.getString(11)==null?"":rset1.getString(11);
				String due_dt=rset1.getString(12)==null?"":rset1.getString(12);
				double ff_qty=rset1.getDouble(13);
				
				net_payable=net_payable-tds_amt;
				
				total_gross_amt+=gross_amt;
				total_tax_amt+=tax_amt;
				total_tcs_amt+=tcs_amt;
				total_tds_amt+=tds_amt;
				total_pay_recev_amt+=pay_recev_amt;
				total_invoice_amt+=invoice_amt;
				total_net_payable+=net_payable;
				total_qty+=ff_qty;
				
				billedAmtInfo+="<td align='center'>"+inv_dt+"</td>";
				billedAmtInfo+="<td align='center'>"+inv_no+"</td>";
				
				billedAmtInfo+="<td align='right'>"+nf.format(ff_qty)+"</td>";
				billedAmtInfo+="<td align='right'>"+nf.format(gross_amt)+"</td>";
				billedAmtInfo+="<td align='right'>"+nf.format(tax_amt)+"</td>";
				billedAmtInfo+="<td align='right'>"+nf.format(invoice_amt)+"</td>";
				billedAmtInfo+="<td align='right'>"+nf.format(tcs_amt)+"</td>";
				billedAmtInfo+="<td align='right'>"+nf.format(tds_amt)+"</td>";
				billedAmtInfo+="<td align='right'>"+nf.format(net_payable)+"</td>";
				billedAmtInfo+="<td align='center'>"+due_dt+"</td>";
				billedAmtInfo+="<td align='center'>"+pay_recev_dt+"</td>";
				billedAmtInfo+="<td align='right'>"+nf.format(pay_recev_amt)+"</td>";			
								
				double due_amt=0;
				double short_amt=0;
				
				int pay_recev_count=utilDate.getDays(pay_recev_dt, reportDt);
				
				if(pay_recev_count<=1 && !pay_recev_dt.equals(""))
				{
					due_amt=(gross_amt) - ((pay_recev_amt+tds_amt) - (tax_amt + tcs_amt));
					short_amt=net_payable - pay_recev_amt;
				}
				else
				{
					due_amt=gross_amt;
					short_amt=net_payable;
				}
				
				if(short_amt<=CommonVariable.receivable_tolerance) //THIS FOR GROSS EXPO INCL. TAX
				{
					temp_total_short_amt+=0;
				}
				else
				{
					temp_total_short_amt+=short_amt;
				}
				
				total_short_amt+=short_amt;
				billedAmtInfo+="<td align='right'>"+nf.format(short_amt)+"</td>";
				
				if(due_amt<=CommonVariable.receivable_tolerance)
				{
					due_amt=0;
				}
				billed_amt+=due_amt;
				billedAmtInfo+="</tr>";
			}
			rset1.close();
			stmt1.close();
			
			billedAmtWithTax=temp_total_short_amt; //THIS FOR GROSS EXPO INCL. TAX
			
			billedAmtInfo+="<tr style='font-weight:bold; background:#cff4fc;'>";
			billedAmtInfo+="<td colspan='2' align='right'>Total : </td>";
			billedAmtInfo+="<td align='right'>"+nf.format(total_qty)+"</td>";
			billedAmtInfo+="<td align='right'>"+nf.format(total_gross_amt)+"</td>";
			billedAmtInfo+="<td align='right'>"+nf.format(total_tax_amt)+"</td>";
			billedAmtInfo+="<td align='right'>"+nf.format(total_invoice_amt)+"</td>";
			billedAmtInfo+="<td align='right'>"+nf.format(total_tcs_amt)+"</td>";
			billedAmtInfo+="<td align='right'>"+nf.format(total_tds_amt)+"</td>";
			billedAmtInfo+="<td align='right'>"+nf.format(total_net_payable)+"</td>";
			billedAmtInfo+="<td align='center' colspan='2'></td>";
			billedAmtInfo+="<td align='right'>"+nf.format(total_pay_recev_amt)+"</td>";
			billedAmtInfo+="<td align='right'>"+nf.format(total_short_amt)+"</td>";
			billedAmtInfo+="</tr>";
			billedAmtInfo+="<tr style='font-weight:bold;'>";
			billedAmtInfo+="<td colspan='13' align='center' style='color:green;'>"
					+ "Account Receivable = (Gross Amount) - ((Pay Received Amount + TDS Amount) - (Tax Amount + TCS Amount)) = "+nf.format(billed_amt)+"</td>";
			billedAmtInfo+="</tr>";
			billedAmtInfo+="</tbody>";
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return billed_amt;
	}
	
	double unbilledQty=0;
	String unbilledAmtInfo="";
	double unbilledAmtWithTax=0;
	public double getUnbilledReceivable(String comp_cd, String counterparty_cd, String agmt_no, String agmt_rev, String cont_no, String cont_rev, String cont_type, 
			String agmt_base,String reportDt, String cont_st_dt, String cont_end_dt,String rate, String rate_unit)
	{
		String function_nm="getUnbilledReceivable()";
		double unbilled_amt=0;
		unbilledQty=0;
		unbilledAmtInfo="";
		unbilledAmtWithTax=0;
		try
		{
			String previousDate=utilDate.getDate(reportDt, "-1");
			int isExpire=utilDate.getDays(cont_end_dt, previousDate);
			if(isExpire<1)
			{
				previousDate=cont_end_dt;
			}
			
			String headInfo="Unbilled Amount Calculated From "+cont_st_dt+" to "+previousDate;
			
			unbilledAmtInfo+="<thead>";
			unbilledAmtInfo+="<tr style='font-weight:bold; background:#cff4fc;'>";
			if(rate_unit.equals("2"))
			{
				unbilledAmtInfo+="<th colspan='14' align='right'>"+headInfo+"</th>";
			}
			else
			{
				unbilledAmtInfo+="<th colspan='13' align='right'>"+headInfo+"</th>";
			}
			unbilledAmtInfo+="</tr>";
			unbilledAmtInfo+="<tr>";
			unbilledAmtInfo+="<th>Gas Day</th>";
			unbilledAmtInfo+="<th>Business Unit</th>";
			unbilledAmtInfo+="<th>Plant</th>";
			unbilledAmtInfo+="<th>Price Rate<br>("+utilBean.getRateUnitNm(conn,rate_unit)+"/MMBTU)</th>";
			if(rate_unit.equals("2"))
			{
				unbilledAmtInfo+="<th>Exchange Rate</th>";
			}
			unbilledAmtInfo+="<th>Unbilled MMBTU</th>";
			unbilledAmtInfo+="<th>Amount</th>";
			for(int i=0; i<VCHARGE_ABBR.size(); i++)
			{
				unbilledAmtInfo+="<th>"+VCHARGE_NAME.elementAt(i)+"</th>";
			}
			unbilledAmtInfo+="<th>Unbilled Amount</th>";
			unbilledAmtInfo+="<th>Tax Structure</th>";
			unbilledAmtInfo+="<th>Tax Amount</th>";
			unbilledAmtInfo+="<th>Unbilled Amount<br>(Incl. Tax)</th>";
			unbilledAmtInfo+="</tr>";
			unbilledAmtInfo+="</thead>";
			unbilledAmtInfo+="<tbody>";
			double total_charges=0;
			double total_amt=0;
			double total_trans_amt=0;
			double total_mktmrg_amt=0;
			double total_chngoth_amt=0;
			double total_tax_amt=0;
			queryString="SELECT SUM(QTY_MMBTU),TO_CHAR(GAS_DT,'DD/MM/YYYY'),PLANT_SEQ,BU_SEQ "
	  				+ "FROM FMS_DAILY_ALLOCATION_DTL A "
	  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
					+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONTRACT_TYPE=? "
					+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
					+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
					+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) "
					+ "AND (SELECT COUNT(*) FROM FMS_INVOICE_MST B WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_UNIT=A.BU_SEQ "
					+ "AND B.PERIOD_START_DT<=A.GAS_DT AND B.PERIOD_END_DT>=A.GAS_DT AND B.INVOICE_DT<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND B.PDF_INV_DTL IS NOT NULL) = 0 "
					+ "GROUP BY GAS_DT,PLANT_SEQ,BU_SEQ "
					+ "ORDER BY GAS_DT DESC ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, cont_no);
			stmt.setString(2, agmt_no);
			stmt.setString(3, comp_cd);
			stmt.setString(4, counterparty_cd);
			stmt.setString(5, cont_type);
			stmt.setString(6, cont_st_dt);
			stmt.setString(7, previousDate);
			stmt.setString(8, reportDt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				unbilledAmtInfo+="<tr>";
				double qty=rset.getDouble(1);
				String gas_dt=rset.getString(2)==null?"":rset.getString(2);
				String plant_seq=rset.getString(3)==null?"":rset.getString(3);
				String bu_seq=rset.getString(4)==null?"":rset.getString(4);
				unbilledAmtInfo+="<td align='center'>"+gas_dt+"</td>";
				unbilledAmtInfo+="<td align='center'>"+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_seq, "B")+"</td>";
				unbilledAmtInfo+="<td align='center'>"+utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq, "C")+"</td>";
				
				unbilledQty+=qty;
				
				String new_price=getSalesRate(comp_cd, counterparty_cd, agmt_no, cont_no, cont_type, gas_dt);
				if(new_price.equals(""))
				{
					new_price=rate;
				}
				unbilledAmtInfo+="<td align='right'>"+new_price+"</td>";

				double amt=(qty * Double.parseDouble(new_price));
				
				if(rate_unit.equals("2"))
				{
					double exchng_rate=getExchangeRate(comp_cd, counterparty_cd, agmt_no, cont_no, cont_type, gas_dt);
					unbilledAmtInfo+="<td align='right'>"+nf2.format(exchng_rate)+"</td>";
					total_amt += amt * exchng_rate;
				}
				else
				{
					total_amt+=amt;
				}
				unbilledAmtInfo+="<td align='right'>"+nf.format(qty)+"</td>";
				unbilledAmtInfo+="<td align='right'>"+nf.format(amt)+"</td>";
				//System.out.println(""+gas_dt+"======"+nf.format(qty)+"====="+plant_seq+"====="+new_price+"===="+nf.format(qty * Double.parseDouble(new_price)));
				
				double temp_total_charges=0;
				Vector charges = getOtherCharges(comp_cd, counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, cont_type, plant_seq,gas_dt);
				if(!charges.elementAt(0).equals(""))
				{
					double temp=(qty * Double.parseDouble(""+charges.elementAt(0)));
					temp_total_charges+=temp;
					total_charges+=temp;
					total_trans_amt+=temp;
					unbilledAmtInfo+="<td align='right' title='"+charges.elementAt(0)+" INR/MMBTU'>"+nf.format(temp)+"</td>";
				}
				else
				{
					unbilledAmtInfo+="<td align='right'></td>";
				}
				if(!charges.elementAt(1).equals(""))
				{
					double temp=(qty * Double.parseDouble(""+charges.elementAt(1)));
					temp_total_charges+=temp;
					total_charges+=temp;
					total_mktmrg_amt+=temp;
					unbilledAmtInfo+="<td align='right' title='"+charges.elementAt(1)+" INR/MMBTU'>"+nf.format(temp)+"</td>";
				}
				else
				{
					unbilledAmtInfo+="<td align='right'></td>";
				}
				if(!charges.elementAt(2).equals(""))
				{
					double temp=(qty * Double.parseDouble(""+charges.elementAt(2)));
					temp_total_charges+=temp;
					total_charges+=temp;
					total_chngoth_amt+=temp;
					unbilledAmtInfo+="<td align='right' title='"+charges.elementAt(2)+" INR/MMBTU'>"+nf.format(temp)+"</td>";
				}
				else
				{
					unbilledAmtInfo+="<td align='right'></td>";
				}
				unbilledAmtInfo+="<td align='right'>"+nf.format(amt+temp_total_charges)+"</td>";
				
				String billingFrq="";
				queryString2="SELECT BILLING_FREQ "
						+ "FROM FMS_SUPPLY_BILLING_DTL A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev+"' "
						+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev+"' "
						+ "AND CONTRACT_TYPE=? "
						+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_SUPPLY_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND TO_DATE(?,'DD/MM/YYYY') > EFF_DT)";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, counterparty_cd);
				stmt2.setString(3, agmt_no);
				stmt2.setString(4, cont_no);
				stmt2.setString(5, cont_type);
				stmt2.setString(6, gas_dt);
				rset2=stmt2.executeQuery();
				if(rset2.next())
		  		{
		  			billingFrq=rset2.getString(1)==null?"":rset2.getString(1);
		  		}
				rset2.close();
				stmt2.close();
				
				String periodStDt=utilBean.getFirstDtOfBillingCycle(billingFrq, "", gas_dt);
				
				double temp_unbilled_amt=amt+temp_total_charges;
				Vector temp = new Vector();
				temp=TaxCalc.TaxAmountCalculationWithInfo(conn, comp_cd, counterparty_cd, "C", plant_seq, bu_seq, periodStDt, nf.format(temp_unbilled_amt));
				double tax_amt = Double.parseDouble(""+temp.elementAt(0));
				//tax_struct_cd = ""+temp.elementAt(1);
				//tax_struct_dt = ""+temp.elementAt(2);
				//tax_info = ""+temp.elementAt(3);
				String tax_struct_dtl = ""+temp.elementAt(4);
				//tax_factor = ""+temp.elementAt(5);
				double temp_unbilled_amtWithTax=tax_amt+temp_unbilled_amt;
				
				total_tax_amt+=tax_amt;
				
				unbilledAmtInfo+="<td align='center'>"+tax_struct_dtl+"</td>";
				unbilledAmtInfo+="<td align='right'>"+nf.format(tax_amt)+"</td>";
				unbilledAmtInfo+="<td align='right'>"+nf.format(temp_unbilled_amtWithTax)+"</td>";
				unbilledAmtInfo+="</tr>";
			}
			rset.close();
			stmt.close();
			unbilled_amt=total_amt+total_charges;
			unbilledAmtWithTax=unbilled_amt+total_tax_amt;
			
			unbilledAmtInfo+="<tr style='font-weight:bold; background:#cff4fc;'>";
			if(rate_unit.equals("2"))
			{
				unbilledAmtInfo+="<td colspan='5' align='right'>Total : </td>";
			}
			else
			{
				unbilledAmtInfo+="<td colspan='4' align='right'>Total : </td>";
			}
			unbilledAmtInfo+="<td align='right'>"+nf.format(unbilledQty)+"</td>";
			unbilledAmtInfo+="<td align='right'>"+nf.format(total_amt)+"</td>";
			unbilledAmtInfo+="<td align='right'>"+nf.format(total_trans_amt)+"</td>";
			unbilledAmtInfo+="<td align='right'>"+nf.format(total_mktmrg_amt)+"</td>";
			unbilledAmtInfo+="<td align='right'>"+nf.format(total_chngoth_amt)+"</td>";
			unbilledAmtInfo+="<td align='right'>"+nf.format(unbilled_amt)+"</td>";
			unbilledAmtInfo+="<td align='right'></td>";
			unbilledAmtInfo+="<td align='right'>"+nf.format(total_tax_amt)+"</td>";
			unbilledAmtInfo+="<td align='right'>"+nf.format(unbilledAmtWithTax)+"</td>";
			unbilledAmtInfo+="</tr>";
			unbilledAmtInfo+="</tbody>";
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return unbilled_amt;
	}
	
	double undeliveredQty=0;
	String undeliveredAmtInfo="";
	public double getUndeliveredCurrentMonth(String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String cont_type, 
			String agmt_base,String reportDt, String cont_st_dt,String cont_end_dt,String rate, String rate_unit,String dcq, String tcq, String dlvQty)
	{
		String function_nm="getUndeliveredCurrentMonth()";
		double undelivered_amt=0;
		undeliveredQty=0;
		undeliveredAmtInfo="";
		try
		{
			if(utilDate.getDays(reportDt, cont_end_dt)<=1) //IF CONTRACT IS NOT EXPIRED
			{
				String lastDateOfMonth = utilDate.getLastDateOfMonth(reportDt);
				int contExpiry = utilDate.getDays(lastDateOfMonth, cont_end_dt);//IF CONTRACT EXPIRED BEFORE MONTH END.
				if(contExpiry > 0)
				{
					lastDateOfMonth = cont_end_dt;
				}
				int no_of_days = utilDate.getDays(lastDateOfMonth, reportDt);
				
				
				double temp_dcq = 0;
				double total_UndeliveredQty =0;
			
				double amt=0;
				if(!rate.equals("") && !dcq.equals(""))
				{
					if(Double.parseDouble(dlvQty)<=Double.parseDouble(tcq))
					{
						total_UndeliveredQty = Double.parseDouble(dcq) * no_of_days;
						String tot_dlvQty=nf.format(total_UndeliveredQty+Double.parseDouble(dlvQty));
												
						if(Double.parseDouble(tot_dlvQty)>Double.parseDouble(tcq))
						{
							total_UndeliveredQty=Double.parseDouble(tcq) - Double.parseDouble(dlvQty);
						}
						
						undeliveredQty=total_UndeliveredQty;
						amt=total_UndeliveredQty*Double.parseDouble(rate);
						undeliveredAmtInfo+="TCQ : "+tcq+" DCQ : "+dcq;
						undeliveredAmtInfo+="\nDelivered MMBTU : "+dlvQty;
						undeliveredAmtInfo+="\n\nForward Notional Current Month Calculated From "+reportDt+" to "+lastDateOfMonth;
						undeliveredAmtInfo+="\n\nForward Notional Current Month MMBTU : "+nf.format(undeliveredQty);
						undeliveredAmtInfo+="\nPrice Rate : "+rate+" "+utilBean.getRateUnitNm(conn,rate_unit)+"/MMBTU";
						
						if(rate_unit.equals("2"))
						{
							double exchng_rate=getExchangeRate(comp_cd, counterparty_cd, agmt_no, cont_no, cont_type, lastDateOfMonth);
							undelivered_amt = amt * exchng_rate;
							undeliveredAmtInfo+="\nExchage Rate : "+nf.format(exchng_rate);
						}
						else
						{
							undelivered_amt=amt;
						}
						undeliveredAmtInfo+="\n\nForward Notional Current Month : "+nf.format(undelivered_amt);
					}
				}				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return undelivered_amt;
	}
	
	String fwdNotionalAmtInfo="";
	public double getForwardNotionalNextMonth(String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String cont_type, 
			String agmt_base, String reportDt, String cont_st_dt,String cont_end_dt,String rate, String rate_unit,String dcq,String tcq, String dlvQty)
	{
		String function_nm="getForwardNotionalNextMonth()";
		double fwdNotionalAmt=0;
		fwdNotionalAmtInfo="";
		try
		{
			String lastDateOfMonth = utilDate.getLastDateOfMonth(reportDt);
			String FirstDtOfNextMonth = utilDate.getDate(lastDateOfMonth, "1");
			int contExpiry = utilDate.getDays(cont_end_dt, FirstDtOfNextMonth);//IF CONTRACT EXPIRED AFTER THIS MONTH END.
			String lastDateOfNextMonth =utilDate.getLastDateOfMonth(FirstDtOfNextMonth);
			
			if(contExpiry > 0)
			{
				int contExpiry_2 = utilDate.getDays(lastDateOfNextMonth, cont_end_dt);//IF CONTRACT EXPIRED BEFORE NEXT MONTH END.
				if(contExpiry_2 > 0)
				{
					lastDateOfNextMonth=cont_end_dt;
				}
				int no_of_days = utilDate.getDays(lastDateOfNextMonth, FirstDtOfNextMonth);
								
				double temp_dcq = 0;
				double total_UndeliveredQty =0;
			
				double amt=0;
				if(!rate.equals("") && !dcq.equals(""))
				{
					if(Double.parseDouble(dlvQty)<=Double.parseDouble(tcq))
					{
						total_UndeliveredQty = Double.parseDouble(dcq) * no_of_days;
						String tot_dlvQty=nf.format(total_UndeliveredQty+Double.parseDouble(dlvQty));
						
						if(Double.parseDouble(tot_dlvQty)>Double.parseDouble(tcq))
						{
							total_UndeliveredQty=Double.parseDouble(tcq) - Double.parseDouble(dlvQty);
						}
						
						amt=total_UndeliveredQty*Double.parseDouble(rate);

						fwdNotionalAmtInfo+="TCQ : "+tcq+" DCQ : "+dcq;
						fwdNotionalAmtInfo+="\nDelivered MMBTU + Forward Notional Current Month MMBTU : "+dlvQty;
						fwdNotionalAmtInfo+="\n\nForward Notional Next Month Calculated From "+FirstDtOfNextMonth+" to "+lastDateOfNextMonth;
						fwdNotionalAmtInfo+="\n\nForward Notional Next Month MMBTU : "+nf.format(total_UndeliveredQty);
						fwdNotionalAmtInfo+="\nPrice Rate : "+rate+" "+utilBean.getRateUnitNm(conn,rate_unit)+"/MMBTU";
						
						if(rate_unit.equals("2"))
						{
							double exchng_rate=getExchangeRate(comp_cd, counterparty_cd, agmt_no, cont_no, cont_type, lastDateOfNextMonth);
							fwdNotionalAmt = amt * exchng_rate;
							fwdNotionalAmtInfo+="\nExchage Rate : "+nf.format(exchng_rate);
						}
						else
						{
							fwdNotionalAmt=amt;
						}
						fwdNotionalAmtInfo+="\n\nForward Notional Next Month : "+nf.format(fwdNotionalAmt);
					}
				}
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return fwdNotionalAmt;
	}
	
	public Vector getOtherCharges(String comp_cd, String counterparty_cd, String agmt_no, String agmt_rev_no, String cont_no, String cont_rev_no, String contract_type, String plant_seq, String date)
	{
		String function_nm="getOtherCharges()";
		Vector chrg = new Vector();
		
		try
		{
			for(int i=0;i<VCHARGE_ABBR.size();i++)
			{
				String chrg_abbr = ""+VCHARGE_ABBR.elementAt(i);

				String rate = "";
				queryString_temp = "SELECT A.CHARGE_RATE "
						+ "FROM FMS_SUPPLY_CONT_PLANT_CHRG A "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? "
						+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONTRACT_TYPE=? AND A.PLANT_SEQ_NO=? AND A.CHARGE_ABBR=? "
						+ "AND A.EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_SUPPLY_CONT_PLANT_CHRG B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO "
						+ "AND A.CHARGE_ABBR=B.CHARGE_ABBR AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
				try (PreparedStatement stmt_temp = conn.prepareStatement(queryString_temp)) 
				{
					stmt_temp.setString(1, comp_cd);
					stmt_temp.setString(2, counterparty_cd);
					stmt_temp.setString(3, cont_no);
					stmt_temp.setString(4, agmt_no);
					stmt_temp.setString(5, agmt_rev_no);
					stmt_temp.setString(6, contract_type);
					stmt_temp.setString(7, plant_seq);
					stmt_temp.setString(8, chrg_abbr);
					stmt_temp.setString(9, date);
					try (ResultSet rset_temp = stmt_temp.executeQuery()) 
					{
						if (rset_temp.next()) 
						{
							rate = rset_temp.getString(1) == null ? "" : nf.format(rset_temp.getDouble(1));
						}
					}
				}
				chrg.add(rate);  // Add the rate to the vector
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return chrg;
	}
	
	public Double getExchangeRate(String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String contract_type, String date)
	{
		String function_nm="getExchangeRate()";
		double exchangRate=0;
		try
		{
			String exchng_rate_cd="";
			String exchang_criteria="";
			String exchng_rate_cal="";
			String fixed_exchng_val="";
			
			queryString_temp="SELECT EXCHNG_RATE_CD,EXCHNG_CRITERIA,EXCHNG_RATE_CAL,"
					+ "EXCHG_VAL "
					+ "FROM FMS_SUPPLY_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? "//AND AGMT_REV='"+agmt_rev_no+"' "
					+ "AND CONT_NO=? "//AND CONT_REV='"+cont_rev_no+"' "
					+ "AND CONTRACT_TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_SUPPLY_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) ";
			stmt_temp = conn.prepareStatement(queryString_temp);
			stmt_temp.setString(1, comp_cd);
			stmt_temp.setString(2, counterparty_cd);
			stmt_temp.setString(3, agmt_no);
			stmt_temp.setString(4, cont_no);
			stmt_temp.setString(5, contract_type);
			stmt_temp.setString(6, date);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				exchng_rate_cd = rset_temp.getString(1)==null?"":rset_temp.getString(1);
				exchang_criteria = rset_temp.getString(2)==null?"":rset_temp.getString(2);
				exchng_rate_cal = rset_temp.getString(3)==null?"":rset_temp.getString(3);
				
				fixed_exchng_val=nf2.format(rset_temp.getDouble(4));
			}
			else
			{	
				fixed_exchng_val=nf2.format(0);
			}
			rset_temp.close();
			stmt_temp.close();
			
			if(exchng_rate_cd.equals("0")) //FOR FIXED EXCHANGE RATE
			{
				exchangRate=Double.parseDouble(fixed_exchng_val);
			}
			else
			{
				queryString_temp1="SELECT EXCHG_VAL "
						+ "FROM FMS_EXCHG_RATE_ENTRY A "
						+ "WHERE EXCHG_RATE_CD=? "
						+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B "
						+ "WHERE A.EXCHG_RATE_CD=B.EXCHG_RATE_CD AND B.EFF_DT <= TO_DATE(?,'DD/MM/YYYY'))";
				stmt_temp1 = conn.prepareStatement(queryString_temp1);
				stmt_temp1.setString(1, exchng_rate_cd);
				stmt_temp1.setString(2, date);
				rset_temp1=stmt_temp1.executeQuery();
				if(rset_temp1.next())
				{
					exchangRate=rset_temp1.getDouble(1);
				}
				rset_temp1.close();
				stmt_temp1.close();
			}
			
			if(exchangRate==0) //IF EXCHNG_RATE==0, DEFAULT 'Shell Treasury Rate' WILL BE CONSIDERED
			{
				String rate_nm="Shell Treasury Rate";
				
				queryString_temp1="SELECT EXC_RATE_CD "
						+ "FROM FMS_EXCHG_RATE_MST "
						+ "WHERE UPPER(EXC_RATE_NM) = ?";
				stmt_temp1 = conn.prepareStatement(queryString_temp1);
				stmt_temp1.setString(1, rate_nm.toUpperCase());
				rset_temp1=stmt_temp1.executeQuery();
				if(rset_temp1.next())
				{
					exchng_rate_cd = rset_temp1.getString(1)==null?"":rset_temp1.getString(1);
				}
				rset_temp1.close();
				stmt_temp1.close();
				
				queryString="SELECT EXCHG_VAL "
						+ "FROM FMS_EXCHG_RATE_ENTRY A "
						+ "WHERE EXCHG_RATE_CD=? "
						+ "AND EFF_DT =(SELECT MAX(B.EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B "
						+ "WHERE A.EXCHG_RATE_CD=B.EXCHG_RATE_CD AND B.EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) AND FLAG=?";
				stmt_temp1 = conn.prepareStatement(queryString);
				stmt_temp1.setString(1, exchng_rate_cd);
				stmt_temp1.setString(2, "Y");
				rset_temp1=stmt_temp1.executeQuery();
				if(rset_temp1.next())
				{
					exchangRate = rset_temp1.getDouble(1);
				}
				rset_temp1.close();
				stmt_temp1.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return exchangRate;
	}
	
	public String getSalesRate(String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String cont_type, String date)
	{
		String function_nm="getSalesRate()";
		String price="";
		try
		{
			queryString_temp = "SELECT DISTINCT NEW_SALE_PRICE "
					+ "FROM FMS_SUPPLY_ALLOC_REVISED A "
					+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND CONT_NO=? "
					+ "AND COUNTERPARTY_CD=? AND FLAG=? AND CONTRACT_TYPE=? "
					+ "AND MODIFICATION_SEQ_NO = (SELECT MAX(MODIFICATION_SEQ_NO) FROM FMS_SUPPLY_ALLOC_REVISED B "
					+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.FLAG=B.FLAG AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ "AND B.NEW_PRICE_EFF_DT <=TO_DATE(?,'DD/MM/YYYY'))";
			stmt_temp = conn.prepareStatement(queryString_temp);
			stmt_temp.setString(1, comp_cd);
			stmt_temp.setString(2, agmt_no);
			stmt_temp.setString(3, cont_no);
			stmt_temp.setString(4, counterparty_cd);
			stmt_temp.setString(5, "A");
			stmt_temp.setString(6, cont_type);
			stmt_temp.setString(7, date);
			rset_temp=stmt_temp.executeQuery();
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				price=rset_temp.getString(1)==null?"":rset_temp.getString(1);
			}
			rset_temp.close();
			stmt_temp.close();
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return price;
	}
	
	public void CR_LimitCalculation()
	{
		String function_nm="CR_LimitCalculation()";
		try
		{
			HashMap<String,Double> limit_info = new HashMap<String,Double>();
			Iterator o = limit_value.keySet().iterator();
		    while (o.hasNext()) 
		    {
		    	String key = (String) o.next();
		    	String temp[] = key.split("-");
		    	String countpty_cd=temp[0];
		    	String gx=temp[1];
		    	
				double limitValue=0;
				queryString ="SELECT SUM(AMT) "
						+ "FROM FMS_LIMIT_DTL "
						+ "WHERE COUNTERPARTY_CD=? AND GX=? "
						+ "AND EFF_DT<=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND ((EXP_DT IS NULL AND INACTIVATION_DT IS NULL) OR (EXP_DT >= TO_DATE(?,'DD/MM/YYYY') "
						+ "AND INACTIVATION_DT IS NULL) OR ((EXP_DT >= TO_DATE(?,'DD/MM/YYYY') OR EXP_DT IS NULL) "
						+ "AND INACTIVATION_DT-1 >= TO_DATE(?,'DD/MM/YYYY'))) "
						+ "AND ((ACTION_TYPE=?) OR (ACTION_TYPE=? AND LIMIT_TYPE != ?))";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, countpty_cd);
				stmt.setString(2, gx);
				stmt.setString(3, report_dt);
				stmt.setString(4, report_dt);
				stmt.setString(5, report_dt);
				stmt.setString(6, report_dt);
				stmt.setString(7, "Adjust Limit");
				stmt.setString(8, "Adjust Usage");
				stmt.setString(9, "Unsecured");
				rset=stmt.executeQuery();
				while(rset.next())
				{
					limitValue=rset.getDouble(1);
				}
				rset.close();
				stmt.close();
				
				limit_info.put(key, limitValue);
				limit_value.put(key, limitValue);
		    }
		    
		    Temp_CR_LimitCalculation();
		    
		    double netExposure=0;
		    double creditExceed=0;
		    double pcgValue=0;
		    double pcgDealValue=0; //HP20250328
		    
		    String countpty_cd="";
		    String limit_dtl_info="";
		    
		    for(int i=0; i<VCOUNTERPARTY_CD.size(); i++)
			{
		    	String gx=""+VGX.elementAt(i);
		    	countpty_cd=""+VCOUNTERPARTY_CD.elementAt(i)+"-"+gx;
		    	if(VGX.elementAt(i).equals("I"))
		    	{
		    		countpty_cd=utilBean.getGasExchangeCd(conn,"IGX")+"-"+gx;
		    	}
		    	String credit_exceed_info="";
			    
		    	if(!VNET_EXPOSURE.elementAt(i).equals(""))
				{
					netExposure=Double.parseDouble(""+VNET_EXPOSURE.elementAt(i));
				}
		    	
		    	credit_exceed_info+="Net Exposure : "+nf.format(netExposure)+"\n";
		    	creditExceed=0;
		    	//HP20250328 pcgValue=Double.parseDouble(""+VPCG_VALUE.elementAt(i));
		    	pcgValue=Double.parseDouble(""+VPCG_UNCAPPED_VALUE.elementAt(i));
		    	pcgDealValue=Double.parseDouble(""+VPCG_DEAL_VALUE.elementAt(i));
		    	double creditExceed_temp=0;
		    	double temp_net_expo = netExposure;
		    	double limitValue_info=limit_info.get(countpty_cd);
		    	//HP20250328 double BalanceTotal=netExposure-limitValue_info;
		    	double BalanceTotal=netExposure - pcgDealValue -limitValue_info;
		    	
		    	creditExceed=BalanceTotal;
		    	creditExceed_temp = creditExceed; //FOR DISPALY PURPOSE ONLY
		    	double temp = creditExceed;
	    		double parent_rem = 0;
	    		boolean check=false;
	    		double parent_limit=0;
	    		
		    	double rem_limit=0;
		    	double consumed_limit=0;
		    	//System.out.println("net Expo="+nf.format(netExposure));
		    	//System.out.println("limitValue_info="+nf.format(limitValue_info));
		    	//System.out.println("BalanceTotal="+nf.format(BalanceTotal));
		    	//System.out.println(parent_limit_value);
		    	if(creditExceed < 2)
		    	{	
		    		creditExceed =0;
		    		
		    		credit_exceed_info+="Own Limit Consumed : "+nf.format(limitValue_info+BalanceTotal)+"\n";
		    		credit_exceed_info+= "\nTotal Credit Exceed\n"
		    				+ "="+nf.format(creditExceed);
		    		rem_limit =(-1)*(BalanceTotal);
		    		consumed_limit = netExposure;
		    		
		    		limit_dtl_info = "(O) Consumed : "+nf.format(consumed_limit)+" Balance : "+nf.format(rem_limit);
		    	}
		    	else 
		    	{
		    		consumed_limit = temp_net_expo - creditExceed;
		    		rem_limit = 0;
		    		limit_dtl_info = "(O) Consumed : "+nf.format(consumed_limit)+" Balance : "+nf.format(rem_limit);
		    		
		    		credit_exceed_info+="Own Limit Consumed : "+nf.format(limitValue_info)+"\n";
		    		if(VLIMIT_PARENT_FLAG.elementAt(i).equals("Y")) //HP20250328 && pcgValue > 0)
			    	{
			    		boolean inactive_deal = limit_info.containsKey(VPARENT_CD.elementAt(i)+"-"+gx);
			    		if(!inactive_deal)
			    		{
			    			check = true;
			    			parent_limit = parent_limit_value.get(""+VPARENT_CD.elementAt(i)+"-"+gx);
			    			//System.out.println("parent_limit="+nf.format(parent_limit));
			    			//HP20250328 parent_limit=parent_limit>pcgValue?pcgValue:parent_limit; //PCG IS THERE
			    			//System.out.println("pcgValue="+nf.format(pcgValue));
			    			
			    			creditExceed = creditExceed - parent_limit; 
			    			creditExceed_temp = creditExceed; //FOR DISPALY PURPOSE ONLY
			    			
			    			if(creditExceed < 2) 
			    			{
			    				parent_rem = (-1)*creditExceed;	
			    				parent_limit = temp;
			    				creditExceed=0;
			    			}
			    			else
			    			{
			    				parent_rem = 0;
			    			}
			    			credit_exceed_info+="Parent Limit Consumed(PCG) : "+nf.format(parent_limit)+"\n";
			    			parent_limit_value.replace(""+VPARENT_CD.elementAt(i)+"-"+gx, parent_rem);
			    			
			    			//limit_dtl_info += "\n(P) Consumed : "+nf.format(parent_limit)+" Balance : "+nf.format(parent_rem);
			    		}
			    		else
			    		{
			    			check = true;
			    			parent_limit = temp_limit_info.get(""+VPARENT_CD.elementAt(i)+"-"+gx);
			    			//System.out.println("ELSE");
			    			//System.out.println("parent_limit="+nf.format(parent_limit));
			    			//HP20250328 parent_limit=parent_limit>pcgValue?pcgValue:parent_limit; //PCG IS THERE
			    			//System.out.println("pcgValue="+nf.format(pcgValue));
			    			
			    			creditExceed = creditExceed - parent_limit; 
			    			creditExceed_temp = creditExceed; //FOR DISPALY PURPOSE ONLY
			    			if(creditExceed < 2)
			    			{
			    				parent_rem = (-1)*creditExceed;	
			    				parent_limit = temp;
			    				creditExceed=0;
			    			}
			    			else
			    			{
			    				parent_rem = 0;
			    			}
			    			credit_exceed_info+="Parent Limit Consumed(PCG) : "+nf.format(parent_limit)+"\n";
			    			temp_limit_info.replace(""+VPARENT_CD.elementAt(i)+"-"+gx, parent_rem);
			    			
			    			//limit_dtl_info += "\n(P) Consumed : "+nf.format(parent_limit)+" Balance : "+nf.format(parent_rem);
			    		}
			    	}
		    		
		    		if(creditExceed_temp < 0) //FOR DISPALY PURPOSE ONLY
		    		{
		    			creditExceed_temp=0;
		    		}
		    		credit_exceed_info+= "\nTotal Credit Exceed\n"
		    				//+ "="+nf.format(creditExceed)+"\n";
		    				+ "="+nf.format(creditExceed_temp)+"\n";
		    		if(check)
		    		{
		    			credit_exceed_info+= "=[ "+nf.format(netExposure)+" - ("+nf.format(pcgDealValue)+" + "+nf.format(limitValue_info)+" + "+nf.format(parent_limit)+") ]";
		    		}
		    		else
		    		{
		    			credit_exceed_info+= "=[ "+nf.format(netExposure)+" - "+nf.format(pcgDealValue)+" - "+nf.format(limitValue_info)+" ]";
		    		}
		    	}
		    	//System.out.println("------------------------"+VLIMIT_PARENT_FLAG.elementAt(i));
		    	limit_info.replace(countpty_cd,rem_limit );
		    	
		    	VCREDIT_EXCEED_BALANCE.add(nf.format(rem_limit));
		    	VCONSUMED_LIMIT.add(limit_dtl_info);
		    	VCREDIT_EXCEED.add(nf.format(creditExceed));
				VCREDIT_EXCEED_INFO.add(credit_exceed_info);
				VLIMIT_VALUE_LINKED_COMP.add("");
				VLIMIT.add(nf.format(limit_value.get(countpty_cd)));
				
				double LastExchgRate=Double.parseDouble(""+VEXCHG_RATE.elementAt(i));
				if(LastExchgRate>0) 
				{
					VNET_EXPOSURE_USD.add(nf2.format(netExposure/LastExchgRate));
					VCREDIT_EXCEED_USD.add(nf2.format(creditExceed/LastExchgRate));
				}
				else 
				{
					VNET_EXPOSURE_USD.add(nf2.format(netExposure));	
					VCREDIT_EXCEED_USD.add(nf2.format(creditExceed)); 
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void Temp_CR_LimitCalculation()
	{
		String function_nm="Temp_CR_LimitCalculation()";
		try
		{
			Iterator o = limit_value.keySet().iterator();
		    while (o.hasNext()) 
		    {
		    	String key = (String) o.next();
		    	String temp[] = key.split("-");
		    	String countpty_cd=temp[0];
		    	String gx=temp[1];
		    	
				double limitValue=0;
				queryString ="SELECT SUM(AMT) "
						+ "FROM FMS_LIMIT_DTL "
						+ "WHERE COUNTERPARTY_CD=? AND GX=? "
						+ "AND EFF_DT<=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND ((EXP_DT IS NULL AND INACTIVATION_DT IS NULL) OR (EXP_DT >= TO_DATE(?,'DD/MM/YYYY') "
						+ "AND INACTIVATION_DT IS NULL) OR ((EXP_DT >= TO_DATE(?,'DD/MM/YYYY') OR EXP_DT IS NULL) "
						+ "AND INACTIVATION_DT-1 >= TO_DATE(?,'DD/MM/YYYY'))) "
						+ "AND ((ACTION_TYPE=?) OR (ACTION_TYPE=? AND LIMIT_TYPE != ?))";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, countpty_cd);
				stmt.setString(2, gx);
				stmt.setString(3, report_dt);
				stmt.setString(4, report_dt);
				stmt.setString(5, report_dt);
				stmt.setString(6, report_dt);
				stmt.setString(7, "Adjust Limit");
				stmt.setString(8, "Adjust Usage");
				stmt.setString(9, "Unsecured");
				rset=stmt.executeQuery();				
				while(rset.next())
				{
					limitValue=rset.getDouble(1);
				}
				rset.close();
				stmt.close();
				
				temp_limit_info.put(key, limitValue);
				limit_value.put(key, limitValue);
		    }
		    
		    double netExposure=0;
		    double creditExceed=0;
		    
		    String countpty_cd="";
		    
		    for(int i=0; i<VCOUNTERPARTY_CD.size(); i++)
			{
		    	countpty_cd=""+VCOUNTERPARTY_CD.elementAt(i)+"-"+VGX.elementAt(i);
		    	if(VGX.elementAt(i).equals("I"))
		    	{
		    		countpty_cd=utilBean.getGasExchangeCd(conn,"IGX")+"-"+VGX.elementAt(i);
		    	}
		    	String credit_exceed_info="";
			    
		    	if(!VNET_EXPOSURE.elementAt(i).equals(""))
				{
					netExposure=Double.parseDouble(""+VNET_EXPOSURE.elementAt(i));
				}
		    	
		    	credit_exceed_info+="Net Exposure : "+nf.format(netExposure)+"\n";
		    	creditExceed=0;
		    	double limitValue_info=temp_limit_info.get(countpty_cd);
		    	double BalanceTotal=netExposure-limitValue_info;
		    	
		    	creditExceed=BalanceTotal;
		    	double rem_limit=0;
		    	if(creditExceed < 0) {
		    		creditExceed =0;
		    		rem_limit =(-1)*(BalanceTotal);
		    	}else {
		    		rem_limit = 0;
		    	}
		    	
		    	temp_limit_info.replace(countpty_cd,rem_limit );
		    	
		    }    
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void CR_IGX_Suummary()
	{
		String function_nm="CR_IGX_Suummary()";
		try
		{
			double total_account_rece = 0;
			double total_unbilled_rece = 0;
			double total_undelivered_curr_mth = 0;
			double total_forward_notional = 0;
			double total_gross_expo = 0;
			double total_gross_expo_incl_tax = 0;
			double total_net_expo = 0;
			double total_limit = 0;
			double total_credit_exceed = 0;
			double total_credit_exceed_usd = 0;
			double total_net_expo_usd = 0;
			
			int i=0;
			for(int j=0;j<VEXPOSURE_HEADING.size();j++)
			{
				String expo_gx=""+VEXPOSURE_HEADING_FLAG.elementAt(j);
				int index=Integer.parseInt(""+VINDEX.elementAt(j));
				if(index>0)
				{
					int k=0;
					for(i=i; i<VCOUNTERPARTY_CD.size(); i++)
					{
						k+=1;
						if(expo_gx.equals("I"))
						{
							total_account_rece += Double.parseDouble(""+VBILLED_AMT.elementAt(i));
							total_unbilled_rece += Double.parseDouble(""+VUNBILLED_AMT.elementAt(i));
							total_undelivered_curr_mth += Double.parseDouble(""+VUNBILLED_CURRENT_MONTH.elementAt(i));
							total_forward_notional += Double.parseDouble(""+VFORWARD_NOTIONAL.elementAt(i));
							total_gross_expo += Double.parseDouble(""+VGROSS_EXPOSURE.elementAt(i));
							total_gross_expo_incl_tax += Double.parseDouble(""+VGROSS_EXPOSURE_TAX.elementAt(i));
							total_net_expo += Double.parseDouble(""+VNET_EXPOSURE.elementAt(i));
							total_limit = Double.parseDouble(""+VLIMIT.elementAt(i));
							total_credit_exceed += Double.parseDouble(""+VCREDIT_EXCEED.elementAt(i));
							total_credit_exceed_usd += Double.parseDouble(""+VCREDIT_EXCEED_USD.elementAt(i));
							total_net_expo_usd += Double.parseDouble(""+VNET_EXPOSURE_USD.elementAt(i));
						}
						if(k==index)
						{
							i++;
							break;
						}
					}
				}
			}
			
			total_igx_ac_rece = nf.format(total_account_rece);
			total_igx_unbilled_rece = nf.format(total_unbilled_rece);
			total_igx_delv_curr_mth = nf.format(total_undelivered_curr_mth);
			total_igx_fwd_not = nf.format(total_forward_notional);
			total_igx_gross_expo = nf.format(total_gross_expo);
			total_igx_gross_expo_incl_tax = nf.format(total_gross_expo_incl_tax);
			total_igx_net_expo = nf.format(total_net_expo);
			total_igx_limit = nf.format(total_limit);
			total_igx_cr_exceed = nf.format(total_credit_exceed);
			total_igx_cr_exceed_usd = nf.format(total_credit_exceed_usd);
			total_igx_net_expo_usd = nf.format(total_net_expo_usd);
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
		
	public void getExccedCreditReason()
	{
		String function_nm="getExccedCreditReason()";
		try
		{
			double collateralValue = 0;
			double collateralLinkedValue = 0; 
			double limitValue=0;
			double parentLimitValue=0;
			double credit_exceed = 0;
			
			for(int j=0; j<VCOLLATERAL_VALUE.size(); j++)
			{
				String compnyCd=""+VLEGAL_ENTITY.elementAt(j);
				String countpty_cd=""+VCOUNTERPARTY_CD.elementAt(j);
				String agmt=""+VAGMT_NO.elementAt(j);
				String agmt_rev=""+VAGMT_REV_NO.elementAt(j);
				String cont=""+VCONT_NO.elementAt(j);
				String cont_rev=""+VCONT_REV_NO.elementAt(j);
				String cont_type=""+VCONTRACT_TYPE.elementAt(j);
				String gx=""+VGX.elementAt(j);
				
				String reasonExceed="";
				if(!VCOLLATERAL_VALUE.elementAt(j).equals("")) {
					collateralValue= Double.parseDouble(""+VCOLLATERAL_VALUE.elementAt(j));
				}else {
					collateralValue=0.00;
				}
				if(!VLIMIT.elementAt(j).equals("")) {
					limitValue= Double.parseDouble(""+VLIMIT.elementAt(j));
				}else {
					limitValue=0.00;
				}
				if(!VPARENT_LIMIT_VALUE.elementAt(j).equals("")) {
					parentLimitValue = Double.parseDouble(""+VPARENT_LIMIT_VALUE.elementAt(j));
				}else {
					parentLimitValue = 0.00;
				}
				
				if(!VCREDIT_EXCEED.elementAt(j).equals("")) {
					credit_exceed = Double.parseDouble(""+VCREDIT_EXCEED.elementAt(j));
				}else {
					credit_exceed = 0.00;
				}
				
				
				//Shi Ting 07/12/20202: //HARSH20201208
				//1)Credit limit exceeded (Logic: If counter partys exposure exceeds credit limit / collateral 
				//(if counter party has parent, it will use parents limit) for any of the exposure date
				//2)For new deals entered on trade date = EOD date, Open deal with counterparty having no credit limit
				//(Logic: if sell deal, counterparty has no limit, but security type entered during deal captured is open account)
				//3)For new deals entered on trade date = EOD date, Security required for counterparty rated C and D
				//(Logic: if buy deal, counterparty is rated C and D but security type entered during deal captured is open account)
				
				//THE PURPOSE IS IF BUY DEAL THEN CHECK START DT == SYSDATE IF YES THEN CHECK CREDIT RATING C OR D & OPEN ACCOUNT IF YES THEN SHOW BUY DEAL IN EXCEED CREDIT REPORT OTHERWISH NOT SHOW AS PER CUSTOMER FEEDBACK ON  19/02/2021
				String ExceedReasonflag = "N"; //only for buy deal HARSH20210223
				
				//HARSH20210607 IF EXCEED > 0 AND CONTERPARTY HAVING NO COLLATERAL AND NO LIMIT THEN IT WILL APPEAR IN EXCEDD REPORT AS FLAG THE EXCEED AS PER SHI TING MAIL ON 07/06/2021 
				if(credit_exceed > 0)
				{
					//AS PER YI-FANG MAIL AND INCIDENT@2500003 IN EMS UAT
					//If exceed value and duration is within approved parameters, please state reason as Exceed approved up to INRXX or , whichever earlier. 
					//If approved exceed is no longer valid, please state reason as Approved exceed no longer valid. 
					//If no approved exceed and there is an exceed, please state reason as Credit Limit or Collateral Value Exceed.

					int isApprovedExceedDayConfig=0;
					int isExceedDayConfig=0;
					double ExceedValue=0;
					String currency="";
					queryString="SELECT COUNT(*),VALUE,CURRENCY "
							+ "FROM FMS_CREDIT_EXCEED_DAYS "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND STATUS=? "
							+ "AND FROM_DT <= TO_DATE(?,'DD/MM/YYYY') AND TO_DT >= TO_DATE(?,'DD/MM/YYYY') "
							+ "GROUP BY VALUE,CURRENCY ";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, compnyCd);
					stmt.setString(2, countpty_cd);
					stmt.setString(3, agmt);
					stmt.setString(4, cont);
					stmt.setString(5, cont_type);
					stmt.setString(6, "O");
					stmt.setString(7, report_dt);
					stmt.setString(8, report_dt);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						isExceedDayConfig=rset.getInt(1);
						ExceedValue=rset.getDouble(2);
						currency=utilBean.getRateUnitNm(conn,rset.getString(3)==null?"":rset.getString(3));
					}
					rset.close();
					stmt.close();
					
					//APPROVED EXCEED COUNT
					queryString="SELECT COUNT(*) "
							+ "FROM FMS_CREDIT_EXCEED_DAYS "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND STATUS=? "
							+ "AND FROM_DT <= TO_DATE(?,'DD/MM/YYYY')";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, compnyCd);
					stmt.setString(2, countpty_cd);
					stmt.setString(3, agmt);
					stmt.setString(4, cont);
					stmt.setString(5, cont_type);
					stmt.setString(6, "O");
					stmt.setString(7, report_dt);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						isApprovedExceedDayConfig=rset.getInt(1);
					}
					rset.close();
					stmt.close();
					
					if(isExceedDayConfig > 0 && ExceedValue >= credit_exceed)
					{
						reasonExceed = "Exceed Approved up to "+nf.format(ExceedValue)+" "+currency;
						ExceedReasonflag = "Y";
					}
					else if(isApprovedExceedDayConfig > 0)
					{
						reasonExceed = "Approved Exceed no Longer Valid";
						ExceedReasonflag = "Y";
					}
					else 
					{
						reasonExceed = "Credit Limit or Collateral Value Exceed";//HARSH20210607
						ExceedReasonflag = "Y";//HARSH20210607
					}
				}
				
				if(VSIGNING_DT.elementAt(j).equals(report_dt)) 
				{//NOW TRAED_DATE IS A SIGNING DATE SO, WE COMPARE WITH REPORT DATE AS PER FEEDBACK ON 15/03/2021 
					String checkOA="";
					checkOA ="SELECT NVL(COUNT(*),0) "
							+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.GX=? "
							+ "AND B.AGMT_NO=? AND B.CONT_NO=? AND B.CONTRACT_TYPE=? "
							+ "AND SEC_TYPE IN (?) "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.SEQ_NO=B.SEQ_NO "
							+ "AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX";
					stmt1 = conn.prepareStatement(checkOA);
					stmt1.setString(1, compnyCd);
					stmt1.setString(2, countpty_cd);
					stmt1.setString(3, gx);
					stmt1.setString(4, agmt);
					stmt1.setString(5, cont);
					stmt1.setString(6, cont_type);
					stmt1.setString(7, "OA");
					rset1=stmt1.executeQuery();
					int found = 0;
					if(rset1.next())
					{
						found = rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					
					if(found > 0)
					{
						if(cont_type.equals("D") || cont_type.equals("I"))
						{
							String credit_rating = "";
							queryString="SELECT CREDIT_RATING "
									+ "FROM FMS_LIMIT_MST A "
									+ "WHERE COUNTERPARTY_CD=? AND GX=? "
									+ "AND RATING_EFF_DT=(SELECT MAX(RATING_EFF_DATE) FROM FMS_LIMIT_MST B "
									+ "WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.GX=B.GX ";
							stmt3 = conn.prepareStatement(queryString);
							stmt3.setString(1, countpty_cd);
							stmt3.setString(2, gx);
							rset3=stmt3.executeQuery();
							if(rset3.next())
							{
								credit_rating = rset3.getString(1)==null?"":rset3.getString(1);
							}
							rset3.close();
							stmt3.close();
							
							if(credit_rating.equals(""))//HARSH20210609 AS PER SHI TING MAIL ON 09/06/2021
							{
								reasonExceed = "Security required for counterparty with no rating";
								ExceedReasonflag = "Y"; //HARSH20210223
							}
							else if(credit_rating.equals("C") || credit_rating.equals("D"))
							{
								reasonExceed = "Security required for counterparty rated C and D";
								ExceedReasonflag = "Y"; //HARSH20210223
							}
						}
						else
						{
							if(limitValue <= 0.00)
							{
								reasonExceed = "Open Deal With Counter Party having no Credit Limit";
								ExceedReasonflag = "Y"; //HARSH20210223
							}
						}
					}
				}
				
				VCREDIT_EXCEED_REASON.add(reasonExceed);
				VCREDIT_EXCEED_REASON_FLAG.add(ExceedReasonflag); //HARSH20210223
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void ExposureTrackingDealWiseFreezeData()
	{
		String function_nm="ExposureTrackingDealWiseFreezeData()";
		try
		{
			int count= 0;
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_CR_EXPO_EOD_MST "
					+ "WHERE REPORT_DT=TO_DATE(?,'DD/MM/YYYY')";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, report_dt);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				count=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			queryString1="DELETE FROM FMS_CR_EXPO_EOD_MST "
					+ "WHERE REPORT_DT=TO_DATE(?,'DD/MM/YYYY')";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, report_dt);
			stmt1.executeUpdate();
			
			stmt1.close();
			
			for(int i=0; i<VCOUNTERPARTY_CD.size(); i++)
			{
				String parent_nm="";
				if(!VPARENT_CD.elementAt(i).equals(""))
				{
					parent_nm=utilBean.getCounterpartyName(conn, ""+VPARENT_CD.elementAt(i));
				}
				
				queryString2="INSERT INTO FMS_CR_EXPO_EOD_MST(COMPANY_CD,REPORT_DT,BUY_SELL,"
						+ "COUNTERPARTY_CD,COUNTERPARTY_NM,CONT_TYPE,"
						+ "MAPPING_ID,AGMT_BASE,CONT_REF,CONT_SIGN_DT,CONT_START_DT,CONT_END_DT,"
						+ "PRICE_TYPE,CONT_PRICE,RATE_UNIT,EXCHG_RATE,AC_RECV,UN_BILL_RECV,"
						+ "UN_DELV_CURR_MTH,FWD_NOT_NEXT_MTH,GROSS_AMT,GROSS_INCLTAX_AMT,"
						+ "COLLAT_VALUE,NET_EXPO_AMT,LIMIT_VALUE,P_LIMIT_VALUE,CR_EXCEED_AMT,"
						+ "NET_EXPO_AMT_USD,CR_EXCEED_AMT_USD,ENT_BY,ENT_DT,GX,"
						+ "LE_PARENT_NM,ULTI_LE_PARENT_NM,LC_AMT,OTH_COLLAT,GX_COUNTERPARTY_CD,CASH_COLLAT_VALUE,PCG_VALUE) "
						+ "VALUES(?,TO_DATE(?,'DD/MM/YYYY'),?,"
						+ "?,?,?,"
						+ "?,?,?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
						+ "?,?,?,?,?,?,"
						+ "?,?,?,?,"
						+ "?,?,?,?,?,"
						+ "?,?,?,SYSDATE,?,"
						+ "?,?,?,?,?,?,?)";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, ""+VLEGAL_ENTITY.elementAt(i));
				stmt2.setString(2, report_dt);
				stmt2.setString(3, ""+VACCOUNT.elementAt(i));
				stmt2.setString(4, ""+VCOUNTERPARTY_CD.elementAt(i));
				stmt2.setString(5, ""+VCOUNTERPARTY_NM.elementAt(i));
				stmt2.setString(6, ""+VCONTRACT_TYPE.elementAt(i));
				stmt2.setString(7, ""+VMAPPING_ID.elementAt(i));
				stmt2.setString(8, ""+VAGMT_BASE.elementAt(i));
				stmt2.setString(9, ""+VCONT_REF.elementAt(i));
				stmt2.setString(10, ""+VSIGNING_DT.elementAt(i));
				stmt2.setString(11, ""+VSTART_DT.elementAt(i));
				stmt2.setString(12, ""+VEND_DT.elementAt(i));
				stmt2.setString(13, ""+VPRICE_TYPE.elementAt(i));
				stmt2.setString(14, ""+VRATE.elementAt(i));
				stmt2.setString(15, ""+VRATE_UNIT.elementAt(i));
				stmt2.setString(16, ""+VEXCHG_RATE.elementAt(i));
				stmt2.setString(17, ""+VBILLED_AMT.elementAt(i));
				stmt2.setString(18, ""+VUNBILLED_AMT.elementAt(i));
				stmt2.setString(19, ""+VUNBILLED_CURRENT_MONTH.elementAt(i));
				stmt2.setString(20, ""+VFORWARD_NOTIONAL.elementAt(i));
				stmt2.setString(21, ""+VGROSS_EXPOSURE.elementAt(i));
				stmt2.setString(22, ""+VGROSS_EXPOSURE_TAX.elementAt(i));
				stmt2.setString(23, ""+VCOLLATERAL_VALUE.elementAt(i));
				stmt2.setString(24, ""+VNET_EXPOSURE.elementAt(i));
				stmt2.setString(25, ""+VLIMIT.elementAt(i));
				stmt2.setString(26, ""+VPARENT_LIMIT_VALUE.elementAt(i));
				stmt2.setString(27, ""+VCREDIT_EXCEED.elementAt(i));
				stmt2.setString(28, ""+VNET_EXPOSURE_USD.elementAt(i));
				stmt2.setString(29, ""+VCREDIT_EXCEED_USD.elementAt(i));
				stmt2.setString(30, emp_cd);
				stmt2.setString(31, ""+VGX.elementAt(i));
				stmt2.setString(32, parent_nm);
				stmt2.setString(33, parent_nm);
				stmt2.setString(34, ""+VLC_AMT.elementAt(i));
				stmt2.setString(35, ""+VOTH_COLLAT.elementAt(i));
				stmt2.setString(36, ""+VGX_COUNTERPARTY_CD.elementAt(i));
				stmt2.setString(37, ""+VCASH_COLLATERAL_VALUE.elementAt(i));
				stmt2.setString(38, ""+VPCG_VALUE.elementAt(i));
				stmt2.executeUpdate();
				
				stmt2.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void SendExcelForExposureTrackingReport()
    {
		String function_nm="SendExcelForExposureTrackingReport()";
    	String mailBody = "";
		try
		{
			
			String split_sysdate[] = report_dt.split("/");
			String splited_sysdate = split_sysdate[2]+split_sysdate[1]+split_sysdate[0];
			
			//String filename = file_path+""+comp_abbr+"-Exposure_Tracking_Report_"+splited_sysdate+".xls";
			//String subject=comp_abbr+" FMSng "+context+": Credit Risk Exposure Tracking Report dated "+report_dt;
			
			String filename = file_path+"Exposure_Tracking_Report_"+splited_sysdate+".xls";
			String subject=CommonVariable.app_name+" "+context+": Credit Risk Exposure Tracking Report dated "+report_dt;
			
			HSSFWorkbook workbook = new HSSFWorkbook();  
			HSSFSheet sheet = workbook.createSheet("Exposure Tracking Report");
			
			mailBody="<html>"
					+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>";
			mailBody+="Please find "+CommonVariable.app_name+" Credit Risk Exposure Tracking Report dated "+report_dt+" attached.";
			
			int k=0;
			for(int a=0; a < VEXPOSURE_HEADING.size(); a++) 
			{
				String expo_head_flag=""+VEXPOSURE_HEADING_FLAG.elementAt(a);
				int index=Integer.parseInt(""+VINDEX.elementAt(a));
				
				HSSFRow row_head1 = sheet.createRow((short)VMAILINDEX.size()+3*a);
				row_head1.createCell((short) 0).setCellValue(""+VEXPOSURE_HEADING.elementAt(a));
				
				sheet.addMergedRegion(new CellRangeAddress(VMAILINDEX.size()+3*a,VMAILINDEX.size()+3*a,0,20));
				
				HSSFRow rowhead1 = sheet.createRow((short)VMAILINDEX.size()+3*a+1); 
				rowhead1.createCell((short) 0).setCellValue("Sr#");
				rowhead1.createCell((short) 1).setCellValue("Legal Entity");
				rowhead1.createCell((short) 2).setCellValue("Account");  
				rowhead1.createCell((short) 3).setCellValue("Counterparty");  
				rowhead1.createCell((short) 4).setCellValue("Contract Type");
				rowhead1.createCell((short) 5).setCellValue("Contract#");
				rowhead1.createCell((short) 6).setCellValue("Signing Date");
				rowhead1.createCell((short) 7).setCellValue("Contract Period");
				rowhead1.createCell((short) 8).setCellValue("Price Type"); 
				rowhead1.createCell((short) 9).setCellValue("Price Rate"); 
				rowhead1.createCell((short) 10).setCellValue("Rate Unit");
				rowhead1.createCell((short) 11).setCellValue("Account Receivable (INR)");  
				rowhead1.createCell((short) 12).setCellValue("Unbilled Receivable (INR)");  
				rowhead1.createCell((short) 13).setCellValue("Undelivered Current Month (INR)");
				rowhead1.createCell((short) 14).setCellValue("Forward Notional Next Month (INR)");
				rowhead1.createCell((short) 15).setCellValue("Gross Exposure (INR)");
				rowhead1.createCell((short) 16).setCellValue("Gross Exposure (INR)(Incl. Tax)"); 
				rowhead1.createCell((short) 17).setCellValue("Collateral Value(INR)"); 
				rowhead1.createCell((short) 18).setCellValue("Cash Collateral Value(INR)"); 
				rowhead1.createCell((short) 19).setCellValue("Net Exposure (INR)");
				rowhead1.createCell((short) 20).setCellValue("PCG (INR)");
				rowhead1.createCell((short) 21).setCellValue("Limit (INR)");
				rowhead1.createCell((short) 22).setCellValue("Credit Exceed (INR)"); 
				rowhead1.createCell((short) 23).setCellValue("Net Exposure (USD)"); 
				rowhead1.createCell((short) 24).setCellValue("Credit Exceed (USD)");
			
				int mailindex = 0;
				if(index>0)
				{
					int j=0;
					for( k=k; k < VCOUNTERPARTY_CD.size(); k++ )
					{
						j=j+1;
						mailindex++;
						
						HSSFRow row = sheet.createRow((short)VMAILINDEX.size()+3*a+2);  
						row.createCell((short) 0).setCellValue(j);  
						row.createCell((short) 1).setCellValue(""+VLEGAL_ENTITY_ABBR.elementAt(k));
						row.createCell((short) 2).setCellValue(""+VACCOUNT.elementAt(k));  
						row.createCell((short) 3).setCellValue(""+VCOUNTERPARTY_NM.elementAt(k));
						row.createCell((short) 4).setCellValue(""+VDIS_CONTRACT_TYPE.elementAt(k));
						row.createCell((short) 5).setCellValue(""+VDISPLAY_DEAL_MAP.elementAt(k));
						row.createCell((short) 6).setCellValue(""+VSIGNING_DT.elementAt(k));
						row.createCell((short) 7).setCellValue(""+VSTART_DT.elementAt(k)+"-"+VEND_DT.elementAt(k));  
						row.createCell((short) 8).setCellValue(""+VPRICE_TYPE.elementAt(k));
						row.createCell((short) 9).setCellValue(Double.parseDouble(""+VRATE.elementAt(k)));
						row.createCell((short) 10).setCellValue(""+VRATE_UNIT_NM.elementAt(k));
						row.createCell((short) 11).setCellValue(Double.parseDouble(""+VBILLED_AMT.elementAt(k)));  
						row.createCell((short) 12).setCellValue(Double.parseDouble(""+VUNBILLED_AMT.elementAt(k)));
						row.createCell((short) 13).setCellValue(Double.parseDouble(""+VUNBILLED_CURRENT_MONTH.elementAt(k)));
						row.createCell((short) 14).setCellValue(Double.parseDouble(""+VFORWARD_NOTIONAL.elementAt(k)));
						row.createCell((short) 15).setCellValue(Double.parseDouble(""+VGROSS_EXPOSURE.elementAt(k)));  
						row.createCell((short) 16).setCellValue(Double.parseDouble(""+VGROSS_EXPOSURE_TAX.elementAt(k)));
						row.createCell((short) 17).setCellValue(Double.parseDouble(""+VCOLLATERAL_VALUE.elementAt(k)));
						row.createCell((short) 18).setCellValue(Double.parseDouble(""+VCASH_COLLATERAL_VALUE.elementAt(k)));
						row.createCell((short) 19).setCellValue(Double.parseDouble(""+VNET_EXPOSURE.elementAt(k)));
						row.createCell((short) 20).setCellValue(Double.parseDouble(""+VPCG_VALUE.elementAt(k)));
						row.createCell((short) 21).setCellValue(Double.parseDouble(""+VLIMIT.elementAt(k)));  
						row.createCell((short) 22).setCellValue(Double.parseDouble(""+VCREDIT_EXCEED.elementAt(k)));
						row.createCell((short) 23).setCellValue(Double.parseDouble(""+VNET_EXPOSURE_USD.elementAt(k)));
						row.createCell((short) 24).setCellValue(Double.parseDouble(""+VCREDIT_EXCEED_USD.elementAt(k)));
						VMAILINDEX.add(mailindex);
						
						if(j==index)
						{
							if(expo_head_flag.equals("I"))
							{
								HSSFRow row1 = sheet.createRow((short)VMAILINDEX.size()+3*a+3); 
								row1.createCell((short) 9).setCellValue("IGX Summary");
								row1.createCell((short) 10).setCellValue(Double.parseDouble(total_igx_ac_rece));
								row1.createCell((short) 11).setCellValue(Double.parseDouble(total_igx_unbilled_rece));
								row1.createCell((short) 12).setCellValue(Double.parseDouble(total_igx_delv_curr_mth));
								row1.createCell((short) 13).setCellValue(Double.parseDouble(total_igx_fwd_not));
								row1.createCell((short) 14).setCellValue(Double.parseDouble(total_igx_gross_expo));
								row1.createCell((short) 15).setCellValue(Double.parseDouble(total_igx_gross_expo_incl_tax));
								row1.createCell((short) 18).setCellValue(Double.parseDouble(total_igx_net_expo));
								row1.createCell((short) 20).setCellValue(Double.parseDouble(total_igx_limit));
								row1.createCell((short) 21).setCellValue(Double.parseDouble(total_igx_cr_exceed));
								row1.createCell((short) 22).setCellValue(Double.parseDouble(total_igx_net_expo_usd));
								row1.createCell((short) 23).setCellValue(Double.parseDouble(total_igx_cr_exceed_usd));
							}
							k=k+1;
							break;
						}
					}
				}
				else
				{
					mailindex++;
					VMAILINDEX.add(mailindex);
				}
			}
			
			mailBody+= "</span>";
			mailBody+=CommonVariable.mail_disclaimer;
			mailBody+= "</html>";
			
			FileOutputStream fileOut = new FileOutputStream(filename);  
			workbook.write(fileOut);  
			fileOut.close();  
			File file = new File(filename);
			
			String to_mail_list = utilBean.getToMailReceipentList(conn,"0","Exposure Tracking Report","Risk Mgmt","Daily","Auto");
			String cc_mail_list= utilBean.getCcMailReceipentList(conn,"0","Exposure Tracking Report","Risk Mgmt","Daily","Auto");
			
			if(!to_mail_list.equals("") && !mailBody.equals(""))
			{
				mail.sendMail(conn, to_mail_list, subject, mailBody, filename, cc_mail_list, "");
				System.out.println("Exposure Tracking Report Mail Done(Daily).....");
			}
		    
		}
		catch (Exception e) 
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
    }
	
	public void SendExcelForExeedCreditReport()
	{
		String function_nm="SendExcelForExeedCreditReport()";
		String mailBody = "";
		try
		{

			String split_sysdate[] = report_dt.split("/");
			String splited_sysdate = split_sysdate[2]+split_sysdate[1]+split_sysdate[0];

			String filename = file_path+"Exceed_Credit_Report_"+splited_sysdate+".xls";
			
			String subject=CommonVariable.app_name+" "+context+": Credit Risk Exceed Credit Report dated "+report_dt;
			HSSFWorkbook workbook = new HSSFWorkbook();  
			HSSFSheet sheet = workbook.createSheet("Exceed Credit Report");

			mailBody="<html>"
					+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>";
			mailBody+="Please find "+CommonVariable.app_name+" Credit Risk Exceed Credit Report dated "+report_dt+" attached.";

			int k=0;
			for(int a=0; a < VEXPOSURE_HEADING.size(); a++) 
			{
				String expo_head_flag=""+VEXPOSURE_HEADING_FLAG.elementAt(a);
				int index=Integer.parseInt(""+VINDEX.elementAt(a));

				HSSFRow row_head1 = sheet.createRow((short)VMAILINDEX.size()+3*a);
				row_head1.createCell((short) 0).setCellValue(""+VEXPOSURE_HEADING.elementAt(a));
				sheet.addMergedRegion(new CellRangeAddress(VMAILINDEX.size()+3*a,VMAILINDEX.size()+3*a,0,12));

				HSSFRow rowhead1 = sheet.createRow((short)VMAILINDEX.size()+3*a+1); 
				rowhead1.createCell((short) 0).setCellValue("Sr#");
				rowhead1.createCell((short) 1).setCellValue("Legal Entity"); 
				rowhead1.createCell((short) 2).setCellValue("Account");  
				rowhead1.createCell((short) 3).setCellValue("Counterparty");  
				rowhead1.createCell((short) 4).setCellValue("Contract Type");
				rowhead1.createCell((short) 5).setCellValue("Contract#");
				rowhead1.createCell((short) 6).setCellValue("Signing Date");
				rowhead1.createCell((short) 7).setCellValue("Contract Period");
				rowhead1.createCell((short) 8).setCellValue("Price Type"); 
				rowhead1.createCell((short) 9).setCellValue("Price Rate"); 
				rowhead1.createCell((short) 10).setCellValue("Rate Unit");
				rowhead1.createCell((short) 11).setCellValue("Credit Exceed (INR)");  
				rowhead1.createCell((short) 12).setCellValue("Credit Exceed (USD)");  
				rowhead1.createCell((short) 13).setCellValue("Exceed Reason");

				int mailindex = 0;
				if(index>0)
				{
					int j=0;
					int no=0;
					for( k=k; k < VCOUNTERPARTY_CD.size(); k++ )
					{
						j=j+1;
						if (VCREDIT_EXCEED_REASON_FLAG.elementAt(k).equals("Y"))
						{
							no+=1;

							mailindex++;

							HSSFRow row = sheet.createRow((short)VMAILINDEX.size()+3*a+2);  
							row.createCell((short) 0).setCellValue(no);  
							row.createCell((short) 1).setCellValue(""+VLEGAL_ENTITY_ABBR.elementAt(k));  
							row.createCell((short) 2).setCellValue(""+VACCOUNT.elementAt(k));  
							row.createCell((short) 3).setCellValue(""+VCOUNTERPARTY_NM.elementAt(k));
							row.createCell((short) 4).setCellValue(""+VDIS_CONTRACT_TYPE.elementAt(k));
							row.createCell((short) 5).setCellValue(""+VDISPLAY_DEAL_MAP.elementAt(k));
							row.createCell((short) 6).setCellValue(""+VSIGNING_DT.elementAt(k));
							row.createCell((short) 7).setCellValue(""+VSTART_DT.elementAt(k) +"-"+""+ VEND_DT.elementAt(k));  
							row.createCell((short) 8).setCellValue(""+VPRICE_TYPE.elementAt(k));
							row.createCell((short) 9).setCellValue(Double.parseDouble(""+VRATE.elementAt(k)));
							row.createCell((short) 10).setCellValue(""+VRATE_UNIT_NM.elementAt(k));
							row.createCell((short) 11).setCellValue(Double.parseDouble(""+VCREDIT_EXCEED.elementAt(k)));  
							row.createCell((short) 12).setCellValue(Double.parseDouble(""+VCREDIT_EXCEED_USD.elementAt(k)));
							row.createCell((short) 13).setCellValue(""+VCREDIT_EXCEED_REASON.elementAt(k));
							VMAILINDEX.add(mailindex);
						}

						if(j==index)
						{
							if(expo_head_flag.equals("I"))
							{
								HSSFRow row1 = sheet.createRow((short)VMAILINDEX.size()+3*a+3); 
								row1.createCell((short) 9).setCellValue("IGX Summary");
								row1.createCell((short) 10).setCellValue(Double.parseDouble(total_igx_cr_exceed));
								row1.createCell((short) 11).setCellValue(Double.parseDouble(total_igx_cr_exceed_usd));
							}
							k=k+1;
							break;
						}
					}
				}
				else
				{
					mailindex++;
					VMAILINDEX.add(mailindex);
				}
			}
			mailBody+= "</span>";
			mailBody+="<br><br><br><font style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Please maintain confidentiality."
					+ "<br><b>NOTE:</b><i> System Generated Notification - Please do not Reply.</i>"
					+ "</html>";

			//Added By HM to Auto size the column widths
			for(int columnIndex = 0; columnIndex < 12; columnIndex++) 
			{
				sheet.autoSizeColumn(columnIndex);
			}

			FileOutputStream fileOut = new FileOutputStream(filename);  
			workbook.write(fileOut);  
			fileOut.close();  
			File file = new File(filename);

			String to_mail_list = utilBean.getToMailReceipentList(conn,"0","Exceed Credit Report","Risk Mgmt","Daily","Auto");
			String cc_mail_list= utilBean.getCcMailReceipentList(conn,"0","Exceed Credit Report","Risk Mgmt","Daily","Auto");

			if(!to_mail_list.equals("") && !mailBody.equals(""))
			{
				mail.sendMail(conn,to_mail_list, subject, mailBody, filename, cc_mail_list, "");
				System.out.println("Exceed Credit Report Mail Done(Daily).....");
			}

		}
		catch (Exception e) 
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getChargeMaster()
	{
		String function_nm="getChargeMaster()";
		try
		{
			utilBean.getChargeMaster(conn);
			VCHARGE_NAME=utilBean.getCHARGE_NAME();
			VCHARGE_ABBR=utilBean.getCHARGE_ABBR();
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	String isCreditExceedRpt="N";
	
	Vector VGX_COUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VAGMT_NO = new Vector();
	Vector VAGMT_REV_NO = new Vector();
	Vector VCONT_NO = new Vector();
	Vector VCONT_REV_NO = new Vector();
	Vector VCONTRACT_TYPE = new Vector();
	Vector VDISPLAY_DEAL_MAP = new Vector();
	Vector VSTART_DT = new Vector();
	Vector VEND_DT = new Vector();
	Vector VRATE = new Vector();
	Vector VRATE_UNIT = new Vector();
	Vector VRATE_UNIT_NM = new Vector();
	Vector VTCQ = new Vector();
	Vector VDCQ = new Vector();
	Vector VAGMT_BASE = new Vector();
	Vector VACCOUNT = new Vector();
	Vector VBILLED_AMT = new Vector();
	Vector VBILLED_AMT_INFO = new Vector();
	Vector VUNBILLED_AMT = new Vector();
	Vector VUNBILLED_AMT_INFO = new Vector();
	Vector VUNBILLED_CURRENT_MONTH = new Vector();
	Vector VUNBILLED_CURRENT_MONTH_INFO = new Vector();
	Vector VFORWARD_NOTIONAL = new Vector(); 
	Vector VFORWARD_NOTIONAL_INFO = new Vector(); 
	Vector VGROSS_EXPOSURE = new Vector(); 	
	Vector VGROSS_EXPOSURE_TAX = new Vector();
	Vector VGROSS_EXPOSURE_INFO = new Vector();
	Vector VGROSS_EXPOSURE_TAX_INFO = new Vector();
	Vector VCOLLATERAL_VALUE = new Vector();
	Vector VNET_EXPOSURE = new Vector();
	Vector VNET_EXPOSURE_INFO = new Vector();
	Vector VLIMIT = new Vector();
	Vector VCREDIT_EXCEED = new Vector();
	Vector VNET_EXPOSURE_USD = new Vector();
	Vector VCREDIT_EXCEED_USD = new Vector();
	Vector VCOLLATERAL_INFO = new Vector();
	Vector VBILLED_QTY = new Vector();
	Vector VHEADING_INFO = new Vector();
	Vector VEXPOSURE_HEADING = new Vector();
	Vector VEXPOSURE_HEADING_FLAG = new Vector();
	Vector VGX = new Vector();
	Vector VSIGNING_DT = new Vector();
	Vector VINDEX = new Vector();
	Vector VPRICE_TYPE = new Vector();
	Vector VPARENT_CD = new Vector();
	Vector VLIMIT_PARENT_FLAG = new Vector();
	Vector VPARENT_LIMIT_VALUE = new Vector();
	Vector VLIMIT_VALUE_LINKED  = new Vector();
	Vector VCREDIT_EXCEED_BALANCE = new Vector();
	Vector VCONSUMED_LIMIT = new Vector();
	Vector VCREDIT_EXCEED_INFO = new Vector();
	Vector VLIMIT_VALUE_LINKED_COMP = new Vector();
	Vector VEXCHG_RATE = new Vector();
	Vector VLC_AMT = new Vector();
	Vector VOTH_COLLAT = new Vector();
	Vector VMAPPING_ID = new Vector();
	Vector VCONT_REF = new Vector();
	Vector VMAILINDEX = new Vector();
	Vector VLEGAL_ENTITY = new Vector();
	Vector VLEGAL_ENTITY_ABBR = new Vector();
	Vector VCASH_COLLATERAL_VALUE = new Vector();
	Vector VCASH_COLLATERAL_INFO = new Vector();
	Vector VPCG_VALUE = new Vector();
	Vector VPCG_UNCAPPED_VALUE = new Vector();
	Vector VPCG_DEAL_VALUE = new Vector();
	Vector VPCG_INFO = new Vector();
	
	Vector VCHARGE_ABBR = new Vector();
	Vector VCHARGE_NAME = new Vector();
	
	Vector VCREDIT_EXCEED_REASON = new Vector();
	Vector VCREDIT_EXCEED_REASON_FLAG = new Vector();
	Vector VDIS_CONTRACT_TYPE = new Vector();
	
	String total_igx_ac_rece = "0.00";
	String total_igx_unbilled_rece = "0.00";
	String total_igx_delv_curr_mth = "0.00";
	String total_igx_fwd_not = "0.00";
	String total_igx_gross_expo = "0.00";
	String total_igx_gross_expo_incl_tax = "0.00";
	String total_igx_net_expo = "0.00";
	String total_igx_limit = "0.00";
	String total_igx_cr_exceed = "0.00";
	String total_igx_cr_exceed_usd = "0.00";
	String total_igx_net_expo_usd = "0.00";
}
