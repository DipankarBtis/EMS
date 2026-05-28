package automation;

import java.io.File;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.XmlUtilBean;

public class Auto_ZemaPricingUpload 
{
	public static void main(String[] args) 
	{
		uploadZemaPrice uzp = new uploadZemaPrice();
		uzp.init();
	}
}

class uploadZemaPrice 
{
	String db_src_file_name="Auto_ZemaPricingUpload.java";
	Connection conn;
	PreparedStatement stmt,stmt1,stmt2,stmt3,stmt_tmp1;
	ResultSet rset,rset1,rset2,rset3,rset_tmpl;
	//String queryString="",queryString1="";
	String msg = "";
	String to_date="";
	String context = "";
	String zema_filepath = ""; 
	String copy_zema_filepath = "";
	
	String emp_cd="0";//DEFAULT 0
	
	UtilBean utilBean = new UtilBean();
	DateUtil utilDate = new DateUtil();
	Auto_MailDelivery mail = new Auto_MailDelivery();
	XmlUtilBean xmlUtil = new XmlUtilBean();
	
	String ip = "";
	
	public void init()
	{ 
		String function_nm="init()";
        try
        {
        	conn=new Auto_DB_Connection().db_conn();
        	if(conn != null)
            {
        		context=utilBean.getAutomationKeyDetail(conn, "ENV_CONTEXT");
                zema_filepath=utilBean.getAutomationKeyDetail(conn, "ZEMA_SRC_PATH");
                copy_zema_filepath=utilBean.getAutomationKeyDetail(conn, "ZEMA_DEST_PATH");
    			
                ip = ""+InetAddress.getLocalHost().getHostAddress();
                
                Upload_XML_Data();
                
                conn.close();
            }
        }
        catch(Exception e)
        {
        	new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
        }
        finally
		{
			if(rset != null){try {rset.close();}catch(SQLException e){System.out.println("rset is not close " + e);}}
			if(rset1 != null){try {rset1.close();}catch(SQLException e){System.out.println("rset1 is not close " + e);}}
			if(rset2 != null){try {rset2.close();}catch(SQLException e){System.out.println("rset2 is not close " + e);}}
			if(rset3 != null){try {rset3.close();}catch(SQLException e){System.out.println("rset3 is not close " + e);}}
			if(rset_tmpl != null){try {rset_tmpl.close();}catch(SQLException e){System.out.println("rset_tmpl is not close " + e);}}
			if(stmt != null){try {stmt.close();}catch(SQLException e){System.out.println("stmt is not close " + e);}}
			if(stmt1 != null){try {stmt1.close();}catch(SQLException e){System.out.println("stmt1 is not close " + e);}}
			if(stmt2 != null){try {stmt2.close();}catch(SQLException e){System.out.println("stmt2 is not close " + e);}}
			if(stmt3 != null){try {stmt3.close();}catch(SQLException e){System.out.println("stmt3 is not close " + e);}}
			if(stmt_tmp1 != null){try {stmt_tmp1.close();}catch(SQLException e){System.out.println("stmt_tmp1 is not close " + e);}}
			if(conn != null){try {conn.close();}catch(SQLException e){System.out.println("conn is not close " + e);}}
		}
    }
	
	public void Upload_XML_Data() throws Exception
	{
		String function_nm="Upload_XML_Data()";
		System.out.println("\n<<<<<<<<<<<<<<<<<<ZEMA XML FILE READ>>>>>>>>>>>>>>>>>>>>>>>>>>");
		msg="";
		String from_dt="";
		try
		{
			Timestamp ts = new Timestamp(System.currentTimeMillis());
	        String tsString = ts.toString().substring(0, 19);
	        String tsDate = tsString.substring(0, 10);
	        String time = tsString.substring(11, 19);
	        
	        String systime_str="OLD_";
	        if(!tsDate.equals("") && !time.equals(""))
	        {
	        	String split[] = tsDate.split("-");
	        	systime_str+=split[0]+""+split[1]+""+split[2];
	        	systime_str+="_";
	        	systime_str+=""+time.replaceAll(":", "");
	        }
	        
			from_dt=utilDate.getPreviousDate();
			
			String SplitedDate = "";
			if(!from_dt.equals(""))
			{
				String split[] = from_dt.split("/");
				SplitedDate = split[2]+split[1]+split[0];
			}
			
			to_date = from_dt;
			
			String created_file_name = "Shell_India_XML_Batch_PROD_"+SplitedDate;
			String original_file_name = "";
			
			/*File directoryPath = new File(zema_filepath);
			File fileslist[] = directoryPath.listFiles();
			System.out.println("XML File List");
			for(int i=0; i<fileslist.length; i++)
			{
				String name=fileslist[i].getName().toLowerCase();
				long fsize=fileslist[i].getName().length();
				System.out.println(i+" "+name+"----"+fsize);
	
				if(name.endsWith(".xml"))
				{
					if(created_file_name.toUpperCase().equals(fileslist[i].getName().toString().toUpperCase().substring(0,created_file_name.length())))
					{
						String check = created_file_name+".xml";
						if(fileslist[i].getName().length() > check.length())
						{
							long filesizeinbyte = fileslist[i].length();
							long filesizeiKB = filesizeinbyte / 1024;
							System.out.println("Original File Size In KB : "+filesizeiKB);
							if(filesizeiKB > 0)
							{
								original_file_name = fileslist[i].getName();
							}
						}
					}
				}
			}
			*/

			Path directoryPath = Paths.get(zema_filepath);
			AtomicReference<String> originalFileNameRef = new AtomicReference<>();
			
			try (Stream<Path> files = Files.list(directoryPath)) 
			{
				files.filter(Files::isRegularFile)
					.filter(path -> path.toString().toLowerCase().endsWith(".xml"))
					.filter(path -> {
							String fileName = path.getFileName().toString();
							return fileName.toUpperCase().startsWith(created_file_name.toUpperCase()) && fileName.length() > (created_file_name + ".xml").length();
							})

					.sorted(Comparator.comparing((Path path) -> path.getFileName().toString().toLowerCase()))
					.forEach(path -> {
						try 
						{
							long fileSizeInKB = Files.size(path) / 1024;
							System.out.println("File: " + path.getFileName() + " | Size: " + fileSizeInKB + " KB");

							if (fileSizeInKB > 0) 
							{
								originalFileNameRef.set(path.getFileName().toString());
							}
						} 
						catch (Exception e) 
						{
							new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
						}
					});
			}
			original_file_name = originalFileNameRef.get();
			
			
			System.out.println("Created File Name : "+created_file_name);
			System.out.println("Original_File_Name : "+original_file_name);
			
			if(!original_file_name.equals(""))
			{
				File checkFileExists = new File(zema_filepath+original_file_name);
				if(checkFileExists.exists())
				{
					System.out.println("File is in folder");
					
					File createNewFile = new File(copy_zema_filepath+created_file_name+".xml");
					if(!createNewFile.exists())
					{
						Path temp = Files.copy(Paths.get(zema_filepath+original_file_name),Paths.get(copy_zema_filepath+created_file_name+".xml"));
						System.out.println("File Created : "+temp);
					}
					else
					{
						boolean flag = createNewFile.renameTo(new File(copy_zema_filepath+created_file_name+""+systime_str+".xml"));
						System.out.println("File Already Exists and Backup File : "+created_file_name+""+systime_str+".xml : "+flag);
						
						Path temp = Files.copy(Paths.get(zema_filepath+original_file_name),Paths.get(copy_zema_filepath+created_file_name+".xml"));
						System.out.println("File Created : "+temp);
					}
				}
				else
				{
					System.out.println("File is not in folder");
				}
				
				File checkCreatedFile = new File(copy_zema_filepath+created_file_name+".xml");
				if(checkCreatedFile.exists())
				{
					System.out.println("Created file is in folder");
					File fXmlFile = new File(copy_zema_filepath+created_file_name+".xml");
				    DocumentBuilderFactory dbFactory = xmlUtil.dcoumentBuilderFactory();
				    
				    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				    Document doc = dBuilder.parse(fXmlFile);
				   
				    doc.getDocumentElement().normalize();
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
				            Report_Date = eElement.getAttribute("opr_date");
				            
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
											VACTUAL_CURVE_TYPE.add(getActualCurveName(Curve_Name));
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
					
					int count_fwd=0;
					int count_spot=0;
					
					for(int i=0 ;i<VREPORT_DT.size(); i++)
					{	
						String ReportDate="";
				    	if(!VREPORT_DT.elementAt(i).equals(""))
				    	{
				    		String split_reportDt[] = VREPORT_DT.elementAt(i).toString().split("-");
					    	ReportDate = split_reportDt[2]+"/"+split_reportDt[1]+"/"+split_reportDt[0];
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
							String queryString = "SELECT COUNT(*) "
									+ "FROM FMS_FORWARD_PRICE_DTL "
									+ "WHERE CURVE_DT = TO_DATE(?,'DD/MM/YYYY') "
									+ "AND CURVE_NM = ? "
									+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY')";		
							stmt=conn.prepareStatement(queryString);
							stmt.setString(1, ""+VCURVE_DT.elementAt(i));
							stmt.setString(2, ""+VCURVE_NM.elementAt(i));
							stmt.setString(3, ReportDate);
							rset=stmt.executeQuery();
							if(rset.next())
							{
								if(rset.getInt(1) > 0) 
								{
								}
								else if(!CurveValue.equals("")) //JAYASRI20230922
								{
									String queryString1 = "INSERT INTO FMS_FORWARD_PRICE_DTL(REPORT_DT,CURVE_DT,CURVE_NM,COMMODITY_TYPE,CURVE_TYPE,"
											+ "CURVE_UNIT,PHYS_FIN,SETTLE_PRICE,ENT_BY,ENT_DT) "
											+ "VALUES(TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,"
											+ "?,?,?,?,?,SYSDATE)";
									stmt1=conn.prepareStatement(queryString1);
									stmt1.setString(1, ReportDate);
									stmt1.setString(2, ""+VCURVE_DT.elementAt(i));
									stmt1.setString(3, ""+VCURVE_NM.elementAt(i));
									stmt1.setString(4, ""+VCOMMODITY_TYPE.elementAt(i));
									stmt1.setString(5, ""+VCURVE_TYPE.elementAt(i));
									stmt1.setString(6, ""+VCURVE_UNIT.elementAt(i));
									stmt1.setString(7, ""+VPHY_FINANCE.elementAt(i));
									stmt1.setString(8, CurveValue);
									stmt1.setString(9, emp_cd);
									stmt1.executeUpdate();
									stmt1.close();
									inserted++;
									count_fwd++;
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

							String queryString = "SELECT COUNT(*) FROM FMS_SPOT_PRICE_DTL "
									+ "WHERE CURVE_DT = TO_DATE(?,'DD/MM/YYYY') "
									+ "AND CURVE_NM = ? "
									+ "AND REPORT_DT=TO_DATE(?,'DD/MM/YYYY')";
							stmt=conn.prepareStatement(queryString);
							stmt.setString(1, CurveDate);
							stmt.setString(2, ""+VCURVE_NM.elementAt(i));
							stmt.setString(3, ReportDate);
							rset=stmt.executeQuery();
							if(rset.next())
							{ 
								if(rset.getInt(1) > 0) 
								{
								}
								else if(!CurveValue.equals("")) //JAYASRI20230922 
								{
									String queryString1 = "INSERT INTO FMS_SPOT_PRICE_DTL(REPORT_DT,CURVE_DT,CURVE_NM,COMMODITY_TYPE,CURVE_TYPE,"
											+ "CURVE_UNIT,PHYS_FIN,SETTLE_PRICE,ENT_BY,ENT_DT,ACTUAL_CURVE) "
											+ "VALUES(TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,"
											+ "?,?,?,?,?,SYSDATE,?)";
									stmt1=conn.prepareStatement(queryString1);
									stmt1.setString(1, ReportDate);
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
									stmt1.close();
									inserted++;
									count_spot++;
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
					
					msg = count_spot+" Spot Prices and "+count_fwd+" Forward Prices Uploaded from ZEMA price File "+original_file_name+".";
					
					boolean flag = sendZEMAPriceUploadConfirmationMail(original_file_name);
					if(flag)
					{
						msg+=" Notification Mail Generated!";
					}
					
				}
				else
				{
					msg = "Created File is not in folder for the Date "+from_dt;
				}
			}
			else
			{
				msg = "No ZEMA pricing file dated "+from_dt+" available for Upload";
			}
		}
		catch(Exception e)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			msg="Error in Exception! - Auto Zema Pricing Failed!";
		}
		
		try
		{
			new automation.Auto_InfoLogger().InsertInfoLogger("0", "","System", ip, "0", "Auto ZEMA Pricing","0","Auto ZEMA Pricing", "", "", msg);  	
		}
		catch(Exception infoLogger)
		{
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, infoLogger);
		}
	}
	
	public String getActualCurveName(String prod_nm)
	{
		String function_nm="getActualCurveName()";
		String nm="";
		try
		{
			String queryString="SELECT CURVE_TYPE "
					+ "FROM FMS_PROD_CURVE_MAP "
					+ "WHERE PROD_TYPE=?";
			stmt=conn.prepareStatement(queryString);
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
			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
		return nm;
	}
	
	public boolean sendZEMAPriceUploadConfirmationMail(String original_file_name)
  	{
		boolean flag=false;
		
		String function_nm="sendZEMAPriceUploadConfirmationMail()";
  		int Count=0; int ForwardPriceCount=0;  int SpotPriceCount=0;
  		String MailBody=""; String Subject="";
  		try
  		{
  			String queryString = "SELECT COUNT(*) "
  					+ "FROM FMS_FORWARD_PRICE_DTL "
  					+ "WHERE REPORT_DT=TO_DATE(?,'DD/MM/YYYY')";
  			stmt=conn.prepareStatement(queryString);
  			stmt.setString(1, to_date);
  			rset = stmt.executeQuery();
  			if(rset.next())
  			{
  				Count = rset.getInt(1);
  				ForwardPriceCount= rset.getInt(1);
  			}
  			rset.close();
			stmt.close();
			
  			queryString = "SELECT COUNT(*) "
  					+ "FROM FMS_SPOT_PRICE_DTL "
  					+ "WHERE REPORT_DT=TO_DATE(?,'DD/MM/YYYY')";
  			stmt=conn.prepareStatement(queryString);
  			stmt.setString(1, to_date);
  			rset = stmt.executeQuery();
  			if(rset.next())
  			{
  				Count += rset.getInt(1);
  				SpotPriceCount= rset.getInt(1);
  			}
  			rset.close();
			stmt.close();
			
			String sys_timing=utilDate.getSysdateWithTime24hr();
  			
  			if(Count > 0)
  			{
  				MailBody = "<font style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Successfully Uploaded ZEMA pricing for "+to_date+" "+CommonVariable.app_name+" ("+SpotPriceCount+" Spot Prices and "+ForwardPriceCount+" Forward Prices) on "+sys_timing+"Hrs.</font>";
  				 				
  				Subject=CommonVariable.app_name+" "+context+": Market Risk ZEMA Pricing Upload Successful!";
  			}
  			else
  			{
  				MailBody = "<font style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>Uploading ZEMA pricing for "+to_date+" in "+CommonVariable.app_name+" Failed on "+sys_timing+"Hrs.</font>";
  				
  				Subject=CommonVariable.app_name+" "+context+": Market Risk ZEMA Pricing Upload Failed!";
			}
			
  			MailBody += "<br><font style='font-size:"+CommonVariable.mail_font_size+";font-family:"+CommonVariable.mail_font_family+";'>ZEMA price xml used is: "+original_file_name+"</font>";
			
  			MailBody+=CommonVariable.mail_disclaimer;
  			
  			String to_list=utilBean.getToMailReceipentList(conn, "0", "Zema Price Upload", "Risk Mgmt", "Daily", "Auto");
			String cc_list=utilBean.getCcMailReceipentList(conn, "0", "Zema Price Upload", "Risk Mgmt", "Daily", "Auto");
			
			System.out.println("to_list=="+to_list);
			System.out.println("cc_list=="+cc_list);
			if(!to_list.equals(""))
			{
				flag = mail.sendMail(conn, to_list, Subject, MailBody, "", cc_list, "");
			}
  		}
  		catch(Exception e)
  		{
  			new Auto_SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
  		}
  		
  		return flag;
  	}
	
	Vector VCURVE_NM = new Vector();
	Vector VREPORT_DT = new Vector();
	Vector VCOMMODITY_TYPE = new Vector();
	Vector VCURVE_TYPE = new Vector();
	Vector VCURVE_UNIT = new Vector();
	Vector VPHY_FINANCE = new Vector();
	Vector VCURVE_DT = new Vector();
	Vector VCURVE_VALUE = new Vector();
	Vector VACTUAL_CURVE_TYPE = new Vector();
}
