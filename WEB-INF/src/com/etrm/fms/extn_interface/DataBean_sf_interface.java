package com.etrm.fms.extn_interface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

public class DataBean_sf_interface
{
	String db_src_file_name="DataBean_sf_interface.java";
	
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
	    			if(callFlag.equalsIgnoreCase("SF_ACTIVE_DEAL"))
	    			{
	    				getCounterpartyList();
	    				getSegment();
	    				getActiveDealsDtl();
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
	
	public void getCounterpartyList()
	{
		String function_nm="getCounterpartyList()";
		try
		{
			String queryString1="SELECT DISTINCT COMPANY_CD, COUNTERPARTY_CD  "
					+ "FROM FMS_LTCORA_CONT_CARGO_DTL  "
					+ "WHERE COMPANY_CD=? AND BUY_SALE=? ";
			queryString1+="UNION "
					+ "SELECT DISTINCT COMPANY_CD, COUNTERPARTY_CD  "
					+ "FROM FMS_SUPPLY_CONT_MST  "
					+ "WHERE COMPANY_CD=? ";
			stmt=conn.prepareStatement(queryString1);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "C");
			stmt.setString(3, comp_cd);
			ResultSet rset=stmt.executeQuery();
			while(rset.next())
			{
				String own_cd=rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
				
				VMST_COUNTERPARTY_CD.add(countpty_cd);
				VMST_COUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
				VMST_COUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
			}
			rset.close();
			stmt.close();
			
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
			VSEGMENT.add("RLNG");
			VSEGMENT.add("DLNG");

			VSEGMENT_NM.add("RLNG Sales/LTCORA Contract");
			VSEGMENT_NM.add("DLNG Sales Contract");
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getActiveDealsDtl()
	{
		String function_nm="getActiveDealsDtl()";
		try
		{
			for(int i=0;i<VSEGMENT.size();i++) 
			{
				int selCnt=0;
				int dataCnt=0;
				
				String queryString ="";
				
				if(VSEGMENT.elementAt(i).equals("RLNG"))
				{
					queryString += "SELECT COMPANY_CD, COUNTERPARTY_CD, CONTRACT_TYPE, CONT_NO, CARGO_NO, CARGO_REF, '', TO_CHAR(EDQ_FROM_DT, 'DD/MM/YYYY'), " //HM : As per vijay's feedback 20250422 selecting FROM_DT instead of ACTUAL_RECPT_DT
						    + " TO_CHAR(ACTUAL_RECPT_DT + COALESCE(STORAGE_EXT_DAYS, 0) + COALESCE(STORAGE_DAYS-1, 0),'DD/MM/YYYY'), " //HM : After vijay's feedback 20250422, calculating Storage days on bases of ACTUAL_RECPT_DT and not with STRAT_DT (As it was before.) 
						    + " CARGO_STATUS, TO_CHAR(COALESCE(MODIFY_DT,ENT_DT), 'DD/MM/YYYY HH24:MI:SS'),AGMT_NO,AGMT_REV,CONT_REV,TO_CHAR(SF_GEN_DT, 'DD/MM/YYYY HH24:MI:SS'),'Y' "
						    + " FROM FMS_LTCORA_CONT_CARGO_DTL A "
						    + " WHERE COMPANY_CD=? AND BUY_SALE=? "
						    + " AND ACTUAL_RECPT_DT<=TO_DATE(?,'DD/MM/YYYY') "
						    + " AND (ACTUAL_RECPT_DT + COALESCE(STORAGE_EXT_DAYS, 0) + COALESCE(STORAGE_DAYS-1, 0))>=TO_DATE(?,'DD/MM/YYYY') "
						    + " AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B "
							+ " WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE "
							+ " AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
							+ " AND A.CONT_NO=B.CONT_NO AND A.CARGO_NO=B.CARGO_NO) "
						    + " UNION ";
				}
				queryString +="SELECT COMPANY_CD, COUNTERPARTY_CD, CONTRACT_TYPE, CONT_NO, 0, CONT_REF_NO, TRADE_REF_NO, TO_CHAR(START_DT, 'DD/MM/YYYY'), "
					    + " TO_CHAR(END_DT, 'DD/MM/YYYY'), CONT_STATUS, TO_CHAR(COALESCE(MODIFY_DT,ENT_DT), 'DD/MM/YYYY HH24:MI:SS'),AGMT_NO,AGMT_REV,CONT_REV,TO_CHAR(SF_GEN_DT, 'DD/MM/YYYY HH24:MI:SS'),IS_ALLOCATED "
					    + " FROM FMS_SUPPLY_CONT_MST A "
					    + " WHERE COMPANY_CD=? "
					    + " AND START_DT<=TO_DATE(?,'DD/MM/YYYY') "
					    + " AND END_DT>=TO_DATE(?,'DD/MM/YYYY') "
					    + " AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ " AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				
				if(VSEGMENT.elementAt(i).equals("RLNG"))
				{
					queryString +="AND CONTRACT_TYPE IN ('S','L','X')";
				}
				else if(VSEGMENT.elementAt(i).equals("DLNG"))
				{
					queryString +="AND CONTRACT_TYPE IN ('F','E','W')";
				}
				String temp_string = queryString;
				
				stmt=conn.prepareStatement(temp_string);
				if(VSEGMENT.elementAt(i).equals("RLNG"))
				{
					stmt.setString(++selCnt, comp_cd);
					stmt.setString(++selCnt, "C");//BUY_SALE
					stmt.setString(++selCnt, report_dt);
					stmt.setString(++selCnt, report_dt);
				}
				stmt.setString(++selCnt, comp_cd);
				stmt.setString(++selCnt, report_dt);
				stmt.setString(++selCnt, report_dt);
				ResultSet rset=stmt.executeQuery();
				while(rset.next())
				{
					dataCnt++;
					
					String own_cd=rset.getString(1)==null?"":rset.getString(1);
					String countpty_cd=rset.getString(2)==null?"":rset.getString(2);
					String cont_type = rset.getString(3)==null?"":rset.getString(3);
					String cont =rset.getString(4)==null?"":rset.getString(4);
					String cargo_no =rset.getString(5)==null?"":rset.getString(5);
					String cont_cargo_ref =rset.getString(6)==null?"":rset.getString(6);
					String agmt =rset.getString(12)==null?"":rset.getString(12);
					String agmt_rev =rset.getString(13)==null?"":rset.getString(13);
					String cont_rev =rset.getString(14)==null?"":rset.getString(14);
					String last_mod_dt_time =rset.getString(11)==null?"":rset.getString(11);
					String sf_gen_dt =rset.getString(15)==null?"":rset.getString(15);
					String is_allocated =rset.getString(16)==null?"N":rset.getString(16);
					
					String ltcora_cont_ref = "";
		            String ltcora_cont_name ="";
		            
		            String queryString_temp = "SELECT CONT_REF_NO,CONT_NAME "
							+ "FROM FMS_LTCORA_CONT_MST A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
							+ "AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? "
							+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
					stmt_temp=conn.prepareStatement(queryString_temp);
					stmt_temp.setString(1, own_cd);
					stmt_temp.setString(2, countpty_cd);
					stmt_temp.setString(3, agmt);
					stmt_temp.setString(4, agmt_rev);
					stmt_temp.setString(5, cont_type);
					stmt_temp.setString(6, cont);
					ResultSet rset_temp=stmt_temp.executeQuery();
			  		if(rset_temp.next())
			  		{
			  			ltcora_cont_ref =  rset_temp.getString(1)==null?"":rset_temp.getString(1);
			  			ltcora_cont_name = rset_temp.getString(2)==null?"":rset_temp.getString(2);
			  		}
			  		rset_temp.close();
			  		stmt_temp.close();
			  		
					VCOUNTERPARTY_CD.add(countpty_cd);
					VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
					VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
					VCONTRACT_TYPE.add(utilBean.getContractTypeName(cont_type));
					
					VCARGO_NO.add(cargo_no);
					
					String cont_disp_name = utilBean.NewDealMappingId(own_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, cargo_no);
					String sf_cont_disp_name = utilBean.SalesforceDealMapping(own_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, cargo_no);
					
					VCONT_NO.add(cont_disp_name);
					VCONT_SF_NO.add(sf_cont_disp_name);
					
					if(cont_type.equals("X") || cont_type.equals("W")) 
					{
						VCONT_REF_NO.add(rset.getString(7)==null?"":rset.getString(7));//TRADE REF NO
					} 
					else
					{
						VCONT_REF_NO.add(cont_cargo_ref);//CONT REF NO
						
						/*if(cont_type.equals("O") || cont_type.equals("Q")) 
						{
							VCONT_REF_NO.add(cont_cargo_ref);//CONT REF NO
						}
						else
						{
							VCONT_REF_NO.add(rset.getString(6)==null?"":rset.getString(6));//CONT REF NO
						}*/
					}
					//VTRADE_REF_NO.add(rset.getString(7)==null?"":rset.getString(7));
					
					VCONT_START_DT.add(rset.getString(8)==null?"":rset.getString(8));
					VCONT_END_DT.add(rset.getString(9)==null?"":rset.getString(9));
					
					//HM : Removed below condition as both condition are performing same output (As to resolve vulnerability)
					/*if(cont_type.equals("X") || cont_type.equals("W")) 
					{
						VCONT_START_DT.add(rset.getString(8)==null?"":rset.getString(8));
						VCONT_END_DT.add(rset.getString(9)==null?"":rset.getString(9));
					} 
					else
					{
						VCONT_START_DT.add(rset.getString(8)==null?"":rset.getString(8));
						VCONT_END_DT.add(rset.getString(9)==null?"":rset.getString(9));
					}*/
					
					String cont_status = rset.getString(10)==null?"":rset.getString(10);
					VCONT_STATUS.add(getContStatusName(cont_status));
					VLAST_MODIFY_DT.add(last_mod_dt_time);
					
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

					if(!is_allocated.equals("Y")) 
					{
						VSF_XML_STATUS.add("Y");//Posting not Allowed
						VSF_ROW_COLOR.add("#f2f2f2");
					}
					else
					{
						if (!cont_status.equals("Y"))
						{	
							VSF_XML_STATUS.add("Y");//Posting not Allowed
						} 
						else
						{									
							if(!sf_gen_dt.equals(""))
							{
								LocalDateTime lastModDateTime = LocalDateTime.parse(last_mod_dt_time, formatter);
						        LocalDateTime sfGenDateTime = LocalDateTime.parse(sf_gen_dt, formatter);
						        
								if (lastModDateTime.isAfter(sfGenDateTime)) 
						        {
						            VSF_XML_STATUS.add("N");//Posting yet not done for Last modified contract
						        } 
								else
								{
									VSF_XML_STATUS.add("Y");//Posting done for Last modified contract
								}
							}
							else
							{						
								VSF_XML_STATUS.add("N");//Posting yet not done for Last modified contract
							}
						}
						
						VSF_ROW_COLOR.add("#ffffff");
					}
					
					String cont_xml_gen_mapping = own_cd+"@"+countpty_cd+"@"+agmt+"@"+agmt_rev+"@"+cont+"@"+cont_rev+"@"+cont_type+"@"+cargo_no;
					VCONT_XML_GEN_MAPPING.add(cont_xml_gen_mapping);
					VCONT_SF_XML_GEN_DT.add(sf_gen_dt);
				}
				rset.close();
				stmt.close();
				
				VINDEX.add(dataCnt);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public String getContStatusName(String status_flg)
	{
		String function_nm="getContStatusName()";
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
			else if(status_flg.equals("T"))
			{
				nm="Terminated";
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return nm;
	}
	
	String callFlag = "";
	public void setCallFlag(String callFlag) {this.callFlag = callFlag;}
	String comp_cd = "";
	public void setComp_cd(String comp_cd) {this.comp_cd = comp_cd;}
	
	String report_dt = "";
	public void setReport_dt(String report_dt) {this.report_dt = report_dt;}
	String from_dt = "";
	public void setFrom_dt(String from_dt) {this.from_dt = from_dt;}
	String to_dt = "";
	public void setTo_dt(String to_dt) {this.to_dt = to_dt;}
	
	String bu_region = "";
	public String getBu_Region() {return bu_region;}
	
	String counterparty_cd = "";
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	
	Vector VMST_COUNTERPARTY_CD = new Vector();
	Vector VMST_COUNTERPARTY_NM = new Vector();
	Vector VMST_COUNTERPARTY_ABBR = new Vector();
	
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VCOUNTERPARTY_CATEGORY = new Vector();
	Vector VCONTRACT_TYPE = new Vector();
	Vector VBUY_SALE = new Vector();
	Vector VCONT_NO = new Vector();
	Vector VCONT_SF_NO = new Vector();
	Vector VCONT_REF_NO = new Vector();
	Vector VTRADE_REF_NO = new Vector();
	Vector VCONT_START_DT = new Vector();
	Vector VCONT_END_DT = new Vector();
	Vector VCONT_STATUS = new Vector();
	Vector VLAST_MODIFY_DT = new Vector();
	Vector VSF_XML_STATUS = new Vector();
	Vector VSF_ROW_COLOR = new Vector();
	Vector VCONT_XML_GEN_MAPPING = new Vector();
	Vector VCONT_SF_XML_GEN_DT = new Vector();
	Vector VCARGO_NO = new Vector();
	Vector VDEAL_REF = new Vector();
	
	Vector VSEGMENT = new Vector();
	Vector VSEGMENT_NM = new Vector();
	Vector VSEGMENT_TYPE = new Vector();
	
	Vector VINDEX = new Vector();
	
	public Vector getVMST_COUNTERPARTY_CD() {return VMST_COUNTERPARTY_CD;}
	public Vector getVMST_COUNTERPARTY_NM() {return VMST_COUNTERPARTY_NM;}
	public Vector getVMST_COUNTERPARTY_ABBR() {return VMST_COUNTERPARTY_ABBR;}
	
	public Vector getVDEAL_REF(){return VDEAL_REF;}
	public Vector getVCONT_NO(){return VCONT_NO;}
	public Vector getVCONT_SF_NO(){return VCONT_SF_NO;}
	public Vector getVCONT_REF_NO(){return VCONT_REF_NO;}
	public Vector getVTRADE_REF_NO(){return VTRADE_REF_NO;}
	public Vector getVCONT_START_DT(){return VCONT_START_DT;}
	public Vector getVCONT_END_DT(){return VCONT_END_DT;}
	public Vector getVCONT_STATUS(){return VCONT_STATUS;}
	public Vector getVLAST_MODIFY_DT(){return VLAST_MODIFY_DT;}
	public Vector getVSF_XML_STATUS(){return VSF_XML_STATUS;}
	public Vector getVSF_ROW_COLOR(){return VSF_ROW_COLOR;}
	public Vector getVCONT_XML_GEN_MAPPING(){return VCONT_XML_GEN_MAPPING;}
	public Vector getVCONT_SF_XML_GEN_DT(){return VCONT_SF_XML_GEN_DT;}
	public Vector getVCARGO_NO(){return VCARGO_NO;}
	public Vector getVBUY_SALE(){return VBUY_SALE;}
	public Vector getVCONTRACT_TYPE(){return VCONTRACT_TYPE;}
	public Vector getVCOUNTERPARTY_CATEGORY(){return VCOUNTERPARTY_CATEGORY;}
	public Vector getVCOUNTERPARTY_ABBR(){return VCOUNTERPARTY_ABBR;}
	public Vector getVCOUNTERPARTY_NM(){return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_CD(){return VCOUNTERPARTY_CD;}
	
	public Vector getVSEGMENT() {return VSEGMENT;}
	public Vector getVSEGMENT_NM() {return VSEGMENT_NM;}
	public Vector getVSEGMENT_TYPE() {return VSEGMENT_TYPE;}
	
	public Vector getVINDEX() {return VINDEX;}
}
