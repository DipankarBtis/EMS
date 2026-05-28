package com.etrm.fms.dlng;

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

public class DataBean_DLNG_Master 
{
	String db_src_file_name="DataBean_DLNG_Master.java";
	Connection conn;
	PreparedStatement stmt;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt5;
	PreparedStatement stmt6;
	ResultSet rset;
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset5;
	ResultSet rset6;
	String queryString="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";
	String queryString6="";

	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");

	UtilBean utilBean = new UtilBean();
	UtilBean_DLNG utilBean_dlng = new UtilBean_DLNG();
	DateUtil utilDate = new DateUtil();

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
	    			if(callFlag.equalsIgnoreCase("TRUCK_TRANS_MST"))
	    			{
	    				getStateMst();
	    				fetchGovtStatNoList();
	    				getTruckTransList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("TRUCK_TYPE_MST"))
	    			{
	    				getTruckTypeList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("TRUCK_MST"))
	    			{
	    				getActiveTruckTypeList();
	    				getTruckTransporterList();
	    				getTruckList();
	    				getAllTruckTransList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("TRUCK_DRIVER_MST"))
	    			{
	    				getStateMst();
	    				getTruckTransporterList();
	    				getTruckDriverList();
	    				getAllTruckTransList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("LINK_TRUCK_TRANS"))
	    			{
	    				getTruckList();
	    				getTruckTransList();
	    				getLinkedTruckTransDtls();
	    				getAllTruckTransList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("LINK_DRIVER_TRANS"))
	    			{
	    				getTruckDriverList();
	    				getTruckTransList();
	    				getLinkedDriverTransDtls();
	    				getAllTruckTransList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("LINK_TRUCK_DRIVER"))
	    			{
	    				getTruckTransList();
	    				getLinkedTruckDriverTransDtls();
	    				getLinkedTrucktoDriverDtls();
	    			}
	    			else if(callFlag.equalsIgnoreCase("CHECKPOST_MST"))
	    			{
	    				getStateMst();
	    				getCheckPostList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("TRUCK_TRANS_CONTACT_DETAILS"))
					{
	    				getTruckTransList();
	    				getTruckTransAddrTypeDtl();
	    				getTruckTransContactDtl();
					}
	    			else if(callFlag.equalsIgnoreCase("FILLING_MST"))
	    			{
	    				comp_abbr = utilBean.getCompanyAbbr(conn,comp_cd);
	    				getCompanyOwnerDetail();
	    				getCounterPartyList();
	    				getFillingStationDtl();
	    			}
	    			else if(callFlag.equalsIgnoreCase("BAY_MST"))
	    			{
	    				comp_abbr = utilBean.getCompanyAbbr(conn,comp_cd);
	    				getFillingStationlist();
	    				getBaySlotDtls();
	    			}
	    			else if(callFlag.equalsIgnoreCase("CUST_PLANT_CHKPOST_LINK"))
	    			{
	    				comp_abbr = utilBean.getCompanyAbbr(conn,comp_cd);
	    				getCustomerList();
	    				getCustomerPlantList();
	    				getAvailableCheckpostlist();
	    				getCheckpostPlantLinkDtl();
	    				getCheckpostPlantDeLinkDtl();
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
	    	if(rset6 != null){try{rset6.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt6 != null){try{stmt6.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}
	
	public void getCheckpostPlantDeLinkDtl() 
	{
		String function_nm="getCheckpostPlantLinkDtl()";
		try
		{
			queryString = "SELECT COMPANY_CD,COUNTERPARTY_CD,ENTITY_TYPE,PLANT_SEQ_NO,CHKPOST_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY'),TO_CHAR(RELEASE_DT,'DD/MM/YYYY') "
					+ "FROM FMS_LINK_CHECKPOST_PLANT A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY_TYPE=? "
					+ "AND (TO_DATE(A.RELEASE_DT)<=TO_DATE(SYSDATE) AND A.RELEASE_DT IS NOT NULL) "
					+ "AND A.REV_SEQ=(SELECT MAX(B.REV_SEQ) FROM FMS_LINK_CHECKPOST_PLANT B WHERE "
					+ "	A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY_TYPE=B.ENTITY_TYPE "
					+ "	AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO)"
					+ "ORDER BY COUNTERPARTY_CD";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, "C");
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String company_cd = rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd = rset.getString(2)==null?"":rset.getString(2);
				String entity_typ = rset.getString(3)==null?"":rset.getString(3);
				String plant_seq_no = rset.getString(4)==null?"":rset.getString(4);
				String checkpost_cd = rset.getString(5)==null?"":rset.getString(5);
				String eff_dt = rset.getString(6)==null?"":rset.getString(6);
				String relese_dt = rset.getString(7)==null?"":rset.getString(7);
				
				VDL_LEGAL_ENTITY.add(utilBean.getCompanyAbbr(conn,company_cd));
				VDL_COUNTPTY_CD.add(countpty_cd);
				VDL_COUNTPTY_NM.add(utilBean.getCounterpartyName(conn,countpty_cd));
				VDL_DURATION.add(eff_dt+"-"+relese_dt);
				
				VDELINKED_PLANT_NAME.add(utilBean.getCounterpartyPlantName(conn,countpty_cd, company_cd, plant_seq_no, entity_typ));
				VDELINKED_PLANT_ABBR.add(utilBean.getCounterpartyPlantABBR(conn,countpty_cd, company_cd, plant_seq_no, entity_typ));
				VDELINKED_CHECKPOST_NM.add(utilBean_dlng.getCheckPostName(conn,checkpost_cd));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getAllTruckTransList()
	{
		String function_nm="getAllTruckTransList()";
		try
		{
			int index = 0;
			queryString = "SELECT TRUCK_TRANS_CD,TRUCK_TRANS_NAME,TRUCK_TRANS_ABBR "
					+ "FROM FMS_TRUCK_TRANSPORTER_MST ";
			queryString+= "ORDER BY TRUCK_TRANS_CD";
			stmt=conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				VALLTRUCK_TRANS_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VALLTRUCK_TRANS_NAME.add(rset.getString(2)==null?"":rset.getString(2));
				VALLTRUCK_TRANS_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCheckpostPlantLinkDtl() 
	{
		String function_nm="getCheckpostPlantLinkDtl()";
		try
		{
			queryString = "SELECT COMPANY_CD,COUNTERPARTY_CD,ENTITY_TYPE,PLANT_SEQ_NO,CHKPOST_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY') "
					+ "FROM FMS_LINK_CHECKPOST_PLANT A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY_TYPE=? "
					+ "AND (TO_DATE(A.RELEASE_DT)>TO_DATE(SYSDATE) OR A.RELEASE_DT IS NULL) "
					+ "AND A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_LINK_CHECKPOST_PLANT B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY_TYPE=B.ENTITY_TYPE "
					+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.CHKPOST_CD=B.CHKPOST_CD) "
					+ "AND A.REV_SEQ=(SELECT MAX(B.REV_SEQ) FROM FMS_LINK_CHECKPOST_PLANT B WHERE "
					+ "	A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY_TYPE=B.ENTITY_TYPE "
					+ "	AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.EFF_DT=B.EFF_DT)"
					+ "ORDER BY COUNTERPARTY_CD";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, "C");
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String company_cd = rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd = rset.getString(2)==null?"":rset.getString(2);
				String entity_typ = rset.getString(3)==null?"":rset.getString(3);
				String plant_seq_no = rset.getString(4)==null?"":rset.getString(4);
				String checkpost_cd = rset.getString(5)==null?"":rset.getString(5);
				String eff_dt = rset.getString(6)==null?"":rset.getString(6);
				
				VLEGAL_ENTITY.add(utilBean.getCompanyAbbr(conn,company_cd));
				VCOUNTPTY_CD.add(countpty_cd);
				VCOUNTPTY_NM.add(utilBean.getCounterpartyName(conn,countpty_cd));
				VENTITY_TYPE.add(entity_typ);
				VLINKED_PLANT_SEQ.add(plant_seq_no);
				VLINKED_CHKPOST_CD.add(checkpost_cd);
				VEFF_DT.add(eff_dt);
				
				VLINKED_PLANT_NAME.add(utilBean.getCounterpartyPlantName(conn,countpty_cd, company_cd, plant_seq_no, entity_typ));
				VLINKED_PLANT_ABBR.add(utilBean.getCounterpartyPlantABBR(conn,countpty_cd, company_cd, plant_seq_no, entity_typ));
				VLINKED_CHECKPOST_NM.add(utilBean_dlng.getCheckPostName(conn,checkpost_cd));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getAvailableCheckpostlist() 
	{
		String function_nm="getAvailableCheckpostlist()";
		try
		{
			queryString = "SELECT CHKPOST_CD,CHKPOST_NAME "
					+ "FROM FMS_CHECKPOST_MST "
					+ "WHERE ACTIVE_FLAG=? "
					+ "ORDER BY CHKPOST_CD";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, "Y");
			rset = stmt.executeQuery();
			while(rset.next())
			{
				VCHECKPOST_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VCHECKPOST_NAME.add(rset.getString(2)==null?"":rset.getString(2));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}


	public void getCustomerPlantList() 
	{

		String function_nm="getCustomerPlantList()";

		try
		{
			queryString="SELECT COUNTERPARTY_CD,ENTITY,SEQ_NO,PLANT_NAME,PLANT_ABBR "
					+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
					+ "WHERE ENTITY=? AND COUNTERPARTY_CD=? AND COMPANY_CD=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, "C");
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, comp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String plant_seq = rset.getString(3)==null?"0":rset.getString(3);
				VPLANT_SEQ_NO.add(rset.getString(3)==null?"":rset.getString(3));
				VPLANT_NAME.add(rset.getString(4)==null?"":rset.getString(4));
				VPLANT_ABBR.add(rset.getString(5)==null?"":rset.getString(5));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getCustomerList() 
	{
		String function_nm="getCustomerList()";
		try
		{
			utilBean.getAllEntityCounterpartyList(conn,"KYC",comp_cd,"C");
			VCOUNTERPARTY_CD= utilBean.getCOUNTERPARTY_CD();
			VCOUNTERPARTY_NM = utilBean.getCOUNTERPARTY_NM();
			VCOUNTERPARTY_ABBR = utilBean.getCOUNTERPARTY_ABBR();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}


	public void getBaySlotDtls() 
	{
		String function_nm="getBaySlotDtls()";
		try
		{
			queryString = "SELECT FILL_STATION_CD,BAY_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY'),BAY_NAME,ACTIVE_FLAG,SLOT_CALD_TYPE,SLOT_START_TIME,SLOT_INTERVAL "
					+ "FROM FMS_BAY_SLOT_MST ";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String fill_station_cd = rset.getString(1)==null?"":rset.getString(1);
				String bay_cd = rset.getString(2)==null?"":rset.getString(2);
				String eff_dt = rset.getString(3)==null?"":rset.getString(3);
				String bay_name = rset.getString(4)==null?"":rset.getString(4);
				String status = rset.getString(5)==null?"":rset.getString(5);
				String slot_cald_type = rset.getString(6)==null?"":rset.getString(6);
				String slot_start_time = rset.getString(7)==null?"":rset.getString(7);
				String slot_interval = rset.getString(8)==null?"":rset.getString(8);
				
				VFILLING_STATION_CD.add(fill_station_cd);
				VFILLING_STATION_NAME.add(utilBean_dlng.getFillStationName(conn, fill_station_cd));
				VBAY_CD.add(bay_cd);
				VEFF_DT.add(eff_dt);
				VBAY_NAME.add(bay_name);
				VSTATUS.add(status);
				VSLOT_CALD_TYPE.add(slot_cald_type);
				VSLOT_START_TIME.add(slot_start_time);
				VSLOT_INTERVAL.add(slot_interval);
			}
			stmt.close();
			rset.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getFillingStationlist() 
	{
		String function_nm="getFillingStationlist()";
		try
		{
			queryString = "SELECT FILL_STATION_CD,FILL_STATION_NAME,FILL_STATION_ABBR "
					+ "FROM FMS_FILLING_STATION_MST "
					+ "WHERE ACTIVE_FLAG=?";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, "Y");
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String fill_station_cd = rset.getString(1)==null?"":rset.getString(1);
				String fill_station_nm = rset.getString(2)==null?"":rset.getString(2);
				String fill_station_abbr = rset.getString(3)==null?"":rset.getString(3);
				
				VFILL_STATION_CD.add(fill_station_cd);
				VFILL_STATION_NM.add(fill_station_nm);
				VFILL_STATION_ABBR.add(fill_station_abbr);
			}
			stmt.close();
			rset.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getFillingStationDtl() 
	{
		String function_nm="getFillingStationDtl()";
		try
		{
			queryString = "SELECT COUNTERPARTY_CD,FILL_STATION_CD,FILL_STATION_NAME,FILL_STATION_ABBR,TO_CHAR(EFF_DT,'DD/MM/YYYY'),ACTIVE_FLAG "
					+ "FROM FMS_FILLING_STATION_MST "
					//+ "WHERE COMPANY_CD=? "
					+ "ORDER BY FILL_STATION_CD";
			stmt = conn.prepareStatement(queryString);
			//stmt.setString(1, comp_cd);
			//stmt.setString(2, "T");
			//stmt.setString(3, counterparty_cd);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				//String company_cd = rset.getString(1)==null?"":rset.getString(1);
				String countpty_cd = rset.getString(1)==null?"":rset.getString(1);
				String fill_station_cd = rset.getString(2)==null?"":rset.getString(2);
				String fill_station_nm = rset.getString(3)==null?"":rset.getString(3);
				String fill_station_abbr = rset.getString(4)==null?"":rset.getString(4);
				String eff_dt = rset.getString(5)==null?"":rset.getString(5);
				String status = rset.getString(6)==null?"":rset.getString(6);
				
				VCOUNTPTY_CD.add(countpty_cd);
				VCOUNTPTY_NM.add(utilBean.getCounterpartyName(conn,countpty_cd));
				
				VFILL_STATION_CD.add(fill_station_cd);
				VFILL_STATION_NM.add(fill_station_nm);
				VFILL_STATION_ABBR.add(fill_station_abbr);
				VEFF_DT.add(eff_dt);
				VSTATUS.add(status);
			}
			stmt.close();
			rset.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getCounterPartyList() 
	{
		String function_nm="getCounterPartyList()";
		try
		{
			queryString = "SELECT COUNTERPARTY_CD,COUNTERPARTY_NM,COUNTERPARTY_ABBR "
					+ "FROM FMS_COUNTERPARTY_MST A "
					+ "WHERE STATUS='Y' AND KYC='Y' "
					+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COUNTERPARTY_MST B "
					+ "WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD) "
					+ "ORDER BY COUNTERPARTY_NM";
			stmt = conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				VCOUNTERPARTY_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VCOUNTERPARTY_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VCOUNTERPARTY_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
			}
			stmt.close();
			rset.close();
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
			queryString="SELECT COMPANY_CD,COMPANY_NM,COMPANY_ABBR "
					+ "FROM FMS_COMPANY_OWNER_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COMPANY_OWNER_MST B WHERE A.COMPANY_CD=B.COMPANY_CD)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				VBO_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VBO_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VBO_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getTruckTransContactDtl() 
	{
		String function_nm="getTruckTransContactDtl()";

		try
		{
			queryString="SELECT SEQ_NO,TO_CHAR(EFF_DT,'DD/MM/YYYY'),CONTACT_PERSON,DESIGNATION,MOBILE,EMAIL,"
					+ "NOM_FLAG,INV_FLAG,FM_FLAG,PM_FLAG,JT_FLAG,OTHER_FLAG,ACTIVE_FLAG,RM_FLAG "
					+ "FROM FMS_TRUCK_TRANS_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND TRUCK_TRANS_CD=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_TRUCK_TRANS_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.TRUCK_TRANS_CD=B.TRUCK_TRANS_CD AND A.SEQ_NO=B.SEQ_NO) "
					+ "GROUP BY SEQ_NO,TO_CHAR(EFF_DT,'DD/MM/YYYY'),CONTACT_PERSON,DESIGNATION,MOBILE,EMAIL,"
					+ "NOM_FLAG,INV_FLAG,FM_FLAG,PM_FLAG,JT_FLAG,OTHER_FLAG,ACTIVE_FLAG,RM_FLAG ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, truck_trans_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String seq_no=rset.getString(1)==null?"":rset.getString(1);
				String eff_dt=rset.getString(2)==null?"":rset.getString(2);
				
				VSEQ_NO.add(rset.getString(1)==null?"":rset.getString(1));
				VCONTACT_EFF_DT.add(rset.getString(2)==null?"":rset.getString(2));
				VPERSON_NM.add(rset.getString(3)==null?"":rset.getString(3));
				VDESIGNATION.add(rset.getString(4)==null?"":rset.getString(4));
				VMOBILE.add(rset.getString(5)==null?"":rset.getString(5));
				VEMAIL.add(rset.getString(6)==null?"":rset.getString(6));
				VNOM.add(rset.getString(7)==null?"N":rset.getString(7));
				VINV.add(rset.getString(8)==null?"N":rset.getString(8));
				VFM.add(rset.getString(9)==null?"N":rset.getString(9));
				VPM.add(rset.getString(10)==null?"N":rset.getString(10));
				VJT.add(rset.getString(11)==null?"N":rset.getString(11));
				VOTHER.add(rset.getString(12)==null?"N":rset.getString(12));
				VCONTACT_STATUS.add(rset.getString(13)==null?"N":rset.getString(13));
				VRM.add(rset.getString(14)==null?"N":rset.getString(14));
				
				String temp_add_flag="";
				String temp_add_addr_line="";
				String temp_phone="";
				String temp_fax1="";
				String temp_fax2="";
				String temp_flag="";
				
				int index=0;
				queryString1="SELECT ADDR_FLAG,ADDL_ADDR_LINE,PHONE,FAX_1,FAX_2,ADDR_IS_ACTIVE "
						+ "FROM FMS_TRUCK_TRANS_CONTACT_MST "
						+ "WHERE COMPANY_CD=? AND TRUCK_TRANS_CD=? "
						+ "AND SEQ_NO=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY')";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, truck_trans_cd);
				stmt1.setString(3, seq_no);
				stmt1.setString(4, eff_dt);
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					index+=1;
					
					String add_flag = rset1.getString(1)==null?"":rset1.getString(1);
					String add_addr_line = rset1.getString(2)==null?"":rset1.getString(2);
					String phone = rset1.getString(3)==null?"":rset1.getString(3);
					String fax1 = rset1.getString(4)==null?"":rset1.getString(4);
					String fax2 = rset1.getString(5)==null?"":rset1.getString(5);
					String flag = rset1.getString(6)==null?"N":rset1.getString(6);
					
					String add_type_nm="Registered";
					
					temp_add_flag=temp_add_flag+"@@"+add_flag;
					temp_add_addr_line=temp_add_addr_line+"@@"+add_addr_line;
					temp_phone=temp_phone+"@@"+phone;
					temp_fax1=temp_fax1+"@@"+fax1;
					temp_fax2=temp_fax2+"@@"+fax2;
					temp_flag=temp_flag+"@@"+flag;
					
					VADDR_FLAG.add(add_flag);
					VADDR_TYPE.add(add_type_nm);
					VADD_ADDRESS.add(add_addr_line);
					VPHONE.add(phone);
					VFAX1.add(fax1);
					VFAX2.add(fax2);
					VFLAG.add(flag);
				}
				rset1.close();
				stmt1.close();
				
				VINDEX.add(index);
				
				VTEMP_ADDR_FLAG.add(temp_add_flag);
				VTEMP_ADD_ADDRESS.add(temp_add_addr_line);
				VTEMP_PHONE.add(temp_phone);
				VTEMP_FAX1.add(temp_fax1);
				VTEMP_FAX2.add(temp_fax2);
				VTEMP_FLAG.add(temp_flag);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getCheckPostList() 
	{
		String function_nm="getCheckPostList()";
		try
		{
			queryString = "SELECT CHKPOST_CD,CHKPOST_NAME,STATE_CODE,TO_CHAR(EFF_DT,'DD/MM/YYYY'),ACTIVE_FLAG "
					+ "FROM FMS_CHECKPOST_MST "
					+ "ORDER BY CHKPOST_CD";
			stmt=conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				VCHECKPOST_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VCHECKPOST_NAME.add(rset.getString(2)==null?"":rset.getString(2));
				String checkpost_state_cd = rset.getString(3)==null?"":rset.getString(3);
				VCHECKPOST_STATE_CD.add(checkpost_state_cd);
				VCHECKPOST_STATE_NM.add(utilBean.getStateName(conn,checkpost_state_cd));
				VEFF_DT.add(rset.getString(4)==null?"":rset.getString(4));
				VSTATUS.add(rset.getString(5)==null?"":rset.getString(5));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getLinkedTrucktoDriverDtls() 
	{
		String function_nm="getLinkedTrucktoDriverDtls()";
		try
		{
			for(int i=0; i<VTRUCK_TRANS_CD.size(); i++)
			{
				int driver_index_linked = 0;
				String truck_trans_cd=""+VTRUCK_TRANS_CD.elementAt(i);
				queryString = "SELECT TRUCK_CD,DRIVER_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY'),TO_CHAR(RELEASE_DT,'DD/MM/YYYY'),REMARKS,LINK_SEQ "
						+ "FROM FMS_TRUCK_DRIVER_LINK A "
						+ "WHERE A.DRIVER_CD IN (SELECT DRIVER_CD FROM FMS_TRUCK_DRIVER_TRANS_LINK B WHERE B.TRUCK_TRANS_CD=? "
						+ "AND B.LINK_SEQ  = (SELECT MAX(C.LINK_SEQ) FROM FMS_TRUCK_DRIVER_TRANS_LINK C WHERE B.DRIVER_CD=C.DRIVER_CD) "
						+ "AND (TO_DATE(B.RELEASE_DT)>TO_DATE(SYSDATE) OR B.RELEASE_DT IS NULL)) "
						+ "AND (TO_DATE(A.RELEASE_DT)>TO_DATE(SYSDATE) OR A.RELEASE_DT IS NULL) "
						+ "AND A.TRUCK_CD IN (SELECT TRUCK_CD FROM FMS_TRUCK_TRANSPORTER_LINK D WHERE D.TRUCK_TRANS_CD=? "
						+ "AND D.LINK_SEQ  = (SELECT MAX(E.LINK_SEQ) FROM FMS_TRUCK_TRANSPORTER_LINK E WHERE D.TRUCK_CD=E.TRUCK_CD) "
						+ "AND (TO_DATE(D.RELEASE_DT)>TO_DATE(SYSDATE) OR D.RELEASE_DT IS NULL)) "
						+ "AND A.LINK_SEQ=(SELECT MAX(F.LINK_SEQ) FROM FMS_TRUCK_DRIVER_LINK F WHERE A.DRIVER_CD=F.DRIVER_CD) "
						+ "ORDER BY DRIVER_CD";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, truck_trans_cd);
				stmt.setString(2, truck_trans_cd);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					driver_index_linked+=1;
					String truck_cd_linked = rset.getString(1)==null?"":rset.getString(1);
					String driver_cd_linked = rset.getString(2)==null?"":rset.getString(2);
					
					VTRUCK_CD_LINKED.add(truck_cd_linked);
					VTRUCK_REG_NO_LINKED.add(utilBean_dlng.getTruckRegNo(conn,truck_cd_linked));
					VDRIVER_CD_LINKED.add(driver_cd_linked);
					VEFF_DT_LINKED.add(rset.getString(3)==null?"":rset.getString(3));
					VRELEASE_DT_LINKED.add(rset.getString(4)==null?"":rset.getString(4));
					VREMARK_LINKED.add(rset.getString(5)==null?"":rset.getString(5));
					VLINK_SEQ.add(rset.getString(6)==null?"":rset.getString(6));
					
					String driver_status_linked="";
					String driver_license_linked="";
					String driver_license_from_dt_linked="";
					String driver_license_to_dt_linked="";
					String driver_name_linked = "";
					queryString1="SELECT DRIVER_STATUS,LICENCE_NO,TO_CHAR(LICENCE_FROM_DT,'DD/MM/YYYY'),TO_CHAR(LICENCE_TO_DT,'DD/MM/YYYY'),DRIVER_NAME FROM FMS_TRUCK_DRIVER_MST "
							+ "WHERE DRIVER_CD=? ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, driver_cd_linked);
					rset1 = stmt1.executeQuery();
					if(rset1.next())
					{
						driver_status_linked = rset1.getString(1)==null?"":rset1.getString(1);
						driver_license_linked = rset1.getString(2)==null?"":rset1.getString(2);
						driver_license_from_dt_linked = rset1.getString(3)==null?"":rset1.getString(3);
						driver_license_to_dt_linked = rset1.getString(4)==null?"":rset1.getString(4);
						driver_name_linked = rset1.getString(5)==null?"":rset1.getString(5);
					}
					rset1.close();
					stmt1.close();
					
					VDRIVER_STATUS_LINKED.add(driver_status_linked);
					VDRIVER_LICENSE_LINKED.add(driver_license_linked);
					VDRIVER_LICENSE_DURATION_LINKED.add(driver_license_from_dt_linked+"-"+driver_license_to_dt_linked);
					VDRIVER_NAME_LINKED.add(driver_name_linked);
					
					String last_release_dt=getLastReleaseDt("DRIVER_CD","FMS_TRUCK_DRIVER_LINK",driver_cd_linked);
					VLINKED_LAST_RELEASE_DT.add(last_release_dt);
				}
				rset.close();
				stmt.close();
				VDRIVER_INDEX_LINKED.add(driver_index_linked);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getLinkedTruckDriverTransDtls() 
	{
		String function_nm="getLinkedTruckDriverTransDtls()";
		try
		{
			for(int i=0; i<VTRUCK_TRANS_CD.size(); i++)
			{
				String truck_trans_cd=""+VTRUCK_TRANS_CD.elementAt(i);
				
				int driver_index = 0;
				queryString = "SELECT TRUCK_TRANS_CD,DRIVER_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY'),TO_CHAR(RELEASE_DT,'DD/MM/YYYY'),REMARKS "
						+ "FROM FMS_TRUCK_DRIVER_TRANS_LINK A "
						+ "WHERE A.TRUCK_TRANS_CD=? AND (TO_DATE(A.RELEASE_DT)>TO_DATE(SYSDATE) OR A.RELEASE_DT IS NULL) AND A.DRIVER_CD NOT IN (SELECT DRIVER_CD FROM FMS_TRUCK_DRIVER_LINK B WHERE (TO_DATE(B.RELEASE_DT)>TO_DATE(SYSDATE) OR B.RELEASE_DT IS NULL)) "
						+ "AND A.LINK_SEQ=(SELECT MAX(C.LINK_SEQ) FROM FMS_TRUCK_DRIVER_TRANS_LINK C WHERE A.DRIVER_CD=C.DRIVER_CD) "
						+ "ORDER BY A.TRUCK_TRANS_CD";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, truck_trans_cd);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					driver_index+=1;
					String linked_driver_cd=rset.getString(2)==null?"":rset.getString(2);
					VLINK_DRIVER_CD.add(linked_driver_cd);
					VLINK_DRIVER_TRANS_EFF_DT.add(rset.getString(3)==null?"":rset.getString(3));
					
					String linked_driver_status="";
					String linked_driver_license="";
					String linked_driver_license_from_dt="";
					String linked_driver_license_to_dt="";
					String linked_driver_name = "";
					queryString1="SELECT DRIVER_STATUS,LICENCE_NO,TO_CHAR(LICENCE_FROM_DT,'DD/MM/YYYY'),TO_CHAR(LICENCE_TO_DT,'DD/MM/YYYY'),DRIVER_NAME FROM FMS_TRUCK_DRIVER_MST "
							+ "WHERE DRIVER_CD=? ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, linked_driver_cd);
					rset1 = stmt1.executeQuery();
					if(rset1.next())
					{
						linked_driver_status = rset1.getString(1)==null?"":rset1.getString(1);
						linked_driver_license = rset1.getString(2)==null?"":rset1.getString(2);
						linked_driver_license_from_dt = rset1.getString(3)==null?"":rset1.getString(3);
						linked_driver_license_to_dt = rset1.getString(4)==null?"":rset1.getString(4);
						linked_driver_name = rset1.getString(5)==null?"":rset1.getString(5);
					}
					rset1.close();
					stmt1.close();
					
					VLINKED_DRIVER_STATUS.add(linked_driver_status);
					VLINKED_DRIVER_LICENSE.add(linked_driver_license);
					VLINKED_DRIVER_LICENSE_DURATION.add(linked_driver_license_from_dt+"-"+linked_driver_license_to_dt);
					VLINKED_DRIVER_NAME.add(linked_driver_name);
					
					String last_release_dt=getLastReleaseDt("DRIVER_CD","FMS_TRUCK_DRIVER_LINK",linked_driver_cd);
					VLAST_RELEASE_DT.add(last_release_dt);
				}
				rset.close();
				stmt.close();
				
				VDRIVER_INDEX.add(driver_index);
				
				int truck_index=0;
				queryString = "SELECT TRUCK_TRANS_CD,TRUCK_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY'),TO_CHAR(RELEASE_DT,'DD/MM/YYYY'),REMARKS "
						+ "FROM FMS_TRUCK_TRANSPORTER_LINK A "
						+ "WHERE A.TRUCK_TRANS_CD=? AND (TO_DATE(A.RELEASE_DT)>TO_DATE(SYSDATE) OR A.RELEASE_DT IS NULL) "
						+ "AND A.LINK_SEQ=(SELECT MAX(B.LINK_SEQ) FROM FMS_TRUCK_TRANSPORTER_LINK B WHERE A.TRUCK_CD=B.TRUCK_CD ) "
						+ "ORDER BY TRUCK_TRANS_CD";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, truck_trans_cd);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					truck_index+=1;
					String linked_truck_cd=rset.getString(2)==null?"":rset.getString(2);
					VLINK_TRUCK_CD.add(linked_truck_cd);
					VLINKED_TRUCK_TRANS_CD.add(rset.getString(1)==null?"":rset.getString(1));
					
					String linked_truck_status="";
					String linked_truck_reg_no="";
					
					queryString1="SELECT ACTIVE_FLAG,TRUCK_REG_NUM FROM FMS_TRUCK_MST "
							+ "WHERE TRUCK_CD=? ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, linked_truck_cd);
					rset1 = stmt1.executeQuery();
					if(rset1.next())
					{
						linked_truck_status = rset1.getString(1)==null?"":rset1.getString(1);
						linked_truck_reg_no = rset1.getString(2)==null?"":rset1.getString(2);
					}
					rset1.close();
					stmt1.close();
					
					VLINKED_TRUCK_STATUS.add(linked_truck_status);
					VLINKED_TRUCK_REG_NO.add(linked_truck_reg_no);
					
				}
				rset.close();
				stmt.close();
				
				VTRUCK_INDEX.add(truck_index);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getLinkedDriverTransDtls() 
	{

		String function_nm="getLinkedDriverTransDtls()";
		try
		{
			queryString = "SELECT TRUCK_TRANS_CD,DRIVER_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY'),TO_CHAR(RELEASE_DT,'DD/MM/YYYY'),REMARKS,LINK_SEQ "
					+ "FROM FMS_TRUCK_DRIVER_TRANS_LINK A "
					+ "WHERE (TO_DATE(A.RELEASE_DT)>TO_DATE(SYSDATE) OR RELEASE_DT IS NULL) "
					+ "AND A.LINK_SEQ=(SELECT MAX(B.LINK_SEQ) FROM FMS_TRUCK_DRIVER_TRANS_LINK B WHERE A.DRIVER_CD=B.DRIVER_CD ) "
					+ "ORDER BY DRIVER_CD";
			stmt=conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String linked_driver_cd=rset.getString(2)==null?"":rset.getString(2);
				String linked_trans_cd=rset.getString(1)==null?"":rset.getString(1);
				
				VLINKED_TRUCK_TRANS_CD.add(linked_trans_cd);
				VLINKED_TRUCK_TRANS_NAME.add(utilBean_dlng.getTruckTransName(conn,linked_trans_cd));
				VLINK_DRIVER_CD.add(linked_driver_cd);
				VLINK_DRIVER_NAME.add(utilBean_dlng.getDriverName(conn,linked_driver_cd));
				VLINKED_EFF_DT.add(rset.getString(3)==null?"":rset.getString(3));
				VLINKED_RELEASE_DT.add(rset.getString(4)==null?"":rset.getString(4));
				VREMARK.add(rset.getString(5)==null?"":rset.getString(5));
				VLINK_SEQ.add(rset.getString(6)==null?"":rset.getString(6));
				
				String linked_driver_status="";
				String linked_driver_license="";
				String linked_driver_license_from_dt="";
				String linked_driver_license_to_dt="";
				
				queryString1="SELECT DRIVER_STATUS,LICENCE_NO,TO_CHAR(LICENCE_FROM_DT,'DD/MM/YYYY'),TO_CHAR(LICENCE_TO_DT,'DD/MM/YYYY') FROM FMS_TRUCK_DRIVER_MST "
						+ "WHERE DRIVER_CD=? ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, linked_driver_cd);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					linked_driver_status = rset1.getString(1)==null?"":rset1.getString(1);
					linked_driver_license = rset1.getString(2)==null?"":rset1.getString(2);
					linked_driver_license_from_dt = rset1.getString(3)==null?"":rset1.getString(3);
					linked_driver_license_to_dt = rset1.getString(4)==null?"":rset1.getString(4);
				}
				rset1.close();
				stmt1.close();
				
				VLINKED_DRIVER_STATUS.add(linked_driver_status);
				VLINKED_DRIVER_LICENSE.add(linked_driver_license);
				VLINKED_DRIVER_LICENSE_DURATION.add(linked_driver_license_from_dt+"-"+linked_driver_license_to_dt);
				
				String last_release_dt=getLastReleaseDt("DRIVER_CD","FMS_TRUCK_DRIVER_TRANS_LINK",linked_driver_cd);
				VLINKED_LAST_RELEASE_DT.add(last_release_dt);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getLinkedTruckTransDtls() 
	{
		String function_nm="getLinkedTruckTransDtls()";
		try
		{
			queryString = "SELECT TRUCK_TRANS_CD,TRUCK_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY'),TO_CHAR(RELEASE_DT,'DD/MM/YYYY'),REMARKS,LINK_SEQ "
					+ "FROM FMS_TRUCK_TRANSPORTER_LINK A "
					+ "WHERE (TO_DATE(A.RELEASE_DT)>TO_DATE(SYSDATE) OR A.RELEASE_DT IS NULL) "
					+ "AND A.LINK_SEQ=(SELECT MAX(B.LINK_SEQ) FROM FMS_TRUCK_TRANSPORTER_LINK B WHERE A.TRUCK_CD=B.TRUCK_CD) "
					+ "ORDER BY TRUCK_CD";
			stmt=conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String linked_truck_cd=rset.getString(2)==null?"":rset.getString(2);
				String linked_trans_cd=rset.getString(1)==null?"":rset.getString(1);
				VLINKED_TRUCK_TRANS_CD.add(linked_trans_cd);
				VLINKED_TRUCK_TRANS_NAME.add(utilBean_dlng.getTruckTransName(conn,linked_trans_cd));
				VLINK_TRUCK_CD.add(linked_truck_cd);
				VLINK_TRUCK_REG_NO.add(utilBean_dlng.getTruckRegNo(conn,linked_truck_cd));
				VLINKED_EFF_DT.add(rset.getString(3)==null?"":rset.getString(3));
				VLINKED_RELEASE_DT.add(rset.getString(4)==null?"":rset.getString(4));
				VREMARK.add(rset.getString(5)==null?"":rset.getString(5));
				VLINK_SEQ.add(rset.getString(6)==null?"":rset.getString(6));
				
				String linked_truck_status="";
				queryString1="SELECT ACTIVE_FLAG FROM FMS_TRUCK_MST "
						+ "WHERE TRUCK_CD=? ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, linked_truck_cd);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					linked_truck_status = rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
				VLINKED_TRUCK_STATUS.add(linked_truck_status);
				
				String last_release_dt=getLastReleaseDt("TRUCK_CD","FMS_TRUCK_TRANSPORTER_LINK",linked_truck_cd);
				VLINKED_LAST_RELEASE_DT.add(last_release_dt);
				
				String max_nominated_date = getMaxNominationDate(linked_truck_cd,linked_trans_cd);
				VMAX_NOMINATION_DT.add(max_nominated_date);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public String getMaxNominationDate(String truck_cd,String trans_cd)
	{
		String function_nm="getMaxNominationDate()";
		String dt="";
		try
		{	
			int cnt=0;
			String queryString5 = "SELECT TO_CHAR(MAX(GAS_DT), 'DD/MM/YYYY') AS MAX_GAS_DT "
                    + "FROM (SELECT GAS_DT FROM FMS_DLNG_SELLER_NOM_DTL WHERE COMPANY_CD = ? AND TRUCK_CD = ? AND TRUCK_TRANS_CD = ? "
                    + "UNION ALL "
                    + "SELECT GAS_DT FROM FMS_DLNG_BUYER_NOM_DTL WHERE COMPANY_CD = ? AND TRUCK_CD = ? AND TRUCK_TRANS_CD = ?)";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(++cnt, comp_cd);
			stmt5.setString(++cnt, truck_cd);
			stmt5.setString(++cnt, trans_cd);
			stmt5.setString(++cnt, comp_cd);
			stmt5.setString(++cnt, truck_cd);
			stmt5.setString(++cnt, trans_cd);
			rset5=stmt5.executeQuery();
			while(rset5.next())
			{
				dt=rset5.getString(1)==null?"":rset5.getString(1);
			}
			rset5.close();
			stmt5.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return dt;
	}

	public void getTruckDriverList() 
	{
		String function_nm="getTruckDriverList()";
		try
		{
			queryString = "SELECT DRIVER_CD,DRIVER_NAME,DRIVER_ADDR,TO_CHAR(DRIVER_DOB,'DD/MM/YYYY'),DRIVER_STATUS,DRIVER_MOBILE,LICENCE_NO,"
					+ "LICENCE_TYPE,TO_CHAR(LICENCE_FROM_DT,'DD/MM/YYYY'),TO_CHAR(LICENCE_TO_DT,'DD/MM/YYYY'),LICENCE_ISSUE_STATE,LICENCE_FILE_NAME,TO_CHAR(EFF_DT,'DD/MM/YYYY') "
					+  "FROM FMS_TRUCK_DRIVER_MST A ";
			if(callFlag.equals("LINK_DRIVER_TRANS"))
			{
				queryString+="WHERE DRIVER_CD NOT IN (SELECT DRIVER_CD FROM FMS_TRUCK_DRIVER_TRANS_LINK B WHERE (TO_DATE(B.RELEASE_DT)>TO_DATE(SYSDATE) OR B.RELEASE_DT IS NULL)) ";
			}	
			
			queryString+= "ORDER BY DRIVER_CD";
			stmt=conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String driver_cd=rset.getString(1)==null?"":rset.getString(1);
				VDRIVER_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VDRIVER_NAME.add(rset.getString(2)==null?"":rset.getString(2));
				VDRIVER_ADDR.add(rset.getString(3)==null?"":rset.getString(3));
				VDRIVER_DOB.add(rset.getString(4)==null?"":rset.getString(4));
				VDRIVER_STATUS.add(rset.getString(5)==null?"":rset.getString(5));
				VDRIVER_MOBILE.add(rset.getString(6)==null?"":rset.getString(6));
				VLICENSE_NO.add(rset.getString(7)==null?"":rset.getString(7));
				VLICENSE_TYPE.add(rset.getString(8)==null?"":rset.getString(8));
				VLICENSE_FROM_DT.add(rset.getString(9)==null?"":rset.getString(9));
				VLICENSE_TO_DT.add(rset.getString(10)==null?"":rset.getString(10));
				VLICENSE_ISSUE_STATE_CD.add(rset.getString(11)==null?"":rset.getString(11));
				VLICENSE_ISSUE_STATE_NAME.add(""+utilBean.getStateName(conn,rset.getString(11)==null?"":rset.getString(11)));
				VLICENSE_FILE_NAME.add(rset.getString(12)==null?"":rset.getString(12));
				VDRIVER_EFF_DT.add(rset.getString(13)==null?"":rset.getString(13));
				
				String last_release_dt=getLastReleaseDt("DRIVER_CD","FMS_TRUCK_DRIVER_TRANS_LINK",driver_cd);
				VLAST_RELEASE_DT.add(last_release_dt);
				
				String linked_truck_trans_cd="";
				String linked_truck_trans_eff_dt="";
				String linked_flag="N";
				queryString2 = "SELECT TRUCK_TRANS_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY') "
						+ "FROM FMS_TRUCK_DRIVER_TRANS_LINK A "
						+ "WHERE (TO_DATE(A.RELEASE_DT)>TO_DATE(SYSDATE) OR RELEASE_DT IS NULL) AND DRIVER_CD=? "
						+ "AND A.LINK_SEQ=(SELECT MAX(B.LINK_SEQ) FROM FMS_TRUCK_DRIVER_TRANS_LINK B WHERE A.DRIVER_CD=B.DRIVER_CD) "
						+ "ORDER BY DRIVER_CD";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, driver_cd);
				rset2 = stmt2.executeQuery();
				if(rset2.next())
				{
					linked_truck_trans_cd=rset2.getString(1)==null?"":rset2.getString(1);
					linked_truck_trans_eff_dt=rset2.getString(2)==null?"":rset2.getString(2);
					linked_flag="Y";
				}
				rset2.close();
				stmt2.close();
				
				VLINKEDTRUCKTRANSNAME.add(utilBean_dlng.getTruckTransName(conn,linked_truck_trans_cd));
				VLINKEDTRUCKTRANS.add(linked_truck_trans_cd);
				VLINKEDEFFDT.add(linked_truck_trans_eff_dt);
				VLINKEDFLAG.add(linked_flag);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getTruckList() 
	{
		String function_nm="getTruckList()";
		try
		{
			queryString = "SELECT TRUCK_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY'),TRUCK_REG_NUM,TRUCK_TYPE,TRUCK_VOL_M3,TRUCK_VOL_MT,TRUCK_LOAD_CAP,ACTIVE_FLAG "
					+ "FROM FMS_TRUCK_MST A ";
			if(callFlag.equals("LINK_TRUCK_TRANS"))
			{
				queryString+="WHERE TRUCK_CD NOT IN (SELECT TRUCK_CD FROM FMS_TRUCK_TRANSPORTER_LINK B WHERE (TO_DATE(B.RELEASE_DT)>TO_DATE(SYSDATE) OR B.RELEASE_DT IS NULL)) ";
			}	
			queryString+= "ORDER BY TRUCK_CD";
			stmt=conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String truck_cd=rset.getString(1)==null?"":rset.getString(1);
				VTRUCK_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VEFF_DT.add(rset.getString(2)==null?"":rset.getString(2));
				VTRUCK_REG_NO.add(rset.getString(3)==null?"":rset.getString(3));
				VTRUCK_TYPE.add(rset.getString(4)==null?"":rset.getString(4));
				VTRUCK_VOL_M3.add(rset.getString(5)==null?"":rset.getString(5));
				VTRUCK_VOL_MT.add(rset.getString(6)==null?"":rset.getString(6));
				VTRUCK_LOAD_CAP.add(rset.getString(7)==null?"":rset.getString(7));
				VTRUCK_STATUS.add(rset.getString(8)==null?"":rset.getString(8));
				
				String last_release_dt=getLastReleaseDt("TRUCK_CD","FMS_TRUCK_TRANSPORTER_LINK",truck_cd);
				VLAST_RELEASE_DT.add(last_release_dt);
				
				String linked_truck_trans_cd="";
				String linked_truck_trans_eff_dt="";
				String linked_flag="N";
				queryString2 = "SELECT TRUCK_TRANS_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY') "
						+ "FROM FMS_TRUCK_TRANSPORTER_LINK A "
						+ "WHERE (TO_DATE(A.RELEASE_DT)>TO_DATE(SYSDATE) OR RELEASE_DT IS NULL) AND TRUCK_CD=? "
						+ "AND A.LINK_SEQ=(SELECT MAX(B.LINK_SEQ) FROM FMS_TRUCK_TRANSPORTER_LINK B WHERE A.TRUCK_CD=B.TRUCK_CD) "
						+ "ORDER BY TRUCK_CD";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, truck_cd);
				rset2 = stmt2.executeQuery();
				if(rset2.next())
				{
					linked_truck_trans_cd=rset2.getString(1)==null?"":rset2.getString(1);
					linked_truck_trans_eff_dt=rset2.getString(2)==null?"":rset2.getString(2);
					linked_flag="Y";
				}
				rset2.close();
				stmt2.close();
				
				VLINKEDTRUCKTRANSNAME.add(utilBean_dlng.getTruckTransName(conn,linked_truck_trans_cd));
				VLINKEDTRUCKTRANS.add(linked_truck_trans_cd);
				VLINKEDEFFDT.add(linked_truck_trans_eff_dt);
				VLINKEDFLAG.add(linked_flag);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getActiveTruckTypeList() 
	{
		String function_nm="getActiveTruckTypeList()";
		try
		{
			queryString = "SELECT TRUCK_TYPE "
					+ "FROM FMS_TRUCK_TYPE_MST "
					+ "WHERE EFF_DT<=SYSDATE AND ACTIVE_FLAG='Y' "
					+ "ORDER BY TRUCK_TYPE";
			stmt=conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				VACTIVE_TRUCK_TYPE.add(rset.getString(1)==null?"":rset.getString(1));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getTruckTypeList() 
	{
		String function_nm="getTruckTypeList()";
		try
		{
			queryString = "SELECT TRUCK_TYPE,TO_CHAR(EFF_DT,'DD/MM/YYYY'),ACTIVE_FLAG,TRUCK_VOL_M3,TRUCK_VOL_MT,TRUCK_LOAD_CAP "
					+ "FROM FMS_TRUCK_TYPE_MST "
					+ "ORDER BY TRUCK_TYPE";
			stmt=conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				VTRUCK_TYPE.add(rset.getString(1)==null?"":rset.getString(1));
				VEFF_DT.add(rset.getString(2)==null?"":rset.getString(2));
				VSTATUS.add(rset.getString(3)==null?"":rset.getString(3));
				VTRUCK_TYP_VOL_M3.add(rset.getString(4)==null?"":rset.getString(4));
				VTRUCK_TYP_VOL_MT.add(rset.getString(5)==null?"":rset.getString(5));
				VTRUCK_TYP_LOAD_CAP.add(rset.getString(6)==null?"":rset.getString(6));
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
	
	public void getTruckTransList()
	{
		String function_nm="getTruckTransList()";
		try
		{
			int index = 0;
			queryString = "SELECT TRUCK_TRANS_CD,TRUCK_TRANS_NAME,TRUCK_TRANS_ABBR,TO_CHAR(EFF_DT,'DD/MM/YYYY'),"
					+ "ADDR,CITY,PIN,STATE,ACTIVE_FLAG "
					+ "FROM FMS_TRUCK_TRANSPORTER_MST ";
			if(callFlag.equals("LINK_TRUCK_TRANS") || callFlag.equals("LINK_DRIVER_TRANS") || callFlag.equals("LINK_TRUCK_DRIVER") || callFlag.equals("TRUCK_TRANS_CONTACT_DETAILS") || callFlag.equals("TRUCK_MST") || callFlag.equals("TRUCK_DRIVER_MST"))
			{
				queryString+="WHERE ACTIVE_FLAG='Y' AND EFF_DT<SYSDATE ";	
			}
			queryString+= "ORDER BY TRUCK_TRANS_CD";
			stmt=conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				index+=1;
				String truck_trans_cd = rset.getString(1)==null?"":rset.getString(1);
				VTRUCK_TRANS_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VTRUCK_TRANS_NAME.add(rset.getString(2)==null?"":rset.getString(2));
				VTRUCK_TRANS_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
				VEFF_DT.add(rset.getString(4)==null?"":rset.getString(4));
				VADDR.add(rset.getString(5)==null?"":rset.getString(5));
				VCITY.add(rset.getString(6)==null?"":rset.getString(6));
				VPIN.add(rset.getString(7)==null?"":rset.getString(7));
				VTRUCK_TRANS_STATE_CD.add(rset.getString(8)==null?"":rset.getString(8));
				VTRUCK_TRANS_STATE_NAME.add(""+utilBean.getStateName(conn,rset.getString(8)==null?"":rset.getString(8)));
				VSTATUS.add(rset.getString(9)==null?"":rset.getString(9));
				
				String tax_id="";
				queryString1 = "SELECT A.STAT_NO, B.STAT_NM "
						+ "FROM FMS_TRUCK_TRANSPORTER_TAX A,FMS_GOVT_STAT_TAX B "
						+ "WHERE TRUCK_TRANS_CD=? AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, truck_trans_cd);
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
	
	public void getTruckTransporterList()
	{
		String function_nm="getTruckTransporterList()";
		try
		{
			int index = 0;
			queryString = "SELECT TRUCK_TRANS_CD,TRUCK_TRANS_NAME,TRUCK_TRANS_ABBR,TO_CHAR(EFF_DT,'DD/MM/YYYY'),"
					+ "ADDR,CITY,PIN,STATE,ACTIVE_FLAG "
					+ "FROM FMS_TRUCK_TRANSPORTER_MST ";
			if(callFlag.equals("LINK_TRUCK_TRANS") || callFlag.equals("LINK_DRIVER_TRANS") || callFlag.equals("LINK_TRUCK_DRIVER") || callFlag.equals("TRUCK_TRANS_CONTACT_DETAILS") || callFlag.equals("TRUCK_MST") || callFlag.equals("TRUCK_DRIVER_MST"))
			{
				queryString+="WHERE ACTIVE_FLAG='Y' AND EFF_DT<SYSDATE ";	
			}
			queryString+= "ORDER BY TRUCK_TRANS_CD";
			stmt=conn.prepareStatement(queryString);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				index+=1;
				String truck_trans_cd = rset.getString(1)==null?"":rset.getString(1);
				VTRUCK_TRANS_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VTRUCK_TRANS_NAME.add(rset.getString(2)==null?"":rset.getString(2));
				VTRUCK_TRANS_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getTruckTransAddrTypeDtl()
	{
		String function_nm="getTruckTransAddrDtl()";
		try
		{
			queryString1="SELECT DISTINCT ADDR "
					+ "FROM FMS_TRUCK_TRANSPORTER_MST A "
					+ "WHERE TRUCK_TRANS_CD=? ";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, truck_trans_cd);
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				VADDRESS_TYPE.add("R");
				VADDRESS_NAME.add("Registered");
			}
			rset1.close();
			stmt1.close();
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
	
	public String getLastReleaseDt(String col_name,String tbl_name, String cd)
	{
		String function_nm="getLastReleaseDt()";
		String dt="";
		try
		{	
			int cnt=0;
			queryString5="SELECT TO_CHAR(RELEASE_DT,'DD/MM/YYYY') "
					+ "FROM "+tbl_name+" A "
					+ "WHERE "+col_name+"=? AND RELEASE_DT<SYSDATE "
					+ "AND LINK_SEQ=(SELECT MAX(LINK_SEQ) FROM "+tbl_name+" B WHERE A."+col_name+"=B."+col_name+" AND RELEASE_DT<SYSDATE)";
			stmt5=conn.prepareStatement(queryString5);
			//stmt5.setString(++cnt, tbl_name);
			stmt5.setString(++cnt, cd);
			rset5=stmt5.executeQuery();
			if(rset5.next())
			{
				dt=rset5.getString(1)==null?"":rset5.getString(1);
			}
			rset5.close();
			stmt5.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return dt;
	}
	
	String callFlag = "";
	public void setCallFlag(String callFlag) {this.callFlag = callFlag;}
	String comp_cd = "";
	public void setComp_cd(String comp_cd) {this.comp_cd = comp_cd;}
	String comp_abbr = "";
	public void setComp_abbr(String comp_abbr) {this.comp_abbr = comp_abbr;}
	
	String truck_trans_cd = "";
	String opration="";
	String counterparty_cd = "";
	String fill_station = "";
	
	public void setTruck_trans_cd(String truck_trans_cd) {this.truck_trans_cd = truck_trans_cd;}
	public void setOpration(String opration) {this.opration = opration;}
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setFill_station(String fill_station) {this.fill_station = fill_station;}
	
	public String getComp_abbr() {return comp_abbr;}
	
	Vector VSTATE_CD = new Vector();
	Vector VSTATE_NM = new Vector();
	Vector VTRUCK_TRANS_CD = new Vector();
	Vector VTRUCK_TRANS_NAME = new Vector();
	Vector VTRUCK_TRANS_ABBR = new Vector();
	Vector VEFF_DT = new Vector();
	Vector VDRIVER_EFF_DT = new Vector();
	Vector VADDR = new Vector();
	Vector VCITY = new Vector();
	Vector VPIN = new Vector();
	Vector VTRUCK_TRANS_STATE_CD = new Vector();
	Vector VTRUCK_TRANS_STATE_NAME = new Vector();
	Vector VGST_TIN_NO = new Vector();
	Vector VGST_TIN_DT = new Vector();
	Vector VCST_TIN_NO = new Vector();
	Vector VCST_TIN_DT = new Vector();
	Vector VPAN_NO = new Vector();
	Vector VPAN_ISSUE_DT = new Vector();
	Vector VTAN_NO = new Vector();
	Vector VTAN_ISSUE_DT = new Vector();
	Vector VGSTIN_NO = new Vector();
	Vector VGSTIN_DT = new Vector();
	Vector VSTATUS = new Vector();
	Vector VTRUCK_STATUS = new Vector();
	Vector VDRIVER_STATUS = new Vector();
	Vector VINDEX = new Vector();
	Vector VTRUCK_TYPE = new Vector();
	Vector VTRUCK_CD = new Vector();
	Vector VTRUCK_REG_NO = new Vector();
	Vector VTRUCK_VOL_M3 = new Vector();
	Vector VTRUCK_VOL_MT = new Vector();
	Vector VTRUCK_LOAD_CAP = new Vector();
	Vector VTRUCK_TYP_VOL_M3 = new Vector();
	Vector VTRUCK_TYP_VOL_MT = new Vector();
	Vector VTRUCK_TYP_LOAD_CAP = new Vector();
	Vector VACTIVE_TRUCK_TYPE = new Vector();
	Vector VDRIVER_CD = new Vector();
	Vector VDRIVER_NAME = new Vector();
	Vector VDRIVER_ADDR = new Vector();
	Vector VDRIVER_DOB = new Vector();
	Vector VDRIVER_MOBILE = new Vector();
	Vector VLICENSE_NO = new Vector();
	Vector VLICENSE_TYPE = new Vector();
	Vector VLICENSE_FROM_DT = new Vector();
	Vector VLICENSE_TO_DT = new Vector();
	Vector VLICENSE_ISSUE_STATE_NAME = new Vector();
	Vector VLICENSE_ISSUE_STATE_CD = new Vector();
	Vector VLICENSE_FILE_NAME = new Vector();
	Vector VLINKED_TRUCK_TRANS_CD = new Vector();
	Vector VLINKED_TRUCK_TRANS_NAME = new Vector();
	Vector VLINK_TRUCK_CD = new Vector();
	Vector VLINK_TRUCK_REG_NO = new Vector();
	Vector VLINKED_EFF_DT = new Vector();
	Vector VLINKED_RELEASE_DT = new Vector();
	Vector VREMARK = new Vector();
	Vector VLINKED_TRUCK_STATUS = new Vector();
	Vector VLINKED_TRUCK_REG_NO = new Vector();
	Vector VLINK_DRIVER_CD = new Vector();
	Vector VLINK_DRIVER_NAME = new Vector();
	Vector VLINKED_DRIVER_STATUS = new Vector();
	Vector VLINKED_DRIVER_LICENSE = new Vector();
	Vector VLINKED_DRIVER_LICENSE_DURATION = new Vector();
	Vector VLINKED_DRIVER_NAME = new Vector();
	Vector VTRUCK_INDEX = new Vector();
	Vector VDRIVER_INDEX = new Vector();
	Vector VTRUCK_CD_LINKED = new Vector();
	Vector VTRUCK_REG_NO_LINKED = new Vector();
	Vector VDRIVER_CD_LINKED = new Vector();
	Vector VDRIVER_INDEX_LINKED = new Vector();
	Vector VEFF_DT_LINKED = new Vector();
	Vector VRELEASE_DT_LINKED = new Vector();
	Vector VREMARK_LINKED = new Vector();
	Vector VDRIVER_STATUS_LINKED = new Vector();
	Vector VDRIVER_LICENSE_LINKED = new Vector();
	Vector VDRIVER_LICENSE_DURATION_LINKED = new Vector();
	Vector VDRIVER_NAME_LINKED = new Vector();
	Vector VLAST_RELEASE_DT =  new Vector();
	Vector VLINKED_LAST_RELEASE_DT =  new Vector();
	Vector VCHECKPOST_CD = new Vector();
	Vector VCHECKPOST_NAME = new Vector();
	Vector VCHECKPOST_STATE_CD = new Vector();
	Vector VCHECKPOST_STATE_NM = new Vector();
	Vector VADDRESS_TYPE = new Vector();
	Vector VADDRESS_NAME = new Vector();
	Vector VSEQ_NO = new Vector();
	Vector VPERSON_NM = new Vector();
	Vector VDESIGNATION = new Vector();
	Vector VMOBILE = new Vector();
	Vector VEMAIL = new Vector();
	Vector VNOM = new Vector();
	Vector VINV = new Vector();
	Vector VFM = new Vector();
	Vector VPM = new Vector();
	Vector VJT = new Vector();
	Vector VOTHER = new Vector();
	Vector VRM = new Vector();
	Vector VADDR_FLAG = new Vector();
	Vector VADDR_TYPE = new Vector();
	Vector VADD_ADDRESS = new Vector();
	Vector VPHONE = new Vector();
	Vector VFAX1 = new Vector();
	Vector VFAX2 = new Vector();
	Vector VFLAG = new Vector();
	Vector VTEMP_ADDR_FLAG = new Vector();
	Vector VTEMP_ADD_ADDRESS = new Vector();
	Vector VTEMP_PHONE = new Vector();
	Vector VTEMP_FAX1 = new Vector();
	Vector VTEMP_FAX2 = new Vector();
	Vector VTEMP_FLAG = new Vector();
	Vector VCONTACT_EFF_DT = new Vector();
	Vector VCONTACT_STATUS = new Vector();
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VLEGAL_ENTITY = new Vector();
	Vector VCOUNTPTY_CD = new Vector();
	Vector VENTITY_TYPE = new Vector();
	Vector VFILL_STATION_CD = new Vector();
	Vector VFILL_STATION_NM = new Vector();
	Vector VFILL_STATION_ABBR = new Vector();
	Vector VFILLING_STATION_CD = new Vector();
	Vector VFILLING_STATION_NAME = new Vector();
	Vector VBAY_CD = new Vector();
	Vector VBAY_NAME = new Vector();
	Vector VSLOT_CALD_TYPE = new Vector();
	Vector VSLOT_START_TIME = new Vector();
	Vector VSLOT_INTERVAL = new Vector();
	Vector VPLANT_SEQ_NO = new Vector();
	Vector VPLANT_NAME = new Vector();
	Vector VPLANT_ABBR = new Vector();
	Vector VLINKED_PLANT_SEQ = new Vector();
	Vector VLINKED_CHKPOST_CD = new Vector();
	Vector VLINKED_PLANT_NAME = new Vector();
	Vector VLINKED_PLANT_ABBR = new Vector();
	Vector VLINKED_CHECKPOST_NM = new Vector();
	Vector VSTAT_CD = new Vector();
	Vector VSTAT_NM = new Vector();
	Vector VSTAT_TYPE = new Vector();
	Vector VSTAT_STATUS = new Vector();
	Vector VTAX_ID = new Vector();
	Vector VLINKED_FLAG = new Vector();
	Vector VBO_CD = new Vector();
	Vector VBO_NM = new Vector();
	Vector VBO_ABBR = new Vector();
	Vector VCOUNTPTY_NM = new Vector();
	Vector ENTITY_TYP_NAME = new Vector();
	Vector VLINKEDTRUCKTRANS = new Vector();
	Vector VLINKEDTRUCKTRANSNAME = new Vector();
	Vector VLINKEDEFFDT = new Vector();
	Vector VLINKEDFLAG = new Vector();
	Vector VALLTRUCK_TRANS_CD = new Vector();
	Vector VALLTRUCK_TRANS_NAME = new Vector();
	Vector VALLTRUCK_TRANS_ABBR = new Vector();
	Vector VDELINKED_CHECKPOST_NM = new Vector();
	Vector VDELINKED_PLANT_ABBR = new Vector();
	Vector VDELINKED_PLANT_NAME = new Vector();
	Vector VDL_DURATION = new Vector();
	Vector VDL_COUNTPTY_CD = new Vector();
	Vector VDL_LEGAL_ENTITY = new Vector();
	Vector VDL_COUNTPTY_NM = new Vector();
	
	Vector VLINK_SEQ = new Vector();
	Vector VMAX_NOMINATION_DT = new Vector();
	Vector VLINK_DRIVER_TRANS_EFF_DT = new Vector();
	
	public Vector getVSTATE_CD() {return VSTATE_CD;}
	public Vector getVSTATE_NM() {return VSTATE_NM;}
	public Vector getVTRUCK_TRANS_CD() {return VTRUCK_TRANS_CD;}
	public Vector getVTRUCK_TRANS_NAME() {return VTRUCK_TRANS_NAME;}
	public Vector getVTRUCK_TRANS_ABBR() {return VTRUCK_TRANS_ABBR;}
	public Vector getVEFF_DT() {return VEFF_DT;}
	public Vector getVDRIVER_EFF_DT() {return VDRIVER_EFF_DT;}
	public Vector getVADDR() {return VADDR;}
	public Vector getVCITY() {return VCITY;}
	public Vector getVPIN() {return VPIN;}
	public Vector getVTRUCK_TRANS_STATE_CD() {return VTRUCK_TRANS_STATE_CD;}
	public Vector getVTRUCK_TRANS_STATE_NAME() {return VTRUCK_TRANS_STATE_NAME;}
	public Vector getVGST_TIN_NO() {return VGST_TIN_NO;}
	public Vector getVGST_TIN_DT() {return VGST_TIN_DT;}
	public Vector getVCST_TIN_NO() {return VCST_TIN_NO;}
	public Vector getVCST_TIN_DT() {return VCST_TIN_DT;}
	public Vector getVPAN_NO() {return VPAN_NO;}
	public Vector getVPAN_ISSUE_DT() {return VPAN_ISSUE_DT;}
	public Vector getVTAN_NO() {return VTAN_NO;}
	public Vector getVTAN_ISSUE_DT() {return VTAN_ISSUE_DT;}
	public Vector getVGSTIN_NO() {return VGSTIN_NO;}
	public Vector getVGSTIN_DT() {return VGSTIN_DT;}
	public Vector getVSTATUS() {return VSTATUS;}
	public Vector getVTRUCK_STATUS() {return VTRUCK_STATUS;}
	public Vector getVDRIVER_STATUS() {return VDRIVER_STATUS;}
	public Vector getVINDEX() {return VINDEX;}
	public Vector getVTRUCK_TYPE() {return VTRUCK_TYPE;}
	public Vector getVTRUCK_CD() {return VTRUCK_CD;}
	public Vector getVTRUCK_REG_NO() {return VTRUCK_REG_NO;}
	public Vector getVTRUCK_VOL_M3() {return VTRUCK_VOL_M3;}
	public Vector getVTRUCK_VOL_MT() {return VTRUCK_VOL_MT;}
	public Vector getVTRUCK_LOAD_CAP() {return VTRUCK_LOAD_CAP;}
	public Vector getVTRUCK_TYP_VOL_M3() {return VTRUCK_TYP_VOL_M3;}
	public Vector getVTRUCK_TYP_VOL_MT() {return VTRUCK_TYP_VOL_MT;}
	public Vector getVTRUCK_TYP_LOAD_CAP() {return VTRUCK_TYP_LOAD_CAP;}
	public Vector getVACTIVE_TRUCK_TYPE() {return VACTIVE_TRUCK_TYPE;}
	public Vector getVDRIVER_CD() {return VDRIVER_CD;}
	public Vector getVDRIVER_NAME() {return VDRIVER_NAME;}
	public Vector getVDRIVER_ADDR() {return VDRIVER_ADDR;}
	public Vector getVDRIVER_DOB() {return VDRIVER_DOB;}
	public Vector getVDRIVER_MOBILE() {return VDRIVER_MOBILE;}
	public Vector getVLICENSE_NO() {return VLICENSE_NO;}
	public Vector getVLICENSE_TYPE() {return VLICENSE_TYPE;}
	public Vector getVLICENSE_FROM_DT() {return VLICENSE_FROM_DT;}
	public Vector getVLICENSE_TO_DT() {return VLICENSE_TO_DT;}
	public Vector getVLICENSE_ISSUE_STATE_NAME() {return VLICENSE_ISSUE_STATE_NAME;}
	public Vector getVLICENSE_ISSUE_STATE_CD() {return VLICENSE_ISSUE_STATE_CD;}
	public Vector getVLICENSE_FILE_NAME() {return VLICENSE_FILE_NAME;}
	public Vector getVLINKED_TRUCK_TRANS_CD() {return VLINKED_TRUCK_TRANS_CD;}
	public Vector getVLINKED_TRUCK_TRANS_NAME() {return VLINKED_TRUCK_TRANS_NAME;}
	public Vector getVLINK_TRUCK_CD() {return VLINK_TRUCK_CD;}
	public Vector getVLINK_TRUCK_REG_NO() {return VLINK_TRUCK_REG_NO;}
	public Vector getVLINKED_EFF_DT() {return VLINKED_EFF_DT;}
	public Vector getVLINKED_RELEASE_DT() {return VLINKED_RELEASE_DT;}
	public Vector getVREMARK() {return VREMARK;}
	public Vector getVLINKED_TRUCK_STATUS() {return VLINKED_TRUCK_STATUS;}
	public Vector getVLINKED_TRUCK_REG_NO() {return VLINKED_TRUCK_REG_NO;}
	public Vector getVLINK_DRIVER_CD() {return VLINK_DRIVER_CD;}
	public Vector getVLINK_DRIVER_NAME() {return VLINK_DRIVER_NAME;}
	public Vector getVLINKED_DRIVER_STATUS() {return VLINKED_DRIVER_STATUS;}
	public Vector getVLINKED_DRIVER_LICENSE() {return VLINKED_DRIVER_LICENSE;}
	public Vector getVLINKED_DRIVER_LICENSE_DURATION() {return VLINKED_DRIVER_LICENSE_DURATION;}
	public Vector getVLINKED_DRIVER_NAME() {return VLINKED_DRIVER_NAME;}
	public Vector getVTRUCK_INDEX() {return VTRUCK_INDEX;}
	public Vector getVDRIVER_INDEX() {return VDRIVER_INDEX;}
	public Vector getVTRUCK_CD_LINKED() {return VTRUCK_CD_LINKED;}
	public Vector getVTRUCK_REG_NO_LINKED() {return VTRUCK_REG_NO_LINKED;}
	public Vector getVDRIVER_CD_LINKED() {return VDRIVER_CD_LINKED;}
	public Vector getVDRIVER_INDEX_LINKED() {return VDRIVER_INDEX_LINKED;}
	public Vector getVEFF_DT_LINKED() {return VEFF_DT_LINKED;}
	public Vector getVRELEASE_DT_LINKED() {return VRELEASE_DT_LINKED;}
	public Vector getVREMARK_LINKED() {return VREMARK_LINKED;}
	public Vector getVDRIVER_STATUS_LINKED() {return VDRIVER_STATUS_LINKED;}
	public Vector getVDRIVER_LICENSE_LINKED() {return VDRIVER_LICENSE_LINKED;}
	public Vector getVDRIVER_LICENSE_DURATION_LINKED() {return VDRIVER_LICENSE_DURATION_LINKED;}
	public Vector getVDRIVER_NAME_LINKED() {return VDRIVER_NAME_LINKED;}
	public Vector getVLAST_RELEASE_DT() {return VLAST_RELEASE_DT;}
	public Vector getVLINKED_LAST_RELEASE_DT() {return VLINKED_LAST_RELEASE_DT;}
	public Vector getVCHECKPOST_CD() {return VCHECKPOST_CD;}
	public Vector getVCHECKPOST_NAME() {return VCHECKPOST_NAME;}
	public Vector getVCHECKPOST_STATE_CD() {return VCHECKPOST_STATE_CD;}
	public Vector getVCHECKPOST_STATE_NM() {return VCHECKPOST_STATE_NM;}
	public Vector getVADDRESS_TYPE() {return VADDRESS_TYPE;}
	public Vector getVADDRESS_NAME() {return VADDRESS_NAME;}
	public Vector getVSEQ_NO() {return VSEQ_NO;}
	public Vector getVPERSON_NM() {return VPERSON_NM;}
	public Vector getVDESIGNATION() {return VDESIGNATION;}
	public Vector getVMOBILE() {return VMOBILE;}
	public Vector getVEMAIL() {return VEMAIL;}
	public Vector getVNOM() {return VNOM;}
	public Vector getVINV() {return VINV;}
	public Vector getVFM() {return VFM;}
	public Vector getVPM() {return VPM;}
	public Vector getVJT() {return VJT;}
	public Vector getVOTHER() {return VOTHER;}
	public Vector getVRM() {return VRM;}
	public Vector getVADDR_FLAG() {return VADDR_FLAG;}
	public Vector getVADDR_TYPE() {return VADDR_TYPE;}
	public Vector getVADD_ADDRESS() {return VADD_ADDRESS;}
	public Vector getVPHONE() {return VPHONE;}
	public Vector getVFAX1() {return VFAX1;}
	public Vector getVFAX2() {return VFAX2;}
	public Vector getVFLAG() {return VFLAG;}
	public Vector getVTEMP_ADDR_FLAG() {return VTEMP_ADDR_FLAG;}
	public Vector getVTEMP_ADD_ADDRESS() {return VTEMP_ADD_ADDRESS;}
	public Vector getVTEMP_PHONE() {return VTEMP_PHONE;}
	public Vector getVTEMP_FAX1() {return VTEMP_FAX1;}
	public Vector getVTEMP_FAX2() {return VTEMP_FAX2;}
	public Vector getVTEMP_FLAG() {return VTEMP_FLAG;}
	public Vector getVCONTACT_EFF_DT() {return VCONTACT_EFF_DT;}
	public Vector getVCONTACT_STATUS() {return VCONTACT_STATUS;}
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVLEGAL_ENTITY() {return VLEGAL_ENTITY;}
	public Vector getVCOUNTPTY_CD() {return VCOUNTPTY_CD;}
	public Vector getVENTITY_TYPE() {return VENTITY_TYPE;}
	public Vector getVFILL_STATION_CD() {return VFILL_STATION_CD;}
	public Vector getVFILL_STATION_NM() {return VFILL_STATION_NM;}
	public Vector getVFILL_STATION_ABBR() {return VFILL_STATION_ABBR;}
	public Vector getVFILLING_STATION_CD() {return VFILLING_STATION_CD;}
	public Vector getVFILLING_STATION_NAME() {return VFILLING_STATION_NAME;}
	public Vector getVBAY_CD() {return VBAY_CD;}
	public Vector getVBAY_NAME() {return VBAY_NAME;}
	public Vector getVSLOT_CALD_TYPE() {return VSLOT_CALD_TYPE;}
	public Vector getVSLOT_START_TIME() {return VSLOT_START_TIME;}
	public Vector getVSLOT_INTERVAL() {return VSLOT_INTERVAL;}
	public Vector getVPLANT_SEQ_NO() {return VPLANT_SEQ_NO;}
	public Vector getVPLANT_NAME() {return VPLANT_NAME;}
	public Vector getVPLANT_ABBR() {return VPLANT_ABBR;}
	public Vector getVLINKED_PLANT_SEQ() {return VLINKED_PLANT_SEQ;}
	public Vector getVLINKED_CHKPOST_CD() {return VLINKED_CHKPOST_CD;}
	public Vector getVLINKED_PLANT_NAME() {return VLINKED_PLANT_NAME;}
	public Vector getVLINKED_PLANT_ABBR() {return VLINKED_PLANT_ABBR;}
	public Vector getVLINKED_CHECKPOST_NM() {return VLINKED_CHECKPOST_NM;}
	public Vector getVSTAT_CD() {return VSTAT_CD;}
	public Vector getVSTAT_NM() {return VSTAT_NM;}
	public Vector getVSTAT_TYPE() {return VSTAT_TYPE;}
	public Vector getVSTAT_STATUS() {return VSTAT_STATUS;}
	public Vector getVTAX_ID() {return VTAX_ID;}
	public Vector getVLINKED_FLAG() {return VLINKED_FLAG;}
	public Vector getVBO_CD() {return VBO_CD;}
	public Vector getVBO_NM() {return VBO_NM;}
	public Vector getVBO_ABBR() {return VBO_ABBR;}
	public Vector getENTITY_TYP_NAME() {return ENTITY_TYP_NAME;}
	public Vector getVCOUNTPTY_NM() {return VCOUNTPTY_NM;}
	public Vector getVLINKEDFLAG() {return VLINKEDFLAG;}
	public Vector getVLINKEDEFFDT() {return VLINKEDEFFDT;}
	public Vector getVLINKEDTRUCKTRANS() {return VLINKEDTRUCKTRANS;}
	public Vector getVLINKEDTRUCKTRANSNAME() {return VLINKEDTRUCKTRANSNAME;}
	public Vector getVALLTRUCK_TRANS_CD() {return VALLTRUCK_TRANS_CD;}
	public Vector getVALLTRUCK_TRANS_NAME() {return VALLTRUCK_TRANS_NAME;}
	public Vector getVALLTRUCK_TRANS_ABBR() {return VALLTRUCK_TRANS_ABBR;}
	
	public Vector getVDELINKED_CHECKPOST_NM() {return VDELINKED_CHECKPOST_NM;}
	public Vector getVDELINKED_PLANT_ABBR() {return VDELINKED_PLANT_ABBR;}
	public Vector getVDELINKED_PLANT_NAME() {return VDELINKED_PLANT_NAME;}
	public Vector getVDL_DURATION() {return VDL_DURATION;}
	public Vector getVDL_COUNTPTY_CD() {return VDL_COUNTPTY_CD;}
	public Vector getVDL_LEGAL_ENTITY() {return VDL_LEGAL_ENTITY;}
	public Vector getVDL_COUNTPTY_NM() {return VDL_COUNTPTY_NM;}
	
	public Vector getVLINK_SEQ() {return VLINK_SEQ;}
	public Vector getVMAX_NOMINATION_DT() {return VMAX_NOMINATION_DT;}
	public Vector getVLINK_DRIVER_TRANS_EFF_DT() {return VLINK_DRIVER_TRANS_EFF_DT;}
}
