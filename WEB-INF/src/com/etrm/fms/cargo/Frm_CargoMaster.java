package com.etrm.fms.cargo;

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

import com.etrm.fms.mail.MailDelivery;
import com.etrm.fms.util.CommonVariable;
import com.etrm.fms.util.DateUtil;
import com.etrm.fms.util.RuntimeConf;
import com.etrm.fms.util.SystemErrorLogger;
import com.etrm.fms.util.UtilBean;
import com.etrm.fms.util.escapeSingleQuotes;

@WebServlet("/servlet/Frm_CargoMaster")
public class Frm_CargoMaster extends HttpServlet
{
	static String frm_src_file_name="Frm_CargoMaster.java";
	public static  Connection dbcon;
	
	public static String servletName = "Frm_CargoMaster";
	public static String option = "";
	public static String url = "";
	public static String msg = "";
	public static String msg_type = "";
	public static String err_msg = "";
	public static String approved_flag ="";
	
	private static String queryString = null;
	private static String query = null;
	private static String query1 = null;
	private static String query2 = null;
	private static PreparedStatement stmt = null;
	private static PreparedStatement stmt0 = null;
	private static PreparedStatement stmt1 = null;
	private static PreparedStatement stmt2 = null;
	private static PreparedStatement stmt3 = null;
	private static PreparedStatement stmt4 = null;
	private static PreparedStatement stmt5 = null;
	private static PreparedStatement stmt6 = null;
	private static PreparedStatement stmt_temp = null;
	
	private static ResultSet rset = null;
	private static ResultSet rset0 = null;
	private static ResultSet rset1 = null;
	private static ResultSet rset2 = null;
	private static ResultSet rset3 = null;
	private static ResultSet rset4 = null;
	private static ResultSet rset5 = null;
	private static ResultSet rset6 = null;
	private static ResultSet rset_temp = null;
	
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
	
	static NumberFormat nf = new DecimalFormat("###########0.00");
	static NumberFormat nf2 = new DecimalFormat("###########0.0000");
	static NumberFormat nf3 = new DecimalFormat("###########0.000000");
	static UtilBean utilBean = new UtilBean();
	static DateUtil utilDate = new DateUtil();
	static MailDelivery mailDelv = new MailDelivery();
	
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
					
					if(option.equalsIgnoreCase("MSP_AGREEMENT_MST"))
					{
						InsertUpdateAgreementDetail(request);
					}
					else if(option.equalsIgnoreCase("MSP_AGREEMENT_BILLING_DTL"))
					{
						InsertUpdateAgreementBillingDetail(request);
					}
					else if(option.equalsIgnoreCase("CN_MST"))
					{
						InsertUpdateCnDetail(request);
					}
					else if(option.equalsIgnoreCase("CN_CONT_FCC"))
					{
						FCCforCnDetail(request);
					}
					else if(option.equalsIgnoreCase("CN_BILLING_DTL"))
					{
						InsertUpdateCnBillingDetail(request);
					}
					else if(option.equalsIgnoreCase("CN_CARGO_DTL"))
					{
						InsertUpdateCnCargoDtls(request);
					}
					else if(option.equalsIgnoreCase("CARGO_NOMINATION_MST"))
					{
						InsertUpdateCargoNominationDetail(request);
					}
					else if(option.equalsIgnoreCase("MSPA_LIABILITY_CLAUSE"))
					{
						InsertUpdateMspaLiabilityDtls(request);
					}
					else if(option.equalsIgnoreCase("CN_LIABILITY_CLAUSE"))
					{
						InsertUpdateCnLiabilityDtls(request);
					}
					else if(option.equalsIgnoreCase("CARGO_ARRIVAL"))
					{
						InsertUpdateCargoArrivalDtls(request);
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
				if(rset0 != null){try {rset0.close();}catch(SQLException e){System.out.println("rset0 is not close " + e);}}
				if(rset1 != null){try {rset1.close();}catch(SQLException e){System.out.println("rset1 is not close " + e);}}
				if(rset2 != null){try {rset2.close();}catch(SQLException e){System.out.println("rset2 is not close " + e);}}
				if(rset3 != null){try {rset3.close();}catch(SQLException e){System.out.println("rset3 is not close " + e);}}
				if(rset4 != null){try {rset4.close();}catch(SQLException e){System.out.println("rset4 is not close " + e);}}
				if(rset5 != null){try {rset5.close();}catch(SQLException e){System.out.println("rset5 is not close " + e);}}
				if(rset6 != null){try {rset6.close();}catch(SQLException e){System.out.println("rset6 is not close " + e);}}
				if(rset_temp != null){try {rset_temp.close();}catch(SQLException e){System.out.println("rset_temp is not close " + e);}}
				if(stmt != null){try {stmt.close();}catch(SQLException e){System.out.println("stmt is not close " + e);}}
				if(stmt0 != null){try {stmt0.close();}catch(SQLException e){System.out.println("stmt0 is not close " + e);}}
				if(stmt1 != null){try {stmt1.close();}catch(SQLException e){System.out.println("stmt1 is not close " + e);}}
				if(stmt2 != null){try {stmt2.close();}catch(SQLException e){System.out.println("stmt2 is not close " + e);}}
				if(stmt3 != null){try {stmt3.close();}catch(SQLException e){System.out.println("stmt3 is not close " + e);}}
				if(stmt4 != null){try {stmt4.close();}catch(SQLException e){System.out.println("stmt4 is not close " + e);}}
				if(stmt5 != null){try {stmt5.close();}catch(SQLException e){System.out.println("stmt5 is not close " + e);}}
				if(stmt6 != null){try {stmt6.close();}catch(SQLException e){System.out.println("stmt6 is not close " + e);}}
				if(stmt_temp != null){try {stmt_temp.close();}catch(SQLException e){System.out.println("stmt_temp is not close " + e);}}
				if(dbcon != null){try {dbcon.close();}catch(SQLException e){System.out.println("conn is not close " + e);}}
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
	
	private void FCCforCnDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="FCCforCnDetail()";
		msg="";
		msg_type="";
		url="";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd); 
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String fcc_flg=request.getParameter("fcc_flg")==null?"N":request.getParameter("fcc_flg");
			String cont_status_flg = fcc_flg;
			
			String cont_name_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			
			query ="UPDATE FMS_TRADER_CN_MST SET FCC_FLAG=?,FCC_BY=?,FCC_DATE=SYSDATE, CONT_STATUS=? "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND CONT_REV=? "
					+ "AND AGMT_NO=? AND AGMT_REV=?";
			stmt=dbcon.prepareStatement(query);
			stmt.setString(1, fcc_flg);
			stmt.setString(2, emp_cd);
			stmt.setString(3, cont_status_flg);
			stmt.setString(4, comp_cd);
			stmt.setString(5, counterparty_cd);
			stmt.setString(6, cont_no);
			stmt.setString(7, cont_rev_no);
			stmt.setString(8, agmt_no);
			stmt.setString(9, agmt_rev_no);
			stmt.executeUpdate();
			
			stmt.close();
			
			//ADD LOG IN LOG TABLE
			int log_seq_no=1;
			queryString = "SELECT MAX(LOG_SEQ_NO) "
					+ "FROM LOG_TRADER_CN_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND AGMT_NO=? AND CONTRACT_TYPE=?";
			stmt0=dbcon.prepareStatement(queryString);
			stmt0.setString(1, comp_cd);
			stmt0.setString(2, counterparty_cd);
			stmt0.setString(3, cont_no);
			stmt0.setString(4, agmt_no);
			stmt0.setString(5, contract_type);
			rset0 = stmt0.executeQuery();
			if(rset0.next())
			{
				log_seq_no = (rset0.getInt(1)+1);
			}
			rset0.close();
			stmt0.close();
			
			query="INSERT INTO LOG_TRADER_CN_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_BASE,AGMT_TYP,CONTRACT_TYPE,LOG_SEQ_NO,LOG_BY,LOG_DT,CONT_NO,CONT_REV,"
					+ "CONT_NAME,CONT_REF_NO,CONT_STATUS,DDA_DT,DDA_TIME,SIGNING_DT,SIGNING_TIME,START_DT,END_DT,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,REV_DT,"
					+ "NUM_CARGO,DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,DEMURRAGE_RATE,DEMURRAGE_RATE_UNIT,ALW_LAYTIME_HRS,ALW_LAYTIME_MNS,MEASUREMENT,MEAS_STANDARD,"
					+ "MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,"
					+ "BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,DEMURRAGE,DAY_DEF_CLAUSE,DEMURRAGE_CLAUSE,MEASUREMENT_CLAUSE,OFF_SPEC_GAS_CLAUSE,"
					+ "FCC_FLAG,FCC_BY,FCC_DATE) "
					+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_BASE,AGMT_TYP,CONTRACT_TYPE,?,?,SYSDATE,CONT_NO,CONT_REV,"
					+ "CONT_NAME,CONT_REF_NO,CONT_STATUS,DDA_DT,DDA_TIME,SIGNING_DT,SIGNING_TIME,START_DT,END_DT,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,REV_DT,"
					+ "NUM_CARGO,DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,DEMURRAGE_RATE,DEMURRAGE_RATE_UNIT,ALW_LAYTIME_HRS,ALW_LAYTIME_MNS,MEASUREMENT,MEAS_STANDARD,"
					+ "MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,"
					+ "BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,DEMURRAGE,DAY_DEF_CLAUSE,DEMURRAGE_CLAUSE,MEASUREMENT_CLAUSE,OFF_SPEC_GAS_CLAUSE,"
					+ "FCC_FLAG,FCC_BY,FCC_DATE "
					+ "FROM FMS_TRADER_CN_MST "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND CONT_REV=? "
					+ "AND AGMT_NO=? AND AGMT_REV=? "
					+ "AND CONTRACT_TYPE=?";
			stmt=dbcon.prepareStatement(query);
			stmt.setInt(1, log_seq_no);
			stmt.setString(2, emp_cd);
			stmt.setString(3, comp_cd);
			stmt.setString(4, counterparty_cd);
			stmt.setString(5, cont_no);
			stmt.setString(6, cont_rev_no);
			stmt.setString(7, agmt_no);
			stmt.setString(8, agmt_rev_no);
			stmt.setString(9, contract_type);
			stmt.executeQuery();
			
			stmt.close();
			
			
			if(fcc_flg.equals("Y"))
			{
				msg = "Successful! - FCC Approved for CN "+cont_name_map+" for "+counterparty_abbr+" Successfully!";
			}
			else
			{
				msg = "Successful! - FCC Disapproved for CN "+cont_name_map+" for "+counterparty_abbr+" Successfully!";
			}
			msg_type="S";
			
			url = "../cargo/frm_confirm_notice_mst_fcc.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+
					"&contract_type="+contract_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! FCC Approval Detail submission Failed!";
		}
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}

	private void InsertUpdateCargoArrivalDtls(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateCargoArrivalDtls()";
		msg="";
		msg_type="";
		url="";
		String cargo_name = "";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String allocation_status = request.getParameter("allocation_status")==null?"":request.getParameter("allocation_status");
			String bl_opration = request.getParameter("bl_opration")==null?"":request.getParameter("bl_opration");
			String boe_opration = request.getParameter("boe_opration")==null?"":request.getParameter("boe_opration");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String agmt_type=request.getParameter("agmt_type")==null?"M":request.getParameter("agmt_type");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String cargo_no=request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
			String ship_cd = request.getParameter("vessel_cd")==null?"":request.getParameter("vessel_cd");
			String ship_name = request.getParameter("vessel_nm")==null?"":request.getParameter("vessel_nm");
			String act_arival_dt = request.getParameter("act_arival_dt")==null?"":request.getParameter("act_arival_dt");
			String booked_dt = request.getParameter("booked_dt")==null?"":request.getParameter("booked_dt");
			String booked_time = request.getParameter("booked_time")==null?"":request.getParameter("booked_time");
			String float_dt = request.getParameter("float_dt")==null?"":request.getParameter("float_dt");
			String float_time = request.getParameter("float_time")==null?"":request.getParameter("float_time");
			String pio_ob_dt = request.getParameter("pio_ob_dt")==null?"":request.getParameter("pio_ob_dt");
			String pio_ob_time = request.getParameter("pio_ob_time")==null?"":request.getParameter("pio_ob_time");
			String uac_dt = request.getParameter("uac_dt")==null?"":request.getParameter("uac_dt");
			String uac_time = request.getParameter("uac_time")==null?"":request.getParameter("uac_time");
			String uadc_dt = request.getParameter("uadc_dt")==null?"":request.getParameter("uadc_dt");
			String uadc_time = request.getParameter("uadc_time")==null?"":request.getParameter("uadc_time");
			String departure_dt = request.getParameter("departure_dt")==null?"":request.getParameter("departure_dt");
			String departure_time = request.getParameter("departure_time")==null?"":request.getParameter("departure_time");
			String cargo_details_notes = request.getParameter("cargo_details_notes")==null?"":request.getParameter("cargo_details_notes");
			String qq_cer_no = request.getParameter("qq_cer_no")==null?"":request.getParameter("qq_cer_no");
			String qq_cer_dt = request.getParameter("qq_cer_dt")==null?"":request.getParameter("qq_cer_dt");
			String net_unloaded_qunt = request.getParameter("net_unloaded_qunt")==null?"":request.getParameter("net_unloaded_qunt");
			String net_unloaded_qunt_mt = request.getParameter("net_unloaded_qunt_mt")==null?"":request.getParameter("net_unloaded_qunt_mt");
			String net_unloaded_qunt_m3 = request.getParameter("net_unloaded_qunt_m3")==null?"":request.getParameter("net_unloaded_qunt_m3");
			String dens_material = request.getParameter("dens_material")==null?"":request.getParameter("dens_material");
			String qq_ghv = request.getParameter("qq_ghv")==null?"":request.getParameter("qq_ghv");
			String qq_gcv = request.getParameter("qq_gcv")==null?"":request.getParameter("qq_gcv");
			String app_lab_notes = request.getParameter("app_lab_notes")==null?"":request.getParameter("app_lab_notes");
			
			String all_fast_dt = request.getParameter("all_fast_dt")==null?"":request.getParameter("all_fast_dt");
			String all_fast_time = request.getParameter("all_fast_time")==null?"":request.getParameter("all_fast_time");
			String custom_start_dt = request.getParameter("custom_start_dt")==null?"":request.getParameter("custom_start_dt");
			String custom_start_time = request.getParameter("custom_start_time")==null?"":request.getParameter("custom_start_time");
			String custom_end_dt = request.getParameter("custom_end_dt")==null?"":request.getParameter("custom_end_dt");
			String custom_end_time = request.getParameter("custom_end_time")==null?"":request.getParameter("custom_end_time");
			
			String boe_no = request.getParameter("boe_no")==null?"":request.getParameter("boe_no");
			String boe_ref = request.getParameter("boe_ref")==null?"":request.getParameter("boe_ref");
			String disp_boe_number = "BOE"+(utilBean.PrePaddingZero(boe_no,2));
			String business_unit = request.getParameter("business_unit")==null?"":request.getParameter("business_unit");
			String trader_plant = request.getParameter("trader_plant")==null?"":request.getParameter("trader_plant");
			String boe_expected_qunt = request.getParameter("boe_expected_qunt")==null?"":request.getParameter("boe_expected_qunt");
			String boe_expected_qunt_unit = request.getParameter("boe_expected_qunt_unit")==null?"":request.getParameter("boe_expected_qunt_unit");
			String be_dt = request.getParameter("be_dt")==null?"":request.getParameter("be_dt");
			String mt = request.getParameter("mt")==null?"":request.getParameter("mt");
			String m3 = request.getParameter("m3")==null?"":request.getParameter("m3");
			String actual_qunt = request.getParameter("actual_qunt")==null?"":request.getParameter("actual_qunt");
			String actual_qunt_unit = request.getParameter("actual_qunt_unit")==null?"":request.getParameter("actual_qunt_unit");
			String actual_mt = request.getParameter("actual_mt")==null?"":request.getParameter("actual_mt");
			String actual_m3 = request.getParameter("actual_m3")==null?"":request.getParameter("actual_m3");
			String CD_flg = request.getParameter("CD_Checkbox")==null?"":request.getParameter("CD_Checkbox");
			String port_code = request.getParameter("port_code")==null?"":request.getParameter("port_code");
			
			String bl_no = request.getParameter("bl_no")==null?"":request.getParameter("bl_no");
			String bill_of_lad_no = request.getParameter("bill_of_lad_no")==null?"":request.getParameter("bill_of_lad_no");
			String disp_bl_number = "BL"+(utilBean.PrePaddingZero(bl_no,2));
			String bill_of_lad_dt = request.getParameter("bill_of_lad_dt")==null?"":request.getParameter("bill_of_lad_dt");
			String imp_dept_no = request.getParameter("imp_dept_no")==null?"":request.getParameter("imp_dept_no");
			String imp_code = request.getParameter("imp_code")==null?"":request.getParameter("imp_code");
			String endors_dt = request.getParameter("endors_dt")==null?"":request.getParameter("endors_dt");
			String app_bill_notes = request.getParameter("app_bill_notes")==null?"":request.getParameter("app_bill_notes");
			
			String expected_qunt = request.getParameter("expected_qunt")==null?"":request.getParameter("expected_qunt");
			String cont_name = request.getParameter("confirm_no")==null?"":request.getParameter("confirm_no");
			cargo_name = request.getParameter("cargo_number")==null?"":request.getParameter("cargo_number");
			String alloc_rev = request.getParameter("alloc_rev")==null?"":request.getParameter("alloc_rev");
			
			String boe_provisional_price = request.getParameter("boe_provisional_price")==null?"":request.getParameter("boe_provisional_price");
			String boe_provisional_price_unit = request.getParameter("boe_provisional_price_unit")==null?"":request.getParameter("boe_provisional_price_unit");
			String boe_final_price = request.getParameter("boe_final_price")==null?"":request.getParameter("boe_final_price");
			String boe_final_price_unit = request.getParameter("boe_final_price_unit")==null?"":request.getParameter("boe_final_price_unit");
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !cont_no.equals("") && !agmt_type.equals("") && !contract_type.equals("") && !cargo_no.equals(""))
			{
				int c = 0, bl_diff = 0,boe_diff=0;
				String q = "SELECT P.COUNT_NOM_BL-Q.COUNT_ALLOC_BL,R.COUNT_NOM_BOE-S.COUNT_ALLOC_BOE FROM  "
						+ "(SELECT COUNT(*) COUNT_NOM_BL "
						+ "FROM FMS_BUY_CARGO_NOM_BL A  "
						+ "  WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? AND AGMT_NO=?  "
						+ "  AND CONTRACT_TYPE=? AND CONT_NO=? AND CARGO_NO=? "
						+ "  AND NOM_REV=(SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM_BL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD  "
						+ "	AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE  "
						+ "	AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CARGO_NO=B.CARGO_NO AND A.BL_NO=B.BL_NO)) P, "
						+ "(SELECT COUNT(*) COUNT_ALLOC_BL "
						+ "FROM FMS_BUY_CARGO_ALLOC_BL A  "
						+ "  WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? AND AGMT_NO=?  "
						+ "  AND CONTRACT_TYPE=? AND CONT_NO=? AND CARGO_NO=?  "
						+ "  AND ALLOC_REV=(SELECT MAX(B.ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC_BL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD  "
						+ "  AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE  "
						+ "  AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CARGO_NO=B.CARGO_NO AND A.BL_NO=B.BL_NO)) Q, "
						+ "(SELECT COUNT(*) COUNT_NOM_BOE "
						+ "FROM FMS_BUY_CARGO_NOM_BOE A  "
						+ "  WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? AND AGMT_NO=?  "
						+ "  AND CONTRACT_TYPE=? AND CONT_NO=? AND CARGO_NO=?  "
						+ "  AND NOM_REV=(SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM_BOE B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD  "
						+ "  AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE  "
						+ "  AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CARGO_NO=B.CARGO_NO AND A.BOE_NO=B.BOE_NO) ) R, "
						+ "(SELECT COUNT(*) COUNT_ALLOC_BOE "
						+ "FROM FMS_BUY_CARGO_ALLOC_BOE A  "
						+ "  WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? AND AGMT_NO=?  "
						+ "  AND CONTRACT_TYPE=? AND CONT_NO=? AND CARGO_NO=?  "
						+ "  AND ALLOC_REV=(SELECT MAX(B.ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC_BOE B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD  "
						+ "  AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE  "
						+ "  AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CARGO_NO=B.CARGO_NO AND A.BOE_NO=B.BOE_NO)) S";
				
				stmt3=dbcon.prepareStatement(q);
				stmt3.setString(++c, comp_cd);
				stmt3.setString(++c, counterparty_cd);
				stmt3.setString(++c, agmt_type);
				stmt3.setString(++c, agmt_no);
				stmt3.setString(++c, contract_type);
				stmt3.setString(++c, cont_no);
				stmt3.setString(++c, cargo_no);
				stmt3.setString(++c, comp_cd);
				stmt3.setString(++c, counterparty_cd);
				stmt3.setString(++c, agmt_type);
				stmt3.setString(++c, agmt_no);
				stmt3.setString(++c, contract_type);
				stmt3.setString(++c, cont_no);
				stmt3.setString(++c, cargo_no);
				stmt3.setString(++c, comp_cd);
				stmt3.setString(++c, counterparty_cd);
				stmt3.setString(++c, agmt_type);
				stmt3.setString(++c, agmt_no);
				stmt3.setString(++c, contract_type);
				stmt3.setString(++c, cont_no);
				stmt3.setString(++c, cargo_no);
				stmt3.setString(++c, comp_cd);
				stmt3.setString(++c, counterparty_cd);
				stmt3.setString(++c, agmt_type);
				stmt3.setString(++c, agmt_no);
				stmt3.setString(++c, contract_type);
				stmt3.setString(++c, cont_no);
				stmt3.setString(++c, cargo_no);
				rset3=stmt3.executeQuery();
				if(rset3.next())
				{
					bl_diff=rset3.getInt(1);
					boe_diff=rset3.getInt(2);
				}
				rset3.close();
				stmt3.close();
				
				if(!allocation_status.equals("Pending"))
				{
					if(boe_diff!=0)
					{
						int cnt3=0;
						
						query2 = "INSERT INTO FMS_BUY_CARGO_ALLOC_BOE(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,ALLOC_REV,"
								+ "BOE_NO,BU_SEQ,PLANT_SEQ,ENT_BY,ENT_DT,FLAG,CUSTOM_DUTY,LOAD_PORT) "
								+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,?,"
								+ "BOE_NO,BU_SEQ,PLANT_SEQ,ENT_BY,ENT_DT,FLAG,CUSTOM_DUTY,LOAD_PORT "
								+ "FROM FMS_BUY_CARGO_NOM_BOE A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? AND AGMT_NO=? "
								+ "AND CONTRACT_TYPE=? AND CONT_NO=? AND CARGO_NO=? "
								+ "AND NOM_REV=(SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM_BOE B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
								+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CARGO_NO=B.CARGO_NO) "
								+ "AND NOT EXISTS ( SELECT 1 FROM FMS_BUY_CARGO_ALLOC_BOE C WHERE A.COMPANY_CD = C.COMPANY_CD AND A.COUNTERPARTY_CD = C.COUNTERPARTY_CD "
								+ "AND A.AGMT_TYPE = C.AGMT_TYPE AND A.AGMT_NO = C.AGMT_NO AND A.AGMT_REV = C.AGMT_REV  AND A.CONTRACT_TYPE = C.CONTRACT_TYPE "
								+ "AND A.CONT_NO = C.CONT_NO AND A.CONT_REV = C.CONT_REV AND A.CARGO_NO = C.CARGO_NO AND A.BOE_NO=C.BOE_NO)";
						stmt2 =dbcon.prepareStatement(query2);
						stmt2.setString(++cnt3, alloc_rev);
						stmt2.setString(++cnt3, comp_cd);
						stmt2.setString(++cnt3, counterparty_cd);
						stmt2.setString(++cnt3, agmt_type);
						stmt2.setString(++cnt3, agmt_no);
						stmt2.setString(++cnt3, contract_type);
						stmt2.setString(++cnt3, cont_no);
						stmt2.setString(++cnt3, cargo_no);
						stmt2.executeUpdate();						
						stmt2.close();
					}
					
					if(bl_diff!=0)
					{
						int cnt3=0;
						
						query2 = "INSERT INTO FMS_BUY_CARGO_ALLOC_BL(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,ALLOC_REV,"
								+ "BL_NO,ENT_BY,ENT_DT,FLAG) "
								+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,?,"
								+ "BL_NO,ENT_BY,ENT_DT,FLAG "
								+ "FROM FMS_BUY_CARGO_NOM_BL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? AND AGMT_NO=? "
								+ "AND CONTRACT_TYPE=? AND CONT_NO=? AND CARGO_NO=? "
								+ "AND NOM_REV=(SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM_BL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
								+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CARGO_NO=B.CARGO_NO) "
								+ "AND NOT EXISTS ( SELECT 1 FROM FMS_BUY_CARGO_ALLOC_BL C WHERE A.COMPANY_CD = C.COMPANY_CD AND A.COUNTERPARTY_CD = C.COUNTERPARTY_CD "
								+ "AND A.AGMT_TYPE = C.AGMT_TYPE AND A.AGMT_NO = C.AGMT_NO AND A.AGMT_REV = C.AGMT_REV  AND A.CONTRACT_TYPE = C.CONTRACT_TYPE "
								+ "AND A.CONT_NO = C.CONT_NO AND A.CONT_REV = C.CONT_REV AND A.CARGO_NO = C.CARGO_NO AND A.BL_NO=C.BL_NO)";
						stmt2 =dbcon.prepareStatement(query2);
						stmt2.setString(++cnt3, alloc_rev);
						stmt2.setString(++cnt3, comp_cd);
						stmt2.setString(++cnt3, counterparty_cd);
						stmt2.setString(++cnt3, agmt_type);
						stmt2.setString(++cnt3, agmt_no);
						stmt2.setString(++cnt3, contract_type);
						stmt2.setString(++cnt3, cont_no);
						stmt2.setString(++cnt3, cargo_no);
						stmt2.executeUpdate();
						
						stmt2.close();
					}
				}
				
				if(opration.equals("BOE_UPDATE"))
				{
					
					int cnt=0;
					query1 = "UPDATE FMS_BUY_CARGO_ALLOC_BOE SET BU_SEQ=?,PLANT_SEQ=?,BOE_REF=?,BOE_DT=TO_DATE(?,'DD/MM/YYYY'), "
							+ "ACT_BOE_QTY=?,ACT_BOE_QTY_UNIT=?,ACT_QTY_MT=?,ACT_QTY_SCM=?,CUSTOM_DUTY=?,LOAD_PORT=?,"
							+ "BOE_PROVISIONAL_PRICE=?,BOE_PROVISIONAL_PRICE_UNIT=?,BOE_FINAL_PRICE=?,BOE_FINAL_PRICE_UNIT=? "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? "
							+ "AND CONT_REV=? AND CARGO_NO=? AND ALLOC_REV=? AND BOE_NO=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, business_unit);
					stmt1.setString(++cnt, trader_plant);
					stmt1.setString(++cnt, boe_ref);
					stmt1.setString(++cnt, be_dt);
					stmt1.setString(++cnt, actual_qunt);
					stmt1.setString(++cnt, actual_qunt_unit);
					stmt1.setString(++cnt, actual_mt);
					stmt1.setString(++cnt, actual_m3);
					stmt1.setString(++cnt, CD_flg);
					stmt1.setString(++cnt, port_code);
					stmt1.setString(++cnt, boe_provisional_price);
					stmt1.setString(++cnt, boe_provisional_price_unit);
					stmt1.setString(++cnt, boe_final_price);
					stmt1.setString(++cnt, boe_final_price_unit);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, counterparty_cd);
					stmt1.setString(++cnt, agmt_type);
					stmt1.setString(++cnt, agmt_no);
					stmt1.setString(++cnt, agmt_rev_no);
					stmt1.setString(++cnt, contract_type);
					stmt1.setString(++cnt, cont_no);
					stmt1.setString(++cnt, cont_rev_no);
					stmt1.setString(++cnt, cargo_no);
					stmt1.setString(++cnt, alloc_rev);
					stmt1.setString(++cnt, boe_no);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					msg = "Successful! - Cargo "+cargo_name+" BOE "+disp_boe_number+" Detail for "+counterparty_abbr+" Modified Successfully!";
					msg_type = "S";
					
				}
				else if(opration.equals("BL_UPDATE"))
				{
					int cnt=0;
					
					query1 = "UPDATE FMS_BUY_CARGO_ALLOC_BL SET BL_REF=?,BL_DT=TO_DATE(?,'DD/MM/YYYY'),IMPORT_DEPT_SNO=?,IMPORT_CD=?, "
							+ "ENDORSE_DT=TO_DATE(?,'DD/MM/YYYY'),REMARK=? "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? "
							+ "AND CONT_REV=? AND CARGO_NO=? AND ALLOC_REV=? AND BL_NO=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, bill_of_lad_no);
					stmt1.setString(++cnt, bill_of_lad_dt);
					stmt1.setString(++cnt, imp_dept_no);
					stmt1.setString(++cnt, imp_code);
					stmt1.setString(++cnt, endors_dt);
					stmt1.setString(++cnt, app_bill_notes);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, counterparty_cd);
					stmt1.setString(++cnt, agmt_type);
					stmt1.setString(++cnt, agmt_no);
					stmt1.setString(++cnt, agmt_rev_no);
					stmt1.setString(++cnt, contract_type);
					stmt1.setString(++cnt, cont_no);
					stmt1.setString(++cnt, cont_rev_no);
					stmt1.setString(++cnt, cargo_no);
					stmt1.setString(++cnt, alloc_rev);
					stmt1.setString(++cnt, bl_no);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					msg = "Successful! - Cargo "+cargo_name+" BL "+disp_bl_number+" Detail for "+counterparty_abbr+" Modified Successfully!";
					msg_type = "S";
				}
				else
				{
					query="SELECT COUNT(*),MAX(ALLOC_REV) "
							+ "FROM FMS_BUY_CARGO_ALLOC "
							+ "WHERE CONT_NO=? AND COMPANY_CD=? "
							+ "AND CONTRACT_TYPE=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_TYPE=? AND CARGO_NO=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, cont_no);
					stmt.setString(2, comp_cd);
					stmt.setString(3, contract_type);
					stmt.setString(4, counterparty_cd);
					stmt.setString(5, agmt_no);
					stmt.setString(6, agmt_type);
					stmt.setString(7, cargo_no);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						if(rset.getInt(1)>0)
						{
							alloc_rev = ""+(rset.getInt(2)+1);
						}
						else
						{
							alloc_rev = "0";
						}
					}
					else
					{
						alloc_rev = "0";
					}
					rset.close();
					stmt.close();
					
					int cnt=0;
					query1 = "INSERT INTO FMS_BUY_CARGO_ALLOC(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,ALLOC_REV,SHIP_CD,"
							+ "ACT_ARRV_DT,BOOKED_DT,BOOKED_TIME,FLOAT_DT,FLOAT_TIME,PILOT_ON_BOARD_DT,PILOT_ON_BOARD_TIME,UNLOAD_ARM_CON_DT,UNLOAD_ARM_CON_TIME,UNLOAD_ARM_DIS_CON_DT,"
							+ "UNLOAD_ARM_DIS_CON_TIME,DISCHARGE_DT,DISCHARGE_TIME,REMARK,QQ_NO,QQ_DT,QQ_QTY_MMBTU,QQ_QQ_QTY_MT,QQ_QQ_QTY_SCM,QQ_DENSITY,QQ_GHV,QQ_GCV,QQ_REMARK,"
							+ "ACT_QTY_MMBTU,ENT_BY,ENT_DT,ALL_FAST_DT,ALL_FAST_TIME,CUSTOME_CLEARANCE_START_DT,CUSTOME_CLEARANCE_START_TIME,CUSTOME_CLEARANCE_END_DT,CUSTOME_CLEARANCE_END_TIME) "
							+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,"
							+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),"
							+ "?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?,SYSDATE,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?)";
					stmt1 =dbcon.prepareStatement(query1);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, counterparty_cd);
					stmt1.setString(++cnt, agmt_type);
					stmt1.setString(++cnt, agmt_no);
					stmt1.setString(++cnt, agmt_rev_no);
					stmt1.setString(++cnt, contract_type);
					stmt1.setString(++cnt, cont_no);
					stmt1.setString(++cnt, cont_rev_no);
					stmt1.setString(++cnt, cargo_no);
					stmt1.setString(++cnt, alloc_rev);
					stmt1.setString(++cnt, ship_cd);
					stmt1.setString(++cnt, act_arival_dt);
					stmt1.setString(++cnt, booked_dt);
					stmt1.setString(++cnt, booked_time);
					stmt1.setString(++cnt, float_dt);
					stmt1.setString(++cnt, float_time);
					stmt1.setString(++cnt, pio_ob_dt);
					stmt1.setString(++cnt, pio_ob_time);
					stmt1.setString(++cnt, uac_dt);
					stmt1.setString(++cnt, uac_time);
					stmt1.setString(++cnt, uadc_dt);
					stmt1.setString(++cnt, uadc_time);
					stmt1.setString(++cnt, departure_dt);
					stmt1.setString(++cnt, departure_time);
					stmt1.setString(++cnt, cargo_details_notes);
					stmt1.setString(++cnt, qq_cer_no);
					stmt1.setString(++cnt, qq_cer_dt);
					stmt1.setString(++cnt, net_unloaded_qunt);
					stmt1.setString(++cnt, net_unloaded_qunt_mt);
					stmt1.setString(++cnt, net_unloaded_qunt_m3);
					stmt1.setString(++cnt, dens_material);
					stmt1.setString(++cnt, qq_ghv);
					stmt1.setString(++cnt, qq_gcv);
					stmt1.setString(++cnt, app_lab_notes);
					stmt1.setString(++cnt, expected_qunt);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, all_fast_dt);
					stmt1.setString(++cnt, all_fast_time);
					stmt1.setString(++cnt, custom_start_dt);
					stmt1.setString(++cnt, custom_start_time);
					stmt1.setString(++cnt, custom_end_dt);
					stmt1.setString(++cnt, custom_end_time);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					if(!allocation_status.equals("Pending"))
					{
						int cnt1=0;
						query2 = "INSERT INTO FMS_BUY_CARGO_ALLOC_BL(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,ALLOC_REV,"
								+ "BL_NO,BL_REF,BL_DT,IMPORT_DEPT_SNO,IMPORT_CD,ENDORSE_DT,REMARK,ENT_BY,ENT_DT,FLAG) "
								+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,?,"
								+ "BL_NO,BL_REF,BL_DT,IMPORT_DEPT_SNO,IMPORT_CD,ENDORSE_DT,REMARK,ENT_BY,ENT_DT,FLAG "
								+ "FROM FMS_BUY_CARGO_ALLOC_BL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? AND AGMT_NO=? "
								+ "AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? AND CONT_REV=? AND CARGO_NO=? "
								+ "AND ALLOC_REV=(SELECT MAX(B.ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC_BL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
								+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CARGO_NO=B.CARGO_NO AND A.BL_NO=B.BL_NO)";
						stmt2 =dbcon.prepareStatement(query2);
						stmt2.setString(++cnt1, alloc_rev);
						stmt2.setString(++cnt1, comp_cd);
						stmt2.setString(++cnt1, counterparty_cd);
						stmt2.setString(++cnt1, agmt_type);
						stmt2.setString(++cnt1, agmt_no);
						stmt2.setString(++cnt1, agmt_rev_no);
						stmt2.setString(++cnt1, contract_type);
						stmt2.setString(++cnt1, cont_no);
						stmt2.setString(++cnt1, cont_rev_no);
						stmt2.setString(++cnt1, cargo_no);
						stmt2.executeUpdate();
						
						stmt2.close();
						
						int cnt2=0;
						query2 = "INSERT INTO FMS_BUY_CARGO_ALLOC_BOE(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,ALLOC_REV,"
								+ "BOE_NO,BU_SEQ,PLANT_SEQ,BOE_REF,BOE_DT,ACT_BOE_QTY,ACT_BOE_QTY_UNIT,ACT_QTY_MT,BOE_PROVISIONAL_PRICE,BOE_PROVISIONAL_PRICE_UNIT,BOE_FINAL_PRICE,BOE_FINAL_PRICE_UNIT, "
								+ "ACT_QTY_SCM,CUSTOM_DUTY,LOAD_PORT,ENT_BY,ENT_DT,FLAG) "
								+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,?,"
								+ "BOE_NO,BU_SEQ,PLANT_SEQ,BOE_REF,BOE_DT,ACT_BOE_QTY,ACT_BOE_QTY_UNIT,ACT_QTY_MT,BOE_PROVISIONAL_PRICE,BOE_PROVISIONAL_PRICE_UNIT,BOE_FINAL_PRICE,BOE_FINAL_PRICE_UNIT, "
								+ "ACT_QTY_SCM,CUSTOM_DUTY,LOAD_PORT,ENT_BY,ENT_DT,FLAG "
								+ "FROM FMS_BUY_CARGO_ALLOC_BOE A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? AND AGMT_NO=? "
								+ "AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? AND CONT_REV=? AND CARGO_NO=? "
								+ "AND ALLOC_REV=(SELECT MAX(B.ALLOC_REV) FROM FMS_BUY_CARGO_ALLOC_BOE B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
								+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CARGO_NO=B.CARGO_NO)";
						stmt2 =dbcon.prepareStatement(query2);
						stmt2.setString(++cnt2, alloc_rev);
						stmt2.setString(++cnt2, comp_cd);
						stmt2.setString(++cnt2, counterparty_cd);
						stmt2.setString(++cnt2, agmt_type);
						stmt2.setString(++cnt2, agmt_no);
						stmt2.setString(++cnt2, agmt_rev_no);
						stmt2.setString(++cnt2, contract_type);
						stmt2.setString(++cnt2, cont_no);
						stmt2.setString(++cnt2, cont_rev_no);
						stmt2.setString(++cnt2, cargo_no);
						stmt2.executeUpdate();
						
						stmt2.close();
						
					}
					else if(allocation_status.equals("Pending"))
					{
						int cnt1=0;
						query2 = "INSERT INTO FMS_BUY_CARGO_ALLOC_BL(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,ALLOC_REV,"
								+ "BL_NO,ENT_BY,ENT_DT,FLAG) "
								+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,?,"
								+ "BL_NO,ENT_BY,ENT_DT,FLAG "
								+ "FROM FMS_BUY_CARGO_NOM_BL A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? AND AGMT_NO=? "
								+ "AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? AND CONT_REV=? AND CARGO_NO=? "
								+ "AND NOM_REV=(SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM_BL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
								+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CARGO_NO=B.CARGO_NO)";
						stmt2 =dbcon.prepareStatement(query2);
						stmt2.setString(++cnt1, alloc_rev);
						stmt2.setString(++cnt1, comp_cd);
						stmt2.setString(++cnt1, counterparty_cd);
						stmt2.setString(++cnt1, agmt_type);
						stmt2.setString(++cnt1, agmt_no);
						stmt2.setString(++cnt1, agmt_rev_no);
						stmt2.setString(++cnt1, contract_type);
						stmt2.setString(++cnt1, cont_no);
						stmt2.setString(++cnt1, cont_rev_no);
						stmt2.setString(++cnt1, cargo_no);
						stmt2.executeUpdate();
						
						stmt2.close();
						
						
						int cnt2=0;
						query2 = "INSERT INTO FMS_BUY_CARGO_ALLOC_BOE(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,ALLOC_REV,"
								+ "BOE_NO,BU_SEQ,PLANT_SEQ,ENT_BY,ENT_DT,FLAG,CUSTOM_DUTY,LOAD_PORT) "
								+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,?,"
								+ "BOE_NO,BU_SEQ,PLANT_SEQ,ENT_BY,ENT_DT,FLAG,CUSTOM_DUTY,LOAD_PORT "
								+ "FROM FMS_BUY_CARGO_NOM_BOE A "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_TYPE=? AND AGMT_NO=? "
								+ "AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? AND CONT_REV=? AND CARGO_NO=? "
								+ "AND NOM_REV=(SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM_BOE B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
								+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
								+ "AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CARGO_NO=B.CARGO_NO)";
						stmt2 =dbcon.prepareStatement(query2);
						stmt2.setString(++cnt2, alloc_rev);
						stmt2.setString(++cnt2, comp_cd);
						stmt2.setString(++cnt2, counterparty_cd);
						stmt2.setString(++cnt2, agmt_type);
						stmt2.setString(++cnt2, agmt_no);
						stmt2.setString(++cnt2, agmt_rev_no);
						stmt2.setString(++cnt2, contract_type);
						stmt2.setString(++cnt2, cont_no);
						stmt2.setString(++cnt2, cont_rev_no);
						stmt2.setString(++cnt2, cargo_no);
						stmt2.executeUpdate();
						
						stmt2.close();
					}
					
					msg = "Successful! - "+cargo_name+" Cargo Arrival Detail for "+counterparty_abbr+" submitted Successfully!";
					msg_type = "S";
				}
				
				int cnt=0;
				query = "SELECT COUNT(A.ACT_BOE_QTY),B.QQ_QTY_MMBTU "
						+ "FROM FMS_BUY_CARGO_ALLOC_BOE A , FMS_BUY_CARGO_ALLOC B "
						+ "WHERE A.COMPANY_CD=? AND B.COMPANY_CD=? "
						+ "AND A.COUNTERPARTY_CD=? AND B.COUNTERPARTY_CD=? "
						+ "AND A.CONT_NO=? AND B.CONT_NO=? "
						+ "AND A.CONTRACT_TYPE=? AND B.CONTRACT_TYPE=? "
						+ "AND A.AGMT_NO=? AND B.AGMT_NO=? "
						+ "AND A.AGMT_TYPE=? AND B.AGMT_TYPE=? "
						+ "AND A.CARGO_NO=? AND B.CARGO_NO=? "
						+ "AND A.ALLOC_REV=? AND B.ALLOC_REV=? "
						+ "GROUP BY ACT_BOE_QTY,QQ_QTY_MMBTU";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(++cnt, comp_cd);
				stmt.setString(++cnt, comp_cd);
				stmt.setString(++cnt, counterparty_cd);
				stmt.setString(++cnt, counterparty_cd);
				stmt.setString(++cnt, cont_no);
				stmt.setString(++cnt, cont_no);
				stmt.setString(++cnt, contract_type);
				stmt.setString(++cnt, contract_type);
				stmt.setString(++cnt, agmt_no);
				stmt.setString(++cnt, agmt_no);
				stmt.setString(++cnt, agmt_type);
				stmt.setString(++cnt, agmt_type);
				stmt.setString(++cnt, cargo_no);
				stmt.setString(++cnt, cargo_no);
				stmt.setString(++cnt, alloc_rev);
				stmt.setString(++cnt, alloc_rev);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					int boe_cnt = rset.getInt(1);
					String lab_qty = rset.getString(2)==null?"":rset.getString(2);
					
					if(boe_cnt==0 || lab_qty.equals(""))
					{
						allocation_status="Custom";
					}
					else
					{
						allocation_status="Allocated";
					}
				}
				else
				{
					allocation_status="Pending";
				}
				
			}
			else
			{
				msg = "Failed! - "+cargo_name+" Cargo Arrival Detail for "+counterparty_abbr+" submission Failed!";
				msg_type = "E";
			}
			
			url = "../cargo/frm_cargo_arrival.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+""+"&cargo_no="+cargo_no+"&cont_no="+cont_no+"&ship_cd="+ship_cd+
			"&contract_type="+contract_type+"&cargo_name="+cargo_name+"&cont_name="+cont_name+"&arrival_dt="+act_arival_dt+"&ship_name="+ship_name+
			"&cont_rev="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev="+agmt_rev_no+"&agmt_type="+agmt_type+"&conf_volume="+expected_qunt+"&allocation_status="+allocation_status+commonUrl_pra;
			
			
			dbcon.commit();
			
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! "+cargo_name+" Cargo Arrival Detail submission Failed!";
		}
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}

	private void InsertUpdateCnLiabilityDtls(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateCnLiabilityDtls()";
		msg="";
		msg_type="";
		url="";
		String cont_name_map="";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String agreement_type=request.getParameter("agreement_type")==null?"M":request.getParameter("agreement_type");
			String start_dt=request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			
			String liab_lq_damg = request.getParameter("liab_lq_damg")==null?"":request.getParameter("liab_lq_damg");
			String ld_price_per = request.getParameter("ld_price_per")==null?"":request.getParameter("ld_price_per");
			String ld_promise = request.getParameter("ld_promise")==null?"":request.getParameter("ld_promise");
			String ld_low_per = request.getParameter("ld_low_per")==null?"":request.getParameter("ld_low_per");
			String remark = request.getParameter("remark")==null?"":request.getParameter("remark");
			String ld_chk = request.getParameter("ld_chk")==null?"":request.getParameter("ld_chk");
			
			String liab_take_pay = request.getParameter("liab_take_pay")==null?"":request.getParameter("liab_take_pay");
			String top_price_per = request.getParameter("top_price_per")==null?"":request.getParameter("top_price_per");
			String top_promise = request.getParameter("top_promise")==null?"":request.getParameter("top_promise");
			String top_per = request.getParameter("top_per")==null?"":request.getParameter("top_per");
			String top_chk = request.getParameter("top_chk")==null?"":request.getParameter("top_chk");
			String top_obligation = request.getParameter("top_obligation")==null?"":request.getParameter("top_obligation");
			String top_remark = request.getParameter("top_remark")==null?"":request.getParameter("top_remark");
			
			String liab_makeup = request.getParameter("liab_makeup")==null?"":request.getParameter("liab_makeup");
			String mug_price_per = request.getParameter("mug_price_per")==null?"":request.getParameter("mug_price_per");
			String mug_period_per = request.getParameter("mug_period_per")==null?"":request.getParameter("mug_period_per");
			String recovery_day = request.getParameter("recovery_day")==null?"":request.getParameter("recovery_day");
			String mug_remark = request.getParameter("mug_remark")==null?"":request.getParameter("mug_remark");
			
			//cont_name_map = comp_cd+contract_type+counterparty_cd+"-"+cont_no+"-"+cont_rev_no;
			cont_name_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");

			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !agreement_type.equals(""))
			{
				int count=0;
				query = "SELECT COUNT(*) "
						+ "FROM FMS_TRADER_LIABILITY "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? AND CONT_NO=? AND CONTRACT_TYPE=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt_no);
				stmt.setString(4, cont_no);
				stmt.setString(5, contract_type);
				rset = stmt.executeQuery();
				count = 0;
				if(rset.next())
				{
					 count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count > 0)
				{
					int cnt = 0;
					query="UPDATE FMS_TRADER_LIABILITY SET LIAB_LQ_DAMG=?,LQ_DAMG_RATE_PER=?,LQ_DAMG_PROMISE=?,LQ_DAMG_LIAB_PER=?,LQ_DAMG_LIAB_ON=?,LQ_DAMG_RMK=?,"
							+ "LIAB_TAKE_PAY=?,TAKE_PAY_RATE_PER=?,TAKE_PAY_PROMISE=?,TAKE_PAY_LIAB_PER=?,TAKE_PAY_LIAB_ON=?,TAKE_PAY_LIAB_QTY=?,TAKE_PAY_LIAB_QTY_UNIT=?,TAKE_PAY_RMK=?,"
							+ "LIAB_MAKEUP=?,MAKEUP_RATE_PER=?,MAKEUP_LIAB_PER=?,MAKEUP_LIAB_ON=?,MAKEUP_RECOVERY_DAYS=?,MAKEUP_RMK=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,REV_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_TYPE=? AND CONT_NO=? AND CONTRACT_TYPE=?";
					stmt1 =dbcon.prepareStatement(query);
					stmt1.setString(++cnt, liab_lq_damg);
					stmt1.setString(++cnt, ld_price_per);
					stmt1.setString(++cnt, ld_promise);
					stmt1.setString(++cnt, ld_low_per);
					stmt1.setString(++cnt, ld_chk);
					stmt1.setString(++cnt, remark);
					stmt1.setString(++cnt, liab_take_pay);
					stmt1.setString(++cnt, top_price_per);
					stmt1.setString(++cnt, top_promise);
					stmt1.setString(++cnt, top_per);
					stmt1.setString(++cnt, top_chk);
					stmt1.setString(++cnt, top_obligation);
					stmt1.setString(++cnt, "");
					stmt1.setString(++cnt, top_remark);
					stmt1.setString(++cnt, liab_makeup);
					stmt1.setString(++cnt, mug_price_per);
					stmt1.setString(++cnt, mug_period_per);
					stmt1.setString(++cnt, "");
					stmt1.setString(++cnt, recovery_day);
					stmt1.setString(++cnt, mug_remark);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, counterparty_cd);
					stmt1.setString(++cnt, agmt_no);
					stmt1.setString(++cnt, agreement_type);
					stmt1.setString(++cnt, cont_no);
					stmt1.setString(++cnt, contract_type);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					msg = "Successful! "+cont_name_map+" Liability Detail for "+counterparty_abbr+" Modified Successfully!";
					msg_type = "S";
				}
				else
				{
					int cnt=0;
					query="INSERT INTO FMS_TRADER_LIABILITY(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
							+ "LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,"
							+ "LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,TAKE_PAY_RMK,"
							+ "LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,ENT_DT,ENT_BY,REV_DT) "
							+ "VALUES(?,?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,SYSDATE,?,SYSDATE)";
					stmt1 =dbcon.prepareStatement(query);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, counterparty_cd);
					stmt1.setString(++cnt, agreement_type);
					stmt1.setString(++cnt, agmt_no);
					stmt1.setString(++cnt, agmt_rev_no);
					stmt1.setString(++cnt, cont_no);
					stmt1.setString(++cnt, cont_rev_no);
					stmt1.setString(++cnt, contract_type);
					stmt1.setString(++cnt, liab_lq_damg);
					stmt1.setString(++cnt, ld_price_per);
					stmt1.setString(++cnt, ld_promise);
					stmt1.setString(++cnt, ld_low_per);
					stmt1.setString(++cnt, ld_chk);
					stmt1.setString(++cnt, remark);
					stmt1.setString(++cnt, liab_take_pay);
					stmt1.setString(++cnt, top_price_per);
					stmt1.setString(++cnt, top_promise);
					stmt1.setString(++cnt, top_per);
					stmt1.setString(++cnt, top_chk);
					stmt1.setString(++cnt, top_obligation);
					stmt1.setString(++cnt, "");
					stmt1.setString(++cnt, top_remark);
					stmt1.setString(++cnt, liab_makeup);
					stmt1.setString(++cnt, mug_price_per);
					stmt1.setString(++cnt, mug_period_per);
					stmt1.setString(++cnt, "");
					stmt1.setString(++cnt, recovery_day);
					stmt1.setString(++cnt, mug_remark);
					stmt1.setString(++cnt, emp_cd);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					msg = "Successful! -  "+cont_name_map+" Liability Detail for "+counterparty_abbr+" submitted Successfully!";
					msg_type = "S";
				}
			}
			else
			{
				msg = "Failed! -  "+cont_name_map+" Liability Detail for "+counterparty_abbr+" submission Failed!";
				msg_type = "E";
			}
			
			url = "../cargo/frm_mspa_liability_clause.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&agreement_type="+agreement_type+
					"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&start_dt="+start_dt+commonUrl_pra;
			
			dbcon.commit();
			
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Agreement Liability Insert/Update Failed";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}

	private void InsertUpdateMspaLiabilityDtls(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateMspaLiabilityDtls()";
		msg="";
		msg_type="";
		url="";
		String agmt_name_map = "";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String agreement_type=request.getParameter("agreement_type")==null?"M":request.getParameter("agreement_type");
			String start_dt=request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			
			String liab_lq_damg = request.getParameter("liab_lq_damg")==null?"":request.getParameter("liab_lq_damg");
			String ld_price_per = request.getParameter("ld_price_per")==null?"":request.getParameter("ld_price_per");
			String ld_promise = request.getParameter("ld_promise")==null?"":request.getParameter("ld_promise");
			String ld_low_per = request.getParameter("ld_low_per")==null?"":request.getParameter("ld_low_per");
			String remark = request.getParameter("remark")==null?"":request.getParameter("remark");
			String ld_chk = request.getParameter("ld_chk")==null?"":request.getParameter("ld_chk");
			
			String liab_take_pay = request.getParameter("liab_take_pay")==null?"":request.getParameter("liab_take_pay");
			String top_price_per = request.getParameter("top_price_per")==null?"":request.getParameter("top_price_per");
			String top_promise = request.getParameter("top_promise")==null?"":request.getParameter("top_promise");
			String top_per = request.getParameter("top_per")==null?"":request.getParameter("top_per");
			String top_chk = request.getParameter("top_chk")==null?"":request.getParameter("top_chk");
			String top_obligation = request.getParameter("top_obligation")==null?"":request.getParameter("top_obligation");
			String top_remark = request.getParameter("top_remark")==null?"":request.getParameter("top_remark");
			
			String liab_makeup = request.getParameter("liab_makeup")==null?"":request.getParameter("liab_makeup");
			String mug_price_per = request.getParameter("mug_price_per")==null?"":request.getParameter("mug_price_per");
			String mug_period_per = request.getParameter("mug_period_per")==null?"":request.getParameter("mug_period_per");
			String recovery_day = request.getParameter("recovery_day")==null?"":request.getParameter("recovery_day");
			String mug_remark = request.getParameter("mug_remark")==null?"":request.getParameter("mug_remark");
			
			//agmt_name_map = counterparty_abbr+"-"+agreement_type+agmt_no+"-"+agmt_rev_no;
			agmt_name_map = utilBean.NewAgmtMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, agreement_type);

			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !agreement_type.equals(""))
			{
				int count=0;
				query = "SELECT COUNT(*) "
						+ "FROM FMS_TRADER_AGMT_LIABILITY "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? ";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt_no);
				rset = stmt.executeQuery();
				count = 0;
				if(rset.next())
				{
					 count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count > 0)
				{
					int cnt = 0;
					query="UPDATE FMS_TRADER_AGMT_LIABILITY SET LIAB_LQ_DAMG=?,LQ_DAMG_RATE_PER=?,LQ_DAMG_PROMISE=?,LQ_DAMG_LIAB_PER=?,LQ_DAMG_LIAB_ON=?,LQ_DAMG_RMK=?,"
							+ "LIAB_TAKE_PAY=?,TAKE_PAY_RATE_PER=?,TAKE_PAY_PROMISE=?,TAKE_PAY_LIAB_PER=?,TAKE_PAY_LIAB_ON=?,TAKE_PAY_LIAB_QTY=?,TAKE_PAY_LIAB_QTY_UNIT=?,TAKE_PAY_RMK=?,"
							+ "LIAB_MAKEUP=?,MAKEUP_RATE_PER=?,MAKEUP_LIAB_PER=?,MAKEUP_LIAB_ON=?,MAKEUP_RECOVERY_DAYS=?,MAKEUP_RMK=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,REV_DT=SYSDATE "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_TYPE=?";
					stmt1 =dbcon.prepareStatement(query);
					stmt1.setString(++cnt, liab_lq_damg);
					stmt1.setString(++cnt, ld_price_per);
					stmt1.setString(++cnt, ld_promise);
					stmt1.setString(++cnt, ld_low_per);
					stmt1.setString(++cnt, ld_chk);
					stmt1.setString(++cnt, remark);
					stmt1.setString(++cnt, liab_take_pay);
					stmt1.setString(++cnt, top_price_per);
					stmt1.setString(++cnt, top_promise);
					stmt1.setString(++cnt, top_per);
					stmt1.setString(++cnt, top_chk);
					stmt1.setString(++cnt, top_obligation);
					stmt1.setString(++cnt, "");
					stmt1.setString(++cnt, top_remark);
					stmt1.setString(++cnt, liab_makeup);
					stmt1.setString(++cnt, mug_price_per);
					stmt1.setString(++cnt, mug_period_per);
					stmt1.setString(++cnt, "");
					stmt1.setString(++cnt, recovery_day);
					stmt1.setString(++cnt, mug_remark);
					stmt1.setString(++cnt, emp_cd);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, counterparty_cd);
					stmt1.setString(++cnt, agmt_no);
					stmt1.setString(++cnt, agreement_type);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					msg = "Successful! "+agmt_name_map+" Liability Detail for "+counterparty_abbr+" Modified Successfully!";
					msg_type = "S";
				}
				else
				{
					int cnt=0;
					query="INSERT INTO FMS_TRADER_AGMT_LIABILITY(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,"
							+ "LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,"
							+ "LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,TAKE_PAY_RMK,"
							+ "LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,ENT_DT,ENT_BY,REV_DT) "
							+ "VALUES(?,?,?,?,?,"
							+ "?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,SYSDATE,?,SYSDATE)";
					stmt1 =dbcon.prepareStatement(query);
					stmt1.setString(++cnt, comp_cd);
					stmt1.setString(++cnt, counterparty_cd);
					stmt1.setString(++cnt, agreement_type);
					stmt1.setString(++cnt, agmt_no);
					stmt1.setString(++cnt, agmt_rev_no);
					stmt1.setString(++cnt, liab_lq_damg);
					stmt1.setString(++cnt, ld_price_per);
					stmt1.setString(++cnt, ld_promise);
					stmt1.setString(++cnt, ld_low_per);
					stmt1.setString(++cnt, ld_chk);
					stmt1.setString(++cnt, remark);
					stmt1.setString(++cnt, liab_take_pay);
					stmt1.setString(++cnt, top_price_per);
					stmt1.setString(++cnt, top_promise);
					stmt1.setString(++cnt, top_per);
					stmt1.setString(++cnt, top_chk);
					stmt1.setString(++cnt, top_obligation);
					stmt1.setString(++cnt, "");
					stmt1.setString(++cnt, top_remark);
					stmt1.setString(++cnt, liab_makeup);
					stmt1.setString(++cnt, mug_price_per);
					stmt1.setString(++cnt, mug_period_per);
					stmt1.setString(++cnt, "");
					stmt1.setString(++cnt, recovery_day);
					stmt1.setString(++cnt, mug_remark);
					stmt1.setString(++cnt, emp_cd);
					stmt1.executeUpdate();
					
					stmt1.close();
					
					msg = "Successful! - "+agmt_name_map+" Liability Detail for "+counterparty_abbr+" submitted Successfully!";
					msg_type = "S";
				}
			}
			else
			{
				msg = "Failed! - "+agmt_name_map+" Liability Detail for "+counterparty_abbr+" submission Failed!";
				msg_type = "E";
			}
			
			url = "../cargo/frm_mspa_liability_clause.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&agreement_type="+agreement_type+
					"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&start_dt="+start_dt+commonUrl_pra;
			
			dbcon.commit();
			
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Agreement Liability Insert/Update Failed";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}

	private void InsertUpdateCargoNominationDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateCargoNominationDetail()";
		msg="";
		msg_type="";
		url="";
		String agmt_name_map="";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String billing_opration = request.getParameter("billing_opration")==null?"":request.getParameter("billing_opration"); // JD: ??
			String bl_opration = request.getParameter("bl_opration")==null?"":request.getParameter("bl_opration");
			String boe_opration = request.getParameter("boe_opration")==null?"":request.getParameter("boe_opration");
			String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance");
			String sysdt = utilDate.getSysdate();
			
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			
			String status = request.getParameter("status")==null?"":request.getParameter("status");
			
			
			if(status.equals("")) {
				status="A";
			}

			String agmt_type = request.getParameter("agmt_type")==null?"":request.getParameter("agmt_type");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String remark=request.getParameter("remark")==null?"":request.getParameter("remark");//escObj.replaceSingleQuotes("remark");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String cont_no = request.getParameter("confirm_no")==null?"":request.getParameter("confirm_no");
			String cont_rev = request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String cargo_no = request.getParameter("cargo_number")==null?"":request.getParameter("cargo_number");
			String nom_rev = request.getParameter("nom_rev")==null?"":request.getParameter("nom_rev");
			String ship_cd = request.getParameter("ship_cd")==null?"":request.getParameter("ship_cd");

			String exp_delv_qty = request.getParameter("mandate_req_vol")==null?"":request.getParameter("mandate_req_vol");
			String exp_from_dt = request.getParameter("man_req_from_dt")==null?"":request.getParameter("man_req_from_dt");
			String exp_to_dt = request.getParameter("man_req_to_dt")==null?"":request.getParameter("man_req_to_dt");
			String country_origin = request.getParameter("country_org")==null?"":request.getParameter("country_org");
			String load_port = request.getParameter("load_port")==null?"":request.getParameter("load_port");
			
			String split_bol = request.getParameter("BL_Checkbox")==null?"":request.getParameter("BL_Checkbox");
			String num_bl = request.getParameter("bl_number")==null?"":request.getParameter("bl_number");
			String num_boe = request.getParameter("boe_number")==null?"":request.getParameter("boe_number");

			String liquefac_plant = request.getParameter("liquefac_plant")==null?"":request.getParameter("liquefac_plant");
			String liquefac_country = request.getParameter("liquefac_country")==null?"":request.getParameter("liquefac_country");
			String liquefac_promotor = request.getParameter("liquefac_promotor")==null?"":request.getParameter("liquefac_promotor");
			String liquefac_remark = request.getParameter("liquefac_remark")==null?"":request.getParameter("liquefac_remark");
			String flag = request.getParameter("flag")==null?"":request.getParameter("flag");
			
			String surveyor_cont_dtl = request.getParameter("surveyor_cont_dtl")==null?"":request.getParameter("surveyor_cont_dtl");
			String cha_cont_dtl = request.getParameter("cha_cont_dtl")==null?"":request.getParameter("cha_cont_dtl");
			String vessel_cont_dtl = request.getParameter("vessel_cont_dtl")==null?"":request.getParameter("vessel_cont_dtl");
			
			String bl_no = request.getParameter("bl_no")==null?"":request.getParameter("bl_no").replace("BL", "");
			String bl_qty = request.getParameter("bl_qty")==null?"":request.getParameter("bl_qty");
			String bl_qty_mt = request.getParameter("bl_qty_mt")==null?"":request.getParameter("bl_qty_mt");
			String bl_qty_scm = request.getParameter("bl_qty_scm")==null?"":request.getParameter("bl_qty_scm");
			String bl_qty_unit = request.getParameter("bl_qty_unit")==null?"":request.getParameter("bl_qty_unit");
			String bl_price = request.getParameter("bl_price")==null?"":request.getParameter("bl_price");
			String bl_price_unit = request.getParameter("bl_price_unit")==null?"":request.getParameter("bl_price_unit");

			String boe_no = request.getParameter("boe_no")==null?"":request.getParameter("boe_no").replace("BOE", "");
			String boe_qty = request.getParameter("boe_qty")==null?"":request.getParameter("boe_qty");
			String boe_qty_mt = request.getParameter("boe_qty_mt")==null?"":request.getParameter("boe_qty_mt");
			String boe_qty_scm = request.getParameter("boe_qty_scm")==null?"":request.getParameter("boe_qty_scm");
			String boe_qty_unit = request.getParameter("boe_qty_unit")==null?"":request.getParameter("boe_qty_unit");
			String boe_price = request.getParameter("boe_price")==null?"":request.getParameter("boe_price");
			String boe_price_unit = request.getParameter("boe_price_unit")==null?"":request.getParameter("boe_price_unit");
			String bu_seq = request.getParameter("bu_seq")==null?"":request.getParameter("bu_seq");
			String plant_seq = request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
			
			String CD_flg = request.getParameter("custDuty_flag")==null?"":request.getParameter("custDuty_flag");
			String port_code = request.getParameter("port_code")==null?"":request.getParameter("port_code");
			String linked_bl = request.getParameter("sel_bl_list")==null?"":request.getParameter("sel_bl_list");
			
			//String cargo_no_map = comp_cd+contract_type+counterparty_cd+"-"+cont_no+"-"+cargo_no;
			String cargo_no_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev, contract_type, cargo_no);
			
			if(opration.equals("INSERT"))
			{
				nom_rev="0"; 
			}
			else if(opration.equals("MODIFY"))
			{				
				query="SELECT MAX(NOM_REV) "
						+ "FROM FMS_BUY_CARGO_NOM "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND CONTRACT_TYPE=? AND CONT_NO= ? AND CARGO_NO=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt_type);
				stmt.setString(4, agmt_no);
				stmt.setString(5, contract_type);
				stmt.setString(6, cont_no);
				stmt.setString(7, cargo_no);				

				rset=stmt.executeQuery();
				if(rset.next())
				{
					if (bl_opration.equals("") && boe_opration.equals(""))
					{	
						nom_rev = ""+(rset.getInt(1)+1);
					}
					else
					{
						// Just fetching Nomination Revision to add BL/BoE Details
						nom_rev = ""+(rset.getInt(1));
					}	
				}
				rset.close();
				stmt.close();													
			}
					 
			if(opration.equals("MODIFY"))
			{
				if(billing_opration.equals("BL_UPDATE"))
				{
					int blcnt=0;
					
					if(bl_opration.equals("BL_INS"))
					{
						String bl_number = "";
						
						query="SELECT MAX(BL_NO) "
								+ "FROM FMS_BUY_CARGO_NOM_BL "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
								+ "AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? "
								+ "AND CARGO_NO=? AND NOM_REV=? ";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, counterparty_cd);
						stmt.setString(3, agmt_no);
						stmt.setString(4, agmt_rev_no);
						stmt.setString(5, contract_type);
						stmt.setString(6, cont_no);
						stmt.setString(7, cargo_no);
						stmt.setString(8, nom_rev);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							bl_number = ""+(rset.getInt(1)+1);
						}
						else
						{
							bl_number = "0";
						}
						rset.close();
						stmt.close();
						
						queryString = "INSERT INTO FMS_BUY_CARGO_NOM_BL(COMPANY_CD,"
								+ "COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,"
								+ "CONT_NO,CONT_REV,CARGO_NO,NOM_REV,BL_NO,BL_QTY,"
								+ "BL_QTY_UNIT,BL_PRICE,BL_PRICE_UNIT,ENT_BY,ENT_DT,FLAG,BL_QTY_MT,BL_QTY_SCM) "
								+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,?,?)";
						stmt = dbcon.prepareStatement(queryString);
						stmt.setString(++blcnt, comp_cd);
						stmt.setString(++blcnt, counterparty_cd);
						stmt.setString(++blcnt, agmt_type);
						stmt.setString(++blcnt, agmt_no);
						stmt.setString(++blcnt, agmt_rev_no);
						stmt.setString(++blcnt, contract_type);
						stmt.setString(++blcnt, cont_no);
						stmt.setString(++blcnt, cont_rev);
						stmt.setString(++blcnt, cargo_no);
						stmt.setString(++blcnt, nom_rev);
						stmt.setString(++blcnt, bl_number);
						stmt.setString(++blcnt, bl_qty);
						stmt.setString(++blcnt, bl_qty_unit);
						stmt.setString(++blcnt, bl_price);
						stmt.setString(++blcnt, bl_price_unit);
						stmt.setString(++blcnt, emp_cd);
						stmt.setString(++blcnt, flag);
						stmt.setString(++blcnt, bl_qty_mt);
						stmt.setString(++blcnt, bl_qty_scm);
						stmt.executeUpdate();
						
						stmt.close();

						if(Integer.parseInt(bl_number)>0) 
						{
							query1="UPDATE FMS_BUY_CARGO_NOM A SET SPLIT_BOL=?,NUM_BL=? "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
									+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=? "
									+ "AND NOM_REV=(SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM B "
										+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
										+ "AND A.AGMT_TYPE = B.AGMT_TYPE AND A.AGMT_NO = B.AGMT_NO "
										+ "AND B.CONTRACT_TYPE = A.CONTRACT_TYPE AND B.CONT_NO = A.CONT_NO AND B.CARGO_NO = A.CARGO_NO)";
							stmt1=dbcon.prepareStatement(query1);
							stmt1.setString(1, split_bol);
							stmt1.setString(2, bl_number);
							stmt1.setString(3, comp_cd);
							stmt1.setString(4, counterparty_cd);
							stmt1.setString(5, cont_no);
							stmt1.setString(6, agmt_type);
							stmt1.setString(7, agmt_no);
							stmt1.setString(8, contract_type);
							stmt1.setString(9, cargo_no);
							stmt1.executeUpdate();
							stmt1.close();
						}
						
						String disp_bl_number = "BL"+(utilBean.PrePaddingZero(bl_number,2));
						msg = "Successful! - Cargo "+cargo_no_map+" "+disp_bl_number+" Document Details for "+counterparty_abbr+" inserted Successfully!";
						msg_type = "S";
					}
					else if(bl_opration.equals("BL_MOD"))
					{
						blcnt =0;
						
						queryString = "UPDATE FMS_BUY_CARGO_NOM_BL A SET BL_QTY=?,"
								+ "BL_QTY_UNIT=?,"
								+ "BL_PRICE=?,"
								+ "BL_PRICE_UNIT=?, BL_QTY_MT=?, BL_QTY_SCM=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
								+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=? AND BL_NO=? "
								+ "AND NOM_REV=(SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM_BL B "
								+ "WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
								+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CARGO_NO=B.CARGO_NO) ";
						stmt1=dbcon.prepareStatement(queryString);
						stmt1.setString(++blcnt, bl_qty);
						stmt1.setString(++blcnt, bl_qty_unit);
						stmt1.setString(++blcnt, bl_price);
						stmt1.setString(++blcnt, bl_price_unit);
						stmt1.setString(++blcnt, bl_qty_mt);
						stmt1.setString(++blcnt, bl_qty_scm);
						stmt1.setString(++blcnt, comp_cd);
						stmt1.setString(++blcnt, counterparty_cd);
						stmt1.setString(++blcnt, cont_no);
						stmt1.setString(++blcnt, agmt_type);
						stmt1.setString(++blcnt, agmt_no);
						stmt1.setString(++blcnt, contract_type);
						stmt1.setString(++blcnt, cargo_no);
						stmt1.setString(++blcnt, bl_no);
						stmt1.executeUpdate();
						stmt1.close();
						
						String disp_bl_number = "BL"+(utilBean.PrePaddingZero(bl_no,2));
						msg = "Successful! - Cargo "+cargo_no_map+" "+disp_bl_number+" Details for "+counterparty_abbr+" Modified Successfully!";
						msg_type = "S";
					}
				}
				else if (billing_opration.equals("BOE_UPDATE"))
				{
					int boecnt=0;
					String boe_number = "";
					
					if(boe_opration.equals("BOE_INS"))
					{
						query="SELECT MAX(BOE_NO) "
								+ "FROM FMS_BUY_CARGO_NOM_BOE "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? "
								+ "AND AGMT_REV=? AND CONTRACT_TYPE=? AND CONT_NO=? "
								+ "AND CARGO_NO=? AND NOM_REV=? ";
						stmt=dbcon.prepareStatement(query);
						stmt.setString(1, comp_cd);
						stmt.setString(2, counterparty_cd);
						stmt.setString(3, agmt_no);
						stmt.setString(4, agmt_rev_no);
						stmt.setString(5, contract_type);
						stmt.setString(6, cont_no);
						stmt.setString(7, cargo_no);
						stmt.setString(8, nom_rev);
						rset=stmt.executeQuery();
						if(rset.next())
						{
							boe_number = ""+(rset.getInt(1)+1);
						}
						else
						{
							boe_number = "0";
						}
						rset.close();
						stmt.close();

						queryString = "INSERT INTO FMS_BUY_CARGO_NOM_BOE(COMPANY_CD,"
								+ "COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,"
								+ "CONT_NO,CONT_REV,CARGO_NO,NOM_REV,BOE_NO,BU_SEQ,PLANT_SEQ,"
								+ "BOE_QTY,BOE_QTY_UNIT,BOE_PRICE,BOE_PRICE_UNIT,ENT_BY,ENT_DT,"
								+ "CUSTOM_DUTY,LOAD_PORT,LINKED_BL,BOE_QTY_MT,BOE_QTY_SCM) "
								+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,?,?,?,?) ";
						stmt = dbcon.prepareStatement(queryString);
						stmt.setString(++boecnt, comp_cd);
						stmt.setString(++boecnt, counterparty_cd);
						stmt.setString(++boecnt, agmt_type);
						stmt.setString(++boecnt, agmt_no);
						stmt.setString(++boecnt, agmt_rev_no);
						stmt.setString(++boecnt, contract_type);
						stmt.setString(++boecnt, cont_no);
						stmt.setString(++boecnt, cont_rev);
						stmt.setString(++boecnt, cargo_no);
						stmt.setString(++boecnt, nom_rev);
						stmt.setString(++boecnt, boe_number);
						stmt.setString(++boecnt, bu_seq);
						stmt.setString(++boecnt, plant_seq);
						stmt.setString(++boecnt, boe_qty);
						stmt.setString(++boecnt, boe_qty_unit);
						stmt.setString(++boecnt, boe_price);
						stmt.setString(++boecnt, boe_price_unit);
						stmt.setString(++boecnt, emp_cd);
						stmt.setString(++boecnt, CD_flg);
						stmt.setString(++boecnt, port_code);
						stmt.setString(++boecnt, linked_bl);
						stmt.setString(++boecnt, boe_qty_mt);
						stmt.setString(++boecnt, boe_qty_scm);
						stmt.executeUpdate();
						
						stmt.close();
						
						if(Integer.parseInt(boe_number)>0) 
						{
							query1="UPDATE FMS_BUY_CARGO_NOM A SET NUM_BOE=? "
									+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
									+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=? "
									+ "AND NOM_REV=(SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM B "
									+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
									+ "AND A.AGMT_TYPE = B.AGMT_TYPE AND A.AGMT_NO = B.AGMT_NO "
									+ "AND B.CONTRACT_TYPE = A.CONTRACT_TYPE AND B.CONT_NO = A.CONT_NO AND B.CARGO_NO = A.CARGO_NO)";
							stmt1=dbcon.prepareStatement(query1);
							stmt1.setString(1, boe_number);
							stmt1.setString(2, comp_cd);
							stmt1.setString(3, counterparty_cd);
							stmt1.setString(4, cont_no);
							stmt1.setString(5, agmt_type);
							stmt1.setString(6, agmt_no);
							stmt1.setString(7, contract_type);
							stmt1.setString(8, cargo_no);
							stmt1.executeUpdate();
							stmt1.close();
						}
						String disp_boe_number = "BOE"+(utilBean.PrePaddingZero(boe_number,2));
						msg = "Successful! - Cargo "+cargo_no_map+" "+disp_boe_number+" Details for "+counterparty_abbr+" inserted Successfully!";
						msg_type = "S";
					}
					else if(boe_opration.equals("BOE_MOD"))
					{
						boecnt=0;
						
						queryString = "UPDATE FMS_BUY_CARGO_NOM_BOE A SET BU_SEQ=?,"
								+ "PLANT_SEQ=?,"
								+ "BOE_QTY=?,"
								+ "BOE_QTY_UNIT=?,"
								+ "BOE_PRICE=?, "
								+ "BOE_PRICE_UNIT=?,"
								+ "CUSTOM_DUTY=?,"
								+ "LOAD_PORT=?,"
								+ "LINKED_BL=?,"
								+ "BOE_QTY_MT=?,"
								+ "BOE_QTY_SCM=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
								+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=? AND BOE_NO=? "
								+ "AND NOM_REV=(SELECT MAX(B.NOM_REV) FROM FMS_BUY_CARGO_NOM_BOE B "
								+ "WHERE A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.COMPANY_CD=B.COMPANY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
								+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV AND A.CARGO_NO=B.CARGO_NO) ";
						stmt1=dbcon.prepareStatement(queryString);
						stmt1.setString(++boecnt, bu_seq);
						stmt1.setString(++boecnt, plant_seq);
						stmt1.setString(++boecnt, boe_qty);
						stmt1.setString(++boecnt, boe_qty_unit);
						stmt1.setString(++boecnt, boe_price);
						stmt1.setString(++boecnt, boe_price_unit);
						stmt1.setString(++boecnt, CD_flg);
						stmt1.setString(++boecnt, port_code);
						stmt1.setString(++boecnt, linked_bl);
						stmt1.setString(++boecnt, boe_qty_mt);
						stmt1.setString(++boecnt, boe_qty_scm);
						stmt1.setString(++boecnt, comp_cd);
						stmt1.setString(++boecnt, counterparty_cd);
						stmt1.setString(++boecnt, cont_no);
						stmt1.setString(++boecnt, agmt_type);
						stmt1.setString(++boecnt, agmt_no);
						stmt1.setString(++boecnt, contract_type);
						stmt1.setString(++boecnt, cargo_no);
						stmt1.setString(++boecnt, boe_no);
						stmt1.executeUpdate();
						stmt1.close();
						
						String disp_boe_number = "BOE"+(utilBean.PrePaddingZero(boe_no,2));
						msg = "Successful! - Cargo "+cargo_no_map+" "+disp_boe_number+" Details for "+counterparty_abbr+" Modified Successfully!";
						msg_type = "S";
					}
				}
				else
				{
					String old_rev_no = ""+(Integer.parseInt(nom_rev)-1);
					
					int updcnt=0;
					
					query = "INSERT INTO FMS_BUY_CARGO_NOM(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,"
							+ "CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,NOM_REV,SHIP_CD,EXP_DELV_QTY,"
							+ "EXP_FROM_DT,EXP_TO_DT,COUNTRY_ORIGIN,LOAD_PORT,"
							+ "REMARK,NUM_BL,NUM_BOE,LIQUEFAC_PLANT,LIQUEFAC_COUNTRY,LIQUEFAC_PROMOTOR,"
							+ "LIQUEFAC_REMARK,ENT_BY,ENT_DT,FLAG,SPLIT_BOL,LINKED_SURVEYOR_CONT,LINKED_CHAGENT_CONT,LINKED_VAGENT_CONT) "
							+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,"
							+ "CONTRACT_TYPE,CONT_NO,?,CARGO_NO,?,SHIP_CD,EXP_DELV_QTY,"
							+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),?,?,"
							+ "?,NUM_BL,NUM_BOE,?,?,?,"
							+ "?,?,SYSDATE,FLAG,SPLIT_BOL,?,?,? "
							+ "FROM FMS_BUY_CARGO_NOM "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND AGMT_NO=? AND CONT_NO=? AND CARGO_NO=? AND NOM_REV=?";
					stmt0 = dbcon.prepareStatement(query);
					stmt0.setString(++updcnt, cont_rev);
					stmt0.setString(++updcnt, nom_rev);
					stmt0.setString(++updcnt, exp_from_dt);
					stmt0.setString(++updcnt, exp_to_dt);
					stmt0.setString(++updcnt, country_origin);
					stmt0.setString(++updcnt, load_port);
					stmt0.setString(++updcnt, remark);
					stmt0.setString(++updcnt, liquefac_plant);
					stmt0.setString(++updcnt, liquefac_country);
					stmt0.setString(++updcnt, liquefac_promotor);
					stmt0.setString(++updcnt, liquefac_remark);
					stmt0.setString(++updcnt, emp_cd);
					stmt0.setString(++updcnt, surveyor_cont_dtl);
					stmt0.setString(++updcnt, cha_cont_dtl);
					stmt0.setString(++updcnt, vessel_cont_dtl);
					stmt0.setString(++updcnt, comp_cd);
					stmt0.setString(++updcnt, counterparty_cd);
					stmt0.setString(++updcnt, agmt_no);
					stmt0.setString(++updcnt, cont_no);
					stmt0.setString(++updcnt, cargo_no);
					stmt0.setString(++updcnt, old_rev_no);
					stmt0.executeUpdate();
					
					int blcnt=0;
					
					query="INSERT INTO FMS_BUY_CARGO_NOM_BL(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,"
							+ "CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,NOM_REV,BL_NO,BL_QTY,BL_QTY_UNIT,"
							+ "BL_PRICE,BL_PRICE_UNIT,ENT_BY,ENT_DT,FLAG,BL_QTY_MT,BL_QTY_SCM) "
							+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONTRACT_TYPE,CONT_NO,"
							+ "?,CARGO_NO,?,BL_NO,BL_QTY,BL_QTY_UNIT,BL_PRICE,BL_PRICE_UNIT,ENT_BY,ENT_DT,FLAG,BL_QTY_MT,BL_QTY_SCM "
							+ "FROM FMS_BUY_CARGO_NOM_BL "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
							+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=? AND NOM_REV=?";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(++blcnt, cont_rev);
					stmt1.setString(++blcnt, nom_rev);
					stmt1.setString(++blcnt, comp_cd);
					stmt1.setString(++blcnt, counterparty_cd);
					stmt1.setString(++blcnt, cont_no);
					stmt1.setString(++blcnt, agmt_type);
					stmt1.setString(++blcnt, agmt_no);
					stmt1.setString(++blcnt, contract_type);
					stmt1.setString(++blcnt, cargo_no);
					stmt1.setString(++blcnt, old_rev_no);
					stmt1.executeUpdate();
					stmt1.close();
					
					int boecnt=0;
					
					query1="INSERT INTO FMS_BUY_CARGO_NOM_BOE(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,"
							+ "AGMT_REV,CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,NOM_REV,BOE_NO,BU_SEQ,"
							+ "PLANT_SEQ,BOE_QTY,BOE_QTY_UNIT,BOE_PRICE,BOE_PRICE_UNIT,ENT_BY,ENT_DT,FLAG,CUSTOM_DUTY,LOAD_PORT,LINKED_BL,BOE_QTY_MT,BOE_QTY_SCM) "
							+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,"
							+ "AGMT_REV,CONTRACT_TYPE,CONT_NO,?,CARGO_NO,?,BOE_NO,BU_SEQ,"
							+ "PLANT_SEQ,BOE_QTY,BOE_QTY_UNIT,BOE_PRICE,BOE_PRICE_UNIT,ENT_BY,ENT_DT,FLAG,CUSTOM_DUTY,LOAD_PORT,LINKED_BL,BOE_QTY_MT,BOE_QTY_SCM "
							+ "FROM FMS_BUY_CARGO_NOM_BOE "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
							+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=? AND NOM_REV=?";
					stmt2 = dbcon.prepareStatement(query1);
					stmt2.setString(++boecnt, cont_rev);
					stmt2.setString(++boecnt, nom_rev);
					stmt2.setString(++boecnt, comp_cd);
					stmt2.setString(++boecnt, counterparty_cd);
					stmt2.setString(++boecnt, cont_no);
					stmt2.setString(++boecnt, agmt_type);
					stmt2.setString(++boecnt, agmt_no);
					stmt2.setString(++boecnt, contract_type);
					stmt2.setString(++boecnt, cargo_no);
					stmt2.setString(++boecnt, old_rev_no);
					stmt2.executeUpdate();
					stmt2.close();
					
					stmt0.close();
					
					msg = "Successful! - "+cargo_no_map+" Cargo Nomination for "+counterparty_abbr+" Modified Successfully!";
					msg_type = "S";
				}
			}
			else
			{
				int nomInsCnt=0;
				
				query = "INSERT INTO FMS_BUY_CARGO_NOM(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,"
						+ "CONTRACT_TYPE,CONT_NO,CONT_REV,CARGO_NO,NOM_REV,SHIP_CD,EXP_DELV_QTY,"
						+ "EXP_FROM_DT,EXP_TO_DT,COUNTRY_ORIGIN,LOAD_PORT,"
						+ "REMARK,NUM_BL,NUM_BOE,LIQUEFAC_PLANT,LIQUEFAC_COUNTRY,LIQUEFAC_PROMOTOR,"
						+ "LIQUEFAC_REMARK,ENT_BY,ENT_DT,FLAG,LINKED_SURVEYOR_CONT,LINKED_CHAGENT_CONT,LINKED_VAGENT_CONT) "
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,"
						+ "TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),"
						+ "?,?,?,?,?,?,?,?,?,?,"
						+ "TO_DATE(?,'DD/MM/YYYY'),"
						+ "?,?,?,?)";
				stmt0 = dbcon.prepareStatement(query);
				stmt0.setString(++nomInsCnt, comp_cd);
				stmt0.setString(++nomInsCnt, counterparty_cd);
				stmt0.setString(++nomInsCnt, agmt_type);
				stmt0.setString(++nomInsCnt, agmt_no);
				stmt0.setString(++nomInsCnt, agmt_rev_no);
				stmt0.setString(++nomInsCnt, contract_type);
				stmt0.setString(++nomInsCnt, cont_no);
				stmt0.setString(++nomInsCnt, cont_rev);
				stmt0.setString(++nomInsCnt, cargo_no);
				stmt0.setString(++nomInsCnt, nom_rev);
				stmt0.setString(++nomInsCnt, ship_cd);
				stmt0.setString(++nomInsCnt, exp_delv_qty);
				stmt0.setString(++nomInsCnt, exp_from_dt);
				stmt0.setString(++nomInsCnt, exp_to_dt);
				stmt0.setString(++nomInsCnt, country_origin);
				stmt0.setString(++nomInsCnt, load_port);
				stmt0.setString(++nomInsCnt, remark);
				stmt0.setString(++nomInsCnt, num_bl);
				stmt0.setString(++nomInsCnt, num_boe);
				stmt0.setString(++nomInsCnt, liquefac_plant);
				stmt0.setString(++nomInsCnt, liquefac_country);
				stmt0.setString(++nomInsCnt, liquefac_promotor);
				stmt0.setString(++nomInsCnt, liquefac_remark);
				stmt0.setString(++nomInsCnt, emp_cd);
				stmt0.setString(++nomInsCnt, sysdt);
				stmt0.setString(++nomInsCnt, flag);
				stmt0.setString(++nomInsCnt, surveyor_cont_dtl);
				stmt0.setString(++nomInsCnt, cha_cont_dtl);
				stmt0.setString(++nomInsCnt, vessel_cont_dtl);
				stmt0.executeUpdate();
				
				stmt0.close();
				
				msg = "Successful! - "+cargo_no_map+" Cargo Nomination for "+counterparty_abbr+" Added Successfully!";
				opration="MODIFY";
				msg_type="S";
			}
			
			url = "../cargo/frm_cargo_nomination.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
					"&agreement_type="+agmt_type+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&clearance="+clearance+"&cargo_number="+cargo_no+"&confirm_no="+cont_no+
					"&ship_cd="+ship_cd+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Agreement "+agmt_name_map+" Insert/Update Failed";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void InsertUpdateCnCargoDtls(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateCnCargoDtls()";
		msg="";
		msg_type="";
		url="";
		String cargo_no = "";
		String cargo_no_map = "";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String agreement_type = request.getParameter("agmt_type")==null?"":request.getParameter("agmt_type");
			String cargo_agmt_base = request.getParameter("cargo_agmt_base")==null?"":request.getParameter("cargo_agmt_base");
			cargo_no = request.getParameter("cargo_no")==null?"":request.getParameter("cargo_no");
			String cargo_ref = request.getParameter("cargo_ref")==null?"":request.getParameter("cargo_ref");
			String cargo_from_dt = request.getParameter("cargo_from_dt")==null?"":request.getParameter("cargo_from_dt");
			String cargo_to_dt = request.getParameter("cargo_to_dt")==null?"":request.getParameter("cargo_to_dt");
			String cargo_volume = request.getParameter("cargo_volume")==null?"":request.getParameter("cargo_volume");
			String cargo_status_flag = request.getParameter("cargo_status_flag")==null?"":request.getParameter("cargo_status_flag");
			String cargo_price = request.getParameter("cargo_price")==null?"":request.getParameter("cargo_price");
			String cargo_rate_unit = request.getParameter("cargo_rate_unit")==null?"":request.getParameter("cargo_rate_unit");
			String cargo_tolenrance_per = request.getParameter("cargo_tolenrance_per")==null?"":request.getParameter("cargo_tolenrance_per");
			
			//cargo_no_map = comp_cd+contract_type+counterparty_cd+"-"+cont_no+"-"+cargo_no;
			cargo_no_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, cargo_no);
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !cargo_no.equals("") && !cont_no.equals(""))
			{
				int count=0;
				query = "SELECT COUNT(*) "
						+ "FROM FMS_TRADER_CARGO_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=? AND AGMT_REV=? AND CONT_REV=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, agreement_type);
				stmt.setString(5, agmt_no);
				stmt.setString(6, contract_type);
				stmt.setString(7, cargo_no);
				stmt.setString(8, agmt_rev_no);
				stmt.setString(9, cont_rev_no);
				rset = stmt.executeQuery();
				if(rset.next())
				{
					 count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(count > 0)
				{
					query1="UPDATE FMS_TRADER_CARGO_MST SET CARGO_REF=?,CARGO_STATUS=?,CARGO_QTY=?,RATE=?,RATE_UNIT=?,"
							+ "START_DT=TO_DATE(?,'DD/MM/YYYY'),END_DT=TO_DATE(?,'DD/MM/YYYY'),MODIFY_DT=SYSDATE,MODIFY_BY=?,TOLERANCE=? "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
							+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=? AND AGMT_REV=? AND CONT_REV=?";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, cargo_ref);
					stmt1.setString(2, cargo_status_flag);
					stmt1.setString(3, cargo_volume);
					stmt1.setString(4, cargo_price);
					stmt1.setString(5, cargo_rate_unit);
					stmt1.setString(6, cargo_from_dt);
					stmt1.setString(7, cargo_to_dt);
					stmt1.setString(8, emp_cd);
					stmt1.setString(9, cargo_tolenrance_per);
					stmt1.setString(10, comp_cd);
					stmt1.setString(11, counterparty_cd);
					stmt1.setString(12, cont_no);
					stmt1.setString(13, agreement_type);
					stmt1.setString(14, agmt_no);
					stmt1.setString(15, contract_type);
					stmt1.setString(16, cargo_no);
					stmt1.setString(17, agmt_rev_no);
					stmt1.setString(18, cont_rev_no);
					stmt1.executeUpdate();
					stmt1.close();
					
					msg = "Successful! - "+cargo_no_map+" Cargo Data for "+counterparty_abbr+" Modified Successfully!";
					msg_type = "S";
				}
				else
				{
					query1="INSERT INTO FMS_TRADER_CARGO_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,CONT_NO,CONTRACT_TYPE,CARGO_NO,CARGO_REF,"
						+ "CARGO_STATUS,CARGO_QTY,RATE,RATE_UNIT,START_DT,END_DT,ENT_DT,ENT_BY,AGMT_REV,CONT_REV,TOLERANCE) "
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),SYSDATE,?,?,?,?)";
					stmt1=dbcon.prepareStatement(query1);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, agreement_type);
					stmt1.setString(4, agmt_no);
					stmt1.setString(5, cont_no);
					stmt1.setString(6, contract_type);
					stmt1.setString(7, cargo_no);
					stmt1.setString(8, cargo_ref);
					stmt1.setString(9, cargo_status_flag);
					stmt1.setString(10, cargo_volume);
					stmt1.setString(11, cargo_price);
					stmt1.setString(12, cargo_rate_unit);
					stmt1.setString(13, cargo_from_dt);
					stmt1.setString(14, cargo_to_dt);
					stmt1.setString(15, emp_cd);
					stmt1.setString(16, agmt_rev_no);
					stmt1.setString(17, cont_rev_no);
					stmt1.setString(18, cargo_tolenrance_per);
					stmt1.executeUpdate();
					stmt1.close();
					
					msg = "Successful! - "+cargo_no_map+" Cargo Data for "+counterparty_abbr+" submitted Successfully!";
					msg_type = "S";
				}
				
				//ADD LOG IN LOG TABLE
				int log_seq_no=1;
				queryString = "SELECT MAX(LOG_SEQ_NO) "
						+ "FROM LOG_TRADER_CARGO_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=?";
				stmt0=dbcon.prepareStatement(queryString);
				stmt0.setString(1, comp_cd);
				stmt0.setString(2, counterparty_cd);
				stmt0.setString(3, cont_no);
				stmt0.setString(4, agreement_type);
				stmt0.setString(5, agmt_no);
				stmt0.setString(6, contract_type);
				stmt0.setString(7, cargo_no);
				rset0 = stmt0.executeQuery();
				if(rset0.next())
				{
					log_seq_no = (rset0.getInt(1)+1);
				}
				rset0.close();
				stmt0.close();
				
				query="INSERT INTO LOG_TRADER_CARGO_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,CONTRACT_TYPE,CONT_NO,CARGO_NO,LOG_SEQ_NO,LOG_BY,LOG_DT,CARGO_REF,"
						+ "CARGO_STATUS,CARGO_QTY,RATE,RATE_UNIT,START_DT,END_DT,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,AGMT_REV,CONT_REV,TOLERANCE) "
						+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,CONTRACT_TYPE,CONT_NO,CARGO_NO,?,?,SYSDATE,CARGO_REF,"
						+ "CARGO_STATUS,CARGO_QTY,RATE,RATE_UNIT,START_DT,END_DT,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,AGMT_REV,CONT_REV,TOLERANCE "
						+ "FROM FMS_TRADER_CARGO_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND AGMT_TYPE=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? AND CARGO_NO=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setInt(1, log_seq_no);
				stmt.setString(2, emp_cd);
				stmt.setString(3, comp_cd);
				stmt.setString(4, counterparty_cd);
				stmt.setString(5, cont_no);
				stmt.setString(6, agreement_type);
				stmt.setString(7, agmt_no);
				stmt.setString(8, contract_type);
				stmt.setString(9, cargo_no);
				stmt.executeQuery();
				
				stmt.close();
			}
			else
			{
				msg = "Failed! - "+cargo_no_map+" Cargo Data for "+counterparty_abbr+" submission Failed!";
				msg_type = "E";
			}
			
			url = "../cargo/frm_confirm_notice_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+
					"&contract_type="+contract_type+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Cargo "+cargo_no+" Insert/Update Failed";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
		
	}

	private void InsertUpdateCnBillingDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateCnBillingDetail()";
		msg="";
		msg_type="";
		url="";
		String cont_name_map = "";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String contract_type=request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String start_dt=request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			String rate_unit=request.getParameter("rate_unit")==null?"2":request.getParameter("rate_unit");
			
			String freq=request.getParameter("freq")==null?"F":request.getParameter("freq");
			String billing_flag=request.getParameter("billing_flag")==null?"Y":request.getParameter("billing_flag");
			String billing_days=request.getParameter("billing_days")==null?"":request.getParameter("billing_days");
			String due_date=request.getParameter("due_date")==null?"":request.getParameter("due_date");
			String sec_due_date=request.getParameter("sec_due_date")==null?"":request.getParameter("sec_due_date");
			String inv_currency=request.getParameter("inv_currency")==null?"":request.getParameter("inv_currency");
			String payment_currency=request.getParameter("payment_currency")==null?"":request.getParameter("payment_currency");
			String rate=request.getParameter("rate")==null?"":request.getParameter("rate");
			String plusmin=request.getParameter("plusmin")==null?"":request.getParameter("plusmin");
			String modeper=request.getParameter("modeper")==null?"":request.getParameter("modeper");
			String exchng_rate=request.getParameter("exchng_rate")==null?"":request.getParameter("exchng_rate");
			
			String exch_calc_base=request.getParameter("exch_calc_base")==null?"":request.getParameter("exch_calc_base");
			String inv_criteria=request.getParameter("inv_criteria")==null?"":request.getParameter("inv_criteria");
			String exchg_rate_note=request.getParameter("exchg_rate_note")==null?"":request.getParameter("exchg_rate_note");
			
			String due_dt_in=request.getParameter("due_dt_in")==null?"":request.getParameter("due_dt_in");
			String exclude_sat=request.getParameter("exclude_sat")==null?"N":request.getParameter("exclude_sat");
			String holidayState_map = request.getParameter("holidayState_map")==null?"":request.getParameter("holidayState_map");
			
			String[] sat=request.getParameterValues("sat");
			String exclude_sat_map = "";
			
			if(exclude_sat.equals("Y"))
			{	
				if(sat!=null)
				{
					for(int i=0; i<sat.length; i++)
					{
						 if (sat[i].contains("Y")) 
						 {
			                if (exclude_sat_map.length() > 0) 
			                {
			                	exclude_sat_map+="-"; 
			                }
			                exclude_sat_map+=sat[i].charAt(0);
						 }
					}
				}
			}
			else
			{
				exclude_sat_map = "";
			}
			
			String[] cust_plant_map = holidayState_map.split("@@");
			//cont_name_map = comp_cd+contract_type+counterparty_cd+"-"+cont_no+"-"+cont_rev_no;
			cont_name_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !cont_no.equals("") && !cont_rev_no.equals(""))
			{
				String queryString="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_TRADER_CONT_PLANT A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
						+ "AND AGMT_NO=? AND CONTRACT_TYPE=? "
						+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_CONT_PLANT B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND "
						+ "A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE)";
				stmt3 = dbcon.prepareStatement(queryString);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, counterparty_cd);
				stmt3.setString(3, cont_no);
				stmt3.setString(4, agmt_no);
				stmt3.setString(5, contract_type);
				rset3=stmt3.executeQuery();
				while(rset3.next())
				{
					String plant_seq = rset3.getString(1)==null?"":rset3.getString(1);
					String state_map = "";
					for(int i=0; i<cust_plant_map.length; i++)
					{
						if(cust_plant_map[i].startsWith(plant_seq))
						{
							state_map = cust_plant_map[i].split("//")[1];
						}
					}
					int count=0;
					query = "SELECT COUNT(*) "
							+ "FROM FMS_TRADER_BILLING_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? "
							+ "AND CONT_REV=(SELECT MAX(B.CONT_REV) FROM FMS_TRADER_BILLING_DTL B WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND "
							+ "A.CONT_NO=B.CONT_NO AND A.AGMT_NO=B.AGMT_NO AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO)";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, cont_no);
					//stmt.setString(4, cont_rev_no);
					stmt.setString(4, agmt_no);
					stmt.setString(5, agmt_rev_no);
					stmt.setString(6, contract_type);
					stmt.setString(7, plant_seq);
					rset = stmt.executeQuery();
					count = 0;
					if(rset.next())
					{
						 count = rset.getInt(1);
					}
					rset.close();
					stmt.close();
					
					if(count > 0)
					{
						int cnt=0;
						query1="UPDATE FMS_TRADER_BILLING_DTL SET BILLING_FREQ=?,BILLING_FLAG=?,DUE_DATE=?,SECOND_DUE_DT=?,"
								+ "INVOICE_CUR_CD=?,PAYMENT_CUR_CD=?,INT_CAL_RATE_CD=?,INT_CAL_SIGN=?,"
								+ "INT_CAL_PERCENTAGE=?,EXCHNG_RATE_CD=?,EXCHNG_RATE_CAL=?,"
								+ "EXCHNG_CRITERIA=?,EXCHG_RATE_NOTE=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,DUE_DT_IN=?,"
								+ "EXCLUDE_SAT=?,BILLING_DAYS=?,EXCL_SAT_MAP=?,HOLIDAY_STATE=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? "
								+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=?";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(++cnt, freq);
						stmt1.setString(++cnt, billing_flag);
						stmt1.setString(++cnt, due_date);
						stmt1.setString(++cnt, sec_due_date);
						stmt1.setString(++cnt, inv_currency);
						stmt1.setString(++cnt, payment_currency);
						stmt1.setString(++cnt, rate);
						stmt1.setString(++cnt, plusmin);
						stmt1.setString(++cnt, modeper);
						stmt1.setString(++cnt, exchng_rate);
						stmt1.setString(++cnt, exch_calc_base);
						stmt1.setString(++cnt, inv_criteria);
						stmt1.setString(++cnt, exchg_rate_note);
						stmt1.setString(++cnt, emp_cd);
						stmt1.setString(++cnt, due_dt_in);
						stmt1.setString(++cnt, exclude_sat);
						stmt1.setString(++cnt, billing_days);
						stmt1.setString(++cnt, exclude_sat_map);
						stmt1.setString(++cnt, state_map);
						stmt1.setString(++cnt, comp_cd);
						stmt1.setString(++cnt, counterparty_cd);
						stmt1.setString(++cnt, cont_no);
						//stmt1.setString(++cnt, cont_rev_no);
						stmt1.setString(++cnt, agmt_no);
						stmt1.setString(++cnt, agmt_rev_no);
						stmt1.setString(++cnt, contract_type);
						stmt1.setString(++cnt, plant_seq);
						stmt1.executeUpdate();
						
						stmt1.close();
						
						msg = "Successful! - "+cont_name_map+" Billing Details for "+counterparty_abbr+" Modified Successfully!";
						msg_type = "S";
					}
					else
					{
						int cnt1=0;
						query1="INSERT INTO FMS_TRADER_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,"
								+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,"
								+ "EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,ENT_DT,ENT_BY,DUE_DT_IN,EXCLUDE_SAT,BILLING_DAYS,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE) "
								+ "VALUES(?,?,?,?,?,?,?,?,"
								+ "?,?,?,?,?,?,?,?,?,"
								+ "?,?,?,SYSDATE,?,?,?,?,?,?,?)";
						stmt1=dbcon.prepareStatement(query1);
						stmt1.setString(++cnt1, comp_cd);
						stmt1.setString(++cnt1, counterparty_cd);
						stmt1.setString(++cnt1, agmt_no);
						stmt1.setString(++cnt1, agmt_rev_no);
						stmt1.setString(++cnt1, cont_no);
						stmt1.setString(++cnt1, cont_rev_no);
						stmt1.setString(++cnt1, contract_type);
						stmt1.setString(++cnt1, freq);
						stmt1.setString(++cnt1, billing_flag);
						stmt1.setString(++cnt1, due_date);
						stmt1.setString(++cnt1, sec_due_date);
						stmt1.setString(++cnt1, inv_currency);
						stmt1.setString(++cnt1, payment_currency);
						stmt1.setString(++cnt1, rate);
						stmt1.setString(++cnt1, plusmin);
						stmt1.setString(++cnt1, modeper);
						stmt1.setString(++cnt1, exchng_rate);
						stmt1.setString(++cnt1, exch_calc_base);
						stmt1.setString(++cnt1, inv_criteria);
						stmt1.setString(++cnt1, exchg_rate_note);
						stmt1.setString(++cnt1, emp_cd);
						stmt1.setString(++cnt1, due_dt_in);
						stmt1.setString(++cnt1, exclude_sat);
						stmt1.setString(++cnt1, billing_days);
						stmt1.setString(++cnt1, exclude_sat_map);
						stmt1.setString(++cnt1, plant_seq);
						stmt1.setString(++cnt1, state_map);
						stmt1.executeUpdate();
						
						stmt1.close();
						
						msg = "Successful! - "+cont_name_map+" Billing Details for "+counterparty_abbr+" submitted Successfully!";
						msg_type = "S";
					}
				}
				rset3.close();
				stmt3.close();
			}
			else
			{
				msg = "Failed! - "+cont_name_map+" Billing Details for "+counterparty_abbr+" submission Failed!";
				msg_type = "E";
			}
			
			url = "../cargo/frm_cn_billing_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&contract_type="+contract_type+"&rate_unit="+rate_unit+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&start_dt="+start_dt+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! CN Billing Details Insert/Update Failed";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}

	private void InsertUpdateCnDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateCnDetail()";
		msg="";
		msg_type="";
		url="";
		
		try
		{
			old_value=request.getParameter("old_value")==null?"":request.getParameter("old_value");
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String operation = "";
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String cont_ref_no = request.getParameter("cont_ref_no")==null?"":request.getParameter("cont_ref_no");
			String trade_ref_no = request.getParameter("trade_ref_no")==null?"":request.getParameter("trade_ref_no");
			String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
			String cont_status = request.getParameter("cont_status")==null?"":request.getParameter("cont_status");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String cont_no=request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
			String cont_rev_no=request.getParameter("cont_rev_no")==null?"":request.getParameter("cont_rev_no");
			String signing_dt = request.getParameter("signing_dt")==null?"":request.getParameter("signing_dt");
			String signing_time = request.getParameter("signing_time")==null?"":request.getParameter("signing_time");
			String dda_dt = request.getParameter("dda_dt")==null?"":request.getParameter("dda_dt");
			String dda_time = request.getParameter("dda_time")==null?"":request.getParameter("dda_time");
			String ent_dt = request.getParameter("ent_dt")==null?"":request.getParameter("ent_dt");
			String ent_time = request.getParameter("ent_time")==null?"":request.getParameter("ent_time");
			String agreement_type = request.getParameter("agreement_type")==null?"":request.getParameter("agreement_type");
			String agreement_base = request.getParameter("agreement_base")==null?"":request.getParameter("agreement_base");
			String start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			String end_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
			String agmt_type = request.getParameter("agmt_type")==null?"":request.getParameter("agmt_type");
			String demurrage_rate = request.getParameter("demmurage_rate")==null?"":request.getParameter("demmurage_rate");
			String demurrage_rate_unit = request.getParameter("demmurage_rate_unit")==null?"":request.getParameter("demmurage_rate_unit");
			String demurrage_flg = request.getParameter("demmurageCheckbox")==null?"":request.getParameter("demmurageCheckbox");
			String allowed_lay_time_hrs = request.getParameter("allowed_lay_time_hrs")==null?"":request.getParameter("allowed_lay_time_hrs");
			String allowed_lay_time_min = request.getParameter("allowed_lay_time_min")==null?"":request.getParameter("allowed_lay_time_min");
			String measurement_flg = request.getParameter("measurementCheckbox")==null?"":request.getParameter("measurementCheckbox");
			String meas_standard = request.getParameter("meas_standard")==null?"":request.getParameter("meas_standard");
			String meas_temperature = request.getParameter("meas_temperature")==null?"":request.getParameter("meas_temperature");
			String pressure_min_bar = request.getParameter("pressure_min_bar")==null?"":request.getParameter("pressure_min_bar");
			String pressure_max_bar = request.getParameter("pressure_max_bar")==null?"":request.getParameter("pressure_max_bar");
			String off_spec_gas_flg = request.getParameter("off_spec_gas_checkbox")==null?"":request.getParameter("off_spec_gas_checkbox");
			String spec_gas_energy_base = request.getParameter("spec_gas_energy_base")==null?"":request.getParameter("spec_gas_energy_base");
			String spec_gas_min_energy = request.getParameter("spec_gas_min_energy")==null?"":request.getParameter("spec_gas_min_energy");
			String spec_gas_max_energy = request.getParameter("spec_gas_max_energy")==null?"":request.getParameter("spec_gas_max_energy");
			String liability_flg = request.getParameter("liability_checkbox")==null?"":request.getParameter("liability_checkbox");
			String liability_clause = request.getParameter("liability_clause")==null?"":request.getParameter("liability_clause");
			String liab_lq_damg = request.getParameter("liab_lq_damg")==null?"":request.getParameter("liab_lq_damg");
			String liab_take_pay = request.getParameter("liab_take_pay")==null?"":request.getParameter("liab_take_pay");
			String liab_makeup = request.getParameter("liab_makeup")==null?"":request.getParameter("liab_makeup");
			String billing_flag = request.getParameter("billing_flag")==null?"":request.getParameter("billing_flag");
			String buy_clause_no = request.getParameter("buy_clause_no")==null?"":request.getParameter("buy_clause_no");
			String terminator_flg = request.getParameter("terminator_checkbox")==null?"":request.getParameter("terminator_checkbox");
			String terminate_clause = request.getParameter("terminate_clause")==null?"":request.getParameter("terminate_clause");
			String terminate_planed = request.getParameter("terminate_planed")==null?"":request.getParameter("terminate_planed");
			String terminate_force = request.getParameter("terminate_force")==null?"":request.getParameter("terminate_force");
			//String day_def_clause_no = request.getParameter("day_def_clause_no")==null?"":request.getParameter("day_def_clause_no");
			String demurrage_clause_no = request.getParameter("demurrage_clause_no")==null?"":request.getParameter("demurrage_clause_no");
			String measure_clause_no = request.getParameter("measure_clause_no")==null?"":request.getParameter("measure_clause_no");
			String spec_clause_no = request.getParameter("spec_clause_no")==null?"":request.getParameter("spec_clause_no");
			
			
			String[] transportation_charges = request.getParameterValues("transportation_charges");
			String[] chk_plant = request.getParameterValues("chk_plant");
			String tmp_chk_plant = request.getParameter("tmp_chk_plant")==null?"":request.getParameter("tmp_chk_plant");
			String[] split_value = request.getParameterValues("split_value");
			
			String[] oth_trd_cd = request.getParameterValues("oth_trd_cd");
			String[] oth_split_value = request.getParameterValues("oth_split_value");
			String[] oth_plant_seq_no = request.getParameterValues("oth_plant_seq_no");
			
			String[] chk_trans = request.getParameterValues("chk_trans");
			String[] trans_cd = request.getParameterValues("trans_cd");
			String[] trans_plant_seq_no = request.getParameterValues("trans_plant_seq_no");
			
			String[] chk_bu_plant = request.getParameterValues("chk_bu_plant");
			String cont_status_flg = request.getParameter("cont_status_flg")==null?"":request.getParameter("cont_status_flg");
			String rev_chk = request.getParameter("rev_chk")==null?"":request.getParameter("rev_chk");
			String rev_eff_dt = request.getParameter("rev_eff_dt")==null?"":request.getParameter("rev_eff_dt");
			
			String day_def_clause_no = request.getParameter("day_def_clause_no")==null?"":request.getParameter("day_def_clause_no");
			String day_def = request.getParameter("day_def")==null?"N":request.getParameter("day_def");
			String day_time_from = request.getParameter("day_time_from")==null?"":request.getParameter("day_time_from");
			String day_time_to = request.getParameter("day_time_to")==null?"":request.getParameter("day_time_to");
			String today_dt=utilDate.getSysdate();
			//String year = today_dt.substring(8,ent_dt.length());
			
			if(opration.equals("INSERT"))
			{
				old_value="";
				String year = "7"+ent_dt.substring(8,ent_dt.length());
				int cont = Integer.parseInt(year) * 1000;
				query="SELECT MAX(CONT_NO) FROM FMS_TRADER_CN_MST "
						+ "WHERE CONT_NO LIKE ? AND COMPANY_CD=? "
						+ "AND CONTRACT_TYPE=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setString(1, year+"%");
				stmt.setString(2, comp_cd);
				stmt.setString(3, contract_type);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					if(rset.getInt(1) == 0)
					{
						cont_no = ""+(cont+1);
					}
					else
					{
						cont_no = ""+(rset.getInt(1)+1);
					}
				}
				else
				{
					cont_no = ""+(cont+1);
				}
				cont_rev_no="0";
				
				rset.close();
				stmt.close();
			}
			else if(opration.equals("MODIFY"))
			{
				if(rev_chk.equals("Y"))
				{
					query="SELECT MAX(CONT_REV) "
							+ "FROM FMS_TRADER_CN_MST "
							+ "WHERE CONT_NO= ? AND COMPANY_CD=? "
							+ "AND CONTRACT_TYPE=? AND COUNTERPARTY_CD=?";
					stmt=dbcon.prepareStatement(query);
					stmt.setString(1, cont_no);
					stmt.setString(2, comp_cd);
					stmt.setString(3, contract_type);
					stmt.setString(4, counterparty_cd);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						cont_rev_no = ""+(rset.getInt(1)+1);
					}
					else
					{
						cont_rev_no = "0";
					}
					rset.close();
					stmt.close();
					cont_status_flg = "P";
					
				}
			}
			
			// DONT CHANGE - THIS IS CONTRACT NAME
			String cont_name = comp_abbr+"-"+counterparty_abbr+"-MSPA"+agmt_no+"-REV"+agmt_rev_no+" "+contract_type+cont_no+"-REV"+cont_rev_no;
			// DISPLAY CONTRACT NAME 
			String cont_name_map = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !cont_no.equals("") && !cont_rev_no.equals("") && !contract_type.equals(""))
			{
				int rev_count=0;
				query = "SELECT COUNT(*) FROM FMS_TRADER_CN_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND CONT_REV=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=?";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, cont_no);
				stmt.setString(4, cont_rev_no);
				stmt.setString(5, agmt_no);
				stmt.setString(6, agmt_rev_no);
				stmt.setString(7, contract_type);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					rev_count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
				
				if(rev_count > 0)
				{
					
					query ="UPDATE FMS_TRADER_CN_MST SET CONT_REF_NO=?, SIGNING_DT=TO_DATE(?,'DD/MM/YYYY'), SIGNING_TIME=?, START_DT=TO_DATE(?,'DD/MM/YYYY'),"
							+ "END_DT=TO_DATE(?,'DD/MM/YYYY'), AGMT_BASE=?, AGMT_TYPE=?,AGMT_TYP=?,DAY_DEF_FLAG=?,DAY_START_TIME=?,DAY_END_TIME=?,CONT_NAME=?,"
							+ "CONTRACT_TYPE=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,CONT_STATUS=?,DDA_DT=TO_DATE(?,'DD/MM/YYYY'),DDA_TIME=?,DEMURRAGE_RATE=?,DEMURRAGE_RATE_UNIT=?, "
							+ "DEMURRAGE=?,ALW_LAYTIME_HRS=?, ALW_LAYTIME_MNS=?,MEASUREMENT =?,MEAS_STANDARD=?, MEAS_TEMPERATURE=?,PRESSURE_MIN_BAR=?,PRESSURE_MAX_BAR=?, "
							+ "OFF_SPEC_GAS=?,SPEC_GAS_ENERGY_BASE=?,SPEC_GAS_MIN_ENERGY=?,SPEC_GAS_MAX_ENERGY=?,LIABILITY=?,LIABILITY_CLAUSE=?, "
							+ "BILLING_FLAG=?,BILLING_CLAUSE=?,TERMINATE_FLAG=?,TERMINATE_CLAUSE=?,TERMINATE_PLANED=?,TERMINATE_FORCE=?, "
							+ "DAY_DEF_CLAUSE=?,DEMURRAGE_CLAUSE=?,MEASUREMENT_CLAUSE=?,OFF_SPEC_GAS_CLAUSE=?,REV_DT=TO_DATE(?,'DD/MM/YYYY') "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND CONT_REV=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=?";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(1, cont_ref_no);
					stmt1.setString(2, signing_dt);
					stmt1.setString(3, signing_time);
					stmt1.setString(4, start_dt);
					stmt1.setString(5, end_dt);
					stmt1.setString(6, agreement_base);
					stmt1.setString(7, agmt_type);
					stmt1.setString(8, agreement_type);
					stmt1.setString(9, day_def);
					stmt1.setString(10, day_time_from);
					stmt1.setString(11, day_time_to);
					stmt1.setString(12, cont_name);
					stmt1.setString(13, contract_type);
					stmt1.setString(14, emp_cd);
					stmt1.setString(15, cont_status_flg);
					stmt1.setString(16, dda_dt);
					stmt1.setString(17, dda_time);
					stmt1.setString(18, demurrage_rate);
					stmt1.setString(19, demurrage_rate_unit);
					stmt1.setString(20, demurrage_flg);
					stmt1.setString(21, allowed_lay_time_hrs);
					stmt1.setString(22, allowed_lay_time_min);
					stmt1.setString(23, measurement_flg);
					stmt1.setString(24, meas_standard);
					stmt1.setString(25, meas_temperature);
					stmt1.setString(26, pressure_min_bar);
					stmt1.setString(27, pressure_max_bar);
					stmt1.setString(28, off_spec_gas_flg);
					stmt1.setString(29, spec_gas_energy_base);
					stmt1.setString(30, spec_gas_min_energy);
					stmt1.setString(31, spec_gas_max_energy);
					stmt1.setString(32, liability_flg);
					stmt1.setString(33, liability_clause);
					stmt1.setString(34, billing_flag);
					stmt1.setString(35, buy_clause_no);
					stmt1.setString(36, terminator_flg);
					stmt1.setString(37, terminate_clause);
					stmt1.setString(38, terminate_planed);
					stmt1.setString(39, terminate_force);
					stmt1.setString(40, day_def_clause_no);
					stmt1.setString(41, demurrage_clause_no);
					stmt1.setString(42, measure_clause_no);
					stmt1.setString(43, spec_clause_no);
					stmt1.setString(44, rev_eff_dt);
					stmt1.setString(45, comp_cd);
					stmt1.setString(46, counterparty_cd);
					stmt1.setString(47, cont_no);
					stmt1.setString(48, cont_rev_no);
					stmt1.setString(49, agmt_no);
					stmt1.setString(50, agmt_rev_no);
					stmt1.setString(51, contract_type);
					stmt1.executeUpdate();
					stmt1.close();
				}
				else
				{
					query ="INSERT INTO FMS_TRADER_CN_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_BASE,AGMT_TYP,CONTRACT_TYPE,CONT_NO,CONT_REV,CONT_NAME,CONT_REF_NO,CONT_STATUS,"
							+ "DDA_DT,DDA_TIME,SIGNING_DT,SIGNING_TIME,START_DT,END_DT,ENT_DT,ENT_BY,DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,DEMURRAGE_RATE,DEMURRAGE_RATE_UNIT,ALW_LAYTIME_HRS,ALW_LAYTIME_MNS,"
							+ "MEASUREMENT,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,"
							+ "BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,DEMURRAGE,DAY_DEF_CLAUSE,DEMURRAGE_CLAUSE,MEASUREMENT_CLAUSE,OFF_SPEC_GAS_CLAUSE,REV_DT) "
							+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,"
							+ "TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'),SYSDATE,?,?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'))";
					stmt1 = dbcon.prepareStatement(query);
					stmt1.setString(1, comp_cd);
					stmt1.setString(2, counterparty_cd);
					stmt1.setString(3, agmt_type);
					stmt1.setString(4, agmt_no);
					stmt1.setString(5, agmt_rev_no);
					stmt1.setString(6, agreement_base);
					stmt1.setString(7, agreement_type);
					stmt1.setString(8, contract_type);
					stmt1.setString(9, cont_no);
					stmt1.setString(10, cont_rev_no);
					stmt1.setString(11, cont_name);
					stmt1.setString(12, cont_ref_no);
					stmt1.setString(13, cont_status_flg);
					stmt1.setString(14, dda_dt);
					stmt1.setString(15, dda_time);
					stmt1.setString(16, signing_dt);
					stmt1.setString(17, signing_time);
					stmt1.setString(18, start_dt);
					stmt1.setString(19, end_dt);
					stmt1.setString(20, emp_cd);
					stmt1.setString(21, day_def);
					stmt1.setString(22, day_time_from);
					stmt1.setString(23, day_time_to);
					stmt1.setString(24, demurrage_rate);
					stmt1.setString(25, demurrage_rate_unit);
					stmt1.setString(26, allowed_lay_time_hrs);
					stmt1.setString(27, allowed_lay_time_min);
					stmt1.setString(28, measurement_flg);
					stmt1.setString(29, meas_standard);
					stmt1.setString(30, meas_temperature);
					stmt1.setString(31, pressure_min_bar);
					stmt1.setString(32, pressure_max_bar);
					stmt1.setString(33, off_spec_gas_flg);
					stmt1.setString(34, spec_gas_energy_base);
					stmt1.setString(35, spec_gas_min_energy);
					stmt1.setString(36, spec_gas_max_energy);
					stmt1.setString(37, liability_flg);
					stmt1.setString(38, liability_clause);
					stmt1.setString(39, billing_flag);
					stmt1.setString(40, buy_clause_no);
					stmt1.setString(41, terminator_flg);
					stmt1.setString(42, terminate_clause);
					stmt1.setString(43, terminate_planed);
					stmt1.setString(44, terminate_force);
					stmt1.setString(45, demurrage_flg);
					stmt1.setString(46, day_def_clause_no);
					stmt1.setString(47, demurrage_clause_no);
					stmt1.setString(48, measure_clause_no);
					stmt1.setString(49, spec_clause_no);
					stmt1.setString(50, rev_eff_dt);
					stmt1.executeUpdate();
					stmt1.close();
					
					if(liability_flg.equals("Y") && !rev_chk.equals("Y"))
					{
						query1="INSERT INTO FMS_TRADER_LIABILITY(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
								+ "LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,"
								+ "LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,TAKE_PAY_RMK,"
								+ "LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,ENT_DT,ENT_BY,REV_DT) "
								+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,?,?,?,"
								+ "LIAB_LQ_DAMG,LQ_DAMG_RATE_PER,LQ_DAMG_PROMISE,LQ_DAMG_LIAB_PER,LQ_DAMG_LIAB_ON,LQ_DAMG_RMK,"
								+ "LIAB_TAKE_PAY,TAKE_PAY_RATE_PER,TAKE_PAY_PROMISE,TAKE_PAY_LIAB_PER,TAKE_PAY_LIAB_ON,TAKE_PAY_LIAB_QTY,TAKE_PAY_LIAB_QTY_UNIT,TAKE_PAY_RMK,"
								+ "LIAB_MAKEUP,MAKEUP_RATE_PER,MAKEUP_LIAB_PER,MAKEUP_LIAB_ON,MAKEUP_RECOVERY_DAYS,MAKEUP_RMK,SYSDATE,?,REV_DT "
								+ "FROM FMS_TRADER_AGMT_LIABILITY A "
								+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.AGMT_NO=? AND A.AGMT_REV=? "
								+ "AND A.AGMT_TYPE=? AND EXISTS("
								+ "SELECT * "
								+ "FROM FMS_TRADER_AGMT_LIABILITY B "
								+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV "
								+ "AND A.AGMT_TYPE=B.AGMT_TYPE "
								+ ")";
						
						stmt0 = dbcon.prepareStatement(query1);
						stmt0.setString(1, cont_no);
						stmt0.setString(2, cont_rev_no);
						stmt0.setString(3, contract_type);
						stmt0.setString(4, emp_cd);
						stmt0.setString(5, comp_cd);
						stmt0.setString(6, counterparty_cd);
						stmt0.setString(7, agmt_no);
						stmt0.setString(8, agmt_rev_no);
						stmt0.setString(9, agmt_type);
						stmt0.executeUpdate();
						
						stmt0.close();
					}
				}
				
				//ADD LOG IN LOG TABLE
				int log_seq_no=1;
				queryString = "SELECT MAX(LOG_SEQ_NO) "
						+ "FROM LOG_TRADER_CN_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND AGMT_NO=? AND CONTRACT_TYPE=?";
				stmt0=dbcon.prepareStatement(queryString);
				stmt0.setString(1, comp_cd);
				stmt0.setString(2, counterparty_cd);
				stmt0.setString(3, cont_no);
				stmt0.setString(4, agmt_no);
				stmt0.setString(5, contract_type);
				rset0 = stmt0.executeQuery();
				if(rset0.next())
				{
					log_seq_no = (rset0.getInt(1)+1);
				}
				rset0.close();
				stmt0.close();
				
				query="INSERT INTO LOG_TRADER_CN_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_BASE,AGMT_TYP,CONTRACT_TYPE,LOG_SEQ_NO,LOG_BY,LOG_DT,CONT_NO,CONT_REV,"
						+ "CONT_NAME,CONT_REF_NO,CONT_STATUS,DDA_DT,DDA_TIME,SIGNING_DT,SIGNING_TIME,START_DT,END_DT,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,REV_DT,"
						+ "NUM_CARGO,DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,DEMURRAGE_RATE,DEMURRAGE_RATE_UNIT,ALW_LAYTIME_HRS,ALW_LAYTIME_MNS,MEASUREMENT,MEAS_STANDARD,"
						+ "MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,"
						+ "BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,DEMURRAGE,DAY_DEF_CLAUSE,DEMURRAGE_CLAUSE,MEASUREMENT_CLAUSE,OFF_SPEC_GAS_CLAUSE,"
						+ "FCC_FLAG,FCC_BY,FCC_DATE) "
						+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_BASE,AGMT_TYP,CONTRACT_TYPE,?,?,SYSDATE,CONT_NO,CONT_REV,"
						+ "CONT_NAME,CONT_REF_NO,CONT_STATUS,DDA_DT,DDA_TIME,SIGNING_DT,SIGNING_TIME,START_DT,END_DT,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY,REV_DT,"
						+ "NUM_CARGO,DAY_DEF_FLAG,DAY_START_TIME,DAY_END_TIME,DEMURRAGE_RATE,DEMURRAGE_RATE_UNIT,ALW_LAYTIME_HRS,ALW_LAYTIME_MNS,MEASUREMENT,MEAS_STANDARD,"
						+ "MEAS_TEMPERATURE,PRESSURE_MIN_BAR,PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,LIABILITY,LIABILITY_CLAUSE,"
						+ "BILLING_FLAG,BILLING_CLAUSE,TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,DEMURRAGE,DAY_DEF_CLAUSE,DEMURRAGE_CLAUSE,MEASUREMENT_CLAUSE,OFF_SPEC_GAS_CLAUSE,"
						+ "FCC_FLAG,FCC_BY,FCC_DATE "
						+ "FROM FMS_TRADER_CN_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND CONT_REV=? "
						+ "AND AGMT_NO=? AND AGMT_REV=? "
						+ "AND CONTRACT_TYPE=?";
				stmt=dbcon.prepareStatement(query);
				stmt.setInt(1, log_seq_no);
				stmt.setString(2, emp_cd);
				stmt.setString(3, comp_cd);
				stmt.setString(4, counterparty_cd);
				stmt.setString(5, cont_no);
				stmt.setString(6, cont_rev_no);
				stmt.setString(7, agmt_no);
				stmt.setString(8, agmt_rev_no);
				stmt.setString(9, contract_type);
				stmt.executeQuery();
				
				stmt.close();
				
				//TRADER PLANT
				query1 = "DELETE FROM FMS_TRADER_CONT_PLANT A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? AND CONT_NO=? AND CONT_REV=?  "
						+ "AND AGMT_NO=? AND AGMT_REV=? AND CONTRACT_TYPE=?  "
						+ "AND EXISTS( "
						+ "SELECT * FROM FMS_TRADER_CONT_PLANT B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO AND A.CONT_REV=B.CONT_REV  "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
						+ ")";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, cont_no);
				stmt1.setString(4, cont_rev_no);
				stmt1.setString(5, agmt_no);
				stmt1.setString(6, agmt_rev_no);
				stmt1.setString(7, contract_type);
				stmt1.executeUpdate();
				stmt1.close();
				
				if(chk_plant!=null)
				{
					for(int i=0;i<chk_plant.length;i++)
					{
						query2 = "INSERT INTO FMS_TRADER_CONT_PLANT(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, "
								+ "CONTRACT_TYPE,PLANT_SEQ_NO, ENT_BY, ENT_DT) "
								+ "VALUES(?,?,?,?,?,?,"
								+ "?,?,?,SYSDATE) ";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_no);
						stmt2.setString(4, agmt_rev_no);
						stmt2.setString(5, cont_no);
						stmt2.setString(6, cont_rev_no);
						stmt2.setString(7, contract_type);
						stmt2.setString(8, chk_plant[i]);
						stmt2.setString(9, emp_cd);
						stmt2.executeUpdate();
						
						stmt2.close();
						
						String temp_chk_plant[]=tmp_chk_plant.split("@");
						for(int m=0; m<temp_chk_plant.length; m++)
						{
							if(!temp_chk_plant[m].equals(chk_plant[i]) && !chk_plant[i].equals(""))
							{
								updateCargoContractBillingPlant(counterparty_cd, cont_no, agmt_no, agmt_rev_no, contract_type, temp_chk_plant[m], chk_plant[i], chk_plant.length);
							}
						}
					 }
				}
				
				//BUSINESS UNIT
				query1="DELETE FROM FMS_TRADER_CONT_BU A "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? AND A.CONT_NO=? AND A.CONT_REV=?  "
						+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONTRACT_TYPE=?  "
						+ "AND EXISTS( "
						+ "SELECT * FROM FMS_TRADER_CONT_BU B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD AND A.CONT_NO=B.CONT_NO "
						+ "AND A.CONT_REV=B.CONT_REV AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV  "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE "
						+ ")";
				stmt1=dbcon.prepareStatement(query1);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, cont_no);
				stmt1.setString(4, cont_rev_no);
				stmt1.setString(5, agmt_no);
				stmt1.setString(6, agmt_rev_no);
				stmt1.setString(7, contract_type);
				stmt1.executeUpdate();
				stmt1.close();
				if(chk_bu_plant!=null)
				{
					for(int i=0;i<chk_bu_plant.length;i++)
					{
						query2 = "INSERT INTO FMS_TRADER_CONT_BU(COMPANY_CD, COUNTERPARTY_CD, AGMT_NO, AGMT_REV, CONT_NO, CONT_REV, CONTRACT_TYPE,PLANT_SEQ_NO, ENT_BY, ENT_DT) "
								+ "VALUES(?,?, ?, ?,?,?,?,?,?,SYSDATE) ";
						stmt2=dbcon.prepareStatement(query2);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_no);
						stmt2.setString(4, agmt_rev_no);
						stmt2.setString(5, cont_no);
						stmt2.setString(6, cont_rev_no);
						stmt2.setString(7, contract_type);
						stmt2.setString(8, chk_bu_plant[i]);
						stmt2.setString(9, emp_cd);
						stmt2.executeUpdate();
						
						stmt2.close();
					 }
				}
				
				//CARGO DETAIL
				String old_rev_no = ""+(Integer.parseInt(cont_rev_no)-1);
				query ="INSERT INTO FMS_TRADER_CARGO_MST(COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,"
						+ "CARGO_NO,CARGO_REF,CARGO_STATUS,CARGO_QTY,RATE,RATE_UNIT,START_DT,END_DT,ENT_DT,ENT_BY,MODIFY_DT,MODIFY_BY) "
						+ "SELECT COMPANY_CD,COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,CONT_NO,?,CONTRACT_TYPE,"
						+ "CARGO_NO,CARGO_REF,CARGO_STATUS,CARGO_QTY,RATE,RATE_UNIT,START_DT,END_DT,ENT_DT,ENT_BY,MODIFY_DT,? "
						+ "FROM FMS_TRADER_CARGO_MST A "
						+ "WHERE A.COMPANY_CD=? AND A.COUNTERPARTY_CD=? "
						+ "AND A.AGMT_NO=? AND A.AGMT_REV=? AND A.CONT_NO=? "
						+ "AND A.CONTRACT_TYPE=? AND A.CONT_REV=? AND "
						+ "NOT EXISTS( "
						+ "SELECT *  "
						+ "FROM FMS_TRADER_CARGO_MST  B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD  "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV= B.AGMT_REV  AND A.CONT_NO=B.CONT_NO "
						+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND B.CONT_REV=?"
						+ ")";
				stmt2 = dbcon.prepareStatement(query);
				stmt2.setString(1, cont_rev_no);
				stmt2.setString(2, emp_cd);
				stmt2.setString(3, comp_cd);
				stmt2.setString(4, counterparty_cd);
				stmt2.setString(5, agmt_no);
				stmt2.setString(6, agmt_rev_no);
				stmt2.setString(7, cont_no);
				stmt2.setString(8, contract_type);
				stmt2.setString(9, old_rev_no);
				stmt2.setString(10, cont_rev_no);
				
				stmt2.executeUpdate();
				stmt2.close();
				
				
				if(opration.equals("INSERT"))
				{
					operation="INSERT";
				}
				else
				{
					operation="MODIFY";
				}
				
				if(opration.equals("INSERT"))
				{
					msg = "Successful! - New Confirmation Notice "+cont_name_map+" Added for "+counterparty_abbr+" Successfully!";
					opration="MODIFY";
				}
				else
				{
					msg = "Successful! - Confirmation Notice "+cont_name_map+" Modified for "+counterparty_abbr+" Successfully!";
				}
				msg_type="S";
			}
			else
			{
				msg = "Failed! - Confirmation Notice "+cont_name_map+" for "+counterparty_abbr+" Modification Failed!";
				msg_type="E";
			}
			String cp_name = ""+utilBean.getCounterpartyName(dbcon,counterparty_cd);
			String cp_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			
			String mapped_cont_no = utilBean.NewDealMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, cont_no, cont_rev_no, contract_type, "");
			
			new_value ="CP="+counterparty_cd+"#NAME="+cp_name+"#ABBR="+cp_abbr+"#CONTNAME="+cont_name+"#CONTNO="+mapped_cont_no+"#CONTREFNO="+cont_ref_no+"#CONTTYPE="+contract_type+"#TRADE_REFNO="+trade_ref_no+"#DDADT="+dda_dt+"#DDATIME="+dda_time+"#SIGNDT="+signing_dt+"#SIGNTIME="+signing_time+
					"#ENTDT="+ent_dt+"#ENTTIME="+ent_time+"#AGMTTYPE="+agreement_type+"#AGMTBASE="+agreement_base+"#STARTDT="+start_dt+"#ENDDT="+end_dt+"#CONT_STATUS="+cont_status_flg;
			
			url = "../cargo/frm_confirm_notice_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
					"&cont_no="+cont_no+"&cont_rev_no="+cont_rev_no+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+
					"&contract_type="+contract_type+commonUrl_pra;
			
			dbcon.commit();
			
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Confirmation Notice Insert/Update Failed";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void InsertUpdateAgreementDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateAgreementDetail()";
		msg="";
		msg_type="";
		url="";
		String agmt_name_map="";
		try
		{
			old_value=request.getParameter("old_value")==null?"":request.getParameter("old_value");
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			String agmt_ref_no = request.getParameter("agmt_ref_no")==null?"":request.getParameter("agmt_ref_no");
			String signing_dt = request.getParameter("signing_dt")==null?"":request.getParameter("signing_dt");
			String signing_time = request.getParameter("signing_time")==null?"":request.getParameter("signing_time");
			String agreement_type = request.getParameter("agreement_type")==null?"":request.getParameter("agreement_type");
			String agreement_typ = request.getParameter("agreement_typ")==null?"0":request.getParameter("agreement_typ");
			String agreement_base = request.getParameter("agreement_base")==null?"":request.getParameter("agreement_base");
			String start_dt = request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			String end_dt = request.getParameter("end_dt")==null?"":request.getParameter("end_dt");
			String mdcq = request.getParameter("mdcq")==null?"":request.getParameter("mdcq");
			String status = request.getParameter("status")==null?"":request.getParameter("status");
			if(status.equals("")) {
				status="A";
			}
			String ent_dt = request.getParameter("ent_dt")==null?"":request.getParameter("ent_dt");
			String ent_time = request.getParameter("ent_time")==null?"":request.getParameter("ent_time");
			String[] chk_plant = request.getParameterValues("chk_plant");
			
			String[] chk_trans = request.getParameterValues("chk_trans");
			String[] trans_cd = request.getParameterValues("trans_cd");
			String[] trans_plant_seq_no = request.getParameterValues("trans_plant_seq_no");
			
			String[] chk_bu_plant = request.getParameterValues("chk_bu_plant");
			
			String agmt_type = request.getParameter("agmt_type")==null?"":request.getParameter("agmt_type");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String remark="";
			remark=escObj.replaceSingleQuotes(remark);
			
			String billing_flag = request.getParameter("billing_flag")==null?"":request.getParameter("billing_flag");
			
			String buyer_nom = request.getParameter("buyer_nom")==null?"N":request.getParameter("buyer_nom");
			String buy_nom_cutoff = request.getParameter("buy_nom_cutoff")==null?"N":request.getParameter("buy_nom_cutoff");
			String[] chk_buyer_nom = request.getParameterValues("chk_buyer_nom");
			String seller_nom = request.getParameter("seller_nom")==null?"N":request.getParameter("seller_nom");
			String[] chk_seller_nom = request.getParameterValues("chk_seller_nom");
			
			//String day_def = request.getParameter("day_def")==null?"N":request.getParameter("day_def");
			//String day_time_from = request.getParameter("day_time_from")==null?"":request.getParameter("day_time_from");
			//String day_time_to = request.getParameter("day_time_to")==null?"":request.getParameter("day_time_to");

			String flag = request.getParameter("flag")==null?"":request.getParameter("flag");
			String demmurage = request.getParameter("demmurageCheckbox")==null?"":request.getParameter("demmurageCheckbox");
			String demmurage_rate = request.getParameter("demmurage_rate")==null?"":request.getParameter("demmurage_rate");
			String demmurage_rate_unit = request.getParameter("demmurage_rate_unit")==null?"":request.getParameter("demmurage_rate_unit");
			String alw_laytime_hrs = request.getParameter("alw_laytime_hrs")==null?"":request.getParameter("alw_laytime_hrs");
			String alw_laytime_mns = request.getParameter("alw_laytime_mns")==null?"":request.getParameter("alw_laytime_mns");
			String measurement = request.getParameter("measurementCheckbox")==null?"":request.getParameter("measurementCheckbox");
			String meas_standard = request.getParameter("meas_standard")==null?"":request.getParameter("meas_standard");
			String meas_temperature = request.getParameter("meas_temperature")==null?"":request.getParameter("meas_temperature");
			String pressure_min_bar = request.getParameter("pressure_min_bar")==null?"":request.getParameter("pressure_min_bar");
			String pressure_max_bar = request.getParameter("pressure_max_bar")==null?"":request.getParameter("pressure_max_bar");
			String off_spec_gas = request.getParameter("off_spec_gas_checkbox")==null?"":request.getParameter("off_spec_gas_checkbox");
			String spec_gas_energy_base = request.getParameter("spec_gas_energy_base")==null?"":request.getParameter("spec_gas_energy_base");
			String spec_gas_min_energy = request.getParameter("spec_gas_min_energy")==null?"":request.getParameter("spec_gas_min_energy");
			String spec_gas_max_energy = request.getParameter("spec_gas_max_energy")==null?"":request.getParameter("spec_gas_max_energy");
			String liability = request.getParameter("liability_checkbox")==null?"":request.getParameter("liability_checkbox");
			String liability_clause = request.getParameter("liability_clause")==null?"":request.getParameter("liability_clause");
			String liab_lq_damg = request.getParameter("liab_lq_damg")==null?"":request.getParameter("liab_lq_damg");
			String liab_take_pay = request.getParameter("liab_take_pay")==null?"":request.getParameter("liab_take_pay");
			String liab_makeup = request.getParameter("liab_makeup")==null?"":request.getParameter("liab_makeup");
			String billing_clause = request.getParameter("billing_clause_no")==null?"":request.getParameter("billing_clause_no");
			String terminate_flag = request.getParameter("terminator_checkbox")==null?"":request.getParameter("terminator_checkbox");
			String terminate_clause = request.getParameter("terminate_clause")==null?"":request.getParameter("terminate_clause");
			String terminate_planed = request.getParameter("terminate_planed")==null?"":request.getParameter("terminate_planed");
			String terminate_force = request.getParameter("terminate_force")==null?"":request.getParameter("terminate_force");
			String reopen_request_flag = request.getParameter("reopen_request_flag")==null?"":request.getParameter("reopen_request_flag");
			String reopen_request_by = request.getParameter("reopen_request_by")==null?"":request.getParameter("reopen_request_by");
			String reopen_request_dt = request.getParameter("reopen_request_dt")==null?"":request.getParameter("reopen_request_dt");
			String reopen_approval_flag = request.getParameter("reopen_approval_flag")==null?"":request.getParameter("reopen_approval_flag");
			String reopen_approve_by = request.getParameter("reopen_approve_by")==null?"":request.getParameter("reopen_approve_by");
			String reopen_approval_dt = request.getParameter("reopen_approval_dt")==null?"":request.getParameter("reopen_approval_dt");
			String reopen_remark = request.getParameter("reopen_remark")==null?"":request.getParameter("reopen_remark");
			
			String meas_clause = request.getParameter("measure_clause_no")==null?"":request.getParameter("measure_clause_no");
			String demurage_clause = request.getParameter("demurrage_clause_no")==null?"":request.getParameter("demurrage_clause_no");
			String spec_clause = request.getParameter("spec_clause_no")==null?"":request.getParameter("spec_clause_no");
			
			String day_def_clause_no = request.getParameter("day_def_clause_no")==null?"":request.getParameter("day_def_clause_no");
			String day_def = request.getParameter("day_def")==null?"N":request.getParameter("day_def");
			String day_time_from = request.getParameter("day_time_from")==null?"":request.getParameter("day_time_from");
			String day_time_to = request.getParameter("day_time_to")==null?"":request.getParameter("day_time_to");
			String rev_chk = request.getParameter("rev_chk")==null?"":request.getParameter("rev_chk");
			String rev_eff_dt = request.getParameter("rev_eff_dt")==null?"":request.getParameter("rev_eff_dt");
			
			if(opration.equals("INSERT"))
			{
				old_value="";
				query="SELECT MAX(AGMT_NO) FROM FMS_TRADER_AGMT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=?";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt_type);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					agmt_no = ""+(rset.getInt(1)+1);
				}
				else
				{
					agmt_no = "1";
				}
				agmt_rev_no="0";
				rset.close();
				stmt.close();
			}
			else if(opration.equals("MODIFY"))
			{
				if(rev_chk.equals("Y"))
				{
					query="SELECT MAX(AGMT_REV) FROM FMS_TRADER_AGMT_MST "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_TYPE=? AND AGMT_NO=?";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, agmt_type);
					stmt.setString(4, agmt_no);
					rset=stmt.executeQuery();
					if(rset.next())
					{
						agmt_rev_no = ""+(rset.getInt(1)+1);
					}
					else
					{
						agmt_rev_no = "0";
					}
					rset.close();
					stmt.close();
				}
			}
			
			String sysdt = utilDate.getSysdate();
			
			String agmt_name = comp_abbr+"-"+counterparty_abbr+"-"+agmt_type+agmt_no+"-REV"+agmt_rev_no;
			//agmt_name_map = counterparty_abbr+"-"+agmt_type+agmt_no+"-"+agmt_rev_no;
			agmt_name_map = utilBean.NewAgmtMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, agmt_type);
			
			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals(""))
			{
				int rev_count=0;
				query = "SELECT COUNT(*) FROM FMS_TRADER_AGMT_MST "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=?";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, comp_cd);
				stmt.setString(2, counterparty_cd);
				stmt.setString(3, agmt_type);
				stmt.setString(4, agmt_no);
				stmt.setString(5, agmt_rev_no);
				rset=stmt.executeQuery();
				if(rset.next())
				{
					rev_count = rset.getInt(1);
				}
				rset.close();
				stmt.close();
			
				if(rev_count > 0)
				{
					
					query ="UPDATE FMS_TRADER_AGMT_MST SET AGMT_REF_NO=?,SIGNING_DT=TO_DATE(?,'DD/MM/YYYY'),"
							+ "START_DT=TO_DATE(?,'DD/MM/YYYY'),END_DT=TO_DATE(?,'DD/MM/YYYY'),AGMT_BASE=?,AGMT_TYP=?,"
							+ "DAY_DEF=?,DAY_START_TIME=?,DAY_END_TIME=?,"
							+ "REMARK=?,AGMT_NAME=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,STATUS=?,"
							+ "BILLING_FLAG=?,REV_DT=TO_DATE(?,'DD/MM/YYYY'),"
							+ "ALW_LAYTIME_HRS=?,ALW_LAYTIME_MNS=?,"
							+ "MEASUREMENT=?,MEAS_STANDARD=?,MEAS_TEMPERATURE=?,OFF_SPEC_GAS=?,"
							+ "SPEC_GAS_ENERGY_BASE=?,SPEC_GAS_MIN_ENERGY=?,SPEC_GAS_MAX_ENERGY=?,"
							+ "LIABILITY=?,LIABILITY_CLAUSE=?,"
							+ "TERMINATE_FLAG=?,TERMINATE_CLAUSE=?,TERMINATE_PLANED=?,TERMINATE_FORCE=?,PRESSURE_MIN_BAR=?,PRESSURE_MAX_BAR=?, "
							+ "MEAS_CLAUSE=? , DEMURRAGE_CLAUSE=? , SPEC_CLAUSE=?,BILLING_CLAUSE=?,DEMURRAGE=?,DAY_DEF_CLAUSE=?,DEMURRAGE_RATE=?,DEMURRAGE_RATE_UNIT=? "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? AND AGMT_TYPE=?";
					stmt0 = dbcon.prepareStatement(query);
					stmt0.setString(1, agmt_ref_no);
					stmt0.setString(2, signing_dt);
					stmt0.setString(3, start_dt);
					stmt0.setString(4, end_dt);
					stmt0.setString(5, agreement_base);
					stmt0.setString(6, agreement_type);
					stmt0.setString(7, day_def);
					stmt0.setString(8, day_time_from);
					stmt0.setString(9, day_time_to);
					stmt0.setString(10, remark);
					stmt0.setString(11, agmt_name);
					stmt0.setString(12, emp_cd);
					stmt0.setString(13, status);
					stmt0.setString(14, billing_flag);
					stmt0.setString(15, rev_eff_dt);
					stmt0.setString(16, alw_laytime_hrs);
					stmt0.setString(17, alw_laytime_mns);
					stmt0.setString(18, measurement);
					stmt0.setString(19, meas_standard);
					stmt0.setString(20, meas_temperature);
					stmt0.setString(21, off_spec_gas);
					stmt0.setString(22, spec_gas_energy_base);
					stmt0.setString(23, spec_gas_min_energy);
					stmt0.setString(24, spec_gas_max_energy);
					stmt0.setString(25, liability);
					stmt0.setString(26, liability_clause);
					stmt0.setString(27, terminate_flag);
					stmt0.setString(28, terminate_clause);
					stmt0.setString(29, terminate_planed);
					stmt0.setString(30, terminate_force);
					stmt0.setString(31, pressure_min_bar);
					stmt0.setString(32, pressure_max_bar);
					stmt0.setString(33, meas_clause);
					stmt0.setString(34, demurage_clause);
					stmt0.setString(35, spec_clause);
					stmt0.setString(36, billing_clause);
					stmt0.setString(37, demmurage);
					stmt0.setString(38, day_def_clause_no);
					stmt0.setString(39, demmurage_rate);
					stmt0.setString(40, demmurage_rate_unit);
					stmt0.setString(41, comp_cd);
					stmt0.setString(42, counterparty_cd);
					stmt0.setString(43, agmt_no);
					stmt0.setString(44, agmt_rev_no);
					stmt0.setString(45, agmt_type);
					stmt0.executeUpdate();
					
					stmt0.close();
				}
				else
				{
					int cnt = 0;
					query = "INSERT INTO FMS_TRADER_AGMT_MST(COMPANY_CD,"
							+ "COUNTERPARTY_CD,AGMT_TYPE,AGMT_NO,AGMT_REV,AGMT_NAME,AGMT_REF_NO,AGMT_BASE,AGMT_TYP,"
							+ "SIGNING_DT,START_DT,END_DT,STATUS,FLAG,REMARK,ENT_BY,ENT_DT,REV_DT,"
							+ "DAY_DEF,DAY_START_TIME,DAY_END_TIME,DEMURRAGE_RATE,DEMURRAGE_RATE_UNIT,"
							+ "ALW_LAYTIME_HRS,ALW_LAYTIME_MNS,MEASUREMENT,MEAS_STANDARD,MEAS_TEMPERATURE,PRESSURE_MIN_BAR,"
							+ "PRESSURE_MAX_BAR,OFF_SPEC_GAS,SPEC_GAS_ENERGY_BASE,SPEC_GAS_MIN_ENERGY,SPEC_GAS_MAX_ENERGY,"
							+ "LIABILITY,LIABILITY_CLAUSE,BILLING_FLAG,BILLING_CLAUSE,"
							+ "TERMINATE_FLAG,TERMINATE_CLAUSE,TERMINATE_PLANED,TERMINATE_FORCE,REOPEN_REQUEST_FLAG,"
							+ "REOPEN_REQUEST_BY,"
							+ "REOPEN_REQUEST_DT,REOPEN_APPROVAL_FLAG,REOPEN_APPROVE_BY,"
							+ "REOPEN_APPROVAL_DT,REOPEN_REMARK,DEMURRAGE,DEMURRAGE_CLAUSE,MEAS_CLAUSE,SPEC_CLAUSE,"
							+ "DAY_DEF_CLAUSE) "
							+ "VALUES(?,?,?,?,?,?,?,?,?,"
							+ "TO_DATE(?,'DD/MM/YYYY'),"
							+ "TO_DATE(?,'DD/MM/YYYY'),"
							+ "TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,"
							+ "SYSDATE,"
							+ "TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,"
							+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
							+ "TO_DATE(?,'DD/MM/YYYY'),?,"
							+ "?,"
							+ "TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,"
							+ "?)";
					stmt0 = dbcon.prepareStatement(query);
					stmt0.setString(++cnt, comp_cd);
					stmt0.setString(++cnt, counterparty_cd);
					stmt0.setString(++cnt, agmt_type);
					stmt0.setString(++cnt, agmt_no);
					stmt0.setString(++cnt, agmt_rev_no);
					stmt0.setString(++cnt, agmt_name);
					stmt0.setString(++cnt, agmt_ref_no);
					stmt0.setString(++cnt, agreement_base);
					stmt0.setString(++cnt, agreement_type);
					stmt0.setString(++cnt, signing_dt);
					stmt0.setString(++cnt, start_dt);
					stmt0.setString(++cnt, end_dt);
					stmt0.setString(++cnt, status);
					stmt0.setString(++cnt, flag);
					stmt0.setString(++cnt, remark);
					stmt0.setString(++cnt, emp_cd);
					stmt0.setString(++cnt, rev_eff_dt);
					stmt0.setString(++cnt, day_def);
					stmt0.setString(++cnt, day_time_from);
					stmt0.setString(++cnt, day_time_to);
					stmt0.setString(++cnt, demmurage_rate);
					stmt0.setString(++cnt, demmurage_rate_unit);
					stmt0.setString(++cnt, alw_laytime_hrs);
					stmt0.setString(++cnt, alw_laytime_mns);
					stmt0.setString(++cnt, measurement);
					stmt0.setString(++cnt, meas_standard);
					stmt0.setString(++cnt, meas_temperature);
					stmt0.setString(++cnt, pressure_min_bar);
					stmt0.setString(++cnt, pressure_max_bar);
					stmt0.setString(++cnt, off_spec_gas);
					stmt0.setString(++cnt, spec_gas_energy_base);
					stmt0.setString(++cnt, spec_gas_min_energy);
					stmt0.setString(++cnt, spec_gas_max_energy);
					stmt0.setString(++cnt, liability);
					stmt0.setString(++cnt, liability_clause);
					stmt0.setString(++cnt, billing_flag);
					stmt0.setString(++cnt, billing_clause);
					stmt0.setString(++cnt, terminate_flag);
					stmt0.setString(++cnt, terminate_clause);
					stmt0.setString(++cnt, terminate_planed);
					stmt0.setString(++cnt, terminate_force);
					stmt0.setString(++cnt, reopen_request_flag);
					stmt0.setString(++cnt, reopen_request_by);
					stmt0.setString(++cnt, reopen_request_dt);
					stmt0.setString(++cnt, reopen_approval_flag);
					stmt0.setString(++cnt, reopen_approve_by);
					stmt0.setString(++cnt, reopen_approval_dt);
					stmt0.setString(++cnt, reopen_remark);
					stmt0.setString(++cnt, demmurage);
					stmt0.setString(++cnt, demurage_clause);
					stmt0.setString(++cnt, meas_clause);
					stmt0.setString(++cnt, spec_clause);
					stmt0.setString(++cnt, day_def_clause_no);
					stmt0.executeUpdate();

					
					stmt0.close();
				}
				
				int count = 0;
				
				//TRADER PLANT
				query = "DELETE FROM FMS_TRADER_AGMT_PLANT A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND "
						+ "EXISTS (SELECT * "
						+ "FROM FMS_TRADER_AGMT_PLANT B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV) ";
				
				stmt1 = dbcon.prepareStatement(query);
				stmt1.setString(1, comp_cd);
				stmt1.setString(2, counterparty_cd);
				stmt1.setString(3, agmt_type);
				stmt1.setString(4, agmt_no);
				stmt1.setString(5, agmt_rev_no);
				stmt1.executeUpdate();
				stmt1.close();
				if(chk_plant!=null)
				{
					for(int i=0;i<chk_plant.length;i++)
					{
						query = "INSERT INTO FMS_TRADER_AGMT_PLANT(COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE,AGMT_NO, AGMT_REV, PLANT_SEQ_NO, ENT_BY, ENT_DT) "
								+ "VALUES(?,?,?,?,?,?,?,SYSDATE) ";
						stmt2 = dbcon.prepareStatement(query);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_type);
						stmt2.setString(4, agmt_no);
						stmt2.setString(5, agmt_rev_no);
						stmt2.setString(6, chk_plant[i]);
						stmt2.setString(7, emp_cd);
						stmt2.executeUpdate();
						
						stmt2.close();
					 }
				}
				
				//BUSINESS UNIT
				query = "DELETE FROM FMS_TRADER_AGMT_BU A  "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=?  "
						+ "AND AGMT_TYPE=? AND AGMT_NO=? AND AGMT_REV=? AND "
						+ "EXISTS (SELECT * "
						+ "FROM FMS_TRADER_AGMT_BU B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD  "
						+ "AND A.AGMT_TYPE=B.AGMT_TYPE AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV) ";
				stmt_temp = dbcon.prepareStatement(query);
				stmt_temp.setString(1, comp_cd);
				stmt_temp.setString(2, counterparty_cd);
				stmt_temp.setString(3, agmt_type);
				stmt_temp.setString(4, agmt_no);
				stmt_temp.setString(5, agmt_rev_no);
				stmt_temp.executeUpdate();
				stmt_temp.close();
				
				if(chk_bu_plant!=null)
				{
					for(int i=0;i<chk_bu_plant.length;i++)
					{
						query = "INSERT INTO FMS_TRADER_AGMT_BU(COMPANY_CD, COUNTERPARTY_CD, AGMT_TYPE,AGMT_NO, AGMT_REV, PLANT_SEQ_NO, ENT_BY, ENT_DT) "
								+ "VALUES(?,?,?,?,?,?,?,SYSDATE) ";
						stmt2 = dbcon.prepareStatement(query);
						stmt2.setString(1, comp_cd);
						stmt2.setString(2, counterparty_cd);
						stmt2.setString(3, agmt_type);
						stmt2.setString(4, agmt_no);
						stmt2.setString(5, agmt_rev_no);
						stmt2.setString(6, chk_bu_plant[i]);
						stmt2.setString(7, emp_cd);
						stmt2.executeUpdate();
						
						stmt2.close();
					}
				}
				
				if(opration.equals("INSERT"))
				{
					msg = "Successful! - New Agreement "+agmt_name_map+" Added for "+counterparty_abbr+" Successfully!";
					opration="MODIFY";
				}
				else
				{
					msg = "Successful! - Agreement "+agmt_name_map+" Modified for "+counterparty_abbr+" Successfully!";
				}
				msg_type="S";
			}
			else
			{
				msg = "Failed! - Agreement "+agmt_name_map+" for "+counterparty_abbr+" Modification Failed!";
				msg_type="E";
			}
			String cp_name = ""+utilBean.getCounterpartyName(dbcon,counterparty_cd);
			String cp_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			
			new_value="CP="+counterparty_cd+"#NAME="+cp_name+"#ABBR="+cp_abbr+"#AGMTNAME="+agmt_name+"#AGMTNO="+agmt_no+"#AGMTREFNO="+agmt_ref_no+"#AGMTTYPE="+agmt_type+"#SIGNDT="+signing_dt+"#SIGNTIME="+signing_time+
					"#ENTDT="+ent_dt+"#ENTTIME="+ent_time+"#AGMTTYP="+agreement_type+"#AGMTBASE="+agreement_base+"#STARTDT="+start_dt+"#ENDDT="+end_dt;
			
			url = "../cargo/frm_mspa_mst.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&opration="+opration+
					"&agreement_type="+agmt_type+"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&clearance="+clearance+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! Agreement "+agmt_name_map+" Insert/Update Failed";
		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void InsertUpdateAgreementBillingDetail(HttpServletRequest request) throws SQLException 
	{
		String function_nm="InsertUpdateAgreementBillingDetail()";
		msg="";
		msg_type="";
		url="";
		String agmt_name_map ="";
		try
		{
			String opration = request.getParameter("opration")==null?"":request.getParameter("opration");
			String clearance = request.getParameter("clearance")==null?"":request.getParameter("clearance");
			
			String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
			String agmt_no=request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
			String agmt_rev_no=request.getParameter("agmt_rev_no")==null?"":request.getParameter("agmt_rev_no");
			String agreement_type=request.getParameter("agreement_type")==null?"F":request.getParameter("agreement_type");
			String start_dt=request.getParameter("start_dt")==null?"":request.getParameter("start_dt");
			
			String freq=request.getParameter("freq")==null?"F":request.getParameter("freq");
			String billing_flag=request.getParameter("billing_flag")==null?"Y":request.getParameter("billing_flag");
			String due_date=request.getParameter("due_date")==null?"":request.getParameter("due_date");
			String sec_due_date=request.getParameter("sec_due_date")==null?"":request.getParameter("sec_due_date");
			String inv_currency=request.getParameter("inv_currency")==null?"":request.getParameter("inv_currency");
			String payment_currency=request.getParameter("payment_currency")==null?"":request.getParameter("payment_currency");
			String rate=request.getParameter("rate")==null?"":request.getParameter("rate");
			String plusmin=request.getParameter("plusmin")==null?"":request.getParameter("plusmin");
			String modeper=request.getParameter("modeper")==null?"":request.getParameter("modeper");
			String exchng_rate=request.getParameter("exchng_rate")==null?"":request.getParameter("exchng_rate");
			String exch_calc_base=request.getParameter("exch_calc_base")==null?"":request.getParameter("exch_calc_base");
			String inv_criteria=request.getParameter("inv_criteria")==null?"":request.getParameter("inv_criteria");
			String exchg_rate_note=request.getParameter("exchg_rate_note")==null?"":request.getParameter("exchg_rate_note");
			
			String due_dt_in=request.getParameter("due_dt_in")==null?"":request.getParameter("due_dt_in");
			String exclude_sat=request.getParameter("exclude_sat")==null?"N":request.getParameter("exclude_sat");
			String exchg_val=request.getParameter("exchg_val")==null?"":request.getParameter("exchg_val");
			String holidayState_map = request.getParameter("holidayState_map")==null?"":request.getParameter("holidayState_map");
			String[] sat=request.getParameterValues("sat");
			String exclude_sat_map = "";
			if(exclude_sat.equals("Y"))
			{
				if(sat!=null)
				{
					for(int i=0; i<sat.length; i++)
					{
						 if (sat[i].contains("Y")) 
						 {
			                if (exclude_sat_map.length() > 0) 
			                {
			                	exclude_sat_map+="-"; 
			                }
			                exclude_sat_map+=sat[i].charAt(0);
						 }
					}
				}
			}
			else
			{
				exclude_sat_map = "";
			}
			
			String[] cust_plant_map = holidayState_map.split("@@");
			String counterparty_abbr = ""+utilBean.getCounterpartyABBR(dbcon,counterparty_cd);
			//agmt_name_map = counterparty_abbr+"-"+agreement_type+agmt_no+"-"+agmt_rev_no;
			agmt_name_map = utilBean.NewAgmtMappingId(comp_cd, counterparty_cd, agmt_no, agmt_rev_no, agreement_type);

			if(!comp_cd.equals("") && !counterparty_cd.equals("") && !agmt_no.equals("") && !agmt_rev_no.equals("") && !agreement_type.equals(""))
			{
				String queryString="SELECT PLANT_SEQ_NO "
						+ "FROM FMS_TRADER_AGMT_PLANT A "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND AGMT_NO=? "
						+ "AND AGMT_TYPE=? "
						+ "AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_TRADER_AGMT_PLANT B "
						+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
						+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE)";
				stmt3 = dbcon.prepareStatement(queryString);
				stmt3.setString(1, comp_cd);
				stmt3.setString(2, counterparty_cd);
				stmt3.setString(3, agmt_no);
				//stmt3.setString(4, agmt_rev_no);
				stmt3.setString(4, agreement_type);
				rset3=stmt3.executeQuery();
				while(rset3.next())
				{
					String plant_seq = rset3.getString(1)==null?"":rset3.getString(1);
					String state_map = "";
					for(int i=0; i<cust_plant_map.length; i++)
					{
						if(cust_plant_map[i].startsWith(plant_seq))
						{
							state_map = cust_plant_map[i].split("//")[1];
						}
					}
					
					int count=0;
					query = "SELECT COUNT(*) "
							+ "FROM FMS_TRADER_AGMT_BILLING_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND AGMT_NO=? AND AGMT_TYPE=? AND PLANT_SEQ_NO=? "
							+ "AND AGMT_REV=(SELECT MAX(B.AGMT_REV) FROM FMS_TRADER_AGMT_BILLING_DTL B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_TYPE=B.AGMT_TYPE AND A.PLANT_SEQ_NO=B.PLANT_SEQ_NO)";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, comp_cd);
					stmt.setString(2, counterparty_cd);
					stmt.setString(3, agmt_no);
					//stmt.setString(4, agmt_rev_no);
					stmt.setString(4, agreement_type);
					stmt.setString(5, plant_seq);
					rset = stmt.executeQuery();
					count = 0;
					if(rset.next())
					{
						 count = rset.getInt(1);
					}
					rset.close();
					stmt.close();
					
					if(count > 0)
					{
						int cnt=0;
						query="UPDATE FMS_TRADER_AGMT_BILLING_DTL SET BILLING_FREQ=?,BILLING_FLAG=?,DUE_DATE=?,SECOND_DUE_DT=?,"
								+ "INVOICE_CUR_CD=?,PAYMENT_CUR_CD=?,INT_CAL_RATE_CD=?,INT_CAL_SIGN=?,"
								+ "INT_CAL_PERCENTAGE=?,EXCHNG_RATE_CD=?,EXCHNG_RATE_CAL=?,"
								+ "EXCHNG_CRITERIA=?,EXCHG_RATE_NOTE=?,MODIFY_DT=SYSDATE,MODIFY_BY=?,"
								+ "DUE_DT_IN=?,EXCLUDE_SAT=?,EXCHG_VAL=?,EXCL_SAT_MAP=?,HOLIDAY_STATE=? "
								+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
								+ "AND AGMT_NO=? AND AGMT_TYPE=? AND PLANT_SEQ_NO=?";
						stmt1 =dbcon.prepareStatement(query);
						stmt1.setString(++cnt, freq);
						stmt1.setString(++cnt, billing_flag);
						stmt1.setString(++cnt, due_date);
						stmt1.setString(++cnt, sec_due_date);
						stmt1.setString(++cnt, inv_currency);
						stmt1.setString(++cnt, payment_currency);
						stmt1.setString(++cnt, rate);
						stmt1.setString(++cnt, plusmin);
						stmt1.setString(++cnt, modeper);
						stmt1.setString(++cnt, exchng_rate);
						stmt1.setString(++cnt, exch_calc_base);
						stmt1.setString(++cnt, inv_criteria);
						stmt1.setString(++cnt, exchg_rate_note);
						stmt1.setString(++cnt, emp_cd);
						stmt1.setString(++cnt, due_dt_in);
						stmt1.setString(++cnt, exclude_sat);
						stmt1.setString(++cnt, exchg_val);
						stmt1.setString(++cnt, exclude_sat_map);
						stmt1.setString(++cnt, state_map);
						stmt1.setString(++cnt, comp_cd);
						stmt1.setString(++cnt, counterparty_cd);
						stmt1.setString(++cnt, agmt_no);
						//stmt1.setString(++cnt, agmt_rev_no);
						stmt1.setString(++cnt, agreement_type);
						stmt1.setString(++cnt, plant_seq);
						stmt1.executeUpdate();
						
						stmt1.close();
						
						msg = "Successful! - "+agmt_name_map+" Billing Detail for "+counterparty_abbr+"  Modified Successfully!";
						msg_type = "S";
					}
					else
					{
						int cnt1=0;
						query="INSERT INTO FMS_TRADER_AGMT_BILLING_DTL(COMPANY_CD,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,AGMT_TYPE,BILLING_FREQ,"
								+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,"
								+ "EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,ENT_DT,ENT_BY,DUE_DT_IN,EXCLUDE_SAT,EXCHG_VAL,EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE) "
								+ "VALUES(?,?,?,?,?,?,"
								+ "?,?,?,?,?,?,?,?,?,"
								+ "?,?,?,SYSDATE,?,?,?,?,?,?,?)";
						stmt1 =dbcon.prepareStatement(query);
						stmt1.setString(++cnt1, comp_cd);
						stmt1.setString(++cnt1, counterparty_cd);
						stmt1.setString(++cnt1, agmt_no);
						stmt1.setString(++cnt1, agmt_rev_no);
						stmt1.setString(++cnt1, agreement_type);
						stmt1.setString(++cnt1, freq);
						stmt1.setString(++cnt1, billing_flag);
						stmt1.setString(++cnt1, due_date);
						stmt1.setString(++cnt1, sec_due_date);
						stmt1.setString(++cnt1, inv_currency);
						stmt1.setString(++cnt1, payment_currency);
						stmt1.setString(++cnt1, rate);
						stmt1.setString(++cnt1, plusmin);
						stmt1.setString(++cnt1, modeper);
						stmt1.setString(++cnt1, exchng_rate);
						stmt1.setString(++cnt1, exch_calc_base);
						stmt1.setString(++cnt1, inv_criteria);
						stmt1.setString(++cnt1, exchg_rate_note);
						stmt1.setString(++cnt1, emp_cd);
						stmt1.setString(++cnt1, due_dt_in);
						stmt1.setString(++cnt1, exclude_sat);
						stmt1.setString(++cnt1, exchg_val);
						stmt1.setString(++cnt1, exclude_sat_map);
						stmt1.setString(++cnt1, plant_seq);
						stmt1.setString(++cnt1, state_map);
						stmt1.executeUpdate();
						
						stmt1.close();
						
						msg = "Successful! - "+agmt_name_map+" Billing Detail for "+counterparty_abbr+" submitted Successfully!";
						msg_type = "S";
					}
				}
				rset3.close();
				stmt3.close();
			}
			else
			{
				msg = "Failed! - "+agmt_name_map+" Billing Detail for "+counterparty_abbr+" submission Failed!";
				msg_type = "E";
			}
			
			url = "../cargo/frm_mspa_agmt_billing_dtl.jsp?msg="+msg+"&msg_type="+msg_type+"&counterparty_cd="+counterparty_cd+"&agreement_type="+agreement_type+
					"&agmt_no="+agmt_no+"&agmt_rev_no="+agmt_rev_no+"&clearance="+clearance+"&start_dt="+start_dt+commonUrl_pra;
			
			dbcon.commit();
		}
		catch(Exception e)
		{
			dbcon.rollback();
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			url=CommonVariable.errorpage_url+"?e="+e;
			msg = "Error in Exception! "+agmt_name_map+" Billing Detail Insert/Update Failed";

		}
		
		try
		{
			new com.etrm.fms.util.InfoLogger().InsertInfoLogger(emp_cd, comp_cd,emp_nm, ip, form_id, form_nm,mod_cd,mod_nm, old_value, new_value, msg);  	
		}
		catch(Exception infoLogger)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, infoLogger);
		}
	}
	
	private void updateCargoContractBillingPlant(String counterpty_cd,String cont_no, String agmt_no, String agmt_rev_no, String contract_type, String plant_seq, String new_plant_seq, int new_plant_cnt) throws Exception
	{
		String function_nm="updateCargoContractBillingPlant()";
		
		try
		{
			int billing_count=0;
			query="SELECT COUNT(*) "
					+ "FROM FMS_TRADER_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND PLANT_SEQ_NO=? "
					+ "AND AGMT_NO=? "
					+ "AND CONTRACT_TYPE=? ";
			stmt = dbcon.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterpty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, plant_seq);
			stmt.setString(5, agmt_no);
			stmt.setString(6, contract_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				billing_count=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			int new_billing_count=0;
			query="SELECT COUNT(*) "
					+ "FROM FMS_TRADER_BILLING_DTL A "
					+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
					+ "AND CONT_NO=? AND PLANT_SEQ_NO=? "
					+ "AND AGMT_NO=? "
					+ "AND CONTRACT_TYPE=? ";
			stmt = dbcon.prepareStatement(query);
			stmt.setString(1, comp_cd);
			stmt.setString(2, counterpty_cd);
			stmt.setString(3, cont_no);
			stmt.setString(4, new_plant_seq);
			stmt.setString(5, agmt_no);
			stmt.setString(6, contract_type);
			rset=stmt.executeQuery();
			if(rset.next())
			{
				new_billing_count=rset.getInt(1);
			}
			rset.close();
			stmt.close();
			
			String hol_state=utilBean.getState_TIN(dbcon, comp_cd, counterpty_cd, "T", new_plant_seq);
			if(hol_state.equals(""))
			{
				hol_state="24";
			}
			if(billing_count > 0 && new_billing_count == 0)
			{
				if(new_plant_cnt==1)
				{
					query ="UPDATE FMS_TRADER_BILLING_DTL A SET PLANT_SEQ_NO=? "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? AND PLANT_SEQ_NO=? "
							+ "AND AGMT_NO=? "
							+ "AND CONTRACT_TYPE=? ";
					stmt = dbcon.prepareStatement(query);
					stmt.setString(1, new_plant_seq);
					stmt.setString(2, comp_cd);
					stmt.setString(3, counterpty_cd);
					stmt.setString(4, cont_no);
					stmt.setString(5, plant_seq);
					stmt.setString(6, agmt_no);
					stmt.setString(7, contract_type);
					stmt.executeUpdate();
					
					stmt.close();
				}
				else
				{
					
					query="INSERT INTO FMS_TRADER_BILLING_DTL(COMPANY_CD ,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,SPLIT_FLAG,"
							+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,"
							+ "EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,DUE_DT_IN,EXCLUDE_SAT,BILLING_DAYS,EXCHG_VAL,"
							+ "EXCL_SAT_MAP,PLANT_SEQ_NO,HOLIDAY_STATE) "
							+ "SELECT COMPANY_CD ,COUNTERPARTY_CD,AGMT_NO,AGMT_REV,CONT_NO,CONT_REV,CONTRACT_TYPE,BILLING_FREQ,SPLIT_FLAG,"
							+ "BILLING_FLAG,DUE_DATE,SECOND_DUE_DT,INVOICE_CUR_CD,PAYMENT_CUR_CD,INT_CAL_RATE_CD,INT_CAL_SIGN,INT_CAL_PERCENTAGE,EXCHNG_RATE_CD,"
							+ "EXCHNG_RATE_CAL,EXCHNG_CRITERIA,EXCHG_RATE_NOTE,TAX_STRUCT_CD,ENT_DT,ENT_BY,DUE_DT_IN,EXCLUDE_SAT,BILLING_DAYS,EXCHG_VAL,"
							+ "EXCL_SAT_MAP,?,? "
							+ "FROM FMS_TRADER_BILLING_DTL A "
							+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
							+ "AND CONT_NO=? "
							+ "AND AGMT_NO=? AND AGMT_REV=? "
							+ "AND CONTRACT_TYPE=? AND PLANT_SEQ_NO=? AND NOT EXISTS("
							+ "SELECT * "
							+ "FROM FMS_TRADER_BILLING_DTL B "
							+ "WHERE A.COMPANY_CD=B.COMPANY_CD AND A.COUNTERPARTY_CD=B.COUNTERPARTY_CD  "
							+ "AND A.AGMT_NO=B.AGMT_NO AND A.AGMT_REV=B.AGMT_REV AND A.CONT_NO=B.CONT_NO "
							+ "AND A.CONTRACT_TYPE=B.CONTRACT_TYPE AND A.PLANT_SEQ_NO=?)";
					stmt0 = dbcon.prepareStatement(query);
					stmt0.setString(1, new_plant_seq);
					stmt0.setString(2, hol_state);
					stmt0.setString(3, comp_cd);
					stmt0.setString(4, counterpty_cd);
					stmt0.setString(5, cont_no);
					stmt0.setString(6, agmt_no);
					stmt0.setString(7, agmt_rev_no);
					stmt0.setString(8, contract_type);
					stmt0.setString(9, plant_seq);
					stmt0.setString(10, new_plant_seq);
					stmt0.executeUpdate();
					
					stmt0.close();
				}
			}
			else
			{
				query ="UPDATE FMS_TRADER_BILLING_DTL A SET HOLIDAY_STATE=? "
						+ "WHERE COMPANY_CD=? AND COUNTERPARTY_CD=? "
						+ "AND CONT_NO=? AND PLANT_SEQ_NO=? "
						+ "AND AGMT_NO=? "
						+ "AND CONTRACT_TYPE=? AND HOLIDAY_STATE IS NULL";
				stmt = dbcon.prepareStatement(query);
				stmt.setString(1, hol_state);
				stmt.setString(2, comp_cd);
				stmt.setString(3, counterpty_cd);
				stmt.setString(4, cont_no);
				stmt.setString(5, new_plant_seq);
				stmt.setString(6, agmt_no);
				stmt.setString(7, contract_type);
				stmt.executeUpdate();
				
				stmt.close();
			}
		}
		catch(Exception e)
		{
			new SystemErrorLogger().InsertErrorLogger(frm_src_file_name, function_nm, e);
			throw e;
		}
	}
}
