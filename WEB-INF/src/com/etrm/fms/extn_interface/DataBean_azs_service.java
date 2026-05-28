package com.etrm.fms.extn_interface;

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

public class DataBean_azs_service
{
	String db_src_file_name="DataBean_azs_service.java";
	Connection conn;
	
	PreparedStatement stmt;
	PreparedStatement stmt0;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt4;
	PreparedStatement stmt5;
	PreparedStatement stmt6;
	
	ResultSet rset;
	ResultSet rset0;
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset5;
	ResultSet rset6;
	
	UtilBean utilBean = new UtilBean();
	DateUtil utilDate = new DateUtil();
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf3 = new DecimalFormat("###########0.000");
	
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
					if(callFlag.equalsIgnoreCase("BUS_API_RPT"))
					{
						getSegment();
						getTradeBusApiRpt();
						getTransportBusApiRpt();
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
			
			if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt0 != null){try{stmt0.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt4 != null){try{stmt4.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt5 != null){try{stmt5.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}	
			if(stmt6 != null){try{stmt6.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}	
			
			if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		}
	}
	
	public void getSegment()
	{
		String function_nm="getSegment()";
		try
		{
			VSEGMENT_CD.add("T");	//for trader
			VSEGMENT_NM.add("Trader Dataset");
			VSEGMENT_CD.add("R");	//for transporter
			VSEGMENT_NM.add("Trasporter Dataset");
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getTradeBusApiRpt()
	{
		String function_nm="getTradeBusApiRpt()";
		try
		{
			int index=0;
			String query="SELECT RECORD_ID,BLOB,DATA_TYPE,SUBDATA_TYPE,COUNTERPRTY, "
					+ "EMAIL_PATH,EMAIL_SUBJECT,TO_CHAR(REPORT_DATE,'DD/MM/YYYY'), "
					+ "TO_CHAR(GAS_DAY,'DD/MM/YYYY'),SL_NO,CTR_NO,CUSTOMER_NAME, "
					+ "NOMINATION_QTY,CONFIRM_QTY,SCHEDULE_QTY,ALLOCATION_QTY,"
					+ "MSG_ID,SEQ_NO,TO_CHAR(ENT_DT,'DD/MM/YYYY') "
					+ "FROM FMS_AZS_BUS_TRADE "
					+ "WHERE REPORT_DATE>=TO_DATE(?,'DD/MM/YYYY') AND REPORT_DATE<=TO_DATE(?,'DD/MM/YYYY') ";
			stmt=conn.prepareStatement(query);
			stmt.setString(1, from_dt);
			stmt.setString(2, to_dt);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				index++;
				String record_id=rset.getString(1)==null?"":rset.getString(1);
				String blob=rset.getString(2)==null?"":rset.getString(2);
				String data_type=rset.getString(3)==null?"":rset.getString(3);
				String subdata_type=rset.getString(4)==null?"":rset.getString(4);
				String counterprty=rset.getString(5)==null?"":rset.getString(5);
				String email_path=rset.getString(6)==null?"":rset.getString(6);
				String email_sub=rset.getString(7)==null?"":rset.getString(7);
				String report_dt=rset.getString(8)==null?"":rset.getString(8);
				String gas_day=rset.getString(9)==null?"":rset.getString(9);
				String sl_no=rset.getString(10)==null?"":rset.getString(10);
				String ctr_no=rset.getString(11)==null?"":rset.getString(11);
				String customer_nm=rset.getString(12)==null?"":rset.getString(12);
				String nom_qty=rset.getString(13)==null?"":nf.format(rset.getDouble(13));
				String confirm_qty=rset.getString(14)==null?"":nf.format(rset.getDouble(14));
				String sch_qty=rset.getString(15)==null?"":nf.format(rset.getDouble(15));
				String alloc_qty=rset.getString(16)==null?"":nf.format(rset.getDouble(16));
				String msg_id=rset.getString(17)==null?"":rset.getString(17);
				String seq_no=rset.getString(18)==null?"":rset.getString(18);
				String ent_dt=rset.getString(19)==null?"":rset.getString(19);
				
				VRECORD_ID.add(record_id);
				VBLOB.add(blob);
				VDATA_TYPE.add(data_type);
				VSUB_DATA_TYPE.add(subdata_type);
				VCOUNTERPARTY.add(counterprty);
				VEMAIL_PATH.add(email_path);
				VEMAIL_SUBJECT.add(email_sub);
				VREPORT_DT.add(report_dt);
				VGAS_DAY.add(gas_day);
				VSL_NO.add(sl_no);
				VCTR_NO.add(ctr_no);
				VCUSTOMER_NM.add(customer_nm);
				VNOMINATION_QTY.add(nom_qty);
				VCONFIRM_QTY.add(confirm_qty);
				VSCH_QTY.add(sch_qty);
				VALLOC_QTY.add(alloc_qty);
				VMSG_ID.add(msg_id);
				VSEQ_NO.add(seq_no);
				VENT_DT.add(ent_dt);
				VEMS_PROCESSED.add("");
			}
			rset.close();
			stmt.close();
			VINDEX.add(index);
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getTransportBusApiRpt()
	{
		String function_nm="getTransportBusApiRpt()";
		try
		{
			int index =0;
			VINDEX.add(index);
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
	String from_dt = "";
	public void setFrom_dt(String from_dt) {this.from_dt=from_dt;}
	String to_dt="";
	public void setTo_dt(String to_dt) {this.to_dt=to_dt;}
	
	Vector VSEGMENT_CD = new Vector();
	Vector VSEGMENT_NM = new Vector();
	
	Vector VRECORD_ID = new Vector();
	Vector VBLOB = new Vector();
	Vector VDATA_TYPE = new Vector();
	Vector VSUB_DATA_TYPE = new Vector();
	Vector VCOUNTERPARTY = new Vector();
	Vector VEMAIL_PATH = new Vector();
	Vector VEMAIL_SUBJECT = new Vector();
	Vector VREPORT_DT = new Vector();
	Vector VGAS_DAY = new Vector();
	Vector VSL_NO = new Vector();
	Vector VCTR_NO = new Vector();
	Vector VCUSTOMER_NM = new Vector();
	Vector VNOMINATION_QTY = new Vector();
	Vector VCONFIRM_QTY = new Vector();
	Vector VSCH_QTY = new Vector();
	Vector VALLOC_QTY = new Vector();
	Vector VINDEX = new Vector();
	Vector VMSG_ID = new Vector();
	Vector VSEQ_NO = new Vector();
	Vector VENT_DT = new Vector();
	Vector VEMS_PROCESSED = new Vector();
	
	public Vector getVSEGMENT_CD() {return VSEGMENT_CD;}
	public Vector getVSEGMENT_NM() {return VSEGMENT_NM;}
	
	public Vector getVRECORD_ID() {return VRECORD_ID;}
	public Vector getVBLOB() {return VBLOB;}
	public Vector getVDATA_TYPE() {return VDATA_TYPE;}
	public Vector getVSUB_DATA_TYPE() {return VSUB_DATA_TYPE;}
	public Vector getVCOUNTERPARTY() {return VCOUNTERPARTY;}
	public Vector getVEMAIL_PATH() {return VEMAIL_PATH;}
	public Vector getVEMAIL_SUBJECT() {return VEMAIL_SUBJECT;}
	public Vector getVREPORT_DT() {return VREPORT_DT;}
	public Vector getVGAS_DAY() {return VGAS_DAY;}
	public Vector getVSL_NO() {return VSL_NO;}
	public Vector getVCTR_NO() {return VCTR_NO;}
	public Vector getVCUSTOMER_NM() {return VCUSTOMER_NM;}
	public Vector getVNOMINATION_QTY() {return VNOMINATION_QTY;}
	public Vector getVCONFIRM_QTY() {return VCONFIRM_QTY;}
	public Vector getVSCH_QTY() {return VSCH_QTY;}
	public Vector getVALLOC_QTY() {return VALLOC_QTY;}
	public Vector getVINDEX() {return VINDEX;}
	public Vector getVMSG_ID() {return VMSG_ID;}
	public Vector getVSEQ_NO() {return VSEQ_NO;}
	public Vector getVENT_DT() {return VENT_DT;}
	public Vector getVEMS_PROCESSED() {return VEMS_PROCESSED;}
}