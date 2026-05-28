package com.etrm.fms.market_risk;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.etrm.fms.util.DB_AllocationUtil;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;

//Coded By          : Harsh Patel
//Code Reviewed by	:  
//CR Date			: 23/09/2023 
//Status	  		: Developing
public class DB_MR_ExposureReport_ap 
{
	String db_src_file_name="DB_MR_ExposureReport.java";
	Connection conn;
	PreparedStatement stmt;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt_temp;
	PreparedStatement stmt_temp1;
	ResultSet rset;
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset_temp;
	ResultSet rset_temp1;
	/*String queryString="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";
	String queryString_temp="";
	*/
	
	UtilBean utilBean = new UtilBean();
	DateUtil utilDate = new DateUtil();
	DB_AllocationUtil utilAlloc = new DB_AllocationUtil();
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf3 = new DecimalFormat("###########0.000");
	
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
	    			if(callFlag.equalsIgnoreCase("EXPOSURE_CONTRACT_LIST"))
	    			{
	    				getContractListForExposure();
	    				getStorageExposure("");
	    			}
	    			else if(callFlag.equalsIgnoreCase("EXPOSURE_CONTRACT_DTL"))
	    			{
	    				getContractDetail();
	    				ExposureCalculation();
	    			}
	    			else if(callFlag.equalsIgnoreCase("DELETE_EOD_DATA"))
	    			{
	    				deleteFreezMrExpoEodData();
	    			}
	    			else if(callFlag.equalsIgnoreCase("AUTO_EXPOSURE_EOD_PROCESS"))
	    			{
        				doClear();
        				getContractDetail();
        				ExposureCalculation();
        				System.out.println("Freezing Data into DB Table : "+index);
        				freezMrExpoEodData(index);
	    			}
	    			else if(callFlag.equalsIgnoreCase("GENERATE_QP_EXPO_CSV"))
	    			{
	    				generateQPcsvFile();
	    			}
	    			else if(callFlag.equalsIgnoreCase("EXPOSURE_EOD_CSV"))
	    			{
        				getEodFreezedData();
	    			}
	    			else if(callFlag.equalsIgnoreCase("FETCH_EOD_FREEZED_DATA"))
	    			{
	    				getExposureDetailFreezedData();	
	    			}
	    			else if(callFlag.equalsIgnoreCase("EXPOSURE_QP_RPT"))
	    			{
        				getQPDetails();
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
	    	if(rset_temp1 != null){try{rset_temp1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp != null){try{stmt_temp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp1 != null){try{stmt_temp1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}
	
	public void getQPDetails()
	{
		String function_nm="getQPDetails()";
		try
		{
			String queryString = "SELECT TO_CHAR(COB_DT,'DD/MM/YYYY'), LEGAL_ENTITY,DEAL_NUM,COUNTERPARTY,BUY_SELL,PRICE_TYPE,UNIT,CONTRACT_MONTH,"
					+ "CURVE_NAME,EXPOSURE,FINANCIAL_PHYSICAL,REALISED_UNREALISED,FORWARD_PRICE "
					+ "FROM QP_EXPOSURE ";
			stmt=conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			while(rset.next()) 
			{
				VCOB_DT.add(rset.getString(1)==null?"":rset.getString(1));
				VLEGAL_ENTITY.add(rset.getString(2)==null?"":rset.getString(2));
				VDEAL_NUM.add(rset.getString(3)==null?"":rset.getString(3));
				VCOUNTERPARTY.add(rset.getString(4)==null?"":rset.getString(4));
				VBUY_SELL.add(rset.getString(5)==null?"":rset.getString(5));
				VPRICE_TYPE.add(rset.getString(6)==null?"":rset.getString(6));
				VUNIT.add(rset.getString(7)==null?"":rset.getString(7));
				VCONTRACT_MONTH.add(rset.getString(8)==null?"":rset.getString(8));
				VCURVE_NAME.add(rset.getString(9)==null?"":rset.getString(9));
				VEXPOSURE.add(rset.getString(10)==null?"":rset.getString(10));
				VFINANCIAL_PHYSICAL.add(rset.getString(11)==null?"":rset.getString(11));
				VREALISED_UNREALISED.add(rset.getString(12)==null?"":rset.getString(12));
				VFORWARD_PRICE.add(rset.getString(13)==null?"":rset.getString(13));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getContractListForExposure()
	{
		String function_nm="getContractListForExposure()";
		try
		{
			String storageDt=utilDate.getDate(report_dt, "1");
			
			int count=0;
			String queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONT_REF_NO,TRADE_REF_NO,CONTRACT_TYPE,"
					+ "TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),AGMT_BASE,"
					+ "RATE,RATE_UNIT,CONT_STATUS,'Sell',START_DT,NULL,NULL,NULL "
					+ "FROM FMS_SUPPLY_CONT_MST A "
					+ "WHERE CONT_STATUS NOT IN ('C','X') " //'F' removed as per INC#2410044
					+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			if(!expo_type.equals(""))
			{
				queryString+="AND 'R'=? ";
			}
			queryString+="UNION ALL ";
			queryString+="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONT_REF_NO,TRADE_REF_NO,CONTRACT_TYPE,"
					+ "TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),AGMT_BASE,"
					+ "RATE,RATE_UNIT,CONT_STATUS,'Buy',START_DT,NULL,NULL,NULL "
					+ "FROM FMS_TRADER_CONT_MST A "
					+ "WHERE CONT_STATUS NOT IN ('C','X') " //'F' removed as per INC#2410044
					+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			if(!expo_type.equals(""))
			{
				queryString+="AND 'P'=? ";
			}
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
			if(!expo_type.equals(""))
			{
				queryString+="AND 'P'=? ";
			}
			queryString+="UNION ALL ";
			queryString+="SELECT COMPANY_CD,0,0,0,0,0,'STORAGE TANK',NULL,'Z',"
					+ "'"+storageDt+"','"+storageDt+"','"+storageDt+"',NULL,"
					+ "0,'2','Y','Buy',TO_DATE('"+storageDt+"','DD/MM/YYYY') START_DT,0,NULL,NULL "
					+ "FROM FMS_COMPANY_OWNER_MST A "
					+ "WHERE EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COMPANY_OWNER_MST B WHERE A.COMPANY_CD=B.COMPANY_CD) ";
			if(!expo_type.equals(""))
			{
				queryString+="AND 'P'=? ";
			}
			queryString+="UNION ALL ";
			queryString+="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONT_REF_NO,NULL,A.CONTRACT_TYPE,"
					+ "TO_CHAR(A.SIGNING_DT,'DD/MM/YYYY'),TO_CHAR(B.PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(B.PRICE_END_DT,'DD/MM/YYYY'),NULL,"
					+ "B.RATE,TO_CHAR(B.RATE_UNIT),A.CONT_STATUS,B.BUY_SELL,(B.PRICE_START_DT) START_DT,B.INSTRUMENT_NO,B.INSTRUMENT_TYPE,B.CURVE_NM "
					+ "FROM FMS_DERV_CONT_MST A, FMS_DERV_INSTRUMENT_MST B "
					+ "WHERE A.CONT_STATUS NOT IN ('C','X') AND A.AGMT_TYPE=? " //'F' removed as per INC#2410044
					+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_DERV_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.AGMT_TYPE=B.AGMT_TYPE) "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.AGMT_TYPE=B.AGMT_TYPE ";
			if(!expo_type.equals(""))
			{
				queryString+="AND CASE WHEN B.BUY_SELL='BUY' THEN 'P' ELSE CASE WHEN B.BUY_SELL='SELL' THEN 'R' ELSE '' END END = ? ";
			}
			queryString+="ORDER BY START_DT DESC ";
			
			String temp_queryString = queryString;
			stmt=conn.prepareStatement(temp_queryString);
			if(!expo_type.equals(""))
			{
				stmt.setString(++count, expo_type);
				stmt.setString(++count, expo_type);
			}
			stmt.setString(++count, "Y");
			if(!expo_type.equals(""))
			{
				stmt.setString(++count, expo_type);
				stmt.setString(++count, expo_type);
			}
			stmt.setString(++count, "U");
			if(!expo_type.equals(""))
			{
				stmt.setString(++count, expo_type);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String buy_sell=rset.getString(17)==null?"":rset.getString(17);
				buy_sell=buy_sell.substring(0,1)+buy_sell.substring(1, buy_sell.length()).toLowerCase();
				VACCOUNT.add(buy_sell);
				//VACCOUNT_EOD.add(buy_sell); //FOR EOD
				
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
				VMAPPING_ID.add(contPriceMapping);//FOR EOD_PROCESS
				
				VLEGAL_ENTITY_CD.add(companyCd);
				VLEGAL_ENTITY.add(companyAbbr);
				VCOUNTERPARTY_CD.add(countpty_cd);
				//VCOUNTERPARTY_CD_EOD.add(countpty_cd); //FOR EOD
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
					dealNo=dealNo+" <font style='background: #a6ff4d;'>[DLV]</font>";
				}
				
				VDISPLAY_DEAL_MAP.add(dealNo);
				VAGMT_NO.add(agmt);
				VAGMT_REV_NO.add(agmt_rev);
				VCONT_NO.add(cont);
				VCONT_REV_NO.add(cont_rev);
				VCONTRACT_TYPE.add(cont_type);
				VCARGO_NO.add(cargo_no);
				//VAGMT_NO_EOD.add(agmt); //FOR EOD
				//VAGMT_REV_NO_EOD.add(agmt_rev); //FOR EOD
				//VCONT_NO_EOD.add(cont); //FOR EOD
				//VCONT_REV_NO_EOD.add(cont_rev); //FOR EOD
				//VCONTRACT_TYPE_EOD.add(cont_type); //FOR EOD
				
				VCONT_REF_NO.add(cont_ref);
				VDIS_CONTRACT_TYPE.add(utilBean.getMR_ContractTypeName(cont_type));
				
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
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, companyCd);
					stmt1.setString(2, contPriceMapping);
					stmt1.setString(3, cont_type);
					stmt1.setString(4, dt);
					stmt1.setString(5, dt);
					rset1=stmt1.executeQuery();
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
								fin_curve_nm=curve_nm+" "+temp[0]+"("+temp[1]+","+temp[2]+","+temp[3]+")";
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
				
				VPRICE_TYPE.add(price_type_desc);
				VPRICE_TYPE_EOD.add(price_type_desc);//FRO EOD_PROCESS
				
				String cont_status_flg=rset.getString(16)==null?"":rset.getString(16);
				VCONT_STATUS_FLG.add(cont_status_flg);
				VCONT_STATUS.add(""+utilBean.ContStatusName(cont_status_flg));
				
				String isEodDone="";
				String queryString1="SELECT COUNT(*) "
						+ "FROM FMS_MR_EXPO_EOD_DTL "
						+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND BUY_SELL=? AND COUNTERPARTY_CD=? "
						+ "AND CONTRACT_TYPE=? AND MAPPING_ID=?";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, companyCd);
				stmt1.setString(2, report_dt);
				stmt1.setString(3, buy_sell);
				stmt1.setString(4, countpty_cd);
				stmt1.setString(5, cont_type);
				stmt1.setString(6, contPriceMapping);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					if(rset1.getInt(1)>0)
					{
						isEodDone="Y";
					}
				}
				rset1.close();
				stmt1.close();
				VIS_EOD_PROCESS_DONE.add(isEodDone);
			}
			rset.close();
			stmt.close();
		/*	
			//STORAGE FOR SEMTIPL
			if(expo_type.equals("P") || expo_type.equals(""))
			{
				String storageDate=utilDate.getDate(report_dt, "1");
				String ownerAbbr=utilBean.getCompanyAbbr("1");
				
				VACCOUNT.add("Buy");
				//VACCOUNT_EOD.add(buy_sell); //FOR EOD
				
				String contPriceMapping="0-0-0";
				VMAPPING_ID.add(contPriceMapping);//FOR EOD_PROCESS
				
				VLEGAL_ENTITY_CD.add("1");
				VLEGAL_ENTITY.add(""+utilBean.getCompanyAbbr("1"));
				VCOUNTERPARTY_CD.add("0");
				//VCOUNTERPARTY_CD_EOD.add(countpty_cd); //FOR EOD
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
				//VAGMT_NO_EOD.add(agmt); //FOR EOD
				//VAGMT_REV_NO_EOD.add(agmt_rev); //FOR EOD
				//VCONT_NO_EOD.add(cont); //FOR EOD
				//VCONT_REV_NO_EOD.add(cont_rev); //FOR EOD
				//VCONTRACT_TYPE_EOD.add(cont_type); //FOR EOD
				
				VCONT_REF_NO.add("STORAGE TANK");
				VDIS_CONTRACT_TYPE.add(utilBean.getContractTypeName("Z"));
				
				VSIGNING_DT.add(storageDate);
				VSTART_DT.add(storageDate);
				VEND_DT.add(storageDate);
				
				String rate = "0";
				String rate_unit = "2";
				VRATE.add(nf2.format(Double.parseDouble(rate)));
				//VRATE_UNIT.add(""+utilBean.getRateUnitNm(rate_unit));
				
				String phys_curve_nm="LNG_PHYS_INDIA";
				String fin_curve_nm="";
				VPRICE_LINE_RATE.add("");
				VPHYS_CURVE_NM.add(phys_curve_nm);
				VFIN_CURVE_NM.add(fin_curve_nm);
				VPRICE_TYPE.add("FIXED");
				VPRICE_TYPE_EOD.add("");//FRO EOD_PROCESS
				VCONT_STATUS_FLG.add("Y");
				VCONT_STATUS.add(""+ContStatusName("Y"));
				
				String isEodDone="";
				queryString1="SELECT COUNT(*) "
						+ "FROM FMS_MR_EXPO_EOD_DTL "
						+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND BUY_SELL=? AND COUNTERPARTY_CD=? "
						+ "AND CONTRACT_TYPE=? AND MAPPING_ID=?";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, report_dt);
				stmt1.setString(3, "Buy");
				stmt1.setString(4, "0");
				stmt1.setString(5, "Z");
				stmt1.setString(6, contPriceMapping);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					if(rset1.getInt(1)>0)
					{
						isEodDone="Y";
					}
				}
				rset1.close();
				stmt1.close();
				VIS_EOD_PROCESS_DONE.add(isEodDone);
			}
		*/	
			//STORAGE FOR SEIPL
		/*	if(expo_type.equals("P") || expo_type.equals(""))
			{
				String storageDate=utilDate.getDate(report_dt, "1");
				String ownerAbbr=utilBean.getCompanyAbbr("2");
				
				VACCOUNT.add("Buy");
				//VACCOUNT_EOD.add(buy_sell); //FOR EOD
				
				String contPriceMapping="0-0-0";
				VMAPPING_ID.add(contPriceMapping);//FOR EOD_PROCESS
				
				VLEGAL_ENTITY_CD.add("2");
				VLEGAL_ENTITY.add(""+utilBean.getCompanyAbbr("2"));
				VCOUNTERPARTY_CD.add("0");
				//VCOUNTERPARTY_CD_EOD.add(countpty_cd); //FOR EOD
				VCOUNTERPARTY_ABBR.add(ownerAbbr+"-STORAGE");
				VCOUNTERPARTY_NM.add(ownerAbbr+"-STORAGE");
				
				String dealNo=utilBean.NewDealMappingId("2", "0", "0", "0", "0", "0", "Z", "");
				VDEAL_NUM_EOD.add(dealNo);//FOR EOD
				
				VDISPLAY_DEAL_MAP.add(dealNo);
				VAGMT_NO.add("0");
				VAGMT_REV_NO.add("0");
				VCONT_NO.add("0");
				VCONT_REV_NO.add("0");
				VCONTRACT_TYPE.add("Z");
				VCARGO_NO.add("");
				//VAGMT_NO_EOD.add(agmt); //FOR EOD
				//VAGMT_REV_NO_EOD.add(agmt_rev); //FOR EOD
				//VCONT_NO_EOD.add(cont); //FOR EOD
				//VCONT_REV_NO_EOD.add(cont_rev); //FOR EOD
				//VCONTRACT_TYPE_EOD.add(cont_type); //FOR EOD
				
				VCONT_REF_NO.add("STORAGE TANK");
				VDIS_CONTRACT_TYPE.add(utilBean.getContractTypeName("Z"));
				
				VSIGNING_DT.add(storageDate);
				VSTART_DT.add(storageDate);
				VEND_DT.add(storageDate);
				
				String rate = "0";
				String rate_unit = "2";
				VRATE.add(nf2.format(Double.parseDouble(rate)));
				//VRATE_UNIT.add(""+utilBean.getRateUnitNm(rate_unit));
				
				String phys_curve_nm="LNG_PHYS_INDIA";
				String fin_curve_nm="";
				VPRICE_LINE_RATE.add("");
				VPHYS_CURVE_NM.add(phys_curve_nm);
				VFIN_CURVE_NM.add(fin_curve_nm);
				VPRICE_TYPE.add("FIXED");
				VPRICE_TYPE_EOD.add("");//FRO EOD_PROCESS
				VCONT_STATUS_FLG.add("Y");
				VCONT_STATUS.add(""+ContStatusName("Y"));
				
				String isEodDone="";
				queryString1="SELECT COUNT(*) "
						+ "FROM FMS_MR_EXPO_EOD_DTL "
						+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND BUY_SELL=? AND COUNTERPARTY_CD=? "
						+ "AND CONTRACT_TYPE=? AND MAPPING_ID=?";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, report_dt);
				stmt1.setString(3, "Buy");
				stmt1.setString(4, "0");
				stmt1.setString(5, "Z");
				stmt1.setString(6, contPriceMapping);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					if(rset1.getInt(1)>0)
					{
						isEodDone="Y";
					}
				}
				rset1.close();
				stmt1.close();
				VIS_EOD_PROCESS_DONE.add(isEodDone);
			} */
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getContractDetail()
	{
		String function_nm="getContractDetail()";
		try
		{
			if(contract_type.equals("Z"))
			{
				String storageDate=utilDate.getDate(report_dt, "1");
				String ownerAbbr=utilBean.getCompanyAbbr(conn,comp_cd);
				
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
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
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
			else
			{
				utilBean.getEffectiveCompanyOwnerList(conn);
				VCOMPANY_CD=utilBean.getCOUNTERPARTY_CD();
				VCOMPANY_ABBR=utilBean.getCOUNTERPARTY_ABBR();
				VCOMPANY_NAME=utilBean.getCOUNTERPARTY_NM();
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
				
				storage_collapse_info+=" ("+VCOMPANY_ABBR.elementAt(i)+" : "+nf.format(total_stock_mmbtu)+")";
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
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
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, start_dt);
			stmt.setString(2, end_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String gas_dt=rset.getString(1)==null?"":rset.getString(1);
				String cont_month="01/"+gas_dt.substring(3,gas_dt.length());
				
				String contPriceMapping=counterparty_cd+"-"+agmt_no+"-"+cont_no;
				if(contract_type.equals("N"))
				{
					contPriceMapping+="-"+cargo_no;
				}
				
				String var_color="blue";
				String var_dcq=utilBean.getContVariableTAQ(conn,comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, gas_dt);
				if(var_dcq.equals(""))
				{
					 var_color="black";
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
									+ "AND C.GAS_DT=B.GAS_DT AND B.CARGO_NO=C.CARGO_NO) "
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
							//actual_qty=Double.parseDouble(""+utilAlloc.getPurchaseAllocationQty(comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, "0",gas_dt));
						
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
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, contPriceMapping);
				stmt1.setString(3, contract_type);
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
							
							//JD
							//avgPriceInfo="Eff. Price (Month) = Settle Price * Slope + Const.";
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
							//avgPriceInfo+="\n\nEffective Price = Sum(Settle Price)/#Month \n= "+nf2.format(totalPrice)+"/"+count_withoutzero+" = "+nf2.format(avgPrice);
							if(settle_count_withoutzero>0) {
								avgSettlePrice=totalSettlePrice/settle_count_withoutzero; // JD -- Moved UP 
							}
							avgPrice = avgSettlePrice * Double.parseDouble(slope) + Double.parseDouble(constant); //JD - added
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
							VDCQ_COLOR.add("");
							VCONTRACT_PRICE.add("");
						}
						else
						{
							VDCQ.add(""+VTEMP_DCQ.elementAt(i));
							VDCQ_COLOR.add(var_color);
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
						VDCQ_COLOR.add(var_color);
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
						VDCQ_COLOR.add("");
						VACTUAL_QTY.add("");
						VCONTRACT_PRICE.add("");
						VTOTAL_CHARGE.add("");
					}
					
					if((sr%2)==0)
					{
						VROW_COLOR.add("#e6e6e6");
					}
					else
					{
						VROW_COLOR.add("");
					}
					
					VGAS_DT_MULTI.add(gas_dt); //FOR EOD PROCESS
					VCONT_MMYYYY_EOD.add(gas_dt.substring(3,gas_dt.length())); //FOR EOD PROCESS
					VSEQ_NO.add(i); //FOR EOD PROCESS
					VFIN_CURVE_NM_DTL.add(VTEMP_CURVE_NM.elementAt(i)); //FOR EOD PROCESS
					VPHYS_CURVE_NM_DTL.add(phys_curve_nm); //FOR EOD PROCESS INCIDENT#2410042
					/*if(price_type.equals("M"))
					{
						VPRICE_TYPE_DTL_EOD.add("Float"); //FOR EOD PROCESS
					}
					else
					{
						VPRICE_TYPE_DTL_EOD.add("Fixed"); //FOR EOD PROCESS
					}*/
					VPRICE_TYPE_DTL_EOD.add(""+VTEMP_CURVE_EOD.elementAt(i)); //FOR EOD PROCESS
					
					var_dcq=""+VTEMP_DCQ.elementAt(i);
					actual_qty=Double.parseDouble(""+VTEMP_ALLOC_QTY.elementAt(i));
					
					VPRICE_TYPE.add(VTEMP_CURVE.elementAt(i));
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
					String eff_rate_info="";
					
					String phy_expo_ori="";
					String fin_expo_ori="";
					String phy_expo_ori_info="Original Exposure (Physical Leg)\n = ";
					String fin_expo_ori_info="Original Exposure (Financial Leg)\n = ";
					
					phy_count_ru=utilDate.getDays(gas_dt, report_dt);
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
					
					VFIN_MMYYYY.add(fin_mmyyyy);
					VSETTLE_START_DT.add(settle_start_dt);
					VSETTLE_END_DT.add(settle_end_dt);
					
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
						fin_expo_ori_info+="Original Exposure (Physical Leg) * (-1) * Slope \n = "+phy_expo_ori+" * (-1) * "+slope+"\n = "+fin_expo_ori;
						
						
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
						
						fin_expo_ori_info+="Original Exposure (Physical Leg) * (-1) \n = "+fin_expo_ori;
						fin_expo_u_info+="ZERO \n = "+fin_expo_u;
						
						eff_rate=Double.parseDouble(variable_rate);
						eff_rate_info=" = "+nf2.format(eff_rate);
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
					
					phy_expo_r_info+="Origina Exposure (Physical Leg) - Unrealized Exposure (Physical Leg) "
							+ "\n = "+phy_expo_ori+" - "+phy_expo_u+"\n = "+phy_expo_r;
					fin_expo_r_info+="Origina Exposure (Financial Leg) - Unrealized Exposure (Financial Leg) "
							+ "\n = "+fin_expo_ori+" - "+fin_expo_u+"\n = "+fin_expo_r;
					
					VPHY_EXPO_ORIGINAL.add(phy_expo_ori);
					VFIN_EXPO_ORIGINAL.add(fin_expo_ori);
					VPHY_EXPO_ORIGINAL_INFO.add(phy_expo_ori_info);
					VFIN_EXPO_ORIGINAL_INFO.add(fin_expo_ori_info);
					
					VPHY_REALIZED_UNREALIZED.add(phy_ru);
					VPHY_EXPO_REALIZED.add(phy_expo_r);
					VPHY_EXPO_UNREALIZED.add(phy_expo_u);
					VPHY_EXPO_REALIZED_INFO.add(phy_expo_r_info);
					VPHY_EXPO_UNREALIZED_INFO.add(phy_expo_u_info);
					
					VFIN_REALIZED_UNREALIZED.add(fin_ru);
					VFIN_EXPO_REALIZED.add(fin_expo_r);
					VFIN_EXPO_UNREALIZED.add(fin_expo_u);
					VFIN_EXPO_REALIZED_INFO.add(fin_expo_r_info);
					VFIN_EXPO_UNREALIZED_INFO.add(fin_expo_u_info);
					
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
					
					VEFF_RATE_INFO.add(eff_rate_info);
					
					VPHY_FORWARD_PRICE.add(nf2.format(phy_fwd_price));
					VFIN_FORWARD_PRICE.add(nf2.format(fin_fwd_price));
					
					String fin_realized_leg_info="";
					String fin_unrealized_leg_info="";
					String phy_unrealized_leg_info="";
					
					double fin_realized_leg=0;
					if(fin_ru.equals("U"))
					{
						fin_realized_leg=Double.parseDouble(fin_expo_r) * ((settle_price +(Double.parseDouble(constant)/Double.parseDouble(slope)))+(weighted_average/Double.parseDouble(slope)));
						fin_realized_leg_info = "= Fin. Realized Expo. * ((Settle Price +(Const. / Slope)) + (WA Charges / Slope)) \n = "+fin_expo_r+" MMBTU * (("+nf2.format(settle_price)+" + ("+constant+" / "+slope+")) + ("+nf2.format(weighted_average)+" / "+slope+"))\n = "+nf.format(fin_realized_leg);
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
							fin_realized_leg_info = "= Fin. Realized Expo. * ((Eff. Settle Price +(Const. / Slope)) + (WA Charges / Slope)) \n = "+fin_expo_r+" MMBTU * (("+VTEMP_AVG_SETTLE_PRICE.elementAt(i)+" + ("+constant+" / "+slope+")) + ("+nf2.format(weighted_average)+" / "+slope+"))\n = "+nf.format(fin_realized_leg);
						}
						else if(price_type.equals("M"))	
						{
							fin_realized_leg=Double.parseDouble(fin_expo_r) * ((settle_price +(Double.parseDouble(constant)/Double.parseDouble(slope)))+(weighted_average/Double.parseDouble(slope)));
							fin_realized_leg_info = "= Fin. Realized Expo. * ((Settle Price +(Const. / Slope)) + (WA Charges / Slope)) \n = "+fin_expo_r+" MMBTU * (("+nf2.format(settle_price)+" + ("+constant+" / "+slope+")) + ("+nf2.format(weighted_average)+" / "+slope+"))\n = "+nf.format(fin_realized_leg);
						}
						else
						{
							fin_realized_leg=eff_rate * Double.parseDouble(fin_expo_r);
							fin_realized_leg_info = "= Fin. Realized Expo. * Eff. Contract Price \n = "+fin_expo_r+" MMBTU * "+nf2.format(eff_rate)+"\n = "+nf.format(fin_realized_leg);
						}
					}
					tot_fin_realized_leg+=fin_realized_leg;
					
					double phy_unrealized_leg=0;
					if(!phy_expo_u.equals(""))
					{
						phy_unrealized_leg=Double.parseDouble(phy_expo_u) * (phy_fwd_price+weighted_average);
						phy_unrealized_leg_info = "= Phy. Unrealized Expo. * (Phy. Forward Price + WA Charges) \n = "+phy_expo_u+" MMBTU * ("+phy_fwd_price+" + "+nf2.format(weighted_average)+") \n = "+nf.format(phy_unrealized_leg);

					}
					tot_phy_unrealized_leg+=phy_unrealized_leg;
					
					double fin_unrealized_leg=0;
					if(!fin_expo_u.equals(""))
					{
						fin_unrealized_leg=Double.parseDouble(fin_expo_u) * ((fin_fwd_price + (Double.parseDouble(constant)/Double.parseDouble(slope)))+(weighted_average/Double.parseDouble(slope)));
						fin_unrealized_leg_info = "= Fin. Unrealized Expo. * ((Fin. Forward Price +(Const. / Slope)) + (WA Charges / Slope) \n = "+fin_expo_u+" MMBTU * (("+fin_fwd_price+" * ("+constant+" / "+slope+")) + ("+nf2.format(weighted_average)+" / "+slope+"))\n = "+nf.format(fin_unrealized_leg);
					}
					tot_fin_unrealized_leg+=fin_unrealized_leg;
					
					double total_amt= phy_unrealized_leg+fin_unrealized_leg+fin_realized_leg;
					tot_total+=total_amt;
					
					VPHY_UNREALIZED_LEG.add(nf.format(phy_unrealized_leg));
					VFIN_UNREALIZED_LEG.add(nf.format(fin_unrealized_leg));
					VFIN_REALIZED_LEG.add(nf.format(fin_realized_leg));
					VPHY_UNREALIZED_LEG_INFO.add(phy_unrealized_leg_info);
					VFIN_UNREALIZED_LEG_INFO.add(fin_unrealized_leg_info);
					VFIN_REALIZED_LEG_INFO.add(fin_realized_leg_info);
					
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
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
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
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return temp;
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
				stmt_temp=conn.prepareStatement(queryString_temp);
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
					queryString_temp="SELECT EXCHG_VAL "
							+ "FROM FMS_EXCHG_RATE_ENTRY A "
							+ "WHERE EXCHG_RATE_CD=? "
							+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B "
							+ "WHERE A.EXCHG_RATE_CD=B.EXCHG_RATE_CD AND B.EFF_DT <= TO_DATE(?,'DD/MM/YYYY'))";
					stmt_temp=conn.prepareStatement(queryString_temp);
					stmt_temp.setString(1, exchng_rate_cd);
					stmt_temp.setString(2, date);
					rset_temp=stmt_temp.executeQuery();
					if(rset_temp.next())
					{
						exchangRate=rset_temp.getDouble(1);
					}
					rset_temp.close();
					stmt_temp.close();
				}
			}
			
			if(exchangRate==0) //IF EXCHNG_RATE==0, DEFAULT 'Shell Treasury Rate' WILL BE CONSIDERED
			{
				String rate_nm="Shell Treasury Rate";
				
				String queryString_temp="SELECT EXC_RATE_CD "
						+ "FROM FMS_EXCHG_RATE_MST "
						+ "WHERE UPPER(EXC_RATE_NM) = ?"; 
				stmt_temp=conn.prepareStatement(queryString_temp);
				stmt_temp.setString(1, rate_nm.toUpperCase());
				rset_temp=stmt_temp.executeQuery();
				if(rset_temp.next())
				{
					exchng_rate_cd = rset_temp.getString(1)==null?"":rset_temp.getString(1);
				}
				rset_temp.close();
				stmt_temp.close();
				
				queryString_temp="SELECT EXCHG_VAL "
						+ "FROM FMS_EXCHG_RATE_ENTRY A "
						+ "WHERE EXCHG_RATE_CD=? "
						+ "AND EFF_DT =(SELECT MAX(B.EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B "
						+ "WHERE A.EXCHG_RATE_CD=B.EXCHG_RATE_CD  "
						+ "AND B.EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) AND FLAG=?";
				stmt_temp=conn.prepareStatement(queryString_temp);
				stmt_temp.setString(1, exchng_rate_cd);
				stmt_temp.setString(2, "Y");
				rset_temp=stmt_temp.executeQuery();
				if(rset_temp.next())
				{
					exchangRate = rset_temp.getDouble(1);
				}
				rset_temp.close();
				stmt_temp.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
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
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return nm;
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
					+ "?,?,?,"
					+ "?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
					+ "?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,?,"
					+ "?,?,?,?,?,?,?,?,?,?,"
					+ "?,?,?,?,?,?,?,?,"
					+ "?,?,SYSDATE)";
			//System.out.println("MST-->"+queryString);
			stmt_temp=conn.prepareStatement(queryString);
			stmt_temp.setString(1, comp_cd);
			stmt_temp.setString(2, report_dt);
			stmt_temp.setString(3, account);
			stmt_temp.setString(4, counterparty_cd);
			stmt_temp.setString(5, ""+VCOUNTERPARTY_NM.elementAt(i));
			stmt_temp.setString(6, contract_type);
			stmt_temp.setString(7, mappingId);
			stmt_temp.setString(8, ""+VCONT_REF_NO.elementAt(i));
			stmt_temp.setString(9, ""+VSIGNING_DT.elementAt(i));
			stmt_temp.setString(10, ""+VSTART_DT.elementAt(i));
			stmt_temp.setString(11, ""+VEND_DT.elementAt(i));
			stmt_temp.setString(12, entered_cd);
			stmt_temp.setString(13, entered_dt);
			stmt_temp.setString(14, approved_cd);
			stmt_temp.setString(15, approved_dt);
			stmt_temp.setString(16, ""+VCONT_STATUS_FLG.elementAt(i));
			stmt_temp.setString(17, ""+VPRICE_TYPE_EOD.elementAt(i));
			stmt_temp.setString(18, ""+VRATE.elementAt(i));
			stmt_temp.setString(19, rate_unit);
			stmt_temp.setString(20, ""+VPHYS_CURVE_NM.elementAt(i));
			stmt_temp.setString(21, ""+VFIN_CURVE_NM.elementAt(i));
			stmt_temp.setString(22, total_dcq);
			stmt_temp.setString(23, total_actual_qty);
			stmt_temp.setString(24, phy_expo_ori);
			stmt_temp.setString(25, fin_expo_ori);
			stmt_temp.setString(26, phy_expo_unrealized);
			stmt_temp.setString(27, fin_expo_unrealized);
			stmt_temp.setString(28, phy_expo_realized);
			stmt_temp.setString(29, fin_expo_realized);
			stmt_temp.setString(30, phy_unrealized_leg);
			stmt_temp.setString(31, fin_unrealized_leg);
			stmt_temp.setString(32, fin_realized_leg);
			stmt_temp.setString(33, total);
			stmt_temp.setString(34, "");
			stmt_temp.setString(35, "");
			stmt_temp.setString(36, ""+VDEAL_NUM_EOD.elementAt(i));
			stmt_temp.setString(37, emp_cd);			
			stmt_temp.executeUpdate();
			stmt_temp.close();
			
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
				
				queryString="INSERT INTO FMS_MR_EXPO_EOD_DTL(COMPANY_CD,REPORT_DT,BUY_SELL,COUNTERPARTY_CD,"
						+ "CONTRACT_TYPE, MAPPING_ID,GAS_DT, "
						+ "CONT_MTH,DCQ,ALLOC_QTY,"
						+ "SEQ_NO,PRICE_TYPE,FIN_CURVE_NM,"
						+ "CONT_PRICE,RATE_UNIT,"
						+ "SPOT_MTH,SPOT_START_DT,SPOT_END_DT,"
						+ "SETTLE_PRICE,"
						+ "RU_PHY_FLAG,RU_FIN_FLAG,"
						+ "FWD_PRICE_PHY,FWD_PRICE_FIN,"
						+ "SLOPE, CONST, EFF_RATE_USD, "
						+ "ORI_EXPO_PHY, ORI_EXPO_FIN, "
						+ "UNR_EXPO_PHY, UNR_EXPO_FIN, "
						+ "R_EXPO_PHY, R_EXPO_FIN,"
						+ "UNR_PHY_LEG, UNR_FIN_LEG, "
						+ "R_FIN_LEG, MTM_TOTAL,ENT_BY,ENT_DT,PHY_CURVE_NM,WA_RATE) "
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
				stmt_temp1=conn.prepareStatement(queryString);
				stmt_temp1.setString(1, comp_cd);
				stmt_temp1.setString(2, report_dt);
				stmt_temp1.setString(3, account);
				stmt_temp1.setString(4, counterparty_cd);
				stmt_temp1.setString(5, contract_type);
				stmt_temp1.setString(6, mappingId);
				stmt_temp1.setString(7, ""+VGAS_DT_MULTI.elementAt(j));
				stmt_temp1.setString(8, cont_mth);
				stmt_temp1.setString(9, ""+VDCQ.elementAt(j));
				stmt_temp1.setString(10, ""+VACTUAL_QTY.elementAt(j));
				stmt_temp1.setString(11, ""+VSEQ_NO.elementAt(j));
				stmt_temp1.setString(12, ""+VPRICE_TYPE_DTL_EOD.elementAt(j));
				stmt_temp1.setString(13, ""+VFIN_CURVE_NM_DTL.elementAt(j));
				stmt_temp1.setString(14, ""+VCONTRACT_PRICE.elementAt(j));
				stmt_temp1.setString(15, rate_unit);
				stmt_temp1.setString(16, fin_mth);
				stmt_temp1.setString(17, ""+VSETTLE_START_DT.elementAt(j));
				stmt_temp1.setString(18, ""+VSETTLE_END_DT.elementAt(j));
				stmt_temp1.setString(19, ""+VSETTLE_PRICE.elementAt(j));
				stmt_temp1.setString(20, ""+VPHY_REALIZED_UNREALIZED.elementAt(j));
				stmt_temp1.setString(21, ""+VFIN_REALIZED_UNREALIZED.elementAt(j));
				stmt_temp1.setString(22, ""+VPHY_FORWARD_PRICE.elementAt(j));
				stmt_temp1.setString(23, ""+VFIN_FORWARD_PRICE.elementAt(j));
				stmt_temp1.setString(24, ""+VSLOPE.elementAt(j));
				stmt_temp1.setString(25, ""+VCONST.elementAt(j));
				stmt_temp1.setString(26, ""+VEFF_RATE.elementAt(j));
				stmt_temp1.setString(27, ""+VPHY_EXPO_ORIGINAL.elementAt(j));
				stmt_temp1.setString(28, ""+VFIN_EXPO_ORIGINAL.elementAt(j));
				stmt_temp1.setString(29, ""+VPHY_EXPO_UNREALIZED.elementAt(j));
				stmt_temp1.setString(30, ""+VFIN_EXPO_UNREALIZED.elementAt(j));
				stmt_temp1.setString(31, ""+VPHY_EXPO_REALIZED.elementAt(j));
				stmt_temp1.setString(32, ""+VFIN_EXPO_REALIZED.elementAt(j));
				stmt_temp1.setString(33, ""+VPHY_UNREALIZED_LEG.elementAt(j));
				stmt_temp1.setString(34, ""+VFIN_UNREALIZED_LEG.elementAt(j));
				stmt_temp1.setString(35, ""+VFIN_REALIZED_LEG.elementAt(j));
				stmt_temp1.setString(36, ""+VTOTAL.elementAt(j));
				stmt_temp1.setString(37, emp_cd);
				stmt_temp1.setString(38, ""+VPHYS_CURVE_NM_DTL.elementAt(j));
				stmt_temp1.setString(39, ""+VTOTAL_CHARGE.elementAt(j));
				stmt_temp1.executeUpdate();
				stmt_temp1.close();
			}
			
			conn.commit();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
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
			stmt_temp=conn.prepareStatement(queryString);
			stmt_temp.setString(1, report_dt);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				count=rset_temp.getInt(1);
			}
			rset_temp.close();
			stmt_temp.close();
			
			queryString="DELETE FROM FMS_MR_EXPO_EOD_MST "
					+ "WHERE REPORT_DT=TO_DATE(?,'DD/MM/YYYY')";
			stmt_temp=conn.prepareStatement(queryString);
			stmt_temp.setString(1, report_dt);
			stmt_temp.executeUpdate();
			
			count= 0;
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_MR_EXPO_EOD_DTL "
					+ "WHERE REPORT_DT=TO_DATE(?,'DD/MM/YYYY')";
			stmt_temp=conn.prepareStatement(queryString);
			stmt_temp.setString(1, report_dt);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				count=rset_temp.getInt(1);
			}
			rset_temp.close();
			stmt_temp.close();
			
			queryString="DELETE FROM FMS_MR_EXPO_EOD_DTL "
					+ "WHERE REPORT_DT=TO_DATE(?,'DD/MM/YYYY')";
			stmt_temp=conn.prepareStatement(queryString);
			stmt_temp.setString(1, report_dt);
			stmt_temp.executeUpdate();

		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getEodFreezedData()
	{
		String function_nm="getEodFreezedData()";
		try
		{
			String queryString="SELECT COUNTERPARTY_CD,BUY_SELL,CONTRACT_TYPE,MAPPING_ID,TO_CHAR(REPORT_DT,'DD/MM/YYYY'),COMPANY_CD,CONT_REF "
					+ "FROM FMS_MR_EXPO_EOD_MST "
					+ "WHERE REPORT_DT=TO_DATE(?,'DD/MM/YYYY')";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, report_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String cd = rset.getString(1)==null?"":rset.getString(1);
				String buySell = rset.getString(2)==null?"":rset.getString(2);
				String cont_type = rset.getString(3)==null?"":rset.getString(3);
				String mapp_id = rset.getString(4)==null?"":rset.getString(4);
				String rpt_dt = rset.getString(5)==null?"":rset.getString(5);
				String comp_cd = rset.getString(6)==null?"":rset.getString(6);
				String legalEntity=utilBean.getCompanyAbbr(conn, comp_cd);
				String contRef = rset.getString(7)==null?"":rset.getString(7);
				
				int sr_no=0;
				String queryString1="SELECT A.COUNTERPARTY_NM,A.BUY_SELL,TO_CHAR(A.CONT_SIGN_DT,'DD/MM/YYYY'),A.MAPPING_ID,A.CONTRACT_TYPE,"
						+ "B.FIN_CURVE_NM,A.PHYS_CURVE,TO_CHAR(B.GAS_DT,'DD/MM/YYYY'),B.DCQ,B.PRICE_TYPE,B.ALLOC_QTY,B.CONT_PRICE,"
						+ "TO_CHAR(B.SPOT_MTH,'DD/MM/YYYY'),TO_CHAR(B.SPOT_START_DT,'DD/MM/YYYY'),TO_CHAR(B.SPOT_END_DT,'DD/MM/YYYY'),"
						+ "B.SLOPE,B.CONST,B.EFF_RATE_USD,TO_CHAR(B.CONT_MTH,'DD/MM/YYYY'),B.ORI_EXPO_PHY,B.ORI_EXPO_FIN,"
						+ "B.RU_PHY_FLAG,B.RU_FIN_FLAG,B.UNR_EXPO_PHY,B.UNR_EXPO_FIN,B.R_EXPO_PHY,B.R_EXPO_FIN,"
						+ "B.FWD_PRICE_PHY,B.FWD_PRICE_FIN,B.SETTLE_PRICE,B.UNR_PHY_LEG,B.UNR_FIN_LEG,B.R_FIN_LEG,B.MTM_TOTAL,B.SEQ_NO,"
						+ "B.PHY_CURVE_NM, A.DEAL_NUM,B.WA_RATE "
						+ "FROM FMS_MR_EXPO_EOD_MST A, FMS_MR_EXPO_EOD_DTL B "
						+ "WHERE A.COMPANY_CD=? AND A.REPORT_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.COUNTERPARTY_CD=? AND A.BUY_SELL=? AND A.CONTRACT_TYPE=? "
						+ "AND A.MAPPING_ID=? "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.REPORT_DT=B.REPORT_DT "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SELL=B.BUY_SELL AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
						+ "AND A.MAPPING_ID=B.MAPPING_ID "
						+ "ORDER BY GAS_DT,SEQ_NO";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, rpt_dt);
				stmt1.setString(3, cd);
				stmt1.setString(4, buySell);
				stmt1.setString(5, cont_type);
				stmt1.setString(6, mapp_id);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					int seqNo = rset1.getInt(35);
					if(seqNo==0)
					{
						sr_no+=1;
						VSR.add(sr_no);
					}
					else
					{
						VSR.add("");
					}
					VLEGAL_ENTITY.add(legalEntity);
					VCONT_REF_NO.add(contRef);
					VCOUNTERPARTY_NM.add(rset1.getString(1)==null?"":rset1.getString(1));
					VACCOUNT.add(rset1.getString(2)==null?"":rset1.getString(2));
					VSIGNING_DT.add(rset1.getString(3)==null?"":rset1.getString(3));
					VDIS_CONTRACT_TYPE.add(utilBean.getMR_ContractTypeName(cont_type));
					
					/*
					String dealNo="";
					if(!mapp_id.equals(""))
					{
						String split[] = mapp_id.split("-");
						dealNo=utilBean.getDisplayDealMapping(split[1], "", split[2], "", cont_type);
					}
					VDISPLAY_DEAL_MAP.add(dealNo);
					*/
					
					VFIN_CURVE_NM.add(rset1.getString(6)==null?"":rset1.getString(6));
					//VPHYS_CURVE_NM.add(rset1.getString(7)==null?"":rset1.getString(7)); THIS IS COMMING FROM MST TABLE AS INCIDENT#2410042 COMMENTED
					VGAS_DT.add(rset1.getString(8)==null?"":rset1.getString(8));
					VDCQ.add(rset1.getString(9)==null?"":nf.format(rset1.getDouble(9)));
					VPRICE_TYPE.add(rset1.getString(10)==null?"":rset1.getString(10));
					VACTUAL_QTY.add(rset1.getString(11)==null?"":nf.format(rset1.getDouble(11)));
					VCONTRACT_PRICE.add(rset1.getString(12)==null?"":nf2.format(rset1.getDouble(12)));
					VFIN_MMYYYY.add(rset1.getString(13)==null?"":rset1.getString(13));
					VSETTLE_START_DT.add(rset1.getString(14)==null?"":rset1.getString(14));
					VSETTLE_END_DT.add(rset1.getString(15)==null?"":rset1.getString(15));
					VSLOPE.add(rset1.getString(16)==null?"":rset1.getString(16));
					VCONST.add(rset1.getString(17)==null?"":rset1.getString(17));
					VEFF_RATE.add(rset1.getString(18)==null?"":nf2.format(rset1.getDouble(18)));
					VCONT_MMYYYY.add(rset1.getString(19)==null?"":rset1.getString(19));
					VPHY_EXPO_ORIGINAL.add(rset1.getString(20)==null?"":nf.format(rset1.getDouble(20)));
					VFIN_EXPO_ORIGINAL.add(rset1.getString(21)==null?"":nf.format(rset1.getDouble(21)));
					VPHY_REALIZED_UNREALIZED.add(rset1.getString(22)==null?"":rset1.getString(22));
					VFIN_REALIZED_UNREALIZED.add(rset1.getString(23)==null?"":rset1.getString(23));
					VPHY_EXPO_UNREALIZED.add(rset1.getString(24)==null?"":nf.format(rset1.getDouble(24)));
					VFIN_EXPO_UNREALIZED.add(rset1.getString(25)==null?"":nf.format(rset1.getDouble(25)));
					VPHY_EXPO_REALIZED.add(rset1.getString(26)==null?"":nf.format(rset1.getDouble(26)));
					VFIN_EXPO_REALIZED.add(rset1.getString(27)==null?"":nf.format(rset1.getDouble(27)));
					VPHY_FORWARD_PRICE.add(rset1.getString(28)==null?"":nf2.format(rset1.getDouble(28)));
					VFIN_FORWARD_PRICE.add(rset1.getString(29)==null?"":nf2.format(rset1.getDouble(29)));
					VSETTLE_PRICE.add(rset1.getString(30)==null?"":nf2.format(rset1.getDouble(30)));
					VPHY_UNREALIZED_LEG.add(rset1.getString(31)==null?"":nf.format(rset1.getDouble(31)));
					VFIN_UNREALIZED_LEG.add(rset1.getString(32)==null?"":nf.format(rset1.getDouble(32)));
					VFIN_REALIZED_LEG.add(rset1.getString(33)==null?"":nf.format(rset1.getDouble(33)));
					VTOTAL.add(rset1.getString(34)==null?"":nf.format(rset1.getDouble(34)));
					VPHYS_CURVE_NM.add(rset1.getString(36)==null?"":rset1.getString(36)); //AS PER INCIDENT#2410042 FETCHING FROM DTL TABLE
					VDISPLAY_DEAL_MAP.add(rset1.getString(37)==null?"":rset1.getString(37));
					VTOTAL_CHARGE.add(rset1.getString(38)==null?"":rset1.getString(38));
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
	
	public void getExposureDetailFreezedData()
	{
		String function_nm="getExposureDetailFreezedData()";
		try
		{
			String mapp_id=counterparty_cd+"-"+agmt_no+"-"+cont_no;
			if(contract_type.equals("N") || contract_type.equals("V"))
			{
				mapp_id+="-"+cargo_no;
			}
			
			String queryString="SELECT COUNTERPARTY_NM,CONT_REF,TO_CHAR(CONT_SIGN_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(CONT_START_DT,'DD/MM/YYYY'),TO_CHAR(CONT_END_DT,'DD/MM/YYYY'),"
					+ "TOT_DCQ,TOT_ALLOC_QTY,TOT_ORI_EXPO_PHY,TOT_ORI_EXPO_FIN,TOT_UNR_EXPO_PHY,TOT_UNR_EXPO_FIN,"
					+ "TOT_R_EXPO_PHY,TOT_R_EXPO_FIN,TOT_UNR_PHY_LEG,TOT_UNR_FIN_LEG,TOT_R_FIN_LEG,TOT_MTM_TOTAL,"
					+ "CONT_ENT_BY,TO_CHAR(CONT_ENT_DT,'DD/MM/YYYY'),CONT_APRV_BY,TO_CHAR(CONT_APRV_DT,'DD/MM/YYYY') "
					+ "FROM FMS_MR_EXPO_EOD_MST "
					+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND COUNTERPARTY_CD=? AND BUY_SELL=? AND CONTRACT_TYPE=? "
					+ "AND MAPPING_ID=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, report_dt);
			stmt.setString(3, counterparty_cd);
			stmt.setString(4, account);
			stmt.setString(5, contract_type);
			stmt.setString(6, mapp_id);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				counterparty_nm=rset.getString(1)==null?"":rset.getString(1);
				deal_ref_no=rset.getString(2)==null?"":rset.getString(2);
				deal_dt=rset.getString(3)==null?"":rset.getString(3);
				start_dt=rset.getString(4)==null?"":rset.getString(4);
				end_dt=rset.getString(5)==null?"":rset.getString(5);
				//display_deal_mapp=utilBean.getDisplayDealMapping(agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type);
				
				display_deal_mapp=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, cargo_no);
				
				total_dcq=rset.getString(6)==null?"":nf.format(rset.getDouble(6));
				total_actual_qty=rset.getString(7)==null?"":nf.format(rset.getDouble(7));
				phy_expo_ori=rset.getString(8)==null?"":nf.format(rset.getDouble(8));
				fin_expo_ori=rset.getString(9)==null?"":nf.format(rset.getDouble(9));
				phy_expo_unrealized=rset.getString(10)==null?"":nf.format(rset.getDouble(10));
				fin_expo_unrealized=rset.getString(11)==null?"":nf.format(rset.getDouble(11));
				phy_expo_realized=rset.getString(12)==null?"":nf.format(rset.getDouble(12));
				fin_expo_realized=rset.getString(13)==null?"":nf.format(rset.getDouble(13));
				phy_unrealized_leg=rset.getString(14)==null?"":nf.format(rset.getDouble(14));
				fin_unrealized_leg=rset.getString(15)==null?"":nf.format(rset.getDouble(15));
				fin_realized_leg=rset.getString(16)==null?"":nf.format(rset.getDouble(16));
				total=rset.getString(17)==null?"":nf.format(rset.getDouble(17));
				
				entered_cd=rset.getString(18)==null?"":rset.getString(18);
				entered_by=utilBean.getEmpName(conn,entered_cd);
				entered_dt=rset.getString(19)==null?"":rset.getString(19);
				approved_cd=rset.getString(20)==null?"":rset.getString(20);
				approved_by=utilBean.getEmpName(conn,approved_cd);
				approved_dt=rset.getString(21)==null?"":rset.getString(21);
			}
			rset.close();
			stmt.close();
			
			int sr=0;
			queryString="SELECT TO_CHAR(GAS_DT,'DD/MM/YYYY'),TO_CHAR(CONT_MTH,'MM/YYYY'),DCQ,ALLOC_QTY,"
					+ "PRICE_TYPE,CONT_PRICE,TO_CHAR(SPOT_MTH,'MM/YYYY'),TO_CHAR(SPOT_START_DT,'DD/MM/YYYY'),TO_CHAR(SPOT_END_DT,'DD/MM/YYYY'),"
					+ "SETTLE_PRICE,RU_PHY_FLAG,RU_FIN_FLAG,FWD_PRICE_PHY,FWD_PRICE_FIN,SLOPE,CONST,"
					+ "EFF_RATE_USD,ORI_EXPO_PHY,ORI_EXPO_FIN,UNR_EXPO_PHY,UNR_EXPO_FIN,R_EXPO_PHY,R_EXPO_FIN,"
					+ "UNR_PHY_LEG,UNR_FIN_LEG,R_FIN_LEG,MTM_TOTAL,SEQ_NO,PHY_CURVE_NM,FIN_CURVE_NM,WA_RATE "
					+ "FROM FMS_MR_EXPO_EOD_DTL "
					+ "WHERE COMPANY_CD=? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND COUNTERPARTY_CD=? AND BUY_SELL=? AND CONTRACT_TYPE=? "
					+ "AND MAPPING_ID=? "
					+ "ORDER BY GAS_DT,SEQ_NO";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, report_dt);
			stmt.setString(3, counterparty_cd);
			stmt.setString(4, account);
			stmt.setString(5, contract_type);
			stmt.setString(6, mapp_id);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VGAS_DT.add(rset.getString(1)==null?"":rset.getString(1));
				VCONT_MMYYYY.add(rset.getString(2)==null?"":rset.getString(2));
				VDCQ.add(rset.getString(3)==null?"":nf.format(rset.getDouble(3)));
				VACTUAL_QTY.add(rset.getString(4)==null?"":nf.format(rset.getDouble(4)));
				//VPRICE_TYPE.add(rset.getString(5)==null?"":rset.getString(5));
				VCONTRACT_PRICE.add(rset.getString(6)==null?"":nf2.format(rset.getDouble(6)));
				VFIN_MMYYYY.add(rset.getString(7)==null?"":rset.getString(7));
				VSETTLE_START_DT.add(rset.getString(8)==null?"":rset.getString(8));
				VSETTLE_END_DT.add(rset.getString(9)==null?"":rset.getString(9));
				VSETTLE_PRICE.add(rset.getString(10)==null?"":nf2.format(rset.getDouble(10)));
				VPHY_REALIZED_UNREALIZED.add(rset.getString(11)==null?"":rset.getString(11));
				VFIN_REALIZED_UNREALIZED.add(rset.getString(12)==null?"":rset.getString(12));
				VPHY_FORWARD_PRICE.add(rset.getString(13)==null?"":nf2.format(rset.getDouble(13)));
				VFIN_FORWARD_PRICE.add(rset.getString(14)==null?"":nf2.format(rset.getDouble(14)));
				VSLOPE.add(rset.getString(15)==null?"":rset.getString(15));
				VCONST.add(rset.getString(16)==null?"":rset.getString(16));
				VEFF_RATE.add(rset.getString(17)==null?"":nf2.format(rset.getDouble(17)));
				VPHY_EXPO_ORIGINAL.add(rset.getString(18)==null?"":nf.format(rset.getDouble(18)));
				VFIN_EXPO_ORIGINAL.add(rset.getString(19)==null?"":nf.format(rset.getDouble(19)));
				VPHY_EXPO_UNREALIZED.add(rset.getString(20)==null?"":nf.format(rset.getDouble(20)));
				VFIN_EXPO_UNREALIZED.add(rset.getString(21)==null?"":nf.format(rset.getDouble(21)));
				VPHY_EXPO_REALIZED.add(rset.getString(22)==null?"":nf.format(rset.getDouble(22)));
				VFIN_EXPO_REALIZED.add(rset.getString(23)==null?"":nf.format(rset.getDouble(23)));
				VPHY_UNREALIZED_LEG.add(rset.getString(24)==null?"":nf.format(rset.getDouble(24)));
				VFIN_UNREALIZED_LEG.add(rset.getString(25)==null?"":nf.format(rset.getDouble(25)));
				VFIN_REALIZED_LEG.add(rset.getString(26)==null?"":nf.format(rset.getDouble(26)));
				VTOTAL.add(rset.getString(27)==null?"":nf.format(rset.getDouble(27)));
				VPHYS_CURVE_NM_DTL.add(rset.getString(29)==null?"":rset.getString(29)); //INCIDENT#2410042
				
				//JD
				String tmp_price_type = rset.getString(5)==null?"":rset.getString(5);
				if(!tmp_price_type.equals("Fixed"))
				{
					tmp_price_type = tmp_price_type+" : "+(rset.getString(30)==null?"":rset.getString(30)); 
				}
				VPRICE_TYPE.add(tmp_price_type);
				VTOTAL_CHARGE.add(rset.getString(31)==null?"":nf.format(rset.getDouble(31)));
				
				int seq_no=rset.getInt(28);
				if(contract_type.equals("V"))
				{
					sr+=1;
					VSR.add(sr);
				}
				else if(seq_no==0) 
				{
					sr+=1;
					VSR.add(sr);
				}
				else 
				{
					VSR.add("");
				}				
				
				if((sr%2)==0)
				{
					VROW_COLOR.add("#e6e6e6");
				}
				else
				{
					VROW_COLOR.add("");
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
	
	public void generateQPcsvFile()
	{
		String function_nm="generateQPcsvFile()";
		BufferedWriter bw = null; 
		PrintWriter pw = null;
		FileWriter fw = null;
		
		try
		{
			String QP_file_path=utilBean.getAutomationKeyDetail(conn,"DAILY_RPT_PATH");
			String split_date[] = report_dt.split("/");
			String date = split_date[2]+split_date[1]+split_date[0];
			String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
			
			String QP_file_nm="QP_Exposure_"+date+".csv";			
			
			if(!QP_file_path.equals(""))
			{
				if(new File(QP_file_path).exists())
				{	
					if(new File(QP_file_path+""+QP_file_nm).exists())
					{						
						QP_file_nm="QP_Exposure_"+date+"-"+timestamp+".csv";
					}
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
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		finally 
		{
			try
			{
				if(pw!=null) {pw.close();}
				if(bw!=null) {bw.close();}
				if(fw!=null) {fw.close();}
			}
			catch(Exception e)
			{
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			}
		}
	}
	
	public void doClear()
	{
		String function_nm="generateQPcsvFile()";
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
			VPRICE_TYPE_DTL_EOD.clear();			
			VTOTAL_CHARGE.clear();
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
	
	String report_dt = "";
	String expo_type = "";
	String counterparty_cd = "";
	String cont_no = "";
	String cont_rev_no = "";
	String agmt_no = "";
	String agmt_rev_no = "";
	String contract_type = "";
	String account = "";
	String emp_cd = "";
	String cargo_no = "";
	int index = 0;
	
	public void setReport_dt(String report_dt) {this.report_dt = report_dt;}
	public void setExpo_type(String expo_type) {this.expo_type = expo_type;}
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setCont_no(String cont_no) {this.cont_no = cont_no;}
	public void setCont_rev_no(String cont_rev_no) {this.cont_rev_no = cont_rev_no;}
	public void setAgmt_no(String agmt_no) {this.agmt_no = agmt_no;}
	public void setAgmt_rev_no(String agmt_rev_no) {this.agmt_rev_no = agmt_rev_no;}
	public void setContract_type(String contract_type) {this.contract_type = contract_type;}
	public void setAccount(String account) {this.account = account;}
	public void setEmp_cd(String emp_cd) {this.emp_cd = emp_cd;}
	public void setCargo_no(String cargo_no) {this.cargo_no = cargo_no;}
	
	public void setIndex(int index) {this.index = index;}
	
	//FOR EOD PROCESS
	//Vector VCOUNTERPARTY_CD_EOD = new Vector();
	//Vector VCONT_NO_EOD = new Vector();
	//Vector VCONT_REV_NO_EOD = new Vector();
	//Vector VAGMT_NO_EOD = new Vector();
	//Vector VAGMT_REV_NO_EOD = new Vector();
	//Vector VCONTRACT_TYPE_EOD = new Vector();
	//Vector VACCOUNT_EOD = new Vector();
	
	Vector VGAS_DT_MULTI = new Vector();
	Vector VSEQ_NO = new Vector();
	Vector VFIN_CURVE_NM_DTL = new Vector();
	Vector VPRICE_TYPE_EOD = new Vector();
	Vector VPRICE_TYPE_DTL_EOD = new Vector();
	Vector VMAPPING_ID = new Vector();
	Vector VCONT_MMYYYY_EOD = new Vector();
	Vector VDEAL_NUM_EOD = new Vector();
	Vector VPHYS_CURVE_NM_DTL = new Vector();
	/////////
	
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
	Vector VCONTRACT_TYPE = new Vector();
	Vector VCARGO_NO = new Vector();
	Vector VSIGNING_DT = new Vector();
	Vector VSTART_DT = new Vector();
	Vector VEND_DT = new Vector();
	Vector VACCOUNT = new Vector();
	Vector VDISPLAY_DEAL_MAP = new Vector();
	Vector VRATE = new Vector();
	Vector VRATE_UNIT = new Vector();
	Vector VPRICE_TYPE = new Vector();
	Vector VCONT_REF_NO = new Vector();
	Vector VPHYS_CURVE_NM = new Vector();
	Vector VFIN_CURVE_NM = new Vector();
	Vector VCONT_STATUS = new Vector();
	Vector VCONT_STATUS_FLG = new Vector();
	Vector VGAS_DT = new Vector();
	Vector VDCQ = new Vector();
	Vector VDCQ_COLOR = new Vector();
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
	Vector VIS_EOD_PROCESS_DONE = new Vector();
	Vector VDIS_CONTRACT_TYPE = new Vector();
	
	Vector VCOB_DT = new Vector();
	Vector VLEGAL_ENTITY_CD = new Vector();
	Vector VLEGAL_ENTITY = new Vector();
	Vector VDEAL_NUM = new Vector();
	Vector VCOUNTERPARTY = new Vector();
	Vector VBUY_SELL = new Vector();
	Vector VUNIT = new Vector();
	Vector VCONTRACT_MONTH = new Vector();
	Vector VCURVE_NAME = new Vector();
	Vector VEXPOSURE = new Vector();
	Vector VFINANCIAL_PHYSICAL = new Vector();
	Vector VREALISED_UNREALISED = new Vector();
	Vector VFORWARD_PRICE = new Vector();
	
	Vector VSTORAGE_ROW_HEADING = new Vector();
	Vector VSTORAGE_MMSCM = new Vector();
	Vector VSTORAGE_MMBTU = new Vector();
	Vector VSTORAGE_LAST_DT = new Vector();
	
	Vector VCOMPANY_CD = new Vector();
	Vector VCOMPANY_ABBR = new Vector();
	Vector VCOMPANY_NAME = new Vector();
	Vector VTOTAL_CHARGE = new Vector();
	
	public Vector getVCOB_DT() {return VCOB_DT;}
	public Vector getVLEGAL_ENTITY_CD() {return VLEGAL_ENTITY_CD;}
	public Vector getVLEGAL_ENTITY() {return VLEGAL_ENTITY;}
	public Vector getVDEAL_NUM() {return VDEAL_NUM;}
	public Vector getVCOUNTERPARTY() {return VCOUNTERPARTY;}
	public Vector getVBUY_SELL() {return VBUY_SELL;}
	public Vector getVUNIT() {return VUNIT;}
	public Vector getVCONTRACT_MONTH() {return VCONTRACT_MONTH;}
	public Vector getVCURVE_NAME() {return VCURVE_NAME;}
	public Vector getVEXPOSURE() {return VEXPOSURE;}
	public Vector getVFINANCIAL_PHYSICAL() {return VFINANCIAL_PHYSICAL;}
	public Vector getVREALISED_UNREALISED() {return VREALISED_UNREALISED;}
	public Vector getVFORWARD_PRICE() {return VFORWARD_PRICE;}
	
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVMST_COUNTERPARTY_CD() {return VMST_COUNTERPARTY_CD;}
	public Vector getVMST_COUNTERPARTY_NM() {return VMST_COUNTERPARTY_NM;}
	public Vector getVMST_COUNTERPARTY_ABBR() {return VMST_COUNTERPARTY_ABBR;}
	public Vector getVCONT_NO() {return VCONT_NO;}
	public Vector getVCONT_REV_NO() {return VCONT_REV_NO;}
	public Vector getVAGMT_NO() {return VAGMT_NO;}
	public Vector getVAGMT_REV_NO() {return VAGMT_REV_NO;}
	public Vector getVCONTRACT_TYPE() {return VCONTRACT_TYPE;}
	public Vector getVCARGO_NO() {return VCARGO_NO;}
	public Vector getVSIGNING_DT() {return VSIGNING_DT;}
	public Vector getVSTART_DT() {return VSTART_DT;}
	public Vector getVEND_DT() {return VEND_DT;}
	public Vector getVACCOUNT() {return VACCOUNT;}
	public Vector getVDISPLAY_DEAL_MAP() {return VDISPLAY_DEAL_MAP;}
	public Vector getVRATE() {return VRATE;}
	public Vector getVRATE_UNIT() {return VRATE_UNIT;}
	public Vector getVPRICE_TYPE() {return VPRICE_TYPE;}
	public Vector getVCONT_REF_NO() {return VCONT_REF_NO;}
	public Vector getVPHYS_CURVE_NM() {return VPHYS_CURVE_NM;}
	public Vector getVFIN_CURVE_NM() {return VFIN_CURVE_NM;}
	public Vector getVCONT_STATUS() {return VCONT_STATUS;}
	public Vector getVCONT_STATUS_FLG() {return VCONT_STATUS_FLG;}
	public Vector getVGAS_DT() {return VGAS_DT;}
	public Vector getVDCQ() {return VDCQ;}
	public Vector getVDCQ_COLOR() {return VDCQ_COLOR;}
	public Vector getVACTUAL_QTY() {return VACTUAL_QTY;}
	public Vector getVEFF_RATE() {return VEFF_RATE;}
	public Vector getVEFF_RATE_INFO() {return VEFF_RATE_INFO;}
	public Vector getVPHY_REALIZED_UNREALIZED() {return VPHY_REALIZED_UNREALIZED;}
	public Vector getVFIN_REALIZED_UNREALIZED() {return VFIN_REALIZED_UNREALIZED;}
	public Vector getVPHY_EXPO_REALIZED() {return VPHY_EXPO_REALIZED;}
	public Vector getVFIN_EXPO_REALIZED() {return VFIN_EXPO_REALIZED;}
	public Vector getVPHY_EXPO_UNREALIZED() {return VPHY_EXPO_UNREALIZED;}
	public Vector getVFIN_EXPO_UNREALIZED() {return VFIN_EXPO_UNREALIZED;}
	public Vector getVPHY_EXPO_REALIZED_INFO() {return VPHY_EXPO_REALIZED_INFO;}
	public Vector getVFIN_EXPO_REALIZED_INFO() {return VFIN_EXPO_REALIZED_INFO;}
	public Vector getVPHY_EXPO_UNREALIZED_INFO() {return VPHY_EXPO_UNREALIZED_INFO;}
	public Vector getVFIN_EXPO_UNREALIZED_INFO() {return VFIN_EXPO_UNREALIZED_INFO;}
	public Vector getVSETTLE_PRICE() {return VSETTLE_PRICE;}
	public Vector getVSLOPE() {return VSLOPE;}
	public Vector getVCONST() {return VCONST;}
	public Vector getVPHY_FORWARD_PRICE() {return VPHY_FORWARD_PRICE;}
	public Vector getVFIN_FORWARD_PRICE() {return VFIN_FORWARD_PRICE;}
	public Vector getVPHY_UNREALIZED_LEG() {return VPHY_UNREALIZED_LEG;}
	public Vector getVFIN_UNREALIZED_LEG() {return VFIN_UNREALIZED_LEG;}
	public Vector getVFIN_REALIZED_LEG() {return VFIN_REALIZED_LEG;}
	public Vector getVPHY_UNREALIZED_LEG_INFO() {return VPHY_UNREALIZED_LEG_INFO;}
	public Vector getVFIN_UNREALIZED_LEG_INFO() {return VFIN_UNREALIZED_LEG_INFO;}
	public Vector getVFIN_REALIZED_LEG_INFO() {return VFIN_REALIZED_LEG_INFO;}
	public Vector getVTOTAL() {return VTOTAL;}
	public Vector getVCONT_MMYYYY() {return VCONT_MMYYYY;}
	public Vector getVPRICE_LINE_RATE() {return VPRICE_LINE_RATE;}
	public Vector getVFIN_MMYYYY() {return VFIN_MMYYYY;}
	public Vector getVSETTLE_START_DT() {return VSETTLE_START_DT;}
	public Vector getVSETTLE_END_DT() {return VSETTLE_END_DT;}
	public Vector getVCONTRACT_PRICE() {return VCONTRACT_PRICE;}
	public Vector getVPHY_EXPO_ORIGINAL() {return VPHY_EXPO_ORIGINAL;}
	public Vector getVFIN_EXPO_ORIGINAL() {return VFIN_EXPO_ORIGINAL;}
	public Vector getVPHY_EXPO_ORIGINAL_INFO() {return VPHY_EXPO_ORIGINAL_INFO;}
	public Vector getVFIN_EXPO_ORIGINAL_INFO() {return VFIN_EXPO_ORIGINAL_INFO;}
	public Vector getVSR() {return VSR;}
	public Vector getVROW_COLOR() {return VROW_COLOR;}
	public Vector getVIS_EOD_PROCESS_DONE() {return VIS_EOD_PROCESS_DONE;}
	public Vector getVDIS_CONTRACT_TYPE() {return VDIS_CONTRACT_TYPE;}
	
	public Vector getVPHYS_CURVE_NM_DTL() {return VPHYS_CURVE_NM_DTL;}
	
	public Vector getVSTORAGE_ROW_HEADING() {return VSTORAGE_ROW_HEADING;}
	public Vector getVSTORAGE_MMSCM() {return VSTORAGE_MMSCM;}
	public Vector getVSTORAGE_MMBTU() {return VSTORAGE_MMBTU;}
	public Vector getVSTORAGE_LAST_DT() {return VSTORAGE_LAST_DT;}
	
	public Vector getVCOMPANY_CD() {return VCOMPANY_CD;}
	public Vector getVCOMPANY_ABBR() {return VCOMPANY_ABBR;}
	public Vector getVCOMPANY_NAME() {return VCOMPANY_NAME;}
	public Vector getVTOTAL_CHARGE() {return VTOTAL_CHARGE;}
	
	String counterparty_nm="";
	String display_deal_mapp="";
	String deal_ref_no="";
	String deal_dt="";
	String start_dt="";
	String end_dt="";
	String entered_by="";
	String entered_cd="";
	String entered_dt="";
	String approved_by="";
	String approved_cd="";
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
	
	public String getCounterparty_nm() {return counterparty_nm;}
	public String getDisplay_deal_mapp() {return display_deal_mapp;}
	public String getDeal_ref_no() {return deal_ref_no;}
	public String getDeal_dt() {return deal_dt;}
	public String getStart_dt() {return start_dt;}
	public String getEnd_dt() {return end_dt;}
	public String getEntered_by() {return entered_by;}
	public String getEntered_dt() {return entered_dt;}
	public String getApproved_by() {return approved_by;}
	public String getApproved_dt() {return approved_dt;}
	public String getDcq() {return dcq;}
	public String getRate() {return rate;}
	public String getRate_unit() {return rate_unit;}
	public String getTotal() {return total;}
	public String getFin_realized_leg() {return fin_realized_leg;}
	public String getFin_unrealized_leg() {return fin_unrealized_leg;}
	public String getPhy_unrealized_leg() {return phy_unrealized_leg;}
	public String getPhy_expo_ori() {return phy_expo_ori;}
	public String getFin_expo_ori() {return fin_expo_ori;}
	public String getPhy_expo_unrealized() {return phy_expo_unrealized;}
	public String getFin_expo_unrealized() {return fin_expo_unrealized;}
	public String getPhy_expo_realized() {return phy_expo_realized;}
	public String getFin_expo_realized() {return fin_expo_realized;}
	public String getTotal_dcq() {return total_dcq;}
	public String getTotal_actual_qty() {return total_actual_qty;}
	public String getStorage_collapse_info() {return storage_collapse_info;}
	public String getDerivative_curve_nm() {return derivative_curve_nm;}
}
