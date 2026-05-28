package com.etrm.fms.market_risk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.ExcelVersionDtl;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.XmlUtilBean;
import com.etrm.fms.util.escapeSingleQuotes;

//Coded By          : Harsh Maheta
//Code Reviewed by	:  
//CR Date			: 13/06/2023 
//Status	  		: Developing

@WebServlet("/servlet/Frm_MarketRisk")
@MultipartConfig(fileSizeThreshold=1024*1024*10, 	// 10 MB 
maxFileSize=1024*1024*50,      	// 50 MB
maxRequestSize=1024*1024*100)   	// 100 MB
public class Frm_MarketRisk extends HttpServlet
{
	static String frm_src_file_name="Frm_MarketRisk.java";
	public static  Connection dbcon;
	
	public static String servletName = "Frm_market_risk";
	public static String option = "";
	public static String url = "";
	public static String msg = "";
	public static String date = "";
	public static String msg_type = "";
	public static String err_msg = "";
	
	private static String queryString = null;
	private static String queryString1 = null;
	private static String queryString2 = null;
	private static String query = null;
	private static String query0 = null;
	private static String query1 = null;
	private static String query2 = null;
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
	
	public static String comp_cd="";
	public static String comp_abbr="";
	
	public static String old_value="";
	public static String new_value="";
	
	public static String commonUrl_pra="";
	
	public static escapeSingleQuotes escObj = new escapeSingleQuotes();
	
	static UtilBean utilBean = new UtilBean();
	static DateUtil utilDate = new DateUtil();
	static ExcelVersionDtl excelversiondtl = new ExcelVersionDtl();
	static XmlUtilBean xmlUtil = new XmlUtilBean();
	
	private static final String SAVE_DIR = CommonVariable.market_risk_dir;
	
	public static boolean isOLE2Stream(String filePath) throws IOException {return ExcelVersionDtl.isMasterNumberMatch(filePath, ExcelVersionDtl.OLE2_MASTER_NUMBER);}
	public static boolean isOOXMLStream(String filePath) throws IOException {return ExcelVersionDtl.isMasterNumberMatch(filePath,ExcelVersionDtl. OOXML_MASTER_NUMBER);}
	
	public static org.apache.poi.ss.usermodel.Workbook workbook = new XSSFWorkbook();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException
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
					u=request.getParameter("u")==null?"":request.getParameter("u");
					
					comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
					comp_abbr = (String)session.getAttribute("comp_abbr")==null?"":(String)session.getAttribute("comp_abbr");
					
					new_value="";
					old_value="";
					
					option=request.getParameter("option")==null?"":request.getParameter("option");
					
					commonUrl_pra = "&u="+u;
					
					if(option.equalsIgnoreCase("SETTLEMENT_DTL"))
					{
						InsertUpdateSettlementCalendar(request);
					}
					else if (option.equalsIgnoreCase("HOLIDAY_CLAND_DTL")) 
					{
						InsertUpdateHolidayCalendar(request);
					}
					else if (option.equalsIgnoreCase("SETTLEMENT_FORWARD_PRICING_DTL")) 
					{
						InsertUpdateSettlementForwardPricing(request);
					}
				}
				
				dbcon.close();
				dbcon=null;
			}
			catch(Exception e)
			{
				new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
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
				if(workbook != null){try {workbook.close();}catch(IOException  e){System.out.println("XSSFWorkbook is not close " + e);}}
			}
		}
		
		try 
		{
			response.sendRedirect(url);
		}
		catch(IOException e)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
		}
	}
	
	private void InsertUpdateSettlementForwardPricing(HttpServletRequest request)throws IOException,SQLException, ServletException 
	{
		String function_nm="InsertUpdateSettlementForwardPricing()";
		String opration =request.getParameter("opration");
		String sel_file_name =request.getParameter("radio_btn");
		String report_dt = request.getParameter("report_dt");
		String upload_report_dt = request.getParameter("upload_report_dt")==null?"":request.getParameter("upload_report_dt");
		String delete_report_Dt = request.getParameter("delete_report_Dt")==null?"":request.getParameter("delete_report_Dt");

		HttpSession session = request.getSession();
		String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
		String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
			
		try
		{	
			String appPath = request.getServletContext().getRealPath("");
			
	    	String main_folder=CommonVariable.work_dir;
			File main_folderDir = new File(appPath+File.separator+main_folder);
	        if(!main_folderDir.exists())
	        {
	        	main_folderDir.mkdir();
	        }
	        
	        String savePath = appPath+File.separator+main_folder+File.separator+SAVE_DIR;
			File fileSaveDir = new File(savePath);
	        if(!fileSaveDir.exists()) 
	        {
	            fileSaveDir.mkdir();
	        }
			
	        String innerSubSavePath = savePath+File.separator+"Pricing_XML";
	        File subfile = new File(innerSubSavePath);
	        if(!subfile.exists())
	        {
	        	subfile.mkdir();
	        }
	        
	        //For File uploaded file naming
	        LocalDateTime dateObj = LocalDateTime.now();
	        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HHmmss");
	        String time = dateObj.format(formatter1);
	        
	        String file_name="";
	        String fileName="";
	        
	        if(opration.equals("IMPORT")) 
	        {
        		for (Part part : request.getParts()) 
		        {
		        	fileName = extractFileName(part);
		        	//Refines the fileName in case it is an absolute path
				    fileName = new File(fileName).getName();
				    if(!fileName.equals(""))
				    {
				    	file_name=fileName;
				    	String filePath = innerSubSavePath + File.separator + fileName;
				    	
				    	File file = new File(filePath);
				    	
				    	if (file.exists()) 
				    	{
				    		msg = file_name+" already exists!";
							msg_type="E";
							url = "../market_risk/frm_settlement_pricing_mst.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
				        } 
				    	else 
				        {
				    		part.write(filePath);
				    		
				    		msg = "ZEMA XML Price file dated "+file_name+" uploaded succesfully";
				    		msg_type = "S";
				    		url = "../market_risk/frm_settlement_pricing_mst.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
				        }
				    } 
		        }
	        }
	        
	        fileName = new File(fileName).getName();
	        
	        if(opration.equals("UPLOAD")) 
	        {
	        	String[] sel_file_nm = sel_file_name.split(",");
	    		String file_nm = sel_file_nm[0];
	    		String file_size =  sel_file_nm[1];
	    		
	        	//By HM - This block of code is used to get Day of report date
	    		String[] split_upload_report_dt = upload_report_dt.split("/");
	    		int d_dt = Integer.parseInt(split_upload_report_dt[0]);
	    		int m_dt = Integer.parseInt(split_upload_report_dt[1]);
	    		int y_dt = Integer.parseInt(split_upload_report_dt[2]);
	    		LocalDate Report_date_local = LocalDate.of(y_dt, m_dt, d_dt);
	    		DayOfWeek report_dt_day =  Report_date_local.getDayOfWeek();
	    		String upload_report_dt_day = report_dt_day.name();
	    		
	        	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				
				String file_path =innerSubSavePath+File.separator+file_nm;
				
				String realPath = request.getRealPath("");
				File files = new File(file_path);
				String canonicalPath_files = files.getCanonicalPath();
				/*if(canonicalPath_files.startsWith("/var/lib/"))
				{
					canonicalPath_files=canonicalPath_files.replaceAll("/var/lib/", "/usr/share/");
				}*/
				if(!canonicalPath_files.startsWith(realPath))
		        {
		        	throw new IOException("Entry is outside of the target directory!");
		        }
		        else if (files.exists()) 
		    	{	
		    		FileInputStream file = new FileInputStream(files);
		    		
					//an instance of factory that gives a document builder
					DocumentBuilderFactory dbf = xmlUtil.dcoumentBuilderFactory();  
					
					//an instance of builder to parse the specified XML file  
					DocumentBuilder db = dbf.newDocumentBuilder();  
					
					// Parse the XML data
					org.w3c.dom.Document doc = db.parse(file);
					
					// Get the root element
		            Element rootElement = doc.getDocumentElement();
					doc.getDocumentElement().normalize();
					
					Vector VCURVE_NM = new Vector();
					Vector VREPORT_DT = new Vector();
					Vector VCOMMODITY_TYPE = new Vector();
					Vector VCURVE_TYPE = new Vector();
					Vector VCURVE_UNIT = new Vector();
					Vector VPHY_FINANCE = new Vector();
					Vector VCURVE_DT = new Vector();
					Vector VCURVE_VALUE = new Vector();
					Vector VACTUAL_CURVE_TYPE = new Vector();//JD20230925
					
					NodeList nList = doc.getElementsByTagName("curve");
					for (int temp = 0; temp < nList.getLength(); temp++) 
					{
						Node nNode = nList.item(temp);
						if (nNode.getNodeType() == Node.ELEMENT_NODE) 
						{
							if(nNode.hasAttributes())
							{
								NamedNodeMap nodeMap = (NamedNodeMap) nNode.getAttributes();
								for (int i = 0; i < ((NamedNodeMap) nodeMap).getLength(); i++) {
									Node node = nodeMap.item(i);
								}
							}
							
							String Curve_Name = "";
							String Report_Date = "";
							String COMMODITY_TYPE = "";
							String CURVE_TYPE = "";
							String PHYSICAL_FINANCIAL = "";
							String UNIT = "";
							Element eElement = (Element) nNode;
						   
							Curve_Name = eElement.getAttribute("name");
							String Report_date = eElement.getAttribute("opr_date");
							
							SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
						    SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
						    Date input_date = (Date) inputFormat.parse(Report_date);
						   
						    Report_Date = outputFormat.format(input_date);
							
							NodeList nodes = eElement.getChildNodes();
							for(int i=0; i<nodes.getLength(); i++)
							{
								Node node = nodes.item(i);
								String childTag = node.getNodeName();
								if(childTag.equalsIgnoreCase("properties"))
								{
									Element ele = (Element) node;
									NodeList nodes1 = ele.getChildNodes();
									int temp_j=0;
									  
									for(int j=0; j<nodes1.getLength(); j++)
									{
										Node node1 = nodes1.item(j);
										String childTag1 = node1.getNodeName();
										if(childTag1.equalsIgnoreCase("property"))
										{
											Element ele1 = (Element) node1;
											if(ele1.getAttribute("name").toString().equals("FMS_COMMODITY_TYPE"))
											{
												COMMODITY_TYPE=ele.getElementsByTagName(childTag1).item(temp_j).getTextContent();
											}
											else if(ele1.getAttribute("name").toString().equals("FMS_CURVE_TYPE"))
											{
												CURVE_TYPE=ele.getElementsByTagName(childTag1).item(temp_j).getTextContent();
											}
											else if(ele1.getAttribute("name").toString().equals("FMS_PHYSICAL_FINANCIAL"))
											{
												PHYSICAL_FINANCIAL=ele.getElementsByTagName(childTag1).item(temp_j).getTextContent();
											}
											else if(ele1.getAttribute("name").toString().equals("FMS_UNIT"))
											{
												UNIT=ele.getElementsByTagName(childTag1).item(temp_j).getTextContent();
											}
											temp_j += 1;
										  }
									  }
								}
								else if(childTag.equalsIgnoreCase("type"))
								{
									Element ele = (Element) node;
									NodeList nodes1 = ele.getChildNodes();
									int temp_j=0,k=0;
									
									for(int j=0; j<nodes1.getLength(); j++)
									{
										Node node1 = nodes1.item(j);
										String childTag1 = node1.getNodeName();
										
										if(childTag1.equalsIgnoreCase("contract") && !CURVE_TYPE.equals("Spot"))
										{
											Element ele1 = (Element) node1;
											k=k+1;
											  
											VCURVE_NM.add(Curve_Name);
											VREPORT_DT.add(Report_Date);
											VCOMMODITY_TYPE.add(COMMODITY_TYPE);
											VCURVE_TYPE.add(CURVE_TYPE);
											VACTUAL_CURVE_TYPE.add("");//JD20230925 NOT REQIRED FOR FWD PRICE
											VCURVE_UNIT.add(UNIT);
											if(PHYSICAL_FINANCIAL.equals(""))
											{
												VPHY_FINANCE.add("Financial");
											}
											else
											{
												VPHY_FINANCE.add(PHYSICAL_FINANCIAL);  
											}
											  
											String CurveDate = "01/"+ele1.getAttribute("code").toString()+"/"+ele1.getAttribute("year").toString();
											VCURVE_DT.add(CurveDate);
											VCURVE_VALUE.add(ele.getElementsByTagName(childTag1).item(temp_j).getTextContent());
											temp_j += 1;
										}
										else if(childTag1.equalsIgnoreCase("value")&& CURVE_TYPE.equals("Spot"))
										{
											Element ele1 = (Element) node1;
											
											VCURVE_NM.add(Curve_Name);
											VACTUAL_CURVE_TYPE.add(getActualCurveName(Curve_Name));//JD20230925
											//System.out.println(getActualCurveName(Curve_Name));
											VREPORT_DT.add(Report_Date);
											VCOMMODITY_TYPE.add(COMMODITY_TYPE);
											VCURVE_TYPE.add(CURVE_TYPE);
											VCURVE_UNIT.add(UNIT);
											
											//FMS_PHYSICAL_FINANCIAL is not Property tag for SPOT Price
											VPHY_FINANCE.add("Financial");
																						  
											String CurveDate = ele1.getAttribute("opr_date").toString();
											VCURVE_DT.add(CurveDate);
											VCURVE_VALUE.add(ele.getElementsByTagName(childTag1).item(temp_j).getTextContent());
											temp_j += 1;
										}
									}
								}
							}
						}
					}
		    	
					int j=0;
					int inserted=0;
					int updated=0;
					int failed=0;
										
					String SYS_DATE = utilDate.getSysdate();
					
					String[] split_sysdt = SYS_DATE.split("/");
					String splited_sysdt = split_sysdt[2]+split_sysdt[1]+split_sysdt[0];
					
					String[] split_upload_dt = upload_report_dt.split("/");
					String splited_upload_dt = split_upload_dt[2]+split_upload_dt[1]+split_upload_dt[0];
					
					if(splited_upload_dt.compareTo(splited_sysdt)<0 || splited_upload_dt.compareTo(splited_sysdt)==0) 
					{
						if(upload_report_dt.equals(""+VREPORT_DT.elementAt(0))) 
						{
							if(!upload_report_dt_day.equalsIgnoreCase("SATURDAY") && !upload_report_dt_day.equalsIgnoreCase("SUNDAY")) 
							{
								for(int i=0 ;i<VREPORT_DT.size(); i++)
								{
									String ReportDate="";
									if(!VREPORT_DT.elementAt(i).equals(""))
									{
										ReportDate = ""+VREPORT_DT.elementAt(i);
									}
									
									if(VCURVE_TYPE.elementAt(i).toString().toUpperCase().equals("FORWARD"))
									{
										j=j+1;
										String CurveValue = "";
										if(!VCURVE_VALUE.elementAt(i).equals(""))
										{
											CurveValue = VCURVE_VALUE.elementAt(i).toString();
										}
										else
										{
											//CurveValue = "0";
											CurveValue = ""; //JAYASRI20230922
										}
										queryString = "SELECT COUNT(*) "
												+ "FROM FMS_FORWARD_PRICE_DTL "
												+ "WHERE CURVE_DT = TO_DATE(?,'DD/MM/YYYY') AND CURVE_NM = ? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY')";
										stmt =  dbcon.prepareStatement(queryString);
										stmt.setString(1, ""+VCURVE_DT.elementAt(i));
										stmt.setString(2, ""+VCURVE_NM.elementAt(i));
										stmt.setString(3, ""+VREPORT_DT.elementAt(i));
										rset=stmt.executeQuery();
										if(rset.next())
										{
											if(rset.getInt(1) > 0) 
											{
												msg = "Pricing reported on "+VREPORT_DT.elementAt(i)+" already exists!";
												msg_type="W";
											}
											else if(!CurveValue.equals("")) //JAYASRI20230922
											{
												queryString1 = "INSERT INTO FMS_FORWARD_PRICE_DTL(REPORT_DT,CURVE_DT,CURVE_NM,COMMODITY_TYPE,CURVE_TYPE,"
														+ "CURVE_UNIT,PHYS_FIN,SETTLE_PRICE,ENT_BY,ENT_DT) "
														+ "VALUES(TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,"
														+ "?,?,?,?,?,SYSDATE)";
												stmt1 = dbcon.prepareStatement(queryString1);
												stmt1.setString(1, ""+VREPORT_DT.elementAt(i));
												stmt1.setString(2, ""+VCURVE_DT.elementAt(i));
												stmt1.setString(3, ""+VCURVE_NM.elementAt(i));
												stmt1.setString(4, ""+VCOMMODITY_TYPE.elementAt(i));
												stmt1.setString(5, ""+VCURVE_TYPE.elementAt(i));
												stmt1.setString(6, ""+VCURVE_UNIT.elementAt(i));
												stmt1.setString(7, ""+VPHY_FINANCE.elementAt(i));
												stmt1.setString(8, CurveValue);
												stmt1.setString(9, emp_cd);
												stmt1.executeUpdate();
												inserted++;
												
												stmt1.close();
											}
										}
										else
										{
											failed++;
										}
										rset.close();
										stmt.close();
									}
									else if(VCURVE_TYPE.elementAt(i).toString().toUpperCase().equals("SPOT"))
									{
										String CurveValue = "";
										if(!VCURVE_VALUE.elementAt(i).equals(""))
										{
											CurveValue = VCURVE_VALUE.elementAt(i).toString();
										}
										else
										{
											//CurveValue = "0";
											CurveValue = ""; //JAYASRI20230922
										}
										
										String CurveDate="";
										if(!VCURVE_DT.elementAt(i).equals(""))
										{
											String splitCurveDt[] = VCURVE_DT.elementAt(i).toString().split("-");
											CurveDate = splitCurveDt[2]+"/"+splitCurveDt[1]+"/"+splitCurveDt[0];
										}

										queryString = "SELECT COUNT(*) FROM FMS_SPOT_PRICE_DTL "
												+ "WHERE CURVE_DT = TO_DATE(?,'DD/MM/YYYY') AND CURVE_NM = ? AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY')";
										stmt =  dbcon.prepareStatement(queryString);
										stmt.setString(1, CurveDate);
										stmt.setString(2, ""+VCURVE_NM.elementAt(i));
										stmt.setString(3, ""+VREPORT_DT.elementAt(i));
										rset=stmt.executeQuery();
										if(rset.next())
										{ 
											if(rset.getInt(1) > 0) 
											{
												msg = "Zema Pricing reported on "+VREPORT_DT.elementAt(i)+" already exists!";
												msg_type="W";
											}
											else if(!CurveValue.equals("")) //JAYASRI20230922 
											{
												queryString1 = "INSERT INTO FMS_SPOT_PRICE_DTL(REPORT_DT,CURVE_DT,CURVE_NM,COMMODITY_TYPE,CURVE_TYPE,"
														+ "CURVE_UNIT,PHYS_FIN,SETTLE_PRICE,ENT_BY,ENT_DT,ACTUAL_CURVE) "
														+ "VALUES(TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,"
														+ "?,?,?,?,?,SYSDATE,?)";
												//JD20230925 ACTUAL_CURVE COLUMN ADDED
												//System.out.println(queryString1);
												stmt1 = dbcon.prepareStatement(queryString1);
												stmt1.setString(1, ""+VREPORT_DT.elementAt(i));
												stmt1.setString(2, CurveDate);
												stmt1.setString(3, ""+VCURVE_NM.elementAt(i));
												stmt1.setString(4, ""+VCOMMODITY_TYPE.elementAt(i));
												stmt1.setString(5, ""+VCURVE_TYPE.elementAt(i));
												stmt1.setString(6, ""+VCURVE_UNIT.elementAt(i));
												stmt1.setString(7, ""+VPHY_FINANCE.elementAt(i));
												stmt1.setString(8, CurveValue);
												stmt1.setString(9, emp_cd);
												stmt1.setString(10, ""+VACTUAL_CURVE_TYPE.elementAt(i));
												stmt1.executeUpdate();
												inserted++;
												
												stmt1.close();
											}
										}
										else
										{
											failed++;
										}
										rset.close();
										stmt.close();
									}
								}
								msg="Zema Pricing inserted (successful "+inserted+" Failed "+failed+")from "+file_nm+"!";
								msg_type="S";
							}
							else 
							{
								msg = "Please select any weekday!!";
								msg_type="W";
							}
						}
						else 
						{
							msg = "ZEMA price file is corrupted!";
							msg_type="W";
						}
					}
					else 
					{
						msg = "Trying to upload price for "+report_dt+" ZEMA Price can not be uploaded before "+SYS_DATE;
						msg_type="W";
					}
					url = "../market_risk/frm_settlement_pricing_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&date="+date+"&report_dt="+report_dt+commonUrl_pra;
					dbcon.commit();
		        } 
	        }
	        
	        if(opration.equals("DELETE")) 
	        {
	        	if(!delete_report_Dt.equals(""))
				{
					queryString = "SELECT COUNT(*) FROM FMS_FORWARD_PRICE_DTL "
		  					+ " WHERE REPORT_DT = TO_DATE(?,'dd/mm/yyyy')";
		  			stmt = dbcon.prepareStatement(queryString);
		  			stmt.setString(1, delete_report_Dt);
		  			rset = stmt.executeQuery();
		  			if(rset.next())
		  			{
		  				if(rset.getInt(1) > 0) 
						{
		  					queryString1="DELETE FROM FMS_FORWARD_PRICE_DTL "
		  							+ "WHERE REPORT_DT=TO_DATE(?,'dd/mm/yyyy') ";
		  					stmt1 = dbcon.prepareStatement(queryString1);
		  					stmt1.setString(1, delete_report_Dt);
		  					stmt1.executeUpdate();
		  					msg = "Pricing for report date "+delete_report_Dt+" deleted Successfully!";
							msg_type="S";
							
							stmt1.close();
		  				}
		  				else
		  				{
		  					msg = "Pricing reported on "+delete_report_Dt+" does not Deleted";
							msg_type="E";
		  				}
		  			}
		  			rset.close();
		  			stmt.close();

		  			queryString2 = "SELECT COUNT(*) FROM FMS_SPOT_PRICE_DTL "
		  					+ " WHERE REPORT_DT=TO_DATE(?,'dd/mm/yyyy')";
		  			stmt2 = dbcon.prepareStatement(queryString2);
		  			stmt2.setString(1, delete_report_Dt);
		  			rset2 = stmt2.executeQuery();
		  			if(rset2.next())
		  			{
		  				if(rset2.getInt(1) > 0) 
						{
		  					queryString1="DELETE FROM FMS_SPOT_PRICE_DTL "
		  							+ "WHERE REPORT_DT=TO_DATE(?,'dd/mm/yyyy') ";
		  					stmt1 = dbcon.prepareStatement(queryString1);
		  					stmt1.setString(1, delete_report_Dt);
		  					stmt1.executeUpdate();
		  					msg = "Pricing reported on "+delete_report_Dt+" deleted Successfully!";
							msg_type="S";
							
							stmt1.close();
		  				}
		  				else
		  				{
		  					msg = "No data found for the Report Date "+delete_report_Dt+" for the ZEMA Price deletion!!";
							msg_type="E";		  				
		  				}
		  			}
		  			rset2.close();
		  			stmt2.close();
		  			dbcon.commit();
				}
				else
				{
					msg="No data found for the Report Date "+delete_report_Dt+" for the ZEMA Price deletion!!";
					msg_type="W";
				}
	        	url = "../market_risk/frm_settlement_pricing_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&date="+date+"&report_dt="+report_dt+commonUrl_pra;
	        }
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);		
			url=CommonVariable.errorpage_url+"?e="+e;
			msg="Error in Exception! ZEMA Price Insert/update Failed";
		}
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}	
	}
	
	private void InsertUpdateHolidayCalendar(HttpServletRequest request)throws IOException,SQLException, ServletException 
	{
		String function_nm="InsertUpdateHolidayCalendar()";
		String opration =request.getParameter("opration");
		
		if(opration.equals("UPLOAD"))
		{
			HttpSession session = request.getSession();
			String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
				
			try
			{	
				//if(opration.equals("UPLOAD")) 
				{			
				String appPath = request.getServletContext().getRealPath("");
		    	
		    	String main_folder=CommonVariable.work_dir;
				File MainDir = new File(appPath+File.separator+main_folder);
		        if(!MainDir.exists()) 
		        {
		        	MainDir.mkdir();
		        }
		        
				String savePath = appPath+File.separator+main_folder+File.separator+SAVE_DIR;
				File fileSaveDir = new File(savePath);
		        if(!fileSaveDir.exists()) 
		        {
		            fileSaveDir.mkdir();
		        }
				
		        String subSavePath = savePath+File.separator+"price_curve_calendar";
		        File subfile = new File(subSavePath);
		        if(!subfile.exists())
		        {
		        	subfile.mkdir();
		        }
		        
		        //For File uploaded file naming
		        LocalDateTime dateObj = LocalDateTime.now();
		        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
		        String date = dateObj.format(formatter1);
		        
		        String file_name="";
		        String fileName="";
		        
		       for (Part part : request.getParts()) 
		        {
		        	fileName = extractFileName(part);
		        	//Refines the fileName in case it is an absolute path
				    fileName = new File(fileName).getName().replace(".xlsx", "");
				    if(!fileName.equals("") )
				    {
				    	file_name=fileName;
				    	part.write(subSavePath +File.separator+ fileName+"_"+date+".xlsx");
				    } 
		        }
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				String file_path =subSavePath+File.separator+file_name+"_"+date+".xlsx";
				
				FileInputStream file = new FileInputStream(new File(file_path));
				
				//XSSFWorkbook workbook = new XSSFWorkbook(file);
				if (isOLE2Stream(file_path)||isOOXMLStream(file_path)) 
				{
					workbook = WorkbookFactory.create(file);
					for(int i=0; i<workbook.getNumberOfSheets();i++)
					{
						String holiday_name = "";
						String holiday_date = "";
						String holiday_status = "";
						String holiday_up_date="";
						String Curve_NM="";
						
						String sheet_name = workbook.getSheetName(i);
						//XSSFSheet sheet = workbook.getSheetAt(i);
						org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(i);
						
						if(!sheet_name.equals(""))
						{				
							Iterator<Row>rowIterator = sheet.iterator();
							while(rowIterator.hasNext()) 
							{
								Curve_NM = "";
								Row row = rowIterator.next();
								int row_no = row.getRowNum();
								if(row_no!=0) 
								{
									Iterator<Cell> cellIterator = row.cellIterator();
									while(cellIterator.hasNext()) 
									{
										Cell cell = cellIterator.next();
										
										if(cell.getColumnIndex()==0)
										{
											holiday_name = cell.getStringCellValue();
										}
										if(cell.getColumnIndex()==1) 
										{
											holiday_date = cell.getStringCellValue();
										}
										if(cell.getColumnIndex()==2) 
										{
											holiday_status = cell.getStringCellValue();
											if(holiday_status.equals("YES")||holiday_status.equals("Yes")) 
											{
												holiday_status = "Y";
											}
											else if(holiday_status.equals("NO")||holiday_status.equals("No"))
											{
												holiday_status = "N";
											}
										}
										if(cell.getColumnIndex()==3) 
										{
											Curve_NM = cell.getStringCellValue();
										}
									}
									if(Curve_NM.length()!=0) 
									{
										query ="SELECT COUNT(*) "
												+ "FROM FMS_CURVE_HOLIDAY_CALND "
												+ "WHERE CURVE_NM=? AND HOLIDAY_DT=TO_DATE(?,'DD/MM/YYYY')";
										stmt = dbcon.prepareStatement(query);
										stmt.setString(1, Curve_NM);
										stmt.setString(2, holiday_date);
										rset=stmt.executeQuery();
										if(rset.next())
										{
											if(rset.getInt(1) > 0) 
											{
												query1="UPDATE FMS_CURVE_HOLIDAY_CALND SET HOLIDAY_NM=?,"
														+ "MODIFY_BY=?,MODIFY_DT=SYSDATE,STATUS=? "
														+ "WHERE HOLIDAY_DT=TO_DATE(?,'DD/MM/YYYY') ";
												stmt1 = dbcon.prepareStatement(query1);
												stmt1.setString(1, holiday_name);
												stmt1.setString(2, emp_cd);
												stmt1.setString(3, holiday_status);
												stmt1.setString(4, holiday_date);
												stmt1.executeUpdate();
												msg = "Successful! - Holiday Calendar Modified Successfully!";
												msg_type="S";
												
												stmt1.close();
											}
											else 
											{
												query1="INSERT INTO FMS_CURVE_HOLIDAY_CALND(CURVE_NM,HOLIDAY_DT,HOLIDAY_NM,STATUS,ENT_BY,ENT_DT) "
														+ "VALUES(?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,SYSDATE) ";
												stmt1 = dbcon.prepareStatement(query1);
												stmt1.setString(1, Curve_NM);
												stmt1.setString(2, holiday_date);
												stmt1.setString(3, holiday_name);
												stmt1.setString(4, holiday_status);
												stmt1.setString(5, emp_cd);
												stmt1.executeUpdate();
												msg = "Successful! - Holiday Calendar Added Successfully!";
												msg_type="S";
												
												stmt1.close();
											}
										}
										else
										{
											msg = "Failed! - Data Submission Failed!";
											msg_type="E";
										}
										rset.close();
										stmt.close();
									}
								}
								url = "../market_risk/frm_holiday_calendar_mst.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
								dbcon.commit();
								}
							}					
						}
					}
					else 
					{
			            msg = "Failed! - The version of the XLS file you're trying to upload is not supported.";
						msg_type="E";
						url = "../market_risk/frm_holiday_calendar_mst.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			        }
				}
				dbcon.commit();
			}
			catch(Exception e)
			{
				new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
				dbcon.rollback();
				url=CommonVariable.errorpage_url+"?e="+e;
			}
			try
			{
				new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
			}
			catch(Exception infoLogger)
			{
				new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
			}	
		}
		else if (opration.equals("INSERT")) 
		{
			HttpSession session = request.getSession();
			String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
				
			try
			{	
				String holiday_name = request.getParameter("holiday_nm")==null?"":request.getParameter("holiday_nm");
				String holiday_date = request.getParameter("holiday_dt")==null?"":request.getParameter("holiday_dt");
				String holiday_status = request.getParameter("status_flag")==null?"":request.getParameter("status_flag");
				String Curve_hidd_NM=request.getParameter("curve_hidd_nm")==null?"":request.getParameter("curve_hidd_nm");
				String Curve_NM=request.getParameter("curve_nm")==null?"":request.getParameter("curve_nm");

				if(Curve_NM.length()!=0 || Curve_hidd_NM.length()!=0) 
				{
					query ="SELECT COUNT(*) "
							+ "FROM FMS_CURVE_HOLIDAY_CALND "
							+ "WHERE CURVE_NM=? AND HOLIDAY_DT=TO_DATE(?,'DD/MM/YYYY')";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, Curve_hidd_NM);
					stmt.setString(2, holiday_date);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						if(rset.getInt(1) > 0) 
						{
							query1="UPDATE FMS_CURVE_HOLIDAY_CALND SET HOLIDAY_NM=?,"
									+ "MODIFY_BY=?,MODIFY_DT=SYSDATE,STATUS=? "
									+ "WHERE HOLIDAY_DT=TO_DATE(?,'DD/MM/YYYY') ";
							stmt1 = dbcon.prepareStatement(query1);
							stmt1.setString(1, holiday_name);
							stmt1.setString(2, emp_cd);
							stmt1.setString(3, holiday_status);
							stmt1.setString(4, holiday_date);
							stmt1.executeUpdate();
							msg = "Successful! - Price Holiday "+holiday_name+" Updated!";
							msg_type="S";
							
							stmt1.close();
						}
						else 
						{
							query1="INSERT INTO FMS_CURVE_HOLIDAY_CALND(CURVE_NM,HOLIDAY_DT,HOLIDAY_NM,STATUS,ENT_BY,ENT_DT) "
									+ "VALUES(?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,SYSDATE) ";
							stmt1 = dbcon.prepareStatement(query1);
							stmt1.setString(1, Curve_NM);
							stmt1.setString(2, holiday_date);
							stmt1.setString(3, holiday_name);
							stmt1.setString(4, holiday_status);
							stmt1.setString(5, emp_cd);
							stmt1.executeUpdate();
							msg = "Successful! - Price Holiday "+holiday_name+" Added!";
							msg_type="S";
							
							stmt1.close();
						}
					}
					else
					{
						msg = "Failed! - Price Holiday Data Submission Failed!";
						msg_type="E";
					}
					rset.close();
					stmt.close();
				}
				url = "../market_risk/frm_holiday_calendar_mst.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
				dbcon.commit();
			}
			catch(Exception e)
			{
				dbcon.rollback();
				new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);				
				url=CommonVariable.errorpage_url+"?e="+e;
				msg = "Error in Exception! - Price Holiday Insert/Update Failed";
				
			}
			
			try
			{
				new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
			}
			catch(Exception infoLogger)
			{
				new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
			}
		}
	}
	
	private void InsertUpdateSettlementCalendar(HttpServletRequest request)throws IOException,SQLException, ServletException
	{
		String function_nm="InsertUpdateSettlementCalendar()";
		String opration =request.getParameter("opration");
		
		if(opration.equals("UPLOAD"))
		{
			HttpSession session = request.getSession();
			String emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
			String comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
				
			try
			{	
				//if(opration.equals("UPLOAD")) 
				{			
				String appPath = request.getServletContext().getRealPath("");
				
		    	String main_folder=CommonVariable.work_dir;
				File MainDir = new File(appPath+File.separator+main_folder);
		        if(!MainDir.exists()) 
		        {
		        	MainDir.mkdir();
		        }
		        
				String savePath = appPath+File.separator+main_folder+File.separator+SAVE_DIR;
				File fileSaveDir = new File(savePath);
		        if(!fileSaveDir.exists()) 
		        {
		            fileSaveDir.mkdir();
		        }
				
		        String subSavePath = savePath+File.separator+"price_curve_calendar";
		        File subfile = new File(subSavePath);
		        if(!subfile.exists())
		        {
		        	subfile.mkdir();
		        }
		        
		        //For File uploaded file naming
		        LocalDateTime dateObj = LocalDateTime.now();
		        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
		        String date = dateObj.format(formatter1);
		        
		        String file_name="";
		        String fileName="";
		        
		       for (Part part : request.getParts()) 
		        {
		        	fileName = extractFileName(part);
		        	//Refines the fileName in case it is an absolute path
				    fileName = new File(fileName).getName().replace(".xlsx", "");
				    if(!fileName.equals("") )
				    {
				    	file_name=fileName;
				    	part.write(subSavePath +File.separator+ fileName+"_"+date+".xlsx");
				    } 
		        }
				
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					
				String file_path =subSavePath+File.separator+file_name+"_"+date+".xlsx";
				FileInputStream file = new FileInputStream(new File(file_path));
				
				//XSSFWorkbook workbook = new XSSFWorkbook(file);
				if (isOLE2Stream(file_path)||isOOXMLStream(file_path)) 
				{
					workbook = WorkbookFactory.create(file);
					
					for(int i=0; i<workbook.getNumberOfSheets();i++)
					{
						String Contract_Month = "";
						String Settlement_Start_DT = "";
						String Settlement_End_DT = "";
						String Settlement_DT="";
						String Curve_NM="";
						
						String space_val = " ";
						
						String sheet_name = workbook.getSheetName(i);
						//XSSFSheet sheet = workbook.getSheetAt(i);
						org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(i);
						
						if(!sheet_name.equals(""))
						{										
							Iterator<Row>rowIterator = sheet.iterator();
							while(rowIterator.hasNext()) 
							{
								Curve_NM = "";
								Row row = rowIterator.next();
								int row_no = row.getRowNum();
								if(row_no!=0) 
								{
									Iterator<Cell> cellIterator = row.cellIterator();
									while(cellIterator.hasNext()) 
									{
										Cell cell = cellIterator.next();
										String tmpCellVal = "";
										
										if(cell.getColumnIndex()==0)
										{
											tmpCellVal = cell.getStringCellValue();
											Contract_Month = tmpCellVal.replaceAll(space_val, "");;
										}
										if(cell.getColumnIndex()==1) 
										{
											tmpCellVal = cell.getStringCellValue();
											Settlement_Start_DT = tmpCellVal.replaceAll(space_val, "");;
										}
										if(cell.getColumnIndex()==2) 
										{
											tmpCellVal = cell.getStringCellValue();
											Settlement_End_DT = tmpCellVal.replaceAll(space_val, "");;
										}
										if(cell.getColumnIndex()==3) 
										{
											tmpCellVal = cell.getStringCellValue();
											Settlement_DT = tmpCellVal.replaceAll(space_val, "");;
										}
										if(cell.getColumnIndex()==4) 
										{
											tmpCellVal = cell.getStringCellValue();
											Curve_NM = tmpCellVal.replaceAll(space_val, "");;
										}
									}

									if(Curve_NM.length()!=0) 
									{
										query ="SELECT COUNT(*) "
												+ "FROM FMS_CURVE_SETTLE_CALND "
												+ "WHERE CURVE_NM=? AND CONT_MONTH=TO_DATE(?,'DD/MM/YYYY')";
										stmt = dbcon.prepareStatement(query);
										stmt.setString(1, Curve_NM);
										stmt.setString(2, Contract_Month);
										rset=stmt.executeQuery();
										if(rset.next())
										{
											if(rset.getInt(1) > 0) 
											{
												query1="UPDATE FMS_CURVE_SETTLE_CALND SET SETTLE_START_DT=TO_DATE(?,'DD/MM/YYYY'), "
														+ "SETTLE_END_DT=TO_DATE(?,'DD/MM/YYYY'), "
														+ "SETTLE_DT=TO_DATE(?,'DD/MM/YYYY'), "
														+ "MODIFY_BY=?, MODIFY_DT=SYSDATE "
														+ "WHERE CURVE_NM=? AND CONT_MONTH=TO_DATE(?,'DD/MM/YYYY')";
												stmt1 = dbcon.prepareStatement(query1);
												stmt1.setString(1, Settlement_Start_DT);
												stmt1.setString(2, Settlement_End_DT);
												stmt1.setString(3, Settlement_DT);
												stmt1.setString(4, emp_cd);
												stmt1.setString(5, Curve_NM);
												stmt1.setString(6, Contract_Month);
												stmt1.executeUpdate();
												msg = "Successful! - Settlement Calendar Modified Successfully!";
												msg_type="S";
												
												stmt1.close();
											}
											else 
											{
												//System.out.println(Contract_Month+"=="+Settlement_Start_DT+"=="+Settlement_End_DT+"=="+Settlement_DT);
												query1="INSERT INTO FMS_CURVE_SETTLE_CALND(CURVE_NM,CONT_MONTH,SETTLE_START_DT,SETTLE_END_DT,SETTLE_DT,ENT_BY,ENT_DT) "
														+ "VALUES(?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,SYSDATE)";
												stmt1 = dbcon.prepareStatement(query1);
												stmt1.setString(1, Curve_NM);
												stmt1.setString(2, Contract_Month);
												stmt1.setString(3, Settlement_Start_DT);
												stmt1.setString(4, Settlement_End_DT);
												stmt1.setString(5, Settlement_DT);
												stmt1.setString(6, emp_cd);
												stmt1.executeUpdate();
												msg = "Successful! - Settlement Calendar Added Successfully!";
												msg_type="S";
												
												stmt1.close();
											}
										}
										else
										{
											msg = "Failed! - Settlement Calendar Data Submission Failed!";
											msg_type="E";
										}
										rset.close();
										stmt.close();
									}
								}
								url = "../market_risk/frm_settlement_calendar_mst.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
								dbcon.commit();
								}
							}					
						}
					}
					else 
					{
			            msg = "Failed! - The version of the XLS file you're trying to upload is not supported.";
						msg_type="E";
						url = "../market_risk/frm_settlement_calendar_mst.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
			        }
				}
				dbcon.commit();
			}
			catch(Exception e)
			{
				dbcon.rollback();
				new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);				
				url=CommonVariable.errorpage_url+"?e="+e;
				msg = "Error in Exception! - Settlement Calendar Insert/Update Failed!";
			}
			try
			{
				new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,(String)session.getAttribute("emp_nm"), (String)session.getAttribute("ip"), form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
			}
			catch(Exception infoLogger)
			{
				new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
			}	
		}
	}
	
	/*
	 * <!--Harsh Maheta 20230504 : Added Function for Extracting File-->
	 */
	private String extractFileName(Part part) 
    {
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
	
	//JD20230925 FOLLOWING FUNCTION CREATED BY JD
	public String getActualCurveName(String prod_nm)
	{
		String function_nm="getActualCurveName()";
		String nm="";
		try
		{
			query="SELECT CURVE_TYPE "
					+ "FROM FMS_PROD_CURVE_MAP "
					+ "WHERE PROD_TYPE=?";
			//System.out.println(query);
			stmt = dbcon.prepareStatement(query);
			stmt.setString(1, prod_nm.toUpperCase());
			rset=stmt.executeQuery();
			if(rset.next())
			{
				nm=rset.getString(1)==null?"":rset.getString(1);
			}
			rset.close();
			stmt.close();
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
		}
		return nm;
	}
}
