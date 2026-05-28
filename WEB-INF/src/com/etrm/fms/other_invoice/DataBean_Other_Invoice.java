package com.etrm.fms.other_invoice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.CurrencyUtil;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;

//Coded By          : Deep Tank,Arth Patel
//Code Reviewed by	:  
//CR Date			: 07/10/2025
//Status	  		: Developing
public class DataBean_Other_Invoice 
{
	String db_src_file_name="DataBean_Other_Invoice.java";

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
	PreparedStatement stmt8;
	ResultSet rset;
	ResultSet rset0; 
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset3;
	ResultSet rset4;
	ResultSet rset5;
	ResultSet rset6;
	ResultSet rset7;
	ResultSet rset8;
	String queryString="";
	String queryString0="";
	String queryString1="";
	String queryString2="";
	String queryString3="";
	String queryString4="";
	String queryString5="";
	String queryString6="";
	String queryString7="";
	String queryString8="";
	
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf3 = new DecimalFormat("###########0.000");
	NumberFormat nf0 = new DecimalFormat("###########0.0");
	NumberFormat nf00 = new DecimalFormat("###########0");
	
	UtilBean utilBean = new UtilBean();
	DateUtil utilDate = new DateUtil();
	CurrencyUtil es=new CurrencyUtil();
	
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
	    			
	    			if (callFlag.equalsIgnoreCase("VENDOR_MASTER")) 
	    			{
	    				getCountryMst();
	    				getStateMst();
	    				
	    				if(opration.equalsIgnoreCase("MODIFY"))
	    				{
	    					
	    					if(!vendor_cd.equals("0") && !vendor_cd.equals(""))
	    					{
	    						getVendorDetail();              
	    						getVendorAddressDetail();      
	    					}
	    					else 
	    					{
	    						for (int i = 0; i < 3; i++) 
	    						{
	    							reg_eff_dt[i] = "";
	    							address_flag[i] = "";
	    							address[i] = "";
	    							city[i] = "";
	    							pin[i] = "";
	    							state[i] = "";
	    							zone[i] = "0";
	    							country[i] = "0";
	    							phone[i] = "";
	    							mobile[i] = "";
	    							alt_phone[i] = "";
	    							fax1[i] = "";
	    							fax2[i] = "";
	    							email[i] = "";
	    						}
	    					}
	    				}
    					else 
    					{
    						for (int i = 0; i < 3; i++) 
    						{
    							reg_eff_dt[i] = "";
    							address_flag[i] = "";
    							address[i] = "";
    							city[i] = "";
    							pin[i] = "";
    							state[i] = "";
    							zone[i] = "0";
    							country[i] = "0";
    							phone[i] = "";
    							mobile[i] = "";
    							alt_phone[i] = "";
    							fax1[i] = "";
    							fax2[i] = "";
    							email[i] = "";
    						}
    					}
	    			}
	    			else if(callFlag.equalsIgnoreCase("VENDOR_LIST")) 
	    			{
	    				getVendorList();
	    			}
	    			else if(callFlag.equals("OTHER_INVOICE_LIST")) // Deep20251010
	    			{
	    				getInvoiceType();
	    				getOtherInvoicesList();
	    			}
	    			else if(callFlag.equals("OTH_INV_COST_RECHARGE"))
	    			{
	    				getSupplierList();
	    				getVendorList();
	    				getSAC_List();
	    				
	    				if(opration.equals("INSERT")) 
	    				{
	    					getSupplierDtl();
		    				getVendorDtl();
	    				}
	    				else if(opration.equals("MODIFY"))   //Deep20251015
	    				{   										
							getInvoiceDtls();   
							getSupplierDtl();
		    				getVendorDtl();
	    				}
	    			}
	    			else if(callFlag.equals("COST_RECHARGE_APROVAL"))    //Deep20251015
	    			{
	    				getInvoiceDtls();
	    				getSupplierDtl();
	    				getVendorDtl();
	    				if(opration.equals("APPROVE"))
	    				{
	    					getInvoiceNo();
	    				}
	    			}
	    			else if(callFlag.equals("OTHER_INVOICE_COST_RECHARGE_HPPL")) //AP20251014
	    			{
    					getSupplierList();
    					getSAC_List();
    					if(opration.equals("INSERT")) 
	    				{
    						getSupplierDtl();
    	    				getVendorDtl();
	    				}
    					else if(opration.equals("MODIFY"))
	    				{
    						getInvoiceDtlHPPL();
    						getSupplierDtl();
		    				getVendorDtl();
	    				}
	    			}
	    			else if(callFlag.equals("COST_RECHARGE_HPPL_APROVAL"))    //AP20251015
	    			{
	    				getInvoiceDtlHPPL();
	    				getSupplierDtl();
	    				getVendorDtl();
	    				if(opration.equals("APPROVE"))
	    				{
	    					getInvoiceNo();
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("VIEW_ALL_PDF"))
	    			{
	    				getAllPdfFileName();
	    			}
	    			else if(callFlag.equalsIgnoreCase("TAX_STRUCTURE_LIST")) 
	    			{
	    				getTaxStructureList();
	    			}
	    			else if(callFlag.equals("OTH_INV_SHIPPING_AGENT")) //AP20251101
	    			{
	    				getSupplierList();
	    				getVendorList();
	    				getSAC_List();
	    				if(opration.equals("INSERT")) 
	    				{
	    					getShipMst();
	    					getSupplierDtl();
		    				getVendorDtl();
	    				}
	    				else if(opration.equals("MODIFY"))
	    				{   										
	    					getInvoiceDtlHPPLShippingAgent();   
							getSupplierDtl();
		    				getVendorDtl();
		    				getShipMstMod();
	    				}
	    				
	    			}
	    			else if(callFlag.equals("HPPL_SHIPPING_AGENT_APROVAL"))    //AP20251105
	    			{
	    				getInvoiceDtlHPPLShippingAgent();
	    				getSupplierDtl();
	    				getVendorDtl();
	    				if(opration.equals("APPROVE"))
	    				{
	    					getInvoiceNo();
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("VESSEL_SHIP_DTL"))
	    			{
	    				getShipMst();
	    			}
	    			else if(callFlag.equals("OTH_INV_HPPL_SEIPL")) //Deep20251104
	    			{
	    				getSupplierList();
	    				getSAC_List();
	    				if(opration.equals("INSERT")) 
	    				{
	    					getSupplierDtl();
		    				getVendorDtl();
		    				getCargoDetails();
	    				}
	    				else if(opration.equals("MODIFY")) 
	    				{   										
	    					getInvoiceDtlHPPLSEIPL();   
							getSupplierDtl();
		    				getVendorDtl();
		    				getCargoDetailsModify();
	    				}
	    				getExchangeRate();
	    			}
	    			else if(callFlag.equals("HPPL_SEIPL_APPROVAL"))  //Deep20251107
	    			{
	    				getInvoiceDtlHPPLSEIPL();   
						getSupplierDtl();
	    				getVendorDtl();
	    				getCargoDetailschkappr();
	    				if(opration.equals("APPROVE"))
	    				{
	    					getInvoiceNo();
	    				}
	    			}
	    			else if(callFlag.equals("OTH_INV_NPR"))  //AP20251110
	    			{
	    				getSupplierList();
	    				getVendorList();
	    				getSAC_List();
	    				
	    				if(opration.equals("INSERT")) 
	    				{
	    					getSupplierDtl();
		    				getVendorDtl();
	    				}
	    				else if(opration.equals("MODIFY"))
	    				{   										
							getInvoiceDtlsNPR();   
							getSupplierDtl();
		    				getVendorDtl();
	    				}
	    			}
	    			else if(callFlag.equals("NPR_APROVAL"))    //AP20251110
	    			{
	    				getInvoiceDtlsNPR();
	    				getSupplierDtl();
	    				getVendorDtl();
	    				if(opration.equals("APPROVE"))
	    				{
	    					getInvoiceNo();
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("EXISTING_CRDR_LIST")) //AP20251112 //DT20260130
	    			{
	    				getInvoiceTypeforCRDR();
	    				getOtherInvCRDRList();
	    			}
					else if(callFlag.equalsIgnoreCase("HSA_CRDR_LIST")) //AP20251112 //DT20260131
	    			{
	    				getInvoiceTypeforCRDR();
	    				getInvoiceList();
	    				getCriteriaList();
	    				getSelectedInvoiceDtl();
	    				getCrDrInvoiceDtl();
	    				getCrDrRefDetail();
	    			}
					else if(callFlag.equalsIgnoreCase("HSA_CRDR_APROVAL")) //AP20251120
					{
						getInvoiceTypeforCRDR();
						getInvoiceList();
						getCriteriaList();
						getSelectedInvoiceDtl();
						getCrDrInvoiceDtl();
						getCrDrRefDetail();
						getCrDrCkpAprView();
						getSupplierDtl();
	    				getVendorDtl();
	    				if(opration.equals("APPROVE"))
	    				{
	    					getInvoiceNo();
	    				}
					}
	    			else if(callFlag.equals("OTH_INV_AHPL_SHARE"))  //Deep20251112
	    			{
	    				getSupplierList();
	    				getVendorList();
	    				getSAC_List();
	    				VLINE_NO.add("1");
	    				VLINE_NO1.add("1");
	    				
	    				if(opration.equals("INSERT")) 
	    				{
	    					getSupplierDtl();
		    				getVendorDtl();
	    				}
	    				if(opration.equals("MODIFY"))
	    				{
	    					getInvoiceDtlsAHPLShare();
	    					getSupplierDtl();
		    				getVendorDtl();
	    				}
	    			}
	    			else if(callFlag.equals("AHPL_SHARE_APPROVAL"))  //Deep20251115
	    			{
	    				getInvoiceDtlsAHPLShare();
	    				getSupplierDtl();
	    				getVendorDtl();
	    				if(opration.equals("APPROVE"))
	    				{
	    					getInvoiceNo();
	    				}
	    			}
	    			else if(callFlag.equals("OTH_INV_SCRAP_FIXED_ASSET")) //Deep20251117
	    			{
	    				getSupplierList();
	    				getVendorList();
	    				
	    				if(opration.equals("INSERT")) 
	    				{
	    					VLINE_NO.add("1");
	    					VGAMT_DES.add("");
	    					VPRICE.add("");
	    					VSAC_CODE.add("");
	    					VQTY.add("");
	    					VUOM_NO.add("");
	    					VGITEM_AMT.add("");
	    					VTAX_STRUCT_APP_DT.add("");
	    					VTAX_STRUCTURE_CD.add("");
	    					VTAX_STRUCTURE_DESC.add("");
	    					VTAX_AMT.add("");
	    					VCESS_PER.add("");
	    					VCESS_AMT.add("");
	    					
	    					getSupplierDtl();
		    				getVendorDtl();
	    				}
	    				if(opration.equals("MODIFY"))
	    				{
	    					getInvoiceDtlsSFA();
	    					getSupplierDtl();
		    				getVendorDtl();
	    				}
	    			}
	    			else if(callFlag.equals("OTH_INV_SFA_APPROVAL"))  //Deep20251120
	    			{
	    				getInvoiceDtlsSFA();
	    				getSupplierDtl();
	    				getVendorDtl();
	    				if(opration.equals("APPROVE"))
	    				{
	    					getInvoiceNo();
	    				}
	    			}
	    			else if(callFlag.equals("OTH_INV_GA_INVOICE")) //Deep20251229
	    			{
	    				getCompOwnerList();
	    				getCompOwnerBUList();
	    				getVendorList();
	    				getSAC_List();
	    				
	    				if(opration.equals("INSERT")) 
	    				{
	    					VLINE_NO.add("1");
	    					VGAMT_DES.add("");
	    					VSAC_CODE.add("");
	    					VSAC_DESC.add("0");
	    					VGITEM_AMT.add("");
	    					VTAX_STRUCT_APP_DT.add("");
	    					VTAX_STRUCTURE_CD.add("");
	    					VTAX_STRUCTURE_DESC.add("");
	    					VTAX_AMT.add("");
	    					VSAC_VAL.add("0");
	    					VITEM_TOTAL.add("");
	    					VTAX_STRUCTURE.add("");
	    					VTAX_STRUCTURE1.add("");
	    					
	    					getCompOwnerDtl();
		    				getVendorDtl();
	    				}
	    				if(opration.equals("MODIFY"))
	    				{
	    					getInvoiceDtlsGA();
	    					getCompOwnerDtl();
		    				getVendorDtl();
	    				}
	    			}
	    			else if(callFlag.equals("OTH_INV_GA_APPROVAL"))  //Deep20251231
	    			{
	    				getInvoiceDtlsGA();
	    				getCompOwnerDtl();
	    				getVendorDtl();
	    				if(opration.equals("APPROVE"))
	    				{
	    					getInvoiceNo();
	    				}
	    			}
	    			else if(callFlag.equalsIgnoreCase("AHPL_CRDR_LIST")) //DT20260131
	    			{
	    				getInvoiceTypeforCRDR();
	    				getInvoiceList();
	    				getCriteriaList();
	    				getAhplInvoiceDtl();
	    				getSupplierDtl();
	    				getVendorDtl();
	    				getAhplCrDrInvoiceDtl();
	    				getAhplCrDrRefDetail();
	    			}
	    			else if(callFlag.equalsIgnoreCase("AHPL_CRDR_APROVAL")) //DT20260201
					{
	    				getAhplInvoiceDtl();
	    				getSupplierDtl();
	    				getVendorDtl();
	    				getAhplCrDrInvoiceDtl();
	    				getAhplCrDrRefDetail();
	    				if(opration.equals("APPROVE"))
	    				{
	    					getInvoiceNo();
	    				}
					}
	    			else if(callFlag.equalsIgnoreCase("PFA_CRDR_LIST")) //DT20260210
	    			{
	    				getInvoiceTypeforCRDR();
	    				getInvoiceList();
	    				getCriteriaList();
	    				getPFAInvoiceDtl();
	    				getInvoiceCargoDetails();
	    				getPFACrDrInvoiceDtl();
	    				getPFACrDrCargoDetails();
	    				if(opration.equals("INSERT"))
	    				{
	    					getnewCargodetails();
	    				}
	    				else
	    				{
	    					getPFACrDrRefCargoDetails();
	    				}
	    				getPFACrDrRefDetail();
	    			}
	    			else if(callFlag.equalsIgnoreCase("PFA_CRDR_APROVAL")) //DT20260220
					{
	    				getPFAInvoiceDtl();
	    				getSupplierDtl();
	    				getVendorDtl();
	    				getInvoiceCargoDetails();
	    				getPFACrDrInvoiceDtl();
	    				getPFACrDrCargoDetails();
	    				getPFACrDrRefCargoDetails();
	    				getPFACrDrRefDetail();
	    				if(opration.equals("APPROVE"))
	    				{
	    					getInvoiceNo();
	    				}
					}
	    			else if(callFlag.equalsIgnoreCase("COSTRH_CRDR_LIST")) //AJAY20260221 
	    			{
	    				getInvoiceTypeforCRDR();
	    				getInvoiceList();
	    				getCriteriaList();
	    				getCostRHInvoiceDtl();
	    				getCostRHCrDrInvoiceDtl();
	    				getCostRHCrDrRefDetail();
	    				getSupplierDtl();
	    				getVendorDtl();
	    			}
	    			else if(callFlag.equalsIgnoreCase("COSTRH_CRDR_APROVAL")) //AJAY20260221
					{
	    				getCostRHInvoiceDtl();
	    				getSupplierDtl();
	    				getVendorDtl();
	    				getCostRHCrDrInvoiceDtl();
	    				getCostRHCrDrRefDetail();
	    				if(opration.equals("APPROVE"))
	    				{
	    					getInvoiceNo();
	    				}
					}
	    			else if(callFlag.equalsIgnoreCase("COSTR_CRDR_LIST")) //AJAY20260221 
	    			{
	    				getInvoiceTypeforCRDR();
	    				getInvoiceList();
	    				getCriteriaList();
	    				getCostRInvoiceDtl();
	    				getCostRCrDrInvoiceDtl();
	    				getCostRCrDrRefDetail();
	    				getSupplierDtl();
	    				getVendorDtl();
	    			}
	    			else if(callFlag.equalsIgnoreCase("COSTR_CRDR_APROVAL")) //AJAY20260221
					{
	    				getCostRInvoiceDtl();
	    				getSupplierDtl();
	    				getVendorDtl();
	    				getCostRCrDrInvoiceDtl();
	    				getCostRCrDrRefDetail();
	    				if(opration.equals("APPROVE"))
	    				{
	    					getInvoiceNo();
	    				}
					}
	    			else if(callFlag.equalsIgnoreCase("SFA_CRDR_LIST")) //DT20260310 
	    			{
	    				getInvoiceTypeforCRDR();
	    				getInvoiceList();
	    				getCriteriaList();
	    				getSFAInvoiceDtl();
	    				getSFARCrDrInvoiceDtl();
	    				getSFARCrDrItemDtl();
	    				getSFACrDrRefItemDetail();
	    				getSFACrDrRefDetail();
	    				getSupplierDtl();
	    				getVendorDtl();
	    			}
	    			else if(callFlag.equalsIgnoreCase("SFA_CRDR_APROVAL")) //DT20260318
	    			{
	    				getSFAInvoiceDtl();
	    				getSupplierDtl();
	    				getVendorDtl();
	    				getSFARCrDrInvoiceDtl();
	    				getSFARCrDrItemDtl();
	    				getSFACrDrRefItemDetail();
	    				getSFACrDrRefDetail();
	    				if(opration.equals("APPROVE"))
	    				{
	    					getInvoiceNo();
	    				}
	    			}
                    else if(callFlag.equals("OTH_INV_RE_EXPORT")) 
	    			{
	    				getSupplierList();
	    				getVendorList();
	    				
	    				if(opration.equals("INSERT")) 
	    				{
	    					VLINE_NO.add("1");
	    					VGAMT_DES.add("");
	    					VPRICE.add("");
	    					VSAC_CODE.add("");
	    					VQTY.add("");
	    					VUOM_NO.add("");
	    					VGITEM_AMT.add("");
	    					VDIMENSION.add("");
	    					VNET_WT.add("");
	    					VGROSS_WT.add("");
	    					VPACK_DTLS.add("");
	    					
	    					getSupplierDtl();
		    				getVendorDtl();
		    				getVendorShipBillAddr();
	    				}
	    				if(opration.equals("MODIFY"))
	    				{
	    					getInvoiceDtlsRXP();
	    					getSupplierDtl();
		    				getVendorDtl();
		    				getVendorShipBillAddr();
	    				}
	    			}
	    			else if(callFlag.equals("OTH_INV_RE_EXPORT_APPROVAL"))  
	    			{
	    				getInvoiceDtlsRXP();
	    				getSupplierDtl();
	    				getVendorDtl();
	    				getVendorShipBillAddr();
	    				if(opration.equals("APPROVE"))
	    				{
	    					getInvoiceNo();
	    				}
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
	    	if(rset8 != null){try{rset8.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
	    	if(stmt0 != null){try{stmt0.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
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
	
	//Deep20251004
	public void getVendorList()
	{
		String function_nm="getVendorList()";

		try
		{
			if(!callFlag.equals("VENDOR_LIST"))
			{
				entity_role="V";
			}
			String queryString = "SELECT A.ENTITY_CD,A.ENTITY_NAME,A.ENTITY_ABBR,A.BUSINESS_FLAG "
					+ "FROM FMS_OTH_ENTITY_MST A "
					+ "WHERE A.ENTITY_TYPE = ? "
					+ "AND EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS_OTH_ENTITY_MST B WHERE A.ENTITY_TYPE = B.ENTITY_TYPE AND A.ENTITY_CD=B.ENTITY_CD) ";
			if(callFlag.equals("VENDOR_LIST"))
			{
				queryString	+= "AND A.ACTIVE_FLAG = 'Y' ";
			}
			else if(callFlag.equals("OTH_INV_AHPL_SHARE"))
			{
				queryString	+= "AND A.ENTITY_ABBR = 'AHPL' ";
			}
			queryString	+= "ORDER BY A.ENTITY_ABBR ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1,entity_role);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VVENDOR_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VENDOR_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VENDOR_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
				if (entity_role.equals("V")) 
				{
					VINVOICE_TYPE.add(rset.getString(4).equals("B") ? "B2B" : "B2C");
				}
				if(callFlag.equals("OTH_INV_AHPL_SHARE"))
				{
					vendor_cd=rset.getString(1)==null?"":rset.getString(1);
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
	
	//Deep20251004
	public void getVendorDetail()   
	{	
		String function_nm="getVendorDetail()";

		try
		{
			String queryString="SELECT TO_CHAR(A.EFF_DT,'DD/MM/YYYY'),A.ENTITY_NAME,A.ENTITY_ABBR,A.PAN_NO,TO_CHAR(A.PAN_ISSUE_DT,'DD/MM/YYYY'),A.NOTES,"
					+ "A.GST_TIN_NO,TO_CHAR(A.GST_TIN_DT, 'DD/MM/YYYY'),A.CST_TIN_NO,TO_CHAR(A.CST_TIN_DT, 'DD/MM/YYYY'),A.TAN_NO,TO_CHAR(A.TAN_ISSUE_DT, 'DD/MM/YYYY'),"
					+ "A.GSTIN_NO,TO_CHAR(A.GSTIN_DT, 'DD/MM/YYYY'),A.WEB_ADDR,A.PAYEE_ACCOUNT_NO,A.IFSC,A.PAYEE_NM,A.ADDL_NO,TO_CHAR(A.ADDL_ISSUE_DT, 'DD/MM/YYYY'),A.BUSINESS_FLAG "
					+ "FROM FMS_OTH_ENTITY_MST A "
					+ "WHERE A.ENTITY_CD = ? AND A.ENTITY_TYPE = ? "
					+ "AND A.EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS_OTH_ENTITY_MST B WHERE A.ENTITY_CD = B.ENTITY_CD AND B.ENTITY_TYPE=A.ENTITY_TYPE) ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, vendor_cd);
			stmt.setString(2, entity_role);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				eff_dt = rset.getString(1)==null?"":rset.getString(1);
				name = rset.getString(2)==null?"":rset.getString(2);
				abbr = rset.getString(3)==null?"":rset.getString(3);
				pan_no = rset.getString(4)==null?"":rset.getString(4);
				pan_dt = rset.getString(5)==null?"":rset.getString(5);
				notes = rset.getString(6)==null?"":rset.getString(6);
				gst_tin_no = rset.getString(7)==null? "":rset.getString(7);
				gst_tin_dt = rset.getString(8)==null? "":rset.getString(8);
				cst_tin_no = rset.getString(9)==null? "":rset.getString(9);
				cst_tin_dt = rset.getString(10)==null? "":rset.getString(10);
				tan_no = rset.getString(11)==null? "":rset.getString(11);
				tan_issue_dt = rset.getString(12)==null? "":rset.getString(12);
				gstin_no  = rset.getString(13)==null? "":rset.getString(13);
				gstin_dt = rset.getString(14)==null? "":rset.getString(14);
				web_addr = rset.getString(15)==null? "":rset.getString(15);
				payee_acc_no = rset.getString(16)==null? "":rset.getString(16);
				ifsc = rset.getString(17)==null? "":rset.getString(17);
				payee_nm = rset.getString(18)==null? "":rset.getString(18);
				service_no = rset.getString(19)==null?"":rset.getString(19);
				service_dt = rset.getString(20)==null?"":rset.getString(20);
				business_flag = rset.getString(21)==null?"":rset.getString(21);
				
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//Deep20251004
	public void getVendorAddressDetail()
	{
		String function_nm="getVendorAddressDetail()";
		
		try
		{
			int index = 0;
			
			for (int i = 0; i < 3; i++) 
			{
				reg_eff_dt[i] = "";
				address_flag[i] = "";
				address[i] = "";
				city[i] = "";
				pin[i] = "";
				state[i] = "";
				zone[i] = "0";
				country[i] = "0";
				phone[i] = "";
				mobile[i] = "";
				alt_phone[i] = "";
				fax1[i] = "";
				fax2[i] = "";
				email[i] = "";
			}
			
			String queryString="SELECT A.ADDRESS_TYPE,TO_CHAR(A.EFF_DT,'DD/MM/YYYY'),A.ADDR,A.CITY,A.PIN,A.STATE,A.ZONE,A.COUNTRY,A.PHONE,A.MOBILE,A.ALT_PHONE,"
					+ "A.FAX_1,A.FAX_2,A.EMAIL "
					+ "FROM FMS_OTH_ENTITY_ADDR_MST A "
					+ "WHERE A.ENTITY_CD=? AND A.ENTITY_TYPE = ? "
					+ "AND A.EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_OTH_ENTITY_ADDR_MST B WHERE A.ENTITY_CD=B.ENTITY_CD "
					+ "AND A.ADDRESS_TYPE=B.ADDRESS_TYPE AND A.ENTITY_TYPE = B.ENTITY_TYPE) ORDER BY A.ADDRESS_TYPE DESC";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, vendor_cd);
			stmt.setString(2, entity_role);
			rset=stmt.executeQuery();
			while (rset.next())
			{
				if (rset.getString(1).equals("C")) 
				{
					index = 1;
				}
				else if(rset.getString(1).equals("B")) 
				{
					index = 2;
				}
				address_flag[index] =  rset.getString(1)==null?"":rset.getString(1);
				reg_eff_dt[index] = rset.getString(2)==null?"":rset.getString(2);
				address[index] = rset.getString(3)==null?"":rset.getString(3);
				city[index] = rset.getString(4)==null?"":rset.getString(4);
				pin[index] = rset.getString(5)==null?"":rset.getString(5);
				state[index] = rset.getString(6)==null?"0":rset.getString(6);
				zone[index] = rset.getString(7)==null?"0":rset.getString(7);
				country[index] = rset.getString(8)==null?"0":rset.getString(8);
				phone[index] = rset.getString(9)==null?"":rset.getString(9);
				mobile[index] = rset.getString(10)==null?"":rset.getString(10);
				alt_phone[index] = rset.getString(11)==null?"":rset.getString(11);
				fax1[index] = rset.getString(12)==null?"":rset.getString(12);
				fax2[index] = rset.getString(13)==null?"":rset.getString(13);
				email[index] = rset.getString(14)==null?"":rset.getString(14);
				
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getInvoiceType()
	{
		String function_nm="getInvoiceType()";

		try
		{
			VINVOICE_LIST_ABBR.add("RCM Invoice(Cost Recharge)");
			VINVOICE_LIST_ABBR.add("Cost Recharge HPPL");
			VINVOICE_LIST_ABBR.add("Berthing Invoice (HPPL Shipping Agent)");
			VINVOICE_LIST_ABBR.add("PFA Fees Invoice (HPPL-SEIPL)");
			VINVOICE_LIST_ABBR.add("Scrap Fixed Asset");
			VINVOICE_LIST_ABBR.add("NPR");
			VINVOICE_LIST_ABBR.add("AHPL Invoice (AHPL Revenue Share)");
			VINVOICE_LIST_ABBR.add("GNA Invoice");
            VINVOICE_LIST_ABBR.add("ReExport Invoice");
				    				
			VINVOICE_LIST_NAME.add("frm_oth_inv_cost_recharge.jsp");
			VINVOICE_LIST_NAME.add("frm_other_invoice_cost_recharge_hppl.jsp");
			VINVOICE_LIST_NAME.add("frm_oth_inv_shipping_agent.jsp");
			VINVOICE_LIST_NAME.add("frm_oth_inv_hppl_seipl.jsp");
			VINVOICE_LIST_NAME.add("frm_oth_inv_scrap_fixed_asset.jsp");
			VINVOICE_LIST_NAME.add("frm_oth_inv_npr.jsp");
			VINVOICE_LIST_NAME.add("frm_oth_inv_ahpl_share.jsp");
			VINVOICE_LIST_NAME.add("frm_oth_inv_ga_inv.jsp");
            VINVOICE_LIST_NAME.add("frm_oth_inv_re_export.jsp");
			
			VINVOICE_TYPE.add("COSTR");
			VINVOICE_TYPE.add("COSTRH");
			VINVOICE_TYPE.add("HSA");
			VINVOICE_TYPE.add("HS");
			VINVOICE_TYPE.add("SFA");
			VINVOICE_TYPE.add("NPR");
			VINVOICE_TYPE.add("AHPL");
			VINVOICE_TYPE.add("GA");
            VINVOICE_TYPE.add("RXP");
			
			VCHECK_APPROVE.add("rpt_costr_chk_aprv_dtl.jsp");
			VCHECK_APPROVE.add("rpt_costr_hppl_chk_aprv_dtl.jsp");
			VCHECK_APPROVE.add("rpt_hsa_chk_aprv_dtl.jsp");
			VCHECK_APPROVE.add("rpt_hppl_seipl_chk_aprv_dtl.jsp");
			VCHECK_APPROVE.add("rpt_spa_chk_aprv_dtl.jsp");
			VCHECK_APPROVE.add("rpt_npr_chk_aprv_dtl.jsp");
			VCHECK_APPROVE.add("rpt_ahpl_share_chk_aprv_dtl.jsp");
			VCHECK_APPROVE.add("rpt_ga_inv_chk_aprv_dtl.jsp");
            VCHECK_APPROVE.add("rpt_re_export_chk_aprv_dtl.jsp");
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getOtherInvoicesList() 
	{
		String function_nm="getOtherInvoicesList()";
		try 
		{
			for (int i = 0; i < VINVOICE_TYPE.size(); i++ ) 
			{
				int index = 0;
				entity_role="V";
				
				if(VINVOICE_TYPE.elementAt(i).equals("HS") || VINVOICE_TYPE.elementAt(i).equals("COSTRH"))
				{
					entity_role="S";
				}
					
				queryString = "SELECT B.ENTITY_NAME, A.INVOICE_NO, TO_CHAR(A.INVOICE_DT, 'DD/MM/YYYY'), A.INVOICE_TYPE, A.VENDOR_CD, A.PDF_INV_DTL, A.CHECKED_FLAG, "
						+ "A.APPROVED_FLAG, A.PRINT_BY_ORI, A.PRINT_BY_DUP, A.INVOICE_SEQ, A.FINANCIAL_YEAR, A.SUPPLIER_CD, A.PRINT_BY_TRI, A.BU_UNIT, A.SAP_APPROVAL "
						+ "FROM FMS_OTH_INVOICE_MST A, FMS_OTH_ENTITY_MST B "
						+ " WHERE A.VENDOR_CD = B.ENTITY_CD AND TO_CHAR(A.INVOICE_DT, 'MM/YYYY') = ? AND A.COMPANY_CD = ? AND "
						+ " B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS_OTH_ENTITY_MST C WHERE A.VENDOR_CD = C.ENTITY_CD AND B.ENTITY_TYPE=C.ENTITY_TYPE) "
						+ "AND A.INVOICE_TYPE = ? AND B.ENTITY_TYPE = ? AND A.INV_FLAG = ? "
						+ "ORDER BY INVOICE_SEQ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, month+"/"+year);
				stmt.setString(2, comp_cd);
				stmt.setString(3, ""+VINVOICE_TYPE.elementAt(i));
				stmt.setString(4, entity_role);
				stmt.setString(5, "F");
				rset = stmt.executeQuery();
				while (rset.next()) 
				{
					index++;	
					VVENDOR_NAME.add(rset.getString(1) == null ? "" : rset.getString(1));
					VINV_NO.add(rset.getString(2) == null ? "" : rset.getString(2));
					VINVOICE_DT.add(rset.getString(3) == null ? "" : rset.getString(3));
					VINV_TYPE.add(rset.getString(4) == null ? "" : rset.getString(4));
					VENDORCD.add(rset.getString(5) == null ? "" : rset.getString(5));
					VCHECK_FLAG.add(rset.getString(7) == null ? "" : rset.getString(7));
					VAPPROVE_FLAG.add(rset.getString(8) == null ? "" : rset.getString(8));
					VBU_SEQ.add(rset.getString(15) == null ? "" : rset.getString(15));
					String pdf_ori = rset.getString(9) == null ? "" : rset.getString(9);
					String pdf_dup = rset.getString(10) == null ? "" : rset.getString(10);
					String pdf_tri = rset.getString(14) == null ? "" : rset.getString(14);
					
					String invoice_no = rset.getString(2) == null ? "" : rset.getString(2);
					String invoice_type = rset.getString(4) == null ? "" : rset.getString(4);
					String invoice_seq = rset.getString(11) == null ? "" : rset.getString(11);
					String fiscal_yr = rset.getString(12) == null ? "" : rset.getString(12); 
					String supplier_cd = rset.getString(13) == null ? "" : rset.getString(13); 
					String sun_appr = rset.getString(16) == null ? "" : rset.getString(16); 
					
					VSUPPLIER_CD.add(supplier_cd);
					VINVOICE_SEQ.add(invoice_seq);
					VFIN_YEAR.add(fiscal_yr);
					VSUN_APPROVAL_FLAG.add(sun_appr);
					
					String queryString1="SELECT A.ENTITY_NAME "
							+ "FROM FMS_OTH_ENTITY_MST A "
							+ "WHERE A.ENTITY_CD=? AND A.ENTITY_TYPE = 'S' "
							+ "AND A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS_OTH_ENTITY_MST C WHERE A.ENTITY_CD = C.ENTITY_CD AND A.ENTITY_TYPE=C.ENTITY_TYPE) ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, supplier_cd);
					rset1 = stmt1.executeQuery();
					if (rset1.next()) 
					{
						VSUPPLIER_NM.add(rset1.getString(1) == null ? "" : rset1.getString(1));
					}
					rset1.close();
					stmt1.close();
					
					if(print_pdf_type.equals("O") && !pdf_ori.equals(""))
					{
						VPDF_TYPE.add(print_pdf_type);
					}
					else if(print_pdf_type.equals("D") && !pdf_dup.equals(""))
					{
						VPDF_TYPE.add(print_pdf_type);
					}
					else if(print_pdf_type.equals("T") && !pdf_tri.equals(""))
					{
						VPDF_TYPE.add(print_pdf_type);
					}
					else if(print_pdf_type.equals("All"))
					{
						String allPdfType="";
						allPdfType+=pdf_ori.equals("")?allPdfType.equals("")?"O":",O":"";
						allPdfType+=pdf_dup.equals("")?allPdfType.equals("")?"D":",D":"";
						allPdfType+=pdf_tri.equals("")?allPdfType.equals("")?"T":",T":"";
						
						if(allPdfType.equals(""))
						{
							allPdfType="All";
						}
						VPDF_TYPE.add(allPdfType);
					}
					else
					{
						VPDF_TYPE.add("");
					}
					
					if(view_pdf_type.equals("All"))
					{
						int cnt=0;
						queryString6="SELECT COUNT(*) "
								+ "FROM FMS_OTH_INV_FILE_DTL "
								+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
								+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? "
			 	        		+ "AND PDF_TYPE!=? ";
						stmt6=conn.prepareStatement(queryString6);
						stmt6.setString(++cnt, comp_cd);
						stmt6.setString(++cnt, invoice_seq);
						stmt6.setString(++cnt, fiscal_yr);
						stmt6.setString(++cnt, invoice_type);
						stmt6.setString(++cnt, supplier_cd);
						stmt6.setString(++cnt, "X");
						rset6=stmt6.executeQuery();
						if(rset6.next())
						{
							if(rset6.getInt(1)>0)
							{
								VPDF_FILE_NAME.add("All");
								VPDF_FILE_PATH.add("");
							}
							else
							{
								VPDF_FILE_NAME.add("");
								VPDF_FILE_PATH.add("");
							}
						}
						else
						{
							VPDF_FILE_NAME.add("");
							VPDF_FILE_PATH.add("");
						}
						rset6.close();
						stmt6.close();
					}
					else
					{
						queryString6="SELECT FILE_NAME,PDF_SIGNED "
								+ "FROM FMS_OTH_INV_FILE_DTL "
								+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? "
			 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND SUPPLIER_CD = ? "
			 	        		+ "AND PDF_TYPE=?";
						stmt6=conn.prepareStatement(queryString6);
						stmt6.setString(1, comp_cd);
						stmt6.setString(2, invoice_type);
						stmt6.setString(3, invoice_seq);
						stmt6.setString(4, fiscal_yr);
						stmt6.setString(5, supplier_cd);
						stmt6.setString(6, view_pdf_type);
						rset6=stmt6.executeQuery();
						if(rset6.next())
						{
							String pdf_signed=rset6.getString(2)==null?"":rset6.getString(2);
							if(pdf_signed.equals("Y"))
							{
								VPDF_FILE_NAME.add(rset6.getString(1)==null?"":rset6.getString(1));
								VPDF_FILE_PATH.add(CommonVariable.signed_ig_inv_path);
							}
							else
							{
								VPDF_FILE_NAME.add(rset6.getString(1)==null?"":rset6.getString(1));
								VPDF_FILE_PATH.add(CommonVariable.ig_inv_path);
							}
						}
						else
						{
							VPDF_FILE_NAME.add("");
							VPDF_FILE_PATH.add("");
						}
						rset6.close();
						stmt6.close();
					}
					
					if(sun_appr.equals("Y"))
					{
						queryString5="SELECT FILE_NAME "
								+ "FROM FMS_OTH_INV_FILE_DTL "
								+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? "
			 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND SUPPLIER_CD = ? "
			 	        		+ "AND PDF_TYPE=?";
						stmt5=conn.prepareStatement(queryString5);
						stmt5.setString(1, comp_cd);
						stmt5.setString(2, invoice_type);
						stmt5.setString(3, invoice_seq);
						stmt5.setString(4, fiscal_yr);
						stmt5.setString(5, supplier_cd);
						stmt5.setString(6, "S");
						rset5=stmt5.executeQuery();
						if(rset5.next())
						{
							VXML_FILE_NAME.add(rset5.getString(1)==null?"":rset5.getString(1));
							VXML_FILE_PATH.add(CommonVariable.sun_xml);
						}
						else 
						{
							VXML_FILE_NAME.add("");
							VXML_FILE_PATH.add("");
						}
						rset5.close();
						stmt5.close();
					}
					else 
					{
						VXML_FILE_NAME.add("");
						VXML_FILE_PATH.add("");
					}
					
					
					String irn_no="";
					queryString5="SELECT IRN_NO "
							+ "FROM FMS_OTH_INV_IRN_DTL "
							+ "WHERE COMPANY_CD=? AND INVOICE_NO=? ";
					stmt5=conn.prepareStatement(queryString5);
					stmt5.setString(1, comp_cd);
					stmt5.setString(2, invoice_no);
					rset5=stmt5.executeQuery();
					if(rset5.next())
					{
						irn_no=rset5.getString(1)==null?"":rset5.getString(1);
					}
					rset5.close();
					stmt5.close();
					
					VIS_IRN_GENERATED.add(irn_no.equals("")?"N":"Y");
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
	
	//Deep20251014
	public void getSupplierList()
	{	
		String function_nm="getSupplierList()";

		try
		{
			String queryString="SELECT ENTITY_CD,ENTITY_NAME,ENTITY_ABBR  "
					+ "FROM FMS_OTH_ENTITY_MST "
					+ "WHERE ENTITY_TYPE='S' ";
			if(callFlag.equals("OTHER_INVOICE_COST_RECHARGE_HPPL"))
			{
				queryString+="AND ENTITY_ABBR='SEIPL' ";
			}
			if(callFlag.equals("OTH_INV_SHIPPING_AGENT") || callFlag.equals("OTH_INV_HPPL_SEIPL") || callFlag.equals("OTH_INV_AHPL_SHARE"))
			{
				queryString+="AND ENTITY_ABBR='HPPL' ";
			}
			if(opration.equals("INSERT"))
			{
				queryString+="AND ACTIVE_FLAG='Y'";
			}
			stmt=conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				if(callFlag.equals("OTHER_INVOICE_COST_RECHARGE_HPPL") || callFlag.equals("OTH_INV_SHIPPING_AGENT") || callFlag.equals("OTH_INV_HPPL_SEIPL") || callFlag.equals("OTH_INV_AHPL_SHARE"))
				{
					supp_cd=rset.getString(1)==null?"":rset.getString(1);
				}
				VSUPPLIER_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VSUPPLIER_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VSUPPLIER_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
			}
			rset.close();
			stmt.close();
			
			if(callFlag.equals("OTH_INV_HPPL_SEIPL") || callFlag.equals("OTHER_INVOICE_COST_RECHARGE_HPPL"))
			{
				queryString1="SELECT ENTITY_CD,ENTITY_NAME,ENTITY_ABBR  "
					+ "FROM FMS_OTH_ENTITY_MST "
					+ "WHERE ENTITY_TYPE='S' ";
				if(callFlag.equals("OTHER_INVOICE_COST_RECHARGE_HPPL"))
				{
					queryString1+= "AND ENTITY_ABBR = 'HPPL'";
				}
				else if(opration.equals("MODIFY"))
				{
					queryString1+= "AND ENTITY_ABBR IN ('SEIPL','HLPL')";
				}
				else 
				{
					queryString1+= "AND ENTITY_ABBR = 'SEIPL'";
				}
				stmt=conn.prepareStatement(queryString1);
				rset=stmt.executeQuery();
				while(rset.next())
				{
					VVENDOR_CD.add(rset.getString(1)==null?"":rset.getString(1));
					VENDOR_NM.add(rset.getString(2)==null?"":rset.getString(2));
					VENDOR_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
					vendor_cd=rset.getString(1)==null?"":rset.getString(1);
				}
			}
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSupplierDtl()
	{	
		String function_nm="getSupplierDtl()";
 
		try
		{
			String queryString="SELECT A.ADDR,A.CITY,B.PAN_NO,A.STATE,B.ENTITY_ABBR,B.GSTIN_NO,A.PIN,B.ENTITY_NAME "
					+ "FROM FMS_OTH_ENTITY_ADDR_MST A, FMS_OTH_ENTITY_MST B "
					+ "WHERE B.ENTITY_CD=? AND B.ENTITY_TYPE = ? AND A.ADDRESS_TYPE='R' AND A.ENTITY_CD = B.ENTITY_CD AND A.ENTITY_TYPE = B.ENTITY_TYPE "
					+ "AND A.EFF_DT=(SELECT MAX(C.EFF_DT) FROM FMS_OTH_ENTITY_ADDR_MST C WHERE A.ENTITY_CD=C.ENTITY_CD AND A.ENTITY_TYPE = C.ENTITY_TYPE)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, supp_cd);
			stmt.setString(2, "S");
			rset=stmt.executeQuery();
			if(rset.next())
			{
				supp_addr = rset.getString(1)==null?"":rset.getString(1);
				supp_city = rset.getString(2)==null?"":rset.getString(2);
				supp_pan_no = rset.getString(3)==null?"":rset.getString(3);
				supp_state =  rset.getString(4)==null?"":rset.getString(4);
				supp_abbr =  rset.getString(5)==null?"":rset.getString(5);
				supp_gstin_no = rset.getString(6)==null?"":rset.getString(6);
				supp_pin = rset.getString(7)==null?"":rset.getString(7);
				supp_nm = rset.getString(8)==null?"":rset.getString(8);
				
				String query = "SELECT TIN "
						+ "FROM FMS_STATE_MST "
						+ "WHERE STATE_NM=? "
						+ "ORDER BY STATE_NM";
				stmt1 = conn.prepareStatement(query);
				stmt1.setString(1, supp_state);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					supp_state_tin= rset1.getString(1)==null?"":rset1.getString(1);
				}
				stmt1.close();
				rset1.close();
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getVendorDtl()
	{	
		String function_nm="getVendorDtl()";
		try
		{
			String queryString="SELECT PAN_NO,GSTIN_NO,ENTITY_NAME,ENTITY_ABBR,ENTITY_CD,ENTITY_TYPE "
					+ "FROM FMS_OTH_ENTITY_MST  "
					+ "WHERE ENTITY_CD=? AND ENTITY_TYPE = ? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, vendor_cd); 
			if(callFlag.equals("OTH_INV_HPPL_SEIPL") || callFlag.equals("HPPL_SEIPL_APPROVAL") 
			|| callFlag.equals("OTHER_INVOICE_COST_RECHARGE_HPPL") || callFlag.equals("COST_RECHARGE_HPPL_APROVAL") 
			|| callFlag.equals("PFA_CRDR_LIST") || callFlag.equals("PFA_CRDR_APROVAL") 
			|| callFlag.equals("COSTRH_CRDR_LIST") || callFlag.equals("COSTRH_CRDR_APROVAL"))
			{
				stmt.setString(2, "S");
			}
			else 
			{
				stmt.setString(2, "V");
			}
			rset=stmt.executeQuery();
			if(rset.next())
			{
				vend_pan_no = rset.getString(1)==null?"":rset.getString(1);
				vend_gstin_no= rset.getString(2)==null?"":rset.getString(2);
				vendor_name=rset.getString(3)==null?"":rset.getString(3);
				abbr=rset.getString(4)==null?"":rset.getString(4);
				String cd=rset.getString(5)==null?"":rset.getString(5);
				String type=rset.getString(6)==null?"":rset.getString(6);
				
				String query = "SELECT ADDR,CITY,PIN,COUNTRY,STATE "
						+ "FROM FMS_OTH_ENTITY_ADDR_MST "
						+ "WHERE ENTITY_CD = ? AND ENTITY_TYPE = ? AND ADDRESS_TYPE='R'";
				stmt1 = conn.prepareStatement(query);
				stmt1.setString(1,cd);
				stmt1.setString(2,type);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					vend_abbr = rset1.getString(1)==null?"":rset1.getString(1);
					vend_city = rset1.getString(2)==null?"":rset1.getString(2);
					vend_pin_no= rset1.getString(3)==null?"":rset1.getString(3);
					vend_country=rset1.getString(4)==null?"":rset1.getString(4);
					vend_state=rset1.getString(5)==null?"":rset1.getString(5);
				}
				stmt1.close();
				rset1.close();
				
				query = "SELECT TIN "
						+ "FROM FMS_STATE_MST "
						+ "WHERE STATE_NM=? "
						+ "ORDER BY STATE_NM";
				stmt1 = conn.prepareStatement(query);
				stmt1.setString(1, vend_state);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					vend_state_tin= rset1.getString(1)==null?"":rset1.getString(1);
				}
				stmt1.close();
				rset1.close();
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSAC_List()
	{	
		String function_nm="getSAC_List()";
		try
		{
			String queryString="SELECT  SAC_CD ,SAC_DESC,SAC_CODE  "
					+ "FROM FMS_SAC_MST  ";
			stmt=conn.prepareStatement(queryString);  
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VSAC_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VSAC_DESC.add(rset.getString(2)==null?"":rset.getString(2));
				VSAC_CODE.add(rset.getString(3)==null?"":rset.getString(3));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getTaxStructureList()
	{	
		String function_nm="getTaxStructureList()";
		try
		{
			vend_state=vend_state.toUpperCase();
			supp_state=supp_state.toUpperCase();
			String tax_type="";
			if(!vend_state.equals("") && !supp_state.equals("") && vend_state.equals(supp_state)) // CGST, SGST 
			{
				tax_type="CGST";
			}
			else //IGST
			{	
				tax_type="IGST";
			}
			queryString="SELECT TAX_STR_CD,DESCR,STATUS,REMARK,TAX_CATEGORY,TO_CHAR(APP_DATE,'DD/MM/YYYY'),"
					+ "SAP_TAX_CODE,SAP_GL,PAY_RECV "
					+ "FROM FMS_TAX_STRUCTURE A "
					+ "WHERE TAX_CATEGORY=? AND (DESCR LIKE ?) "
					+ "AND APP_DATE=(SELECT MAX(B.APP_DATE) FROM FMS_TAX_STRUCTURE B "
					+ "WHERE A.TAX_STR_CD=B.TAX_STR_CD AND B.APP_DATE<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) ";
			queryString+="AND PAY_RECV=? ";
			queryString+="ORDER BY TAX_STR_CD";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, tax_category);
			stmt.setString(2, tax_type+"%");
			stmt.setString(3, "R");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				
				String tax_struct_cd=rset.getString(1)==null?"":rset.getString(1);

				VTAX_STRUCT_CD.add(tax_struct_cd);
				VTAX_STRUCT_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VTAX_STRUCT_STATUS.add(rset.getString(3)==null?"":rset.getString(3));
				VTAX_STRUCT_RMK.add(rset.getString(4)==null?"":rset.getString(4));
				String tax_category=rset.getString(5)==null?"P":rset.getString(5);
				VTAX_CATEGORY.add(tax_category);
				String tax_category_nm="";
				if(tax_category.equals("P"))
				{
					tax_category_nm="Product";
				}
				else if(tax_category.equals("S"))
				{
					tax_category_nm="Service";
				}
				VTAX_CATEGORY_NM.add(tax_category_nm);
				VTAX_STRUCT_APP_DT.add(rset.getString(6)==null?"":rset.getString(6));
				VSAP_TAX_CODE.add(rset.getString(7)==null?"":rset.getString(7));
				VSAP_GL.add(rset.getString(8)==null?"":rset.getString(8));
				String pay_recv=rset.getString(9)==null?"":rset.getString(9);
				VPAY_RECV.add(pay_recv);
				String pay_recv_nm="";
				if(pay_recv.equals("P"))
				{
					pay_recv_nm="Payable";
				}
				else if(pay_recv.equals("R"))
				{
					pay_recv_nm="Receivable";
				}
				VPAY_RECV_NM.add(pay_recv_nm);
			}
			rset.close();
			stmt.close();
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getInvoiceNo()
	{	
		String function_nm="getInvoiceNo()";

		try
		{
//			if (Integer.parseInt(month) < 4) 
//			{
//				year = (Integer.parseInt(year)-1) + "-" + year;
//			}
//			else 
//			{
//				year = year + "-" + (Integer.parseInt(year) + 1);
//			}
			
			year = fin_yr;
			
			if(!inv_id_seq.equals(""))
			{
				VINVOICE_ID_SEQ.add(inv_id_seq);
				VINVOICE_NO.add(inv_no);
			}
			
			String state_code="";
			if(inv_type.equals("GA"))
			{
				String query = "SELECT STATE_CODE "
						+ "FROM FMS_STATE_MST "
						+ "WHERE TIN=? "
						+ "ORDER BY STATE_NM";
				stmt = conn.prepareStatement(query);
				stmt.setString(1, supp_state_tin);
				rset = stmt.executeQuery();
				if(rset.next())
				{
					state_code= rset.getString(1)==null?"":rset.getString(1);
				}
				stmt.close();
				rset.close();
			}
			
			int no_inv_no=1;
			for(int i=1;i<=no_inv_no;i++)
			{
				String inv_id_seq=""+i;
				if(callFlag.equalsIgnoreCase("HSA_CRDR_APROVAL") || callFlag.equalsIgnoreCase("AHPL_CRDR_APROVAL") || callFlag.equalsIgnoreCase("PFA_CRDR_APROVAL") || callFlag.equalsIgnoreCase("COSTRH_CRDR_APROVAL") 
						|| callFlag.equalsIgnoreCase("COSTR_CRDR_APROVAL") || callFlag.equalsIgnoreCase("SFA_CRDR_APROVAL"))
				{
					String cr_dr = "",cr_dr1="";
					int count=0;
					int cnt=0;
				
					queryString1 = "SELECT NVL(MAX(INVOICE_ID_SEQ)+1, 1) "
							+ "FROM FMS_OTH_INVOICE_MST "
							+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? ";
					
					if(inv_type.equals("HSA"))
					{
						queryString1+="AND INVOICE_TYPE IN ('HSA') ";
					}
					else if(supp_abbr.equals("HPPL") && (inv_type.equals("SFA") || inv_type.equals("NPR") || inv_type.equals("HS")))
					{
						queryString1+="AND INVOICE_TYPE IN ('SFA','NPR','HS') ";
					}
					else if(supp_abbr.equals("SEIPL") && (inv_type.equals("SFA") || inv_type.equals("NPR") || inv_type.equals("COSTRH") || (inv_type.equals("GA") && state_code.equals("GJ"))))
					{
						queryString1+="AND INVOICE_TYPE IN ('SFA','NPR','COSTRH','GA') ";
						if(inv_type.equals("GA"))
						{
							queryString1+=" AND (BU_STATE_TIN=? OR BU_STATE_TIN IS NULL) ";
						}
					}
					else if(inv_type.equals("GA") && !state_code.equals("GJ"))
					{
						queryString1+="AND INVOICE_TYPE = 'GA' AND BU_UNIT=? ";
					}
					else
					{
						queryString1+="AND INVOICE_TYPE = ? ";
					}
					queryString1+= "AND SUPPLIER_CD = ? AND INV_FLAG=?";
					
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, year);
					if((supp_abbr.equals("HPPL") || supp_abbr.equals("SEIPL")) && (inv_type.equals("SFA") || inv_type.equals("NPR") || inv_type.equals("HSA") || inv_type.equals("HS") || inv_type.equals("COSTRH") || (inv_type.equals("GA") && state_code.equals("GJ"))))
					{
						if(inv_type.equals("GA"))
						{
							stmt1.setString(++cnt, supp_state_tin);
						}
					}
					else if(inv_type.equals("GA") && !state_code.equals("GJ"))
					{
						stmt1.setString(++cnt, bu_unit);
					}
					else
					{
						stmt1.setString(++cnt, inv_type);
					}
					stmt1.setString(++cnt, supp_cd);
					stmt1.setString(++cnt, cr_dr_type);
					rset1 = stmt1.executeQuery();
					if (rset1.next()) 
					{
						inv_id_seq=rset1.getString(1)==null?"":rset1.getString(1);
					}
					rset1.close();
					stmt1.close();
				
					{
						if(cr_dr_type.equals("CR"))
						{
							cr_dr = "C";
							cr_dr1 = "CR";
							
						}
						else
						{
							cr_dr ="D";
							cr_dr1 ="DR";
						}
						
						String invoice_no="";
						if(year!=null && !year.equals(""))
						{
							if(inv_type.equals("AHPL"))
							{
								invoice_no = "000".substring(inv_id_seq.length()) + inv_id_seq + "/" + year.split("-")[0].substring(2) + "-" + year.split("-")[1].substring(2);
							}
							else 
							{
								invoice_no = "0000".substring(inv_id_seq.length()) + inv_id_seq + "/" + year.split("-")[0].substring(2) + "-" + year.split("-")[1].substring(2);
							}
						}
			
						if (inv_type.equals("COSTR")) 
						{
							invoice_no = supp_abbr.equals("HPPL") ? (cr_dr1+"RCP"+invoice_no) : (cr_dr1+"RCL"+invoice_no);
						}
						else if (inv_type.equals("HSA")) 
						{
							invoice_no = cr_dr+invoice_no;;
						}
						else if (inv_type.equals("COSTRH") || inv_type.equals("SFA") || inv_type.equals("HS") || inv_type.equals("NPR")) 
						{
							invoice_no = supp_abbr.equals("HPPL") ? (cr_dr1+"P"+invoice_no) : (cr_dr1+"F"+invoice_no);
						}
						else if(inv_type.equals("GA") && state_code.equals("GJ"))
						{
							invoice_no =cr_dr1+"F"+invoice_no;
						}
						else if(inv_type.equals("GA") && !state_code.equals("Gujarat"))
						{
							invoice_no =cr_dr1+state_code+"F"+invoice_no;
						}
						else 
						{
							invoice_no = cr_dr_type.equals("CR") ? ("CRAH"+invoice_no) : ("DRAH"+invoice_no);
						}
						VINVOICE_ID_SEQ.add(inv_id_seq);
						VINVOICE_NO.add(invoice_no);
					}
				}
				else
				{
					int count=0;
					int cnt=0;
				
					queryString1 = "SELECT NVL(MAX(INVOICE_ID_SEQ)+1, 1) "
								+ "FROM FMS_OTH_INVOICE_MST "
								+ "WHERE COMPANY_CD = ? AND FINANCIAL_YEAR = ? ";
					if(supp_abbr.equals("HPPL") && (inv_type.equals("SFA") || inv_type.equals("NPR") || inv_type.equals("HSA") || inv_type.equals("HS")))
					{
						queryString1+="AND INVOICE_TYPE IN ('SFA','NPR','HSA','HS') ";
					}
					else if(supp_abbr.equals("SEIPL") && (inv_type.equals("SFA") || inv_type.equals("NPR") || inv_type.equals("COSTRH") || (inv_type.equals("GA") && state_code.equals("GJ"))))
					{
						queryString1+="AND INVOICE_TYPE IN ('SFA','NPR','COSTRH','GA') ";
						if(inv_type.equals("GA"))
						{
							queryString1+=" AND (BU_STATE_TIN=? OR BU_STATE_TIN IS NULL) ";
						}
					}
					else if(inv_type.equals("GA") && !state_code.equals("GJ"))
					{
						queryString1+="AND INVOICE_TYPE = 'GA' AND BU_UNIT=? ";
					}
					else
					{
						queryString1+="AND INVOICE_TYPE = ? ";
					}
					queryString1+= "AND SUPPLIER_CD = ? AND INV_FLAG='F'";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, year);
					if((supp_abbr.equals("HPPL") || supp_abbr.equals("SEIPL")) && (inv_type.equals("SFA") || inv_type.equals("NPR") || inv_type.equals("HSA") || inv_type.equals("HS") || inv_type.equals("COSTRH") || (inv_type.equals("GA") && state_code.equals("GJ"))))
					{
						if(inv_type.equals("GA"))
						{
							stmt1.setString(++cnt, supp_state_tin);
						}
					}
					else if(inv_type.equals("GA") && !state_code.equals("GJ"))
					{
						stmt1.setString(++cnt, bu_unit);
					}
					else
					{
						stmt1.setString(++cnt, inv_type);
					}
					stmt1.setString(++cnt, supp_cd);
					rset1 = stmt1.executeQuery();
					if (rset1.next()) 
					{
						inv_id_seq=rset1.getString(1)==null?"":rset1.getString(1);
					
				
						{
							String invoice_no="";
							
							if(year!=null && !year.equals(""))
							{
								if(inv_type.equals("AHPL"))
								{
									invoice_no = "000".substring(inv_id_seq.length()) + inv_id_seq + "/" + year.split("-")[0].substring(2) + "-" + year.split("-")[1].substring(2);
								}
								else 
								{
									invoice_no = "0000".substring(inv_id_seq.length()) + inv_id_seq + "/" + year.split("-")[0].substring(2) + "-" + year.split("-")[1].substring(2);
								}
								
							}
				
							if (inv_type.equals("COSTR")) 
							{
								invoice_no = supp_abbr.equals("HPPL") ? ("RCP"+invoice_no) : ("RCL"+invoice_no);
							}
							else if (inv_type.equals("COSTRH") || inv_type.equals("SFA") || inv_type.equals("HSA") || inv_type.equals("HS") || inv_type.equals("NPR")) 
							{
								invoice_no = supp_abbr.equals("HPPL") ? ("P"+invoice_no) : ("F"+invoice_no);
							}
							else if(inv_type.equals("GA") && state_code.equals("GJ"))
							{
								invoice_no ="F"+invoice_no;
							}
							else if(inv_type.equals("GA") && !state_code.equals("Gujarat"))
							{
								invoice_no =state_code+"F"+invoice_no;
							}
							else if(inv_type.equals("RXP"))
							{
								invoice_no ="RXP"+invoice_no;
							}
							else 
							{
								invoice_no = "AH"+invoice_no;
							}
							
							VINVOICE_ID_SEQ.add(inv_id_seq);
							VINVOICE_NO.add(invoice_no);
						}
					
					}
					rset1.close();
					stmt1.close();
				}
			}
			
			if(inv_id_seq.equals(""))
			{
				if(VINVOICE_ID_SEQ.size()>0)
				{
					inv_id_seq=""+VINVOICE_ID_SEQ.elementAt(0);
				}
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getInvoiceDtls()
	{	
		String function_nm="getInvoiceDtls()";

		try
		{
			double taxValue = 0;
			String tax_value="";
			queryString = "SELECT A.COMPANY_CD,A.VENDOR_CD,A.SUPPLIER_CD,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),B.VENDOR_SUPP_INV_REF,"
					+ "B.PACER_NO,A.GROSS_AMT,A.TAX_AMT_INR,A.NET_PAYABLE,A.EXCHG_RATE_VALUE,TO_CHAR(A.EXCHG_RATE_DT, 'DD/MM/YYYY'),"
					+ "A.SALE_PRICE_UNIT,B.SAC_CODE,B.TAX_STRUCT_CD,B.ITEM_DESCRIPTION,A.REMARK,A.REMARK1,A.APPROVED_FLAG,A.FINANCIAL_YEAR,"
					+ "A.INVOICE_SEQ,A.SALE_AMT,A.CHECKED_FLAG,A.INVOICE_NO,A.INVOICE_ID_SEQ,TO_CHAR(B.TAX_EFF_DT, 'DD/MM/YYYY'),A.INVOICE_CATEGORY,A.INVOICE_RAISED_IN "
					+ "FROM FMS_OTH_INVOICE_MST A, FMS_OTH_INVOICE_DTL B "
					+ "WHERE A.COMPANY_CD = ? AND A.INVOICE_SEQ = ? AND A.INVOICE_TYPE = ? AND A.FINANCIAL_YEAR=? AND A.SUPPLIER_CD=? AND A.INV_FLAG = ? "
					+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.INVOICE_TYPE = B.INVOICE_TYPE "
					+ "AND A.VENDOR_CD = B.VENDOR_CD AND A.SUPPLIER_CD = B.SUPPLIER_CD AND A.INVOICE_SEQ = B.INVOICE_SEQ AND A.INV_FLAG = B.INV_FLAG";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_seq);
			stmt.setString(3, inv_type);
			stmt.setString(4, fin_yr);
			stmt.setString(5, supp_cd);
			stmt.setString(6, "F");
			rset = stmt.executeQuery();
			if(rset.next()) 
			{
				submission_chk = true;
				
				vendor_cd= rset.getString(2) == null ? "" : rset.getString(2);
				supp_cd= rset.getString(3) == null ? "" : rset.getString(3);				
				inv_dt = rset.getString(4) == null ? "" : rset.getString(4);
				ref_no = rset.getString(5) == null ? "" : rset.getString(5);
				pacer_no = rset.getString(6) == null ? "" : rset.getString(6);				
				gross_amt = nf.format(Double.parseDouble(rset.getString(7) == null ? "" : rset.getString(7)));
				total_gst = nf.format(Double.parseDouble(rset.getString(8) == null ? "" : rset.getString(8)));
				net_amt = nf.format(Double.parseDouble(rset.getString(9) == null ? "" : rset.getString(9)));
				exchng_rate = rset.getString(10) == null ? "" : rset.getString(10);
				exchng_eff_dt = rset.getString(11) == null ? "" : rset.getString(11);
				currency = rset.getString(12) == null ? "" : rset.getString(12);
				sac_cd = rset.getString(13) == null ? "" : rset.getString(13);
				tax_struct_cd = rset.getString(14) == null ? "" : rset.getString(14);
				tax_struct_dt = rset.getString(25) == null ? "" : rset.getString(25);
				desc_item = rset.getString(15) == null ? "" : rset.getString(15);
				remark1 = rset.getString(16) == null ? "" : rset.getString(16);
				remark2 = rset.getString(17) == null ? "" : rset.getString(17);
				
				if(opration.equals("CHECK"))
				{
					activity_value = rset.getString(22) == null ? "" : rset.getString(22);
				}
				else if(opration.equals("APPROVE"))
				{
					activity_value = rset.getString(18) == null ? "" : rset.getString(18);
				}
				fin_yr = rset.getString(19) == null ? "" : rset.getString(19);
				inv_seq = rset.getString(20) == null ? "" : rset.getString(20);
				sale_amt = nf.format(Double.parseDouble(rset.getString(21) == null ? "" : rset.getString(21)));
				inv_no = rset.getString(23) == null ? "" : rset.getString(23);
				inv_id_seq = rset.getString(24) == null ? "" : rset.getString(24);
				invoice_category = rset.getString(26) == null ? "" : rset.getString(26);
				invoice_raised_in = rset.getString(27) == null ? "" : rset.getString(27);
				
				taxValue = rset.getDouble(8);
				queryString2 = "SELECT SAC_CODE,SAC_DESC "
						+ "FROM FMS_SAC_MST A "
						+ "WHERE SAC_CD  = ?";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1,sac_cd);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					sac_code = rset2.getString(1)==null?"":rset2.getString(1);	
					sac_des = rset2.getString(2)==null?"":rset2.getString(2);	
				}
				rset2.close();
				stmt2.close();
				
				tax_struct_info=utilBean.getTaxDescr(conn,tax_struct_cd);
				
				if (tax_struct_info.contains(",")) 
				{
					tax_value = nf.format(Double.parseDouble((Double.doubleToRawLongBits(taxValue)==Double.doubleToRawLongBits(0)) ? "0" : String.format("%.2f", taxValue / 2)));
				} 
				else 
				{
					tax_value = nf.format(Double.parseDouble((Double.doubleToRawLongBits(taxValue)==Double.doubleToRawLongBits(0)) ? "0" : String.format("%.2f", taxValue)));
				}	
			
				tax_amt=nf.format(taxValue);
				
				
				//if(!opration.equals("MODIFY")) 
				{
					int srno=0;
					String currency_nm ="";
					if(currency.equals("1")) 
					{
						currency_nm = "INR";
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add(desc_item);
						VPDF_COL3.add(currency_nm);
						VPDF_COL4.add(es.formatAmount(sale_amt));
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("<b>Total Amount</b>");
						VPDF_COL3.add("<b>"+currency_nm+"</b>");
						VPDF_COL4.add("<b>"+es.formatAmount(gross_amt)+"</b>");
						
					}
					else if(currency.equals("2")) 
					{
						currency_nm = "USD";
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add(desc_item);
						VPDF_COL3.add(currency_nm);
						VPDF_COL4.add(es.formatAmount(sale_amt));
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("Exchange Rate : "+exchng_rate+" (INR/USD) dated "+exchng_eff_dt);
						VPDF_COL3.add("INR/USD");
						VPDF_COL4.add(exchng_rate);
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("<b>Net Amount</b>");
						VPDF_COL3.add("<b>INR</b>");
						VPDF_COL4.add("<b>"+es.formatAmount(gross_amt)+"</b>");
						
						
					}
					else if(currency.equals("3")) 
					{
						currency_nm = "GBP";
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add(desc_item);
						VPDF_COL3.add(currency_nm);
						VPDF_COL4.add(es.formatAmount(sale_amt));
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("Exchange Rate : "+exchng_rate+" (INR/GBP) dated "+exchng_eff_dt);
						VPDF_COL3.add("INR/GBP");
						VPDF_COL4.add(exchng_rate);
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("<b>Net Amount</b>");
						VPDF_COL3.add("<b>INR</b>");
						VPDF_COL4.add("<b>"+es.formatAmount(gross_amt)+"</b>");
						
					}
					else if(currency.equals("4")) 
					{
						currency_nm = "EURO";
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add(desc_item);
						VPDF_COL3.add(currency_nm);
						VPDF_COL4.add(es.formatAmount(sale_amt));
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("Exchange Rate : "+exchng_rate+" (INR/EURO) dated "+exchng_eff_dt);
						VPDF_COL3.add("INR/EURO");
						VPDF_COL4.add(exchng_rate);
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("<b>Net Amount</b>");
						VPDF_COL3.add("<b>INR</b>");
						VPDF_COL4.add("<b>"+es.formatAmount(gross_amt)+"</b>");

					}
					else if(currency.equals("5")) 
					{
						currency_nm = "YEN";
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add(desc_item);
						VPDF_COL3.add(currency_nm);
						VPDF_COL4.add(es.formatAmount(sale_amt));
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("Exchange Rate : "+exchng_rate+" (INR/YEN) dated "+exchng_eff_dt);
						VPDF_COL3.add("INR/YEN");
						VPDF_COL4.add(exchng_rate);
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("<b>Net Amount</b>");
						VPDF_COL3.add("<b>INR</b>");
						VPDF_COL4.add("<b>"+es.formatAmount(gross_amt)+"</b>");
						
					}

					srno+=1;
					double temp_srno=srno;
					Vector VTAX_CODE = new Vector();
					Vector VTAX_DESCR = new Vector();
					Vector VTAX_AMT = new Vector();
					if (tax_struct_info.contains(",")) 
					{
						BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_amt));
						BigDecimal factor = new BigDecimal(2);
						for(int i = 0;i<tax_struct_info.split(",").length;i++) 
						{
							BigDecimal sub_tax_amt = tax_amt1.divide(factor, 0, RoundingMode.HALF_UP);
							temp_srno=temp_srno+0.1;
							VPDF_COL1.add(nf0.format(temp_srno));
							VPDF_COL2.add(tax_struct_info.split(",")[i]);
							VPDF_COL3.add("INR");
							VPDF_COL4.add(es.formatAmount(""+Double.parseDouble(""+sub_tax_amt)));
							
							VTAX_CODE.add(tax_struct_cd);
							VTAX_DESCR.add(tax_struct_info.split(",")[i]);
							VTAX_AMT.add(nf.format(sub_tax_amt));
						}
						Vector VTEMP_TAX_DTL = new Vector();
						
						VTEMP_TAX_DTL.add(VTAX_CODE);
						VTEMP_TAX_DTL.add(VTAX_DESCR);
						VTEMP_TAX_DTL.add(VTAX_AMT);
						
						VMULTI_TAX_STRUCT.add(VTEMP_TAX_DTL);
					} 
					
					VPDF_COL1.add(srno);
					VPDF_COL2.add("<b>Tax ("+tax_struct_info+")</b>");
					VPDF_COL3.add("<b>INR</b>");
					VPDF_COL4.add("<b>"+es.formatAmount(tax_amt)+"</b>");
					
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("<b>Total Amount in INR</b>");
					VPDF_COL3.add("<b>INR</b>");
					VPDF_COL4.add("<b>"+es.formatAmount(net_amt)+"</b>");
				}
				
				if(!net_amt.equalsIgnoreCase(""))		
				{
					amt_in_word=es.convert(Double.parseDouble(net_amt));
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
	
	public void getInvoiceDtlHPPL()
	{	
		String function_nm="getInvoiceDtlHPPL()";
		try
		{
			double taxValue = 0;
			String tax_value = "";
			queryString = "SELECT A.COMPANY_CD, A.SUPPLIER_CD, A.VENDOR_CD, TO_CHAR(A.INVOICE_DT, 'DD/MM/YYYY'), B.SAC_CODE, A.SALE_PRICE_UNIT, "
					+ "A.EXCHG_RATE_VALUE, TO_CHAR(A.EXCHG_RATE_DT, 'DD/MM/YYYY'),"
					+ "A.GROSS_AMT, A.TAX_AMT_INR, B.PURCHASE_NO, B.REFERENCE_NO, B.TAX_STRUCT_CD, B.ITEM_DESCRIPTION, A.NET_PAYABLE,A.SALE_AMT,A.REMARK,A.REMARK1,"
					+ "A.APPROVED_FLAG,A.FINANCIAL_YEAR,A.INVOICE_SEQ,A.CHECKED_FLAG,A.INVOICE_NO,A.INVOICE_ID_SEQ,TO_CHAR(B.TAX_EFF_DT, 'DD/MM/YYYY'),A.INVOICE_CATEGORY,A.INVOICE_RAISED_IN  "
					+ "FROM FMS_OTH_INVOICE_MST A, FMS_OTH_INVOICE_DTL B "
					+ "WHERE A.COMPANY_CD = ? AND A.INVOICE_SEQ = ? AND A.INVOICE_TYPE = ? AND A.FINANCIAL_YEAR=? AND A.SUPPLIER_CD=? AND A.INV_FLAG = ? "
					+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.INVOICE_SEQ = B.INVOICE_SEQ AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.INVOICE_TYPE = B.INVOICE_TYPE AND "
					+ "A.INVOICE_DT = B.EFF_DT AND A.VENDOR_CD = B.VENDOR_CD AND A.SUPPLIER_CD = B.SUPPLIER_CD AND A.INV_FLAG = B.INV_FLAG";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_seq);
			stmt.setString(3, inv_type);
			stmt.setString(4, fin_yr);
			stmt.setString(5, supp_cd);
			stmt.setString(6, "F");
			rset = stmt.executeQuery();
			if(rset.next()) 
			{
				submission_chk = true;
				supp_cd= rset.getString(2) == null ? "" : rset.getString(2);
				vendor_cd= rset.getString(3) == null ? "" : rset.getString(3);
				inv_dt = rset.getString(4) == null ? "" : rset.getString(4);
				sac_cd = rset.getString(5) == null ? "" : rset.getString(5);
				currency = rset.getString(6) == null ? "" : rset.getString(6);
				exchng_rate = rset.getString(7) == null ? "" : rset.getString(7);
				exchng_eff_dt = rset.getString(8) == null ? "" : rset.getString(8);
				sale_amt = nf.format(Double.parseDouble(rset.getString(16) == null ? "" : rset.getString(16)));
				gross_amt = nf.format(Double.parseDouble(rset.getString(9) == null ? "" : rset.getString(9)));
				total_gst = nf.format(Double.parseDouble(rset.getString(10) == null ? "" : rset.getString(10)));
				tax_struct_cd = rset.getString(13) == null ? "" : rset.getString(13);
				tax_struct_dt = rset.getString(25) == null ? "" : rset.getString(25);
				taxValue = rset.getDouble(10);
				
				purchase_no = rset.getString(11) == null ? "" : rset.getString(11);
				ref_no = rset.getString(12) == null ? "" : rset.getString(12);
				
				desc_item = rset.getString(14) == null ? "" : rset.getString(14);
				net_amt = nf.format(Double.parseDouble(rset.getString(15) == null ? "" : rset.getString(15)));
				remark1=rset.getString(17) == null ? "" : rset.getString(17);
				remark2=rset.getString(18) == null ? "" : rset.getString(18);
				if(opration.equals("CHECK"))
				{
					activity_value = rset.getString(22) == null ? "" : rset.getString(22);
				}
				else if(opration.equals("APPROVE"))
				{
					activity_value = rset.getString(19) == null ? "" : rset.getString(19);
				}
				fin_yr = rset.getString(20) == null ? "" : rset.getString(20);
				inv_seq = rset.getString(21) == null ? "" : rset.getString(21);
				inv_no = rset.getString(23) == null ? "" : rset.getString(23);
				inv_id_seq = rset.getString(24) == null ? "" : rset.getString(24);
				invoice_category = rset.getString(26) == null ? "" : rset.getString(26);
				invoice_raised_in = rset.getString(27) == null ? "" : rset.getString(27);
				
				queryString2 = "SELECT SAC_CODE,SAC_DESC "
						+ "FROM FMS_SAC_MST A "
						+ "WHERE SAC_CD  = ?";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1,sac_cd);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					sac_code = rset2.getString(1)==null?"":rset2.getString(1);	
					sac_des = rset2.getString(2)==null?"":rset2.getString(2);	
				}
				rset2.close();
				stmt2.close();
				
				tax_struct_info=utilBean.getTaxDescr(conn,tax_struct_cd);
				if (tax_struct_info.contains(",")) 
				{
					tax_value = nf.format(Double.parseDouble((Double.doubleToRawLongBits(taxValue)==Double.doubleToRawLongBits(0)) ? "0" : String.format("%.2f", taxValue / 2)));
				} 
				else 
				{
					tax_value = nf.format(Double.parseDouble((Double.doubleToRawLongBits(taxValue)==Double.doubleToRawLongBits(0)) ? "0" : String.format("%.2f", taxValue)));
				}	
			
				tax_amt=nf.format(taxValue);
				
				
				//if(!opration.equals("MODIFY")) 
				{
					int srno=0;
					if(currency.equals("1")) 
					{
						String currency_nm = "INR";
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add(desc_item);
						VPDF_COL3.add(currency_nm);
						VPDF_COL4.add(es.formatAmount(sale_amt));
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("<b>Total Amount</b>");
						VPDF_COL3.add("<b>"+currency_nm+"</b>");
						VPDF_COL4.add("<b>"+es.formatAmount(gross_amt)+"</b>");
						
						srno+=1;
					}
					
					double temp_srno=srno;
					Vector VTAX_CODE = new Vector();
					Vector VTAX_DESCR = new Vector();
					Vector VTAX_AMT = new Vector();
					if (tax_struct_info.contains(",")) 
					{
						BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_amt));
						BigDecimal factor = new BigDecimal(2);
						for(int i = 0;i<tax_struct_info.split(",").length;i++) 
						{
							BigDecimal sub_tax_amt = tax_amt1.divide(factor, 0, RoundingMode.HALF_UP);
							temp_srno=temp_srno+0.1;
							VPDF_COL1.add(nf0.format(temp_srno));
							VPDF_COL2.add(tax_struct_info.split(",")[i]);
							VPDF_COL3.add("INR");
							VPDF_COL4.add(es.formatAmount(""+Double.parseDouble(""+sub_tax_amt)));
							
							VTAX_CODE.add(tax_struct_cd);
							VTAX_DESCR.add(tax_struct_info.split(",")[i]);
							VTAX_AMT.add(nf.format(sub_tax_amt));
						}
						Vector VTEMP_TAX_DTL = new Vector();
						
						VTEMP_TAX_DTL.add(VTAX_CODE);
						VTEMP_TAX_DTL.add(VTAX_DESCR);
						VTEMP_TAX_DTL.add(VTAX_AMT);
						
						VMULTI_TAX_STRUCT.add(VTEMP_TAX_DTL);
					} 
					
					VPDF_COL1.add(srno);
					VPDF_COL2.add("<b>Tax ("+tax_struct_info+")</b>");
					VPDF_COL3.add("<b>INR</b>");
					VPDF_COL4.add("<b>"+es.formatAmount(tax_amt)+"</b>");
					
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("<b>Total Amount in INR</b>");
					VPDF_COL3.add("<b>INR</b>");
					VPDF_COL4.add("<b>"+es.formatAmount(net_amt)+"</b>");
				}
				
				if(!net_amt.equalsIgnoreCase(""))		
				{
					amt_in_word=es.convert(Double.parseDouble(net_amt));
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
	
	public void getAllPdfFileName()
	{
		String function_nm="getAllPdfFileName()";
		try
		{
			queryString3="SELECT FILE_NAME,PDF_SIGNED "
					+ "FROM FMS_OTH_INV_FILE_DTL "
					+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? AND SUPPLIER_CD=? "
 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
 	        		+ "AND PDF_TYPE!=?";
			stmt3=conn.prepareStatement(queryString3);
			stmt3.setString(1, comp_cd);
			stmt3.setString(2, inv_type);
			stmt3.setString(3, supp_cd);
			stmt3.setString(4, inv_seq);
			stmt3.setString(5, fin_yr);
			stmt3.setString(6, "X");
			rset3=stmt3.executeQuery();
			while(rset3.next())
			{
				String pdf_signed=rset3.getString(2)==null?"":rset3.getString(2);
				VPDF_FILE_NAME.add(rset3.getString(1)==null?"":rset3.getString(1));
				
				if(pdf_signed.equals("Y"))
				{
					VPDF_FILE_PATH.add(CommonVariable.signed_ig_inv_path);
				}
				else
				{
					VPDF_FILE_PATH.add(CommonVariable.ig_inv_path);
				}
			}
			rset3.close();
			stmt3.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//AP20251104
	public void getShipMst()
	{
		String function_nm="getShipMst()";
		try
		{
			int count =0;
			
			queryString="SELECT SHIP_CD,SHIP_NAME,SHIP_FLAG,SHIP_ITEM "
					+ "FROM FMS_SHIP_MST A "
					+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_SHIP_MST B WHERE A.SHIP_CD=B.SHIP_CD) ";
			if(!vessel_cd.equals("") || opration.equals("INSERT"))
			{
				queryString+= "AND SHIP_CD=? ";
			}
			queryString+= "ORDER BY SHIP_CD DESC ";
			stmt=conn.prepareStatement(queryString);
			if(!vessel_cd.equals("") || opration.equals("INSERT"))
			{
				stmt.setString(++count, vessel_cd);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				ship_cd =(rset.getString(1)==null?"":rset.getString(1));
				ship_name =(rset.getString(2)==null?"":rset.getString(2));
				vessel_nm =(rset.getString(2)==null?"":rset.getString(2));
				ship_flag=(rset.getString(3)==null?"":rset.getString(3));
				vessel_flag=(rset.getString(3)==null?"":rset.getString(3));
				ship_item=(rset.getString(4)==null?"":rset.getString(4));
				vessel_item=(rset.getString(4)==null?"":rset.getString(4));
				 
				VSHIP_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VSHIP_NAME.add(rset.getString(2)==null?"":rset.getString(2));
				VSHIP_FLAG.add(rset.getString(3)==null?"":rset.getString(3));
				VSHIP_ITEM.add(rset.getString(4)==null?"":rset.getString(4));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getShipMstMod()
	{
		String function_nm="getShipMstMod()";
		try
		{
			int count =0;
			queryString="SELECT SHIP_CD,SHIP_NAME,SHIP_FLAG,SHIP_ITEM "
					+ "FROM FMS_SHIP_MST A "
					+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_SHIP_MST B WHERE A.SHIP_CD=B.SHIP_CD) AND SHIP_CD=? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++count, ship_cd);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				vessel_cd =(rset.getString(1)==null?"":rset.getString(1));
				vessel_nm =(rset.getString(2)==null?"":rset.getString(2));
				vessel_flag=(rset.getString(3)==null?"":rset.getString(3));
				vessel_item=(rset.getString(4)==null?"":rset.getString(4));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//AP20251104
	public void getInvoiceDtlHPPLShippingAgent()
	{	
		String function_nm="getInvoiceDtlHPPLShippingAgent()";
		try
		{
			double taxValue = 0;
			String tax_value = "";
			queryString = "SELECT A.COMPANY_CD, A.SUPPLIER_CD, A.VENDOR_CD, TO_CHAR(A.INVOICE_DT, 'DD/MM/YYYY'), B.SAC_CODE, A.SALE_PRICE_UNIT, "
					+ "A.EXCHG_RATE_VALUE, TO_CHAR(A.EXCHG_RATE_DT, 'DD/MM/YYYY'),"
					+ "A.GROSS_AMT, A.TAX_AMT_INR, B.PURCHASE_NO, B.REFERENCE_NO, B.TAX_STRUCT_CD, B.ITEM_DESCRIPTION, A.NET_PAYABLE,A.SALE_AMT,A.REMARK,A.REMARK1,"
					+ "A.APPROVED_FLAG,A.FINANCIAL_YEAR,A.INVOICE_SEQ,A.CHECKED_FLAG,A.INVOICE_NO,A.INVOICE_ID_SEQ,TO_CHAR(B.TAX_EFF_DT, 'DD/MM/YYYY'),A.INVOICE_CATEGORY,A.INVOICE_RAISED_IN,  "
					+ "A.SALE_PRICE,TO_CHAR(A.DUE_DT, 'DD/MM/YYYY'),A.REMARK2,A.REMARK3,B.VESSEL_CD,B.VESSEL_AGENT,B.VESSEL_FLAG,B.GRT,B.IMPORTER,B.QUANTITY,B.HRS_BERTHING,B.TIME_SLOTS_BERTHING "
					+ "FROM FMS_OTH_INVOICE_MST A, FMS_OTH_INVOICE_DTL B "
					+ "WHERE A.COMPANY_CD = ? AND A.INVOICE_SEQ = ? AND A.INVOICE_TYPE = ? AND A.FINANCIAL_YEAR=? AND A.SUPPLIER_CD=? AND A.INV_FLAG = ? "
					+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.INVOICE_SEQ = B.INVOICE_SEQ AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.INVOICE_TYPE = B.INVOICE_TYPE AND "
					+ "A.INVOICE_DT = B.EFF_DT AND A.VENDOR_CD = B.VENDOR_CD AND A.SUPPLIER_CD = B.SUPPLIER_CD AND A.INV_FLAG = B.INV_FLAG";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_seq);
			stmt.setString(3, inv_type);
			stmt.setString(4, fin_yr);
			stmt.setString(5, supp_cd);
			stmt.setString(6, "F");
			rset = stmt.executeQuery();
			if(rset.next()) 
			{
				submission_chk = true;
				supp_cd= rset.getString(2) == null ? "" : rset.getString(2);
				vendor_cd= rset.getString(3) == null ? "" : rset.getString(3);
				inv_dt = rset.getString(4) == null ? "" : rset.getString(4);
				sac_cd = rset.getString(5) == null ? "" : rset.getString(5);
				currency = rset.getString(6) == null ? "" : rset.getString(6);
				exchng_rate = rset.getString(7) == null ? "" : rset.getString(7);
				exchng_eff_dt = rset.getString(8) == null ? "" : rset.getString(8);
				sale_amt = nf.format(Double.parseDouble(rset.getString(16) == null ? "" : rset.getString(16)));
				gross_amt = nf.format(Double.parseDouble(rset.getString(9) == null ? "" : rset.getString(9)));
				total_gst = nf.format(Double.parseDouble(rset.getString(10) == null ? "" : rset.getString(10)));
				tax_struct_cd = rset.getString(13) == null ? "" : rset.getString(13);
				tax_struct_dt = rset.getString(25) == null ? "" : rset.getString(25);
				taxValue = rset.getDouble(10);
				
				purchase_no = rset.getString(11) == null ? "" : rset.getString(11);
				ref_no = rset.getString(12) == null ? "" : rset.getString(12);
				
				desc_item = rset.getString(14) == null ? "" : rset.getString(14);
				net_amt = nf.format(Double.parseDouble(rset.getString(15) == null ? "" : rset.getString(15)));
				remark1=rset.getString(17) == null ? "" : rset.getString(17);
				remark2=rset.getString(18) == null ? "" : rset.getString(18);
				if(opration.equals("CHECK"))
				{
					activity_value = rset.getString(22) == null ? "" : rset.getString(22);
				}
				else if(opration.equals("APPROVE"))
				{
					activity_value = rset.getString(19) == null ? "" : rset.getString(19);
				}
				fin_yr = rset.getString(20) == null ? "" : rset.getString(20);
				inv_seq = rset.getString(21) == null ? "" : rset.getString(21);
				inv_no = rset.getString(23) == null ? "" : rset.getString(23);
				inv_id_seq = rset.getString(24) == null ? "" : rset.getString(24);
				invoice_category = rset.getString(26) == null ? "" : rset.getString(26);
				invoice_raised_in = rset.getString(27) == null ? "" : rset.getString(27);
				rate = nf2.format(rset.getDouble(28));
				inv_due_dt = rset.getString(29) == null ? "" : rset.getString(29);
				remark3 = rset.getString(30) == null ? "" : rset.getString(30);
				remark4 = rset.getString(31) == null ? "" : rset.getString(31);
				vessel_cd = rset.getString(32) == null ? "" : rset.getString(32);
				vessel_agent_nm = rset.getString(33) == null ? "" : rset.getString(33);
				vessel_flag = rset.getString(34) == null ? "" : rset.getString(34);
				grt = rset.getString(35) == null ? "" : rset.getString(35);
				if(opration.equals("APPROVE") || opration.equals("CHECK"))
				{
					grt = es.formatWithCommaNoDecimal(grt);
				}
				importer_nm = rset.getString(36) == null ? "" : rset.getString(36);
				inv_qty = rset.getString(37) == null ? "" : rset.getString(37);
				berthing_hours = rset.getString(38) == null ? "" : rset.getString(38);
				berthing_slots_no = rset.getString(39) == null ? "" : rset.getString(39);
				
				queryString1="SELECT SHIP_CD,SHIP_NAME,SHIP_FLAG,SHIP_ITEM "
						+ "FROM FMS_SHIP_MST A "
						+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_SHIP_MST B WHERE A.SHIP_CD=B.SHIP_CD) AND SHIP_CD=? ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, vessel_cd);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					vessel_nm =(rset1.getString(2)==null?"":rset1.getString(2));
					vessel_flag=(rset1.getString(3)==null?"":rset1.getString(3));
					vessel_item=(rset1.getString(4)==null?"":rset1.getString(4));
				}
				rset1.close();
				stmt1.close();
				
				queryString2 = "SELECT SAC_CODE,SAC_DESC "
						+ "FROM FMS_SAC_MST A "
						+ "WHERE SAC_CD  = ?";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1,sac_cd);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					sac_code = rset2.getString(1)==null?"":rset2.getString(1);	
					sac_des = rset2.getString(2)==null?"":rset2.getString(2);	
				}
				rset2.close();
				stmt2.close();
				
				tax_struct_info=utilBean.getTaxDescr(conn,tax_struct_cd);
				if (tax_struct_info.contains(",")) 
				{
					tax_value = nf.format(Double.parseDouble((Double.doubleToRawLongBits(taxValue)==Double.doubleToRawLongBits(0)) ? "0" : String.format("%.2f", taxValue / 2)));
				} 
				else 
				{
					tax_value = nf.format(Double.parseDouble((Double.doubleToRawLongBits(taxValue)==Double.doubleToRawLongBits(0)) ? "0" : String.format("%.2f", taxValue)));
				}	
			
				tax_amt=nf.format(taxValue);
				
				
				//if(!opration.equals("MODIFY")) 
				{
					int srno=0;
					if(currency.equals("2")) 
					{
						String currency_nm = "USD";
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add(desc_item);
						VPDF_COL3.add(currency_nm);
						VPDF_COL4.add(rate);
						VPDF_COL5.add("Per GRT/Per 8 Hrs");
						VPDF_COL6.add(es.formatAmount(sale_amt));
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("<b>Total Charges</b>");
						VPDF_COL3.add("<b>"+currency_nm+"</b>");
						VPDF_COL4.add("");
						VPDF_COL5.add("");
						VPDF_COL6.add("<b>"+es.formatAmount(gross_amt)+"</b>");
						
						srno+=1;
					}
					
					double temp_srno=srno;
					Vector VTAX_CODE = new Vector();
					Vector VTAX_DESCR = new Vector();
					Vector VTAX_AMT = new Vector();
					if (tax_struct_info.contains(",")) 
					{
						BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_amt));
						BigDecimal factor = new BigDecimal(2);
						for(int i = 0;i<tax_struct_info.split(",").length;i++) 
						{
							BigDecimal sub_tax_amt = tax_amt1.divide(factor, 0, RoundingMode.HALF_UP);
							temp_srno=temp_srno+0.1;
							VPDF_COL1.add(nf0.format(temp_srno));
							VPDF_COL2.add(tax_struct_info.split(",")[i]);
							VPDF_COL3.add("USD");
							VPDF_COL4.add("");
							VPDF_COL5.add("");
							VPDF_COL6.add(es.formatAmount(""+Double.parseDouble(""+sub_tax_amt)));
							
							VTAX_CODE.add(tax_struct_cd);
							VTAX_DESCR.add(tax_struct_info.split(",")[i]);
							VTAX_AMT.add(nf.format(sub_tax_amt));
						}
						Vector VTEMP_TAX_DTL = new Vector();
						
						VTEMP_TAX_DTL.add(VTAX_CODE);
						VTEMP_TAX_DTL.add(VTAX_DESCR);
						VTEMP_TAX_DTL.add(VTAX_AMT);
						
						VMULTI_TAX_STRUCT.add(VTEMP_TAX_DTL);
					} 
					
					VPDF_COL1.add(srno);
					VPDF_COL2.add("<b>Tax ("+tax_struct_info+")</b>");
					VPDF_COL3.add("<b>USD</b>");
					VPDF_COL4.add("");
					VPDF_COL5.add("");
					VPDF_COL6.add("<b>"+es.formatAmount(tax_amt)+"</b>");

					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("<b>Total Amount in USD</b>");
					VPDF_COL3.add("<b>USD</b>");
					VPDF_COL4.add("");
					VPDF_COL5.add("");
					VPDF_COL6.add("<b>"+es.formatAmount(net_amt)+"</b>");
				}
				
				if(!net_amt.equalsIgnoreCase(""))		
				{
					amt_in_word=es.convert(Double.parseDouble(net_amt));
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
	
	//Deep20251104
	public void getExchangeRate()
	{
		String function_nm="getExchangeRate()";
		try
		{
			queryString = "SELECT TO_CHAR (SYSDATE-1,'DD/MM/YYYY')"
					+ "FROM DUAL ";
			stmt=conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				if(user_dt.equals(""))
				{
					user_dt = rset.getString(1)==null?"":rset.getString(1);;
				}
			}
			rset.close();
			stmt.close();
			
			queryString = "SELECT EXC_RATE_CD,EXC_RATE_NM "
					+ "FROM FMS_EXCHG_RATE_MST "
					+ "ORDER BY EXC_RATE_NM";
			stmt=conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VEXCHNG_RATE_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VEXCHNG_RATE_NM.add(rset.getString(2)==null?"":rset.getString(2));
			}
			rset.close();
			stmt.close();
			
			
			for (int i = 0; i < VEXCHNG_RATE_CD.size(); i++) 
			{
				queryString = "SELECT EXCHG_VAL,TO_CHAR (EFF_DT,'DD/MM/YYYY') "
						+ "FROM FMS_EXCHG_RATE_ENTRY "
						+ "WHERE EXCHG_RATE_CD = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1,""+VEXCHNG_RATE_CD.elementAt(i));
				stmt.setString(2,user_dt);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					VEXCHNG_RATE.add(nf.format(Double.parseDouble(rset.getString(1) == null ? "" : rset.getString(1))));
					VEXCHNG_EFF_DT.add(rset.getString(2)==null?"":rset.getString(2));
				}
				else
				{
					VEXCHNG_RATE.add(" ");
					VEXCHNG_EFF_DT.add(" ");
				}
				
				rset.close();
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//Deep20251106
	public void getCargoDetails()
	{
		String function_nm="getCargoDetails()";
		try
		{
			queryString="SELECT TO_CHAR(TO_DATE(DTL + LEVEL - 1,'DD/MM/YY'),'DD/MM/YYYY') "
					+ "FROM (SELECT TO_DATE(?,'MM/YYYY') AS DTL "
					+ "FROM DUAL) "
					+ "CONNECT BY LEVEL <= ADD_MONTHS (DTL, 1) - DTL ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1,month+"/"+year);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VMONTH_DATES.add(rset.getString(1)==null?"":rset.getString(1));
			}
			rset.close();
			stmt.close();
			
			if(cargo_type.equals("LTCORA"))
			{
				for (int i = 0; i < VMONTH_DATES.size(); i++) 
				{
					queryString = "SELECT A.CARGO_REF,TO_CHAR(A.QQ_DT,'DD-MON-YY'),A.SHIP_CD,NVL(D.ADQ_QTY,'0'),B.SHIP_NAME "
							+ "FROM FMS_LTCORA_CONT_CARGO_DTL A, FMS_SHIP_MST B, FMS_LTCORA_CONT_CARGO_ADQ D "
							+ "WHERE A.COMPANY_CD=? AND A.SHIP_CD = B.SHIP_CD AND A.QQ_DT = TO_DATE(?, 'DD/MM/YYYY') AND D.ADQ_QTY!=0  AND A.BUY_SALE = 'C' "
							+ "AND A.COMPANY_CD = D.COMPANY_CD AND A.COUNTERPARTY_CD = D.COUNTERPARTY_CD AND A.BUY_SALE = D.BUY_SALE AND A.AGMT_TYPE = D.AGMT_TYPE "
							+ "AND A.AGMT_NO = D.AGMT_NO AND A.CONT_NO = D.CONT_NO AND A.CONTRACT_TYPE = D.CONTRACT_TYPE AND A.CARGO_NO = D.CARGO_NO  "
							+ "AND A.CARGO_REF NOT IN(SELECT C.REFERENCE_NO FROM FMS_OTH_INVOICE_DTL C WHERE C.INVOICE_TYPE = ? AND C.CARGO_TYPE = ? "
							+ "AND C.REFERENCE_NO IS NOT NULL AND C.INV_FLAG = 'F') "
							+ "ORDER BY A.QQ_DT";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1,comp_cd);
					stmt.setString(2,""+VMONTH_DATES.elementAt(i));
					stmt.setString(3,inv_type);
					stmt.setString(4,cargo_type);
					rset=stmt.executeQuery();
					while(rset.next())
					{
						VCARGO_REF.add(rset.getString(1)==null?"":rset.getString(1)); 
						VCARGO_DT.add(rset.getString(2)==null?"":rset.getString(2));
						VSHIP_CD.add(rset.getString(3)==null?"":rset.getString(3));
						VQTY_MMBTU.add(nf.format(Double.parseDouble(rset.getString(4) == null ? "" : rset.getString(4))));
						VCARGO_TYPE.add("LTCORA");
						VSHIP_NAME.add(rset.getString(5)==null?"":rset.getString(5));
						VDIS_PORT.add("");
						VRATE.add("");
						VCARGO_AMT.add("");
					}
					rset.close();
					stmt.close();
				}
			}
			else if(cargo_type.equals("SALES"))
			{
				for (int i = 0; i < VMONTH_DATES.size(); i++) 
				{
					queryString = "SELECT D.CARGO_REF,TO_CHAR(A.QQ_DT,'DD-MON-YY'),A.SHIP_CD,NVL(A.QQ_QTY_MMBTU,'0') "
							+ "FROM FMS_BUY_CARGO_ALLOC A, FMS_TRADER_CARGO_MST D "
							+ "WHERE A.COMPANY_CD=? AND A.QQ_DT = TO_DATE(?, 'DD/MM/YYYY') AND A.QQ_QTY_MMBTU!='0' "
							+ "AND A.COMPANY_CD = D.COMPANY_CD AND A.COUNTERPARTY_CD = D.COUNTERPARTY_CD AND A.AGMT_TYPE = D.AGMT_TYPE "
							+ "AND A.AGMT_NO = D.AGMT_NO AND A.CONT_NO = D.CONT_NO AND A.CONTRACT_TYPE = D.CONTRACT_TYPE AND A.CARGO_NO = D.CARGO_NO "
							+ "AND D.CARGO_REF NOT IN(SELECT C.REFERENCE_NO FROM FMS_OTH_INVOICE_DTL C WHERE C.INVOICE_TYPE = ? AND C.CARGO_TYPE = ? "
							+ "AND C.REFERENCE_NO IS NOT NULL AND C.INV_FLAG = 'F') "
							+ "ORDER BY A.QQ_DT";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1,comp_cd);
					stmt.setString(2,""+VMONTH_DATES.elementAt(i));
					stmt.setString(3,inv_type);
					stmt.setString(4,cargo_type);
					rset=stmt.executeQuery();
					while(rset.next())
					{
						queryString2 = "SELECT SHIP_NAME "
								+ "FROM FMS_SHIP_MST "
								+ "WHERE SHIP_CD  = ?";
						stmt2=conn.prepareStatement(queryString2);
						stmt2.setString(1,rset.getString(3)==null?"":rset.getString(3));
						rset2=stmt2.executeQuery();
						if(rset2.next())
						{
							VSHIP_NAME.add(rset2.getString(1)==null?"":rset2.getString(1));
						}
						else 
						{
							VSHIP_NAME.add("");
						}
						rset2.close();
						stmt2.close();
						
						VCARGO_REF.add(rset.getString(1)==null?"":rset.getString(1)); 
						VCARGO_DT.add(rset.getString(2)==null?"":rset.getString(2));
						VSHIP_CD.add(rset.getString(3)==null?"":rset.getString(3));
						VQTY_MMBTU.add(nf.format(Double.parseDouble(rset.getString(4) == null ? "" : rset.getString(4))));
						VCARGO_TYPE.add("SALES");
						VDIS_PORT.add("");
						VRATE.add("");
						VCARGO_AMT.add("");
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

	//Deep20251106
	public void getInvoiceDtlHPPLSEIPL()
	{	
		String function_nm="getInvoiceDtlHPPLSEIPL()";

		try
		{
			double taxValue = 0;
			String tax_value="";
			queryString = "SELECT A.COMPANY_CD,A.VENDOR_CD,A.SUPPLIER_CD,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),B.VENDOR_SUPP_INV_REF,"
					+ "B.PACER_NO,A.GROSS_AMT,A.TAX_AMT_INR,A.NET_PAYABLE,A.EXCHG_RATE_VALUE,TO_CHAR(A.EXCHG_RATE_DT, 'DD/MM/YYYY'),"
					+ "A.SALE_PRICE_UNIT,B.SAC_CODE,B.TAX_STRUCT_CD,B.ITEM_DESCRIPTION,A.REMARK,A.REMARK1,A.APPROVED_FLAG,A.FINANCIAL_YEAR,"
					+ "A.INVOICE_SEQ,A.SALE_AMT,A.CHECKED_FLAG,A.INVOICE_NO,A.INVOICE_ID_SEQ,TO_CHAR(B.TAX_EFF_DT, 'DD/MM/YYYY'),A.INVOICE_CATEGORY,A.INVOICE_RAISED_IN,"
					+ "TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),B.CARGO_TYPE,TO_CHAR(A.EXCHG_RATE_DT,'DD/MM/YYYY'),A.REMARK2,A.EXCHG_RATE_CD "
					+ "FROM FMS_OTH_INVOICE_MST A, FMS_OTH_INVOICE_DTL B "
					+ "WHERE A.COMPANY_CD = ? AND A.INVOICE_SEQ = ? AND A.INVOICE_TYPE = ? AND A.FINANCIAL_YEAR=? AND A.SUPPLIER_CD=? AND A.INV_FLAG = ? "
					+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.INVOICE_TYPE = B.INVOICE_TYPE "
					+ "AND A.VENDOR_CD = B.VENDOR_CD AND A.SUPPLIER_CD = B.SUPPLIER_CD AND A.INVOICE_SEQ = B.INVOICE_SEQ AND A.VENDOR_TYPE = 'S' AND A.INV_FLAG = B.INV_FLAG";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_seq);
			stmt.setString(3, inv_type);
			stmt.setString(4, fin_yr);
			stmt.setString(5, supp_cd);
			stmt.setString(6, "F");
			rset = stmt.executeQuery();
			if(rset.next()) 
			{
				submission_chk = true;
				
				vendor_cd= rset.getString(2) == null ? "" : rset.getString(2);
				supp_cd= rset.getString(3) == null ? "" : rset.getString(3);				
				inv_dt = rset.getString(4) == null ? "" : rset.getString(4);
				gross_amt = nf.format(Double.parseDouble(rset.getString(7) == null ? "" : rset.getString(7)));
				total_gst = nf.format(Double.parseDouble(rset.getString(8) == null ? "" : rset.getString(8)));
				net_amt = nf.format(Double.parseDouble(rset.getString(9) == null ? "" : rset.getString(9)));
				exchang_val = rset.getString(10) == null ? "" : rset.getString(10);
				exchng_eff_dt = rset.getString(11) == null ? "" : rset.getString(11);
				currency = rset.getString(12) == null ? "" : rset.getString(12);
				sac_cd = rset.getString(13) == null ? "" : rset.getString(13);
				tax_struct_cd = rset.getString(14) == null ? "" : rset.getString(14);
				tax_struct_dt = rset.getString(25) == null ? "" : rset.getString(25);
				desc_item = rset.getString(15) == null ? "" : rset.getString(15);
				remark1 = rset.getString(16) == null ? "" : rset.getString(16);
				remark2 = rset.getString(17) == null ? "" : rset.getString(17);
				if(opration.equals("CHECK"))
				{
					activity_value = rset.getString(22) == null ? "" : rset.getString(22);
				}
				else if(opration.equals("APPROVE"))
				{
					activity_value = rset.getString(18) == null ? "" : rset.getString(18);
				}
				fin_yr = rset.getString(19) == null ? "" : rset.getString(19);
				inv_seq = rset.getString(20) == null ? "" : rset.getString(20);
				sale_amt = nf.format(Double.parseDouble(rset.getString(21) == null ? "" : rset.getString(21)));
				inv_no = rset.getString(23) == null ? "" : rset.getString(23);
				inv_id_seq = rset.getString(24) == null ? "" : rset.getString(24);
				invoice_category = rset.getString(26) == null ? "" : rset.getString(26);
				invoice_raised_in = rset.getString(27) == null ? "" : rset.getString(27);
				due_dt = rset.getString(28) == null ? "" : rset.getString(28);
				cargo_type = rset.getString(29) == null ? "" : rset.getString(29);
				user_dt = rset.getString(30) == null ? "" : rset.getString(30);
				exchng_eff_dt = rset.getString(30) == null ? "" : rset.getString(30);
				remark3 = rset.getString(31) == null ? "" : rset.getString(31);
				exchng_rate = rset.getString(32) == null ? "" : rset.getString(32);
				
				taxValue = rset.getDouble(8);
				queryString2 = "SELECT SAC_CODE,SAC_DESC "
						+ "FROM FMS_SAC_MST A "
						+ "WHERE SAC_CD  = ?";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1,sac_cd);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					sac_code = rset2.getString(1)==null?"":rset2.getString(1);	
					sac_des = rset2.getString(2)==null?"":rset2.getString(2);	
				}
				rset2.close();
				stmt2.close();
				
				tax_struct_info=utilBean.getTaxDescr(conn,tax_struct_cd);
				
				if (tax_struct_info.contains(",")) 
				{
					tax_value = nf.format(Double.parseDouble((Double.doubleToRawLongBits(taxValue)==Double.doubleToRawLongBits(0)) ? "0" : String.format("%.2f", taxValue / 2)));
				} 
				else 
				{
					tax_value = nf.format(Double.parseDouble((Double.doubleToRawLongBits(taxValue)==Double.doubleToRawLongBits(0)) ? "0" : String.format("%.2f", taxValue)));
				}	
			
				tax_amt=nf.format(taxValue);
				
				
				//if(!opration.equals("MODIFY")) 
				{
					int srno=0;
					String currency_nm ="";
					if(currency.equals("2")) 
					{
						currency_nm = "USD";
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add(desc_item +"<br><br>For the Following Cargoes received in the month of "+utilDate.getShortMonthName(inv_dt)+" "+year);
						VPDF_COL3.add("");
						VPDF_COL4.add("");
						VPDF_COL5.add("");
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("");
						VPDF_COL3.add(currency_nm);
						VPDF_COL4.add("");
						VPDF_COL5.add("");
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("<b>Total Charges</b>");
						VPDF_COL3.add("<b>"+currency_nm+"</b>");
						VPDF_COL4.add("");
						VPDF_COL5.add("<b>"+es.formatAmount(sale_amt)+"</b>");
						
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("Exchange Rate : "+exchang_val+" (INR/USD) dated "+exchng_eff_dt);
						VPDF_COL3.add("INR/USD");
						VPDF_COL4.add("");
						VPDF_COL5.add(exchang_val);
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("<b>Net Amount</b>");
						VPDF_COL3.add("<b>INR</b>");
						VPDF_COL4.add("");
						VPDF_COL5.add("<b>"+es.formatAmount(gross_amt)+"</b>");
						
						srno+=1;
					}
				
					double temp_srno=srno;
					Vector VTAX_CODE = new Vector();
					Vector VTAX_DESCR = new Vector();
					Vector VTAX_AMT = new Vector();
					if (tax_struct_info.contains(",")) 
					{
						BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_amt));
						BigDecimal factor = new BigDecimal(2);
						for(int i = 0;i<tax_struct_info.split(",").length;i++) 
						{
							BigDecimal sub_tax_amt = tax_amt1.divide(factor, 0, RoundingMode.HALF_UP);
							temp_srno=temp_srno+0.1;
							VPDF_COL1.add(nf0.format(temp_srno));
							VPDF_COL2.add(tax_struct_info.split(",")[i]);
							VPDF_COL3.add("INR");
							VPDF_COL4.add("");
							VPDF_COL5.add(es.formatAmount(""+Double.parseDouble(""+sub_tax_amt)));
							
							VTAX_CODE.add(tax_struct_cd);
							VTAX_DESCR.add(tax_struct_info.split(",")[i]);
							VTAX_AMT.add(nf.format(sub_tax_amt));
						}
						Vector VTEMP_TAX_DTL = new Vector();
						
						VTEMP_TAX_DTL.add(VTAX_CODE);
						VTEMP_TAX_DTL.add(VTAX_DESCR);
						VTEMP_TAX_DTL.add(VTAX_AMT);
						
						VMULTI_TAX_STRUCT.add(VTEMP_TAX_DTL);
					} 
					
					VPDF_COL1.add(srno);
					VPDF_COL2.add("<b>Tax ("+tax_struct_info+")</b>");
					VPDF_COL3.add("<b>INR</b>");
					VPDF_COL4.add("");
					VPDF_COL5.add("<b>"+es.formatAmount(tax_amt)+"</b>");

					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("<b>Total Amount in INR</b>");
					VPDF_COL3.add("<b>INR</b>");
					VPDF_COL4.add("");
					VPDF_COL5.add("<b>"+es.formatAmount(net_amt)+"</b>");
				}
				
				if(!net_amt.equalsIgnoreCase(""))		
				{
					amt_in_word=es.convert(Double.parseDouble(net_amt));
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
	
	//Deep20251106
	public void getCargoDetailsModify()
	{
		String function_nm="getCargoDetailsModify()";
		try
		{
			queryString="SELECT TO_CHAR(TO_DATE(DTL + LEVEL - 1,'DD/MM/YY'),'DD/MM/YYYY') "
					+ "FROM (SELECT TO_DATE(?,'MM/YYYY') AS DTL "
					+ "FROM DUAL) "
					+ "CONNECT BY LEVEL <= ADD_MONTHS (DTL, 1) - DTL ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1,month+"/"+year);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VMONTH_DATES.add(rset.getString(1)==null?"":rset.getString(1));
			}
			rset.close();
			stmt.close();
			
			if(cargo_type.equals("LTCORA"))
			{
				for (int i = 0; i < VMONTH_DATES.size(); i++) 
				{
					queryString = "SELECT A.CARGO_REF,TO_CHAR(A.QQ_DT,'DD-MON-YY'),A.SHIP_CD,NVL(D.ADQ_QTY,'0'),B.SHIP_NAME, 0, 0.00, 0.00 "
							+ "FROM FMS_LTCORA_CONT_CARGO_DTL A, FMS_SHIP_MST B, FMS_LTCORA_CONT_CARGO_ADQ D "
							+ "WHERE A.SHIP_CD = B.SHIP_CD AND A.COMPANY_CD = ? AND A.QQ_DT = TO_DATE(?, 'DD/MM/YYYY') AND D.ADQ_QTY!=0 AND A.BUY_SALE = 'C'  "
							+ "AND A.COMPANY_CD = D.COMPANY_CD AND A.COUNTERPARTY_CD = D.COUNTERPARTY_CD AND A.BUY_SALE = D.BUY_SALE AND A.AGMT_TYPE = D.AGMT_TYPE "
							+ "AND A.AGMT_NO = D.AGMT_NO AND A.CONT_NO = D.CONT_NO AND A.CONTRACT_TYPE = D.CONTRACT_TYPE AND A.CARGO_NO = D.CARGO_NO  "
							+ "AND A.CARGO_REF NOT IN(SELECT C.REFERENCE_NO FROM FMS_OTH_INVOICE_DTL C WHERE C.INVOICE_TYPE = ? AND C.CARGO_TYPE = ? "
							+ "AND C.REFERENCE_NO IS NOT NULL AND C.INV_FLAG = 'F') ";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2,""+VMONTH_DATES.elementAt(i));
					stmt.setString(3,inv_type);
					stmt.setString(4,cargo_type);
					rset=stmt.executeQuery();
					while(rset.next())
					{
						VCARGO_REF.add(rset.getString(1)==null?"":rset.getString(1)); 
						VCARGO_DT.add(rset.getString(2)==null?"":rset.getString(2));
						VSHIP_CD.add(rset.getString(3)==null?"":rset.getString(3));
						VQTY_MMBTU.add(nf.format(Double.parseDouble(rset.getString(4) == null ? "" : rset.getString(4))));
						VCARGO_TYPE.add("LTCORA");
						VSHIP_NAME.add(rset.getString(5)==null?"":rset.getString(5));
						VDIS_PORT.add(rset.getString(6)==null?"":rset.getString(6));
						double rate = rset.getDouble(7);
						double cargo_amt = rset.getDouble(8);
						if(Double.doubleToRawLongBits(rate)==Double.doubleToRawLongBits(0))
						{
							VRATE.add("");
						}
						else
						{
							VRATE.add(nf2.format(rate));
						}
						if(Double.doubleToRawLongBits(cargo_amt)==Double.doubleToRawLongBits(0))
						{
							VCARGO_AMT.add("");
						}
						else
						{
							VCARGO_AMT.add(nf.format(cargo_amt));
						}
						
					}
					rset.close();
					stmt.close();
				}
				
				queryString1= "SELECT A.CARGO_REF,TO_CHAR(A.QQ_DT,'DD-MON-YY'),A.SHIP_CD,NVL(D.ADQ_QTY,'0'),B.SHIP_NAME,E.DISCHARGE_PORT,E.SALE_PRICE,E.CARGO_AMOUNT "
							+ "FROM FMS_LTCORA_CONT_CARGO_DTL A, FMS_SHIP_MST B, FMS_LTCORA_CONT_CARGO_ADQ D, FMS_OTH_INVOICE_DTL E "
							+ "WHERE A.SHIP_CD = B.SHIP_CD AND A.COMPANY_CD = ? AND D.ADQ_QTY!=0 AND A.BUY_SALE = 'C'  "
							+ "AND A.COMPANY_CD = D.COMPANY_CD AND A.COUNTERPARTY_CD = D.COUNTERPARTY_CD AND A.BUY_SALE = D.BUY_SALE AND A.AGMT_TYPE = D.AGMT_TYPE "
							+ "AND A.AGMT_NO = D.AGMT_NO AND A.CONT_NO = D.CONT_NO AND A.CONTRACT_TYPE = D.CONTRACT_TYPE AND A.CARGO_NO = D.CARGO_NO  "
							+ "AND A.CARGO_REF IN (SELECT C.REFERENCE_NO FROM FMS_OTH_INVOICE_DTL C WHERE C.INVOICE_TYPE = E.INVOICE_TYPE AND C.CARGO_TYPE = E.CARGO_TYPE "
							+ "AND C.INVOICE_SEQ = E.INVOICE_SEQ "
							+ "AND C.FINANCIAL_YEAR = E.FINANCIAL_YEAR AND C.SUPPLIER_CD = E.SUPPLIER_CD AND C.INV_FLAG = E.INV_FLAG "
							+ "AND C.REFERENCE_NO IS NOT NULL) AND E.INVOICE_TYPE = ? AND E.CARGO_TYPE = ? AND E.INVOICE_SEQ = ? "
							+ "AND E.FINANCIAL_YEAR = ? AND E.SUPPLIER_CD = ? AND E.INV_FLAG = ? AND A.COMPANY_CD = E.COMPANY_CD AND A.CARGO_REF = E.REFERENCE_NO ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2,inv_type);
				stmt1.setString(3,cargo_type);
				stmt1.setString(4, inv_seq);
				stmt1.setString(5, fin_yr);
				stmt1.setString(6, supp_cd);
				stmt1.setString(7, "F");
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					VCARGO_REF.add(rset1.getString(1)==null?"":rset1.getString(1)); 
					VCARGO_DT.add(rset1.getString(2)==null?"":rset1.getString(2));
					VSHIP_CD.add(rset1.getString(3)==null?"":rset1.getString(3));
					VQTY_MMBTU.add(nf.format(Double.parseDouble(rset1.getString(4) == null ? "" : rset1.getString(4))));
					VCARGO_TYPE.add("LTCORA");
					VSHIP_NAME.add(rset1.getString(5)==null?"":rset1.getString(5));
					VDIS_PORT.add(rset1.getString(6)==null?"":rset1.getString(6));
					double rate = rset1.getDouble(7);
					double cargo_amt = rset1.getDouble(8);
					if(Double.doubleToRawLongBits(rate)==Double.doubleToRawLongBits(0))
					{
						VRATE.add("");
					}
					else
					{
						VRATE.add(nf2.format(rate));
					}
					if(Double.doubleToRawLongBits(cargo_amt)==Double.doubleToRawLongBits(0))
					{
						VCARGO_AMT.add("");
					}
					else
					{
						VCARGO_AMT.add(nf.format(cargo_amt));
					}
					
				}
				rset1.close();
				stmt1.close();
				
			}
			else if(cargo_type.equals("SALES"))
			{
				queryString1 = "SELECT D.CARGO_REF,TO_CHAR(A.QQ_DT,'DD-MON-YY'),E.VESSEL_CD,NVL(A.QQ_QTY_MMBTU,'0'),E.DISCHARGE_PORT,E.SALE_PRICE,E.CARGO_AMOUNT "
						+ "FROM FMS_BUY_CARGO_ALLOC A, FMS_TRADER_CARGO_MST D, FMS_OTH_INVOICE_DTL E  "
						+ "WHERE A.QQ_QTY_MMBTU!='0' AND A.COMPANY_CD = ? "
						+ "AND A.COMPANY_CD = D.COMPANY_CD AND A.COUNTERPARTY_CD = D.COUNTERPARTY_CD AND A.AGMT_TYPE = D.AGMT_TYPE "
						+ "AND A.AGMT_NO = D.AGMT_NO AND A.CONT_NO = D.CONT_NO AND A.CONTRACT_TYPE = D.CONTRACT_TYPE AND A.CARGO_NO = D.CARGO_NO "
						+ "AND D.CARGO_REF IN(SELECT C.REFERENCE_NO FROM FMS_OTH_INVOICE_DTL C WHERE C.INVOICE_TYPE = E.INVOICE_TYPE AND C.CARGO_TYPE = E.CARGO_TYPE "
						+ "AND C.INVOICE_SEQ = E.INVOICE_SEQ "
						+ "AND C.FINANCIAL_YEAR = E.FINANCIAL_YEAR AND C.SUPPLIER_CD = E.SUPPLIER_CD AND C.INV_FLAG = E.INV_FLAG "
						+ "AND C.REFERENCE_NO IS NOT NULL) AND E.INVOICE_TYPE = ? AND E.CARGO_TYPE = ? AND E.INVOICE_SEQ = ? "
						+ "AND E.FINANCIAL_YEAR = ? AND E.SUPPLIER_CD = ? AND E.INV_FLAG = ? AND A.COMPANY_CD = E.COMPANY_CD AND D.CARGO_REF = E.REFERENCE_NO ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2,inv_type);
				stmt1.setString(3,cargo_type);
				stmt1.setString(4, inv_seq);
				stmt1.setString(5, fin_yr);
				stmt1.setString(6, supp_cd);
				stmt1.setString(7, "F");
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					queryString2 = "SELECT SHIP_NAME "
							+ "FROM FMS_SHIP_MST "
							+ "WHERE SHIP_CD  = ?";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1,rset1.getString(3)==null?"":rset1.getString(3));
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						VSHIP_NAME.add(rset2.getString(1)==null?"":rset2.getString(1));
					}
					else 
					{
						VSHIP_NAME.add("");
					}
					rset2.close();
					stmt2.close();
					
					VCARGO_REF.add(rset1.getString(1)==null?"":rset1.getString(1)); 
					VCARGO_DT.add(rset1.getString(2)==null?"":rset1.getString(2));
					VSHIP_CD.add(rset1.getString(3)==null?"":rset1.getString(3));
					VQTY_MMBTU.add(nf.format(Double.parseDouble(rset1.getString(4) == null ? "" : rset1.getString(4))));
					VCARGO_TYPE.add("SALES");
					VDIS_PORT.add(rset1.getString(5)==null?"":rset1.getString(5));
					double rate = rset1.getDouble(6);
					double cargo_amt = rset1.getDouble(7);
					if(Double.doubleToRawLongBits(rate)==Double.doubleToRawLongBits(0))
					{
						VRATE.add("");
					}
					else
					{
						VRATE.add(nf2.format(rate));
					}
					if(Double.doubleToRawLongBits(cargo_amt)==Double.doubleToRawLongBits(0))
					{
						VCARGO_AMT.add("");
					}
					else
					{
						VCARGO_AMT.add(nf.format(cargo_amt));
					}
				}
				rset1.close();
				stmt1.close();
				
				for (int i = 0; i < VMONTH_DATES.size(); i++) 
				{
					queryString = "SELECT D.CARGO_REF,TO_CHAR(A.QQ_DT,'DD-MON-YY'),A.SHIP_CD,NVL(A.QQ_QTY_MMBTU,'0'), 0, 0.00, 0.00 "
							+ "FROM FMS_BUY_CARGO_ALLOC A, FMS_TRADER_CARGO_MST D "
							+ "WHERE A.QQ_DT = TO_DATE(?, 'DD/MM/YYYY') AND A.QQ_QTY_MMBTU!='0' "
							+ "AND A.COMPANY_CD = D.COMPANY_CD AND A.COUNTERPARTY_CD = D.COUNTERPARTY_CD AND A.AGMT_TYPE = D.AGMT_TYPE "
							+ "AND A.AGMT_NO = D.AGMT_NO AND A.CONT_NO = D.CONT_NO AND A.CONTRACT_TYPE = D.CONTRACT_TYPE AND A.CARGO_NO = D.CARGO_NO "
							+ "AND D.CARGO_REF NOT IN(SELECT C.REFERENCE_NO FROM FMS_OTH_INVOICE_DTL C WHERE C.INVOICE_TYPE = ? AND C.CARGO_TYPE = ? "
							+ "AND C.REFERENCE_NO IS NOT NULL AND C.INV_FLAG = 'F') ";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1,""+VMONTH_DATES.elementAt(i));
					stmt.setString(2,inv_type);
					stmt.setString(3,cargo_type);
					rset=stmt.executeQuery();
					while(rset.next())
					{
						queryString2 = "SELECT SHIP_NAME "
								+ "FROM FMS_SHIP_MST "
								+ "WHERE SHIP_CD  = ?";
						stmt2=conn.prepareStatement(queryString2);
						stmt2.setString(1,rset.getString(3)==null?"":rset.getString(3));
						rset2=stmt2.executeQuery();
						if(rset2.next())
						{
							VSHIP_NAME.add(rset2.getString(1)==null?"":rset2.getString(1));
						}
						else 
						{
							VSHIP_NAME.add("");
						}
						rset2.close();
						stmt2.close();
						
						VCARGO_REF.add(rset.getString(1)==null?"":rset.getString(1)); 
						VCARGO_DT.add(rset.getString(2)==null?"":rset.getString(2));
						VSHIP_CD.add(rset.getString(3)==null?"":rset.getString(3));
						VQTY_MMBTU.add(nf.format(Double.parseDouble(rset.getString(4) == null ? "" : rset.getString(4))));
						VCARGO_TYPE.add("SALES");
						VDIS_PORT.add(rset.getString(5)==null?"":rset.getString(5));
						double rate = rset.getDouble(6);
						double cargo_amt = rset.getDouble(7);
						if(Double.doubleToRawLongBits(rate)==Double.doubleToRawLongBits(0))
						{
							VRATE.add("");
						}
						else
						{
							VRATE.add(nf2.format(rate));
						}
						if(Double.doubleToRawLongBits(cargo_amt)==Double.doubleToRawLongBits(0))
						{
							VCARGO_AMT.add("");
						}
						else
						{
							VCARGO_AMT.add(nf.format(cargo_amt));
						}
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
	
	public void getCargoDetailschkappr()
	{
		String function_nm="getCargoDetailschkappr()";
		try
		{
			if(cargo_type.equals("LTCORA"))
			{
					int count=0;
					queryString	= "SELECT A.CARGO_REF,TO_CHAR(A.QQ_DT,'DD-Mon-YY'),A.SHIP_CD,NVL(D.ADQ_QTY,'0'),B.SHIP_NAME,E.DISCHARGE_PORT,E.SALE_PRICE,E.CARGO_AMOUNT "
							+ "FROM FMS_LTCORA_CONT_CARGO_DTL A, FMS_SHIP_MST B, FMS_LTCORA_CONT_CARGO_ADQ D, FMS_OTH_INVOICE_DTL E "
							+ "WHERE A.SHIP_CD = B.SHIP_CD AND A.COMPANY_CD = ? AND D.ADQ_QTY!=0 AND A.BUY_SALE = 'C' "
							+ "AND A.COMPANY_CD = D.COMPANY_CD AND A.COUNTERPARTY_CD = D.COUNTERPARTY_CD AND A.BUY_SALE = D.BUY_SALE AND A.AGMT_TYPE = D.AGMT_TYPE "
							+ "AND A.AGMT_NO = D.AGMT_NO AND A.CONT_NO = D.CONT_NO AND A.CONTRACT_TYPE = D.CONTRACT_TYPE AND A.CARGO_NO = D.CARGO_NO  "
							+ "AND A.CARGO_REF IN (SELECT C.REFERENCE_NO FROM FMS_OTH_INVOICE_DTL C WHERE C.INVOICE_TYPE = E.INVOICE_TYPE AND C.CARGO_TYPE = E.CARGO_TYPE "
							+ "AND C.INVOICE_SEQ = E.INVOICE_SEQ "
							+ "AND C.FINANCIAL_YEAR = E.FINANCIAL_YEAR AND C.SUPPLIER_CD = E.SUPPLIER_CD AND C.INV_FLAG = E.INV_FLAG "
							+ "AND C.REFERENCE_NO IS NOT NULL) AND E.INVOICE_TYPE = ? AND E.CARGO_TYPE = ? AND E.INVOICE_SEQ = ? "
							+ "AND E.FINANCIAL_YEAR = ? AND E.SUPPLIER_CD = ? AND E.INV_FLAG = ? AND A.COMPANY_CD = E.COMPANY_CD AND A.CARGO_REF = E.REFERENCE_NO ";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(++count, comp_cd);
					stmt.setString(++count,inv_type);
					stmt.setString(++count,cargo_type);
					stmt.setString(++count, inv_seq);
					stmt.setString(++count, fin_yr);
					stmt.setString(++count, supp_cd);
					stmt.setString(++count, "F");
					rset=stmt.executeQuery();
					while(rset.next())
					{
						double rate = rset.getDouble(7);
						double cargo_amt = rset.getDouble(8);
						String port = rset.getString(6)==null?"":rset.getString(6);
						
						VCARGO_DT.add(rset.getString(2)==null?"":rset.getString(2));
						VQTY_MMBTU.add(es.formatAmount(""+Double.parseDouble(rset.getString(4) == null ? "" : rset.getString(4))));
						VSHIP_NAME.add(rset.getString(5)==null?"":rset.getString(5));
						
						if(port.equals("1"))
						{
							VDIS_PORT.add("Hazira");
						}
						else if(port.equals("2"))
						{
							VDIS_PORT.add("Dahej");
						}
						else 
						{
							VDIS_PORT.add("");
						}
						
						if(Double.doubleToRawLongBits(rate)==Double.doubleToRawLongBits(0))
						{
							VRATE.add("");
						}
						else
						{
							VRATE.add(nf2.format(rate));
						}
						if(Double.doubleToRawLongBits(cargo_amt)==Double.doubleToRawLongBits(0))
						{
							VCARGO_AMT.add("");
						}
						else
						{
							VCARGO_AMT.add(es.formatAmount(""+cargo_amt));
						}
					}
					rset.close();
					stmt.close();
			}
			else if(cargo_type.equals("SALES"))
			{
				queryString1 = "SELECT D.CARGO_REF,TO_CHAR(A.QQ_DT,'DD-Mon-YY'),E.VESSEL_CD,NVL(A.QQ_QTY_MMBTU,'0'),E.DISCHARGE_PORT,E.SALE_PRICE,E.CARGO_AMOUNT "
						+ "FROM FMS_BUY_CARGO_ALLOC A, FMS_TRADER_CARGO_MST D, FMS_OTH_INVOICE_DTL E  "
						+ "WHERE A.QQ_QTY_MMBTU!='0' AND A.COMPANY_CD = ? "
						+ "AND A.COMPANY_CD = D.COMPANY_CD AND A.COUNTERPARTY_CD = D.COUNTERPARTY_CD AND A.AGMT_TYPE = D.AGMT_TYPE "
						+ "AND A.AGMT_NO = D.AGMT_NO AND A.CONT_NO = D.CONT_NO AND A.CONTRACT_TYPE = D.CONTRACT_TYPE AND A.CARGO_NO = D.CARGO_NO "
						+ "AND D.CARGO_REF IN(SELECT C.REFERENCE_NO FROM FMS_OTH_INVOICE_DTL C WHERE C.INVOICE_TYPE = E.INVOICE_TYPE AND C.CARGO_TYPE = E.CARGO_TYPE "
						+ "AND C.INVOICE_SEQ = E.INVOICE_SEQ "
						+ "AND C.FINANCIAL_YEAR = E.FINANCIAL_YEAR AND C.SUPPLIER_CD = E.SUPPLIER_CD AND C.INV_FLAG = E.INV_FLAG "
						+ "AND C.REFERENCE_NO IS NOT NULL) AND E.INVOICE_TYPE = ? AND E.CARGO_TYPE = ? AND E.INVOICE_SEQ = ? "
						+ "AND E.FINANCIAL_YEAR = ? AND E.SUPPLIER_CD = ? AND E.INV_FLAG = ? AND A.COMPANY_CD = E.COMPANY_CD AND D.CARGO_REF = E.REFERENCE_NO ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2,inv_type);
				stmt1.setString(3,cargo_type);
				stmt1.setString(4, inv_seq);
				stmt1.setString(5, fin_yr);
				stmt1.setString(6, supp_cd);
				stmt1.setString(7, "F");
				rset1=stmt1.executeQuery();
				while(rset1.next())
				{
					queryString2 = "SELECT SHIP_NAME "
							+ "FROM FMS_SHIP_MST "
							+ "WHERE SHIP_CD  = ?";
					stmt2=conn.prepareStatement(queryString2);
					stmt2.setString(1,rset1.getString(3)==null?"":rset1.getString(3));
					rset2=stmt2.executeQuery();
					if(rset2.next())
					{
						VSHIP_NAME.add(rset2.getString(1)==null?"":rset2.getString(1));
					}
					else 
					{
						VSHIP_NAME.add("");
					}
					rset2.close();
					stmt2.close();
					
					double rate = rset1.getDouble(6);
					double cargo_amt = rset1.getDouble(7);
					String port = rset1.getString(5)==null?"":rset1.getString(5);
					
					if(Double.doubleToRawLongBits(rate)==Double.doubleToRawLongBits(0))
					{
						VRATE.add("");
					}
					else
					{
						VRATE.add(nf2.format(rate));
					}
					if(Double.doubleToRawLongBits(cargo_amt)==Double.doubleToRawLongBits(0))
					{
						VCARGO_AMT.add("");
					}
					else
					{
						VCARGO_AMT.add(es.formatAmount(""+cargo_amt));
					}

					if(port.equals("1"))
					{
						VDIS_PORT.add("Hazira");
					}
					else if(port.equals("2"))
					{
						VDIS_PORT.add("Dahej");
					}
					else 
					{
						VDIS_PORT.add("");
					}
					
					VCARGO_DT.add(rset1.getString(2)==null?"":rset1.getString(2));
					VQTY_MMBTU.add(es.formatAmount(""+Double.parseDouble(rset1.getString(4) == null ? "" : rset1.getString(4))));
				}
				rset1.close();
				stmt1.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getInvoiceDtlsNPR()
	{	
		String function_nm="getInvoiceDtlsNPR()";

		try
		{
			double taxValue = 0;
			String tax_value="";
			queryString = "SELECT A.COMPANY_CD,A.VENDOR_CD,A.SUPPLIER_CD,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),B.VENDOR_SUPP_INV_REF,"
					+ "B.PACER_NO,A.GROSS_AMT,A.TAX_AMT_INR,A.NET_PAYABLE,A.EXCHG_RATE_VALUE,TO_CHAR(A.EXCHG_RATE_DT, 'DD/MM/YYYY'),"
					+ "A.SALE_PRICE_UNIT,B.SAC_CODE,B.TAX_STRUCT_CD,B.ITEM_DESCRIPTION,A.REMARK,A.REMARK1,A.APPROVED_FLAG,A.FINANCIAL_YEAR,"
					+ "A.INVOICE_SEQ,A.SALE_AMT,A.CHECKED_FLAG,A.INVOICE_NO,A.INVOICE_ID_SEQ,TO_CHAR(B.TAX_EFF_DT, 'DD/MM/YYYY'),A.INVOICE_CATEGORY,A.INVOICE_RAISED_IN "
					+ "FROM FMS_OTH_INVOICE_MST A, FMS_OTH_INVOICE_DTL B "
					+ "WHERE A.COMPANY_CD = ? AND A.INVOICE_SEQ = ? AND A.INVOICE_TYPE = ? AND A.FINANCIAL_YEAR=? AND A.SUPPLIER_CD=? AND A.INV_FLAG = ? "
					+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.INVOICE_TYPE = B.INVOICE_TYPE "
					+ "AND A.VENDOR_CD = B.VENDOR_CD AND A.SUPPLIER_CD = B.SUPPLIER_CD AND A.INVOICE_SEQ = B.INVOICE_SEQ AND A.INV_FLAG = B.INV_FLAG";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_seq);
			stmt.setString(3, inv_type);
			stmt.setString(4, fin_yr);
			stmt.setString(5, supp_cd);
			stmt.setString(6, "F");
			rset = stmt.executeQuery();
			if(rset.next()) 
			{
				submission_chk = true;
				
				vendor_cd= rset.getString(2) == null ? "" : rset.getString(2);
				supp_cd= rset.getString(3) == null ? "" : rset.getString(3);				
				inv_dt = rset.getString(4) == null ? "" : rset.getString(4);
				ref_no = rset.getString(5) == null ? "" : rset.getString(5);
				pacer_no = rset.getString(6) == null ? "" : rset.getString(6);				
				gross_amt = nf.format(Double.parseDouble(rset.getString(7) == null ? "" : rset.getString(7)));
				total_gst = nf.format(Double.parseDouble(rset.getString(8) == null ? "" : rset.getString(8)));
				net_amt = nf.format(Double.parseDouble(rset.getString(9) == null ? "" : rset.getString(9)));
				exchng_rate = rset.getString(10) == null ? "" : rset.getString(10);
				exchng_eff_dt = rset.getString(11) == null ? "" : rset.getString(11);
				currency = rset.getString(12) == null ? "" : rset.getString(12);
				sac_cd = rset.getString(13) == null ? "" : rset.getString(13);
				tax_struct_cd = rset.getString(14) == null ? "" : rset.getString(14);
				tax_struct_dt = rset.getString(25) == null ? "" : rset.getString(25);
				desc_item = rset.getString(15) == null ? "" : rset.getString(15);
				remark1 = rset.getString(16) == null ? "" : rset.getString(16);
				remark2 = rset.getString(17) == null ? "" : rset.getString(17);
				if(opration.equals("CHECK"))
				{
					activity_value = rset.getString(22) == null ? "" : rset.getString(22);
				}
				else if(opration.equals("APPROVE"))
				{
					activity_value = rset.getString(18) == null ? "" : rset.getString(18);
				}
				fin_yr = rset.getString(19) == null ? "" : rset.getString(19);
				inv_seq = rset.getString(20) == null ? "" : rset.getString(20);
				sale_amt = nf.format(Double.parseDouble(rset.getString(21) == null ? "" : rset.getString(21)));
				inv_no = rset.getString(23) == null ? "" : rset.getString(23);
				inv_id_seq = rset.getString(24) == null ? "" : rset.getString(24);
				invoice_category = rset.getString(26) == null ? "" : rset.getString(26);
				invoice_raised_in = rset.getString(27) == null ? "" : rset.getString(27);
				
				taxValue = rset.getDouble(8);
				queryString2 = "SELECT SAC_CODE,SAC_DESC "
						+ "FROM FMS_SAC_MST A "
						+ "WHERE SAC_CD  = ?";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1,sac_cd);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					sac_code = rset2.getString(1)==null?"":rset2.getString(1);	
					sac_des = rset2.getString(2)==null?"":rset2.getString(2);	
				}
				rset2.close();
				stmt2.close();
				
				tax_struct_info=utilBean.getTaxDescr(conn,tax_struct_cd);
				
				if (tax_struct_info.contains(",")) 
				{
					tax_value = nf.format(Double.parseDouble((Double.doubleToRawLongBits(taxValue)==Double.doubleToRawLongBits(0)) ? "0" : String.format("%.2f", taxValue / 2)));
				} 
				else 
				{
					tax_value = nf.format(Double.parseDouble((Double.doubleToRawLongBits(taxValue)==Double.doubleToRawLongBits(0)) ? "0" : String.format("%.2f", taxValue)));
				}	
			
				tax_amt=nf.format(taxValue);
				
				
				//if(!opration.equals("MODIFY")) 
				{
					int srno=0;
					String currency_nm ="";
					if(currency.equals("1")) 
					{
						currency_nm = "INR";
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add(desc_item);
						VPDF_COL3.add(currency_nm);
						VPDF_COL4.add(es.formatAmount(sale_amt));
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("<b>Total Amount</b>");
						VPDF_COL3.add("<b>"+currency_nm+"</b>");
						VPDF_COL4.add("<b>"+es.formatAmount(gross_amt)+"</b>");
						
						srno+=1;
						
					}
					else if(currency.equals("2")) 
					{
						currency_nm = "USD";
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add(desc_item);
						VPDF_COL3.add(currency_nm);
						VPDF_COL4.add(es.formatAmount(sale_amt));
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("Exchange Rate : "+exchng_rate+" (INR/USD) dated "+exchng_eff_dt);
						VPDF_COL3.add("INR/USD");
						VPDF_COL4.add(exchng_rate);
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("<b>Net Amount</b>");
						VPDF_COL3.add("<b>INR</b>");
						VPDF_COL4.add("<b>"+es.formatAmount(gross_amt)+"</b>");
						
						srno+=1;
					}
					
					double temp_srno=srno;
					Vector VTAX_CODE = new Vector();
					Vector VTAX_DESCR = new Vector();
					Vector VTAX_AMT = new Vector();
					if (tax_struct_info.contains(",")) 
					{

		            	BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_amt));
						BigDecimal factor = new BigDecimal(2);
						for(int i = 0;i<tax_struct_info.split(",").length;i++) 
						{
							BigDecimal sub_tax_amt = tax_amt1.divide(factor, 0, RoundingMode.HALF_UP);
							temp_srno=temp_srno+0.1;
							VPDF_COL1.add(nf0.format(temp_srno));
							VPDF_COL2.add(tax_struct_info.split(",")[i]);
							VPDF_COL3.add("INR");
							VPDF_COL4.add(es.formatAmount(""+Double.parseDouble(""+sub_tax_amt)));
							
							VTAX_CODE.add(tax_struct_cd);
							VTAX_DESCR.add(tax_struct_info.split(",")[i]);
							VTAX_AMT.add(nf.format(sub_tax_amt));
						}
						Vector VTEMP_TAX_DTL = new Vector();
						
						VTEMP_TAX_DTL.add(VTAX_CODE);
						VTEMP_TAX_DTL.add(VTAX_DESCR);
						VTEMP_TAX_DTL.add(VTAX_AMT);
						
						VMULTI_TAX_STRUCT.add(VTEMP_TAX_DTL);
					} 
					
					VPDF_COL1.add(srno);
					VPDF_COL2.add("<b>Tax ("+tax_struct_info+")</b>");
					VPDF_COL3.add("<b>INR</b>");
					VPDF_COL4.add("<b>"+es.formatAmount(tax_amt)+"</b>");

					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("<b>Total Amount in INR</b>");
					VPDF_COL3.add("<b>INR</b>");
					VPDF_COL4.add("<b>"+es.formatAmount(net_amt)+"</b>");
				}
				
				if(!net_amt.equalsIgnoreCase(""))		
				{
					amt_in_word=es.convert(Double.parseDouble(net_amt));
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
	
	//AP20251112
	public void getInvoiceTypeforCRDR()
	{	
		String function_nm="getInvoiceTypeforCRDR()";

		try
		{
			VINVOICE_TYPE.add("COSTR");
			VINVOICE_TYPE.add("COSTRH");//AR20260221
			VINVOICE_TYPE.add("HSA");
			VINVOICE_TYPE.add("HS");
			VINVOICE_TYPE.add("SFA");
			VINVOICE_TYPE.add("AHPL");
		
			VINVOICE_TYPE_NM.add("RCM Invoice(Cost Recharge)");
			VINVOICE_TYPE_NM.add("Cost Recharge HPPL");//AR20260221
			VINVOICE_TYPE_NM.add("Berthing Invoice (HPPL Shipping Agent)");
			VINVOICE_TYPE_NM.add("PFA Fees Invoice (HPPL-SEIPL)");
			VINVOICE_TYPE_NM.add("Scrap Fixed Asset");
			VINVOICE_TYPE_NM.add("AHPL Invoice (AHPL Revenue Share)");
			
			VINVOICE_LIST_NM.add("frm_oth_inv_cost_recharge_crdr.jsp");
			VINVOICE_LIST_NM.add("frm_other_inv_cost_recharge_hppl_crdr.jsp");//AR20260221
			VINVOICE_LIST_NM.add("frm_oth_inv_hsa_crdr.jsp");
			VINVOICE_LIST_NM.add("frm_oth_inv_hppl_seipl_crdr.jsp");
			VINVOICE_LIST_NM.add("frm_oth_inv_scrap_fixed_asset_crdr.jsp");
			VINVOICE_LIST_NM.add("frm_oth_inv_ahpl_crdr.jsp");
			
			VCRDR_CHECK_APPROVE.add("rpt_view_chk_aprv_COSTR_crdr_dtl.jsp");
			VCRDR_CHECK_APPROVE.add("rpt_view_chk_aprv_COSTRH_crdr_dtl.jsp");//20260221
			VCRDR_CHECK_APPROVE.add("rpt_view_chk_aprv_HSA_crdr_dtl.jsp");
			VCRDR_CHECK_APPROVE.add("rpt_view_chk_aprv_PFA_crdr_dtl.jsp");
			VCRDR_CHECK_APPROVE.add("rpt_view_chk_aprv_SFA_crdr_dtl.jsp");
			VCRDR_CHECK_APPROVE.add("rpt_view_chk_aprv_AHPL_crdr_dtl.jsp");
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//AP20251112
	public void getOtherInvCRDRList()
	{	
		String function_nm="getOtherInvCRDRList()";

		try
		{
			for (int i = 0; i < VINVOICE_TYPE.size(); i++ ) 
			{
				int index = 0;
				entity_role="V";
				
				if(VINVOICE_TYPE.elementAt(i).equals("HS") || VINVOICE_TYPE.elementAt(i).equals("COSTRH"))
				{
					entity_role="S";
				}
				
				queryString = "SELECT B.ENTITY_NAME, A.INVOICE_NO, TO_CHAR(A.INVOICE_DT, 'DD/MM/YYYY'), A.INVOICE_TYPE, A.VENDOR_CD, A.PDF_INV_DTL, A.CHECKED_FLAG, "
						+ "A.APPROVED_FLAG, A.PRINT_BY_ORI, A.PRINT_BY_DUP, A.INVOICE_SEQ, A.FINANCIAL_YEAR, A.SUPPLIER_CD,A.VENDOR_CD,A.INV_FLAG,A.REF_NO,A.SAP_APPROVAL, "
						+ "A.CRITERIA "
						+ "FROM FMS_OTH_INVOICE_MST A, FMS_OTH_ENTITY_MST B "
						+ " WHERE A.VENDOR_CD = B.ENTITY_CD AND TO_CHAR(A.INVOICE_DT, 'MM/YYYY') = ? AND A.COMPANY_CD = ? AND "
						+ " B.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS_OTH_ENTITY_MST C WHERE A.VENDOR_CD = C.ENTITY_CD AND B.ENTITY_TYPE=C.ENTITY_TYPE) "
						+ "AND A.INVOICE_TYPE = ? AND B.ENTITY_TYPE = ? AND A.INV_FLAG IN ('CR','DR') "
						+ "ORDER BY INVOICE_SEQ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, month+"/"+year);
				stmt.setString(2, comp_cd);
				stmt.setString(3, ""+VINVOICE_TYPE.elementAt(i));
				stmt.setString(4, entity_role);
				rset = stmt.executeQuery();
				while (rset.next()) 
				{
					index++;	
					VVENDOR_NAME.add(rset.getString(1) == null ? "" : rset.getString(1));
					VCRDR_NO.add(rset.getString(2) == null ? "" : rset.getString(2));
					VINVOICE_DT.add(rset.getString(3) == null ? "" : rset.getString(3));
					VINV_TYPE.add(rset.getString(4) == null ? "" : rset.getString(4));
					VENDORCD.add(rset.getString(5) == null ? "" : rset.getString(5));
					VCHECK_FLAG.add(rset.getString(7) == null ? "" : rset.getString(7));
					VAPPROVE_FLAG.add(rset.getString(8) == null ? "" : rset.getString(8));
					String pdf_ori = rset.getString(9) == null ? "" : rset.getString(9);
					String pdf_dup = rset.getString(10) == null ? "" : rset.getString(10);
					
					String invoice_no = rset.getString(2) == null ? "" : rset.getString(2);
					String invoice_type = rset.getString(4) == null ? "" : rset.getString(4);
					String invoice_seq = rset.getString(11) == null ? "" : rset.getString(11);
					String fiscal_yr = rset.getString(12) == null ? "" : rset.getString(12); 
					String supplier_cd = rset.getString(13) == null ? "" : rset.getString(13); 
					String vendor_cd = rset.getString(14) == null ? "" : rset.getString(14); 
					String inv_flag = rset.getString(15) == null ? "" : rset.getString(15); 
					String ref_inv_no = rset.getString(16) == null ? "" : rset.getString(16); 
					String sun_appr = rset.getString(17) == null ? "" : rset.getString(17); 
					String criteria = rset.getString(18) == null ? "" : rset.getString(18); 
					
					VSUPPLIER_CD.add(supplier_cd);
					VVENDOR_CD.add(vendor_cd);
					VINVOICE_SEQ.add(invoice_seq);
					VFIN_YEAR.add(fiscal_yr);
					VINV_FLAG.add(inv_flag);
					VREF_INV_NO.add(ref_inv_no);
					VSUN_APPROVAL_FLAG.add(sun_appr);
					VCRDR_CRITERIA.add(criteria);
					if(inv_flag.equals("CR"))
					{
						VCRDR_TYPE.add("Credit Note");
					}
					else 
					{
						VCRDR_TYPE.add("Debit Note");
					}
					
					String queryString1="SELECT A.ENTITY_NAME "
							+ "FROM FMS_OTH_ENTITY_MST A "
							+ "WHERE A.ENTITY_CD=? AND A.ENTITY_TYPE = 'S' "
							+ "AND A.EFF_DT = (SELECT MAX(C.EFF_DT) FROM FMS_OTH_ENTITY_MST C WHERE A.ENTITY_CD = C.ENTITY_CD AND A.ENTITY_TYPE=C.ENTITY_TYPE) ";
					stmt1 = conn.prepareStatement(queryString1);
					stmt1.setString(1, supplier_cd);
					rset1 = stmt1.executeQuery();
					if (rset1.next()) 
					{
						VSUPPLIER_NM.add(rset1.getString(1) == null ? "" : rset1.getString(1));
					}
					rset1.close();
					stmt1.close();
					
					if(print_pdf_type.equals("O") && !pdf_ori.equals(""))
					{
						VPDF_TYPE.add(print_pdf_type);
					}
					else if(print_pdf_type.equals("D") && !pdf_dup.equals(""))
					{
						VPDF_TYPE.add(print_pdf_type);
					}
					else if(print_pdf_type.equals("All"))
					{
						String allPdfType="";
						allPdfType+=pdf_ori.equals("")?allPdfType.equals("")?"O":",O":"";
						allPdfType+=pdf_dup.equals("")?allPdfType.equals("")?"D":",D":"";
						
						if(allPdfType.equals(""))
						{
							allPdfType="All";
						}
						VPDF_TYPE.add(allPdfType);
					}
					else
					{
						VPDF_TYPE.add("");
					}
					
					if(view_pdf_type.equals("All"))
					{
						queryString6="SELECT COUNT(*) "
								+ "FROM FMS_OTH_INV_FILE_DTL "
								+ "WHERE COMPANY_CD=? AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? "
								+ "AND INVOICE_TYPE = ? AND SUPPLIER_CD = ? "
			 	        		+ "AND PDF_TYPE!=?";
						stmt6=conn.prepareStatement(queryString6);
						stmt6.setString(1, comp_cd);
						stmt6.setString(2, invoice_seq);
						stmt6.setString(3, fiscal_yr);
						stmt6.setString(4, invoice_type);
						stmt6.setString(5, supplier_cd);
						stmt6.setString(6, "X");
						rset6=stmt6.executeQuery();
						if(rset6.next())
						{
							if(rset6.getInt(1)>0)
							{
								VPDF_FILE_NAME.add("All");
								VPDF_FILE_PATH.add("");
							}
							else
							{
								VPDF_FILE_NAME.add("");
								VPDF_FILE_PATH.add("");
							}
						}
						else
						{
							VPDF_FILE_NAME.add("");
							VPDF_FILE_PATH.add("");
						}
						rset6.close();
						stmt6.close();
					}
					else
					{
						queryString6="SELECT FILE_NAME,PDF_SIGNED "
								+ "FROM FMS_OTH_INV_FILE_DTL "
								+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? "
			 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND SUPPLIER_CD = ? "
			 	        		+ "AND PDF_TYPE=?";
						stmt6=conn.prepareStatement(queryString6);
						stmt6.setString(1, comp_cd);
						stmt6.setString(2, invoice_type);
						stmt6.setString(3, invoice_seq);
						stmt6.setString(4, fiscal_yr);
						stmt6.setString(5, supplier_cd);
						stmt6.setString(6, view_pdf_type);
						rset6=stmt6.executeQuery();
						if(rset6.next())
						{
							String pdf_signed=rset6.getString(2)==null?"":rset6.getString(2);
							
							if(pdf_signed.equals("Y"))
							{
								VPDF_FILE_NAME.add(rset6.getString(1)==null?"":rset6.getString(1));
								VPDF_FILE_PATH.add(CommonVariable.signed_ig_inv_path);
							}
							else
							{
								VPDF_FILE_NAME.add(rset6.getString(1)==null?"":rset6.getString(1));
								VPDF_FILE_PATH.add(CommonVariable.ig_inv_path);
							}
						}
						else
						{
							VPDF_FILE_NAME.add("");
							VPDF_FILE_PATH.add("");
						}
						rset6.close();
						stmt6.close();
					}
					
					if(sun_appr.equals("Y"))
					{
						queryString5="SELECT FILE_NAME "
								+ "FROM FMS_OTH_INV_FILE_DTL "
								+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? "
			 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND SUPPLIER_CD = ? "
			 	        		+ "AND PDF_TYPE=?";
						stmt5=conn.prepareStatement(queryString5);
						stmt5.setString(1, comp_cd);
						stmt5.setString(2, invoice_type);
						stmt5.setString(3, invoice_seq);
						stmt5.setString(4, fiscal_yr);
						stmt5.setString(5, supplier_cd);
						stmt5.setString(6, "S");
						rset5=stmt5.executeQuery();
						if(rset5.next())
						{
							VXML_FILE_NAME.add(rset5.getString(1)==null?"":rset5.getString(1));
							VXML_FILE_PATH.add(CommonVariable.sun_xml);
						}
						else 
						{
							VXML_FILE_NAME.add("");
							VXML_FILE_PATH.add("");
						}
						rset5.close();
						stmt5.close();
					}
					else 
					{
						VXML_FILE_NAME.add("");
						VXML_FILE_PATH.add("");
					}
					
					
					String irn_no="";
					queryString5="SELECT IRN_NO "
							+ "FROM FMS_OTH_INV_IRN_DTL "
							+ "WHERE COMPANY_CD=? AND INVOICE_NO=? ";
					stmt5=conn.prepareStatement(queryString5);
					stmt5.setString(1, comp_cd);
					stmt5.setString(2, invoice_no);
					rset5=stmt5.executeQuery();
					if(rset5.next())
					{
						irn_no=rset5.getString(1)==null?"":rset5.getString(1);
					}
					rset5.close();
					stmt5.close();
					
					VIS_IRN_GENERATED.add(irn_no.equals("")?"N":"Y");
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
	
	//AP20251115
	public void getInvoiceList()
	{	
		String function_nm="getInvoiceList()";

		try
		{
			if(opration.equals("INSERT"))
			{
				queryString = "SELECT INVOICE_NO "
						+ "FROM FMS_OTH_INVOICE_MST A "
						+ "WHERE TO_CHAR(INVOICE_DT, 'MM/YYYY') = ? AND COMPANY_CD = ? AND INVOICE_TYPE=? "
						+ "AND INVOICE_NO IS NOT NULL AND INV_FLAG='F' ";
				/*
				 * if(opration.equals("PREPARE")) { queryString
				 * +="AND (SELECT COUNT(*) FROM FMS_OTH_INVOICE_MST B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.INVOICE_TYPE=B.INVOICE_TYPE AND A.INVOICE_NO=B.REF_NO)=0 "
				 * ; }
				 */
				queryString += "ORDER BY INVOICE_NO";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, month+"/"+year);
				stmt.setString(2, comp_cd);
				stmt.setString(3, inv_type);
				rset = stmt.executeQuery();
				while (rset.next()) 
				{
					VSEL_INVOICE_NO.add(rset.getString(1)==null?"":rset.getString(1));	
				}
				rset.close();
				stmt.close();
			}
			else if(opration.equals("MODIFY"))
			{
				VSEL_INVOICE_NO.add(sel_inv_no);
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//AP20251115
	public void getCriteriaList()
	{
		String function_nm="getCriteriaList()";

		try
		{
			String pricecd ="",querystring="";
			querystring = "SELECT SALE_PRICE_UNIT "
					+ "FROM FMS_OTH_INVOICE_MST "
					+ "WHERE INVOICE_NO = ? AND INVOICE_TYPE = 'COSTR' ";
			stmt = conn.prepareStatement(querystring);
			stmt.setString(1,sel_inv_no);
			rset = stmt.executeQuery();
			if(rset.next())
			{
				pricecd = rset.getString(1);
			}
			else
			{
				pricecd = "";	
			}
			rset.close();
			stmt.close();
			
			if(inv_type.equals("HSA"))
			{
				VCRITERIA_FLAG.add("RATE");
				VCRITERIA_NAME.add("Change in RATE");
				VCRITERIA_HIDE.add("N");
				
				VCRITERIA_FLAG.add("GRT");
				VCRITERIA_NAME.add("Change in GRT");
				VCRITERIA_HIDE.add("N");
				
				VCRITERIA_FLAG.add("HRS");
				VCRITERIA_NAME.add("Change in HRS");
				VCRITERIA_HIDE.add("N");
			}
			else if(inv_type.equals("HS"))
			{
				VCRITERIA_FLAG.add("RATE");
				VCRITERIA_NAME.add("Change in Rate");
				VCRITERIA_HIDE.add("N");
				
				VCRITERIA_FLAG.add("EXCHG");
				VCRITERIA_NAME.add("Change in Exchange Rate");
				VCRITERIA_HIDE.add("N");
				
				VCRITERIA_FLAG.add("TAXP");
				VCRITERIA_NAME.add("Change in Tax %");
				VCRITERIA_HIDE.add("N");
				
				VCRITERIA_FLAG.add("QTY");
				VCRITERIA_NAME.add("Change in Quantity");
				VCRITERIA_HIDE.add("N");

			}
			else if(inv_type.equals("AHPL") || inv_type.equals("COSTRH") || inv_type.equals("COSTR"))
			{
				VCRITERIA_FLAG.add("GAMT");
				VCRITERIA_NAME.add("Change in Gross Amount");
				VCRITERIA_HIDE.add("N");
				
				if(inv_type.equals("COSTR"))
				{
					VCRITERIA_FLAG.add("EXCHG");
					VCRITERIA_NAME.add("Change in Exchange Rate");
					VCRITERIA_HIDE.add(pricecd.equals("1")?"Y":"N");
				}
				
				VCRITERIA_FLAG.add("TAXP");
				VCRITERIA_NAME.add("Change in Tax %");
				VCRITERIA_HIDE.add("N");
			}
			else if(inv_type.equals("SFA"))
			{
				VCRITERIA_FLAG.add("QTY");
				VCRITERIA_NAME.add("Change in Quantity");
				VCRITERIA_HIDE.add("N");
				
				VCRITERIA_FLAG.add("RATE");
				VCRITERIA_NAME.add("Change in Rate");
				VCRITERIA_HIDE.add("N");
				
				VCRITERIA_FLAG.add("TAXP");
				VCRITERIA_NAME.add("Change in Tax %");
				VCRITERIA_HIDE.add("N");
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//AP20251115
	public void getSelectedInvoiceDtl()
	{
		String function_nm="getSelectedInvoiceDtl()";

		try
		{
			BigDecimal factor = new BigDecimal(1);
			queryString = "SELECT SUPPLIER_CD,VENDOR_CD,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'),GROSS_AMT,TAX_AMT_INR,NET_PAYABLE,"
					+ "INVOICE_RAISED_IN,REMARK,REMARK1,REMARK2,REMARK3 "
					+ "FROM FMS_OTH_INVOICE_MST "
					+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND INVOICE_NO=? "
					+ "AND INVOICE_NO IS NOT NULL AND INV_FLAG='F' ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_type);
			stmt.setString(3, sel_inv_no);
			rset = stmt.executeQuery();
			while (rset.next()) 
			{
				supp_cd = rset.getString(1)==null?"":rset.getString(1);
				vendor_cd = rset.getString(2)==null?"":rset.getString(2);
				supp_nm=getEntity_nm(supp_cd,"S");
				vendor_name=getEntity_nm(vendor_cd,"V");
				main_invoice_dt=rset.getString(3)==null?"":rset.getString(3);
				main_invoice_due_dt=rset.getString(4)==null?"":rset.getString(4);
				double grossAmt=rset.getDouble(5);
				double taxAmt=rset.getDouble(6);
				double netPayable=rset.getDouble(7);
				invoice_raised_in=rset.getString(8)==null?"":rset.getString(8);
				invoice_raised_in_nm=utilBean.getRateUnitNm(conn, invoice_raised_in);
				remark1=rset.getString(9)==null?"":rset.getString(9);
				remark2=rset.getString(10)==null?"":rset.getString(10);
				remark3=rset.getString(11)==null?"":rset.getString(11);
				remark4=rset.getString(12)==null?"":rset.getString(12);
				
				BigDecimal grossAmt1 = BigDecimal.valueOf(grossAmt);
				BigDecimal sub_grossAmt1 = grossAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal taxAmt1 = BigDecimal.valueOf(taxAmt);
				BigDecimal sub_taxAmt1 = taxAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal netPayable1 = BigDecimal.valueOf(netPayable);
				BigDecimal sub_netPayable1 = netPayable1.divide(factor, 0, RoundingMode.HALF_UP);
				
				main_gross_amt=nf.format(sub_grossAmt1);				
				main_tax_amt=nf.format(sub_taxAmt1);				
				main_net_payable=nf.format(sub_netPayable1);	
				
				queryString1 = "SELECT GRT,QUANTITY,HRS_BERTHING,TIME_SLOTS_BERTHING,SALE_PRICE,SALE_PRICE_UNIT,TAX_STRUCT_CD,"
						+ "VESSEL_CD,VESSEL_AGENT,IMPORTER,SAC_CODE "
						+ "FROM FMS_OTH_INVOICE_DTL "
						+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND INVOICE_NO=? "
						+ "AND INVOICE_NO IS NOT NULL AND INV_FLAG='F' ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, inv_type);
				stmt1.setString(3, sel_inv_no);
				rset1 = stmt1.executeQuery();
				while(rset1.next()) 
				{
					double tmp_grt=rset1.getDouble(1);
					double qtyMmbtu=rset1.getDouble(2);
					main_berthing_hrs=rset1.getString(3)==null?"":rset1.getString(3);
					main_berthing_slots=rset1.getString(4)==null?"":rset1.getString(4);
					double tmp_rate=rset1.getDouble(5);
					price_cd=rset1.getString(6)==null?"":rset1.getString(6);
					price_cd_nm=utilBean.getRateUnitNm(conn, price_cd);
					main_tax_struct_cd=rset1.getString(7)==null?"":rset1.getString(7);
					main_tax_struct_info=utilBean.getTaxDescr(conn,main_tax_struct_cd);
					vessel_cd=rset1.getString(8)==null?"":rset1.getString(8);
					importer_nm=rset1.getString(10)==null?"":rset1.getString(10);
					vessel_agent_nm=rset1.getString(9)==null?"":rset1.getString(9);
					sac_cd=rset1.getString(11)==null?"":rset1.getString(11);
					
					BigDecimal tmp_grt1 = BigDecimal.valueOf(tmp_grt);
					BigDecimal sub_tmp_grt1 = tmp_grt1.divide(factor, 0, RoundingMode.HALF_UP);
					
					BigDecimal qtyMmbtu1 = BigDecimal.valueOf(qtyMmbtu);
					BigDecimal sub_qtyMmbtu1 = qtyMmbtu1.divide(factor, 0, RoundingMode.HALF_UP);
					
					main_grt=nf.format(sub_tmp_grt1);				
					main_qty_mmbtu=nf.format(sub_qtyMmbtu1);				
					main_rate=nf2.format(tmp_rate);	
				}
				rset1.close();
				stmt1.close();
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//AP20251115
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
	
	//AP20251118
	public void getCrDrInvoiceDtl()
	{
		String function_nm="getCrDrInvoiceDtl()";
		try
		{
			BigDecimal factor = new BigDecimal(1);
			queryString = "SELECT SUPPLIER_CD,VENDOR_CD,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'),GROSS_AMT,TAX_AMT_INR,NET_PAYABLE,"
					+ "INVOICE_RAISED_IN,INV_FLAG,CRITERIA,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),REMARK,FINANCIAL_YEAR,"
					+ "APPROVED_FLAG,CHECKED_FLAG,INVOICE_NO,INVOICE_ID_SEQ "
					+ "FROM FMS_OTH_INVOICE_MST "
					+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
					+ "AND INVOICE_SEQ=? "
					+ "AND INV_FLAG IN ('CR','DR') ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_type);
			stmt.setString(3, fin_yr);
			stmt.setString(4, inv_seq);
			rset = stmt.executeQuery();
			while (rset.next()) 
			{
				supp_cd = rset.getString(1)==null?"":rset.getString(1);
				vendor_cd = rset.getString(2)==null?"":rset.getString(2);
				supp_nm=getEntity_nm(supp_cd,"S");
				vendor_name=getEntity_nm(vendor_cd,"V");
				crdr_invoice_dt=rset.getString(3)==null?"":rset.getString(3);
				crdr_invoice_due_dt=rset.getString(4)==null?"":rset.getString(4);
				double grossAmt=rset.getDouble(5);
				double taxAmt=rset.getDouble(6);
				double netPayable=rset.getDouble(7);
				invoice_raised_in=rset.getString(8)==null?"":rset.getString(8);
				invoice_raised_in_nm=utilBean.getRateUnitNm(conn, invoice_raised_in);
				cr_dr_type=rset.getString(9)==null?"":rset.getString(9);
				criteri_formula=rset.getString(10)==null?"":rset.getString(10);
				period_start_dt=rset.getString(11)==null?"":rset.getString(11);
				period_end_dt=rset.getString(12)==null?"":rset.getString(12);
				crdr_remark=rset.getString(13)==null?"":rset.getString(13);
				financial_year=rset.getString(14)==null?"":rset.getString(14);
				inv_no=rset.getString(17)==null?"":rset.getString(17);
				inv_id_seq=rset.getString(18)==null?"":rset.getString(18);
				
				if(callFlag.equalsIgnoreCase("HSA_CRDR_APROVAL"))
				{
					if(Double.doubleToRawLongBits(grossAmt)<Double.doubleToRawLongBits(0))
					{
						grossAmt=(-1)*grossAmt;
					}
					if(Double.doubleToRawLongBits(taxAmt)<Double.doubleToRawLongBits(0))
					{
						taxAmt=(-1)*taxAmt;
					}
					if(Double.doubleToRawLongBits(netPayable)<Double.doubleToRawLongBits(0))
					{
						netPayable=(-1)*netPayable;
					}
				}
				
				BigDecimal grossAmt1 = BigDecimal.valueOf(grossAmt);
				BigDecimal sub_grossAmt1 = grossAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal taxAmt1 = BigDecimal.valueOf(taxAmt);
				BigDecimal sub_taxAmt1 = taxAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal netPayable1 = BigDecimal.valueOf(netPayable);
				BigDecimal sub_netPayable1 = netPayable1.divide(factor, 0, RoundingMode.HALF_UP);
				
				gross_amt=nf.format(sub_grossAmt1);				
				tax_amt=nf.format(sub_taxAmt1);				
				net_payable=nf.format(sub_netPayable1);				
				
				if(opration.equals("CHECK"))
				{
					activity_value = rset.getString(16) == null ? "" : rset.getString(16);
				}
				else if(opration.equals("APPROVE"))
				{
					activity_value = rset.getString(15) == null ? "" : rset.getString(15);
				}
				
				queryString1 = "SELECT GRT,QUANTITY,HRS_BERTHING,TIME_SLOTS_BERTHING,SALE_PRICE,SALE_PRICE_UNIT,TAX_STRUCT_CD "
						+ "FROM FMS_OTH_INVOICE_DTL "
						+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
						+ "AND INVOICE_SEQ=? "
						+ "AND INV_FLAG IN ('CR','DR') ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, inv_type);
				stmt1.setString(3, fin_yr);
				stmt1.setString(4, inv_seq);
				rset1 = stmt1.executeQuery();
				while(rset1.next()) 
				{
					double tmp_grt=rset1.getDouble(1);
					double qtyMmbtu=rset1.getDouble(2);
					berthing_hrs=rset1.getString(3)==null?"":rset1.getString(3);
					berthing_slots=rset1.getString(4)==null?"":rset1.getString(4);
					double tmp_rate=rset1.getDouble(5);
					price_cd=rset1.getString(6)==null?"":rset1.getString(6);
					price_cd_nm=utilBean.getRateUnitNm(conn, price_cd);
					tax_struct_cd=rset1.getString(7)==null?"":rset1.getString(7);
					tax_struct_info=utilBean.getTaxDescr(conn,main_tax_struct_cd);
					
					if(callFlag.equalsIgnoreCase("HSA_CRDR_APROVAL"))
					{
						if(Double.doubleToRawLongBits(tmp_grt)<Double.doubleToRawLongBits(0))
						{
							tmp_grt=(-1)*tmp_grt;
						}
						if(Double.doubleToRawLongBits(qtyMmbtu)<Double.doubleToRawLongBits(0))
						{
							qtyMmbtu=(-1)*qtyMmbtu;
						}
						if(Double.doubleToRawLongBits(tmp_rate)<Double.doubleToRawLongBits(0))
						{
							tmp_rate=(-1)*tmp_rate;
						}
						if(Double.doubleToRawLongBits(Double.parseDouble(berthing_hrs))<Double.doubleToRawLongBits(0))
						{
							berthing_hrs=nf00.format(Double.parseDouble(berthing_hrs)*(-1));
						}
					}
					
					BigDecimal tmp_grt1 = BigDecimal.valueOf(tmp_grt);
					BigDecimal sub_tmp_grt1 = tmp_grt1.divide(factor, 0, RoundingMode.HALF_UP);
					
					BigDecimal qtyMmbtu1 = BigDecimal.valueOf(qtyMmbtu);
					BigDecimal sub_qtyMmbtu1 = qtyMmbtu1.divide(factor, 0, RoundingMode.HALF_UP);
					
					grt=nf.format(sub_tmp_grt1);				
					qty_mmbtu=nf.format(sub_qtyMmbtu1);				
					rate=nf2.format(tmp_rate);	
				}
				rset1.close();
				stmt1.close();
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	//AP20251118
	public void getCrDrRefDetail()
	{
		String function_nm="getCrDrRefDetail()";
		try
		{
			BigDecimal factor = new BigDecimal(1);
			queryString = "SELECT QUANTITY,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,NET_PAYABLE_AMT,GRT,"
					+ "HRS_BERTHING,TIME_SLOTS_BERTHING,SALE_PRICE "
					+ "FROM FMS_OTH_INV_CRDR_REF "
					+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
					+ "AND SUPPLIER_CD=? AND VENDOR_CD=? AND INVOICE_SEQ=? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_type);
			stmt.setString(3, financial_year);
			stmt.setString(4, supp_cd);
			stmt.setString(5, vendor_cd);
			stmt.setString(6, inv_seq);
			rset = stmt.executeQuery();
			while (rset.next()) 
			{
				double qtyMmbtu = rset.getDouble(1);
				double grossAmt = rset.getDouble(2);
				double taxAmt=rset.getDouble(3);
				new_tax_struct_cd=rset.getString(4)==null?"":rset.getString(4);
				new_tax_struct_info=utilBean.getTaxDescr(conn,new_tax_struct_cd);
				double netPayable=rset.getDouble(5);
				double tmp_grt=rset.getDouble(6);
				new_berthing_hrs=rset.getString(7)==null?"":rset.getString(7);
				new_berthing_slots=rset.getString(8)==null?"":rset.getString(8);
				double tmp_rate=rset.getDouble(9);
				
				BigDecimal grossAmt1 = BigDecimal.valueOf(grossAmt);
				BigDecimal sub_grossAmt1 = grossAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal taxAmt1 = BigDecimal.valueOf(taxAmt);
				BigDecimal sub_taxAmt1 = taxAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal netPayable1 = BigDecimal.valueOf(netPayable);
				BigDecimal sub_netPayable1 = netPayable1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal tmp_grt1 = BigDecimal.valueOf(tmp_grt);
				BigDecimal sub_tmp_grt1 = tmp_grt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal qtyMmbtu1 = BigDecimal.valueOf(qtyMmbtu);
				BigDecimal sub_qtyMmbtu1 = qtyMmbtu1.divide(factor, 0, RoundingMode.HALF_UP);
				
				new_grt=""+sub_tmp_grt1;
				if(opration.equals("APPROVE") || opration.equals("CHECK"))
				{
					new_grt = es.formatWithCommaNoDecimal(new_grt);
				}
				new_qty_mmbtu=""+sub_qtyMmbtu1;				
				new_rate=""+tmp_rate;	
				
				new_gross_amt=nf.format(sub_grossAmt1);				
				new_tax_amt=nf.format(sub_taxAmt1);				
				new_net_payable=nf.format(sub_netPayable1);	
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//AP20251120
	public void getCrDrCkpAprView()
	{
		String function_nm="getCrDrCkpAprView()";
		try
		{
			queryString1="SELECT SHIP_CD,SHIP_NAME,SHIP_FLAG,SHIP_ITEM "
					+ "FROM FMS_SHIP_MST A "
					+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_SHIP_MST B WHERE A.SHIP_CD=B.SHIP_CD) AND SHIP_CD=? ";
			stmt1=conn.prepareStatement(queryString1);
			stmt1.setString(1, vessel_cd);
			rset1=stmt1.executeQuery();
			if(rset1.next())
			{
				vessel_nm =(rset1.getString(2)==null?"":rset1.getString(2));
				vessel_flag=(rset1.getString(3)==null?"":rset1.getString(3));
				vessel_item=(rset1.getString(4)==null?"":rset1.getString(4));
			}
			rset1.close();
			stmt1.close();
			
			queryString2 = "SELECT SAC_CODE,SAC_DESC "
					+ "FROM FMS_SAC_MST A "
					+ "WHERE SAC_CD  = ?";
			stmt2=conn.prepareStatement(queryString2);
			stmt2.setString(1,sac_cd);
			rset2=stmt2.executeQuery();
			if(rset2.next())
			{
				sac_code = rset2.getString(1)==null?"":rset2.getString(1);	
				sac_des = rset2.getString(2)==null?"":rset2.getString(2);	
			}
			rset2.close();
			stmt2.close();
			
			//if(!opration.equals("MODIFY")) 
			{
				int srno=0;
				if(price_cd.equals("2")) 
				{
					String currency_nm = "USD";
					
					String desc_item="";
					if(criteri_formula.contains("HRS"))
					{
						desc_item = "Berthing Charges Refund for "+ berthing_slots +" Slot against Inv No. "+sel_inv_no;
					}
					else
					{
						desc_item="Berthing Charges";
					}
					
					if(criteri_formula.contains("RATE"))
					{
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add(desc_item);
						VPDF_COL3.add(currency_nm);
						VPDF_COL4.add(rate);
						VPDF_COL5.add("Per GRT/Per 8 Hrs");
						VPDF_COL6.add("");
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("<b>Invoice rate</b>");
						VPDF_COL3.add("<b>"+currency_nm+"</b>");
						VPDF_COL4.add(main_rate);
						VPDF_COL5.add("Per GRT/Per 8 Hrs");
						VPDF_COL6.add("");
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("<b>Applicable rate</b>");
						VPDF_COL3.add("<b>"+currency_nm+"</b>");
						VPDF_COL4.add(new_rate);
						VPDF_COL5.add("Per GRT/Per 8 Hrs");
						VPDF_COL6.add("");
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("<b>Difference In rate</b>");
						VPDF_COL3.add("<b>"+currency_nm+"</b>");
						VPDF_COL4.add(rate);
						VPDF_COL5.add("Per GRT/Per 8 Hrs");
						VPDF_COL6.add("");
					}
					else
					{
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add(desc_item);
						VPDF_COL3.add(currency_nm);
						VPDF_COL4.add(new_rate);
						VPDF_COL5.add("Per GRT/Per 8 Hrs");
						VPDF_COL6.add(es.formatAmount(gross_amt));
					}
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("<b>Total Charges</b>");
					VPDF_COL3.add("<b>"+currency_nm+"</b>");
					VPDF_COL4.add("");
					VPDF_COL5.add("");
					VPDF_COL6.add("<b>"+es.formatAmount(gross_amt)+"</b>");
					
					srno+=1;
				}
				
				double temp_srno=srno;
				Vector VTAX_CODE = new Vector();
				Vector VTAX_DESCR = new Vector();
				Vector VTAX_AMT = new Vector();
				if (tax_struct_info.contains(",")) 
				{
					BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_amt));
					BigDecimal factor = new BigDecimal(2);
					for(int i = 0;i<tax_struct_info.split(",").length;i++) 
					{
						BigDecimal sub_tax_amt = tax_amt1.divide(factor, 0, RoundingMode.HALF_UP);
						temp_srno=temp_srno+0.1;
						VPDF_COL1.add(nf0.format(temp_srno));
						VPDF_COL2.add(tax_struct_info.split(",")[i]);
						VPDF_COL3.add("USD");
						VPDF_COL4.add("");
						VPDF_COL5.add("");
						VPDF_COL6.add(es.formatAmount(""+Double.parseDouble(""+sub_tax_amt)));
						
						VTAX_CODE.add(tax_struct_cd);
						VTAX_DESCR.add(tax_struct_info.split(",")[i]);
						VTAX_AMT.add(nf.format(sub_tax_amt));
					}
					Vector VTEMP_TAX_DTL = new Vector();
					
					VTEMP_TAX_DTL.add(VTAX_CODE);
					VTEMP_TAX_DTL.add(VTAX_DESCR);
					VTEMP_TAX_DTL.add(VTAX_AMT);
					
					VMULTI_TAX_STRUCT.add(VTEMP_TAX_DTL);
				} 
				
				VPDF_COL1.add(srno);
				VPDF_COL2.add("<b>Tax ("+tax_struct_info+")</b>");
				VPDF_COL3.add("<b>USD</b>");
				VPDF_COL4.add("");
				VPDF_COL5.add("");
				VPDF_COL6.add("<b>"+es.formatAmount(tax_amt)+"</b>");

				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("<b>Total Amount in USD</b>");
				VPDF_COL3.add("<b>USD</b>");
				VPDF_COL4.add("");
				VPDF_COL5.add("");
				VPDF_COL6.add("<b>"+es.formatAmount(net_payable)+"</b>");
			}
			
			if(!net_payable.equalsIgnoreCase(""))		
			{
				amt_in_word=es.convert(Double.parseDouble(net_payable));
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//Deep20251115
	public void getInvoiceDtlsAHPLShare()
	{
		
		String function_nm="getInvoiceDtlsAHPLShare()";

		try
		{
			double taxValue = 0;
			String tax_value="",gross_sign="",less_sign="";
			queryString = "SELECT A.COMPANY_CD,A.VENDOR_CD,A.SUPPLIER_CD,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),B.VENDOR_SUPP_INV_REF,"
					+ "B.PACER_NO,A.GROSS_AMT,A.TAX_AMT_INR,A.NET_PAYABLE,A.EXCHG_RATE_VALUE,TO_CHAR(A.EXCHG_RATE_DT, 'DD/MM/YYYY'),"
					+ "A.SALE_PRICE_UNIT,B.SAC_CODE,B.TAX_STRUCT_CD,B.ITEM_DESCRIPTION,A.REMARK,A.REMARK1,A.APPROVED_FLAG,A.FINANCIAL_YEAR,"
					+ "A.INVOICE_SEQ,A.SALE_AMT,A.CHECKED_FLAG,A.INVOICE_NO,A.INVOICE_ID_SEQ,TO_CHAR(B.TAX_EFF_DT, 'DD/MM/YYYY'),A.INVOICE_CATEGORY,A.INVOICE_RAISED_IN,"
					+ "TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),A.REMARK2,B.SIGN "
					+ "FROM FMS_OTH_INVOICE_MST A, FMS_OTH_INVOICE_DTL B "
					+ "WHERE A.COMPANY_CD = ? AND A.INVOICE_SEQ = ? AND A.INVOICE_TYPE = ? AND A.FINANCIAL_YEAR=? AND A.SUPPLIER_CD=? "
					+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.INVOICE_TYPE = B.INVOICE_TYPE AND A.INV_FLAG = B.INV_FLAG "
					+ "AND A.VENDOR_CD = B.VENDOR_CD AND A.SUPPLIER_CD = B.SUPPLIER_CD AND A.INVOICE_SEQ = B.INVOICE_SEQ AND A.VENDOR_TYPE = ? AND A.INV_FLAG = ?";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_seq);
			stmt.setString(3, inv_type);
			stmt.setString(4, fin_yr);
			stmt.setString(5, supp_cd);
			stmt.setString(6, "V");
			stmt.setString(7, "F");
			rset = stmt.executeQuery();
			if(rset.next()) 
			{
				submission_chk = true;
				
				vendor_cd= rset.getString(2) == null ? "" : rset.getString(2);
				supp_cd= rset.getString(3) == null ? "" : rset.getString(3);				
				inv_dt = rset.getString(4) == null ? "" : rset.getString(4);
				gross_amt = nf.format(Double.parseDouble(rset.getString(7) == null ? "" : rset.getString(7)));
				total_gst = nf.format(Double.parseDouble(rset.getString(8) == null ? "" : rset.getString(8)));
				net_amt = nf.format(Double.parseDouble(rset.getString(9) == null ? "" : rset.getString(9)));
				exchang_val = rset.getString(10) == null ? "" : rset.getString(10);
				exchng_eff_dt = rset.getString(11) == null ? "" : rset.getString(11);
				currency = rset.getString(12) == null ? "" : rset.getString(12);
				sac_cd = rset.getString(13) == null ? "" : rset.getString(13);
				tax_struct_cd = rset.getString(14) == null ? "" : rset.getString(14);
				tax_struct_dt = rset.getString(25) == null ? "" : rset.getString(25);
				desc_item = rset.getString(15) == null ? "" : rset.getString(15);
				remark1 = rset.getString(16) == null ? "" : rset.getString(16);
				remark2 = rset.getString(17) == null ? "" : rset.getString(17);
				if(opration.equals("CHECK"))
				{
					activity_value = rset.getString(22) == null ? "" : rset.getString(22);
				}
				else if(opration.equals("APPROVE"))
				{
					activity_value = rset.getString(18) == null ? "" : rset.getString(18);
				}
				fin_yr = rset.getString(19) == null ? "" : rset.getString(19);
				inv_seq = rset.getString(20) == null ? "" : rset.getString(20);
				sale_amt = nf.format(Double.parseDouble(rset.getString(21) == null ? "" : rset.getString(21)));
				inv_no = rset.getString(23) == null ? "" : rset.getString(23);
				inv_id_seq = rset.getString(24) == null ? "" : rset.getString(24);
				invoice_category = rset.getString(26) == null ? "" : rset.getString(26);
				invoice_raised_in = rset.getString(27) == null ? "" : rset.getString(27);
				due_dt = rset.getString(28) == null ? "" : rset.getString(28);
				remark3 = rset.getString(29) == null ? "" : rset.getString(29);
				//sign = rset.getString(30) == null ? null : rset.getString(30);
				taxValue = rset.getDouble(8);
				
				tax_struct_info=utilBean.getTaxDescr(conn,tax_struct_cd);
				
				if (tax_struct_info.contains(",")) 
				{
					tax_value = nf.format(Double.parseDouble((Double.doubleToRawLongBits(taxValue)==Double.doubleToRawLongBits(0)) ? "0" : String.format("%.2f", taxValue / 2)));
				} 
				else 
				{
					tax_value = nf.format(Double.parseDouble((Double.doubleToRawLongBits(taxValue)==Double.doubleToRawLongBits(0)) ? "0" : String.format("%.2f", taxValue)));
				}	
			
				tax_amt=nf.format(taxValue);
				
				queryString2 = "SELECT SAC_CODE,SAC_DESC "
						+ "FROM FMS_SAC_MST A "
						+ "WHERE SAC_CD  = ?";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1,sac_cd);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					sac_code = rset2.getString(1)==null?"":rset2.getString(1);	
					sac_des = rset2.getString(2)==null?"":rset2.getString(2);	
				}
				rset2.close();
				stmt2.close();
				
				
				if (!opration.equals("INSERT")) 
				{
					queryString2 = "SELECT SIGN "
							+ "FROM FMS_OTH_INVOICE_DTL  "
							+ "WHERE COMPANY_CD  = ? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR = ? AND SUPPLIER_CD = ? "
							+ "AND ITEM_FLAG = ? AND INV_FLAG = ?";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, inv_seq);
					stmt2.setString(3, inv_type);
					stmt2.setString(4, fin_yr);
					stmt2.setString(5, supp_cd);
					stmt2.setString(6, "G");
					stmt2.setString(7, "F");
					rset2 = stmt2.executeQuery();
					while (rset2.next()) 
					{
						gross_sign = rset2.getString(1) == null ? null : rset2.getString(1);
						
					}
					rset2.close();
					stmt2.close();
					
					if(gross_sign!=null && opration.equals("MODIFY"))
					{
						VGSIGN.add("");
						VGAMT_DES.add("");
						VGITEM_AMT.add("");
					}
					
					queryString2 = "SELECT SIGN "
							+ "FROM FMS_OTH_INVOICE_DTL  "
							+ "WHERE COMPANY_CD  = ? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR = ? AND SUPPLIER_CD = ? "
							+ "AND ITEM_FLAG = ? AND INV_FLAG = ?";
					stmt2 = conn.prepareStatement(queryString2);
					stmt2.setString(1, comp_cd);
					stmt2.setString(2, inv_seq);
					stmt2.setString(3, inv_type);
					stmt2.setString(4, fin_yr);
					stmt2.setString(5, supp_cd);
					stmt2.setString(6, "L");
					stmt2.setString(7, "F");
					rset2 = stmt2.executeQuery();
					while (rset2.next()) 
					{
						less_sign = rset2.getString(1) == null ? null : rset2.getString(1);
						
					}
					rset2.close();
					stmt2.close();
					
					if(less_sign!=null && opration.equals("MODIFY"))
					{
						VLSIGN.add("");
						VLAMT_DES.add("");
						VLITEM_AMT.add("");
					}
				}
				
				String gross_formula = "a ",less_formula="b ";
				int index = 2;
				queryString2 = "SELECT CARGO_AMOUNT,SIGN,AMT_DESCRIPTION,ITEM_AMT,SIGN "
						+ "FROM FMS_OTH_INVOICE_DTL  "
						+ "WHERE COMPANY_CD  = ? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR = ? AND SUPPLIER_CD = ? "
						+ "AND ITEM_FLAG = ? AND INV_FLAG = ?";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, inv_seq);
				stmt2.setString(3, inv_type);
				stmt2.setString(4, fin_yr);
				stmt2.setString(5, supp_cd);
				stmt2.setString(6, "G");
				stmt2.setString(7, "F");
				rset2=stmt2.executeQuery();
				while(rset2.next())
				{
					gross_revenue = rset2.getString(1) == null ? "" : rset2.getString(1);
					if (gross_sign!=null) 
					{
						VGSIGN.add(rset2.getString(2) == null ? "" : rset2.getString(2));
						VGAMT_DES.add(rset2.getString(3) == null ? "" : rset2.getString(3));
						VGITEM_AMT.add(rset2.getString(4) == null ? "" : rset2.getString(4));
						VLINE_NO.add(index++);
					}
				}
				rset2.close();
				stmt2.close();
				
				index = 2;
				queryString2 = "SELECT CARGO_AMOUNT,SIGN,AMT_DESCRIPTION,ITEM_AMT,SIGN "
						+ "FROM FMS_OTH_INVOICE_DTL  "
						+ "WHERE COMPANY_CD  = ? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR = ? AND SUPPLIER_CD = ? "
						+ "AND ITEM_FLAG = ? AND INV_FLAG = ?";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, inv_seq);
				stmt2.setString(3, inv_type);
				stmt2.setString(4, fin_yr);
				stmt2.setString(5, supp_cd);
				stmt2.setString(6, "L");
				stmt2.setString(7, "F");
				rset2=stmt2.executeQuery();
				while(rset2.next())
				{
					gross_less = rset2.getString(1)==null?"":rset2.getString(1);
					if (less_sign!=null) 
					{
						VLSIGN.add(rset2.getString(2)==null?"":rset2.getString(2));
						VLAMT_DES.add(rset2.getString(3)==null?"":rset2.getString(3));
						VLITEM_AMT.add(rset2.getString(4)==null?"":rset2.getString(4));
						VLINE_NO1.add(index++);
						
					}
				}
				rset2.close();
				stmt2.close();
				
				double temp_srno = 0;
				int srno=0;
				String currency_nm ="";
				currency_nm = "INR";
				
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("Gross Revenue (a)");
				VPDF_COL3.add(currency_nm);
				VPDF_COL4.add(es.formatAmount(gross_revenue));
				
				for (int i = 0; i < VGSIGN.size(); i++) 
				{
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add(VGAMT_DES.elementAt(i)+" (a"+(i+1)+")");
					VPDF_COL3.add(currency_nm);
					if (!VGITEM_AMT.elementAt(i).equals("")) 
					{
						VPDF_COL4.add(nf3.format(Double.parseDouble("" + VGITEM_AMT.elementAt(i))));
					}
					else 
					{
						VPDF_COL4.add("");
					}
					gross_formula += VGSIGN.elementAt(i)+" a"+(i+1)+" ";
				}
				
				
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("Less: Water Front Royalty(WFR) (b)");
				VPDF_COL3.add(currency_nm);
				VPDF_COL4.add(es.formatAmount(gross_less));
				
				for (int i = 0; i < VLSIGN.size(); i++) 
				{
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add(VLAMT_DES.elementAt(i)+" (b"+(i+1)+")");
					VPDF_COL3.add(currency_nm);
					if (!VLITEM_AMT.elementAt(i).equals("")) 
					{
						VPDF_COL4.add(nf3.format(Double.parseDouble("" + VLITEM_AMT.elementAt(i))));
					}
					else 
					{
						VPDF_COL4.add("");
					}
					less_formula += VLSIGN.elementAt(i)+" b"+(i+1)+" ";
				}

				
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("Net Revenue Excluding WFR (("+gross_formula+") - ("+less_formula+"))");
				VPDF_COL3.add("<b>INR</b>");
				VPDF_COL4.add("<b>"+es.formatAmount(sale_amt)+"</b>");
				
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add(desc_item);
				VPDF_COL3.add("INR");
				VPDF_COL4.add(es.formatAmount(gross_amt));
				
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("<b>Total Amount</b>");
				VPDF_COL3.add("<b>INR</b>");
				VPDF_COL4.add("<b>"+es.formatAmount(gross_amt)+"</b>");
				
				srno+=1;

				temp_srno=srno;
				Vector VTAX_CODE = new Vector();
				Vector VTAX_DESCR = new Vector();
				Vector VTAX_AMT = new Vector();
				if (tax_struct_info.contains(",")) 
				{
					BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_amt));
					BigDecimal factor = new BigDecimal(2);
					for(int i = 0;i<tax_struct_info.split(",").length;i++) 
					{
						BigDecimal sub_tax_amt = tax_amt1.divide(factor, 0, RoundingMode.HALF_UP);
						temp_srno=temp_srno+0.1;
						VPDF_COL1.add(nf0.format(temp_srno));
						VPDF_COL2.add(tax_struct_info.split(",")[i]);
						VPDF_COL3.add("INR");
						VPDF_COL4.add(es.formatAmount(""+Double.parseDouble(""+sub_tax_amt)));
						
						VTAX_CODE.add(tax_struct_cd);
						VTAX_DESCR.add(tax_struct_info.split(",")[i]);
						VTAX_AMT.add(nf.format(sub_tax_amt));
					}
					Vector VTEMP_TAX_DTL = new Vector();
					
					VTEMP_TAX_DTL.add(VTAX_CODE);
					VTEMP_TAX_DTL.add(VTAX_DESCR);
					VTEMP_TAX_DTL.add(VTAX_AMT);
					
					VMULTI_TAX_STRUCT.add(VTEMP_TAX_DTL);
				} 
				
				VPDF_COL1.add(srno);
				VPDF_COL2.add("<b>Tax ("+tax_struct_info+")</b>");
				VPDF_COL3.add("<b>INR</b>");
				VPDF_COL4.add("<b>"+es.formatAmount(tax_amt)+"</b>");
				
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("<b>Total Amount in INR</b>");
				VPDF_COL3.add("<b>INR</b>");
				VPDF_COL4.add("<b>"+es.formatAmount(net_amt)+"</b>");
				
				if(!net_amt.equalsIgnoreCase(""))		
				{
					amt_in_word=es.convert(Double.parseDouble(net_amt));
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
	
	public void getInvoiceDtlsSFA()
	{	
		String function_nm="getInvoiceDtlsSFA()";

		try
		{
			double taxValue = 0;
			String tax_value="";
			queryString = "SELECT A.COMPANY_CD,A.VENDOR_CD,A.SUPPLIER_CD,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),B.GATE_PASS_NO,"
					+ "B.SALE_NO,A.GROSS_AMT,A.TAX_AMT_INR,A.NET_PAYABLE,A.EXCHG_RATE_VALUE,TO_CHAR(A.EXCHG_RATE_DT, 'DD/MM/YYYY'),"
					+ "A.SALE_PRICE_UNIT,B.SAC_CODE,B.TAX_STRUCT_CD,B.ITEM_DESCRIPTION,A.REMARK,A.REMARK1,A.APPROVED_FLAG,A.FINANCIAL_YEAR,"
					+ "A.INVOICE_SEQ,A.SALE_AMT,A.CHECKED_FLAG,A.INVOICE_NO,A.INVOICE_ID_SEQ,TO_CHAR(B.TAX_EFF_DT, 'DD/MM/YYYY'),A.INVOICE_CATEGORY,"
					+ "A.INVOICE_RAISED_IN,B.CESS_RATE,B.TOTAL_CESS_AMOUNT "
					+ "FROM FMS_OTH_INVOICE_MST A, FMS_OTH_INVOICE_DTL B "
					+ "WHERE A.COMPANY_CD = ? AND A.INVOICE_SEQ = ? AND A.INVOICE_TYPE = ? AND A.FINANCIAL_YEAR=? AND A.SUPPLIER_CD=? AND A.INV_FLAG = ? "
					+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.INVOICE_TYPE = B.INVOICE_TYPE "
					+ "AND A.VENDOR_CD = B.VENDOR_CD AND A.SUPPLIER_CD = B.SUPPLIER_CD AND A.INVOICE_SEQ = B.INVOICE_SEQ AND A.INV_FLAG = B.INV_FLAG";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_seq);
			stmt.setString(3, inv_type);
			stmt.setString(4, fin_yr);
			stmt.setString(5, supp_cd);
			stmt.setString(6, "F");
			rset = stmt.executeQuery();
			if(rset.next()) 
			{
				submission_chk = true;
				
				vendor_cd= rset.getString(2) == null ? "" : rset.getString(2);
				supp_cd= rset.getString(3) == null ? "" : rset.getString(3);				
				inv_dt = rset.getString(4) == null ? "" : rset.getString(4);
				gate_pass = rset.getString(5) == null ? "" : rset.getString(5);
				sale_no = rset.getString(6) == null ? "" : rset.getString(6);				
				gross_amt = nf.format(Double.parseDouble(rset.getString(7) == null ? "" : rset.getString(7)));
				total_gst = nf.format(Double.parseDouble(rset.getString(8) == null ? "" : rset.getString(8)));
				net_amt = nf.format(Double.parseDouble(rset.getString(9) == null ? "" : rset.getString(9)));
				exchng_rate = rset.getString(10) == null ? "" : rset.getString(10);
				exchng_eff_dt = rset.getString(11) == null ? "" : rset.getString(11);
				currency = rset.getString(12) == null ? "" : rset.getString(12);
				sac_cd = rset.getString(13) == null ? "" : rset.getString(13);
				tax_struct_cd = rset.getString(14) == null ? "" : rset.getString(14);
				tax_struct_dt = rset.getString(25) == null ? "" : rset.getString(25);
				desc_item = rset.getString(15) == null ? "" : rset.getString(15);
				remark1 = rset.getString(16) == null ? "" : rset.getString(16);
				remark2 = rset.getString(17) == null ? "" : rset.getString(17);
				if(opration.equals("CHECK"))
				{
					activity_value = rset.getString(22) == null ? "" : rset.getString(22);
				}
				else if(opration.equals("APPROVE"))
				{
					activity_value = rset.getString(18) == null ? "" : rset.getString(18);
				}
				fin_yr = rset.getString(19) == null ? "" : rset.getString(19);
				inv_seq = rset.getString(20) == null ? "" : rset.getString(20);
				sale_amt = nf.format(Double.parseDouble(rset.getString(21) == null ? "" : rset.getString(21)));
				inv_no = rset.getString(23) == null ? "" : rset.getString(23);
				inv_id_seq = rset.getString(24) == null ? "" : rset.getString(24);
				invoice_category = rset.getString(26) == null ? "" : rset.getString(26);
				invoice_raised_in = rset.getString(27) == null ? "" : rset.getString(27);
				cess_flag = rset.getString(28) == null ? "" : "true";
				cess_amt = nf.format(rset.getDouble(29));
				
				
				taxValue = rset.getDouble(8);
				
				tax_struct_info=utilBean.getTaxDescr(conn,tax_struct_cd);
				
				if (tax_struct_info.contains(",")) 
				{
					tax_value = nf.format(Double.parseDouble((Double.doubleToRawLongBits(taxValue)==Double.doubleToRawLongBits(0)) ? "0" : String.format("%.2f", taxValue / 2)));
				} 
				else 
				{
					tax_value = nf.format(Double.parseDouble((Double.doubleToRawLongBits(taxValue)==Double.doubleToRawLongBits(0)) ? "0" : String.format("%.2f", taxValue)));
				}	
			
				tax_amt=nf.format(taxValue);
				
				queryString2 = "SELECT SAC_CODE,SAC_DESC "
						+ "FROM FMS_SAC_MST A "
						+ "WHERE SAC_CD  = ?";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1,sac_cd);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					sac_code = rset2.getString(1)==null?"":rset2.getString(1);	
					sac_des = rset2.getString(2)==null?"":rset2.getString(2);	
				}
				rset2.close();
				stmt2.close();

				Map<String, Double> taxAmountMap = new LinkedHashMap<>();
				String taxDesc="";
				double taxAmt = 0.0;
				int index = 1;
				queryString2 = "SELECT ITEM_DESCRIPTION,SALE_PRICE,HSN_CODE,QUANTITY,UAM_NO,ITEM_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT, 'DD/MM/YYYY'),ITEM_TAX_AMT,CESS_RATE,CESS_AMOUNT "
						+ "FROM FMS_OTH_INVOICE_DTL  "
						+ "WHERE COMPANY_CD  = ? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR = ? AND SUPPLIER_CD = ? "
						+ "AND INV_FLAG = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY')";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, inv_seq);
				stmt2.setString(3, inv_type);
				stmt2.setString(4, fin_yr);
				stmt2.setString(5, supp_cd);
				stmt2.setString(6, "F");
				stmt2.setString(7, inv_dt);
				rset2=stmt2.executeQuery();
				while(rset2.next())
				{
					VGAMT_DES.add(rset2.getString(1)==null?"":rset2.getString(1));
					VPRICE.add(nf.format(Double.parseDouble("" + rset2.getString(2)==null?"":rset2.getString(2))));
					VSAC_CODE.add(rset2.getString(3)==null?"":rset2.getString(3));
					VQTY.add(rset2.getString(4)==null?"":rset2.getString(4));
					VUOM_NO.add(rset2.getString(5)==null?"":rset2.getString(5));
					VGITEM_AMT.add(nf.format(Double.parseDouble("" + rset2.getString(6)==null?"":rset2.getString(6))));
					if(invoice_category.equals("S"))
					{
						VTAX_STRUCTURE_DESC.add(rset2.getString(7)==null?"":rset2.getString(7));
					}
					else 
					{
						VTAX_STRUCTURE_DESC.add(utilBean.getTaxDescr(conn,rset2.getString(7)==null?"":rset2.getString(7)));
						taxDesc = utilBean.getTaxDescr(conn,rset2.getString(7) == null ? "" : rset2.getString(7));
						
					}
					VTAX_STRUCTURE_CD.add(rset2.getString(7)==null?"":rset2.getString(7));
					VTAX_STRUCT_APP_DT.add(rset2.getString(8)==null?"":rset2.getString(8));
					if(invoice_category.equals("S"))
					{
						VTAX_AMT.add(rset2.getString(9)==null?"":rset2.getString(9));
					}
					else
					{
						VTAX_AMT.add(nf.format(Double.parseDouble("" + rset2.getString(9)==null?"":rset2.getString(9))));
						if (rset2.getString(9) != null && !rset2.getString(9).isEmpty()) 
						{
						    taxAmt = Double.parseDouble(rset2.getString(9));
						}
						
						if (taxAmountMap.containsKey(taxDesc)) 
						{
						    taxAmountMap.put(taxDesc, taxAmountMap.get(taxDesc) + taxAmt);
						} 
						else 
						{
						    taxAmountMap.put(taxDesc, taxAmt);
						}
					}
					
					VCESS_PER.add(rset2.getString(10)==null?"":rset2.getString(10));
					if(invoice_category.equals("S"))
					{
						VCESS_AMT.add(rset2.getString(11)==null?"":rset2.getString(11));
					}
					else
					{
						VCESS_AMT.add(nf.format(Double.parseDouble("" + rset2.getString(11)==null?"":rset2.getString(11))));
					}
					VLINE_NO.add(index++);
				}
				rset2.close();
				stmt2.close();
				
				for (Map.Entry<String, Double> entry : taxAmountMap.entrySet()) 
				{
				    VTAX_STRUCT_NM.add(entry.getKey());
				    VTAX_VAL_AMT.add(nf.format(entry.getValue()));
				}
				
				int srno=0;
				String currency_nm ="INR";
				
				if(invoice_category.equals("S"))
				{
					for(int i = 0; i<VGITEM_AMT.size();i++)
					{
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add(VSAC_CODE.elementAt(i));
						VPDF_COL3.add(VGAMT_DES.elementAt(i));
						VPDF_COL4.add(VUOM_NO.elementAt(i));
						VPDF_COL5.add(nf.format(Double.parseDouble("" + VQTY.elementAt(i))));
						VPDF_COL6.add(nf.format(Double.parseDouble("" + VPRICE.elementAt(i))));
						VPDF_COL7.add(currency_nm);
						VPDF_COL8.add(es.formatAmount(""+VGITEM_AMT.elementAt(i)));
					}
					
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("");
					VPDF_COL3.add("<b>Total Amount</b>");
					VPDF_COL4.add("");
					VPDF_COL5.add("");
					VPDF_COL6.add("");
					VPDF_COL7.add("<b>"+currency_nm+"</b>");
					VPDF_COL8.add("<b>"+es.formatAmount(gross_amt)+"</b>");
					
					srno+=1;
					double temp_srno=srno;
					Vector VTAX_CODE = new Vector();
					Vector VTAX_DESCR = new Vector();
					Vector VTAX_AMT = new Vector();
					
					if (tax_struct_info.contains(",")) 
					{
						BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_amt));
						BigDecimal factor = new BigDecimal(2);
						for(int i = 0;i<tax_struct_info.split(",").length;i++) 
						{
							BigDecimal sub_tax_amt = tax_amt1.divide(factor, 0, RoundingMode.HALF_UP);
							temp_srno=temp_srno+0.1;
							VPDF_COL1.add(nf0.format(temp_srno));
							VPDF_COL2.add("");
							VPDF_COL3.add(tax_struct_info.split(",")[i]);
							VPDF_COL4.add("");
							VPDF_COL5.add("");
							VPDF_COL6.add("");
							VPDF_COL7.add("INR");
							VPDF_COL8.add(es.formatAmount(""+Double.parseDouble(""+sub_tax_amt)));
							
							VTAX_CODE.add(tax_struct_cd);
							VTAX_DESCR.add(tax_struct_info.split(",")[i]);
							VTAX_AMT.add(nf.format(sub_tax_amt));
						}
						Vector VTEMP_TAX_DTL = new Vector();
						
						VTEMP_TAX_DTL.add(VTAX_CODE);
						VTEMP_TAX_DTL.add(VTAX_DESCR);
						VTEMP_TAX_DTL.add(VTAX_AMT);
						
						VMULTI_TAX_STRUCT.add(VTEMP_TAX_DTL);
					} 
					
					VPDF_COL1.add(srno);
					VPDF_COL2.add("");
					VPDF_COL3.add("<b>Tax ("+tax_struct_info+")</b>");
					VPDF_COL4.add("");
					VPDF_COL5.add("");
					VPDF_COL6.add("");
					VPDF_COL7.add("<b>"+currency_nm+"</b>");
					VPDF_COL8.add("<b>"+es.formatAmount(tax_amt)+"</b>");
					
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("");
					VPDF_COL3.add("<b>Total Amount in INR</b>");
					VPDF_COL4.add("");
					VPDF_COL5.add("");
					VPDF_COL6.add("");
					VPDF_COL7.add("<b>"+currency_nm+"</b>");
					VPDF_COL8.add("<b>"+es.formatAmount(net_amt)+"</b>");
				}
				else if(invoice_category.equals("P"))
				{
					for(int i = 0; i<VGITEM_AMT.size();i++)
					{
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add(VSAC_CODE.elementAt(i));
						VPDF_COL3.add(VGAMT_DES.elementAt(i));
						VPDF_COL4.add(VUOM_NO.elementAt(i));
						VPDF_COL5.add(nf.format(Double.parseDouble("" + VQTY.elementAt(i))));
						VPDF_COL6.add(nf.format(Double.parseDouble("" + VPRICE.elementAt(i))));
						VPDF_COL7.add(VTAX_STRUCTURE_DESC.elementAt(i).toString().replace(","," ").split(" ")[1]);
						if(cess_flag.equals("true"))
						{	
							VPDF_COL8.add(VCESS_PER.elementAt(i)+"%");
						}
						VPDF_COL9.add(currency_nm);
						VPDF_COL10.add(es.formatAmount(""+VGITEM_AMT.elementAt(i)));
					}
					
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("");
					VPDF_COL3.add("<b>Total Amount</b>");
					VPDF_COL4.add("");
					VPDF_COL5.add("");
					VPDF_COL6.add("");
					VPDF_COL7.add("");
					VPDF_COL8.add("");
					VPDF_COL9.add("<b>"+currency_nm+"</b>");
					VPDF_COL10.add("<b>"+es.formatAmount(gross_amt)+"</b>");
					
					srno+=1;
					double temp_srno=srno;
					
					for (int i = 0; i < VTAX_STRUCT_NM.size(); i++) 
					{
					    String desc = VTAX_STRUCT_NM.elementAt(i).toString();

					    if (desc.contains(",")) 
					    {
					        String[] parts = desc.split(",");  

					        BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(""+VTAX_VAL_AMT.elementAt(i)));
							BigDecimal factor = new BigDecimal(2);
							
					        for (int j = 0; j < parts.length; j++) 
					        {
					            BigDecimal sub_tax_amt = tax_amt1.divide(factor, 0, RoundingMode.HALF_UP);
								temp_srno=temp_srno+0.1;
					            VPDF_COL1.add(nf0.format(temp_srno));
					            VPDF_COL2.add("");
					            VPDF_COL3.add(parts[j].trim());
					            VPDF_COL4.add("");
					            VPDF_COL5.add("");
					            VPDF_COL6.add("");
					            VPDF_COL7.add("");
					            VPDF_COL8.add("");
					            VPDF_COL9.add("INR");
					            VPDF_COL10.add(es.formatAmount(""+Double.parseDouble(""+sub_tax_amt)));
					        }
					    }
					    else 
					    {
					    	temp_srno=temp_srno+0.1;
					        VPDF_COL1.add(nf0.format(temp_srno));
					        VPDF_COL2.add("");
					        VPDF_COL3.add(desc);
					        VPDF_COL4.add("");
					        VPDF_COL5.add("");
					        VPDF_COL6.add("");
					        VPDF_COL7.add("");
					        VPDF_COL8.add("");
					        VPDF_COL9.add("INR");
					        VPDF_COL10.add(es.formatAmount(""+tax_amt));
					    }
					}

					VPDF_COL1.add(srno);
					VPDF_COL2.add("");
					VPDF_COL3.add("<b>Total Tax Amount</b>");
					VPDF_COL4.add("");
					VPDF_COL5.add("");
					VPDF_COL6.add("");
					VPDF_COL7.add("");
					VPDF_COL8.add("");
					VPDF_COL9.add("<b>"+currency_nm+"</b>");
					VPDF_COL10.add("<b>"+es.formatAmount(tax_amt)+"</b>");
					
					if(cess_flag.equals("true"))
					{
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("");
						VPDF_COL3.add("<b>Total Cess</b>");
						VPDF_COL4.add("");
						VPDF_COL5.add("");
						VPDF_COL6.add("");
						VPDF_COL7.add("");
						VPDF_COL8.add("");
						VPDF_COL9.add("<b>"+currency_nm+"</b>");
						VPDF_COL10.add("<b>"+es.formatAmount(cess_amt)+"</b>");
					}
					
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("");
					VPDF_COL3.add("<b>Total Amount in INR</b>");
					VPDF_COL4.add("");
					VPDF_COL5.add("");
					VPDF_COL6.add("");
					VPDF_COL7.add("");
					VPDF_COL8.add("");
					VPDF_COL9.add("<b>"+currency_nm+"</b>");
					VPDF_COL10.add("<b>"+es.formatAmount(net_amt)+"</b>");
				}
				
				
				if(!net_amt.equalsIgnoreCase(""))		
				{
					amt_in_word=es.convert(Double.parseDouble(net_amt));
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
	
	//Deep20251229
	public void getCompOwnerList()
	{	
		String function_nm="getCompOwnerList()";

		try
		{
			String queryString  = "SELECT COMPANY_CD,COMPANY_NM,COMPANY_ABBR "
					+ "FROM FMS_COMPANY_OWNER_MST "
					+ "WHERE COMPANY_ABBR='SEIPL' AND STATUS = 'Y' ";
			stmt=conn.prepareStatement(queryString);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				supp_cd=rset.getString(1)==null?"":rset.getString(1);
				VSUPPLIER_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VSUPPLIER_NM.add(rset.getString(2)==null?"":rset.getString(2));
				VSUPPLIER_ABBR.add(rset.getString(3)==null?"":rset.getString(3));
			}
			rset.close();
			stmt.close();
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCompOwnerBUList()
	{	
		String function_nm="getCompOwnerBUList()";

		try
		{
			
			String queryString  = "SELECT SEQ_NO,PLANT_NAME "
					+ "FROM FMS_COUNTERPARTY_PLANT_DTL A "
					+ "WHERE ENTITY='B' AND COMPANY_CD=? "
					+ "AND EFF_DT=(SELECT MAX(b.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL B WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
					+ "AND A.SEQ_NO=B.SEQ_NO AND A.COMPANY_CD=B.COMPANY_CD AND B.EFF_DT<=TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY') AND A.ENTITY=B.ENTITY) "
					+ "AND STATUS='Y' ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, supp_cd);
			rset=stmt.executeQuery();
			while(rset.next())
			{
				VBU_CD.add(rset.getString(1)==null?"":rset.getString(1));
				VBU_NM.add(rset.getString(2)==null?"":rset.getString(2));
			}
			rset.close();
			stmt.close();
			
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCompOwnerDtl()
	{	
		String function_nm="getCompOwnerDtl()";
 
		try
		{
			String queryString="SELECT A.PLANT_ADDR,A.PLANT_CITY,A.PLANT_STATE,B.COMPANY_ABBR,A.PLANT_PIN,B.COMPANY_NM,A.SEQ_NO "
					+ "FROM FMS_COUNTERPARTY_PLANT_DTL A, FMS_COMPANY_OWNER_MST B "
					+ "WHERE B.COMPANY_CD=? AND A.ENTITY = ? AND A.SEQ_NO = ? AND A.COUNTERPARTY_CD = B.COMPANY_CD "
					+ "AND A.EFF_DT=(SELECT MAX(C.EFF_DT) FROM FMS_COUNTERPARTY_PLANT_DTL C WHERE A.COMPANY_CD = C.COMPANY_CD "
					+ "AND A.COUNTERPARTY_CD = C.COUNTERPARTY_CD AND A.ENTITY = C.ENTITY AND A.SEQ_NO = C.SEQ_NO)";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, supp_cd);
			stmt.setString(2, "B");
			stmt.setString(3, bu_unit);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				supp_addr = rset.getString(1)==null?"":rset.getString(1);
				supp_city = rset.getString(2)==null?"":rset.getString(2);
				supp_state =  rset.getString(3)==null?"":rset.getString(3);
				supp_abbr =  rset.getString(4)==null?"":rset.getString(4);
				supp_pin = rset.getString(5)==null?"":rset.getString(5);
				supp_nm = rset.getString(6)==null?"":rset.getString(6);
				bu_unit=rset.getString(7)==null?"":rset.getString(7);
				
				String query = "SELECT A.STAT_NO "
						+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B  "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.ENTITY = 'B' AND A.PLANT_SEQ_NO=? "
						+ "AND A.STAT_CD = B.STAT_CD AND B.STAT_TYPE = 'G' AND B.STAT_NM = 'PAN' ";
				stmt1 = conn.prepareStatement(query);
				stmt1.setString(1, supp_cd);
				stmt1.setString(2, supp_cd);
				stmt1.setString(3, bu_unit);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					supp_pan_no = rset1.getString(1)==null?"":rset1.getString(1);
				}
				stmt1.close();
				rset1.close();
				
				query = "SELECT A.STAT_NO "
						+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B  "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.ENTITY = 'B' AND A.PLANT_SEQ_NO=? "
						+ "AND A.STAT_CD = B.STAT_CD AND B.STAT_TYPE = 'R' AND B.STAT_NM = 'GSTIN' ";
				stmt1 = conn.prepareStatement(query);
				stmt1.setString(1, supp_cd);
				stmt1.setString(2, supp_cd);
				stmt1.setString(3, bu_unit);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					supp_gstin_no = rset1.getString(1)==null?"":rset1.getString(1);
				}
				stmt1.close();
				rset1.close();
				
				query = "SELECT TIN "
						+ "FROM FMS_STATE_MST "
						+ "WHERE STATE_NM=? "
						+ "ORDER BY STATE_NM";
				stmt1 = conn.prepareStatement(query);
				stmt1.setString(1, supp_state);
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					supp_state_tin= rset1.getString(1)==null?"":rset1.getString(1);
				}
				stmt1.close();
				rset1.close();
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	public void getInvoiceDtlsGA()
	{	
		String function_nm="getInvoiceDtlsGA()";

		try
		{
			double taxValue = 0;
			String tax_value="";
			
			queryString = "SELECT A.COMPANY_CD,A.VENDOR_CD,A.SUPPLIER_CD,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),B.GATE_PASS_NO,"
					+ "B.SALE_NO,A.GROSS_AMT,A.TAX_AMT_INR,A.NET_PAYABLE,A.EXCHG_RATE_VALUE,TO_CHAR(A.EXCHG_RATE_DT, 'DD/MM/YYYY'),"
					+ "A.SALE_PRICE_UNIT,B.SAC_CODE,B.TAX_STRUCT_CD,B.ITEM_DESCRIPTION,A.REMARK,A.REMARK1,A.APPROVED_FLAG,A.FINANCIAL_YEAR,"
					+ "A.INVOICE_SEQ,A.SALE_AMT,A.CHECKED_FLAG,A.INVOICE_NO,A.INVOICE_ID_SEQ,TO_CHAR(B.TAX_EFF_DT, 'DD/MM/YYYY'),A.INVOICE_CATEGORY,"
					+ "A.INVOICE_RAISED_IN "
					+ "FROM FMS_OTH_INVOICE_MST A, FMS_OTH_INVOICE_DTL B "
					+ "WHERE A.COMPANY_CD = ? AND A.INVOICE_SEQ = ? AND A.INVOICE_TYPE = ? AND A.FINANCIAL_YEAR=? AND A.SUPPLIER_CD=? AND A.INV_FLAG = ? "
					+ "AND A.BU_UNIT = ? AND A.COMPANY_CD = B.COMPANY_CD AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.INVOICE_TYPE = B.INVOICE_TYPE "
					+ "AND A.VENDOR_CD = B.VENDOR_CD AND A.SUPPLIER_CD = B.SUPPLIER_CD AND A.INVOICE_SEQ = B.INVOICE_SEQ AND A.INV_FLAG = B.INV_FLAG AND A.BU_UNIT = B.BU_UNIT ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_seq);
			stmt.setString(3, inv_type);
			stmt.setString(4, fin_yr);
			stmt.setString(5, supp_cd);
			stmt.setString(6, "F");
			stmt.setString(7, bu_unit);
			rset = stmt.executeQuery();
			if(rset.next()) 
			{
				
				submission_chk = true;
				
				vendor_cd= rset.getString(2) == null ? "" : rset.getString(2);
				supp_cd= rset.getString(3) == null ? "" : rset.getString(3);				
				inv_dt = rset.getString(4) == null ? "" : rset.getString(4);
				gross_amt = nf.format(Double.parseDouble(rset.getString(7) == null ? "" : rset.getString(7)));
				total_gst = nf.format(Double.parseDouble(rset.getString(8) == null ? "" : rset.getString(8)));
				net_amt = nf.format(Double.parseDouble(rset.getString(9) == null ? "" : rset.getString(9)));
				exchng_rate = rset.getString(10) == null ? "" : rset.getString(10);
				exchng_eff_dt = rset.getString(11) == null ? "" : rset.getString(11);
				currency = rset.getString(12) == null ? "" : rset.getString(12);
				sac_cd = rset.getString(13) == null ? "" : rset.getString(13);
				tax_struct_cd = rset.getString(14) == null ? "" : rset.getString(14);
				tax_struct_dt = rset.getString(25) == null ? "" : rset.getString(25);
				desc_item = rset.getString(15) == null ? "" : rset.getString(15);
				remark1 = rset.getString(16) == null ? "" : rset.getString(16);
				remark2 = rset.getString(17) == null ? "" : rset.getString(17);
				if(opration.equals("CHECK"))
				{
					activity_value = rset.getString(22) == null ? "" : rset.getString(22);
				}
				else if(opration.equals("APPROVE"))
				{
					activity_value = rset.getString(18) == null ? "" : rset.getString(18);
				}
				fin_yr = rset.getString(19) == null ? "" : rset.getString(19);
				inv_seq = rset.getString(20) == null ? "" : rset.getString(20);
				sale_amt = nf.format(Double.parseDouble(rset.getString(21) == null ? "" : rset.getString(21)));
				inv_no = rset.getString(23) == null ? "" : rset.getString(23);
				inv_id_seq = rset.getString(24) == null ? "" : rset.getString(24);
				invoice_category = rset.getString(26) == null ? "" : rset.getString(26);
				invoice_raised_in = rset.getString(27) == null ? "" : rset.getString(27);
				
				
				tax_struct_info=utilBean.getTaxDescr(conn,tax_struct_cd);
				
				if (tax_struct_info.contains(",")) 
				{
					tax_value = nf.format(Double.parseDouble((Double.doubleToRawLongBits(taxValue)==Double.doubleToRawLongBits(0)) ? "0" : String.format("%.2f", taxValue / 2)));
				} 
				else 
				{
					tax_value = nf.format(Double.parseDouble((Double.doubleToRawLongBits(taxValue)==Double.doubleToRawLongBits(0)) ? "0" : String.format("%.2f", taxValue)));
				}	
			
				tax_amt=nf.format(rset.getDouble(8));
				
				queryString2 = "SELECT SAC_CODE,SAC_DESC "
						+ "FROM FMS_SAC_MST A "
						+ "WHERE SAC_CD  = ?";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1,sac_cd);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					sac_code = rset2.getString(1)==null?"":rset2.getString(1);	
					sac_des = rset2.getString(2)==null?"":rset2.getString(2);	
				}
				rset2.close();
				stmt2.close();

				Map<String, Double> taxAmountMap = new LinkedHashMap<>();
				String taxDesc="";
				double taxAmt = 0.0;
				int index = 1;
				queryString2 = "SELECT ITEM_DESCRIPTION,SAC_CODE,ITEM_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT, 'DD/MM/YYYY'),ITEM_TAX_AMT,ITEM_TOTAL_AMT,TAX_CD "
						+ "FROM FMS_OTH_INVOICE_DTL  "
						+ "WHERE COMPANY_CD  = ? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR = ? AND SUPPLIER_CD = ? "
						+ "AND INV_FLAG = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY') AND BU_UNIT = ? "
						+ "ORDER BY SEQ_NO";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, inv_seq);
				stmt2.setString(3, inv_type);
				stmt2.setString(4, fin_yr);
				stmt2.setString(5, supp_cd);
				stmt2.setString(6, "F");
				stmt2.setString(7, inv_dt);
				stmt2.setString(8, bu_unit);
				rset2=stmt2.executeQuery();
				while(rset2.next())
				{
					tax_struct_info = rset2.getString(8)==null?"":rset2.getString(8);
					VTAX_STRUCT_NM.add(tax_struct_info);
					taxValue = rset2.getDouble(6);
					VGAMT_DES.add(rset2.getString(1)==null?"":rset2.getString(1));
					VSAC_VAL.add(rset2.getString(2)==null?"":rset2.getString(2));
					VGITEM_AMT.add(nf.format(Double.parseDouble("" + rset2.getString(3)==null?"":rset2.getString(3))));
					taxAmt = taxValue;
					if(tax_struct_info.equals("C"))
					{
						tax_struct_info = utilBean.getTaxDescr(conn,rset2.getString(4)==null?"":rset2.getString(4));
						if(opration.equals("CHECK") || opration.equals("APPROVE"))
						{
							VTAX_STRUCTURE.add(tax_struct_info.split(", ")[0].split(" ")[1]);
							VTAX_STRUCTURE1.add(tax_struct_info.split(", ")[1].split(" ")[1]);
						}
						else 
						{
							VTAX_STRUCTURE.add(tax_struct_info.split(", ")[0]);
							VTAX_STRUCTURE1.add(tax_struct_info.split(", ")[1]);
						}
						taxAmt = taxValue/2;
					}
					if(opration.equals("CHECK") || opration.equals("APPROVE"))
					{
						VTAX_STRUCTURE_DESC.add(utilBean.getTaxDescr(conn,rset2.getString(4)==null?"":rset2.getString(4)).split(", ")[0].split(" ")[1]);
					}
					else if(opration.equals("MODIFY"))
					{
						VTAX_STRUCTURE_DESC.add(utilBean.getTaxDescr(conn,rset2.getString(4)==null?"":rset2.getString(4)));
					}
					VTAX_STRUCTURE_CD.add(rset2.getString(4)==null?"":rset2.getString(4));
					VTAX_STRUCT_APP_DT.add(rset2.getString(5)==null?"":rset2.getString(5));
					VITEM_TOTAL.add(nf.format(rset2.getDouble(7)));
					VTAX_AMT.add(nf.format(taxAmt));
					VLINE_NO.add(index++);
				}
				rset2.close();
				stmt2.close();
				
				int srno=0;
				String currency_nm ="INR";
				
					for(int i = 0; i<VGITEM_AMT.size();i++)
					{
						queryString3 = "SELECT SAC_CODE "
								+ "FROM FMS_SAC_MST A "
								+ "WHERE SAC_CD  = ?";
						stmt3=conn.prepareStatement(queryString3);
						stmt3.setString(1, ""+VSAC_VAL.elementAt(i));
						rset3=stmt3.executeQuery();
						if(rset3.next())
						{
							sac_code = rset3.getString(1)==null?"":rset3.getString(1);	
						}
						rset3.close();
						stmt3.close();
						
						if(VTAX_STRUCT_NM.elementAt(i).equals("C"))
						{
							srno+=1;
							VPDF_COL1.add(srno);
							VPDF_COL2.add(VGAMT_DES.elementAt(i));
							VPDF_COL3.add(sac_code);
							VPDF_COL4.add(es.formatAmount(""+VGITEM_AMT.elementAt(i)));
							VPDF_COL5.add(VTAX_STRUCTURE.elementAt(i));
							VPDF_COL6.add(es.formatAmount(""+VTAX_AMT.elementAt(i)));
							VPDF_COL7.add(VTAX_STRUCTURE1.elementAt(i));
							VPDF_COL8.add(es.formatAmount(""+VTAX_AMT.elementAt(i)));
							VPDF_COL9.add(es.formatAmount(""+VITEM_TOTAL.elementAt(i)));
						}
						else 
						{
							srno+=1;
							VPDF_COL1.add(srno);
							VPDF_COL2.add(VGAMT_DES.elementAt(i));
							VPDF_COL3.add(sac_code);
							VPDF_COL4.add(es.formatAmount(""+VGITEM_AMT.elementAt(i)));
							VPDF_COL5.add(VTAX_STRUCTURE_DESC.elementAt(i));
							VPDF_COL6.add(es.formatAmount(""+VTAX_AMT.elementAt(i)));
							VPDF_COL7.add(es.formatAmount(""+VITEM_TOTAL.elementAt(i)));
						}
					}
					
					if(tax_struct_info.contains("CGST"))
					{
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("<b>Gross Amount</b>");
						VPDF_COL3.add("");
						VPDF_COL4.add("");
						VPDF_COL5.add("");
						VPDF_COL6.add("");
						VPDF_COL7.add("");
						VPDF_COL8.add("");
						VPDF_COL9.add("<b>"+es.formatAmount(gross_amt)+"</b>");
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("<b>Total Tax Amount</b>");
						VPDF_COL3.add("");
						VPDF_COL4.add("");
						VPDF_COL5.add("");
						VPDF_COL6.add("");
						VPDF_COL7.add("");
						VPDF_COL8.add("");
						VPDF_COL9.add("<b>"+es.formatAmount(tax_amt)+"</b>");
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("<b>Total Amount in INR</b>");
						VPDF_COL3.add("");
						VPDF_COL4.add("");
						VPDF_COL5.add("");
						VPDF_COL6.add("");
						VPDF_COL7.add("");
						VPDF_COL8.add("");
						VPDF_COL9.add("<b>"+es.formatAmount(net_amt)+"</b>");
					}
					else 
					{
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("<b>Gross Amount</b>");
						VPDF_COL3.add("");
						VPDF_COL4.add("");
						VPDF_COL5.add("");
						VPDF_COL6.add("");
						VPDF_COL7.add("<b>"+es.formatAmount(gross_amt)+"</b>");
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("<b>Total Tax Amount</b>");
						VPDF_COL3.add("");
						VPDF_COL4.add("");
						VPDF_COL5.add("");
						VPDF_COL6.add("");
						VPDF_COL7.add("<b>"+es.formatAmount(tax_amt)+"</b>");
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("<b>Total Amount in INR</b>");
						VPDF_COL3.add("");
						VPDF_COL4.add("");
						VPDF_COL5.add("");
						VPDF_COL6.add("");
						VPDF_COL7.add("<b>"+es.formatAmount(net_amt)+"</b>");
					}
				}
				
				if(!net_amt.equalsIgnoreCase(""))		
				{
					amt_in_word=es.convert(Double.parseDouble(net_amt));
				}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//DT20260202
	public void getAhplInvoiceDtl()
	{
		String function_nm="getAhplInvoiceDtl()";

		try
		{
			BigDecimal factor = new BigDecimal(1);
			queryString = "SELECT SUPPLIER_CD,VENDOR_CD,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'),GROSS_AMT,TAX_AMT_INR,NET_PAYABLE,"
					+ "INVOICE_RAISED_IN,REMARK,REMARK1,REMARK2,REMARK3 "
					+ "FROM FMS_OTH_INVOICE_MST "
					+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND INVOICE_NO=? "
					+ "AND INVOICE_NO IS NOT NULL AND INV_FLAG='F' ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_type);
			stmt.setString(3, sel_inv_no);
			rset = stmt.executeQuery();
			while (rset.next()) 
			{
				supp_cd = rset.getString(1)==null?"":rset.getString(1);
				vendor_cd = rset.getString(2)==null?"":rset.getString(2);
				supp_nm=getEntity_nm(supp_cd,"S");
				vendor_name=getEntity_nm(vendor_cd,"V");
				main_invoice_dt=rset.getString(3)==null?"":rset.getString(3);
				main_invoice_due_dt=rset.getString(4)==null?"":rset.getString(4);
				double grossAmt=rset.getDouble(5);
				double taxAmt=rset.getDouble(6);
				double netPayable=rset.getDouble(7);
				invoice_raised_in=rset.getString(8)==null?"":rset.getString(8);
				invoice_raised_in_nm=utilBean.getRateUnitNm(conn, invoice_raised_in);
				remark1=rset.getString(9)==null?"":rset.getString(9);
				remark2=rset.getString(10)==null?"":rset.getString(10);
				remark3=rset.getString(11)==null?"":rset.getString(11);
				remark4=rset.getString(12)==null?"":rset.getString(12);
				
				BigDecimal grossAmt1 = BigDecimal.valueOf(grossAmt);
				BigDecimal sub_grossAmt1 = grossAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal taxAmt1 = BigDecimal.valueOf(taxAmt);
				BigDecimal sub_taxAmt1 = taxAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal netPayable1 = BigDecimal.valueOf(netPayable);
				BigDecimal sub_netPayable1 = netPayable1.divide(factor, 0, RoundingMode.HALF_UP);
				
				main_gross_amt=nf.format(sub_grossAmt1);				
				main_tax_amt=nf.format(sub_taxAmt1);				
				main_net_payable=nf.format(sub_netPayable1);	
				
				queryString1 = "SELECT SALE_PRICE_UNIT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),SAC_CODE,ITEM_DESCRIPTION "
						+ "FROM FMS_OTH_INVOICE_DTL "
						+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND INVOICE_NO=? "
						+ "AND INVOICE_NO IS NOT NULL AND INV_FLAG='F' ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, inv_type);
				stmt1.setString(3, sel_inv_no);
				rset1 = stmt1.executeQuery();
				while(rset1.next()) 
				{
					price_cd=rset1.getString(1)==null?"":rset1.getString(1);
					price_cd_nm=utilBean.getRateUnitNm(conn, price_cd);
					main_tax_struct_cd=rset1.getString(2)==null?"":rset1.getString(2);
					main_tax_struct_info=utilBean.getTaxDescr(conn,main_tax_struct_cd);
					main_tax_struct_dt = rset1.getString(3)==null?"":rset1.getString(3);
					sac_cd = rset1.getString(4)==null?"":rset1.getString(4);
					desc_item = rset1.getString(5)==null?"":rset1.getString(5);
				}
				rset1.close();
				stmt1.close();
				
				queryString2 = "SELECT SAC_CODE,SAC_DESC "
						+ "FROM FMS_SAC_MST A "
						+ "WHERE SAC_CD  = ?";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1,sac_cd);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					sac_code = rset2.getString(1)==null?"":rset2.getString(1);	
					sac_des = rset2.getString(2)==null?"":rset2.getString(2);	
				}
				rset2.close();
				stmt2.close();
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	//DT20260203
	public void getAhplCrDrInvoiceDtl()
	{
		String function_nm="getAhplCrDrInvoiceDtl()";
		try
		{
			BigDecimal factor = new BigDecimal(1);
			queryString = "SELECT SUPPLIER_CD,VENDOR_CD,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'),GROSS_AMT,TAX_AMT_INR,NET_PAYABLE,"
					+ "INVOICE_RAISED_IN,INV_FLAG,CRITERIA,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),REMARK,FINANCIAL_YEAR,"
					+ "APPROVED_FLAG,CHECKED_FLAG,INVOICE_NO,INVOICE_ID_SEQ "
					+ "FROM FMS_OTH_INVOICE_MST "
					+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
					+ "AND INVOICE_SEQ=? "
					+ "AND INV_FLAG IN ('CR','DR') ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_type);
			stmt.setString(3, fin_yr);
			stmt.setString(4, inv_seq);
			rset = stmt.executeQuery();
			while (rset.next()) 
			{
				supp_cd = rset.getString(1)==null?"":rset.getString(1);
				vendor_cd = rset.getString(2)==null?"":rset.getString(2);
				supp_nm=getEntity_nm(supp_cd,"S");
				vendor_name=getEntity_nm(vendor_cd,"V");
				crdr_invoice_dt=rset.getString(3)==null?"":rset.getString(3);
				crdr_invoice_due_dt=rset.getString(4)==null?"":rset.getString(4);
				double grossAmt=rset.getDouble(5);
				double taxAmt=rset.getDouble(6);
				double netPayable=rset.getDouble(7);
				invoice_raised_in=rset.getString(8)==null?"":rset.getString(8);
				invoice_raised_in_nm=utilBean.getRateUnitNm(conn, invoice_raised_in);
				cr_dr_type=rset.getString(9)==null?"":rset.getString(9);
				criteri_formula=rset.getString(10)==null?"":rset.getString(10);
				period_start_dt=rset.getString(11)==null?"":rset.getString(11);
				period_end_dt=rset.getString(12)==null?"":rset.getString(12);
				crdr_remark=rset.getString(13)==null?"":rset.getString(13);
				financial_year=rset.getString(14)==null?"":rset.getString(14);
				inv_no=rset.getString(17)==null?"":rset.getString(17);
				inv_id_seq=rset.getString(18)==null?"":rset.getString(18);
				
				if(callFlag.equalsIgnoreCase("AHPL_CRDR_APROVAL"))
				{
					if(Double.doubleToRawLongBits(grossAmt)<Double.doubleToRawLongBits(0))
					{
						grossAmt=(-1)*grossAmt;
					}
					if(Double.doubleToRawLongBits(taxAmt)<Double.doubleToRawLongBits(0))
					{
						taxAmt=(-1)*taxAmt;
					}
					if(Double.doubleToRawLongBits(netPayable)<Double.doubleToRawLongBits(0))
					{
						netPayable=(-1)*netPayable;
					}
				}
				
				BigDecimal grossAmt1 = BigDecimal.valueOf(grossAmt);
				BigDecimal sub_grossAmt1 = grossAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal taxAmt1 = BigDecimal.valueOf(taxAmt);
				BigDecimal sub_taxAmt1 = taxAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal netPayable1 = BigDecimal.valueOf(netPayable);
				BigDecimal sub_netPayable1 = netPayable1.divide(factor, 0, RoundingMode.HALF_UP);
				
				gross_amt=nf.format(sub_grossAmt1);				
				tax_amt=nf.format(sub_taxAmt1);				
				net_payable=nf.format(sub_netPayable1);				
				
				if(opration.equals("CHECK"))
				{
					activity_value = rset.getString(16) == null ? "" : rset.getString(16);
				}
				else if(opration.equals("APPROVE"))
				{
					activity_value = rset.getString(15) == null ? "" : rset.getString(15);
				}
				
				queryString1 = "SELECT TAX_STRUCT_CD "
						+ "FROM FMS_OTH_INVOICE_DTL "
						+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
						+ "AND INVOICE_SEQ=? "
						+ "AND INV_FLAG IN ('CR','DR') ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, inv_type);
				stmt1.setString(3, fin_yr);
				stmt1.setString(4, inv_seq);
				rset1 = stmt1.executeQuery();
				while(rset1.next()) 
				{
					tax_struct_cd=rset1.getString(1)==null?"":rset1.getString(1);
					tax_struct_info=utilBean.getTaxDescr(conn,main_tax_struct_cd);
				}
				rset1.close();
				stmt1.close();
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getAhplCrDrRefDetail()
	{
		String function_nm="getAhplCrDrRefDetail()";
		try
		{
			BigDecimal factor = new BigDecimal(1);
			queryString = "SELECT GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,NET_PAYABLE_AMT,SALE_PRICE "
					+ "FROM FMS_OTH_INV_CRDR_REF "
					+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
					+ "AND SUPPLIER_CD=? AND VENDOR_CD=? AND INVOICE_SEQ=? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_type);
			stmt.setString(3, financial_year);
			stmt.setString(4, supp_cd);
			stmt.setString(5, vendor_cd);
			stmt.setString(6, inv_seq);
			rset = stmt.executeQuery();
			while (rset.next()) 
			{
				double grossAmt = rset.getDouble(1);
				double taxAmt=rset.getDouble(2);
				new_tax_struct_cd=rset.getString(3)==null?"":rset.getString(3);
				new_tax_struct_info=utilBean.getTaxDescr(conn,new_tax_struct_cd);
				double netPayable=rset.getDouble(4);
				double tmp_rate=rset.getDouble(5);
				
				BigDecimal grossAmt1 = BigDecimal.valueOf(grossAmt);
				BigDecimal sub_grossAmt1 = grossAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal taxAmt1 = BigDecimal.valueOf(taxAmt);
				BigDecimal sub_taxAmt1 = taxAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal netPayable1 = BigDecimal.valueOf(netPayable);
				BigDecimal sub_netPayable1 = netPayable1.divide(factor, 0, RoundingMode.HALF_UP);
				
				new_rate=""+tmp_rate;	
				new_gross_amt=nf.format(sub_grossAmt1);				
				new_tax_amt=nf.format(sub_taxAmt1);				
				new_net_payable=nf.format(sub_netPayable1);	
				
				reason="Change in ";
				if(!criteri_formula.equals(""))
				{
					String[] split_criteri_formula = criteri_formula.split("#");
					for(int i=0; i<split_criteri_formula.length; i++)
					{
						if(i!=0)
						{
							if((i+1) == split_criteri_formula.length)
							{
								reason+=" and ";
							}
							else
							{
								reason+=", ";
							}
						}
						
						if(split_criteri_formula[i].toString().equals("GAMT"))
						{
							reason+="Gross Amount";
						}
						else if(split_criteri_formula[i].toString().equals("TAXP"))
						{
							reason+="Tax %";
						}
					}
				}
				
				
				int srno=0;
				String type = "",currency_nm = "INR";
				String dt = utilDate.getDateFormatDD_MOM_YY(main_invoice_dt);
				type = cr_dr_type.equals("CR") ? "Credit Note":"Debit Note";
				
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add(desc_item);
				VPDF_COL3.add(currency_nm);
				if(criteri_formula.contains("GAMT"))
				{
					VPDF_COL4.add("<b>"+es.formatAmount(gross_amt)+"</b>");
				}
				else 
				{
					VPDF_COL4.add("");
				}
				
				srno+=1;
				double temp_srno=srno;
				Vector VTAX_CODE = new Vector();
				Vector VTAX_DESCR = new Vector();
				Vector VTAX_AMT = new Vector();
				if (new_tax_struct_info.contains(",")) 
				{
					BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_amt));
					factor = new BigDecimal(2);
					for(int i = 0;i<new_tax_struct_info.split(",").length;i++) 
					{
						BigDecimal sub_tax_amt = tax_amt1.divide(factor, 0, RoundingMode.HALF_UP);
						temp_srno=temp_srno+0.1;
						VPDF_COL1.add(nf0.format(temp_srno));
						VPDF_COL2.add(new_tax_struct_info.split(",")[i]);
						VPDF_COL3.add(currency_nm);
						VPDF_COL4.add(es.formatAmount(""+Double.parseDouble(""+sub_tax_amt)));
						
						VTAX_CODE.add(tax_struct_cd);
						VTAX_DESCR.add(new_tax_struct_info.split(",")[i]);
						VTAX_AMT.add(nf.format(sub_tax_amt));
					}
					Vector VTEMP_TAX_DTL = new Vector();
					
					VTEMP_TAX_DTL.add(VTAX_CODE);
					VTEMP_TAX_DTL.add(VTAX_DESCR);
					VTEMP_TAX_DTL.add(VTAX_AMT);
					
					VMULTI_TAX_STRUCT.add(VTEMP_TAX_DTL);
				} 
				
				VPDF_COL1.add(srno);
				VPDF_COL2.add("<b>Tax ("+new_tax_struct_info+")</b>");
				VPDF_COL3.add("<b>"+currency_nm+"</b>");
				VPDF_COL4.add("<b>"+es.formatAmount(tax_amt)+"</b>");

				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("<b>Total Amount in INR</b>");
				VPDF_COL3.add("<b>"+currency_nm+"</b>");
				VPDF_COL4.add("<b>"+es.formatAmount(net_payable)+"</b>");
			
				if(!net_payable.equalsIgnoreCase(""))		
				{
					amt_in_word=es.convert(Double.parseDouble(net_payable));
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
	
	
	public String getEntity_state(String cd,String type)
	{
		String function_nm="getEntity_state()";
		String entity_nm="";
		try
		{
			String queryString0 = "SELECT A.STATE "
					+ "FROM FMS_OTH_ENTITY_ADDR_MST A "
					+ "WHERE A.ENTITY_CD=? AND A.ENTITY_TYPE = ? AND A.ADDRESS_TYPE = 'R' "
					+ "AND EFF_DT = (SELECT MAX(B.EFF_DT) FROM FMS_OTH_ENTITY_ADDR_MST B WHERE A.ENTITY_TYPE = B.ENTITY_TYPE AND A.ENTITY_CD=B.ENTITY_CD) ";
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
	
	//DT20260205
	public void getPFAInvoiceDtl()
	{
		String function_nm="getPFAInvoiceDtl()";

		try
		{
			BigDecimal factor = new BigDecimal(1);
			queryString = "SELECT SUPPLIER_CD,VENDOR_CD,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'),SALE_AMT,TAX_AMT_INR,NET_PAYABLE,"
					+ "INVOICE_RAISED_IN,REMARK,REMARK1,REMARK2,REMARK3,EXCHG_RATE_VALUE,GROSS_AMT,EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY') "
					+ "FROM FMS_OTH_INVOICE_MST "
					+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND INVOICE_NO=? "
					+ "AND INVOICE_NO IS NOT NULL AND INV_FLAG='F' ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_type);
			stmt.setString(3, sel_inv_no);
			rset = stmt.executeQuery();
			while (rset.next()) 
			{
				supp_cd = rset.getString(1)==null?"":rset.getString(1);
				vendor_cd = rset.getString(2)==null?"":rset.getString(2);
				supp_nm=getEntity_nm(supp_cd,"S");
				vendor_name=getEntity_nm(vendor_cd,"S");
				supp_state = getEntity_state(supp_cd,"S");
				vend_state = getEntity_state(vendor_cd,"S");
				main_invoice_dt=rset.getString(3)==null?"":rset.getString(3);
				main_invoice_due_dt=rset.getString(4)==null?"":rset.getString(4);
				double grossAmt=rset.getDouble(5);
				double taxAmt=rset.getDouble(6);
				double netPayable=rset.getDouble(7);
				invoice_raised_in=rset.getString(8)==null?"":rset.getString(8);
				invoice_raised_in_nm=utilBean.getRateUnitNm(conn, invoice_raised_in);
				remark1=rset.getString(9)==null?"":rset.getString(9);
				remark2=rset.getString(10)==null?"":rset.getString(10);
				remark3=rset.getString(11)==null?"":rset.getString(11);
				remark4=rset.getString(12)==null?"":rset.getString(12);
				main_exchang_rate = rset.getString(13)==null?"":nf2.format(rset.getDouble(13));
				main_gross_amt1=rset.getString(14)==null?"":nf.format(rset.getDouble(14));
				exchange_rate_cd = rset.getString(15)==null?"":rset.getString(15);
				exchng_eff_dt = rset.getString(16)==null?"":rset.getString(16);
				
				BigDecimal grossAmt1 = BigDecimal.valueOf(grossAmt);
				BigDecimal sub_grossAmt1 = grossAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal taxAmt1 = BigDecimal.valueOf(taxAmt);
				BigDecimal sub_taxAmt1 = taxAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal netPayable1 = BigDecimal.valueOf(netPayable);
				BigDecimal sub_netPayable1 = netPayable1.divide(factor, 0, RoundingMode.HALF_UP);
				
				main_gross_amt=nf.format(sub_grossAmt1);				
				main_tax_amt=nf.format(sub_taxAmt1);				
				main_net_payable=nf.format(sub_netPayable1);	
				
				queryString1 = "SELECT SALE_PRICE_UNIT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),SAC_CODE,ITEM_DESCRIPTION,CARGO_TYPE "
						+ "FROM FMS_OTH_INVOICE_DTL "
						+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND INVOICE_NO=? "
						+ "AND INVOICE_NO IS NOT NULL AND INV_FLAG='F' ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, inv_type);
				stmt1.setString(3, sel_inv_no);
				rset1 = stmt1.executeQuery();
				while(rset1.next()) 
				{
					price_cd=rset1.getString(1)==null?"":rset1.getString(1);
					price_cd_nm=utilBean.getRateUnitNm(conn, price_cd);
					main_tax_struct_cd=rset1.getString(2)==null?"":rset1.getString(2);
					main_tax_struct_info=utilBean.getTaxDescr(conn,main_tax_struct_cd);
					main_tax_struct_dt = rset1.getString(3)==null?"":rset1.getString(3);
					sac_cd = rset1.getString(4)==null?"":rset1.getString(4);
					desc_item = rset1.getString(5)==null?"":rset1.getString(5);
					cargo_type = rset1.getString(6)==null?"":rset1.getString(6);
				}
				rset1.close();
				stmt1.close();
				
				queryString2 = "SELECT SAC_CODE,SAC_DESC "
						+ "FROM FMS_SAC_MST A "
						+ "WHERE SAC_CD  = ?";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1,sac_cd);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					sac_code = rset2.getString(1)==null?"":rset2.getString(1);	
					sac_des = rset2.getString(2)==null?"":rset2.getString(2);	
				}
				rset2.close();
				stmt2.close();
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getInvoiceCargoDetails()
	{
		String function_nm="getInvoiceCargoDetails()";
		try
		{
			int count=0;
			String ship_cd = "";
			queryString	= "SELECT REFERENCE_NO,TO_CHAR(CARGO_DT,'DD-MON-YY'),CARGO_TYPE,VESSEL_CD,NVL(QUANTITY,'0'),SALE_PRICE,CARGO_AMOUNT "
					+ "FROM FMS_OTH_INVOICE_DTL "
					+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE = ? AND INVOICE_NO = ? AND INV_FLAG = ? ";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(++count, comp_cd);
			stmt.setString(++count,inv_type);
			stmt.setString(++count,sel_inv_no);
			stmt.setString(++count, "F");
			rset=stmt.executeQuery();
			while(rset.next())
			{
				ship_cd = rset.getString(4)==null?"":rset.getString(4);
				VSHIP_CD.add(ship_cd);
				VCARGO_REF.add(rset.getString(1)==null?"":rset.getString(1));
				VCARGO_DT.add(rset.getString(2)==null?"":rset.getString(2));
				VCARGO_TYPE.add(rset.getString(3)==null?"":rset.getString(3));
				VSHIP_NAME.add(utilBean.getShipName(conn, ship_cd));
				VMAIN_QTY_MMBTU.add(rset.getString(5)==null?"":nf.format(rset.getDouble(5)));
				VMAIN_RATE.add(rset.getString(6)==null?"":nf2.format(rset.getDouble(6)));
				VMAIN_CARGO_AMT.add(rset.getString(7)==null?"":nf.format(rset.getDouble(7)));
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getPFACrDrInvoiceDtl()
	{
		String function_nm="getPFACrDrInvoiceDtl()";

		try
		{
			BigDecimal factor = new BigDecimal(1);
			queryString = "SELECT SUPPLIER_CD,VENDOR_CD,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'),GROSS_AMT,TAX_AMT_INR,NET_PAYABLE,"
					+ "INVOICE_RAISED_IN,INV_FLAG,CRITERIA,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),REMARK,FINANCIAL_YEAR,"
					+ "APPROVED_FLAG,CHECKED_FLAG,INVOICE_NO,INVOICE_ID_SEQ,EXCHG_RATE_VALUE,SALE_AMT "
					+ "FROM FMS_OTH_INVOICE_MST "
					+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
					+ "AND INVOICE_SEQ=? "
					+ "AND INV_FLAG IN ('CR','DR') ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_type);
			stmt.setString(3, fin_yr);
			stmt.setString(4, inv_seq);
			rset = stmt.executeQuery();
			while (rset.next()) 
			{
				supp_cd = rset.getString(1)==null?"":rset.getString(1);
				vendor_cd = rset.getString(2)==null?"":rset.getString(2);
				crdr_invoice_dt=rset.getString(3)==null?"":rset.getString(3);
				crdr_invoice_due_dt=rset.getString(4)==null?"":rset.getString(4);
				double grossAmt=rset.getDouble(5);
				double taxAmt=rset.getDouble(6);
				double netPayable=rset.getDouble(7);
				invoice_raised_in=rset.getString(8)==null?"":rset.getString(8);
				invoice_raised_in_nm=utilBean.getRateUnitNm(conn, invoice_raised_in);
				cr_dr_type=rset.getString(9)==null?"":rset.getString(9);
				criteri_formula=rset.getString(10)==null?"":rset.getString(10);
				period_start_dt=rset.getString(11)==null?"":rset.getString(11);
				period_end_dt=rset.getString(12)==null?"":rset.getString(12);
				crdr_remark=rset.getString(13)==null?"":rset.getString(13);
				financial_year=rset.getString(14)==null?"":rset.getString(14);
				inv_no=rset.getString(17)==null?"":rset.getString(17);
				inv_id_seq=rset.getString(18)==null?"":rset.getString(18);
				double grossamt11=rset.getDouble(20);
				double exchngrate = rset.getDouble(19);
				
				if(callFlag.equalsIgnoreCase("PFA_CRDR_APROVAL"))
				{
					if(Double.doubleToRawLongBits(grossamt11)<Double.doubleToRawLongBits(0))
					{
						grossamt11=(-1)*grossamt11;
					}
					if(Double.doubleToRawLongBits(grossAmt)<Double.doubleToRawLongBits(0))
					{
						grossAmt=(-1)*grossAmt;
					}
					if(Double.doubleToRawLongBits(exchngrate)<Double.doubleToRawLongBits(0))
					{
						exchngrate=(-1)*exchngrate;
					}
					if(Double.doubleToRawLongBits(taxAmt)<Double.doubleToRawLongBits(0))
					{
						taxAmt=(-1)*taxAmt;
					}
					if(Double.doubleToRawLongBits(netPayable)<Double.doubleToRawLongBits(0))
					{
						netPayable=(-1)*netPayable;
					}
				}
				
				BigDecimal grossAmt1 = BigDecimal.valueOf(grossAmt);
				BigDecimal sub_grossAmt1 = grossAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal grossAmt11 = BigDecimal.valueOf(grossamt11);
				BigDecimal subArossAmt11 = grossAmt11.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal taxAmt1 = BigDecimal.valueOf(taxAmt);
				BigDecimal sub_taxAmt1 = taxAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal netPayable1 = BigDecimal.valueOf(netPayable);
				BigDecimal sub_netPayable1 = netPayable1.divide(factor, 0, RoundingMode.HALF_UP);
				
				gross_amt=nf.format(subArossAmt11);				
				tax_amt=nf.format(sub_taxAmt1);				
				net_payable=nf.format(sub_netPayable1);				
				gross_amt1=nf.format(sub_grossAmt1);	 			
				exchng_rate=nf2.format(exchngrate);	 			
				
				if(opration.equals("CHECK"))
				{
					activity_value = rset.getString(16) == null ? "" : rset.getString(16);
				}
				else if(opration.equals("APPROVE"))
				{
					activity_value = rset.getString(15) == null ? "" : rset.getString(15);
				}
				
				queryString1 = "SELECT TAX_STRUCT_CD "
						+ "FROM FMS_OTH_INVOICE_DTL "
						+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
						+ "AND INVOICE_SEQ=? "
						+ "AND INV_FLAG IN ('CR','DR') ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, inv_type);
				stmt1.setString(3, fin_yr);
				stmt1.setString(4, inv_seq);
				rset1 = stmt1.executeQuery();
				if(rset1.next()) 
				{
					tax_struct_cd=rset1.getString(1)==null?"":rset1.getString(1);
					tax_struct_info=utilBean.getTaxDescr(conn,main_tax_struct_cd);
				}
				rset1.close();
				stmt1.close();
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	public void getPFACrDrCargoDetails()
	{
		String function_nm="getPFACrDrCargoDetails()";
		try
		{
			for(int i = 0;i<VSHIP_CD.size();i++)
			{
				queryString	= "SELECT NVL(QUANTITY,'0'),SALE_PRICE,CARGO_AMOUNT "
						+ "FROM FMS_OTH_INVOICE_DTL "
						+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
						+ "AND INVOICE_SEQ=? AND REFERENCE_NO = ? "
						+ "AND INV_FLAG IN ('CR','DR') ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_type);
				stmt.setString(3, fin_yr);
				stmt.setString(4, inv_seq);
				stmt.setString(5, ""+VCARGO_REF.elementAt(i));
				rset=stmt.executeQuery();
				if(rset.next())
				{
					VQTY_MMBTU.add(rset.getString(1)==null?"":nf.format(rset.getDouble(1)));
					VRATE.add(rset.getString(2)==null?"":nf2.format(rset.getDouble(2)));
					VCARGO_AMT.add(rset.getString(3)==null?"":nf.format(rset.getDouble(3)));
					IS_CRDR_SUBMITTED.add("Y");
				}
				else 
				{
					VQTY_MMBTU.add("");
					VRATE.add("");
					VCARGO_AMT.add("");
					IS_CRDR_SUBMITTED.add("");
				}
				rset.close();
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getnewCargodetails()
	{
		String function_nm="getnewCargodetails()";
		try
		{
			if(cargo_type.equals("LTCORA"))
			{
				for(int i = 0;i<VSHIP_CD.size();i++)
				{
					queryString = "SELECT NVL(B.ADQ_QTY,'0') "
							+ "FROM FMS_LTCORA_CONT_CARGO_DTL A, FMS_LTCORA_CONT_CARGO_ADQ B "
							+ "WHERE A.COMPANY_CD=? AND A.CARGO_REF = ? AND A.SHIP_CD = ? AND A.QQ_DT = TO_DATE(?, 'DD-MON-YY') AND B.ADQ_QTY!=0  AND A.BUY_SALE = 'C' "
							+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND A.BUY_SALE = B.BUY_SALE AND A.AGMT_TYPE = B.AGMT_TYPE "
							+ "AND A.AGMT_NO = B.AGMT_NO AND A.CONT_NO = B.CONT_NO AND A.CONTRACT_TYPE = B.CONTRACT_TYPE AND A.CARGO_NO = B.CARGO_NO "
							+ "AND B.ADQ_QTY!=?";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1,comp_cd);
					stmt.setString(2,""+VCARGO_REF.elementAt(i));
					stmt.setString(3,""+VSHIP_CD.elementAt(i));
					stmt.setString(4,""+VCARGO_DT.elementAt(i));
					stmt.setString(5,""+VMAIN_QTY_MMBTU.elementAt(i));
					rset=stmt.executeQuery();
					if(rset.next())
					{
						VNEW_QTY_MMBTU.add(rset.getString(1)==null?"":nf.format(rset.getDouble(1)));
						VNEW_RATE.add(VMAIN_RATE.elementAt(i));
						VNEW_CARGO_AMT.add(VMAIN_CARGO_AMT.elementAt(i));
					}
					else 
					{
						VNEW_QTY_MMBTU.add(VMAIN_QTY_MMBTU.elementAt(i));
						VNEW_RATE.add(VMAIN_RATE.elementAt(i));
						VNEW_CARGO_AMT.add(VMAIN_CARGO_AMT.elementAt(i));
					}
					rset.close();
					stmt.close();
				}
				
			}
			else if(cargo_type.equals("SALES"))
			{
				for(int i = 0;i<VSHIP_CD.size();i++)
				{
					queryString = "SELECT NVL(A.QQ_QTY_MMBTU,'0') "
							+ "FROM FMS_BUY_CARGO_ALLOC A, FMS_TRADER_CARGO_MST B "
							+ "WHERE A.COMPANY_CD=? AND A.SHIP_CD = ? AND B.CARGO_REF = ? AND A.QQ_DT = TO_DATE(?, 'DD-MON-YY') AND A.QQ_QTY_MMBTU!='0' "
							+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.COUNTERPARTY_CD = B.COUNTERPARTY_CD AND A.AGMT_TYPE = B.AGMT_TYPE "
							+ "AND A.AGMT_NO = B.AGMT_NO AND A.CONT_NO = B.CONT_NO AND A.CONTRACT_TYPE = B.CONTRACT_TYPE AND A.CARGO_NO = B.CARGO_NO "
							+ "AND A.QQ_QTY_MMBTU!=? ";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1,comp_cd);
					stmt.setString(2,""+VSHIP_CD.elementAt(i));
					stmt.setString(3,""+VCARGO_REF.elementAt(i));
					stmt.setString(4,""+VCARGO_DT.elementAt(i));
					stmt.setString(5,""+VMAIN_QTY_MMBTU.elementAt(i));
					rset=stmt.executeQuery();
					if(rset.next())
					{
						VNEW_QTY_MMBTU.add(rset.getString(1)==null?"":nf.format(rset.getDouble(1)));
						VNEW_RATE.add(VMAIN_RATE.elementAt(i));
						VNEW_CARGO_AMT.add(VMAIN_CARGO_AMT.elementAt(i));
					}
					else 
					{
						VNEW_QTY_MMBTU.add(VMAIN_QTY_MMBTU.elementAt(i));
						VNEW_RATE.add(VMAIN_RATE.elementAt(i));
						VNEW_CARGO_AMT.add(VMAIN_CARGO_AMT.elementAt(i));
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
	
	
	public void getPFACrDrRefCargoDetails()
	{
		String function_nm="getPFACrDrRefCargoDetails()";
		try
		{
			for(int i = 0;i<VSHIP_CD.size();i++)
			{
				queryString	= "SELECT NVL(QUANTITY,'0'),SALE_PRICE,CARGO_AMOUNT "
						+ "FROM FMS_OTH_INV_CRDR_REF "
						+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
						+ "AND INVOICE_SEQ=? AND REFERENCE_NO = ? ";
				stmt = conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_type);
				stmt.setString(3, fin_yr);
				stmt.setString(4, inv_seq);
				stmt.setString(5, ""+VCARGO_REF.elementAt(i));
				rset=stmt.executeQuery();
				if(rset.next())
				{
					VNEW_QTY_MMBTU.add(rset.getString(1)==null?"":nf.format(rset.getDouble(1)));
					VNEW_RATE.add(rset.getString(2)==null?"":nf2.format(rset.getDouble(2)));
					VNEW_CARGO_AMT.add(rset.getString(3)==null?"":nf.format(rset.getDouble(3)));
				}
				else 
				{
					VNEW_QTY_MMBTU.add(VMAIN_QTY_MMBTU.elementAt(i));
					VNEW_RATE.add(VMAIN_RATE.elementAt(i));
					VNEW_CARGO_AMT.add(VMAIN_CARGO_AMT.elementAt(i));
				}
				rset.close();
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	
	public void getPFACrDrRefDetail()
	{
		String function_nm="getPFACrDrRefDetail()";
		try
		{
			BigDecimal factor = new BigDecimal(1);
			queryString = "SELECT GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,NET_PAYABLE_AMT,EXCHG_RATE_VALUE,SALE_AMT "
					+ "FROM FMS_OTH_INV_CRDR_REF "
					+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
					+ "AND SUPPLIER_CD=? AND VENDOR_CD=? AND INVOICE_SEQ=? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_type);
			stmt.setString(3, financial_year);
			stmt.setString(4, supp_cd);
			stmt.setString(5, vendor_cd);
			stmt.setString(6, inv_seq);
			rset = stmt.executeQuery();
			if (rset.next()) 
			{
				double grossAmt = rset.getDouble(1);
				double taxAmt=rset.getDouble(2);
				new_tax_struct_cd=rset.getString(3)==null?"":rset.getString(3);
				new_tax_struct_info=utilBean.getTaxDescr(conn,new_tax_struct_cd);
				double netPayable=rset.getDouble(4);
				double exchgrt=rset.getDouble(5);
				double grossamt11=rset.getDouble(6);
				
				BigDecimal grossAmt1 = BigDecimal.valueOf(grossAmt);
				BigDecimal sub_grossAmt1 = grossAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal taxAmt1 = BigDecimal.valueOf(taxAmt);
				BigDecimal sub_taxAmt1 = taxAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal grossAmt11 = BigDecimal.valueOf(grossamt11);
				BigDecimal sub_grossAmt11 = grossAmt11.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal netPayable1 = BigDecimal.valueOf(netPayable);
				BigDecimal sub_netPayable1 = netPayable1.divide(factor, 0, RoundingMode.HALF_UP);
				
				new_gross_amt=nf.format(sub_grossAmt11);				
				new_gross_amt1=nf.format(sub_grossAmt1);				
				new_exchang_rate=nf2.format(exchgrt);				
				new_tax_amt=nf.format(sub_taxAmt1);				
				new_net_payable=nf.format(sub_netPayable1);	
				
				reason="Change in ";
				if(!criteri_formula.equals(""))
				{
					String[] split_criteri_formula = criteri_formula.split("#");
					for(int i=0; i<split_criteri_formula.length; i++)
					{
						if(i!=0)
						{
							if((i+1) == split_criteri_formula.length)
							{
								reason+=" and ";
							}
							else
							{
								reason+=", ";
							}
						}
						
						if(split_criteri_formula[i].toString().equals("RATE"))
						{
							reason+="Rate";
						}
						else if(split_criteri_formula[i].toString().equals("TAXP"))
						{
							reason+="Tax %";
						}
						else if(split_criteri_formula[i].toString().equals("EXCHG"))
						{
							reason+="Exchange Rate";
						}
						else if(split_criteri_formula[i].toString().equals("QTY"))
						{
							reason+="Quantity";
						}
					}
				}
				if(criteri_formula.contains("QTY"))
				{
					for(int i = 0; i<VSHIP_CD.size();i++)
					{
						if(!VNEW_QTY_MMBTU.elementAt(i).equals(VMAIN_QTY_MMBTU.elementAt(i)))
						{
							VNSHIP_NAME.add(VSHIP_NAME.elementAt(i));
							VNQTY_MMBTU.add(VNEW_QTY_MMBTU.elementAt(i));
							VNCARGO_DATE.add(VCARGO_DT.elementAt(i));
							VNCARGO_RATE.add(Math.abs(Double.parseDouble(VNEW_RATE.elementAt(i).toString())));
							VNCARGO_AMT.add(es.formatAmount("" + Math.abs(Double.parseDouble(VCARGO_AMT.elementAt(i).toString()))));

						}
					}
				}
				else if(criteri_formula.contains("RATE"))
				{
					for(int i = 0; i<VSHIP_CD.size();i++)
					{
						if(!VRATE.elementAt(i).equals(""))
						{
							VNSHIP_NAME.add(VSHIP_NAME.elementAt(i));
							VNQTY_MMBTU.add(VMAIN_QTY_MMBTU.elementAt(i));
							VNCARGO_DATE.add(VCARGO_DT.elementAt(i));
							VNCARGO_RATE.add(Math.abs(Double.parseDouble(VRATE.elementAt(i).toString())));
							VNCARGO_AMT.add(es.formatAmount("" + Math.abs(Double.parseDouble(VCARGO_AMT.elementAt(i).toString()))));
						}
					}
				}
				else if(criteri_formula.contains("EXCHG") || criteri_formula.contains("TAXP"))
				{
					for(int i = 0; i<VSHIP_CD.size();i++)
					{
						if(!VRATE.elementAt(i).equals(""))
						{
							VNSHIP_NAME.add(VSHIP_NAME.elementAt(i));
							VNQTY_MMBTU.add(VMAIN_QTY_MMBTU.elementAt(i));
							VNCARGO_RATE.add(VMAIN_RATE.elementAt(i));
							VNCARGO_DATE.add(VCARGO_DT.elementAt(i));
							VNCARGO_AMT.add(es.formatAmount(""+VMAIN_CARGO_AMT.elementAt(i)));
						}
					}
				}
				
				int srno=0;
				String type = "",currency_nm = "INR";
				String dt = utilDate.getDateFormatDD_MOM_YY(main_invoice_dt);
				type = cr_dr_type.equals("CR") ? "Credit Note":"Debit Note";
				
				String desc_item= "PFA "+ type+" for "+reason;
				
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add(desc_item +"<br><br>For the Following Cargoes received in the month of "+utilDate.getShortMonthName(main_invoice_dt)+" "+year);
				VPDF_COL3.add(currency_nm);
				VPDF_COL4.add("");
				VPDF_COL5.add("");
				
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("");
				VPDF_COL3.add(currency_nm);
				VPDF_COL4.add("");
				VPDF_COL5.add("");
				
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("<b>Total Charges</b>");
				VPDF_COL3.add("<b>USD</b>");
				VPDF_COL4.add("");
				if(criteri_formula.contains("EXCHG"))
				{
					VPDF_COL5.add("<b>"+es.formatAmount(new_gross_amt)+"</b>");
				}
				else
				{
					VPDF_COL5.add("<b>"+es.formatAmount(gross_amt)+"</b>");
				}
				
				if(criteri_formula.contains("EXCHG"))
				{
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("Exchange Rate");
					VPDF_COL3.add("INR/USD");
					VPDF_COL4.add(main_exchang_rate);
					VPDF_COL5.add("");
					
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("Applicable Exchange Rate");
					VPDF_COL3.add("INR/USD");
					VPDF_COL4.add(new_exchang_rate);
					VPDF_COL5.add("");
					
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("Difference in Exchange Rate");
					VPDF_COL3.add("INR/USD");
					VPDF_COL4.add(exchng_rate);
					VPDF_COL5.add("");
				}
				else 
				{
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("Exchange Rate");
					VPDF_COL3.add("INR/USD");
					VPDF_COL4.add(main_exchang_rate);
					VPDF_COL5.add("");
				}
				
				
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("<b>Gross Amount</b>");
				VPDF_COL3.add("<b>INR</b>");
				VPDF_COL4.add("");
				VPDF_COL5.add("<b>"+es.formatAmount(gross_amt1)+"</b>");
				
				srno+=1;
				double temp_srno=srno;
				Vector VTAX_CODE = new Vector();
				Vector VTAX_DESCR = new Vector();
				Vector VTAX_AMT = new Vector();
				if (new_tax_struct_info.contains(",")) 
				{
					BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_amt));
					factor = new BigDecimal(2);
					for(int i = 0;i<new_tax_struct_info.split(",").length;i++) 
					{
						BigDecimal sub_tax_amt = tax_amt1.divide(factor, 0, RoundingMode.HALF_UP);
						temp_srno=temp_srno+0.1;
						VPDF_COL1.add(nf0.format(temp_srno));
						VPDF_COL2.add(new_tax_struct_info.split(",")[i]);
						VPDF_COL3.add(currency_nm);
						VPDF_COL4.add("");
						VPDF_COL5.add(es.formatAmount(""+Double.parseDouble(""+sub_tax_amt)));
						
						VTAX_CODE.add(tax_struct_cd);
						VTAX_DESCR.add(new_tax_struct_info.split(",")[i]);
						VTAX_AMT.add(nf.format(sub_tax_amt));
					}
					Vector VTEMP_TAX_DTL = new Vector();
					
					VTEMP_TAX_DTL.add(VTAX_CODE);
					VTEMP_TAX_DTL.add(VTAX_DESCR);
					VTEMP_TAX_DTL.add(VTAX_AMT);
					
					VMULTI_TAX_STRUCT.add(VTEMP_TAX_DTL);
				} 
				
				VPDF_COL1.add(srno);
				VPDF_COL2.add("<b>Tax ("+new_tax_struct_info+")</b>");
				VPDF_COL3.add("<b>"+currency_nm+"</b>");
				VPDF_COL4.add("");
				VPDF_COL5.add("<b>"+es.formatAmount(tax_amt)+"</b>");

				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("<b>Total Amount in INR</b>");
				VPDF_COL3.add("<b>"+currency_nm+"</b>");
				VPDF_COL4.add("");
				VPDF_COL5.add("<b>"+es.formatAmount(net_payable)+"</b>");
			
				if(!net_payable.equalsIgnoreCase(""))		
				{
					amt_in_word=es.convert(Double.parseDouble(net_payable));
				}
			}
			rset.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	
	public void getCostRHInvoiceDtl()
	{
		String function_nm="getCostRHInvoiceDtl()";

		try
		{
			BigDecimal factor = new BigDecimal(1);
			queryString = "SELECT SUPPLIER_CD,VENDOR_CD,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'),GROSS_AMT,TAX_AMT_INR,NET_PAYABLE,"
					+ "INVOICE_RAISED_IN,REMARK,REMARK1,REMARK2,REMARK3 "
					+ "FROM FMS_OTH_INVOICE_MST "
					+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND INVOICE_NO=? "
					+ "AND INVOICE_NO IS NOT NULL AND INV_FLAG='F' ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_type);
			stmt.setString(3, sel_inv_no);
			rset = stmt.executeQuery();
			while (rset.next()) 
			{
				supp_cd = rset.getString(1)==null?"":rset.getString(1);
				vendor_cd = rset.getString(2)==null?"":rset.getString(2);
				supp_nm=getEntity_nm(supp_cd,"S");
				vendor_name=getEntity_nm(vendor_cd,"S");
				main_invoice_dt=rset.getString(3)==null?"":rset.getString(3);
				main_invoice_due_dt=rset.getString(4)==null?"":rset.getString(4);
				double grossAmt=rset.getDouble(5);
				double taxAmt=rset.getDouble(6);
				double netPayable=rset.getDouble(7);
				invoice_raised_in=rset.getString(8)==null?"":rset.getString(8);
				invoice_raised_in_nm=utilBean.getRateUnitNm(conn, invoice_raised_in);
				remark1=rset.getString(9)==null?"":rset.getString(9);
				remark2=rset.getString(10)==null?"":rset.getString(10);
				remark3=rset.getString(11)==null?"":rset.getString(11);
				remark4=rset.getString(12)==null?"":rset.getString(12);
				
				BigDecimal grossAmt1 = BigDecimal.valueOf(grossAmt);
				BigDecimal sub_grossAmt1 = grossAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal taxAmt1 = BigDecimal.valueOf(taxAmt);
				BigDecimal sub_taxAmt1 = taxAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal netPayable1 = BigDecimal.valueOf(netPayable);
				BigDecimal sub_netPayable1 = netPayable1.divide(factor, 0, RoundingMode.HALF_UP);
				
				main_gross_amt=nf.format(sub_grossAmt1);				
				main_tax_amt=nf.format(sub_taxAmt1);				
				main_net_payable=nf.format(sub_netPayable1);	
				
				queryString1 = "SELECT SALE_PRICE_UNIT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),SAC_CODE,ITEM_DESCRIPTION "
						+ "FROM FMS_OTH_INVOICE_DTL "
						+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND INVOICE_NO=? "
						+ "AND INVOICE_NO IS NOT NULL AND INV_FLAG='F' ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, inv_type);
				stmt1.setString(3, sel_inv_no);
				rset1 = stmt1.executeQuery();
				while(rset1.next()) 
				{
					price_cd=rset1.getString(1)==null?"":rset1.getString(1);
					price_cd_nm=utilBean.getRateUnitNm(conn, price_cd);
					main_tax_struct_cd=rset1.getString(2)==null?"":rset1.getString(2);
					main_tax_struct_info=utilBean.getTaxDescr(conn,main_tax_struct_cd);
					main_tax_struct_dt = rset1.getString(3)==null?"":rset1.getString(3);
					sac_cd = rset1.getString(4)==null?"":rset1.getString(4);
					desc_item = rset1.getString(5)==null?"":rset1.getString(5);
				}
				rset1.close();
				stmt1.close();
				
				queryString2 = "SELECT SAC_CODE,SAC_DESC "
						+ "FROM FMS_SAC_MST A "
						+ "WHERE SAC_CD  = ?";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1,sac_cd);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					sac_code = rset2.getString(1)==null?"":rset2.getString(1);	
					sac_des = rset2.getString(2)==null?"":rset2.getString(2);	
				}
				rset2.close();
				stmt2.close();
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	
	public void getCostRHCrDrInvoiceDtl()
	{
		String function_nm="getCostRHCrDrInvoiceDtl()";
		try
		{
			BigDecimal factor = new BigDecimal(1);
			queryString = "SELECT SUPPLIER_CD,VENDOR_CD,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'),GROSS_AMT,TAX_AMT_INR,NET_PAYABLE,"
					+ "INVOICE_RAISED_IN,INV_FLAG,CRITERIA,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),REMARK,FINANCIAL_YEAR,"
					+ "APPROVED_FLAG,CHECKED_FLAG,INVOICE_NO,INVOICE_ID_SEQ "
					+ "FROM FMS_OTH_INVOICE_MST "
					+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
					+ "AND INVOICE_SEQ=? "
					+ "AND INV_FLAG IN ('CR','DR') ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_type);
			stmt.setString(3, fin_yr);
			stmt.setString(4, inv_seq);
			rset = stmt.executeQuery();
			while (rset.next()) 
			{
				supp_cd = rset.getString(1)==null?"":rset.getString(1);
				vendor_cd = rset.getString(2)==null?"":rset.getString(2);
				supp_nm=getEntity_nm(supp_cd,"S");
				vendor_name=getEntity_nm(vendor_cd,"S");
				crdr_invoice_dt=rset.getString(3)==null?"":rset.getString(3);
				crdr_invoice_due_dt=rset.getString(4)==null?"":rset.getString(4);
				double grossAmt=rset.getDouble(5);
				double taxAmt=rset.getDouble(6);
				double netPayable=rset.getDouble(7);
				invoice_raised_in=rset.getString(8)==null?"":rset.getString(8);
				invoice_raised_in_nm=utilBean.getRateUnitNm(conn, invoice_raised_in);
				cr_dr_type=rset.getString(9)==null?"":rset.getString(9);
				criteri_formula=rset.getString(10)==null?"":rset.getString(10);
				period_start_dt=rset.getString(11)==null?"":rset.getString(11);
				period_end_dt=rset.getString(12)==null?"":rset.getString(12);
				crdr_remark=rset.getString(13)==null?"":rset.getString(13);
				financial_year=rset.getString(14)==null?"":rset.getString(14);
				inv_no=rset.getString(17)==null?"":rset.getString(17);
				inv_id_seq=rset.getString(18)==null?"":rset.getString(18);
				
				if(callFlag.equalsIgnoreCase("COSTRH_CRDR_APROVAL"))
				{
					if(Double.doubleToRawLongBits(grossAmt)<Double.doubleToRawLongBits(0))
					{
						grossAmt=(-1)*grossAmt;
					}
					if(Double.doubleToRawLongBits(taxAmt)<Double.doubleToRawLongBits(0))
					{
						taxAmt=(-1)*taxAmt;
					}
					if(Double.doubleToRawLongBits(netPayable)<Double.doubleToRawLongBits(0))
					{
						netPayable=(-1)*netPayable;
					}
				}
				
				BigDecimal grossAmt1 = BigDecimal.valueOf(grossAmt);
				BigDecimal sub_grossAmt1 = grossAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal taxAmt1 = BigDecimal.valueOf(taxAmt);
				BigDecimal sub_taxAmt1 = taxAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal netPayable1 = BigDecimal.valueOf(netPayable);
				BigDecimal sub_netPayable1 = netPayable1.divide(factor, 0, RoundingMode.HALF_UP);
				
				gross_amt=nf.format(sub_grossAmt1);				
				tax_amt=nf.format(sub_taxAmt1);				
				net_payable=nf.format(sub_netPayable1);				
				
				if(opration.equals("CHECK"))
				{
					activity_value = rset.getString(16) == null ? "" : rset.getString(16);
				}
				else if(opration.equals("APPROVE"))
				{
					activity_value = rset.getString(15) == null ? "" : rset.getString(15);
				}
				
				queryString1 = "SELECT TAX_STRUCT_CD "
						+ "FROM FMS_OTH_INVOICE_DTL "
						+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
						+ "AND INVOICE_SEQ=? "
						+ "AND INV_FLAG IN ('CR','DR') ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, inv_type);
				stmt1.setString(3, fin_yr);
				stmt1.setString(4, inv_seq);
				rset1 = stmt1.executeQuery();
				while(rset1.next()) 
				{
					tax_struct_cd=rset1.getString(1)==null?"":rset1.getString(1);
					tax_struct_info=utilBean.getTaxDescr(conn,main_tax_struct_cd);
				}
				rset1.close();
				stmt1.close();
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCostRHCrDrRefDetail()
	{
		String function_nm="getCostRHCrDrRefDetail()";
		try
		{
			BigDecimal factor = new BigDecimal(1);
			queryString = "SELECT GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,NET_PAYABLE_AMT,SALE_PRICE "
					+ "FROM FMS_OTH_INV_CRDR_REF "
					+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
					+ "AND SUPPLIER_CD=? AND VENDOR_CD=? AND INVOICE_SEQ=? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_type);
			stmt.setString(3, financial_year);
			stmt.setString(4, supp_cd);
			stmt.setString(5, vendor_cd);
			stmt.setString(6, inv_seq);
			rset = stmt.executeQuery();
			while (rset.next()) 
			{
				double grossAmt = rset.getDouble(1);
				double taxAmt=rset.getDouble(2);
				new_tax_struct_cd=rset.getString(3)==null?"":rset.getString(3);
				new_tax_struct_info=utilBean.getTaxDescr(conn,new_tax_struct_cd);
				double netPayable=rset.getDouble(4);
				double tmp_rate=rset.getDouble(5);
				
				BigDecimal grossAmt1 = BigDecimal.valueOf(grossAmt);
				BigDecimal sub_grossAmt1 = grossAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal taxAmt1 = BigDecimal.valueOf(taxAmt);
				BigDecimal sub_taxAmt1 = taxAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal netPayable1 = BigDecimal.valueOf(netPayable);
				BigDecimal sub_netPayable1 = netPayable1.divide(factor, 0, RoundingMode.HALF_UP);
				
				new_rate=""+tmp_rate;	
				new_gross_amt=nf.format(sub_grossAmt1);				
				new_tax_amt=nf.format(sub_taxAmt1);				
				new_net_payable=nf.format(sub_netPayable1);	
				
				reason="Change in ";
				if(!criteri_formula.equals(""))
				{
					String[] split_criteri_formula = criteri_formula.split("#");
					for(int i=0; i<split_criteri_formula.length; i++)
					{
						if(i!=0)
						{
							if((i+1) == split_criteri_formula.length)
							{
								reason+=" and ";
							}
							else
							{
								reason+=", ";
							}
						}
						
						if(split_criteri_formula[i].toString().equals("GAMT"))
						{
							reason+="Gross Amount";
						}
						else if(split_criteri_formula[i].toString().equals("TAXP"))
						{
							reason+="Tax %";
						}
					}
				}
				
				
				int srno=0;
				String type = "",currency_nm = "INR";
				String dt = utilDate.getDateFormatDD_MOM_YY(main_invoice_dt);
				type = cr_dr_type.equals("CR") ? "Credit Note":"Debit Note";
				
				String desc_item= type+" against Invoice No : "+sel_inv_no+" dated "+dt;
				
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add(desc_item);
				VPDF_COL3.add("");
				VPDF_COL4.add("");
				
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("<b>Gross Amount</b>");
				VPDF_COL3.add("<b>"+currency_nm+"</b>");
				if(criteri_formula.contains("GAMT"))
				{
					VPDF_COL4.add("<b>"+es.formatAmount(gross_amt)+"</b>");
				}
				else 
				{
					VPDF_COL4.add("");
				}
				
				srno+=1;
				double temp_srno=srno;
				Vector VTAX_CODE = new Vector();
				Vector VTAX_DESCR = new Vector();
				Vector VTAX_AMT = new Vector();
				if (new_tax_struct_info.contains(",")) 
				{
					BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_amt));
					factor = new BigDecimal(2);
					for(int i = 0;i<new_tax_struct_info.split(",").length;i++) 
					{
						BigDecimal sub_tax_amt = tax_amt1.divide(factor, 0, RoundingMode.HALF_UP);
						temp_srno=temp_srno+0.1;
						VPDF_COL1.add(nf0.format(temp_srno));
						VPDF_COL2.add(new_tax_struct_info.split(",")[i]);
						VPDF_COL3.add(currency_nm);
						VPDF_COL4.add(es.formatAmount(""+Double.parseDouble(""+sub_tax_amt)));
						
						VTAX_CODE.add(tax_struct_cd);
						VTAX_DESCR.add(new_tax_struct_info.split(",")[i]);
						VTAX_AMT.add(nf.format(sub_tax_amt));
					}
					Vector VTEMP_TAX_DTL = new Vector();
					
					VTEMP_TAX_DTL.add(VTAX_CODE);
					VTEMP_TAX_DTL.add(VTAX_DESCR);
					VTEMP_TAX_DTL.add(VTAX_AMT);
					
					VMULTI_TAX_STRUCT.add(VTEMP_TAX_DTL);
				} 
				
				VPDF_COL1.add(srno);
				VPDF_COL2.add("<b>Tax ("+new_tax_struct_info+")</b>");
				VPDF_COL3.add("<b>"+currency_nm+"</b>");
				VPDF_COL4.add("<b>"+es.formatAmount(tax_amt)+"</b>");

				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("<b>Total Amount in INR</b>");
				VPDF_COL3.add("<b>"+currency_nm+"</b>");
				VPDF_COL4.add("<b>"+es.formatAmount(net_payable)+"</b>");
			
				if(!net_payable.equalsIgnoreCase(""))		
				{
					amt_in_word=es.convert(Double.parseDouble(net_payable));
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
	
	public void getCostRInvoiceDtl()
	{
		String function_nm="getCostRInvoiceDtl()";

		try
		{
			BigDecimal factor = new BigDecimal(1);
			queryString = "SELECT SUPPLIER_CD,VENDOR_CD,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'),GROSS_AMT,TAX_AMT_INR,NET_PAYABLE,"
					+ "INVOICE_RAISED_IN,REMARK,REMARK1,REMARK2,REMARK3,EXCHG_RATE_CD,TO_CHAR(EXCHG_RATE_DT,'DD/MM/YYYY'),EXCHG_RATE_VALUE,SALE_AMT "
					+ "FROM FMS_OTH_INVOICE_MST "
					+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND INVOICE_NO=? "
					+ "AND INVOICE_NO IS NOT NULL AND INV_FLAG='F' ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_type);
			stmt.setString(3, sel_inv_no);
			rset = stmt.executeQuery();
			while (rset.next()) 
			{
				supp_cd = rset.getString(1)==null?"":rset.getString(1);
				vendor_cd = rset.getString(2)==null?"":rset.getString(2);
				supp_nm=getEntity_nm(supp_cd,"S");
				vendor_name=getEntity_nm(vendor_cd,"V");
				main_invoice_dt=rset.getString(3)==null?"":rset.getString(3);
				main_invoice_due_dt=rset.getString(4)==null?"":rset.getString(4);
				double main_grossAmt1 = rset.getDouble(5);
				double taxAmt=rset.getDouble(6);
				double netPayable=rset.getDouble(7);
				invoice_raised_in=rset.getString(8)==null?"":rset.getString(8);
				invoice_raised_in_nm=utilBean.getRateUnitNm(conn, invoice_raised_in);
				remark1=rset.getString(9)==null?"":rset.getString(9);
				remark2=rset.getString(10)==null?"":rset.getString(10);
				remark3=rset.getString(11)==null?"":rset.getString(11);
				remark4=rset.getString(12)==null?"":rset.getString(12);
				exchange_rate_cd = rset.getString(13)==null?"":rset.getString(13);
				exchng_eff_dt = rset.getString(14)==null?"":rset.getString(14);
				double mn_exchang_rate=rset.getDouble(15);
				double grossAmt=rset.getDouble(16);
				
				BigDecimal grossAmt1 = BigDecimal.valueOf(grossAmt);
				BigDecimal sub_grossAmt1 = grossAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal taxAmt1 = BigDecimal.valueOf(taxAmt);
				BigDecimal sub_taxAmt1 = taxAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal netPayable1 = BigDecimal.valueOf(netPayable);
				BigDecimal sub_netPayable1 = netPayable1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal maingross_amt1 = BigDecimal.valueOf(main_grossAmt1);
				BigDecimal sub_main_grossAmt1 = maingross_amt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				main_gross_amt=nf.format(sub_grossAmt1);				
				main_tax_amt=nf.format(sub_taxAmt1);				
				main_net_payable=nf.format(sub_netPayable1);
				main_gross_amt1 = nf.format(maingross_amt1);
				main_exchang_rate = nf2.format(mn_exchang_rate);
				
				queryString1 = "SELECT SALE_PRICE_UNIT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),SAC_CODE,ITEM_DESCRIPTION "
						+ "FROM FMS_OTH_INVOICE_DTL "
						+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND INVOICE_NO=? AND VENDOR_CD = ? "
						+ "AND SUPPLIER_CD = ? AND INVOICE_NO IS NOT NULL AND INV_FLAG='F' ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, inv_type);
				stmt1.setString(3, sel_inv_no);
				stmt1.setString(4, vendor_cd);
				stmt1.setString(5, supp_cd);
				rset1 = stmt1.executeQuery();
				while(rset1.next()) 
				{
					price_cd=rset1.getString(1)==null?"":rset1.getString(1);
					price_cd_nm=utilBean.getRateUnitNm(conn, price_cd);
					main_tax_struct_cd=rset1.getString(2)==null?"":rset1.getString(2);
					main_tax_struct_info=utilBean.getTaxDescr(conn,main_tax_struct_cd);
					main_tax_struct_dt = rset1.getString(3)==null?"":rset1.getString(3);
					sac_cd = rset1.getString(4)==null?"":rset1.getString(4);
					desc_item = rset1.getString(5)==null?"":rset1.getString(5);
				}
				rset1.close();
				stmt1.close();
				
				queryString2 = "SELECT SAC_CODE,SAC_DESC "
						+ "FROM FMS_SAC_MST A "
						+ "WHERE SAC_CD  = ?";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1,sac_cd);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					sac_code = rset2.getString(1)==null?"":rset2.getString(1);	
					sac_des = rset2.getString(2)==null?"":rset2.getString(2);	
				}
				rset2.close();
				stmt2.close();
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	
	public void getCostRCrDrInvoiceDtl()
	{
		String function_nm="getCostRCrDrInvoiceDtl()";
		try
		{
			BigDecimal factor = new BigDecimal(1);
			queryString = "SELECT SUPPLIER_CD,VENDOR_CD,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'),GROSS_AMT,TAX_AMT_INR,NET_PAYABLE,"
					+ "INVOICE_RAISED_IN,INV_FLAG,CRITERIA,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),REMARK,FINANCIAL_YEAR,"
					+ "APPROVED_FLAG,CHECKED_FLAG,INVOICE_NO,INVOICE_ID_SEQ,EXCHG_RATE_VALUE,SALE_AMT "
					+ "FROM FMS_OTH_INVOICE_MST "
					+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
					+ "AND INVOICE_SEQ=? "
					+ "AND INV_FLAG IN ('CR','DR') ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_type);
			stmt.setString(3, fin_yr);
			stmt.setString(4, inv_seq);
			rset = stmt.executeQuery();
			while (rset.next()) 
			{
				supp_cd = rset.getString(1)==null?"":rset.getString(1);
				vendor_cd = rset.getString(2)==null?"":rset.getString(2);
				supp_nm=getEntity_nm(supp_cd,"S");
				vendor_name=getEntity_nm(vendor_cd,"V");
				crdr_invoice_dt=rset.getString(3)==null?"":rset.getString(3);
				crdr_invoice_due_dt=rset.getString(4)==null?"":rset.getString(4);
				double grossAmt=rset.getDouble(5);
				double taxAmt=rset.getDouble(6);
				double netPayable=rset.getDouble(7);
				invoice_raised_in=rset.getString(8)==null?"":rset.getString(8);
				invoice_raised_in_nm=utilBean.getRateUnitNm(conn, invoice_raised_in);
				cr_dr_type=rset.getString(9)==null?"":rset.getString(9);
				criteri_formula=rset.getString(10)==null?"":rset.getString(10);
				period_start_dt=rset.getString(11)==null?"":rset.getString(11);
				period_end_dt=rset.getString(12)==null?"":rset.getString(12);
				crdr_remark=rset.getString(13)==null?"":rset.getString(13);
				financial_year=rset.getString(14)==null?"":rset.getString(14);
				inv_no=rset.getString(17)==null?"":rset.getString(17);
				inv_id_seq=rset.getString(18)==null?"":rset.getString(18);
				double cr_dr_grossamt=rset.getDouble(20);
				double exchngrate = rset.getDouble(19);
				double exchngrate1 = rset.getDouble(19);
				
				if(callFlag.equalsIgnoreCase("COSTR_CRDR_APROVAL"))
				{
					if(Double.doubleToRawLongBits(grossAmt)<Double.doubleToRawLongBits(0))
					{
						grossAmt=(-1)*grossAmt;
					}
					if(Double.doubleToRawLongBits(taxAmt)<Double.doubleToRawLongBits(0))
					{
						taxAmt=(-1)*taxAmt;
					}
					if(Double.doubleToRawLongBits(netPayable)<Double.doubleToRawLongBits(0))
					{
						netPayable=(-1)*netPayable;
					}
					if(Double.doubleToRawLongBits(cr_dr_grossamt)<Double.doubleToRawLongBits(0))
					{
						cr_dr_grossamt=(-1)*cr_dr_grossamt;
					}
					if(Double.doubleToRawLongBits(exchngrate1)<Double.doubleToRawLongBits(0))
					{
						exchngrate1=(-1)*exchngrate1;
					}
				}
				
				BigDecimal grossAmt1 = BigDecimal.valueOf(grossAmt);
				BigDecimal sub_grossAmt1 = grossAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal taxAmt1 = BigDecimal.valueOf(taxAmt);
				BigDecimal sub_taxAmt1 = taxAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal netPayable1 = BigDecimal.valueOf(netPayable);
				BigDecimal sub_netPayable1 = netPayable1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal crdrgrossamt = BigDecimal.valueOf(cr_dr_grossamt);
				BigDecimal sub_cr_dr_grossamt = crdrgrossamt.divide(factor, 0, RoundingMode.HALF_UP);
				
				gross_amt=nf.format(sub_cr_dr_grossamt);				
				tax_amt=nf.format(sub_taxAmt1);				
				net_payable=nf.format(sub_netPayable1);
				gross_amt1=nf.format(sub_grossAmt1);
				exchange_rate=nf2.format(exchngrate);
				exchng_rate = nf2.format(exchngrate1);
				
				if(opration.equals("CHECK"))
				{
					activity_value = rset.getString(16) == null ? "" : rset.getString(16);
				}
				else if(opration.equals("APPROVE"))
				{
					activity_value = rset.getString(15) == null ? "" : rset.getString(15);
				}
				
				queryString1 = "SELECT TAX_STRUCT_CD "
						+ "FROM FMS_OTH_INVOICE_DTL "
						+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
						+ "AND INVOICE_SEQ=? "
						+ "AND INV_FLAG IN ('CR','DR') ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, inv_type);
				stmt1.setString(3, fin_yr);
				stmt1.setString(4, inv_seq);
				rset1 = stmt1.executeQuery();
				while(rset1.next()) 
				{
					tax_struct_cd=rset1.getString(1)==null?"":rset1.getString(1);
					tax_struct_info=utilBean.getTaxDescr(conn,main_tax_struct_cd);
				}
				rset1.close();
				stmt1.close();
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCostRCrDrRefDetail()
	{
		String function_nm="getCostRCrDrRefDetail()";
		try
		{
			BigDecimal factor = new BigDecimal(1);
			queryString = "SELECT GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,NET_PAYABLE_AMT,SALE_PRICE,EXCHG_RATE_VALUE,SALE_AMT "
					+ "FROM FMS_OTH_INV_CRDR_REF "
					+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
					+ "AND SUPPLIER_CD=? AND VENDOR_CD=? AND INVOICE_SEQ=? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_type);
			stmt.setString(3, financial_year);
			stmt.setString(4, supp_cd);
			stmt.setString(5, vendor_cd);
			stmt.setString(6, inv_seq);
			rset = stmt.executeQuery();
			while (rset.next()) 
			{
				double grossAmt = rset.getDouble(1);
				double taxAmt=rset.getDouble(2);
				new_tax_struct_cd=rset.getString(3)==null?"":rset.getString(3);
				new_tax_struct_info=utilBean.getTaxDescr(conn,new_tax_struct_cd);
				double netPayable=rset.getDouble(4);
				double tmp_rate=rset.getDouble(5);
				double exchgrt=rset.getDouble(6);
				double grossamt11=rset.getDouble(7);
				
				BigDecimal grossAmt1 = BigDecimal.valueOf(grossamt11);
				BigDecimal sub_grossAmt1 = grossAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal taxAmt1 = BigDecimal.valueOf(taxAmt);
				BigDecimal sub_taxAmt1 = taxAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal netPayable1 = BigDecimal.valueOf(netPayable);
				BigDecimal sub_netPayable1 = netPayable1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal new_grossAmt11 = BigDecimal.valueOf(grossAmt);
				BigDecimal sub_grossAmt11 = new_grossAmt11.divide(factor, 0, RoundingMode.HALF_UP);
				
				new_rate=""+tmp_rate;	
				new_gross_amt=nf.format(sub_grossAmt1);				
				new_tax_amt=nf.format(sub_taxAmt1);				
				new_net_payable=nf.format(sub_netPayable1);
				new_gross_amt1=nf.format(sub_grossAmt11);				
				new_exchang_rate=nf2.format(exchgrt);
				
				reason="Change in ";
				if(!criteri_formula.equals(""))
				{
					String[] split_criteri_formula = criteri_formula.split("#");
					for(int i=0; i<split_criteri_formula.length; i++)
					{
						if(i!=0)
						{
							if((i+1) == split_criteri_formula.length)
							{
								reason+=" and ";
							}
							else
							{
								reason+=", ";
							}
						}
						
						if(split_criteri_formula[i].toString().equals("GAMT"))
						{
							reason+="Gross Amount";
						}
						else if(split_criteri_formula[i].toString().equals("EXCHG"))
						{
							reason+="Exchange Rate";
						}
						else if(split_criteri_formula[i].toString().equals("TAXP"))
						{
							reason+="Tax %";
						}
					}
				}
				
				
				int srno=0;
				String type = "",currency_nm = "INR",currency = "USD";
				String dt = utilDate.getDateFormatDD_MOM_YY(main_invoice_dt);
				type = cr_dr_type.equals("CR") ? "Credit Note":"Debit Note";
				
				String desc_item= type+" against Invoice No : "+sel_inv_no+" dated "+dt;
				
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add(desc_item);
				VPDF_COL3.add("");
				VPDF_COL4.add("");
				
//				srno+=1;
//				VPDF_COL1.add(srno);
//				VPDF_COL2.add("<b>Gross Amount</b>");
//				VPDF_COL3.add("<b>"+currency+"</b>");
//				VPDF_COL4.add("<b>"+es.formatAmount(gross_amt)+"</b>");
				
				 String curr1 = "INR",curr2="INR",curr3="INR";
		            if(price_cd.equals("2"))
		            {
		            	curr1 = "USD";
		            	curr2 = "INR/USD";
		            	curr3 = "INR";
		            }
		            else if(price_cd.equals("3")) 
		            {	
		            	curr1 = "GBP";
		            	curr2 = "INR/GBP";
		            	curr3 = "INR";
		            }
		            else if(price_cd.equals("4")) 
		            {	
		            	curr1 = "EURO";
		            	curr2 = "INR/EURO";
		            	curr3 = "INR";
		            }
		            else if(price_cd.equals("5")) 
		            {	
		            	curr1 = "YEN";
		            	curr2 = "INR/YEN";
		            	curr3 = "INR";
		            }
		            
				if(!price_cd.equals("1"))
				{
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("<b>Gross Amount</b>");
					VPDF_COL3.add("<b>"+curr1+"</b>");
					if(criteri_formula.contains("GAMT"))
					{
						VPDF_COL4.add("<b>"+es.formatAmount(gross_amt)+"</b>");
					}
					else if(criteri_formula.contains("EXCHG"))
		   	     	{
						VPDF_COL4.add("<b>"+es.formatAmount(new_gross_amt)+"</b>");
		   	     	}
					else
					{
						VPDF_COL4.add("");
					}
					
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("Exchange Rate");
					VPDF_COL3.add(curr2);
					VPDF_COL4.add(main_exchang_rate);
					
					if(criteri_formula.contains("EXCHG"))
					{
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("Applicable Exchange Rate");
						VPDF_COL3.add(curr2);
						VPDF_COL4.add(new_exchang_rate);
						
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add("Difference in Exchange Rate");
						VPDF_COL3.add(curr2);
						VPDF_COL4.add(exchng_rate);
					}
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("<b>Gross Amount</b>");
					VPDF_COL3.add("<b>"+curr3+"</b>");
					VPDF_COL4.add("<b>"+es.formatAmount(gross_amt1)+"</b>");
				}
				else
				{
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("<b>Gross Amount</b>");
					VPDF_COL3.add("<b>"+curr1+"</b>");
					if(criteri_formula.contains("GAMT"))
					{
						VPDF_COL4.add("<b>"+es.formatAmount(gross_amt)+"</b>");
					}
					else
					{
						VPDF_COL4.add("");
					}
						
				}
				
				srno+=1;
				double temp_srno=srno;
				Vector VTAX_CODE = new Vector();
				Vector VTAX_DESCR = new Vector();
				Vector VTAX_AMT = new Vector();
				if (new_tax_struct_info.contains(",")) 
				{
					BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_amt));
					factor = new BigDecimal(2);
					for(int i = 0;i<new_tax_struct_info.split(",").length;i++) 
					{
						BigDecimal sub_tax_amt = tax_amt1.divide(factor, 0, RoundingMode.HALF_UP);
						temp_srno=temp_srno+0.1;
						VPDF_COL1.add(nf0.format(temp_srno));
						VPDF_COL2.add(new_tax_struct_info.split(",")[i]);
						VPDF_COL3.add(currency_nm);
						VPDF_COL4.add(es.formatAmount(""+Double.parseDouble(""+sub_tax_amt)));
						
						VTAX_CODE.add(tax_struct_cd);
						VTAX_DESCR.add(new_tax_struct_info.split(",")[i]);
						VTAX_AMT.add(nf.format(sub_tax_amt));
					}
					Vector VTEMP_TAX_DTL = new Vector();
					
					VTEMP_TAX_DTL.add(VTAX_CODE);
					VTEMP_TAX_DTL.add(VTAX_DESCR);
					VTEMP_TAX_DTL.add(VTAX_AMT);
					
					VMULTI_TAX_STRUCT.add(VTEMP_TAX_DTL);
				} 
				
				VPDF_COL1.add(srno);
				VPDF_COL2.add("<b>Tax ("+new_tax_struct_info+")</b>");
				VPDF_COL3.add("<b>"+currency_nm+"</b>");
				VPDF_COL4.add("<b>"+es.formatAmount(tax_amt)+"</b>");

				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("<b>Total Amount in INR</b>");
				VPDF_COL3.add("<b>"+currency_nm+"</b>");
				VPDF_COL4.add("<b>"+es.formatAmount(net_payable)+"</b>");
			
				if(!net_payable.equalsIgnoreCase(""))		
				{
					amt_in_word=es.convert(Double.parseDouble(net_payable));
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
	
	public void getSFAInvoiceDtl()
	{
		String function_nm="getSFAInvoiceDtl()";

		try
		{
			BigDecimal factor = new BigDecimal(1);
			queryString = "SELECT SUPPLIER_CD,VENDOR_CD,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'),GROSS_AMT,TAX_AMT_INR,NET_PAYABLE,"
					+ "INVOICE_RAISED_IN,REMARK,REMARK1,REMARK2,REMARK3,INVOICE_CATEGORY "
					+ "FROM FMS_OTH_INVOICE_MST "
					+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND INVOICE_NO=? "
					+ "AND INVOICE_NO IS NOT NULL AND INV_FLAG='F' ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_type);
			stmt.setString(3, sel_inv_no);
			rset = stmt.executeQuery();
			while (rset.next()) 
			{
				supp_cd = rset.getString(1)==null?"":rset.getString(1);
				vendor_cd = rset.getString(2)==null?"":rset.getString(2);
				supp_nm=getEntity_nm(supp_cd,"S");
				vendor_name=getEntity_nm(vendor_cd,"V");
				main_invoice_dt=rset.getString(3)==null?"":rset.getString(3);
				main_invoice_due_dt=rset.getString(4)==null?"":rset.getString(4);
				double grossAmt=rset.getDouble(5);
				double taxAmt=rset.getDouble(6);
				double netPayable=rset.getDouble(7);
				invoice_raised_in=rset.getString(8)==null?"":rset.getString(8);
				invoice_raised_in_nm=utilBean.getRateUnitNm(conn, invoice_raised_in);
				remark1=rset.getString(9)==null?"":rset.getString(9);
				remark2=rset.getString(10)==null?"":rset.getString(10);
				remark3=rset.getString(11)==null?"":rset.getString(11);
				remark4=rset.getString(12)==null?"":rset.getString(12);
				invoice_category=rset.getString(13)==null?"":rset.getString(13);
				
				BigDecimal grossAmt1 = BigDecimal.valueOf(grossAmt);
				BigDecimal sub_grossAmt1 = grossAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal taxAmt1 = BigDecimal.valueOf(taxAmt);
				BigDecimal sub_taxAmt1 = taxAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal netPayable1 = BigDecimal.valueOf(netPayable);
				BigDecimal sub_netPayable1 = netPayable1.divide(factor, 0, RoundingMode.HALF_UP);
				
				main_gross_amt=nf.format(sub_grossAmt1);				
				main_tax_amt=nf.format(sub_taxAmt1);				
				main_net_payable=nf.format(sub_netPayable1);	
				
				queryString1 = "SELECT SALE_PRICE_UNIT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY') "
						+ "FROM FMS_OTH_INVOICE_DTL "
						+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND INVOICE_NO=? AND VENDOR_CD = ? "
						+ "AND SUPPLIER_CD = ? AND INVOICE_NO IS NOT NULL AND INV_FLAG='F' ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, inv_type);
				stmt1.setString(3, sel_inv_no);
				stmt1.setString(4, vendor_cd);
				stmt1.setString(5, supp_cd);
				rset1 = stmt1.executeQuery();
				if(rset1.next()) 
				{
					price_cd=rset1.getString(1)==null?"":rset1.getString(1);
					price_cd_nm=utilBean.getRateUnitNm(conn, price_cd);
					main_tax_struct_cd=rset1.getString(2)==null?"":rset1.getString(2);
					main_tax_struct_info=utilBean.getTaxDescr(conn,main_tax_struct_cd);
					main_tax_struct_dt = rset1.getString(3)==null?"":rset1.getString(3);
				}
				rset1.close();
				stmt1.close();
				
				queryString1 = "SELECT ITEM_DESCRIPTION,HSN_CODE,UAM_NO,QUANTITY,SALE_PRICE,ITEM_AMT,TAX_STRUCT_CD,"
						+ "TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),ITEM_TAX_AMT "
						+ "FROM FMS_OTH_INVOICE_DTL "
						+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND INVOICE_NO=? AND VENDOR_CD = ? "
						+ "AND SUPPLIER_CD = ? AND INVOICE_NO IS NOT NULL AND INV_FLAG='F' ORDER BY SEQ_NO";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, inv_type);
				stmt1.setString(3, sel_inv_no);
				stmt1.setString(4, vendor_cd);
				stmt1.setString(5, supp_cd);
				rset1 = stmt1.executeQuery();
				while(rset1.next()) 
				{
					VITEM_DES.add(rset1.getString(1)==null?"":rset1.getString(1));
					VSAC_VAL.add(rset1.getString(2)==null?"":rset1.getString(2));
					VUOM_NO.add(rset1.getString(3)==null?"":rset1.getString(3));
					VMAIN_QTY.add(rset1.getString(4)==null?"":rset1.getString(4));
					VMAIN_PRICE.add(nf.format(Double.parseDouble("" + rset1.getString(5)==null?"0":rset1.getString(5))));
					VMAIN_ITEM_AMT.add(nf.format(Double.parseDouble("" + rset1.getString(6)==null?"":rset1.getString(6))));
					
					if(invoice_category.equals("P"))
					{
						VMAIN_TAX_STRUCT_DESC.add(utilBean.getTaxDescr(conn,rset1.getString(7)==null?"":rset1.getString(7)));
						VMAIN_TAX_STRUCT_APP_DT.add(rset1.getString(8)==null?"":rset1.getString(8));
						VMAIN_TAX_STRUCTURE_CD.add(rset1.getString(7)==null?"":rset1.getString(7));
						VMAIN_TAX_AMT.add(nf.format(Double.parseDouble("" + rset1.getString(9)==null?"":rset1.getString(9))));
					}
					else 
					{
						VMAIN_TAX_STRUCT_DESC.add("");
						VMAIN_TAX_STRUCT_APP_DT.add("");
						VMAIN_TAX_STRUCTURE_CD.add("");
						VMAIN_TAX_AMT.add("");
					}
				}
				rset1.close();
				stmt1.close();
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSFARCrDrInvoiceDtl()
	{
		String function_nm="getSFARCrDrInvoiceDtl()";
		try
		{
			BigDecimal factor = new BigDecimal(1);
			queryString = "SELECT SUPPLIER_CD,VENDOR_CD,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'),GROSS_AMT,TAX_AMT_INR,NET_PAYABLE,"
					+ "INVOICE_RAISED_IN,INV_FLAG,CRITERIA,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),REMARK,FINANCIAL_YEAR,"
					+ "APPROVED_FLAG,CHECKED_FLAG,INVOICE_NO,INVOICE_ID_SEQ "
					+ "FROM FMS_OTH_INVOICE_MST "
					+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
					+ "AND INVOICE_SEQ=? "
					+ "AND INV_FLAG IN ('CR','DR') ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_type);
			stmt.setString(3, fin_yr);
			stmt.setString(4, inv_seq);
			rset = stmt.executeQuery();
			while (rset.next()) 
			{
				supp_cd = rset.getString(1)==null?"":rset.getString(1);
				vendor_cd = rset.getString(2)==null?"":rset.getString(2);
				crdr_invoice_dt=rset.getString(3)==null?"":rset.getString(3);
				crdr_invoice_due_dt=rset.getString(4)==null?"":rset.getString(4);
				double grossAmt=rset.getDouble(5);
				double taxAmt=rset.getDouble(6);
				double netPayable=rset.getDouble(7);
				invoice_raised_in=rset.getString(8)==null?"":rset.getString(8);
				invoice_raised_in_nm=utilBean.getRateUnitNm(conn, invoice_raised_in);
				cr_dr_type=rset.getString(9)==null?"":rset.getString(9);
				criteri_formula=rset.getString(10)==null?"":rset.getString(10);
				period_start_dt=rset.getString(11)==null?"":rset.getString(11);
				period_end_dt=rset.getString(12)==null?"":rset.getString(12);
				crdr_remark=rset.getString(13)==null?"":rset.getString(13);
				financial_year=rset.getString(14)==null?"":rset.getString(14);
				inv_no=rset.getString(17)==null?"":rset.getString(17);
				inv_id_seq=rset.getString(18)==null?"":rset.getString(18);
				
				if(callFlag.equalsIgnoreCase("SFA_CRDR_APROVAL"))
				{
					if(Double.doubleToRawLongBits(grossAmt)<Double.doubleToRawLongBits(0))
					{
						grossAmt=(-1)*grossAmt;
					}
					if(Double.doubleToRawLongBits(taxAmt)<Double.doubleToRawLongBits(0))
					{
						taxAmt=(-1)*taxAmt;
					}
					if(Double.doubleToRawLongBits(netPayable)<Double.doubleToRawLongBits(0))
					{
						netPayable=(-1)*netPayable;
					}
				}
				
				BigDecimal grossAmt1 = BigDecimal.valueOf(grossAmt);
				BigDecimal sub_grossAmt1 = grossAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal taxAmt1 = BigDecimal.valueOf(taxAmt);
				BigDecimal sub_taxAmt1 = taxAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal netPayable1 = BigDecimal.valueOf(netPayable);
				BigDecimal sub_netPayable1 = netPayable1.divide(factor, 0, RoundingMode.HALF_UP);
				
				gross_amt=nf.format(sub_grossAmt1);				
				tax_amt=nf.format(sub_taxAmt1);				
				net_payable=nf.format(sub_netPayable1);				
				
				if(opration.equals("CHECK"))
				{
					activity_value = rset.getString(16) == null ? "" : rset.getString(16);
				}
				else if(opration.equals("APPROVE"))
				{
					activity_value = rset.getString(15) == null ? "" : rset.getString(15);
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
	
	public void getSFARCrDrItemDtl()
	{
		String function_nm="getSFARCrDrItemDtl()";
		try
		{
			for(int i = 0;i<VMAIN_QTY.size();i++)
			{
				queryString1 = "SELECT QUANTITY,SALE_PRICE,ITEM_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),"
						+ "ITEM_TAX_AMT "
						+ "FROM FMS_OTH_INVOICE_DTL "
						+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
						+ "AND INVOICE_SEQ=? AND ITEM_DESCRIPTION = ? "
						+ "AND INV_FLAG IN ('CR','DR') ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, inv_type);
				stmt1.setString(3, fin_yr);
				stmt1.setString(4, inv_seq);
				stmt1.setString(5, ""+VITEM_DES.elementAt(i));
				rset1 = stmt1.executeQuery();
				if(rset1.next()) 
				{
					IS_CRDR_SUBMITTED.add("Y");
					VQTY.add(rset1.getString(1)==null?"":rset1.getString(1));
					VPRICE.add(nf.format(Double.parseDouble("" + rset1.getString(2)==null?"":rset1.getString(2))));
					VITEM_AMT.add(nf.format(Double.parseDouble("" + rset1.getString(3)==null?"":rset1.getString(3))));
					
					if(invoice_category.equals("P"))
					{
						VTAX_STRUCTURE_CD.add(rset1.getString(4)==null?"":rset1.getString(4));
						VTAX_STRUCTURE_DESC.add(utilBean.getTaxDescr(conn,rset1.getString(4)==null?"":rset1.getString(4)));
						VTAX_STRUCT_APP_DT.add(rset1.getString(5)==null?"":rset1.getString(5));
						VTAX_AMT.add(nf.format(Double.parseDouble("" + rset1.getString(6)==null?"":rset1.getString(6))));
					}
					else 
					{
						VTAX_STRUCTURE_DESC.add("");
						VTAX_STRUCT_APP_DT.add("");
						VTAX_STRUCTURE_CD.add("");
						VTAX_AMT.add("");
					}
				}
				else 
				{
					IS_CRDR_SUBMITTED.add("");
					VQTY.add("");
					VPRICE.add("");
					VITEM_AMT.add("");
					VTAX_STRUCTURE_CD.add("");
					VTAX_STRUCTURE_DESC.add("");
					VTAX_STRUCT_APP_DT.add("");
					VTAX_AMT.add("");
				}
				rset1.close();
				stmt1.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSFACrDrRefItemDetail()
	{
		String function_nm="getSFACrDrRefItemDetail()";
		try
		{
			for(int i = 0;i<VMAIN_QTY.size();i++)
			{
				queryString1 = "SELECT QUANTITY,SALE_PRICE,ITEM_AMT,"
						+ "ITEM_TAX_AMT "
						+ "FROM FMS_OTH_INV_CRDR_REF "
						+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
						+ "AND INVOICE_SEQ=? AND ITEM_DESCRIPTION = ? ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, inv_type);
				stmt1.setString(3, fin_yr);
				stmt1.setString(4, inv_seq);
				stmt1.setString(5, ""+VITEM_DES.elementAt(i));
				rset1 = stmt1.executeQuery();
				if(rset1.next()) 
				{
					VNEW_QTY.add(rset1.getString(1)==null?"":rset1.getString(1));
					VNEW_PRICE.add(nf.format(Double.parseDouble("" + rset1.getString(2)==null?"":rset1.getString(2))));
					VNEW_ITEM_AMT.add(nf.format(Double.parseDouble("" + rset1.getString(3)==null?"":rset1.getString(3))));
					
					if(invoice_category.equals("P"))
					{
						VNEW_TAX_STRUCTURE_CD.add(VTAX_STRUCTURE_CD.elementAt(i));
						VNEW_TAX_STRUCT_DESC.add(VTAX_STRUCTURE_DESC.elementAt(i));
						VNEW_TAX_STRUCT_APP_DT.add(VTAX_STRUCT_APP_DT.elementAt(i));
						VNEW_TAX_AMT.add(nf.format(Double.parseDouble("" + rset1.getString(4)==null?"":rset1.getString(4))));
					}
					else 
					{
						VNEW_TAX_STRUCT_DESC.add("");
						VNEW_TAX_STRUCT_APP_DT.add("");
						VNEW_TAX_STRUCTURE_CD.add("");
						VNEW_TAX_AMT.add("");
					}
				}
				else 
				{
					VNEW_QTY.add("");
					VNEW_PRICE.add("");
					VNEW_ITEM_AMT.add("");
					VNEW_TAX_STRUCT_DESC.add("");
					VNEW_TAX_STRUCT_APP_DT.add("");
					VNEW_TAX_STRUCTURE_CD.add("");
					VNEW_TAX_AMT.add("");
				}
				rset1.close();
				stmt1.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getSFACrDrRefDetail()
	{
		String function_nm="getSFACrDrRefDetail()";
		try
		{
			BigDecimal factor = new BigDecimal(1);
			queryString = "SELECT GROSS_AMT,TAX_AMT,NET_PAYABLE_AMT,TAX_STRUCT_CD "
					+ "FROM FMS_OTH_INV_CRDR_REF "
					+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
					+ "AND SUPPLIER_CD=? AND VENDOR_CD=? AND INVOICE_SEQ=? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_type);
			stmt.setString(3, financial_year);
			stmt.setString(4, supp_cd);
			stmt.setString(5, vendor_cd);
			stmt.setString(6, inv_seq);
			rset = stmt.executeQuery();
			while (rset.next()) 
			{
				double grossAmt = rset.getDouble(1);
				double taxAmt=rset.getDouble(2);
				double netPayable=rset.getDouble(3);
				new_tax_struct_cd=rset.getString(4)==null?"":rset.getString(4);
				new_tax_struct_info=utilBean.getTaxDescr(conn,new_tax_struct_cd);
				new_tax_struct_dt=tax_struct_dt;
				BigDecimal grossAmt1 = BigDecimal.valueOf(grossAmt);
				BigDecimal sub_grossAmt1 = grossAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal taxAmt1 = BigDecimal.valueOf(taxAmt);
				BigDecimal sub_taxAmt1 = taxAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal netPayable1 = BigDecimal.valueOf(netPayable);
				BigDecimal sub_netPayable1 = netPayable1.divide(factor, 0, RoundingMode.HALF_UP);
				
				new_gross_amt=nf.format(sub_grossAmt1);				
				new_tax_amt=nf.format(sub_taxAmt1);				
				new_net_payable=nf.format(sub_netPayable1);	
				
				reason="Change in ";
				if(!criteri_formula.equals(""))
				{
					String[] split_criteri_formula = criteri_formula.split("#");
					for(int i=0; i<split_criteri_formula.length; i++)
					{
						if(i!=0)
						{
							if((i+1) == split_criteri_formula.length)
							{
								reason+=" and ";
							}
							else
							{
								reason+=", ";
							}
						}
						
						if(split_criteri_formula[i].toString().equals("RATE"))
						{
							reason+="Rate";
						}
						else if(split_criteri_formula[i].toString().equals("QTY"))
						{
							reason+="Quantity";
						}
						else if(split_criteri_formula[i].toString().equals("TAXP"))
						{
							reason+="Tax %";
						}
					}
				}
				
				if(!net_payable.equalsIgnoreCase(""))		
				{
					amt_in_word=es.convert(Double.parseDouble(net_payable));
				}
			}
			rset.close();
			stmt.close();
			
			int srno=0;
			String type = "",currency_nm = "INR";
			
			if(invoice_category.equals("P"))
			{
				for(int i = 0; i<VPRICE.size();i++)
				{
					if(!VPRICE.elementAt(i).equals(""))
					{
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add(VSAC_VAL.elementAt(i));
						VPDF_COL3.add(VITEM_DES.elementAt(i));
						VPDF_COL4.add(VUOM_NO.elementAt(i));
						if(criteri_formula.contains("QTY"))
						{
							VPDF_COL5.add(nf.format(Math.abs(Double.parseDouble(""+VQTY.elementAt(i)))));
						}
						else 
						{
							VPDF_COL5.add(VNEW_QTY.elementAt(i));
						}
						
						if(criteri_formula.contains("RATE"))
						{
							VPDF_COL6.add(es.formatAmount(""+Math.abs(Double.parseDouble(""+VPRICE.elementAt(i)))));
						}
						else 
						{
							VPDF_COL6.add(VNEW_PRICE.elementAt(i));
						}
						VPDF_COL7.add(VTAX_STRUCTURE_DESC.elementAt(i).toString().replace(","," ").split(" ")[1]);
						VPDF_COL9.add(currency_nm);
						if(criteri_formula.contains("TAXP"))
						{
							VPDF_COL10.add(es.formatAmount(""+VNEW_ITEM_AMT.elementAt(i)));
						}
						else 
						{
							VPDF_COL10.add(es.formatAmount(""+Math.abs(Double.parseDouble(""+VITEM_AMT.elementAt(i)))));
						}
					}
				}
				
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("");
				VPDF_COL3.add("<b>Gross Amount</b>");
				VPDF_COL4.add("");
				VPDF_COL5.add("");
				VPDF_COL6.add("");
				VPDF_COL7.add("");
				VPDF_COL9.add("<b>"+currency_nm+"</b>");
				VPDF_COL10.add("<b>"+es.formatAmount(gross_amt)+"</b>");
				
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("");
				VPDF_COL3.add("<b>Total Tax Amount</b>");
				VPDF_COL4.add("");
				VPDF_COL5.add("");
				VPDF_COL6.add("");
				VPDF_COL7.add("");
				VPDF_COL9.add("<b>"+currency_nm+"</b>");
				VPDF_COL10.add("<b>"+es.formatAmount(tax_amt)+"</b>");
				
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("");
				VPDF_COL3.add("<b>Total Amount in INR</b>");
				VPDF_COL4.add("");
				VPDF_COL5.add("");
				VPDF_COL6.add("");
				VPDF_COL7.add("");
				VPDF_COL9.add("<b>"+currency_nm+"</b>");
				VPDF_COL10.add("<b>"+es.formatAmount(net_payable)+"</b>");
			}
			else 
			{
				for(int i = 0; i<VPRICE.size();i++)
				{
					if(!VPRICE.elementAt(i).equals(""))
					{
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add(VSAC_VAL.elementAt(i));
						VPDF_COL3.add(VITEM_DES.elementAt(i));
						VPDF_COL4.add(VUOM_NO.elementAt(i));
						if(criteri_formula.contains("QTY"))
						{
							VPDF_COL5.add(nf.format(Math.abs(Double.parseDouble(""+VQTY.elementAt(i)))));
						}
						else 
						{
							VPDF_COL5.add(VNEW_QTY.elementAt(i));
						}
						
						if(criteri_formula.contains("RATE"))
						{
							VPDF_COL6.add(es.formatAmount(""+Math.abs(Double.parseDouble(""+VPRICE.elementAt(i)))));
						}
						else 
						{
							VPDF_COL6.add(VNEW_PRICE.elementAt(i));
						}
						VPDF_COL7.add(currency_nm);
						if(criteri_formula.contains("TAXP"))
						{
							VPDF_COL8.add(es.formatAmount(""+VNEW_ITEM_AMT.elementAt(i)));
						}
						else 
						{
							VPDF_COL8.add(es.formatAmount(""+Math.abs(Double.parseDouble(""+VITEM_AMT.elementAt(i)))));
						}
					}
				}
				
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("");
				VPDF_COL3.add("<b>Gross Amount</b>");
				VPDF_COL4.add("");
				VPDF_COL5.add("");
				VPDF_COL6.add("");
				VPDF_COL7.add("<b>"+currency_nm+"</b>");
				VPDF_COL8.add("<b>"+es.formatAmount(gross_amt)+"</b>");
				
				srno+=1;
				double temp_srno=srno;
				
				if (new_tax_struct_info.contains(",")) 
				{
					BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_amt));
					factor = new BigDecimal(2);
					for(int i = 0;i<new_tax_struct_info.split(",").length;i++) 
					{
						BigDecimal sub_tax_amt = tax_amt1.divide(factor, 0, RoundingMode.HALF_UP);
						temp_srno=temp_srno+0.1;
						VPDF_COL1.add(nf0.format(temp_srno));
						VPDF_COL2.add("");
						VPDF_COL3.add(new_tax_struct_info.split(",")[i]);
						VPDF_COL4.add("");
						VPDF_COL5.add("");
						VPDF_COL6.add("");
						VPDF_COL7.add("INR");
						VPDF_COL8.add(es.formatAmount(""+Double.parseDouble(""+sub_tax_amt)));
						
						VTAX_AMT.add(nf.format(sub_tax_amt));
					}
				} 
				
				VPDF_COL1.add(srno);
				VPDF_COL2.add("");
				VPDF_COL3.add("<b>Tax ("+new_tax_struct_info+")</b>");
				VPDF_COL4.add("");
				VPDF_COL5.add("");
				VPDF_COL6.add("");
				VPDF_COL7.add("<b>"+currency_nm+"</b>");
				VPDF_COL8.add("<b>"+es.formatAmount(tax_amt)+"</b>");
				
				srno+=1;
				VPDF_COL1.add(srno);
				VPDF_COL2.add("");
				VPDF_COL3.add("<b>Total Amount in INR</b>");
				VPDF_COL4.add("");
				VPDF_COL5.add("");
				VPDF_COL6.add("");
				VPDF_COL7.add("<b>"+currency_nm+"</b>");
				VPDF_COL8.add("<b>"+es.formatAmount(net_payable)+"</b>");
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}


    public void getVendorShipBillAddr()
	{
		String function_nm="getVendorShipBillAddr()";
		try
		{
				String query = "SELECT ADDR,CITY,PIN,COUNTRY,STATE "
						+ "FROM FMS_OTH_ENTITY_ADDR_MST "
						+ "WHERE ENTITY_CD = ? AND ENTITY_TYPE = ? AND ADDRESS_TYPE='C'";
				stmt1 = conn.prepareStatement(query);
				stmt1.setString(1,vendor_cd);
				stmt1.setString(2,"V");
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					vend_ship_abbr = rset1.getString(1)==null?"":rset1.getString(1);
					vend_ship_city = rset1.getString(2)==null?"":rset1.getString(2);
					vend_ship_pin_no= rset1.getString(3)==null?"":rset1.getString(3);
					vend_ship_country=rset1.getString(4)==null?"":rset1.getString(4);
					vend_ship_state=rset1.getString(5)==null?"":rset1.getString(5);
				}
				stmt1.close();
				rset1.close();
				
				String query1 = "SELECT ADDR,CITY,PIN,COUNTRY,STATE "
						+ "FROM FMS_OTH_ENTITY_ADDR_MST "
						+ "WHERE ENTITY_CD = ? AND ENTITY_TYPE = ? AND ADDRESS_TYPE='B'";
				stmt1 = conn.prepareStatement(query1);
				stmt1.setString(1,vendor_cd);
				stmt1.setString(2,"V");
				rset1 = stmt1.executeQuery();
				if(rset1.next())
				{
					vend_bill_abbr = rset1.getString(1)==null?"":rset1.getString(1);
					vend_bill_city = rset1.getString(2)==null?"":rset1.getString(2);
					vend_bill_pin_no= rset1.getString(3)==null?"":rset1.getString(3);
					vend_bill_country=rset1.getString(4)==null?"":rset1.getString(4);
					vend_bill_state=rset1.getString(5)==null?"":rset1.getString(5);
				}
				stmt1.close();
				rset1.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getInvoiceDtlsRXP()
	{	
		String function_nm="getInvoiceDtlsRXP()";

		try
		{
			double taxValue = 0;
			String tax_value="";
			queryString = "SELECT A.COMPANY_CD,A.VENDOR_CD,A.SUPPLIER_CD,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'), "
					+ "A.GROSS_AMT,A.NET_PAYABLE,B.SALE_PRICE_UNIT,B.ITEM_DESCRIPTION, "
					+ "A.REMARK,A.REMARK1,A.APPROVED_FLAG,A.FINANCIAL_YEAR,A.INVOICE_SEQ,A.SALE_AMT, " //14
					+ "A.CHECKED_FLAG,A.INVOICE_NO,A.INVOICE_ID_SEQ,A.INVOICE_CATEGORY,A.INVOICE_RAISED_IN, "
					+ "B.REFERENCE_NO,B.PER_CARRIAGE,B.FINAL_DESTINATION,B.COUNTRY_OF_ORIGIN, "
					+ "B.PORT_OF_DISCHARGE,B.PORT_OF_LOADING,B.PAY_TERMS "
					+ "FROM FMS_OTH_INVOICE_MST A, FMS_OTH_INVOICE_DTL B "
					+ "WHERE A.COMPANY_CD = ? AND A.INVOICE_SEQ = ? AND A.INVOICE_TYPE = ? AND A.FINANCIAL_YEAR=? AND A.SUPPLIER_CD=? AND A.INV_FLAG = ? "
					+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.INVOICE_TYPE = B.INVOICE_TYPE "
					+ "AND A.VENDOR_CD = B.VENDOR_CD AND A.SUPPLIER_CD = B.SUPPLIER_CD AND A.INVOICE_SEQ = B.INVOICE_SEQ AND A.INV_FLAG = B.INV_FLAG";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_seq);
			stmt.setString(3, inv_type);
			stmt.setString(4, fin_yr);
			stmt.setString(5, supp_cd);
			stmt.setString(6, "F");
			rset = stmt.executeQuery();
			if(rset.next()) 
			{
				submission_chk = true;
				
				vendor_cd= rset.getString(2) == null ? "" : rset.getString(2);
				supp_cd= rset.getString(3) == null ? "" : rset.getString(3);				
				inv_dt = rset.getString(4) == null ? "" : rset.getString(4);
				gross_amt = nf.format(Double.parseDouble(rset.getString(5) == null ? "" : rset.getString(5)));
				net_amt = nf.format(Double.parseDouble(rset.getString(6) == null ? "" : rset.getString(6)));
				currency = rset.getString(7) == null ? "" : rset.getString(7);
				desc_item = rset.getString(8) == null ? "" : rset.getString(8);
				remark1 = rset.getString(9) == null ? "" : rset.getString(9);
				remark2 = rset.getString(10) == null ? "" : rset.getString(10);
				if(opration.equals("CHECK"))
				{
					activity_value = rset.getString(15) == null ? "" : rset.getString(15);
				}
				else if(opration.equals("APPROVE"))
				{
					activity_value = rset.getString(11) == null ? "" : rset.getString(11);
				}
				fin_yr = rset.getString(12) == null ? "" : rset.getString(12);
				inv_seq = rset.getString(13) == null ? "" : rset.getString(13);
				sale_amt = nf.format(Double.parseDouble(rset.getString(14) == null ? "" : rset.getString(14)));
				inv_no = rset.getString(16) == null ? "" : rset.getString(16);
				inv_id_seq = rset.getString(17) == null ? "" : rset.getString(17);
				invoice_category = rset.getString(18) == null ? "" : rset.getString(18);
				invoice_raised_in = rset.getString(19) == null ? "" : rset.getString(19);
				reference_no = rset.getString(20) == null ? "" : rset.getString(20);
				per_carriage = rset.getString(21) == null ? "" : rset.getString(21);
				final_destination = rset.getString(22) == null ? "" : rset.getString(22);	
				country_of_origin = rset.getString(23) == null ? "" : rset.getString(23);	
				port_of_discharge = rset.getString(24) == null ? "" : rset.getString(24);	
				port_of_loading = rset.getString(25) == null ? "" : rset.getString(25);	
				pay_terms = rset.getString(26) == null ? "" : rset.getString(26);

				int index = 1;
				queryString2 = "SELECT ITEM_DESCRIPTION,SALE_PRICE,HSN_CODE,QUANTITY,UAM_NO,ITEM_AMT, "
						+ "ITEM_DIMENSIONS,ITEM_NET_WT,ITEM_GROSS_WT,ITEM_PACK_DTLS "
						+ "FROM FMS_OTH_INVOICE_DTL  "
						+ "WHERE COMPANY_CD  = ? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR = ? AND SUPPLIER_CD = ? "
						+ "AND INV_FLAG = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY')";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, comp_cd);
				stmt2.setString(2, inv_seq);
				stmt2.setString(3, inv_type);
				stmt2.setString(4, fin_yr);
				stmt2.setString(5, supp_cd);
				stmt2.setString(6, "F");
				stmt2.setString(7, inv_dt);
				rset2=stmt2.executeQuery();
				while(rset2.next())
				{
					VGAMT_DES.add(rset2.getString(1)==null?"":rset2.getString(1));
					VPRICE.add(nf.format(Double.parseDouble("" + rset2.getString(2)==null?"":rset2.getString(2))));
					VSAC_CODE.add(rset2.getString(3)==null?"":rset2.getString(3));
					VQTY.add(rset2.getString(4)==null?"":rset2.getString(4));
					VUOM_NO.add(rset2.getString(5)==null?"":rset2.getString(5));
					VGITEM_AMT.add(nf.format(Double.parseDouble("" + rset2.getString(6)==null?"":rset2.getString(6))));
					VDIMENSION.add(rset2.getString(7)==null?"":rset2.getString(7));
					VNET_WT.add(rset2.getString(8)==null?"":rset2.getString(8));
					VGROSS_WT.add(rset2.getString(9)==null?"":rset2.getString(9));
					VPACK_DTLS.add(rset2.getString(10)==null?"":rset2.getString(10));
					VLINE_NO.add(index++);
				}
				rset2.close();
				stmt2.close();
				
				int srno=0;
				String currency_nm ="";
				if(currency.equals("1"))
				{
					currency_nm = "INR";
				}
				else if(currency.equals("2"))
				{
					currency_nm = "USD";
				}
				else if(currency.equals("3"))
				{
					currency_nm = "GBP";
				}
				else if(currency.equals("4"))
				{
					currency_nm = "EURO";
				}
				else if(currency.equals("5"))
				{
					currency_nm = "YEN";
				}
				
					for(int i = 0; i<VGITEM_AMT.size();i++)
					{
						String packDetails = "";
						packDetails += VPACK_DTLS.elementAt(i) + "\n";
						packDetails += "<b>Dimensions:</b> " + VDIMENSION.elementAt(i) + "\n";
						packDetails += "<b>Net Wt.: </b>" + VNET_WT.elementAt(i) + "\n";
						packDetails += "<b>Gross Wt.: </b>" + VGROSS_WT.elementAt(i);
						VLAMT_DES.add(packDetails);
						srno+=1;
						VPDF_COL1.add(srno);
						VPDF_COL2.add(VSAC_CODE.elementAt(i));
						VPDF_COL3.add("<b>"+VGAMT_DES.elementAt(i)+"<b>");
						VPDF_COL4.add(VUOM_NO.elementAt(i));
						VPDF_COL5.add(packDetails);
						VPDF_COL6.add(VQTY.elementAt(i));
						VPDF_COL7.add(es.formatAmount(""+VPRICE.elementAt(i)));
						VPDF_COL8.add(currency_nm);
						VPDF_COL9.add(es.formatAmount(""+VGITEM_AMT.elementAt(i)));
					}
					
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("");
					VPDF_COL3.add("<b>Gross Amount</b>");
					VPDF_COL4.add("");
					VPDF_COL5.add("");
					VPDF_COL6.add("");
					VPDF_COL7.add("");
					VPDF_COL8.add("<b>"+currency_nm+"</b>");
					VPDF_COL9.add("<b>"+es.formatAmount(gross_amt)+"</b>");
					
					srno+=1;
					VPDF_COL1.add(srno);
					VPDF_COL2.add("");
					VPDF_COL3.add("<b>Invoice Amount</b>");
					VPDF_COL4.add("");
					VPDF_COL5.add("");
					VPDF_COL6.add("");
					VPDF_COL7.add("");
					VPDF_COL8.add("<b>"+currency_nm+"</b>");
					VPDF_COL9.add("<b>"+es.formatAmount(net_amt)+"</b>");
//				}
				
				if(!net_amt.equalsIgnoreCase(""))		
				{
					amt_in_word=es.convert(Double.parseDouble(net_amt));
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
	
	
	String callFlag = "";
	public void setCallFlag(String callFlag) {this.callFlag = callFlag;}
	String comp_cd = "";
	public void setComp_cd(String comp_cd) {this.comp_cd = comp_cd;}
	String comp_abbr = "";
	public void setComp_abbr(String comp_abbr) {this.comp_abbr = comp_abbr;}
	
	String entity_role="";
	String month = "";
	String year = "";
	String inv_type = "";
	String inv_no = "";
	String inv_id_seq = "";
	String opration="";
	String counterparty_cd="";
	String name="";
	String abbr="";
	String eff_dt="";
	String service_no="";
	String service_dt="";
	String max_eff_dt="";
	String pan_no="";
	String pan_dt="";
	String notes="";
	String vendor_cd = "";		
	String gst_tin_no = "";		
	String gst_tin_dt = "";		
	String cst_tin_no = "";		
	String cst_tin_dt = "";		
	String gstin_no = "";		
	String gstin_dt = "";		
	String tan_no = "";			
	String tan_issue_dt = "";	
	String ifsc = "";			
	String payee_acc_no = "";	
	String payee_nm = "";	
	String web_addr="";
	String business_flag= "";
	
	String supp_cd="";      
	String supp_nm="";      
	String supp_abbr="";    
	String supp_pin = "";
	String supp_addr="";   
	String supp_city="";    
	String supp_pan_no="";  
	String supp_state=""; 
	String supp_gstin_no = "";
	String supp_state_tin = "";
	
	String vend_abbr="";   	
	String vend_city="";		
	String vend_pan_no="";		
	String vend_gstin_no="";	
	String vend_pin_no="";		
	String vend_country="";		 
	String vend_state="";	 	
	String vendor_name="";	 	
	String vend_state_tin="";	 	
	String sac_code="";	 	
	String sac_des="";	 	
	
	String[] reg_eff_dt= new String[3];
	String[] address_flag= new String[3];
	String[] address= new String[3];
	String[] city= new String[3];
	String[] pin= new String[3];
	String[] state= new String[3];
	String[] zone= new String[3];
	String[] country= new String[3];
	String[] phone= new String[3];
	String[] mobile= new String[3];
	String[] alt_phone= new String[3];
	String[] fax1= new String[3];
	String[] fax2= new String[3];
	String[] email= new String[3];
	
	String inv_dt = "";
	String sac_cd = "";
	String currency = "";
	String exchng_rate = "";
	String exchng_eff_dt = "";
	String purchase_no = "";
	String ref_no = "";
	String sale_amt = "";
	String tax_name = "";
	String total_gst = "";
	String tax_amt = "";
	String desc_item = "";
	String amount = "";
	String net_amt = "";
	String total_amount = "";
	String pacer_no = ""; 
	String remark1 = ""; 
	String remark2 = ""; 
	String activity_value = "";
	String state_tin = "";
	String fin_yr = "";
	String inv_seq = "";
	String print_pdf_type = "";
	String gross_amt = "";
	String view_pdf_type = "";
	String due_dt = "";
	
	String tax_category = "";
	String tax_struct_dt = "";
	String tax_struct_cd = "";
	String tax_struct_info = "";
	String invoice_category = "";
	String invoice_raised_in = "";
	
	String ship_cd = "";
	String ship_name = "";
	String ship_flag = "";
	String ship_item = "";
	String rate = "";
	String inv_due_dt = "";
	String remark3 = "";
	String remark4 = "";
	String vessel_cd = "";
	String vessel_nm = "";
	String vessel_item = "";
	String vessel_agent_nm = "";
	String vessel_flag = "";
	String grt = "";
	String importer_nm = "";
	String inv_qty = "";
	String berthing_hours = "";
	String berthing_slots_no = "";
	String cargo_type = "";
	String user_dt = "";
	String exchang_val = "";
	String amt_in_word = "";
	String gross_revenue = "";
	String gross_less = "";
	String gate_pass = "";
	String sale_no = "";
	
	String cr_dr_type = "";
	String sel_inv_no = "";
	String main_invoice_dt = "";
	String main_invoice_due_dt = "";
	String main_qty_mmbtu = "";
	String main_grt = "";
	String main_berthing_hrs = "";
	String main_berthing_slots = "";
	String main_rate = "";
	String price_cd_nm = "";
	String price_cd = "";
	String main_gross_amt = "";
	String main_tax_amt = "";
	String main_net_payable = "";
	String invoice_raised_in_nm = "";
	String main_tax_struct_cd = "";
	String main_tax_struct_info = "";
	String criteri_formula = "";
	String main_tax_struct_dt = "";
	String main_gross_amt1 = "";
	
	String crdr_invoice_dt = "";
	String crdr_invoice_due_dt = "";
	String net_payable = "";
	String qty_mmbtu = "";
	String berthing_hrs = "";
	String berthing_slots = "";
	String period_start_dt = "";
	String period_end_dt = "";
	String financial_year = "";
	String new_qty_mmbtu = "";
	String new_gross_amt = "";
	String new_tax_amt = "";
	String new_tax_struct_cd = "";
	String new_tax_struct_info = "";
	String new_tax_struct_dt = "";
	String new_net_payable = "";
	String new_grt = "";
	String new_berthing_hrs = "";
	String new_berthing_slots = "";
	String new_rate = "";
	String crdr_remark = "";
	String cess_flag = "";
	String cess_amt = "";
	String bu_unit = "";
	String reason = "";
	String exchange_rate = "";
	String main_exchang_rate = "";
	String new_exchang_rate = "";
	String exchange_rate_cd = "";
	String gross_amt1 = "";
	String new_gross_amt1 = "";
	
	String per_carriage = "";
	String reference_no = "";
	String final_destination = "";
	String country_of_origin = "";
	String port_of_discharge = "";
	String port_of_loading = "";
	String pay_terms = "";
	
	String vend_ship_abbr="";   	
	String vend_ship_city="";		
	String vend_ship_pin_no="";		
	String vend_ship_country="";		 
	String vend_ship_state="";
	
	String vend_bill_abbr="";   	
	String vend_bill_city="";		
	String vend_bill_pin_no="";		
	String vend_bill_country="";		 
	String vend_bill_state="";
	
	boolean submission_chk = false;
	
	public void setOpration(String opration) {this.opration = opration;}
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setVendor_cd(String vendor_cd) {this.vendor_cd = vendor_cd;}
	public void setEntity_role(String entity_role) {this.entity_role = entity_role;}
	public void setMonth(String mnth) {month = mnth;}
	public void setYear(String yr) {year = yr;}
	public void setSupplier_Cd(String supp_cd) {this.supp_cd = supp_cd;}
	public void setInv_type(String inv_type) {this.inv_type = inv_type;}
	public void setInv_No(String inv_no) {this.inv_no = inv_no;}
	public void setPrint_pdf_type(String print_pdf_type) {this.print_pdf_type = print_pdf_type;}
	public void setView_pdf_type(String view_pdf_type) {this.view_pdf_type = view_pdf_type;}
	public void setInv_seq(String inv_seq) {this.inv_seq = inv_seq;}
	public void setFin_yr(String fin_yr) {this.fin_yr = fin_yr;}
	public void setTax_category(String tax_category) {this.tax_category = tax_category;}
	public void setSupp_state(String supp_state) {this.supp_state = supp_state;}
	public void setVend_state(String vend_state) {this.vend_state = vend_state;}
	public void setUser_dt(String user_dt) {this.user_dt = user_dt;}
	public void setCargo_type(String cargo_type) {this.cargo_type = cargo_type;}
	public void setVessel_cd(String vessel_cd) {this.vessel_cd = vessel_cd;}
	public void setShip_cd(String ship_cd) {this.ship_cd = ship_cd;}
	public void setCr_dr_type(String cr_dr_type) {this.cr_dr_type = cr_dr_type;}
	public void setSel_inv_no(String sel_inv_no) {this.sel_inv_no = sel_inv_no;}
	public void setBu_unit(String bu_unit) {this.bu_unit = bu_unit;}

	public String getVendor_cd() {return vendor_cd;}
	public String getName() {return name;}
	public String getAbbr() {return abbr;}
	public String getEff_dt() {return eff_dt;}
	public String getService_no() {return service_no;}
	public String getService_dt() {return service_dt;}
	public String getMax_eff_dt() {return max_eff_dt;}
	public String getPan_no() {return pan_no;}
	public String getPan_dt() {return pan_dt;}
	public String getNotes() {return notes;}
	public String getWeb_addr() {return web_addr;}
	
	public String[] getREG_EFF_DT() {return reg_eff_dt;}
	public String[] getADDRESS_FLAG() {return address_flag;}
	public String[] getADDRESS() {return address;}
	public String[] getCITY() {return city;}
	public String[] getPIN() {return pin;}
	public String[] getSTATE() {return state;}
	public String[] getZONE() {return zone;}
	public String[] getCOUNTRY() {return country;}
	public String[] getPHONE() {return phone;}
	public String[] getMOBILE() {return mobile;}
	public String[] getALT_PHONE() {return alt_phone;}
	public String[] getFAX1() {return fax1;}
	public String[] getFAX2() {return fax2;}
	public String[] getEMAIL() {return email;}
	
	public String getGST_TIN_NO() {return gst_tin_no;}
	public String getGST_TIN_DT() {return gst_tin_dt;}
	public String getCST_TIN_NO() {return cst_tin_no;}
	public String getCST_TIN_DT() {return cst_tin_dt;}
	public String getTAN_NO() {return tan_no;}
	public String getTAN_ISSUE_DT() {return tan_issue_dt;}
	public String getGSTIN_NO() {return gstin_no;}
	public String getGSTIN_DT() {return gstin_dt;}
	public String getPAYEE_ACC_NO() {return payee_acc_no;}
	public String getIFSC() {return ifsc;}
	public String getPAYEE_NM() {return payee_nm;}
	public String getBUSINESS_FLAG() {return business_flag;}

	public String getInv_No() {return inv_no;}
	public String getInv_id_seq() {return inv_id_seq;}
	public String getSupp_cd() {return supp_cd;}
	public String getSupplier_Address() {return supp_addr;}
	public String getSupplier_City() {return supp_city;}
	public String getSupplier_Pan_No() {return supp_pan_no;}
	public String getSupplier_State() {return supp_state;}
	public String getSupp_gstin_no() {return supp_gstin_no;}
	public String getSupp_state_tin() {return supp_state_tin;}
	public String getSupp_abbr() {return supp_abbr;}
	
	public String getVendor_Address() {return vend_abbr;}
	public String getVendor_City() {return vend_city;}
	public String getVendor_Pan_No() {return vend_pan_no;}
	public String getVendor_GstTin_No() {return vend_gstin_no;}
	public String getVendor_Pin_No() {return vend_pin_no;}
	public String getVendor_Country() {return vend_country;}
	public String getVendor_State() {return vend_state;}
	//public String getVendor_Name() {return vendor_name;}
	public String getVend_state_tin() {return vend_state_tin;}
	
	public String getInv_dt() {return inv_dt;}
	public String getDue_dt() {return due_dt;}
	public String getSac_cd() {return sac_cd;}
	public String getCurrency() {return currency;}
	public String getExchng_rate() {return exchng_rate;}
	public String getExchng_eff_dt() {return exchng_eff_dt;}
	public String getPurchase_no() {return purchase_no;}
	public String getRef_no() {return ref_no;}
	public String getTax_name() {return tax_name;}
	public String getSale_amt() {return sale_amt;}
	public String getTotal_gst() {return total_gst;}
	public String getTax_amt() {return tax_amt;}
	public String getDesc_item() {return desc_item;}
	public String getAmount() {return amount;}
	public String getNet_amt() {return net_amt;}
	public String getTotal_amount() {return total_amount;}
	public String getPacer_no() {return pacer_no;}
	public String getremark1() {return remark1;}
	public String getremark2() {return remark2;}
	public String getActivity_value() {return activity_value;}
	public String getFinanical_yr() {return fin_yr;}
	public String getInvoice_seq() {return inv_seq;}
	//public String getApprove_flag() {return approve_flag;}
	public String getGross_amt() {return gross_amt;}
	
	public String getSupp_pin() {return supp_pin;}
	public String getVendor_name() {return vendor_name;}
	public String getSac_code() {return sac_code;}
	public String getSac_des() {return sac_des;}
	public String getState_tin() {return state_tin;}
	public String getSupp_nm() {return supp_nm;}
	
	public String getTax_struct_cd() {return tax_struct_cd;}
	public String getTax_struct_dt() {return tax_struct_dt;}
	public String getTax_struct_info() {return tax_struct_info;}
	public String getInvoice_category() {return invoice_category;}
	public String getInvoice_raised_in() {return invoice_raised_in;}
	public String getExchang_val() {return exchang_val;}
	
	public String getShip_cd() {return ship_cd;}
	public String getShip_name() {return ship_name;}
	public String getShip_flag() {return ship_flag;}
	public String getShip_item() {return ship_item;}
	public String getRate() {return rate;}
	public String getInv_due_dt() {return inv_due_dt;}
	public String getRemark3() {return remark3;}
	public String getRemark4() {return remark4;}
	public String getVessel_cd() {return vessel_cd;}
	public String getVessel_nm() {return vessel_nm;}
	public String getVessel_item() {return vessel_item;}
	public String getVessel_agent_nm() {return vessel_agent_nm;}
	public String getVessel_flag() {return vessel_flag;}
	public String getGrt() {return grt;}
	public String getImporter_nm() {return importer_nm;}
	public String getInv_qty() {return inv_qty;}
	public String getBerthing_hours() {return berthing_hours;}
	public String getBerthing_slots_no() {return berthing_slots_no;}
	public String getCargo_type() {return cargo_type;}
	public String getUser_dt() {return user_dt;}
	public String getAmt_in_word() {return amt_in_word;}
	public String getGross_revenue() {return gross_revenue;}
	public String getGross_less() {return gross_less;}
	public String getGate_pass() {return gate_pass;}
	public String getSale_no() {return sale_no;}
	public String getCess_flag() {return cess_flag;}
	public String getCess_amt() {return cess_amt;}
	public String getExchang_rate() {return exchange_rate;}
	
	public String getMain_invoice_dt() {return main_invoice_dt;}
	public String getMain_invoice_due_dt() {return main_invoice_due_dt;}
	public String getMain_qty_mmbtu() {return main_qty_mmbtu;}
	public String getMain_grt() {return main_grt;}
	public String getMain_berthing_hrs() {return main_berthing_hrs;}
	public String getMain_berthing_slots() {return main_berthing_slots;}
	public String getMain_rate() {return main_rate;}
	public String getPrice_cd() {return price_cd;}
	public String getPrice_cd_nm() {return price_cd_nm;}
	public String getMain_gross_amt() {return main_gross_amt;}
	public String getMain_tax_amt() {return main_tax_amt;}
	public String getMain_net_payable() {return main_net_payable;}
	public String getInvoice_raised_in_nm() {return invoice_raised_in_nm;}
	public String getMain_tax_struct_cd() {return main_tax_struct_cd;}
	public String getMain_tax_struct_info() {return main_tax_struct_info;}
	public String getMain_tax_struct_dt() {return main_tax_struct_dt;}
	public String getCriteri_formula() {return criteri_formula;}
	public String getMain_exchang_rate() {return main_exchang_rate;}
	public String getMain_gross_amt1() {return main_gross_amt1;}
	
	public String getCrdr_invoice_dt() {return crdr_invoice_dt;}
	public String getCrdr_invoice_due_dt() {return crdr_invoice_due_dt;}
	public String getNet_payable() {return net_payable;}
	public String getQty_mmbtu() {return qty_mmbtu;}
	public String getBerthing_hrs() {return berthing_hrs;}
	public String getBerthing_slots() {return berthing_slots;}
	public String getPeriod_start_dt() {return period_start_dt;}
	public String getPeriod_end_dt() {return period_end_dt;}
	public String getFinancial_year() {return financial_year;}
	public String getNew_qty_mmbtu() {return new_qty_mmbtu;}
	public String getNew_gross_amt() {return new_gross_amt;}
	public String getNew_tax_amt() {return new_tax_amt;}
	public String getNew_tax_struct_cd() {return new_tax_struct_cd;}
	public String getNew_tax_struct_info() {return new_tax_struct_info;}
	public String getNew_tax_struct_dt() {return new_tax_struct_dt;}
	public String getNew_net_payable() {return new_net_payable;}
	public String getNew_grt() {return new_grt;}
	public String getNew_berthing_hrs() {return new_berthing_hrs;}
	public String getNew_berthing_slots() {return new_berthing_slots;}
	public String getNew_rate() {return new_rate;}
	public String getNew_exchang_rate() {return new_exchang_rate;}
	public String getExchang_rate_cd() {return exchange_rate_cd;}
	public String getCrdr_remark() {return crdr_remark;}
	public String getBu_unit() {return bu_unit;}
	public String getReason() {return reason;}
	public String getGross_amt1() {return gross_amt1;}
	public String getNew_gross_amt1() {return new_gross_amt1;}
	
	public boolean getSubmission_chk() {return submission_chk;}
	
	public String getPer_carriage() {return per_carriage;}
	public String getReference_no() {return reference_no;}
	public String getFinal_destination() {return final_destination;}
	public String getCountry_of_origin() {return country_of_origin;}
	public String getPort_of_discharge() {return port_of_discharge;}
	public String getPort_of_loading() {return port_of_loading;}
	public String getPay_terms() {return pay_terms;}
	
	public String getVend_Ship_Abbr() {return vend_ship_abbr;}
	public String getVend_Ship_City() {return vend_ship_city;}
	public String getVend_Ship_Pin_No() {return vend_ship_pin_no;}
	public String getVend_Ship_Country() {return vend_ship_country;}
	public String getVend_Ship_State() {return vend_ship_state;}
	
	public String getVend_Bill_Abbr() {return vend_bill_abbr;}
	public String getVend_Bill_City() {return vend_bill_city;}
	public String getVend_Bill_Pin_No() {return vend_bill_pin_no;}
	public String getVend_Bill_Country() {return vend_bill_country;}
	public String getVend_Bill_State() {return vend_bill_state;}

	Vector VSUPPLIER_CD = new Vector();
	Vector VSUPPLIER_NM = new Vector();
	Vector VSUPPLIER_ABBR = new Vector();
	Vector VSAC_CD = new Vector();
	Vector VSAC_DESC = new Vector();
	Vector VSAC_CODE =new Vector();
	Vector VTAX_STRUCTURE_CD = new Vector();
	Vector VTAX_STRUCTURE_DESC = new Vector();
	
	Vector VCOUNTRY_CODE = new Vector();
	Vector VCOUNTRY_NM = new Vector();
	Vector VISO_CODE = new Vector();
	Vector VTIN = new Vector();
	Vector VSTATE_CODE = new Vector();
	Vector VSTATE_NM = new Vector();
	Vector VVENDOR_CD = new Vector();		
	Vector VENDOR_NM = new Vector();		
	Vector VENDOR_ABBR = new Vector();		
	Vector VINVOICE_TYPE = new Vector();
	Vector VINVOICE_TYPE_NM = new Vector();
	Vector VINDEX = new Vector();
	Vector VINVOICE_LIST_ABBR = new Vector();
	Vector VINVOICE_LIST_NAME = new Vector();
	Vector VCHECK_APPROVE = new Vector();
	Vector VPDF_GEN = new Vector();
	Vector VPDF_FILE_NAME = new Vector();
	Vector VLINE_NO = new Vector();
	Vector VLINE_NO1 = new Vector();
	
	Vector VVENDOR_NAME = new Vector();
	Vector VENDORCD = new Vector();
	Vector VINV_NO = new Vector();
	Vector VINV_TYPE = new Vector();
	Vector VINVOICE_DT = new Vector();
	Vector VPDF_TYPE = new Vector();
	Vector VCHECK_FLAG = new Vector();
	Vector VAPPROVE_FLAG = new Vector();
	Vector VPDF_FILE_PATH = new Vector();
	Vector VFIN_YEAR = new Vector();
	Vector VINVOICE_SEQ = new Vector();
	
	Vector VPDF_COL1 = new Vector();
	Vector VPDF_COL2 = new Vector();
	Vector VPDF_COL3 = new Vector();
	Vector VPDF_COL4 = new Vector();
	Vector VPDF_COL5 = new Vector();
	Vector VPDF_COL6 = new Vector();
	Vector VPDF_COL7 = new Vector();
	Vector VPDF_COL8 = new Vector();
	Vector VPDF_COL9 = new Vector();
	Vector VPDF_COL10 = new Vector();
	
	Vector VTAX_STRUCT_CD = new Vector();
	Vector VTAX_STRUCT_NM = new Vector();
	Vector VTAX_STRUCT_APP_DT = new Vector();
	Vector VTAX_STRUCT_STATUS = new Vector();
	Vector VTAX_STRUCT_RMK = new Vector();
	Vector VTAX_CATEGORY = new Vector();
	Vector VTAX_CATEGORY_NM = new Vector();
	Vector VMULTI_TAX_STRUCT = new Vector();
	Vector VSAP_TAX_CODE = new Vector();
	Vector VSAP_GL = new Vector();
	Vector VPAY_RECV = new Vector();
	Vector VPAY_RECV_NM = new Vector();
	Vector VINVOICE_NO = new Vector();
	Vector VINVOICE_ID_SEQ = new Vector();
	
	Vector VSHIP_CD = new Vector();
	Vector VSHIP_NAME = new Vector();
	Vector VSHIP_FLAG = new Vector();
	Vector VSHIP_ITEM = new Vector();
	Vector VEXCHNG_RATE_CD = new Vector();
	Vector VEXCHNG_RATE_NM = new Vector();
	Vector VEXCHNG_RATE = new Vector();
	Vector VEXCHNG_EFF_DT = new Vector();
	Vector VMONTH_DATES = new Vector();
	Vector VCARGO_REF =  new Vector();
	Vector VCARGO_DT =  new Vector();
	Vector VQTY_MMBTU =  new Vector();
	Vector VCARGO_TYPE =  new Vector();
	Vector VDIS_PORT =  new Vector();
	Vector VRATE =  new Vector();
	Vector VCARGO_AMT =  new Vector();
	Vector VIS_IRN_GENERATED =  new Vector();
	Vector VGSIGN =  new Vector();
	Vector VGAMT_DES =  new Vector();
	Vector VGITEM_AMT =  new Vector();
	Vector VLSIGN =  new Vector();
	Vector VLAMT_DES =  new Vector();
	Vector VLITEM_AMT =  new Vector();
	Vector VPRICE =  new Vector();
	Vector VQTY =  new Vector();
	Vector VUOM_NO =  new Vector();
	
	Vector VCRITERIA_FLAG = new Vector();
	Vector VCRITERIA_NAME = new Vector();
	Vector VCRITERIA_HIDE = new Vector();
	Vector VREF_INV_NO = new Vector();
	Vector VINV_FLAG = new Vector();
	Vector VCRDR_NO = new Vector();
	Vector VSEL_INVOICE_NO = new Vector();
	Vector VTAX_AMT = new Vector();
	Vector VCESS_PER = new Vector();
	Vector VCESS_AMT = new Vector();
	Vector VTAX_VAL_AMT = new Vector();
	Vector VBU_CD = new Vector();
	Vector VBU_NM = new Vector();
	Vector VTAX_STRUCTURE = new Vector();
	Vector VTAX_STRUCTURE1 = new Vector();
	Vector VITEM_TOTAL = new Vector();
	Vector VSAC_VAL = new Vector();
	Vector VBU_SEQ = new Vector();
	
	Vector VINVOICE_LIST_NM = new Vector();
	Vector VCRDR_CHECK_APPROVE = new Vector();
	Vector IS_CRDR_SUBMITTED = new Vector();
	
	Vector VMAIN_QTY_MMBTU = new Vector();
	Vector VMAIN_RATE = new Vector();
	Vector VMAIN_CARGO_AMT = new Vector();

	Vector VNEW_QTY_MMBTU = new Vector();
	Vector VNEW_RATE = new Vector();
	Vector VNEW_CARGO_AMT = new Vector();
	Vector VSUN_APPROVAL_FLAG = new Vector();
	Vector VXML_FILE_PATH = new Vector();
	Vector VXML_FILE_NAME = new Vector();
	
	Vector VNSHIP_NAME = new Vector();
	Vector VNCARGO_RATE = new Vector();
	Vector VNCARGO_DATE = new Vector();
	Vector VNCARGO_AMT = new Vector();
	Vector VNQTY_MMBTU = new Vector();
	Vector VCRDR_CRITERIA = new Vector();
	Vector VCRDR_TYPE = new Vector();
	
	Vector VMAIN_QTY = new Vector();
	Vector VMAIN_PRICE = new Vector();
	Vector VMAIN_ITEM_AMT = new Vector();
	Vector VITEM_DES = new Vector();
	Vector VMAIN_TAX_STRUCT_DESC = new Vector();
	Vector VMAIN_TAX_STRUCT_APP_DT = new Vector();
	Vector VMAIN_TAX_STRUCTURE_CD = new Vector();
	Vector VMAIN_TAX_AMT = new Vector();
	
	Vector VNEW_QTY = new Vector();
	Vector VNEW_PRICE = new Vector();
	Vector VNEW_ITEM_AMT = new Vector();
	Vector VNEW_TAX_STRUCT_DESC = new Vector();
	Vector VNEW_TAX_STRUCT_APP_DT = new Vector();
	Vector VNEW_TAX_STRUCTURE_CD = new Vector();
	Vector VNEW_TAX_AMT = new Vector();
	Vector VITEM_AMT = new Vector();
	
	Vector VDIMENSION = new Vector();
	Vector VNET_WT = new Vector();
	Vector VGROSS_WT = new Vector();
	Vector VPACK_DTLS = new Vector();
	
	public Vector getVSUPPLIER_CD() {return VSUPPLIER_CD;}
	public Vector getVSUPPLIER_NM() {return VSUPPLIER_NM;}
	public Vector getVSUPPLIER_ABBR() {return VSUPPLIER_ABBR;}
	public Vector getVSAC_CD() {return VSAC_CD;}
	public Vector getVSAC_DESC() {return VSAC_DESC;}
	public Vector getVSAC_CODE() {return VSAC_CODE;}
	public Vector getVTAX_STRUCTURE_DESC() {return VTAX_STRUCTURE_DESC;}
	public Vector getVTAX_STRUCTURE_CD() {return VTAX_STRUCTURE_CD;}
	public Vector getVTAX_STRUCTURE() {return VTAX_STRUCTURE;}
	public Vector getVTAX_STRUCTURE1() {return VTAX_STRUCTURE1;}
	public Vector getVITEM_TOTAL() {return VITEM_TOTAL;}
	public Vector getVSAC_VAL() {return VSAC_VAL;}
	
	public Vector getVCOUNTRY_CODE() {return VCOUNTRY_CODE;}
	public Vector getVCOUNTRY_NM() {return VCOUNTRY_NM;}
	public Vector getVISO_CODE() {return VISO_CODE;}
	public Vector getVTIN() {return VTIN;}
	public Vector getVSTATE_CODE() {return VSTATE_CODE;}
	public Vector getVSTATE_NM() {return VSTATE_NM;}
	public Vector getVVENDOR_CD() {return VVENDOR_CD;}
	public Vector getVENDOR_NM() {return VENDOR_NM;}
	public Vector getVENDOR_ABBR() {return VENDOR_ABBR;}
	public Vector getVINVOICE_TYPE() {return VINVOICE_TYPE;}
	public Vector getVINVOICE_TYPE_NM() {return VINVOICE_TYPE_NM;}
	public Vector getVINDEX() {return VINDEX;}
	public Vector getVINVOICE_LIST_ABBR() {return VINVOICE_LIST_ABBR;}
	public Vector getVINVOICE_LIST_NAME() {return VINVOICE_LIST_NAME;}
	public Vector getVCHECK_APPROVE() {return VCHECK_APPROVE;}
	public Vector getVPDF_GEN() {return VPDF_GEN;}
	public Vector getVPDF_FILE_NAME() {return VPDF_FILE_NAME;}
	
	public Vector getVVENDOR_NAME() {return VVENDOR_NAME;}
	public Vector getVENDORCD() {return VENDORCD;}
	public Vector getVINV_NO() {return VINV_NO;}
	public Vector getVINV_TYPE() {return VINV_TYPE;}
	public Vector getVINVOICE_DT() {return VINVOICE_DT;}
	public Vector getVPDF_TYPE() {return VPDF_TYPE;}
	public Vector getVCHECK_FLAG() {return VCHECK_FLAG;}
	public Vector getVAPPROVE_FLAG() {return VAPPROVE_FLAG;}
	public Vector getVPDF_FILE_PATH() {return VPDF_FILE_PATH;}
	public Vector getVFIN_YEAR() {return VFIN_YEAR;}
	public Vector getVINVOICE_SEQ() {return VINVOICE_SEQ;}
	
	public Vector getVPDF_COL1() {return VPDF_COL1;}
	public Vector getVPDF_COL2() {return VPDF_COL2;}
	public Vector getVPDF_COL3() {return VPDF_COL3;}
	public Vector getVPDF_COL4() {return VPDF_COL4;}
	public Vector getVPDF_COL5() {return VPDF_COL5;}
	public Vector getVPDF_COL6() {return VPDF_COL6;}
	public Vector getVPDF_COL7() {return VPDF_COL7;}
	public Vector getVPDF_COL8() {return VPDF_COL8;}
	public Vector getVPDF_COL9() {return VPDF_COL9;}
	public Vector getVPDF_COL10() {return VPDF_COL10;}

	public Vector getVTAX_STRUCT_CD() {return VTAX_STRUCT_CD;}
	public Vector getVTAX_STRUCT_NM() {return VTAX_STRUCT_NM;}
	public Vector getVTAX_STRUCT_APP_DT() {return VTAX_STRUCT_APP_DT;}
	public Vector getVTAX_STRUCT_STATUS() {return VTAX_STRUCT_STATUS;}
	public Vector getVTAX_STRUCT_RMK() {return VTAX_STRUCT_RMK;}
	public Vector getVTAX_CATEGORY() {return VTAX_CATEGORY;}
	public Vector getVTAX_CATEGORY_NM() {return VTAX_CATEGORY_NM;}
	public Vector getVMULTI_TAX_STRUCT() {return VMULTI_TAX_STRUCT;}
	public Vector getVSAP_TAX_CODE() {return VSAP_TAX_CODE;}
	public Vector getVSAP_GL() {return VSAP_GL;}
	public Vector getVPAY_RECV() {return VPAY_RECV;}
	public Vector getVPAY_RECV_NM() {return VPAY_RECV_NM;}
	public Vector getVINVOICE_NO() {return VINVOICE_NO;}
	public Vector getVINVOICE_ID_SEQ() {return VINVOICE_ID_SEQ;}
	
	public Vector getVEXCHNG_RATE_CD() {return VEXCHNG_RATE_CD;}
	public Vector getVEXCHNG_RATE_NM() {return VEXCHNG_RATE_NM;}
	public Vector getVEXCHNG_RATE() {return VEXCHNG_RATE;}
	public Vector getVEXCHNG_EFF_DT() {return VEXCHNG_EFF_DT;}
	
	public Vector getVCARGO_REF() {return VCARGO_REF;}
	public Vector getVCARGO_DT() {return VCARGO_DT;}
	public Vector getVSHIP_CD() {return VSHIP_CD;}
	public Vector getVQTY_MMBTU() {return VQTY_MMBTU;}
	public Vector getVCARGO_TYPE() {return VCARGO_TYPE;}
	public Vector getVSHIP_NAME() {return VSHIP_NAME;}
	public Vector getVSHIP_FLAG() {return VSHIP_FLAG;}
	public Vector getVSHIP_ITEM() {return VSHIP_ITEM;}
	public Vector getVDIS_PORT() {return VDIS_PORT;}
	public Vector getVRATE() {return VRATE;}
	public Vector getVCARGO_AMT() {return VCARGO_AMT;}
	public Vector getVIS_IRN_GENERATED() {return VIS_IRN_GENERATED;}
	public Vector getVLINE_NO() {return VLINE_NO;}
	public Vector getVLINE_NO1() {return VLINE_NO1;}
	public Vector getVGSIGN() {return VGSIGN;}
	public Vector getVGAMT_DES() {return VGAMT_DES;}
	public Vector getVGITEM_AMT() {return VGITEM_AMT;}
	public Vector getVLSIGN() {return VLSIGN;}
	public Vector getVLAMT_DES() {return VLAMT_DES;}
	public Vector getVLITEM_AMT() {return VLITEM_AMT;}
	public Vector getVPRICE() {return VPRICE;}
	public Vector getVQTY() {return VQTY;}
	public Vector getVUOM_NO() {return VUOM_NO;}
	
	public Vector getVCRITERIA_FLAG() {return VCRITERIA_FLAG;}
	public Vector getVCRITERIA_NAME() {return VCRITERIA_NAME;}
	public Vector getVCRITERIA_HIDE() {return VCRITERIA_HIDE;}
	public Vector getVINV_FLAG() {return VINV_FLAG;}
	public Vector getVREF_INV_NO() {return VREF_INV_NO;}
	public Vector getVCRDR_NO() {return VCRDR_NO;}
	public Vector getVSEL_INVOICE_NO() {return VSEL_INVOICE_NO;}
	public Vector getVTAX_AMT() {return VTAX_AMT;}
	public Vector getVCESS_PER() {return VCESS_PER;}
	public Vector getVCESS_AMT() {return VCESS_AMT;}
	public Vector getVBU_CD() {return VBU_CD;}
	public Vector getVBU_NM() {return VBU_NM;}
	public Vector getVBU_SEQ() {return VBU_SEQ;}
	
	public Vector getVINVOICE_LIST_NM() {return VINVOICE_LIST_NM;}
	public Vector getVCRDR_CHECK_APPROVE() {return VCRDR_CHECK_APPROVE;}
	public Vector getIS_CRDR_SUBMITTED() {return IS_CRDR_SUBMITTED;}

	public Vector getVMAIN_QTY_MMBTU() {return VMAIN_QTY_MMBTU;}
	public Vector getVMAIN_RATE() {return VMAIN_RATE;}
	public Vector getVMAIN_CARGO_AMT() {return VMAIN_CARGO_AMT;}
	
	public Vector getVNEW_QTY_MMBTU() {return VNEW_QTY_MMBTU;}
	public Vector getVNEW_RATE() {return VNEW_RATE;}
	public Vector getVNEW_CARGO_AMT() {return VNEW_CARGO_AMT;}
	public Vector getVSUN_APPROVAL_FLAG() {return VSUN_APPROVAL_FLAG;}
	public Vector getVXML_FILE_NAME() {return VXML_FILE_NAME;}
	public Vector getVXML_FILE_PATH() {return VXML_FILE_PATH;}
	
	public Vector getVNSHIP_NAME() {return VNSHIP_NAME;}
	public Vector getVNCARGO_RATE() {return VNCARGO_RATE;}
	public Vector getVNCARGO_DATE() {return VNCARGO_DATE;}
	public Vector getVNCARGO_AMT() {return VNCARGO_AMT;}
	public Vector getVNQTY_MMBTU() {return VNQTY_MMBTU;}
	public Vector getVCRDR_CRITERIA() {return VCRDR_CRITERIA;}
	public Vector getVCRDR_TYPE() {return VCRDR_TYPE;}
	
	public Vector getVMAIN_QTY() {return VMAIN_QTY;}
	public Vector getVMAIN_PRICE() {return VMAIN_PRICE;}
	public Vector getVMAIN_ITEM_AMT() {return VMAIN_ITEM_AMT;}
	public Vector getVITEM_DES() {return VITEM_DES;}
	public Vector getVMAIN_TAX_STRUCT_DESC() {return VMAIN_TAX_STRUCT_DESC;}
	public Vector getVMAIN_TAX_STRUCT_APP_DT() {return VMAIN_TAX_STRUCT_APP_DT;}
	public Vector getVMAIN_TAX_STRUCTURE_CD() {return VMAIN_TAX_STRUCTURE_CD;}
	public Vector getVMAIN_TAX_AMT() {return VMAIN_TAX_AMT;}
	
	public Vector getVNEW_QTY() {return VNEW_QTY;}
	public Vector getVNEW_PRICE() {return VNEW_PRICE;}
	public Vector getVNEW_ITEM_AMT() {return VNEW_ITEM_AMT;}
	public Vector getVNEW_TAX_STRUCT_DESC() {return VNEW_TAX_STRUCT_DESC;}
	public Vector getVNEW_TAX_STRUCT_APP_DT() {return VNEW_TAX_STRUCT_APP_DT;}
	public Vector getVNEW_TAX_STRUCTURE_CD() {return VNEW_TAX_STRUCTURE_CD;}
	public Vector getVNEW_TAX_AMT() {return VNEW_TAX_AMT;}
	public Vector getVITEM_AMT() {return VITEM_AMT;}
	
	public Vector getVDIMENSION() {return VDIMENSION;}
	public Vector getVNET_WT() {return VNET_WT;}
	public Vector getVGROSS_WT() {return VGROSS_WT;}
	public Vector getVPACK_DTLS() {return VPACK_DTLS;}

}
