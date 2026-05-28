package com.etrm.fms.util;

import java.awt.Color;
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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

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

/*import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfAnnotation;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.events.FieldPositioningEvents;*/

public class DataBean_Pdf_DRCR_Generation
{
	String db_src_file_name="DataBean_Pdf_DRCR_Generation.java";

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
	DB_AllocationUtil utilAlloc = new DB_AllocationUtil();

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
		    			conn.setAutoCommit(false);
		    			if(callFlag.equalsIgnoreCase("DRCR_PDF"))	//BHAUMIK20250903
		    			{
		    				printPdfFileFordrcr();	//BHAUMIK20250903
		    			}
		    			else if(callFlag.equalsIgnoreCase("PDF_DRCR_NOTE")) //Deep20250904
		    			{
		    				printPdfFileForDrCrNote(); //Deep20250904
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
		String function_nm="deletePdfFile()";
		try
		{
			File file = new File(pdfFile);
			if(file.exists())
			{
				boolean delete = file.delete();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			throw e;
		}

	}
	public void printPdfFileFordrcr() throws SQLException
	{
		String function_nm="printPdfFileFordrcr()";

		String emp_nm ="";
		String ip = "";
		msg="";
		String msg_content="";
		String path ="";

		Rectangle pageSize = new Rectangle(595, 842);
		Rectangle pageSize1 = new Rectangle(595, 842);
		pageSize.setBackgroundColor(BaseColor.WHITE);
		Document document = new Document(pageSize);
		try
		{
			HttpSession session = request.getSession();
			emp_nm = (String)session.getAttribute("emp_nm")==null?"":(String)session.getAttribute("emp_nm");
			ip = (String)session.getAttribute("ip")==null?"":(String)session.getAttribute("ip");

			String company_nm = ""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");
			String company_abbr = ""+session.getAttribute("comp_abbr")==null?"":""+session.getAttribute("comp_abbr");
			String counterparty_nm=""+utilBean.getCounterpartyName(conn,counterparty_cd);
			String counterparty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);

			String inv_lineitem="Invoice";
			String tax_lineitem="Tax";
			String gross_lineitem="Gross";
			String exchg_lineitem="Exchange Rate";
			if(inv_flag.equals("CP") || inv_flag.equals("CF"))
			{
				inv_lineitem="Custom Duty";
				tax_lineitem="Custom Duty";
				gross_lineitem="Invoice";
				exchg_lineitem="Custom Exchange Rate";
			}

			String invoice_type="System Generated";
			String invoice_abbr="SG";
			if(inv_type.equals("S"))
			{
				invoice_type="System Generated";
				invoice_abbr="SG";
			}
			else if(inv_type.equals("P"))
			{
				invoice_type="Party Generated";
				invoice_abbr="PG";
			}

			String tmpmsg="";

			if(contract_type.equals("N") && inv_flag.equals("P"))
			{
				tmpmsg="Cargo Provisional";
			}
			else if(contract_type.equals("N") && inv_flag.equals("PF"))
			{
				tmpmsg="Cargo Proforma";
			}
			else if(contract_type.equals("N") && inv_flag.equals("CP"))
			{
				tmpmsg="Provisional Custom Duty";
			}
			else if(contract_type.equals("N") && inv_flag.equals("CF"))
			{
				tmpmsg="Final Custom Duty";
			}
			else if(contract_type.equals("G") || contract_type.equals("P"))
			{
				if(inv_flag.equals("UG"))
				{
					tmpmsg="LTCORA SUG Purchase";
				}
				else
				{
					tmpmsg="LTCORA Purchase";
				}
			}
			else if(contract_type.equals("Y"))
			{
				tmpmsg="Surveyor Service";
			}
			else if(contract_type.equals("A"))
			{
				tmpmsg="Vessel Agent Service";
			}
			else if(contract_type.equals("H"))
			{
				if (inv_flag.equals("P"))
				{
					tmpmsg="Custom House Agent Provisional Service";
				}
				else
				{
					tmpmsg="Custom House Agent Service";
				}
			}
			else
			{
				tmpmsg="Purchase";
			}
			msg_content=tmpmsg+"("+invoice_abbr+")"+" Remittance";

			String appPath = request.getServletContext().getRealPath("");

			String main_folder="";
			if(!comp_cd.equals(""))
			{
				main_folder=CommonVariable.work_dir+comp_cd;
			}
	        String sub_folder="purchase";
	        String sub_folder2="drcr";
	        File sub_folderDir2 = new File(appPath+File.separator+main_folder+File.separator+sub_folder+File.separator+sub_folder2);
	        if(!sub_folderDir2.exists())
	        {
	        	sub_folderDir2.mkdirs();
	        }

	        String monthofinv = dateUtil.getMonthNameMON(period_end_dt);
	        String invDt=dateUtil.getDateFormatDD_MOM_YY(""+invoice_data.get("InvoiceDate"));
	        String dtPeriod="";
	        if(!period_start_dt.equals("") && !period_end_dt.equals(""))
	        {
	        	dtPeriod=period_start_dt.substring(0,2);
	        	dtPeriod+="-";
	        	dtPeriod+=period_end_dt.substring(0,2);
	        }

	        String entity_nm="";

	        String contNm="";
	        String provNm="";
	        String invNm="";
	        String endingNm="";
	        if(contract_type.equals("I"))
	        {
	        	contNm="IGX";
	        	endingNm=monthofinv.trim()+"_"+dtPeriod;
	        }
	        else if(contract_type.equals("N"))
	        {
	        	contNm="LNG";
	        	endingNm=invDt;
	        }
	        else if(contract_type.equals("G"))
	        {
	        	contNm="CN";
	        	endingNm=monthofinv.trim()+"_"+dtPeriod;
	        }
	        else if(contract_type.equals("P"))
	        {
	        	contNm="PERIOD";
	        	endingNm=monthofinv.trim()+"_"+dtPeriod;
	        }
	        else if(contract_type.equals("Y"))
			{
	        	contNm="SURVEYOR";
	        	endingNm=invDt;
	        	entity_nm="Surveyor";
			}
			else if(contract_type.equals("A"))
			{
				contNm="VESSEL_AGENT";
				endingNm=invDt;
				entity_nm="Vessel Agent";
			}
			else if(contract_type.equals("H"))
			{
				contNm="CUSTOM_HOUSE_AGENT";
				endingNm=invDt;
				entity_nm="Custom House Agent";
			}
			else if(contract_type.equals("T"))
	        {
	        	contNm="InTankLNG_RLNG";
	        	endingNm=monthofinv.trim()+"_"+dtPeriod;
	        }
	        else
	        {
	        	contNm="DomNG";
	        	endingNm=monthofinv.trim()+"_"+dtPeriod;
	        }

	        if(inv_flag.equals("P") || inv_flag.equals("CP"))
	        {
	        	provNm="PROVISIONAL-";
	        }

	        if(inv_flag.equals("CP") || inv_flag.equals("CF"))
	        {
	        	invNm="CUSTOMDUTY_CHALLAN-";
	        }
	        else if(contract_type.equals("G") || contract_type.equals("P"))
	        {
	        	if(inv_flag.equals("UG"))
				{
	        		invNm="LTCORA_SUG_INVOICE-";
	        		endingNm=invDt;
				}
	        	else
	        	{
	        		invNm="LTCORA_INVOICE-";
	        	}
	        }
	        else
	        {
	        	invNm="INVOICE-";
	        }
	        String type = "";
	        if(drcr_flag.equals("DR")) {
	        	type = "Debit";
	        }
	        else if(drcr_flag.equals("CR")) {
	        	type = "Credit";
	        }


	        String drcr_dt="",drcr_due_dt="",item_value="",total_gross="",total_tax="",total_amt="",drcr_fin_yr="",inv_fin_yr="";
            queryString="SELECT A.DR_CR_NO,TO_CHAR(DR_CR_DT,'DD-MON-YY'),"
					+ "TO_CHAR(DR_CR_DUE_DT,'DD-MON-YY'),TO_CHAR(PERIOD_START_DT,'DD-MON-YY'),TO_CHAR(PERIOD_END_DT,'DD-MON-YY'),"
					+ "B.ITEM_DIFF_VALUE,A.TOTAL_GROSS_AMT,A.TOTAL_TAX_AMT,A.TOTAL_AMT, A.DR_CR_FIN_YR,A.FINANCIAL_YEAR,A.APPROVED_FLAG,A.INVOICE_NO,A.CRITERIA,A.DR_CR_SEQ_NO,B.ITEM_AMT,B.ITEM_DIFF_AMT "
					+ "FROM FMS_PUR_DR_CR_MST A, FMS_PUR_DR_CR_DTL B "
					+ "WHERE A.COMPANY_CD = B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
					+ "AND A.AGMT_NO=B.AGMT_NO AND A.PLANT_SEQ=B.PLANT_SEQ AND A.BU_UNIT=B.BU_UNIT AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
					+ "AND A.DR_CR_REF_NO = B.DR_CR_REF_NO AND A.INVOICE_NO = B.INVOICE_NO "
					+ "AND A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? "
					+ "AND A.AGMT_NO=? AND A.PLANT_SEQ=? AND A.BU_UNIT=? AND A.CONTRACT_TYPE=? "
					+ "AND A.PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND A.PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND A.FINANCIAL_YEAR=? "
					+ "AND A.INVOICE_SEQ=? AND A.INVOICE_NO = ? AND A.INVOICE_DT=TO_DATE(?,'DD/MM/YYYY') ";
					if(contract_type.equals("N") || contract_type.equals("G") || contract_type.equals("P")) {
						queryString+= "AND A.CARGO_NO=? ";
					}
            			queryString+= "AND A.INV_FLAG=? AND A.DR_CR_SEQ_NO = ? AND A.DR_CR_FLAG = ? "
            					+ "AND A.CRITERIA = ? AND A.REF_TYPE = ? ";
            int scount=1;
            stmt=conn.prepareStatement(queryString);
			stmt.setString(scount++, comp_cd);
			stmt.setString(scount++, counterparty_cd);
			stmt.setString(scount++, cont_no);
			stmt.setString(scount++, agmt_no);
			stmt.setString(scount++, plant_seq);
			stmt.setString(scount++, bu_plant_seq);
			stmt.setString(scount++, contract_type);
			stmt.setString(scount++, period_start_dt);
			stmt.setString(scount++, period_end_dt);
			stmt.setString(scount++, financial_year);
			stmt.setString(scount++, inv_seq);
			stmt.setString(scount++, invoice_data.get("InvoiceNo"));
			stmt.setString(scount++, invoice_data.get("InvoiceDate"));
			if(contract_type.equals("N") || contract_type.equals("G") || contract_type.equals("P")) {
			stmt.setString(scount++, cargo_no);
			}
			stmt.setString(scount++, inv_flag);
			stmt.setString(scount++, drcr_seq);
			stmt.setString(scount++, drcr_flag);
			stmt.setString(scount++, drcr_criteria);
			stmt.setString(scount++, ref_type);
			rset = stmt.executeQuery();
			if(rset.next()) {
				drcr_dt = rset.getString(2);
				drcr_due_dt = rset.getString(3);
				item_value = nf2.format(rset.getDouble(6));
				total_gross= nf.format(rset.getDouble(7));
				total_tax = nf.format(rset.getDouble(8));
				total_amt = nf.format(rset.getDouble(9));
				drcr_fin_yr = rset.getString(10);
				inv_fin_yr = rset.getString(11);
				}
			rset.close();
			stmt.close();

			file_nm=type.toUpperCase()+"-"+company_abbr+"-"+provNm+""+contNm+"-"+cont_no+"-"+drcr_seq+"-"+counterparty_abbr+"_"+drcr_fin_yr+"_"+endingNm+".pdf";

			path = appPath+File.separator+main_folder+File.separator+sub_folder+File.separator+sub_folder2+File.separator+file_nm;
			PdfWriter writer = PdfWriter.getInstance(document,new FileOutputStream(path));

			document.open();
			document.setPageSize(pageSize);
            document.newPage();

            String context_nm = request.getContextPath();
			String server_nm = request.getServerName();
			String server_port = ""+request.getServerPort();

			file_url = CommonVariable.app_protocol+"://"+server_nm+":"+server_port+context_nm;

			Font small_black_normal = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL,  BaseColor.BLACK);
			Font small_black_normal_10 = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL,  BaseColor.BLACK);
			Font small_red_normal_10 = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL,  BaseColor.RED);
			Font small_black_normal_14 = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.NORMAL,  BaseColor.BLACK);
            Font small_black_bold = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD,  BaseColor.BLACK);
            Font black_bold = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD,  BaseColor.BLACK);
            Font black_bold1 = FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD,  BaseColor.BLACK);

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

            float[] Contact1 = {0.50f};
   	     	PdfPTable LogoTable = new PdfPTable(Contact1);
   	     	LogoTable.setWidthPercentage(100);
   	     	LogoTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
   	     	LogoTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
   	     	LogoTable.addCell(company_logo_cell);

			float[] ContactAddrWidths1 = {0.100f};
   	     	PdfPTable HlplLogoTable = new PdfPTable(ContactAddrWidths1);
            HlplLogoTable.setWidthPercentage(100);
            HlplLogoTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            HlplLogoTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            HlplLogoTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
            HlplLogoTable.addCell(new Phrase(new Chunk("REMITTANCE ADVISE",small_black_normal_14)));

            float[] ContactAddrWidths2 = {0.100f};
   	     	PdfPTable Table = new PdfPTable(ContactAddrWidths2);
            Table.setWidthPercentage(100);
            Table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            Table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
            //Table.addCell(new Phrase(new Chunk(counterparty_nm,black_bold1)));
            Table.addCell(new Phrase(new Chunk(company_nm,black_bold1)));

            float[] ContactAddrWidths3 = {0.100f};
   	     	PdfPTable Table1 = new PdfPTable(ContactAddrWidths3);
            Table1.setWidthPercentage(100);
            Table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            Table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			/*if(inv_flag.equals("CP"))
			{
				Table1.addCell(new Phrase(new Chunk("PROVISIONAL CUSTOM DUTY CHALLAN ("+invoice_type+")",small_black_normal_10)));
			}
			else if(inv_flag.equals("CF"))
			{
				Table1.addCell(new Phrase(new Chunk("FINAL ASSESSED CUSTOM DUTY ("+invoice_type+")",small_black_normal_10)));
			}
			else if(contract_type.equals("N") && inv_flag.equals("PF"))
			{
				Table1.addCell(new Phrase(new Chunk("PROFORMA INVOICE ("+invoice_type+")",small_black_normal_10)));

				Table1.setWidthPercentage(100);
				Table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			    Table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			    Table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			    Table1.addCell(new Phrase(new Chunk("FOR CUSTOMS CLEARANCE PURPOSES ONLY",small_red_normal_10)));
			}
			else if(contract_type.equals("N") && inv_flag.equals("F"))
			{
				if(!invoice_data.get("DiffPrice").toString().equals(""))
				{
					if(Double.parseDouble(""+invoice_data.get("DiffPrice")) < 0)
					{
						Table1.addCell(new Phrase(new Chunk("CREDIT NOTE ("+invoice_type+")",small_black_normal_10)));
					}
					else
			    	{
			    		Table1.addCell(new Phrase(new Chunk("INVOICE ("+invoice_type+")",small_black_normal_10)));
			    	}
				}
				else
				{
					Table1.addCell(new Phrase(new Chunk("INVOICE ("+invoice_type+")",small_black_normal_10)));
				}
			}
			else if(contract_type.equals("N") && inv_flag.equals("P"))
			{
				Table1.addCell(new Phrase(new Chunk("INVOICE ("+invoice_type+")",small_black_normal_10)));
			}
			else
			{
				Table1.addCell(new Phrase(new Chunk("Tax Invoice ("+invoice_type+")",small_black_normal_10)));
			}*/


            if(drcr_flag.equals("DR"))
            {
            	Table1.addCell(new Phrase(new Chunk("DEBIT NOTE",small_black_normal_10)));
            }
            else if(drcr_flag.equals("CR"))
            {
            	Table1.addCell(new Phrase(new Chunk("CREDIT NOTE",small_black_normal_10)));
            }
            String contRef="";
            String cargoRef="";
			String signingDt="";
			String countptyCd="";
			String contRev="";
			String splitValue="";
			String top_heading_nm="";
			String agmtSigningDt="";

			String boe_number="";
			String boe_date="";
			String ship_nm="";
			String str_cargoname="";
			String arrival_dt="";

			if(contract_type.equals("Y") || contract_type.equals("A") || contract_type.equals("H"))
			{
				queryString="SELECT CONT_REF_NO,TO_CHAR(SIGNING_DT,'DD-MON-YY') "
						+ "FROM FMS_CARGO_SVC_CONT_MST "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND ENTITY_TYPE=? "
						+ "AND CONT_NO=? AND COUNTERPARTY_CD=? ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, contract_type);
				stmt.setString(3, entity);
				stmt.setString(4, cont_no);
				stmt.setString(5, counterparty_cd);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					contRef=rset.getString(1)==null?"":rset.getString(1);
					signingDt=rset.getString(2)==null?"":rset.getString(2);
				}
				rset.close();
				stmt.close();

				top_heading_nm=""+utilBean.getCounterpartyName(conn,counterparty_cd);

				String cargoCountptyCd="";
				String cargoContNo="";
				String cargoAgmtNo="";
				String cargoAgmtTyp="";
				String cargoContTyp="";
				String cargoNo="";
				if(!mapping_id.equals(""))
				{
					String[] temp=mapping_id.split("-");
					cargoCountptyCd=temp[0];
					cargoAgmtTyp=temp[1];
					cargoAgmtNo=temp[2];
					cargoContTyp=temp[3];
					cargoContNo=temp[4];
					cargoNo=temp[5];
				}

				queryString="SELECT TO_CHAR(A.ACT_ARRV_DT,'DD-MON-YY'),A.SHIP_CD "
						+ "FROM FMS_BUY_CARGO_ALLOC A, FMS_BUY_CARGO_ALLOC_BOE B "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_TYPE=? AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
						+ "AND A.CARGO_NO=? "
						+ "AND A.ALLOC_REV = (SELECT MAX(B.ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO AND A.AGMT_TYPE=B.AGMT_TYPE) "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
						+ "AND A.CONT_NO=B.CONT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
						+ "AND A.CARGO_NO=B.CARGO_NO AND A.ALLOC_REV=B.ALLOC_REV";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, cargoCountptyCd);
				stmt.setString(3, cargoAgmtTyp);
				stmt.setString(4, cargoAgmtNo);
				stmt.setString(5, cargoContNo);
				stmt.setString(6, cargoContTyp);
				stmt.setString(7, cargoNo);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					arrival_dt=rset.getString(1)==null?"":rset.getString(1);
					String ship_cd=rset.getString(2)==null?"":rset.getString(2);
					ship_nm=utilBean.getShipName(conn,ship_cd);
				}
				rset.close();
				stmt.close();
			}
			else if(contract_type.equals("N"))
			{
				queryString="SELECT A.CONT_REF_NO,TO_CHAR(A.SIGNING_DT,'DD-MON-YY'),B.CARGO_REF  "
						+ "FROM FMS_TRADER_CN_MST A, FMS_TRADER_CARGO_MST B "
						+ "WHERE A.COMPANY_CD=? AND A.CONTRACT_TYPE=? AND A.AGMT_TYPE=? "
						+ "AND A.CONT_NO=? AND A.AGMT_NO=? AND A.COUNTERPARTY_CD=? AND B.CARGO_NO=? "
						+ "AND A.CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CN_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_TYPE=B.AGMT_TYPE "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.AGMT_TYPE=B.AGMT_TYPE";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, contract_type);
				stmt.setString(3, "M");
				stmt.setString(4, cont_no);
				stmt.setString(5, agmt_no);
				stmt.setString(6, counterparty_cd);
				stmt.setString(7, cargo_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					contRef=rset.getString(1)==null?"":rset.getString(1);
					signingDt=rset.getString(2)==null?"":rset.getString(2);
					cargoRef=rset.getString(3)==null?"":rset.getString(3);
				}
				rset.close();
				stmt.close();

				queryString="SELECT B.BOE_REF,TO_CHAR(B.BOE_DT,'DD-MON-YY'),A.SHIP_CD "
						+ "FROM FMS_BUY_CARGO_ALLOC A, FMS_BUY_CARGO_ALLOC_BOE B "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_TYPE=? AND A.AGMT_NO=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? "
						+ "AND A.CARGO_NO=? AND B.BOE_NO=? "
						+ "AND A.ALLOC_REV = (SELECT MAX(B.ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CARGO_NO=B.CARGO_NO AND A.AGMT_TYPE=B.AGMT_TYPE) "
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
						+ "AND A.CONT_NO=B.CONT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO "
						+ "AND A.CARGO_NO=B.CARGO_NO AND A.ALLOC_REV=B.ALLOC_REV";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, "M");
				stmt.setString(4, agmt_no);
				stmt.setString(5, cont_no);
				stmt.setString(6, contract_type);
				stmt.setString(7, cargo_no);
				stmt.setString(8, boe_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					boe_number = rset.getString(1)==null?"":rset.getString(1);
					boe_date=rset.getString(2)==null?"":rset.getString(2);
					String ship_cd=rset.getString(3)==null?"":rset.getString(3);
					ship_nm=utilBean.getShipName(conn,ship_cd);
				}
				rset.close();
				stmt.close();

				queryString1="SELECT TO_CHAR(SIGNING_DT,'DD-MON-YY')  "
						+ "FROM FMS_TRADER_AGMT_MST A "
						+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_TRADER_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, "M");
				stmt1.setString(3, agmt_no);
				stmt1.setString(4, counterparty_cd);
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					agmtSigningDt=rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();

				top_heading_nm=""+utilBean.getCounterpartyName(conn,counterparty_cd);
			}
			else if(contract_type.equals("G") || contract_type.equals("P"))
			{
				queryString="SELECT A.CONT_REF_NO,TO_CHAR(A.SIGNING_DT,'DD-MON-YY'),A.CONT_REF_NO,B.SHIP_CD,B.BOE_NO,TO_CHAR(B.BOE_DT,'DD-MON-YY') "
						+ "FROM FMS_LTCORA_CONT_MST A,"
							+ "FMS_LTCORA_CONT_CARGO_DTL B "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? "
						+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.AGMT_TYPE=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND B.CARGO_NO=? "
						+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
						+ ""
						+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, "T");
				stmt.setString(4, agmt_no);
				stmt.setString(5, agmt_rev_no);
				stmt.setString(6, "L");
				stmt.setString(7, cont_no);
				stmt.setString(8, contract_type);
				stmt.setString(9, cargo_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					contRef=rset.getString(1)==null?"":rset.getString(1);
					signingDt=rset.getString(2)==null?"":rset.getString(2);
					cargoRef=rset.getString(3)==null?"":rset.getString(3);

					String ship_cd=rset.getString(4)==null?"":rset.getString(4);
					ship_nm=utilBean.getShipName(conn,ship_cd);
					boe_number = rset.getString(5)==null?"":rset.getString(5);
					boe_date=rset.getString(6)==null?"":rset.getString(6);

				}
				rset.close();
				stmt.close();

				queryString1="SELECT TO_CHAR(SIGNING_DT,'DD-MON-YY')  "
						+ "FROM FMS_LTCORA_AGMT_MST A "
						+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
						+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_LTCORA_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.BUY_SALE=B.BUY_SALE) ";
				stmt1=conn.prepareStatement(queryString1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, "L");
				stmt1.setString(3, agmt_no);
				stmt1.setString(4, counterparty_cd);
				stmt1.setString(5, "T");
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					agmtSigningDt=rset1.getString(1)==null?"":rset1.getString(1);
				}
				rset1.close();
				stmt1.close();

				top_heading_nm=""+utilBean.getCounterpartyName(conn,counterparty_cd);
			}
			else
			{
				queryString="SELECT CONT_REF_NO,TO_CHAR(SIGNING_DT,'DD-MON-YY'),TRADE_REF_NO,CONT_REV,COUNTERPARTY_CD,SPLIT_FLAG  "
						+ "FROM FMS_TRADER_CONT_MST A "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
						+ "AND CONT_NO=? AND AGMT_NO=? "
						+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_TRADER_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, contract_type);
				stmt.setString(3, cont_no);
				stmt.setString(4, agmt_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					contRef=rset.getString(1)==null?"":rset.getString(1);
					signingDt=rset.getString(2)==null?"":rset.getString(2);

					String trade_ref=rset.getString(3)==null?"":rset.getString(3);
					if(contract_type.equals("I"))
					{
						contRef=trade_ref;
					}

					contRev=rset.getString(4)==null?"":rset.getString(4);
					countptyCd=rset.getString(5)==null?"":rset.getString(5);
					splitValue=rset.getString(6)==null?"":rset.getString(6);
				}
				rset.close();
				stmt.close();

				if(splitValue.equals("Y"))
				{
					String nm=""+utilBean.getCounterpartyName(conn,countptyCd);
					top_heading_nm=""+nm;

					queryString="SELECT SPLIT_TRADER_CD,PLANT_SEQ_NO,SPLIT_VALUE "
							+ "FROM FMS_TRADER_CONT_SPLIT_PLANT "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND CONT_REV=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, countptyCd);
					stmt.setString(3, cont_no);
					stmt.setString(4, contRev);
					stmt.setString(5, agmt_no);
					stmt.setString(6, agmt_rev_no);
					stmt.setString(7, contract_type);
					rset=stmt.executeQuery();
					while(rset.next())
					{
						String cd=rset.getString(1)==null?"":rset.getString(1);
						nm=""+utilBean.getCounterpartyName(conn,cd);
						if(top_heading_nm.equals(""))
						{
							top_heading_nm+=""+nm;
						}
						else
						{
							top_heading_nm+=" and "+nm;
						}
					}
					rset.close();
					stmt.close();
				}
				else
				{
					top_heading_nm=""+counterparty_nm;
				}
			}

			if(contract_type.equals("Y") || contract_type.equals("A") || contract_type.equals("H"))
			{
				str_cargoname=ship_nm+" Dated "+arrival_dt;
			}
			else
			{
				str_cargoname="Cargo Name - "+ship_nm+" (BOE "+boe_number+" Dated "+boe_date+")";
				if(boe_number.equals(""))
				{
					str_cargoname="Cargo Name - "+ship_nm;
				}
			}

			String temp_period_end_dt=dateUtil.getDateFormatDD_MOM_YY(period_end_dt);
			String mnthYr="";
			if(!temp_period_end_dt.equals(""))
			{
				mnthYr=temp_period_end_dt.substring(3,temp_period_end_dt.length());
			}

			String info = "In respect of ";

			if(contract_type.equals("N"))
			{
				info+="Confirmation Notice";
				info+="("+contRef+"/"+cargoRef+") executed on "+signingDt+" pursuant to MSPA executed on "+agmtSigningDt;
				info+="\nBetween "+top_heading_nm+" and "+company_nm;
			}
			else
			{
				if(contract_type.equals("I"))
				{
					info+="Exchange Transaction";
				}
				else if(contract_type.equals("G") || contract_type.equals("P"))
				{
					info+="LTCORA Purchase Contract";
				}
				else if(contract_type.equals("A") || contract_type.equals("Y") || contract_type.equals("H"))
				{
					info+=entity_nm+" Contract";
				}
				else
				{
					info+="Purchase Contract";
				}
				info+="("+contRef+") executed on "+signingDt+"";
	            		//+ "\nFor the Billing Period "+period_start_dt+" to "+period_end_dt;
				info+="\nBetween "+top_heading_nm+" and "+company_nm;
			}

            float[] ContactAddrWidths4 = {0.100f};
   	     	PdfPTable Table2 = new PdfPTable(ContactAddrWidths4);
            Table2.setWidthPercentage(100);
            Table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            Table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
            Table2.addCell(new Phrase(new Chunk(info,small_black_normal)));

            float[] ContactAddrWidths5 = {0.80f,0.30F,0.80F};
   	     	PdfPTable Table3 = new PdfPTable(ContactAddrWidths5);
            Table3.setWidthPercentage(100);
            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            Table3.addCell(new Phrase(new Chunk("From:",black_bold)));

            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            Table3.addCell(new Phrase(new Chunk("",black_bold)));

            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            Table3.addCell(new Phrase(new Chunk("To:",black_bold)));

            HashMap bu_plant_detail=utilBean.getCounterpartyPlantDetail(conn,comp_cd, "B", comp_cd, bu_plant_seq);
            String bu_plantAddress=""+bu_plant_detail.get("plant_address");
			String bu_plantCity=""+bu_plant_detail.get("plant_city");
			String bu_plantState=""+bu_plant_detail.get("plant_state");
			String bu_plantPin=""+bu_plant_detail.get("plant_pin");

			HashMap plant_detail=new HashMap();
			if(contract_type.equals("Y") || contract_type.equals("H") || contract_type.equals("A"))
			{
				plant_detail=utilBean.getCounterpartyBuPlantDetail(conn,comp_cd, entity, counterparty_cd, plant_seq);
			}
			else
			{
				plant_detail=utilBean.getCounterpartyPlantDetail(conn,comp_cd, "T", counterparty_cd, plant_seq);
			}
            String plantAddress=""+plant_detail.get("plant_address");
			String plantCity=""+plant_detail.get("plant_city");
			String plantState=""+plant_detail.get("plant_state");
			String plantPin=""+plant_detail.get("plant_pin");

			float[] ContactAddrWidths6 = {0.80f,0.30F,0.80F};
   	     	PdfPTable Table4 = new PdfPTable(ContactAddrWidths6);
            Table4.setWidthPercentage(100);
            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            Table4.addCell(new Phrase(new Chunk(company_nm,small_black_normal)));

            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            Table4.addCell(new Phrase(new Chunk("",small_black_normal)));

            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            if(inv_flag.equals("CP") || inv_flag.equals("CF"))
            {
            	Table4.addCell(new Phrase(new Chunk("Indian Customs",small_black_normal)));
            }
            else
            {
            	Table4.addCell(new Phrase(new Chunk(counterparty_nm,small_black_normal)));
            }

            float[] ContactAddrWidths7 = {0.80f,0.30F,0.80F};
   	     	PdfPTable Table5 = new PdfPTable(ContactAddrWidths7);
            Table5.setWidthPercentage(100);
            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            Table5.addCell(new Phrase(new Chunk(bu_plantAddress+","+bu_plantCity,small_black_normal)));

            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            Table5.addCell(new Phrase(new Chunk("",small_black_normal)));

            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            if(!inv_flag.equals("CP") && !inv_flag.equals("CF"))
            {
            	Table5.addCell(new Phrase(new Chunk(plantAddress+","+plantCity,small_black_normal)));
            }
            else
            {
            	Table5.addCell(new Phrase(new Chunk("",small_black_normal)));
            }

            float[] ContactAddrWidths8 = {0.80f,0.30F,0.80F};
   	     	PdfPTable Table6 = new PdfPTable(ContactAddrWidths8);
            Table6.setWidthPercentage(100);
            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            Table6.addCell(new Phrase(new Chunk(bu_plantState+" - "+bu_plantPin,small_black_normal)));

            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            Table6.addCell(new Phrase(new Chunk("",small_black_normal)));

            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            if(!inv_flag.equals("CP") && !inv_flag.equals("CF"))
            {
            	Table6.addCell(new Phrase(new Chunk(plantState+" - "+plantPin,small_black_normal)));
            }
            else
            {
            	Table6.addCell(new Phrase(new Chunk("",small_black_normal)));
            }

            String bu_tax_info="";
            String tax_info="";
            if(contract_type.equals("Y") || contract_type.equals("H") || contract_type.equals("A"))
			{
            	queryString = "SELECT A.STAT_NO, TO_CHAR(A.EFF_DT,'DD-MON-YY'), B.STAT_NM "
						+ "FROM FMS_COUNTERPARTY_BU_TAX A, FMS_GOVT_STAT_TAX B "
						+ "WHERE A.COUNTERPARTY_CD=? AND A.ENTITY=? "
						+ "AND A.PLANT_SEQ_NO=? AND A.COMPANY_CD=? AND B.STAT_CD=? "
						+ "AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, counterparty_cd);
				stmt.setString(2, entity);
				stmt.setString(3, plant_seq);
				stmt.setString(4, comp_cd);
				stmt.setString(5, "1003");
				rset=stmt.executeQuery();

				if(rset.next())
				{
					String no = rset.getString(1)==null?"":rset.getString(1);
					String nm = rset.getString(3)==null?"":rset.getString(3);

					tax_info+="\n"+nm+" : "+no;
				}
				rset.close();
				stmt.close();

				queryString = "SELECT A.STAT_NO, TO_CHAR(A.EFF_DT,'DD-MON-YY'), B.STAT_NM "
						+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B "
						+ "WHERE A.COUNTERPARTY_CD=? AND A.ENTITY=? "
						+ "AND A.PLANT_SEQ_NO=? AND A.COMPANY_CD=? AND B.STAT_CD=? "
						+ "AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, "B");
				stmt.setString(3, bu_plant_seq);
				stmt.setString(4, comp_cd);
				stmt.setString(5, "1003");
				rset=stmt.executeQuery();
				if(rset.next())
				{
					String no = rset.getString(1)==null?"":rset.getString(1);
					String nm = rset.getString(3)==null?"":rset.getString(3);

					bu_tax_info+="\n"+nm+" : "+no;
				}
				rset.close();
				stmt.close();
			}
            else if(contract_type.equals("N"))
			{
            	queryString = "SELECT A.STAT_NO, TO_CHAR(A.EFF_DT,'DD-MON-YY'), B.STAT_NM "
						+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B "
						+ "WHERE A.COUNTERPARTY_CD=? AND A.ENTITY=? "
						+ "AND A.PLANT_SEQ_NO=? AND A.COMPANY_CD=? AND B.STAT_CD=? "
						+ "AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, counterparty_cd);
				stmt.setString(2, "T");
				stmt.setString(3, plant_seq);
				stmt.setString(4, comp_cd);
				stmt.setString(5, "1001");
				rset=stmt.executeQuery();
				if(rset.next())
				{
					String no = rset.getString(1)==null?"":rset.getString(1);
					String nm = rset.getString(3)==null?"":rset.getString(3);

					tax_info+="\n"+nm+" : "+no;
				}
				rset.close();
				stmt.close();

				queryString = "SELECT A.STAT_NO, TO_CHAR(A.EFF_DT,'DD-MON-YY'), B.STAT_NM "
						+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B "
						+ "WHERE A.COUNTERPARTY_CD=? AND A.ENTITY=? "
						+ "AND A.PLANT_SEQ_NO=? AND A.COMPANY_CD=? AND B.STAT_CD=? "
						+ "AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, "B");
				stmt.setString(3, bu_plant_seq);
				stmt.setString(4, comp_cd);
				stmt.setString(5, "1001");
				rset=stmt.executeQuery();
				if(rset.next())
				{
					String no = rset.getString(1)==null?"":rset.getString(1);
					String nm = rset.getString(3)==null?"":rset.getString(3);

					bu_tax_info+="\n"+nm+" : "+no;
				}
				rset.close();
				stmt.close();
			}
            else
            {
            	bu_tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "B", comp_cd, bu_plant_seq);
            	tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "T", counterparty_cd, plant_seq);

            	if(contract_type.equals("G") || contract_type.equals("P"))
				{
					bu_tax_info+="\nSAC : 999799"
							+ "\nDescription of Service : Other Miscellaneous services - Other Services n.e.c."
							+ "\nPlace Of Supply : "+bu_plantState;
				}
            }





			float[] ContactAddrWidths9 = {0.80f,0.30F,0.80F};
   	     	PdfPTable Table7 = new PdfPTable(ContactAddrWidths9);
            Table7.setWidthPercentage(100);
            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            Table7.addCell(new Phrase(new Chunk(bu_tax_info,small_black_normal)));

            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            Table7.addCell(new Phrase(new Chunk("",small_black_normal)));

            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            if(!inv_flag.equals("CP") && !inv_flag.equals("CF"))
            {
            	Table7.addCell(new Phrase(new Chunk(tax_info,small_black_normal)));
            }
            else
            {
            	Table7.addCell(new Phrase(new Chunk("",small_black_normal)));
            }

            float[] ContactAddrWidths10 = {0.80F,0.30F,0.30F,0.40F};
   	     	PdfPTable Table8 = new PdfPTable(ContactAddrWidths10);
            Table8.setWidthPercentage(100);
            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
            Table8.addCell(new Phrase(new Chunk("",small_black_normal)));

            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
            Table8.addCell(new Phrase(new Chunk("",small_black_normal)));

            Table8.getDefaultCell().setBorder(Rectangle.BOX);
            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            Table8.addCell(new Phrase(new Chunk(type+" Note Date :",black_bold)));

            Table8.getDefaultCell().setBorder(Rectangle.BOX);
            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            Table8.addCell(new Phrase(new Chunk(drcr_dt,black_bold)));

            if(drcr_due_dt != null && !drcr_due_dt.equals(""))
            {
            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
            Table8.addCell(new Phrase(new Chunk("",small_black_normal)));

            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
            Table8.addCell(new Phrase(new Chunk("",small_black_normal)));

            Table8.getDefaultCell().setBorder(Rectangle.BOX);
            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            Table8.addCell(new Phrase(new Chunk(type+" Due Date :",black_bold)));

            Table8.getDefaultCell().setBorder(Rectangle.BOX);
            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            Table8.addCell(new Phrase(new Chunk(drcr_due_dt,black_bold)));
            }

            float[] ContactAddrWidths11 = {0.80F,0.30F,0.30F,0.40F};
            PdfPTable Table9 = new PdfPTable(ContactAddrWidths11);
//            if(drcr_due_dt != null && !drcr_due_dt.equals(""))
            {
	            Table9.setWidthPercentage(100);
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table9.addCell(new Phrase(new Chunk("",small_black_normal)));

	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table9.addCell(new Phrase(new Chunk("",small_black_normal)));

	            Table9.getDefaultCell().setBorder(Rectangle.BOX);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table9.addCell(new Phrase(new Chunk(type+" Note No :",black_bold)));

	            Table9.getDefaultCell().setBorder(Rectangle.BOX);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table9.addCell(new Phrase(new Chunk(drcr_no,black_bold)));
            }

            float[] ContactAddrWidths10_1 = {0.80F,0.30F,0.30F,0.40F};
            PdfPTable Table8_1 = new PdfPTable(ContactAddrWidths10_1);
            Table8_1.setWidthPercentage(100);
            Table8_1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table8_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            Table8_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
            Table8_1.addCell(new Phrase(new Chunk("",small_black_normal)));

            Table8_1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table8_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            Table8_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
            Table8_1.addCell(new Phrase(new Chunk("",small_black_normal)));

            Table8_1.getDefaultCell().setBorder(Rectangle.BOX);
            Table8_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table8_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            Table8_1.addCell(new Phrase(new Chunk("Remittance#  :",black_bold)));

            Table8_1.getDefaultCell().setBorder(Rectangle.BOX);
            Table8_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table8_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            Table8_1.addCell(new Phrase(new Chunk(invoice_data.get("SysInvNo"),black_bold)));

            Table8_1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table8_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            Table8_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
            Table8_1.addCell(new Phrase(new Chunk("",small_black_normal)));

            Table8_1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table8_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            Table8_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
            Table8_1.addCell(new Phrase(new Chunk("",small_black_normal)));

            Table8_1.getDefaultCell().setBorder(Rectangle.BOX);
            Table8_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table8_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            Table8_1.addCell(new Phrase(new Chunk(inv_lineitem+" Date :",black_bold)));

            Table8_1.getDefaultCell().setBorder(Rectangle.BOX);
            Table8_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table8_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            Table8_1.addCell(new Phrase(new Chunk(dateUtil.getDateFormatDD_MOM_YY(""+invoice_data.get("InvoiceDate")),black_bold)));



            float[] ContactAddrWidths12 = {0.18F,0.62F,0.30F,0.30F,0.40F};
   	     	PdfPTable Table10 = new PdfPTable(ContactAddrWidths12);
            Table10.setWidthPercentage(100);
            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            Table10.addCell(new Phrase(new Chunk("CRITERIA : ",black_bold)));

            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            if(drcr_criteria.equals("1")) {
            	Table10.addCell(new Phrase(new Chunk("CHANGE IN EXCHANGE RATE",black_bold)));
            }
            else if(drcr_criteria.equals("2")) {
            	Table10.addCell(new Phrase(new Chunk("CHANGE IN PRICE",black_bold)));
            }
            else if(drcr_criteria.equals("3")) {
            	Table10.addCell(new Phrase(new Chunk("CHANGE IN QUANTITY",black_bold)));
            }

            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
            Table10.addCell(new Phrase(new Chunk("",small_black_normal)));

            Table10.getDefaultCell().setBorder(Rectangle.BOX);
            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//            if(contract_type.equals("Y") || contract_type.equals("H") || contract_type.equals("A"))
//            {
//            	Table10.addCell(new Phrase(new Chunk("Vendor "+inv_lineitem+"# :",small_black_normal)));
//            }
//            else
//            {
            	Table10.addCell(new Phrase(new Chunk("Ref "+inv_lineitem+" # :",black_bold)));
//            }

            Table10.getDefaultCell().setBorder(Rectangle.BOX);
            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            Table10.addCell(new Phrase(new Chunk(""+invoice_data.get("InvoiceNo"),black_bold)));

//            float[] ContactAddrWidths12_1 = {0.80F,0.20F,0.10F,0.20F};
//   	     	PdfPTable Table10_1 = new PdfPTable(ContactAddrWidths12_1);
//   	     	Table10_1.setWidthPercentage(100);
//            Table10_1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//            Table10_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//            Table10_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//            Table10_1.addCell(new Phrase(new Chunk("Billing Period : ",small_black_normal)));
//
//            Table10_1.getDefaultCell().setBorder(Rectangle.BOX);
//            Table10_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//            Table10_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//            Table10_1.addCell(new Phrase(new Chunk(""+temp_period_start_dt,small_black_normal)));
//
//            Table10_1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//            Table10_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//            Table10_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//            Table10_1.addCell(new Phrase(new Chunk("to",small_black_normal)));
//
//            Table10_1.getDefaultCell().setBorder(Rectangle.BOX);
//            Table10_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//            Table10_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//            Table10_1.addCell(new Phrase(new Chunk(""+temp_period_end_dt,small_black_normal)));

            float[] ContactAddrWidths13 = {0.055F,0.60F,0.20F,0.20F,0.30F,0.20F,0.20F};
   	     	PdfPTable Table11 = new PdfPTable(ContactAddrWidths13);
            Table11.setWidthPercentage(100);
            Table11.getDefaultCell().setBorder(Rectangle.BOX);
            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
            Table11.addCell(new Phrase(new Chunk("Sr.",black_bold)));

            Table11.getDefaultCell().setBorder(Rectangle.BOX);
            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
            Table11.addCell(new Phrase(new Chunk("Item",black_bold)));

            Table11.getDefaultCell().setBorder(Rectangle.BOX);
            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
            Table11.addCell(new Phrase(new Chunk("Attachment",black_bold)));

            Table11.getDefaultCell().setBorder(Rectangle.BOX);
            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
            Table11.addCell(new Phrase(new Chunk("Currency",black_bold)));

            Table11.getDefaultCell().setBorder(Rectangle.BOX);
            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//            if(contract_type.equals("Y") || contract_type.equals("H") || contract_type.equals("A"))
//            {
//            	Table11.addCell(new Phrase(new Chunk("Quantity",small_black_normal)));
//            }
//            else
//            {
            Table11.addCell(new Phrase(new Chunk("Quantity\n(MMBTU)",black_bold)));
//            }

            Table11.getDefaultCell().setBorder(Rectangle.BOX);
            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
            Table11.addCell(new Phrase(new Chunk("Rate",black_bold)));

            Table11.getDefaultCell().setBorder(Rectangle.BOX);
            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
            Table11.addCell(new Phrase(new Chunk("Amount",black_bold)));

            String salesValue="";
            salesValue=""+invoice_data.get("GrossAmt");

            String curr="";
            String curr1="";
            if(invoice_data.get("PriceCd").equals("1"))
            {
            	curr="INR";
            }
            else if(invoice_data.get("PriceCd").equals("2"))
            {
            	curr="USD";
            }

            if(invoice_data.get("InvoiceRaisedIn").equals("1"))
            {
            	curr1="INR";
            }
            else if(invoice_data.get("InvoiceRaisedIn").equals("2"))
            {
            	curr1="USD";
            }




            int sr=1;

            float[] ContactAddrWidths14 = {0.055F,0.60F,0.20F,0.20F,0.30F,0.20F,0.20F};
   	     	PdfPTable Table12 = new PdfPTable(ContactAddrWidths14);
            Table12.setWidthPercentage(100);
            //Table12.getDefaultCell().setBorderWidthBottom(0);

            Table12.getDefaultCell().setBorder(Rectangle.BOX);
            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
            Table12.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

            Table12.getDefaultCell().setBorder(Rectangle.BOX);
            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            if(contract_type.equals("N")){
            	Table12.addCell(new Phrase(new Chunk("Liquified Natural Gas (LNG)",small_black_normal)));
            }else if(contract_type.equals("G") || contract_type.equals("P")) {
            	if(inv_flag.equals("UG")) {
            		Table12.addCell(new Phrase(new Chunk("Natural Gas (Discharge during "+mnthYr+")",small_black_normal)));
            	}else {
            		Table12.addCell(new Phrase(new Chunk("Natural Gas (Regasified)",small_black_normal)));
            	}
            }else if(contract_type.equals("Y") || contract_type.equals("A") || contract_type.equals("H")) {
            	Table12.addCell(new Phrase(new Chunk(entity_nm+" Service Fee for "+str_cargoname,small_black_normal)));
            }else {
            	Table12.addCell(new Phrase(new Chunk("Natural Gas",small_black_normal)));
            }

            Table12.getDefaultCell().setBorder(Rectangle.BOX);
            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
            Table12.addCell(new Phrase(new Chunk("",small_black_normal)));

            Table12.getDefaultCell().setBorder(Rectangle.BOX);
            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
            Table12.addCell(new Phrase(new Chunk(""+curr,small_black_normal)));

            Table12.getDefaultCell().setBorder(Rectangle.BOX);
            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
            if(contract_type.equals("Y") || contract_type.equals("H") || contract_type.equals("A"))
            {
            	Table12.addCell(new Phrase(new Chunk(""+invoice_data.get("Qty")+" "+invoice_data.get("QtyUnitNm"),small_black_normal)));
            }
            else
            {
            	Table12.addCell(new Phrase(new Chunk(""+invoice_data.get("Qty"),small_black_normal)));
            }

            Table12.getDefaultCell().setBorder(Rectangle.BOX);
            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
            if(inv_flag.equals("UG"))
            {
            	Table12.addCell(new Phrase(new Chunk("",small_black_normal)));
            }
            else
            {
            	Table12.addCell(new Phrase(new Chunk(""+invoice_data.get("Price"),small_black_normal)));
            }

            Table12.getDefaultCell().setBorder(Rectangle.BOX);
            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
            if((contract_type.equals("N") && inv_flag.equals("F")) || inv_flag.equals("UG"))
            {
            	Table12.addCell(new Phrase(new Chunk("",small_black_normal)));
            }
            else
            {
            	Table12.addCell(new Phrase(new Chunk(""+salesValue,small_black_normal)));
            }

//            if(contract_type.equals("N") && inv_flag.equals("F"))
//            {
//            	sr+=1;
//            	Table12.setWidthPercentage(100);
//                Table12.getDefaultCell().setBorder(Rectangle.BOX);
//                Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//                Table12.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
//
//                Table12.getDefaultCell().setBorder(Rectangle.BOX);
//                Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//                Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//                Table12.addCell(new Phrase(new Chunk("Difference in Price",small_black_normal)));
//
//                Table12.getDefaultCell().setBorder(Rectangle.BOX);
//                Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//                Table12.addCell(new Phrase(new Chunk(""+curr,small_black_normal)));
//
//                Table12.getDefaultCell().setBorder(Rectangle.BOX);
//                Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//                Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//                Table12.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//                Table12.getDefaultCell().setBorder(Rectangle.BOX);
//                Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//                Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//                Table12.addCell(new Phrase(new Chunk(""+invoice_data.get("DiffPrice"),small_black_normal)));
//
//                Table12.getDefaultCell().setBorder(Rectangle.BOX);
//                Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//                Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//                Table12.addCell(new Phrase(new Chunk(""+salesValue,small_black_normal)));
//            }

//            if(inv_flag.equals("UG"))
//            {
//            	sr+=1;
//            	Table12.setWidthPercentage(100);
//                Table12.getDefaultCell().setBorder(Rectangle.BOX);
//                Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//                Table12.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
//
//                Table12.getDefaultCell().setBorder(Rectangle.BOX);
//                Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//                Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//                Table12.addCell(new Phrase(new Chunk("SUG ("+invoice_data.get("SUGpercent")+"% of above)",small_black_normal)));
//
//                Table12.getDefaultCell().setBorder(Rectangle.BOX);
//                Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//                Table12.addCell(new Phrase(new Chunk(""+curr,small_black_normal)));
//
//                Table12.getDefaultCell().setBorder(Rectangle.BOX);
//                Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//                Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//                Table12.addCell(new Phrase(new Chunk(""+invoice_data.get("SUGqty"),small_black_normal)));
//
//                Table12.getDefaultCell().setBorder(Rectangle.BOX);
//                Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//                Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//                Table12.addCell(new Phrase(new Chunk(""+invoice_data.get("Price"),small_black_normal)));
//
//                Table12.getDefaultCell().setBorder(Rectangle.BOX);
//                Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//                Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//                Table12.addCell(new Phrase(new Chunk(""+salesValue,small_black_normal)));
//            }
            String crt="";
            if(drcr_criteria.equals("1")) {
            	crt = "Difference in Exchange Rate";
            }
            else if(drcr_criteria.equals("2")) {
            	crt = "Difference in Price";
            }
            else if(drcr_criteria.equals("3")) {
            	crt = "Difference in Quantity";
            }

            if(invoice_data.get("InvoiceRaisedIn").equals(invoice_data.get("PriceCd")) && !drcr_criteria.equals("1"))
            {
            	sr+=1;

               Table12.setWidthPercentage(100);
               Table12.getDefaultCell().setBorder(Rectangle.BOX);
               Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
               Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
               Table12.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

               Table12.getDefaultCell().setBorder(Rectangle.BOX);
               Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
               Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
               Table12.addCell(new Phrase(new Chunk(crt,small_black_normal)));

               Table12.getDefaultCell().setBorder(Rectangle.BOX);
               Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
               Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
               Table12.addCell(new Phrase(new Chunk("Att1",small_black_normal)));

               Table12.getDefaultCell().setBorder(Rectangle.BOX);
               Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
               Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
               if(drcr_criteria.equals("2")) {
            	   Table12.addCell(new Phrase(new Chunk("USD",small_black_normal)));
               }
               else if(drcr_criteria.equals("3")) {
            	   Table12.addCell(new Phrase(new Chunk("",small_black_normal)));
               }

               Table12.getDefaultCell().setBorder(Rectangle.BOX);
               Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
               Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
               if(drcr_criteria.equals("3")) {
            	   Table12.addCell(new Phrase(new Chunk(item_value,small_black_normal)));
               }
               else {
            	   Table12.addCell(new Phrase(new Chunk("",small_black_normal)));
               }

               Table12.getDefaultCell().setBorder(Rectangle.BOX);
               Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
               Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
               if(drcr_criteria.equals("2")) {
            	   Table12.addCell(new Phrase(new Chunk(item_value,small_black_normal)));
               }
               else {
            	   Table12.addCell(new Phrase(new Chunk("",small_black_normal)));
               }

               Table12.getDefaultCell().setBorder(Rectangle.BOX);
               Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
               Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
               Table12.addCell(new Phrase(new Chunk("",small_black_normal)));
            }
            float[] ContactAddrWidths15 = {0.055F,0.60F,0.20F,0.20F,0.30F,0.20F,0.20F};
   	     	PdfPTable Table13 = new PdfPTable(ContactAddrWidths15);
            Table13.setWidthPercentage(100);
            if(!invoice_data.get("InvoiceRaisedIn").equals(invoice_data.get("PriceCd")))
            {
            	sr+=1;

            //Table13.getDefaultCell().setBorderWidthTop(0);

            Table13.getDefaultCell().setBorder(Rectangle.BOX);
            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
            Table13.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

            Table13.getDefaultCell().setBorder(Rectangle.BOX);
            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            if(drcr_criteria.equals("1")) {
            	Table13.addCell(new Phrase(new Chunk(crt,small_black_normal)));
            }
            else {
            	Table13.addCell(new Phrase(new Chunk(exchg_lineitem,small_black_normal)));
            }

            Table13.getDefaultCell().setBorder(Rectangle.BOX);
            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
            if(drcr_criteria.equals("1")) {
            	Table13.addCell(new Phrase(new Chunk("Att1",small_black_normal)));
            }
            else {
            	Table13.addCell(new Phrase(new Chunk("",small_black_normal)));
            }

            Table13.getDefaultCell().setBorder(Rectangle.BOX);
            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
            Table13.addCell(new Phrase(new Chunk("INR/USD",small_black_normal)));

            Table13.getDefaultCell().setBorder(Rectangle.BOX);
            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
            Table13.addCell(new Phrase(new Chunk("",small_black_normal)));

            Table13.getDefaultCell().setBorder(Rectangle.BOX);
            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
            if(drcr_criteria.equals("1")) {
            	Table13.addCell(new Phrase(new Chunk(item_value,small_black_normal)));
            }
            else {
            	Table13.addCell(new Phrase(new Chunk(""+invoice_data.get("ExchangRate"),small_black_normal)));
            }

            Table13.getDefaultCell().setBorder(Rectangle.BOX);
            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
            Table13.addCell(new Phrase(new Chunk("",small_black_normal)));

            if(!drcr_criteria.equals("1")) {
	            sr+=1;
	            Table13.setWidthPercentage(100);
	            Table13.getDefaultCell().setBorder(Rectangle.BOX);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table13.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

	            Table13.getDefaultCell().setBorder(Rectangle.BOX);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table13.addCell(new Phrase(new Chunk(crt,small_black_normal)));

	            Table13.getDefaultCell().setBorder(Rectangle.BOX);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table13.addCell(new Phrase(new Chunk("Att1",small_black_normal)));

	            Table13.getDefaultCell().setBorder(Rectangle.BOX);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table13.addCell(new Phrase(new Chunk("USD",small_black_normal)));

	            Table13.getDefaultCell().setBorder(Rectangle.BOX);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table13.addCell(new Phrase(new Chunk("",small_black_normal)));

	            Table13.getDefaultCell().setBorder(Rectangle.BOX);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table13.addCell(new Phrase(new Chunk(item_value,small_black_normal)));

	            Table13.getDefaultCell().setBorder(Rectangle.BOX);
	            Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table13.addCell(new Phrase(new Chunk("",small_black_normal)));
            }
		}

            sr+=1;
            float[] ContactAddrWidths16 = {0.055F,0.60F,0.20F,0.20F,0.30F,0.20F,0.20F};
   	     	PdfPTable Table14 = new PdfPTable(ContactAddrWidths16);
   	     	Table14.setWidthPercentage(100);
   	     	//Table14.getDefaultCell().setBorderWidthBottom(0);

   	     	Table14.getDefaultCell().setBorder(Rectangle.BOX);
   	     	Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
   	     	Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
   	     	Table14.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

   	     	Table14.getDefaultCell().setBorder(Rectangle.BOX);
   	     	Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
   	     	Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
   	     	Table14.addCell(new Phrase(new Chunk(gross_lineitem+" Amount",small_black_normal)));

   	     	Table14.getDefaultCell().setBorder(Rectangle.BOX);
   	     	Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
   	     	Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
   	     	Table14.addCell(new Phrase(new Chunk("",small_black_normal)));

   	     	Table14.getDefaultCell().setBorder(Rectangle.BOX);
   	     	Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
   	     	Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
   	     	Table14.addCell(new Phrase(new Chunk(""+curr1,small_black_normal)));

   	     	Table14.getDefaultCell().setBorder(Rectangle.BOX);
   	     	Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
   	     	Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
   	     	Table14.addCell(new Phrase(new Chunk("",small_black_normal)));

   	     	Table14.getDefaultCell().setBorder(Rectangle.BOX);
   	     	Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
   	     	Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
   	     	Table14.addCell(new Phrase(new Chunk("",small_black_normal)));

   	     	Table14.getDefaultCell().setBorder(Rectangle.BOX);
   	     	Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
   	     	Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
   	     	Table14.addCell(new Phrase(new Chunk(total_gross,small_black_normal)));

//   	     float[] ContactAddrWidths16_1 = {0.05F,0.60F,0.20F,0.20F,0.30F,0.20F,0.20F};
//	     	PdfPTable Table14_1 = new PdfPTable(ContactAddrWidths16_1);
//   	     if(invoice_data.get("InvoiceRaisedIn").equals(invoice_data.get("PriceCd")))
//         {
//         	sr+=1;
//
//   	     	Table14_1.setWidthPercentage(100);
//         	Table14_1.setWidthPercentage(100);
//            Table14_1.getDefaultCell().setBorder(Rectangle.BOX);
//            Table14_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//            Table14_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//            Table14_1.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
//
//            Table14_1.getDefaultCell().setBorder(Rectangle.BOX);
//            Table14_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//            Table14_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//            Table14_1.addCell(new Phrase(new Chunk(crt,small_black_normal)));
//
//            Table14_1.getDefaultCell().setBorder(Rectangle.BOX);
//            Table14_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//            Table14_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//            Table14_1.addCell(new Phrase(new Chunk("Att1",small_black_normal)));
//
//            Table14_1.getDefaultCell().setBorder(Rectangle.BOX);
//            Table14_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//            Table14_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//            Table14_1.addCell(new Phrase(new Chunk("USD",small_black_normal)));
//
//            Table14_1.getDefaultCell().setBorder(Rectangle.BOX);
//            Table14_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//            Table14_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//            Table14_1.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//            Table14_1.getDefaultCell().setBorder(Rectangle.BOX);
//            Table14_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//            Table14_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//            Table14_1.addCell(new Phrase(new Chunk(item_value,small_black_normal)));
//
//            Table14_1.getDefaultCell().setBorder(Rectangle.BOX);
//            Table14_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//            Table14_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//            Table14_1.addCell(new Phrase(new Chunk("",small_black_normal)));
//         }



//            if(!invoice_data.get("trans_chrg").toString().equals(""))
//            {
//            	sr+=1;
//                Table14.setWidthPercentage(100);
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//                Table14.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//                Table14.addCell(new Phrase(new Chunk("Transportation Tariff",small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//                Table14.addCell(new Phrase(new Chunk(""+curr1,small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//                Table14.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//                Table14.addCell(new Phrase(new Chunk(""+invoice_data.get("trans_chrg"),small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//                Table14.addCell(new Phrase(new Chunk(""+invoice_data.get("trans_chrg_amt"),small_black_normal)));
//            }

//            if(!invoice_data.get("market_margin").toString().equals(""))
//            {
//            	sr+=1;
//                Table14.setWidthPercentage(100);
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//                Table14.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//                Table14.addCell(new Phrase(new Chunk("Marketing Margin",small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//                Table14.addCell(new Phrase(new Chunk(""+curr1,small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//                Table14.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//                Table14.addCell(new Phrase(new Chunk(""+invoice_data.get("market_margin"),small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//                Table14.addCell(new Phrase(new Chunk(""+invoice_data.get("market_margin_amt"),small_black_normal)));
//            }

//            if(!invoice_data.get("oth_chrge").toString().equals(""))
//            {
//            	sr+=1;
//                Table14.setWidthPercentage(100);
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//                Table14.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//                Table14.addCell(new Phrase(new Chunk("Other Charges",small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//                Table14.addCell(new Phrase(new Chunk(""+curr1,small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//                Table14.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//                Table14.addCell(new Phrase(new Chunk(""+invoice_data.get("oth_chrge"),small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//                Table14.addCell(new Phrase(new Chunk(""+invoice_data.get("oth_chrge_amt"),small_black_normal)));
//            }

//            if(invoice_data.get("isGrossIncTriff").toString().equals("true"))
//            {
//            	sr+=1;
//                Table14.setWidthPercentage(100);
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//                Table14.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//                Table14.addCell(new Phrase(new Chunk("Total Gross Amount",small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//                Table14.addCell(new Phrase(new Chunk(""+curr1,small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//                Table14.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//                Table14.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//                Table14.addCell(new Phrase(new Chunk(""+invoice_data.get("gross_incl_chrg"),small_black_normal)));
//            }

//            if(inv_flag.equals("CP") || inv_flag.equals("CF"))
//            {
//            	sr+=1;
//                Table14.setWidthPercentage(100);
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//                Table14.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//                Table14.addCell(new Phrase(new Chunk("CIF Value of Cargo",small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//                Table14.addCell(new Phrase(new Chunk(""+curr1,small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//                Table14.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//                Table14.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//                Table14.addCell(new Phrase(new Chunk(""+invoice_data.get("CIFAmt"),small_black_normal)));
//
//                sr+=1;
//                Table14.setWidthPercentage(100);
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//                Table14.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//                Table14.addCell(new Phrase(new Chunk("Assessable Value",small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//                Table14.addCell(new Phrase(new Chunk(""+curr1,small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//                Table14.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//                Table14.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//                Table14.getDefaultCell().setBorder(Rectangle.BOX);
//                Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//                Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//                Table14.addCell(new Phrase(new Chunk(""+invoice_data.get("AssessableAmt"),small_black_normal)));
//            }

            float[] ContactAddrWidths17 = {0.055F,0.60F,0.20F,0.20F,0.30F,0.20F,0.20F};
   	     	PdfPTable Table15 = new PdfPTable(ContactAddrWidths17);
            if((!contract_type.equals("N") && !inv_flag.equals("P"))
            		|| (inv_flag.equals("CP") || inv_flag.equals("CF"))
            		|| (contract_type.equals("H") && inv_flag.equals("P")))
            {
	    		sr+=1;
	   	     	/*if(!contract_type.equals("I")) {
	   	     	Table15.getDefaultCell().setBorderWidthTop(0);
	   	     	}else {
	   	     	Table15.getDefaultCell().setBorderWidthTop(0);
	   	     	Table15.getDefaultCell().setBorderWidthBottom(0);
	   	     	}*/

	   	     	Table15.setWidthPercentage(100);
	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table15.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);

	            /*if(inv_type.equals("P"))
	            {
	            	Table15.addCell(new Phrase(new Chunk("Tax",small_black_normal)));
	            }
	            else*/
	            {
	            	Table15.addCell(new Phrase(new Chunk(tax_lineitem+" ("+invoice_data.get("TaxStructDtl")+")",small_black_normal)));
	            }

	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table15.addCell(new Phrase(new Chunk("",small_black_normal)));

	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table15.addCell(new Phrase(new Chunk(""+curr1,small_black_normal)));

	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk("",small_black_normal)));

	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk("",small_black_normal)));

	            Table15.getDefaultCell().setBorder(Rectangle.BOX);
	            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table15.addCell(new Phrase(new Chunk(total_tax,small_black_normal)));

	            if(ref_type.equals("SG"))
	            {
		            queryString1="SELECT COUNT(*) "
		            		+ "FROM FMS_PUR_SG_INV_TAX_DTL "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? AND INV_FLAG=? AND TAX_STRUCT_CD=?";
	            }
	            else
	            {
	            	queryString1="SELECT COUNT(*) "
		            		+ "FROM FMS_PUR_PG_INV_TAX_DTL "
		            		+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? AND INV_FLAG=? AND TAX_STRUCT_CD=?";
	            }
	            stmt1=conn.prepareStatement(queryString1);
	            stmt1.setString(1, comp_cd);
	            stmt1.setString(2, contract_type);
	            stmt1.setString(3, financial_year);
	            stmt1.setString(4, invoice_data.get("InvoiceSeq"));
	            stmt1.setString(5, inv_flag);
	            stmt1.setString(6, invoice_data.get("TaxStructCd"));
				rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					if(rset1.getInt(1)>1)
					{
						String temp_sub_tax = nf.format(Double.parseDouble(total_tax) / rset1.getDouble(1));
						double temp_srno=sr;
						if(ref_type.equals("SG"))
			            {
							queryString="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_PUR_SG_INV_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
									+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? AND INV_FLAG=? AND TAX_STRUCT_CD=?";
			            }
						else
						{
							queryString="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
									+ "FROM FMS_PUR_PG_INV_TAX_DTL "
									+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
									+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? AND INV_FLAG=? AND TAX_STRUCT_CD=?";
						}
						stmt=conn.prepareStatement(queryString);
			            stmt.setString(1, comp_cd);
			            stmt.setString(2, contract_type);
			            stmt.setString(3, financial_year);
			            stmt.setString(4, invoice_data.get("InvoiceSeq"));
			            stmt.setString(5, inv_flag);
			            stmt.setString(6, invoice_data.get("TaxStructCd"));
						rset=stmt.executeQuery();
						while(rset.next())
						{
							temp_srno=temp_srno+0.1;

							String tax_descr=rset.getString(2)==null?"":rset.getString(2);

							Table15.setWidthPercentage(100);
				            Table15.getDefaultCell().setBorder(Rectangle.BOX);
				            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table15.addCell(new Phrase(new Chunk(""+nf0.format(temp_srno),small_black_normal)));

				            Table15.getDefaultCell().setBorder(Rectangle.BOX);
				            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
				            Table15.addCell(new Phrase(new Chunk(""+tax_descr+"",small_black_normal)));

				            Table15.getDefaultCell().setBorder(Rectangle.BOX);
				            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table15.addCell(new Phrase(new Chunk("",small_black_normal)));

				            Table15.getDefaultCell().setBorder(Rectangle.BOX);
				            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table15.addCell(new Phrase(new Chunk(""+curr1,small_black_normal)));

				            Table15.getDefaultCell().setBorder(Rectangle.BOX);
				            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
				            Table15.addCell(new Phrase(new Chunk("",small_black_normal)));

				            Table15.getDefaultCell().setBorder(Rectangle.BOX);
				            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				            Table15.addCell(new Phrase(new Chunk("",small_black_normal)));

				            Table15.getDefaultCell().setBorder(Rectangle.BOX);
				            Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				            Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				            Table15.addCell(new Phrase(new Chunk(temp_sub_tax,small_black_normal)));
						}
						rset.close();
						stmt.close();
					}
				}
				rset1.close();
				stmt1.close();
            }

            /*float[] ContactAddrWidths23 = {0.05F,0.60F,0.20F,0.20F,0.20F,0.20F};
   	     	PdfPTable Table21 = new PdfPTable(ContactAddrWidths23);

            float[] ContactAddrWidths24 = {0.05F,0.60F,0.20F,0.20F,0.20F,0.20F};
   	     	PdfPTable Table22 = new PdfPTable(ContactAddrWidths24);
   	     	if(contract_type.equals("I"))
            {
            	sr+=1;
            	Table21.getDefaultCell().setBorderWidthTop(0);
            	Table21.getDefaultCell().setBorderWidthBottom(0);

            	Table21.setWidthPercentage(100);
                Table21.getDefaultCell().setBorder(Rectangle.BOX);
                Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
                Table21.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

                Table21.getDefaultCell().setBorder(Rectangle.BOX);
                Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);

                if(inv_type.equals("P"))
                {
                	Table21.addCell(new Phrase(new Chunk("IGX TXN Charges",small_black_normal)));
                }
                else
                {
                	Table21.addCell(new Phrase(new Chunk("IGX TXN Charges ("+invoice_data.get("TxnChrg")+"%)",small_black_normal)));
                }
                Table21.getDefaultCell().setBorder(Rectangle.BOX);
                Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
                Table21.addCell(new Phrase(new Chunk("",small_black_normal)));

                Table21.getDefaultCell().setBorder(Rectangle.BOX);
                Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
                Table21.addCell(new Phrase(new Chunk(""+curr1,small_black_normal)));

                Table21.getDefaultCell().setBorder(Rectangle.BOX);
                Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
                Table21.addCell(new Phrase(new Chunk("",small_black_normal)));

                Table21.getDefaultCell().setBorder(Rectangle.BOX);
                Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
                Table21.addCell(new Phrase(new Chunk("",small_black_normal)));

                Table21.getDefaultCell().setBorder(Rectangle.BOX);
                Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
                Table21.addCell(new Phrase(new Chunk(""+invoice_data.get("TxnAmount"),small_black_normal)));

                sr+=1;
                Table22.getDefaultCell().setBorderWidthTop(0);

       	     	Table22.setWidthPercentage(100);
                Table22.getDefaultCell().setBorder(Rectangle.BOX);
                Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
                Table22.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

                Table22.getDefaultCell().setBorder(Rectangle.BOX);
                Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);

                if(inv_type.equals("P"))
                {
                	Table22.addCell(new Phrase(new Chunk("Tax on IGX TXN",small_black_normal)));
                }
                else
                {
                	Table22.addCell(new Phrase(new Chunk("Tax on IGX TXN ("+invoice_data.get("TaxTxnDtl")+")",small_black_normal)));
                }
                Table22.getDefaultCell().setBorder(Rectangle.BOX);
                Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
                Table22.addCell(new Phrase(new Chunk("",small_black_normal)));

                Table22.getDefaultCell().setBorder(Rectangle.BOX);
                Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
                Table22.addCell(new Phrase(new Chunk(""+curr1,small_black_normal)));

                Table22.getDefaultCell().setBorder(Rectangle.BOX);
                Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
                Table22.addCell(new Phrase(new Chunk("",small_black_normal)));

                Table22.getDefaultCell().setBorder(Rectangle.BOX);
                Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
                Table22.addCell(new Phrase(new Chunk("",small_black_normal)));

                Table22.getDefaultCell().setBorder(Rectangle.BOX);
                Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
                Table22.addCell(new Phrase(new Chunk(""+invoice_data.get("TaxTxnChrg"),small_black_normal)));
            }*/


            float[] ContactAddrWidths18 = {0.055F,0.60F,0.20F,0.20F,0.30F,0.20F,0.20F};
   	     	PdfPTable Table16 = new PdfPTable(ContactAddrWidths18);
   	     	//Table16.getDefaultCell().setBorderWidthBottom(0);

   	     	if(inv_flag.equals("CF"))
   	     	{
   	     		sr+=1;
	   	     	Table16.setWidthPercentage(100);
	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table16.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table16.addCell(new Phrase(new Chunk("Custom Duty Paid",small_black_normal)));

	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table16.addCell(new Phrase(new Chunk(""+curr1,small_black_normal)));

	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk("",small_black_normal)));

	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk("",small_black_normal)));

	            Table16.getDefaultCell().setBorder(Rectangle.BOX);
	            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table16.addCell(new Phrase(new Chunk(""+invoice_data.get("CdPaidAmt"),small_black_normal)));

   	     	}

   	     	sr+=1;
   	     	Table16.setWidthPercentage(100);
            Table16.getDefaultCell().setBorder(Rectangle.BOX);
            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
            Table16.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

            Table16.getDefaultCell().setBorder(Rectangle.BOX);
            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            Table16.addCell(new Phrase(new Chunk(inv_lineitem+" Amount",small_black_normal)));

            Table16.getDefaultCell().setBorder(Rectangle.BOX);
            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
            Table16.addCell(new Phrase(new Chunk("",small_black_normal)));

            Table16.getDefaultCell().setBorder(Rectangle.BOX);
            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
            Table16.addCell(new Phrase(new Chunk(""+curr1,small_black_normal)));

            Table16.getDefaultCell().setBorder(Rectangle.BOX);
            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
            Table16.addCell(new Phrase(new Chunk("",small_black_normal)));

            Table16.getDefaultCell().setBorder(Rectangle.BOX);
            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
            Table16.addCell(new Phrase(new Chunk("",small_black_normal)));

            Table16.getDefaultCell().setBorder(Rectangle.BOX);
            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
            Table16.addCell(new Phrase(new Chunk(total_amt,small_black_normal)));

//            if(invoice_data.get("ApplicableFlag").equals("Y"))
//   	     	{
//            	sr+=1;
//
//            	Table16.setWidthPercentage(100);
//                Table16.getDefaultCell().setBorder(Rectangle.BOX);
//                Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//                Table16.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
//
//                Table16.getDefaultCell().setBorder(Rectangle.BOX);
//                Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//                Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//                Table16.addCell(new Phrase(new Chunk("TCS",small_black_normal)));
//
//                Table16.getDefaultCell().setBorder(Rectangle.BOX);
//                Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//                Table16.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//                Table16.getDefaultCell().setBorder(Rectangle.BOX);
//                Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//                Table16.addCell(new Phrase(new Chunk(""+curr1,small_black_normal)));
//
//                Table16.getDefaultCell().setBorder(Rectangle.BOX);
//                Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//                Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//                Table16.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//                Table16.getDefaultCell().setBorder(Rectangle.BOX);
//                Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//                Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//                if(inv_type.equals("S"))
//                {
//                	Table16.addCell(new Phrase(new Chunk(""+invoice_data.get("TcsFactor")+"%",small_black_normal)));
//                }
//                else
//                {
//                	Table16.addCell(new Phrase(new Chunk("",small_black_normal)));
//                }
//
//                Table16.getDefaultCell().setBorder(Rectangle.BOX);
//                Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//                Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//                Table16.addCell(new Phrase(new Chunk(""+invoice_data.get("TcsAmt"),small_black_normal)));
//   	     	}

//            float[] ContactAddrWidths19 = {0.05F,0.60F,0.20F,0.20F,0.30F,0.20F,0.20F};
//   	     	PdfPTable Table17 = new PdfPTable(ContactAddrWidths19);
   	     	//Table17.getDefaultCell().setBorderWidthTop(0);
//   	     	if(!invoice_data.get("AdjustSign").equals(""))
//   	     	{
//   	     		sr+=1;
//
//	   	     	Table17.setWidthPercentage(100);
//	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
//	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//	            Table17.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
//
//	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
//	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//	            Table17.addCell(new Phrase(new Chunk("Adjustment",small_black_normal)));
//
//	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
//	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//	            Table17.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
//	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//	            Table17.addCell(new Phrase(new Chunk(""+curr1,small_black_normal)));
//
//	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
//	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//	            Table17.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
//	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//	            Table17.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
//	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//	            Table17.addCell(new Phrase(new Chunk(""+invoice_data.get("AdjustSign")+""+invoice_data.get("AdjustAmt"),small_black_normal)));
//   	     	}

            sr+=1;
            float[] ContactAddrWidths20 = {0.055F,0.60F,0.20F,0.20F,0.30F,0.20F,0.20F};
   	     	PdfPTable Table18 = new PdfPTable(ContactAddrWidths20);

   	     	Table18.setWidthPercentage(100);
            Table18.getDefaultCell().setBorder(Rectangle.BOX);
            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
            Table18.addCell(new Phrase(new Chunk(""+sr,black_bold)));

            Table18.getDefaultCell().setBorder(Rectangle.BOX);
            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            Table18.addCell(new Phrase(new Chunk("Net Amount Payable",black_bold)));

            Table18.getDefaultCell().setBorder(Rectangle.BOX);
            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
            Table18.addCell(new Phrase(new Chunk("",black_bold)));

            Table18.getDefaultCell().setBorder(Rectangle.BOX);
            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
            Table18.addCell(new Phrase(new Chunk(""+curr1,black_bold)));

            Table18.getDefaultCell().setBorder(Rectangle.BOX);
            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
            Table18.addCell(new Phrase(new Chunk("",black_bold)));

            Table18.getDefaultCell().setBorder(Rectangle.BOX);
            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
            Table18.addCell(new Phrase(new Chunk("",black_bold)));

            Table18.getDefaultCell().setBorder(Rectangle.BOX);
            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
            Table18.addCell(new Phrase(new Chunk(total_amt,black_bold)));

            float[] ContactAddrWidths23 = {0.100F};
	     	PdfPTable Table21 = new PdfPTable(ContactAddrWidths23);
	     	Table21.setWidthPercentage(100);
	     	Table21.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	     	Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	     	Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	     	Table21.addCell(new Phrase(new Chunk(str_cargoname,small_black_normal)));

            /*float[] ContactAddrWidths21 = {0.100F};
   	     	PdfPTable Table19 = new PdfPTable(ContactAddrWidths21);

   	     	Table19.setWidthPercentage(100);
   	     	Table19.getDefaultCell().setBorder(Rectangle.NO_BORDER);
   	     	Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
   	     	Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
   	     	Table19.addCell(new Phrase(new Chunk("For "+company_nm,black_bold)));

   	     	float[] ContactAddrWidths22 = {0.100F};
	     	PdfPTable Table20 = new PdfPTable(ContactAddrWidths22);

	     	Table20.setWidthPercentage(100);
	     	Table20.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	     	Table20.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	     	Table20.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	     	Table20.addCell(new Phrase(new Chunk("Authorised Signatory",black_bold)));
             */

	     	document.add(LogoTable);
			document.add(HlplLogoTable);
			document.add(Table);
			document.add(Table1);
			if(!inv_flag.equals("CP") && !inv_flag.equals("CF"))
			{
				document.add(Table2);
			}
			document.add(new Paragraph("  "));
			document.add(Table3);
			document.add(Table4);
			document.add(Table5);
			document.add(Table6);
			document.add(Table7);
			document.add(new Paragraph("  "));
			document.add(new Paragraph("  "));
			document.add(Table8);
			document.add(Table9);
			document.add(Table8_1);

			document.add(Table10);
//			if(!inv_flag.equals("UG") && !contract_type.equals("Y") && !contract_type.equals("H") && !contract_type.equals("A"))
//            { //BILLING PERIOD NOT REQUIRED FOR SUG INVOICE
////			document.add(Table10_1);
//			}
			document.add(new Paragraph("  "));
			document.add(Table11);
			document.add(Table12);
			if(!invoice_data.get("PriceCd").equals(invoice_data.get("InvoiceRaisedIn")))
			{
				document.add(Table13);
			}
			document.add(Table14);
//			 if(invoice_data.get("InvoiceRaisedIn").equals(invoice_data.get("PriceCd")))
//	         {
//			document.add(Table14_1);
//	         }
			document.add(Table15);
			/*if(contract_type.equals("I"))
            {
				document.add(Table21);
				document.add(Table22);
            }*/
			document.add(Table16);
//			if(!invoice_data.get("AdjustSign").equals(""))
//   	     	{
//				document.add(Table17);
//   	     	}
			document.add(Table18);
			if(contract_type.equals("N") || inv_flag.equals("UG")) {
			document.add(new Paragraph("  "));
			document.add(new Paragraph("  "));
			document.add(Table21);
			}
			document.add(new Paragraph("  "));
			document.add(new Paragraph("  "));
			//document.add(Table19);
			document.add(new Paragraph("  "));
			document.add(new Paragraph("  "));
			//document.add(Table20);
			document.add(new Paragraph("  "));
			document.add(new Paragraph("  "));
			document.add(new Paragraph("  "));
			document.add(new Paragraph("  "));

			// Adding content to the document
			float[] footer = {0.100f};
   	     	PdfPTable footer_tab = new PdfPTable(footer);
   	     	footer_tab.setWidthPercentage(100);
   	     	footer_tab.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		   	footer_tab.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		   	footer_tab.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		   	footer_tab.addCell(new Phrase(new Chunk("*** This is Computer Generated Report and No Signature Required ***",small_black_normal)));

		    document.add(footer_tab);

		  //For Attachment 1
			Font big_black_bold = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD,  BaseColor.BLACK);
			document.setPageSize(pageSize1);
            document.newPage();
		    if(drcr_criteria.equals("1")) {

	            float[] att1_ContactAddrWidths1 = {0.80f,0.10F,0.20F,0.80F};
	   	     	PdfPTable att1_Table1 = new PdfPTable(att1_ContactAddrWidths1);
	            att1_Table1.setWidthPercentage(100);
	            att1_Table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            //att1_Table1.addCell(new Phrase(new Chunk("Business Unit: "+bu_plantNm,black_bold)));
	            att1_Table1.addCell(new Phrase(new Chunk("Business Unit: ",black_bold)));

	            att1_Table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table1.addCell(new Phrase(new Chunk("",black_bold)));

	            att1_Table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table1.addCell(new Phrase(new Chunk("To:",black_bold)));

	            att1_Table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table1.addCell(new Phrase(new Chunk("",black_bold)));

				float[] att1_ContactAddrWidths2 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable att1_Table2 = new PdfPTable(att1_ContactAddrWidths2);
	   	     	att1_Table2.setWidthPercentage(100);
	            att1_Table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table2.addCell(new Phrase(new Chunk(company_nm,small_black_normal)));

	            att1_Table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table2.addCell(new Phrase(new Chunk("",small_black_normal)));

	            att1_Table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table2.addCell(new Phrase(new Chunk(counterparty_nm,small_black_normal)));

	            float[] att1_ContactAddrWidths3 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable att1_Table3 = new PdfPTable(att1_ContactAddrWidths3);
	            att1_Table3.setWidthPercentage(100);
	            att1_Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table3.addCell(new Phrase(new Chunk(bu_plantAddress+","+bu_plantCity,small_black_normal)));

	            att1_Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table3.addCell(new Phrase(new Chunk("",small_black_normal)));

	            att1_Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table3.addCell(new Phrase(new Chunk(plantAddress+","+plantCity,small_black_normal)));


	            float[] att1_ContactAddrWidths4 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable att1_Table4 = new PdfPTable(att1_ContactAddrWidths4);
	            att1_Table4.setWidthPercentage(100);
	            att1_Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table4.addCell(new Phrase(new Chunk(bu_plantState+" - "+bu_plantPin,small_black_normal)));

	            att1_Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table4.addCell(new Phrase(new Chunk("",small_black_normal)));

	            att1_Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table4.addCell(new Phrase(new Chunk(plantState+" - "+plantPin,small_black_normal)));


	            String title_note="ATTACHMENT 1 - Applicable Exchange Rate";
//	            if(inv_flag.equals("ST"))
//	            {
//	            	title_note="ATTACHMENT 1 - Storage Inventory & Storage Charges";
//	            }

	            PdfPTable title_note_table = new PdfPTable(1);
	            title_note_table.setWidthPercentage(100);
	            title_note_table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            title_note_table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            title_note_table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            title_note_table.addCell(new Phrase(new Chunk(title_note,big_black_bold)));

	            float[] att1_ContactAddrWidths5 = {0.80F,0.30F,0.30F,0.40F};
	   	     	PdfPTable att1_Table5 = new PdfPTable(att1_ContactAddrWidths5);
	            att1_Table5.setWidthPercentage(100);
	            att1_Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            att1_Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            att1_Table5.addCell(new Phrase(new Chunk("",small_black_normal)));

	            att1_Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            att1_Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            att1_Table5.addCell(new Phrase(new Chunk("",small_black_normal)));

	            att1_Table5.getDefaultCell().setBorder(Rectangle.BOX);
	            att1_Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table5.addCell(new Phrase(new Chunk(type+" Note Date :",black_bold)));

	            att1_Table5.getDefaultCell().setBorder(Rectangle.BOX);
	            att1_Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            //att1_Table5.addCell(new Phrase(new Chunk(dateUtil.getDateFormatDD_MOM_YY(invoice_dt),small_black_normal)));
	            att1_Table5.addCell(new Phrase(new Chunk(drcr_dt,black_bold)));

	            float[] att1_ContactAddrWidths6 = {0.80F,0.30F,0.30F,0.40F};
	   	     	PdfPTable att1_Table6 = new PdfPTable(att1_ContactAddrWidths6);
	            if(drcr_due_dt != null && !drcr_due_dt.equals("")) {
	            att1_Table6.setWidthPercentage(100);
	            att1_Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            att1_Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            att1_Table6.addCell(new Phrase(new Chunk("",small_black_normal)));

	            att1_Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            att1_Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            att1_Table6.addCell(new Phrase(new Chunk("",small_black_normal)));

	            att1_Table6.getDefaultCell().setBorder(Rectangle.BOX);
	            att1_Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table6.addCell(new Phrase(new Chunk(type+" Due Date :",black_bold)));

	            att1_Table6.getDefaultCell().setBorder(Rectangle.BOX);
	            att1_Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            //att1_Table6.addCell(new Phrase(new Chunk(dateUtil.getDateFormatDD_MOM_YY(invoice_due_dt),small_black_normal)));
	            att1_Table6.addCell(new Phrase(new Chunk(drcr_due_dt,black_bold)));
	            }

	            float[] att1_ContactAddrWidths7 = {0.80F,0.30F,0.30F,0.40F};
	   	     	PdfPTable att1_Table7 = new PdfPTable(att1_ContactAddrWidths7);
	            att1_Table7.setWidthPercentage(100);
	            att1_Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            att1_Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            att1_Table7.addCell(new Phrase(new Chunk("",black_bold)));

	            att1_Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            att1_Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            att1_Table7.addCell(new Phrase(new Chunk("",small_black_normal)));

	            att1_Table7.getDefaultCell().setBorder(Rectangle.BOX);
	            att1_Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table7.addCell(new Phrase(new Chunk(type+" Note No:",black_bold)));

	            att1_Table7.getDefaultCell().setBorder(Rectangle.BOX);
	            att1_Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table7.addCell(new Phrase(new Chunk(drcr_no,black_bold)));

//	            float[] att1_ContactAddrWidths8 = {0.80F,0.30F,0.20F,0.30F};
//	   	     	PdfPTable att1_Table8 = new PdfPTable(att1_ContactAddrWidths8);
//	   	     	att1_Table8.setWidthPercentage(100);
//	            att1_Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//	            att1_Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//	            att1_Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//	            att1_Table8.addCell(new Phrase(new Chunk("",black_bold)));
//
//	            att1_Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//	            att1_Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//	            att1_Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//	            att1_Table8.addCell(new Phrase(new Chunk("",black_bold)));
//
//	            att1_Table8.getDefaultCell().setBorder(Rectangle.BOX);
//	            att1_Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//	            att1_Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//	            att1_Table8.addCell(new Phrase(new Chunk("Billing Period : ",black_bold)));
//
//	            att1_Table8.getDefaultCell().setBorder(Rectangle.BOX);
//	            att1_Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//	            att1_Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//	            att1_Table8.addCell(new Phrase(new Chunk(""+period_start_dt+" to "+period_end_dt,black_bold)));


	            if(!invoice_data.get("InvoiceRaisedIn").equals(invoice_data.get("PriceCd")))
	            {
	            	String component_flag="";
	            	String component1="";
	            	String component2="";
	            	String exchang_rate_nm="";
	            	String component1_rate_nm="";
	            	String component2_rate_nm="";
	            	String component1_rate="";
	            	String component2_rate="";

	            	queryString="SELECT COMPONENT_FLAG,COMPONENT1,COMPONENT2,EXC_RATE_NM,BANK_ABBR "
							+ "FROM FMS_EXCHG_RATE_MST "
							+ "WHERE EXC_RATE_CD=?";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, invoice_data.get("ExchangRateCd"));
	            	rset=stmt.executeQuery();
					if(rset.next())
					{
						component_flag=rset.getString(1)==null?"":rset.getString(1);
						component1=rset.getString(2)==null?"":rset.getString(2);
						component2=rset.getString(3)==null?"":rset.getString(3);
						exchang_rate_nm=rset.getString(4)==null?"":rset.getString(4);
						String source=rset.getString(5)==null?"":rset.getString(5);
					}
					rset.close();
					stmt.close();

						queryString1="SELECT EXC_RATE_NM "
								+ "FROM FMS_EXCHG_RATE_MST "
								+ "WHERE EXC_RATE_CD=?";
						stmt1=conn.prepareStatement(queryString1);
						stmt1.setString(1, component1);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							component1_rate_nm=rset1.getString(1)==null?"":rset1.getString(1);
						}
						rset1.close();
						stmt1.close();

						queryString2="SELECT EXC_RATE_NM "
								+ "FROM FMS_EXCHG_RATE_MST "
								+ "WHERE EXC_RATE_CD=?";
						stmt2=conn.prepareStatement(queryString2);
						stmt2.setString(1, component2);
						rset2=stmt2.executeQuery();
						if(rset2.next())
						{
							component2_rate_nm=rset2.getString(1)==null?"":rset2.getString(1);
						}
						rset2.close();
						stmt2.close();

						queryString2="SELECT EXCHG_VAL "
								+ "FROM FMS_EXCHG_RATE_ENTRY "
								+ "WHERE EXCHG_RATE_CD=? "
								+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY')";
						stmt2=conn.prepareStatement(queryString2);
						stmt2.setString(1, component1);
						stmt2.setString(2, invoice_data.get("ExchangRateDt"));
						rset2=stmt2.executeQuery();
						if(rset2.next())
						{
							component1_rate=nf2.format(rset2.getDouble(1));
						}
						rset2.close();
						stmt2.close();

						queryString2="SELECT EXCHG_VAL "
								+ "FROM FMS_EXCHG_RATE_ENTRY "
								+ "WHERE EXCHG_RATE_CD=? "
								+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY')";
						stmt2=conn.prepareStatement(queryString2);
						stmt2.setString(1, component2);
						stmt2.setString(2, invoice_data.get("ExchangRateDt"));
						rset2=stmt2.executeQuery();
						if(rset2.next())
						{
							component2_rate=nf2.format(rset2.getDouble(1));
						}
						rset2.close();
						stmt2.close();

	            	sr=1;
	            	//float[] att1_ContactAddrWidths9 = {0.1F, 0.05F, 0.02F, 0.15F};
	            	float[] att1_ContactAddrWidths9 = {0.002F, 0.05F, 0.02F, 0.15F};
		   	     	PdfPTable att1_Table9 = new PdfPTable(att1_ContactAddrWidths9);
		   	     	att1_Table9.setWidthPercentage(100);

		   	     	att1_Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
		            att1_Table9.addCell(new Phrase(new Chunk("",small_black_normal)));

		            att1_Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
		            att1_Table9.addCell(new Phrase(new Chunk(exchang_rate_nm+".. On "+invoice_data.get("InvoiceDate"),small_black_normal)));

		            att1_Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
		            att1_Table9.addCell(new Phrase(new Chunk(invoice_data.get("ExchangRate"),small_black_normal)));

		            att1_Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
		            att1_Table9.addCell(new Phrase(new Chunk(curr,small_black_normal)));

//		            sr+=1;
//		            float[] att1_ContactAddrWidths10 = {0.002F, 0.05F, 0.02F, 0.15F};
//		   	     	PdfPTable att1_Table10 = new PdfPTable(att1_ContactAddrWidths10);
//		   	     	att1_Table10.setWidthPercentage(100);
//
//		   	     	att1_Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//		            att1_Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//		            att1_Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
//		            att1_Table10.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
//
//		            att1_Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//		            att1_Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//		            att1_Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
//		            att1_Table10.addCell(new Phrase(new Chunk("Applicable Exchange Rate",small_black_normal)));
//
//		            att1_Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//		            att1_Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//		            att1_Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
//		            att1_Table10.addCell(new Phrase(new Chunk(item_diff_amt,small_black_normal)));
//
//		            att1_Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//		            att1_Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//		            att1_Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
//		            att1_Table10.addCell(new Phrase(new Chunk(curr,small_black_normal)));


		            float[] att1_ContactAddrWidths11 = {0.002F, 0.05F, 0.02F, 0.15F};
		   	     	PdfPTable att1_Table11 = new PdfPTable(att1_ContactAddrWidths11);
		   	     	att1_Table11.setWidthPercentage(100);

		   	     	att1_Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
		            att1_Table11.addCell(new Phrase(new Chunk("",small_black_normal)));

		            att1_Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
		            att1_Table11.addCell(new Phrase(new Chunk("Difference in Exchange Rate",black_bold)));

		            att1_Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
		            att1_Table11.addCell(new Phrase(new Chunk(item_value,black_bold)));

		            att1_Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
		            att1_Table11.addCell(new Phrase(new Chunk(curr,small_black_normal)));


		            document.add(LogoTable);
		            document.add(att1_Table1);
		            document.add(att1_Table2);
		            document.add(att1_Table3);
		            document.add(att1_Table4);
		            document.add(new Paragraph("              "));
		            document.add(title_note_table);
		            document.add(new Paragraph("              "));
		            document.add(att1_Table5);
		            if(drcr_due_dt != null && !drcr_due_dt.equals("")) {
		            document.add(att1_Table6);
		            }
		            document.add(att1_Table7);
//		            if(!inv_flag.equals("ST"))
		            {
//		            	document.add(att1_Table8);
		            	document.add(new Paragraph("              "));
		            	document.add(att1_Table9);
//		            	document.add(att1_Table10);
		            	document.add(new Paragraph("              "));
		            	document.add(att1_Table11);
		            }
	            }



			}
			else if(drcr_criteria.equals("2"))
			{

	            float[] att1_ContactAddrWidths1 = {0.80f,0.10F,0.20F,0.80F};
	   	     	PdfPTable att1_Table1 = new PdfPTable(att1_ContactAddrWidths1);
	            att1_Table1.setWidthPercentage(100);
	            att1_Table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            //att1_Table1.addCell(new Phrase(new Chunk("Business Unit: "+bu_plantNm,black_bold)));
	            att1_Table1.addCell(new Phrase(new Chunk("Business Unit: ",black_bold)));

	            att1_Table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table1.addCell(new Phrase(new Chunk("",black_bold)));

	            att1_Table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table1.addCell(new Phrase(new Chunk("To:",black_bold)));

	            att1_Table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table1.addCell(new Phrase(new Chunk("",black_bold)));

				float[] att1_ContactAddrWidths2 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable att1_Table2 = new PdfPTable(att1_ContactAddrWidths2);
	   	     	att1_Table2.setWidthPercentage(100);
	            att1_Table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table2.addCell(new Phrase(new Chunk(company_nm,small_black_normal)));

	            att1_Table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table2.addCell(new Phrase(new Chunk("",small_black_normal)));

	            att1_Table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table2.addCell(new Phrase(new Chunk(counterparty_nm,small_black_normal)));

	            float[] att1_ContactAddrWidths3 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable att1_Table3 = new PdfPTable(att1_ContactAddrWidths3);
	            att1_Table3.setWidthPercentage(100);
	            att1_Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table3.addCell(new Phrase(new Chunk(bu_plantAddress+","+bu_plantCity,small_black_normal)));

	            att1_Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table3.addCell(new Phrase(new Chunk("",small_black_normal)));

	            att1_Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table3.addCell(new Phrase(new Chunk(plantAddress+","+plantCity,small_black_normal)));


	            float[] att1_ContactAddrWidths4 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable att1_Table4 = new PdfPTable(att1_ContactAddrWidths4);
	            att1_Table4.setWidthPercentage(100);
	            att1_Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table4.addCell(new Phrase(new Chunk(bu_plantState+" - "+bu_plantPin,small_black_normal)));

	            att1_Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table4.addCell(new Phrase(new Chunk("",small_black_normal)));

	            att1_Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table4.addCell(new Phrase(new Chunk(plantState+" - "+plantPin,small_black_normal)));

	            String title_note="ATTACHMENT 1 - Applicable Price";

	            PdfPTable title_note_table = new PdfPTable(1);
	            title_note_table.setWidthPercentage(100);
	            title_note_table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            title_note_table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            title_note_table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            title_note_table.addCell(new Phrase(new Chunk(title_note,big_black_bold)));

	            float[] att1_ContactAddrWidths5 = {0.80F,0.30F,0.30F,0.40F};
	   	     	PdfPTable att1_Table5 = new PdfPTable(att1_ContactAddrWidths5);
	            att1_Table5.setWidthPercentage(100);
	            att1_Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            att1_Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            att1_Table5.addCell(new Phrase(new Chunk("",small_black_normal)));

	            att1_Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            att1_Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            att1_Table5.addCell(new Phrase(new Chunk("",small_black_normal)));

	            att1_Table5.getDefaultCell().setBorder(Rectangle.BOX);
	            att1_Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table5.addCell(new Phrase(new Chunk(type+" Note Date :",black_bold)));

	            att1_Table5.getDefaultCell().setBorder(Rectangle.BOX);
	            att1_Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            //att1_Table5.addCell(new Phrase(new Chunk(dateUtil.getDateFormatDD_MOM_YY(invoice_dt),small_black_normal)));
	            att1_Table5.addCell(new Phrase(new Chunk(drcr_dt,black_bold)));

	            float[] att1_ContactAddrWidths6 = {0.80F,0.30F,0.30F,0.40F};
	   	     	PdfPTable att1_Table6 = new PdfPTable(att1_ContactAddrWidths6);
	   	     if(drcr_due_dt != null && !drcr_due_dt.equals("")) {
	            att1_Table6.setWidthPercentage(100);
	            att1_Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            att1_Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            att1_Table6.addCell(new Phrase(new Chunk("",small_black_normal)));

	            att1_Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            att1_Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            att1_Table6.addCell(new Phrase(new Chunk("",small_black_normal)));

	            att1_Table6.getDefaultCell().setBorder(Rectangle.BOX);
	            att1_Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table6.addCell(new Phrase(new Chunk(type+" Due Date :",black_bold)));

	            att1_Table6.getDefaultCell().setBorder(Rectangle.BOX);
	            att1_Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            //att1_Table6.addCell(new Phrase(new Chunk(dateUtil.getDateFormatDD_MOM_YY(invoice_due_dt),small_black_normal)));
	            att1_Table6.addCell(new Phrase(new Chunk(drcr_due_dt,black_bold)));
	   	     }

	            float[] att1_ContactAddrWidths7 = {0.80F,0.30F,0.30F,0.40F};
	   	     	PdfPTable att1_Table7 = new PdfPTable(att1_ContactAddrWidths7);
	            att1_Table7.setWidthPercentage(100);
	            att1_Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            att1_Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            att1_Table7.addCell(new Phrase(new Chunk("",black_bold)));

	            att1_Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            att1_Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            att1_Table7.addCell(new Phrase(new Chunk("",small_black_normal)));

	            att1_Table7.getDefaultCell().setBorder(Rectangle.BOX);
	            att1_Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table7.addCell(new Phrase(new Chunk(type+" Note No:",black_bold)));

	            att1_Table7.getDefaultCell().setBorder(Rectangle.BOX);
	            att1_Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table7.addCell(new Phrase(new Chunk(drcr_no,black_bold)));

				/*float[] att1_ContactAddrWidths8 = {0.80F,0.30F,0.20F,0.30F};
				PdfPTable att1_Table8 = new PdfPTable(att1_ContactAddrWidths8);
				att1_Table8.setWidthPercentage(100);
				att1_Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				att1_Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				att1_Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
				att1_Table8.addCell(new Phrase(new Chunk("",black_bold)));

				att1_Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				att1_Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				att1_Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
				att1_Table8.addCell(new Phrase(new Chunk("",black_bold)));

				att1_Table8.getDefaultCell().setBorder(Rectangle.BOX);
				att1_Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				att1_Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
				att1_Table8.addCell(new Phrase(new Chunk("Billing Period : ",black_bold)));

				att1_Table8.getDefaultCell().setBorder(Rectangle.BOX);
				att1_Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				att1_Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
				att1_Table8.addCell(new Phrase(new Chunk(""+period_start_dt+" to "+period_end_dt,black_bold)));*/


//	            if(!invoice_data.get("InvoiceRaisedIn").equals(invoice_data.get("PriceCd")))
	            {
//	            	sr=1;
	            	//float[] att1_ContactAddrWidths9 = {0.1F, 0.05F, 0.02F, 0.15F};
	            	float[] att1_ContactAddrWidths9 = {0.002F, 0.07F, 0.02F, 0.15F};
		   	     	PdfPTable att1_Table9 = new PdfPTable(att1_ContactAddrWidths9);
		   	     	att1_Table9.setWidthPercentage(100);

		   	     	att1_Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
		            att1_Table9.addCell(new Phrase(new Chunk("",small_black_normal)));

		            att1_Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
		            att1_Table9.addCell(new Phrase(new Chunk("Invoice Price",small_black_normal)));

		            att1_Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
		            att1_Table9.addCell(new Phrase(new Chunk(invoice_data.get("Price"),small_black_normal)));

		            att1_Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
		            att1_Table9.addCell(new Phrase(new Chunk(curr,small_black_normal)));

//		            sr+=1;
//		            float[] att1_ContactAddrWidths10 = {0.002F, 0.05F, 0.02F, 0.15F};
//		   	     	PdfPTable att1_Table10 = new PdfPTable(att1_ContactAddrWidths10);
//		   	     	att1_Table10.setWidthPercentage(100);
//
//		   	     	att1_Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//		            att1_Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//		            att1_Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
//		            att1_Table10.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
//
//		            att1_Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//		            att1_Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//		            att1_Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
//		            att1_Table10.addCell(new Phrase(new Chunk("Applicable Sales Price Rate",small_black_normal)));
//
//		            att1_Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//		            att1_Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//		            att1_Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
//		            att1_Table10.addCell(new Phrase(new Chunk(item_diff_amt,small_black_normal)));
//
//		            att1_Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//		            att1_Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//		            att1_Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
//		            att1_Table10.addCell(new Phrase(new Chunk(curr,small_black_normal)));


		            float[] att1_ContactAddrWidths11 = {0.002F, 0.07F, 0.02F, 0.15F};
		   	     	PdfPTable att1_Table11 = new PdfPTable(att1_ContactAddrWidths11);
		   	     	att1_Table11.setWidthPercentage(100);

		   	     	att1_Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
		            att1_Table11.addCell(new Phrase(new Chunk("",small_black_normal)));

		            att1_Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
		            att1_Table11.addCell(new Phrase(new Chunk("Difference in Price",black_bold)));

		            att1_Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
		            att1_Table11.addCell(new Phrase(new Chunk(item_value,black_bold)));

		            att1_Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
		            att1_Table11.addCell(new Phrase(new Chunk(curr,small_black_normal)));


		            document.add(LogoTable);
		            document.add(att1_Table1);
		            document.add(att1_Table2);
		            document.add(att1_Table3);
		            document.add(att1_Table4);
		            document.add(new Paragraph("              "));
		            document.add(title_note_table);
		            document.add(new Paragraph("              "));
		            document.add(att1_Table5);
		            if(drcr_due_dt != null && !drcr_due_dt.equals("")) {
		            document.add(att1_Table6);
		            }
		            document.add(att1_Table7);
//		            if(!inv_flag.equals("ST"))
		            {
//		            	document.add(att1_Table8);
		            	document.add(new Paragraph("              "));
		            	document.add(att1_Table9);
//		            	document.add(att1_Table10);
		            	document.add(new Paragraph("              "));
		            	document.add(att1_Table11);
		            }
	            }
			}

			else if(drcr_criteria.equals("3"))
			{

				float[] att1_ContactAddrWidths1 = {0.80f,0.10F,0.20F,0.80F};
				PdfPTable att1_Table1 = new PdfPTable(att1_ContactAddrWidths1);
				att1_Table1.setWidthPercentage(100);
				att1_Table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				att1_Table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				att1_Table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				//att1_Table1.addCell(new Phrase(new Chunk("Business Unit: "+bu_plantNm,black_bold)));
				att1_Table1.addCell(new Phrase(new Chunk("Business Unit: ",black_bold)));

				att1_Table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				att1_Table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				att1_Table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				att1_Table1.addCell(new Phrase(new Chunk("",black_bold)));

				att1_Table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				att1_Table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				att1_Table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				att1_Table1.addCell(new Phrase(new Chunk("To:",black_bold)));

				att1_Table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				att1_Table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				att1_Table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				att1_Table1.addCell(new Phrase(new Chunk("",black_bold)));

				float[] att1_ContactAddrWidths2 = {0.80f,0.30F,0.80F};
				PdfPTable att1_Table2 = new PdfPTable(att1_ContactAddrWidths2);
				att1_Table2.setWidthPercentage(100);
				att1_Table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				att1_Table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				att1_Table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				att1_Table2.addCell(new Phrase(new Chunk(company_nm,small_black_normal)));

				att1_Table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				att1_Table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				att1_Table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				att1_Table2.addCell(new Phrase(new Chunk("",small_black_normal)));

				att1_Table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				att1_Table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				att1_Table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				att1_Table2.addCell(new Phrase(new Chunk(counterparty_nm,small_black_normal)));

				float[] att1_ContactAddrWidths3 = {0.80f,0.30F,0.80F};
				PdfPTable att1_Table3 = new PdfPTable(att1_ContactAddrWidths3);
				att1_Table3.setWidthPercentage(100);
				att1_Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				att1_Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				att1_Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				att1_Table3.addCell(new Phrase(new Chunk(bu_plantAddress+","+bu_plantCity,small_black_normal)));

				att1_Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				att1_Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				att1_Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				att1_Table3.addCell(new Phrase(new Chunk("",small_black_normal)));

				att1_Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				att1_Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				att1_Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				att1_Table3.addCell(new Phrase(new Chunk(plantAddress+","+plantCity,small_black_normal)));


				float[] att1_ContactAddrWidths4 = {0.80f,0.30F,0.80F};
				PdfPTable att1_Table4 = new PdfPTable(att1_ContactAddrWidths4);
				att1_Table4.setWidthPercentage(100);
				att1_Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				att1_Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				att1_Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				att1_Table4.addCell(new Phrase(new Chunk(bu_plantState+" - "+bu_plantPin,small_black_normal)));

				att1_Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				att1_Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				att1_Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				att1_Table4.addCell(new Phrase(new Chunk("",small_black_normal)));

				att1_Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				att1_Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				att1_Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				att1_Table4.addCell(new Phrase(new Chunk(plantState+" - "+plantPin,small_black_normal)));

				String title_note="ATTACHMENT 1 - Applicable Quantity";

				PdfPTable title_note_table = new PdfPTable(1);
				title_note_table.setWidthPercentage(100);
				title_note_table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				title_note_table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				title_note_table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				title_note_table.addCell(new Phrase(new Chunk(title_note,big_black_bold)));

				float[] att1_ContactAddrWidths5 = {0.80F,0.30F,0.30F,0.40F};
				PdfPTable att1_Table5 = new PdfPTable(att1_ContactAddrWidths5);
				att1_Table5.setWidthPercentage(100);
				att1_Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				att1_Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				att1_Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
				att1_Table5.addCell(new Phrase(new Chunk("",small_black_normal)));

				att1_Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				att1_Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				att1_Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
				att1_Table5.addCell(new Phrase(new Chunk("",small_black_normal)));

				att1_Table5.getDefaultCell().setBorder(Rectangle.BOX);
				att1_Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				att1_Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				att1_Table5.addCell(new Phrase(new Chunk(type+" Note Date :",black_bold)));

				att1_Table5.getDefaultCell().setBorder(Rectangle.BOX);
				att1_Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				att1_Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				//att1_Table5.addCell(new Phrase(new Chunk(dateUtil.getDateFormatDD_MOM_YY(invoice_dt),small_black_normal)));
				att1_Table5.addCell(new Phrase(new Chunk(drcr_dt,black_bold)));

	            float[] att1_ContactAddrWidths6 = {0.80F,0.30F,0.30F,0.40F};
	   	     	PdfPTable att1_Table6 = new PdfPTable(att1_ContactAddrWidths6);
	   	     	if(drcr_due_dt != null && !drcr_due_dt.equals("")) {
	            att1_Table6.setWidthPercentage(100);
	            att1_Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            att1_Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            att1_Table6.addCell(new Phrase(new Chunk("",small_black_normal)));

	            att1_Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            att1_Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            att1_Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            att1_Table6.addCell(new Phrase(new Chunk("",small_black_normal)));

	            att1_Table6.getDefaultCell().setBorder(Rectangle.BOX);
	            att1_Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            att1_Table6.addCell(new Phrase(new Chunk(type+" Due Date :",black_bold)));

	            att1_Table6.getDefaultCell().setBorder(Rectangle.BOX);
	            att1_Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            att1_Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            //att1_Table6.addCell(new Phrase(new Chunk(dateUtil.getDateFormatDD_MOM_YY(invoice_due_dt),small_black_normal)));
	            att1_Table6.addCell(new Phrase(new Chunk(drcr_due_dt,black_bold)));
	   	     	}

				float[] att1_ContactAddrWidths7 = {0.80F,0.30F,0.30F,0.40F};
				PdfPTable att1_Table7 = new PdfPTable(att1_ContactAddrWidths7);
				att1_Table7.setWidthPercentage(100);
				att1_Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				att1_Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				att1_Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
				att1_Table7.addCell(new Phrase(new Chunk("",black_bold)));

				att1_Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				att1_Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				att1_Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
				att1_Table7.addCell(new Phrase(new Chunk("",small_black_normal)));

				att1_Table7.getDefaultCell().setBorder(Rectangle.BOX);
				att1_Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				att1_Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				att1_Table7.addCell(new Phrase(new Chunk(type+" Note No:",black_bold)));

				att1_Table7.getDefaultCell().setBorder(Rectangle.BOX);
				att1_Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				att1_Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				att1_Table7.addCell(new Phrase(new Chunk(drcr_no,black_bold)));

				/*float[] att1_ContactAddrWidths8 = {0.80F,0.30F,0.20F,0.30F};
				PdfPTable att1_Table8 = new PdfPTable(att1_ContactAddrWidths8);
				att1_Table8.setWidthPercentage(100);
				att1_Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				att1_Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				att1_Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
				att1_Table8.addCell(new Phrase(new Chunk("",black_bold)));

				att1_Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				att1_Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				att1_Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
				att1_Table8.addCell(new Phrase(new Chunk("",black_bold)));

				att1_Table8.getDefaultCell().setBorder(Rectangle.BOX);
				att1_Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				att1_Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
				att1_Table8.addCell(new Phrase(new Chunk("Billing Period : ",black_bold)));

				att1_Table8.getDefaultCell().setBorder(Rectangle.BOX);
				att1_Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				att1_Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
				att1_Table8.addCell(new Phrase(new Chunk(""+period_start_dt+" to "+period_end_dt,black_bold)));*/


//	            if(!invoice_data.get("InvoiceRaisedIn").equals(invoice_data.get("PriceCd")))
				{
//	            	sr=1;
					//float[] att1_ContactAddrWidths9 = {0.1F, 0.05F, 0.02F, 0.15F};
					float[] att1_ContactAddrWidths9 = {0.002F, 0.07F, 0.02F, 0.15F};
					PdfPTable att1_Table9 = new PdfPTable(att1_ContactAddrWidths9);
					att1_Table9.setWidthPercentage(100);

					att1_Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					att1_Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					att1_Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
					att1_Table9.addCell(new Phrase(new Chunk("",small_black_normal)));

					att1_Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					att1_Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					att1_Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
					att1_Table9.addCell(new Phrase(new Chunk("Invoice Purchase Price Rate",small_black_normal)));

					att1_Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					att1_Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					att1_Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
					att1_Table9.addCell(new Phrase(new Chunk(invoice_data.get("Price"),small_black_normal)));

					att1_Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					att1_Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					att1_Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
					att1_Table9.addCell(new Phrase(new Chunk(curr,small_black_normal)));

//		            sr+=1;
//		            float[] att1_ContactAddrWidths10 = {0.002F, 0.05F, 0.02F, 0.15F};
//		   	     	PdfPTable att1_Table10 = new PdfPTable(att1_ContactAddrWidths10);
//		   	     	att1_Table10.setWidthPercentage(100);
//
//		   	     	att1_Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//		            att1_Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//		            att1_Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
//		            att1_Table10.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
//
//		            att1_Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//		            att1_Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//		            att1_Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
//		            att1_Table10.addCell(new Phrase(new Chunk("Applicable Sales Price Rate",small_black_normal)));
//
//		            att1_Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//		            att1_Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//		            att1_Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
//		            att1_Table10.addCell(new Phrase(new Chunk(item_diff_amt,small_black_normal)));
//
//		            att1_Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//		            att1_Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//		            att1_Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
//		            att1_Table10.addCell(new Phrase(new Chunk(curr,small_black_normal)));


					float[] att1_ContactAddrWidths11 = {0.002F, 0.07F, 0.02F, 0.15F};
					PdfPTable att1_Table11 = new PdfPTable(att1_ContactAddrWidths11);
					att1_Table11.setWidthPercentage(100);

					att1_Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					att1_Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					att1_Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
					att1_Table11.addCell(new Phrase(new Chunk("",small_black_normal)));

					att1_Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					att1_Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					att1_Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
					att1_Table11.addCell(new Phrase(new Chunk("Difference in Quantity",black_bold)));

					att1_Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					att1_Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					att1_Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
					att1_Table11.addCell(new Phrase(new Chunk(item_value,black_bold)));

					att1_Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					att1_Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					att1_Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
					att1_Table11.addCell(new Phrase(new Chunk(curr,small_black_normal)));


					document.add(LogoTable);
					document.add(att1_Table1);
					document.add(att1_Table2);
					document.add(att1_Table3);
					document.add(att1_Table4);
					document.add(new Paragraph("              "));
					document.add(title_note_table);
					document.add(new Paragraph("              "));
					document.add(att1_Table5);
					if(drcr_due_dt != null && !drcr_due_dt.equals("")) {
		            document.add(att1_Table6);
					}
					document.add(att1_Table7);
//		            if(!inv_flag.equals("ST"))
					{
//		            	document.add(att1_Table8);
						document.add(new Paragraph("              "));
						document.add(att1_Table9);
//		            	document.add(att1_Table10);
						document.add(new Paragraph("              "));
						document.add(att1_Table11);
					}
				}
			}

			document.close();

			String dealMap = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, cargo_no);
			File isPDFexist = new File(path);
			if(isPDFexist.exists())
			{
				int update_flag=0;
				int pdf_entry=0;
					queryString="UPDATE FMS_PUR_DR_CR_MST SET PDF_DRCR_DTL=?,PRINT_BY_ORI=?,PRINT_DT_ORI=SYSDATE "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
							+ "AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND INVOICE_NO = ? AND INVOICE_DT=TO_DATE(?,'DD/MM/YYYY') ";
							if(contract_type.equals("N") || contract_type.equals("G") || contract_type.equals("P")) {
								queryString+= "AND CARGO_NO=? ";
							}
		            			queryString+= "AND INV_FLAG=? AND DR_CR_SEQ_NO = ? AND DR_CR_FLAG = ? "
		            					+ "AND CRITERIA = ? AND REF_TYPE = ? ";
					int stcount=0;
					stmt=conn.prepareStatement(queryString);
					stmt.setString(++stcount, "O");
					stmt.setString(++stcount, emp_cd);
					stmt.setString(++stcount, comp_cd);
					stmt.setString(++stcount, counterparty_cd);
					stmt.setString(++stcount, cont_no);
					stmt.setString(++stcount, agmt_no);
					stmt.setString(++stcount, plant_seq);
					stmt.setString(++stcount, bu_plant_seq);
					stmt.setString(++stcount, contract_type);
					stmt.setString(++stcount, period_start_dt);
					stmt.setString(++stcount, period_end_dt);
					stmt.setString(++stcount, financial_year);
					stmt.setString(++stcount, inv_seq);
					stmt.setString(++stcount, invoice_data.get("InvoiceNo"));
					stmt.setString(++stcount, invoice_data.get("InvoiceDate"));
					if(contract_type.equals("N") || contract_type.equals("G") || contract_type.equals("P")) {
					stmt.setString(++stcount, cargo_no);
					}
					stmt.setString(++stcount, inv_flag);
					stmt.setString(++stcount, drcr_seq);
					stmt.setString(++stcount, drcr_flag);
					stmt.setString(++stcount, drcr_criteria);
					stmt.setString(++stcount, ref_type);
					update_flag=stmt.executeUpdate();

					stmt.close();

				int count=0;
		        queryString1="SELECT COUNT(*) "
		        		+ "FROM FMS_DRCR_PUR_INV_FILE_DTL "
		        		+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
		        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=? AND INV_FLAG=? ";
		        stmt1=conn.prepareStatement(queryString1);
		        stmt1.setString(1, comp_cd);
		        stmt1.setString(2, contract_type);
		        stmt1.setString(3, drcr_seq);
		        stmt1.setString(4, financial_year);
		        stmt1.setString(5, drcr_flag);
		        stmt1.setString(6, inv_flag);
		        rset1=stmt1.executeQuery();
		        if(rset1.next())
		        {
		        	count=rset1.getInt(1);
		        }
		        rset1.close();
		        stmt1.close();

		        if(count > 0)
		        {
		        	queryString2="UPDATE FMS_DRCR_PUR_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
		 	        		+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
		 	        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND INV_TITLE=? AND INV_FLAG=? ";
		        	stmt2=conn.prepareStatement(queryString2);
		        	stmt2.setString(1, file_nm);
		        	stmt2.setString(2, emp_cd);
		        	stmt2.setString(3, comp_cd);
		        	stmt2.setString(4, contract_type);
		        	stmt2.setString(5, drcr_seq);
		        	stmt2.setString(6, inv_fin_yr);
		        	stmt2.setString(7, drcr_flag);
		        	stmt2.setString(8, inv_flag);
		        	pdf_entry=stmt2.executeUpdate();

		        	stmt2.close();
		        }
		        else
		        {
		        	queryString2="INSERT INTO FMS_DRCR_PUR_INV_FILE_DTL(COMPANY_CD,CONTRACT_TYPE,INVOICE_SEQ,FINANCIAL_YEAR,INV_TITLE,"
		        			+ "FILE_NAME,ENT_BY,ENT_DT,INV_FLAG) "
		        			+ "VALUES(?,?,?,?,?,"
		        			+ "?,?,SYSDATE,?)";
		        	stmt2=conn.prepareStatement(queryString2);
		        	stmt2.setString(1, comp_cd);
		        	stmt2.setString(2, contract_type);
		        	stmt2.setString(3, drcr_seq);
		        	stmt2.setString(4, inv_fin_yr);
		        	stmt2.setString(5, drcr_flag);
		        	stmt2.setString(6, file_nm);
		        	stmt2.setString(7, emp_cd);
		        	stmt2.setString(8, inv_flag);
		        	pdf_entry=stmt2.executeUpdate();

		        	stmt2.close();
		        }

		        if(pdf_entry == 0 || update_flag == 0)
		        {
		        	conn.rollback();
		        	deletePdfFile(path);
		        	msg="Failed! - "+msg_content+" PDF for "+dealMap+" Generation Failed!";
		        	msg_type="E";
		        }
		        else
		        {
		        	conn.commit();
		        	msg = "Successful! - "+msg_content+" PDF "+file_nm+" for "+dealMap+" Generated Successfully!";
		        	msg_type="S";
		        }
			}
			else
			{
				msg="Failed! - "+msg_content+" PDF for "+dealMap+" Generation Failed!";
				msg_type="E";
			}
		}
		catch(Exception e)
		{
			conn.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);

			document.close();
			deletePdfFile(path);

			msg="Error in Exception! - "+msg_content+" PDF Generation Failed!";
			msg_type="E";
		}
		finally
		{
			document.close();
		}

		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, "", "", msg);
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
		}
	}

	//Deep :
	public void printPdfFileForDrCrNote() throws SQLException
	{
		String function_nm="printPdfFileForDrCrNote()";

		String emp_nm ="";
		String ip = "";
		msg="";
		//String msg_content="";
		String path ="";
		String counterparty_abbr="";
		String invdtl="Sales";

		Rectangle pageSize = new Rectangle(595, 842);
		Rectangle pageSize1=new Rectangle(842,595);

		pageSize.setBackgroundColor(BaseColor.WHITE);
		pageSize1.setBackgroundColor(BaseColor.WHITE);

		Document document = new Document(pageSize);
		try
		{
			HttpSession session = request.getSession();
			emp_nm = (String)session.getAttribute("emp_nm")==null?"":(String)session.getAttribute("emp_nm");
			ip = (String)session.getAttribute("ip")==null?"":(String)session.getAttribute("ip");

			String company_nm = ""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");
			String company_abbr = ""+session.getAttribute("comp_abbr")==null?"":""+session.getAttribute("comp_abbr");
			String counterparty_nm=""+utilBean.getCounterpartyName(conn,counterparty_cd);
			counterparty_abbr=""+utilBean.getCounterpartyABBR(conn,counterparty_cd);

			if(inv_flag.equals("UG"))
			{
				invdtl="LTCORA(Sell) SUG";
			}
			else if(inv_flag.equals("ST"))
			{
				invdtl="LTCORA(Sell) STORAGE";
			}
			else if(contract_type.equals("Q") || contract_type.equals("O"))
			{
				invdtl="LTCORA(Sell)";
			}

			int pdfGenCount=0;
			queryString="SELECT COUNT(*) "
					+ "FROM FMS_SALES_DR_CR_MST A ,FMS_DRCR_INV_FILE_DTL B "
					+ "WHERE A.COMPANY_CD=? AND A.FINANCIAL_YEAR=? AND A.INVOICE_SEQ=? AND A.BU_STATE_TIN=? "
					+ "AND A.PRINT_BY_ORI IS NOT NULL AND A.PRINT_DT_ORI IS NOT NULL AND B.PDF_TYPE=? AND B.FILE_NAME IS NOT NULL "
					+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.BU_STATE_TIN=B.BU_STATE_TIN AND A.INVOICE_SEQ=B.INVOICE_SEQ "
    		  		+ "AND A.FINANCIAL_YEAR=B.FINANCIAL_YEAR";
			stmt=conn.prepareStatement(queryString);
			stmt.setString(1, comp_cd);
			stmt.setString(2, financial_year);
			stmt.setString(3, inv_seq);
			stmt.setString(4, bu_state_tin);
			stmt.setString(5, print_pdf_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				pdfGenCount=rset.getInt(1);
			}
			rset.close();
			stmt.close();

			String dealMap = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, cargo_no);

			if(pdfGenCount>0)
			{
				msg="Failed! - "+invdtl+" Invoice for "+counterparty_abbr+" PDF for "+dealMap+" already Generated!";
	        	msg_type="E";
	        }
			else
			{
				String contRef="";
				String signingDt="";
				String agmtSigningDt="";
				String dcq="";
				String agmt_base="";
				double mdcq_percentage=100;
				String ship_nm="";
				String boe_number="";
				String boe_date="";
				String arrivalDt="";
				String auto_pay_flag="";

				if(contract_type.equals("O") || contract_type.equals("Q"))
				{
					queryString="SELECT A.CONT_REF_NO,TO_CHAR(A.DDA_DT,'DD-MON-YY'),A.CONT_REF_NO,B.SHIP_CD,B.BOE_NO,TO_CHAR(B.BOE_DT,'DD-MON-YY'),TO_CHAR(SIGNING_DT,'DD-MON-YY'),"
							+ "B.CSOC_QTY,A.MDCQ_PERCENTAGE,TO_CHAR(B.ACTUAL_RECPT_DT,'DD-MON-YY') "
							+ "FROM FMS_LTCORA_CONT_MST A,"
								+ "FMS_LTCORA_CONT_CARGO_DTL B "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.BUY_SALE=? "
							+ "AND A.AGMT_NO=? AND A.AGMT_TYPE=? AND A.CONT_NO=? AND A.CONTRACT_TYPE=? AND B.CARGO_NO=? "
							+ "AND A.CONT_REV = (SELECT MAX(B.CONT_REV) FROM FMS_LTCORA_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE) "
							+ ""
							+ "AND A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.BUY_SALE=B.BUY_SALE AND A.AGMT_TYPE=B.AGMT_TYPE ";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, "C");
					stmt.setString(4, agmt_no);
					stmt.setString(5, "A");
					stmt.setString(6, cont_no);
					stmt.setString(7, contract_type);
					stmt.setString(8, cargo_no);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						contRef=rset.getString(1)==null?"":rset.getString(1);
						//dda_dt=rset.getString(2)==null?"":rset.getString(2);
						//cargoRef=rset.getString(3)==null?"":rset.getString(3);

						String ship_cd=rset.getString(4)==null?"":rset.getString(4);
						ship_nm=utilBean.getShipName(conn,ship_cd);
						boe_number = rset.getString(5)==null?"":rset.getString(5);
						boe_date=rset.getString(6)==null?"":rset.getString(6);

						signingDt=rset.getString(7)==null?"":rset.getString(7);
						dcq=nf.format(rset.getDouble(8));
						mdcq_percentage=rset.getDouble(9);
						if(Double.doubleToRawLongBits(mdcq_percentage)==Double.doubleToRawLongBits(0))
						{
							mdcq_percentage=100;
						}
						arrivalDt=rset.getString(10)==null?"":rset.getString(10);
					}
					rset.close();
					stmt.close();

					queryString1="SELECT TO_CHAR(SIGNING_DT,'DD-MON-YY')  "
							+ "FROM FMS_LTCORA_AGMT_MST A "
							+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
							+ "AND AGMT_NO=? AND COUNTERPARTY_CD=? AND BUY_SALE=? "
							+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_LTCORA_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.BUY_SALE=B.BUY_SALE) ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, "A");
					stmt1.setString(3, agmt_no);
					stmt1.setString(4, counterparty_cd);
					stmt1.setString(5, "C");
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						agmtSigningDt=rset1.getString(1)==null?"":rset1.getString(1);
					}
					rset1.close();
					stmt1.close();
				}
				else
				{
					queryString="SELECT CONT_REF_NO,TO_CHAR(SIGNING_DT,'DD-MON-YY'),TRADE_REF_NO,DCQ,"
							+ "MDCQ_PERCENTAGE,AGMT_BASE,ADV_ADJUST  "
							+ "FROM FMS_SUPPLY_CONT_MST A "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? "
							+ "AND CONT_NO=? AND AGMT_NO=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_REV = (SELECT MAX(CONT_REV) FROM FMS_SUPPLY_CONT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
							+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE) ";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, contract_type);
					stmt.setString(3, cont_no);
					stmt.setString(4, agmt_no);
					stmt.setString(5, counterparty_cd);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						contRef=rset.getString(1)==null?"":rset.getString(1);
						signingDt=rset.getString(2)==null?"":rset.getString(2);

						String trade_ref=rset.getString(3)==null?"":rset.getString(3);
						if(contract_type.equals("X"))
						{
							contRef=trade_ref;
						}
						dcq=nf.format(rset.getDouble(4));
						mdcq_percentage=rset.getDouble(5);
						if(Double.doubleToRawLongBits(mdcq_percentage)==Double.doubleToRawLongBits(0))
						{
							mdcq_percentage=100;
						}
						agmt_base=rset.getString(6)==null?"":rset.getString(6);
						auto_pay_flag=rset.getString(7)==null?"":rset.getString(7);
					}
					rset.close();
					stmt.close();

					if(contract_type.equals("S"))
					{
						queryString="SELECT TO_CHAR(SIGNING_DT,'DD-MON-YY')  "
								+ "FROM FMS_AGMT_MST A "
								+ "WHERE COMPANY_CD=? AND AGMT_TYPE=? "
								+ "AND AGMT_NO=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_REV = (SELECT MAX(AGMT_REV) FROM FMS_AGMT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
								+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE) ";
						stmt=conn.prepareStatement(queryString);
						stmt.setString(1, comp_cd);
						stmt.setString(2, "F");
						stmt.setString(3, agmt_no);
						stmt.setString(4, counterparty_cd);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							agmtSigningDt=rset.getString(1)==null?"":rset.getString(1);
						}
						rset.close();
						stmt.close();
					}
				}

				HashMap bu_plant_detail=utilBean.getCounterpartyPlantDetail(conn,comp_cd, "B", comp_cd, bu_plant_seq);
				String bu_plantAddress=""+bu_plant_detail.get("plant_address");
				String bu_plantCity=""+bu_plant_detail.get("plant_city");
				String bu_plantState=""+bu_plant_detail.get("plant_state");
				String bu_plantPin=""+bu_plant_detail.get("plant_pin");
				String bu_plantNm=""+bu_plant_detail.get("plant_name");

				HashMap plant_detail=utilBean.getCounterpartyPlantDetail(conn,comp_cd, "C", counterparty_cd, plant_seq);
				String plantAddress=""+plant_detail.get("plant_address");
				String plantCity=""+plant_detail.get("plant_city");
				String plantState=""+plant_detail.get("plant_state");
				String plantPin=""+plant_detail.get("plant_pin");

				String bu_contact_person_cd="";
				String contact_person_cd="";
				String invoice_dt="";
				String invoice_due_dt="";
				String remark_1="";
				String remark_2="";
				String qty_mmbtu="";
				String price="";
				String price_cd="";
				String gross_amt="";
				String exchang_rate_cd="";
				String exchang_rate_dt="";
				String exchang_rate="";
				String exchang_rate_type="";
				String gross_amt1="";
				String tax_amt="";
				String tax_struct_cd="";
				String tax_struct_dt="";
				String invoice_amt="";
				String net_payable="";
				String applicable_abbr="";
				String applicable_amt="";
				String TCS_factor="";
				String tax_struct_dtl="";
				String invoice_no="";
				String invoice_raised_in="";
				String temp_invoice_dt="";
				String transportation_charges = "";
				String transportation_amount = "";
				String gross_include_transport_tariff = "";
				String marketing_margin = "";
				String marketing_margin_amount = "";
				String other_charges = "";
				String other_charges_amount = "";
				String irn_no="";
				String qr_code="";
				String sug_qty="";
				String sug_percent="";

				String drcr_no = "";
				String drcr_dt = "";
				String drcr_due_dt="";
				String type = "";
				String item_amt = "";
				String item_diff_amt = "";
				String diff_value = "";


				boolean isGrossIncTriff=false;

				double netPayable=0;
				double tdsAmt=0;

				String temp_period_start_dt=period_start_dt;
				String temp_period_end_dt=period_end_dt;

				String cont_map=counterparty_cd+"-"+contract_type+"-"+agmt_no+"-%-"+cont_no+"-%";
				String exit_point="C-"+counterparty_cd+"-"+plant_seq;

				queryString="SELECT BU_CONTACT_PERSON_CD,CONTACT_PERSON_CD,INVOICE_NO,TO_CHAR(INVOICE_DT,'DD-MON-YY'),"
						+ "TO_CHAR(DUE_DT,'DD-MON-YY'),TO_CHAR(PERIOD_START_DT,'DD-MON-YY'),TO_CHAR(PERIOD_END_DT,'DD-MON-YY'),REMARK_1,REMARK_2,"
						+ "ALLOC_QTY,SALE_PRICE,SALE_PRICE_UNIT,SALE_AMT,EXCHG_RATE_VALUE,GROSS_AMT,TAX_AMT,TAX_STRUCT_CD,TO_CHAR(TAX_EFF_DT,'DD/MM/YYYY'),"
						+ "INVOICE_AMT,NET_PAYABLE_AMT,TCS_TDS,TCS_AMT,TCS_FACTOR,CHECKED_FLAG,APPROVED_FLAG, "
						+ "INVOICE_ID_SEQ,INVOICE_RAISED_IN,TO_CHAR(INVOICE_DT,'DD/MM/YYYY'),TRANSPORTATION_CHARGE,TRANSPORTATION_AMOUNT,EXCHG_RATE_CD,"
						+ "TO_CHAR(EXCHG_RATE_DT,'DD-MON-YY'),EXCHG_RATE_TYPE,MARKET_MARGIN,MARKET_MARGIN_AMT,OTHER_CHARGES,OTHER_CHARGES_AMT,SUG_QTY,SUG_PERCENT,TDS_GROSS_AMT "
						+ "FROM FMS_INVOICE_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
						+ "AND BU_STATE_TIN=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
						+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND FINANCIAL_YEAR=? "
						+ "AND INVOICE_SEQ=? AND CARGO_NO=? AND INV_FLAG=? ";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, agmt_no);
				stmt.setString(5, plant_seq);
				stmt.setString(6, bu_plant_seq);
				stmt.setString(7, contract_type);
				stmt.setString(8, bu_state_tin);
				stmt.setString(9, period_start_dt);
				stmt.setString(10, period_end_dt);
				stmt.setString(11, financial_year);
				stmt.setString(12, inv_seq);
				stmt.setString(13, cargo_no);
				stmt.setString(14, inv_flag);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					invoice_no=rset.getString(3)==null?"":rset.getString(3);
					invoice_dt=rset.getString(4)==null?"":rset.getString(4);
					invoice_due_dt=rset.getString(5)==null?"":rset.getString(5);
					remark_1=rset.getString(8)==null?"":rset.getString(8);
					remark_2=rset.getString(9)==null?"":rset.getString(9);


					queryString1="SELECT A.BU_CONTACT_PERSON_CD,A.CONTACT_PERSON_CD,A.DR_CR_NO,TO_CHAR(A.DR_CR_DT,'DD-MON-YY'),"
							+ "TO_CHAR(A.DR_CR_DUE_DT,'DD-MON-YY'),TO_CHAR(A.PERIOD_START_DT,'DD-MON-YY'),TO_CHAR(A.PERIOD_END_DT,'DD-MON-YY'),"
							+ "B.ITEM_DIFF_VALUE,A.TOTAL_GROSS,A.TOTAL_TAX,A.TOTAL_AMT,A.APPROVED_FLAG,A.INVOICE_NO,A.CRITERIA,A.DR_CR_SEQ,B.ITEM_AMT,B.ITEM_DIFF_AMT "
							+ "FROM FMS_SALES_DR_CR_MST A, FMS_SALES_DR_CR_DTL B "
							+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? "
							+ "AND A.AGMT_NO=? AND A.PLANT_SEQ=? AND A.BU_UNIT=? AND A.CONTRACT_TYPE=? "
							+ "AND A.BU_STATE_TIN=? "
							+ "AND A.PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') AND A.PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND A.DR_CR_FIN_YR=? "
							+ "AND A.INVOICE_SEQ=? AND A.CARGO_NO=? AND A.INV_FLAG=? AND A.DR_CR_SEQ = ? AND A.DR_CR_FLAG = ? AND A.CRITERIA = ?"
							+ "AND A.COMPANY_CD = B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO = B.CONT_NO AND A.AGMT_NO = B.AGMT_NO "
							+ "AND A.CONTRACT_TYPE = B.CONTRACT_TYPE AND A.PLANT_SEQ = B.PLANT_SEQ AND A.BU_UNIT = B.BU_UNIT AND A.DR_CR_FLAG = B.DR_CR_FLAG "
							+ "AND A.DR_CR_SEQ = B.DR_CR_SEQ AND A.CRITERIA = B.CRITERIA ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, cont_no);
					stmt1.setString(4, agmt_no);
					stmt1.setString(5, plant_seq);
					stmt1.setString(6, bu_plant_seq);
					stmt1.setString(7, contract_type);
					stmt1.setString(8, bu_state_tin);
					stmt1.setString(9, period_start_dt);
					stmt1.setString(10, period_end_dt);
					stmt1.setString(11, drcr_fin_yr);
					stmt1.setString(12, inv_seq);
					stmt1.setString(13, cargo_no);
					stmt1.setString(14, inv_flag);
					stmt1.setString(15, drcr_seq);
					stmt1.setString(16, drcr_flag);
					stmt1.setString(17, criteria);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						bu_contact_person_cd=rset1.getString(1)==null?"0":rset.getString(11);
						contact_person_cd=rset1.getString(2)==null?"0":rset1.getString(2);

						//invoice_id_seq=rset.getString(26)==null?"":rset.getString(26);

						//inv_period_start_dt=rset.getString(6)==null?"":rset.getString(6);
						//inv_period_end_dt=rset.getString(7)==null?"":rset.getString(7);
						drcr_no = rset1.getString(3)==null?"0":rset1.getString(3);
						drcr_dt = rset1.getString(4)==null?"":rset1.getString(4);
						drcr_due_dt = rset1.getString(5)==null?"":rset1.getString(5);

						item_amt = nf2.format(rset1.getDouble(16) == 0 ? "" : rset1.getDouble(16));
						item_diff_amt = nf2.format(rset1.getDouble(17) == 0 ? "" : rset1.getDouble(17));
						if (criteria.equals("3")) {
							diff_value = nf.format(rset1.getDouble(8) == 0 ? "" : rset1.getDouble(8));
							item_amt = nf.format(rset1.getDouble(16) == 0 ? "" : rset1.getDouble(16));
							item_diff_amt = nf.format(rset1.getDouble(17) == 0 ? "" : rset1.getDouble(17));
						}
						else {
							diff_value = nf2.format(rset1.getDouble(8) == 0 ? "" : rset1.getDouble(8));
							item_amt = nf2.format(rset1.getDouble(16) == 0 ? "" : rset1.getDouble(16));
							item_diff_amt = nf2.format(rset1.getDouble(17) == 0 ? "" : rset1.getDouble(17));
						}
						gross_amt1=nf.format(rset1.getDouble(9));
						tax_amt=nf.format(rset1.getDouble(10));
						invoice_amt=nf.format(rset1.getDouble(11));
						net_payable=nf.format(rset1.getDouble(11));
						netPayable=rset1.getDouble(11);

						qty_mmbtu=nf.format(rset.getDouble(10));
						price_cd=rset.getString(12)==null?"":rset.getString(12);
						price=utilBean.RateNumberFormat(rset.getDouble(11), price_cd);
						gross_amt=nf.format(rset.getDouble(13));
						exchang_rate=nf2.format(rset.getDouble(14));

						tax_struct_cd=rset.getString(17)==null?"":rset.getString(17);
						tax_struct_dt=rset.getString(18)==null?"":rset.getString(18);

						applicable_abbr=rset.getString(21)==null?"":rset.getString(21);
						applicable_amt=nf.format(rset.getDouble(22));
						TCS_factor=nf3.format(rset.getDouble(23));

						invoice_raised_in=rset.getString(27)==null?"":rset.getString(27);
						temp_invoice_dt=rset.getString(28)==null?"":rset.getString(28);

						transportation_charges=rset.getString(29)==null?"":nf2.format(rset.getDouble(29));
						transportation_amount=rset.getString(30)==null?"":nf.format(rset.getDouble(30));

						exchang_rate_cd=rset.getString(31)==null?"":rset.getString(31);
						exchang_rate_dt=rset.getString(32)==null?"":rset.getString(32);
						exchang_rate_type=rset.getString(33)==null?"":rset.getString(33);

						marketing_margin=rset.getString(34)==null?"":nf.format(rset.getDouble(34));
						marketing_margin_amount=rset.getString(35)==null?"":nf.format(rset.getDouble(35));
						other_charges=rset.getString(36)==null?"":nf.format(rset.getDouble(36));
						other_charges_amount=rset.getString(37)==null?"":nf.format(rset.getDouble(37));

						sug_qty=rset.getString(38)==null?"":nf.format(rset.getDouble(38));
						sug_percent=rset.getString(39)==null?"":nf.format(rset.getDouble(39));

						tdsAmt=rset.getDouble(40);

						if(drcr_flag.equals("DR")) {
							type = "Debit";
						}
						else {
							type = "Credit";
						}


//						gross_include_transport_tariff=nf.format(Double.parseDouble(gross_amt1));
//						if(!transportation_amount.equals(""))
//						{
//							if(Double.parseDouble(transportation_amount) > 0)
//							{
//								gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(transportation_amount));
//								isGrossIncTriff=true;
//							}
//						}
//						if(!marketing_margin_amount.equals(""))
//						{
//							if(Double.parseDouble(marketing_margin_amount) > 0)
//							{
//								gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(marketing_margin_amount));
//								isGrossIncTriff=true;
//							}
//						}
//						if(!other_charges_amount.equals(""))
//						{
//							if(Double.parseDouble(other_charges_amount) > 0)
//							{
//								gross_include_transport_tariff=nf.format(Double.parseDouble(gross_include_transport_tariff) + Double.parseDouble(other_charges_amount));
//								isGrossIncTriff=true;
//							}
//						}

						tax_struct_dtl=utilBean.getTaxDescr(conn, tax_struct_cd);

				}
				rset1.close();
				stmt1.close();

				}
				rset.close();
				stmt.close();

				if(contract_type.equals("O") || contract_type.equals("Q"))
				{
					queryString1="SELECT IRN_NO,SIGN_QR_CODE "
							+ "FROM FMS_INVOICE_IRN_DTL "
							+ "WHERE COMPANY_CD=? AND INVOICE_NO=? ";
					stmt1=conn.prepareStatement(queryString1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, invoice_no);
					rset1=stmt1.executeQuery();
					if(rset1.next())
					{
						irn_no=rset1.getString(1)==null?"":rset1.getString(1);
						qr_code=rset1.getString(2)==null?"":rset1.getString(2);
					}
					rset1.close();
					stmt1.close();
				}

				String contact_person_nm="";
				String bu_contact_person_nm="";

				queryString="SELECT CONTACT_PERSON "
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
				stmt.setString(6, "RLNG");
				stmt.setString(7, temp_invoice_dt);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					contact_person_nm=rset.getString(1)==null?"":rset.getString(1);
				}
				rset.close();
				stmt.close();

				queryString="SELECT CONTACT_PERSON "
						+ "FROM FMS_ENTITY_CONTACT_MST A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND ENTITY=? AND SEQ_NO=? AND ADDR_FLAG=? "
						+ "AND TYPE=?"
						+ "AND EFF_DT=(SELECT MAX(B.EFF_DT) FROM FMS_ENTITY_CONTACT_MST B WHERE A.COMPANY_CD=B.COMPANY_CD "
						+ "AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.ENTITY=B.ENTITY AND A.SEQ_NO=B.SEQ_NO "
						+ "AND EFF_DT <= TO_DATE(?,'DD/MM/YYYY') AND A.TYPE=B.TYPE)";
				stmt=conn.prepareStatement(queryString);
				stmt.setString(1, comp_cd);
				stmt.setString(2, comp_cd);
				stmt.setString(3, "B");
				stmt.setString(4, bu_contact_person_cd);
				stmt.setString(5, "P"+bu_plant_seq);
				stmt.setString(6, "RLNG");
				stmt.setString(7, temp_invoice_dt);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					bu_contact_person_nm=rset.getString(1)==null?"":rset.getString(1);
				}
				rset.close();
				stmt.close();

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
				String sub_folder2="drcr_note";
				String temp_path=appPath+File.separator+main_folder+File.separator+sub_folder+File.separator+sub_folder2;
				File main_folderDir = new File(temp_path);
		        if(!main_folderDir.exists())
		        {
		        	main_folderDir.mkdirs();
		        }

		        String monthofinv = dateUtil.getMonthNameMON(period_end_dt);
		        String dtPeriod="";
		        if(!period_start_dt.equals("") && !period_end_dt.equals(""))
		        {
		        	dtPeriod=period_start_dt.substring(0,2);
		        	dtPeriod+="-";
		        	dtPeriod+=period_end_dt.substring(0,2);
		        }

		        String contNm="";
		        String invNm="";
		        String drcr = "";
		        if(drcr_flag.equals("DR")) {
		        	drcr = "DEBIT_NOTE";
		        }
		        else {
		        	drcr = "CREDIT_NOTE";
		        }
		        if(contract_type.equals("X"))
		        {
		        	contNm="IGX";
		        	invNm=drcr;
		        }
		        else if(contract_type.equals("L"))
		        {
		        	contNm="LOA";
		        	invNm=drcr;
		        }
		        else if(contract_type.equals("S"))
		        {
		        	contNm="SN";
		        	invNm=drcr;
		        }
		        else if(inv_flag.equals("UG"))
		        {
		        	if(contract_type.equals("O"))
			        {
			        	contNm="Period";
			        	invNm="LTCORA_SUG_"+drcr;
			        }
			        else if(contract_type.equals("Q"))
			        {
			        	contNm="CN";
			        	invNm="LTCORA_SUG_"+drcr;
			        }
		        }
		        else if(inv_flag.equals("ST"))
		        {
		        	if(contract_type.equals("O"))
			        {
			        	contNm="Period";
			        	invNm="LTCORA_STORAGE_"+drcr;
			        }
			        else if(contract_type.equals("Q"))
			        {
			        	contNm="CN";
			        	invNm="LTCORA_STORAGE_"+drcr;
			        }
		        }
		        else if(contract_type.equals("O"))
		        {
		        	contNm="Period";
		        	invNm="LTCORA_"+drcr;
		        }
		        else if(contract_type.equals("Q"))
		        {
		        	contNm="CN";
		        	invNm="LTCORA_"+drcr;
		        }

				file_nm=print_pdf_type+"-"+company_abbr+"-"+invNm+"-"+contNm+""+bu_state_tin+"-"+drcr_seq+"-"+counterparty_abbr+"_"+drcr_fin_yr+"_"+monthofinv.trim()+"_"+dtPeriod+".pdf";

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

	            float[] Contact1 = {0.50f};
	   	     	PdfPTable LogoTable = new PdfPTable(Contact1);
	   	     	LogoTable.setWidthPercentage(100);
	   	     	LogoTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	   	     	LogoTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	   	     	LogoTable.addCell(company_logo_cell);

				float[] ContactAddrWidths1 = {0.100f};
	   	     	PdfPTable HlplLogoTable = new PdfPTable(ContactAddrWidths1);
	            HlplLogoTable.setWidthPercentage(100);
	            HlplLogoTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            HlplLogoTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            HlplLogoTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            HlplLogoTable.addCell(new Phrase(new Chunk(print_pdf_type_nm,small_black_normal_10)));

	            float[] ContactAddrWidths2 = {0.100f};
	   	     	PdfPTable Table = new PdfPTable(ContactAddrWidths2);
	            Table.setWidthPercentage(100);
	            Table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table.addCell(new Phrase(new Chunk(company_nm,black_bold1)));

	            float[] ContactAddrWidths3 = {0.100f};
	   	     	PdfPTable Table1 = new PdfPTable(ContactAddrWidths3);
	            Table1.setWidthPercentage(100);
	            Table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//	            if(contract_type.equals("Q") || contract_type.equals("O"))
//	            {
//	            	Table1.addCell(new Phrase(new Chunk("TAX INVOICE",small_black_normal_12)));
//	            }
//	            else if(bu_plantState.equals(plantState))
//	            {
//	            	Table1.addCell(new Phrase(new Chunk("TAX INVOICE",small_black_normal_12)));
//	            }
//	            else
//	            {
//	            	Table1.addCell(new Phrase(new Chunk("RETAIL INVOICE",small_black_normal_12)));
//	            }

	            if(drcr_flag.equals("DR"))
	            {
	            	Table1.addCell(new Phrase(new Chunk("DEBIT NOTE",small_black_normal_12)));
	            }
	            else if(drcr_flag.equals("CR"))
	            {
	            	Table1.addCell(new Phrase(new Chunk("CREDIT NOTE",small_black_normal_12)));
	            }


//	            if(contract_type.equals("Q") || contract_type.equals("O"))
//	            {
//	            	Table1.setWidthPercentage(100);
//	                Table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//	                Table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//	                Table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//	                Table1.addCell(new Phrase(new Chunk("Tax Invoice issued under Rule 46 of the Central Goods and Services Tax Rules, 2017",small_black_normal_10)));
//	            }
				period_start_dt=dateUtil.getDateFormatDD_MOM_YY(period_start_dt);
				period_end_dt=dateUtil.getDateFormatDD_MOM_YY(period_end_dt);

				String info ="In respect of ";
				if(contract_type.equals("S")){
					info+="Supply Notice ("+contRef+") executed on "+signingDt+" pursuant to Framework Gas Sales Agreement executed on "+agmtSigningDt+"";
				}else if(contract_type.equals("Q")){
					//info+="Period ("+contRef+") executed on "+signingDt+" pursuant to Framework LTCORA Agreement executed on "+agmtSigningDt+"";
					info+="LTCORA Agreement executed on "+agmtSigningDt+"";
				}else if(contract_type.equals("O")){
					//info+="CN ("+contRef+") executed on "+signingDt+" pursuant to Framework LTCORA Agreement executed on "+agmtSigningDt+"";
					info+="LTCORA Agreement executed on "+agmtSigningDt+" & CN ("+contRef+") executed on "+signingDt+"";
				}else if(contract_type.equals("L")){
					info+="Letter of Agreement ("+contRef+") executed on "+signingDt;
				}else if(contract_type.equals("X")){
					info+="Exchange Transaction ("+contRef+") executed on "+signingDt;
				}
				info+="\nbetween "+company_nm+" and "+counterparty_nm;

	            float[] ContactAddrWidths4 = {0.100f};
	   	     	PdfPTable Table2 = new PdfPTable(ContactAddrWidths4);
	            Table2.setWidthPercentage(100);
	            Table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table2.addCell(new Phrase(new Chunk(info,small_black_normal)));

	            float[] ContactAddrWidths5 = {0.80f,0.10F,0.20F,0.80F};
	   	     	PdfPTable Table3 = new PdfPTable(ContactAddrWidths5);
	            Table3.setWidthPercentage(100);
	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            //Table3.addCell(new Phrase(new Chunk("Business Unit: "+bu_plantNm,black_bold)));
	            Table3.addCell(new Phrase(new Chunk("Business Unit: ",black_bold)));

	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("",black_bold)));

	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk("To:",black_bold)));

	            Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table3.addCell(new Phrase(new Chunk(""+contact_person_nm+",",black_bold)));



				float[] ContactAddrWidths6 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table4 = new PdfPTable(ContactAddrWidths6);
	            Table4.setWidthPercentage(100);
	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk(company_nm,small_black_normal)));

	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk("",small_black_normal)));

	            Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table4.addCell(new Phrase(new Chunk(counterparty_nm,small_black_normal)));

	            float[] ContactAddrWidths7 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table5 = new PdfPTable(ContactAddrWidths7);
	            Table5.setWidthPercentage(100);
	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk(bu_plantAddress+","+bu_plantCity,small_black_normal)));

	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk("",small_black_normal)));

	            Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table5.addCell(new Phrase(new Chunk(plantAddress+","+plantCity,small_black_normal)));


	            float[] ContactAddrWidths8 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table6 = new PdfPTable(ContactAddrWidths8);
	            Table6.setWidthPercentage(100);
	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table6.addCell(new Phrase(new Chunk(bu_plantState+" - "+bu_plantPin,small_black_normal)));

	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table6.addCell(new Phrase(new Chunk("",small_black_normal)));

	            Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table6.addCell(new Phrase(new Chunk(plantState+" - "+plantPin,small_black_normal)));

	            String tax_info="";
	            String bu_tax_info="";

	            if(contract_type.equals("O") || contract_type.equals("Q"))
				{
					tax_info="State : "+plantState;
					tax_info+="\nState Code : "+utilBean.getState_TIN(conn, comp_cd, counterparty_cd, "C", plant_seq);

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

						tax_info+="\n"+nm+" : "+no;
					}
					stmt.close();
					rset.close();

					bu_tax_info="State : "+bu_plantState;
					bu_tax_info+="\nState Code : "+utilBean.getState_TIN(conn, comp_cd, comp_cd, "B", bu_plant_seq);

					queryString = "SELECT A.STAT_NO, TO_CHAR(A.EFF_DT,'DD/MM/YYYY'), B.STAT_NM "
							+ "FROM FMS_COUNTERPARTY_PLANT_TAX A, FMS_GOVT_STAT_TAX B "
							+ "WHERE A.COUNTERPARTY_CD=? AND A.ENTITY=? "
							+ "AND A.PLANT_SEQ_NO=? AND A.COMPANY_CD=? "
							+ "AND A.STAT_CD=B.STAT_CD AND A.STAT_NO IS NOT NULL AND B.STAT_CD IN ('1003','1001')";
					stmt = conn.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, "B");
					stmt.setString(3, bu_plant_seq);
					stmt.setString(4, comp_cd);
					rset = stmt.executeQuery();
					while(rset.next())
					{
						String no = rset.getString(1)==null?"":rset.getString(1);
						String nm = rset.getString(3)==null?"":rset.getString(3);

						bu_tax_info+="\n"+nm+" : "+no;
					}
					stmt.close();
					rset.close();

					bu_tax_info+="\nSAC : 999799"
							+ "\nDescription of Service : Other Miscellaneous services - Other Services n.e.c."
							+ "\nPlace Of Supply : "+plantState; //WILL BE CUSTOMER PLANT STATE AS DISCUSSED BY JAYASRI MAM WITH VIJAY ON 20250725
				}
				else
				{
					tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "C", counterparty_cd, plant_seq);

					bu_tax_info=utilBean.getCounterpartyPlantTaxInfo(conn,comp_cd, "B", comp_cd, bu_plant_seq);
				}

	           float[] ContactAddrWidths9 = {0.80f,0.30F,0.80F};
	   	     	PdfPTable Table7 = new PdfPTable(ContactAddrWidths9);
	            Table7.setWidthPercentage(100);
	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk(bu_tax_info,small_black_normal)));

	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk("",small_black_normal)));

	            Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table7.addCell(new Phrase(new Chunk(tax_info,small_black_normal)));

	            PdfPTable InvoiceDateInfoTable;
	            PdfPTable BillingPeriodInfoTable;

	            float[] InvoiceDateInfoWidths_qr = {0.30f, 0.20f, 0.70f};
				PdfPTable InvoiceDateInfoTable_qr = new PdfPTable(InvoiceDateInfoWidths_qr);
				if(!irn_no.equals("") && !qr_code.equals("") && (contract_type.equals("Q") || contract_type.equals("O")))
					{
						float[] InvoiceDateInfoWidths = {0.10F,0.30F,0.30F};
			            InvoiceDateInfoTable = new PdfPTable(InvoiceDateInfoWidths);
			            InvoiceDateInfoTable.setWidthPercentage(100);
			            InvoiceDateInfoTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						InvoiceDateInfoTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						InvoiceDateInfoTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
						InvoiceDateInfoTable.addCell(new Phrase(new Chunk("",black_bold)));

			            InvoiceDateInfoTable.getDefaultCell().setBorder(Rectangle.BOX);
						InvoiceDateInfoTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						InvoiceDateInfoTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
						InvoiceDateInfoTable.addCell(new Phrase(new Chunk(type+" Note Date :",black_bold)));

						InvoiceDateInfoTable.getDefaultCell().setBorder(Rectangle.BOX);
						InvoiceDateInfoTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						InvoiceDateInfoTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            InvoiceDateInfoTable.addCell(new Phrase(new Chunk(drcr_dt,black_bold)));

			            InvoiceDateInfoTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						InvoiceDateInfoTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						InvoiceDateInfoTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
						InvoiceDateInfoTable.addCell(new Phrase(new Chunk("",black_bold)));
			            //due date
			            InvoiceDateInfoTable.getDefaultCell().setBorder(Rectangle.BOX);
			            InvoiceDateInfoTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            InvoiceDateInfoTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            InvoiceDateInfoTable.addCell(new Phrase(new Chunk("Payment Due Date :",black_bold)));

			            InvoiceDateInfoTable.getDefaultCell().setBorder(Rectangle.BOX);
			            InvoiceDateInfoTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            InvoiceDateInfoTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            InvoiceDateInfoTable.addCell(new Phrase(new Chunk(drcr_due_dt,black_bold)));

			            InvoiceDateInfoTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
						InvoiceDateInfoTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
						InvoiceDateInfoTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
						InvoiceDateInfoTable.addCell(new Phrase(new Chunk("",black_bold)));
			            //inv no
			            InvoiceDateInfoTable.getDefaultCell().setBorder(Rectangle.BOX);
			            InvoiceDateInfoTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            InvoiceDateInfoTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            InvoiceDateInfoTable.addCell(new Phrase(new Chunk(company_abbr+" "+type+" Note No:",black_bold)));

			            InvoiceDateInfoTable.getDefaultCell().setBorder(Rectangle.BOX);
			            InvoiceDateInfoTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            InvoiceDateInfoTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            InvoiceDateInfoTable.addCell(new Phrase(new Chunk(drcr_no,black_bold)));

//			            float[] BillingPeriodInfoWidths = {0.20F,0.10F};
//			            BillingPeriodInfoTable = new PdfPTable(BillingPeriodInfoWidths);
//			            BillingPeriodInfoTable.setWidthPercentage(100);

			            InvoiceDateInfoTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            InvoiceDateInfoTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            InvoiceDateInfoTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            InvoiceDateInfoTable.addCell(new Phrase(new Chunk("",black_bold)));

			            InvoiceDateInfoTable.getDefaultCell().setBorder(Rectangle.BOX);
			            InvoiceDateInfoTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            InvoiceDateInfoTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            InvoiceDateInfoTable.addCell(new Phrase(new Chunk("Billing Period : ",black_bold)));

			            InvoiceDateInfoTable.getDefaultCell().setBorder(Rectangle.BOX);
			            InvoiceDateInfoTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            InvoiceDateInfoTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            InvoiceDateInfoTable.addCell(new Phrase(new Chunk(""+period_start_dt+" to "+period_end_dt,black_bold)));

//			            if(!inv_flag.equals("UG") && !inv_flag.equals("ST"))
//			            {
////			            	InvoiceDateInfoTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//			            	InvoiceDateInfoTable.getDefaultCell().setColspan(3);
//			            	InvoiceDateInfoTable.addCell(BillingPeriodInfoTable);
//			            }

			            InvoiceDateInfoTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            InvoiceDateInfoTable.getDefaultCell().setColspan(3);
			            InvoiceDateInfoTable.addCell(new Phrase(new Chunk("",small_black_bold)));

						int width=90;
						int height=90;
						Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
				        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

				        QRCodeWriter qrCodeWriter = new QRCodeWriter();
				        BitMatrix matrix = qrCodeWriter.encode(qr_code, BarcodeFormat.QR_CODE, width, height, hintMap);

				        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				        for (int x = 0; x < width; x++)
				        {
				            for (int y = 0; y < height; y++)
				            {
				                image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
				            }
				        }

				        BufferedImage qrImage = image;

				        ByteArrayOutputStream baos = new ByteArrayOutputStream();
				        ImageIO.write(qrImage, "png", baos);
				        Image qr_codeimg = Image.getInstance(baos.toByteArray());
						qr_codeimg.setBorder(Rectangle.NO_BORDER);
		                qr_codeimg.setAlignment(Element.ALIGN_LEFT);
		                PdfPCell qrcode_cell = new PdfPCell(qr_codeimg,false);
		                qrcode_cell.setBorder(Rectangle.NO_BORDER);
		                qrcode_cell.setHorizontalAlignment(Element.ALIGN_LEFT);

		                String char_16= irn_no.substring(0,16);
		                String char_32= irn_no.substring(16,32);
		                String char_48= irn_no.substring(32,48);
		                String char_64= irn_no.substring(48,irn_no.length());
		                String final_irn_no=char_16+"\n"+char_32+"\n"+char_48+"\n"+char_64;

		                PdfPTable InvoiceDateInfoTable_qr1 = new PdfPTable(1);
		       	        InvoiceDateInfoTable_qr1.setWidthPercentage(100);
		       	        InvoiceDateInfoTable_qr1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		       	        InvoiceDateInfoTable_qr1.getDefaultCell().setBorder(Rectangle.BOX);
		       	        InvoiceDateInfoTable_qr1.addCell(qrcode_cell);

		       	        PdfPTable InvoiceDateInfoTable_qr11 = new PdfPTable(1);
		    	        InvoiceDateInfoTable_qr11.setWidthPercentage(100);
		    	        InvoiceDateInfoTable_qr11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		    	        InvoiceDateInfoTable_qr11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		    	        InvoiceDateInfoTable_qr11.addCell(new Phrase(new Chunk("IRN",black_bold)));
		    	        InvoiceDateInfoTable_qr11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		    	        InvoiceDateInfoTable_qr11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		    	        InvoiceDateInfoTable_qr11.addCell(new Phrase(new Chunk(final_irn_no,small_black_normal)));

		    	        InvoiceDateInfoTable_qr.setWidthPercentage(100);
		       	        InvoiceDateInfoTable_qr.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		       	        InvoiceDateInfoTable_qr.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		       	        InvoiceDateInfoTable_qr.addCell(InvoiceDateInfoTable_qr1);

		       	        InvoiceDateInfoTable_qr.getDefaultCell().setBorder(Rectangle.BOX);
		       	        InvoiceDateInfoTable_qr.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		       	        InvoiceDateInfoTable_qr.addCell(InvoiceDateInfoTable_qr11);

		       	        InvoiceDateInfoTable_qr.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		       	        InvoiceDateInfoTable_qr.addCell(InvoiceDateInfoTable);
					}

	            float[] ContactAddrWidths10 = {0.80F,0.30F,0.40F,0.40F};
	   	     	PdfPTable Table8 = new PdfPTable(ContactAddrWidths10);
	            Table8.setWidthPercentage(100);
	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table8.addCell(new Phrase(new Chunk("",small_black_normal)));

	            Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table8.addCell(new Phrase(new Chunk("",small_black_normal)));

	            Table8.getDefaultCell().setBorder(Rectangle.BOX);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table8.addCell(new Phrase(new Chunk(type+" Note Date :",black_bold)));

	            Table8.getDefaultCell().setBorder(Rectangle.BOX);
	            Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            //Table8.addCell(new Phrase(new Chunk(dateUtil.getDateFormatDD_MOM_YY(invoice_dt),small_black_normal)));
	            Table8.addCell(new Phrase(new Chunk(drcr_dt,black_bold)));

	            float[] ContactAddrWidths11 = {0.80F,0.30F,0.40F,0.40F};
	   	     	PdfPTable Table9 = new PdfPTable(ContactAddrWidths11);
	            Table9.setWidthPercentage(100);
	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table9.addCell(new Phrase(new Chunk("",small_black_normal)));

	            Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table9.addCell(new Phrase(new Chunk("",small_black_normal)));

	            Table9.getDefaultCell().setBorder(Rectangle.BOX);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table9.addCell(new Phrase(new Chunk("Payment Due Date :",black_bold)));

	            Table9.getDefaultCell().setBorder(Rectangle.BOX);
	            Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            //Table9.addCell(new Phrase(new Chunk(dateUtil.getDateFormatDD_MOM_YY(invoice_due_dt),small_black_normal)));
	            Table9.addCell(new Phrase(new Chunk(drcr_due_dt,black_bold)));

	            float[] ContactAddrWidths12 = {0.80F,0.30F,0.40F,0.40F};
	   	     	PdfPTable Table10 = new PdfPTable(ContactAddrWidths12);
	            Table10.setWidthPercentage(100);
	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table10.addCell(new Phrase(new Chunk("",small_black_normal)));

	            Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table10.addCell(new Phrase(new Chunk("",small_black_normal)));

	            Table10.getDefaultCell().setBorder(Rectangle.BOX);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table10.addCell(new Phrase(new Chunk(company_abbr+" "+type+" Note No:",black_bold)));

	            Table10.getDefaultCell().setBorder(Rectangle.BOX);
	            Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table10.addCell(new Phrase(new Chunk(drcr_no,black_bold)));



	            float[] ContactAddrWidths12_1 = {0.80F,0.30F,0.40F,0.40F};
	   	     	PdfPTable Table10_1 = new PdfPTable(ContactAddrWidths12_1);
	   	     	Table10_1.setWidthPercentage(100);


	   	     	Table10_1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table10_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table10_1.addCell(new Phrase(new Chunk("",black_bold)));

	            Table10_1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	            Table10_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table10_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table10_1.addCell(new Phrase(new Chunk("",black_bold)));

	            Table10_1.getDefaultCell().setBorder(Rectangle.BOX);
	            Table10_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table10_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table10_1.addCell(new Phrase(new Chunk("Billing Period : ",black_bold)));

	            Table10_1.getDefaultCell().setBorder(Rectangle.BOX);
	            Table10_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table10_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table10_1.addCell(new Phrase(new Chunk(""+period_start_dt+" to "+period_end_dt,black_bold)));


	            float[] ContactAddrWidths13 = {0.08F,0.60F,0.20F,0.20F,0.20F,0.20F,0.20F};
	   	     	PdfPTable Table11 = new PdfPTable(ContactAddrWidths13);
	            Table11.setWidthPercentage(100);
	            Table11.getDefaultCell().setBorder(Rectangle.BOX);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table11.addCell(new Phrase(new Chunk("Sr.No.",small_black_bold)));

	            Table11.getDefaultCell().setBorder(Rectangle.BOX);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table11.addCell(new Phrase(new Chunk("Item Description",small_black_bold)));

	            Table11.getDefaultCell().setBorder(Rectangle.BOX);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table11.addCell(new Phrase(new Chunk("Attachment",small_black_bold)));

	            Table11.getDefaultCell().setBorder(Rectangle.BOX);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table11.addCell(new Phrase(new Chunk("Currency",small_black_bold)));

	            Table11.getDefaultCell().setBorder(Rectangle.BOX);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table11.addCell(new Phrase(new Chunk("Quantity\n(MMBTU)",small_black_bold)));

	            Table11.getDefaultCell().setBorder(Rectangle.BOX);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table11.addCell(new Phrase(new Chunk("Rate",small_black_bold)));

	            Table11.getDefaultCell().setBorder(Rectangle.BOX);
	            Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table11.addCell(new Phrase(new Chunk("Amount",small_black_bold)));

	            String salesValue="";
	            salesValue=""+gross_amt;

	            String curr="";
	            String curr1="";
	            if(price_cd.equals("1"))
	            {
	            	curr="INR";
	            }
	            else if(price_cd.equals("2"))
	            {
	            	curr="USD";
	            }

	            if(invoice_raised_in.equals("1"))
	            {
	            	curr1="INR";
	            }
	            else if(invoice_raised_in.equals("2"))
	            {
	            	curr1="USD";
	            }

	            int sr=1;

	            float[] ContactAddrWidths14 = {0.08F,0.60F,0.20F,0.20F,0.20F,0.20F,0.20F};
	   	     	PdfPTable Table12 = new PdfPTable(ContactAddrWidths14);
	            Table12.setWidthPercentage(100);
	            //Table12.getDefaultCell().setBorderWidthBottom(0);
	            if(inv_flag.equals("UG"))
	            {
	            	String mnthYr="";
					if(!temp_period_end_dt.equals(""))
					{
						mnthYr=temp_period_end_dt.substring(3,temp_period_end_dt.length());
						String[] split= mnthYr.split("/");
						mnthYr=dateUtil.getMonthName(temp_period_end_dt)+"-"+split[1];
					}

	            	Table12.getDefaultCell().setBorder(Rectangle.BOX);
		            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table12.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

		            Table12.getDefaultCell().setBorder(Rectangle.BOX);
		            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            Table12.addCell(new Phrase(new Chunk("Actual Quantity of LNG discharged during month of "+mnthYr+"",small_black_normal)));

		            Table12.getDefaultCell().setBorder(Rectangle.BOX);
		            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table12.addCell(new Phrase(new Chunk("Att1",small_black_normal)));

		            Table12.getDefaultCell().setBorder(Rectangle.BOX);
		            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table12.addCell(new Phrase(new Chunk("",small_black_normal)));

		            Table12.getDefaultCell().setBorder(Rectangle.BOX);
		            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table12.addCell(new Phrase(new Chunk(qty_mmbtu,small_black_normal)));

		            Table12.getDefaultCell().setBorder(Rectangle.BOX);
		            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table12.addCell(new Phrase(new Chunk("",small_black_normal)));

		            Table12.getDefaultCell().setBorder(Rectangle.BOX);
		            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table12.addCell(new Phrase(new Chunk("",small_black_normal)));

		            sr+=1;
	            	Table12.getDefaultCell().setBorder(Rectangle.BOX);
		            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table12.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

		            Table12.getDefaultCell().setBorder(Rectangle.BOX);
		            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            Table12.addCell(new Phrase(new Chunk(sug_percent+"% of above as SUG (System Use Gas)",small_black_normal)));

		            Table12.getDefaultCell().setBorder(Rectangle.BOX);
		            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table12.addCell(new Phrase(new Chunk("",small_black_normal)));

		            Table12.getDefaultCell().setBorder(Rectangle.BOX);
		            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table12.addCell(new Phrase(new Chunk("",small_black_normal)));

		            Table12.getDefaultCell().setBorder(Rectangle.BOX);
		            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table12.addCell(new Phrase(new Chunk(sug_qty,small_black_normal)));

		            Table12.getDefaultCell().setBorder(Rectangle.BOX);
		            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table12.addCell(new Phrase(new Chunk("",small_black_normal)));

		            Table12.getDefaultCell().setBorder(Rectangle.BOX);
		            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table12.addCell(new Phrase(new Chunk("",small_black_normal)));

		            sr+=1;
	            	Table12.getDefaultCell().setBorder(Rectangle.BOX);
		            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table12.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

		            Table12.getDefaultCell().setBorder(Rectangle.BOX);
		            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            Table12.addCell(new Phrase(new Chunk("Value of SUG (only for the purpose of GST payment on LTCORA Services)",small_black_normal)));

		            Table12.getDefaultCell().setBorder(Rectangle.BOX);
		            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table12.addCell(new Phrase(new Chunk("Att2",small_black_normal)));

		            Table12.getDefaultCell().setBorder(Rectangle.BOX);
		            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            Table12.addCell(new Phrase(new Chunk(""+curr,small_black_normal)));

		            Table12.getDefaultCell().setBorder(Rectangle.BOX);
		            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table12.addCell(new Phrase(new Chunk("",small_black_normal)));

		            Table12.getDefaultCell().setBorder(Rectangle.BOX);
		            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table12.addCell(new Phrase(new Chunk(""+price,small_black_normal)));

		            Table12.getDefaultCell().setBorder(Rectangle.BOX);
		            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            Table12.addCell(new Phrase(new Chunk(""+salesValue,small_black_normal)));
	            }
	            else
	            {
	            	if(contract_type.equals("O") || contract_type.equals("Q"))
					{
	            		if(inv_flag.equals("ST"))
	            		{
	                		Table12.getDefaultCell().setBorder(Rectangle.BOX);
		    	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		    	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		    	            Table12.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

		    	            Table12.getDefaultCell().setBorder(Rectangle.BOX);
		    	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		    	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		    	            Table12.addCell(new Phrase(new Chunk("Storage Charges For The Extended Storage Duration For Cargo Arrived on "+arrivalDt,small_black_normal)));

		    	            Table12.getDefaultCell().setBorder(Rectangle.BOX);
		    	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		    	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		    	            Table12.addCell(new Phrase(new Chunk("Att1",small_black_normal)));

		    	            Table12.getDefaultCell().setBorder(Rectangle.BOX);
		    	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		    	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		    	            Table12.addCell(new Phrase(new Chunk(curr,small_black_normal)));

		    	            Table12.getDefaultCell().setBorder(Rectangle.BOX);
		    	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		    	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		    	            Table12.addCell(new Phrase(new Chunk("As per Att1",small_black_normal)));

		    	            Table12.getDefaultCell().setBorder(Rectangle.BOX);
		    	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		    	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		    	            Table12.addCell(new Phrase(new Chunk("As per Att1",small_black_normal)));

		    	            Table12.getDefaultCell().setBorder(Rectangle.BOX);
		    	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		    	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		    	            Table12.addCell(new Phrase(new Chunk(""+salesValue,small_black_normal)));
	            		}
	            		else
	            		{
	            			Table12.getDefaultCell().setBorder(Rectangle.BOX);
				            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table12.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

				            Table12.getDefaultCell().setBorder(Rectangle.BOX);
				            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				            Table12.addCell(new Phrase(new Chunk("Natural Gas (Delivered) as per Invoice ref "+invoice_no+" dated " +invoice_dt,small_black_normal)));

				            Table12.getDefaultCell().setBorder(Rectangle.BOX);
				            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table12.addCell(new Phrase(new Chunk("",small_black_normal)));

				            Table12.getDefaultCell().setBorder(Rectangle.BOX);
				            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table12.addCell(new Phrase(new Chunk(""+curr,small_black_normal)));

				            Table12.getDefaultCell().setBorder(Rectangle.BOX);
				            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
				            Table12.addCell(new Phrase(new Chunk(qty_mmbtu,small_black_normal)));

				            Table12.getDefaultCell().setBorder(Rectangle.BOX);
				            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
				            Table12.addCell(new Phrase(new Chunk(price,small_black_normal)));

				            Table12.getDefaultCell().setBorder(Rectangle.BOX);
				            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
				            if (criteria.equals("3")) {
								Table12.addCell(new Phrase(new Chunk("", small_black_normal)));
							}
				            else {
				            	Table12.addCell(new Phrase(new Chunk("" + salesValue, small_black_normal)));
				            }

//		    	            if(price_cd.equals("2"))
//		    	            {
//		    	                sr+=1;
//		        	            Table12.getDefaultCell().setBorder(Rectangle.BOX);
//		        	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//		        	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//		        	            Table12.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
//
//		        	            Table12.getDefaultCell().setBorder(Rectangle.BOX);
//		        	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//		        	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//		        	            Table12.addCell(new Phrase(new Chunk("Gross Amount(USD)",small_black_normal)));
//
//		        	            Table12.getDefaultCell().setBorder(Rectangle.BOX);
//		        	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//		        	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//		        	            Table12.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//		        	            Table12.getDefaultCell().setBorder(Rectangle.BOX);
//		        	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//		        	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//		        	            Table12.addCell(new Phrase(new Chunk(curr,small_black_normal)));
//
//		        	            Table12.getDefaultCell().setBorder(Rectangle.BOX);
//		        	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//		        	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//		        	            Table12.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//		        	            Table12.getDefaultCell().setBorder(Rectangle.BOX);
//		        	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//		        	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//		        	            Table12.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//		        	            Table12.getDefaultCell().setBorder(Rectangle.BOX);
//		        	            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//		        	            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//		        	            Table12.addCell(new Phrase(new Chunk(""+salesValue,small_black_normal)));
//		    	            }
	            		}
					}
	            	else
	            	{
			            Table12.getDefaultCell().setBorder(Rectangle.BOX);
			            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table12.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

			            Table12.getDefaultCell().setBorder(Rectangle.BOX);
			            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
			            Table12.addCell(new Phrase(new Chunk("Natural Gas (Delivered) as per Invoice ref "+invoice_no+" dated " +invoice_dt,small_black_normal)));

			            Table12.getDefaultCell().setBorder(Rectangle.BOX);
			            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table12.addCell(new Phrase(new Chunk("",small_black_normal)));

			            Table12.getDefaultCell().setBorder(Rectangle.BOX);
			            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			            Table12.addCell(new Phrase(new Chunk(""+curr,small_black_normal)));

			            Table12.getDefaultCell().setBorder(Rectangle.BOX);
			            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            Table12.addCell(new Phrase(new Chunk(qty_mmbtu,small_black_normal)));

			            Table12.getDefaultCell().setBorder(Rectangle.BOX);
			            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            Table12.addCell(new Phrase(new Chunk(price,small_black_normal)));

			            Table12.getDefaultCell().setBorder(Rectangle.BOX);
			            Table12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            Table12.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			            if (criteria.equals("3")) {
							Table12.addCell(new Phrase(new Chunk("", small_black_normal)));
						}
			            else {
			            	Table12.addCell(new Phrase(new Chunk("" + salesValue, small_black_normal)));
			            }

	            	}
	            }

	            BigDecimal diff_gross = new BigDecimal("0");
				if (criteria.equals("3")) {
					BigDecimal price1 = BigDecimal.valueOf(Double.parseDouble(price));
					BigDecimal quantity = BigDecimal.valueOf(Double.parseDouble(diff_value));
					diff_gross = new BigDecimal(0);
					diff_gross = price1.multiply(quantity);
					sr+=1;
				}

	            float[] ContactAddrWidths26 = {0.08F,0.60F,0.20F,0.20F,0.20F,0.20F,0.20F};
	    	    PdfPTable Table24 = new PdfPTable(ContactAddrWidths26);
        	    Table24.setWidthPercentage(100);

        	    Table24.getDefaultCell().setBorder(Rectangle.BOX);
        	    Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        	    Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        	    Table24.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

        	    Table24.getDefaultCell().setBorder(Rectangle.BOX);
        	    Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        	    Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
        	    Table24.addCell(new Phrase(new Chunk("Difference in Quantity",small_black_normal)));

        	    Table24.getDefaultCell().setBorder(Rectangle.BOX);
        	    Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        	    Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        	    Table24.addCell(new Phrase(new Chunk("Att1",small_black_normal)));

        	    Table24.getDefaultCell().setBorder(Rectangle.BOX);
        	    Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        	    Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        	    Table24.addCell(new Phrase(new Chunk("",small_black_normal)));

        	    Table24.getDefaultCell().setBorder(Rectangle.BOX);
        	    Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        	    Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
        	    Table24.addCell(new Phrase(new Chunk(diff_value,small_black_normal)));

        	    Table24.getDefaultCell().setBorder(Rectangle.BOX);
        	    Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        	    Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
        	    Table24.addCell(new Phrase(new Chunk("",small_black_normal)));

        	    Table24.getDefaultCell().setBorder(Rectangle.BOX);
        	    Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        	    Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
        	    Table24.addCell(new Phrase(new Chunk("",small_black_normal)));


            	if (criteria.equals("3")) {
					sr += 1;
				}
				float[] ContactAddrWidths27 = {0.08F,0.60F,0.20F,0.20F,0.20F,0.20F,0.20F};
	    	    PdfPTable Table25 = new PdfPTable(ContactAddrWidths27);
        	    Table25.setWidthPercentage(100);
        	    Table25.getDefaultCell().setBorder(Rectangle.BOX);
        	    Table25.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        	    Table25.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        	    Table25.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

        	    Table25.getDefaultCell().setBorder(Rectangle.BOX);
        	    Table25.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        	    Table25.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
        	    Table25.addCell(new Phrase(new Chunk("Gross Amount",small_black_normal)));

        	    Table25.getDefaultCell().setBorder(Rectangle.BOX);
        	    Table25.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        	    Table25.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        	    Table25.addCell(new Phrase(new Chunk("",small_black_normal)));

        	    Table25.getDefaultCell().setBorder(Rectangle.BOX);
        	    Table25.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        	    Table25.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        	    Table25.addCell(new Phrase(new Chunk(""+curr,small_black_normal)));

        	    Table25.getDefaultCell().setBorder(Rectangle.BOX);
        	    Table25.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        	    Table25.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
        	    Table25.addCell(new Phrase(new Chunk("",small_black_normal)));

        	    Table25.getDefaultCell().setBorder(Rectangle.BOX);
        	    Table25.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        	    Table25.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
        	    Table25.addCell(new Phrase(new Chunk("",small_black_normal)));

        	    Table25.getDefaultCell().setBorder(Rectangle.BOX);
        	    Table25.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        	    Table25.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
        	    Table25.addCell(new Phrase(new Chunk(""+diff_gross,small_black_normal)));

					if (!invoice_raised_in.equals(price_cd)) {
						sr += 1;
					}

					float[] ContactAddrWidths15 = { 0.08F, 0.60F, 0.20F, 0.20F, 0.20F, 0.20F, 0.20F };
					PdfPTable Table13 = new PdfPTable(ContactAddrWidths15);
					Table13.setWidthPercentage(100);
					//Table13.getDefaultCell().setBorderWidthTop(0);
					Table13.getDefaultCell().setBorder(Rectangle.BOX);
					Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
					Table13.addCell(new Phrase(new Chunk("" + sr, small_black_normal)));
					Table13.getDefaultCell().setBorder(Rectangle.BOX);
					Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
					Table13.addCell(new Phrase(new Chunk("Exchange Rate", small_black_normal)));
					Table13.getDefaultCell().setBorder(Rectangle.BOX);
					Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
					Table13.addCell(new Phrase(new Chunk("", small_black_normal)));
					Table13.getDefaultCell().setBorder(Rectangle.BOX);
					Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
					Table13.addCell(new Phrase(new Chunk("INR/USD", small_black_normal)));
					Table13.getDefaultCell().setBorder(Rectangle.BOX);
					Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
					Table13.addCell(new Phrase(new Chunk("", small_black_normal)));
					Table13.getDefaultCell().setBorder(Rectangle.BOX);
					Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
					Table13.addCell(new Phrase(new Chunk(exchang_rate, small_black_normal)));
					Table13.getDefaultCell().setBorder(Rectangle.BOX);
					Table13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					Table13.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
					Table13.addCell(new Phrase(new Chunk("", small_black_normal)));


	            float[] ContactAddrWidths16 = {0.08F,0.60F,0.20F,0.20F,0.20F,0.20F,0.20F};
	   	     	PdfPTable Table14 = new PdfPTable(ContactAddrWidths16);
	            Table14.setWidthPercentage(100);
	            //Table14.getDefaultCell().setBorderWidthBottom(0);

	            Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table14.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

	            Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            if(criteria.equals("1")) {
	            	Table14.addCell(new Phrase(new Chunk("Difference in Exchange Rate",small_black_normal)));
	            }
	            else if(criteria.equals("2"))
	            {
	            	Table14.addCell(new Phrase(new Chunk("Applicable Sales Rate",small_black_normal)));
	            }
//	            else if(criteria.equals("3"))
//	            {
//	            	Table14.addCell(new Phrase(new Chunk("Applicable Quantity",small_black_normal)));
//	            }

	            Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table14.addCell(new Phrase(new Chunk("Att1",small_black_normal)));

	            Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            if (criteria.equals("1")) {
					Table14.addCell(new Phrase(new Chunk("INR/USD", small_black_normal)));
				}
	            else {
	            	Table14.addCell(new Phrase(new Chunk("USD", small_black_normal)));
	            }
				Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table14.addCell(new Phrase(new Chunk("",small_black_normal)));

	            Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table14.addCell(new Phrase(new Chunk(diff_value,small_black_normal)));

	            Table14.getDefaultCell().setBorder(Rectangle.BOX);
	            Table14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table14.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table14.addCell(new Phrase(new Chunk("",small_black_normal)));

	    	    sr+=1;
	            float[] ContactAddrWidths17 = {0.08F,0.60F,0.20F,0.20F,0.20F,0.20F,0.20F};
	   	     	PdfPTable Table15 = new PdfPTable(ContactAddrWidths17);
	   	     	Table15.setWidthPercentage(100);

	   	     	Table15.getDefaultCell().setBorder(Rectangle.BOX);
	   	     	Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	   	     	Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	   	     	Table15.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

	   	     	Table15.getDefaultCell().setBorder(Rectangle.BOX);
	   	     	Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	   	     	Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	   	     	Table15.addCell(new Phrase(new Chunk("Gross Amount",small_black_normal)));

	   	     	Table15.getDefaultCell().setBorder(Rectangle.BOX);
	   	     	Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	   	     	Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	   	     	Table15.addCell(new Phrase(new Chunk("",small_black_normal)));

	   	     	Table15.getDefaultCell().setBorder(Rectangle.BOX);
	   	     	Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	   	     	Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	   	     	Table15.addCell(new Phrase(new Chunk(""+curr1,small_black_normal)));

	   	     	Table15.getDefaultCell().setBorder(Rectangle.BOX);
	   	     	Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	   	     	Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	   	     	Table15.addCell(new Phrase(new Chunk("",small_black_normal)));

	   	     	Table15.getDefaultCell().setBorder(Rectangle.BOX);
	   	     	Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	   	     	Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	   	     	Table15.addCell(new Phrase(new Chunk("",small_black_normal)));

	   	     	Table15.getDefaultCell().setBorder(Rectangle.BOX);
	   	     	Table15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	   	     	Table15.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	   	     	Table15.addCell(new Phrase(new Chunk(gross_amt1,small_black_normal)));

//	            float[] ContactAddrWidths28 = {0.08F,0.60F,0.20F,0.20F,0.20F,0.20F,0.20F};
//	    	    PdfPTable Table26 = new PdfPTable(ContactAddrWidths26);
//
//	    	    float[] ContactAddrWidths29 = {0.08F,0.60F,0.20F,0.20F,0.20F,0.20F,0.20F};
//	    	    PdfPTable Table27 = new PdfPTable(ContactAddrWidths27);

	            //if(agmt_base.equals("D"))
//	    	    if(!transportation_charges.equals(""))
//	            {
//	    	    	if(Double.parseDouble(transportation_amount) > 0)
//	    	    	{
//		            	sr+=1;
//
//		        	    Table24.setWidthPercentage(100);
//
//		        	    Table24.getDefaultCell().setBorder(Rectangle.BOX);
//		        	    Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//		        	    Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//		        	    Table24.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
//
//		        	    Table24.getDefaultCell().setBorder(Rectangle.BOX);
//		        	    Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//		        	    Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//		        	    Table24.addCell(new Phrase(new Chunk("Transporation Tariff",small_black_normal)));
//
//		        	    Table24.getDefaultCell().setBorder(Rectangle.BOX);
//		        	    Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//		        	    Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//		        	    Table24.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//		        	    Table24.getDefaultCell().setBorder(Rectangle.BOX);
//		        	    Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//		        	    Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//		        	    Table24.addCell(new Phrase(new Chunk(""+curr1,small_black_normal)));
//
//		        	    Table24.getDefaultCell().setBorder(Rectangle.BOX);
//		        	    Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//		        	    Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//		        	    Table24.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//		        	    Table24.getDefaultCell().setBorder(Rectangle.BOX);
//		        	    Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//		        	    Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//		        	    Table24.addCell(new Phrase(new Chunk(transportation_charges,small_black_normal)));
//
//		        	    Table24.getDefaultCell().setBorder(Rectangle.BOX);
//		        	    Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//		        	    Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//		        	    Table24.addCell(new Phrase(new Chunk(transportation_amount,small_black_normal)));
//	    	    	}
//	            }
//
//	            if(!marketing_margin.equals(""))
//	            {
//	            	if(Double.parseDouble(marketing_margin) > 0)
//	            	{
//		            	sr+=1;
//
//		        	    Table24.setWidthPercentage(100);
//
//		        	    Table24.getDefaultCell().setBorder(Rectangle.BOX);
//		        	    Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//		        	    Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//		        	    Table24.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
//
//		        	    Table24.getDefaultCell().setBorder(Rectangle.BOX);
//		        	    Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//		        	    Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//		        	    Table24.addCell(new Phrase(new Chunk("Marketing Margin",small_black_normal)));
//
//		        	    Table24.getDefaultCell().setBorder(Rectangle.BOX);
//		        	    Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//		        	    Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//		        	    Table24.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//		        	    Table24.getDefaultCell().setBorder(Rectangle.BOX);
//		        	    Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//		        	    Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//		        	    Table24.addCell(new Phrase(new Chunk(""+curr1,small_black_normal)));
//
//		        	    Table24.getDefaultCell().setBorder(Rectangle.BOX);
//		        	    Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//		        	    Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//		        	    Table24.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//		        	    Table24.getDefaultCell().setBorder(Rectangle.BOX);
//		        	    Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//		        	    Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//		        	    Table24.addCell(new Phrase(new Chunk(marketing_margin,small_black_normal)));
//
//		        	    Table24.getDefaultCell().setBorder(Rectangle.BOX);
//		        	    Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//		        	    Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//		        	    Table24.addCell(new Phrase(new Chunk(marketing_margin_amount,small_black_normal)));
//	            	}
//	            }
//
//	            if(!other_charges.equals(""))
//	            {
//	            	if(Double.parseDouble(other_charges_amount) > 0)
//	            	{
//		            	sr+=1;
//
//		        	    Table24.setWidthPercentage(100);
//
//		        	    Table24.getDefaultCell().setBorder(Rectangle.BOX);
//		        	    Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//		        	    Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//		        	    Table24.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
//
//		        	    Table24.getDefaultCell().setBorder(Rectangle.BOX);
//		        	    Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//		        	    Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//		        	    Table24.addCell(new Phrase(new Chunk("Other Charges",small_black_normal)));
//
//		        	    Table24.getDefaultCell().setBorder(Rectangle.BOX);
//		        	    Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//		        	    Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//		        	    Table24.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//		        	    Table24.getDefaultCell().setBorder(Rectangle.BOX);
//		        	    Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//		        	    Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//		        	    Table24.addCell(new Phrase(new Chunk(""+curr1,small_black_normal)));
//
//		        	    Table24.getDefaultCell().setBorder(Rectangle.BOX);
//		        	    Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//		        	    Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//		        	    Table24.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//		        	    Table24.getDefaultCell().setBorder(Rectangle.BOX);
//		        	    Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//		        	    Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//		        	    Table24.addCell(new Phrase(new Chunk(other_charges,small_black_normal)));
//
//		        	    Table24.getDefaultCell().setBorder(Rectangle.BOX);
//		        	    Table24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//		        	    Table24.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//		        	    Table24.addCell(new Phrase(new Chunk(other_charges_amount,small_black_normal)));
//	            	}
//	            }
//
//	            if(isGrossIncTriff)
//	            {
//	            	sr+=1;
//
//	        	    Table25.setWidthPercentage(100);
//
//	        	    Table25.getDefaultCell().setBorder(Rectangle.BOX);
//	        	    Table25.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//	        	    Table25.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//	        	    Table25.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
//
//	        	    Table25.getDefaultCell().setBorder(Rectangle.BOX);
//	        	    Table25.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//	        	    Table25.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//	        	    Table25.addCell(new Phrase(new Chunk("Total Gross Amount",small_black_normal)));
//
//	        	    Table25.getDefaultCell().setBorder(Rectangle.BOX);
//	        	    Table25.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//	        	    Table25.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//	        	    Table25.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//	        	    Table25.getDefaultCell().setBorder(Rectangle.BOX);
//	        	    Table25.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//	        	    Table25.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//	        	    Table25.addCell(new Phrase(new Chunk(""+curr1,small_black_normal)));
//
//	        	    Table25.getDefaultCell().setBorder(Rectangle.BOX);
//	        	    Table25.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//	        	    Table25.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//	        	    Table25.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//	        	    Table25.getDefaultCell().setBorder(Rectangle.BOX);
//	        	    Table25.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//	        	    Table25.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//	        	    Table25.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//	        	    Table25.getDefaultCell().setBorder(Rectangle.BOX);
//	        	    Table25.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//	        	    Table25.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//	        	    Table25.addCell(new Phrase(new Chunk(gross_include_transport_tariff,small_black_normal)));
//	            }

//	            sr+=1;
//	            float[] ContactAddrWidths17 = {0.08F,0.60F,0.20F,0.20F,0.20F,0.20F,0.20F};
//	   	     	PdfPTable Table15 = new PdfPTable(ContactAddrWidths17);
//	   	     	/*if(!contract_type.equals("I")) {
//	   	     	Table15.getDefaultCell().setBorderWidthTop(0);
//	   	     	}else {
//	   	     	Table15.getDefaultCell().setBorderWidthTop(0);
//	   	     	Table15.getDefaultCell().setBorderWidthBottom(0);
//	   	     	}*/
//
	   	     	sr+=1;
	   	     	float[] ContactAddrWidths18 = {0.08F,0.60F,0.20F,0.20F,0.20F,0.20F,0.20F};
	   	     	PdfPTable Table16 = new PdfPTable(ContactAddrWidths16);
	   	     	Table16.setWidthPercentage(100);

	   	     	Table16.setWidthPercentage(100);
			   	Table16.getDefaultCell().setBorder(Rectangle.BOX);
			   	Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			   	Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			   	Table16.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

			   	Table16.getDefaultCell().setBorder(Rectangle.BOX);
			   	Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			   	Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
			   	Table16.addCell(new Phrase(new Chunk("Tax ("+tax_struct_dtl+")",small_black_normal)));

			   	Table16.getDefaultCell().setBorder(Rectangle.BOX);
			   	Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			   	Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			   	Table16.addCell(new Phrase(new Chunk("",small_black_normal)));

			   	Table16.getDefaultCell().setBorder(Rectangle.BOX);
			   	Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			   	Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			   	Table16.addCell(new Phrase(new Chunk(""+curr1,small_black_normal)));

			   	Table16.getDefaultCell().setBorder(Rectangle.BOX);
			   	Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			   	Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			   	Table16.addCell(new Phrase(new Chunk("",small_black_normal)));

			   	Table16.getDefaultCell().setBorder(Rectangle.BOX);
			   	Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			   	Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			   	Table16.addCell(new Phrase(new Chunk("",small_black_normal)));

			   	Table16.getDefaultCell().setBorder(Rectangle.BOX);
			   	Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			   	Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
			   	Table16.addCell(new Phrase(new Chunk(tax_amt,small_black_normal)));

	            queryString1="SELECT COUNT(*) "
						+ "FROM FMS_INV_TAX_DTL "
						+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
						+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? ";
	            stmt1=conn.prepareStatement(queryString1);
	            stmt1.setString(1, comp_cd);
	            stmt1.setString(2, bu_state_tin);
	            stmt1.setString(3, financial_year);
	            stmt1.setString(4, inv_seq);
	            rset1=stmt1.executeQuery();
				if(rset1.next())
				{
					if(rset1.getInt(1)>1)
					{
						BigDecimal tax_amt1 = BigDecimal.valueOf(Double.parseDouble(tax_amt));
						BigDecimal factor = new BigDecimal(2);


						double temp_srno=sr;
						queryString="SELECT TAX_CODE,TAX_DESCR,TAX_AMT,TAX_BASE_AMT "
								+ "FROM FMS_INV_TAX_DTL "
								+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
								+ "AND FINANCIAL_YEAR=? AND INVOICE_SEQ=? ";
						stmt=conn.prepareStatement(queryString);
			            stmt.setString(1, comp_cd);
			            stmt.setString(2, bu_state_tin);
			            stmt.setString(3, financial_year);
			            stmt.setString(4, inv_seq);
			            rset=stmt.executeQuery();
						while(rset.next())
						{
							BigDecimal sub_tax_amt = tax_amt1.divide(factor, 2, RoundingMode.HALF_UP);
							temp_srno=temp_srno+0.1;

							String tax_descr=rset.getString(2)==null?"":rset.getString(2);
							String taxAmt=rset.getString(3)==null?"":nf.format(rset.getDouble(3));

							Table16.setWidthPercentage(100);
							Table16.getDefaultCell().setBorder(Rectangle.BOX);
							Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table16.addCell(new Phrase(new Chunk(""+nf0.format(temp_srno),small_black_normal)));

				            Table16.getDefaultCell().setBorder(Rectangle.BOX);
				            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
				            Table16.addCell(new Phrase(new Chunk(""+tax_descr+"",small_black_normal)));

				            Table16.getDefaultCell().setBorder(Rectangle.BOX);
				            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table16.addCell(new Phrase(new Chunk("",small_black_normal)));

				            Table16.getDefaultCell().setBorder(Rectangle.BOX);
				            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				            Table16.addCell(new Phrase(new Chunk(""+curr1,small_black_normal)));

				            Table16.getDefaultCell().setBorder(Rectangle.BOX);
				            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
				            Table16.addCell(new Phrase(new Chunk("",small_black_normal)));

				            Table16.getDefaultCell().setBorder(Rectangle.BOX);
				            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
				            Table16.addCell(new Phrase(new Chunk("",small_black_normal)));

				            Table16.getDefaultCell().setBorder(Rectangle.BOX);
				            Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				            Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
				            Table16.addCell(new Phrase(new Chunk(""+sub_tax_amt,small_black_normal)));
						}
						rset.close();
						stmt.close();
					}
				}
				rset1.close();
				stmt1.close();

	            /*float[] ContactAddrWidths23 = {0.05F,0.60F,0.20F,0.20F,0.20F,0.20F};
	   	     	PdfPTable Table21 = new PdfPTable(ContactAddrWidths23);

	            float[] ContactAddrWidths24 = {0.05F,0.60F,0.20F,0.20F,0.20F,0.20F};
	   	     	PdfPTable Table25 = new PdfPTable(ContactAddrWidths24);
	   	     	if(contract_type.equals("I"))
	            {
	            	sr+=1;
	            	Table21.getDefaultCell().setBorderWidthTop(0);
	            	Table21.getDefaultCell().setBorderWidthBottom(0);

	            	Table21.setWidthPercentage(100);
	                Table21.getDefaultCell().setBorder(Rectangle.BOX);
	                Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	                Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	                Table21.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

	                Table21.getDefaultCell().setBorder(Rectangle.BOX);
	                Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	                Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);

	                if(inv_type.equals("P"))
	                {
	                	Table21.addCell(new Phrase(new Chunk("IGX TXN Charges",small_black_normal)));
	                }
	                else
	                {
	                	Table21.addCell(new Phrase(new Chunk("IGX TXN Charges ("+invoice_data.get("TxnChrg")+"%)",small_black_normal)));
	                }
	                Table21.getDefaultCell().setBorder(Rectangle.BOX);
	                Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	                Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	                Table21.addCell(new Phrase(new Chunk("",small_black_normal)));

	                Table21.getDefaultCell().setBorder(Rectangle.BOX);
	                Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	                Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	                Table21.addCell(new Phrase(new Chunk(""+curr1,small_black_normal)));

	                Table21.getDefaultCell().setBorder(Rectangle.BOX);
	                Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	                Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	                Table21.addCell(new Phrase(new Chunk("",small_black_normal)));

	                Table21.getDefaultCell().setBorder(Rectangle.BOX);
	                Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	                Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	                Table21.addCell(new Phrase(new Chunk("",small_black_normal)));

	                Table21.getDefaultCell().setBorder(Rectangle.BOX);
	                Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	                Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	                Table21.addCell(new Phrase(new Chunk(""+invoice_data.get("TxnAmount"),small_black_normal)));

	                sr+=1;
	                Table22.getDefaultCell().setBorderWidthTop(0);

	       	     	Table22.setWidthPercentage(100);
	                Table22.getDefaultCell().setBorder(Rectangle.BOX);
	                Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	                Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	                Table22.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

	                Table22.getDefaultCell().setBorder(Rectangle.BOX);
	                Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	                Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);

	                if(inv_type.equals("P"))
	                {
	                	Table22.addCell(new Phrase(new Chunk("Tax on IGX TXN",small_black_normal)));
	                }
	                else
	                {
	                	Table22.addCell(new Phrase(new Chunk("Tax on IGX TXN ("+invoice_data.get("TaxTxnDtl")+")",small_black_normal)));
	                }
	                Table22.getDefaultCell().setBorder(Rectangle.BOX);
	                Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	                Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	                Table22.addCell(new Phrase(new Chunk("",small_black_normal)));

	                Table22.getDefaultCell().setBorder(Rectangle.BOX);
	                Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	                Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	                Table22.addCell(new Phrase(new Chunk(""+curr1,small_black_normal)));

	                Table22.getDefaultCell().setBorder(Rectangle.BOX);
	                Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	                Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	                Table22.addCell(new Phrase(new Chunk("",small_black_normal)));

	                Table22.getDefaultCell().setBorder(Rectangle.BOX);
	                Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	                Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	                Table22.addCell(new Phrase(new Chunk("",small_black_normal)));

	                Table22.getDefaultCell().setBorder(Rectangle.BOX);
	                Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	                Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	                Table22.addCell(new Phrase(new Chunk(""+invoice_data.get("TaxTxnChrg"),small_black_normal)));
	            }*/

				String invAmtLbl=inv_flag.equals("UG")?"Invoice Amount - GST on SUG":"Invoice Amount";
	            sr+=1;
	            float[] ContactAddrWidths19 = {0.08F,0.60F,0.20F,0.20F,0.20F,0.20F,0.20F};
	   	     	PdfPTable Table17 = new PdfPTable(ContactAddrWidths18);
	   	     	//Table16.getDefaultCell().setBorderWidthBottom(0);

	   	     	Table17.setWidthPercentage(100);
	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table17.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table17.addCell(new Phrase(new Chunk(invAmtLbl,small_black_normal)));

	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table17.addCell(new Phrase(new Chunk("",small_black_normal)));

	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table17.addCell(new Phrase(new Chunk(""+curr1,small_black_normal)));

	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table17.addCell(new Phrase(new Chunk("",small_black_normal)));

	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table17.addCell(new Phrase(new Chunk("",small_black_normal)));

	            Table17.getDefaultCell().setBorder(Rectangle.BOX);
	            Table17.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table17.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table17.addCell(new Phrase(new Chunk(invoice_amt,small_black_normal)));
//
//	            if(applicable_abbr.equals("TCS"))
//	            {
//	            	sr+=1;
//
//	            	Table16.setWidthPercentage(100);
//	                Table16.getDefaultCell().setBorder(Rectangle.BOX);
//	                Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//	                Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//	                Table16.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));
//
//	                Table16.getDefaultCell().setBorder(Rectangle.BOX);
//	                Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//	                Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
//	                Table16.addCell(new Phrase(new Chunk("TCS",small_black_normal)));
//
//	                Table16.getDefaultCell().setBorder(Rectangle.BOX);
//	                Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//	                Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//	                Table16.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//	                Table16.getDefaultCell().setBorder(Rectangle.BOX);
//	                Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//	                Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
//	                Table16.addCell(new Phrase(new Chunk(""+curr1,small_black_normal)));
//
//	                Table16.getDefaultCell().setBorder(Rectangle.BOX);
//	                Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//	                Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//	                Table16.addCell(new Phrase(new Chunk("",small_black_normal)));
//
//	                Table16.getDefaultCell().setBorder(Rectangle.BOX);
//	                Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//	                Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//	                Table16.addCell(new Phrase(new Chunk(TCS_factor+"%",small_black_normal)));
//
//	                Table16.getDefaultCell().setBorder(Rectangle.BOX);
//	                Table16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//	                Table16.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
//	                Table16.addCell(new Phrase(new Chunk(applicable_amt,small_black_normal)));
//	            }

	            sr+=1;
	            float[] ContactAddrWidths20 = {0.08F,0.60F,0.20F,0.20F,0.20F,0.20F,0.20F};
	   	     	PdfPTable Table18 = new PdfPTable(ContactAddrWidths20);

	   	     	Table18.setWidthPercentage(100);
	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk(""+sr,black_bold)));

	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	            Table18.addCell(new Phrase(new Chunk("Net Amount Payable",black_bold)));

	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk("",black_bold)));

	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
	            Table18.addCell(new Phrase(new Chunk(""+curr1,black_bold)));

	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table18.addCell(new Phrase(new Chunk("",black_bold)));

	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table18.addCell(new Phrase(new Chunk("",black_bold)));

	            Table18.getDefaultCell().setBorder(Rectangle.BOX);
	            Table18.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            Table18.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
	            Table18.addCell(new Phrase(new Chunk(net_payable,black_bold)));

	            float[] ContactAddrWidths21 = {0.100F};
	   	     	PdfPTable Table19 = new PdfPTable(ContactAddrWidths21);
	   	     	Table19.setWidthPercentage(100);
	   	     	Table19.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	   	     	Table19.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	   	     	Table19.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	   	     	Table19.addCell(new Phrase(new Chunk(remark_1,small_black_normal)));

	   	     	float[] ContactAddrWidths22 = {0.100F};
		     	PdfPTable Table20 = new PdfPTable(ContactAddrWidths22);
		     	Table20.setWidthPercentage(100);
		     	Table20.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table20.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table20.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table20.addCell(new Phrase(new Chunk(remark_2,small_black_normal)));

		     	float[] ContactAddrWidths23 = {0.100F};
	   	     	PdfPTable Table21 = new PdfPTable(ContactAddrWidths21);
	   	     	Table21.setWidthPercentage(100);
	   	     	Table21.getDefaultCell().setBorder(Rectangle.NO_BORDER);
	   	     	Table21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	   	  		Table21.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
	   	  		Table21.addCell(new Phrase(new Chunk("For "+company_nm,black_bold)));

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
	   	     	sigCell.setFixedHeight(50f);
	   	     	BillingFieldsInfoTable81.addCell(sigCell);

	   	     	float[] ContactAddrWidths24 = {0.100F};
		     	PdfPTable Table22 = new PdfPTable(ContactAddrWidths22);

		     	Table22.setWidthPercentage(100);
		     	Table22.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		     	Table22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		     	Table22.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		     	Table22.addCell(new Phrase(new Chunk("Authorised Signatory",black_bold)));

		     	document.add(LogoTable);
				document.add(HlplLogoTable);
				document.add(Table);
				document.add(Table1);
				document.add(Table2);
				document.add(new Paragraph("  "));
				document.add(Table3);
				document.add(Table4);
				document.add(Table5);
				document.add(Table6);
				document.add(Table7);
				document.add(new Paragraph("  "));
				document.add(new Paragraph("  "));
				if(!irn_no.equals("") && !qr_code.equals("") && (contract_type.equals("Q") || contract_type.equals("O")))
				{
					document.add(InvoiceDateInfoTable_qr);
				}
				else
				{
					document.add(Table8);
					if (drcr_due_dt!=null) {
						document.add(Table9);
					}
					document.add(Table10);
					if(!inv_flag.equals("UG") && !inv_flag.equals("ST"))
		            {
						document.add(Table10_1);
		            }
				}
				document.add(new Paragraph("  "));
				document.add(Table11);
				document.add(Table12);
				if(criteria.equals("3")) {
					document.add(Table24);
					document.add(Table25);
				}
				if (!criteria.equals("1")) {
					if (!price_cd.equals(invoice_raised_in)) {
						document.add(Table13);
					}
				}
				if(!inv_flag.equals("UG"))
	   	     	{
					document.add(Table14);
	   	     	}
				//if(agmt_base.equals("D"))
//				{
//					document.add(Table24);
//					document.add(Table25);
//				}
				document.add(Table15);
				/*if(contract_type.equals("I"))
	            {
					document.add(Table21);
					document.add(Table22);
	            }*/
				document.add(Table16);
				document.add(Table17);
				document.add(Table18);
				document.add(new Paragraph("  "));
				document.add(Table19);
				document.add(Table20);
				document.add(new Paragraph("  "));
				document.add(new Paragraph("  "));
				document.add(Table21);
				document.add(BillingFieldsInfoTable81);
				document.add(Table22);


				//For Attachment 1
				Font big_black_bold = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
				Font small_black_bold2 = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);

				document.setPageSize(pageSize1);
	            document.newPage();

				if(criteria.equals("1"))
				{
		            float[] att1_ContactAddrWidths1 = {0.80f,0.10F,0.20F,0.80F};
		   	     	PdfPTable att1_Table1 = new PdfPTable(att1_ContactAddrWidths1);
		            att1_Table1.setWidthPercentage(100);
		            att1_Table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            //att1_Table1.addCell(new Phrase(new Chunk("Business Unit: "+bu_plantNm,black_bold)));
		            att1_Table1.addCell(new Phrase(new Chunk("Business Unit: ",black_bold)));

		            att1_Table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table1.addCell(new Phrase(new Chunk("",black_bold)));

		            att1_Table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table1.addCell(new Phrase(new Chunk("To:",black_bold)));

		            att1_Table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table1.addCell(new Phrase(new Chunk(""+contact_person_nm+",",black_bold)));

					float[] att1_ContactAddrWidths2 = {0.80f,0.30F,0.80F};
		   	     	PdfPTable att1_Table2 = new PdfPTable(att1_ContactAddrWidths2);
		   	     	att1_Table2.setWidthPercentage(100);
		            att1_Table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table2.addCell(new Phrase(new Chunk(company_nm,small_black_normal)));

		            att1_Table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table2.addCell(new Phrase(new Chunk("",small_black_normal)));

		            att1_Table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table2.addCell(new Phrase(new Chunk(counterparty_nm,small_black_normal)));

		            float[] att1_ContactAddrWidths3 = {0.80f,0.30F,0.80F};
		   	     	PdfPTable att1_Table3 = new PdfPTable(att1_ContactAddrWidths3);
		            att1_Table3.setWidthPercentage(100);
		            att1_Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table3.addCell(new Phrase(new Chunk(bu_plantAddress+","+bu_plantCity,small_black_normal)));

		            att1_Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table3.addCell(new Phrase(new Chunk("",small_black_normal)));

		            att1_Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table3.addCell(new Phrase(new Chunk(plantAddress+","+plantCity,small_black_normal)));


		            float[] att1_ContactAddrWidths4 = {0.80f,0.30F,0.80F};
		   	     	PdfPTable att1_Table4 = new PdfPTable(att1_ContactAddrWidths4);
		            att1_Table4.setWidthPercentage(100);
		            att1_Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table4.addCell(new Phrase(new Chunk(bu_plantState+" - "+bu_plantPin,small_black_normal)));

		            att1_Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table4.addCell(new Phrase(new Chunk("",small_black_normal)));

		            att1_Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table4.addCell(new Phrase(new Chunk(plantState+" - "+plantPin,small_black_normal)));


		            String title_note="ATTACHMENT 1 - Applicable Exchange Rate";
//		            if(inv_flag.equals("ST"))
//		            {
//		            	title_note="ATTACHMENT 1 - Storage Inventory & Storage Charges";
//		            }

		            PdfPTable title_note_table = new PdfPTable(1);
		            title_note_table.setWidthPercentage(100);
		            title_note_table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            title_note_table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            title_note_table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            title_note_table.addCell(new Phrase(new Chunk(title_note,big_black_bold)));

		            float[] att1_ContactAddrWidths5 = {0.80F,0.30F,0.30F,0.30F};
		   	     	PdfPTable att1_Table5 = new PdfPTable(att1_ContactAddrWidths5);
		            att1_Table5.setWidthPercentage(100);
		            att1_Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table5.addCell(new Phrase(new Chunk("",small_black_normal)));

		            att1_Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table5.addCell(new Phrase(new Chunk("",small_black_normal)));

		            att1_Table5.getDefaultCell().setBorder(Rectangle.BOX);
		            att1_Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table5.addCell(new Phrase(new Chunk(type+" Note Date :",black_bold)));

		            att1_Table5.getDefaultCell().setBorder(Rectangle.BOX);
		            att1_Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            //att1_Table5.addCell(new Phrase(new Chunk(dateUtil.getDateFormatDD_MOM_YY(invoice_dt),small_black_normal)));
		            att1_Table5.addCell(new Phrase(new Chunk(drcr_dt,black_bold)));

		            float[] att1_ContactAddrWidths6 = {0.80F,0.30F,0.30F,0.30F};
		   	     	PdfPTable att1_Table6 = new PdfPTable(att1_ContactAddrWidths6);
		            att1_Table6.setWidthPercentage(100);
		            att1_Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table6.addCell(new Phrase(new Chunk("",small_black_normal)));

		            att1_Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table6.addCell(new Phrase(new Chunk("",small_black_normal)));

		            att1_Table6.getDefaultCell().setBorder(Rectangle.BOX);
		            att1_Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table6.addCell(new Phrase(new Chunk("Payment Due Date :",black_bold)));

		            att1_Table6.getDefaultCell().setBorder(Rectangle.BOX);
		            att1_Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            //att1_Table6.addCell(new Phrase(new Chunk(dateUtil.getDateFormatDD_MOM_YY(invoice_due_dt),small_black_normal)));
		            att1_Table6.addCell(new Phrase(new Chunk(drcr_due_dt,black_bold)));

		            float[] att1_ContactAddrWidths7 = {0.80F,0.30F,0.30F,0.30F};
		   	     	PdfPTable att1_Table7 = new PdfPTable(att1_ContactAddrWidths7);
		            att1_Table7.setWidthPercentage(100);
		            att1_Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table7.addCell(new Phrase(new Chunk("",black_bold)));

		            att1_Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table7.addCell(new Phrase(new Chunk("",small_black_normal)));

		            att1_Table7.getDefaultCell().setBorder(Rectangle.BOX);
		            att1_Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table7.addCell(new Phrase(new Chunk(company_abbr+" "+type+" Note No:",black_bold)));

		            att1_Table7.getDefaultCell().setBorder(Rectangle.BOX);
		            att1_Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table7.addCell(new Phrase(new Chunk(drcr_no,black_bold)));

		            float[] att1_ContactAddrWidths8 = {0.80F,0.30F,0.30F,0.30F};
		   	     	PdfPTable att1_Table8 = new PdfPTable(att1_ContactAddrWidths8);
		   	     	att1_Table8.setWidthPercentage(100);
		            att1_Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table8.addCell(new Phrase(new Chunk("",black_bold)));

		            att1_Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table8.addCell(new Phrase(new Chunk("",black_bold)));

		            att1_Table8.getDefaultCell().setBorder(Rectangle.BOX);
		            att1_Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table8.addCell(new Phrase(new Chunk("Billing Period : ",black_bold)));

		            att1_Table8.getDefaultCell().setBorder(Rectangle.BOX);
		            att1_Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table8.addCell(new Phrase(new Chunk(""+period_start_dt+" to "+period_end_dt,black_bold)));


		            if(!invoice_raised_in.equals(price_cd))
		            {
		            	String component_flag="";
		            	String component1="";
		            	String component2="";
		            	String exchang_rate_nm="";
		            	String component1_rate_nm="";
		            	String component2_rate_nm="";
		            	String component1_rate="";
		            	String component2_rate="";

		            	queryString="SELECT COMPONENT_FLAG,COMPONENT1,COMPONENT2,EXC_RATE_NM,BANK_ABBR "
								+ "FROM FMS_EXCHG_RATE_MST "
								+ "WHERE EXC_RATE_CD=?";
						stmt=conn.prepareStatement(queryString);
						stmt.setString(1, exchang_rate_cd);
		            	rset=stmt.executeQuery();
						if(rset.next())
						{
							component_flag=rset.getString(1)==null?"":rset.getString(1);
							component1=rset.getString(2)==null?"":rset.getString(2);
							component2=rset.getString(3)==null?"":rset.getString(3);
							exchang_rate_nm=rset.getString(4)==null?"":rset.getString(4);
							String source=rset.getString(5)==null?"":rset.getString(5);
						}
						rset.close();
						stmt.close();

//						if(exchang_rate_type.equals("A"))
//						{
//							queryString1="SELECT EXCHG_RATE_VALUE,TO_CHAR(ALLOCATION_DT,'DD-MON-YY') "
//									+ "FROM FMS_INVOICE_DTL "
//									+ "WHERE COMPANY_CD=? AND FINANCIAL_YEAR=? "
//									+ "AND BU_STATE_TIN=? AND INVOICE_SEQ=? "
//									+ "ORDER BY ALLOCATION_DT";
//							stmt1=conn.prepareStatement(queryString1);
//							stmt1.setString(1, comp_cd);
//							stmt1.setString(2, financial_year);
//							stmt1.setString(3, bu_state_tin);
//							stmt1.setString(4, inv_seq);
//							rset1=stmt1.executeQuery();
//							while(rset1.next())
//							{
//								String ExRate=nf2.format(rset1.getDouble(1));
//								String allocDt=rset1.getString(2)==null?"":rset1.getString(2);
//							}
//							rset1.close();
//							stmt1.close();
//						}
//						else if(component_flag.equals("Y"))
//						{
//							queryString1="SELECT EXC_RATE_NM "
//									+ "FROM FMS_EXCHG_RATE_MST "
//									+ "WHERE EXC_RATE_CD=?";
//							stmt1=conn.prepareStatement(queryString1);
//							stmt1.setString(1, component1);
//							rset1=stmt1.executeQuery();
//							if(rset1.next())
//							{
//								component1_rate_nm=rset1.getString(1)==null?"":rset1.getString(1);
//							}
//							rset1.close();
//							stmt1.close();
//
//							queryString2="SELECT EXC_RATE_NM "
//									+ "FROM FMS_EXCHG_RATE_MST "
//									+ "WHERE EXC_RATE_CD=?";
//							stmt2=conn.prepareStatement(queryString2);
//							stmt2.setString(1, component2);
//							rset2=stmt2.executeQuery();
//							if(rset2.next())
//							{
//								component2_rate_nm=rset2.getString(1)==null?"":rset2.getString(1);
//							}
//							rset2.close();
//							stmt2.close();
//
//							queryString3="SELECT EXCHG_VAL "
//									+ "FROM FMS_EXCHG_RATE_ENTRY "
//									+ "WHERE EXCHG_RATE_CD=? "
//									+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY')";
//							stmt3=conn.prepareStatement(queryString3);
//							stmt3.setString(1, component1);
//							stmt3.setString(2, exchang_rate_dt);
//							rset3=stmt3.executeQuery();
//							if(rset3.next())
//							{
//								component1_rate=nf2.format(rset3.getDouble(1));
//							}
//							rset3.close();
//							stmt3.close();
//
//							queryString4="SELECT EXCHG_VAL "
//									+ "FROM FMS_EXCHG_RATE_ENTRY "
//									+ "WHERE EXCHG_RATE_CD=? "
//									+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY')";
//							stmt4=conn.prepareStatement(queryString4);
//							stmt4.setString(1, component2);
//							stmt4.setString(2, exchang_rate_dt);
//							rset4=stmt4.executeQuery();
//							if(rset4.next())
//							{
//								component2_rate=nf2.format(rset4.getDouble(1));
//							}
//							rset4.close();
//							stmt4.close();
//						}

		            	sr=1;
		            	//float[] att1_ContactAddrWidths9 = {0.1F, 0.05F, 0.02F, 0.15F};
		            	float[] att1_ContactAddrWidths9 = {0.002F, 0.06F, 0.02F, 0.15F};
			   	     	PdfPTable att1_Table9 = new PdfPTable(att1_ContactAddrWidths9);
			   	     	att1_Table9.setWidthPercentage(100);

			   	     	att1_Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            att1_Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table9.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

			            att1_Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            att1_Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table9.addCell(new Phrase(new Chunk(exchang_rate_nm+".. On "+invoice_dt,small_black_normal)));

			            att1_Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            att1_Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table9.addCell(new Phrase(new Chunk(item_amt,small_black_normal)));

			            att1_Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            att1_Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table9.addCell(new Phrase(new Chunk(curr,small_black_normal)));

			            sr+=1;
			            float[] att1_ContactAddrWidths10 = {0.002F, 0.06F, 0.02F, 0.15F};
			   	     	PdfPTable att1_Table10 = new PdfPTable(att1_ContactAddrWidths10);
			   	     	att1_Table10.setWidthPercentage(100);

			   	     	att1_Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            att1_Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table10.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

			            att1_Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            att1_Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table10.addCell(new Phrase(new Chunk("Applicable Exchange Rate",small_black_normal)));

			            att1_Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            att1_Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table10.addCell(new Phrase(new Chunk(item_diff_amt,small_black_normal)));

			            att1_Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            att1_Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table10.addCell(new Phrase(new Chunk(curr,small_black_normal)));


			            float[] att1_ContactAddrWidths11 = {0.002F, 0.06F, 0.02F, 0.15F};
			   	     	PdfPTable att1_Table11 = new PdfPTable(att1_ContactAddrWidths11);
			   	     	att1_Table11.setWidthPercentage(100);

			   	     	att1_Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            att1_Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table11.addCell(new Phrase(new Chunk("",small_black_normal)));

			            att1_Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            att1_Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table11.addCell(new Phrase(new Chunk("Difference in Exchange Rate",black_bold)));

			            att1_Table11.getDefaultCell().setBorder(Rectangle.BOX);
			            att1_Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            att1_Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table11.addCell(new Phrase(new Chunk(diff_value,black_bold)));

			            att1_Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            att1_Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table11.addCell(new Phrase(new Chunk(curr,small_black_normal)));


			            document.add(LogoTable);
			            document.add(att1_Table1);
			            document.add(att1_Table2);
			            document.add(att1_Table3);
			            document.add(att1_Table4);
			            document.add(new Paragraph("              "));
			            document.add(title_note_table);
			            document.add(new Paragraph("              "));
			            document.add(att1_Table5);
			            document.add(att1_Table6);
			            document.add(att1_Table7);
			            if(!inv_flag.equals("ST"))
			            {
			            	document.add(att1_Table8);
			            	document.add(new Paragraph("              "));
			            	document.add(att1_Table9);
			            	document.add(att1_Table10);
			            	document.add(new Paragraph("              "));
			            	document.add(att1_Table11);
			            }
		            }

				}
				else if(criteria.equals("2"))
				{
		            float[] att1_ContactAddrWidths1 = {0.80f,0.10F,0.20F,0.80F};
		   	     	PdfPTable att1_Table1 = new PdfPTable(att1_ContactAddrWidths1);
		            att1_Table1.setWidthPercentage(100);
		            att1_Table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            //att1_Table1.addCell(new Phrase(new Chunk("Business Unit: "+bu_plantNm,black_bold)));
		            att1_Table1.addCell(new Phrase(new Chunk("Business Unit: ",black_bold)));

		            att1_Table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table1.addCell(new Phrase(new Chunk("",black_bold)));

		            att1_Table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table1.addCell(new Phrase(new Chunk("To:",black_bold)));

		            att1_Table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table1.addCell(new Phrase(new Chunk(""+contact_person_nm+",",black_bold)));

					float[] att1_ContactAddrWidths2 = {0.80f,0.30F,0.80F};
		   	     	PdfPTable att1_Table2 = new PdfPTable(att1_ContactAddrWidths2);
		   	     	att1_Table2.setWidthPercentage(100);
		            att1_Table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table2.addCell(new Phrase(new Chunk(company_nm,small_black_normal)));

		            att1_Table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table2.addCell(new Phrase(new Chunk("",small_black_normal)));

		            att1_Table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table2.addCell(new Phrase(new Chunk(counterparty_nm,small_black_normal)));

		            float[] att1_ContactAddrWidths3 = {0.80f,0.30F,0.80F};
		   	     	PdfPTable att1_Table3 = new PdfPTable(att1_ContactAddrWidths3);
		            att1_Table3.setWidthPercentage(100);
		            att1_Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table3.addCell(new Phrase(new Chunk(bu_plantAddress+","+bu_plantCity,small_black_normal)));

		            att1_Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table3.addCell(new Phrase(new Chunk("",small_black_normal)));

		            att1_Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table3.addCell(new Phrase(new Chunk(plantAddress+","+plantCity,small_black_normal)));


		            float[] att1_ContactAddrWidths4 = {0.80f,0.30F,0.80F};
		   	     	PdfPTable att1_Table4 = new PdfPTable(att1_ContactAddrWidths4);
		            att1_Table4.setWidthPercentage(100);
		            att1_Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table4.addCell(new Phrase(new Chunk(bu_plantState+" - "+bu_plantPin,small_black_normal)));

		            att1_Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table4.addCell(new Phrase(new Chunk("",small_black_normal)));

		            att1_Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table4.addCell(new Phrase(new Chunk(plantState+" - "+plantPin,small_black_normal)));

		            String title_note="ATTACHMENT 1 - Applicable Sales Rate";
		            if(inv_flag.equals("ST"))
		            {
		            	title_note="ATTACHMENT 1 - Storage Inventory & Storage Charges";
		            }

		            PdfPTable title_note_table = new PdfPTable(1);
		            title_note_table.setWidthPercentage(100);
		            title_note_table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            title_note_table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            title_note_table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            title_note_table.addCell(new Phrase(new Chunk(title_note,big_black_bold)));

		            float[] att1_ContactAddrWidths5 = {0.80F,0.30F,0.30F,0.30F};
		   	     	PdfPTable att1_Table5 = new PdfPTable(att1_ContactAddrWidths5);
		            att1_Table5.setWidthPercentage(100);
		            att1_Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table5.addCell(new Phrase(new Chunk("",small_black_normal)));

		            att1_Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table5.addCell(new Phrase(new Chunk("",small_black_normal)));

		            att1_Table5.getDefaultCell().setBorder(Rectangle.BOX);
		            att1_Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table5.addCell(new Phrase(new Chunk(type+" Note Date :",black_bold)));

		            att1_Table5.getDefaultCell().setBorder(Rectangle.BOX);
		            att1_Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            //att1_Table5.addCell(new Phrase(new Chunk(dateUtil.getDateFormatDD_MOM_YY(invoice_dt),small_black_normal)));
		            att1_Table5.addCell(new Phrase(new Chunk(drcr_dt,black_bold)));

		            float[] att1_ContactAddrWidths6 = {0.80F,0.30F,0.30F,0.30F};
		   	     	PdfPTable att1_Table6 = new PdfPTable(att1_ContactAddrWidths6);
		            att1_Table6.setWidthPercentage(100);
		            att1_Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table6.addCell(new Phrase(new Chunk("",small_black_normal)));

		            att1_Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table6.addCell(new Phrase(new Chunk("",small_black_normal)));

		            att1_Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table6.addCell(new Phrase(new Chunk("Payment Due Date :",black_bold)));

		            att1_Table6.getDefaultCell().setBorder(Rectangle.BOX);
		            att1_Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            //att1_Table6.addCell(new Phrase(new Chunk(dateUtil.getDateFormatDD_MOM_YY(invoice_due_dt),small_black_normal)));
		            att1_Table6.addCell(new Phrase(new Chunk(drcr_due_dt,black_bold)));

		            float[] att1_ContactAddrWidths7 = {0.80F,0.30F,0.30F,0.30F};
		   	     	PdfPTable att1_Table7 = new PdfPTable(att1_ContactAddrWidths7);
		            att1_Table7.setWidthPercentage(100);
		            att1_Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table7.addCell(new Phrase(new Chunk("",black_bold)));

		            att1_Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table7.addCell(new Phrase(new Chunk("",small_black_normal)));

		            att1_Table7.getDefaultCell().setBorder(Rectangle.BOX);
		            att1_Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table7.addCell(new Phrase(new Chunk(company_abbr+" "+type+" Note No:",black_bold)));

		            att1_Table7.getDefaultCell().setBorder(Rectangle.BOX);
		            att1_Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table7.addCell(new Phrase(new Chunk(drcr_no,black_bold)));

		            float[] att1_ContactAddrWidths8 = {0.80F,0.30F,0.30F,0.30F};
		   	     	PdfPTable att1_Table8 = new PdfPTable(att1_ContactAddrWidths8);
		   	     	att1_Table8.setWidthPercentage(100);
		            att1_Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table8.addCell(new Phrase(new Chunk("",black_bold)));

		            att1_Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table8.addCell(new Phrase(new Chunk("",black_bold)));

		            att1_Table8.getDefaultCell().setBorder(Rectangle.BOX);
		            att1_Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table8.addCell(new Phrase(new Chunk("Billing Period : ",black_bold)));

		            att1_Table8.getDefaultCell().setBorder(Rectangle.BOX);
		            att1_Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table8.addCell(new Phrase(new Chunk(""+period_start_dt+" to "+period_end_dt,black_bold)));


		            if(!invoice_raised_in.equals(price_cd))
		            {
		            	sr=1;
		            	//float[] att1_ContactAddrWidths9 = {0.1F, 0.05F, 0.02F, 0.15F};
		            	float[] att1_ContactAddrWidths9 = {0.002F, 0.05F, 0.02F, 0.15F};
			   	     	PdfPTable att1_Table9 = new PdfPTable(att1_ContactAddrWidths9);
			   	     	att1_Table9.setWidthPercentage(100);

			   	     	att1_Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            att1_Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table9.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

			            att1_Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            att1_Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table9.addCell(new Phrase(new Chunk("Invoice Sales Price Rate",small_black_normal)));

			            att1_Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            att1_Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table9.addCell(new Phrase(new Chunk(item_amt,small_black_normal)));

			            att1_Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            att1_Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table9.addCell(new Phrase(new Chunk(curr,small_black_normal)));

			            sr+=1;
			            float[] att1_ContactAddrWidths10 = {0.002F, 0.05F, 0.02F, 0.15F};
			   	     	PdfPTable att1_Table10 = new PdfPTable(att1_ContactAddrWidths10);
			   	     	att1_Table10.setWidthPercentage(100);

			   	     	att1_Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            att1_Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table10.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

			            att1_Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            att1_Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table10.addCell(new Phrase(new Chunk("Applicable Sales Price Rate",small_black_normal)));

			            att1_Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            att1_Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table10.addCell(new Phrase(new Chunk(item_diff_amt,small_black_normal)));

			            att1_Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            att1_Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table10.addCell(new Phrase(new Chunk(curr,small_black_normal)));


			            float[] att1_ContactAddrWidths11 = {0.002F, 0.05F, 0.02F, 0.15F};
			   	     	PdfPTable att1_Table11 = new PdfPTable(att1_ContactAddrWidths11);
			   	     	att1_Table11.setWidthPercentage(100);

			   	     	att1_Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            att1_Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table11.addCell(new Phrase(new Chunk("",small_black_normal)));

			            att1_Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            att1_Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table11.addCell(new Phrase(new Chunk("Difference in Sales Price Rate",black_bold)));

			            att1_Table11.getDefaultCell().setBorder(Rectangle.BOX);
			            att1_Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            att1_Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table11.addCell(new Phrase(new Chunk(diff_value,black_bold)));

			            att1_Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            att1_Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table11.addCell(new Phrase(new Chunk(curr,small_black_normal)));


			            document.add(LogoTable);
			            document.add(att1_Table1);
			            document.add(att1_Table2);
			            document.add(att1_Table3);
			            document.add(att1_Table4);
			            document.add(new Paragraph("              "));
			            document.add(title_note_table);
			            document.add(new Paragraph("              "));
			            document.add(att1_Table5);
			            document.add(att1_Table6);
			            document.add(att1_Table7);
			            if(!inv_flag.equals("ST"))
			            {
			            	document.add(att1_Table8);
			            	document.add(new Paragraph("              "));
			            	document.add(att1_Table9);
			            	document.add(att1_Table10);
			            	document.add(new Paragraph("              "));
			            	document.add(att1_Table11);
			            }
		            }
				}
				else if(criteria.equals("3"))
				{
		            float[] att1_ContactAddrWidths1 = {0.80f,0.10F,0.20F,0.80F};
		   	     	PdfPTable att1_Table1 = new PdfPTable(att1_ContactAddrWidths1);
		            att1_Table1.setWidthPercentage(100);
		            att1_Table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            //att1_Table1.addCell(new Phrase(new Chunk("Business Unit: "+bu_plantNm,black_bold)));
		            att1_Table1.addCell(new Phrase(new Chunk("Business Unit: ",black_bold)));

		            att1_Table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table1.addCell(new Phrase(new Chunk("",black_bold)));

		            att1_Table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table1.addCell(new Phrase(new Chunk("To:",black_bold)));

		            att1_Table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table1.addCell(new Phrase(new Chunk(""+contact_person_nm+",",black_bold)));

					float[] att1_ContactAddrWidths2 = {0.80f,0.30F,0.80F};
		   	     	PdfPTable att1_Table2 = new PdfPTable(att1_ContactAddrWidths2);
		   	     	att1_Table2.setWidthPercentage(100);
		            att1_Table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table2.addCell(new Phrase(new Chunk(company_nm,small_black_normal)));

		            att1_Table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table2.addCell(new Phrase(new Chunk("",small_black_normal)));

		            att1_Table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table2.addCell(new Phrase(new Chunk(counterparty_nm,small_black_normal)));

		            float[] att1_ContactAddrWidths3 = {0.80f,0.30F,0.80F};
		   	     	PdfPTable att1_Table3 = new PdfPTable(att1_ContactAddrWidths3);
		            att1_Table3.setWidthPercentage(100);
		            att1_Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table3.addCell(new Phrase(new Chunk(bu_plantAddress+","+bu_plantCity,small_black_normal)));

		            att1_Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table3.addCell(new Phrase(new Chunk("",small_black_normal)));

		            att1_Table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table3.addCell(new Phrase(new Chunk(plantAddress+","+plantCity,small_black_normal)));


		            float[] att1_ContactAddrWidths4 = {0.80f,0.30F,0.80F};
		   	     	PdfPTable att1_Table4 = new PdfPTable(att1_ContactAddrWidths4);
		            att1_Table4.setWidthPercentage(100);
		            att1_Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table4.addCell(new Phrase(new Chunk(bu_plantState+" - "+bu_plantPin,small_black_normal)));

		            att1_Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table4.addCell(new Phrase(new Chunk("",small_black_normal)));

		            att1_Table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            att1_Table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
		            att1_Table4.addCell(new Phrase(new Chunk(plantState+" - "+plantPin,small_black_normal)));

		            String title_note="ATTACHMENT 1 - Applicable Quantity";
		            if(inv_flag.equals("ST"))
		            {
		            	title_note="ATTACHMENT 1 - Storage Inventory & Storage Charges";
		            }

		            PdfPTable title_note_table = new PdfPTable(1);
		            title_note_table.setWidthPercentage(100);
		            title_note_table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            title_note_table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            title_note_table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
		            title_note_table.addCell(new Phrase(new Chunk(title_note,big_black_bold)));

		            float[] att1_ContactAddrWidths5 = {0.80F,0.30F,0.30F,0.30F};
		   	     	PdfPTable att1_Table5 = new PdfPTable(att1_ContactAddrWidths5);
		            att1_Table5.setWidthPercentage(100);
		            att1_Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table5.addCell(new Phrase(new Chunk("",small_black_normal)));

		            att1_Table5.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table5.addCell(new Phrase(new Chunk("",small_black_normal)));

		            att1_Table5.getDefaultCell().setBorder(Rectangle.BOX);
		            att1_Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table5.addCell(new Phrase(new Chunk(type+" Note Date :",black_bold)));

		            att1_Table5.getDefaultCell().setBorder(Rectangle.BOX);
		            att1_Table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            //att1_Table5.addCell(new Phrase(new Chunk(dateUtil.getDateFormatDD_MOM_YY(invoice_dt),small_black_normal)));
		            att1_Table5.addCell(new Phrase(new Chunk(drcr_dt,black_bold)));

		            float[] att1_ContactAddrWidths6 = {0.80F,0.30F,0.30F,0.30F};
		   	     	PdfPTable att1_Table6 = new PdfPTable(att1_ContactAddrWidths6);
		            att1_Table6.setWidthPercentage(100);
		            att1_Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table6.addCell(new Phrase(new Chunk("",small_black_normal)));

		            att1_Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table6.addCell(new Phrase(new Chunk("",small_black_normal)));

		            att1_Table6.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table6.addCell(new Phrase(new Chunk("Payment Due Date :",black_bold)));

		            att1_Table6.getDefaultCell().setBorder(Rectangle.BOX);
		            att1_Table6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table6.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            //att1_Table6.addCell(new Phrase(new Chunk(dateUtil.getDateFormatDD_MOM_YY(invoice_due_dt),small_black_normal)));
		            att1_Table6.addCell(new Phrase(new Chunk(drcr_due_dt,black_bold)));

		            float[] att1_ContactAddrWidths7 = {0.80F,0.30F,0.30F,0.30F};
		   	     	PdfPTable att1_Table7 = new PdfPTable(att1_ContactAddrWidths7);
		            att1_Table7.setWidthPercentage(100);
		            att1_Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table7.addCell(new Phrase(new Chunk("",black_bold)));

		            att1_Table7.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table7.addCell(new Phrase(new Chunk("",small_black_normal)));

		            att1_Table7.getDefaultCell().setBorder(Rectangle.BOX);
		            att1_Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table7.addCell(new Phrase(new Chunk(company_abbr+" "+type+" Note No:",black_bold)));

		            att1_Table7.getDefaultCell().setBorder(Rectangle.BOX);
		            att1_Table7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table7.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table7.addCell(new Phrase(new Chunk(drcr_no,black_bold)));

		            float[] att1_ContactAddrWidths8 = {0.80F,0.30F,0.30F,0.30F};
		   	     	PdfPTable att1_Table8 = new PdfPTable(att1_ContactAddrWidths8);
		   	     	att1_Table8.setWidthPercentage(100);
		            att1_Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table8.addCell(new Phrase(new Chunk("",black_bold)));

		            att1_Table8.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		            att1_Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table8.addCell(new Phrase(new Chunk("",black_bold)));

		            att1_Table8.getDefaultCell().setBorder(Rectangle.BOX);
		            att1_Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table8.addCell(new Phrase(new Chunk("Billing Period : ",black_bold)));

		            att1_Table8.getDefaultCell().setBorder(Rectangle.BOX);
		            att1_Table8.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		            att1_Table8.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		            att1_Table8.addCell(new Phrase(new Chunk(""+period_start_dt+" to "+period_end_dt,black_bold)));


		            if(!invoice_raised_in.equals(price_cd))
		            {
		            	sr=1;
		            	//float[] att1_ContactAddrWidths9 = {0.1F, 0.05F, 0.02F, 0.15F};
		            	float[] att1_ContactAddrWidths9 = {0.002F, 0.05F, 0.02F, 0.15F};
			   	     	PdfPTable att1_Table9 = new PdfPTable(att1_ContactAddrWidths9);
			   	     	att1_Table9.setWidthPercentage(100);

			   	     	att1_Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            att1_Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table9.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

			            att1_Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            att1_Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table9.addCell(new Phrase(new Chunk("Invoiced Quantity(MMBTU)",small_black_normal)));

			            att1_Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            att1_Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table9.addCell(new Phrase(new Chunk(item_amt,small_black_normal)));

			            att1_Table9.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            att1_Table9.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table9.addCell(new Phrase(new Chunk("",small_black_normal)));

			            sr+=1;
			            float[] att1_ContactAddrWidths10 = {0.002F, 0.05F, 0.02F, 0.15F};
			   	     	PdfPTable att1_Table10 = new PdfPTable(att1_ContactAddrWidths10);
			   	     	att1_Table10.setWidthPercentage(100);

			   	     	att1_Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            att1_Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table10.addCell(new Phrase(new Chunk(""+sr,small_black_normal)));

			            att1_Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            att1_Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table10.addCell(new Phrase(new Chunk("Applicable Quantity(MMBTU)",small_black_normal)));

			            att1_Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            att1_Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table10.addCell(new Phrase(new Chunk(item_diff_amt,small_black_normal)));

			            att1_Table10.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table10.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            att1_Table10.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table10.addCell(new Phrase(new Chunk("",small_black_normal)));


			            float[] att1_ContactAddrWidths11 = {0.002F, 0.05F, 0.02F, 0.15F};
			   	     	PdfPTable att1_Table11 = new PdfPTable(att1_ContactAddrWidths11);
			   	     	att1_Table11.setWidthPercentage(100);

			   	     	att1_Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            att1_Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table11.addCell(new Phrase(new Chunk("",small_black_normal)));

			            att1_Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            att1_Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table11.addCell(new Phrase(new Chunk("Difference in Quantity(MMBTU)",black_bold)));

			            att1_Table11.getDefaultCell().setBorder(Rectangle.BOX);
			            att1_Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			            att1_Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table11.addCell(new Phrase(new Chunk(diff_value,black_bold)));

			            att1_Table11.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			            att1_Table11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            att1_Table11.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			            att1_Table11.addCell(new Phrase(new Chunk("",small_black_normal)));


			            document.add(LogoTable);
			            document.add(att1_Table1);
			            document.add(att1_Table2);
			            document.add(att1_Table3);
			            document.add(att1_Table4);
			            document.add(new Paragraph("              "));
			            document.add(title_note_table);
			            document.add(new Paragraph("              "));
			            document.add(att1_Table5);
			            document.add(att1_Table6);
			            document.add(att1_Table7);
			            if(!inv_flag.equals("ST"))
			            {
			            	document.add(att1_Table8);
			            	document.add(new Paragraph("              "));
			            	document.add(att1_Table9);
			            	document.add(att1_Table10);
			            	document.add(new Paragraph("              "));
			            	document.add(att1_Table11);
			            }
		            }

				}
	            document.close();

	            File isPDFexist = new File(path);
				if(isPDFexist.exists())
				{
					String invoice_title="";
					int update_flag=0;
					int pdf_entry=0;

					int cont=0;
					queryString="UPDATE FMS_SALES_DR_CR_MST SET PDF_INV_DTL=? ";
						if(print_pdf_type.equals("O"))
						{
							queryString+= ",PRINT_BY_ORI=?,PRINT_DT_ORI=SYSDATE ";
						}
						else if(print_pdf_type.equals("T"))
						{
							queryString+= ",PRINT_BY_TRI=?,PRINT_DT_TRI=SYSDATE ";
						}
						else if(print_pdf_type.equals("D"))
						{
							queryString+= ",PRINT_BY_DUP=?,PRINT_DT_DUP=SYSDATE ";
						}
						queryString+= "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND PLANT_SEQ=? AND BU_UNIT=? AND CONTRACT_TYPE=? "
							+ "AND FREQ=? AND PERIOD_START_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "AND PERIOD_END_DT=TO_DATE(?,'DD/MM/YYYY') AND FINANCIAL_YEAR=? "
							+ "AND INVOICE_SEQ=? AND BU_STATE_TIN=? AND CARGO_NO=? AND INV_FLAG=? AND DR_CR_SEQ = ? AND DR_CR_FLAG = ? AND CRITERIA = ? AND DR_CR_FIN_YR = ?";
					stmt=conn.prepareStatement(queryString);
					stmt.setString(++cont, print_pdf_type);
					if(print_pdf_type.equals("O"))
					{
						stmt.setString(++cont, emp_cd);
					}
					else if(print_pdf_type.equals("T"))
					{
						stmt.setString(++cont, emp_cd);
					}
					else if(print_pdf_type.equals("D"))
					{
						stmt.setString(++cont, emp_cd);
					}
					stmt.setString(++cont, comp_cd);
					stmt.setString(++cont, counterparty_cd);
					stmt.setString(++cont, cont_no);
					stmt.setString(++cont, agmt_no);
					stmt.setString(++cont, plant_seq);
					stmt.setString(++cont, bu_plant_seq);
					stmt.setString(++cont, contract_type);
					stmt.setString(++cont, billing_cycle);
					stmt.setString(++cont, temp_period_start_dt);
					stmt.setString(++cont, temp_period_end_dt);
					stmt.setString(++cont, financial_year);
					stmt.setString(++cont, inv_seq);
					stmt.setString(++cont, bu_state_tin);
					stmt.setString(++cont, cargo_no);
					stmt.setString(++cont, inv_flag);
					stmt.setString(++cont, drcr_seq);
					stmt.setString(++cont, drcr_flag);
					stmt.setString(++cont, criteria);
					stmt.setString(++cont, drcr_fin_yr);
					update_flag=stmt.executeUpdate();
					stmt.close();

					int count=0;
			        queryString1="SELECT COUNT(*) "
			        		+ "FROM FMS_DRCR_INV_FILE_DTL "
			        		+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
			        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=?";
			        stmt1=conn.prepareStatement(queryString1);
			        stmt1.setString(1, comp_cd);
			        stmt1.setString(2, bu_state_tin);
			        stmt1.setString(3, drcr_seq);
			        stmt1.setString(4, drcr_fin_yr);
			        stmt1.setString(5, print_pdf_type);
			        rset1=stmt1.executeQuery();
			        if(rset1.next())
			        {
			        	count=rset1.getInt(1);
			        }
			        rset1.close();
			        stmt1.close();

			        if(count > 0)
			        {
			        	queryString1="UPDATE FMS_DRCR_INV_FILE_DTL SET FILE_NAME=?,MODIFY_BY=?,MODIFY_DT=SYSDATE "
			        			+ "WHERE COMPANY_CD=? AND BU_STATE_TIN=? "
				        		+ "AND INVOICE_SEQ=? AND FINANCIAL_YEAR=? AND PDF_TYPE=?";
			        	stmt1=conn.prepareStatement(queryString1);
			        	stmt1.setString(1, file_nm);
			        	stmt1.setString(2, emp_cd);
			        	stmt1.setString(3, comp_cd);
			 	        stmt1.setString(4, bu_state_tin);
			 	        stmt1.setString(5, drcr_seq);
			 	        stmt1.setString(6, drcr_fin_yr);
			 	        stmt1.setString(7, print_pdf_type);
			 	        pdf_entry=stmt1.executeUpdate();

			        	stmt1.close();
			        }
			        else
			        {
			        	queryString1="INSERT INTO FMS_DRCR_INV_FILE_DTL(COMPANY_CD,BU_STATE_TIN,INVOICE_SEQ,FINANCIAL_YEAR,PDF_TYPE,"
			        			+ "FILE_NAME,ENT_BY,ENT_DT) "
			        			+ "VALUES(?,?,?,?,?,"
			        			+ "?,?,SYSDATE)";
			        	stmt1=conn.prepareStatement(queryString1);
			        	stmt1.setString(1, comp_cd);
			 	        stmt1.setString(2, bu_state_tin);
			 	        stmt1.setString(3, drcr_seq);
			 	        stmt1.setString(4, drcr_fin_yr);
			 	        stmt1.setString(5, print_pdf_type);
			 	        stmt1.setString(6, file_nm);
			        	stmt1.setString(7, emp_cd);
			        	pdf_entry=stmt1.executeUpdate();

			        	stmt1.close();
			        }

			        if(pdf_entry == 0 || update_flag == 0)
			        {
			        	conn.rollback();
			        	deletePdfFile(path);
			        	msg="Failed! - "+invdtl+" Invoice for "+counterparty_abbr+" PDF for "+dealMap+" Generation Failed!";
			        	msg_type="E";
			        }
			        else
			        {
			        	conn.commit();
			        	msg = "Successful! - "+invdtl+" Invoice for "+counterparty_abbr+" PDF "+file_nm+" for "+dealMap+" Generated Successfully!";
			        	msg_type="S";
			        }
				}
				else
				{
					msg="Failed! - "+invdtl+" Invoice for "+counterparty_abbr+" PDF for "+dealMap+" Generation Failed!";
					msg_type="E";
				}
			}
		}
		catch(Exception e)
		{
			conn.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);

			document.close();
			deletePdfFile(path);

			msg="Error in Exception! - "+invdtl+" Invoice for "+counterparty_abbr+" PDF Generation Failed!";
			msg_type="E";
		}
		finally
		{
			document.close();
		}

		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, "", "", msg);
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
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

	String file_url = "";
	String file_nm = "";
	public String getFile_url() {return file_url;}
	public String getFile_nm() {return file_nm;}


	String criteria="";
	String drcr_fin_yr="";


	String clearance = "";
	String counterparty_cd = "";
	String gx_counterparty_cd = "";
	String cont_no = "";
	String cont_rev_no = "";
	String agmt_no = "";
	String agmt_rev_no = "";
	String contract_type = "";
	String gas_dt="";
	String from_date = "";
	String to_date = "";
	String report_dt = "";
	String plant_seq = "";
	String bu_plant_seq = "";
	String gx_bu_plant_seq = "";
	String to_contact = "";
	String from_contact = "";
	String remark = "";
	String frq_type = "";
	String bu_state_tin = "";

	String inv_type = "";
	String inv_seq = "";
	String inv_no = "";
	String financial_year = "";
	String mapping_id = "";
	String period_start_dt="";
	String period_end_dt="";

	String is_print="";

	String print_pdf_type="";
	String invoice_type="";
	String trans_bu_seq="";

	String seq_no="";
	String seq_rev_no="";
	String gx="";
	String isReversal="";
	String emp_cd="";

	String cargo_no="";
	String boe_no="";
	String inv_flag="";
	String billing_cycle="";

	String form_id="";
	String form_nm="";
	String mod_cd="";
	String mod_nm="";

	String entity="";
	String countpty_name="";
	String contact_person_cd="";

	String tot_mmscm="";
	String tot_mmbtu="";
	String tot_obl_mcm="";
	String ask_nom_mcm="";
	String ask_obl_mcm="";
	String ask_nom_mmbtu="";
	String chk_oblig="";
	String chk_exp="";
	String tot_exp_mmscm="";
	String ask_exp_mmscm="";

	String truck_cd="";


	public void setCrtieria(String criteria) {this.criteria = criteria;}
	public void setdrcr_fin_yr(String drcr_fin_yr) {this.drcr_fin_yr = drcr_fin_yr;}

	public void setClearance(String clearance) {this.clearance = clearance;}
	public void setCounterparty_cd(String counterparty_cd) {this.counterparty_cd = counterparty_cd;}
	public void setGx_counterparty_cd(String gx_counterparty_cd) {this.gx_counterparty_cd = gx_counterparty_cd;}
	public void setCont_no(String cont_no) {this.cont_no = cont_no;}
	public void setCont_rev_no(String cont_rev_no) {this.cont_rev_no = cont_rev_no;}
	public void setAgmt_no(String agmt_no) {this.agmt_no = agmt_no;}
	public void setAgmt_rev_no(String agmt_rev_no) {this.agmt_rev_no = agmt_rev_no;}
	public void setContract_type(String contract_type) {this.contract_type = contract_type;}
	public void setGas_dt(String gas_dt) {this.gas_dt = gas_dt;}
	public void setFrom_date(String from_date) {this.from_date = from_date;}
	public void setTo_date(String to_date) {this.to_date = to_date;}
	public void setReport_dt(String report_dt) {this.report_dt = report_dt;}
	public void setPlant_seq(String plant_seq) {this.plant_seq = plant_seq;}
	public void setBu_plant_seq(String bu_plant_seq) {this.bu_plant_seq = bu_plant_seq;}
	public void setGx_bu_plant_seq(String gx_bu_plant_seq) {this.gx_bu_plant_seq = gx_bu_plant_seq;}
	public void setTo_contact(String to_contact) {this.to_contact = to_contact;}
	public void setFrom_contact(String from_contact) {this.from_contact = from_contact;}
	public void setRemark(String remark) {this.remark = remark;}
	public void setFrq_type(String frq_type) {this.frq_type = frq_type;}

	public void setInv_type(String inv_type) {this.inv_type = inv_type;}
	public void setInv_seq(String inv_seq) {this.inv_seq = inv_seq;}
	public void setInv_no(String inv_no) {this.inv_no = inv_no;}
	public void setFinancial_year(String financial_year) {this.financial_year = financial_year;}
	public void setMapping_id(String mapping_id) {this.mapping_id = mapping_id;}
	public void setPeriod_start_dt(String period_start_dt) {this.period_start_dt = period_start_dt;}
	public void setPeriod_end_dt(String period_end_dt) {this.period_end_dt = period_end_dt;}

	public void setIs_print(String is_print) {this.is_print = is_print;}

	public void setPrint_pdf_type(String print_pdf_type) {this.print_pdf_type = print_pdf_type;}
	public void setBu_state_tin(String bu_state_tin) {this.bu_state_tin = bu_state_tin;}

	public void setInvoice_type(String invoice_type) {this.invoice_type = invoice_type;}
	public void setTrans_bu_seq(String trans_bu_seq) {this.trans_bu_seq = trans_bu_seq;}

	public void setSeq_no(String seq_no) {this.seq_no = seq_no;}
	public void setSeq_rev_no(String seq_rev_no) {this.seq_rev_no = seq_rev_no;}
	public void setGx(String gx) {this.gx = gx;}
	public void setIsReversal(String isReversal) {this.isReversal = isReversal;}
	public void setEmp_cd(String emp_cd) {this.emp_cd = emp_cd;}

	public void setCargo_no(String cargo_no) {this.cargo_no = cargo_no;}
	public void setBoe_no(String boe_no) {this.boe_no = boe_no;}
	public void setInv_flag(String inv_flag) {this.inv_flag = inv_flag;}
	public void setBilling_cycle(String billing_cycle) {this.billing_cycle = billing_cycle;}

	public void setForm_id(String form_id) {this.form_id = form_id;}
	public void setForm_nm(String form_nm) {this.form_nm = form_nm;}
	public void setMod_cd(String mod_cd) {this.mod_cd = mod_cd;}
	public void setMod_nm(String mod_nm) {this.mod_nm = mod_nm;}

	public void setEntity(String entity) {this.entity = entity;}
	public void setContact_person_cd(String contact_person_cd) {this.contact_person_cd = contact_person_cd;}

	public void setTot_mmscm(String tot_mmscm) {this.tot_mmscm = tot_mmscm;}
	public void setTot_mmbtu(String tot_mmbtu) {this.tot_mmbtu = tot_mmbtu;}
	public void setTot_obl_mcm(String tot_obl_mcm) {this.tot_obl_mcm = tot_obl_mcm;}
	public void setAsk_nom_mcm(String ask_nom_mcm) {this.ask_nom_mcm = ask_nom_mcm;}
	public void setAsk_obl_mcm(String ask_obl_mcm) {this.ask_obl_mcm = ask_obl_mcm;}
	public void setAsk_nom_mmbtu(String ask_nom_mmbtu) {this.ask_nom_mmbtu = ask_nom_mmbtu;}
	public void setChk_oblig(String chk_oblig) {this.chk_oblig = chk_oblig;}
	public void setChk_exp(String chk_exp) {this.chk_exp = chk_exp;}
	public void setTot_exp_mmscm(String tot_exp_mmscm) {this.tot_exp_mmscm = tot_exp_mmscm;}
	public void setAsk_exp_mmscm(String ask_exp_mmscm) {this.ask_exp_mmscm = ask_exp_mmscm;}

	public void setTruck_cd(String truck_cd) {this.truck_cd = truck_cd;}


	HashMap<String, String> invoice_data = new HashMap<String, String>();


	public HashMap<String, String> getInvoice_data() {return invoice_data;}

	public void setInvoice_data(HashMap<String, String> invoice_data) {this.invoice_data = invoice_data;}

	String msg="";
	String msg_type="";
	String all_pdf_print="";

	public String getMsg() {return msg;}
	public String getMsg_type() {return msg_type;}
	public String getCountpty_name() {return countpty_name;}
	public String getAll_pdf_print() {return all_pdf_print;}



	String drcr_flag="";
	String drcr_seq="";
	String drcr_criteria="";
	String drcr_no="";
	String ref_type="";

	public void setDrcr_flag(String drcr_flag) {this.drcr_flag = drcr_flag;}
	public void setDrcr_seq(String drcr_seq) {this.drcr_seq = drcr_seq;}
	public void setDrcr_criteria(String drcr_criteria) {this.drcr_criteria = drcr_criteria;}
	public void setDrcr_no(String drcr_no) {this.drcr_no = drcr_no;}
	public void setRef_type(String ref_type) {this.ref_type = ref_type;}


}
