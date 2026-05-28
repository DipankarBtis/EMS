package com.etrm.fms.master;

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

import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;

//Coded By          : Harsh Patel
//Code Reviewed by	:
//CR Date			: 22/09/2022
//Status	  		: Developing
public class DataBean_Master
{
	String db_src_file_name="DataBean_Master.java";
	Connection conn;
	PreparedStatement stmt;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt5;
	ResultSet rset;
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset5;
	String queryString="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";

	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");

	UtilBean utilBean = new UtilBean();
	DateUtil utilDate = new DateUtil();

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
	    			if(callFlag.equalsIgnoreCase("GOVT_STAT_TAX_MST"))
	    			{
	    				fetchGovtStatNoList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("RATE_MASTER"))
	    			{
	    				if(!rate_mode.equals("0"))
	    				{
	    					getRateMaster();
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("INTEREST_AND_EXCHANGE_RATE_ENTRY"))
	    			{
	    				getCurrency();
	    				if(!rate_mode.equals("0"))
	    				{
	    					getRateMaster();
	    				}

	    				if(!component.equals("0") && !component.equals(""))
	    				{
	    					getRateComponentFlag();
	    					getInterestExchangeRateValue();
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("HOLIDAY_MST"))
	    			{
	    				getStateMst();
	    				getYearList();
	    				getHolidayList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("TAX_MASTER"))
	    			{
	    				getTaxMaster();
	    			}
	    			else if(callFlag.equalsIgnoreCase("TAX_STRUCTURE"))
	    			{
	    				getTaxCategoryMst();
	    				getTaxStructure();
	    				getTaxMaster();
	    			}
	    			else if(callFlag.equalsIgnoreCase("METER_MASTER"))
	    			{
	    				getMeterCounterpartyList();
	    				getMeterDetail();
	    			}
	    			else if(callFlag.equalsIgnoreCase("TAX_STRUCTURE_LIST"))
	    			{
	    				getTaxStructureList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("CRDR_TAX_STRUCTURE_LIST"))
	    			{
	    				getCrDrTaxStructureList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("SECTOR_MST"))
	    			{
	    				getSectorMaster();
	    			}
	    			else if(callFlag.equalsIgnoreCase("TCS_TDS_TAX_STRUCTURE_LIST"))
	    			{
	    				getTcsTdsTaxStructureList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("BANK_MST"))
	    			{
	    				getBankList();
	    				getStateMst();
	    				getCountryMst();
	    			}
	    			else if(callFlag.equalsIgnoreCase("VESSEL_MST"))
	    			{
	    				getShipMst();
	    			}
	    			else if(callFlag.equalsIgnoreCase("CUSTOM_DUTY_TAX_MST"))
	    			{
	    				getCustomDutyTaxdtls();
	    			}
	    			else if(callFlag.equalsIgnoreCase("PRODUCT_MST"))
	    			{
	    				getProductMst();
	    			}
	    			else if(callFlag.equalsIgnoreCase("MOLECULE_MST"))
	    			{
	    				getProductActiveMst();
	    				getProductMoleculeMst();
	    			}
	    			else if(callFlag.equalsIgnoreCase("SAC_MST"))
	    			{
	    				getSacDtl();
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
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}

	private void getCustomDutyTaxdtls() 
	{
		String function_nm="getCustomDutyTaxdtls()";
		try
		{
			int index = 0;
			queryString="SELECT A.TAX_STRUCT_CD,TO_CHAR(B.APP_DATE,'DD/MM/YYYY'),A.TAX_STRUCT_DTL,A.TAX_STRUCT_REMARK,"
					+ "TO_CHAR(A.EFF_DT,'DD/MM/YYYY'),B.SAP_TAX_CODE,TO_CHAR(A.ENT_DT,'DD/MM/YYYY HH24:MI:SS'),A.ENT_BY "
					+ "FROM FMS_CUSTOM_TAX_STRUCT_DTL A, FMS_TAX_STRUCTURE B "
					+ "WHERE A.COMPANY_CD=? AND A.TAX_STRUCT_CD=B.TAX_STR_CD ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				index+=1;
				VTAX_STRUCT_CD.add(rset.getString(1)==null?"":rset.getString(1));
				
				String tax_struct_dt = rset.getString(2)==null?"":rset.getString(2);
				String tax_struct_name = rset.getString(3)==null?"":rset.getString(3);
				
				VTAX_STRUCT_APP_DT.add(tax_struct_dt);
				VTAX_STRUCT_NM.add(tax_struct_name);
				VDISP_TAX_STRUCT_NM.add(tax_struct_name+" Commencement on "+tax_struct_dt);
				VTAX_STRUCT_RMK.add(rset.getString(4)==null?"":rset.getString(4));
				VEFF_DT.add(rset.getString(5)==null?"":rset.getString(5));
				VSAP_TAX_CODE.add(rset.getString(6)==null?"":rset.getString(6));
				
				String ent_dt = rset.getString(7)==null?"":rset.getString(7);
				String ent_by = rset.getString(8)==null?"":rset.getString(8);
				
				VENT_BY.add(""+utilBean.getEmpName(conn,ent_by)+"<br>"+ent_dt);
				VINDEX.add(index);
			}
			rset.close();
			stmt.close();
			
			queryString1="SELECT TO_CHAR(MAX(EFF_DT),'DD/MM/YYYY') "
					+ "FROM FMS_CUSTOM_TAX_STRUCT_DTL "
					+ "WHERE COMPANY_CD=? ";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				last_eff_dt=rset1.getString(1)==null?"":rset1.getString(1);
			}
			rset1.close();
			stmt1.close();
		}
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getBankList()
	{
		String function_nm="getBankList()";
		try
		{
			int index = 0;
			queryString = "SELECT BANK_CD,BANK_NAME,BANK_ABBR,TO_CHAR(EFF_DT,'DD/MM/YYYY'),BRANCH_NAME, "
					+ "ADDR,CITY,PIN,STATE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL,REMARK,BRANCH_IFSC_CD,ACTIVE_FLAG "
					+ "FROM FMS_BANK_MST "
					+ "ORDER BY BANK_CD";
			stmt=conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				index+=1;
				VBANK_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VBANK_NAME.add(rset.getString(2)==null?"":rset.getString(2));
				VBANK_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
				VEFF_DT.add(rset.getString(4)==null?"":rset.getString(4));
				VBANK_BRANCH.add(rset.getString(5)==null?"":rset.getString(5));
				VBANK_ADDR.add(rset.getString(6)==null?"":rset.getString(6));
				VBANK_CITY.add(rset.getString(7)==null?"":rset.getString(7));
				VBANK_PIN.add(rset.getString(8)==null?"":rset.getString(8));
				String bank_state=rset.getString(9)==null?"":rset.getString(9);
				VBANK_STATE.add(bank_state);
				String bank_country=rset.getString(10)==null?"":rset.getString(10);
				VBANK_COUNTRY.add(bank_country);
				VBANK_PHONE.add(rset.getString(11)==null?"":rset.getString(11));
				VBANK_MOBILE.add(rset.getString(12)==null?"":rset.getString(12));
				VBANK_ALT_PHONE.add(rset.getString(13)==null?"":rset.getString(13));
				VBANK_FAX1.add(rset.getString(14)==null?"":rset.getString(14));
				VBANK_FAX2.add(rset.getString(15)==null?"":rset.getString(15));
				VBANK_EMAIL.add(rset.getString(16)==null?"":rset.getString(16));
				VBANK_REMARKS.add(rset.getString(17)==null?"":rset.getString(17));
				VBANK_IFSC_CD.add(rset.getString(18)==null?"":rset.getString(18));
				VBANK_STATUS_FLAG.add(rset.getString(19)==null?"N":rset.getString(19));
				
				VINDEX.add(index);
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void fetchGovtStatNoList()
	{
		String function_nm="fetchGovtStatNoList()";
		try
		{
			queryString = "SELECT NVL(STAT_CD,?),STAT_NM,STAT_TYPE,STATUS,REMARK "
					+ "FROM FMS_GOVT_STAT_TAX "
					+ "ORDER BY STAT_CD";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, "0");
			rset = stmt.executeQuery();
			while(rset.next())
			{
				VSTAT_CD.add(rset.getString(1)==null?"0":rset.getString(1));
				VSTAT_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VSTAT_TYPE.add(rset.getString(3)==null?"0":rset.getString(3));
				VSTAT_STATUS.add(rset.getString(4)==null?"0":rset.getString(4));
				VSTAT_REMARK.add(rset.getString(5)==null?"":rset.getString(5));
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getRateMaster()
	{
		String function_nm="getRateMaster()";
		try
		{
			if(rate_mode.equals("EXCHANGE"))
			{
				queryString = "SELECT EXC_RATE_CD,EXC_RATE_NM,BANK_ABBR,FLAG,"
						+ "COMPONENT_FLAG,COMPONENT1,COMPONENT2 "
						+ "FROM FMS_EXCHG_RATE_MST "
						+ "ORDER BY EXC_RATE_NM";
			}
			else
			{
				queryString = "SELECT INT_RATE_CD,INT_RATE_NM,BANK_ABBR,FLAG "
						+ "FROM FMS_INT_RATE_MST "
						+ "ORDER BY INT_RATE_NM";
			}
			stmt=conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VRATE_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VRATE_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VBANK_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
				VRATE_FLAG.add(rset.getString(4)==null?"":rset.getString(4));
				
				if(rate_mode.equals("EXCHANGE"))
				{
					VCOMPONENT_FLAG.add(rset.getString(5)==null?"":rset.getString(5));
					VCOMPONENT1.add(rset.getString(6)==null?"":rset.getString(6));
					VCOMPONENT2.add(rset.getString(7)==null?"":rset.getString(7));					
				}
				else
				{
					VCOMPONENT_FLAG.add("");
					VCOMPONENT1.add("");
					VCOMPONENT2.add("");	
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

	public void getInterestExchangeRateValue()
	{
		String function_nm="getInterestExchangeRateValue()";
		try
		{
			String start_dt = "01/"+month+"/"+year;

			queryString="SELECT TO_CHAR(TO_DATE(TD.END_DATE + 1 - ROWNUM),'DD/MM/YYYY') MONTH_DATE "
					+ "FROM ALL_OBJECTS,(SELECT TO_DATE(?,'DD/MM/YYYY')-1 START_DATE,TO_DATE(ADD_MONTHS(TRUNC(TO_DATE(?,'DD/MM/YYYY')) - (TO_NUMBER(TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'DD')) - 1), 1) -1,'DD/MM/YYYY') END_DATE FROM DUAL) TD "
					+ "WHERE TO_DATE(TD.END_DATE - ROWNUM, 'DD/MM/YYYY') >= TO_DATE(TD.START_DATE,'DD/MM/YYYY') "
					+ "ORDER BY TO_DATE(MONTH_DATE,'DD/MM/YYYY')";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, start_dt);
			stmt.setString(2, start_dt);
			stmt.setString(3, start_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String date = rset.getString(1)==null?"":rset.getString(1);

				if(rate_mode.equals("EXCHANGE"))
				{
					queryString1="SELECT EXCHG_VAL,CURRENCY_CD,CURRENCY_CD_FROM,REMARK "
							+ "FROM FMS_EXCHG_RATE_ENTRY "
							+ "WHERE EXCHG_RATE_CD=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND CURRENCY_CD_FROM=? AND CURRENCY_CD=?";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, component);
					stmt1.setString(2, date);
					stmt1.setString(3, currency_from);
					stmt1.setString(4, currency_to);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						VRATE_VALUE.add(nf2.format(rset1.getDouble(1)));
						VTO_CURRENCY.add(rset1.getString(2)==null?"1":rset1.getString(2));
						VFROM_CURRENCY.add(rset1.getString(3)==null?"2":rset1.getString(3));
						VRATE_REMARK.add(rset1.getString(4)==null?"":rset1.getString(4));
						VRATE_EFF_DT.add(date);
						VCOLOR.add("#99ffcc");
					}
					else
					{
						VRATE_VALUE.add("");
						VTO_CURRENCY.add("1");
						VFROM_CURRENCY.add("2");
						VRATE_REMARK.add("");
						VRATE_EFF_DT.add(date);
						VCOLOR.add("");
					}
					rset1.close();
					stmt1.close();
				}
				else
				{
					queryString1="SELECT INT_VAL,REMARK "
							+ "FROM FMS_INT_PAY_RATE_ENTRY "
							+ "WHERE INT_RATE_CD=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY')";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, component);
					stmt1.setString(2, date);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						VRATE_VALUE.add(nf.format(rset1.getDouble(1)));
						VRATE_REMARK.add(rset1.getString(2)==null?"":rset1.getString(2));
						VRATE_EFF_DT.add(date);
						VCOLOR.add("#99ffcc");
					}
					else
					{
						VRATE_VALUE.add("");
						VRATE_REMARK.add("");
						VRATE_EFF_DT.add(date);
						VCOLOR.add("");
					}
					rset1.close();
					stmt1.close();
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

	public void getCurrency()
	{
		String function_nm="getCurrency()";
		try
		{
			queryString="SELECT RATE_UNIT_CD,RATE_UNIT_ABR "
					+ "FROM FMS_RATE_UNIT";
			stmt=conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VCURRENCY_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VCURRENCY_ABR.add(rset.getString(2)==null?"":rset.getString(2));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getStateMst()
	{
		String function_nm="getStateMst()";
		try
		{
			utilBean.getStateMaster(conn);
			VSTATE_CD = utilBean.getTIN();
			VSTATE_NM = utilBean.getSTATE_NM();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCountryMst()
	{
		String function_nm="getCountryMst()";
		try
		{
			utilBean.getCountryMaster(conn);
			VCOUNTRY_CD = utilBean.getCOUNTRY_CODE();
			VCOUNTRY_NM = utilBean.getCOUNTRY_NM();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getYearList()
	{
		String function_nm="getYearList()";
		try
		{
			int temp_yr=utilDate.getCurrentYear();

			queryString = "SELECT DISTINCT TO_NUMBER(TO_CHAR(TO_DATE(TO_CHAR(HOLIDAY_DT,'DD/MM/YYYY'),'DD/MM/YYYY'),'YYYY')) "
					+ "FROM FMS_HOLIDAY_DTL "
					+ "WHERE HOLIDAY_DT IS NOT NULL "
					+ "ORDER BY TO_NUMBER(TO_CHAR(TO_DATE(TO_CHAR(HOLIDAY_DT,'DD/MM/YYYY'),'DD/MM/YYYY'),'YYYY')) DESC";
			stmt=conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VYear.add(rset.getString(1)==null?"":rset.getString(1));
			}
			rset.close();
			stmt.close();

			if(!VYear.contains(""+temp_yr))
			{
				VYear.add(temp_yr);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getHolidayList()
	{
		String function_nm="getHolidayList()";
		try
		{
			queryString="SELECT TO_CHAR(HOLIDAY_DT,'DD/MM/YYYY'),HOLIDAY_NM,HOLIDAY_DAY,FLAG,STATE_TIN "
					+ "FROM FMS_HOLIDAY_DTL "
					+ "WHERE HOLIDAY_DT >= TO_DATE(?,'DD/MM/YYYY') AND HOLIDAY_DT <= TO_DATE(?,'DD/MM/YYYY') "
					+ "ORDER BY HOLIDAY_DT";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, "01/01/"+year);
			stmt.setString(2, "31/12/"+year);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VHOLIDAY_DT.add(rset.getString(1)==null?"":rset.getString(1));
				VHOLIDAY_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VHOLIDAY_DAY.add(rset.getString(3)==null?"":rset.getString(3));
				VHOLIDAY_FLAG.add(rset.getString(4)==null?"N":rset.getString(4));
				VHOLI_STATE_CD.add(rset.getString(5)==null?"":rset.getString(5));

				String state_cd = rset.getString(5)==null?"00":rset.getString(5);
				String state_nm = utilBean.getStateName(conn,state_cd);
				
				//Harsh Maheta: VHOLI_STATE_NM updated for no state selected
				if(state_cd.equals("00")) 
				{
					state_nm="All State - National Holiday";
				}
				
				VHOLI_STATE_NM.add(state_nm);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getTaxMaster()
	{
		String function_nm="getTaxMaster()";
		try
		{
			queryString="SELECT TAX_CODE,TAX_NAME,TAX_ALIAS_CODE,SHT_NM,TO_CHAR(APP_DATE,'DD/MM/YYYY'),STATUS "
					+ "FROM FMS_TAX_MST "
					+ "ORDER BY TAX_CODE";
			stmt=conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String tax_code = rset.getString(1)==null?"":rset.getString(1);
				VTAX_CD.add(tax_code);
				VTAX_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VTAX_ALIAS_NM.add(rset.getString(3)==null?"":rset.getString(3));
				VTAX_SHT_NM.add(rset.getString(4)==null?"":rset.getString(4));
				VTAX_APP_DT.add(rset.getString(5)==null?"":rset.getString(5));
				VTAX_STATUS.add(rset.getString(6)==null?"":rset.getString(6));

				queryString1="SELECT COUNT(*) "
						+ "FROM FMS_TAX_STRUCTURE_DTL "
						+ "WHERE TAX_CODE=?";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, tax_code);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					VCOUNT.add(""+rset1.getInt(1));
				}
				else
				{
					VCOUNT.add("0");
				}
				rset1.close();
				stmt1.close();
			}
			stmt.close();
			rset.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getTaxCategoryMst()
	{
		String function_nm="getTaxCategoryMst()";
		try
		{
			queryString="SELECT DISTINCT TAX_CATEGORY "
					+ "FROM FMS_TAX_STRUCTURE "
					+ "WHERE TAX_CATEGORY IS NOT NULL ";
			stmt=conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String tax_category=rset.getString(1)==null?"":rset.getString(1);
				String tax_category_nm = Tax_CategoryName(tax_category);
				VMASTER_TAX_CATEGORY.add(tax_category);
				VMASTER_TAX_CATEGORY_NM.add(tax_category_nm+" Tax/s");
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getTaxStructure()
	{
		String function_nm="getTaxStructure()";
		try
		{
			for(int i=0; i<VMASTER_TAX_CATEGORY.size(); i++)
			{
				int index=0;
				queryString="SELECT TAX_STR_CD,DESCR,STATUS,REMARK,TAX_CATEGORY,TO_CHAR(APP_DATE,'DD/MM/YYYY'),"
						+ "SAP_TAX_CODE,SAP_GL,PAY_RECV "
						+ "FROM FMS_TAX_STRUCTURE A "
						+ "WHERE TAX_CATEGORY=? "
						+ "AND APP_DATE=(SELECT MAX(B.APP_DATE) FROM FMS_TAX_STRUCTURE B "
						+ "WHERE A.TAX_STR_CD=B.TAX_STR_CD AND B.APP_DATE<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) "
						+ "ORDER BY TAX_STR_CD";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, ""+VMASTER_TAX_CATEGORY.elementAt(i));
				rset=stmt.executeQuery();
				while(rset.next())
				{
					index=index+1;
					String tax_struct_cd=rset.getString(1)==null?"":rset.getString(1);

					VTAX_STRUCT_CD.add(tax_struct_cd);
					VTAX_STRUCT_NM.add(rset.getString(2)==null?"":rset.getString(2));
					VTAX_STRUCT_STATUS.add(rset.getString(3)==null?"":rset.getString(3));
					VTAX_STRUCT_RMK.add(rset.getString(4)==null?"":rset.getString(4));
					String tax_category=rset.getString(5)==null?"P":rset.getString(5);
					VTAX_CATEGORY.add(tax_category);
					VTAX_CATEGORY_NM.add(""+Tax_CategoryName(tax_category));
					VTAX_STRUCT_APP_DT.add(rset.getString(6)==null?"":rset.getString(6));
					VSAP_TAX_CODE.add(rset.getString(7)==null?"":rset.getString(7));
					VSAP_GL.add(rset.getString(8)==null?"":rset.getString(8));
					String pay_recv=rset.getString(9)==null?"":rset.getString(9);
					VPAY_RECV.add(pay_recv);
					String pay_recv_nm="";
					if(pay_recv.equals("P"))
					{
						pay_recv_nm="Payable";
					}
					else if(pay_recv.equals("R"))
					{
						pay_recv_nm="Receivable";
					}
					VPAY_RECV_NM.add(pay_recv_nm);	
					
					int count=0;
					/*queryString1="SELECT COUNT(*) "
							+ "FROM FMS_ENTITY_TAX_STRUCT_DTL A "
							+ "WHERE TAX_STRUCT_CD=?";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, tax_struct_cd);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						count+=rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					
					queryString2="SELECT COUNT(*) "
							+ "FROM FMS_ENTITY_SERVICE_TAX_DTL A "
							+ "WHERE TAX_STRUCT_CD=?";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, tax_struct_cd);
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						count+=rset2.getInt(1);
					}
					rset2.close();
					stmt2.close();
					
					queryString3="SELECT COUNT(*) "
							+ "FROM FMS_ENTITY_BU_SVC_TAX_DTL A "
							+ "WHERE TAX_STRUCT_CD=?";
					stmt3=conn.prepareStatement(queryString3);
					stmt3.setString(1, tax_struct_cd);
					rset3=stmt3.executeQuery();
					if(rset3.next())
					{
						count+=rset3.getInt(1);
					}
					rset3.close();
					stmt3.close();
					
					queryString3="SELECT COUNT(*) "
							+ "FROM FMS_ENTITY_TCS_TDS_MST A "
							+ "WHERE TAX_STRUCT_CD=?";
					stmt3=conn.prepareStatement(queryString3);
					stmt3.setString(1, tax_struct_cd);
					rset3=stmt3.executeQuery();
					if(rset3.next())
					{
						count+=rset3.getInt(1);
					}
					rset3.close();
					stmt3.close();
					
					queryString4="SELECT COUNT(*) "
							+ "FROM FMS_PUR_FFLOW_INV_MST A "
							+ "WHERE TAX_STRUCT_CD=?";
					stmt4=conn.prepareStatement(queryString4);
					stmt4.setString(1, tax_struct_cd);
					rset4=stmt4.executeQuery();
					if(rset4.next())
					{
						count+=rset4.getInt(1);
					}
					rset4.close();
					stmt4.close();
					
					queryString5="SELECT COUNT(*) "
							+ "FROM FMS_FFLOW_INV_MST A "
							+ "WHERE TAX_STRUCT_CD=?";
					stmt5=conn.prepareStatement(queryString5);
					stmt5.setString(1, tax_struct_cd);
					rset5=stmt5.executeQuery();
					if(rset5.next())
					{
						count+=rset5.getInt(1);
					}
					rset5.close();
					stmt5.close();
					*/
					queryString1 = "SELECT SUM(CNT) AS TOTAL_COUNT FROM ("
				             + "SELECT COUNT(*) AS CNT FROM FMS_ENTITY_TAX_STRUCT_DTL WHERE TAX_STRUCT_CD=? "
				             + "UNION ALL "
				             + "SELECT COUNT(*) AS CNT FROM FMS_ENTITY_SERVICE_TAX_DTL WHERE TAX_STRUCT_CD=? "
				             + "UNION ALL "
				             + "SELECT COUNT(*) AS CNT FROM FMS_ENTITY_BU_SVC_TAX_DTL WHERE TAX_STRUCT_CD=? "
				             + "UNION ALL "
				             + "SELECT COUNT(*) AS CNT FROM FMS_ENTITY_TCS_TDS_MST WHERE TAX_STRUCT_CD=? "
				             + "UNION ALL "
				             + "SELECT COUNT(*) AS CNT FROM FMS_CUSTOM_TAX_STRUCT_DTL WHERE TAX_STRUCT_CD=? "
				             + "UNION ALL "
				             + "SELECT COUNT(*) AS CNT FROM FMS_PUR_FFLOW_INV_MST WHERE TAX_STRUCT_CD=? "
				             + "UNION ALL "
				             + "SELECT COUNT(*) AS CNT FROM FMS_FFLOW_INV_MST WHERE TAX_STRUCT_CD=? "
				             + "UNION ALL "
				             + "SELECT COUNT(*) AS CNT FROM FMS_GX_FFLOW_INV_MST WHERE TAX_STRUCT_CD=? "
				             + "UNION ALL "
				             + "SELECT COUNT(*) AS CNT FROM FMS_GTA_FFLOW_INV_MST WHERE TAX_STRUCT_CD=? "
				             + "UNION ALL "
				             + "SELECT COUNT(*) AS CNT FROM FMS_PUR_FFLOW_INV_MST WHERE TDS_STRUCT_CD=? "
				             + "UNION ALL "
				             + "SELECT COUNT(*) AS CNT FROM FMS_FFLOW_INV_MST WHERE TDS_STRUCT_CD=? "
				             + "UNION ALL "
				             + "SELECT COUNT(*) AS CNT FROM FMS_GX_FFLOW_INV_MST WHERE TDS_STRUCT_CD=? "
				             + "UNION ALL "
				             + "SELECT COUNT(*) AS CNT FROM FMS_GTA_FFLOW_INV_MST WHERE TDS_STRUCT_CD=? "
				             + "UNION ALL "
				             + "SELECT COUNT(*) AS CNT FROM FMS_SECURITY_MST WHERE TDS_STRUCT_CD=? "
				             + "UNION ALL "
				             + "SELECT COUNT(*) AS CNT FROM FMS_PUR_FFLOW_INV_MST WHERE TCS_STRUCT_CD=? "
				             + "UNION ALL "
				             + "SELECT COUNT(*) AS CNT FROM FMS_FFLOW_INV_MST WHERE TCS_STRUCT_CD=? "
				             + "UNION ALL "
				             + "SELECT COUNT(*) AS CNT FROM FMS_GX_FFLOW_INV_MST WHERE TCS_STRUCT_CD=? "
				             + "UNION ALL "
				             + "SELECT COUNT(*) AS CNT FROM FMS_GTA_FFLOW_INV_MST WHERE TCS_STRUCT_CD=? "
				             + ") A";
					stmt1=conn.prepareStatement(queryString1);
					for (int j = 1; j <= 18; j++) 
					{
						stmt1.setString(j, tax_struct_cd);
					}
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						count=rset1.getInt(1);
					}
					rset1.close();
					stmt1.close();
					
					VCOUNT.add(count);
				}
				VINDEX.add(index);
				rset.close();
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getTaxStructureList()
	{
		String function_nm="getTaxStructureList()";
		try
		{
			queryString="SELECT TAX_STR_CD,DESCR,STATUS,REMARK,TAX_CATEGORY,TO_CHAR(APP_DATE,'DD/MM/YYYY'),"
					+ "SAP_TAX_CODE,SAP_GL,PAY_RECV "
					+ "FROM FMS_TAX_STRUCTURE A "
					+ "WHERE TAX_CATEGORY=? "
					+ "AND APP_DATE=(SELECT MAX(B.APP_DATE) FROM FMS_TAX_STRUCTURE B "
					+ "WHERE A.TAX_STR_CD=B.TAX_STR_CD AND B.APP_DATE<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) ";
			if(!pay_recv.equals(""))
			{
				queryString+="AND PAY_RECV=? ";
			}
			queryString+="ORDER BY TAX_STR_CD";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, tax_category);
			if(!pay_recv.equals(""))
			{
				stmt.setString(2, pay_recv);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String tax_struct_cd=rset.getString(1)==null?"":rset.getString(1);

				VTAX_STRUCT_CD.add(tax_struct_cd);
				VTAX_STRUCT_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VTAX_STRUCT_STATUS.add(rset.getString(3)==null?"":rset.getString(3));
				VTAX_STRUCT_RMK.add(rset.getString(4)==null?"":rset.getString(4));
				String tax_category=rset.getString(5)==null?"P":rset.getString(5);
				VTAX_CATEGORY.add(tax_category);
				VTAX_CATEGORY_NM.add(""+Tax_CategoryName(tax_category));
				VTAX_STRUCT_APP_DT.add(rset.getString(6)==null?"":rset.getString(6));
				VSAP_TAX_CODE.add(rset.getString(7)==null?"":rset.getString(7));
				VSAP_GL.add(rset.getString(8)==null?"":rset.getString(8));
				String pay_recv=rset.getString(9)==null?"":rset.getString(9);
				VPAY_RECV.add(pay_recv);
				String pay_recv_nm="";
				if(pay_recv.equals("P"))
				{
					pay_recv_nm="Payable";
				}
				else if(pay_recv.equals("R"))
				{
					pay_recv_nm="Receivable";
				}
				VPAY_RECV_NM.add(pay_recv_nm);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCrDrTaxStructureList()
	{
		String function_nm="getCrDrTaxStructureList()";
		try
		{
			queryString="SELECT A.TAX_STR_CD,A.DESCR,A.STATUS,A.REMARK,A.TAX_CATEGORY,TO_CHAR(A.APP_DATE,'DD/MM/YYYY'),"
					+ "A.SAP_TAX_CODE,A.SAP_GL,A.PAY_RECV "
					+ "FROM FMS_TAX_STRUCTURE A "
					+ "WHERE A.TAX_CATEGORY=? AND A.TAX_STR_CD != ? "
					+ "AND A.APP_DATE=(SELECT MAX(B.APP_DATE) FROM FMS_TAX_STRUCTURE B "
					+ "WHERE A.TAX_STR_CD=B.TAX_STR_CD AND B.APP_DATE<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) "
					+ "AND (SELECT COUNT(*) FROM FMS_TAX_STRUCTURE_DTL B WHERE A.TAX_STR_CD=B.TAX_STR_CD "
					+ "AND B.TAX_CODE IN (SELECT C.TAX_CODE FROM FMS_TAX_STRUCTURE_DTL C WHERE C.TAX_STR_CD=?)) > 0";
			if(!pay_recv.equals(""))
			{
				queryString+="AND PAY_RECV=? ";
			}
			queryString+="ORDER BY TAX_STR_CD";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, tax_category);
			stmt.setString(2, tax_struct_cd);
			stmt.setString(3, tax_struct_cd);
			if(!pay_recv.equals(""))
			{
				stmt.setString(4, pay_recv);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String tax_struct_cd=rset.getString(1)==null?"":rset.getString(1);

				VTAX_STRUCT_CD.add(tax_struct_cd);
				VTAX_STRUCT_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VTAX_STRUCT_STATUS.add(rset.getString(3)==null?"":rset.getString(3));
				VTAX_STRUCT_RMK.add(rset.getString(4)==null?"":rset.getString(4));
				String tax_category=rset.getString(5)==null?"P":rset.getString(5);
				VTAX_CATEGORY.add(tax_category);
				VTAX_CATEGORY_NM.add(""+Tax_CategoryName(tax_category));
				VTAX_STRUCT_APP_DT.add(rset.getString(6)==null?"":rset.getString(6));
				VSAP_TAX_CODE.add(rset.getString(7)==null?"":rset.getString(7));
				VSAP_GL.add(rset.getString(8)==null?"":rset.getString(8));
				String pay_recv=rset.getString(9)==null?"":rset.getString(9);
				VPAY_RECV.add(pay_recv);
				String pay_recv_nm="";
				if(pay_recv.equals("P"))
				{
					pay_recv_nm="Payable";
				}
				else if(pay_recv.equals("R"))
				{
					pay_recv_nm="Receivable";
				}
				VPAY_RECV_NM.add(pay_recv_nm);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getTcsTdsTaxStructureList()
	{
		String function_nm="getTcsTdsTaxStructureList()";
		try
		{
			queryString="SELECT TAX_STR_CD,DESCR,STATUS,REMARK,TAX_CATEGORY,TO_CHAR(APP_DATE,'DD/MM/YYYY'),"
					+ "SAP_TAX_CODE,SAP_GL,PAY_RECV "
					+ "FROM FMS_TAX_STRUCTURE A "
					+ "WHERE TAX_CATEGORY=? AND (DESCR LIKE ?) "
					+ "AND APP_DATE=(SELECT MAX(B.APP_DATE) FROM FMS_TAX_STRUCTURE B "
					+ "WHERE A.TAX_STR_CD=B.TAX_STR_CD AND B.APP_DATE<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) ";
			if(!pay_recv.equals(""))
			{
				queryString+="AND PAY_RECV=? ";
			}	
			queryString+="ORDER BY TAX_STR_CD";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, tax_category);
			stmt.setString(2, tax_app+"%");
			if(!pay_recv.equals(""))
			{
				stmt.setString(3, pay_recv);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String tax_struct_cd=rset.getString(1)==null?"":rset.getString(1);

				VTAX_STRUCT_CD.add(tax_struct_cd);
				VTAX_STRUCT_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VTAX_STRUCT_STATUS.add(rset.getString(3)==null?"":rset.getString(3));
				VTAX_STRUCT_RMK.add(rset.getString(4)==null?"":rset.getString(4));
				String tax_category=rset.getString(5)==null?"P":rset.getString(5);
				VTAX_CATEGORY.add(tax_category);
				VTAX_CATEGORY_NM.add(""+Tax_CategoryName(tax_category));
				VTAX_STRUCT_APP_DT.add(rset.getString(6)==null?"":rset.getString(6));
				VSAP_TAX_CODE.add(rset.getString(7)==null?"":rset.getString(7));
				VSAP_GL.add(rset.getString(8)==null?"":rset.getString(8));
				String pay_recv=rset.getString(9)==null?"":rset.getString(9);
				VPAY_RECV.add(pay_recv);
				String pay_recv_nm="";
				if(pay_recv.equals("P"))
				{
					pay_recv_nm="Payable";
				}
				else if(pay_recv.equals("R"))
				{
					pay_recv_nm="Receivable";
				}
				VPAY_RECV_NM.add(pay_recv_nm);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getCounterpartyList()
	{
		String function_nm="getCounterpartyList()";
		try
		{
			//utilBean.getEffectiveTransporterCounterpartyList(comp_cd);
			utilBean.getEffectiveEntityCounterpartyList(conn,comp_cd,"R");
			VCOUNTERPARTY_CD= utilBean.getCOUNTERPARTY_CD();
			VCOUNTERPARTY_NM = utilBean.getCOUNTERPARTY_NM();
			VCOUNTERPARTY_ABBR = utilBean.getCOUNTERPARTY_ABBR();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getMeterDetail()
	{
		String function_nm="getMeterDetail()";
		try
		{
			queryString="SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_METER_MST "
					+ "WHERE COMPANY_CD=? AND METER_TYPE=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, entity);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String transCd=rset.getString(1)==null?"":rset.getString(1);
				String trans_abbr = ""+utilBean.getCounterpartyABBR(conn,transCd);
				String trans_status = (String) utilBean.getCounterpartyDetails(conn, transCd, utilDate.getSysdate()).get("status");

				VTRANSPORTER_CD.add(transCd);
				VTRANSPORTER_ABBR.add(trans_abbr);
				VTRANSPORTER_STATUS.add(trans_status);
			}
			rset.close();
			stmt.close();

			for(int i=0; i<VTRANSPORTER_CD.size(); i++)
			{
				int index=0;
				String transCd=""+VTRANSPORTER_CD.elementAt(i);
				queryString1="SELECT DISTINCT PLANT_SEQ "
						+ "FROM FMS_METER_MST "
						+ "WHERE COMPANY_CD=? AND METER_TYPE=? "
						+ "AND COUNTERPARTY_CD=?";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, entity);
				stmt1.setString(3, transCd);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					String trans_plant_seq=rset1.getString(1)==null?"":rset1.getString(1);

					VTRANSPORTER_PLANT_SEQ.add(trans_plant_seq);
					VTRANSPORTER_PLANT_ABBR.add(""+utilBean.getCounterpartyPlantABBR(conn,transCd, comp_cd, trans_plant_seq, "R"));

					int sub_index=0;
					index+=1;
					queryString2="SELECT METER_SEQ,METER_ID,METER_REF,SPECIFICATION,NOTE,STATUS,"
							+ "ENT_BY,TO_CHAR(ENT_DT,'HH24:MI:SS'),MODIFY_BY,TO_CHAR(MODIFY_DT,'HH24:MI:SS'),TO_CHAR(EFF_DT,'DD/MM/YYYY') "
							+ "FROM FMS_METER_MST A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND PLANT_SEQ=? "
							+ "AND METER_TYPE=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_METER_MST B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.PLANT_SEQ=B.PLANT_SEQ "
							+ "AND A.METER_TYPE=B.METER_TYPE AND A.METER_SEQ=B.METER_SEQ)";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, transCd);
					stmt2.setString(3, trans_plant_seq);
					stmt2.setString(4, entity);
					rset2=stmt2.executeQuery();
					while(rset2.next())
					{
						sub_index+=1;
						VMETER_SEQ.add(rset2.getString(1)==null?"0":rset2.getString(1));
						VMETER_ID.add(rset2.getString(2)==null?"":rset2.getString(2));
						VMETER_REF.add(rset2.getString(3)==null?"":rset2.getString(3));
						VSPECIFICATION.add(rset2.getString(4)==null?"":rset2.getString(4));
						VNOTE.add(rset2.getString(5)==null?"":rset2.getString(5));
						VSTATUS.add(rset2.getString(6)==null?"Y":rset2.getString(6));

						String ent_by_nm=""+utilBean.getEmpName(conn,rset2.getString(7)==null?"":rset2.getString(7));
						VENT_BY.add(ent_by_nm);
						VENT_DT.add(rset2.getString(8)==null?"":rset2.getString(8));

						String modify_by_nm=""+utilBean.getEmpName(conn,rset2.getString(9)==null?"":rset2.getString(9));
						VMODIFY_BY.add(modify_by_nm);
						VMODIFY_DT.add(rset2.getString(10)==null?"":rset2.getString(10));
						VEFF_DT.add(rset2.getString(11)==null?"":rset2.getString(11));
					}
					rset2.close();
					stmt2.close();

					VSUB_INDEX.add(sub_index);
				}
				VINDEX.add(index);
				rset1.close();
				stmt1.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getMeterCounterpartyList()
	{
		String function_nm="getMeterDetail()";
		try
		{
			utilBean.getAllEntityCounterpartyList(conn,"KYC",comp_cd,"R");
			VCOUNTERPARTY_CD= utilBean.getCOUNTERPARTY_CD();
			VCOUNTERPARTY_NM = utilBean.getCOUNTERPARTY_NM();
			VCOUNTERPARTY_ABBR = utilBean.getCOUNTERPARTY_ABBR();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public String Tax_CategoryName(String flag)
	{
		String function_nm="Tax_CategoryName()";
		String tax_category_nm="";
		try
		{
			if(flag.equals("P"))
			{
				tax_category_nm="Product";
			}
			else if(flag.equals("S"))
			{
				tax_category_nm="Service";
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return tax_category_nm;
	}

	public void getSectorMaster()
	{
		String function_nm="getSectorMaster()";
		try
		{
			int count=0;
//			queryString="SELECT SECTOR_CD,SECTOR_NAME,SECTOR_TYPE,STATUS_FLAG,SECTOR_ABBR "
//					+ "FROM FMS_SECTOR_MST ";
			queryString="SELECT A.SECTOR_CD,A.SECTOR_NAME,A.SECTOR_TYPE,A.STATUS_FLAG,A.SECTOR_ABBR,  "
					+ "TO_CHAR(B.EFF_DT,'DD/MM/YYYY'),B.DEMAND_SECT_CD,B.SUPPLY_SECT_CD  "
					+ "FROM FMS_SECTOR_MST A LEFT  JOIN "
					+ "(SELECT SECTOR_CD,EFF_DT,DEMAND_SECT_CD,SUPPLY_SECT_CD  "
					+ "FROM FMS_SECTOR_DTL X WHERE EFF_DT = "
					+ "(SELECT MAX(EFF_DT) FROM FMS_SECTOR_DTL Y WHERE X.SECTOR_CD = Y.SECTOR_CD "
					+ "GROUP BY SECTOR_CD)"
					+ ") B  "
					+ "ON  A.SECTOR_CD = B.SECTOR_CD ";
			if(!filter_status.equals("0"))
			{
				queryString+="WHERE A.STATUS_FLAG=? ";
			}
			queryString+= "ORDER BY A.SECTOR_NAME ";
			stmt=conn.prepareStatement(queryString);
			if(!filter_status.equals("0"))
			{
				stmt.setString(1, filter_status);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				count=0;
				String sector_cd=rset.getString(1)==null?"":rset.getString(1);
				VSECTOR_CD.add(sector_cd);
				VSECTOR_NAME.add(rset.getString(2)==null?"":rset.getString(2));
				String sector_type=rset.getString(3)==null?"":rset.getString(3);
				String sector_type_nm="";
				if(sector_type.equals("Y"))
				{
					sector_type_nm="Re-seller/CGD/LDC";
				}
				else if(sector_type.equals("P"))
				{
					sector_type_nm="End Customer";
				}
				else if(sector_type.equals("O"))
				{
					sector_type_nm="Others";
				}

				VSECTOR_TYPE.add(sector_type);
				VSECTOR_TYPE_NM.add(sector_type_nm);

				VSTATUS_FLAG.add(rset.getString(4)==null?"":rset.getString(4));
				VSECTOR_ABBR.add(rset.getString(5)==null?"":rset.getString(5));
				VEFF_DT.add(rset.getString(6)==null?"":rset.getString(6));
				VDEMAND_SECTOR_CD.add(rset.getString(7)==null?"":rset.getString(7));
				VSUPPLY_SECTOR_CD.add(rset.getString(8)==null?"":rset.getString(8));
				
				queryString1 = "SELECT TO_CHAR(EFF_DT,'DD/MM/YYYY'), DEMAND_SECT_CD, SUPPLY_SECT_CD "
						+ "FROM FMS_SECTOR_DTL A "
						+ "WHERE SECTOR_CD = ?"
						+ "AND EFF_DT NOT IN (SELECT MAX(EFF_DT) FROM FMS_SECTOR_DTL B WHERE A.SECTOR_CD = B.SECTOR_CD) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, sector_cd);
				rset1 = stmt1.executeQuery();
				while(rset1.next())
				{
					count++;
					VEFF_DT.add(rset1.getString(1)==null?"":rset1.getString(1));
					VDEMAND_SECTOR_CD.add(rset1.getString(2)==null?"":rset1.getString(2));
					VSUPPLY_SECTOR_CD.add(rset1.getString(3)==null?"":rset1.getString(3));
				}
				VINDEX.add(count);
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
	
	public void getRateComponentFlag()
	{
		String function_nm="getRateComponentFlag()";
		try
		{
			if(rate_mode.equals("EXCHANGE"))
			{
				queryString = "SELECT COMPONENT_FLAG "
						+ "FROM FMS_EXCHG_RATE_MST "
						+ "WHERE EXC_RATE_CD=? ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, component);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					component_flag=rset.getString(1)==null?"":rset.getString(1);
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
	
	public void getShipMst()
	{
		String function_nm="getShipMst()";
		try
		{
			queryString="SELECT SHIP_CD,SHIP_NAME,SHIP_CALL_SIGN,SHIP_FLAG,SHIP_IMO_NO,SHIP_CLASS_SOC,INMARSAT_NO,"
					+ "SHIP_OWNER_NAME,SHIP_OPERATOR_NAME,SHIP_FAX_NO,SHIP_TELEX_NO,SHIP_EMAIL,GROSS_TONNAGE,"
					+ "CARGO_CAPACITY,VOLUME_UNIT,PERCENTAGE_CAPACITY,SHIP_ITEM,ENT_BY,TO_CHAR(ENT_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(MODIFY_DT,'DD/MM/YYYY'),MODIFY_BY,TO_CHAR(EFF_DT,'DD/MM/YYYY') "
					+ "FROM FMS_SHIP_MST A "
					+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_SHIP_MST B WHERE A.SHIP_CD=B.SHIP_CD) "
					+ "ORDER BY SHIP_CD DESC";
			
			stmt=conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				 ship_cd =(rset.getString(1)==null?"":rset.getString(1));
				 ship_name =(rset.getString(2)==null?"":rset.getString(2));
				 ship_call_sign=(rset.getString(3)==null?"":rset.getString(3));
				 ship_flag=(rset.getString(4)==null?"":rset.getString(4));
				 ship_imo_no=(rset.getString(5)==null?"":rset.getString(5));
				 ship_class_soc=(rset.getString(6)==null?"":rset.getString(6));
				 inmarsat_no=(rset.getString(7)==null?"":rset.getString(7));
				 ship_owner_name=(rset.getString(8)==null?"":rset.getString(8));
				 ship_operator_name=(rset.getString(9)==null?"":rset.getString(9));
				 ship_fax_no=(rset.getString(10)==null?"":rset.getString(10));
				 ship_telex_no=(rset.getString(11)==null?"":rset.getString(11));
				 ship_email=(rset.getString(12)==null?"":rset.getString(12));
				 gross_tonnage=(rset.getString(13)==null?"":rset.getString(13));
				 cargo_capacity=(rset.getString(14)==null?"":rset.getString(14));
				 volume_unit=(rset.getString(15)==null?"":rset.getString(15));
				 percentage_capacity=(rset.getString(16)==null?"":rset.getString(16));
				 ship_item=(rset.getString(17)==null?"":rset.getString(17));
				 ship_eff_dt=(rset.getString(22)==null?"":rset.getString(22));
				 
				VSHIP_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VSHIP_NAME.add(rset.getString(2)==null?"":rset.getString(2));
				VSHIP_CALL_SIGN.add(rset.getString(3)==null?"":rset.getString(3));
				VSHIP_FLAG.add(rset.getString(4)==null?"":rset.getString(4));
				VSHIP_IMO_NO.add(rset.getString(5)==null?"":rset.getString(5));
				VSHIP_CLASS_SOC.add(rset.getString(6)==null?"":rset.getString(6));
				VINMARSAT_NO.add(rset.getString(7)==null?"":rset.getString(7));
				VSHIP_OWNER_NAME.add(rset.getString(8)==null?"":rset.getString(8));
				VSHIP_OPERATOR_NAME.add(rset.getString(9)==null?"":rset.getString(9));
				VSHIP_FAX_NO.add(rset.getString(10)==null?"":rset.getString(10));
				VSHIP_TELEX_NO.add(rset.getString(11)==null?"":rset.getString(11));
				VSHIP_EMAIL.add(rset.getString(12)==null?"":rset.getString(12));
				VGROSS_TONNAGE.add(rset.getString(13)==null?"":rset.getString(13));
				VCARGO_CAPACITY.add(rset.getString(14)==null?"":rset.getString(14));
				VVOLUME_UNIT.add(rset.getString(15)==null?"":rset.getString(15));
				VPERCENTAGE_CAPACITY.add(rset.getString(16)==null?"":rset.getString(16));
				VSHIP_ITEM.add(rset.getString(17)==null?"":rset.getString(17));
				VENT_BY.add(rset.getString(18)==null?"":rset.getString(18));
				VENT_DT.add(rset.getString(19)==null?"":rset.getString(19));
				VMODIFY_DT.add(rset.getString(20)==null?"":rset.getString(20));
				VMODIFY_BY.add(rset.getString(21)==null?"":rset.getString(21));
				VSHIP_EFF_DT.add(rset.getString(22)==null?"":rset.getString(22));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getProductMst()
	{
		String function_nm="getProductMst()";
		try
		{
			queryString="SELECT PROD_CD,PROD_NM,PROD_ABBR,PROD_DESC,PROD_FLAG "
					+ "FROM FMS_PRODUCT_MST A "
					+ "ORDER BY PROD_CD DESC";
			stmt=conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VPROD_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VPROD_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VPROD_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
				VPROD_DESC.add(rset.getString(4)==null?"":rset.getString(4));
				VPROD_FLAG.add(rset.getString(5)==null?"":rset.getString(5));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getProductActiveMst()
	{
		String function_nm="getProductActiveMst()";
		try
		{
			queryString="SELECT PROD_CD,PROD_NM,PROD_ABBR,PROD_DESC,PROD_FLAG "
					+ "FROM FMS_PRODUCT_MST A ";
					//+ "WHERE PROD_FLAG=? "
			stmt=conn.prepareStatement(queryString);
			//stmt.setString(1, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VPROD_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VPROD_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VPROD_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
				VPROD_DESC.add(rset.getString(4)==null?"":rset.getString(4));
				VPROD_FLAG.add(rset.getString(5)==null?"":rset.getString(5));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getProductMoleculeMst()
	{
		String function_nm="getProductMoleculeMst()";
		try
		{
			queryString="SELECT PROD_CD,MOLE_NM,MOLE_ABBR,MOLE_DESC,MOLE_FLAG,MOLE_CD "
					+ "FROM FMS_PRODUCT_MOLECULE_MST A "
					+ "ORDER BY MOLE_CD DESC";
			stmt=conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				
				String prod_cd = rset.getString(1)==null?"":rset.getString(1);
				
				String queryString1="SELECT PROD_NM,PROD_ABBR,PROD_DESC,PROD_FLAG "
						+ "FROM FMS_PRODUCT_MST A "
						+ "WHERE PROD_CD=? ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, prod_cd);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					VPROD_MOLE_NM.add(rset1.getString(1)==null?"":rset1.getString(1));
					VPROD_MOLE_CD.add(prod_cd);
				}
				rset1.close();
				stmt1.close();
				
				VMOLE_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VMOLE_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
				VMOLE_DESC.add(rset.getString(4)==null?"":rset.getString(4));
				VMOLE_FLAG.add(rset.getString(5)==null?"":rset.getString(5));
				VMOLE_CD.add(rset.getString(6)==null?"":rset.getString(6));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSacDtl()
	{
		String function_nm="getSacDtl()";
		try
		{
			String sac_cd ="";
			String sac_code ="";
			String sac_desc ="";
			String remarks ="";
			String sac_flag ="";
			queryString = "SELECT SAC_CD,SAC_CODE,SAC_DESC,REMARKS,SAC_FLAG "
					+ "FROM FMS_SAC_MST";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				 sac_cd = rset.getString(1)==null?"":rset.getString(1);
				 sac_code = rset.getString(2)==null?"":rset.getString(2);
				 sac_desc = rset.getString(3)==null?"":rset.getString(3);
				 remarks = rset.getString(4)==null?"":rset.getString(4);
				 sac_flag = rset.getString(5)==null?"":rset.getString(5);
				 
				 VSAC_CD.add(sac_cd);
				 VSAC_CODE.add(sac_code);
				 VSAC_DESC.add(sac_desc);
				 VSAC_REMARKS.add(remarks);
				 VSAC_FLAG.add(sac_flag);
			}
			rset.close();
			stmt.close();
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

	String rate_mode = "";
	String month = "";
	String year = "";
	String component = "";
	String currency_from = "";
	String currency_to = "";
	String entity= "";
	String tax_category= "";
	String filter_status="";
	String tax_app="";
	String pay_recv="";
	String tax_struct_cd="";

	String ship_cd="";
	String ship_name ="";
	String ship_call_sign="";
	String ship_flag="";
	String ship_imo_no="";
	String ship_class_soc="";
	String inmarsat_no="";
	String ship_owner_name="";
	String ship_operator_name="";
	String ship_fax_no="";
	String ship_telex_no="";
	String ship_email="";
	String gross_tonnage="";
	String cargo_capacity="";
	String volume_unit="";
	String percentage_capacity="";
	String ship_item="";
	String ship_eff_dt="";
	
	public void setRate_mode(String rate_mode) {this.rate_mode = rate_mode;}
	public void setMonth(String month) {this.month = month;}
	public void setYear(String year) {this.year = year;}
	public void setComponent(String component) {this.component = component;}
	public void setCurrency_from(String currency_from) {this.currency_from = currency_from;}
	public void setCurrency_to(String currency_to) {this.currency_to = currency_to;}
	public void setEntity(String entity) {this.entity = entity;}
	public void setTax_category(String tax_category) {this.tax_category = tax_category;}
	public void setFilter_status(String filter_status) {this.filter_status = filter_status;}
	public void setTax_app(String tax_app) {this.tax_app = tax_app;}
	public void setPay_recv(String pay_recv) {this.pay_recv = pay_recv;}
	public void setShip_cd(String ship_cd) {this.ship_cd = ship_cd;}
	public void setTax_struct_cd(String tax_struct_cd) {this.tax_struct_cd = tax_struct_cd;}
	
	Vector VBANK_NAME = new Vector();
	Vector VBANK_BRANCH = new Vector();
	Vector VBANK_CD = new Vector();
	Vector VEFF_DT = new Vector();
	Vector VBANK_ADDR = new Vector();
	Vector VBANK_CITY = new Vector();
	Vector VBANK_STATE = new Vector();
	Vector VBANK_STATE_CD = new Vector();
	Vector VBANK_COUNTRY = new Vector();
	Vector VBANK_PIN = new Vector();
	Vector VBANK_PHONE = new Vector();
	Vector VBANK_MOBILE = new Vector();
	Vector VBANK_ALT_PHONE = new Vector();
	Vector VBANK_FAX1 = new Vector();
	Vector VBANK_FAX2 = new Vector();
	Vector VBANK_EMAIL = new Vector();
	Vector VBANK_REMARKS = new Vector();
	Vector VBANK_IFSC_CD = new Vector();
	Vector VBANK_STATUS_FLAG = new Vector();
	
	Vector VSTAT_CD = new Vector();
	Vector VSTAT_NM = new Vector();
	Vector VSTAT_TYPE = new Vector();
	Vector VSTAT_STATUS = new Vector();
	Vector VSTAT_REMARK = new Vector();
	Vector VRATE_CD = new Vector();
	Vector VRATE_NM = new Vector();
	Vector VBANK_ABBR = new Vector();
	Vector VRATE_FLAG = new Vector();
	Vector VRATE_VALUE= new Vector();
	Vector VTO_CURRENCY= new Vector();
	Vector VFROM_CURRENCY= new Vector();
	Vector VRATE_REMARK= new Vector();
	Vector VRATE_EFF_DT= new Vector();
	Vector VCURRENCY_CD= new Vector();
	Vector VCURRENCY_ABR= new Vector();
	Vector VHOLIDAY_DT = new Vector();
	Vector VHOLIDAY_NM = new Vector();
	Vector VHOLIDAY_DAY = new Vector();
	Vector VHOLIDAY_FLAG = new Vector();
	Vector VHOLI_STATE_CD = new Vector();
	Vector VHOLI_STATE_NM = new Vector();
	Vector VYear = new Vector();
	Vector VSTATE_CD = new Vector();
	Vector VSTATE_NM = new Vector();

	Vector VCOUNTRY_CD = new Vector();
	Vector VCOUNTRY_NM = new Vector();
	
	Vector VCOLOR = new Vector();
	Vector VTAX_CD = new Vector();
	Vector VTAX_NM = new Vector();
	Vector VTAX_ALIAS_NM = new Vector();
	Vector VTAX_SHT_NM = new Vector();
	Vector VTAX_APP_DT = new Vector();
	Vector VTAX_STATUS = new Vector();
	Vector VTAX_STRUCT_CD = new Vector();
	Vector VTAX_STRUCT_NM = new Vector();
	Vector VDISP_TAX_STRUCT_NM = new Vector();
	Vector VTAX_STRUCT_APP_DT = new Vector();
	Vector VTAX_STRUCT_STATUS = new Vector();
	Vector VTAX_STRUCT_RMK = new Vector();
	Vector VTAX_CATEGORY = new Vector();
	Vector VTAX_CATEGORY_NM = new Vector();
	Vector VSAP_TAX_CODE = new Vector();
	Vector VSAP_GL = new Vector();
	Vector VCOUNT = new Vector();
	Vector VMASTER_TAX_CATEGORY = new Vector();
	Vector VMASTER_TAX_CATEGORY_NM = new Vector();
	Vector VPAY_RECV = new Vector();
	Vector VPAY_RECV_NM = new Vector();

	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VTRANSPORTER_CD = new Vector();
	Vector VTRANSPORTER_ABBR = new Vector();
	Vector VTRANSPORTER_PLANT_SEQ = new Vector();
	Vector VTRANSPORTER_PLANT_ABBR = new Vector();
	Vector VTRANSPORTER_STATUS = new Vector();

	Vector VMETER_SEQ = new Vector();
	Vector VMETER_ID = new Vector();
	Vector VMETER_REF = new Vector();
	Vector VSPECIFICATION = new Vector();
	Vector VNOTE = new Vector();
	Vector VSTATUS = new Vector();
	Vector VMODIFY_BY = new Vector();
	Vector VMODIFY_DT = new Vector();
	Vector VENT_BY = new Vector();
	Vector VENT_DT = new Vector();

	Vector VINDEX = new Vector();
	Vector VSUB_INDEX = new Vector();

	Vector VSECTOR_CD = new Vector();
	Vector VSECTOR_NAME	= new Vector();
	Vector VSECTOR_ABBR = new Vector();
	Vector VSECTOR_TYPE = new Vector();
	Vector VSECTOR_TYPE_NM = new Vector();
	Vector VSTATUS_FLAG = new Vector();
	Vector VDEMAND_SECTOR_CD = new Vector();
	Vector VSUPPLY_SECTOR_CD = new Vector();

	Vector VCOMPONENT_FLAG = new Vector();
	Vector VCOMPONENT1 = new Vector();
	Vector VCOMPONENT2 = new Vector();
	
	Vector VSHIP_CD = new Vector();
	Vector VSHIP_NAME = new Vector();
	Vector VSHIP_CALL_SIGN = new Vector();
	Vector VSHIP_FLAG = new Vector();
	Vector VSHIP_IMO_NO = new Vector();
	Vector VSHIP_CLASS_SOC = new Vector();
	Vector VINMARSAT_NO = new Vector();
	Vector VSHIP_OWNER_NAME = new Vector();
	Vector VSHIP_OPERATOR_NAME = new Vector();
	Vector VSHIP_FAX_NO = new Vector();
	Vector VSHIP_TELEX_NO = new Vector();
	Vector VSHIP_EMAIL = new Vector();
	Vector VGROSS_TONNAGE = new Vector();
	Vector VCARGO_CAPACITY = new Vector();
	Vector VVOLUME_UNIT = new Vector();
	Vector VPERCENTAGE_CAPACITY = new Vector();
	Vector VSHIP_ITEM = new Vector();
	Vector VSHIP_EFF_DT = new Vector();
	
	Vector VPROD_CD = new Vector();
	Vector VPROD_NM = new Vector();
	Vector VPROD_ABBR = new Vector();
	Vector VPROD_DESC = new Vector();
	Vector VPROD_FLAG = new Vector();
	
	Vector VPROD_MOLE_CD = new Vector();
	Vector VPROD_MOLE_NM = new Vector();
	Vector VMOLE_CD = new Vector();
	Vector VMOLE_NM = new Vector();
	Vector VMOLE_ABBR = new Vector();
	Vector VMOLE_DESC = new Vector();
	Vector VMOLE_FLAG = new Vector();
	
	Vector VSAC_CD = new Vector();		//Pratham Bhatt 20240925: for SAC 
	Vector VSAC_CODE = new Vector();		//Pratham Bhatt 20240925: for SAC 
	Vector VSAC_DESC = new Vector();		//Pratham Bhatt 20240925: for SAC 
	Vector VSAC_REMARKS = new Vector();		//Pratham Bhatt 20240925: for SAC 
	Vector VSAC_FLAG = new Vector();		//Pratham Bhatt 20240925: for SAC 
	
 	public Vector getVSTAT_CD() {return VSTAT_CD;}
	public Vector getVSTAT_NM() {return VSTAT_NM;}
	public Vector getVSTAT_TYPE() {return VSTAT_TYPE;}
	public Vector getVSTAT_STATUS() {return VSTAT_STATUS;}
	public Vector getVSTAT_REMARK() {return VSTAT_REMARK;}
	public Vector getVRATE_CD() {return VRATE_CD;}
	public Vector getVRATE_NM() {return VRATE_NM;}
	public Vector getVBANK_ABBR() {return VBANK_ABBR;}
	public Vector getVRATE_FLAG() {return VRATE_FLAG;}
	public Vector getVRATE_VALUE() {return VRATE_VALUE;}
	public Vector getVTO_CURRENCY() {return VTO_CURRENCY;}
	public Vector getVFROM_CURRENCY() {return VFROM_CURRENCY;}
	public Vector getVRATE_REMARK() {return VRATE_REMARK;}
	public Vector getVRATE_EFF_DT() {return VRATE_EFF_DT;}
	public Vector getVCURRENCY_CD() {return VCURRENCY_CD;}
	public Vector getVCURRENCY_ABR() {return VCURRENCY_ABR;}
	public Vector getVHOLI_STATE_CD() {return VHOLI_STATE_CD;}
	public Vector getVHOLI_STATE_NM() {return VHOLI_STATE_NM;}
	public Vector getVHOLIDAY_DT() {return VHOLIDAY_DT;}
	public Vector getVHOLIDAY_NM() {return VHOLIDAY_NM;}
	public Vector getVHOLIDAY_DAY() {return VHOLIDAY_DAY;}
	public Vector getVHOLIDAY_FLAG() {return VHOLIDAY_FLAG;}
	public Vector getVSTATE_CD() {return VSTATE_CD;}
	public Vector getVSTATE_NM() {return VSTATE_NM;}
	public Vector getVYear() {return VYear;}
	public Vector getVCOLOR() {return VCOLOR;}
	public Vector getVTAX_CD() {return VTAX_CD;}
	public Vector getVTAX_NM() {return VTAX_NM;}
	public Vector getVTAX_ALIAS_NM() {return VTAX_ALIAS_NM;}
	public Vector getVTAX_SHT_NM() {return VTAX_SHT_NM;}
	public Vector getVTAX_APP_DT() {return VTAX_APP_DT;}
	public Vector getVTAX_STATUS() {return VTAX_STATUS;}
	public Vector getVTAX_STRUCT_CD() {return VTAX_STRUCT_CD;}
	public Vector getVTAX_STRUCT_NM() {return VTAX_STRUCT_NM;}
	public Vector getVDISP_TAX_STRUCT_NM() {return VDISP_TAX_STRUCT_NM;}
	public Vector getVTAX_STRUCT_APP_DT() {return VTAX_STRUCT_APP_DT;}
	public Vector getVTAX_STRUCT_STATUS() {return VTAX_STRUCT_STATUS;}
	public Vector getVTAX_STRUCT_RMK() {return VTAX_STRUCT_RMK;}
	public Vector getVTAX_CATEGORY() {return VTAX_CATEGORY;}
	public Vector getVTAX_CATEGORY_NM() {return VTAX_CATEGORY_NM;}
	public Vector getVSAP_TAX_CODE() {return VSAP_TAX_CODE;}
	public Vector getVSAP_GL() {return VSAP_GL;}
	public Vector getVCOUNT() {return VCOUNT;}
	public Vector getVMASTER_TAX_CATEGORY() {return VMASTER_TAX_CATEGORY;}
	public Vector getVMASTER_TAX_CATEGORY_NM() {return VMASTER_TAX_CATEGORY_NM;}
	public Vector getVPAY_RECV() {return VPAY_RECV;}
	public Vector getVPAY_RECV_NM() {return VPAY_RECV_NM;}

	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVTRANSPORTER_CD() {return VTRANSPORTER_CD;}
	public Vector getVTRANSPORTER_STATUS() {return VTRANSPORTER_STATUS;}
	public Vector getVTRANSPORTER_ABBR() {return VTRANSPORTER_ABBR;}
	public Vector getVTRANSPORTER_PLANT_SEQ() {return VTRANSPORTER_PLANT_SEQ;}
	public Vector getVTRANSPORTER_PLANT_ABBR() {return VTRANSPORTER_PLANT_ABBR;}

	public Vector getVMETER_SEQ() {return VMETER_SEQ;}
	public Vector getVMETER_ID() {return VMETER_ID;}
	public Vector getVMETER_REF() {return VMETER_REF;}
	public Vector getVSPECIFICATION() {return VSPECIFICATION;}
	public Vector getVNOTE() {return VNOTE;}
	public Vector getVSTATUS() {return VSTATUS;}
	public Vector getVMODIFY_BY() {return VMODIFY_BY;}
	public Vector getVMODIFY_DT() {return VMODIFY_DT;}
	public Vector getVENT_BY() {return VENT_BY;}
	public Vector getVENT_DT() {return VENT_DT;}

	public Vector getVINDEX() {return VINDEX;}
	public Vector getVSUB_INDEX() {return VSUB_INDEX;}

	public Vector getVSECTOR_CD() {return VSECTOR_CD;}
	public Vector getVSECTOR_NAME() {return VSECTOR_NAME;}
	public Vector getVSECTOR_ABBR() {return VSECTOR_ABBR;}
	public Vector getVSECTOR_TYPE() {return VSECTOR_TYPE;}
	public Vector getVSECTOR_TYPE_NM() {return VSECTOR_TYPE_NM;}
	public Vector getVSTATUS_FLAG() {return VSTATUS_FLAG;}
	public Vector getVDEMAND_SECTOR_CD() {return VDEMAND_SECTOR_CD;}
	public Vector getVSUPPLY_SECTOR_CD() {return VSUPPLY_SECTOR_CD;}
	
	public Vector getVCOMPONENT_FLAG() {return VCOMPONENT_FLAG;}
	public Vector getVCOMPONENT1() {return VCOMPONENT1;}
	public Vector getVCOMPONENT2() {return VCOMPONENT2;}
	
	public Vector getVBANK_NAME() {return VBANK_NAME;}
	public Vector getVBANK_BRANCH() {return VBANK_BRANCH;}
	public Vector getVBANK_CD() {return VBANK_CD;}
	public Vector getVEFF_DT() {return VEFF_DT;}
	public Vector getVCOUNTRY_CD() {return VCOUNTRY_CD;}
	public Vector getVCOUNTRY_NM() {return VCOUNTRY_NM;}
	public Vector getVBANK_ADDR() {return VBANK_ADDR;}
	public Vector getVBANK_CITY() {return VBANK_CITY;}
	public Vector getVBANK_STATE() {return VBANK_STATE;}
	public Vector getVBANK_COUNTRY() {return VBANK_COUNTRY;	}
	public Vector getVBANK_PIN() {return VBANK_PIN;	}
	public Vector getVBANK_PHONE() {return VBANK_PHONE;}
	public Vector getVBANK_MOBILE() {return VBANK_MOBILE;}
	public Vector getVBANK_ALT_PHONE() {return VBANK_ALT_PHONE;	}
	public Vector getVBANK_FAX1() {return VBANK_FAX1;}
	public Vector getVBANK_FAX2() {return VBANK_FAX2;}
	public Vector getVBANK_EMAIL() {return VBANK_EMAIL;}
	public Vector getVBANK_REMARKS() {return VBANK_REMARKS;}
	public Vector getVBANK_IFSC_CD() {return VBANK_IFSC_CD;	}
	public Vector getVBANK_STATUS_FLAG() {return VBANK_STATUS_FLAG;	}
	public Vector getVBANK_STATE_CD() {return VBANK_STATE_CD;}
	
	String component_flag="";
	String last_eff_dt="";
	
	public String getComponent_flag() {return component_flag;}
	public String getLast_eff_dt() {return last_eff_dt;}
	
	public Vector getVSHIP_CD() {return VSHIP_CD;}
	public Vector getVSHIP_NAME() {return VSHIP_NAME;}
	public Vector getVSHIP_CALL_SIGN() {return VSHIP_CALL_SIGN;}
	public Vector getVSHIP_FLAG() {return VSHIP_FLAG;}
	public Vector getVSHIP_IMO_NO() {return VSHIP_IMO_NO;}
	public Vector getVSHIP_CLASS_SOC() {return VSHIP_CLASS_SOC;}
	public Vector getVINMARSAT_NO() {return VINMARSAT_NO;}
	public Vector getVSHIP_OWNER_NAME() {return VSHIP_OWNER_NAME;}
	public Vector getVSHIP_OPERATOR_NAME() {return VSHIP_OPERATOR_NAME;}
	public Vector getVSHIP_FAX_NO() {return VSHIP_FAX_NO;}
	public Vector getVSHIP_TELEX_NO() {return VSHIP_TELEX_NO;}
	public Vector getVSHIP_EMAIL() {return VSHIP_EMAIL;}
	public Vector getVGROSS_TONNAGE() {return VGROSS_TONNAGE;}
	public Vector getVCARGO_CAPACITY() {return VCARGO_CAPACITY;}
	public Vector getVVOLUME_UNIT() {return VVOLUME_UNIT;}
	public Vector getVPERCENTAGE_CAPACITY() {return VPERCENTAGE_CAPACITY;}
	public Vector getVSHIP_ITEM() {return VSHIP_ITEM;}
	public Vector getVSHIP_EFF_DT() {return VSHIP_EFF_DT;}

	public Vector getVPROD_CD() {return VPROD_CD;}
	public Vector getVPROD_NM() {return VPROD_NM;}
	public Vector getVPROD_ABBR() {return VPROD_ABBR;}
	public Vector getVPROD_DESC() {return VPROD_DESC;}
	public Vector getVPROD_FLAG() {return VPROD_FLAG;}

	public Vector getVPROD_MOLE_CD() {return VPROD_MOLE_CD;}
	public Vector getVPROD_MOLE_NM() {return VPROD_MOLE_NM;}
	public Vector getVMOLE_CD() {return VMOLE_CD;}
	public Vector getVMOLE_NM() {return VMOLE_NM;}
	public Vector getVMOLE_ABBR() {return VMOLE_ABBR;}
	public Vector getVMOLE_DESC() {return VMOLE_DESC;}
	public Vector getVMOLE_FLAG() {return VMOLE_FLAG;}
	
	public Vector getVSAC_FLAG() {return VSAC_FLAG;}	//Pratham Bhatt 20240925: for SAC
	public Vector getVSAC_CD() {return VSAC_CD;}	//Pratham Bhatt 20240925: for SAC
	public Vector getVSAC_CODE() {return VSAC_CODE;}	//Pratham Bhatt 20240925: for SAC
	public Vector getVSAC_DESC() {return VSAC_DESC;}	//Pratham Bhatt 20240925: for SAC
	public Vector getVSAC_REMARKS() {return VSAC_REMARKS;}	//Pratham Bhatt 20240925: for SAC
	
	public String getShip_cd() {return ship_cd;}
	public String getShip_name() {return ship_name;}
	public String getShip_call_sign() {return ship_call_sign;}
	public String getShip_flag() {return ship_flag;}
	public String getShip_imo_no() {return ship_imo_no;}
	public String getShip_class_soc() {return ship_class_soc;}
	public String getShip_owner_name() {return ship_owner_name;}
	public String getShip_operator_name() {return ship_operator_name;}
	public String getShip_fax_no() {return ship_fax_no;}
	public String getShip_telex_no() {return ship_telex_no;}
	public String getShip_email() {return ship_email;}
	public String getShip_item() {return ship_item;}
	public String getShip_eff_dt() {return ship_eff_dt;}
	public String getInmarsat_no() {return inmarsat_no;}
	public String getGross_tonnage() {return gross_tonnage;}
	public String getCargo_capacity() {return cargo_capacity;}
	public String getVolume_unit() {return volume_unit;}
	public String getPercentage_capacity() {return percentage_capacity;}
	
}
