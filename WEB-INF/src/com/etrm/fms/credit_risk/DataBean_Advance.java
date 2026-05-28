package com.etrm.fms.credit_risk;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.XmlUtilBean;

//Coded By          : Harsh Patel
//Code Reviewed by	:
//CR Date			: 10/12/2024
//Status	  		: Developing
public class DataBean_Advance 
{
	String db_src_file_name="DataBean_Advance.java";
	
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
	PreparedStatement stmt_temp1;
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
	ResultSet rset_temp1;
	String queryString="";
	//String queryString1="";
	//String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";
	String queryString6="";
	String queryString_temp1="";
	
	UtilBean utilBean = new UtilBean();
	DateUtil dateUtil = new DateUtil();
	XmlUtilBean xmlUtil = new XmlUtilBean();
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf0 = new DecimalFormat("###########0.0");
	
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
	    			if(callFlag.equalsIgnoreCase("CASH_COLLATERAL_MANAGEMENT"))
	    			{
	    				getCounterpartyDetails();
	    				if(clearance.equals("I") && VMST_COUNTERPARTY_CD.size()==1)
	    				{
	    					counterparty_cd=""+VMST_COUNTERPARTY_CD.elementAt(0);
	    				}
	    				getContractDetail();
	    				getAdvanceSecurityDetails();
	    			}
	    			else if(callFlag.equalsIgnoreCase("ADV_SAP_XML"))
					{
						generateAdvanceXML();
						parseSAP_XMLfile();
					}
					else if(callFlag.equalsIgnoreCase("GENERATE_ADV_SAP_XML"))
					{
						generateAdvanceXML();
					}
					else if(callFlag.equalsIgnoreCase("PARSE_SAP_XML"))
					{
						parseSAP_XMLfile();
						getPostingDetail();
					}
					else if(callFlag.equalsIgnoreCase("VIEW_REMITTANCE")) //HP20231204
					{
						getSecurityDetailForViewRemittance();
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
	    	if(rset_temp1 != null){try{rset_temp1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
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
	    	if(stmt_temp1 != null){try{stmt_temp1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}
	
	public void getCounterpartyDetails() 
	{
		String function_nm="getCounterpartyDetails()";
		try
		{
			if(clearance.equals("I"))
			{
				queryString = "SELECT COUNTERPARTY_CD,COUNTERPARTY_NM,COUNTERPARTY_ABBR "
						+ "FROM FMS_COMPANY_EXCHG_MST A "
						+ "WHERE EFF_DT=(SELECT MAX(EFF_DT) FROM FMS_COMPANY_EXCHG_MST B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD) "
						+ "ORDER BY COUNTERPARTY_NM";
			}
			else
			{
				queryString = "SELECT COUNTERPARTY_CD,COUNTERPARTY_NM,COUNTERPARTY_ABBR "
						+ "FROM FMS_COUNTERPARTY_MST A "
						+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_MST B "
						+ "WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD) "
						+ "AND KYC=? " //STATUS=? AND
						+ "ORDER BY COUNTERPARTY_NM";
			}
			stmt = conn.prepareStatement(queryString);
			if(clearance.equals("I"))
			{
			}
			else
			{
				stmt.setString(1, "Y");
				//stmt.setString(2, "Y");
			}
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String counterpty_cd = rset.getString(1)==null?"":rset.getString(1);
				VMST_COUNTERPARTY_CD.add(counterpty_cd);
				VMST_COUNTERPARTY_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VMST_COUNTERPARTY_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getContractDetail()
	{
		String function_nm="getContractDetail()";
		try
		{
			int cont1=0;
			String queryString2 = "SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),COUNTERPARTY_CD,"
					+ "CONT_REF_NO,TRADE_REF_NO,'GAS','Sell' "
					+ "FROM FMS_SUPPLY_CONT_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
					+ "AND (END_DT+180) >=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') ";
			queryString2+=clearance.equals("I")? "AND CONTRACT_TYPE IN (?,?) ":"AND COUNTERPARTY_CD=? AND CONTRACT_TYPE NOT IN (?,?) ";
			queryString2+=" UNION ALL ";
			queryString2+="SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),COUNTERPARTY_CD,"
					+ "CONT_REF_NO,TRADE_REF_NO,'GAS','Buy' "
					+ "FROM FMS_TRADER_CONT_MST A "
					+ "WHERE COMPANY_CD=? "
					+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
					+ "AND (END_DT + 180) >=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') ";
			queryString2+=clearance.equals("I")?"AND CONTRACT_TYPE=? ":"AND COUNTERPARTY_CD=? AND CONTRACT_TYPE NOT IN (?) ";
			queryString2+=" UNION ALL ";
			queryString2+="SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),COUNTERPARTY_CD,"
					+ "CONT_REF_NO,NULL,'GAS','Buy' "
					+ "FROM FMS_TRADER_CN_MST A  "
					+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND COUNTERPARTY_CD=? "
					+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD  "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
					+ "AND (END_DT + 180) >=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND 'K'=? ";
			queryString2+=" UNION ALL ";
			queryString2+="SELECT AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,TO_CHAR(START_DT,'DD/MM/YYYY'),TO_CHAR(END_DT,'DD/MM/YYYY'),COUNTERPARTY_CD,"
					+ "CONT_REF_NO,NULL,'LTCORA',CASE WHEN A.CONTRACT_TYPE='G' OR A.CONTRACT_TYPE='P' THEN 'Buy' ELSE CASE WHEN A.CONTRACT_TYPE='O' OR A.CONTRACT_TYPE='Q' THEN 'Sell' ELSE NULL END END "
					+ "FROM FMS_LTCORA_CONT_MST A   "
					+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE IN (?,?,?,?) AND COUNTERPARTY_CD=? AND AGMT_TYPE IN (?,?)"
					+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD   "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.AGMT_TYPE=B.AGMT_TYPE) "
					+ "AND (END_DT + 180) >=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND 'K'=? ";
			String temp_queryString2=queryString2;
			stmt2 = conn.prepareStatement(temp_queryString2);
			stmt2.setString(++cont1, comp_cd);
			if(clearance.equals("I"))
			{
				stmt2.setString(++cont1, "X");
				stmt2.setString(++cont1, "W");
			}
			else
			{
				stmt2.setString(++cont1, counterparty_cd);
				stmt2.setString(++cont1, "X");
				stmt2.setString(++cont1, "W");
			}
			stmt2.setString(++cont1, comp_cd);
			if(clearance.equals("I"))
			{
				stmt2.setString(++cont1, "I");
			}
			else
			{
				stmt2.setString(++cont1, counterparty_cd);
				stmt2.setString(++cont1, "I");
			}
			stmt2.setString(++cont1,comp_cd);
			stmt2.setString(++cont1,"N");
			stmt2.setString(++cont1, counterparty_cd);
			stmt2.setString(++cont1,clearance);
			stmt2.setString(++cont1, comp_cd);
			stmt2.setString(++cont1, "G");
			stmt2.setString(++cont1, "P");
			stmt2.setString(++cont1, "O");
			stmt2.setString(++cont1, "Q");
			stmt2.setString(++cont1, counterparty_cd);
			stmt2.setString(++cont1, "A");
			stmt2.setString(++cont1, "L");
			stmt2.setString(++cont1,clearance);
			rset2 = stmt2.executeQuery();
			while(rset2.next())
			{
				String agmt = rset2.getString(1)==null?"0":rset2.getString(1);
				String agmt_rev = "0";//rset2.getString(2)==null?"0":rset2.getString(2);
				String cont = rset2.getString(3)==null?"0":rset2.getString(3);
				String cont_rev = "0";//rset2.getString(4)==null?"0":rset2.getString(4);
				String cont_type = rset2.getString(5)==null?"":rset2.getString(5);
				String start_dt = rset2.getString(6)==null?"":rset2.getString(6);
				String to_dt = rset2.getString(7)==null?"":rset2.getString(7);
				String countpty_cd = rset2.getString(8)==null?"":rset2.getString(8);
				String cont_ref = rset2.getString(9)==null?"":rset2.getString(9);
				if(clearance.equals("I"))
				{
					cont_ref= rset2.getString(10)==null?"":rset2.getString(10);
				}
				String dealType = rset2.getString(11)==null?"":rset2.getString(11);
				String cont_buy_sell = rset2.getString(12)==null?"":rset2.getString(12);
				
				String abbr=""+utilBean.getCounterpartyABBR(conn,countpty_cd);
				String dealno=utilBean.NewDealMappingId(comp_cd, countpty_cd, agmt, agmt_rev, cont, cont_rev, cont_type,"");
				
				VAGMT_NO.add(agmt);
				VAGMT_REV_NO.add(agmt_rev);
				VCONT_NO.add(cont);
				VCONT_REV_NO.add(cont_rev);
				VCONTRACT_TYPE.add(cont_type);
				VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_type));

				VCONTRACT_MAPPING.add(countpty_cd+"-"+cont_type+"-"+agmt+"-"+agmt_rev+"-"+cont+"-"+cont_rev);
				VCONTRACT_MAPPING_DIS.add(abbr+" - "+dealno+" ["+cont_ref+"] <label style='color:blue;'>("+start_dt+" - "+to_dt+")</label>");
				VCONTRACT_MAPPING_DIS_1.add(abbr+" - "+dealno+" ["+cont_ref+"]");
				VCONTRACT_EXPIRED.add("N");
				VCONTRACT_DEAL_TYPE.add(dealType);
				VCONT_BUY_SELL.add(cont_buy_sell);
			}
			rset2.close();
			stmt2.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getAdvanceSecurityDetails()
	{
		String function_nm="getAdvanceSecurityDetails()";
		try
		{
			int st_count=0;
			queryString = "SELECT COUNTERPARTY_CD,SEC_TYPE,SEC_REF_NO,STATUS,CURRENCY,VALUE,TO_CHAR(RECEIPT_DT,'DD/MM/YYYY'),SEQ_NO,"
					+ "DEAL_TYPE,REMARKS,SEQ_REV_NO,PG_REF,SAP_APPROVAL,CR_DR,TDS_AMT,TDS_STRUCT_CD,BU_UNIT,PLANT_SEQ,TAX_AMT,TAX_STRUCT_CD,GROSS_AMT,RECPT_SEC_REF "
					+ "FROM FMS_SECURITY_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEC_TYPE IN (?,?) AND GX=? "
					+ "ORDER BY ENT_DT DESC ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(++st_count, comp_cd);
			stmt.setString(++st_count, counterparty_cd);
			stmt.setString(++st_count, "ADV");
			stmt.setString(++st_count, "DPT");
			stmt.setString(++st_count, clearance);
			rset = stmt.executeQuery();
			while(rset.next())
			{
				String counterPartyCd = rset.getString(1)==null?"":rset.getString(1);
				String secType=rset.getString(2)==null?"":rset.getString(2);
				String sec_ref_no =  rset.getString(3)==null?"":rset.getString(3);
				String currency= rset.getString(5)==null?"":rset.getString(5);
				double value = rset.getDouble(6); 
				String seq_no = rset.getString(8)==null?"":rset.getString(8);
				String seq_rev_no = rset.getString(11)==null?"":rset.getString(11);
				double tds_amt = rset.getDouble(15);
				double total_value = value + tds_amt;
				
				VCOUNTERPARTY_CD.add(counterPartyCd);
				if(clearance.equals("I"))
				{
					VCOUNTERPARTY_NAME.add(""+utilBean.getGasExchangeName(conn,counterPartyCd));
					VCOUNTERPARTY_ABBR.add(""+utilBean.getGasExchangeAbbr(conn,counterPartyCd));
				}
				else
				{
					VCOUNTERPARTY_NAME.add(""+utilBean.getCounterpartyName(conn,counterPartyCd));
					VCOUNTERPARTY_ABBR.add(""+utilBean.getCounterpartyABBR(conn,counterPartyCd));
				}
				VSEC_TYPE.add(secType);
				VSEC_REF_NO.add(comp_cd+"-"+sec_ref_no);
				VTEMP_SEC_REF_NO.add(sec_ref_no);
				VSTATUS.add(rset.getString(4)==null?"":rset.getString(4));
				VCURRENCY.add(currency);
				VCURRENCY_NM.add(utilBean.getRateUnitNm(conn,currency));
				VVALUE.add(rset.getString(6)==null?"":nf.format(rset.getDouble(6)));
				VRECEIVED_DATE.add(rset.getString(7)==null?"":rset.getString(7));
				VSEQ_NO.add(seq_no);
				VDEAL_TYPE.add(rset.getString(9)==null?"":rset.getString(9));
				VREMARK.add(rset.getString(10)==null?"":rset.getString(10));
				VSEQ_REV_NO.add(seq_rev_no);
				VADV_PG_REF.add(rset.getString(12)==null?"":rset.getString(12));
				
				String sap_aprv_flag=rset.getString(13)==null?"":rset.getString(13);
				String cr_dr=rset.getString(14)==null?"":rset.getString(14);
				String cr_dr_nm=cr_dr.equals("CR")?"Credit":cr_dr.equals("DR")?"Debit":"";
				VCRDR.add(cr_dr);
				VCRDR_NM.add(cr_dr_nm);
				VTDS_AMT.add(rset.getString(15)==null?"":nf.format(rset.getDouble(15)));
				String tds_struct_cd=rset.getString(16)==null?"":rset.getString(16);
				VTDS_STRUCT_CD.add(tds_struct_cd);
				VTDS_STRUCT_INFO.add(utilBean.getTaxDescr(conn,tds_struct_cd));
				VTOTAL_VALUE.add(nf.format(total_value));
				
				VBU_UNIT.add(rset.getString(17)==null?"":rset.getString(17));
				VPLANT_SEQ.add(rset.getString(18)==null?"":rset.getString(18));
				
				VTAX_AMT.add(rset.getString(19)==null?"":nf.format(rset.getDouble(19)));
				String tax_struct_cd=rset.getString(20)==null?"":rset.getString(20);
				VTAX_STRUCT_CD.add(tax_struct_cd);
				VTAX_STRUCT_DTL.add(utilBean.getTaxDescr(conn,tax_struct_cd));
				VGROSS_AMT.add(rset.getString(21)==null?"":nf.format(rset.getDouble(21)));
				VATT_RECEIPT_VOUCHER.add(rset.getString(22)==null?"":rset.getString(22));
				
				String dealNo = "";
				String deal_mapping = "";
				String buySell = "";
				String cont_type = "";
				String cont_ref = "";
				String temp_split_value = "";
				String temp_split_by = "";
				String temp_cont_ref = "";
				st_count=0;
				String queryString1 = "SELECT A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.COUNTERPARTY_CD,"
						+ "A.CONT_REF_NO,A.TRADE_REF_NO,'GAS','Sell',B.SHARE_PERCENT,B.SPLIT_BY "
						+ "FROM FMS_SUPPLY_CONT_MST A, FMS_SECURITY_DEAL_MAP B "
						+ "WHERE A.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.GX=? AND B.SEQ_NO=? AND B.SEQ_REV_NO=? "
						+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE ";
				queryString1+=clearance.equals("I")?"AND A.COUNTERPARTY_CD=B.ENTITY_CD ":"AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD ";
				queryString1+=" UNION ALL ";
				queryString1+= "SELECT A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.COUNTERPARTY_CD,"
						+ "A.CONT_REF_NO,A.TRADE_REF_NO,'GAS','Buy',B.SHARE_PERCENT,B.SPLIT_BY "
						+ "FROM FMS_TRADER_CONT_MST A, FMS_SECURITY_DEAL_MAP B "
						+ "WHERE A.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.GX=? AND B.SEQ_NO=? AND B.SEQ_REV_NO=? "
						+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE ";
				queryString1+=clearance.equals("I")?"AND A.COUNTERPARTY_CD=B.ENTITY_CD ":"AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD ";
				queryString1+=" UNION ALL ";
				queryString1+= "SELECT A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.COUNTERPARTY_CD,"
						+ "A.CONT_REF_NO,NULL,'GAS','Buy',B.SHARE_PERCENT,B.SPLIT_BY "
						+ "FROM FMS_TRADER_CN_MST A, FMS_SECURITY_DEAL_MAP B  "
						+ "WHERE A.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.GX=? AND B.SEQ_NO=? AND B.SEQ_REV_NO=? "
						+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD  "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE ";
				queryString1+=clearance.equals("I")?"AND A.COUNTERPARTY_CD=B.ENTITY_CD ":"AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD ";
				queryString1+=" UNION ALL ";
				queryString1+= "SELECT A.AGMT_NO,A.AGMT_REV,A.CONT_NO,A.CONT_REV,A.CONTRACT_TYPE,TO_CHAR(A.START_DT,'DD/MM/YYYY'),TO_CHAR(A.END_DT,'DD/MM/YYYY'),A.COUNTERPARTY_CD,"
						+ "A.CONT_REF_NO,NULL,'LTCORA',CASE WHEN A.CONTRACT_TYPE='G' OR A.CONTRACT_TYPE='P' THEN 'Buy' ELSE CASE WHEN A.CONTRACT_TYPE='O' OR A.CONTRACT_TYPE='Q' THEN 'Sell' ELSE NULL END END,B.SHARE_PERCENT,B.SPLIT_BY "
						+ "FROM FMS_LTCORA_CONT_MST A, FMS_SECURITY_DEAL_MAP B "
						+ "WHERE A.COMPANY_CD=? AND B.COUNTERPARTY_CD=? AND B.GX=? AND B.SEQ_NO=? AND B.SEQ_REV_NO=? "
						+ "AND A.CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD   "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE ";
				queryString1+=clearance.equals("I")?"AND A.COUNTERPARTY_CD=B.ENTITY_CD ":"AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD ";
				String temp_queryString1=queryString1;
				stmt1 = conn.prepareStatement(temp_queryString1);
				stmt1.setString(++st_count, comp_cd);
				stmt1.setString(++st_count, counterPartyCd);
				stmt1.setString(++st_count, clearance);
				stmt1.setString(++st_count, seq_no);
				stmt1.setString(++st_count, seq_rev_no);
				stmt1.setString(++st_count, comp_cd);
				stmt1.setString(++st_count, counterPartyCd);
				stmt1.setString(++st_count, clearance);
				stmt1.setString(++st_count, seq_no);
				stmt1.setString(++st_count, seq_rev_no);
				stmt1.setString(++st_count, comp_cd);
				stmt1.setString(++st_count, counterPartyCd);
				stmt1.setString(++st_count, clearance);
				stmt1.setString(++st_count, seq_no);
				stmt1.setString(++st_count, seq_rev_no);
				stmt1.setString(++st_count, comp_cd);
				stmt1.setString(++st_count, counterPartyCd);
				stmt1.setString(++st_count, clearance);
				stmt1.setString(++st_count, seq_no);
				stmt1.setString(++st_count, seq_rev_no);
				rset1 = stmt1.executeQuery();
				while(rset1.next())
				{
					String agmt = rset1.getString(1)==null?"0":rset1.getString(1);
					String agmt_rev = "0";//rset1.getString(2)==null?"":rset1.getString(2);
					String cont = rset1.getString(3)==null?"":rset1.getString(3);
					String cont_rev = "0";//rset1.getString(4)==null?"":rset1.getString(4);
					cont_type = rset1.getString(5)==null?"":rset1.getString(5);
					String start_dt = rset1.getString(6)==null?"":rset1.getString(6);
					String to_dt = rset1.getString(7)==null?"":rset1.getString(7);
					String counterparty_cd = rset1.getString(8)==null?"":rset1.getString(8);
					cont_ref = rset1.getString(9)==null?"":rset1.getString(9);
					if(clearance.equals("I"))
					{
						cont_ref= rset1.getString(10)==null?"":rset1.getString(10);
					}
					String dealType = rset1.getString(11)==null?"":rset1.getString(11);
					String cont_buy_sell = rset1.getString(12)==null?"":rset1.getString(12);
					buySell=cont_buy_sell;
					
					String share_percent = rset1.getString(13)==null?"":nf.format(rset1.getDouble(13));
					String split_by = rset1.getString(14)==null?"P":rset1.getString(14);
					
					String abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);
					String tempDealNo=utilBean.NewDealMappingId(comp_cd,counterparty_cd, agmt, agmt_rev, cont, cont_rev, cont_type, "");
					dealNo=dealNo.equals("")?tempDealNo:dealNo+","+tempDealNo;
					
					String temp_dealMapping=counterparty_cd+"-"+cont_type+"-"+agmt+"-"+agmt_rev+"-"+cont+"-"+cont_rev;
					deal_mapping=deal_mapping.equals("")?temp_dealMapping:deal_mapping+"@"+temp_dealMapping;
					
					temp_split_value=temp_split_value.equals("")?share_percent:temp_split_value+"@"+share_percent;
					//temp_split_by=temp_split_by.equals("")?split_by:temp_split_by+"@"+split_by;
					temp_split_by=split_by;
					
					temp_cont_ref+=temp_cont_ref.equals("")?cont_ref:","+cont_ref;
					
					//if(!VCONTRACT_MAPPING.contains(deal_mapping))
					if(!VCONTRACT_MAPPING.contains(temp_dealMapping))
					{
						VAGMT_NO.add(agmt);
						VAGMT_REV_NO.add(agmt_rev);
						VCONT_NO.add(cont);
						VCONT_REV_NO.add(cont_rev);
						VCONTRACT_TYPE.add(cont_type);
						VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_type));
	
						VCONTRACT_MAPPING.add(counterparty_cd+"-"+cont_type+"-"+agmt+"-"+agmt_rev+"-"+cont+"-"+cont_rev);
						VCONTRACT_MAPPING_DIS.add(abbr+" - "+tempDealNo+" ["+cont_ref+"] <label style='color:blue;'>("+start_dt+" - "+to_dt+")</label>");
						VCONTRACT_MAPPING_DIS_1.add(abbr+" - "+tempDealNo+" ["+cont_ref+"]");
						VCONTRACT_EXPIRED.add("Y");
						VCONTRACT_DEAL_TYPE.add(dealType);
						VCONT_BUY_SELL.add(cont_buy_sell);
					}
				}
				rset1.close();
				stmt1.close();
				
				VDEAL_NO.add(dealNo);
				VDEAL_MAPPING.add(deal_mapping);
				VDEAL_CONT_REF.add(temp_cont_ref);
				VBUY_SELL.add(buySell);
				VSPLIT_VALUE.add(temp_split_value);
				VSPLIT_BY.add(temp_split_by);
				
				String pdf_generated = "N";
				String pdf_name="";
				String pdf_file_path="";
				String sec_int_ref="";
				
				if(cont_type.equals("S") || cont_type.equals("L") 
						|| cont_type.equals("E") || cont_type.equals("F") 
						|| ((cont_type.equals("O") || cont_type.equals("Q")) && secType.equals("DPT")))
				{
					pdf_generated = "X";
					
					if(sap_aprv_flag.equals(""))
					{
						if(cr_dr.equals("DR"))
						{
							sap_aprv_flag="Z";
						}
						else if(tds_amt <= 0)
						{
							sap_aprv_flag="Z";
						}
					}
				}
				/*else if()
				{
					pdf_generated = "X";
					
					if(sap_aprv_flag.equals(""))
					{
						if(cr_dr.equals("DR"))
						{
							sap_aprv_flag="Z";
						}
						else if(tds_amt <= 0)
						{
							sap_aprv_flag="Z";
						}
					}
				}*/
				else
				{
					queryString5 = "SELECT FILE_NAME,PDF_SIGNED,SEC_INT_REF "
							+ "FROM FMS_SECURITY_FILE_DTL "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
							+ "AND SEQ_REV_NO=? AND GX=? AND FILE_TYPE=?";
					stmt5 = conn.prepareStatement(queryString5);
					stmt5.setString(1, comp_cd);
					stmt5.setString(2, counterPartyCd);
					stmt5.setString(3, seq_no);
					stmt5.setString(4, seq_rev_no);
					stmt5.setString(5, clearance);
					stmt5.setString(6, "PDF");
					rset5 = stmt5.executeQuery();
					if(rset5.next())
					{
						pdf_generated = "Y";
						pdf_name = rset5.getString(1)==null?"":rset5.getString(1);
						String pdf_signed=rset5.getString(2)==null?"":rset5.getString(2);
						sec_int_ref=rset5.getString(3)==null?"":rset5.getString(3);
						if(pdf_signed.equals("Y"))
						{
							pdf_file_path=CommonVariable.signed_security_adv_path;
						}
						else
						{
							pdf_file_path=CommonVariable.security_adv_path;
						}
					}
					rset5.close();
					stmt5.close();
				}
				VPDF_GENERATED.add(pdf_generated);
				VPDF_FILE_NAME.add(pdf_name);
				VPDF_FILE_PATH.add(pdf_file_path);
				VSAP_APPROVAL_FLAG.add(sap_aprv_flag);
				VSEC_INT_REF.add(sec_int_ref);

				/*String pdf_rev_generated = "N";
				String rev_pdf_name="";
				queryString6 = "SELECT FILE_NAME "
						+ "FROM FMS_SECURITY_FILE_DTL "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND SEQ_NO=? "
						+ "AND SEQ_REV_NO=? AND GX=? AND FILE_TYPE=?";
				stmt6 = conn.prepareStatement(queryString6);
				stmt6.setString(1, comp_cd);
				stmt6.setString(2, counterPartyCd);
				stmt6.setString(3, seq_no);
				stmt6.setString(4, seq_rev_no);
				stmt6.setString(5, clearance);
				stmt6.setString(6, "PDF_REV");
				rset6 = stmt6.executeQuery();
				if(rset6.next())
				{
					pdf_rev_generated = "Y";
					rev_pdf_name = rset6.getString(1)==null?"":rset6.getString(1);
				}
				VPDF_REV_GENERATED.add(pdf_rev_generated);
				VPDF_REV_FILE_NAME.add(rev_pdf_name);

				rset6.close();
				stmt6.close();*/
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void generateAdvanceXML()
	{
		String function_nm="generateAdvanceXML()";
		try
		{
			String sysdate=dateUtil.getSysdate();
			String sysdateWithTime=dateUtil.getSysdateWithTime24hr();
			String xml_sysdate="";
			String postingMonth="";
			String[] split=sysdate.split("/");
			xml_sysdate=split[2]+""+split[1]+""+split[0];
			postingMonth=split[2]+""+split[1];

			String[] splitSys = sysdateWithTime.split(" ");
			String date_timestamp=xml_sysdate+" "+splitSys[1];

			String documentDate="";
			String accountingPeriodMonth="";
			String accountingPeriodYear="";
			if(!sysdate.equals(""))
			{
				String[] temp_split=sysdate.split("/");
				accountingPeriodMonth=temp_split[1];
				accountingPeriodYear=temp_split[2];	

				documentDate=temp_split[2]+""+temp_split[1]+""+temp_split[0];
			}

			String counterparty_abbr="";
			String counterparty_nm="";
			String account="";
			if(gx.equals("I"))
			{
				counterparty_abbr=utilBean.getGasExchangeAbbr(conn,counterparty_cd);
				counterparty_nm=utilBean.getGasExchangeName(conn,counterparty_cd);
				account=utilBean.getGasExchangeSAPcode(conn,counterparty_cd);
			}
			else
			{
				counterparty_abbr=utilBean.getCounterpartyABBR(conn,counterparty_cd);
				counterparty_nm=utilBean.getCounterpartyName(conn,counterparty_cd);
				account=utilBean.getCounterpartySAPcode(conn,counterparty_cd);
			}

			String agmt="";
			String agmt_rev="";
			String cont="";
			String cont_rev="";
			String cont_type="";
			String entity_cd="";
			String gx_bu_seq_no="";
			String bu_seq_no="";
			String receipt_dt="";
			String gross_amt="";
			String ref_num="";
			String pg_ref="";

			String plant_seq="";
			String plantAddress="";
			String plantCity="";
			String plantState="";
			String plantPin="";
			String plantNm="";

			double tds_amt=0;
			String tds_struct_cd="";
			String cr_dr="";
			
			double netAmt=0;
			double grossAmt=0;
			double taxAmt=0;
			String taxStructCd="";
			
			int noofitem=0;
			String queryString="SELECT A.COUNTERPARTY_CD,B.AGMT_NO,B.AGMT_REV,B.CONT_NO,B.CONT_REV,B.CONTRACT_TYPE,B.ENTITY_CD,"
					+ "A.CURRENCY,A.VALUE,TO_CHAR(A.RECEIPT_DT,'DD/MM/YYYY'),A.SEC_REF_NO,A.SEC_TYPE,A.STATUS,A.VALUE,A.SEC_CATEGORY,"
					+ "A.PG_REF,A.TDS_AMT,A.TDS_STRUCT_CD,A.CR_DR,A.BU_UNIT,A.PLANT_SEQ,TAX_AMT,TAX_STRUCT_CD "
					+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B "
					+ "WHERE A.COMPANY_CD=? AND A.GX=? "
					+ "AND A.COUNTERPARTY_CD=? AND A.SEQ_NO=? AND A.SEQ_REV_NO=? "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.SEQ_NO=B.SEQ_NO "
					+ "AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, gx);
			stmt.setString(3, counterparty_cd);
			stmt.setString(4, seq_no);
			stmt.setString(5, seq_rev_no);
			rset = stmt.executeQuery();
			if(rset.next())
			{
				if(gx.equals("I"))
				{
					entity_cd=rset.getString(7)==null?"":rset.getString(7);
				}
				else
				{
					entity_cd=counterparty_cd;
				}

				agmt=rset.getString(2)==null?"":rset.getString(2);
				agmt_rev=rset.getString(3)==null?"":rset.getString(3);
				cont=rset.getString(4)==null?"":rset.getString(4);
				cont_rev=rset.getString(5)==null?"":rset.getString(5);
				cont_type=rset.getString(6)==null?"":rset.getString(6);
				currency=rset.getString(8)==null?"":rset.getString(8);
				gross_amt=nf.format(rset.getDouble(9));
				receipt_dt=rset.getString(10)==null?"":rset.getString(10);
				if(!receipt_dt.equals(""))
				{
					String[] temp_split=receipt_dt.split("/");
					documentDate=temp_split[2]+""+temp_split[1]+""+temp_split[0];
				}
				ref_num=rset.getString(11)==null?"":rset.getString(11);
				sec_type=rset.getString(12)==null?"":rset.getString(12);
				status=rset.getString(13)==null?"":rset.getString(13);
				value=rset.getString(14)==null?"":rset.getString(14);
				grossAmt=rset.getDouble(14);
				sec_category = rset.getString(15)==null?"":rset.getString(15);
				pg_ref = rset.getString(16)==null?"":rset.getString(16);
				tds_amt=rset.getDouble(17);
				tds_struct_cd = rset.getString(18)==null?"":rset.getString(18);
				cr_dr = rset.getString(19)==null?"":rset.getString(19);
				bu_seq_no = rset.getString(20)==null?"":rset.getString(20);
				plant_seq = rset.getString(21)==null?"":rset.getString(21);
				taxAmt=rset.getDouble(22);
				taxStructCd= rset.getString(23)==null?"":rset.getString(23);
				
				currency=utilBean.getRateUnitNm(conn, currency);

				dealno=utilBean.getDisplayDealMapping(agmt, agmt_rev, cont, cont_rev, cont_type);
				
				if(cont_type.equals("S") || cont_type.equals("L") 
						|| cont_type.equals("E") || cont_type.equals("F") 
						|| ((cont_type.equals("O") || cont_type.equals("Q")) && sec_type.equals("DPT")))
				{
					netAmt = tds_amt;
					noofitem=1;
				}
				else if(cont_type.equals("Q") || cont_type.equals("O"))
				{
					netAmt = taxAmt;
					noofitem=1;
				}
				else
				{
					netAmt = grossAmt;
					noofitem=2;
				}
			}
			VSEC_TYPE.add(sec_type);
			VSTATUS.add(status);

			rset.close();
			stmt.close();

			String dueDt="";
			if(!receipt_dt.equals(""))
			{
				String temp[]=receipt_dt.split("/");
				dueDt=temp[2]+""+temp[1]+""+temp[0];
			}

			String buStateNm="";
			String buAbbr="";
			
			if(bu_seq_no.equals(""))
			{
				String queryString1 = "SELECT STATE "
						+ "FROM FMS_COMPANY_OWNER_ADDR_MST A "
						+ "WHERE COMPANY_CD=? AND ADDRESS_TYPE=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COMPANY_OWNER_ADDR_MST B WHERE A.ADDRESS_TYPE=B.ADDRESS_TYPE AND A.COMPANY_CD=B.COMPANY_CD "
						+ "AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, "R");
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					buStateNm=rset1.getString(1)==null?"":rset1.getString(1);
					buAbbr=utilBean.getCompanyAbbr(conn, comp_cd);
				}
				rset1.close();
				stmt1.close();
			}
			else
			{
				String queryString1 = "SELECT PLANT_STATE,PLANT_ABBR "
						+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
						+ "WHERE COUNTERPARTY_CD=? AND ENTITY=? AND COMPANY_CD=? AND SEQ_NO=? "
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, "B");
				stmt1.setString(3, comp_cd);
				stmt1.setString(4, bu_seq_no);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					buStateNm=rset1.getString(1)==null?"":rset1.getString(1);
					buAbbr=rset1.getString(2)==null?"":rset1.getString(2);
				}
				rset1.close();
				stmt1.close();
			}
			
			String docHeaderText=buStateNm+"/"+buAbbr+" - BU";

			String entity="";
			String fileName_startWith="";
			String docType="";
			String root_tag_name="";
			String ven_cust_id="";
			if(gx.equals("I"))
			{
				fileName_startWith="AP_";
				docType="X1";
				root_tag_name="EmsSAPApMessage";
				ven_cust_id="VendorId";
			}
			else
			{
				if(cont_type.equals("S") || cont_type.equals("L") 
						|| cont_type.equals("E") || cont_type.equals("F")  
						|| cont_type.equals("O") || cont_type.equals("Q"))
				{
					fileName_startWith="AR_";
					
					if(cont_type.equals("S") || cont_type.equals("L") 
							|| cont_type.equals("E") || cont_type.equals("F") 
							|| ((cont_type.equals("O") || cont_type.equals("Q")) && sec_type.equals("DPT")))
					{
						docType="SA";
					}
					else
					{
						docType="X2";
					}
					
					root_tag_name="EmsSAPArMessage";
					ven_cust_id="CustomerId";
				}
				else if(cont_type.equals("D") || cont_type.equals("T") || cont_type.equals("G") || cont_type.equals("P") || cont_type.equals("N"))
				{
					fileName_startWith="AP_";
					docType="X1";
					root_tag_name="EmsSAPApMessage";
					ven_cust_id="VendorId";
				}
			}
			
			
			if(cont_type.equals("S") || cont_type.equals("L") || cont_type.equals("X") 
					|| cont_type.equals("E") || cont_type.equals("F") || cont_type.equals("W") 
					|| cont_type.equals("O") || cont_type.equals("Q"))
			{
				entity="C";
			}
			else if(cont_type.equals("D") || cont_type.equals("T") || cont_type.equals("I") || cont_type.equals("G") || cont_type.equals("P") || cont_type.equals("N"))
			{
				entity="T";
			}
			
			HashMap plant_detail=new HashMap();
            if(gx.equals("I"))
            {
            	plant_detail=utilBean.getCounterpartyBuPlantDetail(conn,comp_cd, "G", counterparty_cd, plant_seq);
            	plantNm=counterparty_nm;
            }
            else
            {
            	if(plant_seq.equals(""))
            	{
            		plant_detail=utilBean.getCounterpartyAddressDetail(conn,counterparty_cd, "R");
            		plantNm=counterparty_nm;
            	}
            	else
            	{
            		plant_detail=utilBean.getCounterpartyPlantDetail(conn,comp_cd, entity, counterparty_cd, plant_seq);
            		plantNm=""+plant_detail.get("plant_name");
            	}
            	
            }
            plantAddress=""+plant_detail.get("plant_address");
            plantCity=""+plant_detail.get("plant_city");
            plantState=""+plant_detail.get("plant_state");
            plantPin=""+plant_detail.get("plant_pin");
            

			String assignmentNo="";
			String fms_MessageId="";
			String ems_ref_no="";
			assignmentNo=utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt,"", cont, "", cont_type, "");

			String queryString3 = "";
			queryString3="SELECT SEC_INT_REF "
					+ "FROM FMS_SECURITY_FILE_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=? "
					+ "AND FILE_TYPE=? ";
			stmt3 = conn.prepareStatement(queryString3);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, counterparty_cd);
			stmt3.setString(3, seq_no);
			stmt3.setString(4, seq_rev_no);
			stmt3.setString(5, gx);
			if(isReversal.equals("Y"))
			{
				stmt3.setString(6, "PDF_REV");
			}
			else
			{
				stmt3.setString(6, "PDF");
			}
			rset3 = stmt3.executeQuery();
			if(rset3.next())
			{
				fms_MessageId=rset3.getString(1)==null?"":rset3.getString(1);
				fms_MessageId=fms_MessageId.replace("/", "-");
				
				ems_ref_no=rset3.getString(1)==null?"":rset3.getString(1);
				if(cont_type.equals("O") || cont_type.equals("Q"))
				{
					pg_ref=ems_ref_no;
				}
			}			
			
			if(ems_ref_no.equals(""))
			{
				fms_MessageId=comp_cd+""+gx+""+counterparty_cd+"S"+seq_no;
				if(!seq_rev_no.equals("") && !seq_rev_no.equals("0"))
				{
					fms_MessageId+="V"+seq_rev_no;
				}
				if(isReversal.equals("Y"))
				{
					fms_MessageId+="-REV";
					pg_ref+="-REV";
				}
				
				ems_ref_no=fms_MessageId;
			}
			rset3.close();
			stmt3.close();

			String UserID = ""+utilBean.getUserName(conn,emp_cd);
			String businessAreaCode=utilBean.getBusinessAreaSAPcode(conn, comp_cd, entity, cont_type, sysdate);

			DocumentBuilderFactory docFactory = xmlUtil.dcoumentBuilderFactory();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();

			//root fmsng
			Element fmsng = doc.createElement(root_tag_name);
			doc.appendChild(fmsng);

			//root elements
			Element Header = doc.createElement("Header");
			fmsng.appendChild(Header);
			Element Invoice = doc.createElement("Invoice");
			fmsng.appendChild(Invoice);

			//Header elements
			Element MessageId = doc.createElement("MessageId");
			Element Scope = doc.createElement("Scope");
			Element DateTimeStamp = doc.createElement("DateTimeStamp");
			Element DataSource = doc.createElement("DataSource");

			Header.appendChild(MessageId);
			Header.appendChild(Scope);
			Header.appendChild(DateTimeStamp);
			Header.appendChild(DataSource);

			MessageId.appendChild(doc.createTextNode(fms_MessageId));
			Scope.appendChild(doc.createTextNode(""+utilBean.getCompanySAPcode(conn, comp_cd)+"Accounting"));
			DateTimeStamp.appendChild(doc.createTextNode(date_timestamp));
			DataSource.appendChild(doc.createTextNode(CommonVariable.app_name));

			//Invoice elements
			Element InvoiceHeader = doc.createElement("InvoiceHeader");
			Invoice.appendChild(InvoiceHeader);

			Element BusinessActivity = doc.createElement("BusinessActivity");
			Element DocumentType = doc.createElement("DocumentType");
			Element DocumentDate = doc.createElement("DocumentDate");
			Element PostingDate = doc.createElement("PostingDate");
			Element AccountingPeriodMonth = doc.createElement("AccountingPeriodMonth");
			Element AccountingPeriodYear = doc.createElement("AccountingPeriodYear");
			Element InternalLegalEntity = doc.createElement("InternalLegalEntity");
			Element DocHeaderText = doc.createElement("DocHeaderText");
			Element RefNum = doc.createElement("RefNum");
			Element EmsRefNum = doc.createElement("EmsRefNum");
			Element Currency = doc.createElement("Currency");
			Element LocalCurrency = doc.createElement("LocalCurrency");
			Element ExchangeRate = doc.createElement("ExchangeRate");
			Element CalculateTax = doc.createElement("CalculateTax");
			Element TranslationDate = doc.createElement("TranslationDate");
			Element TradingPartnerBusinessArea = doc.createElement("TradingPartnerBusinessArea");
			Element AddressLine1 = doc.createElement("AddressLine");
			Element AddressLine2 = doc.createElement("AddressLine");
			Element AddressLine3 = doc.createElement("AddressLine");
			Element AddressLine4 = doc.createElement("AddressLine");
			Element AddressLine5 = doc.createElement("AddressLine");
			Element AddressLine6 = doc.createElement("AddressLine");
			Element AddressLine7 = doc.createElement("AddressLine");
			Element AddressLine8 = doc.createElement("AddressLine");
			Element UserName = doc.createElement("UserName");

			//InvoiceHeader element
			InvoiceHeader.appendChild(BusinessActivity);
			InvoiceHeader.appendChild(DocumentType);
			InvoiceHeader.appendChild(DocumentDate);
			InvoiceHeader.appendChild(PostingDate);
			InvoiceHeader.appendChild(AccountingPeriodMonth);
			InvoiceHeader.appendChild(AccountingPeriodYear);
			InvoiceHeader.appendChild(InternalLegalEntity);
			InvoiceHeader.appendChild(DocHeaderText);
			InvoiceHeader.appendChild(RefNum);
			InvoiceHeader.appendChild(EmsRefNum);
			InvoiceHeader.appendChild(Currency);
			InvoiceHeader.appendChild(LocalCurrency);
			InvoiceHeader.appendChild(ExchangeRate);
			InvoiceHeader.appendChild(CalculateTax);
			InvoiceHeader.appendChild(TranslationDate);
			InvoiceHeader.appendChild(TradingPartnerBusinessArea);
			InvoiceHeader.appendChild(AddressLine1);
			InvoiceHeader.appendChild(AddressLine2);
			InvoiceHeader.appendChild(AddressLine3);
			InvoiceHeader.appendChild(AddressLine4);
			InvoiceHeader.appendChild(AddressLine5);
			InvoiceHeader.appendChild(AddressLine6);
			InvoiceHeader.appendChild(AddressLine7);
			InvoiceHeader.appendChild(AddressLine8);
			InvoiceHeader.appendChild(UserName);

			BusinessActivity.appendChild(doc.createTextNode("RFBU")); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN
			//DocumentType.appendChild(doc.createTextNode("X1"));
			DocumentType.appendChild(doc.createTextNode(docType));
			DocumentDate.appendChild(doc.createTextNode(documentDate));
			PostingDate.appendChild(doc.createTextNode(xml_sysdate));
			AccountingPeriodMonth.appendChild(doc.createTextNode(accountingPeriodMonth));
			AccountingPeriodYear.appendChild(doc.createTextNode(accountingPeriodYear));
			InternalLegalEntity.appendChild(doc.createTextNode(utilBean.getCompanySAPcode(conn, comp_cd))); 
			DocHeaderText.appendChild(doc.createTextNode(docHeaderText));
			RefNum.appendChild(doc.createTextNode(pg_ref));// ref_num removed, Added pg_ref as discussed with Divya
			EmsRefNum.appendChild(doc.createTextNode(ems_ref_no));
			Currency.appendChild(doc.createTextNode("INR")); //NO NEED TO SEND OTHER TYPE OF CURRENCY - JD

			AddressLine1.appendChild(doc.createTextNode(plantNm));
			AddressLine2.appendChild(doc.createTextNode(plantAddress));
			AddressLine3.appendChild(doc.createTextNode(plantState));
			AddressLine4.appendChild(doc.createTextNode(plantCity));
			AddressLine5.appendChild(doc.createTextNode(plantPin));

			UserName.appendChild(doc.createTextNode(UserID));
			int k=0;
			int i=1;
			for(i=1; i<=noofitem;i++)
			{
				Element InvoiceDetail = doc.createElement("InvoiceDetail");
				Invoice.appendChild(InvoiceDetail);

				Element VendorId  = doc.createElement(ven_cust_id);
				Element LineSeqNo = doc.createElement("LineSeqNo");
				Element PostingKey = doc.createElement("PostingKey");
				Element Account = doc.createElement("Account");
				Element CurrencyAmount = doc.createElement("CurrencyAmount");
				Element LocalCurrencyAmount = doc.createElement("LocalCurrencyAmount");
				Element TaxCode = doc.createElement("TaxCode");
				Element BusinessArea = doc.createElement("BusinessArea");
				Element ItemText = doc.createElement("ItemText");
				Element Volume = doc.createElement("Volume");
				Element VolumeUnit = doc.createElement("VolumeUnit");
				Element ReferenceKey1 = doc.createElement("ReferenceKey1");
				Element ReferenceKey2 = doc.createElement("ReferenceKey2");
				Element ProductionPeriod = doc.createElement("ProductionPeriod");
				Element AssignmentNumber = doc.createElement("AssignmentNumber");
				Element PaymentTerms = doc.createElement("PaymentTerms");
				Element PaymentDueDate = doc.createElement("PaymentDueDate");
				Element Material=doc.createElement("Material");

				// InvoiceDetail elements
				InvoiceDetail.appendChild(VendorId);
				InvoiceDetail.appendChild(LineSeqNo);
				InvoiceDetail.appendChild(PostingKey);
				InvoiceDetail.appendChild(Account);
				InvoiceDetail.appendChild(CurrencyAmount);
				InvoiceDetail.appendChild(LocalCurrencyAmount);
				InvoiceDetail.appendChild(TaxCode);
				InvoiceDetail.appendChild(BusinessArea);
				InvoiceDetail.appendChild(ItemText);
				InvoiceDetail.appendChild(Volume);
				InvoiceDetail.appendChild(VolumeUnit);
				InvoiceDetail.appendChild(ReferenceKey1);
				InvoiceDetail.appendChild(ReferenceKey2);
				InvoiceDetail.appendChild(ProductionPeriod);
				InvoiceDetail.appendChild(AssignmentNumber);
				InvoiceDetail.appendChild(PaymentTerms);
				InvoiceDetail.appendChild(PaymentDueDate);
				InvoiceDetail.appendChild(Material);

				String pk = "";
				String sign ="";
				
				//if(isReversal.equals("Y"))
				if(cr_dr.equals("DR"))
				{
					if(i==1)
					{
						if(gx.equals("I"))
						{
							pk = "21";
						}
						else 
						{
							pk ="11"; //AS PER VIJAY'S MAIL ON 20250827
						}
						VendorId.appendChild(doc.createTextNode(utilBean.PrePaddingZero(account, 10)));
						ItemText.appendChild(doc.createTextNode(ems_ref_no));
						sign ="";
						gross_amt=nf.format(netAmt);
					}
					else
					{
						pk = "50";
						String tempAccount="2780310";//ADV
						Account.appendChild(doc.createTextNode(utilBean.PrePaddingZero(tempAccount, 10))); 
						sign ="-";
						Material.appendChild(doc.createTextNode(utilBean.PrePaddingZero("1168001", 18)));
						ItemText.appendChild(doc.createTextNode("Gas Purchase (AUD)"));
						gross_amt=nf.format(grossAmt);
					}
				}
				else
				{
					if(i==1)
					{
						if(cont_type.equals("Q") || cont_type.equals("O"))
						{
							pk = "01";
							sign ="";
						}
						else
						{
							pk = "31";
							sign ="-";
						}
						
						VendorId.appendChild(doc.createTextNode(utilBean.PrePaddingZero(account, 10)));
						ItemText.appendChild(doc.createTextNode(ems_ref_no));
						gross_amt=nf.format(netAmt);
					}
					else
					{
						pk = "40";
						String tempAccount="2780310";//ADV
						Account.appendChild(doc.createTextNode(utilBean.PrePaddingZero(tempAccount, 10))); 
						sign ="";
						Material.appendChild(doc.createTextNode(utilBean.PrePaddingZero("1168001", 18)));
						ItemText.appendChild(doc.createTextNode("Gas Purchase (AUD)"));
						gross_amt=nf.format(grossAmt);
					}
				}

				if(cont_type.equals("S") || cont_type.equals("L") 
						|| cont_type.equals("E") || cont_type.equals("F") 
						|| ((cont_type.equals("O") || cont_type.equals("Q")) && sec_type.equals("DPT")))
				{
					pk = "16";
					sign="-";
					
					PaymentTerms.appendChild(doc.createTextNode("")); 
					PaymentDueDate.appendChild(doc.createTextNode(""));
				}
				else
				{
					PaymentTerms.appendChild(doc.createTextNode("ZB00")); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN
					PaymentDueDate.appendChild(doc.createTextNode(dueDt));//AS PER SUNIDHI MAIL 16/08/2023
				}

				k=i;
				LineSeqNo.appendChild(doc.createTextNode(""+k));
				PostingKey.appendChild(doc.createTextNode(pk));		    	

				CurrencyAmount.appendChild(doc.createTextNode(sign+gross_amt));
				TaxCode.appendChild(doc.createTextNode(""));//taxCode
				BusinessArea.appendChild(doc.createTextNode(businessAreaCode)); 

				//Volume.appendChild(doc.createTextNode("0"));
				//VolumeUnit.appendChild(doc.createTextNode("MMB"));
				
				Volume.appendChild(doc.createTextNode(""));
				VolumeUnit.appendChild(doc.createTextNode(""));

				//ReferenceKey1.appendChild(doc.createTextNode(counterparty_abbr+"-"+contract_type+cont_no));
				ReferenceKey1.appendChild(doc.createTextNode(assignmentNo));
				ReferenceKey2.appendChild(doc.createTextNode(UserID));

				ProductionPeriod.appendChild(doc.createTextNode(postingMonth));
				AssignmentNumber.appendChild(doc.createTextNode(assignmentNo));
				
			}
			
			queryString="SELECT TAX_CODE,TAX_STRUCT_CD,TAX_AMT,TAX_BASE_AMT "
					+ "FROM FMS_SECURITY_TAX_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";
			stmt1 = conn.prepareStatement(queryString);
			stmt1.setString(1, comp_cd);
			stmt1.setString(2, counterparty_cd);
			stmt1.setString(3, seq_no);
			stmt1.setString(4, seq_rev_no);
			stmt1.setString(5, gx);
			rset1=stmt1.executeQuery();
			while(rset1.next())
			{
				String tax_code=rset1.getString(1)==null?"":rset1.getString(1);
				String taxStrctCd=rset1.getString(2)==null?"":rset1.getString(2);
				String tax=rset1.getString(3)==null?"":nf.format(rset1.getDouble(3));
				String taxBaseAmt=rset1.getString(4)==null?"":nf.format(rset1.getDouble(4));
				if(!tax.equals(""))
				{
					String pk="50";
			    	String sign="-";
			    	if(cr_dr.equals("DR"))
					{
			    		pk="40";
			    		sign="-";
					}
		    		
			    	k+=1;
			    	
			    	String gl_code="";
			    	String tax_sap_code="";
			    	String amt=tax;
			    	gl_code=utilBean.getTaxGLcode(conn,taxStructCd, tax_code);
			    	tax_sap_code=utilBean.getTaxSAPcode(conn,taxStructCd, tax_code);
			    	
			    	if(Double.parseDouble(amt) > 0)
			    	{
				    	Element InvoiceDetail = doc.createElement("InvoiceDetail");
				    	Invoice.appendChild(InvoiceDetail);
				    	
				    	Element LineSeqNo = doc.createElement("LineSeqNo");
					    Element PostingKey = doc.createElement("PostingKey");
					    Element Account = doc.createElement("Account");
					    Element LineInd = doc.createElement("LineInd");
					    Element TaxAmount = doc.createElement("TaxAmount");
					    Element TaxAmountLocal = doc.createElement("TaxAmountLocal");
					    Element TaxCode = doc.createElement("TaxCode");
					    Element BusinessArea = doc.createElement("BusinessArea");
					    Element TaxType = doc.createElement("TaxType");	
					    Element TaxBase = doc.createElement("TaxBase");
					    
				    	// InvoiceDetail elements
					    InvoiceDetail.appendChild(LineSeqNo);
					    InvoiceDetail.appendChild(PostingKey);
					    InvoiceDetail.appendChild(Account);
					    InvoiceDetail.appendChild(LineInd);
					    InvoiceDetail.appendChild(TaxAmount);
					    InvoiceDetail.appendChild(TaxAmountLocal);
					    InvoiceDetail.appendChild(TaxCode);
					    InvoiceDetail.appendChild(BusinessArea);
					    InvoiceDetail.appendChild(TaxType);
					    InvoiceDetail.appendChild(TaxBase); //AS PER SUNIDHI MAIL 16/08/2023
					    				    
				    	LineSeqNo.appendChild(doc.createTextNode(""+k));
				    	PostingKey.appendChild(doc.createTextNode(pk));
				    	Account.appendChild(doc.createTextNode(utilBean.PrePaddingZero(gl_code, 10)));
				    	LineInd.appendChild(doc.createTextNode("T")); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN
				    	TaxAmount.appendChild(doc.createTextNode(sign+""+amt));
				    	TaxCode.appendChild(doc.createTextNode(tax_sap_code));
				    	BusinessArea.appendChild(doc.createTextNode(businessAreaCode));
				    	TaxType.appendChild(doc.createTextNode("A")); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN
				    	TaxBase.appendChild(doc.createTextNode(taxBaseAmt)); //AS PER SUNIDHI MAIL 16/08/2023
			    	}
				}
			}
			rset1.close();
			stmt1.close();

			if(tds_amt>0)
		    {
		    	String gl_code="";
		    	String TcsTdscode="";
		    	String amt="";
		    	String pk="";
		    	String taxBase="";
		    	String sign="";
		    	
	    		gl_code=utilBean.getTaxGLcode(conn,tds_struct_cd);
	    		TcsTdscode=utilBean.getTaxSAPcode(conn,tds_struct_cd);
	    		amt=nf.format(tds_amt);
	    		pk="50";
	    		taxBase=nf.format(grossAmt);
	    		
	    		sign="-";
	    		if(noofitem==1)
				{
	    			if(cr_dr.equals("DR"))
			    	{
			    		pk = "50";
			    		sign = "-";
			    	}
	    			else
	    			{
		    			pk = "40";
			    		sign = "";
	    			}
				}
	    		else if(cr_dr.equals("DR"))
		    	{
		    		pk = "40";
		    		sign = "";
		    	}
		    	
		    	
	    		//i=noofitem+1;
	    		k+=1;
	    		
		    	Element InvoiceDetail = doc.createElement("InvoiceDetail");
		    	Invoice.appendChild(InvoiceDetail);
		    			    
			    Element LineSeqNo = doc.createElement("LineSeqNo");
			    Element PostingKey = doc.createElement("PostingKey");
			    Element Account = doc.createElement("Account");
			    Element LineInd = doc.createElement("LineInd");
			    Element TaxAmount = doc.createElement("TaxAmount");
			    Element TaxAmountLocal = doc.createElement("TaxAmountLocal");
			    Element TaxCode = doc.createElement("TaxCode");
			    Element BusinessArea = doc.createElement("BusinessArea");
			    Element TaxType = doc.createElement("TaxType");
			    Element TaxBase = doc.createElement("TaxBase");
			    Element ItemText = doc.createElement("ItemText");
			    		    
		    	// InvoiceDetail elements
			    InvoiceDetail.appendChild(LineSeqNo);
			    InvoiceDetail.appendChild(PostingKey);
			    InvoiceDetail.appendChild(Account);
			    InvoiceDetail.appendChild(LineInd);
			    InvoiceDetail.appendChild(TaxAmount);
			    InvoiceDetail.appendChild(TaxAmountLocal);
			    InvoiceDetail.appendChild(TaxCode);
			    InvoiceDetail.appendChild(BusinessArea);
			    InvoiceDetail.appendChild(TaxType);
			    InvoiceDetail.appendChild(TaxBase); //AS PER SUNIDHI MAIL 16/08/2023
			    		
			    if(cont_type.equals("S") || cont_type.equals("L") 
			    		|| cont_type.equals("E") || cont_type.equals("F") 
			    		|| ((cont_type.equals("O") || cont_type.equals("Q")) && sec_type.equals("DPT")))
				{
			    	InvoiceDetail.appendChild(ItemText);
			    	
			    	LineSeqNo.appendChild(doc.createTextNode(""+k));
			    	PostingKey.appendChild(doc.createTextNode(pk));
			    	Account.appendChild(doc.createTextNode(utilBean.PrePaddingZero(gl_code, 10)));
			    	ItemText.appendChild(doc.createTextNode("TDS on Adv "+TcsTdscode));
			    	LineInd.appendChild(doc.createTextNode("")); //AS INSTRUCTED BY VIJAY 20250722
			    	TaxAmount.appendChild(doc.createTextNode(sign+""+amt));
			    	TaxCode.appendChild(doc.createTextNode("")); //AS INSTRUCTED BY VIJAY 20250722
			    	BusinessArea.appendChild(doc.createTextNode(businessAreaCode));
			    	TaxType.appendChild(doc.createTextNode("")); //AS INSTRUCTED BY VIJAY 20250722
			    	TaxBase.appendChild(doc.createTextNode(taxBase)); //AS PER SUNIDHI MAIL 16/08/2023
				}
			    else
			    {
			    	LineSeqNo.appendChild(doc.createTextNode(""+k));
			    	PostingKey.appendChild(doc.createTextNode(pk));
			    	Account.appendChild(doc.createTextNode(utilBean.PrePaddingZero(gl_code, 10)));
			    	LineInd.appendChild(doc.createTextNode("TDS")); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN
			    	TaxAmount.appendChild(doc.createTextNode(sign+""+amt));
			    	TaxCode.appendChild(doc.createTextNode(TcsTdscode));
			    	BusinessArea.appendChild(doc.createTextNode(businessAreaCode));
			    	TaxType.appendChild(doc.createTextNode("V")); //FIXED VALUE AS INSTRUCTED BY MAHESH MOHAN	
			    	TaxBase.appendChild(doc.createTextNode(taxBase)); //AS PER SUNIDHI MAIL 16/08/2023
			    }
		    	
		    }

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD","");
			transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet","");

			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);


			String xmlFileNm="";
			String datetime="";
			datetime=splitSys[0].replaceAll("/", "")+""+splitSys[1].replaceAll(":", "");		    

			if(!fms_MessageId.equals("") && !fileName_startWith.equals(""))
		    {
		    	if(sap_approval_flag.equals("Y"))
		        {
		    		xmlFileNm=fileName_startWith+""+fms_MessageId+"_"+datetime+".xml";
		        }
		    	else
		    	{
		    		xmlFileNm=fileName_startWith+""+fms_MessageId+".xml";
		    	}
		    }

			if(!xmlFileNm.equals(""))
			{
				String appPath = request.getServletContext().getRealPath("");

				String main_folder="";
				if(!comp_cd.equals(""))
				{
					main_folder=CommonVariable.work_dir+comp_cd;
				}
				File MainDir = new File(appPath+File.separator+main_folder);
				if(!MainDir.exists()) 
				{
					MainDir.mkdir();
				}
				String sub_folder=""+CommonVariable.sap_xml;
				if(!sap_approval_flag.equals("Y"))
				{
					sub_folder=""+CommonVariable.temp_sap_xml;
				}
				File SubDir = new File(appPath+File.separator+main_folder+File.separator+sub_folder);
				if(!SubDir.exists()) 
				{
					SubDir.mkdir();
				}

				StreamResult result =  new StreamResult(new File(appPath+File.separator+main_folder+File.separator+sub_folder+""+File.separator+""+xmlFileNm));
				transformer.transform(source, result);

				xmlfile_name=xmlFileNm;

				if(sap_approval_flag.equals("Y"))
				{
					File fileExi = new File(appPath+File.separator+main_folder+File.separator+sub_folder+File.separator+xmlFileNm);
					if(fileExi.exists()) 
					{
						if(isReversal.equals("Y"))
						{
							int count=0;
							String queryString4="SELECT COUNT(SEQ_NO) "
									+ "FROM FMS_SECURITY_FILE_DTL "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=? "
									+ "AND FILE_TYPE=? ";
							stmt4 = conn.prepareStatement(queryString4);
							stmt4.setString(1, comp_cd);
							stmt4.setString(2, counterparty_cd);
							stmt4.setString(3, seq_no);
							stmt4.setString(4, seq_rev_no);
							stmt4.setString(5, gx);
							stmt4.setString(6, "XML_REV");
							rset4 = stmt4.executeQuery();
							if(rset4.next())
							{
								count=rset4.getInt(1);
							}
							rset4.close();
							stmt4.close();

							if(count>0)
							{
								String queryString5="UPDATE FMS_SECURITY_FILE_DTL SET FILE_NAME=?,"
										+ "MODIFY_BY=?,MODIFY_DT=SYSDATE "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=? "
										+ "AND FILE_TYPE=? ";
								stmt5 = conn.prepareStatement(queryString5);
								stmt5.setString(1, xmlFileNm);
								stmt5.setString(2, emp_cd);
								stmt5.setString(3, comp_cd);
								stmt5.setString(4, counterparty_cd);
								stmt5.setString(5, seq_no);
								stmt5.setString(6, seq_rev_no);
								stmt5.setString(7, gx);
								stmt5.setString(8, "XML_REV");
								stmt5.executeUpdate();

								stmt5.close();
							}
							else
							{
								String queryString5="INSERT INTO FMS_SECURITY_FILE_DTL(COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEQ_REV_NO,GX,"
										+ "FILE_TYPE,FILE_NAME,ENT_BY,ENT_DT) "
										+ "VALUES(?,?,?,?,?,?,?,?,SYSDATE)";
								//System.out.println(queryString);
								stmt5 = conn.prepareStatement(queryString5);
								stmt5.setString(1, comp_cd);
								stmt5.setString(2, counterparty_cd);
								stmt5.setString(3, seq_no);
								stmt5.setString(4, seq_rev_no);
								stmt5.setString(5, gx);
								stmt5.setString(6, "XML_REV");
								stmt5.setString(7, xmlFileNm);
								stmt5.setString(8, emp_cd);
								stmt5.executeUpdate();

								stmt5.close();
							}
						}
						else
						{
							int count=0;
							String queryString4="SELECT COUNT(SEQ_NO) "
									+ "FROM FMS_SECURITY_FILE_DTL "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=? "
									+ "AND FILE_TYPE=? ";
							stmt4 = conn.prepareStatement(queryString4);
							stmt4.setString(1, comp_cd);
							stmt4.setString(2, counterparty_cd);
							stmt4.setString(3, seq_no);
							stmt4.setString(4, seq_rev_no);
							stmt4.setString(5, gx);
							stmt4.setString(6, "XML");
							rset4 = stmt4.executeQuery();
							if(rset4.next())
							{
								count=rset4.getInt(1);
							}
							rset4.close();
							stmt4.close();

							if(count>0)
							{
								String queryString5="UPDATE FMS_SECURITY_FILE_DTL SET FILE_NAME=?,"
										+ "MODIFY_BY=?,MODIFY_DT=SYSDATE "
										+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
										+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=? "
										+ "AND FILE_TYPE=? ";
								stmt5 = conn.prepareStatement(queryString5);
								stmt5.setString(1, xmlFileNm);
								stmt5.setString(2, emp_cd);
								stmt5.setString(3, comp_cd);
								stmt5.setString(4, counterparty_cd);
								stmt5.setString(5, seq_no);
								stmt5.setString(6, seq_rev_no);
								stmt5.setString(7, gx);
								stmt5.setString(8, "XML");
								stmt5.executeUpdate();

								stmt5.close();
							}
							else
							{
								String queryString5="INSERT INTO FMS_SECURITY_FILE_DTL(COMPANY_CD,COUNTERPARTY_CD,SEQ_NO,SEQ_REV_NO,GX,"
										+ "FILE_TYPE,FILE_NAME,ENT_BY,ENT_DT) "
										+ "VALUES(?,?,?,?,?,?,?,?,SYSDATE)";
								stmt5 = conn.prepareStatement(queryString5);
								stmt5.setString(1, comp_cd);
								stmt5.setString(2, counterparty_cd);
								stmt5.setString(3, seq_no);
								stmt5.setString(4, seq_rev_no);
								stmt5.setString(5, gx);
								stmt5.setString(6, "XML");
								stmt5.setString(7, xmlFileNm);
								stmt5.setString(8, emp_cd);
								stmt5.executeUpdate();

								stmt5.close();
							}
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void parseSAP_XMLfile()
	{
		String function_nm="parseSAP_XMLfile()";
		try
		{
			if(gx.equals("I"))
			{
				counterparty_abbr=utilBean.getGasExchangeAbbr(conn,counterparty_cd);
				counterparty_nm=utilBean.getGasExchangeName(conn,counterparty_cd);
			}
			else
			{
				counterparty_abbr=utilBean.getCounterpartyABBR(conn,counterparty_cd);
				counterparty_nm=utilBean.getCounterpartyName(conn,counterparty_cd);
			}

			if(xmlfile_name.equals(""))
			{
				String fms_MessageId="";
				String queryString="SELECT FILE_NAME "
						+ "FROM FMS_SECURITY_FILE_DTL "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=? "
						+ "AND FILE_TYPE=? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, seq_no);
				stmt.setString(4, seq_rev_no);
				stmt.setString(5, gx);
				if(isReversal.equals("Y"))
				{
					stmt.setString(6, "XML_REV");
				}
				else
				{
					stmt.setString(6, "XML");
				}
				rset = stmt.executeQuery();
				if(rset.next())
				{
					fms_MessageId=rset.getString(1)==null?"":rset.getString(1);
				}
				xmlfile_name=fms_MessageId;

				rset.close();
				stmt.close();
			}

			if(!xmlfile_name.equals(""))
			{
				String final_path=file_path+File.separator+xmlfile_name;

				if(sap_approval_flag.equals("Y"))
				{
					File fXmlFile = new File(file_path+File.separator+xmlfile_name);
					if(fXmlFile.exists())
					{
						final_path=file_path+File.separator+xmlfile_name;
					}
					else
					{
						String new_path=file_path+File.separator+CommonVariable.sap_xml_success+File.separator+xmlfile_name;
						final_path=new_path;
					}
				}

				File file = new File(final_path);
				if(file.exists())				
				{
					//System.out.println("Created file is in folder");

					DocumentBuilderFactory dbFactory = xmlUtil.dcoumentBuilderFactory();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(file);

					doc.getDocumentElement().normalize();
					
					zeroTotal="0.00";
					
					/*NodeList nList = doc.getElementsByTagName("EmsSAPApMessage");
					if(nList.getLength() <= 0)
					{
						nList = doc.getElementsByTagName("EmsSAPArMessage");
						
						if(nList.getLength() <= 0)
						{
							nList = doc.getElementsByTagName("FmsngSAPArMessage");
							
							if(nList.getLength() <= 0)
							{
								nList = doc.getElementsByTagName("FmsngSAPApMessage");
							}
						}
					}*/
					
					String[] tagNames = {
						    "EmsSAPApMessage",
						    "EmsSAPArMessage",
						    "FmsngSAPArMessage",
						    "FmsngSAPApMessage"
						};

					NodeList nList = null;

					for(String tag : tagNames) 
					{
					    nList = doc.getElementsByTagName(tag);
					    if (nList.getLength() > 0) 
					    {
					        break;
					    }
					}

					for (int temp = 0; temp < nList.getLength(); temp++) 
					{
						Node nNode = nList.item(temp);
						Element eElement = (Element) nNode;

						NodeList nodes = eElement.getChildNodes();
						for(int i=0; i<nodes.getLength(); i++)
						{
							Node node = nodes.item(i);
							String childTag = node.getNodeName();
							//System.out.println(childTag);
							if(childTag.equalsIgnoreCase("Header"))
							{
								Element ele = (Element) node;
								NodeList nodes1 = ele.getChildNodes();
								for(int j=0; j<nodes1.getLength(); j++)
								{
									Node node1 = nodes1.item(j);
									String childTag1 = node1.getNodeName();
									if(childTag1.equalsIgnoreCase("MessageId"))
									{
										documentNo=nodes1.item(j).getTextContent();
									}
								}
							}
							else if(childTag.equalsIgnoreCase("Invoice"))
							{
								Element ele = (Element) node;
								NodeList nodes1 = ele.getChildNodes();
								for(int j=0; j<nodes1.getLength(); j++)
								{
									Node node1 = nodes1.item(j);
									String childTag1 = node1.getNodeName();
									//System.out.println("childTag1="+childTag1);
									if(childTag1.equalsIgnoreCase("InvoiceHeader"))
									{
										Element ele1 = (Element) node1;
										NodeList nodes2 = ele1.getChildNodes();

										for(int k=0; k<nodes2.getLength(); k++)
										{
											Node node2 = nodes2.item(k);
											String childTag2 = node2.getNodeName();

											//System.out.println("childTag2="+childTag2);
											if(childTag2.equals("DocumentType"))
											{
												//System.out.println(k+""+nodes2.item(k).getTextContent());
												documentType=nodes2.item(k).getTextContent();
											}
											else if(childTag2.equals("DocumentDate"))
											{
												documentDate=nodes2.item(k).getTextContent();											
											}
											else if(childTag2.equals("DocumentNo"))
											{
												//documentNo=nodes2.item(k).getTextContent();											
											}
											else if(childTag2.equals("PostingDate")) 
											{
												postingDate=nodes2.item(k).getTextContent();
											}
											else if(childTag2.equals("AccountingPeriodMonth")) 
											{
												accountingPeriodMonth=nodes2.item(k).getTextContent();
											}	
											else if(childTag2.equals("AccountingPeriodYear")) 
											{
												accountingPeriodYear=nodes2.item(k).getTextContent();
											}	
											else if(childTag2.equals("InternalLegalEntity")) 
											{
												headerCompanyCode=nodes2.item(k).getTextContent();
											}	
											else if(childTag2.equals("DocHeaderText")) 
											{
												docHeaderText=nodes2.item(k).getTextContent();
											}	
											else if(childTag2.equals("RefNum"))
											{
												refNum=nodes2.item(k).getTextContent();
											}
											else if(childTag2.equals("Currency")) 
											{
												currency=nodes2.item(k).getTextContent();
											}
											else if(childTag2.equals("LocalCurrency")) {}
											else if(childTag2.equals("ExchangeRate")) {}
											else if(childTag2.equals("CalculateTax")) {}
											else if(childTag2.equals("TranslationDate")) {}
											else if(childTag2.equals("TradingPartnerBusinessArea")) {}
										}
									}	
									else if(childTag1.equalsIgnoreCase("InvoiceDetail"))
									{
										Element ele1 = (Element) node1;
										NodeList nodes2 = ele1.getChildNodes();

										String lineseqno= "";
										String postingkey= "";
										String account= "";
										String vendorId= "";
										String currencyamount= "";
										String businessarea= "";
										String itemtext= "";
										String taxCode= "";

										for(int k=0; k<nodes2.getLength(); k++)
										{
											Node node2 = nodes2.item(k);
											String childTag2 = node2.getNodeName();

											//System.out.println("childTag2="+childTag2);
											if(childTag2.equals("LineSeqNo")) 
											{
												lineseqno=nodes2.item(k).getTextContent();
											}
											else if(childTag2.equals("PostingKey")) 
											{
												postingkey=nodes2.item(k).getTextContent();
											}
											else if(childTag2.equals("Account")) 
											{
												account=nodes2.item(k).getTextContent();
											}
											else if(childTag2.equals("VendorId"))
											{
												vendorId=nodes2.item(k).getTextContent();
											}
											else if(childTag2.equals("CustomerId"))
											{
												vendorId=nodes2.item(k).getTextContent();
											}
											else if(childTag2.equals("TransactionType")) {}
											else if(childTag2.equals("CurrencyAmount") || childTag2.equals("TaxAmount")) 
											{
												currencyamount=nodes2.item(k).getTextContent();
											}
											else if(childTag2.equals("LocalCurrencyAmount")) {}
											else if(childTag2.equals("BusinessArea")) 
											{
												businessarea=nodes2.item(k).getTextContent();
											}
											else if(childTag2.equals("ItemText") || childTag2.equals("LineInd")) 
											{
												itemtext=nodes2.item(k).getTextContent();
											}
											else if(childTag2.equals("TaxCode")) 
											{
												taxCode=nodes2.item(k).getTextContent();
											}
											else if(childTag2.equals("Volume")) {}
											else if(childTag2.equals("VolumeUnit")) {}
											else if(childTag2.equals("ReferenceKey1")) {}
											else if(childTag2.equals("ReferenceKey2")) {}
											else if(childTag2.equals("ProductionPeriod")) {}
											else if(childTag2.equals("AssignmentNumber")) {}
										}

										String tempAccount="";
										if(!account.equals("") && vendorId.equals(""))
										{
											tempAccount=account;
										}
										else if(account.equals("") && !vendorId.equals(""))
										{
											tempAccount=vendorId;
										}
										if(lineseqno.equals("2") && itemtext.equals("TDS"))
										{
											if(!currencyamount.equals(""))
											{
												zeroTotal=nf.format(Double.parseDouble(zeroTotal)+Double.parseDouble(currencyamount));
											}
										}
										else if(!itemtext.equals("TDS")) //AS PER VIJAY MAIL ON 19-10-2023 TO ADDRESS SAP LIMITATION FOR TDS
										{
											if(!currencyamount.equals(""))
											{
												zeroTotal=nf.format(Double.parseDouble(zeroTotal)+Double.parseDouble(currencyamount));
											}
										}
										
										if(itemtext.equals("T") || itemtext.equals("TDS") || itemtext.equals("TCS"))
										{
											itemtext=itemtext+" ["+taxCode+"]";
										}
										
										VLINESEQNO.add(lineseqno);
										VPOSTINGKEY.add(postingkey);
										VACCOUNT.add(tempAccount);
										VCURRENCYAMOUNT.add(currencyamount);
										VBUSINESSAREA.add(businessarea);
										VITEMTEXT.add(itemtext);
										VSHORTTEXT.add(utilBean.getGLdesc(conn, ""+utilBean.RemovePrePaddingZero(account)));
									}	
								}	
							}
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getPostingDetail()
	{
		String function_nm="getPostingDetail()";
		try
		{
			queryString="SELECT MSG_STATUS,DOC_NO,STATUS_MSG,TO_CHAR(TO_DATE(TO_CHAR(POST_DT)||' '||POST_TIME,'DD-MM-YYYY HH24:MI:SS'),'DD-MM-YYYY HH24:MI:SS') "
					+ "FROM FMS_SAP_ACK_DTL A "
					+ "WHERE COMPANY_CD=? AND FMS_REF=? "
					+ "AND TO_DATE(TO_CHAR(POST_DT)||' '||POST_TIME,'DD-MM-YYYY HH24:MI:SS')=(SELECT MAX(TO_DATE(TO_CHAR(POST_DT)||' '||POST_TIME,'DD-MM-YYYY HH24:MI:SS')) "
					+ "FROM FMS_SAP_ACK_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.FMS_REF=B.FMS_REF) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, sec_int_ref);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				sap_msg_status=rset.getString(1)==null?"":rset.getString(1);
				sap_doc_no=rset.getString(2)==null?"":rset.getString(2);
				sap_ack_msg=rset.getString(3)==null?"":rset.getString(3);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getSecurityDetailForViewRemittance()
	{
		String function_nm="getSecurityDetailForViewRemittance()";
		try
		{
			String cpty_cd="";
			String agmt="";
			String cont="";
			String cont_ty="";
			String bu_unit="";
			String plant_seq="";
			String tax_amt="";
			String tax_struct_cd="";
			String tax_struct_dtl="";
			String gross_amt="";
			String att_receipt_voucher="";
			String sec_type="";
			
			String queryString="SELECT B.COUNTERPARTY_CD,B.AGMT_NO,B.CONT_NO,B.ENTITY_CD,TO_CHAR(A.RECEIPT_DT,'DD/MM/YYYY'),A.VALUE,A.CURRENCY,"
					+ "B.CONTRACT_TYPE,A.PG_REF,A.STATUS,A.SAP_APPROVAL,A.BU_UNIT,A.PLANT_SEQ,A.TAX_AMT,A.TAX_STRUCT_CD,A.CR_DR,A.GROSS_AMT,A.RECPT_SEC_REF,A.SEC_TYPE "
					+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B "
					+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
					+ "AND A.SEQ_NO=? AND A.SEQ_REV_NO=? AND A.GX=? "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=B.SEQ_NO AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterparty_cd);
			stmt.setString(3, seq_no);
			stmt.setString(4, seq_rev_no);
			stmt.setString(5, gx);
			rset = stmt.executeQuery();
			if(rset.next())
			{
				cpty_cd=rset.getString(1)==null?"":rset.getString(1);
				agmt=rset.getString(2)==null?"":rset.getString(2);
				cont=rset.getString(3)==null?"":rset.getString(3);
				if(gx.equals("I"))
				{
					cpty_cd=rset.getString(4)==null?"":rset.getString(4);
				}
				received_date=rset.getString(5)==null?"":rset.getString(5);
				value=nf.format(rset.getDouble(6));
				currency=utilBean.getRateUnitNm(conn,rset.getString(7)==null?"":rset.getString(7));
				cont_ty=rset.getString(8)==null?"":rset.getString(8);
				contract_type=cont_ty;
				pg_ref=rset.getString(9)==null?"":rset.getString(9);
				status=rset.getString(10)==null?"":rset.getString(10);
				sap_approval_flag=rset.getString(11)==null?"":rset.getString(11);
				bu_unit=rset.getString(12)==null?"":rset.getString(12);
				plant_seq=rset.getString(13)==null?"":rset.getString(13);
				tax_amt=nf.format(rset.getDouble(14));
				tax_struct_cd=rset.getString(15)==null?"":rset.getString(15);
				tax_struct_dtl=utilBean.getTaxDescr(conn, tax_struct_cd);
				cr_dr=rset.getString(16)==null?"":rset.getString(16);
				gross_amt=nf.format(rset.getDouble(17));
				att_receipt_voucher=rset.getString(18)==null?"":rset.getString(18);
				sec_type=rset.getString(19)==null?"":rset.getString(19);
			}
			rset.close();
			stmt.close();
			
			//ADVANCE SEQ NO
			if(cont_ty.equals("O") || cont_ty.equals("Q"))
			{
				String invoice_prefix=utilBean.getInvoicePrefix(conn,comp_cd);
				String bu_state_tin=utilBean.getState_TIN(conn, comp_cd, comp_cd, "B", bu_unit);
				String state_abbr="";
				String state_code=bu_state_tin;
				if(state_code.length()<=1)
				{
					state_code="0"+state_code;
				}
				String fin_yr=dateUtil.getFinancialYear(received_date);
				if(!fin_yr.equals(""))
				{
					String[] temp = fin_yr.split("-");
					fin_yr=temp[0].substring(2,temp[0].length())+"-"+temp[1].substring(2,temp[1].length());
				}
				String contType="'Q','O'";
				String invSeries="A";	
				if(cr_dr.equals("DR"))
				{
					invSeries="R";
				}
				
				String queryString2="SELECT STATE_CODE "
						+ "FROM FMS_STATE_MST "
						+ "WHERE TIN=?";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, state_code);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					state_abbr=rset2.getString(1)==null?"":rset2.getString(1);
				}
				rset2.close();
				stmt2.close();
				
				if(!invoice_prefix.equals(""))
				{
					int no_inv_no=10;
					for(int i=1;i<=no_inv_no;i++)
					{
						String invoice_id_seq=""+i;
						int count=0;
						String queryString1="SELECT COUNT(*) "
								+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B, FMS_SECURITY_FILE_DTL C "
								+ "WHERE A.COMPANY_CD=? AND A.GX=? "
								+ "AND C.SEC_INT_REF LIKE ? AND C.FILE_TYPE=? "
								+ "AND B.CONTRACT_TYPE IN ("+contType+") "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.SEQ_NO=B.SEQ_NO AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX "
								+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
								+ "AND A.SEQ_NO=C.SEQ_NO AND A.SEQ_REV_NO=C.SEQ_REV_NO AND A.GX=C.GX ";
						String temp_queryString1=queryString1;
						stmt=conn.prepareStatement(temp_queryString1);
						stmt.setString(1, comp_cd);
						stmt.setString(2, gx);
						stmt.setString(3, invoice_prefix+""+invSeries+""+state_abbr+utilBean.PrePaddingZero(invoice_id_seq, 4)+"/"+fin_yr);
						stmt.setString(4, "PDF");
						rset=stmt.executeQuery();
						if(rset.next())
						{
							count += rset.getInt(1);
						}
						rset.close();
						stmt.close();
						
						if(count==0)
						{	
							String invoice_no="";
							invoice_no=invoice_prefix+""+invSeries+""+state_abbr+""+utilBean.PrePaddingZero(invoice_id_seq, 4)+"/"+fin_yr;
							
							VINVOICE_ID_SEQ.add(invoice_id_seq);
							VINVOICE_NO.add(invoice_no);
						}
						else
						{
							no_inv_no+=1;
						}
					}
				}
				
				if(invoice_id_seq.equals(""))
				{
					if(VINVOICE_ID_SEQ.size()>0)
					{
						invoice_id_seq=""+VINVOICE_ID_SEQ.elementAt(0);
					}
				}
			}
			else
			{
				remittance_no=comp_cd+""+gx+""+counterparty_cd+"S"+seq_no;
				if(!seq_rev_no.equals("") && !seq_rev_no.equals("0"))
				{
					remittance_no+="V"+seq_rev_no;
				}
				if(isReversal.equals("Y"))
				{
					remittance_no+="-REV";
					pg_ref+="-REV";
				}
			}

			if(gx.equals("I"))
			{
				gx_counterparty_nm=utilBean.getGasExchangeName(conn,counterparty_cd);
			}
			counterparty_nm=utilBean.getCounterpartyName(conn,cpty_cd);

			String queryString0 = "";
			if(cont_ty.equals("O") || cont_ty.equals("Q"))
			{
				queryString0="SELECT A.CONT_REF_NO,NULL,TO_CHAR(SIGNING_DT,'DD-MON-YY') "
						+ "FROM FMS_LTCORA_CONT_MST A "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) ";
				
				String queryString1="SELECT TO_CHAR(SIGNING_DT,'DD-MON-YY')  "
						+ "FROM FMS_LTCORA_AGMT_MST A "
						+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
						+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_LTCORA_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.BUY_SALE=B.BUY_SALE) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, "A");
				stmt1.setString(3, agmt);
				stmt1.setString(4, cpty_cd);
				stmt1.setString(5, "C");
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					agmtSigningDt=rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
			}
			else if(cont_ty.equals("I") || cont_ty.equals("T") || cont_ty.equals("D"))
			{
				queryString0="SELECT CONT_REF_NO,TRADE_REF_NO,TO_CHAR(SIGNING_DT,'DD/MM/YYYY') "
						+ "FROM FMS_TRADER_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
						+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			}
			else
			{
				queryString0="SELECT CONT_REF_NO,TRADE_REF_NO,TO_CHAR(SIGNING_DT,'DD/MM/YYYY') "
						+ "FROM FMS_SUPPLY_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=? "
						+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
			}
			stmt0 = conn.prepareStatement(queryString0);
			stmt0.setString(1, comp_cd);
			stmt0.setString(2, cpty_cd);
			stmt0.setString(3, agmt);
			stmt0.setString(4, cont);
			stmt0.setString(5, cont_ty);
			rset0 = stmt0.executeQuery();
			if(rset0.next())
			{
				contRef  = rset0.getString(1)==null?"":rset0.getString(1);
				if(gx.equals("I"))
				{
					contRef= rset0.getString(2)==null?"":rset0.getString(2);
				}
				signing_dt= rset0.getString(3)==null?"":rset0.getString(3);
			}
			
			String entity="";
			if(cont_ty.equals("S") || cont_ty.equals("L") || cont_ty.equals("X") 
					|| cont_ty.equals("E") || cont_ty.equals("F") || cont_ty.equals("W") 
					|| cont_ty.equals("O") || cont_ty.equals("Q"))
			{
				entity="C";
			}
			else if(cont_ty.equals("D") || cont_ty.equals("I") || cont_ty.equals("G") || cont_ty.equals("P") || cont_ty.equals("N"))
			{
				entity="T";
			}
			
			HashMap bu_plant_detail=new HashMap();
			if(bu_unit.equals(""))
			{
				bu_plant_detail=utilBean.getCompanyAddressDetail(conn, comp_cd, "R");
				bu_plantNm=utilBean.getCompanyName(conn, bu_unit);
			}
			else
			{
				bu_plant_detail=utilBean.getCounterpartyPlantDetail(conn,comp_cd, "B", comp_cd, bu_unit);
				bu_plantNm=""+bu_plant_detail.get("plant_name");
			}
			bu_plantAddress=""+bu_plant_detail.get("plant_address");
			bu_plantCity=""+bu_plant_detail.get("plant_city");
			bu_plantState=""+bu_plant_detail.get("plant_state");
			bu_plantPin=""+bu_plant_detail.get("plant_pin");
			
			
			HashMap plant_detail=new HashMap();
            if(gx.equals("I"))
            {
            	plant_detail=utilBean.getCounterpartyBuPlantDetail(conn,comp_cd, "G", counterparty_cd, plant_seq);
            }
            else
            {
            	if(plant_seq.equals(""))
            	{
            		plant_detail=utilBean.getCounterpartyAddressDetail(conn, counterparty_cd, "R");
            		plantNm=counterparty_nm;
            	}
            	else
            	{
            		plant_detail=utilBean.getCounterpartyPlantDetail(conn,comp_cd, entity, counterparty_cd, plant_seq);
            		plantNm=""+plant_detail.get("plant_name");
            	}
            }
            plantAddress=""+plant_detail.get("plant_address");
            plantCity=""+plant_detail.get("plant_city");
            plantState=""+plant_detail.get("plant_state");
            plantPin=""+plant_detail.get("plant_pin");
            
            
			if(cont_ty.equals("O") || cont_ty.equals("Q"))
			{
				tax_info="State : "+plantState;
				tax_info+="<br>State Code : "+utilBean.getState_TIN(conn, comp_cd, counterparty_cd, entity, plant_seq);
				//String temp_tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "C", counterparty_cd, plant_seq);
				//tax_info+=temp_tax_info.replaceAll("\n", "<br>");
				
				queryString = "SELECT A.STAT_NO, TO_CHAR(A.EFF_DT,'DD/MM/YYYY'), B.STAT_NM "
						+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B "
						+ "WHERE A.COUNTERPARTY_CD=? AND A.ENTITY=? "
						+ "AND A.PLANT_SEQ_NO=? AND A.COMPANY_CD=? "
						+ "AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL AND B.STAT_CD IN ('1003','1001')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, counterparty_cd);
				stmt.setString(2, "C");
				stmt.setString(3, plant_seq);
				stmt.setString(4, comp_cd);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					String no = rset.getString(1)==null?"":rset.getString(1);
					String nm = rset.getString(3)==null?"":rset.getString(3);
	
					tax_info+="<br>"+nm+" : "+no;
				}
				stmt.close();
				rset.close();
				
				bu_tax_info="State : "+bu_plantState;
				bu_tax_info+="<br>State Code : "+utilBean.getState_TIN(conn, comp_cd, comp_cd, "B", bu_unit);
				//String temp_bu_tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "B", comp_cd, bu_unit);
				//bu_tax_info+=temp_bu_tax_info.replaceAll("\n", "<br>");
				
				queryString = "SELECT A.STAT_NO, TO_CHAR(A.EFF_DT,'DD/MM/YYYY'), B.STAT_NM "
						+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B "
						+ "WHERE A.COUNTERPARTY_CD=? AND A.ENTITY=? "
						+ "AND A.PLANT_SEQ_NO=? AND A.COMPANY_CD=? "
						+ "AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL AND B.STAT_CD IN ('1003','1001')";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, "B");
				stmt.setString(3, bu_unit);
				stmt.setString(4, comp_cd);
				rset = stmt.executeQuery();
				while(rset.next())
				{
					String no = rset.getString(1)==null?"":rset.getString(1);
					String nm = rset.getString(3)==null?"":rset.getString(3);
	
					bu_tax_info+="<br>"+nm+" : "+no;
				}
				stmt.close();
				rset.close();
				
				bu_tax_info+="<br>SAC : 999799"
						+ "<br>Description of Service : Other Miscellaneous services - Other Services n.e.c."
						+ "<br>Place Of Supply : "+bu_plantState;
				int srno=0;
				if(cr_dr.equals("DR") && sec_type.equals("ADV") && !att_receipt_voucher.equals(""))
				{
					String dated="";
					String queryString1="SELECT A.TAX_AMT,A.TAX_STRUCT_CD,A.GROSS_AMT,TO_CHAR(A.RECEIPT_DT,'DD-MON-YY') "
							+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B, FMS_SECURITY_FILE_DTL C "
							+ "WHERE A.COMPANY_CD=? AND A.GX=? AND C.FILE_TYPE=? AND C.SEC_INT_REF=? "
							+ "AND A.CR_DR=? "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.SEQ_NO=B.SEQ_NO AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX "
							+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
							+ "AND A.SEQ_NO=C.SEQ_NO AND A.SEQ_REV_NO=C.SEQ_REV_NO AND A.GX=C.GX ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, gx);
					stmt1.setString(3, "PDF");
					stmt1.setString(4, att_receipt_voucher);
					stmt1.setString(5, "CR");
					rset1 = stmt1.executeQuery();
					if(rset1.next())
					{
						tax_amt=nf.format(rset1.getDouble(1));
						tax_struct_cd=rset1.getString(2)==null?"":rset1.getString(2);
						tax_struct_dtl=utilBean.getTaxDescr(conn, tax_struct_cd);
						gross_amt=nf.format(rset1.getDouble(3));
						dated=rset1.getString(4)==null?"":rset1.getString(4);
					}
					rset1.close();
					stmt1.close();
					
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("Advance amount received as per Receipt Voucher No. "+att_receipt_voucher+" dated "+dated);
					VPDF_COL3.add(currency);
					VPDF_COL4.add("Lumsum");
					VPDF_COL5.add(gross_amt);
					
					/*srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("Gross Amount");
					VPDF_COL3.add(currency);
					VPDF_COL4.add("");
					VPDF_COL5.add(gross_amt);
					*/
					
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("Advance Tax ("+tax_struct_dtl+") received as per Receipt Voucher No. "+att_receipt_voucher+" dated "+dated);
					VPDF_COL3.add(currency);
					VPDF_COL4.add("");
					VPDF_COL5.add(tax_amt);
					
					double temp_srno=srno;
					queryString1="SELECT COUNT(*) "
							+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B, FMS_SECURITY_FILE_DTL C, FMS_SECURITY_TAX_DTL D "
							+ "WHERE A.COMPANY_CD=? AND A.GX=? AND C.FILE_TYPE=? AND C.SEC_INT_REF=? "
							+ "AND A.CR_DR=? "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.SEQ_NO=B.SEQ_NO AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX "
							+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
							+ "AND A.SEQ_NO=C.SEQ_NO AND A.SEQ_REV_NO=C.SEQ_REV_NO AND A.GX=C.GX "
							+ ""
							+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD "
							+ "AND A.SEQ_NO=D.SEQ_NO AND A.SEQ_REV_NO=D.SEQ_REV_NO AND A.GX=D.GX "
							+ "";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, gx);
					stmt1.setString(3, "PDF");
					stmt1.setString(4, att_receipt_voucher);
					stmt1.setString(5, "CR");
					rset1 = stmt1.executeQuery();
					if(rset1.next())
					{
						if(rset1.getInt(1)>1)
						{
							String taxlblInfo="";
					
							String queryString2="SELECT D.TAX_CODE,D.TAX_DESCR,D.TAX_AMT,D.TAX_BASE_AMT "
									+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B, FMS_SECURITY_FILE_DTL C, FMS_SECURITY_TAX_DTL D "
									+ "WHERE A.COMPANY_CD=? AND A.GX=? AND C.FILE_TYPE=? AND C.SEC_INT_REF=? "
									+ "AND A.CR_DR=? "
									+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
									+ "AND A.SEQ_NO=B.SEQ_NO AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX "
									+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
									+ "AND A.SEQ_NO=C.SEQ_NO AND A.SEQ_REV_NO=C.SEQ_REV_NO AND A.GX=C.GX "
									+ ""
									+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD "
									+ "AND A.SEQ_NO=D.SEQ_NO AND A.SEQ_REV_NO=D.SEQ_REV_NO AND A.GX=D.GX "
									+ "";
							stmt2 = conn.prepareStatement(queryString2);
							stmt2.setString(1, comp_cd);
							stmt2.setString(2, gx);
							stmt2.setString(3, "PDF");
							stmt2.setString(4, att_receipt_voucher);
							stmt2.setString(5, "CR");
							rset2 = stmt2.executeQuery();
							while(rset2.next())
							{
								taxlblInfo=rset2.getString(2)==null?"":rset2.getString(2);
								taxlblInfo=taxlblInfo+" on advance amount";
								temp_srno=temp_srno+0.1;
								VPDF_COL1.add(nf0.format(temp_srno));
								VPDF_COL2.add(taxlblInfo);
								VPDF_COL3.add(currency);
								VPDF_COL4.add("");
								VPDF_COL5.add(rset2.getString(3)==null?"":nf.format(rset2.getDouble(3)));
							}
							rset2.close();
							stmt2.close();
						}
					}
					rset1.close();
					stmt1.close();
					
					String invAmtLbl="Total Advance Amount";
					String inv_amt=nf.format(Double.parseDouble(gross_amt)+Double.parseDouble(tax_amt));
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add(invAmtLbl);
					VPDF_COL3.add(currency);
					VPDF_COL4.add("");
					VPDF_COL5.add(inv_amt);
					
					double adjted_amt=0;
					String queryString2="SELECT SUM(B.AMOUNT), LISTAGG(A.INVOICE_NO || ' dated '||TO_CHAR(A.INVOICE_DT,'DD-MON-YY'), ',') WITHIN GROUP (ORDER BY A.INVOICE_NO) AS INVOICE_NO_LIST "
							+ "FROM FMS_INVOICE_MST A, FMS_INV_ADV_DTL B "
							+ "WHERE A.COMPANY_CD=? AND B.SEC_INT_REF=? AND B.INV_COMPONENT=? "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.BU_STATE_TIN=B.BU_STATE_TIN";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, att_receipt_voucher);
					stmt2.setString(3, "GROSS");
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						adjted_amt+=rset2.getDouble(1);
						String invoiceMap=rset2.getString(2)==null?"":rset2.getString(2);
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("Adjustment for LTCORA service against Tax Invoice No. "+invoiceMap);
						VPDF_COL3.add(currency);
						VPDF_COL4.add("");
						VPDF_COL5.add(rset2.getString(1)==null?"":nf.format(rset2.getDouble(1)));
					}
					rset2.close();
					stmt2.close();
					
					queryString="SELECT D.TAX_DESCR "
							+ "FROM FMS_SECURITY_MST A,FMS_SECURITY_DEAL_MAP B, FMS_SECURITY_FILE_DTL C, FMS_SECURITY_TAX_DTL D "
							+ "WHERE A.COMPANY_CD=? AND A.GX=? AND C.FILE_TYPE=? AND C.SEC_INT_REF=? "
							+ "AND A.CR_DR=? "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.SEQ_NO=B.SEQ_NO AND A.SEQ_REV_NO=B.SEQ_REV_NO AND A.GX=B.GX "
							+ "AND A.COMPANY_CD=C.COMPANY_CD AND A.COUNTERPARTY_CD=C.COUNTERPARTY_CD "
							+ "AND A.SEQ_NO=C.SEQ_NO AND A.SEQ_REV_NO=C.SEQ_REV_NO AND A.GX=C.GX "
							+ ""
							+ "AND A.COMPANY_CD=D.COMPANY_CD AND A.COUNTERPARTY_CD=D.COUNTERPARTY_CD "
							+ "AND A.SEQ_NO=D.SEQ_NO AND A.SEQ_REV_NO=D.SEQ_REV_NO AND A.GX=D.GX "
							+ "";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, gx);
					stmt.setString(3, "PDF");
					stmt.setString(4, att_receipt_voucher);
					stmt.setString(5, "CR");
					rset=stmt.executeQuery();
					while(rset.next())
					{
						String taxDesc=rset.getString(1)==null?"":rset.getString(1);
						String taxAbbr="";
						if(!taxDesc.equals(""))
						{
							String[] split=taxDesc.split(" ");
							taxAbbr=split[0];
						}
						
						queryString2="SELECT SUM(B.AMOUNT), LISTAGG(A.INVOICE_NO || ' dated '||TO_CHAR(A.INVOICE_DT,'DD-MON-YY'), ',') WITHIN GROUP (ORDER BY A.INVOICE_NO) AS INVOICE_NO_LIST "
								+ "FROM FMS_INVOICE_MST A, FMS_INV_ADV_DTL B "
								+ "WHERE A.COMPANY_CD=? AND B.SEC_INT_REF=? AND B.INV_COMPONENT=? "
								+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.BU_STATE_TIN=B.BU_STATE_TIN";
						stmt2=conn.prepareStatement(queryString2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, att_receipt_voucher);
						stmt2.setString(3, taxAbbr);
						rset2=stmt2.executeQuery();
						if(rset2.next())
						{
							adjted_amt+=rset2.getDouble(1);
							String invoiceMap=rset2.getString(2)==null?"":rset2.getString(2);
							
							srno+=1;
							VPDF_COL1.add(srno);
							VPDF_COL2.add("Adjustment for "+taxAbbr+" paid against Tax Invoice No. "+invoiceMap);
							VPDF_COL3.add(currency);
							VPDF_COL4.add("");
							VPDF_COL5.add(rset2.getString(1)==null?"":nf.format(rset2.getDouble(1)));
						}
						rset2.close();
						stmt2.close();
					}
					rset.close();
					stmt.close();
					
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("Net Balance Amount");
					VPDF_COL3.add(currency);
					VPDF_COL4.add("");
					VPDF_COL5.add(nf.format(Double.parseDouble(inv_amt) - adjted_amt));
				}
				else
				{
					srno+=1;
					VPDF_COL1.add(srno);
					if(cr_dr.equals("DR"))
					{
						VPDF_COL2.add("Adjustment for LTCORA services");
					}
					else
					{
						VPDF_COL2.add("Advance Invoice For Regasification");
					}
					VPDF_COL3.add(currency);
					VPDF_COL4.add("Lumsum");
					VPDF_COL5.add(gross_amt);
					
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("Gross Amount");
					VPDF_COL3.add(currency);
					VPDF_COL4.add("");
					VPDF_COL5.add(gross_amt);
					
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("Tax ("+tax_struct_dtl+")");
					VPDF_COL3.add(currency);
					VPDF_COL4.add("");
					VPDF_COL5.add(tax_amt);
					
					double temp_srno=srno;
					String queryString1="SELECT COUNT(*) "
							+ "FROM FMS_SECURITY_TAX_DTL "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, seq_no);
					stmt1.setString(4, seq_rev_no);
					stmt1.setString(5, gx);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						if(rset1.getInt(1)>1)
						{
							queryString="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_SECURITY_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
									+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=?";
							stmt=conn.prepareStatement(queryString);
							stmt.setString(1, comp_cd);
							stmt.setString(2, counterparty_cd);
							stmt.setString(3, seq_no);
							stmt.setString(4, seq_rev_no);
							stmt.setString(5, gx);
							rset=stmt.executeQuery();
							while(rset.next())
							{
								temp_srno=temp_srno+0.1;
								VPDF_COL1.add(nf0.format(temp_srno));
								VPDF_COL2.add(rset.getString(2)==null?"":rset.getString(2));
								VPDF_COL3.add(currency);
								VPDF_COL4.add("");
								VPDF_COL5.add(rset.getString(3)==null?"":nf.format(rset.getDouble(3)));
							}
							rset.close();
							stmt.close();
						}
					}
					rset1.close();
					stmt1.close();
					
					String invAmtLbl="Invoice Amount";
					String inv_amt=nf.format(Double.parseDouble(gross_amt)+Double.parseDouble(tax_amt));
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add(invAmtLbl);
					VPDF_COL3.add(currency);
					VPDF_COL4.add("");
					VPDF_COL5.add(inv_amt);
					
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("Net Amount Payable");
					VPDF_COL3.add(currency);
					VPDF_COL4.add("");
					VPDF_COL5.add(inv_amt);
				}
			}
			else
			{	
				if(gx.equals("I"))
	            {
	            	tax_info=utilBean.getCounterpartyBuPlantTaxInfo(conn,comp_cd, "G", counterparty_cd, plant_seq);
	            }
	            else
	            {
	            	tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, entity, counterparty_cd, plant_seq);
	            }
				tax_info=tax_info.replaceAll("\n", "<br>");
				
				bu_tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "B", comp_cd, bu_unit);
				bu_tax_info=bu_tax_info.replaceAll("\n", "<br>");
			}
			
			String queryString7 = "";
			queryString7="SELECT FILE_NAME "
					+ "FROM FMS_SECURITY_FILE_DTL "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND SEQ_NO=? AND SEQ_REV_NO=? AND GX=? "
					+ "AND FILE_TYPE=? ";
			stmt7 = conn.prepareStatement(queryString7);
			stmt7.setString(1, comp_cd);
			stmt7.setString(2, counterparty_cd);
			stmt7.setString(3, seq_no);
			stmt7.setString(4, seq_rev_no);
			stmt7.setString(5, gx);
			if(isReversal.equals("Y"))
			{
				stmt7.setString(6, "PDF_REV");
			}
			else
			{
				stmt7.setString(6, "PDF");
			}
			rset7 = stmt7.executeQuery();
			if(rset7.next())
			{
				print_pdf_flag="Y";
			}
			else
			{
				print_pdf_flag="N";
			}

			rset7.close();
			stmt7.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public HttpServletRequest request = null;
	public void setRequest(HttpServletRequest request) {this.request = request;}
	
	String callFlag = "";
	public void setCallFlag(String callFlag) {this.callFlag = callFlag;}
	String comp_cd = "";
	public void setComp_cd(String comp_cd) {this.comp_cd = comp_cd;}
	
	String counterparty_cd = "";
	String cont_no = "";
	String cont_rev_no = "";
	String agmt_no = "";
	String agmt_rev_no = "";
	String contract_type = "";
	String clearance = "";
	String adv_flag = "";
	String gx = "";
	String seq_no = "";
	String seq_rev_no = "";
	String sec_ref_no = "";
	String emp_cd = "";
	String counterparty_nm = "";
	String sec_type = "";
	String status = "";
	String currency = "";
	String value = "";
	String received_date = "";
	String dealno = "";
	String sec_category="";
	String sec_int_ref="";
	
	String sap_approval_flag="";//HP20230919
	String xmlfile_name="";//HP20230919
	String file_path="";//HP20230919
	String isReversal="";//HP20230920
	
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setCont_no(String cont_no) {this.cont_no = cont_no;}
	public void setCont_rev_no(String cont_rev_no) {this.cont_rev_no = cont_rev_no;}
	public void setAgmt_no(String agmt_no) {this.agmt_no = agmt_no;}
	public void setAgmt_rev_no(String agmt_rev_no) {this.agmt_rev_no = agmt_rev_no;}
	public void setContract_type(String contract_type) {this.contract_type = contract_type;}
	public void setClearance(String clearance) {this.clearance = clearance;}
	public void setAdv_flag(String adv_flag) {this.adv_flag = adv_flag;}
	public void setGx(String gx) {this.gx = gx;}
	public void setSeq_no(String seq_no) {this.seq_no = seq_no;}
	public void setSeq_rev_no(String seq_rev_no) {this.seq_rev_no = seq_rev_no;}
	public void setSec_ref_no(String sec_ref_no) {this.sec_ref_no = sec_ref_no;}
	public void setEmp_cd(String emp_cd) {this.emp_cd = emp_cd;}
	public void setSap_approval_flag(String sap_approval_flag) {this.sap_approval_flag = sap_approval_flag;}//HP20230919
	public void setXmlfile_name(String xmlfile_name) {this.xmlfile_name = xmlfile_name;}//HP20230919
	public void setFile_path(String file_path) {this.file_path = file_path;}//HP20230919
	public void setIsReversal(String isReversal) {this.isReversal = isReversal;}//HP20230920
	public void setSec_category(String sec_category) {this.sec_category = sec_category;}
	public void setSec_int_ref(String sec_int_ref) {this.sec_int_ref = sec_int_ref;}
	
	public String getCounterparty_cd() {return counterparty_cd;}
	public String getXmlfile_name() {return xmlfile_name;}//HP20230919
	public String getCounterparty_nm() {return counterparty_nm;}
	public String getSec_type() {return sec_type;}
	public String getStatus() {return status;}
	public String getCurrency() {return currency;}
	public String getValue() {return value;}
	public String getDealno() {return dealno;}
	public String getReceived_date() {return received_date;}
	public String getSec_category() {return sec_category;}
	public String getContract_type() {return contract_type;}
	
	Vector VMST_COUNTERPARTY_CD = new Vector();
	Vector VMST_COUNTERPARTY_NM = new Vector();
	Vector VMST_COUNTERPARTY_ABBR = new Vector();
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NAME = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VCONT_NO = new Vector();
	Vector VCONT_REV_NO = new Vector();
	Vector VAGMT_NO = new Vector();
	Vector VAGMT_REV_NO = new Vector();
	Vector VCONTRACT_TYPE = new Vector();
	Vector VCONTRACT_TYPE_NM = new Vector();
	Vector VSTATUS = new Vector();
	Vector VSTATUS_NM = new Vector();
	Vector VCURRENCY = new Vector();
	Vector VCURRENCY_NM = new Vector();
	Vector VSEC_TYPE = new Vector();
	Vector VSEC_REF_NO = new Vector();
	Vector VVALUE = new Vector();
	Vector VTOTAL_VALUE = new Vector();
	Vector VRECEIVED_DATE = new Vector();
	Vector VDEAL_TYPE = new Vector();
	Vector VSEQ_NO = new Vector();
	Vector VSEQ_REV_NO = new Vector();
	Vector VREMARK = new Vector();
	Vector VADV_PG_REF = new Vector();
	Vector VDEAL_NO = new Vector();
	Vector VPDF_GENERATED = new Vector();
	Vector VPDF_FILE_NAME = new Vector();
	Vector VSAP_APPROVAL_FLAG = new Vector();
	Vector VCONTRACT_MAPPING = new Vector();
	Vector VCONTRACT_MAPPING_DIS = new Vector();
	Vector VCONTRACT_MAPPING_DIS_1 = new Vector();
	Vector VCRDR = new Vector();
	Vector VCRDR_NM = new Vector();
	Vector VTDS_AMT = new Vector();
	Vector VTDS_STRUCT_CD = new Vector();
	Vector VTDS_STRUCT_INFO = new Vector();
	Vector VTEMP_SEC_REF_NO = new Vector();
	Vector VDEAL_MAPPING = new Vector();
	Vector VCONTRACT_EXPIRED = new Vector();
	Vector VCONTRACT_DEAL_TYPE = new Vector();
	Vector VCONT_BUY_SELL = new Vector();
	Vector VBUY_SELL = new Vector();
	Vector VBU_UNIT = new Vector();
	Vector VPLANT_SEQ = new Vector();
	Vector VTAX_AMT = new Vector();
	Vector VTAX_STRUCT_CD = new Vector();
	Vector VTAX_STRUCT_DTL = new Vector();
	Vector VGROSS_AMT = new Vector();
	Vector VDEAL_CONT_REF = new Vector();
	Vector VATT_RECEIPT_VOUCHER = new Vector();
	
	Vector VINVOICE_ID_SEQ = new Vector();
	Vector VINVOICE_NO = new Vector();
	
	Vector VPDF_FILE_PATH=new Vector();
	Vector VPDF_SIGNED_FLAG=new Vector();
	
	Vector VSEC_INT_REF=new Vector();
	
	Vector VSPLIT_VALUE=new Vector();
	Vector VSPLIT_BY=new Vector();
	
	//HP20230919
	Vector VLINESEQNO = new Vector();
	Vector VPOSTINGKEY = new Vector();
	Vector VACCOUNT = new Vector();
	Vector VCURRENCYAMOUNT = new Vector();
	Vector VBUSINESSAREA = new Vector();
	Vector VITEMTEXT = new Vector();
	Vector VSHORTTEXT = new Vector();
	
	Vector VPDF_COL1 = new Vector();
	Vector VPDF_COL2 = new Vector();
	Vector VPDF_COL3 = new Vector();
	Vector VPDF_COL4 = new Vector();
	Vector VPDF_COL5 = new Vector();
	
	public Vector getVMST_COUNTERPARTY_CD() {return VMST_COUNTERPARTY_CD;}
	public Vector getVMST_COUNTERPARTY_NM() {return VMST_COUNTERPARTY_NM;}
	public Vector getVMST_COUNTERPARTY_ABBR() {return VMST_COUNTERPARTY_ABBR;}
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NAME() {return VCOUNTERPARTY_NAME;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVCONT_NO() {return VCONT_NO;}
	public Vector getVCONT_REV_NO() {return VCONT_REV_NO;}
	public Vector getVAGMT_NO() {return VAGMT_NO;}
	public Vector getVAGMT_REV_NO() {return VAGMT_REV_NO;}
	public Vector getVCONTRACT_TYPE() {return VCONTRACT_TYPE;}
	public Vector getVCONTRACT_TYPE_NM() {return VCONTRACT_TYPE_NM;}
	public Vector getVSTATUS() {return VSTATUS;}
	public Vector getVSTATUS_NM() {return VSTATUS_NM;}
	public Vector getVCURRENCY() {return VCURRENCY;}
	public Vector getVCURRENCY_NM() {return VCURRENCY_NM;}
	public Vector getVSEC_TYPE() {return VSEC_TYPE;}
	public Vector getVSEC_REF_NO() {return VSEC_REF_NO;}
	public Vector getVVALUE() {return VVALUE;}
	public Vector getVTOTAL_VALUE() {return VTOTAL_VALUE;}
	public Vector getVRECEIVED_DATE() {return VRECEIVED_DATE;}
	public Vector getVDEAL_TYPE() {return VDEAL_TYPE;}
	public Vector getVSEQ_NO() {return VSEQ_NO;}
	public Vector getVSEQ_REV_NO() {return VSEQ_REV_NO;}
	public Vector getVREMARK() {return VREMARK;}
	public Vector getVADV_PG_REF() {return VADV_PG_REF;}
	public Vector getVDEAL_NO() {return VDEAL_NO;}
	public Vector getVPDF_GENERATED() {return VPDF_GENERATED;}
	public Vector getVPDF_FILE_NAME() {return VPDF_FILE_NAME;}
	public Vector getVSAP_APPROVAL_FLAG() {return VSAP_APPROVAL_FLAG;}
	public Vector getVCONTRACT_MAPPING() {return VCONTRACT_MAPPING;}
	public Vector getVCONTRACT_MAPPING_DIS() {return VCONTRACT_MAPPING_DIS;}
	public Vector getVCONTRACT_MAPPING_DIS_1() {return VCONTRACT_MAPPING_DIS_1;}
	public Vector getVCRDR() {return VCRDR;}
	public Vector getVCRDR_NM() {return VCRDR_NM;}
	public Vector getVTDS_AMT() {return VTDS_AMT;}
	public Vector getVTDS_STRUCT_CD() {return VTDS_STRUCT_CD;}
	public Vector getVTDS_STRUCT_INFO() {return VTDS_STRUCT_INFO;}
	public Vector getVTEMP_SEC_REF_NO() {return VTEMP_SEC_REF_NO;}
	public Vector getVDEAL_MAPPING() {return VDEAL_MAPPING;}
	public Vector getVCONTRACT_EXPIRED() {return VCONTRACT_EXPIRED;}
	public Vector getVCONTRACT_DEAL_TYPE() {return VCONTRACT_DEAL_TYPE;}
	public Vector getVCONT_BUY_SELL() {return VCONT_BUY_SELL;}
	public Vector getVBUY_SELL() {return VBUY_SELL;}
	public Vector getVBU_UNIT() {return VBU_UNIT;}
	public Vector getVPLANT_SEQ() {return VPLANT_SEQ;}
	public Vector getVTAX_AMT() {return VTAX_AMT;}
	public Vector getVTAX_STRUCT_CD() {return VTAX_STRUCT_CD;}
	public Vector getVTAX_STRUCT_DTL() {return VTAX_STRUCT_DTL;}
	public Vector getVGROSS_AMT() {return VGROSS_AMT;}
	public Vector getVDEAL_CONT_REF() {return VDEAL_CONT_REF;}
	public Vector getVATT_RECEIPT_VOUCHER() {return VATT_RECEIPT_VOUCHER;}
	
	public Vector getVINVOICE_ID_SEQ() {return VINVOICE_ID_SEQ;}
	public Vector getVINVOICE_NO() {return VINVOICE_NO;}
	
	public Vector getVPDF_FILE_PATH() {return VPDF_FILE_PATH;}
	public Vector getVPDF_SIGNED_FLAG() {return VPDF_SIGNED_FLAG;}
	
	public Vector getVSEC_INT_REF() {return VSEC_INT_REF;}
	
	public Vector getVSPLIT_VALUE() {return VSPLIT_VALUE;}
	public Vector getVSPLIT_BY() {return VSPLIT_BY;}
	
	//HP20230919
	public Vector getVLINESEQNO() {return VLINESEQNO;}
	public Vector getVPOSTINGKEY() {return VPOSTINGKEY;}
	public Vector getVACCOUNT() {return VACCOUNT;}
	public Vector getVCURRENCYAMOUNT() {return VCURRENCYAMOUNT;}
	public Vector getVBUSINESSAREA() {return VBUSINESSAREA;}
	public Vector getVITEMTEXT() {return VITEMTEXT;}
	public Vector getVSHORTTEXT() {return VSHORTTEXT;}
	
	public Vector getVPDF_COL1() {return VPDF_COL1;}
	public Vector getVPDF_COL2() {return VPDF_COL2;}
	public Vector getVPDF_COL3() {return VPDF_COL3;}
	public Vector getVPDF_COL4() {return VPDF_COL4;}
	public Vector getVPDF_COL5() {return VPDF_COL5;}
	
	String counterparty_abbr="";
	String counterparty_name="";
	String company_abbr = "";

	// SAP XML Variables HP20230919
	String documentType="";
	String documentDate="";
	String documentNo="";
	String postingDate="";
	String accountingPeriodMonth="";
	String accountingPeriodYear="";
	String headerCompanyCode="";
	String docHeaderText="";
	String refNum ="";
	
	String plantAddress="";
	String plantCity="";
	String plantState="";
	String plantPin="";
	String plantNm="";

	String bu_plantAddress="";
	String bu_plantCity="";
	String bu_plantState="";
	String bu_plantPin="";
	String bu_plantNm="";

	String tax_info="";
	String bu_tax_info="";
	
	String pg_ref="";
	String contRef="";
	String signing_dt="";
	String gx_counterparty_nm="";
	String remittance_no="";
	String print_pdf_flag="";
	String zeroTotal="";
	
	String agmtSigningDt="";
	String cr_dr="";
	String invoice_id_seq="";
	
	String sap_msg_status="";
	String sap_doc_no="";
	String sap_ack_msg="";
	
	public String getCounterparty_abbr() {return counterparty_abbr;}
	public String getCounterparty_name() {return counterparty_name;}
	public String getCompany_abbr() {return company_abbr;}
	
	//HP20230919
	public String getDocumentType() {return documentType;}
	public String getDocumentDate() {return documentDate;}
	public String getDocumentNo() {return documentNo;}
	public String getPostingDate() {return postingDate;}
	public String getAccountingPeriodMonth() {return accountingPeriodMonth;}
	public String getAccountingPeriodYear() {return accountingPeriodYear;}
	public String getHeaderCompanyCode() {return headerCompanyCode;}
	public String getDocHeaderText() {return docHeaderText;}
	public String getRefNum() {return refNum;}
	
	public String getPlantAddress() {return plantAddress;}
	public String getPlantCity() {return plantCity;}
	public String getPlantState() {return plantState;}
	public String getPlantPin() {return plantPin;}
	public String getPlantNm() {return plantNm;}

	public String getBu_plantAddress() {return bu_plantAddress;}
	public String getBu_plantCity() {return bu_plantCity;}
	public String getBu_plantState() {return bu_plantState;}
	public String getBu_plantPin() {return bu_plantPin;}
	public String getBu_plantNm() {return bu_plantNm;}

	public String getTax_info() {return tax_info;}
	public String getBu_tax_info() {return bu_tax_info;}
	
	public String getPg_ref() {return pg_ref;}
	public String getContRef() {return contRef;}

	public String getSigning_dt() {return signing_dt;}
	public String getGx_counterparty_nm() {return gx_counterparty_nm;}
	public String getRemittance_no() {return remittance_no;}
	public String getPrint_pdf_flag() {return print_pdf_flag;}
	public String getZeroTotal() {return zeroTotal;}
	
	public String getAgmtSigningDt() {return agmtSigningDt;}
	public String getCr_dr() {return cr_dr;}
	public String getInvoice_id_seq() {return invoice_id_seq;}
	
	public String getSap_doc_no() {return sap_doc_no;}
	public String getSap_msg_status() {return sap_msg_status;}
	public String geSap_ack_msg() {return sap_ack_msg;}
}