package automation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Vector;

import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DB_AllocationUtil;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.UtilBean;

public class Auto_MR_Exposure_Eod_ap {

	public static void main(String[] args) 
	{
		MR_Exposure_ap mr_expo = new MR_Exposure_ap();
		mr_expo.init();
	}
}

class MR_Exposure_ap
{
	String db_src_file_name="Auto_MR_Exposure_Eod.java";
	Connection conn;
	PreparedStatement stmt,stmt1,stmt2,stmt3,stmt4,stmt_temp,stmt_temp1;
	ResultSet rset,rset1,rset2,rset3,rset4,rset_temp,rset_temp1;
	//String queryString="",queryString1="",queryString2="",queryString3="",queryString4="",queryString5="",queryString_temp="",queryString_temp1="";
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf3 = new DecimalFormat("###########0.000");
	
	String comp_cd="";
	String context="";
	String emp_cd="0";
	String QP_file_path="";
	String QP_file_nm="";
	
	UtilBean utilBean = new UtilBean();
	DateUtil utilDate = new DateUtil();
	Auto_MailDelivery mail = new Auto_MailDelivery();
	DB_AllocationUtil utilAlloc = new DB_AllocationUtil();

    public void init()
	{ 
    	String function_nm="init()";
        try
        {
        	conn=new Auto_DB_Connection().db_conn();
        	if(conn != null)
            {
				context=utilBean.getAutomationKeyDetail(conn, "ENV_CONTEXT");
    			QP_file_path=utilBean.getAutomationKeyDetail(conn, "DAILY_RPT_PATH");
    			
    			String dayNm=utilDate.getDaysName();
    			if(dayNm.trim().toUpperCase().equalsIgnoreCase("MONDAY"))
    			{
    				String sysdate=utilDate.getSysdate();
    				report_dt=utilDate.getDate(sysdate, "-3");
    				
        			deleteFreezMrExpoEodData();
        			System.out.println("Fetching Contract Details the Report Dt : "+report_dt);
        			getContractListForExposure();
        			System.out.println("Fetched Contract Details the Report Dt : "+report_dt);
        			for(int i=0; i<VCOUNTERPARTY_CD.size();i++)
        			{
        				doClear();
        				
        				counterparty_cd=""+VCOUNTERPARTY_CD.elementAt(i);
        				agmt_no=""+VAGMT_NO.elementAt(i);
        				agmt_rev_no=""+VAGMT_REV_NO.elementAt(i);
        				cont_no=""+VCONT_NO.elementAt(i);
        				cont_rev_no=""+VCONT_REV_NO.elementAt(i);
        				account=""+VACCOUNT.elementAt(i);
        				contract_type=""+VCONTRACT_TYPE.elementAt(i);
        				cargo_no=""+VCARGO_NO.elementAt(i);
        				comp_cd=""+VLEGAL_ENTITY_CD.elementAt(i);
        				
        				getContractDetail();
        				ExposureCalculation();
        				System.out.println("Freezing Data into DB Table : "+i);
        				freezMrExpoEodData(i);
        			}
        			generateQPcsvFile();
        			sendEODConfirmationMail();
        			sendQPFile();
    			}
    			else if(!dayNm.trim().toUpperCase().equalsIgnoreCase("SATURDAY") && !dayNm.trim().toUpperCase().equalsIgnoreCase("SUNDAY"))					
				{
    				report_dt=utilDate.getPreviousDate();
    				
        			deleteFreezMrExpoEodData();
        			System.out.println("Fetching Contract Details the Report Dt : "+report_dt);
        			getContractListForExposure();
        			System.out.println("Fetched Contract Details the Report Dt : "+report_dt);
        			for(int i=0; i<VCOUNTERPARTY_CD.size();i++)
        			{
        				doClear();
        				
        				counterparty_cd=""+VCOUNTERPARTY_CD.elementAt(i);
        				agmt_no=""+VAGMT_NO.elementAt(i);
        				agmt_rev_no=""+VAGMT_REV_NO.elementAt(i);
        				cont_no=""+VCONT_NO.elementAt(i);
        				cont_rev_no=""+VCONT_REV_NO.elementAt(i);
        				account=""+VACCOUNT.elementAt(i);
        				contract_type=""+VCONTRACT_TYPE.elementAt(i);
        				cargo_no=""+VCARGO_NO.elementAt(i);
        				comp_cd=""+VLEGAL_ENTITY_CD.elementAt(i);
        				
        				getContractDetail();
        				ExposureCalculation();
        				System.out.println("Freezing Data into DB Table : "+i);
        				freezMrExpoEodData(i);
        			}
        			generateQPcsvFile();
        			sendEODConfirmationMail();
        			sendQPFile();
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
    
    public void getContractListForExposure()
	{
    	String function_nm="getContractListForExposure()";
		try
		{
			String storageDt=utilDate.getDate(report_dt, "1");
			
			String queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONT_REF_NO,TRADE_REF_NO,CONTRACT_TYPE,"
					+ "TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),AGMT_BASE,"
					+ "RATE,RATE_UNIT,CONT_STATUS,'Sell',START_DT,NULL,NULL,NULL "
					+ "FROM FMS_SUPPLY_CONT_MST A "
					+ "WHERE CONT_STATUS NOT IN ('C','X') " //'F' removed as per INC#2410044
					+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			queryString+="UNION ALL ";
			queryString+="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONT_REF_NO,TRADE_REF_NO,CONTRACT_TYPE,"
					+ "TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),AGMT_BASE,"
					+ "RATE,RATE_UNIT,CONT_STATUS,'Buy',START_DT,NULL,NULL,NULL "
					+ "FROM FMS_TRADER_CONT_MST A "
					+ "WHERE CONT_STATUS NOT IN ('C','X') " //'F' removed as per INC#2410044
					+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			queryString+="UNION ALL ";
			queryString+="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,B.CARGO_REF,NULL,A.CONTRACT_TYPE,"
					+ "TO_CHAR(A.SIGNING_DT,'DD/MM/YYYY'),TO_CHAR(B.START_DT,'DD/MM/YYYY'),TO_CHAR(B.END_DT,'DD/MM/YYYY'),A.AGMT_BASE,"
					+ "B.RATE,B.RATE_UNIT,A.CONT_STATUS,'Buy',B.START_DT,B.CARGO_NO,NULL,NULL "
					+ "FROM FMS_TRADER_CN_MST A, FMS_TRADER_CARGO_MST B "
					+ "WHERE A.CONT_STATUS NOT IN ('C','X') " //'F' removed as per INC#2410044
					+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.AGMT_TYPE=B.AGMT_TYPE) "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.AGMT_TYPE=B.AGMT_TYPE "
					+ "AND B.CARGO_STATUS=? ";
			queryString+="UNION ALL ";
			queryString+="SELECT COMPANY_CD,0,0,0,0,0,'STORAGE TANK',NULL,'Z',"
					+ "'"+storageDt+"','"+storageDt+"','"+storageDt+"',NULL,"
					+ "0,'2','Y','Buy',TO_DATE('"+storageDt+"','DD/MM/YYYY') START_DT,0,NULL,NULL "
					+ "FROM FMS_COMPANY_OWNER_MST A "
					+ "WHERE EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COMPANY_OWNER_MST B WHERE A.COMPANY_CD=B.COMPANY_CD) ";
			queryString+="UNION ALL ";
			queryString+="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONT_REF_NO,NULL,A.CONTRACT_TYPE,"
					+ "TO_CHAR(A.SIGNING_DT,'DD/MM/YYYY'),TO_CHAR(B.PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(B.PRICE_END_DT,'DD/MM/YYYY'),NULL,"
					+ "B.RATE,TO_CHAR(B.RATE_UNIT),A.CONT_STATUS,B.BUY_SELL,(B.PRICE_START_DT) START_DT,B.INSTRUMENT_NO,B.INSTRUMENT_TYPE,B.CURVE_NM "
					+ "FROM FMS_DERV_CONT_MST A, FMS_DERV_INSTRUMENT_MST B "
					+ "WHERE A.CONT_STATUS NOT IN ('C','X') AND A.AGMT_TYPE=? " //'F' removed as per INC#2410044
					+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_DERV_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.AGMT_TYPE=B.AGMT_TYPE) "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.AGMT_TYPE=B.AGMT_TYPE ";
			queryString+="ORDER BY START_DT DESC ";
			String temp_queryString=queryString;
			stmt = conn.prepareStatement(temp_queryString);
			stmt.setString(1, "Y");
			stmt.setString(2, "U");
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String buy_sell=rset.getString(17)==null?"":rset.getString(17);
				buy_sell=buy_sell.substring(0,1)+buy_sell.substring(1, buy_sell.length()).toLowerCase();
				VACCOUNT.add(buy_sell);
				
				String companyCd = rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd = rset.getString(2)==null?"":rset.getString(2);
				String agmt = rset.getString(3)==null?"":rset.getString(3);
				String agmt_rev = rset.getString(4)==null?"":rset.getString(4);
				String cont = rset.getString(5)==null?"":rset.getString(5);
				String cont_rev = rset.getString(6)==null?"":rset.getString(6);
				String cont_ref = rset.getString(7)==null?"":rset.getString(7);
				String trade_ref = rset.getString(8)==null?"":rset.getString(8);
				String cont_type = rset.getString(9)==null?"":rset.getString(9);
				String signindt = rset.getString(10)==null?"":rset.getString(10);
				String startDt = rset.getString(11)==null?"":rset.getString(11);
				String endDt = rset.getString(12)==null?"":rset.getString(12);
				
				String cargo_no=rset.getString(19)==null?"":rset.getString(19);
				String instrument_type=rset.getString(20)==null?"":rset.getString(20);
				String hedge_curve_nm=rset.getString(21)==null?"":rset.getString(21);

				String agmt_base = rset.getString(13)==null?"":rset.getString(13);
				
				if(cont_type.equals("X") || cont_type.equals("I") || cont_type.equals("W")) //X for sell side //I for Buy side //W for dlng sell side
				{
					cont_ref=trade_ref;
				}
				
				String companyAbbr =""+utilBean.getCompanyAbbr(conn,companyCd);
				
				String contPriceMapping=countpty_cd+"-"+agmt+"-"+cont;
				if(cont_type.equals("N") || cont_type.equals("V")) {
					contPriceMapping+="-"+cargo_no;
				}
				VMAPPING_ID.add(contPriceMapping);
				VLEGAL_ENTITY_CD.add(companyCd);
				VLEGAL_ENTITY.add(""+utilBean.getCompanyAbbr(conn,companyCd));
				VCOUNTERPARTY_CD.add(countpty_cd);
				if(cont_type.equals("Z"))
				{
					VCOUNTERPARTY_ABBR.add(companyAbbr+"-STORAGE");
					VCOUNTERPARTY_NM.add(companyAbbr+"-STORAGE");
				}
				else
				{
					VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
					VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				}
				//String dealNo=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
				String dealNo=utilBean.NewDealMappingId(companyCd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, cargo_no);
				VDEAL_NUM_EOD.add(dealNo);//FOR EOD
				if(agmt_base.equals("D"))
				{
					dealNo=dealNo+"[DLV]";
				}
				
				VDISPLAY_DEAL_MAP.add(dealNo);
				VAGMT_NO.add(agmt);
				VAGMT_REV_NO.add(agmt_rev);
				VCONT_NO.add(cont);
				VCONT_REV_NO.add(cont_rev);
				VCONTRACT_TYPE.add(cont_type);
				VCARGO_NO.add(cargo_no);
				VCONT_REF_NO.add(cont_ref);
				
				VSIGNING_DT.add(signindt);
				VSTART_DT.add(startDt);
				VEND_DT.add(endDt);
				
				String rate = ""+rset.getDouble(14);
				String rate_unit = rset.getString(15)==null?"":rset.getString(15);
				
				double exchngRate=getExchangeRate(companyCd, countpty_cd, agmt, cont, cont_type, report_dt,buy_sell);
				if(rate_unit.equals("1"))
				{
					if(exchngRate>0)
					{
						rate=nf2.format(Double.parseDouble(rate)/exchngRate);
					}
				}
				VRATE.add(nf2.format(Double.parseDouble(rate)));
				//VRATE_UNIT.add(""+utilBean.getRateUnitNm(rate_unit));
				
				String phys_curve_nm="RLNG_PHYS_INDIA";
				String fin_curve_nm="";
				if(buy_sell.equals("Buy"))
				{
					phys_curve_nm="LNG_PHYS_INDIA";
				}
				String price_type="";
				String variable_rate="";
				String curve_nm="";
				String price_type_desc="FIXED";
				
				if(cont_type.equals("V"))
				{
					phys_curve_nm="";
					fin_curve_nm=hedge_curve_nm;
					price_type_desc="FIX/FLOAT";
				}
				else
				{
					int days=utilDate.getDays(endDt, report_dt);
					String dt=report_dt;
					if(days<=0)
					{
						dt=endDt;
					}
					String queryString1="SELECT PHYS_CURVE_NM,PRICE_TYPE,RATE,RATE_UNIT,CURVE_NM,"
							+ "CURVE_LOGIC,FORMULA "
							+ "FROM FMS_CONT_PRICE_DTL "
							+ "WHERE COMPANY_CD=? AND MAPPING_ID=? AND CONTRACT_TYPE=? "
							+ "AND FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND TO_DT>=TO_DATE(?,'DD/MM/YYYY')";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, companyCd);
					stmt1.setString(2, contPriceMapping);
					stmt1.setString(3, cont_type);
					stmt1.setString(4, report_dt);
					stmt1.setString(5, report_dt);
					rset1 = stmt1.executeQuery();
					if(rset1.next())
					{
						phys_curve_nm=rset1.getString(1)==null?"":rset1.getString(1);
						price_type=rset1.getString(2)==null?"":rset1.getString(2);
						
						variable_rate=nf.format(rset1.getDouble(3));
						String var_rate_unit=rset1.getString(4)==null?"":rset1.getString(4);
						curve_nm=rset1.getString(5)==null?"":rset1.getString(5);
						String curve_logic=rset1.getString(6)==null?"":rset1.getString(6);
						String formula_desc=rset1.getString(7)==null?"":rset1.getString(7);
						
						if(var_rate_unit.equals("1"))
						{
							if(exchngRate>0)
							{
								variable_rate=nf2.format(Double.parseDouble(variable_rate)/exchngRate);
							}
						}
						else
						{
							variable_rate=nf2.format(rset1.getDouble(3));
						}
						
						if(price_type.equals("F"))
						{
							//price_type_desc="FIXED : Variable";
							price_type_desc="FIXED";
						}
						else if(price_type.equals("M"))
						{
							if(curve_logic.equals("MIN") || curve_logic.equals("MAX") || curve_logic.equals("AVG"))
							{
								price_type_desc="FLOAT";
								fin_curve_nm=curve_logic.substring(0,1)+curve_logic.substring(1, curve_logic.length()).toLowerCase()+" of ["+formula_desc.substring(4,formula_desc.length()).replaceAll("@", ",")+"]";
							}
							else if(curve_logic.equals("MULTI_LEG"))
							{
								String temp[]=formula_desc.split("@");
								price_type_desc="FLOAT";
								fin_curve_nm=curve_nm+" "+temp[0]+"("+temp[1]+","+temp[2]+")";
							}
							else
							{
								price_type_desc="FLOAT";
								fin_curve_nm=curve_nm;
							}
						}
						else
						{
							price_type_desc="FIXED";
						}
					}
					rset1.close();
					stmt1.close();
				}
				
				if(!variable_rate.equals("") && !variable_rate.equals(rate))
				{
					VPRICE_LINE_RATE.add(variable_rate);
				}
				else
				{
					VPRICE_LINE_RATE.add("");
				}
				
				VPHYS_CURVE_NM.add(phys_curve_nm);
				VFIN_CURVE_NM.add(fin_curve_nm);
				
				VPRICE_TYPE_MST.add(price_type_desc);
				
				String cont_status_flg=rset.getString(16)==null?"":rset.getString(16);
				VCONT_STATUS_FLG.add(cont_status_flg);
				VCONT_STATUS.add(""+utilBean.ContStatusName(cont_status_flg));
			}
			rset.close();
			stmt.close();
			
			/*
			//ADDING FOR STORAGE
			String storageDate=utilDate.getDate(report_dt, "1");
			String ownerAbbr=utilBean.getCompanyAbbr(conn,"1");
			
			VACCOUNT.add("Buy");
			String contPriceMapping="0-0-0";
			VMAPPING_ID.add(contPriceMapping);//FOR EOD_PROCESS
			
			VLEGAL_ENTITY_CD.add("1");
			VLEGAL_ENTITY.add(""+utilBean.getCompanyAbbr(conn,"1"));
			VCOUNTERPARTY_CD.add("0");
			VCOUNTERPARTY_ABBR.add(ownerAbbr+"-STORAGE");
			VCOUNTERPARTY_NM.add(ownerAbbr+"-STORAGE");
			String dealNo=utilBean.NewDealMappingId("1", "0", "0", "0", "0", "0", "Z", "");
			VDEAL_NUM_EOD.add(dealNo);//FOR EOD
			
			VDISPLAY_DEAL_MAP.add(dealNo);
			VAGMT_NO.add("0");
			VAGMT_REV_NO.add("0");
			VCONT_NO.add("0");
			VCONT_REV_NO.add("0");
			VCONTRACT_TYPE.add("Z");
			VCARGO_NO.add("");
			VCONT_REF_NO.add("STORAGE TANK");
			
			VSIGNING_DT.add(storageDate);
			VSTART_DT.add(storageDate);
			VEND_DT.add(storageDate);
			
			String rate = "0";
			String rate_unit = "2";
			VRATE.add(nf2.format(Double.parseDouble(rate)));
			//VRATE_UNIT.add(""+utilBean.getRateUnitNm(rate_unit));
			
			String phys_curve_nm="LNG_PHYS_INDIA";
			VPRICE_LINE_RATE.add("");
			VPHYS_CURVE_NM.add(phys_curve_nm);
			VFIN_CURVE_NM.add("");
			VPRICE_TYPE_MST.add("FIXED");
			VCONT_STATUS_FLG.add("Y");
			VCONT_STATUS.add(""+ContStatusName("Y"));
			/////////////
			 */
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    public Double getExchangeRate(String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String contract_type, String date, String buy_sell)
	{
    	String function_nm="getExchangeRate()";
		double exchangRate=0;
		try
		{
			String exchng_rate_cd="";
			String exchang_criteria="";
			String exchng_rate_cal="";
			String fixed_exchng_val="";
			
			if(buy_sell.equals("Sell"))
			{
				String queryString_temp="SELECT EXCHNG_RATE_CD,EXCHNG_CRITERIA,EXCHNG_RATE_CAL,"
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
				rset_temp = stmt_temp.executeQuery();
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
					String queryString_temp1="SELECT EXCHG_VAL "
							+ "FROM FMS_EXCHG_RATE_ENTRY A "
							+ "WHERE EXCHG_RATE_CD=? "
							+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B "
							+ "WHERE A.EXCHG_RATE_CD=B.EXCHG_RATE_CD AND B.EFF_DT <= TO_DATE(?,'DD/MM/YYYY'))";
					stmt_temp1 = conn.prepareStatement(queryString_temp1);
					stmt_temp1.setString(1, exchng_rate_cd);
					stmt_temp1.setString(2, date);
					rset_temp1 = stmt_temp1.executeQuery();
					if(rset_temp1.next())
					{
						exchangRate=rset_temp1.getDouble(1);
					}
					
					rset_temp1.close();
					stmt_temp1.close();
				}
			}
			
			if(exchangRate==0) //IF EXCHNG_RATE==0, DEFAULT 'Shell Treasury Rate' WILL BE CONSIDERED
			{
				String rate_nm="Shell Treasury Rate";
				
				String queryString_temp="SELECT EXC_RATE_CD "
						+ "FROM FMS_EXCHG_RATE_MST "
						+ "WHERE UPPER(EXC_RATE_NM) = ?"; 
				stmt_temp = conn.prepareStatement(queryString_temp);
				stmt_temp.setString(1, rate_nm.toUpperCase());
				rset_temp = stmt_temp.executeQuery();
				if(rset_temp.next())
				{
					exchng_rate_cd = rset_temp.getString(1)==null?"":rset_temp.getString(1);
				}
				
				rset_temp.close();
				stmt_temp.close();
				
				String queryString_temp1="SELECT EXCHG_VAL "
						+ "FROM FMS_EXCHG_RATE_ENTRY A "
						+ "WHERE EXCHG_RATE_CD=? "
						+ "AND EFF_DT =(SELECT MAX(B.EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B "
						+ "WHERE A.EXCHG_RATE_CD=B.EXCHG_RATE_CD  "
						+ "AND B.EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) AND FLAG=?";
				stmt_temp1 = conn.prepareStatement(queryString_temp1);
				stmt_temp1.setString(1, exchng_rate_cd);
				stmt_temp1.setString(2, "Y");
				rset_temp1 = stmt_temp1.executeQuery();
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
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return nm;
	}
    
    public void getContractDetail()
	{
    	String function_nm="getContractDetail()";
		try
		{
			if(contract_type.equals("Z"))
			{
				String storageDate=utilDate.getDate(report_dt, "1");
				String ownerAbbr=utilBean.getCompanyAbbr(conn, comp_cd);
				
				counterparty_nm=ownerAbbr+"-STORAGE";
				
				deal_ref_no="STORAGE TANK";
				deal_dt=storageDate;
				start_dt=storageDate;
				end_dt=storageDate;
				
				display_deal_mapp=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, cargo_no);
				
				rate_unit="2";
				rate=utilBean.RateNumberFormat(0, rate_unit);
				
				getStorageExposure(comp_cd);
				dcq=nf.format(storage_mmbtu);
			}
			else
			{
				counterparty_nm=utilBean.getCounterpartyName(conn,counterparty_cd);
				
				String queryString="";
				if(contract_type.equals("V"))
				{
					queryString="SELECT A.CONT_REF_NO,NULL,"
							+ "TO_CHAR(A.SIGNING_DT,'DD/MM/YYYY'),TO_CHAR(B.CONT_DD_MM_YR,'DD/MM/YYYY'),TO_CHAR(B.CONT_DD_MM_YR,'DD/MM/YYYY'),NULL,"
							+ "B.ENT_BY,TO_CHAR(B.ENT_DT,'DD/MM/YYYY'),NULL,NULL,B.QTY,"
							+ "B.RATE,TO_CHAR(B.RATE_UNIT),B.CONV_FACTOR,B.CURVE_NM "
							+ "FROM FMS_DERV_CONT_MST A, FMS_DERV_INSTRUMENT_MST B "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
							+ "AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND B.INSTRUMENT_NO=? AND A.AGMT_TYPE=? "
							+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_DERV_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.AGMT_TYPE=B.AGMT_TYPE) "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.AGMT_TYPE=B.AGMT_TYPE ";
					
				}
				else if(account.equals("Buy"))
				{
					if(contract_type.equals("N"))
					{
						queryString="SELECT B.CARGO_REF,NULL,"
								+ "TO_CHAR(A.SIGNING_DT,'DD/MM/YYYY'),TO_CHAR(B.START_DT,'DD/MM/YYYY'),TO_CHAR(B.END_DT,'DD/MM/YYYY'),A.AGMT_BASE,"
								+ "A.ENT_BY,TO_CHAR(A.ENT_DT,'DD/MM/YYYY'),A.FCC_BY,TO_CHAR(A.FCC_DATE,'DD/MM/YYYY'),B.CARGO_QTY,"
								+ "B.RATE,B.RATE_UNIT,1,NULL "
								+ "FROM FMS_TRADER_CN_MST A, FMS_TRADER_CARGO_MST B "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
								+ "AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND A.AGMT_TYPE=? AND B.CARGO_NO=? "
								+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.AGMT_TYPE=B.AGMT_TYPE) "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
								+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.AGMT_TYPE=B.AGMT_TYPE ";
					}
					else
					{
						queryString="SELECT CONT_REF_NO,TRADE_REF_NO,"
								+ "TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),AGMT_BASE,"
								+ "ENT_BY,TO_CHAR(ENT_DT,'DD/MM/YYYY'),FCC_BY,TO_CHAR(FCC_DATE,'DD/MM/YYYY'),DCQ,"
								+ "RATE,RATE_UNIT,1,NULL "
								+ "FROM FMS_TRADER_CONT_MST A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
								+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
					}
				}
				else
				{
					queryString="SELECT CONT_REF_NO,TRADE_REF_NO,"
							+ "TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),AGMT_BASE,"
							+ "ENT_BY,TO_CHAR(ENT_DT,'DD/MM/YYYY'),FCC_BY,TO_CHAR(FCC_DATE,'DD/MM/YYYY'),DCQ,"
							+ "RATE,RATE_UNIT,1,NULL "
							+ "FROM FMS_SUPPLY_CONT_MST A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
							+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				}
				int st_count=0;
				stmt=conn.prepareStatement(queryString);
				stmt.setString(++st_count, comp_cd);
				stmt.setString(++st_count, counterparty_cd);
				stmt.setString(++st_count, agmt_no);
				stmt.setString(++st_count, cont_no);
				stmt.setString(++st_count, contract_type);
				if(contract_type.equals("N"))
				{
					stmt.setString(++st_count, "M");
					stmt.setString(++st_count, cargo_no);
				}
				else if(contract_type.equals("V"))
				{
					stmt.setString(++st_count, cargo_no);
					stmt.setString(++st_count, "U");
				}
				rset=stmt.executeQuery();
				if(rset.next())
				{
					deal_ref_no=rset.getString(1)==null?"":rset.getString(1);
					if(contract_type.equals("X") || contract_type.equals("I") || contract_type.equals("W")) //X for sell side //I for Buy side //W for dlng sell side
					{
						deal_ref_no=rset.getString(2)==null?"":rset.getString(2);
					}
					deal_dt=rset.getString(3)==null?"":rset.getString(3);
					start_dt=rset.getString(4)==null?"":rset.getString(4);
					end_dt=rset.getString(5)==null?"":rset.getString(5);
					if(contract_type.equals("V"))
					{
						start_dt=utilDate.getLastDateOfMonth(start_dt);
						end_dt=utilDate.getLastDateOfMonth(end_dt);
					}
					String agmt_base=rset.getString(6)==null?"":rset.getString(6);
					
					//display_deal_mapp=utilBean.getDisplayDealMapping(agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type);
					display_deal_mapp=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, cargo_no);
					if(agmt_base.equals("D"))
					{
						display_deal_mapp=display_deal_mapp+" <font style='background: #a6ff4d;'>[DLV]</font>";
					}
					entered_cd=rset.getString(7)==null?"":rset.getString(7);
					entered_by=utilBean.getEmpName(conn,entered_cd);
					entered_dt=rset.getString(8)==null?"":rset.getString(8);
					approved_cd=rset.getString(9)==null?"":rset.getString(9);
					approved_by=utilBean.getEmpName(conn,approved_cd);
					approved_dt=rset.getString(10)==null?"":rset.getString(10);
					dcq=nf.format(rset.getDouble(11));
					if(contract_type.equals("N"))
					{
						int noOfDay=utilDate.getDays(end_dt, start_dt);
						if(noOfDay>0)
						{
							dcq=nf.format(rset.getDouble(11)/noOfDay);
						}
					}
					
					rate_unit=rset.getString(13)==null?"":rset.getString(13);
					rate=utilBean.RateNumberFormat(rset.getDouble(12), rate_unit);
					
					double conversion_factor=rset.getDouble(14);
					if(contract_type.equals("V"))
					{
						dcq=nf.format(rset.getDouble(11) * conversion_factor);
					}
					else
					{
						dcq=nf.format(rset.getDouble(11));
					}
					
					derivative_curve_nm=rset.getString(15)==null?"":rset.getString(15);
				}
				rset.close();
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    double storage_mmbtu=0;
	public void getStorageExposure(String temp_own_cd)
	{
		String function_nm="getStorageExposure()";
		storage_mmbtu=0;
		try
		{
			String storageDt=utilDate.getDate(report_dt, "1");
			if(!temp_own_cd.equals(""))
			{
				VCOMPANY_CD.add(temp_own_cd);
				VCOMPANY_ABBR.add("");
				VCOMPANY_NAME.add("");
			}
			
			VSTORAGE_ROW_HEADING.add("Tank Operational Stock");
			VSTORAGE_ROW_HEADING.add("Tank Dead Stock");
			VSTORAGE_ROW_HEADING.add("Tank Effective Stock");
			VSTORAGE_ROW_HEADING.add("Third Party Balance (Obligation - Supplied)");
			VSTORAGE_ROW_HEADING.add("LTCORA Purchase (Obligation - Supplied)");
			VSTORAGE_ROW_HEADING.add("Tank Stock");
			
			
			storage_collapse_info="";
			for(int i=0;i<VCOMPANY_CD.size(); i++)
			{
				String comp_cd=""+VCOMPANY_CD.elementAt(i);
				
				///Sell side LTCORA /////////////////////////////////////////////////////////
				double strg_qty=0;
				String queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,B.CARGO_NO,A.SUG,SUM(ADQ_QTY) "
						+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_CARGO_DTL B,"
							+ "FMS_LTCORA_CONT_CARGO_ADQ C "
						+ "WHERE A.COMPANY_CD=? AND A.BUY_SALE=? AND B.CARGO_STATUS=? AND A.AGMT_TYPE=? "
						+ "AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0) >=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE "
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND B.CARGO_NO=C.CARGO_NO AND A.CONT_NO=C.CONT_NO "
						+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.BUY_SALE=C.BUY_SALE AND A.AGMT_TYPE=C.AGMT_TYPE "
						+ "AND C.ADQ_DT <= TO_DATE(?,'DD/MM/YYYY') "
						+ "GROUP BY A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,B.CARGO_NO,A.SUG ";
				int st_count=0;
				stmt=conn.prepareStatement(queryString);
				stmt.setString(++st_count, comp_cd);
				stmt.setString(++st_count, "C");
				stmt.setString(++st_count, "Y");
				stmt.setString(++st_count, "A");
				stmt.setString(++st_count, report_dt);
				stmt.setString(++st_count, report_dt);
				stmt.setString(++st_count, report_dt);
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
					String cargo_no = rset.getString(8)==null?"":rset.getString(8);
					String sug_percent = rset.getString(9)==null?"":nf.format(rset.getDouble(9));
					double adq_qty=rset.getDouble(10);
					
					String queryString1="SELECT LTCORA_TARIFF,LTCORA_TARIFF_UNIT,SUG "
							+ "FROM FMS_LTCORA_CONT_CARGO_MOD "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=? AND CONT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=? ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, own_cd);
					stmt1.setString(2, countpty_cd);
					stmt1.setString(3, "C");
					stmt1.setString(4, agmtno);
					stmt1.setString(5, agmtrev);
					stmt1.setString(6, "A");
					stmt1.setString(7, contno);
					stmt1.setString(8, cont_type);
					stmt1.setString(9, cargo_no);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						sug_percent = rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
					}
					rset1.close();
					stmt1.close();
					
					if(adq_qty > 0 && !sug_percent.equals(""))
					{
						strg_qty+= adq_qty - (adq_qty * Double.parseDouble(sug_percent) / 100);
					}
					
					double actual_qty=0;
					queryString1="SELECT SUM(QTY_MMBTU) "
			  				+ "FROM FMS_DAILY_ALLOCATION_DTL A "
			  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
							+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONTRACT_TYPE=? AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "//AND CARGO_NO=? "
							+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
							+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
							+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, contno);
					stmt1.setString(2, agmtno);
					stmt1.setString(3, own_cd);
					stmt1.setString(4, countpty_cd);
					stmt1.setString(5, cont_type);
					stmt1.setString(6, report_dt);
					//stmt1.setString(7, cargo_no);
					rset1 = stmt1.executeQuery();
					if(rset1.next())
					{
						actual_qty = rset1.getDouble(1);
					}
					rset1.close();
					stmt1.close();
					
					strg_qty = strg_qty - actual_qty; 
				}
				rset.close();
				stmt.close();
				
				//LTCORA Purchase Side ///////////////////////////////////////////////////////////////////////////
				double buy_strg_qty=0;
				
				queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,B.CARGO_NO,A.SUG,SUM(ADQ_QTY) "
						+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_CARGO_DTL B,"
							+ "FMS_LTCORA_CONT_CARGO_ADQ C "
						+ "WHERE A.COMPANY_CD=? AND A.BUY_SALE=? AND B.CARGO_STATUS=? AND A.AGMT_TYPE=? "
						+ "AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0) >=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE "
						+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND B.CARGO_NO=C.CARGO_NO AND A.CONT_NO=C.CONT_NO "
						+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE AND A.BUY_SALE=C.BUY_SALE AND A.AGMT_TYPE=C.AGMT_TYPE "
						+ "AND C.ADQ_DT <= TO_DATE(?,'DD/MM/YYYY') "
						+ "GROUP BY A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,B.CARGO_NO,A.SUG ";
				st_count=0;
				stmt=conn.prepareStatement(queryString);
				stmt.setString(++st_count, comp_cd);
				stmt.setString(++st_count, "T");
				stmt.setString(++st_count, "Y");
				stmt.setString(++st_count, "L");
				stmt.setString(++st_count, report_dt);
				stmt.setString(++st_count, report_dt);
				stmt.setString(++st_count, report_dt);
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
					String cargo_no = rset.getString(8)==null?"":rset.getString(8);
					String sug_percent = rset.getString(9)==null?"":nf.format(rset.getDouble(9));
					double adq_qty=rset.getDouble(10);
					
					String queryString1="SELECT LTCORA_TARIFF,LTCORA_TARIFF_UNIT,SUG "
							+ "FROM FMS_LTCORA_CONT_CARGO_MOD "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=? AND CONT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=? ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, own_cd);
					stmt1.setString(2, countpty_cd);
					stmt1.setString(3, "T");
					stmt1.setString(4, agmtno);
					stmt1.setString(5, agmtrev);
					stmt1.setString(6, "L");
					stmt1.setString(7, contno);
					stmt1.setString(8, cont_type);
					stmt1.setString(9, cargo_no);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						sug_percent = rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
					}
					rset1.close();
					stmt1.close();
					
					if(adq_qty > 0 && !sug_percent.equals(""))
					{
						buy_strg_qty+= adq_qty - (adq_qty * Double.parseDouble(sug_percent) / 100);
					}
					
					double actual_qty=0;
					queryString1="SELECT SUM(QTY_MMBTU) "
			  				+ "FROM FMS_BUY_DAILY_ALLOCATION A "
			  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
							+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONTRACT_TYPE=? AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
							+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
							+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
							+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, contno);
					stmt1.setString(2, agmtno);
					stmt1.setString(3, own_cd);
					stmt1.setString(4, countpty_cd);
					stmt1.setString(5, cont_type);
					stmt1.setString(6, report_dt);
					stmt1.setString(7, cargo_no);
					rset1 = stmt1.executeQuery();
					if(rset1.next())
					{
						actual_qty = rset1.getDouble(1);
					}
					rset1.close();
					stmt1.close();
					
					buy_strg_qty = buy_strg_qty - actual_qty; 
				}
				rset.close();
				stmt.close();
				
				double tank_mmbtu=0;
				double tank_mmscm=0;
				double FactorM3ToMMSCM=595; 
				double FactorMMSCMtoMMBTU=38900;
				
				double dead_stock_mmscm=0;
				double dead_stock_mmbtu=0;
				
				String inventory_dt="";
				queryString="SELECT SUM(A.TANK_MMBTU),SUM(B.TANK_D1_VOLUME+B.TANK_D2_VOLUME),A.TANK_CONV_FACTOR_1,A.TANK_CD,TO_CHAR(INV_LEVEL_DT,'DD/MM/YYYY') "
						+ "FROM FMS_TANK_INVENTORY_DTL A, FMS_TANK_MST B "
						+ "WHERE A.COMPANY_CD=? AND B.STATUS=? AND A.TANK_VOLUME > 0 "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.TANK_CD=B.TANK_CD "
						+ "AND A.INV_LEVEL_DT=(SELECT MAX(B.INV_LEVEL_DT) FROM FMS_TANK_INVENTORY_DTL B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND B.INV_LEVEL_DT<=TO_DATE(?,'DD/MM/YYYY')) "
						+ "AND B.EFF_DT=(SELECT MAX(A.EFF_DT) FROM FMS_TANK_MST A WHERE A.COMPANY_CD=B.COMPANY_CD AND A.TANK_CD=B.TANK_CD "
						+ "AND A.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) "
						+ "GROUP BY A.TANK_CONV_FACTOR_1,A.TANK_CD,TO_CHAR(INV_LEVEL_DT,'DD/MM/YYYY')";
				st_count=0;
				stmt=conn.prepareStatement(queryString);
				stmt.setString(++st_count, comp_cd);
				stmt.setString(++st_count, "Y");
				stmt.setString(++st_count, storageDt);
				stmt.setString(++st_count, storageDt);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					double temp_tank_mmbtu=rset.getDouble(1);
					double temp_dead_stock_vol=rset.getDouble(2);
					double temp_factor_m3_to_mmscm=rset.getDouble(3);
					
					inventory_dt=rset.getString(5)==null?"":rset.getString(5);
					
					if(temp_factor_m3_to_mmscm==0)
					{
						temp_factor_m3_to_mmscm=FactorM3ToMMSCM;
					}
					
					double temp_dead_stock_mmscm=(temp_dead_stock_vol * temp_factor_m3_to_mmscm)/1000000;
					
					tank_mmbtu+=temp_tank_mmbtu;
					tank_mmscm+=(temp_tank_mmbtu/FactorMMSCMtoMMBTU);
					
					dead_stock_mmscm+=temp_dead_stock_mmscm;
					dead_stock_mmbtu+=(temp_dead_stock_mmscm*FactorMMSCMtoMMBTU);
				}
				rset.close();
				stmt.close();
				
				double tank_eff_mmbtu=tank_mmbtu-dead_stock_mmbtu;
				double tank_eff_mmscm=tank_mmscm-dead_stock_mmscm;
				
				double strg_qty_mmscm=strg_qty/FactorMMSCMtoMMBTU;
				double buy_strg_qty_mmscm=buy_strg_qty/FactorMMSCMtoMMBTU;
				
				double total_stock_mmscm=tank_eff_mmscm-strg_qty_mmscm;
				total_stock_mmscm=total_stock_mmscm+buy_strg_qty_mmscm;
				
				double total_stock_mmbtu=tank_eff_mmbtu-strg_qty;
				total_stock_mmbtu=total_stock_mmbtu+buy_strg_qty;
				
				storage_mmbtu=total_stock_mmbtu;
				
				VSTORAGE_MMSCM.add(nf.format(tank_mmscm));
				VSTORAGE_MMSCM.add("(-) "+nf.format(dead_stock_mmscm));
				VSTORAGE_MMSCM.add(nf.format(tank_eff_mmscm));
				VSTORAGE_MMSCM.add("(-) "+nf.format(strg_qty_mmscm));
				VSTORAGE_MMSCM.add("(+) "+nf.format(buy_strg_qty_mmscm));
				VSTORAGE_MMSCM.add(nf.format(total_stock_mmscm));
				
				VSTORAGE_MMBTU.add(nf.format(tank_mmbtu));
				VSTORAGE_MMBTU.add("(-) "+nf.format(dead_stock_mmbtu));
				VSTORAGE_MMBTU.add(nf.format(tank_eff_mmbtu));
				VSTORAGE_MMBTU.add("(-) "+nf.format(strg_qty));
				VSTORAGE_MMBTU.add("(+) "+nf.format(buy_strg_qty));
				VSTORAGE_MMBTU.add(nf.format(total_stock_mmbtu));
				
				VSTORAGE_LAST_DT.add(inventory_dt.equals("")?"":inventory_dt.equals(storageDt)? "Last Update on "+inventory_dt:"<font color='red'>Last Update on "+inventory_dt+"</fonr>");
				VSTORAGE_LAST_DT.add("");
				VSTORAGE_LAST_DT.add("");
				VSTORAGE_LAST_DT.add("");
				VSTORAGE_LAST_DT.add("");
				VSTORAGE_LAST_DT.add("");
				
				//storage_collapse_info+=" ("+VCOMPANY_ABBR.elementAt(i)+" : "+nf.format(total_stock_mmbtu)+")";
			}
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
			
			double exchngRate=getExchangeRate(comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, report_dt,account);
			if(rate_unit.equals("1"))
			{	
				if(exchngRate>0)
				{
					rate=nf2.format(Double.parseDouble(rate)/exchngRate);
				}
			}
			
			double sign=-1;
			if(account.equals("Buy"))
			{
				sign=1;
			}
			int sr=0;
			String queryString="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
					+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(?,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
					+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
					+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, start_dt);
			stmt.setString(2, end_dt);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String gas_dt=rset.getString(1)==null?"":rset.getString(1);
				String cont_month="01/"+gas_dt.substring(3,gas_dt.length());
				
				String contPriceMapping=counterparty_cd+"-"+agmt_no+"-"+cont_no;
				if(contract_type.equals("N"))
				{
					contPriceMapping+="-"+cargo_no;
				}
				
				String var_dcq=utilBean.getContVariableTAQ(conn,comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, gas_dt);
				if(var_dcq.equals(""))
				{
					if(account.equals("Sell"))
					{
						var_dcq=utilBean.getContVariableDCQ(conn,comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, gas_dt);
						if(var_dcq.equals(""))
						{
							var_dcq=dcq;
						}
					}
					else if(contract_type.equals("N"))
					{
						var_dcq=dcq;
					}
					else
					{
						var_dcq=utilBean.getPurchaseContVariableDCQ(conn,comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, gas_dt);
						if(var_dcq.equals(""))
						{
							var_dcq=dcq;
						}
					}
				}
				double temp_var_dcq=Double.parseDouble(var_dcq);
				var_dcq=nf.format(Double.parseDouble(var_dcq) * sign);
				
				double actual_qty=0;
				double temp_actual_qty=0;
				double weighted_average=0;
				if(contract_type.equals("V"))
				{
					temp_actual_qty=Double.parseDouble(var_dcq);
					actual_qty=Double.parseDouble(var_dcq);
				}
				else if(utilDate.getDays(gas_dt, report_dt) <= 1) //AS PER INC#2410083
				{
					if(account.equals("Sell"))
					{
						//actual_qty=Double.parseDouble(""+utilBean.getSupplyAllocationQty(conn,comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, gas_dt));
						if(contract_type.equals("E") || contract_type.equals("F") || contract_type.equals("W")) //DLNG Deals
						{
							actual_qty=Double.parseDouble(""+utilAlloc.getDlngAllocationQty(conn, comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, gas_dt));
						}
						else //RLNG Deals
						{
							double weighted_terms=0; 
							String queryString1="SELECT A.PLANT_SEQ_NO,SUM(B.QTY_MMBTU) "
									+ "FROM FMS_SUPPLY_CONT_PLANT A, FMS_DAILY_ALLOCATION_DTL B "
									+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? "
									+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONTRACT_TYPE=? "
									+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
									+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
									+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
									+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO "
									+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ AND B.GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND B.NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL C "
									+ "WHERE C.CONT_NO=B.CONT_NO AND C.AGMT_NO=B.AGMT_NO "
									+ "AND C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
									+ "AND C.TRANSPORTER_CD=B.TRANSPORTER_CD AND C.TRANS_SEQ=B.TRANS_SEQ "
									+ "AND C.PLANT_SEQ=B.PLANT_SEQ AND C.CONTRACT_TYPE=B.CONTRACT_TYPE AND C.BU_SEQ=B.BU_SEQ "
									+ "AND C.GAS_DT=B.GAS_DT AND C.CARGO_NO=B.CARGO_NO) "
									+ "GROUP BY A.PLANT_SEQ_NO ";
							stmt1=conn.prepareStatement(queryString1);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, counterparty_cd);
							stmt1.setString(3, cont_no);
							stmt1.setString(4, agmt_no);
							stmt1.setString(5, agmt_rev_no);
							stmt1.setString(6, contract_type);
							stmt1.setString(7, gas_dt);
							rset1=stmt1.executeQuery();
							while(rset1.next())
							{
								String plant_seq=rset1.getString(1)==null?"":rset1.getString(1);
								double temp_qty=rset1.getDouble(2);
								actual_qty+=temp_qty;
								
								String queryString2="SELECT B.CHARGE_ABBR,TO_CHAR(A.EFF_DT,'DD/MM/YYYY'),A.CHARGE_RATE "
										+ "FROM FMS_CHARGE_MST B LEFT JOIN FMS_SUPPLY_CONT_PLANT_CHRG A ON A.CHARGE_ABBR=B.CHARGE_ABBR "
										+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? "
										+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONTRACT_TYPE=? AND A.PLANT_SEQ_NO=? AND A.CHARGE_RATE > 0 "
										+ "AND A.EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_SUPPLY_CONT_PLANT_CHRG B WHERE A.COMPANY_CD=B.COMPANY_CD "
										+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
										+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.CHARGE_ABBR=B.CHARGE_ABBR "
										+ "AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
								stmt2=conn.prepareStatement(queryString2);
								stmt2.setString(1, comp_cd);
								stmt2.setString(2, counterparty_cd);
								stmt2.setString(3, cont_no);
								stmt2.setString(4, agmt_no);
								stmt2.setString(5, agmt_rev_no);
								stmt2.setString(6, contract_type);
								stmt2.setString(7, plant_seq);
								stmt2.setString(8, gas_dt);
								rset2=stmt2.executeQuery();
								while(rset2.next())
								{
									double chrg=rset2.getDouble(3);
									weighted_terms+=temp_qty*chrg;
								}
								rset2.close();
								stmt2.close();
							}
							rset1.close();
							stmt1.close();
							
							if(weighted_terms > 0 && actual_qty > 0 && exchngRate > 0)
							{
								weighted_average=(weighted_terms/actual_qty)/exchngRate;
							}
						}
					}
					else
					{
						if(contract_type.equals("N"))
						{
							String queryString1 = "SELECT A.QQ_QTY_MMBTU "
									+ "FROM FMS_BUY_CARGO_ALLOC A "
					  				+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_TYPE=? "
					  				+ "AND A.CONT_NO=? AND A.AGMT_NO=? AND A.CONTRACT_TYPE=? AND A.CARGO_NO=? AND A.QQ_DT=TO_DATE(?,'DD/MM/YYYY')"
					  				+ "AND A.ALLOC_REV=(SELECT MAX(B.ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO AND B.AGMT_TYPE=A.AGMT_TYPE AND B.CARGO_NO=A.CARGO_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD AND B.CONTRACT_TYPE=A.CONTRACT_TYPE) ";
							stmt1=conn.prepareStatement(queryString1);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, counterparty_cd);
							stmt1.setString(3, "M");
							stmt1.setString(4, cont_no);
							stmt1.setString(5, agmt_no);
							stmt1.setString(6, contract_type);
							stmt1.setString(7, cargo_no);
							stmt1.setString(8, gas_dt);
							rset1=stmt1.executeQuery();
							if(rset1.next())
							{
								actual_qty=rset1.getDouble(1);
							}
							rset1.close();
							stmt1.close();
						}
						else
						{
							//actual_qty=Double.parseDouble(""+utilBean.getPurchaseAllocationQty(conn,comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, gas_dt));
							
							double weighted_terms=0; 
							String queryString1="SELECT A.PLANT_SEQ_NO,SUM(B.QTY_MMBTU) "
									+ "FROM FMS_TRADER_CONT_PLANT A, FMS_BUY_DAILY_ALLOCATION B "
									+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? "
									+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONTRACT_TYPE=? "
									+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
									+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
									+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
									+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO "
									+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ AND B.GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND B.NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION C "
									+ "WHERE C.CONT_NO=B.CONT_NO AND C.AGMT_NO=B.AGMT_NO "
									+ "AND C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
									+ "AND C.TRANSPORTER_CD=B.TRANSPORTER_CD AND C.TRANS_SEQ=B.TRANS_SEQ "
									+ "AND C.PLANT_SEQ=B.PLANT_SEQ AND C.CONTRACT_TYPE=B.CONTRACT_TYPE AND C.BU_SEQ=B.BU_SEQ "
									+ "AND C.GAS_DT=B.GAS_DT AND C.CARGO_NO=B.CARGO_NO) "
									+ "GROUP BY A.PLANT_SEQ_NO ";
							stmt1=conn.prepareStatement(queryString1);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, counterparty_cd);
							stmt1.setString(3, cont_no);
							stmt1.setString(4, agmt_no);
							stmt1.setString(5, agmt_rev_no);
							stmt1.setString(6, contract_type);
							stmt1.setString(7, gas_dt);
							rset1=stmt1.executeQuery();
							while(rset1.next())
							{
								String plant_seq=rset1.getString(1)==null?"":rset1.getString(1);
								double temp_qty=rset1.getDouble(2);
								actual_qty+=temp_qty;
								
								String queryString2="SELECT B.CHARGE_ABBR,TO_CHAR(A.EFF_DT,'DD/MM/YYYY'),A.CHARGE_RATE "
										+ "FROM FMS_CHARGE_MST B LEFT JOIN FMS_TRADER_CONT_PLANT_CHRG A ON A.CHARGE_ABBR=B.CHARGE_ABBR "
										+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? "
										+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONTRACT_TYPE=? AND A.PLANT_SEQ_NO=? AND A.CHARGE_RATE > 0 "
										+ "AND A.EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_TRADER_CONT_PLANT_CHRG B WHERE A.COMPANY_CD=B.COMPANY_CD "
										+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
										+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.CHARGE_ABBR=B.CHARGE_ABBR "
										+ "AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
								stmt2=conn.prepareStatement(queryString2);
								stmt2.setString(1, comp_cd);
								stmt2.setString(2, counterparty_cd);
								stmt2.setString(3, cont_no);
								stmt2.setString(4, agmt_no);
								stmt2.setString(5, agmt_rev_no);
								stmt2.setString(6, contract_type);
								stmt2.setString(7, plant_seq);
								stmt2.setString(8, gas_dt);
								rset2=stmt2.executeQuery();
								while(rset2.next())
								{
									double chrg=rset2.getDouble(3);
									weighted_terms+=temp_qty*chrg;
								}
								rset2.close();
								stmt2.close();
							}
							rset1.close();
							stmt1.close();
							
							if(weighted_terms > 0 && actual_qty > 0 && exchngRate > 0)
							{
								weighted_average=(weighted_terms/actual_qty)/exchngRate;
							}
						}
					}
				}
				else
				{
					if(account.equals("Sell"))
					{
						double weighted_terms=0;
						double total_terms=0;
						String queryString1="SELECT A.PLANT_SEQ_NO "
								+ "FROM FMS_SUPPLY_CONT_PLANT A "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? "
								+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONTRACT_TYPE=? "
								+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
								+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterparty_cd);
						stmt1.setString(3, cont_no);
						stmt1.setString(4, agmt_no);
						stmt1.setString(5, agmt_rev_no);
						stmt1.setString(6, contract_type);
						rset1=stmt1.executeQuery();
						while(rset1.next())
						{
							String plant_seq=rset1.getString(1)==null?"":rset1.getString(1);
							
							total_terms+=temp_var_dcq;
							
							String queryString2="SELECT B.CHARGE_ABBR,TO_CHAR(A.EFF_DT,'DD/MM/YYYY'),A.CHARGE_RATE "
									+ "FROM FMS_CHARGE_MST B LEFT JOIN FMS_SUPPLY_CONT_PLANT_CHRG A ON A.CHARGE_ABBR=B.CHARGE_ABBR "
									+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? "
									+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONTRACT_TYPE=? AND A.PLANT_SEQ_NO=? AND A.CHARGE_RATE > 0 "
									+ "AND A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_SUPPLY_CONT_PLANT_CHRG B WHERE A.COMPANY_CD=B.COMPANY_CD "
									+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
									+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.CHARGE_ABBR=B.CHARGE_ABBR "
									+ "AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
							stmt2=conn.prepareStatement(queryString2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, counterparty_cd);
							stmt2.setString(3, cont_no);
							stmt2.setString(4, agmt_no);
							stmt2.setString(5, agmt_rev_no);
							stmt2.setString(6, contract_type);
							stmt2.setString(7, plant_seq);
							stmt2.setString(8, gas_dt);
							rset2=stmt2.executeQuery();
							while(rset2.next())
							{
								double chrg=rset2.getDouble(3);
								weighted_terms+=temp_var_dcq*chrg;
							}
							rset2.close();
							stmt2.close();
						}
						rset1.close();
						stmt1.close();
						
						if(weighted_terms > 0 && total_terms > 0 && exchngRate > 0)
						{
							weighted_average=(weighted_terms/total_terms)/exchngRate;
						}
					}
					else if(!contract_type.equals("N"))
					{
						double weighted_terms=0;
						double total_terms=0;
						String queryString1="SELECT A.PLANT_SEQ_NO "
								+ "FROM FMS_TRADER_CONT_PLANT A "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? "
								+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONTRACT_TYPE=? "
								+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
								+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterparty_cd);
						stmt1.setString(3, cont_no);
						stmt1.setString(4, agmt_no);
						stmt1.setString(5, agmt_rev_no);
						stmt1.setString(6, contract_type);
						rset1=stmt1.executeQuery();
						while(rset1.next())
						{
							String plant_seq=rset1.getString(1)==null?"":rset1.getString(1);
							
							total_terms+=temp_var_dcq;
							
							String queryString2="SELECT B.CHARGE_ABBR,TO_CHAR(A.EFF_DT,'DD/MM/YYYY'),A.CHARGE_RATE "
									+ "FROM FMS_CHARGE_MST B LEFT JOIN FMS_TRADER_CONT_PLANT_CHRG A ON A.CHARGE_ABBR=B.CHARGE_ABBR "
									+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? "
									+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONTRACT_TYPE=? AND A.PLANT_SEQ_NO=? AND A.CHARGE_RATE > 0 "
									+ "AND A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_TRADER_CONT_PLANT_CHRG B WHERE A.COMPANY_CD=B.COMPANY_CD "
									+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
									+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.CHARGE_ABBR=B.CHARGE_ABBR "
									+ "AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
							stmt2=conn.prepareStatement(queryString2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, counterparty_cd);
							stmt2.setString(3, cont_no);
							stmt2.setString(4, agmt_no);
							stmt2.setString(5, agmt_rev_no);
							stmt2.setString(6, contract_type);
							stmt2.setString(7, plant_seq);
							stmt2.setString(8, gas_dt);
							rset2=stmt2.executeQuery();
							while(rset2.next())
							{
								double chrg=rset2.getDouble(3);
								weighted_terms+=temp_var_dcq*chrg;
							}
							rset2.close();
							stmt2.close();
						}
						rset1.close();
						stmt1.close();
						
						if(weighted_terms > 0 && total_terms > 0 && exchngRate > 0)
						{
							weighted_average=(weighted_terms/total_terms)/exchngRate;
						}
					}
				}
				
				temp_actual_qty=actual_qty;
				if(temp_actual_qty!=0)
				{
					temp_actual_qty=sign*temp_actual_qty;
				}
				
				if(actual_qty!=0)
				{
					actual_qty=sign*actual_qty;
				}
				
				/*VGAS_DT.add(gas_dt);
				VCONT_MMYYYY.add(gas_dt.substring(3,gas_dt.length()));
				VDCQ.add(Double.parseDouble(var_dcq));
				VACTUAL_QTY.add(nf.format(actual_qty));
				*/
				
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
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, contPriceMapping);
				stmt1.setString(3, contract_type);
				stmt1.setString(4, gas_dt);
				stmt1.setString(5, gas_dt);
				rset1 = stmt1.executeQuery();
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
					
					if(var_rate_unit.equals("1"))
					{
						if(exchngRate>0)
						{
							variable_rate=nf2.format(Double.parseDouble(variable_rate)/exchngRate);
						}
					}
				}
				rset1.close();
				stmt1.close();
				
				if(phys_curve_nm.equals("") && !contract_type.equals("V"))
				{
					if(account.equals("Sell"))
					{
						phys_curve_nm="RLNG_PHYS_INDIA";
					}
					else
					{
						phys_curve_nm="LNG_PHYS_INDIA";
					}
				}
				
				String cont_price="";
				if(account.equals("Sell"))
				{
					queryString1 = "SELECT DISTINCT NEW_SALE_PRICE "
							+ "FROM FMS_SUPPLY_ALLOC_REVISED A "
							+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND CONT_NO=? "
							+ "AND COUNTERPARTY_CD=? AND FLAG=? AND CONTRACT_TYPE=? "
							+ "AND MODIFICATION_SEQ_NO = (SELECT MAX(MODIFICATION_SEQ_NO) FROM FMS_SUPPLY_ALLOC_REVISED B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.FLAG=B.FLAG AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
							+ "AND B.NEW_PRICE_EFF_DT <=TO_DATE(?,'DD/MM/YYYY'))";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, agmt_no);
					stmt1.setString(3, cont_no);
					stmt1.setString(4, counterparty_cd);
					stmt1.setString(5, "A");
					stmt1.setString(6, contract_type);
					stmt1.setString(7, gas_dt);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						cont_price=rset1.getString(1)==null?"":rset1.getString(1);
						
						if(rate_unit.equals("1"))
						{	
							if(exchngRate>0)
							{
								cont_price=nf2.format(Double.parseDouble(cont_price)/exchngRate);
							}
						}
					}
					rset1.close();
					stmt1.close();
				}
				else if(account.equals("Buy"))
				{
					queryString1 = "SELECT DISTINCT NEW_PRICE "
							+ "FROM FMS_TRADER_CONT_PRICE_DTL A "
							+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND CONT_NO=? "
							+ "AND COUNTERPARTY_CD=? AND FLAG=? AND CONTRACT_TYPE=? "
							+ "AND SEQ_NO = (SELECT MAX(SEQ_NO) FROM FMS_TRADER_CONT_PRICE_DTL B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.FLAG=B.FLAG AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
							+ "AND B.EFF_DT <=TO_DATE(?,'DD/MM/YYYY'))";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, agmt_no);
					stmt1.setString(3, cont_no);
					stmt1.setString(4, counterparty_cd);
					stmt1.setString(5, "Y");
					stmt1.setString(6, contract_type);
					stmt1.setString(7, gas_dt);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						cont_price=rset1.getString(1)==null?"":rset1.getString(1);
						
						if(rate_unit.equals("1"))
						{	
							if(exchngRate>0)
							{
								cont_price=nf2.format(Double.parseDouble(cont_price)/exchngRate);
							}
						}
					}
					rset1.close();
					stmt1.close();
				}
				
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
				
				int phyCountRu=utilDate.getDays(gas_dt, report_dt);
				String phyRu="";
				if(phyCountRu<=1)
				{
					phyRu="R";
				}
				else
				{
					phyRu="U";
				}
				
				
				if(contract_type.equals("V"))
				{
					//FIXED SIDE
					VTEMP_CURVE.add("Fixed");
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
					//var_dcq=nf.format(Double.parseDouble(var_dcq) * sign);
					if(account.equals("Buy"))
					{
						var_dcq=nf.format(Double.parseDouble(var_dcq) * -1);
						if(actual_qty!=0)
						{
							actual_qty=-1*actual_qty;
						}
					}
					else
					{
						var_dcq=nf.format(Double.parseDouble(var_dcq) * -1);
						if(actual_qty!=0)
						{
							actual_qty=-1*actual_qty;
						}
					}
					curve_nm=derivative_curve_nm;
					VTEMP_CURVE.add("Float(SINGLE)");
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
						//VTEMP_CURVE.add("Float("+curve_logic+") : "+curve_nm);
						VTEMP_CURVE.add("Float("+curve_logic+")"); //INC#2410042
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
						stmt1.setString(3, contract_type);
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
							
							int count_ru=utilDate.getDays(""+temp.elementAt(2), report_dt);
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
							
							int fin_count_ru=utilDate.getDays(""+temp.elementAt(2), report_dt);
							
							int count_u=0;
							int count_r=0;
							
							if(fin_count_ru>1)
							{	
								int isSettlmentPeriodActive=utilDate.isDateWithinPeriod(""+temp.elementAt(1),""+temp.elementAt(2),report_dt);
								
								if(isSettlmentPeriodActive==1)
								{
									String nextdt=utilDate.getDate(report_dt, "1"); 
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

							//int count_withoutzero=0; //JD
							int settle_count_withoutzero=0;
							double totalPrice=0;
							double totalSettlePrice=0;
							
							int count = Integer.parseInt(m1)+(Integer.parseInt(m2)-Integer.parseInt(m1))+1;
							int multiply_factor=-1;
							if(m0.equals("Forward"))
							{
								multiply_factor=1;
							}
							
							for(int i=Integer.parseInt(m1);i<=Integer.parseInt(m2);i++)
							{
								//VTEMP_CURVE.add("Float("+curve_logic+") : "+curve_nm);
								VTEMP_CURVE.add("Float("+curve_logic+")"); //INC#2410042
								VTEMP_CURVE_NM.add(curve_nm);
								VTEMP_SLOPE.add(slope);
								VTEMP_CONST.add(constant);
								VTEMP_DCQ.add(temp_dcq);
								VTEMP_ALLOC_QTY.add(temp_alloc_qty);
								
								int temp_i=i*multiply_factor;
								
								String multi_cont_month="";
								queryString1="SELECT TO_CHAR(ADD_MONTHS(TO_DATE(?,'DD/MM/YYYY'),?),'MM/YYYY') "
										+ "FROM DUAL";
								stmt1 = conn.prepareStatement(queryString1);
								stmt1.setString(1, gas_dt);
								stmt1.setString(2, ""+temp_i);
								rset1 = stmt1.executeQuery();
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
								
								int count_ru=utilDate.getDays(""+temp.elementAt(2), report_dt);
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
								
								//JD 
								/*
								avgPriceInfo+="\nEff. Price ("+temp.elementAt(0)+") = "+nf2.format(settle_price)+" * "+slope+" + "+constant;
								settle_price=settle_price * Double.parseDouble(slope) + Double.parseDouble(constant);
								
								avgPriceInfo+=" = "+nf2.format(settle_price);
								
								if(settle_price>0)
								{
									totalPrice+=settle_price;
									count_withoutzero+=1;
								}
								*/ 
								VTEMP_SETTLE_PRICE.add(nf2.format(settle_price));
								VTEMP_AVG_SETTLE_PRICE.add("");
								VTEMP_EFF_PRICE.add("");
								VTEMP_EFF_PRICE_INFO.add("");
							}
							//JD
							//avgPrice=totalPrice/count_withoutzero;
							
							if(settle_count_withoutzero>0) {
								avgSettlePrice=totalSettlePrice/settle_count_withoutzero; // JD -- Moved UP 
							}
							avgPrice = avgSettlePrice * Double.parseDouble(slope) + Double.parseDouble(constant); //JD - added
							avgPrice+=weighted_average;
						}
						else
						{
							//VTEMP_CURVE.add("Float(SINGLE) : "+curve_nm);
							VTEMP_CURVE.add("Float(SINGLE)"); //INC#2410042
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
					
					if(contract_type.equals("V"))
					{
						sr=sr + 1;
						VSR.add(sr);
						VGAS_DT.add(gas_dt);
						VCONT_MMYYYY.add(gas_dt.substring(3,gas_dt.length()));
						//VDCQ.add(Double.parseDouble(var_dcq));
						
						//VACTUAL_QTY.add(nf.format(actual_qty));
						//VACTUAL_QTY.add(""+VTEMP_ALLOC_QTY.elementAt(i));
						VACTUAL_QTY.add("0.00");
						
						VTOTAL_CHARGE.add(nf2.format(weighted_average));
						
						//tot_actual_qty+=temp_actual_qty;
						
						if(i>0)
						{
							price_type="M";
							
							VDCQ.add("");
							VCONTRACT_PRICE.add("");
						}
						else
						{
							VDCQ.add(""+VTEMP_DCQ.elementAt(i));
							tot_dcq+=Double.parseDouble(""+VTEMP_DCQ.elementAt(i));
							VCONTRACT_PRICE.add(nf2.format(Double.parseDouble(cont_price)));
						}
					}
					else if(i==0)
					{
						sr=sr + 1;
						VSR.add(sr);
						VGAS_DT.add(gas_dt);
						VCONT_MMYYYY.add(gas_dt.substring(3,gas_dt.length()));
						VDCQ.add(Double.parseDouble(var_dcq));
						VACTUAL_QTY.add(nf.format(actual_qty));
						VCONTRACT_PRICE.add(nf2.format(Double.parseDouble(cont_price)));
						VTOTAL_CHARGE.add(nf2.format(weighted_average));
						tot_dcq+=Double.parseDouble(var_dcq);
						tot_actual_qty+=temp_actual_qty;
					}
					else
					{
						VSR.add("");
						VGAS_DT.add("");
						VCONT_MMYYYY.add("");
						VDCQ.add("");
						VACTUAL_QTY.add("");
						VCONTRACT_PRICE.add("");
						VTOTAL_CHARGE.add("");
					}
					
					VGAS_DT_MULTI.add(gas_dt);
					VSEQ_NO.add(i);
					VCONT_MMYYYY_EOD.add(gas_dt.substring(3,gas_dt.length()));
					
					var_dcq=""+VTEMP_DCQ.elementAt(i);
					actual_qty=Double.parseDouble(""+VTEMP_ALLOC_QTY.elementAt(i));
					
					VPRICE_TYPE.add(VTEMP_CURVE.elementAt(i));
					VFIN_CURVE_NM_DTL.add(VTEMP_CURVE_NM.elementAt(i));
					VPHYS_CURVE_NM_DTL.add(phys_curve_nm); //FOR EOD PROCESS INCIDENT#2410042
					VSLOPE.add(slope);
					VCONST.add(constant);
					
					double settle_price=0;
					String fin_mmyyyy=""+VTEMP_FIN_MMYYYY.elementAt(i);
					String fin_cont_month=""+VTEMP_FIN_CONT_MONTH.elementAt(i);
					String settle_start_dt=""+VTEMP_SETTLE_START_DT.elementAt(i);
					String settle_end_dt=""+VTEMP_SETTLE_END_DT.elementAt(i);
					String settle_dt="";
					int phy_count_ru=0;
					int fin_count_ru=0;
					double eff_rate=0;
					
					String phy_expo_ori="";
					String fin_expo_ori="";
					
					phy_count_ru=utilDate.getDays(gas_dt, report_dt);
					String phy_ru="";
					String phy_expo_r="";
					String phy_expo_u="";
					
					String fin_ru="";
					String fin_expo_r="";
					String fin_expo_u=nf.format(0);
					
					double fin_fwd_price=0;
					
					VFIN_MMYYYY.add(fin_mmyyyy);
					VSETTLE_START_DT.add(settle_start_dt);
					VSETTLE_END_DT.add(settle_end_dt);
					
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
						fin_count_ru=utilDate.getDays(settle_end_dt, report_dt);
						
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
							/*
							fin_expo_ori=nf.format(Double.parseDouble(var_dcq) * -1 * Double.parseDouble(slope));
							fin_expo_ori_info+="DCQ for ("+fin_ru+") * (-1) * Slope \n = "+fin_expo_ori+" * (-1) * "+slope+"\n = "+fin_expo_ori;
							*/
							int isSettlmentPeriodActive=utilDate.isDateWithinPeriod(settle_start_dt,settle_end_dt,report_dt);
							
							if(isSettlmentPeriodActive==1)
							{
								String nextdt=utilDate.getDate(report_dt, "1"); 
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
							/*
							if(actual_qty!=0)
							{
								fin_expo_ori=nf.format(actual_qty * -1 * Double.parseDouble(slope));
							}
							else
							{
								fin_expo_ori=nf.format(0);
							}
							fin_expo_ori_info+="Allocated MMBTU for ("+fin_ru+") * (-1) * Slope \n = "+fin_expo_ori+" * (-1) * "+slope+"\n = "+fin_expo_ori;
							*/
							
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
						
						if(!VTEMP_EFF_PRICE.elementAt(i).equals("")) //THIS IS SPECIAL FOR MULTI LEG
						{
							eff_rate=Double.parseDouble(""+VTEMP_EFF_PRICE.elementAt(i));
						}
						else
						{
							//eff_rate=(settle_price * Double.parseDouble(slope)) + Double.parseDouble(constant);
							eff_rate=((((count_u * fin_fwd_price) + (count_r * settle_price))/(count_r+count_u)) * Double.parseDouble(slope)) + Double.parseDouble(constant);
							if(Double.isNaN(eff_rate))
							{
								eff_rate=0;
							}
							eff_rate+=weighted_average;
						}
					}
					else
					{
						
						
						if(contract_type.equals("Z")) //MAKING ORI FIN EXPO AS ZERO(0) FRO STORAGE AS DISCUSSED WITH RS ON 20240919
						{
							fin_ru="";
							fin_expo_ori=nf.format(0);
						}
						else
						{
							fin_ru="R";
							if(Double.parseDouble(phy_expo_ori)!=0)
							{
								fin_expo_ori=nf.format(Double.parseDouble(phy_expo_ori) * -1);
							}
							else
							{
								fin_expo_ori=nf.format(0);
							}
						}
						
						eff_rate=Double.parseDouble(variable_rate);
					}
					
					if(settle_price>0)
					{
						VSETTLE_PRICE.add(nf2.format(settle_price));
					}
					else
					{
						VSETTLE_PRICE.add("");
					}
					
					phy_expo_r=nf.format(Double.parseDouble(phy_expo_ori)-Double.parseDouble(phy_expo_u));
					fin_expo_r=nf.format(Double.parseDouble(fin_expo_ori)-Double.parseDouble(fin_expo_u));
					
					VPHY_EXPO_ORIGINAL.add(phy_expo_ori);
					VFIN_EXPO_ORIGINAL.add(fin_expo_ori);
					
					VPHY_REALIZED_UNREALIZED.add(phy_ru);
					VPHY_EXPO_REALIZED.add(phy_expo_r);
					VPHY_EXPO_UNREALIZED.add(phy_expo_u);
					
					VFIN_REALIZED_UNREALIZED.add(fin_ru);
					VFIN_EXPO_REALIZED.add(fin_expo_r);
					VFIN_EXPO_UNREALIZED.add(fin_expo_u);
					
					tot_phy_expo_realized+=Double.parseDouble(phy_expo_r);
					tot_phy_expo_unrealized+=Double.parseDouble(phy_expo_u);
					
					tot_fin_expo_realized+=Double.parseDouble(fin_expo_r);
					tot_fin_expo_unrealized+=Double.parseDouble(fin_expo_u);
					
					tot_phy_expo_ori+=Double.parseDouble(phy_expo_ori);
					tot_fin_expo_ori+=Double.parseDouble(fin_expo_ori);
					
					double phy_fwd_price=getForwardPrice(phys_curve_nm, report_dt, cont_month, "Forward", "Physical");
					
					if(eff_rate!=0)
					{
						VEFF_RATE.add(nf2.format(eff_rate));
					}
					else
					{
						VEFF_RATE.add("");
					}
					
					VPHY_FORWARD_PRICE.add(nf2.format(phy_fwd_price));
					VFIN_FORWARD_PRICE.add(nf2.format(fin_fwd_price));
					
					double fin_realized_leg=0;
					if(fin_ru.equals("U"))
					{
						fin_realized_leg=Double.parseDouble(fin_expo_r) * ((settle_price +(Double.parseDouble(constant)/Double.parseDouble(slope)))+(weighted_average/Double.parseDouble(slope)));
					}
					else
					{
						/*if(phy_count_ru<=1)
						{
							fin_realized_leg=eff_rate * Double.parseDouble(fin_expo_r);
							fin_realized_leg_info = "= Fin. Realized Expo. * Eff. Contract Price \n = "+fin_expo_r+" MMBTU * "+nf2.format(eff_rate)+"\n = "+nf.format(fin_realized_leg);
						}
						else if(fin_ru.equals("R") && price_type.equals("M"))*/
						if(curve_logic.equals("MULTI_LEG") && !VTEMP_FIN_RU.contains("U") && !VTEMP_PHY_RU.contains("U"))
						{
							fin_realized_leg=Double.parseDouble(fin_expo_r) * ((Double.parseDouble(""+VTEMP_AVG_SETTLE_PRICE.elementAt(i)) +(Double.parseDouble(constant)/Double.parseDouble(slope)))+(weighted_average/Double.parseDouble(slope)));
						}
						else if(price_type.equals("M"))	
						{
							fin_realized_leg=Double.parseDouble(fin_expo_r) * ((settle_price +(Double.parseDouble(constant)/Double.parseDouble(slope)))+(weighted_average/Double.parseDouble(slope)));
						}
						else
						{
							fin_realized_leg=eff_rate * Double.parseDouble(fin_expo_r);
						}
					}
					tot_fin_realized_leg+=fin_realized_leg;
					
					double phy_unrealized_leg=0;
					if(!phy_expo_u.equals(""))
					{
						phy_unrealized_leg=Double.parseDouble(phy_expo_u) * (phy_fwd_price+weighted_average);
					}
					tot_phy_unrealized_leg+=phy_unrealized_leg;
					
					double fin_unrealized_leg=0;
					if(!fin_expo_u.equals(""))
					{
						fin_unrealized_leg=Double.parseDouble(fin_expo_u) * ((fin_fwd_price + (Double.parseDouble(constant)/Double.parseDouble(slope)))+(weighted_average/Double.parseDouble(slope)));
					}
					tot_fin_unrealized_leg+=fin_unrealized_leg;
					
					double total_amt= phy_unrealized_leg+fin_unrealized_leg+fin_realized_leg;
					tot_total+=total_amt;
					
					VPHY_UNREALIZED_LEG.add(nf.format(phy_unrealized_leg));
					VFIN_UNREALIZED_LEG.add(nf.format(fin_unrealized_leg));
					VFIN_REALIZED_LEG.add(nf.format(fin_realized_leg));
					
					VTOTAL.add(nf.format(total_amt));
				}
			}
			rset.close();
			stmt.close();
			
			total=nf.format(tot_total);
			fin_realized_leg=nf.format(tot_fin_realized_leg);
			phy_unrealized_leg=nf.format(tot_phy_unrealized_leg);
			fin_unrealized_leg=nf.format(tot_fin_unrealized_leg);
			fin_expo_realized=nf.format(tot_fin_expo_realized);
			fin_expo_unrealized=nf.format(tot_fin_expo_unrealized);
			
			phy_expo_realized=nf.format(tot_phy_expo_realized);
			phy_expo_unrealized=nf.format(tot_phy_expo_unrealized);
			
			phy_expo_ori=nf.format(tot_phy_expo_ori);
			fin_expo_ori=nf.format(tot_fin_expo_ori);
			
			total_dcq=nf.format(tot_dcq);
			total_actual_qty=nf.format(tot_actual_qty);
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
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
					+ "WHERE CURVE_NM=? AND STATUS=? "
					+ "AND HOLIDAY_DT>=TO_DATE(?,'DD/MM/YYYY') AND HOLIDAY_DT<=TO_DATE(?,'DD/MM/YYYY'))";
			stmt_temp = conn.prepareStatement(queryString_temp);
			stmt_temp.setString(1, date2);
			stmt_temp.setString(2, date1);
			stmt_temp.setString(3, date1);
			stmt_temp.setString(4, date1);
			stmt_temp.setString(5, curve_nm);
			stmt_temp.setString(6, "Y");
			stmt_temp.setString(7, date1);
			stmt_temp.setString(8, date1);
			rset_temp = stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				days=rset_temp.getInt(1);
			}
			
			rset_temp.close();
			stmt_temp.close();
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
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
			stmt_temp = conn.prepareStatement(queryString_temp);
			stmt_temp.setString(1, curve_nm);
			stmt_temp.setString(2, cont_month);
			rset_temp = stmt_temp.executeQuery();
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
				settle_start_dt=utilDate.getDate(settle_dt, avgDay);
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
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return temp;
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
			stmt_temp = conn.prepareStatement(queryString_temp);
			stmt_temp.setString(1, cont_month);
			stmt_temp.setString(2, curve_nm);
			stmt_temp.setString(3, phys_fin);
			stmt_temp.setString(4, curve_type);
			stmt_temp.setString(5, report_dt);
			rset_temp = stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				price=rset_temp.getDouble(1);
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

	public Double getSettledPrice(String curve_nm, String date1, String date2)
	{
		String function_nm="getSettledPrice()";
		double price=0;
		try
		{
			String queryString_temp="SELECT AVG(SETTLE_PRICE) "
					+ "FROM FMS_SPOT_PRICE_DTL "
					+ "WHERE ACTUAL_CURVE=? "
					+ "AND SETTLE_PRICE IS NOT NULL AND SETTLE_PRICE > 0 AND CURVE_TYPE='Spot' "
					+ "AND TO_CHAR(CURVE_DT,'DD/MM/YYYY') IN (SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY')+RNUM,'DD/MM/YYYY') "
					+ "FROM (SELECT ROWNUM-1 RNUM FROM ALL_OBJECTS WHERE  ROWNUM <= TO_DATE(?,'DD/MM/YYYY')-TO_DATE(?,'DD/MM/YYYY')+1) "
					+ "WHERE TO_CHAR(TO_DATE(?,'DD/MM/YYYY')+RNUM,'DY') NOT IN ('SAT','SUN')) "
					+ "AND TO_CHAR(CURVE_DT,'DD/MM/YYYY') NOT IN (SELECT TO_CHAR(HOLIDAY_DT,'DD/MM/YYYY') FROM FMS_CURVE_HOLIDAY_CALND "
					+ "WHERE CURVE_NM=? AND STATUS=? "
					+ "AND HOLIDAY_DT>=TO_DATE(?,'DD/MM/YYYY') AND HOLIDAY_DT<=TO_DATE(?,'DD/MM/YYYY'))";
			stmt_temp = conn.prepareStatement(queryString_temp);
			stmt_temp.setString(1, curve_nm);
			stmt_temp.setString(2, date1);
			stmt_temp.setString(3, date2);
			stmt_temp.setString(4, date1);
			stmt_temp.setString(5, date1);
			stmt_temp.setString(6, curve_nm);
			stmt_temp.setString(7, "Y");
			stmt_temp.setString(8, date1);
			stmt_temp.setString(9, date2);
			rset_temp = stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				price=rset_temp.getDouble(1);
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
	
	public void freezMrExpoEodData(int i)
	{
		String function_nm="freezMrExpoEodData()";
		try
		{
			String rate_unit="1";
			String mappingId=""+VMAPPING_ID.elementAt(i);
			
			String queryString="INSERT INTO FMS_MR_EXPO_EOD_MST(COMPANY_CD,REPORT_DT,BUY_SELL,COUNTERPARTY_CD,"
					+ "COUNTERPARTY_NM,CONTRACT_TYPE,MAPPING_ID,"
					+ "CONT_REF,CONT_SIGN_DT,CONT_START_DT,CONT_END_DT,"
					+ "CONT_ENT_BY,CONT_ENT_DT,CONT_APRV_BY,CONT_APRV_DT,CONT_STATUS,PRICE_TYPE,"
					+ "CONT_PRICE,RATE_UNIT,PHYS_CURVE,FIN_CURVE,TOT_DCQ,TOT_ALLOC_QTY,TOT_ORI_EXPO_PHY,TOT_ORI_EXPO_FIN,TOT_UNR_EXPO_PHY,TOT_UNR_EXPO_FIN,"
					+ "TOT_R_EXPO_PHY,TOT_R_EXPO_FIN,TOT_UNR_PHY_LEG,TOT_UNR_FIN_LEG,TOT_R_FIN_LEG,TOT_MTM_TOTAL,PHYS_FWD_PRICE_DT,FIN_FWD_PRICE_DT,"
					+ "DEAL_NUM,ENT_BY,ENT_DT) "
					+ "VALUES(?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
					+ "?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
					+ "?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
					+ "?,?,?,?,?,?,?,?,?,?,"
					+ "?,?,?,?,?,?,?,?,"
					+ "?,?,SYSDATE)";
			//System.out.println("MST-->"+queryString);
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, report_dt);
			stmt.setString(3, account);
			stmt.setString(4, counterparty_cd);
			stmt.setString(5, ""+VCOUNTERPARTY_NM.elementAt(i));
			stmt.setString(6, contract_type);
			stmt.setString(7, mappingId);
			stmt.setString(8, ""+VCONT_REF_NO.elementAt(i));
			stmt.setString(9, ""+VSIGNING_DT.elementAt(i));
			stmt.setString(10, ""+VSTART_DT.elementAt(i));
			stmt.setString(11, ""+VEND_DT.elementAt(i));
			stmt.setString(12, entered_cd);
			stmt.setString(13, entered_dt);
			stmt.setString(14, approved_cd);
			stmt.setString(15, approved_dt);
			stmt.setString(16, ""+VCONT_STATUS_FLG.elementAt(i));
			stmt.setString(17, ""+VPRICE_TYPE_MST.elementAt(i));
			stmt.setString(18, ""+VRATE.elementAt(i));
			stmt.setString(19, rate_unit);
			stmt.setString(20, ""+VPHYS_CURVE_NM.elementAt(i));
			stmt.setString(21, ""+VFIN_CURVE_NM.elementAt(i));
			stmt.setString(22, total_dcq);
			stmt.setString(23, total_actual_qty);
			stmt.setString(24, phy_expo_ori);
			stmt.setString(25, fin_expo_ori);
			stmt.setString(26, phy_expo_unrealized);
			stmt.setString(27, fin_expo_unrealized);
			stmt.setString(28, phy_expo_realized);
			stmt.setString(29, fin_expo_realized);
			stmt.setString(30, phy_unrealized_leg);
			stmt.setString(31, fin_unrealized_leg);
			stmt.setString(32, fin_realized_leg);
			stmt.setString(33, total);
			stmt.setString(34, "");
			stmt.setString(35, "");
			stmt.setString(36, ""+VDEAL_NUM_EOD.elementAt(i));
			stmt.setString(37, emp_cd);
			stmt.executeUpdate();
			
			stmt.close();
			
			for(int j=0;j<VGAS_DT_MULTI.size();j++)
			{
				String cont_mth="01/"+VCONT_MMYYYY_EOD.elementAt(j);
				if(VCONT_MMYYYY_EOD.elementAt(j).equals(""))
				{
					cont_mth="";
				}
				String fin_mth="01/"+VFIN_MMYYYY.elementAt(j);
				if(VFIN_MMYYYY.elementAt(j).equals(""))
				{
					fin_mth="";
				}
				
				String queryString1="INSERT INTO FMS_MR_EXPO_EOD_DTL(COMPANY_CD,REPORT_DT,BUY_SELL,COUNTERPARTY_CD,"
						+ "CONTRACT_TYPE, MAPPING_ID,GAS_DT, CONT_MTH,DCQ,ALLOC_QTY,SEQ_NO,PRICE_TYPE,FIN_CURVE_NM,CONT_PRICE,RATE_UNIT,"
						+ "SPOT_MTH,SPOT_START_DT,SPOT_END_DT,SETTLE_PRICE,RU_PHY_FLAG,RU_FIN_FLAG,FWD_PRICE_PHY,FWD_PRICE_FIN,"
						+ "SLOPE, CONST, EFF_RATE_USD,ORI_EXPO_PHY, ORI_EXPO_FIN,UNR_EXPO_PHY, UNR_EXPO_FIN,R_EXPO_PHY, R_EXPO_FIN,"
						+ "UNR_PHY_LEG, UNR_FIN_LEG,R_FIN_LEG, MTM_TOTAL,ENT_BY,ENT_DT,PHY_CURVE_NM,WA_RATE) "
						+ "VALUES(?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
						+ "?,?,TO_DATE(?,'DD/MM/YYYY'),"
						+ "TO_DATE(?,'DD/MM/YYYY'),?,?,"
						+ "?,?,?,"
						+ "?,?,"
						+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
						+ "?,"
						+ "?,?,"
						+ "?,?,"
						+ "?,?,?,"
						+ "?,?,"
						+ "?,?,"
						+ "?,?,"
						+ "?,?,"
						+ "?,?,?,SYSDATE,?,?)";
				//System.out.println("DTL-->"+queryString);
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, report_dt);
				stmt1.setString(3, account);
				stmt1.setString(4, counterparty_cd);
				stmt1.setString(5, contract_type);
				stmt1.setString(6, mappingId);
				stmt1.setString(7, ""+VGAS_DT_MULTI.elementAt(j));
				stmt1.setString(8, cont_mth);
				stmt1.setString(9, ""+VDCQ.elementAt(j));
				stmt1.setString(10, ""+VACTUAL_QTY.elementAt(j));
				stmt1.setString(11, ""+VSEQ_NO.elementAt(j));
				stmt1.setString(12, ""+VPRICE_TYPE.elementAt(j));
				stmt1.setString(13, ""+VFIN_CURVE_NM_DTL.elementAt(j));
				stmt1.setString(14, ""+VCONTRACT_PRICE.elementAt(j));
				stmt1.setString(15, rate_unit);
				stmt1.setString(16, fin_mth);
				stmt1.setString(17, ""+VSETTLE_START_DT.elementAt(j));
				stmt1.setString(18, ""+VSETTLE_END_DT.elementAt(j));
				stmt1.setString(19, ""+VSETTLE_PRICE.elementAt(j));
				stmt1.setString(20, ""+VPHY_REALIZED_UNREALIZED.elementAt(j));
				stmt1.setString(21, ""+VFIN_REALIZED_UNREALIZED.elementAt(j));
				stmt1.setString(22, ""+VPHY_FORWARD_PRICE.elementAt(j));
				stmt1.setString(23, ""+VFIN_FORWARD_PRICE.elementAt(j));
				stmt1.setString(24, ""+VSLOPE.elementAt(j));
				stmt1.setString(25, ""+VCONST.elementAt(j));
				stmt1.setString(26, ""+VEFF_RATE.elementAt(j));
				stmt1.setString(27, ""+VPHY_EXPO_ORIGINAL.elementAt(j));
				stmt1.setString(28, ""+VFIN_EXPO_ORIGINAL.elementAt(j));
				stmt1.setString(29, ""+VPHY_EXPO_UNREALIZED.elementAt(j));
				stmt1.setString(30, ""+VFIN_EXPO_UNREALIZED.elementAt(j));
				stmt1.setString(31, ""+VPHY_EXPO_REALIZED.elementAt(j));
				stmt1.setString(32, ""+VFIN_EXPO_REALIZED.elementAt(j));
				stmt1.setString(33, ""+VPHY_UNREALIZED_LEG.elementAt(j));
				stmt1.setString(34, ""+VFIN_UNREALIZED_LEG.elementAt(j));
				stmt1.setString(35, ""+VFIN_REALIZED_LEG.elementAt(j));
				stmt1.setString(36, ""+VTOTAL.elementAt(j));
				stmt1.setString(37, emp_cd);
				stmt1.setString(38, ""+VPHYS_CURVE_NM_DTL.elementAt(j));
				stmt1.setString(39, ""+VTOTAL_CHARGE.elementAt(j));
				
				stmt1.executeUpdate();
				stmt1.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void deleteFreezMrExpoEodData()
	{
		String function_nm="deleteFreezMrExpoEodData()";
		try
		{
			int count= 0;
			String queryString="SELECT COUNT(*) "
					+ "FROM FMS_MR_EXPO_EOD_MST "
					+ "WHERE REPORT_DT=TO_DATE(?,'DD/MM/YYYY')";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, report_dt);
			rset = stmt.executeQuery();
			if(rset.next())
			{
				count=rset.getInt(1);
			}
			
			rset.close();
			stmt.close();
			
			String queryString1="DELETE FROM FMS_MR_EXPO_EOD_MST "
					+ "WHERE REPORT_DT=TO_DATE(?,'DD/MM/YYYY')";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, report_dt);
			stmt1.executeQuery();
			
			stmt1.close();
			
			count= 0;
			String queryString2="SELECT COUNT(*) "
					+ "FROM FMS_MR_EXPO_EOD_DTL "
					+ "WHERE REPORT_DT=TO_DATE(?,'DD/MM/YYYY')";
			stmt2 = conn.prepareStatement(queryString2);
			stmt2.setString(1, report_dt);
			rset2 = stmt2.executeQuery();
			if(rset2.next())
			{
				count=rset2.getInt(1);
			}
			rset2.close();
			stmt2.close();
			
			String queryString3="DELETE FROM FMS_MR_EXPO_EOD_DTL "
					+ "WHERE REPORT_DT=TO_DATE(?,'DD/MM/YYYY')";
			stmt3 = conn.prepareStatement(queryString3);
			stmt3.setString(1, report_dt);
			stmt3.executeQuery();

			stmt3.close();
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void sendEODConfirmationMail()
	{
		String function_nm="sendEODConfirmationMail()";
		int Count=0;
		String fileName[] = {};
		String MailBody="";
		try
		{
			String queryString = "SELECT COUNT(*) "
					+ "FROM FMS_MR_EXPO_EOD_DTL "
					+ "WHERE REPORT_DT=TO_DATE(?,'DD/MM/YYYY')";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, report_dt);
			rset = stmt.executeQuery();
			if(rset.next())
			{
				Count = rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			if(Count > 0)
			{
				MailBody="<html>"
						+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>";				
				MailBody +=CommonVariable.app_name+" Market Risk Exposure EoD Process for "+report_dt+" completed at "+utilDate.getSysdateWithTime24hr()+".</font>";
			}
			else
			{
				MailBody="<html>"
						+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>";				
				MailBody +=CommonVariable.app_name+" Market Risk Exposure EoD Process for "+report_dt+" NOT completed at "+utilDate.getSysdateWithTime24hr()+".</font>";	
			}
			
			MailBody+=CommonVariable.mail_disclaimer;
			MailBody+="</html>";
			
			String subject=CommonVariable.app_name_sub+" "+context+": Market Risk Exposure EoD Process Status for "+report_dt;
			String to_list=utilBean.getToMailReceipentList(conn, "0", "Exposure EoD Process", "Risk Mgmt", "Daily", "Auto");
			String cc_list=utilBean.getCcMailReceipentList(conn, "0", "Exposure EoD Process", "Risk Mgmt", "Daily", "Auto");
			
			if(!to_list.equals(""))
			{
				mail.sendMail(conn, to_list, subject, MailBody, "", cc_list, "");
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void generateQPcsvFile()
	{
		String function_nm="generateQPcsvFile()";
		BufferedWriter bw = null; 
		PrintWriter pw = null;
		FileWriter fw = null;
		
		try
		{
			String split_date[] = report_dt.split("/");
			String date = split_date[2]+split_date[1]+split_date[0];
			QP_file_nm="Exposure_Report_"+date+".csv";
			
			if(!QP_file_path.equals(""))
			{
				if(new File(QP_file_path).exists())
				{	
					fw = new FileWriter(QP_file_path+""+QP_file_nm); 
					bw = new BufferedWriter(fw); 
			    	pw = new PrintWriter(bw);
			    	
			    	pw.println("COB DT,LEGAL ENTITY,DEAL NUM,COUNTERPARTY,BUY/SELL,PRICE TYPE,UNIT,CONTRACT MONTH,CURVE NAME,EXPOSURE,FINANCIAL/PHYSICAL,REALISED/UNREALISED,FORWARD PRICE");
			    	
			    	String queryString = "SELECT TO_CHAR(COB_DT,'DD/MM/YYYY'), LEGAL_ENTITY,DEAL_NUM,COUNTERPARTY,BUY_SELL,PRICE_TYPE,UNIT,CONTRACT_MONTH,"
							+ "CURVE_NAME,EXPOSURE,FINANCIAL_PHYSICAL,REALISED_UNREALISED,FORWARD_PRICE "
							+ "FROM QP_EXPOSURE ";
					stmt = conn.prepareStatement(queryString);
					rset = stmt.executeQuery();
					while(rset.next()) 
					{
						String cob_dt=rset.getString(1)==null?"":rset.getString(1);
						String legal_entity=rset.getString(2)==null?"":rset.getString(2);
						String dealNum=rset.getString(3)==null?"":rset.getString(3);
						String countpty_nm=rset.getString(4)==null?"":rset.getString(4);
						String buy_sell=rset.getString(5)==null?"":rset.getString(5);
						String priceType=rset.getString(6)==null?"":rset.getString(6);
						String unit = rset.getString(7)==null?"":rset.getString(7);
						String contMth=rset.getString(8)==null?"":rset.getString(8);
						String curveNm=rset.getString(9)==null?"":rset.getString(9);
						String exposure=rset.getString(10)==null?"":rset.getString(10);
						String phys_fin=rset.getString(11)==null?"":rset.getString(11);
						String r_ur=rset.getString(12)==null?"":rset.getString(12);
						String fwd_price=rset.getString(13)==null?"":rset.getString(13);
						
						pw.println(""+cob_dt+","+legal_entity+","+dealNum+","+countpty_nm+","+buy_sell+","+priceType+","+unit+",  "+contMth+","+curveNm+","
								+ ""+exposure+","+phys_fin+","+r_ur+","+fwd_price);
					}
					rset.close();
					stmt.close();
					
					pw.flush();
			    	pw.close();
			    	bw.close();
			    	fw.close();
				}
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally 
		{
			if(pw!=null) {try{pw.close();}catch(Exception e) {new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(bw!=null) {try{bw.close();}catch(Exception e) {new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(fw!=null) {try{fw.close();}catch(Exception e) {new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}			
		}
	}
	
	public void sendQPFile()
	{
		String function_nm="sendQPFile()";
		try
		{
			String split_date[] = report_dt.split("/");
			String date = split_date[2]+split_date[1]+split_date[0];
			
			String QP_EXPO = QP_file_path+""+QP_file_nm; 
			
			String fileName[]= new String[1];
			int i=-1;
			String MailBody="";
			File file1 = new File(QP_EXPO);
			if(file1.exists())
			{
				i=i+1;
				fileName[i] = QP_EXPO;
			}
			
			if(i != -1)
			{
				MailBody="<html>"
						+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>";				
				MailBody +="Please find "+CommonVariable.app_name+" Market Risk QP Exposure Report dated "+report_dt+" attached.";
				MailBody+=CommonVariable.mail_disclaimer;
				MailBody+="</html>";
				

				String subject=CommonVariable.app_name_sub+" "+context+": Market Risk QP Exposure Report dated "+report_dt;
				String to_list=utilBean.getToMailReceipentList(conn, "0", "QP Exposure Report", "Risk Mgmt", "Daily", "Auto");
				String cc_list=utilBean.getCcMailReceipentList(conn, "0", "QP Exposure Report", "Risk Mgmt", "Daily", "Auto");
				
				if(!to_list.equals(""))
				{
					mail.sendMail(conn, to_list, subject, MailBody, QP_EXPO, cc_list, "");
				}
			}
			else
			{
				System.out.println("files are not available in the following Path :");
				System.out.println(QP_EXPO);
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void doClear()
	{
		String function_nm="doClear()";
		try
		{
			VSR.clear();
			VGAS_DT.clear();
			VCONT_MMYYYY.clear();
			VDCQ.clear();
			VACTUAL_QTY.clear();
			VCONTRACT_PRICE.clear();
			VROW_COLOR.clear();
			VPRICE_TYPE.clear();
			VSLOPE.clear();
			VCONST.clear();
			VFIN_MMYYYY.clear();
			VSETTLE_START_DT.clear();
			VSETTLE_END_DT.clear();
			VSETTLE_PRICE.clear();
			VPHY_EXPO_ORIGINAL.clear();
			VFIN_EXPO_ORIGINAL.clear();
			VPHY_REALIZED_UNREALIZED.clear();
			VPHY_EXPO_REALIZED.clear();
			VPHY_EXPO_UNREALIZED.clear();
			VFIN_REALIZED_UNREALIZED.clear();
			VFIN_EXPO_REALIZED.clear();
			VFIN_EXPO_UNREALIZED.clear();
			VEFF_RATE.clear();
			VPHY_FORWARD_PRICE.clear();
			VFIN_FORWARD_PRICE.clear();
			VPHY_UNREALIZED_LEG.clear();
			VFIN_UNREALIZED_LEG.clear();
			VFIN_REALIZED_LEG.clear();
			VTOTAL.clear();
			VGAS_DT_MULTI.clear();
			VSEQ_NO.clear();
			VFIN_CURVE_NM_DTL.clear();
			VCONT_MMYYYY_EOD.clear();
			VPHYS_CURVE_NM_DTL.clear(); //INCIDENT#2410042
			VTOTAL_CHARGE.clear();
			
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
    
    String report_dt="";
    String counterparty_cd = "";
	String cont_no = "";
	String cont_rev_no = "";
	String agmt_no = "";
	String agmt_rev_no = "";
	String contract_type = "";
	String account = "";
	String cargo_no = "";
    
    Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VMST_COUNTERPARTY_CD = new Vector();
	Vector VMST_COUNTERPARTY_NM = new Vector();
	Vector VMST_COUNTERPARTY_ABBR = new Vector();
	Vector VCONT_NO = new Vector();
	Vector VCONT_REV_NO = new Vector();
	Vector VAGMT_NO = new Vector();
	Vector VAGMT_REV_NO = new Vector();
	Vector VCARGO_NO = new Vector();
	Vector VCONTRACT_TYPE = new Vector();
	Vector VSIGNING_DT = new Vector();
	Vector VSTART_DT = new Vector();
	Vector VEND_DT = new Vector();
	Vector VACCOUNT = new Vector();
	Vector VDISPLAY_DEAL_MAP = new Vector();
	Vector VRATE = new Vector();
	Vector VRATE_UNIT = new Vector();
	Vector VPRICE_TYPE = new Vector();
	Vector VPRICE_TYPE_MST = new Vector();
	Vector VCONT_REF_NO = new Vector();
	Vector VPHYS_CURVE_NM = new Vector();
	Vector VFIN_CURVE_NM = new Vector();
	Vector VCONT_STATUS = new Vector();
	Vector VCONT_STATUS_FLG = new Vector();
	Vector VGAS_DT = new Vector();
	Vector VDCQ = new Vector();
	Vector VACTUAL_QTY = new Vector();
	Vector VEFF_RATE = new Vector();
	Vector VEFF_RATE_INFO = new Vector();
	Vector VPHY_REALIZED_UNREALIZED = new Vector();
	Vector VFIN_REALIZED_UNREALIZED = new Vector();
	Vector VPHY_EXPO_REALIZED = new Vector();
	Vector VFIN_EXPO_REALIZED = new Vector();
	Vector VPHY_EXPO_UNREALIZED = new Vector();
	Vector VFIN_EXPO_UNREALIZED = new Vector();
	Vector VPHY_EXPO_REALIZED_INFO = new Vector();
	Vector VFIN_EXPO_REALIZED_INFO = new Vector();
	Vector VPHY_EXPO_UNREALIZED_INFO = new Vector();
	Vector VFIN_EXPO_UNREALIZED_INFO = new Vector();
	Vector VSETTLE_PRICE = new Vector();
	Vector VSLOPE = new Vector();
	Vector VCONST = new Vector();
	Vector VPHY_FORWARD_PRICE = new Vector();
	Vector VFIN_FORWARD_PRICE = new Vector();
	Vector VPHY_UNREALIZED_LEG = new Vector();
	Vector VFIN_UNREALIZED_LEG = new Vector();
	Vector VFIN_REALIZED_LEG = new Vector();
	Vector VPHY_UNREALIZED_LEG_INFO = new Vector();
	Vector VFIN_UNREALIZED_LEG_INFO = new Vector();
	Vector VFIN_REALIZED_LEG_INFO = new Vector();
	Vector VTOTAL = new Vector();
	Vector VCONT_MMYYYY = new Vector();
	Vector VPRICE_LINE_RATE = new Vector();
	Vector VFIN_MMYYYY = new Vector();
	Vector VSETTLE_START_DT = new Vector();
	Vector VSETTLE_END_DT = new Vector();
	Vector VCONTRACT_PRICE = new Vector();
	Vector VPHY_EXPO_ORIGINAL = new Vector();
	Vector VFIN_EXPO_ORIGINAL = new Vector();
	Vector VPHY_EXPO_ORIGINAL_INFO = new Vector();
	Vector VFIN_EXPO_ORIGINAL_INFO = new Vector();
	Vector VSR = new Vector();
	Vector VROW_COLOR = new Vector();
	Vector VMAPPING_ID = new Vector();
	Vector VGAS_DT_MULTI = new Vector();
	Vector VSEQ_NO = new Vector();
	Vector VFIN_CURVE_NM_DTL = new Vector();
	Vector VCONT_MMYYYY_EOD = new Vector();
	Vector VDEAL_NUM_EOD = new Vector();
	Vector VPHYS_CURVE_NM_DTL = new Vector(); //INCIDENT#2410042
	Vector VLEGAL_ENTITY_CD = new Vector();
	Vector VLEGAL_ENTITY = new Vector();
	
	Vector VSTORAGE_ROW_HEADING = new Vector();
	Vector VSTORAGE_MMSCM = new Vector();
	Vector VSTORAGE_MMBTU = new Vector();
	Vector VSTORAGE_LAST_DT = new Vector();
	
	Vector VCOMPANY_CD = new Vector();
	Vector VCOMPANY_ABBR = new Vector();
	Vector VCOMPANY_NAME = new Vector();
	Vector VTOTAL_CHARGE = new Vector();
	
	String counterparty_nm="";
	String display_deal_mapp="";
	String deal_ref_no="";
	String deal_dt="";
	String start_dt="";
	String end_dt="";
	String entered_cd="";
	String entered_by="";
	String entered_dt="";
	String approved_cd="";
	String approved_by="";
	String approved_dt="";
	String dcq="";
	String rate="";
	String rate_unit="";
	String total="";
	String fin_realized_leg="";
	String fin_unrealized_leg="";
	String phy_unrealized_leg="";
	String phy_expo_ori="";
	String fin_expo_ori="";
	String phy_expo_unrealized="";
	String fin_expo_unrealized="";
	String phy_expo_realized="";
	String fin_expo_realized="";
	String total_dcq="";
	String total_actual_qty="";
	String storage_collapse_info="";
	String derivative_curve_nm="";
}
