package com.etrm.fms.market_risk;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;

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
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.escapeSingleQuotes;

//Coded By          : Harsh Patel
//Code Reviewed by	:
//CR Date			: 11/08/2023
//Status	  		: Developing
@WebServlet("/servlet/Frm_VariablePricing")
public class Frm_VariablePricing extends HttpServlet
{
	static String db_src_file_name="Frm_VariablePricing.java";
	public static  Connection dbcon;

	public static String servletName = "Frm_VariablePricing";
	public static String option = "";
	public static String url = "";
	public static String msg = "";
	public static String msg_type = "";
	public static String err_msg = "";

	private static String queryString = null;
	private static String queryString1 = null;
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
	static DateUtil utilDate = new DateUtil();
	
	static NumberFormat nf = new DecimalFormat("###########0.00");
	static NumberFormat nf2 = new DecimalFormat("###########0.0000");
	static NumberFormat nf3 = new DecimalFormat("###########0.000");
	
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

					emp_cd = (String)session.getAttribute("emp_cd")==null?"":(String)session.getAttribute("emp_cd");
					comp_cd = (String)session.getAttribute("comp_cd")==null?"":(String)session.getAttribute("comp_cd");
					comp_abbr = (String)session.getAttribute("comp_abbr")==null?"":(String)session.getAttribute("comp_abbr");
					emp_nm = (String)session.getAttribute("emp_nm")==null?"":(String)session.getAttribute("emp_nm");
					ip = (String)session.getAttribute("ip")==null?"":(String)session.getAttribute("ip");

					new_value="";
					old_value="";

					option=request.getParameter("option")==null?"":request.getParameter("option");

					commonUrl_pra = "&u="+u;

					if(option.equalsIgnoreCase("VARIABLE_PRICE_CONFIG"))
					{
						InsertUpdateContVariablePriceConfigDetail(request);
					}
					else if(option.equalsIgnoreCase("VARIABLE_TAQ_CONFIG"))
					{
						InsertUpdateContTAQConfigDetail(request);
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
	
	private void InsertUpdateContVariablePriceConfigDetail(HttpServletRequest request) throws SQLException
	{
		String function_nm="InsertUpdateContVariablePriceConfigDetail()";
		msg="";
		msg_type="";
		url="";
		
		old_value="";
		new_value="";

		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			old_value = request.getParameter("old_value")==null?"":request.getParameter("old_value");
			new_value ="";
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
			String agmt_no=request.getParameter("agmt_no")==null?"0":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"0":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"0":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"0":request.getParameter("cont_rev_no");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String cont_start_dt=request.getParameter("cont_start_dt")==null?"":request.getParameter("cont_start_dt");
			String cont_end_dt = request.getParameter("cont_end_dt")==null?"":request.getParameter("cont_end_dt");
			String cont_ref = request.getParameter("cont_ref")==null?"":request.getParameter("cont_ref");
			String cont_status = request.getParameter("cont_status")==null?"":request.getParameter("cont_status");
			String cargo_no=request.getParameter("cargo_no")==null?"0":request.getParameter("cargo_no");
			
			String from_dt = request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
			String to_dt=request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
			String price_type=request.getParameter("price_type")==null?"":request.getParameter("price_type");
			String currency=request.getParameter("currency")==null?"":request.getParameter("currency");
			String rate=request.getParameter("rate")==null?"":request.getParameter("rate");
			String physical_curve = request.getParameter("physical_curve")==null?"":request.getParameter("physical_curve");
			String curve_nm[] = request.getParameterValues("curve_nm");
			String remark=request.getParameter("remark")==null?"":request.getParameter("remark");
			
			String slope=request.getParameter("slope")==null?"1":request.getParameter("slope");
			String constant=request.getParameter("constant")==null?"0":request.getParameter("constant");
			String seq_no=request.getParameter("seq_no")==null?"":request.getParameter("seq_no");
			
			String price_range=request.getParameter("price_range")==null?"":request.getParameter("price_range");
			String days = request.getParameter("days"); 
			String price_start_dt=request.getParameter("price_start_dt")==null?"":request.getParameter("price_start_dt");
			String price_end_dt=request.getParameter("price_end_dt")==null?"":request.getParameter("price_end_dt");
			
			String opal_min = request.getParameter("opal_min")==null?"":request.getParameter("opal_min");
			String formula_desc = request.getParameter("formula_desc")==null?"":request.getParameter("formula_desc");
			
			//PB20260227
			String price_deci = request.getParameter("price_deci")==null?"":request.getParameter("price_deci");
			String prem_disc_rate = request.getParameter("prem_disc_rate")==null?"":request.getParameter("prem_disc_rate");
			String final_price_deci = request.getParameter("final_price_deci")==null?"":request.getParameter("final_price_deci");
			
			String counterparty_abbr = utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			/*String contract_no ="";
			if(contract_type.equalsIgnoreCase("S"))
			{
				contract_no = contract_type+""+agmt_no+"-"+cont_no+"-"+cargo_no;
			}
			else if(contract_type.equalsIgnoreCase("S"))
			{
				contract_no = contract_type+""+agmt_no+"-"+cont_no;
			}
			else
			{
				contract_no = contract_type+""+cont_no;
			}*/
			
			String MIN_price_range[] = request.getParameterValues("MIN_price_range"); 
			String MIN_days[] = request.getParameterValues("MIN_days"); 
			String MIN_price_start_dt[] = request.getParameterValues("MIN_price_start_dt");
			String MIN_price_end_dt[] = request.getParameterValues("MIN_price_end_dt");
			
			String Min_slope[] = request.getParameterValues("MIN_slope"); 
			String Min_constant[] = request.getParameterValues("MIN_constant"); 
			String MIN_formula_desc[] = request.getParameterValues("MIN_formula_desc"); 
			
			//PB 20260227
			String MIN_price_deci[] = request.getParameterValues("MIN_price_deci");
			String MIN_prem_disc_rate[] = request.getParameterValues("MIN_prem_disc_rate");
			
			if(days != null)
			{
				if(price_range.equals("O"))
				{
					price_range=price_range+""+days;
				}
			}
			
			//String mapping_id=counterparty_cd+"-"+agmt_no+"-"+cont_no;
			
			String mapping_id = request.getParameter("mapping_id")==null?"":request.getParameter("mapping_id");
			String display_map_id = request.getParameter("display_map_id")==null?"":request.getParameter("display_map_id");
			
			if(opration.equals("MODIFY"))
			{
				if(!seq_no.equals(""))
				{
					if(price_type.equals("F"))
					{
						query="UPDATE FMS_CONT_PRICE_DTL SET FROM_DT=TO_DATE(?,'DD/MM/YYYY'),TO_DT=TO_DATE(?,'DD/MM/YYYY'),"
								+ "PRICE_TYPE=?,RATE=?,RATE_UNIT=?,PHYS_CURVE_NM=?,REMARKS=?,"
								+ "CURVE_NM=?,SLOPE=?,CONST=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,"
								+ "PRICE_RANGE=?,PRICE_START_DT=TO_DATE(?,'DD/MM/YYYY'),PRICE_END_DT=TO_DATE(?,'DD/MM/YYYY'),"
								+ "CURVE_LOGIC=?,FORMULA=? "
								+ "WHERE COMPANY_CD=? AND MAPPING_ID=? "
								+ "AND CONTRACT_TYPE=? AND SEQ_NO=?";
						stmt = dbcon.prepareStatement(query);
						stmt.setString(1, from_dt);
						stmt.setString(2, to_dt);
						stmt.setString(3, price_type);
						stmt.setString(4, rate);
						stmt.setString(5, currency);
						stmt.setString(6, physical_curve);
						stmt.setString(7, remark);
						stmt.setString(8, "");
						stmt.setString(9, "1");
						stmt.setString(10, "0");
						stmt.setString(11, emp_cd);
						stmt.setString(12, "");
						stmt.setString(13, "");
						stmt.setString(14, "");
						stmt.setString(15, "");
						stmt.setString(16, "");
						stmt.setString(17, comp_cd);
						stmt.setString(18, mapping_id);
						stmt.setString(19, contract_type);
						stmt.setString(20, seq_no);
						stmt.executeUpdate();
						msg = "Successful! - Contract Variable Price for "+display_map_id+" Modified Successfully!";
						msg_type="S";
						
						stmt.close();
						
						queryString="DELETE FROM FMS_CONT_PRICE_MIN_DTL "
								+ "WHERE COMPANY_CD=? AND MAPPING_ID LIKE ? "
								+ "AND CONTRACT_TYPE=? AND SEQ_NO=?";
						stmt1 = dbcon.prepareStatement(queryString);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, mapping_id);
						stmt1.setString(3, contract_type);
						stmt1.setString(4, seq_no);
						stmt1.executeUpdate();
						
						stmt1.close();
					}
					else
					{
						if(opal_min.equals("MULTI_LEG") || opal_min.equals("SINGLE"))
						{
							query="UPDATE FMS_CONT_PRICE_DTL SET FROM_DT=TO_DATE(?,'DD/MM/YYYY'),TO_DT=TO_DATE(?,'DD/MM/YYYY'),"
									+ "PRICE_TYPE=?,RATE=?,RATE_UNIT=?,PHYS_CURVE_NM=?,REMARKS=?,"
									+ "CURVE_NM=?,SLOPE=?,CONST=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,"
									+ "PRICE_RANGE=?,PRICE_START_DT=TO_DATE(?,'DD/MM/YYYY'),PRICE_END_DT=TO_DATE(?,'DD/MM/YYYY'),"
									+ "CURVE_LOGIC=?,FORMULA=?,"
									+ "PRICE_DECI=?,FINAL_PRICE_DECI=?,PREM_DISC_RATE=? "		//PB 20260227
									+ "WHERE COMPANY_CD=? AND MAPPING_ID=? "
									+ "AND CONTRACT_TYPE=? AND SEQ_NO=?";
							stmt = dbcon.prepareStatement(query);
							stmt.setString(1, from_dt);
							stmt.setString(2, to_dt);
							stmt.setString(3, price_type);
							stmt.setString(4, rate);
							stmt.setString(5, currency);
							stmt.setString(6, physical_curve);
							stmt.setString(7, remark);
							stmt.setString(8, curve_nm[0]);
							stmt.setString(9, slope);
							stmt.setString(10, constant);
							stmt.setString(11, emp_cd);
							stmt.setString(12, price_range);
							stmt.setString(13, price_start_dt);
							stmt.setString(14, price_end_dt);
							stmt.setString(15, opal_min);
							stmt.setString(16, formula_desc);
							stmt.setString(17, price_deci);
							stmt.setString(18, final_price_deci);
							stmt.setString(19, prem_disc_rate);
							stmt.setString(20, comp_cd);
							stmt.setString(21, mapping_id);
							stmt.setString(22, contract_type);
							stmt.setString(23, seq_no);
							stmt.executeUpdate();
							msg = "Successful! - Contract Variable Price for "+display_map_id+" Modified Successfully!";
							msg_type="S";
							
							stmt.close();
							
							queryString="DELETE FROM FMS_CONT_PRICE_MIN_DTL "
									+ "WHERE COMPANY_CD=? AND MAPPING_ID LIKE ? "
									+ "AND CONTRACT_TYPE=? AND SEQ_NO=?";
							stmt1 = dbcon.prepareStatement(queryString);
							stmt1.setString(1, comp_cd);
							stmt1.setString(2, mapping_id);
							stmt1.setString(3, contract_type);
							stmt1.setString(4, seq_no);
							stmt1.executeUpdate();
							
							stmt1.close();
							
							String mapped_cont_no = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, cargo_no);
							
							new_value = "CP="+counterparty_cd+"#CPABBR="+counterparty_abbr+"#CONTNO="+mapped_cont_no+"#SLOPE="+slope+"#CONSTANT="+constant+"#CURVE_NM="+curve_nm[0]+"#VP="+"Y"+"#CONTREFNO="+cont_ref+"#CONT_STATUS="+cont_status;
						}
						else if(opal_min.equals("MIN") || opal_min.equals("MAX") || opal_min.equals("AVG") || opal_min.equals("MIN_ADV"))
						{
							query="UPDATE FMS_CONT_PRICE_DTL SET FROM_DT=TO_DATE(?,'DD/MM/YYYY'),TO_DT=TO_DATE(?,'DD/MM/YYYY'),"
									+ "PRICE_TYPE=?,RATE=?,RATE_UNIT=?,PHYS_CURVE_NM=?,REMARKS=?,"
									+ "CURVE_NM=?,SLOPE=?,CONST=?,MODIFY_BY=?,MODIFY_DT=SYSDATE,"
									+ "PRICE_RANGE=?,PRICE_START_DT=TO_DATE(?,'DD/MM/YYYY'),PRICE_END_DT=TO_DATE(?,'DD/MM/YYYY'),"
									+ "CURVE_LOGIC=?,FORMULA=?, "
									+ "PRICE_DECI=?,FINAL_PRICE_DECI=?,PREM_DISC_RATE=? "		//PB 20260227
									+ "WHERE COMPANY_CD=? AND MAPPING_ID=? "
									+ "AND CONTRACT_TYPE=? AND SEQ_NO=?";
							stmt = dbcon.prepareStatement(query);
							stmt.setString(1, from_dt);
							stmt.setString(2, to_dt);
							stmt.setString(3, price_type);
							stmt.setString(4, rate);
							stmt.setString(5, currency);
							stmt.setString(6, physical_curve);
							stmt.setString(7, remark);
							stmt.setString(8, "");
							stmt.setString(9, slope);
							stmt.setString(10, constant);
							stmt.setString(11, emp_cd);
							stmt.setString(12, price_range);
							stmt.setString(13, price_start_dt);
							stmt.setString(14, price_end_dt);
							stmt.setString(15, opal_min);
							stmt.setString(16, formula_desc);
							stmt.setString(17, price_deci);
							stmt.setString(18, final_price_deci);
							stmt.setString(19, prem_disc_rate);
							stmt.setString(20, comp_cd);
							stmt.setString(21, mapping_id);
							stmt.setString(22, contract_type);
							stmt.setString(23, seq_no);
							stmt.executeUpdate();
							msg = "Successful! - Contract Variable Price for "+display_map_id+" Modified Successfully!";
							msg_type="S";
							
							stmt.close();
							if(curve_nm != null && Min_slope != null && Min_constant != null)
							{
								queryString="DELETE FROM FMS_CONT_PRICE_MIN_DTL "
										+ "WHERE COMPANY_CD=? AND MAPPING_ID LIKE ? "
										+ "AND CONTRACT_TYPE=? AND SEQ_NO=?";
								stmt1 = dbcon.prepareStatement(queryString);
								stmt1.setString(1, comp_cd);
								stmt1.setString(2, mapping_id);
								stmt1.setString(3, contract_type);
								stmt1.setString(4, seq_no);
								stmt1.executeUpdate();
								
								stmt1.close();
								
								for(int i=0; i<curve_nm.length;i++)
								{
									String min_price_range = "";
									if(MIN_price_range[i].equals("O"))
									{
										if(MIN_days[i] != null)
										{
											min_price_range=MIN_price_range[i]+""+MIN_days[i];
										}
									}
									else
									{
										min_price_range = MIN_price_range[i];
									}
									
									queryString1="INSERT INTO FMS_CONT_PRICE_MIN_DTL(COMPANY_CD,MAPPING_ID, CONTRACT_TYPE, SEQ_NO, FROM_DT, TO_DT, "
											+ "CURVE_NM, SLOPE, CONST, REMARKS, ENT_BY, ENT_DT, "
											+ "PRICE_RANGE,PRICE_START_DT,PRICE_END_DT,"
											+ "CURVE_LOGIC,FORMULA,PRICE_DECI,PREM_DISC_RATE) "
											+ "VALUES(?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
											+ "?,?,?,?,?,SYSDATE,"
											+ "?,to_date(?,'DD/MM/YYYY'),to_date(?,'DD/MM/YYYY'),"
											+ "?,?,?,?)";						
									stmt2 = dbcon.prepareStatement(queryString1);
									stmt2.setString(1, comp_cd);
									stmt2.setString(2, mapping_id);
									stmt2.setString(3, contract_type);
									stmt2.setString(4, seq_no);
									stmt2.setString(5, from_dt);
									stmt2.setString(6, to_dt);
									stmt2.setString(7, curve_nm[i]);
									stmt2.setString(8, Min_slope[i]);
									stmt2.setString(9, Min_constant[i]);
									stmt2.setString(10, remark);
									stmt2.setString(11, emp_cd);
									stmt2.setString(12, min_price_range);
									stmt2.setString(13, MIN_price_start_dt[i]);
									stmt2.setString(14, MIN_price_end_dt[i]);
									stmt2.setString(15, opal_min);
									stmt2.setString(16, MIN_formula_desc[i]);
									stmt2.setString(17, MIN_price_deci[i]);
									stmt2.setString(18, MIN_prem_disc_rate[i]);
									stmt2.executeUpdate();
									stmt2.close();
								}
							}
							
							String new_curve_nm=(Arrays.toString(curve_nm).replace("[", "").replace("]", "").replace(", ", "<br>"));
							String new_Min_slope=(Arrays.toString(Min_slope).replace("[", "").replace("]", "").replace(", ", "<br>"));
							String new_Min_constant=(Arrays.toString(Min_constant).replace("[", "").replace("]", "").replace(", ", "<br>"));
							
							String mapped_cont_no = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, cargo_no);
							
							new_value = "CP="+counterparty_cd+"#CPABBR="+counterparty_abbr+"#CONTNO="+mapped_cont_no+"#SLOPE="+new_Min_slope+"#CONSTANT="+new_Min_constant+"#CURVE_NM="+new_curve_nm+"#VP="+"Y"+"#CONTREFNO="+cont_ref+"#CONT_STATUS="+cont_status;
						}
					}
				}
			}
			else
			{
				query="SELECT NVL(MAX(SEQ_NO),0) "
						+ "FROM FMS_CONT_PRICE_DTL "
						+ "WHERE COMPANY_CD=? AND MAPPING_ID=? AND CONTRACT_TYPE=?";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, mapping_id);
				stmt.setString(3, contract_type);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					seq_no=""+(rset.getInt(1)+1);
				}
				else
				{
					seq_no="1";
				}
				rset.close();
				stmt.close();
				
				if(price_type.equals("F"))
				{
					query="INSERT INTO FMS_CONT_PRICE_DTL(COMPANY_CD,MAPPING_ID,CONTRACT_TYPE,SEQ_NO,"
							+ "FROM_DT,TO_DT,PRICE_TYPE,RATE,RATE_UNIT,PHYS_CURVE_NM,REMARKS,"
							+ "SLOPE,CONST,ENT_BY,ENT_DT) "
							+ "VALUES(?,?,?,?,"
							+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,"
							+ "?,?,?,?,"
							+ "?,?,?,SYSDATE)";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, mapping_id);
					stmt1.setString(3, contract_type);
					stmt1.setString(4, seq_no);
					stmt1.setString(5, from_dt);
					stmt1.setString(6, to_dt);
					stmt1.setString(7, price_type);
					stmt1.setString(8, rate);
					stmt1.setString(9, currency);
					stmt1.setString(10, physical_curve);
					stmt1.setString(11, remark);
					stmt1.setString(12, slope);
					stmt1.setString(13, constant);
					stmt1.setString(14, emp_cd);
					stmt1.executeQuery();
					msg = "Successful! - Contract Variable Price for "+display_map_id+" Inserted Successfully!";
					msg_type="S";
					
					stmt1.close();
				}
				else
				{
					if(opal_min.equals("MULTI_LEG") || opal_min.equals("SINGLE"))
					{
						query="INSERT INTO FMS_CONT_PRICE_DTL(COMPANY_CD,MAPPING_ID,CONTRACT_TYPE,SEQ_NO,"
								+ "FROM_DT,TO_DT,PRICE_TYPE,RATE,RATE_UNIT,PHYS_CURVE_NM,REMARKS,"
								+ "SLOPE,CONST,ENT_BY,ENT_DT,CURVE_NM,"
								+ "PRICE_RANGE,PRICE_START_DT,PRICE_END_DT,"
								+ "CURVE_LOGIC,FORMULA,PRICE_DECI,FINAL_PRICE_DECI,PREM_DISC_RATE) "
								+ "VALUES(?,?,?,?,"
								+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,"
								+ "?,?,?,?,"
								+ "?,?,?,SYSDATE,?,"
								+ "?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
								+ "?,?,?,?,?)";
						stmt1 = dbcon.prepareStatement(query);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, mapping_id);
						stmt1.setString(3, contract_type);
						stmt1.setString(4, seq_no);
						stmt1.setString(5, from_dt);
						stmt1.setString(6, to_dt);
						stmt1.setString(7, price_type);
						stmt1.setString(8, rate);
						stmt1.setString(9, currency);
						stmt1.setString(10, physical_curve);
						stmt1.setString(11, remark);
						stmt1.setString(12, slope);
						stmt1.setString(13, constant);
						stmt1.setString(14, emp_cd);
						stmt1.setString(15, curve_nm[0]);
						stmt1.setString(16, price_range);
						stmt1.setString(17, price_start_dt);
						stmt1.setString(18, price_end_dt);
						stmt1.setString(19, opal_min);
						stmt1.setString(20, formula_desc);
						stmt1.setString(21, price_deci);
						stmt1.setString(22, final_price_deci);
						stmt1.setString(23, prem_disc_rate);
						stmt1.executeQuery();
						msg = "Successful! - Contract Variable Price for "+display_map_id+" Inserted Successfully!";
						msg_type="S";
						
						stmt1.close();
						
						String mapped_cont_no = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, cargo_no);
						
						new_value = "CP="+counterparty_cd+"#CPABBR="+counterparty_abbr+"#CONTNO="+mapped_cont_no+"#SLOPE="+slope+"#CONSTANT="+constant+"#CURVE_NM="+curve_nm[0]+"#VP="+"Y"+"#CONTREFNO="+cont_ref+"#CONT_STATUS="+cont_status;
					}
					else if(opal_min.equals("MIN") || opal_min.equals("MAX") || opal_min.equals("AVG") || opal_min.equals("MIN_ADV"))
					{
						query="INSERT INTO FMS_CONT_PRICE_DTL(COMPANY_CD,MAPPING_ID,CONTRACT_TYPE,SEQ_NO,"
								+ "FROM_DT,TO_DT,PRICE_TYPE,RATE,RATE_UNIT,PHYS_CURVE_NM,REMARKS,"
								+ "SLOPE,CONST,ENT_BY,ENT_DT,CURVE_NM,"
								+ "PRICE_RANGE,PRICE_START_DT,PRICE_END_DT,"
								+ "CURVE_LOGIC,FORMULA,PRICE_DECI,FINAL_PRICE_DECI,PREM_DISC_RATE) "
								+ "VALUES(?,?,?,?,"
								+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,"
								+ "?,?,?,?,"
								+ "?,?,?,SYSDATE,?,"
								+ "?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
								+ "?,?,?,?,?)";
						stmt1 = dbcon.prepareStatement(query);
						stmt1.setString(1, comp_cd);
						stmt1.setString(2, mapping_id);
						stmt1.setString(3, contract_type);
						stmt1.setString(4, seq_no);
						stmt1.setString(5, from_dt);
						stmt1.setString(6, to_dt);
						stmt1.setString(7, price_type);
						stmt1.setString(8, rate);
						stmt1.setString(9, currency);
						stmt1.setString(10, physical_curve);
						stmt1.setString(11, remark);
						stmt1.setString(12, slope);
						stmt1.setString(13, constant);
						stmt1.setString(14, emp_cd);
						stmt1.setString(15, "");
						stmt1.setString(16, price_range);
						stmt1.setString(17, price_start_dt);
						stmt1.setString(18, price_end_dt);
						stmt1.setString(19, opal_min);
						stmt1.setString(20, formula_desc);
						stmt1.setString(21, price_deci);
						stmt1.setString(22, final_price_deci);
						stmt1.setString(23, prem_disc_rate);
						stmt1.executeQuery();
						
						stmt1.close();
						
						if(curve_nm != null && Min_slope != null && Min_constant != null)
						{
							for(int i=0; i<curve_nm.length;i++)
							{
								String min_price_range = "";
								if(MIN_price_range[i].equals("O"))
								{
									if(MIN_days[i] != null)
									{
										min_price_range=MIN_price_range[i]+""+MIN_days[i];
									}
								}
								else
								{
									min_price_range = MIN_price_range[i];
								}
								
								queryString="INSERT INTO FMS_CONT_PRICE_MIN_DTL(COMPANY_CD,MAPPING_ID, CONTRACT_TYPE, SEQ_NO, FROM_DT, TO_DT, "
										+ "CURVE_NM, SLOPE, CONST, REMARKS, ENT_BY, ENT_DT, "
										+ "PRICE_RANGE,PRICE_START_DT,PRICE_END_DT,"
										+ "CURVE_LOGIC,FORMULA,PRICE_DECI, PREM_DISC_RATE) "
										+ "VALUES(?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
										+ "?,?,?,?,?,SYSDATE,"
										+ "?,to_date(?,'DD/MM/YYYY'),to_date(?,'DD/MM/YYYY'),"
										+ "?,?,?,?)";	
								stmt2 = dbcon.prepareStatement(queryString);
								stmt2.setString(1, comp_cd);
								stmt2.setString(2, mapping_id);
								stmt2.setString(3, contract_type);
								stmt2.setString(4, seq_no);
								stmt2.setString(5, from_dt);
								stmt2.setString(6, to_dt);
								stmt2.setString(7, curve_nm[i]);
								stmt2.setString(8, Min_slope[i]);
								stmt2.setString(9, Min_constant[i]);
								stmt2.setString(10, remark);
								stmt2.setString(11, emp_cd);
								stmt2.setString(12, min_price_range);
								stmt2.setString(13, MIN_price_start_dt[i]);
								stmt2.setString(14, MIN_price_end_dt[i]);
								stmt2.setString(15, opal_min);
								stmt2.setString(16, MIN_formula_desc[i]);
								stmt2.setString(17, MIN_price_deci[i]);
								stmt2.setString(18, MIN_prem_disc_rate[i]);
								stmt2.executeUpdate();
								
								stmt2.close();
							}
						}
						msg = "Successful! - Contract Variable Price for "+display_map_id+" Inserted Successfully!";
						msg_type="S";
						
						String new_curve_nm=(Arrays.toString(curve_nm).replace("[", "").replace("]", "").replace(", ", "<br>"));
						String new_Min_slope=(Arrays.toString(Min_slope).replace("[", "").replace("]", "").replace(", ", "<br>"));
						String new_Min_constant=(Arrays.toString(Min_constant).replace("[", "").replace("]", "").replace(", ", "<br>"));
						
						String mapped_cont_no = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, cargo_no);
						
						new_value = "CP="+counterparty_cd+"#CPABBR="+counterparty_abbr+"#CONTNO="+mapped_cont_no+"#SLOPE="+new_Min_slope+"#CONSTANT="+new_Min_constant+"#CURVE_NM="+new_curve_nm+"#VP="+"Y"+"#CONTREFNO="+cont_ref+"#CONT_STATUS="+cont_status;
					}
				}
			}

			url = "../market_risk/frm_config_price_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&cont_ref="+cont_ref+"&cont_status="+cont_status+
					"&start_dt="+cont_start_dt+"&end_dt="+cont_end_dt+"&cargo_no="+cargo_no+
					"&contract_type="+contract_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg="Error in Exception! - Contract Variable Price Insert/Update Falied!";
			
			old_value="";
			new_value="";
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

	private void InsertUpdateContTAQConfigDetail(HttpServletRequest request) throws SQLException
	{
		String function_nm="InsertUpdateContTAQConfigDetail()";
		msg="";
		msg_type="";
		url="";
		
		old_value="";
		new_value="";

		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
			String agmt_no=request.getParameter("agmt_no")==null?"0":request.getParameter("agmt_no");
			String cont_no=request.getParameter("cont_no")==null?"0":request.getParameter("cont_no");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String cont_start_dt=request.getParameter("cont_start_dt")==null?"":request.getParameter("cont_start_dt");
			String cont_end_dt = request.getParameter("cont_end_dt")==null?"":request.getParameter("cont_end_dt");
			String account=request.getParameter("account")==null?"":request.getParameter("account");
			
			String from_dt = request.getParameter("from_dt")==null?"":request.getParameter("from_dt");
			String to_dt=request.getParameter("to_dt")==null?"":request.getParameter("to_dt");
			String tcq=request.getParameter("tcq")==null?"":request.getParameter("tcq");
			String dcq=request.getParameter("dcq")==null?"":request.getParameter("dcq");
			String remark=request.getParameter("remark")==null?"":request.getParameter("remark");
			
			String seq_no=request.getParameter("seq_no")==null?"":request.getParameter("seq_no");
			String display_map_id = request.getParameter("display_map_id")==null?"":request.getParameter("display_map_id");
			
			int no_of_days = utilDate.getDays(to_dt, from_dt);
			if(!tcq.equals("") && !to_dt.equals("") && !from_dt.equals(""))
			{
				dcq=nf.format(Double.parseDouble(tcq)/no_of_days);
			}
			
			if(opration.equals("MODIFY"))
			{
				if(!seq_no.equals(""))
				{
					query="UPDATE FMS_MR_CONT_TAQ_DTL SET FROM_DT=TO_DATE(?,'DD/MM/YYYY'),TO_DT=TO_DATE(?,'DD/MM/YYYY'),"
							+ "ASED_TCQ=?,ASED_DCQ=?,REMARK=?,MODIFY_BY=?, MODIFY_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
							+ "AND AGMT_NO=? AND CONT_NO=? AND SEQ_NO=?";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, from_dt);
					stmt.setString(2, to_dt);
					stmt.setString(3, tcq);
					stmt.setString(4, dcq);
					stmt.setString(5, remark);
					stmt.setString(6, emp_cd);
					stmt.setString(7, comp_cd);
					stmt.setString(8, counterparty_cd);
					stmt.setString(9, contract_type);
					stmt.setString(10, agmt_no);
					stmt.setString(11, cont_no);
					stmt.setString(12, seq_no);
					stmt.executeQuery();
					msg = "Successful! - Contract Variable TAQ for "+display_map_id+" Modified Successfully!";
					msg_type="S";
						
					stmt.close();
				}
			}
			else
			{
				query="SELECT NVL(MAX(SEQ_NO),0) "
						+ "FROM FMS_MR_CONT_TAQ_DTL "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONTRACT_TYPE=? "
						+ "AND AGMT_NO=? AND CONT_NO=? ";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, contract_type);
				stmt.setString(4, agmt_no);
				stmt.setString(5, cont_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					seq_no=""+(rset.getInt(1)+1);
				}
				else
				{
					seq_no="1";
				}
				rset.close();
				stmt.close();
				
				
				query="INSERT INTO FMS_MR_CONT_TAQ_DTL(COMPANY_CD,COUNTERPARTY_CD,CONTRACT_TYPE,AGMT_NO,CONT_NO,SEQ_NO,"
						+ "FROM_DT,TO_DT,ASED_TCQ,ASED_DCQ,REMARK,ENT_BY,ENT_DT) "
						+ "VALUES(?,?,?,?,?,?,"
						+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,SYSDATE)";
				stmt1 = dbcon.prepareStatement(query);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, contract_type);
				stmt1.setString(4, agmt_no);
				stmt1.setString(5, cont_no);
				stmt1.setString(6, seq_no);
				stmt1.setString(7, from_dt);
				stmt1.setString(8, to_dt);
				stmt1.setString(9, tcq);
				stmt1.setString(10, dcq);
				stmt1.setString(11, remark);
				stmt1.setString(12, emp_cd);
				stmt1.executeQuery();
				msg = "Successful! - Contract Variable TAQ for "+display_map_id+" Inserted Successfully!";
				msg_type="S";
					
				stmt1.close();
			}

			url = "../market_risk/frm_mr_var_taq_config.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+
					"&cont_no="+cont_no+"&agmt_no="+agmt_no+"&start_dt="+cont_start_dt+"&end_dt="+cont_end_dt+"&account="+account+
					"&contract_type="+contract_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(db_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg="Error in Exception! - Contract Variable TAQ Insert/Update Falied!";
			
			old_value="";
			new_value="";
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
