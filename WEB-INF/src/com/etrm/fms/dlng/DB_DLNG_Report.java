package com.etrm.fms.dlng;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.etrm.fms.mail.MailDelivery;
import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DB_AllocationUtil;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;

public class DB_DLNG_Report 
{
	String db_src_file_name="DB_DLNG_Report.java";
	Connection conn; 
	PreparedStatement stmt;
	PreparedStatement stmt0;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt5;
	PreparedStatement stmt6;
	PreparedStatement stmt7;
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
	ResultSet rset6;
	ResultSet rset7;
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
	String queryString6="";
	
	UtilBean utilBean = new UtilBean();
	DateUtil dateUtil= new DateUtil();
	DB_AllocationUtil allocUtil = new DB_AllocationUtil();
	UtilBean_DLNG utilBean_DLNG = new UtilBean_DLNG();
	MailDelivery mailDelv = new MailDelivery();
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf1 = new DecimalFormat("###########0.000");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	
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
	    			if(callFlag.equalsIgnoreCase("DLNG_CONTRACT_SUMMARY"))
	    			{
	    				getCustomerCounterpartyList();
	    				getHeaderSegment();
	    				getSegment_type();
	    				getContractSummary();
	    			}
	    			else if(callFlag.equalsIgnoreCase("DLNG_BUYER_NOM_REV_WISE")) 
					{
						getBuyerNominationRevisionWise();
					}
	    			else if(callFlag.equalsIgnoreCase("DLNG_SELLER_NOM_REV_WISE"))
					{
	    				getSellerRevCounterpartyList();
						getSellerNominationRevisionWise();
					}
	    			else if (callFlag.equalsIgnoreCase("DLNG_ALLOCATION_TO_CUSTOMER")) 
					{
						getCustomerMasterForAllocToCust();
						getBUList(); //RG20250926 For adding Bu wise filter
						getAllocToCustomerData();
					}
	    			else if (callFlag.equalsIgnoreCase("DLNG_ALLOCATION_CONTRACT_WISE")) 
					{
						getCustomerCounterpartyList();
						getSegment();
						getContractList();
						getBUList(); //RG20250926 For adding Bu wise filter
						getDateWiseAllocationQty();
					}
	    			else if(callFlag.equalsIgnoreCase("DLNG_TCQ_VARIATION"))
	    			{
	    				getCustomerCounterpartyList();
	    				getSegment();
	    				getContractTcqVariation();
	    			}
	    			else if (callFlag.equalsIgnoreCase("DLNG_NOM_ALLOC")) 
					{
	    				getCustomerMstFromBuyerNom();
						getTransporterMstFromSellerNom();
						getBuMstFromBuyerNom();		// SagarB20250922 Added this function for showing BU list in DLNG-> DLNG Nomination vs Truck Loading
						getNominationAndAllocationDtl();
					}
	    			else if(callFlag.equalsIgnoreCase("DLNG_SELLER_NOMINATION_TO_TRANSPORTER"))
	    			{
	    				getDailySellerNominationToTransporter();
	    			}
	    			else if(callFlag.equalsIgnoreCase("DLNG_VIEW_SELLER_NOMINATION_TO_TRANSPORTER"))
	    			{
	    				getViewNominationToTransporterData();
	    				getViewNominationToTransporterInfo();
	    			}
	    			else if(callFlag.equalsIgnoreCase("DLNG_SEND_MAIL_SELLER_TO_TRANS"))
	    			{
	    				getSendMailSellerToTransporterDetail();
	    			}
	    			else if(callFlag.equalsIgnoreCase("DLNG_SELLER_NOMINATION_TO_CUSTOMER"))
	    			{
	    				getContractTypeMasterForNomToCust();
	    				getDailySellerNominationToCustomer();
	    			}
	    			else if(callFlag.equalsIgnoreCase("DLNG_VIEW_SELLER_NOMINATION_TO_CUSTOMER"))
	    			{
	    				getViewNominationToCustomerData();
	    				getViewNominationToCustomerInfo();
	    			}
	    			else if(callFlag.equalsIgnoreCase("DLNG_SEND_MAIL_SELLER_TO_CUST"))
	    			{
	    				getSendMailSellerToCustomerDetail();
	    			}
	    			else if (callFlag.equalsIgnoreCase("DELIVERY_REPORT")) 
	    			{
	    				getAllocCounterpartyList();
	    				getDeliveryReportDtls();
	    			}
	    			else if (callFlag.equalsIgnoreCase("DLNG_ALLOC_TO_CUST")) 
	    			{
	    				getCustomerMasterForAllocToCust();
	    				getAllocToCustomer();
	    			}
	    			else if(callFlag.equalsIgnoreCase("DLNG_JOINT_TICKET"))
	    			{
	    				getDailyJointTicket();
	    			}
	    			else if(callFlag.equalsIgnoreCase("DLNG_VIEW_JOINT_TICKET"))
	    			{
	    				getViewJointTicketData();
	    				getViewJointTicketInfo();
	    			}
	    			else if(callFlag.equalsIgnoreCase("DLNG_SEND_MAIL_JOINT_TICKET"))
	    			{
	    				getSendMailJTDetail();
	    			}
	    			else if(callFlag.equalsIgnoreCase("DLNG_TRUCKS_SLOT_WISE_DTL"))
	    			{
	    				getTruckSlotWiseDtl();
	    			}
	    			else if(callFlag.equalsIgnoreCase("DLNG_TERMINAL_REPORT"))
	    			{
	    				getFillStMasterForTerminalRpt();
	    				getTerminalReportDtl();
	    			}
	    			else if(callFlag.equalsIgnoreCase("DLNG_ALLOC_TO_CUSTOMER_PLANT_CONTRACT_WISE"))
	    			{
	    				getCustomerMasterForAllocToCust();
						getBUList();
	    				getContractMap();
	    				getDealMapping();
	    				getPlantName();
	    				getCustomerAllocationData();
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
	    	if(rset7 != null){try{rset7.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
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
	    	if(stmt6 != null){try{stmt6.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt7 != null){try{stmt7.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp != null){try{stmt_temp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp0 != null){try{stmt_temp0.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp1 != null){try{stmt_temp1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_temp2 != null){try{stmt_temp2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}
	
	public void getTruckSlotWiseDtl()
	{
		String function_nm="getTruckSlotWiseDtl()";
		try
		{
			String queryString_temp=""
					+ " SELECT TO_CHAR(GAS_DT,'DD/MM/YYYY') AS GAS_DT,TRUCK_CD,COMPANY_CD,COUNTERPARTY_CD, "
					+ " AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ,BU_SEQ,TRUCK_TRANS_CD,"
					+ " FILL_STATION_CD,BAY_CD,SLOT_START_TIME,SLOT_END_TIME,NEXT_AVAIL_HRS,'A' AS CURR_STATUS,"//Allocated = A
					+ "	LOAD_START_TIME, LOAD_END_TIME,TO_CHAR(LOAD_END_DT,'DD/MM/YYYY'), CARGO_NO "
					+ " FROM FMS_DLNG_ALLOC_MST E "//,FMS_DLNG_SELLER_NOM_DTL X "
    				+ " WHERE GAS_DT<=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') "
    				+ " AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST F "
					+ " WHERE F.CONT_NO=E.CONT_NO AND F.AGMT_NO=E.AGMT_NO "
					+ "	AND F.COMPANY_CD=E.COMPANY_CD AND F.COUNTERPARTY_CD=E.COUNTERPARTY_CD "
					+ "	AND F.PLANT_SEQ=E.PLANT_SEQ AND F.CONTRACT_TYPE=E.CONTRACT_TYPE AND F.GAS_DT=E.GAS_DT "
					+ " AND F.TRUCK_TRANS_CD=E.TRUCK_TRANS_CD AND F.TRUCK_CD=E.TRUCK_CD AND E.CARGO_NO=F.CARGO_NO) "
					+ ""
					+ "AND (TRUCK_TRANS_CD != ? AND TRUCK_CD != ? AND FILL_STATION_CD != ? AND BAY_CD != ? "
					+ "AND (SLOT_START_TIME LIKE '__:__' AND CAST(SUBSTR(SLOT_START_TIME, 1, 2) AS INT) < 24 AND CAST(SUBSTR(SLOT_START_TIME, 4, 2) AS INT) < 60) "
					+ "AND (SLOT_END_TIME LIKE '__:__' AND CAST(SUBSTR(SLOT_END_TIME, 1, 2) AS INT) < 24 AND CAST(SUBSTR(SLOT_END_TIME, 4, 2) AS INT) < 60) "
					+ ")"//HM20260221 : Excluding seller nom data for which truck is released (LIVE Issue methioned by Bhaumik P)
					+ ""
					+ ""
					+ " UNION ALL "
					+ " SELECT TO_CHAR(GAS_DT,'DD/MM/YYYY') AS GAS_DT,TRUCK_CD,COMPANY_CD,COUNTERPARTY_CD, "
					+ " AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ,BU_SEQ,TRUCK_TRANS_CD,"
					+ " FILL_STATION_CD,BAY_CD,SLOT_START_TIME,SLOT_END_TIME,NEXT_AVAIL_HRS,'B' AS CURR_STATUS, "//Sheduled = B
					+ " '','',TO_CHAR(ARRIVAL_DT,'DD/MM/YYYY'), CARGO_NO "
					+ " FROM FMS_DLNG_SELLER_NOM_DTL C "
    				+ " WHERE GAS_DT<=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') "
    				+ " AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_SELLER_NOM_DTL D "
					+ " WHERE C.CONT_NO=D.CONT_NO AND C.AGMT_NO=D.AGMT_NO "
					+ "	AND C.COMPANY_CD=D.COMPANY_CD AND C.COUNTERPARTY_CD=D.COUNTERPARTY_CD "
					+ "	AND C.PLANT_SEQ=D.PLANT_SEQ AND C.CONTRACT_TYPE=D.CONTRACT_TYPE AND C.GAS_DT=D.GAS_DT "
					+ " AND C.TRUCK_TRANS_CD=D.TRUCK_TRANS_CD AND C.TRUCK_CD=D.TRUCK_CD AND C.CARGO_NO=D.CARGO_NO) "
					+ ""
					+ "AND (TRUCK_TRANS_CD != ? AND TRUCK_CD != ? AND FILL_STATION_CD != ? AND BAY_CD != ? "
					+ "AND (SLOT_START_TIME LIKE '__:__' AND CAST(SUBSTR(SLOT_START_TIME, 1, 2) AS INT) < 24 AND CAST(SUBSTR(SLOT_START_TIME, 4, 2) AS INT) < 60) "
					+ "AND (SLOT_END_TIME LIKE '__:__' AND CAST(SUBSTR(SLOT_END_TIME, 1, 2) AS INT) < 24 AND CAST(SUBSTR(SLOT_END_TIME, 4, 2) AS INT) < 60) "
					+ ")"//HM20260221 : Excluding seller nom data for which truck is released (LIVE Issue methioned by Bhaumik P)
					+ ""
					+ ""
					+ " UNION ALL "
					+ " SELECT TO_CHAR(GAS_DT,'DD/MM/YYYY') AS GAS_DT,TRUCK_CD,COMPANY_CD,COUNTERPARTY_CD,"
					+ " AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,PLANT_SEQ,BU_SEQ,TRUCK_TRANS_CD,"
					+ " FILL_STATION_CD,BAY_CD,SLOT_START_TIME,SLOT_END_TIME,NEXT_AVAIL_HRS,'C' AS CURR_STATUS, "//Nominated = C
					+ " '','',TO_CHAR(ARRIVAL_DT,'DD/MM/YYYY'), CARGO_NO "
					+ " FROM FMS_DLNG_BUYER_NOM_DTL A "
					+ " WHERE GAS_DT<=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') "
    				+ " AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_BUYER_NOM_DTL B "
					+ " WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "	AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "	AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.GAS_DT=A.GAS_DT "
					+ " AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD AND B.CARGO_NO=A.CARGO_NO) "
					+ ""
					+ "AND (TRUCK_TRANS_CD != ? AND TRUCK_CD != ? AND FILL_STATION_CD != ? AND BAY_CD != ? "
					+ "AND (SLOT_START_TIME LIKE '__:__' AND CAST(SUBSTR(SLOT_START_TIME, 1, 2) AS INT) < 24 AND CAST(SUBSTR(SLOT_START_TIME, 4, 2) AS INT) < 60) "
					+ "AND (SLOT_END_TIME LIKE '__:__' AND CAST(SUBSTR(SLOT_END_TIME, 1, 2) AS INT) < 24 AND CAST(SUBSTR(SLOT_END_TIME, 4, 2) AS INT) < 60) "
					+ ")"//HM20260221 : Excluding seller nom data for which truck is released (LIVE Issue methioned by Bhaumik P)
					+ ""
					+ ""
					+ "ORDER BY GAS_DT,TRUCK_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BU_SEQ,PLANT_SEQ,TRUCK_TRANS_CD,FILL_STATION_CD,BAY_CD,CURR_STATUS";
			int stmt_cnt=0;
			stmt_temp = conn.prepareStatement(queryString_temp);
			//stmt_temp.setString(++stmt_cnt, comp_cd);
			stmt_temp.setString(++stmt_cnt, to_dt);
			stmt_temp.setString(++stmt_cnt, from_dt);
			stmt_temp.setString(++stmt_cnt, "0");
			stmt_temp.setString(++stmt_cnt, "0");
			stmt_temp.setString(++stmt_cnt, "0");
			stmt_temp.setString(++stmt_cnt, "0");
			//stmt_temp.setString(++stmt_cnt, comp_cd);
			stmt_temp.setString(++stmt_cnt, to_dt);
			stmt_temp.setString(++stmt_cnt, from_dt);
			stmt_temp.setString(++stmt_cnt, "0");
			stmt_temp.setString(++stmt_cnt, "0");
			stmt_temp.setString(++stmt_cnt, "0");
			stmt_temp.setString(++stmt_cnt, "0");
			//stmt_temp.setString(++stmt_cnt, comp_cd);
			stmt_temp.setString(++stmt_cnt, to_dt);
			stmt_temp.setString(++stmt_cnt, from_dt);
			stmt_temp.setString(++stmt_cnt, "0");
			stmt_temp.setString(++stmt_cnt, "0");
			stmt_temp.setString(++stmt_cnt, "0");
			stmt_temp.setString(++stmt_cnt, "0");
			rset_temp=stmt_temp.executeQuery();
			while(rset_temp.next())
			{
				String gas_dt  = rset_temp.getString(1)==null?"":rset_temp.getString(1);
				String truck_cd  = rset_temp.getString(2)==null?"":rset_temp.getString(2);
				String comp_cd  = rset_temp.getString(3)==null?"":rset_temp.getString(3);
				String countPartyCd  = rset_temp.getString(4)==null?"":rset_temp.getString(4);
				String agmt  = rset_temp.getString(5)==null?"":rset_temp.getString(5);
				String agmt_rev  = rset_temp.getString(6)==null?"":rset_temp.getString(6);
				String cont  = rset_temp.getString(7)==null?"":rset_temp.getString(7);
				String cont_rev  = rset_temp.getString(8)==null?"":rset_temp.getString(8);
				String cont_type  = rset_temp.getString(9)==null?"":rset_temp.getString(9);
				String plant_seq = rset_temp.getString(10)==null?"":rset_temp.getString(10);
				String bu_plant_seq  = rset_temp.getString(11)==null?"":rset_temp.getString(11);
				String transporter_cd  = rset_temp.getString(12)==null?"":rset_temp.getString(12);
				String filling_st_cd  = rset_temp.getString(13)==null?"":rset_temp.getString(13);
				String bay_cd  = rset_temp.getString(14)==null?"":rset_temp.getString(14);
				String slot_start_time  = rset_temp.getString(15)==null?"":rset_temp.getString(15);
				String slot_end_time  = rset_temp.getString(16)==null?"":rset_temp.getString(16);
				String next_avail_hrs  = rset_temp.getString(17)==null?"":rset_temp.getString(17);
				String curr_status  = rset_temp.getString(18)==null?"":rset_temp.getString(18);
				String cargo_no = rset_temp.getString(22)==null?"":rset_temp.getString(22);
				
				VGAS_DT.add(gas_dt);
				VTRUCK_REG_NUM.add(utilBean_DLNG.getTruckRegNo(conn, truck_cd));
				VLEAGAL_ENTITY.add(utilBean.getCompanyAbbr(conn, comp_cd));
				VCOUNTERPARTY_CD.add(countPartyCd);
				VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn, countPartyCd));
				VCOUNTERPARTY_NM.add(utilBean.getCounterpartyName(conn, countPartyCd));
				
				String plant_abbr = utilBean.getCounterpartyPlantABBR(conn,countPartyCd, comp_cd, plant_seq, "C");
				String bu_abbr = utilBean.getCounterpartyPlantABBR(conn, comp_cd, comp_cd, bu_plant_seq, "B");
				
				VCONT_BU_PLANT_MAP.add(bu_abbr+" - "+plant_abbr);
				VTRANSPORTER_ABBR.add(utilBean_DLNG.getTruckTransABBR(conn, transporter_cd));
				VTRANSPORTER_NM.add(utilBean_DLNG.getTruckTransName(conn, transporter_cd));
				VTRANSPORTER_CD.add(transporter_cd);
				VFILLING_ST_NM.add(utilBean_DLNG.getFillStationName(conn, filling_st_cd));
				VFILLING_ST_ABBR.add(utilBean_DLNG.getFillStationABBR(conn, filling_st_cd));
				VBAY_CD.add(bay_cd);
				
				String queryString2="SELECT BAY_CD,BAY_NAME,SLOT_START_TIME,SLOT_INTERVAL "
	    				+ "FROM FMS_BAY_SLOT_MST A "
	    				+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_BAY_SLOT_MST B "
	    				+ "WHERE A.FILL_STATION_CD=B.FILL_STATION_CD "
	    				+ "AND A.BAY_CD=B.BAY_CD "
	    				+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) "
	    				+ "AND ACTIVE_FLAG=? AND BAY_CD=?";
	    		stmt2 = conn.prepareStatement(queryString2);
	    		stmt2.setString(1, "Y");
	    		stmt2.setString(2, bay_cd);
	    		//stmt2.setString(3, comp_cd);
	    		rset2=stmt2.executeQuery();
	    		while(rset2.next())
	    		{
	    			VBAY_NM.add(rset2.getString(2)==null?"":rset2.getString(2));
	    		}
	    		rset2.close();
	    		stmt2.close();
				
	    		String slot_dtl = slot_start_time+" - "+slot_end_time;
	    		VSLOT_DLT.add(slot_dtl);
	    		VNEXT_AVAIL_HRS.add(next_avail_hrs);
	    		
	    		String temp_occupied_start_time=rset_temp.getString(19)==null?slot_start_time:rset_temp.getString(19);
	    		String temp_occupied_end_time=rset_temp.getString(20)==null?slot_end_time:rset_temp.getString(20);
	    		String temp_occupied_end_dt=rset_temp.getString(21)==null?"":rset_temp.getString(21);

	    		String resultTime = calculateNewTime(temp_occupied_end_time,temp_occupied_end_dt, Integer.parseInt(next_avail_hrs));
	    		
	    		//String occupied_start_time=rset_temp.getString(19)==null?slot_start_time:rset_temp.getString(19);
	    		String occupied_end_time=resultTime;//rset_temp.getString(20)==null?slot_end_time:rset_temp.getString(20);
	    		
				//VOCCUPIED_START_TIME.add(occupied_start_time);
				VOCCUPIED_END_TIME.add(occupied_end_time);
	    		
	    		VCURR_STATUS.add(curr_status);
	    		
				String tittle_dis_cont_mapping= utilBean.NewDealMappingId(comp_cd, countPartyCd, agmt, agmt_rev, cont, cont_rev, cont_type, cargo_no);
				VTITTLE_DISP_CONT_NO.add(tittle_dis_cont_mapping);
				
				queryString = "SELECT TRUCK_CD,DRIVER_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY'),TO_CHAR(RELEASE_DT,'DD/MM/YYYY'),REMARKS,LINK_SEQ "
						+ "FROM FMS_TRUCK_DRIVER_LINK A "
						+ "WHERE A.DRIVER_CD IN (SELECT DRIVER_CD "
						+ "	FROM FMS_TRUCK_DRIVER_TRANS_LINK B "
						+ "	WHERE B.TRUCK_TRANS_CD=? AND TRUCK_CD=? "
						+ "	AND B.LINK_SEQ  = (SELECT MAX(C.LINK_SEQ) "
						+ "		FROM FMS_TRUCK_DRIVER_TRANS_LINK C "
						+ "		WHERE B.DRIVER_CD=C.DRIVER_CD) "
						+ "	AND (TO_DATE(B.RELEASE_DT,'DD/MM/YYYY')>TO_DATE(?,'DD/MM/YYYY') OR B.RELEASE_DT IS NULL)) "
						+ "AND (TO_DATE(A.RELEASE_DT,'DD/MM/YYYY')>TO_DATE(?,'DD/MM/YYYY') OR A.RELEASE_DT IS NULL) "
						+ "AND A.TRUCK_CD IN (SELECT TRUCK_CD "
						+ "	FROM FMS_TRUCK_TRANSPORTER_LINK D "
						+ "	WHERE D.TRUCK_TRANS_CD=? AND D.TRUCK_CD=? "
						+ "	AND D.LINK_SEQ  = (SELECT MAX(E.LINK_SEQ) "
						+ "		FROM FMS_TRUCK_TRANSPORTER_LINK E WHERE D.TRUCK_CD=E.TRUCK_CD) "
						+ "		AND (TO_DATE(D.RELEASE_DT,'DD/MM/YYYY')>TO_DATE(?,'DD/MM/YYYY') OR D.RELEASE_DT IS NULL)) "
						+ "	AND A.LINK_SEQ=(SELECT MAX(F.LINK_SEQ) FROM FMS_TRUCK_DRIVER_LINK F WHERE A.DRIVER_CD=F.DRIVER_CD) "
						+ "ORDER BY DRIVER_CD";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, transporter_cd);
				stmt.setString(2, truck_cd);
				stmt.setString(3, gas_dt);
				stmt.setString(4, gas_dt);
				stmt.setString(5, transporter_cd);
				stmt.setString(6, truck_cd);
				stmt.setString(7, gas_dt);
				rset = stmt.executeQuery();
				if(rset.next())
				{
					String assigned_driver  = rset.getString(1)==null?"":rset.getString(1);
					
					String assigned_driver_nm = utilBean_DLNG.getDriverName(conn, assigned_driver);
					VASSIGNED_DRIVER.add(assigned_driver_nm);
				}
				else
				{
					VASSIGNED_DRIVER.add("");
				}
				rset.close();
				stmt.close();
				
			}
			rset_temp.close();
			stmt_temp.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public static String calculateNewTime(String tempOccupiedEndTime, String tempOccupiedEndDate, int nextAvailHours)
	{
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalTime endTime = LocalTime.parse(tempOccupiedEndTime, timeFormatter);
        LocalDate endDate = LocalDate.parse(tempOccupiedEndDate, dateFormatter);

        LocalDateTime dateTime = LocalDateTime.of(endDate, endTime);

        LocalDateTime resultDateTime = dateTime.plusHours(nextAvailHours);

        String formattedTime = resultDateTime.toLocalTime().format(timeFormatter);
        String formattedDate = resultDateTime.toLocalDate().format(dateFormatter);

        return formattedDate + " " + formattedTime;
    }

	public void getSendMailJTDetail()
	{
		String function_nm="getSendMailJTDetail()";
		try
		{
			String ownAddress="";
			String ownCity="";
			String ownState="";
			String ownPin="";
			String ownCountry="";
			String ownPhone="";
			String ownEmail="";
			String sysdate=dateUtil.getSysdate();
			
			queryString="SELECT ADDR,CITY,STATE,PIN,COUNTRY,PHONE,EMAIL "
					+ "FROM FMS_COMPANY_OWNER_ADDR_MST A "
					+ "WHERE COMPANY_CD=? AND ADDRESS_TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COMPANY_OWNER_ADDR_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.ADDRESS_TYPE=B.ADDRESS_TYPE "
					+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "C");
			rset=stmt.executeQuery();
			if(rset.next())
			{
				ownAddress  = rset.getString(1)==null?"":rset.getString(1);
				ownCity  = rset.getString(2)==null?"":rset.getString(2);
				ownState  = rset.getString(3)==null?"":rset.getString(3);
				ownPin  = rset.getString(4)==null?"":rset.getString(4);
				ownCountry  = rset.getString(5)==null?"":rset.getString(5);
				ownPhone  = rset.getString(6)==null?"":rset.getString(6);
				ownEmail  = rset.getString(7)==null?"":rset.getString(7);
			}
			rset.close();
			stmt.close();
			
			//get TO list
			String to_list="";
			queryString1="SELECT EMAIL "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
					+ "AND ENTITY=? AND ADDR_FLAG=? AND JT_FLAG=? "
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? "
					+ "AND TYPE=? "//AND TO_INV='Y' "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.TYPE=B.TYPE) "
					+ "AND EMAIL IS NOT NULL";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_cd);
			stmt1.setString(3, contact_person_cd);
			stmt1.setString(4, "C");
			stmt1.setString(5, "P"+plant_seq);
			stmt1.setString(6, "Y");
			stmt1.setString(7, "Y");
			stmt1.setString(8, "Y");
			stmt1.setString(9, "DLNG");
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				String email=rset1.getString(1)==null?"":rset1.getString(1);
				if(to_list.equals(""))
				{
					to_list=email;
				}
				else
				{
					to_list+=", "+email;
				}
			}
			rset1.close();
			stmt1.close();
			
			//get CC list
			String cc_list="";
			String bcc_list="";
			queryString2="SELECT EMAIL "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO!=? "
					+ "AND ENTITY=? AND ADDR_FLAG=? AND JT_FLAG=? "
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? "
					+ "AND TYPE=? " //AND (TO_INV='N' OR TO_INV IS NULL) "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.TYPE=B.TYPE) "
					+ "AND EMAIL IS NOT NULL";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, counterparty_cd);
			stmt2.setString(3, contact_person_cd);
			stmt2.setString(4, "C");
			stmt2.setString(5, "P"+plant_seq);
			stmt2.setString(6, "Y");
			stmt2.setString(7, "Y");
			stmt2.setString(8, "Y");
			stmt2.setString(9, "DLNG");
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				String email=rset2.getString(1)==null?"":rset2.getString(1);
				if(cc_list.equals(""))
				{
					cc_list=email;
				}
				else
				{
					cc_list+=", "+email;
				}
			}
			rset2.close();
			stmt2.close();
			
			queryString3="SELECT EMAIL "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND ADDR_FLAG=? AND JT_FLAG=? "
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND (TO_JT=? OR TO_JT IS NULL) "
					+ "AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.TYPE=B.TYPE) "
					+ "AND EMAIL IS NOT NULL";
			stmt3=conn.prepareStatement(queryString3);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, comp_cd);
			stmt3.setString(3, "B");
			stmt3.setString(4, "P"+bu_plant_seq);
			stmt3.setString(5, "Y");
			stmt3.setString(6, "Y");
			stmt3.setString(7, "Y");
			stmt3.setString(8, "N");
			stmt3.setString(9, "DLNG");
			rset3=stmt3.executeQuery();
			while(rset3.next())
			{
				String email=rset3.getString(1)==null?"":rset3.getString(1);
				if(cc_list.equals(""))
				{
					cc_list=email;
				}
				else
				{
					cc_list+=", "+email;
				}
			}
			rset3.close();
			stmt3.close();
			
			queryString3="SELECT EMAIL "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND ADDR_FLAG=? AND JT_FLAG=? "
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND (TO_JT=? OR TO_JT IS NULL) "
					+ "AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.TYPE=B.TYPE) "
					+ "AND EMAIL IS NOT NULL";
			stmt3=conn.prepareStatement(queryString3);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, comp_cd);
			stmt3.setString(3, "B");
			stmt3.setString(4, "P"+bu_plant_seq);
			stmt3.setString(5, "Y");
			stmt3.setString(6, "Y");
			stmt3.setString(7, "Y");
			stmt3.setString(8, "B");
			stmt3.setString(9, "DLNG");
			rset3=stmt3.executeQuery();
			while(rset3.next())
			{
				String email=rset3.getString(1)==null?"":rset3.getString(1);
				if(bcc_list.equals(""))
				{
					bcc_list=email;
				}
				else
				{
					bcc_list+=", "+email;
				}
			}
			rset3.close();
			stmt3.close();
			
			if(contract_type.equals("W")) //ADDITIONAL EMAIL IDs FOR IGX ONLY
			{
				queryString="SELECT B.AGMT_NO,B.AGMT_NO,B.CONT_NO,B.CONT_REV "
						+ "FROM FMS_DLNG_ALLOC_MST A, FMS_SUPPLY_CONT_MST B "
		  				+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
		  				+ "AND A.GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND A.CONTRACT_TYPE=? "
		  				+ "AND A.PLANT_SEQ=? AND A.BU_SEQ=? "
						+ "AND A.NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) "
						+ "AND B.CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST C WHERE C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND C.AGMT_NO=B.AGMT_NO AND C.AGMT_REV=B.AGMT_REV AND C.CONT_NO=B.CONT_NO AND C.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE ";
				stmt0 = conn.prepareStatement(queryString);
				stmt0.setString(1, comp_cd);
				stmt0.setString(2, counterparty_cd);
				stmt0.setString(3, gas_dt);
				stmt0.setString(4, contract_type);
				stmt0.setString(5, plant_seq);
				stmt0.setString(6, bu_plant_seq);
				rset0=stmt0.executeQuery();
				while(rset0.next())
				{
					String agmt=rset0.getString(1)==null?"":rset0.getString(1);
					String agmtRev=rset0.getString(2)==null?"":rset0.getString(2);
					String cont=rset0.getString(3)==null?"":rset0.getString(3);
					String contRev=rset0.getString(4)==null?"":rset0.getString(4);
					
					Vector VGX_CD=new Vector();
					Vector VGX_BU_CD=new Vector();
					
					queryString4="SELECT GX_COUNTERPARTY_CD,GX_BU_SEQ_NO "
							+ "FROM FMS_SUPPLY_CONT_GX_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=?";
					stmt4 = conn.prepareStatement(queryString4);
					stmt4.setString(1, comp_cd);
					stmt4.setString(2, counterparty_cd);
					stmt4.setString(3, cont);
					stmt4.setString(4, contRev);
					stmt4.setString(5, agmt);
					stmt4.setString(6, agmtRev);
					stmt4.setString(7, contract_type);
					rset4=stmt4.executeQuery();
					while(rset4.next())
					{
						String gx_cd=rset4.getString(1)==null?"":rset4.getString(1);
						String gx_bu_cd=rset4.getString(2)==null?"":rset4.getString(2);
						
						if(!VGX_CD.contains(gx_cd) && !VGX_BU_CD.contains(gx_bu_cd))
						{
							VGX_CD.add(gx_cd);
							VGX_BU_CD.add(gx_bu_cd);
						}
					}
					rset4.close();
					stmt4.close();
					
					for(int i=0;i<VGX_CD.size();i++)
					{
						queryString5="SELECT EMAIL "
								+ "FROM FMS_ENTITY_CONTACT_MST A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND ENTITY=? AND ADDR_FLAG=? AND JT_FLAG=? "
								+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND TO_JT=? "
								+ "AND TYPE=? "
								+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
								+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.TYPE=B.TYPE) "
								+ "AND EMAIL IS NOT NULL";
						stmt5=conn.prepareStatement(queryString5);
						stmt5.setString(1, comp_cd);
						stmt5.setString(2, ""+VGX_CD.elementAt(i));
						stmt5.setString(3, "G");
						stmt5.setString(4, "B"+VGX_BU_CD.elementAt(i));
						stmt5.setString(5, "Y");
						stmt5.setString(6, "Y");
						stmt5.setString(7, "Y");
						stmt5.setString(8, "Y");
						stmt5.setString(9, "DLNG");
						rset5=stmt5.executeQuery();
						if(rset5.next())
						{
							String email=rset5.getString(1)==null?"":rset5.getString(1);
							if(to_list.equals(""))
							{
								to_list=email;
							}
							else
							{
								to_list+=", "+email;
							}
						}
						rset5.close();
						stmt5.close();
						
						//get CC list
						queryString6="SELECT EMAIL "
								+ "FROM FMS_ENTITY_CONTACT_MST A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND ENTITY=? AND ADDR_FLAG=? AND JT_FLAG=? "
								+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND (TO_JT=? OR TO_JT IS NULL) "
								+ "AND TYPE=? "
								+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
								+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.TYPE=B.TYPE) "
								+ "AND EMAIL IS NOT NULL";
						stmt6=conn.prepareStatement(queryString6);
						stmt6.setString(1, comp_cd);
						stmt6.setString(2, ""+VGX_CD.elementAt(i));
						stmt6.setString(3, "G");
						stmt6.setString(4, "B"+VGX_BU_CD.elementAt(i));
						stmt6.setString(5, "Y");
						stmt6.setString(6, "Y");
						stmt6.setString(7, "Y");
						stmt6.setString(8, "N");
						stmt6.setString(9, "DLNG");
						rset6=stmt6.executeQuery();
						while(rset6.next())
						{
							String email=rset6.getString(1)==null?"":rset6.getString(1);
							if(cc_list.equals(""))
							{
								cc_list=email;
							}
							else
							{
								cc_list+=", "+email;
							}
						}
						rset6.close();
						stmt6.close();
					}
				}
				rset0.close();
				stmt0.close();
			}
			
			String pdf_name="";
			queryString4="SELECT REMARK,ADDRESSED_TO,PDF_NM "
					+ "FROM FMS_DAILY_JOINT_TICKET A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONTRACT_TYPE=? AND PLANT_SEQ=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND BU_SEQ=? AND ENTITY_TYPE='C'";
			stmt4=conn.prepareStatement(queryString4);
			stmt4.setString(1, comp_cd);
			stmt4.setString(2, counterparty_cd);
			stmt4.setString(3, contract_type);
			stmt4.setString(4, plant_seq);
			stmt4.setString(5, gas_dt);
			stmt4.setString(6, bu_plant_seq);
			rset4=stmt4.executeQuery();
			if(rset4.next())
			{
				pdf_name=rset4.getString(3)==null?"":rset4.getString(3);
			}
			rset4.close();
			stmt4.close();
			
			String customer_abbr=utilBean.getCounterpartyABBR(conn,counterparty_cd);
			String plant_nm=utilBean.getCounterpartyPlantName(conn,counterparty_cd, comp_cd, plant_seq, "C");
			String company_abbr=utilBean.getCompanyAbbr(conn, comp_cd);
			
			String subject=company_abbr+"/";
			
			subject+="DLNG Daily Joint Ticket ("+customer_abbr+"-"+plant_nm+")-"+gas_dt;
			
			String directory_path=file_path+File.separator+pdf_name;
			
			File pdfFile = new File(directory_path);
			if(!pdfFile.exists())
			{
				pdf_name="";
			}
			
			String companyNm=utilBean.getCompanyName(conn,comp_cd);
			String mail_body="Dear Sir/Madam,"
					+ "\n\nPlease find enclosed the Joint Ticket details for the gas date "+gas_dt+"."
					+ "\nIn case of any discrepancy, please revert to us by end of today or else it will be construed as deemed acceptance."
					+ "\n\nIn case of any query, please contact us at "+ownEmail+""				
					+ "\n\n\nThank You,"
					+ "\n\n"+companyNm+""
					+ "\n"+ownAddress+", "
					+ "\n"+ownCity+", "+ownState+" - "+ownPin+""
					+ "\nEmail: "+ownEmail+""
					+ "\nPh: "+ownPhone+""
					+ "\n\n***This is an auto-generated email from the system, please do not reply to this email.";
			
			VMAIL_FROM_LIST.add(utilBean.getFromMailId(conn));
			VMAIL_TO_LIST.add(to_list);
			VMAIL_CC_LIST.add(cc_list);
			VMAIL_BCC_LIST.add(bcc_list);
			VMAIL_SUBJECT.add(subject);
			VMAIL_ATTACHMENT.add(pdf_name);
			VMAIL_ATTACHMENT_PATH.add(CommonVariable.join_ticket_pdf_path);
			VMAIL_BODY.add(mail_body);
			
			if(file.equals("ALL_MAIL") && !pdf_name.equals(""))
			{
				sendAllMail(to_list,mail_body,subject,directory_path,cc_list,bcc_list,"DLNG_JOINT_TICKET");
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getViewJointTicketInfo()
	{
		String function_nm="getViewJointTicketInfo()";
		try
		{
			view_join_ticket_info.put("company_nm", ""+utilBean.getCompanyName(conn,comp_cd));
			view_join_ticket_info.put("customer_nm", ""+utilBean.getCounterpartyName(conn,counterparty_cd));
			view_join_ticket_info.put("customer_abbr", ""+utilBean.getCounterpartyABBR(conn,counterparty_cd));
			view_join_ticket_info.put("plant_nm", ""+utilBean.getCounterpartyPlantName(conn,counterparty_cd, comp_cd, plant_seq, "C"));
			view_join_ticket_info.put("emp_nm", ""+utilBean.getEmpName(conn,emp_cd));
			
			String contact_person_nm="";
			String contact_person_phone="";
			String contact_person_fax="";
			queryString="SELECT CONTACT_PERSON,PHONE,FAX_1 "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND SEQ_NO=? AND ADDR_FLAG=? "
					+ "AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
					+ "AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY') AND A.TYPE=B.TYPE)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, "C");
			stmt.setString(4, contact_person_cd);
			stmt.setString(5, "P"+plant_seq);
			stmt.setString(6, "DLNG");
			stmt.setString(7, gas_dt);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				contact_person_nm=rset.getString(1)==null?"":rset.getString(1);
				contact_person_phone=rset.getString(2)==null?"":rset.getString(2);
				contact_person_fax=rset.getString(3)==null?"":rset.getString(3);
			}
			rset.close();
			stmt.close();
			
			String plantAddress="";
			String plantCity="";
			String plantState="";
			String plantPin="";
			String plantNm="";
			
			queryString1 = "SELECT PLANT_ADDR,PLANT_CITY,PLANT_STATE,PLANT_PIN,PLANT_NAME,PLANT_CITY "
					+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
					+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND COMPANY_CD=? AND SEQ_NO=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) ";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, counterparty_cd);
			stmt1.setString(2, "C");
			stmt1.setString(3, comp_cd);
			stmt1.setString(4, plant_seq);
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				plantAddress  = rset1.getString(1)==null?"":rset1.getString(1);
				plantCity  = rset1.getString(2)==null?"":rset1.getString(2);
				plantState  = rset1.getString(3)==null?"":rset1.getString(3);
				plantPin  = rset1.getString(4)==null?"":rset1.getString(4);
				plantNm  = rset1.getString(5)==null?"":rset1.getString(5);
			}
			rset1.close();
			stmt1.close();
			
			String bu_plantAddress="";
			String bu_plantCity="";
			String bu_plantState="";
			String bu_plantPin="";
			String bu_plantNm="";
			
			queryString2 = "SELECT PLANT_ADDR,PLANT_CITY,PLANT_STATE,PLANT_PIN,PLANT_NAME "
					+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
					+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND COMPANY_CD=? AND SEQ_NO=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) ";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, "B");
			stmt2.setString(3, comp_cd);
			stmt2.setString(4, bu_plant_seq);
			rset2=stmt2.executeQuery();
			if(rset2.next())
			{
				bu_plantAddress  = rset2.getString(1)==null?"":rset2.getString(1);
				bu_plantCity  = rset2.getString(2)==null?"":rset2.getString(2);
				bu_plantState  = rset2.getString(3)==null?"":rset2.getString(3);
				bu_plantPin  = rset2.getString(4)==null?"":rset2.getString(4);
				bu_plantNm  = rset2.getString(5)==null?"":rset2.getString(5);
			}
			rset2.close();
			stmt2.close();
			
			String ownPhone="";
			String ownEmail="";
			queryString3="SELECT ADDR,CITY,STATE,PIN,COUNTRY,PHONE,EMAIL "
					+ "FROM FMS_COMPANY_OWNER_ADDR_MST A "
					+ "WHERE COMPANY_CD=? AND ADDRESS_TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COMPANY_OWNER_ADDR_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.ADDRESS_TYPE=B.ADDRESS_TYPE "
					+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
			stmt3=conn.prepareStatement(queryString3);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, "C");
			rset3=stmt3.executeQuery();
			if(rset3.next())
			{
				ownPhone  = rset3.getString(6)==null?"":rset3.getString(6);
				ownEmail  = rset3.getString(7)==null?"":rset3.getString(7);
			}
			rset3.close();
			stmt3.close();
			
			String remark="";
			String seq_no="";
			queryString2="SELECT REMARK,NVL((SEQ_NO),'0') "
					+ "FROM FMS_DAILY_JOINT_TICKET A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
					+ "AND PLANT_SEQ=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? AND ENTITY_TYPE='C'";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, counterparty_cd);
			stmt2.setString(3, contract_type);
			stmt2.setString(4, plant_seq);
			stmt2.setString(5, gas_dt);
			stmt2.setString(6, bu_plant_seq);
			rset2=stmt2.executeQuery();
			if(rset2.next())
			{
				remark=rset2.getString(1)==null?"":rset2.getString(1);
				seq_no =""+(rset2.getInt(2)+1);
			}
			else
			{
				remark="";
				seq_no="1";
			}
			rset2.close();
			stmt2.close();
			
			view_join_ticket_info.put("contact_person_nm",contact_person_nm);
			view_join_ticket_info.put("contact_person_phone",contact_person_phone);
			view_join_ticket_info.put("contact_person_fax",contact_person_fax);
			view_join_ticket_info.put("plantAddress",plantAddress);
			view_join_ticket_info.put("plantCity",plantCity);
			view_join_ticket_info.put("plantState",plantState);
			view_join_ticket_info.put("plantPin",plantPin);
			view_join_ticket_info.put("plantNm",plantNm);
			
			view_join_ticket_info.put("bu_plantAddress",bu_plantAddress);
			view_join_ticket_info.put("bu_plantCity",bu_plantCity);
			view_join_ticket_info.put("bu_plantState",bu_plantState);
			view_join_ticket_info.put("bu_plantPin",bu_plantPin);
			view_join_ticket_info.put("bu_plantNm",bu_plantNm);
			
			view_join_ticket_info.put("ownPhone",ownPhone);
			view_join_ticket_info.put("ownEmail",ownEmail);
			view_join_ticket_info.put("seq_no",seq_no);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getViewJointTicketData()
	{
		String function_nm="getViewJointTicketData()";
		try
		{
			HashMap map_gas_dt = new HashMap();
			HashMap map_sell_nom = new HashMap();
			HashMap map_base = new HashMap();
			HashMap map_base_val = new HashMap();
			HashMap map_mmbtu = new HashMap();
			HashMap map_scm = new HashMap();
			HashMap map_grid = new HashMap();
			
			double tot_sellNom=0;
			double tot_mmbtu=0;
			double tot_scm=0;
			
			int count=0;
			int selCnt=0;

			queryString="SELECT TRUCK_TRANS_CD,TRUCK_CD,SUM(QTY_MMBTU),SUM(QTY_MT),SUM(GCV) "
					+ "FROM ( "
						+ "SELECT DISTINCT A.TRUCK_TRANS_CD,A.TRUCK_CD,SUM(A.QTY_MMBTU) AS QTY_MMBTU ,SUM(A.QTY_MT) AS QTY_MT ,SUM(A.GCV) AS GCV "
						+ "FROM FMS_DLNG_ALLOC_MST A, FMS_SUPPLY_CONT_MST B "
		  				+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
		  				+ "AND A.PLANT_SEQ=? AND A.BU_SEQ=? AND A.CONTRACT_TYPE=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						//+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) "
						+ "AND B.CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST C WHERE C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND C.AGMT_NO=B.AGMT_NO AND C.AGMT_REV=B.AGMT_REV AND C.CONT_NO=B.CONT_NO AND C.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND B.AGMT_BASE=? AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
						+ "GROUP BY A.TRUCK_TRANS_CD,A.TRUCK_CD "
						+ "UNION ALL "//LTCORA
						+ "SELECT DISTINCT A.TRUCK_TRANS_CD,A.TRUCK_CD,SUM(A.QTY_MMBTU) AS QTY_MMBTU ,SUM(A.QTY_MT) AS QTY_MT ,SUM(A.GCV) AS GCV "
						+ "FROM FMS_DLNG_ALLOC_MST A, FMS_LTCORA_CONT_CARGO_DTL B "
		  				+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
		  				+ "AND A.PLANT_SEQ=? AND A.BU_SEQ=? AND A.CONTRACT_TYPE=? "
		  				+ "AND A.QTY_MMBTU > 0 "
		  				+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						//+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) "
						+ "AND B.CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND C.AGMT_NO=B.AGMT_NO AND C.AGMT_REV=B.AGMT_REV AND C.CONT_NO=B.CONT_NO AND C.CONTRACT_TYPE=B.CONTRACT_TYPE AND C.BUY_SALE=B.BUY_SALE AND C.CARGO_NO=B.CARGO_NO) "
						+ "AND B.BUY_SALE=? AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO "
						+ "GROUP BY A.TRUCK_TRANS_CD,A.TRUCK_CD "
						+ ")"
					+ "GROUP BY TRUCK_TRANS_CD,TRUCK_CD";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++selCnt, comp_cd);
			stmt.setString(++selCnt, counterparty_cd);
			stmt.setString(++selCnt, gas_dt);
			stmt.setString(++selCnt, plant_seq);
			stmt.setString(++selCnt, bu_plant_seq);
			stmt.setString(++selCnt, contract_type);
			stmt.setString(++selCnt, "X");
			stmt.setString(++selCnt, comp_cd);
			stmt.setString(++selCnt, counterparty_cd);
			stmt.setString(++selCnt, gas_dt);
			stmt.setString(++selCnt, plant_seq);
			stmt.setString(++selCnt, bu_plant_seq);
			stmt.setString(++selCnt, contract_type);
			stmt.setString(++selCnt, "C");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				count+=1;
				map_gas_dt.put(""+count, gas_dt);
				
				String trans_cd=rset.getString(1)==null?"":rset.getString(1);
				String trans_truck=rset.getString(2)==null?"":rset.getString(2);
				
				double mmbtu=rset.getDouble(3);
				double scm=rset.getDouble(4);
				double base_val=rset.getDouble(5);
				
				map_base.put(""+count, "GCV");
				map_base_val.put(""+count, nf2.format(base_val));
				map_mmbtu.put(""+count, nf.format(mmbtu));
				map_scm.put(""+count, nf.format(scm));
				
				double seller_nom=0;
				queryString1="SELECT SUM(QTY_MMBTU),SUM(QTY_MT) "
		  				+ "FROM FMS_DLNG_SELLER_NOM_DTL A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
		  				+ "AND PLANT_SEQ=? AND BU_SEQ=? AND CONTRACT_TYPE=? "
		  				+ "AND TRUCK_TRANS_CD=? AND TRUCK_CD=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_SELLER_NOM_DTL B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
						+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, gas_dt);
				stmt1.setString(4, plant_seq);
				stmt1.setString(5, bu_plant_seq);
				stmt1.setString(6, contract_type);
				stmt1.setString(7, trans_cd);
				stmt1.setString(8, trans_truck);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					seller_nom=rset1.getDouble(1);
				}
				rset1.close();
				stmt1.close();
				
				map_sell_nom.put(""+count, nf.format(seller_nom));
				map_grid.put(""+count, ""+utilBean_DLNG.getTruckRegNo(conn, trans_truck));
				
				tot_mmbtu+=mmbtu;
				tot_scm+=scm;
				tot_sellNom+=seller_nom;
			}
			rset.close();
			stmt.close();
			
			
			count+=1;
			map_gas_dt.put(""+count, "Total");
			map_base.put(""+count,"");
			map_base_val.put(""+count, "-");
			map_mmbtu.put(""+count, nf.format(tot_mmbtu));
			map_scm.put(""+count, nf.format(tot_scm));
			map_sell_nom.put(""+count, nf.format(tot_sellNom));
			map_grid.put(""+count, "-");
			
			view_join_ticket_data.put("v_gas_dt", map_gas_dt);
			view_join_ticket_data.put("v_sell_nom", map_sell_nom);
			view_join_ticket_data.put("v_base", map_base);
			view_join_ticket_data.put("v_base_val", map_base_val);
			view_join_ticket_data.put("v_mmbtu", map_mmbtu);
			view_join_ticket_data.put("v_scm", map_scm);
			view_join_ticket_data.put("v_grid", map_grid);
			
			
			HashMap map_contracts = new HashMap();
			HashMap map_cont_mmbtu = new HashMap();
			
			count=0;
			selCnt=0;
			queryString3="SELECT AGMT_NO,CONT_NO,SUM(QTY_MMBTU),CONT_REF_NO,TRADE_REF_NO,CARGO_NO "
					+ "FROM ("
						+ "SELECT A.AGMT_NO,A.CONT_NO,SUM(A.QTY_MMBTU) AS QTY_MMBTU,B.CONT_REF_NO,B.TRADE_REF_NO,NULL AS CARGO_NO "
						+ "FROM FMS_DLNG_ALLOC_MST A, FMS_SUPPLY_CONT_MST B "
		  				+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
		  				+ "AND A.GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND A.CONTRACT_TYPE=? "
		  				+ "AND A.PLANT_SEQ=? AND A.BU_SEQ=? "
						+ "AND A.NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						//+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) "
						+ "AND B.CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST C WHERE C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND C.AGMT_NO=B.AGMT_NO AND C.AGMT_REV=B.AGMT_REV AND C.CONT_NO=B.CONT_NO AND C.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND B.AGMT_BASE=? AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
						+ "GROUP BY A.AGMT_NO,A.CONT_NO,B.CONT_REF_NO,B.TRADE_REF_NO,NULL "
						+ "UNION ALL "//LTCORA
						+ "SELECT A.AGMT_NO,A.CONT_NO,SUM(A.QTY_MMBTU) AS QTY_MMBTU,B.CARGO_REF,NULL AS TRADE_REF_NO,B.CARGO_NO AS CARGO_NO "
						+ "FROM FMS_DLNG_ALLOC_MST A, FMS_LTCORA_CONT_CARGO_DTL B "
		  				+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
		  				+ "AND A.PLANT_SEQ=? AND A.BU_SEQ=? AND A.CONTRACT_TYPE=? "
		  				+ "AND A.QTY_MMBTU > 0 "
		  				+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						//+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) "
						+ "AND B.CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND C.AGMT_NO=B.AGMT_NO AND C.AGMT_REV=B.AGMT_REV AND C.CONT_NO=B.CONT_NO AND C.CONTRACT_TYPE=B.CONTRACT_TYPE AND C.BUY_SALE=B.BUY_SALE AND C.CARGO_NO=B.CARGO_NO) "
						+ "AND B.BUY_SALE=? AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO "
						+ "GROUP BY A.AGMT_NO,A.CONT_NO,B.CARGO_REF,NULL,B.CARGO_NO "
					+ ")"
					+ "GROUP BY AGMT_NO,CONT_NO,CONT_REF_NO,TRADE_REF_NO,CARGO_NO";
			stmt3=conn.prepareStatement(queryString3);
			stmt3.setString(++selCnt, comp_cd);
			stmt3.setString(++selCnt, counterparty_cd);
			stmt3.setString(++selCnt, gas_dt);
			stmt3.setString(++selCnt, contract_type);
			stmt3.setString(++selCnt, plant_seq);
			stmt3.setString(++selCnt, bu_plant_seq);
			stmt3.setString(++selCnt, "X");
			stmt3.setString(++selCnt, comp_cd);
			stmt3.setString(++selCnt, counterparty_cd);
			stmt3.setString(++selCnt, gas_dt);
			stmt3.setString(++selCnt, plant_seq);
			stmt3.setString(++selCnt, bu_plant_seq);
			stmt3.setString(++selCnt, contract_type);
			stmt3.setString(++selCnt, "C");
			rset3=stmt3.executeQuery();
			while(rset3.next())
			{
				String agmt=rset3.getString(1)==null?"":rset3.getString(1);
				String cont=rset3.getString(2)==null?"":rset3.getString(2);
				double mmbtu=rset3.getDouble(3);
				String cont_ref=rset3.getString(4)==null?"":rset3.getString(4);
				String trade_ref=rset3.getString(5)==null?"":rset3.getString(5);
				String cargo_no=rset3.getString(6)==null?"":rset3.getString(6);
				
				String deal_no =utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt, "", cont, "", contract_type, cargo_no);
				
				if(contract_type.equals("X"))
				{
					cont_ref=trade_ref;
				}
				count+=1;
				if(!cont_ref.equals(""))
				{
					map_contracts.put(""+count, cont_ref);
				}
				else
				{
					map_contracts.put(""+count, deal_no);
				}
				map_cont_mmbtu.put(""+count, nf.format(mmbtu));
			}
			rset3.close();
			stmt3.close();
			
			view_join_ticket_data.put("v_contracts", map_contracts);
			view_join_ticket_data.put("v_cont_mmbtu", map_cont_mmbtu);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getDailyJointTicket()
	{
		String function_nm="getDailyJointTicket()";
		try
		{
			Vector TEMP_PLANT_SEQ = new Vector();
			Vector TEMP_BU_SEQ = new Vector();
			Vector TEMP_COUNTERPARTY_CD = new Vector();
			Vector TEMP_CONTRACT_TYPE = new Vector();
			Vector TEMP_COMBINATION = new Vector();
			Vector TEMP_CARGONO = new Vector();
			
			queryString="SELECT DISTINCT A.PLANT_SEQ,A.BU_SEQ,A.COUNTERPARTY_CD,A.CONTRACT_TYPE "
					+ "FROM FMS_DLNG_ALLOC_MST A, FMS_SUPPLY_CONT_MST B "
	  				+ "WHERE A.COMPANY_CD=? AND A.GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B "
					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD "
					+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
					+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) "
					+ "AND B.CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST C WHERE C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND C.AGMT_NO=B.AGMT_NO AND C.AGMT_REV=B.AGMT_REV AND C.CONT_NO=B.CONT_NO AND C.CONTRACT_TYPE=B.CONTRACT_TYPE) "
					+ "AND B.AGMT_BASE=? AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE";
			queryString+=" UNION ALL ";
			queryString+="SELECT DISTINCT A.PLANT_SEQ,A.BU_SEQ,A.COUNTERPARTY_CD,A.CONTRACT_TYPE "
					+ "FROM FMS_DLNG_ALLOC_MST A, FMS_LTCORA_CONT_CARGO_DTL B "
	  				+ "WHERE A.COMPANY_CD=? AND A.GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B "
					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD "
					+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
					+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) "
					+ "AND B.CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND C.AGMT_NO=B.AGMT_NO AND C.CONT_NO=B.CONT_NO AND C.CONTRACT_TYPE=B.CONTRACT_TYPE AND C.BUY_SALE=B.BUY_SALE AND B.CARGO_NO=C.CARGO_NO) "
					+ "AND B.BUY_SALE=? AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND B.CARGO_NO=A.CARGO_NO ";
			stmt0 =conn.prepareStatement(queryString);
			stmt0.setString(1, comp_cd);
			stmt0.setString(2, gas_dt);
			stmt0.setString(3, "X");
			stmt0.setString(4, comp_cd);
			stmt0.setString(5, gas_dt);
			stmt0.setString(6, "C");
			rset0=stmt0.executeQuery();
			while(rset0.next())
			{
				String plantSeq=rset0.getString(1)==null?"":rset0.getString(1);
				String buSeq=rset0.getString(2)==null?"":rset0.getString(2);
				String countpty_cd=rset0.getString(3)==null?"":rset0.getString(3);
				String cont_type=rset0.getString(4)==null?"":rset0.getString(4);
				//String cargo_no=rset0.getString(5)==null?"":rset0.getString(5);
				String combination=plantSeq+"-"+buSeq+"-"+countpty_cd+"-"+cont_type;
				
				TEMP_PLANT_SEQ.add(plantSeq);
				TEMP_BU_SEQ.add(buSeq);
				TEMP_COUNTERPARTY_CD.add(countpty_cd);
				TEMP_CONTRACT_TYPE.add(cont_type);
				TEMP_COMBINATION.add(combination);
				//TEMP_CARGONO.add(cargo_no);
			}
			rset0.close();
			stmt0.close();
			
			for(int i=0;i<TEMP_PLANT_SEQ.size();i++)
			{
				String plant_seq=""+TEMP_PLANT_SEQ.elementAt(i);
				String bu_plant_seq=""+TEMP_BU_SEQ.elementAt(i);
				String countpty_cd=""+TEMP_COUNTERPARTY_CD.elementAt(i);
				String cont_type=""+TEMP_CONTRACT_TYPE.elementAt(i);
				//String cargo_no=""+TEMP_CARGONO.elementAt(i);
				
				String exit_pt_mapping="C-"+countpty_cd+"-"+plant_seq;
				String sellContMapp=countpty_cd+"-"+cont_type+"-%-%-%-%";
				
				String customer_abbr=utilBean.getCounterpartyABBR(conn,countpty_cd);
				String customer_nm=utilBean.getCounterpartyName(conn,countpty_cd);
				String plant_nm=utilBean.getCounterpartyPlantName(conn,countpty_cd, comp_cd, plant_seq, "C");
				
				VCOUNTERPARTY_PLANT_SEQ.add(plant_seq);
				VCOUNTERPARTY_PLANT_ABBR.add(plant_nm);
				VBU_PLANT_SEQ.add(bu_plant_seq);
				VBU_PLANT_ABBR.add(utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B"));
				VCOUNTERPARTY_CD.add(countpty_cd);
				VCOUNTERPARTY_NM.add(customer_nm);
				VCOUNTERPARTY_ABBR.add(customer_abbr);
				VCONTRACT_TYPE.add(cont_type);
				//VCARGO_NO.add(cargo_no);
				VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_type));
				
				double mmbtu=0;
				double scm=0;
				
				if(cont_type.equals("O") || cont_type.equals("Q"))
				{
					queryString1="SELECT SUM(A.QTY_MMBTU),SUM(A.QTY_MT) "
							+ "FROM FMS_DLNG_ALLOC_MST A, FMS_LTCORA_CONT_CARGO_DTL B "
							+ "WHERE A.COMPANY_CD=? AND A.GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND A.PLANT_SEQ=? AND A.BU_SEQ=? "
							+ "AND A.COUNTERPARTY_CD=? AND A.CONTRACT_TYPE=? "
							+ "AND A.NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
							+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) "
							+ "AND B.CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL C WHERE C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND C.AGMT_NO=B.AGMT_NO AND C.AGMT_REV=B.AGMT_REV AND C.CONT_NO=B.CONT_NO AND C.CONTRACT_TYPE=B.CONTRACT_TYPE AND B.BUY_SALE=C.BUY_SALE AND C.CARGO_NO=B.CARGO_NO) "
							+ "AND B.BUY_SALE=? AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO ";
				}
				else
				{
					queryString1="SELECT SUM(QTY_MMBTU),SUM(QTY_MT) "
							+ "FROM FMS_DLNG_ALLOC_MST A, FMS_SUPPLY_CONT_MST B "
			  				+ "WHERE A.COMPANY_CD=? AND A.GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
			  				+ "AND A.PLANT_SEQ=? AND A.BU_SEQ=? "
			  				+ "AND A.COUNTERPARTY_CD=? AND A.CONTRACT_TYPE=? "
							+ "AND A.NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							//+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD "
							+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
							+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) "
							+ "AND B.CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST C WHERE C.COMPANY_CD=B.COMPANY_CD AND C.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND C.AGMT_NO=B.AGMT_NO AND C.AGMT_REV=B.AGMT_REV AND C.CONT_NO=B.CONT_NO AND C.CONTRACT_TYPE=B.CONTRACT_TYPE) "
							+ "AND B.AGMT_BASE=? AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE ";
				}
				String temp_queryString=queryString1;
				stmt1 = conn.prepareStatement(temp_queryString);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, gas_dt);
				stmt1.setString(3, plant_seq);
				stmt1.setString(4, bu_plant_seq);
				stmt1.setString(5, countpty_cd);
				stmt1.setString(6, cont_type);
				if(cont_type.equals("O") || cont_type.equals("Q"))
				{
					stmt1.setString(7, "C");
				}
				else
				{
					stmt1.setString(7, "X");
				}
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					mmbtu=rset1.getDouble(1);
					scm=rset1.getDouble(2);
				}
				rset1.close();
				stmt1.close();
				
				VQTY_MMBTU.add(nf1.format(mmbtu));
				VQTY_SCM.add(nf1.format(scm));
				
				queryString3="SELECT SUM(QTY_MMBTU),SUM(QTY_MT) "
		  				+ "FROM FMS_DLNG_SELLER_NOM_DTL A "
		  				+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_SELLER_NOM_DTL B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						//+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD "
						+ "AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
						+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				stmt3 = conn.prepareStatement(queryString3);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, countpty_cd);
				stmt3.setString(3, plant_seq);
				stmt3.setString(4, cont_type);
				stmt3.setString(5, bu_plant_seq);
				stmt3.setString(6, gas_dt);
				rset3=stmt3.executeQuery();
				if(rset3.next())
				{
					VSELLER_NOM.add(nf.format(rset3.getDouble(1)));
				}
				else
				{
					VSELLER_NOM.add(nf.format(0));
				}
				rset3.close();
				stmt3.close();
				
				Vector VTEMP_CONTACT_PERSON=new Vector();
				Vector VTEMP_CONTACT_PERSON_CD=new Vector();
				
				VTEMP_CONTACT_PERSON.add("--Select--");
				VTEMP_CONTACT_PERSON_CD.add("");
				queryString4="SELECT CONTACT_PERSON, SEQ_NO "
						+ "FROM FMS_ENTITY_CONTACT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND ENTITY=? AND ADDR_FLAG=? AND JT_FLAG=? "
						+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? "
						+ "AND TYPE=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
						+ "AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY') AND A.TYPE=B.TYPE)";
				stmt4 = conn.prepareStatement(queryString4);
				stmt4.setString(1, comp_cd);
				stmt4.setString(2, countpty_cd);
				stmt4.setString(3, "C");
				stmt4.setString(4, "P"+plant_seq);
				stmt4.setString(5, "Y");
				stmt4.setString(6, "Y");
				stmt4.setString(7, "Y");
				stmt4.setString(8, "DLNG");
				stmt4.setString(9, gas_dt);
				rset4=stmt4.executeQuery();
				while(rset4.next())
				{
					VTEMP_CONTACT_PERSON.add(rset4.getString(1)==null?"":rset4.getString(1));
					VTEMP_CONTACT_PERSON_CD.add(rset4.getString(2)==null?"":rset4.getString(2));
				}
				rset4.close();
				stmt4.close();
				
				String contact_person_cd="";
				queryString5="SELECT SEQ_NO "
						+ "FROM FMS_ENTITY_CONTACT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND ENTITY=? AND ADDR_FLAG=? AND JT_FLAG=? "
						+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND TO_JT=? "
						+ "AND TYPE=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
						+ "AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY') AND A.TYPE=B.TYPE)";
				stmt5 = conn.prepareStatement(queryString5);
				stmt5.setString(1, comp_cd);
				stmt5.setString(2, countpty_cd);
				stmt5.setString(3, "C");
				stmt5.setString(4, "P"+plant_seq);
				stmt5.setString(5, "Y");
				stmt5.setString(6, "Y");
				stmt5.setString(7, "Y");
				stmt5.setString(8, "Y");
				stmt5.setString(9, "DLNG");
				stmt5.setString(10, gas_dt);
				rset5=stmt5.executeQuery();
				if(rset5.next())
				{
					contact_person_cd=rset5.getString(1)==null?"0":rset5.getString(1);
				}
				rset5.close();
				stmt5.close();
				
				VCONTACT_PERSON.add(VTEMP_CONTACT_PERSON);
				VCONTACT_PERSON_CD.add(VTEMP_CONTACT_PERSON_CD);
				VSEL_CONTACT_PERSON_CD.add(contact_person_cd);
				
				String remark="";
				String contact_person="";
				String pdf_name="";
				String pdf_path="";
				String email_flag="";
				queryString4="SELECT REMARK,ADDRESSED_TO,PDF_NM,EMAIL_SENT "
						+ "FROM FMS_DAILY_JOINT_TICKET A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND PLANT_SEQ=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? AND ENTITY_TYPE='C'";
				stmt4=conn.prepareStatement(queryString4);
				stmt4.setString(1, comp_cd);
				stmt4.setString(2, countpty_cd);
				stmt4.setString(3, cont_type);
				stmt4.setString(4, plant_seq);
				stmt4.setString(5, gas_dt);
				stmt4.setString(6, bu_plant_seq);
				rset4=stmt4.executeQuery();
				if(rset4.next())
				{
					remark=rset4.getString(1)==null?"":rset4.getString(1);
					contact_person=rset4.getString(2)==null?"":rset4.getString(2);
					pdf_name=rset4.getString(3)==null?"":rset4.getString(3);
					email_flag=rset4.getString(4)==null?"":rset4.getString(4);
					pdf_path=CommonVariable.join_ticket_pdf_path;
				}
				rset4.close();
				stmt4.close();
				
				VREMARK.add(remark);
				VADDRESSED_PERSON.add(contact_person);
				VPDF_NAME.add(pdf_name);
				VPDF_PATH.add(pdf_path);
				VEMAIL_FLAG.add(email_flag);
				
				String pdf_exists="N";
				
				String directory_path=file_path+File.separator+pdf_name;
				
				File pdfFile = new File(directory_path);
				if(pdfFile.exists() && !pdf_name.equals(""))
				{
					pdf_exists="Y";
				}
				VPDF_EXISTS.add(pdf_exists);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getAllocToCustomer() 
	{
		String function_nm = "getAllocToCustomer()";
		try 
		{
			int cont2 = 0;
			double tot_alloc_mmbtu=0;
			double tot_alloc_mt=0;
			String queryString1 = "SELECT NOM_REV_NO, SUM(QTY_MMBTU),SUM(QTY_MT), "
					+ "COUNTERPARTY_CD,PLANT_SEQ,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TO_CHAR(GAS_DT,'DD/MM/YYYY'),BU_SEQ,CARGO_NO "
					+ "FROM FMS_DLNG_ALLOC_MST A " 
					+ "WHERE COMPANY_CD=? "
					+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B "
					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND A.BU_SEQ=B.BU_SEQ "
					+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
					+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
			if (!counterparty_cd.equals("") && !counterparty_cd.equals("0")) 
			{
				queryString1 += "AND COUNTERPARTY_CD=? ";
			}
			
			// SagarB20250923 Added below block if condition for DLNG-> DLNG Nomination vs Truck Loading
			if (!bu_seq.equals("") && !bu_seq.equals("0")) 
			{
				queryString1 += "AND A.BU_SEQ=? ";
			}
			queryString1 += "GROUP BY  NOM_REV_NO, COUNTERPARTY_CD, PLANT_SEQ, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE,GAS_DT,BU_SEQ,CARGO_NO ";
			queryString1 += "ORDER BY GAS_DT,NOM_REV_NO, COUNTERPARTY_CD, PLANT_SEQ, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE,BU_SEQ,CARGO_NO ";
			String temp_queryString1=queryString1;
			stmt1 = conn.prepareStatement(temp_queryString1);
			stmt1.setString(++cont2, comp_cd);
			stmt1.setString(++cont2, from_dt);
			stmt1.setString(++cont2, to_dt);
			if (!counterparty_cd.equals("") && !counterparty_cd.equals("0")) 
			{
				stmt1.setString(++cont2, counterparty_cd);
			}
			
			// SagarB20250923 Added below block if condition for DLNG-> DLNG Nomination vs Truck Loading
			if (!bu_seq.equals("") && !bu_seq.equals("0")) 
			{
				stmt1.setString(++cont2, bu_seq);
			}
			rset1 = stmt1.executeQuery();
			while (rset1.next()) 
			{
				double alloc_mmbtu = rset1.getDouble(2);
				double alloc_mt = rset1.getDouble(3);
				VNOM_REV_NO.add(rset1.getString(1) == null ? "" : rset1.getString(1));
				VQTY_ALLOC_MMBTU.add(nf1.format(alloc_mmbtu));
				VQTY_ALLOC_MT.add(nf1.format(alloc_mt));

				String countpty_cd = rset1.getString(4) == null ? "" : rset1.getString(4);
				String plant_seq = rset1.getString(5) == null ? "" : rset1.getString(5);
				String plant_abbr = utilBean.getCounterpartyPlantABBR(conn,countpty_cd, comp_cd, plant_seq, "C");

				String agmt = rset1.getString(6) == null ? "" : rset1.getString(6);
				String agmt_rev = rset1.getString(7) == null ? "" : rset1.getString(7);
				String cont = rset1.getString(8) == null ? "" : rset1.getString(8);
				String cont_rev = rset1.getString(9) == null ? "" : rset1.getString(9);
				String cont_type = rset1.getString(10) == null ? "" : rset1.getString(10);

				String GasDt = rset1.getString(11) == null ? "" : rset1.getString(11);
				String bu_seq = rset1.getString(12) == null ? "" : rset1.getString(12);
				String bu_abbr = utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_seq, "B");
				String cargo_no = rset1.getString(13) == null ? "" : rset1.getString(13);
				
				String dis_cont_mapping = utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, cargo_no);

				VGAS_DT.add(GasDt);
				VCOUNTERPARTY_CD.add(countpty_cd);
				VCOUNTERPARTY_NM.add("" + utilBean.getCounterpartyName(conn,countpty_cd));
				VCOUNTERPARTY_ABBR.add("" + utilBean.getCounterpartyABBR(conn,countpty_cd));
				VCOUNTERPARTY_PLANT_SEQ.add(plant_seq);
				VCOUNTERPARTY_PLANT_ABBR.add(plant_abbr);
				VBU_PLANT_ABBR.add(bu_abbr);
				VCONTRACT_TYPE.add(cont_type);
				VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_type));
				VDIS_CONT_MAPPING.add(dis_cont_mapping);

				
				double dcq = 0;
				String cont_ref = "";
				String agmt_base = "";
				String start_dt="";
				String end_dt="";
				
				String queryString2 = "SELECT CONT_REF_NO,DCQ,TRADE_REF_NO,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY') " 
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) AND CONTRACT_TYPE=? " 
						+ "AND AGMT_NO=? AND AGMT_REV=? ";
				if (!counterparty_cd.equals("") && !counterparty_cd.equals("0")) 
				{
					queryString2 += "AND COUNTERPARTY_CD=? ";
				}
				queryString2 += "UNION ";
				queryString2 += "SELECT CARGO_REF,CSOC_QTY,NULL,TO_CHAR(A.ACTUAL_RECPT_DT,'DD/MM/YYYY'),TO_CHAR(TO_DATE(A.ACTUAL_RECPT_DT,'DD/MM/YY')+NVL(STORAGE_DAYS-1,0)+NVL(STORAGE_EXT_DAYS,0),'DD/MM/YYYY') "
						+ "FROM FMS_LTCORA_CONT_CARGO_DTL A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND BUY_SALE=? AND AGMT_TYPE=? "
						+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE "
						+ "AND A.CARGO_NO=B.CARGO_NO) AND CONTRACT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND CARGO_NO=? ";
				if (!counterparty_cd.equals("") && !counterparty_cd.equals("0")) 
				{
					queryString2 += "AND COUNTERPARTY_CD=? ";
				}
				int stmt2_count=0;
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(++stmt2_count, comp_cd);
				stmt2.setString(++stmt2_count, countpty_cd);
				stmt2.setString(++stmt2_count, cont);
				stmt2.setString(++stmt2_count, cont_type);
				stmt2.setString(++stmt2_count, agmt);
				stmt2.setString(++stmt2_count, agmt_rev);
				if (!counterparty_cd.equals("") && !counterparty_cd.equals("0")) 
				{
					stmt2.setString(++stmt2_count, counterparty_cd);
				}
				stmt2.setString(++stmt2_count, comp_cd);
				stmt2.setString(++stmt2_count, countpty_cd);
				stmt2.setString(++stmt2_count, cont);
				stmt2.setString(++stmt2_count, "C");
				stmt2.setString(++stmt2_count, "A");
				stmt2.setString(++stmt2_count, cont_type);
				stmt2.setString(++stmt2_count, agmt);
				stmt2.setString(++stmt2_count, agmt_rev);
				stmt2.setString(++stmt2_count, cargo_no);
				if (!counterparty_cd.equals("") && !counterparty_cd.equals("0")) 
				{
					stmt2.setString(++stmt2_count, counterparty_cd);
				}
				rset2 = stmt2.executeQuery();
				if (rset2.next()) 
				{
					cont_ref = rset2.getString(1) == null ? "" : rset2.getString(1);
					dcq = rset2.getDouble(2);
					String trade_ref = rset2.getString(3) == null ? "" : rset2.getString(3);
					if (cont_type.equals("W")) 
					{
						cont_ref = trade_ref;
					}
					
					start_dt =rset2.getString(4) == null ? "" : rset2.getString(4);
					end_dt =rset2.getString(5) == null ? "" : rset2.getString(5);
				}
				rset2.close();
				stmt2.close();

				String variable_dcq = utilBean.getContVariableDCQ(conn,comp_cd, countpty_cd, agmt, cont, cont_type,GasDt);
				
				if(!cargo_no.equals("") && !cargo_no.equals("0"))
				{
					variable_dcq = utilBean.getCargoVariableCSOC(conn,comp_cd,countpty_cd,"C",agmt,cont,cont_type,cargo_no,GasDt);
				}
				
				if (!variable_dcq.equals("")) 
				{
					dcq = Double.parseDouble(variable_dcq);
				}
				
				VCONT_REF.add(cont_ref);
				VDCQ.add(nf.format(dcq));
				VSTART_DT.add(start_dt);
				VEND_DT.add(end_dt);
				
				tot_alloc_mmbtu+=alloc_mmbtu;
				tot_alloc_mt+=alloc_mt;
			}
			rset1.close();
			stmt1.close();
			total_alloc_mmbtu=nf1.format(tot_alloc_mmbtu);
			total_alloc_mt=nf1.format(tot_alloc_mt);
			
			
			// SagarB20250924 Added below block for showing BU list
			String queryString = "SELECT DISTINCT(BU_SEQ) " 
					+ "FROM FMS_DLNG_ALLOC_MST "
					+ "WHERE COMPANY_CD=? AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') ";
			if(!counterparty_cd.equals("0"))
			{
				queryString += "AND COUNTERPARTY_CD=? ";
			}
			
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "" + from_dt);
			stmt.setString(3, "" + to_dt);

			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(4, counterparty_cd);
			}
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				String bu_seq = rset.getString(1) == null ? "" : rset.getString(1);
				VBU_PLANT_SEQ.add(bu_seq);
				VBu_Plant_Abbr_List.add("" + utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_seq, "B"));
			}
			stmt.close();
			rset.close();
			
		} 
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getDeliveryReportDtls() 
	{
		String function_nm="getDeliveryReportDtls()";
		try
		{
			String queryString_temp="SELECT DISTINCT A.COMPANY_CD,A.COUNTERPARTY_CD "
					//+ ",A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,"
					//+ "A.CONT_REF_NO,"
					//+ "A.DCQ,A.CONT_NAME,A.MDCQ_PERCENTAGE,A.TRADE_REF_NO,A.AGMT_TYPE "
					+ "FROM FMS_SUPPLY_CONT_MST A,"
						+ "FMS_SUPPLY_CONT_PLANT B, "
						+ "FMS_SUPPLY_CONT_BU D, "
						+ "FMS_DLNG_ALLOC_MST E "
					+ "WHERE A.COMPANY_CD=? AND A.BUYER_NOM_FLAG=? AND A.FCC_FLAG=? AND A.IS_ALLOCATED=? "
					+ "AND A.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.END_DT>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND A.CONTRACT_TYPE IN ('F','E','W') "
					+ "AND A.CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
					+ ""
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ ""
					+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.AGMT_NO=D.AGMT_NO AND A.AGMT_REV=D.AGMT_REV "
					+ "AND A.CONT_NO=D.CONT_NO AND A.CONT_REV=D.CONT_REV AND A.CONTRACT_TYPE=D.CONTRACT_TYPE "
					+ ""
					+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.AGMT_NO=E.AGMT_NO AND A.AGMT_REV=E.AGMT_REV "
					+ "AND A.CONT_NO=E.CONT_NO "
					//+ "AND A.CONT_REV=E.CONT_REV  "
					+ "AND A.CONTRACT_TYPE=E.CONTRACT_TYPE "
					+ " AND E.PLANT_SEQ=B.PLANT_SEQ_NO AND E.BU_SEQ=D.PLANT_SEQ_NO "
					+ "	AND E.GAS_DT<=TO_DATE(?,'DD/MM/YYYY') AND E.GAS_DT>=TO_DATE(?,'DD/MM/YYYY') "
					+ " AND E.NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST F "
					+ " WHERE F.CONT_NO=E.CONT_NO AND F.AGMT_NO=E.AGMT_NO AND F.COMPANY_CD=E.COMPANY_CD AND F.COUNTERPARTY_CD=E.COUNTERPARTY_CD "
					+ " AND F.BU_SEQ=E.BU_SEQ "
					+ " AND F.PLANT_SEQ=E.PLANT_SEQ AND F.CONTRACT_TYPE=E.CONTRACT_TYPE AND F.GAS_DT=E.GAS_DT AND F.CARGO_NO=E.CARGO_NO) ";
			if(!counterparty_cd.equals("0"))
			{
				queryString_temp+= "AND E.COUNTERPARTY_CD=? ";
			}
			
			// SagarB20250923 Added below block if condition for DLNG-> DLNG Nomination vs Truck Loading
			if (!bu_seq.equals("") && !bu_seq.equals("0")) 
			{
				queryString_temp += "AND E.BU_SEQ=? ";
			}
			
			//PB20260120: ADDED FOR DLNG LTCORA SECTION 
			queryString_temp += "UNION ";
			queryString_temp += "SELECT DISTINCT A.COMPANY_CD,A.COUNTERPARTY_CD  "
					+ "FROM FMS_LTCORA_CONT_CARGO_DTL A, "
					+ "FMS_LTCORA_CONT_MST B, "
					+ "FMS_LTCORA_CONT_PLANT C, "
					+ "FMS_LTCORA_CONT_BU D, "
					+ "FMS_DLNG_ALLOC_MST E  "
					+ "WHERE A.COMPANY_CD=? AND B.BUYER_NOM=? AND B.FCC_FLAG=? "
					+ "AND B.START_DT<=TO_DATE(?,'DD/MM/YYYY') AND B.END_DT>=TO_DATE(?,'DD/MM/YYYY')  "
					+ "AND A.CONTRACT_TYPE IN ('Q','O') AND A.BUY_SALE='C' AND A.AGMT_TYPE='A' "
					+ "AND B.CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST C WHERE B.COMPANY_CD=C.COMPANY_CD AND B.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
					+ "AND B.BUY_SALE=C.BUY_SALE AND B.AGMT_TYPE=C.AGMT_TYPE AND B.AGMT_NO=C.AGMT_NO AND B.AGMT_REV=C.AGMT_REV AND B.CONT_NO=C.CONT_NO  "
					+ "AND B.CONTRACT_TYPE=C.CONTRACT_TYPE) "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO  "
					+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD AND A.BUY_SALE=C.BUY_SALE AND A.AGMT_TYPE=C.AGMT_TYPE AND A.AGMT_NO=C.AGMT_NO  "
					+ "AND A.CONT_NO=C.CONT_NO AND B.CONT_REV=C.CONT_REV AND A.CONTRACT_TYPE=C.CONTRACT_TYPE  "
					+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD AND A.BUY_SALE=D.BUY_SALE AND A.AGMT_TYPE=D.AGMT_TYPE AND A.AGMT_NO=D.AGMT_NO "
					+ "AND A.AGMT_REV=D.AGMT_REV AND A.CONT_NO=D.CONT_NO AND B.CONT_REV=D.CONT_REV AND A.CONTRACT_TYPE=D.CONTRACT_TYPE "
					+ "AND A.COMPANY_CD=E.COMPANY_CD AND A.COUNTERPARTY_CD=E.COUNTERPARTY_CD AND A.AGMT_NO=E.AGMT_NO AND A.AGMT_REV=E.AGMT_REV "
					+ "AND A.CONT_NO=E.CONT_NO AND A.CONTRACT_TYPE=E.CONTRACT_TYPE AND E.PLANT_SEQ=C.PLANT_SEQ_NO AND E.BU_SEQ=D.PLANT_SEQ_NO "
					+ "AND E.GAS_DT<=TO_DATE(?,'DD/MM/YYYY') AND E.GAS_DT>=TO_DATE(?,'DD/MM/YYYY')  "
					+ "AND E.NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST F  "
					+ "WHERE F.CONT_NO=E.CONT_NO AND F.AGMT_NO=E.AGMT_NO AND F.COMPANY_CD=E.COMPANY_CD AND F.COUNTERPARTY_CD=E.COUNTERPARTY_CD  "
					+ "AND F.BU_SEQ=E.BU_SEQ  "
					+ "AND F.PLANT_SEQ=E.PLANT_SEQ AND F.CONTRACT_TYPE=E.CONTRACT_TYPE AND F.GAS_DT=E.GAS_DT AND F.CARGO_NO=E.CARGO_NO)  "
					+ "AND A.CARGO_NO=E.CARGO_NO ";
			if(!counterparty_cd.equals("0"))
			{
				queryString_temp+= "AND E.COUNTERPARTY_CD=? ";
			}
			
			// SagarB20250923 Added below block if condition for DLNG-> DLNG Nomination vs Truck Loading
			if (!bu_seq.equals("") && !bu_seq.equals("0")) 
			{
				queryString_temp += "AND E.BU_SEQ=? ";
			}
			
			int stmt_cnt=0;
			stmt_temp = conn.prepareStatement(queryString_temp);
			stmt_temp.setString(++stmt_cnt, comp_cd);
			stmt_temp.setString(++stmt_cnt, "Y");
			stmt_temp.setString(++stmt_cnt, "Y");
			stmt_temp.setString(++stmt_cnt, "Y");
			stmt_temp.setString(++stmt_cnt, to_dt);
			stmt_temp.setString(++stmt_cnt, from_dt);
			stmt_temp.setString(++stmt_cnt, to_dt);
			stmt_temp.setString(++stmt_cnt, from_dt);
			if(!counterparty_cd.equals("0"))
			{
				stmt_temp.setString(++stmt_cnt, counterparty_cd);
			}
			// SagarB20250923 Added below block if condition for DLNG-> DLNG Nomination vs Truck Loading
			if (!bu_seq.equals("") && !bu_seq.equals("0")) 
			{
				stmt_temp.setString(++stmt_cnt, bu_seq);
			}
			//pb20260120: added for handling DLNG LTCORA section 
			stmt_temp.setString(++stmt_cnt, comp_cd);
			stmt_temp.setString(++stmt_cnt, "Y");
			stmt_temp.setString(++stmt_cnt, "Y");
			stmt_temp.setString(++stmt_cnt, to_dt);
			stmt_temp.setString(++stmt_cnt, from_dt);
			stmt_temp.setString(++stmt_cnt, to_dt);
			stmt_temp.setString(++stmt_cnt, from_dt);
			if(!counterparty_cd.equals("0"))
			{
				stmt_temp.setString(++stmt_cnt, counterparty_cd);
			}
			// SagarB20250923 Added below block if condition for DLNG-> DLNG Nomination vs Truck Loading
			if (!bu_seq.equals("") && !bu_seq.equals("0")) 
			{
				stmt_temp.setString(++stmt_cnt, bu_seq);
			}
			
			ResultSet rset_temp=stmt_temp.executeQuery();
			while(rset_temp.next())
			{
				int index=0;
				
				String companyCd = rset_temp.getString(1)==null?"":rset_temp.getString(1);
				String countpty_cd = rset_temp.getString(2)==null?"":rset_temp.getString(2);
				String agmt = "";// rset_temp.getString(3)==null?"":rset_temp.getString(3);
				String agmt_rev = "";// rset_temp.getString(4)==null?"":rset_temp.getString(4);
				String cont = "";// rset_temp.getString(5)==null?"":rset_temp.getString(5);
				String cont_rev = "";// rset_temp.getString(6)==null?"":rset_temp.getString(6);
				String cont_type = "";// rset_temp.getString(7)==null?"":rset_temp.getString(7);
				String cont_ref = "";// rset_temp.getString(8)==null?"":rset_temp.getString(8);
				
				String cargo_no = "";

				String cont_name = "";// rset_temp.getString(10)==null?"":rset_temp.getString(10);
				String trade_ref = "";// rset_temp.getString(12)==null?"":rset_temp.getString(12);
				if(cont_type.equals("W")) {
					cont_ref=trade_ref;
				}

				String agmtType = "";// rset_temp.getString(13)==null?"":rset_temp.getString(13);
				
				//String tittle_dis_cont_mapping= utilBean.NewDealMappingId(companyCd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
				
				VTITTLE_DISP_CONT_NO.add(utilBean.getCounterpartyABBR(conn, countpty_cd)+"-"+utilBean.getCounterpartyName(conn, countpty_cd));
				VCONT_REF.add(cont_ref);
				
				int stmt_cnt1=0;
				
				String queryString_temp1 = "SELECT DISTINCT PLANT_SEQ "
						+ "FROM FMS_DLNG_ALLOC_MST A "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
						//+ "AND A.AGMT_NO=? AND A.AGMT_REV=? "
						//+ "AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND A.CONT_REV=? "
						+ "AND A.GAS_DT<=TO_DATE(?,'DD/MM/YYYY') AND A.GAS_DT>=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.NOM_REV_NO = (SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B "
						+ "	WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "	AND B.BU_SEQ=A.BU_SEQ "
						+ "	AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)";
				
				// SagarB20250923 Added below block if condition for DLNG-> DLNG Nomination vs Truck Loading
				if (!bu_seq.equals("") && !bu_seq.equals("0")) 
				{
					queryString_temp1 += " AND A.BU_SEQ=? ";
				}
				stmt_cnt=0;
				stmt1 = conn.prepareStatement(queryString_temp1);
				stmt1.setString(++stmt_cnt1, companyCd);
				stmt1.setString(++stmt_cnt1, countpty_cd);
				/*stmt1.setString(++stmt_cnt1, agmt);
				stmt1.setString(++stmt_cnt1, agmt_rev);
				stmt1.setString(++stmt_cnt1, cont);
				stmt1.setString(++stmt_cnt1, cont_type);
				stmt1.setString(++stmt_cnt1, cont_rev);*/
				stmt1.setString(++stmt_cnt1, to_dt);
				stmt1.setString(++stmt_cnt1, from_dt);
				
				// SagarB20250923 Added below block if condition for DLNG-> DLNG Nomination vs Truck Loading
				if (!bu_seq.equals("") && !bu_seq.equals("0")) 
				{
					stmt1.setString(++stmt_cnt1, bu_seq);
				}
				rset1=stmt1.executeQuery();
				while(rset1.next()) 
				{
					index++;
					int sub_index=0;
					
					String plant_seq1 = rset1.getString(1)==null?"":rset1.getString(1);
					String plant_abbr1=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, companyCd, plant_seq1, "C");
					//String buCd1 = rset1.getString(2)==null?"":rset1.getString(2);
					//String bu_plant_seq1 = rset1.getString(3)==null?"":rset1.getString(3);
					//String bu_plant_abbr1=utilBean.getCounterpartyPlantABBR(conn,buCd1, comp_cd, bu_plant_seq1, "B");
					
					VCONT_BU_PLANT_SEQ.add(plant_seq1);
					VCONT_BU_PLANT_MAP.add(plant_abbr1+"");
					
					String plant_seq = plant_seq1;
					String plant_abbr=utilBean.getCounterpartyPlantABBR(conn,countpty_cd, companyCd, plant_seq, "C");
					//String buCd = buCd1;
					//String bu_plant_seq = bu_plant_seq1;
					//String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,buCd, comp_cd, bu_plant_seq, "B");

					String dis_cont_mapping= utilBean.NewDealMappingId(companyCd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");

					int selCnt=0;
					double tot_buyer_mmbtu=0;
					double tot_seller_mmbtu=0;
					double tot_alloc_mmbtu=0;
					double tot_alloc_mt=0;
					String queryString="SELECT NOM_REV_NO,BASE,GCV,NCV,QTY_MMBTU,QTY_MT,"
							+ "TO_CHAR(GEN_DT,'DD/MM/YYYY'),GEN_TIME,"
							+ "TRUCK_TRANS_CD,TRUCK_CD,GCV_MMBTU,"
							+ "TO_CHAR(LOAD_START_DT,'DD/MM/YYYY'),LOAD_START_TIME,TO_CHAR(LOAD_END_DT,'DD/MM/YYYY'),LOAD_END_TIME,"
							+ "TO_CHAR(GAS_DT,'DD/MM/YYYY'),COMPANY_CD,BU_SEQ,CONT_NO,AGMT_NO,CONTRACT_TYPE,CARGO_NO "
							+ "FROM FMS_DLNG_ALLOC_MST A "
							+ "WHERE "
							//+ "CONT_NO=? AND AGMT_NO=? "
							//+ "AND "
							+ "COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND PLANT_SEQ=? "
							//+ "AND CONTRACT_TYPE=? "
							//+ "AND BU_SEQ=? "
							+ "AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND B.BU_SEQ=A.BU_SEQ "
							+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
							+ "AND B.GAS_DT=A.GAS_DT "
							//+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND A.TRUCK_CD=B.TRUCK_CD"
							+ "AND A.FILL_STATION_CD=B.FILL_STATION_CD AND A.BAY_CD=B.BAY_CD AND A.SLOT_START_TIME=B.SLOT_START_TIME AND A.SLOT_END_TIME=B.SLOT_END_TIME "
							+ "AND B.CARGO_NO=A.CARGO_NO"
							+ ") ";

					
							// SagarB20250923 Added below block if condition for DLNG-> DLNG Nomination vs Truck Loading
							if (!bu_seq.equals("") && !bu_seq.equals("0")) 
							{
								queryString += "AND A.BU_SEQ=? ";
							}
							queryString += "ORDER BY GAS_DT ASC ";
					stmt3 = conn.prepareStatement(queryString);
					/*stmt3.setString(++selCnt, cont);
					stmt3.setString(++selCnt, agmt);*/
					stmt3.setString(++selCnt, companyCd);
					stmt3.setString(++selCnt, countpty_cd);
					stmt3.setString(++selCnt, plant_seq);
					//stmt3.setString(++selCnt, cont_type);
					//stmt3.setString(++selCnt, bu_plant_seq);
					stmt3.setString(++selCnt, to_dt);
					stmt3.setString(++selCnt, from_dt);
					
					// SagarB20250923 Added below block if condition for DLNG-> DLNG Nomination vs Truck Loading
					if (!bu_seq.equals("") && !bu_seq.equals("0")) 
					{
						stmt3.setString(++selCnt, bu_seq);
					}
					rset3=stmt3.executeQuery();
					while(rset3.next())
					{
						sub_index++;
						
						String buCd = rset3.getString(17)==null?"":rset3.getString(17);
						String bu_plant_seq = rset3.getString(18)==null?"":rset3.getString(18);
						String bu_plant_abbr=utilBean.getCounterpartyPlantABBR(conn,buCd, buCd, bu_plant_seq, "B");
						cont = rset3.getString(19)==null?"":rset3.getString(19);
						agmt = rset3.getString(20)==null?"":rset3.getString(20);
						cont_type = rset3.getString(21)==null?"":rset3.getString(21);
						cargo_no = rset3.getString(22)==null?"":rset3.getString(22);
						
						
						VCOUNTERPARTY_CD.add(countpty_cd);
						VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,countpty_cd));
						VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,countpty_cd));
						VDIS_CONT_MAPPING.add(dis_cont_mapping);
						VCOUNTERPARTY_PLANT_SEQ.add(plant_seq);
						VCOUNTERPARTY_PLANT_ABBR.add(plant_abbr);
						VBU_CD.add(buCd);
//						VBU_PLANT_SEQ.add(bu_plant_seq);		SagarB20250926 Commented this since it was not in use
						VBU_PLANT_ABBR.add(bu_plant_abbr);
						
						double qty_alloc_mmbtu=rset3.getDouble(5);
						double qty_alloc_mt=rset3.getDouble(6);
			  			VQTY_MMBTU.add(nf1.format(rset3.getDouble(5)));
			  			VQTY_MT.add(nf1.format(rset3.getDouble(6)));
			  			VLOAD_START_DT.add(rset3.getString(12)==null?"":rset3.getString(12));
			  			VLOAD_START_TIME.add(rset3.getString(13)==null?"":rset3.getString(13));
			  			VLOAD_END_DT.add(rset3.getString(14)==null?"":rset3.getString(14));
			  			VLOAD_END_TIME.add(rset3.getString(15)==null?"":rset3.getString(15));
			  			VGAS_DT.add(rset3.getString(16)==null?"":rset3.getString(16));
			  			String gas_dt =rset3.getString(16)==null?"":rset3.getString(16);
						String truck_trans_cd =rset3.getString(9)==null?"":rset3.getString(9);
						String truck_cd =rset3.getString(10)==null?"":rset3.getString(10);
						
						String tittle_dis_cont_mapping= utilBean.NewDealMappingId(companyCd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, cargo_no);
						VCONT_NAME.add(tittle_dis_cont_mapping);
						
						tot_alloc_mmbtu+=qty_alloc_mmbtu;
						tot_alloc_mt+=qty_alloc_mt;

						double buyer_nom_mmbtu = 0;
						double seller_nom_mmbtu = 0;
						
						int selCnt1=0;
						
						String queryString2="SELECT QTY_MMBTU "
								+ "FROM FMS_DLNG_BUYER_NOM_DTL A "
								+ "WHERE CONT_NO=? AND AGMT_NO=? "
								+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
								+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND TRUCK_TRANS_CD=? AND TRUCK_CD=? AND CARGO_NO=? "
								+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_BUYER_NOM_DTL B "
								+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
								+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
								+ "AND B.BU_SEQ=A.BU_SEQ "
								+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
								+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO "
								//+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND A.TRUCK_CD=B.TRUCK_CD"
								+ ") ";
						stmt2 = conn.prepareStatement(queryString2);
						stmt2.setString(++selCnt1, cont);
						stmt2.setString(++selCnt1, agmt);
						stmt2.setString(++selCnt1, companyCd);
						stmt2.setString(++selCnt1, countpty_cd);
						stmt2.setString(++selCnt1, plant_seq);
						stmt2.setString(++selCnt1, cont_type);
						stmt2.setString(++selCnt1, bu_plant_seq);
						stmt2.setString(++selCnt1, gas_dt);
						stmt2.setString(++selCnt1, truck_trans_cd);
						stmt2.setString(++selCnt1, truck_cd);
						stmt2.setString(++selCnt1, cargo_no);
						rset2=stmt2.executeQuery();
						if(rset2.next())
						{
							buyer_nom_mmbtu = rset2.getDouble(1);
						}
						rset2.close();
						stmt2.close();
						
						
						int selCnt2=0;
						
						String queryString5="SELECT QTY_MMBTU "
								+ "FROM FMS_DLNG_SELLER_NOM_DTL A "
								+ "WHERE CONT_NO=? AND AGMT_NO=? "
								+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
								+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND TRUCK_TRANS_CD=? AND TRUCK_CD=? AND CARGO_NO=? "
								+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_SELLER_NOM_DTL B "
								+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
								+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
								+ "AND B.BU_SEQ=A.BU_SEQ "
								+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
								+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO "
								//+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND A.TRUCK_CD=B.TRUCK_CD"
								+ ") ";
						stmt5 = conn.prepareStatement(queryString5);
						stmt5.setString(++selCnt2, cont);
						stmt5.setString(++selCnt2, agmt);
						stmt5.setString(++selCnt2, companyCd);
						stmt5.setString(++selCnt2, countpty_cd);
						stmt5.setString(++selCnt2, plant_seq);
						stmt5.setString(++selCnt2, cont_type);
						stmt5.setString(++selCnt2, bu_plant_seq);
						stmt5.setString(++selCnt2, gas_dt);
						stmt5.setString(++selCnt2, truck_trans_cd);
						stmt5.setString(++selCnt2, truck_cd);
						stmt5.setString(++selCnt2, cargo_no);
						rset5=stmt5.executeQuery();
						if(rset5.next())
						{
							seller_nom_mmbtu = rset5.getDouble(1);
						}
						rset5.close();
						stmt5.close();
						tot_seller_mmbtu+=seller_nom_mmbtu;
						String buyNomQty = ""+nf.format(buyer_nom_mmbtu);
						String sellNomQty = ""+nf.format(seller_nom_mmbtu);
						
						if(Double.doubleToRawLongBits(buyer_nom_mmbtu)==Double.doubleToRawLongBits(0))
						{
							buyNomQty = "-";
						}
						
						if(!sellNomQty.equals("") && buyNomQty.equals("-"))
						{
							tot_buyer_mmbtu+=seller_nom_mmbtu;
						}
						else
						{
							tot_buyer_mmbtu+=buyer_nom_mmbtu;
						}
						
			  			VBUYER_NOM.add(buyNomQty);
			  			VSELLER_NOM.add(sellNomQty);
						
			  			String queryString12="SELECT TRUCK_REG_NUM,TRUCK_VOL_M3,TRUCK_VOL_MT,TRUCK_LOAD_CAP "
								+ "FROM FMS_TRUCK_MST A "
								+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_TRUCK_MST B WHERE A.TRUCK_CD=B.TRUCK_CD "
								+ "AND TO_DATE(B.EFF_DT,'DD/MM/YYYY')<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) "
								+ "AND ACTIVE_FLAG=? AND TRUCK_CD=? ";
						stmt4 = conn.prepareStatement(queryString12);
						stmt4.setString(1, "Y");
						stmt4.setString(2, truck_cd);
						rset4=stmt4.executeQuery();
						while(rset4.next())
						{
							String truck_reg_num = rset4.getString(1)==null?"":rset4.getString(1);
							
							Float truck_vol_m3 = rset4.getFloat(2);
							Float truck_vol_mt = rset4.getFloat(3);
							
							Double truck_load_cap = rset4.getDouble(4);
							
							VTRUCK_TRANS_CD.add(truck_trans_cd);
							VTRUCK_CD.add(truck_cd);
							VTRUCK_REG_NUM.add(truck_reg_num);
						}
						rset4.close();
						stmt4.close();
					}
					rset3.close();
					stmt3.close();
					
					VSUB_INDEX.add(sub_index);
					VTOTAL_SELLER_MMBTU.add(nf.format(tot_seller_mmbtu));
					VTOTAL_BUYER_MMBTU.add(nf.format(tot_buyer_mmbtu));
					VTOTAL_ALLOC_MMBTU.add(nf1.format(tot_alloc_mmbtu));
					VTOTAL_ALLOC_MT.add(nf1.format(tot_alloc_mt));
					
				}
				rset1.close();
				stmt1.close();
				
				VINDEX.add(index);
			}
			rset_temp.close();
			stmt_temp.close();
			
			
			// SagarB20250924 Added below block for showing BU list
			String queryString = "SELECT DISTINCT(BU_SEQ) " 
					+ "FROM FMS_DLNG_ALLOC_MST "
					+ "WHERE COMPANY_CD=? AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') ";
			if(!counterparty_cd.equals("0"))
			{
				queryString += "AND COUNTERPARTY_CD=? ";
			}
			
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "" + from_dt);
			stmt.setString(3, "" + to_dt);

			if(!counterparty_cd.equals("0"))
			{
				stmt.setString(4, counterparty_cd);
			}
			rset = stmt.executeQuery();
			
			while (rset.next()) {
				String bu_seq = rset.getString(1) == null ? "" : rset.getString(1);
				VBU_PLANT_SEQ.add(bu_seq);
				VBu_Plant_Abbr_List.add("" + utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_seq, "B"));
			}
			stmt.close();
			rset.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getAllocCounterpartyList()
	{
		String function_nm="getAllocCounterpartyList()";
		try
		{
			int selCount=0;
			String queryString="SELECT DISTINCT(COUNTERPARTY_CD) "
					+ "FROM FMS_DLNG_ALLOC_MST A "
					+ "WHERE COMPANY_CD=? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(++selCount, comp_cd);
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
	
	public void getSendMailSellerToCustomerDetail()
	{
		String function_nm="getSendMailSellerToCustomerDetail()";
		try
		{
			String ownAddress="";
			String ownCity="";
			String ownState="";
			String ownPin="";
			String ownCountry="";
			String ownPhone="";
			String ownEmail="";
			String sysdate=dateUtil.getSysdate();
			
			queryString="SELECT ADDR,CITY,STATE,PIN,COUNTRY,PHONE,EMAIL "
					+ "FROM FMS_COMPANY_OWNER_ADDR_MST A "
					+ "WHERE COMPANY_CD=? AND ADDRESS_TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COMPANY_OWNER_ADDR_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.ADDRESS_TYPE=B.ADDRESS_TYPE "
					+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
			stmt0=conn.prepareStatement(queryString);
			stmt0.setString(1, comp_cd);
			stmt0.setString(2, "C");
			rset0=stmt0.executeQuery();
			if(rset0.next())
			{
				ownAddress  = rset0.getString(1)==null?"":rset0.getString(1);
				ownCity  = rset0.getString(2)==null?"":rset0.getString(2);
				ownState  = rset0.getString(3)==null?"":rset0.getString(3);
				ownPin  = rset0.getString(4)==null?"":rset0.getString(4);
				ownCountry  = rset0.getString(5)==null?"":rset0.getString(5);
				ownPhone  = rset0.getString(6)==null?"":rset0.getString(6);
				ownEmail  = rset0.getString(7)==null?"":rset0.getString(7);
			}
			rset0.close();
			stmt0.close();
			
			//get TO list
			String to_list="";
			queryString="SELECT EMAIL "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
					+ "AND ENTITY=? AND ADDR_FLAG=? AND NOM_FLAG=? "
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? "
					+ "AND TYPE=? "//AND TO_INV='Y' "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.TYPE=B.TYPE) "
					+ "AND EMAIL IS NOT NULL";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, contact_person_cd);
			stmt.setString(4, "C");
			stmt.setString(5, "P"+plant_seq);
			stmt.setString(6, "Y");
			stmt.setString(7, "Y");
			stmt.setString(8, "Y");
			stmt.setString(9, "DLNG");
			rset=stmt.executeQuery();
			if(rset.next())
			{
				String email=rset.getString(1)==null?"":rset.getString(1);
				if(!to_list.contains(email))
				{
					if(to_list.equals(""))
					{
						to_list=email;
					}
					else
					{
						to_list+=", "+email;
					}
				}
			}
			rset.close();
			stmt.close();
			
			//get CC list
			String cc_list="";
			String bcc_list="";
			queryString1="SELECT EMAIL "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO!=? "
					+ "AND ENTITY=? AND ADDR_FLAG=? AND NOM_FLAG=? "
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? "
					+ "AND TYPE=? " //AND (TO_INV='N' OR TO_INV IS NULL) "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.TYPE=B.TYPE) "
					+ "AND EMAIL IS NOT NULL";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_cd);
			stmt1.setString(3, contact_person_cd);
			stmt1.setString(4, "C");
			stmt1.setString(5, "P"+plant_seq);
			stmt1.setString(6, "Y");
			stmt1.setString(7, "Y");
			stmt1.setString(8, "Y");
			stmt1.setString(9, "DLNG");
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				String email=rset1.getString(1)==null?"":rset1.getString(1);
				if(!to_list.contains(email))
				{
					if(cc_list.equals(""))
					{
						cc_list=email;
					}
					else
					{
						cc_list+=", "+email;
					}
				}
			}
			rset1.close();
			stmt1.close();
			
			queryString5="SELECT DISTINCT BU_SEQ "
	  				+ "FROM FMS_DLNG_SELLER_NOM_DTL "
	  				+ "WHERE COMPANY_CD=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND COUNTERPARTY_CD=? "
	  				+ "AND CONTRACT_TYPE=? AND CONT_NO=? AND AGMT_NO=? AND PLANT_SEQ=?";
			stmt5=conn.prepareStatement(queryString5);
			stmt5.setString(1, comp_cd);
			stmt5.setString(2, gas_dt);
			stmt5.setString(3, counterparty_cd);
			stmt5.setString(4, contract_type);
			stmt5.setString(5, cont_no);
			stmt5.setString(6, agmt_no);
			stmt5.setString(7, plant_seq);
			rset5=stmt5.executeQuery();
			while(rset5.next())
			{
				String bu_seq=rset5.getString(1)==null?"":rset5.getString(1);
				// CC List selected from Company Correspondence Address
				queryString2="SELECT EMAIL "
						+ "FROM FMS_ENTITY_CONTACT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND ENTITY=? AND ADDR_FLAG IN (?,?) AND NOM_FLAG=? "
						+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND (TO_NOM=? OR TO_NOM IS NULL) "
						+ "AND TYPE=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
						+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.TYPE=B.TYPE) "
						+ "AND EMAIL IS NOT NULL";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, comp_cd);
				stmt2.setString(3, "B");
				stmt2.setString(4, "C");
				stmt2.setString(5, "P"+bu_seq);
				stmt2.setString(6, "Y");
				stmt2.setString(7, "Y");
				stmt2.setString(8, "Y");
				stmt2.setString(9, "N");
				stmt2.setString(10, "DLNG");
				rset2=stmt2.executeQuery();
				while(rset2.next())
				{
					String email=rset2.getString(1)==null?"":rset2.getString(1);
					if(!to_list.contains(email))
					{
						if(cc_list.equals(""))
						{
							cc_list=email;
						}
						else
						{
							cc_list+=", "+email;
						}
					}
				}
				rset2.close();
				stmt2.close();
				
				// BCC List selected from Company Correspondence Address
				queryString2="SELECT EMAIL "
						+ "FROM FMS_ENTITY_CONTACT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND ENTITY=? AND ADDR_FLAG IN (?,?) AND NOM_FLAG=? "
						+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND (TO_NOM=? OR TO_NOM IS NULL) "
						+ "AND TYPE=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
						+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.TYPE=B.TYPE) "
						+ "AND EMAIL IS NOT NULL";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, comp_cd);
				stmt2.setString(3, "B");
				stmt2.setString(4, "C");
				stmt2.setString(5, "P"+bu_seq);
				stmt2.setString(6, "Y");
				stmt2.setString(7, "Y");
				stmt2.setString(8, "Y");
				stmt2.setString(9, "B");
				stmt2.setString(10, "DLNG");
				rset2=stmt2.executeQuery();
				while(rset2.next())
				{
					String email=rset2.getString(1)==null?"":rset2.getString(1);
					if(!to_list.contains(email))
					{
						if(bcc_list.equals(""))
						{
							bcc_list=email;
						}
						else
						{
							bcc_list+=", "+email;
						}
					}
				}
				rset2.close();
				stmt2.close();
			}
			rset5.close();
			stmt5.close();
			
			if(contract_type.equals("W")) //ADDITIONAL EMAIL IDs FOR IGX ONLY
			{
				queryString="SELECT AGMT_REV,CONT_REV "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND AGMT_NO=? AND CONTRACT_TYPE=? "
						+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
				stmt_temp = conn.prepareStatement(queryString);
				stmt_temp.setString(1, comp_cd);
				stmt_temp.setString(2, counterparty_cd);
				stmt_temp.setString(3, cont_no);
				stmt_temp.setString(4, agmt_no);
				stmt_temp.setString(5, contract_type);
				rset_temp=stmt_temp.executeQuery();
				if(rset_temp.next())
				{
					String agmtRev=rset_temp.getString(1)==null?"":rset_temp.getString(1);
					String contRev=rset_temp.getString(2)==null?"":rset_temp.getString(2);
					
					queryString3="SELECT GX_COUNTERPARTY_CD,GX_BU_SEQ_NO "
							+ "FROM FMS_SUPPLY_CONT_GX_BU "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=?";
					stmt3=conn.prepareStatement(queryString3);
					stmt3.setString(1, comp_cd);
					stmt3.setString(2, counterparty_cd);
					stmt3.setString(3, cont_no);
					stmt3.setString(4, contRev);
					stmt3.setString(5, agmt_no);
					stmt3.setString(6, agmtRev);
					stmt3.setString(7, contract_type);
					rset3=stmt3.executeQuery();
					while(rset3.next())
					{
						String gx_cd=rset3.getString(1)==null?"":rset3.getString(1);
						String gx_bu_cd=rset3.getString(2)==null?"":rset3.getString(2);
						
						queryString4="SELECT EMAIL "
								+ "FROM FMS_ENTITY_CONTACT_MST A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND ENTITY=? AND ADDR_FLAG=? AND NOM_FLAG=? "
								+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND TO_NOM=? "
								+ "AND TYPE=? "
								+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
								+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.TYPE=B.TYPE) "
								+ "AND EMAIL IS NOT NULL";
						stmt4=conn.prepareStatement(queryString4);
						stmt4.setString(1, comp_cd);
						stmt4.setString(2, gx_cd);
						stmt4.setString(3, "G");
						stmt4.setString(4, "B"+gx_bu_cd);
						stmt4.setString(5, "Y");
						stmt4.setString(6, "Y");
						stmt4.setString(7, "Y");
						stmt4.setString(8, "Y");
						stmt4.setString(9, "DLNG");
						rset4=stmt4.executeQuery();
						if(rset4.next())
						{
							String email=rset4.getString(1)==null?"":rset4.getString(1);
							if(!to_list.contains(email))
							{
								if(to_list.equals(""))
								{
									to_list=email;
								}
								else
								{
									to_list+=", "+email;
								}
							}
						}
						rset4.close();
						stmt4.close();
						
						//get CC list
						queryString5="SELECT EMAIL "
								+ "FROM FMS_ENTITY_CONTACT_MST A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND ENTITY=? AND ADDR_FLAG=? AND NOM_FLAG=? "
								+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND (TO_NOM=? OR TO_NOM IS NULL) "
								+ "AND TYPE=? "
								+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
								+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.TYPE=B.TYPE) "
								+ "AND EMAIL IS NOT NULL";
						stmt5=conn.prepareStatement(queryString5);
						stmt5.setString(1, comp_cd);
						stmt5.setString(2, gx_cd);
						stmt5.setString(3, "G");
						stmt5.setString(4, "B"+gx_bu_cd);
						stmt5.setString(5, "Y");
						stmt5.setString(6, "Y");
						stmt5.setString(7, "Y");
						stmt5.setString(8, "N");
						stmt5.setString(9, "DLNG");
						rset5=stmt5.executeQuery();
						while(rset5.next())
						{
							String email=rset5.getString(1)==null?"":rset5.getString(1);
							if(!to_list.contains(email))
							{
								if(cc_list.equals(""))
								{
									cc_list=email;
								}
								else
								{
									cc_list+=", "+email;
								}
							}
						}
						rset5.close();
						stmt5.close();
					}
					rset3.close();
					stmt3.close();
				}
				rset_temp.close();
				stmt_temp.close();
			}
			
			String pdf_name="";
			queryString4="SELECT REMARK,ADDRESSED_TO,PDF_NM "
					+ "FROM FMS_DAILY_SELLER_NOM_REMARK A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND AGMT_NO=? AND CONTRACT_TYPE=? "
					+ "AND PLANT_SEQ=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY')";
			stmt4=conn.prepareStatement(queryString4);
			stmt4.setString(1, comp_cd);
			stmt4.setString(2, counterparty_cd);
			stmt4.setString(3, cont_no);
			stmt4.setString(4, agmt_no);
			stmt4.setString(5, contract_type);
			stmt4.setString(6, plant_seq);
			stmt4.setString(7, gas_dt);
			rset4=stmt4.executeQuery();
			if(rset4.next())
			{
				pdf_name=rset4.getString(3)==null?"":rset4.getString(3);
			}
			rset4.close();
			stmt4.close();
			
			String counterparty_abbr=utilBean.getCounterpartyABBR(conn,counterparty_cd);
			String plant_nm=utilBean.getCounterpartyPlantName(conn,counterparty_cd, comp_cd, plant_seq, "C");
			String company_abbr=utilBean.getCompanyAbbr(conn, comp_cd);
			String subject=company_abbr+"/";
			if (gas_dt.equals(sysdate))
			{
				subject+="INTRADAY/";
			}
			
			subject+="DLNG Seller Nomination To Customer("+counterparty_abbr+"-"+plant_nm+")-"+gas_dt;
			
			String directory_path=file_path+File.separator+pdf_name;
			
			File pdfFile = new File(directory_path);
			if(!pdfFile.exists())
			{
				pdf_name="";
			}
			
			String companyNm=utilBean.getCompanyName(conn,comp_cd);
			String mail_body="Dear Sir/Madam,"
					+ "\n\nPlease find enclosed "+counterparty_abbr+"-"+plant_nm+" Seller Nomination details for the gas date "+gas_dt+"."
					+ "\nIn case of any discrepancy, please revert to us by end of today or else it will be construed as deemed acceptance."
					+ "\n\nIn case of any query, please contact us at "+ownEmail+""				
					+ "\n\n\nThank You,"
					+ "\n\n"+companyNm+""
					+ "\n"+ownAddress+", "
					+ "\n"+ownCity+", "+ownState+" - "+ownPin+""
					+ "\nEmail: "+ownEmail+""
					+ "\nPh: "+ownPhone+""
					+ "\n\n***This is an auto-generated email from the system, please do not reply to this email.";
			
			VMAIL_FROM_LIST.add(utilBean.getFromMailId(conn));
			VMAIL_TO_LIST.add(to_list);
			VMAIL_CC_LIST.add(cc_list);
			VMAIL_BCC_LIST.add(bcc_list);
			VMAIL_SUBJECT.add(subject);
			VMAIL_ATTACHMENT.add(pdf_name);
			VMAIL_ATTACHMENT_PATH.add(CommonVariable.nom_to_customer_pdf_path);
			VMAIL_BODY.add(mail_body);
			
			if(file.equals("ALL_MAIL") && !pdf_name.equals(""))
			{
				sendAllMail(to_list,mail_body,subject,directory_path,cc_list,bcc_list,"DLNG_NOM_TO_CUST");
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getViewNominationToCustomerInfo()
	{
		String function_nm="getViewNominationToCustomerInfo()";
		try
		{
			String ph_no="";
			queryString="SELECT PH_NO "
					+ "FROM FMS_EMP_MST "
					+ "WHERE EMP_CD=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, emp_cd);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				ph_no=rset.getString(1)==null?"":rset.getString(1);
			}
			rset.close();
			stmt.close();
			
			String company_nm = ""+utilBean.getCompanyName(conn,comp_cd);
			String customer_nm = ""+utilBean.getCounterpartyName(conn,counterparty_cd);
			view_seller_nomination_to_customer_info.put("company_nm", company_nm);
			view_seller_nomination_to_customer_info.put("customer_nm", customer_nm);
			view_seller_nomination_to_customer_info.put("customer_abbr", ""+utilBean.getCounterpartyABBR(conn,counterparty_cd));
			view_seller_nomination_to_customer_info.put("plant_nm", ""+utilBean.getCounterpartyPlantName(conn,counterparty_cd, comp_cd, plant_seq, "C"));
			view_seller_nomination_to_customer_info.put("emp_nm", ""+utilBean.getEmpName(conn,emp_cd));
			view_seller_nomination_to_customer_info.put("emp_ph_no", ""+ph_no);
			
			String signing_dt="";
			String agmtSigningDt="";
			String contRef="";
			String cargoRef="";
			queryString1="SELECT TO_CHAR(SIGNING_DT,'DD-MON-YY'),CONT_REF_NO,TRADE_REF_NO "
					+ "FROM FMS_SUPPLY_CONT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND AGMT_NO=? AND CONTRACT_TYPE=? "
					+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
					+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_cd);
			stmt1.setString(3, cont_no);
			stmt1.setString(4, agmt_no);
			stmt1.setString(5, contract_type);
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				signing_dt=rset1.getString(1)==null?"":rset1.getString(1);
				contRef=rset1.getString(2)==null?"":rset1.getString(2);
				if(contract_type.equals("W"))
				{
					contRef=rset1.getString(3)==null?"":rset1.getString(3);
				}
			}
			rset1.close();
			stmt1.close();
			
			if(contract_type.equals("O") || contract_type.equals("Q"))
			{
				queryString1="SELECT TO_CHAR(SIGNING_DT,'DD-MON-YY'),CONT_REF_NO "
						+ "FROM FMS_LTCORA_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND AGMT_NO=? AND CONTRACT_TYPE=? AND BUY_SALE=? "
						+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE)";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, cont_no);
				stmt1.setString(4, agmt_no);
				stmt1.setString(5, contract_type);
				stmt1.setString(6, "C");
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					signing_dt=rset1.getString(1)==null?"":rset1.getString(1);
					contRef=rset1.getString(2)==null?"":rset1.getString(2);
				}
				rset1.close();
				stmt1.close();
				
				queryString2="SELECT CARGO_REF "
						+ "FROM FMS_LTCORA_CONT_CARGO_DTL A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND AGMT_NO=? AND CONTRACT_TYPE=? AND BUY_SALE=? AND CARGO_NO=? "
						+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.CARGO_NO=B.CARGO_NO)";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, counterparty_cd);
				stmt2.setString(3, cont_no);
				stmt2.setString(4, agmt_no);
				stmt2.setString(5, contract_type);
				stmt2.setString(6, "C");
				stmt2.setString(7, cargo_no);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					cargoRef=rset2.getString(1)==null?"":rset2.getString(1);
				}
				rset2.close();
				stmt2.close();
			}
			
			if(contract_type.equals("F"))
			{
				queryString2="SELECT TO_CHAR(SIGNING_DT,'DD-MON-YY') "
						+ "FROM FMS_AGMT_MST A "
						+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) ";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, "D");
				stmt2.setString(3, agmt_no);
				stmt2.setString(4, counterparty_cd);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					agmtSigningDt=rset2.getString(1)==null?"":rset2.getString(1);
				}
				rset2.close();
				stmt2.close();
			}
			
			String remark="";
			String seq_no="";
			queryString2="SELECT REMARK,NVL((SEQ_NO),'0') "
					+ "FROM FMS_DAILY_SELLER_NOM_REMARK A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND AGMT_NO=? AND CONTRACT_TYPE=? "
					+ "AND PLANT_SEQ=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY')";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, counterparty_cd);
			stmt2.setString(3, cont_no);
			stmt2.setString(4, agmt_no);
			stmt2.setString(5, contract_type);
			stmt2.setString(6, plant_seq);
			stmt2.setString(7, gas_dt);
			rset2=stmt2.executeQuery();
			if(rset2.next())
			{
				remark=rset2.getString(1)==null?"":rset2.getString(1);
				seq_no =""+(rset2.getInt(2)+1);
			}
			else
			{
				remark="";
				seq_no="1";
			}
			rset2.close();
			stmt2.close();
			
			String contact_person_nm="";
			String contact_person_phone="";
			String contact_person_fax="";
			queryString3="SELECT CONTACT_PERSON,PHONE,FAX_1 "
					+ "FROM FMS_ENTITY_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND ENTITY=? AND SEQ_NO=? AND ADDR_FLAG=? "
					+ "AND TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
					+ "AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY') AND A.TYPE=B.TYPE)";
			stmt3=conn.prepareStatement(queryString3);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, counterparty_cd);
			stmt3.setString(3, "C");
			stmt3.setString(4, contact_person_cd);
			stmt3.setString(5, "P"+plant_seq);
			stmt3.setString(6, "DLNG");
			stmt3.setString(7, gas_dt);
			rset3=stmt3.executeQuery();
			if(rset3.next())
			{
				contact_person_nm=rset3.getString(1)==null?"":rset3.getString(1);
				contact_person_phone=rset3.getString(2)==null?"":rset3.getString(2);
				contact_person_fax=rset3.getString(3)==null?"":rset3.getString(3);
			}
			rset3.close();
			stmt3.close();
			
			String plantAddress="";
			String plantCity="";
			String plantState="";
			String plantPin="";
			String plantNm="";
			
			queryString4 = "SELECT PLANT_ADDR,PLANT_CITY,PLANT_STATE,PLANT_PIN,PLANT_NAME,PLANT_CITY "
					+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
					+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND COMPANY_CD=? AND SEQ_NO=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) ";
			stmt4=conn.prepareStatement(queryString4);
			stmt4.setString(1, counterparty_cd);
			stmt4.setString(2, "C");
			stmt4.setString(3, comp_cd);
			stmt4.setString(4, plant_seq);
			rset4=stmt4.executeQuery();
			if(rset4.next())
			{
				plantAddress  = rset4.getString(1)==null?"":rset4.getString(1);
				plantCity  = rset4.getString(2)==null?"":rset4.getString(2);
				plantState  = rset4.getString(3)==null?"":rset4.getString(3);
				plantPin  = rset4.getString(4)==null?"":rset4.getString(4);
				plantNm  = rset4.getString(5)==null?"":rset4.getString(5);
			}
			rset4.close();
			stmt4.close();
			
			String ownAddress="";
			String ownCity="";
			String ownState="";
			String ownPin="";
			String ownCountry="";
			String ownPhone="";
			String ownEmail="";
			
			queryString5="SELECT ADDR,CITY,STATE,PIN,COUNTRY,PHONE,EMAIL "
					+ "FROM FMS_COMPANY_OWNER_ADDR_MST A "
					+ "WHERE COMPANY_CD=? AND ADDRESS_TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COMPANY_OWNER_ADDR_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.ADDRESS_TYPE=B.ADDRESS_TYPE "
					+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
			stmt5=conn.prepareStatement(queryString5);
			stmt5.setString(1, comp_cd);
			stmt5.setString(2, "C");
			rset5=stmt5.executeQuery();
			if(rset5.next())
			{
				ownAddress  = rset5.getString(1)==null?"":rset5.getString(1);
				ownCity  = rset5.getString(2)==null?"":rset5.getString(2);
				ownState  = rset5.getString(3)==null?"":rset5.getString(3);
				ownPin  = rset5.getString(4)==null?"":rset5.getString(4);
				ownCountry  = rset5.getString(5)==null?"":rset5.getString(5);
				ownPhone  = rset5.getString(6)==null?"":rset5.getString(6);
				ownEmail  = rset5.getString(7)==null?"":rset5.getString(7);
			}
			rset5.close();
			stmt5.close();
			
			view_seller_nomination_to_customer_info.put("plantAddress",plantAddress);
			view_seller_nomination_to_customer_info.put("plantCity",plantCity);
			view_seller_nomination_to_customer_info.put("plantState",plantState);
			view_seller_nomination_to_customer_info.put("plantPin",plantPin);
			view_seller_nomination_to_customer_info.put("plantNm",plantNm);
			
			view_seller_nomination_to_customer_info.put("ownAddress",ownAddress);
			view_seller_nomination_to_customer_info.put("ownCity",ownCity);
			view_seller_nomination_to_customer_info.put("ownState",ownState);
			view_seller_nomination_to_customer_info.put("ownPin",ownPin);
			view_seller_nomination_to_customer_info.put("ownCountry",ownCountry);
			view_seller_nomination_to_customer_info.put("ownPhone",ownPhone);
			view_seller_nomination_to_customer_info.put("ownEmail",ownEmail);
			
			view_seller_nomination_to_customer_info.put("contact_person_nm",contact_person_nm);
			view_seller_nomination_to_customer_info.put("contact_person_phone",contact_person_phone);
			view_seller_nomination_to_customer_info.put("contact_person_fax",contact_person_fax);
			view_seller_nomination_to_customer_info.put("signing_dt",signing_dt);
			view_seller_nomination_to_customer_info.put("agmt_signing_dt",agmtSigningDt);
			view_seller_nomination_to_customer_info.put("contRef",contRef);
			view_seller_nomination_to_customer_info.put("remark",remark);
			view_seller_nomination_to_customer_info.put("seq_no",seq_no);
			
			String contract_dtl_Str = "";
			if (contract_type.equals("E"))
			{
				contract_dtl_Str = "DLNG Letter of Agreement ("+contRef+") executed on "+signing_dt+")";
			}
			else if (contract_type.equals("W"))
			{
				contract_dtl_Str = "DLNG Exchange Transaction ("+contRef+") executed on "+signing_dt+")";
			}	
			else if (contract_type.equals("F"))
			{	
				contract_dtl_Str = "DLNG Supply Notice ("+contRef+") executed on "+signing_dt+" "
						+"pursuant to Framework LNG Sales Agreement executed on "+agmtSigningDt+"";
			}
			else if(contract_type.equals("O"))
			{
				//contract_dtl_Str = "LTCORA(Sell) CN ("+contRef+") executed on "+signing_dt+" for Cargo "+cargoRef+ " ";
				contract_dtl_Str = "LTCORA executed on "+signing_dt+" for Cargo "+cargoRef+ " "; // 11-02-2026: VIJAY Feedback
			}
			else if(contract_type.equals("Q"))
			{
				//contract_dtl_Str = "LTCORA(Sell) Period ("+contRef+") executed on "+signing_dt+" for Cargo "+cargoRef+ " ";
				contract_dtl_Str = "LTCORA executed on "+signing_dt+" for Cargo "+cargoRef+ " "; // 11-02-2026: VIJAY Feedback
			}
			
			String emailBody = "As per requirement clause of "+contract_dtl_Str+" "
					+ "between "+company_nm+" and "+customer_nm+", "
					+ "the Seller Nomination notification for gas day "+gas_dt+" as follows:";
			 
			view_seller_nomination_to_customer_info.put("emailBody",emailBody);		 
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getViewNominationToCustomerData()
	{
		String function_nm="getViewNominationToCustomerData()";
		try
		{
			HashMap map_gas_dt = new HashMap();
			HashMap map_mmbtu = new HashMap();
			HashMap map_mt = new HashMap();
			HashMap map_grid = new HashMap();
			HashMap map_fill_station = new HashMap();
			HashMap map_checkpost = new HashMap();
			HashMap total_mmbtu = new HashMap();
			HashMap total_mt = new HashMap();
			HashMap map_plant_abbr = new HashMap();
			HashMap map_nom_dt = new HashMap();
			HashMap map_nom_time = new HashMap();
			HashMap map_remark = new HashMap();
			double tot_mmbtu=0;
			double tot_mt=0;
			int count=0;
			queryString="SELECT SUM(QTY_MMBTU),SUM(QTY_MT),TRUCK_TRANS_CD,TRUCK_CD,FILL_STATION_CD,PLANT_SEQ,"
					+ "ARRIVAL_TIME,REMARK,TO_CHAR(ARRIVAL_DT,'DD/MM/YYYY') "
	  				+ "FROM FMS_DLNG_SELLER_NOM_DTL A "
	  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
					+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
					+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_SELLER_NOM_DTL B "
					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.BU_SEQ=A.BU_SEQ "
					+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
					+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) "
					+ "GROUP BY TRUCK_TRANS_CD,TRUCK_CD,FILL_STATION_CD,PLANT_SEQ,ARRIVAL_TIME,REMARK,ARRIVAL_DT "
					+ "ORDER BY TRUCK_TRANS_CD,TRUCK_CD,FILL_STATION_CD,PLANT_SEQ,ARRIVAL_TIME,REMARK,ARRIVAL_DT ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, cont_no);
			stmt.setString(2, agmt_no);
			stmt.setString(3, comp_cd);
			stmt.setString(4, counterparty_cd);
			stmt.setString(5, plant_seq);
			stmt.setString(6, contract_type);
			stmt.setString(7, gas_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				double mmbtu=rset.getDouble(1);
				double mt=rset.getDouble(2);
				String transportCd=rset.getString(3)==null?"":rset.getString(3);
				String transport_truck=rset.getString(4)==null?"":rset.getString(4);
				String fill_station_cd=rset.getString(5)==null?"":rset.getString(5);
				String plant_seq=rset.getString(6)==null?"":rset.getString(6);
				String nom_time=rset.getString(7)==null?"":rset.getString(7);
				String remark=rset.getString(8)==null?"":rset.getString(8);
				String nom_dt=rset.getString(9)==null?"":rset.getString(9);
				String plant_abbr = utilBean.getCounterpartyPlantABBR(conn, counterparty_cd, comp_cd, plant_seq, "C");
				String trans_abbr=""+utilBean_DLNG.getTruckTransABBR(conn,transportCd);
				String trans_truck_nm=""+utilBean_DLNG.getTruckRegNo(conn, transport_truck);
				String fill_station_nm=""+utilBean_DLNG.getFillStationName(conn, fill_station_cd);
				tot_mmbtu=tot_mmbtu+mmbtu;
				tot_mt=tot_mt+mt;
				
				HashMap<String, String> plant_detail = utilBean.getCounterpartyPlantDetail(conn,comp_cd, "C", counterparty_cd, plant_seq);
				String plantCity = ""+plant_detail.get("plant_city");
				
				String checkpost_cd="";
				String checkpost_nm="";
				String queryString2="SELECT CHKPOST_CD "
						+ "FROM FMS_LINK_CHECKPOST_PLANT A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND PLANT_SEQ_NO=? AND ENTITY_TYPE='C' "
						+ "AND (RELEASE_DT>=TO_DATE(?,'DD/MM/YYYY') OR RELEASE_DT IS NULL) "
						+ "AND EFF_DT<=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_LINK_CHECKPOST_PLANT B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY_TYPE=B.ENTITY_TYPE "
						+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO ) " //AND A.CHKPOST_CD=B.CHKPOST_CD
						+ "AND A.REV_SEQ=(SELECT MAX(B.REV_SEQ) FROM FMS_LINK_CHECKPOST_PLANT B WHERE "
						+ "	A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY_TYPE=B.ENTITY_TYPE "
						+ "	AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.EFF_DT=B.EFF_DT)";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, counterparty_cd);
				stmt2.setString(3, plant_seq);
				stmt2.setString(4, gas_dt);
				stmt2.setString(5, gas_dt);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					checkpost_cd=rset2.getString(1)==null?"":rset2.getString(1);
					checkpost_nm=utilBean_DLNG.getCheckPostName(conn, checkpost_cd);
				}
				rset2.close();
				stmt2.close();
				
				count+=1;
				map_gas_dt.put(""+count, gas_dt);
				map_mmbtu.put(""+count, nf.format(mmbtu));
				map_mt.put(""+count, nf.format(mt));
				map_grid.put(""+count,trans_truck_nm);
				map_fill_station.put(""+count,fill_station_nm);
				map_checkpost.put(""+count,checkpost_nm);
				map_plant_abbr.put(""+count,plant_abbr+" ("+plantCity+")");
				map_nom_dt.put(""+count,nom_dt);
				map_nom_time.put(""+count,nom_time);
				map_remark.put(""+count,remark);
			}
			rset.close();
			stmt.close();
			total_mmbtu.put(""+count, nf.format(tot_mmbtu));
			total_mt.put(""+count, nf.format(tot_mt));
			
			view_seller_nomination_to_customer_data.put("v_gas_dt", map_gas_dt);
			view_seller_nomination_to_customer_data.put("v_mmbtu", map_mmbtu);
			view_seller_nomination_to_customer_data.put("v_mt", map_mt);
			view_seller_nomination_to_customer_data.put("v_grid", map_grid);
			view_seller_nomination_to_customer_data.put("v_total", total_mmbtu);
			view_seller_nomination_to_customer_data.put("v_total_mt", total_mt);
			view_seller_nomination_to_customer_data.put("v_fill_station", map_fill_station);
			view_seller_nomination_to_customer_data.put("v_checkpost", map_checkpost);
			view_seller_nomination_to_customer_data.put("v_plant_abbr", map_plant_abbr);
			view_seller_nomination_to_customer_data.put("v_nom_dt", map_nom_dt);
			view_seller_nomination_to_customer_data.put("v_nom_time", map_nom_time);
			view_seller_nomination_to_customer_data.put("v_remark", map_remark);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getDailySellerNominationToCustomer()
	{
		String function_nm="getDailySellerNominationToCustomer()";
		try
		{
			Vector TEMP_CONT_TYPE=new Vector();
			Vector TEMP_CONT_TYPE_NM=new Vector();
			
			if(!contract_type.equals("") && !contract_type.equals("0"))
			{
				if(VMST_CONTRACT_TYPE.contains(contract_type))
				{
					TEMP_CONT_TYPE.add(VMST_CONTRACT_TYPE.get(VMST_CONTRACT_TYPE.indexOf(contract_type)));
					TEMP_CONT_TYPE_NM.add(VMST_CONTRACT_TYPE_NM.get(VMST_CONTRACT_TYPE.indexOf(contract_type)));
				}
			}
			else
			{
				TEMP_CONT_TYPE=VMST_CONTRACT_TYPE;
				TEMP_CONT_TYPE_NM=VMST_CONTRACT_TYPE_NM;
			}
			
			for(int i=0;i<TEMP_CONT_TYPE.size();i++)
			{
				String contType=""+TEMP_CONT_TYPE.elementAt(i);
				String contType_nm=""+TEMP_CONT_TYPE_NM.elementAt(i);
				
				int index=0;
				
				queryString="SELECT DISTINCT COUNTERPARTY_CD,AGMT_NO,CONT_NO,PLANT_SEQ,CARGO_NO "
		  				+ "FROM FMS_DLNG_SELLER_NOM_DTL "
		  				+ "WHERE COMPANY_CD=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
		  				+ "AND CONTRACT_TYPE=?";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, gas_dt);
				stmt.setString(3, contType);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					index+=1;
					
					String counterpty_cd=rset.getString(1)==null?"":rset.getString(1);
					String agmtno=rset.getString(2)==null?"":rset.getString(2);
					String contno=rset.getString(3)==null?"":rset.getString(3);
					String plantSeq=rset.getString(4)==null?"":rset.getString(4);
					//String bu_seq=rset.getString(5)==null?"":rset.getString(5);
					String counterptyAbbr = utilBean.getCounterpartyABBR(conn,counterpty_cd);
					String plantAbbr=utilBean.getCounterpartyPlantABBR(conn,counterpty_cd, comp_cd, plantSeq, "C");
					String plantNm=utilBean.getCounterpartyPlantName(conn,counterpty_cd, comp_cd, plantSeq, "C");
					String cargo_no=rset.getString(5)==null?"":rset.getString(5);;
					
					VCOUNTERPARTY_CD.add(counterpty_cd);
					VCOUNTERPARTY_ABBR.add(counterptyAbbr);
					VCOUNTERPARTY_NM.add(""+utilBean.getCounterpartyName(conn,counterpty_cd));
					VCONTRACT_TYPE.add(contType);
					VCONTRACT_TYPE_NM.add(contType_nm);
					VCOUNTERPARTY_PLANT_SEQ.add(plantSeq);
					VCOUNTERPARTY_PLANT_ABBR.add(plantAbbr);
					VBU_PLANT_SEQ.add("");
					
					String contRef="";
					queryString1="SELECT CONT_REF_NO,TRADE_REF_NO "
							+ "FROM FMS_SUPPLY_CONT_MST A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND AGMT_NO=? AND CONTRACT_TYPE=? "
							+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
							+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterpty_cd);
					stmt1.setString(3, contno);
					stmt1.setString(4, agmtno);
					stmt1.setString(5, contType);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						contRef=rset1.getString(1)==null?"":rset1.getString(1);
						String tradeRef=rset1.getString(2)==null?"":rset1.getString(2);
						
						if(contType.equals("W"))
						{
							contRef=tradeRef;
						}
					}
					rset1.close();
					stmt1.close();
					
					if(contType.equals("O") || contType.equals("Q"))
					{
						queryString1="SELECT CARGO_REF "
								+ "FROM FMS_LTCORA_CONT_CARGO_DTL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND CONT_NO=? AND AGMT_NO=? AND CONTRACT_TYPE=? AND BUY_SALE=?  AND CARGO_NO=? "
								+ "AND CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
								+ "AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.CARGO_NO=B.CARGO_NO)";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, counterpty_cd);
						stmt1.setString(3, contno);
						stmt1.setString(4, agmtno);
						stmt1.setString(5, contType);
						stmt1.setString(6, "C");
						stmt1.setString(7, cargo_no);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							contRef=rset1.getString(1)==null?"":rset1.getString(1);
						}
						rset1.close();
						stmt1.close();
					}
					
					String dealNo=utilBean.NewDealMappingId(comp_cd, counterpty_cd, agmtno, "", contno, "", contType, cargo_no);
					VDIS_CONT_MAPPING.add(dealNo);
					VAGMT_NO.add(agmtno);
					VCONT_NO.add(contno);
					VCARGO_NO.add(cargo_no);
					VCONT_REF.add(contRef);
					
					Vector VTEMP_CONTACT_PERSON=new Vector();
					Vector VTEMP_CONTACT_PERSON_CD=new Vector();
					
					VTEMP_CONTACT_PERSON.add("--Select--");
					VTEMP_CONTACT_PERSON_CD.add("");
					queryString2="SELECT CONTACT_PERSON, SEQ_NO "
							+ "FROM FMS_ENTITY_CONTACT_MST A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND ENTITY=? AND ADDR_FLAG=? AND NOM_FLAG=? "
							+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? "
							+ "AND TYPE=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
							+ "AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY') AND A.TYPE=B.TYPE)";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, counterpty_cd);
					stmt2.setString(3, "C");
					stmt2.setString(4, "P"+plantSeq);
					stmt2.setString(5, "Y");
					stmt2.setString(6, "Y");
					stmt2.setString(7, "Y");
					stmt2.setString(8, "DLNG");
					stmt2.setString(9, gas_dt);
					rset2=stmt2.executeQuery();
					while(rset2.next())
					{
						VTEMP_CONTACT_PERSON.add(rset2.getString(1)==null?"":rset2.getString(1));
						VTEMP_CONTACT_PERSON_CD.add(rset2.getString(2)==null?"":rset2.getString(2));
					}
					rset2.close();
					stmt2.close();
					
					String contact_person_cd="";
					queryString3="SELECT SEQ_NO "
							+ "FROM FMS_ENTITY_CONTACT_MST A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND ENTITY=? AND ADDR_FLAG=? AND NOM_FLAG=? "
							+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND TO_NOM=? "
							+ "AND TYPE=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
							+ "AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY') AND A.TYPE=B.TYPE)";
					stmt3=conn.prepareStatement(queryString3);
					stmt3.setString(1, comp_cd);
					stmt3.setString(2, counterpty_cd);
					stmt3.setString(3, "C");
					stmt3.setString(4, "P"+plantSeq);
					stmt3.setString(5, "Y");
					stmt3.setString(6, "Y");
					stmt3.setString(7, "Y");
					stmt3.setString(8, "Y");
					stmt3.setString(9, "DLNG");
					stmt3.setString(10, gas_dt);
					rset3=stmt3.executeQuery();
					if(rset3.next())
					{
						contact_person_cd=rset3.getString(1)==null?"0":rset3.getString(1);
					}
					rset3.close();
					stmt3.close();
					
					VCONTACT_PERSON.add(VTEMP_CONTACT_PERSON);
					VCONTACT_PERSON_CD.add(VTEMP_CONTACT_PERSON_CD);
					VSEL_CONTACT_PERSON_CD.add(contact_person_cd);
					
					String remark="";
					String contact_person="";
					String pdf_name="";
					String pdf_path="";
					String email_flag="";
					queryString4="SELECT REMARK,ADDRESSED_TO,PDF_NM,EMAIL_SENT "
							+ "FROM FMS_DAILY_SELLER_NOM_REMARK A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND AGMT_NO=? AND CONTRACT_TYPE=? "
							+ "AND PLANT_SEQ=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=?";
					stmt4=conn.prepareStatement(queryString4);
					stmt4.setString(1, comp_cd);
					stmt4.setString(2, counterpty_cd);
					stmt4.setString(3, contno);
					stmt4.setString(4, agmtno);
					stmt4.setString(5, contType);
					stmt4.setString(6, plantSeq);
					stmt4.setString(7, gas_dt);
					stmt4.setString(8, cargo_no);
					rset4=stmt4.executeQuery();
					if(rset4.next())
					{
						remark=rset4.getString(1)==null?"":rset4.getString(1);
						contact_person=rset4.getString(2)==null?"":rset4.getString(2);
						pdf_name=rset4.getString(3)==null?"":rset4.getString(3);
						email_flag=rset4.getString(4)==null?"":rset4.getString(4);
						pdf_path=CommonVariable.nom_to_customer_pdf_path;
					}
					rset4.close();
					stmt4.close();
					
					VREMARK.add(remark);
					VADDRESSED_PERSON.add(contact_person);
					VPDF_NAME.add(pdf_name);
					VPDF_PATH.add(pdf_path);
					VEMAIL_FLAG.add(email_flag);
					
					String pdf_exists="N";
					
					String directory_path=file_path+File.separator+pdf_name;
					
					File pdfFile = new File(directory_path);
					if(pdfFile.exists() && !pdf_name.equals(""))
					{
						pdf_exists="Y";
					}
					VPDF_EXISTS.add(pdf_exists);
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
	
	public void getContractTypeMasterForNomToCust()
	{
		String function_nm="getContractTypeMasterForNomToCust()";
		try
		{
			queryString="SELECT DISTINCT CONTRACT_TYPE "
	  				+ "FROM FMS_DLNG_SELLER_NOM_DTL "
	  				+ "WHERE COMPANY_CD=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
	  				+ "ORDER BY CONTRACT_TYPE ASC";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, gas_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String contType=rset.getString(1)==null?"":rset.getString(1);
				String contNm=utilBean.getContractTypeName(contType);
				
				VMST_CONTRACT_TYPE.add(contType);
				VMST_CONTRACT_TYPE_NM.add(contNm);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSendMailSellerToTransporterDetail()
	{
		String function_nm="getSendMailSellerToTransporterDetail()";
		try
		{
			String ownAddress="";
			String ownCity="";
			String ownState="";
			String ownPin="";
			String ownCountry="";
			String ownPhone="";
			String ownEmail="";
			String sysdate=dateUtil.getSysdate();
			
			queryString="SELECT ADDR,CITY,STATE,PIN,COUNTRY,PHONE,EMAIL "
					+ "FROM FMS_COMPANY_OWNER_ADDR_MST A "
					+ "WHERE COMPANY_CD=? AND ADDRESS_TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COMPANY_OWNER_ADDR_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.ADDRESS_TYPE=B.ADDRESS_TYPE "
					+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "C");
			rset=stmt.executeQuery();
			if(rset.next())
			{
				ownAddress  = rset.getString(1)==null?"":rset.getString(1);
				ownCity  = rset.getString(2)==null?"":rset.getString(2);
				ownState  = rset.getString(3)==null?"":rset.getString(3);
				ownPin  = rset.getString(4)==null?"":rset.getString(4);
				ownCountry  = rset.getString(5)==null?"":rset.getString(5);
				ownPhone  = rset.getString(6)==null?"":rset.getString(6);
				ownEmail  = rset.getString(7)==null?"":rset.getString(7);
			}
			rset.close();
			stmt.close();
			
			//get TO list
			String to_list="";
			queryString1="SELECT EMAIL "
					+ "FROM FMS_TRUCK_TRANS_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND TRUCK_TRANS_CD=? AND SEQ_NO=? "
					+ "AND ADDR_FLAG=? AND NOM_FLAG=? "
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_TRUCK_TRANS_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.TRUCK_TRANS_CD=B.TRUCK_TRANS_CD AND A.SEQ_NO=B.SEQ_NO "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) "
					+ "AND EMAIL IS NOT NULL";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_cd);
			stmt1.setString(3, contact_person_cd);
			stmt1.setString(4, "R");
			stmt1.setString(5, "Y");
			stmt1.setString(6, "Y");
			stmt1.setString(7, "Y");
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				String email=rset1.getString(1)==null?"":rset1.getString(1);
				if(to_list.equals(""))
				{
					to_list=email;
				}
				else
				{
					to_list+=", "+email;
				}
			}
			rset1.close();
			stmt1.close();
			
			//get CC list
			String cc_list="";
			String bcc_list="";
			queryString2="SELECT EMAIL "
					+ "FROM FMS_TRUCK_TRANS_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND TRUCK_TRANS_CD=? AND SEQ_NO!=? "
					+ "AND ADDR_FLAG=? AND NOM_FLAG=? "
					+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_TRUCK_TRANS_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.TRUCK_TRANS_CD=B.TRUCK_TRANS_CD AND A.SEQ_NO=B.SEQ_NO "
					+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) "
					+ "AND EMAIL IS NOT NULL";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, counterparty_cd);
			stmt2.setString(3, contact_person_cd);
			stmt2.setString(4, "R");
			stmt2.setString(5, "Y");
			stmt2.setString(6, "Y");
			stmt2.setString(7, "Y");
			rset2=stmt2.executeQuery();
			while(rset2.next())
			{
				String email=rset2.getString(1)==null?"":rset2.getString(1);
				if(cc_list.equals(""))
				{
					cc_list=email;
				}
				else
				{
					cc_list+=", "+email;
				}
			}
			rset2.close();
			stmt2.close();
			
			queryString5="SELECT DISTINCT BU_SEQ "
	  				+ "FROM FMS_DLNG_SELLER_NOM_DTL A "
	  				+ "WHERE COMPANY_CD=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
	  				+ "AND TRUCK_TRANS_CD=? "
					+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_SELLER_NOM_DTL B "
					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD "
					+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
					+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
			stmt5=conn.prepareStatement(queryString5);
			stmt5.setString(1, comp_cd);
			stmt5.setString(2, gas_dt);
			stmt5.setString(3, counterparty_cd);
			rset5=stmt5.executeQuery();
			while(rset5.next())
			{
				String bu_seq=rset5.getString(1)==null?"":rset5.getString(1);
			// CC List selected from Company Correspondence Address
				queryString3="SELECT EMAIL "
						+ "FROM FMS_ENTITY_CONTACT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND ENTITY=? AND ADDR_FLAG IN (?,?) AND NOM_FLAG=? "
						+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND (TO_NOM=? OR TO_NOM IS NULL) "
						+ "AND TYPE=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
						+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.TYPE=B.TYPE) "
						+ "AND EMAIL IS NOT NULL";
				stmt3=conn.prepareStatement(queryString3);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, comp_cd);
				stmt3.setString(3, "B");
				stmt3.setString(4, "C");
				stmt3.setString(5, "P"+bu_seq);
				stmt3.setString(6, "Y");
				stmt3.setString(7, "Y");
				stmt3.setString(8, "Y");
				stmt3.setString(9, "N");
				stmt3.setString(10, "DLNG");
				rset3=stmt3.executeQuery();
				while(rset3.next())
				{
					String email=rset3.getString(1)==null?"":rset3.getString(1);
					if(cc_list.equals(""))
					{
						cc_list=email;
					}
					else
					{
						cc_list+=", "+email;
					}
				}
				rset3.close();
				stmt3.close();
				
				// BCC List selected from Company Correspondence Address
				queryString2="SELECT EMAIL "
						+ "FROM FMS_ENTITY_CONTACT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND ENTITY=? AND ADDR_FLAG IN (?,?) AND NOM_FLAG=? "
						+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? AND (TO_NOM=? OR TO_NOM IS NULL) "
						+ "AND TYPE=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
						+ "AND EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.TYPE=B.TYPE) "
						+ "AND EMAIL IS NOT NULL";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, comp_cd);
				stmt2.setString(3, "B");
				stmt2.setString(4, "C");
				stmt2.setString(5, "P"+bu_seq);
				stmt2.setString(6, "Y");
				stmt2.setString(7, "Y");
				stmt2.setString(8, "Y");
				stmt2.setString(9, "B");
				stmt2.setString(10, "DLNG");
				rset2=stmt2.executeQuery();
				while(rset2.next())
				{
					String email=rset2.getString(1)==null?"":rset2.getString(1);
					if(!to_list.contains(email))
					{
						if(bcc_list.equals(""))
						{
							bcc_list=email;
						}
						else
						{
							bcc_list+=", "+email;
						}
					}
				}
				rset2.close();
				stmt2.close();
			}
			rset5.close();
			stmt5.close();
			
			String pdf_name="";
			queryString4="SELECT REMARK,ADDRESSED_TO,PDF_NM "
					+ "FROM FMS_DLNG_SELLER_NOM_TRANS A "
					+ "WHERE COMPANY_CD=? "
					+ "AND TRUCK_TRANS_CD=? "
					//+ "AND TRUCK_CD=? "
					+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY')";
			stmt4=conn.prepareStatement(queryString4);
			stmt4.setString(1, comp_cd);
			stmt4.setString(2, counterparty_cd);
			//stmt4.setString(3, truck_cd);
			stmt4.setString(3, gas_dt);
			rset4=stmt4.executeQuery();
			if(rset4.next())
			{
				pdf_name=rset4.getString(3)==null?"":rset4.getString(3);
			}
			rset4.close();
			stmt4.close();
			
			String transporter_abbr=utilBean_DLNG.getTruckTransABBR(conn,counterparty_cd);
			String truck_nm=utilBean_DLNG.getTruckRegNo(conn, truck_cd);
			String company_abbr=utilBean.getCompanyAbbr(conn, comp_cd);
			String subject=company_abbr+"/";
			if (gas_dt.equals(sysdate))
			{
				subject+="INTRADAY/";
			}
			
			subject+="DLNG Seller Nomination To Transporter("+transporter_abbr+"-"+truck_nm+")-"+gas_dt;
			
			String directory_path=file_path+File.separator+pdf_name;
			
			File pdfFile = new File(directory_path);
			if(!pdfFile.exists())
			{
				pdf_name="";
			}
			
			String companyNm=utilBean.getCompanyName(conn,comp_cd);
			String mail_body="Dear Sir/Madam,"
					+ "\n\nPlease find enclosed "+transporter_abbr+"-"+truck_nm+" Seller Nomination details for the gas date "+gas_dt+"."
					+ "\nIn case of any discrepancy, please revert to us by end of today or else it will be construed as deemed acceptance."
					+ "\n\nIn case of any query, please contact us at "+ownEmail+""				
					+ "\n\n\nThank You,"
					+ "\n\n"+companyNm+""
					+ "\n"+ownAddress+", "
					+ "\n"+ownCity+", "+ownState+" - "+ownPin+""
					+ "\nEmail: "+ownEmail+""
					+ "\nPh: "+ownPhone+""
					+ "\n\n***This is an auto-generated email from the system, please do not reply to this email.";
			
			VMAIL_FROM_LIST.add(utilBean.getFromMailId(conn));
			VMAIL_TO_LIST.add(to_list);
			VMAIL_CC_LIST.add(cc_list);
			VMAIL_BCC_LIST.add(bcc_list);
			VMAIL_SUBJECT.add(subject);
			VMAIL_ATTACHMENT.add(pdf_name);
			VMAIL_ATTACHMENT_PATH.add(CommonVariable.nom_to_transporter_pdf_path);
			VMAIL_BODY.add(mail_body);
			
			if(file.equals("ALL_MAIL") && !pdf_name.equals(""))
			{
				sendAllMail(to_list,mail_body,subject,directory_path,cc_list,bcc_list,"DLNG_NOM_TO_TRANS");
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void sendAllMail(String email_to,String email_body,String subject,String attachment,String email_cc,String email_bcc,String rpt_typ)
	{
		String function_nm="sendAllMail()";
		try
		{
			if(email_to != null)
			{
				//for(int i=0;i<email_to.length;i++)
				{
					email_body=email_body.replaceAll("\n", "<br>");
					email_body="<html>"
							+ "<span style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>"+email_body+"</span>"
							+ "</html>";
					
					if(!email_to.equals("") && !email_body.equals(""))
					{
						mailDelv.sendMail(comp_cd,email_to, subject, email_body, attachment, email_cc, email_bcc);
						
						if(rpt_typ.equals("DLNG_NOM_TO_CUST"))
						{
							queryString3="UPDATE FMS_DAILY_SELLER_NOM_REMARK SET MODIFIY_BY=?,MODIFIY_DT=SYSDATE,EMAIL_SENT='Y' "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND AGMT_NO=? AND CONT_NO=? "
									+ "AND CONTRACT_TYPE=? AND PLANT_SEQ=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY')";
							stmt1 = conn.prepareStatement(queryString3);
							stmt1.setString(1, emp_cd);
							stmt1.setString(2, comp_cd);
							stmt1.setString(3, counterparty_cd);
							stmt1.setString(4, agmt_no);
							stmt1.setString(5, cont_no);
							stmt1.setString(6, contract_type);
							stmt1.setString(7, plant_seq);
							stmt1.setString(8, gas_dt);
							stmt1.executeUpdate();
							
							stmt1.close();
						}
						else if(rpt_typ.equals("DLNG_NOM_TO_TRANS"))
						{
							queryString3="UPDATE FMS_DLNG_SELLER_NOM_TRANS SET MODIFIY_BY=?,MODIFIY_DT=SYSDATE,EMAIL_SENT='Y' "
									+ "WHERE COMPANY_CD=? AND TRUCK_TRANS_CD=? "
									//+ "AND TRUCK_CD=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY')";
							stmt1 = conn.prepareStatement(queryString3);
							stmt1.setString(1, emp_cd);
							stmt1.setString(2, comp_cd);
							stmt1.setString(3, counterparty_cd);
							//stmt1.setString(4, truck_cd);
							stmt1.setString(4, gas_dt);
							stmt1.executeUpdate();
							
							stmt1.close();
						}
						else if(rpt_typ.equals("DLNG_JOINT_TICKET"))
						{
							queryString3="UPDATE FMS_DAILY_JOINT_TICKET SET MODIFIY_BY=?,MODIFIY_DT=SYSDATE,EMAIL_SENT='Y' "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND CONTRACT_TYPE=? AND PLANT_SEQ=? "
									+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND BU_SEQ=? AND ENTITY_TYPE='C'";
							stmt1 = conn.prepareStatement(queryString3);
							stmt1.setString(1, emp_cd);
							stmt1.setString(2, comp_cd);
							stmt1.setString(3, counterparty_cd);
							stmt1.setString(4, contract_type);
							stmt1.setString(5, plant_seq);
							stmt1.setString(6, gas_dt);
							stmt1.setString(7, bu_plant_seq);
							stmt1.executeUpdate();
							
							stmt1.close();
						}
						all_mail_sent="Y";
						
						String msg = "Email Sent for "+subject;
						String emp_nm=utilBean.getEmpName(conn, emp_cd);
						
						try
						{
							new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_name,mod_id,mod_name, "", "", msg);  	
						}
						catch(Exception infoLogger)
						{
							new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
						}
					}
				}
			}
			conn.commit();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getViewNominationToTransporterInfo()
	{
		String function_nm="getViewNominationToTransporterInfo()";
		try
		{
			String ph_no="";
			queryString="SELECT PH_NO "
					+ "FROM FMS_EMP_MST "
					+ "WHERE EMP_CD=? ";
			stmt0=conn.prepareStatement(queryString);
			stmt0.setString(1, emp_cd);
			rset0=stmt0.executeQuery();
			if(rset0.next())
			{
				ph_no=rset0.getString(1)==null?"":rset0.getString(1);
			}
			rset0.close();
			stmt0.close();
			
			String company_nm = ""+utilBean.getCompanyName(conn,comp_cd);
			String transporter_nm = ""+utilBean_DLNG.getTruckTransName(conn,counterparty_cd);
			view_info.put("company_nm", company_nm);
			view_info.put("transporter_nm", transporter_nm);
			view_info.put("transporter_abbr", ""+utilBean_DLNG.getTruckTransABBR(conn,counterparty_cd));
			view_info.put("truck_nm", ""+utilBean_DLNG.getTruckRegNo(conn, truck_cd));
			view_info.put("emp_nm", ""+utilBean.getEmpName(conn,emp_cd));
			view_info.put("emp_ph_no", ""+ph_no);
			
			String seq_no="";
			String remark="";
			queryString2="SELECT NVL((SEQ_NO),'0') "
					+ "FROM FMS_DLNG_SELLER_NOM_TRANS A "
					+ "WHERE COMPANY_CD=? AND TRUCK_TRANS_CD=? "
					//+ "AND TRUCK_CD=? "
					+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY')";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, comp_cd);
			stmt2.setString(2, counterparty_cd);
			//stmt2.setString(3, truck_cd);
			stmt2.setString(3, gas_dt);
			rset2=stmt2.executeQuery();
			if(rset2.next())
			{
				seq_no =""+(rset2.getInt(1)+1);
			}
			else
			{
				seq_no="1";
			}
			rset2.close();
			stmt2.close();
			
			String contact_person_nm="";
			String contact_person_phone="";
			String contact_person_fax="";
			queryString1="SELECT CONTACT_PERSON,PHONE,FAX_1 "
					+ "FROM FMS_TRUCK_TRANS_CONTACT_MST A "
					+ "WHERE COMPANY_CD=? AND TRUCK_TRANS_CD=? "
					+ "AND SEQ_NO=? AND ADDR_FLAG=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_TRUCK_TRANS_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.TRUCK_TRANS_CD=B.TRUCK_TRANS_CD AND A.SEQ_NO=B.SEQ_NO "
					+ "AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY'))";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_cd);
			stmt1.setString(3, contact_person_cd);
			stmt1.setString(4, "R");
			stmt1.setString(5, gas_dt);
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				contact_person_nm=rset1.getString(1)==null?"":rset1.getString(1);
				contact_person_phone=rset1.getString(2)==null?"":rset1.getString(2);
				contact_person_fax=rset1.getString(3)==null?"":rset1.getString(3);
			}
			rset1.close();
			stmt1.close();
			
			String plantAddress="";
			String plantCity="";
			String plantState="";
			String plantPin="";
			String plantNm="";
			
			queryString2 = "SELECT ADDR,CITY,STATE,PIN,TRUCK_TRANS_NAME "
					+ "FROM FMS_TRUCK_TRANSPORTER_MST A "
					+ "WHERE TRUCK_TRANS_CD=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_TRUCK_TRANSPORTER_MST B WHERE A.TRUCK_TRANS_CD=B.TRUCK_TRANS_CD "
					+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) ";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1, counterparty_cd);
			rset2=stmt2.executeQuery();
			if(rset2.next())
			{
				plantAddress  = rset2.getString(1)==null?"":rset2.getString(1);
				plantCity  = rset2.getString(2)==null?"":rset2.getString(2);
				plantState  = rset2.getString(3)==null?"":rset2.getString(3);
				plantPin  = rset2.getString(4)==null?"":rset2.getString(4);
				plantNm  = rset2.getString(5)==null?"":rset2.getString(5);
			}
			rset2.close();
			stmt2.close();
			
			String ownAddress="";
			String ownCity="";
			String ownState="";
			String ownPin="";
			String ownCountry="";
			String ownPhone="";
			String ownEmail="";
			
			queryString3="SELECT ADDR,CITY,STATE,PIN,COUNTRY,PHONE,EMAIL "
					+ "FROM FMS_COMPANY_OWNER_ADDR_MST A "
					+ "WHERE COMPANY_CD=? AND ADDRESS_TYPE=? "
					+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COMPANY_OWNER_ADDR_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.ADDRESS_TYPE=B.ADDRESS_TYPE "
					+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY'))";
			stmt3=conn.prepareStatement(queryString3);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, "C");
			rset3=stmt3.executeQuery();
			if(rset3.next())
			{
				ownAddress  = rset3.getString(1)==null?"":rset3.getString(1);
				ownCity  = rset3.getString(2)==null?"":rset3.getString(2);
				ownState  = rset3.getString(3)==null?"":rset3.getString(3);
				ownPin  = rset3.getString(4)==null?"":rset3.getString(4);
				ownCountry  = rset3.getString(5)==null?"":rset3.getString(5);
				ownPhone  = rset3.getString(6)==null?"":rset3.getString(6);
				ownEmail  = rset3.getString(7)==null?"":rset3.getString(7);
			}
			rset3.close();
			stmt3.close();
			
			view_info.put("plantAddress",plantAddress);
			view_info.put("plantCity",plantCity);
			view_info.put("plantState",plantState);
			view_info.put("plantPin",plantPin);
			view_info.put("plantNm",plantNm);
			
			view_info.put("ownAddress",ownAddress);
			view_info.put("ownCity",ownCity);
			view_info.put("ownState",ownState);
			view_info.put("ownPin",ownPin);
			view_info.put("ownCountry",ownCountry);
			view_info.put("ownPhone",ownPhone);
			view_info.put("ownEmail",ownEmail);
			
			view_info.put("contact_person_nm",contact_person_nm);
			view_info.put("contact_person_phone",contact_person_phone);
			view_info.put("contact_person_fax",contact_person_fax);
			view_info.put("remark",remark);		 
			view_info.put("seq_no",seq_no);		 
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getViewNominationToTransporterData()
	{
		String function_nm="getViewNominationToTransporterData()";
		try
		{
			HashMap map_gas_dt = new HashMap();
			HashMap map_mmbtu = new HashMap();
			HashMap map_mt = new HashMap();
			HashMap map_grid = new HashMap();
			HashMap map_customer_code = new HashMap();
			HashMap map_ct_ref = new HashMap();
			
			HashMap map_cust_plant = new HashMap();
			HashMap map_truck = new HashMap();
			HashMap map_nom_time = new HashMap();
			HashMap map_nom_dt = new HashMap();
			HashMap map_nom_rev = new HashMap();
			HashMap map_remark = new HashMap();
			HashMap map_checkpost = new HashMap();
			HashMap map_driver = new HashMap();
			
			int count=0;
			
			double tot_mmbtu=0;
			double tot_mt=0;
			
			queryString="SELECT SUM(QTY_MMBTU),SUM(QTY_MT),COUNTERPARTY_CD,PLANT_SEQ,TRUCK_CD,ARRIVAL_TIME,NOM_REV_NO,REMARK,TO_CHAR(ARRIVAL_DT,'DD/MM/YYYY') "
	  				+ "FROM FMS_DLNG_SELLER_NOM_DTL A "
	  				+ "WHERE COMPANY_CD=? AND TRUCK_TRANS_CD=? "
					//+ "AND TRUCK_CD=? "
					+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_SELLER_NOM_DTL B "
					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.COMPANY_CD=A.COMPANY_CD "
					+ "AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					//+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD "
					//+ "AND B.TRUCK_CD=A.TRUCK_CD "
					+ "AND B.BU_SEQ=A.BU_SEQ "
					+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
					+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) "
					+ "GROUP BY COUNTERPARTY_CD,PLANT_SEQ,TRUCK_CD,ARRIVAL_TIME,NOM_REV_NO,REMARK,ARRIVAL_DT "
					+ "ORDER BY COUNTERPARTY_CD,PLANT_SEQ,TRUCK_CD,ARRIVAL_TIME,NOM_REV_NO,REMARK,ARRIVAL_DT ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			//stmt.setString(3, truck_cd);
			stmt.setString(3, gas_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				double mmbtu=rset.getDouble(1);
				double mt = rset.getDouble(2);
				
				tot_mmbtu+=mmbtu;
				tot_mt+=mt;
				
				String counterptyCd=rset.getString(3)==null?"":rset.getString(3);
				String plant_seq=rset.getString(4)==null?"":rset.getString(4);
				String truck_reg_cd=rset.getString(5)==null?"":rset.getString(5);
				String truck_reg_no=utilBean_DLNG.getTruckRegNo(conn, truck_reg_cd);
				String arrival_time=rset.getString(6)==null?"":rset.getString(6);
				String nom_rev=rset.getString(7)==null?"":rset.getString(7);
				String remark=rset.getString(8)==null?"":rset.getString(8);
				String arrival_dt=rset.getString(9)==null?"":rset.getString(9);
				
				String counterpty_abbr=""+utilBean.getCounterpartyABBR(conn,counterptyCd);
				String counterpty_nm=""+utilBean.getCounterpartyName(conn,counterptyCd);
				String plant_nm=""+utilBean.getCounterpartyPlantName(conn,counterptyCd, comp_cd, plant_seq, "C");
				
				HashMap<String, String> plant_detail = utilBean.getCounterpartyPlantDetail(conn,comp_cd, "C", counterptyCd, plant_seq);
				String plantCity = ""+plant_detail.get("plant_city");
				
				String customer_code=""+utilBean.getTranspoterCustomerCode(conn,comp_cd, counterparty_cd, counterptyCd, plant_seq, gas_dt); //20230605 ADDED
				String driver_cd="";
				String driver_nm="";
				String queryString1="SELECT DRIVER_CD "
						+ "FROM FMS_TRUCK_DRIVER_LINK A "
						+ "WHERE TRUCK_CD=? "
						+ "AND LINK_SEQ=(SELECT MAX(LINK_SEQ) FROM FMS_TRUCK_DRIVER_LINK B "
						+ "WHERE A.TRUCK_CD=B.TRUCK_CD AND (RELEASE_DT>=TO_DATE(?,'DD/MM/YYYY') OR RELEASE_DT IS NULL) "
						+ "AND EFF_DT<=TO_DATE(?,'DD/MM/YYYY') "
						+ ") ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, truck_reg_cd);
				stmt1.setString(2, gas_dt);
				stmt1.setString(3, gas_dt);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					driver_cd=rset1.getString(1)==null?"":rset1.getString(1);
					driver_nm=utilBean_DLNG.getDriverName(conn, driver_cd);
				}
				rset1.close();
				stmt1.close();
				
				String checkpost_cd="";
				String checkpost_nm="";
				String queryString2="SELECT CHKPOST_CD "
						+ "FROM FMS_LINK_CHECKPOST_PLANT A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND PLANT_SEQ_NO=? AND ENTITY_TYPE='C' "
						+ "AND (RELEASE_DT>=TO_DATE(?,'DD/MM/YYYY') OR RELEASE_DT IS NULL) "
						+ "AND EFF_DT<=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_LINK_CHECKPOST_PLANT B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY_TYPE=B.ENTITY_TYPE "
						+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO ) " //AND A.CHKPOST_CD=B.CHKPOST_CD
						+ "AND A.REV_SEQ=(SELECT MAX(B.REV_SEQ) FROM FMS_LINK_CHECKPOST_PLANT B WHERE "
						+ "	A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY_TYPE=B.ENTITY_TYPE "
						+ "	AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.EFF_DT=B.EFF_DT)";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, counterptyCd);
				stmt2.setString(3, plant_seq);
				stmt2.setString(4, gas_dt);
				stmt2.setString(5, gas_dt);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					checkpost_cd=rset2.getString(1)==null?"":rset2.getString(1);
					checkpost_nm=utilBean_DLNG.getCheckPostName(conn, checkpost_cd);
				}
				rset2.close();
				stmt2.close();
				
				count+=1;
				map_gas_dt.put(""+count, gas_dt);
				map_mmbtu.put(""+count, nf.format(mmbtu));
				map_mt.put(""+count, nf.format(mt));
				map_grid.put(""+count,counterpty_nm);
				map_customer_code.put(""+count, customer_code);
				
				map_cust_plant.put(""+count, plant_nm +" ("+plantCity+")");
				map_truck.put(""+count, truck_reg_no);
				map_nom_time.put(""+count, arrival_time);
				map_nom_dt.put(""+count, arrival_dt);
				map_nom_rev.put(""+count, nom_rev);
				map_remark.put(""+count, remark);
				map_driver.put(""+count, driver_nm);
				map_checkpost.put(""+count, checkpost_nm);
			}
			rset.close();
			stmt.close();
			
			count+=1;
			map_gas_dt.put(""+count, "");
			map_mmbtu.put(""+count, nf.format(tot_mmbtu));
			map_mt.put(""+count, nf.format(tot_mt));
			map_customer_code.put(""+count, "");
			map_grid.put(""+count,"Total");
			map_cust_plant.put(""+count, "");
			map_truck.put(""+count, "");
			map_nom_time.put(""+count, "");
			map_nom_dt.put(""+count, "");
			map_nom_rev.put(""+count, "");
			map_remark.put(""+count, "");
			map_driver.put(""+count, "");
			map_checkpost.put(""+count, "");
			
			view_data.put("v_gas_dt", map_gas_dt);
			view_data.put("v_mmbtu", map_mmbtu);
			view_data.put("v_mt", map_mt);
			view_data.put("v_customer", map_grid);
			view_data.put("v_customer_code", map_customer_code);
			
			view_data.put("v_customer_plant", map_cust_plant);
			view_data.put("v_truck", map_truck);
			view_data.put("v_nom_time", map_nom_time);
			view_data.put("v_nom_dt", map_nom_dt);
			view_data.put("v_nom_rev", map_nom_rev);
			view_data.put("v_remark", map_remark);
			view_data.put("v_driver", map_driver);
			view_data.put("v_checkpost", map_checkpost);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getDailySellerNominationToTransporter()
	{
		String function_nm="getDailySellerNominationToTransporter()";
		try
		{
			/*queryString="SELECT DISTINCT TRUCK_TRANS_CD "
					+ "FROM FMS_DLNG_SELLER_NOM_DTL A "
					+ "WHERE COMPANY_CD=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_SELLER_NOM_DTL B "
					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD "
					+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
					+ "AND B.GAS_DT=A.GAS_DT) ";
			stmt0=conn.prepareStatement(queryString);
			stmt0.setString(1, comp_cd);
			stmt0.setString(2, gas_dt);
			rset0=stmt0.executeQuery();
			while(rset0.next())
			{
				String trns_cd = rset0.getString(1)==null?"":rset0.getString(1);
				String trns_abbr = ""+utilBean_DLNG.getTruckTransABBR(conn,trns_cd);
				String trns_nm = ""+utilBean_DLNG.getTruckTransName(conn,trns_cd);
				
				VTRANSPORTER_CD.add(trns_cd);
				VTRANSPORTER_ABBR.add(trns_abbr);
				VTRANSPORTER_NM.add(trns_nm);
			}
			rset0.close();
			stmt0.close();*/
			
			//for(int i=0; i<VTRANSPORTER_CD.size(); i++)
			{
				int index=0;
				
				queryString="SELECT DISTINCT TRUCK_TRANS_CD "
		  				+ "FROM FMS_DLNG_SELLER_NOM_DTL A "
		  				+ "WHERE COMPANY_CD=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_SELLER_NOM_DTL B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD "
						//+ "AND B.TRUCK_CD=A.TRUCK_CD "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, gas_dt);
				//stmt.setString(3, transporterCd);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					index++;
					
					String transporterCd=rset.getString(1)==null?"":rset.getString(1);
					String transporterAbbr = ""+utilBean_DLNG.getTruckTransABBR(conn,transporterCd);
					String transporterName = ""+utilBean_DLNG.getTruckTransName(conn,transporterCd);
					
					VTRANSPORTER_CD.add(transporterCd);
					VTRANSPORTER_ABBR.add(transporterAbbr);
					VTRANSPORTER_NM.add(transporterName);
					
					//VTRANSPORTER_TRUCK.add(trns_truck);
					//VTRANSPORTER_TRUCK_NO.add(trns_truck_no);
					
					Vector VTEMP_CONTACT_PERSON=new Vector();
					Vector VTEMP_CONTACT_PERSON_CD=new Vector();
					
					VTEMP_CONTACT_PERSON.add("--Select--");
					VTEMP_CONTACT_PERSON_CD.add("");
					queryString1="SELECT CONTACT_PERSON, SEQ_NO "
							+ "FROM FMS_TRUCK_TRANS_CONTACT_MST A "
							+ "WHERE COMPANY_CD=? AND TRUCK_TRANS_CD=? "
							+ "AND ADDR_FLAG=? AND NOM_FLAG=? "
							+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_TRUCK_TRANS_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.TRUCK_TRANS_CD=B.TRUCK_TRANS_CD AND A.SEQ_NO=B.SEQ_NO "
							+ "AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY'))";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, transporterCd);
					stmt1.setString(3, "R");
					stmt1.setString(4, "Y");
					stmt1.setString(5, "Y");
					stmt1.setString(6, "Y");
					stmt1.setString(7, gas_dt);
					rset1=stmt1.executeQuery();
					while(rset1.next())
					{
						VTEMP_CONTACT_PERSON.add(rset1.getString(1)==null?"":rset1.getString(1));
						VTEMP_CONTACT_PERSON_CD.add(rset1.getString(2)==null?"":rset1.getString(2));
					}
					rset1.close();
					stmt1.close();
					
					String contact_person_cd="";
					queryString2="SELECT SEQ_NO "
							+ "FROM FMS_TRUCK_TRANS_CONTACT_MST A "
							+ "WHERE COMPANY_CD=? AND TRUCK_TRANS_CD=? "
							+ "AND ADDR_FLAG=? AND NOM_FLAG=? "
							+ "AND ACTIVE_FLAG=? AND ADDR_IS_ACTIVE=? "
							+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_TRUCK_TRANS_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.TRUCK_TRANS_CD=B.TRUCK_TRANS_CD AND A.SEQ_NO=B.SEQ_NO "
							+ "AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY'))";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, transporterCd);
					stmt2.setString(3, "R");
					stmt2.setString(4, "Y");
					stmt2.setString(5, "Y");
					stmt2.setString(6, "Y");
					stmt2.setString(7, gas_dt);
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						contact_person_cd=rset2.getString(1)==null?"0":rset2.getString(1);
					}
					rset2.close();
					stmt2.close();
					
					VCONTACT_PERSON.add(VTEMP_CONTACT_PERSON);
					VCONTACT_PERSON_CD.add(VTEMP_CONTACT_PERSON_CD);
					VSEL_CONTACT_PERSON_CD.add(contact_person_cd);
					
					String remark="";
					String contact_person="";
					String pdf_name="";
					String pdf_path="";
					String email_flag="";
					
					queryString4="SELECT REMARK,ADDRESSED_TO,PDF_NM,EMAIL_SENT "
							+ "FROM FMS_DLNG_SELLER_NOM_TRANS A "
							+ "WHERE COMPANY_CD=? "
							+ "AND TRUCK_TRANS_CD=? "
							//+ "AND TRUCK_CD=? "
							+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY')";
					stmt4=conn.prepareStatement(queryString4);
					stmt4.setString(1, comp_cd);
					stmt4.setString(2, transporterCd);
					stmt4.setString(3, gas_dt);
					rset4=stmt4.executeQuery();
					if(rset4.next())
					{
						remark=rset4.getString(1)==null?"":rset4.getString(1);
						contact_person=rset4.getString(2)==null?"":rset4.getString(2);
						pdf_name=rset4.getString(3)==null?"":rset4.getString(3);
						email_flag=rset4.getString(4)==null?"":rset4.getString(4);
						pdf_path=CommonVariable.nom_to_transporter_pdf_path;
					}
					rset4.close();
					stmt4.close();
					
					VREMARK.add(remark);
					VADDRESSED_PERSON.add(contact_person);
					VPDF_NAME.add(pdf_name);
					VPDF_PATH.add(pdf_path);
					VEMAIL_FLAG.add(email_flag);
					
					String pdf_exists="N";
					
					String directory_path=file_path+File.separator+pdf_name;
					
					File pdfFile = new File(directory_path);
					if(pdfFile.exists() && !pdf_name.equals(""))
					{
						pdf_exists="Y";
					}
					VPDF_EXISTS.add(pdf_exists);
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
	
	public void getNominationAndAllocationDtl() 
	{
		String function_nm = "getNominationAndAllocationDtl()";
		try 
		{
			//AP20250529 Commenting all Buyer Nomination Part as for Now.
			String buyerNomQuery = "SELECT SUM(QTY_MMBTU) " 
					+ "FROM FMS_DLNG_BUYER_NOM B "
					+ "WHERE CONT_NO=A.CONT_NO AND AGMT_NO=A.AGMT_NO "
					+ "AND COMPANY_CD=A.COMPANY_CD AND COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND A.BU_SEQ=BU_SEQ "
					+ "AND PLANT_SEQ=A.PLANT_SEQ AND CONTRACT_TYPE=A.CONTRACT_TYPE "
					+ "AND GAS_DT=A.GAS_DT AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_BUYER_NOM C "
					+ "WHERE B.CONT_NO=C.CONT_NO AND B.AGMT_NO=C.AGMT_NO "
					+ "AND B.COMPANY_CD=C.COMPANY_CD AND B.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
					+ "AND C.BU_SEQ=B.BU_SEQ "
					+ "AND B.PLANT_SEQ=C.PLANT_SEQ AND B.CONTRACT_TYPE=C.CONTRACT_TYPE " 
					+ "AND B.GAS_DT=C.GAS_DT AND B.CARGO_NO=C.CARGO_NO) ";

			String sellerNomQuery = "SELECT SUM(QTY_MMBTU) " 
					+ "FROM FMS_DLNG_SELLER_NOM_DTL B "
					+ "WHERE CONT_NO=A.CONT_NO AND AGMT_NO=A.AGMT_NO "
					+ "AND COMPANY_CD=A.COMPANY_CD AND COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND A.BU_SEQ=BU_SEQ "
					+ "AND PLANT_SEQ=A.PLANT_SEQ AND CONTRACT_TYPE=A.CONTRACT_TYPE "
					+ "AND GAS_DT=A.GAS_DT AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_SELLER_NOM_DTL C "
					+ "WHERE B.CONT_NO=C.CONT_NO AND B.AGMT_NO=C.AGMT_NO "
					+ "AND B.COMPANY_CD=C.COMPANY_CD AND B.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
					+ "AND C.BU_SEQ=B.BU_SEQ "
					+ "AND B.PLANT_SEQ=C.PLANT_SEQ AND B.CONTRACT_TYPE=C.CONTRACT_TYPE " 
					+ "AND B.GAS_DT=C.GAS_DT AND B.CARGO_NO=C.CARGO_NO) ";

			String buyerNomCount = "SELECT COUNT(*) " 
					+ "FROM FMS_DLNG_BUYER_NOM B "
					+ "WHERE CONT_NO=A.CONT_NO AND AGMT_NO=A.AGMT_NO "
					+ "AND COMPANY_CD=A.COMPANY_CD AND COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND A.BU_SEQ=BU_SEQ "
					+ "AND PLANT_SEQ=A.PLANT_SEQ AND CONTRACT_TYPE=A.CONTRACT_TYPE "
					+ "AND GAS_DT=A.GAS_DT AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_BUYER_NOM C "
					+ "WHERE B.CONT_NO=C.CONT_NO AND B.AGMT_NO=C.AGMT_NO "
					+ "AND B.COMPANY_CD=C.COMPANY_CD AND B.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
					+ "AND C.BU_SEQ=B.BU_SEQ "
					+ "AND B.PLANT_SEQ=C.PLANT_SEQ AND B.CONTRACT_TYPE=C.CONTRACT_TYPE " 
					+ "AND B.GAS_DT=C.GAS_DT AND B.CARGO_NO=C.CARGO_NO) ";

			String sellerNomCount = "SELECT COUNT(*) " 
					+ "FROM FMS_DLNG_SELLER_NOM_DTL B "
					+ "WHERE CONT_NO=A.CONT_NO AND AGMT_NO=A.AGMT_NO "
					+ "AND COMPANY_CD=A.COMPANY_CD AND COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND A.BU_SEQ=BU_SEQ "
					+ "AND PLANT_SEQ=A.PLANT_SEQ AND CONTRACT_TYPE=A.CONTRACT_TYPE "
					+ "AND GAS_DT=A.GAS_DT AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_SELLER_NOM_DTL C "
					+ "WHERE B.CONT_NO=C.CONT_NO AND B.AGMT_NO=C.AGMT_NO "
					+ "AND B.COMPANY_CD=C.COMPANY_CD AND B.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
					+ "AND C.BU_SEQ=B.BU_SEQ "
					+ "AND B.PLANT_SEQ=C.PLANT_SEQ AND B.CONTRACT_TYPE=C.CONTRACT_TYPE " 
					+ "AND B.GAS_DT=C.GAS_DT AND B.CARGO_NO=C.CARGO_NO) ";

			int sub_index = 0;
			int cont2 = 0;
			double tot_buyer_mmbtu=0;
			double tot_buyer_mt=0;
			double tot_seller_mmbtu=0;
			double tot_seller_mt=0;
			double tot_alloc_mmbtu=0;
			double tot_alloc_mt=0;
			// Fetch contract wise Buyer NOMINATION DATA
			String queryString1 = "SELECT NOM_REV_NO, SUM(QTY_MMBTU),SUM(QTY_MT), "
					+ "COUNTERPARTY_CD,PLANT_SEQ,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TO_CHAR(GAS_DT,'DD/MM/YYYY'),BU_SEQ,CARGO_NO "
					+ "FROM FMS_DLNG_BUYER_NOM A " 
					+ "WHERE COMPANY_CD=? "
					//+ "AND TRUCK_TRANS_CD=? AND TRUCK_CD=? "
					+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_BUYER_NOM B "
					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND A.BU_SEQ=B.BU_SEQ "
					+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
					+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
			if (!counterparty_cd.equals("") && !counterparty_cd.equals("0")) 
			{
				queryString1 += "AND COUNTERPARTY_CD=? ";
			}
			if (chk_diff.equals("Y")) 
			{
				queryString1 += "AND (" + buyerNomCount + ") > 0 AND (" + sellerNomCount + ") > 0 " + "AND (NVL(("+ buyerNomQuery + "),0) - NVL((" + sellerNomQuery + "),0))!=0 ";
			}
			
			// SagarB20250923 Added below block if condition for DLNG-> DLNG Nomination vs Truck Loading
			if (!bu_seq.equals("") && !bu_seq.equals("0")) 
			{
				queryString1 += "AND A.BU_SEQ=? ";
			}
			queryString1 += "GROUP BY  NOM_REV_NO, COUNTERPARTY_CD, PLANT_SEQ, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE,GAS_DT,BU_SEQ,CARGO_NO "
					+ "ORDER BY GAS_DT,NOM_REV_NO, COUNTERPARTY_CD, PLANT_SEQ, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE,BU_SEQ,CARGO_NO ";
			String temp_queryString1=queryString1;
			stmt1 = conn.prepareStatement(temp_queryString1);
			stmt1.setString(++cont2, comp_cd);
			stmt1.setString(++cont2, from_dt);
			stmt1.setString(++cont2, to_dt);
			if (!counterparty_cd.equals("") && !counterparty_cd.equals("0")) 
			{
				stmt1.setString(++cont2, counterparty_cd);
			}
			
			// SagarB20250923 Added below block if condition for DLNG-> DLNG Nomination vs Truck Loading
			if (!bu_seq.equals("") && !bu_seq.equals("0")) 
			{
				stmt1.setString(++cont2, bu_seq);
			}
			rset1 = stmt1.executeQuery();
			while (rset1.next()) 
			{
				sub_index += 1;
				double buyer_mmbtu = rset1.getDouble(2);
				double buyer_mt = rset1.getDouble(3);
				VNOM_REV_NO.add(rset1.getString(1) == null ? "" : rset1.getString(1));
				VQTY_MMBTU.add(nf.format(buyer_mmbtu));
				VQTY_MT.add(nf.format(rset1.getDouble(3)));

				String countpty_cd = rset1.getString(4) == null ? "" : rset1.getString(4);
				String plant_seq = rset1.getString(5) == null ? "" : rset1.getString(5);
				String plant_abbr = utilBean.getCounterpartyPlantABBR(conn,countpty_cd, comp_cd, plant_seq, "C");

				String agmt = rset1.getString(6) == null ? "" : rset1.getString(6);
				String agmt_rev = rset1.getString(7) == null ? "" : rset1.getString(7);
				String cont = rset1.getString(8) == null ? "" : rset1.getString(8);
				String cont_rev = rset1.getString(9) == null ? "" : rset1.getString(9);
				String cont_type = rset1.getString(10) == null ? "" : rset1.getString(10);

				String GasDt = rset1.getString(11) == null ? "" : rset1.getString(11);
				String bu_seq = rset1.getString(12) == null ? "" : rset1.getString(12);
				String bu_abbr = utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_seq, "B");
				
				String cargo_no=rset1.getString(13)==null?"":rset1.getString(13);			//PB20260120			
				String dis_cont_mapping = utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, cargo_no);

				VGAS_DT.add(GasDt);
				VCOUNTERPARTY_CD.add(countpty_cd);
				VCOUNTERPARTY_ABBR.add("" + utilBean.getCounterpartyABBR(conn,countpty_cd));
				VCOUNTERPARTY_NM.add("" + utilBean.getCounterpartyName(conn,countpty_cd));
				VCOUNTERPARTY_PLANT_SEQ.add(plant_seq);
				VCOUNTERPARTY_PLANT_ABBR.add(plant_abbr);
				VBU_PLANT_ABBR.add(bu_abbr);
				VCONTRACT_TYPE.add(cont_type);
				VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_type));
				VDIS_CONT_MAPPING.add(dis_cont_mapping);
				tot_buyer_mmbtu+=buyer_mmbtu;
				tot_buyer_mt+=buyer_mt;
				
				double seller_mmbtu = 0;
				double seller_mt = 0;
				int cont3=0;
				// Fetch Contract wise Seller Nomination Data
				String queryString3 = "SELECT SUM(QTY_MMBTU),SUM(QTY_MT),COUNT(*) " 
						+ "FROM FMS_DLNG_SELLER_NOM_DTL A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND PLANT_SEQ=? AND AGMT_NO=? AND CONTRACT_TYPE=? " 
						+ "AND CONT_NO=? AND BU_SEQ=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_SELLER_NOM_DTL B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND A.BU_SEQ=B.BU_SEQ "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
						+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				queryString3 += "GROUP BY  NOM_REV_NO, COUNTERPARTY_CD, PLANT_SEQ, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE,GAS_DT,BU_SEQ "
						+ "ORDER BY GAS_DT,NOM_REV_NO, COUNTERPARTY_CD, PLANT_SEQ, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE,BU_SEQ ";
				stmt3 = conn.prepareStatement(queryString3);
				stmt3.setString(++cont3, comp_cd);
				stmt3.setString(++cont3, countpty_cd);
				stmt3.setString(++cont3, plant_seq);
				stmt3.setString(++cont3, agmt);
				stmt3.setString(++cont3, cont_type);
				stmt3.setString(++cont3, cont);
				stmt3.setString(++cont3, bu_seq);
				stmt3.setString(++cont3, GasDt);
				stmt3.setString(++cont3, cargo_no);
				rset3 = stmt3.executeQuery();
				if (rset3.next()) 
				{
					seller_mmbtu = rset3.getDouble(1);
					seller_mt = rset3.getDouble(2);
					int count = rset3.getInt(3);
					if (count > 0) 
					{
						String color = "";
						if (Double.doubleToRawLongBits(buyer_mmbtu) - Double.doubleToRawLongBits(seller_mmbtu) != Double.doubleToRawLongBits(0)) 
						{
							color = "red";
						}
						VQTY_SELLER_MMBTU.add(nf.format(seller_mmbtu));
						VQTY_SELLER_MT.add(nf.format(seller_mt));
						VCOLOR.add(color);
					} 
					else 
					{
						VQTY_SELLER_MMBTU.add("-");
						VQTY_SELLER_MT.add("-");
						VCOLOR.add("");
					}
					tot_seller_mmbtu+=seller_mmbtu;
					tot_seller_mt+=seller_mt;
				} 
				else 
				{
					VQTY_SELLER_MMBTU.add("-");
					VQTY_SELLER_MT.add("-");
					VCOLOR.add("");
					tot_seller_mmbtu+=0;
					tot_seller_mt+=0;
				}
				rset3.close();
				stmt3.close();
				
				double dcq = 0;
				String cont_ref = "";
				String agmt_base = "";
				
				String queryString2 = "SELECT CONT_REF_NO,DCQ,TRADE_REF_NO  " 
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) AND CONTRACT_TYPE=? " 
						+ "AND AGMT_NO=? AND AGMT_REV=? ";
				if (!counterparty_cd.equals("") && !counterparty_cd.equals("0")) 
				{
					queryString2 += "AND COUNTERPARTY_CD=? ";
				}
				queryString2 += "UNION ";		//pb20260120: ADDED FOR HANDLING CARGO
				queryString2 += "SELECT CARGO_REF,CSOC_QTY,NULL "
						+ "FROM FMS_LTCORA_CONT_CARGO_DTL A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND BUY_SALE=? AND AGMT_TYPE=? "
						+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE "
						+ "AND A.CARGO_NO=B.CARGO_NO) AND CONTRACT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND CARGO_NO=? ";
				if (!counterparty_cd.equals("") && !counterparty_cd.equals("0")) 
				{
					queryString2 += "AND COUNTERPARTY_CD=? ";
				}
				int stmt2_count=0;
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(++stmt2_count, comp_cd);
				stmt2.setString(++stmt2_count, countpty_cd);
				stmt2.setString(++stmt2_count, cont);
				stmt2.setString(++stmt2_count, cont_type);
				stmt2.setString(++stmt2_count, agmt);
				stmt2.setString(++stmt2_count, agmt_rev);
				if (!counterparty_cd.equals("") && !counterparty_cd.equals("0")) 
				{
					stmt2.setString(++stmt2_count, counterparty_cd);
				}
				stmt2.setString(++stmt2_count, comp_cd);
				stmt2.setString(++stmt2_count, countpty_cd);
				stmt2.setString(++stmt2_count, cont);
				stmt2.setString(++stmt2_count, "C");
				stmt2.setString(++stmt2_count, "A");
				stmt2.setString(++stmt2_count, cont_type);
				stmt2.setString(++stmt2_count, agmt);
				stmt2.setString(++stmt2_count, agmt_rev);
				stmt2.setString(++stmt2_count, cargo_no);
				if (!counterparty_cd.equals("") && !counterparty_cd.equals("0")) 
				{
					stmt2.setString(++stmt2_count, counterparty_cd);
				}
				rset2 = stmt2.executeQuery();
				if (rset2.next()) 
				{
					cont_ref = rset2.getString(1) == null ? "" : rset2.getString(1);
					dcq = rset2.getDouble(2);
					String trade_ref = rset2.getString(3) == null ? "" : rset2.getString(3);
					if (cont_type.equals("W")) 
					{
						cont_ref = trade_ref;
					}
				}
				rset2.close();
				stmt2.close();

				String variable_dcq = utilBean.getContVariableDCQ(conn,comp_cd, countpty_cd, agmt, cont, cont_type,GasDt);
				if(!cargo_no.equals("") && !cargo_no.equals("0"))
				{
					variable_dcq = utilBean.getCargoVariableCSOC(conn,comp_cd,countpty_cd,"C",agmt,cont,cont_type,cargo_no,GasDt);
				}
				if (!variable_dcq.equals("")) 
				{
					dcq = Double.parseDouble(variable_dcq);
				}
				VCONT_REF.add(cont_ref);
				VDCQ.add(nf.format(dcq));
				
				// Fetch Contract wise Allocation Data
				int st_count=0;
				double alloc_mmbtu = 0;
				double alloc_mt = 0;
				String temp_alloc_mmbtu = "";
				
				String queryString4="SELECT SUM(QTY_MMBTU),SUM(QTY_MT) "
		  				+ "FROM FMS_DLNG_ALLOC_MST A "
		  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						//+ "AND TRUCK_TRANS_CD=? AND TRUCK_CD=? "
						+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
						+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						//+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				queryString4 += "GROUP BY  NOM_REV_NO, COUNTERPARTY_CD, PLANT_SEQ, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE,GAS_DT,BU_SEQ "
						+ "ORDER BY GAS_DT,NOM_REV_NO, COUNTERPARTY_CD, PLANT_SEQ, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE,BU_SEQ ";
				stmt4 = conn.prepareStatement(queryString4);
				stmt4.setString(++st_count, cont);
				stmt4.setString(++st_count, agmt);
				stmt4.setString(++st_count, comp_cd);
				stmt4.setString(++st_count, countpty_cd);
				stmt4.setString(++st_count, plant_seq);
				stmt4.setString(++st_count, cont_type);
				stmt4.setString(++st_count, bu_seq);
				stmt4.setString(++st_count, GasDt);
				stmt4.setString(++st_count, cargo_no);
				rset4 = stmt4.executeQuery();
				if(rset4.next())
				{
					temp_alloc_mmbtu=rset4.getString(1)== null ? "" : rset4.getString(1);
					alloc_mmbtu=rset4.getDouble(1);
					alloc_mt=rset4.getDouble(2);
				}
				stmt4.close();
				rset4.close();
				
				if (!temp_alloc_mmbtu.equals("")) 
				{
					String color = "";
					if (Double.doubleToRawLongBits(seller_mmbtu) - Double.doubleToRawLongBits(alloc_mmbtu) != Double.doubleToRawLongBits(0)) 
					{
						color = "red";
					}

					VQTY_ALLOC_MMBTU.add(nf1.format(alloc_mmbtu));
					VQTY_ALLOC_MT.add(nf1.format(alloc_mt));
					VCOLOR_ALLOC.add(color);
					
					tot_alloc_mmbtu+=alloc_mmbtu;
					tot_alloc_mt+=alloc_mt;
				} 
				else 
				{
					VQTY_ALLOC_MMBTU.add("-");
					VQTY_ALLOC_MT.add("-");
					VCOLOR_ALLOC.add("");
					
					tot_alloc_mmbtu+=0;
					tot_alloc_mt+=0;
				}
			}
			VSUB_INDEX.add(sub_index);
			rset1.close();
			stmt1.close();
			
			total_buyer_mmbtu=nf.format(tot_buyer_mmbtu);
			total_buyer_mt=nf.format(tot_buyer_mt);
			total_seller_mmbtu=nf.format(tot_seller_mmbtu);
			total_seller_mt=nf.format(tot_seller_mt);
			total_alloc_mmbtu=nf.format(tot_alloc_mmbtu);
			total_alloc_mt=nf.format(tot_alloc_mt);
		} 
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getTransporterMstFromSellerNom() //AP20250529: IN Sales side There is getTransporterMstFromBuyerNom()
	{
		String function_nm = "getTransporterMstFromSellerNom()";
		try 
		{
			String queryString = "SELECT DISTINCT TRUCK_TRANS_CD " 
					+ "FROM FMS_DLNG_SELLER_NOM_DTL " 
					+ "WHERE COMPANY_CD=? "
					+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') ";
			if (!counterparty_cd.equals("") && !counterparty_cd.equals("0")) 
			{
				queryString += "AND COUNTERPARTY_CD=? ";
			}
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, from_dt);
			stmt.setString(3, to_dt);
			if (!counterparty_cd.equals("") && !counterparty_cd.equals("0")) 
			{
				stmt.setString(4, counterparty_cd);
			}
			rset = stmt.executeQuery();
			while (rset.next())
			{
				String transporter_cd = rset.getString(1) == null ? "" : rset.getString(1);
				VMST_TRANSPORTER_CD.add(transporter_cd);
				VMST_TRANSPORTER_NM.add(""+utilBean_DLNG.getTruckTransName(conn, transporter_cd));
				VMST_TRANSPORTER_ABBR.add(""+utilBean_DLNG.getTruckTransABBR(conn,transporter_cd));
			}
			rset.close();
			stmt.close();

			int count = 0;
			String queryString1 = "SELECT DISTINCT TRUCK_TRANS_CD, TRUCK_CD " 
					+ "FROM FMS_DLNG_SELLER_NOM_DTL "
					+ "WHERE COMPANY_CD=? "
					+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') ";
			if (!counterparty_cd.equals("") && !counterparty_cd.equals("0")) 
			{
				queryString1 += "AND COUNTERPARTY_CD=? ";
			}
			if (!transporter_cd.equals("") && !transporter_cd.equals("0")) 
			{
				queryString1 += "AND TRUCK_TRANS_CD=? ";
			}
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(++count, comp_cd);
			stmt1.setString(++count, from_dt);
			stmt1.setString(++count, to_dt);
			if (!counterparty_cd.equals("") && !counterparty_cd.equals("0")) 
			{
				stmt1.setString(++count, counterparty_cd);
			}
			if (!transporter_cd.equals("") && !transporter_cd.equals("0")) 
			{
				stmt1.setString(++count, transporter_cd);
			}
			rset1 = stmt1.executeQuery();
			while (rset1.next()) 
			{
				String transporter_cd = rset1.getString(1) == null ? "" : rset1.getString(1);
				String transporter_truck = rset1.getString(2) == null ? "" : rset1.getString(2);
				VMST_TRANSPORTER_TRUCK.add(transporter_truck);
				VMST_TRANSPORTER_TRUCK_NO.add(""+utilBean_DLNG.getTruckRegNo(conn, transporter_truck));
			}
			rset1.close();
			stmt1.close();
		} 
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCustomerMstFromBuyerNom()
	{
		String function_nm = "getCustomerMstFromBuyerNom()";
		try 
		{
			String queryString = "SELECT DISTINCT COUNTERPARTY_CD " 
					+ "FROM FMS_DLNG_BUYER_NOM " 
					+ "WHERE COMPANY_CD=? "
					+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, from_dt);
			stmt.setString(3, to_dt);
			rset = stmt.executeQuery();
			while (rset.next()) 
			{
				String counterpty_cd = rset.getString(1) == null ? "" : rset.getString(1);
				VMST_COUNTERPARTY_CD.add(counterpty_cd);
				VMST_COUNTERPARTY_NM.add("" + utilBean.getCounterpartyName(conn,counterpty_cd));
				VMST_COUNTERPARTY_ABBR.add("" + utilBean.getCounterpartyABBR(conn,counterpty_cd));
			}
			rset.close();
			stmt.close();
		} 
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	private void getContractTcqVariation() 
	{
		String function_nm="getContractTcqVariation()";
		try
		{
			if(!segmentType.equals("0"))
			{
				VTEMP_SEGMENT_TYPE.add(segmentType);
				if(segmentType.equals("F"))
				{
					VTEMP_SEGMENT.add("DLNG Supply Notice");
				}
				else if(segmentType.equals("E"))
				{
					VTEMP_SEGMENT.add("DLNG Letter of Agreement");
				}
				else if(segmentType.equals("W"))
				{
					VTEMP_SEGMENT.add("DLNG IGX");
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
					
					if(cont_type.equals("W"))
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
					VCONT_STATUS.add(utilBean.ContStatusName(cont_status_flg));
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
	
	public void getDateWiseAllocationQty() 
	{
		String function_nm = "getDateWiseAllocationQty()";
		try 
		{
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
				String date = rset.getString(1) == null ? "" : rset.getString(1);
				VALLOCATION_DATA.add(date);
				VCOLOR.add("");
				for (int i = 0; i < VCOUNTERPARTY_CD.size(); i++) 
				{
					int st_count=0;
					double alloc_mmbtu = 0;
					double alloc_scm = 0;
					String temp_allocation_mmbtu="";
					String temp_alloc_mmbtu = "";
					String temp_alloc_mt = "";
					
					String queryString4 ="";
					queryString4="SELECT SUM(QTY_MMBTU),SUM(QTY_MT) "
			  				+ "FROM FMS_DLNG_ALLOC_MST A "
			  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
							+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONTRACT_TYPE=? AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') ";
					//RG20250926 Added this block for adding BU wise filter
					if(!bu_plant.equals("0") && (!bu_plant.equals(""))) {
					queryString4 += " AND BU_SEQ=? ";		
						}
					//
					queryString4 += " AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							//+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD "
							+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
							+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
					stmt4 = conn.prepareStatement(queryString4);
					stmt4.setString(++st_count, ""+VCONT_NO.elementAt(i));
					stmt4.setString(++st_count, ""+VAGMT_NO.elementAt(i));
					stmt4.setString(++st_count, comp_cd);
					stmt4.setString(++st_count, ""+VCOUNTERPARTY_CD.elementAt(i));
					stmt4.setString(++st_count, ""+VCONTRACT_TYPE.elementAt(i));
					stmt4.setString(++st_count, date);
					stmt4.setString(++st_count, date);
					//RG20250922 Added this block for adding BU wise filter
					if(!bu_plant.equals("0") && (!bu_plant.equals(""))) {
					stmt4.setString(++st_count, bu_plant);
					}
					//
					rset4 = stmt4.executeQuery();
					if(rset4.next())
					{
						temp_allocation_mmbtu=rset4.getString(1)== null ? "" : rset4.getString(1);
						temp_alloc_mmbtu=nf1.format(rset4.getDouble(1));
						temp_alloc_mt=nf1.format(rset4.getDouble(2));
					}
					stmt4.close();
					rset4.close();
					
					String qty = "";
					qty = temp_alloc_mmbtu;
					VALLOCATION_DATA.add(qty);
					if (!temp_allocation_mmbtu.equals("")) 
					{
						VCOLOR.add("#99ffcc");
					} 
					else 
					{
						VCOLOR.add("");
					}
					String scm = temp_alloc_mt;
					VALLOCATION_DATA.add(scm);
					if (!temp_allocation_mmbtu.equals("")) 
					{
						VCOLOR.add("#99ffcc");
					} 
					else 
					{
						VCOLOR.add("");
					}
				}
			}
			rset.close();
			stmt.close();
			
			double total_mmbtu=0;
			double total_mt=0;
			double temp_total_mmbtu = 0;
			double temp_total_mt = 0;
			for (int k = 0; k < VCOUNTERPARTY_CD.size(); k++) 
			{
				int st_count=0;
				queryString1 = "SELECT SUM(QTY_MMBTU), SUM(QTY_MT) "
						+ "FROM FMS_DLNG_ALLOC_MST A " 
						+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONTRACT_TYPE=? AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') ";
				//RG20250926 Added this block for adding BU wise filter
				if(!bu_plant.equals("0") && (!bu_plant.equals(""))) {
				queryString1 += " AND BU_SEQ=? ";		
					}
				//
				queryString1 += " AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						//+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(++st_count, ""+VCONT_NO.elementAt(k));
				stmt1.setString(++st_count, ""+VAGMT_NO.elementAt(k));
				stmt1.setString(++st_count, comp_cd);
				stmt1.setString(++st_count, ""+VCOUNTERPARTY_CD.elementAt(k));
				stmt1.setString(++st_count, ""+VCONTRACT_TYPE.elementAt(k));
				stmt1.setString(++st_count, from_dt);
				stmt1.setString(++st_count, to_dt);
				//RG20250922 Added this block for adding BU wise filter
				if(!bu_plant.equals("0") && (!bu_plant.equals(""))) {
				stmt1.setString(++st_count, bu_plant);
				}
				//
				rset1 = stmt1.executeQuery();
				while (rset1.next()) 
				{
					total_mmbtu = rset1.getDouble(1);
					total_mt = rset1.getDouble(2);

					if (Double.doubleToRawLongBits(total_mmbtu) != Double.doubleToRawLongBits(0)) 
					{
						temp_total_mmbtu = total_mmbtu;
						temp_total_mt = total_mt;
					}
					else
					{
						temp_total_mmbtu = 0.00;
						temp_total_mt = 0.00;
					}
					VTOTAL_MMBTU.add(nf1.format(temp_total_mmbtu));
					VTOTAL_MT.add(nf1.format(temp_total_mt));
				}
				rset1.close();
				stmt1.close();
			}
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
			if (!segmentType.equals("0")) 
			{
				VTEMP_SEGMENT_TYPE.add(segmentType);
				if(segmentType.equals("F"))
				{
					VTEMP_SEGMENT.add("DLNG Supply Notice");
				}
				else if(segmentType.equals("E"))
				{
					VTEMP_SEGMENT.add("DLNG Letter of Agreement");
				}
				else if(segmentType.equals("W"))
				{
					VTEMP_SEGMENT.add("DLNG IGX");
				}
				else if (segmentType.equals("A"))
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
				VTEMP_SEGMENT = VSEGMENT;
				VTEMP_SEGMENT_TYPE = VSEGMENT_TYPE;
			}

			for (int i = 0; i < VTEMP_SEGMENT.size(); i++) 
			{
				if(VTEMP_SEGMENT_TYPE.elementAt(i).equals("A"))
				{
					getLTCORASellContDtl(VTEMP_SEGMENT_TYPE.elementAt(i).toString());
				}
				else
				{
				
					String queryString = "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONT_REF_NO,CONTRACT_TYPE,"
							+ "TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),AGMT_BASE "
							+ "FROM FMS_SUPPLY_CONT_MST A " 
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND START_DT<=TO_DATE(?,'DD/MM/YYYY') AND END_DT>=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
					if (!counterparty_cd.equals("0")) 
					{
						queryString += " AND COUNTERPARTY_CD=? ";
					}
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, "" + VTEMP_SEGMENT_TYPE.elementAt(i));
					stmt.setString(3, to_dt);
					stmt.setString(4, from_dt);
					if (!counterparty_cd.equals("0")) 
					{
						stmt.setString(5, counterparty_cd);
					}
					rset = stmt.executeQuery();
					while (rset.next()) 
					{
						String companyCd = rset.getString(1) == null ? "" : rset.getString(1);
						String countpty_cd = rset.getString(2) == null ? "" : rset.getString(2);
						String agmt = rset.getString(3) == null ? "" : rset.getString(3);
						String agmt_rev = rset.getString(4) == null ? "" : rset.getString(4);
						String cont = rset.getString(5) == null ? "" : rset.getString(5);
						String cont_rev = rset.getString(6) == null ? "" : rset.getString(6);
						String cont_ref = rset.getString(7) == null ? "" : rset.getString(7);
						String cont_type = "" + VTEMP_SEGMENT_TYPE.elementAt(i); // 8
						String start_dt = rset.getString(9) == null ? "" : rset.getString(9);
						String end_dt = rset.getString(10) == null ? "" : rset.getString(10);
						String agmt_base = rset.getString(11) == null ? "" : rset.getString(11);
	
						VCOUNTERPARTY_CD.add(countpty_cd);
						VCOUNTERPARTY_ABBR.add("" + utilBean.getCounterpartyABBR(conn,countpty_cd));
						VCOUNTERPARTY_NM.add("" + utilBean.getCounterpartyName(conn,countpty_cd));
						VDIS_CONT_MAPPING.add(utilBean.NewDealMappingId(companyCd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, ""));
						/*if (cont_type.equals("S")) 
						{
							VDIS_CONT_MAPPING.add(cont_type + agmt + "-" + cont);
						} 
						else 
						{
							VDIS_CONT_MAPPING.add(cont_type + "" + cont);
						}*/
						VAGMT_NO.add(agmt);
						VCONT_NO.add(cont);
						VCONTRACT_TYPE.add(cont_type);
						VCONT_REF.add(cont_ref);
						VSTART_DT.add(start_dt);
						VEND_DT.add(end_dt);
						VAGMT_BASE.add(agmt_base);
						VCARGO_NO.add("0");
						VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_type));
					}
					rset.close();
					stmt.close();
				}
			}
		} 
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getLTCORASellContDtl(String segment)
	{
		String function_nm="getLTCORASellContDtl()";
		try
		{
			int index=0;
			
			String queryString="SELECT A.COMPANY_CD,A.COUNTERPARTY_CD,A.CARGO_REF, TO_CHAR(A.ACTUAL_RECPT_DT,'DD/MM/YYYY'), "
					+ "TO_CHAR((A.ACTUAL_RECPT_DT + COALESCE(A.STORAGE_EXT_DAYS, 0) + COALESCE(A.STORAGE_DAYS-1, 0)),'DD/MM/YYYY'), C.AGMT_BASE, A.AGMT_NO, A.AGMT_REV,  A.CONT_NO, A.CONT_REV, A.CONTRACT_TYPE, A.CARGO_NO, EDQ_QTY,       "
					+ "TO_CHAR(A.QQ_DT,'DD/MM/YYYY'), TO_CHAR(A.ACTUAL_RECPT_DT,'DD/MM/YYYY'), C.SUG, C.CONT_REF_NO, C.CONT_STATUS, C.LTCORA_TARIFF, C.LTCORA_TARIFF_UNIT,  "
					+ "A.STORAGE_DAYS,A.STORAGE_EXT_DAYS, C.EXTEND_STORAGE "
					+ "FROM FMS_LTCORA_CONT_CARGO_DTL A, FMS_LTCORA_CONT_MST C  "
					+ "WHERE A.COMPANY_CD=? AND A.AGMT_TYPE=?"
					+ "AND A.CONT_REV=(SELECT MAX(CONT_REV) FROM FMS_LTCORA_CONT_CARGO_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_TYPE=B.AGMT_TYPE "
					+ "AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.ACTUAL_RECPT_DT<=TO_DATE(?,'DD/MM/YYYY') AND "
					+ "NVL((TO_DATE(A.ACTUAL_RECPT_DT)+A.STORAGE_DAYS-1+ A.STORAGE_EXT_DAYS),(TO_DATE(A.ACTUAL_RECPT_DT)+A.STORAGE_DAYS-1))>= TO_DATE(?,'DD/MM/YYYY') "
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
				String companyCd = rset.getString(1) == null ? "" : rset.getString(1);
				String countpty_cd = rset.getString(2) == null ? "" : rset.getString(2);
				String cont_ref = rset.getString(3) == null ? "" : rset.getString(3);
				String start_dt = rset.getString(4) == null ? "" : rset.getString(4);
				String end_dt = rset.getString(5) == null ? "" : rset.getString(5);
				String agmt_base = rset.getString(6) == null ? "" : rset.getString(6);
				String agmt = rset.getString(7) == null ? "" : rset.getString(7);
				String agmt_rev = rset.getString(8) == null ? "" : rset.getString(8);
				String cont = rset.getString(9) == null ? "" : rset.getString(9);
				String cont_rev = rset.getString(10) == null ? "" : rset.getString(10);
				String cont_type = rset.getString(11) == null ? "" : rset.getString(11);
				String cargo_no = rset.getString(12) == null ? "" : rset.getString(12);
				
				
				VCOUNTERPARTY_CD.add(countpty_cd);
				VCOUNTERPARTY_ABBR.add("" + utilBean.getCounterpartyABBR(conn,countpty_cd));
				VCOUNTERPARTY_NM.add("" + utilBean.getCounterpartyName(conn,countpty_cd));
				VDIS_CONT_MAPPING.add(utilBean.NewDealMappingId(companyCd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, cargo_no));
				VAGMT_NO.add(agmt);
				VCONT_NO.add(cont);
				VCONTRACT_TYPE.add(cont_type);
				VCONT_REF.add(cont_ref);
				VSTART_DT.add(start_dt);
				VEND_DT.add(end_dt);
				VAGMT_BASE.add(agmt_base);
				VCARGO_NO.add(cargo_no);
				VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_type));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
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

			for (int i = 0; i < TEMP_CUST_CD.size(); i++) 
			{
				Vector TEMP_CUST_PLANT_SEQ = new Vector();
				Vector TEMP_CUST_PLANT_NM = new Vector();

				String queryString = "SELECT SEQ_NO,PLANT_NAME " 
						+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
						+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND COMPANY_CD=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) "
						+ "AND STATUS=? " 
						+ "ORDER BY PLANT_NAME";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, "" + TEMP_CUST_CD.elementAt(i));
				stmt.setString(2, "C");
				stmt.setString(3, comp_cd);
				stmt.setString(4, "Y");
				rset = stmt.executeQuery();
				while (rset.next()) 
				{
					TEMP_CUST_PLANT_SEQ.add(rset.getString(1) == null ? "" : rset.getString(1));
					TEMP_CUST_PLANT_NM.add(rset.getString(2) == null ? "" : rset.getString(2));
				}
				rset.close();
				stmt.close();

				VCOUNTERPARTY_PLANT_SEQ.add(TEMP_CUST_PLANT_SEQ);
				VCOUNTERPARTY_PLANT_NM.add(TEMP_CUST_PLANT_NM);
			}
			// String countpty_cd="";
			for (int i = 0; i < TEMP_CUST_CD.size(); i++) 
			{
				String countpty_cd = "" + TEMP_CUST_CD.elementAt(i);
				VCOUNTERPARTY_CD.add(countpty_cd);
				VCOUNTERPARTY_ABBR.add(TEMP_CUST_ABBR.elementAt(i));
				VCOUNTERPARTY_NM.add(TEMP_CUST_NM.elementAt(i));

				int index = 0;

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

					String cont_map = countpty_cd + "-%-%-%-%-%";
					String queryString1 = "SELECT SUM(QTY_MMBTU), SUM(QTY_MT) "
							+ "FROM FMS_DLNG_ALLOC_MST A " 
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') ";
					//RG20250922 Added this block for adding BU wise filter
					if(!bu_plant.equals("0") && (!bu_plant.equals(""))) {
					queryString1+= " AND BU_SEQ=? ";		
						}
					//
					queryString1+= " AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							//+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD "
							+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
							+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, countpty_cd);
					stmt1.setString(3, gas_dt);
					if(!bu_plant.equals("0") && (!bu_plant.equals(""))) {//RG20250926 added for DLNG BU wise filter
					stmt1.setString(4, bu_plant);		
						}
					rset1 = stmt1.executeQuery();
					while (rset1.next()) 
					{
						temp_mmbtu = rset1.getString(1) == null ? "" : rset1.getString(1);
						mmbtu = rset1.getDouble(1);
						scm = rset1.getDouble(2);
						// int count=rset1.getInt(2);

						if (temp_mmbtu.equals("")) 
						{
							VQTY_MMBTU.add("-");
							VQTY_SCM.add("-");

						} 
						else 
						{
							VQTY_MMBTU.add(nf1.format(mmbtu));
							VQTY_SCM.add(nf1.format(scm));
						}
					}
					rset1.close();
					stmt1.close();

					for (int j = 0; j < ((Vector) VCOUNTERPARTY_PLANT_SEQ.elementAt(i)).size(); j++) 
					{
						String plantSeq = "" + ((Vector) VCOUNTERPARTY_PLANT_SEQ.elementAt(i)).elementAt(j);
						queryString1 = "SELECT SUM(QTY_MMBTU), SUM(QTY_MT) "
								+ "FROM FMS_DLNG_ALLOC_MST A " 
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND PLANT_SEQ=? ";
						//RG20250922 Added this block for adding BU wise filter
						if(!bu_plant.equals("0") && (!bu_plant.equals(""))) {
						queryString1+= " AND BU_SEQ=? ";		
							}
						//
						queryString1+= " AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B "
								+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
								+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
								//+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD "
								+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
								+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, countpty_cd);
						stmt1.setString(3, gas_dt);
						stmt1.setString(4, plantSeq);
						if(!bu_plant.equals("0") && (!bu_plant.equals(""))) {//RG20250926 added for DLNG BU wise filter
						stmt1.setString(5, bu_plant);		
						}
						rset1 = stmt1.executeQuery();
						while (rset1.next())
						{
							temp_mmbtu = rset1.getString(1) == null ? "" : rset1.getString(1);
							mmbtu = rset1.getDouble(1);

							scm = rset1.getDouble(2);
							// int count=rset1.getInt(2);

							if (temp_mmbtu.equals("")) 
							{
								VQTY_MMBTU.add("-");
								VQTY_SCM.add("-");
							} 
							else 
							{
								VQTY_MMBTU.add(nf1.format(mmbtu));
								VQTY_SCM.add(nf1.format(scm));
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

				String queryString1 = "SELECT SUM(QTY_MMBTU), SUM(QTY_MT) "
						+ "FROM FMS_DLNG_ALLOC_MST A " 
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') ";
					//RG20250922 Added this block for adding BU wise filter
					if(!bu_plant.equals("0") && (!bu_plant.equals(""))) {
					 queryString1+= " AND BU_SEQ=? ";		
						}
					//
					 queryString1+= " AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						//+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO)";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, countpty_cd);
				stmt1.setString(3, from_dt);
				stmt1.setString(4, to_dt);
				if(!bu_plant.equals("0") && (!bu_plant.equals(""))) {//RG20250926 added for DLNG BU wise filter
				stmt1.setString(5, bu_plant);		
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
				VTOTAL_QTY_MMBTU.add(nf1.format(temp_total_mmbtu));
				VTOTAL_QTY_SCM.add(nf1.format(temp_total_scm));

				for (int k = 0; k < ((Vector) VCOUNTERPARTY_PLANT_SEQ.elementAt(i)).size(); k++) 
				{
					String plantSeq = "" + ((Vector) VCOUNTERPARTY_PLANT_SEQ.elementAt(i)).elementAt(k);

					queryString1 = "SELECT SUM(QTY_MMBTU), SUM(QTY_MT) "
							+ "FROM FMS_DLNG_ALLOC_MST A " 
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') AND PLANT_SEQ=? ";
					//RG20250922 Added this block for adding BU wise filter
					if(!bu_plant.equals("0") && (!bu_plant.equals(""))) {
					 queryString1+= " AND BU_SEQ=? ";		
						}
					//
					queryString1+= " AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							//+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD "
							+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
							+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, countpty_cd);
					stmt1.setString(3, from_dt);
					stmt1.setString(4, to_dt);
					stmt1.setString(5, plantSeq);
					if(!bu_plant.equals("0") && (!bu_plant.equals(""))) {//RG20250926 added for DLNG BU wise filter
					stmt1.setString(6, bu_plant);		
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
						VTOTAL_QTY_MMBTU.add(nf1.format(temp_total_mmbtu));
						VTOTAL_QTY_SCM.add(nf1.format(temp_total_scm));
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
	
	public void getCustomerMasterForAllocToCust() 
	{
		String function_nm = "getCustomerMasterForAllocToCust()";
		try 
		{
			String queryString = "SELECT DISTINCT COUNTERPARTY_CD " 
					+ "FROM FMS_DLNG_ALLOC_MST A "
					+ "WHERE COMPANY_CD=? " 
					+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B "
					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD "
					+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
					+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, from_dt);
			stmt.setString(3, to_dt);
			rset = stmt.executeQuery();
			while (rset.next()) 
			{
				String cust_cd=rset.getString(1) == null ?"":rset.getString(1);
				String cust_abbr=""+utilBean.getCounterpartyABBR(conn,cust_cd);
				String cust_nm=""+utilBean.getCounterpartyName(conn,cust_cd);

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
	
	public void getSellerNominationRevisionWise() 
	{
		String function_nm = "getSellerNominationRevisionWise()";
		try 
		{
			String queryString = "SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_DLNG_SELLER_NOM_DTL A "
					+ "WHERE COMPANY_CD=? AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') ";
			
			if(!counterparty_cd.equals("0")) 
			{
				queryString += "AND COUNTERPARTY_CD= ?";
			}
			stmt0 = conn.prepareStatement(queryString);
			stmt0.setString(1, comp_cd);
			stmt0.setString(2, gas_dt);
			if(!counterparty_cd.equals("0")) {
				stmt0.setString(3, counterparty_cd);
			}
			rset0 = stmt0.executeQuery();
			while (rset0.next()) 
			{
				int index = 0;
				
				String cp_cd = rset0.getString(1) == null ? "" : rset0.getString(1);
				String cp_abbr = utilBean.getCounterpartyABBR(conn, cp_cd); //""+utilBean_DLNG.getTruckTransABBR(conn, trns_cd);
				String cp_name = utilBean.getCounterpartyName(conn, cp_cd);

				VCOUNTERPARTY_CD.add(cp_cd);
				VCOUNTERPARTY_ABBR.add(cp_abbr+"-"+cp_name);
				
				// GET SELLER NOMINATION DATA FOR THE CONTRACT WISE
				String queryString1 = "SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_MT,TO_CHAR(GEN_DT,'DD/MM/YYYY'),"
						+ "COUNTERPARTY_CD,PLANT_SEQ,BU_SEQ,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TRUCK_CD,TRUCK_TRANS_CD,CARGO_NO "
						+ "FROM FMS_DLNG_SELLER_NOM_DTL A " 
						+ "WHERE COMPANY_CD=? "
						+ "AND COUNTERPARTY_CD=? "
						//+ "AND TRUCK_CD=? " 
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "ORDER BY NOM_REV_NO";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, cp_cd);
				//stmt1.setString(3, truck_cd);
				stmt1.setString(3, gas_dt);
				rset1 = stmt1.executeQuery();
				while (rset1.next()) 
				{
					index++;
					
					VNOM_REV_NO.add(rset1.getString(1) == null ? "" : rset1.getString(1));
					VGEN_TIME.add(rset1.getString(2) == null ? "" : rset1.getString(2));
					VBASE.add(rset1.getString(3) == null ? "" : rset1.getString(3));
					VGCV.add(rset1.getString(4) == null ? "9802.80" : rset1.getString(4));
					VNCV.add(rset1.getString(5) == null ? "8831.35" : rset1.getString(5));
					VQTY_MMBTU.add(nf.format(rset1.getDouble(6)));
					VQTY_SCM.add(nf.format(rset1.getDouble(7)));
					VGEN_DT.add(rset1.getString(8) == null ? "" : rset1.getString(8));

					String truck_cd = rset1.getString(17) == null ? "" : rset1.getString(17);
					String trans_cd = rset1.getString(18) == null ? "" : rset1.getString(18);
					String cargo_no = rset1.getString(19) == null ? "" : rset1.getString(19);
					
					VTRANSPORTER_TRUCK.add(truck_cd);
					VTRANSPORTER_TRUCK_NO.add(""+utilBean_DLNG.getTruckRegNo(conn, truck_cd));
					VTRANSPORTER_CD.add(trans_cd);
					VTRANSPORTER_ABBR.add(utilBean_DLNG.getTruckTransABBR(conn, trans_cd));
					
					String countpty_cd = rset1.getString(9) == null ? "" : rset1.getString(9);
					String plant_seq = rset1.getString(10) == null ? "" : rset1.getString(10);
					String bu_plant_seq = rset1.getString(11) == null ? "" : rset1.getString(11);

					String bu_plant_abbr = utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B");
					String plant_abbr = utilBean.getCounterpartyPlantABBR(conn,countpty_cd, comp_cd, plant_seq, "C");

					String agmt = rset1.getString(12) == null ? "" : rset1.getString(12);
					String agmt_rev = rset1.getString(13) == null ? "" : rset1.getString(13);
					String cont = rset1.getString(14) == null ? "" : rset1.getString(14);
					String cont_rev = rset1.getString(15) == null ? "" : rset1.getString(15);
					String cont_type = rset1.getString(16) == null ? "" : rset1.getString(16);

					String dis_cont_mapping = utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, cargo_no);
					
					//VCOUNTERPARTY_CD.add(countpty_cd);
					//VCOUNTERPARTY_ABBR.add("" + utilBean.getCounterpartyABBR(conn,countpty_cd));
					VCOUNTERPARTY_PLANT_SEQ.add(plant_seq);
					VCOUNTERPARTY_PLANT_ABBR.add(plant_abbr);
					VBU_CD.add(comp_cd);
					VBU_PLANT_SEQ.add(bu_plant_seq);
					VBU_PLANT_ABBR.add(bu_plant_abbr);
					VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_type));
					VDIS_CONT_MAPPING.add(dis_cont_mapping);

					String dcq = "";
					String cont_ref = "";
					
					String queryString2 = "SELECT CONT_REF_NO,TRADE_REF_NO,DCQ " 
							+ "FROM FMS_SUPPLY_CONT_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND CONT_REV=? AND CONTRACT_TYPE=? " 
							+ "AND AGMT_NO=? AND AGMT_REV=? ";
					queryString2+="UNION ";
					queryString2+="SELECT CARGO_REF,NULL,CSOC_QTY "
							+ "FROM FMS_LTCORA_CONT_CARGO_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND BUY_SALE=? AND AGMT_TYPE=? "
							+ "AND A.CONT_REV=? AND CONTRACT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND CARGO_NO=? ";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, countpty_cd);
					stmt2.setString(3, cont);
					stmt2.setString(4, cont_rev);
					stmt2.setString(5, cont_type);
					stmt2.setString(6, agmt);
					stmt2.setString(7, agmt_rev);
					stmt2.setString(8, comp_cd);
					stmt2.setString(9, countpty_cd);
					stmt2.setString(10, cont);
					stmt2.setString(11, "C");
					stmt2.setString(12, "A");
					stmt2.setString(13, cont_rev);
					stmt2.setString(14, cont_type);
					stmt2.setString(15, agmt);
					stmt2.setString(16, agmt_rev);
					stmt2.setString(17, cargo_no);
					rset2 = stmt2.executeQuery();
					if (rset2.next()) 
					{
						cont_ref = rset2.getString(1) == null ? "" : rset2.getString(1);
						String trade_ref = rset2.getString(2) == null ? "" : rset2.getString(2);
						if (cont_type.equals("W")) 
						{
							cont_ref = trade_ref;
						}
						dcq = nf.format(rset2.getDouble(3));
					}
					rset2.close();
					stmt2.close();

					String variable_dcq = utilBean.getContVariableDCQ(conn,comp_cd, countpty_cd, agmt, cont, cont_type,gas_dt);
					if(!cargo_no.equals("") && !cargo_no.equals("0"))
					{
						variable_dcq=utilBean.getCargoVariableCSOC(conn,comp_cd,countpty_cd,"C",agmt,cont,cont_type,cargo_no,gas_dt);
					}
					if (!variable_dcq.equals("")) 
					{
						dcq = variable_dcq;
					}

					VCONT_REF.add(cont_ref);
					VDCQ.add(dcq);

					String queryString3 = "SELECT NOM_REV_NO,QTY_MMBTU,QTY_MT " 
							+ "FROM FMS_DLNG_BUYER_NOM_DTL A "
							+ "WHERE CONT_NO=? AND AGMT_NO=? " 
							+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND TRUCK_TRANS_CD=? AND TRUCK_CD=? "
							+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? AND BU_SEQ=? "
							+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND CARGO_NO=? "
							+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_BUYER_NOM_DTL B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD "
							+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
							+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
					stmt3 = conn.prepareStatement(queryString3);
					stmt3.setString(1, cont);
					stmt3.setString(2, agmt);
					stmt3.setString(3, comp_cd);
					stmt3.setString(4, cp_cd);
					stmt3.setString(5, trans_cd);
					stmt3.setString(6, truck_cd);
					stmt3.setString(7, plant_seq);
					stmt3.setString(8, cont_type);
					stmt3.setString(9, bu_plant_seq);
					stmt3.setString(10, gas_dt);
					stmt3.setString(11, cargo_no);
					rset3 = stmt3.executeQuery();
					if (rset3.next()) 
					{
						VBUYER_NOM_REV_NO.add(rset3.getString(1) == null ? "" : rset3.getString(1));
						VBUYER_NOM.add(nf.format(rset3.getDouble(2)));
					}
					else
					{
						VBUYER_NOM_REV_NO.add("");
						VBUYER_NOM.add("");
					}
					rset3.close();
					stmt3.close();
				}
				rset1.close();
				stmt1.close();
				
				VINDEX.add(index);
			}
			rset0.close();
			stmt0.close();
		} 
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSellerRevCounterpartyList()
	{
		String function_nm="getSellerRevCounterpartyList()";
		try
		{
			int selCount=0;
			String queryString="SELECT DISTINCT(COUNTERPARTY_CD) "
					+ "FROM FMS_DLNG_SELLER_NOM_DTL A "
					+ "WHERE COMPANY_CD=? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(++selCount, comp_cd);
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
	
	public void getBuyerNominationRevisionWise() 
	{
		String function_nm = "getBuyerNominationRevisionWise()";
		try 
		{
			int sub_index = 0;
			String queryString1 = "SELECT NOM_REV_NO,GEN_TIME,BASE,GCV,NCV,QTY_MMBTU,QTY_MT,TO_CHAR(GEN_DT,'DD/MM/YYYY'),"
					+ "COUNTERPARTY_CD,PLANT_SEQ,BU_SEQ,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,CARGO_NO "
					+ "FROM FMS_DLNG_BUYER_NOM A " 
					+ "WHERE COMPANY_CD=? "
					+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "ORDER BY NOM_REV_NO";
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, gas_dt);
			rset1 = stmt1.executeQuery();
			while (rset1.next()) 
			{
				sub_index += 1;
				VNOM_REV_NO.add(rset1.getString(1) == null ? "" : rset1.getString(1));
				VGEN_TIME.add(rset1.getString(2) == null ? "" : rset1.getString(2));
				VBASE.add(rset1.getString(3) == null ? "" : rset1.getString(3));
				VGCV.add(rset1.getString(4) == null ? "9802.80" : rset1.getString(4));
				VNCV.add(rset1.getString(5) == null ? "8831.35" : rset1.getString(5));
				VQTY_MMBTU.add(nf.format(rset1.getDouble(6)));
				VQTY_SCM.add(nf.format(rset1.getDouble(7)));
				VGEN_DT.add(rset1.getString(8) == null ? "" : rset1.getString(8));

				String countpty_cd = rset1.getString(9) == null ? "" : rset1.getString(9);
				String plant_seq = rset1.getString(10) == null ? "" : rset1.getString(10);
				String bu_plant_seq = rset1.getString(11) == null ? "" : rset1.getString(11);

				String bu_plant_abbr = utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_plant_seq, "B");
				String plant_abbr = utilBean.getCounterpartyPlantABBR(conn,countpty_cd, comp_cd, plant_seq, "C");

				String agmt = rset1.getString(12) == null ? "" : rset1.getString(12);
				String agmt_rev = rset1.getString(13) == null ? "" : rset1.getString(13);
				String cont = rset1.getString(14) == null ? "" : rset1.getString(14);
				String cont_rev = rset1.getString(15) == null ? "" : rset1.getString(15);
				String cont_type = rset1.getString(16) == null ? "" : rset1.getString(16);
				String cargo_no = rset1.getString(17)==null?"":rset1.getString(17);		//PB20260120
				
				String dis_cont_mapping = utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, cargo_no);

				VCOUNTERPARTY_CD.add(countpty_cd);
				VCOUNTERPARTY_ABBR.add("" + utilBean.getCounterpartyABBR(conn,countpty_cd));
				VCOUNTERPARTY_NM.add("" + utilBean.getCounterpartyName(conn,countpty_cd));
				VCOUNTERPARTY_PLANT_SEQ.add(plant_seq);
				VCOUNTERPARTY_PLANT_ABBR.add(plant_abbr);
				VBU_CD.add(comp_cd);
				VBU_PLANT_SEQ.add(bu_plant_seq);
				VBU_PLANT_ABBR.add(bu_plant_abbr);
				VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_type));
				VDIS_CONT_MAPPING.add(dis_cont_mapping);

				String dcq = "";
				String cont_ref = "";
				String queryString2 = "SELECT CONT_REF_NO,TRADE_REF_NO,DCQ " 
						+ "FROM FMS_SUPPLY_CONT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV=? AND CONTRACT_TYPE=? " 
						+ "AND AGMT_NO=? AND AGMT_REV=? ";
				queryString2+="UNION ";
				queryString2+="SELECT CARGO_REF,NULL,CSOC_QTY "
						+ "FROM FMS_LTCORA_CONT_CARGO_DTL A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND BUY_SALE=? AND AGMT_TYPE=? "
						+ "AND A.CONT_REV=? AND CONTRACT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND CARGO_NO=? ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, countpty_cd);
				stmt2.setString(3, cont);
				stmt2.setString(4, cont_rev);
				stmt2.setString(5, cont_type);
				stmt2.setString(6, agmt);
				stmt2.setString(7, agmt_rev);
				stmt2.setString(8, comp_cd);
				stmt2.setString(9, countpty_cd);
				stmt2.setString(10, cont);
				stmt2.setString(11, "C");
				stmt2.setString(12, "A");
				stmt2.setString(13, cont_rev);
				stmt2.setString(14, cont_type);
				stmt2.setString(15, agmt);
				stmt2.setString(16, agmt_rev);
				stmt2.setString(17, cargo_no);
				rset2 = stmt2.executeQuery();
				if(rset2.next()) 
				{
					cont_ref = rset2.getString(1) == null ? "" : rset2.getString(1);
					String trade_ref = rset2.getString(2) == null ? "" : rset2.getString(2);
					if (cont_type.equals("W")) 
					{
						cont_ref = trade_ref;
					}
					dcq = nf.format(rset2.getDouble(3));
				}
				rset2.close();
				stmt2.close();

				String variable_dcq = utilBean.getContVariableDCQ(conn,comp_cd, countpty_cd, agmt, cont, cont_type,gas_dt);
				if(!cargo_no.equals("") && !cargo_no.equals("0"))
				{
					variable_dcq=utilBean.getCargoVariableCSOC(conn,comp_cd,countpty_cd,"C",agmt,cont,cont_type,cargo_no,gas_dt);
				}
				if (!variable_dcq.equals("")) 
				{
					dcq = variable_dcq;
				}
				VCONT_REF.add(cont_ref);
				VDCQ.add(dcq);
			}
			VSUB_INDEX.add(sub_index);
			rset1.close();
			stmt1.close();
		} 
		catch (Exception e) 
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
	
	public void getHeaderSegment()
	{
		String function_nm="getHeaderSegment()";

		try
		{
			VHEADER_SEGMENT.add("DLNG");
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
				if(VHEADER_SEGMENT.elementAt(i).equals("DLNG"))
				{
					VSEGMENT.add("DLNG Supply Notice");
					VSEGMENT.add("DLNG Letter of Agreement");
					VSEGMENT.add("DLNG IGX");
					
					VSEGMENT_TYPE.add("F");
					VSEGMENT_TYPE.add("E");
					VSEGMENT_TYPE.add("W");
					segment_index=3;
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
			VSEGMENT.add("DLNG Supply Notice");
			VSEGMENT.add("DLNG Letter of Agreement");
			VSEGMENT.add("DLNG IGX");
			VSEGMENT.add("LTCORA(sell)");
			
			VSEGMENT_TYPE.add("F");
			VSEGMENT_TYPE.add("E");
			VSEGMENT_TYPE.add("W");
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
				if(segmentType.equals("F"))
				{
					VTEMP_SEGMENT.add("DLNG Supply Notice");
				}
				else if(segmentType.equals("E"))
				{
					VTEMP_SEGMENT.add("DLNG Letter of Agreement");
				}
				else if(segmentType.equals("W"))
				{
					VTEMP_SEGMENT.add("DLNG IGX");
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
					String dcq = rset.getString(22)==null?"": nf.format(rset.getDouble(22));
					String cont_type=""+VTEMP_SEGMENT_TYPE.elementAt(i);
					
					if(cont_type.equals("W"))
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
					VCONT_STATUS.add(utilBean.ContStatusName(cont_status_flg));
					
					total_dcq+=dcq.equals("")?0:Double.parseDouble(dcq);
					VDCQ.add(dcq);
					
					double suppliedQty=0;
					String min_dt="";
					String max_dt="";
					String query="SELECT SUM(QTY_MMBTU) "
		  					+ "FROM FMS_DLNG_ALLOC_MST A  "
		  					+ "WHERE CONT_NO=? AND AGMT_NO=?  "
		  					+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=?"
		  					+ "AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY')"		//less than to date 
		  					//+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY')"		//greater than from date 
		  					+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B  "
		  					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO  "
		  					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD  "
		  					+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD AND B.BU_SEQ=A.BU_SEQ  "
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
							+ "FROM FMS_DLNG_ALLOC_MST A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
							+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ=B.PLANT_SEQ AND A.TRUCK_TRANS_CD=B.TRUCK_TRANS_CD "
							+ "AND A.TRUCK_CD=B.TRUCK_CD AND A.GAS_DT=B.GAS_DT AND B.CARGO_NO=A.CARGO_NO)";
					stmt_temp = conn.prepareStatement(queryString);
					stmt_temp.setString(1, companyCd);
					stmt_temp.setString(2, countpty_cd);
					stmt_temp.setString(3, agmt);
					stmt_temp.setString(4, cont);
					stmt_temp.setString(5, cont_type);
					rset_temp=stmt_temp.executeQuery();
					if(rset_temp.next())
					{
						min_dt=rset_temp.getString(2)==null?"":rset_temp.getString(2);
						max_dt=rset_temp.getString(3)==null?"":rset_temp.getString(3);
					}
					rset_temp.close();
					stmt_temp.close();
					
					int st_count=0;
					query="SELECT SUM(QTY_MMBTU) "
			  				+ "FROM FMS_DLNG_ALLOC_MST A "
			  				+ "WHERE CONT_NO=? AND AGMT_NO=? "
							+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONTRACT_TYPE=? "
							+ "AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_ALLOC_MST B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD "
							+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
							+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
					stmt_temp = conn.prepareStatement(query);
					stmt_temp.setString(++st_count, cont);
					stmt_temp.setString(++st_count, agmt);
					stmt_temp.setString(++st_count, companyCd);
					stmt_temp.setString(++st_count, countpty_cd);
					stmt_temp.setString(++st_count, cont_type);
					stmt_temp.setString(++st_count, to_dt);
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
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getTerminalReportDtl()
	{
		String function_nm="getTerminalReportDtl()";
		try
		{
			int selCont=0;
			
			String queryString5="SELECT TO_CHAR(GAS_DT,'DD/MM/YYYY'),COUNTERPARTY_CD,PLANT_SEQ,NOM_REV_NO,"
					+ "QTY_MMBTU,QTY_MT,TRUCK_TRANS_CD,ARRIVAL_TIME,SLOT_END_TIME,TO_CHAR(ARRIVAL_DT,'DD/MM/YYYY'),"
					+ "REMARK,TRUCK_CD,CONT_NO,AGMT_NO,CONTRACT_TYPE,BU_SEQ,COMPANY_CD,FILL_STATION_CD,BAY_CD,SLOT_START_TIME,CARGO_NO "
					+ "FROM FMS_DLNG_SELLER_NOM_DTL A "
					+ "WHERE COMPANY_CD=? ";
			if(!fill_station.equals("0")) 
			{
				queryString5+= "AND FILL_STATION_CD=? ";
			}
			queryString5+= "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
					//+ "AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_SELLER_NOM_DTL B "
					+ " WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "	AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "	AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE "
					+ " AND B.GAS_DT=A.GAS_DT AND B.BU_SEQ =A.BU_SEQ AND B.CARGO_NO=A.CARGO_NO "
					//+ " AND B.FILL_STATION_CD=A.FILL_STATION_CD AND B.BAY_CD=A.BAY_CD"
					//+ " AND B.SLOT_START_TIME=A.SLOT_START_TIME AND B.SLOT_END_TIME=A.SLOT_END_TIME"
					//+ "	AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD "
					+ ") "
					+ "ORDER BY ARRIVAL_DT,ARRIVAL_TIME ASC";
			stmt5 = conn.prepareStatement(queryString5);
			stmt5.setString(++selCont, comp_cd);
			if(!fill_station.equals("0"))
			{
				stmt5.setString(++selCont, fill_station);
			}
			stmt5.setString(++selCont, gas_dt);
			//stmt5.setString(++selCont, to_dt);
			rset5=stmt5.executeQuery();
			while(rset5.next())
			{
				String gas_dt = rset5.getString(1)==null?"":rset5.getString(1);
				String counterparty_cd = rset5.getString(2)==null?"":rset5.getString(2);
				String plant_seq = rset5.getString(3)==null?"":rset5.getString(3);
				String max_nom_rev_no = rset5.getString(4)==null?"":rset5.getString(4);
				String qty_mmbtu = rset5.getString(5)==null?"":rset5.getString(5);
				String qty_mt = rset5.getString(6)==null?"":rset5.getString(6);
				String truck_trans_cd = rset5.getString(7)==null?"":rset5.getString(7);
				String arrival_time = rset5.getString(8)==null?"":rset5.getString(8);
				String arrival_dt = rset5.getString(10)==null?"":rset5.getString(10);
				String remark = rset5.getString(11)==null?"":rset5.getString(11);
				String truck_cd = rset5.getString(12)==null?"":rset5.getString(12);

				String cont = rset5.getString(13)==null?"":rset5.getString(13);
				String agmt = rset5.getString(14)==null?"":rset5.getString(14);
				String cont_type = rset5.getString(15)==null?"":rset5.getString(15);
				String bu_plant_seq = rset5.getString(16)==null?"":rset5.getString(16);
				String bu_cd = rset5.getString(17)==null?"":rset5.getString(17);

				String fillSt_cd = rset5.getString(18)==null?"":rset5.getString(18);
				String fillst_abbr = utilBean_DLNG.getFillStationABBR(conn, fillSt_cd);
				String bay_cd = rset5.getString(19)==null?"":rset5.getString(19);
				String cargo_no = rset5.getString(21)==null?"":rset5.getString(21);
				String bay_nm ="";
				
				String queryString6="SELECT BAY_CD,BAY_NAME,SLOT_START_TIME,SLOT_INTERVAL "
						+ "FROM FMS_BAY_SLOT_MST A "
						+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_BAY_SLOT_MST B "
						+ "WHERE A.FILL_STATION_CD=B.FILL_STATION_CD "
						+ "AND A.BAY_CD=B.BAY_CD "
						+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) "
						+ "AND FILL_STATION_CD=? AND BAY_CD=?";
				stmt2 = conn.prepareStatement(queryString6);
				stmt2.setString(1, fillSt_cd);
				stmt2.setString(2, bay_cd);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					String bayCd = rset2.getString(1)==null?"":rset2.getString(1);
					String bayName = rset2.getString(2)==null?"":rset2.getString(2);
					String baySlotStTime = rset2.getString(3)==null?"":rset2.getString(3);
					String baySlotIntrvl = rset2.getString(4)==null?"":rset2.getString(4);
					
					bay_nm = bayName;
				}
				rset2.close();
				stmt2.close();
				
				String slot_start_time = rset5.getString(20)==null?"":rset5.getString(20);
				String slot_end_time = rset5.getString(9)==null?"":rset5.getString(9);
				
				String bu_seq_nm = utilBean.getCounterpartyPlantName(conn, bu_cd, comp_cd, bu_plant_seq, "B");
				String bu_seq_abbr = utilBean.getCounterpartyPlantABBR(conn,bu_cd, comp_cd, bu_plant_seq, "B");

				String plant_seq_nm = utilBean.getCounterpartyPlantName(conn, counterparty_cd, comp_cd, plant_seq, "C");
				String plant_seq_abbr = utilBean.getCounterpartyPlantName(conn, counterparty_cd, comp_cd, plant_seq, "C");
				
				VGAS_DT.add(gas_dt);
				VCOUNTERPARTY_CD.add(counterparty_cd);
				String cpName=utilBean.getCounterpartyName(conn, counterparty_cd);
				if(cont_type.equals("O") || cont_type.equals("Q")) {
					cpName += " (TP)";
				}
				VCOUNTERPARTY_NM.add(cpName);
				VCOUNTERPARTY_ABBR.add(utilBean.getCounterpartyABBR(conn, counterparty_cd));
				VCOUNTERPARTY_PLANT_SEQ.add(plant_seq);
				VCOUNTERPARTY_PLANT_NM.add(plant_seq_nm);
				VCOUNTERPARTY_PLANT_ABBR.add(plant_seq_abbr);
				VNOM_REV_NO.add(max_nom_rev_no);
				VQTY_MMBTU.add(qty_mmbtu);
				VQTY_MT.add(qty_mt);
				VTRUCK_TRANS_CD.add(truck_trans_cd);
				VTRANSPORTER_ABBR.add(utilBean_DLNG.getTruckTransABBR(conn, truck_trans_cd));
				VTRANSPORTER_NM.add(utilBean_DLNG.getTruckTransName(conn, truck_trans_cd));
				VTRUCK_CD.add(truck_cd);
				VTRUCK_REG_NUM.add(utilBean_DLNG.getTruckRegNo(conn, truck_cd));
				VSLOT_START_TIME.add(arrival_time);
				//VSLOT_END_TIME.add(slot_end_time);
				VARRIVAL_DT.add(arrival_dt);
				VREMARK.add(remark);
				VBU_PLANT_SEQ.add(bu_plant_seq);
				VBU_PLANT_ABBR.add(bu_seq_abbr);
				VFILLING_ST_ABBR.add(fillst_abbr);
				VBAY_NM.add(bay_nm);
				VSLOT_DLT.add(slot_start_time+" - "+slot_end_time+" ("+arrival_dt+")");
				VDIS_CONT_MAPPING.add(utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt, "", cont, "", cont_type, cargo_no));
				
				//HM20260108 : SELECTED DRIVER DETAIL As per Live issue
				queryString4="SELECT DRIVER_CD,LINK_SEQ "
						+ "FROM (SELECT A.DRIVER_CD, A.LINK_SEQ, ROW_NUMBER()  "
						+ " OVER ( PARTITION BY A.TRUCK_CD ORDER BY A.EFF_DT DESC, A.LINK_SEQ DESC) RN "
						+ " FROM FMS_TRUCK_DRIVER_LINK A "
						+ " WHERE A.TRUCK_CD = ? "
						+ " AND A.EFF_DT <= TO_DATE(?, 'DD/MM/YYYY') "
						+ " AND (A.RELEASE_DT IS NULL "
						+ " OR A.RELEASE_DT >= TO_DATE(?, 'DD/MM/YYYY'))) "
						+ " WHERE RN = 1";
				
				/*AP : SELECTED DRIVER DETAIL
				 * queryString4="SELECT DRIVER_CD "
						+ "FROM FMS_TRUCK_DRIVER_LINK A "
						+ "WHERE TRUCK_CD=? "
						+ "AND LINK_SEQ=(SELECT MAX(LINK_SEQ) FROM FMS_TRUCK_DRIVER_LINK B "
						+ "WHERE A.TRUCK_CD=B.TRUCK_CD AND (RELEASE_DT>=TO_DATE(?,'DD/MM/YYYY') OR RELEASE_DT IS NULL) "
						+ "AND EFF_DT<=TO_DATE(?,'DD/MM/YYYY') "
						+ ") ";*/
				//HM20251216 : To fix the Link Seq and Eff. Date issue raised on BUG 2510090
						/*+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_TRUCK_DRIVER_LINK B WHERE A.TRUCK_CD=B.TRUCK_CD "
						+ "AND A.LINK_SEQ=B.LINK_SEQ AND A.DRIVER_CD=B.DRIVER_CD AND B.EFF_DT <= TO_DATE(?,'DD/MM/YYYY')) "
						+ "AND LINK_SEQ=(SELECT MAX(LINK_SEQ) FROM FMS_TRUCK_DRIVER_LINK B WHERE A.DRIVER_CD=B.DRIVER_CD) ";*/
				stmt4=conn.prepareStatement(queryString4);
				stmt4.setString(1, truck_cd);
				stmt4.setString(2, gas_dt);
				stmt4.setString(3, gas_dt);
				rset4 = stmt4.executeQuery();
				if(rset4.next())
				{
					String assigned_driver  = rset4.getString(1)==null?"":rset4.getString(1);
					
					String assigned_driver_nm = utilBean_DLNG.getDriverName(conn, assigned_driver);
					VDRIVER_NM.add(assigned_driver_nm);
				}
				else
				{
					VDRIVER_NM.add("");
				}
				rset4.close();
				stmt4.close();
				
				String queryString3 = "SELECT COMPANY_CD,COUNTERPARTY_CD,ENTITY_TYPE,PLANT_SEQ_NO,CHKPOST_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY') "
						+ "FROM FMS_LINK_CHECKPOST_PLANT A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY_TYPE=? "
						+ "AND PLANT_SEQ_NO=? "
						+ "AND (TO_DATE(A.RELEASE_DT)>TO_DATE(?,'DD/MM/YYYY') OR A.RELEASE_DT IS NULL) "
						+ "AND A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_LINK_CHECKPOST_PLANT B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY_TYPE=B.ENTITY_TYPE "
						+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.CHKPOST_CD=B.CHKPOST_CD) "
						+ "AND A.REV_SEQ=(SELECT MAX(B.REV_SEQ) FROM FMS_LINK_CHECKPOST_PLANT B WHERE "
						+ "	A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY_TYPE=B.ENTITY_TYPE "
						+ "	AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.EFF_DT=B.EFF_DT)";
				stmt3=conn.prepareStatement(queryString3);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, counterparty_cd);
				stmt3.setString(3, "C");
				stmt3.setString(4, plant_seq);
				stmt3.setString(5, gas_dt);
				rset3 = stmt3.executeQuery();
				if(rset3.next())
				{
					String checkpost_cd = rset3.getString(5)==null?"":rset3.getString(5);
					
					VCHECKPOST_NM.add(utilBean_DLNG.getCheckPostName(conn,checkpost_cd));
				}
				else
				{
					VCHECKPOST_NM.add("");
				}
				rset3.close();
				stmt3.close();

				int selCont1=0;
				
				int index=0;
				
				String queryString2="SELECT TO_CHAR(GAS_DT,'DD/MM/YYYY'),COUNTERPARTY_CD,PLANT_SEQ,NOM_REV_NO,"
						+ "QTY_MMBTU,QTY_MT,TRUCK_TRANS_CD,ARRIVAL_TIME,SLOT_END_TIME,TO_CHAR(ARRIVAL_DT,'DD/MM/YYYY'),REMARK,TRUCK_CD,"
						+ "FILL_STATION_CD,BAY_CD "
						+ "FROM FMS_DLNG_SELLER_NOM_DTL A "
						+ "WHERE CONT_NO=? AND AGMT_NO=? "
						+ "AND COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND PLANT_SEQ=? AND CONTRACT_TYPE=? "
						+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND BU_SEQ=? "
						+ "AND TRUCK_TRANS_CD=? AND TRUCK_CD=? "
						+ "AND NOM_REV_NO != ? "
						+ "AND FILL_STATION_CD=? "
						+ "AND BAY_CD=? "
						+ "AND CARGO_NO=? "
						+ "ORDER BY NOM_REV_NO DESC";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, cont);
				stmt2.setString(2, agmt);
				stmt2.setString(3, comp_cd);
				stmt2.setString(4, counterparty_cd);
				stmt2.setString(5, plant_seq);
				stmt2.setString(6, cont_type);
				stmt2.setString(7, gas_dt);
				stmt2.setString(8, bu_plant_seq);
				stmt2.setString(9, truck_trans_cd);
				stmt2.setString(10, truck_cd);
				stmt2.setString(11, max_nom_rev_no);
				stmt2.setString(12, fillSt_cd);
				stmt2.setString(13, bay_cd);
				stmt2.setString(14, cargo_no);
				rset2=stmt2.executeQuery();
				while(rset2.next())
				{
					index+=1;
					
					String dtl_nom_rev_no = rset2.getString(4)==null?"":rset2.getString(4);
					String dtl_qty_mmbtu = rset2.getString(5)==null?"":rset2.getString(5);
					String dtl_qty_mt = rset2.getString(6)==null?"":rset2.getString(6);
					String dtl_truck_trans_cd = rset2.getString(7)==null?"":rset2.getString(7);
					String dtl_slot_start_time = rset2.getString(8)==null?"":rset2.getString(8);
					String dtl_slot_end_time = rset2.getString(9)==null?"":rset2.getString(9);
					String dtl_arrival_dt = rset2.getString(10)==null?"":rset2.getString(10);
					String dtl_remark = rset2.getString(11)==null?"":rset2.getString(11);
					String dtl_truck_cd = rset2.getString(12)==null?"":rset2.getString(12);
					
					String dtl_fillst_cd = rset2.getString(13)==null?"":rset2.getString(13);
					String dtl_fillst_abbr = utilBean_DLNG.getFillStationABBR(conn, dtl_fillst_cd);
					
					String dtl_bay_cd = rset2.getString(14)==null?"":rset2.getString(14);
					String dtl_bay_nm="";
					
					String queryString7="SELECT BAY_CD,BAY_NAME,SLOT_START_TIME,SLOT_INTERVAL "
							+ "FROM FMS_BAY_SLOT_MST A "
							+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_BAY_SLOT_MST B "
							+ "WHERE A.FILL_STATION_CD=B.FILL_STATION_CD "
							+ "AND A.BAY_CD=B.BAY_CD "
							+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) "
							+ "AND FILL_STATION_CD=? AND BAY_CD=?";
					stmt7 = conn.prepareStatement(queryString7);
					stmt7.setString(1, dtl_fillst_cd);
					stmt7.setString(2, dtl_bay_cd);
					rset7=stmt7.executeQuery();
					if(rset7.next())
					{
						String bayCd = rset7.getString(1)==null?"":rset7.getString(1);
						String bayName = rset7.getString(2)==null?"":rset7.getString(2);
						String baySlotStTime = rset7.getString(3)==null?"":rset7.getString(3);
						String baySlotIntrvl = rset7.getString(4)==null?"":rset7.getString(4);
						
						dtl_bay_nm = bayName;
					}
					rset7.close();
					stmt7.close();
					
					VDTL_NOM_REV_NO.add(dtl_nom_rev_no);
					VDTL_QTY_MMBTU.add(dtl_qty_mmbtu);
					VDTL_QTY_MT.add(dtl_qty_mt);
					VDTL_TRUCK_TRANS_CD.add(dtl_truck_trans_cd);
					VDTL_TRUCK_CD.add(dtl_truck_cd);
					VDTL_TRANSPORTER_ABBR.add(utilBean_DLNG.getTruckTransABBR(conn, dtl_truck_trans_cd));
					VDTL_TRANSPORTER_NM.add(utilBean_DLNG.getTruckTransName(conn, dtl_truck_trans_cd));
					VDTL_TRUCK_REG_NUM.add(utilBean_DLNG.getTruckRegNo(conn, dtl_truck_cd));
					VDTL_SLOT_START_TIME.add(dtl_slot_start_time);
					//VDTL_SLOT_END_TIME.add(dtl_slot_end_time);
					VDTL_ARRIVAL_DT.add(dtl_arrival_dt);
					VDTL_REMARK.add(dtl_remark);
					
					VDTL_FILLING_ST_ABBR.add(dtl_fillst_abbr);
					VDTL_BAY_NM.add(dtl_bay_nm);
					VDTL_SLOT_DLT.add(dtl_slot_start_time+" - "+dtl_slot_end_time+" ("+dtl_arrival_dt+")");
					
					//SELECTED DRIVER DETAIL
					queryString1="SELECT DRIVER_CD "
							+ "FROM FMS_TRUCK_DRIVER_LINK A "
							+ "WHERE TRUCK_CD=? "
							+ "AND EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_TRUCK_DRIVER_LINK B WHERE A.TRUCK_CD=B.TRUCK_CD "
							+ "AND A.LINK_SEQ=B.LINK_SEQ AND A.DRIVER_CD=B.DRIVER_CD AND B.EFF_DT <= TO_DATE(?,'DD/MM/YYYY')) "
							+ "AND LINK_SEQ=(SELECT MAX(LINK_SEQ) FROM FMS_TRUCK_DRIVER_LINK B WHERE A.DRIVER_CD=B.DRIVER_CD) ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, truck_cd);
					stmt1.setString(2, gas_dt);
					rset1 = stmt1.executeQuery();
					if(rset1.next())
					{
						String assigned_driver  = rset1.getString(1)==null?"":rset1.getString(1);
						
						String assigned_driver_nm = utilBean_DLNG.getDriverName(conn, assigned_driver);
						VDTL_DRIVER_NM.add(assigned_driver_nm);
					}
					else
					{
						VDTL_DRIVER_NM.add("");
					}
					rset1.close();
					stmt1.close();
					
					String queryString0 = "SELECT COMPANY_CD,COUNTERPARTY_CD,ENTITY_TYPE,PLANT_SEQ_NO,CHKPOST_CD,TO_CHAR(EFF_DT,'DD/MM/YYYY') "
							+ "FROM FMS_LINK_CHECKPOST_PLANT A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND ENTITY_TYPE=? "
							+ "AND PLANT_SEQ_NO=? "
							+ "AND (TO_DATE(A.RELEASE_DT)>TO_DATE(?,'DD/MM/YYYY') OR A.RELEASE_DT IS NULL) "
							+ "AND A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_LINK_CHECKPOST_PLANT B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY_TYPE=B.ENTITY_TYPE "
							+ "AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.CHKPOST_CD=B.CHKPOST_CD) "
							+ "AND A.REV_SEQ=(SELECT MAX(B.REV_SEQ) FROM FMS_LINK_CHECKPOST_PLANT B WHERE "
							+ "	A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY_TYPE=B.ENTITY_TYPE "
							+ "	AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO AND A.EFF_DT=B.EFF_DT)";
					stmt3=conn.prepareStatement(queryString0);
					stmt3.setString(1, comp_cd);
					stmt3.setString(2, counterparty_cd);
					stmt3.setString(3, "C");
					stmt3.setString(4, plant_seq);
					stmt3.setString(5, gas_dt);
					rset3 = stmt3.executeQuery();
					if(rset3.next())
					{
						String checkpost_cd = rset3.getString(5)==null?"":rset3.getString(5);
						
						VDTL_CHECKPOST_NM.add(utilBean_DLNG.getCheckPostName(conn,checkpost_cd));
					}
					else
					{
						VDTL_CHECKPOST_NM.add("");
					}
					rset3.close();
					stmt3.close();
				}
				rset2.close();
				stmt2.close();
				
				VINDEX.add(index);
			}
			rset5.close();
			stmt5.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getFillStMasterForTerminalRpt() 
	{
		String function_nm = "getFillStMasterForTerminalRpt()";
		try 
		{
			String queryString = "SELECT DISTINCT FILL_STATION_CD " 
					+ "FROM FMS_DLNG_SELLER_NOM_DTL A "
					+ "WHERE COMPANY_CD=? " 
					+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) FROM FMS_DLNG_SELLER_NOM_DTL B "
					+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
					+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
					+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD "
					+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
					+ "AND B.GAS_DT=A.GAS_DT AND B.CARGO_NO=A.CARGO_NO) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, gas_dt);
			rset = stmt.executeQuery();
			while (rset.next()) 
			{
				String fillSt_cd=rset.getString(1) == null ?"":rset.getString(1);
				String fillSt_abbr=""+utilBean_DLNG.getFillStationABBR(conn, fillSt_cd);
				String fillSt_nm=""+utilBean_DLNG.getFillStationName(conn, fillSt_cd);

				VMST_FILLST_CD.add(fillSt_cd);
				VMST_FILLST_ABBR.add(fillSt_abbr);
				VMST_FILLST_NM.add(fillSt_nm);
			}
			rset.close();
			stmt.close();
		} 
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
		
	// SagarB20250922 Added this function for showing BU list in DLNG-> DLNG Nomination vs Truck Loading
	public void getBuMstFromBuyerNom() {

		String function_nm = "getBuMstFromBuyerNom()";
		try 
		{
			int count = 0;
			String queryString1 = "SELECT DISTINCT BU_SEQ " 
					+ "FROM FMS_DLNG_BUYER_NOM "
					+ "WHERE COMPANY_CD=? "
					+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') ";
			if (!counterparty_cd.equals("") && !counterparty_cd.equals("0")) 
			{
				queryString1 += "AND COUNTERPARTY_CD=? ";
			}
			stmt1 = conn.prepareStatement(queryString1);
			stmt1.setString(++count, comp_cd);
			stmt1.setString(++count, from_dt);
			stmt1.setString(++count, to_dt);
			if (!counterparty_cd.equals("") && !counterparty_cd.equals("0")) 
			{
				stmt1.setString(++count, counterparty_cd);
			}
			rset1 = stmt1.executeQuery();
			while (rset1.next()) 
			{
				String bu_seq = rset1.getString(1) == null ? "" : rset1.getString(1);
				VBU_PLANT_SEQ.add(bu_seq);
				VBu_Plant_Abbr_List.add("" + utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_seq, "B"));
			}
			rset1.close();
			stmt1.close();
		} 
		catch (Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
		public void getBUList() //RG20250926 For adding Bu wise filter
	{

		String function_nm = "getBUList()";
		try 
		{
			//RG20250922 For adding Bu wise filter
			int count=0;
			String queryString_bu = "SELECT DISTINCT BU_SEQ " 
					+ "FROM FMS_DLNG_ALLOC_MST A "
					+ "WHERE COMPANY_CD=? " 
					+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') ";
			if(!counterparty_cd.equals("0") && (!counterparty_cd.equals(""))) {
				queryString_bu +=" AND COUNTERPARTY_CD=? ";
				}
			stmt = conn.prepareStatement(queryString_bu);
			stmt.setString(++count, comp_cd);
			stmt.setString(++count, from_dt);
			stmt.setString(++count, to_dt);
			if(!counterparty_cd.equals("0") && (!counterparty_cd.equals(""))) {
			stmt.setString(++count, counterparty_cd);
				}
			rset = stmt.executeQuery();
			while (rset.next()) 
			{
				String bu_cd = rset.getString(1) == null ? "" : rset.getString(1);
				VBU_PLANT_SEQ.add(bu_cd);
				VBU_PLANT_ABBR.add("" + utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_cd, "B"));
			}
			rset.close();
			stmt.close();
			//
			
		} 
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}	
	}
	
	public void getContractMap() 
	{
		String function_nm = "getContractMap()";
		try 
		{
			 String companyCd ="",countpty_cd="",agmt="",agmt_rev="",cont="",cont_type="",cont_rev="";
			 String deal_map = "",cont_ref="",cargo_no="";
			 queryString ="SELECT DISTINCT A.COMPANY_CD,A.COUNTERPARTY_CD,A.AGMT_NO, "
					+ "A.CONT_NO,A.CONTRACT_TYPE,A.CARGO_NO "
					+ "FROM FMS_DLNG_ALLOC_MST A "
					+ "WHERE A.COMPANY_CD = ? AND A.GAS_DT >= TO_DATE(?, 'DD/MM/YYYY') "
					+ "AND A.GAS_DT <= TO_DATE(?, 'DD/MM/YYYY') AND A.COUNTERPARTY_CD = ? "
					+ "AND A.NOM_REV_NO = ( "
					+ "SELECT MAX(B.NOM_REV_NO) "
					+ "FROM FMS_DLNG_ALLOC_MST B "
					+ "WHERE B.COMPANY_CD = A.COMPANY_CD "
					+ "AND B.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
					+ "AND B.AGMT_NO = A.AGMT_NO "
					+ "AND B.CONT_NO = A.CONT_NO AND B.PLANT_SEQ = A.PLANT_SEQ "
					+ "AND B.TRUCK_TRANS_CD = A.TRUCK_TRANS_CD AND B.TRUCK_CD = A.TRUCK_CD "
					+ "AND B.BU_SEQ = A.BU_SEQ AND B.GAS_DT = A.GAS_DT "
					+ "AND B.CONTRACT_TYPE = A.CONTRACT_TYPE) ORDER BY A.CONT_NO";
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
						 cont = rset.getString(4) == null ? "" : rset.getString(4);
						 cont_type = rset.getString(5) == null ? "" : rset.getString(5);
						 cargo_no = rset.getString(6) == null ? "" : rset.getString(6);
	
						VAGMT_NO.add(agmt);
						VCONT_NO.add(cont);
						VCONTRACT_TYPE.add(cont_type);
						VCARGO_NO.add(cargo_no);
						
						if(!cont_type.equals("Q") && (!cont_type.equals("O")))
						{
						String queryString1 = "SELECT A.CONT_REF_NO,A.SPEC_GAS_ENERGY_BASE "
								+ "FROM FMS_SUPPLY_CONT_MST A "
								+ "WHERE A.COMPANY_CD = ? AND A.COUNTERPARTY_CD = ? "
								+ "AND A.AGMT_NO = ? "
								+ "AND A.CONT_NO = ? AND A.CONTRACT_TYPE = ? "
								+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) "
								+ "FROM FMS_SUPPLY_CONT_MST B "
								+ "WHERE B.COMPANY_CD = A.COMPANY_CD "
								+ "AND B.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
								+ "AND B.AGMT_NO = A.AGMT_NO "
								+ "AND B.CONT_NO  = A.CONT_NO "
								+ "AND B.CONTRACT_TYPE = A.CONTRACT_TYPE) ORDER BY A.CONT_NO";
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, countpty_cd);
						stmt1.setString(3, agmt);
						stmt1.setString(4, cont);
						stmt1.setString(5, cont_type);
						rset1 = stmt1.executeQuery();
						if(rset1.next())
						{
							cont_ref = rset1.getString(1)==null?"":rset1.getString(1);
							VCONT_REF.add(cont_ref);
							VDIS_CONT_MAPPING.add(utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, "", cont, "", cont_type, cargo_no));
							VGCV_NCV.add(rset1.getString(2) == null ? "" : rset1.getString(2));
						}
						rset1.close();
						stmt1.close();
					  }
					  else
					  {
						  String queryString1 = "SELECT A.CARGO_REF,B.SPEC_GAS_ENERGY_BASE "
									+ "FROM FMS_LTCORA_CONT_CARGO_DTL A, FMS_LTCORA_CONT_MST B "
									+ "WHERE A.COMPANY_CD = ? AND A.COUNTERPARTY_CD = ? "
									+ "AND A.AGMT_NO = ? "
									+ "AND A.CONT_NO = ? AND A.CONTRACT_TYPE = ? "
									+ "AND A.CARGO_NO = ? "
									+ "AND B.COMPANY_CD = A.COMPANY_CD "
									+ "AND B.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
									+ "AND B.AGMT_NO = A.AGMT_NO "
									+ "AND B.AGMT_REV = A.AGMT_REV "
									+ "AND B.CONT_NO = A.CONT_NO "
									+ "AND B.CONTRACT_TYPE = A.CONTRACT_TYPE "
									+ "AND B.CONT_REV = A.CONT_REV "
									+ "AND A.CONT_REV = (SELECT MAX(C.CONT_REV) "
									+ "FROM FMS_LTCORA_CONT_MST C "
									+ "WHERE C.COMPANY_CD = A.COMPANY_CD "
									+ "AND C.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
									+ "AND C.AGMT_NO = A.AGMT_NO "
									+ "AND C.AGMT_REV = A.AGMT_REV "
									+ "AND C.CONT_NO  = A.CONT_NO "
									+ "AND C.CONTRACT_TYPE = A.CONTRACT_TYPE) ";
								stmt1 = conn.prepareStatement(queryString1);
								stmt1.setString(1, comp_cd);
								stmt1.setString(2, countpty_cd);
								stmt1.setString(3, agmt);
								stmt1.setString(4, cont);
								stmt1.setString(5, cont_type);
								stmt1.setString(6, cargo_no);
								rset1 = stmt1.executeQuery();
								if(rset1.next())
								{
									cont_ref = rset1.getString(1)==null?"":rset1.getString(1);
									VCONT_REF.add(cont_ref);
									VGCV_NCV.add(rset1.getString(2)==null?"":rset1.getString(2));
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
				if(!contract_type.equals("Q") && (!contract_type.equals("O")))
				{
				String queryString1 = "SELECT A.CONT_REF_NO "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE A.COMPANY_CD = ? AND A.COUNTERPARTY_CD = ? "
						+ "AND A.AGMT_NO = ? "
						+ "AND A.CONT_NO = ? AND A.CONTRACT_TYPE = ? "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) "
						+ "FROM FMS_SUPPLY_CONT_MST B "
						+ "WHERE B.COMPANY_CD = A.COMPANY_CD "
						+ "AND B.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
						+ "AND B.AGMT_NO = A.AGMT_NO "
						+ "AND B.CONT_NO  = A.CONT_NO "
						+ "AND B.CONTRACT_TYPE = A.CONTRACT_TYPE) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, agmt_no);
				stmt1.setString(4, cont_no);
				stmt1.setString(5, contract_type);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					cont_ref_no = rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
			    deal_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, "", cont_no, "", contract_type, "");
			}
			else
			{
				String queryString1 = "SELECT A.CARGO_REF "
						+ "FROM FMS_LTCORA_CONT_CARGO_DTL A "
						+ "WHERE A.COMPANY_CD = ? AND A.COUNTERPARTY_CD = ? "
						+ "AND A.AGMT_NO = ? "
						+ "AND A.CONT_NO = ? AND A.CONTRACT_TYPE = ? "
						+ "AND A.CARGO_NO = ? "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) "
						+ "FROM FMS_LTCORA_CONT_MST B "
						+ "WHERE B.COMPANY_CD = A.COMPANY_CD "
						+ "AND B.COUNTERPARTY_CD = A.COUNTERPARTY_CD "
						+ "AND B.AGMT_NO = A.AGMT_NO "
						+ "AND B.CONT_NO  = A.CONT_NO "
						+ "AND B.CONTRACT_TYPE = A.CONTRACT_TYPE) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, agmt_no);
				stmt1.setString(4, cont_no);
				stmt1.setString(5, contract_type);
				stmt1.setString(6, cargo_no);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					cont_ref_no = rset1.getString(1)==null?"":rset1.getString(1);
					deal_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, "", cont_no, "", contract_type, cargo_no);
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
		String function_nm = "getPlantName()";
		try
		{
			for(int i=0;i<VDIS_CONT_MAPPING.size();i++)
			{
				String queryString2 = "SELECT DISTINCT A.PLANT_SEQ,A.BASE " 
						+ "FROM FMS_DLNG_ALLOC_MST A "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD  = ? " 
						+ "AND A.AGMT_NO = ? "
						//+ "AND A.AGMT_REV = ?"
						+ " AND A.CONT_NO = ? AND A.CONTRACT_TYPE = ? "
						+ "AND A.GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND A.GAS_DT<=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND A.NOM_REV_NO=(SELECT MAX(B.NOM_REV_NO) "
						+ "FROM FMS_DLNG_ALLOC_MST B "
						+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
						+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
						+ "AND B.TRUCK_TRANS_CD=A.TRUCK_TRANS_CD AND B.TRUCK_CD=A.TRUCK_CD "
						+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
						+ "AND B.GAS_DT=A.GAS_DT) ";
				stmt2 = conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, counterparty_cd);
				if(deal_no.equals("0"))
				{
					stmt2.setString(3, ""+VAGMT_NO.elementAt(i));
					//stmt2.setString(4, ""+VAGMT_REV.elementAt(i));
					stmt2.setString(4, ""+VCONT_NO.elementAt(i));
					stmt2.setString(5, ""+VCONTRACT_TYPE.elementAt(i));
				}
				else
				{
					stmt2.setString(3, agmt_no);
					//stmt2.setString(4, agmt_rev);
					stmt2.setString(4, cont_no);
					stmt2.setString(5, contract_type);
				}
				stmt2.setString(6, from_dt);
				stmt2.setString(7, to_dt);
				rset2 = stmt2.executeQuery();
				while (rset2.next()) 
				{
					String plant_seq = rset2.getString(1) == null ? "" : rset2.getString(1);
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
				rset2.close();
				stmt2.close();
				VCOUNTERPARTY_PLANT_SEQ.add(VPLANT_SEQ_NO);
				VCOUNTERPARTY_PLANT_NM.add(VPLANT_NM);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name,function_nm,e);
		}
	}
	
	public void getCustomerAllocationData() 
	{
		String function_nm = "getCustomerAllocationData()";
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
					
					String	queryString1=" SELECT SUM(QTY_MMBTU) "
								+ "FROM FMS_DLNG_ALLOC_MST A " 
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') ";
						queryString1+= " AND AGMT_NO = ? AND CONT_NO = ? AND CONTRACT_TYPE= ? AND CARGO_NO = ? ";		
						if (!bu_plant_seq.equals("0") && !bu_plant_seq.equals("")) 
					    {
					        queryString1 += " AND BU_SEQ=? ";
					    }
					    if(!plant_seq.equals("0") && (!plant_seq.equals(""))) 
						{
					    queryString1+= " AND PLANT_SEQ = ? ";		
						}
					    queryString1+= " AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) "
					    		+ "FROM FMS_DLNG_ALLOC_MST B "
								+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
								+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
								+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
								+ "AND B.GAS_DT=A.GAS_DT)";
						if (!bu_plant_seq.equals("0") && !bu_plant_seq.equals("")) 
					    {
					        queryString1 += " AND BU_SEQ=? ";
					    }
					    if(!plant_seq.equals("0") && (!plant_seq.equals(""))) 
						{
					    queryString1+= " AND PLANT_SEQ = ? ";		
						}
						
						stmt1 = conn.prepareStatement(queryString1);
						stmt1.setString(++count, comp_cd);
						stmt1.setString(++count, counterparty_cd);
						stmt1.setString(++count, gas_dt);
						if(!deal_no.equals("0") && (!deal_no.equals(""))) 
						{
						  stmt1.setString(++count, agmt_no);		
						  //stmt1.setString(++count, agmt_rev);		
						  stmt1.setString(++count, cont_no);		
						  stmt1.setString(++count, contract_type);
						  stmt1.setString(++count, cargo_no);
						}
						else
						{
						  stmt1.setString(++count, ""+VAGMT_NO.elementAt(i));		
						  //stmt1.setString(++count, ""+VAGMT_REV.elementAt(i));		
						  stmt1.setString(++count, ""+VCONT_NO.elementAt(i));		
						  stmt1.setString(++count, ""+VCONTRACT_TYPE.elementAt(i));
						  stmt1.setString(++count, ""+VCARGO_NO.elementAt(i));
						}
						if(!bu_plant_seq.equals("0") && (!bu_plant_seq.equals(""))) 
						{
						  stmt1.setString(++count, bu_plant_seq);		
						}
						if(!plant_seq.equals("0") && (!plant_seq.equals(""))) 
						{
						  stmt1.setString(++count, plant_seq);		
						}
						
						if(!bu_plant_seq.equals("0") && (!bu_plant_seq.equals(""))) 
						{
						   stmt1.setString(++count, bu_plant_seq);		
						}
						if(!plant_seq.equals("0") && (!plant_seq.equals(""))) 
						{
						  stmt1.setString(++count, plant_seq);		
						}
						rset1 = stmt1.executeQuery();
						while (rset1.next()) 
						{
							temp_mmbtu = rset1.getString(1) == null ? "" : rset1.getString(1);
							mmbtu = rset1.getDouble(1);
//							scm = rset1.getDouble(2);

							if (temp_mmbtu.equals("")) 
							{
								VQTY_MMBTU.add("0.00");
								VQTY_SCM.add("0.00");
								VCOLOR.add("");

							} 
							else 
							{
								VQTY_MMBTU.add(nf.format(mmbtu));
								if(VGCV_NCV.elementAt(i).equals("GCV") || VGCV_NCV.elementAt(i).equals(""))
								{
									double dividing_factor = 1;
									double multiplying_factor =  0.252*1000000;
									scm = (mmbtu*multiplying_factor)/(9802.8*dividing_factor);
								}
								else
								{
									double dividing_factor = 1.11;
									double multiplying_factor =  0.252*1000000;
									scm = (mmbtu*multiplying_factor)/(8831.35*dividing_factor);
								}
								VQTY_SCM.add(nf.format(scm));
								VCOLOR.add("#99ffcc");
							}
						}
						rset1.close();
						stmt1.close();
					
							for (int j = 0; j < ((Vector) VCOUNTERPARTY_PLANT_SEQ.elementAt(i)).size(); j++) 
							{
								String plantSeq = "" + ((Vector) VCOUNTERPARTY_PLANT_SEQ.elementAt(i)).elementAt(j);
								int count1 =0;
								
								queryString1= " SELECT SUM(QTY_MMBTU) "
										+ "FROM FMS_DLNG_ALLOC_MST A " 
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND GAS_DT=TO_DATE(?,'DD/MM/YYYY') AND PLANT_SEQ=? ";
								
								queryString1+= " AND AGMT_NO = ? AND CONT_NO = ? AND CONTRACT_TYPE= ? AND CARGO_NO = ? ";		
								if (!bu_plant_seq.equals("0") && !bu_plant_seq.equals("")) 
							    {
							        queryString1 += " AND BU_SEQ=? ";
							    }
							    if(!plant_seq.equals("0") && (!plant_seq.equals(""))) 
								{
								   queryString1+= " AND PLANT_SEQ = ? ";		
								}
						
							    queryString1+= " AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) "
							    		+ "FROM FMS_DLNG_ALLOC_MST B "
										+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
										+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
										+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
										+ "AND B.GAS_DT=A.GAS_DT) ";
								
								if (!bu_plant_seq.equals("0") && !bu_plant_seq.equals("")) 
							    {
							        queryString1 += " AND BU_SEQ=? ";
							    }
							    if(!plant_seq.equals("0") && (!plant_seq.equals(""))) 
								{
								   queryString1+= " AND PLANT_SEQ = ? ";		
								}
							    
								stmt1 = conn.prepareStatement(queryString1);
								stmt1.setString(++count1, comp_cd);
								stmt1.setString(++count1, counterparty_cd);
								stmt1.setString(++count1, gas_dt);
								stmt1.setString(++count1, plantSeq);
								
								if(!deal_no.equals("0") && (!deal_no.equals(""))) 
								{
								  stmt1.setString(++count1, agmt_no);		
								  //stmt1.setString(++count1, agmt_rev);		
								  stmt1.setString(++count1, cont_no);		
								  stmt1.setString(++count1, contract_type);
								  stmt1.setString(++count1,cargo_no);
								}
								else
								{
								  stmt1.setString(++count1, (String) VAGMT_NO.elementAt(i));		
								  //stmt1.setString(++count1, (String)VAGMT_REV.elementAt(i));		
								  stmt1.setString(++count1, (String)VCONT_NO.elementAt(i));		
								  stmt1.setString(++count1, (String)VCONTRACT_TYPE.elementAt(i));
								  stmt1.setString(++count1, (String)VCARGO_NO.elementAt(i));
								}
								if(!bu_plant_seq.equals("0") && (!bu_plant_seq.equals(""))) 
								{
								 stmt1.setString(++count1, bu_plant_seq);		
								}
								if(!plant_seq.equals("0") && (!plant_seq.equals(""))) 
								{
								  stmt1.setString(++count1, plant_seq);		
								}
								
								if(!bu_plant_seq.equals("0") && (!bu_plant_seq.equals(""))) 
								{
								 stmt1.setString(++count1, bu_plant_seq);		
								}
								if(!plant_seq.equals("0") && (!plant_seq.equals(""))) 
								{
								  stmt1.setString(++count1, plant_seq);		
								}
								rset1 = stmt1.executeQuery();
								while (rset1.next())
								{
									temp_mmbtu = rset1.getString(1) == null ? "" : rset1.getString(1);
									mmbtu = rset1.getDouble(1);

//									scm = rset1.getDouble(2);

									if (temp_mmbtu.equals("")) 
									{
										VQTY_MMBTU.add("0.00");
										VQTY_SCM.add("0.00");
										VCOLOR.add("");
									} 
									else 
									{
										VQTY_MMBTU.add(nf.format(mmbtu));
										if(VGCV_NCV.elementAt(i).equals("GCV") || VGCV_NCV.elementAt(i).equals(""))
										{
											double dividing_factor = 1;
											double multiplying_factor =  0.252*1000000;
											scm = (mmbtu*multiplying_factor)/(9802.8*dividing_factor);
										}
										else
										{
											double dividing_factor = 1.11;
											double multiplying_factor =  0.252*1000000;
											scm = (mmbtu*multiplying_factor)/(8831.35*dividing_factor);
										}
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
					String queryString1= "SELECT SUM(QTY_MMBTU) "
							+ "FROM FMS_DLNG_ALLOC_MST A " 
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') ";
					queryString1+= " AND AGMT_NO = ? AND CONT_NO = ? AND CONTRACT_TYPE= ? AND CARGO_NO = ? ";		
					if (!bu_plant_seq.equals("0") && !bu_plant_seq.equals("")) 
				    {
				        queryString1 += " AND BU_SEQ=? ";
				    }
				    if(!plant_seq.equals("0") && (!plant_seq.equals(""))) 
					{
					   queryString1+= " AND PLANT_SEQ = ? ";		
					} 
					
				    queryString1+= " AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) "
				    		+ "FROM FMS_DLNG_ALLOC_MST B "
							+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
							+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
							+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
							+ "AND B.GAS_DT=A.GAS_DT)";
					if (!bu_plant_seq.equals("0") && !bu_plant_seq.equals("")) 
				    {
				        queryString1 += " AND BU_SEQ=? ";
				    }
				    if(!plant_seq.equals("0") && (!plant_seq.equals(""))) 
					{
					   queryString1+= " AND PLANT_SEQ = ? ";		
					} 
				    
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(++count, comp_cd);
					stmt1.setString(++count, counterparty_cd);
					stmt1.setString(++count, from_dt);
					stmt1.setString(++count, to_dt);
					
					if(!deal_no.equals("0") && (!deal_no.equals(""))) 
					{
					  stmt1.setString(++count, agmt_no);		
					  //stmt1.setString(++count, agmt_rev);		
					  stmt1.setString(++count, cont_no);		
					  stmt1.setString(++count, contract_type);
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
					if(!bu_plant_seq.equals("0") && (!bu_plant_seq.equals(""))) 
					{
					   stmt1.setString(++count, bu_plant_seq);		
					}
					if(!plant_seq.equals("0") && (!plant_seq.equals(""))) 
					{
					  stmt1.setString(++count, plant_seq);		
					}
					
					if(!bu_plant_seq.equals("0") && (!bu_plant_seq.equals(""))) 
					{
					   stmt1.setString(++count, bu_plant_seq);		
					}
					if(!plant_seq.equals("0") && (!plant_seq.equals(""))) 
					{
					  stmt1.setString(++count, plant_seq);		
					}
					rset1 = stmt1.executeQuery();
					while (rset1.next()) 
					{
						total_mmbtu = rset1.getDouble(1);
//						total_scm = rset1.getDouble(2);

						if (Double.doubleToRawLongBits(total_mmbtu) != Double.doubleToRawLongBits(0)) 
						{
							temp_total_mmbtu = total_mmbtu;
							if(VGCV_NCV.elementAt(i).equals("GCV") || VGCV_NCV.elementAt(i).equals(""))
							{
								double dividing_factor = 1;
								double multiplying_factor =  0.252*1000000;
								total_scm = (total_mmbtu*multiplying_factor)/(9802.8*dividing_factor);
							}
							else
							{
								double dividing_factor = 1.11;
								double multiplying_factor =  0.252*1000000;
								total_scm = (total_mmbtu*multiplying_factor)/(8831.35*dividing_factor);
							}
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
							int count1=0;
							
							queryString1 = "SELECT SUM(QTY_MMBTU) "
									+ "FROM FMS_DLNG_ALLOC_MST A " 
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND GAS_DT>=TO_DATE(?,'DD/MM/YYYY') AND GAS_DT<=TO_DATE(?,'DD/MM/YYYY') AND PLANT_SEQ=? ";
							queryString1+= " AND AGMT_NO = ? AND CONT_NO = ? AND CONTRACT_TYPE= ? AND CARGO_NO = ? ";		
							if(!bu_plant_seq.equals("0") && (!bu_plant_seq.equals(""))) 
							{
							    queryString1+= " AND BU_SEQ=?";		
							}
							if(!plant_seq.equals("0") && (!plant_seq.equals(""))) 
							{
							   queryString1+= " AND PLANT_SEQ = ? ";		
							}
							
							queryString1+= " AND NOM_REV_NO=(SELECT MAX(NOM_REV_NO) "
									+ "FROM FMS_DLNG_ALLOC_MST B "
									+ "WHERE B.CONT_NO=A.CONT_NO AND B.AGMT_NO=A.AGMT_NO "
									+ "AND B.COMPANY_CD=A.COMPANY_CD AND B.COUNTERPARTY_CD=A.COUNTERPARTY_CD "
									+ "AND B.PLANT_SEQ=A.PLANT_SEQ AND B.CONTRACT_TYPE=A.CONTRACT_TYPE AND B.BU_SEQ=A.BU_SEQ "
									+ "AND B.GAS_DT=A.GAS_DT) ";
							if(!bu_plant_seq.equals("0") && (!bu_plant_seq.equals(""))) 
							{
							    queryString1+= " AND BU_SEQ=?";		
							}
							if(!plant_seq.equals("0") && (!plant_seq.equals(""))) 
							{
							   queryString1+= " AND PLANT_SEQ = ? ";		
							}
							
							stmt1 = conn.prepareStatement(queryString1);
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
							  stmt1.setString(++count1, contract_type);
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
							if(!bu_plant_seq.equals("0") && (!bu_plant_seq.equals(""))) 
							{
							   stmt1.setString(++count1, bu_plant_seq);		
							}
							if(!plant_seq.equals("0") && (!plant_seq.equals(""))) 
							{
							  stmt1.setString(++count1, plant_seq);		
							}
							
							if(!bu_plant_seq.equals("0") && (!bu_plant_seq.equals(""))) 
							{
							   stmt1.setString(++count1, bu_plant_seq);		
							}
							if(!plant_seq.equals("0") && (!plant_seq.equals(""))) 
							{
							  stmt1.setString(++count1, plant_seq);		
							}
							rset1 = stmt1.executeQuery();
							while (rset1.next()) 
							{
								total_mmbtu = rset1.getDouble(1);
//								total_scm = rset1.getDouble(2);

								if (Double.doubleToRawLongBits(total_mmbtu) != Double.doubleToRawLongBits(0)) 
								{
									temp_total_mmbtu = total_mmbtu;
									if(VGCV_NCV.elementAt(i).equals("GCV") || VGCV_NCV.elementAt(i).equals(""))
									{
										double dividing_factor = 1;
										double multiplying_factor =  0.252*1000000;
										total_scm = (total_mmbtu*multiplying_factor)/(9802.8*dividing_factor);
									}
									else
									{
										double dividing_factor = 1.11;
										double multiplying_factor =  0.252*1000000;
										total_scm = (total_mmbtu*multiplying_factor)/(8831.35*dividing_factor);
									}
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
	
	String segmentType = "";
	String counterparty_cd = "";
	String transporter_cd = "";
	String from_dt = "";
	String to_dt = "";
	String month = "";
	String year = "";
	String gas_dt = "";
	String chk_diff = "";
	String transporter_truck = "";
	String file_path = "";
	String emp_cd = "";
	String contact_person_cd = "";
	String truck_cd = "";
	String file = "";
	String contract_type = "";
	String cargo_no = "";
	String cont_no = "";
	String agmt_no = "";
	String plant_seq = "";
	String bu_plant_seq = "";
	String fill_station = "";
	
	String ip = "";
	String form_id = "";
	String form_name = "";
	String mod_id = "";
	String mod_name = "";
	
	String agmt_rev="";
	String deal_no="";
	String cont_ref_no="";
	String deal_map="";
	int temp_count =0;
	
	public void setSegmentType(String segmentType) {this.segmentType = segmentType;}
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setTransporter_cd(String transporter_cd) {this.transporter_cd = transporter_cd;}
	public void setFrom_dt(String from_dt) {this.from_dt = from_dt;}
	public void setTo_dt(String to_dt) {this.to_dt = to_dt;}
	public void setMonth(String month) {this.month = month;}
	public void setYear(String year) {this.year = year;}
	public void setGas_dt(String gas_dt) {this.gas_dt = gas_dt;}
	public void setChk_diff(String chk_diff) {this.chk_diff = chk_diff;}
	public void setTransporter_truck(String transporter_truck) {this.transporter_truck = transporter_truck;}
	public void setFile_path(String file_path) {this.file_path = file_path;}
	public void setEmp_cd(String emp_cd) {this.emp_cd = emp_cd;}
	public void setContact_person_cd(String contact_person_cd) {this.contact_person_cd = contact_person_cd;}
	public void setTruck_cd(String truck_cd) {this.truck_cd = truck_cd;}
	public void setFile(String file) {this.file = file;}
	public void setContract_type(String contract_type) {this.contract_type = contract_type;}
	public void setCargo_no(String cargo_no) {this.cargo_no = cargo_no;}
	public void setCont_no(String cont_no) {this.cont_no = cont_no;}
	public void setAgmt_no(String agmt_no) {this.agmt_no = agmt_no;}
	public void setPlant_seq(String plant_seq) {this.plant_seq = plant_seq;}
	public void setBu_plant_seq(String bu_plant_seq) {this.bu_plant_seq = bu_plant_seq;}
	public void setFill_station(String fill_station) {this.fill_station = fill_station;}
	
	public void setIp(String ip) {this.ip = ip;}
	public void setForm_id(String form_id) {this.form_id = form_id;}
	public void setForm_name(String form_name) {this.form_name = form_name;}
	public void setMod_id(String mod_id) {this.mod_id = mod_id;}
	public void setMod_name(String mod_name) {this.mod_name = mod_name;}
	
	public void setAgmt_rev(String agmt_rev) {this.agmt_rev = agmt_rev;}
	public void setDeal_no(String deal_no) {this.deal_no = deal_no;}
	
	String all_mail_sent="";
	String total_buyer_mmbtu="";
	String total_buyer_mt="";
	String total_seller_mmbtu="";
	String total_seller_mt="";
	String total_alloc_mmbtu="";
	String total_alloc_mt="";
	String bu_plant="";
	
	public void setBu_plant(String bu_plant) {
		this.bu_plant = bu_plant;
	}

	public String getAll_mail_sent() {return all_mail_sent;}
	public String getTotal_buyer_mmbtu() {return total_buyer_mmbtu;}
	public String getTotal_buyer_mt() {return total_buyer_mt;}
	public String getTotal_seller_mmbtu() {return total_seller_mmbtu;}
	public String getTotal_seller_mt() {return total_seller_mt;}
	public String getTotal_alloc_mmbtu() {return total_alloc_mmbtu;}
	public String getTotal_alloc_mt() {return total_alloc_mt;}
	
	public String getCont_ref_no() {return cont_ref_no;}
	public String getDeal_map() {return deal_map;}
	public int getTemp_count() {return temp_count;}
	
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VMST_COUNTERPARTY_CD = new Vector();
	Vector VMST_COUNTERPARTY_NM = new Vector();
	Vector VMST_COUNTERPARTY_ABBR = new Vector();
	Vector VSEGMENT = new Vector();
	Vector VSEGMENT_TYPE = new Vector();
	Vector VTEMP_SEGMENT = new Vector();
	Vector VTEMP_SEGMENT_TYPE = new Vector();
	Vector VHEADER_SEGMENT = new Vector();
	Vector VSEGMENT_INDEX = new Vector();
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
	Vector VCONTRACT_TYPE = new Vector();
	Vector VSUPPLIED_QTY_MMBTU = new Vector();
	Vector VSUPPLIED_QTY_MMSCM = new Vector();
	Vector VBALANCE_QTY_MMBTU = new Vector();
	Vector VBALANCE_QTY_MMSCM = new Vector();
	Vector VALLOC_MIN_DT = new Vector();
	Vector VALLOC_MAX_DT = new Vector();
	Vector VIS_ALLOCATED = new Vector();
	Vector VPRICE_TYPE = new Vector();
	Vector VDELV_POINT = new Vector();
	Vector VBU_POINT = new Vector();
	Vector VPLANT_UNIT = new Vector();
	Vector VDUE_DATE = new Vector();
	Vector VEXCHANGE_RATE = new Vector();
	Vector VEXCHNG_RATE_NM = new Vector();
	Vector VTOTALBALANCE_SCM = new Vector();
	Vector VTOTALBALANCE_MMBTU = new Vector();
	Vector VTOTALSUPPLIED_SCM = new Vector();
	Vector VTOTALSUPPLIED_MMBTU = new Vector();
	Vector VTOTAL_SCM = new Vector();
	Vector VTOTAL_MMBTU = new Vector();
	Vector VTOTAL_MT = new Vector();
	Vector VINDEX = new Vector();
	Vector VTRANSPORTER_CD = new Vector();
	Vector VTRANSPORTER_ABBR = new Vector();
	Vector VTRANSPORTER_NM = new Vector();
	Vector VTRANSPORTER_TRUCK = new Vector();
	Vector VTRANSPORTER_TRUCK_NO = new Vector();
	Vector VNOM_REV_NO = new Vector();
	Vector VGEN_DT = new Vector();
	Vector VGEN_TIME = new Vector();
	Vector VBASE = new Vector();
	Vector VGCV = new Vector();
	Vector VNCV = new Vector();
	Vector VDCQ = new Vector();
	Vector VMDCQ_QTY = new Vector();
	Vector VCONT_NAME = new Vector();
	Vector VQTY_MMBTU = new Vector();
	Vector VQTY_SCM = new Vector();
	Vector VCOUNTERPARTY_PLANT_SEQ = new Vector();
	Vector VCOUNTERPARTY_PLANT_ABBR = new Vector();
	Vector VBU_CD = new Vector();
	Vector VBU_PLANT_SEQ = new Vector();
	Vector VBU_PLANT_ABBR = new Vector();
	Vector VCONTRACT_TYPE_NM = new Vector();
	Vector VSUB_INDEX = new Vector();
	Vector VBUYER_NOM = new Vector();
	Vector VSELLER_NOM = new Vector();
	Vector VBUYER_NOM_REV_NO = new Vector();
	Vector VCOUNTERPARTY_PLANT_NM = new Vector();
	Vector VGAS_DT = new Vector();
	Vector VTOTAL_QTY_SCM = new Vector();
	Vector VTOTAL_QTY_MMBTU = new Vector();
	Vector VAGMT_BASE = new Vector();
	Vector VCARGO_NO = new Vector();
	Vector VALLOCATION_DATA = new Vector();
	Vector VCOLOR = new Vector();
	Vector VFINAL_TCQ = new Vector();
	Vector VVARIATION_TCQ = new Vector();
	Vector VMST_TRANSPORTER_CD = new Vector();
	Vector VMST_TRANSPORTER_NM = new Vector();
	Vector VMST_TRANSPORTER_ABBR = new Vector();
	Vector VMST_TRANSPORTER_TRUCK = new Vector();
	Vector VMST_TRANSPORTER_TRUCK_NO = new Vector();
	Vector VQTY_SELLER_MMBTU = new Vector();
	Vector VQTY_SELLER_MT = new Vector();
	Vector VQTY_ALLOC_MMBTU = new Vector();
	Vector VQTY_ALLOC_MT = new Vector();
	Vector VCOLOR_ALLOC = new Vector();
	Vector VCONTACT_PERSON = new Vector();
	Vector VCONTACT_PERSON_CD = new Vector();
	Vector VSEL_CONTACT_PERSON_CD = new Vector();
	Vector VADDRESSED_PERSON = new Vector();
	Vector VPDF_NAME = new Vector();
	Vector VPDF_PATH = new Vector();
	Vector VEMAIL_FLAG = new Vector();
	Vector VREMARK = new Vector();
	Vector VPDF_EXISTS = new Vector();
	Vector VMAIL_FROM_LIST = new Vector();
	Vector VMAIL_TO_LIST = new Vector();
	Vector VMAIL_CC_LIST = new Vector();
	Vector VMAIL_BCC_LIST = new Vector();
	Vector VMAIL_SUBJECT = new Vector();
	Vector VMAIL_ATTACHMENT = new Vector();
	Vector VMAIL_ATTACHMENT_PATH = new Vector();
	Vector VMAIL_BODY = new Vector();
	Vector VMST_CONTRACT_TYPE = new Vector();
	Vector VMST_CONTRACT_TYPE_NM = new Vector();
	Vector VCONT_BU_PLANT_SEQ = new Vector();
	Vector VCONT_BU_PLANT_MAP = new Vector();
	Vector VTITTLE_DISP_CONT_NO = new Vector();
	Vector VQTY_MT = new Vector();
	Vector VLOAD_START_DT = new Vector();
	Vector VLOAD_START_TIME = new Vector();
	Vector VLOAD_END_DT = new Vector();
	Vector VLOAD_END_TIME = new Vector();
	Vector VTRUCK_REG_NUM = new Vector();
	Vector VTRUCK_CD = new Vector();
	Vector VTRUCK_TRANS_CD = new Vector();
	Vector VTOTAL_SELLER_MMBTU = new Vector();
	Vector VTOTAL_BUYER_MMBTU = new Vector();
	Vector VTOTAL_ALLOC_MMBTU = new Vector();
	Vector VTOTAL_ALLOC_MT = new Vector();
	Vector VLEAGAL_ENTITY = new Vector();
	Vector VFILLING_ST_CD = new Vector();
	Vector VFILLING_ST_NM = new Vector();
	Vector VFILLING_ST_ABBR = new Vector();
	Vector VBAY_CD = new Vector();
	Vector VBAY_NM = new Vector();
	Vector VSLOT_DLT = new Vector();
	Vector VNEXT_AVAIL_HRS = new Vector();
	Vector VOCCUPIED_START_TIME = new Vector();
	Vector VOCCUPIED_END_TIME = new Vector();
	Vector VASSIGNED_DRIVER = new Vector();
	Vector VCURR_STATUS = new Vector();
	
	Vector VSLOT_START_TIME = new Vector();
	Vector VSLOT_END_TIME = new Vector();
	Vector VARRIVAL_DT = new Vector();
	Vector VCHECKPOST_NM = new Vector();
	Vector VDRIVER_NM = new Vector();

	Vector VDTL_NOM_REV_NO = new Vector();
	Vector VDTL_QTY_MMBTU = new Vector();
	Vector VDTL_QTY_MT = new Vector();
	Vector VDTL_TRUCK_TRANS_CD = new Vector();
	Vector VDTL_TRUCK_CD = new Vector();
	Vector VDTL_SLOT_START_TIME = new Vector();
	Vector VDTL_SLOT_END_TIME = new Vector();
	Vector VDTL_ARRIVAL_DT = new Vector();
	Vector VDTL_REMARK = new Vector();
	Vector VDTL_DRIVER_NM = new Vector();
	Vector VDTL_CHECKPOST_NM = new Vector();
	Vector VDTL_TRANSPORTER_ABBR = new Vector();
	Vector VDTL_TRANSPORTER_NM = new Vector();
	Vector VDTL_TRUCK_REG_NUM = new Vector();
	Vector VDTL_FILLING_ST_ABBR = new Vector();
	Vector VDTL_BAY_NM = new Vector();
	Vector VDTL_SLOT_DLT = new Vector();
	
	Vector VMST_FILLST_CD = new Vector();
	Vector VMST_FILLST_NM = new Vector();
	Vector VMST_FILLST_ABBR = new Vector();

	Vector VBu_Plant_Abbr_List = new Vector();// SagarB20250923 VBu_Plant_Abbr_List variable and its setter-getter methods declared for DLNG-> DLNG Nomination vs Truck Loading
	
	Vector VTOTAL_DCQ = new Vector();
	
	Vector VAGMT_REV = new Vector();
	Vector VPLANT_NM = new Vector();
	Vector VPLANT_SEQ_NO = new Vector();
	Vector VGCV_NCV = new Vector();
	
	public Vector getVLEAGAL_ENTITY() {return VLEAGAL_ENTITY;}
	public Vector getVFILLING_ST_CD() {return VFILLING_ST_CD;}
	public Vector getVFILLING_ST_NM() {return VFILLING_ST_NM;}
	public Vector getVFILLING_ST_ABBR() {return VFILLING_ST_ABBR;}
	public Vector getVBAY_CD() {return VBAY_CD;}
	public Vector getVBAY_NM() {return VBAY_NM;}
	public Vector getVSLOT_DLT() {return VSLOT_DLT;}
	public Vector getVNEXT_AVAIL_HRS() {return VNEXT_AVAIL_HRS;}
	public Vector getVOCCUPIED_START_TIME() {return VOCCUPIED_START_TIME;}
	public Vector getVOCCUPIED_END_TIME() {return VOCCUPIED_END_TIME;}
	public Vector getVASSIGNED_DRIVER() {return VASSIGNED_DRIVER;}
	public Vector getVCURR_STATUS() {return VCURR_STATUS;}
	
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVMST_COUNTERPARTY_CD() {return VMST_COUNTERPARTY_CD;}
	public Vector getVMST_COUNTERPARTY_NM() {return VMST_COUNTERPARTY_NM;}
	public Vector getVMST_COUNTERPARTY_ABBR() {return VMST_COUNTERPARTY_ABBR;}
	public Vector getVSEGMENT() {return VSEGMENT;}
	public Vector getVSEGMENT_TYPE() {return VSEGMENT_TYPE;}
	public Vector getVTEMP_SEGMENT() {return VTEMP_SEGMENT;}
	public Vector getVTEMP_SEGMENT_TYPE() {return VTEMP_SEGMENT_TYPE;}
	public Vector getVHEADER_SEGMENT() {return VHEADER_SEGMENT;}
	public Vector getVSEGMENT_INDEX() {return VSEGMENT_INDEX;}
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
	public Vector getVCONTRACT_TYPE() {return VCONTRACT_TYPE;}
	public Vector getVSUPPLIED_QTY_MMBTU() {return VSUPPLIED_QTY_MMBTU;}
	public Vector getVSUPPLIED_QTY_MMSCM() {return VSUPPLIED_QTY_MMSCM;}
	public Vector getVBALANCE_QTY_MMBTU() {return VBALANCE_QTY_MMBTU;}
	public Vector getVBALANCE_QTY_MMSCM() {return VBALANCE_QTY_MMSCM;}
	public Vector getVALLOC_MIN_DT() {return VALLOC_MIN_DT;}
	public Vector getVALLOC_MAX_DT() {return VALLOC_MAX_DT;}
	public Vector getVPRICE_TYPE() {return VPRICE_TYPE;}
	public Vector getVIS_ALLOCATED() {return VIS_ALLOCATED;}
	public Vector getVDELV_POINT() {return VDELV_POINT;}
	public Vector getVBU_POINT() {return VBU_POINT;}
	public Vector getVPLANT_UNIT() {return VPLANT_UNIT;}
	public Vector getVDUE_DATE() {return VDUE_DATE;}
	public Vector getVEXCHANGE_RATE() {return VEXCHANGE_RATE;}
	public Vector getVEXCHNG_RATE_NM() {return VEXCHNG_RATE_NM;}
	public Vector getVTOTAL_MMBTU() {return VTOTAL_MMBTU;}
	public Vector getVTOTAL_MT() {return VTOTAL_MT;}
	public Vector getVTOTAL_SCM() {return VTOTAL_SCM;}
	public Vector getVTOTALSUPPLIED_MMBTU() {return VTOTALSUPPLIED_MMBTU;}
	public Vector getVTOTALSUPPLIED_SCM() {return VTOTALSUPPLIED_SCM;}
	public Vector getVTOTALBALANCE_MMBTU() {return VTOTALBALANCE_MMBTU;}
	public Vector getVTOTALBALANCE_SCM() {return VTOTALBALANCE_SCM;}
	public Vector getVINDEX() {return VINDEX;}
	public Vector getVTRANSPORTER_CD() {return VTRANSPORTER_CD;}
	public Vector getVTRANSPORTER_ABBR() {return VTRANSPORTER_ABBR;}
	public Vector getVTRANSPORTER_NM() {return VTRANSPORTER_NM;}
	public Vector getVTRANSPORTER_TRUCK() {return VTRANSPORTER_TRUCK;}
	public Vector getVTRANSPORTER_TRUCK_NO() {return VTRANSPORTER_TRUCK_NO;}
	public Vector getVNOM_REV_NO() {return VNOM_REV_NO;}
	public Vector getVGEN_DT() {return VGEN_DT;}
	public Vector getVGEN_TIME() {return VGEN_TIME;}
	public Vector getVBASE() {return VBASE;}
	public Vector getVGCV() {return VGCV;}
	public Vector getVNCV() {return VNCV;}
	public Vector getVDCQ() {return VDCQ;}
	public Vector getVMDCQ_QTY() {return VMDCQ_QTY;}
	public Vector getVCONT_NAME() {return VCONT_NAME;}
	public Vector getVQTY_MMBTU() {return VQTY_MMBTU;}
	public Vector getVQTY_SCM() {return VQTY_SCM;}
	public Vector getVCOUNTERPARTY_PLANT_SEQ() {return VCOUNTERPARTY_PLANT_SEQ;}
	public Vector getVCOUNTERPARTY_PLANT_ABBR() {return VCOUNTERPARTY_PLANT_ABBR;}
	public Vector getVBU_CD() {return VBU_CD;}
	public Vector getVBU_PLANT_SEQ() {return VBU_PLANT_SEQ;}
	public Vector getVBU_PLANT_ABBR() {return VBU_PLANT_ABBR;}
	public Vector getVCONTRACT_TYPE_NM() {return VCONTRACT_TYPE_NM;}
	public Vector getVSUB_INDEX() {return VSUB_INDEX;}
	public Vector getVBUYER_NOM() {return VBUYER_NOM;}
	public Vector getVSELLER_NOM() {return VSELLER_NOM;}
	public Vector getVBUYER_NOM_REV_NO() {return VBUYER_NOM_REV_NO;}
	public Vector getVCOUNTERPARTY_PLANT_NM() {return VCOUNTERPARTY_PLANT_NM;}
	public Vector getVTOTAL_QTY_SCM() {return VTOTAL_QTY_SCM;}
	public Vector getVTOTAL_QTY_MMBTU() {return VTOTAL_QTY_MMBTU;}
	public Vector getVGAS_DT() {return VGAS_DT;}
	public Vector getVAGMT_BASE() {return VAGMT_BASE;}
	public Vector getVCARGO_NO() {return VCARGO_NO;}
	public Vector getVALLOCATION_DATA() {return VALLOCATION_DATA;}
	public Vector getVCOLOR() {return VCOLOR;}
	public Vector getVFINAL_TCQ() {return VFINAL_TCQ;}
	public Vector getVVARIATION_TCQ() {return VVARIATION_TCQ;}
	public Vector getVMST_TRANSPORTER_CD() {return VMST_TRANSPORTER_CD;}
	public Vector getVMST_TRANSPORTER_NM() {return VMST_TRANSPORTER_NM;}
	public Vector getVMST_TRANSPORTER_ABBR() {return VMST_TRANSPORTER_ABBR;}
	public Vector getVMST_TRANSPORTER_TRUCK() {return VMST_TRANSPORTER_TRUCK;}
	public Vector getVMST_TRANSPORTER_TRUCK_NO() {return VMST_TRANSPORTER_TRUCK_NO;}
	public Vector getVQTY_SELLER_MMBTU() {return VQTY_SELLER_MMBTU;}
	public Vector getVQTY_SELLER_MT() {return VQTY_SELLER_MT;}
	public Vector getVQTY_ALLOC_MMBTU() {return VQTY_ALLOC_MMBTU;}
	public Vector getVQTY_ALLOC_MT() {return VQTY_ALLOC_MT;}
	public Vector getVCOLOR_ALLOC() {return VCOLOR_ALLOC;}
	public Vector getVCONTACT_PERSON() {return VCONTACT_PERSON;}
	public Vector getVCONTACT_PERSON_CD() {return VCONTACT_PERSON_CD;}
	public Vector getVSEL_CONTACT_PERSON_CD() {return VSEL_CONTACT_PERSON_CD;}
	public Vector getVADDRESSED_PERSON() {return VADDRESSED_PERSON;}
	public Vector getVPDF_NAME() {return VPDF_NAME;}
	public Vector getVPDF_PATH() {return VPDF_PATH;}
	public Vector getVEMAIL_FLAG() {return VEMAIL_FLAG;}
	public Vector getVREMARK() {return VREMARK;}
	public Vector getVPDF_EXISTS() {return VPDF_EXISTS;}
	public Vector getVMAIL_FROM_LIST() {return VMAIL_FROM_LIST;}
	public Vector getVMAIL_TO_LIST() {return VMAIL_TO_LIST;}
	public Vector getVMAIL_CC_LIST() {return VMAIL_CC_LIST;}
	public Vector getVMAIL_BCC_LIST() {return VMAIL_BCC_LIST;}
	public Vector getVMAIL_SUBJECT() {return VMAIL_SUBJECT;}
	public Vector getVMAIL_ATTACHMENT() {return VMAIL_ATTACHMENT;}
	public Vector getVMAIL_ATTACHMENT_PATH() {return VMAIL_ATTACHMENT_PATH;}
	public Vector getVMAIL_BODY() {return VMAIL_BODY;}
	public Vector getVMST_CONTRACT_TYPE_NM() {return VMST_CONTRACT_TYPE_NM;}
	public Vector getVMST_CONTRACT_TYPE() {return VMST_CONTRACT_TYPE;}
	public Vector getVCONT_BU_PLANT_SEQ() {return VCONT_BU_PLANT_SEQ;}
	public Vector getVCONT_BU_PLANT_MAP() {return VCONT_BU_PLANT_MAP;}
	public Vector getVTITTLE_DISP_CONT_NO() {return VTITTLE_DISP_CONT_NO;}
	public Vector getVQTY_MT() {return VQTY_MT;}
	public Vector getVLOAD_START_DT() {return VLOAD_START_DT;}
	public Vector getVLOAD_START_TIME() {return VLOAD_START_TIME;}
	public Vector getVLOAD_END_DT() {return VLOAD_END_DT;}
	public Vector getVLOAD_END_TIME() {return VLOAD_END_TIME;}
	public Vector getVTRUCK_REG_NUM() {return VTRUCK_REG_NUM;}
	public Vector getVTRUCK_CD() {return VTRUCK_CD;}
	public Vector getVTRUCK_TRANS_CD() {return VTRUCK_TRANS_CD;}
	public Vector getVTOTAL_SELLER_MMBTU() {return VTOTAL_SELLER_MMBTU;}
	public Vector getVTOTAL_BUYER_MMBTU() {return VTOTAL_BUYER_MMBTU;}
	public Vector getVTOTAL_ALLOC_MMBTU() {return VTOTAL_ALLOC_MMBTU;}
	public Vector getVTOTAL_ALLOC_MT() {return VTOTAL_ALLOC_MT;}

	public Vector getVSLOT_START_TIME() {return VSLOT_START_TIME;}
	public Vector getVSLOT_END_TIME() {return VSLOT_END_TIME;}
	public Vector getVARRIVAL_DT() {return VARRIVAL_DT;}
	public Vector getVDRIVER_NM() {return VDRIVER_NM;}
	public Vector getVCHECKPOST_NM() {return VCHECKPOST_NM;}
	
	public Vector getVDTL_NOM_REV_NO() {return VDTL_NOM_REV_NO;}
	public Vector getVDTL_QTY_MMBTU() {return VDTL_QTY_MMBTU;}
	public Vector getVDTL_QTY_MT() {return VDTL_QTY_MT;}
	public Vector getVDTL_TRUCK_TRANS_CD() {return VDTL_TRUCK_TRANS_CD;}
	public Vector getVDTL_TRUCK_CD() {return VDTL_TRUCK_CD;}
	public Vector getVDTL_SLOT_START_TIME() {return VDTL_SLOT_START_TIME;}
	public Vector getVDTL_SLOT_END_TIME() {return VDTL_SLOT_END_TIME;}
	public Vector getVDTL_ARRIVAL_DT() {return VDTL_ARRIVAL_DT;}
	public Vector getVDTL_REMARK() {return VDTL_REMARK;}
	public Vector getVDTL_DRIVER_NM() {return VDTL_DRIVER_NM;}
	public Vector getVDTL_CHECKPOST_NM() {return VDTL_CHECKPOST_NM;}
	public Vector getVDTL_TRUCK_REG_NUM() {return VDTL_TRUCK_REG_NUM;}
	public Vector getVDTL_TRANSPORTER_NM() {return VDTL_TRANSPORTER_NM;}
	public Vector getVDTL_TRANSPORTER_ABBR() {return VDTL_TRANSPORTER_ABBR;}
	public Vector getVDTL_FILLING_ST_ABBR() {return VDTL_FILLING_ST_ABBR;}
	public Vector getVDTL_BAY_NM() {return VDTL_BAY_NM;}
	public Vector getVDTL_SLOT_DLT() {return VDTL_SLOT_DLT;}
	
	public Vector getVMST_FILLST_CD() {return VMST_FILLST_CD;}
	public Vector getVMST_FILLST_NM() {return VMST_FILLST_NM;}
	public Vector getVMST_FILLST_ABBR() {return VMST_FILLST_ABBR;}
	
	HashMap<String, HashMap<String, String>> view_data = new HashMap();
	HashMap<String, String> view_info = new HashMap();
	HashMap<String, HashMap<String, String>> view_seller_nomination_to_customer_data = new HashMap();
	HashMap<String, String> view_seller_nomination_to_customer_info = new HashMap();
	HashMap<String, HashMap<String, String>> view_join_ticket_data = new HashMap();
	HashMap<String, String> view_join_ticket_info = new HashMap();
	
	public HashMap<String, HashMap<String, String>> getView_data() {return view_data;}
	public HashMap<String, String> getView_info() {return view_info;}
	public HashMap<String, HashMap<String, String>> getView_seller_nomination_to_customer_data() {return view_seller_nomination_to_customer_data;}
	public HashMap<String, String> getView_seller_nomination_to_customer_info() {return view_seller_nomination_to_customer_info;}
	public HashMap<String, HashMap<String, String>> getView_join_ticket_data() {return view_join_ticket_data;}
	public HashMap<String, String> getView_join_ticket_info() {return view_join_ticket_info;}

	// SagarB20250923 bu_seq variable and its setter-getter methods declared for DLNG-> DLNG Nomination vs Truck Loading
	String bu_seq = "";
	public String getBu_seq() {return bu_seq;}
	public void setBu_seq(String Bu_seq) {bu_seq = Bu_seq;}
	
	// SagarB20250923 VBu_Plant_Abbr_List variable and its setter-getter methods declared for DLNG-> DLNG Nomination vs Truck Loading
	public Vector getVBu_Plant_Abbr_List() {return VBu_Plant_Abbr_List;}
	
	public Vector getVTOTAL_DCQ() {return VTOTAL_DCQ;}
	
	public Vector getVAGMT_REV() {return VAGMT_REV;}
	public Vector getVPLANT_NM() {return VPLANT_NM;}
	public Vector getVPLANT_SEQ_NO() {return VPLANT_SEQ_NO;}
	public Vector getVGCV_NCV() {return VGCV_NCV;}
	
}
