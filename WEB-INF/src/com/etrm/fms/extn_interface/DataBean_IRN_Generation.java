package com.etrm.fms.extn_interface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;

//Coded By          : Harsh Patel
//Code Reviewed by	:  
//CR Date			: 06/06/2025 
//Status	  		: Developing
public class DataBean_IRN_Generation 
{
	String db_src_file_name="DataBean_IRN_Generation.java";
	
	Connection conn; 
	PreparedStatement stmt;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt3;
	ResultSet rset;
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	String queryString="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	
	UtilBean utilBean = new UtilBean();
	
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
	    			if(callFlag.equalsIgnoreCase("INVOICE_LIST_FOR_IRN_GEN"))
	    			{
	    				getInvoiceList();
	    			}
	    			else if(callFlag.equalsIgnoreCase("OTH_INVOICE_LIST_FOR_IRN_GEN"))
	    			{
	    				getOtherInvoiceList();
	    			}
	    			
	    			conn.close();
	    		}
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
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt3 != null){try{stmt3.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    }
	}
	
	public void getInvoiceList()
	{
		String function_nm="getInvoiceList()";
		try
		{
			String contType="";
			String invFlag="";
			String ff_inv_type="";
			if(invoice_type.equals("SI"))
			{
				contType="'O','Q'";
				invFlag="'F'";
				ff_inv_type="'"+invoice_type+"'";
			}
			else if(invoice_type.equals("DTM"))
			{
				contType="'B','M'";
				invFlag="'F'";
				ff_inv_type="'"+invoice_type+"'";
			}
			else if(invoice_type.equals("TLU"))
			{
				contType="'O','Q'";
				invFlag="'"+invoice_type+"'";
				ff_inv_type="'"+invoice_type+"'";
			}
			else if(invoice_type.equals("UG") || invoice_type.equals("ST") || invoice_type.equals("DI"))
			{
				contType="'O','Q'";
				invFlag="'"+invoice_type+"'";
				ff_inv_type="'"+invoice_type+"'";
			}
			else if(invoice_type.equals("CR") || invoice_type.equals("DR") 
					|| invoice_type.equals("CCR") || invoice_type.equals("CDR") 
					|| invoice_type.equals("LP") || invoice_type.equals("OR"))
			{
				contType="'O','Q'";
				invFlag="'"+invoice_type+"'";
				ff_inv_type="'"+invoice_type+"'";
			}
			else
			{
				contType="'O','Q','B','M'";
				invFlag="'F','UG','ST','CR','DR','LP','TLU'";
				
				ff_inv_type="'SI','UG','ST','DI','CR','DR','CCR','CDR','LP','OR','TLU'";
			}
			
			queryString="SELECT COUNTERPARTY_CD,CONTRACT_TYPE,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(APPROVED_DT,'DD/MM/YYYY'),INV_FLAG,NULL "
					+ "FROM FMS_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE IN ("+contType+") AND INV_FLAG IN ("+invFlag+") "
					+ "AND TO_CHAR(INVOICE_DT,'MM/YYYY')=TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'MM/YYYY') AND INVOICE_NO IS NOT NULL AND APPROVED_FLAG=? "
					+ " UNION ALL "
					+ "SELECT COUNTERPARTY_CD,CONTRACT_TYPE,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(APPROVED_DT,'DD/MM/YYYY'),INVOICE_TYPE,'FFLOW' "
					+ "FROM FMS_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE IN ("+contType+") AND INVOICE_TYPE IN ("+ff_inv_type+") "
					+ "AND TO_CHAR(INVOICE_DT,'MM/YYYY')=TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'MM/YYYY') AND INVOICE_NO IS NOT NULL AND APPROVED_FLAG=? "
					+ "AND INVOICE_CATEGORY=? AND TAX_STRUCT_CD IS NOT NULL AND TAX_STRUCT_CD > 0 "
					+ " UNION ALL "
					+ "SELECT COUNTERPARTY_CD,CONTRACT_TYPE,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(APPROVED_DT,'DD/MM/YYYY'),INVOICE_TYPE,'DLNG-FFLOW' "
					+ "FROM FMS_DLNG_FFLOW_INV_MST "
					+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE IN ("+contType+") AND INVOICE_TYPE IN ("+ff_inv_type+") "
					+ "AND TO_CHAR(INVOICE_DT,'MM/YYYY')=TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'MM/YYYY') AND INVOICE_NO IS NOT NULL AND APPROVED_FLAG=? "
					+ "AND INVOICE_CATEGORY=? AND TAX_STRUCT_CD IS NOT NULL AND TAX_STRUCT_CD > 0 "
					+ " UNION ALL "
					+ "SELECT COUNTERPARTY_CD,CONTRACT_TYPE,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(APPROVED_DT,'DD/MM/YYYY'),INV_FLAG,'DLNG-SRV' "
					+ "FROM FMS_DLNG_SVC_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE IN ("+contType+") AND INV_FLAG IN ("+invFlag+") "
					+ "AND TO_CHAR(INVOICE_DT,'MM/YYYY')=TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'MM/YYYY') AND INVOICE_NO IS NOT NULL AND APPROVED_FLAG=? "
					+ "AND TAX_STRUCT_CD IS NOT NULL AND TAX_STRUCT_CD > 0 "
					+ "";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "01/"+month+"/"+year);
			stmt.setString(3, "Y");
			stmt.setString(4, comp_cd);
			stmt.setString(5, "01/"+month+"/"+year);
			stmt.setString(6, "Y");
			stmt.setString(7, "S");
			stmt.setString(8, comp_cd);
			stmt.setString(9, "01/"+month+"/"+year);
			stmt.setString(10, "Y");
			stmt.setString(11, "S");
			stmt.setString(12, comp_cd);
			stmt.setString(13, "01/"+month+"/"+year);
			stmt.setString(14, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String contprty_cd=rset.getString(1)==null?"":rset.getString(1);
				String contprty_nm=utilBean.getCounterpartyName(conn, contprty_cd);
				String contprty_abbr=utilBean.getCounterpartyABBR(conn, contprty_cd);
				
				VCOUNTERPARTY_CD.add(contprty_cd);
				VCOUNTERPARTY_NM.add(contprty_nm);
				VCOUNTERPARTY_ABBR.add(contprty_abbr);
				
				String cont_type=rset.getString(2)==null?"":rset.getString(2);
				VCONTRACT_TYPE.add(cont_type);
				VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_type));
				
				String inv_no=rset.getString(3)==null?"":rset.getString(3);
				VINVOICE_NO.add(inv_no);
				VINVOICE_DT.add(rset.getString(4)==null?"":rset.getString(4));
				VAPPROVE_DT.add(rset.getString(5)==null?"":rset.getString(5));
				
				String inv_flag=rset.getString(6)==null?"":rset.getString(6); //FF INVOICE TYPE ALSO COME HERE
				String inv_type_cete=rset.getString(7)==null?"":rset.getString(7);
				String inv_type=(inv_flag.equals("F") && inv_type_cete.equals("DLNG-SRV"))?"DTM":inv_flag.equals("F")?"SI":inv_flag;
				
				VSYS_OR_FF_INV.add(inv_type_cete.equals("")?"S":inv_type_cete.equals("DLNG-SRV")?"DSRV":inv_type_cete.equals("DLNG-FFLOW")?"DFF":"FF");
				
				VINVOICE_TYPE.add(inv_type);
				String inv_type_nm=getInvoiceTypeName(inv_type);
				inv_type_nm=!inv_type_cete.equals("")?inv_type_nm+" <span style='background-color: #ffe6e6;'>["+inv_type_cete+"]</span>":inv_type_nm;
				VINVOICE_TYPE_NM.add(inv_type_nm);
				
				String irn_no="";
				queryString1="SELECT IRN_NO "
						+ "FROM FMS_INVOICE_IRN_DTL "
						+ "WHERE COMPANY_CD=? AND INVOICE_NO=? ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, inv_no);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					irn_no=rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
				
				VIS_IRN_GENERATED.add(irn_no.equals("")?"N":"Y");
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getOtherInvoiceList()
	{
		String function_nm="getOtherInvoiceList()";
		try
		{
			String inv_type="";
			if(invoice_type.equals("COSTRH") 
					|| invoice_type.equals("HSA") || invoice_type.equals("HS") 
					|| invoice_type.equals("SFA") || invoice_type.equals("NPR") 
					|| invoice_type.equals("AHPL") || invoice_type.equals("GA"))
			{
				inv_type="'"+invoice_type+"'";
			}
			else
			{
				inv_type="'COSTRH','HSA','HS','SFA','AHPL','NPR','GA'"; //'COSTR',
			}
			
			queryString="SELECT VENDOR_CD,NULL,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(APPROVED_DT,'DD/MM/YYYY'),INVOICE_TYPE,'OTH',VENDOR_TYPE "
					+ "FROM FMS_OTH_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND INVOICE_TYPE IN ("+inv_type+") "
					+ "AND TO_CHAR(INVOICE_DT,'MM/YYYY')=TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'MM/YYYY') AND INVOICE_NO IS NOT NULL AND APPROVED_FLAG=? "
					//+ "AND INVOICE_CATEGORY=? "//AND TAX_STRUCT_CD IS NOT NULL AND TAX_STRUCT_CD > 0
					+ "";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, "01/"+month+"/"+year);
			stmt.setString(3, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				String contprty_cd=rset.getString(1)==null?"":rset.getString(1);
				String contprty_nm="";
				String contprty_abbr="";
				String entity_role=rset.getString(8)==null?"":rset.getString(8);
				
				String queryString1 = "SELECT A.ENTITY_CD,A.ENTITY_NAME,A.ENTITY_ABBR,A.BUSINESS_FLAG "
						+ "FROM FMS_OTH_ENTITY_MST A "
						+ "WHERE A.ENTITY_TYPE = ? "
						+ "AND EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS_OTH_ENTITY_MST B WHERE A.ENTITY_TYPE = B.ENTITY_TYPE AND A.ENTITY_CD=B.ENTITY_CD) ";
				queryString1	+= "AND A.ACTIVE_FLAG = 'Y' AND ENTITY_CD=?";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1,entity_role);
				stmt1.setString(2,contprty_cd);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					contprty_nm = rset1.getString(2)==null?"":rset1.getString(2);
					contprty_abbr = rset1.getString(3)==null?"":rset1.getString(3);
				}
				rset1.close();
				stmt1.close();
				
				VCOUNTERPARTY_CD.add(contprty_cd);
				VCOUNTERPARTY_NM.add(contprty_nm);
				VCOUNTERPARTY_ABBR.add(contprty_abbr);
				
				String cont_type=rset.getString(2)==null?"":rset.getString(2);
				VCONTRACT_TYPE.add(cont_type);
				VCONTRACT_TYPE_NM.add(utilBean.getContractTypeName(cont_type));
				
				String inv_no=rset.getString(3)==null?"":rset.getString(3);
				VINVOICE_NO.add(inv_no);
				VINVOICE_DT.add(rset.getString(4)==null?"":rset.getString(4));
				VAPPROVE_DT.add(rset.getString(5)==null?"":rset.getString(5));
				
				String inv_flag=rset.getString(6)==null?"":rset.getString(6);
				String inv_type_cete=inv_flag;//rset.getString(7)==null?"":rset.getString(7);
				inv_type=inv_flag;
				
				VSYS_OR_FF_INV.add(inv_flag);
				
				VINVOICE_TYPE.add(inv_type);
				String inv_type_nm=getInvoiceTypeName(inv_type);
				inv_type_nm=!inv_type_cete.equals("")?inv_type_nm+" <span style='background-color: #ffe6e6;'>["+inv_type_cete+"]</span>":inv_type_nm;
				VINVOICE_TYPE_NM.add(inv_type_nm);
				
				String irn_no="";
				queryString1="SELECT IRN_NO "
						+ "FROM FMS_OTH_INV_IRN_DTL "
						+ "WHERE COMPANY_CD=? AND INVOICE_NO=? ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, inv_no);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					irn_no=rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();
				
				VIS_IRN_GENERATED.add(irn_no.equals("")?"N":"Y");
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public String getInvoiceTypeName(String inv_type)
	{
		String function_nm="getInvoiceTypeName()";
		String inv_nm="";
		try
		{
			if(inv_type.equals("UG"))
			{
				inv_nm="SUG Invoice";
			}
			else if(inv_type.equals("SI"))
			{
				inv_nm="Re-Gas Invoice";
			}
			else if(inv_type.equals("ST"))
			{
				inv_nm="Storage Invoice";
			}
			else if(inv_type.equals("DI"))
			{
				inv_nm="Deficiency Invoice";
			}
			else if(inv_type.equals("TLU"))
			{
				inv_nm="TLU Charges";
			}
			else if(inv_type.equals("DTM"))
			{
				inv_nm="DLNG Transport Mgmt Service";
			}
			else if(inv_type.equals("CR"))
			{
				inv_nm="Credit Note";
			}
			else if(inv_type.equals("DR"))
			{
				inv_nm="Debit Note";
			}
			else if(inv_type.equals("CCR"))
			{
				inv_nm="Commercial Credit Note";
			}
			else if(inv_type.equals("CDR"))
			{
				inv_nm="Commercial Debit Note";
			}
			else if(inv_type.equals("LP"))
			{
				inv_nm="Late Payment";
			}
			else if(inv_type.equals("OR"))
			{
				inv_nm="Other";
			}
			else if(inv_type.equals("COSTR"))
			{
				inv_nm="Cost Recharge";
			}
			else if(inv_type.equals("COSTRH"))
			{
				inv_nm="Cost Recharge HPPL";
			}
			else if(inv_type.equals("HSA"))
			{
				inv_nm="Berthing Invoice (HPPL Shipping Agent)";
			}
			else if(inv_type.equals("HS"))
			{
				inv_nm="PFA Fees Invoice (HPPL-SEIPL)";
			}
			else if(inv_type.equals("SFA"))
			{
				inv_nm="Scrap Fixed Asset";
			}
			else if(inv_type.equals("NPR"))
			{
				inv_nm="NPR";
			}
			else if(inv_type.equals("AHPL"))
			{
				inv_nm="AHPL Invoice (AHPL Revenue Share)";
			}
			else
			{
				inv_nm=inv_type;
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return inv_nm;
	}
	
	String callFlag = "";
	public void setCallFlag(String callFlag) {this.callFlag = callFlag;}
	String comp_cd = "";
	public void setComp_cd(String comp_cd) {this.comp_cd = comp_cd;}
	
	String month = "";
	String year = "";
	String invoice_type = "";
	
	public void setMonth(String month) {this.month = month;}
	public void setYear(String year) {this.year = year;}
	public void setInvoice_type(String invoice_type) {this.invoice_type = invoice_type;}
	
	Vector VCOUNTERPARTY_CD = new Vector();
	Vector VCOUNTERPARTY_NM = new Vector();
	Vector VCOUNTERPARTY_ABBR = new Vector();
	Vector VCONTRACT_TYPE = new Vector();
	Vector VCONTRACT_TYPE_NM = new Vector();
	Vector VINVOICE_NO = new Vector();
	Vector VINVOICE_DT = new Vector();
	Vector VAPPROVE_DT = new Vector();
	Vector VINVOICE_TYPE = new Vector();
	Vector VINVOICE_TYPE_NM = new Vector();
	Vector VIS_IRN_GENERATED = new Vector();
	Vector VSYS_OR_FF_INV = new Vector();
	
	public Vector getVCOUNTERPARTY_CD() {return VCOUNTERPARTY_CD;}
	public Vector getVCOUNTERPARTY_NM() {return VCOUNTERPARTY_NM;}
	public Vector getVCOUNTERPARTY_ABBR() {return VCOUNTERPARTY_ABBR;}
	public Vector getVCONTRACT_TYPE() {return VCONTRACT_TYPE;}
	public Vector getVCONTRACT_TYPE_NM() {return VCONTRACT_TYPE_NM;}
	public Vector getVINVOICE_NO() {return VINVOICE_NO;}
	public Vector getVINVOICE_DT() {return VINVOICE_DT;}
	public Vector getVAPPROVE_DT() {return VAPPROVE_DT;}
	public Vector getVINVOICE_TYPE() {return VINVOICE_TYPE;}
	public Vector getVINVOICE_TYPE_NM() {return VINVOICE_TYPE_NM;}
	public Vector getVIS_IRN_GENERATED() {return VIS_IRN_GENERATED;}
	public Vector getVSYS_OR_FF_INV() {return VSYS_OR_FF_INV;}
}		
