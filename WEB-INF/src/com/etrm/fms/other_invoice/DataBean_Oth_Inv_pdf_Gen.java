package com.etrm.fms.other_invoice;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.CurrencyUtil;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.events.FieldPositioningEvents;

public class DataBean_Oth_Inv_pdf_Gen
{
	String db_src_file_name="DataBean_Oth_Inv_pdf_Gen.java";
	
	Connection conn;
	PreparedStatement stmt;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt_tmp;
	ResultSet rset;
	ResultSet rset1;
	ResultSet rset2;
	ResultSet rset_tmp;
	String queryString="";
	String queryString1="";
	String queryString2="";
	
	public static final String SIGNAME = "Signature1";

	UtilBean utilBean = new UtilBean();
	DateUtil dateUtil = new DateUtil();
	CurrencyUtil es=new CurrencyUtil();

	public HttpServletRequest request = null;
	public void setRequest(HttpServletRequest request) {this.request = request;}

	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("###########0.0000");
	NumberFormat nf3 = new DecimalFormat("###########0.000");
	NumberFormat nf0 = new DecimalFormat("###########0.0");

	public void init()
	{
		synchronized (this) 
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
		    			if(callFlag.equalsIgnoreCase("PDF_OTHER_INVOICE")) //Deep20251017
		    			{
		    				if(inv_type.equals("COSTR")) 
		    				{
		    					pdfcostrecharge(); //Deep20251017
		    				}
		    				if(inv_type.equals("COSTRH")) 
		    				{
		    					pdfcostrechargeHPPL(); //AP20251017
		    				}
		    				if(inv_type.equals("HSA")) 
		    				{
		    					pdfcostrechargeHPPLShippingAgent(); //AP20251106
		    				}
		    				if(inv_type.equals("HS")) 
		    				{
		    					pdfHPPLSEIPL(); //Deep20251107
		    				}
		    				if(inv_type.equals("NPR")) 
		    				{
		    					pdfNPR(); //AP20251110
		    				}
		    				if(inv_type.equals("AHPL")) 
		    				{
		    					pdfAHPLShare(); //Deep20251117
		    				}
		    				if(inv_type.equals("SFA")) 
		    				{
		    					pdfSFA(); //Deep20251120
		    				}
		    				if(inv_type.equals("GA")) 
		    				{
		    					pdfGA(); //Deep20251231
		    				}
		    				if(inv_type.equals("RXP")) 
		    				{
		    					pdfRXP(); 
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
		    	if(rset1 != null){try{rset1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(rset2 != null){try{rset2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(rset_tmp != null){try{rset_tmp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(stmt1 != null){try{stmt1.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(stmt2 != null){try{stmt2.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(stmt_tmp != null){try{stmt_tmp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(conn != null){try{conn.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    }
		}
	}
	
	
	public void deletePdfFile(String pdfFile)
	{
	    String function_nm = "deletePdfFile()";
	    try
	    {
	        File file = new File(pdfFile);
	        if (file.exists())
	        {
	            boolean delete = file.delete();
	            if (!delete)
	            {
	                new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, 
	                    new Exception("Failed to delete file: " + pdfFile));
	            }
	        }
	        else
	        {
	            new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, 
	                new Exception("File not found: " + pdfFile));
	        }
	    }
	    catch (Exception e)
	    {
	        new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
	        throw e;
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
			if(inv_type.equals("HS") || inv_type.equals("COSTRH"))
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
	
	public void getInvoiceDtls()
	{	
		String function_nm="getInvoiceDtls()";

		try
		{
			taxValue = 0;
			tax_value="";
			queryString = "SELECT A.COMPANY_CD,A.VENDOR_CD,A.SUPPLIER_CD,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),B.VENDOR_SUPP_INV_REF,"
					+ "B.PACER_NO,A.GROSS_AMT,A.TAX_AMT_INR,A.NET_PAYABLE,A.EXCHG_RATE_VALUE,TO_CHAR(A.EXCHG_RATE_DT, 'DD/MM/YYYY'),"
					+ "A.SALE_PRICE_UNIT,B.SAC_CODE,B.TAX_STRUCT_CD,B.ITEM_DESCRIPTION,A.REMARK,A.REMARK1,A.APPROVED_FLAG,A.FINANCIAL_YEAR,"
					+ "A.INVOICE_SEQ,A.SALE_AMT,A.CHECKED_FLAG,A.INVOICE_ID_SEQ,TO_CHAR(B.TAX_EFF_DT, 'DD/MM/YYYY'),A.INVOICE_CATEGORY,A.INVOICE_RAISED_IN "
					+ "FROM FMS_OTH_INVOICE_MST A, FMS_OTH_INVOICE_DTL B ";
			
			if(is_print.equals("0"))
			{
				
				queryString	+= "WHERE A.COMPANY_CD = ? AND A.INVOICE_SEQ = ? AND A.INVOICE_TYPE = ? AND A.FINANCIAL_YEAR=? AND A.SUPPLIER_CD=? AND A.INV_FLAG = ? "
						+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.INVOICE_TYPE = B.INVOICE_TYPE "
						+ "AND A.VENDOR_CD = B.VENDOR_CD AND A.SUPPLIER_CD = B.SUPPLIER_CD AND A.INVOICE_SEQ = B.INVOICE_SEQ AND A.INV_FLAG = B.INV_FLAG";
			}
			else
			{
				queryString	+= "WHERE A.COMPANY_CD = ? AND A.INVOICE_NO = ? AND A.INVOICE_TYPE = ? AND A.INV_FLAG = ? "
						+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.INVOICE_TYPE = B.INVOICE_TYPE "
						+ "AND A.VENDOR_CD = B.VENDOR_CD AND A.SUPPLIER_CD = B.SUPPLIER_CD AND A.INVOICE_SEQ = B.INVOICE_SEQ AND A.INV_FLAG = B.INV_FLAG";
			}	
			stmt = conn.prepareStatement(queryString);
			if(is_print.equals("0"))
			{
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_seq);
				stmt.setString(3, inv_type);
				stmt.setString(4, fin_yr);
				stmt.setString(5, supp_cd);
				stmt.setString(6, "F");
			}
			else 
			{
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_no);
				stmt.setString(3, inv_type);
				stmt.setString(4, "F");
			}
			rset = stmt.executeQuery();
			
			if(rset.next()) 
			{
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
				tax_struct_dt = rset.getString(24) == null ? "" : rset.getString(24);
				desc_item = rset.getString(15) == null ? "" : rset.getString(15);
				remark1 = rset.getString(16) == null ? "" : rset.getString(16);
				remark2 = rset.getString(17) == null ? "" : rset.getString(17);
				fin_yr = rset.getString(19) == null ? "" : rset.getString(19);
				inv_seq = rset.getString(20) == null ? "" : rset.getString(20);
				sale_amt = nf.format(Double.parseDouble(rset.getString(21) == null ? "" : rset.getString(21)));
				inv_id_seq = rset.getString(23) == null ? "" : rset.getString(23);
				invoice_category = rset.getString(25) == null ? "" : rset.getString(25);
				invoice_raised_in = rset.getString(26) == null ? "" : rset.getString(26);
				
				taxValue = rset.getDouble(8);
				tax_name=utilBean.getTaxDescr(conn,tax_struct_cd);
				
				if (tax_name.contains(",")) 
				{
				    tax_amt = nf.format(Double.parseDouble((Double.doubleToRawLongBits(taxValue)==Double.doubleToRawLongBits(0)) ? "0" : String.format("%.2f", taxValue / 2)));
				} else 
				{
					tax_amt = nf.format(Double.parseDouble((Double.doubleToRawLongBits(taxValue)==Double.doubleToRawLongBits(0)) ? "0" : String.format("%.2f", taxValue)));
				}	
				tax_value=nf.format(taxValue);
				
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
			taxValue = 0;
			tax_value = "";
			queryString = "SELECT A.COMPANY_CD, A.SUPPLIER_CD, A.VENDOR_CD, TO_CHAR(A.INVOICE_DT, 'DD/MM/YYYY'), B.SAC_CODE, A.SALE_PRICE_UNIT, "
					+ "A.EXCHG_RATE_VALUE, TO_CHAR(A.EXCHG_RATE_DT, 'DD/MM/YYYY'),"
					+ "A.GROSS_AMT, A.TAX_AMT_INR, B.PURCHASE_NO, B.REFERENCE_NO, B.TAX_STRUCT_CD, B.ITEM_DESCRIPTION, A.NET_PAYABLE,A.SALE_AMT,A.REMARK,A.REMARK1,"
					+ "A.APPROVED_FLAG,A.FINANCIAL_YEAR,A.INVOICE_SEQ,A.CHECKED_FLAG,A.INVOICE_ID_SEQ,TO_CHAR(B.TAX_EFF_DT, 'DD/MM/YYYY'),A.INVOICE_CATEGORY,A.INVOICE_RAISED_IN  "
					+ "FROM FMS_OTH_INVOICE_MST A, FMS_OTH_INVOICE_DTL B ";
			if(is_print.equals("0"))
			{
				queryString += "WHERE A.COMPANY_CD = ? AND A.INVOICE_SEQ = ? AND A.INVOICE_TYPE = ? AND A.FINANCIAL_YEAR=? AND A.SUPPLIER_CD=? AND A.INV_FLAG = ? "
						+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.INVOICE_SEQ = B.INVOICE_SEQ AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.INVOICE_TYPE = B.INVOICE_TYPE AND "
						+ "A.INVOICE_DT = B.EFF_DT AND A.VENDOR_CD = B.VENDOR_CD AND A.SUPPLIER_CD = B.SUPPLIER_CD AND A.INV_FLAG = B.INV_FLAG";
			}
			else 
			{
				queryString += "WHERE A.COMPANY_CD = ? AND A.INVOICE_NO = ? AND A.INVOICE_TYPE = ? AND A.INV_FLAG = ? "
						+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.INVOICE_SEQ = B.INVOICE_SEQ AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.INVOICE_TYPE = B.INVOICE_TYPE AND "
						+ "A.INVOICE_DT = B.EFF_DT AND A.VENDOR_CD = B.VENDOR_CD AND A.SUPPLIER_CD = B.SUPPLIER_CD AND A.INV_FLAG = B.INV_FLAG";
			}
					
			stmt = conn.prepareStatement(queryString);
			if(is_print.equals("0"))
			{
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_seq);
				stmt.setString(3, inv_type);
				stmt.setString(4, fin_yr);
				stmt.setString(5, supp_cd);
				stmt.setString(6, "F");
			}
			else
			{
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_no);
				stmt.setString(3, inv_type);
				stmt.setString(4, "F");
			}
			rset = stmt.executeQuery();
			if(rset.next()) 
			{
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
				tax_struct_dt = rset.getString(23) == null ? "" : rset.getString(23);
				taxValue = rset.getDouble(10);
				tax_name=utilBean.getTaxDescr(conn,tax_struct_cd);
				if (tax_name.contains(",")) 
				{
				    tax_amt = nf.format(Double.parseDouble((Double.doubleToRawLongBits(taxValue)==Double.doubleToRawLongBits(0)) ? "0" : String.format("%.2f", taxValue / 2)));
				} 
				else 
				{
					tax_amt = nf.format(Double.parseDouble((Double.doubleToRawLongBits(taxValue)==Double.doubleToRawLongBits(0)) ? "0" : String.format("%.2f", taxValue)));
				}
				purchase_no = rset.getString(11) == null ? "" : rset.getString(11);
				ref_no = rset.getString(12) == null ? "" : rset.getString(12);
				tax_value=nf.format(taxValue);
				desc_item = rset.getString(14) == null ? "" : rset.getString(14);
				net_amt = nf.format(Double.parseDouble(rset.getString(15) == null ? "" : rset.getString(15)));
				remark1=rset.getString(17) == null ? "" : rset.getString(17);
				remark2=rset.getString(18) == null ? "" : rset.getString(18);
				fin_yr = rset.getString(20) == null ? "" : rset.getString(20);
				inv_seq = rset.getString(21) == null ? "" : rset.getString(21);
				inv_id_seq = rset.getString(22) == null ? "" : rset.getString(22);
				invoice_category = rset.getString(24) == null ? "" : rset.getString(24);
				invoice_raised_in = rset.getString(25) == null ? "" : rset.getString(25);
				
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

	//Deep : 
	public void pdfcostrecharge() throws SQLException
	{
		String function_nm="pdfcostrecharge()";
		
		String emp_nm ="";
		String ip = "";
		msg="";
		//String msg_content="";
		String path ="";
		
		Rectangle pageSize = new Rectangle(595, 842);
		
		pageSize.setBackgroundColor(BaseColor.WHITE);
		
		Document document = new Document(pageSize);
		try
		{
			HttpSession session = request.getSession();
			emp_nm = (String)session.getAttribute("emp_nm")==null?"":(String)session.getAttribute("emp_nm");
			ip = (String)session.getAttribute("ip")==null?"":(String)session.getAttribute("ip");
			
			String company_nm = ""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");
			String company_abbr = ""+session.getAttribute("comp_abbr")==null?"":""+session.getAttribute("comp_abbr");
			
			int pdfGenCount=0;
			
			if(is_print.equals("1"))
			{
				queryString = "SELECT COUNT(*) "
							+ "FROM FMS_OTH_INVOICE_MST A ,FMS_OTH_INV_FILE_DTL B "
							+ "WHERE A.COMPANY_CD=? AND A.INVOICE_NO = ? AND A.INVOICE_TYPE = ? ";
							if(print_pdf_type.equals("O"))
							{
								queryString+= "AND A.PRINT_BY_ORI IS NOT NULL AND A.PRINT_DT_ORI IS NOT NULL ";
							}
							else if(print_pdf_type.equals("T"))
							{
								queryString+= "AND A.PRINT_BY_TRI IS NOT NULL AND A.PRINT_DT_TRI IS NOT NULL ";
							}
							else if(print_pdf_type.equals("D"))
							{
								queryString+= "AND A.PRINT_BY_DUP IS NOT NULL AND A.PRINT_DT_DUP IS NOT NULL ";
							}
				queryString += "AND B.PDF_TYPE=? AND B.INVOICE_TYPE = ? AND B.FILE_NAME IS NOT NULL "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.INVOICE_TYPE = B.INVOICE_TYPE AND A.SUPPLIER_CD = B.SUPPLIER_CD";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_no);
				stmt.setString(3, inv_type);
				stmt.setString(4, print_pdf_type);
				stmt.setString(5, inv_type);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					pdfGenCount=rset.getInt(1);
				}
				rset.close();
				stmt.close();
			}
			else 
			{
				pdfGenCount = 0;
			}
	
			if(pdfGenCount>0)
			{
				msg="Failed! - PDF for RCM Invoice(Cost Recharge) with Invoice No. "+inv_no+" already Generated!";
	        	msg_type="E";
	        }
			else
			{
				
				getInvoiceDtls();
				getSupplierDtl();
				getVendorDtl();
				
				String print_pdf_type_nm="ORIGINAL";
				if(print_pdf_type.equals("O"))
				{
					print_pdf_type_nm="ORIGINAL";
				}
				else if(print_pdf_type.equals("D"))
				{
					print_pdf_type_nm="DUPLICATE";
				}
	
				String appPath = request.getServletContext().getRealPath("");
	
				String main_folder="";
				if(!comp_cd.equals(""))
				{
					main_folder=CommonVariable.work_dir+comp_cd;
				}
				
				String sub_folder="unsigned_pdf";
				String sub_folder2="ig_invoice";
				
				String temp_path=appPath+File.separator+main_folder+File.separator+sub_folder+File.separator+sub_folder2;
				File main_folderDir = new File(temp_path);
		        if(!main_folderDir.exists())
		        {
		        	main_folderDir.mkdirs();
		        }
		        
		        if(is_print.equals("1"))
		        {
		        	 file_nm=print_pdf_type+"-"+supp_abbr+"-"+inv_type+"-"+inv_no.replace("/", "-")+".pdf";
		        }
		        else
		        {
		        	file_nm="temp.pdf";
		        }
	
				path = appPath+"/"+main_folder+"/"+sub_folder+"/"+sub_folder2+"/"+file_nm;
				PdfWriter writer = PdfWriter.getInstance(document,new FileOutputStream(path));
	
				document.open();
				document.setPageSize(pageSize);
	            document.newPage();
	
	            String context_nm = request.getContextPath();
				String server_nm = request.getServerName();
				String server_port = ""+request.getServerPort();
	
				file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm;
	
				Font small_black_normal = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
				Font small_black_normal_10 = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
				Font small_black_normal_14 = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.NORMAL, BaseColor.BLACK);
				Font small_black_normal_12 = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
				Font small_black_bold = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
	            Font black_bold = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
	            Font black_bold1 = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
	            
	            PdfPCell company_logo_cell = new PdfPCell();
	            if(!comp_logo.equals(""))
	            {
					String file_path = request.getRealPath("/"+CommonVariable.company_logo_path+"/"+comp_logo);
	            	Image company_logo = Image.getInstance(file_path);
		            company_logo.setBorder(Rectangle.NO_BORDER);
		            company_logo.scaleAbsolute(44,40);
		            company_logo_cell = new PdfPCell(company_logo,false);
		            company_logo_cell.setBorder(Rectangle.NO_BORDER);
		            company_logo_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            }
	            
	            float[] mainWidths = {10f, 70f, 15f};  
	            PdfPTable mainTable = new PdfPTable(mainWidths);
	            mainTable.setWidthPercentage(100);
	            mainTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

	            PdfPCell logoCell = new PdfPCell(company_logo_cell);
	            logoCell.setBorder(Rectangle.NO_BORDER);
	            logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            logoCell.setPaddingLeft(10);
	            mainTable.addCell(logoCell);
	            
	            PdfPTable centerTable = new PdfPTable(1);
	            centerTable.setWidthPercentage(100);
	            centerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            centerTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            centerTable.addCell(new Phrase(new Chunk(print_pdf_type_nm, small_black_normal_10)));
	            centerTable.addCell(new Phrase(new Chunk(supp_nm, black_bold1)));
	            centerTable.addCell(new Phrase(new Chunk("TAX INVOICE", small_black_normal_12)));
	            String info = "Tax invoice issued under Rule 46 of the Central Goods and Services Tax Rules, 2017";
	            centerTable.addCell(new Phrase(new Chunk(info, small_black_normal)));

	            PdfPCell centerCell = new PdfPCell(centerTable);
	            centerCell.setBorder(Rectangle.NO_BORDER);
	            centerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            mainTable.addCell(centerCell);

	            PdfPCell emptyCell = new PdfPCell(new Phrase(""));
	            emptyCell.setBorder(Rectangle.NO_BORDER);
	            mainTable.addCell(emptyCell);
	            
	            float[] ContactAddrWidths5 = {0.80f,0.10F,0.20F,0.80F};
	   	     	PdfPTable Table3 = new PdfPTable(ContactAddrWidths5);
	            Table3.setWidthPercentage(100);
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("Registered Office: ",small_black_bold)));
	            
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("To:",small_black_bold)));
	
				float[] ContactAddrWidths6 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table4 = new PdfPTable(ContactAddrWidths6);
	            Table4.setWidthPercentage(100);
	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk(supp_nm,small_black_bold)));
	            
	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk(""+vendor_name+",",small_black_bold)));
	
	            float[] ContactAddrWidths7 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table5 = new PdfPTable(ContactAddrWidths7);
	            Table5.setWidthPercentage(100);
	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk(supp_addr+" "+supp_city,small_black_normal)));
	            
	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk(vend_abbr+" "+vend_city,small_black_normal)));
	            
	
	            float[] ContactAddrWidths8 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table6 = new PdfPTable(ContactAddrWidths8);
	            Table6.setWidthPercentage(100);
	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table6.addCell(new Phrase(new Chunk(supp_state+"-"+supp_pin,small_black_normal)));
	
	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table6.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_pin_no.equals("")) 
	            {
	            	Table6.addCell(new Phrase(new Chunk(vend_state + "-" + vend_pin_no, small_black_normal)));
				}
	            else 
	            {
	            	Table6.addCell(new Phrase(new Chunk(vend_state, small_black_normal)));
	            }
	            
	           	float[] ContactAddrWidths9 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table7 = new PdfPTable(ContactAddrWidths9);
	            Table7.setWidthPercentage(100);
	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk("State: "+supp_state,small_black_normal)));
	
	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk("State: "+vend_state,small_black_normal)));
	            
	            float[] ContactAddrWidths10 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table8 = new PdfPTable(ContactAddrWidths10);
	            Table8.setWidthPercentage(100);
	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table8.addCell(new Phrase(new Chunk("State Code: "+supp_state_tin,small_black_normal)));
	
	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table8.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_state_tin.equals("")) 
	            {
					Table8.addCell(new Phrase(new Chunk("State Code: " + vend_state_tin, small_black_normal)));
				}
	            else 
	            {
					Table8.addCell(new Phrase(new Chunk("", small_black_normal)));
	            }
	            
	            float[] ContactAddrWidths11 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table9 = new PdfPTable(ContactAddrWidths11);
	            Table9.setWidthPercentage(100);
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table9.addCell(new Phrase(new Chunk("GSTIN: "+supp_gstin_no,small_black_normal)));
	
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table9.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_gstin_no.equals("")) 
	            {
					Table9.addCell(new Phrase(new Chunk("GSTIN: " + vend_gstin_no, small_black_normal)));
				}
	            else 
	            {
	            	Table9.addCell(new Phrase(new Chunk("", small_black_normal)));
	            }

	            float[] ContactAddrWidths12 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table10 = new PdfPTable(ContactAddrWidths12);
	            Table10.setWidthPercentage(100);
	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table10.addCell(new Phrase(new Chunk("PAN: "+supp_pan_no,small_black_normal)));
	
	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table10.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_pan_no.equals("")) 
	            {
					Table10.addCell(new Phrase(new Chunk("PAN: " + vend_pan_no, small_black_normal)));
				}
	            else 
	            {
	            	Table10.addCell(new Phrase(new Chunk("", small_black_normal)));
	            }
	            
	            float[] ContactAddrWidths13 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table11 = new PdfPTable(ContactAddrWidths13);
	            Table11.setWidthPercentage(100);
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("SAC: "+sac_code,small_black_normal)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            float[] ContactAddrWidths14 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table12 = new PdfPTable(ContactAddrWidths14);
	            Table12.setWidthPercentage(100);
	            Table12.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table12.addCell(new Phrase(new Chunk("Description of Service: "+sac_des,small_black_normal)));
	
	            Table12.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table12.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table12.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table12.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            float[] ContactAddrWidths15 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table13 = new PdfPTable(ContactAddrWidths15);
	            Table13.setWidthPercentage(100);
	            Table13.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table13.addCell(new Phrase(new Chunk("Place of Supply: "+supp_state,small_black_normal)));
	
	            Table13.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table13.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table13.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table13.addCell(new Phrase(new Chunk("",small_black_normal)));
            
	            float[] ContactAddrWidths16 = {0.80F,0.20F,0.50F,0.40F};
	   	     	PdfPTable Table14 = new PdfPTable(ContactAddrWidths16);
	            Table14.setWidthPercentage(100);
	            Table14.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table14.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table14.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table14.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table14.addCell(new Phrase(new Chunk("Invoice No :",black_bold)));
	
	            Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table14.addCell(new Phrase(new Chunk(inv_no,black_bold)));
	            
	            float[] ContactAddrWidths17 = {0.80F,0.20F,0.50F,0.40F};
	   	     	PdfPTable Table15 = new PdfPTable(ContactAddrWidths17);
	            Table15.setWidthPercentage(100);
	            Table15.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table15.addCell(new Phrase(new Chunk("Invoice Date :",black_bold)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk(inv_dt,black_bold)));

	            float[] ContactAddrWidths18 = {0.80F,0.20F,0.50F,0.40F};
	   	     	PdfPTable Table16 = new PdfPTable(ContactAddrWidths18);
	            Table16.setWidthPercentage(100);
	            Table16.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table16.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table16.addCell(new Phrase(new Chunk("Vendor/Supplier Inv Ref No :",black_bold)));
	
	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk(ref_no,black_bold)));
	            
	            float[] ContactAddrWidths19 = {0.80F,0.20F,0.50F,0.40F};
	   	     	PdfPTable Table17 = new PdfPTable(ContactAddrWidths19);
	            Table17.setWidthPercentage(100);
	            Table17.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table17.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table17.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table17.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table17.addCell(new Phrase(new Chunk("Pace WO/PO No: ",black_bold)));
	
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table17.addCell(new Phrase(new Chunk(pacer_no,black_bold)));
	                      
	            float[] ContactAddrWidths20 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table18 = new PdfPTable(ContactAddrWidths20);
	            Table18.setWidthPercentage(100);
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk("Sr.No.",small_black_bold)));
	
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk("Description",small_black_bold)));
	
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk("Currency",small_black_bold)));
	
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk("Amount",small_black_bold)));

	            
	            String curr1 = "",curr2="",curr3="";
	            if(currency.equals("1")) 
	            {
	            	curr1 = "INR";
	            	curr2 = "INR";
	            	curr3 = "INR";
	            }
	            else if(currency.equals("2"))
	            {
	            	curr1 = "USD";
	            	curr2 = "INR/USD";
	            	curr3 = "INR";
	            }
	            else if(currency.equals("3")) 
	            {	
	            	curr1 = "GBP";
	            	curr2 = "INR/GBP";
	            	curr3 = "INR";
	            }
	            else if(currency.equals("4")) 
	            {	
	            	curr1 = "EURO";
	            	curr2 = "INR/EURO";
	            	curr3 = "INR";
	            }
	            else if(currency.equals("5")) 
	            {	
	            	curr1 = "YEN";
	            	curr2 = "INR/YEN";
	            	curr3 = "INR";
	            }
	            
	            int sr=1;
	            float[] ContactAddrWidths21 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table19 = new PdfPTable(ContactAddrWidths21);
	            Table19.setWidthPercentage(100);
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table19.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table19.addCell(new Phrase(new Chunk(desc_item,small_black_normal)));
	
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table19.addCell(new Phrase(new Chunk(curr1,small_black_normal)));
	
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table19.addCell(new Phrase(new Chunk(es.formatAmount(sale_amt),small_black_normal)));
	            
	            
	            if (!currency.equals("1")) {
					sr += 1;
				}
				float[] ContactAddrWidths22 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table20 = new PdfPTable(ContactAddrWidths22);
	            Table20.setWidthPercentage(100);
	            Table20.getDefaultCell().setBorder(Rectangle.BOX);
	            Table20.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table20.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table20.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table20.getDefaultCell().setBorder(Rectangle.BOX);
	            Table20.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table20.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table20.addCell(new Phrase(new Chunk("Exchange Rate : "+exchng_rate+" ("+curr2+") dated "+exchng_eff_dt,small_black_normal)));
	
	            Table20.getDefaultCell().setBorder(Rectangle.BOX);
	            Table20.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table20.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table20.addCell(new Phrase(new Chunk(curr2,small_black_bold)));
	
	            Table20.getDefaultCell().setBorder(Rectangle.BOX);
	            Table20.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table20.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table20.addCell(new Phrase(new Chunk(exchng_rate,small_black_normal)));
	            
	            sr+=1;
	            float[] ContactAddrWidths23 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table21 = new PdfPTable(ContactAddrWidths23);
	            Table21.setWidthPercentage(100);
	            Table21.getDefaultCell().setBorder(Rectangle.BOX);
	            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table21.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table21.getDefaultCell().setBorder(Rectangle.BOX);
	            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table21.addCell(new Phrase(new Chunk("Total Amount ",small_black_bold)));
	
	            Table21.getDefaultCell().setBorder(Rectangle.BOX);
	            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table21.addCell(new Phrase(new Chunk(curr3,small_black_bold)));
	
	            Table21.getDefaultCell().setBorder(Rectangle.BOX);
	            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table21.addCell(new Phrase(new Chunk(es.formatAmount(gross_amt),small_black_bold)));
	            
	            sr+=1;
	            float[] ContactAddrWidths24 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table22 = new PdfPTable(ContactAddrWidths24);
	            
	            if (tax_name.contains(",")) 
				{
					BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_value));
					BigDecimal factor = new BigDecimal(2);
	            	double temp_srno=sr;
	            	for(int i = 0;i<tax_name.split(", ").length;i++) 
					{
	            		BigDecimal sub_tax_amt = tax_amt1.divide(factor, 0, RoundingMode.HALF_UP);
	            		temp_srno=temp_srno+0.1;
	            		Table22.setWidthPercentage(100);
	            		Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table22.addCell(new Phrase(new Chunk(nf0.format(temp_srno),small_black_normal)));
			
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            Table22.addCell(new Phrase(new Chunk(tax_name.split(",")[i],small_black_normal)));
			
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table22.addCell(new Phrase(new Chunk(curr3,small_black_normal)));
			
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            Table22.addCell(new Phrase(new Chunk(es.formatAmount(""+Double.parseDouble(""+sub_tax_amt)),small_black_normal)));
					}
				}

	            Table22.setWidthPercentage(100);
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table22.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table22.addCell(new Phrase(new Chunk("Tax ("+tax_name+")",small_black_bold)));
	
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table22.addCell(new Phrase(new Chunk(curr3,small_black_bold)));
	
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table22.addCell(new Phrase(new Chunk(es.formatAmount(""+taxValue),small_black_bold)));
	            
	            sr+=1;
	            float[] ContactAddrWidths25 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table23 = new PdfPTable(ContactAddrWidths25);
	            Table23.setWidthPercentage(100);
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table23.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table23.addCell(new Phrase(new Chunk("Total Amount in INR",small_black_bold)));
	
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table23.addCell(new Phrase(new Chunk(curr3,small_black_bold)));
	
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table23.addCell(new Phrase(new Chunk(es.formatAmount(net_amt),small_black_bold)));
	            
	            float[] ContactAddrWidths26_1 = {0.100F};
	   	     	PdfPTable Table24_1 = new PdfPTable(ContactAddrWidths26_1);
	   	     	Table24_1.setWidthPercentage(100);
	   	     	Table24_1.getDefaultCell().setBorder(Rectangle.BOX);
	   	     	Table24_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	   	     	Table24_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	   	     	Table24_1.addCell(new Phrase(new Chunk("Amount in Words | Rupees "+amt_in_word+" Only",black_bold)));
	   	  		
		     	float[] ContactAddrWidths26 = {0.100F};
	   	     	PdfPTable Table24 = new PdfPTable(ContactAddrWidths26);
	   	     	Table24.setWidthPercentage(100);
	   	     	Table24.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	   	     	Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	   	  		Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	   	  		Table24.addCell(new Phrase(new Chunk("For "+supp_nm,black_bold)));
	   	  		
	   	  		// create a signature form field
	   	     	PdfPTable BillingFieldsInfoTable81 = new PdfPTable(1);
	   	     	BillingFieldsInfoTable81.setWidthPercentage(20);
	   	     	BillingFieldsInfoTable81.setHorizontalAlignment(Element.ALIGN_LEFT);
	   	     	BillingFieldsInfoTable81.getDefaultCell().setBorder(Rectangle.BOX);
	   	     	BillingFieldsInfoTable81.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	   	     	BillingFieldsInfoTable81.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	        
	   	     	PdfFormField field = PdfFormField.createSignature(writer);
	   	     	field.setFieldName(SIGNAME);
	   	     	// set the widget properties
	   	     	field.setPage();
	   	     	field.setWidget(new Rectangle(72, 732, 144, 780), PdfAnnotation.HIGHLIGHT_NONE);
	   	     	field.setFlags(PdfAnnotation.FLAGS_PRINT);
	   	     	writer.addAnnotation(field);
	         
	   	     	PdfPCell sigCell = new PdfPCell();
	   	     	FieldPositioningEvents events = new FieldPositioningEvents(writer, field);
	   	     	sigCell.setCellEvent(events);
	   	     	sigCell.setBorder(Rectangle.NO_BORDER);
	   	     	sigCell.setFixedHeight(50f);
	   	     	BillingFieldsInfoTable81.addCell(sigCell);
	
	   	  		float[] ContactAddrWidths27 = {0.100F};
	   	  		PdfPTable Table25 = new PdfPTable(ContactAddrWidths27);
		     	Table25.setWidthPercentage(100);
		     	Table25.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table25.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table25.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table25.addCell(new Phrase(new Chunk("Authorised Signatory",black_bold)));

				document.add(mainTable);
				document.add(new Paragraph("  "));
				document.add(Table3);
				document.add(Table4);
				document.add(Table5);
				document.add(Table6);
				document.add(Table7);
				document.add(Table8);
				document.add(Table9);
				document.add(Table10);
				document.add(Table11);
				document.add(Table12);
				document.add(Table13);
				document.add(new Paragraph("  "));
				document.add(Table14);
				document.add(Table15);
				document.add(Table16);
				document.add(Table17);
				document.add(new Paragraph("  "));
				document.add(Table18);
				document.add(Table19);
				if (!currency.equals("1")) 
				{
					document.add(Table20);
				}
				document.add(Table21);
				document.add(Table22);
				document.add(Table23);
				document.add(new Paragraph("  "));
				document.add(Table24_1);
				document.add(new Paragraph("  "));
				document.add(Table24);
				document.add(new Paragraph("  "));
				document.add(BillingFieldsInfoTable81);
				document.add(Table25);
				
	            document.close();
	            
	            if(is_print.equals("1"))
				{
		            File isPDFexist = new File(path);
					if(isPDFexist.exists())
					{
						int update_flag=0;
						int pdf_entry=0;
						
						int cont=0;
						queryString="UPDATE FMS_OTH_INVOICE_MST SET PDF_INV_DTL=? ";
							if(print_pdf_type.equals("O"))
							{
								queryString+= ",PRINT_BY_ORI=?,PRINT_DT_ORI=SYSDATE ";
							}
							else if(print_pdf_type.equals("D"))
							{
								queryString+= ",PRINT_BY_DUP=?,PRINT_DT_DUP=SYSDATE ";
							}
							queryString+= "WHERE COMPANY_CD=? AND INVOICE_NO=? AND INVOICE_TYPE=? AND INVOICE_SEQ = ? AND SUPPLIER_CD = ?";
							
						stmt=conn.prepareStatement(queryString);
						stmt.setString(++cont, print_pdf_type);
						if(print_pdf_type.equals("O"))
						{
							stmt.setString(++cont, emp_cd);
						}
						else if(print_pdf_type.equals("D"))
						{
							stmt.setString(++cont, emp_cd);
						}
						stmt.setString(++cont, comp_cd);
						stmt.setString(++cont, inv_no);
						stmt.setString(++cont, inv_type);
						stmt.setString(++cont, inv_seq);
						stmt.setString(++cont, supp_cd);
						update_flag=stmt.executeUpdate();
						stmt.close();
						
						int count=0;
				        queryString1="SELECT COUNT(*) "
				        		+ "FROM FMS_OTH_INV_FILE_DTL "
				        		+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? "
				        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND SUPPLIER_CD =?";
				        stmt1=conn.prepareStatement(queryString1);
				        stmt1.setString(1, comp_cd);
				        stmt1.setString(2, inv_type);
				        stmt1.setString(3, inv_seq);
				        stmt1.setString(4, fin_yr);
				        stmt1.setString(5, print_pdf_type);
				        stmt1.setString(6, supp_cd);
				        rset1=stmt1.executeQuery();
				        if(rset1.next())
				        {
				        	count=rset1.getInt(1);
				        }
				        rset1.close();
				        stmt1.close();
				        
				        if(count > 0)
				        {
				        	queryString1="UPDATE FMS_OTH_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
				        			+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? "
					        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND SUPPLIER_CD =?";
				        	stmt1=conn.prepareStatement(queryString1);
				        	stmt1.setString(1, file_nm);
				        	stmt1.setString(2, emp_cd);
				        	stmt1.setString(3, comp_cd);
				 	        stmt1.setString(4, inv_type);
				 	        stmt1.setString(5, inv_seq);
				 	        stmt1.setString(6, fin_yr);
				 	        stmt1.setString(7, print_pdf_type); 
				 	        stmt1.setString(8, supp_cd); 
				 	        pdf_entry=stmt1.executeUpdate();
				        	
				        	stmt1.close();
				        }
				        else
				        {
				        	queryString1="INSERT INTO FMS_OTH_INV_FILE_DTL(COMPANY_CD,SUPPLIER_CD,INVOICE_SEQ,INVOICE_TYPE,FINANCIAL_YEAR,PDF_TYPE,"
				        			+ "FILE_NAME,ENT_BY,ENT_DT) "
				        			+ "VALUES(?,?,?,?,?,?,"
				        			+ "?,?,SYSDATE)";
				        	stmt1=conn.prepareStatement(queryString1);
				        	stmt1.setString(1, comp_cd);
				        	stmt1.setString(2, supp_cd);
				 	        stmt1.setString(3, inv_seq);
				 	        stmt1.setString(4, inv_type);
				 	        stmt1.setString(5, fin_yr);
				 	        stmt1.setString(6, print_pdf_type);
				 	        stmt1.setString(7, file_nm);
				        	stmt1.setString(8, emp_cd);
				        	pdf_entry=stmt1.executeUpdate();
				        	
				        	stmt1.close();
				        }
				        
				        if(pdf_entry == 0 || update_flag == 0)
				        {
				        	conn.rollback();
				        	deletePdfFile(path);
				        	msg = "Failed! - PDF "+file_nm+" for RCM Invoice(Cost Recharge) with Invoice No. "+inv_no+" Generation Failed!";
				        	msg_type="E";
				        }
				        else
				        {
				        	conn.commit();
				        	msg = "Successful! - PDF "+file_nm+" for RCM Invoice(Cost Recharge) with Invoice No. "+inv_no+" Generated Successfully!";
				        	msg_type="S";
				        }
					}
					else
					{
						msg="Failed! - PDF for RCM Invoice(Cost Recharge) with Invoice No. "+inv_no+" Generation Failed!";
						msg_type="E";
					}
				}
			}
		}
		catch(Exception e)
		{
			conn.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			
			document.close();
			deletePdfFile(path);
			
			//msg="Error in Exception! - "+invdtl+" Invoice for "+counterparty_abbr+" PDF Generation Failed!";
			msg = "Error in Exception! - PDF for RCM Invoice(Cost Recharge) with Invoice No. "+inv_no+" Generation Failed!";
			msg_type="E";	
		}
		finally
		{
			document.close();
		}
		
		if(is_print.equals("1"))
		{
			try
			{
				new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, "", "", msg);  	
			}
			catch(Exception infoLogger)
			{
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
			}
		}
	}
	
	//AP20251017
	public void pdfcostrechargeHPPL() throws SQLException
	{
		String function_nm="pdfcostrechargeHPPL()";
		
		String emp_nm ="";
		String ip = "";
		msg="";
		//String msg_content="";
		String path ="";
		
		Rectangle pageSize = new Rectangle(595, 842);
		
		pageSize.setBackgroundColor(BaseColor.WHITE);
		
		Document document = new Document(pageSize);
		try
		{
			HttpSession session = request.getSession();
			emp_nm = (String)session.getAttribute("emp_nm")==null?"":(String)session.getAttribute("emp_nm");
			ip = (String)session.getAttribute("ip")==null?"":(String)session.getAttribute("ip");
			
			String company_nm = ""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");
			String company_abbr = ""+session.getAttribute("comp_abbr")==null?"":""+session.getAttribute("comp_abbr");
			
			int pdfGenCount=0;
			
			if(is_print.equals("1"))
	        {
				queryString = "SELECT COUNT(*) "
							+ "FROM FMS_OTH_INVOICE_MST A ,FMS_OTH_INV_FILE_DTL B "
							+ "WHERE A.COMPANY_CD=? AND A.INVOICE_NO = ? AND A.INVOICE_TYPE = ? ";
							if(print_pdf_type.equals("O"))
							{
								queryString+= "AND A.PRINT_BY_ORI IS NOT NULL AND A.PRINT_DT_ORI IS NOT NULL ";
							}
							else if(print_pdf_type.equals("T"))
							{
								queryString+= "AND A.PRINT_BY_TRI IS NOT NULL AND A.PRINT_DT_TRI IS NOT NULL ";
							}
							else if(print_pdf_type.equals("D"))
							{
								queryString+= "AND A.PRINT_BY_DUP IS NOT NULL AND A.PRINT_DT_DUP IS NOT NULL ";
							}
				queryString += "AND B.PDF_TYPE=? AND B.INVOICE_TYPE = ? AND B.FILE_NAME IS NOT NULL "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.INVOICE_TYPE = B.INVOICE_TYPE AND A.SUPPLIER_CD = B.SUPPLIER_CD";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_no);
				stmt.setString(3, inv_type);
				stmt.setString(4, print_pdf_type);
				stmt.setString(5, inv_type);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					pdfGenCount=rset.getInt(1);
				}
				rset.close();
				stmt.close();
	        }
			else
			{
				pdfGenCount=0;
			}
	
			if(pdfGenCount>0)
			{
				msg="Failed! - PDF for Cost Recharge HPPL with Invoice No. "+inv_no+" already Generated!";
	        	msg_type="E";
	        }
			else
			{
				
				getInvoiceDtlHPPL();
				getSupplierDtl();
				getVendorDtl();
				
				String irn_no="";
				String qr_code="";
				queryString1="SELECT IRN_NO,SIGN_QR_CODE "
						+ "FROM FMS_OTH_INV_IRN_DTL "
						+ "WHERE COMPANY_CD=? AND INVOICE_NO=? ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, inv_no);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					irn_no=rset1.getString(1)==null?"":rset1.getString(1);
					qr_code=rset1.getString(2)==null?"":rset1.getString(2);
				}
				rset1.close();
				stmt1.close();
				
				String print_pdf_type_nm="ORIGINAL";
				if(print_pdf_type.equals("O"))
				{
					print_pdf_type_nm="ORIGINAL";
				}
				else if(print_pdf_type.equals("D"))
				{
					print_pdf_type_nm="DUPLICATE";
				}
	
				String appPath = request.getServletContext().getRealPath("");
	
				String main_folder="";
				if(!comp_cd.equals(""))
				{
					main_folder=CommonVariable.work_dir+comp_cd;
				}
				
				String sub_folder="unsigned_pdf";
				String sub_folder2="ig_invoice";
				
				String temp_path=appPath+File.separator+main_folder+File.separator+sub_folder+File.separator+sub_folder2;
				File main_folderDir = new File(temp_path);
		        if(!main_folderDir.exists())
		        {
		        	main_folderDir.mkdirs();
		        }

		        if(is_print.equals("1"))
		        {
		        	file_nm=print_pdf_type+"-"+supp_abbr+"-"+inv_type+"-"+inv_no.replace("/", "-")+".pdf";
		        }
		        else
		        {
		        	file_nm="temp.pdf";
		        }
		        
	
				path = appPath+"/"+main_folder+"/"+sub_folder+"/"+sub_folder2+"/"+file_nm;
				PdfWriter writer = PdfWriter.getInstance(document,new FileOutputStream(path));
	
				document.open();
				document.setPageSize(pageSize);
	            document.newPage();
	
	            String context_nm = request.getContextPath();
				String server_nm = request.getServerName();
				String server_port = ""+request.getServerPort();
	
				file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm;
	
				Font small_black_normal = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
				Font small_black_normal_10 = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
				Font small_black_normal_14 = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.NORMAL, BaseColor.BLACK);
				Font small_black_normal_12 = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
				Font small_black_bold = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
	            Font black_bold = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
	            Font black_bold1 = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
	            
	            PdfPCell company_logo_cell = new PdfPCell();
	            if(!comp_logo.equals(""))
	            {
					String file_path = request.getRealPath("/"+CommonVariable.company_logo_path+"/"+comp_logo);
	            	Image company_logo = Image.getInstance(file_path);
		            company_logo.setBorder(Rectangle.NO_BORDER);
		            company_logo.scaleAbsolute(44,40);
		            company_logo_cell = new PdfPCell(company_logo,false);
		            company_logo_cell.setBorder(Rectangle.NO_BORDER);
		            company_logo_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            }
	            
	            float[] mainWidths = {10f, 70f, 15f};  
	            PdfPTable mainTable = new PdfPTable(mainWidths);
	            mainTable.setWidthPercentage(100);
	            mainTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

	            PdfPCell logoCell = new PdfPCell(company_logo_cell);
	            logoCell.setBorder(Rectangle.NO_BORDER);
	            logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            logoCell.setPaddingLeft(10);
	            mainTable.addCell(logoCell);
	            
	            PdfPTable centerTable = new PdfPTable(1);
	            centerTable.setWidthPercentage(100);
	            centerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            centerTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            centerTable.addCell(new Phrase(new Chunk(print_pdf_type_nm, small_black_normal_10)));
	            centerTable.addCell(new Phrase(new Chunk(supp_nm, black_bold1)));
	            centerTable.addCell(new Phrase(new Chunk("TAX INVOICE", small_black_normal_12)));
	            String info = "Tax invoice issued under Rule 46 of the Central Goods and Services Tax Rules, 2017";
	            centerTable.addCell(new Phrase(new Chunk(info, small_black_normal)));

	            PdfPCell centerCell = new PdfPCell(centerTable);
	            centerCell.setBorder(Rectangle.NO_BORDER);
	            centerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            mainTable.addCell(centerCell);

	            PdfPCell emptyCell = new PdfPCell(new Phrase(""));
	            emptyCell.setBorder(Rectangle.NO_BORDER);
	            mainTable.addCell(emptyCell);
	
	            float[] ContactAddrWidths5 = {0.80f,0.10F,0.20F,0.80F};
	   	     	PdfPTable Table3 = new PdfPTable(ContactAddrWidths5);
	            Table3.setWidthPercentage(100);
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("Registered Office: ",small_black_bold)));
	            
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("To:",small_black_bold)));
	
				float[] ContactAddrWidths6 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table4 = new PdfPTable(ContactAddrWidths6);
	            Table4.setWidthPercentage(100);
	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk(supp_nm,small_black_bold)));
	            
	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk(""+vendor_name+",",small_black_bold)));
	
	            float[] ContactAddrWidths7 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table5 = new PdfPTable(ContactAddrWidths7);
	            Table5.setWidthPercentage(100);
	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk(supp_addr+" "+supp_city,small_black_normal)));
	            
	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk(vend_abbr+" "+vend_city,small_black_normal)));
	            
	
	            float[] ContactAddrWidths8 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table6 = new PdfPTable(ContactAddrWidths8);
	            Table6.setWidthPercentage(100);
	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table6.addCell(new Phrase(new Chunk(supp_state+"-"+supp_pin,small_black_normal)));
	
	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table6.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_pin_no.equals("")) 
	            {
	            	Table6.addCell(new Phrase(new Chunk(vend_state + "-" + vend_pin_no, small_black_normal)));
				}
	            else 
	            {
	            	Table6.addCell(new Phrase(new Chunk(vend_state, small_black_normal)));
	            }
	            
	           	float[] ContactAddrWidths9 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table7 = new PdfPTable(ContactAddrWidths9);
	            Table7.setWidthPercentage(100);
	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk("State: "+supp_state,small_black_normal)));
	
	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk("State: "+vend_state,small_black_normal)));
	            
	            float[] ContactAddrWidths10 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table8 = new PdfPTable(ContactAddrWidths10);
	            Table8.setWidthPercentage(100);
	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table8.addCell(new Phrase(new Chunk("State Code: "+supp_state_tin,small_black_normal)));
	
	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table8.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_state_tin.equals("")) 
	            {
					Table8.addCell(new Phrase(new Chunk("State Code: " + vend_state_tin, small_black_normal)));
				}
	            else 
	            {
					Table8.addCell(new Phrase(new Chunk("", small_black_normal)));
	            }
	            
	            float[] ContactAddrWidths11 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table9 = new PdfPTable(ContactAddrWidths11);
	            Table9.setWidthPercentage(100);
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table9.addCell(new Phrase(new Chunk("GSTIN: "+supp_gstin_no,small_black_normal)));
	
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table9.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_gstin_no.equals("")) 
	            {
					Table9.addCell(new Phrase(new Chunk("GSTIN: " + vend_gstin_no, small_black_normal)));
				}
	            else 
	            {
	            	Table9.addCell(new Phrase(new Chunk("", small_black_normal)));
	            }

	            float[] ContactAddrWidths12 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table10 = new PdfPTable(ContactAddrWidths12);
	            Table10.setWidthPercentage(100);
	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table10.addCell(new Phrase(new Chunk("PAN: "+supp_pan_no,small_black_normal)));
	
	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table10.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_pan_no.equals("")) 
	            {
					Table10.addCell(new Phrase(new Chunk("PAN: " + vend_pan_no, small_black_normal)));
				}
	            else 
	            {
	            	Table10.addCell(new Phrase(new Chunk("", small_black_normal)));
	            }
	            
				float[] ContactAddrWidths13 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table11 = new PdfPTable(ContactAddrWidths13);
	            Table11.setWidthPercentage(100);
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("SAC: "+sac_code,small_black_normal)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            float[] ContactAddrWidths14 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table12 = new PdfPTable(ContactAddrWidths14);
	            Table12.setWidthPercentage(100);
	            Table12.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table12.addCell(new Phrase(new Chunk("Description of Service: "+sac_des,small_black_normal)));
	
	            Table12.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table12.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table12.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table12.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            float[] ContactAddrWidths15 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table13 = new PdfPTable(ContactAddrWidths15);
	            Table13.setWidthPercentage(100);
	            Table13.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table13.addCell(new Phrase(new Chunk("Place of Supply: "+supp_state,small_black_normal)));
	
	            Table13.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table13.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table13.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table13.addCell(new Phrase(new Chunk("",small_black_normal)));
            
	            float[] ContactAddrWidths16 = {0.80F,0.20F,0.50F,0.40F};
	   	     	PdfPTable Table14 = new PdfPTable(ContactAddrWidths16);
	            Table14.setWidthPercentage(100);
	            Table14.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table14.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table14.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table14.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table14.addCell(new Phrase(new Chunk("Invoice No :",black_bold)));
	
	            Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table14.addCell(new Phrase(new Chunk(inv_no,black_bold)));
	            
	            float[] ContactAddrWidths17 = {0.80F,0.20F,0.50F,0.40F};
	   	     	PdfPTable Table15 = new PdfPTable(ContactAddrWidths17);
	            Table15.setWidthPercentage(100);
	            Table15.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table15.addCell(new Phrase(new Chunk("Invoice Date :",black_bold)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk(inv_dt,black_bold)));

	            float[] ContactAddrWidths18 = {0.80F,0.20F,0.50F,0.40F};
	   	     	PdfPTable Table16 = new PdfPTable(ContactAddrWidths18);
	            Table16.setWidthPercentage(100);
	            Table16.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table16.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table16.addCell(new Phrase(new Chunk("Contract/Purchase Order No:",black_bold)));
	
	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk(purchase_no,black_bold)));
	            
	            float[] ContactAddrWidths19 = {0.80F,0.20F,0.50F,0.40F};
	   	     	PdfPTable Table17 = new PdfPTable(ContactAddrWidths19);
	            Table17.setWidthPercentage(100);
	            Table17.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table17.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table17.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table17.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table17.addCell(new Phrase(new Chunk("Reference No.:",black_bold)));
	
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table17.addCell(new Phrase(new Chunk(ref_no,black_bold)));
	                      
	            float[] ContactAddrWidths20 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table18 = new PdfPTable(ContactAddrWidths20);
	            Table18.setWidthPercentage(100);
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk("Sr.No.",small_black_bold)));
	
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk("Description",small_black_bold)));
	
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk("Currency",small_black_bold)));
	
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk("Amount",small_black_bold)));

	            
	            String curr1 = "",curr2="",curr3="";
	            if(currency.equals("1")) 
	            {
	            	curr1 = "INR";
	            	curr2 = "INR";
	            	curr3 = "INR";
	            }
	            
	            int sr=1;
	            float[] ContactAddrWidths21 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table19 = new PdfPTable(ContactAddrWidths21);
	            Table19.setWidthPercentage(100);
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table19.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table19.addCell(new Phrase(new Chunk(desc_item,small_black_normal)));
	
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table19.addCell(new Phrase(new Chunk(curr1,small_black_normal)));
	
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table19.addCell(new Phrase(new Chunk(es.formatAmount(sale_amt),small_black_normal)));
	            
	            sr+=1;
	            float[] ContactAddrWidths23 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table21 = new PdfPTable(ContactAddrWidths23);
	            Table21.setWidthPercentage(100);
	            Table21.getDefaultCell().setBorder(Rectangle.BOX);
	            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table21.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table21.getDefaultCell().setBorder(Rectangle.BOX);
	            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table21.addCell(new Phrase(new Chunk("Total Amount ",small_black_bold)));
	
	            Table21.getDefaultCell().setBorder(Rectangle.BOX);
	            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table21.addCell(new Phrase(new Chunk(curr3,small_black_bold)));
	
	            Table21.getDefaultCell().setBorder(Rectangle.BOX);
	            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table21.addCell(new Phrase(new Chunk(es.formatAmount(gross_amt),small_black_bold)));
	            
	            sr+=1;
	            float[] ContactAddrWidths24 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table22 = new PdfPTable(ContactAddrWidths24);
	            
	            if (tax_name.contains(",")) 
				{
	            	BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_value));
					BigDecimal factor = new BigDecimal(2);
	            	double temp_srno=sr;
	            	for(int i = 0;i<tax_name.split(", ").length;i++) 
					{
	            		BigDecimal sub_tax_amt = tax_amt1.divide(factor, 0, RoundingMode.HALF_UP);
	            		temp_srno=temp_srno+0.1;
	            		Table22.setWidthPercentage(100);
	            		Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table22.addCell(new Phrase(new Chunk(nf0.format(temp_srno),small_black_normal)));
			
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            Table22.addCell(new Phrase(new Chunk(tax_name.split(",")[i],small_black_normal)));
			
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table22.addCell(new Phrase(new Chunk(curr3,small_black_normal)));
			
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            Table22.addCell(new Phrase(new Chunk(es.formatAmount(""+Double.parseDouble(""+sub_tax_amt)),small_black_normal)));
					}
				}

	            Table22.setWidthPercentage(100);
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table22.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table22.addCell(new Phrase(new Chunk("Tax ("+tax_name+")",small_black_bold)));
	
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table22.addCell(new Phrase(new Chunk(curr3,small_black_bold)));
	
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table22.addCell(new Phrase(new Chunk(es.formatAmount(tax_value),small_black_bold)));
	            
	            
	            sr+=1;
	            float[] ContactAddrWidths25 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table23 = new PdfPTable(ContactAddrWidths25);
	            Table23.setWidthPercentage(100);
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table23.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table23.addCell(new Phrase(new Chunk("Total Amount in INR",small_black_bold)));
	
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table23.addCell(new Phrase(new Chunk(curr3,small_black_bold)));
	
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table23.addCell(new Phrase(new Chunk(es.formatAmount(net_amt),small_black_bold)));
	            
	            float[] ContactAddrWidths26_1 = {0.100F};
	   	     	PdfPTable Table24_1 = new PdfPTable(ContactAddrWidths26_1);
	   	     	Table24_1.setWidthPercentage(100);
	   	     	Table24_1.getDefaultCell().setBorder(Rectangle.BOX);
	   	     	Table24_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	   	     	Table24_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	   	     	Table24_1.addCell(new Phrase(new Chunk("Amount in Words | Rupees "+amt_in_word+" Only",black_bold)));		     	
	   	     	
	   	     	float[] ContactAddrWidths26 = {0.100F};
	   	     	PdfPTable Table24 = new PdfPTable(ContactAddrWidths26);
	   	     	Table24.setWidthPercentage(100);
	   	     	Table24.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	   	     	Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	   	  		Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	   	  		Table24.addCell(new Phrase(new Chunk("For "+supp_nm,black_bold)));
	
	   	  		float[] ContactAddrWidths27 = {0.100F};
	   	  		PdfPTable Table25 = new PdfPTable(ContactAddrWidths27);
		     	Table25.setWidthPercentage(100);
		     	Table25.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table25.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table25.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table25.addCell(new Phrase(new Chunk("Authorised Signatory",black_bold)));
		     	
			     PdfPTable BillingFieldsInfoTable81 = new PdfPTable(1);
			     BillingFieldsInfoTable81.setWidthPercentage(20);
			     BillingFieldsInfoTable81.setHorizontalAlignment(Element.ALIGN_LEFT);
			     BillingFieldsInfoTable81.getDefaultCell().setBorder(Rectangle.BOX);
			     BillingFieldsInfoTable81.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			     BillingFieldsInfoTable81.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	
			     PdfFormField field = PdfFormField.createSignature(writer);
			     field.setFieldName(SIGNAME);
	
			     field.setPage();
			     field.setWidget(new Rectangle(72, 732, 144, 780), PdfAnnotation.HIGHLIGHT_NONE);
			     field.setFlags(PdfAnnotation.FLAGS_PRINT);
			     writer.addAnnotation(field);
	
			     PdfPCell sigCell = new PdfPCell();
			     FieldPositioningEvents events = new FieldPositioningEvents(writer, field);
			     sigCell.setCellEvent(events);
			     sigCell.setBorder(Rectangle.NO_BORDER);
			     sigCell.setFixedHeight(50f);
			     BillingFieldsInfoTable81.addCell(sigCell);
			    
			     PdfPTable leftSignTable = new PdfPTable(1);
			     leftSignTable.setWidthPercentage(100);
			     leftSignTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			     leftSignTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	
			     leftSignTable.addCell(Table24);
			     leftSignTable.addCell(BillingFieldsInfoTable81);
			     leftSignTable.addCell(Table25);
	
			     float[] InvoiceDateInfoWidths_qr = {0.30f, 0.20f, 0.40f};
			     PdfPTable InvoiceDateInfoTable_qr = new PdfPTable(InvoiceDateInfoWidths_qr);
	
			     if (!irn_no.equals("") && !qr_code.equals("")) {
	
			         int width = 90;
			         int height = 90;
			         Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
			         hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
	
			         QRCodeWriter qrCodeWriter = new QRCodeWriter();
			         BitMatrix matrix = qrCodeWriter.encode(qr_code, BarcodeFormat.QR_CODE, width, height, hintMap);
	
			         BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	
			         for (int x = 0; x < width; x++) {
			             for (int y = 0; y < height; y++) {
			                 image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
			             }
			         }
	
			         ByteArrayOutputStream baos = new ByteArrayOutputStream();
			         ImageIO.write(image, "png", baos);
			         Image qr_codeimg = Image.getInstance(baos.toByteArray());
			         qr_codeimg.setBorder(Rectangle.NO_BORDER);
			         qr_codeimg.setAlignment(Element.ALIGN_LEFT);
	
			         PdfPCell qrcode_cell = new PdfPCell(qr_codeimg, false);
			         qrcode_cell.setBorder(Rectangle.NO_BORDER);
			         qrcode_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	
			         String char_16 = irn_no.substring(0, 16);
			         String char_32 = irn_no.substring(16, 32);
			         String char_48 = irn_no.substring(32, 48);
			         String char_64 = irn_no.substring(48);
			         String final_irn_no = char_16 + "\n" + char_32 + "\n" + char_48 + "\n" + char_64;
	
			         PdfPTable InvoiceDateInfoTable_qr1 = new PdfPTable(1);
			         InvoiceDateInfoTable_qr1.setWidthPercentage(100);
			         InvoiceDateInfoTable_qr1.getDefaultCell().setBorder(Rectangle.BOX);
			         InvoiceDateInfoTable_qr1.addCell(qrcode_cell);
	
			         PdfPTable InvoiceDateInfoTable_qr11 = new PdfPTable(1);
			         InvoiceDateInfoTable_qr11.setWidthPercentage(100);
			         InvoiceDateInfoTable_qr11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			         InvoiceDateInfoTable_qr11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			         InvoiceDateInfoTable_qr11.addCell(new Phrase(new Chunk("IRN", black_bold)));
			         InvoiceDateInfoTable_qr11.addCell(new Phrase(new Chunk(final_irn_no, small_black_normal)));
	
			         InvoiceDateInfoTable_qr.setWidthPercentage(100);
			         InvoiceDateInfoTable_qr.getDefaultCell().setBorder(Rectangle.BOX);
			         InvoiceDateInfoTable_qr.addCell(InvoiceDateInfoTable_qr11);
	
			         InvoiceDateInfoTable_qr.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			         InvoiceDateInfoTable_qr.addCell("");
	
			         InvoiceDateInfoTable_qr.addCell(InvoiceDateInfoTable_qr1);
			     }
	
			     PdfPTable finalRow = new PdfPTable(3);
			     finalRow.setWidthPercentage(100);
			     finalRow.setWidths(new float[]{0.3f, 0.2f ,0.5f});
			     finalRow.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	
			     finalRow.addCell(leftSignTable);
			     finalRow.addCell("");
			     finalRow.addCell(InvoiceDateInfoTable_qr);

				document.add(mainTable);
				document.add(new Paragraph("  "));
				document.add(Table3);
				document.add(Table4);
				document.add(Table5);
				document.add(Table6);
				document.add(Table7);
				document.add(Table8);
				document.add(Table9);
				document.add(Table10);
				document.add(Table11);
				document.add(Table12);
				document.add(Table13);
				document.add(new Paragraph("  "));
				document.add(Table14);
				document.add(Table15);
				document.add(Table16);
				document.add(Table17);
				document.add(new Paragraph("  "));
				document.add(Table18);
				document.add(Table19);
				document.add(Table21);
				document.add(Table22);
				document.add(Table23);
				document.add(new Paragraph("  "));
				document.add(Table24_1);
				document.add(new Paragraph("  "));
				document.add(finalRow);
				
	            document.close();
	            if(is_print.equals("1"))
				{
		            File isPDFexist = new File(path);
					if(isPDFexist.exists())
					{
						int update_flag=0;
						int pdf_entry=0;
						
						int cont=0;
						queryString="UPDATE FMS_OTH_INVOICE_MST SET PDF_INV_DTL=? ";
							if(print_pdf_type.equals("O"))
							{
								queryString+= ",PRINT_BY_ORI=?,PRINT_DT_ORI=SYSDATE ";
							}
							else if(print_pdf_type.equals("D"))
							{
								queryString+= ",PRINT_BY_DUP=?,PRINT_DT_DUP=SYSDATE ";
							}
							queryString+= "WHERE COMPANY_CD=? AND INVOICE_NO=? AND INVOICE_TYPE=? AND INVOICE_SEQ = ? AND SUPPLIER_CD = ?";
							
						stmt=conn.prepareStatement(queryString);
						stmt.setString(++cont, print_pdf_type);
						if(print_pdf_type.equals("O"))
						{
							stmt.setString(++cont, emp_cd);
						}
						else if(print_pdf_type.equals("D"))
						{
							stmt.setString(++cont, emp_cd);
						}
						stmt.setString(++cont, comp_cd);
						stmt.setString(++cont, inv_no);
						stmt.setString(++cont, inv_type);
						stmt.setString(++cont, inv_seq);
						stmt.setString(++cont, supp_cd);
						update_flag=stmt.executeUpdate();
						stmt.close();
						
						int count=0;
				        queryString1="SELECT COUNT(*) "
				        		+ "FROM FMS_OTH_INV_FILE_DTL "
				        		+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? "
				        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND SUPPLIER_CD =?";
				        stmt1=conn.prepareStatement(queryString1);
				        stmt1.setString(1, comp_cd);
				        stmt1.setString(2, inv_type);
				        stmt1.setString(3, inv_seq);
				        stmt1.setString(4, fin_yr);
				        stmt1.setString(5, print_pdf_type);
				        stmt1.setString(6, supp_cd);
				        rset1=stmt1.executeQuery();
				        if(rset1.next())
				        {
				        	count=rset1.getInt(1);
				        }
				        rset1.close();
				        stmt1.close();
				        
				        if(count > 0)
				        {
				        	queryString1="UPDATE FMS_OTH_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
				        			+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? "
					        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND SUPPLIER_CD =?";
				        	stmt1=conn.prepareStatement(queryString1);
				        	stmt1.setString(1, file_nm);
				        	stmt1.setString(2, emp_cd);
				        	stmt1.setString(3, comp_cd);
				 	        stmt1.setString(4, inv_type);
				 	        stmt1.setString(5, inv_seq);
				 	        stmt1.setString(6, fin_yr);
				 	        stmt1.setString(7, print_pdf_type); 
				 	        stmt1.setString(8, supp_cd); 
				 	        pdf_entry=stmt1.executeUpdate();
				        	
				        	stmt1.close();
				        }
				        else
				        {
				        	queryString1="INSERT INTO FMS_OTH_INV_FILE_DTL(COMPANY_CD,SUPPLIER_CD,INVOICE_SEQ,INVOICE_TYPE,FINANCIAL_YEAR,PDF_TYPE,"
				        			+ "FILE_NAME,ENT_BY,ENT_DT) "
				        			+ "VALUES(?,?,?,?,?,?,"
				        			+ "?,?,SYSDATE)";
				        	stmt1=conn.prepareStatement(queryString1);
				        	stmt1.setString(1, comp_cd);
				        	stmt1.setString(2, supp_cd);
				 	        stmt1.setString(3, inv_seq);
				 	        stmt1.setString(4, inv_type);
				 	        stmt1.setString(5, fin_yr);
				 	        stmt1.setString(6, print_pdf_type);
				 	        stmt1.setString(7, file_nm);
				        	stmt1.setString(8, emp_cd);
				        	pdf_entry=stmt1.executeUpdate();
				        	
				        	stmt1.close();
				        }
				        
				        if(pdf_entry == 0 || update_flag == 0)
				        {
				        	conn.rollback();
				        	deletePdfFile(path);
				        	msg = "Failed! - PDF "+file_nm+" for Cost Recharge HPPL with Invoice No. "+inv_no+" Generation Failed!";
				        	msg_type="E";
				        }
				        else
				        {
				        	conn.commit();
				        	msg = "Successful! - PDF "+file_nm+" for Cost Recharge HPPL with Invoice No. "+inv_no+" Generated Successfully!";
				        	msg_type="S";
				        }
					}
					else
					{
						msg="Failed! - PDF for Cost Recharge HPPL with Invoice No. "+inv_no+" Generation Failed!";
						msg_type="E";
					}
				}
			}
		}
		catch(Exception e)
		{
			conn.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			
			document.close();
			deletePdfFile(path);
			
			msg = "Error in Exception! - PDF for Cost Recharge HPPL with Invoice No. "+inv_no+" Generation Failed!";
			msg_type="E";	
		}
		finally
		{
			document.close();
		}
		
		if(is_print.equals("1"))
		{
			try
			{
				new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, "", "", msg);  	
			}
			catch(Exception infoLogger)
			{
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
			}
		}
	}
	
	//AP20251106
	public void pdfcostrechargeHPPLShippingAgent() throws SQLException
	{
		String function_nm="pdfcostrechargeHPPLShippingAgent()";
		
		String emp_nm ="";
		String ip = "";
		msg="";
		//String msg_content="";
		String path ="";
		
		Rectangle pageSize = new Rectangle(595, 842);
		
		pageSize.setBackgroundColor(BaseColor.WHITE);
		
		Document document = new Document(pageSize);
		try
		{
			HttpSession session = request.getSession();
			emp_nm = (String)session.getAttribute("emp_nm")==null?"":(String)session.getAttribute("emp_nm");
			ip = (String)session.getAttribute("ip")==null?"":(String)session.getAttribute("ip");
			
			String company_nm = ""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");
			String company_abbr = ""+session.getAttribute("comp_abbr")==null?"":""+session.getAttribute("comp_abbr");
			
			int pdfGenCount=0;
			
			if(is_print.equals("1"))
	        {
				queryString = "SELECT COUNT(*) "
							+ "FROM FMS_OTH_INVOICE_MST A ,FMS_OTH_INV_FILE_DTL B "
							+ "WHERE A.COMPANY_CD=? AND A.INVOICE_NO = ? AND A.INVOICE_TYPE = ? ";
							if(print_pdf_type.equals("O"))
							{
								queryString+= "AND A.PRINT_BY_ORI IS NOT NULL AND A.PRINT_DT_ORI IS NOT NULL ";
							}
							else if(print_pdf_type.equals("T"))
							{
								queryString+= "AND A.PRINT_BY_TRI IS NOT NULL AND A.PRINT_DT_TRI IS NOT NULL ";
							}
							else if(print_pdf_type.equals("D"))
							{
								queryString+= "AND A.PRINT_BY_DUP IS NOT NULL AND A.PRINT_DT_DUP IS NOT NULL ";
							}
				queryString += "AND B.PDF_TYPE=? AND B.INVOICE_TYPE = ? AND B.FILE_NAME IS NOT NULL "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.INVOICE_TYPE = B.INVOICE_TYPE AND A.SUPPLIER_CD = B.SUPPLIER_CD";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_no);
				stmt.setString(3, inv_type);
				stmt.setString(4, print_pdf_type);
				stmt.setString(5, inv_type);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					pdfGenCount=rset.getInt(1);
				}
				rset.close();
				stmt.close();
	        }
			else
			{
				pdfGenCount=0;
			}
	
			if(pdfGenCount>0)
			{
				msg="Failed! - PDF for HPPL Shipping Agent with Invoice No. "+inv_no+" already Generated!";
	        	msg_type="E";
	        }
			else
			{
				
				getInvoiceDtlHPPLShippingAgent();
				getSupplierDtl();
				getVendorDtl();
				
				String irn_no="";
				String qr_code="";
				
				queryString1="SELECT IRN_NO,SIGN_QR_CODE "
						+ "FROM FMS_OTH_INV_IRN_DTL "
						+ "WHERE COMPANY_CD=? AND INVOICE_NO=? ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, inv_no);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					irn_no=rset1.getString(1)==null?"":rset1.getString(1);
					qr_code=rset1.getString(2)==null?"":rset1.getString(2);
				}
				rset1.close();
				stmt1.close();
				
				String print_pdf_type_nm="ORIGINAL";
				if(print_pdf_type.equals("O"))
				{
					print_pdf_type_nm="ORIGINAL";
				}
				else if(print_pdf_type.equals("D"))
				{
					print_pdf_type_nm="DUPLICATE";
				}
	
				String appPath = request.getServletContext().getRealPath("");
	
				String main_folder="";
				if(!comp_cd.equals(""))
				{
					main_folder=CommonVariable.work_dir+comp_cd;
				}
				
				String sub_folder="unsigned_pdf";
				String sub_folder2="ig_invoice";
				
				String temp_path=appPath+File.separator+main_folder+File.separator+sub_folder+File.separator+sub_folder2;
				File main_folderDir = new File(temp_path);
		        if(!main_folderDir.exists())
		        {
		        	main_folderDir.mkdirs();
		        }

		        if(is_print.equals("1"))
		        {
		        	file_nm=print_pdf_type+"-"+supp_abbr+"-"+inv_type+"-"+inv_no.replace("/", "-")+".pdf";
		        }
		        else
		        {
		        	file_nm="temp.pdf";
		        }
		        
	
				path = appPath+"/"+main_folder+"/"+sub_folder+"/"+sub_folder2+"/"+file_nm;
				PdfWriter writer = PdfWriter.getInstance(document,new FileOutputStream(path));
	
				document.open();
				document.setPageSize(pageSize);
	            document.newPage();
	
	            String context_nm = request.getContextPath();
				String server_nm = request.getServerName();
				String server_port = ""+request.getServerPort();
	
				file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm;
	
				Font small_black_normal = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
				Font small_black_normal_10 = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
				Font small_black_normal_14 = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.NORMAL, BaseColor.BLACK);
				Font small_black_normal_12 = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
				Font small_black_bold = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
	            Font black_bold = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
	            Font black_bold1 = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
	            
	            PdfPCell company_logo_cell = new PdfPCell();
	            if(!comp_logo.equals(""))
	            {
					String file_path = request.getRealPath("/"+CommonVariable.company_logo_path+"/"+comp_logo);
	            	Image company_logo = Image.getInstance(file_path);
		            company_logo.setBorder(Rectangle.NO_BORDER);
		            company_logo.scaleAbsolute(44,40);
		            company_logo_cell = new PdfPCell(company_logo,false);
		            company_logo_cell.setBorder(Rectangle.NO_BORDER);
		            company_logo_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            }
	            
	            float[] mainWidths = {10f, 70f, 15f};  
	            PdfPTable mainTable = new PdfPTable(mainWidths);
	            mainTable.setWidthPercentage(100);
	            mainTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

	            PdfPCell logoCell = new PdfPCell(company_logo_cell);
	            logoCell.setBorder(Rectangle.NO_BORDER);
	            logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            logoCell.setPaddingLeft(10);
	            mainTable.addCell(logoCell);
	            
	            PdfPTable centerTable = new PdfPTable(1);
	            centerTable.setWidthPercentage(100);
	            centerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            centerTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            centerTable.addCell(new Phrase(new Chunk(print_pdf_type_nm, small_black_normal_10)));
	            centerTable.addCell(new Phrase(new Chunk(supp_nm, black_bold1)));
	            centerTable.addCell(new Phrase(new Chunk("TAX INVOICE", small_black_normal_12)));
	            String info = "Tax invoice issued under Rule 46 of the Central Goods and Services Tax Rules, 2017";
	            centerTable.addCell(new Phrase(new Chunk(info, small_black_normal)));

	            PdfPCell centerCell = new PdfPCell(centerTable);
	            centerCell.setBorder(Rectangle.NO_BORDER);
	            centerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            mainTable.addCell(centerCell);

	            PdfPCell emptyCell = new PdfPCell(new Phrase(""));
	            emptyCell.setBorder(Rectangle.NO_BORDER);
	            mainTable.addCell(emptyCell);
	
	            float[] ContactAddrWidths5 = {0.80f,0.10F,0.20F,0.80F};
	   	     	PdfPTable Table3 = new PdfPTable(ContactAddrWidths5);
	            Table3.setWidthPercentage(100);
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("Registered Office: ",small_black_bold)));
	            
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("To:",small_black_bold)));
	
				float[] ContactAddrWidths6 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table4 = new PdfPTable(ContactAddrWidths6);
	            Table4.setWidthPercentage(100);
	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk(supp_nm,small_black_bold)));
	            
	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk(""+vendor_name+",",small_black_bold)));
	
	            float[] ContactAddrWidths7 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table5 = new PdfPTable(ContactAddrWidths7);
	            Table5.setWidthPercentage(100);
	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk(supp_addr+" "+supp_city,small_black_normal)));
	            
	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk(vend_abbr+" "+vend_city,small_black_normal)));
	            
	
	            float[] ContactAddrWidths8 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table6 = new PdfPTable(ContactAddrWidths8);
	            Table6.setWidthPercentage(100);
	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table6.addCell(new Phrase(new Chunk(supp_state+"-"+supp_pin,small_black_normal)));
	
	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table6.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_pin_no.equals("")) 
	            {
	            	Table6.addCell(new Phrase(new Chunk(vend_state + "-" + vend_pin_no, small_black_normal)));
				}
	            else 
	            {
	            	Table6.addCell(new Phrase(new Chunk(vend_state, small_black_normal)));
	            }
	            
	           	float[] ContactAddrWidths9 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table7 = new PdfPTable(ContactAddrWidths9);
	            Table7.setWidthPercentage(100);
	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk("State: "+supp_state,small_black_normal)));
	
	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk("State: "+vend_state,small_black_normal)));
	            
	            float[] ContactAddrWidths10 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table8 = new PdfPTable(ContactAddrWidths10);
	            Table8.setWidthPercentage(100);
	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table8.addCell(new Phrase(new Chunk("State Code: "+supp_state_tin,small_black_normal)));
	
	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table8.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_state_tin.equals("")) 
	            {
					Table8.addCell(new Phrase(new Chunk("State Code: " + vend_state_tin, small_black_normal)));
				}
	            else 
	            {
					Table8.addCell(new Phrase(new Chunk("", small_black_normal)));
	            }
	            
	            float[] ContactAddrWidths11 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table9 = new PdfPTable(ContactAddrWidths11);
	            Table9.setWidthPercentage(100);
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table9.addCell(new Phrase(new Chunk("GSTIN: "+supp_gstin_no,small_black_normal)));
	
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table9.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_gstin_no.equals("")) 
	            {
					Table9.addCell(new Phrase(new Chunk("GSTIN: " + vend_gstin_no, small_black_normal)));
				}
	            else 
	            {
	            	Table9.addCell(new Phrase(new Chunk("", small_black_normal)));
	            }

	            float[] ContactAddrWidths12 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table10 = new PdfPTable(ContactAddrWidths12);
	            Table10.setWidthPercentage(100);
	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table10.addCell(new Phrase(new Chunk("PAN: "+supp_pan_no,small_black_normal)));
	
	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table10.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_pan_no.equals("")) 
	            {
					Table10.addCell(new Phrase(new Chunk("PAN: " + vend_pan_no, small_black_normal)));
				}
	            else 
	            {
	            	Table10.addCell(new Phrase(new Chunk("", small_black_normal)));
	            }
	            
				float[] ContactAddrWidths13 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table11 = new PdfPTable(ContactAddrWidths13);
	            Table11.setWidthPercentage(100);
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("SAC: "+sac_code,small_black_normal)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            float[] ContactAddrWidths14 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table12 = new PdfPTable(ContactAddrWidths14);
	            Table12.setWidthPercentage(100);
	            Table12.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table12.addCell(new Phrase(new Chunk("Description of Service: "+sac_des,small_black_normal)));
	
	            Table12.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table12.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table12.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table12.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            float[] ContactAddrWidths15 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table13 = new PdfPTable(ContactAddrWidths15);
	            Table13.setWidthPercentage(100);
	            Table13.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table13.addCell(new Phrase(new Chunk("Place of Supply: "+supp_state,small_black_normal)));
	
	            Table13.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table13.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table13.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table13.addCell(new Phrase(new Chunk("",small_black_normal)));
            
	            float[] ContactAddrWidths16 = {0.40F,0.50F,0.20F,0.40F,0.40F};
	   	     	PdfPTable Table14 = new PdfPTable(ContactAddrWidths16);
	            Table14.setWidthPercentage(100);
	            Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table14.addCell(new Phrase(new Chunk("Importer:",black_bold)));
	            
	            Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table14.addCell(new Phrase(new Chunk(importer_nm,black_bold)));
	
	            Table14.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table14.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table14.addCell(new Phrase(new Chunk("Invoice No:",black_bold)));
	
	            Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table14.addCell(new Phrase(new Chunk(inv_no,black_bold)));
	            
	            float[] ContactAddrWidths17 = {0.40F,0.50F,0.20F,0.40F,0.40F};
	   	     	PdfPTable Table15 = new PdfPTable(ContactAddrWidths17);
	            Table15.setWidthPercentage(100);
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table15.addCell(new Phrase(new Chunk("Vessel Agent:",black_bold)));
	            
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk(vessel_agent_nm,black_bold)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table15.addCell(new Phrase(new Chunk("Invoice Date:",black_bold)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk(inv_dt,black_bold)));

	            float[] ContactAddrWidths18 = {0.40F,0.50F,0.20F,0.40F,0.40F};
	   	     	PdfPTable Table16 = new PdfPTable(ContactAddrWidths18);
	            Table16.setWidthPercentage(100);
	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table16.addCell(new Phrase(new Chunk("No of Time Slots - 8 Hrs Berthing:",black_bold)));
	            
	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk(berthing_slots_no,black_bold)));
	
	            Table16.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table16.addCell(new Phrase(new Chunk("Invoice Due Date :",black_bold)));
	
	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk(inv_due_dt,black_bold)));
	            
	            float[] ContactAddrWidths19 = {0.40F,0.50F,0.20F,0.40F,0.40F};
	   	     	PdfPTable Table17 = new PdfPTable(ContactAddrWidths19);
	            Table17.setWidthPercentage(100);
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table17.addCell(new Phrase(new Chunk("No. of Hours Berthing:",black_bold)));
	            
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table17.addCell(new Phrase(new Chunk(berthing_hours,black_bold)));
	
	            Table17.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table17.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table17.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table17.addCell(new Phrase(new Chunk("",black_bold)));
	
	            Table17.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table17.addCell(new Phrase(new Chunk("",black_bold)));
	            
	            float[] ContactAddrWidths28 = {0.50F,0.30F,0.20F,0.20F,0.20F};
	   	     	PdfPTable Table26 = new PdfPTable(ContactAddrWidths28);
	            Table26.setWidthPercentage(100);
	            Table26.getDefaultCell().setBorder(Rectangle.BOX);
	            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table26.addCell(new Phrase(new Chunk("Vessel Name",black_bold)));
	
	            Table26.getDefaultCell().setBorder(Rectangle.BOX);
	            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table26.addCell(new Phrase(new Chunk("Flag",black_bold)));
	
	            Table26.getDefaultCell().setBorder(Rectangle.BOX);
	            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table26.addCell(new Phrase(new Chunk("Cargo",black_bold)));
	
	            Table26.getDefaultCell().setBorder(Rectangle.BOX);
	            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table26.addCell(new Phrase(new Chunk("GRT",black_bold)));
	            
	            Table26.getDefaultCell().setBorder(Rectangle.BOX);
	            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table26.addCell(new Phrase(new Chunk("Quantity",black_bold)));
	            
	            float[] ContactAddrWidths29 = {0.50F,0.30F,0.20F,0.20F,0.20F};
	   	     	PdfPTable Table27 = new PdfPTable(ContactAddrWidths29);
	            Table27.setWidthPercentage(100);
	            Table27.getDefaultCell().setBorder(Rectangle.BOX);
	            Table27.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table27.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table27.addCell(new Phrase(new Chunk(vessel_nm,small_black_normal)));
	
	            Table27.getDefaultCell().setBorder(Rectangle.BOX);
	            Table27.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table27.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table27.addCell(new Phrase(new Chunk(vessel_flag,small_black_normal)));
	
	            Table27.getDefaultCell().setBorder(Rectangle.BOX);
	            Table27.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table27.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table27.addCell(new Phrase(new Chunk(vessel_item,small_black_normal)));
	
	            Table27.getDefaultCell().setBorder(Rectangle.BOX);
	            Table27.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table27.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table27.addCell(new Phrase(new Chunk(es.formatWithCommaNoDecimal(grt),small_black_normal)));
	            
	            Table27.getDefaultCell().setBorder(Rectangle.BOX);
	            Table27.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table27.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table27.addCell(new Phrase(new Chunk(inv_qty+" M\u00B3",small_black_normal)));
	                      
	            float[] ContactAddrWidths20 = {0.08F,0.60F,0.20F,0.20F,0.30F,0.20F};
	   	     	PdfPTable Table18 = new PdfPTable(ContactAddrWidths20);
	            Table18.setWidthPercentage(100);
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk("Sr.No.",small_black_bold)));
	
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk("Description",small_black_bold)));
	
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk("Currency",small_black_bold)));
	            
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk("Rate",small_black_bold)));
	            
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk("Unit",small_black_bold)));
	
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk("Amount",small_black_bold)));

	            String curr1 = "",curr2="",curr3="";
	            if(currency.equals("2")) 
	            {
	            	curr1 = "USD";
	            	curr2 = "USD";
	            	curr3 = "USD";
	            }
	            
	            int sr=1;
	            float[] ContactAddrWidths21 = {0.08F,0.60F,0.20F,0.20F,0.30F,0.20F};
	   	     	PdfPTable Table19 = new PdfPTable(ContactAddrWidths21);
	            Table19.setWidthPercentage(100);
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table19.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table19.addCell(new Phrase(new Chunk(desc_item,small_black_normal)));
	
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table19.addCell(new Phrase(new Chunk(curr1,small_black_normal)));
	            
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table19.addCell(new Phrase(new Chunk(rate,small_black_normal)));
	            
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table19.addCell(new Phrase(new Chunk("Per GRT/Per 8 Hrs",small_black_normal)));
	
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table19.addCell(new Phrase(new Chunk(es.formatAmount(sale_amt),small_black_normal)));
	            
	            sr+=1;
	            float[] ContactAddrWidths23 = {0.08F,0.60F,0.20F,0.20F,0.30F,0.20F};
	   	     	PdfPTable Table21 = new PdfPTable(ContactAddrWidths23);
	            Table21.setWidthPercentage(100);
	            Table21.getDefaultCell().setBorder(Rectangle.BOX);
	            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table21.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table21.getDefaultCell().setBorder(Rectangle.BOX);
	            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table21.addCell(new Phrase(new Chunk("Total Charges ",small_black_bold)));
	
	            Table21.getDefaultCell().setBorder(Rectangle.BOX);
	            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table21.addCell(new Phrase(new Chunk(curr3,small_black_bold)));
	            
	            Table21.getDefaultCell().setBorder(Rectangle.BOX);
	            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table21.addCell(new Phrase(new Chunk("",small_black_bold)));
	            
	            Table21.getDefaultCell().setBorder(Rectangle.BOX);
	            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table21.addCell(new Phrase(new Chunk("",small_black_bold)));
	
	            Table21.getDefaultCell().setBorder(Rectangle.BOX);
	            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table21.addCell(new Phrase(new Chunk(es.formatAmount(gross_amt),small_black_bold)));
	            
	            sr+=1;
	            float[] ContactAddrWidths24 = {0.08F,0.60F,0.20F,0.20F,0.30F,0.20F};
	   	     	PdfPTable Table22 = new PdfPTable(ContactAddrWidths24);
	            
	            
	            if (tax_name.contains(",")) 
				{
	            	BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_amt));
					BigDecimal factor = new BigDecimal(2);
	            	double temp_srno=sr;
	            	for(int i = 0;i<tax_name.split(", ").length;i++) 
					{
	            		BigDecimal sub_tax_amt = tax_amt1.divide(factor, 0, RoundingMode.HALF_UP);
	            		temp_srno=temp_srno+0.1;
	            		Table22.setWidthPercentage(100);
	            		Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table22.addCell(new Phrase(new Chunk(nf0.format(temp_srno),small_black_normal)));
			
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            Table22.addCell(new Phrase(new Chunk(tax_name.split(",")[i],small_black_normal)));
			
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table22.addCell(new Phrase(new Chunk(curr3,small_black_normal)));
			            
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table22.addCell(new Phrase(new Chunk("",small_black_bold)));
			            
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table22.addCell(new Phrase(new Chunk("",small_black_bold)));
			
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            Table22.addCell(new Phrase(new Chunk(es.formatAmount(""+Double.parseDouble(""+sub_tax_amt)),small_black_normal)));
					}
				}

	            Table22.setWidthPercentage(100);
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table22.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table22.addCell(new Phrase(new Chunk("Tax ("+tax_name+")",small_black_bold)));
	
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table22.addCell(new Phrase(new Chunk(curr3,small_black_bold)));
	            
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table22.addCell(new Phrase(new Chunk("",small_black_bold)));
	            
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table22.addCell(new Phrase(new Chunk("",small_black_bold)));
	
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table22.addCell(new Phrase(new Chunk(es.formatAmount(tax_amt),small_black_bold)));
	            
	            sr+=1;
	            float[] ContactAddrWidths25 = {0.08F,0.60F,0.20F,0.20F,0.30F,0.20F};
	   	     	PdfPTable Table23 = new PdfPTable(ContactAddrWidths25);
	            Table23.setWidthPercentage(100);
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table23.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table23.addCell(new Phrase(new Chunk("Total Amount in USD",small_black_bold)));
	
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table23.addCell(new Phrase(new Chunk(curr3,small_black_bold)));
	            
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table23.addCell(new Phrase(new Chunk("",small_black_bold)));
	            
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table23.addCell(new Phrase(new Chunk("",small_black_bold)));
	
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table23.addCell(new Phrase(new Chunk(es.formatAmount(net_amt),small_black_bold)));
	            
	            float[] ContactAddrWidths26_1 = {0.100F};
	   	     	PdfPTable Table24_1 = new PdfPTable(ContactAddrWidths26_1);
	   	     	Table24_1.setWidthPercentage(100);
	   	     	Table24_1.getDefaultCell().setBorder(Rectangle.BOX);
	   	     	Table24_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	   	     	Table24_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	   	     	Table24_1.addCell(new Phrase(new Chunk("Amount in Words | USD "+amt_in_word+" Only",black_bold)));
	            
		     	float[] ContactAddrWidths26 = {0.100F};
	   	     	PdfPTable Table24 = new PdfPTable(ContactAddrWidths26);
	   	     	Table24.setWidthPercentage(100);
	   	     	Table24.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	   	     	Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	   	  		Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	   	  		Table24.addCell(new Phrase(new Chunk("For "+supp_nm,black_bold)));
	
	   	  		float[] ContactAddrWidths27 = {0.100F};
	   	  		PdfPTable Table25 = new PdfPTable(ContactAddrWidths27);
		     	Table25.setWidthPercentage(100);
		     	Table25.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table25.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table25.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table25.addCell(new Phrase(new Chunk("Authorised Signatory",black_bold)));
		     	
		     	PdfPTable BillingFieldsInfoTable81 = new PdfPTable(1);
			     BillingFieldsInfoTable81.setWidthPercentage(20);
			     BillingFieldsInfoTable81.setHorizontalAlignment(Element.ALIGN_LEFT);
			     BillingFieldsInfoTable81.getDefaultCell().setBorder(Rectangle.BOX);
			     BillingFieldsInfoTable81.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			     BillingFieldsInfoTable81.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	
			     PdfFormField field = PdfFormField.createSignature(writer);
			     field.setFieldName(SIGNAME);
	
			     field.setPage();
			     field.setWidget(new Rectangle(72, 732, 144, 780), PdfAnnotation.HIGHLIGHT_NONE);
			     field.setFlags(PdfAnnotation.FLAGS_PRINT);
			     writer.addAnnotation(field);
	
			     PdfPCell sigCell = new PdfPCell();
			     FieldPositioningEvents events = new FieldPositioningEvents(writer, field);
			     sigCell.setCellEvent(events);
			     sigCell.setBorder(Rectangle.NO_BORDER);
			     sigCell.setFixedHeight(50f);
			     BillingFieldsInfoTable81.addCell(sigCell);
			    
			     PdfPTable leftSignTable = new PdfPTable(1);
			     leftSignTable.setWidthPercentage(100);
			     leftSignTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			     leftSignTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	
			     leftSignTable.addCell(Table24);
			     leftSignTable.addCell(BillingFieldsInfoTable81);
			     leftSignTable.addCell(Table25);
	
			     float[] InvoiceDateInfoWidths_qr = {0.30f, 0.20f, 0.40f};
			     PdfPTable InvoiceDateInfoTable_qr = new PdfPTable(InvoiceDateInfoWidths_qr);
	
			     if (!irn_no.equals("") && !qr_code.equals("")) {
	
			         int width = 90;
			         int height = 90;
			         Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
			         hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
	
			         QRCodeWriter qrCodeWriter = new QRCodeWriter();
			         BitMatrix matrix = qrCodeWriter.encode(qr_code, BarcodeFormat.QR_CODE, width, height, hintMap);
	
			         BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	
			         for (int x = 0; x < width; x++) {
			             for (int y = 0; y < height; y++) {
			                 image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
			             }
			         }
	
			         ByteArrayOutputStream baos = new ByteArrayOutputStream();
			         ImageIO.write(image, "png", baos);
			         Image qr_codeimg = Image.getInstance(baos.toByteArray());
			         qr_codeimg.setBorder(Rectangle.NO_BORDER);
			         qr_codeimg.setAlignment(Element.ALIGN_LEFT);
	
			         PdfPCell qrcode_cell = new PdfPCell(qr_codeimg, false);
			         qrcode_cell.setBorder(Rectangle.NO_BORDER);
			         qrcode_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	
			         String char_16 = irn_no.substring(0, 16);
			         String char_32 = irn_no.substring(16, 32);
			         String char_48 = irn_no.substring(32, 48);
			         String char_64 = irn_no.substring(48);
			         String final_irn_no = char_16 + "\n" + char_32 + "\n" + char_48 + "\n" + char_64;
	
			         PdfPTable InvoiceDateInfoTable_qr1 = new PdfPTable(1);
			         InvoiceDateInfoTable_qr1.setWidthPercentage(100);
			         InvoiceDateInfoTable_qr1.getDefaultCell().setBorder(Rectangle.BOX);
			         InvoiceDateInfoTable_qr1.addCell(qrcode_cell);
	
			         PdfPTable InvoiceDateInfoTable_qr11 = new PdfPTable(1);
			         InvoiceDateInfoTable_qr11.setWidthPercentage(100);
			         InvoiceDateInfoTable_qr11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			         InvoiceDateInfoTable_qr11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			         InvoiceDateInfoTable_qr11.addCell(new Phrase(new Chunk("IRN", black_bold)));
			         InvoiceDateInfoTable_qr11.addCell(new Phrase(new Chunk(final_irn_no, small_black_normal)));
	
			         InvoiceDateInfoTable_qr.setWidthPercentage(100);
			         InvoiceDateInfoTable_qr.getDefaultCell().setBorder(Rectangle.BOX);
			         InvoiceDateInfoTable_qr.addCell(InvoiceDateInfoTable_qr11);
	
			         InvoiceDateInfoTable_qr.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			         InvoiceDateInfoTable_qr.addCell("");
	
			         InvoiceDateInfoTable_qr.addCell(InvoiceDateInfoTable_qr1);
			     }
	
			     PdfPTable finalRow = new PdfPTable(3);
			     finalRow.setWidthPercentage(100);
			     finalRow.setWidths(new float[]{0.3f, 0.2f ,0.5f});
			     finalRow.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	
			     finalRow.addCell(leftSignTable);
			     finalRow.addCell("");
			     finalRow.addCell(InvoiceDateInfoTable_qr);
		     	
		     	float[] ContactAddrWidths30 = {0.100F};
	   	  		PdfPTable Table28 = new PdfPTable(ContactAddrWidths30);
		     	Table28.setWidthPercentage(100);
		     	Table28.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table28.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table28.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table28.addCell(new Phrase(new Chunk("Notes:",black_bold)));
		     	
		     	float[] ContactAddrWidths31 = {0.01F,0.200F};
	   	  		PdfPTable Table29 = new PdfPTable(ContactAddrWidths31);
		     	Table29.setWidthPercentage(100);
		     	Table29.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table29.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table29.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table29.addCell(new Phrase(new Chunk("1. ",small_black_normal)));
		     	
		     	Table29.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table29.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table29.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table29.addCell(new Phrase(new Chunk(remark1,small_black_normal)));
		     	
		     	float[] ContactAddrWidths32 = {0.01F,0.200F};
	   	  		PdfPTable Table30 = new PdfPTable(ContactAddrWidths32);
		     	Table30.setWidthPercentage(100);
		     	Table30.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table30.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table30.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table30.addCell(new Phrase(new Chunk("2. ",small_black_normal)));
		     	
		     	Table30.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table30.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table30.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table30.addCell(new Phrase(new Chunk(remark2,small_black_normal)));
		     	
		     	float[] ContactAddrWidths33 = {0.01F,0.200F};
	   	  		PdfPTable Table31 = new PdfPTable(ContactAddrWidths33);
		     	Table31.setWidthPercentage(100);
		     	Table31.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table31.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table31.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table31.addCell(new Phrase(new Chunk("3. ",small_black_normal)));
		     	
		     	Table31.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table31.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table31.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table31.addCell(new Phrase(new Chunk(remark3,small_black_normal)));
		     	
		     	float[] ContactAddrWidths34 = {0.01F,0.200F};
	   	  		PdfPTable Table32 = new PdfPTable(ContactAddrWidths34);
		     	Table32.setWidthPercentage(100);
		     	Table32.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table32.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table32.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table32.addCell(new Phrase(new Chunk("4. ",small_black_normal)));
		     	
		     	Table32.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table32.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table32.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table32.addCell(new Phrase(new Chunk(remark4,small_black_normal)));

		     	Paragraph gap = new Paragraph("");
		     	gap.setSpacingBefore(3f); // adjust value
		     	gap.setSpacingAfter(3f);
		     	
				document.add(mainTable);
				document.add(gap);
				document.add(Table3);
				document.add(Table4);
				document.add(Table5);
				document.add(Table6);
				document.add(Table7);
				document.add(Table8);
				document.add(Table9);
				document.add(Table10);
				document.add(Table11);
				document.add(Table12);
				document.add(Table13);
				document.add(gap);
				document.add(Table14);
				document.add(Table15);
				document.add(Table16);
				document.add(Table17);
				document.add(gap);
				document.add(Table26);
				document.add(Table27);
				document.add(gap);
				document.add(Table18);
				document.add(Table19);
				document.add(Table21);
				document.add(Table22);
				document.add(Table23);
				document.add(gap);
				document.add(Table24_1);
				document.add(finalRow);
				document.add(Table28);
				document.add(Table29);
				document.add(Table30);
				document.add(Table31);
				document.add(Table32);
				
	            document.close();
	            
	            if(is_print.equals("1"))
				{
		            File isPDFexist = new File(path);
					if(isPDFexist.exists())
					{
						int update_flag=0;
						int pdf_entry=0;
						
						int cont=0;
						queryString="UPDATE FMS_OTH_INVOICE_MST SET PDF_INV_DTL=? ";
							if(print_pdf_type.equals("O"))
							{
								queryString+= ",PRINT_BY_ORI=?,PRINT_DT_ORI=SYSDATE ";
							}
							else if(print_pdf_type.equals("D"))
							{
								queryString+= ",PRINT_BY_DUP=?,PRINT_DT_DUP=SYSDATE ";
							}
							queryString+= "WHERE COMPANY_CD=? AND INVOICE_NO=? AND INVOICE_TYPE=? AND INVOICE_SEQ = ? AND SUPPLIER_CD = ?";
							
						stmt=conn.prepareStatement(queryString);
						stmt.setString(++cont, print_pdf_type);
						if(print_pdf_type.equals("O"))
						{
							stmt.setString(++cont, emp_cd);
						}
						else if(print_pdf_type.equals("D"))
						{
							stmt.setString(++cont, emp_cd);
						}
						stmt.setString(++cont, comp_cd);
						stmt.setString(++cont, inv_no);
						stmt.setString(++cont, inv_type);
						stmt.setString(++cont, inv_seq);
						stmt.setString(++cont, supp_cd);
						update_flag=stmt.executeUpdate();
						stmt.close();
						
						int count=0;
				        queryString1="SELECT COUNT(*) "
				        		+ "FROM FMS_OTH_INV_FILE_DTL "
				        		+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? "
				        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND SUPPLIER_CD =?";
				        stmt1=conn.prepareStatement(queryString1);
				        stmt1.setString(1, comp_cd);
				        stmt1.setString(2, inv_type);
				        stmt1.setString(3, inv_seq);
				        stmt1.setString(4, fin_yr);
				        stmt1.setString(5, print_pdf_type);
				        stmt1.setString(6, supp_cd);
				        rset1=stmt1.executeQuery();
				        if(rset1.next())
				        {
				        	count=rset1.getInt(1);
				        }
				        rset1.close();
				        stmt1.close();
				        
				        if(count > 0)
				        {
				        	queryString1="UPDATE FMS_OTH_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
				        			+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? "
					        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND SUPPLIER_CD =?";
				        	stmt1=conn.prepareStatement(queryString1);
				        	stmt1.setString(1, file_nm);
				        	stmt1.setString(2, emp_cd);
				        	stmt1.setString(3, comp_cd);
				 	        stmt1.setString(4, inv_type);
				 	        stmt1.setString(5, inv_seq);
				 	        stmt1.setString(6, fin_yr);
				 	        stmt1.setString(7, print_pdf_type); 
				 	        stmt1.setString(8, supp_cd); 
				 	        pdf_entry=stmt1.executeUpdate();
				        	
				        	stmt1.close();
				        }
				        else
				        {
				        	queryString1="INSERT INTO FMS_OTH_INV_FILE_DTL(COMPANY_CD,SUPPLIER_CD,INVOICE_SEQ,INVOICE_TYPE,FINANCIAL_YEAR,PDF_TYPE,"
				        			+ "FILE_NAME,ENT_BY,ENT_DT) "
				        			+ "VALUES(?,?,?,?,?,?,"
				        			+ "?,?,SYSDATE)";
				        	stmt1=conn.prepareStatement(queryString1);
				        	stmt1.setString(1, comp_cd);
				        	stmt1.setString(2, supp_cd);
				 	        stmt1.setString(3, inv_seq);
				 	        stmt1.setString(4, inv_type);
				 	        stmt1.setString(5, fin_yr);
				 	        stmt1.setString(6, print_pdf_type);
				 	        stmt1.setString(7, file_nm);
				        	stmt1.setString(8, emp_cd);
				        	pdf_entry=stmt1.executeUpdate();
				        	
				        	stmt1.close();
				        }
				        
				        if(pdf_entry == 0 || update_flag == 0)
				        {
				        	conn.rollback();
				        	deletePdfFile(path);
				        	msg = "Failed! - PDF "+file_nm+" for HPPL Shipping Agent with Invoice No. "+inv_no+" Generation Failed!";
				        	msg_type="E";
				        }
				        else
				        {
				        	conn.commit();
				        	msg = "Successful! - PDF "+file_nm+" for HPPL Shipping Agent with Invoice No. "+inv_no+" Generated Successfully!";
				        	msg_type="S";
				        }
					}
					else
					{
						msg="Failed! - PDF for HPPL Shipping Agent with Invoice No. "+inv_no+" Generation Failed!";
						msg_type="E";
					}
				}
			}
		}
		catch(Exception e)
		{
			conn.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			
			document.close();
			deletePdfFile(path);
			
			msg = "Error in Exception! - PDF for HPPL Shipping Agent with Invoice No. "+inv_no+" Generation Failed!";
			msg_type="E";	
		}
		finally
		{
			document.close();
		}
		
		if(is_print.equals("1"))
		{
			try
			{
				new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, "", "", msg);  	
			}
			catch(Exception infoLogger)
			{
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
			}
		}
	}
	
	//AP20251106
	public void getInvoiceDtlHPPLShippingAgent()
	{	
		String function_nm="getInvoiceDtlHPPLShippingAgent()";
		try
		{
			queryString = "SELECT A.COMPANY_CD, A.SUPPLIER_CD, A.VENDOR_CD, TO_CHAR(A.INVOICE_DT, 'DD/MM/YYYY'), B.SAC_CODE, A.SALE_PRICE_UNIT, "
					+ "A.EXCHG_RATE_VALUE, TO_CHAR(A.EXCHG_RATE_DT, 'DD/MM/YYYY'),"
					+ "A.GROSS_AMT, A.TAX_AMT_INR, B.PURCHASE_NO, B.REFERENCE_NO, B.TAX_STRUCT_CD, B.ITEM_DESCRIPTION, A.NET_PAYABLE,A.SALE_AMT,A.REMARK,A.REMARK1,"
					+ "A.APPROVED_FLAG,A.FINANCIAL_YEAR,A.INVOICE_SEQ,A.CHECKED_FLAG,A.INVOICE_NO,A.INVOICE_ID_SEQ,TO_CHAR(B.TAX_EFF_DT, 'DD/MM/YYYY'),A.INVOICE_CATEGORY,A.INVOICE_RAISED_IN,  "
					+ "A.SALE_PRICE,TO_CHAR(A.DUE_DT, 'DD/MM/YYYY'),A.REMARK2,A.REMARK3,B.VESSEL_CD,B.VESSEL_AGENT,B.VESSEL_FLAG,B.GRT,B.IMPORTER,B.QUANTITY,B.HRS_BERTHING,B.TIME_SLOTS_BERTHING "
					+ "FROM FMS_OTH_INVOICE_MST A, FMS_OTH_INVOICE_DTL B ";
			if(is_print.equals("0"))
			{	
				queryString += "WHERE A.COMPANY_CD = ? AND A.INVOICE_SEQ = ? AND A.INVOICE_TYPE = ? AND A.FINANCIAL_YEAR=? AND A.SUPPLIER_CD=? AND A.INV_FLAG = ? "
						+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.INVOICE_SEQ = B.INVOICE_SEQ AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.INVOICE_TYPE = B.INVOICE_TYPE AND "
						+ "A.INVOICE_DT = B.EFF_DT AND A.VENDOR_CD = B.VENDOR_CD AND A.SUPPLIER_CD = B.SUPPLIER_CD AND A.INV_FLAG = B.INV_FLAG";
			}
			else
			{
				queryString += "WHERE A.COMPANY_CD = ? AND A.INVOICE_NO = ? AND A.INVOICE_TYPE = ? AND A.INV_FLAG = ? "
					+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.INVOICE_SEQ = B.INVOICE_SEQ AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.INVOICE_TYPE = B.INVOICE_TYPE AND "
					+ "A.INVOICE_DT = B.EFF_DT AND A.VENDOR_CD = B.VENDOR_CD AND A.SUPPLIER_CD = B.SUPPLIER_CD AND A.INV_FLAG = B.INV_FLAG";
			}
			stmt = conn.prepareStatement(queryString);
			if(is_print.equals("0"))
			{
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_seq);
				stmt.setString(3, inv_type);
				stmt.setString(4, fin_yr);
				stmt.setString(5, supp_cd);
				stmt.setString(6, "F");
			}
			else
			{
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_no);
				stmt.setString(3, inv_type);
				stmt.setString(4, "F");
			}
			rset = stmt.executeQuery();
			if(rset.next()) 
			{
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
				importer_nm = rset.getString(36) == null ? "" : rset.getString(36);
				inv_qty = rset.getString(37) == null ? "" : rset.getString(37);
				berthing_hours = rset.getString(38) == null ? "" : rset.getString(38);
				berthing_slots_no = rset.getString(39) == null ? "" : rset.getString(39);
				
				getShipMst();
				
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
				
				tax_name=utilBean.getTaxDescr(conn,tax_struct_cd);
				if (tax_name.contains(",")) 
				{
					tax_value = nf.format(Double.parseDouble((Double.doubleToRawLongBits(taxValue)==Double.doubleToRawLongBits(0)) ? "0" : String.format("%.2f", taxValue / 2)));
				} 
				else 
				{
					tax_value = nf.format(Double.parseDouble((Double.doubleToRawLongBits(taxValue)==Double.doubleToRawLongBits(0)) ? "0" : String.format("%.2f", taxValue)));
				}	
			
				tax_amt=nf.format(taxValue);
				
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
	
	//AP20251106
	public void getShipMst()
	{
		String function_nm="getShipMst()";
		try
		{
			int count =0;
			
			queryString="SELECT SHIP_CD,SHIP_NAME,SHIP_FLAG,SHIP_ITEM "
					+ "FROM FMS_SHIP_MST A "
					+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_SHIP_MST B WHERE A.SHIP_CD=B.SHIP_CD) ";
			if(!vessel_cd.equals(""))
			{
				queryString+= "AND SHIP_CD=? ";
			}
			queryString+= "ORDER BY SHIP_CD DESC ";
			stmt=conn.prepareStatement(queryString);
			if(!vessel_cd.equals(""))
			{
				stmt.setString(++count, vessel_cd);
			}
			rset=stmt.executeQuery();
			while(rset.next())
			{
				vessel_nm =(rset.getString(2)==null?"":rset.getString(2));
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

	public void getInvoiceDtlHPPLSEIPL()
	{	
		String function_nm="getInvoiceDtlHPPLSEIPL()";

		try
		{
			queryString = "SELECT A.COMPANY_CD,A.VENDOR_CD,A.SUPPLIER_CD,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),B.VENDOR_SUPP_INV_REF,"
					+ "B.PACER_NO,A.GROSS_AMT,A.TAX_AMT_INR,A.NET_PAYABLE,A.EXCHG_RATE_VALUE,TO_CHAR(A.EXCHG_RATE_DT, 'DD/MM/YYYY'),"
					+ "A.SALE_PRICE_UNIT,B.SAC_CODE,B.TAX_STRUCT_CD,B.ITEM_DESCRIPTION,A.REMARK,A.REMARK1,A.APPROVED_FLAG,A.FINANCIAL_YEAR,"
					+ "A.INVOICE_SEQ,A.SALE_AMT,A.CHECKED_FLAG,A.INVOICE_NO,A.INVOICE_ID_SEQ,TO_CHAR(B.TAX_EFF_DT, 'DD/MM/YYYY'),A.INVOICE_CATEGORY,A.INVOICE_RAISED_IN,"
					+ "TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),B.CARGO_TYPE,TO_CHAR(A.EXCHG_RATE_DT,'DD/MM/YYYY'),A.REMARK2,A.EXCHG_RATE_CD "
					+ "FROM FMS_OTH_INVOICE_MST A, FMS_OTH_INVOICE_DTL B ";
			if(is_print.equals("0"))
			{
				queryString += "WHERE A.COMPANY_CD = ? AND A.INVOICE_SEQ = ? AND A.INVOICE_TYPE = ? AND A.FINANCIAL_YEAR=? AND A.SUPPLIER_CD=? AND A.INV_FLAG = ? "
						+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.INVOICE_TYPE = B.INVOICE_TYPE "
						+ "AND A.VENDOR_CD = B.VENDOR_CD AND A.SUPPLIER_CD = B.SUPPLIER_CD AND A.INVOICE_SEQ = B.INVOICE_SEQ AND A.VENDOR_TYPE = 'S' AND A.INV_FLAG = B.INV_FLAG";
			}
			else
			{
				queryString += "WHERE A.COMPANY_CD = ? AND A.INVOICE_NO = ? AND A.INVOICE_TYPE = ? AND A.INV_FLAG = ? "
						+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.INVOICE_SEQ = B.INVOICE_SEQ AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.INVOICE_TYPE = B.INVOICE_TYPE AND "
						+ "A.INVOICE_DT = B.EFF_DT AND A.VENDOR_CD = B.VENDOR_CD AND A.SUPPLIER_CD = B.SUPPLIER_CD AND A.INV_FLAG = B.INV_FLAG";
			}
					
			stmt = conn.prepareStatement(queryString);
			if(is_print.equals("0"))
			{
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_seq);
				stmt.setString(3, inv_type);
				stmt.setString(4, fin_yr);
				stmt.setString(5, supp_cd);
				stmt.setString(6, "F");
			}
			else
			{
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_no);
				stmt.setString(3, inv_type);
				stmt.setString(4, "F");
			}
			
			rset = stmt.executeQuery();
			if(rset.next()) 
			{
				
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
				fin_yr = rset.getString(19) == null ? "" : rset.getString(19);
				inv_seq = rset.getString(20) == null ? "" : rset.getString(20);
				sale_amt = nf.format(Double.parseDouble(rset.getString(21) == null ? "" : rset.getString(21)));
				inv_no = rset.getString(23) == null ? "" : rset.getString(23);
				inv_id_seq = rset.getString(24) == null ? "" : rset.getString(24);
				invoice_category = rset.getString(26) == null ? "" : rset.getString(26);
				invoice_raised_in = rset.getString(27) == null ? "" : rset.getString(27);
				inv_due_dt = rset.getString(28) == null ? "" : rset.getString(28);
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
				
				tax_name=utilBean.getTaxDescr(conn,tax_struct_cd);
				
				if (tax_name.contains(",")) 
				{
					tax_value = nf.format(Double.parseDouble((Double.doubleToRawLongBits(taxValue)==Double.doubleToRawLongBits(0)) ? "0" : String.format("%.2f", taxValue / 2)));
				} 
				else 
				{
					tax_value = nf.format(Double.parseDouble((Double.doubleToRawLongBits(taxValue)==Double.doubleToRawLongBits(0)) ? "0" : String.format("%.2f", taxValue)));
				}	
			
				tax_amt=nf.format(taxValue);
			}
			rset.close();
			stmt.close();
			
			if(!net_amt.equalsIgnoreCase(""))		
			{
				amt_in_word=es.convert(Double.parseDouble(net_amt));
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void getCargoDetails()
	{
		String function_nm="getCargoDetails()";
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
	
	public void pdfHPPLSEIPL() throws SQLException
	{
		String function_nm="pdfHPPLSEIPL()";
		
		String emp_nm ="";
		String ip = "";
		msg="";
		//String msg_content="";
		String path ="";
		
		Rectangle pageSize = new Rectangle(595, 842);
		
		pageSize.setBackgroundColor(BaseColor.WHITE);
		
		Document document = new Document(pageSize);
		try
		{
			HttpSession session = request.getSession();
			emp_nm = (String)session.getAttribute("emp_nm")==null?"":(String)session.getAttribute("emp_nm");
			ip = (String)session.getAttribute("ip")==null?"":(String)session.getAttribute("ip");
			
			String company_nm = ""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");
			String company_abbr = ""+session.getAttribute("comp_abbr")==null?"":""+session.getAttribute("comp_abbr");
			
			int pdfGenCount=0;
			if(is_print.equals("1"))
	        {
				queryString = "SELECT COUNT(*) "
							+ "FROM FMS_OTH_INVOICE_MST A ,FMS_OTH_INV_FILE_DTL B "
							+ "WHERE A.COMPANY_CD=? AND A.INVOICE_NO = ? AND A.INVOICE_TYPE = ? ";
							if(print_pdf_type.equals("O"))
							{
								queryString+= "AND A.PRINT_BY_ORI IS NOT NULL AND A.PRINT_DT_ORI IS NOT NULL ";
							}
							else if(print_pdf_type.equals("T"))
							{
								queryString+= "AND A.PRINT_BY_TRI IS NOT NULL AND A.PRINT_DT_TRI IS NOT NULL ";
							}
							else if(print_pdf_type.equals("D"))
							{
								queryString+= "AND A.PRINT_BY_DUP IS NOT NULL AND A.PRINT_DT_DUP IS NOT NULL ";
							}
				queryString += "AND B.PDF_TYPE=? AND B.INVOICE_TYPE = ? AND B.FILE_NAME IS NOT NULL "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.INVOICE_TYPE = B.INVOICE_TYPE AND A.SUPPLIER_CD = B.SUPPLIER_CD";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_no);
				stmt.setString(3, inv_type);
				stmt.setString(4, print_pdf_type);
				stmt.setString(5, inv_type);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					pdfGenCount=rset.getInt(1);
				}
				rset.close();
				stmt.close();
	        }
			else
			{
				pdfGenCount = 0;
			}
	
			if(pdfGenCount>0)
			{
				msg="Failed! - PDF for PFA Fees Invoice with Invoice No. "+inv_no+" already Generated!";
	        	msg_type="E";
	        }
			else
			{
				
				getInvoiceDtlHPPLSEIPL();
				getSupplierDtl();
				getVendorDtl();
				getCargoDetails();
				
				String irn_no="";
				String qr_code="";
				
				queryString1="SELECT IRN_NO,SIGN_QR_CODE "
						+ "FROM FMS_OTH_INV_IRN_DTL "
						+ "WHERE COMPANY_CD=? AND INVOICE_NO=? ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, inv_no);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					irn_no=rset1.getString(1)==null?"":rset1.getString(1);
					qr_code=rset1.getString(2)==null?"":rset1.getString(2);
				}
				rset1.close();
				stmt1.close();
				
				String print_pdf_type_nm="ORIGINAL";
				if(print_pdf_type.equals("O"))
				{
					print_pdf_type_nm="ORIGINAL";
				}
				else if(print_pdf_type.equals("D"))
				{
					print_pdf_type_nm="DUPLICATE";
				}
	
				String appPath = request.getServletContext().getRealPath("");
	
				String main_folder="";
				if(!comp_cd.equals(""))
				{
					main_folder=CommonVariable.work_dir+comp_cd;
				}
				
				String sub_folder="unsigned_pdf";
				String sub_folder2="ig_invoice";
				
				String temp_path=appPath+File.separator+main_folder+File.separator+sub_folder+File.separator+sub_folder2;
				File main_folderDir = new File(temp_path);
		        if(!main_folderDir.exists())
		        {
		        	main_folderDir.mkdirs();
		        }

		        if(is_print.equals("1"))
		        {
		        	file_nm=print_pdf_type+"-"+supp_abbr+"-"+inv_type+"-"+inv_no.replace("/", "-")+".pdf";
		        }
		        else
		        {
		        	file_nm="temp.pdf";
		        }
	
				path = appPath+"/"+main_folder+"/"+sub_folder+"/"+sub_folder2+"/"+file_nm;
				PdfWriter writer = PdfWriter.getInstance(document,new FileOutputStream(path));
	
				document.open();
				document.setPageSize(pageSize);
	            document.newPage();
	
	            String context_nm = request.getContextPath();
				String server_nm = request.getServerName();
				String server_port = ""+request.getServerPort();
	
				file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm;
	
				Font small_black_normal = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
				Font small_black_normal_10 = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
				Font small_black_normal_14 = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.NORMAL, BaseColor.BLACK);
				Font small_black_normal_12 = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
				Font small_black_bold = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
	            Font black_bold = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
	            Font black_bold1 = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
	            
	            PdfPCell company_logo_cell = new PdfPCell();
	            if(!comp_logo.equals(""))
	            {
					String file_path = request.getRealPath("/"+CommonVariable.company_logo_path+"/"+comp_logo);
	            	Image company_logo = Image.getInstance(file_path);
		            company_logo.setBorder(Rectangle.NO_BORDER);
		            company_logo.scaleAbsolute(44,40);
		            company_logo_cell = new PdfPCell(company_logo,false);
		            company_logo_cell.setBorder(Rectangle.NO_BORDER);
		            company_logo_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            }
	            
	            float[] mainWidths = {10f, 70f, 15f};  
	            PdfPTable mainTable = new PdfPTable(mainWidths);
	            mainTable.setWidthPercentage(100);
	            mainTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

	            PdfPCell logoCell = new PdfPCell(company_logo_cell);
	            logoCell.setBorder(Rectangle.NO_BORDER);
	            logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            logoCell.setPaddingLeft(10);
	            mainTable.addCell(logoCell);
	            
	            PdfPTable centerTable = new PdfPTable(1);
	            centerTable.setWidthPercentage(100);
	            centerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            centerTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            centerTable.addCell(new Phrase(new Chunk(print_pdf_type_nm, small_black_normal_10)));
	            centerTable.addCell(new Phrase(new Chunk(supp_nm, black_bold1)));
	            centerTable.addCell(new Phrase(new Chunk("TAX INVOICE", small_black_normal_12)));
	            String info = "Tax invoice issued under Rule 46 of the Central Goods and Services Tax Rules, 2017";
	            centerTable.addCell(new Phrase(new Chunk(info, small_black_normal)));

	            PdfPCell centerCell = new PdfPCell(centerTable);
	            centerCell.setBorder(Rectangle.NO_BORDER);
	            centerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            mainTable.addCell(centerCell);

	            PdfPCell emptyCell = new PdfPCell(new Phrase(""));
	            emptyCell.setBorder(Rectangle.NO_BORDER);
	            mainTable.addCell(emptyCell);
	            
	            float[] ContactAddrWidths5 = {0.80f,0.10F,0.20F,0.80F};
	   	     	PdfPTable Table3 = new PdfPTable(ContactAddrWidths5);
	            Table3.setWidthPercentage(100);
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("Registered Office: ",small_black_bold)));
	            
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("To:",small_black_bold)));
	
				float[] ContactAddrWidths6 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table4 = new PdfPTable(ContactAddrWidths6);
	            Table4.setWidthPercentage(100);
	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk(supp_nm,small_black_bold)));
	            
	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk(""+vendor_name+",",small_black_bold)));
	
	            float[] ContactAddrWidths7 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table5 = new PdfPTable(ContactAddrWidths7);
	            Table5.setWidthPercentage(100);
	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk(supp_addr+" "+supp_city,small_black_normal)));
	            
	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk(vend_abbr+" "+vend_city,small_black_normal)));
	            
	
	            float[] ContactAddrWidths8 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table6 = new PdfPTable(ContactAddrWidths8);
	            Table6.setWidthPercentage(100);
	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table6.addCell(new Phrase(new Chunk(supp_state+"-"+supp_pin,small_black_normal)));
	
	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table6.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_pin_no.equals("")) 
	            {
	            	Table6.addCell(new Phrase(new Chunk(vend_state + "-" + vend_pin_no, small_black_normal)));
				}
	            else 
	            {
	            	Table6.addCell(new Phrase(new Chunk(vend_state, small_black_normal)));
	            }
	            
	           	float[] ContactAddrWidths9 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table7 = new PdfPTable(ContactAddrWidths9);
	            Table7.setWidthPercentage(100);
	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk("State: "+supp_state,small_black_normal)));
	
	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk("State: "+vend_state,small_black_normal)));
	            
	            float[] ContactAddrWidths10 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table8 = new PdfPTable(ContactAddrWidths10);
	            Table8.setWidthPercentage(100);
	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table8.addCell(new Phrase(new Chunk("State Code: "+supp_state_tin,small_black_normal)));
	
	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table8.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_state_tin.equals("")) 
	            {
					Table8.addCell(new Phrase(new Chunk("State Code: " + vend_state_tin, small_black_normal)));
				}
	            else 
	            {
					Table8.addCell(new Phrase(new Chunk("", small_black_normal)));
	            }
	            
	            float[] ContactAddrWidths11 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table9 = new PdfPTable(ContactAddrWidths11);
	            Table9.setWidthPercentage(100);
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table9.addCell(new Phrase(new Chunk("GSTIN: "+supp_gstin_no,small_black_normal)));
	
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table9.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_gstin_no.equals("")) 
	            {
					Table9.addCell(new Phrase(new Chunk("GSTIN: " + vend_gstin_no, small_black_normal)));
				}
	            else 
	            {
	            	Table9.addCell(new Phrase(new Chunk("", small_black_normal)));
	            }

	            float[] ContactAddrWidths12 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table10 = new PdfPTable(ContactAddrWidths12);
	            Table10.setWidthPercentage(100);
	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table10.addCell(new Phrase(new Chunk("PAN: "+supp_pan_no,small_black_normal)));
	
	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table10.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_pan_no.equals("")) 
	            {
					Table10.addCell(new Phrase(new Chunk("PAN: " + vend_pan_no, small_black_normal)));
				}
	            else 
	            {
	            	Table10.addCell(new Phrase(new Chunk("", small_black_normal)));
	            }
	            
	            float[] ContactAddrWidths13 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table11 = new PdfPTable(ContactAddrWidths13);
	            Table11.setWidthPercentage(100);
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("SAC: "+sac_code,small_black_normal)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            float[] ContactAddrWidths14 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table12 = new PdfPTable(ContactAddrWidths14);
	            Table12.setWidthPercentage(100);
	            Table12.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table12.addCell(new Phrase(new Chunk("Description of Service: "+sac_des,small_black_normal)));
	
	            Table12.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table12.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table12.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table12.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            float[] ContactAddrWidths15 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table13 = new PdfPTable(ContactAddrWidths15);
	            Table13.setWidthPercentage(100);
	            Table13.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table13.addCell(new Phrase(new Chunk("Place of Supply: "+supp_state,small_black_normal)));
	
	            Table13.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table13.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table13.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table13.addCell(new Phrase(new Chunk("",small_black_normal)));
            
	            float[] ContactAddrWidths16 = {0.80F,0.20F,0.50F,0.40F};
	   	     	PdfPTable Table14 = new PdfPTable(ContactAddrWidths16);
	            Table14.setWidthPercentage(100);
	            Table14.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table14.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table14.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table14.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table14.addCell(new Phrase(new Chunk("Invoice No :",black_bold)));
	
	            Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table14.addCell(new Phrase(new Chunk(inv_no,black_bold)));
	            
	            float[] ContactAddrWidths17 = {0.80F,0.20F,0.50F,0.40F};
	   	     	PdfPTable Table15 = new PdfPTable(ContactAddrWidths17);
	            Table15.setWidthPercentage(100);
	            Table15.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table15.addCell(new Phrase(new Chunk("Invoice Date :",black_bold)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk(inv_dt,black_bold)));

	            float[] ContactAddrWidths18 = {0.80F,0.20F,0.50F,0.40F};
	   	     	PdfPTable Table16 = new PdfPTable(ContactAddrWidths18);
	            Table16.setWidthPercentage(100);
	            Table16.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table16.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table16.addCell(new Phrase(new Chunk("Invoice Due Date :",black_bold)));
	
	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk(inv_due_dt,black_bold)));
	            
	            float[] ContactAddrWidths20 = {0.08F,0.20F,0.20F,0.20F,0.20F,0.20F,0.20F};
	   	     	PdfPTable Table18 = new PdfPTable(ContactAddrWidths20);
	            Table18.setWidthPercentage(100);
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setColspan(1);
	            Table18.addCell(new Phrase(new Chunk("Sr.No.",small_black_bold)));
	
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setColspan(3);
	            Table18.addCell(new Phrase(new Chunk("Description",small_black_bold)));
	
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setColspan(1);
	            Table18.addCell(new Phrase(new Chunk("Currency",small_black_bold)));
	            
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setColspan(1);
	            Table18.addCell(new Phrase(new Chunk("Rate",small_black_bold)));
	
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setColspan(1);
	            Table18.addCell(new Phrase(new Chunk("Amount",small_black_bold)));

	            
	            String curr1 = "",curr2="",curr3="";
	            if(currency.equals("2"))
	            {
	            	curr1 = "USD";
	            	curr2 = "INR/USD";
	            	curr3 = "INR";
	            }
	            int sr=1;
	            float[] ContactAddrWidths21 = {0.08F,0.20F,0.20F,0.20F,0.20F,0.20F,0.20F};
	   	     	PdfPTable Table19 = new PdfPTable(ContactAddrWidths21);
	            Table19.setWidthPercentage(100);
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table19.getDefaultCell().setColspan(1);
	            Table19.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table19.getDefaultCell().setColspan(3);
	            Table19.addCell(new Phrase(new Chunk(desc_item+"\n\nFor the Following Cargoes received in the month of "+dateUtil.getShortMonthName(inv_dt)+" "+inv_dt.split("/")[2],small_black_normal)));
	
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table19.getDefaultCell().setColspan(1);
	            Table19.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table19.getDefaultCell().setColspan(1);
	            Table19.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table19.getDefaultCell().setColspan(1);
	            Table19.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            
	            sr += 1;
	            float[] ContactAddrWidths28 = {0.08F,0.20F,0.20F,0.20F,0.20F,0.20F,0.20F};
	   	     	PdfPTable Table26 = new PdfPTable(ContactAddrWidths28);
	            Table26.setWidthPercentage(100);
	            Table26.getDefaultCell().setBorder(Rectangle.TOP);
	            Table26.getDefaultCell().setBorder(Rectangle.RIGHT);
	            Table26.getDefaultCell().setBorder(Rectangle.LEFT);
	            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table26.getDefaultCell().setColspan(1);
	            Table26.getDefaultCell().setRowspan(VSHIP_NAME.size()+1);
	            Table26.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table26.getDefaultCell().setBorder(Rectangle.BOX);
	            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table26.getDefaultCell().setColspan(1);
	            Table26.addCell(new Phrase(new Chunk("Cargo Date",small_black_bold)));
	
	            Table26.getDefaultCell().setBorder(Rectangle.BOX);
	            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table26.getDefaultCell().setColspan(1);
	            Table26.addCell(new Phrase(new Chunk("Cargo Name",small_black_bold)));
	            
	            Table26.getDefaultCell().setBorder(Rectangle.BOX);
	            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table26.getDefaultCell().setColspan(1);
	            Table26.addCell(new Phrase(new Chunk("MMBTU",small_black_bold)));
	            
	            Table26.getDefaultCell().setBorder(Rectangle.BOX);
	            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table26.getDefaultCell().setColspan(1);
	            Table26.addCell(new Phrase(new Chunk("",small_black_bold)));
	
	            Table26.getDefaultCell().setBorder(Rectangle.BOX);
	            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT); 
	            Table26.getDefaultCell().setColspan(1); 
	            Table26.addCell(new Phrase(new Chunk("",small_black_bold)));
	            
	            Table26.getDefaultCell().setBorder(Rectangle.BOX);
	            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT); 
	            Table26.getDefaultCell().setColspan(1); 
	            Table26.addCell(new Phrase(new Chunk("",small_black_bold)));
	            
	            for(int j=0; j<VSHIP_NAME.size(); j++)
	            {
	            	Table26.getDefaultCell().setBorder(Rectangle.RIGHT);
	            	Table26.getDefaultCell().setBorder(Rectangle.LEFT);
		            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            Table26.getDefaultCell().setColspan(1);
		            Table26.addCell(new Phrase(new Chunk("",small_black_bold)));
	            	
	            	Table26.getDefaultCell().setBorder(Rectangle.BOX);
		            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table26.getDefaultCell().setColspan(1);
		            Table26.addCell(new Phrase(new Chunk(""+VCARGO_DT.elementAt(j),small_black_normal)));
		
		            Table26.getDefaultCell().setBorder(Rectangle.BOX);
		            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table26.getDefaultCell().setColspan(1);
		            Table26.addCell(new Phrase(new Chunk(""+VSHIP_NAME.elementAt(j),small_black_normal)));
		            
		            Table26.getDefaultCell().setBorder(Rectangle.BOX);
		            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table26.getDefaultCell().setColspan(1);
		            Table26.addCell(new Phrase(new Chunk(""+VQTY_MMBTU.elementAt(j),small_black_normal)));
		            
		            Table26.getDefaultCell().setBorder(Rectangle.BOX);
		            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table26.getDefaultCell().setColspan(1);
		            Table26.addCell(new Phrase(new Chunk("USD",small_black_normal)));
		
		            Table26.getDefaultCell().setBorder(Rectangle.BOX);
		            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT); 
		            Table26.getDefaultCell().setColspan(1); 
		            Table26.addCell(new Phrase(new Chunk(""+VRATE.elementAt(j),small_black_normal)));
		            
		            Table26.getDefaultCell().setBorder(Rectangle.BOX);
		            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT); 
		            Table26.getDefaultCell().setColspan(1); 
		            Table26.addCell(new Phrase(new Chunk(""+VCARGO_AMT.elementAt(j),small_black_normal)));
	            }
	            
	            
	            sr += 1;
	            float[] ContactAddrWidths19 = {0.08F,0.20F,0.20F,0.20F,0.20F,0.20F,0.20F};
	   	     	PdfPTable Table17 = new PdfPTable(ContactAddrWidths19);
	            Table17.setWidthPercentage(100);
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setColspan(1);
	            Table17.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table17.getDefaultCell().setColspan(3);
	            Table17.addCell(new Phrase(new Chunk("Total Charges",small_black_bold)));
	
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setColspan(1);
	            Table17.addCell(new Phrase(new Chunk(curr1,small_black_bold)));
	            
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setColspan(1);
	            Table17.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT); 
	            Table17.getDefaultCell().setColspan(1); 
	            Table17.addCell(new Phrase(new Chunk(es.formatAmount(sale_amt),small_black_bold)));
	            
	            if (!currency.equals("1")) {
					sr += 1;
				}
				float[] ContactAddrWidths22 = {0.08F,0.20F,0.20F,0.20F,0.20F,0.20F,0.20F};
	   	     	PdfPTable Table20 = new PdfPTable(ContactAddrWidths22);
	            Table20.setWidthPercentage(100);
	            Table20.getDefaultCell().setBorder(Rectangle.BOX);
	            Table20.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table20.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table20.getDefaultCell().setColspan(1);
	            Table20.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table20.getDefaultCell().setBorder(Rectangle.BOX);
	            Table20.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table20.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table20.getDefaultCell().setColspan(3);
	            Table20.addCell(new Phrase(new Chunk("Exchange Rate : "+nf2.format(Double.parseDouble(exchang_val))+" ("+curr2+") dated "+exchng_eff_dt,small_black_normal)));
	
	            Table20.getDefaultCell().setBorder(Rectangle.BOX);
	            Table20.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table20.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table20.getDefaultCell().setColspan(1);
	            Table20.addCell(new Phrase(new Chunk(curr2,small_black_bold)));
	            
	            Table20.getDefaultCell().setBorder(Rectangle.BOX);
	            Table20.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table20.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table20.getDefaultCell().setColspan(1);
	            Table20.addCell(new Phrase(new Chunk("",small_black_bold)));
	
	            Table20.getDefaultCell().setBorder(Rectangle.BOX);
	            Table20.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table20.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table20.getDefaultCell().setColspan(1);
	            Table20.addCell(new Phrase(new Chunk(nf2.format(Double.parseDouble(exchang_val)),small_black_normal)));
	            
	            sr+=1;
	            float[] ContactAddrWidths23 = {0.08F,0.20F,0.20F,0.20F,0.20F,0.20F,0.20F};
	   	     	PdfPTable Table21 = new PdfPTable(ContactAddrWidths23);
	            Table21.setWidthPercentage(100);
	            Table21.getDefaultCell().setBorder(Rectangle.BOX);
	            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table21.getDefaultCell().setColspan(1);
	            Table21.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table21.getDefaultCell().setBorder(Rectangle.BOX);
	            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table21.getDefaultCell().setColspan(3);
	            Table21.addCell(new Phrase(new Chunk("Total Amount ",small_black_bold)));
	
	            Table21.getDefaultCell().setBorder(Rectangle.BOX);
	            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table21.getDefaultCell().setColspan(1);
	            Table21.addCell(new Phrase(new Chunk(curr3,small_black_bold)));
	            
	            Table21.getDefaultCell().setBorder(Rectangle.BOX);
	            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table21.getDefaultCell().setColspan(1);
	            Table21.addCell(new Phrase(new Chunk("",small_black_bold)));
	
	            Table21.getDefaultCell().setBorder(Rectangle.BOX);
	            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table21.getDefaultCell().setColspan(1);
	            Table21.addCell(new Phrase(new Chunk(es.formatAmount(gross_amt),small_black_bold)));
	            
	            sr+=1;
	            float[] ContactAddrWidths24 = {0.08F,0.20F,0.20F,0.20F,0.20F,0.20F,0.20F};
	   	     	PdfPTable Table22 = new PdfPTable(ContactAddrWidths24);
	            
	            if (tax_name.contains(",")) 
				{
	            	BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_amt));
					BigDecimal factor = new BigDecimal(2);
	            	double temp_srno=sr;
	            	for(int i = 0;i<tax_name.split(", ").length;i++) 
					{
	            		BigDecimal sub_tax_amt = tax_amt1.divide(factor, 0, RoundingMode.HALF_UP);
	            		temp_srno=temp_srno+0.1;
	            		Table22.setWidthPercentage(100);
	            		Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table22.getDefaultCell().setColspan(1);
			            Table22.addCell(new Phrase(new Chunk(nf0.format(temp_srno),small_black_normal)));
			
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            Table22.getDefaultCell().setColspan(3);
			            Table22.addCell(new Phrase(new Chunk(tax_name.split(",")[i],small_black_normal)));
			
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table22.getDefaultCell().setColspan(1);
			            Table22.addCell(new Phrase(new Chunk(curr3,small_black_normal)));
			            
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table22.getDefaultCell().setColspan(1);
			            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
			
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            Table22.getDefaultCell().setColspan(1);
			            Table22.addCell(new Phrase(new Chunk(es.formatAmount(""+Double.parseDouble(""+sub_tax_amt)),small_black_normal)));
					}
				}

	            Table22.setWidthPercentage(100);
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table22.getDefaultCell().setColspan(1);
	            Table22.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table22.getDefaultCell().setColspan(3);
	            Table22.addCell(new Phrase(new Chunk("Tax ("+tax_name+")",small_black_bold)));
	
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table22.getDefaultCell().setColspan(1);
	            Table22.addCell(new Phrase(new Chunk(curr3,small_black_bold)));
	            
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table22.getDefaultCell().setColspan(1);
	            Table22.addCell(new Phrase(new Chunk("",small_black_bold)));
	
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table22.getDefaultCell().setColspan(1);
	            Table22.addCell(new Phrase(new Chunk(es.formatAmount(tax_amt),small_black_bold)));
	            
	            sr+=1;
	            float[] ContactAddrWidths25 = {0.08F,0.20F,0.20F,0.20F,0.20F,0.20F,0.20F};
	   	     	PdfPTable Table23 = new PdfPTable(ContactAddrWidths25);
	            Table23.setWidthPercentage(100);
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table23.getDefaultCell().setColspan(1);
	            Table23.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table23.getDefaultCell().setColspan(3);
	            Table23.addCell(new Phrase(new Chunk("Total Amount in INR",small_black_bold)));
	
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table23.getDefaultCell().setColspan(1);
	            Table23.addCell(new Phrase(new Chunk(curr3,small_black_bold)));
	            
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table23.getDefaultCell().setColspan(1);
	            Table23.addCell(new Phrase(new Chunk("",small_black_bold)));
	
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table23.getDefaultCell().setColspan(1);
	            Table23.addCell(new Phrase(new Chunk(es.formatAmount(net_amt),small_black_bold)));
	            
	            float[] ContactAddrWidths26_1 = {0.100F};
	   	     	PdfPTable Table24_1 = new PdfPTable(ContactAddrWidths26_1);
	   	     	Table24_1.setWidthPercentage(100);
	   	     	Table24_1.getDefaultCell().setBorder(Rectangle.BOX);
	   	     	Table24_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	   	     	Table24_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	   	     	Table24_1.addCell(new Phrase(new Chunk("Amount in Words | Rupees "+amt_in_word+" Only",black_bold)));
	            
		     	float[] ContactAddrWidths26 = {0.100F};
	   	     	PdfPTable Table24 = new PdfPTable(ContactAddrWidths26);
	   	     	Table24.setWidthPercentage(100);
	   	     	Table24.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	   	     	Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	   	  		Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	   	  		Table24.addCell(new Phrase(new Chunk("For "+supp_nm,black_bold)));
	
	   	  		float[] ContactAddrWidths27 = {0.100F};
	   	  		PdfPTable Table25 = new PdfPTable(ContactAddrWidths27);
		     	Table25.setWidthPercentage(100);
		     	Table25.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table25.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table25.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table25.addCell(new Phrase(new Chunk("Authorised Signatory",black_bold)));
		     	
		     	PdfPTable BillingFieldsInfoTable81 = new PdfPTable(1);
			     BillingFieldsInfoTable81.setWidthPercentage(20);
			     BillingFieldsInfoTable81.setHorizontalAlignment(Element.ALIGN_LEFT);
			     BillingFieldsInfoTable81.getDefaultCell().setBorder(Rectangle.BOX);
			     BillingFieldsInfoTable81.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			     BillingFieldsInfoTable81.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	
			     PdfFormField field = PdfFormField.createSignature(writer);
			     field.setFieldName(SIGNAME);
	
			     field.setPage();
			     field.setWidget(new Rectangle(72, 732, 144, 780), PdfAnnotation.HIGHLIGHT_NONE);
			     field.setFlags(PdfAnnotation.FLAGS_PRINT);
			     writer.addAnnotation(field);
	
			     PdfPCell sigCell = new PdfPCell();
			     FieldPositioningEvents events = new FieldPositioningEvents(writer, field);
			     sigCell.setCellEvent(events);
			     sigCell.setBorder(Rectangle.NO_BORDER);
			     sigCell.setFixedHeight(50f);
			     BillingFieldsInfoTable81.addCell(sigCell);
			    
			     PdfPTable leftSignTable = new PdfPTable(1);
			     leftSignTable.setWidthPercentage(100);
			     leftSignTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			     leftSignTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	
			     leftSignTable.addCell(Table24);
			     leftSignTable.addCell(BillingFieldsInfoTable81);
			     leftSignTable.addCell(Table25);
	
			     float[] InvoiceDateInfoWidths_qr = {0.30f, 0.20f, 0.40f};
			     PdfPTable InvoiceDateInfoTable_qr = new PdfPTable(InvoiceDateInfoWidths_qr);
	
			     if (!irn_no.equals("") && !qr_code.equals("")) {
	
			         int width = 90;
			         int height = 90;
			         Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
			         hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
	
			         QRCodeWriter qrCodeWriter = new QRCodeWriter();
			         BitMatrix matrix = qrCodeWriter.encode(qr_code, BarcodeFormat.QR_CODE, width, height, hintMap);
	
			         BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	
			         for (int x = 0; x < width; x++) {
			             for (int y = 0; y < height; y++) {
			                 image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
			             }
			         }
	
			         ByteArrayOutputStream baos = new ByteArrayOutputStream();
			         ImageIO.write(image, "png", baos);
			         Image qr_codeimg = Image.getInstance(baos.toByteArray());
			         qr_codeimg.setBorder(Rectangle.NO_BORDER);
			         qr_codeimg.setAlignment(Element.ALIGN_LEFT);
	
			         PdfPCell qrcode_cell = new PdfPCell(qr_codeimg, false);
			         qrcode_cell.setBorder(Rectangle.NO_BORDER);
			         qrcode_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	
			         String char_16 = irn_no.substring(0, 16);
			         String char_32 = irn_no.substring(16, 32);
			         String char_48 = irn_no.substring(32, 48);
			         String char_64 = irn_no.substring(48);
			         String final_irn_no = char_16 + "\n" + char_32 + "\n" + char_48 + "\n" + char_64;
	
			         PdfPTable InvoiceDateInfoTable_qr1 = new PdfPTable(1);
			         InvoiceDateInfoTable_qr1.setWidthPercentage(100);
			         InvoiceDateInfoTable_qr1.getDefaultCell().setBorder(Rectangle.BOX);
			         InvoiceDateInfoTable_qr1.addCell(qrcode_cell);
	
			         PdfPTable InvoiceDateInfoTable_qr11 = new PdfPTable(1);
			         InvoiceDateInfoTable_qr11.setWidthPercentage(100);
			         InvoiceDateInfoTable_qr11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			         InvoiceDateInfoTable_qr11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			         InvoiceDateInfoTable_qr11.addCell(new Phrase(new Chunk("IRN", black_bold)));
			         InvoiceDateInfoTable_qr11.addCell(new Phrase(new Chunk(final_irn_no, small_black_normal)));
	
			         InvoiceDateInfoTable_qr.setWidthPercentage(100);
			         InvoiceDateInfoTable_qr.getDefaultCell().setBorder(Rectangle.BOX);
			         InvoiceDateInfoTable_qr.addCell(InvoiceDateInfoTable_qr11);
	
			         InvoiceDateInfoTable_qr.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			         InvoiceDateInfoTable_qr.addCell("");
	
			         InvoiceDateInfoTable_qr.addCell(InvoiceDateInfoTable_qr1);
			     }
	
			     PdfPTable finalRow = new PdfPTable(3);
			     finalRow.setWidthPercentage(100);
			     finalRow.setWidths(new float[]{0.3f, 0.2f ,0.5f});
			     finalRow.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	
			     finalRow.addCell(leftSignTable);
			     finalRow.addCell("");
			     finalRow.addCell(InvoiceDateInfoTable_qr);
		     	
		     	float[] ContactAddrWidths30 = {0.100F};
	   	  		PdfPTable Table28 = new PdfPTable(ContactAddrWidths30);
		     	Table28.setWidthPercentage(100);
		     	Table28.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table28.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table28.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table28.addCell(new Phrase(new Chunk("Notes:",black_bold)));
		     	
		     	float[] ContactAddrWidths31 = {0.01F,0.200F};
	   	  		PdfPTable Table29 = new PdfPTable(ContactAddrWidths31);
		     	Table29.setWidthPercentage(100);
		     	Table29.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table29.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table29.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table29.addCell(new Phrase(new Chunk("1. ",small_black_normal)));
		     	
		     	Table29.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table29.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table29.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table29.addCell(new Phrase(new Chunk(remark1,small_black_normal)));
		     	
		     	float[] ContactAddrWidths32 = {0.01F,0.200F};
	   	  		PdfPTable Table30 = new PdfPTable(ContactAddrWidths32);
		     	Table30.setWidthPercentage(100);
		     	Table30.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table30.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table30.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table30.addCell(new Phrase(new Chunk("2. ",small_black_normal)));
		     	
		     	Table30.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table30.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table30.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table30.addCell(new Phrase(new Chunk(remark2,small_black_normal)));
		     	
		     	float[] ContactAddrWidths33 = {0.01F,0.200F};
	   	  		PdfPTable Table31 = new PdfPTable(ContactAddrWidths33);
		     	Table31.setWidthPercentage(100);
		     	Table31.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table31.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table31.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table31.addCell(new Phrase(new Chunk("3. ",small_black_normal)));
		     	
		     	Table31.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table31.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table31.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table31.addCell(new Phrase(new Chunk(remark3,small_black_normal)));

				document.add(mainTable);
				document.add(new Paragraph("  "));
				document.add(Table3);
				document.add(Table4);
				document.add(Table5);
				document.add(Table6);
				document.add(Table7);
				document.add(Table8);
				document.add(Table9);
				document.add(Table10);
				document.add(Table11);
				document.add(Table12);
				document.add(Table13);
				document.add(Table14);
				document.add(Table15);
				document.add(Table16);
				document.add(new Paragraph("  "));
				document.add(Table18);
				document.add(Table19);
				document.add(Table26);
				document.add(Table17);
				if (!currency.equals("1")) 
				{
					document.add(Table20);
				}
				document.add(Table21);
				document.add(Table22);
				document.add(Table23);
				document.add(new Paragraph("  "));
				document.add(Table24_1);
				document.add(new Paragraph("  "));
				document.add(finalRow);
				document.add(Table28);
				document.add(Table29);
				document.add(Table30);
				document.add(Table31);
				
	            document.close();
	            if(is_print.equals("1"))
				{
		            File isPDFexist = new File(path);
					if(isPDFexist.exists())
					{
						int update_flag=0;
						int pdf_entry=0;
						
						int cont=0;
						queryString="UPDATE FMS_OTH_INVOICE_MST SET PDF_INV_DTL=? ";
							if(print_pdf_type.equals("O"))
							{
								queryString+= ",PRINT_BY_ORI=?,PRINT_DT_ORI=SYSDATE ";
							}
							else if(print_pdf_type.equals("D"))
							{
								queryString+= ",PRINT_BY_DUP=?,PRINT_DT_DUP=SYSDATE ";
							}
							queryString+= "WHERE COMPANY_CD=? AND INVOICE_NO=? AND INVOICE_TYPE=? AND INVOICE_SEQ = ? AND SUPPLIER_CD = ?";
							
						stmt=conn.prepareStatement(queryString);
						stmt.setString(++cont, print_pdf_type);
						if(print_pdf_type.equals("O"))
						{
							stmt.setString(++cont, emp_cd);
						}
						else if(print_pdf_type.equals("D"))
						{
							stmt.setString(++cont, emp_cd);
						}
						stmt.setString(++cont, comp_cd);
						stmt.setString(++cont, inv_no);
						stmt.setString(++cont, inv_type);
						stmt.setString(++cont, inv_seq);
						stmt.setString(++cont, supp_cd);
						update_flag=stmt.executeUpdate();
						stmt.close();
						
						int count=0;
				        queryString1="SELECT COUNT(*) "
				        		+ "FROM FMS_OTH_INV_FILE_DTL "
				        		+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? "
				        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND SUPPLIER_CD =?";
				        stmt1=conn.prepareStatement(queryString1);
				        stmt1.setString(1, comp_cd);
				        stmt1.setString(2, inv_type);
				        stmt1.setString(3, inv_seq);
				        stmt1.setString(4, fin_yr);
				        stmt1.setString(5, print_pdf_type);
				        stmt1.setString(6, supp_cd);
				        rset1=stmt1.executeQuery();
				        if(rset1.next())
				        {
				        	count=rset1.getInt(1);
				        }
				        rset1.close();
				        stmt1.close();
				        
				        if(count > 0)
				        {
				        	queryString1="UPDATE FMS_OTH_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
				        			+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? "
					        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND SUPPLIER_CD =?";
				        	stmt1=conn.prepareStatement(queryString1);
				        	stmt1.setString(1, file_nm);
				        	stmt1.setString(2, emp_cd);
				        	stmt1.setString(3, comp_cd);
				 	        stmt1.setString(4, inv_type);
				 	        stmt1.setString(5, inv_seq);
				 	        stmt1.setString(6, fin_yr);
				 	        stmt1.setString(7, print_pdf_type); 
				 	        stmt1.setString(8, supp_cd); 
				 	        pdf_entry=stmt1.executeUpdate();
				        	
				        	stmt1.close();
				        }
				        else
				        {
				        	queryString1="INSERT INTO FMS_OTH_INV_FILE_DTL(COMPANY_CD,SUPPLIER_CD,INVOICE_SEQ,INVOICE_TYPE,FINANCIAL_YEAR,PDF_TYPE,"
				        			+ "FILE_NAME,ENT_BY,ENT_DT) "
				        			+ "VALUES(?,?,?,?,?,?,"
				        			+ "?,?,SYSDATE)";
				        	stmt1=conn.prepareStatement(queryString1);
				        	stmt1.setString(1, comp_cd);
				        	stmt1.setString(2, supp_cd);
				 	        stmt1.setString(3, inv_seq);
				 	        stmt1.setString(4, inv_type);
				 	        stmt1.setString(5, fin_yr);
				 	        stmt1.setString(6, print_pdf_type);
				 	        stmt1.setString(7, file_nm);
				        	stmt1.setString(8, emp_cd);
				        	pdf_entry=stmt1.executeUpdate();
				        	
				        	stmt1.close();
				        }
				        
				        if(pdf_entry == 0 || update_flag == 0)
				        {
				        	conn.rollback();
				        	deletePdfFile(path);
				        	msg = "Failed! - PDF "+file_nm+" for PFA Fees Invoice with Invoice No. "+inv_no+" Generation Failed!";
				        	msg_type="E";
				        }
				        else
				        {
				        	conn.commit();
				        	msg = "Successful! - PDF "+file_nm+" for PFA Fees Invoice with Invoice No. "+inv_no+" Generated Successfully!";
				        	msg_type="S";
				        }
					}
					else
					{
						msg="Failed! - PDF for PFA Fees Invoice with Invoice No. "+inv_no+" Generation Failed!";
						msg_type="E";
					}
				}
			}
		}
		catch(Exception e)
		{
			conn.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			
			document.close();
			deletePdfFile(path);
			
			msg = "Error in Exception! - PDF for PFA Fees Invoice with Invoice No. "+inv_no+" Generation Failed!";
			msg_type="E";	
		}
		finally
		{
			document.close();
		}
		
		if(is_print.equals("1"))
		{
			try
			{
				new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, "", "", msg);  	
			}
			catch(Exception infoLogger)
			{
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
			}
		}
	}
	
	public void pdfNPR() throws SQLException
	{
		String function_nm="pdfNPR()";
		
		String emp_nm ="";
		String ip = "";
		msg="";
		//String msg_content="";
		String path ="";
		
		Rectangle pageSize = new Rectangle(595, 842);
		
		pageSize.setBackgroundColor(BaseColor.WHITE);
		
		Document document = new Document(pageSize);
		try
		{
			HttpSession session = request.getSession();
			emp_nm = (String)session.getAttribute("emp_nm")==null?"":(String)session.getAttribute("emp_nm");
			ip = (String)session.getAttribute("ip")==null?"":(String)session.getAttribute("ip");
			
			String company_nm = ""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");
			String company_abbr = ""+session.getAttribute("comp_abbr")==null?"":""+session.getAttribute("comp_abbr");
			
			int pdfGenCount=0;
			
			if(is_print.equals("1"))
	        {
				queryString = "SELECT COUNT(*) "
							+ "FROM FMS_OTH_INVOICE_MST A ,FMS_OTH_INV_FILE_DTL B "
							+ "WHERE A.COMPANY_CD=? AND A.INVOICE_NO = ? AND A.INVOICE_TYPE = ? ";
							if(print_pdf_type.equals("O"))
							{
								queryString+= "AND A.PRINT_BY_ORI IS NOT NULL AND A.PRINT_DT_ORI IS NOT NULL ";
							}
							else if(print_pdf_type.equals("T"))
							{
								queryString+= "AND A.PRINT_BY_TRI IS NOT NULL AND A.PRINT_DT_TRI IS NOT NULL ";
							}
							else if(print_pdf_type.equals("D"))
							{
								queryString+= "AND A.PRINT_BY_DUP IS NOT NULL AND A.PRINT_DT_DUP IS NOT NULL ";
							}
				queryString += "AND B.PDF_TYPE=? AND B.INVOICE_TYPE = ? AND B.FILE_NAME IS NOT NULL "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.INVOICE_TYPE = B.INVOICE_TYPE AND A.SUPPLIER_CD = B.SUPPLIER_CD";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_no);
				stmt.setString(3, inv_type);
				stmt.setString(4, print_pdf_type);
				stmt.setString(5, inv_type);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					pdfGenCount=rset.getInt(1);
				}
				rset.close();
				stmt.close();
	        }
			else
			{
				pdfGenCount=0;
			}
			
	
			if(pdfGenCount>0)
			{
				msg="Failed! - PDF for NPR with Invoice No. "+inv_no+" already Generated!";
	        	msg_type="E";
	        }
			else
			{
				
				getInvoiceDtls();
				getSupplierDtl();
				getVendorDtl();
				
				
				String irn_no="";
				String qr_code="";
				queryString1="SELECT IRN_NO,SIGN_QR_CODE "
						+ "FROM FMS_OTH_INV_IRN_DTL "
						+ "WHERE COMPANY_CD=? AND INVOICE_NO=? ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, inv_no);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					irn_no=rset1.getString(1)==null?"":rset1.getString(1);
					qr_code=rset1.getString(2)==null?"":rset1.getString(2);
				}
				rset1.close();
				stmt1.close();
				
				
				
				String print_pdf_type_nm="ORIGINAL";
				if(print_pdf_type.equals("O"))
				{
					print_pdf_type_nm="ORIGINAL";
				}
				else if(print_pdf_type.equals("D"))
				{
					print_pdf_type_nm="DUPLICATE";
				}
	
				String appPath = request.getServletContext().getRealPath("");
	
				String main_folder="";
				if(!comp_cd.equals(""))
				{
					main_folder=CommonVariable.work_dir+comp_cd;
				}
				
				String sub_folder="unsigned_pdf";
				String sub_folder2="ig_invoice";
				
				String temp_path=appPath+File.separator+main_folder+File.separator+sub_folder+File.separator+sub_folder2;
				File main_folderDir = new File(temp_path);
		        if(!main_folderDir.exists())
		        {
		        	main_folderDir.mkdirs();
		        }

		        if(is_print.equals("1"))
		        {
		        	file_nm=print_pdf_type+"-"+supp_abbr+"-"+inv_type+"-"+inv_no.replace("/", "-")+".pdf";
		        }
		        else
		        {
		        	file_nm="temp.pdf";
		        }
		        
				path = appPath+"/"+main_folder+"/"+sub_folder+"/"+sub_folder2+"/"+file_nm;
				PdfWriter writer = PdfWriter.getInstance(document,new FileOutputStream(path));
	
				document.open();
				document.setPageSize(pageSize);
	            document.newPage();
	
	            String context_nm = request.getContextPath();
				String server_nm = request.getServerName();
				String server_port = ""+request.getServerPort();
	
				file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm;
	
				Font small_black_normal = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
				Font small_black_normal_10 = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
				Font small_black_normal_14 = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.NORMAL, BaseColor.BLACK);
				Font small_black_normal_12 = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
				Font small_black_bold = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
	            Font black_bold = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
	            Font black_bold1 = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
	            
	            PdfPCell company_logo_cell = new PdfPCell();
	            if(!comp_logo.equals(""))
	            {
					String file_path = request.getRealPath("/"+CommonVariable.company_logo_path+"/"+comp_logo);
	            	Image company_logo = Image.getInstance(file_path);
		            company_logo.setBorder(Rectangle.NO_BORDER);
		            company_logo.scaleAbsolute(44,40);
		            company_logo_cell = new PdfPCell(company_logo,false);
		            company_logo_cell.setBorder(Rectangle.NO_BORDER);
		            company_logo_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            }
	            
	            float[] mainWidths = {10f, 70f, 15f};  
	            PdfPTable mainTable = new PdfPTable(mainWidths);
	            mainTable.setWidthPercentage(100);
	            mainTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

	            PdfPCell logoCell = new PdfPCell(company_logo_cell);
	            logoCell.setBorder(Rectangle.NO_BORDER);
	            logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            logoCell.setPaddingLeft(10);
	            mainTable.addCell(logoCell);
	            
	            PdfPTable centerTable = new PdfPTable(1);
	            centerTable.setWidthPercentage(100);
	            centerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            centerTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            centerTable.addCell(new Phrase(new Chunk(print_pdf_type_nm, small_black_normal_10)));
	            centerTable.addCell(new Phrase(new Chunk(supp_nm, black_bold1)));
	            centerTable.addCell(new Phrase(new Chunk("TAX INVOICE", small_black_normal_12)));
	            String info = "Tax invoice issued under Rule 46 of the Central Goods and Services Tax Rules, 2017";
	            centerTable.addCell(new Phrase(new Chunk(info, small_black_normal)));

	            PdfPCell centerCell = new PdfPCell(centerTable);
	            centerCell.setBorder(Rectangle.NO_BORDER);
	            centerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            mainTable.addCell(centerCell);

	            PdfPCell emptyCell = new PdfPCell(new Phrase(""));
	            emptyCell.setBorder(Rectangle.NO_BORDER);
	            mainTable.addCell(emptyCell);
	            
	            float[] ContactAddrWidths5 = {0.80f,0.10F,0.20F,0.80F};
	   	     	PdfPTable Table3 = new PdfPTable(ContactAddrWidths5);
	            Table3.setWidthPercentage(100);
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("Registered Office: ",small_black_bold)));
	            
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("To:",small_black_bold)));
	
				float[] ContactAddrWidths6 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table4 = new PdfPTable(ContactAddrWidths6);
	            Table4.setWidthPercentage(100);
	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk(supp_nm,small_black_bold)));
	            
	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk(""+vendor_name+",",small_black_bold)));
	
	            float[] ContactAddrWidths7 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table5 = new PdfPTable(ContactAddrWidths7);
	            Table5.setWidthPercentage(100);
	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk(supp_addr+" "+supp_city,small_black_normal)));
	            
	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk(vend_abbr+" "+vend_city,small_black_normal)));
	            
	
	            float[] ContactAddrWidths8 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table6 = new PdfPTable(ContactAddrWidths8);
	            Table6.setWidthPercentage(100);
	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table6.addCell(new Phrase(new Chunk(supp_state+"-"+supp_pin,small_black_normal)));
	
	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table6.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_pin_no.equals("")) 
	            {
	            	Table6.addCell(new Phrase(new Chunk(vend_state + "-" + vend_pin_no, small_black_normal)));
				}
	            else 
	            {
	            	Table6.addCell(new Phrase(new Chunk(vend_state, small_black_normal)));
	            }
	            
	           	float[] ContactAddrWidths9 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table7 = new PdfPTable(ContactAddrWidths9);
	            Table7.setWidthPercentage(100);
	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk("State: "+supp_state,small_black_normal)));
	
	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk("State: "+vend_state,small_black_normal)));
	            
	            float[] ContactAddrWidths10 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table8 = new PdfPTable(ContactAddrWidths10);
	            Table8.setWidthPercentage(100);
	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table8.addCell(new Phrase(new Chunk("State Code: "+supp_state_tin,small_black_normal)));
	
	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table8.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_state_tin.equals("")) 
	            {
					Table8.addCell(new Phrase(new Chunk("State Code: " + vend_state_tin, small_black_normal)));
				}
	            else 
	            {
					Table8.addCell(new Phrase(new Chunk("", small_black_normal)));
	            }
	            
	            float[] ContactAddrWidths11 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table9 = new PdfPTable(ContactAddrWidths11);
	            Table9.setWidthPercentage(100);
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table9.addCell(new Phrase(new Chunk("GSTIN: "+supp_gstin_no,small_black_normal)));
	
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table9.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_gstin_no.equals("")) 
	            {
					Table9.addCell(new Phrase(new Chunk("GSTIN: " + vend_gstin_no, small_black_normal)));
				}
	            else 
	            {
	            	Table9.addCell(new Phrase(new Chunk("", small_black_normal)));
	            }

	            float[] ContactAddrWidths12 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table10 = new PdfPTable(ContactAddrWidths12);
	            Table10.setWidthPercentage(100);
	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table10.addCell(new Phrase(new Chunk("PAN: "+supp_pan_no,small_black_normal)));
	
	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table10.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_pan_no.equals("")) 
	            {
					Table10.addCell(new Phrase(new Chunk("PAN: " + vend_pan_no, small_black_normal)));
				}
	            else 
	            {
	            	Table10.addCell(new Phrase(new Chunk("", small_black_normal)));
	            }
	            
	            float[] ContactAddrWidths13 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table11 = new PdfPTable(ContactAddrWidths13);
	            Table11.setWidthPercentage(100);
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("SAC: "+sac_code,small_black_normal)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            float[] ContactAddrWidths14 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table12 = new PdfPTable(ContactAddrWidths14);
	            Table12.setWidthPercentage(100);
	            Table12.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table12.addCell(new Phrase(new Chunk("Description of Service: "+sac_des,small_black_normal)));
	
	            Table12.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table12.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table12.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table12.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            float[] ContactAddrWidths15 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table13 = new PdfPTable(ContactAddrWidths15);
	            Table13.setWidthPercentage(100);
	            Table13.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table13.addCell(new Phrase(new Chunk("Place of Supply: "+supp_state,small_black_normal)));
	
	            Table13.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table13.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table13.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table13.addCell(new Phrase(new Chunk("",small_black_normal)));
            
	            float[] ContactAddrWidths16 = {0.80F,0.20F,0.50F,0.40F};
	   	     	PdfPTable Table14 = new PdfPTable(ContactAddrWidths16);
	            Table14.setWidthPercentage(100);
	            Table14.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table14.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table14.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table14.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table14.addCell(new Phrase(new Chunk("Invoice No :",black_bold)));
	
	            Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table14.addCell(new Phrase(new Chunk(inv_no,black_bold)));
	            
	            float[] ContactAddrWidths17 = {0.80F,0.20F,0.50F,0.40F};
	   	     	PdfPTable Table15 = new PdfPTable(ContactAddrWidths17);
	            Table15.setWidthPercentage(100);
	            Table15.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table15.addCell(new Phrase(new Chunk("Invoice Date :",black_bold)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk(inv_dt,black_bold)));

	            float[] ContactAddrWidths18 = {0.80F,0.20F,0.50F,0.40F};
	   	     	PdfPTable Table16 = new PdfPTable(ContactAddrWidths18);
	            Table16.setWidthPercentage(100);
	            Table16.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table16.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table16.addCell(new Phrase(new Chunk("Vendor/Supplier Inv Ref No :",black_bold)));
	
	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk(ref_no,black_bold)));
	            
	            float[] ContactAddrWidths19 = {0.80F,0.20F,0.50F,0.40F};
	   	     	PdfPTable Table17 = new PdfPTable(ContactAddrWidths19);
	            Table17.setWidthPercentage(100);
	            Table17.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table17.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table17.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table17.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table17.addCell(new Phrase(new Chunk("Pace WO/PO No: ",black_bold)));
	
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table17.addCell(new Phrase(new Chunk(pacer_no,black_bold)));
	                      
	            float[] ContactAddrWidths20 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table18 = new PdfPTable(ContactAddrWidths20);
	            Table18.setWidthPercentage(100);
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk("Sr.No.",small_black_bold)));
	
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk("Description",small_black_bold)));
	
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk("Currency",small_black_bold)));
	
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk("Amount",small_black_bold)));

	            
	            String curr1 = "",curr2="",curr3="";
	            if(currency.equals("1")) 
	            {
	            	curr1 = "INR";
	            	curr2 = "INR";
	            	curr3 = "INR";
	            }
	            else if(currency.equals("2"))
	            {
	            	curr1 = "USD";
	            	curr2 = "INR/USD";
	            	curr3 = "INR";
	            }
	            
	            int sr=1;
	            float[] ContactAddrWidths21 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table19 = new PdfPTable(ContactAddrWidths21);
	            Table19.setWidthPercentage(100);
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table19.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table19.addCell(new Phrase(new Chunk(desc_item,small_black_normal)));
	
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table19.addCell(new Phrase(new Chunk(curr1,small_black_normal)));
	
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table19.addCell(new Phrase(new Chunk(es.formatAmount(sale_amt),small_black_normal)));
	            
	            
	            if (!currency.equals("1")) {
					sr += 1;
				}
				float[] ContactAddrWidths22 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table20 = new PdfPTable(ContactAddrWidths22);
	            Table20.setWidthPercentage(100);
	            Table20.getDefaultCell().setBorder(Rectangle.BOX);
	            Table20.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table20.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table20.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table20.getDefaultCell().setBorder(Rectangle.BOX);
	            Table20.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table20.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table20.addCell(new Phrase(new Chunk("Exchange Rate : "+exchng_rate+" ("+curr2+") dated "+exchng_eff_dt,small_black_normal)));
	
	            Table20.getDefaultCell().setBorder(Rectangle.BOX);
	            Table20.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table20.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table20.addCell(new Phrase(new Chunk(curr2,small_black_bold)));
	
	            Table20.getDefaultCell().setBorder(Rectangle.BOX);
	            Table20.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table20.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table20.addCell(new Phrase(new Chunk(exchng_rate,small_black_normal)));
	            
	            sr+=1;
	            float[] ContactAddrWidths23 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table21 = new PdfPTable(ContactAddrWidths23);
	            Table21.setWidthPercentage(100);
	            Table21.getDefaultCell().setBorder(Rectangle.BOX);
	            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table21.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table21.getDefaultCell().setBorder(Rectangle.BOX);
	            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table21.addCell(new Phrase(new Chunk("Total Amount ",small_black_bold)));
	
	            Table21.getDefaultCell().setBorder(Rectangle.BOX);
	            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table21.addCell(new Phrase(new Chunk(curr3,small_black_bold)));
	
	            Table21.getDefaultCell().setBorder(Rectangle.BOX);
	            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table21.addCell(new Phrase(new Chunk(es.formatAmount(gross_amt),small_black_bold)));
	            
	            sr+=1;
	            float[] ContactAddrWidths24 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table22 = new PdfPTable(ContactAddrWidths24);
	            
	            if (tax_name.contains(",")) 
				{
	            	BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_value));
					BigDecimal factor = new BigDecimal(2);
	            	double temp_srno=sr;
	            	for(int i = 0;i<tax_name.split(", ").length;i++) 
					{
	            		BigDecimal sub_tax_amt = tax_amt1.divide(factor, 0, RoundingMode.HALF_UP);
	            		temp_srno=temp_srno+0.1;
	            		Table22.setWidthPercentage(100);
	            		Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table22.addCell(new Phrase(new Chunk(nf0.format(temp_srno),small_black_normal)));
			
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            Table22.addCell(new Phrase(new Chunk(tax_name.split(",")[i],small_black_normal)));
			
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table22.addCell(new Phrase(new Chunk(curr3,small_black_normal)));
			
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            Table22.addCell(new Phrase(new Chunk(es.formatAmount(""+Double.parseDouble(""+sub_tax_amt)),small_black_normal)));
					}
				}

	            Table22.setWidthPercentage(100);
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table22.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table22.addCell(new Phrase(new Chunk("Tax ("+tax_name+")",small_black_bold)));
	
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table22.addCell(new Phrase(new Chunk(curr3,small_black_bold)));
	
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table22.addCell(new Phrase(new Chunk(es.formatAmount(tax_value),small_black_bold)));
	            
	            sr+=1;
	            float[] ContactAddrWidths25 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table23 = new PdfPTable(ContactAddrWidths25);
	            Table23.setWidthPercentage(100);
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table23.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table23.addCell(new Phrase(new Chunk("Total Amount in INR",small_black_bold)));
	
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table23.addCell(new Phrase(new Chunk(curr3,small_black_bold)));
	
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table23.addCell(new Phrase(new Chunk(es.formatAmount(net_amt),small_black_bold)));
	            
	            float[] ContactAddrWidths26_1 = {0.100F};
	   	     	PdfPTable Table24_1 = new PdfPTable(ContactAddrWidths26_1);
	   	     	Table24_1.setWidthPercentage(100);
	   	     	Table24_1.getDefaultCell().setBorder(Rectangle.BOX);
	   	     	Table24_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	   	     	Table24_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	   	     	Table24_1.addCell(new Phrase(new Chunk("Amount in Words | Rupees "+amt_in_word+" Only",black_bold)));		     	
	   	     	
	   	     	float[] ContactAddrWidths26 = {0.100F};
	   	     	PdfPTable Table24 = new PdfPTable(ContactAddrWidths26);
	   	     	Table24.setWidthPercentage(100);
	   	     	Table24.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	   	     	Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	   	  		Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	   	  		Table24.addCell(new Phrase(new Chunk("For "+supp_nm,black_bold)));
	
	   	  		float[] ContactAddrWidths27 = {0.100F};
	   	  		PdfPTable Table25 = new PdfPTable(ContactAddrWidths27);
		     	Table25.setWidthPercentage(100);
		     	Table25.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table25.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table25.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table25.addCell(new Phrase(new Chunk("Authorised Signatory",black_bold)));
		     	
			     PdfPTable BillingFieldsInfoTable81 = new PdfPTable(1);
			     BillingFieldsInfoTable81.setWidthPercentage(20);
			     BillingFieldsInfoTable81.setHorizontalAlignment(Element.ALIGN_LEFT);
			     BillingFieldsInfoTable81.getDefaultCell().setBorder(Rectangle.BOX);
			     BillingFieldsInfoTable81.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			     BillingFieldsInfoTable81.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	
			     PdfFormField field = PdfFormField.createSignature(writer);
			     field.setFieldName(SIGNAME);
	
			     field.setPage();
			     field.setWidget(new Rectangle(72, 732, 144, 780), PdfAnnotation.HIGHLIGHT_NONE);
			     field.setFlags(PdfAnnotation.FLAGS_PRINT);
			     writer.addAnnotation(field);
	
			     PdfPCell sigCell = new PdfPCell();
			     FieldPositioningEvents events = new FieldPositioningEvents(writer, field);
			     sigCell.setCellEvent(events);
			     sigCell.setBorder(Rectangle.NO_BORDER);
			     sigCell.setFixedHeight(50f);
			     BillingFieldsInfoTable81.addCell(sigCell);
			    
			     PdfPTable leftSignTable = new PdfPTable(1);
			     leftSignTable.setWidthPercentage(100);
			     leftSignTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			     leftSignTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	
			     leftSignTable.addCell(Table24);
			     leftSignTable.addCell(BillingFieldsInfoTable81);
			     leftSignTable.addCell(Table25);
	
			     float[] InvoiceDateInfoWidths_qr = {0.30f, 0.20f, 0.40f};
			     PdfPTable InvoiceDateInfoTable_qr = new PdfPTable(InvoiceDateInfoWidths_qr);
	
			     if (!irn_no.equals("") && !qr_code.equals("")) {
	
			         int width = 90;
			         int height = 90;
			         Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
			         hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
	
			         QRCodeWriter qrCodeWriter = new QRCodeWriter();
			         BitMatrix matrix = qrCodeWriter.encode(qr_code, BarcodeFormat.QR_CODE, width, height, hintMap);
	
			         BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	
			         for (int x = 0; x < width; x++) {
			             for (int y = 0; y < height; y++) {
			                 image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
			             }
			         }
	
			         ByteArrayOutputStream baos = new ByteArrayOutputStream();
			         ImageIO.write(image, "png", baos);
			         Image qr_codeimg = Image.getInstance(baos.toByteArray());
			         qr_codeimg.setBorder(Rectangle.NO_BORDER);
			         qr_codeimg.setAlignment(Element.ALIGN_LEFT);
	
			         PdfPCell qrcode_cell = new PdfPCell(qr_codeimg, false);
			         qrcode_cell.setBorder(Rectangle.NO_BORDER);
			         qrcode_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	
			         String char_16 = irn_no.substring(0, 16);
			         String char_32 = irn_no.substring(16, 32);
			         String char_48 = irn_no.substring(32, 48);
			         String char_64 = irn_no.substring(48);
			         String final_irn_no = char_16 + "\n" + char_32 + "\n" + char_48 + "\n" + char_64;
	
			         PdfPTable InvoiceDateInfoTable_qr1 = new PdfPTable(1);
			         InvoiceDateInfoTable_qr1.setWidthPercentage(100);
			         InvoiceDateInfoTable_qr1.getDefaultCell().setBorder(Rectangle.BOX);
			         InvoiceDateInfoTable_qr1.addCell(qrcode_cell);
	
			         PdfPTable InvoiceDateInfoTable_qr11 = new PdfPTable(1);
			         InvoiceDateInfoTable_qr11.setWidthPercentage(100);
			         InvoiceDateInfoTable_qr11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			         InvoiceDateInfoTable_qr11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			         InvoiceDateInfoTable_qr11.addCell(new Phrase(new Chunk("IRN", black_bold)));
			         InvoiceDateInfoTable_qr11.addCell(new Phrase(new Chunk(final_irn_no, small_black_normal)));
	
			         InvoiceDateInfoTable_qr.setWidthPercentage(100);
			         InvoiceDateInfoTable_qr.getDefaultCell().setBorder(Rectangle.BOX);
			         InvoiceDateInfoTable_qr.addCell(InvoiceDateInfoTable_qr11);
	
			         InvoiceDateInfoTable_qr.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			         InvoiceDateInfoTable_qr.addCell("");
	
			         InvoiceDateInfoTable_qr.addCell(InvoiceDateInfoTable_qr1);
			     }
	
			     PdfPTable finalRow = new PdfPTable(3);
			     finalRow.setWidthPercentage(100);
			     finalRow.setWidths(new float[]{0.3f, 0.2f ,0.5f});
			     finalRow.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	
			     finalRow.addCell(leftSignTable);
			     finalRow.addCell("");
			     finalRow.addCell(InvoiceDateInfoTable_qr);
		     	
		     	float[] ContactAddrWidths30 = {0.100F};
	   	  		PdfPTable Table28 = new PdfPTable(ContactAddrWidths30);
		     	Table28.setWidthPercentage(100);
		     	Table28.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table28.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table28.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table28.addCell(new Phrase(new Chunk("Notes:",black_bold)));
		     	
		     	float[] ContactAddrWidths31 = {0.01F,0.200F};
	   	  		PdfPTable Table29 = new PdfPTable(ContactAddrWidths31);
		     	Table29.setWidthPercentage(100);
		     	Table29.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table29.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table29.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table29.addCell(new Phrase(new Chunk("1. ",small_black_normal)));
		     	
		     	Table29.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table29.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table29.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table29.addCell(new Phrase(new Chunk(remark1,small_black_normal)));

				document.add(mainTable);
				document.add(new Paragraph("  "));
				document.add(Table3);
				document.add(Table4);
				document.add(Table5);
				document.add(Table6);
				document.add(Table7);
				document.add(Table8);
				document.add(Table9);
				document.add(Table10);
				document.add(Table11);
				document.add(Table12);
				document.add(Table13);
				document.add(new Paragraph("  "));
				document.add(Table14);
				document.add(Table15);
				document.add(Table16);
				document.add(Table17);
				document.add(new Paragraph("  "));
				document.add(Table18);
				document.add(Table19);
				if (!currency.equals("1")) 
				{
					document.add(Table20);
				}
				document.add(Table21);
				document.add(Table22);
				document.add(Table23);
				document.add(new Paragraph("  "));
				document.add(Table24_1);
				document.add(new Paragraph("  "));
				document.add(finalRow);
				document.add(Table28);
				document.add(Table29);
				
	            document.close();
	            if(is_print.equals("1"))
				{ 
		            File isPDFexist = new File(path);
					if(isPDFexist.exists())
					{
						int update_flag=0;
						int pdf_entry=0;
						
						int cont=0;
						queryString="UPDATE FMS_OTH_INVOICE_MST SET PDF_INV_DTL=? ";
							if(print_pdf_type.equals("O"))
							{
								queryString+= ",PRINT_BY_ORI=?,PRINT_DT_ORI=SYSDATE ";
							}
							else if(print_pdf_type.equals("D"))
							{
								queryString+= ",PRINT_BY_DUP=?,PRINT_DT_DUP=SYSDATE ";
							}
							queryString+= "WHERE COMPANY_CD=? AND INVOICE_NO=? AND INVOICE_TYPE=? AND INVOICE_SEQ = ? AND SUPPLIER_CD = ?";
							
						stmt=conn.prepareStatement(queryString);
						stmt.setString(++cont, print_pdf_type);
						if(print_pdf_type.equals("O"))
						{
							stmt.setString(++cont, emp_cd);
						}
						else if(print_pdf_type.equals("D"))
						{
							stmt.setString(++cont, emp_cd);
						}
						stmt.setString(++cont, comp_cd);
						stmt.setString(++cont, inv_no);
						stmt.setString(++cont, inv_type);
						stmt.setString(++cont, inv_seq);
						stmt.setString(++cont, supp_cd);
						update_flag=stmt.executeUpdate();
						stmt.close();
						
						int count=0;
				        queryString1="SELECT COUNT(*) "
				        		+ "FROM FMS_OTH_INV_FILE_DTL "
				        		+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? "
				        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND SUPPLIER_CD =?";
				        stmt1=conn.prepareStatement(queryString1);
				        stmt1.setString(1, comp_cd);
				        stmt1.setString(2, inv_type);
				        stmt1.setString(3, inv_seq);
				        stmt1.setString(4, fin_yr);
				        stmt1.setString(5, print_pdf_type);
				        stmt1.setString(6, supp_cd);
				        rset1=stmt1.executeQuery();
				        if(rset1.next())
				        {
				        	count=rset1.getInt(1);
				        }
				        rset1.close();
				        stmt1.close();
				        
				        if(count > 0)
				        {
				        	queryString1="UPDATE FMS_OTH_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
				        			+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? "
					        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND SUPPLIER_CD =?";
				        	stmt1=conn.prepareStatement(queryString1);
				        	stmt1.setString(1, file_nm);
				        	stmt1.setString(2, emp_cd);
				        	stmt1.setString(3, comp_cd);
				 	        stmt1.setString(4, inv_type);
				 	        stmt1.setString(5, inv_seq);
				 	        stmt1.setString(6, fin_yr);
				 	        stmt1.setString(7, print_pdf_type); 
				 	        stmt1.setString(8, supp_cd); 
				 	        pdf_entry=stmt1.executeUpdate();
				        	
				        	stmt1.close();
				        }
				        else
				        {
				        	queryString1="INSERT INTO FMS_OTH_INV_FILE_DTL(COMPANY_CD,SUPPLIER_CD,INVOICE_SEQ,INVOICE_TYPE,FINANCIAL_YEAR,PDF_TYPE,"
				        			+ "FILE_NAME,ENT_BY,ENT_DT) "
				        			+ "VALUES(?,?,?,?,?,?,"
				        			+ "?,?,SYSDATE)";
				        	stmt1=conn.prepareStatement(queryString1);
				        	stmt1.setString(1, comp_cd);
				        	stmt1.setString(2, supp_cd);
				 	        stmt1.setString(3, inv_seq);
				 	        stmt1.setString(4, inv_type);
				 	        stmt1.setString(5, fin_yr);
				 	        stmt1.setString(6, print_pdf_type);
				 	        stmt1.setString(7, file_nm);
				        	stmt1.setString(8, emp_cd);
				        	pdf_entry=stmt1.executeUpdate();
				        	
				        	stmt1.close();
				        }
				        
				        if(pdf_entry == 0 || update_flag == 0)
				        {
				        	conn.rollback();
				        	deletePdfFile(path);
				        	msg = "Failed! - PDF "+file_nm+" for NPR with Invoice No. "+inv_no+" Generation Failed!";
				        	msg_type="E";
				        }
				        else
				        {
				        	conn.commit();
				        	msg = "Successful! - PDF "+file_nm+" for NPR with Invoice No. "+inv_no+" Generated Successfully!";
				        	msg_type="S";
				        }
					}
					else
					{
						msg="Failed! - PDF for NPR with Invoice No. "+inv_no+" Generation Failed!";
						msg_type="E";
					}
				}
			}
		}
		catch(Exception e)
		{
			conn.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			
			document.close();
			deletePdfFile(path);
			
			msg = "Error in Exception! - PDF for NPR with Invoice No. "+inv_no+" Generation Failed!";
			msg_type="E";	
		}
		finally
		{
			document.close();
		}
		
		if(is_print.equals("1"))
		{
			try
			{
				new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, "", "", msg);  	
			}
			catch(Exception infoLogger)
			{
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
			}
		}
	}
	
	//Deep20251115
	public void getInvoiceDtlsAHPLShare()
	{
		
		String function_nm="getInvoiceDtlsAHPLShare()";

		try
		{
			
			queryString = "SELECT A.COMPANY_CD,A.VENDOR_CD,A.SUPPLIER_CD,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),B.VENDOR_SUPP_INV_REF,"
					+ "B.PACER_NO,A.GROSS_AMT,A.TAX_AMT_INR,A.NET_PAYABLE,A.EXCHG_RATE_VALUE,TO_CHAR(A.EXCHG_RATE_DT, 'DD/MM/YYYY'),"
					+ "A.SALE_PRICE_UNIT,B.SAC_CODE,B.TAX_STRUCT_CD,B.ITEM_DESCRIPTION,A.REMARK,A.REMARK1,A.APPROVED_FLAG,A.FINANCIAL_YEAR,"
					+ "A.INVOICE_SEQ,A.SALE_AMT,A.CHECKED_FLAG,A.INVOICE_NO,A.INVOICE_ID_SEQ,TO_CHAR(B.TAX_EFF_DT, 'DD/MM/YYYY'),A.INVOICE_CATEGORY,A.INVOICE_RAISED_IN,"
					+ "TO_CHAR(A.DUE_DT,'DD/MM/YYYY'),A.REMARK2,B.SIGN "
					+ "FROM FMS_OTH_INVOICE_MST A, FMS_OTH_INVOICE_DTL B ";
					
			if(is_print.equals("0"))
			{
				queryString+= "WHERE A.COMPANY_CD = ? AND A.INVOICE_SEQ = ? AND A.INVOICE_TYPE = ? AND A.FINANCIAL_YEAR=? AND A.SUPPLIER_CD=? "
						+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.INVOICE_TYPE = B.INVOICE_TYPE AND A.INV_FLAG = B.INV_FLAG "
						+ "AND A.VENDOR_CD = B.VENDOR_CD AND A.SUPPLIER_CD = B.SUPPLIER_CD AND A.INVOICE_SEQ = B.INVOICE_SEQ AND A.VENDOR_TYPE = ? AND A.INV_FLAG = ?";
			}
			else
			{
				queryString+= "WHERE A.COMPANY_CD = ? AND A.INVOICE_NO = ? AND A.INVOICE_TYPE = ? "
						+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.INVOICE_SEQ = B.INVOICE_SEQ AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.INVOICE_TYPE = B.INVOICE_TYPE AND "
						+ "A.INVOICE_DT = B.EFF_DT AND A.VENDOR_CD = B.VENDOR_CD AND A.SUPPLIER_CD = B.SUPPLIER_CD AND A.INV_FLAG = B.INV_FLAG AND "
						+ "A.VENDOR_TYPE =? AND A.INV_FLAG = ? ";
			}
					
			stmt = conn.prepareStatement(queryString);
			if(is_print.equals("0"))
			{
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_seq);
				stmt.setString(3, inv_type);
				stmt.setString(4, fin_yr);
				stmt.setString(5, supp_cd);
				stmt.setString(6, "V");
				stmt.setString(7, "F");
			}
			else
			{
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_no);
				stmt.setString(3, inv_type);
				stmt.setString(4, "V");
				stmt.setString(5, "F");
			}
			rset = stmt.executeQuery();
			if(rset.next()) 
			{

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
				fin_yr = rset.getString(19) == null ? "" : rset.getString(19);
				inv_seq = rset.getString(20) == null ? "" : rset.getString(20);
				sale_amt = nf.format(Double.parseDouble(rset.getString(21) == null ? "" : rset.getString(21)));
				inv_no = rset.getString(23) == null ? "" : rset.getString(23);
				inv_id_seq = rset.getString(24) == null ? "" : rset.getString(24);
				invoice_category = rset.getString(26) == null ? "" : rset.getString(26);
				invoice_raised_in = rset.getString(27) == null ? "" : rset.getString(27);
				inv_due_dt = rset.getString(28) == null ? "" : rset.getString(28);
				remark3 = rset.getString(29) == null ? "" : rset.getString(29);
				sign = rset.getString(30) == null ? null : rset.getString(30);
				taxValue = rset.getDouble(8);
				
				tax_name=utilBean.getTaxDescr(conn,tax_struct_cd);
				
				if (tax_name.contains(",")) 
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
				

				queryString2 = "SELECT CARGO_AMOUNT,SIGN,AMT_DESCRIPTION,ITEM_AMT "
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
					if (rset2.getString(2)!=null) 
					{
						VGSIGN.add(rset2.getString(2) == null ? "" : rset2.getString(2));
						VGAMT_DES.add(rset2.getString(3) == null ? "" : rset2.getString(3));
						VGITEM_AMT.add(rset2.getString(4) == null ? "" : rset2.getString(4));
					}
				}
				rset2.close();
				stmt2.close();
				
				queryString2 = "SELECT CARGO_AMOUNT,SIGN,AMT_DESCRIPTION,ITEM_AMT "
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
					if (rset2.getString(2)!=null) 
					{
						VLSIGN.add(rset2.getString(2)==null?"":rset2.getString(2));
						VLAMT_DES.add(rset2.getString(3)==null?"":rset2.getString(3));
						VLITEM_AMT.add(rset2.getString(4)==null?"":rset2.getString(4));
					}
				}
				rset2.close();
				stmt2.close();
				
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
	
	public void pdfAHPLShare() throws SQLException
	{
		String function_nm="pdfAHPLShare()";
		
		String emp_nm ="";
		String ip = "";
		msg="";
		//String msg_content="";
		String path ="";
		
		Rectangle pageSize = new Rectangle(595, 842);
		
		pageSize.setBackgroundColor(BaseColor.WHITE);
		
		Document document = new Document(pageSize);
		try
		{
			HttpSession session = request.getSession();
			emp_nm = (String)session.getAttribute("emp_nm")==null?"":(String)session.getAttribute("emp_nm");
			ip = (String)session.getAttribute("ip")==null?"":(String)session.getAttribute("ip");
			
			String company_nm = ""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");
			String company_abbr = ""+session.getAttribute("comp_abbr")==null?"":""+session.getAttribute("comp_abbr");
			
			int pdfGenCount=0;
			
			if(is_print.equals("1"))
	        {
				queryString = "SELECT COUNT(*) "
							+ "FROM FMS_OTH_INVOICE_MST A ,FMS_OTH_INV_FILE_DTL B "
							+ "WHERE A.COMPANY_CD=? AND A.INVOICE_NO = ? AND A.INVOICE_TYPE = ? ";
							if(print_pdf_type.equals("O"))
							{
								queryString+= "AND A.PRINT_BY_ORI IS NOT NULL AND A.PRINT_DT_ORI IS NOT NULL ";
							}
							else if(print_pdf_type.equals("T"))
							{
								queryString+= "AND A.PRINT_BY_TRI IS NOT NULL AND A.PRINT_DT_TRI IS NOT NULL ";
							}
							else if(print_pdf_type.equals("D"))
							{
								queryString+= "AND A.PRINT_BY_DUP IS NOT NULL AND A.PRINT_DT_DUP IS NOT NULL ";
							}
				queryString += "AND B.PDF_TYPE=? AND B.INVOICE_TYPE = ? AND B.FILE_NAME IS NOT NULL "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.INVOICE_TYPE = B.INVOICE_TYPE AND A.SUPPLIER_CD = B.SUPPLIER_CD";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_no);
				stmt.setString(3, inv_type);
				stmt.setString(4, print_pdf_type);
				stmt.setString(5, inv_type);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					pdfGenCount=rset.getInt(1);
				}
				rset.close();
				stmt.close();
	        }
			else
			{
				pdfGenCount = 0;
			}
	
			if(pdfGenCount>0)
			{
				msg="Failed! - PDF for AHPL Revenue Share with Invoice No. "+inv_no+" already Generated!";
	        	msg_type="E";
	        }
			else
			{
				
				getInvoiceDtlsAHPLShare();
				getSupplierDtl();
				getVendorDtl();
				
				String irn_no="";
				String qr_code="";
				queryString1="SELECT IRN_NO,SIGN_QR_CODE "
						+ "FROM FMS_OTH_INV_IRN_DTL "
						+ "WHERE COMPANY_CD=? AND INVOICE_NO=? ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, inv_no);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					irn_no=rset1.getString(1)==null?"":rset1.getString(1);
					qr_code=rset1.getString(2)==null?"":rset1.getString(2);
				}
				rset1.close();
				stmt1.close();
				
				String print_pdf_type_nm="ORIGINAL";
				if(print_pdf_type.equals("O"))
				{
					print_pdf_type_nm="ORIGINAL";
				}
				else if(print_pdf_type.equals("D"))
				{
					print_pdf_type_nm="DUPLICATE";
				}
	
				String appPath = request.getServletContext().getRealPath("");
	
				String main_folder="";
				if(!comp_cd.equals(""))
				{
					main_folder=CommonVariable.work_dir+comp_cd;
				}
				
				String sub_folder="unsigned_pdf";
				String sub_folder2="ig_invoice";
				
				String temp_path=appPath+File.separator+main_folder+File.separator+sub_folder+File.separator+sub_folder2;
				File main_folderDir = new File(temp_path);
		        if(!main_folderDir.exists())
		        {
		        	main_folderDir.mkdirs();
		        }

		        if(is_print.equals("1"))
		        {
		        	file_nm=print_pdf_type+"-"+supp_abbr+"-"+inv_type+"-"+inv_no.replace("/", "-")+".pdf";
		        }
		        else
		        {
		        	file_nm="temp.pdf";
		        }
	
				path = appPath+"/"+main_folder+"/"+sub_folder+"/"+sub_folder2+"/"+file_nm;
				PdfWriter writer = PdfWriter.getInstance(document,new FileOutputStream(path));
	
				document.open();
				document.setPageSize(pageSize);
	            document.newPage();
	
	            String context_nm = request.getContextPath();
				String server_nm = request.getServerName();
				String server_port = ""+request.getServerPort();
	
				file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm;
	
				Font small_black_normal = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
				Font small_black_normal_10 = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
				Font small_black_normal_14 = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.NORMAL, BaseColor.BLACK);
				Font small_black_normal_12 = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
				Font small_black_bold = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
	            Font black_bold = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
	            Font black_bold1 = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
	            
	            PdfPCell company_logo_cell = new PdfPCell();
	            if(!comp_logo.equals(""))
	            {
					String file_path = request.getRealPath("/"+CommonVariable.company_logo_path+"/"+comp_logo);
	            	Image company_logo = Image.getInstance(file_path);
		            company_logo.setBorder(Rectangle.NO_BORDER);
		            company_logo.scaleAbsolute(44,40);
		            company_logo_cell = new PdfPCell(company_logo,false);
		            company_logo_cell.setBorder(Rectangle.NO_BORDER);
		            company_logo_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            }
	            
	            float[] mainWidths = {10f, 70f, 15f};  
	            PdfPTable mainTable = new PdfPTable(mainWidths);
	            mainTable.setWidthPercentage(100);
	            mainTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

	            PdfPCell logoCell = new PdfPCell(company_logo_cell);
	            logoCell.setBorder(Rectangle.NO_BORDER);
	            logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            logoCell.setPaddingLeft(10);
	            mainTable.addCell(logoCell);
	            
	            PdfPTable centerTable = new PdfPTable(1);
	            centerTable.setWidthPercentage(100);
	            centerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            centerTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            centerTable.addCell(new Phrase(new Chunk(print_pdf_type_nm, small_black_normal_10)));
	            centerTable.addCell(new Phrase(new Chunk(supp_nm, black_bold1)));
	            centerTable.addCell(new Phrase(new Chunk("TAX INVOICE", small_black_normal_12)));
	            String info = "Tax invoice issued under Rule 46 of the Central Goods and Services Tax Rules, 2017";
	            centerTable.addCell(new Phrase(new Chunk(info, small_black_normal)));

	            PdfPCell centerCell = new PdfPCell(centerTable);
	            centerCell.setBorder(Rectangle.NO_BORDER);
	            centerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            mainTable.addCell(centerCell);

	            PdfPCell emptyCell = new PdfPCell(new Phrase(""));
	            emptyCell.setBorder(Rectangle.NO_BORDER);
	            mainTable.addCell(emptyCell);
	
	            float[] ContactAddrWidths5 = {0.80f,0.10F,0.20F,0.80F};
	   	     	PdfPTable Table3 = new PdfPTable(ContactAddrWidths5);
	            Table3.setWidthPercentage(100);
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("Registered Office: ",small_black_bold)));
	            
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("To:",small_black_bold)));
	
				float[] ContactAddrWidths6 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table4 = new PdfPTable(ContactAddrWidths6);
	            Table4.setWidthPercentage(100);
	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk(supp_nm,small_black_bold)));
	            
	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk(""+vendor_name+",",small_black_bold)));
	
	            float[] ContactAddrWidths7 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table5 = new PdfPTable(ContactAddrWidths7);
	            Table5.setWidthPercentage(100);
	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk(supp_addr+" "+supp_city,small_black_normal)));
	            
	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk(vend_abbr+" "+vend_city,small_black_normal)));
	            
	
	            float[] ContactAddrWidths8 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table6 = new PdfPTable(ContactAddrWidths8);
	            Table6.setWidthPercentage(100);
	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table6.addCell(new Phrase(new Chunk(supp_state+"-"+supp_pin,small_black_normal)));
	
	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table6.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_pin_no.equals("")) 
	            {
	            	Table6.addCell(new Phrase(new Chunk(vend_state + "-" + vend_pin_no, small_black_normal)));
				}
	            else 
	            {
	            	Table6.addCell(new Phrase(new Chunk(vend_state, small_black_normal)));
	            }
	            
	           	float[] ContactAddrWidths9 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table7 = new PdfPTable(ContactAddrWidths9);
	            Table7.setWidthPercentage(100);
	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk("State: "+supp_state,small_black_normal)));
	
	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk("State: "+vend_state,small_black_normal)));
	            
	            float[] ContactAddrWidths10 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table8 = new PdfPTable(ContactAddrWidths10);
	            Table8.setWidthPercentage(100);
	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table8.addCell(new Phrase(new Chunk("State Code: "+supp_state_tin,small_black_normal)));
	
	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table8.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_state_tin.equals("")) 
	            {
					Table8.addCell(new Phrase(new Chunk("State Code: " + vend_state_tin, small_black_normal)));
				}
	            else 
	            {
					Table8.addCell(new Phrase(new Chunk("", small_black_normal)));
	            }
	            
	            float[] ContactAddrWidths11 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table9 = new PdfPTable(ContactAddrWidths11);
	            Table9.setWidthPercentage(100);
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table9.addCell(new Phrase(new Chunk("GSTIN: "+supp_gstin_no,small_black_normal)));
	
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table9.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_gstin_no.equals("")) 
	            {
					Table9.addCell(new Phrase(new Chunk("GSTIN: " + vend_gstin_no, small_black_normal)));
				}
	            else 
	            {
	            	Table9.addCell(new Phrase(new Chunk("", small_black_normal)));
	            }

	            float[] ContactAddrWidths12 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table10 = new PdfPTable(ContactAddrWidths12);
	            Table10.setWidthPercentage(100);
	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table10.addCell(new Phrase(new Chunk("PAN: "+supp_pan_no,small_black_normal)));
	
	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table10.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_pan_no.equals("")) 
	            {
					Table10.addCell(new Phrase(new Chunk("PAN: " + vend_pan_no, small_black_normal)));
				}
	            else 
	            {
	            	Table10.addCell(new Phrase(new Chunk("", small_black_normal)));
	            }
	            
				float[] ContactAddrWidths13 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table11 = new PdfPTable(ContactAddrWidths13);
	            Table11.setWidthPercentage(100);
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("SAC: "+sac_code,small_black_normal)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            float[] ContactAddrWidths14 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table12 = new PdfPTable(ContactAddrWidths14);
	            Table12.setWidthPercentage(100);
	            Table12.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table12.addCell(new Phrase(new Chunk("Description of Service: "+sac_des,small_black_normal)));
	
	            Table12.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table12.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table12.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table12.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            float[] ContactAddrWidths15 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table13 = new PdfPTable(ContactAddrWidths15);
	            Table13.setWidthPercentage(100);
	            Table13.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table13.addCell(new Phrase(new Chunk("Place of Supply: "+supp_state,small_black_normal)));
	
	            Table13.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table13.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table13.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table13.addCell(new Phrase(new Chunk("",small_black_normal)));
            
	            float[] ContactAddrWidths16 = {0.80F,0.20F,0.50F,0.40F};
	   	     	PdfPTable Table14 = new PdfPTable(ContactAddrWidths16);
	            Table14.setWidthPercentage(100);
	            Table14.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table14.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table14.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table14.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table14.addCell(new Phrase(new Chunk("Invoice No :",black_bold)));
	
	            Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table14.addCell(new Phrase(new Chunk(inv_no,black_bold)));
	            
	            float[] ContactAddrWidths17 = {0.80F,0.20F,0.50F,0.40F};
	   	     	PdfPTable Table15 = new PdfPTable(ContactAddrWidths17);
	            Table15.setWidthPercentage(100);
	            Table15.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table15.addCell(new Phrase(new Chunk("Invoice Date :",black_bold)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk(inv_dt,black_bold)));

	            
	            float[] ContactAddrWidths19 = {0.80F,0.20F,0.50F,0.40F};
	   	     	PdfPTable Table17 = new PdfPTable(ContactAddrWidths19);
	            Table17.setWidthPercentage(100);
	            Table17.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table17.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table17.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table17.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table17.addCell(new Phrase(new Chunk("Invoice Due Date:",black_bold)));
	
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table17.addCell(new Phrase(new Chunk(inv_due_dt,black_bold)));
	                      
	            float[] ContactAddrWidths20 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table18 = new PdfPTable(ContactAddrWidths20);
	            Table18.setWidthPercentage(100);
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk("Sr.No.",small_black_bold)));
	
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk("Description",small_black_bold)));
	
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk("Currency",small_black_bold)));
	
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk("Amount",small_black_bold)));

	            
	            String curr1 = "",curr3="";
	            int count = 0;
	            if(currency.equals("1")) 
	            {
	            	curr1 = "INR";
	            	curr3 = "INR";
	            }
	            
	            int sr=1;
	            float[] ContactAddrWidths21 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table19 = new PdfPTable(ContactAddrWidths21);
	            Table19.setWidthPercentage(100);
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table19.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table19.addCell(new Phrase(new Chunk("Gross Revenue (a)",small_black_normal)));
	
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table19.addCell(new Phrase(new Chunk(curr1,small_black_normal)));
	
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table19.addCell(new Phrase(new Chunk(es.formatAmount(gross_revenue),small_black_normal)));
	            
	            double temp_srno = 0;
            	for(int i = 0;i<VGSIGN.size();i++) 
				{
            		sr+=1;
            		count+=1;
            		Table19.setWidthPercentage(100);
            		Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table19.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
		
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            Table19.addCell(new Phrase(new Chunk(""+VGAMT_DES.elementAt(i)+" (a"+(i+1)+")",small_black_normal)));
		
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table19.addCell(new Phrase(new Chunk(curr3,small_black_normal)));
		
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table19.addCell(new Phrase(new Chunk(es.formatAmount(""+VGITEM_AMT.elementAt(i)),small_black_normal)));
		            gross_formula += VGSIGN.elementAt(i)+" a"+(i+1)+" "; 
				}
	            
	            sr+=1;
	            float[] ContactAddrWidths23 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table21 = new PdfPTable(ContactAddrWidths23);
	            Table21.setWidthPercentage(100);
	            Table21.getDefaultCell().setBorder(Rectangle.BOX);
	            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table21.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table21.getDefaultCell().setBorder(Rectangle.BOX);
	            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table21.addCell(new Phrase(new Chunk("Less: Water Front Royalty(WFR) (b)",small_black_normal)));
	
	            Table21.getDefaultCell().setBorder(Rectangle.BOX);
	            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table21.addCell(new Phrase(new Chunk(curr3,small_black_normal)));
	
	            Table21.getDefaultCell().setBorder(Rectangle.BOX);
	            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table21.addCell(new Phrase(new Chunk(es.formatAmount(gross_less),small_black_normal)));
	            
	            count=0;
            	for(int i = 0;i<VLSIGN.size();i++) 
				{
            		sr+=1;
            		count+=1;
            		Table21.setWidthPercentage(100);
            		Table21.getDefaultCell().setBorder(Rectangle.BOX);
		            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table21.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
		
		            Table21.getDefaultCell().setBorder(Rectangle.BOX);
		            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            Table21.addCell(new Phrase(new Chunk(""+VLAMT_DES.elementAt(i)+" (b"+(i+1)+")",small_black_normal)));
		
		            Table21.getDefaultCell().setBorder(Rectangle.BOX);
		            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table21.addCell(new Phrase(new Chunk(curr3,small_black_normal)));
		
		            Table21.getDefaultCell().setBorder(Rectangle.BOX);
		            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table21.addCell(new Phrase(new Chunk(es.formatAmount(""+VLITEM_AMT.elementAt(i)),small_black_normal)));
		            less_formula += VLSIGN.elementAt(i)+" b"+(i+1)+" ";
				}
	            
	            sr+=1;
	            float[] ContactAddrWidths18 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table16 = new PdfPTable(ContactAddrWidths18);
	            Table16.setWidthPercentage(100);
	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table16.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table16.addCell(new Phrase(new Chunk("Net Revenue Excluding WFR (("+gross_formula+") - ("+less_formula+"))",small_black_normal)));
	
	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table16.addCell(new Phrase(new Chunk(curr3,small_black_normal)));
	
	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk(es.formatAmount(sale_amt),small_black_normal)));
	            
	            sr+=1;
	            float[] ContactAddrWidths22 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table20 = new PdfPTable(ContactAddrWidths22);
	            Table20.setWidthPercentage(100);
	            Table20.getDefaultCell().setBorder(Rectangle.BOX);
	            Table20.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table20.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table20.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table20.getDefaultCell().setBorder(Rectangle.BOX);
	            Table20.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table20.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table20.addCell(new Phrase(new Chunk(desc_item,small_black_normal)));
	
	            Table20.getDefaultCell().setBorder(Rectangle.BOX);
	            Table20.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table20.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table20.addCell(new Phrase(new Chunk(curr3,small_black_normal)));
	
	            Table20.getDefaultCell().setBorder(Rectangle.BOX);
	            Table20.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table20.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table20.addCell(new Phrase(new Chunk(es.formatAmount(gross_amt),small_black_normal)));
	            
	            sr+=1;
	            float[] ContactAddrWidths28 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table26 = new PdfPTable(ContactAddrWidths28);
	            Table26.setWidthPercentage(100);
	            Table26.getDefaultCell().setBorder(Rectangle.BOX);
	            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table26.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table26.getDefaultCell().setBorder(Rectangle.BOX);
	            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table26.addCell(new Phrase(new Chunk("Total Amount",small_black_bold)));
	
	            Table26.getDefaultCell().setBorder(Rectangle.BOX);
	            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table26.addCell(new Phrase(new Chunk(curr3,small_black_bold)));
	
	            Table26.getDefaultCell().setBorder(Rectangle.BOX);
	            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table26.addCell(new Phrase(new Chunk(es.formatAmount(gross_amt),small_black_bold)));
	            
	            sr+=1;
	            float[] ContactAddrWidths24 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table22 = new PdfPTable(ContactAddrWidths24);
	            
	            if (tax_name.contains(",")) 
				{
	            	BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_amt));
					BigDecimal factor = new BigDecimal(2);
	            	temp_srno=sr;
	            	for(int i = 0;i<tax_name.split(", ").length;i++) 
					{
	            		BigDecimal sub_tax_amt = tax_amt1.divide(factor, 0, RoundingMode.HALF_UP);
	            		temp_srno=temp_srno+0.1;
	            		Table22.setWidthPercentage(100);
	            		Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table22.addCell(new Phrase(new Chunk(nf0.format(temp_srno),small_black_normal)));
			
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            Table22.addCell(new Phrase(new Chunk(tax_name.split(",")[i],small_black_normal)));
			
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table22.addCell(new Phrase(new Chunk(curr3,small_black_normal)));
			
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            Table22.addCell(new Phrase(new Chunk(es.formatAmount(""+Double.parseDouble(""+sub_tax_amt)),small_black_normal)));
					}
				}

	            Table22.setWidthPercentage(100);
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table22.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table22.addCell(new Phrase(new Chunk("Tax ("+tax_name+")",small_black_bold)));
	
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table22.addCell(new Phrase(new Chunk(curr3,small_black_bold)));
	
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table22.addCell(new Phrase(new Chunk(es.formatAmount(tax_amt),small_black_bold)));
	            
	            
	            sr+=1;
	            float[] ContactAddrWidths25 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table23 = new PdfPTable(ContactAddrWidths25);
	            Table23.setWidthPercentage(100);
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table23.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table23.addCell(new Phrase(new Chunk("Total Amount in INR",small_black_bold)));
	
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table23.addCell(new Phrase(new Chunk(curr3,small_black_bold)));
	
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table23.addCell(new Phrase(new Chunk(es.formatAmount(net_amt),small_black_bold)));
	            
	            float[] ContactAddrWidths26_1 = {0.100F};
	   	     	PdfPTable Table24_1 = new PdfPTable(ContactAddrWidths26_1);
	   	     	Table24_1.setWidthPercentage(100);
	   	     	Table24_1.getDefaultCell().setBorder(Rectangle.BOX);
	   	     	Table24_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	   	     	Table24_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	   	     	Table24_1.addCell(new Phrase(new Chunk("Amount in Words | Rupees "+amt_in_word+" Only",black_bold)));		     	
	   	     	
	   	     	float[] ContactAddrWidths26 = {0.100F};
	   	     	PdfPTable Table24 = new PdfPTable(ContactAddrWidths26);
	   	     	Table24.setWidthPercentage(100);
	   	     	Table24.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	   	     	Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	   	  		Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	   	  		Table24.addCell(new Phrase(new Chunk("For "+supp_nm,black_bold)));
	
	   	  		float[] ContactAddrWidths27 = {0.100F};
	   	  		PdfPTable Table25 = new PdfPTable(ContactAddrWidths27);
		     	Table25.setWidthPercentage(100);
		     	Table25.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table25.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table25.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table25.addCell(new Phrase(new Chunk("Authorised Signatory",black_bold)));
		     	
		     	PdfPTable BillingFieldsInfoTable81 = new PdfPTable(1);
			     BillingFieldsInfoTable81.setWidthPercentage(20);
			     BillingFieldsInfoTable81.setHorizontalAlignment(Element.ALIGN_LEFT);
			     BillingFieldsInfoTable81.getDefaultCell().setBorder(Rectangle.BOX);
			     BillingFieldsInfoTable81.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			     BillingFieldsInfoTable81.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	
			     PdfFormField field = PdfFormField.createSignature(writer);
			     field.setFieldName(SIGNAME);
	
			     field.setPage();
			     field.setWidget(new Rectangle(72, 732, 144, 780), PdfAnnotation.HIGHLIGHT_NONE);
			     field.setFlags(PdfAnnotation.FLAGS_PRINT);
			     writer.addAnnotation(field);
	
			     PdfPCell sigCell = new PdfPCell();
			     FieldPositioningEvents events = new FieldPositioningEvents(writer, field);
			     sigCell.setCellEvent(events);
			     sigCell.setBorder(Rectangle.NO_BORDER);
			     sigCell.setFixedHeight(50f);
			     BillingFieldsInfoTable81.addCell(sigCell);
			    
			     PdfPTable leftSignTable = new PdfPTable(1);
			     leftSignTable.setWidthPercentage(100);
			     leftSignTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			     leftSignTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	
			     leftSignTable.addCell(Table24);
			     leftSignTable.addCell(BillingFieldsInfoTable81);
			     leftSignTable.addCell(Table25);
	
			     float[] InvoiceDateInfoWidths_qr = {0.30f, 0.20f, 0.40f};
			     PdfPTable InvoiceDateInfoTable_qr = new PdfPTable(InvoiceDateInfoWidths_qr);
	
			     if (!irn_no.equals("") && !qr_code.equals("")) {
	
			         int width = 90;
			         int height = 90;
			         Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
			         hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
	
			         QRCodeWriter qrCodeWriter = new QRCodeWriter();
			         BitMatrix matrix = qrCodeWriter.encode(qr_code, BarcodeFormat.QR_CODE, width, height, hintMap);
	
			         BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	
			         for (int x = 0; x < width; x++) {
			             for (int y = 0; y < height; y++) {
			                 image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
			             }
			         }
	
			         ByteArrayOutputStream baos = new ByteArrayOutputStream();
			         ImageIO.write(image, "png", baos);
			         Image qr_codeimg = Image.getInstance(baos.toByteArray());
			         qr_codeimg.setBorder(Rectangle.NO_BORDER);
			         qr_codeimg.setAlignment(Element.ALIGN_LEFT);
	
			         PdfPCell qrcode_cell = new PdfPCell(qr_codeimg, false);
			         qrcode_cell.setBorder(Rectangle.NO_BORDER);
			         qrcode_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	
			         String char_16 = irn_no.substring(0, 16);
			         String char_32 = irn_no.substring(16, 32);
			         String char_48 = irn_no.substring(32, 48);
			         String char_64 = irn_no.substring(48);
			         String final_irn_no = char_16 + "\n" + char_32 + "\n" + char_48 + "\n" + char_64;
	
			         PdfPTable InvoiceDateInfoTable_qr1 = new PdfPTable(1);
			         InvoiceDateInfoTable_qr1.setWidthPercentage(100);
			         InvoiceDateInfoTable_qr1.getDefaultCell().setBorder(Rectangle.BOX);
			         InvoiceDateInfoTable_qr1.addCell(qrcode_cell);
	
			         PdfPTable InvoiceDateInfoTable_qr11 = new PdfPTable(1);
			         InvoiceDateInfoTable_qr11.setWidthPercentage(100);
			         InvoiceDateInfoTable_qr11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			         InvoiceDateInfoTable_qr11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			         InvoiceDateInfoTable_qr11.addCell(new Phrase(new Chunk("IRN", black_bold)));
			         InvoiceDateInfoTable_qr11.addCell(new Phrase(new Chunk(final_irn_no, small_black_normal)));
	
			         InvoiceDateInfoTable_qr.setWidthPercentage(100);
			         InvoiceDateInfoTable_qr.getDefaultCell().setBorder(Rectangle.BOX);
			         InvoiceDateInfoTable_qr.addCell(InvoiceDateInfoTable_qr11);
	
			         InvoiceDateInfoTable_qr.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			         InvoiceDateInfoTable_qr.addCell("");
	
			         InvoiceDateInfoTable_qr.addCell(InvoiceDateInfoTable_qr1);
			     }
	
			     PdfPTable finalRow = new PdfPTable(3);
			     finalRow.setWidthPercentage(100);
			     finalRow.setWidths(new float[]{0.3f, 0.2f ,0.5f});
			     finalRow.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	
			     finalRow.addCell(leftSignTable);
			     finalRow.addCell("");
			     finalRow.addCell(InvoiceDateInfoTable_qr);

		        float[] ContactAddrWidths30 = {0.100F};
	   	  		PdfPTable Table28 = new PdfPTable(ContactAddrWidths30);
		     	Table28.setWidthPercentage(100);
		     	Table28.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table28.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table28.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table28.addCell(new Phrase(new Chunk("Notes:",black_bold)));
		     	
		     	float[] ContactAddrWidths31 = {0.01F,0.200F};
	   	  		PdfPTable Table29 = new PdfPTable(ContactAddrWidths31);
		     	Table29.setWidthPercentage(100);
		     	Table29.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table29.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table29.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table29.addCell(new Phrase(new Chunk("1. ",small_black_normal)));
		     	
		     	Table29.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table29.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table29.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table29.addCell(new Phrase(new Chunk(remark1,small_black_normal)));
		     	
		     	float[] ContactAddrWidths32 = {0.01F,0.200F};
	   	  		PdfPTable Table30 = new PdfPTable(ContactAddrWidths32);
		     	Table30.setWidthPercentage(100);
		     	Table30.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table30.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table30.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table30.addCell(new Phrase(new Chunk("2. ",small_black_normal)));
		     	
		     	Table30.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table30.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table30.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table30.addCell(new Phrase(new Chunk(remark2,small_black_normal)));
		     	

				document.add(mainTable);
				document.add(new Paragraph("  "));
				document.add(Table3);
				document.add(Table4);
				document.add(Table5);
				document.add(Table6);
				document.add(Table7);
				document.add(Table8);
				document.add(Table9);
				document.add(Table10);
				document.add(Table11);
				document.add(Table12);
				document.add(Table13);
				document.add(new Paragraph("  "));
				document.add(Table14);
				document.add(Table15);
				document.add(Table17);
				document.add(new Paragraph("  "));
				document.add(Table18);
				document.add(Table19);
				document.add(Table21);
				document.add(Table16);
				document.add(Table20);
				document.add(Table26);
				document.add(Table22);
				document.add(Table23);
				document.add(new Paragraph("  "));
				document.add(Table24_1);
				document.add(new Paragraph("  "));
				document.add(finalRow);
				document.add(new Paragraph("  "));
				document.add(Table28);
				document.add(Table29);
				document.add(Table30);
				
	            document.close();
	            
	            if(is_print.equals("1"))
				{
		            File isPDFexist = new File(path);
					if(isPDFexist.exists())
					{
						int update_flag=0;
						int pdf_entry=0;
						
						int cont=0;
						queryString="UPDATE FMS_OTH_INVOICE_MST SET PDF_INV_DTL=? ";
							if(print_pdf_type.equals("O"))
							{
								queryString+= ",PRINT_BY_ORI=?,PRINT_DT_ORI=SYSDATE ";
							}
							else if(print_pdf_type.equals("D"))
							{
								queryString+= ",PRINT_BY_DUP=?,PRINT_DT_DUP=SYSDATE ";
							}
							queryString+= "WHERE COMPANY_CD=? AND INVOICE_NO=? AND INVOICE_TYPE=? AND INVOICE_SEQ = ? AND SUPPLIER_CD = ?";
							
						stmt=conn.prepareStatement(queryString);
						stmt.setString(++cont, print_pdf_type);
						if(print_pdf_type.equals("O"))
						{
							stmt.setString(++cont, emp_cd);
						}
						else if(print_pdf_type.equals("D"))
						{
							stmt.setString(++cont, emp_cd);
						}
						stmt.setString(++cont, comp_cd);
						stmt.setString(++cont, inv_no);
						stmt.setString(++cont, inv_type);
						stmt.setString(++cont, inv_seq);
						stmt.setString(++cont, supp_cd);
						update_flag=stmt.executeUpdate();
						stmt.close();
						
						int cnt=0;
				        queryString1="SELECT COUNT(*) "
				        		+ "FROM FMS_OTH_INV_FILE_DTL "
				        		+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? "
				        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND SUPPLIER_CD =?";
				        stmt1=conn.prepareStatement(queryString1);
				        stmt1.setString(1, comp_cd);
				        stmt1.setString(2, inv_type);
				        stmt1.setString(3, inv_seq);
				        stmt1.setString(4, fin_yr);
				        stmt1.setString(5, print_pdf_type);
				        stmt1.setString(6, supp_cd);
				        rset1=stmt1.executeQuery();
				        if(rset1.next())
				        {
				        	cnt=rset1.getInt(1);
				        }
				        rset1.close();
				        stmt1.close();
				        
				        if(cnt > 0)
				        {
				        	queryString1="UPDATE FMS_OTH_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
				        			+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? "
					        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND SUPPLIER_CD =?";
				        	stmt1=conn.prepareStatement(queryString1);
				        	stmt1.setString(1, file_nm);
				        	stmt1.setString(2, emp_cd);
				        	stmt1.setString(3, comp_cd);
				 	        stmt1.setString(4, inv_type);
				 	        stmt1.setString(5, inv_seq);
				 	        stmt1.setString(6, fin_yr);
				 	        stmt1.setString(7, print_pdf_type); 
				 	        stmt1.setString(8, supp_cd); 
				 	        pdf_entry=stmt1.executeUpdate();
				        	
				        	stmt1.close();
				        }
				        else
				        {
				        	queryString1="INSERT INTO FMS_OTH_INV_FILE_DTL(COMPANY_CD,SUPPLIER_CD,INVOICE_SEQ,INVOICE_TYPE,FINANCIAL_YEAR,PDF_TYPE,"
				        			+ "FILE_NAME,ENT_BY,ENT_DT) "
				        			+ "VALUES(?,?,?,?,?,?,"
				        			+ "?,?,SYSDATE)";
				        	stmt1=conn.prepareStatement(queryString1);
				        	stmt1.setString(1, comp_cd);
				        	stmt1.setString(2, supp_cd);
				 	        stmt1.setString(3, inv_seq);
				 	        stmt1.setString(4, inv_type);
				 	        stmt1.setString(5, fin_yr);
				 	        stmt1.setString(6, print_pdf_type);
				 	        stmt1.setString(7, file_nm);
				        	stmt1.setString(8, emp_cd);
				        	pdf_entry=stmt1.executeUpdate();
				        	
				        	stmt1.close();
				        }
				        
				        if(pdf_entry == 0 || update_flag == 0)
				        {
				        	conn.rollback();
				        	deletePdfFile(path);
				        	msg = "Failed! - PDF "+file_nm+" for AHPL Revenue Share with Invoice No. "+inv_no+" Generation Failed!";
				        	msg_type="E";
				        }
				        else
				        {
				        	conn.commit();
				        	msg = "Successful! - PDF "+file_nm+" for AHPL Revenue Share with Invoice No. "+inv_no+" Generated Successfully!";
				        	msg_type="S";
				        }
					}
					else
					{
						msg="Failed! - PDF for AHPL Revenue Share with Invoice No. "+inv_no+" Generation Failed!";
						msg_type="E";
					}
				}
			}
		}
		catch(Exception e)
		{
			conn.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			
			document.close();
			deletePdfFile(path);
			
			msg = "Error in Exception! - PDF for AHPL Revenue Share with Invoice No. "+inv_no+" Generation Failed!";
			msg_type="E";	
		}
		finally
		{
			document.close();
		}
		
		if(is_print.equals("1"))
		{
			try
			{
				new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, "", "", msg);  	
			}
			catch(Exception infoLogger)
			{
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
			}
		}
	}
	
	//Deep20251120
	public void getInvoiceDtlsSFA()
	{	
		String function_nm="getInvoiceDtlsSFA()";

		try
		{
			queryString = "SELECT A.COMPANY_CD,A.VENDOR_CD,A.SUPPLIER_CD,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),B.GATE_PASS_NO,"
					+ "B.SALE_NO,A.GROSS_AMT,A.TAX_AMT_INR,A.NET_PAYABLE,A.EXCHG_RATE_VALUE,TO_CHAR(A.EXCHG_RATE_DT, 'DD/MM/YYYY'),"
					+ "A.SALE_PRICE_UNIT,B.SAC_CODE,B.TAX_STRUCT_CD,B.ITEM_DESCRIPTION,A.REMARK,A.REMARK1,A.APPROVED_FLAG,A.FINANCIAL_YEAR,"
					+ "A.INVOICE_SEQ,A.SALE_AMT,A.CHECKED_FLAG,A.INVOICE_NO,A.INVOICE_ID_SEQ,TO_CHAR(B.TAX_EFF_DT, 'DD/MM/YYYY'),A.INVOICE_CATEGORY,"
					+ "A.INVOICE_RAISED_IN,B.CESS_RATE,B.TOTAL_CESS_AMOUNT "
					+ "FROM FMS_OTH_INVOICE_MST A, FMS_OTH_INVOICE_DTL B ";
			if(is_print.equals("0"))
			{
				queryString += "WHERE A.COMPANY_CD = ? AND A.INVOICE_SEQ = ? AND A.INVOICE_TYPE = ? AND A.FINANCIAL_YEAR=? AND A.SUPPLIER_CD=? AND A.INV_FLAG = ? "
						+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.INVOICE_TYPE = B.INVOICE_TYPE "
						+ "AND A.VENDOR_CD = B.VENDOR_CD AND A.SUPPLIER_CD = B.SUPPLIER_CD AND A.INVOICE_SEQ = B.INVOICE_SEQ AND A.INV_FLAG = B.INV_FLAG";
			}
			else
			{
				queryString += "WHERE A.COMPANY_CD = ? AND A.INVOICE_NO = ? AND A.INVOICE_TYPE = ? "
						+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.INVOICE_SEQ = B.INVOICE_SEQ AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.INVOICE_TYPE = B.INVOICE_TYPE AND "
						+ "A.INVOICE_DT = B.EFF_DT AND A.VENDOR_CD = B.VENDOR_CD AND A.SUPPLIER_CD = B.SUPPLIER_CD AND A.INV_FLAG = B.INV_FLAG AND "
						+ "A.VENDOR_TYPE =? AND A.INV_FLAG = ? ";
			}
			stmt = conn.prepareStatement(queryString);
			if(is_print.equals("0"))
			{
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_seq);
				stmt.setString(3, inv_type);
				stmt.setString(4, fin_yr);
				stmt.setString(5, supp_cd);
				stmt.setString(6, "F");
			}
			else
			{
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_no);
				stmt.setString(3, inv_type);
				stmt.setString(4, "V");
				stmt.setString(5, "F");
			}
			rset = stmt.executeQuery();
			if(rset.next()) 
			{
				
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
				
				tax_name=utilBean.getTaxDescr(conn,tax_struct_cd);
				
				if (tax_name.contains(",")) 
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

				queryString2 = "SELECT ITEM_DESCRIPTION,SALE_PRICE,HSN_CODE,QUANTITY,UAM_NO,ITEM_AMT,TAX_STRUCT_CD,TAX_EFF_DT,ITEM_TAX_AMT,CESS_RATE,CESS_AMOUNT "
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
				}
				rset2.close();
				stmt2.close();
				
				for (Map.Entry<String, Double> entry : taxAmountMap.entrySet()) 
				{
				    VTAX_STRUCT_NM.add(entry.getKey());
				    VTAX_VAL_AMT.add(nf.format(entry.getValue()));
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
	
	//Deep20251120
	public void pdfSFA() throws SQLException
	{
		String function_nm="pdfSFA()";
		
		String emp_nm ="";
		String ip = "";
		msg="";
		//String msg_content="";
		String path ="";
		
		Rectangle pageSize = new Rectangle(595, 842);
		
		pageSize.setBackgroundColor(BaseColor.WHITE);
		
		Document document = new Document(pageSize);
		try
		{
			HttpSession session = request.getSession();
			emp_nm = (String)session.getAttribute("emp_nm")==null?"":(String)session.getAttribute("emp_nm");
			ip = (String)session.getAttribute("ip")==null?"":(String)session.getAttribute("ip");
			
			String company_nm = ""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");
			String company_abbr = ""+session.getAttribute("comp_abbr")==null?"":""+session.getAttribute("comp_abbr");
			
			int pdfGenCount=0;
			
			if(is_print.equals("1"))
	        {
				queryString = "SELECT COUNT(*) "
							+ "FROM FMS_OTH_INVOICE_MST A ,FMS_OTH_INV_FILE_DTL B "
							+ "WHERE A.COMPANY_CD=? AND A.INVOICE_NO = ? AND A.INVOICE_TYPE = ? ";
							if(print_pdf_type.equals("O"))
							{
								queryString+= "AND A.PRINT_BY_ORI IS NOT NULL AND A.PRINT_DT_ORI IS NOT NULL ";
							}
							else if(print_pdf_type.equals("T"))
							{
								queryString+= "AND A.PRINT_BY_TRI IS NOT NULL AND A.PRINT_DT_TRI IS NOT NULL ";
							}
							else if(print_pdf_type.equals("D"))
							{
								queryString+= "AND A.PRINT_BY_DUP IS NOT NULL AND A.PRINT_DT_DUP IS NOT NULL ";
							}
				queryString += "AND B.PDF_TYPE=? AND B.INVOICE_TYPE = ? AND B.FILE_NAME IS NOT NULL "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.INVOICE_TYPE = B.INVOICE_TYPE AND A.SUPPLIER_CD = B.SUPPLIER_CD";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_no);
				stmt.setString(3, inv_type);
				stmt.setString(4, print_pdf_type);
				stmt.setString(5, inv_type);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					pdfGenCount=rset.getInt(1);
				}
				rset.close();
				stmt.close();
	        }
			else
			{
				pdfGenCount=0;
			}
	
			if(pdfGenCount>0)
			{
				msg="Failed! - PDF for Scrap fixed Asset with Invoice No. "+inv_no+" already Generated!";
	        	msg_type="E";
	        }
			else
			{
				
				getInvoiceDtlsSFA();
				getSupplierDtl();
				getVendorDtl();
				
				String irn_no="";
				String qr_code="";
				queryString1="SELECT IRN_NO,SIGN_QR_CODE "
						+ "FROM FMS_OTH_INV_IRN_DTL "
						+ "WHERE COMPANY_CD=? AND INVOICE_NO=? ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, inv_no);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					irn_no=rset1.getString(1)==null?"":rset1.getString(1);
					qr_code=rset1.getString(2)==null?"":rset1.getString(2);
				}
				rset1.close();
				stmt1.close();
				
				String print_pdf_type_nm="ORIGINAL";
				if(print_pdf_type.equals("O"))
				{
					print_pdf_type_nm="ORIGINAL";
				}
				else if(print_pdf_type.equals("D"))
				{
					print_pdf_type_nm="DUPLICATE";
				}
				else if(print_pdf_type.equals("T"))
				{
					print_pdf_type_nm="TRIPLICATE";
				}
	
				String appPath = request.getServletContext().getRealPath("");
	
				String main_folder="";
				if(!comp_cd.equals(""))
				{
					main_folder=CommonVariable.work_dir+comp_cd;
				}
				
				String sub_folder="unsigned_pdf";
				String sub_folder2="ig_invoice";
				
				String temp_path=appPath+File.separator+main_folder+File.separator+sub_folder+File.separator+sub_folder2;
				File main_folderDir = new File(temp_path);
		        if(!main_folderDir.exists())
		        {
		        	main_folderDir.mkdirs();
		        }

		        if(is_print.equals("1"))
		        {
		        	file_nm=print_pdf_type+"-"+supp_abbr+"-"+inv_type+"-"+inv_no.replace("/", "-")+".pdf";
		        }
		        else
		        {
		        	file_nm="temp.pdf";
		        }
	
				path = appPath+"/"+main_folder+"/"+sub_folder+"/"+sub_folder2+"/"+file_nm;
				PdfWriter writer = PdfWriter.getInstance(document,new FileOutputStream(path));
	
				document.open();
				document.setPageSize(pageSize);
	            document.newPage();
	
	            String context_nm = request.getContextPath();
				String server_nm = request.getServerName();
				String server_port = ""+request.getServerPort();
	
				file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm;
	
				Font small_black_normal = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
				Font small_black_normal_10 = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
				Font small_black_normal_14 = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.NORMAL, BaseColor.BLACK);
				Font small_black_normal_12 = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
				Font small_black_bold = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
	            Font black_bold = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
	            Font black_bold1 = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
	            
	            PdfPCell company_logo_cell = new PdfPCell();
	            if(!comp_logo.equals(""))
	            {
					String file_path = request.getRealPath("/"+CommonVariable.company_logo_path+"/"+comp_logo);
	            	Image company_logo = Image.getInstance(file_path);
		            company_logo.setBorder(Rectangle.NO_BORDER);
		            company_logo.scaleAbsolute(44,40);
		            company_logo_cell = new PdfPCell(company_logo,false);
		            company_logo_cell.setBorder(Rectangle.NO_BORDER);
		            company_logo_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            }
	            
	            float[] mainWidths = {10f, 70f, 15f};  
	            PdfPTable mainTable = new PdfPTable(mainWidths);
	            mainTable.setWidthPercentage(100);
	            mainTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

	            PdfPCell logoCell = new PdfPCell(company_logo_cell);
	            logoCell.setBorder(Rectangle.NO_BORDER);
	            logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            logoCell.setPaddingLeft(10);
	            mainTable.addCell(logoCell);
	            
	            PdfPTable centerTable = new PdfPTable(1);
	            centerTable.setWidthPercentage(100);
	            centerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            centerTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            centerTable.addCell(new Phrase(new Chunk(print_pdf_type_nm, small_black_normal_10)));
	            centerTable.addCell(new Phrase(new Chunk(supp_nm, black_bold1)));
	            centerTable.addCell(new Phrase(new Chunk("TAX INVOICE", small_black_normal_12)));
	            String info = "Tax invoice issued under Rule 46 of the Central Goods and Services Tax Rules, 2017";
	            centerTable.addCell(new Phrase(new Chunk(info, small_black_normal)));

	            PdfPCell centerCell = new PdfPCell(centerTable);
	            centerCell.setBorder(Rectangle.NO_BORDER);
	            centerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            mainTable.addCell(centerCell);

	            PdfPCell emptyCell = new PdfPCell(new Phrase(""));
	            emptyCell.setBorder(Rectangle.NO_BORDER);
	            mainTable.addCell(emptyCell);
	
	            float[] ContactAddrWidths5 = {0.80f,0.10F,0.20F,0.80F};
	   	     	PdfPTable Table3 = new PdfPTable(ContactAddrWidths5);
	            Table3.setWidthPercentage(100);
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("Registered Office: ",small_black_bold)));
	            
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("To:",small_black_bold)));
	
				float[] ContactAddrWidths6 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table4 = new PdfPTable(ContactAddrWidths6);
	            Table4.setWidthPercentage(100);
	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk(supp_nm,small_black_bold)));
	            
	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk(""+vendor_name+",",small_black_bold)));
	
	            float[] ContactAddrWidths7 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table5 = new PdfPTable(ContactAddrWidths7);
	            Table5.setWidthPercentage(100);
	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk(supp_addr+" "+supp_city,small_black_normal)));
	            
	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk(vend_abbr+" "+vend_city,small_black_normal)));
	            
	
	            float[] ContactAddrWidths8 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table6 = new PdfPTable(ContactAddrWidths8);
	            Table6.setWidthPercentage(100);
	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table6.addCell(new Phrase(new Chunk(supp_state+"-"+supp_pin,small_black_normal)));
	
	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table6.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_pin_no.equals("")) 
	            {
	            	Table6.addCell(new Phrase(new Chunk(vend_state + "-" + vend_pin_no, small_black_normal)));
				}
	            else 
	            {
	            	Table6.addCell(new Phrase(new Chunk(vend_state, small_black_normal)));
	            }
	            
	           	float[] ContactAddrWidths9 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table7 = new PdfPTable(ContactAddrWidths9);
	            Table7.setWidthPercentage(100);
	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk("State: "+supp_state,small_black_normal)));
	
	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk("State: "+vend_state,small_black_normal)));
	            
	            float[] ContactAddrWidths10 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table8 = new PdfPTable(ContactAddrWidths10);
	            Table8.setWidthPercentage(100);
	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table8.addCell(new Phrase(new Chunk("State Code: "+supp_state_tin,small_black_normal)));
	
	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table8.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_state_tin.equals("")) 
	            {
					Table8.addCell(new Phrase(new Chunk("State Code: " + vend_state_tin, small_black_normal)));
				}
	            else 
	            {
					Table8.addCell(new Phrase(new Chunk("", small_black_normal)));
	            }
	            
	            float[] ContactAddrWidths11 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table9 = new PdfPTable(ContactAddrWidths11);
	            Table9.setWidthPercentage(100);
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table9.addCell(new Phrase(new Chunk("GSTIN: "+supp_gstin_no,small_black_normal)));
	
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table9.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_gstin_no.equals("")) 
	            {
					Table9.addCell(new Phrase(new Chunk("GSTIN: " + vend_gstin_no, small_black_normal)));
				}
	            else 
	            {
	            	Table9.addCell(new Phrase(new Chunk("", small_black_normal)));
	            }

	            float[] ContactAddrWidths12 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table10 = new PdfPTable(ContactAddrWidths12);
	            Table10.setWidthPercentage(100);
	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table10.addCell(new Phrase(new Chunk("PAN: "+supp_pan_no,small_black_normal)));
	
	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table10.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_pan_no.equals("")) 
	            {
					Table10.addCell(new Phrase(new Chunk("PAN: " + vend_pan_no, small_black_normal)));
				}
	            else 
	            {
	            	Table10.addCell(new Phrase(new Chunk("", small_black_normal)));
	            }
	            
//	            float[] ContactAddrWidths13 = {0.80f,0.30F,0.80F};
//	   	     	PdfPTable Table11 = new PdfPTable(ContactAddrWidths13);
//	            Table11.setWidthPercentage(100);
//	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//	            Table11.addCell(new Phrase(new Chunk("SAC: "+sac_code,small_black_normal)));
//	
//	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//	            Table11.addCell(new Phrase(new Chunk("",small_black_normal)));
//	
//	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//	            Table11.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
//	            float[] ContactAddrWidths14 = {0.80f,0.30F,0.80F};
//	   	     	PdfPTable Table12 = new PdfPTable(ContactAddrWidths14);
//	            Table12.setWidthPercentage(100);
//	            Table12.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//	            Table12.addCell(new Phrase(new Chunk("Description of Service: "+sac_des,small_black_normal)));
//	
//	            Table12.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//	            Table12.addCell(new Phrase(new Chunk("",small_black_normal)));
//	
//	            Table12.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//	            Table12.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            float[] ContactAddrWidths15 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table13 = new PdfPTable(ContactAddrWidths15);
	            Table13.setWidthPercentage(100);
	            Table13.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table13.addCell(new Phrase(new Chunk("Place of Supply: "+supp_state,small_black_normal)));
	
	            Table13.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table13.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table13.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table13.addCell(new Phrase(new Chunk("",small_black_normal)));
            
	            float[] ContactAddrWidths16 = {0.30F,0.30F,0.40F,0.30F,0.30F};
	   	     	PdfPTable Table14 = new PdfPTable(ContactAddrWidths16);
	            Table14.setWidthPercentage(100);
	            Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table14.addCell(new Phrase(new Chunk("Sale Agreement No :",black_bold)));
	
	            Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table14.addCell(new Phrase(new Chunk(sale_no,black_bold)));
	            
	            Table14.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table14.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table14.addCell(new Phrase(new Chunk("Invoice No :",black_bold)));
	
	            Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table14.addCell(new Phrase(new Chunk(inv_no,black_bold)));
	            
	            float[] ContactAddrWidths17 = {0.30F,0.30F,0.40F,0.30F,0.30F};
	   	     	PdfPTable Table15 = new PdfPTable(ContactAddrWidths17);
	            Table15.setWidthPercentage(100);
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table15.addCell(new Phrase(new Chunk("Gate Pass No:",black_bold)));
	            
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk(gate_pass,black_bold)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table15.addCell(new Phrase(new Chunk("Invoice Date :",black_bold)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk(inv_dt,black_bold)));

//	            float[] ContactAddrWidths18 = {0.80F,0.20F,0.50F,0.40F};
//	   	     	PdfPTable Table16 = new PdfPTable(ContactAddrWidths18);
//	            Table16.setWidthPercentage(100);
//	            Table16.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//	            Table16.addCell(new Phrase(new Chunk("",small_black_normal)));
//	
//	            Table16.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//	            Table16.addCell(new Phrase(new Chunk("",small_black_normal)));
//	
//	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
//	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//	            Table16.addCell(new Phrase(new Chunk("Vendor/Supplier Inv Ref No :",black_bold)));
//	
//	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
//	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//	            Table16.addCell(new Phrase(new Chunk(ref_no,black_bold)));
//	            
//	            float[] ContactAddrWidths19 = {0.80F,0.20F,0.50F,0.40F};
//	   	     	PdfPTable Table17 = new PdfPTable(ContactAddrWidths19);
//	            Table17.setWidthPercentage(100);
//	            Table17.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//	            Table17.addCell(new Phrase(new Chunk("",small_black_normal)));
//	
//	            Table17.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//	            Table17.addCell(new Phrase(new Chunk("",small_black_normal)));
//	
//	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
//	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//	            Table17.addCell(new Phrase(new Chunk("Pace WO/PO No: ",black_bold)));
//	
//	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
//	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//	            Table17.addCell(new Phrase(new Chunk(pacer_no,black_bold)));
	            
	            if(invoice_category.equals("S"))
	            {         
		            float[] ContactAddrWidths20 = {0.12F,0.20F,0.70F,0.15F,0.17F,0.23F,0.17F,0.20F};
		   	     	PdfPTable Table18 = new PdfPTable(ContactAddrWidths20);
		            Table18.setWidthPercentage(100);
		            Table18.getDefaultCell().setBorder(Rectangle.BOX);
		            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table18.addCell(new Phrase(new Chunk("Sr.No.",small_black_bold)));
		            
		            Table18.getDefaultCell().setBorder(Rectangle.BOX);
		            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table18.addCell(new Phrase(new Chunk("HSN/SAC Code",small_black_bold)));
		
		            Table18.getDefaultCell().setBorder(Rectangle.BOX);
		            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table18.addCell(new Phrase(new Chunk("Description",small_black_bold)));
		            
		            Table18.getDefaultCell().setBorder(Rectangle.BOX);
		            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table18.addCell(new Phrase(new Chunk("UOM",small_black_bold)));
		            
		            Table18.getDefaultCell().setBorder(Rectangle.BOX);
		            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table18.addCell(new Phrase(new Chunk("Quantity",small_black_bold)));
		            
		            Table18.getDefaultCell().setBorder(Rectangle.BOX);
		            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table18.addCell(new Phrase(new Chunk("Rate per Qty",small_black_bold)));
		
		            Table18.getDefaultCell().setBorder(Rectangle.BOX);
		            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table18.addCell(new Phrase(new Chunk("Currency",small_black_bold)));
		
		            Table18.getDefaultCell().setBorder(Rectangle.BOX);
		            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table18.addCell(new Phrase(new Chunk("Amount",small_black_bold)));
	
		            
		            String curr1 = "INR";
		            
		            int sr=0;
		            float[] ContactAddrWidths21 = {0.12F,0.20F,0.70F,0.15F,0.17F,0.23F,0.17F,0.20F};
		   	     	PdfPTable Table19 = new PdfPTable(ContactAddrWidths21);
		   	     	
		            for(int i = 0;i<VQTY.size();i++) 
		            {
		            	sr += 1;
						Table19.setWidthPercentage(100);
						Table19.getDefaultCell().setBorder(Rectangle.BOX);
						Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						Table19.addCell(new Phrase(new Chunk("" + sr, small_black_normal)));
						
						Table19.getDefaultCell().setBorder(Rectangle.BOX);
						Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						Table19.addCell(new Phrase(new Chunk(""+VSAC_CODE.elementAt(i), small_black_normal)));
						
						Table19.getDefaultCell().setBorder(Rectangle.BOX);
						Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
						Table19.addCell(new Phrase(new Chunk(""+VGAMT_DES.elementAt(i), small_black_normal)));
						
						Table19.getDefaultCell().setBorder(Rectangle.BOX);
						Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						Table19.addCell(new Phrase(new Chunk(""+VUOM_NO.elementAt(i), small_black_normal)));
						
						Table19.getDefaultCell().setBorder(Rectangle.BOX);
						Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
						Table19.addCell(new Phrase(new Chunk(nf.format(Double.parseDouble("" + VQTY.elementAt(i))), small_black_normal)));
						
						Table19.getDefaultCell().setBorder(Rectangle.BOX);
						Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
						Table19.addCell(new Phrase(new Chunk(nf.format(Double.parseDouble("" + VPRICE.elementAt(i))), small_black_normal)));
						
						Table19.getDefaultCell().setBorder(Rectangle.BOX);
						Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						Table19.addCell(new Phrase(new Chunk(curr1, small_black_normal)));
						
						Table19.getDefaultCell().setBorder(Rectangle.BOX);
						Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
						Table19.addCell(new Phrase(new Chunk(es.formatAmount(""+VGITEM_AMT.elementAt(i)), small_black_normal)));
					}
					
		            sr+=1;
		            float[] ContactAddrWidths23 = {0.12F,0.20F,0.70F,0.15F,0.17F,0.23F,0.17F,0.20F};
		   	     	PdfPTable Table21 = new PdfPTable(ContactAddrWidths23);
		            Table21.setWidthPercentage(100);
		            
		            Table21.getDefaultCell().setBorder(Rectangle.BOX);
		            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table21.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
		            
		            Table21.getDefaultCell().setBorder(Rectangle.BOX);
		            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table21.addCell(new Phrase(new Chunk("",small_black_normal)));
		
		            Table21.getDefaultCell().setBorder(Rectangle.BOX);
		            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            Table21.addCell(new Phrase(new Chunk("Total Amount ",small_black_bold)));
		            
		            Table21.getDefaultCell().setBorder(Rectangle.BOX);
		            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table21.addCell(new Phrase(new Chunk("",small_black_normal)));
		            
		            Table21.getDefaultCell().setBorder(Rectangle.BOX);
		            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table21.addCell(new Phrase(new Chunk("",small_black_normal)));
		            
		            Table21.getDefaultCell().setBorder(Rectangle.BOX);
		            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table21.addCell(new Phrase(new Chunk("",small_black_normal)));
		
		            Table21.getDefaultCell().setBorder(Rectangle.BOX);
		            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table21.addCell(new Phrase(new Chunk(curr1,small_black_bold)));
		
		            Table21.getDefaultCell().setBorder(Rectangle.BOX);
		            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table21.addCell(new Phrase(new Chunk(es.formatAmount(gross_amt),small_black_bold)));
		            
		            sr+=1;
		            float[] ContactAddrWidths24 = {0.12F,0.20F,0.70F,0.15F,0.17F,0.23F,0.17F,0.20F};
		   	     	PdfPTable Table22 = new PdfPTable(ContactAddrWidths24);
		            
		            if (tax_name.contains(",")) 
					{
						BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_amt));
						BigDecimal factor = new BigDecimal(2);
		            	double temp_srno=sr;
		            	for(int i = 0;i<tax_name.split(", ").length;i++) 
						{
		            		BigDecimal sub_tax_amt = tax_amt1.divide(factor, 0, RoundingMode.HALF_UP);
		            		temp_srno=temp_srno+0.1;
		            		Table22.setWidthPercentage(100);
		            		Table22.getDefaultCell().setBorder(Rectangle.BOX);
				            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table22.addCell(new Phrase(new Chunk(nf0.format(temp_srno),small_black_normal)));
				            
				            Table22.getDefaultCell().setBorder(Rectangle.BOX);
				            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
				            
				            Table22.getDefaultCell().setBorder(Rectangle.BOX);
				            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
				            Table22.addCell(new Phrase(new Chunk(tax_name.split(",")[i],small_black_normal)));
				            
				            Table22.getDefaultCell().setBorder(Rectangle.BOX);
				            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
				            
				            Table22.getDefaultCell().setBorder(Rectangle.BOX);
				            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
				            
				            Table22.getDefaultCell().setBorder(Rectangle.BOX);
				            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
				
				            Table22.getDefaultCell().setBorder(Rectangle.BOX);
				            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table22.addCell(new Phrase(new Chunk(curr1,small_black_normal)));
				
				            Table22.getDefaultCell().setBorder(Rectangle.BOX);
				            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
				            Table22.addCell(new Phrase(new Chunk(es.formatAmount(""+Double.parseDouble(""+sub_tax_amt)),small_black_normal)));
						}
					}
	
		            Table22.setWidthPercentage(100);
		            Table22.getDefaultCell().setBorder(Rectangle.BOX);
		            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table22.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
		
		            Table22.getDefaultCell().setBorder(Rectangle.BOX);
		            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
		            
		            Table22.getDefaultCell().setBorder(Rectangle.BOX);
		            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            Table22.addCell(new Phrase(new Chunk("Tax ("+tax_name+")",small_black_bold)));
		            
		            Table22.getDefaultCell().setBorder(Rectangle.BOX);
		            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
		            
		            Table22.getDefaultCell().setBorder(Rectangle.BOX);
		            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
		            
		            Table22.getDefaultCell().setBorder(Rectangle.BOX);
		            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
		
		            Table22.getDefaultCell().setBorder(Rectangle.BOX);
		            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table22.addCell(new Phrase(new Chunk(curr1,small_black_bold)));
		
		            Table22.getDefaultCell().setBorder(Rectangle.BOX);
		            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table22.addCell(new Phrase(new Chunk(es.formatAmount(tax_amt),small_black_bold)));
		            
		            sr+=1;
		            float[] ContactAddrWidths25 = {0.12F,0.20F,0.70F,0.15F,0.17F,0.23F,0.17F,0.20F};
		   	     	PdfPTable Table23 = new PdfPTable(ContactAddrWidths25);
		            Table23.setWidthPercentage(100);
		            Table23.getDefaultCell().setBorder(Rectangle.BOX);
		            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table23.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
		            
		            Table23.getDefaultCell().setBorder(Rectangle.BOX);
		            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table23.addCell(new Phrase(new Chunk("",small_black_normal)));
		            
		            Table23.getDefaultCell().setBorder(Rectangle.BOX);
		            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            Table23.addCell(new Phrase(new Chunk("Total Amount in INR",small_black_bold)));
		            
		            Table23.getDefaultCell().setBorder(Rectangle.BOX);
		            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table23.addCell(new Phrase(new Chunk("",small_black_normal)));
		            
		            Table23.getDefaultCell().setBorder(Rectangle.BOX);
		            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table23.addCell(new Phrase(new Chunk("",small_black_normal)));
		            
		            Table23.getDefaultCell().setBorder(Rectangle.BOX);
		            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table23.addCell(new Phrase(new Chunk("",small_black_normal)));
		
		            Table23.getDefaultCell().setBorder(Rectangle.BOX);
		            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table23.addCell(new Phrase(new Chunk(curr1,small_black_bold)));
		
		            Table23.getDefaultCell().setBorder(Rectangle.BOX);
		            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table23.addCell(new Phrase(new Chunk(es.formatAmount(net_amt),small_black_bold)));
		            
		            float[] ContactAddrWidths26_1 = {0.100F};
		   	     	PdfPTable Table24_1 = new PdfPTable(ContactAddrWidths26_1);
		   	     	Table24_1.setWidthPercentage(100);
		   	     	Table24_1.getDefaultCell().setBorder(Rectangle.BOX);
		   	     	Table24_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		   	     	Table24_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		   	     	Table24_1.addCell(new Phrase(new Chunk("Amount in Words | Rupees "+amt_in_word+" Only",black_bold)));
		   	  		
		   	     	float[] ContactAddrWidths26 = {0.100F};
		   	     	PdfPTable Table24 = new PdfPTable(ContactAddrWidths26);
		   	     	Table24.setWidthPercentage(100);
		   	     	Table24.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		   	     	Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		   	  		Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		   	  		Table24.addCell(new Phrase(new Chunk("For "+supp_nm,black_bold)));
		
		   	  		float[] ContactAddrWidths27 = {0.100F};
		   	  		PdfPTable Table25 = new PdfPTable(ContactAddrWidths27);
			     	Table25.setWidthPercentage(100);
			     	Table25.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			     	Table25.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			     	Table25.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
			     	Table25.addCell(new Phrase(new Chunk("Authorised Signatory",black_bold)));
			     	
			     	PdfPTable BillingFieldsInfoTable81 = new PdfPTable(1);
				     BillingFieldsInfoTable81.setWidthPercentage(20);
				     BillingFieldsInfoTable81.setHorizontalAlignment(Element.ALIGN_LEFT);
				     BillingFieldsInfoTable81.getDefaultCell().setBorder(Rectangle.BOX);
				     BillingFieldsInfoTable81.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				     BillingFieldsInfoTable81.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		
				     PdfFormField field = PdfFormField.createSignature(writer);
				     field.setFieldName(SIGNAME);
		
				     field.setPage();
				     field.setWidget(new Rectangle(72, 732, 144, 780), PdfAnnotation.HIGHLIGHT_NONE);
				     field.setFlags(PdfAnnotation.FLAGS_PRINT);
				     writer.addAnnotation(field);
		
				     PdfPCell sigCell = new PdfPCell();
				     FieldPositioningEvents events = new FieldPositioningEvents(writer, field);
				     sigCell.setCellEvent(events);
				     sigCell.setBorder(Rectangle.NO_BORDER);
				     sigCell.setFixedHeight(50f);
				     BillingFieldsInfoTable81.addCell(sigCell);
				    
				     PdfPTable leftSignTable = new PdfPTable(1);
				     leftSignTable.setWidthPercentage(100);
				     leftSignTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				     leftSignTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		
				     leftSignTable.addCell(Table24);
				     leftSignTable.addCell(BillingFieldsInfoTable81);
				     leftSignTable.addCell(Table25);
		
				     float[] InvoiceDateInfoWidths_qr = {0.30f, 0.20f, 0.40f};
				     PdfPTable InvoiceDateInfoTable_qr = new PdfPTable(InvoiceDateInfoWidths_qr);
		
				     if (!irn_no.equals("") && !qr_code.equals("")) {
		
				         int width = 90;
				         int height = 90;
				         Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
				         hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		
				         QRCodeWriter qrCodeWriter = new QRCodeWriter();
				         BitMatrix matrix = qrCodeWriter.encode(qr_code, BarcodeFormat.QR_CODE, width, height, hintMap);
		
				         BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
				         for (int x = 0; x < width; x++) {
				             for (int y = 0; y < height; y++) {
				                 image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
				             }
				         }
		
				         ByteArrayOutputStream baos = new ByteArrayOutputStream();
				         ImageIO.write(image, "png", baos);
				         Image qr_codeimg = Image.getInstance(baos.toByteArray());
				         qr_codeimg.setBorder(Rectangle.NO_BORDER);
				         qr_codeimg.setAlignment(Element.ALIGN_LEFT);
		
				         PdfPCell qrcode_cell = new PdfPCell(qr_codeimg, false);
				         qrcode_cell.setBorder(Rectangle.NO_BORDER);
				         qrcode_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		
				         String char_16 = irn_no.substring(0, 16);
				         String char_32 = irn_no.substring(16, 32);
				         String char_48 = irn_no.substring(32, 48);
				         String char_64 = irn_no.substring(48);
				         String final_irn_no = char_16 + "\n" + char_32 + "\n" + char_48 + "\n" + char_64;
		
				         PdfPTable InvoiceDateInfoTable_qr1 = new PdfPTable(1);
				         InvoiceDateInfoTable_qr1.setWidthPercentage(100);
				         InvoiceDateInfoTable_qr1.getDefaultCell().setBorder(Rectangle.BOX);
				         InvoiceDateInfoTable_qr1.addCell(qrcode_cell);
		
				         PdfPTable InvoiceDateInfoTable_qr11 = new PdfPTable(1);
				         InvoiceDateInfoTable_qr11.setWidthPercentage(100);
				         InvoiceDateInfoTable_qr11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				         InvoiceDateInfoTable_qr11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				         InvoiceDateInfoTable_qr11.addCell(new Phrase(new Chunk("IRN", black_bold)));
				         InvoiceDateInfoTable_qr11.addCell(new Phrase(new Chunk(final_irn_no, small_black_normal)));
		
				         InvoiceDateInfoTable_qr.setWidthPercentage(100);
				         InvoiceDateInfoTable_qr.getDefaultCell().setBorder(Rectangle.BOX);
				         InvoiceDateInfoTable_qr.addCell(InvoiceDateInfoTable_qr11);
		
				         InvoiceDateInfoTable_qr.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				         InvoiceDateInfoTable_qr.addCell("");
		
				         InvoiceDateInfoTable_qr.addCell(InvoiceDateInfoTable_qr1);
				     }
		
				     PdfPTable finalRow = new PdfPTable(3);
				     finalRow.setWidthPercentage(100);
				     finalRow.setWidths(new float[]{0.3f, 0.2f ,0.5f});
				     finalRow.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		
				     finalRow.addCell(leftSignTable);
				     finalRow.addCell("");
				     finalRow.addCell(InvoiceDateInfoTable_qr);
	
					document.add(mainTable);
					document.add(new Paragraph("  "));
					document.add(Table3);
					document.add(Table4);
					document.add(Table5);
					document.add(Table6);
					document.add(Table7);
					document.add(Table8);
					document.add(Table9);
					document.add(Table10);
					document.add(Table13);
					document.add(new Paragraph("  "));
					document.add(Table14);
					document.add(Table15);
					document.add(new Paragraph("  "));
					document.add(Table18);
					document.add(Table19);
					document.add(Table21);
					document.add(Table22);
					document.add(Table23);
					document.add(new Paragraph("  "));
					document.add(Table24_1);
					document.add(new Paragraph("  "));
					document.add(finalRow);
				
				}
	            else if(invoice_category.equals("P"))
	            {
	                if(cess_flag.equals("true"))
	                {
			            float[] ContactAddrWidths20 = {0.12F,0.20F,0.70F,0.15F,0.17F,0.20F,0.17F,0.17F,0.17F,0.20F};
			   	     	PdfPTable Table18 = new PdfPTable(ContactAddrWidths20);
			            Table18.setWidthPercentage(100);
			            Table18.getDefaultCell().setBorder(Rectangle.BOX);
			            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table18.addCell(new Phrase(new Chunk("Sr.No.",small_black_bold)));
			            
			            Table18.getDefaultCell().setBorder(Rectangle.BOX);
			            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table18.addCell(new Phrase(new Chunk("HSN/SAC Code",small_black_bold)));
			
			            Table18.getDefaultCell().setBorder(Rectangle.BOX);
			            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table18.addCell(new Phrase(new Chunk("Description",small_black_bold)));
			            
			            Table18.getDefaultCell().setBorder(Rectangle.BOX);
			            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table18.addCell(new Phrase(new Chunk("UOM",small_black_bold)));
			            
			            Table18.getDefaultCell().setBorder(Rectangle.BOX);
			            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table18.addCell(new Phrase(new Chunk("Quantity",small_black_bold)));
			            
			            Table18.getDefaultCell().setBorder(Rectangle.BOX);
			            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table18.addCell(new Phrase(new Chunk("Rate per Qty",small_black_bold)));
			            
			            Table18.getDefaultCell().setBorder(Rectangle.BOX);
			            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table18.addCell(new Phrase(new Chunk("Tax Rate(%)",small_black_bold)));
			            
			            Table18.getDefaultCell().setBorder(Rectangle.BOX);
			            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table18.addCell(new Phrase(new Chunk("Cess(%)",small_black_bold)));
			
			            Table18.getDefaultCell().setBorder(Rectangle.BOX);
			            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table18.addCell(new Phrase(new Chunk("Currency",small_black_bold)));
			
			            Table18.getDefaultCell().setBorder(Rectangle.BOX);
			            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table18.addCell(new Phrase(new Chunk("Amount",small_black_bold)));
		
			            
			            String curr1 = "INR";
			            
			            int sr=0;
			            float[] ContactAddrWidths21 = {0.12F,0.20F,0.70F,0.15F,0.17F,0.20F,0.17F,0.17F,0.17F,0.20F};
			   	     	PdfPTable Table19 = new PdfPTable(ContactAddrWidths21);
			   	     	
			            for(int i = 0;i<VQTY.size();i++) 
			            {
			            	sr += 1;
							Table19.setWidthPercentage(100);
							Table19.getDefaultCell().setBorder(Rectangle.BOX);
							Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
							Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
							Table19.addCell(new Phrase(new Chunk("" + sr, small_black_normal)));
							
							Table19.getDefaultCell().setBorder(Rectangle.BOX);
							Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
							Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
							Table19.addCell(new Phrase(new Chunk(""+VSAC_CODE.elementAt(i), small_black_normal)));
							
							Table19.getDefaultCell().setBorder(Rectangle.BOX);
							Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
							Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
							Table19.addCell(new Phrase(new Chunk(""+VGAMT_DES.elementAt(i), small_black_normal)));
							
							Table19.getDefaultCell().setBorder(Rectangle.BOX);
							Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
							Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
							Table19.addCell(new Phrase(new Chunk(""+VUOM_NO.elementAt(i), small_black_normal)));
							
							Table19.getDefaultCell().setBorder(Rectangle.BOX);
							Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
							Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
							Table19.addCell(new Phrase(new Chunk(nf.format(Double.parseDouble("" + VQTY.elementAt(i))), small_black_normal)));
							
							Table19.getDefaultCell().setBorder(Rectangle.BOX);
							Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
							Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
							Table19.addCell(new Phrase(new Chunk(nf.format(Double.parseDouble("" + VPRICE.elementAt(i))), small_black_normal)));
							
							Table19.getDefaultCell().setBorder(Rectangle.BOX);
							Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
							Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
							Table19.addCell(new Phrase(new Chunk(VTAX_STRUCTURE_DESC.elementAt(i).toString().replace(","," ").split(" ")[1], small_black_normal)));
							
							Table19.getDefaultCell().setBorder(Rectangle.BOX);
							Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
							Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
							Table19.addCell(new Phrase(new Chunk(VCESS_PER.elementAt(i)+"%", small_black_normal)));
							
							Table19.getDefaultCell().setBorder(Rectangle.BOX);
							Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
							Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
							Table19.addCell(new Phrase(new Chunk(curr1, small_black_normal)));
							
							Table19.getDefaultCell().setBorder(Rectangle.BOX);
							Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
							Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
							Table19.addCell(new Phrase(new Chunk(es.formatAmount(""+VGITEM_AMT.elementAt(i)), small_black_normal)));
						}
						
			            sr+=1;
			            float[] ContactAddrWidths23 = {0.12F,0.20F,0.70F,0.15F,0.17F,0.20F,0.17F,0.17F,0.17F,0.20F};
			   	     	PdfPTable Table21 = new PdfPTable(ContactAddrWidths23);
			            Table21.setWidthPercentage(100);
			            
			            Table21.getDefaultCell().setBorder(Rectangle.BOX);
			            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table21.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
			            
			            Table21.getDefaultCell().setBorder(Rectangle.BOX);
			            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table21.addCell(new Phrase(new Chunk("",small_black_normal)));
			
			            Table21.getDefaultCell().setBorder(Rectangle.BOX);
			            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
			            Table21.addCell(new Phrase(new Chunk("Total Amount ",small_black_bold)));
			            
			            Table21.getDefaultCell().setBorder(Rectangle.BOX);
			            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table21.addCell(new Phrase(new Chunk("",small_black_normal)));
			            
			            Table21.getDefaultCell().setBorder(Rectangle.BOX);
			            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table21.addCell(new Phrase(new Chunk("",small_black_normal)));
			            
			            Table21.getDefaultCell().setBorder(Rectangle.BOX);
			            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table21.addCell(new Phrase(new Chunk("",small_black_normal)));
			            
			            Table21.getDefaultCell().setBorder(Rectangle.BOX);
			            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table21.addCell(new Phrase(new Chunk("",small_black_normal)));
			            
			            Table21.getDefaultCell().setBorder(Rectangle.BOX);
			            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table21.addCell(new Phrase(new Chunk("",small_black_normal)));
			
			            Table21.getDefaultCell().setBorder(Rectangle.BOX);
			            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table21.addCell(new Phrase(new Chunk(curr1,small_black_bold)));
			
			            Table21.getDefaultCell().setBorder(Rectangle.BOX);
			            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            Table21.addCell(new Phrase(new Chunk(es.formatAmount(gross_amt),small_black_bold)));
			            
			            sr+=1;
			            float[] ContactAddrWidths24 = {0.12F,0.20F,0.70F,0.15F,0.17F,0.20F,0.17F,0.17F,0.17F,0.20F};
			   	     	PdfPTable Table22 = new PdfPTable(ContactAddrWidths24);
			            
			   	     	double temp_srno=sr;
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
				            		Table22.setWidthPercentage(100);
				            		Table22.getDefaultCell().setBorder(Rectangle.BOX);
						            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						            Table22.addCell(new Phrase(new Chunk(nf0.format(temp_srno),small_black_normal)));
						            
						            Table22.getDefaultCell().setBorder(Rectangle.BOX);
						            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
						            
						            Table22.getDefaultCell().setBorder(Rectangle.BOX);
						            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
						            Table22.addCell(new Phrase(new Chunk(parts[j].trim(),small_black_normal)));
						            
						            Table22.getDefaultCell().setBorder(Rectangle.BOX);
						            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
						            
						            Table22.getDefaultCell().setBorder(Rectangle.BOX);
						            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
						            
						            Table22.getDefaultCell().setBorder(Rectangle.BOX);
						            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
						            
						            Table22.getDefaultCell().setBorder(Rectangle.BOX);
						            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
						            
						            Table22.getDefaultCell().setBorder(Rectangle.BOX);
						            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
						
						            Table22.getDefaultCell().setBorder(Rectangle.BOX);
						            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						            Table22.addCell(new Phrase(new Chunk(curr1,small_black_normal)));
						
						            Table22.getDefaultCell().setBorder(Rectangle.BOX);
						            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
						            Table22.addCell(new Phrase(new Chunk(es.formatAmount(""+Double.parseDouble(""+sub_tax_amt)),small_black_normal)));
								}
							}
						    
						    else 
						    {
						    	temp_srno=temp_srno+0.1;
			            		Table22.setWidthPercentage(100);
			            		Table22.getDefaultCell().setBorder(Rectangle.BOX);
					            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
					            Table22.addCell(new Phrase(new Chunk(nf0.format(temp_srno),small_black_normal)));
					            
					            Table22.getDefaultCell().setBorder(Rectangle.BOX);
					            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
					            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
					            
					            Table22.getDefaultCell().setBorder(Rectangle.BOX);
					            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
					            Table22.addCell(new Phrase(new Chunk(desc,small_black_normal)));
					            
					            Table22.getDefaultCell().setBorder(Rectangle.BOX);
					            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
					            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
					            
					            Table22.getDefaultCell().setBorder(Rectangle.BOX);
					            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
					            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
					            
					            Table22.getDefaultCell().setBorder(Rectangle.BOX);
					            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
					            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
					            
					            Table22.getDefaultCell().setBorder(Rectangle.BOX);
					            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
					            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
					            
					            Table22.getDefaultCell().setBorder(Rectangle.BOX);
					            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
					            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
					
					            Table22.getDefaultCell().setBorder(Rectangle.BOX);
					            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
					            Table22.addCell(new Phrase(new Chunk(curr1,small_black_normal)));
					
					            Table22.getDefaultCell().setBorder(Rectangle.BOX);
					            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
					            Table22.addCell(new Phrase(new Chunk(es.formatAmount(""+tax_amt),small_black_normal)));
						    }
			   	     	}
			   	     	
			            Table22.setWidthPercentage(100);
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table22.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
			
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
			            
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
			            Table22.addCell(new Phrase(new Chunk("Total Tax Amount",small_black_bold)));
			            
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
			            
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
			            
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
			            
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
			            
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
			
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table22.addCell(new Phrase(new Chunk(curr1,small_black_bold)));
			
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            Table22.addCell(new Phrase(new Chunk(es.formatAmount(tax_amt),small_black_bold)));
			            
			            sr+=1;
			            float[] ContactAddrWidths19 = {0.12F,0.20F,0.70F,0.15F,0.17F,0.20F,0.17F,0.17F,0.17F,0.20F};
			   	     	PdfPTable Table17 = new PdfPTable(ContactAddrWidths19);
			            Table17.setWidthPercentage(100);
			            Table17.getDefaultCell().setBorder(Rectangle.BOX);
			            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table17.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
		
			            Table17.getDefaultCell().setBorder(Rectangle.BOX);
			            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table17.addCell(new Phrase(new Chunk("",small_black_normal)));
			
			            Table17.getDefaultCell().setBorder(Rectangle.BOX);
			            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
			            Table17.addCell(new Phrase(new Chunk("Total Cess",black_bold)));
			            
			            Table17.getDefaultCell().setBorder(Rectangle.BOX);
			            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table17.addCell(new Phrase(new Chunk("",small_black_normal)));
			            
			            Table17.getDefaultCell().setBorder(Rectangle.BOX);
			            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table17.addCell(new Phrase(new Chunk("",small_black_normal)));
			            
			            Table17.getDefaultCell().setBorder(Rectangle.BOX);
			            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table17.addCell(new Phrase(new Chunk("",small_black_normal)));
			            
			            Table17.getDefaultCell().setBorder(Rectangle.BOX);
			            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table17.addCell(new Phrase(new Chunk("",small_black_normal)));
			            
			            Table17.getDefaultCell().setBorder(Rectangle.BOX);
			            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table17.addCell(new Phrase(new Chunk("",small_black_normal)));
			            
			            Table17.getDefaultCell().setBorder(Rectangle.BOX);
			            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table17.addCell(new Phrase(new Chunk(curr1,black_bold)));
		
			            Table17.getDefaultCell().setBorder(Rectangle.BOX);
			            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            Table17.addCell(new Phrase(new Chunk(es.formatAmount(cess_amt),black_bold)));
			            
			            sr+=1;
			            float[] ContactAddrWidths25 = {0.12F,0.20F,0.70F,0.15F,0.17F,0.20F,0.17F,0.17F,0.17F,0.20F};
			   	     	PdfPTable Table23 = new PdfPTable(ContactAddrWidths25);
			            Table23.setWidthPercentage(100);
			            Table23.getDefaultCell().setBorder(Rectangle.BOX);
			            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table23.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
			            
			            Table23.getDefaultCell().setBorder(Rectangle.BOX);
			            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table23.addCell(new Phrase(new Chunk("",small_black_normal)));
			            
			            Table23.getDefaultCell().setBorder(Rectangle.BOX);
			            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
			            Table23.addCell(new Phrase(new Chunk("Total Amount in INR",small_black_bold)));
			            
			            Table23.getDefaultCell().setBorder(Rectangle.BOX);
			            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table23.addCell(new Phrase(new Chunk("",small_black_normal)));
			            
			            Table23.getDefaultCell().setBorder(Rectangle.BOX);
			            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table23.addCell(new Phrase(new Chunk("",small_black_normal)));
			            
			            Table23.getDefaultCell().setBorder(Rectangle.BOX);
			            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table23.addCell(new Phrase(new Chunk("",small_black_normal)));
			            
			            Table23.getDefaultCell().setBorder(Rectangle.BOX);
			            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table23.addCell(new Phrase(new Chunk("",small_black_normal)));
			            
			            Table23.getDefaultCell().setBorder(Rectangle.BOX);
			            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table23.addCell(new Phrase(new Chunk("",small_black_normal)));
			            
			            Table23.getDefaultCell().setBorder(Rectangle.BOX);
			            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table23.addCell(new Phrase(new Chunk(curr1,small_black_bold)));
			
			            Table23.getDefaultCell().setBorder(Rectangle.BOX);
			            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            Table23.addCell(new Phrase(new Chunk(es.formatAmount(net_amt),small_black_bold)));
			            
			            float[] ContactAddrWidths26_1 = {0.100F};
			   	     	PdfPTable Table24_1 = new PdfPTable(ContactAddrWidths26_1);
			   	     	Table24_1.setWidthPercentage(100);
			   	     	Table24_1.getDefaultCell().setBorder(Rectangle.BOX);
			   	     	Table24_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			   	     	Table24_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
			   	     	Table24_1.addCell(new Phrase(new Chunk("Amount in Words | Rupees "+amt_in_word+" Only",black_bold)));
			   	  		
			   	     	float[] ContactAddrWidths26 = {0.100F};
			   	     	PdfPTable Table24 = new PdfPTable(ContactAddrWidths26);
			   	     	Table24.setWidthPercentage(100);
			   	     	Table24.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			   	     	Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			   	  		Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
			   	  		Table24.addCell(new Phrase(new Chunk("For "+supp_nm,black_bold)));
			
			   	  		float[] ContactAddrWidths27 = {0.100F};
			   	  		PdfPTable Table25 = new PdfPTable(ContactAddrWidths27);
				     	Table25.setWidthPercentage(100);
				     	Table25.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				     	Table25.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				     	Table25.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				     	Table25.addCell(new Phrase(new Chunk("Authorised Signatory",black_bold)));
				     	
				     	PdfPTable BillingFieldsInfoTable81 = new PdfPTable(1);
					     BillingFieldsInfoTable81.setWidthPercentage(20);
					     BillingFieldsInfoTable81.setHorizontalAlignment(Element.ALIGN_LEFT);
					     BillingFieldsInfoTable81.getDefaultCell().setBorder(Rectangle.BOX);
					     BillingFieldsInfoTable81.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					     BillingFieldsInfoTable81.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
			
					     PdfFormField field = PdfFormField.createSignature(writer);
					     field.setFieldName(SIGNAME);
			
					     field.setPage();
					     field.setWidget(new Rectangle(72, 732, 144, 780), PdfAnnotation.HIGHLIGHT_NONE);
					     field.setFlags(PdfAnnotation.FLAGS_PRINT);
					     writer.addAnnotation(field);
			
					     PdfPCell sigCell = new PdfPCell();
					     FieldPositioningEvents events = new FieldPositioningEvents(writer, field);
					     sigCell.setCellEvent(events);
					     sigCell.setBorder(Rectangle.NO_BORDER);
					     sigCell.setFixedHeight(50f);
					     BillingFieldsInfoTable81.addCell(sigCell);
					    
					     PdfPTable leftSignTable = new PdfPTable(1);
					     leftSignTable.setWidthPercentage(100);
					     leftSignTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					     leftSignTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			
					     leftSignTable.addCell(Table24);
					     leftSignTable.addCell(BillingFieldsInfoTable81);
					     leftSignTable.addCell(Table25);
			
					     float[] InvoiceDateInfoWidths_qr = {0.30f, 0.20f, 0.40f};
					     PdfPTable InvoiceDateInfoTable_qr = new PdfPTable(InvoiceDateInfoWidths_qr);
			
					     if (!irn_no.equals("") && !qr_code.equals("")) {
			
					         int width = 90;
					         int height = 90;
					         Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
					         hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
			
					         QRCodeWriter qrCodeWriter = new QRCodeWriter();
					         BitMatrix matrix = qrCodeWriter.encode(qr_code, BarcodeFormat.QR_CODE, width, height, hintMap);
			
					         BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
					         for (int x = 0; x < width; x++) {
					             for (int y = 0; y < height; y++) {
					                 image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
					             }
					         }
			
					         ByteArrayOutputStream baos = new ByteArrayOutputStream();
					         ImageIO.write(image, "png", baos);
					         Image qr_codeimg = Image.getInstance(baos.toByteArray());
					         qr_codeimg.setBorder(Rectangle.NO_BORDER);
					         qr_codeimg.setAlignment(Element.ALIGN_LEFT);
			
					         PdfPCell qrcode_cell = new PdfPCell(qr_codeimg, false);
					         qrcode_cell.setBorder(Rectangle.NO_BORDER);
					         qrcode_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			
					         String char_16 = irn_no.substring(0, 16);
					         String char_32 = irn_no.substring(16, 32);
					         String char_48 = irn_no.substring(32, 48);
					         String char_64 = irn_no.substring(48);
					         String final_irn_no = char_16 + "\n" + char_32 + "\n" + char_48 + "\n" + char_64;
			
					         PdfPTable InvoiceDateInfoTable_qr1 = new PdfPTable(1);
					         InvoiceDateInfoTable_qr1.setWidthPercentage(100);
					         InvoiceDateInfoTable_qr1.getDefaultCell().setBorder(Rectangle.BOX);
					         InvoiceDateInfoTable_qr1.addCell(qrcode_cell);
			
					         PdfPTable InvoiceDateInfoTable_qr11 = new PdfPTable(1);
					         InvoiceDateInfoTable_qr11.setWidthPercentage(100);
					         InvoiceDateInfoTable_qr11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					         InvoiceDateInfoTable_qr11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					         InvoiceDateInfoTable_qr11.addCell(new Phrase(new Chunk("IRN", black_bold)));
					         InvoiceDateInfoTable_qr11.addCell(new Phrase(new Chunk(final_irn_no, small_black_normal)));
			
					         InvoiceDateInfoTable_qr.setWidthPercentage(100);
					         InvoiceDateInfoTable_qr.getDefaultCell().setBorder(Rectangle.BOX);
					         InvoiceDateInfoTable_qr.addCell(InvoiceDateInfoTable_qr11);
			
					         InvoiceDateInfoTable_qr.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					         InvoiceDateInfoTable_qr.addCell("");
			
					         InvoiceDateInfoTable_qr.addCell(InvoiceDateInfoTable_qr1);
					     }
			
					     PdfPTable finalRow = new PdfPTable(3);
					     finalRow.setWidthPercentage(100);
					     finalRow.setWidths(new float[]{0.3f, 0.2f ,0.5f});
					     finalRow.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			
					     finalRow.addCell(leftSignTable);
					     finalRow.addCell("");
					     finalRow.addCell(InvoiceDateInfoTable_qr);
		
						document.add(mainTable);
						document.add(new Paragraph("  "));
						document.add(Table3);
						document.add(Table4);
						document.add(Table5);
						document.add(Table6);
						document.add(Table7);
						document.add(Table8);
						document.add(Table9);
						document.add(Table10);
						document.add(Table13);
						document.add(new Paragraph("  "));
						document.add(Table14);
						document.add(Table15);
						document.add(new Paragraph("  "));
						document.add(Table18);
						document.add(Table19);
						document.add(Table21);
						document.add(Table22);
						document.add(Table17);
						document.add(Table23);
						document.add(new Paragraph("  "));
						document.add(Table24_1);
						document.add(new Paragraph("  "));
						document.add(finalRow);
	                }
	                else 
	                {
	                	 float[] ContactAddrWidths20 = {0.12F,0.20F,0.70F,0.15F,0.17F,0.20F,0.17F,0.17F,0.20F};
				   	     	PdfPTable Table18 = new PdfPTable(ContactAddrWidths20);
				            Table18.setWidthPercentage(100);
				            Table18.getDefaultCell().setBorder(Rectangle.BOX);
				            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table18.addCell(new Phrase(new Chunk("Sr.No.",small_black_bold)));
				            
				            Table18.getDefaultCell().setBorder(Rectangle.BOX);
				            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table18.addCell(new Phrase(new Chunk("HSN/SAC Code",small_black_bold)));
				
				            Table18.getDefaultCell().setBorder(Rectangle.BOX);
				            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table18.addCell(new Phrase(new Chunk("Description",small_black_bold)));
				            
				            Table18.getDefaultCell().setBorder(Rectangle.BOX);
				            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table18.addCell(new Phrase(new Chunk("UOM",small_black_bold)));
				            
				            Table18.getDefaultCell().setBorder(Rectangle.BOX);
				            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table18.addCell(new Phrase(new Chunk("Quantity",small_black_bold)));
				            
				            Table18.getDefaultCell().setBorder(Rectangle.BOX);
				            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table18.addCell(new Phrase(new Chunk("Rate per Qty",small_black_bold)));
				            
				            Table18.getDefaultCell().setBorder(Rectangle.BOX);
				            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table18.addCell(new Phrase(new Chunk("Tax Rate(%)",small_black_bold)));
				
				            Table18.getDefaultCell().setBorder(Rectangle.BOX);
				            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table18.addCell(new Phrase(new Chunk("Currency",small_black_bold)));
				
				            Table18.getDefaultCell().setBorder(Rectangle.BOX);
				            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table18.addCell(new Phrase(new Chunk("Amount",small_black_bold)));
			
				            
				            String curr1 = "INR";
				            
				            int sr=0;
				            float[] ContactAddrWidths21 = {0.12F,0.20F,0.70F,0.15F,0.17F,0.20F,0.17F,0.17F,0.20F};
				   	     	PdfPTable Table19 = new PdfPTable(ContactAddrWidths21);
				   	     	
				            for(int i = 0;i<VQTY.size();i++) 
				            {
				            	sr += 1;
								Table19.setWidthPercentage(100);
								Table19.getDefaultCell().setBorder(Rectangle.BOX);
								Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
								Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
								Table19.addCell(new Phrase(new Chunk("" + sr, small_black_normal)));
								
								Table19.getDefaultCell().setBorder(Rectangle.BOX);
								Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
								Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
								Table19.addCell(new Phrase(new Chunk(""+VSAC_CODE.elementAt(i), small_black_normal)));
								
								Table19.getDefaultCell().setBorder(Rectangle.BOX);
								Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
								Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
								Table19.addCell(new Phrase(new Chunk(""+VGAMT_DES.elementAt(i), small_black_normal)));
								
								Table19.getDefaultCell().setBorder(Rectangle.BOX);
								Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
								Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
								Table19.addCell(new Phrase(new Chunk(""+VUOM_NO.elementAt(i), small_black_normal)));
								
								Table19.getDefaultCell().setBorder(Rectangle.BOX);
								Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
								Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
								Table19.addCell(new Phrase(new Chunk(nf.format(Double.parseDouble("" + VQTY.elementAt(i))), small_black_normal)));
								
								Table19.getDefaultCell().setBorder(Rectangle.BOX);
								Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
								Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
								Table19.addCell(new Phrase(new Chunk(nf.format(Double.parseDouble("" + VPRICE.elementAt(i))), small_black_normal)));
								
								Table19.getDefaultCell().setBorder(Rectangle.BOX);
								Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
								Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
								Table19.addCell(new Phrase(new Chunk(VTAX_STRUCTURE_DESC.elementAt(i).toString().replace(","," ").split(" ")[1], small_black_normal)));
								
								Table19.getDefaultCell().setBorder(Rectangle.BOX);
								Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
								Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
								Table19.addCell(new Phrase(new Chunk(curr1, small_black_normal)));
								
								Table19.getDefaultCell().setBorder(Rectangle.BOX);
								Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
								Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
								Table19.addCell(new Phrase(new Chunk(es.formatAmount(""+VGITEM_AMT.elementAt(i)), small_black_normal)));
							}
							
				            sr+=1;
				            float[] ContactAddrWidths23 = {0.12F,0.20F,0.70F,0.15F,0.17F,0.20F,0.17F,0.17F,0.20F};
				   	     	PdfPTable Table21 = new PdfPTable(ContactAddrWidths23);
				            Table21.setWidthPercentage(100);
				            
				            Table21.getDefaultCell().setBorder(Rectangle.BOX);
				            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table21.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
				            
				            Table21.getDefaultCell().setBorder(Rectangle.BOX);
				            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table21.addCell(new Phrase(new Chunk("",small_black_normal)));
				
				            Table21.getDefaultCell().setBorder(Rectangle.BOX);
				            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				            Table21.addCell(new Phrase(new Chunk("Total Amount ",small_black_bold)));
				            
				            Table21.getDefaultCell().setBorder(Rectangle.BOX);
				            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table21.addCell(new Phrase(new Chunk("",small_black_normal)));
				            
				            Table21.getDefaultCell().setBorder(Rectangle.BOX);
				            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table21.addCell(new Phrase(new Chunk("",small_black_normal)));
				            
				            Table21.getDefaultCell().setBorder(Rectangle.BOX);
				            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table21.addCell(new Phrase(new Chunk("",small_black_normal)));
				            
				            Table21.getDefaultCell().setBorder(Rectangle.BOX);
				            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table21.addCell(new Phrase(new Chunk("",small_black_normal)));
				            
				            Table21.getDefaultCell().setBorder(Rectangle.BOX);
				            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table21.addCell(new Phrase(new Chunk(curr1,small_black_bold)));
				
				            Table21.getDefaultCell().setBorder(Rectangle.BOX);
				            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
				            Table21.addCell(new Phrase(new Chunk(es.formatAmount(gross_amt),small_black_bold)));
				            
				            sr+=1;
				            float[] ContactAddrWidths24 = {0.12F,0.20F,0.70F,0.15F,0.17F,0.20F,0.17F,0.17F,0.20F};
				   	     	PdfPTable Table22 = new PdfPTable(ContactAddrWidths24);
				            
				   	     	double temp_srno=sr;
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
					            		Table22.setWidthPercentage(100);
					            		Table22.getDefaultCell().setBorder(Rectangle.BOX);
							            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
							            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
							            Table22.addCell(new Phrase(new Chunk(nf0.format(temp_srno),small_black_normal)));
							            
							            Table22.getDefaultCell().setBorder(Rectangle.BOX);
							            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
							            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
							            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
							            
							            Table22.getDefaultCell().setBorder(Rectangle.BOX);
							            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
							            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
							            Table22.addCell(new Phrase(new Chunk(parts[j].trim(),small_black_normal)));
							            
							            Table22.getDefaultCell().setBorder(Rectangle.BOX);
							            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
							            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
							            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
							            
							            Table22.getDefaultCell().setBorder(Rectangle.BOX);
							            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
							            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
							            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
							            
							            Table22.getDefaultCell().setBorder(Rectangle.BOX);
							            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
							            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
							            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
							            
							            Table22.getDefaultCell().setBorder(Rectangle.BOX);
							            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
							            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
							            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
							            
							            Table22.getDefaultCell().setBorder(Rectangle.BOX);
							            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
							            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
							            Table22.addCell(new Phrase(new Chunk(curr1,small_black_normal)));
							
							            Table22.getDefaultCell().setBorder(Rectangle.BOX);
							            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
							            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
							            Table22.addCell(new Phrase(new Chunk(es.formatAmount(""+Double.parseDouble(""+sub_tax_amt)),small_black_normal)));
									}
								}
							    
							    else 
							    {
							    	temp_srno=temp_srno+0.1;
				            		Table22.setWidthPercentage(100);
				            		Table22.getDefaultCell().setBorder(Rectangle.BOX);
						            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						            Table22.addCell(new Phrase(new Chunk(nf0.format(temp_srno),small_black_normal)));
						            
						            Table22.getDefaultCell().setBorder(Rectangle.BOX);
						            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
						            
						            Table22.getDefaultCell().setBorder(Rectangle.BOX);
						            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
						            Table22.addCell(new Phrase(new Chunk(desc,small_black_normal)));
						            
						            Table22.getDefaultCell().setBorder(Rectangle.BOX);
						            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
						            
						            Table22.getDefaultCell().setBorder(Rectangle.BOX);
						            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
						            
						            Table22.getDefaultCell().setBorder(Rectangle.BOX);
						            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
						            
						            Table22.getDefaultCell().setBorder(Rectangle.BOX);
						            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
						            
						            Table22.getDefaultCell().setBorder(Rectangle.BOX);
						            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						            Table22.addCell(new Phrase(new Chunk(curr1,small_black_normal)));
						
						            Table22.getDefaultCell().setBorder(Rectangle.BOX);
						            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
						            Table22.addCell(new Phrase(new Chunk(es.formatAmount(""+tax_amt),small_black_normal)));
							    }
				   	     	}
			
				            Table22.setWidthPercentage(100);
				            Table22.getDefaultCell().setBorder(Rectangle.BOX);
				            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table22.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
				
				            Table22.getDefaultCell().setBorder(Rectangle.BOX);
				            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
				            
				            Table22.getDefaultCell().setBorder(Rectangle.BOX);
				            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				            Table22.addCell(new Phrase(new Chunk("Total Tax Amount",small_black_bold)));
				            
				            Table22.getDefaultCell().setBorder(Rectangle.BOX);
				            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
				            
				            Table22.getDefaultCell().setBorder(Rectangle.BOX);
				            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
				            
				            Table22.getDefaultCell().setBorder(Rectangle.BOX);
				            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
				            
				            Table22.getDefaultCell().setBorder(Rectangle.BOX);
				            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
				
				            Table22.getDefaultCell().setBorder(Rectangle.BOX);
				            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table22.addCell(new Phrase(new Chunk(curr1,small_black_bold)));
				
				            Table22.getDefaultCell().setBorder(Rectangle.BOX);
				            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
				            Table22.addCell(new Phrase(new Chunk(es.formatAmount(tax_amt),small_black_bold)));
				            
				            sr+=1;
				            float[] ContactAddrWidths25 = {0.12F,0.20F,0.70F,0.15F,0.17F,0.20F,0.17F,0.17F,0.20F};
				   	     	PdfPTable Table23 = new PdfPTable(ContactAddrWidths25);
				            Table23.setWidthPercentage(100);
				            Table23.getDefaultCell().setBorder(Rectangle.BOX);
				            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table23.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
				            
				            Table23.getDefaultCell().setBorder(Rectangle.BOX);
				            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table23.addCell(new Phrase(new Chunk("",small_black_normal)));
				            
				            Table23.getDefaultCell().setBorder(Rectangle.BOX);
				            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				            Table23.addCell(new Phrase(new Chunk("Total Amount in INR",small_black_bold)));
				            
				            Table23.getDefaultCell().setBorder(Rectangle.BOX);
				            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table23.addCell(new Phrase(new Chunk("",small_black_normal)));
				            
				            Table23.getDefaultCell().setBorder(Rectangle.BOX);
				            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table23.addCell(new Phrase(new Chunk("",small_black_normal)));
				            
				            Table23.getDefaultCell().setBorder(Rectangle.BOX);
				            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table23.addCell(new Phrase(new Chunk("",small_black_normal)));
				            
				            Table23.getDefaultCell().setBorder(Rectangle.BOX);
				            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table23.addCell(new Phrase(new Chunk("",small_black_normal)));
				
				            Table23.getDefaultCell().setBorder(Rectangle.BOX);
				            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table23.addCell(new Phrase(new Chunk(curr1,small_black_bold)));
				
				            Table23.getDefaultCell().setBorder(Rectangle.BOX);
				            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
				            Table23.addCell(new Phrase(new Chunk(es.formatAmount(net_amt),small_black_bold)));
				            
				            float[] ContactAddrWidths26_1 = {0.100F};
				   	     	PdfPTable Table24_1 = new PdfPTable(ContactAddrWidths26_1);
				   	     	Table24_1.setWidthPercentage(100);
				   	     	Table24_1.getDefaultCell().setBorder(Rectangle.BOX);
				   	     	Table24_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				   	     	Table24_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				   	     	Table24_1.addCell(new Phrase(new Chunk("Amount in Words | Rupees "+amt_in_word+" Only",black_bold)));
				   	  		
				   	     	float[] ContactAddrWidths26 = {0.100F};
				   	     	PdfPTable Table24 = new PdfPTable(ContactAddrWidths26);
				   	     	Table24.setWidthPercentage(100);
				   	     	Table24.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				   	     	Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				   	  		Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				   	  		Table24.addCell(new Phrase(new Chunk("For "+supp_nm,black_bold)));
				
				   	  		float[] ContactAddrWidths27 = {0.100F};
				   	  		PdfPTable Table25 = new PdfPTable(ContactAddrWidths27);
					     	Table25.setWidthPercentage(100);
					     	Table25.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					     	Table25.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					     	Table25.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
					     	Table25.addCell(new Phrase(new Chunk("Authorised Signatory",black_bold)));
					     	
					     	PdfPTable BillingFieldsInfoTable81 = new PdfPTable(1);
						     BillingFieldsInfoTable81.setWidthPercentage(20);
						     BillingFieldsInfoTable81.setHorizontalAlignment(Element.ALIGN_LEFT);
						     BillingFieldsInfoTable81.getDefaultCell().setBorder(Rectangle.BOX);
						     BillingFieldsInfoTable81.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						     BillingFieldsInfoTable81.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				
						     PdfFormField field = PdfFormField.createSignature(writer);
						     field.setFieldName(SIGNAME);
				
						     field.setPage();
						     field.setWidget(new Rectangle(72, 732, 144, 780), PdfAnnotation.HIGHLIGHT_NONE);
						     field.setFlags(PdfAnnotation.FLAGS_PRINT);
						     writer.addAnnotation(field);
				
						     PdfPCell sigCell = new PdfPCell();
						     FieldPositioningEvents events = new FieldPositioningEvents(writer, field);
						     sigCell.setCellEvent(events);
						     sigCell.setBorder(Rectangle.NO_BORDER);
						     sigCell.setFixedHeight(50f);
						     BillingFieldsInfoTable81.addCell(sigCell);
						    
						     PdfPTable leftSignTable = new PdfPTable(1);
						     leftSignTable.setWidthPercentage(100);
						     leftSignTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						     leftSignTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				
						     leftSignTable.addCell(Table24);
						     leftSignTable.addCell(BillingFieldsInfoTable81);
						     leftSignTable.addCell(Table25);
				
						     float[] InvoiceDateInfoWidths_qr = {0.30f, 0.20f, 0.40f};
						     PdfPTable InvoiceDateInfoTable_qr = new PdfPTable(InvoiceDateInfoWidths_qr);
				
						     if (!irn_no.equals("") && !qr_code.equals("")) {
				
						         int width = 90;
						         int height = 90;
						         Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
						         hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
				
						         QRCodeWriter qrCodeWriter = new QRCodeWriter();
						         BitMatrix matrix = qrCodeWriter.encode(qr_code, BarcodeFormat.QR_CODE, width, height, hintMap);
				
						         BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				
						         for (int x = 0; x < width; x++) {
						             for (int y = 0; y < height; y++) {
						                 image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
						             }
						         }
				
						         ByteArrayOutputStream baos = new ByteArrayOutputStream();
						         ImageIO.write(image, "png", baos);
						         Image qr_codeimg = Image.getInstance(baos.toByteArray());
						         qr_codeimg.setBorder(Rectangle.NO_BORDER);
						         qr_codeimg.setAlignment(Element.ALIGN_LEFT);
				
						         PdfPCell qrcode_cell = new PdfPCell(qr_codeimg, false);
						         qrcode_cell.setBorder(Rectangle.NO_BORDER);
						         qrcode_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				
						         String char_16 = irn_no.substring(0, 16);
						         String char_32 = irn_no.substring(16, 32);
						         String char_48 = irn_no.substring(32, 48);
						         String char_64 = irn_no.substring(48);
						         String final_irn_no = char_16 + "\n" + char_32 + "\n" + char_48 + "\n" + char_64;
				
						         PdfPTable InvoiceDateInfoTable_qr1 = new PdfPTable(1);
						         InvoiceDateInfoTable_qr1.setWidthPercentage(100);
						         InvoiceDateInfoTable_qr1.getDefaultCell().setBorder(Rectangle.BOX);
						         InvoiceDateInfoTable_qr1.addCell(qrcode_cell);
				
						         PdfPTable InvoiceDateInfoTable_qr11 = new PdfPTable(1);
						         InvoiceDateInfoTable_qr11.setWidthPercentage(100);
						         InvoiceDateInfoTable_qr11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						         InvoiceDateInfoTable_qr11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						         InvoiceDateInfoTable_qr11.addCell(new Phrase(new Chunk("IRN", black_bold)));
						         InvoiceDateInfoTable_qr11.addCell(new Phrase(new Chunk(final_irn_no, small_black_normal)));
				
						         InvoiceDateInfoTable_qr.setWidthPercentage(100);
						         InvoiceDateInfoTable_qr.getDefaultCell().setBorder(Rectangle.BOX);
						         InvoiceDateInfoTable_qr.addCell(InvoiceDateInfoTable_qr11);
				
						         InvoiceDateInfoTable_qr.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						         InvoiceDateInfoTable_qr.addCell("");
				
						         InvoiceDateInfoTable_qr.addCell(InvoiceDateInfoTable_qr1);
						     }
				
						     PdfPTable finalRow = new PdfPTable(3);
						     finalRow.setWidthPercentage(100);
						     finalRow.setWidths(new float[]{0.3f, 0.2f ,0.5f});
						     finalRow.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				
						     finalRow.addCell(leftSignTable);
						     finalRow.addCell("");
						     finalRow.addCell(InvoiceDateInfoTable_qr);
						     
						    float[] ContactAddrWidths30 = {0.100F};
				   	  		PdfPTable Table28 = new PdfPTable(ContactAddrWidths30);
					     	Table28.setWidthPercentage(100);
					     	Table28.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					     	Table28.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					     	Table28.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
					     	Table28.addCell(new Phrase(new Chunk("Notes:",black_bold)));
					     	
					     	float[] ContactAddrWidths31 = {0.01F,0.200F};
				   	  		PdfPTable Table29 = new PdfPTable(ContactAddrWidths31);
					     	Table29.setWidthPercentage(100);
					     	Table29.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					     	Table29.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					     	Table29.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
					     	Table29.addCell(new Phrase(new Chunk("1. ",small_black_normal)));
					     	
					     	Table29.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					     	Table29.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					     	Table29.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
					     	Table29.addCell(new Phrase(new Chunk(remark1,small_black_normal)));
			
							document.add(mainTable);
							document.add(new Paragraph("  "));
							document.add(Table3);
							document.add(Table4);
							document.add(Table5);
							document.add(Table6);
							document.add(Table7);
							document.add(Table8);
							document.add(Table9);
							document.add(Table10);
							document.add(Table13);
							document.add(new Paragraph("  "));
							document.add(Table14);
							document.add(Table15);
							document.add(new Paragraph("  "));
							document.add(Table18);
							document.add(Table19);
							document.add(Table21);
							document.add(Table22);
							document.add(Table23);
							document.add(new Paragraph("  "));
							document.add(Table24_1);
							document.add(new Paragraph("  "));
							document.add(finalRow);
							document.add(Table28);
							document.add(Table29);
	                }
				
	            }
	            
	            document.close();

	            if(is_print.equals("1"))
				{
		            File isPDFexist = new File(path);
					if(isPDFexist.exists())
					{
						int update_flag=0;
						int pdf_entry=0;
						
						int cont=0;
						queryString="UPDATE FMS_OTH_INVOICE_MST SET PDF_INV_DTL=? ";
							if(print_pdf_type.equals("O"))
							{
								queryString+= ",PRINT_BY_ORI=?,PRINT_DT_ORI=SYSDATE ";
							}
							else if(print_pdf_type.equals("D"))
							{
								queryString+= ",PRINT_BY_DUP=?,PRINT_DT_DUP=SYSDATE ";
							}
							else if(print_pdf_type.equals("T"))
							{
								queryString+= ",PRINT_BY_TRI=?,PRINT_DT_TRI=SYSDATE ";
							}
							queryString+= "WHERE COMPANY_CD=? AND INVOICE_NO=? AND INVOICE_TYPE=? AND INVOICE_SEQ = ? AND SUPPLIER_CD = ?";
							
						stmt=conn.prepareStatement(queryString);
						stmt.setString(++cont, print_pdf_type);
						if(print_pdf_type.equals("O"))
						{
							stmt.setString(++cont, emp_cd);
						}
						else if(print_pdf_type.equals("D"))
						{
							stmt.setString(++cont, emp_cd);
						}
						else if(print_pdf_type.equals("T"))
						{
							stmt.setString(++cont, emp_cd);
						}
						stmt.setString(++cont, comp_cd);
						stmt.setString(++cont, inv_no);
						stmt.setString(++cont, inv_type);
						stmt.setString(++cont, inv_seq);
						stmt.setString(++cont, supp_cd);
						update_flag=stmt.executeUpdate();
						stmt.close();
						
						int count=0;
				        queryString1="SELECT COUNT(*) "
				        		+ "FROM FMS_OTH_INV_FILE_DTL "
				        		+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? "
				        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND SUPPLIER_CD =?";
				        stmt1=conn.prepareStatement(queryString1);
				        stmt1.setString(1, comp_cd);
				        stmt1.setString(2, inv_type);
				        stmt1.setString(3, inv_seq);
				        stmt1.setString(4, fin_yr);
				        stmt1.setString(5, print_pdf_type);
				        stmt1.setString(6, supp_cd);
				        rset1=stmt1.executeQuery();
				        if(rset1.next())
				        {
				        	count=rset1.getInt(1);
				        }
				        rset1.close();
				        stmt1.close();
				        
				        if(count > 0)
				        {
				        	queryString1="UPDATE FMS_OTH_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
				        			+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? "
					        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND SUPPLIER_CD =?";
				        	stmt1=conn.prepareStatement(queryString1);
				        	stmt1.setString(1, file_nm);
				        	stmt1.setString(2, emp_cd);
				        	stmt1.setString(3, comp_cd);
				 	        stmt1.setString(4, inv_type);
				 	        stmt1.setString(5, inv_seq);
				 	        stmt1.setString(6, fin_yr);
				 	        stmt1.setString(7, print_pdf_type); 
				 	        stmt1.setString(8, supp_cd); 
				 	        pdf_entry=stmt1.executeUpdate();
				        	
				        	stmt1.close();
				        }
				        else
				        {
				        	queryString1="INSERT INTO FMS_OTH_INV_FILE_DTL(COMPANY_CD,SUPPLIER_CD,INVOICE_SEQ,INVOICE_TYPE,FINANCIAL_YEAR,PDF_TYPE,"
				        			+ "FILE_NAME,ENT_BY,ENT_DT) "
				        			+ "VALUES(?,?,?,?,?,?,"
				        			+ "?,?,SYSDATE)";
				        	stmt1=conn.prepareStatement(queryString1);
				        	stmt1.setString(1, comp_cd);
				        	stmt1.setString(2, supp_cd);
				 	        stmt1.setString(3, inv_seq);
				 	        stmt1.setString(4, inv_type);
				 	        stmt1.setString(5, fin_yr);
				 	        stmt1.setString(6, print_pdf_type);
				 	        stmt1.setString(7, file_nm);
				        	stmt1.setString(8, emp_cd);
				        	pdf_entry=stmt1.executeUpdate();
				        	
				        	stmt1.close();
				        }
				        
				        if(pdf_entry == 0 || update_flag == 0)
				        {
				        	conn.rollback();
				        	deletePdfFile(path);
				        	msg = "Failed! - PDF "+file_nm+" for Scrap fixed Asset with Invoice No. "+inv_no+" Generation Failed!";
				        	msg_type="E";
				        }
				        else
				        {
				        	conn.commit();
				        	msg = "Successful! - PDF "+file_nm+" for Scrap fixed Asset with Invoice No. "+inv_no+" Generated Successfully!";
				        	msg_type="S";
				        }
					}
					else
					{
						msg="Failed! - PDF for Scrap fixed Asset with Invoice No. "+inv_no+" Generation Failed!";
						msg_type="E";
					}
				}
			}
		}
		catch(Exception e)
		{
			conn.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			
			document.close();
			deletePdfFile(path);
			
			msg = "Error in Exception! - PDF for Scrap fixed Asset with Invoice No. "+inv_no+" Generation Failed!";
			msg_type="E";	
		}
		finally
		{
			document.close();
		}
		
		if(is_print.equals("1"))
		{
			try
			{
				new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, "", "", msg);  	
			}
			catch(Exception infoLogger)
			{
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
			}
		}
	}
	
	
	public void getInvoiceDtlsGA()
	{	
		String function_nm="getInvoiceDtlsGA()";

		try
		{
			double taxValue = 0;
			String tax_value="";
			queryString = "SELECT A.COMPANY_CD,A.VENDOR_CD,A.SUPPLIER_CD,TO_CHAR(A.INVOICE_DT,'DD/MM/YYYY'),A.BU_UNIT,"
					+ "B.SALE_NO,A.GROSS_AMT,A.TAX_AMT_INR,A.NET_PAYABLE,A.EXCHG_RATE_VALUE,TO_CHAR(A.EXCHG_RATE_DT, 'DD/MM/YYYY'),"
					+ "A.SALE_PRICE_UNIT,B.SAC_CODE,B.TAX_STRUCT_CD,B.ITEM_DESCRIPTION,A.REMARK,A.REMARK1,A.APPROVED_FLAG,A.FINANCIAL_YEAR,"
					+ "A.INVOICE_SEQ,A.SALE_AMT,A.CHECKED_FLAG,A.INVOICE_NO,A.INVOICE_ID_SEQ,TO_CHAR(B.TAX_EFF_DT, 'DD/MM/YYYY'),A.INVOICE_CATEGORY,"
					+ "A.INVOICE_RAISED_IN "
					+ "FROM FMS_OTH_INVOICE_MST A, FMS_OTH_INVOICE_DTL B ";
				if(is_print.equals("0"))
				{
					queryString	+= "WHERE A.COMPANY_CD = ? AND A.INVOICE_SEQ = ? AND A.INVOICE_TYPE = ? AND A.FINANCIAL_YEAR=? AND A.SUPPLIER_CD=? AND A.INV_FLAG = ? "
							+ "AND A.BU_UNIT = ? AND A.COMPANY_CD = B.COMPANY_CD AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.INVOICE_TYPE = B.INVOICE_TYPE "
							+ "AND A.VENDOR_CD = B.VENDOR_CD AND A.SUPPLIER_CD = B.SUPPLIER_CD AND A.INVOICE_SEQ = B.INVOICE_SEQ AND A.INV_FLAG = B.INV_FLAG AND A.BU_UNIT = B.BU_UNIT ";
				}
				else 
				{
					queryString	+= "WHERE A.COMPANY_CD = ? AND A.INVOICE_NO = ? AND A.INVOICE_TYPE = ? "
							+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.INVOICE_SEQ = B.INVOICE_SEQ AND A.FINANCIAL_YEAR = B.FINANCIAL_YEAR AND A.INVOICE_TYPE = B.INVOICE_TYPE AND "
							+ "A.INVOICE_DT = B.EFF_DT AND A.VENDOR_CD = B.VENDOR_CD AND A.SUPPLIER_CD = B.SUPPLIER_CD AND A.INV_FLAG = B.INV_FLAG AND A.BU_UNIT = B.BU_UNIT AND "
							+ "A.VENDOR_TYPE =? AND A.INV_FLAG = ? ";
				}
			stmt = conn.prepareStatement(queryString);
			
			if(is_print.equals("0"))
			{
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_seq);
				stmt.setString(3, inv_type);
				stmt.setString(4, fin_yr);
				stmt.setString(5, supp_cd);
				stmt.setString(6, "F");
				stmt.setString(7, bu_unit);
			}
			else
			{
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_no);
				stmt.setString(3, inv_type);
				stmt.setString(4, "V");
				stmt.setString(5, "F");
			}
			rset = stmt.executeQuery();
			if(rset.next()) 
			{
				vendor_cd= rset.getString(2) == null ? "" : rset.getString(2);
				supp_cd= rset.getString(3) == null ? "" : rset.getString(3);				
				inv_dt = rset.getString(4) == null ? "" : rset.getString(4);
				bu_unit = rset.getString(5) == null ? "" : rset.getString(5);
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
					VGITEM_AMT.add(nf.format(Double.parseDouble("" + rset2.getString(3)==null?"":rset2.getString(3))));
					taxAmt = taxValue;
					if(tax_struct_info.equals("C"))
					{
						tax_struct_info = utilBean.getTaxDescr(conn,rset2.getString(4)==null?"":rset2.getString(4));
						VTAX_STRUCTURE.add(tax_struct_info.split(", ")[0].split(" ")[1]);
						VTAX_STRUCTURE1.add(tax_struct_info.split(", ")[1].split(" ")[1]);
						taxAmt = taxValue/2;
					}
					VTAX_STRUCTURE_DESC.add(utilBean.getTaxDescr(conn,rset2.getString(4)==null?"":rset2.getString(4)).split(", ")[0].split(" ")[1]);
					VITEM_TOTAL.add(nf.format(rset2.getDouble(7)));
					VTAX_AMT.add(nf.format(taxAmt));
					
					queryString1 = "SELECT SAC_CODE "
								+ "FROM FMS_SAC_MST A "
								+ "WHERE SAC_CD  = ?";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, rset2.getString(2)==null?"":rset2.getString(2));
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						VSAC_CODE.add(rset1.getString(1)==null?"":rset1.getString(1));	
					}
					rset1.close();
					stmt1.close();
				}
				rset2.close();
				stmt2.close();
				
				int srno=0;
				String currency_nm ="INR";
				
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
	
	public void pdfGA() throws SQLException
	{
		String function_nm="pdfGA()";
		String emp_nm ="";
		String ip = "";
		msg="";
		//String msg_content="";
		String path ="";
		
		Rectangle pageSize = new Rectangle(595, 842);
		
		pageSize.setBackgroundColor(BaseColor.WHITE);
		
		Document document = new Document(pageSize);
		try
		{
			HttpSession session = request.getSession();
			emp_nm = (String)session.getAttribute("emp_nm")==null?"":(String)session.getAttribute("emp_nm");
			ip = (String)session.getAttribute("ip")==null?"":(String)session.getAttribute("ip");
			
			String company_nm = ""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");
			String company_abbr = ""+session.getAttribute("comp_abbr")==null?"":""+session.getAttribute("comp_abbr");
			
			int pdfGenCount=0;
			if(is_print.equals("1"))
			{
				queryString = "SELECT COUNT(*) "
							+ "FROM FMS_OTH_INVOICE_MST A ,FMS_OTH_INV_FILE_DTL B "
							+ "WHERE A.COMPANY_CD=? AND A.INVOICE_NO = ? AND A.INVOICE_TYPE = ? AND A.BU_UNIT = ? ";
							if(print_pdf_type.equals("O"))
							{
								queryString+= "AND A.PRINT_BY_ORI IS NOT NULL AND A.PRINT_DT_ORI IS NOT NULL ";
							}
							else if(print_pdf_type.equals("T"))
							{
								queryString+= "AND A.PRINT_BY_TRI IS NOT NULL AND A.PRINT_DT_TRI IS NOT NULL ";
							}
							else if(print_pdf_type.equals("D"))
							{
								queryString+= "AND A.PRINT_BY_DUP IS NOT NULL AND A.PRINT_DT_DUP IS NOT NULL ";
							}
				queryString += "AND B.PDF_TYPE=? AND B.INVOICE_TYPE = ? AND B.FILE_NAME IS NOT NULL "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.INVOICE_TYPE = B.INVOICE_TYPE AND A.SUPPLIER_CD = B.SUPPLIER_CD";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_no);
				stmt.setString(3, inv_type);
				stmt.setString(4, bu_unit);
				stmt.setString(5, print_pdf_type);
				stmt.setString(6, inv_type);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					pdfGenCount=rset.getInt(1);
				}
				rset.close();
				stmt.close();
			}
			else 
			{
				pdfGenCount = 0;
			}
			
			if(pdfGenCount>0)
			{
				msg="Failed! - PDF for GNA Invoice with Invoice No. "+inv_no+" already Generated!";
	        	msg_type="E";
	        }
			else
			{
				
				getInvoiceDtlsGA();
				getCompOwnerDtl();
				getVendorDtl();
				
				String irn_no="";
				String qr_code="";
				queryString1="SELECT IRN_NO,SIGN_QR_CODE "
						+ "FROM FMS_OTH_INV_IRN_DTL "
						+ "WHERE COMPANY_CD=? AND INVOICE_NO=? ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, inv_no);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					irn_no=rset1.getString(1)==null?"":rset1.getString(1);
					qr_code=rset1.getString(2)==null?"":rset1.getString(2);
				}
				rset1.close();
				stmt1.close();
				
				String print_pdf_type_nm="ORIGINAL";
				if(print_pdf_type.equals("O"))
				{
					print_pdf_type_nm="ORIGINAL";
				}
				else if(print_pdf_type.equals("D"))
				{
					print_pdf_type_nm="DUPLICATE";
				}
				else if(print_pdf_type.equals("T"))
				{
					print_pdf_type_nm="TRIPLICATE";
				}
	
				String appPath = request.getServletContext().getRealPath("");
	
				String main_folder="";
				if(!comp_cd.equals(""))
				{
					main_folder=CommonVariable.work_dir+comp_cd;
				}
				
				String sub_folder="unsigned_pdf";
				String sub_folder2="ig_invoice";
				
				String temp_path=appPath+File.separator+main_folder+File.separator+sub_folder+File.separator+sub_folder2;
				File main_folderDir = new File(temp_path);
		        if(!main_folderDir.exists())
		        {
		        	main_folderDir.mkdirs();
		        }

		        if(is_print.equals("1"))
		        {
		        	file_nm=print_pdf_type+"-"+supp_abbr+"-"+inv_type+"-"+inv_no.replace("/", "-")+".pdf";
		        }
		        else
		        {
		        	file_nm="temp.pdf";
		        }
				path = appPath+"/"+main_folder+"/"+sub_folder+"/"+sub_folder2+"/"+file_nm;
				PdfWriter writer = PdfWriter.getInstance(document,new FileOutputStream(path));
	
				document.open();
				document.setPageSize(pageSize);
	            document.newPage();
	
	            String context_nm = request.getContextPath();
				String server_nm = request.getServerName();
				String server_port = ""+request.getServerPort();
	
				file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm;
	
				Font small_black_normal = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
				Font small_black_normal_10 = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
				Font small_black_normal_14 = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.NORMAL, BaseColor.BLACK);
				Font small_black_normal_12 = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
				Font small_black_bold = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
	            Font black_bold = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
	            Font black_bold1 = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
	            
	            PdfPCell company_logo_cell = new PdfPCell();
	            if(!comp_logo.equals(""))
	            {
					String file_path = request.getRealPath("/"+CommonVariable.company_logo_path+"/"+comp_logo);
	            	Image company_logo = Image.getInstance(file_path);
		            company_logo.setBorder(Rectangle.NO_BORDER);
		            company_logo.scaleAbsolute(44,40);
		            company_logo_cell = new PdfPCell(company_logo,false);
		            company_logo_cell.setBorder(Rectangle.NO_BORDER);
		            company_logo_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            }
	            
	            float[] mainWidths = {10f, 70f, 15f};  
	            PdfPTable mainTable = new PdfPTable(mainWidths);
	            mainTable.setWidthPercentage(100);
	            mainTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

	            PdfPCell logoCell = new PdfPCell(company_logo_cell);
	            logoCell.setBorder(Rectangle.NO_BORDER);
	            logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            logoCell.setPaddingLeft(10);
	            mainTable.addCell(logoCell);
	            
	            PdfPTable centerTable = new PdfPTable(1);
	            centerTable.setWidthPercentage(100);
	            centerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            centerTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            centerTable.addCell(new Phrase(new Chunk(print_pdf_type_nm, small_black_normal_10)));
	            centerTable.addCell(new Phrase(new Chunk(supp_nm, black_bold1)));
	            centerTable.addCell(new Phrase(new Chunk("TAX INVOICE", small_black_normal_12)));
	            String info = "Tax invoice issued under Rule 46 of the Central Goods and Services Tax Rules, 2017";
	            centerTable.addCell(new Phrase(new Chunk(info, small_black_normal)));

	            PdfPCell centerCell = new PdfPCell(centerTable);
	            centerCell.setBorder(Rectangle.NO_BORDER);
	            centerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            mainTable.addCell(centerCell);

	            PdfPCell emptyCell = new PdfPCell(new Phrase(""));
	            emptyCell.setBorder(Rectangle.NO_BORDER);
	            mainTable.addCell(emptyCell);

	            float[] ContactAddrWidths5 = {0.80f,0.10F,0.20F,0.80F};
	   	     	PdfPTable Table3 = new PdfPTable(ContactAddrWidths5);
	            Table3.setWidthPercentage(100);
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("Registered Office: ",small_black_bold)));
	            
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("To:",small_black_bold)));
	
				float[] ContactAddrWidths6 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table4 = new PdfPTable(ContactAddrWidths6);
	            Table4.setWidthPercentage(100);
	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk(supp_nm,small_black_bold)));
	            
	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk(""+vendor_name+",",small_black_bold)));
	
	            float[] ContactAddrWidths7 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table5 = new PdfPTable(ContactAddrWidths7);
	            Table5.setWidthPercentage(100);
	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk(supp_addr+" "+supp_city,small_black_normal)));
	            
	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk(vend_abbr+" "+vend_city,small_black_normal)));
	            
	
	            float[] ContactAddrWidths8 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table6 = new PdfPTable(ContactAddrWidths8);
	            Table6.setWidthPercentage(100);
	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table6.addCell(new Phrase(new Chunk(supp_state+"-"+supp_pin,small_black_normal)));
	
	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table6.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_pin_no.equals("")) 
	            {
	            	Table6.addCell(new Phrase(new Chunk(vend_state + "-" + vend_pin_no, small_black_normal)));
				}
	            else 
	            {
	            	Table6.addCell(new Phrase(new Chunk(vend_state, small_black_normal)));
	            }
	            
	           	float[] ContactAddrWidths9 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table7 = new PdfPTable(ContactAddrWidths9);
	            Table7.setWidthPercentage(100);
	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk("State: "+supp_state,small_black_normal)));
	
	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk("State: "+vend_state,small_black_normal)));
	            
	            float[] ContactAddrWidths10 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table8 = new PdfPTable(ContactAddrWidths10);
	            Table8.setWidthPercentage(100);
	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table8.addCell(new Phrase(new Chunk("State Code: "+supp_state_tin,small_black_normal)));
	
	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table8.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_state_tin.equals("")) 
	            {
					Table8.addCell(new Phrase(new Chunk("State Code: " + vend_state_tin, small_black_normal)));
				}
	            else 
	            {
					Table8.addCell(new Phrase(new Chunk("", small_black_normal)));
	            }
	            
	            float[] ContactAddrWidths11 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table9 = new PdfPTable(ContactAddrWidths11);
	            Table9.setWidthPercentage(100);
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table9.addCell(new Phrase(new Chunk("GSTIN: "+supp_gstin_no,small_black_normal)));
	
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table9.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_gstin_no.equals("")) 
	            {
					Table9.addCell(new Phrase(new Chunk("GSTIN: " + vend_gstin_no, small_black_normal)));
				}
	            else 
	            {
	            	Table9.addCell(new Phrase(new Chunk("", small_black_normal)));
	            }

	            float[] ContactAddrWidths12 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table10 = new PdfPTable(ContactAddrWidths12);
	            Table10.setWidthPercentage(100);
	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table10.addCell(new Phrase(new Chunk("PAN: "+supp_pan_no,small_black_normal)));
	
	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table10.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_pan_no.equals("")) 
	            {
					Table10.addCell(new Phrase(new Chunk("PAN: " + vend_pan_no, small_black_normal)));
				}
	            else 
	            {
	            	Table10.addCell(new Phrase(new Chunk("", small_black_normal)));
	            }
	            
	            float[] ContactAddrWidths15 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table13 = new PdfPTable(ContactAddrWidths15);
	            Table13.setWidthPercentage(100);
	            Table13.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table13.addCell(new Phrase(new Chunk("Place of Supply: "+supp_state,small_black_normal)));
	
	            Table13.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table13.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table13.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table13.addCell(new Phrase(new Chunk("",small_black_normal)));
            
	            float[] ContactAddrWidths16 = {0.30F,0.30F,0.40F,0.30F,0.30F};
	   	     	PdfPTable Table14 = new PdfPTable(ContactAddrWidths16);
	            Table14.setWidthPercentage(100);
	            Table14.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table14.addCell(new Phrase(new Chunk("",black_bold)));
	
	            Table14.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table14.addCell(new Phrase(new Chunk("",black_bold)));
	            
	            Table14.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table14.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table14.addCell(new Phrase(new Chunk("Invoice No :",black_bold)));
	
	            Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table14.addCell(new Phrase(new Chunk(inv_no,black_bold)));
	            
	            float[] ContactAddrWidths17 = {0.30F,0.30F,0.40F,0.30F,0.30F};
	   	     	PdfPTable Table15 = new PdfPTable(ContactAddrWidths17);
	            Table15.setWidthPercentage(100);
	            Table15.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table15.addCell(new Phrase(new Chunk("",black_bold)));
	            
	            Table15.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk("",black_bold)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table15.addCell(new Phrase(new Chunk("Invoice Date :",black_bold)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk(inv_dt,black_bold)));

	            
	            if(supp_state.equals(vend_state))
	            {         
		            float[] ContactAddrWidths20 = {0.07F,0.40F,0.15F,0.18F,0.08F,0.15F,0.08F,0.15F,0.20F};
		   	     	PdfPTable Table18 = new PdfPTable(ContactAddrWidths20);
		   	     	Table18.setWidthPercentage(100);
			   	  	PdfPCell cell;
	
			   	  	cell = new PdfPCell(new Phrase("Sr. No.", small_black_bold));
			   	  	cell.setRowspan(2);
			   	  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			   	  	cell.setVerticalAlignment(Element.ALIGN_CENTER);
			   	  	Table18.addCell(cell);
	
			   	  	cell = new PdfPCell(new Phrase("Description of Goods/Services", small_black_bold));
			   	  	cell.setRowspan(2);
			   	  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			   	  	cell.setVerticalAlignment(Element.ALIGN_CENTER);
			   	  	Table18.addCell(cell);
	
			   	  	cell = new PdfPCell(new Phrase("HSN/SAC Code", small_black_bold));
			   	  	cell.setRowspan(2);
			   	  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			   	  	cell.setVerticalAlignment(Element.ALIGN_CENTER);
			   	  	Table18.addCell(cell);
	
			   	  	cell = new PdfPCell(new Phrase("Amount", small_black_bold));
			   	  	cell.setRowspan(2);
			   	  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			   	  	cell.setVerticalAlignment(Element.ALIGN_CENTER);
			   	  	Table18.addCell(cell);
	
			   	  	cell = new PdfPCell(new Phrase("CGST", small_black_bold));
			   	  	cell.setColspan(2);
			   	  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			   	  	cell.setVerticalAlignment(Element.ALIGN_CENTER);
			   	  	Table18.addCell(cell);
	
			   	  	cell = new PdfPCell(new Phrase("SGST", small_black_bold));
			   	  	cell.setColspan(2);
			   	  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			   	  	cell.setVerticalAlignment(Element.ALIGN_CENTER);
			   	  	Table18.addCell(cell);
	
			   	  	cell = new PdfPCell(new Phrase("Total Amount", small_black_bold));
			   	  	cell.setRowspan(2);
			   	  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			   	  	cell.setVerticalAlignment(Element.ALIGN_CENTER);
			   	  	Table18.addCell(cell);
			   	  	
			   	  	cell = new PdfPCell(new Phrase("Rate", small_black_bold));
			   	  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			   	  	Table18.addCell(cell);
	
			   	  	cell = new PdfPCell(new Phrase("Amount", small_black_bold));
			   	  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			   	  	Table18.addCell(cell);
	
			   	  	cell = new PdfPCell(new Phrase("Rate", small_black_bold));
			   	  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			   	  	Table18.addCell(cell);
	
			   	  	cell = new PdfPCell(new Phrase("Amount", small_black_bold));
			   	  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			   	  	Table18.addCell(cell);
		   	     	
		            String curr1 = "INR";
		            
		            int sr=0;
		            float[] ContactAddrWidths21 = {0.07F,0.40F,0.15F,0.18F,0.08F,0.15F,0.08F,0.15F,0.20F};
		   	     	PdfPTable Table19 = new PdfPTable(ContactAddrWidths21);
		   	     	
		            for(int i = 0;i<VGITEM_AMT.size();i++) 
		            {
		            	sr += 1;
						Table19.setWidthPercentage(100);
						Table19.getDefaultCell().setBorder(Rectangle.BOX);
						Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						Table19.addCell(new Phrase(new Chunk("" + sr, small_black_normal)));
						
						Table19.getDefaultCell().setBorder(Rectangle.BOX);
						Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
						Table19.addCell(new Phrase(new Chunk(""+VGAMT_DES.elementAt(i), small_black_normal)));
						
						Table19.getDefaultCell().setBorder(Rectangle.BOX);
						Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						Table19.addCell(new Phrase(new Chunk(""+VSAC_CODE.elementAt(i), small_black_normal)));
						
						Table19.getDefaultCell().setBorder(Rectangle.BOX);
						Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
						Table19.addCell(new Phrase(new Chunk(es.formatAmount(""+VGITEM_AMT.elementAt(i)), small_black_normal)));
						
						Table19.getDefaultCell().setBorder(Rectangle.BOX);
						Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						Table19.addCell(new Phrase(new Chunk(""+VTAX_STRUCTURE.elementAt(i), small_black_normal)));
						
						Table19.getDefaultCell().setBorder(Rectangle.BOX);
						Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
						Table19.addCell(new Phrase(new Chunk(es.formatAmount(""+VTAX_AMT.elementAt(i)), small_black_normal)));
						
						Table19.getDefaultCell().setBorder(Rectangle.BOX);
						Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						Table19.addCell(new Phrase(new Chunk(""+VTAX_STRUCTURE1.elementAt(i), small_black_normal)));
						
						Table19.getDefaultCell().setBorder(Rectangle.BOX);
						Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
						Table19.addCell(new Phrase(new Chunk(es.formatAmount(""+VTAX_AMT.elementAt(i)), small_black_normal)));
						
						Table19.getDefaultCell().setBorder(Rectangle.BOX);
						Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
						Table19.addCell(new Phrase(new Chunk(es.formatAmount(""+VITEM_TOTAL.elementAt(i)), small_black_normal)));
					}
					
		            sr+=1;
		            float[] ContactAddrWidths23 = {0.07F,0.40F,0.15F,0.18F,0.08F,0.15F,0.08F,0.15F,0.20F};
		   	     	PdfPTable Table21 = new PdfPTable(ContactAddrWidths23);
		            Table21.setWidthPercentage(100);
		            
		            Table21.getDefaultCell().setBorder(Rectangle.BOX);
		            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table21.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
		            
		            Table21.getDefaultCell().setBorder(Rectangle.BOX);
		            Table21.getDefaultCell().setColspan(7);
		            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            Table21.addCell(new Phrase(new Chunk("Gross Amount ",small_black_bold)));

		            Table21.getDefaultCell().setBorder(Rectangle.BOX);
		            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table21.addCell(new Phrase(new Chunk(es.formatAmount(gross_amt),small_black_bold)));
		            
		            sr+=1;
		            float[] ContactAddrWidths24 = {0.07F,0.40F,0.15F,0.18F,0.08F,0.15F,0.08F,0.15F,0.20F};
		   	     	PdfPTable Table22 = new PdfPTable(ContactAddrWidths24);
		            
		            Table22.setWidthPercentage(100);
		            Table22.getDefaultCell().setBorder(Rectangle.BOX);
		            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table22.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
		
		            Table22.getDefaultCell().setBorder(Rectangle.BOX);
		            Table22.getDefaultCell().setColspan(7);
		            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            Table22.addCell(new Phrase(new Chunk("Total Tax Amount",small_black_bold)));
		            
		            Table22.getDefaultCell().setBorder(Rectangle.BOX);
		            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table22.addCell(new Phrase(new Chunk(es.formatAmount(tax_amt),small_black_bold)));
		            
		            sr+=1;
		            float[] ContactAddrWidths25 = {0.07F,0.40F,0.15F,0.18F,0.08F,0.15F,0.08F,0.15F,0.20F};
		   	     	PdfPTable Table23 = new PdfPTable(ContactAddrWidths25);
		            Table23.setWidthPercentage(100);
		            Table23.getDefaultCell().setBorder(Rectangle.BOX);
		            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table23.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
		            
		            Table23.getDefaultCell().setBorder(Rectangle.BOX);  
		            Table23.getDefaultCell().setColspan(7);
		            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            Table23.addCell(new Phrase(new Chunk("Total Amount in INR",small_black_bold)));
		            
		            Table23.getDefaultCell().setBorder(Rectangle.BOX);
		            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table23.addCell(new Phrase(new Chunk(es.formatAmount(net_amt),small_black_bold)));
		            
		            float[] ContactAddrWidths26_1 = {0.100F};
		   	     	PdfPTable Table24_1 = new PdfPTable(ContactAddrWidths26_1);
		   	     	Table24_1.setWidthPercentage(100);
		   	     	Table24_1.getDefaultCell().setBorder(Rectangle.BOX);
		   	     	Table24_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		   	     	Table24_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		   	     	Table24_1.addCell(new Phrase(new Chunk("Amount in Words | Rupees "+amt_in_word+" Only",black_bold)));
		   	  		
		   	     	float[] ContactAddrWidths26 = {0.100F};
		   	     	PdfPTable Table24 = new PdfPTable(ContactAddrWidths26);
		   	     	Table24.setWidthPercentage(100);
		   	     	Table24.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		   	     	Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		   	  		Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		   	  		Table24.addCell(new Phrase(new Chunk("For "+supp_nm,black_bold)));
		
		   	  		float[] ContactAddrWidths27 = {0.100F};
		   	  		PdfPTable Table25 = new PdfPTable(ContactAddrWidths27);
			     	Table25.setWidthPercentage(100);
			     	Table25.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			     	Table25.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			     	Table25.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
			     	Table25.addCell(new Phrase(new Chunk("Authorised Signatory",black_bold)));
			     	
			     	PdfPTable BillingFieldsInfoTable81 = new PdfPTable(1);
				     BillingFieldsInfoTable81.setWidthPercentage(20);
				     BillingFieldsInfoTable81.setHorizontalAlignment(Element.ALIGN_LEFT);
				     BillingFieldsInfoTable81.getDefaultCell().setBorder(Rectangle.BOX);
				     BillingFieldsInfoTable81.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				     BillingFieldsInfoTable81.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		
				     PdfFormField field = PdfFormField.createSignature(writer);
				     field.setFieldName(SIGNAME);
		
				     field.setPage();
				     field.setWidget(new Rectangle(72, 732, 144, 780), PdfAnnotation.HIGHLIGHT_NONE);
				     field.setFlags(PdfAnnotation.FLAGS_PRINT);
				     writer.addAnnotation(field);
		
				     PdfPCell sigCell = new PdfPCell();
				     FieldPositioningEvents events = new FieldPositioningEvents(writer, field);
				     sigCell.setCellEvent(events);
				     sigCell.setBorder(Rectangle.NO_BORDER);
				     sigCell.setFixedHeight(50f);
				     BillingFieldsInfoTable81.addCell(sigCell);
				    
				     PdfPTable leftSignTable = new PdfPTable(1);
				     leftSignTable.setWidthPercentage(100);
				     leftSignTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				     leftSignTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		
				     leftSignTable.addCell(Table24);
				     leftSignTable.addCell(BillingFieldsInfoTable81);
				     leftSignTable.addCell(Table25);
		
				     float[] InvoiceDateInfoWidths_qr = {0.30f, 0.20f, 0.40f};
				     PdfPTable InvoiceDateInfoTable_qr = new PdfPTable(InvoiceDateInfoWidths_qr);
		
				     if (!irn_no.equals("") && !qr_code.equals("")) 
				     {
				         int width = 90;
				         int height = 90;
				         float fixedBoxHeight = 110f;
				         Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
				         hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		
				         QRCodeWriter qrCodeWriter = new QRCodeWriter();
				         BitMatrix matrix = qrCodeWriter.encode(qr_code, BarcodeFormat.QR_CODE, width, height, hintMap);
		
				         BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
				         for (int x = 0; x < width; x++) {
				             for (int y = 0; y < height; y++) {
				                 image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
				             }
				         }
		
				         ByteArrayOutputStream baos = new ByteArrayOutputStream();
				         ImageIO.write(image, "png", baos);
				         Image qr_codeimg = Image.getInstance(baos.toByteArray());
				         qr_codeimg.setBorder(Rectangle.NO_BORDER);
				         qr_codeimg.setAlignment(Element.ALIGN_LEFT);
		
				         PdfPCell qrcode_cell = new PdfPCell(qr_codeimg, false);
				         qrcode_cell.setBorder(Rectangle.NO_BORDER);
				         qrcode_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		
				         String char_16 = irn_no.substring(0, 16);
				         String char_32 = irn_no.substring(16, 32);
				         String char_48 = irn_no.substring(32, 48);
				         String char_64 = irn_no.substring(48);
				         String final_irn_no = char_16 + "\n" + char_32 + "\n" + char_48 + "\n" + char_64;
		
				         PdfPTable InvoiceDateInfoTable_qr1 = new PdfPTable(1);
				         InvoiceDateInfoTable_qr1.setWidthPercentage(100);
				         InvoiceDateInfoTable_qr1.getDefaultCell().setBorder(Rectangle.BOX);
				         InvoiceDateInfoTable_qr1.addCell(qrcode_cell);
		
				         PdfPTable InvoiceDateInfoTable_qr11 = new PdfPTable(1);
				         InvoiceDateInfoTable_qr11.setWidthPercentage(100);
				         InvoiceDateInfoTable_qr11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				         InvoiceDateInfoTable_qr11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				         InvoiceDateInfoTable_qr11.addCell(new Phrase(new Chunk("IRN", black_bold)));
				         InvoiceDateInfoTable_qr11.addCell(new Phrase(new Chunk(final_irn_no, small_black_normal)));
				         InvoiceDateInfoTable_qr11.getDefaultCell().setFixedHeight(50);
				         
				         InvoiceDateInfoTable_qr.setWidthPercentage(100);
				         InvoiceDateInfoTable_qr.getDefaultCell().setBorder(Rectangle.BOX);
				         InvoiceDateInfoTable_qr.addCell(InvoiceDateInfoTable_qr11);
				         InvoiceDateInfoTable_qr.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				         InvoiceDateInfoTable_qr.addCell("");
				         InvoiceDateInfoTable_qr.addCell(InvoiceDateInfoTable_qr1);
				     }

		
				     PdfPTable finalRow = new PdfPTable(3);
				     finalRow.setWidthPercentage(100);
				     finalRow.setWidths(new float[]{0.3f, 0.2f ,0.5f});
				     finalRow.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		
				     finalRow.addCell(leftSignTable);
				     finalRow.addCell("");
				     finalRow.addCell(InvoiceDateInfoTable_qr);
				     
				     float[] ContactAddrWidths30 = {0.100F};
		   	  		PdfPTable Table28 = new PdfPTable(ContactAddrWidths30);
			     	Table28.setWidthPercentage(100);
			     	Table28.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			     	Table28.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			     	Table28.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
			     	Table28.addCell(new Phrase(new Chunk("Notes:",black_bold)));
			     	
			     	float[] ContactAddrWidths31 = {0.200F};
		   	  		PdfPTable Table29 = new PdfPTable(ContactAddrWidths31);
			     	Table29.setWidthPercentage(100);
			     	Table29.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			     	Table29.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			     	Table29.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
			     	Table29.addCell(new Phrase(new Chunk(remark1,small_black_normal)));
	
	
					document.add(mainTable);
					document.add(new Paragraph("  "));
					document.add(Table3);
					document.add(Table4);
					document.add(Table5);
					document.add(Table6);
					document.add(Table7);
					document.add(Table8);
					document.add(Table9);
					document.add(Table10);
					document.add(Table13);
					document.add(new Paragraph("  "));
					document.add(Table14);
					document.add(Table15);
					document.add(new Paragraph("  "));
					document.add(Table18);
					document.add(Table19);
					document.add(Table21);
					document.add(Table22);
					document.add(Table23);
					document.add(new Paragraph("  "));
					document.add(Table24_1);
					document.add(new Paragraph("  "));
					document.add(finalRow);
					if(!remark1.equals(""))
					{
						document.add(Table28);
						document.add(Table29);
					}
				
				}
	            else 
	            {
	            	  	float[] ContactAddrWidths20 = {0.07F,0.40F,0.15F,0.18F,0.08F,0.15F,0.20F};
			   	     	PdfPTable Table18 = new PdfPTable(ContactAddrWidths20);
			   	     	Table18.setWidthPercentage(100);
				   	  	PdfPCell cell;
		
				   	  	cell = new PdfPCell(new Phrase("Sr. No.", small_black_bold));
				   	  	cell.setRowspan(2);
				   	  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				   	  	cell.setVerticalAlignment(Element.ALIGN_CENTER);
				   	  	Table18.addCell(cell);
		
				   	  	cell = new PdfPCell(new Phrase("Description of Goods/Services", small_black_bold));
				   	  	cell.setRowspan(2);
				   	  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				   	  	cell.setVerticalAlignment(Element.ALIGN_CENTER);
				   	  	Table18.addCell(cell);
		
				   	  	cell = new PdfPCell(new Phrase("HSN/SAC Code", small_black_bold));
				   	  	cell.setRowspan(2);
				   	  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				   	  	cell.setVerticalAlignment(Element.ALIGN_CENTER);
				   	  	Table18.addCell(cell);
				   	  	
				   	  	cell = new PdfPCell(new Phrase("Amount", small_black_bold));
				   	  	cell.setRowspan(2);
				   	  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				   	  	cell.setVerticalAlignment(Element.ALIGN_CENTER);
				   	  	Table18.addCell(cell);
		
				   	  	cell = new PdfPCell(new Phrase("IGST", small_black_bold));
				   	  	cell.setColspan(2);
				   	  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				   	  	cell.setVerticalAlignment(Element.ALIGN_CENTER);
				   	  	Table18.addCell(cell);
		
				   	  	cell = new PdfPCell(new Phrase("Total Amount", small_black_bold));
				   	  	cell.setRowspan(2);
				   	  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				   	  	cell.setVerticalAlignment(Element.ALIGN_CENTER);
				   	  	Table18.addCell(cell);
				   	  	
				   	  	cell = new PdfPCell(new Phrase("Rate", small_black_bold));
				   	  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				   	  	Table18.addCell(cell);
		
				   	  	cell = new PdfPCell(new Phrase("Amount", small_black_bold));
				   	  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				   	  	Table18.addCell(cell);
		
			            String curr1 = "INR";
			            
			            int sr=0;
			            float[] ContactAddrWidths21 = {0.07F,0.40F,0.15F,0.18F,0.08F,0.15F,0.20F};
			   	     	PdfPTable Table19 = new PdfPTable(ContactAddrWidths21);
			   	     	
			            for(int i = 0;i<VGITEM_AMT.size();i++) 
			            {
			            	sr += 1;
							Table19.setWidthPercentage(100);
							Table19.getDefaultCell().setBorder(Rectangle.BOX);
							Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
							Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
							Table19.addCell(new Phrase(new Chunk("" + sr, small_black_normal)));
							
							Table19.getDefaultCell().setBorder(Rectangle.BOX);
							Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
							Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
							Table19.addCell(new Phrase(new Chunk(""+VGAMT_DES.elementAt(i), small_black_normal)));
							
							Table19.getDefaultCell().setBorder(Rectangle.BOX);
							Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
							Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
							Table19.addCell(new Phrase(new Chunk(""+VSAC_CODE.elementAt(i), small_black_normal)));
							
							Table19.getDefaultCell().setBorder(Rectangle.BOX);
							Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
							Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
							Table19.addCell(new Phrase(new Chunk(es.formatAmount(""+VGITEM_AMT.elementAt(i)), small_black_normal)));
							
							Table19.getDefaultCell().setBorder(Rectangle.BOX);
							Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
							Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
							Table19.addCell(new Phrase(new Chunk(""+VTAX_STRUCTURE_DESC.elementAt(i), small_black_normal)));
							
							Table19.getDefaultCell().setBorder(Rectangle.BOX);
							Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
							Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
							Table19.addCell(new Phrase(new Chunk(es.formatAmount(""+VTAX_AMT.elementAt(i)), small_black_normal)));
							
							Table19.getDefaultCell().setBorder(Rectangle.BOX);
							Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
							Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
							Table19.addCell(new Phrase(new Chunk(es.formatAmount(""+VITEM_TOTAL.elementAt(i)), small_black_normal)));
						}
						
			            sr+=1;
			            float[] ContactAddrWidths23 = {0.07F,0.40F,0.15F,0.18F,0.08F,0.15F,0.20F};
			   	     	PdfPTable Table21 = new PdfPTable(ContactAddrWidths23);
			            Table21.setWidthPercentage(100);
			            
			            Table21.getDefaultCell().setBorder(Rectangle.BOX);
			            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table21.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
			            
			            Table21.getDefaultCell().setBorder(Rectangle.BOX);
			            Table21.getDefaultCell().setColspan(5);
			            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
			            Table21.addCell(new Phrase(new Chunk("Gross Amount ",small_black_bold)));

			            Table21.getDefaultCell().setBorder(Rectangle.BOX);
			            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            Table21.addCell(new Phrase(new Chunk(es.formatAmount(gross_amt),small_black_bold)));
			            
			            sr+=1;
			            float[] ContactAddrWidths24 = {0.07F,0.40F,0.15F,0.18F,0.08F,0.15F,0.20F};
			   	     	PdfPTable Table22 = new PdfPTable(ContactAddrWidths24);
			            
			            Table22.setWidthPercentage(100);
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table22.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
			
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setColspan(5);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
			            Table22.addCell(new Phrase(new Chunk("Total Tax Amount",small_black_bold)));
			            
			            Table22.getDefaultCell().setBorder(Rectangle.BOX);
			            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            Table22.addCell(new Phrase(new Chunk(es.formatAmount(tax_amt),small_black_bold)));
			            
			            sr+=1;
			            float[] ContactAddrWidths25 = {0.07F,0.40F,0.15F,0.18F,0.08F,0.15F,0.20F};
			   	     	PdfPTable Table23 = new PdfPTable(ContactAddrWidths25);
			            Table23.setWidthPercentage(100);
			            Table23.getDefaultCell().setBorder(Rectangle.BOX);
			            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table23.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
			            
			            Table23.getDefaultCell().setBorder(Rectangle.BOX);  
			            Table23.getDefaultCell().setColspan(5);
			            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
			            Table23.addCell(new Phrase(new Chunk("Total Amount in INR",small_black_bold)));
			            
			            Table23.getDefaultCell().setBorder(Rectangle.BOX);
			            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            Table23.addCell(new Phrase(new Chunk(es.formatAmount(net_amt),small_black_bold)));
			            
			            float[] ContactAddrWidths26_1 = {0.100F};
			   	     	PdfPTable Table24_1 = new PdfPTable(ContactAddrWidths26_1);
			   	     	Table24_1.setWidthPercentage(100);
			   	     	Table24_1.getDefaultCell().setBorder(Rectangle.BOX);
			   	     	Table24_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			   	     	Table24_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
			   	     	Table24_1.addCell(new Phrase(new Chunk("Amount in Words | Rupees "+amt_in_word+" Only",black_bold)));
			   	  		
			   	     	float[] ContactAddrWidths26 = {0.100F};
			   	     	PdfPTable Table24 = new PdfPTable(ContactAddrWidths26);
			   	     	Table24.setWidthPercentage(100);
			   	     	Table24.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			   	     	Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			   	  		Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
			   	  		Table24.addCell(new Phrase(new Chunk("For "+supp_nm,black_bold)));
			
			   	  		float[] ContactAddrWidths27 = {0.100F};
			   	  		PdfPTable Table25 = new PdfPTable(ContactAddrWidths27);
				     	Table25.setWidthPercentage(100);
				     	Table25.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				     	Table25.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				     	Table25.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				     	Table25.addCell(new Phrase(new Chunk("Authorised Signatory",black_bold)));
				     	
				     	PdfPTable BillingFieldsInfoTable81 = new PdfPTable(1);
					     BillingFieldsInfoTable81.setWidthPercentage(20);
					     BillingFieldsInfoTable81.setHorizontalAlignment(Element.ALIGN_LEFT);
					     BillingFieldsInfoTable81.getDefaultCell().setBorder(Rectangle.BOX);
					     BillingFieldsInfoTable81.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					     BillingFieldsInfoTable81.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
			
					     PdfFormField field = PdfFormField.createSignature(writer);
					     field.setFieldName(SIGNAME);
			
					     field.setPage();
					     field.setWidget(new Rectangle(72, 732, 144, 780), PdfAnnotation.HIGHLIGHT_NONE);
					     field.setFlags(PdfAnnotation.FLAGS_PRINT);
					     writer.addAnnotation(field);
			
					     PdfPCell sigCell = new PdfPCell();
					     FieldPositioningEvents events = new FieldPositioningEvents(writer, field);
					     sigCell.setCellEvent(events);
					     sigCell.setBorder(Rectangle.NO_BORDER);
					     sigCell.setFixedHeight(50f);
					     BillingFieldsInfoTable81.addCell(sigCell);
					    
					     PdfPTable leftSignTable = new PdfPTable(1);
					     leftSignTable.setWidthPercentage(100);
					     leftSignTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					     leftSignTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			
					     leftSignTable.addCell(Table24);
					     leftSignTable.addCell(BillingFieldsInfoTable81);
					     leftSignTable.addCell(Table25);
			
					     float[] InvoiceDateInfoWidths_qr = {0.30f, 0.20f, 0.40f};
					     PdfPTable InvoiceDateInfoTable_qr = new PdfPTable(InvoiceDateInfoWidths_qr);
			
					     if (!irn_no.equals("") && !qr_code.equals("")) 
					     {
					         int width = 90;
					         int height = 90;
					         float fixedBoxHeight = 110f;
					         Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
					         hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
			
					         QRCodeWriter qrCodeWriter = new QRCodeWriter();
					         BitMatrix matrix = qrCodeWriter.encode(qr_code, BarcodeFormat.QR_CODE, width, height, hintMap);
			
					         BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
					         for (int x = 0; x < width; x++) {
					             for (int y = 0; y < height; y++) {
					                 image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
					             }
					         }
			
					         ByteArrayOutputStream baos = new ByteArrayOutputStream();
					         ImageIO.write(image, "png", baos);
					         Image qr_codeimg = Image.getInstance(baos.toByteArray());
					         qr_codeimg.setBorder(Rectangle.NO_BORDER);
					         qr_codeimg.setAlignment(Element.ALIGN_LEFT);
			
					         PdfPCell qrcode_cell = new PdfPCell(qr_codeimg, false);
					         qrcode_cell.setBorder(Rectangle.NO_BORDER);
					         qrcode_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			
					         String char_16 = irn_no.substring(0, 16);
					         String char_32 = irn_no.substring(16, 32);
					         String char_48 = irn_no.substring(32, 48);
					         String char_64 = irn_no.substring(48);
					         String final_irn_no = char_16 + "\n" + char_32 + "\n" + char_48 + "\n" + char_64;
			
					         PdfPTable InvoiceDateInfoTable_qr1 = new PdfPTable(1);
					         InvoiceDateInfoTable_qr1.setWidthPercentage(100);
					         InvoiceDateInfoTable_qr1.getDefaultCell().setBorder(Rectangle.BOX);
					         InvoiceDateInfoTable_qr1.addCell(qrcode_cell);
			
					         PdfPTable InvoiceDateInfoTable_qr11 = new PdfPTable(1);
					         InvoiceDateInfoTable_qr11.setWidthPercentage(100);
					         InvoiceDateInfoTable_qr11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					         InvoiceDateInfoTable_qr11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					         InvoiceDateInfoTable_qr11.addCell(new Phrase(new Chunk("IRN", black_bold)));
					         InvoiceDateInfoTable_qr11.addCell(new Phrase(new Chunk(final_irn_no, small_black_normal)));
			
					         InvoiceDateInfoTable_qr.setWidthPercentage(100);
					         InvoiceDateInfoTable_qr.getDefaultCell().setBorder(Rectangle.BOX);
					         InvoiceDateInfoTable_qr.addCell(InvoiceDateInfoTable_qr11);
			
					         InvoiceDateInfoTable_qr.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					         InvoiceDateInfoTable_qr.addCell("");
			
					         InvoiceDateInfoTable_qr.addCell(InvoiceDateInfoTable_qr1);
					     }

						     PdfPTable finalRow = new PdfPTable(3);
						     finalRow.setWidthPercentage(100);
						     finalRow.setWidths(new float[]{0.3f, 0.2f ,0.5f});
						     finalRow.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				
						     finalRow.addCell(leftSignTable);
						     finalRow.addCell("");
						     finalRow.addCell(InvoiceDateInfoTable_qr);
						     
						    float[] ContactAddrWidths30 = {0.100F};
				   	  		PdfPTable Table28 = new PdfPTable(ContactAddrWidths30);
					     	Table28.setWidthPercentage(100);
					     	Table28.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					     	Table28.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					     	Table28.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
					     	Table28.addCell(new Phrase(new Chunk("Notes:",black_bold)));
					     	
					     	float[] ContactAddrWidths31 = {0.200F};
				   	  		PdfPTable Table29 = new PdfPTable(ContactAddrWidths31);
					     	Table29.setWidthPercentage(100);
 					     	Table29.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					     	Table29.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					     	Table29.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
					     	Table29.addCell(new Phrase(new Chunk(remark1,small_black_normal)));
		
						document.add(mainTable);
						document.add(new Paragraph("  "));
						document.add(Table3);
						document.add(Table4);
						document.add(Table5);
						document.add(Table6);
						document.add(Table7);
						document.add(Table8);
						document.add(Table9);
						document.add(Table10);
						document.add(Table13);
						document.add(new Paragraph("  "));
						document.add(Table14);
						document.add(Table15);
						document.add(new Paragraph("  "));
						document.add(Table18);
						document.add(Table19);
						document.add(Table21);
						document.add(Table22);
						document.add(Table23);
						document.add(new Paragraph("  "));
						document.add(Table24_1);
						document.add(new Paragraph("  "));
						document.add(finalRow);
						if(!remark1.equals(""))
						{
							document.add(Table28);
							document.add(Table29);
						}
	            }
	            
	            document.close();

	            
	            if(is_print.equals("1"))
				{
		            File isPDFexist = new File(path);
					if(isPDFexist.exists())
					{
						int update_flag=0;
						int pdf_entry=0;
						
						int cont=0;
						queryString="UPDATE FMS_OTH_INVOICE_MST SET PDF_INV_DTL=? ";
							if(print_pdf_type.equals("O"))
							{
								queryString+= ",PRINT_BY_ORI=?,PRINT_DT_ORI=SYSDATE ";
							}
							else if(print_pdf_type.equals("D"))
							{
								queryString+= ",PRINT_BY_DUP=?,PRINT_DT_DUP=SYSDATE ";
							}
							else if(print_pdf_type.equals("T"))
							{
								queryString+= ",PRINT_BY_TRI=?,PRINT_DT_TRI=SYSDATE ";
							}
							queryString+= "WHERE COMPANY_CD=? AND INVOICE_NO=? AND INVOICE_TYPE=? AND INVOICE_SEQ = ? AND SUPPLIER_CD = ?";
							
						stmt=conn.prepareStatement(queryString);
						stmt.setString(++cont, print_pdf_type);
						if(print_pdf_type.equals("O"))
						{
							stmt.setString(++cont, emp_cd);
						}
						else if(print_pdf_type.equals("D"))
						{
							stmt.setString(++cont, emp_cd);
						}
						else if(print_pdf_type.equals("T"))
						{
							stmt.setString(++cont, emp_cd);
						}
						stmt.setString(++cont, comp_cd);
						stmt.setString(++cont, inv_no);
						stmt.setString(++cont, inv_type);
						stmt.setString(++cont, inv_seq);
						stmt.setString(++cont, supp_cd);
						update_flag=stmt.executeUpdate();
						stmt.close();
						
						int count=0;
				        queryString1="SELECT COUNT(*) "
				        		+ "FROM FMS_OTH_INV_FILE_DTL "
				        		+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? "
				        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND SUPPLIER_CD =? ";
				        stmt1=conn.prepareStatement(queryString1);
				        stmt1.setString(1, comp_cd);
				        stmt1.setString(2, inv_type);
				        stmt1.setString(3, inv_seq);
				        stmt1.setString(4, fin_yr);
				        stmt1.setString(5, print_pdf_type);
				        stmt1.setString(6, supp_cd);
				        rset1=stmt1.executeQuery();
				        if(rset1.next())
				        {
				        	count=rset1.getInt(1);
				        }
				        rset1.close();
				        stmt1.close();
				        
				        if(count > 0)
				        {
				        	queryString1="UPDATE FMS_OTH_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
				        			+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? "
					        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND SUPPLIER_CD =?";
				        	stmt1=conn.prepareStatement(queryString1);
				        	stmt1.setString(1, file_nm);
				        	stmt1.setString(2, emp_cd);
				        	stmt1.setString(3, comp_cd);
				 	        stmt1.setString(4, inv_type);
				 	        stmt1.setString(5, inv_seq);
				 	        stmt1.setString(6, fin_yr);
				 	        stmt1.setString(7, print_pdf_type); 
				 	        stmt1.setString(8, supp_cd); 
				 	        pdf_entry=stmt1.executeUpdate();
				        	
				        	stmt1.close();
				        }
				        else
				        {
				        	queryString1="INSERT INTO FMS_OTH_INV_FILE_DTL(COMPANY_CD,SUPPLIER_CD,INVOICE_SEQ,INVOICE_TYPE,FINANCIAL_YEAR,PDF_TYPE,"
				        			+ "FILE_NAME,ENT_BY,ENT_DT) "
				        			+ "VALUES(?,?,?,?,?,?,"
				        			+ "?,?,SYSDATE)";
				        	stmt1=conn.prepareStatement(queryString1);
				        	stmt1.setString(1, comp_cd);
				        	stmt1.setString(2, supp_cd);
				 	        stmt1.setString(3, inv_seq);
				 	        stmt1.setString(4, inv_type);
				 	        stmt1.setString(5, fin_yr);
				 	        stmt1.setString(6, print_pdf_type);
				 	        stmt1.setString(7, file_nm);
				        	stmt1.setString(8, emp_cd);
				        	pdf_entry=stmt1.executeUpdate();
				        	
				        	stmt1.close();
				        }
				        
				        if(pdf_entry == 0 || update_flag == 0)
				        {
				        	conn.rollback();
				        	deletePdfFile(path);
				        	msg = "Failed! - PDF "+file_nm+" for GNA Invoice with Invoice No. "+inv_no+" Generation Failed!";
				        	msg_type="E";
				        }
				        else
				        {
				        	conn.commit();
				        	msg = "Successful! - PDF "+file_nm+" for GNA Invoice with Invoice No. "+inv_no+" Generated Successfully!";
				        	msg_type="S";
				        }
					}
					else
					{
						msg="Failed! - PDF for GNA Invoice with Invoice No. "+inv_no+" Generation Failed!";
						msg_type="E";
					}
				}
			}
		}
		catch(Exception e)
		{
			conn.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			
			document.close();
			deletePdfFile(path);
			
			msg = "Error in Exception! - PDF for GNA Invoice with Invoice No. "+inv_no+" Generation Failed!";
			msg_type="E";	
		}
		finally
		{
			document.close();
		}
		
		if(is_print.equals("1"))
		{
			try
			{
				new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, "", "", msg);  	
			}
			catch(Exception infoLogger)
			{
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
			}
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
					+ "A.GROSS_AMT,A.NET_PAYABLE,A.REMARK,A.REMARK1,A.FINANCIAL_YEAR,A.INVOICE_SEQ,A.SALE_AMT, "
					+ "A.INVOICE_NO,A.INVOICE_ID_SEQ,A.INVOICE_CATEGORY,A.INVOICE_RAISED_IN "
					+ "FROM FMS_OTH_INVOICE_MST A ";
					if(is_print.equals("0"))
					{
						queryString	+= "WHERE A.COMPANY_CD = ? AND A.INVOICE_SEQ = ? AND A.INVOICE_TYPE = ? "
								+ "AND A.FINANCIAL_YEAR=? AND A.SUPPLIER_CD=? AND A.INV_FLAG = ? ";
					}
					else 
					{
						queryString	+= "WHERE A.COMPANY_CD = ? AND A.INVOICE_NO = ? AND A.INVOICE_TYPE = ? "
								+ "AND A.VENDOR_TYPE =? AND A.INV_FLAG = ? ";
					}
			stmt = conn.prepareStatement(queryString);
			if(is_print.equals("0"))
			{
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_seq);
				stmt.setString(3, inv_type);
				stmt.setString(4, fin_yr);
				stmt.setString(5, supp_cd);
				stmt.setString(6, "F");
			}
			else
			{
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_no);
				stmt.setString(3, inv_type);
				stmt.setString(4, "V");
				stmt.setString(5, "F");
			}
			rset = stmt.executeQuery();
			if(rset.next()) 
			{
				vendor_cd= rset.getString(2) == null ? "" : rset.getString(2);
				supp_cd= rset.getString(3) == null ? "" : rset.getString(3);				
				inv_dt = rset.getString(4) == null ? "" : rset.getString(4);
				gross_amt = nf.format(Double.parseDouble(rset.getString(5) == null ? "" : rset.getString(5)));
				net_amt = nf.format(Double.parseDouble(rset.getString(6) == null ? "" : rset.getString(6)));
				remark1 = rset.getString(7) == null ? "" : rset.getString(7);
				remark2 = rset.getString(8) == null ? "" : rset.getString(8);
				fin_yr = rset.getString(9) == null ? "" : rset.getString(9);
				inv_seq = rset.getString(10) == null ? "" : rset.getString(10);
				sale_amt = nf.format(Double.parseDouble(rset.getString(11) == null ? "" : rset.getString(11)));
				inv_no = rset.getString(12) == null ? "" : rset.getString(12);
				inv_id_seq = rset.getString(13) == null ? "" : rset.getString(13);
				invoice_category = rset.getString(14) == null ? "" : rset.getString(14);
				invoice_raised_in = rset.getString(15) == null ? "" : rset.getString(15);
//				

				queryString1 = "SELECT REFERENCE_NO,PER_CARRIAGE,FINAL_DESTINATION, "
						+ "COUNTRY_OF_ORIGIN,PORT_OF_DISCHARGE,PORT_OF_LOADING,PAY_TERMS "
						+ "FROM FMS_OTH_INVOICE_DTL  "
						+ "WHERE COMPANY_CD  = ? AND INVOICE_SEQ = ? AND INVOICE_TYPE = ? AND FINANCIAL_YEAR = ? AND SUPPLIER_CD = ? "
						+ "AND INV_FLAG = ? AND EFF_DT = TO_DATE(?, 'DD/MM/YYYY')";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, inv_seq);
				stmt1.setString(3, inv_type);
				stmt1.setString(4, fin_yr);
				stmt1.setString(5, supp_cd);
				stmt1.setString(6, "F");
				stmt1.setString(7, inv_dt);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					reference_no = rset1.getString(1) == null ? "" : rset1.getString(1);
					per_carriage = rset1.getString(2) == null ? "" : rset1.getString(2);
					final_destination = rset1.getString(3) == null ? "" : rset1.getString(3);	
					country_of_origin = rset1.getString(4) == null ? "" : rset1.getString(4);	
					port_of_discharge = rset1.getString(5) == null ? "" : rset1.getString(5);	
					port_of_loading = rset1.getString(6) == null ? "" : rset1.getString(6);	
					pay_terms = rset1.getString(7) == null ? "" : rset1.getString(7);
				}
				rset1.close();
				stmt1.close();
				
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
				}
				rset2.close();
				stmt2.close();
				
				int srno=0;
				String currency_nm ="";
				if(invoice_raised_in.equals("1"))
				{
					currency_nm = "INR";
				}
				else if(invoice_raised_in.equals("2"))
				{
					currency_nm = "USD";
				}
				else if(invoice_raised_in.equals("3"))
				{
					currency_nm = "GBP";
				}
				else if(invoice_raised_in.equals("4"))
				{
					currency_nm = "EURO";
				}
				else if(invoice_raised_in.equals("5"))
				{
					currency_nm = "YEN";
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
	
	public void pdfRXP() throws SQLException
	{
		String function_nm="pdfRXP()";
		String emp_nm ="";
		String ip = "";
		msg="";
		//String msg_content="";
		String path ="";
		
		Rectangle pageSize = new Rectangle(595, 842);
		
		pageSize.setBackgroundColor(BaseColor.WHITE);
		
		Document document = new Document(pageSize);
		try
		{
			HttpSession session = request.getSession();
			emp_nm = (String)session.getAttribute("emp_nm")==null?"":(String)session.getAttribute("emp_nm");
			ip = (String)session.getAttribute("ip")==null?"":(String)session.getAttribute("ip");
			
			String company_nm = ""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");
			String company_abbr = ""+session.getAttribute("comp_abbr")==null?"":""+session.getAttribute("comp_abbr");
			
			int pdfGenCount=0;
			if(is_print.equals("1"))
			{
				queryString = "SELECT COUNT(*) "
							+ "FROM FMS_OTH_INVOICE_MST A ,FMS_OTH_INV_FILE_DTL B "
							+ "WHERE A.COMPANY_CD=? AND A.INVOICE_NO = ? AND A.INVOICE_TYPE = ? AND A.BU_UNIT = ? ";
							if(print_pdf_type.equals("O"))
							{
								queryString+= "AND A.PRINT_BY_ORI IS NOT NULL AND A.PRINT_DT_ORI IS NOT NULL ";
							}
							else if(print_pdf_type.equals("T"))
							{
								queryString+= "AND A.PRINT_BY_TRI IS NOT NULL AND A.PRINT_DT_TRI IS NOT NULL ";
							}
							else if(print_pdf_type.equals("D"))
							{
								queryString+= "AND A.PRINT_BY_DUP IS NOT NULL AND A.PRINT_DT_DUP IS NOT NULL ";
							}
				queryString += "AND B.PDF_TYPE=? AND B.INVOICE_TYPE = ? AND B.FILE_NAME IS NOT NULL "
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.INVOICE_TYPE = B.INVOICE_TYPE AND A.SUPPLIER_CD = B.SUPPLIER_CD";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_no);
				stmt.setString(3, inv_type);
				stmt.setString(4, bu_unit);
				stmt.setString(5, print_pdf_type);
				stmt.setString(6, inv_type);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					pdfGenCount=rset.getInt(1);
				}
				rset.close();
				stmt.close();
			}
			else 
			{
				pdfGenCount = 0;
			}
			
			if(pdfGenCount>0)
			{
				msg="Failed! - PDF for ReExport Invoice with Invoice No. "+inv_no+" already Generated!";
	        	msg_type="E";
	        }
			else
			{
				
				getInvoiceDtlsRXP();
				getSupplierDtl();
				getVendorDtl();
				getVendorShipBillAddr();
				
				String print_pdf_type_nm="ORIGINAL";
				if(print_pdf_type.equals("O"))
				{
					print_pdf_type_nm="ORIGINAL";
				}
				else if(print_pdf_type.equals("D"))
				{
					print_pdf_type_nm="DUPLICATE";
				}
				else if(print_pdf_type.equals("T"))
				{
					print_pdf_type_nm="TRIPLICATE";
				}
	
				String appPath = request.getServletContext().getRealPath("");
	
				String main_folder="";
				if(!comp_cd.equals(""))
				{
					main_folder=CommonVariable.work_dir+comp_cd;
				}
				
				String sub_folder="unsigned_pdf";
				String sub_folder2="ig_invoice";
				
				String temp_path=appPath+File.separator+main_folder+File.separator+sub_folder+File.separator+sub_folder2;
				File main_folderDir = new File(temp_path);
		        if(!main_folderDir.exists())
		        {
		        	main_folderDir.mkdirs();
		        }

		        if(is_print.equals("1"))
		        {
		        	file_nm=print_pdf_type+"-"+supp_abbr+"-"+inv_type+"-"+inv_no.replace("/", "-")+".pdf";
		        }
		        else
		        {
		        	file_nm="temp.pdf";
		        }
				path = appPath+"/"+main_folder+"/"+sub_folder+"/"+sub_folder2+"/"+file_nm;
				PdfWriter writer = PdfWriter.getInstance(document,new FileOutputStream(path));
	
				document.open();
				document.setPageSize(pageSize);
	            document.newPage();
	
	            String context_nm = request.getContextPath();
				String server_nm = request.getServerName();
				String server_port = ""+request.getServerPort();
	
				file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm;
	
				Font small_black_normal = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
				Font small_black_normal_10 = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
				Font small_black_normal_14 = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.NORMAL, BaseColor.BLACK);
				Font small_black_normal_12 = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
				Font small_black_bold = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
	            Font black_bold = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
	            Font black_bold1 = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
	            
	            PdfPCell company_logo_cell = new PdfPCell();
	            if(!comp_logo.equals(""))
	            {
					String file_path = request.getRealPath("/"+CommonVariable.company_logo_path+"/"+comp_logo);
	            	Image company_logo = Image.getInstance(file_path);
		            company_logo.setBorder(Rectangle.NO_BORDER);
		            company_logo.scaleAbsolute(44,40);
		            company_logo_cell = new PdfPCell(company_logo,false);
		            company_logo_cell.setBorder(Rectangle.NO_BORDER);
		            company_logo_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            }
	            
	            float[] mainWidths = {10f, 70f, 15f};  
	            PdfPTable mainTable = new PdfPTable(mainWidths);
	            mainTable.setWidthPercentage(100);
	            mainTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

	            PdfPCell logoCell = new PdfPCell(company_logo_cell);
	            logoCell.setBorder(Rectangle.NO_BORDER);
	            logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            logoCell.setPaddingLeft(10);
	            mainTable.addCell(logoCell);
	            
	            PdfPTable centerTable = new PdfPTable(1);
	            centerTable.setWidthPercentage(100);
	            centerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            centerTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            centerTable.addCell(new Phrase(new Chunk(print_pdf_type_nm, small_black_normal_10)));
	            centerTable.addCell(new Phrase(new Chunk(supp_nm, black_bold1)));
	            centerTable.addCell(new Phrase(new Chunk("INVOICE cum PACKING LIST", small_black_normal_12)));
//	            String info = "Tax invoice issued under Rule 46 of the Central Goods and Services Tax Rules, 2017";
//	            centerTable.addCell(new Phrase(new Chunk(info, small_black_normal)));

	            PdfPCell centerCell = new PdfPCell(centerTable);
	            centerCell.setBorder(Rectangle.NO_BORDER);
	            centerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            mainTable.addCell(centerCell);

	            PdfPCell emptyCell = new PdfPCell(new Phrase(""));
	            emptyCell.setBorder(Rectangle.NO_BORDER);
	            mainTable.addCell(emptyCell);

	            float[] ContactAddrWidths5 = {0.80f,0.10F,0.20F,0.80F};
	   	     	PdfPTable Table3 = new PdfPTable(ContactAddrWidths5);
	            Table3.setWidthPercentage(100);
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("Registered Office: ",small_black_bold)));
	            
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("To:",small_black_bold)));
	
				float[] ContactAddrWidths6 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table4 = new PdfPTable(ContactAddrWidths6);
	            Table4.setWidthPercentage(100);
	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk(supp_nm,small_black_bold)));
	            
	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk(""+vendor_name+",",small_black_bold)));
	
	            float[] ContactAddrWidths7 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table5 = new PdfPTable(ContactAddrWidths7);
	            Table5.setWidthPercentage(100);
	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk(supp_addr+" "+supp_city,small_black_normal)));
	            
	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk(vend_abbr+" "+vend_city,small_black_normal)));
	            
	
	            float[] ContactAddrWidths8 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table6 = new PdfPTable(ContactAddrWidths8);
	            Table6.setWidthPercentage(100);
	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table6.addCell(new Phrase(new Chunk(supp_state+"-"+supp_pin,small_black_normal)));
	
	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table6.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_pin_no.equals("")) 
	            {
	            	Table6.addCell(new Phrase(new Chunk(vend_state + "-" + vend_pin_no, small_black_normal)));
				}
	            else 
	            {
	            	Table6.addCell(new Phrase(new Chunk(vend_state, small_black_normal)));
	            }
	            
	           	float[] ContactAddrWidths9 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table7 = new PdfPTable(ContactAddrWidths9);
	            Table7.setWidthPercentage(100);
	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk("State: "+supp_state,small_black_normal)));
	
	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk("State: "+vend_state,small_black_normal)));
	            
	            float[] ContactAddrWidths10 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table8 = new PdfPTable(ContactAddrWidths10);
	            Table8.setWidthPercentage(100);
	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table8.addCell(new Phrase(new Chunk("State Code: "+supp_state_tin,small_black_normal)));
	
	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table8.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_state_tin.equals("")) 
	            {
					Table8.addCell(new Phrase(new Chunk("State Code: " + vend_state_tin, small_black_normal)));
				}
	            else 
	            {
					Table8.addCell(new Phrase(new Chunk("", small_black_normal)));
	            }
	            
	            float[] ContactAddrWidths11 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table9 = new PdfPTable(ContactAddrWidths11);
	            Table9.setWidthPercentage(100);
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table9.addCell(new Phrase(new Chunk("GSTIN: "+supp_gstin_no,small_black_normal)));
	
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table9.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_gstin_no.equals("")) 
	            {
					Table9.addCell(new Phrase(new Chunk("GSTIN: " + vend_gstin_no, small_black_normal)));
				}
	            else 
	            {
	            	Table9.addCell(new Phrase(new Chunk("", small_black_normal)));
	            }

	            float[] ContactAddrWidths12 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table10 = new PdfPTable(ContactAddrWidths12);
	            Table10.setWidthPercentage(100);
	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table10.addCell(new Phrase(new Chunk("PAN: "+supp_pan_no,small_black_normal)));
	
	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table10.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if (!vend_pan_no.equals("")) 
	            {
					Table10.addCell(new Phrase(new Chunk("PAN: " + vend_pan_no, small_black_normal)));
				}
	            else 
	            {
	            	Table10.addCell(new Phrase(new Chunk("", small_black_normal)));
	            }
	            
	            float[] ContactAddrWidths15 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table13 = new PdfPTable(ContactAddrWidths15);
	            Table13.setWidthPercentage(100);
	            Table13.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table13.addCell(new Phrase(new Chunk("Place of Supply: "+supp_state,small_black_normal)));
	
	            Table13.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table13.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table13.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table13.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            //vendor shipping and billing
	            float[] ContactAddrWidths40 = {0.28f,0.55F,0.20F,0.75F};
	   	     	PdfPTable Table11 = new PdfPTable(ContactAddrWidths40);
	            Table11.setWidthPercentage(100);
	            Table11.getDefaultCell().setBorder(Rectangle.BOX);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("Per-Carriage By:",black_bold)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.BOX);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk(per_carriage,small_black_normal)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("",black_bold)));
	            
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("Shipping Address:",black_bold)));
	            
	            
	            Table11.setWidthPercentage(100);
	            Table11.getDefaultCell().setBorder(Rectangle.BOX);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("Vessel/Flight No:",black_bold)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.BOX);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk(reference_no,small_black_normal)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("",black_bold)));
	            
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk(vend_ship_abbr,small_black_normal)));
	            
	            
	            Table11.setWidthPercentage(100);
	            Table11.getDefaultCell().setBorder(Rectangle.BOX);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("Port Of Discharge:",black_bold)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.BOX);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk(port_of_discharge,small_black_normal)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("",black_bold)));
	            
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk(vend_ship_city,small_black_normal)));
	            
	            
	            Table11.setWidthPercentage(100);
	            Table11.getDefaultCell().setBorder(Rectangle.BOX);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("Port Of Loading:",black_bold)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.BOX);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk(port_of_loading,small_black_normal)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("",black_bold)));
	            
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk(vend_ship_state+"-"+vend_ship_pin_no,small_black_normal)));
	            
	            
	            Table11.setWidthPercentage(100);
	            Table11.getDefaultCell().setBorder(Rectangle.BOX);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("Final Destination:",black_bold)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.BOX);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk(final_destination,small_black_normal)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("",black_bold)));
	            
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table11.setWidthPercentage(100);
	            Table11.getDefaultCell().setBorder(Rectangle.BOX);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("Country Of Origin:",black_bold)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.BOX);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk(country_of_origin,small_black_normal)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("Billing Address:",black_bold)));
	            
	            Table11.setWidthPercentage(100);
	            Table11.getDefaultCell().setBorder(Rectangle.BOX);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("Terms Of Delivery And Payment:",black_bold)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.BOX);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk(pay_terms,small_black_normal)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk(vend_bill_abbr,small_black_normal)));
	            
	            Table11.setWidthPercentage(100);
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk(vend_bill_city,small_black_normal)));
	            
	            Table11.setWidthPercentage(100);
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table11.addCell(new Phrase(new Chunk(vend_bill_state+"-"+vend_bill_pin_no,small_black_normal)));
	            
	            float[] ContactAddrWidths16 = {0.30F,0.30F,0.40F,0.30F,0.30F};
	   	     	PdfPTable Table14 = new PdfPTable(ContactAddrWidths16);
	            Table14.setWidthPercentage(100);
	            Table14.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table14.addCell(new Phrase(new Chunk("",black_bold)));
	
	            Table14.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table14.addCell(new Phrase(new Chunk("",black_bold)));
	            
	            Table14.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table14.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table14.addCell(new Phrase(new Chunk("Invoice No :",black_bold)));
	
	            Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table14.addCell(new Phrase(new Chunk(inv_no,black_bold)));
	            
	            float[] ContactAddrWidths17 = {0.30F,0.30F,0.40F,0.30F,0.30F};
	   	     	PdfPTable Table15 = new PdfPTable(ContactAddrWidths17);
	            Table15.setWidthPercentage(100);
	            Table15.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table15.addCell(new Phrase(new Chunk("",black_bold)));
	            
	            Table15.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk("",black_bold)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table15.addCell(new Phrase(new Chunk("Invoice Date :",black_bold)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk(inv_dt,black_bold)));

	            
	            float[] ContactAddrWidths20 = {0.08F,0.20F,0.60F,0.10F,0.45F,0.12F,0.18F,0.17F,0.20F};
	   	     	PdfPTable Table18 = new PdfPTable(ContactAddrWidths20);
	   	     	Table18.setWidthPercentage(100);
		   	  	PdfPCell cell;

		   	  	cell = new PdfPCell(new Phrase("Sr. No.", small_black_bold));
		   	  	cell.setRowspan(2);
		   	  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		   	  	cell.setVerticalAlignment(Element.ALIGN_CENTER);
		   	  	Table18.addCell(cell);

		   	    cell = new PdfPCell(new Phrase("HSN/SAC Code", small_black_bold));
		   	  	cell.setRowspan(2);
		   	  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		   	  	cell.setVerticalAlignment(Element.ALIGN_CENTER);
		   	  	Table18.addCell(cell);
		   	  	
		   	  	cell = new PdfPCell(new Phrase("Description of Goods", small_black_bold));
		   	  	cell.setRowspan(2);
		   	  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		   	  	cell.setVerticalAlignment(Element.ALIGN_CENTER);
		   	  	Table18.addCell(cell);
		   	  	
		   	    cell = new PdfPCell(new Phrase("UOM", small_black_bold));
		   	  	cell.setRowspan(2);
		   	  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		   	  	cell.setVerticalAlignment(Element.ALIGN_CENTER);
		   	  	Table18.addCell(cell);
			   	  	
		   	    cell = new PdfPCell(new Phrase("Packing Details", small_black_bold));
		   	  	cell.setRowspan(2);
		   	  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		   	  	cell.setVerticalAlignment(Element.ALIGN_CENTER);
		   	  	Table18.addCell(cell);
		   	  	
		   	    cell = new PdfPCell(new Phrase("Qty", small_black_bold));
		   	  	cell.setRowspan(2);
		   	  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		   	  	cell.setVerticalAlignment(Element.ALIGN_CENTER);
		   	  	Table18.addCell(cell);
		   	  	
		   	  	cell = new PdfPCell(new Phrase("Rate Per Qty", small_black_bold));
		   	    cell.setRowspan(2);
		   	  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		   	  	cell.setVerticalAlignment(Element.ALIGN_CENTER);
		   	    Table18.addCell(cell);
		   	    
		   	    cell = new PdfPCell(new Phrase("Currency", small_black_bold));
		   	    cell.setRowspan(2);
		   	  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		   	  	cell.setVerticalAlignment(Element.ALIGN_CENTER);
		   	    Table18.addCell(cell);
		   	    
		   	  	cell = new PdfPCell(new Phrase("Amount", small_black_bold));
		   	    cell.setRowspan(2);
		   	  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		   	  	cell.setVerticalAlignment(Element.ALIGN_CENTER);
		   	    Table18.addCell(cell);
	
		   	    String currency_nm ="";
				if(invoice_raised_in.equals("1"))
				{
					currency_nm = "INR";
				}
				else if(invoice_raised_in.equals("2"))
				{
					currency_nm = "USD";
				}
				else if(invoice_raised_in.equals("3"))
				{
					currency_nm = "GBP";
				}
				else if(invoice_raised_in.equals("4"))
				{
					currency_nm = "EURO";
				}
				else if(invoice_raised_in.equals("5"))
				{
					currency_nm = "YEN";
				}
	            
	            int sr=0;
	            float[] ContactAddrWidths21 = {0.08F,0.20F,0.60F,0.10F,0.45F,0.12F,0.18F,0.17F,0.20F};
	   	     	PdfPTable Table19 = new PdfPTable(ContactAddrWidths21);
		   	     	
	            for(int i = 0;i<VGITEM_AMT.size();i++) 
	            {
	            	String packDetails = "";
					packDetails += VPACK_DTLS.elementAt(i) + "\n";
					packDetails += "Dimensions: " + VDIMENSION.elementAt(i) + "\n";
					packDetails += "Net Wt.: " + VNET_WT.elementAt(i) + "\n";
					packDetails += "Gross Wt.: " + VGROSS_WT.elementAt(i);
					VLAMT_DES.add(packDetails);
					
	            	sr += 1;
					Table19.setWidthPercentage(100);
					Table19.getDefaultCell().setBorder(Rectangle.BOX);
					Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
					Table19.addCell(new Phrase(new Chunk("" + sr, small_black_normal)));
					
					Table19.getDefaultCell().setBorder(Rectangle.BOX);
					Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
					Table19.addCell(new Phrase(new Chunk(""+VSAC_CODE.elementAt(i), small_black_normal)));
					
					Table19.getDefaultCell().setBorder(Rectangle.BOX);
					Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
					Table19.addCell(new Phrase(new Chunk(""+VGAMT_DES.elementAt(i), small_black_normal)));
						
					Table19.getDefaultCell().setBorder(Rectangle.BOX);
					Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
					Table19.addCell(new Phrase(new Chunk(""+VUOM_NO.elementAt(i), small_black_normal)));
					
					Table19.getDefaultCell().setBorder(Rectangle.BOX);
					Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
					Phrase phrase = new Phrase();
					phrase.add(new Chunk(VPACK_DTLS.elementAt(i) + "\n", small_black_normal));
					phrase.add(new Chunk("Dimensions: ", small_black_bold));
					phrase.add(new Chunk(VDIMENSION.elementAt(i) + "\n", small_black_normal));
					phrase.add(new Chunk("Net Wt.: ", small_black_bold));
					phrase.add(new Chunk(VNET_WT.elementAt(i) + "\n", small_black_normal));
					phrase.add(new Chunk("Gross Wt.: ", small_black_bold));
					phrase.add(new Chunk(""+VGROSS_WT.elementAt(i), small_black_normal));
					Table19.addCell(phrase);
					
					Table19.getDefaultCell().setBorder(Rectangle.BOX);
					Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
					Table19.addCell(new Phrase(new Chunk(""+VQTY.elementAt(i), small_black_normal)));
					
					Table19.getDefaultCell().setBorder(Rectangle.BOX);
					Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
					Table19.addCell(new Phrase(new Chunk(es.formatAmount(""+VPRICE.elementAt(i)), small_black_normal)));
					
					Table19.getDefaultCell().setBorder(Rectangle.BOX);
					Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
					Table19.addCell(new Phrase(new Chunk(currency_nm, small_black_normal)));
						
					Table19.getDefaultCell().setBorder(Rectangle.BOX);
					Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
					Table19.addCell(new Phrase(new Chunk(es.formatAmount(""+VGITEM_AMT.elementAt(i)), small_black_normal)));
						
					}
					
		            sr+=1;
		            float[] ContactAddrWidths23 = {0.08F,0.20F,0.60F,0.10F,0.45F,0.12F,0.18F,0.17F,0.20F};
		   	     	PdfPTable Table21 = new PdfPTable(ContactAddrWidths23);
		            Table21.setWidthPercentage(100);
		            
		            Table21.getDefaultCell().setBorder(Rectangle.BOX);
		            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table21.getDefaultCell().setColspan(1);
		            Table21.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
		            
		            Table21.getDefaultCell().setBorder(Rectangle.BOX);
		            Table21.getDefaultCell().setColspan(6);
		            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            Table21.addCell(new Phrase(new Chunk("Gross Amount ",small_black_bold)));

		            Table21.getDefaultCell().setColspan(1);
		            
		            Table21.getDefaultCell().setBorder(Rectangle.BOX);
		            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table21.addCell(new Phrase(new Chunk(currency_nm, small_black_bold)));
		            
		            Table21.getDefaultCell().setBorder(Rectangle.BOX);
		            Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table21.addCell(new Phrase(new Chunk(es.formatAmount(gross_amt),small_black_bold)));
		            
		            
		            sr+=1;
		            float[] ContactAddrWidths25 = {0.08F,0.20F,0.60F,0.10F,0.45F,0.12F,0.18F,0.17F,0.20F};
		   	     	PdfPTable Table23 = new PdfPTable(ContactAddrWidths25);
		            Table23.setWidthPercentage(100);
		            Table23.getDefaultCell().setBorder(Rectangle.BOX);
		            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table23.getDefaultCell().setColspan(1);
		            Table23.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
		            
		            Table23.getDefaultCell().setBorder(Rectangle.BOX);  
		            Table23.getDefaultCell().setColspan(6);
		            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            Table23.addCell(new Phrase(new Chunk("Invoice Amount",small_black_bold)));
		            
		            Table23.getDefaultCell().setColspan(1);
		            
		            Table23.getDefaultCell().setBorder(Rectangle.BOX);
		            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table23.addCell(new Phrase(new Chunk(currency_nm, small_black_bold)));
		            
		            Table23.getDefaultCell().setBorder(Rectangle.BOX);
		            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table23.addCell(new Phrase(new Chunk(es.formatAmount(net_amt),small_black_bold)));
		            
		            float[] ContactAddrWidths26_1 = {0.100F};
		   	     	PdfPTable Table24_1 = new PdfPTable(ContactAddrWidths26_1);
		   	     	Table24_1.setWidthPercentage(100);
		   	     	Table24_1.getDefaultCell().setBorder(Rectangle.BOX);
		   	     	Table24_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		   	     	Table24_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		   	     	Table24_1.addCell(new Phrase(new Chunk("Amount in Words | "+currency_nm+"  "+amt_in_word+" Only",black_bold)));
		   	  		
		   	     	float[] ContactAddrWidths26 = {0.100F};
		   	     	PdfPTable Table24 = new PdfPTable(ContactAddrWidths26);
		   	     	Table24.setWidthPercentage(100);
		   	     	Table24.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		   	     	Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		   	  		Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		   	  		Table24.addCell(new Phrase(new Chunk("For "+supp_nm,black_bold)));
		
		   	  		float[] ContactAddrWidths27 = {0.100F};
		   	  		PdfPTable Table25 = new PdfPTable(ContactAddrWidths27);
			     	Table25.setWidthPercentage(100);
			     	Table25.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			     	Table25.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			     	Table25.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
			     	Table25.addCell(new Phrase(new Chunk("Authorised Signatory",black_bold)));
			     	
			     	 PdfPTable BillingFieldsInfoTable81 = new PdfPTable(1);
				     BillingFieldsInfoTable81.setWidthPercentage(20);
				     BillingFieldsInfoTable81.setHorizontalAlignment(Element.ALIGN_LEFT);
				     BillingFieldsInfoTable81.getDefaultCell().setBorder(Rectangle.BOX);
				     BillingFieldsInfoTable81.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				     BillingFieldsInfoTable81.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		
				     PdfFormField field = PdfFormField.createSignature(writer);
				     field.setFieldName(SIGNAME);
		
				     field.setPage();
				     field.setWidget(new Rectangle(72, 732, 144, 780), PdfAnnotation.HIGHLIGHT_NONE);
				     field.setFlags(PdfAnnotation.FLAGS_PRINT);
				     writer.addAnnotation(field);
		
				     PdfPCell sigCell = new PdfPCell();
				     FieldPositioningEvents events = new FieldPositioningEvents(writer, field);
				     sigCell.setCellEvent(events);
				     sigCell.setBorder(Rectangle.NO_BORDER);
				     sigCell.setFixedHeight(50f);
				     BillingFieldsInfoTable81.addCell(sigCell);
				    
				     PdfPTable leftSignTable = new PdfPTable(1);
				     leftSignTable.setWidthPercentage(100);
				     leftSignTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				     leftSignTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		
				     leftSignTable.addCell(Table24);
				     leftSignTable.addCell(BillingFieldsInfoTable81);
				     leftSignTable.addCell(Table25);
		
				     float[] InvoiceDateInfoWidths_qr = {0.30f, 0.20f, 0.40f};
				     PdfPTable InvoiceDateInfoTable_qr = new PdfPTable(InvoiceDateInfoWidths_qr);
		

		
				     PdfPTable finalRow = new PdfPTable(3);
				     finalRow.setWidthPercentage(100);
				     finalRow.setWidths(new float[]{0.3f, 0.2f ,0.5f});
				     finalRow.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		
				     finalRow.addCell(leftSignTable);
				     finalRow.addCell("");
				     finalRow.addCell(InvoiceDateInfoTable_qr);
				     
				     float[] ContactAddrWidths30 = {0.100F};
		   	  		PdfPTable Table28 = new PdfPTable(ContactAddrWidths30);
			     	Table28.setWidthPercentage(100);
			     	Table28.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			     	Table28.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			     	Table28.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
			     	Table28.addCell(new Phrase(new Chunk("Notes:",black_bold)));
			     	
			     	float[] ContactAddrWidths31 = {0.200F};
		   	  		PdfPTable Table29 = new PdfPTable(ContactAddrWidths31);
			     	Table29.setWidthPercentage(100);
			     	Table29.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			     	Table29.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			     	Table29.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
			     	Table29.addCell(new Phrase(new Chunk(remark1,small_black_normal)));
	
	
					document.add(mainTable);
					document.add(new Paragraph("  "));
					document.add(Table3);
					document.add(Table4);
					document.add(Table5);
					document.add(Table6);
					document.add(Table7);
					document.add(Table8);
					document.add(Table9);
					document.add(Table10);
					document.add(Table13);
					document.add(new Paragraph("  "));
					document.add(Table11);
					document.add(new Paragraph("  "));
					document.add(Table14);
					document.add(Table15);
					document.add(new Paragraph("  "));
					document.add(Table18);
					document.add(Table19);
					document.add(Table21);
//					document.add(Table22);
					document.add(Table23);
					document.add(new Paragraph("  "));
					document.add(Table24_1);
					document.add(new Paragraph("  "));
					document.add(finalRow);
					if(!remark1.equals(""))
					{
						document.add(Table28);
						document.add(Table29);
					}
				
//				}
	            
	            document.close();

	            
	            if(is_print.equals("1"))
				{
		            File isPDFexist = new File(path);
					if(isPDFexist.exists())
					{
						int update_flag=0;
						int pdf_entry=0;
						
						int cont=0;
						queryString="UPDATE FMS_OTH_INVOICE_MST SET PDF_INV_DTL=? ";
							if(print_pdf_type.equals("O"))
							{
								queryString+= ",PRINT_BY_ORI=?,PRINT_DT_ORI=SYSDATE ";
							}
							else if(print_pdf_type.equals("D"))
							{
								queryString+= ",PRINT_BY_DUP=?,PRINT_DT_DUP=SYSDATE ";
							}
							else if(print_pdf_type.equals("T"))
							{
								queryString+= ",PRINT_BY_TRI=?,PRINT_DT_TRI=SYSDATE ";
							}
							queryString+= "WHERE COMPANY_CD=? AND INVOICE_NO=? AND INVOICE_TYPE=? AND INVOICE_SEQ = ? AND SUPPLIER_CD = ?";
							
						stmt=conn.prepareStatement(queryString);
						stmt.setString(++cont, print_pdf_type);
						if(print_pdf_type.equals("O"))
						{
							stmt.setString(++cont, emp_cd);
						}
						else if(print_pdf_type.equals("D"))
						{
							stmt.setString(++cont, emp_cd);
						}
						else if(print_pdf_type.equals("T"))
						{
							stmt.setString(++cont, emp_cd);
						}
						stmt.setString(++cont, comp_cd);
						stmt.setString(++cont, inv_no);
						stmt.setString(++cont, inv_type);
						stmt.setString(++cont, inv_seq);
						stmt.setString(++cont, supp_cd);
						update_flag=stmt.executeUpdate();
						stmt.close();
						
						int count=0;
				        queryString1="SELECT COUNT(*) "
				        		+ "FROM FMS_OTH_INV_FILE_DTL "
				        		+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? "
				        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND SUPPLIER_CD =? ";
				        stmt1=conn.prepareStatement(queryString1);
				        stmt1.setString(1, comp_cd);
				        stmt1.setString(2, inv_type);
				        stmt1.setString(3, inv_seq);
				        stmt1.setString(4, fin_yr);
				        stmt1.setString(5, print_pdf_type);
				        stmt1.setString(6, supp_cd);
				        rset1=stmt1.executeQuery();
				        if(rset1.next())
				        {
				        	count=rset1.getInt(1);
				        }
				        rset1.close();
				        stmt1.close();
				        
				        if(count > 0)
				        {
				        	queryString1="UPDATE FMS_OTH_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
				        			+ "WHERE COMPANY_CD=? AND INVOICE_TYPE=? "
					        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=? AND SUPPLIER_CD =?";
				        	stmt1=conn.prepareStatement(queryString1);
				        	stmt1.setString(1, file_nm);
				        	stmt1.setString(2, emp_cd);
				        	stmt1.setString(3, comp_cd);
				 	        stmt1.setString(4, inv_type);
				 	        stmt1.setString(5, inv_seq);
				 	        stmt1.setString(6, fin_yr);
				 	        stmt1.setString(7, print_pdf_type); 
				 	        stmt1.setString(8, supp_cd); 
				 	        pdf_entry=stmt1.executeUpdate();
				        	
				        	stmt1.close();
				        }
				        else
				        {
				        	queryString1="INSERT INTO FMS_OTH_INV_FILE_DTL(COMPANY_CD,SUPPLIER_CD,INVOICE_SEQ,INVOICE_TYPE,FINANCIAL_YEAR,PDF_TYPE,"
				        			+ "FILE_NAME,ENT_BY,ENT_DT) "
				        			+ "VALUES(?,?,?,?,?,?,"
				        			+ "?,?,SYSDATE)";
				        	stmt1=conn.prepareStatement(queryString1);
				        	stmt1.setString(1, comp_cd);
				        	stmt1.setString(2, supp_cd);
				 	        stmt1.setString(3, inv_seq);
				 	        stmt1.setString(4, inv_type);
				 	        stmt1.setString(5, fin_yr);
				 	        stmt1.setString(6, print_pdf_type);
				 	        stmt1.setString(7, file_nm);
				        	stmt1.setString(8, emp_cd);
				        	pdf_entry=stmt1.executeUpdate();
				        	
				        	stmt1.close();
				        }
				        
				        if(pdf_entry == 0 || update_flag == 0)
				        {
				        	conn.rollback();
				        	deletePdfFile(path);
				        	msg = "Failed! - PDF "+file_nm+" for ReExport Invoice with Invoice No. "+inv_no+" Generation Failed!";
				        	msg_type="E";
				        }
				        else
				        {
				        	conn.commit();
				        	msg = "Successful! - PDF "+file_nm+" for ReExport Invoice with Invoice No. "+inv_no+" Generated Successfully!";
				        	msg_type="S";
				        }
					}
					else
					{
						msg="Failed! - PDF for ReExport Invoice with Invoice No. "+inv_no+" Generation Failed!";
						msg_type="E";
					}
				}
			}
		}
		catch(Exception e)
		{
			conn.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			
			document.close();
			deletePdfFile(path);
			
			msg = "Error in Exception! - PDF for ReExport Invoice with Invoice No. "+inv_no+" Generation Failed!";
			msg_type="E";	
		}
		finally
		{
			document.close();
		}
		
		if(is_print.equals("1"))
		{
			try
			{
				new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, "", "", msg);  	
			}
			catch(Exception infoLogger)
			{
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
			}
		}
	}
	
	
	String callFlag = "";
	public void setCallFlag(String callFlag) {this.callFlag = callFlag;}
	String comp_cd = "";
	public void setComp_cd(String comp_cd) {this.comp_cd = comp_cd;}
	String comp_nm = "";
	public void setComp_nm(String comp_nm) {this.comp_nm = comp_nm;}
	String comp_logo = "";
	public void setComp_logo(String comp_logo) {this.comp_logo = comp_logo;}
	String inv_no="";
	public void setInv_No(String inv_seq_no) {this.inv_no = inv_seq_no;}
	String inv_type = "";
	public void setInv_type(String inv_type) {this.inv_type = inv_type;}
	String print_pdf_type="";
	public void setPrint_pdf_type(String print_pdf_type) {this.print_pdf_type = print_pdf_type;}
	public void setSupplier_Cd(String supp_cd) {this.supp_cd = supp_cd;}
	public void setInv_seq(String inv_seq) {this.inv_seq = inv_seq;}
	public void setFin_yr(String fin_yr) {this.fin_yr = fin_yr;}
	public void setIs_print(String is_print) {this.is_print = is_print;}
	public void setBu_unit(String bu_unit) {this.bu_unit = bu_unit;}

	String file_url = "";
	String file_nm = "";
	public String getFile_url() {return file_url;}
	public String getFile_nm() {return file_nm;}
	
	
	String form_id="";
	String form_nm="";
	String mod_cd="";
	String mod_nm="";
	String emp_cd="";
	
	public void setForm_id(String form_id) {this.form_id = form_id;}
	public void setForm_nm(String form_nm) {this.form_nm = form_nm;}
	public void setMod_cd(String mod_cd) {this.mod_cd = mod_cd;}
	public void setMod_nm(String mod_nm) {this.mod_nm = mod_nm;}
	public void setEmp_cd(String emp_cd) {this.emp_cd = emp_cd;}
	
	String msg="";
	String msg_type="";
	String all_pdf_print="";
	String is_print="";
	
	
	String supp_cd="";      
	String supp_nm="";      
	String supp_abbr="";    
	String supp_pin = "";
	String supp_addr="";   
	String supp_city="";    
	String supp_pan_no="";  
	String supp_state=""; 
	String supp_gstin_no = "";
	
	String vendor_cd = "";
	String vend_abbr="";   	
	String vend_city="";		
	String vend_pan_no="";		
	String vend_gstin_no="";	
	String vend_pin_no="";		
	String vend_country="";		 
	String vend_state="";	 	
	String vendor_name="";	 	
	String sac_code="";	 	
	String sac_des="";	
	
	String inv_dt = "";
	String sac_cd = "";
	String currency = "";
	String exchng_rate = "";
	String exchng_eff_dt = "";
	String purchase_no = "";
	String ref_no = "";
	String sale_amt = "";
	String tax_name = "";
	String tax_struct_cd = "";
	String tax_struct_dt = "";
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
	String approve_flag = "";
	String gross_amt = "";
	String vend_state_tin = "";
	String supp_state_tin = "";
	String tax_value = "";
	String invoice_raised_in = "";
	String inv_id_seq = "";
	String invoice_category = "";
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
	String abbr="";
	String amt_in_word="";
	String gross_revenue="";
	String gross_less="";
	String sign="";
	String gross_formula = "a ";
	String less_formula="b ";
	String gate_pass="";
	String sale_no="";
	String cess_flag="";
	String cess_amt="";
	String tax_struct_info = "";
	String bu_unit="";
	
	String reference_no="";
	String per_carriage="";
	String final_destination="";
	String country_of_origin="";
	String port_of_discharge="";
	String port_of_loading="";
	String pay_terms="";
	
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
	
	Vector VRATE =  new Vector();
	Vector VCARGO_AMT =  new Vector();
	Vector VCARGO_DT =  new Vector();
	Vector VQTY_MMBTU =  new Vector();
	Vector VSHIP_NAME = new Vector();
	Vector VDIS_PORT = new Vector();
	
	Vector VGITEM_AMT = new Vector();
	Vector VGAMT_DES = new Vector();
	Vector VGSIGN = new Vector();
	Vector VLSIGN = new Vector();
	Vector VLAMT_DES = new Vector();
	Vector VLITEM_AMT = new Vector();
	
	Vector VPRICE = new Vector();
	Vector VSAC_CODE = new Vector();
	Vector VQTY = new Vector();
	Vector VUOM_NO = new Vector();
	Vector VTAX_AMT = new Vector();
	Vector VCESS_PER = new Vector();
	Vector VCESS_AMT = new Vector();
	Vector VTAX_STRUCTURE_CD = new Vector();
	Vector VTAX_STRUCTURE_DESC = new Vector();
	Vector VTAX_STRUCT_APP_DT = new Vector();
	Vector VTAX_STRUCT_NM = new Vector();
	Vector VTAX_VAL_AMT = new Vector();
	Vector VITEM_TOTAL = new Vector();
	Vector VTAX_STRUCTURE1 = new Vector();
	Vector VTAX_STRUCTURE = new Vector();
	
	Vector VDIMENSION = new Vector();
	Vector VNET_WT = new Vector();
	Vector VGROSS_WT = new Vector();
	Vector VPACK_DTLS = new Vector();
	
	double taxValue = 0;
	
	public String getMsg() {return msg;}
	public String getMsg_type() {return msg_type;}
	public String getAll_pdf_print() {return all_pdf_print;}
	
}
