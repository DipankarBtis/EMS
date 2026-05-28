package com.etrm.fms.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;

//Coded By          : Harsh Maheta
//Code Reviewed by	:  
//CR Date			: 21/03/2023 
//Status	  		: Developing

public class DB_Counterparty_Report 
{	
	String db_src_file_name="DB_Counterparty_Report.java";

	UtilBean utilBean = new UtilBean();
	DateUtil utilDate = new DateUtil();
	
	Connection conn; 
	PreparedStatement stmt;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt5;
	PreparedStatement stmt6;
	PreparedStatement stmt7;
	PreparedStatement stmt8;
	ResultSet rset;
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset5;
	ResultSet rset6;
	ResultSet rset7;
	ResultSet rset8;
	String queryString="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";
	String queryString6="";
	String queryString7="";
	String queryString8="";
	
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
	    			if(callFlag.equalsIgnoreCase("ENTITY_MASTER"))
	    			{
	    				getCountryMst();
	    				getStateMst();
	    				getEntityMaster();
	    				getSectorMaster();
	    				getCOLOR();
	    				getEntityrole();
	    				if(entity_role.equals("B"))
	    				{
	    					getCompanyOwnerDetail();
	    					getCounterpartyBankDetail();
	    				}
	    				else if(entity_role.equals("G"))
	    				{
	    					getCompanyExchangeDetail();
	    				}
	    				else
	    				{
	    					getCOUNTERPARTY_DTL();
	    				}
	    				getEntityAddressDetail();
	    				getCounterpartyRequesterApproverNote();
	    				getCounterpartyPlantDetail();
	    				if(entity_role.equals("R") || entity_role.equals("G") || entity_role.equals("S") || entity_role.equals("H") || entity_role.equals("V"))
	    				{
	    					getCounterpartyBuDetail();
	    				}
	    			}
	    			else if (callFlag.equalsIgnoreCase("ENTITY_EXCEL_MASTER")) 
	    			{
	    				getCountryMst();
	    				getStateMst();
	    				getEntityMaster();
	    				getSectorMaster();
	    				if(entity_role.equals("B"))
	    				{
	    					getCompanyOwnerDetail();
	    				}
	    				else if(entity_role.equals("G"))
	    				{
	    					getCompanyExchangeDetail();
	    				}
	    				else
	    				{
	    					getCounterpartyDetail();
	    				}
	    				
	    				getEntitiesAddressDetail();
	    				getCounterpartyRequesterApproverNote();
	    				getMultiCounterpartyPlantsDetails();
	    				fetchGovtStatNoList();
	    				getCounterpartyBankDetail();
	    				if(entity_role.equals("R") || entity_role.equals("G") || entity_role.equals("S") || entity_role.equals("H") || entity_role.equals("V"))
	    				{
	    					getMultiCounterpartyBuDetail();
	    				}
					}
	    		}
	    	}
		}
		catch (Exception e) {
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
	    	if(rset5 != null){try{rset5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset6 != null){try{rset6.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset7 != null){try{rset7.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset8 != null){try{rset8.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt6 != null){try{stmt6.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt7 != null){try{stmt7.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt8 != null){try{stmt8.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}
	
	public void getCounterpartyDetail()
	{
		String function_nm="getCounterpartyDetail()";

		try
		{
			int cont=0;
			queryString="SELECT TO_CHAR(A.EFF_DT,'DD/MM/YYYY'),A.COUNTERPARTY_NM,A.COUNTERPARTY_ABBR,A.PAN_NO,TO_CHAR(A.PAN_ISSUE_DT,'DD/MM/YYYY'),"
					+ "A.NOTES,A.STATUS,A.KYC,A.IGX,A.CATEGORY,A.WEB_ADDR,A.SAP_CODE "
					+ "FROM FMS_COUNTERPARTY_MST A, FMS_ENTITY_REQ_DTL B "
					+ "WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND B.COMPANY_CD=? AND B.ENTITY=? AND B.STATUS='A' "
					+ "AND  A.EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD) ";
			queryString+= "ORDER BY COUNTERPARTY_NM";
			
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++cont, comp_cd);
			stmt.setString(++cont, entity_role);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				eff_dt = rset.getString(1)==null?"":rset.getString(1);
				name = rset.getString(2)==null?"":rset.getString(2);
				abbr = rset.getString(3)==null?"":rset.getString(3);
				pan_no = rset.getString(4)==null?"":rset.getString(4);
				pan_dt = rset.getString(5)==null?"":rset.getString(5);
				notes = rset.getString(6)==null?"":rset.getString(6);
				status = rset.getString(7)==null?"Y":rset.getString(7);
				kyc_flg = rset.getString(8)==null?"N":rset.getString(8);
				igx_flg = rset.getString(9)==null?"N":rset.getString(9);
				category = rset.getString(10)==null?"":rset.getString(10);
				web_addr = rset.getString(11)==null?"":rset.getString(11);
				sap_code = rset.getString(12)==null?"":rset.getString(12);
				
				VEFF_DT.add(eff_dt);
				VPAN.add(pan_no);
				VPAN_DT.add(pan_dt);
				VREMARK.add(notes);
				VSTATUS.add(status);
				VCATEGORY.add(category);
				VWEB_ADD.add(web_addr);
				
				if(kyc_flg.equals("Y") && igx_flg.equals("Y")){
					clearance = "KYC, IGX";
				}else if(kyc_flg.equals("Y")) {
					clearance = "KYC";
				}else if(igx_flg.equals("Y")) {
					clearance = "IGX";
				}else {
					clearance = "";
				}
				
				VCLEARANCE.add(clearance);
			}
			rset.close();
			stmt.close();
			
			queryString1="SELECT ENTITY,REMARK,APRV_NOTE,REQ_BY,TO_CHAR(REQ_DT,'DD/MM/YYYY HH:MM:SS'),APRV_BY,TO_CHAR(APRV_DT,'DD/MM/YYYY HH:MM:SS') "
					+ "FROM FMS_ENTITY_REQ_DTL "
					+ "WHERE ENTITY=? AND STATUS!=? AND COMPANY_CD=? ";
			queryString1+= "ORDER BY COUNTERPARTY_CD";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, entity_role);
			stmt1.setString(2, "X");
			stmt1.setString(3, comp_cd);
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				requester_approver_note="";
				
				String rmk=rset1.getString(2)==null?"":rset1.getString(2);
				String aprv_note=rset1.getString(3)==null?"":rset1.getString(3);
				String req_by= ""+utilBean.getEmpName(conn,rset1.getString(4)==null?"0":rset1.getString(4));
				String req_dt = rset1.getString(5)==null?"":rset1.getString(5);
				String aprv_by= ""+utilBean.getEmpName(conn,rset1.getString(6)==null?"0":rset1.getString(6));
				String aprv_dt = rset1.getString(7)==null?"":rset1.getString(7);
				
				if(requester_approver_note.equals(""))
				{
					if(!aprv_note.equals(""))
					{
						requester_approver_note+=""+aprv_by+" ["+aprv_dt+"] [Approver] :: "+aprv_note;
					}
					requester_approver_note+="\n"+req_by+" ["+req_dt+"] [Requester] :: "+rmk;
				}
				else
				{
					if(!aprv_note.equals(""))
					{
						requester_approver_note+="\n"+aprv_by+" ["+aprv_dt+"] [Approver] :: "+aprv_note;
					}
					requester_approver_note+="\n"+req_by+" ["+req_dt+"] [Requester] :: "+rmk;
				}
				
				VRQAP_NOTES.add(requester_approver_note);
			}
			rset1.close();
			stmt1.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCompanyExchangeDetail()
	{
		String function_nm="getCompanyExchangeDetail()";

		try
		{
			queryString="SELECT TO_CHAR(EFF_DT,'DD/MM/YYYY'),COUNTERPARTY_NM,COUNTERPARTY_ABBR,PAN_NO,TO_CHAR(PAN_ISSUE_DT,'DD/MM/YYYY'),NOTES,STATUS,"
					+ "CATEGORY,WEB_ADDR,SAP_CODE "
					+ "FROM FMS_COMPANY_EXCHG_MST A "
					+ "WHERE COUNTERPARTY_CD=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COMPANY_EXCHG_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, counterparty_cd);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				eff_dt = rset.getString(1)==null?"":rset.getString(1);
				name = rset.getString(2)==null?"":rset.getString(2);
				abbr = rset.getString(3)==null?"":rset.getString(3);
				pan_no = rset.getString(4)==null?"":rset.getString(4);
				pan_dt = rset.getString(5)==null?"":rset.getString(5);
				notes = rset.getString(6)==null?"":rset.getString(6);
				status = rset.getString(7)==null?"Y":rset.getString(7);
				category = rset.getString(8)==null?"0":rset.getString(8);
				web_addr = rset.getString(9)==null?"":rset.getString(9);
				sap_code = rset.getString(10)==null?"":rset.getString(10);
			}
			else
			{
				status="Y";
				category="0";
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
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
				String stat_cd=rset.getString(1)==null?"0":rset.getString(1);
				VSTAT_CD.add(stat_cd);
				VSTAT_NM.add(rset.getString(2)==null?"":rset.getString(2));
				
				String stat_type = rset.getString(3)==null?"0":rset.getString(3);
				
				if(stat_type.trim().equals("S"))
				{
					stat_type = "Sales Invoice";
				}
				else if(stat_type.trim().equals("R"))
				{
					stat_type = "Re-Gas Invoice";
				}
				else if(stat_type.trim().equals("G"))
				{
					stat_type = "General Identification Number";
				}
				else if(stat_type.trim().equals("P"))
				{
					stat_type = "Purchase Invoice";
				}
				VSTAT_TYPE.add(stat_type);
				VSTAT_STATUS.add(rset.getString(4)==null?"0":rset.getString(4));
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	
	/*public void getEntityPlantBuDetail()
	{
		String function_nm="getEntityPlantBuDetail()";
	
		try
		{
			queryString="SELECT COUNTERPARTY_CD,ENTITY,SEQ_NO,TO_CHAR(EFF_DT,'DD/MM/YYYY'),PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,"
					+ "PLANT_ZONE,PLANT_CITY,PLANT_PIN "
					+ "FROM FMS_COUNTERPARTY_BU_DTL A "
					+ "WHERE ENTITY=? AND COUNTERPARTY_CD=? AND COMPANY_CD=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_BU_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, entity_role);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String plant_seq = rset.getString(3)==null?"0":rset.getString(3);
				VBU_PLANT_SEQ_NO.add(rset.getString(3)==null?"":rset.getString(3));
				VBU_PLANT_EFF_DT.add(rset.getString(4)==null?"":rset.getString(4));
				VBU_PLANT_NAME.add(rset.getString(5)==null?"":rset.getString(5));
				VBU_PLANT_ABBR.add(rset.getString(6)==null?"":rset.getString(6));
				VBU_PLANT_ADDR.add(rset.getString(7)==null?"":rset.getString(7));
				VBU_PLANT_STATE.add(rset.getString(8)==null?"":rset.getString(8));
				VBU_PLANT_ZONE.add(rset.getString(9)==null?"":rset.getString(9));
				VBU_PLANT_ZONE_NM.add(""+getZoneName(rset.getString(9)==null?"":rset.getString(9)));
				VBU_PLANT_CITY.add(rset.getString(10)==null?"":rset.getString(10));
				VBU_PLANT_PIN.add(rset.getString(11)==null?"":rset.getString(11));
				
				String tax_id="";
				queryString1 = "SELECT A.STAT_NO, B.STAT_NM "
						+ "FROM FMS_COUNTERPARTY_BU_TAX A,FMS_GOVT_STAT_TAX B "
						+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND A.COMPANY_CD=? "
						+ "AND PLANT_SEQ_NO=? AND A.STAT_CD=B.STAT_CD AND A.COMPANY_CD=B.COMPANY_CD AND A.STAT_NO IS NOT NULL";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, counterparty_cd);
				stmt1.setString(2, entity_role);
				stmt1.setString(3, comp_cd);
				stmt1.setString(4, plant_seq);
				rset1 = stmt1.executeQuery();
				while(rset1.next())
				{
					String stat_no = rset1.getString(1)==null?"":rset1.getString(1);
					String stat_nm = rset1.getString(2)==null?"":rset1.getString(2);
					
					if(tax_id.equals(""))
					{
						tax_id +=""+stat_nm+" "+stat_no;
					}
					else
					{
						tax_id +="<br>"+stat_nm+" "+stat_no;
					}
				}
				rset1.close();
				stmt1.close();
				
				VBU_TAX_ID.add(tax_id);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}*/
	
	public void getCountryMst()
	{
		String function_nm="getCountryMst()";

		try
		{
			utilBean.getCountryMaster(conn);
			VCOUNTRY_CODE= utilBean.getCOUNTRY_CODE();
			VCOUNTRY_NM = utilBean.getCOUNTRY_NM();
			VISO_CODE = utilBean.getISO_CODE();
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
			VTIN= utilBean.getTIN();
			VSTATE_CODE = utilBean.getSTATE_CODE();
			VSTATE_NM = utilBean.getSTATE_NM();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getEntityrole()
	{
		String function_nm="getEntityrole()";

		if(!entity_role.equals("0")) 
		{
			if(entity_role.equals("B")) 
			{
				entity = "Business Owner"; 
			}
			else if (entity_role.equals("C"))
			{
				entity = "Customer"; 
			}
			else if (entity_role.equals("T"))
			
			{
				entity = "Trader"; 
			}
			else if (entity_role.equals("R")){
				entity = "Trasporter"; 
			}
		}
	}
	public void getEntityMaster()
	{
		String function_nm="getEntityMaster()";

		try
		{
			if(!entity_role.equals("0"))
			{
				if(entity_role.equals("B"))
				{
					queryString = "SELECT COMPANY_CD,COMPANY_NM,COMPANY_ABBR "
							+ "FROM FMS_COMPANY_OWNER_MST A "
							+ "WHERE COMPANY_CD=? "
							+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COMPANY_OWNER_MST B WHERE A.COMPANY_CD=B.COMPANY_CD) ";
					queryString+= "ORDER BY COMPANY_CD";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					rset=stmt.executeQuery();
					while (rset.next()) 
					{
						VCOUNTERPARTY_CD.add(rset.getString(1)==null?"":rset.getString(1));
						VCOUNTERPARTY_NM.add(rset.getString(2)==null?"":rset.getString(2));
						VCOUNTERPARTY_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
					}
					rset.close();
					stmt.close();
				}
				else if(entity_role.equals("G"))
				{
					queryString1 = "SELECT COUNTERPARTY_CD,COUNTERPARTY_NM,COUNTERPARTY_ABBR "
							+ "FROM FMS_COMPANY_EXCHG_MST A "
							+ "WHERE EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COMPANY_EXCHG_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD) ";
					queryString1+= "ORDER BY COUNTERPARTY_NM";
					stmt1=conn.prepareStatement(queryString1);
					rset1=stmt1.executeQuery();
					while (rset1.next()) 
					{
						VCOUNTERPARTY_CD.add(rset1.getString(1)==null?"":rset1.getString(1));
						VCOUNTERPARTY_NM.add(rset1.getString(2)==null?"":rset1.getString(2));
						VCOUNTERPARTY_ABBR.add(rset1.getString(3)==null?"":rset1.getString(3));
					}
					rset1.close();
					stmt1.close();
				}
				else
				{
					int cont=0;
					queryString = "SELECT A.COUNTERPARTY_CD,A.COUNTERPARTY_NM,A.COUNTERPARTY_ABBR "
							+ "FROM FMS_COUNTERPARTY_MST A, FMS_ENTITY_REQ_DTL B "
							+ "WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND B.COMPANY_CD=? AND B.ENTITY=? AND B.STATUS='A' "
							+ "AND  A.EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD) ";
					queryString+= "ORDER BY COUNTERPARTY_NM";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(++cont, comp_cd);
					stmt.setString(++cont, entity_role);
					rset=stmt.executeQuery();
					while (rset.next()) 
					{
						VCOUNTERPARTY_CD.add(rset.getString(1)==null?"":rset.getString(1));
						VCOUNTERPARTY_NM.add(rset.getString(2)==null?"":rset.getString(2));
						VCOUNTERPARTY_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
					}
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
	
	public void getSectorMaster()
	{
		String function_nm="getSectorMaster()";

		try
		{
			queryString="SELECT SECTOR_CD,SECTOR_NAME "
					+ "FROM FMS_SECTOR_MST "
					+ "WHERE STATUS_FLAG=? "
					+ "ORDER BY SECTOR_NAME";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VSECTOR_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VSECTOR_NAME.add(rset.getString(2)==null?"":rset.getString(2));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCompanyOwnerDetail()
	{
		String function_nm="getCompanyOwnerDetail()";

		try
		{
			queryString="SELECT TO_CHAR(EFF_DT,'DD/MM/YYYY'),COMPANY_NM,COMPANY_ABBR,PAN_NO,TO_CHAR(PAN_ISSUE_DT,'DD/MM/YYYY'),NOTES,STATUS,"
					+ "CATEGORY,WEB_ADDR "
					+ "FROM FMS_COMPANY_OWNER_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COMPANY_OWNER_MST B WHERE A.COMPANY_CD=B.COMPANY_CD)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, counterparty_cd);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				eff_dt = rset.getString(1)==null?"":rset.getString(1);
				name = rset.getString(2)==null?"":rset.getString(2);
				abbr = rset.getString(3)==null?"":rset.getString(3);
				pan_no = rset.getString(4)==null?"":rset.getString(4);
				pan_dt = rset.getString(5)==null?"":rset.getString(5);
				notes = rset.getString(6)==null?"":rset.getString(6);
				status = rset.getString(7)==null?"Y":rset.getString(7);
				category = rset.getString(8)==null?"0":rset.getString(8);
				web_addr = rset.getString(9)==null?"":rset.getString(9);
				
				
					if(kyc_flg.equals("Y") && igx_flg.equals("Y")){
						status = "KYC, IGX";
					}else if(kyc_flg.equals("Y")) {
						status = "KYC";
					}else if(igx_flg.equals("Y")) {
						status = "IGX";
					}else {
						status = "";
						kyc_flg="";
					}
				
			}
			else
			{
				status="";
				category="0";
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCOLOR() 
	{
		String function_nm="getCOLOR()";

		try {
			if(entity_role.equals("T")) 
			{
				color = "skyblue";
			}
			else if(entity_role.equals("C")) 
			{
				color = "lightgreen";
			}
			else if(entity_role.equals("R")) 
			{
				color = "#ff99ff";
			}
			else if(entity_role.equals("B")) 
			{
				color = "#ffff99";
			}
			else if(entity_role.equals("V")) 
			{
				color = "#e6c8be";
			}
			else if(entity_role.equals("H")) 
			{
				color = "#FFCC99";
			}
			else if(entity_role.equals("S")) 
			{
				color = "#70FBF1";
			}
		}
		
		catch(Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCOUNTERPARTY_DTL() 
	{		
		String function_nm="getCOUNTERPARTY_DTL()";
	
		try {
			
			String data = "SELECT COUNTERPARTY_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY'),COUNTERPARTY_NM,COUNTERPARTY_ABBR,"
							+ "PAN_NO,TO_CHAR(PAN_ISSUE_DT,'DD/MM/YYYY'),CATEGORY,WEB_ADDR,NOTES,STATUS,KYC,IGX,"
							+ "ENT_BY,ENT_DT,MODIFY_BY,MODIFY_DT "
							+ "FROM FMS_COUNTERPARTY_MST A "
							+ "WHERE COUNTERPARTY_CD=? " 
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD)";
			stmt=conn.prepareStatement(data);
			stmt.setString(1, counterparty_cd);
			rset = stmt.executeQuery();
			if (rset.next())
			{						
				eff_dt = rset.getString(2)==null?"":rset.getString(2);
				name = rset.getString(3)==null?"":rset.getString(3);
				abbr = rset.getString(4)==null?"":rset.getString(4);
				pan_no = rset.getString(5)==null?"":rset.getString(5);
				pan_dt = rset.getString(6)==null?"":rset.getString(6);
				category = rset.getString(7)==null?"0":rset.getString(7);
				web_addr = rset.getString(8)==null?"":rset.getString(8);
				notes = rset.getString(9)==null?"":rset.getString(9);
				status = rset.getString(10)==null?"Y":rset.getString(10);
				kyc_flg = rset.getString(11)==null?"N":rset.getString(11);
				igx_flg = rset.getString(12)==null?"N":rset.getString(12);
				
				if(kyc_flg.equals("Y") && igx_flg.equals("Y")){
					clearance = "KYC, IGX";
				}else if(kyc_flg.equals("Y")) {
					clearance = "KYC";
				}else if(igx_flg.equals("Y")) {
					clearance = "IGX";
				}else {
					clearance = "";
				}
				
			}
			else {
				status = "Y";
				category = "";
				kyc_flg= "";
				notes = "";
			}
			rset.close();
			stmt.close();
			
		}
		
		catch(Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCounterpartyRequesterApproverNote()
	{
		String function_nm="getCounterpartyRequesterApproverNote()";

		try
		{
			queryString="SELECT ENTITY,REMARK,APRV_NOTE,REQ_BY,TO_CHAR(REQ_DT,'DD/MM/YYYY HH:MM:SS'),APRV_BY,TO_CHAR(APRV_DT,'DD/MM/YYYY HH:MM:SS') "
					+ "FROM FMS_ENTITY_REQ_DTL "
					+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND STATUS!=? AND COMPANY_CD=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, counterparty_cd);
			stmt.setString(2, entity_role);
			stmt.setString(3, "X");
			stmt.setString(4, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String rmk=rset.getString(2)==null?"":rset.getString(2);
				String aprv_note=rset.getString(3)==null?"":rset.getString(3);
				String req_by= ""+utilBean.getEmpName(conn,rset.getString(4)==null?"0":rset.getString(4));
				String req_dt = rset.getString(5)==null?"":rset.getString(5);
				String aprv_by= ""+utilBean.getEmpName(conn,rset.getString(6)==null?"0":rset.getString(6));
				String aprv_dt = rset.getString(7)==null?"":rset.getString(7);
				
				if(requester_approver_note.equals(""))
				{
					if(!aprv_note.equals(""))
					{
						requester_approver_note+=""+aprv_by+" ["+aprv_dt+"] [Approver] :: "+aprv_note;
					}
					requester_approver_note+="<br>"+req_by+" ["+req_dt+"] [Requester] :: "+rmk;
				}
				else
				{
					if(!aprv_note.equals(""))
					{
						requester_approver_note+="<br>"+aprv_by+" ["+aprv_dt+"] [Approver] :: "+aprv_note;
					}
					requester_approver_note+="<br>"+req_by+" ["+req_dt+"] [Requester] :: "+rmk;
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
	
	public void getCounterpartyBankDetail()
	{
		String function_nm="getCounterpartyBankDetail()";

		try
		{
			queryString="SELECT COUNTERPARTY_CD,ENTITY,TO_CHAR(EFF_DT,'DD/MM/YYYY'),BANK_NAME,ACCOUNT_ID,IFSC_CODE,BRANCH_NAME,"
					+ "STATE_NM  "
					+ "FROM FMS_ENTITY_BANK_MST A "
					+ "WHERE ENTITY=? AND COUNTERPARTY_CD=? AND COMPANY_CD=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_BANK_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.ENTITY=B.ENTITY AND A.COMPANY_CD=B.COMPANY_CD AND A.CATEGORY=B.CATEGORY)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, entity_role);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, comp_cd);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				bank_eff_dt = rset.getString(3)==null?"":rset.getString(3);
				bank_name=rset.getString(4)==null?"":rset.getString(4);
				bank_account_no=rset.getString(5)==null?"":rset.getString(5);
				ifsc_code=rset.getString(6)==null?"":rset.getString(6);
				bank_branch=rset.getString(7)==null?"":rset.getString(7);
				bank_state=rset.getString(8)==null?"":rset.getString(8);
				
				bank_formula=bank_name+", "+bank_branch+", "+bank_state+", A/C No : "+bank_account_no+", IFSC Code : "+ifsc_code;
			}
			rset.close();
			stmt.close();
			
			queryString1="SELECT COUNTERPARTY_CD,ENTITY,TO_CHAR(EFF_DT,'DD/MM/YYYY'),BANK_NAME,ACCOUNT_ID,IFSC_CODE,BRANCH_NAME,"
					+ "STATE_NM  "
					+ "FROM FMS_ENTITY_BANK_MST A "
					+ "WHERE ENTITY=? AND COUNTERPARTY_CD=? AND COMPANY_CD=? "
					+ "ORDER BY EFF_DT DESC";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, entity_role);
			stmt1.setString(2, counterparty_cd);
			stmt1.setString(3, comp_cd);
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				String bank_eff_dt = rset1.getString(3)==null?"":rset1.getString(3);
				String bank_name=rset1.getString(4)==null?"":rset1.getString(4);
				String bank_account_no=rset1.getString(5)==null?"":rset1.getString(5);
				String ifsc_code=rset1.getString(6)==null?"":rset1.getString(6);
				String bank_branch=rset1.getString(7)==null?"":rset1.getString(7);
				String bank_state=rset1.getString(8)==null?"":rset1.getString(8);
				
				String  bank_formula=bank_name+", "+bank_branch+", "+bank_state+", A/C No : "+bank_account_no+", IFSC Code : "+ifsc_code;
				
				VBANK_FORMULA.add(bank_formula);
				VBANK_EFF_DT.add(bank_eff_dt);
			}
			rset1.close();
			stmt1.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getEntitiesAddressDetail() 
	{
		String function_nm="getEntitiesAddressDetail()";

		try 
		{
			for(int j=0;j<VCOUNTERPARTY_CD.size();j++) 
			{
				
				for(int i=0;i<3;i++)
				{
					int index=i;
					int entity_index=0;
					
					String add_flg="";
					if(i==0)
					{
						add_flg="R";
					}
					else if(i==1)
					{
						add_flg="C";
					}
					else if(i==2)
					{
						add_flg="B";
					}
					
					int cont=0;
					if(entity_role.equals("B"))
					{
						queryString="SELECT ADDRESS_TYPE,TO_CHAR(EFF_DT,'DD/MM/YYYY'),ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL "
								+ "FROM FMS_COMPANY_OWNER_ADDR_MST A "
								+ "WHERE COMPANY_CD=? AND ADDRESS_TYPE=? "
								+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COMPANY_OWNER_ADDR_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.ADDRESS_TYPE=B.ADDRESS_TYPE )";
					}
					else if(entity_role.equals("G"))
					{
						queryString="SELECT ADDRESS_TYPE,TO_CHAR(EFF_DT,'DD/MM/YYYY'),ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL "
								+ "FROM FMS_COMPANY_EXCHG_ADDR_MST A "
								+ "WHERE COUNTERPARTY_CD=? AND COMPANY_CD=? AND ADDRESS_TYPE=? "
								+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COMPANY_EXCHG_ADDR_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND "
								+ "A.COMPANY_CD=B.COMPANY_CD AND A.ADDRESS_TYPE=B.ADDRESS_TYPE)";
					}
					else if(i==0)
					{
						queryString="SELECT A.ADDRESS_TYPE,TO_CHAR(A.EFF_DT,'DD/MM/YYYY'),A.ADDR,CITY,A.PIN,A.STATE,A.ZONE,A.COUNTRY,"
								+ "A.PHONE,A.MOBILE,A.ALT_PHONE,A.FAX_1,A.FAX_2,A.EMAIL "
								+ "FROM FMS_COUNTERPARTY_ADDR_MST A , FMS_COUNTERPARTY_MST C, FMS_ENTITY_REQ_DTL B "
								+ "WHERE A.COUNTERPARTY_CD=? AND A.ADDRESS_TYPE=? "
								+ "AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND B.COMPANY_CD=? AND B.ENTITY=? AND B.STATUS='A'"
								+ "AND A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_ADDR_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.ADDRESS_TYPE=B.ADDRESS_TYPE) "
								+ "AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD ";
						queryString+= "ORDER BY C.COUNTERPARTY_CD";
					}
					else
					{
						queryString="SELECT A.ADDRESS_TYPE,TO_CHAR(A.EFF_DT,'DD/MM/YYYY'),A.ADDR,CITY,A.PIN,A.STATE,A.ZONE,A.COUNTRY,"
								+ "A.PHONE,A.MOBILE,A.ALT_PHONE,A.FAX_1,A.FAX_2,A.EMAIL "
								+ "FROM FMS_ENTITY_ADDR_MST A , FMS_COUNTERPARTY_MST C, FMS_ENTITY_REQ_DTL B "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.ADDRESS_TYPE=? AND A.ENTITY=? "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND B.STATUS='A' "
								+ "AND A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_ADDR_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.ADDRESS_TYPE=B.ADDRESS_TYPE AND A.ENTITY=B.ENTITY) "
								+ "AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD ";
						queryString+= "ORDER BY C.COUNTERPARTY_CD";
					}
					stmt=conn.prepareStatement(queryString);
					if(entity_role.equals("B"))
					{
						stmt.setString(++cont, counterparty_cd);
						stmt.setString(++cont, add_flg);
					}
					else if(entity_role.equals("G"))
					{
						stmt.setString(++cont, counterparty_cd);
						stmt.setString(++cont, comp_cd);
						stmt.setString(++cont, add_flg);
					}
					else if(i==0)
					{
						stmt.setString(++cont, ""+VCOUNTERPARTY_CD.elementAt(j));
						stmt.setString(++cont, add_flg);
						stmt.setString(++cont, comp_cd);
						stmt.setString(++cont, entity);
					}
					else
					{
						stmt.setString(++cont, comp_cd);
						stmt.setString(++cont, ""+VCOUNTERPARTY_CD.elementAt(j));
						stmt.setString(++cont, add_flg);
						stmt.setString(++cont, entity_role);
					}
					rset=stmt.executeQuery();
					if(rset.next())
					{
						entity_index+=1;
						REG_EFF_DT[index] = rset.getString(2)==null?"":rset.getString(2);
						ADDRESS_FLAG[index] = add_flg;
						ADDRESS[index] = rset.getString(3)==null?"":rset.getString(3);
						CITY[index] = rset.getString(4)==null?"":rset.getString(4);
						PIN[index] = rset.getString(5)==null?"":rset.getString(5);
						STATE[index] = rset.getString(6)==null?"":rset.getString(6);
						ZONE[index] = rset.getString(7)==null?"":rset.getString(7);
						COUNTRY[index] = rset.getString(8)==null?"":rset.getString(8);
						PHONE[index] = rset.getString(9)==null?"":rset.getString(9);
						MOBILE[index] = rset.getString(10)==null?"":rset.getString(10);
						ALT_PHONE[index] = rset.getString(11)==null?"":rset.getString(11);
						FAX1[index] = rset.getString(12)==null?"":rset.getString(12);
						FAX2[index] = rset.getString(13)==null?"":rset.getString(13);
						EMAIL[index] = rset.getString(14)==null?"":rset.getString(14);
					}
					else
					{
						REG_EFF_DT[index] = "";
						ADDRESS_FLAG[index] = add_flg;
						ADDRESS[index] =  "";
						CITY[index] =  "";
						PIN[index]  =  "";
						STATE[index]=  "";
						ZONE[index] = "";
						COUNTRY[index]= "";
						PHONE[index] =  "";
						MOBILE[index]=  "";
						ALT_PHONE[index]= "";
						FAX1[index]=  "";
						FAX2[index]=  "";
						EMAIL[index]=  "";
					}
					rset.close();
					stmt.close();
					
					VADD_REG_EFF_DT.add(REG_EFF_DT[index]);
					VADD_ADDRESS_FLAG.add(ADDRESS_FLAG[index]);
					VADD_ADDRES.add(ADDRESS[index]);
					VADD_CITY.add(CITY[index]);
					VADD_PIN.add(PIN[index]);
					VADD_STATE.add(STATE[index]);
					VADD_ZONE.add(ZONE[index]);
					VADD_COUNTRY.add(COUNTRY[index]);
					VADD_PHONE.add(PHONE[index]);
					VADD_MOBILE.add(MOBILE[index]);
					VADD_ALT_PHONE.add(ALT_PHONE[index]);
					VADD_FAX1.add(FAX1[index]);
					VADD_FAX2.add(FAX2[index]);
					VADD_EMAIL.add(EMAIL[index]);
					
					VENTITY_INDEX.add(entity_index);
				}
			}
		} 
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	public void getEntityAddressDetail()
	{
		String function_nm="getEntityAddressDetail()";

		try
		{
			int entity_index=0;
			for(int i=0;i<3;i++)
			{
				int index=i;
				String add_flg="";
				if(i==0)
				{
					add_flg="R";
				}
				else if(i==1)
				{
					add_flg="C";
				}
				else if(i==2)
				{
					add_flg="B";
				}
				
				int cont=0;
				if(entity_role.equals("B"))
				{
					queryString="SELECT ADDRESS_TYPE,TO_CHAR(EFF_DT,'DD/MM/YYYY'),ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL "
							+ "FROM FMS_COMPANY_OWNER_ADDR_MST A "
							+ "WHERE COMPANY_CD=? AND ADDRESS_TYPE=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COMPANY_OWNER_ADDR_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.ADDRESS_TYPE=B.ADDRESS_TYPE)";
				}
				else if(entity_role.equals("G"))
				{
					queryString="SELECT ADDRESS_TYPE,TO_CHAR(EFF_DT,'DD/MM/YYYY'),ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL "
							+ "FROM FMS_COMPANY_EXCHG_ADDR_MST A "
							+ "WHERE COUNTERPARTY_CD=? AND ADDRESS_TYPE=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COMPANY_EXCHG_ADDR_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.ADDRESS_TYPE=B.ADDRESS_TYPE)";
				}
				else if(i==0)
				{
					queryString="SELECT ADDRESS_TYPE,TO_CHAR(EFF_DT,'DD/MM/YYYY'),ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL "
							+ "FROM FMS_COUNTERPARTY_ADDR_MST A "
							+ "WHERE COUNTERPARTY_CD=? AND ADDRESS_TYPE=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_ADDR_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.ADDRESS_TYPE=B.ADDRESS_TYPE)";
				}
				else
				{
					queryString="SELECT ADDRESS_TYPE,TO_CHAR(EFF_DT,'DD/MM/YYYY'),ADDR,CITY,PIN,STATE,ZONE,COUNTRY,PHONE,MOBILE,ALT_PHONE,FAX_1,FAX_2,EMAIL "
							+ "FROM FMS_ENTITY_ADDR_MST A "
							+ "WHERE COUNTERPARTY_CD=? AND COMPANY_CD=? AND ADDRESS_TYPE=? AND ENTITY=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_ADDR_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.ADDRESS_TYPE=B.ADDRESS_TYPE AND A.ENTITY=B.ENTITY)";
				}
				stmt=conn.prepareStatement(queryString);
				if(entity_role.equals("B"))
				{
					stmt.setString(++cont, counterparty_cd);
					stmt.setString(++cont, add_flg);
				}
				else if(entity_role.equals("G"))
				{
					stmt.setString(++cont, counterparty_cd);
					stmt.setString(++cont, add_flg);
				}
				else if(i==0)
				{
					stmt.setString(++cont, counterparty_cd);
					stmt.setString(++cont, add_flg);
				}
				else
				{
					stmt.setString(++cont, counterparty_cd);
					stmt.setString(++cont, comp_cd);
					stmt.setString(++cont, add_flg);
					stmt.setString(++cont, entity_role);
				}
				rset=stmt.executeQuery();
				if(rset.next())
				{
					entity_index+=1;
					REG_EFF_DT[index] = rset.getString(2)==null?"":rset.getString(2);
					ADDRESS_FLAG[index] = add_flg;
					ADDRESS[index] = rset.getString(3)==null?"":rset.getString(3);
					CITY[index] = rset.getString(4)==null?"":rset.getString(4);
					PIN[index] = rset.getString(5)==null?"":rset.getString(5);
					STATE[index] = rset.getString(6)==null?"0":rset.getString(6);
					ZONE[index] = rset.getString(7)==null?"0":rset.getString(7);
					COUNTRY[index] = rset.getString(8)==null?"0":rset.getString(8);
					PHONE[index] = rset.getString(9)==null?"":rset.getString(9);
					MOBILE[index] = rset.getString(10)==null?"":rset.getString(10);
					ALT_PHONE[index] = rset.getString(11)==null?"":rset.getString(11);
					FAX1[index] = rset.getString(12)==null?"":rset.getString(12);
					FAX2[index] = rset.getString(13)==null?"":rset.getString(13);
					EMAIL[index] = rset.getString(14)==null?"":rset.getString(14);
				}
				else
				{
					REG_EFF_DT[index] = "";
					ADDRESS_FLAG[index] = add_flg;
					ADDRESS[index] =  "";
					CITY[index] =  "";
					PIN[index]  =  "";
					STATE[index]=  "";
					ZONE[index] = "";
					COUNTRY[index]= "";
					PHONE[index] =  "";
					MOBILE[index]=  "";
					ALT_PHONE[index]= "";
					FAX1[index]=  "";
					FAX2[index]=  "";
					EMAIL[index]=  "";
				}
				rset.close();
				stmt.close();
			}
			VENTITY_INDEX.add(entity_index);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCounterpartyBuDetail()
	{
		String function_nm="getCounterpartyBuDetail()";

		try
		{
			queryString="SELECT COUNTERPARTY_CD,ENTITY,SEQ_NO,TO_CHAR(EFF_DT,'DD/MM/YYYY'),PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,"
					+ "PLANT_ZONE,PLANT_CITY,PLANT_PIN "
					+ "FROM FMS_COUNTERPARTY_BU_DTL A "
					+ "WHERE ENTITY=? AND COUNTERPARTY_CD=? AND COMPANY_CD=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_BU_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, entity_role);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String plant_seq = rset.getString(3)==null?"0":rset.getString(3);
				VBU_PLANT_SEQ_NO.add(rset.getString(3)==null?"":rset.getString(3));
				VBU_PLANT_EFF_DT.add(rset.getString(4)==null?"":rset.getString(4));
				VBU_PLANT_NAME.add(rset.getString(5)==null?"":rset.getString(5));
				VBU_PLANT_ABBR.add(rset.getString(6)==null?"":rset.getString(6));
				VBU_PLANT_ADDR.add(rset.getString(7)==null?"":rset.getString(7));
				VBU_PLANT_STATE.add(rset.getString(8)==null?"":rset.getString(8));
				VBU_PLANT_ZONE.add(rset.getString(9)==null?"":rset.getString(9));
				VBU_PLANT_ZONE_NM.add(""+getZoneName(rset.getString(9)==null?"":rset.getString(9)));
				VBU_PLANT_CITY.add(rset.getString(10)==null?"":rset.getString(10));
				VBU_PLANT_PIN.add(rset.getString(11)==null?"":rset.getString(11));

				String tax_id="";
				queryString1 = "SELECT A.STAT_NO, B.STAT_NM "
						+ "FROM FMS_COUNTERPARTY_BU_TAX A,FMS_GOVT_STAT_TAX B "
						+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND A.COMPANY_CD=? "
						+ "AND PLANT_SEQ_NO=? AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, counterparty_cd);
				stmt1.setString(2, entity_role);
				stmt1.setString(3, comp_cd);
				stmt1.setString(4, plant_seq);
				rset1 = stmt1.executeQuery();
				while(rset1.next())
				{
					String stat_no = rset1.getString(1)==null?"":rset1.getString(1);
					String stat_nm = rset1.getString(2)==null?"":rset1.getString(2);
					
					if(tax_id.equals(""))
					{
						tax_id +=""+stat_nm+" "+stat_no;
					}
					else
					{
						tax_id +="<br>"+stat_nm+" "+stat_no;
					}
				}
				rset1.close();
				stmt1.close();
				
				VBU_TAX_ID.add(tax_id);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getMultiCounterpartyBuDetail()
	{
		String function_nm="getCounterpartyBuDetail()";

		try
		{
			for(int i=0; i<VCOUNTERPARTY_CD.size(); i++) 
			{
				int bu_index=0;
				
				queryString="SELECT COUNTERPARTY_CD,ENTITY,SEQ_NO,TO_CHAR(EFF_DT,'DD/MM/YYYY'),PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,"
						+ "PLANT_ZONE,PLANT_CITY,PLANT_PIN "
						+ "FROM FMS_COUNTERPARTY_BU_DTL A "
						+ "WHERE ENTITY=? AND COUNTERPARTY_CD=? AND COMPANY_CD=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_BU_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD)";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, entity_role);
				stmt.setString(2, ""+VCOUNTERPARTY_CD.elementAt(i));
				stmt.setString(3, comp_cd);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					bu_index+=1;
					
					String plant_seq = rset.getString(3)==null?"0":rset.getString(3);
					VBU_PLANT_SEQ_NO.add(rset.getString(3)==null?"":rset.getString(3));
					VBU_PLANT_EFF_DT.add(rset.getString(4)==null?"":rset.getString(4));
					VBU_PLANT_NAME.add(rset.getString(5)==null?"":rset.getString(5));
					VBU_PLANT_ABBR.add(rset.getString(6)==null?"":rset.getString(6));
					VBU_PLANT_ADDR.add(rset.getString(7)==null?"":rset.getString(7));
					VBU_PLANT_STATE.add(rset.getString(8)==null?"":rset.getString(8));
					VBU_PLANT_ZONE.add(rset.getString(9)==null?"":rset.getString(9));
					VBU_PLANT_ZONE_NM.add(""+getZoneName(rset.getString(9)==null?"":rset.getString(9)));
					VBU_PLANT_CITY.add(rset.getString(10)==null?"":rset.getString(10));
					VBU_PLANT_PIN.add(rset.getString(11)==null?"":rset.getString(11));

					String tax_id="";
					queryString1 = "SELECT A.STAT_NO, B.STAT_NM "
							+ "FROM FMS_COUNTERPARTY_BU_TAX A,FMS_GOVT_STAT_TAX B "
							+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND A.COMPANY_CD=? "
							+ "AND PLANT_SEQ_NO=? AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, counterparty_cd);
					stmt1.setString(2, entity_role);
					stmt1.setString(3, comp_cd);
					stmt1.setString(4, plant_seq);
					rset1 = stmt1.executeQuery();
					while(rset1.next())
					{
						String stat_no = rset1.getString(1)==null?"":rset1.getString(1);
						String stat_nm = rset1.getString(2)==null?"":rset1.getString(2);
						
						if(tax_id.equals(""))
						{
							tax_id +=""+stat_nm+" "+stat_no;
						}
						else
						{
							tax_id +="<br>"+stat_nm+" "+stat_no;
						}
					}
					rset1.close();
					stmt1.close();
					
					VBU_TAX_ID.add(tax_id);
				}
				
				VBU_INDEX.add(bu_index);
				
				rset.close();
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCounterpartyPlantDetail()
	{
		String function_nm="getCounterpartyPlantDetail()";

		try
		{
			queryString="SELECT COUNTERPARTY_CD,ENTITY,SEQ_NO,TO_CHAR(EFF_DT,'DD/MM/YYYY'),PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,"
					+ "PLANT_ZONE,PLANT_CITY,PLANT_PIN,PLANT_SECTOR "
					+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
					+ "WHERE ENTITY=? AND COUNTERPARTY_CD=? AND COMPANY_CD=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, entity_role);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String plant_seq = rset.getString(3)==null?"0":rset.getString(3);
				VPLANT_SEQ_NO.add(rset.getString(3)==null?"":rset.getString(3));
				VPLANT_EFF_DT.add(rset.getString(4)==null?"":rset.getString(4));
				VPLANT_NAME.add(rset.getString(5)==null?"":rset.getString(5));
				VPLANT_ABBR.add(rset.getString(6)==null?"":rset.getString(6));
				VPLANT_ADDR.add(rset.getString(7)==null?"":rset.getString(7));
				VPLANT_STATE.add(rset.getString(8)==null?"":rset.getString(8));
				VPLANT_ZONE.add(rset.getString(9)==null?"":rset.getString(9));
				VPLANT_ZONE_NM.add(""+getZoneName(rset.getString(9)==null?"":rset.getString(9)));
				VPLANT_CITY.add(rset.getString(10)==null?"":rset.getString(10));
				VPLANT_PIN.add(rset.getString(11)==null?"":rset.getString(11));
				String sectorCd=rset.getString(12)==null?"":rset.getString(12);
				VPLANT_SECTOR.add(sectorCd);
				VPLANT_SECTOR_NM.add(""+utilBean.getSectorName(conn,sectorCd, comp_cd));
				
				String tax_id="";
				queryString1 = "SELECT A.STAT_NO, B.STAT_NM "
						+ "FROM FMS_COUNTERPARTY_PLANT_TAX A,FMS_GOVT_STAT_TAX B "
						+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND A.COMPANY_CD=? "
						+ "AND PLANT_SEQ_NO=? AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, counterparty_cd);
				stmt1.setString(2, entity_role);
				stmt1.setString(3, comp_cd);
				stmt1.setString(4, plant_seq);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					String stat_no = rset1.getString(1)==null?"":rset1.getString(1);
					String stat_nm = rset1.getString(2)==null?"":rset1.getString(2);
					
					if(tax_id.equals(""))
					{
						tax_id +=""+stat_nm+" "+stat_no;
					}
					else
					{
						tax_id +="<br>"+stat_nm+" "+stat_no;
					}
				}
				rset1.close();
				stmt1.close();
				
				VTAX_ID.add(tax_id);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getMultiCounterpartyPlantsDetails()
	{
		String function_nm="getMultiCounterpartyPlantsDetails()";

		try
		{
			for(int i=0; i<VCOUNTERPARTY_CD.size(); i++) 
			{
				int plant_index=0;
				
				queryString="SELECT COUNTERPARTY_CD,ENTITY,SEQ_NO,TO_CHAR(EFF_DT,'DD/MM/YYYY'),PLANT_NAME,PLANT_ABBR,PLANT_ADDR,PLANT_STATE,"
						+ "PLANT_ZONE,PLANT_CITY,PLANT_PIN,PLANT_SECTOR "
						+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
						+ "WHERE ENTITY=? AND COMPANY_CD=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD)";
				queryString+=" AND COUNTERPARTY_CD=? ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, entity_role);
				stmt.setString(2, comp_cd);
				stmt.setString(3, ""+VCOUNTERPARTY_CD.elementAt(i));
				rset=stmt.executeQuery();
				while(rset.next())
				{
					plant_index+=1;
					
					String plant_seq = rset.getString(3)==null?"0":rset.getString(3);
					VPLANT_SEQ_NO.add(rset.getString(3)==null?"":rset.getString(3));
					VPLANT_EFF_DT.add(rset.getString(4)==null?"":rset.getString(4));
					VPLANT_NAME.add(rset.getString(5)==null?"":rset.getString(5));
					VPLANT_ABBR.add(rset.getString(6)==null?"":rset.getString(6));
					VPLANT_ADDR.add(rset.getString(7)==null?"":rset.getString(7));
					VPLANT_STATE.add(rset.getString(8)==null?"":rset.getString(8));
					VPLANT_ZONE.add(rset.getString(9)==null?"":rset.getString(9));
					VPLANT_ZONE_NM.add(""+getZoneName(rset.getString(9)==null?"":rset.getString(9)));
					VPLANT_CITY.add(rset.getString(10)==null?"":rset.getString(10));
					VPLANT_PIN.add(rset.getString(11)==null?"":rset.getString(11));
					String sectorCd=rset.getString(12)==null?"":rset.getString(12);
					VPLANT_SECTOR.add(sectorCd);
					VPLANT_SECTOR_NM.add(""+utilBean.getSectorName(conn,sectorCd, comp_cd));
					
					String tax_id="";
					queryString1 = "SELECT A.STAT_NO, B.STAT_NM "
							+ "FROM FMS_COUNTERPARTY_PLANT_TAX A,FMS_GOVT_STAT_TAX B "
							+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND A.COMPANY_CD=? "
							+ "AND PLANT_SEQ_NO=? AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, counterparty_cd);
					stmt1.setString(2, entity_role);
					stmt1.setString(3, comp_cd);
					stmt1.setString(4, plant_seq);
					rset1 = stmt1.executeQuery();
					while(rset1.next())
					{
						String stat_no = rset1.getString(1)==null?"":rset1.getString(1);
						String stat_nm = rset1.getString(2)==null?"":rset1.getString(2);
						
						if(tax_id.equals(""))
						{
							tax_id +=""+stat_nm+" "+stat_no;
						}
						else
						{
							tax_id +="<br>"+stat_nm+" "+stat_no;
						}
					}
					rset1.close();
					stmt1.close();
					
					VTAX_ID.add(tax_id);
				}
				rset.close();
				stmt.close();
				
				VPLANT_INDEX.add(plant_index);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getPlantDetail()
	{
		String function_nm="getPlantDetail()";

		try
		{
			utilBean.getEffectiveCounterpartyPlantList(conn,counterparty_cd, entity_role, comp_cd);
			VPLANT_SEQ_NO = utilBean.getPLANT_SEQ_NO();
			VPLANT_ABBR = utilBean.getPLANT_ABBR();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	
	
	public void getCounterpartyMst()
	{
		String function_nm="getCounterpartyMst()";

		try
		{
			utilBean.getCounterpartyMaster(conn);
			VCOUNTERPARTY_CD= utilBean.getCOUNTERPARTY_CD();
			VCOUNTERPARTY_NM = utilBean.getCOUNTERPARTY_NM();
			VCOUNTERPARTY_ABBR = utilBean.getCOUNTERPARTY_ABBR();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	
	public void getAddressType()
	{
		String function_nm="getAddressType()";

		try
		{
			if(entity_role.equals("B"))
			{
				queryString="SELECT DISTINCT ADDRESS_TYPE "
						+ "FROM FMS_COMPANY_OWNER_ADDR_MST A "
						+ "WHERE COMPANY_CD=? ";	
			}
			else
			{
				queryString="SELECT DISTINCT ADDRESS_TYPE "
						+ "FROM FMS_COUNTERPARTY_ADDR_MST A "
						+ "WHERE COUNTERPARTY_CD=? ";
				queryString+="UNION ";
				queryString+="SELECT DISTINCT ADDRESS_TYPE "
						+ "FROM FMS_ENTITY_ADDR_MST A "
						+ "WHERE COUNTERPARTY_CD=? AND COMPANY_CD=? ";
			}
			stmt=conn.prepareStatement(queryString);
			if(entity_role.equals("B"))
			{
				stmt.setString(1, counterparty_cd);
			}
			else
			{
				stmt.setString(1, counterparty_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, comp_cd);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String add_type = rset.getString(1)==null?"":rset.getString(1);
				if(add_type.equals("R"))
				{
					VADDR_TYPE.add(add_type);
					VADD_ADDRESS.add("Registered");
				}
				else if(add_type.equals("C"))
				{
					VADDR_TYPE.add(add_type);
					VADD_ADDRESS.add("Correspondence");
				}
				else if(add_type.equals("B"))
				{
					VADDR_TYPE.add(add_type);
					VADD_ADDRESS.add("Billing");
				}
			}
			rset.close();
			stmt.close();
			
			for(int i=0;i<VPLANT_SEQ_NO.size();i++)
			{
				VADDR_TYPE.add("P"+VPLANT_SEQ_NO.elementAt(i));
				VADD_ADDRESS.add(""+VPLANT_ABBR.elementAt(i));
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getEntityRoleRequest()
	{
		String function_nm="getEntityRoleRequest()";

		try 
		{
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_ENTITY_REQ_DTL WHERE (STATUS=? OR STATUS=?)  "
					+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=?";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, "R");
			stmt.setString(2, "A");
			stmt.setString(3, comp_cd);
			stmt.setString(4, counterparty_cd);
			stmt.setString(5, "C");
			rset=stmt.executeQuery();
			if(rset.next())
			{
				if(rset.getInt(1) > 0)
				{
					customer = "Y";
				}
				else
				{
					customer = "N";
				}
			}
			rset.close();
			stmt.close();
			
			queryString1="SELECT COUNT(*) "
					+ "FROM FMS_ENTITY_REQ_DTL WHERE (STATUS=? OR STATUS=?)  "
					+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=?";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, "R");
			stmt1.setString(2, "A");
			stmt1.setString(3, comp_cd);
			stmt1.setString(4, counterparty_cd);
			stmt1.setString(5, "T");
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				if(rset1.getInt(1) > 0)
				{
					trader = "Y";
				}
				else
				{
					trader = "N";
				}
			}
			rset1.close();
			stmt1.close();
			
			queryString2="SELECT COUNT(*) "
					+ "FROM FMS_ENTITY_REQ_DTL WHERE (STATUS=? OR STATUS=?)  "
					+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=?";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, "R");
			stmt2.setString(2, "A");
			stmt2.setString(3, comp_cd);
			stmt2.setString(4, counterparty_cd);
			stmt2.setString(5, "R");
			rset2=stmt2.executeQuery();
			if(rset2.next())
			{
				if(rset2.getInt(1) > 0)
				{
					transporter = "Y";
				}
				else
				{
					transporter = "N";
				}
			}
			rset2.close();
			stmt2.close();
			
			queryString4="SELECT COUNT(*) "
					+ "FROM FMS_ENTITY_REQ_DTL WHERE (STATUS=? OR STATUS=?)  "
					+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=?";
			stmt4=conn.prepareStatement(queryString4);
			stmt4.setString(1, "R");
			stmt4.setString(2, "A");
			stmt4.setString(3, comp_cd);
			stmt4.setString(4, counterparty_cd);
			stmt4.setString(5, "V");
			rset4=stmt4.executeQuery();
			if(rset4.next())
			{
				if(rset4.getInt(1) > 0)
				{
					vessel_agent = "Y";
				}
				else
				{
					vessel_agent = "N";
				}
			}
			rset4.close();
			stmt4.close();
			
			queryString5="SELECT COUNT(*) "
					+ "FROM FMS_ENTITY_REQ_DTL WHERE (STATUS=? OR STATUS=?)  "
					+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=?";
			stmt5=conn.prepareStatement(queryString5);
			stmt5.setString(1, "R");
			stmt5.setString(2, "A");
			stmt5.setString(3, comp_cd);
			stmt5.setString(4, counterparty_cd);
			stmt5.setString(5, "H");
			rset5=stmt5.executeQuery();
			if(rset5.next())
			{
				if(rset5.getInt(1) > 0)
				{
					custom_hagent = "Y";
				}
				else
				{
					custom_hagent = "N";
				}
			}
			rset5.close();
			stmt5.close();
			
			queryString6="SELECT COUNT(*) "
					+ "FROM FMS_ENTITY_REQ_DTL WHERE (STATUS=? OR STATUS=?)  "
					+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY=?";
			stmt6=conn.prepareStatement(queryString4);
			stmt6.setString(1, "R");
			stmt6.setString(2, "A");
			stmt6.setString(3, comp_cd);
			stmt6.setString(4, counterparty_cd);
			stmt6.setString(5, "S");
			rset6=stmt6.executeQuery();
			if(rset6.next())
			{
				if(rset6.getInt(1) > 0)
				{
					surveyor = "Y";
				}
				else
				{
					surveyor = "N";
				}
			}
			rset6.close();
			stmt6.close();
			
			queryString3="SELECT COUNTERPARTY_CD,ENTITY,REMARK,REQ_BY,TO_CHAR(REQ_DT,'DD/MM/YYYY'),SEQ_NO "
					+ "FROM FMS_ENTITY_REQ_DTL WHERE STATUS=? AND COMPANY_CD=? ";
			stmt3=conn.prepareStatement(queryString3);
			stmt3.setString(1, "R");
			stmt3.setString(2, comp_cd);
			rset3=stmt3.executeQuery();
			while(rset3.next())
			{
				String counterpty_cd = rset3.getString(1)==null?"":rset3.getString(1);
				VCOUNTERPARTY_CD.add(counterpty_cd);
				VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,counterpty_cd));
				VENTITY_ROLE.add(rset3.getString(2)==null?"":rset3.getString(2));
				VREMARK.add(rset3.getString(3)==null?"":rset3.getString(3));
				
				String req_cd = rset3.getString(4)==null?"":rset3.getString(4);
				VREQ_BY.add(""+utilBean.getEmpName(conn,req_cd));
				VREQ_DT.add(rset3.getString(5)==null?"":rset3.getString(5));
				VSEQ_NO.add(rset3.getString(6)==null?"":rset3.getString(6));
			}
			rset3.close();
			stmt3.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public String getZoneName(String zone)
	{
		String function_nm="getZoneName()";

		String name="";
		try
		{
			if(zone.equals("N"))
			{
				name="North";
			}
			else if(zone.equals("S"))
			{
				name="South";
			}
			else if(zone.equals("E"))
			{
				name="East";
			}
			else if(zone.equals("W"))
			{
				name="West";
			}
			else if(zone.equals("C"))
			{
				name="Central";
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return name;
	}
	
	String callFlag = "";
	public void setCallFlag(String callFlag) {this.callFlag = callFlag;}
	String comp_cd = "";
	public void setComp_cd(String comp_cd) {this.comp_cd = comp_cd;}
	
	String opration="";
	String counterparty_cd="";
	String name="";
	String abbr="";
	String eff_dt="";
	String pan_no="";
	String pan_dt="";
	String notes="";
	String clearance="";
	String kyc_flg="N";
	String igx_flg="N";
	String business_unit="N";
	String customer="N";
	String trader="N";
	String transporter="N";
	String vessel_agent="N";
	String custom_hagent="N";
	String surveyor="N";
	String status="";
	String category="";
	String web_addr="";
	String sap_code="";
	
	String Tax_id = "";
	String color = "";
	
	String plant_seq = "";
	String peef_dt = "";
	String pname = "";
	String pabbr = "";
	String paddr = "";
	String pstate = "";
	String pzone = "";
	String pzone_nm = "";
	String pcity = "";
	String ppin = "";
	String psector_cd = "";
	String psector_nm = "";
	
	String reg_eff_dt="";
	String address="";
	String city="";
	String pin="";
	String state="";
	String zone="";
	String country="";
	String phone="";
	String mobile="";
	String alt_phone="";
	String fax1="";
	String fax2="";
	String email="";
	
	String entity = "";
	String entity_role="";
	String requester_approver_note="";
	String bu_unti="";
	String bu_plant_state="";
	String financial_year="";
	String turnover_cd="";
	
	String bank_eff_dt="";
	String bank_name="";
	String bank_account_no="";
	String bank_branch="";
	String ifsc_code="";
	String bank_state="";
	String bank_formula="";
	
	String address_type="";
	String notification_type="";
	
	public void setOpration(String opration) {this.opration = opration;}
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setEntity_role(String entity_role) {this.entity_role = entity_role;}
	public void setBu_unti(String bu_unti) {this.bu_unti = bu_unti;}
	public void setFinancial_year(String financial_year) {this.financial_year = financial_year;}
	public void setTurnover_cd(String turnover_cd) {this.turnover_cd = turnover_cd;}
	public void setAddress_type(String address_type) {this.address_type = address_type;}
	public void setNotification_type(String notification_type) {this.notification_type = notification_type;}
	
	
	public String getName() {return name;}
	public String getAbbr() {return abbr;}
	public String getEff_dt() {return eff_dt;}
	public String getPan_no() {return pan_no;}
	public String getPan_dt() {return pan_dt;}
	public String getNotes() {return notes;}
	public String getKyc_flg() {return kyc_flg;}
	public String getIgx_flg() {return igx_flg;}
	public String getBusiness_unit() {return business_unit;}
	public String getCustomer() {return customer;}
	public String getTrader() {return trader;}
	public String getTransporter() {return transporter;}
	public String getVessel_agent() {return vessel_agent;}
	public String getCustom_hagent() {return custom_hagent;}
	public String getSurveyor() {return surveyor;}
	public String getStatus() {return status;}
	public String getCategory() {return category;}
	public String getWeb_addr() {return web_addr;}
	public String getSap_code() {return sap_code;}
	public String getReg_eff_dt() {return reg_eff_dt;}
	public String getAddress() {return address;}
	public String getCity() {return city;}
	public String getPin() {return pin;}
	public String getState() {return state;}
	public String getZone() {return zone;}
	public String getCountry() {return country;}
	public String getPhone() {return phone;}
	public String getMobile() {return mobile;}
	public String getAlt_phone() {return alt_phone;}
	public String getFax1() {return fax1;}
	public String getFax2() {return fax2;}
	public String getEmail() {return email;}
	public String getRequester_approver_note() {return requester_approver_note;}
	public String getBu_plant_state() {return bu_plant_state;}
	
	public String getBank_eff_dt() {return bank_eff_dt;}
	public String getBank_name() {return bank_name;}
	public String getBank_account_no() {return bank_account_no;}
	public String getBank_branch() {return bank_branch;}
	public String getIfsc_code() {return ifsc_code;}
	public String getBank_state() {return bank_state;}
	public String getBank_formula() {return bank_formula;}
	
	public String getBu_unit() {return bu_unti;}
	public String getCounterparty_cd() {return counterparty_cd;}
	
	Vector VPAN = new Vector();
	Vector VPAN_DT = new Vector();
	Vector VWEB_ADD = new Vector();
	Vector VKYC = new Vector();
	Vector VIGX = new Vector();
	Vector VCUSTOMER = new Vector();
	Vector VTRADER = new Vector();
	Vector VTRASPORTER = new Vector();
	Vector VCATEGORY = new Vector();
	Vector VRQAP_NOTES = new Vector();
	Vector VALT_PHONE = new Vector();
	
	Vector VENT_BY = new Vector();
	Vector VENT_DT = new Vector();
	Vector VMODIFY_BY = new Vector();
	Vector VMODIFY_DT = new Vector();
	Vector VCOUNTRY_CODE = new Vector();
	Vector VCOUNTRY_NM = new Vector();
	Vector VISO_CODE = new Vector();
	Vector VTIN = new Vector();
	Vector VSTATE_CODE = new Vector();
	Vector VSTATE_NM = new Vector();
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VCOUNTERPTY_CD = new Vector();
	Vector VCOUNTERPTY_NM = new Vector();
	Vector VENTITY_ROLE = new Vector();
	Vector VREMARK = new Vector();
	Vector VREQ_BY = new Vector();
	Vector VREQ_DT = new Vector();
	Vector VSEQ_NO = new Vector();
	Vector VPLANT_SEQ_NO = new Vector();
	Vector VPLANT_EFF_DT = new Vector();
	Vector VPLANT_NAME = new Vector();
	Vector VPLANT_ABBR = new Vector();
	Vector VPLANT_ADDR = new Vector();
	Vector VPLANT_STATE = new Vector();
	Vector VPLANT_ZONE = new Vector();
	Vector VPLANT_ZONE_NM = new Vector();
	Vector VPLANT_CITY = new Vector();
	Vector VPLANT_PIN = new Vector();
	Vector VPLANT_SECTOR = new Vector();
	Vector VPLANT_SECTOR_NM = new Vector();
	Vector VPLANT_STATUS = new Vector();
	Vector VTAX_ID = new Vector();
	
	Vector VPLANT_INDEX = new Vector();
	Vector VENTITY_INDEX = new Vector();
	
	Vector VADD_REG_EFF_DT = new Vector();
	Vector VADD_ADDRESS_FLAG = new Vector();
	Vector VADD_ADDRES = new Vector();
	Vector VADD_CITY = new Vector();
	Vector VADD_PIN = new Vector();
	Vector VADD_STATE = new Vector();
	Vector VADD_ZONE = new Vector();
	Vector VADD_COUNTRY = new Vector();
	Vector VADD_PHONE = new Vector();
	Vector VADD_MOBILE = new Vector();
	Vector VADD_ALT_PHONE = new Vector();
	Vector VADD_FAX1 = new Vector();
	Vector VADD_FAX2 = new Vector();
	Vector VADD_EMAIL = new Vector();
	
	Vector VSTAT_CD = new Vector();
	Vector VSTAT_NM = new Vector();
	Vector VSTAT_TYPE = new Vector();
	Vector VSTAT_STATUS = new Vector();
	Vector VSTAT_REMARK = new Vector();
	Vector VSTAT_NO = new Vector();
	Vector VSTAT_EFF_DT = new Vector();
	
	Vector VREQUESTER = new Vector();
	Vector VAPPROVER = new Vector();
	Vector VCOLOR = new Vector();
	Vector VCLEARANCE = new Vector();
	
	Vector VEFF_DT = new Vector();
	Vector VPERSON_NM = new Vector();
	Vector VDESIGNATION = new Vector();
	Vector VPHONE = new Vector();
	Vector VMOBILE = new Vector();
	Vector VFAX1 = new Vector();
	Vector VFAX2 = new Vector();
	Vector VEMAIL = new Vector();
	Vector VADDR_FLAG = new Vector();
	Vector VADDR_TYPE = new Vector();
	Vector VADD_ADDRESS = new Vector();
	Vector VNOM = new Vector();
	Vector VINV = new Vector();
	Vector VJT = new Vector();
	Vector VFM = new Vector();
	Vector VPM = new Vector();
	Vector VOTHER = new Vector();
	Vector VSTATUS = new Vector();
	Vector VADDRESS_TYPE = new Vector();
	Vector VADDRESS_NAME = new Vector();
	Vector VTAX_STRUCT_CD = new Vector();
	Vector VTAX_STRUCT_NM = new Vector();
	Vector VTAX_STRUCT_APP_DT = new Vector();
	Vector VTAX_STRUCT_RMK = new Vector();
	Vector VSER_TAX_STRUCT_CD = new Vector();
	Vector VSER_TAX_STRUCT_NM = new Vector();
	Vector VSER_TAX_STRUCT_APP_DT = new Vector();
	Vector VSER_TAX_STRUCT_RMK = new Vector();
	Vector VTEMP_TAX_STRUCT_CD = new Vector();
	Vector VTEMP_TAX_STRUCT_NM = new Vector();
	Vector VTEMP_TAX_STRUCT_APP_DT = new Vector();
	Vector VTEMP_TAX_STRUCT_RMK = new Vector();
	Vector VTEMP_SER_TAX_STRUCT_CD = new Vector();
	Vector VTEMP_SER_TAX_STRUCT_NM = new Vector();
	Vector VTEMP_SER_TAX_STRUCT_APP_DT = new Vector();
	Vector VTEMP_SER_TAX_STRUCT_RMK = new Vector();
	Vector VINDEX = new Vector();
	Vector VTEMP_SER_INDEX = new Vector();
	Vector VTEMP_ENTERED_BY = new Vector();
	Vector VTEMP_SER_ENTERED_BY = new Vector();
	Vector VSER_COLOR = new Vector();
	Vector VSER_INDEX = new Vector();
	Vector VSER_INVOICE_TYPE = new Vector();
	Vector VSER_ENTERED_BY = new Vector();
	Vector VSER_INVOICE_TYPE_NM = new Vector();
	Vector VFLAG = new Vector();
	Vector VTO_LIST_FLAG = new Vector();
	
	Vector VTEMP_COUNTERPARTY = new Vector();
	
	Vector VBU_CD = new Vector();
	Vector VBU_PLANT_NM = new Vector();
	Vector VBU_PLANT_ABBR = new Vector();
	Vector VBU_PLANT_SEQ_NO = new Vector();
	Vector VBU_PLANT_EFF_DT = new Vector();
	Vector VBU_PLANT_NAME = new Vector();
	Vector VBU_PLANT_ADDR = new Vector();
	Vector VBU_PLANT_STATE = new Vector();
	Vector VBU_PLANT_ZONE = new Vector();
	Vector VBU_PLANT_ZONE_NM = new Vector();
	Vector VBU_PLANT_CITY = new Vector();
	Vector VBU_PLANT_PIN = new Vector();
	Vector VBU_PLANT_STATUS = new Vector();
	Vector VBU_TAX_ID = new Vector();
	Vector VBU_INDEX = new Vector();
	
	Vector VFINANCIAL_YEAR = new Vector();
	Vector VTURNOVER_FLAG = new Vector();
	
	Vector VBANK_FORMULA = new Vector();
	Vector VBANK_EFF_DT = new Vector();
	
	Vector VSECTOR_CD = new Vector();
	Vector VSECTOR_NAME = new Vector();
	
	Vector VTEMP_PHONE = new Vector();
	Vector VTEMP_FAX1 = new Vector();
	Vector VTEMP_FAX2 = new Vector();
	Vector VTEMP_ADDR_FLAG = new Vector();
	Vector VTEMP_ADD_ADDRESS = new Vector();
	Vector VTEMP_FLAG = new Vector();
	Vector VTEMP_TO_NOM = new Vector();
	Vector VTEMP_TO_INV = new Vector();
	Vector VTEMP_TO_JT = new Vector();
	Vector VTEMP_TO_FM = new Vector();
	Vector VTEMP_TO_PM = new Vector();
	Vector VTEMP_TO_OTHER = new Vector();
	
	public Vector getVCOUNTRY_CODE() {return VCOUNTRY_CODE;}
	public Vector getVCOUNTRY_NM() {return VCOUNTRY_NM;}
	public Vector getVISO_CODE() {return VISO_CODE;}
	public Vector getVTIN() {return VTIN;}
	public Vector getVSTATE_CODE() {return VSTATE_CODE;}
	public Vector getVSTATE_NM() {return VSTATE_NM;}
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVCOUNTERPTY_CD() {return VCOUNTERPTY_CD;}
	public Vector getVCOUNTERPTY_NM() {return VCOUNTERPTY_NM;}
	public Vector getVENTITY_ROLE() {return VENTITY_ROLE;}
	public Vector getVREMARK() {return VREMARK;}
	public Vector getVREQ_BY() {return VREQ_BY;}
	public Vector getVREQ_DT() {return VREQ_DT;}
	public Vector getVSEQ_NO() {return VSEQ_NO;}

	public Vector getVPLANT_SEQ_NO() {return VPLANT_SEQ_NO;}
	public Vector getVPLANT_EFF_DT() {return VPLANT_EFF_DT;}
	public Vector getVPLANT_NAME() {return VPLANT_NAME;}
	public Vector getVPLANT_ABBR() {return VPLANT_ABBR;}
	public Vector getVPLANT_ADDR() {return VPLANT_ADDR;}
	public Vector getVPLANT_STATE() {return VPLANT_STATE;}
	public Vector getVPLANT_ZONE() {return VPLANT_ZONE;}
	public Vector getVPLANT_ZONE_NM() {return VPLANT_ZONE_NM;}
	public Vector getVPLANT_CITY() {return VPLANT_CITY;}
	public Vector getVPLANT_PIN() {return VPLANT_PIN;}
	public Vector getVPLANT_SECTOR() {return VPLANT_SECTOR;}
	public Vector getVPLANT_SECTOR_NM() {return VPLANT_SECTOR_NM;}
	public Vector getVPLANT_STATUS() {return VPLANT_STATUS;}
	public Vector getVTAX_ID() {return VTAX_ID;}
	
	public Vector getVBU_PLANT_EFF_DT() {return VBU_PLANT_EFF_DT;}
	public Vector getVBU_PLANT_NAME() {return VBU_PLANT_NAME;}
	public Vector getVBU_PLANT_ADDR() {return VBU_PLANT_ADDR;}
	public Vector getVBU_PLANT_STATE() {return VBU_PLANT_STATE;}
	public Vector getVBU_PLANT_ZONE() {return VBU_PLANT_ZONE;}
	public Vector getVBU_PLANT_ZONE_NM() {return VBU_PLANT_ZONE_NM;}
	public Vector getVBU_PLANT_CITY() {return VBU_PLANT_CITY;}
	public Vector getVBU_PLANT_PIN() {return VBU_PLANT_PIN;}
	public Vector getVBU_PLANT_STATUS() {return VBU_PLANT_STATUS;}
	public Vector getVBU_TAX_ID() {return VBU_TAX_ID;}
	public Vector getVBU_INDEX() {return VBU_INDEX;}
	
	public Vector getVSTAT_CD() {return VSTAT_CD;}
	public Vector getVSTAT_NM() {return VSTAT_NM;}
	public Vector getVSTAT_TYPE() {return VSTAT_TYPE;}
	public Vector getVSTAT_STATUS() {return VSTAT_STATUS;}
	public Vector getVSTAT_REMARK() {return VSTAT_REMARK;}
	public Vector getVSTAT_NO() {return VSTAT_NO;}
	public Vector getVSTAT_EFF_DT() {return VSTAT_EFF_DT;}
	
	public Vector getVREQUESTER() {return VREQUESTER;}
	public Vector getVAPPROVER() {return VAPPROVER;}
	public Vector getVCOLOR() {return VCOLOR;}
	public Vector getVCLEARANCE() {return VCLEARANCE;}
	
	public Vector getVEFF_DT() {return VEFF_DT;}
	public Vector getVPERSON_NM() {return VPERSON_NM;}
	public Vector getVDESIGNATION() {return VDESIGNATION;}
	public Vector getVPHONE() {return VPHONE;}
	public Vector getVMOBILE() {return VMOBILE;}
	public Vector getVFAX1() {return VFAX1;}
	public Vector getVFAX2() {return VFAX2;}
	public Vector getVEMAIL() {return VEMAIL;}
	public Vector getVADDR_FLAG() {return VADDR_FLAG;}
	public Vector getVADDR_TYPE() {return VADDR_TYPE;}
	public Vector getVADD_ADDRESS() {return VADD_ADDRESS;}
	public Vector getVNOM() {return VNOM;}
	public Vector getVINV() {return VINV;}
	public Vector getVJT() {return VJT;}
	public Vector getVFM() {return VFM;}
	public Vector getVPM() {return VPM;}
	public Vector getVOTHER() {return VOTHER;}
	public Vector getVSTATUS() {return VSTATUS;}
	public Vector getVADDRESS_TYPE() {return VADDRESS_TYPE;}
	public Vector getVADDRESS_NAME() {return VADDRESS_NAME;}
	public Vector getVTAX_STRUCT_CD() {return VTAX_STRUCT_CD;}
	public Vector getVTAX_STRUCT_NM() {return VTAX_STRUCT_NM;}
	public Vector getVTAX_STRUCT_APP_DT() {return VTAX_STRUCT_APP_DT;}
	public Vector getVTAX_STRUCT_RMK() {return VTAX_STRUCT_RMK;}
	public Vector getVSER_TAX_STRUCT_CD() {return VSER_TAX_STRUCT_CD;}
	public Vector getVSER_TAX_STRUCT_NM() {return VSER_TAX_STRUCT_NM;}
	public Vector getVSER_TAX_STRUCT_APP_DT() {return VSER_TAX_STRUCT_APP_DT;}
	public Vector getVSER_TAX_STRUCT_RMK() {return VSER_TAX_STRUCT_RMK;}
	public Vector getVTEMP_TAX_STRUCT_CD() {return VTEMP_TAX_STRUCT_CD;}
	public Vector getVTEMP_TAX_STRUCT_NM() {return VTEMP_TAX_STRUCT_NM;}
	public Vector getVTEMP_TAX_STRUCT_APP_DT() {return VTEMP_TAX_STRUCT_APP_DT;}
	public Vector getVTEMP_TAX_STRUCT_RMK() {return VTEMP_TAX_STRUCT_RMK;}
	public Vector getVTEMP_SER_TAX_STRUCT_CD() {return VTEMP_SER_TAX_STRUCT_CD;}
	public Vector getVTEMP_SER_TAX_STRUCT_NM() {return VTEMP_SER_TAX_STRUCT_NM;}
	public Vector getVTEMP_SER_TAX_STRUCT_APP_DT() {return VTEMP_SER_TAX_STRUCT_APP_DT;}
	public Vector getVTEMP_SER_TAX_STRUCT_RMK() {return VTEMP_SER_TAX_STRUCT_RMK;}
	public Vector getVINDEX() {return VINDEX;}
	public Vector getVTEMP_SER_INDEX() {return VTEMP_SER_INDEX;}
	public Vector getVTEMP_ENTERED_BY() {return VTEMP_ENTERED_BY;}
	public Vector getVTEMP_SER_ENTERED_BY() {return VTEMP_SER_ENTERED_BY;}
	public Vector getVSER_COLOR() {return VSER_COLOR;}
	public Vector getVSER_INDEX() {return VSER_INDEX;}
	public Vector getVSER_INVOICE_TYPE() {return VSER_INVOICE_TYPE;}
	public Vector getVSER_ENTERED_BY() {return VSER_ENTERED_BY;}
	public Vector getVSER_INVOICE_TYPE_NM() {return VSER_INVOICE_TYPE_NM;}
	public Vector getVFLAG() {return VFLAG;}
	public Vector getVTO_LIST_FLAG() {return VTO_LIST_FLAG;}
	
	public Vector getVBU_CD() {return VBU_CD;}
	public Vector getVBU_PLANT_NM() {return VBU_PLANT_NM;}
	public Vector getVBU_PLANT_ABBR() {return VBU_PLANT_ABBR;}
	public Vector getVBU_PLANT_SEQ_NO() {return VBU_PLANT_SEQ_NO;}
	
	public Vector getVFINANCIAL_YEAR() {return VFINANCIAL_YEAR;}
	public Vector getVTURNOVER_FLAG() {return VTURNOVER_FLAG;}
	
	public Vector getVBANK_FORMULA() {return VBANK_FORMULA;}
	public Vector getVBANK_EFF_DT() {return VBANK_EFF_DT;}
	
	public Vector getVSECTOR_CD() {return VSECTOR_CD;}
	public Vector getVSECTOR_NAME() {return VSECTOR_NAME;}
	
	public Vector getVTEMP_PHONE() {return VTEMP_PHONE;}
	public Vector getVTEMP_FAX1() {return VTEMP_FAX1;}
	public Vector getVTEMP_FAX2() {return VTEMP_FAX2;}
	public Vector getVTEMP_ADDR_FLAG() {return VTEMP_ADDR_FLAG;}
	public Vector getVTEMP_ADD_ADDRESS() {return VTEMP_ADD_ADDRESS;}
	public Vector getVTEMP_FLAG() {return VTEMP_FLAG;}
	public Vector getVTEMP_TO_NOM() {return VTEMP_TO_NOM;}
	public Vector getVTEMP_TO_INV() {return VTEMP_TO_INV;}
	public Vector getVTEMP_TO_JT() {return VTEMP_TO_JT;}
	public Vector getVTEMP_TO_FM() {return VTEMP_TO_FM;}
	public Vector getVTEMP_TO_PM() {return VTEMP_TO_PM;}
	public Vector getVTEMP_TO_OTHER() {return VTEMP_TO_OTHER;}
	
	String[] REG_EFF_DT= new String[3];
	String[] ADDRESS_FLAG= new String[3];
	String[] ADDRESS= new String[3];
	String[] CITY= new String[3];
	String[] PIN= new String[3];
	String[] STATE= new String[3];
	String[] ZONE= new String[3];
	String[] COUNTRY= new String[3];
	String[] PHONE= new String[3];
	String[] MOBILE= new String[3];
	String[] ALT_PHONE= new String[3];
	String[] FAX1= new String[3];
	String[] FAX2= new String[3];
	String[] EMAIL= new String[3];

	public String[] getREG_EFF_DT() {return REG_EFF_DT;}
	public String[] getADDRESS_FLAG() {return ADDRESS_FLAG;}
	public String[] getADDRESS() {return ADDRESS;}
	public String[] getCITY() {return CITY;}
	public String[] getPIN() {return PIN;}
	public String[] getSTATE() {return STATE;}
	public String[] getZONE() {return ZONE;}
	public String[] getCOUNTRY() {return COUNTRY;}
	public String[] getPHONE() {return PHONE;}
	public String[] getMOBILE() {return MOBILE;}
	public String[] getALT_PHONE() {return ALT_PHONE;}
	public String[] getFAX1() {return FAX1;}
	public String[] getFAX2() {return FAX2;}
	public String[] getEMAIL() {return EMAIL;}

	public Vector getVPAN() {return VPAN;}
	public Vector getVPAN_DT() {return VPAN_DT;}

	public Vector getVWEB_ADD() {return VWEB_ADD;}
	public Vector getVKYC() {return VKYC;}
	public Vector getVCATEGORY() {return VCATEGORY;}
	public Vector getVRQAP_NOTES() {return VRQAP_NOTES;}
	public Vector getVALT_PHONE() {return VALT_PHONE;}

	public Vector getVIGX() {
		return VIGX;
	}

	public void setVIGX(Vector vIGX) {
		VIGX = vIGX;
	}

	public Vector getVCUSTOMER() {
		return VCUSTOMER;
	}

	public void setVCUSTOMER(Vector vCUSTOMER) {
		VCUSTOMER = vCUSTOMER;
	}

	public Vector getVTRADER() {
		return VTRADER;
	}

	public void setVTRADER(Vector vTRADER) {
		VTRADER = vTRADER;
	}

	public Vector getVTRASPORTER() {
		return VTRASPORTER;
	}

	public void setVTRASPORTER(Vector vTRASPORTER) {
		VTRASPORTER = vTRASPORTER;
	}

	public void setVCATEGORY(Vector vCATEGORY) {
		VCATEGORY = vCATEGORY;
	}

	public Vector getVENT_BY() {
		return VENT_BY;
	}

	public void setVENT_BY(Vector vENT_BY) {
		VENT_BY = vENT_BY;
	}

	public Vector getVENT_DT() {
		return VENT_DT;
	}

	public void setVENT_DT(Vector vENT_DT) {
		VENT_DT = vENT_DT;
	}

	public Vector getVMODIFY_BY() {
		return VMODIFY_BY;
	}

	public void setVMODIFY_BY(Vector vMODIFY_BY) {
		VMODIFY_BY = vMODIFY_BY;
	}

	public Vector getVMODIFY_DT() {
		return VMODIFY_DT;
	}

	public void setVMODIFY_DT(Vector vMODIFY_DT) {
		VMODIFY_DT = vMODIFY_DT;
	}

	public Vector getVTEMP_COUNTERPARTY() {
		return VTEMP_COUNTERPARTY;
	}

	public void setVTEMP_COUNTERPARTY(Vector vTEMP_COUNTERPARTY) {
		VTEMP_COUNTERPARTY = vTEMP_COUNTERPARTY;
	}

	public String getPlant_seq() {
		return plant_seq;
	}

	public void setPlant_seq(String plant_seq) {
		this.plant_seq = plant_seq;
	}

	public String getPeef_dt() {
		return peef_dt;
	}

	public void setPeef_dt(String peef_dt) {
		this.peef_dt = peef_dt;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getPabbr() {
		return pabbr;
	}

	public void setPabbr(String pabbr) {
		this.pabbr = pabbr;
	}

	public String getPaddr() {
		return paddr;
	}

	public void setPaddr(String paddr) {
		this.paddr = paddr;
	}

	public String getPstate() {
		return pstate;
	}

	public void setPstate(String pstate) {
		this.pstate = pstate;
	}

	public String getPzone() {
		return pzone;
	}

	public void setPzone(String pzone) {
		this.pzone = pzone;
	}

	public String getPzone_nm() {
		return pzone_nm;
	}

	public void setPzone_nm(String pzone_nm) {
		this.pzone_nm = pzone_nm;
	}

	public String getPcity() {
		return pcity;
	}

	public void setPcity(String pcity) {
		this.pcity = pcity;
	}

	public String getPpin() {
		return ppin;
	}

	public void setPpin(String ppin) {
		this.ppin = ppin;
	}

	public String getPsector_cd() {
		return psector_cd;
	}

	public void setPsector_cd(String psector_cd) {
		this.psector_cd = psector_cd;
	}

	public String getPsector_nm() {
		return psector_nm;
	}

	public void setPsector_nm(String psector_nm) {
		this.psector_nm = psector_nm;
	}

	public void setPan_no(String pan_no) {
		this.pan_no = pan_no;
	}

	public void setPan_dt(String pan_dt) {
		this.pan_dt = pan_dt;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTax_id() {
		return Tax_id;
	}

	public void setTax_id(String tax_id) {
		Tax_id = tax_id;
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}

	public String getClearance() {
		return clearance;
	}

	public void setClearance(String clearance) {
		this.clearance = clearance;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public Vector getVPLANT_INDEX() {
		return VPLANT_INDEX;
	}

	public Vector getVENTITY_INDEX() {
		return VENTITY_INDEX;
	}

	public Vector getVADD_REG_EFF_DT() {
		return VADD_REG_EFF_DT;
	}

	public Vector getVADD_ADDRESS_FLAG() {
		return VADD_ADDRESS_FLAG;
	}

	public Vector getVADD_ADDRES() {
		return VADD_ADDRES;
	}

	public Vector getVADD_CITY() {
		return VADD_CITY;
	}

	public Vector getVADD_PIN() {
		return VADD_PIN;
	}

	public Vector getVADD_STATE() {
		return VADD_STATE;
	}

	public Vector getVADD_ZONE() {
		return VADD_ZONE;
	}

	public Vector getVADD_COUNTRY() {
		return VADD_COUNTRY;
	}

	public Vector getVADD_PHONE() {
		return VADD_PHONE;
	}

	public Vector getVADD_MOBILE() {
		return VADD_MOBILE;
	}

	public Vector getVADD_ALT_PHONE() {
		return VADD_ALT_PHONE;
	}

	public Vector getVADD_FAX1() {
		return VADD_FAX1;
	}

	public Vector getVADD_FAX2() {
		return VADD_FAX2;
	}

	public Vector getVADD_EMAIL() {
		return VADD_EMAIL;
	}
}
