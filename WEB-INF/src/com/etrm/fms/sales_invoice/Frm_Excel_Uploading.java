package com.etrm.fms.sales_invoice;

//Servlet Introduced By     : Samik Shah ...
//Servlet Introduced On     : 20th May, 2010 ...
//Code Reviewed By			:  
//Code Reviewed Date		:  
//Code Review Status  		:
//Last Modified By			: Samik Shah ...
//Last Modified Date		: 17th June, 2010 ...

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level; //PP20240708
import java.util.logging.Logger; //PP20240708
//import com.hlpl.hazira.fms7.invoice_report.CipherEncrypter;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;




import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.escapeSingleQuotes;


public class Frm_Excel_Uploading extends HttpServlet
{ 
	private static final Logger LOGGER = Logger.getLogger(Frm_Excel_Uploading.class.getName()); //PP20240708
	public static Connection dbcon;
	
	public String servletName = "Frm_Sales_Invoice";
	public String methodName = "";
	public String option = "";
	public String url = "";
	public String form_name = "";
	public String msg = "";
	int count = 0;
	static escapeSingleQuotes obj = new escapeSingleQuotes();
	
	//Following NumberFormat Object is defined by Samik Shah ... On 2nd June, 2010 ...
	NumberFormat nf = new DecimalFormat("###########0.00");
	NumberFormat nf2 = new DecimalFormat("##0.0000"); //For Currency Purpose ... Defined By Samik Shah On 1st June, 2010 ...
	NumberFormat nf4 = new DecimalFormat("##0.000"); //For Currency Purpose ... Defined By Samik Shah On 1st June, 2010 ...
	NumberFormat nff = new DecimalFormat("######,##0.00");
	NumberFormat nf5 = new DecimalFormat("############.##");
	
	private static String queryString = null;
	private static String queryString1 = null;
	private static String queryString2 = null;
	private static PreparedStatement stmt = null;
	private static PreparedStatement stmt1 = null;
	private static PreparedStatement stmt2 = null;
	private ResultSet rset = null;
	private ResultSet rset1 = null;
	private ResultSet rset2 = null;
	
	public String write_permission = "N";
	public String delete_permission = "N";
	public String print_permission = "N";
	public String check_permission = "N";
	public String authorize_permission = "N";
	public String approve_permission = "N";
	public String audit_permission = "N";
	
	public String form_id = "0";
	public String form_nm = "";
	String supp_cd="1";
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException
	{
		try
		{
			Context Context = new InitialContext();
			if(Context == null)  {
				throw new Exception("Boom - No Context");
			}			
			Context envContext  = (Context)Context.lookup("java:/comp/env");
			
			Context envContext1 = (Context)Context.lookup("java:/comp/env");
			
			DataSource ds = (DataSource)envContext.lookup(RuntimeConf.security_database);			
			if(ds != null) {
				dbcon = ds.getConnection();
			}
			else {
			}
			
			if(dbcon != null) {
				dbcon.setAutoCommit(false);
//				stmt = dbcon.createStatement(); 
//				stmt1 = dbcon.createStatement();
//				stmt2 = dbcon.createStatement(); 
			}			
			option=req.getParameter("option")==null?"":req.getParameter("option");
			write_permission = req.getParameter("write_permission")==null?"N":req.getParameter("write_permission");
			delete_permission = req.getParameter("delete_permission")==null?"N":req.getParameter("delete_permission");
			print_permission = req.getParameter("print_permission")==null?"N":req.getParameter("print_permission");
			check_permission = req.getParameter("check_permission")==null?"N":req.getParameter("check_permission");
			authorize_permission = req.getParameter("authorize_permission")==null?"N":req.getParameter("authorize_permission");
			approve_permission = req.getParameter("approve_permission")==null?"N":req.getParameter("approve_permission");
			audit_permission = req.getParameter("audit_permission")==null?"N":req.getParameter("audit_permission");
			
			form_id = req.getParameter("form_id")==null?"0":req.getParameter("form_id");
			form_nm = req.getParameter("form_nm")==null?"":req.getParameter("form_nm");
			final boolean isMultipart = ServletFileUpload.isMultipartContent(req);
            if (isMultipart) {
                this.upload_excel(req, res);
            }
			
			if(option.equalsIgnoreCase("Prepare_Excel_for_Invoice")) {
				Prepare_Excel_for_Invoice(req,res); 
			}else if(option.equalsIgnoreCase("Download_File")){
				//Download_File(req,res);
			}
			/*SB20240521: Looks not in use
			 else if(option.equalsIgnoreCase("Upload_Excel_FTP")){
				Upload_Excel_FTP(req,res);
			}*/
		}		   
		catch(Exception e) {
			// e.printStackTrace(); Commented  and inserted LOGGER.log(Level.WARNING, "context:", e); //PP20240708 During Codescan
			LOGGER.log(Level.WARNING, "context:", e); //PP20240708
			url="../home/ExceptionHandler.jsp?excp="+e.getMessage()+"&modulename=sales_invoice&formname="+form_name;
		}
		finally {
			if(rset != null) {
				try {
					rset.close();
				} catch(SQLException e) {
					LOGGER.log(Level.WARNING, "context:", e); //PP20240708
				}
			}
			if(rset1 != null) {
				try {
					rset1.close();
				} catch(SQLException e) {
					LOGGER.log(Level.WARNING, "context:", e); //PP20240708
				}
			}
			if(rset2 != null) {
				try {
					rset2.close();
				} catch(SQLException e) {
					LOGGER.log(Level.WARNING, "context:", e); //PP20240708
				}
			}
			if(stmt != null) {
				try {
					stmt.close();
				} catch(SQLException e) {
					LOGGER.log(Level.WARNING, "context:", e); //PP20240708
				} 
			}
			if(stmt1 != null) {
				try {
					stmt1.close();
				} catch(SQLException e) {
					LOGGER.log(Level.WARNING, "context:", e); //PP20240708
				}
			}
			if(stmt2 != null) {
				try {
					stmt2.close();
				} catch(SQLException e) {
					LOGGER.log(Level.WARNING, "context:", e); //PP20240708
				}
			}
			if(dbcon != null) {
				try {
					dbcon.close();
				} catch(SQLException e) {
					LOGGER.log(Level.WARNING, "context:", e); //PP20240708
				}
			}
		}
		try //SB20240510
        {
              res.sendRedirect(url); //SB20240513
        }
        catch(IOException e) //SB20240513
        {
              LOGGER.log(Level.WARNING, "context:", e); //PP20240708 //SB20240513
        }

		
		//res.sendRedirect(url);
	}
	
	 private void upload_excel(final HttpServletRequest request, final HttpServletResponse response) throws SQLException {
	        final String queryString = "";
	        try {
	        	//System.out.println("--new_inv_no--in this method"+write_permission);
	        	String t_dup="";
	        	String t_succ="";
	        	String t_unsucc="";
	        	HttpSession session = request.getSession();
	            this.methodName = "upload_excel()";
	            final List items = new ServletFileUpload((FileItemFactory)new DiskFileItemFactory()).parseRequest(request);
	            final Iterator itr = items.iterator();
	            final Iterator itr2 = items.iterator();
	            final String filepath = request.getRealPath("/pdf_reports/xls_reports/irn_xls_reports");
	            String pathnm = "";
	            String fin_yr_ = "";
	            String month = "",inv_type="",year="",user_cd="",conc_nm="",gstin_no="";
	            final Vector new_inv_no = new Vector();
	            int count = 0;
	            String[] file_bunch_qtr = null;
	            final Map email_map = new HashMap();
	            final Map file_map = new HashMap();
	            while (itr2.hasNext()) {
	                final FileItem item = (FileItem)itr2.next();
	                final String fieldname = item.getFieldName();
	                final String itemName = item.getName();
	                if (item.isFormField() && item.getFieldName().startsWith("excel_name_cyg")) {
	                    new_inv_no.add(item.getString());
	                    ++count;
	                }
	                if(item.isFormField() && item.getFieldName().startsWith("user_cd_hid")){
	                	user_cd=item.getString();
	                }
	                if(item.isFormField() && item.getFieldName().startsWith("write_permission")){
	                	write_permission=item.getString();
	                }
	                if(item.isFormField() && item.getFieldName().startsWith("month")){
	                	month=item.getString();
	                }
	                if(item.isFormField() && item.getFieldName().startsWith("year")){
	                	year=item.getString();
	                }
	                /*if(item.isFormField() && item.getFieldName().startsWith("bill_cycle")){
	                	bill_cycle=item.getString();
	                }
	                if(item.isFormField() && item.getFieldName().startsWith("inv_type")){
	                	inv_type=item.getString();
	                }
	               */
	            }
	            System.out.println("--fin_yr_--"+fin_yr_+"---"+month+"------"+new_inv_no);
	            while (itr.hasNext()) {
	                final FileItem item = (FileItem)itr.next();
	                String fieldname = item.getFieldName();
	                String itemName = item.getName();
	                if (item.isFormField()) {
//	                    for (int i = 0; i < new_inv_no.size(); ++i) {
//	                        final String tempCd = "email_input" + emp_cd2.elementAt(i);
//	                        if (item.getFieldName().startsWith("email_input") && tempCd.equals(item.getFieldName())) {
//	                            email_map.put(emp_cd2.elementAt(i), item.getString());
//	                            final String query = "update hr_emp_mst set email_id='" + item.getString() + "' where emp_cd='" + emp_cd2.elementAt(i) + "'";
//	                            this.stmt1.executeUpdate(query);
//	                        }
//	                    }
	                }
	                else {
	                    fieldname = item.getFieldName();
	                    itemName = item.getName();
	                    String temp_file_nm="";
	                    for (int i = 0; i < new_inv_no.size(); ++i) {
	                        final String tempCd = new_inv_no.elementAt(i)+"";
	                        /*if(inv_type.equals("LTCORA") || inv_type.equals("SUG")){
	                        	temp_file_nm=inv_type+"-"+new_inv_no.elementAt(i);
	                        }else if(inv_type.equals("REC")){
	                        	temp_file_nm="Advance-"+new_inv_no.elementAt(i);
	                        }*/
	                        temp_file_nm=new_inv_no.elementAt(i)+"";
	                        if (item.getFieldName().startsWith("file_input")) {
	                            file_map.put(new_inv_no.elementAt(i), item.getString());
	                            pathnm = item.getName();
	                            final int p1 = itemName.lastIndexOf(47);
	                            String fnm = itemName.substring(p1 + 1);
	                            if(!fnm.equals("")){
		                            fnm = fnm.substring(fnm.lastIndexOf("."), fnm.length());
		                            //System.out.println("--fnm--"+fnm);
		                            final File savedFile = new File(String.valueOf(filepath) + "/" + temp_file_nm + fnm);
		                            //System.out.println("--fnm--"+"/" + temp_file_nm + fnm);
		                            final File lst_qtr = new File(filepath);
		                            file_bunch_qtr = lst_qtr.list();
		                            for (int j = 0; j < file_bunch_qtr.length; ++j) {
		                                final String file = file_bunch_qtr[j];
		                                //System.out.println("-file--"+file);
		                                final String f = file.substring(0, file.indexOf("."));
		                                if (f.equals(new StringBuilder().append(new_inv_no.elementAt(i)).toString())) {
		                                    final File f2 = new File(String.valueOf(filepath) + "/" + file);
		                                    if (f2.exists()) {
		                                        f2.delete();
		                                    }
		                                }
		                            }
		                            item.write(savedFile);
		                           /*String query = "UPDATE FMS7_INV_EXCEL_UPLOAD_DTL set DOWNLOAD_FLAG='Y',DOWNLOAD_BY='"+user_cd+"',DOWNLOAD_DT=TO_DATE(SYSDATE,'DD/MM/YYYY') "
		        		    				+ "WHERE EXCEL_FILE_NM ='"+temp_file_nm+"' AND UPLOAD_FLAG='Y' ";
		        			        //System.out.println("Account_Approval : "+queryString);	        
		        			        stmt.executeUpdate(query);*/
		                           String query = "INSERT INTO FMS7_INV_EXCEL_UPLOAD_DTL (EXCEL_FILE_NM,UPLOAD_FLAG,ENT_BY,ENT_DT) "
		                					+ " VALUES (?,'Y',?,TO_DATE(SYSDATE,'DD/MM/YYYY'))";
		                           stmt = dbcon.prepareStatement(query);
		           	    		   stmt.setString(1, temp_file_nm); 
		        	    		   stmt.setString(2, user_cd);
		                           stmt.executeUpdate();
		                           stmt.close();
		        			        conc_nm+=temp_file_nm+".xlsx"+"@";
	                            }
	                        }
	                    }
	                }
	            }
	            String query = "SELECT GSTIN_NO FROM FMS7_SUPPLIER_MST WHERE SUPPLIER_CD=?";
	            stmt = dbcon.prepareStatement(query);
    		    stmt.setString(1, supp_cd); 
                stmt.executeUpdate();
	            if(rset.next()){
	            	gstin_no=rset.getString(1)==null?"":rset.getString(1);
	            }
                rset.close();
                stmt.close();
	           // Download_File(request,response,);
	            
	            //final String msg = "Excel Sucessfully Uploaded !!";
	            dbcon.commit();
	            String return_val="";
		        if(!conc_nm.equals("")){
		        	return_val=Download_File(request,response,conc_nm,user_cd,gstin_no);
		        }
		        int cnt_dup=0,cnt_succ=0,cnt_unsucc=0;
		        
		        if(!return_val.equals("")){
//		        	System.out.println("return_val---"+return_val);
		        	String temp[]=return_val.split("_");
//		        	System.out.println("return_val--TEMP[1]-"+temp[1]);
		        	String temp_dup[]=temp[0].split("@");
		        	cnt_dup=Integer.parseInt(temp_dup[0]);
		        	String temp_succ[]=temp[1].split("@");
		        	cnt_succ=Integer.parseInt(temp_succ[0]);
		        	String temp_unsucc[]=temp[2].split("@");
		        	cnt_unsucc=Integer.parseInt(temp_unsucc[0]);
		        	//msg ="File Uploaded Successfully";
		        	msg="";
//		        	System.out.println("cnt_succ---"+cnt_succ);
//		        	System.out.println("cnt_unsucc---"+cnt_unsucc);
		        	
			        if(cnt_dup>0){
			        	msg+="Following Invoices already imported : "+temp_dup[1]+"";
			        	t_dup=temp_dup[1];
			        }
			        if(cnt_succ>0){
			        	msg+="Following Invoices Successfully imported : "+temp_succ[1];
			        	t_succ=temp_succ[1];
			        }
			        if(cnt_unsucc>0){
			        	msg+="Following Invoices does not exist : "+temp_unsucc[1]+"";
			        	t_unsucc=temp_unsucc[1];
			        }
		        }else{
		        	msg ="File Uploaded Successfully, But IRN Number Not Generated";
		        }
		        try
				{
					//new com.InfoLogger().writelog("["+session.getAttribute("username")+"/"+session.getAttribute("ip")+"]: "+form_id+"@"+form_nm+"~: "+msg);
				}
				catch(Exception infoLogger)
				{
					//System.out.println("Exception While Writing into InfoLogger Servlet From "+servletName+" Servlet -->\nUnder "+methodName+" Method -->\n"+infoLogger.getMessage());
					// SagarB20240801
			    	// infoLogger.printStackTrace(); 
			    	LOGGER.log(Level.WARNING, "context:", infoLogger); 
				}
	            url = "../sales_invoice/frm_inv_excel_uploading.jsp?msg="+msg+"&t_dup="+t_dup+"&t_succ="+t_succ+"&t_unsucc="+t_unsucc+"&cnt_dup="+cnt_dup+"&cnt_succ="+cnt_succ+"&cnt_unsucc="+cnt_unsucc+"&month="+month+"&year="+year+"&inv_type="+inv_type+"&write_permission="+write_permission+"&delete_permission="+delete_permission+"&print_permission="+print_permission+"&approve_permission="+approve_permission+"&audit_permission="+audit_permission+"&check_permission="+check_permission+"&authorize_permission="+authorize_permission;
	            System.out.println("url--"+url);
	        }
	        catch (Exception e) {
	            final String msg2 = "Excel Upload Failed !!!";
	            dbcon.rollback();
	            LOGGER.log(Level.WARNING, "context:", e); //PP20240708
	            url = "../sales_invoice/frm_inv_excel_uploading.jsp?write_permission="+write_permission+"&delete_permission="+delete_permission+"&print_permission="+print_permission+"&approve_permission="+approve_permission+"&audit_permission="+audit_permission+"&check_permission="+check_permission+"&authorize_permission="+authorize_permission;
	        }
	    }
/*SB20240521: Looks like not in use. Why FMS8NEW ???	 public void Upload_Excel_FTP(HttpServletRequest request,HttpServletResponse response) throws Exception
	 {
			methodName = "uploadXmlFile()";
			String message ="";
			HttpSession session = request.getSession();
			String file_nm=request.getParameter("excel_nm")==null?"":request.getParameter("excel_nm");
			String user_cd = (String)session.getAttribute("user_cd")==null?"0":(String)session.getAttribute("user_cd");
			
			String path = getServletContext().getRealPath("/pdf_reports/xls_reports"); //My path from where we have take file
			Path To_path=new File(path).toPath().normalize(); //HM20240518
			String ORI_path=getServletContext().getRealPath("");//HM20240518
			Path ORI_To_path=new File(ORI_path).toPath().normalize(); //HM20240518
			
			
			boolean error = false;
			FTPClient ftp = new FTPClient();
//			String server="157.32.229.119"; //"192.168.20.111";
			
			boolean login_flag = false;
			FileInputStream fis =null;
			boolean dir_change = false;
			String up_path="/FMS8NEW/pdf_reports/xls_reports/irn_xls_reports";
//			String domain="";
			
			String ftpUrl = "192.168.20.111";
			int defaultPort = 21;
			String user_nm ="user"; 
			String passwd ="ruchi"; 
			
			try
			{
				int reply;
				String msg="";
//				String status = ftp.getStatus();
				System.out.println("status = ");
			    
				ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
			    ftp.connect(ftpUrl, defaultPort);
			    login_flag = ftp.login(user_nm,passwd);
			    //System.out.println("Connected to "+server+" = "+login_flag+"\nReply String = "+ftp.getReplyString()); 
			    // After connection attempt, you should check the reply code to verify success.
			    reply = ftp.getReplyCode();
			    //System.out.println("Server Reply Code = "+reply);
			    if(!FTPReply.isPositiveCompletion(reply)) 
			    {
			        ftp.disconnect();
			        System.err.println("FTP server refused connection.");		       
			    }
			    else
			    {
			    	dir_change = ftp.changeWorkingDirectory(up_path); //var/ lib/tomcat path required
			    	if(dir_change)
			    	{
				    	String fl_nm="";
					    	String docPathXML = path+"\\"+file_nm+".xlsx";
					    	
					    	Path To_docPathXML=new File(docPathXML).toPath().normalize(); //HM20240518
					    	
					    	System.out.println("FTP server Successfully connected.");	
					    	ftp.setFileType(FTP.BINARY_FILE_TYPE);
					    	if(!To_path.startsWith(ORI_To_path)) { //SB20240509
								  throw new IOException("Entry is outside of the target directory!");//SB20240509
							  }
							 else //SB20240509
							  {//SB20240509
					    	//ftp.enterLocalPassiveMode();
					    	fis = new FileInputStream(docPathXML); 
					    	error=ftp.storeFile(file_nm,fis);	
					    	if(error)
					    	{
//					    		fl_nm+=xml_file_upload[i]+", ";
//					    		if(i==(xml_file_upload.length-1))
//					    		{
//					    			fl_nm = fl_nm.substring(0,fl_nm.length()-2);
//					    		}
					    		System.out.println("File Uploaded Successfully on FTP server.");
					    		msg ="Following File(s) Uploaded Successfully on FTP server :<br>"+file_nm;
					    		queryString = "INSERT INTO FMS7_INV_EXCEL_UPLOAD_DTL (EXCEL_FILE_NM,UPLOAD_FLAG,DOWNLOAD_FLAG,ENT_BY,ENT_DT) "
					    					+ " VALUES (?,'Y','',?,TO_DATE(SYSDATE,'DD/MM/YYYY'))";
						        //System.out.println("Account_Approval : "+queryString);	        
					    		   stmt = dbcon.prepareStatement(queryString);
		           	    		   stmt.setString(1, file_nm); 
		        	    		   stmt.setString(2, user_cd);
					    		   stmt.executeUpdate();
					    		   stmt.close();
					    	}
					    	else
					    	{
					    		System.out.println("File Failed to Upload.");		    		
					    		msg ="File Uploading Failed due Access Denied or Some Other Reason !!!";
					    	}
				    	      }
			    	}
			    	else
			    	{
			    		msg ="File Uploading Failed due to failure in changing directory !!!";
			    		System.out.println("Failed to change Directory .");
			    	}
			    }
			    //transfer files
			    ftp.logout();
				message = msg;
				url="../sales_invoice/frm_inv_excel_uploading.jsp?msg="+message;
				dbcon.commit();
				try
				{
					new com.hlpl.hazira.fms7.util.InfoLogger().writelog("["+session.getAttribute("username")+"/"+session.getAttribute("ip")+"]: "+form_id+"@"+form_nm+"~: "+message);
				}
				catch(Exception infoLogger)
				{
					//System.out.println("Exception While Writing into InfoLogger Servlet From "+servletName+" Servlet -->\nUnder "+methodName+" Method -->\n"+infoLogger.getMessage());
					infoLogger.printStackTrace();
				}
			}
			catch(Exception e)
			{
				dbcon.rollback();
				error = true;
//				System.out.println("EXCEPTION:Frm_Xml_Generation-->uploadXmlFile()-->at the time of Saving: "+e.getMessage());
				LOGGER.log(Level.WARNING, "context:", e); //PP20240708
				message = "File Uploading Failed Due to One of the following Reasons :<br>(1) Network Connection Failed to FTP Server.<br>(2) FTP Server Not Responding due to it's Service failure.";
				url="../sales_invoice/frm_inv_excel_uploading.jsp?msg="+message;
			}
			finally 
			{
		      if(ftp.isConnected()) 
		      {
		        try 
		        {
		          ftp.disconnect();
		        } 
		        catch(IOException ioe) 
		        {
		        }
		      }	     
			}
	 }*/
	 
	 public String Download_File(HttpServletRequest request,HttpServletResponse response,String file_nms,String user_cd,String supp_gstin_no) throws Exception
	 {
		 String return_val="";
		 int cnt_success=0;int cnt_unsucess=0;int cnt_dup=0;String dup_inv="", success_inv="",unsuccess_inv="";
		 HttpSession session = request.getSession();
		String user_cd1 = (String)session.getAttribute("user_cd")==null?"0":(String)session.getAttribute("user_cd");
		 try {
			 System.out.println("file_nms--"+file_nms);
			 String file_nm[]=file_nms.split("@");
			 for(int i=0;i<file_nm.length;i++){
				 System.out.println("file_nms--"+file_nm[i]);
				 String invno="",irn_no="",QRCODE="",gstin_no="",b2c_qrcode="";
				 String pdf_path = request.getRealPath("/pdf_reports/xls_reports/irn_xls_reports"); //RG20191218 changed for invoice filtering
					pdf_path=pdf_path+"//"+file_nm[i];
					FileInputStream file = new FileInputStream(new File(pdf_path));
	
					//Create Workbook instance holding reference to .xlsx file
					XSSFWorkbook workbook = new XSSFWorkbook(file);
	
					//Get first/desired sheet from the workbook
					XSSFSheet sheet = workbook.getSheetAt(0);
					//System.out.println("after this"+file);
					//Iterate through each rows one by one
					Iterator<Row> rowIterator = sheet.iterator();
					while (rowIterator.hasNext()) 
					{
						Row row = rowIterator.next();
						//For each row, iterate through all the columns
						Iterator<Cell> cellIterator = row.cellIterator();
						invno="";irn_no="";QRCODE="";gstin_no="";b2c_qrcode="";
						while (cellIterator.hasNext()) 
						{
							Cell cell = cellIterator.next();
							int rw_ind=cell.getRowIndex();
							int row_num=row.getRowNum();
							//System.out.print("row_num--"+row_num);
							if(row_num>0){
							//System.out.print("val--"+cell.getColumnIndex()+"--value---"+cell.getStringCellValue());
								/*if(cell.getColumnIndex()==8 || cell.getColumnIndex()==14){
									if(cell.getColumnIndex()==8){
										invno=cell.getStringCellValue();
									} else if(cell.getColumnIndex()==14){
										irn_no=cell.getStringCellValue();
									}*/
								//System.out.print("cell.getColumnIndex()-in this------"+cell.getColumnIndex());
								if(cell.getColumnIndex()==2 || cell.getColumnIndex()==7 || cell.getColumnIndex()==11 || cell.getColumnIndex()==12 || cell.getColumnIndex()==0 ){
									if(cell.getColumnIndex()==2){
										invno=cell.getStringCellValue();
									} else if(cell.getColumnIndex()==7){
										irn_no=cell.getStringCellValue();
									}
									else if(cell.getColumnIndex()==11){
										QRCODE=cell.getStringCellValue();
									}
									else if(cell.getColumnIndex()==0){
										gstin_no=cell.getStringCellValue();
									}
									else if(cell.getColumnIndex()==12){
										//System.out.print("cell.getColumnIndex()-------"+cell.getColumnIndex());
										b2c_qrcode=cell.getStringCellValue();
										//System.out.print("cell.b2c_qrcode()-------"+b2c_qrcode);
										//System.out.print("cell.b2c_qrcode()---rn----"+irn_no);
									}
									
									
									int cnt=0;
									if(irn_no.equals("") && (!b2c_qrcode.equals(""))){
										queryString="SELECT CONTRACT_TYPE FROM FMS7_INVOICE_MST WHERE NEW_INV_SEQ_NO=?";
										stmt = dbcon.prepareStatement(queryString);
				           	    		stmt.setString(1, invno); 
										rset=stmt.executeQuery();
										//System.out.print("val-------"+queryString);
										if(rset.next()){
											queryString1="SELECT COUNT(*) FROM FMS7_INVOICE_IRN_B2C_DTL WHERE NEW_INV_SEQ_NO=? AND CONTRACT_TYPE=?";
											stmt1 = dbcon.prepareStatement(queryString1);
					           	    		stmt1.setString(1, invno); 
					           	    		stmt1.setString(2, rset.getString(1)); 
											rset1=stmt1.executeQuery();
											//System.out.print("val-11-"+queryString1);
											if(rset1.next()){
												cnt=rset1.getInt(1);
											}
											rset1.close();
											stmt1.close();
											if(cnt==0){
												queryString1="INSERT INTO FMS7_INVOICE_IRN_B2C_DTL (NEW_INV_SEQ_NO,CONTRACT_TYPE,FLAG,ENT_DT,ENT_BY,XLS_FILE_NM,QR_CODE)"
														+ " VALUES (?,?,'Y',SYSDATE,?,?,?)";
												stmt1 = dbcon.prepareStatement(queryString1);
						           	    		stmt1.setString(1, invno); 
						           	    		stmt1.setString(2, rset.getString(1)); 
						           	    		stmt1.setString(3, user_cd1); 
						           	    		stmt1.setString(4, file_nm[i]); 
						           	    		stmt1.setString(5, b2c_qrcode); 
												stmt1.executeUpdate();
												stmt1.close();
												
												if(!success_inv.contains(invno)){
													cnt_success++;
													success_inv+=invno+",";
												}
											}else{
												if(!dup_inv.contains(invno) && (!success_inv.contains(invno))){
													dup_inv+=invno+",";
													cnt_dup++;
												}
											}
											//System.out.print("val--"+queryString1);
										}else{
											
											if(!unsuccess_inv.contains(invno)){
												cnt_unsucess++;
												unsuccess_inv+=invno+",";
											}
										}
										rset.close();
										stmt.close();
									}
									if(!irn_no.equals("") && (!invno.equals("")) && (!QRCODE.equals(""))){
										if(invno.startsWith("C") || invno.startsWith("D")){
//											System.out.println("gstin_no-------"+gstin_no);
//											System.out.println("supp_gstin_no-------"+supp_gstin_no);
											if(!gstin_no.equals(supp_gstin_no)){
												String second_char=invno.substring(1,2);
												//System.out.println("second_char--"+second_char);
												if(second_char.equals("T")){
													
													queryString="SELECT CONTRACT_TYPE FROM DLNG_DR_CR_NOTE WHERE DR_CR_DOC_NO=?";
													stmt = dbcon.prepareStatement(queryString);
							           	    		stmt.setString(1, invno); 
													rset=stmt.executeQuery();
													//System.out.print("val---IN THIS----"+queryString);
													if(rset.next()){
														queryString1="SELECT COUNT(*) FROM FMS7_INVOICE_IRN_DTL WHERE NEW_INV_SEQ_NO=? AND CONTRACT_TYPE=?";
														stmt1 = dbcon.prepareStatement(queryString1);
								           	    		stmt1.setString(1, invno); 
								           	    		stmt1.setString(2, rset.getString(1)); 
														rset1=stmt1.executeQuery();
														if(rset1.next()){
															cnt=rset1.getInt(1);
														}
														rset1.close();
														stmt1.close();
														if(cnt==0){
															queryString1="INSERT INTO FMS7_INVOICE_IRN_DTL (IRN_NO,NEW_INV_SEQ_NO,CONTRACT_TYPE,FLAG,ENT_DT,ENT_BY,XLS_FILE_NM,SIGN_QR_CODE)"
																	+ " VALUES (?,?,?,'Y',SYSDATE,?,?,?)";
															stmt1 = dbcon.prepareStatement(queryString1);
									           	    		stmt1.setString(1, irn_no); 
									           	    		stmt1.setString(2, invno); 
									           	    		stmt1.setString(3, rset.getString(1));
									           	    		stmt1.setString(4, user_cd1); 
									           	    		stmt1.setString(5, file_nm[i]); 
									           	    		stmt1.setString(6, QRCODE); 
															stmt1.executeUpdate();
															stmt1.close();
															
															if(!success_inv.contains(invno)){
																cnt_success++;
															success_inv+=invno+",";
															}
														}else{
															if(!dup_inv.contains(invno) && (!success_inv.contains(invno))){
																dup_inv+=invno+",";
																cnt_dup++;
															}
															
														}
														//System.out.print("val--"+queryString1);
													}else{
														
														if(!unsuccess_inv.contains(invno)){
														unsuccess_inv+=invno+",";
														cnt_unsucess++;
														}
													}
												
												}else{
													queryString="SELECT CONTRACT_TYPE FROM FMS8_OTHINV_DR_CR_NOTE WHERE DR_CR_DOC_NO=?";
													stmt = dbcon.prepareStatement(queryString);
							           	    		stmt.setString(1, invno); 
													rset=stmt.executeQuery();
													//System.out.print("val-------"+queryString);
													if(rset.next()){
														queryString1="SELECT COUNT(*) FROM FMS7_INVOICE_IRN_DTL WHERE NEW_INV_SEQ_NO=? AND CONTRACT_TYPE=?";
														stmt1 = dbcon.prepareStatement(queryString1);
								           	    		stmt1.setString(1, invno); 
								           	    		stmt1.setString(2, rset.getString(1)); 
														rset1=stmt1.executeQuery();
														if(rset1.next()){
															cnt=rset1.getInt(1);
														}
														rset1.close();
														stmt1.close();
														if(cnt==0){
															queryString1="INSERT INTO FMS7_INVOICE_IRN_DTL (IRN_NO,NEW_INV_SEQ_NO,CONTRACT_TYPE,FLAG,ENT_DT,ENT_BY,XLS_FILE_NM,SIGN_QR_CODE)"
																	+ " VALUES (?,?,?,'Y',SYSDATE,?,?,?)";
															stmt1 = dbcon.prepareStatement(queryString1);
									           	    		stmt1.setString(1, irn_no); 
									           	    		stmt1.setString(2, invno); 
									           	    		stmt1.setString(3, rset.getString(1));
									           	    		stmt1.setString(4, user_cd1); 
									           	    		stmt1.setString(5, file_nm[i]); 
									           	    		stmt1.setString(6, QRCODE); 
															stmt1.executeUpdate();
															stmt1.close();
															
															if(!success_inv.contains(invno)){
																cnt_success++;
															success_inv+=invno+",";
															}
														}else{
															if(!dup_inv.contains(invno) && (!success_inv.contains(invno))){
																dup_inv+=invno+",";
																cnt_dup++;
															}
														}
														//System.out.print("val--"+queryString1);
													}else{
														
														if(!unsuccess_inv.contains(invno)){
															cnt_unsucess++;
															unsuccess_inv+=invno+",";
														}
													}
												}
											}else{
												String second_char=invno.substring(1,2);
												//System.out.println("second_char--"+second_char);
												if(second_char.equals("T")){
													
													queryString="SELECT CONTRACT_TYPE FROM DLNG_DR_CR_NOTE WHERE DR_CR_DOC_NO=?";
													stmt = dbcon.prepareStatement(queryString);
							           	    		stmt.setString(1, invno); 
													rset=stmt.executeQuery();
													//System.out.print("val---IN THIS----"+queryString);
													if(rset.next()){
														queryString1="SELECT COUNT(*) FROM FMS7_INVOICE_IRN_DTL WHERE NEW_INV_SEQ_NO=? AND CONTRACT_TYPE=?";
														stmt1 = dbcon.prepareStatement(queryString1);
								           	    		stmt1.setString(1, invno); 
								           	    		stmt1.setString(2, rset.getString(1)); 
														rset1=stmt1.executeQuery();
														if(rset1.next()){
															cnt=rset1.getInt(1);
														}
														rset1.close();
														stmt1.close();
														if(cnt==0){
															queryString1="INSERT INTO FMS7_INVOICE_IRN_DTL (IRN_NO,NEW_INV_SEQ_NO,CONTRACT_TYPE,FLAG,ENT_DT,ENT_BY,XLS_FILE_NM,SIGN_QR_CODE)"
																	+ " VALUES (?,?,?,'Y',SYSDATE,?,?,?)";
															stmt1 = dbcon.prepareStatement(queryString1);
									           	    		stmt1.setString(1, irn_no); 
									           	    		stmt1.setString(2, invno); 
									           	    		stmt1.setString(3, rset.getString(1));
									           	    		stmt1.setString(4, user_cd1); 
									           	    		stmt1.setString(5, file_nm[i]); 
									           	    		stmt1.setString(6, QRCODE); 
															stmt1.executeUpdate();
															stmt1.close();
															
															if(!success_inv.contains(invno)){
																cnt_success++;
															success_inv+=invno+",";
															}
														}else{
															if(!dup_inv.contains(invno) && (!success_inv.contains(invno))){
																dup_inv+=invno+",";
																cnt_dup++;
															}
															
														}
														//System.out.print("val--"+queryString1);
													}else{
														
														if(!unsuccess_inv.contains(invno)){
														unsuccess_inv+=invno+",";
														cnt_unsucess++;
														}
													}
													rset.close();
													stmt.close();
												
													}else{
													queryString="SELECT CONTRACT_TYPE FROM FMS7_DR_CR_NOTE WHERE DR_CR_DOC_NO=?";
													stmt = dbcon.prepareStatement(queryString);
							           	    		stmt.setString(1, invno); 
													rset=stmt.executeQuery();
													//System.out.print("val---IN THIS----"+queryString);
													if(rset.next()){
														queryString1="SELECT COUNT(*) FROM FMS7_INVOICE_IRN_DTL WHERE NEW_INV_SEQ_NO=? AND CONTRACT_TYPE=?";
														stmt1 = dbcon.prepareStatement(queryString1);
								           	    		stmt1.setString(1, invno); 
								           	    		stmt1.setString(2, rset.getString(1)); 
														rset1=stmt1.executeQuery();
														if(rset1.next()){
															cnt=rset1.getInt(1);
														}
														rset1.close();
														stmt1.close();
														if(cnt==0){
															queryString1="INSERT INTO FMS7_INVOICE_IRN_DTL (IRN_NO,NEW_INV_SEQ_NO,CONTRACT_TYPE,FLAG,ENT_DT,ENT_BY,XLS_FILE_NM,SIGN_QR_CODE)"
																	+ " VALUES (?,?,?,'Y',SYSDATE,?,?,?)";
															stmt1 = dbcon.prepareStatement(queryString1);
									           	    		stmt1.setString(1, irn_no); 
									           	    		stmt1.setString(2, invno); 
									           	    		stmt1.setString(3, rset.getString(1));
									           	    		stmt1.setString(4, user_cd1); 
									           	    		stmt1.setString(5, file_nm[i]); 
									           	    		stmt1.setString(6, QRCODE); 
															stmt1.executeUpdate();
															stmt1.close();
															
															if(!success_inv.contains(invno)){
																cnt_success++;
															success_inv+=invno+",";
															}
														}else{
															if(!dup_inv.contains(invno) && (!success_inv.contains(invno))){
																dup_inv+=invno+",";
																cnt_dup++;
															}
															
														}
														//System.out.print("val--"+queryString1);
													}else{
														
														if(!unsuccess_inv.contains(invno)){
														unsuccess_inv+=invno+",";
														cnt_unsucess++;
														}
													}
													rset.close();
													stmt.close();
												}
											}
											
										}else if(invno.startsWith("T")){
											queryString="SELECT CONTRACT_TYPE FROM DLNG_INVOICE_MST WHERE NEW_INV_SEQ_NO=?";
											stmt = dbcon.prepareStatement(queryString);
					           	    		stmt.setString(1, invno); 
											rset=stmt.executeQuery();
											//System.out.print("val-------"+queryString);
											if(rset.next()){
												queryString1="SELECT COUNT(*) FROM FMS7_INVOICE_IRN_DTL WHERE NEW_INV_SEQ_NO=? AND CONTRACT_TYPE=?";
												stmt1 = dbcon.prepareStatement(queryString1);
						           	    		stmt1.setString(1, invno); 
						           	    		stmt1.setString(2, rset.getString(1)); 
												rset1=stmt1.executeQuery();
												//System.out.print("val-11-"+queryString1);
												if(rset1.next()){
													cnt=rset1.getInt(1);
												}
												rset1.close();
												stmt1.close();
												if(cnt==0){
													queryString1="INSERT INTO FMS7_INVOICE_IRN_DTL (IRN_NO,NEW_INV_SEQ_NO,CONTRACT_TYPE,FLAG,ENT_DT,ENT_BY,XLS_FILE_NM,SIGN_QR_CODE)"
															+ " VALUES (?,?,?,'Y',SYSDATE,?,?,?)";
													stmt1 = dbcon.prepareStatement(queryString1);
							           	    		stmt1.setString(1, irn_no); 
							           	    		stmt1.setString(2, invno); 
							           	    		stmt1.setString(3, rset.getString(1));
							           	    		stmt1.setString(4, user_cd1); 
							           	    		stmt1.setString(5, file_nm[i]); 
							           	    		stmt1.setString(6, QRCODE); 
													stmt1.executeUpdate();
													stmt1.close();
													
													if(!success_inv.contains(invno)){
													success_inv+=invno+",";
													cnt_success++;
													}
												}else{
													
													if(!dup_inv.contains(invno) && (!success_inv.contains(invno))){
														dup_inv+=invno+",";
														cnt_dup++;
													}
												}
												//System.out.print("val--"+queryString1);
											}else{
												//System.out.println("in thiss"+unsuccess_inv);
												if(!unsuccess_inv.contains(invno)){
													//System.out.println("in thiss111111");
													cnt_unsucess++;
													unsuccess_inv+=invno+",";
													//System.out.println("in thiss>>>>>>>>>>>>"+unsuccess_inv);
													}
												
											}
											rset.close();
											stmt.close();
										}else if(invno.startsWith("LT")){
											queryString="SELECT CONTRACT_TYPE FROM DLNG_INVOICE_MST WHERE NEW_INV_SEQ_NO=? and contract_type='M'";
											stmt = dbcon.prepareStatement(queryString);
					           	    		stmt.setString(1, invno); 
											rset=stmt.executeQuery();
										//	System.out.print("val-------"+queryString);
											if(rset.next()){
												queryString1="SELECT COUNT(*) FROM FMS7_INVOICE_IRN_DTL WHERE NEW_INV_SEQ_NO=? AND CONTRACT_TYPE=?";
												stmt1 = dbcon.prepareStatement(queryString1);
						           	    		stmt1.setString(1, invno); 
						           	    		stmt1.setString(2, rset.getString(1)); 
												rset1=stmt1.executeQuery();
											//	System.out.print("val-11-"+queryString1);
												if(rset1.next()){
													cnt=rset1.getInt(1);
												}
												rset1.close();
												stmt1.close();
												if(cnt==0){
													queryString1="INSERT INTO FMS7_INVOICE_IRN_DTL (IRN_NO,NEW_INV_SEQ_NO,CONTRACT_TYPE,FLAG,ENT_DT,ENT_BY,XLS_FILE_NM,SIGN_QR_CODE)"
															+ " VALUES (?,?,?,'Y',SYSDATE,?,?,?)";
													stmt1 = dbcon.prepareStatement(queryString1);
							           	    		stmt1.setString(1, irn_no); 
							           	    		stmt1.setString(2, invno); 
							           	    		stmt1.setString(3, rset.getString(1));
							           	    		stmt1.setString(4, user_cd1); 
							           	    		stmt1.setString(5, file_nm[i]); 
							           	    		stmt1.setString(6, QRCODE); 
													stmt1.executeUpdate();
													stmt1.close();
													
													if(!success_inv.contains(invno)){
													success_inv+=invno+",";
													cnt_success++;
													}
												}else{
													
													if(!dup_inv.contains(invno) && (!success_inv.contains(invno))){
														dup_inv+=invno+",";
														cnt_dup++;
													}
												}
												//System.out.print("val--"+queryString1);
											}else{
												//System.out.println("in thiss"+unsuccess_inv);
												if(!unsuccess_inv.contains(invno)){
													//System.out.println("in thiss111111");
													cnt_unsucess++;
													unsuccess_inv+=invno+",";
//													System.out.println("in thiss>>>>>>>>>>>>"+unsuccess_inv);
													}
												
											}
											rset.close();
											stmt.close();
										}
										else{
											queryString="SELECT CONTRACT_TYPE FROM FMS7_INVOICE_MST WHERE NEW_INV_SEQ_NO=?";
											stmt = dbcon.prepareStatement(queryString);
					           	    		stmt.setString(1, invno); 
											rset=stmt.executeQuery();
//											System.out.print("val-------"+queryString);
											if(rset.next()){
												queryString1="SELECT COUNT(*) FROM FMS7_INVOICE_IRN_DTL WHERE NEW_INV_SEQ_NO=? AND CONTRACT_TYPE=?";
												stmt1 = dbcon.prepareStatement(queryString1);
						           	    		stmt1.setString(1, invno); 
						           	    		stmt1.setString(2, rset.getString(1)); 
												rset1=stmt1.executeQuery();
												//System.out.print("val-11-"+queryString1);
												if(rset1.next()){
													cnt=rset1.getInt(1);
												}
												if(cnt==0){
													queryString1="INSERT INTO FMS7_INVOICE_IRN_DTL (IRN_NO,NEW_INV_SEQ_NO,CONTRACT_TYPE,FLAG,ENT_DT,ENT_BY,XLS_FILE_NM,SIGN_QR_CODE)"
															+ " VALUES (?,?,?,'Y',SYSDATE,?,?,?)";
													stmt1 = dbcon.prepareStatement(queryString1);
							           	    		stmt1.setString(1, irn_no); 
							           	    		stmt1.setString(2, invno); 
							           	    		stmt1.setString(3, rset.getString(1));
							           	    		stmt1.setString(4, user_cd1); 
							           	    		stmt1.setString(5, file_nm[i]); 
							           	    		stmt1.setString(6, QRCODE); 
													stmt1.executeUpdate();
													stmt1.close();
													
													if(!success_inv.contains(invno)){
														cnt_success++;
														success_inv+=invno+",";
														}
												}else{
													if(!dup_inv.contains(invno) && (!success_inv.contains(invno))){
														dup_inv+=invno+",";
														cnt_dup++;
													}
												}
												//System.out.print("val--"+queryString1);
											}else{
												
												if(!unsuccess_inv.contains(invno)){
													cnt_unsucess++;
													unsuccess_inv+=invno+",";
													}
											}
											rset.close();
											stmt.close();
										}
									}
									dbcon.commit();
									//System.out.print("val--"+cell.getStringCellValue());
									
								//Check the cell type and format accordingly
									/*switch (cell.getCellType()) 
									{
											case Cell.CELL_TYPE_STRING:
												
											System.out.print("val--"+cell.getStringCellValue() + "\t");
											break;
									}*/
								}
						}
						}
						//System.out.println("");
					}
	
	//				Row row = sheet.getRow(0);	
	//				Cell cell = row.getCell((short)7);
	//				System.out.println("--cell--"+cell);
					file.close();
			 }
			
			 return_val=cnt_dup+"@"+dup_inv+"_"+cnt_success+"@"+success_inv+"_"+cnt_unsucess+"@"+unsuccess_inv;
//				String msg="File downloaded successfully";
//				url = "../sales_invoice/frm_inv_excel_uploading.jsp?msg=" + msg;

		} catch (Exception e) {
			// TODO: handle exception
			return_val="";
			LOGGER.log(Level.WARNING, "context:", e); //PP20240708
			dbcon.rollback();
			String msg="Failes to download file";
			url = "../sales_invoice/frm_inv_excel_uploading.jsp?msg=" + msg;
		}
		 return return_val;
	 }
	 
	 public void Prepare_Excel_for_Invoice(HttpServletRequest request,HttpServletResponse response) throws Exception
	 {
		 HttpSession session = request.getSession();
		 String user_cd = (String)session.getAttribute("user_cd")==null?"0":(String)session.getAttribute("user_cd");
		 String month=StringEscapeUtils.escapeXml(request.getParameter("month")==null?"":request.getParameter("month"));//HM20240522
		 String year=request.getParameter("year")==null?"":request.getParameter("year");
		 String inv_type=request.getParameter("inv_type")==null?"":request.getParameter("inv_type");
		 String fin_yr_hid=request.getParameter("fin_yr_hid")==null?"":request.getParameter("fin_yr_hid");
//		 String supplier_nm=request.getParameter("supplier_nm") ==null?"":request.getParameter("supplier_nm");
//		 String supplier_city=request.getParameter("supplier_city") ==null?"":request.getParameter("supplier_city");
//		 String supplier_statecd=request.getParameter("supplier_statecd") ==null?"":request.getParameter("supplier_statecd");
//		 String supplier_pincd=request.getParameter("supplier_pincd") ==null?"":request.getParameter("supplier_pincd");
//		 String supplier_gstinno=request.getParameter("supplier_gstinno") ==null?"":request.getParameter("supplier_gstinno");
		 String supplier_sac_cd=request.getParameter("supplier_sac_cd") ==null?"":request.getParameter("supplier_sac_cd");
		 String supplier_sac_desc=request.getParameter("supplier_sac_desc") ==null?"":request.getParameter("supplier_sac_desc");
//		 String supplier_addr=request.getParameter("supplier_addr")==null?"":request.getParameter("supplier_addr");
		 String invoice_no[]=request.getParameterValues("new_inv_no");
		 String chk_flag[]=request.getParameterValues("chk_flag");
		 String invoice_type[]=request.getParameterValues("invoice_type");
		 String customer_nm[]=request.getParameterValues("customer_nm");
		 String customer_city[]=request.getParameterValues("customer_city");
		 String customer_pincd[]=request.getParameterValues("customer_pincd");
		 String customer_statecd[]=request.getParameterValues("customer_statecd");
		 String customer_gstinno[]=request.getParameterValues("customer_gstinno");
		 String customer_addr[]=request.getParameterValues("customer_addr");
		 String sac_cd_cust[]=request.getParameterValues("sac_cd_cust");
		 String sac_code_flag[]=request.getParameterValues("sac_code_flag");
		 String item_descr[]=request.getParameterValues("item_descr");
		 
		 String totalqty[]=request.getParameterValues("total_qty");
		 String tax_struct_cd[]=request.getParameterValues("tax_struct_cd");
		 String period_end_dt[]=request.getParameterValues("period_end_dt");
		 String gross_amt_inr[]=request.getParameterValues("gross_amt_inr");
		 String net_amt_inr[]=request.getParameterValues("net_amt_inr");
		 String priceper_qty[]=request.getParameterValues("priceperqty");
		 String inv_dt[]=request.getParameterValues("inv_dt");
		 String remark_specification[]=request.getParameterValues("remark_specification");
		 String suppl_nm[]=request.getParameterValues("Vsupplier_nm");
		 String suppl_city[]=request.getParameterValues("Vsupplier_city");
		 String suppl_addr[]=request.getParameterValues("Vsupplier_addr");
		 String suppl_pin[]=request.getParameterValues("Vsupplier_pin");
		 String suppl_st_cd[]=request.getParameterValues("Vsupplier_state_cd");
		 String suppl_gstin[]=request.getParameterValues("Vsupplier_gstin");
		 String Vinv_cont_typ[]=request.getParameterValues("Vinv_cont_typ");
		 String docval[]=request.getParameterValues("docval");
		 String Vb2c_hid[]=request.getParameterValues("Vb2c_hid");
		 String Vpayee_nm_hid[]=request.getParameterValues("Vpayee_nm_hid");
		 String Vpayee_no_hid[]=request.getParameterValues("Vpayee_no_hid");
		 String Vifsc_hid[]=request.getParameterValues("Vifsc_hid");
		 String Vsupp_cd[]=request.getParameterValues("Vsupp_cd");
		 
		 int cnt=0;
		 int tax_factor=0;Vector customer_Invoice_Tax_Amt=new Vector();
		 String tax_struct_code="",bill_period_end_dt=""; 
		 double gross_inr=0.00;
		 double tax_amt = 0;
		 Vector cust_nm=new Vector();
		 Vector cust_city=new Vector();
		 Vector cust_addr=new Vector();
		 Vector cust_pincd=new Vector();
		 Vector cust_statecd=new Vector();
		 Vector cust_gstinno=new Vector();
		 Vector hlpl_inv_no=new Vector();
		 Vector inv_date=new Vector();
		 Vector priceperqty=new Vector();
		 Vector gross_amt=new Vector();
		 Vector net_amt=new Vector();
		 Vector flag_invoice=new Vector();
		 Vector item_desc=new Vector();
		 Vector contract_type=new Vector();
		 Vector total_qty=new Vector();
		 Vector inv_dt_format=new Vector();
		 Vector tax_fact=new Vector();
		 Vector doc_type=new Vector();
		 Vector hsn_sac_cd=new Vector();
		 Vector hsn_sac_flag=new Vector();
		 Vector Vsuppl_addr=new Vector();
		 Vector Vsuppl_nm=new Vector();
		 Vector Vsuppl_city=new Vector();
		 Vector Vsuppl_pin=new Vector();
		 Vector Vsuppl_st_cd=new Vector();
		 Vector Vsuppl_gstin=new Vector();
		 Vector sr_no=new Vector();
		 Vector sac_hsn_flag=new Vector();
		 Vector documentval=new Vector();
		 Vector Vb2c=new Vector();
		 Vector Vpayee_nm=new Vector();
		 Vector Vpayee_no=new Vector();
		 Vector Vifsc=new Vector();
		 Vector supp_cd=new Vector();
		 String systime=""; 
		 
		// System.out.println("invoice_no--"+invoice_no.length);
		 
		 
		 try {
			 queryString="SELECT TO_CHAR(SYSDATE,'DDMMYYYY-HH24MI') FROM DUAL";
			 stmt = dbcon.prepareStatement(queryString);
			 rset=stmt.executeQuery();
			 if(rset.next()){
				 systime=rset.getString(1);
			 }
			 rset.close();
			 stmt.close();
			if(chk_flag.length>0){
				for(int i=0;i<chk_flag.length;i++){
					if(chk_flag[i].equalsIgnoreCase("Y")){
						//System.out.println("invoice_no--"+chk_flag.length);
						if(invoice_type[i].equalsIgnoreCase("LTCORA"))
						{
							cust_nm.add(customer_nm[i]);
							cust_city.add(customer_city[i]);
							cust_pincd.add(customer_pincd[i]);
							cust_statecd.add(customer_statecd[i]);
							cust_gstinno.add(customer_gstinno[i]);
							cust_addr.add(customer_addr[i]);
							hlpl_inv_no.add(invoice_no[i]);
							priceperqty.add(priceper_qty[i]);
							gross_amt.add(gross_amt_inr[i]);
							net_amt.add(net_amt_inr[i]);
							documentval.add(net_amt_inr[i]);
							tax_struct_code=""+tax_struct_cd[i];
							bill_period_end_dt=period_end_dt[i];
							gross_inr=Double.parseDouble(gross_amt_inr[i]);
							total_qty.add(totalqty[i]);
							flag_invoice.add("Y");
							item_desc.add("Natural Gas (Regasified)");
							contract_type.add(Vinv_cont_typ[i]);
							inv_dt_format.add(inv_dt[i]);
							doc_type.add("INV");
							hsn_sac_cd.add(sac_cd_cust[i]);
							Vsuppl_nm.add(suppl_nm[i]);
							Vsuppl_city.add(suppl_city[i]);
							Vsuppl_addr.add(suppl_addr[i]);
							Vsuppl_gstin.add(suppl_gstin[i]);
							Vsuppl_pin.add(suppl_pin[i]);
							Vsuppl_st_cd.add(suppl_st_cd[i]);
							sac_hsn_flag.add(sac_code_flag[i]);
							Vb2c.add(Vb2c_hid[i]);
							Vpayee_nm.add(Vpayee_nm_hid[i]);
							Vpayee_no.add(Vpayee_no_hid[i]);
							Vifsc.add(Vifsc_hid[i]);
							supp_cd.add(Vsupp_cd[i]);
							sr_no.add("1");
							
								String tax_str="";
								tax_factor=0;
								queryString = "SELECT NVL(A.tax_code,'0'), NVL(A.factor,'0.00'), NVL(A.tax_on,'1'), NVL(A.tax_on_cd,'0'), " +
										  "TO_CHAR(A.app_date,'DD/MM/YYYY') FROM FMS7_TAX_STRUCTURE_DTL A WHERE A.tax_str_cd=? AND " +
										  "A.app_date=(SELECT MAX(B.app_date) FROM FMS7_TAX_STRUCTURE_DTL B WHERE B.tax_str_cd=? AND " +
										  "B.app_date<=TO_DATE(?,'DD/MM/YYYY')) ORDER BY A.tax_code";
								////System.out.println("STEP-1A.3:FMS7_TAX_STRUCTURE_DTL: "+queryString);
								stmt = dbcon.prepareStatement(queryString);
								stmt.setString(1, tax_struct_code);
								stmt.setString(2, tax_struct_code);
							    stmt.setString(3, bill_period_end_dt);
								rset=stmt.executeQuery();
								while(rset.next())
								{
									tax_factor+= Integer.parseInt((rset.getString(2)));
									if(rset.getString(3).equals("1"))
									{
										//tax_amt = (Double.parseDouble(customer_Invoice_Gross_Amt_INR)*Double.parseDouble(rset.getString(2)))/100;
										tax_amt = (gross_inr*Double.parseDouble(rset.getString(2)))/100;
										
									}
									else if(rset.getString(3).equals("2"))
									{
										queryString1 = "SELECT NVL(A.tax_code,'0'), NVL(A.factor,'0'), NVL(A.tax_on,'1'), NVL(A.tax_on_cd,'0'), " +
												  "TO_CHAR(A.app_date,'DD/MM/YYYY') FROM FMS7_TAX_STRUCTURE_DTL A WHERE A.tax_str_cd=? AND " +
												  "A.app_date=(SELECT MAX(B.app_date) FROM FMS7_TAX_STRUCTURE_DTL B WHERE B.tax_str_cd=? AND " +
												  "B.app_date<=TO_DATE(?,'DD/MM/YYYY')) AND A.tax_code=?";
									//////System.out.println("Query For Finding Out Tax Which Is Dependent On Other Tax Value = "+queryString1);
										stmt1 = dbcon.prepareStatement(queryString1);
										stmt1.setString(1, tax_struct_code);
										stmt1.setString(2, tax_struct_code);
									    stmt1.setString(3, bill_period_end_dt);
									    stmt1.setString(4, ""+rset.getString(4));
										rset1=stmt1.executeQuery();
								 		if(rset1.next())
								 		{
								 			if(rset1.getString(3).equals("1"))
											{
												//tax_amt = (Double.parseDouble(customer_Invoice_Gross_Amt_INR)*Double.parseDouble(rset1.getString(2)))/100;
								 				tax_amt = (gross_inr*Double.parseDouble(rset1.getString(2)))/100;
											}
										
							 			tax_amt = (tax_amt*Double.parseDouble(rset.getString(2)))/100;
								 		}
								 		else
								 		{
								 			tax_amt = 0;
								 		}
								 		rset1.close();
								 		stmt1.close();
									}
									else
									{
										tax_amt = 0;
									}
								
								//customer_Invoice_Tax_Amt.add(nf.format(Math.round(tax_amt)));
								tax_str+=nf.format(Math.round(tax_amt))+"@";
							}
							rset.close();
							stmt.close();
								tax_fact.add(tax_factor);
								customer_Invoice_Tax_Amt.add(tax_str);
						}else if(invoice_type[i].equalsIgnoreCase("DLNG_SER")){
							String temp[]=priceper_qty[i].split("@");
							String temp_gross[]=gross_amt_inr[i].split("@");
							String net[]=net_amt_inr[i].split("@");
							String qty[]=totalqty[i].split("@");
							String sac[]=sac_cd_cust[i].split("@"); 
							String taxcd[]=tax_struct_cd[i].split("@");
							String desc[]=item_descr[i].split("@");
							String docvalue[]=docval[i].split("@");
							String tax[]=remark_specification[i].split("%%");
							//System.out.println("temp.length--"+temp.length);
							for(int k=0;k<temp.length;k++){
								//System.out.println("temp.length--"+sac[k]);	
								cust_nm.add(customer_nm[i]);
								cust_city.add(customer_city[i]);
								cust_pincd.add(customer_pincd[i]);
								cust_statecd.add(customer_statecd[i]);
								cust_gstinno.add(customer_gstinno[i]);
								cust_addr.add(customer_addr[i]);
								hlpl_inv_no.add(invoice_no[i]);
								//tax_struct_code=""+tax_struct_cd[i];
								bill_period_end_dt=period_end_dt[i];
								//gross_inr=Double.parseDouble(gross_amt_inr[i]);
								flag_invoice.add("O");
								item_desc.add(desc[k]);
								contract_type.add(Vinv_cont_typ[i]);
								inv_dt_format.add(inv_dt[i]);
								sac_hsn_flag.add(sac_code_flag[i]);
								doc_type.add("INV");
								sr_no.add(k+1);
								Vsuppl_nm.add(suppl_nm[i]);
								Vsuppl_city.add(suppl_city[i]);
								Vsuppl_addr.add(suppl_addr[i]);
								Vsuppl_gstin.add(suppl_gstin[i]);
								Vsuppl_pin.add(suppl_pin[i]);
								Vsuppl_st_cd.add(suppl_st_cd[i]);
								priceperqty.add(temp[k]);
								gross_amt.add(temp_gross[k]);
								net_amt.add(net[k]);
								documentval.add(docvalue[k]);
								total_qty.add(qty[k]);
								hsn_sac_cd.add(sac[k]);
								tax_fact.add(taxcd[k]);
								customer_Invoice_Tax_Amt.add(tax[k]);
								Vb2c.add(Vb2c_hid[i]);
								Vpayee_nm.add(Vpayee_nm_hid[i]);
								Vpayee_no.add(Vpayee_no_hid[i]);
								Vifsc.add(Vifsc_hid[i]);
								supp_cd.add(Vsupp_cd[i]);
							}
						}
						else if(invoice_type[i].equalsIgnoreCase("DLNG_LATEPAY")){
							cust_nm.add(customer_nm[i]);
							cust_city.add(customer_city[i]);
							cust_pincd.add(customer_pincd[i]);
							cust_statecd.add(customer_statecd[i]);
							cust_gstinno.add(customer_gstinno[i]);
							cust_addr.add(customer_addr[i]);
							hlpl_inv_no.add(invoice_no[i]);
							priceperqty.add(priceper_qty[i]);
							gross_amt.add(gross_amt_inr[i]);
							net_amt.add(net_amt_inr[i]);
							documentval.add(net_amt_inr[i]);
							tax_struct_code=""+tax_struct_cd[i];
							bill_period_end_dt=period_end_dt[i];
							gross_inr=Double.parseDouble(gross_amt_inr[i]);
							total_qty.add(totalqty[i]);
							flag_invoice.add("Y");
							item_desc.add("Natural Gas (Regasified)");
							contract_type.add(Vinv_cont_typ[i]);
							inv_dt_format.add(inv_dt[i]);
							doc_type.add("INV");
							hsn_sac_cd.add(sac_cd_cust[i]);
							Vsuppl_nm.add(suppl_nm[i]);
							Vsuppl_city.add(suppl_city[i]);
							Vsuppl_addr.add(suppl_addr[i]);
							Vsuppl_gstin.add(suppl_gstin[i]);
							Vsuppl_pin.add(suppl_pin[i]);
							Vsuppl_st_cd.add(suppl_st_cd[i]);
							sac_hsn_flag.add(sac_code_flag[i]);
							Vb2c.add(Vb2c_hid[i]);
							Vpayee_nm.add(Vpayee_nm_hid[i]);
							Vpayee_no.add(Vpayee_no_hid[i]);
							Vifsc.add(Vifsc_hid[i]);
							supp_cd.add(Vsupp_cd[i]);
							sr_no.add("1");
							tax_fact.add(tax_struct_cd[i]);
							customer_Invoice_Tax_Amt.add(remark_specification[i]);
						
						}
						else if(invoice_type[i].equalsIgnoreCase("LATEPAY")){
							cust_nm.add(customer_nm[i]);
							cust_city.add(customer_city[i]);
							cust_pincd.add(customer_pincd[i]);
							cust_statecd.add(customer_statecd[i]);
							cust_gstinno.add(customer_gstinno[i]);
							cust_addr.add(customer_addr[i]);
							hlpl_inv_no.add(invoice_no[i]);
							priceperqty.add(priceper_qty[i]);
							gross_amt.add(gross_amt_inr[i]);
							net_amt.add(net_amt_inr[i]);
							documentval.add(net_amt_inr[i]);
							tax_struct_code=""+tax_struct_cd[i];
							bill_period_end_dt=period_end_dt[i];
							gross_inr=Double.parseDouble(gross_amt_inr[i]);
							total_qty.add(totalqty[i]);
							flag_invoice.add("Y");
							item_desc.add("Natural Gas (Regasified)");
							contract_type.add(Vinv_cont_typ[i]);
							inv_dt_format.add(inv_dt[i]);
							doc_type.add("INV");
							hsn_sac_cd.add(sac_cd_cust[i]);
							Vsuppl_nm.add(suppl_nm[i]);
							Vsuppl_city.add(suppl_city[i]);
							Vsuppl_addr.add(suppl_addr[i]);
							Vsuppl_gstin.add(suppl_gstin[i]);
							Vsuppl_pin.add(suppl_pin[i]);
							Vsuppl_st_cd.add(suppl_st_cd[i]);
							sac_hsn_flag.add(sac_code_flag[i]);
							Vb2c.add(Vb2c_hid[i]);
							Vpayee_nm.add(Vpayee_nm_hid[i]);
							Vpayee_no.add(Vpayee_no_hid[i]);
							Vifsc.add(Vifsc_hid[i]);
							supp_cd.add(Vsupp_cd[i]);
							sr_no.add("1");
							
								String tax_str="";
								tax_factor=0;
								queryString = "SELECT NVL(A.tax_code,'0'), NVL(A.factor,'0.00'), NVL(A.tax_on,'1'), NVL(A.tax_on_cd,'0'), " +
										  "TO_CHAR(A.app_date,'DD/MM/YYYY') FROM FMS7_TAX_STRUCTURE_DTL A WHERE A.tax_str_cd=? AND " +
										  "A.app_date=(SELECT MAX(B.app_date) FROM FMS7_TAX_STRUCTURE_DTL B WHERE B.tax_str_cd=? AND " +
										  "B.app_date<=TO_DATE(?,'DD/MM/YYYY')) ORDER BY A.tax_code";
								////System.out.println("STEP-1A.3:FMS7_TAX_STRUCTURE_DTL: "+queryString);
								stmt = dbcon.prepareStatement(queryString);
								stmt.setString(1, tax_struct_code);
								stmt.setString(2, tax_struct_code);
							    stmt.setString(3, bill_period_end_dt);
								rset=stmt.executeQuery();
								while(rset.next())
								{
									tax_factor+= Integer.parseInt((rset.getString(2)));
									if(rset.getString(3).equals("1"))
									{
										//tax_amt = (Double.parseDouble(customer_Invoice_Gross_Amt_INR)*Double.parseDouble(rset.getString(2)))/100;
										tax_amt = (gross_inr*Double.parseDouble(rset.getString(2)))/100;
										
									}
									else if(rset.getString(3).equals("2"))
									{
										queryString1 = "SELECT NVL(A.tax_code,'0'), NVL(A.factor,'0'), NVL(A.tax_on,'1'), NVL(A.tax_on_cd,'0'), " +
												  "TO_CHAR(A.app_date,'DD/MM/YYYY') FROM FMS7_TAX_STRUCTURE_DTL A WHERE A.tax_str_cd=? AND " +
												  "A.app_date=(SELECT MAX(B.app_date) FROM FMS7_TAX_STRUCTURE_DTL B WHERE B.tax_str_cd=? AND " +
												  "B.app_date<=TO_DATE(?,'DD/MM/YYYY')) AND A.tax_code=?";
									//////System.out.println("Query For Finding Out Tax Which Is Dependent On Other Tax Value = "+queryString1);
										stmt1 = dbcon.prepareStatement(queryString1);
										stmt1.setString(1, tax_struct_code);
										stmt1.setString(2, tax_struct_code);
									    stmt1.setString(3, bill_period_end_dt);
									    stmt1.setString(4, ""+rset.getString(4));
										rset1=stmt1.executeQuery();
								 		if(rset1.next())
								 		{
								 			if(rset1.getString(3).equals("1"))
											{
												//tax_amt = (Double.parseDouble(customer_Invoice_Gross_Amt_INR)*Double.parseDouble(rset1.getString(2)))/100;
								 				tax_amt = (gross_inr*Double.parseDouble(rset1.getString(2)))/100;
											}
										
							 			tax_amt = (tax_amt*Double.parseDouble(rset.getString(2)))/100;
								 		}
								 		else
								 		{
								 			tax_amt = 0;
								 		}
								 		rset1.close();
								 		stmt1.close();
									}
									else
									{
										tax_amt = 0;
									}
								
								//customer_Invoice_Tax_Amt.add(nf.format(Math.round(tax_amt)));
								tax_str+=nf.format(Math.round(tax_amt))+"@";
							}
							rset.close();
							stmt.close();
								tax_fact.add(tax_factor);
								customer_Invoice_Tax_Amt.add(tax_str);
						
						}
						else if(invoice_type[i].equalsIgnoreCase("SUG"))
						{
							cust_nm.add(customer_nm[i]);
							cust_city.add(customer_city[i]);
							cust_pincd.add(customer_pincd[i]);
							cust_statecd.add(customer_statecd[i]);
							cust_gstinno.add(customer_gstinno[i]);
							cust_addr.add(customer_addr[i]);
							hlpl_inv_no.add(invoice_no[i]);
							priceperqty.add(priceper_qty[i]);
							gross_amt.add(gross_amt_inr[i]);
							net_amt.add(net_amt_inr[i]);
							documentval.add(net_amt_inr[i]);
							tax_struct_code=""+tax_struct_cd[i];
							bill_period_end_dt=period_end_dt[i];
							gross_inr=Double.parseDouble(gross_amt_inr[i]);
							total_qty.add(totalqty[i]);
							flag_invoice.add("U");
							sr_no.add("1");
							item_desc.add("Actual Quantity of LNG discharged during month of "+year+"");
							contract_type.add(Vinv_cont_typ[i]);
							inv_dt_format.add(inv_dt[i]);
							doc_type.add("INV");
							hsn_sac_cd.add(sac_cd_cust[i]);
							Vsuppl_nm.add(suppl_nm[i]);
							Vsuppl_city.add(suppl_city[i]);
							Vsuppl_addr.add(suppl_addr[i]);
							Vsuppl_gstin.add(suppl_gstin[i]);
							Vsuppl_pin.add(suppl_pin[i]);
							Vsuppl_st_cd.add(suppl_st_cd[i]);
							sac_hsn_flag.add(sac_code_flag[i]);
							Vb2c.add(Vb2c_hid[i]);
							Vpayee_nm.add(Vpayee_nm_hid[i]);
							Vpayee_no.add(Vpayee_no_hid[i]);
							Vifsc.add(Vifsc_hid[i]);
							supp_cd.add(Vsupp_cd[i]);
							String tax_str="";
							tax_factor=0;
							queryString = "SELECT NVL(A.tax_code,'0'), NVL(A.factor,'0.00'), NVL(A.tax_on,'1'), NVL(A.tax_on_cd,'0'), " +
									  "TO_CHAR(A.app_date,'DD/MM/YYYY') FROM FMS7_TAX_STRUCTURE_DTL A WHERE A.tax_str_cd=? AND " +
									  "A.app_date=(SELECT MAX(B.app_date) FROM FMS7_TAX_STRUCTURE_DTL B WHERE B.tax_str_cd=? AND " +
									  "B.app_date<=TO_DATE(?,'DD/MM/YYYY')) ORDER BY A.tax_code";
							////System.out.println("STEP-1A.3:FMS7_TAX_STRUCTURE_DTL: "+queryString);
							stmt = dbcon.prepareStatement(queryString);
							stmt.setString(1, tax_struct_code);
							stmt.setString(2, tax_struct_code);
						    stmt.setString(3, bill_period_end_dt);
							rset=stmt.executeQuery();
							while(rset.next())
							{
								tax_factor+= Integer.parseInt((rset.getString(2)));
								if(rset.getString(3).equals("1"))
								{
									//tax_amt = (Double.parseDouble(customer_Invoice_Gross_Amt_INR)*Double.parseDouble(rset.getString(2)))/100;
									tax_amt = (gross_inr*Double.parseDouble(rset.getString(2)))/100;
									
								}
								else if(rset.getString(3).equals("2"))
								{
									queryString1 = "SELECT NVL(A.tax_code,'0'), NVL(A.factor,'0'), NVL(A.tax_on,'1'), NVL(A.tax_on_cd,'0'), " +
											  "TO_CHAR(A.app_date,'DD/MM/YYYY') FROM FMS7_TAX_STRUCTURE_DTL A WHERE A.tax_str_cd=? AND " +
											  "A.app_date=(SELECT MAX(B.app_date) FROM FMS7_TAX_STRUCTURE_DTL B WHERE B.tax_str_cd=? AND " +
											  "B.app_date<=TO_DATE(?,'DD/MM/YYYY')) AND A.tax_code=?";
								//////System.out.println("Query For Finding Out Tax Which Is Dependent On Other Tax Value = "+queryString1);
									stmt1 = dbcon.prepareStatement(queryString1);
									stmt1.setString(1, tax_struct_code);
									stmt1.setString(2, tax_struct_code);
								    stmt1.setString(3, bill_period_end_dt);
								    stmt1.setString(4, ""+rset.getString(4));
									rset1=stmt1.executeQuery();
							 		if(rset1.next())
							 		{
							 			if(rset1.getString(3).equals("1"))
										{
											//tax_amt = (Double.parseDouble(customer_Invoice_Gross_Amt_INR)*Double.parseDouble(rset1.getString(2)))/100;
							 				tax_amt = (gross_inr*Double.parseDouble(rset1.getString(2)))/100;
										}
									
						 			tax_amt = (tax_amt*Double.parseDouble(rset.getString(2)))/100;
							 		}
							 		else
							 		{
							 			tax_amt = 0;
							 		}
							 		rset1.close();
							 		stmt1.close();
								}
								else
								{
									tax_amt = 0;
								}
							
							//customer_Invoice_Tax_Amt.add(nf.format(Math.round(tax_amt)));
							tax_str+=nf.format(Math.round(tax_amt))+"@";
						}
						rset.close();
						stmt.close();
							tax_fact.add(tax_factor);
							customer_Invoice_Tax_Amt.add(tax_str);
						}
						else if(invoice_type[i].equalsIgnoreCase("REC"))
						{
							cust_nm.add(customer_nm[i]);
							cust_city.add(customer_city[i]);
							cust_pincd.add(customer_pincd[i]);
							cust_statecd.add(customer_statecd[i]);
							cust_gstinno.add(customer_gstinno[i]);
							cust_addr.add(customer_addr[i]);
							hlpl_inv_no.add(invoice_no[i]);
							priceperqty.add(priceper_qty[i]);
							gross_amt.add(gross_amt_inr[i]);
							net_amt.add(net_amt_inr[i]);
							documentval.add(net_amt_inr[i]);
							tax_struct_code=""+tax_struct_cd[i];
							bill_period_end_dt=period_end_dt[i];
							gross_inr=Double.parseDouble(gross_amt_inr[i]);
							total_qty.add(totalqty[i]);
							flag_invoice.add("A");
							item_desc.add(remark_specification[i]);
							contract_type.add(Vinv_cont_typ[i]);
							inv_dt_format.add(inv_dt[i]);
							doc_type.add("INV");
							sr_no.add("1");
							hsn_sac_cd.add(sac_cd_cust[i]);
							Vsuppl_nm.add(suppl_nm[i]);
							Vsuppl_city.add(suppl_city[i]);
							Vsuppl_addr.add(suppl_addr[i]);
							Vsuppl_gstin.add(suppl_gstin[i]);
							Vsuppl_pin.add(suppl_pin[i]);
							Vsuppl_st_cd.add(suppl_st_cd[i]);
							sac_hsn_flag.add(sac_code_flag[i]);
							Vb2c.add(Vb2c_hid[i]);
							Vpayee_nm.add(Vpayee_nm_hid[i]);
							Vpayee_no.add(Vpayee_no_hid[i]);
							Vifsc.add(Vifsc_hid[i]);
							supp_cd.add(Vsupp_cd[i]);
							String tax_str="";
							tax_factor=0;
							queryString = "SELECT NVL(A.tax_code,'0'), NVL(A.factor,'0.00'), NVL(A.tax_on,'1'), NVL(A.tax_on_cd,'0'), " +
									  "TO_CHAR(A.app_date,'DD/MM/YYYY') FROM FMS7_TAX_STRUCTURE_DTL A WHERE A.tax_str_cd=? AND " +
									  "A.app_date=(SELECT MAX(B.app_date) FROM FMS7_TAX_STRUCTURE_DTL B WHERE B.tax_str_cd=? AND " +
									  "B.app_date<=TO_DATE(?,'DD/MM/YYYY')) ORDER BY A.tax_code";
							////System.out.println("STEP-1A.3:FMS7_TAX_STRUCTURE_DTL: "+queryString);
							stmt = dbcon.prepareStatement(queryString);
							stmt.setString(1, tax_struct_code);
							stmt.setString(2, tax_struct_code);
						    stmt.setString(3, bill_period_end_dt);
							rset=stmt.executeQuery();
							while(rset.next())
							{
								tax_factor+= Integer.parseInt((rset.getString(2)));
								if(rset.getString(3).equals("1"))
								{
									//tax_amt = (Double.parseDouble(customer_Invoice_Gross_Amt_INR)*Double.parseDouble(rset.getString(2)))/100;
									tax_amt = (gross_inr*Double.parseDouble(rset.getString(2)))/100;
									
								}
								else if(rset.getString(3).equals("2"))
								{
									queryString1 = "SELECT NVL(A.tax_code,'0'), NVL(A.factor,'0'), NVL(A.tax_on,'1'), NVL(A.tax_on_cd,'0'), " +
											  "TO_CHAR(A.app_date,'DD/MM/YYYY') FROM FMS7_TAX_STRUCTURE_DTL A WHERE A.tax_str_cd=? AND " +
											  "A.app_date=(SELECT MAX(B.app_date) FROM FMS7_TAX_STRUCTURE_DTL B WHERE B.tax_str_cd=? AND " +
											  "B.app_date<=TO_DATE(?,'DD/MM/YYYY')) AND A.tax_code=?";
								//////System.out.println("Query For Finding Out Tax Which Is Dependent On Other Tax Value = "+queryString1);
									stmt1 = dbcon.prepareStatement(queryString1);
									stmt1.setString(1, tax_struct_code);
									stmt1.setString(2, tax_struct_code);
								    stmt1.setString(3, bill_period_end_dt);
								    stmt1.setString(4, ""+rset.getString(4));
									rset1=stmt1.executeQuery();
							 		if(rset1.next())
							 		{
							 			if(rset1.getString(3).equals("1"))
										{
											//tax_amt = (Double.parseDouble(customer_Invoice_Gross_Amt_INR)*Double.parseDouble(rset1.getString(2)))/100;
							 				tax_amt = (gross_inr*Double.parseDouble(rset1.getString(2)))/100;
										}
									
						 			tax_amt = (tax_amt*Double.parseDouble(rset.getString(2)))/100;
							 		}
							 		else
							 		{
							 			tax_amt = 0;
							 		}
							 		rset1.close();
							 		stmt1.close();
								}
								else
								{
									tax_amt = 0;
								}
							
							//customer_Invoice_Tax_Amt.add(nf.format(Math.round(tax_amt)));
							tax_str+=nf.format(Math.round(tax_amt))+"@";
						}
							rset.close();
							stmt.close();
							tax_fact.add(tax_factor);
							customer_Invoice_Tax_Amt.add(tax_str);
						}else if(invoice_type[i].equalsIgnoreCase("STORAGE")){

							cust_nm.add(customer_nm[i]);
							cust_city.add(customer_city[i]);
							cust_pincd.add(customer_pincd[i]);
							cust_statecd.add(customer_statecd[i]);
							cust_gstinno.add(customer_gstinno[i]);
							cust_addr.add(customer_addr[i]);
							hlpl_inv_no.add(invoice_no[i]);
							priceperqty.add(priceper_qty[i]);
							gross_amt.add(gross_amt_inr[i]);
							net_amt.add(net_amt_inr[i]);
							documentval.add(net_amt_inr[i]);
							tax_struct_code=""+tax_struct_cd[i];
							bill_period_end_dt=period_end_dt[i];
							gross_inr=Double.parseDouble(gross_amt_inr[i]);
							total_qty.add(totalqty[i]);
							flag_invoice.add("B");
							item_desc.add("Natural Gas (Regasified)");
							contract_type.add(Vinv_cont_typ[i]);
							inv_dt_format.add(inv_dt[i]);
							doc_type.add("INV");
							sr_no.add("1");
							hsn_sac_cd.add(sac_cd_cust[i]);
							Vsuppl_nm.add(suppl_nm[i]);
							Vsuppl_city.add(suppl_city[i]);
							Vsuppl_addr.add(suppl_addr[i]);
							Vsuppl_gstin.add(suppl_gstin[i]);
							Vsuppl_pin.add(suppl_pin[i]);
							Vsuppl_st_cd.add(suppl_st_cd[i]);
							sac_hsn_flag.add(sac_code_flag[i]);
							Vb2c.add(Vb2c_hid[i]);
							Vpayee_nm.add(Vpayee_nm_hid[i]);
							Vpayee_no.add(Vpayee_no_hid[i]);
							Vifsc.add(Vifsc_hid[i]);
							supp_cd.add(Vsupp_cd[i]);
							String tax_str="";
							tax_factor=0;
							queryString = "SELECT NVL(A.tax_code,'0'), NVL(A.factor,'0.00'), NVL(A.tax_on,'1'), NVL(A.tax_on_cd,'0'), " +
									  "TO_CHAR(A.app_date,'DD/MM/YYYY') FROM FMS7_TAX_STRUCTURE_DTL A WHERE A.tax_str_cd=? AND " +
									  "A.app_date=(SELECT MAX(B.app_date) FROM FMS7_TAX_STRUCTURE_DTL B WHERE B.tax_str_cd=? AND " +
									  "B.app_date<=TO_DATE(?,'DD/MM/YYYY')) ORDER BY A.tax_code";
							////System.out.println("STEP-1A.3:FMS7_TAX_STRUCTURE_DTL: "+queryString);
							stmt = dbcon.prepareStatement(queryString);
							stmt.setString(1, tax_struct_code);
							stmt.setString(2, tax_struct_code);
						    stmt.setString(3, bill_period_end_dt);
							rset=stmt.executeQuery();
							while(rset.next())
							{
								tax_factor+= Integer.parseInt((rset.getString(2)));
								if(rset.getString(3).equals("1"))
								{
									//tax_amt = (Double.parseDouble(customer_Invoice_Gross_Amt_INR)*Double.parseDouble(rset.getString(2)))/100;
									tax_amt = (gross_inr*Double.parseDouble(rset.getString(2)))/100;
									
								}
								else if(rset.getString(3).equals("2"))
								{
									queryString1 = "SELECT NVL(A.tax_code,'0'), NVL(A.factor,'0'), NVL(A.tax_on,'1'), NVL(A.tax_on_cd,'0'), " +
											  "TO_CHAR(A.app_date,'DD/MM/YYYY') FROM FMS7_TAX_STRUCTURE_DTL A WHERE A.tax_str_cd=? AND " +
											  "A.app_date=(SELECT MAX(B.app_date) FROM FMS7_TAX_STRUCTURE_DTL B WHERE B.tax_str_cd=? AND " +
											  "B.app_date<=TO_DATE(?,'DD/MM/YYYY')) AND A.tax_code=?";
								//////System.out.println("Query For Finding Out Tax Which Is Dependent On Other Tax Value = "+queryString1);
									stmt1 = dbcon.prepareStatement(queryString1);
									stmt1.setString(1, tax_struct_code);
									stmt1.setString(2, tax_struct_code);
								    stmt1.setString(3, bill_period_end_dt);
								    stmt1.setString(4, ""+rset.getString(4));
									rset1=stmt1.executeQuery();
							 		if(rset1.next())
							 		{
							 			if(rset1.getString(3).equals("1"))
										{
											//tax_amt = (Double.parseDouble(customer_Invoice_Gross_Amt_INR)*Double.parseDouble(rset1.getString(2)))/100;
							 				tax_amt = (gross_inr*Double.parseDouble(rset1.getString(2)))/100;
										}
									
						 			tax_amt = (tax_amt*Double.parseDouble(rset.getString(2)))/100;
							 		}
							 		else
							 		{
							 			tax_amt = 0;
							 		}
							 		rset1.close();
							 		stmt1.close();
								}
								else
								{
									tax_amt = 0;
								}
							
							//customer_Invoice_Tax_Amt.add(nf.format(Math.round(tax_amt)));
							tax_str+=nf.format(Math.round(tax_amt))+"@";
						}
							rset.close();
							stmt.close();
							tax_fact.add(tax_factor);
							customer_Invoice_Tax_Amt.add(tax_str);
						
						}
						else if(invoice_type[i].equalsIgnoreCase("CREDIT_SER") || invoice_type[i].equalsIgnoreCase("DEBIT_SER")){


							cust_nm.add(customer_nm[i]);
							cust_city.add(customer_city[i]);
							cust_pincd.add(customer_pincd[i]);
							cust_statecd.add(customer_statecd[i]);
							cust_gstinno.add(customer_gstinno[i]);
							cust_addr.add(customer_addr[i]);
							hlpl_inv_no.add(invoice_no[i]);
							priceperqty.add(priceper_qty[i]);
							gross_amt.add(gross_amt_inr[i]);
							//net_amt.add(net_amt_inr[i]);
							tax_struct_code=""+tax_struct_cd[i];
							bill_period_end_dt=period_end_dt[i];
							gross_inr=Double.parseDouble(gross_amt_inr[i]);
							total_qty.add(totalqty[i]);
							flag_invoice.add("CRDR");
							item_desc.add("Natural Gas (Regasified)");
							sr_no.add("1");
							sac_hsn_flag.add(sac_code_flag[i]);
							contract_type.add(Vinv_cont_typ[i]);
							inv_dt_format.add(inv_dt[i]);
							hsn_sac_cd.add(sac_cd_cust[i]);
							if(invoice_type[i].equalsIgnoreCase("DEBIT_SER")){
							doc_type.add("DBN");
							}else{
								doc_type.add("CRN");
							}
							Vsuppl_nm.add(suppl_nm[i]);
							Vsuppl_city.add(suppl_city[i]);
							Vsuppl_addr.add(suppl_addr[i]);
							Vsuppl_gstin.add(suppl_gstin[i]);
							Vsuppl_pin.add(suppl_pin[i]);
							Vsuppl_st_cd.add(suppl_st_cd[i]);
							Vb2c.add(Vb2c_hid[i]);
							Vpayee_nm.add(Vpayee_nm_hid[i]);
							Vpayee_no.add(Vpayee_no_hid[i]);
							Vifsc.add(Vifsc_hid[i]);
							supp_cd.add(Vsupp_cd[i]);
							String tax_str="";
							tax_factor=0;
							double taxtot=0;
							queryString = "SELECT NVL(A.tax_code,'0'), NVL(A.factor,'0.00'), NVL(A.tax_on,'1'), NVL(A.tax_on_cd,'0'), " +
									  "TO_CHAR(A.app_date,'DD/MM/YYYY') FROM FMS7_TAX_STRUCTURE_DTL A WHERE A.tax_str_cd=? AND " +
									  "A.app_date=(SELECT MAX(B.app_date) FROM FMS7_TAX_STRUCTURE_DTL B WHERE B.tax_str_cd=? AND " +
									  "B.app_date<=TO_DATE(?,'DD/MM/YYYY')) ORDER BY A.tax_code";
							////System.out.println("STEP-1A.3:FMS7_TAX_STRUCTURE_DTL: "+queryString);
							stmt = dbcon.prepareStatement(queryString);
							stmt.setString(1, tax_struct_code);
							stmt.setString(2, tax_struct_code);
						    stmt.setString(3, bill_period_end_dt);
							rset=stmt.executeQuery();
							while(rset.next())
							{
								tax_factor+= Integer.parseInt((rset.getString(2)));
								if(rset.getString(3).equals("1"))
								{
									//tax_amt = (Double.parseDouble(customer_Invoice_Gross_Amt_INR)*Double.parseDouble(rset.getString(2)))/100;
									tax_amt = (gross_inr*Double.parseDouble(rset.getString(2)))/100;
									
								}
								else if(rset.getString(3).equals("2"))
								{
									queryString1 = "SELECT NVL(A.tax_code,'0'), NVL(A.factor,'0'), NVL(A.tax_on,'1'), NVL(A.tax_on_cd,'0'), " +
											  "TO_CHAR(A.app_date,'DD/MM/YYYY') FROM FMS7_TAX_STRUCTURE_DTL A WHERE A.tax_str_cd=? AND " +
											  "A.app_date=(SELECT MAX(B.app_date) FROM FMS7_TAX_STRUCTURE_DTL B WHERE B.tax_str_cd=? AND " +
											  "B.app_date<=TO_DATE(?,'DD/MM/YYYY')) AND A.tax_code=?";
								//////System.out.println("Query For Finding Out Tax Which Is Dependent On Other Tax Value = "+queryString1);
									stmt1 = dbcon.prepareStatement(queryString1);
									stmt1.setString(1, tax_struct_code);
									stmt1.setString(2, tax_struct_code);
								    stmt1.setString(3, bill_period_end_dt);
								    stmt1.setString(4, ""+rset.getString(4));
									rset1=stmt1.executeQuery();
							 		if(rset1.next())
							 		{
							 			if(rset1.getString(3).equals("1"))
										{
											//tax_amt = (Double.parseDouble(customer_Invoice_Gross_Amt_INR)*Double.parseDouble(rset1.getString(2)))/100;
							 				tax_amt = (gross_inr*Double.parseDouble(rset1.getString(2)))/100;
										}
									
						 			tax_amt = (tax_amt*Double.parseDouble(rset.getString(2)))/100;
							 		}
							 		else
							 		{
							 			tax_amt = 0;
							 		}
							 		rset1.close();
							 		stmt1.close();
								}
								else
								{
									tax_amt = 0;
								}
							
							//customer_Invoice_Tax_Amt.add(nf.format(Math.round(tax_amt)));
							tax_str+=nf.format(Math.round(tax_amt))+"@";
							taxtot+=Math.round(tax_amt);
							//net_amt.add(net_amt_inr[i]);
						}
						rset.close();
						stmt.close();
							tax_fact.add(tax_factor);
							customer_Invoice_Tax_Amt.add(tax_str);
							net_amt.add(nf.format(Math.round(gross_inr+taxtot)));
							documentval.add(nf.format(Math.round(gross_inr+taxtot)));
						
						
						}
						else if(invoice_type[i].equalsIgnoreCase("CREDIT") || invoice_type[i].equalsIgnoreCase("DEBIT")){

							cust_nm.add(customer_nm[i]);
							cust_city.add(customer_city[i]);
							cust_pincd.add(customer_pincd[i]);
							cust_statecd.add(customer_statecd[i]);
							cust_gstinno.add(customer_gstinno[i]);
							cust_addr.add(customer_addr[i]);
							hlpl_inv_no.add(invoice_no[i]);
							priceperqty.add(priceper_qty[i]);
							gross_amt.add(gross_amt_inr[i]);
							//net_amt.add(net_amt_inr[i]);
							tax_struct_code=""+tax_struct_cd[i];
							bill_period_end_dt=period_end_dt[i];
							gross_inr=Double.parseDouble(gross_amt_inr[i]);
							total_qty.add(totalqty[i]);
							flag_invoice.add("CRDR");
							item_desc.add("Natural Gas (Regasified)");
							sr_no.add("1");
							sac_hsn_flag.add(sac_code_flag[i]);
							contract_type.add(Vinv_cont_typ[i]);
							inv_dt_format.add(inv_dt[i]);
							hsn_sac_cd.add(sac_cd_cust[i]);
							if(invoice_type[i].equalsIgnoreCase("DEBIT")){
							doc_type.add("DBN");
							}else{
								doc_type.add("CRN");
							}
							Vsuppl_nm.add(suppl_nm[i]);
							Vsuppl_city.add(suppl_city[i]);
							Vsuppl_addr.add(suppl_addr[i]);
							Vsuppl_gstin.add(suppl_gstin[i]);
							Vsuppl_pin.add(suppl_pin[i]);
							Vsuppl_st_cd.add(suppl_st_cd[i]);
							Vb2c.add(Vb2c_hid[i]);
							Vpayee_nm.add(Vpayee_nm_hid[i]);
							Vpayee_no.add(Vpayee_no_hid[i]);
							Vifsc.add(Vifsc_hid[i]);
							supp_cd.add(Vsupp_cd[i]);
							String tax_str="";
							tax_factor=0;
							double taxtot=0;
							queryString = "SELECT NVL(A.tax_code,'0'), NVL(A.factor,'0.00'), NVL(A.tax_on,'1'), NVL(A.tax_on_cd,'0'), " +
									  "TO_CHAR(A.app_date,'DD/MM/YYYY') FROM FMS7_TAX_STRUCTURE_DTL A WHERE A.tax_str_cd=? AND " +
									  "A.app_date=(SELECT MAX(B.app_date) FROM FMS7_TAX_STRUCTURE_DTL B WHERE B.tax_str_cd=? AND " +
									  "B.app_date<=TO_DATE(?,'DD/MM/YYYY')) ORDER BY A.tax_code";
							////System.out.println("STEP-1A.3:FMS7_TAX_STRUCTURE_DTL: "+queryString);
							stmt = dbcon.prepareStatement(queryString);
							stmt.setString(1, tax_struct_code);
							stmt.setString(2, tax_struct_code);
						    stmt.setString(3, bill_period_end_dt);
							rset=stmt.executeQuery();
							while(rset.next())
							{
								tax_factor+= Integer.parseInt((rset.getString(2)));
								if(rset.getString(3).equals("1"))
								{
									//tax_amt = (Double.parseDouble(customer_Invoice_Gross_Amt_INR)*Double.parseDouble(rset.getString(2)))/100;
									tax_amt = (gross_inr*Double.parseDouble(rset.getString(2)))/100;
									
								}
								else if(rset.getString(3).equals("2"))
								{
									queryString1 = "SELECT NVL(A.tax_code,'0'), NVL(A.factor,'0'), NVL(A.tax_on,'1'), NVL(A.tax_on_cd,'0'), " +
											  "TO_CHAR(A.app_date,'DD/MM/YYYY') FROM FMS7_TAX_STRUCTURE_DTL A WHERE A.tax_str_cd=? AND " +
											  "A.app_date=(SELECT MAX(B.app_date) FROM FMS7_TAX_STRUCTURE_DTL B WHERE B.tax_str_cd=? AND " +
											  "B.app_date<=TO_DATE(?,'DD/MM/YYYY')) AND A.tax_code=?";
								//////System.out.println("Query For Finding Out Tax Which Is Dependent On Other Tax Value = "+queryString1);
									stmt1 = dbcon.prepareStatement(queryString1);
									stmt1.setString(1, tax_struct_code);
									stmt1.setString(2, tax_struct_code);
								    stmt1.setString(3, bill_period_end_dt);
								    stmt1.setString(4, ""+rset.getString(4));
									rset1=stmt1.executeQuery();
							 		if(rset1.next())
							 		{
							 			if(rset1.getString(3).equals("1"))
										{
											//tax_amt = (Double.parseDouble(customer_Invoice_Gross_Amt_INR)*Double.parseDouble(rset1.getString(2)))/100;
							 				tax_amt = (gross_inr*Double.parseDouble(rset1.getString(2)))/100;
										}
									
						 			tax_amt = (tax_amt*Double.parseDouble(rset.getString(2)))/100;
							 		}
							 		else
							 		{
							 			tax_amt = 0;
							 		}
							 		rset1.close();
							 		stmt1.close();
								}
								else
								{
									tax_amt = 0;
								}
							
							//customer_Invoice_Tax_Amt.add(nf.format(Math.round(tax_amt)));
							tax_str+=nf.format(Math.round(tax_amt))+"@";
							taxtot+=Math.round(tax_amt);
							//net_amt.add(net_amt_inr[i]);
						}
						rset.close();
						stmt.close();
							tax_fact.add(tax_factor);
							customer_Invoice_Tax_Amt.add(tax_str);
							net_amt.add(nf.format(Math.round(gross_inr+taxtot)));
							documentval.add(nf.format(Math.round(gross_inr+taxtot)));
						
						}else if(invoice_type[i].equalsIgnoreCase("OTHER")){
							String cont_type=Vinv_cont_typ[i];
							if(cont_type.equals("Z")){
								String temp[]=priceper_qty[i].split("@");
								String temp_gross[]=gross_amt_inr[i].split("@");
								String net[]=net_amt_inr[i].split("@");
								String qty[]=totalqty[i].split("@");
								String sac[]=sac_cd_cust[i].split("@"); 
								String taxcd[]=tax_struct_cd[i].split("@");
								String desc[]=item_descr[i].split("@");
								String docvalue[]=docval[i].split("@");
								String tax[]=remark_specification[i].split("%%");
								//System.out.println("temp.length--"+temp.length);
								for(int k=0;k<temp.length;k++){
									//System.out.println("temp.length--"+sac[k]);	
									cust_nm.add(customer_nm[i]);
									cust_city.add(customer_city[i]);
									cust_pincd.add(customer_pincd[i]);
									cust_statecd.add(customer_statecd[i]);
									cust_gstinno.add(customer_gstinno[i]);
									cust_addr.add(customer_addr[i]);
									hlpl_inv_no.add(invoice_no[i]);
									//tax_struct_code=""+tax_struct_cd[i];
									bill_period_end_dt=period_end_dt[i];
									//gross_inr=Double.parseDouble(gross_amt_inr[i]);
									flag_invoice.add("O");
									item_desc.add(desc[k]);
									contract_type.add(Vinv_cont_typ[i]);
									inv_dt_format.add(inv_dt[i]);
									sac_hsn_flag.add(sac_code_flag[i]);
									doc_type.add("INV");
									sr_no.add(k+1);
									Vsuppl_nm.add(suppl_nm[i]);
									Vsuppl_city.add(suppl_city[i]);
									Vsuppl_addr.add(suppl_addr[i]);
									Vsuppl_gstin.add(suppl_gstin[i]);
									Vsuppl_pin.add(suppl_pin[i]);
									Vsuppl_st_cd.add(suppl_st_cd[i]);
									priceperqty.add(temp[k]);
									gross_amt.add(temp_gross[k]);
									net_amt.add(net[k]);
									documentval.add(docvalue[k]);
									total_qty.add(qty[k]);
									hsn_sac_cd.add(sac[k]);
									tax_fact.add(taxcd[k]);
									customer_Invoice_Tax_Amt.add(tax[k]);
									Vb2c.add(Vb2c_hid[i]);
									Vpayee_nm.add(Vpayee_nm_hid[i]);
									Vpayee_no.add(Vpayee_no_hid[i]);
									Vifsc.add(Vifsc_hid[i]);
									supp_cd.add(Vsupp_cd[i]);
								}
							}else{
								
								cust_nm.add(customer_nm[i]);
								cust_city.add(customer_city[i]);
								cust_pincd.add(customer_pincd[i]);
								cust_statecd.add(customer_statecd[i]);
								cust_gstinno.add(customer_gstinno[i]);
								cust_addr.add(customer_addr[i]);
								hlpl_inv_no.add(invoice_no[i]);
								priceperqty.add(priceper_qty[i]);
								gross_amt.add(gross_amt_inr[i]);
								net_amt.add(net_amt_inr[i]);
								documentval.add(net_amt_inr[i]);
								//tax_struct_code=""+tax_struct_cd[i];
								bill_period_end_dt=period_end_dt[i];
								gross_inr=Double.parseDouble(gross_amt_inr[i]);
								total_qty.add(totalqty[i]);
								flag_invoice.add("O");
								//if(cont_type.equals("2")){
									item_desc.add(item_descr[i]);
								//}
								/*else{
									item_desc.add("Natural Gas (Regasified)");
								}*/
								sac_hsn_flag.add(sac_code_flag[i]);
								contract_type.add(Vinv_cont_typ[i]);
								inv_dt_format.add(inv_dt[i]);
								doc_type.add("INV");
								sr_no.add("1");
								hsn_sac_cd.add(sac_cd_cust[i]);
								tax_fact.add(tax_struct_cd[i]);
								customer_Invoice_Tax_Amt.add(remark_specification[i]);
								Vsuppl_nm.add(suppl_nm[i]);
								Vsuppl_city.add(suppl_city[i]);
								Vsuppl_addr.add(suppl_addr[i]);
								Vsuppl_gstin.add(suppl_gstin[i]);
								Vsuppl_pin.add(suppl_pin[i]);
								Vsuppl_st_cd.add(suppl_st_cd[i]);
								Vb2c.add(Vb2c_hid[i]); //MCL 20210907 enabled for B2C testing
								//Vb2c.add(""); //MCL 20210907 disabled for B2C testing
								Vpayee_nm.add(Vpayee_nm_hid[i]);
								Vpayee_no.add(Vpayee_no_hid[i]);
								Vifsc.add(Vifsc_hid[i]);
								supp_cd.add(Vsupp_cd[i]);
							}
						}
						else if(invoice_type[i].equalsIgnoreCase("CRDROTH")){

							String cont_type=Vinv_cont_typ[i];
							if(cont_type.equals("Z")){
								String temp[]=priceper_qty[i].split("@");
								String temp_gross[]=gross_amt_inr[i].split("@");
								String net[]=net_amt_inr[i].split("@");
								String qty[]=totalqty[i].split("@");
								String sac[]=sac_cd_cust[i].split("@"); 
								String taxcd[]=tax_struct_cd[i].split("@");
								String desc[]=item_descr[i].split("@");
								String docvalue[]=docval[i].split("@");
								String tax[]=remark_specification[i].split("%%");
								//System.out.println("temp.length--"+temp.length);
								for(int k=0;k<temp.length;k++){
									System.out.println("temp.length--"+sac[k]);	
									cust_nm.add(customer_nm[i]);
									cust_city.add(customer_city[i]);
									cust_pincd.add(customer_pincd[i]);
									cust_statecd.add(customer_statecd[i]);
									cust_gstinno.add(customer_gstinno[i]);
									cust_addr.add(customer_addr[i]);
									hlpl_inv_no.add(invoice_no[i]);
									//tax_struct_code=""+tax_struct_cd[i];
									bill_period_end_dt=period_end_dt[i];
									//gross_inr=Double.parseDouble(gross_amt_inr[i]);
									flag_invoice.add("OTHCRDR");
									item_desc.add(desc[k]);
									contract_type.add(Vinv_cont_typ[i]);
									inv_dt_format.add(inv_dt[i]);
									sac_hsn_flag.add(sac_code_flag[i]);
									if(invoice_type[i].equalsIgnoreCase("DEBIT")){
										doc_type.add("DBN");
									}else{
										doc_type.add("CRN");
									}
									sr_no.add(k+1);
									Vsuppl_nm.add(suppl_nm[i]);
									Vsuppl_city.add(suppl_city[i]);
									Vsuppl_addr.add(suppl_addr[i]);
									Vsuppl_gstin.add(suppl_gstin[i]);
									Vsuppl_pin.add(suppl_pin[i]);
									Vsuppl_st_cd.add(suppl_st_cd[i]);
									priceperqty.add(temp[k]);
									gross_amt.add(temp_gross[k]);
									net_amt.add(net[k]);
									documentval.add(docvalue[k]);
									total_qty.add(qty[k]);
									hsn_sac_cd.add(sac[k]);
									tax_fact.add(taxcd[k]);
									customer_Invoice_Tax_Amt.add(tax[k]);
									Vb2c.add(Vb2c_hid[i]);
									Vpayee_nm.add(Vpayee_nm_hid[i]);
									Vpayee_no.add(Vpayee_no_hid[i]);
									Vifsc.add(Vifsc_hid[i]);
									supp_cd.add(Vsupp_cd[i]);
								}
							}else{
								
								cust_nm.add(customer_nm[i]);
								cust_city.add(customer_city[i]);
								cust_pincd.add(customer_pincd[i]);
								cust_statecd.add(customer_statecd[i]);
								cust_gstinno.add(customer_gstinno[i]);
								cust_addr.add(customer_addr[i]);
								hlpl_inv_no.add(invoice_no[i]);
								priceperqty.add(priceper_qty[i]);
								gross_amt.add(gross_amt_inr[i]);
								net_amt.add(net_amt_inr[i]);
								System.out.println("net_amt in this "+net_amt);
								documentval.add(net_amt_inr[i]);
								//tax_struct_code=""+tax_struct_cd[i];
								bill_period_end_dt=period_end_dt[i];
								gross_inr=Double.parseDouble(gross_amt_inr[i]);
								total_qty.add(totalqty[i]);
								flag_invoice.add("OTHCRDR");
								//if(cont_type.equals("2")){
									item_desc.add(item_descr[i]);
								//}
								/*else{
									item_desc.add("Natural Gas (Regasified)");
								}*/
								sac_hsn_flag.add(sac_code_flag[i]);
								contract_type.add(Vinv_cont_typ[i]);
								inv_dt_format.add(inv_dt[i]);
								if(invoice_no[i].startsWith("D")){
									doc_type.add("DBN");
								}else{
									doc_type.add("CRN");
								}
								sr_no.add("1");
								hsn_sac_cd.add(sac_cd_cust[i]);
								tax_fact.add(tax_struct_cd[i]);
								customer_Invoice_Tax_Amt.add(remark_specification[i]);
								Vsuppl_nm.add(suppl_nm[i]);
								Vsuppl_city.add(suppl_city[i]);
								Vsuppl_addr.add(suppl_addr[i]);
								Vsuppl_gstin.add(suppl_gstin[i]);
								Vsuppl_pin.add(suppl_pin[i]);
								Vsuppl_st_cd.add(suppl_st_cd[i]);
								Vb2c.add(Vb2c_hid[i]);
								Vpayee_nm.add(Vpayee_nm_hid[i]);
								Vpayee_no.add(Vpayee_no_hid[i]);
								Vifsc.add(Vifsc_hid[i]);
								supp_cd.add(Vsupp_cd[i]);
							}
						}
					}
				}
			} 
			//String file_nm="INVOICE-"+month+"-"+year+"-"+systime+".xlsx";
			System.out.println("tax_fact"+tax_fact);
			int rt=prepare_excel(request,response,hlpl_inv_no,Vsuppl_nm,Vsuppl_city,Vsuppl_gstin,Vsuppl_pin,supplier_sac_cd,supplier_sac_desc,Vsuppl_st_cd,Vsuppl_addr,cust_addr,cust_city,cust_nm,cust_pincd,cust_statecd,cust_gstinno,inv_dt_format,net_amt,total_qty,contract_type,month,year,tax_fact,customer_Invoice_Tax_Amt,priceperqty,gross_amt,flag_invoice,item_desc,doc_type,systime,hsn_sac_cd,sr_no,sac_hsn_flag,documentval,Vb2c,Vpayee_nm,Vpayee_no,Vifsc,supp_cd);
			String msg="";
			if(rt==1){
				String file_nm="INVOICE-"+month+"-"+year+"-"+systime+"";
				msg = "Excel Sucessfully Generated !!";
				queryString = "INSERT INTO FMS7_INV_EXCEL_UPLOAD_DTL (EXCEL_FILE_NM,UPLOAD_FLAG,ENT_BY,ENT_DT) "
    					+ " VALUES (?,'Y',?,TO_DATE(SYSDATE,'DD/MM/YYYY'))";
	       // System.out.println("Account_Approval : "+queryString);	        
				stmt = dbcon.prepareStatement(queryString);
				stmt.setString(1, file_nm);
				stmt.setString(2, user_cd);
				stmt.executeUpdate();
				stmt.close();
			}else{
				msg = "Excel Not Generated !!";
			}
			dbcon.commit();
			try
			{
				//new com.hlpl.hazira.fms7.util.InfoLogger().writelog("["+session.getAttribute("username")+"/"+session.getAttribute("ip")+"]: "+form_id+"@"+form_nm+"~: "+msg);
			}
			catch(Exception infoLogger)
			{
				////System.out.println("Exception While Writing into InfoLogger Servlet From "+servletName+" Servlet -->\nUnder "+methodName+" Method -->\n"+infoLogger.getMessage());
				// SagarB20240801
		    	// infoLogger.printStackTrace(); 
		    	LOGGER.log(Level.WARNING, "context:", infoLogger); 
			}
			url = "../sales_invoice/frm_inv_excel_uploading.jsp?msg=" + msg+"&month="+month+"&year="+year+"&inv_type="+inv_type+"&systime="+systime+"&write_permission="+write_permission+"&delete_permission="+delete_permission+"&print_permission="+print_permission+"&approve_permission="+approve_permission+"&audit_permission="+audit_permission+"&check_permission="+check_permission+"&authorize_permission="+authorize_permission;
		} catch (Exception e) {
			// TODO: handle exception
			String msg = "Excel Not Generated !!";
			dbcon.rollback();
            url = "../sales_invoice/frm_inv_excel_uploading.jsp?msg=" + msg+"&month="+month+"&year="+year+"&inv_type="+inv_type;
			LOGGER.log(Level.WARNING, "context:", e); //PP20240708
		}
	 }
	 
	 public int prepare_excel(HttpServletRequest request,HttpServletResponse response,Vector hlpl_inv_no,Vector supplier_nm,Vector supplier_city,Vector supplier_gstinno,Vector supplier_pincd,String supplier_sac_cd,String supplier_sac_desc,Vector supplier_statecd,Vector supplier_addr,Vector cust_addr,Vector customer_city,Vector customer_nm,Vector customer_pincd,Vector customer_statecd,Vector customer_gstinno,Vector inv_dt,Vector net_amt,Vector total_qty,Vector contract_type,String month,String year,Vector tax_factor,Vector customer_Invoice_Tax_Amt,Vector priceperqty,Vector gross_inr,Vector flag_invoice,Vector item_desc,Vector doc_type,String systime,Vector hsn_sac_cd,Vector sr_no,Vector sac_hsn_flag,Vector docval,Vector Vb2c,Vector Vpayee_nm,Vector Vpayee_no,Vector Vifsc,Vector supp_cd) {
	     int rt=0;   
		 try {
	        	//System.out.println("--supp--"+supplier_gstinno);
	        	HttpSession sess = request.getSession();
				String pdf_path = request.getRealPath("/pdf_reports/xls_reports"); //RG20191218 changed for invoice filtering
				File file_pdf_path = new File(pdf_path);
				
				String ORI_pdf_path=request.getRealPath("");//HM20240518
				Path ORI_To_pdf_path=new File(ORI_pdf_path).toPath().normalize(); //HM20240518
				
				String return_pre=""+(month+year);
				String fileName="",sheetnm="";
				fileName ="INVOICE-"+month+"-"+year+"-"+systime+".xlsx";
				sheetnm="INVOICE-"+month+"-"+year+"-"+systime+"";
				Vector tax_amt=new Vector();
				
			//SB20240521	pdf_path = pdf_path+"//"+fileName;
				Path To_pdf_path=new File(pdf_path).toPath().normalize(); //HM20240518
				if(!To_pdf_path.startsWith(ORI_To_pdf_path))
				{ //SB20240509
					  throw new IOException("Entry is outside of the target directory!");//SB20240509
				  
				}
				 else //SB20240509
				{//SB20240509
					 pdf_path = To_pdf_path+File.separator+fileName; //SB20240521//HM20240522
				XSSFWorkbook workbook = new XSSFWorkbook();
	            XSSFSheet sheet = workbook.createSheet(sheetnm); 
	            Row rowhead = sheet.createRow((short)0);
	            int cellno=0;
	            rowhead.createCell((short)cellno++).setCellValue("LocationGstin");
	            rowhead.createCell((short)cellno++).setCellValue("LocationName");
	            rowhead.createCell((short)cellno++).setCellValue("ReturnPeriod");
	            rowhead.createCell((short)cellno++).setCellValue("LiabilityDischargeReturnPeriod");
	            rowhead.createCell((short)cellno++).setCellValue("ITCClaimReturnPeriod");
	            rowhead.createCell((short)cellno++).setCellValue("Purpose");
	            rowhead.createCell((short)cellno++).setCellValue("AutoPushOrGenerate");
	            rowhead.createCell((short)cellno++).setCellValue("SupplyType");
	            rowhead.createCell((short)cellno++).setCellValue("Irn");
	            rowhead.createCell((short)cellno++).setCellValue("DocumentType");
	            rowhead.createCell((short)cellno++).setCellValue("TransactionType");
	            rowhead.createCell((short)cellno++).setCellValue("TransactionNature");
	            rowhead.createCell((short)cellno++).setCellValue("TransactionTypeDescription");
	            rowhead.createCell((short)cellno++).setCellValue("TaxpayerType");
	            rowhead.createCell((short)cellno++).setCellValue("DocumentNumber");
	            rowhead.createCell((short)cellno++).setCellValue("DocumentSeriesCode");
	            rowhead.createCell((short)cellno++).setCellValue("DocumentDate");
	            //rowhead.createCell((short)cellno++).setCellValue("ReferenceDocumentNumber");
	            //rowhead.createCell((short)cellno++).setCellValue("ReferenceDocumentDate");
	            //rowhead.createCell((short)cellno++).setCellValue("TransactionMode");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromGstin");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromLegalName");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromTradeName");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromVendorCode");
//	            rowhead.createCell((short)cellno++).setCellValue("BillFromBuildingNumber");
//	            rowhead.createCell((short)cellno++).setCellValue("BillFromBuildingName");
//	            rowhead.createCell((short)cellno++).setCellValue("BillFromFlatNumber");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromAddress1");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromAddress2");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromCity");
	            //rowhead.createCell((short)cellno++).setCellValue("BillFromDistrict");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromStateCode");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromPincode");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromPhone");
	            rowhead.createCell((short)cellno++).setCellValue("BillFromEmail");
	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromGstin");
	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromTradeName");
	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromVendorCode");
//	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromBuildingNumber");
//	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromBuildingName");
//	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromFlatNumber");
	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromAddress1");
	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromAddress2");
	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromCity");
//	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromDistrict");
	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromStateCode");
	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromPincode");
//	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromPhone");
//	            rowhead.createCell((short)cellno++).setCellValue("DispatchFromEmail");
	            rowhead.createCell((short)cellno++).setCellValue("BillToGstin");
	            rowhead.createCell((short)cellno++).setCellValue("BillToLegalName");
	            rowhead.createCell((short)cellno++).setCellValue("BillToTradeName");
	            rowhead.createCell((short)cellno++).setCellValue("BillToVendorCode");
//	            rowhead.createCell((short)cellno++).setCellValue("BillToBuildingNumber");
//	            rowhead.createCell((short)cellno++).setCellValue("BillToBuildingName");
//	            rowhead.createCell((short)cellno++).setCellValue("BillToFlatNumber");
	            rowhead.createCell((short)cellno++).setCellValue("BillToAddress1");
	            rowhead.createCell((short)cellno++).setCellValue("BillToAddress2");
	            rowhead.createCell((short)cellno++).setCellValue("BillToCity");
//	            rowhead.createCell((short)cellno++).setCellValue("BillToDistrict");
	            rowhead.createCell((short)cellno++).setCellValue("BillToStateCode");
	            rowhead.createCell((short)cellno++).setCellValue("BillToPincode");
	            rowhead.createCell((short)cellno++).setCellValue("BillToPhone");
	            rowhead.createCell((short)cellno++).setCellValue("BillToEmail");
	            rowhead.createCell((short)cellno++).setCellValue("ShipToGstin");
	            rowhead.createCell((short)cellno++).setCellValue("ShipToLegalName");
	            rowhead.createCell((short)cellno++).setCellValue("ShipToTradeName");
	            rowhead.createCell((short)cellno++).setCellValue("ShipToVendorCode");
//	            rowhead.createCell((short)cellno++).setCellValue("ShipToBuildingNumber");
//	            rowhead.createCell((short)cellno++).setCellValue("ShipToBuildingName");
//	            rowhead.createCell((short)cellno++).setCellValue("ShipToFlatNumber");
	            rowhead.createCell((short)cellno++).setCellValue("ShipToAddress1");
	            rowhead.createCell((short)cellno++).setCellValue("ShipToAddress2");
	            rowhead.createCell((short)cellno++).setCellValue("ShipToCity");
//	            rowhead.createCell((short)cellno++).setCellValue("ShipToDistrict");
	            rowhead.createCell((short)cellno++).setCellValue("ShipToStateCode");
	            rowhead.createCell((short)cellno++).setCellValue("ShipToPincode");
//	            rowhead.createCell((short)cellno++).setCellValue("ShipToPhone");
//	            rowhead.createCell((short)cellno++).setCellValue("ShipToEmail");
	            rowhead.createCell((short)cellno++).setCellValue("PaymentType");
	            rowhead.createCell((short)cellno++).setCellValue("PaymentMode");
	            rowhead.createCell((short)cellno++).setCellValue("PaymentAmount");
	            rowhead.createCell((short)cellno++).setCellValue("AdvancePaidAmount");
	            rowhead.createCell((short)cellno++).setCellValue("PaymentDate");
	            rowhead.createCell((short)cellno++).setCellValue("PaymentRemarks");
	            rowhead.createCell((short)cellno++).setCellValue("PaymentTerms");
	            rowhead.createCell((short)cellno++).setCellValue("PaymentInstruction");
	            rowhead.createCell((short)cellno++).setCellValue("PayeeName");
	            rowhead.createCell((short)cellno++).setCellValue("PayeeAccountNumber");
	            rowhead.createCell((short)cellno++).setCellValue("PaymentAmountDue");
//	            rowhead.createCell((short)cellno++).setCellValue("PaymentDueDate");
	            rowhead.createCell((short)cellno++).setCellValue("Ifsc");
//	            rowhead.createCell((short)cellno++).setCellValue("AccountDetails");
	            rowhead.createCell((short)cellno++).setCellValue("CreditTransfer");
	            rowhead.createCell((short)cellno++).setCellValue("DirectDebit");
	            rowhead.createCell((short)cellno++).setCellValue("CreditDays");
	            rowhead.createCell((short)cellno++).setCellValue("CreditAvailedDate");
	            rowhead.createCell((short)cellno++).setCellValue("CreditReversalDate");
	            rowhead.createCell((short)cellno++).setCellValue("RefDocumentRemarks");
	            rowhead.createCell((short)cellno++).setCellValue("RefDocumentPeriodStartDate");
	            rowhead.createCell((short)cellno++).setCellValue("RefDocumentPeriodEndDate");
	            rowhead.createCell((short)cellno++).setCellValue("RefPrecedingDocumentDetails");
	            rowhead.createCell((short)cellno++).setCellValue("RefContractDetails");
	            rowhead.createCell((short)cellno++).setCellValue("AdditionalSupportingDocumentDetails");
//	            rowhead.createCell((short)cellno++).setCellValue("ExternalReference");
//	            rowhead.createCell((short)cellno++).setCellValue("ProjectReferenceNumber");
//	            rowhead.createCell((short)cellno++).setCellValue("POReferenceNumber");
	            rowhead.createCell((short)cellno++).setCellValue("BillNumber");
	            rowhead.createCell((short)cellno++).setCellValue("BillDate");
	            rowhead.createCell((short)cellno++).setCellValue("PortCode");
	            rowhead.createCell((short)cellno++).setCellValue("DocumentCurrencyCode");
//	            rowhead.createCell((short)cellno++).setCellValue("DocumentCurrencyValue");
	            rowhead.createCell((short)cellno++).setCellValue("DestinationCountry");
	            rowhead.createCell((short)cellno++).setCellValue("ExportDuty");
	            rowhead.createCell((short)cellno++).setCellValue("Pos");
	            rowhead.createCell((short)cellno++).setCellValue("DocumentValue");
	            rowhead.createCell((short)cellno++).setCellValue("DocumentValueInForeignCurrency");
	            rowhead.createCell((short)cellno++).setCellValue("RoundOffAmount");
	            rowhead.createCell((short)cellno++).setCellValue("DifferentialPercentage");
	            rowhead.createCell((short)cellno++).setCellValue("ReverseCharge");
	            rowhead.createCell((short)cellno++).setCellValue("ClaimRefund");
	            rowhead.createCell((short)cellno++).setCellValue("UnderIgstAct");
	            rowhead.createCell((short)cellno++).setCellValue("RefundEligibility");
	            rowhead.createCell((short)cellno++).setCellValue("ECommerceGstin");
	            rowhead.createCell((short)cellno++).setCellValue("TDSGSTIN");
	            rowhead.createCell((short)cellno++).setCellValue("PnrOrUniqueNumber");
	            rowhead.createCell((short)cellno++).setCellValue("AvailProvisionalItc");
	            rowhead.createCell((short)cellno++).setCellValue("OriginalGstin");
	            rowhead.createCell((short)cellno++).setCellValue("OriginalStateCode");
	            rowhead.createCell((short)cellno++).setCellValue("OriginalTradeName");
	            
	            rowhead.createCell((short)cellno++).setCellValue("OriginalDocumentType");
	            rowhead.createCell((short)cellno++).setCellValue("OriginalDocumentNumber");
	            rowhead.createCell((short)cellno++).setCellValue("OriginalDocumentDate");
	            rowhead.createCell((short)cellno++).setCellValue("OriginalReturnPeriod");
	            rowhead.createCell((short)cellno++).setCellValue("OriginalAmountDeducted");
	            rowhead.createCell((short)cellno++).setCellValue("OriginalPortCode");
	            rowhead.createCell((short)cellno++).setCellValue("TransportDateTime");
	            rowhead.createCell((short)cellno++).setCellValue("TransporterId");
	            rowhead.createCell((short)cellno++).setCellValue("TransporterName");
//	            rowhead.createCell((short)cellno++).setCellValue("TransportDate");
//	            rowhead.createCell((short)cellno++).setCellValue("TransportTime");
//	            rowhead.createCell((short)cellno++).setCellValue("DistanceLevel");
	            rowhead.createCell((short)cellno++).setCellValue("TransportMode");
	            rowhead.createCell((short)cellno++).setCellValue("Distance");
	            rowhead.createCell((short)cellno++).setCellValue("TransportDocumentNumber");
	            rowhead.createCell((short)cellno++).setCellValue("TransportDocumentDate");
	            rowhead.createCell((short)cellno++).setCellValue("VehicleNumber");
	            rowhead.createCell((short)cellno++).setCellValue("VehicleType");
	            rowhead.createCell((short)cellno++).setCellValue("ToEmailAddresses");
	            rowhead.createCell((short)cellno++).setCellValue("ToMobileNumbers");
	            rowhead.createCell((short)cellno++).setCellValue("JWOriginalDocumentNumber");
	            rowhead.createCell((short)cellno++).setCellValue("JWOriginalDocumentDate");
	            rowhead.createCell((short)cellno++).setCellValue("JWDocumentNumber");
	            rowhead.createCell((short)cellno++).setCellValue("JWDocumentDate");
	            rowhead.createCell((short)cellno++).setCellValue("Custom1");
	            rowhead.createCell((short)cellno++).setCellValue("Custom2");
	            rowhead.createCell((short)cellno++).setCellValue("Custom3");
	            rowhead.createCell((short)cellno++).setCellValue("Custom4");
	            rowhead.createCell((short)cellno++).setCellValue("Custom5");
	            rowhead.createCell((short)cellno++).setCellValue("Custom6");
	            rowhead.createCell((short)cellno++).setCellValue("Custom7");
	            rowhead.createCell((short)cellno++).setCellValue("Custom8");
	            rowhead.createCell((short)cellno++).setCellValue("Custom9");
	            rowhead.createCell((short)cellno++).setCellValue("Custom10");
	            rowhead.createCell((short)cellno++).setCellValue("SerialNumber");
	            rowhead.createCell((short)cellno++).setCellValue("IsService");
	            rowhead.createCell((short)cellno++).setCellValue("Hsn");
	            rowhead.createCell((short)cellno++).setCellValue("ProductCode");
	            rowhead.createCell((short)cellno++).setCellValue("ItemName");
	            rowhead.createCell((short)cellno++).setCellValue("ItemDescription");
	            rowhead.createCell((short)cellno++).setCellValue("NatureOfJWDone");
	            rowhead.createCell((short)cellno++).setCellValue("Barcode");
	            rowhead.createCell((short)cellno++).setCellValue("Uqc");
	            rowhead.createCell((short)cellno++).setCellValue("Quantity");
	            rowhead.createCell((short)cellno++).setCellValue("FreeQuantity");
	            rowhead.createCell((short)cellno++).setCellValue("LossUnitOfMeasure");
	            rowhead.createCell((short)cellno++).setCellValue("LossTotalQuantity");
	            rowhead.createCell((short)cellno++).setCellValue("Rate");
	            rowhead.createCell((short)cellno++).setCellValue("CessRate");
	            rowhead.createCell((short)cellno++).setCellValue("StateCessRate");
	            rowhead.createCell((short)cellno++).setCellValue("CessNonAdvaloremRate");
	            rowhead.createCell((short)cellno++).setCellValue("PricePerQuantity");
	            rowhead.createCell((short)cellno++).setCellValue("DiscountAmount");
	            rowhead.createCell((short)cellno++).setCellValue("GrossAmount");
	            rowhead.createCell((short)cellno++).setCellValue("OtherCharges");
	            rowhead.createCell((short)cellno++).setCellValue("TaxableValue");
	            rowhead.createCell((short)cellno++).setCellValue("PreTaxValue");
	            //rowhead.createCell((short)cellno++).setCellValue("AssessableValue");
	            rowhead.createCell((short)cellno++).setCellValue("IgstAmount");
	            rowhead.createCell((short)cellno++).setCellValue("CgstAmount");
	            rowhead.createCell((short)cellno++).setCellValue("SgstAmount");
	            rowhead.createCell((short)cellno++).setCellValue("CessAmount");
	            rowhead.createCell((short)cellno++).setCellValue("StateCessAmount");
	            rowhead.createCell((short)cellno++).setCellValue("StateCessNonAdvaloremAmount");
	            rowhead.createCell((short)cellno++).setCellValue("CessNonAdvaloremAmount");
	            rowhead.createCell((short)cellno++).setCellValue("OrderLineReference");
	            rowhead.createCell((short)cellno++).setCellValue("OriginCountry");
	            rowhead.createCell((short)cellno++).setCellValue("ItemTotal");
	            rowhead.createCell((short)cellno++).setCellValue("ItemAttributeDetails");
	            rowhead.createCell((short)cellno++).setCellValue("TaxType");
	            rowhead.createCell((short)cellno++).setCellValue("BatchNameNumber");
	            rowhead.createCell((short)cellno++).setCellValue("BatchExpiryDate");
	            rowhead.createCell((short)cellno++).setCellValue("WarrantyDate");
	            rowhead.createCell((short)cellno++).setCellValue("ItcEligibility");
	            rowhead.createCell((short)cellno++).setCellValue("ItcIgstAmount");
	            rowhead.createCell((short)cellno++).setCellValue("ItcCgstAmount");
	            rowhead.createCell((short)cellno++).setCellValue("ItcSgstAmount");
	            rowhead.createCell((short)cellno++).setCellValue("ItcCessAmount");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem1");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem2");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem3");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem4");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem5");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem6");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem7");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem8");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem9");
	            rowhead.createCell((short)cellno++).setCellValue("CustomItem10");
	            if(Vb2c.contains("Y")){
	            	rowhead.createCell((short)cellno++).setCellValue("UpiId");
	            }
	            //rowhead.createCell((short)cellno++).setCellValue("EInvoicePushStatus");
	            int row_no=0;
	            //System.out.println("hlpl_inv--"+hlpl_inv_no.size());
	            for(int i=0;i<hlpl_inv_no.size();i++){
	            	row_no=i+1;
		            Row row = sheet.createRow((short)row_no);
		            int cellno1=0;
		            //For tax amount
		            tax_amt.clear();
		            //System.out.println("customer_Invoice_Tax_Amt.elementAt(i)--"+customer_Invoice_Tax_Amt.elementAt(i));
		            if(!customer_Invoice_Tax_Amt.elementAt(i).equals("")){
		            	String temp_tax="";
		            	if(contract_type.elementAt(i).equals("Z") || contract_type.elementAt(i).equals("V")){
		            		temp_tax=customer_Invoice_Tax_Amt.elementAt(i).toString();
		            	}else{
		            		//System.out.println("customer_Invoice_Tax_Amt.elementAt(i)--"+customer_Invoice_Tax_Amt.elementAt(i));
		            		temp_tax=customer_Invoice_Tax_Amt.elementAt(i).toString().substring(0,customer_Invoice_Tax_Amt.elementAt(i).toString().length()-1);
		            	}
		            	
		            	String temp[]=temp_tax.split("@");
		            	for(int k=0;k<temp.length;k++){
		            		tax_amt.add(temp[k]);
		            	}
		            }
		            //
		            row.createCell((short)cellno1++).setCellValue(""+supplier_gstinno.elementAt(i));//supplier gstin no
		            row.createCell((short)cellno1++).setCellValue(""+supplier_nm.elementAt(i));//supplier nm
		            row.createCell((short)cellno1++).setCellValue(return_pre);//month and year
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("EINV");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("S");//supply type S for sales and p for purchase
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue(""+doc_type.elementAt(i));
		            if(Vb2c.elementAt(i).equals("Y")){
		            	row.createCell((short)cellno1++).setCellValue("B2C");
		            }else{
		            	row.createCell((short)cellno1++).setCellValue("B2B");
		            }
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue(""+hlpl_inv_no.elementAt(i));//invoice num	            //row.createCell((short)11).setCellValue(no);//invoice num
		            row.createCell((short)cellno1++).setCellValue("");//invoice num
		            row.createCell((short)cellno1++).setCellValue(""+inv_dt.elementAt(i));//invoice date
	//	            row.createCell((short)cellno1++).setCellValue("");
	//	            row.createCell((short)cellno1++).setCellValue("");
		            //row.createCell((short)cellno1++).setCellValue("REG");
		            row.createCell((short)cellno1++).setCellValue(""+supplier_gstinno.elementAt(i));//supplier gstin no
		            row.createCell((short)cellno1++).setCellValue(""+supplier_nm.elementAt(i));//supplier nm
		            row.createCell((short)cellno1++).setCellValue("");//supplier nm
	//	            row.createCell((short)cellno1++).setCellValue("");
	//	            row.createCell((short)cellno1++).setCellValue("");
	//	            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue(""+supplier_addr.elementAt(i));
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue(""+supplier_city.elementAt(i));//supplier city
		            //row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue(""+supplier_statecd.elementAt(i)); //supplier state code
		            row.createCell((short)cellno1++).setCellValue(""+supplier_pincd.elementAt(i));//supplier pin code
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
	//	            row.createCell((short)cellno1++).setCellValue("");
	//	            row.createCell((short)cellno1++).setCellValue("");
	//	            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            //row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
	//	            row.createCell((short)cellno1++).setCellValue("");
	//	            row.createCell((short)cellno1++).setCellValue("");
		            if(Vb2c.elementAt(i).equals("Y")){
		            	row.createCell((short)cellno1++).setCellValue(""); //cust_gstin_no
		            }else{
		            	row.createCell((short)cellno1++).setCellValue(""+customer_gstinno.elementAt(i)); //cust_gstin_no
		            }
		            row.createCell((short)cellno1++).setCellValue(""+customer_nm.elementAt(i)); //cust_nm
		            row.createCell((short)cellno1++).setCellValue(""); //cust_nm
		            row.createCell((short)cellno1++).setCellValue(""); //cust_nm
		            row.createCell((short)cellno1++).setCellValue(""+cust_addr.elementAt(i));
	//	            row.createCell((short)cellno1++).setCellValue("");
	//	            row.createCell((short)cellno1++).setCellValue("");
	//	            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue(""+customer_city.elementAt(i));//cust city
		            //row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue(""+customer_statecd.elementAt(i));//cust state code
		            row.createCell((short)cellno1++).setCellValue(""+customer_pincd.elementAt(i));//cust pin code
		            row.createCell((short)cellno1++).setCellValue("");//cust phone num
		            row.createCell((short)cellno1++).setCellValue("");//cust email
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
	//	            row.createCell((short)cellno1++).setCellValue("");
	//	            row.createCell((short)cellno1++).setCellValue("");
	//	            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
	//	            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
	//	            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            if(Vb2c.elementAt(i).equals("Y")){
		            	row.createCell((short)cellno1++).setCellValue(""+Vpayee_nm.elementAt(i));
			            row.createCell((short)cellno1++).setCellValue(""+Vpayee_no.elementAt(i));
			            row.createCell((short)cellno1++).setCellValue(""+docval.elementAt(i));
			            //row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue(""+Vifsc.elementAt(i));
		            }else{
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            //row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            }
		            //row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
	//	            row.createCell((short)cellno1++).setCellValue("");
	//	            row.createCell((short)cellno1++).setCellValue("");
	//	            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            if(contract_type.elementAt(i).equals("1")){
		            	row.createCell((short)cellno1++).setCellValue("USD");
		            }else{
		            	if(Vb2c.elementAt(i).equals("Y")){
		            		row.createCell((short)cellno1++).setCellValue("INR");
		            	}else{
		            		row.createCell((short)cellno1++).setCellValue("");
		            	}
		            	
		            }
		            //row.createCell((short)cellno1++).setCellValue("");
		            //row.createCell((short)cellno1++).setCellValue("");
		            if(contract_type.elementAt(i).equals("1")){
		            	row.createCell((short)cellno1++).setCellValue("IN");
		            }else{
		            	if(Vb2c.elementAt(i).equals("Y")){
		            		row.createCell((short)cellno1++).setCellValue("IN");
		            	}else{
		            	row.createCell((short)cellno1++).setCellValue("");
		            	}
		            }
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue(""+customer_statecd.elementAt(i));//POS //Enabled by MCL 20210415
		            //row.createCell((short)cellno1++).setCellValue(""+supplier_statecd.elementAt(i));//POS //Disabled by MCL 20210415
		            if(contract_type.elementAt(i).equals("Z") || contract_type.elementAt(i).equals("V")){
		            	row.createCell((short)cellno1++).setCellValue(""+docval.elementAt(i)); //Net amount inr
		            }else{
		            	row.createCell((short)cellno1++).setCellValue(""+net_amt.elementAt(i)); //Net amount inr
		            }
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("N");
		            row.createCell((short)cellno1++).setCellValue("N");
		           
		            if(tax_amt.size()==1){
		            	 //row.createCell((short)cellno1++).setCellValue("Y"); //Commented by MCL 20210415
		            	 row.createCell((short)cellno1++).setCellValue("N"); //Added by MCL 20210415 this should be N  for SEIPL
		            }else{
		            	 row.createCell((short)cellno1++).setCellValue("N");
		            }
		            row.createCell((short)cellno1++).setCellValue("N");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
	//	            row.createCell((short)cellno1++).setCellValue("");
	//	            row.createCell((short)cellno1++).setCellValue("");
	//	            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");//cust email address 
		            row.createCell((short)cellno1++).setCellValue("");//cust phone num
		            row.createCell((short)cellno1++).setCellValue("");//cust phone num
		            row.createCell((short)cellno1++).setCellValue("");//cust phone num
		            row.createCell((short)cellno1++).setCellValue("");//cust phone num
		            row.createCell((short)cellno1++).setCellValue("");//cust phone num
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue(""+sr_no.elementAt(i));
		            if(sac_hsn_flag.elementAt(i).equals("P")){
		            	row.createCell((short)cellno1++).setCellValue("N");
		            }else{
		            	row.createCell((short)cellno1++).setCellValue("Y");
		            }
		            
		           // row.createCell((short)cellno1++).setCellValue(supplier_sac_cd);//sac_no
		            row.createCell((short)cellno1++).setCellValue(""+hsn_sac_cd.elementAt(i));//sac_no
		            row.createCell((short)cellno1++).setCellValue("");//sac_no
		            
		            if(contract_type.elementAt(i).equals("Z") || contract_type.elementAt(i).equals("V")){
		            	row.createCell((short)cellno1++).setCellValue(""+item_desc.elementAt(i));//sac_description
		            }else{
		            	row.createCell((short)cellno1++).setCellValue(supplier_sac_desc);//sac_description
		            }
		            row.createCell((short)cellno1++).setCellValue(""+item_desc.elementAt(i));
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("OTH");
		            row.createCell((short)cellno1++).setCellValue(""+total_qty.elementAt(i));//Inv quanityt
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue(""+tax_factor.elementAt(i));
		            /*row.createCell((short)cellno1++).setCellValue("0.00");
		            row.createCell((short)cellno1++).setCellValue("0.00");
		            row.createCell((short)cellno1++).setCellValue("0.00");*/
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue(""+priceperqty.elementAt(i));
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue(""+gross_inr.elementAt(i));
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue(""+gross_inr.elementAt(i));//tax amount inr not rounded off
		            row.createCell((short)cellno1++).setCellValue("");//tax amount inr not rounded off
		            if(tax_amt.size()==1){
		            	row.createCell((short)cellno1++).setCellValue(Double.parseDouble(""+tax_amt.elementAt(0)));//tax comp
		            	row.createCell((short)cellno1++).setCellValue("");
			            row.createCell((short)cellno1++).setCellValue("");
		            }else if(tax_amt.size()>1){
		            	row.createCell((short)cellno1++).setCellValue("");//tax comp
		            	row.createCell((short)cellno1++).setCellValue(Double.parseDouble(""+tax_amt.elementAt(0)));
			            row.createCell((short)cellno1++).setCellValue(Double.parseDouble(""+tax_amt.elementAt(1)));
		            }
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue(""+net_amt.elementAt(i));//net amount
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            row.createCell((short)cellno1++).setCellValue("");
		            if(Vb2c.elementAt(i).equals("Y")){
		            	if(supp_cd.elementAt(i).equals("2")){
		            		row.createCell((short)cellno1++).setCellValue("haziraport@sc");
		            	}else{
		            		row.createCell((short)cellno1++).setCellValue("seipl@sc");
		            	}
		            	
		            }
	            }
	            
	          //HM20240522
	            
	           // System.out.println("in this11 ");
//	            FileOutputStream fileOut = new FileOutputStream(pdf_path);
//	            workbook.write(fileOut);
//	            fileOut.close();
//	           // workbook.close();
//	            System.out.println("Your excel file has been generated!");
	            
	            //HM20240522
	            try (FileOutputStream fileOut = new FileOutputStream(file_pdf_path))
	            {
	            	workbook.write(fileOut);
	 	            fileOut.close();
	 	           // workbook.close();
	 	            System.out.println("Your excel file has been generated!");
	            } 
	            catch (FileNotFoundException e) 
	            {
	            	// Handle file not found exception (e.g., log error, notify user)
	            	LOGGER.log(Level.WARNING, "context:", e); //PP20240708
	            } 
	            catch (IOException e)
	            {
	            	// Handle other I/O exceptions (e.g., disk errors, permissions issues)
	            	LOGGER.log(Level.WARNING, "context:", e); //PP20240708
	            }
	            rt=1;
				}
	        } catch ( Exception ex ) 
	        {
	            System.out.println(ex);
				// SagarB20240801
		    	// ex.printStackTrace(); 
		    	LOGGER.log(Level.WARNING, "context:", ex); 
	            rt=0;
	        }
		 
		 return rt;
	    }

	 
}//End Of Class Frm_Excel_Uploading ...

