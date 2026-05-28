package com.etrm.fms.contract_master;

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

public class DB_ContractMaster_Report 
{
	String db_src_file_name="DB_ContractMaster_Report.java";
	Connection conn; 
	PreparedStatement stmt;
	PreparedStatement stmt0;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt5;
	PreparedStatement stmt_temp;
	PreparedStatement stmt_temp0;
	PreparedStatement stmt_temp1;
	PreparedStatement stmt_temp2;
	ResultSet rset;
	ResultSet rset0;
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset5;
	ResultSet rset_temp;
	ResultSet rset_temp0;
	ResultSet rset_temp1;
	ResultSet rset_temp2;
	String queryString="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";
	
	UtilBean utilBean = new UtilBean();
	DateUtil dateUtil= new DateUtil();
	DB_AllocationUtil allocUtil = new DB_AllocationUtil();
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	
	public void init()
	{
		String function_nm="init()";
		try
		{
			Context initContext = new InitialContext();
	    	if(initContext == null)
	    	{
	    		throw new Exception("Boom - No Context");
	    	}
	    	
	    	Context envContext  = (Context)initContext.lookup("java:/comp/env");
	    	DataSource ds = (DataSource)envContext.lookup(RuntimeConf.security_database);
	    	if(ds != null) 
	    	{
	    		conn = ds.getConnection();       
	    		if(conn != null)  
	    		{	    			
	    			if(callFlag.equalsIgnoreCase("CONTRACT_SUMMARY"))
	    			{
	    				getDispSegment_type();
	    				getCustomerCounterpartyList();
	    				getHeaderSegment();
	    				getSegment_type();
	    				getContractSummary();
	    			}
	    			else if(callFlag.equalsIgnoreCase("CONTRACT_WISE"))
	    			{
	    				getCustomerDtls();
	    				getSegment();
	    				getContractWiseDtls();
	    			}
	    			else if(callFlag.equalsIgnoreCase("TCQ_VARIATION"))
	    			{
	    				getCustomerCounterpartyList();
	    				getSegment();
	    				getContractTcqVariation();
	    			}
	    			else if(callFlag.equalsIgnoreCase("ALL_CONT_STAT_REPORT"))
	    			{
	    				comp_abbr = utilBean.getCompanyAbbr(conn, comp_cd);
	    				getAgmtCustomerList();
	    				getAgmtDtlsForStatRpt();
	    				getContDtlForAgmt();
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
	    	if(rset_temp != null){try{rset_temp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_temp0 != null){try{rset_temp0.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_temp1 != null){try{rset_temp1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_temp2 != null){try{rset_temp2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt0 != null){try{stmt0.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp != null){try{stmt_temp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp0 != null){try{stmt_temp0.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp1 != null){try{stmt_temp1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp2 != null){try{stmt_temp2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}
	
	//PB 20250531: for Statistical report for all contract
	public void getAgmtCustomerList()
	{
		String function_nm="getAgmtCustomerList()";
		try
		{
			if(agmtType.equals("T"))
			{
				queryString="SELECT DISTINCT COUNTERPARTY_CD "
						+ "FROM FMS_SUPPLY_CONT_MST "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, "L");
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String counterparty_cd = rset.getString(1)==null?"":rset.getString(1);
					VMST_COUNTERPARTY_CD.add(counterparty_cd);
					VMST_COUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn, counterparty_cd));
					VMST_COUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn, counterparty_cd));
				}
				rset.close();
				stmt.close();
			}
			else
			{
				queryString="SELECT DISTINCT COUNTERPARTY_CD FROM FMS_AGMT_MST "
						+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? ";
				queryString+="UNION ";
				queryString+="SELECT DISTINCT COUNTERPARTY_CD FROM FMS_LTCORA_AGMT_MST "
						+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, agmtType);
				stmt.setString(3, comp_cd);
				stmt.setString(4, agmtType);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String counterparty_cd = rset.getString(1)==null?"":rset.getString(1);
					VMST_COUNTERPARTY_CD.add(counterparty_cd);
					VMST_COUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn, counterparty_cd));
					VMST_COUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn, counterparty_cd));
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
	
	public void getAgmtDtlsForStatRpt()
	{
		String function_nm="getAgmtDtlsForStatRpt()";
		try
		{
			if(agmtType.equals("T"))
			{
				queryString="SELECT CONT_NO,CONT_REV, CONTRACT_TYPE,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),  "
						+ "CASE WHEN TO_DATE(TO_CHAR(START_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')  "
						+ "THEN 'Y' ELSE 'N' END CONT_STATUS, AGMT_REV  "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO "
						+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND CONTRACT_TYPE=B.CONTRACT_TYPE ) "
						+ "ORDER BY AGMT_NO,AGMT_REV";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, "L");
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String agmt_no = rset.getString(1)==null?"":rset.getString(1);
					String cont_rev = rset.getString(2)==null?"":rset.getString(2);
					String agmt_type = rset.getString(3)==null?"":rset.getString(3);
					String start_dt = rset.getString(4)==null?"":rset.getString(4);
					String end_dt = rset.getString(5)==null?"":rset.getString(5);
					String is_expired = rset.getString(6)==null?"":rset.getString(6);
					String agmt_rev = rset.getString(7)==null?"":rset.getString(7);
					
					//String agmt_map = utilBean.NewAgmtMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev, agmt_type);
					String agmt_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, "0", "0", agmt_no, cont_rev, agmt_type, "");
					
					VAGMT_NO.add(agmt_no);
					VAGMT_REV_NO.add(agmt_rev);
					VAGMT_DISP_MAP.add(agmt_map);
					VAGMT_START_DT.add(start_dt);
					VAGMT_END_DT.add(end_dt);
					VEXPIRY_STATUS.add(is_expired);
				}
				rset.close();
				stmt.close();
			}
			else
			{
				queryString="SELECT AGMT_NO, AGMT_REV, AGMT_TYPE,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'), "
						+ "CASE WHEN TO_DATE(TO_CHAR(START_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') "
						+ "THEN 'Y' ELSE 'N' END AGMT_STATUS "
						+ "FROM FMS_AGMT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? ";
						//+ "ORDER BY AGMT_NO,AGMT_REV ";
				queryString+="UNION ";
				queryString+="SELECT AGMT_NO, AGMT_REV, AGMT_TYPE,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'), "
						+ "CASE WHEN TO_DATE(TO_CHAR(START_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') "
						+ "THEN 'Y' ELSE 'N' END AGMT_STATUS "
						+ "FROM FMS_LTCORA_AGMT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? "
						+ "ORDER BY AGMT_NO,AGMT_REV ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmtType);
				stmt.setString(4, comp_cd);
				stmt.setString(5, counterparty_cd);
				stmt.setString(6, agmtType);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					String agmt_no = rset.getString(1)==null?"":rset.getString(1);
					String agmt_rev = rset.getString(2)==null?"":rset.getString(2);
					String agmt_type = rset.getString(3)==null?"":rset.getString(3);
					String start_dt = rset.getString(4)==null?"":rset.getString(4);
					String end_dt = rset.getString(5)==null?"":rset.getString(5);
					String is_expired = rset.getString(6)==null?"":rset.getString(6);
					
					String agmt_map = utilBean.NewAgmtMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev, agmt_type);
					
					VAGMT_NO.add(agmt_no);
					VAGMT_REV_NO.add(agmt_rev);
					VAGMT_DISP_MAP.add(agmt_map);
					VAGMT_START_DT.add(start_dt);
					VAGMT_END_DT.add(end_dt);
					VEXPIRY_STATUS.add(is_expired);
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
	
	public void getContDtlForAgmt()
	{
		String function_nm="getContDtlForAgmt()";
		try
		{
			for(int i=0;i<VAGMT_NO.size();i++)
			{
				int index=0;
				int ctn=0;
				if(agmtType.equals("F") || agmtType.equals("T"))
				{
					queryString="SELECT CONT_NO,CONT_REV,CONTRACT_TYPE,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'), "
							+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_STATUS, "
							+ "CASE WHEN END_DT<TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') THEN 'Y' ELSE 'N' END IS_EXPIRED  "
							+ "FROM FMS_SUPPLY_CONT_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? ";
					if(agmtType.equals("T"))
					{
						queryString+="AND CONT_NO=? AND AGMT_REV=? ";
					}
					else
					{
						queryString+="AND AGMT_NO=? AND AGMT_REV=? ";
					}
					queryString+= "AND CONTRACT_TYPE NOT IN (?,?,?) "
							+ "ORDER BY CONT_NO,CONT_REV ";
					stmt1=conn.prepareStatement(queryString);
					stmt1.setString(++ctn, comp_cd);
					stmt1.setString(++ctn, counterparty_cd);
					stmt1.setString(++ctn, ""+VAGMT_NO.elementAt(i));
					stmt1.setString(++ctn, ""+VAGMT_REV_NO.elementAt(i));
					stmt1.setString(++ctn, "F");
					stmt1.setString(++ctn, "E");
					stmt1.setString(++ctn, "W");
				}
				else
				{
					queryString="SELECT CONT_NO,CONT_REV,CONTRACT_TYPE,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),  "
							+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_STATUS,  "
							+ "CASE WHEN END_DT<TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') THEN 'Y' ELSE 'N' END IS_EXPIRED   "
							+ "FROM FMS_LTCORA_CONT_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=?  "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "ORDER BY CONT_NO,CONT_REV ";
					stmt1=conn.prepareStatement(queryString);
					stmt1.setString(++ctn, comp_cd);
					stmt1.setString(++ctn, counterparty_cd);
					stmt1.setString(++ctn, ""+VAGMT_NO.elementAt(i));
					stmt1.setString(++ctn, ""+VAGMT_REV_NO.elementAt(i));
					
				}
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					index++;
					String cont_no=rset1.getString(1)==null?"":rset1.getString(1);
					String cont_rev=rset1.getString(2)==null?"":rset1.getString(2);
					String cont_type=rset1.getString(3)==null?"":rset1.getString(3);
					String sign_dt=rset1.getString(4)==null?"":rset1.getString(4);
					String start_dt=rset1.getString(5)==null?"":rset1.getString(5);
					String end_dt=rset1.getString(6)==null?"":rset1.getString(6);
					String cont_status=rset1.getString(7)==null?"":rset1.getString(7);
					String is_expired=rset1.getString(8)==null?"":rset1.getString(8);
					String disp_cont_map=utilBean.NewDealMappingId(comp_cd, counterparty_cd, ""+VAGMT_NO.elementAt(i), ""+VAGMT_REV_NO.elementAt(i), cont_no, cont_rev, cont_type, "");
					
					VCONT_NO.add(cont_no);
					VCONT_REV_NO.add(cont_rev);
					VCONTRACT_TYPE.add(cont_type);
					VSIGNING_DT.add(sign_dt);
					VSTART_DT.add(start_dt);
					VEND_DT.add(end_dt);
					VCONT_STATUS.add(""+utilBean.ContStatusName(cont_status));
					VCONT_STATUS_FLG.add(cont_status);
					VDIS_CONT_MAPPING.add(disp_cont_map);
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
	
	
	
	
	
	public void getHeaderSegment()
	{
		String function_nm="getHeaderSegment()";

		try
		{
			if(segmentType.equals("0"))
			{
				VHEADER_SEGMENT.add("RLNG");
				VHEADER_SEGMENT.add("LTCORA");
			}
			else if(segmentType.equals("A"))
			{
				VHEADER_SEGMENT.add("LTCORA");
			}
			else
			{
				VHEADER_SEGMENT.add("RLNG");
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	// Below Class is coded by Arth Patel On 20230713 , for Contract TCQ Variation report.
	private void getContractTcqVariation() 
	{
		String function_nm="getContractTcqVariation()";
		try
		{
			if(!segmentType.equals("0"))
			{
				VTEMP_SEGMENT_TYPE.add(segmentType);
				if(segmentType.equals("S"))
				{
					VTEMP_SEGMENT.add("Supply Notice");
				}
				else if(segmentType.equals("L"))
				{
					VTEMP_SEGMENT.add("Letter of Agreement");
				}
				else if(segmentType.equals("X"))
				{
					VTEMP_SEGMENT.add("IGX");
				}
				else if(segmentType.equals("A"))
				{
					VTEMP_SEGMENT.add("LTCORA(sell)");
				}
				else
				{
					VTEMP_SEGMENT.add("");
				}
			}
			else
			{
				VTEMP_SEGMENT=VSEGMENT;
				VTEMP_SEGMENT_TYPE=VSEGMENT_TYPE;
			}
			
			for(int i=0; i<VTEMP_SEGMENT_TYPE.size(); i++)
			{
				String agmt = "";
				String agmt_rev = "";
				String cont = "";
				String cont_rev = "";
				int index=0;
				queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONT_REF_NO,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),"
						+ "RATE,RATE_UNIT,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_STATUS,TCQ,"
						+ "TO_CHAR(ENT_DT,'DD/MM/YYYY'),ENT_BY,TO_CHAR(FCC_DATE,'DD/MM/YYYY'),FCC_BY,IS_ALLOCATED,TRADE_REF_NO,AGMT_BASE "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
				if(!counterparty_cd.equals("0"))
				{
					queryString+=" AND COUNTERPARTY_CD=? ";
				}
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, ""+VTEMP_SEGMENT_TYPE.elementAt(i));
				stmt.setString(3, to_dt);
				stmt.setString(4, from_dt);
				if(!counterparty_cd.equals("0"))
				{
					stmt.setString(5, counterparty_cd);
				}
				rset=stmt.executeQuery();
				while(rset.next())
				{
					index+=1;
					String companyCd = rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd = rset.getString(2)==null?"":rset.getString(2);
					agmt = rset.getString(3)==null?"":rset.getString(3);
					agmt_rev = rset.getString(4)==null?"":rset.getString(4);
					cont = rset.getString(5)==null?"":rset.getString(5);
					cont_rev = rset.getString(6)==null?"":rset.getString(6);
					String cont_ref = rset.getString(7)==null?"":rset.getString(7);
					String trade_ref = rset.getString(20)==null?"":rset.getString(20);
					String agmt_base = rset.getString(21)==null?"":rset.getString(21);
					String cont_type=""+VTEMP_SEGMENT_TYPE.elementAt(i);
					
					if(cont_type.equals("X"))
					{
						cont_ref=trade_ref;
					}
					
					VCOUNTERPARTY_CD.add(countpty_cd);
					VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
					VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
					
					//String dealNo=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
					String dealNo=utilBean.NewDealMappingId(companyCd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
					
					if(agmt_base.equals("D"))
					{
						dealNo=dealNo+" <font style='background: #a6ff4d;'>[DLV]</font>";
					}
					VDIS_CONT_MAPPING.add(dealNo);
					
					VCONT_REF.add(cont_ref);
					VSIGNING_DT.add(rset.getString(8)==null?"":rset.getString(8));
					String rate = ""+rset.getDouble(9);
					String rate_unit = rset.getString(10)==null?"":rset.getString(10);
					VRATE.add(""+utilBean.RateNumberFormat(Double.parseDouble(rate), rate_unit));
					VRATE_UNIT.add(""+utilBean.getRateUnitNm(conn,rate_unit));
					String start_date = rset.getString(11)==null?"":rset.getString(11);
					VSTART_DT.add(start_date);
					String end_date = rset.getString(12)==null?"":rset.getString(12);
					VEND_DT.add(end_date);
					String cont_status_flg=rset.getString(13)==null?"":rset.getString(13);
					VCONT_STATUS_FLG.add(cont_status_flg);
					VCONT_STATUS.add(""+ContStatusName(cont_status_flg));
					double final_tcq = rset.getDouble(14);
					VFINAL_TCQ.add(final_tcq);
					VIS_ALLOCATED.add(rset.getString(19)==null?"N":rset.getString(19));
				
					double tcq=0;
					queryString1="SELECT TCQ,IS_ALLOCATED "
							+ "FROM FMS_SUPPLY_CONT_MST A "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND AGMT_NO=? AND CONT_NO=? "
							+ "AND A.CONT_REV=(SELECT MIN(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
							+ "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') "
							+ " AND COUNTERPARTY_CD=? ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, companyCd);
					stmt1.setString(2, ""+VTEMP_SEGMENT_TYPE.elementAt(i));
					stmt1.setString(3, agmt);
					stmt1.setString(4, cont);
					stmt1.setString(5, end_date);
					stmt1.setString(6, start_date);
					stmt1.setString(7, countpty_cd);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						tcq = rset1.getDouble(1);
					}
					VTCQ.add(tcq);
					double variation_tcq = final_tcq-tcq;
					VVARIATION_TCQ.add(variation_tcq);
					rset1.close();
					stmt1.close();
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

	// Below Class is coded by Arth Patel On 20230602 , for Contract Wise report.
	public void getCustomerDtls() 
	{
		String function_nm="getCustomerDtls()";

		try
		{
			queryString = "SELECT DISTINCT(COUNTERPARTY_CD) "
					+ "FROM FMS_SUPPLY_CONT_MST "
					+ "WHERE COMPANY_CD=? ORDER BY COUNTERPARTY_CD";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String counterparty_cd=rset.getString(1)==null?"":rset.getString(1);
				VMST_COUNTERPARTY_CD.add(counterparty_cd);	
				VMST_COUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,counterparty_cd));
				VMST_COUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,counterparty_cd));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
		
	// Below Class is coded by Arth Patel On 20230602 , for Contract Wise report.
	public void getContractWiseDtls() 
	{
		String function_nm="getContractWiseDtls()";

		try
		{	
			String to_date="";
			String frm_dt = "";
			String approved_counterparty_cd = "";
			String approvedflag = "";
			int current_month = dateUtil.getCurrentMonth();
			int current_year = dateUtil.getCurrentYear();
			String curr_month = String.valueOf(current_month);
			if(curr_month.length()==1){
				curr_month="0"+curr_month;
			}else{
				curr_month=""+curr_month;
			}
			String curr_year = String.valueOf(current_year);
			double exchange_value = 0;
			
			if(!month.equals("0") && !year.equals("0")) 
			{
				frm_dt = "01/"+month+"/"+year;
				to_date = dateUtil.getLastDateOfMonth(month,year);
			}
			
			queryString3="SELECT DISTINCT APPROVE_FLAG,TO_CHAR(APPROVE_DT,'DD/MM/YYYY'),COUNTERPARTY_CD "
					+ "FROM FMS_EXP_SALES_DTLS "
					+ "WHERE COMPANY_CD=? AND MONTH=? AND YEAR=? ";
			if(!counterparty_cd.equals("0"))
			{
				queryString3+="AND COUNTERPARTY_CD=? ";
			}
			stmt3 = conn.prepareStatement(queryString3);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, month);
			stmt3.setString(3, year);
			if(!counterparty_cd.equals("0"))
			{
				stmt3.setString(4, counterparty_cd);
			}
			rset3=stmt3.executeQuery();
			while(rset3.next()) 
			{
				approved_counterparty_cd = rset3.getString(3)==null?"":rset3.getString(3);
				approvedflag = rset3.getString(1)==null?"":rset3.getString(1);
				VAPPROVED_FLAG.add(approvedflag);
				VAPPROVED_DATE.add(rset3.getString(2)==null?"":rset3.getString(2));
				VAPPROVED_COUNTERPARTY_CD.add(approved_counterparty_cd);
				
				VAPPROVED_COUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,rset2.getString(1)==null?"":rset2.getString(1)));
			}
			rset3.close();
			stmt3.close();
			
			queryString="SELECT MAX(EXCHG_VAL) "
					+ "FROM FMS_EXCHG_RATE_ENTRY "
					+ "WHERE EXCHG_RATE_CD=? AND EFF_DT<=TO_DATE(?,'DD/MM/YYYY')  ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, "3");
			stmt.setString(2, frm_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				exchange_value=rset.getDouble(1);
			}
			rset.close();
			stmt.close();
			
			//if(curr_month.equals(month) && curr_year.equals(year)) 
			{
				for(int i=0; i<VSEGMENT_TYPE.size(); i++)
				{
					String agmt = "";
					String cont = "";
					String cont_type = "";
					String cont_rev ="";
					double temp_rate = 0;
					double tcq =0;
					
					int index = 0;
					queryString="SELECT DISTINCT(COUNTERPARTY_CD) "
							+ "FROM FMS_SUPPLY_CONT_MST  "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
					if(!counterparty_cd.equals("0"))
					{
						queryString+="AND COUNTERPARTY_CD=? ";
					}
						queryString+="ORDER BY COUNTERPARTY_CD";
					stmt1 = conn.prepareStatement(queryString);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, ""+VSEGMENT_TYPE.elementAt(i));
					stmt1.setString(3, to_date);
					stmt1.setString(4, frm_dt);
					if(!counterparty_cd.equals("0"))
					{
						stmt1.setString(5, counterparty_cd);
					}
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						index+=1;
						String counterparty_cd = rset1.getString(1)==null?"":rset1.getString(1);
						
						VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,counterparty_cd));
						VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,counterparty_cd));
						
						int sub_index=0;
						double total_tcq = 0;
						double totalsuppliedQty=0;
					    double totalbalancemmbtu=0;
						double totalsales_amt_inr=0;
						double totalsales_amt_usd=0;
						queryString="SELECT DISTINCT TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),TCQ,AGMT_BASE,CONT_NAME,COUNTERPARTY_CD,"
								+ "COMPANY_CD,RATE,RATE_UNIT ,AGMT_NO,CONT_NO,CONT_REF_NO,TRADE_REF_NO,AGMT_REV,CONT_REV,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),CONTRACT_TYPE "
								+ "FROM FMS_SUPPLY_CONT_MST A "
								+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
								+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
								+ "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') "
								+"AND COUNTERPARTY_CD=? "
								+"ORDER BY COUNTERPARTY_CD";
						stmt0 = conn.prepareStatement(queryString);
						stmt0.setString(1, comp_cd);
						stmt0.setString(2, ""+VSEGMENT_TYPE.elementAt(i));
						stmt0.setString(3, to_date);
						stmt0.setString(4, frm_dt);
						stmt0.setString(5, counterparty_cd);
						rset0=stmt0.executeQuery();
						while(rset0.next())
						{
							sub_index+=1;
							VSTART_DT.add(rset0.getString(1)==null?"":rset0.getString(1));
							VEND_DT.add(rset0.getString(2)==null?"":rset0.getString(2));
							String agmt_base = rset0.getString(4)==null?"":rset0.getString(4);
							String cont_name = rset0.getString(5)==null?"":rset0.getString(5);
							String countpty_cd = rset0.getString(6)==null?"":rset0.getString(6);
							String companyCd = rset0.getString(7)==null?"":rset0.getString(7);
							temp_rate = rset0.getDouble(8);
							agmt = rset0.getString(10)==null?"":rset0.getString(10);
							cont = rset0.getString(11)==null?"":rset0.getString(11);
							String cont_ref = rset0.getString(12)==null?"":rset0.getString(12);
							String trade_ref = rset0.getString(13)==null?"":rset0.getString(13);
							String agmt_rev = rset0.getString(14)==null?"":rset0.getString(14);
							cont_rev = rset0.getString(15)==null?"":rset0.getString(15);
							cont_type = rset0.getString(17)==null?"":rset0.getString(17);
							VCONTRACT_TYPE.add(cont_type);
							
							if(cont_type.equals("X"))
							{
								cont_ref=trade_ref;
							}
							VCONT_REF.add(cont_ref);
							VSIGNING_DT.add(rset0.getString(16)==null?"":rset0.getString(16));
							VCOUNTERPARTY_CD.add(countpty_cd);
							
							String min_dt="";
							String max_dt="";
							queryString="SELECT SUM(QTY_MMBTU),TO_CHAR(MIN(GAS_DT),'DD/MM/YYYY'),TO_CHAR(MAX(GAS_DT),'DD/MM/YYYY') "
									+ "FROM FMS_DAILY_ALLOCATION_DTL A "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
									+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
									+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ=B.PLANT_SEQ AND A.TRANSPORTER_CD=B.TRANSPORTER_CD "
									+ "AND A.TRANS_SEQ=B.TRANS_SEQ AND A.GAS_DT<TO_DATE(?,'DD/MM/YYYY') AND A.GAS_DT=B.GAS_DT AND A.CARGO_NO=B.CARGO_NO)";
							 stmt_temp = conn.prepareStatement(queryString);
							 stmt_temp.setString(1, companyCd);
							 stmt_temp.setString(2, countpty_cd);
							 stmt_temp.setString(3, agmt);
							 stmt_temp.setString(4, cont);
							 stmt_temp.setString(5, cont_type);
							 stmt_temp.setString(6, frm_dt);
							 rset_temp=stmt_temp.executeQuery(); 
						     while(rset_temp.next()) 
						     {
							     min_dt=rset_temp.getString(2)==null?"":rset_temp.getString(2);
							     max_dt=rset_temp.getString(3)==null?"":rset_temp.getString(3);
						     }
						     rset_temp.close();
						     stmt_temp.close();
						     VALLOC_MIN_DT.add(min_dt); VALLOC_MAX_DT.add(max_dt);
							
						     queryString1 = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,MONTH,YEAR,TCQ, "
									+ "SUPPLIED_QTY,BALANCE_QTY,EXP_SALES_RATE,RATE_UNIT,EXP_SALES_AMT_INR,EXP_SALES_AMT_USD,APPROVE_FLAG "
									+ "FROM FMS_EXP_SALES_DTLS A "
									+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND AGMT_NO=? AND CONT_NO=? "
									+ "AND CONT_REV=? AND AGMT_REV=? "
									+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_EXP_SALES_DTLS B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
									+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
									+ "AND MONTH=? AND YEAR>=? AND COUNTERPARTY_CD=? "
									+ "ORDER BY COUNTERPARTY_CD";
						     stmt_temp1 = conn.prepareStatement(queryString1);
							 stmt_temp1.setString(1, companyCd);
							 stmt_temp1.setString(2, ""+VSEGMENT_TYPE.elementAt(i));
							 stmt_temp1.setString(3, agmt);
							 stmt_temp1.setString(4, cont);
							 stmt_temp1.setString(5, cont_rev);
							 stmt_temp1.setString(6, agmt_rev);
							 stmt_temp1.setString(7, month);
							 stmt_temp1.setString(8, year);
							 stmt_temp1.setString(9, countpty_cd);
							 rset_temp1=stmt_temp1.executeQuery();
						     if(rset_temp1.next()) 
						     {
						    	 String agmt_no = rset_temp1.getString(3)==null?"":rset_temp1.getString(3);
						    	 String agmtRev = rset_temp1.getString(4)==null?"":rset_temp1.getString(4);
						    	 String cont_no = rset_temp1.getString(5)==null?"":rset_temp1.getString(5);
						    	 String contRev = rset_temp1.getString(6)==null?"":rset_temp1.getString(6);
						    	 tcq = rset_temp1.getDouble(10);
						    	 double supplied_qty = rset_temp1.getDouble(11);
						    	 double balance_qty = rset_temp1.getDouble(12);
						    	 String sales_rate = ""+rset_temp1.getDouble(13);
						    	 String sales_rate_unit = rset_temp1.getString(14)==null?"":rset_temp1.getString(14);
						    	 double sales_amt_inr = rset_temp1.getDouble(15);
						    	 double sales_amt_usd = rset_temp1.getDouble(16);
						    	 String approved_flag = rset_temp1.getString(17)==null?"":rset_temp1.getString(17);
								
						    	 //String dealNo=utilBean.getDisplayDealMapping(agmt_no, agmtRev, cont_no, contRev, cont_type);
						    	 String dealNo=utilBean.NewDealMappingId(companyCd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
						    	 if(agmt_base.equals("D"))
						    	 {
						    		 dealNo=dealNo+" <font style='background: #a6ff4d;'>[DLV]</font>";
						    	 }
						    	 VDIS_CONT_MAPPING.add(dealNo);
						    	 VTCQ.add(nf.format(tcq));
						    	 VSUPPLIED_QTY_MMBTU.add(supplied_qty);
						    	 VBALANCEQTY.add(balance_qty);
						    	 VRATE.add(""+utilBean.RateNumberFormat(Double.parseDouble(sales_rate), sales_rate_unit));
						    	 VRATE_UNIT.add(""+utilBean.getRateUnitNm(conn,sales_rate_unit));
						    	 VTEMP_RATE_UNIT.add(sales_rate_unit);
						    	 VSALES_AMT_INR.add(nf.format(sales_amt_inr));
						    	 VSALES_AMT_USD.add(nf.format(sales_amt_usd));
						    	 VAPPROVED_COLOR.add("#99ffcc");
						    	 VAGMT_NO.add(agmt_no);
						    	 VCONT_NO.add(cont_no);
						    	 VAGMT_REV_NO.add(agmtRev);
						    	 VCONT_REV_NO.add(contRev);
									
						    	 total_tcq += tcq;
						    	 totalsuppliedQty += supplied_qty;
						    	 totalbalancemmbtu += balance_qty;
						    	 totalsales_amt_inr += sales_amt_inr;
						    	 totalsales_amt_usd += sales_amt_usd;
								
						     }
						     else
						     {
						    	 queryString="SELECT DISTINCT TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),TCQ,AGMT_BASE,CONT_NAME,COUNTERPARTY_CD,"
										+ "COMPANY_CD,RATE,RATE_UNIT ,AGMT_NO,CONT_NO,CONT_REF_NO,TRADE_REF_NO,AGMT_REV,CONT_REV,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),CONTRACT_TYPE "
										+ "FROM FMS_SUPPLY_CONT_MST A "
										+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND AGMT_NO=? AND CONT_NO=? "
										+ "AND CONT_REV=? AND AGMT_REV=? "
										+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
										+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
										+ "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') "
										+"AND COUNTERPARTY_CD=? "
										+"ORDER BY COUNTERPARTY_CD";
						    	 stmt2 = conn.prepareStatement(queryString);
								 stmt2.setString(1, comp_cd);
								 stmt2.setString(2, ""+VSEGMENT_TYPE.elementAt(i));
								 stmt2.setString(3, agmt);
								 stmt2.setString(4, cont);
								 stmt2.setString(5, cont_rev);
								 stmt2.setString(6, agmt_rev);
								 stmt2.setString(7, to_date);
								 stmt2.setString(8, frm_dt);
								 stmt2.setString(9, counterparty_cd);
								 rset2=stmt2.executeQuery();
						    	 if(rset2.next()) 
						    	 {
						    		 String agmt_no = rset2.getString(10)==null?"":rset2.getString(10);
						    		 String agmtRev = rset2.getString(14)==null?"":rset2.getString(14);
						    		 String cont_no = rset2.getString(11)==null?"":rset2.getString(11);
						    		 String contRev = rset2.getString(15)==null?"":rset2.getString(15);
						    		 tcq = rset2.getDouble(3);
						    		 String sales_rate = ""+rset2.getDouble(8);
						    		 String sales_rate_unit = rset2.getString(9)==null?"":rset2.getString(9);
									
						    		 //String dealNo=utilBean.getDisplayDealMapping(agmt_no, agmtRev, cont_no, contRev, cont_type);
						    		 String dealNo=utilBean.NewDealMappingId(companyCd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
						    		 if(agmt_base.equals("D"))
						    		 {
						    			 dealNo=dealNo+" <font style='background: #a6ff4d;'>[DLV]</font>";
						    		 }
						    		 VDIS_CONT_MAPPING.add(dealNo);
						    		 VTCQ.add(nf.format(tcq));
						    		 VRATE.add(""+utilBean.RateNumberFormat(Double.parseDouble(sales_rate), sales_rate_unit));
						    		 VRATE_UNIT.add(""+utilBean.getRateUnitNm(conn,sales_rate_unit));
						    		 VTEMP_RATE_UNIT.add(sales_rate_unit);
						    		 VAGMT_NO.add(agmt_no);
						    		 VCONT_NO.add(cont_no);
						    		 VAGMT_REV_NO.add(agmtRev);
						    		 VCONT_REV_NO.add(contRev);
						    		 VAPPROVED_COLOR.add("");
									
						    		 double suppliedQty=0;
						    		 queryString="SELECT SUM(QTY_MMBTU),TO_CHAR(MIN(GAS_DT),'DD/MM/YYYY'),TO_CHAR(MAX(GAS_DT),'DD/MM/YYYY') "
											+ "FROM FMS_DAILY_ALLOCATION_DTL A "
											+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
											+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
											+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
											+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
											+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ=B.PLANT_SEQ AND A.TRANSPORTER_CD=B.TRANSPORTER_CD "
											+ "AND A.TRANS_SEQ=B.TRANS_SEQ AND A.GAS_DT<TO_DATE(?,'DD/MM/YYYY') AND A.GAS_DT=B.GAS_DT AND A.CARGO_NO=B.CARGO_NO)";
									 stmt_temp2 = conn.prepareStatement(queryString);
									 stmt_temp2.setString(1, companyCd);
									 stmt_temp2.setString(2, countpty_cd);
									 stmt_temp2.setString(3, agmt);
									 stmt_temp2.setString(4, cont);
									 stmt_temp2.setString(5, cont_type);
									 stmt_temp2.setString(6, frm_dt);
						    		 rset_temp2=stmt_temp2.executeQuery(); 
						    		 while(rset_temp2.next()) 
						    		 {
						    			 suppliedQty=rset_temp2.getDouble(1);
						    		 }
						    		 rset_temp2.close();
						    		 stmt_temp2.close();
						    		 VSUPPLIED_QTY_MMBTU.add(nf.format(suppliedQty));
								    
						    		 int suppliedqty = (int)suppliedQty;
						    		 int rate1 = (int)temp_rate;
						    		 double balancemmbtu = tcq-suppliedqty;
						    		 double grossAmt = balancemmbtu*rate1;
									
						    		 VBALANCEQTY.add(nf.format(balancemmbtu));
									
						    		 total_tcq+=tcq;
						    		 totalsuppliedQty+= suppliedQty;
						    		 totalbalancemmbtu+=balancemmbtu;
						    		 if(sales_rate_unit.equals("1")) 
						    		 {
						    			 VSALES_AMT_INR.add(nf.format(grossAmt));
						    			 VSALES_AMT_USD.add(nf.format(grossAmt/exchange_value));
										
						    			 totalsales_amt_inr+=grossAmt;
						    			 totalsales_amt_usd+=(grossAmt/exchange_value);
						    		 }
						    		 else if(sales_rate_unit.equals("2"))
						    		 {
						    			 VSALES_AMT_INR.add(nf.format(grossAmt*exchange_value));
						    			 VSALES_AMT_USD.add(nf.format(grossAmt));
										
						    			 totalsales_amt_inr+=(grossAmt*exchange_value);
						    			 totalsales_amt_usd+=grossAmt;
						    		 }
						    	 }
						    	 rset2.close();
						    	 stmt2.close();
						     }
						     rset_temp1.close();
						     stmt_temp1.close();
						}
						VSUB_INDEX.add(sub_index);
						VTOTAL_TCQ.add(nf.format(total_tcq));
					 	VTOTAL_SUPPLIED_QTY_MMBTU.add(nf.format(totalsuppliedQty));
						VTOTAL_BALANCE_QTY_MMBTU.add(nf.format(totalbalancemmbtu));
						VTOTAL_SALES_AMT_INR.add(nf.format(totalsales_amt_inr));
						VTOTAL_SALES_AMT_USD.add(nf.format(totalsales_amt_usd));
						
						rset0.close();
						stmt0.close();
					}
					VINDEX.add(index);
					rset1.close();
					stmt1.close();
				}
			}
			/* else 
			{
				for(int i=0; i<VSEGMENT_TYPE.size(); i++)
				{
					String agmt = "";
					String cont = "";
					String cont_type = "";
					String cont_rev ="";
					double temp_rate = 0;
					double tcq =0;
				
					int index = 0;
					double total_tcq = 0;
					double totalsuppliedQty=0;
				    double totalbalancemmbtu=0;
					double totalsales_amt_inr=0;
					double totalsales_amt_usd=0;
					queryString1="SELECT DISTINCT(COUNTERPARTY_CD) "
							+ "FROM FMS_EXP_SALES_DTLS "
							+ "WHERE COMPANY_CD='"+comp_cd+"' AND CONTRACT_TYPE='"+VSEGMENT_TYPE.elementAt(i)+"' "
							+ "AND month='"+month+"' AND YEAR='"+year+"' ";
					if(!counterparty_cd.equals("0"))
					{
						queryString+="AND COUNTERPARTY_CD='"+approved_counterparty_cd+"' ";
					}
						queryString+="ORDER BY COUNTERPARTY_CD";
						
					rset2=stmt2.executeQuery(queryString1);
					while(rset2.next())
					{
						index+=1;
						String counterparty_cd = rset2.getString(1)==null?"":rset2.getString(1);
						
						VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(counterparty_cd, comp_cd));
						VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(counterparty_cd, comp_cd));
						
						
						int sub_index=0;
						queryString="SELECT DISTINCT TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),TCQ,AGMT_BASE,CONT_NAME,COUNTERPARTY_CD,"
								+ "COMPANY_CD,RATE,RATE_UNIT ,AGMT_NO,CONT_NO,CONT_REF_NO,TRADE_REF_NO,AGMT_REV,CONT_REV,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),CONTRACT_TYPE "
								+ "FROM FMS_SUPPLY_CONT_MST A "
								+ "WHERE COMPANY_CD='"+comp_cd+"' AND CONTRACT_TYPE='"+VSEGMENT_TYPE.elementAt(i)+"' "
								+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
								+ "AND START_DT<=TO_DATE('"+to_date+"','DD/MM/YYYY') AND END_DT>=TO_DATE('"+frm_dt+"','DD/MM/YYYY') ";
						
						queryString+="AND COUNTERPARTY_CD='"+counterparty_cd+"' ";
						
						queryString+="ORDER BY COUNTERPARTY_CD";
						
						rset=stmt.executeQuery(queryString);
						while(rset.next())
						{
							sub_index+=1;
							VSTART_DT.add(rset.getString(1)==null?"":rset.getString(1));
							VEND_DT.add(rset.getString(2)==null?"":rset.getString(2));
							String agmt_base = rset.getString(4)==null?"":rset.getString(4);
							String cont_name = rset.getString(5)==null?"":rset.getString(5);
							String countpty_cd = rset.getString(6)==null?"":rset.getString(6);
							String companyCd = rset.getString(7)==null?"":rset.getString(7);
							temp_rate = rset.getDouble(8);
							agmt = rset.getString(10)==null?"":rset.getString(10);
							cont = rset.getString(11)==null?"":rset.getString(11);
							String cont_ref = rset.getString(12)==null?"":rset.getString(12);
							String trade_ref = rset.getString(13)==null?"":rset.getString(13);
							String agmt_rev = rset.getString(14)==null?"":rset.getString(14);
							cont_rev = rset.getString(15)==null?"":rset.getString(15);
							cont_type = rset.getString(17)==null?"":rset.getString(17);
							VCONTRACT_TYPE.add(cont_type);
							
							if(cont_type.equals("X"))
							{
								cont_ref=trade_ref;
							}
							VCONT_REF.add(cont_ref);
							VSIGNING_DT.add(rset.getString(16)==null?"":rset.getString(16));
							VCOUNTERPARTY_CD.add(countpty_cd);
							
							String min_dt="";
							String max_dt="";
							queryString="SELECT SUM(QTY_MMBTU),TO_CHAR(MIN(GAS_DT),'DD/MM/YYYY'),TO_CHAR(MAX(GAS_DT),'DD/MM/YYYY') "
									+ "FROM FMS_DAILY_ALLOCATION_DTL A "
									+ "WHERE COMPANY_CD='"+companyCd+"' AND COUNTERPARTY_CD='"+countpty_cd+"' "
									+ "AND AGMT_NO='"+agmt+"' AND CONT_NO='"+cont+"' AND CONTRACT_TYPE='"+cont_type+"' "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
									+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
									+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ=B.PLANT_SEQ AND A.TRANSPORTER_CD=B.TRANSPORTER_CD "
									+ "AND A.TRANS_SEQ=B.TRANS_SEQ AND A.GAS_DT<TO_DATE('"+frm_dt+"','DD/MM/YYYY') AND A.GAS_DT=B.GAS_DT)";
							 rset_temp=stmt_temp.executeQuery(queryString); 
						     while(rset_temp.next()) 
						     {
							     
							     min_dt=rset_temp.getString(2)==null?"":rset_temp.getString(2);
							     max_dt=rset_temp.getString(3)==null?"":rset_temp.getString(3);
						     }
						     VALLOC_MIN_DT.add(min_dt); VALLOC_MAX_DT.add(max_dt);
						
					     	queryString1 = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,MONTH,YEAR,TCQ,"
									+ "SUPPLIED_QTY,BALANCE_QTY,EXP_SALES_RATE,RATE_UNIT,EXP_SALES_AMT_INR,EXP_SALES_AMT_USD,APPROVE_FLAG "
									+ "FROM FMS_EXP_SALES_DTLS A "
									+ "WHERE COMPANY_CD='"+companyCd+"' AND CONTRACT_TYPE='"+VSEGMENT_TYPE.elementAt(i)+"' AND AGMT_NO='"+agmt+"' AND CONT_NO='"+cont+"' "
									+ "AND CONT_REV='"+cont_rev+"' AND AGMT_REV='"+agmt_rev+"' "
									+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_EXP_SALES_DTLS B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
									+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
									+ "AND MONTH='"+month+"' AND YEAR>='"+year+"' AND COUNTERPARTY_CD='"+countpty_cd+"' "
									+ "ORDER BY COUNTERPARTY_CD";
	
							rset3=stmt3.executeQuery(queryString1);
							while(rset3.next())
							{
								
								String agmt_no = rset3.getString(3)==null?"":rset3.getString(3);
								String agmtRev = rset3.getString(4)==null?"":rset3.getString(4);
								String cont_no = rset3.getString(5)==null?"":rset3.getString(5);
								String contRev = rset3.getString(6)==null?"":rset3.getString(6);
								tcq = rset3.getDouble(10);
								double supplied_qty = rset3.getDouble(11);
								double balance_qty = rset3.getDouble(12);
								String sales_rate = ""+rset3.getDouble(13);
								String sales_rate_unit = rset3.getString(14)==null?"":rset3.getString(14);
								double sales_amt_inr = rset3.getDouble(15);
								double sales_amt_usd = rset3.getDouble(16);
								String approved_flag = rset3.getString(17)==null?"":rset3.getString(17);
								
								String dealNo=utilBean.getDisplayDealMapping(agmt_no, agmtRev, cont_no, contRev, cont_type);
								if(agmt_base.equals("D"))
								{
									dealNo=dealNo+" <font style='background: #a6ff4d;'>[DLV]</font>";
								}
								VDIS_CONT_MAPPING.add(dealNo);
								VTCQ.add(nf.format(tcq));
								VSUPPLIED_QTY_MMBTU.add(supplied_qty);
								VBALANCEQTY.add(balance_qty);
								VRATE.add(""+utilBean.RateNumberFormat(Double.parseDouble(sales_rate), sales_rate_unit));
								VRATE_UNIT.add(""+utilBean.getRateUnitNm(sales_rate_unit));
								VTEMP_RATE_UNIT.add(sales_rate_unit);
								VSALES_AMT_INR.add(sales_amt_inr);
								VSALES_AMT_USD.add(sales_amt_usd);
								VAPPROVED_COLOR.add("#99ffcc");
								VAGMT_NO.add(agmt_no);
								VCONT_NO.add(cont_no);
								VAGMT_REV_NO.add(agmtRev);
								VCONT_REV_NO.add(contRev);
								
								 total_tcq += tcq;
						    	 totalsuppliedQty += supplied_qty;
						    	 totalbalancemmbtu += balance_qty;
						    	 totalsales_amt_inr += sales_amt_inr;
						    	 totalsales_amt_usd += sales_amt_usd;
							}
						}
						VSUB_INDEX.add(sub_index);
						
						VTOTAL_TCQ.add(nf.format(total_tcq));
						VTOTAL_SUPPLIED_QTY_MMBTU.add(nf.format(totalsuppliedQty));
						VTOTAL_BALANCE_QTY_MMBTU.add(nf.format(totalbalancemmbtu));
						VTOTAL_SALES_AMT_INR.add(nf.format(totalsales_amt_inr));
						VTOTAL_SALES_AMT_USD.add(nf.format(totalsales_amt_usd));
					}
					VINDEX.add(index);
				}
			}*/ 
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
			VMST_COUNTERPARTY_CD= utilBean.getCOUNTERPARTY_CD();
			VMST_COUNTERPARTY_NM = utilBean.getCOUNTERPARTY_NM();
			VMST_COUNTERPARTY_ABBR = utilBean.getCOUNTERPARTY_ABBR();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSegment_type()
	{
		String function_nm="getSegment_type()";

		try
		{
			int segment_index=0;
			for(int i=0; i<VHEADER_SEGMENT.size(); i++)
			{
				if(VHEADER_SEGMENT.elementAt(i).equals("RLNG"))
				{
					VSEGMENT.add("Supply Notice");
					VSEGMENT.add("Letter of Agreement");
					VSEGMENT.add("IGX");
					
					VSEGMENT_TYPE.add("S");
					VSEGMENT_TYPE.add("L");
					VSEGMENT_TYPE.add("X");
					segment_index=3;
				}
				else if(VHEADER_SEGMENT.elementAt(i).equals("LTCORA"))
				{
					VSEGMENT.add("LTCORA(sell)");
					
					VSEGMENT_TYPE.add("A");
					segment_index=1;
				}
				VSEGMENT_INDEX.add(segment_index);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSegment()
	{
		String function_nm="getSegment()";

		try
		{
			VSEGMENT.add("Supply Notice");
			VSEGMENT.add("Letter of Agreement");
			VSEGMENT.add("IGX");
			VSEGMENT.add("LTCORA(sell)");
			
			VSEGMENT_TYPE.add("S");
			VSEGMENT_TYPE.add("L");
			VSEGMENT_TYPE.add("X");
			VSEGMENT_TYPE.add("A");
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getContractSummary()
	{
		String function_nm="getContractSummary()";

		try
		{
			if(!segmentType.equals("0"))
			{
				VTEMP_SEGMENT_TYPE.add(segmentType);
				if(segmentType.equals("S"))
				{
					VTEMP_SEGMENT.add("Supply Notice");
				}
				else if(segmentType.equals("L"))
				{
					VTEMP_SEGMENT.add("Letter of Agreement");
				}
				else if(segmentType.equals("X"))
				{
					VTEMP_SEGMENT.add("IGX");
				}
				else if(segmentType.equals("A"))
				{
					VTEMP_SEGMENT.add("LTCORA(sell)");
				}
				else
				{
					VTEMP_SEGMENT.add("");
				}
			}
			else
			{
				VTEMP_SEGMENT=VSEGMENT;
				VTEMP_SEGMENT_TYPE=VSEGMENT_TYPE;
			}
			
			for(int i=0; i<VTEMP_SEGMENT_TYPE.size(); i++)
			{
				if(VTEMP_SEGMENT_TYPE.elementAt(i).toString().equals("A"))
				{
					getLTCORA_Summary(VTEMP_SEGMENT_TYPE.elementAt(i).toString()); 
				}
				else
				{
					int index=0;
					String due_date = "";
		  			String exchng_rate_cd ="";
		  			String exchng_rate_nm ="";
		  			double total_dcq=0;
		  			double total_tcq = 0;
					double totalsuppliedQty=0;
				    double totalbalancemmbtu=0;
					double total_scm=0;
					double totalsuppliedscm=0;
					double totalbalancescm=0;
		  			
					queryString="SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONT_REF_NO,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),"
							+ "RATE,RATE_UNIT,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),CONT_STATUS,TCQ,"
							+ "TO_CHAR(ENT_DT,'DD/MM/YYYY'),ENT_BY,TO_CHAR(FCC_DATE,'DD/MM/YYYY'),FCC_BY,IS_ALLOCATED,TRADE_REF_NO,AGMT_BASE,DCQ "
							+ "FROM FMS_SUPPLY_CONT_MST A "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
							+ "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') ";
					if(!counterparty_cd.equals("0"))
					{
						queryString+=" AND COUNTERPARTY_CD=? ";
					}
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, ""+VTEMP_SEGMENT_TYPE.elementAt(i));
					stmt.setString(3, to_dt);
					stmt.setString(4, from_dt);
					if(!counterparty_cd.equals("0"))
					{
						stmt.setString(5, counterparty_cd);
					}
					rset=stmt.executeQuery();
					while(rset.next())
					{
						index+=1;
						String companyCd = rset.getString(1)==null?"":rset.getString(1);
						String countpty_cd = rset.getString(2)==null?"":rset.getString(2);
						String agmt = rset.getString(3)==null?"":rset.getString(3);
						String agmt_rev = rset.getString(4)==null?"":rset.getString(4);
						String cont = rset.getString(5)==null?"":rset.getString(5);
						String cont_rev = rset.getString(6)==null?"":rset.getString(6);
						String cont_ref = rset.getString(7)==null?"":rset.getString(7);
						String start_dt = rset.getString(11)==null?"":rset.getString(11);
						String end_dt = rset.getString(12)==null?"":rset.getString(12);
						String trade_ref = rset.getString(20)==null?"":rset.getString(20);
						String agmt_base = rset.getString(21)==null?"":rset.getString(21);
						String dcq = rset.getString(22)==null?"":nf.format(rset.getDouble(22));	
						String cont_type=""+VTEMP_SEGMENT_TYPE.elementAt(i);
						
						if(cont_type.equals("X"))
						{
							cont_ref=trade_ref;
						}
						
						VCOUNTERPARTY_CD.add(countpty_cd);
						VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
						VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
						
						//String dealNo=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
						String dealNo=utilBean.NewDealMappingId(companyCd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
						
						if(agmt_base.equals("D"))
						{
							dealNo=dealNo+" <font style='background: #a6ff4d;'>[DLV]</font>";
						}
						VCONTRACT_TYPE.add(dealNo);
						
						VCONT_REF.add(cont_ref);
						VSIGNING_DT.add(rset.getString(8)==null?"":rset.getString(8));
						String rate = ""+rset.getDouble(9);
						String rate_unit = rset.getString(10)==null?"":rset.getString(10);
						VRATE.add(""+utilBean.RateNumberFormat(Double.parseDouble(rate), rate_unit));
						VRATE_UNIT.add(""+utilBean.getRateUnitNm(conn,rate_unit));
						VSTART_DT.add(start_dt);
						VEND_DT.add(end_dt);
						String cont_status_flg=rset.getString(13)==null?"":rset.getString(13);
						VCONT_STATUS_FLG.add(cont_status_flg);
						VCONT_STATUS.add(""+ContStatusName(cont_status_flg));
												
						total_dcq+=dcq.equals("")?0:Double.parseDouble(dcq);
						VDCQ.add(dcq);
						
						double suppliedQty=0;
						String min_dt="";
						String max_dt="";
						String query="SELECT SUM(QTY_MMBTU) "
			  					+ "FROM FMS_BUY_DAILY_ALLOCATION A  "
			  					+ "WHERE CONT_NO=? AND AGMT_NO=?  "
			  					+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=?"
			  					+ "AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY')"		//less than to date 
			  					//+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY')"		//greater than from date 
			  					+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B  "
			  					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO  "
			  					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD  "
			  					+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ  "
			  					+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE  "
			  					+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)";
			  			stmt3=conn.prepareStatement(query);
						stmt3.setString(1, cont);
						stmt3.setString(2, agmt);
						stmt3.setString(3, comp_cd);
						stmt3.setString(4, countpty_cd);
						stmt3.setString(5, cont_type);
						stmt3.setString(6, to_dt);
						//stmt3.setString(7, from_dt);
						rset3=stmt3.executeQuery();
						if(rset3.next())
						{
							suppliedQty=rset3.getDouble(1);
						}
						rset3.close();
						stmt3.close();
						
						queryString="SELECT SUM(QTY_MMBTU),TO_CHAR(MIN(GAS_DT),'DD/MM/YYYY'),TO_CHAR(MAX(GAS_DT),'DD/MM/YYYY') "
								+ "FROM FMS_DAILY_ALLOCATION_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
								+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
								+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ=B.PLANT_SEQ AND A.TRANSPORTER_CD=B.TRANSPORTER_CD "
								+ "AND A.TRANS_SEQ=B.TRANS_SEQ AND A.GAS_DT=B.GAS_DT AND A.CARGO_NO=B.CARGO_NO)";
						stmt_temp = conn.prepareStatement(queryString);
						stmt_temp.setString(1, companyCd);
						stmt_temp.setString(2, countpty_cd);
						stmt_temp.setString(3, agmt);
						stmt_temp.setString(4, cont);
						stmt_temp.setString(5, cont_type);
						rset_temp=stmt_temp.executeQuery();
						if(rset_temp.next())
						{
							//suppliedQty=rset_temp.getDouble(1);
							min_dt=rset_temp.getString(2)==null?"":rset_temp.getString(2);
							max_dt=rset_temp.getString(3)==null?"":rset_temp.getString(3);
						}
						rset_temp.close();
						stmt_temp.close();
						
						//suppliedQty = Double.parseDouble(allocUtil.getBestSupplyAllocationQty(conn, companyCd, countpty_cd, agmt, cont, cont_type,start_dt,end_dt,agmt_base));
						
						String cont_map=countpty_cd+"-"+cont_type+"-"+agmt+"-%-"+cont+"-%";
						int st_count=0;
						if(agmt_base.equals("D"))
						{
							query = "SELECT SUM(ALLOC_QTY) " 
									+ "FROM "
									+ "(SELECT SUM(EXIT_QTY_MMBTU) ALLOC_QTY "
									+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A " 
									+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
									//+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND SELL_CONT_MAP LIKE ? "
									+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
									+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) "
									+ "UNION " 
									+ "SELECT SUM(QTY_MMBTU) ALLOC_QTY "
									+ "FROM FMS_DAILY_ALLOCATION_DTL A " 
									+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND CONTRACT_TYPE=? "
									//+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) " 
									+ "AND GAS_DT NOT IN (SELECT DISTINCT GAS_DT FROM FMS_DAILY_TRANSPORTER_ALLOC A "
									+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
									//+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND SELL_CONT_MAP LIKE ? ))";
						}
						else
						{
							query="SELECT SUM(QTY_MMBTU) "
					  				+ "FROM FMS_DAILY_ALLOCATION_DTL A "
					  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND CONTRACT_TYPE=? "
									//+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
						}
						stmt_temp = conn.prepareStatement(query);
						if(agmt_base.equals("D"))
						{
							stmt_temp.setString(++st_count, companyCd);
							stmt_temp.setString(++st_count, "C");
							//stmt_temp.setString(++st_count, start_dt);
							stmt_temp.setString(++st_count, to_dt);
							stmt_temp.setString(++st_count, cont_map);
							stmt_temp.setString(++st_count, cont);
							stmt_temp.setString(++st_count, agmt);
							stmt_temp.setString(++st_count, companyCd);
							stmt_temp.setString(++st_count, countpty_cd);
							stmt_temp.setString(++st_count, cont_type);
							//stmt_temp.setString(++st_count, start_dt);
							stmt_temp.setString(++st_count, to_dt);
							stmt_temp.setString(++st_count, companyCd);
							stmt_temp.setString(++st_count, "C");
							//stmt_temp.setString(++st_count, start_dt);
							stmt_temp.setString(++st_count, to_dt);
							stmt_temp.setString(++st_count, cont_map);
						}
						else
						{
							stmt_temp.setString(++st_count, cont);
							stmt_temp.setString(++st_count, agmt);
							stmt_temp.setString(++st_count, companyCd);
							stmt_temp.setString(++st_count, countpty_cd);
							stmt_temp.setString(++st_count, cont_type);
							//stmt_temp.setString(++st_count, start_dt);
							stmt_temp.setString(++st_count, to_dt);
						}
						rset_temp = stmt_temp.executeQuery();
						if(rset_temp.next())
						{
							suppliedQty=Double.parseDouble(nf.format(rset_temp.getDouble(1)));
						}
						stmt_temp.close();
						rset_temp.close();
						
						double tcq = rset.getDouble(14);
						double balanceQty=tcq-suppliedQty;
						double mmscm = 38900;
						
						double scm = tcq/mmscm;
						double suppliedscm = suppliedQty/mmscm;
						double balancescm = balanceQty/mmscm;
						VTCQ.add(nf.format(tcq));
						VTCQ_MMSCM.add(nf.format(scm));
						VSUPPLIED_QTY_MMBTU.add(nf.format(suppliedQty));
						VSUPPLIED_QTY_MMSCM.add(nf.format(suppliedscm));
						VBALANCE_QTY_MMBTU.add(nf.format(balanceQty));
						VBALANCE_QTY_MMSCM.add(nf.format(balancescm));
						VALLOC_MIN_DT.add(min_dt);
						VALLOC_MAX_DT.add(max_dt);
						
						total_tcq += tcq;
						totalsuppliedQty += suppliedQty;
					    totalbalancemmbtu += balanceQty;
						total_scm += scm;
						totalsuppliedscm += suppliedscm;
						totalbalancescm += balancescm;
						
						String ent_dt=rset.getString(15)==null?"":rset.getString(15);
						String ent_by=rset.getString(16)==null?"":rset.getString(16);
						String fcc_dt=rset.getString(17)==null?"":rset.getString(17);
						String fcc_by=rset.getString(18)==null?"":rset.getString(18);
						VENT_DT.add(ent_dt);
						VENT_BY.add(""+utilBean.getEmpName(conn,ent_by));
						VAPRV_DT.add(fcc_dt);
						VAPRV_BY.add(""+utilBean.getEmpName(conn,fcc_by));
						VIS_ALLOCATED.add(rset.getString(19)==null?"N":rset.getString(19));
						
						String price_type="";
						String price_mapping=countpty_cd+"-"+agmt+"-"+cont;
						int price_count=0;
						
						String query2="SELECT COUNT(*) "
								+ "FROM FMS_CONT_PRICE_DTL "
								+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
								+ "AND MAPPING_ID=? ";
						stmt1=conn.prepareStatement(query2);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, cont_type);
						stmt1.setString(3, price_mapping);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							price_count=rset1.getInt(1);
						}
						rset1.close();
						stmt1.close();
						price_type=price_count>0?"Float":"Fixed";
						
						VPRICE_TYPE.add(price_type);
						
						
						String DelvNm="";
					  	queryString1 = "SELECT TRANSPORTER_CD,PLANT_SEQ_NO "
					  			+ "FROM FMS_SUPPLY_CONT_TRANSPTR "
					  			+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? "
					  			+ "AND CONT_NO=? AND CONT_REV=? "
					  			+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=?";
					  	stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, companyCd);
						stmt1.setString(2, agmt);
						stmt1.setString(3, agmt_rev);
						stmt1.setString(4, cont);
						stmt1.setString(5, cont_rev);
						stmt1.setString(6, countpty_cd);
						stmt1.setString(7, cont_type);
						rset1=stmt1.executeQuery();
			  			while(rset1.next())
			  			{
			  				String trans_cd = rset1.getString(1)==null?"0":rset1.getString(1);
			  				String plant_seq = rset1.getString(2)==null?"0":rset1.getString(2);
			  				if(DelvNm.equals(""))
			  				{
			  					DelvNm+=""+utilBean.getCounterpartyPlantABBR(conn,trans_cd, companyCd, plant_seq, "R");
			  				}
			  				else
			  				{
			  					DelvNm+=","+utilBean.getCounterpartyPlantABBR(conn,trans_cd, companyCd, plant_seq, "R");
			  				}
			  			}
			  			VDELV_POINT.add(DelvNm);
			  			rset1.close();
			  			stmt1.close();
			  			
			  			String BuUnit="";
					  	queryString1 = "SELECT COMPANY_CD,PLANT_SEQ_NO "
					  			+ "FROM FMS_SUPPLY_CONT_BU "
					  			+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? "
					  			+ "AND CONT_NO=? AND CONT_REV=? "
					  			+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=?";
					  	stmt0 = conn.prepareStatement(queryString1);
						stmt0.setString(1, companyCd);
						stmt0.setString(2, agmt);
						stmt0.setString(3, agmt_rev);
						stmt0.setString(4, cont);
						stmt0.setString(5, cont_rev);
						stmt0.setString(6, countpty_cd);
						stmt0.setString(7, cont_type);
						rset0=stmt0.executeQuery();
			  			while(rset0.next())
			  			{
			  				String ownercd = rset0.getString(1)==null?"0":rset0.getString(1);
			  				String plant_seq = rset0.getString(2)==null?"0":rset0.getString(2);
			  				if(BuUnit.equals(""))
			  				{
			  					BuUnit+=""+utilBean.getCounterpartyPlantABBR(conn,ownercd, companyCd, plant_seq,"B");
			  				}
			  				else
			  				{
			  					BuUnit+=","+utilBean.getCounterpartyPlantABBR(conn,ownercd, companyCd, plant_seq, "B");
			  				}
			  			}
			  			VBU_POINT.add(BuUnit);
			  			rset0.close();
			  			stmt0.close();
			  			
			  			String plantUnits="";
					  	queryString1 = "SELECT COUNTERPARTY_CD,PLANT_SEQ_NO "
					  			+ "FROM FMS_SUPPLY_CONT_PLANT "
					  			+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? "
					  			+ "AND CONT_NO=? AND CONT_REV=? "
					  			+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=?";
					  	stmt2 = conn.prepareStatement(queryString1);
						stmt2.setString(1, companyCd);
						stmt2.setString(2, agmt);
						stmt2.setString(3, agmt_rev);
						stmt2.setString(4, cont);
						stmt2.setString(5, cont_rev);
						stmt2.setString(6, countpty_cd);
						stmt2.setString(7, cont_type);
						rset2=stmt2.executeQuery();
			  			while(rset2.next())
			  			{
			  				String counterpty_cd = rset2.getString(1)==null?"0":rset2.getString(1);
			  				String plant_seq = rset2.getString(2)==null?"0":rset2.getString(2);
			  				
			  				if(plantUnits.equals(""))
			  				{
			  					plantUnits+=""+utilBean.getCounterpartyPlantABBR(conn,counterpty_cd, companyCd, plant_seq, "C");
			  				}
			  				else
			  				{
			  					plantUnits+=","+utilBean.getCounterpartyPlantABBR(conn,counterpty_cd, companyCd, plant_seq, "C");
			  				}
			  			}
			  			VPLANT_UNIT.add(plantUnits);
			  			rset2.close();
			  			stmt2.close();
			  			
			  			queryString4="SELECT DUE_DATE,EXCHNG_RATE_CD "
								+ "FROM FMS_SUPPLY_BILLING_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? "
								+ "AND CONT_NO=? "
								+ "AND CONTRACT_TYPE=? "
								+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_SUPPLY_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
								+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO) AND EFF_DT<=TO_DATE(?,'DD/MM/YYYY')";
			  			stmt4 = conn.prepareStatement(queryString4);
						stmt4.setString(1, companyCd);
						stmt4.setString(2, countpty_cd);
						stmt4.setString(3, agmt);
						stmt4.setString(4, cont);
						stmt4.setString(5, cont_type);
						stmt4.setString(6, to_dt);
						rset4=stmt4.executeQuery();
						if(rset4.next())
						{
							due_date=rset4.getString(1)==null?"":rset4.getString(1);
							exchng_rate_cd=rset4.getString(2)==null?"":rset4.getString(2);
							
							if(!exchng_rate_cd.equals("0"))
							{
								queryString = "SELECT EXC_RATE_NM "
										+ "FROM FMS_EXCHG_RATE_MST "
										+ "WHERE EXC_RATE_CD=? ";
								stmt_temp0 = conn.prepareStatement(queryString);
								stmt_temp0.setString(1, exchng_rate_cd);
								rset_temp0=stmt_temp0.executeQuery();
								if(rset_temp0.next())
								{
									exchng_rate_nm =rset_temp0.getString(1)==null?"":rset_temp0.getString(1);
								}
								rset_temp0.close();
								stmt_temp0.close();
							}
						}
						VDUE_DATE.add(due_date);
						VEXCHANGE_RATE.add(exchng_rate_cd);
						VEXCHNG_RATE_NM.add(exchng_rate_nm);
						rset4.close();
						stmt4.close();
					}
					VTOTAL_MMBTU.add(nf.format(total_tcq));
					VTOTAL_SCM.add(nf.format(total_scm));
					VTOTALSUPPLIED_MMBTU.add(nf.format(totalsuppliedQty));
					VTOTALSUPPLIED_SCM.add(nf.format(totalsuppliedscm));
					VTOTALBALANCE_MMBTU.add(nf.format(totalbalancemmbtu));
					VTOTALBALANCE_SCM.add(nf.format(totalbalancescm));
					VTOTAL_DCQ.add(nf.format(total_dcq));
					VINDEX.add(index);
					
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
	
	//AP20250113 
	public void getLTCORA_Summary(String segment)
	{
		String function_nm="getLTCORA_Summary()";
		try
		{
			String due_date = "";
  			String exchng_rate_cd ="";
  			String exchng_rate_nm ="";
  			double total_dcq=0;
			double lTotalQty=0;
			double lUnloadedTotalQty=0;
			double balTotalQty=0;
			balTotalQty_str="";
			bookedToolTip = "Booked qty\n= IF (ADQ >= 0) {ADQ MMBTU -(ADQ MMBTU*SUG%)}\nELSE {EDQ MMBTU -(EDQ MMBTU*SUG%}";
			int index=0;
			
			String queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.CARGO_REF, TO_CHAR(A.ACTUAL_RECPT_DT,'DD/MM/YYYY'), "
					+ "TO_CHAR((ACTUAL_RECPT_DT + COALESCE(STORAGE_EXT_DAYS, 0) + COALESCE(STORAGE_DAYS-1, 0)),'DD/MM/YYYY'),A.AGMT_TYPE, A.AGMT_REV, A.AGMT_NO, A.CONT_NO, A.CONT_REV, A.CONTRACT_TYPE,EDQ_QTY,       "
					+ "TO_CHAR(A.QQ_DT,'DD/MM/YYYY'), A.CARGO_NO, TO_CHAR(A.ACTUAL_RECPT_DT,'DD/MM/YYYY'), C.SUG, C.CONT_REF_NO, C.CONT_STATUS, C.LTCORA_TARIFF, C.LTCORA_TARIFF_UNIT,  "
					+ "NVL((A.STORAGE_DAYS-1),0),A.STORAGE_EXT_DAYS, C.EXTEND_STORAGE, TO_CHAR(C.SIGNING_DT,'DD/MM/YYYY'),TO_CHAR(C.ENT_DT,'DD/MM/YYYY'),C.ENT_BY,TO_CHAR(FCC_DATE,'DD/MM/YYYY'),FCC_BY,A.CSOC_QTY "
					+ "FROM FMS_LTCORA_CONT_CARGO_DTL A, FMS_LTCORA_CONT_MST C  "
					+ "WHERE A.COMPANY_CD=? AND A.AGMT_TYPE=?"
					+ "AND A.CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_TYPE=B.AGMT_TYPE "
					+ "AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.ACTUAL_RECPT_DT<=TO_DATE(?,'DD/MM/YYYY') AND "
					+ "NVL((TO_DATE(A.ACTUAL_RECPT_DT)+A.STORAGE_DAYS-1+ A.STORAGE_EXT_DAYS),(TO_DATE(A.ACTUAL_RECPT_DT)+A.STORAGE_DAYS-1) )>= TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.AGMT_TYPE=C.AGMT_TYPE "
					+ "AND A.AGMT_NO=C.AGMT_NO AND A.AGMT_REV=C.AGMT_REV AND A.CONTRACT_TYPE = C.CONTRACT_TYPE  AND A.CONT_NO=C.CONT_NO  "
					+ "AND A.CONT_REV=C.CONT_REV" ;
			if(!counterparty_cd.equals("0"))
			{
				queryString+=" AND A.COUNTERPARTY_CD=?  ";
			}
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, segment);
			stmt.setString(3, to_dt);
			stmt.setString(4, from_dt);
			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(5, counterparty_cd);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				index+=1;
				String own_cd = rset.getString(1)==null?"":rset.getString(1);
				String coutpty_cd = rset.getString(2)==null?"":rset.getString(2);
				String cargo_ref = rset.getString(3)==null?"":rset.getString(3);
				String edq_from_dt = rset.getString(4)==null?"":rset.getString(4);
				String edq_to_dt = rset.getString(5)==null?"":rset.getString(5);
				String agmt_typ = rset.getString(6)==null?"":rset.getString(6);
				String agmt_rev = rset.getString(7)==null?"":rset.getString(7);
				String agmt_no = rset.getString(8)==null?"":rset.getString(8);
				String cont_no = rset.getString(9)==null?"":rset.getString(9);
				String cont_rev = rset.getString(10)==null?"":rset.getString(10);
				String cont_tpy = rset.getString(11)==null?"":rset.getString(11);
				String edq_qty = rset.getString(12)==null?"":rset.getString(12);
				String qq_dt = rset.getString(13)==null?"":rset.getString(13);
				String cargo_no = rset.getString(14)==null?"":rset.getString(14);
				String actual_recpt_dt = rset.getString(15)==null?"":rset.getString(15);
				String sug_per = rset.getString(16)==null?"":rset.getString(16);
				double sug = rset.getDouble(16);
  				String cont_ref = rset.getString(17)==null?"":rset.getString(17);
  				String status_flag = rset.getString(18)==null?"":rset.getString(18);
  				double rate = rset.getDouble(19);
  				String price_unit = rset.getString(20)==null?"":rset.getString(20);
  				String storage_days = rset.getString(21)==null?"":rset.getString(21);
  				String storage_ext_days = rset.getString(22)==null?"":rset.getString(22);
  				String extend_storage = rset.getString(23)==null?"":rset.getString(23);
  				String signing_dt = rset.getString(24)==null?"":rset.getString(24);
  				String ent_dt=rset.getString(25)==null?"":rset.getString(25);
				String ent_by=rset.getString(26)==null?"":rset.getString(26);
				String fcc_dt=rset.getString(27)==null?"":rset.getString(27);
				String fcc_by=rset.getString(28)==null?"":rset.getString(28);
				String csoc=rset.getString(29)==null?"":nf.format(rset.getDouble(29));
				
				total_dcq+=csoc.equals("")?0:Double.parseDouble(csoc);
				VDCQ.add(csoc);
				
				VENT_DT.add(ent_dt);
				VENT_BY.add(""+utilBean.getEmpName(conn,ent_by));
				VAPRV_DT.add(fcc_dt);
				VAPRV_BY.add(""+utilBean.getEmpName(conn,fcc_by));
  				String storage_end_dt = dateUtil.getDate(actual_recpt_dt, storage_days);
  				String storage_ext_dt = dateUtil.getDate(storage_end_dt, storage_ext_days);
  				
  				//From dt as Actual Recept date and to dt as storage end date or storage_ext_dt
  				String cargo_to_dt = !storage_ext_days.equals("")?storage_ext_dt:storage_end_dt;
  				
				String cont_mapping = utilBean.NewDealMappingId(own_cd,coutpty_cd,agmt_no,agmt_rev,cont_no,cont_rev,cont_tpy,cargo_no);	//Contract-mapping
				
				//SUG if cargo sug is modified
				String queryString1="SELECT SUG FROM FMS_LTCORA_CONT_CARGO_MOD "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE = ? AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=? AND CONT_NO=? AND CONT_REV=? AND "
						+ "CARGO_NO=?";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1,own_cd);
				stmt1.setString(2,coutpty_cd);
				stmt1.setString(3,agmt_typ);
				stmt1.setString(4,agmt_no);
				stmt1.setString(5,agmt_rev);
				stmt1.setString(6,cont_tpy);
				stmt1.setString(7,cont_no);
				stmt1.setString(8,cont_rev);
				stmt1.setString(9,cargo_no);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					sug_per = rset1.getString(1)==null?"":rset1.getString(1);
					sug = rset1.getDouble(1);
				}
				
				rset1.close();
				stmt1.close();
				
				//for finding ADQ
				int selCnt2=0;
				double adq=0;
				int adq_count = 0;
				String adq_str="";
				String queryString5 = "SELECT ADQ_QTY,ADQ_DT "
						+ "FROM FMS_LTCORA_CONT_CARGO_ADQ A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND BUY_SALE=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? "
						+ "AND CONTRACT_TYPE=? AND CONT_NO=? "
						+ " AND CARGO_NO=? "
						+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_ADQ B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE "
						+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
						+ "AND A.CONT_NO=B.CONT_NO AND A.CARGO_NO=B.CARGO_NO)"
						+ "ORDER BY ADQ_DT ASC";
				stmt5 = conn.prepareStatement(queryString5);
				stmt5.setString(++selCnt2, own_cd);
				stmt5.setString(++selCnt2, coutpty_cd);
				stmt5.setString(++selCnt2, "C");
				stmt5.setString(++selCnt2, agmt_typ);
				stmt5.setString(++selCnt2, agmt_no);
				stmt5.setString(++selCnt2, cont_tpy);
				stmt5.setString(++selCnt2, cont_no);
				stmt5.setString(++selCnt2, cargo_no);
				rset5=stmt5.executeQuery();
				while(rset5.next())
				{
					adq_str = rset5.getString(1)==null?"":rset5.getString(1);
					if(!adq_str.equals(""))
					{
						adq+= Double.parseDouble(adq_str);
						adq_count++;		// for checking whether adq is configured or not, if not then adq_count = 0  
					}
				}

				rset5.close();
				stmt5.close();
				
				if(adq_count==0)
				{
					adq = -1;		//for checking whether adq is configured or not, if not configured then adq = -1 
				}
				
				double booked_qty = adq>=0?(adq - (adq*sug/100)):(Double.parseDouble(edq_qty)- ((Double.parseDouble(edq_qty)*sug/100)));
				lTotalQty+=booked_qty;
				lTotalQty=Double.parseDouble(nf.format(lTotalQty));
  				
  				String booked_info = adq>=0?(adq+"- ("+adq+"*"+sug_per+"%)"):(edq_qty+"- ("+edq_qty+"*"+sug_per+"%)");
				//for owner business unit 
				String BuUnit="";
				
				String DelvNm=""; 
				queryString1="SELECT TRANSPORTER_CD, PLANT_SEQ_NO "
						+ "FROM FMS_LTCORA_CONT_TRANSPTR "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=? AND BUY_SALE=?";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, own_cd);
				stmt1.setString(2, coutpty_cd);
				stmt1.setString(3, cont_no);
				stmt1.setString(4, cont_rev);
				stmt1.setString(5, agmt_no);
				stmt1.setString(6, agmt_rev);
				stmt1.setString(7, cont_tpy);
				stmt1.setString(8, "C");
				rset1=stmt1.executeQuery();
				while(rset1.next())
	  			{
	  				String trans_cd = rset1.getString(1)==null?"0":rset1.getString(1);
	  				String plant_seq = rset1.getString(2)==null?"0":rset1.getString(2);
	  				if(DelvNm.equals(""))
	  				{
	  					DelvNm+=""+utilBean.getCounterpartyPlantABBR(conn,trans_cd, own_cd, plant_seq, "R");
	  				}
	  				else
	  				{
	  					DelvNm+=","+utilBean.getCounterpartyPlantABBR(conn,trans_cd, own_cd, plant_seq, "R");
	  				}
	  			}
	  			VDELV_POINT.add(DelvNm);
				rset1.close();
				stmt1.close();
				
				String QueryString1 = "SELECT COMPANY_CD,PLANT_SEQ_NO  "
			  			+ "FROM FMS_LTCORA_CONT_BU  "
			  			+ "WHERE COMPANY_CD=?  AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?  "
			  			+ "AND CONT_NO=? AND CONT_REV=?  "
			  			+ "AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=?";
			  	stmt1=conn.prepareStatement(QueryString1);
			  	stmt1.setString(1, own_cd);
			  	stmt1.setString(2, agmt_typ);
			  	stmt1.setString(3, agmt_no);
			  	stmt1.setString(4, agmt_rev);
			  	stmt1.setString(5, cont_no);
			  	stmt1.setString(6, cont_rev);
			  	stmt1.setString(7, coutpty_cd);
			  	stmt1.setString(8, cont_tpy);
			  	rset1=stmt1.executeQuery();
	  			while(rset1.next())
	  			{
	  				String ownercd = rset1.getString(1)==null?"0":rset1.getString(1);
	  				String plant_seq = rset1.getString(2)==null?"0":rset1.getString(2);
	  				if(BuUnit.equals(""))
	  				{
	  					BuUnit+=""+utilBean.getCounterpartyPlantABBR(conn,ownercd, own_cd, plant_seq, "B");
	  				}
	  				else
	  				{
	  					BuUnit+=","+utilBean.getCounterpartyPlantABBR(conn,ownercd, own_cd, plant_seq, "B");
	  				}
	  			}
	  			rset1.close();
	  			stmt1.close();
				
	  			String plantUnits="";
	  			queryString2="SELECT COUNTERPARTY_CD,PLANT_SEQ_NO "
						+ "FROM FMS_LTCORA_CONT_PLANT "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=? AND AGMT_TYPE=? AND BUY_SALE=?";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, own_cd);
				stmt2.setString(2, coutpty_cd);
				stmt2.setString(3, cont_no);
				stmt2.setString(4, cont_rev);
				stmt2.setString(5, agmt_no);
				stmt2.setString(6, agmt_rev);
				stmt2.setString(7, cont_tpy);
				stmt2.setString(8, agmt_typ);
				stmt2.setString(9, "C");
				rset2=stmt2.executeQuery();
				while(rset2.next())
	  			{
	  				String counterpty_cd = rset2.getString(1)==null?"0":rset2.getString(1);
	  				String plant_seq = rset2.getString(2)==null?"0":rset2.getString(2);
	  				
	  				if(plantUnits.equals(""))
	  				{
	  					plantUnits+=""+utilBean.getCounterpartyPlantABBR(conn,counterpty_cd, own_cd, plant_seq, "C");
	  				}
	  				else
	  				{
	  					plantUnits+=","+utilBean.getCounterpartyPlantABBR(conn,counterpty_cd, own_cd, plant_seq, "C");
	  				}
	  			}
	  			VPLANT_UNIT.add(plantUnits);
				rset2.close();
				stmt2.close();
	  			
	  			queryString3="SELECT DUE_DATE,EXCHNG_RATE_CD "
						+ "FROM FMS_LTCORA_CONT_BILLING_DTL A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONT_NO=? AND CONT_REV=? AND CONTRACT_TYPE=? AND BUY_SALE=? ";
	  			stmt3 = conn.prepareStatement(queryString3);
				stmt3.setString(1, own_cd);
				stmt3.setString(2, coutpty_cd);
				stmt3.setString(3, agmt_no);
				stmt3.setString(4, agmt_rev);
				stmt3.setString(5, cont_no);
				stmt3.setString(6, cont_rev);
				stmt3.setString(7, cont_tpy);
				stmt3.setString(8, "C");
				rset3=stmt3.executeQuery();
				if(rset3.next())
				{
					due_date=rset3.getString(1)==null?"":rset3.getString(1);
					exchng_rate_cd=rset3.getString(2)==null?"":rset3.getString(2);
					
					if(!exchng_rate_cd.equals("0"))
					{
						queryString = "SELECT EXC_RATE_NM "
								+ "FROM FMS_EXCHG_RATE_MST "
								+ "WHERE EXC_RATE_CD=? ";
						stmt_temp0 = conn.prepareStatement(queryString);
						stmt_temp0.setString(1, exchng_rate_cd);
						rset_temp0=stmt_temp0.executeQuery();
						if(rset_temp0.next())
						{
							exchng_rate_nm =rset_temp0.getString(1)==null?"":rset_temp0.getString(1);
						}
						rset_temp0.close();
						stmt_temp0.close();
					}
				}
				VDUE_DATE.add(due_date);
				VEXCHANGE_RATE.add(exchng_rate_cd);
				VEXCHNG_RATE_NM.add(exchng_rate_nm);
				rset3.close();
				stmt3.close();
	  			
	  			//for allocation details
	  			String alloc_start_dt="";
	  			String alloc_end_dt  = "";
	  			double unldl = 0;
	  			String query="SELECT SUM(QTY_MMBTU) "
		  				+ "FROM FMS_DAILY_ALLOCATION_DTL A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? AND CARGO_NO=? "
						+ "AND GAS_DT <= TO_DATE(?,'DD/MM/YYYY') "		//less than to date 
						//+ "AND GAS_DT >= TO_DATE(?,'DD/MM/YYYY') "		//greater than from date 
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
						+ "AND B.GAS_DT=A.GAS_DT and A.CARGO_NO=B.CARGO_NO) ";
	  			stmt4 = conn.prepareStatement(query);
	  			stmt4.setString(1,cont_no );
	  			stmt4.setString(2,agmt_no );
	  			stmt4.setString(3,own_cd);
	  			stmt4.setString(4,coutpty_cd);
	  			stmt4.setString(5,cont_tpy);
	  			stmt4.setString(6,cargo_no);
	  			stmt4.setString(7,to_dt);
	  			//stmt4.setString(8,from_dt);
	  			rset4 = stmt4.executeQuery();
	  			if(rset4.next())
				{
	  				unldl = rset4.getDouble(1);
	  				lUnloadedTotalQty+=unldl;
	  				lUnloadedTotalQty=Double.parseDouble(nf.format(lUnloadedTotalQty));
				}
	  			rset4.close();
	  			stmt4.close();
	  			
	  			String queryString4="SELECT TO_CHAR(MIN(GAS_DT),'DD/MM/YYYY'),TO_CHAR(MAX(GAS_DT),'DD/MM/YYYY'),SUM(QTY_MMBTU) "
		  				+ "FROM FMS_DAILY_ALLOCATION_DTL A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? AND CARGO_NO=?"
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
						+ "AND B.GAS_DT=A.GAS_DT and A.CARGO_NO=B.CARGO_NO) ";
	  			stmt4 = conn.prepareStatement(queryString4);
	  			stmt4.setString(1,cont_no);
	  			stmt4.setString(2,agmt_no);
	  			stmt4.setString(3,own_cd);
	  			stmt4.setString(4,coutpty_cd);
	  			stmt4.setString(5,cont_tpy);
	  			stmt4.setString(6,cargo_no);
	  			rset4 = stmt4.executeQuery();
	  			if(rset4.next())
				{
	  				alloc_start_dt = rset4.getString(1)==null?"":rset4.getString(1);
	  				alloc_end_dt = rset4.getString(2)==null?"":rset4.getString(2);
	  				//unldl = rset4.getDouble(3);
	  				//lUnloadedTotalQty+=unldl;
	  				//lUnloadedTotalQty=Double.parseDouble(nf.format(lUnloadedTotalQty));
				}
	  			rset4.close();
	  			stmt4.close();
	  			if(price_unit.equals("1"))
	  			{	  					
	  				VRATE_UNIT.add("INR");
	  			}
	  			else if(price_unit.equals("2"))
	  			{	  					
	  				VRATE_UNIT.add("USD");
	  			}
	  			
	  			double balance_qty = booked_qty-unldl;
	  			balTotalQty+=balance_qty;
	  			balTotalQty=Double.parseDouble(nf.format(balTotalQty));
	  			String bal = utilBean.RateNumberFormat(balance_qty,"1");
	  			String bal_info = ""+nf.format(booked_qty)+"-"+""+nf.format(unldl);
	  			double mmscm = 38900;
	  			double balance_qty_scm = balance_qty/mmscm;
	  			String bal_mmscm = utilBean.RateNumberFormat(balance_qty_scm,"1");
	  			
	  			VBAL_INFO.add(bal_info);
	  			VCONT_STATUS_FLG.add(status_flag);
	  			VCONT_STATUS.add(""+ContStatusName(status_flag));
	  			VTCQ.add(nf.format(booked_qty));
	  			VTCQ_MMSCM.add(nf.format(booked_qty/mmscm));
	  			VCONT_REF.add(cargo_ref);
	  			VRATE.add(nf.format(rate));
				VCOUNTERPARTY_CD.add(coutpty_cd);
				VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,coutpty_cd));
				VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,coutpty_cd));
				VCONT_NO.add(cont_no);
				VCONT_REV_NO.add(cont_rev);
				VSTART_DT.add(actual_recpt_dt);
				VEND_DT.add(cargo_to_dt);
				VPRICE_TYPE.add("Fixed");
				VAGMT_NO.add(agmt_no);
				VAGMT_REV_NO.add(agmt_rev);
				VCONTRACT_TYPE.add(cont_mapping);
				VALLOC_MIN_DT.add(alloc_start_dt);	
				VALLOC_MAX_DT.add(alloc_end_dt);
				VSUPPLIED_QTY_MMBTU.add(nf.format(unldl));
				VSUPPLIED_QTY_MMSCM.add(nf.format(unldl/mmscm));
				VBU_POINT.add(BuUnit);
				VBALANCE_QTY_MMBTU.add(bal);		//Added for column Balance qty 
				VBALANCE_QTY_MMSCM.add(bal_mmscm);		//Added for column Balance qty 
				VBOOKED_INFO.add(booked_info);
				VSIGNING_DT.add(signing_dt);			
			}
			VTOTAL_MMBTU.add(nf.format(lTotalQty));
			VTOTAL_SCM.add(nf.format(lTotalQty/38900));
			VTOTALSUPPLIED_MMBTU.add(nf.format(lUnloadedTotalQty));
			VTOTALSUPPLIED_SCM.add(nf.format(lUnloadedTotalQty/38900));
			VTOTALBALANCE_MMBTU.add(nf.format(balTotalQty));
			VTOTALBALANCE_SCM.add(nf.format(balTotalQty/38900));
			VTOTAL_DCQ.add(total_dcq);
			VINDEX.add(index);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
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
	
	public double getAllocatedQty(String compCd, String counterpty_cd,String cont,String agmt,String cont_type)
	{
		String function_nm="getAllocatedQty()";

		double alloc_qty=0;
		try
		{
			queryString="SELECT SUM(QTY_MMBTU) "
					+ "FROM FMS_DAILY_ALLOCATION_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
					+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ=B.PLANT_SEQ AND A.TRANSPORTER_CD=B.TRANSPORTER_CD "
					+ "AND A.TRANS_SEQ=B.TRANS_SEQ AND A.GAS_DT=B.GAS_DT AND A.CARGO_NO=B.CARGO_NO)";
			stmt_temp = conn.prepareStatement(queryString);
			stmt_temp.setString(1, compCd);
			stmt_temp.setString(2, counterpty_cd);
			stmt_temp.setString(3, agmt);
			stmt_temp.setString(4, cont);
			stmt_temp.setString(5, cont_type);
			rset_temp=stmt_temp.executeQuery();
			if(rset_temp.next())
			{
				alloc_qty=rset_temp.getDouble(1);
			}
			rset_temp.close();
			stmt_temp.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return alloc_qty;
	}
	
	public void getDispSegment_type()
	{
		String function_nm="getDispSegment_type()";

		try
		{
			VDISP_SEGMENT.add("Supply Notice");
			VDISP_SEGMENT.add("Letter of Agreement");
			VDISP_SEGMENT.add("IGX");
			VDISP_SEGMENT.add("LTCORA(sell)");
			
			VDISP_SEGMENT_TYPE.add("S");
			VDISP_SEGMENT_TYPE.add("L");
			VDISP_SEGMENT_TYPE.add("X");
			VDISP_SEGMENT_TYPE.add("A");
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
	String agmtType="";
	public void setAgmtType(String agmtType) {this.agmtType = agmtType;}
	
	String segmentType = "";
	String counterparty_cd = "";
	String from_dt = "";
	String to_dt = "";
	String month = "";
	String year = "";
	
	public void setSegmentType(String segmentType) {this.segmentType = segmentType;}
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setFrom_dt(String from_dt) {this.from_dt = from_dt;}
	public void setTo_dt(String to_dt) {this.to_dt = to_dt;}
	public void setMonth(String month) {this.month = month;}
	public void setYear(String year) {this.year = year;}
	
	String balTotalQty_str = "";
	String bookedToolTip = "";
	String comp_abbr="";
	
	public String getBalTotalQty_str() {return balTotalQty_str;}
	public String getBookedToolTip() {return bookedToolTip;}
	public String getComp_abbr() {return comp_abbr;}
	
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
	Vector VTCQ = new Vector();
	Vector VTCQ_MMSCM = new Vector();
	Vector VSIGNING_DT = new Vector();
	Vector VSTART_DT = new Vector();
	Vector VEND_DT = new Vector();
	Vector VRATE = new Vector();
	Vector VRATE_UNIT = new Vector();
	Vector VDIS_CONT_MAPPING = new Vector();
	Vector VCONT_STATUS = new Vector();
	Vector VCONT_STATUS_FLG = new Vector();
	Vector VCONT_REF = new Vector();
	Vector VENT_DT = new Vector();
	Vector VENT_BY = new Vector();
	Vector VAPRV_DT = new Vector();
	Vector VAPRV_BY = new Vector();
	Vector VSEGMENT = new Vector();
	Vector VSEGMENT_TYPE = new Vector();
	Vector VTEMP_SEGMENT = new Vector();
	Vector VTEMP_SEGMENT_TYPE = new Vector();
	Vector VINDEX = new Vector();
	Vector VPRICE_TYPE = new Vector();
	Vector VDELV_POINT = new Vector();
	Vector VBU_POINT = new Vector();
	Vector VPLANT_UNIT = new Vector();
	Vector VIS_ALLOCATED = new Vector();
	Vector VSUPPLIED_QTY_MMBTU = new Vector();
	Vector VSUPPLIED_QTY_MMSCM = new Vector();
	Vector VBALANCE_QTY_MMBTU = new Vector();
	Vector VBALANCE_QTY_MMSCM = new Vector();
	Vector VALLOC_MIN_DT = new Vector();
	Vector VALLOC_MAX_DT = new Vector();
	
	Vector VAPPROVED_FLAG = new Vector();
	Vector VAPPROVED_DATE = new Vector();
	Vector VAPPROVED_COUNTERPARTY_CD = new Vector();
	Vector VAPPROVED_COUNTERPARTY_NM = new Vector();
	Vector VCONTRACT_TYPE = new Vector();
	Vector VBALANCEQTY = new Vector();
	Vector VTEMP_RATE_UNIT = new Vector();
	Vector VSALES_AMT_INR = new Vector();
	Vector VSALES_AMT_USD = new Vector();
	Vector VAPPROVED_COLOR = new Vector();
	Vector VSUB_INDEX = new Vector();
	Vector VTOTAL_TCQ = new Vector();
	Vector VTOTAL_SUPPLIED_QTY_MMBTU = new Vector();
	Vector VTOTAL_BALANCE_QTY_MMBTU = new Vector();
	Vector VTOTAL_SALES_AMT_INR = new Vector();
	Vector VTOTAL_SALES_AMT_USD = new Vector();
	Vector VFINAL_TCQ = new Vector();
	Vector VVARIATION_TCQ = new Vector();
	Vector VDUE_DATE = new Vector();
	Vector VEXCHANGE_RATE = new Vector();
	Vector VEXCHNG_RATE_NM = new Vector();
	
	Vector VTOTALBALANCE_SCM = new Vector();
	Vector VTOTALBALANCE_MMBTU = new Vector();
	Vector VTOTALSUPPLIED_SCM = new Vector();
	Vector VTOTALSUPPLIED_MMBTU = new Vector();
	Vector VTOTAL_SCM = new Vector();
	Vector VTOTAL_MMBTU = new Vector();
	
	Vector VBAL_INFO = new Vector();
	Vector VBOOKED_QTY = new Vector();
	Vector VCONT_REF_NO = new Vector();
	Vector VMIN_ALLOC_DT = new Vector();
	Vector VMAX_ALLOC_DT = new Vector();
	Vector VUNLOADED_QTY = new Vector();
	Vector VTRADER_PLANT = new Vector();
	Vector VBALANCE_QTY = new Vector();
	Vector VBOOKED_INFO = new Vector();
	Vector VTOTAL_QUANTITY = new Vector();
	Vector VTOTAL_UNLOADED_QUANTITY = new Vector();
	Vector VHEADER_SEGMENT = new Vector();
	Vector VSEGMENT_INDEX = new Vector();
	
	Vector VAGMT_DISP_MAP = new Vector();
	Vector VAGMT_START_DT = new Vector();
	Vector VAGMT_END_DT = new Vector();
	Vector VEXPIRY_STATUS = new Vector();
	
	Vector VDCQ = new Vector();
	Vector VTOTAL_DCQ = new Vector();
	Vector VDISP_SEGMENT = new Vector();
	Vector VDISP_SEGMENT_TYPE = new Vector();
	
	public Vector getVAGMT_DISP_MAP() {return VAGMT_DISP_MAP;}
	public Vector getVAGMT_START_DT() {return VAGMT_START_DT;}
	public Vector getVAGMT_END_DT() {return VAGMT_END_DT;}
	public Vector getVEXPIRY_STATUS() {return VEXPIRY_STATUS;}
	
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
	public Vector getVTCQ() {return VTCQ;}
	public Vector getVTCQ_MMSCM() {return VTCQ_MMSCM;}
	public Vector getVSIGNING_DT() {return VSIGNING_DT;}
	public Vector getVSTART_DT() {return VSTART_DT;}
	public Vector getVEND_DT() {return VEND_DT;}
	public Vector getVRATE() {return VRATE;}
	public Vector getVRATE_UNIT() {return VRATE_UNIT;}
	public Vector getVDIS_CONT_MAPPING() {return VDIS_CONT_MAPPING;}
	public Vector getVCONT_STATUS() {return VCONT_STATUS;}
	public Vector getVCONT_STATUS_FLG() {return VCONT_STATUS_FLG;}
	public Vector getVCONT_REF() {return VCONT_REF;}
	public Vector getVENT_DT() {return VENT_DT;}
	public Vector getVENT_BY() {return VENT_BY;}
	public Vector getVAPRV_DT() {return VAPRV_DT;}
	public Vector getVAPRV_BY() {return VAPRV_BY;}
	public Vector getVSEGMENT() {return VSEGMENT;}
	public Vector getVSEGMENT_TYPE() {return VSEGMENT_TYPE;}
	public Vector getVTEMP_SEGMENT() {return VTEMP_SEGMENT;}
	public Vector getVTEMP_SEGMENT_TYPE() {return VTEMP_SEGMENT_TYPE;}
	public Vector getVINDEX() {return VINDEX;}
	public Vector getVPRICE_TYPE() {return VPRICE_TYPE;}
	public Vector getVDELV_POINT() {return VDELV_POINT;}
	public Vector getVBU_POINT() {return VBU_POINT;}
	public Vector getVIS_ALLOCATED() {return VIS_ALLOCATED;}
	
	public Vector getVSUPPLIED_QTY_MMBTU() {return VSUPPLIED_QTY_MMBTU;}
	public Vector getVSUPPLIED_QTY_MMSCM() {return VSUPPLIED_QTY_MMSCM;}
	public Vector getVBALANCE_QTY_MMBTU() {return VBALANCE_QTY_MMBTU;}
	public Vector getVBALANCE_QTY_MMSCM() {return VBALANCE_QTY_MMSCM;}
	public Vector getVALLOC_MIN_DT() {return VALLOC_MIN_DT;}
	public Vector getVALLOC_MAX_DT() {return VALLOC_MAX_DT;}
	
	public Vector getVAPPROVED_FLAG() {return VAPPROVED_FLAG;}
	public Vector getVAPPROVED_DATE() {return VAPPROVED_DATE;}
	public Vector getVAPPROVED_COUNTERPARTY_CD() {return VAPPROVED_COUNTERPARTY_CD;}
	public Vector getVAPPROVED_COUNTERPARTY_NM() {return VAPPROVED_COUNTERPARTY_NM;}
	public Vector getVCONTRACT_TYPE() {return VCONTRACT_TYPE;}
	public Vector getVBALANCEQTY() {return VBALANCEQTY;}
	public Vector getVTEMP_RATE_UNIT() {return VTEMP_RATE_UNIT;}
	public Vector getVSALES_AMT_INR() {return VSALES_AMT_INR;}
	public Vector getVSALES_AMT_USD() {return VSALES_AMT_USD;}
	public Vector getVAPPROVED_COLOR() {return VAPPROVED_COLOR;}
	public Vector getVSUB_INDEX() {return VSUB_INDEX;}
	public Vector getVTOTAL_TCQ() {return VTOTAL_TCQ;}
	public Vector getVTOTAL_SUPPLIED_QTY_MMBTU() {return VTOTAL_SUPPLIED_QTY_MMBTU;}
	public Vector getVTOTAL_BALANCE_QTY_MMBTU() {return VTOTAL_BALANCE_QTY_MMBTU;}
	public Vector getVTOTAL_SALES_AMT_INR() {return VTOTAL_SALES_AMT_INR;}
	public Vector getVTOTAL_SALES_AMT_USD() {return VTOTAL_SALES_AMT_USD;}
	public Vector getVFINAL_TCQ() {return VFINAL_TCQ;}
	public Vector getVVARIATION_TCQ() {return VVARIATION_TCQ;}
	public Vector getVDUE_DATE() {return VDUE_DATE;}
	public Vector getVEXCHANGE_RATE() {return VEXCHANGE_RATE;}
	public Vector getVEXCHNG_RATE_NM() {return VEXCHNG_RATE_NM;}
	
	public Vector getVTOTAL_MMBTU() {return VTOTAL_MMBTU;}
	public Vector getVTOTAL_SCM() {return VTOTAL_SCM;}
	public Vector getVTOTALSUPPLIED_MMBTU() {return VTOTALSUPPLIED_MMBTU;}
	public Vector getVTOTALSUPPLIED_SCM() {return VTOTALSUPPLIED_SCM;}
	public Vector getVTOTALBALANCE_MMBTU() {return VTOTALBALANCE_MMBTU;}
	public Vector getVTOTALBALANCE_SCM() {return VTOTALBALANCE_SCM;}
	public Vector getVPLANT_UNIT() {return VPLANT_UNIT;}
	
	public Vector getVBAL_INFO() {return VBAL_INFO;}
	public Vector getVBOOKED_QTY() {return VBOOKED_QTY;}
	public Vector getVCONT_REF_NO() {return VCONT_REF_NO;}
	public Vector getVMIN_ALLOC_DT() {return VMIN_ALLOC_DT;}
	public Vector getVMAX_ALLOC_DT() {return VMAX_ALLOC_DT;}
	public Vector getVUNLOADED_QTY() {return VUNLOADED_QTY;}
	public Vector getVTRADER_PLANT() {return VTRADER_PLANT;}
	public Vector getVBALANCE_QTY() {return VBALANCE_QTY;}
	public Vector getVBOOKED_INFO() {return VBOOKED_INFO;}
	public Vector getVTOTAL_QUANTITY() {return VTOTAL_QUANTITY;}
	public Vector getVTOTAL_UNLOADED_QUANTITY() {return VTOTAL_UNLOADED_QUANTITY;}
	public Vector getVHEADER_SEGMENT() {return VHEADER_SEGMENT;}
	public Vector getVSEGMENT_INDEX() {return VSEGMENT_INDEX;}
	
	public Vector getVDCQ() {return VDCQ;}
	public Vector getVTOTAL_DCQ() {return VTOTAL_DCQ;}
	
	public Vector getVDISP_SEGMENT() {return VDISP_SEGMENT;}
	public Vector getVDISP_SEGMENT_TYPE() {return VDISP_SEGMENT_TYPE;}
}
