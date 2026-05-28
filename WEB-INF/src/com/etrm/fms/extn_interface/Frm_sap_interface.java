package com.etrm.fms.extn_interface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.ExcelVersionDtl;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.escapeSingleQuotes;

@WebServlet("/servlet/Frm_sap_interface")
@MultipartConfig(fileSizeThreshold=1024*1024*10, 	// 10 MB 
maxFileSize=1024*1024*50,      	// 50 MB
maxRequestSize=1024*1024*100)   	// 100 MB
public class Frm_sap_interface extends HttpServlet
{
	static String db_src_file_name="Frm_sap_interface.java";
	public static  Connection dbcon;
	
	public static String servletName = "Frm_sap_interface";
	public static String option = "";
	public static String url = "";
	public static String msg = "";
	public static String msg_type = "";
	public static String err_msg = "";
	
	private static String queryString = null;
	private static String query = null;
	private static String query1 = null;
	private static String query2 = null;
	private static String query3 = null;
	private static String query4 = null;
	private static PreparedStatement stmt = null;
	private static PreparedStatement stmt1 = null;
	private static PreparedStatement stmt2 = null;
	private static PreparedStatement stmt3 = null;
	private static PreparedStatement stmt4 = null;
	
	private static ResultSet rset = null;
	private static ResultSet rset1 = null;
	private static ResultSet rset2 = null;
	private static ResultSet rset3 = null;
	private static ResultSet rset4 = null;
	
	public static String form_id = "0";
	public static String form_nm = "";
	public static String mod_cd = "0";
	public static String mod_nm = "";
	public static String u = "";
	
	public static String old_value="";
	public static String new_value="";
	
	public static String emp_cd="";
	public static String comp_cd="";
	public static String comp_abbr="";
	public static String emp_nm="";
	public static String ip="";
	
	public static String commonUrl_pra="";
	
	private static final String SAVE_DIR = CommonVariable.dump_sap_xls;
	public static boolean isOLE2Stream(String filePath) throws IOException {return ExcelVersionDtl.isMasterNumberMatch(filePath, ExcelVersionDtl.OLE2_MASTER_NUMBER);}
	public static boolean isOOXMLStream(String filePath) throws IOException {return ExcelVersionDtl.isMasterNumberMatch(filePath,ExcelVersionDtl. OOXML_MASTER_NUMBER);}
	
	public static escapeSingleQuotes escObj = new escapeSingleQuotes();
	
	static UtilBean utilBean = new UtilBean();
	static DateUtil utilDate = new DateUtil();
	
	static NumberFormat nf = new DecimalFormat("###########0.00");
	static NumberFormat nf2 = new DecimalFormat("###########0.0000");
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException
	{
		synchronized(this)
		{
			String function_nm="doPost()";
			HttpSession session = request.getSession();
			if(session.getAttribute("emp_uid")==null || session.getAttribute("emp_uid")=="")
			{
				url="../sess/Expire.jsp";
			}
			else
			{
				try
				{
					Context Context = new InitialContext();
					Context envContext  = (Context)Context.lookup("java:/comp/env");
					DataSource ds = (DataSource)envContext.lookup(RuntimeConf.security_database);
					
					if(ds != null)
					{
						dbcon = ds.getConnection();				
					}
					else
					{
						System.out.println("Data Source Not Found");
					}
					if(dbcon != null)
					{
						dbcon.setAutoCommit(false);
						
						form_id=request.getParameter("form_cd")==null?"0":request.getParameter("form_cd");
						form_nm=request.getParameter("form_nm")==null?"":request.getParameter("form_nm");
						mod_cd=request.getParameter("mod_cd")==null?"0":request.getParameter("mod_cd");
						mod_nm=request.getParameter("mod_nm")==null?"":request.getParameter("mod_nm");
						
						emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
						comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
						comp_abbr = (String)session.getAttribute("comp_abbr")==null?"":(String)session.getAttribute("comp_abbr");
						emp_nm = (String)session.getAttribute("emp_nm")==null?"":(String)session.getAttribute("emp_nm");
						ip = (String)session.getAttribute("ip")==null?"":(String)session.getAttribute("ip");
						u=request.getParameter("u")==null?"":request.getParameter("u");
						
						new_value="";
						old_value="";
						
						option=request.getParameter("option")==null?"":request.getParameter("option");
						
						commonUrl_pra = "&u="+u;
						
						if(option.equalsIgnoreCase("SAP_BUSINESS_AREA"))
						{
							InsertUpdateBusinessAreaCodeDetail(request);
						}
						else if(option.equalsIgnoreCase("UPLOAD_SAP_XLS"))		//PB 20250806: for uploading the sap XLS file 
						{
							UploadSAPXls(request);
						}
					}
					dbcon.close();
					dbcon=null;
				}
				catch(Exception e)
				{
					new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
					url=CommonVariable.errorpage_url+"?e="+e;
				}
				finally
				{
					if(rset != null){try {rset.close();}catch(SQLException e){System.out.println("rset is not close " + e);}}
					if(rset1 != null){try {rset1.close();}catch(SQLException e){System.out.println("rset1 is not close " + e);}}
					if(rset2 != null){try {rset2.close();}catch(SQLException e){System.out.println("rset2 is not close " + e);}}
					if(rset3 != null){try {rset3.close();}catch(SQLException e){System.out.println("rset3 is not close " + e);}}
					if(rset4 != null){try {rset4.close();}catch(SQLException e){System.out.println("rset4 is not close " + e);}}
					if(stmt != null){try {stmt.close();}catch(SQLException e){System.out.println("stmt is not close " + e);}}
					if(stmt1 != null){try {stmt1.close();}catch(SQLException e){System.out.println("stmt1 is not close " + e);}}
					if(stmt2 != null){try {stmt2.close();}catch(SQLException e){System.out.println("stmt2 is not close " + e);}}
					if(stmt3 != null){try {stmt3.close();}catch(SQLException e){System.out.println("stmt3 is not close " + e);}}
					if(stmt4 != null){try {stmt4.close();}catch(SQLException e){System.out.println("stmt4 is not close " + e);}}
					if(dbcon != null){try {dbcon.close();}catch(SQLException e){System.out.println("conn is not close " + e);}}
				}
			}
			
			try {
			response.sendRedirect(url);
			}catch(IOException e) {
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			}
		}
	}	
	
	private void InsertUpdateBusinessAreaCodeDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateBusinessAreaCodeDetail()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String contract_type_nm=utilBean.getContractTypeName(contract_type);
			String entity_role = request.getParameter("entity_role")==null?"":request.getParameter("entity_role");
			String business_area = request.getParameter("business_area")==null?"":request.getParameter("business_area");
			String desc = request.getParameter("desc")==null?"":request.getParameter("desc");
			String eff_dt = request.getParameter("eff_dt")==null?"":request.getParameter("eff_dt");
			
			desc=escObj.replaceSingleQuotes(desc);
			
			if(opration.equals("MODIFY"))
			{
				int count=0;
				query="SELECT COUNT(*) "
						+ "FROM FMS_SAP_BA_CODE_DTL "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND ENTITY=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') ";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, contract_type);
				stmt.setString(3, entity_role);
				stmt.setString(4, eff_dt);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					count=rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count==0)
				{
					query1="INSERT INTO FMS_SAP_BA_CODE_DTL(COMPANY_CD,ENTITY,CONTRACT_TYPE,EFF_DT,BA_CODE,BA_DECR,ENT_DT,ENT_BY) "
							+ "VALUES(?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,SYSDATE,?) ";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, entity_role);
					stmt1.setString(3, contract_type);
					stmt1.setString(4, eff_dt);
					stmt1.setString(5, business_area);
					stmt1.setString(6, desc);
					stmt1.setString(7, emp_cd);
					stmt1.executeUpdate();
					stmt1.close();
					
					msg = "Successful! - Business Area Code for "+contract_type_nm+" Modified Successfully!";
					msg_type="S";
				}
				else
				{
					query1="UPDATE FMS_SAP_BA_CODE_DTL SET BA_CODE=?,BA_DECR=?,MODIFY_DT=SYSDATE,MODIFY_BY=? "
							+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND ENTITY=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') ";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, business_area);
					stmt1.setString(2, desc);
					stmt1.setString(3, emp_cd);
					stmt1.setString(4, comp_cd);
					stmt1.setString(5, contract_type);
					stmt1.setString(6, entity_role);
					stmt1.setString(7, eff_dt);
					stmt1.executeUpdate();
					stmt1.close();
					
					msg = "Successful! - Business Area SAP Code for "+contract_type_nm+" Modified Successfully!";
					msg_type="S";
				}
			}
			else
			{
				int count=0;
				query="SELECT COUNT(*) "
						+ "FROM FMS_SAP_BA_CODE_DTL "
						+ "WHERE COMPANY_CD=? AND CONTRACT_TYPE=? AND ENTITY=? ";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, contract_type);
				stmt.setString(3, entity_role);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					count=rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count==0)
				{
					query1="INSERT INTO FMS_SAP_BA_CODE_DTL(COMPANY_CD,ENTITY,CONTRACT_TYPE,EFF_DT,BA_CODE,BA_DECR,ENT_DT,ENT_BY) "
							+ "VALUES(?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,SYSDATE,?) ";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, entity_role);
					stmt1.setString(3, contract_type);
					stmt1.setString(4, eff_dt);
					stmt1.setString(5, business_area);
					stmt1.setString(6, desc);
					stmt1.setString(7, emp_cd);
					stmt1.executeUpdate();
					stmt1.close();
					
					msg = "Successful! - Business Area SAP Code for "+contract_type_nm+" Inserted Successfully!";
					msg_type="S";
				}
				else
				{
					msg = "Failed! - Business Area SAP Code for "+contract_type_nm+" Already Exist!";
					msg_type="E";
				}
			}
			
			url = "../extn_interface/frm_sap_business_area.jsp?entity_role="+entity_role+
					"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);		
			msg="Error in Exception! - Insert/Update Business Area SAP Code Detail Failed";
			url=CommonVariable.errorpage_url+"?e="+e;
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
		}
	}
	
	//PB 20250806: for uploading the SAP XLS file 
	private void UploadSAPXls(HttpServletRequest request) throws SQLException 
	{
		String function_nm="UploadSAPXls()";
		
		msg="";
		msg_type="";
		url="";
		String opration =request.getParameter("opration");
		if(opration.equals("UPLOAD"))
		{
			try
			{
				String appPath = request.getServletContext().getRealPath("");
		    	String counterparty_cd=request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
		    	String from_dt=request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
		    	String to_dt=request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
		    	String entity_role=request.getParameter("entity_role")==null?"":request.getParameter("entity_role");
		    	String tds=request.getParameter("tds")==null?"":request.getParameter("tds");
		    	String tcs=request.getParameter("tcs")==null?"":request.getParameter("tcs");
		    	String tax=request.getParameter("tax")==null?"":request.getParameter("tax");
				
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
		        
				String savePath = appPath+File.separator+main_folder+File.separator+SAVE_DIR;
				
		        String subSavePath = savePath+File.separator;
		        
		        File fileSaveDir = new File(savePath);
		        if(!fileSaveDir.exists()) 
		        {
		            fileSaveDir.mkdir();
		        }
		        
		    	File subfile = new File(subSavePath);
		        if(!subfile.exists())
		        {
		        	subfile.mkdirs();		//PB 20250929: ERROR FIX
		        }
		        
		        String date=utilDate.getSysdateWithTime24hr();
		        date=date.replaceAll("/", "");
		        date=date.replaceAll(" ", "_");
		        date=date.replaceAll(":", "");
		        
		        String file_name="";
		        String fileName="";
		        String file_extension="";
		        String final_file="";
		        
		        for (Part part : request.getParts()) 
		        {
		        	fileName = extractFileName(part);
		        	//Refines the fileName in case it is an absolute path
				    fileName = new File(fileName).getName();
				    if(!fileName.equals("") )
				    {
				    	file_name=fileName;
				    	String[] split=fileName.split("\\.");
				    	fileName=split[0];
				    	file_extension=split[1];
				    	final_file=fileName+"_"+date+"."+file_extension;
				    	part.write(subSavePath +File.separator+final_file);
				    } 
		        }
		        
		        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
				
				String file_path =subSavePath+""+final_file;
				//FileInputStream file = new FileInputStream(new File(file_path));
				try(FileInputStream file = new FileInputStream(new File(file_path)))
				{
					String msg_sheet ="";
					if (isOLE2Stream(file_path)||isOOXMLStream(file_path)) 
					{
						//org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(file);
						XSSFWorkbook workbook = new XSSFWorkbook(file);
						
						for(int i=0; i<workbook.getNumberOfSheets();i++)
						{
							String sheet_name = workbook.getSheetName(i);
							XSSFSheet sheet= workbook.getSheetAt(i); 
							//org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(i);
							
							if(!sheet_name.equals(""))
							{
								int row_no = 0;	
								int skip_row=0;
								int diff_comp_data=0;
								
								Iterator<Row>rowIterator = sheet.iterator();
								while(rowIterator.hasNext()) 
								{
									Row row = rowIterator.next();
									row_no = row.getRowNum();
									
									String co_cd="";
									String document_no="";
									String account="";
									String year_month="";
									String pk="";
									String doc_type="";
									String reference="";
									String posting_dt="";
									String doc_dt="";
									String entry_dt="";
									String dc_curr="";
									String dc_amt="";
									String lc_curr="";
									String lc_amt2="";
									String lc_amt="";
									String lc_curr2="";
									String assignment="";
									String itm="";
									if(row_no!=0) 
									{
										Iterator<Cell> cellIterator = row.cellIterator();
										while(cellIterator.hasNext()) 
										{
											Cell cell = cellIterator.next();
											if(cell.getColumnIndex()==0)		//0: CoCd
											{
												co_cd=cell.getStringCellValue().trim();
											}
											if(cell.getColumnIndex()==1)	//1: Account
											{
												if(cell.getCellType()==CellType.NUMERIC && cell.getDateCellValue()!=null) 
												{
													BigDecimal acc = BigDecimal.valueOf(cell.getNumericCellValue()).setScale(0, RoundingMode.DOWN);
													account=acc.toPlainString().trim();
												}
												else
												{
													account=cell.getStringCellValue().trim();
												}
											}
											if(cell.getColumnIndex()==2)		//2: Document no
											{
												if(cell.getCellType()==CellType.NUMERIC && cell.getDateCellValue()!=null) 
												{
													//java.math.BigDecimal doc_no = new java.math.BigDecimal(cell.getNumericCellValue());
													BigDecimal doc_no = BigDecimal.valueOf(cell.getNumericCellValue()).setScale(0, RoundingMode.DOWN);
													document_no=doc_no.toPlainString().trim();
												}
												else
												{
													document_no=""+cell.getStringCellValue().trim();
												}
											}
											if(cell.getColumnIndex()==3)		//3: Year/month 
											{
												year_month=cell.getStringCellValue().trim();
											}
											if(cell.getColumnIndex()==4)		//4: PK
											{
												if(cell.getCellType()==CellType.NUMERIC && cell.getDateCellValue()!=null) 
												{
													//java.math.BigDecimal doc_no = new java.math.BigDecimal(cell.getNumericCellValue());
													BigDecimal pk1 = BigDecimal.valueOf(cell.getNumericCellValue()).setScale(0, RoundingMode.DOWN);
													pk=pk1.toPlainString().trim();
												}
												else
												{
													pk=""+cell.getStringCellValue().trim();
												}
											}
											if(cell.getColumnIndex()==5)		//5: Type
											{
												doc_type=""+cell.getStringCellValue().trim();
											}
											if(cell.getColumnIndex()==6)		//6: G/L
											{
												//same as account
											}
											if(cell.getColumnIndex()==7)		//7: Reference
											{
												reference=cell.getStringCellValue().trim();
											}
											if(cell.getColumnIndex()==8)		//8: Assignment
											{
												assignment=cell.getStringCellValue().trim();
											}
											if(cell.getColumnIndex()==9)		//9: ITM
											{
												if(cell.getCellType()==CellType.NUMERIC && cell.getDateCellValue()!=null) 
												{
													BigDecimal itm_no = BigDecimal.valueOf(cell.getNumericCellValue()).setScale(0, RoundingMode.DOWN);
													itm=itm_no.toPlainString().trim();
												}
												else
												{
													itm=cell.getStringCellValue().trim();
												}
											}
											if(cell.getColumnIndex()==10)		//10: Posting date
											{
												if(cell.getCellType()==CellType.NUMERIC && cell.getDateCellValue()!=null) 
												{												
													posting_dt =formatter.format(cell.getDateCellValue()).trim();
												}
												else
												{
													posting_dt=cell.getStringCellValue().trim();
												}
											}
											if(cell.getColumnIndex()==11)		//11: Doc Date
											{
												if(cell.getCellType()==CellType.NUMERIC && cell.getDateCellValue()!=null) 
												{												
													doc_dt =formatter.format(cell.getDateCellValue()).trim();
												}
												else
												{
													doc_dt=cell.getStringCellValue().trim();
												}
											}
											if(cell.getColumnIndex()==12)		//12: Entry date
											{
												if(cell.getCellType()==CellType.NUMERIC && cell.getDateCellValue()!=null) 
												{												
													entry_dt =formatter.format(cell.getDateCellValue()).trim();
												}
												else
												{
													entry_dt=cell.getStringCellValue().trim();
												}
											}
											if(cell.getColumnIndex()==13)		//13: Curr
											{
												dc_curr=cell.getStringCellValue().trim();
												dc_curr=getRateCd(dc_curr).trim();
											}
											if(cell.getColumnIndex()==14)		//14: amount in document currency
											{
												//java.math.BigDecimal acc = new java.math.BigDecimal(cell.getNumericCellValue());
												BigDecimal acc = BigDecimal.valueOf(cell.getNumericCellValue());
												dc_amt=nf.format(acc).trim();
												//dc_amt=acc.toPlainString();
											}
											if(cell.getColumnIndex()==15)		//15: LCurr
											{
												lc_curr=cell.getStringCellValue();
												lc_curr=getRateCd(lc_curr).trim();
											}
											if(cell.getColumnIndex()==16)		//16: amount in loc curr 
											{
												//java.math.BigDecimal acc = new java.math.BigDecimal(cell.getNumericCellValue());
												BigDecimal acc = BigDecimal.valueOf(cell.getNumericCellValue());
												lc_amt=nf.format(acc).trim();
												//lc_amt2=acc.toPlainString();
											}
											if(cell.getColumnIndex()==17)		//17: amount in loc curr2
											{
												//java.math.BigDecimal acc = new java.math.BigDecimal(cell.getNumericCellValue());
												BigDecimal acc = BigDecimal.valueOf(cell.getNumericCellValue());
												lc_amt2=nf.format(acc).trim();
												//lc_amt=acc.toPlainString();
											}
											if(cell.getColumnIndex()==18)		//18: LCurr2
											{
												lc_curr2=cell.getStringCellValue().trim();
												lc_curr2=getRateCd(lc_curr2).trim();
											}
										}
										
										if(!co_cd.equals("") && !account.equals("") && !document_no.equals("") && !posting_dt.equals(""))
										{
											if(co_cd.equals(utilBean.getCompanySAPcode(dbcon, comp_cd)))	
											{
												posting_dt = posting_dt.split("-")[1]+"/"+posting_dt.split("-")[0]+"/"+posting_dt.split("-")[2]; 	//Changing the format of the posting date from mm-dd-yyyy to dd/mm/yyyy
												doc_dt = doc_dt.split("-")[1]+"/"+doc_dt.split("-")[0]+doc_dt.split("-")[2];						//Changing the format of document date from mm-dd-yyyy to dd/mm/yyyy
												entry_dt = entry_dt.split("-")[1]+"/"+entry_dt.split("-")[0]+"/"+entry_dt.split("-")[2];			//Changing the format of entry date from mm-dd-yyyy to dd/mm/yyyy
												
												int count=0;
												query="SELECT COUNT(*) FROM FMS_SAP_PIVOT_DTL "
														+ "WHERE COMPANY_CD=? AND DOC_NO=? AND GL_CODE=? "
														+ "AND POST_DT = TO_DATE(?,'DD/MM/YYYY') "
														+ "AND ITM=? ";		//PB 20251008: FOR ADDING UNIQUENESS
												stmt=dbcon.prepareStatement(query);
												stmt.setString(1, comp_cd);
												stmt.setString(2, document_no);
												stmt.setString(3, account);
												stmt.setString(4, posting_dt);
												stmt.setString(5, itm);
												rset=stmt.executeQuery();
												if(rset.next())
												{
													count=rset.getInt(1);
												}
												rset.close();
												stmt.close();
												
												if(count>0)		//updating the data in the table
												{
													query1="UPDATE FMS_SAP_PIVOT_DTL "
															+ "SET COCD=?, POSTING_KEY=?, DOC_TYPE=?, REFERENCE_NO=?,  "
															+ "DOCUMENT_DT=TO_DATE(?,'DD/MM/YYYY'), ENTRY_DT=TO_DATE(?,'DD/MM/YYYY'), DC_CURR=?, "
															+ "DC_AMT=?, LC_CURR=?,LC_AMT=?, LC_CURR2=?, LC_AMT2=?, MODIFY_BY=?,MODIFY_DT=SYSDATE,ASSIGNMENT=? "
															+ "WHERE COMPANY_CD=? AND DOC_NO=? AND GL_CODE=? AND POST_DT=TO_DATE(?,'DD/MM/YYYY') "
															+ "AND ITM=? ";		//PB 20251008: FOR ADDING UNIQUENESS
													stmt=dbcon.prepareStatement(query1);
													stmt.setString(1, co_cd);
													stmt.setString(2, pk);
													stmt.setString(3, doc_type);
													stmt.setString(4, reference);
													stmt.setString(5, doc_dt);
													stmt.setString(6, entry_dt);
													stmt.setString(7, dc_curr);
													stmt.setString(8, dc_amt);
													stmt.setString(9, lc_curr);
													stmt.setString(10, lc_amt);
													stmt.setString(11, lc_curr2);
													stmt.setString(12, lc_amt2);
													stmt.setString(13, emp_cd);
													stmt.setString(14, assignment);
													stmt.setString(15, comp_cd);
													stmt.setString(16, document_no);
													stmt.setString(17, account);
													stmt.setString(18, posting_dt);
													stmt.setString(19, itm);
													stmt.executeUpdate();
													
													stmt.close();
													msg = "Successful! data Updated in system from worksheet "+sheet_name+" of File "+file_name+" ";
													msg_type="S";
												}
												else			//inserting the data in the table 
												{
													query1="INSERT INTO FMS_SAP_PIVOT_DTL(COMPANY_CD,COCD,DOC_NO,GL_CODE,POST_DT,POSTING_KEY,DOC_TYPE,REFERENCE_NO,DOCUMENT_DT, "
															+ "ENTRY_DT,DC_CURR,DC_AMT,LC_CURR,LC_AMT,LC_CURR2,LC_AMT2,ENT_BY,ENT_DT,ASSIGNMENT,ITM ) "
															+ "VALUES(?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,TO_DATE(?,'DD/MM/YYYY'), "
															+ "TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,?,SYSDATE,?,?) ";
													stmt=dbcon.prepareStatement(query1);
													stmt.setString(1, comp_cd);
													stmt.setString(2, co_cd);
													stmt.setString(3, document_no);
													stmt.setString(4, account);
													stmt.setString(5, posting_dt);
													stmt.setString(6, pk);
													stmt.setString(7, doc_type);
													stmt.setString(8, reference);
													stmt.setString(9, doc_dt);
													stmt.setString(10, entry_dt);
													stmt.setString(11, dc_curr);
													stmt.setString(12, dc_amt);
													stmt.setString(13, lc_curr);
													stmt.setString(14, lc_amt);
													stmt.setString(15, lc_curr2);
													stmt.setString(16, lc_amt2);
													stmt.setString(17, emp_cd);
													stmt.setString(18, assignment);
													stmt.setString(19, itm);
													stmt.executeUpdate();
													
													stmt.close();
													
													msg = "Successful! data imported in system from worksheet "+sheet_name+" of File "+file_name+" ";
													msg_type="S";
												}
											}
											else
											{
												diff_comp_data+=1;
											}
										}
										else
										{
											skip_row+=1;
										}
										
									}
									dbcon.commit();
								}
								int success_rows = row_no - skip_row;
								int same_comp_data = success_rows-diff_comp_data;
								//msg_sheet += success_rows+" out of "+row_no+" data are Inserted/updated from worksheet "+sheet_name+" of File "+file_name+". ";
								msg_sheet+="Out of "+row_no+" records, "+success_rows+" were processed. Of these, "+same_comp_data+" belongs to "+utilBean.getCompanyAbbr(dbcon, comp_cd);
								
								if (success_rows > 0 || row_no==0)
								{	
									msg_type="S";
								}
								else
								{	
									msg_type="E";
								}
								msg=msg_sheet;
								url = "../extn_interface/rpt_sap_recon_report.jsp?msg="+msg+"&msg_type="+msg_type+"&entity_role="+entity_role+"&counterparty_cd="+counterparty_cd+"&from_dt="+from_dt+"&to_dt="+to_dt
										+"&tds="+tds+"&tcs="+tcs+"&tax="+tax+commonUrl_pra;
							}
						}
					}
					else 
					{
						msg = "Failed! - The version of the XLS file you're trying to upload is not supported.";
						msg_type="E";
						url = "../extn_interface/rpt_sap_recon_report.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
					}
					
				}
			}
			catch(Exception e)
			{
				dbcon.rollback();
				new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);		
				msg="Error in Exception! - SAP XLS file upload Failed";
				url=CommonVariable.errorpage_url+"?e="+e;
			}
		}
	}
	
	private String getRateCd(String rate_nm)
	{
		String function_nm="getRateCd";
		String rate_cd="";
		try
		{
			if(rate_nm.trim().equalsIgnoreCase("INR"))
			{
				rate_cd="1";
			}
			else if(rate_nm.trim().equalsIgnoreCase("USD"))
			{
				rate_cd="2";
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);		
			msg="Error in Exception! - SAP XLS file upload Failed";
			url=CommonVariable.errorpage_url+"?e="+e;
		}
		return rate_cd;
	}
	
	private String extractFileName(Part part) 
    {
		String function_nm="extractFileName()";
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        String filenm = "";
        for (String s : items) 
        {
            if (s.trim().startsWith("filename") || s.trim().startsWith("meet_file")) 
            {
                filenm = s.substring(s.indexOf("=") + 2, s.length()-1);
            }       
        }
        return filenm;
    }
}
