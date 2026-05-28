package automation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Vector;

import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.SystemErrorLogger;

public class Auto_UtilBean 
{
	String db_src_file_name="Auto_UtilBean.java";
	
	PreparedStatement stmtement,stmtement1,stmtement2,stmtement3;
	ResultSet resultset,resultset1,resultset2,resultset3;
	//String query = "";
	//String query1="";
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	
	DateUtil utilDate = new DateUtil();
	
	public String getCompanyAbbr(Connection conn, String comp_cd)
	{
		String function_nm="getCompanyAbbr()";
		String name="";
		try
		{
			if(conn!=null)
			{
				String query = "SELECT COMPANY_ABBR "
						+ "FROM FMS_COMPANY_OWNER_MST A "
						+ "WHERE COMPANY_CD=? "
						+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COMPANY_OWNER_MST B WHERE A.COMPANY_CD=B.COMPANY_CD) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, comp_cd);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					name = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return name;
	}
		
	public String getCounterpartyABBR(Connection conn,String cd)
	{
		String function_nm="getCounterpartyABBR()";
		String abbr="";
		try
		{
			if(conn!=null)
			{
				String query = "SELECT COUNTERPARTY_ABBR "
						+ "FROM FMS_COUNTERPARTY_MST A "
						+ "WHERE COUNTERPARTY_CD=? "
						+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, cd);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					abbr = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return abbr;
	}
	
	public String getCounterpartyName(Connection conn,String cd)
	{
		String function_nm="getCounterpartyName()";
		String name="";
		try
		{
			if(conn!=null)
			{
				String query = "SELECT COUNTERPARTY_NM "
						+ "FROM FMS_COUNTERPARTY_MST A "
						+ "WHERE COUNTERPARTY_CD=? "
						+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, cd);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					name = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return name;
	}
	
	public String getDisplayDealMapping(String agmt, String agmt_rev, String cont, String cont_rev, String cont_type)
	{
		String function_nm="getDisplayDealMapping()";
		String dealMapping="";
		try
		{
			if(cont_type.equals("S"))
			{
				dealMapping=cont_type+""+agmt+"-"+cont;
			}
			else
			{
				dealMapping=cont_type+""+cont;
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return dealMapping;
	}
	
	public String getEmpName(Connection conn,String cd)
	{
		String function_nm="getEmpName()";
		String name="";
		try
		{
			if(conn!=null)
			{
				String query = "SELECT EMP_NM "
						+ "FROM FMS_EMP_MST "
						+ "WHERE EMP_CD=? ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, cd);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					name = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return name;
	}
	
	public String RateNumberFormat(double rate, String rate_unit)
	{
		String function_nm="RateNumberFormat()";
		String number="";
		try
		{
			if(rate_unit.equals("1"))
			{
				number = nf.format(rate);
			}
			else if(rate_unit.equals("2"))
			{
				number = nf2.format(rate);
			}
			else
			{
				number = nf.format(rate);
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return number;
	}
	
	public String getContVariableDCQ(Connection conn,String comp_cd,String counterparty_cd, String agmt_no, String cont_no, String cont_type, String date)
	{
		String function_nm="getContVariableDCQ()";
		String dcq="";
		try
		{
			if(conn!=null)
			{
				String query="SELECT DCQ "
						+ "FROM FMS_SUPPLY_CONT_DCQ_DTL "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND CONT_NO=? "
						+ "AND CONTRACT_TYPE=? AND STATUS='Y' "
						+ "AND FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND TO_DT>=TO_DATE(?,'DD/MM/YYYY') ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, comp_cd);
				stmtement.setString(2, counterparty_cd);
				stmtement.setString(3, agmt_no);
				stmtement.setString(4, cont_no);
				stmtement.setString(5, cont_type);
				stmtement.setString(6, date);
				stmtement.setString(7, date);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					dcq=resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();			
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return dcq;
	}
	
	public String getPurchaseContVariableDCQ(Connection conn,String comp_cd,String counterparty_cd, String agmt_no, String cont_no, String cont_type, String date)
	{
		String function_nm="getPurchaseContVariableDCQ()";
		String dcq="";
		try
		{
			if(conn!=null)
			{
				String query="SELECT DCQ "
						+ "FROM FMS_TRADER_CONT_DCQ_DTL "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND CONT_NO=? "
						+ "AND CONTRACT_TYPE=? AND STATUS='Y' "
						+ "AND FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND TO_DT>=TO_DATE(?,'DD/MM/YYYY') ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, comp_cd);
				stmtement.setString(2, counterparty_cd);
				stmtement.setString(3, agmt_no);
				stmtement.setString(4, cont_no);
				stmtement.setString(5, cont_type);
				stmtement.setString(6, date);
				stmtement.setString(7, date);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					dcq=resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return dcq;
	}
	
	public String getContVariableTAQ(Connection conn,String comp_cd,String counterparty_cd, String agmt_no, String cont_no, String cont_type, String date)
	{
		String function_nm="getContVariableTAQ()";
		String taq="";
		try
		{
			if(conn!=null)
			{
				String query="SELECT ASED_DCQ "
						+ "FROM FMS_MR_CONT_TAQ_DTL "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND CONT_NO=? "
						+ "AND CONTRACT_TYPE=? "
						+ "AND FROM_DT<=TO_DATE(?,'DD/MM/YYYY') AND TO_DT>=TO_DATE(?,'DD/MM/YYYY') ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, comp_cd);
				stmtement.setString(2, counterparty_cd);
				stmtement.setString(3, agmt_no);
				stmtement.setString(4, cont_no);
				stmtement.setString(5, cont_type);
				stmtement.setString(6, date);
				stmtement.setString(7, date);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					taq=resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return taq;
	}
	
	//SUPPLY ALLOCATION QTY CONTRACT AND DATE WISE
	public String getSupplyAllocationQty(Connection conn,String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String contract_type, String date)
	{
		String function_nm="getSupplyAllocationQty()";
		String qty="";
		try
		{
			if(conn!=null)
			{
				String query="SELECT SUM(QTY_MMBTU) "
		  				+ "FROM FMS_DAILY_ALLOCATION_DTL A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONTRACT_TYPE=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				stmtement = conn.prepareStatement(query);
	    		stmtement.setString(1, cont_no);
	    		stmtement.setString(2, agmt_no);
	    		stmtement.setString(3, comp_cd);
	    		stmtement.setString(4, counterparty_cd);
	    		stmtement.setString(5, contract_type);
	    		stmtement.setString(6, date);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					qty=nf.format(resultset.getDouble(1));
				}
				stmtement.close();
				resultset.close();
				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return qty;
	}
	
	//PURCHASE ALLOCATION QTY CONTRACT AND DATE WISE
	public String getPurchaseAllocationQty(Connection conn,String comp_cd, String counterparty_cd, String agmt_no, String cont_no, String contract_type, String date)
	{
		String function_nm="getPurchaseAllocationQty()";
		String qty="";
		try
		{
			if(conn!=null)
			{
				String query="SELECT SUM(QTY_MMBTU) "
		  				+ "FROM FMS_BUY_DAILY_ALLOCATION A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONTRACT_TYPE=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND A.CARGO_NO=B.CARGO_NO) ";
				stmtement = conn.prepareStatement(query);
	    		stmtement.setString(1, cont_no);
	    		stmtement.setString(2, agmt_no);
	    		stmtement.setString(3, comp_cd);
	    		stmtement.setString(4, counterparty_cd);
	    		stmtement.setString(5, contract_type);
	    		stmtement.setString(6, date);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					qty=nf.format(resultset.getDouble(1));
				}
				stmtement.close();
				resultset.close();
				
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return qty;
	}
	
	public String getCcMailReceipentList(Connection conn, String comp_cd,String menu_nm, String module_nm, String rpt_freq, String gen_type)
    {
		String function_nm="getCcMailReceipentList()";
    	String recipient_list="";
    	try
    	{
    		if(conn!=null)
			{
    			String shotDyNm=utilDate.getShortDaysName();
				
				String query1="SELECT D.EMAIL_ID "
	    				+ "FROM FMS_EMAIL_SUPPORT_MST A, FMS_EMAIL_RECIPIENT_MST B, FMS_EMAIL_RECIPIENT_DTL C, FMS_EMP_MST D "
	    				+ "WHERE UPPER(A.MODULE_NAME)=? AND UPPER(A.MENU_NAME) LIKE ? AND UPPER(A.REPORT_FREQ)=? AND UPPER(A.GENERATION_TYPE)=? AND A.SUPPORT_FLAG=? "
	    				+ "AND B.COMPANY_CD=? AND B.STOP_FLAG=? AND UPPER(C.ENABLE_DISABLE)=? AND D.EMP_STATUS=? AND D.EMAIL_ID IS NOT NULL "
	    				+ "AND C.CC_EMP_CD IS NOT NULL AND C.CC_EMP_CD != ? "
	    				+ "AND (CASE WHEN UPPER(A.REPORT_FREQ)='DAILY' OR UPPER(A.REPORT_FREQ)='WEEKLY' THEN "+shotDyNm.trim().toUpperCase()+" ELSE 'Y' END)='Y' "
	    				+ "AND A.MODULE_NAME=B.MODULE_NAME AND A.MENU_NAME=B.MENU_NAME AND A.REPORT_FREQ=B.REPORT_FREQ AND A.GENERATION_TYPE=B.GENERATION_TYPE "
	    				+ "AND B.RECIPIENTS_CD=C.RECIPIENTS_CD AND C.CC_EMP_CD=D.EMP_CD "
	    				+ "UNION ALL "
	    				+ "SELECT C.CC_EMAIL "
	    				+ "FROM FMS_EMAIL_SUPPORT_MST A, FMS_EMAIL_RECIPIENT_MST B, FMS_EMAIL_RECIPIENT_DTL C "
	    				+ "WHERE UPPER(A.MODULE_NAME)=? AND UPPER(A.MENU_NAME) LIKE ? AND UPPER(A.REPORT_FREQ)=? AND UPPER(A.GENERATION_TYPE)=? AND A.SUPPORT_FLAG=? "
	    				+ "AND B.COMPANY_CD=? AND B.STOP_FLAG=? AND UPPER(C.ENABLE_DISABLE)=? AND C.CC_EMP_CD = ? AND C.CC_EMAIL IS NOT NULL "
	    				+ "AND (CASE WHEN UPPER(A.REPORT_FREQ)='DAILY' OR UPPER(A.REPORT_FREQ)='WEEKLY' THEN "+shotDyNm.trim().toUpperCase()+" ELSE 'Y' END)='Y' "
	    				+ "AND A.MODULE_NAME=B.MODULE_NAME AND A.MENU_NAME=B.MENU_NAME AND A.REPORT_FREQ=B.REPORT_FREQ AND A.GENERATION_TYPE=B.GENERATION_TYPE "
	    				+ "AND B.RECIPIENTS_CD=C.RECIPIENTS_CD ";
	    		int st_count=0;
	    		String temp_query=query1;
	    		stmtement = conn.prepareStatement(temp_query);
	    		stmtement.setString(++st_count, module_nm.toUpperCase());
	    		stmtement.setString(++st_count, menu_nm.toUpperCase());
	    		stmtement.setString(++st_count, rpt_freq.toUpperCase());
	    		stmtement.setString(++st_count, gen_type.toUpperCase());
	    		stmtement.setString(++st_count, "Y");
	    		stmtement.setString(++st_count, comp_cd);
	    		stmtement.setString(++st_count, "N");
	    		stmtement.setString(++st_count, "Y");
	    		stmtement.setString(++st_count, "Y");
	    		stmtement.setString(++st_count, "0");
	    		stmtement.setString(++st_count, module_nm.toUpperCase());
	    		stmtement.setString(++st_count, menu_nm.toUpperCase());
	    		stmtement.setString(++st_count, rpt_freq.toUpperCase());
	    		stmtement.setString(++st_count, gen_type.toUpperCase());
	    		stmtement.setString(++st_count, "Y");
	    		stmtement.setString(++st_count, comp_cd);
	    		stmtement.setString(++st_count, "N");
	    		stmtement.setString(++st_count, "Y");
	    		stmtement.setString(++st_count, "0");
				resultset = stmtement.executeQuery();
				while(resultset.next())
				{
					String email=resultset.getString(1)==null?"":resultset.getString(1);
					recipient_list += !recipient_list.equals("")?","+email:email;
				}
				stmtement.close();
				resultset.close();
			}
    	}
    	catch(Exception e)
    	{
    		new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
    	}
    	return recipient_list;
    }
	
	public String getToMailReceipentList(Connection conn, String comp_cd, String menu_nm, String module_nm, String rpt_freq, String gen_type)
    {
		String function_nm="getToMailReceipentList()";
    	String recipient_list="";
    	try
    	{
    		if(conn!=null)
			{
    			String shotDyNm=utilDate.getShortDaysName();
    			
    			String query1="SELECT D.EMAIL_ID "
	    				+ "FROM FMS_EMAIL_SUPPORT_MST A, FMS_EMAIL_RECIPIENT_MST B, FMS_EMAIL_RECIPIENT_DTL C, FMS_EMP_MST D "
	    				+ "WHERE UPPER(A.MODULE_NAME)=? AND UPPER(A.MENU_NAME) LIKE ? AND UPPER(A.REPORT_FREQ)=? AND UPPER(A.GENERATION_TYPE)=? AND A.SUPPORT_FLAG=? "
	    				+ "AND B.COMPANY_CD=? AND B.STOP_FLAG=? AND UPPER(C.ENABLE_DISABLE)=? AND D.EMP_STATUS=? AND D.EMAIL_ID IS NOT NULL "
	    				+ "AND C.TO_EMP_CD IS NOT NULL AND C.TO_EMP_CD != ? "
	    				+ "AND (CASE WHEN UPPER(A.REPORT_FREQ)='DAILY' OR UPPER(A.REPORT_FREQ)='WEEKLY' THEN "+shotDyNm.trim().toUpperCase()+" ELSE 'Y' END)='Y' "
	    				+ "AND A.MODULE_NAME=B.MODULE_NAME AND A.MENU_NAME=B.MENU_NAME AND A.REPORT_FREQ=B.REPORT_FREQ AND A.GENERATION_TYPE=B.GENERATION_TYPE "
	    				+ "AND B.RECIPIENTS_CD=C.RECIPIENTS_CD AND C.TO_EMP_CD=D.EMP_CD "
	    				+ "UNION ALL "
	    				+ "SELECT C.TO_EMAIL "
	    				+ "FROM FMS_EMAIL_SUPPORT_MST A, FMS_EMAIL_RECIPIENT_MST B, FMS_EMAIL_RECIPIENT_DTL C "
	    				+ "WHERE UPPER(A.MODULE_NAME)=? AND UPPER(A.MENU_NAME) LIKE ? AND UPPER(A.REPORT_FREQ)=? AND UPPER(A.GENERATION_TYPE)=? AND A.SUPPORT_FLAG=? "
	    				+ "AND B.COMPANY_CD=? AND B.STOP_FLAG=? AND UPPER(C.ENABLE_DISABLE)=? AND C.TO_EMP_CD = ? AND C.TO_EMAIL IS NOT NULL "
	    				+ "AND (CASE WHEN UPPER(A.REPORT_FREQ)='DAILY' OR UPPER(A.REPORT_FREQ)='WEEKLY' THEN "+shotDyNm.trim().toUpperCase()+" ELSE 'Y' END)='Y' "
	    				+ "AND A.MODULE_NAME=B.MODULE_NAME AND A.MENU_NAME=B.MENU_NAME AND A.REPORT_FREQ=B.REPORT_FREQ AND A.GENERATION_TYPE=B.GENERATION_TYPE "
	    				+ "AND B.RECIPIENTS_CD=C.RECIPIENTS_CD ";
	    		int st_count=0;
	    		String temp_query=query1;
	    		stmtement = conn.prepareStatement(temp_query);
	    		stmtement.setString(++st_count, module_nm.toUpperCase());
	    		stmtement.setString(++st_count, menu_nm.toUpperCase());
	    		stmtement.setString(++st_count, rpt_freq.toUpperCase());
	    		stmtement.setString(++st_count, gen_type.toUpperCase());
	    		stmtement.setString(++st_count, "Y");
	    		stmtement.setString(++st_count, comp_cd);
	    		stmtement.setString(++st_count, "N");
	    		stmtement.setString(++st_count, "Y");
	    		stmtement.setString(++st_count, "Y");
	    		stmtement.setString(++st_count, "0");
	    		stmtement.setString(++st_count, module_nm.toUpperCase());
	    		stmtement.setString(++st_count, menu_nm.toUpperCase());
	    		stmtement.setString(++st_count, rpt_freq.toUpperCase());
	    		stmtement.setString(++st_count, gen_type.toUpperCase());
	    		stmtement.setString(++st_count, "Y");
	    		stmtement.setString(++st_count, comp_cd);
	    		stmtement.setString(++st_count, "N");
	    		stmtement.setString(++st_count, "Y");
	    		stmtement.setString(++st_count, "0");
				resultset = stmtement.executeQuery();
				while(resultset.next())
				{
					String email=resultset.getString(1)==null?"":resultset.getString(1);
					recipient_list += !recipient_list.equals("")?","+email:email;
				}
				stmtement.close();
				resultset.close();
			}
    	}
    	catch(Exception e)
    	{
    		new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
    	}
    	return recipient_list;
    }
	
	public String getAutomationKeyDetail(Connection conn, String key_nm)
	{
		String function_nm="getAutomationKeyDetail()";
		String key_value="";
		try
		{
			if(conn!=null)
			{
				String query = "SELECT KEY_VALUE "
						+ "FROM FMS_AUTOMATION_KEY "
						+ "WHERE KEY_NM = ?";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, key_nm);
				resultset = stmtement.executeQuery();
				while(resultset.next())
				{
					key_value=resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return key_value;
	}
	
	public String getBankName(Connection conn,String cd)
	{
		String function_nm="getBankName()";
		String name="";
		try
		{
			if(conn!=null)
			{
				String query="SELECT BANK_NAME "
						+ "FROM FMS_BANK_MST A "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "BANK_CD=? "
						+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_BANK_MST B "
						+ "WHERE A.BANK_CD=B.BANK_CD) ";
				stmtement = conn.prepareStatement(query);
				//stmtement.setString(1, comp_cd);
				stmtement.setString(1, cd);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					name = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return name;
	}
	
	public String getBankABBR(Connection conn,String cd)
	{
		String function_nm="getBankABBR()";
		String abbr="";
		try
		{
			if(conn!=null)
			{
				String query="SELECT BANK_ABBR "
						+ "FROM FMS_BANK_MST A "
						+ "WHERE "//COMPANY_CD=? AND "
						+ "BANK_CD=? "
						+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_BANK_MST B "
						+ "WHERE A.BANK_CD=B.BANK_CD) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, cd);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					abbr = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return abbr;
	}
	public String getGasExchangeName(Connection conn,String cd)
	{
		String function_nm="getGasExchangeName()";
		String name="";
		try
		{
			if(conn!=null)
			{
				String query = "SELECT COUNTERPARTY_NM "
						+ "FROM FMS_COMPANY_EXCHG_MST A "
						+ "WHERE COUNTERPARTY_CD=? "
						+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COMPANY_EXCHG_MST B "
						+ "WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, cd);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					name = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return name;
	}
	
	public String getGasExchangeCd(Connection conn,String abbr)
	{
		String function_nm="getGasExchangeCd()";
		String name="";
		try
		{
			if(conn!=null)
			{
				String query = "SELECT COUNTERPARTY_CD "
						+ "FROM FMS_COMPANY_EXCHG_MST A "
						+ "WHERE COUNTERPARTY_ABBR=? "
						+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COMPANY_EXCHG_MST B "
						+ "WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, abbr);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					name = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return name;
	}
	
	public String getGasExchangeAbbr(Connection conn,String cd)
	{
		String function_nm="getGasExchangeAbbr()";
		String abbr="";
		try
		{
			if(conn!=null)
			{
				String query = "SELECT COUNTERPARTY_ABBR "
						+ "FROM FMS_COMPANY_EXCHG_MST A "
						+ "WHERE COUNTERPARTY_CD=? "
						+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COMPANY_EXCHG_MST B "
						+ "WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, cd);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					abbr = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return abbr;
	}

	public String getCounterpartyPlantABBR(Connection conn,String counterparty_cd, String comp_cd, String plant_seq, String entity)
	{
		String function_nm="getCounterpartyPlantABBR()";
		String abbr="";
		try
		{
			if(conn!=null)
			{
				String query = "SELECT PLANT_ABBR "
						+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
						+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND COMPANY_CD=? AND SEQ_NO=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, counterparty_cd);
				stmtement.setString(2, entity);
				stmtement.setString(3, comp_cd);
				stmtement.setString(4, plant_seq);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					abbr=resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return abbr;
	}

	public String getCounterpartyBuPlantABBR(Connection conn,String counterparty_cd, String comp_cd, String plant_seq, String entity)
	{
		String function_nm="getCounterpartyBuPlantABBR()";
		String abbr="";
		try
		{
			if(conn!=null)
			{
				String query = "SELECT PLANT_ABBR "
						+ "FROM FMS_COUNTERPARTY_BU_DTL A "
						+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND COMPANY_CD=? AND SEQ_NO=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_BU_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, counterparty_cd);
				stmtement.setString(2, entity);
				stmtement.setString(3, comp_cd);
				stmtement.setString(4, plant_seq);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					abbr=resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return abbr;
	}

	public String getCounterpartyPlantName(Connection conn,String counterparty_cd, String comp_cd, String plant_seq, String entity)
	{
		String function_nm="getCounterpartyPlantName()";
		String abbr="";
		try
		{
			if(conn!=null)
			{
				String query = "SELECT PLANT_NAME "
						+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
						+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND COMPANY_CD=? AND SEQ_NO=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, counterparty_cd);
				stmtement.setString(2, entity);
				stmtement.setString(3, comp_cd);
				stmtement.setString(4, plant_seq);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					abbr=resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return abbr;
	}

	public String getRateUnitNm(Connection conn,String  cd)
	{
		String function_nm="getRateUnitNm()";
		String name="";
		try
		{
			if(conn!=null)
			{
				String query = "SELECT RATE_UNIT_ABR "
						+ "FROM FMS_RATE_UNIT "
						+ "WHERE RATE_UNIT_CD=? ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, cd);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					name = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return name;
	}
	
	public String DueDateCalculation(Connection conn,String inv_date,String days,String type,String exclude_sat,String comp_cd,String state_tin)
	{
		String function_nm="DueDateCalculation()";
		String date="";
		String day_nms="";
		
		if(state_tin.length()==1)
		{
			state_tin="0"+state_tin;
		}
		if(exclude_sat.equals("Y"))
		{
			day_nms="'SAT','SUN'";
		}
		else
		{
			day_nms="'SUN'";
		}
		try
		{
			if(conn!=null)
			{
				if(type.equals("C")) //CALENDAR DAYS
				{
					if(!days.equals(""))
					{
						int day=Integer.parseInt(days);
						for(int i=1; i <=day; i++)
		    			{
							String query="SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY')+?,'DD/MM/YYYY') "
									+ "FROM DUAL";
							stmtement = conn.prepareStatement(query);
							stmtement.setString(1, inv_date);
							stmtement.setInt(2, i);
							resultset = stmtement.executeQuery();
							if(resultset.next())
							{
								date = resultset.getString(1)==null?"":resultset.getString(1);
								
								int count=0;
								String query1 = "SELECT COUNT(*) "
										+ "FROM DUAL "
										+ "WHERE TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'DY') IN (?) ";
								stmtement1 = conn.prepareStatement(query1);
								stmtement1.setString(1, date);
								stmtement1.setString(2, day_nms);
								resultset1=stmtement1.executeQuery();
								if(resultset1.next())
								{
									count = resultset1.getInt(1);
									if(count == 1)
									{
										day = day + 1; 
									}
								}
								stmtement1.close();
								resultset1.close();
							}
							stmtement.close();
							resultset.close();
		    			}
					}
				}
				else if(type.equals("B")) //BUSINESS DAY
				{
					if(!days.equals(""))
					{
						int day=Integer.parseInt(days);
						for(int i=1; i <=day; i++)
		    			{
							String query="SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY')+?,'DD/MM/YYYY') "
									+ "FROM DUAL";
							stmtement = conn.prepareStatement(query);
							stmtement.setString(1, inv_date);
							stmtement.setInt(2, i);
							resultset = stmtement.executeQuery();
							if(resultset.next())
							{
								date = resultset.getString(1)==null?"":resultset.getString(1);
								
								int count=0;
								
								String query1="SELECT COUNT(*) "
										+ "FROM FMS_HOLIDAY_DTL "
										+ "WHERE COMPANY_CD=? AND HOLIDAY_DT=TO_DATE(?,'DD/MM/YYYY') "
										+ "AND FLAG='Y' AND STATE_TIN=?";
								stmtement1 = conn.prepareStatement(query1);
								stmtement1.setString(1, comp_cd);
								stmtement1.setString(2, date);
								stmtement1.setString(3, state_tin);
								resultset1=stmtement1.executeQuery();
								if(resultset1.next())
								{
									count = resultset1.getInt(1);
									if(count == 1)
									{
										day = day + 1; 
									}
								}
								stmtement1.close();
								resultset1.close();
								if(count==0)
								{
									query1 = "SELECT COUNT(*) "
											+ "FROM DUAL "
											+ "WHERE TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'DY') IN (?) ";
									stmtement1 = conn.prepareStatement(query1);
									stmtement1.setString(1, date);
									stmtement1.setString(2, day_nms);
									resultset1=stmtement1.executeQuery();
									if(resultset1.next())
									{
										count = resultset1.getInt(1);
										if(count == 1)
										{
											day = day + 1; 
										}
									}
									stmtement1.close();
									resultset1.close();
								}
							}
							stmtement.close();
							resultset.close();
		    			}
					}
				}
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return date;
	}
	
	public String getInvoicePrefix(Connection conn,String comp_cd)
	{
		String function_nm="getInvoicePrefix()";
		String inv_prefix="";
		try
		{
			if(conn!=null)
			{
				String query="SELECT INVOICE_PREFIX "
						+ "FROM FMS_COMPANY_OWNER_MST A "
						+ "WHERE COMPANY_CD=? AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COMPANY_OWNER_MST B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD)";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, comp_cd);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					inv_prefix = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return inv_prefix;
	}
	
	public String PrePaddingZero(String code, int numLen)
	{
		String function_nm="PrePaddingZero()";
		String final_code="";
		try
		{
			final_code=code;
			int number = numLen-code.length();
			for(int i=0;i<number;i++)
			{
				final_code="0"+final_code;
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return final_code;
	}
	
	public String getCounterpartySAPcode(Connection conn,String cd, String comp_cd)
	{
		String function_nm="getCounterpartySAPcode()";
		String name="";
		try
		{
			if(conn!=null)
			{
				String query = "SELECT SAP_CODE "
						+ "FROM FMS_COUNTERPARTY_MST A "
						+ "WHERE COUNTERPARTY_CD=? AND COMPANY_CD=? "
						+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.COMPANY_CD=B.COMPANY_CD) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, cd);
				stmtement.setString(2, comp_cd);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					name = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return name;
	}
	
	public String getCounterpartyBuABBR(Connection conn,String counterparty_cd, String comp_cd, String plant_seq, String entity)
	{
		String function_nm="getCounterpartyBuABBR()";
		String abbr="";
		try
		{
			if(conn!=null)
			{
				String query = "SELECT PLANT_ABBR "
						+ "FROM FMS_COUNTERPARTY_BU_DTL A "
						+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND COMPANY_CD=? AND SEQ_NO=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_BU_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, counterparty_cd);
				stmtement.setString(2, entity);
				stmtement.setString(3, comp_cd);
				stmtement.setString(4, plant_seq);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					abbr=resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return abbr;
	}
	
	public String getGasExchangeSAPcode(Connection conn,String cd, String comp_cd)
	{
		String function_nm="getGasExchangeSAPcode()";
		String name="";
		try
		{
			if(conn!=null)
			{
				String query = "SELECT SAP_CODE "
						+ "FROM FMS_COMPANY_EXCHG_MST A "
						+ "WHERE COUNTERPARTY_CD=? AND COMPANY_CD=? "
						+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COMPANY_EXCHG_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.COMPANY_CD=B.COMPANY_CD) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, cd);
				stmtement.setString(2, comp_cd);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					name = resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return name;
	}
	
	public String NewDealMappingId_bkp(String comp_cd, String counterparty_cd,String agmt, String agmt_rev, String cont, String cont_rev, String cont_type, String cargo_no)
	{
		String function_nm="NewDealMappingId()";
		String map="";
		try
		{
			if(cont_type.equals("S") || (cont_type.equals("N") && (cargo_no.equals("") || cargo_no.equals("0"))) || cont_type.equals("C") || cont_type.equals("R"))
			{
				map=comp_cd+cont_type+counterparty_cd+"-"+agmt+"-"+cont;
			}
			else if(cont_type.equals("N")) 
			{
				map=comp_cd+cont_type+counterparty_cd+"-"+agmt+"-"+cont+"-"+cargo_no;
			}
			else if(cont_type.equals("D") || cont_type.equals("I") || cont_type.equals("L") || cont_type.equals("X"))
			{
				map=comp_cd+cont_type+counterparty_cd+"-"+cont;
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return map;
	}
	
	// This is for Display only
	public String NewDealMappingId(String comp_cd, String counterparty_cd,String agmt, String agmt_rev, String cont, String cont_rev, String cont_type, String cargo_no)
	{
		String function_nm="NewDealMappingId()";
		String map="";
		String entity_type = "";
		
		try
		{		
			// Set Entity Type
			if(cont_type.equals("S") || cont_type.equals("L") || cont_type.equals("X") || cont_type.equals("Q") || cont_type.equals("O") || cont_type.equals("F") || cont_type.equals("E") || cont_type.equals("W") || cont_type.equals("B") || cont_type.equals("M"))
			{
				entity_type = "C";
			}
			else if(cont_type.equals("D") || cont_type.equals("T") || cont_type.equals("N") || cont_type.equals("I") || cont_type.equals("G") || cont_type.equals("P"))
			{
				entity_type = "T";
			}
			else if(cont_type.equals("C") || cont_type.equals("R"))
			{
				entity_type = "R";
			}	
			else if(cont_type.equals("H"))
			{
				entity_type = "H";
			}	
			else if(cont_type.equals("Y"))
			{
				entity_type = "S";
			}	
			else if(cont_type.equals("A"))
			{
				entity_type = "V";
			}
			else if(cont_type.equals("Z"))
			{
				entity_type = "Z";
			}
			
			// Set Mapping		
			if(cont_type.equals("S") || (cont_type.equals("N")  && (cargo_no.equals("") || cargo_no.equals("0"))) 
					|| (cont_type.equals("P") && (cargo_no.equals("") || cargo_no.equals("0")))
					|| (cont_type.equals("G") && (cargo_no.equals("") || cargo_no.equals("0")))
					|| (cont_type.equals("O") && (cargo_no.equals("") || cargo_no.equals("0")))//LTCORA Sell CN
					|| (cont_type.equals("Q") && (cargo_no.equals("") || cargo_no.equals("0")))//LTCORA Sell Period
					|| cont_type.equals("C") || cont_type.equals("R"))
			{
				//map=comp_cd+entity_type+counterparty_cd+"-"+cont_type+""+agmt+"-"+cont;
				map=comp_cd+entity_type+counterparty_cd+""+cont_type+""+agmt+"-"+cont; // Adjusted for SAP 12 digit Limitation
			}
			else if(cont_type.equals("N"))
			{
				//map=comp_cd+entity_type+counterparty_cd+"-"+cont_type+""+agmt+"-"+cont+"-"+cargo_no;
				map=cont_type+""+agmt+"-"+cont+"-"+cargo_no;// Adjusted for SAP 12 digit Limitation
			}					
			else if(cont_type.equals("G") || cont_type.equals("P") || cont_type.equals("Q") || cont_type.equals("O")  )
			{
				//map=comp_cd+entity_type+counterparty_cd+"-"+cont_type+""+agmt+"-"+cont+"-"+cargo_no;
				map=cont_type+""+agmt+"-"+cont+"-"+cargo_no;// Adjusted for SAP 12 digit Limitation
			}
			else if(cont_type.equals("D") || cont_type.equals("T") || cont_type.equals("I") || cont_type.equals("L") || cont_type.equals("X")
					|| cont_type.equals("H") || cont_type.equals("Y")|| cont_type.equals("A") || cont_type.equals("F") || cont_type.equals("E") || cont_type.equals("W") || cont_type.equals("B") || cont_type.equals("M"))
			{
				//map=comp_cd+entity_type+counterparty_cd+"-"+cont_type+""+cont;
				map=comp_cd+entity_type+counterparty_cd+""+cont_type+""+cont; // Adjusted for SAP 12 digit Limitation
			}
			else if(cont_type.equals("Z")) // This is for Storage
			{
				//map=comp_cd+entity_type+counterparty_cd+"-"+cont_type+""+cont;
				map=comp_cd+entity_type+counterparty_cd+""+cont_type+""+cont;// Adjusted for SAP 12 digit Limitation
			}		
										
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return map;
	}
	
	public void getChargeMaster(Connection conn)
	{
		String function_nm="getChargeMaster()";
		try
		{
			if(conn != null)
			{
				String query="SELECT CHARGE_NAME,CHARGE_ABBR "
						+ "FROM FMS_CHARGE_MST ";
				stmtement = conn.prepareStatement(query);
				resultset = stmtement.executeQuery();
				while(resultset.next())
				{
					CHARGE_NAME.add(resultset.getString(1)==null?"":resultset.getString(1));
					CHARGE_ABBR.add(resultset.getString(2)==null?"":resultset.getString(2));
				}
				stmtement.close();
				resultset.close();
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public String getFirstDtOfBillingCycle(String billing_frq,String days,String date)
	{
		String function_nm="getFirstDtOfBillingCycle()";
		String period_start_dt="";
		String period_end_dt="";
		try
		{
			String[] split=date.split("/");
			String month=split[1];
			String year=split[2];
			
			if(billing_frq.equals("F"))
			{	
				int count=utilDate.getDays(date, "15/"+month+"/"+year);
				int count1=utilDate.getDays(date, ""+utilDate.getLastDateOfMonth(month, year));
				
				if(count <= 1)
				{
					period_start_dt= "01/"+month+"/"+year;
				}
				else if(count1 <= 1)
				{
					period_start_dt= "16/"+month+"/"+year;
				}
			}
			else if(billing_frq.equals("W"))
			{
				int count=utilDate.getDays(date, "07/"+month+"/"+year);
				int count1=utilDate.getDays(date, "14/"+month+"/"+year);
				int count2=utilDate.getDays(date, "21/"+month+"/"+year);
				int count3=utilDate.getDays(date, "28/"+month+"/"+year);
				int count4=utilDate.getDays(date, ""+utilDate.getLastDateOfMonth(month, year));
				
				if(count <= 1)
				{
					period_start_dt= "01/"+month+"/"+year;
				}
				else if(count1 <= 1)
				{
					period_start_dt= "08/"+month+"/"+year;
				}
				else if(count2 <= 1)
				{
					period_start_dt= "15/"+month+"/"+year;
				}
				else if(count3 <= 1)
				{
					period_start_dt= "22/"+month+"/"+year;
				}
				else if(count4 <= 1)
				{
					if(month.equals("02"))
					{
						int noofdays=utilDate.getDays(""+utilDate.getLastDateOfMonth(month, year), ""+utilDate.getFirstDateOfMonth(month, year));
						if(noofdays==29)
						{
							period_start_dt="29/"+month+"/"+year;
						}
						/*else
						{
							period_start_dt=""+utilDate.getLastDateOfMonth(month, year);
						}*/
					}
					else
					{
						period_start_dt="29/"+month+"/"+year;
					}
				}
			}
			else if(billing_frq.equals("M"))
			{
				int count=utilDate.getDays(date, ""+utilDate.getLastDateOfMonth(month, year));
				
				if(count <= 1)
				{
					period_start_dt= "01/"+month+"/"+year;
				}
			}
			
			if(period_start_dt.equals(""))
			{
				period_start_dt=date;
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return period_start_dt;
	}
	
	public String  getContractTypeName(String cont_type)
	{
		String function_nm="getContractTypeName()";
		String nm="";
		try
		{			
			if(cont_type.equals("S"))
			{
				nm="Supply Notice";
			}
			else if(cont_type.equals("L")) 
			{
				nm="LoA";
			}
			else if(cont_type.equals("X")) 
			{
				nm="IGX(Sell)";
			} 
			else if(cont_type.equals("F")) 
			{
				nm="DLNG Supply Notice";
			}
			else if(cont_type.equals("E")) 
			{
				nm="DLNG LoA";
			}
			else if(cont_type.equals("W")) 
			{
				nm="DLNG IGX(Sell)";
			}
			else if(cont_type.equals("I")) 
			{
				nm="IGX(Buy)";
			} 
			else if(cont_type.equals("D")) 
			{
				nm="Domestic NG";
			}
			else if(cont_type.equals("T")) 
			{
				nm="In Tank LNG/RLNG";
			}
			else if(cont_type.equals("N"))
			{
				nm="LNG Cargo";
			}	
			else if(cont_type.equals("G")) 
			{
				nm="LTCORA(Buy) CN";
			} 
			else if(cont_type.equals("P"))
			{
				nm="LTCORA(Buy) Period";
			}
			else if(cont_type.equals("Y"))
			{
				nm="Surveyor Agent Contract";
			}
			else if(cont_type.equals("A"))
			{
				nm="Vessel Agent Contract";
			}
			else if(cont_type.equals("H"))
			{
				nm="Custom House Agent Contract";
			}
			else if(cont_type.equals("Z"))
			{
				nm="Storage";
			}
			else if(cont_type.equals("B"))
			{
				nm="Service Order";
			}
			else if(cont_type.equals("M"))
			{
				nm="Term Sheet";
			}
			else if(cont_type.equals("O")) 
			{
				nm="LTCORA(Sell) CN";
			} 
			else if(cont_type.equals("Q"))
			{
				nm="LTCORA(Sell) Period";
			}
			else // if(nm.equals("")) // MISSING DICTIONARY UPDATE
			{
				nm =cont_type;
			}
		}
		
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return nm;
	}
	
	// This is for Salesforce only, we will see if we need to use same for SUN latter
	public String SalesforceDealMapping(String comp_cd, String counterparty_cd,String agmt, String agmt_rev, String cont, String cont_rev, String cont_type, String cargo_no)
	{
		String function_nm="SalesforceDealMapping()";
		String map="";
		String entity_type = "";
		
		try
		{		
			// Set Entity Type
			if(cont_type.equals("S") || cont_type.equals("L") || cont_type.equals("X") || cont_type.equals("Q") || cont_type.equals("O") || cont_type.equals("F") || cont_type.equals("E") || cont_type.equals("W"))
			{
				entity_type = "C";
			}
			
			// Set Mapping	:: C-33-1-S-1-23, C-33-1-L-0-224001	
			map=counterparty_cd+"-"+entity_type+"-"+comp_cd+"-"+cont_type+"-"+agmt+"-"+cont; 
			if(cont_type.equals("Q") || cont_type.equals("O"))
			{
				map+="-"+cargo_no;
			}						
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return map;
	}
	
	public HashMap getCounterpartyPlantDetail(Connection conn, String comp_cd, String entity, String counterparty_cd, String plant_seq)
	{
		String function_nm="getCounterpartyPlantDetail()";
		HashMap<String, String> plant_detail = new HashMap<String, String>();
		try
		{
			String plantAddress  = "";
			String plantCity  = "";
			String plantState  = "";
			String plantPin  = "";
			String plantNm = "";
			
			if(conn != null)
			{
				String query="SELECT PLANT_ADDR,PLANT_CITY,PLANT_STATE,PLANT_PIN,PLANT_NAME "
						+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
						+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND COMPANY_CD=? AND SEQ_NO=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) ";
				stmtement = conn.prepareStatement(query);
				stmtement.setString(1, counterparty_cd);
				stmtement.setString(2, entity);
				stmtement.setString(3, comp_cd);
				stmtement.setString(4, plant_seq);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					plantAddress  = resultset.getString(1)==null?"":resultset.getString(1);
					plantCity  = resultset.getString(2)==null?"":resultset.getString(2);
					plantState  = resultset.getString(3)==null?"":resultset.getString(3);
					plantPin  = resultset.getString(4)==null?"":resultset.getString(4);
					plantNm  = resultset.getString(5)==null?"":resultset.getString(5);
				}
				stmtement.close();
				resultset.close();
			}
			
			plant_detail.put("plant_address", plantAddress);
			plant_detail.put("plant_city", plantCity);
			plant_detail.put("plant_state", plantState);
			plant_detail.put("plant_pin", plantPin);
			plant_detail.put("plant_name", plantNm);
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return plant_detail;
	}
	
	public String getEntityTaxStructureDtl(Connection conn, String comp_cd, String counterparty_cd, String entity, String seq, String bu, String date)
    {
		String function_nm="getEntityTaxStructureDtl()";
    	String structure_dtl="";
    	try
    	{
    		if(conn != null)
			{
    			String query="SELECT TAX_STRUCT_DTL "
						+ "FROM FMS_ENTITY_TAX_STRUCT_DTL A "
						+ "WHERE ENTITY=? AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND PLANT_SEQ_NO=? AND BU_UNIT=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_TAX_STRUCT_DTL B "
						+ "WHERE A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.BU_UNIT=B.BU_UNIT AND B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY'))";
    			stmtement = conn.prepareStatement(query);
    			stmtement.setString(1, entity);
    			stmtement.setString(2, comp_cd);
    			stmtement.setString(3, counterparty_cd);
    			stmtement.setString(4, seq);
    			stmtement.setString(5, bu);
    			stmtement.setString(6, date);
				resultset = stmtement.executeQuery();
				if(resultset.next())
				{
					structure_dtl=resultset.getString(1)==null?"":resultset.getString(1);
				}
				stmtement.close();
				resultset.close();
			}
    	}
    	catch(Exception e)
    	{
    		new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
    	}
    	return structure_dtl;
    }
	
	Vector CHARGE_NAME = new Vector();
	Vector CHARGE_ABBR = new Vector();
	
	public Vector getCHARGE_ABBR() {return CHARGE_ABBR;}
	public Vector getCHARGE_NAME() {return CHARGE_NAME;}
}
