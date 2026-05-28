package com.etrm.fms.extn_interface;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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

//Coded By          : Deep Tank
//Code Reviewed by	:  
//CR Date			: 05/01/2026
//Status	  		: Developing

public class DataBean_oth_inv_sun_interface
{
	String db_src_file_name="DataBean_oth_inv_sun_interface.java";
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
	XmlUtilBean xmlUtil = new XmlUtilBean();
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf3 = new DecimalFormat("###########0.000");
	
	public HttpServletRequest request = null;
	public void setRequest(HttpServletRequest request) {this.request = request;}
	
	public void init()
	{
		String function_nm="init()";
		synchronized(this)		//for handling parallel generation of SUN XML
		{
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
						if(callFlag.equalsIgnoreCase("SUN_ENTITY_ACC_CD"))
						{
							getVendorlist();
							getAccountDetails();
						}
						else if(callFlag.equalsIgnoreCase("OTH_INV_SUN_APPROVAL"))
						{
							getSegment();
							getOtherInvoiceDtl();
						}
						else if(callFlag.equalsIgnoreCase("OTH_INV_SUN_XML_GENERATION"))
						{
							getSunApprovedList();
						}
						if(callFlag.equalsIgnoreCase("GENERATE_OTH_INV_SUN_XML"))
						{
							getSunApprovedList();
							generateOthInvSunXML();
						}
						else if(callFlag.equalsIgnoreCase("OTH_INV_SUN_XML_DOWNLOAD"))
						{
							getXmlFilesList();
							parseSunXML();
						}
						else if(callFlag.equalsIgnoreCase("OTH_INV_SUN_XML_GEN"))
						{
							getInvoiceData();
						}
						else if(callFlag.equalsIgnoreCase("SUN_XML_DATA"))
						{
							doClearVec();
							getInvoiceData();
							generateOthInvSunXML();
						}
						else if(callFlag.equalsIgnoreCase("PARSE_SUN_XML"))
						{
							getFilenm();
							parseSunXML();
							getApprovedDtl();
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
		
	}
	
	public void getVendorlist()
	{
		String function_nm="getVendorlist()";
		try
		{
			String queryString = "SELECT A.ENTITY_CD,A.ENTITY_NAME,A.ENTITY_ABBR "
					+ "FROM FMS_OTH_ENTITY_MST A "
					+ "WHERE A.ENTITY_TYPE = ? "
					+ "AND EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS_OTH_ENTITY_MST B WHERE A.ENTITY_TYPE = B.ENTITY_TYPE AND A.ENTITY_CD=B.ENTITY_CD) "
					+ "AND A.ACTIVE_FLAG = ? ORDER BY A.ENTITY_ABBR";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, "V");
			stmt.setString(2, "Y");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VVENDOR_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VVENDOR_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VVENDOR_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getAccountDetails()
	{
		String function_nm="getAccountDetails()";
		try
		{
			for(int i=0;i<VVENDOR_CD.size();i++)
			{
				String queryString1="SELECT ACCOUNT_CODE FROM FMS_OTH_ENTITY_MST "
						+ "WHERE ENTITY_TYPE=? AND ENTITY_CD=? ";
				stmt4=conn.prepareStatement(queryString1);
				stmt4.setString(1, "V");
				stmt4.setString(2, ""+VVENDOR_CD.elementAt(i));
				rset4 = stmt4.executeQuery();
				if(rset4.next())
				{
					VSUN_ACCOUNT.add(rset4.getString(1)==null?"":rset4.getString(1));
				}
				else
				{
					VSUN_ACCOUNT.add("");
				}
				rset4.close();
				stmt4.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public String getEntity_nm(String cd,String type)
	{
		String function_nm="getEntity_nm()";
		String entity_nm="";
		try
		{
			String queryString0 = "SELECT A.ENTITY_NAME,A.ENTITY_ABBR "
					+ "FROM FMS_OTH_ENTITY_MST A "
					+ "WHERE A.ENTITY_CD=? AND A.ENTITY_TYPE = ? "
					+ "AND EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS_OTH_ENTITY_MST B WHERE A.ENTITY_TYPE = B.ENTITY_TYPE AND A.ENTITY_CD=B.ENTITY_CD) ";
			stmt0 = conn.prepareStatement(queryString0);
			stmt0.setString(1, cd);
			stmt0.setString(2, type);
			rset0 = stmt0.executeQuery();
			while(rset0.next()) 
			{
				entity_nm = rset0.getString(1)==null?"":rset0.getString(1);
			}
			rset0.close();
			stmt0.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return entity_nm;
	}
	
	public String getEntity_abbr(String cd,String type)
	{
		String function_nm="getEntity_abbr()";
		String entity_abbr="";
		try
		{
			String queryString0 = "SELECT A.ENTITY_NAME,A.ENTITY_ABBR "
					+ "FROM FMS_OTH_ENTITY_MST A "
					+ "WHERE A.ENTITY_CD=? AND A.ENTITY_TYPE = ? "
					+ "AND EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS_OTH_ENTITY_MST B WHERE A.ENTITY_TYPE = B.ENTITY_TYPE AND A.ENTITY_CD=B.ENTITY_CD) ";
			stmt0 = conn.prepareStatement(queryString0);
			stmt0.setString(1, cd);
			stmt0.setString(2, type);
			rset0 = stmt0.executeQuery();
			while(rset0.next()) 
			{
				entity_abbr = rset0.getString(2)==null?"":rset0.getString(2);
			}
			rset0.close();
			stmt0.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return entity_abbr;
	}
	
	public void getSegment()
	{
		String function_nm="getSegment()";
		try
		{
			VSEGMENT.add("RCM Invoice(Cost Recharge)");
			VSEGMENT.add("Cost Recharge HPPL");
			VSEGMENT.add("Berthing Invoice (HPPL Shipping Agent)");
			VSEGMENT.add("PFA Fees Invoice (HPPL-SEIPL)");
			VSEGMENT.add("Scrap Fixed Asset");
//			VSEGMENT.add("NPR");
			VSEGMENT.add("AHPL Invoice (AHPL Revenue Share)");
//			VSEGMENT.add("GNA Invoice");
			
			VSEGMENT_TYPE.add("COSTR");
			VSEGMENT_TYPE.add("COSTRH");
			VSEGMENT_TYPE.add("HSA");
			VSEGMENT_TYPE.add("HS");
			VSEGMENT_TYPE.add("SFA");
//			VSEGMENT_TYPE.add("NPR");
			VSEGMENT_TYPE.add("AHPL");
//			VSEGMENT_TYPE.add("GA");
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getOtherInvoiceDtl()
	{
		String function_nm="getOtherInvoiceDtl()";
		
		try
		{
			if(segment.equals("COSTR"))
			{
				VTEMP_SEGMENT_TYPE.add("COSTR");
				VTEMP_SEGMENT.add("RCM Invoice(Cost Recharge)");
			}
			else if(segment.equals("COSTRH"))
			{
				VTEMP_SEGMENT_TYPE.add("COSTRH");
				VTEMP_SEGMENT.add("Cost Recharge HPPL");
			}
			else if(segment.equals("HSA"))
			{
				VTEMP_SEGMENT_TYPE.add("HSA");
				VTEMP_SEGMENT.add("Berthing Invoice (HPPL Shipping Agent)");
			}
			else if(segment.equals("HS"))
			{
				VTEMP_SEGMENT_TYPE.add("HS");
				VTEMP_SEGMENT.add("PFA Fees Invoice (HPPL-SEIPL)");
			}
			else if(segment.equals("SFA"))
			{
				VTEMP_SEGMENT_TYPE.add("SFA");
				VTEMP_SEGMENT.add("Scrap Fixed Asset");
			}
			else if(segment.equals("NPR"))
			{
				VTEMP_SEGMENT_TYPE.add("NPR");
				VTEMP_SEGMENT.add("NPR");
			}
			else if(segment.equals("AHPL"))
			{
				VTEMP_SEGMENT_TYPE.add("AHPL");
				VTEMP_SEGMENT.add("AHPL Invoice (AHPL Revenue Share)");
			}
			else if(segment.equals("GA"))
			{
				VTEMP_SEGMENT_TYPE.add("GA");
				VTEMP_SEGMENT.add("GNA Invoice");
			}
			else
			{
				VTEMP_SEGMENT_TYPE=VSEGMENT_TYPE;
				VTEMP_SEGMENT=VSEGMENT;
			}
			
			for(int i=0;i<VTEMP_SEGMENT_TYPE.size();i++)
			{
				int index=0;
				String queryString="";
				int ctn=0;
				
				queryString="SELECT COMPANY_CD,VENDOR_CD,SUPPLIER_CD,INVOICE_TYPE,FINANCIAL_YEAR,BU_UNIT, "
						+ "BU_STATE_TIN,INVOICE_SEQ,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),"
						+ "TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'),GROSS_AMT,TAX_AMT_INR,NET_PAYABLE,INVOICE_RAISED_IN,"
						+ "VENDOR_TYPE,SAP_APPROVAL,SAP_APPROVED_BY,TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY'),INVOICE_CATEGORY,INV_FLAG "
						+ "FROM FMS_OTH_INVOICE_MST A "
						+ "WHERE COMPANY_CD=? "
						+ "AND INVOICE_DT>=TO_DATE(?,'DD/MM/YYYY') AND INVOICE_DT<=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND APPROVED_FLAG=? AND INVOICE_ID_SEQ IS NOT NULL AND NET_PAYABLE IS NOT NULL "
						+ "AND PDF_INV_DTL IS NOT NULL AND INVOICE_TYPE=? ORDER BY INVOICE_DT";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(++ctn, comp_cd);
				stmt.setString(++ctn, from_dt);
				stmt.setString(++ctn, to_dt);
				stmt.setString(++ctn, "Y");
				stmt.setString(++ctn, ""+VTEMP_SEGMENT_TYPE.elementAt(i));
				rset=stmt.executeQuery();
				while(rset.next())
				{
					index++;
					String companyCd = rset.getString(1)==null?"":rset.getString(1);
					String vendor_cd = rset.getString(2)==null?"":rset.getString(2);
					String supplier_cd = rset.getString(3)==null?"":rset.getString(3);
					String inv_type = rset.getString(4)==null?"":rset.getString(4);
					String finan_yr = rset.getString(5)==null?"":rset.getString(5);
					String bu_seq = rset.getString(6)==null?"":rset.getString(6);
					String bu_state_tin = rset.getString(7)==null?"":rset.getString(7);
					String inv_seq = rset.getString(8)==null?"":rset.getString(8);
					String inv_no = rset.getString(9)==null?"":rset.getString(9);
					String inv_dt = rset.getString(10)==null?"":rset.getString(10);
					String period_st_dt = rset.getString(11)==null?"":rset.getString(11);
					String period_end_dt = rset.getString(12)==null?"":rset.getString(12);
					String inv_due_dt = rset.getString(13)==null?"":rset.getString(13);
					double gross_amt = rset.getDouble(14);
					double tax_amt = rset.getDouble(15);
					double net_payable = rset.getDouble(16);
					String invoice_raised_in=rset.getString(17)==null?"":rset.getString(17);
					String vendor_type=rset.getString(18)==null?"":rset.getString(18);
					String sun_approved_by = rset.getString(20)==null?"":rset.getString(20);
					String sun_approved_dt = rset.getString(21)==null?"":rset.getString(21);
					String inv_category=rset.getString(22)==null?"":rset.getString(22);
					String inv_flag = rset.getString(23)==null?"":rset.getString(23);
					
					if(inv_flag.equals("CR"))
					{
						gross_amt = (-1)*gross_amt;
						tax_amt = (-1)*tax_amt;
						net_payable = (-1)*net_payable;
					}

					VVENDOR_CD.add(vendor_cd);
					VSUPPLIER_CD.add(supplier_cd);
					VVENDOR_NM.add(""+getEntity_abbr(vendor_cd,vendor_type));
					VINVOICE_TYPE.add(inv_type);
					VFINANCIAL_YEAR.add(finan_yr);
					VBU_SEQ.add(bu_seq);
					VBU_NM.add(""+utilBean.getCounterpartyPlantABBR(conn,comp_cd, comp_cd, bu_seq, "B"));
					VBU_STATE_TIN.add(bu_state_tin);
					VINVOICE_SEQ.add(inv_seq);
					VINVOICE_NO.add(inv_no);
					VINVOICE_DT.add(inv_dt);
					VPERIOD_START_DT.add(period_st_dt);
					VPERIOD_END_DT.add(period_end_dt);
					VINVOICE_DUE_DT.add(inv_due_dt);
					VINVOICE_RAISED_IN.add(""+utilBean.getRateUnitNm(conn,invoice_raised_in));
					VPAYMENT_DONE_IN.add(""+utilBean.getRateUnitNm(conn,invoice_raised_in));
					VGROSS_AMT.add(nf.format(gross_amt));
					VTAX_AMT.add(nf.format(tax_amt));
					VINVOICE_AMT.add(nf.format(net_payable));
					VNET_PAYABLE_AMT.add(nf.format(net_payable));
					VSAP_APPROVAL_FLAG.add(rset.getString(19)==null?"":rset.getString(19)); 
					VAPPROVE_HIST.add(utilBean.getEmpName(conn, sun_approved_by)+"<br>"+sun_approved_dt);
					if(inv_category.equals("S"))
					{
						VCASH_FLOW.add("Service");
					}
					else 
					{
						VCASH_FLOW.add("Product");
					}
					VINV_FLAG.add(inv_flag);
				}
				rset.close();
				stmt.close();
				VINDEX.add(index);

			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//for generating the report 
	public void getSunApprovedList()
	{
		String function_nm="getSunApprovedList()";
		try
		{
			String vendor_cd="";
		    String fin_year="";
		    String supplier_cd="";
		    String invoice_seq="";
		    String invoice_no="";
		    String invoice_dt="";
		    String gross_amt="";
		    String tax_amt="";
		    String inv_due_dt="";
		    String accountPeriod="";
		    String inv_raised_in="";
		    String bucac="SEI";	
		    String invoice_type="";
		    String inv_flag="";
		    String net_payable_amt="";
		    String criteria="";
		    String company_cd="";
		    String account_cd="";
		    int ctn=0;
		    String queryString="";
		    
		    queryString="SELECT A.COMPANY_CD,A.VENDOR_CD,A.SUPPLIER_CD,A.INVOICE_TYPE,A.FINANCIAL_YEAR,A.INVOICE_SEQ,A.INVOICE_NO,"
		    		+ "TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),A.INVOICE_RAISED_IN,A.GROSS_AMT,A.TAX_AMT_INR,"
					+ "A.NET_PAYABLE,A.INVOICE_CATEGORY,A.VENDOR_TYPE,A.INV_FLAG,A.REF_NO,A.CRITERIA " 
					+ "FROM FMS_OTH_INVOICE_MST A "
					+ "WHERE A.COMPANY_CD=? AND A.SAP_APPROVAL=? AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
	    			+ "AND TO_DATE(TO_CHAR(A.SAP_APPROVED_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') "
	    			+ "AND NOT EXISTS(SELECT B.PDF_TYPE FROM FMS_OTH_INV_FILE_DTL B WHERE B.COMPANY_CD=A.COMPANY_CD AND A.INVOICE_SEQ=B.INVOICE_SEQ "
	    			+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR AND A.INVOICE_TYPE=B.INVOICE_TYPE AND A.SUPPLIER_CD = B.SUPPLIER_CD AND B.PDF_TYPE=?) ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, "Y");
			stmt.setString(++ctn, from_dt);
			stmt.setString(++ctn, to_dt);
			stmt.setString(++ctn, "S");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				company_cd=rset.getString(1)==null?"":rset.getString(1);
				vendor_cd=rset.getString(2)==null?"":rset.getString(2);
				supplier_cd=rset.getString(3)==null?"":rset.getString(3);
				invoice_type=rset.getString(4)==null?"":rset.getString(4);
				fin_year=rset.getString(5)==null?"":rset.getString(5);
				invoice_seq=rset.getString(6)==null?"":rset.getString(6);
				invoice_no=rset.getString(7)==null?"":rset.getString(7);
				invoice_dt=rset.getString(8)==null?"":rset.getString(8);
				inv_due_dt=rset.getString(9)==null?"":rset.getString(9);
				gross_amt=rset.getString(11)==null?"":nf.format(rset.getDouble(11));
				tax_amt=rset.getString(12)==null?"":nf.format(rset.getDouble(12));
				net_payable_amt=rset.getString(13)==null?"":nf.format(rset.getDouble(13));
				inv_flag=rset.getString(16)==null?"":rset.getString(16);
				accountPeriod=getAccountingPeriod(invoice_dt);
				String invDt = invoice_dt.replace("/", "");
				String dueDt=inv_due_dt.replace("/", "");
				String tax="";
				
				if(inv_flag.equals("CR")) 
				{
				    double gross = Double.parseDouble(gross_amt);
				    double tax1 = Double.parseDouble(tax_amt);
				    double net = Double.parseDouble(net_payable_amt);

				    gross = -gross;
				    tax1 = -tax1;
				    net = -net;

				    gross_amt = String.valueOf(gross);
				    tax_amt = String.valueOf(tax1);
				    net_payable_amt = String.valueOf(net);
				}

				
				String tax_cd = "",desc="";
				String query1 = "SELECT TAX_CD,ITEM_DESCRIPTION "
						+ "FROM FMS_OTH_INVOICE_DTL  "
						+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? "
						+ "AND INVOICE_NO=? AND SUPPLIER_CD=?";
				stmt1 = conn.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, invoice_type);
				stmt1.setString(3, fin_year);
				stmt1.setString(4, invoice_seq);
				stmt1.setString(5, invoice_no);
				stmt1.setString(6, supplier_cd);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					tax_cd = rset1.getString(1)==null?"":rset1.getString(1);
					desc = rset1.getString(2)==null?"":rset1.getString(2);
				}
				rset1.close();
				stmt1.close();
				
				VINVOICE_SEQ.add(invoice_seq);
				VSUPPLIER_CD.add(supplier_cd);
				VINVOICE_TYPE.add(invoice_type);
				VFINANCIAL_YEAR.add(fin_year);
				VINV_FLAG.add(inv_flag);
				
				if(invoice_type.equals("COSTR"))
				{
					account_cd=getotherentitySunAccountCode(vendor_cd,"V");
					if(inv_flag.equals("CR") || inv_flag.equals("DR"))
					{
						for(int i=0;i<2;i++)
						{
							VPERIOD_START_DT.add(accountPeriod);
							VAPPROVAL_DT.add(invDt);
							VINVOICE_NO.add(invoice_no);
							VDESC.add(desc);
							if(i==0)
							{
								VACCOUNT_CD.add(account_cd);
								VBASE_AMT.add(net_payable_amt);
								VDEBIT_CREDIT.add("C");
							}
							else if(i==1)
							{
								VACCOUNT_CD.add("");
								VBASE_AMT.add(net_payable_amt);
								VDEBIT_CREDIT.add("D");
							}
							VCOA_CD.add("");
							VCURRENCY_CD.add("INR");
							if(supplier_cd.equals("1"))
							{
								VEMPLOYEE_CD.add("PNA");
								VCOST_CTR_CD.add("1PO01");
								VBU_UNIT_CD.add("PRT");
							}
							else
							{
								VEMPLOYEE_CD.add("LNA");
								VCOST_CTR_CD.add("1TM01");
								VBU_UNIT_CD.add("LNG");
							}
							VCODE.add("NA");
							VINVOICE_DUE_DT.add(dueDt);
							VINVOICE_DT.add(invDt);
						}
					}
					else
					{
						for(int i=0;i<2;i++)
						{
							VPERIOD_START_DT.add(accountPeriod);
							VAPPROVAL_DT.add(invDt);
							VINVOICE_NO.add(invoice_no);
							VDESC.add(desc);
							if(i==0)
							{
								VACCOUNT_CD.add(account_cd);
								VBASE_AMT.add(net_payable_amt);
								VDEBIT_CREDIT.add("D");
							}
							else if(i==1)
							{
								VACCOUNT_CD.add("");
								VBASE_AMT.add(net_payable_amt);
								VDEBIT_CREDIT.add("C");
							}
							VCOA_CD.add("");
							VCURRENCY_CD.add("INR");
							if(supplier_cd.equals("1"))
							{
								VEMPLOYEE_CD.add("PNA");
								VCOST_CTR_CD.add("1PO01");
								VBU_UNIT_CD.add("PRT");
							}
							else
							{
								VEMPLOYEE_CD.add("LNA");
								VCOST_CTR_CD.add("1TM01");
								VBU_UNIT_CD.add("LNG");
							}
							VCODE.add("NA");
							VINVOICE_DUE_DT.add(dueDt);
							VINVOICE_DT.add(invDt);
						}
					}
				}
				else if(invoice_type.equals("COSTRH"))
				{
					tax = nf.format(Double.parseDouble(tax_amt)/2);
					account_cd = "161001";
					
					if(inv_flag.equals("CR") || inv_flag.equals("DR"))
					{
						for(int i=0;i<4;i++)
						{
							VPERIOD_START_DT.add(accountPeriod);
							VAPPROVAL_DT.add(invDt);
							VINVOICE_NO.add(invoice_no);
							VDESC.add(desc);
							if(i==0)
							{
								VACCOUNT_CD.add(account_cd);
								VBASE_AMT.add(net_payable_amt);
								VDEBIT_CREDIT.add("D");
							}
							else if(i==1)
							{
								VACCOUNT_CD.add("825009");
								VBASE_AMT.add(gross_amt);
								VDEBIT_CREDIT.add("C");
							}
							else if(i==2)
							{
								VACCOUNT_CD.add("245077");
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("C");
							}
							else if(i==3)
							{
								VACCOUNT_CD.add("245078");
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("C");
							}
							VCOA_CD.add("825009");
							VCURRENCY_CD.add("INR");
							VEMPLOYEE_CD.add("PNA");
							VCOST_CTR_CD.add("1FN01");
							VBU_UNIT_CD.add("LNG");
							VCODE.add("NA");
							VINVOICE_DUE_DT.add(dueDt);
							VINVOICE_DT.add(invDt);
						}
					}
					else 
					{
						for(int i=0;i<4;i++)
						{
							VPERIOD_START_DT.add(accountPeriod);
							VAPPROVAL_DT.add(invDt);
							VINVOICE_NO.add(invoice_no);
							VDESC.add(desc);
							if(i==0)
							{
								VACCOUNT_CD.add(account_cd);
								VBASE_AMT.add(net_payable_amt);
								VDEBIT_CREDIT.add("C");
							}
							else if(i==1)
							{
								VACCOUNT_CD.add("825009");
								VBASE_AMT.add(gross_amt);
								VDEBIT_CREDIT.add("D");
							}
							else if(i==2)
							{
								VACCOUNT_CD.add("245077");
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("D");
							}
							else if(i==3)
							{
								VACCOUNT_CD.add("245078");
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("D");
							}
							VCOA_CD.add("825009");
							VCURRENCY_CD.add("INR");
							VEMPLOYEE_CD.add("PNA");
							VCOST_CTR_CD.add("1FN01");
							VBU_UNIT_CD.add("LNG");
							VCODE.add("NA");
							VINVOICE_DUE_DT.add(dueDt);
							VINVOICE_DT.add(invDt);
						}
					}
				}
				else if(invoice_type.equals("HSA"))
				{
					tax = nf.format(Double.parseDouble(tax_amt)/2);
					account_cd=getotherentitySunAccountCode(vendor_cd,"V");
					
					if(inv_flag.equals("CR") || inv_flag.equals("DR"))
					{
						for(int i=0;i<4;i++)
						{
							VPERIOD_START_DT.add(accountPeriod);
							VAPPROVAL_DT.add(invDt);
							VINVOICE_NO.add(invoice_no);
							VDESC.add(desc);
							if(i==0)
							{
								VACCOUNT_CD.add(account_cd);
								VBASE_AMT.add(net_payable_amt);
								VDEBIT_CREDIT.add("C");
							}
							else if(i==1)
							{
								VACCOUNT_CD.add("711003");
								VBASE_AMT.add(gross_amt);
								VDEBIT_CREDIT.add("D");
							}
							else if(i==2)
							{
								VACCOUNT_CD.add("245077");
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("D");
							}
							else if(i==3)
							{
								VACCOUNT_CD.add("245078");
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("D");
							}
							VCOA_CD.add("711003");
							VCURRENCY_CD.add("USD");
							VEMPLOYEE_CD.add("PNA");
							VCOST_CTR_CD.add("1PO40");
							VBU_UNIT_CD.add("PRT");
							VCODE.add("NA");
							VINVOICE_DUE_DT.add(dueDt);
							VINVOICE_DT.add(invDt);
						}
					}
					else 
					{
						for(int i=0;i<4;i++)
						{
							VPERIOD_START_DT.add(accountPeriod);
							VAPPROVAL_DT.add(invDt);
							VINVOICE_NO.add(invoice_no);
							VDESC.add(desc);
							if(i==0)
							{
								VACCOUNT_CD.add(account_cd);
								VBASE_AMT.add(net_payable_amt);
								VDEBIT_CREDIT.add("D");
							}
							else if(i==1)
							{
								VACCOUNT_CD.add("711003");
								VBASE_AMT.add(gross_amt);
								VDEBIT_CREDIT.add("C");
							}
							else if(i==2)
							{
								VACCOUNT_CD.add("245077");
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("C");
							}
							else if(i==3)
							{
								VACCOUNT_CD.add("245078");
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("C");
							}
							VCOA_CD.add("711003");
							VCURRENCY_CD.add("USD");
							VEMPLOYEE_CD.add("PNA");
							VCOST_CTR_CD.add("1PO40");
							VBU_UNIT_CD.add("PRT");
							VCODE.add("NA");
							VINVOICE_DUE_DT.add(dueDt);
							VINVOICE_DT.add(invDt);
						}
					}
				}
				else if(invoice_type.equals("HS"))
				{
					account_cd = "161002";
					tax = nf.format(Double.parseDouble(tax_amt)/2);
				
					if(inv_flag.equals("CR") || inv_flag.equals("DR"))
					{
						for(int i=0;i<4;i++)
						{
							VPERIOD_START_DT.add(accountPeriod);
							VAPPROVAL_DT.add(invDt);
							VINVOICE_NO.add(invoice_no);
							VDESC.add(desc);
							if(i==0)
							{
								VACCOUNT_CD.add(account_cd);
								VBASE_AMT.add(net_payable_amt);
								VDEBIT_CREDIT.add("C");
							}
							else if(i==1)
							{
								VACCOUNT_CD.add("711004");
								VBASE_AMT.add(gross_amt);
								VDEBIT_CREDIT.add("D");
							}
							else if(i==2)
							{
								VACCOUNT_CD.add("245077");
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("D");
							}
							else if(i==3)
							{
								VACCOUNT_CD.add("245078");
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("D");
							}
							VCOA_CD.add("711004");
							VCURRENCY_CD.add("INR");
							VEMPLOYEE_CD.add("PNA");
							VCOST_CTR_CD.add("1PO40");
							VBU_UNIT_CD.add("PRT");
							VCODE.add("NA");
							VINVOICE_DUE_DT.add(dueDt);
							VINVOICE_DT.add(invDt);
						}
					}
					else 
					{
						for(int i=0;i<4;i++)
						{
							VPERIOD_START_DT.add(accountPeriod);
							VAPPROVAL_DT.add(invDt);
							VINVOICE_NO.add(invoice_no);
							VDESC.add(desc);
							if(i==0)
							{
								VACCOUNT_CD.add(account_cd);
								VBASE_AMT.add(net_payable_amt);
								VDEBIT_CREDIT.add("D");
							}
							else if(i==1)
							{
								VACCOUNT_CD.add("711004");
								VBASE_AMT.add(gross_amt);
								VDEBIT_CREDIT.add("C");
							}
							else if(i==2)
							{
								VACCOUNT_CD.add("245077");
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("C");
							}
							else if(i==3)
							{
								VACCOUNT_CD.add("245078");
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("C");
							}
							VCOA_CD.add("711004");
							VCURRENCY_CD.add("INR");
							VEMPLOYEE_CD.add("PNA");
							VCOST_CTR_CD.add("1PO40");
							VBU_UNIT_CD.add("PRT");
							VCODE.add("NA");
							VINVOICE_DUE_DT.add(dueDt);
							VINVOICE_DT.add(invDt);
						}
					}
				}
				else if(invoice_type.equals("AHPL"))
				{
					account_cd = getotherentitySunAccountCode(vendor_cd,"V");
					tax = nf.format(Double.parseDouble(tax_amt)/2);
					
					if(inv_flag.equals("CR") || inv_flag.equals("DR"))
					{
						for(int i=0;i<5;i++)
						{
							VPERIOD_START_DT.add(accountPeriod);
							VAPPROVAL_DT.add(invDt);
							VINVOICE_NO.add(invoice_no);
							VDESC.add(desc);
							if(i==0)
							{
								VACCOUNT_CD.add(account_cd);
								VBASE_AMT.add(gross_amt);
								VDEBIT_CREDIT.add("C");
							}
							else if(i==1)
							{
								VACCOUNT_CD.add(account_cd);
								VBASE_AMT.add(tax_amt);
								VDEBIT_CREDIT.add("C");
							}
							else if(i==2)
							{
								VACCOUNT_CD.add("711006");
								VBASE_AMT.add(gross_amt);
								VDEBIT_CREDIT.add("D");
							}
							else if(i==3)
							{
								VACCOUNT_CD.add("245077");
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("D");
							}
							else if(i==4)
							{
								VACCOUNT_CD.add("245078");
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("D");
							}
							VCOA_CD.add("711006");
							VCURRENCY_CD.add("INR");
							VEMPLOYEE_CD.add("PNA");
							VCOST_CTR_CD.add("1PO40");
							VBU_UNIT_CD.add("PRT");
							VCODE.add("NA");
							VINVOICE_DUE_DT.add(dueDt);
							VINVOICE_DT.add(invDt);
						}
					}
					else 
					{
						for(int i=0;i<5;i++)
						{
							VPERIOD_START_DT.add(accountPeriod);
							VAPPROVAL_DT.add(invDt);
							VINVOICE_NO.add(invoice_no);
							VDESC.add(desc);
							if(i==0)
							{
								VACCOUNT_CD.add(account_cd);
								VBASE_AMT.add(gross_amt);
								VDEBIT_CREDIT.add("D");
							}
							else if(i==1)
							{
								VACCOUNT_CD.add(account_cd);
								VBASE_AMT.add(tax_amt);
								VDEBIT_CREDIT.add("D");
							}
							else if(i==2)
							{
								VACCOUNT_CD.add("711006");
								VBASE_AMT.add(gross_amt);
								VDEBIT_CREDIT.add("C");
							}
							else if(i==3)
							{
								VACCOUNT_CD.add("245077");
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("C");
							}
							else if(i==4)
							{
								VACCOUNT_CD.add("245078");
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("C");
							}
							VCOA_CD.add("711006");
							VCURRENCY_CD.add("INR");
							VEMPLOYEE_CD.add("PNA");
							VCOST_CTR_CD.add("1PO40");
							VBU_UNIT_CD.add("PRT");
							VCODE.add("NA");
							VINVOICE_DUE_DT.add(dueDt);
							VINVOICE_DT.add(invDt);
						}
					}
				}
				else if(invoice_type.equals("SFA"))
				{
					account_cd = getotherentitySunAccountCode(vendor_cd,"V");
					tax = nf.format(Double.parseDouble(tax_amt)/2);
					
					if(!tax_cd.equals("") && tax_cd.equals("I")) //for IGST
					{
						for(int i=0;i<3;i++)
						{
							VPERIOD_START_DT.add(accountPeriod);
							VAPPROVAL_DT.add(invDt);
							VINVOICE_NO.add(invoice_no);
							VDESC.add("TBD");
							if(i==0)
							{
								VACCOUNT_CD.add("243996");
								VBASE_AMT.add(net_payable_amt);
								VDEBIT_CREDIT.add("D");
							}
							else if(i==1)
							{
								VACCOUNT_CD.add("799999");
								VBASE_AMT.add(gross_amt);
								VDEBIT_CREDIT.add("C");
							}
							else if(i==2)
							{
								VACCOUNT_CD.add("245076");
								VBASE_AMT.add(tax_amt);
								VDEBIT_CREDIT.add("C");
							}
							
							VCOA_CD.add("799999");
							VCURRENCY_CD.add("INR");
							if(supplier_cd.equals("1"))
							{
								VEMPLOYEE_CD.add("PNA");
								VCOST_CTR_CD.add("1PO01");
								VBU_UNIT_CD.add("PRT");
							}
							else
							{
								VEMPLOYEE_CD.add("LNA");
								VCOST_CTR_CD.add("1TM01");
								VBU_UNIT_CD.add("LNG");
							}
							VCODE.add("NA");
							VINVOICE_DUE_DT.add(dueDt);
							VINVOICE_DT.add(invDt);
						}
					}
					else if(!tax_cd.equals("") && tax_cd.equals("C")) //for CGST,SGST
					{
						for(int i=0;i<4;i++)
						{
							VPERIOD_START_DT.add(accountPeriod);
							VAPPROVAL_DT.add(invDt);
							VINVOICE_NO.add(invoice_no);
							VDESC.add("TBD");
							if(i==0)
							{
								VACCOUNT_CD.add("243996");
								VBASE_AMT.add(net_payable_amt);
								VDEBIT_CREDIT.add("D");
							}
							else if(i==1)
							{
								VACCOUNT_CD.add("799999");
								VBASE_AMT.add(gross_amt);
								VDEBIT_CREDIT.add("C");
							}
							else if(i==2)
							{
								VACCOUNT_CD.add("245077");
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("C");
							}
							else if(i==3)
							{
								VACCOUNT_CD.add("245078");
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("C");
							}
							
							VCOA_CD.add("799999");
							VCURRENCY_CD.add("INR");
							if(supplier_cd.equals("1"))
							{
								VEMPLOYEE_CD.add("PNA");
								VCOST_CTR_CD.add("1PO01");
								VBU_UNIT_CD.add("PRT");
							}
							else
							{
								VEMPLOYEE_CD.add("LNA");
								VCOST_CTR_CD.add("1TM01");
								VBU_UNIT_CD.add("LNG");
							}
							VCODE.add("NA");
							VINVOICE_DUE_DT.add(dueDt);
							VINVOICE_DT.add(invDt);
						}
					}
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
	
	public void generateOthInvSunXML()
	{
		String function_nm="generateOthInvSunXML()";
		try
		{
			DocumentBuilderFactory docFactory = xmlUtil.dcoumentBuilderFactory();
		    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		    Document doc = docBuilder.newDocument();
			//root fmsng
		    Element fmsng = doc.createElement("SSC");
		    doc.appendChild(fmsng);
		    
		    //root elements
		    Element Payload = doc.createElement("Payload");
		    fmsng.appendChild(Payload);
		    Element Ledger = doc.createElement("Ledger");
		    //fmsng.appendChild(Ledger);
		    Payload.appendChild(Ledger);
		    
		    String xmlFileNm="";
		    String sysdate=utilDate.getSysdateWithTime24hrWithSS();		//FOR HANDLING THE PARALLEL SUBMIT ISSUE
		    sysdate=sysdate.replace("/", "_");
		    sysdate=sysdate.replace(" ", "_");
		    sysdate=sysdate.replace(":", "_");
		    String criteria="";
		    boolean xml_generate_flag=true;
		    
		    for(int i=0;i<VINVOICE_NO.size();i++)
		    {
				Element Line  = doc.createElement("Line");
				Element AccountCode = doc.createElement("AccountCode");
				Element AccountingPeriod = doc.createElement("AccountingPeriod");
				Element TransactionDate = doc.createElement("TransactionDate");
				Element TransactionReference = doc.createElement("TransactionReference");
				Element Description = doc.createElement("Description");
				Element BaseAmount = doc.createElement("BaseAmount");
				Element DebitCredit = doc.createElement("DebitCredit");
				Element TransactionAmount = doc.createElement("TransactionAmount");
				Element Base2ReportingAmount = doc.createElement("Base2ReportingAmount");
				Element CurrencyCode = doc.createElement("CurrencyCode");
				Element CurrencyRate = doc.createElement("CurrencyRate");
				Element JournalType = doc.createElement("JournalType");
				Element JournalSource = doc.createElement("JournalSource");			
				Element DueDate = doc.createElement("DueDate");
				Element AnalysisCode1 = doc.createElement("AnalysisCode1");			
				Element AnalysisCode2 = doc.createElement("AnalysisCode2");			
				Element AnalysisCode3 = doc.createElement("AnalysisCode3");			
				Element AnalysisCode4 = doc.createElement("AnalysisCode4");			
				Element AnalysisCode5 = doc.createElement("AnalysisCode5");			
				Element AnalysisCode6 = doc.createElement("AnalysisCode6");			
				Element AnalysisCode7 = doc.createElement("AnalysisCode7");			
				Element AnalysisCode8 = doc.createElement("AnalysisCode8");			
				Element AnalysisCode10 = doc.createElement("AnalysisCode10");			 
				Element GeneralDescription9 = doc.createElement("GeneralDescription9");			
				Element GeneralDescription10 = doc.createElement("GeneralDescription10");			
				Element GeneralDescription11 = doc.createElement("GeneralDescription11");			
				Element GeneralDescription12 = doc.createElement("GeneralDescription12");			
				Element GeneralDescription13 = doc.createElement("GeneralDescription13");			
				Element GeneralDescription14 = doc.createElement("GeneralDescription14");			
				Element GeneralDescription15 = doc.createElement("GeneralDescription15");			
				
				Ledger.appendChild(Line);
				Line.appendChild(AccountCode);
				Line.appendChild(AccountingPeriod);
				Line.appendChild(BaseAmount);
				Line.appendChild(DebitCredit);
				Line.appendChild(TransactionAmount);
				Line.appendChild(Base2ReportingAmount);
				Line.appendChild(CurrencyCode);
				Line.appendChild(CurrencyRate);
				Line.appendChild(TransactionDate);
				Line.appendChild(JournalType);
				Line.appendChild(JournalSource);		
				Line.appendChild(TransactionReference);
				Line.appendChild(Description);
				Line.appendChild(DueDate);
				Line.appendChild(AnalysisCode1);
				Line.appendChild(AnalysisCode2);		
				Line.appendChild(AnalysisCode3);		
				Line.appendChild(AnalysisCode4);		
				Line.appendChild(AnalysisCode5);		
				Line.appendChild(AnalysisCode6);		
				Line.appendChild(AnalysisCode7);		
				Line.appendChild(AnalysisCode8);		
				Line.appendChild(AnalysisCode10);		
				Line.appendChild(GeneralDescription9);		
				Line.appendChild(GeneralDescription10);		
				Line.appendChild(GeneralDescription11);		
				Line.appendChild(GeneralDescription12);		
				Line.appendChild(GeneralDescription13);		
				Line.appendChild(GeneralDescription14);		
				Line.appendChild(GeneralDescription15);	
				
				AccountCode.appendChild(doc.createTextNode(""+VACCOUNT_CD.elementAt(i)));
				DebitCredit.appendChild(doc.createTextNode(""+VDEBIT_CREDIT.elementAt(i)));
				AnalysisCode4.appendChild(doc.createTextNode(""+VCOA_CD.elementAt(i)));
				AccountingPeriod.appendChild(doc.createTextNode(""+VPERIOD_START_DT.elementAt(i)));
				//BaseAmount.appendChild(doc.createTextNode(invoice_amt));
				BaseAmount.appendChild(doc.createTextNode(""+VBASE_AMT.elementAt(i)));
				Base2ReportingAmount.appendChild(doc.createTextNode("0"));		
				CurrencyCode.appendChild(doc.createTextNode(""+VCURRENCY_CD.elementAt(i)));
				JournalType.appendChild(doc.createTextNode(""+VJOURNAL_TYPE.elementAt(i)));					
				TransactionDate.appendChild(doc.createTextNode(""+VINVOICE_DT.elementAt(i)));
				TransactionReference.appendChild(doc.createTextNode(""+VINVOICE_NO.elementAt(i)));
				Description.appendChild(doc.createTextNode(""+VDESC.elementAt(i)));
				DueDate.appendChild(doc.createTextNode(""+VINVOICE_DUE_DT.elementAt(i)));
				AnalysisCode2.appendChild(doc.createTextNode(""+VCOST_CTR_CD.elementAt(i)));
				AnalysisCode3.appendChild(doc.createTextNode(""+VEMPLOYEE_CD.elementAt(i)));
				AnalysisCode7.appendChild(doc.createTextNode("NA"));
				AnalysisCode10.appendChild(doc.createTextNode(""+VBU_UNIT_CD.elementAt(i)));
		    }
				
			//for saving the file info in the table 
		    for(int i=0;i<VINVOICE_SEQ.size();i++)
		    {
				if(!sysdate.equals(""))
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
			        String sub_folder=""+CommonVariable.sun_xml;
					
					File SubDir = new File(appPath+File.separator+main_folder+File.separator+sub_folder);
			        if(!SubDir.exists()) 
			        {
			        	SubDir.mkdir();
			        }
			        
			        File temp [] = new File(appPath+File.separator+main_folder+File.separator+sub_folder).listFiles();
			        
			        xmlFileNm="OTH_INV_"+sysdate+".xml";
			        for(File filename : temp)
			        {
			        	if(filename.getName().equals(sysdate))
			        	{
			        		xml_generate_flag=xml_generate_flag && false;
			        	}
			        }
			        if(xml_generate_flag)
			        {
		        		int count=0;
		        		String queryString2="SELECT COUNT(*) "
		        				+ "FROM FMS_OTH_INV_FILE_DTL "
		        				+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? AND SUPPLIER_CD=? "
		        				+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=?";
		        		stmt2 = conn.prepareStatement(queryString2);
		        		stmt2.setString(1, comp_cd);
		        		stmt2.setString(2, ""+VINVOICE_TYPE.elementAt(i));
		        		stmt2.setString(3, ""+VSUPPLIER_CD.elementAt(i));
		        		stmt2.setString(4, ""+VINVOICE_SEQ.elementAt(i));
		        		stmt2.setString(5, ""+VFINANCIAL_YEAR.elementAt(i));
		        		stmt2.setString(6, "S");
		        		rset2=stmt2.executeQuery();
		        		if(rset2.next())
		        		{
		        			count=rset2.getInt(1);
		        		}
		        		rset2.close();
		        		stmt2.close();
		        		
		        		if(count > 0)
		        		{
		        			String queryString3="UPDATE FMS_OTH_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
		        					+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? AND SUPPLIER_CD=? "
			        				+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=?";
		        			stmt3 = conn.prepareStatement(queryString3);
		        			stmt3.setString(1, xmlFileNm);
		        			stmt3.setString(2, emp_cd);
		        			stmt3.setString(3, comp_cd);
		        			stmt3.setString(4, ""+VINVOICE_TYPE.elementAt(i));
		        			stmt3.setString(5, ""+VSUPPLIER_CD.elementAt(i));
		        			stmt3.setString(6, ""+VINVOICE_SEQ.elementAt(i));
		        			stmt3.setString(7, ""+VFINANCIAL_YEAR.elementAt(i));
		        			stmt3.setString(8, "S");
		        			stmt3.executeUpdate();
		        			
		        			stmt3.close();
		        		}
		        		else
		        		{
		        			String queryString3="INSERT INTO FMS_OTH_INV_FILE_DTL(COMPANY_CD,SUPPLIER_CD,INVOICE_SEQ,INVOICE_TYPE,"
		        					+ "FINANCIAL_YEAR,PDF_TYPE,FILE_NAME,ENT_BY,ENT_DT) "
		        					+ "VALUES(?,?,?,?,"
		        					+ "?,?,?,?,SYSDATE)";
		        			stmt3 = conn.prepareStatement(queryString3);
		        			stmt3.setString(1, comp_cd);
		        			stmt3.setString(2, ""+VSUPPLIER_CD.elementAt(i));
		        			stmt3.setString(3, ""+VINVOICE_SEQ.elementAt(i));
		        			stmt3.setString(4, ""+VINVOICE_TYPE.elementAt(i));
		        			stmt3.setString(5, ""+VFINANCIAL_YEAR.elementAt(i));
		        			stmt3.setString(6, "S");
		        			stmt3.setString(7, xmlFileNm);
		        			stmt3.setString(8, emp_cd);
		        			stmt3.executeUpdate();
		        			stmt3.close();
		        		}
		        		conn.commit();
			        }
				}
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD","");
			transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet","");
			
		    Transformer transformer = transformerFactory.newTransformer();
		    DOMSource source = new DOMSource(doc);
		    
		    if(xml_generate_flag)
		    {
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
		    		String sub_folder=""+CommonVariable.sun_xml;
		    		
		    		File SubDir = new File(appPath+File.separator+main_folder+File.separator+sub_folder);
		    		if(!SubDir.exists()) 
		    		{
		    			SubDir.mkdir();
		    		}
		    		
		    		StreamResult result =  new StreamResult(new File(appPath+File.separator+main_folder+File.separator+sub_folder+""+File.separator+""+xmlFileNm));
		    		transformer.transform(source, result);
		    		
		    		msg = "Success! - SUN XML for other Invoice generated!";
					msg_type="S";
		    	}
		    	else
		    	{
		    		msg = "Failed! - SUN XML for other Invoice generation failed!";
					msg_type="E";
		    	}
		    }
		    else
		    {
		    	msg = "Failed! - SUN XML for other Invoice generation failed, Please retry again!";
				msg_type="E";
		    }
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//Parsing the Sun XML File
	public void parseSunXML()
	{
		String function_nm="parseSunXML()";
		try
		{
			if(!file_nm.equals(""))
			{
				
				String final_path=file_path+File.separator+file_nm;
				String account_cd="";
				
				File fXmlFile = new File(file_path+File.separator+file_nm);
				if(fXmlFile.exists())
				{
					DocumentBuilderFactory dbFactory = xmlUtil.dcoumentBuilderFactory();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(fXmlFile);
					    
					doc.getDocumentElement().normalize();
					NodeList nList = doc.getElementsByTagName("SSC");
					for (int temp = 0; temp < nList.getLength(); temp++) 
					{
						String journal_type="";
						String inv_no="";
						
						Node nNode = nList.item(temp);
						Element eElement = (Element) nNode;
						
						NodeList nodes = eElement.getChildNodes();
						for(int i=0; i<nodes.getLength(); i++)
						{
							Node node = nodes.item(i);
							String childTag = node.getNodeName();
							if(childTag.equalsIgnoreCase("Payload"))
							{
								Element ele = (Element) node;
								NodeList nodes1 = ele.getChildNodes();
								for(int j=0; j<nodes1.getLength(); j++)
								{
									Node node1 = nodes1.item(j);
									String childTag1 = node1.getNodeName();
									if(childTag1.equalsIgnoreCase("Ledger"))
									{
										Element ele1 = (Element) node1;
										NodeList nodes2 = ele1.getChildNodes();
										
										for(int n=0;n<nodes2.getLength();n++)
										{
											Node node2 = nodes2.item(n);
											String childTag2 = node2.getNodeName();
											
											if(childTag2.equalsIgnoreCase("Line"))
											{
												Element ele2 = (Element) node2;
												NodeList nodes3 = ele2.getChildNodes();
												
												for(int k=0;k<nodes3.getLength();k++)
												{
													Node node3 = nodes3.item(k);
													String childTag3 = node3.getNodeName();
													if(childTag3.equalsIgnoreCase("AccountCode"))
													{
														VACCOUNT_CD.add(nodes3.item(k).getTextContent());
														if(!inv_type.equals("AHPL") && n==1)
														{
															account_cd = nodes3.item(k).getTextContent();
														}
														else if(inv_type.equals("AHPL") && n==2)
														{
															account_cd = nodes3.item(k).getTextContent();
														}
														VACCOUNT_TYPE_NM.add(getAccDes(inv_type,n,account_cd));
													}
													else if(childTag3.equalsIgnoreCase("AccountingPeriod"))
													{
														VPERIOD_START_DT.add(nodes3.item(k).getTextContent());
													}
													else if(childTag3.equalsIgnoreCase("TransactionDate"))
													{
														VINVOICE_DT.add(nodes3.item(k).getTextContent());
														VAPPROVAL_DT.add(nodes3.item(k).getTextContent());
													}
													else if(childTag3.equalsIgnoreCase("JournalType"))
													{
														VJOURNAL_TYPE.add(nodes3.item(k).getTextContent());
													}
													else if(childTag3.equalsIgnoreCase("TransactionReference"))
													{
														VINVOICE_NO.add(nodes3.item(k).getTextContent());
													}
													else if(childTag3.equalsIgnoreCase("Description"))
													{
														VDESC.add(nodes3.item(k).getTextContent());
													}
													else if(childTag3.equalsIgnoreCase("BaseAmount"))
													{
														VBASE_AMT.add(nodes3.item(k).getTextContent());
													}
													else if(childTag3.equalsIgnoreCase("AnalysisCode4"))
													{
														VCOA_CD.add(nodes3.item(k).getTextContent());
													}
													else if(childTag3.equalsIgnoreCase("DebitCredit"))
													{
														VDEBIT_CREDIT.add(nodes3.item(k).getTextContent());
													}
													else if(childTag3.equalsIgnoreCase("CurrencyCode"))
													{
														VCURRENCY_CD.add(nodes3.item(k).getTextContent());
													}
													else if(childTag3.equalsIgnoreCase("AnalysisCode3"))
													{
														VEMPLOYEE_CD.add(nodes3.item(k).getTextContent());
													}
													else if(childTag3.equalsIgnoreCase("AnalysisCode2"))
													{
														VCOST_CTR_CD.add(nodes3.item(k).getTextContent());
													}
													else if(childTag3.equalsIgnoreCase("AnalysisCode10"))
													{
														VBU_UNIT_CD.add(nodes3.item(k).getTextContent());
													}
													else if(childTag3.equalsIgnoreCase("AnalysisCode7"))
													{
														VCODE.add(nodes3.item(k).getTextContent());
													}
													else if(childTag3.equalsIgnoreCase("CurrencyRate"))
													{
														VEXCHNG_RATE.add(nodes3.item(k).getTextContent());
													}
													else if(childTag3.equalsIgnoreCase("DueDate"))
													{
														VINVOICE_DUE_DT.add(nodes3.item(k).getTextContent());
													}
												}
											}
										}
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
	
	public String getotherentitySunAccountCode(String vendor_cd, String entity_type)
	{
		String function_nm="getotherentitySunAccountCode()";
		String account_cd="";
		try
		{
			String queryString="SELECT ACCOUNT_CODE "
					+ "FROM FMS_OTH_ENTITY_MST "
					+ "WHERE ENTITY_CD=? AND ENTITY_TYPE=? ";
			stmt6= conn.prepareStatement(queryString);
			stmt6.setString(1, vendor_cd);
			stmt6.setString(2, entity_type);
			rset6=stmt6.executeQuery();
			if(rset6.next())
			{
				account_cd=rset6.getString(1)==null?"":rset6.getString(1);
			}
			rset6.close();
			stmt6.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return account_cd;
	}
	
	public void getXmlFilesList()
	{
		String function_nm="getXmlFilesList()";
		try
		{
			String queryString="SELECT DISTINCT FILE_NAME,NULL,NULL FROM FMS_OTH_INV_FILE_DTL "
					+ "WHERE COMPANY_CD=? AND PDF_TYPE=? "
					+ "AND TO_DATE(TO_CHAR(ENT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')>=TO_DATE(?,'DD/MM/YYYY') "
					+ "AND TO_DATE(TO_CHAR(ENT_DT,'DD/MM/YYYY'),'DD/MM/YYYY')<=TO_DATE(?,'DD/MM/YYYY') ";
			stmt3=conn.prepareStatement(queryString);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, "S");
			stmt3.setString(3, xml_gen_from_dt);
			stmt3.setString(4, xml_gen_to_dt);
			rset3=stmt3.executeQuery();
			while(rset3.next())
			{
				String file_nm=rset3.getString(1)==null?"":rset3.getString(1);
				String inv_flag=rset3.getString(2)==null?"":rset3.getString(2);
				String inv_head=rset3.getString(3)==null?"":rset3.getString(3);
				VJOURNAL_TYPE_NM.add("IG Finance Invoice");
				VSUN_FILE_NM.add(file_nm);
			}
			rset3.close();
			stmt3.close();
		
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
//	public String getSunApprovalDt(String inv_no)
//	{
//		String function_nm="getSunApprovalDt()";
//		String sun_approval_dt="";
//		try
//		{
//			String query="SELECT TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY')  "
//					+ "FROM FMS_INVOICE_MST "
//					+ "WHERE COMPANY_CD=? AND INVOICE_NO=? AND FIN_SYS=? ";
//			query+= "UNION ";
//			query+= "SELECT TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY')  "
//					+ "FROM FMS_FFLOW_INV_MST "
//					+ "WHERE COMPANY_CD=? AND INVOICE_NO=? AND FIN_SYS=? ";
//			query+= "UNION ";
//			query+="SELECT TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY')  "
//					+ "FROM FMS_PUR_SG_INV_MST "
//					+ "WHERE COMPANY_CD=? AND INVOICE_NO=? AND FIN_SYS=? ";
//			query+= "UNION ";
//			query+= "SELECT TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY')  "
//					+ "FROM FMS_PUR_PG_INV_MST "
//					+ "WHERE COMPANY_CD=? AND INVOICE_NO=? AND FIN_SYS=?";
//			query+= "UNION ";
//			query+= "SELECT TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY')  "
//					+ "FROM FMS_PUR_FFLOW_INV_MST "
//					+ "WHERE COMPANY_CD=? AND INVOICE_NO=? AND FIN_SYS=?";
//			query+= "UNION ";
//			query+="SELECT TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY')  "
//					+ "FROM FMS_DLNG_INVOICE_MST "
//					+ "WHERE COMPANY_CD=? AND INVOICE_NO=? AND FIN_SYS=? ";
//			query+= "UNION ";
//			query+="SELECT TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY')  "
//					+ "FROM FMS_GTA_SG_INV_MST "
//					+ "WHERE COMPANY_CD=? AND INVOICE_NO=? AND FIN_SYS=? ";
//			query+= "UNION ";
//			query+="SELECT TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY')  "
//					+ "FROM FMS_GTA_PG_INV_MST "
//					+ "WHERE COMPANY_CD=? AND INVOICE_NO=? AND FIN_SYS=? ";
//			query+= "UNION ";
//			query+="SELECT TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY')  "
//					+ "FROM FMS_GTA_FFLOW_INV_MST "
//					+ "WHERE COMPANY_CD=? AND INVOICE_REF=? AND FIN_SYS=? ";
//			query+= "UNION ";
//			query+="SELECT TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY') "
//					+ "FROM FMS_DERV_INVOICE_MST "
//					+ "WHERE COMPANY_CD=? AND INVOICE_NO=? AND FIN_SYS=? ";
//			query+= "UNION ";
//			query+="SELECT TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY') "
//					+ "FROM FMS_DERV_INVOICE_MST "
//					+ "WHERE COMPANY_CD=? AND INVOICE_REF_NO=? AND FIN_SYS=? ";
//			query+= "UNION ";
//			query+="SELECT TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY') "
//					+ "FROM FMS_DLNG_FFLOW_INV_MST "
//					+ "WHERE COMPANY_CD=? AND INVOICE_NO=? AND FIN_SYS=? ";
//			query+= "UNION ";
//			query+="SELECT TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY') "
//					+ "FROM FMS_DLNG_SVC_INVOICE_MST "
//					+ "WHERE COMPANY_CD=? AND INVOICE_NO=? AND FIN_SYS=? ";
//			stmt6=conn.prepareStatement(query);
//			stmt6.setString(1, comp_cd);
//			stmt6.setString(2, inv_no);
//			stmt6.setString(3, "S");
//			stmt6.setString(4, comp_cd);
//			stmt6.setString(5, inv_no);
//			stmt6.setString(6, "S");
//			stmt6.setString(7, comp_cd);
//			stmt6.setString(8, inv_no);
//			stmt6.setString(9, "S");
//			stmt6.setString(10, comp_cd);
//			stmt6.setString(11, inv_no);
//			stmt6.setString(12, "S");
//			stmt6.setString(13, comp_cd);
//			stmt6.setString(14, inv_no);
//			stmt6.setString(15, "S");
//			stmt6.setString(16, comp_cd);
//			stmt6.setString(17, inv_no);
//			stmt6.setString(18, "S");
//			stmt6.setString(19, comp_cd);
//			stmt6.setString(20, inv_no);
//			stmt6.setString(21, "S");
//			stmt6.setString(22, comp_cd);
//			stmt6.setString(23, inv_no);
//			stmt6.setString(24, "S");
//			stmt6.setString(25, comp_cd);
//			stmt6.setString(26, inv_no);
//			stmt6.setString(27, "S");
//			stmt6.setString(28, comp_cd);
//			stmt6.setString(29, inv_no);
//			stmt6.setString(30, "S");
//			stmt6.setString(31, comp_cd);
//			stmt6.setString(32, inv_no);
//			stmt6.setString(33, "S");
//			stmt6.setString(34, comp_cd);
//			stmt6.setString(35, inv_no);
//			stmt6.setString(36, "S");
//			stmt6.setString(37, comp_cd);
//			stmt6.setString(38, inv_no);
//			stmt6.setString(39, "S");
//			rset6=stmt6.executeQuery();
//			if(rset6.next())
//			{
//				sun_approval_dt=rset6.getString(1)==null?"":rset6.getString(1);
//			}
//			rset6.close();
//			stmt6.close();
//		}
//		catch(Exception e)
//		{
//			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
//		}
//		return sun_approval_dt;
//	}
	
	
	public String getAccountingPeriod(String invoice_dt)
	{
		String function_nm="getAccountingPeriod()";
		String acc_period="";
		try
		{
			String []invDt = invoice_dt.split("/");
			String trans_dt = invDt[0]+invDt[1]+invDt[2];
			if(invDt[1].equals("01"))
			{
				acc_period="001"+invDt[2];
			}
			else if(invDt[1].equals("02"))
			{
				acc_period="002"+invDt[2];
			}
			else if(invDt[1].equals("03"))
			{
				acc_period="003"+invDt[2];
			}
			else if(invDt[1].equals("04"))
			{
				acc_period="005"+invDt[2];
			}
			else if(invDt[1].equals("05"))
			{
				acc_period="006"+invDt[2];
			}
			else if(invDt[1].equals("06"))
			{
				acc_period="007"+invDt[2];
			}
			else if(invDt[1].equals("07"))
			{
				acc_period="009"+invDt[2];
			}
			else if (invDt[1].equals("08"))
			{
				acc_period="010"+invDt[2];
			}
			else if(invDt[1].equals("09"))
			{
				acc_period="011"+invDt[2];
			}
			else if(invDt[1].equals("10"))
			{
				acc_period="013"+invDt[2];
			}
			else if(invDt[1].equals("11"))
			{
				acc_period="014"+invDt[2];
			}
			else if(invDt[1].equals("12"))
			{
				acc_period="015"+invDt[2];
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return acc_period;
	}
	
	
	public void getInvoiceData()
	{
		String function_nm="getInvoiceData()";
		try
		{
			String vendor_cd="";
		    String fin_year="";
		    String supplier_cd="";
		    String invoice_seq="";
		    String invoice_no="";
		    String invoice_dt="";
		    double gross_amt=0;
		    double tax_amt=0;
		    String inv_due_dt="";
		    String accountPeriod="";
		    String inv_raised_in="";
		    String bucac="SEI";	
		    String invoice_type="";
		    String inv_flag="";
		    double net_payable_amt=0;
		    String criteria="";
		    String company_cd="";
		    String account_cd="";
		    String jorunal_type="FMSSL";
		    
		    int ctn=0;
		    String queryString="";
		    queryString="SELECT A.COMPANY_CD,A.VENDOR_CD,A.SUPPLIER_CD,A.INVOICE_TYPE,A.FINANCIAL_YEAR,A.INVOICE_SEQ,A.INVOICE_NO,"
		    		+ "TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),A.INVOICE_RAISED_IN,A.GROSS_AMT,A.TAX_AMT_INR,"
					+ "A.NET_PAYABLE,A.INVOICE_CATEGORY,A.VENDOR_TYPE,A.INV_FLAG,A.REF_NO,A.CRITERIA " 
					+ "FROM FMS_OTH_INVOICE_MST A "
					+ "WHERE A.COMPANY_CD=? AND INVOICE_NO = ?";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(++ctn, comp_cd);
			stmt.setString(++ctn, Inv_no);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				company_cd=rset.getString(1)==null?"":rset.getString(1);
				vendor_cd=rset.getString(2)==null?"":rset.getString(2);
				supplier_cd=rset.getString(3)==null?"":rset.getString(3);
				invoice_type=rset.getString(4)==null?"":rset.getString(4);
				fin_year=rset.getString(5)==null?"":rset.getString(5);
				invoice_seq=rset.getString(6)==null?"":rset.getString(6);
				invoice_no=rset.getString(7)==null?"":rset.getString(7);
				invoice_dt=rset.getString(8)==null?"":rset.getString(8);
				inv_due_dt=rset.getString(9)==null?"":rset.getString(9);
				gross_amt=rset.getDouble(11);
				tax_amt=rset.getDouble(12);
				net_payable_amt=rset.getDouble(13);
				inv_flag=rset.getString(16)==null?"":rset.getString(16);
				accountPeriod=getAccountingPeriod(invoice_dt);
				String invDt = invoice_dt.replace("/", "");
				String dueDt=inv_due_dt.replace("/", "");
				String tax="";
				
				if(inv_flag.equals("CR"))
				{
					if(Double.doubleToRawLongBits(gross_amt)<Double.doubleToRawLongBits(0))
					{
						gross_amt=(-1)*gross_amt;
					}
					if(Double.doubleToRawLongBits(tax_amt)<Double.doubleToRawLongBits(0))
					{
						tax_amt=(-1)*tax_amt;
					}
					if(Double.doubleToRawLongBits(net_payable_amt)<Double.doubleToRawLongBits(0))
					{
						net_payable_amt=(-1)*net_payable_amt;
					}
				}
				
				String tax_cd = "",desc="";
				String query1 = "SELECT TAX_CD,ITEM_DESCRIPTION "
						+ "FROM FMS_OTH_INVOICE_DTL  "
						+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? "
						+ "AND INVOICE_NO=? AND SUPPLIER_CD=?";
				stmt1 = conn.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, invoice_type);
				stmt1.setString(3, fin_year);
				stmt1.setString(4, invoice_seq);
				stmt1.setString(5, invoice_no);
				stmt1.setString(6, supplier_cd);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					tax_cd = rset1.getString(1)==null?"":rset1.getString(1);
					desc = rset1.getString(2)==null?"":rset1.getString(2);
				}
				rset1.close();
				stmt1.close();
				
				VINVOICE_SEQ.add(invoice_seq);
				VSUPPLIER_CD.add(supplier_cd);
				VINVOICE_TYPE.add(invoice_type);
				VFINANCIAL_YEAR.add(fin_year);
				VINV_FLAG.add(inv_flag);
				
				if(invoice_type.equals("COSTR"))
				{
					account_cd=getotherentitySunAccountCode(vendor_cd,"V");
					
					if(inv_flag.equals("CR") || inv_flag.equals("DR"))
					{
						for(int i=0;i<2;i++)
						{
							VPERIOD_START_DT.add(accountPeriod);
							VAPPROVAL_DT.add(invDt);
							VINVOICE_NO.add(invoice_no);
							VDESC.add(desc);
							VJOURNAL_TYPE.add(jorunal_type);
							
							if(i==0)
							{
								if(tax_cd.contains("I"))
								{
									VACCOUNT_CD.add("245079");
								}
								else 
								{
									VACCOUNT_CD.add("245080");
								}
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(nf.format(net_payable_amt));
								VDEBIT_CREDIT.add("D");
							}
							else if(i==1)
							{
								VACCOUNT_CD.add(account_cd);
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(nf.format(net_payable_amt));
								VDEBIT_CREDIT.add("C");
							}
							VCOA_CD.add("");
							VCURRENCY_CD.add("INR");
							if(supplier_cd.equals("1"))
							{
								VEMPLOYEE_CD.add("PNA");
								VCOST_CTR_CD.add("1PO01");
								VBU_UNIT_CD.add("PRT");
							}
							else
							{
								VEMPLOYEE_CD.add("LNA");
								VCOST_CTR_CD.add("1TM01");
								VBU_UNIT_CD.add("LNG");
							}
							VCODE.add("NA");
							VINVOICE_DUE_DT.add(dueDt);
							VINVOICE_DT.add(invDt);
						}
					}
					else 
					{
						for(int i=0;i<2;i++)
						{
							VPERIOD_START_DT.add(accountPeriod);
							VAPPROVAL_DT.add(invDt);
							VINVOICE_NO.add(invoice_no);
							VDESC.add(desc);
							VJOURNAL_TYPE.add(jorunal_type);
							
							if(i==0)
							{
								if(tax_cd.contains("I"))
								{
									VACCOUNT_CD.add("245079");
								}
								else 
								{
									VACCOUNT_CD.add("245080");
								}
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(nf.format(net_payable_amt));
								VDEBIT_CREDIT.add("C");
							}
							else if(i==1)
							{
								VACCOUNT_CD.add(account_cd);
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(nf.format(net_payable_amt));
								VDEBIT_CREDIT.add("D");
							}
							VCOA_CD.add("");
							VCURRENCY_CD.add("INR");
							if(supplier_cd.equals("1"))
							{
								VEMPLOYEE_CD.add("PNA");
								VCOST_CTR_CD.add("1PO01");
								VBU_UNIT_CD.add("PRT");
							}
							else
							{
								VEMPLOYEE_CD.add("LNA");
								VCOST_CTR_CD.add("1TM01");
								VBU_UNIT_CD.add("LNG");
							}
							VCODE.add("NA");
							VINVOICE_DUE_DT.add(dueDt);
							VINVOICE_DT.add(invDt);
						}
					}
				}
				else if(invoice_type.equals("COSTRH"))
				{
					tax = nf.format(tax_amt/2);
					account_cd = "161001";
					if(inv_flag.equals("CR") || inv_flag.equals("DR"))
					{
						for(int i=0;i<4;i++)
						{
							VPERIOD_START_DT.add(accountPeriod);
							VAPPROVAL_DT.add(invDt);
							VINVOICE_NO.add(invoice_no);
							VDESC.add(desc);
							VJOURNAL_TYPE.add(jorunal_type);
							
							if(i==0)
							{
								VACCOUNT_CD.add(account_cd);
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(nf.format(net_payable_amt));
								VDEBIT_CREDIT.add("C");
							}
							else if(i==1)
							{
								VACCOUNT_CD.add("825009");
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,"825009"));
								VBASE_AMT.add(nf.format(gross_amt));
								VDEBIT_CREDIT.add("D");
							}
							else if(i==2)
							{
								VACCOUNT_CD.add("245077");
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("D");
							}
							else if(i==3)
							{
								VACCOUNT_CD.add("245078");
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("D");
							}
							VCOA_CD.add("825009");
							VCURRENCY_CD.add("INR");
							VEMPLOYEE_CD.add("PNA");
							VCOST_CTR_CD.add("1FN01");
							VBU_UNIT_CD.add("LNG");
							VCODE.add("NA");
							VINVOICE_DUE_DT.add(dueDt);
							VINVOICE_DT.add(invDt);
						}
					}
					else
					{
						for(int i=0;i<4;i++)
						{
							VPERIOD_START_DT.add(accountPeriod);
							VAPPROVAL_DT.add(invDt);
							VINVOICE_NO.add(invoice_no);
							VDESC.add(desc);
							VJOURNAL_TYPE.add(jorunal_type);
							
							if(i==0)
							{
								VACCOUNT_CD.add(account_cd);
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(nf.format(net_payable_amt));
								VDEBIT_CREDIT.add("D");
							}
							else if(i==1)
							{
								VACCOUNT_CD.add("825009");
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,"825009"));
								VBASE_AMT.add(nf.format(gross_amt));
								VDEBIT_CREDIT.add("C");
							}
							else if(i==2)
							{
								VACCOUNT_CD.add("245077");
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("C");
							}
							else if(i==3)
							{
								VACCOUNT_CD.add("245078");
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("C");
							}
							VCOA_CD.add("825009");
							VCURRENCY_CD.add("INR");
							VEMPLOYEE_CD.add("PNA");
							VCOST_CTR_CD.add("1FN01");
							VBU_UNIT_CD.add("LNG");
							VCODE.add("NA");
							VINVOICE_DUE_DT.add(dueDt);
							VINVOICE_DT.add(invDt);
						}
					}
				}
				else if(invoice_type.equals("HSA"))
				{
					desc = "BERTHING CHARGES";
					tax = nf.format(tax_amt/2);
					account_cd=getotherentitySunAccountCode(vendor_cd,"V");
					if(inv_flag.equals("CR") || inv_flag.equals("DR"))
					{
						for(int i=0;i<4;i++)
						{
							VPERIOD_START_DT.add(accountPeriod);
							VAPPROVAL_DT.add(invDt);
							VINVOICE_NO.add(invoice_no);
							VDESC.add(desc);
							VJOURNAL_TYPE.add(jorunal_type);
							
							if(i==0)
							{
								VACCOUNT_CD.add(account_cd);
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(nf.format(net_payable_amt));
								VDEBIT_CREDIT.add("C");
							}
							else if(i==1)
							{
								VACCOUNT_CD.add("711003");
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,"711003"));
								VBASE_AMT.add(nf.format(gross_amt));
								VDEBIT_CREDIT.add("D");
							}
							else if(i==2)
							{
								VACCOUNT_CD.add("245077");
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("D");
							}
							else if(i==3)
							{
								VACCOUNT_CD.add("245078");
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("D");
							}
							VCOA_CD.add("711003");
							VCURRENCY_CD.add("USD");
							VEMPLOYEE_CD.add("PNA");
							VCOST_CTR_CD.add("1PO40");
							VBU_UNIT_CD.add("PRT");
							VCODE.add("NA");
							VINVOICE_DUE_DT.add(dueDt);
							VINVOICE_DT.add(invDt);
						}
					}
					else 
					{
						for(int i=0;i<4;i++)
						{
							VPERIOD_START_DT.add(accountPeriod);
							VAPPROVAL_DT.add(invDt);
							VINVOICE_NO.add(invoice_no);
							VDESC.add(desc);
							VJOURNAL_TYPE.add(jorunal_type);
							
							if(i==0)
							{
								VACCOUNT_CD.add(account_cd);
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(nf.format(net_payable_amt));
								VDEBIT_CREDIT.add("D");
							}
							else if(i==1)
							{
								VACCOUNT_CD.add("711003");
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,"711003"));
								VBASE_AMT.add(nf.format(gross_amt));
								VDEBIT_CREDIT.add("C");
							}
							else if(i==2)
							{
								VACCOUNT_CD.add("245077");
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("C");
							}
							else if(i==3)
							{
								VACCOUNT_CD.add("245078");
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("C");
							}
							VCOA_CD.add("711003");
							VCURRENCY_CD.add("USD");
							VEMPLOYEE_CD.add("PNA");
							VCOST_CTR_CD.add("1PO40");
							VBU_UNIT_CD.add("PRT");
							VCODE.add("NA");
							VINVOICE_DUE_DT.add(dueDt);
							VINVOICE_DT.add(invDt);
						}
					}
					
				}
				else if(invoice_type.equals("HS"))
				{
					account_cd = "161002";
					tax = nf.format(tax_amt/2);
					
					if(inv_flag.equals("CR") || inv_flag.equals("DR"))
					{
						for(int i=0;i<4;i++)
						{
							VPERIOD_START_DT.add(accountPeriod);
							VAPPROVAL_DT.add(invDt);
							VINVOICE_NO.add(invoice_no);
							VDESC.add(desc);
							VJOURNAL_TYPE.add(jorunal_type);
							
							if(i==0)
							{
								VACCOUNT_CD.add(account_cd);
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(nf.format(net_payable_amt));
								VDEBIT_CREDIT.add("C");
							}
							else if(i==1)
							{
								VACCOUNT_CD.add("711004");
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,"711004"));
								VBASE_AMT.add(nf.format(gross_amt));
								VDEBIT_CREDIT.add("D");
							}
							else if(i==2)
							{
								VACCOUNT_CD.add("245077");
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("D");
							}
							else if(i==3)
							{
								VACCOUNT_CD.add("245078");
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("D");
							}
							VCOA_CD.add("711004");
							VCURRENCY_CD.add("INR");
							VEMPLOYEE_CD.add("PNA");
							VCOST_CTR_CD.add("1PO40");
							VBU_UNIT_CD.add("PRT");
							VCODE.add("NA");
							VINVOICE_DUE_DT.add(dueDt);
							VINVOICE_DT.add(invDt);
						}
					}
					else 
					{
						for(int i=0;i<4;i++)
						{
							VPERIOD_START_DT.add(accountPeriod);
							VAPPROVAL_DT.add(invDt);
							VINVOICE_NO.add(invoice_no);
							VDESC.add(desc);
							VJOURNAL_TYPE.add(jorunal_type);
							
							if(i==0)
							{
								VACCOUNT_CD.add(account_cd);
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(nf.format(net_payable_amt));
								VDEBIT_CREDIT.add("D");
							}
							else if(i==1)
							{
								VACCOUNT_CD.add("711004");
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,"711004"));
								VBASE_AMT.add(nf.format(gross_amt));
								VDEBIT_CREDIT.add("C");
							}
							else if(i==2)
							{
								VACCOUNT_CD.add("245077");
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("C");
							}
							else if(i==3)
							{
								VACCOUNT_CD.add("245078");
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("C");
							}
							VCOA_CD.add("711004");
							VCURRENCY_CD.add("INR");
							VEMPLOYEE_CD.add("PNA");
							VCOST_CTR_CD.add("1PO40");
							VBU_UNIT_CD.add("PRT");
							VCODE.add("NA");
							VINVOICE_DUE_DT.add(dueDt);
							VINVOICE_DT.add(invDt);
						}
					}
				}
				else if(invoice_type.equals("AHPL"))
				{
					account_cd = getotherentitySunAccountCode(vendor_cd,"V");
					tax = nf.format(tax_amt/2);
					
					if(inv_flag.equals("CR") || inv_flag.equals("DR"))
					{
						for(int i=0;i<5;i++)
						{
							VPERIOD_START_DT.add(accountPeriod);
							VAPPROVAL_DT.add(invDt);
							VINVOICE_NO.add(invoice_no);
							VDESC.add(desc);
							VJOURNAL_TYPE.add(jorunal_type);
							
							if(i==0)
							{
								VACCOUNT_CD.add(account_cd);
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(nf.format(gross_amt));
								VDEBIT_CREDIT.add("C");
							}
							else if(i==1)
							{
								VACCOUNT_CD.add(account_cd);
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(nf.format(tax_amt));
								VDEBIT_CREDIT.add("C");
							}
							else if(i==2)
							{
								VACCOUNT_CD.add("711006");
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,"711006"));
								VBASE_AMT.add(nf.format(gross_amt));
								VDEBIT_CREDIT.add("D");
							}
							else if(i==3)
							{
								VACCOUNT_CD.add("245077");
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("D");
							}
							else if(i==4)
							{
								VACCOUNT_CD.add("245078");
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("D");
							}
							VCOA_CD.add("711006");
							VCURRENCY_CD.add("INR");
							VEMPLOYEE_CD.add("PNA");
							VCOST_CTR_CD.add("1PO40");
							VBU_UNIT_CD.add("PRT");
							VCODE.add("NA");
							VINVOICE_DUE_DT.add(dueDt);
							VINVOICE_DT.add(invDt);
						}
					}
					else 
					{
						for(int i=0;i<5;i++)
						{
							VPERIOD_START_DT.add(accountPeriod);
							VAPPROVAL_DT.add(invDt);
							VINVOICE_NO.add(invoice_no);
							VDESC.add(desc);
							VJOURNAL_TYPE.add(jorunal_type);
							
							if(i==0)
							{
								VACCOUNT_CD.add(account_cd);
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(nf.format(gross_amt));
								VDEBIT_CREDIT.add("D");
							}
							else if(i==1)
							{
								VACCOUNT_CD.add(account_cd);
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(nf.format(tax_amt));
								VDEBIT_CREDIT.add("D");
							}
							else if(i==2)
							{
								VACCOUNT_CD.add("711006");
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,"711006"));
								VBASE_AMT.add(nf.format(gross_amt));
								VDEBIT_CREDIT.add("C");
							}
							else if(i==3)
							{
								VACCOUNT_CD.add("245077");
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("C");
							}
							else if(i==4)
							{
								VACCOUNT_CD.add("245078");
								VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
								VBASE_AMT.add(tax);
								VDEBIT_CREDIT.add("C");
							}
							VCOA_CD.add("711006");
							VCURRENCY_CD.add("INR");
							VEMPLOYEE_CD.add("PNA");
							VCOST_CTR_CD.add("1PO40");
							VBU_UNIT_CD.add("PRT");
							VCODE.add("NA");
							VINVOICE_DUE_DT.add(dueDt);
							VINVOICE_DT.add(invDt);
						}
					}
				}
				else if(invoice_type.equals("SFA"))
				{
					account_cd = "243996";
					tax = nf.format(Math.round(tax_amt)/2);
					
					if(inv_flag.equals("CR") || inv_flag.equals("DR"))
					{
						if(!tax_cd.equals("") && tax_cd.equals("I")) //for IGST
						{
							for(int i=0;i<3;i++)
							{
								VPERIOD_START_DT.add(accountPeriod);
								VAPPROVAL_DT.add(invDt);
								VINVOICE_NO.add(invoice_no);
								VDESC.add(desc);
								VJOURNAL_TYPE.add(jorunal_type);
								
								if(i==0)
								{
									VACCOUNT_CD.add(account_cd);
									VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
									VBASE_AMT.add(nf.format(net_payable_amt));
									VDEBIT_CREDIT.add("C");
								}
								else if(i==1)
								{
									VACCOUNT_CD.add("799999");
									VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,"799999"));
									VBASE_AMT.add(nf.format(gross_amt));
									VDEBIT_CREDIT.add("D");
								}
								else if(i==2)
								{
									VACCOUNT_CD.add("245076");
									VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
									VBASE_AMT.add(tax_amt);
									VDEBIT_CREDIT.add("D");
								}
								
								VCOA_CD.add("799999");
								VCURRENCY_CD.add("INR");
								if(supplier_cd.equals("1"))
								{
									VEMPLOYEE_CD.add("PNA");
									VCOST_CTR_CD.add("1PO01");
									VBU_UNIT_CD.add("PRT");
								}
								else
								{
									VEMPLOYEE_CD.add("LNA");
									VCOST_CTR_CD.add("1TM01");
									VBU_UNIT_CD.add("LNG");
								}
								VCODE.add("NA");
								VINVOICE_DUE_DT.add(dueDt);
								VINVOICE_DT.add(invDt);
							}
						}
						else if(!tax_cd.equals("") && tax_cd.equals("C")) //for CGST,SGST
						{
							for(int i=0;i<4;i++)
							{
								VPERIOD_START_DT.add(accountPeriod);
								VAPPROVAL_DT.add(invDt);
								VINVOICE_NO.add(invoice_no);
								VDESC.add(desc);
								VJOURNAL_TYPE.add(jorunal_type);
								
								if(i==0)
								{
									VACCOUNT_CD.add("243996");
									VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
									VBASE_AMT.add(nf.format(net_payable_amt));
									VDEBIT_CREDIT.add("C");
								}
								else if(i==1)
								{
									VACCOUNT_CD.add("799999");
									VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,"799999"));
									VBASE_AMT.add(nf.format(gross_amt));
									VDEBIT_CREDIT.add("D");
								}
								else if(i==2)
								{
									VACCOUNT_CD.add("245077");
									VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
									VBASE_AMT.add(tax);
									VDEBIT_CREDIT.add("D");
								}
								else if(i==3)
								{
									VACCOUNT_CD.add("245078");
									VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
									VBASE_AMT.add(tax);
									VDEBIT_CREDIT.add("D");
								}
								
								VCOA_CD.add("799999");
								VCURRENCY_CD.add("INR");
								if(supplier_cd.equals("1"))
								{
									VEMPLOYEE_CD.add("PNA");
									VCOST_CTR_CD.add("1PO01");
									VBU_UNIT_CD.add("PRT");
								}
								else
								{
									VEMPLOYEE_CD.add("LNA");
									VCOST_CTR_CD.add("1TM01");
									VBU_UNIT_CD.add("LNG");
								}
								VCODE.add("NA");
								VINVOICE_DUE_DT.add(dueDt);
								VINVOICE_DT.add(invDt);
							}
						}
					}
					else 
					{
						if(!tax_cd.equals("") && tax_cd.equals("I")) //for IGST
						{
							for(int i=0;i<3;i++)
							{
								VPERIOD_START_DT.add(accountPeriod);
								VAPPROVAL_DT.add(invDt);
								VINVOICE_NO.add(invoice_no);
								VDESC.add(desc);
								VJOURNAL_TYPE.add(jorunal_type);
								
								if(i==0)
								{
									VACCOUNT_CD.add(account_cd);
									VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
									VBASE_AMT.add(nf.format(net_payable_amt));
									VDEBIT_CREDIT.add("D");
								}
								else if(i==1)
								{
									VACCOUNT_CD.add("799999");
									VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,"799999"));
									VBASE_AMT.add(nf.format(gross_amt));
									VDEBIT_CREDIT.add("C");
								}
								else if(i==2)
								{
									VACCOUNT_CD.add("245076");
									VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
									VBASE_AMT.add(tax_amt);
									VDEBIT_CREDIT.add("C");
								}
								
								VCOA_CD.add("799999");
								VCURRENCY_CD.add("INR");
								if(supplier_cd.equals("1"))
								{
									VEMPLOYEE_CD.add("PNA");
									VCOST_CTR_CD.add("1PO01");
									VBU_UNIT_CD.add("PRT");
								}
								else
								{
									VEMPLOYEE_CD.add("LNA");
									VCOST_CTR_CD.add("1TM01");
									VBU_UNIT_CD.add("LNG");
								}
								VCODE.add("NA");
								VINVOICE_DUE_DT.add(dueDt);
								VINVOICE_DT.add(invDt);
							}
						}
						else if(!tax_cd.equals("") && tax_cd.equals("C")) //for CGST,SGST
						{
							for(int i=0;i<4;i++)
							{
								VPERIOD_START_DT.add(accountPeriod);
								VAPPROVAL_DT.add(invDt);
								VINVOICE_NO.add(invoice_no);
								VDESC.add(desc);
								VJOURNAL_TYPE.add(jorunal_type);
								
								if(i==0)
								{
									VACCOUNT_CD.add("243996");
									VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
									VBASE_AMT.add(nf.format(net_payable_amt));
									VDEBIT_CREDIT.add("D");
								}
								else if(i==1)
								{
									VACCOUNT_CD.add("799999");
									VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,"799999"));
									VBASE_AMT.add(nf.format(gross_amt));
									VDEBIT_CREDIT.add("C");
								}
								else if(i==2)
								{
									VACCOUNT_CD.add("245077");
									VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
									VBASE_AMT.add(tax);
									VDEBIT_CREDIT.add("C");
								}
								else if(i==3)
								{
									VACCOUNT_CD.add("245078");
									VACCOUNT_TYPE_NM.add(getAccDes(invoice_type,i,account_cd));
									VBASE_AMT.add(tax);
									VDEBIT_CREDIT.add("C");
								}
								
								VCOA_CD.add("799999");
								VCURRENCY_CD.add("INR");
								if(supplier_cd.equals("1"))
								{
									VEMPLOYEE_CD.add("PNA");
									VCOST_CTR_CD.add("1PO01");
									VBU_UNIT_CD.add("PRT");
								}
								else
								{
									VEMPLOYEE_CD.add("LNA");
									VCOST_CTR_CD.add("1TM01");
									VBU_UNIT_CD.add("LNG");
								}
								VCODE.add("NA");
								VINVOICE_DUE_DT.add(dueDt);
								VINVOICE_DT.add(invDt);
							}
						}
					}
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
	
	public void getFilenm()
	{
		String function_nm="getFilenm()";
		try
		{
			
			String inv_seq="",fin_yr="",supp_cd="",inv_type="";
			String queryString="SELECT INVOICE_SEQ,FINANCIAL_YEAR,SUPPLIER_CD,INVOICE_TYPE FROM FMS_OTH_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND INVOICE_NO=? ";
			stmt3=conn.prepareStatement(queryString);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, Inv_no);
			rset3=stmt3.executeQuery();
			if(rset3.next())
			{
				inv_seq=rset3.getString(1)==null?"":rset3.getString(1);
				fin_yr=rset3.getString(2)==null?"":rset3.getString(2);
				supp_cd=rset3.getString(3)==null?"":rset3.getString(3);
				inv_type=rset3.getString(4)==null?"":rset3.getString(4);
			}
			rset3.close();
			stmt3.close();
			
			queryString="SELECT FILE_NAME FROM FMS_OTH_INV_FILE_DTL "
					+ "WHERE COMPANY_CD=? AND PDF_TYPE=? AND SUPPLIER_CD=? AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
					+ "AND INVOICE_SEQ=?";
			stmt3=conn.prepareStatement(queryString);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, "S");
			stmt3.setString(3, supp_cd);
			stmt3.setString(4, inv_type);
			stmt3.setString(5, fin_yr);
			stmt3.setString(6, inv_seq);
			rset3=stmt3.executeQuery();
			if(rset3.next())
			{
				file_nm=rset3.getString(1)==null?"":rset3.getString(1);
			}
			rset3.close();
			stmt3.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
		
	public void doClearVec()
	{
		String function_nm="doClearVec()";
		try
		{
			VINVOICE_SEQ.clear(); 
			VSUPPLIER_CD.clear(); 
			VINVOICE_TYPE.clear(); 
			VFINANCIAL_YEAR.clear(); 
			VINV_FLAG.clear(); 
			VPERIOD_START_DT.clear(); 
			VAPPROVAL_DT.clear(); 
			VINVOICE_NO.clear(); 
			VDESC.clear(); 
			VACCOUNT_CD.clear(); 
			VBASE_AMT.clear(); 
			VDEBIT_CREDIT.clear(); 
			VCOA_CD.clear(); 
			VCURRENCY_CD.clear(); 
			VEMPLOYEE_CD.clear(); 
			VCOST_CTR_CD.clear(); 
			VBU_UNIT_CD.clear(); 
			VCODE.clear(); 
			VINVOICE_DUE_DT.clear(); 
			VINVOICE_DT.clear();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}	
		
	public void getApprovedDtl()
	{
		String function_nm="getApprovedDtl()";
		try
		{
			String queryString="SELECT SAP_APPROVED_BY,TO_CHAR(SAP_APPROVED_DT,'DD/MM/YYYY') FROM FMS_OTH_INVOICE_MST "
					+ "WHERE COMPANY_CD=? AND INVOICE_NO=? ";
			stmt3=conn.prepareStatement(queryString);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, Inv_no);
			rset3=stmt3.executeQuery();
			while(rset3.next())
			{
				sun_appr_by = rset3.getString(1)==null?"":utilBean.getEmpName(conn, rset3.getString(1));
				sun_appr_dt = rset3.getString(2)==null?"":rset3.getString(2);
			}
			rset3.close();
			stmt3.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}	
	
	public String getAccDes(String invoice_type, int index, String acc_cd)
	{
		String function_nm="getAccDes()";
		String account_des="";
		try
		{
			if(invoice_type.equals("COSTR"))
			{
				if(index==0)
				{
					account_des = "";
				}
				else if(index==1)
				{
					account_des = "";
				}
			}
			else if(invoice_type.equals("COSTRH"))
			{
				if(index==0)
				{
					account_des = "Hazira Port Private Limited";
				}
				else if(index==1)
				{
					account_des = getdes(invoice_type,acc_cd);
				}
				else if(index==2)
				{
					account_des = "Tax Applicable";
				}
				else if(index==3)
				{
					account_des = "Tax Applicable";
				}
			}
			else if(invoice_type.equals("HSA"))
			{
				if(index==0)
				{
					account_des = "Vendor Code";
				}
				else if(index==1)
				{
					account_des = getdes(invoice_type,acc_cd);
				}
				else if(index==2)
				{
					account_des = "Tax Applicable";
				}
				else if(index==3)
				{
					account_des = "Tax Applicable";
				}
			}
			else if(invoice_type.equals("HS"))
			{
				if(index==0)
				{
					account_des = "Shell Energy India Private Limited";
				}
				else if(index==1)
				{
					account_des = getdes(invoice_type,acc_cd);
				}
				else if(index==2)
				{
					account_des = "Tax Applicable";
				}
				else if(index==3)
				{
					account_des = "Tax Applicable";
				}
			}
			else if(invoice_type.equals("SFA"))
			{
				if(index==0)
				{
					account_des = "Outstanding Liability - Sale of Assets";
				}
				else if(index==1)
				{
					account_des = getdes(invoice_type,acc_cd);
				}
				else if(index==2)
				{
					account_des = "Tax Applicable";
				}
				else if(index==3)
				{
					account_des = "Tax Applicable";
				}
			}
			else if(invoice_type.equals("AHPL"))
			{
				if(index==0)
				{
					account_des = "Adani Hazira Port Limited";
				}
				else if(index==1)
				{
					account_des = "Adani Hazira Port Limited";
				}
				else if(index==2)
				{
					account_des = getdes(invoice_type,acc_cd);
				}
				else if(index==3)
				{
					account_des = "Tax Applicable";
				}
				else if(index==4)
				{
					account_des = "Tax Applicable";
				}
			}
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return account_des;
	}
	
	public String getdes(String inv_type, String acc_cd)
	{
		String function_nm="getdes()";
		String account_des="";
		try
		{
			String queryString="SELECT DESCR "
					+ "FROM FMS_OTH_INV_ACC_CODE_SUN "
					+ "WHERE INVOICE_TYPE=? AND ACCOUNT_CODE=?";
			stmt3=conn.prepareStatement(queryString);
			stmt3.setString(1, inv_type);
			stmt3.setString(2, acc_cd);
			rset3=stmt3.executeQuery();
			if(rset3.next())
			{
				account_des = rset3.getString(1)==null?"":rset3.getString(1);
			}
			else 
			{
				account_des="";
			}
			rset3.close();
			stmt3.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		
		return account_des;
	}
	
	
	String callFlag = "";
	public void setCallFlag(String callFlag) {this.callFlag = callFlag;}
	String comp_cd = "";
	public void setComp_cd(String comp_cd) {this.comp_cd = comp_cd;}
	String entity_role="";
	public void setEntity_role(String entity_role) {this.entity_role = entity_role;}
	String from_dt="";
	public void setFrom_dt(String from_dt) {this.from_dt=from_dt;}
	String to_dt="";
	public void setTo_dt(String to_dt) {this.to_dt=to_dt;}
	String emp_cd="";
	public void setEmp_cd(String emp_cd) {this.emp_cd=emp_cd;}
	String file_nm="";
	public void setFileNm(String file_nm) {this.file_nm = file_nm;}
	String xml_gen_from_dt="";
	public void setXml_gen_from_dt(String xml_gen_from_dt) {this.xml_gen_from_dt=xml_gen_from_dt;}
	String xml_gen_to_dt="";
	public void setXml_gen_to_dt(String xml_gen_to_dt) {this.xml_gen_to_dt=xml_gen_to_dt;}
	String segment="";
	public void setSegment(String segment) {this.segment=segment;}
	String file_path="";
	public void setFile_path(String filePath) {this.file_path=filePath;}
	String rem_no="";
	public void setRemittance_no(String remittance_no) {this.rem_no=remittance_no;}
	String Inv_no="";
	public void setInvoice_no(String Inv_no) {this.Inv_no=Inv_no;}
	
	public void setInv_type(String inv_type) {this.inv_type=inv_type;}
	
	String buStateTin="";
	String financial_year="";
	String inv_seq="";
	String inv_type="";
	
	String msg="";
	String msg_type="";
	String sun_appr_by="";
	String sun_appr_dt="";
	
	public String getMsg() {return msg;}
	public String getMsg_type() {return msg_type;}
	public String getSunApprovedBy() {return sun_appr_by;}
	public String getSunApprovedDt() {return sun_appr_dt;}
	
	Vector VMASTER_TAX_CATEGORY = new Vector();
	Vector VMASTER_TAX_CATEGORY_NM = new Vector();
	
	Vector VBU_SEQ = new Vector();
	Vector VBU_ABBR = new Vector();
	Vector VCO_CD = new Vector();
	Vector VBU_STATE = new Vector(); 
	
	Vector VTAX_STRUCT_CD = new Vector();
	Vector VTAX_STRUCT_NM = new Vector();
	Vector VTAX_STRUCT_STATUS = new Vector();
	Vector VTAX_STRUCT_RMK = new Vector();
	Vector VTAX_CATEGORY = new Vector();
	Vector VTAX_CATEGORY_NM = new Vector();
	Vector VTAX_STRUCT_APP_DT = new Vector();
	Vector VSAP_TAX_CODE = new Vector();
	Vector VPAY_RECV_NM = new Vector();
	Vector VINDEX = new Vector();
	Vector VTAX_COUNT = new Vector();
	Vector VSUB_TAX_STRUCT_NM = new Vector();
	Vector VSUN_CD = new Vector();
	Vector VSUG_CD = new Vector();
	Vector VSUB_SUN_CD = new Vector();
	Vector VSUB_SUG_CD = new Vector();
	Vector VTAX_CD = new Vector();
	
	Vector VVENDOR_CD = new Vector();
	Vector VVENDOR_NM = new Vector();   
	Vector VVENDOR_ABBR = new Vector();   
	Vector VSUPPLIER_CD = new Vector();
	
	Vector VACCOUNT_TYPE = new Vector();
	Vector VACCOUNT_TYPE_NM = new Vector();
	Vector VSUN_ACCOUNT = new Vector();
	Vector VSUN_ENTITY_ACCOUNT = new Vector();
	Vector VPLANT_SEQ_NO = new Vector();
	
	Vector VPLANT_ABBR = new Vector();
	Vector VPLANT_INDEX = new Vector();
	Vector VACC_PLANT = new Vector();
	Vector VACC_OTH_PLANT = new Vector();
	
	Vector VINVOICE_NO= new Vector();
	Vector VJOURNAL_TYPE= new Vector();
	Vector VAPPROVAL_DT= new Vector();
	Vector VLEDGER= new Vector();
	Vector VACCOUNT_CD= new Vector();
	Vector VPERIOD_START_DT= new Vector();
	Vector VPERIOD_END_DT= new Vector();
	Vector VBASE_AMT= new Vector();
	Vector VDEBIT_CREDIT= new Vector();
	Vector VREPORT_AMT= new Vector();
	Vector VCURRENCY_CD= new Vector();
	Vector VEXCHNG_RATE= new Vector();
	Vector VINVOICE_DT= new Vector();
	Vector VDESC= new Vector();
	Vector VINVOICE_DUE_DT= new Vector();
	Vector VCOST_CTR_CD= new Vector();
	Vector VCOA_CD= new Vector();
	Vector VCODE= new Vector();
	Vector VBU_UNIT_CD= new Vector();
	Vector VGOOD_SERVICE= new Vector();		
	Vector VREV_CHARGE= new Vector();
	Vector VHSN_CD= new Vector();
	Vector VPOS_CD= new Vector();
	Vector VTAX_LINE_AMT= new Vector();
	Vector VSUPPLY_TYPE= new Vector();
	Vector VTOTAL_INV_AMT= new Vector();
	Vector VEMPLOYEE_CD = new Vector();
	Vector VTRANS_AMT=new Vector();
	Vector VPROJECT_CD=new Vector();
	
	Vector VJOURNAL_TYPE_NM=new Vector();
	Vector VSUN_FILE_NM=new Vector();
	
	Vector VSEGMENT=new Vector();
	Vector VSEGMENT_TYPE=new Vector();
	
	Vector VTEMP_SEGMENT=new Vector();
	Vector VTEMP_SEGMENT_TYPE=new Vector();
	
	Vector VTDS_TAX_AMT = new Vector();
	Vector VTDS_TAX_PERCENT = new Vector();
	Vector VPAY_RECV_AMT= new Vector();
	Vector VPAY_RECV_DT= new Vector();
	Vector VAGMT_NO= new Vector();
	Vector VAGMT_REV_NO= new Vector();
	Vector VCONT_NO= new Vector();
	Vector VCONT_REV_NO= new Vector();
	Vector VFINANCIAL_YEAR= new Vector();
	Vector VBU_STATE_TIN= new Vector();
	Vector VINVOICE_SEQ= new Vector();
	Vector VSALES_PRICE= new Vector();
	Vector VSALES_PRICE_CD= new Vector();
	Vector VSALES_PRICE_NM= new Vector();
	Vector VGROSS_AMT= new Vector();
	Vector VTAX_AMT= new Vector();
	Vector VINVOICE_AMT= new Vector();
	Vector VNET_PAYABLE_AMT= new Vector();
	Vector VSHORT_RECEIVED= new Vector();
	Vector VTDS_TCS_FLAG= new Vector();
	Vector VTCS_AMT= new Vector();
	Vector VTAX_STRUCT_DTL= new Vector();
	Vector VBU_NM= new Vector();
	Vector VSAP_APPROVAL_FLAG= new Vector();
	Vector VTYPE_FLAG= new Vector();
	Vector VALLOC_QTY= new Vector();
	Vector VINVOICE_RAISED_IN= new Vector();
	Vector VPAYMENT_DONE_IN= new Vector();
	Vector VTCS_TDS= new Vector();
	Vector VTDS_GROSS_AMT= new Vector();
	Vector VTDS_GROSS_PERCENT= new Vector();
	Vector VSALES_AMT= new Vector();
	Vector VINVOICE_TYPE= new Vector();
	Vector VCONT_REF_NO= new Vector();
	Vector VCASH_FLOW= new Vector();
	Vector VAPPROVE_HIST=new Vector();
	
	Vector VPAYMENT_TYPE_FLAG=new Vector();
	Vector VPAYMENT_TYPE_NM=new Vector();
	Vector VDIS_REMITTANCE_NO=new Vector();
	Vector VSALES_PRICE_UNIT=new Vector();
	Vector VQTY_UNIT=new Vector();
	Vector VSALE_AMT=new Vector();
	Vector VADJ_SIGN=new Vector();
	Vector VEXCHNAGE_RATE_DATE=new Vector();
	Vector VINV_FLAG=new Vector();
	Vector VTCS_TDS_AMT_USD=new Vector();
	Vector VTCS_TDS_AMT=new Vector();
	Vector VTCS_TDS_FACTOR=new Vector();
	Vector VTCS_TDS_STRUCT_CD=new Vector();
	Vector VTCS_TDS_EFF_DT=new Vector();
	Vector VTCS_TDS_DONE=new Vector();
	Vector VGROSS_AMT_USD=new Vector();
	Vector VTAX_AMT_USD=new Vector();
	Vector VINVOICE_AMT_USD=new Vector();
	Vector VADJ_AMT_USD=new Vector();
	Vector VNET_PAYABLE_USD=new Vector();
	Vector VADJ_AMT=new Vector();
	Vector VNET_PAYABLE=new Vector();
	Vector VEXCHNAGE_RATE=new Vector();
	Vector VSAP_EXCHANG_FLAG=new Vector();
	Vector VSPLIT_VALUE=new Vector();
	Vector VEXCHNG_RATE_CD = new Vector();
	Vector VEXCHNG_RATE_CONFIG = new Vector();
	Vector VACT_ARRIVAL_DT = new Vector();
	Vector VTRANS_DT = new Vector();
	
	public Vector getVSEGMENT() {return VSEGMENT;}
	public Vector getVSEGMENT_TYPE() {return VSEGMENT_TYPE;}
	
	public Vector getVTEMP_SEGMENT() {return VTEMP_SEGMENT;}
	public Vector getVTEMP_SEGMENT_TYPE() {return VTEMP_SEGMENT_TYPE;}
	
	public Vector getVINVOICE_TYPE() {return VINVOICE_TYPE;}
	public Vector getVFINANCIAL_YEAR() {return VFINANCIAL_YEAR;}
	public Vector getVBU_STATE_TIN() {return VBU_STATE_TIN;}
	public Vector getVINVOICE_SEQ() {return VINVOICE_SEQ;}
	public Vector getVGROSS_AMT() {return VGROSS_AMT;}
	public Vector getVTAX_AMT() {return VTAX_AMT;}
	public Vector getVINVOICE_AMT() {return VINVOICE_AMT;}
	public Vector getVNET_PAYABLE_AMT() {return VNET_PAYABLE_AMT;}
	public Vector getVTAX_STRUCT_DTL() {return VTAX_STRUCT_DTL;}
	public Vector getVBU_NM() {return VBU_NM;}
	public Vector getVSAP_APPROVAL_FLAG() {return VSAP_APPROVAL_FLAG;}
	public Vector getVTYPE_FLAG() {return VTYPE_FLAG;}
	public Vector getVINVOICE_RAISED_IN() {return VINVOICE_RAISED_IN;}
	public Vector getVPAYMENT_DONE_IN() {return VPAYMENT_DONE_IN;}
	public Vector getVTDS_GROSS_AMT() {return VTDS_GROSS_AMT;}
	public Vector getVCASH_FLOW() {return VCASH_FLOW;}
	public Vector getVAPPROVE_HIST() {return VAPPROVE_HIST;}
	public Vector getVVENDOR_NM() {return VVENDOR_NM;}
	public Vector getVVENDOR_ABBR() {return VVENDOR_ABBR;}
	
	public Vector getVPAYMENT_TYPE_FLAG() {return VPAYMENT_TYPE_FLAG;}
	public Vector getVPAYMENT_TYPE_NM() {return VPAYMENT_TYPE_NM;}
	public Vector getVDIS_REMITTANCE_NO() {return VDIS_REMITTANCE_NO;}
	public Vector getVSALES_PRICE_UNIT() {return VSALES_PRICE_UNIT;}
	public Vector getVQTY_UNIT() {return VQTY_UNIT;}
	public Vector getVSALE_AMT() {return VSALE_AMT;}
	public Vector getVADJ_SIGN() {return VADJ_SIGN;}
	public Vector getVEXCHNAGE_RATE_DATE() {return VEXCHNAGE_RATE_DATE;}
	public Vector getVINV_FLAG() {return VINV_FLAG;}
	public Vector getVTCS_TDS_AMT_USD() {return VTCS_TDS_AMT_USD;}
	public Vector getVTCS_TDS_AMT() {return VTCS_TDS_AMT;}
	public Vector getVTCS_TDS_FACTOR() {return VTCS_TDS_FACTOR;}
	public Vector getVTCS_TDS_STRUCT_CD() {return VTCS_TDS_STRUCT_CD;}
	public Vector getVTCS_TDS_EFF_DT() {return VTCS_TDS_EFF_DT;}
	public Vector getVTCS_TDS_DONE() {return VTCS_TDS_DONE;}
	public Vector getVGROSS_AMT_USD() {return VGROSS_AMT_USD;}
	public Vector getVTAX_AMT_USD() {return VTAX_AMT_USD;}
	public Vector getVINVOICE_AMT_USD() {return VINVOICE_AMT_USD;}
	public Vector getVADJ_AMT_USD() {return VADJ_AMT_USD;}
	public Vector getVNET_PAYABLE_USD() {return VNET_PAYABLE_USD;}
	public Vector getVADJ_AMT() {return VADJ_AMT;}
	public Vector getVNET_PAYABLE() {return VNET_PAYABLE;}
	public Vector getVEXCHNAGE_RATE() {return VEXCHNAGE_RATE;}
	public Vector getVSAP_EXCHANG_FLAG() {return VSAP_EXCHANG_FLAG;}
	public Vector getVSPLIT_VALUE() {return VSPLIT_VALUE;}
	public Vector getVEXCHNG_RATE_CD() {return VEXCHNG_RATE_CD;}
	public Vector getVEXCHNG_RATE_CONFIG() {return VEXCHNG_RATE_CONFIG;}
	public Vector getVACT_ARRIVAL_DT() {return VACT_ARRIVAL_DT;}
	public Vector getVTRANS_DT() {return VTRANS_DT;}

	public Vector getVINVOICE_NO() {return VINVOICE_NO;}
	public Vector getVJOURNAL_TYPE() {return VJOURNAL_TYPE;}
	public Vector getVAPPROVAL_DT() {return VAPPROVAL_DT;}
	public Vector getVLEDGER() {return VLEDGER;}
	public Vector getVACCOUNT_CD() {return VACCOUNT_CD;}
	public Vector getVPERIOD_START_DT() {return VPERIOD_START_DT;}
	public Vector getVPERIOD_END_DT() {return VPERIOD_END_DT;}
	public Vector getVBASE_AMT() {return VBASE_AMT;}
	public Vector getVDEBIT_CREDIT() {return VDEBIT_CREDIT;}
	public Vector getVREPORT_AMT() {return VREPORT_AMT;}
	public Vector getVCURRENCY_CD() {return VCURRENCY_CD;}
	public Vector getVEXCHNG_RATE() {return VEXCHNG_RATE;}
	public Vector getVINVOICE_DT() {return VINVOICE_DT;}
	public Vector getVDESC() {return VDESC;}
	public Vector getVINVOICE_DUE_DT() {return VINVOICE_DUE_DT;}
	public Vector getVCOST_CTR_CD() {return VCOST_CTR_CD;}
	public Vector getVCOA_CD() {return VCOA_CD;}
	public Vector getVCODE() {return VCODE;}
	public Vector getVBU_UNIT_CD() {return VBU_UNIT_CD;}
	public Vector getVGOOD_SERVICE() {return VGOOD_SERVICE;}
	public Vector getVREV_CHARGE() {return VREV_CHARGE;}
	public Vector getVHSN_CD() {return VHSN_CD;}
	public Vector getVPOS_CD() {return VPOS_CD;}
	public Vector getVTAX_LINE_AMT() {return VTAX_LINE_AMT;}
	public Vector getVSUPPLY_TYPE() {return VSUPPLY_TYPE;}
	public Vector getVTOTAL_INV_AMT() {return VTOTAL_INV_AMT;}
	public Vector getVEMPLOYEE_CD() {return VEMPLOYEE_CD;}
	public Vector getVTRANS_AMT() {return VTRANS_AMT;}
	public Vector getVPROJECT_CD() {return VPROJECT_CD;}
	
	public Vector getVSUN_FILE_NM() {return VSUN_FILE_NM;}
	public Vector getVJOURNAL_TYPE_NM() {return VJOURNAL_TYPE_NM;}
	
	public Vector getVMASTER_TAX_CATEGORY() {return VMASTER_TAX_CATEGORY;}
	public Vector getVMASTER_TAX_CATEGORY_NM() {return VMASTER_TAX_CATEGORY_NM;}

	public Vector getVBU_SEQ() {return VBU_SEQ;}
	public Vector getVBU_ABBR() {return VBU_ABBR;}
	public Vector getVCO_CD() {return VCO_CD;}
	public Vector getVBU_STATE() {return VBU_STATE;}

	public Vector getVTAX_STRUCT_CD() {return VTAX_STRUCT_CD ;}
	public Vector getVTAX_STRUCT_NM() {return VTAX_STRUCT_NM ;}
	public Vector getVTAX_STRUCT_STATUS() {return  VTAX_STRUCT_STATUS;}
	public Vector getVTAX_STRUCT_RMK() {return VTAX_STRUCT_RMK ;}
	public Vector getVTAX_CATEGORY() {return VTAX_CATEGORY ;}
	public Vector getVTAX_CATEGORY_NM() {return VTAX_CATEGORY_NM ;}
	public Vector getVTAX_STRUCT_APP_DT() {return VTAX_STRUCT_APP_DT;}
	public Vector getVSAP_TAX_CODE() {return VSAP_TAX_CODE;}
	public Vector getVPAY_RECV_NM() {return  VPAY_RECV_NM;}
	public Vector getVINDEX() {return VINDEX;}
	public Vector getVTAX_COUNT() {return VTAX_COUNT;}
	public Vector getVSUB_TAX_STRUCT_NM() {return VSUB_TAX_STRUCT_NM;}
	public Vector getVSUN_CD() {return VSUN_CD;}
	public Vector getVSUG_CD() {return VSUG_CD;}
	public Vector getVSUB_SUN_CD() {return VSUB_SUN_CD;}
	public Vector getVSUB_SUG_CD() {return VSUB_SUG_CD;}
	
	public Vector getVVENDOR_CD() {return VVENDOR_CD;}
	public Vector getVSUPPLIER_CD() {return VSUPPLIER_CD;}
	
	public Vector getVSUN_ACCOUNT() {return VSUN_ACCOUNT;}
	public Vector getVSUN_ENTITY_ACCOUNT() {return VSUN_ENTITY_ACCOUNT;}
	public Vector getVACCOUNT_TYPE() {return VACCOUNT_TYPE;}
	public Vector getVACCOUNT_TYPE_NM() {return VACCOUNT_TYPE_NM;}
	public Vector getVPLANT_SEQ_NO() {return VPLANT_SEQ_NO;}
	public Vector getVPLANT_ABBR() {return VPLANT_ABBR;}
	public Vector getVPLANT_INDEX() {return VPLANT_INDEX;}
	public Vector getVACC_PLANT() {return VACC_PLANT;}
	public Vector getVACC_OTH_PLANT() {return VACC_OTH_PLANT;}

}