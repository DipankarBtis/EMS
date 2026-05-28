package com.etrm.fms.developer_tool;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.escapeSingleQuotes;

@WebServlet("/servlet/Frm_DeveloperTool")
@MultipartConfig(fileSizeThreshold=1024*1024*20, 	// 20 MB 
maxFileSize=1024*1024*50,      	// 50 MB
maxRequestSize=1024*1024*100)   	// 100 MB
public class Frm_DeveloperTool extends HttpServlet
{
	static String db_src_file_name="Frm_DeveloperTool.java";

	public static  Connection dbcon;
	
	public static String servletName = "Frm_DeveloperTool";
	public static String option = "";
	public static String url = "";
	public static String msg = "";
	public static String msg_type = "";
	public static String err_msg = "";
	
	private static String queryString = null;
	private static String query = null;
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
	
	public static String old_value="";
	public static String new_value="";
	
	public static String emp_cd="";
	public static String comp_cd="";
	public static String comp_abbr="";
	public static String emp_nm="";
	public static String ip="";
	
	public static String commonUrl_pra="";
	
	public static escapeSingleQuotes escObj = new escapeSingleQuotes();
	
	static UtilBean utilBean = new UtilBean();
	
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
				if(Context == null) 
				{
					throw new Exception("Boom - No Context");
				}
				
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
					
					emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
					comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
					comp_abbr = (String)session.getAttribute("comp_abbr")==null?"":(String)session.getAttribute("comp_abbr");
					emp_nm = (String)session.getAttribute("emp_nm")==null?"":(String)session.getAttribute("emp_nm");
					ip = (String)session.getAttribute("ip")==null?"":(String)session.getAttribute("ip");
					
					new_value="";
					old_value="";
					
					option=request.getParameter("option")==null?"":request.getParameter("option");
					
					commonUrl_pra = "&u="+u;
					
					if(option.equalsIgnoreCase("FILE_UPLOAD"))
					{
						FileUpload(request, response);
					}
					else if(option.equalsIgnoreCase("DATA_DISPOSAL"))
					{
						InsertUpdateDataDisposalData(request, response);
					}
					else if(option.equalsIgnoreCase("ARCHIVE_TBL"))
					{
						CreateArchiveTable(request, response);
					}
					else if(option.equalsIgnoreCase("DLETEL_ARCHIVED_TBL"))
					{
						DeleteArchivedTable(request, response);
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
		} catch(IOException e) {
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}
	
	public void FileUpload(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		String function_nm="FileUpload()";

		msg="";
		msg_type="";
		url="";
		
		try
		{
			String file_name="";
			String submit_type = request.getParameter("submit_type")==null?"":request.getParameter("submit_type");
			String file_path = request.getParameter("file_path")==null?"":request.getParameter("file_path");
			String new_folder_nm = request.getParameter("new_folder_nm")==null?"":request.getParameter("new_folder_nm");
			String path = request.getParameter("path")==null?"":request.getParameter("path");
			
			String sysdt_time = "";
			query="SELECT TO_CHAR(SYSDATE,'DD/MM/YYYY HH24:MI:SS') FROM DUAL";
			PreparedStatement stmt=dbcon.prepareStatement(query);
			ResultSet rset=stmt.executeQuery();
			if(rset.next())
			{
				sysdt_time=rset.getString(1)==null?"":rset.getString(1);
			}
			rset.close();
			stmt.close();
			if(!sysdt_time.equals(""))
			{
				String[] temp = sysdt_time.split(" ");
				sysdt_time = temp[0].replaceAll("/", "")+"_"+temp[1].replaceAll(":", ""); 
			}
			
			if(!file_path.equals(""))
			{
				String realPath = request.getRealPath("");
				
		        File fileSaveDir = new File(file_path);
		        
		        String canonicalPath_fileSaveDir = fileSaveDir.getCanonicalPath();
		        
		        if (fileSaveDir.exists()) 
		        {
		        	if(!canonicalPath_fileSaveDir.startsWith(realPath))
			        {
			        	throw new IOException("Entry is outside of the target directory!");
			        }
			        else if(submit_type.equals("U"))
		        	{
			        	String fileName="";
				        for (Part part : request.getParts()) 
				        {
				        	fileName = extractFileName(part);
				        	// refines the fileName in case it is an absolute path
						    fileName = new File(fileName).getName();
						    if(!fileName.equals("") )
						    {
						    	File original_file = new File(file_path +File.separator+ fileName);
						    	if(original_file.exists())
						    	{
						    		File rename_file = new File(file_path +File.separator+ fileName+"_bkp"+sysdt_time);
						    		boolean isCopied = original_file.renameTo(rename_file);
						    		if(isCopied)
						    		{
						    			file_name=fileName;
								    	part.write(file_path +File.separator+ fileName);
								    	msg="Successful! - File Uploaded Successfully!!";
								    	msg_type="S";
						    		}
						    		else
						    		{
						    			msg="Failed! - Existing File Copied Failed!!";
								    	msg_type="E";
						    		}
						    	}
						    	else
						    	{
						    		file_name=fileName;
							    	part.write(file_path +File.separator+ fileName);
							    	msg="Successful! - File Uploaded Successfully!!";
							    	msg_type="S";
						    	}
						    } 
				        }
		        	}
		        	else if(submit_type.equals("N"))
		        	{
		        		String targetDirectory = file_path+File.separator+new_folder_nm;
			            Path targetPath = new File(targetDirectory).toPath().normalize();
			            
		        		//File new_dir = new File(file_path+File.separator+new_folder_nm);
			            File new_dir = new File(""+targetPath);
		        		//String canonicalPath_new_dir = new_dir.getCanonicalPath();
		        		
		        		//if(!realPath.startsWith(canonicalPath_new_dir))
		        		if(!new_dir.toPath().normalize().startsWith(targetPath))
		        		{
				        	throw new IOException("Entry is outside of the target directory!");
				        }
				        else if(!new_dir.exists())
		        		{
		        			boolean new_fol = new_dir.mkdir();
		        			
		        			if(new_fol)
		        			{
		        				msg="Successful! - Directory created successfully!";
		        				msg_type="S";
		        		    }
		        			else
		        			{
		        		        msg="Failed! - Sorry could not create specified directory!";
		        		        msg_type="E";
		        			}
		        		}
		        		else
		        		{
		        			msg="Failed! - Directory already Exists!";
	        				msg_type="E";
		        		}
		        	}	
		        }
		        else
		        {
		        	msg="Failed! - File Path not Accessible!!";
		        	msg_type="E";
		        }
			}
			else
			{
				msg="Failed! - File Path not Accessible!!";
				msg_type="E";
			}
			
			url = "../developer_tool/frm_developer_tool.jsp?msg="+msg+"&msg_type="+msg_type+"&path="+path+commonUrl_pra;
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			msg="Error in Exception!";
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
	
	private String extractFileName(Part part) 
    {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        //System.out.println("items*******"+items.length);
        String filenm = "";
        for (String s : items) 
        {
            if (s.trim().startsWith("filename") || s.trim().startsWith("meet_file")) 
            {
           	//System.out.println("s*****"+s);
                filenm = s.substring(s.indexOf("=") + 2, s.length()-1);
            }
            
        }
        return filenm;
    }

	public void InsertUpdateDataDisposalData(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		String function_nm="InsertUpdateDataDisposalData()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			String[] tbl_nm = request.getParameterValues("tbl_nm");
			String[] archive_flag = request.getParameterValues("archive_flag");
			String[] archival_logic = request.getParameterValues("archival_logic");
			
			if(tbl_nm!=null)
			{
				for(int i=0; i<tbl_nm.length; i++)
				{
					int count=0;
					query="SELECT COUNT(*) "
							+ "FROM FMS_DATA_DISPOSAL "
							+ "WHERE DB_TABLE=? ";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, tbl_nm[i]);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						count=rset.getInt(1);
					}
					stmt.close();
					rset.close();
					
					if(count > 0)
					{
						query="UPDATE FMS_DATA_DISPOSAL SET ARCHIVE_LOGIC=?, ARCHIVE_DATA=? "
								+ "WHERE DB_TABLE=?";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, archival_logic[i]);
						stmt.setString(2, archive_flag[i]);
						stmt.setString(3, tbl_nm[i]);
						stmt.executeUpdate();
						stmt.close();
					}
					else
					{
						query="INSERT INTO FMS_DATA_DISPOSAL(DB_TABLE,ARCHIVE_LOGIC,ARCHIVE_DATA) "
								+ "VALUES(?,?,?)";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, tbl_nm[i]);
						stmt.setString(2, archival_logic[i]);
						stmt.setString(3, archive_flag[i]);
						stmt.executeUpdate();
						stmt.close();
					}
				}
			}
			
			msg="Successful! - Data Submission Successfully!";
	    	msg_type="S";
	    	
	    	url = "../developer_tool/frm_dev_tool_table.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
	    	
	    	dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			msg="Error in Exception!";
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
	
	public void CreateArchiveTable(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		String function_nm="CreateArchiveTable()";

		msg="";
		msg_type="";
		url="";
		
		try
		{
			String archive_tbl_nm = request.getParameter("archive_tbl_nm")==null?"":request.getParameter("archive_tbl_nm");
			String retention_dt = request.getParameter("retention_dt")==null?"":request.getParameter("retention_dt");
			String archiveLogic = request.getParameter("archiveLogic")==null?"":request.getParameter("archiveLogic");
			
			if(!archive_tbl_nm.equals("") && !retention_dt.equals("") && !archiveLogic.equals(""))
			{
				query="CREATE TABLE ARC_"+archive_tbl_nm+" AS SELECT * FROM "+archive_tbl_nm+" "
						+ "WHERE "+archiveLogic+" < TO_DATE('"+retention_dt+"','DD/MM/YYYY')";
				stmt=dbcon.prepareStatement(query);
				//stmt.setString(1, retention_dt);
				stmt.executeUpdate();
				
				query="DELETE "+archive_tbl_nm+" "
						+ "WHERE "+archiveLogic+" < TO_DATE(?,'DD/MM/YYYY')";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, retention_dt);
				stmt.executeUpdate();
				
				msg="Successful! - Archive Table(ARC_"+archive_tbl_nm+") Credated Successfully!";
		    	msg_type="S";
			}
			else
			{
				msg="Successful! - Archive Table(ARC_"+archive_tbl_nm+") Credated Failed!";
		    	msg_type="E";
			}
			
	    	url = "../developer_tool/frm_dev_tool_table.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
	    	
	    	dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			msg="Error in Exception!";
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
	
	public void DeleteArchivedTable(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		String function_nm="DeleteArchivedTable()";

		msg="";
		msg_type="";
		url="";
		
		try
		{
			String archived_tbl_nm = request.getParameter("archived_tbl_nm")==null?"":request.getParameter("archived_tbl_nm");
					
			if(!archived_tbl_nm.equals(""))
			{
				query="DROP  TABLE "+archived_tbl_nm+" ";
				stmt=dbcon.prepareStatement(query);
				stmt.executeUpdate();
				
				msg="Successful! - Archived Table("+archived_tbl_nm+") Deleted Successfully!";
		    	msg_type="S";
			}
			else
			{
				msg="Successful! - Archive Table("+archived_tbl_nm+") Deletion Failed!";
		    	msg_type="E";
			}
			
	    	url = "../developer_tool/frm_dev_tool_table.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;
	    	
	    	dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			msg="Error in Exception!";
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
}
