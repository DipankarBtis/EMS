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

import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;

//Coded By          : Harsh Patel
//Code Reviewed by	:  
//CR Date			: 04/04/2022 
//Status	  		: Developing
public class DB_Contract_Price_Modification 
{
	String db_src_file_name="DB_Contract_Price_Modification.java";

	Connection conn; 
	PreparedStatement stmt;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	PreparedStatement stmt_tmp;
	PreparedStatement stmt_tmp1;
	PreparedStatement stmt_tmp2;
	PreparedStatement stmt_tmp3;
	ResultSet rset;
	ResultSet rset_tmpl; 
	ResultSet rset_tmp; 
	ResultSet rset_tmp1;
	ResultSet rset_tmp2;
	ResultSet rset_tmp3; 
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	String queryString="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";
	String queryString6="";
	String queryString7="";
	String queryString8="";
	String queryString9="";
	String queryString10="";
	String queryString11="";
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	
	UtilBean utilBean = new UtilBean();
	public String callFlag="";	
	String comp_cd = "";
	public String cargo_ref_no="";
	public String year="";
	
	String from_date ="";
	String to_date = "";
	
	public void setCallFlag(String callFlag) {this.callFlag = callFlag;}
	public void setComp_cd(String comp_cd) {this.comp_cd = comp_cd;}
	public void setFrom_date(String from_date) {this.from_date = from_date;}
	public void setTo_date(String to_date) {this.to_date = to_date;}
	
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
	    			if(callFlag.equalsIgnoreCase("CargoPriceModification"))
	    			{
	    				String cont_typ="RLNG";
	    				getCustomerList(cont_typ);
	    				getExchangeRate();
	    				fetchContractPriceChangeReq(cont_typ);
	    			}
	    			if(callFlag.equalsIgnoreCase("DLNGPriceModification"))
	    			{
	    				String cont_typ="DLNG";
	    				getCustomerList(cont_typ);
	    				getExchangeRate();
	    				fetchContractPriceChangeReq(cont_typ);
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
	    	if(rset_tmp != null){try{rset_tmp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_tmp1 != null){try{rset_tmp1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_tmp2 != null){try{rset_tmp2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(rset_tmp3 != null){try{rset_tmp3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt_tmp != null){try{stmt_tmp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt_tmp1 != null){try{stmt_tmp1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt_tmp2 != null){try{stmt_tmp2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(stmt_tmp3 != null){try{stmt_tmp3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
			if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}
	
	String customer_cd="";
	
	public void getCustomerList(String cont_typ)
	{
		String function_nm="getCustomerList()";

		try
		{
			int ctn=0;
			queryString = "SELECT DISTINCT COUNTERPARTY_CD "
					+ "FROM FMS_SUPPLY_CONT_MST A "
					//+ "WHERE COMPANY_CD=? AND PRICE_REQUEST_FLAG=? AND (PRICE_APPROVE_FLAG=? OR PRICE_APPROVE_FLAG IS NULL) AND END_DT >= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') "
					+ "WHERE COMPANY_CD=? AND PRICE_REQUEST_FLAG=? AND (PRICE_APPROVE_FLAG=? OR PRICE_APPROVE_FLAG IS NULL) "
					+ "AND TO_DATE(TO_CHAR(ADD_MONTHS(TRUNC(LAST_DAY(TO_DATE(TO_CHAR(END_DT,'DD/MM/YYYY'), 'DD/MM/YYYY'))), 1),'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') "
					+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			if(cont_typ.equals("DLNG")||cont_typ.equals("RLNG"))
			{
				queryString+="AND CONTRACT_TYPE IN (?,?,?) ";
			}
			stmt = conn.prepareStatement(queryString);
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, "Y");
			stmt.setString(++ctn, "N");
			if(cont_typ.equals("DLNG"))
			{
				stmt.setString(++ctn, "F");
				stmt.setString(++ctn, "E");
				stmt.setString(++ctn, "W");
			}
			else if(cont_typ.equals("RLNG"))
			{
				stmt.setString(++ctn, "S");
				stmt.setString(++ctn, "L");
				stmt.setString(++ctn, "X");
				
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String cust_cd = rset.getString(1)==null?"":rset.getString(1);
				String cust_nm = utilBean.getCounterpartyName(conn,cust_cd);
				
				VCUSTOMR_CD_MST.add(cust_cd);
				VCUSTOMR_NM_MST.add(cust_nm);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}	
	}
	
	public void fetchContractPriceChangeReq(String cont_typ)
	{
		String function_nm="fetchContractPriceChangeReq()";

		try
		{
			int ctn=0;
			queryString = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,TO_CHAR(SIGNING_DT,'DD/MM/YYYY'),TO_CHAR(START_DT,'DD/MM/YYYY'),"
					+ "TO_CHAR(END_DT,'DD/MM/YYYY'),TCQ,RATE, PRICE_REQUEST_FLAG, PRICE_APPROVE_FLAG, RATE_UNIT,CONTRACT_TYPE,"
					+ "CONT_REF_NO,TRADE_REF_NO "
					+ "FROM FMS_SUPPLY_CONT_MST A "
					+ "WHERE A.COUNTERPARTY_CD=? AND PRICE_REQUEST_FLAG=? AND (PRICE_APPROVE_FLAG=? OR PRICE_APPROVE_FLAG IS NULL) "
					+ "AND A.COMPANY_CD=? "
					+ "AND TO_DATE(TO_CHAR(ADD_MONTHS(TRUNC(LAST_DAY(TO_DATE(TO_CHAR(END_DT,'DD/MM/YYYY'), 'DD/MM/YYYY'))), 1),'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') "
					//+ "AND END_DT >= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') "
					+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
					+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			if(cont_typ.equals("DLNG")||cont_typ.equals("RLNG"))
			{
				queryString+="AND CONTRACT_TYPE IN (?,?,?) ";
			}
			stmt = conn.prepareStatement(queryString);
			stmt.setString(++ctn, customer_cd);
			stmt.setString(++ctn, "Y");
			stmt.setString(++ctn, "N");
			stmt.setString(++ctn, comp_cd);
			if(cont_typ.equals("DLNG"))
			{
				stmt.setString(++ctn, "F");
				stmt.setString(++ctn, "E");
				stmt.setString(++ctn, "W");
			}
			else if(cont_typ.equals("RLNG"))
			{
				stmt.setString(++ctn, "S");
				stmt.setString(++ctn, "L");
				stmt.setString(++ctn, "X");
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String fgsa=rset.getString(1)==null?"":rset.getString(1);
				String fgsa_rev=rset.getString(2)==null?"":rset.getString(2);
				String sn=rset.getString(3)==null?"":rset.getString(3);
				String sn_rev=rset.getString(4)==null?"":rset.getString(4);
				String start_dt=rset.getString(6)==null?"":rset.getString(6);
				String end_dt=rset.getString(7)==null?"":rset.getString(7);
				String rate_unit = rset.getString(12)==null?"2":rset.getString(12);
				
				String segment="RLNG";
				String cont_type= rset.getString(13)==null?"":rset.getString(13);
				String cont_ref= rset.getString(14)==null?"":rset.getString(14);
				String trade_ref= rset.getString(15)==null?"":rset.getString(15);
				String cont_dtl=utilBean.NewDealMappingId(comp_cd, customer_cd, fgsa, fgsa_rev, sn, sn_rev, cont_type, "");
				if (cont_type.equals("X"))
				{
					cont_dtl+="<br>"+trade_ref;
				}	
				else
				{
					cont_dtl+="<br>"+cont_ref;
				}	
				VCONTRACT_DETAIL.add(cont_dtl);
				VFGSA.add(fgsa);
				VFGSA_REV.add(fgsa_rev);
				VSN.add(sn);
				VSN_REV.add(sn_rev);
				VCONT_ST_DT.add(start_dt);
				VCONT_END_DT.add(end_dt);
				VCONTRACT_TYPE.add(cont_type);
				VCONTRACT_TYPE_NM.add(""+utilBean.getContractTypeName(cont_type));
				VSEGMENT.add(segment);
				
				VSALES_RATE.add(nf2.format(rset.getDouble(9)));
				VRATE_UNIT.add(rate_unit);
				
				fetchAllocatedCargoList(customer_cd, fgsa, fgsa_rev, sn, cont_type,start_dt,rate_unit);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	Vector VCONTRACT_DETAIL = new Vector();
	Vector VFGSA = new Vector();
	Vector VFGSA_REV = new Vector();
	Vector VSN = new Vector();
	Vector VSN_REV = new Vector();
	Vector VCONTRACT_TYPE = new Vector();
	Vector VCONTRACT_TYPE_NM = new Vector();
	Vector VSEGMENT = new Vector();
	Vector VCONT_ST_DT = new Vector();
	Vector VCONT_END_DT = new Vector();
	Vector VSALES_RATE = new Vector();
	Vector VINDEX = new Vector();
	Vector VCARGO_NO = new Vector();
	Vector VCARGO_SEQ = new Vector();
	Vector VPUR_MAP_ID = new Vector();
	Vector VCOST_PRICE = new Vector();
	Vector VALLOC_QTY = new Vector();
	Vector VMARGIN = new Vector();
	Vector VTOTAL_MARGIN = new Vector();
	Vector VRATE_UNIT = new Vector();
	Vector VCP_UNIT = new Vector();
	Vector VCP_UNIT_NM = new Vector();
	
	Vector VNEW_SALES_RATE = new Vector();
	Vector VNEW_MARGIN = new Vector();
	Vector VNEW_TOTAL_MARGIN = new Vector();
	
	Vector VMODIFICATION_SEQ_NO = new Vector();
	Vector VPRICE_EFF_DT = new Vector();
	Vector VPRICE_FLAG = new Vector();
	
	Vector VCARGO_SN_REV = new Vector();
	Vector VPRICE_CHANGE_HISTORY = new Vector();
	Vector VPRICE_TYPE = new Vector();
	Vector VMapp_Id = new Vector();
	Vector VLAST_EFF_DT = new Vector();
	
	Vector VCARGO_ALLOC_INDEX = new Vector();
	
	public Vector getVCONTRACT_DETAIL() {return VCONTRACT_DETAIL;}
	public Vector getVFGSA() {return VFGSA;}
	public Vector getVFGSA_REV() {return VFGSA_REV;}
	public Vector getVSN() {return VSN;}
	public Vector getVSN_REV() {return VSN_REV;}
	public Vector getVCONTRACT_TYPE() {return VCONTRACT_TYPE;}
	public Vector getVCONTRACT_TYPE_NM() {return VCONTRACT_TYPE_NM;}
	public Vector getVSEGMENT() {return VSEGMENT;}
	public Vector getVCONT_ST_DT() {return VCONT_ST_DT;}
	public Vector getVCONT_END_DT() {return VCONT_END_DT;}
	public Vector getVSALES_RATE() {return VSALES_RATE;}
	public Vector getVINDEX() {return VINDEX;}
	public Vector getVCARGO_NO() {return VCARGO_NO;}
	public Vector getVCARGO_SEQ() {return VCARGO_SEQ;}
	public Vector getVPUR_MAP_ID() {return VPUR_MAP_ID;}
	public Vector getVCOST_PRICE() {return VCOST_PRICE;}
	public Vector getVALLOC_QTY() {return VALLOC_QTY;}
	public Vector getVMARGIN() {return VMARGIN;}
	public Vector getVTOTAL_MARGIN() {return VTOTAL_MARGIN;}
	public Vector getVRATE_UNIT() {return VRATE_UNIT;}
	public Vector getVCP_UNIT() {return VCP_UNIT;}
	public Vector getVCP_UNIT_NM() {return VCP_UNIT_NM;}
	
	public Vector getVNEW_SALES_RATE() {return VNEW_SALES_RATE;}
	public Vector getVNEW_MARGIN() {return VNEW_MARGIN;}
	public Vector getVNEW_TOTAL_MARGIN() {return VNEW_TOTAL_MARGIN;}
	public Vector getVMODIFICATION_SEQ_NO() {return VMODIFICATION_SEQ_NO;}
	public Vector getVPRICE_EFF_DT() {return VPRICE_EFF_DT;}
	public Vector getVPRICE_FLAG() {return VPRICE_FLAG;}
	public Vector getVCARGO_SN_REV() {return VCARGO_SN_REV;}
	public Vector getVPRICE_CHANGE_HISTORY() {return VPRICE_CHANGE_HISTORY;}
	public Vector getVPRICE_TYPE() {return VPRICE_TYPE;}
	public Vector getVMapp_Id() {return VMapp_Id;}
	public Vector getVLAST_EFF_DT() {return VLAST_EFF_DT;}
	public Vector getVCARGO_ALLOC_INDEX() {return VCARGO_ALLOC_INDEX;}
	
	public String getPriceUnitName(String price_unit)
	{
		String function_nm="getPriceUnitName()";

		String price_unit_nm="";
		try
		{
			if(price_unit.equals("1"))
			{
				price_unit_nm = "(INR/MMBTU)";
			}
			else
			{
				price_unit_nm = "($/MMBTU)";
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return price_unit_nm;
	}
	
	//PB20250127: FOR EXTRACTING THE PURCHASE CONTRACT DETAILS 
	public String getPurchaseDTL(String comp_cd, String cont_no, String cargo_no)
	{
		String function_nm="getPurchaseDTL()";
		String purchase_map_id = ""; 
		try
		{
			//added for handling the pseudo cargo 
			if(cont_no.equals("0") && !cargo_no.equals("0"))		//cont_no =0, counterparty_cd=0 and cargo_no!=0 then pseudo cargo
			{
				String query="SELECT COMPANY_CD, CONTRACT_TYPE, CARGO_NO "
						+ "FROM FMS_PSEUDO_CARGO_DTL "
						+ "WHERE COMPANY_CD=? AND CARGO_NO=?";
				stmt2 = conn.prepareStatement(query);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, cargo_no);
				rset2 = stmt2.executeQuery();
				if(rset2.next())
				{
					String cont_type = rset2.getString(2)==null?"":rset2.getString(2);
					String cont_nm="";
					if(cont_type.equals("D"))
					{
						cont_nm="Domestic NG";
					}
					else if(cont_type.equals("I"))
					{
						cont_nm="IGX";
					}
					else if(cont_type.equals("N"))
					{
						cont_nm="LNG";
					}
					else if(cont_type.equals("T"))
					{
						cont_nm="In Tank LNG|RLNG";
					}
					purchase_map_id=""+comp_cd+""+cont_type+"-"+cargo_no;
					purchase_map_id+="<br>"+"["+purchase_map_id+"]";
				}
				rset2.close();
				stmt2.close();
			}
			//for own volume 
			else if(cont_no.equals("0") && cargo_no.equals("0"))		//values similar to pseudo cargo but here cargo_no is also 0
			{
				//purchase_map_id=cargo_no;
				purchase_map_id="Own Volume";
				purchase_map_id+="<br>"+"["+purchase_map_id+"]";
			}
			else
			{
				String query1 = "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, "
						+ "CONT_NO,CONT_REV,CONTRACT_TYPE,0 AS CARGO_NO,CASE WHEN CONTRACT_TYPE='I' THEN TRADE_REF_NO ELSE CONT_REF_NO END "
						+ "FROM FMS_TRADER_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND CONT_NO=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM  FMS_TRADER_CONT_MST B WHERE  "
						+ "A.COMPANY_CD = B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV = B.AGMT_REV AND  "
						+ "A.CONT_NO = B.CONT_NO AND A.CONTRACT_TYPE = B.CONTRACT_TYPE) "
						+ "UNION  "
						+ "SELECT COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, "
						+ "CONT_NO,CONT_REV,CONTRACT_TYPE,CARGO_NO,CARGO_REF "
						+ "FROM FMS_TRADER_CARGO_MST A "
						+ "WHERE COMPANY_CD=? AND CONT_NO=? AND CARGO_NO=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM  FMS_TRADER_CARGO_MST B WHERE  "
						+ "A.COMPANY_CD = B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV = B.AGMT_REV AND  "
						+ "A.CONT_NO = B.CONT_NO AND A.CONTRACT_TYPE = B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO)";
				stmt1 = conn.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, cont_no);
				stmt1.setString(3, comp_cd);
				stmt1.setString(4, cont_no);
				stmt1.setString(5, cargo_no);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					// String cmp_cd = rset1.getString(1)==null?"":rset1.getString(1);
					String counterparty_cd = rset1.getString(2)==null?"":rset1.getString(2);
					String agmt_no = rset1.getString(3)==null?"":rset1.getString(3);
					String agmt_rev = rset1.getString(4)==null?"":rset1.getString(4);
					// String cont_no = rset1.getString(5)==null?"":rset1.getString(5);
					String cont_rev = rset1.getString(6)==null?"":rset1.getString(6);
					String cont_type = rset1.getString(7)==null?"":rset1.getString(7);
					// String cargo_no = rset1.getString(8)==null?"":rset1.getString(8);
					String cont_ref=rset1.getString(9)==null?"":rset1.getString(9);
					purchase_map_id = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev, cont_no, cont_rev, cont_type,cargo_no);
					purchase_map_id+="<br>"+"["+cont_ref+"]";
				}
				rset1.close();
				stmt1.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return purchase_map_id;
	}
	
	
	
	
	public void fetchAllocatedCargoList(String customer_cd, String fgsa, String fgsa_rev, String sn, String cont_type, String start_dt, String rate_unit) throws Exception
	{
		String function_nm="fetchAllocatedCargoList()";

		try
		{
			int index=0;
			Vector temp_cargo = new Vector();
			Vector temp_cargo_seq = new Vector();
			Vector temp_sn_rev_cargo = new Vector();
			temp_cargo.clear();
			temp_cargo_seq.clear();
		
			queryString1="SELECT PUR_CONT_NO,ALLOC_QTY,SALE_PRICE,COST_PRICE,MARGIN,CONT_REV,TOTAL_MARGIN,CP_UNIT,CARGO_NO "
					+ "FROM FMS_SUPPLY_PURCHASE_MAP_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
					+ "AND AGMT_REV=? AND CONT_NO=? "
					+ "ORDER BY PUR_CONT_NO,ENT_DT ";
			stmt_tmp = conn.prepareStatement(queryString1);
			stmt_tmp.setString(1, comp_cd);
			stmt_tmp.setString(2, customer_cd);
			stmt_tmp.setString(3, fgsa);
			stmt_tmp.setString(4, fgsa_rev);
			stmt_tmp.setString(5, sn);
			rset_tmp=stmt_tmp.executeQuery();
			while(rset_tmp.next())
			{
				index+=1;
				String contNo =rset_tmp.getString(1)==null?"":rset_tmp.getString(1);
				VCARGO_NO.add(contNo);
				temp_cargo.add(contNo);
				VALLOC_QTY.add(nf.format(rset_tmp.getDouble(2)));
				VCOST_PRICE.add(nf2.format(rset_tmp.getDouble(4)));
				VMARGIN.add(nf2.format(rset_tmp.getDouble(5)));
				VCARGO_SN_REV.add(rset_tmp.getString(6)==null?"":rset_tmp.getString(6));
				temp_sn_rev_cargo.add(rset_tmp.getString(6)==null?"":rset_tmp.getString(6));
				VTOTAL_MARGIN.add(nf2.format(rset_tmp.getDouble(7)));
				String price_unit = rset_tmp.getString(8)==null?"2":rset_tmp.getString(8);
				VCP_UNIT.add(price_unit);
				VCP_UNIT_NM.add(""+getPriceUnitName(price_unit));
				
				String cargoNo = rset_tmp.getString(9)==null?"0":rset_tmp.getString(9); 
				VCARGO_SEQ.add(cargoNo);
				temp_cargo_seq.add(cargoNo);
				
				//String pur_map = contNo;
				String pur_map = getPurchaseDTL(comp_cd, contNo, cargoNo);
				/*
				 * if(!cargoNo.equals("") && !cargoNo.equals("0")) { pur_map=contNo+"-"+cargoNo;
				 * }
				 */
				VPUR_MAP_ID.add(pur_map);
			}
			rset_tmp.close();
			stmt_tmp.close();
			
			VINDEX.add(index);
			
			int count_entry=0;
					
			queryString1 = "SELECT NVL(MODIFICATION_SEQ_NO,?),TO_CHAR(NEW_PRICE_EFF_DT,'DD/MM/YYYY'),FLAG "
					+ "FROM FMS_SUPPLY_ALLOC_REVISED "
					+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO = ? "
					+ "AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE=? "
					+ "ORDER BY MODIFICATION_SEQ_NO DESC";
			stmt_tmp1 = conn.prepareStatement(queryString1);
			stmt_tmp1.setString(1, "0");
			stmt_tmp1.setString(2, comp_cd);
			stmt_tmp1.setString(3, fgsa);
			stmt_tmp1.setString(4, fgsa_rev);
			stmt_tmp1.setString(5, sn);
			stmt_tmp1.setString(6, customer_cd);
			stmt_tmp1.setString(7, cont_type);
			rset_tmp1=stmt_tmp1.executeQuery();
			if(rset_tmp1.next())
			{
				String SeqNo = ""+rset_tmp1.getInt(1);
				VMODIFICATION_SEQ_NO.add(SeqNo);
				
				String flg = rset_tmp1.getString(3)==null?"":rset_tmp1.getString(3);
				if(flg.equals("R")){
					VPRICE_EFF_DT.add(rset_tmp1.getString(2)==null?"":rset_tmp1.getString(2));
				}else{
					VPRICE_EFF_DT.add("");
				}
				VPRICE_FLAG.add(rset_tmp1.getString(3)==null?"":rset_tmp1.getString(3));
				
				for(int i=0; i<temp_cargo.size(); i++)
				{
					queryString2 = "SELECT NEW_SALE_PRICE,NEW_MARGIN,NEW_AVG_MARGIN,NEW_TOT_MARGIN "
							+ "FROM FMS_SUPPLY_ALLOC_REVISED "
							+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO = ? AND CONT_REV=? "
							+ "AND COUNTERPARTY_CD = ? AND CONTRACT_TYPE=? AND MODIFICATION_SEQ_NO=? "
							+ "AND PUR_CONT=? AND FLAG=? AND CARGO_NO=? ";
					stmt_tmp2 = conn.prepareStatement(queryString2);
					stmt_tmp2.setString(1, comp_cd);
					stmt_tmp2.setString(2, fgsa);
					stmt_tmp2.setString(3, fgsa_rev);
					stmt_tmp2.setString(4, sn);
					stmt_tmp2.setString(5, ""+temp_sn_rev_cargo.elementAt(i));
					stmt_tmp2.setString(6, customer_cd);
					stmt_tmp2.setString(7, cont_type);
					stmt_tmp2.setString(8, SeqNo);
					stmt_tmp2.setString(9, ""+temp_cargo.elementAt(i));
					stmt_tmp2.setString(10, "R");
					stmt_tmp2.setString(11, ""+temp_cargo_seq.elementAt(i));
					rset_tmp2=stmt_tmp2.executeQuery();
					if(rset_tmp2.next())
					{
						count_entry+=1;
						VNEW_SALES_RATE.add(nf2.format(rset_tmp2.getDouble(1)));
						VNEW_MARGIN.add(nf2.format(rset_tmp2.getDouble(2)));
						VNEW_TOTAL_MARGIN.add(nf2.format(rset_tmp2.getDouble(4)));
					}
					else
					{
						VNEW_SALES_RATE.add("");
						VNEW_MARGIN.add("");
						VNEW_TOTAL_MARGIN.add("");
					}
					rset_tmp2.close();
					stmt_tmp2.close();
				}
			}
			else
			{
				VMODIFICATION_SEQ_NO.add("0");
				VPRICE_EFF_DT.add("");
				VPRICE_FLAG.add("");
				
				for(int i=0; i<temp_cargo.size(); i++)
				{
					VNEW_SALES_RATE.add("");
					VNEW_MARGIN.add("");
					VNEW_TOTAL_MARGIN.add("");
				}
			}
			rset_tmp1.close();
			stmt_tmp1.close();
			
			VCARGO_ALLOC_INDEX.add(count_entry);
			
			//FOR DISPLAY PURPOSE
			String history="";
			String last_eff_dt="";
			int count=0;
			
			queryString1 = "SELECT DISTINCT MODIFICATION_SEQ_NO, NEW_SALE_PRICE,TO_CHAR(NEW_PRICE_EFF_DT,'DD/MM/YYYY'),FLAG,ORI_SALE_PRICE "
					+ "FROM FMS_SUPPLY_ALLOC_REVISED "
					+ "WHERE COMPANY_CD=? AND AGMT_NO=? AND AGMT_REV=? AND CONT_NO = ? AND COUNTERPARTY_CD = ? "
					+ "AND CONTRACT_TYPE=? ORDER BY MODIFICATION_SEQ_NO DESC";//GROUP BY NEW_SALE_PRICE,NEW_PRICE_EFF_DT";
			stmt_tmp1 = conn.prepareStatement(queryString1);
			stmt_tmp1.setString(1, comp_cd);
			stmt_tmp1.setString(2, fgsa);
			stmt_tmp1.setString(3, fgsa_rev);
			stmt_tmp1.setString(4, sn);
			stmt_tmp1.setString(5, customer_cd);
			stmt_tmp1.setString(6, cont_type);
			rset_tmp1=stmt_tmp1.executeQuery();
			while(rset_tmp1.next())
			{
				double sales = rset_tmp1.getDouble(2);
				double ori_sales = rset_tmp1.getDouble(5);
				String dt =rset_tmp1.getString(3)==null?"":rset_tmp1.getString(3);
				String flag=rset_tmp1.getString(4)==null?"":rset_tmp1.getString(4);
				String color="";
				if(flag.equals("X")) {
					flag="Rejected";
					color="red";
				}else if(flag.equals("A")) {
					flag="Approved";
					color="green";
					count+=1;
				}else {
					flag="Requested";
					color="blue";
				}
				
				String rateLable = "($/MMBTU)";
				if(rate_unit.trim().equals("1")) {
					rateLable = "(INR/MMBTU)";
				}
				if(history.equals(""))
				{
					history+=""+nf2.format(sales)+ rateLable+" From "+dt+" <font color='"+color+"'>"+flag+"</font>";
				}
				else
				{
					history+="<br>"+nf2.format(sales)+ rateLable+" From "+dt+" <font color='"+color+"'>"+flag+"</font>";
				}
			}
			rset_tmp1.close();
			stmt_tmp1.close();
			
			VPRICE_CHANGE_HISTORY.add(history);
			VLAST_EFF_DT.add(last_eff_dt);
			
			String Mapp_Id=customer_cd+"-"+fgsa+"-"+fgsa_rev+"-"+sn+"-%";
			VMapp_Id.add(Mapp_Id);
			/*if(segment.equals("DLNG")) 
			{
				if(cont_type.equals("L"))
				{
					cont_type="F";
				}
				else
				{
					cont_type="E";
				}
			}*/
			String price_type="";
			/*queryString1 ="SELECT PRICE_TYPE, CURVE_NM, RATE, SEQ_NO, PHYS_CURVE_NM, REMARKS, SLOPE, CONST  FROM FMS9_MRCR_CONT_PRICE_DTL " 
					+ "WHERE MAPPING_ID LIKE '"+Mapp_Id+"' AND CONTRACT_TYPE='"+cont_type+"' AND FLAG='Y' "; 
		  	rset_tmp = stmt_tmp.executeQuery(queryString1);			
			if(rset_tmp.next())
			{
				price_type=rset_tmp.getString(1)==null?"":rset_tmp.getString(1);
			}
			
			if(price_type.equals("M"))
			{
				price_type="Float";
			}
			else
			{
				price_type="Fixed";
			}*/
			VPRICE_TYPE.add(price_type);
		}
		catch (Exception e) 
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			throw e;
		}
	}
	double exchgRate = 0;
	public double getExchgRate() {return exchgRate;}
	public void getExchangeRate()
	{
		String function_nm="getExchangeRate()";

		try
		{
			String exchg_rate_cd = "",exchg_rate_nm = "";
			String rate_nm="Shell Treasury Rate";
			
			queryString = "SELECT EXC_RATE_NM,EXC_RATE_CD "
					+ "FROM FMS_EXCHG_RATE_MST "
					+ "WHERE UPPER(EXC_RATE_NM) = ?"; 
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, rate_nm.toUpperCase());
			rset=stmt.executeQuery();
			if(rset.next())
			{
				exchg_rate_nm = rset.getString(1)==null?"N.A":rset.getString(1);
				exchg_rate_cd = rset.getString(2)==null?"":rset.getString(2);
			}
			rset.close();
			stmt.close();
			
			queryString = "SELECT EXCHG_VAL "
					+ "FROM FMS_EXCHG_RATE_ENTRY A "
					+ "WHERE EXCHG_RATE_CD=? "
					+ "AND EFF_DT =(SELECT MAX(B.EFF_DT) FROM FMS_EXCHG_RATE_ENTRY B "
					+ "WHERE A.EXCHG_RATE_CD=B.EXCHG_RATE_CD  "
					+ "AND B.EFF_DT <= TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) AND FLAG=?";
			stmt1 = conn.prepareStatement(queryString);
			stmt1.setString(1, exchg_rate_cd);
			stmt1.setString(2, "Y");
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				exchgRate = rset1.getDouble(1);
			}
			rset1.close();
			stmt1.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public String getCustomer_cd() {return customer_cd;}
	public void setCustomer_cd(String customer_cd) {this.customer_cd = customer_cd;}

	Vector VCUSTOMR_CD_MST = new Vector();
	Vector VCUSTOMR_NM_MST = new Vector();
	
	public Vector getVCUSTOMR_CD_MST() {return VCUSTOMR_CD_MST;}
	public Vector getVCUSTOMR_NM_MST() {return VCUSTOMR_NM_MST;}
}
