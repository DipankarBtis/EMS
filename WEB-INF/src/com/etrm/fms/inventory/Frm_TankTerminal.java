package com.etrm.fms.inventory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.escapeSingleQuotes;

//Coded By          : Harsh Maheta
//Code Reviewed by	:
//CR Date			: 24/12/2024
//Status	  		: Developing

@WebServlet("/servlet/Frm_TankTerminal")
public class Frm_TankTerminal extends HttpServlet
{
	static String db_src_file_name="Frm_TankTerminal.java";
	public static Connection dbcon;

	public static String servletName = "Frm_TankTerminal";
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
	private static PreparedStatement stmt0 = null;

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

	static NumberFormat nf = new DecimalFormat("###########0.00");
	static NumberFormat nf2 = new DecimalFormat("###########0.0000");

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

					if(option.equalsIgnoreCase("TANK_MST"))
					{
						InsertUpdateTankMaster(request);
					}
					else if(option.equalsIgnoreCase("TANK_INTERNAL_CONSUMPTION_DTL"))
					{
						InsertUpdateTankInternalConsumptionDtl(request);
					}
					else if(option.equalsIgnoreCase("TANK_INVENTORY_DTL"))
					{
						InsertUpdateTankInventoryDtl(request);
					}
					else if(option.equalsIgnoreCase("INTERNAL_CONSUMPTION_DTL"))
					{
						InsertUpdateInternalConsumptionDtl(request);
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
				if(stmt0 != null){try {stmt0.close();}catch(SQLException e){System.out.println("stmt0 is not close " + e);}}
				if(dbcon != null){try {dbcon.close();}catch(SQLException e){System.out.println("conn is not close " + e);}}
			}
		}

		try
		{
			response.sendRedirect(url);
		}
		catch(IOException e)
		{
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
		}
	}

	private void InsertUpdateTankMaster(HttpServletRequest request) throws SQLException
	{
		String function_nm="InsertUpdateTankMaster()";
		msg="";
		msg_type="";
		url="";

		try
		{
			String opration=request.getParameter("opration")==null?"":request.getParameter("opration");

			String tank_cd=request.getParameter("tank_cd")==null?"":request.getParameter("tank_cd");
			String tank_name=request.getParameter("tank_name")==null?"":request.getParameter("tank_name");
			String eff_dt=request.getParameter("eff_dt")==null?"":request.getParameter("eff_dt");
			String status=request.getParameter("status")==null?"":request.getParameter("status");
			String tank_t1_volume=request.getParameter("tank_t1_volume")==null?"":request.getParameter("tank_t1_volume");
			String tank_t1_height=request.getParameter("tank_t1_height")==null?"":request.getParameter("tank_t1_height");
			String tank_t2_volume=request.getParameter("tank_t2_volume")==null?"":request.getParameter("tank_t2_volume");
			String tank_t2_height=request.getParameter("tank_t2_height")==null?"":request.getParameter("tank_t2_height");
			String tank_d1_volume=request.getParameter("tank_d1_volume")==null?"":request.getParameter("tank_d1_volume");
			String tank_d1_height=request.getParameter("tank_d1_height")==null?"":request.getParameter("tank_d1_height");
			String tank_d2_volume=request.getParameter("tank_d2_volume")==null?"":request.getParameter("tank_d2_volume");
			String tank_d2_height=request.getParameter("tank_d2_height")==null?"":request.getParameter("tank_d2_height");
			String tank_diameter=request.getParameter("tank_diameter")==null?"":request.getParameter("tank_diameter");
			String tank_pi_tag=request.getParameter("tank_pi_tag")==null?"":request.getParameter("tank_pi_tag");

			if(opration.equals("MODIFY"))
			{
				if(!tank_cd.equals(""))
				{
					int count = 0;
					
					String queryString = "SELECT COUNT(*) "
							+ "FROM FMS_TANK_MST A "
							+ "WHERE COMPANY_CD=? AND TANK_CD=? "
							+ "AND EFF_DT=TO_DATE(?,'DD/MM/YYYY')";
					stmt=dbcon.prepareStatement(queryString);
					stmt.setString(1, comp_cd);
					stmt.setString(2, tank_cd);
					stmt.setString(3, eff_dt);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						count = rset.getInt(1);
					}
					rset.close();
					stmt.close();
					
					if(count==0)
					{
						if(!tank_cd.equals(""))
						{
							int insCnt = 0;

							query1="INSERT INTO FMS_TANK_MST(COMPANY_CD,TANK_CD,TANK_NAME,EFF_DT,STATUS,"
									+ "TANK_T1_VOLUME,TANK_T1_HEIGHT,TANK_T2_VOLUME,TANK_T2_HEIGHT,TANK_D1_VOLUME,TANK_D1_HEIGHT,"
									+ "TANK_D2_VOLUME,TANK_D2_HEIGHT,TANK_DIAMETER,ENT_BY,ENT_DT,TANK_PI_TAG) "
									+ "VALUES(?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?)";
							stmt1=dbcon.prepareStatement(query1);
							stmt1.setString(++insCnt, comp_cd);
							stmt1.setString(++insCnt, tank_cd);
							stmt1.setString(++insCnt, tank_name);
							stmt1.setString(++insCnt, eff_dt);
							stmt1.setString(++insCnt, status);
							stmt1.setString(++insCnt, tank_t1_volume);
							stmt1.setString(++insCnt, tank_t1_height);
							stmt1.setString(++insCnt, tank_t2_volume);
							stmt1.setString(++insCnt, tank_t2_height);
							stmt1.setString(++insCnt, tank_d1_volume);
							stmt1.setString(++insCnt, tank_d1_height);
							stmt1.setString(++insCnt, tank_d2_volume);
							stmt1.setString(++insCnt, tank_d2_height);
							stmt1.setString(++insCnt, tank_diameter);
							stmt1.setString(++insCnt, emp_cd);
							stmt1.setString(++insCnt, tank_pi_tag);
							stmt1.executeUpdate();

							stmt1.close();
						}
						else
						{
							msg = "Failed! - Data Insertion for "+tank_name+" Failed!";
							msg_type="E";
						}

						msg = "Successful! - "+tank_name+" Added!";
						msg_type="S";
					}
					else
					{

						int updCnt=0;

						query="UPDATE FMS_TANK_MST SET TANK_NAME=?, STATUS=?, "
								+ "EFF_DT=TO_DATE(?,'DD/MM/YYYY'), TANK_T1_VOLUME=?,TANK_T1_HEIGHT=?,TANK_T2_VOLUME=?,TANK_T2_HEIGHT=?,TANK_D1_VOLUME=?, "
								+ "TANK_D1_HEIGHT=?,TANK_D2_VOLUME=?,TANK_D2_HEIGHT=?,TANK_DIAMETER=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,TANK_PI_TAG=? "
								+ "WHERE TANK_CD=? AND COMPANY_CD=?";
						stmt1=dbcon.prepareStatement(query);
						stmt1.setString(++updCnt, tank_name);
						stmt1.setString(++updCnt, status);
						stmt1.setString(++updCnt, eff_dt);
						stmt1.setString(++updCnt, tank_t1_volume);
						stmt1.setString(++updCnt, tank_t1_height);
						stmt1.setString(++updCnt, tank_t2_volume);
						stmt1.setString(++updCnt, tank_t2_height);
						stmt1.setString(++updCnt, tank_d1_volume);
						stmt1.setString(++updCnt, tank_d1_height);
						stmt1.setString(++updCnt, tank_d2_volume);
						stmt1.setString(++updCnt, tank_d2_height);
						stmt1.setString(++updCnt, tank_diameter);
						stmt1.setString(++updCnt, emp_cd);
						stmt1.setString(++updCnt, tank_pi_tag);
						stmt1.setString(++updCnt, tank_cd);
						stmt1.setString(++updCnt, comp_cd);
						stmt1.executeUpdate();

						stmt1.close();

						msg = "Successful! - "+tank_name+" updated!";
						msg_type="S";
					}
				}
				else
				{
					msg = "Failed! - Data Modification for "+tank_name+" Failed!";
					msg_type="E";
				}
			}
			else
			{
				tank_cd="1";
				query="SELECT NVL(MAX(tank_CD),0) "
						+ "FROM FMS_TANK_MST ";
				stmt=dbcon.prepareStatement(query);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					tank_cd=""+(rset.getInt(1)+1);
				}
				else
				{
					tank_cd="1";
				}
				rset.close();
				stmt.close();

				if(!tank_cd.equals(""))
				{
					int insCnt = 0;

					query1="INSERT INTO FMS_TANK_MST(COMPANY_CD,TANK_CD,TANK_NAME,EFF_DT,STATUS,"
							+ "TANK_T1_VOLUME,TANK_T1_HEIGHT,TANK_T2_VOLUME,TANK_T2_HEIGHT,TANK_D1_VOLUME,TANK_D1_HEIGHT,"
							+ "TANK_D2_VOLUME,TANK_D2_HEIGHT,TANK_DIAMETER,ENT_BY,ENT_DT,TANK_PI_TAG) "
							+ "VALUES(?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++insCnt, comp_cd);
					stmt1.setString(++insCnt, tank_cd);
					stmt1.setString(++insCnt, tank_name);
					stmt1.setString(++insCnt, eff_dt);
					stmt1.setString(++insCnt, status);
					stmt1.setString(++insCnt, tank_t1_volume);
					stmt1.setString(++insCnt, tank_t1_height);
					stmt1.setString(++insCnt, tank_t2_volume);
					stmt1.setString(++insCnt, tank_t2_height);
					stmt1.setString(++insCnt, tank_d1_volume);
					stmt1.setString(++insCnt, tank_d1_height);
					stmt1.setString(++insCnt, tank_d2_volume);
					stmt1.setString(++insCnt, tank_d2_height);
					stmt1.setString(++insCnt, tank_diameter);
					stmt1.setString(++insCnt, emp_cd);
					stmt1.setString(++insCnt, tank_pi_tag);
					stmt1.executeUpdate();

					stmt1.close();
				}
				else
				{
					msg = "Failed! - Data Insertion for "+tank_name+" Failed!";
					msg_type="E";
				}

				msg = "Successful! - "+tank_name+" Added!";
				msg_type="S";
			}

			url = "../inventory/frm_tank_mst.jsp?msg="+msg+"&msg_type="+msg_type+commonUrl_pra;

			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - Tank Master Submission Failed!";
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

	private void InsertUpdateTankInternalConsumptionDtl(HttpServletRequest request) throws SQLException
	{
		String function_nm="InsertUpdateTankInternalConsumptionDtl()";
		msg="";
		msg_type="";
		url="";

		try
		{
			String opration=request.getParameter("opration")==null?"":request.getParameter("opration");

			String ic_eff_dt=request.getParameter("ic_eff_dt")==null?"":request.getParameter("ic_eff_dt");
			String ic_percentage=request.getParameter("ic_percentage")==null?"":request.getParameter("ic_percentage");
			String ic_remark=request.getParameter("ic_remark")==null?"":request.getParameter("ic_remark");

			String query = "SELECT COUNT(*) "
					+ "FROM FMS_TANK_CONSUMPTION_MST "
					+ "WHERE COMPANY_CD=? AND EFF_DT=TO_DATE(?,'DD/MM/YYYY') ";
			stmt=dbcon.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, ic_eff_dt);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				int cnt = rset.getInt(1);

				if(cnt>0)
				{
					int updCnt=0;

					String query0="UPDATE FMS_TANK_CONSUMPTION_MST SET PERCENTAGE=?, REMARK=?, MODIFY_BY=?,MODIFY_DT=SYSDATE "
							+ "WHERE EFF_DT=TO_DATE(?,'DD/MM/YYYY') AND COMPANY_CD=?";
					stmt0=dbcon.prepareStatement(query0);
					stmt0.setString(++updCnt, ic_percentage);
					stmt0.setString(++updCnt, ic_remark);
					stmt0.setString(++updCnt, emp_cd);
					stmt0.setString(++updCnt, ic_eff_dt);
					stmt0.setString(++updCnt, comp_cd);
					stmt0.executeUpdate();

					stmt0.close();

					msg = "Successful! - Internal Comsuption Details Updated!";
					msg_type="S";

				}
				else
				{
					int insCnt = 0;

					String query1="INSERT INTO FMS_TANK_CONSUMPTION_MST(COMPANY_CD,EFF_DT,PERCENTAGE,REMARK,ENT_BY,ENT_DT,FLAG) "
							+ "VALUES(?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,SYSDATE,?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++insCnt, comp_cd);
					stmt1.setString(++insCnt, ic_eff_dt);
					stmt1.setString(++insCnt, ic_percentage);
					stmt1.setString(++insCnt, ic_remark);
					stmt1.setString(++insCnt, emp_cd);
					stmt1.setString(++insCnt, "Y");
					stmt1.executeUpdate();

					stmt1.close();

					msg = "Successful! - Internal Comsuption Details Added!";
					msg_type="S";
				}
			}

			rset.close();
			stmt.close();

			url = "../inventory/frm_tank_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&ic_eff_dt="+ic_eff_dt+commonUrl_pra;

			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - Internal Consumption Submission Failed!";
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

	private void InsertUpdateTankInventoryDtl(HttpServletRequest request) throws SQLException
	{
		String function_nm="InsertUpdateTankInventoryDtl()";
		msg="";
		msg_type="";
		url="";

		try
		{
			String inv_level_dt = request.getParameter("inv_level_dt")==null?"":request.getParameter("inv_level_dt");

			String[] tank_cd = request.getParameterValues("tank_cd");
			String[] tank_height = request.getParameterValues("tank_height");
			String[] tank_volume = request.getParameterValues("tank_volume");
			String[] tank_conv_factor_1 = request.getParameterValues("tank_conv_factor_1");
			String[] tank_mmscm = request.getParameterValues("tank_mmscm");
			String[] tank_conv_factor_2 = request.getParameterValues("tank_conv_factor_2");
			String[] tank_mmbtu = request.getParameterValues("tank_mmbtu");

			if(inv_level_dt!=null)
			{
				String cargo_msg="";

				for(int i=0; i<tank_cd.length;i++)
				{
					int selCnt=0;

					if(!tank_cd[i].equals(""))
					{
						String query1="SELECT COUNT(*) "
								+ "FROM FMS_TANK_INVENTORY_DTL "
								+ "WHERE COMPANY_CD=? AND INV_LEVEL_DT=TO_DATE(?,'DD/MM/YYYY') "
								+ "AND TANK_CD=? ";
						stmt1 = dbcon.prepareStatement(query1);
						stmt1.setString(++selCnt, comp_cd);
						stmt1.setString(++selCnt, inv_level_dt);
						stmt1.setString(++selCnt, tank_cd[i]);
						rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							int count=rset1.getInt(1);

							if(count>0)
							{
								int st_count=0;

								String query ="UPDATE FMS_TANK_INVENTORY_DTL SET TANK_VOLUME=?,TANK_HEIGHT=?,TANK_MMSCM=?,"
										+ "TANK_CONV_FACTOR_1=?,TANK_CONV_FACTOR_2=?,TANK_MMBTU=?,MODIFY_DT=SYSDATE,MODIFY_BY=? "
										+ "WHERE COMPANY_CD=? AND INV_LEVEL_DT=TO_DATE(?,'DD/MM/YYYY') "
										+ "AND TANK_CD=? ";
								stmt0 = dbcon.prepareStatement(query);
								stmt0.setString(++st_count, tank_volume[i]);
								stmt0.setString(++st_count, tank_height[i]);
								stmt0.setString(++st_count, tank_mmscm[i]);
								stmt0.setString(++st_count, tank_conv_factor_1[i]);
								stmt0.setString(++st_count, tank_conv_factor_2[i]);
								stmt0.setString(++st_count, tank_mmbtu[i]);
								stmt0.setString(++st_count, emp_cd);
								stmt0.setString(++st_count, comp_cd);
								stmt0.setString(++st_count, inv_level_dt);
								stmt0.setString(++st_count, tank_cd[i]);
								stmt0.executeUpdate();

								stmt0.close();
								msg = "Successful! - Tank inventory Details for "+inv_level_dt+" Submitted Successfully!";
								msg_type="S";
							}
							else
							{
								int insCnt=0;

								String query = "INSERT INTO FMS_TANK_INVENTORY_DTL (COMPANY_CD, "
										+ "INV_LEVEL_DT, "
										+ "TANK_CD, "
										+ "TANK_VOLUME, "
										+ "TANK_HEIGHT, "
										+ "TANK_MMSCM, "
										+ "TANK_CONV_FACTOR_1, "
										+ "TANK_CONV_FACTOR_2, "
										+ "TANK_MMBTU, "
										+ "ENT_BY, "
										+ "ENT_DT)"
										+ "VALUES(?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,?,?,SYSDATE)";
								stmt2 = dbcon.prepareStatement(query);
								stmt2.setString(++insCnt, comp_cd);
								stmt2.setString(++insCnt, inv_level_dt);
								stmt2.setString(++insCnt, tank_cd[i]);
								stmt2.setString(++insCnt, tank_volume[i]);
								stmt2.setString(++insCnt, tank_height[i]);
								stmt2.setString(++insCnt, tank_mmscm[i]);
								stmt2.setString(++insCnt, tank_conv_factor_1[i]);
								stmt2.setString(++insCnt, tank_conv_factor_2[i]);
								stmt2.setString(++insCnt, tank_mmbtu[i]);
								stmt2.setString(++insCnt, emp_cd);
								stmt2.executeUpdate();

								stmt2.close();
								msg = "Successful! - Tank inventory Details for "+inv_level_dt+" Submitted Successfully!";
								msg_type="S";
							}
						}
						rset1.close();
						stmt1.close();
					}
				}
			}
			else
			{
				msg="Failed! - Data Submission Failed!";
				msg_type="E";
			}

			url = "../inventory/frm_tank_inventory_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&inv_level_dt="+inv_level_dt+commonUrl_pra;

			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - Tank Inventory Details Submission Failed!";
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

	private void InsertUpdateInternalConsumptionDtl(HttpServletRequest request) throws SQLException
	{
		String function_nm="InsertUpdateInternalConsumptionDtl()";
		msg="";
		msg_type="";
		url="";

		try
		{
			String year = request.getParameter("year")==null?"":request.getParameter("year");

			String[] month_no = request.getParameterValues("month_no");
			String[] lng_write_off = request.getParameterValues("lng_write_off");
			String[] flaring = request.getParameterValues("flaring");
			String[] auxilary_consumption = request.getParameterValues("auxilary_consumption");
			String[] scv_fuel_consumption = request.getParameterValues("scv_fuel_consumption");
			String[] sug = request.getParameterValues("sug");
			String[] other_consumption = request.getParameterValues("other_consumption");
			String[] mass_balancing = request.getParameterValues("mass_balancing");
			String[] total_consumption = request.getParameterValues("total_consumption");

			if(year!=null)
			{
				for(int i=0; i<month_no.length;i++)
				{
					int selCnt=0;

					if(!month_no[i].equals(""))
					{
						String query1="SELECT COUNT(*) "
								+ "FROM FMS_TANK_INTRNL_CONSUMPTION "
								+ "WHERE COMPANY_CD=?  "
								+ "AND YEAR=? AND MONTH=?  ";
						stmt1 = dbcon.prepareStatement(query1);
						stmt1.setString(++selCnt, comp_cd);
						stmt1.setString(++selCnt, year);
						stmt1.setString(++selCnt, month_no[i]);
						ResultSet rset1=stmt1.executeQuery();
						if(rset1.next())
						{
							int count=rset1.getInt(1);

							if(count>0)
							{
								int st_count=0;

								String query ="UPDATE FMS_TANK_INTRNL_CONSUMPTION SET LNG_WRITE_OFF=?,FLARING=?,AUXILARY_CONSUMPTION=?,"
										+ "SCV_FUEL_CONSUMPTION=?,SUG=?,OTHER_CONSUMPTION=?,MASS_BALANCING=?,TOTAL_CONSUMPTION=?,MODIFY_DT=SYSDATE,MODIFY_BY=? "
										+ "WHERE COMPANY_CD=?  "
										+ "AND YEAR=? AND MONTH=?  ";
								stmt0 = dbcon.prepareStatement(query);
								stmt0.setString(++st_count, lng_write_off[i]);
								stmt0.setString(++st_count, flaring[i]);
								stmt0.setString(++st_count, auxilary_consumption[i]);
								stmt0.setString(++st_count, scv_fuel_consumption[i]);
								stmt0.setString(++st_count, sug[i]);
								stmt0.setString(++st_count, other_consumption[i]);
								stmt0.setString(++st_count, mass_balancing[i]);
								stmt0.setString(++st_count, total_consumption[i]);
								stmt0.setString(++st_count, emp_cd);
								stmt0.setString(++st_count, comp_cd);
								stmt0.setString(++st_count, year);
								stmt0.setString(++st_count, month_no[i]);
								stmt0.executeUpdate();

								stmt0.close();
								msg = "Successful! - Internal Consumption Details for "+year+" Submitted Successfully!";
								msg_type="S";
							}
							else
							{
								int insCnt=0;

								String query = "INSERT INTO FMS_TANK_INTRNL_CONSUMPTION (COMPANY_CD, "
										+ "YEAR, "
										+ "MONTH, "
										+ "LNG_WRITE_OFF, "
										+ "FLARING, "
										+ "AUXILARY_CONSUMPTION, "
										+ "SCV_FUEL_CONSUMPTION, "
										+ "SUG, "
										+ "OTHER_CONSUMPTION, "
										+ "MASS_BALANCING, "
										+ "TOTAL_CONSUMPTION, "
										+ "ENT_BY, "
										+ "ENT_DT)"
										+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE)";
								stmt2 = dbcon.prepareStatement(query);
								stmt2.setString(++insCnt, comp_cd);
								stmt2.setString(++insCnt, year);
								stmt2.setString(++insCnt, month_no[i]);
								stmt2.setString(++insCnt, lng_write_off[i]);
								stmt2.setString(++insCnt, flaring[i]);
								stmt2.setString(++insCnt, auxilary_consumption[i]);
								stmt2.setString(++insCnt, scv_fuel_consumption[i]);
								stmt2.setString(++insCnt, sug[i]);
								stmt2.setString(++insCnt, other_consumption[i]);
								stmt2.setString(++insCnt, mass_balancing[i]);
								stmt2.setString(++insCnt, total_consumption[i]);
								stmt2.setString(++insCnt, emp_cd);
								stmt2.executeUpdate();

								stmt2.close();
								msg = "Successful! - Internal Consumption Details for "+year+" Submitted Successfully!";
								msg_type="S";
							}
						}
						rset1.close();
						stmt1.close();
					}
				}
			}
			else
			{
				msg="Failed! - Data Submission for Internal Consumption Details Failed!";
				msg_type="E";
			}

			url = "../inventory/frm_tank_internal_consumption_dtl.jsp?year="+year+"&msg="+msg+"&msg_type="+msg_type+commonUrl_pra;

			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! - Internal Consumption Details Submission Failed!";
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
