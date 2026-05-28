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

public class DataBean_Oth_Inv_CR_DR_pdf_Gen
{
	String db_src_file_name="DataBean_Oth_Inv_CR_DR_pdf_Gen.java";
	
	Connection conn;
	PreparedStatement stmt;
	PreparedStatement stmt0;
	PreparedStatement stmt1;
	PreparedStatement stmt2;
	PreparedStatement stmt_tmp;
	ResultSet rset;
	ResultSet rset0;
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
	NumberFormat nf00 = new DecimalFormat("###########0");

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
		    			if(callFlag.equalsIgnoreCase("PDF_OTHER_INVOICE_CR_DR")) //AP20251120
		    			{
		    				if(inv_type.equals("HSA")) 
		    				{
		    					pdfCRDRcostrechargeHPPLShippingAgent(); //AP20251120
		    				}
		    				else if(inv_type.equals("AHPL")) 
		    				{
		    					pdfCRDRAhplShare(); //DT20260203
		    				}
		    				else if(inv_type.equals("HS")) 
		    				{
		    					pdfCRDRPFAFees(); //DT20260220
		    				}
		    				else if(inv_type.equals("COSTRH")) 
		    				{
		    					pdfCRDRCOSTRH(); //DT20260225
		    				}
		    				else if(inv_type.equals("COSTR")) 
		    				{
		    					pdfCRDRCOSTR(); //DT20260225
		    				}
		    				else if(inv_type.equals("SFA")) 
		    				{
		    					pdfCRDRSFA(); //DT20260319
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
		    	if(rset_tmp != null){try{rset_tmp.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(stmt != null){try{stmt.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
		    	if(stmt0 != null){try{stmt0.close();}catch(SQLException e){new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);}}
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
	
	//AP20251121
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
	
	//AP20251121
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

	
	//AP20251121
	public void pdfCRDRcostrechargeHPPLShippingAgent() throws SQLException
	{
		String function_nm="pdfCRDRcostrechargeHPPLShippingAgent()";
		
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
			
			String cr_dr_type_nm="";
			if(cr_dr_type.equals("CR"))
			{
				cr_dr_type_nm="Credit Note";
			}
			else if(cr_dr_type.equals("DR"))
			{
				cr_dr_type_nm="Debit Note";
			}
			
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
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.INVOICE_TYPE = B.INVOICE_TYPE AND A.INV_FLAG =?";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_no);
				stmt.setString(3, inv_type);
				stmt.setString(4, print_pdf_type);
				stmt.setString(5, inv_type);
				stmt.setString(6, cr_dr_type);
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
				msg="Failed! - PDF for Berthing Invoice "+cr_dr_type_nm+" with Invoice No. "+inv_no+" already Generated!";
	        	msg_type="E";
	        }
			else
			{
				
				getSelectedInvoiceDtl();
				getCrDrInvoiceDtl();
				getCrDrRefDetail();
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
		        	file_nm=print_pdf_type+"-"+cr_dr_type+"-"+supp_abbr+"-"+inv_type+"-"+inv_no.replace("/", "-")+".pdf";
	
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
	            centerTable.addCell(new Phrase(new Chunk(cr_dr_type_nm, small_black_normal_12)));
	            String info = cr_dr_type_nm+" issued under Rule 46 of the Central Goods and Services Tax Rules, 2017";
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
	            Table14.addCell(new Phrase(new Chunk(cr_dr_type_nm+" No:",black_bold)));
	
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
	            Table15.addCell(new Phrase(new Chunk(cr_dr_type_nm+" Date:",black_bold)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk(crdr_invoice_dt,black_bold)));

	            float[] ContactAddrWidths18 = {0.40F,0.50F,0.20F,0.40F,0.40F};
	   	     	PdfPTable Table16 = new PdfPTable(ContactAddrWidths18);
	   	     	if(criteri_formula.contains("HRS"))
	   	     	{
		            Table16.setWidthPercentage(100);
		            Table16.getDefaultCell().setBorder(Rectangle.BOX);
		            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            Table16.addCell(new Phrase(new Chunk("No. of Hours Berthing:",black_bold)));
		            
		            Table16.getDefaultCell().setBorder(Rectangle.BOX);
		            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table16.addCell(new Phrase(new Chunk(berthing_hrs,black_bold)));
		
		            Table16.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table16.addCell(new Phrase(new Chunk("",small_black_normal)));
		
		            Table16.getDefaultCell().setBorder(Rectangle.BOX);
		            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            Table16.addCell(new Phrase(new Chunk("Payament Due Date :",black_bold)));
		
		            Table16.getDefaultCell().setBorder(Rectangle.BOX);
		            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table16.addCell(new Phrase(new Chunk(crdr_invoice_due_dt,black_bold)));
	   	     	}
	   	     	else
	   	     	{
		   	     	Table16.setWidthPercentage(100);
		            Table16.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            Table16.addCell(new Phrase(new Chunk("",black_bold)));
		            
		            Table16.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table16.addCell(new Phrase(new Chunk("",black_bold)));
		
		            Table16.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table16.addCell(new Phrase(new Chunk("",small_black_normal)));
		
		            Table16.getDefaultCell().setBorder(Rectangle.BOX);
		            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            Table16.addCell(new Phrase(new Chunk("Payament Due Date :",black_bold)));
		
		            Table16.getDefaultCell().setBorder(Rectangle.BOX);
		            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table16.addCell(new Phrase(new Chunk(crdr_invoice_due_dt,black_bold)));
	   	     	}
	            
	            float[] ContactAddrWidths28 = {0.50F,0.30F,0.20F,0.20F,0.20F};
	            if (criteri_formula.contains("GRT"))
	            {
	                ContactAddrWidths28 = new float[] 
	                { 
	                    0.50F, 0.30F, 0.20F, 0.20F, 0.20F, 0.20F, 0.20F 
	                };
	            }
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
	            
	            if(criteri_formula.contains("GRT"))
	            {
		            Table26.getDefaultCell().setBorder(Rectangle.BOX);
		            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table26.addCell(new Phrase(new Chunk("Invoice GRT",black_bold)));
		            
		            Table26.getDefaultCell().setBorder(Rectangle.BOX);
		            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table26.addCell(new Phrase(new Chunk("Applicable GRT",black_bold)));
		            
		            Table26.getDefaultCell().setBorder(Rectangle.BOX);
		            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table26.addCell(new Phrase(new Chunk("Difference In GRT",black_bold)));
	            }
	            else
	            {
	            	Table26.getDefaultCell().setBorder(Rectangle.BOX);
		            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table26.addCell(new Phrase(new Chunk("GRT",black_bold)));
	            }
	            
	            Table26.getDefaultCell().setBorder(Rectangle.BOX);
	            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table26.addCell(new Phrase(new Chunk("Quantity",black_bold)));
	            
	            float[] ContactAddrWidths29 = {0.50F,0.30F,0.20F,0.20F,0.20F};
	            if (criteri_formula.contains("GRT"))
	            {
	            	ContactAddrWidths29 = new float[] 
	                { 
	                    0.50F, 0.30F, 0.20F, 0.20F, 0.20F, 0.20F, 0.20F 
	                };
	            }
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
	            
	            if(criteri_formula.contains("GRT"))
	            {
	            	Table27.getDefaultCell().setBorder(Rectangle.BOX);
		            Table27.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table27.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table27.addCell(new Phrase(new Chunk(main_grt,small_black_normal)));
		            
		            Table27.getDefaultCell().setBorder(Rectangle.BOX);
		            Table27.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table27.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table27.addCell(new Phrase(new Chunk(new_grt,small_black_normal)));
		            
		            Table27.getDefaultCell().setBorder(Rectangle.BOX);
		            Table27.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table27.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table27.addCell(new Phrase(new Chunk(grt,small_black_normal)));
	            }
	            else
	            {
	            	Table27.getDefaultCell().setBorder(Rectangle.BOX);
		            Table27.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table27.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table27.addCell(new Phrase(new Chunk(new_grt,small_black_normal)));
	            }
	            
	            
	            Table27.getDefaultCell().setBorder(Rectangle.BOX);
	            Table27.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table27.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table27.addCell(new Phrase(new Chunk(new_qty_mmbtu+" M\u00B3",small_black_normal)));
	                      
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
	            if(price_cd.equals("2")) 
	            {
	            	curr1 = "USD";
	            	curr2 = "USD";
	            	curr3 = "USD";
	            }
	            String desc_item="";
				if(criteri_formula.contains("HRS"))
				{
					desc_item = "Berthing Charges Refund for "+ berthing_slots +" Slot against Inv No. "+sel_inv_no;
				}
				else
				{
					desc_item="Berthing Charges";
				}
				
	            int sr=1;
	            float[] ContactAddrWidths21 = {0.08F,0.60F,0.20F,0.20F,0.30F,0.20F};
	   	     	PdfPTable Table19 = new PdfPTable(ContactAddrWidths21);
	            if(criteri_formula.contains("RATE"))
	            {
	            	
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
		            Table19.addCell(new Phrase(new Chunk("",small_black_normal)));
		            
		            sr+=1;
		            //float[] ContactAddrWidths21_1 = {0.08F,0.60F,0.20F,0.20F,0.30F,0.20F};
		   	     	//PdfPTable Table19_1 = new PdfPTable(ContactAddrWidths21_1);
		   	     	Table19.setWidthPercentage(100);
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table19.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
		
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            Table19.addCell(new Phrase(new Chunk("Invoice Rate",small_black_normal)));
		
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table19.addCell(new Phrase(new Chunk(curr1,small_black_normal)));
		            
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table19.addCell(new Phrase(new Chunk(main_rate,small_black_normal)));
		            
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table19.addCell(new Phrase(new Chunk("Per GRT/Per 8 Hrs",small_black_normal)));
		
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table19.addCell(new Phrase(new Chunk("",small_black_normal)));
		            
		            sr+=1;
		            //float[] ContactAddrWidths21_2 = {0.08F,0.60F,0.20F,0.20F,0.30F,0.20F};
		   	     	//PdfPTable Table19_2 = new PdfPTable(ContactAddrWidths21_2);
		   	     	Table19.setWidthPercentage(100);
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table19.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
		
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            Table19.addCell(new Phrase(new Chunk("Applicable Rate",small_black_normal)));
		
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table19.addCell(new Phrase(new Chunk(curr1,small_black_normal)));
		            
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table19.addCell(new Phrase(new Chunk(new_rate,small_black_normal)));
		            
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table19.addCell(new Phrase(new Chunk("Per GRT/Per 8 Hrs",small_black_normal)));
		
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table19.addCell(new Phrase(new Chunk("",small_black_normal)));
		            
		            sr+=1;
		            //float[] ContactAddrWidths21_3 = {0.08F,0.60F,0.20F,0.20F,0.30F,0.20F};
		   	     	//PdfPTable Table19_3 = new PdfPTable(ContactAddrWidths21_3);
		   	     	Table19.setWidthPercentage(100);
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table19.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
		
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            Table19.addCell(new Phrase(new Chunk("Difference In Rate",small_black_normal)));
		
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
		            Table19.addCell(new Phrase(new Chunk(es.formatAmount(gross_amt),small_black_normal)));
	            }
	            else
	            {
	            	//float[] ContactAddrWidths21 = {0.08F,0.60F,0.20F,0.20F,0.30F,0.20F};
		   	     	//PdfPTable Table19 = new PdfPTable(ContactAddrWidths21);
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
		            Table19.addCell(new Phrase(new Chunk(new_rate,small_black_normal)));
		            
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table19.addCell(new Phrase(new Chunk("Per GRT/Per 8 Hrs",small_black_normal)));
		
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table19.addCell(new Phrase(new Chunk(es.formatAmount(gross_amt),small_black_normal)));
	            }
	            
	            
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
	            
	            if (tax_struct_info.contains(",")) 
				{
	            	BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_amt));
					BigDecimal factor = new BigDecimal(2);
	            	double temp_srno=sr;
	            	for(int i = 0;i<tax_struct_info.split(", ").length;i++) 
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
			            Table22.addCell(new Phrase(new Chunk(tax_struct_info.split(",")[i],small_black_normal)));
			
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
	            Table22.addCell(new Phrase(new Chunk("Tax ("+tax_struct_info+")",small_black_bold)));
	
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
	            Table23.addCell(new Phrase(new Chunk(es.formatAmount(net_payable),small_black_bold)));
	            
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

				document.add(mainTable);
				document.add(new Paragraph("  "));
				document.add(Table3);
				document.add(Table4);
				document.add(Table5);
				document.add(Table6);
				document.add(Table7);
				//document.add(new Paragraph("  "));
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
				document.add(new Paragraph("  "));
				document.add(Table26);
				document.add(Table27);
				document.add(new Paragraph("  "));
				document.add(Table18);
				document.add(Table19);
				document.add(Table21);
				document.add(Table22);
				document.add(Table23);
				document.add(new Paragraph("  "));
				document.add(Table24_1);
				document.add(finalRow);
				if(cr_dr_type.equals("DR"))
				{
					document.add(Table28);
					document.add(Table29);
					document.add(Table30);
					document.add(Table31);
					document.add(Table32);
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
				        stmt1.setString(4, financial_year);
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
				 	        stmt1.setString(6, financial_year);
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
				 	        stmt1.setString(5, financial_year);
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
				        	msg = "Failed! - PDF "+file_nm+" for Berthing Invoice "+cr_dr_type_nm+" with Invoice No. "+inv_no+" Generation Failed!";
				        	msg_type="E";
				        }
				        else
				        {
				        	conn.commit();
				        	msg = "Successful! - PDF "+file_nm+" for Berthing Invoice "+cr_dr_type_nm+" with Invoice No. "+inv_no+" Generated Successfully!";
				        	msg_type="S";
				        }
					}
					else
					{
						msg="Failed! - PDF for Berthing Invoice "+cr_dr_type_nm+" with Invoice No. "+inv_no+" Generation Failed!";
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
			
			msg = "Error in Exception! - PDF for Berthing Invoice CR/DR with Invoice No. "+inv_no+" Generation Failed!";
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
	
	//AP20251121
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
				
				queryString2="SELECT SHIP_CD,SHIP_NAME,SHIP_FLAG,SHIP_ITEM "
						+ "FROM FMS_SHIP_MST A "
						+ "WHERE EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_SHIP_MST B WHERE A.SHIP_CD=B.SHIP_CD) AND SHIP_CD=? ";
				stmt2=conn.prepareStatement(queryString2);
				stmt2.setString(1, vessel_cd);
				rset2=stmt2.executeQuery();
				if(rset2.next())
				{
					vessel_nm =(rset2.getString(2)==null?"":rset2.getString(2));
					vessel_flag=(rset2.getString(3)==null?"":rset2.getString(3));
					vessel_item=(rset2.getString(4)==null?"":rset2.getString(4));
				}
				rset2.close();
				stmt2.close();
				
				String queryString3 = "SELECT SAC_CODE,SAC_DESC "
						+ "FROM FMS_SAC_MST A "
						+ "WHERE SAC_CD  = ?";
				stmt2=conn.prepareStatement(queryString3);
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
	
	//AP20251121
	public void getCrDrInvoiceDtl()
	{
		String function_nm="getCrDrInvoiceDtl()";
		try
		{
			BigDecimal factor = new BigDecimal(1);
			queryString = "SELECT SUPPLIER_CD,VENDOR_CD,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TO_CHAR(DUE_DT,'DD/MM/YYYY'),GROSS_AMT,TAX_AMT_INR,NET_PAYABLE,"
					+ "INVOICE_RAISED_IN,INV_FLAG,CRITERIA,TO_CHAR(PERIOD_START_DT,'DD/MM/YYYY'),TO_CHAR(PERIOD_END_DT,'DD/MM/YYYY'),REMARK,FINANCIAL_YEAR,"
					+ "APPROVED_FLAG,CHECKED_FLAG,INVOICE_SEQ,INVOICE_NO "
					+ "FROM FMS_OTH_INVOICE_MST ";
			if(is_print.equals("0"))
			{
				queryString	+= "WHERE COMPANY_CD = ? AND INVOICE_SEQ = ? "
						+ "AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG = ? ";
			}
			else 
			{
				queryString	+= "WHERE COMPANY_CD = ? AND INVOICE_TYPE=?  "
						+ "AND INVOICE_NO=? AND INV_FLAG IN ('CR','DR')";
			}				
			stmt = conn.prepareStatement(queryString);
			
			if(is_print.equals("0"))
			{
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_seq);
				stmt.setString(3, inv_type);
				stmt.setString(4, fin_yr);
				stmt.setString(5, supp_cd);
				stmt.setString(6, cr_dr_type);
			}
			else 
			{
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_type);
				stmt.setString(3, inv_no);
			}
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
				inv_seq=rset.getString(17)==null?"":rset.getString(17);
				inv_no=rset.getString(18)==null?"":rset.getString(18);
				
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
				
				BigDecimal grossAmt1 = BigDecimal.valueOf(grossAmt);
				BigDecimal sub_grossAmt1 = grossAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal taxAmt1 = BigDecimal.valueOf(taxAmt);
				BigDecimal sub_taxAmt1 = taxAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal netPayable1 = BigDecimal.valueOf(netPayable);
				BigDecimal sub_netPayable1 = netPayable1.divide(factor, 0, RoundingMode.HALF_UP);
				
				gross_amt=nf.format(sub_grossAmt1);				
				tax_amt=nf.format(sub_taxAmt1);				
				net_payable=nf.format(sub_netPayable1);	
				
				queryString1 = "SELECT GRT,QUANTITY,HRS_BERTHING,TIME_SLOTS_BERTHING,SALE_PRICE,SALE_PRICE_UNIT,TAX_STRUCT_CD "
						+ "FROM FMS_OTH_INVOICE_DTL "
						+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
						+ "AND INVOICE_SEQ=? "
						+ "AND INV_FLAG IN ('CR','DR') ";
				stmt1 = conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, inv_type);
				stmt1.setString(3, financial_year);
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

	//AP20251121
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
				
				new_grt=es.formatWithCommaNoDecimal(""+sub_tmp_grt1);				
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
	
	
	public void pdfCRDRAhplShare() throws SQLException
	{
		String function_nm="pdfCRDRAhplShare()";
		
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
			
			String cr_dr_type_nm="";
			if(cr_dr_type.equals("CR"))
			{
				cr_dr_type_nm="Credit Note";
			}
			else if(cr_dr_type.equals("DR"))
			{
				cr_dr_type_nm="Debit Note";
			}
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
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.INVOICE_TYPE = B.INVOICE_TYPE AND A.INV_FLAG =?";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_no);
				stmt.setString(3, inv_type);
				stmt.setString(4, print_pdf_type);
				stmt.setString(5, inv_type);
				stmt.setString(6, cr_dr_type);
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
				msg="Failed! - PDF for AHPL "+cr_dr_type_nm+" with Invoice No. "+inv_no+" already Generated!";
	        	msg_type="E";
	        }
			else
			{
				
				getAhplInvoiceDtl();
				getAhplCrDrInvoiceDtl();
				getAhplCrDrRefDetail();
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
			        file_nm=print_pdf_type+"-"+cr_dr_type+"-"+supp_abbr+"-"+inv_type+"-"+inv_no.replace("/", "-")+".pdf";
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
	            centerTable.addCell(new Phrase(new Chunk(cr_dr_type_nm, small_black_normal_12)));
	            String info = cr_dr_type_nm+" issued under Rule 46 of the Central Goods and Services Tax Rules, 2017";
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
	            Table14.addCell(new Phrase(new Chunk(cr_dr_type_nm+" No :",black_bold)));
	
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
	            Table15.addCell(new Phrase(new Chunk(cr_dr_type_nm+" Date :",black_bold)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk(crdr_invoice_dt,black_bold)));

	            float[] ContactAddrWidths18 = {0.80F,0.20F,0.50F,0.40F};
	   	     	PdfPTable Table16 = new PdfPTable(ContactAddrWidths18);
	            Table16.setWidthPercentage(100);
	            Table16.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table16.addCell(new Phrase(new Chunk("",black_bold)));
	            
	            Table16.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk("",black_bold)));
	
	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table16.addCell(new Phrase(new Chunk("Payament Due Date :",black_bold)));
	
	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk(crdr_invoice_due_dt,black_bold)));

	            float[] ContactAddrWidths19 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table17 = new PdfPTable(ContactAddrWidths19);
	            Table17.setWidthPercentage(100);
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table17.addCell(new Phrase(new Chunk("Sr.No.",small_black_bold)));
	
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table17.addCell(new Phrase(new Chunk("Description",small_black_bold)));
	
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table17.addCell(new Phrase(new Chunk("Currency",small_black_bold)));
	            
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table17.addCell(new Phrase(new Chunk("Amount",small_black_bold)));
	                     
	            String curr1 = "INR";
	            String dt = dateUtil.getDateFormatDD_MOM_YY(main_invoice_dt);
	            //String desc_item=cr_dr_type_nm+" against Invoice No. "+sel_inv_no+" dated "+dt ;
				
	            int sr=1;
	            
	            float[] ContactAddrWidths20 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table18 = new PdfPTable(ContactAddrWidths20);
//	            Table18.setWidthPercentage(100);
//	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
//	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//	            Table18.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
//	
//	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
//	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//	            Table18.addCell(new Phrase(new Chunk(desc_item,small_black_normal)));
//	
//	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
//	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//	            Table18.addCell(new Phrase(new Chunk(curr1,small_black_normal)));
//	            
//	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
//	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//	            Table18.addCell(new Phrase(new Chunk("",small_black_bold)));
//	            
//	            sr+=1;
	            Table18.setWidthPercentage(100);
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table18.addCell(new Phrase(new Chunk(desc_item,small_black_normal)));
	
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk(curr1,small_black_bold)));
	            
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            if(criteri_formula.contains("GAMT"))
	            {
	            	Table18.addCell(new Phrase(new Chunk(es.formatAmount(gross_amt),small_black_bold)));
	            }
	            else 
	            {
	            	Table18.addCell(new Phrase(new Chunk("",small_black_bold)));
	            }
	            
	            sr+=1;
	            float[] ContactAddrWidths24 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table22 = new PdfPTable(ContactAddrWidths24);
	            
	            if (new_tax_struct_info.contains(",")) 
				{
	            	BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_amt));
					BigDecimal factor = new BigDecimal(2);
	            	double temp_srno=sr;
	            	for(int i = 0;i<new_tax_struct_info.split(", ").length;i++) 
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
			            Table22.addCell(new Phrase(new Chunk(new_tax_struct_info.split(",")[i],small_black_normal)));
			
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
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table22.addCell(new Phrase(new Chunk("Tax ("+new_tax_struct_info+")",small_black_bold)));
	
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table22.addCell(new Phrase(new Chunk(curr1,small_black_bold)));
	            
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
	            Table23.addCell(new Phrase(new Chunk(curr1,small_black_bold)));
	            
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table23.addCell(new Phrase(new Chunk(es.formatAmount(net_payable),small_black_bold)));
	            
	            float[] ContactAddrWidths26_1 = {0.100F};
	   	     	PdfPTable Table24_1 = new PdfPTable(ContactAddrWidths26_1);
	   	     	Table24_1.setWidthPercentage(100);
	   	     	Table24_1.getDefaultCell().setBorder(Rectangle.BOX);
	   	     	Table24_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	   	     	Table24_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	   	     	Table24_1.addCell(new Phrase(new Chunk("Amount in Words | Rupees "+amt_in_word+" Only",black_bold)));
	   	     	
	   	     	float[] ContactAddrWidths26_2 = {0.100F};
	   	     	PdfPTable Table24_2 = new PdfPTable(ContactAddrWidths26_2);
	   	     	Table24_2.setWidthPercentage(100);
	   	     	Table24_2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	   	     	Table24_2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	   	     	Table24_2.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	   	     	Table24_2.addCell(new Phrase(new Chunk("Reason for "+cr_dr_type_nm+" : "+reason,small_black_normal)));
	            
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
				document.add(new Paragraph("  "));
				document.add(Table17);
				document.add(Table18);
				document.add(Table22);
				document.add(Table23);
				document.add(new Paragraph("  "));
				document.add(Table24_1);
				document.add(new Paragraph("  "));
				document.add(Table24_2);
				document.add(new Paragraph("  "));
				document.add(finalRow);
				if(!remark1.equals(""))
				{
					document.add(Table28);
					document.add(Table29);
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
				        stmt1.setString(4, financial_year);
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
				 	        stmt1.setString(6, financial_year);
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
				 	        stmt1.setString(5, financial_year);
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
				        	msg = "Failed! - PDF "+file_nm+" for AHPL Share CR/DR "+cr_dr_type_nm+" with Invoice No. "+inv_no+" Generation Failed!";
				        	msg_type="E";
				        }
				        else
				        {
				        	conn.commit();
				        	msg = "Successful! - PDF "+file_nm+" for AHPL Share CR/DR "+cr_dr_type_nm+" with Invoice No. "+inv_no+" Generated Successfully!";
				        	msg_type="S";
				        }
					}
					else
					{
						msg="Failed! - PDF for AHPL Share CR/DR "+cr_dr_type_nm+" with Invoice No. "+inv_no+" Generation Failed!";
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
			
			msg = "Error in Exception! - PDF for AHPL Share CR/DR with Invoice No. "+inv_no+" Generation Failed!";
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
					+ "APPROVED_FLAG,CHECKED_FLAG,INVOICE_NO,INVOICE_ID_SEQ,INVOICE_SEQ,REMARK "
					+ "FROM FMS_OTH_INVOICE_MST ";
			if(is_print.equals("0"))
			{
				queryString	+= "WHERE COMPANY_CD = ? AND INVOICE_SEQ = ? "
						+ "AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG = ? ";
			}
			else 
			{
				queryString	+= "WHERE COMPANY_CD = ? AND INVOICE_TYPE=?  "
						+ "AND INVOICE_NO=? AND INV_FLAG IN ('CR','DR')";
			}				
			stmt = conn.prepareStatement(queryString);
			
			if(is_print.equals("0"))
			{
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_seq);
				stmt.setString(3, inv_type);
				stmt.setString(4, fin_yr);
				stmt.setString(5, supp_cd);
				stmt.setString(6, cr_dr_type);
			}
			else 
			{
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_type);
				stmt.setString(3, inv_no);
			}
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
				inv_seq=rset.getString(19)==null?"":rset.getString(19);
				remark1=rset.getString(20)==null?"":rset.getString(20);
				
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
				
				BigDecimal grossAmt1 = BigDecimal.valueOf(grossAmt);
				BigDecimal sub_grossAmt1 = grossAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal taxAmt1 = BigDecimal.valueOf(taxAmt);
				BigDecimal sub_taxAmt1 = taxAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal netPayable1 = BigDecimal.valueOf(netPayable);
				BigDecimal sub_netPayable1 = netPayable1.divide(factor, 0, RoundingMode.HALF_UP);
				
				gross_amt=nf.format(sub_grossAmt1);				
				tax_amt=nf.format(sub_taxAmt1);				
				net_payable=nf.format(sub_netPayable1);		
				if(!net_payable.equalsIgnoreCase(""))		
				{
					amt_in_word=es.convert(Double.parseDouble(net_payable));
				}
				
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
				
				
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
		
	
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
					+ "APPROVED_FLAG,CHECKED_FLAG,INVOICE_NO,INVOICE_ID_SEQ,EXCHG_RATE_VALUE,SALE_AMT,INVOICE_SEQ,INVOICE_TYPE "
					+ "FROM FMS_OTH_INVOICE_MST ";
			if(is_print.equals("0"))
			{
				queryString	+= "WHERE COMPANY_CD = ? AND INVOICE_SEQ = ? "
						+ "AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG = ? ";
			}
			else 
			{
				queryString	+= "WHERE COMPANY_CD = ? AND INVOICE_TYPE=?  "
						+ "AND INVOICE_NO=? AND INV_FLAG IN ('CR','DR')";
			}				
			stmt = conn.prepareStatement(queryString);
			
			if(is_print.equals("0"))
			{
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_seq);
				stmt.setString(3, inv_type);
				stmt.setString(4, fin_yr);
				stmt.setString(5, supp_cd);
				stmt.setString(6, cr_dr_type);
			}
			else 
			{
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_type);
				stmt.setString(3, inv_no);
			}
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
				fin_yr=rset.getString(14)==null?"":rset.getString(14);
				inv_no=rset.getString(17)==null?"":rset.getString(17);
				inv_id_seq=rset.getString(18)==null?"":rset.getString(18);
				double grossamt11=rset.getDouble(20);
				double exchngrate = rset.getDouble(19);
				inv_seq=rset.getString(21)==null?"":rset.getString(21);
				inv_type=rset.getString(22)==null?"":rset.getString(22);
				
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
				}
				else 
				{
					VQTY_MMBTU.add("");
					VRATE.add("");
					VCARGO_AMT.add("");
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
					+ "AND SUPPLIER_CD=? AND INVOICE_SEQ=? ";
			stmt = conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, inv_type);
			stmt.setString(3, fin_yr);
			stmt.setString(4, supp_cd);
			stmt.setString(5, inv_seq);
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
				else if(criteri_formula.contains("EXCHG"))
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
	
	
	public void pdfCRDRPFAFees() throws SQLException
	{
		String function_nm="pdfCRDRPFAFees()";
		
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
			
			String cr_dr_type_nm="";
			if(cr_dr_type.equals("CR"))
			{
				cr_dr_type_nm="Credit Note";
			}
			else if(cr_dr_type.equals("DR"))
			{
				cr_dr_type_nm="Debit Note";
			}
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
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.INVOICE_TYPE = B.INVOICE_TYPE AND A.INV_FLAG =?";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_no);
				stmt.setString(3, inv_type);
				stmt.setString(4, print_pdf_type);
				stmt.setString(5, inv_type);
				stmt.setString(6, cr_dr_type);
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
				msg="Failed! - PDF for PFA Fees "+cr_dr_type_nm+" with Invoice No. "+inv_no+" already Generated!";
	        	msg_type="E";
	        }
			else
			{
				getPFAInvoiceDtl();
				getSupplierDtl();
				getVendorDtl();
				getInvoiceCargoDetails();
				getPFACrDrInvoiceDtl();
				getPFACrDrCargoDetails();
				getPFACrDrRefCargoDetails();
				getPFACrDrRefDetail();
				
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
			        file_nm=print_pdf_type+"-"+cr_dr_type+"-"+supp_abbr+"-"+inv_type+"-"+inv_no.replace("/", "-")+".pdf";
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
	            centerTable.addCell(new Phrase(new Chunk(cr_dr_type_nm, small_black_normal_12)));
	            String info = cr_dr_type_nm+" issued under Rule 46 of the Central Goods and Services Tax Rules, 2017";
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
	            Table14.addCell(new Phrase(new Chunk(cr_dr_type_nm+" No :",black_bold)));
	
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
	            Table15.addCell(new Phrase(new Chunk(cr_dr_type_nm+" Date :",black_bold)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk(crdr_invoice_dt,black_bold)));

	            float[] ContactAddrWidths18 = {0.80F,0.20F,0.50F,0.40F};
	   	     	PdfPTable Table16 = new PdfPTable(ContactAddrWidths18);
	            Table16.setWidthPercentage(100);
	            Table16.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table16.addCell(new Phrase(new Chunk("",black_bold)));
	            
	            Table16.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk("",black_bold)));
	
	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table16.addCell(new Phrase(new Chunk("Payament Due Date :",black_bold)));
	
	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk(crdr_invoice_due_dt,black_bold)));

	            float[] ContactAddrWidths19 = {0.08F,0.20F,0.20F,0.20F,0.20F,0.20F,0.20F};
	   	     	PdfPTable Table17 = new PdfPTable(ContactAddrWidths19);
	            Table17.setWidthPercentage(100);
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setColspan(1);
	            Table17.addCell(new Phrase(new Chunk("Sr.No.",small_black_bold)));
	
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setColspan(3);
	            Table17.addCell(new Phrase(new Chunk("Description",small_black_bold)));
	
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setColspan(1);
	            Table17.addCell(new Phrase(new Chunk("Currency",small_black_bold)));
	            
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setColspan(1);
	            Table17.addCell(new Phrase(new Chunk("Rate",small_black_bold)));
	            
	            
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setColspan(1);
	            Table17.addCell(new Phrase(new Chunk("Amount",small_black_bold)));
	                     
	            String curr1 = "USD";
	            String curr2 = "INR/USD";
	            String curr3 = "INR";
	            
	            String dt = dateUtil.getDateFormatDD_MOM_YY(main_invoice_dt);
	            String desc_item="PFA "+cr_dr_type_nm+" for "+reason;
				
	            int sr=1;
	            
	            float[] ContactAddrWidths20 = {0.08F,0.20F,0.20F,0.20F,0.20F,0.20F,0.20F};
	   	     	PdfPTable Table18 = new PdfPTable(ContactAddrWidths20);
	            Table18.setWidthPercentage(100);
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setColspan(1);
	            Table18.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table18.getDefaultCell().setColspan(3);
	            Table18.addCell(new Phrase(new Chunk(desc_item+"\n\nFor the Following Cargoes received in the month of "+dateUtil.getShortMonthName(main_invoice_dt)+" "+main_invoice_dt.split("/")[2],small_black_normal)));
	
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setColspan(1);
	            Table18.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table18.getDefaultCell().setColspan(1);
	            Table18.addCell(new Phrase(new Chunk("",small_black_bold)));
	            
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table18.getDefaultCell().setColspan(1);
	            Table18.addCell(new Phrase(new Chunk("",small_black_bold)));
	            
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
	            
	            for(int j=0; j<VNSHIP_NAME.size(); j++)
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
		            Table26.addCell(new Phrase(new Chunk(""+VNCARGO_DATE.elementAt(j),small_black_normal)));
		
		            Table26.getDefaultCell().setBorder(Rectangle.BOX);
		            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table26.getDefaultCell().setColspan(1);
		            Table26.addCell(new Phrase(new Chunk(""+VNSHIP_NAME.elementAt(j),small_black_normal)));
		            
		            Table26.getDefaultCell().setBorder(Rectangle.BOX);
		            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table26.getDefaultCell().setColspan(1);
		            Table26.addCell(new Phrase(new Chunk(""+VNQTY_MMBTU.elementAt(j),small_black_normal)));
		            
		            Table26.getDefaultCell().setBorder(Rectangle.BOX);
		            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table26.getDefaultCell().setColspan(1);
		            Table26.addCell(new Phrase(new Chunk(curr1,small_black_normal)));
		
		            Table26.getDefaultCell().setBorder(Rectangle.BOX);
		            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT); 
		            Table26.getDefaultCell().setColspan(1); 
		            Table26.addCell(new Phrase(new Chunk(""+VNCARGO_RATE.elementAt(j),small_black_normal)));
		            
		            Table26.getDefaultCell().setBorder(Rectangle.BOX);
		            Table26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table26.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT); 
		            Table26.getDefaultCell().setColspan(1); 
		            Table26.addCell(new Phrase(new Chunk(""+VNCARGO_AMT.elementAt(j),small_black_normal)));
	            }
	            
	            sr += 1;
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
	            Table19.addCell(new Phrase(new Chunk("Total Charges",small_black_bold)));
	
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table19.getDefaultCell().setColspan(1);
	            Table19.addCell(new Phrase(new Chunk(curr1,small_black_bold)));
	            
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table19.getDefaultCell().setColspan(1);
	            Table19.addCell(new Phrase(new Chunk("",small_black_normal)));
	
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT); 
	            Table19.getDefaultCell().setColspan(1); 
	            
	            if(criteri_formula.contains("EXCHG"))
				{
	            	Table19.addCell(new Phrase(new Chunk(es.formatAmount(new_gross_amt),small_black_bold)));
				}
	            else 
	            {
	            	Table19.addCell(new Phrase(new Chunk(es.formatAmount(gross_amt),small_black_bold)));
	            }
	            
	            
	            if(criteri_formula.contains("EXCHG"))
				{
		            sr += 1;
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
		            Table19.addCell(new Phrase(new Chunk("Exchange Rate",small_black_normal)));
		
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table19.getDefaultCell().setColspan(1);
		            Table19.addCell(new Phrase(new Chunk(curr2,small_black_normal)));
		            
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table19.getDefaultCell().setColspan(1);
		            Table19.addCell(new Phrase(new Chunk(main_exchang_rate,small_black_normal)));
		
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT); 
		            Table19.getDefaultCell().setColspan(1); 
		            Table19.addCell(new Phrase(new Chunk("",small_black_normal)));
		            
		            sr += 1;
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
		            Table19.addCell(new Phrase(new Chunk("Applicable Exchange Rate",small_black_normal)));
		
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table19.getDefaultCell().setColspan(1);
		            Table19.addCell(new Phrase(new Chunk(curr2,small_black_normal)));
		            
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table19.getDefaultCell().setColspan(1);
		            Table19.addCell(new Phrase(new Chunk(new_exchang_rate,small_black_normal)));
		
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT); 
		            Table19.getDefaultCell().setColspan(1); 
		            Table19.addCell(new Phrase(new Chunk("",small_black_normal)));
		            
		            sr += 1;
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
		            Table19.addCell(new Phrase(new Chunk("Difference in Exchange Rate",small_black_normal)));
		
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table19.getDefaultCell().setColspan(1);
		            Table19.addCell(new Phrase(new Chunk(curr2,small_black_normal)));
		            
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table19.getDefaultCell().setColspan(1);
		            Table19.addCell(new Phrase(new Chunk(exchng_rate,small_black_normal)));
		
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT); 
		            Table19.getDefaultCell().setColspan(1); 
		            Table19.addCell(new Phrase(new Chunk("",small_black_normal)));
				}
	            else 
	            {
	            	sr += 1;
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
		            Table19.addCell(new Phrase(new Chunk("Exchange Rate",small_black_normal)));
		
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table19.getDefaultCell().setColspan(1);
		            Table19.addCell(new Phrase(new Chunk(curr2,small_black_normal)));
		            
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table19.getDefaultCell().setColspan(1);
		            Table19.addCell(new Phrase(new Chunk(main_exchang_rate,small_black_normal)));
		
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT); 
		            Table19.getDefaultCell().setColspan(1); 
		            Table19.addCell(new Phrase(new Chunk("",small_black_normal)));
	            }
	            
	            
	            sr += 1;
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
	            Table19.addCell(new Phrase(new Chunk("Gross Amount",small_black_bold)));
	
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table19.getDefaultCell().setColspan(1);
	            Table19.addCell(new Phrase(new Chunk(curr3,small_black_bold)));
	            
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table19.getDefaultCell().setColspan(1);
	            Table19.addCell(new Phrase(new Chunk("",small_black_bold)));
	
	            Table19.getDefaultCell().setBorder(Rectangle.BOX);
	            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT); 
	            Table19.getDefaultCell().setColspan(1); 
	            Table19.addCell(new Phrase(new Chunk(es.formatAmount(gross_amt1),small_black_bold)));
	            
	            sr+=1;
	            float[] ContactAddrWidths24 = {0.08F,0.20F,0.20F,0.20F,0.20F,0.20F,0.20F};
	   	     	PdfPTable Table22 = new PdfPTable(ContactAddrWidths24);
	            
	            if (new_tax_struct_info.contains(",")) 
				{
	            	BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_amt));
					BigDecimal factor = new BigDecimal(2);
	            	double temp_srno=sr;
	            	for(int i = 0;i<new_tax_struct_info.split(", ").length;i++) 
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
			            Table22.addCell(new Phrase(new Chunk(new_tax_struct_info.split(",")[i],small_black_normal)));
			
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
	            Table22.addCell(new Phrase(new Chunk("Tax ("+new_tax_struct_info+")",small_black_bold)));
	
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table22.getDefaultCell().setColspan(1);
	            Table22.addCell(new Phrase(new Chunk(curr3,small_black_bold)));
	            
	            Table22.setWidthPercentage(100);
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table22.getDefaultCell().setColspan(1);
	            Table22.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table22.getDefaultCell().setColspan(1);
	            Table22.addCell(new Phrase(new Chunk(es.formatAmount(tax_amt),small_black_bold)));
	            
	            sr+=1;
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
	            Table22.addCell(new Phrase(new Chunk("Total Amount in INR",small_black_bold)));
	
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
	            Table22.addCell(new Phrase(new Chunk(es.formatAmount(net_payable),small_black_bold)));
	            
	            float[] ContactAddrWidths26_1 = {0.100F};
	   	     	PdfPTable Table24_1 = new PdfPTable(ContactAddrWidths26_1);
	   	     	Table24_1.setWidthPercentage(100);
	   	     	Table24_1.getDefaultCell().setBorder(Rectangle.BOX);
	   	     	Table24_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	   	     	Table24_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	   	     	Table24_1.addCell(new Phrase(new Chunk("Amount in Words | Rupees "+amt_in_word+" Only",black_bold)));
	   	     	
	   	     	float[] ContactAddrWidths26_2 = {0.100F};
	   	     	PdfPTable Table24_2 = new PdfPTable(ContactAddrWidths26_2);
	   	     	Table24_2.setWidthPercentage(100);
	   	     	Table24_2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	   	     	Table24_2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	   	     	Table24_2.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	   	     	Table24_2.addCell(new Phrase(new Chunk("Reason for "+cr_dr_type_nm+" : "+reason,small_black_normal)));
	            
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
				document.add(new Paragraph("  "));
				document.add(Table14);
				document.add(Table15);
				document.add(Table16);
				document.add(new Paragraph("  "));
				document.add(Table17);
				document.add(Table18);
				document.add(Table26);
				document.add(Table19);
				document.add(Table22);
				document.add(new Paragraph("  "));
				document.add(Table24_1);
				document.add(new Paragraph("  "));
				document.add(Table24_2);
				document.add(new Paragraph("  "));
				document.add(finalRow);
				if(!remark1.equals(""))
				{
					document.add(Table28);
					document.add(Table29);
					document.add(Table30);
					document.add(Table31);
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
				        	msg = "Failed! - PDF "+file_nm+" for PFA Fees CR/DR "+cr_dr_type_nm+" with Invoice No. "+inv_no+" Generation Failed!";
				        	msg_type="E";
				        }
				        else
				        {
				        	conn.commit();
				        	msg = "Successful! - PDF "+file_nm+" for PFA Fees CR/DR "+cr_dr_type_nm+" with Invoice No. "+inv_no+" Generated Successfully!";
				        	msg_type="S";
				        }
					}
					else
					{
						msg="Failed! - PDF for PFA Fees CR/DR "+cr_dr_type_nm+" with Invoice No. "+inv_no+" Generation Failed!";
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
			
			msg = "Error in Exception! - PDF for PFA Fees CR/DR with Invoice No. "+inv_no+" Generation Failed!";
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
	
	public void pdfCRDRCOSTRH() throws SQLException
	{
		String function_nm="pdfCRDRCOSTRH()";
		
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
			
			String cr_dr_type_nm="";
			if(cr_dr_type.equals("CR"))
			{
				cr_dr_type_nm="Credit Note";
			}
			else if(cr_dr_type.equals("DR"))
			{
				cr_dr_type_nm="Debit Note";
			}
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
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.INVOICE_TYPE = B.INVOICE_TYPE AND A.INV_FLAG =?";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_no);
				stmt.setString(3, inv_type);
				stmt.setString(4, print_pdf_type);
				stmt.setString(5, inv_type);
				stmt.setString(6, cr_dr_type);
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
				msg="Failed! - PDF for Cost Recharge HPPL "+cr_dr_type_nm+" with Invoice No. "+inv_no+" already Generated!";
	        	msg_type="E";
	        }
			else
			{
				
				getCostRHInvoiceDtl();
				getCostRHCrDrInvoiceDtl();
				getCostRHCrDrRefDetail();
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
			        file_nm=print_pdf_type+"-"+cr_dr_type+"-"+supp_abbr+"-"+inv_type+"-"+inv_no.replace("/", "-")+".pdf";
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
	            centerTable.addCell(new Phrase(new Chunk(cr_dr_type_nm, small_black_normal_12)));
	            String info = cr_dr_type_nm+" issued under Rule 46 of the Central Goods and Services Tax Rules, 2017";
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
	            Table14.addCell(new Phrase(new Chunk(cr_dr_type_nm+" No :",black_bold)));
	
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
	            Table15.addCell(new Phrase(new Chunk(cr_dr_type_nm+" Date :",black_bold)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk(crdr_invoice_dt,black_bold)));

	            float[] ContactAddrWidths18 = {0.80F,0.20F,0.50F,0.40F};
	   	     	PdfPTable Table16 = new PdfPTable(ContactAddrWidths18);
	            Table16.setWidthPercentage(100);
	            Table16.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table16.addCell(new Phrase(new Chunk("",black_bold)));
	            
	            Table16.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk("",black_bold)));
	
	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table16.addCell(new Phrase(new Chunk("Payament Due Date :",black_bold)));
	
	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk(crdr_invoice_due_dt,black_bold)));

	            float[] ContactAddrWidths19 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table17 = new PdfPTable(ContactAddrWidths19);
	            Table17.setWidthPercentage(100);
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table17.addCell(new Phrase(new Chunk("Sr.No.",small_black_bold)));
	
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table17.addCell(new Phrase(new Chunk("Description",small_black_bold)));
	
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table17.addCell(new Phrase(new Chunk("Currency",small_black_bold)));
	            
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table17.addCell(new Phrase(new Chunk("Amount",small_black_bold)));
	                     
	            String curr1 = "INR";
	            String dt = dateUtil.getDateFormatDD_MOM_YY(main_invoice_dt);
	            String desc_item=cr_dr_type_nm+" against Invoice No. "+sel_inv_no+" dated "+dt ;
				
	            int sr=1;
	            
	            float[] ContactAddrWidths20 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table18 = new PdfPTable(ContactAddrWidths20);
	            Table18.setWidthPercentage(100);
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table18.addCell(new Phrase(new Chunk(desc_item,small_black_normal)));
	
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table18.addCell(new Phrase(new Chunk("",small_black_bold)));
	            
	            sr+=1;
	            Table18.setWidthPercentage(100);
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table18.addCell(new Phrase(new Chunk("Gross Amount",small_black_bold)));
	
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk(curr1,small_black_bold)));
	            
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            if(criteri_formula.contains("GAMT"))
	            {
	            	Table18.addCell(new Phrase(new Chunk(es.formatAmount(gross_amt),small_black_bold)));
	            }
	            else 
	            {
	            	Table18.addCell(new Phrase(new Chunk("",small_black_bold)));
	            }
	            	
	            sr+=1;
	            float[] ContactAddrWidths24 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table22 = new PdfPTable(ContactAddrWidths24);
	            
	            if (new_tax_struct_info.contains(",")) 
				{
	            	BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_amt));
					BigDecimal factor = new BigDecimal(2);
	            	double temp_srno=sr;
	            	for(int i = 0;i<new_tax_struct_info.split(", ").length;i++) 
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
			            Table22.addCell(new Phrase(new Chunk(new_tax_struct_info.split(",")[i],small_black_normal)));
			
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
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table22.addCell(new Phrase(new Chunk("Tax ("+new_tax_struct_info+")",small_black_bold)));
	
	            Table22.getDefaultCell().setBorder(Rectangle.BOX);
	            Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table22.addCell(new Phrase(new Chunk(curr1,small_black_bold)));
	            
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
	            Table23.addCell(new Phrase(new Chunk(curr1,small_black_bold)));
	            
	            Table23.getDefaultCell().setBorder(Rectangle.BOX);
	            Table23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table23.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table23.addCell(new Phrase(new Chunk(es.formatAmount(net_payable),small_black_bold)));
	            
	            float[] ContactAddrWidths26_1 = {0.100F};
	   	     	PdfPTable Table24_1 = new PdfPTable(ContactAddrWidths26_1);
	   	     	Table24_1.setWidthPercentage(100);
	   	     	Table24_1.getDefaultCell().setBorder(Rectangle.BOX);
	   	     	Table24_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	   	     	Table24_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	   	     	Table24_1.addCell(new Phrase(new Chunk("Amount in Words | Rupees "+amt_in_word+" Only",black_bold)));
	   	     	
	   	     	float[] ContactAddrWidths26_2 = {0.100F};
	   	     	PdfPTable Table24_2 = new PdfPTable(ContactAddrWidths26_2);
	   	     	Table24_2.setWidthPercentage(100);
	   	     	Table24_2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	   	     	Table24_2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	   	     	Table24_2.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	   	     	Table24_2.addCell(new Phrase(new Chunk("Reason for "+cr_dr_type_nm+" : "+reason,small_black_normal)));
	            
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
				document.add(new Paragraph("  "));
				document.add(Table17);
				document.add(Table18);
				document.add(Table22);
				document.add(Table23);
				document.add(new Paragraph("  "));
				document.add(Table24_1);
				document.add(new Paragraph("  "));
				document.add(Table24_2);
				document.add(new Paragraph("  "));
				document.add(finalRow);
				if(!remark1.equals(""))
				{
					document.add(Table28);
					document.add(Table29);
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
				        stmt1.setString(4, financial_year);
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
				 	        stmt1.setString(6, financial_year);
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
				 	        stmt1.setString(5, financial_year);
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
				        	msg = "Failed! - PDF "+file_nm+" for Cost Recharge HPPL CR/DR "+cr_dr_type_nm+" with Invoice No. "+inv_no+" Generation Failed!";
				        	msg_type="E";
				        }
				        else
				        {
				        	conn.commit();
				        	msg = "Successful! - PDF "+file_nm+" for Cost Recharge HPPL CR/DR "+cr_dr_type_nm+" with Invoice No. "+inv_no+" Generated Successfully!";
				        	msg_type="S";
				        }
					}
					else
					{
						msg="Failed! - PDF for Cost Recharge HPPL CR/DR "+cr_dr_type_nm+" with Invoice No. "+inv_no+" Generation Failed!";
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
			
			msg = "Error in Exception! - PDF for Cost Recharge HPPL CR/DR with Invoice No. "+inv_no+" Generation Failed!";
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
					+ "APPROVED_FLAG,CHECKED_FLAG,INVOICE_NO,INVOICE_ID_SEQ,INVOICE_SEQ "
					+ "FROM FMS_OTH_INVOICE_MST ";
			if(is_print.equals("0"))
			{
				queryString	+= "WHERE COMPANY_CD = ? AND INVOICE_SEQ = ? "
						+ "AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG = ? ";
			}
			else 
			{
				queryString	+= "WHERE COMPANY_CD = ? AND INVOICE_TYPE=?  "
						+ "AND INVOICE_NO=? AND INV_FLAG IN ('CR','DR')";
			}				
			stmt = conn.prepareStatement(queryString);
			
			if(is_print.equals("0"))
			{
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_seq);
				stmt.setString(3, inv_type);
				stmt.setString(4, fin_yr);
				stmt.setString(5, supp_cd);
				stmt.setString(6, cr_dr_type);
			}
			else 
			{
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_type);
				stmt.setString(3, inv_no);
			}
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
				inv_seq=rset.getString(19)==null?"":rset.getString(19);
				
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
				
				BigDecimal grossAmt1 = BigDecimal.valueOf(grossAmt);
				BigDecimal sub_grossAmt1 = grossAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal taxAmt1 = BigDecimal.valueOf(taxAmt);
				BigDecimal sub_taxAmt1 = taxAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal netPayable1 = BigDecimal.valueOf(netPayable);
				BigDecimal sub_netPayable1 = netPayable1.divide(factor, 0, RoundingMode.HALF_UP);
				
				gross_amt=nf.format(sub_grossAmt1);				
				tax_amt=nf.format(sub_taxAmt1);				
				net_payable=nf.format(sub_netPayable1);				
				
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
				String dt = dateUtil.getDateFormatDD_MOM_YY(main_invoice_dt);
				type = cr_dr_type.equals("CR") ? "Credit Note":"Debit Note";
				
				String desc_item= type+" against Invoice No : "+sel_inv_no+" dated "+dt;
				
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
	
	
	public void pdfCRDRCOSTR() throws SQLException
	{
		String function_nm="pdfCRDRCOSTR()";
		
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
			
			String cr_dr_type_nm="";
			if(cr_dr_type.equals("CR"))
			{
				cr_dr_type_nm="Credit Note";
			}
			else if(cr_dr_type.equals("DR"))
			{
				cr_dr_type_nm="Debit Note";
			}
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
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.INVOICE_TYPE = B.INVOICE_TYPE AND A.INV_FLAG =?";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_no);
				stmt.setString(3, inv_type);
				stmt.setString(4, print_pdf_type);
				stmt.setString(5, inv_type);
				stmt.setString(6, cr_dr_type);
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
				msg="Failed! - PDF for RCM Invoice "+cr_dr_type_nm+" with Invoice No. "+inv_no+" already Generated!";
	        	msg_type="E";
	        }
			else
			{
				getCostRInvoiceDtl();
				getCostRCrDrInvoiceDtl();
				getCostRCrDrRefDetail();
				getSupplierDtl();
				getVendorDtl();
				
				String irn_no="";
				String qr_code="";
				
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
			        file_nm=print_pdf_type+"-"+cr_dr_type+"-"+supp_abbr+"-"+inv_type+"-"+inv_no.replace("/", "-")+".pdf";
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
	            centerTable.addCell(new Phrase(new Chunk(cr_dr_type_nm, small_black_normal_12)));
	            String info = cr_dr_type_nm+" issued under Rule 46 of the Central Goods and Services Tax Rules, 2017";
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
	            Table14.addCell(new Phrase(new Chunk(cr_dr_type_nm+" No :",black_bold)));
	
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
	            Table15.addCell(new Phrase(new Chunk(cr_dr_type_nm+" Date :",black_bold)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk(crdr_invoice_dt,black_bold)));

	            float[] ContactAddrWidths18 = {0.80F,0.20F,0.50F,0.40F};
	   	     	PdfPTable Table16 = new PdfPTable(ContactAddrWidths18);
	            Table16.setWidthPercentage(100);
	            Table16.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table16.addCell(new Phrase(new Chunk("",black_bold)));
	            
	            Table16.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk("",black_bold)));
	
	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table16.addCell(new Phrase(new Chunk("Payament Due Date :",black_bold)));
	
	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk(crdr_invoice_due_dt,black_bold)));

	            float[] ContactAddrWidths19 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table17 = new PdfPTable(ContactAddrWidths19);
	            Table17.setWidthPercentage(100);
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table17.addCell(new Phrase(new Chunk("Sr.No.",small_black_bold)));
	
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table17.addCell(new Phrase(new Chunk("Description",small_black_bold)));
	
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table17.addCell(new Phrase(new Chunk("Currency",small_black_bold)));
	            
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table17.addCell(new Phrase(new Chunk("Amount",small_black_bold)));
	                     
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
	            String dt = dateUtil.getDateFormatDD_MOM_YY(main_invoice_dt);
	            String desc_item=cr_dr_type_nm+" against Invoice No. "+sel_inv_no+" dated "+dt ;
				
	            int sr=1;
	            
	            float[] ContactAddrWidths20 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table18 = new PdfPTable(ContactAddrWidths20);
	            Table18.setWidthPercentage(100);
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
	
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table18.addCell(new Phrase(new Chunk(desc_item,small_black_normal)));
	
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk("",small_black_normal)));
	            
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table18.addCell(new Phrase(new Chunk("",small_black_bold)));
	            
	            
	            float[] ContactAddrWidths21 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table19 = new PdfPTable(ContactAddrWidths21);
	   	     	if(!price_cd.equals("1"))
	   	     	{
		   	     	sr+=1;
		            Table18.setWidthPercentage(100);
		            Table18.getDefaultCell().setBorder(Rectangle.BOX);
		            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table18.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
		
		            Table18.getDefaultCell().setBorder(Rectangle.BOX);
		            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            Table18.addCell(new Phrase(new Chunk("Gross Amount",small_black_bold)));
		
		            Table18.getDefaultCell().setBorder(Rectangle.BOX);
		            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table18.addCell(new Phrase(new Chunk(curr1,small_black_bold)));
		            
		            Table18.getDefaultCell().setBorder(Rectangle.BOX);
		            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		   	     	if(criteri_formula.contains("GAMT"))
		   	     	{
		              Table18.addCell(new Phrase(new Chunk(es.formatAmount(gross_amt),small_black_bold)));
		   	     	}
		   	     	else if(criteri_formula.contains("EXCHG"))
		   	     	{
			            Table18.addCell(new Phrase(new Chunk(es.formatAmount(new_gross_amt),small_black_bold)));	
		   	     	}
		   	     	else 
		   	     	{
		   	        	Table18.addCell(new Phrase(new Chunk("",small_black_bold)));	
		   	     	}
		            sr += 1;
		            Table19.setWidthPercentage(100);
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table19.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
		
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            Table19.addCell(new Phrase(new Chunk("Exchange Rate",small_black_normal)));
		
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table19.addCell(new Phrase(new Chunk(curr2,small_black_normal)));
		            
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table19.addCell(new Phrase(new Chunk(main_exchang_rate,small_black_normal)));
		            
		            
		            if(criteri_formula.contains("EXCHG"))
		            {
		            	sr += 1;
			            Table19.setWidthPercentage(100);
			            Table19.getDefaultCell().setBorder(Rectangle.BOX);
			            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table19.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
			
			            Table19.getDefaultCell().setBorder(Rectangle.BOX);
			            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
			            Table19.addCell(new Phrase(new Chunk("Applicable Exchange Rate",small_black_normal)));
			
			            Table19.getDefaultCell().setBorder(Rectangle.BOX);
			            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table19.addCell(new Phrase(new Chunk(curr2,small_black_normal)));
			            
			            Table19.getDefaultCell().setBorder(Rectangle.BOX);
			            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            Table19.addCell(new Phrase(new Chunk(new_exchang_rate,small_black_normal)));
			
			            sr += 1;
			            Table19.setWidthPercentage(100);
			            Table19.getDefaultCell().setBorder(Rectangle.BOX);
			            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table19.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
			
			            Table19.getDefaultCell().setBorder(Rectangle.BOX);
			            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
			            Table19.addCell(new Phrase(new Chunk("Difference in Exchange Rate",small_black_normal)));
			
			            Table19.getDefaultCell().setBorder(Rectangle.BOX);
			            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table19.addCell(new Phrase(new Chunk(curr2,small_black_normal)));
			            
			            Table19.getDefaultCell().setBorder(Rectangle.BOX);
			            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            Table19.getDefaultCell().setColspan(1);
			            Table19.addCell(new Phrase(new Chunk(exchng_rate,small_black_normal)));	
		            }
		            sr += 1;
		            Table19.setWidthPercentage(100);
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table19.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
		
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            Table19.addCell(new Phrase(new Chunk("Gross Amount",small_black_bold)));
		
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table19.addCell(new Phrase(new Chunk(curr3,small_black_bold)));
		            
		            Table19.getDefaultCell().setBorder(Rectangle.BOX);
		            Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT); 
			        Table19.addCell(new Phrase(new Chunk(es.formatAmount(gross_amt1),small_black_bold)));
	   	     	}
	   	     	else
	   	     	{
		   	     	sr+=1;
		            Table18.setWidthPercentage(100);
		            Table18.getDefaultCell().setBorder(Rectangle.BOX);
		            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table18.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
		
		            Table18.getDefaultCell().setBorder(Rectangle.BOX);
		            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            Table18.addCell(new Phrase(new Chunk("Gross Amount",small_black_bold)));
		
		            Table18.getDefaultCell().setBorder(Rectangle.BOX);
		            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table18.addCell(new Phrase(new Chunk(curr1,small_black_bold)));
		            
		            Table18.getDefaultCell().setBorder(Rectangle.BOX);
		            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		   	     	if(criteri_formula.contains("GAMT"))
		   	     	{
		   	     		Table18.addCell(new Phrase(new Chunk(es.formatAmount(gross_amt),small_black_bold)));
		   	     	}
		   	     	else
		   	     	{
			            Table18.addCell(new Phrase(new Chunk("",small_black_bold)));	
		   	     	}
	   	     	}
	            
	            sr+=1;
	            float[] ContactAddrWidths24 = {0.08F,0.70F,0.20F,0.20F};
	   	     	PdfPTable Table22 = new PdfPTable(ContactAddrWidths24);
	            
	            if (new_tax_struct_info.contains(",")) 
				{
	            	BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_amt));
					BigDecimal factor = new BigDecimal(2);
	            	double temp_srno=sr;
	            	for(int i = 0;i<new_tax_struct_info.split(", ").length;i++) 
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
			            Table22.addCell(new Phrase(new Chunk(new_tax_struct_info.split(",")[i],small_black_normal)));
			
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
	            Table22.addCell(new Phrase(new Chunk("Tax ("+new_tax_struct_info+")",small_black_bold)));
	
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
	            Table23.addCell(new Phrase(new Chunk(es.formatAmount(net_payable),small_black_bold)));
	            
	            float[] ContactAddrWidths26_1 = {0.100F};
	   	     	PdfPTable Table24_1 = new PdfPTable(ContactAddrWidths26_1);
	   	     	Table24_1.setWidthPercentage(100);
	   	     	Table24_1.getDefaultCell().setBorder(Rectangle.BOX);
	   	     	Table24_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	   	     	Table24_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	   	     	Table24_1.addCell(new Phrase(new Chunk("Amount in Words | Rupees "+amt_in_word+" Only",black_bold)));
	   	     	
	   	     	float[] ContactAddrWidths26_2 = {0.100F};
	   	     	PdfPTable Table24_2 = new PdfPTable(ContactAddrWidths26_2);
	   	     	Table24_2.setWidthPercentage(100);
	   	     	Table24_2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	   	     	Table24_2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	   	     	Table24_2.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	   	     	Table24_2.addCell(new Phrase(new Chunk("Reason for "+cr_dr_type_nm+" : "+reason,small_black_normal)));
	            
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
				document.add(new Paragraph("  "));
				document.add(Table17);
				document.add(Table18);
				document.add(Table19);
				document.add(Table22);
				document.add(Table23);
				document.add(new Paragraph("  "));
				document.add(Table24_1);
				document.add(new Paragraph("  "));
				document.add(Table24_2);
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
				        stmt1.setString(4, financial_year);
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
				 	        stmt1.setString(6, financial_year);
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
				 	        stmt1.setString(5, financial_year);
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
				        	msg = "Failed! - PDF "+file_nm+" for RCM Invoice(Cost recharge) Share CR/DR "+cr_dr_type_nm+" with Invoice No. "+inv_no+" Generation Failed!";
				        	msg_type="E";
				        }
				        else
				        {
				        	conn.commit();
				        	msg = "Successful! - PDF "+file_nm+" for RCM Invoice(Cost recharge) Share CR/DR "+cr_dr_type_nm+" with Invoice No. "+inv_no+" Generated Successfully!";
				        	msg_type="S";
				        }
					}
					else
					{
						msg="Failed! - PDF for RCM Invoice(Cost recharge) Share CR/DR "+cr_dr_type_nm+" with Invoice No. "+inv_no+" Generation Failed!";
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
			
			msg = "Error in Exception! - PDF for RCM Invoice(Cost recharge) CR/DR with Invoice No. "+inv_no+" Generation Failed!";
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
					+ "APPROVED_FLAG,CHECKED_FLAG,INVOICE_NO,INVOICE_ID_SEQ,EXCHG_RATE_VALUE,SALE_AMT,INVOICE_SEQ "
					+ "FROM FMS_OTH_INVOICE_MST ";
//					+ "WHERE COMPANY_CD = ? AND INVOICE_TYPE=? AND FINANCIAL_YEAR=? "
//					+ "AND INVOICE_SEQ=? "
//					+ "AND INV_FLAG IN ('CR','DR') ";
//			stmt = conn.prepareStatement(queryString);
//			stmt.setString(1, comp_cd);
//			stmt.setString(2, inv_type);
//			stmt.setString(3, fin_yr);
//			stmt.setString(4, inv_seq);
			if(is_print.equals("0"))
			{
				queryString	+= "WHERE COMPANY_CD = ? AND INVOICE_SEQ = ? "
						+ "AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG = ? ";
			}
			else 
			{
				queryString	+= "WHERE COMPANY_CD = ? AND INVOICE_TYPE=?  "
						+ "AND INVOICE_NO=? AND INV_FLAG IN ('CR','DR')";
			}				
			stmt = conn.prepareStatement(queryString);
			
			if(is_print.equals("0"))
			{
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_seq);
				stmt.setString(3, inv_type);
				stmt.setString(4, fin_yr);
				stmt.setString(5, supp_cd);
				stmt.setString(6, cr_dr_type);
			}
			else 
			{
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_type);
				stmt.setString(3, inv_no);
			}
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
				inv_seq=rset.getString(21)==null?"":rset.getString(21);
				
//				if(callFlag.equalsIgnoreCase("COSTR_CRDR_APROVAL"))
//				{
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
//				}
				
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
	
	
	public void pdfCRDRSFA() throws SQLException
	{
		String function_nm="pdfCRDRSFA()";
		
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
			
			String cr_dr_type_nm="";
			if(cr_dr_type.equals("CR"))
			{
				cr_dr_type_nm="Credit Note";
			}
			else if(cr_dr_type.equals("DR"))
			{
				cr_dr_type_nm="Debit Note";
			}
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
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.INVOICE_SEQ=B.INVOICE_SEQ AND A.INVOICE_TYPE = B.INVOICE_TYPE AND A.INV_FLAG =?";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_no);
				stmt.setString(3, inv_type);
				stmt.setString(4, print_pdf_type);
				stmt.setString(5, inv_type);
				stmt.setString(6, cr_dr_type);
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
				msg="Failed! - PDF for Scrap Fixed Asset "+cr_dr_type_nm+" with Invoice No. "+inv_no+" already Generated!";
	        	msg_type="E";
	        }
			else
			{
				
				getSFAInvoiceDtl();
				getSupplierDtl();
				getVendorDtl();
				getSFARCrDrInvoiceDtl();
				getSFARCrDrItemDtl();
				getSFACrDrRefItemDetail();
				getSFACrDrRefDetail();
				
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
			        file_nm=print_pdf_type+"-"+cr_dr_type+"-"+supp_abbr+"-"+inv_type+"-"+inv_no.replace("/", "-")+".pdf";
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
	            centerTable.addCell(new Phrase(new Chunk(cr_dr_type_nm, small_black_normal_12)));
	            String info = cr_dr_type_nm+" issued under Rule 46 of the Central Goods and Services Tax Rules, 2017";
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
	            Table14.addCell(new Phrase(new Chunk(cr_dr_type_nm+" No :",black_bold)));
	
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
	            Table15.addCell(new Phrase(new Chunk(cr_dr_type_nm+" Date :",black_bold)));
	
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk(crdr_invoice_dt,black_bold)));

	            float[] ContactAddrWidths18 = {0.80F,0.20F,0.50F,0.40F};
	   	     	PdfPTable Table16 = new PdfPTable(ContactAddrWidths18);
	            Table16.setWidthPercentage(100);
	            Table16.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table16.addCell(new Phrase(new Chunk("",black_bold)));
	            
	            Table16.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk("",black_bold)));
	
	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table16.addCell(new Phrase(new Chunk("Payament Due Date :",black_bold)));
	
	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk(crdr_invoice_due_dt,black_bold)));

	            
	            if(invoice_category.equals("P"))
	            {
	            	float[] ContactAddrWidths19 = {0.12F,0.20F,0.70F,0.15F,0.17F,0.20F,0.17F,0.17F,0.20F};
		   	     	PdfPTable Table17 = new PdfPTable(ContactAddrWidths19);
		   	     	Table17.setWidthPercentage(100);
		            Table17.getDefaultCell().setBorder(Rectangle.BOX);
		            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table17.addCell(new Phrase(new Chunk("Sr.No.",small_black_bold)));
		            
		            Table17.getDefaultCell().setBorder(Rectangle.BOX);
		            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table17.addCell(new Phrase(new Chunk("HSN/SAC Code",small_black_bold)));
		
		            Table17.getDefaultCell().setBorder(Rectangle.BOX);
		            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table17.addCell(new Phrase(new Chunk("Description",small_black_bold)));
		            
		            Table17.getDefaultCell().setBorder(Rectangle.BOX);
		            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table17.addCell(new Phrase(new Chunk("UOM",small_black_bold)));
		            
		            Table17.getDefaultCell().setBorder(Rectangle.BOX);
		            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table17.addCell(new Phrase(new Chunk("Quantity",small_black_bold)));
		            
		            Table17.getDefaultCell().setBorder(Rectangle.BOX);
		            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table17.addCell(new Phrase(new Chunk("Rate per Qty",small_black_bold)));
		            
		            Table17.getDefaultCell().setBorder(Rectangle.BOX);
		            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table17.addCell(new Phrase(new Chunk("Tax Rate(%)",small_black_bold)));
		
		            Table17.getDefaultCell().setBorder(Rectangle.BOX);
		            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table17.addCell(new Phrase(new Chunk("Currency",small_black_bold)));
		
		            Table17.getDefaultCell().setBorder(Rectangle.BOX);
		            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table17.addCell(new Phrase(new Chunk("Amount",small_black_bold)));
		                     
		            String curr1 = "INR";
		            int sr=0;
		            
		            float[] ContactAddrWidths20 = {0.12F,0.20F,0.70F,0.15F,0.17F,0.20F,0.17F,0.17F,0.20F};
		   	     	PdfPTable Table18 = new PdfPTable(ContactAddrWidths20);
		   	     	
		   	     	for(int i = 0;i<VQTY.size();i++)
		   	     	{
		   	     		sr+=1;
			   	     	Table18.setWidthPercentage(100);
						Table18.getDefaultCell().setBorder(Rectangle.BOX);
						Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						Table18.addCell(new Phrase(new Chunk("" + sr, small_black_normal)));
						
						Table18.getDefaultCell().setBorder(Rectangle.BOX);
						Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						Table18.addCell(new Phrase(new Chunk(""+VSAC_VAL.elementAt(i), small_black_normal)));
						
						Table18.getDefaultCell().setBorder(Rectangle.BOX);
						Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
						Table18.addCell(new Phrase(new Chunk(""+VITEM_DES.elementAt(i), small_black_normal)));
						
						Table18.getDefaultCell().setBorder(Rectangle.BOX);
						Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						Table18.addCell(new Phrase(new Chunk(""+VUOM_NO.elementAt(i), small_black_normal)));
						
						Table18.getDefaultCell().setBorder(Rectangle.BOX);
						Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
						if(criteri_formula.contains("QTY"))
						{
							Table18.addCell(new Phrase(new Chunk(nf.format(Math.abs(Double.parseDouble(""+VQTY.elementAt(i)))), small_black_normal)));
						}
						else
						{
							Table18.addCell(new Phrase(new Chunk(""+VNEW_QTY.elementAt(i), small_black_normal)));
						}
						
						Table18.getDefaultCell().setBorder(Rectangle.BOX);
						Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
						if(criteri_formula.contains("RATE"))
						{
							Table18.addCell(new Phrase(new Chunk(nf.format(Math.abs(Double.parseDouble(""+VPRICE.elementAt(i)))), small_black_normal)));
						}
						else
						{
							Table18.addCell(new Phrase(new Chunk(""+VNEW_PRICE.elementAt(i), small_black_normal)));
						}
						
						Table18.getDefaultCell().setBorder(Rectangle.BOX);
						Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
						Table18.addCell(new Phrase(new Chunk(VTAX_STRUCTURE_DESC.elementAt(i).toString().replace(","," ").split(" ")[1], small_black_normal)));
						
						Table18.getDefaultCell().setBorder(Rectangle.BOX);
						Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						Table18.addCell(new Phrase(new Chunk(curr1, small_black_normal)));
						
						Table18.getDefaultCell().setBorder(Rectangle.BOX);
						Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
						Table18.addCell(new Phrase(new Chunk(es.formatAmount(""+Math.abs(Double.parseDouble(""+VITEM_AMT.elementAt(i)))), small_black_normal)));
		   	     		
		   	     	}
		   	     	
		   	     	sr+=1;
		   	     	Table18.setWidthPercentage(100);
					Table18.getDefaultCell().setBorder(Rectangle.BOX);
					Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
					Table18.addCell(new Phrase(new Chunk("" + sr, small_black_normal)));
					
					Table18.getDefaultCell().setBorder(Rectangle.BOX);
					Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
					Table18.addCell(new Phrase(new Chunk("", small_black_normal)));
					
					Table18.getDefaultCell().setBorder(Rectangle.BOX);
					Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
					Table18.addCell(new Phrase(new Chunk("Gross Amount", small_black_bold)));
					
					Table18.getDefaultCell().setBorder(Rectangle.BOX);
					Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
					Table18.addCell(new Phrase(new Chunk("", small_black_normal)));
					
					Table18.getDefaultCell().setBorder(Rectangle.BOX);
					Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
					Table18.addCell(new Phrase(new Chunk("", small_black_normal)));
					
					Table18.getDefaultCell().setBorder(Rectangle.BOX);
					Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
					Table18.addCell(new Phrase(new Chunk("", small_black_normal)));
					
					Table18.getDefaultCell().setBorder(Rectangle.BOX);
					Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
					Table18.addCell(new Phrase(new Chunk("", small_black_normal)));
					
					Table18.getDefaultCell().setBorder(Rectangle.BOX);
					Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
					Table18.addCell(new Phrase(new Chunk(curr1, small_black_bold)));
					
					Table18.getDefaultCell().setBorder(Rectangle.BOX);
					Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
					Table18.addCell(new Phrase(new Chunk(es.formatAmount(gross_amt), small_black_bold)));
		            	
		            sr+=1;
		            float[] ContactAddrWidths24 = {0.12F,0.20F,0.70F,0.15F,0.17F,0.20F,0.17F,0.17F,0.20F};
		   	     	PdfPTable Table22 = new PdfPTable(ContactAddrWidths24);
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
		            Table23.addCell(new Phrase(new Chunk(es.formatAmount(net_payable),small_black_bold)));
		            
		            float[] ContactAddrWidths26_1 = {0.100F};
		   	     	PdfPTable Table24_1 = new PdfPTable(ContactAddrWidths26_1);
		   	     	Table24_1.setWidthPercentage(100);
		   	     	Table24_1.getDefaultCell().setBorder(Rectangle.BOX);
		   	     	Table24_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		   	     	Table24_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		   	     	Table24_1.addCell(new Phrase(new Chunk("Amount in Words | Rupees "+amt_in_word+" Only",black_bold)));
		   	     	
		   	     	float[] ContactAddrWidths26_2 = {0.100F};
		   	     	PdfPTable Table24_2 = new PdfPTable(ContactAddrWidths26_2);
		   	     	Table24_2.setWidthPercentage(100);
		   	     	Table24_2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		   	     	Table24_2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		   	     	Table24_2.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		   	     	Table24_2.addCell(new Phrase(new Chunk("Reason for "+cr_dr_type_nm+" : "+reason,small_black_normal)));
		            
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
					document.add(Table16);
					document.add(new Paragraph("  "));
					document.add(Table17);
					document.add(Table18);
					document.add(Table22);
					document.add(Table23);
					document.add(new Paragraph("  "));
					document.add(Table24_1);
					document.add(new Paragraph("  "));
					document.add(Table24_2);
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
	            	float[] ContactAddrWidths19 = {0.12F,0.20F,0.70F,0.15F,0.17F,0.23F,0.17F,0.20F};
		   	     	PdfPTable Table17 = new PdfPTable(ContactAddrWidths19);
		   	     	Table17.setWidthPercentage(100);
		            Table17.getDefaultCell().setBorder(Rectangle.BOX);
		            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table17.addCell(new Phrase(new Chunk("Sr.No.",small_black_bold)));
		            
		            Table17.getDefaultCell().setBorder(Rectangle.BOX);
		            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table17.addCell(new Phrase(new Chunk("HSN/SAC Code",small_black_bold)));
		
		            Table17.getDefaultCell().setBorder(Rectangle.BOX);
		            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table17.addCell(new Phrase(new Chunk("Description",small_black_bold)));
		            
		            Table17.getDefaultCell().setBorder(Rectangle.BOX);
		            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table17.addCell(new Phrase(new Chunk("UOM",small_black_bold)));
		            
		            Table17.getDefaultCell().setBorder(Rectangle.BOX);
		            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table17.addCell(new Phrase(new Chunk("Quantity",small_black_bold)));
		            
		            Table17.getDefaultCell().setBorder(Rectangle.BOX);
		            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table17.addCell(new Phrase(new Chunk("Rate per Qty",small_black_bold)));
		            
		            Table17.getDefaultCell().setBorder(Rectangle.BOX);
		            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table17.addCell(new Phrase(new Chunk("Currency",small_black_bold)));
		
		            Table17.getDefaultCell().setBorder(Rectangle.BOX);
		            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table17.addCell(new Phrase(new Chunk("Amount",small_black_bold)));
		                     
		            String curr1 = "INR";
		            int sr=0;
		            
		            float[] ContactAddrWidths20 = {0.12F,0.20F,0.70F,0.15F,0.17F,0.23F,0.17F,0.20F};
		   	     	PdfPTable Table18 = new PdfPTable(ContactAddrWidths20);
		   	     	
		   	     	for(int i = 0;i<VQTY.size();i++)
		   	     	{
		   	     		sr+=1;
			   	     	Table18.setWidthPercentage(100);
						Table18.getDefaultCell().setBorder(Rectangle.BOX);
						Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						Table18.addCell(new Phrase(new Chunk("" + sr, small_black_normal)));
						
						Table18.getDefaultCell().setBorder(Rectangle.BOX);
						Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						Table18.addCell(new Phrase(new Chunk(""+VSAC_VAL.elementAt(i), small_black_normal)));
						
						Table18.getDefaultCell().setBorder(Rectangle.BOX);
						Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
						Table18.addCell(new Phrase(new Chunk(""+VITEM_DES.elementAt(i), small_black_normal)));
						
						Table18.getDefaultCell().setBorder(Rectangle.BOX);
						Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						Table18.addCell(new Phrase(new Chunk(""+VUOM_NO.elementAt(i), small_black_normal)));
						
						Table18.getDefaultCell().setBorder(Rectangle.BOX);
						Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
						if(criteri_formula.contains("QTY"))
						{
							Table18.addCell(new Phrase(new Chunk(nf.format(Math.abs(Double.parseDouble(""+VQTY.elementAt(i)))), small_black_normal)));
						}
						else
						{
							Table18.addCell(new Phrase(new Chunk(""+VNEW_QTY.elementAt(i), small_black_normal)));
						}
						
						Table18.getDefaultCell().setBorder(Rectangle.BOX);
						Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
						if(criteri_formula.contains("RATE"))
						{
							Table18.addCell(new Phrase(new Chunk(nf.format(Math.abs(Double.parseDouble(""+VPRICE.elementAt(i)))), small_black_normal)));
						}
						else
						{
							Table18.addCell(new Phrase(new Chunk(""+VNEW_PRICE.elementAt(i), small_black_normal)));
						}
						
						Table18.getDefaultCell().setBorder(Rectangle.BOX);
						Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
						Table18.addCell(new Phrase(new Chunk(curr1, small_black_normal)));
						
						Table18.getDefaultCell().setBorder(Rectangle.BOX);
						Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
						if(criteri_formula.contains("TAXP"))
						{
							Table18.addCell(new Phrase(new Chunk(""+VNEW_ITEM_AMT.elementAt(i), small_black_normal)));
						}
						else 
						{
							Table18.addCell(new Phrase(new Chunk(es.formatAmount(""+Math.abs(Double.parseDouble(""+VITEM_AMT.elementAt(i)))), small_black_normal)));
						}
		   	     	}
		   	     	
		   	     	sr+=1;
		   	     	Table18.setWidthPercentage(100);
					Table18.getDefaultCell().setBorder(Rectangle.BOX);
					Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
					Table18.addCell(new Phrase(new Chunk("" + sr, small_black_normal)));
					
					Table18.getDefaultCell().setBorder(Rectangle.BOX);
					Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
					Table18.addCell(new Phrase(new Chunk("", small_black_normal)));
					
					Table18.getDefaultCell().setBorder(Rectangle.BOX);
					Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
					Table18.addCell(new Phrase(new Chunk("Gross Amount", small_black_bold)));
					
					Table18.getDefaultCell().setBorder(Rectangle.BOX);
					Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
					Table18.addCell(new Phrase(new Chunk("", small_black_normal)));
					
					Table18.getDefaultCell().setBorder(Rectangle.BOX);
					Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
					Table18.addCell(new Phrase(new Chunk("", small_black_normal)));
					
					Table18.getDefaultCell().setBorder(Rectangle.BOX);
					Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
					Table18.addCell(new Phrase(new Chunk("", small_black_normal)));
					
					Table18.getDefaultCell().setBorder(Rectangle.BOX);
					Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
					Table18.addCell(new Phrase(new Chunk(curr1, small_black_bold)));
					
					Table18.getDefaultCell().setBorder(Rectangle.BOX);
					Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
					Table18.addCell(new Phrase(new Chunk(es.formatAmount(gross_amt), small_black_bold)));
		            	
		            sr+=1;
		            float[] ContactAddrWidths24 = {0.12F,0.20F,0.70F,0.15F,0.17F,0.23F,0.17F,0.20F};
		   	     	PdfPTable Table22 = new PdfPTable(ContactAddrWidths24);
		   	     	
		   	     	if (new_tax_struct_info.contains(",")) 
					{
						BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_amt));
						BigDecimal factor = new BigDecimal(2);
		            	double temp_srno=sr;
		            	for(int i = 0;i<new_tax_struct_info.split(", ").length;i++) 
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
				            Table22.addCell(new Phrase(new Chunk(new_tax_struct_info.split(",")[i],small_black_normal)));
				            
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
		            Table22.addCell(new Phrase(new Chunk("Tax ("+new_tax_struct_info+")",small_black_bold)));
		            
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
		            Table23.addCell(new Phrase(new Chunk(es.formatAmount(net_payable),small_black_bold)));
		            
		            float[] ContactAddrWidths26_1 = {0.100F};
		   	     	PdfPTable Table24_1 = new PdfPTable(ContactAddrWidths26_1);
		   	     	Table24_1.setWidthPercentage(100);
		   	     	Table24_1.getDefaultCell().setBorder(Rectangle.BOX);
		   	     	Table24_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		   	     	Table24_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		   	     	Table24_1.addCell(new Phrase(new Chunk("Amount in Words | Rupees "+amt_in_word+" Only",black_bold)));
		   	     	
		   	     	float[] ContactAddrWidths26_2 = {0.100F};
		   	     	PdfPTable Table24_2 = new PdfPTable(ContactAddrWidths26_2);
		   	     	Table24_2.setWidthPercentage(100);
		   	     	Table24_2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		   	     	Table24_2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		   	     	Table24_2.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		   	     	Table24_2.addCell(new Phrase(new Chunk("Reason for "+cr_dr_type_nm+" : "+reason,small_black_normal)));
		            
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
					document.add(Table16);
					document.add(new Paragraph("  "));
					document.add(Table17);
					document.add(Table18);
					document.add(Table22);
					document.add(Table23);
					document.add(new Paragraph("  "));
					document.add(Table24_1);
					document.add(new Paragraph("  "));
					document.add(Table24_2);
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
				        stmt1.setString(4, financial_year);
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
				 	        stmt1.setString(6, financial_year);
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
				 	        stmt1.setString(5, financial_year);
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
				        	msg = "Failed! - PDF "+file_nm+" for Scrap Fixed Asset "+cr_dr_type_nm+" with Invoice No. "+inv_no+" Generation Failed!";
				        	msg_type="E";
				        }
				        else
				        {
				        	conn.commit();
				        	msg = "Successful! - PDF "+file_nm+" for Scrap Fixed Asset "+cr_dr_type_nm+" with Invoice No. "+inv_no+" Generated Successfully!";
				        	msg_type="S";
				        }
					}
					else
					{
						msg="Failed! - PDF for Scrap Fixed Asset "+cr_dr_type_nm+" with Invoice No. "+inv_no+" Generation Failed!";
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
			
			msg = "Error in Exception! - PDF for Scrap Fixed Asset CR/DR with Invoice No. "+inv_no+" Generation Failed!";
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
					+ "APPROVED_FLAG,CHECKED_FLAG,INVOICE_NO,INVOICE_ID_SEQ,INVOICE_SEQ "
					+ "FROM FMS_OTH_INVOICE_MST ";
			if(is_print.equals("0"))
			{
				queryString	+= "WHERE COMPANY_CD = ? AND INVOICE_SEQ = ? "
						+ "AND INVOICE_TYPE = ? AND FINANCIAL_YEAR=? AND SUPPLIER_CD=? AND INV_FLAG = ? ";
			}
			else 
			{
				queryString	+= "WHERE COMPANY_CD = ? AND INVOICE_TYPE=?  "
						+ "AND INVOICE_NO=? AND INV_FLAG IN ('CR','DR')";
			}				
			stmt = conn.prepareStatement(queryString);
			if(is_print.equals("0"))
			{
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_seq);
				stmt.setString(3, inv_type);
				stmt.setString(4, fin_yr);
				stmt.setString(5, supp_cd);
				stmt.setString(6, cr_dr_type);
			}
			else 
			{
				stmt.setString(1, comp_cd);
				stmt.setString(2, inv_type);
				stmt.setString(3, inv_no);
			}
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
				inv_seq=rset.getString(19)==null?"":rset.getString(19);
				
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
				
				BigDecimal grossAmt1 = BigDecimal.valueOf(grossAmt);
				BigDecimal sub_grossAmt1 = grossAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal taxAmt1 = BigDecimal.valueOf(taxAmt);
				BigDecimal sub_taxAmt1 = taxAmt1.divide(factor, 0, RoundingMode.HALF_UP);
				
				BigDecimal netPayable1 = BigDecimal.valueOf(netPayable);
				BigDecimal sub_netPayable1 = netPayable1.divide(factor, 0, RoundingMode.HALF_UP);
				
				gross_amt=nf.format(sub_grossAmt1);				
				tax_amt=nf.format(sub_taxAmt1);				
				net_payable=nf.format(sub_netPayable1);				
				
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
				stmt1.setString(3, financial_year);
				stmt1.setString(4, inv_seq);
				stmt1.setString(5, ""+VITEM_DES.elementAt(i));
				rset1 = stmt1.executeQuery();
				if(rset1.next()) 
				{
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
				stmt1.setString(3, financial_year);
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

	String file_url = "";
	String file_nm = "";
	public String getFile_url() {return file_url;}
	public String getFile_nm() {return file_nm;}
	
	
	String form_id="";
	String form_nm="";
	String mod_cd="";
	String mod_nm="";
	String emp_cd="";
	String cr_dr_type="";
	String sel_inv_no="";
	String bu_unit="";
	
	public void setForm_id(String form_id) {this.form_id = form_id;}
	public void setForm_nm(String form_nm) {this.form_nm = form_nm;}
	public void setMod_cd(String mod_cd) {this.mod_cd = mod_cd;}
	public void setMod_nm(String mod_nm) {this.mod_nm = mod_nm;}
	public void setEmp_cd(String emp_cd) {this.emp_cd = emp_cd;}
	public void setCr_dr_type(String cr_dr_type) {this.cr_dr_type = cr_dr_type;}
	public void setSel_inv_no(String sel_inv_no) {this.sel_inv_no = sel_inv_no;}
	public void setIs_print(String is_print) {this.is_print = is_print;}
	public void setSupplier_Cd(String supp_cd) {this.supp_cd = supp_cd;}
	public void setInv_seq(String inv_seq) {this.inv_seq = inv_seq;}
	public void setFin_yr(String fin_yr) {this.fin_yr = fin_yr;}
	public void setBu_unit(String bu_unit) {this.bu_unit = bu_unit;}
	
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
	String new_net_payable = "";
	String new_grt = "";
	String new_berthing_hrs = "";
	String new_berthing_slots = "";
	String new_rate = "";
	String crdr_remark = "";
	String tax_struct_info = "";
	String main_tax_struct_dt = "";
	String reason = "";
	String gross_amt1 = "";
	String new_gross_amt1 = "";
	String new_exchang_rate = "";
	String main_exchang_rate = "";
	String main_gross_amt1 = "";
	String exchange_rate_cd = "";
	String exchange_rate = "";
	
	double taxValue = 0;
	
	public String getMsg() {return msg;}
	public String getMsg_type() {return msg_type;}
	public String getAll_pdf_print() {return all_pdf_print;}
	public String getExchang_rate() {return exchange_rate;}

	Vector VRATE =  new Vector();
	Vector VCARGO_AMT =  new Vector();
	Vector VCARGO_DT =  new Vector();
	Vector VQTY_MMBTU =  new Vector();
	Vector VSHIP_NAME = new Vector();
	Vector VMAIN_QTY_MMBTU = new Vector();
	Vector VMAIN_RATE = new Vector();
	Vector VMAIN_CARGO_AMT = new Vector();
	Vector VCARGO_TYPE = new Vector();
	Vector VCARGO_REF = new Vector();
	Vector VSHIP_CD = new Vector();
	Vector VNEW_QTY_MMBTU = new Vector();
	Vector VNEW_RATE = new Vector();
	Vector VNEW_CARGO_AMT = new Vector();
	Vector VNSHIP_NAME = new Vector();
	Vector VNQTY_MMBTU = new Vector();
	Vector VNCARGO_RATE = new Vector();
	Vector VNCARGO_DATE = new Vector();
	Vector VNCARGO_AMT = new Vector();
	
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
	Vector VSAC_VAL = new Vector();
	Vector VQTY =  new Vector();
	Vector VUOM_NO =  new Vector();
	Vector VPRICE =  new Vector();
	Vector VTAX_STRUCTURE_CD = new Vector();
	Vector VTAX_STRUCTURE_DESC = new Vector();
	Vector VTAX_STRUCT_APP_DT = new Vector();
	Vector VTAX_AMT = new Vector();
}
