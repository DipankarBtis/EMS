package com.etrm.fms.mgmt_reports;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.Date;
import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DB_AllocationUtil;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;

//Coded By          : Harsh Maheta
//Code Reviewed by	:  
//CR Date			: 01/01/2024 
//Status	  		: Developing

public class DataBean_MGMT_Reports
{
	String db_src_file_name="DataBean_MGMT_Reports.java";
	
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
	    			if(callFlag.equalsIgnoreCase("IND_GAS_POC"))//NCF Report
	    			{
	    				comp_abbr = utilBean.getCompanyAbbr(conn,comp_cd);
	    				getCounterpartyList();
	    				getIND_GAS_POC_Dtl();		
	    			}
	    			else if(callFlag.equalsIgnoreCase("STORAGE_REPORT"))	//Storage report
	    			{
	    				getLtcoraContTraderCounterpartyList();
	    				getLtcoraStorageDtls();
	    			}
	    			else if(callFlag.equalsIgnoreCase("ENERGY_STATEMENT"))	//ENERGY STATEMENT REPORT 
	    			{
	    				getLtcoraContTraderCounterpartyList();
	    				getBuContactInfo();
	    				getLtcoraEnergyStatement();
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
	
	//Energy Statement
	public void getLtcoraEnergyStatement()
	{
		String function_nm="getLtcoraEnergyStatement()";
		try
		{
			//for adq
			String common_adq="FROM FMS_LTCORA_CONT_CARGO_ADQ B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO  "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO AND B.CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_ADQ C "
					+ "WHERE C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND C.BUY_SALE=B.BUY_SALE AND C.AGMT_TYPE=B.AGMT_TYPE AND C.AGMT_NO=B.AGMT_NO AND C.AGMT_REV=B.AGMT_REV AND C.CONT_NO=B.CONT_NO  "
					+ "AND C.CONTRACT_TYPE=B.CONTRACT_TYPE AND C.CARGO_NO=B.CARGO_NO) ";
			
			String adq_count="SELECT COUNT(*) "+common_adq;
			String adq="SELECT SUM(ADQ_QTY) "+common_adq;
			
			String final_adq="(CASE WHEN ("+adq_count+")> 0 THEN ("+adq+") ELSE A.EDQ_QTY END )";
			
			//for SUG
			String common_sug="FROM FMS_LTCORA_CONT_CARGO_MOD B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.AGMT_TYPE=B.AGMT_TYPE AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE  "
					+ "AND A.CARGO_NO=B.CARGO_NO AND B.CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_MOD C WHERE C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD  "
					+ "AND C.BUY_SALE=B.BUY_SALE AND C.AGMT_NO=B.AGMT_NO AND C.AGMT_REV=B.AGMT_REV AND C.AGMT_TYPE=B.AGMT_TYPE AND C.CONT_NO=B.CONT_NO AND C.CONTRACT_TYPE=B.CONTRACT_TYPE  "
					+ "AND C.CARGO_NO=B.CARGO_NO ) AND B.APPROVAL_FLAG='Y' ";
			
			String sug_count="SELECT COUNT(*) "+common_sug;
			String sug = "SELECT SUG "+common_sug;
			
			String final_sug="(CASE WHEN ("+sug_count+")>0 THEN ("+sug+") ELSE "+(sugValue.equals("")?"0":sugValue)+" END)";	
			
			//for sug value
			String sug_value="("+final_adq+"*"+final_sug+"/100"+")";
			
			//for Regassification value 
			String regass_qty = "("+final_adq+"-"+sug_value+")";
			
			//main query
			String queryString="SELECT A.CARGO_NO,A.QQ_NO,TO_CHAR(A.QQ_DT,'DD/MM/YYYY'),A.SHIP_CD,TO_CHAR(A.ACTUAL_RECPT_DT,'DD/MM/YYYY'), "
					+ "CASE WHEN A.STORAGE_EXT_DAYS IS NOT NULL THEN TO_CHAR(A.ACTUAL_RECPT_DT + A.STORAGE_DAYS - 1 + A.STORAGE_EXT_DAYS, 'DD/MM/YYYY')  "
					+ "ELSE TO_CHAR(A.ACTUAL_RECPT_DT + A.STORAGE_DAYS - 1, 'DD/MM/YYYY') END STORAGE_END_DT, "
					+ "A.BOE_NO,TO_CHAR(A.BOE_DT,'DD/MM/YYYY'),"+final_adq+" UNLOADED ,"+sug_value+" SUG_VALUE,"+regass_qty+" REGAS_QTY, "
					+ "TO_CHAR(D.SIGNING_DT,'DD/MM/YYYY') "
					+ "FROM FMS_LTCORA_CONT_CARGO_DTL A, FMS_LTCORA_CONT_MST D "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? AND A.AGMT_TYPE=?"
					+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
					+ "AND A.CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.AGMT_TYPE=B.AGMT_TYPE AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO) AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.CONTRACT_TYPE=D.CONTRACT_TYPE "
					+ "AND A.BUY_SALE=D.BUY_SALE AND A.AGMT_NO=D.AGMT_NO AND A.AGMT_REV=D.AGMT_REV AND A.AGMT_TYPE=D.AGMT_TYPE AND A.CONT_NO=D.CONT_NO AND A.CONT_REV=D.CONT_REV "
					+ "AND D.CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST E WHERE D.COMPANY_CD=E.COMPANY_CD AND D.COUNTERPARTY_CD=E.COUNTERPARTY_CD "
					+ "AND D.BUY_SALE=E.BUY_SALE AND D.AGMT_NO=E.AGMT_NO AND D.AGMT_REV=E.AGMT_REV AND D.AGMT_TYPE=E.AGMT_TYPE AND D.CONT_NO=E.CONT_NO AND D.CONT_REV=E.CONT_REV "
					+ "AND D.CONTRACT_TYPE=E.CONTRACT_TYPE) ";
			String tempQueryString=queryString;
			stmt=conn.prepareStatement(tempQueryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, buy_sell);
			stmt.setString(4, agmt_type);
			stmt.setString(5, agmt_no);
			stmt.setString(6, agmt_rev);
			stmt.setString(7, cont_no);
			stmt.setString(8, cont_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String cargo_no=rset.getString(1)==null?"":rset.getString(1);
				String qq_no = rset.getString(2)==null?"":rset.getString(2);
				String qq_dt = rset.getString(3)==null?"":rset.getString(3);
				String ship_cd = rset.getString(4)==null?"":rset.getString(4);
				String storage_start_dt = rset.getString(5)==null?"":rset.getString(5);
				String storage_end_dt = rset.getString(6)==null?"":rset.getString(6);
				String boe_no=rset.getString(7)==null?"":rset.getString(7);
				String boe_dt=rset.getString(8)==null?"":rset.getString(8);
				String unloaded = rset.getString(9)==null?"":nf.format(rset.getDouble(9));
				String sug_qty = rset.getString(10)==null?"":nf.format(rset.getDouble(10));
				String regassify_qty = rset.getString(11)==null?"":nf.format(rset.getDouble(11));
				signing_dt = rset.getString(12)==null?"":rset.getString(12);
				
				String deal_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, cont_type, cargo_no);
				String ship_name = utilBean.getShipName(conn, ship_cd);
				
				VDEAL_MAP.add(deal_map);
				VQQ_NO.add(qq_no);
				VQQ_DT.add(qq_dt);
				VSHIP_NM.add(ship_name);
				VSTORAGE_START_DT.add(storage_start_dt);
				VSTORAGE_END_DT.add(storage_end_dt);
				VBOE_NO.add(boe_no);
				VBOE_DT.add(boe_dt);
				VUNLOADED_QTY.add(unloaded);
				VSUG_QTY.add(sug_qty);
				VREGASSIFY_QTY.add(regassify_qty);
				
				String company_abbr = utilBean.getCompanyAbbr(conn, comp_cd);
				String counterparty_abbr = utilBean.getCounterpartyABBR(conn, counterparty_cd);
				
				String file_nm=company_abbr+"-LTCORA_Energy_Statement-"+counterparty_abbr+"-"+deal_map+".pdf";

				String main_folder=CommonVariable.work_dir+comp_cd;
				String sub_folder="ltcora_reports";
				
				String path = appPath+"/"+main_folder+"/"+sub_folder+"/"+file_nm;
				
				if(!new File(path).exists()) 
				{
					VISFILE_EXIST.add("N");
				}
				else
				{
					VISFILE_EXIST.add("Y");
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
	
	public void getBuContactInfo()
	{
		String function_nm="getBuContactInfo()";
		try 
		{
			String queryString="SELECT DISTINCT CONTACT_PERSON, SEQ_NO "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=?"
					+ "AND ENTITY=? AND NOM_FLAG=? AND ADDR_FLAG=? "
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.TYPE=B.TYPE "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, comp_cd);
			stmt.setString(3, "B");
			stmt.setString(4, "Y");
			stmt.setString(5, "C");
			stmt.setString(6, "Y");
			stmt.setString(7, "Y");
			stmt.setString(8, "RLNG");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String contact_person=rset.getString(1)==null?"":rset.getString(1);
				String seq_no= rset.getString(2)==null?"":rset.getString(2);
				VCONTACT_PERSON.add(contact_person);
				VCONTACT_PERSON_SEQ_NO.add(seq_no);
				
				VIEW_LTCORA_INFO.put("contact_person"+seq_no,contact_person);
			}
			rset.close();
			stmt.close();
			
			String ownAddress="";
			String ownCity="";
			String ownState="";
			String ownPin="";
			String ownCountry="";
			String ownPhone="";
			String ownEmail="";
			
			String queryString2="SELECT ADDR,CITY,STATE,PIN,COUNTRY,PHONE,EMAIL "
					+ "FROM FMS_COMPANY_OWNER_ADDR_MST A "
					+ "WHERE COMPANY_CD=? AND ADDRESS_TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COMPANY_OWNER_ADDR_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.ADDRESS_TYPE=B.ADDRESS_TYPE "
					+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, "C");
			rset2=stmt2.executeQuery();
			if(rset2.next())
			{
				ownAddress  = rset2.getString(1)==null?"":rset2.getString(1);
				ownCity  = rset2.getString(2)==null?"":rset2.getString(2);
				ownState  = rset2.getString(3)==null?"":rset2.getString(3);
				ownPin  = rset2.getString(4)==null?"":rset2.getString(4);
				ownCountry  = rset2.getString(5)==null?"":rset2.getString(5);
				ownPhone  = rset2.getString(6)==null?"":rset2.getString(6);
				ownEmail  = rset2.getString(7)==null?"":rset2.getString(7);
			}
			rset2.close();
			stmt2.close();
			VIEW_LTCORA_INFO.put("ownAddress",ownAddress);
			VIEW_LTCORA_INFO.put("ownCity",ownCity);
			VIEW_LTCORA_INFO.put("ownState",ownState);
			VIEW_LTCORA_INFO.put("ownPin",ownPin);
			VIEW_LTCORA_INFO.put("ownCountry",ownCountry);
			VIEW_LTCORA_INFO.put("ownPhone",ownPhone);
			VIEW_LTCORA_INFO.put("ownEmail",ownEmail);
			
			String customerAddress="";
			String customerCity="";
			String customerPin="";
			String customerState="";
			String customerZone="";
			String customerCountry="";
			String customerPhone="";
			String customerEmail="";
			String contact_person_nm="";
			String contact_person_phone="";
			String contact_person_fax="";
			
			String customer_comp_nm = utilBean.getCounterpartyName(conn, counterparty_cd);
			String customer_comp_abbr= utilBean.getCounterpartyABBR(conn, counterparty_cd);
			
			String queryString3="SELECT ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,EMAIL  "
					+ "FROM FMS_COUNTERPARTY_ADDR_MST A  "
					+ "WHERE COUNTERPARTY_CD=? AND ADDRESS_TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_ADDR_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD  "
					+ "AND A.ADDRESS_TYPE=B.ADDRESS_TYPE) ";
			stmt2=conn.prepareStatement(queryString3);
			stmt2.setString(1, counterparty_cd);
			stmt2.setString(2, "R");
			rset2=stmt2.executeQuery();
			if(rset2.next())
			{
				customerAddress=rset2.getString(1)==null?"":rset2.getString(1);
				customerCity=rset2.getString(2)==null?"":rset2.getString(2);
				customerPin=rset2.getString(3)==null?"":rset2.getString(3);
				customerState=rset2.getString(4)==null?"":rset2.getString(4);
				customerZone=rset2.getString(5)==null?"":rset2.getString(5);
				customerCountry=rset2.getString(6)==null?"":rset2.getString(6);
				customerPhone=rset2.getString(7)==null?"":rset2.getString(7);
				customerEmail=rset2.getString(8)==null?"":rset2.getString(8);
			}
			rset2.close();
			stmt2.close();
			
			queryString="SELECT DISTINCT CONTACT_PERSON, PHONE, FAX_1 "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=?"
					+ "AND ENTITY=? AND NOM_FLAG=? AND ADDR_FLAG=? "
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.TYPE=B.TYPE "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, "C");
			stmt.setString(4, "Y");
			stmt.setString(5, "R");
			stmt.setString(6, "Y");
			stmt.setString(7, "Y");
			stmt.setString(8, "RLNG");
			rset=stmt.executeQuery();
			if(rset.next())
			{
				contact_person_nm=rset.getString(1)==null?"":rset.getString(1);
				contact_person_phone=rset.getString(2)==null?"":rset.getString(2);
				contact_person_fax=rset.getString(3)==null?"":rset.getString(3);
			}
			rset.close();
			stmt.close();
			
			VIEW_LTCORA_CUSTOMER_INFO.put("customer_comp_abbr",customer_comp_abbr);
			VIEW_LTCORA_CUSTOMER_INFO.put("contact_person_fax",contact_person_fax);
			VIEW_LTCORA_CUSTOMER_INFO.put("contact_person_phone",contact_person_phone);
			VIEW_LTCORA_CUSTOMER_INFO.put("customer_comp_nm",customer_comp_nm);
			VIEW_LTCORA_CUSTOMER_INFO.put("contact_person_nm",contact_person_nm);
			VIEW_LTCORA_CUSTOMER_INFO.put("customerAddress",customerAddress);
			VIEW_LTCORA_CUSTOMER_INFO.put("customerCity",customerCity);
			VIEW_LTCORA_CUSTOMER_INFO.put("customerPin",customerPin);
			VIEW_LTCORA_CUSTOMER_INFO.put("customerState",customerState);
			VIEW_LTCORA_CUSTOMER_INFO.put("customerZone",customerZone);
			VIEW_LTCORA_CUSTOMER_INFO.put("customerCountry",customerCountry);
			VIEW_LTCORA_CUSTOMER_INFO.put("customerPhone",customerPhone);
			VIEW_LTCORA_CUSTOMER_INFO.put("customerEmail",customerEmail);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//Storage report
	public void getLtcoraStorageDtls()
	{
		String function_nm="getLtcoraStorageDtls()";
		try
		{
			String query="SELECT CARGO_NO,STORAGE_DAYS,TO_CHAR(ACTUAL_RECPT_DT,'DD/MM/YYYY'), "
					+ "TO_CHAR((ACTUAL_RECPT_DT+STORAGE_DAYS-1),'DD/MM/YYYY') STORAGE_DURATION_END_DATE, "
					+ "STORAGE_EXT_DAYS, CASE WHEN STORAGE_EXT_DAYS IS NOT NULL THEN TO_CHAR((ACTUAL_RECPT_DT+STORAGE_DAYS-1+STORAGE_EXT_DAYS),'DD/MM/YYYY')  "
					+ "ELSE TO_CHAR((ACTUAL_RECPT_DT+STORAGE_DAYS-1),'DD/MM/YYYY') END EXT_STORAGE_END_DT "
					+ "FROM FMS_LTCORA_CONT_CARGO_DTL A WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND BUY_SALE=? AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO=? "
					+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.AGMT_TYPE=B.AGMT_TYPE AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO) "
					+ "AND CONTRACT_TYPE=? "
					+ "ORDER BY CARGO_NO ";
			stmt=conn.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, buy_sell);
			stmt.setString(4, agmt_type);
			stmt.setString(5, agmt_no);
			stmt.setString(6, agmt_rev);
			stmt.setString(7, cont_no);
			//stmt.setString(8, cont_rev);
			stmt.setString(8, cont_type);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String cargo_no = rset.getString(1)==null?"":rset.getString(1);
				String storage_days=rset.getString(2)==null?"":rset.getString(2);
				String storage_start_dt=rset.getString(3)==null?"":rset.getString(3);
				String storage_end_dt=rset.getString(4)==null?"":rset.getString(4);
				String storage_ext_days=rset.getString(5)==null?"0":rset.getString(5);
				String storage_ext_end_dt=rset.getString(6)==null?"":rset.getString(6);

				String disp_deal_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, cont_type, cargo_no);
				
				VDEAL_MAP.add(disp_deal_map);
				VSTORAGE_DAYS.add(storage_days);
				VSTORAGE_START_DT.add(storage_start_dt);
				VSTORAGE_END_DT.add(storage_end_dt);
				VSTORAGE_EXT_DAYS.add(storage_ext_days);
				VSTORAGE_EXT_END_DT.add(storage_ext_end_dt);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getLtcoraContTraderCounterpartyList()
	{
		String function_nm="getLtcoraContTraderCounterpartyList()";
		try
		{
			int selCount=0;
			String queryString="SELECT DISTINCT(COUNTERPARTY_CD) "
					+ "FROM FMS_LTCORA_CONT_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
					+ "AND BUY_SALE=? "; 
			stmt = conn.prepareStatement(queryString);
			stmt.setString(++selCount, comp_cd);
			stmt.setString(++selCount, buy_sell);		//for sales LTCORA cargo
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String countpty_cd=rset.getString(1)==null?"":rset.getString(1);
				
				VMST_COUNTERPARTY_CD.add(countpty_cd);
				VMST_COUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
				VMST_COUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
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
			String queryString1="SELECT DISTINCT COMPANY_CD, COUNTERPARTY_CD "
					+ "FROM FMS_TRADER_CONT_MST "
					+ "WHERE COMPANY_CD=? ";
			queryString1+="UNION "
					+ "SELECT DISTINCT COMPANY_CD, COUNTERPARTY_CD "
					+ "FROM FMS_TRADER_CARGO_MST "
					+ "WHERE COMPANY_CD=? ";
			queryString1+="UNION "
					+ "SELECT DISTINCT COMPANY_CD, COUNTERPARTY_CD  "
					+ "FROM FMS_LTCORA_CONT_CARGO_DTL  "
					+ "WHERE COMPANY_CD=? ";
			queryString1+="UNION "
					+ "SELECT DISTINCT COMPANY_CD, COUNTERPARTY_CD  "
					+ "FROM FMS_SUPPLY_CONT_MST  "
					+ "WHERE COMPANY_CD=? ";
			stmt=conn.prepareStatement(queryString1);
			stmt.setString(1, comp_cd);
			stmt.setString(2, comp_cd);
			stmt.setString(3, comp_cd);
			stmt.setString(4, comp_cd);
			rset=stmt.executeQuery();
			
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
	//till here 
	
	public void getIND_GAS_POC_Dtl()
	{
		String function_nm = "getIND_GAS_POC_Dtl()";
		
		try 
		{
			String buy_sale = "";
			String buy_sale_val = "";

			Vector<Double> VVOL = new Vector<>();
			Vector VTEMP_TRD_CD = new Vector();
			Vector VTEMP_PLANT_SEQ = new Vector();
			Vector VTEMP_PLANT_ABBR = new Vector();
			Vector VTEMP_SPLIT_VALUE = new Vector();

			Vector VALLOC_MONTH_YEAR = new Vector();

			int selCnt = 0;
			
			String queryString = "SELECT B.COMPANY_CD AS CO_CODE, A.COMPANY_ABBR AS CO_ABBR,A.COMPANY_NM AS CO_NM, "
					+ "Z.COUNTERPARTY_NM AS COUNTERPARTY_NM,Z.COUNTERPARTY_ABBR AS COUNTERPARTY_ABBR, Z.CATEGORY  AS COUNTERPARTY_CATEGORY, "
					+ "Z.NCF_CATEGORY AS COUNTERPARTY_NCF_CATEGORY, B.AGMT_NO ,B.CONT_NO , B.AGMT_TYPE , B.AGMT_BASE , B.START_DT , B.START_DT ,  B.TCQ , "
					+ "TO_CHAR(B.START_DT,'DD/MM/YYYY') , "
					+ "B.CONTRACT_TYPE ,'T' ,B.QUANTITY_UNIT ,'PHYSICAL' ,  "
					+ "Z.COUNTERPARTY_CD, B.AGMT_REV,B.CONT_REV,0, "
					+ "B.CONT_REF_NO AS DEAL_REF,  "
					+ "TO_DATE(B.START_DT,'DD/MM/YYYY') AS TRADE_DT "
					+ "FROM FMS_TRADER_CONT_MST B,FMS_COMPANY_OWNER_MST A,FMS_COUNTERPARTY_MST Z "
					+ "WHERE B.COMPANY_CD = ?  "
					+ "AND B.CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST C WHERE B.COMPANY_CD=C.COMPANY_CD AND B.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND B.CONT_NO=C.CONT_NO AND B.AGMT_NO=C.AGMT_NO AND B.AGMT_REV=C.AGMT_REV AND B.CONTRACT_TYPE=C.CONTRACT_TYPE)  "
					+ "AND B.END_DT >= TO_DATE(?, 'DD/MM/YYYY') "
					+ "AND B.START_DT <= TO_DATE(?, 'DD/MM/YYYY') "
					+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.EFF_DT = (SELECT MAX(D.EFF_DT)  "
					+ "FROM FMS_COMPANY_OWNER_MST D  "
					+ "WHERE D.COMPANY_CD = A.COMPANY_CD  "
					+ "AND D.EFF_DT <= TO_DATE(TO_CHAR(SYSDATE, 'DD/MM/YYYY'), 'DD/MM/YYYY')) "// --AND ROWNUM = 1 "
					//+ "AND  Z.COMPANY_CD = B.COMPANY_CD  "
					+ "AND Z.COUNTERPARTY_CD = B.COUNTERPARTY_CD "
					+ "AND Z.EFF_DT = (SELECT MAX(D.EFF_DT)  "
					+ "FROM FMS_COUNTERPARTY_MST D  "
					+ "WHERE D.COUNTERPARTY_CD = Z.COUNTERPARTY_CD  "
					//+ "AND D.COMPANY_CD = Z.COMPANY_CD  "
					+ "AND D.EFF_DT <= TO_DATE(TO_CHAR(SYSDATE, 'DD/MM/YYYY'), 'DD/MM/YYYY')) ";// --AND ROWNUM = 1";
			if (!counterparty_cd.equals("0")) 
			{
				queryString+= "AND B.COUNTERPARTY_CD = ? ";
			}
			
			//HM20250729 : Removed LTCORA Impact from the NCF Report as per the feedback Mail from Vijay S.
			/*queryString+= "UNION ALL  "
					+ "SELECT B.COMPANY_CD AS CO_CODE, A.COMPANY_ABBR AS CO_ABBR, A.COMPANY_NM AS CO_NM,  "
					+ "Z.COUNTERPARTY_NM AS COUNTERPARTY_NM,Z.COUNTERPARTY_ABBR AS COUNTERPARTY_ABBR,Z.CATEGORY AS COUNTERPARTY_CATEGORY,  "
					+ "Z.NCF_CATEGORY AS COUNTERPARTY_NCF_CATEGORY, B.AGMT_NO ,B.CONT_NO , B.AGMT_TYPE , 'X' , B.ACTUAL_RECPT_DT , B.ACTUAL_RECPT_DT , 0,  "
					+ "TO_CHAR(B.ACTUAL_RECPT_DT,'DD/MM/YYYY') ,B.CONTRACT_TYPE , B.BUY_SALE , 1 ,'PHYSICAL' ,  "
					+ "Z.COUNTERPARTY_CD, B.AGMT_REV,B.CONT_REV,B.CARGO_NO,  "
					+ "(SELECT A.CONT_REF_NO  "
					+ "FROM FMS_LTCORA_CONT_MST A  "
					+ "WHERE A.COMPANY_CD = B.COMPANY_CD  "
					+ "AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD  "
					+ "AND A.BUY_SALE = B.BUY_SALE  "
					+ "AND A.AGMT_TYPE = B.AGMT_TYPE  "
					+ "AND A.AGMT_NO = B.AGMT_NO  "
					+ "AND A.CONTRACT_TYPE = B.CONTRACT_TYPE  "
					+ "AND A.CONT_NO = B.CONT_NO  "
					+ "AND ROWNUM = 1) AS DEAL_REF,  "
					+ "TO_DATE(B.ACTUAL_RECPT_DT,'DD/MM/YYYY') AS TRADE_DT "
					+ "FROM FMS_LTCORA_CONT_CARGO_DTL B,FMS_COMPANY_OWNER_MST A,FMS_COUNTERPARTY_MST Z  "
					+ "WHERE B.COMPANY_CD = ? "
					+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL C WHERE B.COMPANY_CD=C.COMPANY_CD AND B.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND B.CONT_NO=C.CONT_NO AND B.AGMT_NO=C.AGMT_NO AND B.AGMT_REV=C.AGMT_REV AND B.CONTRACT_TYPE=C.CONTRACT_TYPE)  "
					+ "AND (ACTUAL_RECPT_DT + COALESCE(STORAGE_EXT_DAYS, 0) + COALESCE(STORAGE_DAYS-1, 0)) >= TO_DATE(?, 'DD/MM/YYYY') "
					//+ "AND B.ACTUAL_RECPT_DT <= TO_DATE(?, 'DD/MM/YYYY')    "
					+ "AND B.ACTUAL_RECPT_DT <= TO_DATE(?, 'DD/MM/YYYY') "
					+ "AND A.COMPANY_CD = B.COMPANY_CD  "
					+ "AND A.EFF_DT = (SELECT MAX(D.EFF_DT)  "
					+ "FROM FMS_COMPANY_OWNER_MST D  "
					+ "WHERE D.COMPANY_CD = A.COMPANY_CD  "
					+ "AND D.EFF_DT <= TO_DATE(TO_CHAR(SYSDATE, 'DD/MM/YYYY'), 'DD/MM/YYYY')) "
					//+ "AND  Z.COMPANY_CD = B.COMPANY_CD "
					+ "AND Z.COUNTERPARTY_CD = B.COUNTERPARTY_CD  "
					+ "AND Z.EFF_DT = (SELECT MAX(D.EFF_DT)   "
					+ "FROM FMS_COUNTERPARTY_MST D   "
					+ "WHERE D.COUNTERPARTY_CD = Z.COUNTERPARTY_CD   "
					//+ "AND D.COMPANY_CD = Z.COMPANY_CD  "
					+ "AND D.EFF_DT <= TO_DATE(TO_CHAR(SYSDATE, 'DD/MM/YYYY'), 'DD/MM/YYYY')) ";
			if (!counterparty_cd.equals("0")) 
			{
				queryString+= "AND B.COUNTERPARTY_CD = ? ";
			}*/
	       	queryString+= "UNION ALL  "
					+ "SELECT B.COMPANY_CD AS CO_CODE, A.COMPANY_ABBR AS CO_ABBR, A.COMPANY_NM AS CO_NM,  "
					+ "Z.COUNTERPARTY_NM AS COUNTERPARTY_NM,Z.COUNTERPARTY_ABBR AS COUNTERPARTY_ABBR,Z.CATEGORY AS COUNTERPARTY_CATEGORY, "
					+ "Z.NCF_CATEGORY AS COUNTERPARTY_NCF_CATEGORY,  "
					+ "B.AGMT_NO ,B.CONT_NO , B.AGMT_TYPE , 'X' , B.START_DT , B.START_DT ,  B.CARGO_QTY  , "
					+ "TO_CHAR(B.START_DT,'DD/MM/YYYY') , " 
					+ "B.CONTRACT_TYPE ,'T' ,1 , 'PHYSICAL' ,  "
					+ "Z.COUNTERPARTY_CD, B.AGMT_REV,B.CONT_REV,B.CARGO_NO, "
					+ "B.CARGO_REF AS DEAL_REF,  "
					+ "TO_DATE(B.START_DT,'DD/MM/YYYY') AS TRADE_DT "
					+ "FROM FMS_TRADER_CARGO_MST B,FMS_COMPANY_OWNER_MST A,FMS_COUNTERPARTY_MST Z  "
					+ "WHERE B.COMPANY_CD = ? "
					+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CARGO_MST C WHERE B.COMPANY_CD=C.COMPANY_CD AND B.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND B.CONT_NO=C.CONT_NO AND B.AGMT_NO=C.AGMT_NO AND B.AGMT_REV=C.AGMT_REV AND B.CONTRACT_TYPE=C.CONTRACT_TYPE)  "
					+ "AND B.END_DT >= TO_DATE(?, 'DD/MM/YYYY')    "
					+ "AND B.START_DT <= TO_DATE(?, 'DD/MM/YYYY') "
					+ "AND A.COMPANY_CD = B.COMPANY_CD   "
					+ "AND A.EFF_DT = (SELECT MAX(D.EFF_DT)   "
					+ "FROM FMS_COMPANY_OWNER_MST D   "
					+ "WHERE D.COMPANY_CD = A.COMPANY_CD   "
					+ "AND D.EFF_DT <= TO_DATE(TO_CHAR(SYSDATE, 'DD/MM/YYYY'), 'DD/MM/YYYY'))  "
					//+ "AND  Z.COMPANY_CD = B.COMPANY_CD  "
					+ "AND Z.COUNTERPARTY_CD = B.COUNTERPARTY_CD   "
					+ "AND Z.EFF_DT = (SELECT MAX(D.EFF_DT)    "
					+ "FROM FMS_COUNTERPARTY_MST D    "
					+ "WHERE D.COUNTERPARTY_CD = Z.COUNTERPARTY_CD    "
					//+ "AND D.COMPANY_CD = Z.COMPANY_CD   "
					+ "AND D.EFF_DT <= TO_DATE(TO_CHAR(SYSDATE, 'DD/MM/YYYY'), 'DD/MM/YYYY')) ";
	        if (!counterparty_cd.equals("0")) 
			{
				queryString+= "AND B.COUNTERPARTY_CD = ? ";
			}
	        queryString+= "UNION ALL  "
					+ "SELECT B.COMPANY_CD AS CO_CODE, A.COMPANY_ABBR AS CO_ABBR, A.COMPANY_NM AS CO_NM,   "
					+ "Z.COUNTERPARTY_NM AS COUNTERPARTY_NM,Z.COUNTERPARTY_ABBR AS COUNTERPARTY_ABBR,Z.CATEGORY AS COUNTERPARTY_CATEGORY,  "
					+ "Z.NCF_CATEGORY AS COUNTERPARTY_NCF_CATEGORY,   "
					+ "B.AGMT_NO ,B.CONT_NO , B.AGMT_TYPE , B.AGMT_BASE , B.START_DT , B.START_DT ,  B.TCQ , "
					+ "TO_CHAR(B.START_DT,'DD/MM/YYYY') ,  "
					+ "B.CONTRACT_TYPE , 'C' , 1 , 'PHYSICAL' ,  "
					+ "Z.COUNTERPARTY_CD, B.AGMT_REV,B.CONT_REV,0, "
					+ "B.CONT_REF_NO AS DEAL_REF, "
					+ "TO_DATE(B.START_DT,'DD/MM/YYYY') AS TRADE_DT "
					+ "FROM FMS_SUPPLY_CONT_MST B,FMS_COMPANY_OWNER_MST A,FMS_COUNTERPARTY_MST Z   "
					+ "WHERE B.COMPANY_CD = ? "
					+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST C WHERE B.COMPANY_CD=C.COMPANY_CD AND B.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND B.CONT_NO=C.CONT_NO AND B.AGMT_NO=C.AGMT_NO AND B.AGMT_REV=C.AGMT_REV AND B.CONTRACT_TYPE=C.CONTRACT_TYPE)  "
					+ "AND B.END_DT >= TO_DATE(?, 'DD/MM/YYYY')    "
					+ "AND B.START_DT <= TO_DATE(?, 'DD/MM/YYYY') "
					+ "AND A.COMPANY_CD = B.COMPANY_CD    "
					+ "AND A.EFF_DT = (SELECT MAX(D.EFF_DT) "
					+ "FROM FMS_COMPANY_OWNER_MST D    "
					+ "WHERE D.COMPANY_CD = A.COMPANY_CD    "
					+ "AND D.EFF_DT <= TO_DATE(TO_CHAR(SYSDATE, 'DD/MM/YYYY'), 'DD/MM/YYYY'))   "
					//+ "AND  Z.COMPANY_CD = B.COMPANY_CD   "
					+ "AND Z.COUNTERPARTY_CD = B.COUNTERPARTY_CD    "
					+ "AND Z.EFF_DT = (SELECT MAX(D.EFF_DT)     "
					+ "FROM FMS_COUNTERPARTY_MST D     "
					+ "WHERE D.COUNTERPARTY_CD = Z.COUNTERPARTY_CD     "
					//+ "AND D.COMPANY_CD = Z.COMPANY_CD "
					+ "AND D.EFF_DT <= TO_DATE(TO_CHAR(SYSDATE, 'DD/MM/YYYY'), 'DD/MM/YYYY')) ";
			if (!counterparty_cd.equals("0")) 
			{
				queryString+= "AND B.COUNTERPARTY_CD = ? ";
			}
			//queryString += "ORDER BY TRADE_DT DESC ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(++selCnt, comp_cd);
			stmt.setString(++selCnt, from_dt);
			stmt.setString(++selCnt, to_dt);
			if (!counterparty_cd.equals("0")) 
			{
				stmt.setString(++selCnt, counterparty_cd);
			}
			/*stmt.setString(++selCnt, comp_cd);
			stmt.setString(++selCnt, from_dt);
			stmt.setString(++selCnt, to_dt);
			if (!counterparty_cd.equals("0")) 
			{
				stmt.setString(++selCnt, counterparty_cd);
			}*/
			stmt.setString(++selCnt, comp_cd);
			stmt.setString(++selCnt, from_dt);
			stmt.setString(++selCnt, to_dt);
			if (!counterparty_cd.equals("0")) 
			{
				stmt.setString(++selCnt, counterparty_cd);
			}
			stmt.setString(++selCnt, comp_cd);
			stmt.setString(++selCnt, from_dt);
			stmt.setString(++selCnt, to_dt);
			if (!counterparty_cd.equals("0")) 
			{
				stmt.setString(++selCnt, counterparty_cd);
			}
			rset = stmt.executeQuery();
			while (rset.next()) 
			{
				String ncf_category = rset.getString(7) == null ? "" : rset.getString(7);
				String trade_type = rset.getString(16) == null ? "" : rset.getString(16);
				buy_sale = rset.getString(17) == null ? "" : rset.getString(17);
				
				if (buy_sale.equals("T"))
				{
					buy_sale_val = "Buy";
				}
				else 
				{
					buy_sale_val = "Sell";
				}
				
				String comp_cd = rset.getString(1) == null ? "" : rset.getString(1);
				String counterparty_cd = rset.getString(20) == null ? "" : rset.getString(20);
				String agreement_no = rset.getString(8) == null ? "" : rset.getString(8);
				String contract_no = rset.getString(9) == null ? "" : rset.getString(9);
				String cont_type= rset.getString(16) == null ? "" : rset.getString(16);
				String agmt_rev = rset.getString(21) == null ? "" : rset.getString(21);
				String cont_rev = rset.getString(22) == null ? "" : rset.getString(22);
				String cargo_no = rset.getString(23) == null ? "" : rset.getString(23);
				String entity_type = "";
				
				if(cont_type.equals("S") || cont_type.equals("L") || cont_type.equals("X") || cont_type.equals("Q") || cont_type.equals("O") || cont_type.equals("F") || cont_type.equals("E") || cont_type.equals("W") || cont_type.equals("B") || cont_type.equals("M"))
				{
					entity_type = "C";
				}
				else if(cont_type.equals("D") || cont_type.equals("T") || cont_type.equals("N") || cont_type.equals("I") || cont_type.equals("G") || cont_type.equals("P") || cont_type.equals("V"))
				{
					entity_type = "T";
				}

				String dealMapId = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agreement_no, agmt_rev, contract_no, cont_rev,cont_type, cargo_no);

				String agreement_type = rset.getString(10) == null ? "" : rset.getString(10);
				String agreement_base = rset.getString(11) == null ? "" : rset.getString(11);
				
				if (agreement_base.equals("D")) 
				{
					dealMapId = dealMapId + " <font style='background: #a6ff4d;'>[DLV]</font>";
				}

				double actual_qty = 0;
				
				String allocMonth = "";
				String allocYear = "";
				
				if ("D".equals(agreement_base))
				{
					VALLOC_MONTH_YEAR.clear();
					
					String cont_map = counterparty_cd + "-" + cont_type + "-" + agreement_no + "-%-" + contract_no + "-%";

					String queryMonthYear = "SELECT DISTINCT TO_CHAR(GAS_DT,'MM/YYYY') "
							+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? " 
							+ "AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') " 
							+ "AND SELL_CONT_MAP LIKE ? "
							+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
							+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) ";
					stmtement_1 = conn.prepareStatement(queryMonthYear);
					stmtement_1.setString(1, comp_cd);
					stmtement_1.setString(2, "C");
					stmtement_1.setString(3, to_dt);
					stmtement_1.setString(4, from_dt);
					stmtement_1.setString(5, cont_map);
					resultset_1 = stmtement_1.executeQuery();
					while (resultset_1.next()) 
					{
						String montYear = (resultset_1.getString(1));
						
						VALLOC_MONTH_YEAR.add(montYear);
					}
					stmtement_1.close();
					resultset_1.close();
					
					VALLOC_MONTH_YEAR = sortAllocationDates(VALLOC_MONTH_YEAR);
					
					if(VALLOC_MONTH_YEAR.size()>0)
					{
						for(int i=0; i<VALLOC_MONTH_YEAR.size();i++) 
						{
							String query = "SELECT SUM(EXIT_QTY_MMBTU),COUNT(*) " 
									+ "FROM FMS_DAILY_TRANSPORTER_ALLOC A "
									+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
									+ "AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') " 
									+ "AND SELL_CONT_MAP LIKE ? "
									+ "AND ALLOC_REV_NO=(SELECT MAX(ALLOC_REV_NO) FROM FMS_DAILY_TRANSPORTER_ALLOC B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.SELL_CONT_MAP=A.SELL_CONT_MAP AND A.BU_SEQ=B.BU_SEQ "
									+ "AND B.GAS_DT=A.GAS_DT AND A.ENTRY_PT_MAPPING_ID=B.ENTRY_PT_MAPPING_ID AND A.EXIT_PT_MAPPING_ID=B.EXIT_PT_MAPPING_ID) "
									+ "AND TO_CHAR(GAS_DT, 'MM/YYYY') = ?" 
									+ "AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') " ; 
							stmtement = conn.prepareStatement(query);
							stmtement.setString(1, comp_cd);
							stmtement.setString(2, "C");
							stmtement.setString(3, to_dt);
							stmtement.setString(4, from_dt);
							stmtement.setString(5, cont_map);
							stmtement.setString(6, ""+VALLOC_MONTH_YEAR.elementAt(i));
							stmtement.setString(7, to_dt);
							stmtement.setString(8, from_dt);
							resultset = stmtement.executeQuery();
							if (resultset.next()) 
							{
								actual_qty = (resultset.getDouble(1));
							}
							stmtement.close();
							resultset.close();

							VCO_CODE.add(rset.getString(1) == null ? "" : rset.getString(1));
							VCO_ABBR.add(rset.getString(2) == null ? "" : rset.getString(2));
							VCO_NM.add(rset.getString(3) == null ? "" : rset.getString(3));
							VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,counterparty_cd));
							VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,counterparty_cd));
							VCOUNTERPARTY_CATEGORY.add(rset.getString(6) == null ? "" : rset.getString(6));
							
							VDELIVERY_MONTH.add(VALLOC_MONTH_YEAR.elementAt(i).toString().split("/")[0]);
							VDELIVERY_YEAR.add(VALLOC_MONTH_YEAR.elementAt(i).toString().split("/")[1]);
							VTRADE_DT.add(rset.getString(15) == null ? "" : rset.getString(15));
							trade_type = rset.getString(16) == null ? "" : rset.getString(16);
							VTRADE_TYPE.add("Natural Gas");
							buy_sale = rset.getString(17) == null ? "" : rset.getString(17);
							
							if (buy_sale.equals("T"))
							{
								VBUY_SALE.add("Buy");
								buy_sale_val = "Buy";
							}
							else
							{
								VBUY_SALE.add("Sell");
								buy_sale_val = "Sell";
							}
							VUNIT_OF_MEASURE.add(rset.getString(18) == null ? "" : rset.getString(18));
							VINSTRUMENT_INDICATOR.add(rset.getString(19) == null ? "" : rset.getString(19));
							VDEAL_REF.add(rset.getString(24) == null ? "" : rset.getString(24));

							VNCF_CATEGORY.add(ncf_category);
							VDEAL_NUMBER.add(dealMapId);

							VVOLUME.add(nf.format(actual_qty));
							VVOL.add(actual_qty);
						}
					}
					else
					{
						if(cont_type.equals("F") || cont_type.equals("E") || cont_type.equals("W"))
						{
							String queryMonthYear1 = "SELECT DISTINCT TO_CHAR(GAS_DT,'MM/YYYY') "  
									+ "FROM FMS_DLNG_ALLOC_MST A "
									+ "WHERE CONT_NO=? AND AGMT_NO=? " 
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND CONTRACT_TYPE=? "
									+ "AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') " 
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									//+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.GAS_DT=A.GAS_DT) ";
							stmtement_11 = conn.prepareStatement(queryMonthYear1);
						}
						else
						{
							String queryMonthYear1 = "SELECT DISTINCT TO_CHAR(GAS_DT,'MM/YYYY') "  
									+ "FROM FMS_DAILY_ALLOCATION_DTL A "
									+ "WHERE CONT_NO=? AND AGMT_NO=? " 
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND CONTRACT_TYPE=? "
									+ "AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') " 
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
							stmtement_11 = conn.prepareStatement(queryMonthYear1);
						}
						stmtement_11.setString(1, contract_no);
						stmtement_11.setString(2, agreement_no);
						stmtement_11.setString(3, comp_cd);
						stmtement_11.setString(4, counterparty_cd);
						stmtement_11.setString(5, trade_type);
						stmtement_11.setString(6, to_dt);
						stmtement_11.setString(7, from_dt);
						resultset_11 = stmtement_11.executeQuery();
						while (resultset_11.next()) 
						{
							String montYear = (resultset_11.getString(1));
							VALLOC_MONTH_YEAR.add(montYear);
						}
						stmtement_11.close();
						resultset_11.close();
						
						VALLOC_MONTH_YEAR = sortAllocationDates(VALLOC_MONTH_YEAR);
						
						if(VALLOC_MONTH_YEAR.size()>0)
						{
							for(int i=0; i<VALLOC_MONTH_YEAR.size();i++) 
							{
								if(cont_type.equals("F") || cont_type.equals("E") || cont_type.equals("W"))
								{
									String query1 = "SELECT SUM(QTY_MMBTU) " 
											+ "FROM FMS_DLNG_ALLOC_MST A "
											+ "WHERE CONT_NO=? AND AGMT_NO=? " 
											+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
											+ "AND CONTRACT_TYPE=? "
											+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B "
											+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
											+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
											//+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
											+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
											+ "AND B.GAS_DT=A.GAS_DT) "
											+ "AND TO_CHAR(GAS_DT, 'MM/YYYY') = ?"
											+ "AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
											+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') " ; 
									stmtement1 = conn.prepareStatement(query1);
								}
								else
								{
									String query1 = "SELECT SUM(QTY_MMBTU) " 
											+ "FROM FMS_DAILY_ALLOCATION_DTL A "
											+ "WHERE CONT_NO=? AND AGMT_NO=? " 
											+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
											+ "AND CONTRACT_TYPE=? "
											+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
											+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
											+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
											+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
											+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
											+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) "
											+ "AND TO_CHAR(GAS_DT, 'MM/YYYY') = ?"
											+ "AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
											+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') " ; 
									stmtement1 = conn.prepareStatement(query1);
								}
								
								stmtement1.setString(1, contract_no);
								stmtement1.setString(2, agreement_no);
								stmtement1.setString(3, comp_cd);
								stmtement1.setString(4, counterparty_cd);
								stmtement1.setString(5, trade_type);
								stmtement1.setString(6, ""+VALLOC_MONTH_YEAR.elementAt(i));
								stmtement1.setString(7, to_dt);
								stmtement1.setString(8, from_dt);
								resultset1 = stmtement1.executeQuery();
								if (resultset1.next())
								{
									actual_qty = (resultset1.getDouble(1));
								}
								stmtement1.close();
								resultset1.close();
								
								VCO_CODE.add(rset.getString(1) == null ? "" : rset.getString(1));
								VCO_ABBR.add(rset.getString(2) == null ? "" : rset.getString(2));
								VCO_NM.add(rset.getString(3) == null ? "" : rset.getString(3));
								VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,counterparty_cd));
								VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,counterparty_cd));
								VCOUNTERPARTY_CATEGORY.add(rset.getString(6) == null ? "" : rset.getString(6));
								
								VDELIVERY_MONTH.add(VALLOC_MONTH_YEAR.elementAt(i).toString().split("/")[0]);
								VDELIVERY_YEAR.add(VALLOC_MONTH_YEAR.elementAt(i).toString().split("/")[1]);
								VTRADE_DT.add(rset.getString(15) == null ? "" : rset.getString(15));
								trade_type = rset.getString(16) == null ? "" : rset.getString(16);
								VTRADE_TYPE.add("Natural Gas");
								buy_sale = rset.getString(17) == null ? "" : rset.getString(17);
								
								if (buy_sale.equals("T"))
								{
									VBUY_SALE.add("Buy");
									buy_sale_val = "Buy";
								}
								else
								{
									VBUY_SALE.add("Sell");
									buy_sale_val = "Sell";
								}
								VUNIT_OF_MEASURE.add(rset.getString(18) == null ? "" : rset.getString(18));
								VINSTRUMENT_INDICATOR.add(rset.getString(19) == null ? "" : rset.getString(19));
								VDEAL_REF.add(rset.getString(24) == null ? "" : rset.getString(24));

								VNCF_CATEGORY.add(ncf_category);
								VDEAL_NUMBER.add(dealMapId);

								VVOLUME.add(nf.format(actual_qty));
								VVOL.add(actual_qty);
							}
						}
					}
				}
				else 
				{
					if (buy_sale_val.equals("Sell"))
					{
						VALLOC_MONTH_YEAR.clear();
						
						if(cont_type.equals("F") || cont_type.equals("E") || cont_type.equals("W"))
						{
							String queryMonthYear = "SELECT DISTINCT TO_CHAR(GAS_DT,'MM/YYYY') " 
									+ "FROM FMS_DLNG_ALLOC_MST A "
									+ "WHERE CONT_NO=? AND AGMT_NO=? " 
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND CONTRACT_TYPE=? "
									+ "AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') " 
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									//+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.GAS_DT=A.GAS_DT) ";
							stmtement1 = conn.prepareStatement(queryMonthYear);
						}
						else
						{
							String queryMonthYear = "SELECT DISTINCT TO_CHAR(GAS_DT,'MM/YYYY') " 
									+ "FROM FMS_DAILY_ALLOCATION_DTL A "
									+ "WHERE CONT_NO=? AND AGMT_NO=? " 
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND CONTRACT_TYPE=? "
									+ "AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') " 
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
							stmtement1 = conn.prepareStatement(queryMonthYear);
						}
						stmtement1.setString(1, contract_no);
						stmtement1.setString(2, agreement_no);
						stmtement1.setString(3, comp_cd);
						stmtement1.setString(4, counterparty_cd);
						stmtement1.setString(5, trade_type);
						stmtement1.setString(6, to_dt);
						stmtement1.setString(7, from_dt);
						resultset1 = stmtement1.executeQuery();
						while (resultset1.next())
						{
							String montYear = (resultset1.getString(1));
							VALLOC_MONTH_YEAR.add(montYear);
						}
						stmtement1.close();
						resultset1.close();
						
						VALLOC_MONTH_YEAR = sortAllocationDates(VALLOC_MONTH_YEAR);
						
						if(VALLOC_MONTH_YEAR.size()>0)
						{
							for(int i=0; i<VALLOC_MONTH_YEAR.size();i++) 
							{
								if(cont_type.equals("F") || cont_type.equals("E") || cont_type.equals("W"))
								{
									String query = "SELECT SUM(QTY_MMBTU) " 
											+ "FROM FMS_DLNG_ALLOC_MST A "
											+ "WHERE CONT_NO=? AND AGMT_NO=? " 
											+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
											+ "AND CONTRACT_TYPE=? "
											+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B "
											+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
											+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
											//+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
											+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
											+ "AND B.GAS_DT=A.GAS_DT)"
											+ "AND TO_CHAR(GAS_DT, 'MM/YYYY') = ?"
											+ "AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
											+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') " ; 
									stmtement = conn.prepareStatement(query);
								}
								else
								{
									String query = "SELECT SUM(QTY_MMBTU) " 
											+ "FROM FMS_DAILY_ALLOCATION_DTL A "
											+ "WHERE CONT_NO=? AND AGMT_NO=? " 
											+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
											+ "AND CONTRACT_TYPE=? "
											+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DAILY_ALLOCATION_DTL B "
											+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
											+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
											+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
											+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
											+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)"
											+ "AND TO_CHAR(GAS_DT, 'MM/YYYY') = ?"
											+ "AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
											+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') " ; 
									stmtement = conn.prepareStatement(query);
								}
								stmtement.setString(1, contract_no);
								stmtement.setString(2, agreement_no);
								stmtement.setString(3, comp_cd);
								stmtement.setString(4, counterparty_cd);
								stmtement.setString(5, trade_type);
								stmtement.setString(6, ""+VALLOC_MONTH_YEAR.elementAt(i));
								stmtement.setString(7, to_dt);
								stmtement.setString(8, from_dt);
								resultset = stmtement.executeQuery();
								if (resultset.next())
								{
									actual_qty = (resultset.getDouble(1));
								}
								stmtement.close();
								resultset.close();
								
								VCO_CODE.add(rset.getString(1) == null ? "" : rset.getString(1));
								VCO_ABBR.add(rset.getString(2) == null ? "" : rset.getString(2));
								VCO_NM.add(rset.getString(3) == null ? "" : rset.getString(3));
								VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,counterparty_cd));
								VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,counterparty_cd));
								VCOUNTERPARTY_CATEGORY.add(rset.getString(6) == null ? "" : rset.getString(6));
								
								VDELIVERY_MONTH.add(VALLOC_MONTH_YEAR.elementAt(i).toString().split("/")[0]);
								VDELIVERY_YEAR.add(VALLOC_MONTH_YEAR.elementAt(i).toString().split("/")[1]);
								VTRADE_DT.add(rset.getString(15) == null ? "" : rset.getString(15));
								trade_type = rset.getString(16) == null ? "" : rset.getString(16);
								VTRADE_TYPE.add("Natural Gas");
								buy_sale = rset.getString(17) == null ? "" : rset.getString(17);
								
								if (buy_sale.equals("T"))
								{
									VBUY_SALE.add("Buy");
									buy_sale_val = "Buy";
								}
								else
								{
									VBUY_SALE.add("Sell");
									buy_sale_val = "Sell";
								}
								VUNIT_OF_MEASURE.add(rset.getString(18) == null ? "" : rset.getString(18));
								VINSTRUMENT_INDICATOR.add(rset.getString(19) == null ? "" : rset.getString(19));
								VDEAL_REF.add(rset.getString(24) == null ? "" : rset.getString(24));

								VNCF_CATEGORY.add(ncf_category);
								VDEAL_NUMBER.add(dealMapId);

								VVOLUME.add(nf.format(actual_qty));
								VVOL.add(actual_qty);
							}
						}
					} 
					else
					{
						if (cont_type.equals("N"))
						{
							VALLOC_MONTH_YEAR.clear();
							
							String queryMonthYear = "SELECT DISTINCT TO_CHAR(QQ_DT,'MM/YYYY') " 
									+ "FROM FMS_BUY_CARGO_ALLOC A "
									+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_TYPE=? "
									+ "AND A.CONT_NO=? AND A.AGMT_NO=? AND A.CONTRACT_TYPE=? AND A.CARGO_NO=? "
									+ "AND QQ_DT<=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND QQ_DT>=TO_DATE(?,'DD/MM/YYYY') " 
									+ "AND A.ALLOC_REV=(SELECT MAX(B.ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO AND B.AGMT_TYPE=A.AGMT_TYPE AND B.CARGO_NO=A.CARGO_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD AND B.CONTRACT_TYPE=A.CONTRACT_TYPE) ";
							stmtement1 = conn.prepareStatement(queryMonthYear);
							stmtement1.setString(1, comp_cd);
							stmtement1.setString(2, counterparty_cd);
							stmtement1.setString(3, "M");
							stmtement1.setString(4, contract_no);
							stmtement1.setString(5, agreement_no);
							stmtement1.setString(6, trade_type);
							stmtement1.setString(7, cargo_no);
							stmtement1.setString(8, to_dt);
							stmtement1.setString(9, from_dt);
							resultset1 = stmtement1.executeQuery();
							while (resultset1.next())
							{
								String montYear = (resultset1.getString(1) == null ? "" : resultset1.getString(1));
								VALLOC_MONTH_YEAR.add(montYear);
							}
							stmtement1.close();
							resultset1.close();
							
							VALLOC_MONTH_YEAR = sortAllocationDates(VALLOC_MONTH_YEAR);
							
							if(VALLOC_MONTH_YEAR.size()>0)
							{
								for(int i=0; i<VALLOC_MONTH_YEAR.size();i++) 
								{
									if(!VALLOC_MONTH_YEAR.elementAt(i).equals("")) 
									{
										String queryString1 = "SELECT SUM(A.QQ_QTY_MMBTU) " 
												+ "FROM FMS_BUY_CARGO_ALLOC A "
												+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_TYPE=? "
												+ "AND A.CONT_NO=? AND A.AGMT_NO=? AND A.CONTRACT_TYPE=? AND A.CARGO_NO=? "
												+ "AND A.ALLOC_REV=(SELECT MAX(B.ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC B "
												+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO AND B.AGMT_TYPE=A.AGMT_TYPE AND B.CARGO_NO=A.CARGO_NO "
												+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD AND B.CONTRACT_TYPE=A.CONTRACT_TYPE) "
												+ "AND TO_CHAR(QQ_DT, 'MM/YYYY') = ?"; 
										stmt1 = conn.prepareStatement(queryString1);
										stmt1.setString(1, comp_cd);
										stmt1.setString(2, counterparty_cd);
										stmt1.setString(3, "M");
										stmt1.setString(4, contract_no);
										stmt1.setString(5, agreement_no);
										stmt1.setString(6, trade_type);
										stmt1.setString(7, cargo_no);
										stmt1.setString(8, ""+VALLOC_MONTH_YEAR.elementAt(i));
										rset1 = stmt1.executeQuery();
										if (rset1.next())
										{
											actual_qty = rset1.getDouble(1);
										}
										rset1.close();
										stmt1.close();
										
										VCO_CODE.add(rset.getString(1) == null ? "" : rset.getString(1));
										VCO_ABBR.add(rset.getString(2) == null ? "" : rset.getString(2));
										VCO_NM.add(rset.getString(3) == null ? "" : rset.getString(3));
										VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,counterparty_cd));
										VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,counterparty_cd));
										VCOUNTERPARTY_CATEGORY.add(rset.getString(6) == null ? "" : rset.getString(6));
										
										VDELIVERY_MONTH.add(VALLOC_MONTH_YEAR.elementAt(i).toString().split("/")[0]);
										VDELIVERY_YEAR.add(VALLOC_MONTH_YEAR.elementAt(i).toString().split("/")[1]);
										VTRADE_DT.add(rset.getString(15) == null ? "" : rset.getString(15));
										trade_type = rset.getString(16) == null ? "" : rset.getString(16);
										VTRADE_TYPE.add("Natural Gas");
										buy_sale = rset.getString(17) == null ? "" : rset.getString(17);
										
										if (buy_sale.equals("T"))
										{
											VBUY_SALE.add("Buy");
											buy_sale_val = "Buy";
										}
										else
										{
											VBUY_SALE.add("Sell");
											buy_sale_val = "Sell";
										}
										VUNIT_OF_MEASURE.add(rset.getString(18) == null ? "" : rset.getString(18));
										VINSTRUMENT_INDICATOR.add(rset.getString(19) == null ? "" : rset.getString(19));
										VDEAL_REF.add(rset.getString(24) == null ? "" : rset.getString(24));

										VNCF_CATEGORY.add(ncf_category);
										VDEAL_NUMBER.add(dealMapId);

										VVOLUME.add(nf.format(actual_qty));
										VVOL.add(actual_qty);
									}
								}
							}
						} 
						else
						{
							VALLOC_MONTH_YEAR.clear();
							
							if ("".equals(cargo_no)) 
							{
								cargo_no = "0";
							}

							String queryMonthYear = "SELECT DISTINCT TO_CHAR(GAS_DT,'MM/YYYY') " 
									+ "FROM FMS_BUY_DAILY_ALLOCATION A "
									+ "WHERE CONT_NO=? AND AGMT_NO=? " 
									+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND CONTRACT_TYPE=? AND CARGO_NO=? "
									+ "AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
									+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') " 
									+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
							stmtement1 = conn.prepareStatement(queryMonthYear);
							stmtement1.setString(1, contract_no);
							stmtement1.setString(2, agreement_no);
							stmtement1.setString(3, comp_cd);
							stmtement1.setString(4, counterparty_cd);
							stmtement1.setString(5, trade_type);
							stmtement1.setString(6, cargo_no);
							stmtement1.setString(7, to_dt);
							stmtement1.setString(8, from_dt);
							resultset1 = stmtement1.executeQuery();
							while (resultset1.next())
							{
								String montYear = (resultset1.getString(1));
								VALLOC_MONTH_YEAR.add(montYear);
							}
							stmtement1.close();
							resultset1.close();

							VALLOC_MONTH_YEAR = sortAllocationDates(VALLOC_MONTH_YEAR);
							 
							if(VALLOC_MONTH_YEAR.size()>0) 
							{
								for(int i=0; i<VALLOC_MONTH_YEAR.size();i++) 
								{
									String query = "SELECT SUM(QTY_MMBTU) " 
											+ "FROM FMS_BUY_DAILY_ALLOCATION A "
											+ "WHERE CONT_NO=? AND AGMT_NO=? " 
											+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
											+ "AND CONTRACT_TYPE=? AND CARGO_NO=? "
											+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_BUY_DAILY_ALLOCATION B "
											+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
											+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
											+ "AND B.TRANSPORTER_CD=A.TRANSPORTER_CD AND B.TRANS_SEQ=A.TRANS_SEQ "
											+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
											+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) "
											+ "AND TO_CHAR(GAS_DT, 'MM/YYYY') = ?"
											+ "AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
											+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') " ; 
									stmtement = conn.prepareStatement(query);
									stmtement.setString(1, contract_no);
									stmtement.setString(2, agreement_no);
									stmtement.setString(3, comp_cd);
									stmtement.setString(4, counterparty_cd);
									stmtement.setString(5, trade_type);
									stmtement.setString(6, cargo_no);
									stmtement.setString(7, ""+VALLOC_MONTH_YEAR.elementAt(i));
									stmtement.setString(8, to_dt);
									stmtement.setString(9, from_dt);
									resultset = stmtement.executeQuery();
									if (resultset.next())
									{
										actual_qty = (resultset.getDouble(1));
									}
									stmtement.close();
									resultset.close();
									
									String split_flag = "";
									
									if ("D".equals(cont_type) || "I".equals(cont_type)) 
									{
										String query_string = "SELECT A.SPLIT_FLAG " 
												+ "FROM FMS_TRADER_CONT_MST A "
												+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? "
												+ "AND A.AGMT_NO=? AND A.CONTRACT_TYPE=? "
												+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
												+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
												+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
										prep_stmt = conn.prepareStatement(query_string);
										prep_stmt.setString(1, comp_cd);
										prep_stmt.setString(2, counterparty_cd);
										prep_stmt.setString(3, contract_no);
										prep_stmt.setString(4, agreement_no);
										prep_stmt.setString(5, cont_type);
										temp_rset = prep_stmt.executeQuery();
										if (temp_rset.next())
										{
											split_flag = temp_rset.getString(1) == null ? "" : temp_rset.getString(1);

											String queryString1 = "SELECT PLANT_SEQ_NO,SPLIT_VALUE "
													+ "FROM FMS_TRADER_CONT_PLANT A "
													+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
													+ "AND AGMT_NO=? AND CONTRACT_TYPE=? "
													+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_PLANT B WHERE A.COMPANY_CD=B.COMPANY_CD "
													+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
													+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
											stmt1 = conn.prepareStatement(queryString1);
											stmt1.setString(1, comp_cd);
											stmt1.setString(2, counterparty_cd);
											stmt1.setString(3, contract_no);
											stmt1.setString(4, agreement_no);
											stmt1.setString(5, cont_type);
											rset1 = stmt1.executeQuery();
											while (rset1.next()) 
											{
												String plant_seq = rset1.getString(1) == null ? "" : rset1.getString(1);
												String split_value = rset1.getString(2) == null ? "" : rset1.getString(2);
												String plant_abbr = utilBean.getCounterpartyPlantABBR(conn,counterparty_cd, comp_cd, plant_seq,"T");

												VTEMP_TRD_CD.add(counterparty_cd);
												VTEMP_PLANT_SEQ.add(plant_seq);
												VTEMP_PLANT_ABBR.add(plant_abbr);
												VTEMP_SPLIT_VALUE.add(split_value);
												
												if (split_flag.equals("Y")) 
												{
													VCO_CODE.add(rset.getString(1) == null ? "" : rset.getString(1));
													VCO_ABBR.add(rset.getString(2) == null ? "" : rset.getString(2));
													VCO_NM.add(rset.getString(3) == null ? "" : rset.getString(3));
													VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,counterparty_cd));
													VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,counterparty_cd));
													VCOUNTERPARTY_CATEGORY.add(rset.getString(6) == null ? "" : rset.getString(6));
													
													VDELIVERY_MONTH.add(VALLOC_MONTH_YEAR.elementAt(i).toString().split("/")[0]);
													VDELIVERY_YEAR.add(VALLOC_MONTH_YEAR.elementAt(i).toString().split("/")[1]);
													VTRADE_DT.add(rset.getString(15) == null ? "" : rset.getString(15));
													trade_type = rset.getString(16) == null ? "" : rset.getString(16);
													VTRADE_TYPE.add("Natural Gas");
													buy_sale = rset.getString(17) == null ? "" : rset.getString(17);
													
													if (buy_sale.equals("T"))
													{
														VBUY_SALE.add("Buy");
														buy_sale_val = "Buy";
													}
													else
													{
														VBUY_SALE.add("Sell");
														buy_sale_val = "Sell";
													}
													VUNIT_OF_MEASURE.add(rset.getString(18) == null ? "" : rset.getString(18));
													VINSTRUMENT_INDICATOR.add(rset.getString(19) == null ? "" : rset.getString(19));
													VDEAL_REF.add(rset.getString(24) == null ? "" : rset.getString(24));

													VNCF_CATEGORY.add(ncf_category);
													
													VDEAL_NUMBER.add(dealMapId + " <font style=\"background:#ff99ff;\">[Split "+ split_value + "%]</font>");
													double persented_qty = (actual_qty * (Double.parseDouble("" + split_value) / 100));

													VVOLUME.add(nf.format(persented_qty));
													VVOL.add(persented_qty);
												}
											}
											rset1.close();
											stmt1.close();

											if (split_flag.equals("Y"))
											{
												String queryString2 = "SELECT SPLIT_TRADER_CD,PLANT_SEQ_NO,SPLIT_VALUE "
														+ "FROM FMS_TRADER_CONT_SPLIT_PLANT A "
														+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
														+ "AND AGMT_NO=? AND CONTRACT_TYPE=? "
														+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_SPLIT_PLANT B WHERE A.COMPANY_CD=B.COMPANY_CD "
														+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
														+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
												stmt_1 = conn.prepareStatement(queryString2);
												stmt_1.setString(1, comp_cd);
												stmt_1.setString(2, counterparty_cd);
												stmt_1.setString(3, contract_no);
												stmt_1.setString(4, agreement_no);
												stmt_1.setString(5, cont_type);
												rset_1 = stmt_1.executeQuery();
												while (rset_1.next())
												{
													String split_trd_cd = rset_1.getString(1) == null ? "" : rset_1.getString(1);
													String plant_seq = rset_1.getString(2) == null ? "" : rset_1.getString(2);
													String split_value = rset_1.getString(3) == null ? "" : rset_1.getString(3);
													String plant_abbr = utilBean.getCounterpartyPlantABBR(conn,split_trd_cd, comp_cd, plant_seq,"T");

													VTEMP_TRD_CD.add(split_trd_cd);
													VTEMP_PLANT_SEQ.add(plant_seq);
													VTEMP_PLANT_ABBR.add(plant_abbr);
													VTEMP_SPLIT_VALUE.add(split_value);

													VCO_CODE.add(rset.getString(1) == null ? "" : rset.getString(1));
													VCO_ABBR.add(rset.getString(2) == null ? "" : rset.getString(2));
													VCO_NM.add(rset.getString(3) == null ? "" : rset.getString(3));
													VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,split_trd_cd));
													VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,split_trd_cd));
													VCOUNTERPARTY_CATEGORY.add(rset.getString(6) == null ? "" : rset.getString(6));
													
													VDELIVERY_MONTH.add(VALLOC_MONTH_YEAR.elementAt(i).toString().split("/")[0]);
													VDELIVERY_YEAR.add(VALLOC_MONTH_YEAR.elementAt(i).toString().split("/")[1]);
													VTRADE_DT.add(rset.getString(15) == null ? "" : rset.getString(15));
													trade_type = rset.getString(16) == null ? "" : rset.getString(16);
													VTRADE_TYPE.add("Natural Gas");
													buy_sale = rset.getString(17) == null ? "" : rset.getString(17);
													
													if (buy_sale.equals("T"))
													{
														VBUY_SALE.add("Buy");
														buy_sale_val = "Buy";
													}
													else
													{
														VBUY_SALE.add("Sell");
														buy_sale_val = "Sell";
													}
													VUNIT_OF_MEASURE.add(rset.getString(18) == null ? "" : rset.getString(18));
													VINSTRUMENT_INDICATOR.add(rset.getString(19) == null ? "" : rset.getString(19));
													VDEAL_REF.add(rset.getString(24) == null ? "" : rset.getString(24));

													String split_ncf_category = "";

													String querister = "(SELECT A.NCF_CATEGORY " 
															+ "FROM FMS_COUNTERPARTY_MST A "
															+ "WHERE "
															//+ "A.COMPANY_CD = ? AND " 
															+ "A.COUNTERPARTY_CD = ? "
															+ "AND A.EFF_DT = (SELECT MAX(D.EFF_DT) "
															+ "FROM FMS_COUNTERPARTY_MST D "
															+ "WHERE D.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
															//+ "AND D.COMPANY_CD = A.COMPANY_CD "
															+ "AND D.EFF_DT <= TO_DATE(TO_CHAR(SYSDATE, 'DD/MM/YYYY'), 'DD/MM/YYYY')) "
															+ "AND ROWNUM = 1)";
													temp_stmt = conn.prepareStatement(querister);
													//temp_stmt.setString(1, comp_cd);
													temp_stmt.setString(1, split_trd_cd);
													temp_rset1 = temp_stmt.executeQuery();
													if (temp_rset1.next()) 
													{
														split_ncf_category = temp_rset1.getString(1) == null ? "" : temp_rset1.getString(1);
													}
													temp_rset1.close();
													temp_stmt.close();

													VNCF_CATEGORY.add(split_ncf_category);
													VDEAL_NUMBER.add(dealMapId + " <font style=\"background:#ff99ff;\">[Split " + split_value + "%]</font>");

													double persented_qty = (actual_qty * (Double.parseDouble("" + split_value) / 100));

													VVOLUME.add(nf.format(persented_qty));
													VVOL.add(persented_qty);
												}
												rset_1.close();
												stmt_1.close();
											} 
											else
											{
												VCO_CODE.add(rset.getString(1) == null ? "" : rset.getString(1));
												VCO_ABBR.add(rset.getString(2) == null ? "" : rset.getString(2));
												VCO_NM.add(rset.getString(3) == null ? "" : rset.getString(3));
												VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,counterparty_cd));
												VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,counterparty_cd));
												VCOUNTERPARTY_CATEGORY.add(rset.getString(6) == null ? "" : rset.getString(6));
												
												VDELIVERY_MONTH.add(VALLOC_MONTH_YEAR.elementAt(i).toString().split("/")[0]);
												VDELIVERY_YEAR.add(VALLOC_MONTH_YEAR.elementAt(i).toString().split("/")[1]);
												VTRADE_DT.add(rset.getString(15) == null ? "" : rset.getString(15));
												trade_type = rset.getString(16) == null ? "" : rset.getString(16);
												VTRADE_TYPE.add("Natural Gas");
												buy_sale = rset.getString(17) == null ? "" : rset.getString(17);
												
												if (buy_sale.equals("T"))
												{
													VBUY_SALE.add("Buy");
													buy_sale_val = "Buy";
												}
												else
												{
													VBUY_SALE.add("Sell");
													buy_sale_val = "Sell";
												}
												VUNIT_OF_MEASURE.add(rset.getString(18) == null ? "" : rset.getString(18));
												VINSTRUMENT_INDICATOR.add(rset.getString(19) == null ? "" : rset.getString(19));
												VDEAL_REF.add(rset.getString(24) == null ? "" : rset.getString(24));

												VNCF_CATEGORY.add(ncf_category);
												
												VVOLUME.add(nf.format(actual_qty));
												VVOL.add(actual_qty);
												VDEAL_NUMBER.add(dealMapId);
											}
										} 
										else 
										{
											VCO_CODE.add(rset.getString(1) == null ? "" : rset.getString(1));
											VCO_ABBR.add(rset.getString(2) == null ? "" : rset.getString(2));
											VCO_NM.add(rset.getString(3) == null ? "" : rset.getString(3));
											VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,counterparty_cd));
											VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,counterparty_cd));
											VCOUNTERPARTY_CATEGORY.add(rset.getString(6) == null ? "" : rset.getString(6));
											
											VDELIVERY_MONTH.add(VALLOC_MONTH_YEAR.elementAt(i).toString().split("/")[0]);
											VDELIVERY_YEAR.add(VALLOC_MONTH_YEAR.elementAt(i).toString().split("/")[1]);
											VTRADE_DT.add(rset.getString(15) == null ? "" : rset.getString(15));
											trade_type = rset.getString(16) == null ? "" : rset.getString(16);
											VTRADE_TYPE.add("Natural Gas");
											buy_sale = rset.getString(17) == null ? "" : rset.getString(17);
											
											if (buy_sale.equals("T"))
											{
												VBUY_SALE.add("Buy");
												buy_sale_val = "Buy";
											}
											else
											{
												VBUY_SALE.add("Sell");
												buy_sale_val = "Sell";
											}
											VUNIT_OF_MEASURE.add(rset.getString(18) == null ? "" : rset.getString(18));
											VINSTRUMENT_INDICATOR.add(rset.getString(19) == null ? "" : rset.getString(19));
											VDEAL_REF.add(rset.getString(24) == null ? "" : rset.getString(24));

											VNCF_CATEGORY.add(ncf_category);
											
											VVOLUME.add(nf.format(actual_qty));
											VVOL.add(actual_qty);
											VDEAL_NUMBER.add(dealMapId);
										}
										temp_rset.close();
										prep_stmt.close();
									} 
									else
									{
										VCO_CODE.add(rset.getString(1) == null ? "" : rset.getString(1));
										VCO_ABBR.add(rset.getString(2) == null ? "" : rset.getString(2));
										VCO_NM.add(rset.getString(3) == null ? "" : rset.getString(3));
										VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn,counterparty_cd));
										VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn,counterparty_cd));
										VCOUNTERPARTY_CATEGORY.add(rset.getString(6) == null ? "" : rset.getString(6));
										
										VDELIVERY_MONTH.add(VALLOC_MONTH_YEAR.elementAt(i).toString().split("/")[0]);
										VDELIVERY_YEAR.add(VALLOC_MONTH_YEAR.elementAt(i).toString().split("/")[1]);
										VTRADE_DT.add(rset.getString(15) == null ? "" : rset.getString(15));
										trade_type = rset.getString(16) == null ? "" : rset.getString(16);
										VTRADE_TYPE.add("Natural Gas");
										buy_sale = rset.getString(17) == null ? "" : rset.getString(17);
										
										if (buy_sale.equals("T"))
										{
											VBUY_SALE.add("Buy");
											buy_sale_val = "Buy";
										}
										else
										{
											VBUY_SALE.add("Sell");
											buy_sale_val = "Sell";
										}
										VUNIT_OF_MEASURE.add(rset.getString(18) == null ? "" : rset.getString(18));
										VINSTRUMENT_INDICATOR.add(rset.getString(19) == null ? "" : rset.getString(19));
										VDEAL_REF.add(rset.getString(24) == null ? "" : rset.getString(24));

										VNCF_CATEGORY.add(ncf_category);
										VDEAL_NUMBER.add(dealMapId);

										VVOLUME.add(nf.format(actual_qty));
										VVOL.add(actual_qty);
									}
								}
							}
						}
					}
				}
			}

			rset.close();
			stmt.close();

			buy_sum = 0.00;
			sell_sum = 0.00;
			
			for (int i = 0; i < VVOL.size(); i++)
			{
				double num = VVOL.elementAt(i);
			
				String action = "" + VBUY_SALE.elementAt(i);
			
				if ("Buy".equals(action))
				{
					buy_sum += num; // Add to buy_sum if action is "Buy"
				} 
				else if ("Sell".equals(action)) 
				{
					sell_sum += num; // Add to sell_sum if action is "Sell"
				}
			}
		} 
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public Vector sortAllocationDates(Vector VALLOC_MONTH_YEAR) 
	{
		String function_nm="sortAllocationDates()";
		try 
		{
			SimpleDateFormat sdf1 = new SimpleDateFormat("MM/yyyy");

			for (int i = 0; i < VALLOC_MONTH_YEAR.size(); i++)
			{
				for (int j = 0; j < VALLOC_MONTH_YEAR.size() - 1 - i; j++)
				{
					try 
					{
						Date d1 = sdf1.parse(""+VALLOC_MONTH_YEAR.get(j));
						Date d2 = sdf1.parse(""+VALLOC_MONTH_YEAR.get(j + 1));

						// Compare dates and swap if out of order
		                
						if (d1.compareTo(d2) > 0) 
						{
							String temp = ""+VALLOC_MONTH_YEAR.get(j);
							VALLOC_MONTH_YEAR.set(j, VALLOC_MONTH_YEAR.get(j + 1));
							VALLOC_MONTH_YEAR.set(j + 1, temp);
						}
		                
					}
					catch (ParseException e)
					{
						new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					}
				}
			}
		} 
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return VALLOC_MONTH_YEAR;
	}
	
	String callFlag = "";
	public void setCallFlag(String callFlag) {this.callFlag = callFlag;}
	String comp_cd = "";
	public void setComp_cd(String comp_cd) {this.comp_cd = comp_cd;}
	
	String buy_sell = "";
	public void setBuySell(String buy_sell) {this.buy_sell = buy_sell;}
	
	String agmt_type="";
	public void setAgmt_type(String agmt_type) {this.agmt_type=agmt_type;}
	
	String agmt_no="";
	public void setAgmt_no(String agmt_no) {this.agmt_no=agmt_no;}
	
	String agmt_rev="";
	public void setAgmt_rev(String agmt_rev) {this.agmt_rev=agmt_rev;}
	
	String cont_no="";
	public void setCont_no(String cont_no) {this.cont_no=cont_no;}
	
	String cont_rev="";
	public void setCont_rev(String cont_rev) {this.cont_rev=cont_rev;}
	
	String cont_type="";
	public void setCont_type(String cont_type) {this.cont_type=cont_type;}
	
	String sugValue="0";
	public void setSug(String sugValue) {this.sugValue=sugValue;}
	
	String from_dt = "";
	public void setFrom_dt(String from_dt) {this.from_dt = from_dt;}
	String to_dt = "";
	public void setTo_dt(String to_dt) {this.to_dt = to_dt;}
	String appPath = "";
	public void setAppPath(String appPath) {this.appPath = appPath;}
	
	String bu_region = "";
	String comp_abbr = "";
	
	public String getBu_Region() {return bu_region;}
	public String getComp_abbr() {return comp_abbr;}
	
	String counterparty_cd = "";
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	
	String signing_dt="";
	public String getSigning_dt() {return signing_dt;}
	
	double buy_sum = 0.00;
	double sell_sum = 0.00;
	
	public double getBuy_sum() {return buy_sum;}
	public double getSell_sum() {return sell_sum;}
	
	Vector VMST_COUNTERPARTY_CD = new Vector();
	Vector VMST_COUNTERPARTY_NM = new Vector();
	Vector VMST_COUNTERPARTY_ABBR = new Vector();
	
	
	Vector VCO_CODE = new Vector();
	Vector VCO_ABBR = new Vector();
	Vector VCO_NM = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VCOUNTERPARTY_CATEGORY = new Vector();
	Vector VDELIVERY_MONTH = new Vector();
	Vector VDELIVERY_YEAR = new Vector();
	Vector VVOLUME = new Vector();
	Vector VTRADE_DT = new Vector();
	Vector VTRADE_TYPE = new Vector();
	Vector VBUY_SALE = new Vector();
	Vector VUNIT_OF_MEASURE = new Vector();
	Vector VINSTRUMENT_INDICATOR = new Vector();
	Vector VDEAL_NUMBER = new Vector();
	Vector VDEAL_REF = new Vector();
	Vector VNCF_CATEGORY = new Vector();
	
	//for storage report 
	Vector VDEAL_MAP = new Vector();
	Vector VSTORAGE_DAYS = new Vector();
	Vector VSTORAGE_START_DT = new Vector();
	Vector VSTORAGE_END_DT = new Vector();
	Vector VSTORAGE_EXT_DAYS = new Vector();
	Vector VSTORAGE_EXT_END_DT = new Vector();
	
	//for energy statement
	Vector VCONTACT_PERSON = new Vector();
	Vector VCONTACT_PERSON_SEQ_NO = new Vector();
	Vector VQQ_NO = new Vector();
	Vector VQQ_DT = new Vector();
	Vector VSHIP_NM = new Vector();
	Vector VBOE_NO = new Vector();
	Vector VBOE_DT = new Vector();
	Vector VUNLOADED_QTY = new Vector();
	Vector VSUG_QTY = new Vector();
	Vector VREGASSIFY_QTY = new Vector();
	Vector VISFILE_EXIST = new Vector();
	
	HashMap<String, String> VIEW_LTCORA_INFO = new HashMap();
	HashMap<String, String> VIEW_LTCORA_CUSTOMER_INFO = new HashMap();
	
	public HashMap<String, String> getVIEW_LTCORA_INFO() {return VIEW_LTCORA_INFO;}
	public HashMap<String, String> getVIEW_LTCORA_CUSTOMER_INFO() {return VIEW_LTCORA_CUSTOMER_INFO;}
	
	public Vector getVMST_COUNTERPARTY_CD() {return VMST_COUNTERPARTY_CD;}
	public Vector getVMST_COUNTERPARTY_NM() {return VMST_COUNTERPARTY_NM;}
	public Vector getVMST_COUNTERPARTY_ABBR() {return VMST_COUNTERPARTY_ABBR;}
	
	public Vector getVDEAL_REF(){return VDEAL_REF;}
	public Vector getVNCF_CATEGORY(){return VNCF_CATEGORY;}
	public Vector getVDEAL_NUMBER(){return VDEAL_NUMBER;}
	public Vector getVINSTRUMENT_INDICATOR(){return VINSTRUMENT_INDICATOR;}
	public Vector getVUNIT_OF_MEASURE(){return VUNIT_OF_MEASURE;}
	public Vector getVBUY_SALE(){return VBUY_SALE;}
	public Vector getVTRADE_TYPE(){return VTRADE_TYPE;}
	public Vector getVTRADE_DT(){return VTRADE_DT;}
	public Vector getVVOLUME(){return VVOLUME;}
	public Vector getVDELIVERY_YEAR(){return VDELIVERY_YEAR;}
	public Vector getVDELIVERY_MONTH(){return VDELIVERY_MONTH;}
	public Vector getVCOUNTERPARTY_CATEGORY(){return VCOUNTERPARTY_CATEGORY;}
	public Vector getVCOUNTERPARTY_ABBR(){return VCOUNTERPARTY_ABBR;}
	public Vector getVCOUNTERPARTY_NM(){return VCOUNTERPARTY_NM;}
	public Vector getVCO_NM(){return VCO_NM;}
	public Vector getVCO_ABBR(){return VCO_ABBR;}
	public Vector getVCO_CODE(){return VCO_CODE;}

	public Vector getVDEAL_MAP(){return VDEAL_MAP;}
	public Vector getVSTORAGE_DAYS(){return VSTORAGE_DAYS;}
	public Vector getVSTORAGE_START_DT(){return VSTORAGE_START_DT;}
	public Vector getVSTORAGE_END_DT(){return VSTORAGE_END_DT;}
	public Vector getVSTORAGE_EXT_DAYS(){return VSTORAGE_EXT_DAYS;}
	public Vector getVSTORAGE_EXT_END_DT(){return VSTORAGE_EXT_END_DT;}
	
	public Vector getVCONTACT_PERSON(){return VCONTACT_PERSON;}
	public Vector getVCONTACT_PERSON_SEQ_NO(){return VCONTACT_PERSON_SEQ_NO;}
	public Vector getVQQ_NO(){return VQQ_NO;}
	public Vector getVQQ_DT(){return VQQ_DT;}
	public Vector getVSHIP_NM(){return VSHIP_NM;}
	public Vector getVBOE_NO(){return VBOE_NO;}
	public Vector getVBOE_DT(){return VBOE_DT;}
	public Vector getVUNLOADED_QTY(){return VUNLOADED_QTY;}
	public Vector getVSUG_QTY(){return VSUG_QTY;}
	public Vector getVREGASSIFY_QTY(){return VREGASSIFY_QTY;}
	public Vector getVISFILE_EXIST(){return VISFILE_EXIST;}

}
