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

public class DB_ContractMgmt_Report2
{
String db_src_file_name="DB_ContractMgmt_Report2.java";
	
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
	    			if(callFlag.equalsIgnoreCase("ALLOCATION_TO_CUSTOMER_PLANT"))
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
	public int getTemp_count() {return temp_count;}
	public void setCargo_no(String cargo_no) {this.cargo_no = cargo_no;}
    
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
	
	
	
}