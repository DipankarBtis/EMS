package com.etrm.fms.market_risk;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
//CR Date			: 11/08/2023
//Status	  		: Developing
public class DataBean_VariablePricing 
{
	String db_src_file_name="DataBean_VariablePricing.java";
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
	String queryString="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";
	
	UtilBean utilBean = new UtilBean();
	DateUtil utilDate = new DateUtil();
	DB_AllocationUtil utilAlloc = new DB_AllocationUtil();
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf3 = new DecimalFormat("###########0.000");
	NumberFormat nf1 = new DecimalFormat("###########0.0");
	NumberFormat nf0 = new DecimalFormat("###########0");
	
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
	    			if(callFlag.equalsIgnoreCase("VARIABLE_PRICE_CONFIG"))
	    			{
	    				getVariablePriceConfigDtl();
	    				getPhysicalCurveMaster();
	    				getFinancialCurveMaster();
	    			}
	    			else if(callFlag.equalsIgnoreCase("TAQ_CONT_LIST"))
	    			{
	    				getContractListForTAQ();
	    			}
	    			else if(callFlag.equalsIgnoreCase("VARIABLE_TAQ_CONFIG"))
	    			{
	    				getVariableTAQConfigList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("VARIABLE_PRICE_INFO"))		//PB 20250919: for showing the variable pricing details in i button.
	    			{
	    				getVariablePriceInfo();
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
	
	public void getVariablePriceConfigDtl()
	{
		String function_nm="getVariablePriceConfigDtl()";
		try
		{
			counterparty_abbr=utilBean.getCounterpartyABBR(conn,counterparty_cd);
			display_map_id=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, cargo_no);
			String sysdate = utilDate.getSysdate();
			
			queryString="SELECT TO_CHAR(FROM_DT,'DD/MM/YYYY'),TO_CHAR(TO_DT,'DD/MM/YYYY'),RATE,RATE_UNIT,PRICE_TYPE,PHYS_CURVE_NM,"
					+ "SLOPE,CONST,REMARKS,SEQ_NO,CURVE_NM,PRICE_RANGE,TO_CHAR(PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(PRICE_END_DT,'DD/MM/YYYY'),"
					+ "CURVE_LOGIC,FORMULA,PRICE_DECI,FINAL_PRICE_DECI,PREM_DISC_RATE "
					+ "FROM FMS_CONT_PRICE_DTL "
					+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
					+ "AND MAPPING_ID=? "
					+ "ORDER BY FROM_DT";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, contract_type);
			stmt.setString(3, mapping_id);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String st_dt = rset.getString(1)==null?"":rset.getString(1);
				String end_dt = rset.getString(2)==null?"":rset.getString(2);
				
				VSTART_DT.add(st_dt);
				VEND_DT.add(end_dt);
				VPRICE_RATE.add(rset.getString(3)==null?"":rset.getString(3));
				String price_rate_unit=rset.getString(4)==null?"":rset.getString(4);
				VPRICE_RATE_UNIT.add(price_rate_unit);
				VPRICE_RATE_UNIT_NM.add(utilBean.getRateUnitNm(conn,price_rate_unit));
				String price_type = rset.getString(5)==null?"":rset.getString(5);
				VPRICE_TYPE.add(price_type);
				VPHYSICAL_CURVE.add(rset.getString(6)==null?"":rset.getString(6));
				VREMARK.add(rset.getString(9)==null?"":rset.getString(9));
				String seq_no=rset.getString(10)==null?"":rset.getString(10);
				VSEQ_NO.add(rset.getString(10)==null?"":rset.getString(10));
				String price_range = rset.getString(12)==null?"A":rset.getString(12);
				String price_start_dt = rset.getString(13)==null?"":rset.getString(13);
				String price_end_dt = rset.getString(14)==null?"":rset.getString(14);
				
				VPRICE_START_DT.add(price_start_dt);
				VPRICE_END_DT.add(price_end_dt);
				
				int isExpired = utilDate.getDays(end_dt, sysdate);
				
				if(isExpired<0)
				{
					VIS_RADIO_ENABLE.add("N");
				}
				else
				{
					VIS_RADIO_ENABLE.add("Y");
				}
				
				String formula=rset.getString(16)==null?"":rset.getString(16);
				String curve_logic=rset.getString(15)==null?"":rset.getString(15);
				VCURVE_LOGIC.add(curve_logic);
				VFORMULA.add(formula);
				
				String price_deci=rset.getString(17)==null?"":rset.getString(17);
				String final_price_deci=rset.getString(18)==null?"":rset.getString(18);
				String prem_disc_rate=rset.getString(19)==null?"":rset.getString(19);
				//VPRICE_DECI.add(price_deci);
				//VFINAL_PRICE_DECI.add(final_price_deci);
				//VPREM_DISC_RATE.add(prem_disc_rate);
				
				if(curve_logic.equals("MIN") || curve_logic.equals("MAX") || curve_logic.equals("AVG") || curve_logic.equals("MIN_ADV"))
				{
					String slop_1="",cont_1="",cur_nm_1="",priceRange_1="",priceRangeNm_1="",date_1="",formula_1="",price_deci_1="",prem_disc_rate_1="";
					String slop="",cont="",cur_nm="",priceRange="",priceRangeNm="",priceStDt="",priceEndDt="",temp_formula="",temp_price_deci="",temp_prem_disc_rate="";
					queryString1="SELECT CURVE_NM,SLOPE,CONST,PRICE_RANGE,TO_CHAR(PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(PRICE_END_DT,'DD/MM/YYYY'),FORMULA,"
							+ "PRICE_DECI, PREM_DISC_RATE "
							+ "FROM FMS_CONT_PRICE_MIN_DTL "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND MAPPING_ID=? AND SEQ_NO=?";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, contract_type);
					stmt1.setString(3, mapping_id);
					stmt1.setString(4, seq_no);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						cur_nm = rset1.getString(1)==null?"":rset1.getString(1);
						slop = rset1.getString(2)==null?"":""+rset1.getDouble(2);
						cont = rset1.getString(3)==null?"":""+rset1.getDouble(3);
						priceRange=rset1.getString(4)==null?"A":rset1.getString(4);
						priceStDt=rset1.getString(5)==null?"":rset1.getString(5); 
						priceEndDt=rset1.getString(6)==null?"":rset1.getString(6);
						priceRangeNm=getPriceRangeNm(priceRange,priceStDt,priceEndDt);
						temp_formula=rset1.getString(7)==null?"":rset1.getString(7);
						temp_price_deci=rset1.getString(8)==null?"":rset1.getString(8);
						temp_prem_disc_rate=rset1.getString(9)==null?"":rset1.getString(9);
						
						if(slop_1.equals(""))
						{
							slop_1=slop;
						}
						else
						{
							slop_1+="<br>"+slop;
						}
						if(cont_1.equals(""))
						{
							cont_1=cont;
						}
						else
						{
							cont_1+="<br>"+cont;
						}
						if(cur_nm_1.equals(""))
						{
							cur_nm_1 = cur_nm;
						}
						else
						{
							cur_nm_1 += "<br>"+cur_nm;
						}
						if(priceRange_1.equals(""))
						{
							priceRange_1=priceRange;
						}
						else
						{
							priceRange_1+="<br>"+priceRange;
						}
						if(priceRangeNm_1.equals(""))
						{
							priceRangeNm_1=priceRangeNm;
						}
						else
						{
							priceRangeNm_1+="<br>"+priceRangeNm;
						}
						if(date_1.equals(""))
						{
							date_1=priceStDt+"-"+priceEndDt;
						}
						else
						{
							date_1+="<br>"+priceStDt+"-"+priceEndDt;
						}
						
						if(formula_1.equals(""))
						{
							formula_1=temp_formula.equals("")?" ":temp_formula;
						}
						else
						{
							formula_1+="<br>"+(temp_formula.equals("")?" ":temp_formula);
						}
						if(price_deci_1.equals(""))
						{
							price_deci_1=temp_price_deci;
						}
						else
						{
							price_deci_1+="<br>"+temp_price_deci;
						}
						if(prem_disc_rate_1.equals(""))
						{
							prem_disc_rate_1=temp_prem_disc_rate;
						}
						else
						{
							prem_disc_rate_1+="<br>"+temp_prem_disc_rate;
						}
					}
					rset1.close();
					stmt1.close();
					
					VSLOPE.add(slop_1);
					VCONSTANT.add(cont_1);
					VCURVE_NM.add(cur_nm_1);
					VPRICE_RANGE.add(priceRange_1);
					VPRICE_RANGE_NM.add(priceRangeNm_1);
					VMIN_PRICE_ST_END_DT.add(date_1);
					VMIN_FORMULA.add(formula_1);
					
					VPRICE_DECI.add(price_deci_1);
					VFINAL_PRICE_DECI.add(final_price_deci);
					VPREM_DISC_RATE.add(prem_disc_rate_1);
				}
				else
				{
					VSLOPE.add(rset.getString(7)==null?"1":rset.getDouble(7));
					VCONSTANT.add(rset.getString(8)==null?"0":rset.getDouble(8));
					VCURVE_NM.add(rset.getString(11)==null?"":rset.getString(11));
					VPRICE_RANGE.add(price_range);
					if(price_type.equals("M"))
					{
						VPRICE_RANGE_NM.add(getPriceRangeNm(price_range,price_start_dt,price_end_dt));
					}
					else
					{
						VPRICE_RANGE_NM.add("");
					}
					VMIN_PRICE_ST_END_DT.add("");
					VMIN_FORMULA.add("");
					VPRICE_DECI.add(price_deci);
					VFINAL_PRICE_DECI.add(final_price_deci);
					VPREM_DISC_RATE.add(prem_disc_rate);
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
	
	public void getPhysicalCurveMaster()
	{
		String function_nm="getPhysicalCurveMaster()";
		try
		{
			queryString="SELECT DISTINCT CURVE_NM "
					+ "FROM FMS_FORWARD_PRICE_DTL "
					+ "WHERE UPPER(PHYS_FIN)=?";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, "PHYSICAL");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VPHYSICAL_CURVE_MST.add(rset.getString(1)==null?"":rset.getString(1));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getFinancialCurveMaster()
	{
		String function_nm="getFinancialCurveMaster()";
		try
		{
			queryString="SELECT DISTINCT CURVE_NM "
					+ "FROM FMS_FORWARD_PRICE_DTL "
					+ "WHERE UPPER(PHYS_FIN)=? "
					+ "ORDER BY CURVE_NM DESC ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, "FINANCIAL");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VFINANCIAL_CURVE_MST.add(rset.getString(1)==null?"":rset.getString(1));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public String getPriceRangeNm(String price_range, String price_st_dt, String price_end_dt)
	{
		String function_nm="getPriceRangeNm()";
		String nm="";
		try
		{
			if(price_range.equals("A"))
			{
				nm="Avg.";
			}
			else if(price_range.equals("F"))
			{
				nm="Final Settle Date";
			}
			if(price_range.startsWith("O"))
			{
				nm="Other Avg.("+price_range.substring(1,price_range.length())+")";
			}
			if(price_range.equals("D"))
			{
				nm="Avg. Settle Date("+price_st_dt+" - "+price_end_dt+")";
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return nm;
	}
	
	public void getContractListForTAQ()
	{
		String function_nm="getContractListForTAQ()";
		try
		{
			int st_count=0;
			String sysdate=utilDate.getSysdate();
			
			queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONT_REF_NO,TRADE_REF_NO,CONTRACT_TYPE,"
					+ "TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),AGMT_BASE,"
					+ "'Sell',TCQ,DCQ,START_DT,NULL "
					+ "FROM FMS_SUPPLY_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND CONT_STATUS NOT IN ('C','X') "
					+ "AND START_DT <= TO_DATE(?,'DD/MM/YYYY') AND END_DT >= TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			queryString+="UNION ALL ";
			queryString+="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONT_REF_NO,TRADE_REF_NO,CONTRACT_TYPE,"
					+ "TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),AGMT_BASE,"
					+ "'Buy',TCQ,DCQ,START_DT,NULL "
					+ "FROM FMS_TRADER_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND CONT_STATUS NOT IN ('C','X') "
					+ "AND START_DT <= TO_DATE(?,'DD/MM/YYYY') AND END_DT >= TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			queryString+="ORDER BY START_DT DESC ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++st_count, comp_cd);
			stmt.setString(++st_count, to_dt);
			stmt.setString(++st_count, from_dt);
			stmt.setString(++st_count, comp_cd);
			stmt.setString(++st_count, to_dt);
			stmt.setString(++st_count, from_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String buy_sell=rset.getString(14)==null?"":rset.getString(14);
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
				
				String agmt_base = rset.getString(13)==null?"":rset.getString(13);
				
				double tcq =rset.getDouble(15);
				String dcq = rset.getString(16)==null?"":nf.format(rset.getDouble(16));
				
				if(cont_type.equals("X") || cont_type.equals("I")) //X for sell side //I for Buy side
				{
					cont_ref=trade_ref;
				}
				
				VCOUNTERPARTY_CD.add(countpty_cd);
				VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				
				String dealNo=utilBean.NewDealMappingId(companyCd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, cargo_no);
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
				
				VCONT_REF_NO.add(cont_ref);
				VDIS_CONTRACT_TYPE.add(utilBean.getContractTypeName(cont_type));
				
				VSIGNING_DT.add(rset.getString(10)==null?"":rset.getString(10));
				VSTART_DT.add(rset.getString(11)==null?"":rset.getString(11));
				VEND_DT.add(rset.getString(12)==null?"":rset.getString(12));
				
				double suppliedQty = 0;
				if(buy_sell.equals("Sell"))
				{
					suppliedQty = Double.parseDouble(""+utilAlloc.getSupplyAllocationQty(conn, companyCd, countpty_cd, agmt, cont, cont_type,"0"));
					
					String var_dcq=utilBean.getContVariableDCQ(conn,companyCd, countpty_cd, agmt, cont, cont_type, report_dt);
					if(!var_dcq.equals(""))
					{
						dcq=var_dcq;
					}
				}
				else
				{
					suppliedQty = Double.parseDouble(""+utilAlloc.getPurchaseAllocationQty(conn, companyCd, countpty_cd, agmt, cont, cont_type,"0"));
				}
				double balanceQty=tcq-suppliedQty;
				
				VTCQ.add(nf.format(tcq));
				VDCQ.add(nf.format(Double.parseDouble(dcq)));
				VSUPPLIED_QTY_MMBTU.add(nf.format(suppliedQty));
				VBALANCE_QTY_MMBTU.add(nf.format(balanceQty));
				
				// Get TAQ Detail			
				double total_assessed_tcq=0;
				String taq_remark="";
				String taq_detail="";
				String entry_done="N";
				int tcq_seq=0;
				queryString1="SELECT ASED_TCQ,REMARK,TO_CHAR(FROM_DT,'DD/MM/YYYY'),TO_CHAR(TO_DT,'DD/MM/YYYY'),ASED_DCQ "
						+ "FROM FMS_MR_CONT_TAQ_DTL "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
						+ "ORDER BY FROM_DT";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, companyCd);
				stmt1.setString(2, countpty_cd);
				stmt1.setString(3, agmt);
				stmt1.setString(4, cont);
				stmt1.setString(5, cont_type);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					tcq_seq++;
					entry_done="Y";
					double assed_tcq=rset1.getDouble(1);
					double assed_dcq=rset1.getDouble(5);
					
					String temp_rmk = rset1.getString(2)==null?"":rset1.getString(2);
					
					String temp_frm_dt = rset1.getString(3)==null?"":rset1.getString(3);
					String temp_to_dt = rset1.getString(4)==null?"":rset1.getString(4);
					
					int isExp = utilDate.getDays(temp_to_dt, sysdate);
					int frmDtExp=utilDate.getDays(temp_frm_dt, sysdate);
					
					if(isExp > 0 && frmDtExp < 1)
					{
						int temp_days=utilDate.getDays(temp_to_dt, sysdate);
						total_assessed_tcq+=(assed_dcq*temp_days);
					}
					else if(isExp > 0 && frmDtExp >= 1)
					{
						total_assessed_tcq+=assed_tcq;
					}
						
					if(taq_remark.equals(""))
					{
						taq_remark+=tcq_seq+") "+temp_rmk;
					}
					else
					{
						taq_remark+="<br>"+tcq_seq+") "+temp_rmk;
					}
					
					if(taq_detail.equals(""))
					{
						taq_detail+=tcq_seq+") "+nf.format(assed_tcq)+" MMBTU : "+temp_frm_dt+"-"+temp_to_dt;
					}
					else
					{
						taq_detail+="<br>"+tcq_seq+") "+nf.format(assed_tcq)+" MMBTU : "+temp_frm_dt+"-"+temp_to_dt;
					}
					
				}
				rset1.close();
				stmt1.close();
				
				String ent_by="";
				queryString1="SELECT TO_CHAR(DT,'DD/MM/YYYY HH24:MI:SS'), EMP_CD "
						+ "FROM "
						+ "(SELECT ENT_DT DT,(ENT_BY) EMP_CD "
						+ "FROM FMS_MR_CONT_TAQ_DTL A WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
						+ "AND ENT_DT = (SELECT MAX(ENT_DT)  FROM FMS_MR_CONT_TAQ_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "UNION "
						+ "SELECT MODIFY_DT DT,(MODIFY_BY) EMP_CD "
						+ "FROM FMS_MR_CONT_TAQ_DTL A WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND MODIFY_DT IS NOT NULL "
						+ "AND MODIFY_DT = (SELECT MAX(MODIFY_DT)  FROM FMS_MR_CONT_TAQ_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)) "
						+ "WHERE DT=(SELECT MAX(DT) FROM (SELECT ENT_DT DT "
						+ "FROM FMS_MR_CONT_TAQ_DTL A WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
						+ "AND ENT_DT = (SELECT MAX(ENT_DT)  FROM FMS_MR_CONT_TAQ_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "UNION "
						+ "SELECT MODIFY_DT DT "
						+ "FROM FMS_MR_CONT_TAQ_DTL A WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? AND MODIFY_DT IS NOT NULL "
						+ "AND MODIFY_DT = (SELECT MAX(MODIFY_DT)  FROM FMS_MR_CONT_TAQ_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)))";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, companyCd);
				stmt1.setString(2, countpty_cd);
				stmt1.setString(3, agmt);
				stmt1.setString(4, cont);
				stmt1.setString(5, cont_type);
				stmt1.setString(6, companyCd);
				stmt1.setString(7, countpty_cd);
				stmt1.setString(8, agmt);
				stmt1.setString(9, cont);
				stmt1.setString(10, cont_type);
				stmt1.setString(11, companyCd);
				stmt1.setString(12, countpty_cd);
				stmt1.setString(13, agmt);
				stmt1.setString(14, cont);
				stmt1.setString(15, cont_type);
				stmt1.setString(16, companyCd);
				stmt1.setString(17, countpty_cd);
				stmt1.setString(18, agmt);
				stmt1.setString(19, cont);
				stmt1.setString(20, cont_type);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					String ent_dt= rset1.getString(1)==null?"":rset1.getString(1);
					ent_by=utilBean.getEmpName(conn,rset1.getString(2)==null?"":rset1.getString(2))+"<br>"+ent_dt;
				}
				rset1.close();
				stmt1.close();
				
				VENTERED_BY.add(ent_by);
				
				VTAQ_CONFIGURED.add(entry_done);
				
				total_assessed_tcq+=suppliedQty; // Include supplied MMBTU
				VASSESSED_QTY_MMBTU.add(nf.format(total_assessed_tcq));
				
				VTAQ_REMARK.add(taq_remark);
				VTAQ_DETAIL.add(taq_detail);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getVariableTAQConfigList()
	{
		String function_nm="getVariableTAQConfigList()";
		
		String sysdate = utilDate.getSysdate(); //JD
		try
		{
			counterparty_abbr=utilBean.getCounterpartyABBR(conn,counterparty_cd);
			display_map_id=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			
			queryString="SELECT TO_CHAR(FROM_DT,'DD/MM/YYYY'),TO_CHAR(TO_DT,'DD/MM/YYYY'),SEQ_NO,ASED_TCQ,ASED_DCQ,REMARK,"
					+ "TO_CHAR(ENT_DT,'DD/MM/YYYY HH24:MI:SS'),ENT_BY,TO_CHAR(MODIFY_DT,'DD/MM/YYYY HH24:MI:SS'),MODIFY_BY "
					+ "FROM FMS_MR_CONT_TAQ_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "ORDER BY FROM_DT";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, agmt_no);
			stmt.setString(4, cont_no);
			stmt.setString(5, contract_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String st_dt = rset.getString(1)==null?"":rset.getString(1);
				String end_dt = rset.getString(2)==null?"":rset.getString(2);
				String seq_no=rset.getString(3)==null?"":rset.getString(3);
				
				VSTART_DT.add(st_dt);
				VEND_DT.add(end_dt);
				VSEQ_NO.add(seq_no);
				
				int isExpired = utilDate.getDays(end_dt, sysdate);	//JD			
				if(isExpired<=0)
				{
					VIS_RADIO_ENABLE.add("N");
				}
				else
				{
					VIS_RADIO_ENABLE.add("Y");
				}
				
				VTCQ.add(rset.getString(4)==null?"":nf.format(rset.getDouble(4)));
				VDCQ.add(rset.getString(5)==null?"":nf.format(rset.getDouble(5)));
				VREMARK.add(rset.getString(6)==null?"":rset.getString(6));
				
				String ent_dt= rset.getString(7)==null?"":rset.getString(7);
				String ent_by=utilBean.getEmpName(conn,rset.getString(8)==null?"":rset.getString(8))+"<br>"+ent_dt;
				String modi_dt= rset.getString(9)==null?"":rset.getString(9);
				String modi_by=utilBean.getEmpName(conn,rset.getString(10)==null?"":rset.getString(10))+"<br>"+modi_dt;
				
				VENTERED_BY.add(ent_by);
				VMODIFIED_BY.add(modi_by);
			}
			rset.close();
			stmt.close();	
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//PB 20250919 for variable pricing info
	public void getVariablePriceInfo()
	{
		String function_nm="getVariablePriceInfo()";
		try
		{
			counterparty_abbr=utilBean.getCounterpartyABBR(conn,counterparty_cd);
			display_map_id=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, cargo_no);
			String sysdate=utilDate.getSysdate();
			
			//FOR CHECKING WHETHER CONTRACT HAVE VARIABLE PRICING CONFIGURED
			queryString="SELECT PRICE_TYPE FROM FMS_CONT_PRICE_DTL A "
					+ "WHERE COMPANY_CD=? AND MAPPING_ID=? "
					+ "AND CONTRACT_TYPE=? AND EXISTS(SELECT B.PRICE_TYPE "
					+ "FROM FMS_CONT_PRICE_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.MAPPING_ID=B.MAPPING_ID AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ "AND B.PRICE_TYPE=?) ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1,comp_cd);
			stmt.setString(2, mapping_id);
			stmt.setString(3, contract_type);
			stmt.setString(4, "M");		// M is for Floating pricing type
			rset=stmt.executeQuery();
			if(rset.next())
			{
				//for fetching the months
				queryString1="SELECT DISTINCT TO_CHAR(LAST_DAY(TO_DATE(TD.END_DATE + 1 - ROWNUM)),'DD/MM/YYYY') MONTH_DATE "
						+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,CASE WHEN  TO_DATE(?,'DD/MM/YYYY')<TO_DATE(?,'DD/MM/YYYY') THEN TO_DATE(?,'DD/MM/YYYY') "
						+ "ELSE TO_DATE(?,'DD/MM/YYYY') END END_DATE FROM DUAL) TD "
						+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
						+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, start_dt);
				stmt1.setString(2, end_dt);
				stmt1.setString(3, sysdate);
				stmt1.setString(4, end_dt);
				stmt1.setString(5, sysdate);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					String gas_dt = rset1.getString(1)==null?"":rset1.getString(1);
					String cont_gas_dt="01/"+gas_dt.substring(3, gas_dt.length());
					if(utilDate.getDays(sysdate, gas_dt)<=1)		//if system date < gas date then considering system date 
					{
						gas_dt=sysdate;
					}
					
					int phyCountRu=utilDate.getDays(gas_dt, sysdate);
					String phyRu="";
					if(phyCountRu<=1)
					{
						phyRu="R";
					}
					else
					{
						phyRu="U";
					}
					
					//FOR FETCHING THE PRICING DETAILS
					queryString2="SELECT RATE,RATE_UNIT,PRICE_TYPE,CURVE_NM,SLOPE,CONST,PHYS_CURVE_NM, "
							+ "PRICE_RANGE,TO_CHAR(PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(PRICE_END_DT,'DD/MM/YYYY'), "
							+ "CURVE_LOGIC,SEQ_NO,FORMULA,TO_CHAR(FROM_DT,'DD/MM/YYYY'), TO_CHAR(TO_DT,'DD/MM/YYYY'), "
							+ "(SELECT COUNT(PRICE_TYPE) FROM FMS_CONT_PRICE_DTL WHERE COMPANY_CD=? AND MAPPING_ID=? AND CONTRACT_TYPE=? "
							+ "AND FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND TO_DT>=TO_DATE(?,'DD/MM/YYYY')) TOTAL_ROWS, PRICE_DECI,FINAL_PRICE_DECI,PREM_DISC_RATE "
							+ "FROM FMS_CONT_PRICE_DTL "
							+ "WHERE COMPANY_CD=? AND MAPPING_ID=? AND CONTRACT_TYPE=? "
							+ "AND FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND TO_DT>=TO_DATE(?,'DD/MM/YYYY') ";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, mapping_id);
					stmt2.setString(3, contract_type);
					stmt2.setString(4, gas_dt);
					stmt2.setString(5, cont_gas_dt);
					stmt2.setString(6, comp_cd);
					stmt2.setString(7, mapping_id);
					stmt2.setString(8, contract_type);
					stmt2.setString(9, gas_dt);
					stmt2.setString(10, cont_gas_dt);
					rset2=stmt2.executeQuery();
					while(rset2.next())
					{
						double variable_rate=rset2.getDouble(1);
						String variable_rate_unit=rset2.getString(2)==null?"":rset2.getString(2);
						String price_type=rset2.getString(3)==null?"":rset2.getString(3);
						String curve_nm=rset2.getString(4)==null?"":rset2.getString(4);
						String slope=rset2.getString(5)==null?"":rset2.getString(5);
						String constant=rset2.getString(6)==null?"":rset2.getString(6);
						String phys_curve_nm=rset2.getString(7)==null?"":rset2.getString(7);
						String price_range=rset2.getString(8)==null?"":rset2.getString(8);
						String price_start_dt=rset2.getString(9)==null?"":rset2.getString(9);
						String price_end_dt=rset2.getString(10)==null?"":rset2.getString(10);
						String curve_logic=rset2.getString(11)==null?"":rset2.getString(11);
						String seq_no=rset2.getString(12)==null?"":rset2.getString(12);
						String formula=rset2.getString(13)==null?"":rset2.getString(13);
						String price_from_dt=rset2.getString(14)==null?"":rset2.getString(14);
						String price_to_dt=rset2.getString(15)==null?"":rset2.getString(15);
						int count_row=rset2.getInt(16);
						String price_deci=rset2.getString(17)==null?"4":rset2.getString(17);
						String final_price_deci=rset2.getString(18)==null?"4":rset2.getString(18);
						double prem_disc=rset2.getDouble(19);
						
						if(count_row>1 || utilDate.getDays(price_to_dt, gas_dt)<=1)			//IF MULTIPLE CURVE ARE CONFIGURED FOR THE SINGLE MONTH
						{
							gas_dt=price_to_dt;
						}
						
						String delvh_month=gas_dt.substring(3,gas_dt.length());
						
						Vector temp = new Vector();
						String settle_start_dt="";
						String settle_end_dt="";
						String fin_cont_month="";
						String disp_curve_nm="";
						
						int count_u=0;
						int count_r=0;
						double eff_rate=0;
						double settle_price=0;
						double fin_fwd_price=0;
						double avgSettlePrice=0;
						double avgPrice=0;
						
						// For i-button
						String disp_eff_rate="";
						String disp_decimal_eff_rate="";
						
						String display_price_type="";
						
						if(price_type.equals("M"))
						{
							if(curve_logic.equals("SINGLE"))
							{
								temp=SettlementDateCalculation(curve_nm, cont_gas_dt, price_range, price_start_dt, price_end_dt);
								settle_start_dt = ""+temp.elementAt(1);
								settle_end_dt = ""+temp.elementAt(2);
								fin_cont_month=""+temp.elementAt(4);
								
								int fin_count_ru=utilDate.getDays(settle_end_dt, sysdate);
								if(fin_count_ru>1)
								{	
									int isSettlmentPeriodActive=utilDate.isDateWithinPeriod(settle_start_dt,settle_end_dt,sysdate);
									
									if(isSettlmentPeriodActive==1)
									{
										String nextdt=utilDate.getDate(sysdate, "1"); 
										count_u=getPriceCurveWorkingDays(curve_nm, nextdt, settle_end_dt);
										count_r=getPriceCurveWorkingDays(curve_nm, settle_start_dt, sysdate); 	
									}
									else
									{
										count_u=getPriceCurveWorkingDays(curve_nm, settle_start_dt, settle_end_dt);
										count_r=0;
									}
								}
								else
								{
									count_r=getPriceCurveWorkingDays(curve_nm, settle_start_dt, settle_end_dt);
								}
								
								if(curve_nm.equals("PPAC"))
								{
									settle_price=getForwardPrice(curve_nm, sysdate, fin_cont_month,"Forward","Financial");	
								}
								else
								{
									settle_price=getSettledPrice(curve_nm, settle_start_dt, settle_end_dt);
								}
								fin_fwd_price=getForwardPrice(curve_nm, sysdate, fin_cont_month,"Forward","Financial");
								
								//ENH: Need to convert settled price or forward price as per decimal specified in Price Upto (decimal) field. 
								double temp_settle_price=Double.parseDouble(getNumberFormat(settle_price, price_deci));
								double temp_fin_fwd_price=Double.parseDouble(getNumberFormat(fin_fwd_price, price_deci));
								
								eff_rate=((((count_u * temp_fin_fwd_price) + (count_r * temp_settle_price))/(count_r+count_u)) * Double.parseDouble(slope)) + Double.parseDouble(constant);
								if(Double.isNaN(eff_rate))
								{
									eff_rate=0;
								}
																
																
								// ENH 2510071: Effective_rate = {Settled/fwd price (price upto decimal rounded) + (prem/discount with sign)} * slope + const
								// Adjusting to achive above output : eff_rate += premium(+) or discount(-) * Slope
								eff_rate+=(prem_disc  * Double.parseDouble(slope)) ;
								
								// for i-button display
								disp_eff_rate=""+eff_rate;
								
								//ENH: eff_rate as per decimal specified in (Final Price Upto (Decimal))
								eff_rate=Double.parseDouble(getNumberFormat(eff_rate, final_price_deci));
								
								// for i-button display
								disp_decimal_eff_rate=""+getNumberFormat(eff_rate, final_price_deci);
								//display_price_type="Float (Single)";
								display_price_type="Float";
								disp_curve_nm=curve_nm;
							}
							else if(curve_logic.equals("MIN") || curve_logic.equals("MAX") || curve_logic.equals("AVG") || curve_logic.equals("MIN_ADV"))
							{
								Vector VTEMP_PHY_RU = new Vector();
								Vector VTEMP_FIN_RU = new Vector();
								Vector VTEMP_EFF_PRICE = new Vector();
								Vector VTEMP_SETTLE_PRICE = new Vector();
								Vector VTEMP_CURVE_NM = new Vector();
								
								int counter=0;
								
								queryString3="SELECT CURVE_NM,SLOPE,CONST,"
										+ "PRICE_RANGE,TO_CHAR(PRICE_START_DT,'DD/MM/YYYY'),TO_CHAR(PRICE_END_DT,'DD/MM/YYYY'),FORMULA, "
										+ "PRICE_DECI,PREM_DISC_RATE "
										+ "FROM FMS_CONT_PRICE_MIN_DTL "
										+ "WHERE COMPANY_CD=? AND MAPPING_ID=? AND CONTRACT_TYPE=? "
										+ "AND SEQ_NO=?";
								stmt3=conn.prepareStatement(queryString3);
								stmt3.setString(1, comp_cd);
								stmt3.setString(2, mapping_id);
								stmt3.setString(3, contract_type);
								stmt3.setString(4, seq_no);
								rset3=stmt3.executeQuery();
								while(rset3.next())
								{
									String min_curve_nm=rset3.getString(1)==null?"":rset3.getString(1);
									String min_slope=rset3.getString(2)==null?"1":rset3.getString(2);
									String min_constant=rset3.getString(3)==null?"0":rset3.getString(3);
									String min_price_range=rset3.getString(4)==null?"A":rset3.getString(4);
									String min_price_start_dt=rset3.getString(5)==null?"":rset3.getString(5);
									String min_price_end_dt=rset3.getString(6)==null?"":rset3.getString(6);
									String min_formula=rset3.getString(7)==null?"":rset3.getString(7);
									String min_price_deci=rset3.getString(8)==null?"":rset3.getString(8);
									double min_prem_disc=rset3.getDouble(9);
									
									
									if(!min_formula.equals(""))
									{
										String min_formula_split[]=min_formula.split("@");
										String m0=min_formula_split[1];
										String m1=min_formula_split[2];
										String m2=min_formula_split[3];
										
										int settle_count_withoutzero=0;
										double totalPrice=0;
										double totalSettlePrice=0;
										
										int count = Integer.parseInt(m1)+(Integer.parseInt(m2)-Integer.parseInt(m1))+1;
										int multiply_factor=-1;
										if(m0.equals("Forward"))
										{
											multiply_factor=1;
										}
										
										int i_index=0;
										int first_index_of_minAdv=0;
										for(int i=Integer.parseInt(m1);i<=Integer.parseInt(m2);i++)
										{
											counter++;
											i_index++;
											if(i_index==1) {first_index_of_minAdv=counter;}
											int temp_i=i*multiply_factor;
											String multi_cont_month="";
											String queryString2="SELECT TO_CHAR(ADD_MONTHS(TO_DATE(?,'DD/MM/YYYY'),?),'MM/YYYY') "
													+ "FROM DUAL";
											stmt4=conn.prepareStatement(queryString2);
											stmt4.setString(1, gas_dt);
											stmt4.setInt(2, temp_i);
											rset4=stmt4.executeQuery();
											if(rset4.next())
											{
												multi_cont_month="01/"+(rset4.getString(1)==null?"":rset4.getString(1));
											}
											rset4.close();
											stmt4.close();
											
											temp=SettlementDateCalculation(min_curve_nm, multi_cont_month, min_price_range, min_price_start_dt, min_price_end_dt);
											settle_start_dt = ""+temp.elementAt(1);
											settle_end_dt = ""+temp.elementAt(2);
											fin_cont_month=""+temp.elementAt(4);
											
											int count_ru=utilDate.getDays(""+temp.elementAt(2), sysdate);
											String ru="R";
											if(count_ru>1)
											{
												ru="U";
											}
											
											VTEMP_FIN_RU.add(ru);
											VTEMP_PHY_RU.add(phyRu);
											
											if(count_ru>1)
											{	
												int isSettlmentPeriodActive=utilDate.isDateWithinPeriod(settle_start_dt,settle_end_dt,sysdate);
												
												if(isSettlmentPeriodActive==1)
												{
													String nextdt=utilDate.getDate(gas_dt, "1"); 
													count_u=getPriceCurveWorkingDays(min_curve_nm, nextdt, settle_end_dt);
													count_r=getPriceCurveWorkingDays(min_curve_nm, settle_start_dt, sysdate); 	
												}
												else
												{
													count_u=getPriceCurveWorkingDays(min_curve_nm, settle_start_dt, settle_end_dt);
													count_r=0;
												}
											}
											else
											{
												count_r=getPriceCurveWorkingDays(min_curve_nm, settle_start_dt, settle_end_dt);
											}
											
											if(min_curve_nm.equals("PPAC"))
											{
												settle_price=getForwardPrice(min_curve_nm, sysdate, fin_cont_month,"Forward","Financial");
											}
											else
											{
												settle_price=getSettledPrice(min_curve_nm, settle_start_dt, settle_end_dt);
											}
											fin_fwd_price=getForwardPrice(min_curve_nm, sysdate, fin_cont_month,"Forward","Financial");
											
											//ENH: Need to convert settled price or forward price as per decimal specified in Price Upto (decimal) field. 
											double temp_settle_price=Double.parseDouble(getNumberFormat(settle_price, min_price_deci));
											double temp_fin_fwd_price=Double.parseDouble(getNumberFormat(fin_fwd_price, min_price_deci));
											
											eff_rate=(((count_u * temp_fin_fwd_price) + (count_r * temp_settle_price))/(count_r+count_u));
											if(Double.isNaN(eff_rate))
											{
												eff_rate=0;
											}
											
											if(eff_rate>0)
											{
												totalSettlePrice+=eff_rate;
												settle_count_withoutzero+=1;
											}
											VTEMP_EFF_PRICE.add("");
											VTEMP_SETTLE_PRICE.add(settle_price);
											VTEMP_CURVE_NM.add(min_curve_nm);
										}
										if(settle_count_withoutzero>0) {
											avgSettlePrice=totalSettlePrice/settle_count_withoutzero; 
										}
										avgPrice = avgSettlePrice * Double.parseDouble(min_slope) + Double.parseDouble(min_constant);
										
										// ENH 2510071: Effective_rate = {Settled/fwd price (price upto decimal rounded) + (prem/discount with sign)} * slope + const
										// Adjusting to achive above output : eff_rate += premium(+) or discount(-) * Slope
										avgPrice+=(min_prem_disc  * Double.parseDouble(min_slope)) ;
										
										if(first_index_of_minAdv > 0)
										{
											VTEMP_EFF_PRICE.remove(first_index_of_minAdv-1);
											VTEMP_EFF_PRICE.add(first_index_of_minAdv-1, nf2.format(avgPrice));
										}
									}
									else 
									{
										temp=SettlementDateCalculation(min_curve_nm, cont_gas_dt, min_price_range, min_price_start_dt, min_price_end_dt);
										settle_start_dt = ""+temp.elementAt(1);
										settle_end_dt = ""+temp.elementAt(2);
										fin_cont_month=""+temp.elementAt(4);
										
										if(min_curve_nm.equals("PPAC"))
										{
											settle_price=getForwardPrice(min_curve_nm, sysdate, ""+temp.elementAt(4),"Forward","Financial");
										}
										else
										{
											settle_price=getSettledPrice(min_curve_nm, ""+temp.elementAt(1), ""+temp.elementAt(2));
										}
										
										fin_fwd_price=getForwardPrice(min_curve_nm, sysdate, ""+temp.elementAt(4),"Forward","Financial");
										
										int fin_count_ru=utilDate.getDays(""+temp.elementAt(2), sysdate);
										
										count_u=0;
										count_r=0;
										
										if(fin_count_ru>1)
										{	
											int isSettlmentPeriodActive=utilDate.isDateWithinPeriod(""+temp.elementAt(1),""+temp.elementAt(2),sysdate);
											
											if(isSettlmentPeriodActive==1)
											{
												String nextdt=utilDate.getDate(gas_dt, "1"); 
												count_u=getPriceCurveWorkingDays(min_curve_nm, nextdt, ""+temp.elementAt(2));
												count_r=getPriceCurveWorkingDays(min_curve_nm, ""+temp.elementAt(1), sysdate); 	
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
										
										//ENH 2510071: Need to convert settled price or forward price as per decimal specified in Price Upto (decimal) field. 
										double temp_settle_price=Double.parseDouble(getNumberFormat(settle_price, min_price_deci));
										double temp_fin_fwd_price=Double.parseDouble(getNumberFormat(fin_fwd_price, min_price_deci));
										
										double eff_rate1=((((count_u * temp_fin_fwd_price) + (count_r * temp_settle_price))/(count_r+count_u)) * Double.parseDouble(min_slope)) + Double.parseDouble(min_constant);
										if(Double.isNaN(eff_rate1)) 
										{
											eff_rate1=0;
										}
										else
										{
											// ENH 2510071: Effective_rate = {Settled/fwd price (price upto decimal rounded) + (prem/discount with sign)} * slope + const
											// Adjusting to achive above output : eff_rate += premium(+) or discount(-) * Slope
											eff_rate1+=(min_prem_disc  * Double.parseDouble(min_slope)) ;
										}
										
										VTEMP_EFF_PRICE.add(eff_rate1);
										VTEMP_SETTLE_PRICE.add(settle_price);
										VTEMP_CURVE_NM.add(min_curve_nm);
									}
								}
								rset3.close();
								stmt3.close();
								
								if(curve_logic.equals("MIN") || curve_logic.equals("MAX") || curve_logic.equals("AVG") )// && !VTEMP_FIN_RU.contains("U"))
								{
									String temp_curve_nm="";
									double temp_settle_price=0;
									int index=0;
									double price_sum=0;
									double price=0;
									double temp_price = Double.parseDouble(""+VTEMP_EFF_PRICE.elementAt(index));
									for(int i=0;i<VTEMP_EFF_PRICE.size();i++)
									{
										price=Double.parseDouble(""+VTEMP_EFF_PRICE.elementAt(i));
										price_sum+=price;
										temp_settle_price+=Double.parseDouble(""+VTEMP_SETTLE_PRICE.elementAt(i));
										if(curve_logic.equals("MIN"))
										{
											if(temp_price>price)
											{
												temp_price=price;
												index=i;
											}
										}
										else if(curve_logic.equals("MAX"))
										{
											if(temp_price<price)
											{
												temp_price=price;
												index=i;
											}
										}
										if(temp_curve_nm.equals(""))
										{
											temp_curve_nm+=""+VTEMP_CURVE_NM.elementAt(i);
										}
										else
										{
											temp_curve_nm+=","+VTEMP_CURVE_NM.elementAt(i);
										}
									}
									if(curve_logic.equals("AVG"))
									{
										settle_price=temp_settle_price/VTEMP_EFF_PRICE.size();
										temp_price=price_sum/VTEMP_EFF_PRICE.size();
									}
									else
									{
										settle_price=Double.parseDouble(""+VTEMP_SETTLE_PRICE.elementAt(index));
									}
									
									eff_rate=temp_price;
									disp_eff_rate=""+eff_rate;
									
									//ENH 2510071: Now eff_rate as per decimal specified in (Final Price Upto (Decimal))
									eff_rate=Double.parseDouble(getNumberFormat(eff_rate, final_price_deci));
									disp_decimal_eff_rate=getNumberFormat(eff_rate, final_price_deci);
									
									curve_nm=curve_logic.equals("AVG")?temp_curve_nm:""+VTEMP_CURVE_NM.elementAt(index);
									//display_price_type="Float ("+curve_logic+")";
									display_price_type="Float";
									disp_curve_nm=curve_logic.substring(0,1)+curve_logic.substring(1, curve_logic.length()).toLowerCase()+" of ["+formula.substring(4,formula.length()).replaceAll("@", ",")+"]";
								}
								else if(curve_logic.equals("MIN_ADV"))
								{
									int min_index=0;
									double price=Double.parseDouble(""+VTEMP_EFF_PRICE.elementAt(min_index));
									String min_index_curve="";
									for(int i=1;i<VTEMP_EFF_PRICE.size();i++)
									{	
										if(!VTEMP_EFF_PRICE.elementAt(i).equals(""))
										{
											if(Double.parseDouble(""+VTEMP_EFF_PRICE.elementAt(i)) < price)
											{
												price=Double.parseDouble(""+VTEMP_EFF_PRICE.elementAt(i));
												min_index_curve=""+VTEMP_CURVE_NM.elementAt(i);
												min_index=i;
											}
										}
										else
										{
											if(!VTEMP_FIN_RU.contains("U") && !VTEMP_PHY_RU.contains("U") && !VTEMP_CURVE_NM.elementAt(i).equals("PPAC"))
											{
												VTEMP_EFF_PRICE.remove(i);
												VTEMP_EFF_PRICE.add(i, nf2.format(0));
											}
										}
									}
									/*for(int i=0;i<VTEMP_EFF_PRICE.size();i++)
									{
										if(!min_index_curve.equals("PPAC"))
										{
											if(!VTEMP_FIN_RU.contains("U") && !VTEMP_PHY_RU.contains("U"))
											{
											}
											else
											{
												if(VTEMP_CURVE_NM.elementAt(i).equals(min_index_curve))
												{
													VTEMP_EFF_PRICE.remove(i);
													VTEMP_EFF_PRICE.add(i, "");
												}
											}
										}
										else
										{
											if(VTEMP_PHY_RU.contains("U"))
											{
												if(min_index==i)
												{
												}
												else
												{
													VTEMP_EFF_PRICE.remove(i);
													VTEMP_EFF_PRICE.add(i, "");
												}
											}
											else
											{
												if(min_index==i)
												{
												}
												else
												{
													VTEMP_EFF_PRICE.remove(i);
													VTEMP_EFF_PRICE.add(i, "");
												}
											}
										}
									}*/
									
									eff_rate=price;
									disp_eff_rate = ""+eff_rate;
									//ENH: Now eff_rate as per decimal specified in (Final Price Upto (Decimal))
									eff_rate=Double.parseDouble(getNumberFormat(eff_rate, final_price_deci));
									disp_decimal_eff_rate = ""+getNumberFormat(eff_rate, final_price_deci);

									curve_nm=""+VTEMP_CURVE_NM.elementAt(min_index);
									//display_price_type="Float ("+curve_logic+")";
									display_price_type="Float";
									disp_curve_nm="Min of ["+formula.substring(8,formula.length()).replaceAll("@", ",")+"]";
								}
							}
							else
							{
								if(curve_logic.equals("MULTI_LEG") && !formula.equals(""))
								{	
									Vector VTEMP_PHY_RU = new Vector();
									Vector VTEMP_FIN_RU = new Vector();
									Vector VTEMP_SETTLE_PRICE = new Vector();
									Vector VTEMP_SETTLE_START_DT = new Vector();
									Vector VTEMP_SETTLE_END_DT = new Vector();
									
									String split[]=formula.split("@");
									String m0=split[1];
									String m1=split[2];
									String m2=split[3];
									
									int settle_count_withoutzero=0;
									double totalPrice=0;
									double totalSettlePrice=0;
									avgSettlePrice=0;
									avgPrice=0;
									
									int multiply_factor=-1;
									if(m0.equals("Forward"))
									{
										multiply_factor=1;
									}
									
									for(int i=Integer.parseInt(m1);i<=Integer.parseInt(m2);i++)
									{
										int temp_i=i*multiply_factor;
										
										String multi_cont_month="";
										queryString3="SELECT TO_CHAR(ADD_MONTHS(TO_DATE(?,'DD/MM/YYYY'),?),'MM/YYYY') "
												+ "FROM DUAL";
										stmt3=conn.prepareStatement(queryString3);
										stmt3.setString(1, gas_dt);
										stmt3.setInt(2, temp_i);
										rset3=stmt3.executeQuery();
										if(rset3.next())
										{
											multi_cont_month="01/"+(rset3.getString(1)==null?"":rset3.getString(1));
										}
										rset3.close();
										stmt3.close();
										
										temp=SettlementDateCalculation(curve_nm, multi_cont_month, price_range, price_start_dt, price_end_dt);
										settle_start_dt=""+temp.elementAt(1);
										settle_end_dt=""+temp.elementAt(2);
										fin_cont_month=""+temp.elementAt(4);
										
										int count_ru=utilDate.getDays(""+temp.elementAt(2), sysdate);
										
										String ru="R";
										if(count_ru>1)
										{
											ru="U";
										}
										
										VTEMP_FIN_RU.add(ru);
										VTEMP_PHY_RU.add(phyRu);
										
										settle_price=0;
										if(curve_nm.equals("PPAC"))
										{
											settle_price=getForwardPrice(curve_nm, sysdate, ""+temp.elementAt(4),"Forward","Financial");
										}
										else
										{
											settle_price=getSettledPrice(curve_nm, ""+temp.elementAt(1), ""+temp.elementAt(2));
										}
										
										//ENH: Need to convert settled price or forward price as per decimal specified in Price Upto (decimal) field. 
										double temp_settle_price=Double.parseDouble(getNumberFormat(settle_price, price_deci));
										double temp_fin_fwd_price=Double.parseDouble(getNumberFormat(fin_fwd_price, price_deci));
										
										if(settle_price>0)
										{
											totalSettlePrice+=temp_settle_price;
											settle_count_withoutzero+=1;
										}
										
										VTEMP_SETTLE_PRICE.add(temp_settle_price);
										VTEMP_SETTLE_START_DT.add(settle_start_dt);
										VTEMP_SETTLE_END_DT.add(settle_end_dt);
									}
									if(settle_count_withoutzero>0) 
									{
										avgSettlePrice=totalSettlePrice/settle_count_withoutzero;
									}
									if(curve_logic.equals("MULTI_LEG") && !VTEMP_FIN_RU.contains("U") && !VTEMP_PHY_RU.contains("U"))
									{
										avgPrice = avgSettlePrice * Double.parseDouble(slope) + Double.parseDouble(constant);
										eff_rate=avgPrice;
										// ENH 2510071: Effective_rate = {Settled/fwd price (price upto decimal rounded) + (prem/discount with sign)} * slope + const
										// Adjusting to achive above output : eff_rate += premium(+) or discount(-) * Slope
										eff_rate+=(prem_disc * Double.parseDouble(slope));
										settle_price=avgSettlePrice;
									}
									else
									{
										totalSettlePrice=0;
										double totalEffPrice=0;
										for(int i=0;i<VTEMP_SETTLE_PRICE.size();i++)
										{	
											settle_end_dt = ""+VTEMP_SETTLE_END_DT.elementAt(i);
											settle_start_dt=""+VTEMP_SETTLE_START_DT.elementAt(i);
											
											int fin_count_ru=utilDate.getDays(settle_end_dt, sysdate);
											if(fin_count_ru>1)
											{
												int isSettlmentPeriodActive=utilDate.isDateWithinPeriod(settle_start_dt,settle_end_dt,sysdate);
												
												if(isSettlmentPeriodActive==1)
												{
													String nextdt=utilDate.getDate(sysdate, "1"); 
													count_u=getPriceCurveWorkingDays(curve_nm, nextdt, settle_end_dt);
													count_r=getPriceCurveWorkingDays(curve_nm, settle_start_dt, sysdate); 	
												}
												else
												{
													count_u=getPriceCurveWorkingDays(curve_nm, settle_start_dt, settle_end_dt);
													count_r=0;
												}
											}
											else
											{
												count_r=getPriceCurveWorkingDays(curve_nm, settle_start_dt, settle_end_dt);
											}
											
											if(curve_nm.equals("PPAC"))
											{
												settle_price=getForwardPrice(curve_nm, sysdate, fin_cont_month,"Forward","Financial");
											}
											else
											{
												settle_price=getSettledPrice(curve_nm, settle_start_dt, settle_end_dt);
											}
											fin_fwd_price=getForwardPrice(curve_nm, sysdate, fin_cont_month,"Forward","Financial");
											
											//ENH: Need to convert settled price or forward price as per decimal specified in Price Upto (decimal) field. 
											double temp_settle_price=Double.parseDouble(getNumberFormat(settle_price, price_deci));
											double temp_fin_fwd_price=Double.parseDouble(getNumberFormat(fin_fwd_price, price_deci));
											
											double temp_eff_rate=((((count_u * temp_fin_fwd_price) + (count_r * temp_settle_price))/(count_r+count_u)) * Double.parseDouble(slope)) + Double.parseDouble(constant);
											if(Double.isNaN(temp_eff_rate))
											{
												temp_eff_rate=0;
											}
											else
											{
												// ENH 2510071: Effective_rate = {Settled/fwd price (price upto decimal rounded) + (prem/discount with sign)} * slope + const
												// Adjusting to achive above output : eff_rate += premium(+) or discount(-) * Slope
												temp_eff_rate+=(prem_disc * Double.parseDouble(slope)) ;
											}
											totalSettlePrice+=settle_price;
											totalEffPrice+=temp_eff_rate;
										}
										
										settle_price=totalSettlePrice/VTEMP_SETTLE_PRICE.size();
										eff_rate=totalEffPrice/VTEMP_SETTLE_PRICE.size();
									}
									disp_eff_rate = ""+eff_rate;
									//display_price_type="Float ("+curve_logic+")";
									display_price_type="Float";
									String temp1[]=formula.split("@");
									disp_curve_nm=curve_nm+" "+temp1[0]+"("+temp1[1]+","+temp1[2]+","+temp1[3]+")";
									
									
									//ENH: Now eff_rate as per decimal specified in (Final Price Upto (Decimal))
									eff_rate=Double.parseDouble(getNumberFormat(eff_rate, final_price_deci));
									disp_decimal_eff_rate=""+getNumberFormat(eff_rate, final_price_deci);
								}
							}
							VDELVH_MONTH.add(delvh_month);
							VDURATION.add(gas_dt);
							VCURVE_NM.add(disp_curve_nm);
							VSETTLE_PRICE.add(settle_price);
							VPRICE_TYPE.add(display_price_type);
							VEFF_PRICE.add(disp_eff_rate);
							VPREM_DISC_RATE.add(""+prem_disc);
							VFINAL_PRICE.add(disp_decimal_eff_rate);
							VPRICE_DECI.add(price_deci);
							VFINAL_PRICE_DECI.add(final_price_deci);
						}
						else
						{
							display_price_type="Fixed";
							settle_price=0;
							String account="";
							if(contract_type.equals("S")||contract_type.equals("L")||contract_type.equals("X")||contract_type.equals("F")||contract_type.equals("E")||contract_type.equals("W"))
							{
								account="Sell";
							}
							else if(contract_type.equals("D")||contract_type.equals("T")||contract_type.equals("N"))
							{
								account="Buy";
							}
							double exchngRate=getExchangeRate(comp_cd, counterparty_cd, agmt_no, cont_no, contract_type, report_dt,account);
							if(variable_rate_unit.equals("1"))
							{
								if(exchngRate>0)
								{
									variable_rate=variable_rate/exchngRate;
								}
							}
							eff_rate=variable_rate;
							
							VDELVH_MONTH.add(delvh_month);
							VDURATION.add(gas_dt);
							VCURVE_NM.add(display_price_type);
							VSETTLE_PRICE.add(settle_price);
							VPRICE_TYPE.add(display_price_type);
							VEFF_PRICE.add(eff_rate);
							VPREM_DISC_RATE.add(""+prem_disc);
							VFINAL_PRICE.add(disp_decimal_eff_rate);
							VPRICE_DECI.add(price_deci);
							VFINAL_PRICE_DECI.add(final_price_deci);
						}
					}
					rset2.close();
					stmt2.close();
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
			
			if(Double.doubleToRawLongBits(exchangRate)==Double.doubleToRawLongBits(0)) //IF EXCHNG_RATE==0, DEFAULT 'Shell Treasury Rate' WILL BE CONSIDERED
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
	

	public String getNumberFormat(double number, String decimal)
	{
		String function_nm="getNumberFormat()";
		BigDecimal bd = BigDecimal.valueOf(number); 
		
		try 
		{
			int idecimal = decimal.equals("")? 4:Integer.parseInt(decimal);
			bd = bd.setScale(idecimal, RoundingMode.HALF_UP);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}	

		return ""+bd;
	}
	
	
	public String getNumberFormat1(double number, String decimal)
	{
		String function_nm="getNumberFormat()";
		String no=""+number;
		try
		{
			if(decimal.equals("0"))
			{
				no=nf0.format(number);
			}
			else if(decimal.equals("1"))
			{
				no=nf1.format(number);
			}
			else if(decimal.equals("2"))
			{
				no=nf.format(number);
			}
			else if(decimal.equals("3"))
			{
				no=nf3.format(number);
			}
			else if(decimal.equals("4"))
			{
				no=nf2.format(number);
			}
			else
			{
				no=nf2.format(number);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return no;
	}
	
	String callFlag = "";
	public void setCallFlag(String callFlag) {this.callFlag = callFlag;}
	String comp_cd = "";
	public void setComp_cd(String comp_cd) {this.comp_cd = comp_cd;}
	
	String counterparty_cd = "";
	String cont_no = "";
	String cont_rev_no = "";
	String agmt_no = "";
	String agmt_rev_no = "";
	String contract_type = "";
	String cargo_no = "";
	String mapping_id = "";
	String report_dt = "";
	String from_dt = "";
	String to_dt = "";
	
	String start_dt="";		//PB
	String end_dt="";		//PB
	
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setCont_no(String cont_no) {this.cont_no = cont_no;}
	public void setCont_rev_no(String cont_rev_no) {this.cont_rev_no = cont_rev_no;}
	public void setAgmt_no(String agmt_no) {this.agmt_no = agmt_no;}
	public void setAgmt_rev_no(String agmt_rev_no) {this.agmt_rev_no = agmt_rev_no;}
	public void setContract_type(String contract_type) {this.contract_type = contract_type;}
	public void setCargo_no(String cargo_no) {this.cargo_no = cargo_no;}
	public void setMapping_id(String mapping_id) {this.mapping_id = mapping_id;}
	public void setReport_dt(String report_dt) {this.report_dt = report_dt;}
	public void setFrom_dt(String from_dt) {this.from_dt = from_dt;}
	public void setTo_dt(String to_dt) {this.to_dt = to_dt;}
	
	public void setStart_dt(String start_dt) {this.start_dt=start_dt;}		//PB
	public void setEnd_dt(String end_dt) {this.end_dt=end_dt;}				//PB
	
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VCONT_NO = new Vector();
	Vector VCONT_REV_NO = new Vector();
	Vector VAGMT_NO = new Vector();
	Vector VAGMT_REV_NO = new Vector();
	Vector VCONTRACT_TYPE = new Vector();
	Vector VSIGNING_DT = new Vector();
	Vector VSTART_DT = new Vector();
	Vector VEND_DT = new Vector();
	Vector VPRICE_TYPE = new Vector();
	Vector VPRICE_RATE = new Vector();
	Vector VPRICE_RATE_UNIT_NM = new Vector();
	Vector VPRICE_RATE_UNIT = new Vector();
	Vector VPHYSICAL_CURVE = new Vector();
	Vector VSLOPE = new Vector();
	Vector VCONSTANT = new Vector();
	Vector VREMARK = new Vector();
	Vector VSEQ_NO = new Vector();
	Vector VIS_RADIO_ENABLE = new Vector();
	Vector VPHYSICAL_CURVE_MST = new Vector();
	Vector VFINANCIAL_CURVE_MST = new Vector();
	Vector VCURVE_NM = new Vector();
	Vector VPRICE_RANGE = new Vector();
	Vector VPRICE_RANGE_NM = new Vector();
	Vector VPRICE_START_DT = new Vector();
	Vector VPRICE_END_DT = new Vector();
	Vector VCURVE_LOGIC = new Vector();
	Vector VFORMULA = new Vector();
	Vector VMIN_PRICE_ST_END_DT = new Vector();
	Vector VMIN_FORMULA = new Vector();
	Vector VACCOUNT = new Vector();
	Vector VDISPLAY_DEAL_MAP = new Vector();
	Vector VCONT_REF_NO = new Vector();
	Vector VDIS_CONTRACT_TYPE = new Vector();
	Vector VTCQ = new Vector();
	Vector VDCQ = new Vector();
	Vector VSUPPLIED_QTY_MMBTU = new Vector();
	Vector VBALANCE_QTY_MMBTU = new Vector();
	Vector VASSESSED_QTY_MMBTU = new Vector();
	Vector VTAQ_CONFIGURED = new Vector();
	Vector VTAQ_REMARK = new Vector();
	Vector VTAQ_DETAIL = new Vector();
	Vector VENTERED_BY = new Vector();
	Vector VMODIFIED_BY = new Vector();
	
	//Below Vectors are initialized by PB for Variable pricing info
	Vector VDELVH_MONTH = new Vector();
	Vector VDURATION = new Vector();
	Vector VSETTLE_PRICE = new Vector();
	Vector VEFF_PRICE = new Vector();
	
	Vector VPRICE_DECI = new Vector();
	Vector VFINAL_PRICE_DECI = new Vector();
	Vector VPREM_DISC_RATE = new Vector();
	Vector VMIN_PRICE_DECI = new Vector();
	Vector VMIN_PREM_DISC_RATE = new Vector();
	Vector VFINAL_PRICE = new Vector();
	
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVCONT_NO() {return VCONT_NO;}
	public Vector getVCONT_REV_NO() {return VCONT_REV_NO;}
	public Vector getVAGMT_NO() {return VAGMT_NO;}
	public Vector getVAGMT_REV_NO() {return VAGMT_REV_NO;}
	public Vector getVCONTRACT_TYPE() {return VCONTRACT_TYPE;}
	public Vector getVSIGNING_DT() {return VSIGNING_DT;}
	public Vector getVSTART_DT() {return VSTART_DT;}
	public Vector getVEND_DT() {return VEND_DT;}
	public Vector getVPRICE_TYPE() {return VPRICE_TYPE;}
	public Vector getVPRICE_RATE() {return VPRICE_RATE;}
	public Vector getVPRICE_RATE_UNIT_NM() {return VPRICE_RATE_UNIT_NM;}
	public Vector getVPRICE_RATE_UNIT() {return VPRICE_RATE_UNIT;}
	public Vector getVPHYSICAL_CURVE() {return VPHYSICAL_CURVE;}
	public Vector getVSLOPE() {return VSLOPE;}
	public Vector getVCONSTANT() {return VCONSTANT;}
	public Vector getVREMARK() {return VREMARK;}
	public Vector getVSEQ_NO() {return VSEQ_NO;}
	public Vector getVIS_RADIO_ENABLE() {return VIS_RADIO_ENABLE;}
	public Vector getVPHYSICAL_CURVE_MST() {return VPHYSICAL_CURVE_MST;}
	public Vector getVFINANCIAL_CURVE_MST() {return VFINANCIAL_CURVE_MST;}
	public Vector getVCURVE_NM() {return VCURVE_NM;}
	public Vector getVPRICE_RANGE() {return VPRICE_RANGE;}
	public Vector getVPRICE_RANGE_NM() {return VPRICE_RANGE_NM;}
	public Vector getVPRICE_START_DT() {return VPRICE_START_DT;}
	public Vector getVPRICE_END_DT() {return VPRICE_END_DT;}
	public Vector getVCURVE_LOGIC() {return VCURVE_LOGIC;}
	public Vector getVFORMULA() {return VFORMULA;}
	public Vector getVMIN_PRICE_ST_END_DT() {return VMIN_PRICE_ST_END_DT;}
	public Vector getVMIN_FORMULA() {return VMIN_FORMULA;}
	public Vector getVACCOUNT() {return VACCOUNT;}
	public Vector getVDISPLAY_DEAL_MAP() {return VDISPLAY_DEAL_MAP;}
	public Vector getVCONT_REF_NO() {return VCONT_REF_NO;}
	public Vector getVDIS_CONTRACT_TYPE() {return VDIS_CONTRACT_TYPE;}
	public Vector getVTCQ() {return VTCQ;}
	public Vector getVDCQ() {return VDCQ;}
	public Vector getVSUPPLIED_QTY_MMBTU() {return VSUPPLIED_QTY_MMBTU;}
	public Vector getVBALANCE_QTY_MMBTU() {return VBALANCE_QTY_MMBTU;}
	public Vector getVASSESSED_QTY_MMBTU() {return VASSESSED_QTY_MMBTU;}
	public Vector getVTAQ_CONFIGURED() {return VTAQ_CONFIGURED;}
	public Vector getVTAQ_REMARK() {return VTAQ_REMARK;}
	public Vector getVTAQ_DETAIL() {return VTAQ_DETAIL;}
	public Vector getVENTERED_BY() {return VENTERED_BY;}
	public Vector getVMODIFIED_BY() {return VMODIFIED_BY;}
	
	public Vector getVDELVH_MONTH() {return VDELVH_MONTH;}
	public Vector getVDURATION() {return VDURATION;}
	public Vector getVSETTLE_PRICE() {return VSETTLE_PRICE;}
	public Vector getVEFF_PRICE() {return VEFF_PRICE;}
	
	String counterparty_nm="";
	String counterparty_abbr="";
	String display_map_id="";
	
	public String getCounterparty_nm() {return counterparty_nm;}
	public String getCounterparty_abbr() {return counterparty_abbr;}
	public String getDisplay_map_id() {return display_map_id;}
	
	public Vector getVPRICE_DECI() {return VPRICE_DECI;}
	public Vector getVFINAL_PRICE_DECI() {return VFINAL_PRICE_DECI;}
	public Vector getVPREM_DISC_RATE() {return VPREM_DISC_RATE;}
	public Vector getVMIN_PRICE_DECI() {return VMIN_PRICE_DECI;}
	public Vector getVMIN_PREM_DISC_RATE() {return VMIN_PREM_DISC_RATE;}
	public Vector getVFINAL_PRICE() {return VFINAL_PRICE;}
}
