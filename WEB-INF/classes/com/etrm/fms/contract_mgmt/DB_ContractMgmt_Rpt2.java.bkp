package com.etrm.fms.contract_mgmt;

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

public class DB_ContractMgmt_Rpt2
{
String db_src_file_name="DB_ContractMgmt_Rpt2.java";
	
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
	NumberFormat nf5 = new DecimalFormat("###########0");
	
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
	    			if(callFlag.equalsIgnoreCase("ALLOCATION_TO_CUSTOMER_PLANT"))
	    			{
	    				getCustomerMasterForAllocToCust();
	    				getContractList();
	    				getDealMapping();
	    				getPlantName();
	    				getBUList();
	    				getAllocToCustomerData();
	    			}
	    			else if(callFlag.equalsIgnoreCase("SEND_OUT_SUMMARY"))
					{
						getSendOutSummary();
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
			 String deal_map = "",cont_ref="",cargo_no="",base_agmt="";
			 queryString ="SELECT DISTINCT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO, "
					//+ "A.AGMT_REV,"
					+ "A.CONT_NO,A.CONTRACT_TYPE,A.CARGO_NO "
					+ "FROM FMS_DAILY_ALLOCATION_DTL A "
					+ "WHERE A.COMPANY_CD = ? AND A.GAS_DT >= TO_DATE(?, 'DD/MM/YYYY') "
					+ "AND A.GAS_DT <= TO_DATE(?, 'DD/MM/YYYY') AND A.COUNTERPARTY_CD = ? "
					+ "AND A.NOM_REV_NO = ( "
					+ "SELECT MAX(B.NOM_REV_NO) "
					+ "FROM FMS_DAILY_ALLOCATION_DTL B "
					+ "WHERE B.COMPANY_CD = A.COMPANY_CD "
					+ "AND B.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
					+ "AND B.AGMT_NO = A.AGMT_NO "
					//+ "AND B.AGMT_REV = A.AGMT_REV "
					+ "AND B.CONT_NO = A.CONT_NO AND B.PLANT_SEQ = A.PLANT_SEQ "
					+ "AND B.TRANSPORTER_CD = A.TRANSPORTER_CD AND B.TRANS_SEQ = A.TRANS_SEQ "
					+ "AND B.BU_SEQ = A.BU_SEQ AND B.GAS_DT = A.GAS_DT "
					+ "AND B.CONTRACT_TYPE = A.CONTRACT_TYPE AND B.CARGO_NO = A.CARGO_NO) ORDER BY A.CARGO_NO ";
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
						// agmt_rev = rset.getString(4) == null ? "" : rset.getString(4);
						 cont = rset.getString(4) == null ? "" : rset.getString(4);
						 cont_type = rset.getString(5) == null ? "" : rset.getString(5);
						 cargo_no = rset.getString(6) == null ? "" : rset.getString(6);
	
						VAGMT_NO.add(agmt);
						//VAGMT_REV.add(agmt_rev);
						VCONT_NO.add(cont);
						VCONTRACT_TYPE.add(cont_type);
						VCARGO_NO.add(cargo_no);
								 
						if(!cont_type.equals("Q") && (!cont_type.equals("O")))
						{
							String queryString1 = "SELECT A.CONT_REF_NO,A.CONT_REV,A.AGMT_BASE "
									+ "FROM FMS_SUPPLY_CONT_MST A "
									+ "WHERE A.COMPANY_CD = ? AND A.COUNTERPARTY_CD = ? "
									+ "AND A.AGMT_NO = ? "
									//+ "AND A.AGMT_REV = ? "
									+ "AND A.CONT_NO = ? AND A.CONTRACT_TYPE = ? "
									+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) "
									+ "FROM FMS_SUPPLY_CONT_MST B "
									+ "WHERE B.COMPANY_CD = A.COMPANY_CD "
									+ "AND B.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
									+ "AND B.AGMT_NO = A.AGMT_NO "
									+ "AND B.AGMT_REV = A.AGMT_REV "
									+ "AND B.CONT_NO  = A.CONT_NO "
									+ "AND B.CONTRACT_TYPE = A.CONTRACT_TYPE) ";
							stmt1 = conn.prepareStatement(queryString1);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, countpty_cd);
							stmt1.setString(3, agmt);
							//stmt1.setString(4, agmt_rev);
							stmt1.setString(4, cont);
							stmt1.setString(5, cont_type);
							rset1 = stmt1.executeQuery();
							if(rset1.next())
							{
								cont_ref = rset1.getString(1)==null?"":rset1.getString(1);
								//cont_rev = rset1.getString(2)==null?"":rset1.getString(2);
								base_agmt = rset1.getString(3) == null ? "" : rset1.getString(3);
								VCONT_REF.add(cont_ref);
								//VCONT_REV.add(cont_rev);
								VAGMT_BASE.add(base_agmt);
								VDIS_CONT_MAPPING.add(utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, "", cont, "", cont_type, cargo_no));
							}
							rset1.close();
							stmt1.close();
						}
						else
						{
							String queryString1 = "SELECT A.CARGO_REF,A.CONT_REV,NULL "
								+ "FROM FMS_LTCORA_CONT_CARGO_DTL A "
								+ "WHERE A.COMPANY_CD = ? AND A.COUNTERPARTY_CD = ? "
								+ "AND A.AGMT_NO = ? "
								//+ "AND A.AGMT_REV = ? "
								+ "AND A.CONT_NO = ? AND A.CONTRACT_TYPE = ? "
								+ "AND A.CARGO_NO = ? "
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
							//stmt1.setString(4, agmt_rev);
							stmt1.setString(4, cont);
							stmt1.setString(5, cont_type);
							stmt1.setString(6, cargo_no);
							rset1 = stmt1.executeQuery();
							if(rset1.next())
							{
								cont_ref = rset1.getString(1)==null?"":rset1.getString(1);
								//cont_rev = rset1.getString(2)==null?"":rset1.getString(2);
								base_agmt = rset1.getString(3) == null ? "" : rset1.getString(3);
								VCONT_REF.add(cont_ref);
								//VCONT_REV.add(cont_rev);
								VAGMT_BASE.add(base_agmt);
								VDIS_CONT_MAPPING.add(utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, "", cont, "", cont_type, cargo_no));
							}
							rset1.close();
							stmt1.close();
						}
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
				
				if(!cont_type.equals("Q") && (!cont_type.equals("O")))
				{
					String queryString1 = "SELECT A.CONT_REF_NO,A.AGMT_BASE "
							+ "FROM FMS_SUPPLY_CONT_MST A "
							+ "WHERE A.COMPANY_CD = ? AND A.COUNTERPARTY_CD = ? "
							+ "AND A.AGMT_NO = ? "
							//+ "AND A.AGMT_REV = ? "
							+ "AND A.CONT_NO = ? AND A.CONTRACT_TYPE = ? "
							+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) "
							+ "FROM FMS_SUPPLY_CONT_MST B "
							+ "WHERE B.COMPANY_CD = A.COMPANY_CD "
							+ "AND B.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
							+ "AND B.AGMT_NO = A.AGMT_NO "
							//+ "AND B.AGMT_REV = A.AGMT_REV "
							+ "AND B.CONT_NO  = A.CONT_NO "
							+ "AND B.CONTRACT_TYPE = A.CONTRACT_TYPE) ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, agmt_no);
					//stmt1.setString(4, agmt_rev);
					stmt1.setString(4, cont_no);
					stmt1.setString(5, cont_type);
					rset1 = stmt1.executeQuery();
					if(rset1.next())
					{
						cont_ref_no = rset1.getString(1)==null?"":rset1.getString(1);
						agmt_base = rset1.getString(2)==null?"":rset1.getString(2);
						deal_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, "", cont_no, "", cont_type, cargo_no);
					}
					rset1.close();
					stmt1.close();
				}
				else
				{
					String queryString1 = "SELECT A.CARGO_REF,NULL "
							+ "FROM FMS_LTCORA_CONT_CARGO_DTL A "
							+ "WHERE A.COMPANY_CD = ? AND A.COUNTERPARTY_CD = ? "
							+ "AND A.AGMT_NO = ? "
							//+ "AND A.AGMT_REV = ? "
							+ "AND A.CONT_NO = ? AND A.CONTRACT_TYPE = ? "
							+ "AND A.CARGO_NO = ? "
							+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) "
							+ "FROM FMS_LTCORA_CONT_MST B "
							+ "WHERE B.COMPANY_CD = A.COMPANY_CD "
							+ "AND B.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
							+ "AND B.AGMT_NO = A.AGMT_NO "
							//+ "AND B.AGMT_REV = A.AGMT_REV "
							+ "AND B.CONT_NO  = A.CONT_NO "
							+ "AND B.CONTRACT_TYPE = A.CONTRACT_TYPE) ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, agmt_no);
					//stmt1.setString(4, agmt_rev);
					stmt1.setString(4, cont_no);
					stmt1.setString(5, cont_type);
					stmt1.setString(6, cargo_no);
					rset1 = stmt1.executeQuery();
					if(rset1.next())
					{
						cont_ref_no = rset1.getString(1)==null?"":rset1.getString(1);
						agmt_base = rset1.getString(2)==null?"":rset1.getString(2);
						deal_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, "", cont_no, "", cont_type, cargo_no);
					}
					rset1.close();
					stmt1.close();
				}
				
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
			for(int i=0;i<VDIS_CONT_MAPPING.size();i++)
			{
			String queryString = "SELECT DISTINCT A.PLANT_SEQ " 
						+ "FROM FMS_DAILY_ALLOCATION_DTL A "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD  = ? " 
						+ "AND A.AGMT_NO = ? "
						//+ "AND A.AGMT_REV = ? "
						+ "AND A.CONT_NO = ? AND A.CONTRACT_TYPE = ? AND CARGO_NO = ? "
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
				if(deal_no.equals("0"))
				{
					stmt.setString(3, ""+VAGMT_NO.elementAt(i));
					//stmt.setString(4, ""+VAGMT_REV.elementAt(i));
					stmt.setString(4, ""+VCONT_NO.elementAt(i));
					stmt.setString(5, ""+VCONTRACT_TYPE.elementAt(i));
					stmt.setString(6, ""+VCARGO_NO.elementAt(i));
				}
				else
				{
					stmt.setString(3, agmt_no);
					//stmt.setString(4, agmt_rev);
					stmt.setString(4, cont_no);
					stmt.setString(5, cont_type);
					stmt.setString(6, cargo_no);
				}
				stmt.setString(7, from_dt);
				stmt.setString(8, to_dt);
				rset = stmt.executeQuery();
				while (rset.next()) 
				{
					String plant_seq = rset.getString(1) == null ? "" : rset.getString(1);
					String plant_name = utilBean.getCounterpartyPlantName(conn, counterparty_cd, comp_cd, plant_seq, "C");
					if(!VPLANT_SEQ_NO.contains(plant_seq))
					{
						VPLANT_SEQ_NO.add(plant_seq);
					}
					if(!VPLANT_NM.contains(plant_name))
					{
						VPLANT_NM.add(plant_name);
					}
					
				}
				rset.close();
				stmt.close();
				VCOUNTERPARTY_PLANT_SEQ.add(VPLANT_SEQ_NO);
				VCOUNTERPARTY_PLANT_NM.add(VPLANT_NM);
			}
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
			for(int i=0;i<VDIS_CONT_MAPPING.size();i++)
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
				queryString_bu +=" AND CONT_NO = ? AND CONTRACT_TYPE = ? ";
				queryString_bu +=" UNION "
						+ "SELECT BU_SEQ "
						+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
						+ "WHERE A.COMPANY_CD = ?  "
						+ "AND A.GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND A.GAS_DT<=TO_DATE(?,'DD/MM/YYYY')";
				if(!counterparty_cd.equals("0") && (!counterparty_cd.equals(""))) 
				{
				  queryString_bu +=" AND COUNTERPARTY_CD=? ";
				}
				queryString_bu +=" AND CONT_NO = ? AND CONTRACT_TYPE = ? ";
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
				else
				{
					stmt.setString(++count, ""+VCONT_NO.elementAt(i));
				    stmt.setString(++count, ""+VCONTRACT_TYPE.elementAt(i));
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
				else
				{
					stmt.setString(++count, ""+VCONT_NO.elementAt(i));
				    stmt.setString(++count, ""+VCONTRACT_TYPE.elementAt(i));
				}
				rset = stmt.executeQuery();
				while (rset.next()) 
				{
					String bu_cd = rset.getString(1) == null ? "" : rset.getString(1);
					String bu_abbr = utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_cd, "B");
					if(!VBU_PLANT_SEQ.contains(bu_cd))
					{
						VBU_PLANT_SEQ.add(bu_cd);
					}
					if(!VBU_PLANT_ABBR.contains(bu_abbr))
					{
						VBU_PLANT_ABBR.add(bu_abbr);
					}
				}
				rset.close();
				stmt.close();
		   }
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

		 String temp_deal="",cont_map1="";
		 temp_count = 0;
         if(!deal_no.equals(temp_deal) && !deal_no.equals("0") && !deal_no.equals(""))
         {
         	temp_deal = deal_no;
         	temp_count++;
         }
         else
         {
        	 temp_count = VDIS_CONT_MAPPING.size();
         }
		
		int index = 0;
		for (int i = 0; i < temp_count; i++) 
		{
			
			VCOUNTERPARTY_CD.addAll(TEMP_CUST_CD);
			VCOUNTERPARTY_ABBR.addAll(TEMP_CUST_ABBR);
			VCOUNTERPARTY_NM.addAll(TEMP_CUST_NM);
			index = 0;
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
				String cont_map = counterparty_cd + "-%-%-%-%-%";
				if(!deal_no.equals("0") && (!deal_no.equals("")))
				{
				 cont_map1 = counterparty_cd + "-"+ cont_type +"-"+ agmt_no +"-%" +"-"+ cont_no+"-%";
				}
				else
				{
					cont_map1 = counterparty_cd + "-"+ VCONTRACT_TYPE.elementAt(i) +"-"+ VAGMT_NO.elementAt(i) +"-%" +"-"+ VCONT_NO.elementAt(i)+"-%";
				}
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
//					queryString1+= " AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONTRACT_TYPE= ? ";		
					queryString1+= " AND AGMT_NO = ? AND CONT_NO = ? AND CONTRACT_TYPE= ? AND CARGO_NO = ? ";		
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
					stmt1.setString(++count, counterparty_cd);
					stmt1.setString(++count, gas_dt);
					if(!deal_no.equals("0") && (!deal_no.equals(""))) 
					{
					  stmt1.setString(++count, agmt_no);		
					  //stmt1.setString(++count, agmt_rev);		
					  stmt1.setString(++count, cont_no);		
					  stmt1.setString(++count, cont_type);
					  stmt1.setString(++count, cargo_no);
					}
					else
					{
					  stmt1.setString(++count, ""+VAGMT_NO.elementAt(i));		
					 // stmt1.setString(++count, ""+VAGMT_REV.elementAt(i));		
					  stmt1.setString(++count, ""+VCONT_NO.elementAt(i));		
					  stmt1.setString(++count, ""+VCONTRACT_TYPE.elementAt(i));
					  stmt1.setString(++count, ""+VCARGO_NO.elementAt(i));
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
							VQTY_MMBTU.add("0.00");
							VQTY_SCM.add("0.00");
							VCOLOR.add("");

						} 
						else 
						{
							VQTY_MMBTU.add(nf.format(mmbtu));
							VQTY_SCM.add(nf.format(scm));
							VCOLOR.add("#99ffcc");
						}
					}
					rset1.close();
					stmt1.close();
				
						for (int j = 0; j < ((Vector) VCOUNTERPARTY_PLANT_SEQ.elementAt(i)).size(); j++) 
						{
							String plantSeq = "" + ((Vector) VCOUNTERPARTY_PLANT_SEQ.elementAt(i)).elementAt(j);
							cont_map = counterparty_cd + "-%-%-%-%-%";
							if(!deal_no.equals("0") && (!deal_no.equals("")))
							{
							 cont_map1 = counterparty_cd + "-"+ cont_type +"-"+ agmt_no +"-%" +"-"+ cont_no+"-%";
							}
							else
							{
								cont_map1 = counterparty_cd + "-"+ VCONTRACT_TYPE.elementAt(i) +"-"+ VAGMT_NO.elementAt(i) +"-%" +"-"+ VCONT_NO.elementAt(i)+"-%";
							}
							String exit_point = "C-" + counterparty_cd + "-" + plantSeq;
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
							
//							queryString1+= " AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONTRACT_TYPE= ? ";		
							queryString1+= " AND AGMT_NO = ? AND CONT_NO = ? AND CONTRACT_TYPE= ? AND CARGO_NO = ? ";		
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
							stmt1.setString(++count1, counterparty_cd);
							stmt1.setString(++count1, gas_dt);
							stmt1.setString(++count1, plantSeq);
							
							if(!deal_no.equals("0") && (!deal_no.equals(""))) 
							{
							  stmt1.setString(++count1, agmt_no);		
							  //stmt1.setString(++count1, agmt_rev);		
							  stmt1.setString(++count1, cont_no);		
							  stmt1.setString(++count1, cont_type);
							  stmt1.setString(++count1, cargo_no);
							}
							else
							{
							  stmt1.setString(++count1, (String) VAGMT_NO.elementAt(i));		
							 // stmt1.setString(++count1, (String)VAGMT_REV.elementAt(i));		
							  stmt1.setString(++count1, (String)VCONT_NO.elementAt(i));		
							  stmt1.setString(++count1, (String)VCONTRACT_TYPE.elementAt(i));
							  stmt1.setString(++count1, (String)VCARGO_NO.elementAt(i));
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
									VQTY_MMBTU.add("0.00");
									VQTY_SCM.add("0.00");
									VCOLOR.add("");
								} 
								else 
								{
									VQTY_MMBTU.add(nf.format(mmbtu));
									VQTY_SCM.add(nf.format(scm));
									VCOLOR.add("#99ffcc");
								}
							}
							rset1.close();
							stmt1.close();
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
            cont_map1="";
			String cont_map = counterparty_cd + "-%-%-%-%-%";
			if(!deal_no.equals("0") && (!deal_no.equals("")))
			{
			 cont_map1 = counterparty_cd + "-"+ cont_type +"-"+ agmt_no +"-%" +"-"+ cont_no+"-%";
			}
			else
			{
				cont_map1 = counterparty_cd + "-"+ VCONTRACT_TYPE.elementAt(i) +"-"+ VAGMT_NO.elementAt(i) +"-%" +"-"+ VCONT_NO.elementAt(i)+"-%";
			}
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
//				queryString1+= " AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONTRACT_TYPE= ? ";		
				queryString1+= " AND AGMT_NO = ? AND CONT_NO = ? AND CONTRACT_TYPE= ? AND CARGO_NO = ? ";		
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
				stmt1.setString(++count, counterparty_cd);
				stmt1.setString(++count, from_dt);
				stmt1.setString(++count, to_dt);
				
				if(!deal_no.equals("0") && (!deal_no.equals(""))) 
				{
				  stmt1.setString(++count, agmt_no);		
				  //stmt1.setString(++count, agmt_rev);		
				  stmt1.setString(++count, cont_no);		
				  stmt1.setString(++count, cont_type);
				  stmt1.setString(++count, cargo_no);
				}
				else
				{
				  stmt1.setString(++count, (String)VAGMT_NO.elementAt(i));		
				  //stmt1.setString(++count, (String)VAGMT_REV.elementAt(i));		
				  stmt1.setString(++count, (String)VCONT_NO.elementAt(i));		
				  stmt1.setString(++count, (String)VCONTRACT_TYPE.elementAt(i));	
				  stmt1.setString(++count, (String)VCARGO_NO.elementAt(i));	
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
					for (int k = 0; k < ((Vector) VCOUNTERPARTY_PLANT_SEQ.elementAt(i)).size(); k++) 
					{
						String plantSeq = "" + ((Vector) VCOUNTERPARTY_PLANT_SEQ.elementAt(i)).elementAt(k);
						String exit_point = "C-" + counterparty_cd + "-" + plantSeq;
						cont_map = counterparty_cd + "-%-%-%-%-%";
						if(!deal_no.equals("0") && (!deal_no.equals("")))
						{
						 cont_map1 = counterparty_cd + "-"+ cont_type +"-"+ agmt_no +"-%" +"-"+ cont_no+"-%";
						}
						else
						{
							cont_map1 = counterparty_cd + "-"+ VCONTRACT_TYPE.elementAt(i) +"-"+ VAGMT_NO.elementAt(i) +"-%" +"-"+ VCONT_NO.elementAt(i)+"-%";
						}

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
						
//						queryString1+= " AND AGMT_NO = ? AND AGMT_REV = ? AND CONT_NO = ? AND CONTRACT_TYPE= ? ";		
						queryString1+= " AND AGMT_NO = ? AND CONT_NO = ? AND CONTRACT_TYPE= ? AND CARGO_NO = ? ";		
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
						stmt1.setString(++count1, counterparty_cd);
						stmt1.setString(++count1, from_dt);
						stmt1.setString(++count1, to_dt);
						stmt1.setString(++count1, plantSeq);
						
						if(!deal_no.equals("0") && (!deal_no.equals(""))) 
						{
						  stmt1.setString(++count1, agmt_no);		
						  //stmt1.setString(++count1, agmt_rev);		
						  stmt1.setString(++count1, cont_no);		
						  stmt1.setString(++count1, cont_type);
						  stmt1.setString(++count1, cargo_no);
						}
						else
						{
						  stmt1.setString(++count1, (String) VAGMT_NO.elementAt(i));		
						  //stmt1.setString(++count1, (String)VAGMT_REV.elementAt(i));		
						  stmt1.setString(++count1, (String)VCONT_NO.elementAt(i));		
						  stmt1.setString(++count1, (String)VCONTRACT_TYPE.elementAt(i));	
						  stmt1.setString(++count1, (String)VCARGO_NO.elementAt(i));	
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
//				  }
				}
			}
			catch (Exception e) 
			{
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			}
	}


public void getSendOutSummary() 
{
	String function_nm="getSendOutSummary()";
	try
	{  
		double conversion_factor_mcm = 38900;
		double net_uncommited_total_volume = 0;
		double net_uncommited_total_mcm_volume = 0;
		String first_day_month=from_dt;
//		String befor_15day_month="";
		String expected_volume_end_date = "";
		String previous_month_start_date = "",month="";
		String previous_month_end_date = "";
		String outstanding_commitment_LTCORA ="";
		String outstanding_commitment_LTCORA_mcm ="";
		String outstanding_commitment_sales ="";
		String outstanding_commitment_sales_mcm ="";
		String volume_expected_total="";
		String volume_expected_total_mcm="";
		
			if(from_dt.trim().length()==10)
			{
				String queryString = "SELECT TO_CHAR(LAST_DAY(TO_DATE(?,'DD/MM/YYYY')),'DD/MM/YYYY') FROM DUAL";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, first_day_month);
				rset = stmt.executeQuery();
				if(rset.next())
				{
					expected_volume_end_date = rset.getString(1);
				}
				stmt.close();
				rset.close();
			}
			
			if(from_dt.trim().length()==10)
			{
				String queryString = "SELECT TO_CHAR(TO_DATE(TO_CHAR(TO_DATE(?,'DD/MM/YYYY')-1,'MM/YYYY'),'MM/YYYY'),'DD/MM/YYYY') FROM DUAL";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, first_day_month);
				rset = stmt.executeQuery();
				if(rset.next())
				{
					previous_month_start_date = rset.getString(1);
//					befor_15day_month = rset.getString(1);
					
					queryString = "SELECT TO_CHAR(LAST_DAY(TO_DATE(?,'DD/MM/YYYY')),'DD/MM/YYYY') FROM DUAL";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, previous_month_start_date);
					rset = stmt.executeQuery();
					if(rset.next())
					{
						previous_month_end_date = rset.getString(1);
					}
					stmt.close();
					rset.close();
				}
				stmt.close();
				rset.close();
			}
			if(from_dt.trim().length()==10)
			{
				String queryString = "SELECT TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'DDTH MONTH-YY') FROM DUAL";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, first_day_month);
				rset = stmt.executeQuery();
				if(rset.next())
				{
					selected_from_date = rset.getString(1);
				}
				stmt.close();
				rset.close();
			}
			int mnth = Integer.parseInt(from_dt.substring(3,5));
			if(mnth==1)
			{
				mnth = 12;
				month = "12";
				year = ""+(Integer.parseInt(from_dt.substring(6))-1);
			}
			else
			{
				year = from_dt.substring(6);
				mnth = mnth-1;
				if(mnth<10)
				{
					month = "0"+mnth;
				}
				else
				{
					month = ""+mnth;
				}
			}
//		}
		
        String previous_month_start_date1 = "01/"+month+"/"+year;
		
		double vol_exp_sales = 0;
		double vol_exp_regas = 0;
		double vol_exp_total = 0;
		
		double vol_exp_LTCORA = 0; 
		double vol_recv_LTCORA = 0;
		double opening_stock_LTCORA = 0;
		double int_consumption_LTCORA = 0;
		
		
		double vol_recv_sales = 0;
		double vol_recv_regas = 0;
		double vol_recv_total = 0;
		
		double opening_stock_sales = 0;
		double opening_stock = 0;
		
		double qty_to_be_supplied = 0;
		
		double int_consumption_sales = 0;
		double int_consumption_total = 0;
		consumption_percentage = 2.0;
		
		double dead_stk = 700200;
		
		double month_total = 0;
		double mon_sales = 0;
		double aq_qty=0;
		double eq_qty=0;
		double sgu=0;
		double qty_to_supply1=0;
		double T1Dead_Inventory_mmscm=0;   
		double T1Dead_Inventory_mmbtu=0;
		
	    //SN AND LOA
		String queryString= "SELECT C.COUNTERPARTY_CD "
		+ "FROM FMS_COUNTERPARTY_MST C "
		+ "WHERE C.EFF_DT = ( "
		+ "SELECT MAX(D.EFF_DT) "
		+ "FROM FMS_COUNTERPARTY_MST D "
		+ "WHERE D.COUNTERPARTY_CD = C.COUNTERPARTY_CD) "
		+ "AND C.COUNTERPARTY_CD IN ( "
		+ "SELECT A.COUNTERPARTY_CD "
		+ "FROM FMS_SUPPLY_CONT_MST A "
		+ "WHERE A.START_DT <= TO_DATE(?,'DD/MM/YYYY') "
		+ "AND A.END_DT   >= TO_DATE(?,'DD/MM/YYYY') "
		+ "AND A.COMPANY_CD = ? "
		+ "AND A.CONTRACT_TYPE IN ('S','L','X') "
		+ "AND A.CONT_REV = ( "
		+ "SELECT MAX(B.CONT_REV) "
		+ "FROM FMS_SUPPLY_CONT_MST B "
		+ "WHERE A.COMPANY_CD = B.COMPANY_CD "
		+ "AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD "
		+ "AND A.AGMT_NO = B.AGMT_NO "
		+ "AND A.AGMT_REV = B.AGMT_REV "
		+ "AND A.CONT_NO = B.CONT_NO "
		+ "AND A.CONTRACT_TYPE = B.CONTRACT_TYPE)) "
		+ "ORDER BY C.COUNTERPARTY_ABBR ASC ";

		    stmt = conn.prepareStatement(queryString);
	    	stmt.setString(1, previous_month_end_date);
	    	stmt.setString(2, previous_month_start_date);
	    	stmt.setString(3, comp_cd);
		    rset = stmt.executeQuery();
		    while(rset.next())
		    {
		    	VCOUNTERPARTY_CD.add(rset.getString(1)==null?"":rset.getString(1));
		    }
		    stmt.close();
		    rset.close();
		    
	        double outstanding_commit_sales = 0;
		    for(int i=0;i<VCOUNTERPARTY_CD.size();i++)
		    {
		    	Vector tmp_CONT_NO = new Vector();
				Vector tmp_CONT_REF = new Vector();
				Vector tmp_AGMT_NO = new Vector();
				Vector tmp_TCQ = new Vector();
				Vector tmp_INNER_CUSTOMER_CD_SN_LOA = new Vector();
				Vector tmp_CONT_TYPE = new Vector();
				Vector tmp_QTY_MMBTU = new Vector();
				Vector tmp_QTY_BALANCE = new Vector();
				Vector tmp_REMARK = new Vector();
				String tmp_closure_dt = "";
				String tmp_cont_end_dt="";
				
				String cust_abbr = "" + utilBean.getCounterpartyABBR(conn,""+VCOUNTERPARTY_CD.elementAt(i));
				String cust_nm = "" + utilBean.getCounterpartyName(conn,""+VCOUNTERPARTY_CD.elementAt(i));
				VCOUNTERPARTY_ABBR.add(cust_abbr);
				VCOUNTERPARTY_NM.add(cust_nm);
		    	
		    	int cnt=0;
				double total_balance = 0;
		    	String cont_no="",agmt_no="",cont_type="";
		    	queryString ="SELECT A.COUNTERPARTY_CD,A.AGMT_NO,A.CONT_NO,A.AGMT_REV,A.CONT_REF_NO,TO_CHAR(A.CLOSE_EFF_DT,'DD/MM/YYYY'),"//6
		    			+ "NVL(A.CLOSURE_REQUEST_FLAG,'N'),A.TCQ,TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.CONTRACT_TYPE,A.AGMT_BASE "
		    			+ "FROM FMS_SUPPLY_CONT_MST A "
                        + "WHERE A.START_DT <= TO_DATE(?,'DD/MM/YYYY') "
                        + "AND A.END_DT >= TO_DATE(?,'DD/MM/YYYY') "
			    		+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) "
			    		+ "FROM FMS_SUPPLY_CONT_MST B "
			    		+ "WHERE A.COMPANY_CD = B.COMPANY_CD AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD "
			    		+ "AND A.AGMT_NO = B.AGMT_NO AND A.AGMT_REV = B.AGMT_REV "
			    		+ "AND A.CONT_NO = B.CONT_NO AND A.CONTRACT_TYPE = B.CONTRACT_TYPE "
			    		+ "AND A.CONTRACT_TYPE IN('S','L','X')) AND A.COUNTERPARTY_CD = ? AND A.COMPANY_CD = ? ";
		    	stmt = conn.prepareStatement(queryString);
		    	stmt.setString(1, previous_month_end_date);
		    	stmt.setString(2, previous_month_start_date);
		    	stmt.setString(3, ""+VCOUNTERPARTY_CD.elementAt(i));
		    	stmt.setString(4, comp_cd);
		    	rset = stmt.executeQuery();
		    	while(rset.next())
		    	{
		    		++cnt;
		    		cont_no = rset.getString(3)==null?"":rset.getString(3);
		    		agmt_no = rset.getString(2)==null?"":rset.getString(2);
		    		cont_type = rset.getString(10)==null?"":rset.getString(10);
		    		tmp_closure_dt = rset.getString(6)==null?"0":rset.getString(6);
		    		tmp_cont_end_dt = rset.getString(9)==null?"":rset.getString(9);
		    		
		    		tmp_INNER_CUSTOMER_CD_SN_LOA.add(rset.getString(1)==null?"":rset.getString(1));
		    		tmp_AGMT_NO.add(rset.getString(2)==null?"":rset.getString(2));
		    		tmp_CONT_NO.add(rset.getString(3)==null?"":rset.getString(3));
		    		tmp_CONT_REF.add(rset.getString(5)==null?"":rset.getString(5));
		    		tmp_TCQ.add(rset.getString(8)==null?"":rset.getString(8));
		    		tmp_CONT_TYPE.add(rset.getString(10)==null?"":rset.getString(10));
		    		VAGMT_BASE.add(rset.getString(11)==null?"":rset.getString(11));
		    		
		    		double balance=0;
//		    		String queryString2 = "SELECT SUM(QTY_MMBTU) "
//		    				+ "FROM FMS_DAILY_ALLOCATION_DTL "
//		    				+ "WHERE COUNTERPARTY_CD = ? AND "
//		    				+ "CONT_NO = ? AND AGMT_NO = ? AND CONTRACT_TYPE = ? "
//		    				+ "AND GAS_DT<TO_DATE(?,'DD/MM/YYYY') AND COMPANY_CD = ? ";
		    		String cont_map=""+VCOUNTERPARTY_CD.elementAt(i)+"-"+cont_type+"-"+agmt_no+"-%-"+cont_no+"-%";
		    		VREF_DTL.add(cont_map);
		    		
		    		String queryString2 = "SELECT SUM(ALLOC_QTY) "
		    				+ "FROM ( SELECT SUM(EXIT_QTY_MMBTU) AS ALLOC_QTY "
		    				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
		    				+ "WHERE COMPANY_CD = ? AND CONTRACT_TYPE = ? AND GAS_DT < TO_DATE(?, 'DD/MM/YYYY') AND SELL_CONT_MAP LIKE ? "
		    				+ "AND ALLOC_REV_NO = (SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B WHERE B.CONT_NO = A.CONT_NO AND B.AGMT_NO = A.AGMT_NO "
		    				+ "AND B.COMPANY_CD = A.COMPANY_CD AND B.COUNTERPARTY_CD = A.COUNTERPARTY_CD AND B.CONTRACT_TYPE = A.CONTRACT_TYPE "
		    				+ "AND B.SELL_CONT_MAP = A.SELL_CONT_MAP AND A.BU_SEQ = B.BU_SEQ AND B.GAS_DT = A.GAS_DT AND A.ENTRY_PT_MAPPING_ID = B.ENTRY_PT_MAPPING_ID "
		    				+ "AND A.EXIT_PT_MAPPING_ID = B.EXIT_PT_MAPPING_ID ) "
		    				+ "UNION "
		    				+ "SELECT SUM(QTY_MMBTU) AS ALLOC_QTY "
		    				+ "FROM FMS_DAILY_ALLOCATION_DTL A "
		    				+ "WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND GAS_DT < TO_DATE(?, 'DD/MM/YYYY') AND AGMT_NO = ? AND CONT_NO = ? AND CONTRACT_TYPE = ? "
		    				+ "AND NOM_REV_NO = ( SELECT MAX(NOM_REV_NO) "
			    			+ "FROM FMS_DAILY_ALLOCATION_DTL B "
			    			+ "WHERE B.CONT_NO = A.CONT_NO AND B.AGMT_NO = A.AGMT_NO AND B.COMPANY_CD = A.COMPANY_CD "
			    			+ "AND B.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
			    			+ "AND B.TRANSPORTER_CD = A.TRANSPORTER_CD AND B.TRANS_SEQ = A.TRANS_SEQ AND B.PLANT_SEQ = A.PLANT_SEQ AND B.CONTRACT_TYPE = A.CONTRACT_TYPE AND B.GAS_DT = A.GAS_DT "
			    			+ "AND B.CARGO_NO = A.CARGO_NO) "
		    				+ "AND (A.COUNTERPARTY_CD || '-' || A.CONTRACT_TYPE || '-' || A.AGMT_NO || '-' || A.AGMT_REV || '-' || A.CONT_NO || '-' || A.CONT_REV) "
		    				+ "NOT IN ( SELECT B.SELL_CONT_MAP "
		    				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC B "
		    				+ "WHERE A.COMPANY_CD = B.COMPANY_CD AND B.SELL_CONT_MAP LIKE ? AND A.GAS_DT = B.GAS_DT)) ";
		    				
		    		
		    		stmt2 = conn.prepareStatement(queryString2);
		    		stmt2.setString(1,comp_cd);
		    		stmt2.setString(2,"C");
		    		stmt2.setString(3,from_dt);
		    		stmt2.setString(4,cont_map);
		    		stmt2.setString(5,comp_cd);
		    		stmt2.setString(6,""+VCOUNTERPARTY_CD.elementAt(i));
		    		stmt2.setString(7,from_dt);
		    		stmt2.setString(8,agmt_no);
		    		stmt2.setString(9,cont_no);
		    		stmt2.setString(10,cont_type);
		    		stmt2.setString(11,cont_map);
//		    		stmt2.setString(2,cont_no);
//		    		stmt2.setString(3,agmt_no);
//		    		stmt2.setString(4,cont_type);
//		    		stmt2.setString(5,from_dt);
//		    		stmt2.setString(6, comp_cd);
		    		rset2 = stmt2.executeQuery();
		    		if(rset2.next())
		    		{
		    			tmp_QTY_MMBTU.add((rset2.getString(1) == null ? "0" :nf5.format(Double.parseDouble(rset2.getString(1)))));
		    				balance =	(Double.parseDouble(rset.getString(8)==null?"0":nf5.format(Double.parseDouble(rset.getString(8)))) 
		    		    			-  Double.parseDouble(rset2.getString(1)==null?"0":nf5.format(Double.parseDouble(rset2.getString(1)))));
		    			String tmp ="";
		    			if(!tmp_closure_dt.equals("0"))
		    			{
		    				String queryString3 = "SELECT COUNT(*)  FROM DUAL "
		    						+ "WHERE TO_DATE(?,'DD/MM/YYYY') <= TO_DATE(?,'DD/MM/YYYY')";
		    				stmt3 = conn.prepareStatement(queryString3);
		    				stmt3.setString(1,tmp_closure_dt);
		    				stmt3.setString(2,from_dt);
				    		rset3 = stmt3.executeQuery();
				    		if(rset3.next())
				    		{
				    			tmp = rset3.getString(1)==null?"0":rset3.getString(1);
				    		}
				    		stmt3.close();
				    		rset3.close();
		    			}
		    			//CHECK FOR CLOSURE FLAG AND QUANTITY
		    			if(rset.getString(7).trim().equalsIgnoreCase("A") && balance>0 && tmp.trim().equals("1"))
						{
		    				tmp_QTY_BALANCE.add("0");
		    				tmp_REMARK.add("Short closed by "+nf5.format(balance)+" Mmbtu");
							balance = 0;
						}
		    			else if(rset.getString(7).trim().equalsIgnoreCase("A") && balance<0 && tmp.trim().equals("1"))
						{
		    				tmp_QTY_BALANCE.add("0");
		    				tmp_REMARK.add("Excess supplied by "+nf5.format(balance*(-1))+" Mmbtu");
							balance = 0;
						}
		    			else if(rset.getString(7).trim().equalsIgnoreCase("A") && Double.doubleToRawLongBits(balance)==Double.doubleToRawLongBits(0))
						{
		    				tmp_QTY_BALANCE.add("0");
		    				tmp_REMARK.add("Closed");
							balance = 0;
						}
		    			else
		    			{
		    				String tempdt[] = from_dt.split("/");
		    				String date1=tempdt[2]+tempdt[1]+tempdt[0];
		    				
		    				String tempdt1[] = tmp_cont_end_dt.split("/");
		    				String date2=tempdt1[2]+tempdt1[1]+tempdt1[0];
		    				if(Integer.parseInt(date2)>(Integer.parseInt(date1)))
		    				{
		    					if (Double.doubleToRawLongBits(balance)==Double.doubleToRawLongBits(0))
								{
		    						tmp_QTY_BALANCE.add("0");
		    						tmp_REMARK.add("Closed");
									balance = 0;
								}else
								{
									tmp_QTY_BALANCE.add(nf5.format(balance));
									tmp_REMARK.add(" ");
								}
		    				}
		    				else
		    				{
		    					
								if (balance<0)
								{
									tmp_QTY_BALANCE.add("0");
									tmp_REMARK.add("Excess supplied by "+nf5.format(balance*(-1))+" Mmbtu");
									balance = 0;
									
								}
								else if (Double.doubleToRawLongBits(balance)==Double.doubleToRawLongBits(0))
								{
									tmp_QTY_BALANCE.add("0");
									tmp_REMARK.add("Closed");
									balance = 0;
								}
								else
								{
									if(cont_type.equals("S"))
									{
									  tmp_QTY_BALANCE.add("-");
									  tmp_REMARK.add("Short closed by "+nf5.format(balance)+" Mmbtu");
									  balance = 0;
									}
									else
									{
									   tmp_QTY_BALANCE.add(nf5.format(balance));
									   tmp_REMARK.add("Short closed by "+nf5.format(balance)+" Mmbtu");
									   balance = 0;
									}
								}
		    				}
		    			}
		    		}
		    		else
		    		{
		    			tmp_QTY_MMBTU.add("");
		    			balance = Double.parseDouble(rset.getString(8)==null?"0":nf5.format(Double.parseDouble(rset.getString(8))));
		    			tmp_QTY_BALANCE.add(Double.parseDouble(rset.getString(8)==null?"0":nf5.format(Double.parseDouble(rset.getString(8)))));
		    			tmp_REMARK.add("");
		    		}
		    		stmt2.close();
					rset2.close();
		    		total_balance += balance;
		    		outstanding_commit_sales +=balance;
		    		
		    	}
		    	stmt.close();
				rset.close();
				
				if(cnt==0)
				{
					tmp_CONT_NO.add("");
					tmp_CONT_REF.add("");
					tmp_AGMT_NO.add("");
					tmp_TCQ.add("");
					tmp_INNER_CUSTOMER_CD_SN_LOA.add(""+VCOUNTERPARTY_CD.elementAt(i));
					tmp_CONT_TYPE.add("");
					tmp_QTY_MMBTU.add("");
					tmp_QTY_BALANCE.add("");
					tmp_REMARK.add("");
				}
//				outstanding_commit_sales +=total_balance;
				VCONT_NO.add(tmp_CONT_NO);
				VCONT_REF.add(tmp_CONT_REF);
				VAGMT_NO.add(tmp_AGMT_NO);
				VTCQ.add(tmp_TCQ);
				VINNER_CUSTOMER_CD_SN_LOA.add(tmp_INNER_CUSTOMER_CD_SN_LOA);
				VCONTRACT_TYPE.add(tmp_CONT_TYPE);
				VQTY_MMBTU.add(tmp_QTY_MMBTU);
				VBALANCE.add(tmp_QTY_BALANCE);
				VSN_LOA_OUTER_COUNTER.add(""+cnt);
				VREMARK.add(tmp_REMARK);
				VSN_LOA_TOTAL_BALANCE.add(nf5.format(total_balance));
				
		    }
	    
		    ////LTCORA AND CN
		    double outstanding_commit_LTCORA = 0;
		    
		    queryString ="SELECT DISTINCT COUNTERPARTY_CD "
		    + "FROM FMS_LTCORA_CONT_CARGO_DTL "
		    + "WHERE COMPANY_CD = ? AND BUY_SALE = 'C' "
		    + "AND EDQ_FROM_DT <= TO_DATE(?,'DD/MM/YYYY') "
		    + "AND (ACTUAL_RECPT_DT + NVL(STORAGE_DAYS, 0) - 1 + NVL(STORAGE_EXT_DAYS, 0)) >= TO_DATE(?,'DD/MM/YYYY') "
		    + "ORDER BY COUNTERPARTY_CD ";
		    
		    stmt = conn.prepareStatement(queryString);
		    stmt.setString(1, comp_cd);
		    stmt.setString(2, previous_month_end_date);
	    	stmt.setString(3, previous_month_start_date);
	    	
		    rset = stmt.executeQuery();
		    while(rset.next())
		    {
		    	VCUSTOMER_CD_LTCORA.add(rset.getString(1)==null?"":rset.getString(1));
		    }
		    stmt.close();
		    rset.close();
		    
		    
		    double total_sug = 0,sug = 0,total_edq_qty = 0,total_supplied = 0;
		    double total_qty_to_supply = 0,total_balance=0,balance=0,total_qty_to_supply1=0;
		    double final_total_balance=0,edq_qty1 = 0;
		    for(int i=0;i<VCUSTOMER_CD_LTCORA.size();i++)
			{
		    	
				Vector tmp_QTY_MMBTU = new Vector();
				Vector tmp_REMARK_RE_GAS = new Vector();
				String closure_dt = "";
				
				String queryString1 = "SELECT COUNTERPARTY_ABBR "
		    			+ "FROM FMS_COUNTERPARTY_MST "
		    			+ "WHERE COUNTERPARTY_CD = ? ";
		    	stmt1 = conn.prepareStatement(queryString1);
		    	stmt1.setString(1, ""+VCUSTOMER_CD_LTCORA.elementAt(i));
		    	rset1 = stmt1.executeQuery();
		    	if(rset1.next())
		    	{
		    		VCUSTOMER_NM_LTCORA.add(rset1.getString(1)==null?"":rset1.getString(1));
		    	}
		    	else
		    	{
		    		VCUSTOMER_NM_LTCORA.add("");
		    	}
		    	stmt1.close();
				rset1.close();
				int count = 0; int flag_count = 0;
				double adq_qty=0,edq_qty=0, qty_to_supply = 0, supplied = 0;
				double total_adq_qty = 0;
				String cont_no="",cargo_no="",cont_start_dt="",cont_end_dt="",adq_dt="",cont_type="";
				String buy_flg="",cont_name="",agmt_no="",agmt_type="";

				queryString = "SELECT A.CONT_NO,A.CARGO_NO,NVL(A.EDQ_QTY,'0'),TO_CHAR(A.ACTUAL_RECPT_DT,'DD/MM/YYYY'), "//4
						+ "A.COUNTERPARTY_CD,A.AGMT_NO,A.CONTRACT_TYPE,NVL(B.SUG,'1'),A.BUY_SALE, "//9
						+ "TO_CHAR(A.EDQ_FROM_DT,'DD/MM/YYYY'),TO_CHAR((A.ACTUAL_RECPT_DT + NVL(A.STORAGE_DAYS, 0) - 1 + NVL(A.STORAGE_EXT_DAYS, 0)),'DD/MM/YYYY'),A.AGMT_TYPE "
						+ "FROM FMS_LTCORA_CONT_CARGO_DTL A,FMS_LTCORA_CONT_MST B "
						+ "WHERE A.COMPANY_CD = ? AND A.BUY_SALE = 'C' "
                        + "AND A.EDQ_FROM_DT <= TO_DATE(?,'DD/MM/YYYY') "
                        + "AND (A.ACTUAL_RECPT_DT + NVL(A.STORAGE_DAYS, 0) - 1 + NVL(A.STORAGE_EXT_DAYS, 0)) >= TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.COUNTERPARTY_CD = ? AND B.COMPANY_CD = A.COMPANY_CD AND B.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
						+ "AND A.AGMT_TYPE = B.AGMT_TYPE AND A.AGMT_NO = B.AGMT_NO AND A.CONTRACT_TYPE = B.CONTRACT_TYPE "
						+ "AND A.CONT_NO = B.CONT_NO AND A.BUY_SALE = B.BUY_SALE ORDER BY A.COUNTERPARTY_CD ";

				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
		    	stmt.setString(2, previous_month_end_date);
		    	stmt.setString(3, previous_month_start_date);
		    	stmt.setString(4, ""+VCUSTOMER_CD_LTCORA.elementAt(i));
		    	
		    	rset = stmt.executeQuery();
		    	while(rset.next())
		    	{
		    		cont_no = rset.getString(1)==null?"":rset.getString(1);
		    		cargo_no = rset.getString(2)==null?"":rset.getString(2);
		    		edq_qty = rset.getDouble(3);
		    		adq_dt = rset.getString(4)==null?"":rset.getString(4);
		    		agmt_no = rset.getString(6)==null?"":rset.getString(6);
		    		cont_type = rset.getString(7)==null?"":rset.getString(7);
		    		buy_flg = rset.getString(9)==null?"T":rset.getString(9);
		    		cont_start_dt = rset.getString(10)==null?"":rset.getString(10);
					cont_end_dt = rset.getString(11)==null?"":rset.getString(11);
		    		agmt_type = rset.getString(12)==null?"":rset.getString(12);
		    		 
		    		
		    		String ref_dtl=""+VCUSTOMER_CD_LTCORA.elementAt(i)+"-"+cont_type+"-"+agmt_no+"-%-"+cont_no+"-%";
		    		VREF_DTL.add(ref_dtl);
		    		
		    		 queryString1="SELECT ADQ_QTY "
		    		 		+ "FROM FMS_LTCORA_CONT_CARGO_ADQ "
		    		 		+ "WHERE CONT_NO = ? AND CARGO_NO = ?  "
		    		 		+ "AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE = ? AND COMPANY_CD = ? "
		    		 		+ "AND AGMT_TYPE = ? AND AGMT_NO = ? AND BUY_SALE = 'C' "
		    		 		+ "AND ADQ_QTY IS NOT NULL AND ADQ_QTY <> 0 ";
		    		 stmt1 = conn.prepareStatement(queryString1);
		    		 stmt1.setString(1, cont_no);
		    		 stmt1.setString(2, cargo_no);
		    		 stmt1.setString(3, ""+VCUSTOMER_CD_LTCORA.elementAt(i));
		    		 stmt1.setString(4, cont_type);
		    		 stmt1.setString(5, comp_cd);
		    		 stmt1.setString(6, agmt_type);
		    		 stmt1.setString(7, agmt_no);
		    		 rset1 = stmt1.executeQuery();
		    		 while(rset1.next())
		    		 {
		    			 adq_qty = rset1.getDouble(1);
		    		 }
		    		 stmt1.close();
		    		 rset1.close();
			    		
//							String start_dt = cont_start_dt.substring(3);
//							String end_dt = cont_end_dt.substring(3);
//							if(start_dt.equals(end_dt) && start_dt.equals(previous_month_start_date.substring(3))) 
//							{
//								continue;
//							}
//							else 
//							{
								
//								if(buy_flg.equalsIgnoreCase("T"))
//								{
//									tmp_RE_GAS_NO.add(cont_no);
//								}
//								else if(buy_flg.equalsIgnoreCase("C"))
//								{
//									tmp_RE_GAS_NO.add(agmt_no);
//								}
								
								
								queryString1="SELECT NVL(SUG,'1') "
					    		 		+ "FROM FMS_LTCORA_CONT_CARGO_MOD "
					    		 		+ "WHERE CONT_NO = ? AND CARGO_NO = ? "
					    		 		+ "AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE = ? AND COMPANY_CD = ? "
					    		 		+ "AND AGMT_TYPE = ? AND AGMT_NO = ? AND BUY_SALE = 'C' ";
								 stmt1 = conn.prepareStatement(queryString1);
					    		 stmt1.setString(1, cont_no);
					    		 stmt1.setString(2, cargo_no);
					    		 stmt1.setString(3, ""+VCUSTOMER_CD_LTCORA.elementAt(i));
					    		 stmt1.setString(4, cont_type);
					    		 stmt1.setString(5, comp_cd);
					    		 stmt1.setString(6, agmt_type);
					    		 stmt1.setString(7, agmt_no);
					    		 rset1 = stmt1.executeQuery();
					    		 if(rset1.next())
					    		 {
					    			 sug = rset1.getDouble(1); 
					    		 }
					    		 else
					    		 {
					    			 sug = rset.getDouble(8);
					    		 }
					    		 stmt1.close();
					    		 rset1.close();
								
								if(adq_qty>0)
					    		{
									total_qty_to_supply1 = adq_qty-((adq_qty*sug)/100);
					    		}
								else
								{
									total_qty_to_supply1 = edq_qty-((edq_qty*sug)/100);
								}
								total_qty_to_supply +=qty_to_supply;
								
								if(Double.doubleToRawLongBits(adq_qty)==Double.doubleToRawLongBits(0))
								{
									sug = (edq_qty * sug) / 100;
								} 
								else 
								{
									sug = (adq_qty * sug) / 100;
								}
								
//								if(!adq_dt.equals("") && (adq_dt.substring(3).equals(previous_month_start_date.substring(3)))) 
//								{
									total_adq_qty += adq_qty;
									total_sug += sug;
//									total_edq_qty += edq_qty;
									 
//								}
									
								queryString1 = "SELECT SUM(A.QTY_MMBTU) "
										+ "FROM FMS_DAILY_ALLOCATION_DTL A "
										+ "WHERE A.CONT_NO = ? AND A.COUNTERPARTY_CD = ? AND A.CONTRACT_TYPE = ? "
										+ "AND A.GAS_DT <TO_DATE(?,'DD/MM/YYYY') AND A.COMPANY_CD = ? AND A.CARGO_NO = ? "
										+ "AND A.AGMT_NO = ? AND A.NOM_REV_NO = (SELECT MAX(B.NOM_REV_NO) "
										+ "FROM FMS_DAILY_ALLOCATION_DTL B "
										+ "WHERE A.CONT_NO = B.CONT_NO AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD "
										+ "AND A.CONTRACT_TYPE = B.CONTRACT_TYPE AND A.GAS_DT = B.GAS_DT "
										+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.CARGO_NO = B.CARGO_NO "
										+ "AND A.AGMT_NO = B.AGMT_NO )";
								stmt2 = conn.prepareStatement(queryString1);
								stmt2.setString(1, cont_no);
								stmt2.setString(2, ""+VCUSTOMER_CD_LTCORA.elementAt(i));
								stmt2.setString(3, cont_type);
								stmt2.setString(4, from_dt);
								stmt2.setString(5, comp_cd);
								stmt2.setString(6, cargo_no);
								stmt2.setString(7, agmt_no);
								rset2 = stmt2.executeQuery();
								if(rset2.next())
								{
//									if(!adq_dt.equals("") && (adq_dt.substring(3).equals(previous_month_start_date.substring(3)))) 
//									{
										supplied = rset2.getDouble(1);
										total_supplied += supplied;
//									}
									balance = (total_qty_to_supply1-Double.parseDouble(rset2.getString(1)==null?"0":nf.format(Double.parseDouble(rset2.getString(1)))));
								}
								else
								{
									balance = (total_qty_to_supply1-Double.parseDouble(rset2.getString(1)==null?"0":nf.format(Double.parseDouble(rset2.getString(1)))));
								}
								stmt2.close();
								rset2.close();
								total_balance += balance;
								++count;
//							}
		    	}
		    	stmt.close();
			    rset.close(); 
			   
			    edq_qty1 +=total_adq_qty;
				outstanding_commit_LTCORA = Double.parseDouble(nf.format(total_balance));
				total_edq_qty = edq_qty1 + total_qty_to_supply - total_sug;
				total_balance = total_edq_qty - total_supplied;
				final_total_balance = total_balance;
				total_sug_lt = nf5.format(total_sug);
				total_sug_lt_mcm = nf.format(total_sug/conversion_factor_mcm);
				
				net_commit_lt_vol = nf5.format(total_edq_qty);
				net_commit_lt_vol_mcm = nf.format(total_edq_qty/conversion_factor_mcm);
				
				total_expect_vol = nf.format(total_qty_to_supply);
				total_expect_vol_mcm = nf.format(total_qty_to_supply/conversion_factor_mcm);
				
				total_vol_supplied = nf5.format(total_supplied);
				total_vol_supplied_mcm = nf.format(total_supplied/conversion_factor_mcm);
				
				total_outstanding_commitment = nf5.format(final_total_balance);
				total_outstanding_commitment_mcm = nf.format(final_total_balance/conversion_factor_mcm);
				
				LTCORA_TOTAL_UNLOADED_VOLUME.add(Double.doubleToRawLongBits(total_adq_qty)==Double.doubleToRawLongBits(0)?"-":nf5.format(total_adq_qty));
				LTCORA_TOTAL_UNLOADED_VOLUME_MCM.add(Double.doubleToRawLongBits(total_adq_qty/conversion_factor_mcm)==Double.doubleToRawLongBits(0)?"-":nf.format(total_adq_qty/conversion_factor_mcm));
		    
			}
			outstanding_commitment_total = nf5.format(outstanding_commit_LTCORA+outstanding_commit_sales);
			outstanding_commitment_total_mcm = nf.format((outstanding_commit_LTCORA/conversion_factor_mcm)+(outstanding_commit_sales/conversion_factor_mcm));
		
			
			
			
			if(expected_volume_end_date.length()>=10)
			{
				 queryString = "SELECT SUM(CARGO_QTY) "
						+ "FROM FMS_TRADER_CARGO_MST "
						+ "WHERE START_DT >= TO_DATE(?,'DD/MM/YYYY') "
						+ "AND CARGO_STATUS ='Y' AND COMPANY_CD = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, from_dt);
				stmt.setString(2, comp_cd);
				rset = stmt.executeQuery();
				if(rset.next())
				{
					vol_exp_sales = rset.getString(1)==null?0:Double.parseDouble(nf.format(Double.parseDouble(rset.getString(1))));
				}
				stmt.close();
				rset.close();
				
				volume_expected_sales = nf5.format(vol_exp_sales);
				volume_expected_sales_mcm = nf.format((vol_exp_sales)/conversion_factor_mcm);
				
				
				 queryString = "SELECT NVL(A.EDQ_QTY,'0'),NVL(B.SUG,'1'), NVL(C.ADQ_QTY,'0') "
				    		+ "FROM FMS_LTCORA_CONT_CARGO_DTL A, FMS_LTCORA_CONT_MST B, FMS_LTCORA_CONT_CARGO_ADQ C "
				    		+ "WHERE A.COMPANY_CD = B.COMPANY_CD AND B.COMPANY_CD = C.COMPANY_CD "
				    		+ "AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND B.COUNTERPARTY_CD = C.COUNTERPARTY_CD "
				    		+ "AND A.AGMT_TYPE = B.AGMT_TYPE AND B.AGMT_TYPE = C.AGMT_TYPE "
				    		+ "AND A.CONT_NO = B.CONT_NO AND B.CONT_NO = C.CONT_NO AND "
				    		+ "A.CARGO_NO = C.CARGO_NO "
				    		+ "AND A.EDQ_FROM_DT >= TO_DATE(?,'DD/MM/YYYY') AND B.COMPANY_CD = ? ";
				 stmt = conn.prepareStatement(queryString);
				 stmt.setString(1,from_dt);
				 stmt.setString(2,comp_cd);
				 rset = stmt.executeQuery();
				 while(rset.next())
				 {
					 
					 eq_qty = rset.getDouble(1);
					 sgu = rset.getDouble(2);
					 aq_qty = rset.getDouble(3);
					if(aq_qty>0)
		    		{
						qty_to_supply1  += aq_qty-((aq_qty*sgu)/100);
		    		}
					else
					{
						qty_to_supply1 += eq_qty-((eq_qty*sgu)/100);
					}
				 }
				 stmt.close();
				 rset.close();
				    vol_exp_LTCORA = qty_to_supply1;
				    vol_exp_total = vol_exp_sales+vol_exp_LTCORA;
					volume_expected_total = nf.format(vol_exp_total);
					volume_expected_total_mcm = nf.format(vol_exp_total/conversion_factor_mcm);
				
			    }
			
				if(from_dt.length()>=10)
				{
					 queryString = "SELECT SUM(QTY) ";
					 queryString += "FROM ( SELECT SUM(ACT_QTY_MMBTU) QTY "
							+ "FROM FMS_BUY_CARGO_ALLOC "
							+ "WHERE (ACT_ARRV_DT BETWEEN TO_DATE(?,'DD/MM/YYYY') "
							+ "AND TO_DATE(?,'DD//MM/YYYY')) AND COMPANY_CD = ? ";
					queryString += "UNION ALL ";
					queryString += "SELECT SUM(QTY_MMBTU) QTY "
							+ "FROM FMS_BUY_DAILY_ALLOCATION "
							+ "WHERE (GAS_DT BETWEEN TO_DATE(?,'DD/MM/YYYY') "
							+ "AND TO_DATE(?,'DD//MM/YYYY')) AND COMPANY_CD = ? )";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, previous_month_start_date);
					stmt.setString(2, previous_month_end_date);
					stmt.setString(3, comp_cd);
					stmt.setString(4, previous_month_start_date);
					stmt.setString(5, previous_month_end_date);
					stmt.setString(6, comp_cd);
					rset = stmt.executeQuery();
					if(rset.next())
					{
						vol_recv_sales = rset.getString(1)==null?0:Double.parseDouble(nf.format(Double.parseDouble(rset.getString(1))));
						volume_received_sales = nf5.format(vol_recv_sales);
						volume_received_sales_mcm = nf.format(vol_recv_sales/conversion_factor_mcm);
					}
					stmt.close();
					rset.close();
					
					int counter = 0;
					queryString = "SELECT A.PERCENTAGE " 
							     + "FROM FMS_TANK_CONSUMPTION_MST A "
							     + "WHERE A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_TANK_CONSUMPTION_MST B WHERE "
							     + "B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) AND A.COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, first_day_month);
					stmt.setString(2, comp_cd);
					rset = stmt.executeQuery();
					if(rset.next())
					{
						String int_consumption = rset.getString(1)==null?"":rset.getString(1);
						if(!int_consumption.trim().equals(""))
						{
							consumption_percentage = Double.parseDouble(int_consumption);
						}
					}
					stmt.close();
					rset.close();
					consumption_percentage = Double.parseDouble(nf.format(consumption_percentage));
					
					queryString =	"SELECT SUM(TANK_MMBTU)  "
					+ "FROM FMS_TANK_INVENTORY_DTL  "
					+ "	WHERE TRUNC(INV_LEVEL_DT)=TO_DATE(?,'DD/MM/YYYY') AND COMPANY_CD = ?";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, first_day_month);
					stmt.setString(2, comp_cd);
					rset = stmt.executeQuery();
					if(rset.next())
					{
						month_opening_stock = rset.getString(1)==null?"0":nf5.format(Double.parseDouble(rset.getString(1)));
						opening_stock = rset.getString(1)==null?0:Double.parseDouble(nf.format(Double.parseDouble(rset.getString(1))));
						month_opening_stock_mcm = nf.format(opening_stock/conversion_factor_mcm);
					}
					else
					{
						month_opening_stock = "0";
						opening_stock = 0;
						month_opening_stock_mcm = "0";
					}
					stmt.close();
					rset.close();
					
					int_consumption_total = ((opening_stock +vol_exp_total)*consumption_percentage)/100.00;
					internal_consumption_total = nf5.format(int_consumption_total);
					internal_consumption_total_mcm = nf.format(int_consumption_total/conversion_factor_mcm);
					
					Vector CONT_NO = new Vector();
					Vector CUSTOMER_CD = new Vector();
					Vector CONT_TYPE = new Vector();
					qty_to_be_supplied=0; 
					
					queryString = "SELECT CONT_NO,CONTRACT_TYPE,COUNTERPARTY_CD "
							+ "FROM FMS_LTCORA_CONT_MST "
							+ "WHERE TO_DATE(?,'DD/MM/YYYY') "
							+ "BETWEEN START_DT AND END_DT AND COMPANY_CD = ? ";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, first_day_month);
					stmt.setString(2, comp_cd);
					rset = stmt.executeQuery();
					while(rset.next())
					{
						CONT_NO.add(rset.getString(1)==null?"":rset.getString(1));
						CONT_TYPE.add(rset.getString(2)==null?"":rset.getString(2));
						CUSTOMER_CD.add(rset.getString(3)==null?"":rset.getString(3));
					}
					stmt.close();
					rset.close();
					
					if(counter>0)
					{
						for(int i=0;i<counter;i++)
						{
							queryString = "SELECT SUM(QTY_MMBTU) "
									+ "FROM FMS_DAILY_ALLOCATION_DTL "
									+ "WHERE GAS_DT<TO_DATE(?,'DD/MM/YYYY') "
									+ "AND CONT_NO = ? AND CONTRACT_TYPE = ? "
									+ "AND COUNTERPARTY_CD = ? AND COMPANY_CD = ? ";
							stmt = conn.prepareStatement(queryString);
							stmt.setString(1, first_day_month);
							stmt.setString(2, ""+CONT_NO.elementAt(i));
							stmt.setString(3, ""+CONT_TYPE.elementAt(i));
							stmt.setString(4, ""+CUSTOMER_CD.elementAt(i));
							stmt.setString(5, comp_cd);
							rset = stmt.executeQuery();
							if(rset.next())
							{
								qty_to_be_supplied -= rset.getString(1)==null?0:Double.parseDouble(nf.format(Double.parseDouble(rset.getString(1))));
							}
							stmt.close();
							rset.close();
						}
					}
					
//					queryString = "SELECT TANK_D1_VOLUME " +
//							  "FROM FMS_TANK_MST " +
//							  "WHERE EFF_DT<TO_DATE(?,'DD/MM/YYYY') AND COMPANY_CD = ? " +
//							  "ORDER BY EFF_DT DESC ";
//					stmt = conn.prepareStatement(queryString);
//					stmt.setString(1, first_day_month);
//					System.out.println("first_day_month: "+first_day_month);
//					stmt.setString(2, comp_cd);
//					rset = stmt.executeQuery();
//					if(rset.next())
//					{
//						dead_stk = rset.getString(1)==null?700200:Double.parseDouble(nf.format(Double.parseDouble(rset.getString(1))));
////						dead_stk = 700200;				
//					}
//					stmt.close();
//					rset.close();
					
					double conv_factor_1=0, conv_factor_2=0;
					queryString1 = "SELECT SUM(A.TANK_CONV_FACTOR_1),SUM(A.TANK_CONV_FACTOR_2) "
							+ "FROM FMS_TANK_INVENTORY_DTL A "
							+ "WHERE A.INV_LEVEL_DT = (SELECT MAX(B.INV_LEVEL_DT) "
							+ "FROM FMS_TANK_INVENTORY_DTL B "
							+ "WHERE B.INV_LEVEL_DT <= TO_DATE(?,'DD/MM/YYYY') ) AND A.COMPANY_CD = ? ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, from_dt);
					stmt1.setString(2, comp_cd);
					rset1 = stmt1.executeQuery();
					if(rset1.next())
					{
						conv_factor_1 = rset1.getDouble(1);
						conv_factor_2 = rset1.getDouble(2);
					}
					rset1.close();
					stmt1.close();
					
					double FactorM3ToMMSCM=595; double FactorMMSCMtoMMBTU=38900;
					double d1_vol=0,d2_vol=0;
					queryString1 = "SELECT SUM(NVL(A.TANK_D1_VOLUME,0)),SUM(NVL(A.TANK_D2_VOLUME,0))  "
							+ "FROM FMS_TANK_MST A "
							+ "WHERE A.EFF_DT=(SELECT MAX(B.EFF_DT) "
							+ "FROM FMS_TANK_MST B "
							+ "WHERE B.EFF_DT<=TO_DATE(?,'DD/MM/YYYY')) AND A.COMPANY_CD = ? ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, from_dt);
					stmt1.setString(2, comp_cd);
					rset1 = stmt1.executeQuery();
					if(rset1.next())
					{
						d1_vol = rset1.getDouble(1);
						d2_vol = rset1.getDouble(2);
					}
					rset1.close();
					stmt1.close();
					
					if(conv_factor_1>0)
					{
						FactorM3ToMMSCM = conv_factor_1;	
					}
					T1Dead_Inventory_mmscm = ((d1_vol+d2_vol)*FactorM3ToMMSCM)/1000000;
					
					if(conv_factor_2>0)
					{
						FactorMMSCMtoMMBTU = conv_factor_2;	
					}
					T1Dead_Inventory_mmbtu = (T1Dead_Inventory_mmscm*FactorMMSCMtoMMBTU);
					
					dead_stock = nf5.format(T1Dead_Inventory_mmbtu);
					dead_stock_mcm = nf.format(T1Dead_Inventory_mmscm);
					
					String dd=from_dt.substring(0,2);
					if(Integer.parseInt(dd)<=15)
					{
						queryString = "SELECT TO_CHAR(LAST_DAY(TO_DATE(?,'DD/MM/YYYY')),'DD/MM/YYYY'),TO_CHAR(LAST_DAY(TO_DATE(?,'DD/MM/YYYY')),'MONTH-YY') FROM DUAL";
					}
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, previous_month_start_date1);
					stmt.setString(2, previous_month_start_date1);
					rset = stmt.executeQuery();
					if(rset.next())
					{
						String month_end_dt = rset.getString(1);
						selected_prev_month_year = rset.getString(2);
						if(Integer.parseInt(dd)<=15)
						{
							month_total=0;
							for(int a=0;a<VREF_DTL.size();a++)
							{
								String cd="",agmt_no="",cont_no="",cont_type="";
								String cont_map = VREF_DTL.elementAt(a).toString();
								
								cd = cont_map.split("-")[0];
								agmt_no = cont_map.split("-")[2];
								cont_no = cont_map.split("-")[4];
								cont_type = cont_map.split("-")[1];
								
								String queryString2 = "SELECT SUM(ALLOC_QTY) "
					    				+ "FROM ( SELECT SUM(EXIT_QTY_MMBTU) AS ALLOC_QTY "
					    				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
					    				+ "WHERE COMPANY_CD = ? AND CONTRACT_TYPE = ? AND GAS_DT BETWEEN TO_DATE(?, 'DD/MM/YYYY') AND TO_DATE(?, 'DD/MM/YYYY') AND SELL_CONT_MAP LIKE ? "
					    				+ "AND ALLOC_REV_NO = (SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B WHERE B.CONT_NO = A.CONT_NO AND B.AGMT_NO = A.AGMT_NO "
					    				+ "AND B.COMPANY_CD = A.COMPANY_CD AND B.COUNTERPARTY_CD = A.COUNTERPARTY_CD AND B.CONTRACT_TYPE = A.CONTRACT_TYPE "
					    				+ "AND B.SELL_CONT_MAP = A.SELL_CONT_MAP AND A.BU_SEQ = B.BU_SEQ AND B.GAS_DT = A.GAS_DT AND A.ENTRY_PT_MAPPING_ID = B.ENTRY_PT_MAPPING_ID "
					    				+ "AND A.EXIT_PT_MAPPING_ID = B.EXIT_PT_MAPPING_ID ) "
					    				+ "UNION "
					    				+ "SELECT SUM(QTY_MMBTU) AS ALLOC_QTY "
					    				+ "FROM FMS_DAILY_ALLOCATION_DTL A "
					    				+ "WHERE COMPANY_CD = ? AND COUNTERPARTY_CD = ? AND GAS_DT BETWEEN TO_DATE(?, 'DD/MM/YYYY')  AND TO_DATE(?, 'DD/MM/YYYY') AND AGMT_NO = ? AND CONT_NO = ? AND CONTRACT_TYPE = ? "
					    				+ "AND NOM_REV_NO = ( SELECT MAX(NOM_REV_NO) "
						    			+ "FROM FMS_DAILY_ALLOCATION_DTL B "
						    			+ "WHERE B.CONT_NO = A.CONT_NO AND B.AGMT_NO = A.AGMT_NO AND B.COMPANY_CD = A.COMPANY_CD "
						    			+ "AND B.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
						    			+ "AND B.TRANSPORTER_CD = A.TRANSPORTER_CD AND B.TRANS_SEQ = A.TRANS_SEQ AND B.PLANT_SEQ = A.PLANT_SEQ AND B.CONTRACT_TYPE = A.CONTRACT_TYPE AND B.GAS_DT = A.GAS_DT "
						    			+ "AND B.CARGO_NO = A.CARGO_NO) "
					    				+ "AND (A.COUNTERPARTY_CD || '-' || A.CONTRACT_TYPE || '-' || A.AGMT_NO || '-' || A.AGMT_REV || '-' || A.CONT_NO || '-' || A.CONT_REV) "
					    				+ "NOT IN ( SELECT B.SELL_CONT_MAP "
					    				+ "FROM FMS_DAILY_TRANSPORTER_ALLOC B "
					    				+ "WHERE A.COMPANY_CD = B.COMPANY_CD AND B.SELL_CONT_MAP LIKE ? AND A.GAS_DT = B.GAS_DT)) ";
					    				
					    		stmt2 = conn.prepareStatement(queryString2);
					    		stmt2.setString(1,comp_cd);
					    		stmt2.setString(2,"C");
					    		stmt2.setString(3,previous_month_start_date);
					    		stmt2.setString(4,month_end_dt);
					    		stmt2.setString(5,cont_map);
					    		stmt2.setString(6,comp_cd);
					    		stmt2.setString(7,cd);
					    		stmt2.setString(8,previous_month_start_date);
					    		stmt2.setString(9,month_end_dt);
					    		stmt2.setString(10,agmt_no);
					    		stmt2.setString(11,cont_no);
					    		stmt2.setString(12,cont_type);
					    		stmt2.setString(13,cont_map);
					    		rset2 = stmt2.executeQuery();
					    		if(rset2.next())
					    		{
					    			month_total += rset2.getString(1)==null?0:Double.parseDouble(nf.format(Double.parseDouble(rset2.getString(1))));
					    		}
//								month_total += rset1.getString(1)==null?0:Double.parseDouble(nf.format(Double.parseDouble(rset1.getString(1))));
					    		stmt2.close();
								rset2.close();
							}
						}
					
						month_total_sales = nf5.format(month_total);
						month_total_sales_mcm = nf.format(month_total/conversion_factor_mcm);
					}
					stmt.close();
					rset.close();
				}
			
			String dd=from_dt.substring(0,2);
			if(Integer.parseInt(dd)<=15)
			{
				net_uncommited_total_volume = opening_stock - T1Dead_Inventory_mmbtu - int_consumption_total + vol_exp_total - (outstanding_commit_LTCORA+outstanding_commit_sales) ;
				net_uncommited_total_mcm_volume = (opening_stock/conversion_factor_mcm) - (T1Dead_Inventory_mmbtu/conversion_factor_mcm) - (int_consumption_total/conversion_factor_mcm)
						 + (vol_exp_total/conversion_factor_mcm) - ((outstanding_commit_LTCORA/conversion_factor_mcm)+(outstanding_commit_sales/conversion_factor_mcm)) ;
			}
		    
			net_uncommited_total = nf5.format(net_uncommited_total_volume);
			net_uncommited_total_mcm = nf.format(net_uncommited_total_mcm_volume);
		    //SN LOA
		    for(int i=0; i<VSN_LOA_OUTER_COUNTER.size(); i++)
			{
				VFINAL_COUNT.add(""+VSN_LOA_OUTER_COUNTER.elementAt(i));
			}
			
			for(int i=0; i<VFINAL_COUNT.size(); i++)
			{
				if(Integer.parseInt(""+VFINAL_COUNT.elementAt(i))>max_count_for_column)
				{
					max_count_for_column = Integer.parseInt(""+VFINAL_COUNT.elementAt(i));
				}
			}
			
			for(int i=0; i<VCONT_NO.size(); i++)
			{
				int count = ((Vector)VCONT_NO.elementAt(i)).size();
				for(int j=count; j<max_count_for_column; j++)
				{
					((Vector)VCONT_NO.elementAt(i)).add("");
					((Vector)VCONT_REF.elementAt(i)).add("");
					((Vector)VAGMT_NO.elementAt(i)).add("");
					((Vector)VTCQ.elementAt(i)).add("");
					((Vector)VQTY_MMBTU.elementAt(i)).add("");
					((Vector)VBALANCE.elementAt(i)).add("");
					((Vector)VCONTRACT_TYPE.elementAt(i)).add("");
					((Vector)VREMARK.elementAt(i)).add("");
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
	String cargo_no = "";
	int temp_count =0;
	String year = "";
	
	String selected_from_date="";
	String volume_received_sales="";
	String volume_received_sales_mcm="";
	String month_opening_stock="";
	String month_opening_stock_mcm="";
	String internal_consumption_total="";
	String internal_consumption_total_mcm="";
	String dead_stock="";
	String dead_stock_mcm="";
	String selected_prev_month_year="";
	String month_total_sales="";
	String month_total_sales_mcm="";
	String outstanding_commitment_total="";
	String outstanding_commitment_total_mcm="";
	String net_uncommited_total="";
	String net_uncommited_total_mcm="";
	String volume_expected_sales="";
	String volume_expected_sales_mcm="";
	String total_sug_lt = "";
	String total_sug_lt_mcm = "";
	String net_commit_lt_vol = "";
	String net_commit_lt_vol_mcm = "";
	double consumption_percentage=0;
	String total_expect_vol="";
	String total_expect_vol_mcm="";
	String total_vol_supplied="";
	String total_vol_supplied_mcm="";
	String total_outstanding_commitment="";
	String total_outstanding_commitment_mcm="";
	public int max_count_for_column;
	
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
	public void setCargo_no(String cargo_no) {this.cargo_no = cargo_no;}
	
	public void setYear(String year) {this.year = year;}
	
	public String getDeal_map() {return deal_map;}
	public String getCont_ref_no() {return cont_ref_no;}
	public String getAgmt_base() {return agmt_base;}
	public int getTemp_count() {return temp_count;}
	
	public String getSelected_from_date() {return selected_from_date;}
	public String getFrom_date() {return from_dt;}
	public int getMax_count_for_column() {return max_count_for_column;}
	public String getVolume_received_sales() {return volume_received_sales;}
	public String getVolume_received_sales_mcm() {return volume_received_sales_mcm;}
	public String getMonth_opening_stock() {return month_opening_stock;}
	public String getMonth_opening_stock_mcm() {return month_opening_stock_mcm;}
	public String getInternal_consumption_total() {return internal_consumption_total;}
	public String getInternal_consumption_total_mcm() {return internal_consumption_total_mcm;}
	public String getDead_stock() {return dead_stock;}
	public String getDead_stock_mcm() {return dead_stock_mcm;}
	public String getSelected_prev_month_year() {return selected_prev_month_year;}
	public String getMonth_total_sales() {return month_total_sales;}
	public String getMonth_total_sales_mcm() {return month_total_sales_mcm;}
	public String getOutstanding_commitment_total() {return outstanding_commitment_total;}
	public String getOutstanding_commitment_total_mcm() {return outstanding_commitment_total_mcm;}
	public String getNet_uncommited_total() {return net_uncommited_total;}
	public String getNet_uncommited_total_mcm() {return net_uncommited_total_mcm;}
	public String getVolume_expected_sales() {return volume_expected_sales;}
	public String getVolume_expected_sales_mcm() {return volume_expected_sales_mcm;}
	public String getTotal_sug_lt() {return total_sug_lt;}
	public String getTotal_sug_lt_mcm() {return total_sug_lt_mcm;}
	public double getConsumption_percentage() {return consumption_percentage;}
	public String getNet_commit_lt_vol() {return net_commit_lt_vol;}
	public String getNet_commit_lt_vol_mcm() {return net_commit_lt_vol_mcm;}
	public String getTotal_expect_vol() {return total_expect_vol;}
	public String getTotal_expect_vol_mcm() {return total_expect_vol_mcm;}
	public String getTotal_vol_supplied() {return total_vol_supplied;}
	public String getTotal_vol_supplied_mcm() {return total_vol_supplied_mcm;}
	public String getTotal_outstanding_commitment() {return total_outstanding_commitment;}
	public String getTotal_outstanding_commitment_mcm() {return total_outstanding_commitment_mcm;}
	
    
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VCOUNTERPTY_CD = new Vector();
	Vector VCOUNTERPTY_ABBR = new Vector();
	Vector VCOUNTERPTY_NM = new Vector();
	Vector VCONT_NO = new Vector();
	Vector VAGMT_NO = new Vector();
	Vector VCONTRACT_TYPE = new Vector();
	
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
	
	Vector VPLANT_SEQ_NO = new Vector();
	Vector VPLANT_NM = new Vector();
	Vector VCONT_REV = new Vector();
	Vector VAGMT_REV = new Vector();
	Vector VCOLOR = new Vector();
	
	Vector VREMARK = new Vector();
	Vector VBALANCE = new Vector();
	Vector VSN_LOA_TOTAL_BALANCE = new Vector();
	Vector VINNER_CUSTOMER_CD_SN_LOA = new Vector();
	Vector VSN_LOA_OUTER_COUNTER = new Vector();
	Vector VFINAL_COUNT = new Vector();
	Vector VCUSTOMER_CD_LTCORA = new Vector();
	Vector VCUSTOMER_NM_LTCORA = new Vector();
	Vector LTCORA_TOTAL_UNLOADED_VOLUME = new Vector();
	Vector LTCORA_TOTAL_UNLOADED_VOLUME_MCM = new Vector();
	Vector VTCQ = new Vector();
	Vector VREF_DTL = new Vector();
	
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVCOUNTERPTY_CD() {return VCOUNTERPTY_CD;}
	public Vector getVCOUNTERPTY_ABBR() {return VCOUNTERPTY_ABBR;}
	public Vector getVCOUNTERPTY_NM() {return VCOUNTERPTY_NM;}
	public Vector getVCONT_NO() {return VCONT_NO;}
	public Vector getVAGMT_NO() {return VAGMT_NO;}
	public Vector getVCONTRACT_TYPE() {return VCONTRACT_TYPE;}
	
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
	
	public Vector getVPLANT_SEQ_NO() {return VPLANT_SEQ_NO;}
	public Vector getVPLANT_NM() {return VPLANT_NM;}
	public Vector getVCONT_REV() {return VCONT_REV;}
	public Vector getVAGMT_REV() {return VAGMT_REV;}
	public Vector getVCOLOR() {return VCOLOR;}
	
	public Vector getVREMARK() {return VREMARK;}
	public Vector getVTOTAL_BALANCE() {return VBALANCE;}
	public Vector getVSN_LOA_TOTAL_BALANCE() {return VSN_LOA_TOTAL_BALANCE;}
	public Vector getVINNER_CUSTOMER_CD_SN_LOA() {return VINNER_CUSTOMER_CD_SN_LOA;}
	public Vector getVSN_LOA_OUTER_COUNTER() {return VSN_LOA_OUTER_COUNTER;}
	public Vector getVFINAL_COUNT() {return VFINAL_COUNT;}
	public Vector getVCUSTOMER_CD_LTCORA() {return VCUSTOMER_CD_LTCORA;}
	public Vector getVCUSTOMER_NM_LTCORA() {return VCUSTOMER_NM_LTCORA;}
	public Vector getVLTCORA_TOTAL_UNLOADED_VOLUME() {return LTCORA_TOTAL_UNLOADED_VOLUME;}
	public Vector getVLTCORA_TOTAL_UNLOADED_VOLUME_MCM() {return LTCORA_TOTAL_UNLOADED_VOLUME_MCM;}
	public Vector getVTCQ() {return VTCQ;}
	public Vector getVREF_DTL() {return VREF_DTL;}
	
	
}