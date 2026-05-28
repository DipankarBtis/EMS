<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*, java.util.*, javax.servlet.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>

function refresh() {
	
	var from_dt = document.getElementById('from_dt').value;
	var to_dt = document.getElementById('to_dt').value;
	var u = document.forms[0].u.value;
	
	var url = "frm_data_migrator.jsp?FromDt="+from_dt+"&ToDt="+to_dt+"&u="+u;

	document.getElementById("loading").style.visibility = "visible";
	location.replace(url);
	
}

function generateExcel() {
	
	var chk = document.getElementsByName('chk');
	var checked_ids = "";
	var checked_values = "";
	
	var from_dt = document.getElementById('from_dt').value;
	var to_dt = document.getElementById('to_dt').value;
	var u = document.forms[0].u.value;
	
	for (var z = 0; z < chk.length; z++) {
		if (chk[z].checked) {
			checked_values += chk[z].value + ",";
			checked_ids += chk[z].id + ",";
		}
	}
	
	if (checked_values == "") {
		alert("Kindly select atleast one checkbox.");
	}
	else {
		
		if (confirm("Are you sure you want to Submit ???")) {
			var url = "frm_data_migrator.jsp?checked_values="+checked_values+"&checked_ids="+checked_ids+"&flag=1&FromDt="+from_dt+"&ToDt="+to_dt+"&u="+u;

			document.getElementById("loading").style.visibility = "visible";
			location.replace(url);
		}
	
	}
	
}

function readExcel() {
	
	var chk = document.getElementsByName('chk');
	var checked_ids = "";
	var checked_values = "";
	
	var u = document.forms[0].u.value;
	
	var from_dt = document.getElementById('from_dt').value;
	var to_dt = document.getElementById('to_dt').value;
	
	for (var z = 0; z < chk.length; z++) {
		if (chk[z].checked) {
			checked_values += chk[z].value + ",";
			checked_ids += chk[z].id + ",";
		}
	}
	
	if (checked_values == "") {
		alert("Kindly select atleast one checkbox.");
	}
	else {
		
		if (confirm("Are you sure you want to Submit ???")) {
			var url = "frm_data_migrator.jsp?checked_values="+checked_values+"&checked_ids="+checked_ids+"&flag=2&FromDt="+from_dt+"&ToDt="+to_dt+"&u="+u;

			document.getElementById("loading").style.visibility = "visible";
			location.replace(url);
		}
	
	}
	
}

function rollback() {
	
	var chk = document.getElementsByName('chk');
	var checked_ids = "";
	var checked_values = "";
	
	var u = document.forms[0].u.value;

	var from_dt = document.getElementById('from_dt').value;
	var to_dt = document.getElementById('to_dt').value;
	
	for (var z = 0; z < chk.length; z++) {
		if (chk[z].checked) {
			checked_values += chk[z].value + ",";
			checked_ids += chk[z].id + ",";
		}
	}
	
	if (checked_values == "") {
		alert("Kindly select atleast one checkbox.");
	}
	else {
		
		if (confirm("Are you sure you want to Submit ???")) {
			var url = "frm_data_migrator.jsp?checked_values="+checked_values+"&checked_ids="+checked_ids+"&flag=3&FromDt="+from_dt+"&ToDt="+to_dt+"&u="+u;

			document.getElementById("loading").style.visibility = "visible";
			location.replace(url);
		}
	
	}
	
}

function checkVertical(module) {
	var chk = document.getElementsByName('chk');
	var checkAll = document.getElementById('checkAll-'+module);
	
	for (var z = 0; z < chk.length; z++) {
		if (chk[z].id.includes(module) && !chk[z].disabled) {
			if (checkAll.checked) {
				chk[z].checked = true;
			}
			else {
				chk[z].checked = false;
			}
		}
	}
}

</script>
</head>

<jsp:useBean class="com.etrm.fms.migration.Master_SEIPL_Data_Extractor" id="master_extractor" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.migration.Master_SEIPL_Data_Reader" id="master_reader" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.migration.Master_SEIPL_Data_RollBack" id="master_rollback" scope="request"></jsp:useBean>

<%-- <jsp:useBean class="com.etrm.fms.migration.Purchase_SEIPL_Data_Extractor" id="purchase_extractor" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.migration.Purchase_SEIPL_Data_Reader" id="purchase_reader" scope="request"></jsp:useBean> 
<jsp:useBean class="com.etrm.fms.migration.Purchase_SEIPL_Data_RollBack" id="purchase_rollback" scope="request"></jsp:useBean> 

<jsp:useBean class="com.etrm.fms.migration.Sales_SEIPL_Data_Extractor" id="sales_extractor" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.migration.Sales_SEIPL_Data_Reader" id="sales_reader" scope="request"></jsp:useBean>  

<jsp:useBean class="com.etrm.fms.migration.TermOps_SEIPL_Data_Extractor" id="termops_extractor" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.migration.TermOps_SEIPL_Data_Reader" id="termops_reader" scope="request"></jsp:useBean>   --%>

<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate = utildate.getSysdate();
String sysDateTime = utildate.getSysdateWithTime24hr();
sysDateTime = sysDateTime.replaceAll("/", "-");
sysDateTime = sysDateTime.replaceAll(":", "_");

String start_end_dt = "";

String checked_values = request.getParameter("checked_values") == null ? "" : request.getParameter("checked_values");
String checked_ids = request.getParameter("checked_ids") == null ? "" : request.getParameter("checked_ids");
String flag = request.getParameter("flag") == null ? "" : request.getParameter("flag");

String delta_FromDt = request.getParameter("FromDt") == null ? "01/01/2007" : request.getParameter("FromDt");
String delta_ToDt = request.getParameter("ToDt") == null ? sysdate : request.getParameter("ToDt");
String csv_date = "";

start_end_dt = delta_FromDt.replaceAll("/", "") + "_" + delta_ToDt.replaceAll("/", "");

String migration_setup_dir = request.getSession().getServletContext().getRealPath("/migration/");

if (flag.equals("1")) {	// Generate Excel
	if (checked_ids.contains("MASTER")) {
		master_extractor.setChecked_Values(checked_values);
		master_extractor.setMigration_Setup_Dir(migration_setup_dir);
		master_extractor.setDelta_FromDt(delta_FromDt);
		master_extractor.setDelta_ToDt(delta_ToDt);
		master_extractor.setSysDateTime(sysDateTime);
		master_extractor.setStart_End_Dt(start_end_dt);
		master_extractor.init();
		
		msg = master_extractor.getMsg();
		msg_type = master_extractor.getMsg_Type();
	}

	/* if (checked_ids.contains("PURCHASE")) {
		purchase_extractor.setChecked_Values(checked_values);
		purchase_extractor.setMigration_Setup_Dir(migration_setup_dir);
		purchase_extractor.setDelta_FromDt(delta_FromDt);
		purchase_extractor.setDelta_ToDt(delta_ToDt);
		purchase_extractor.setSysDateTime(sysDateTime);
		purchase_extractor.setStart_End_Dt(start_end_dt);
		purchase_extractor.init();
		
		msg = purchase_extractor.getMsg();
		msg_type = purchase_extractor.getMsg_Type();
	} 

	if (checked_ids.contains("SALES")) {
		sales_extractor.setChecked_Values(checked_values);
		sales_extractor.setMigration_Setup_Dir(migration_setup_dir);
		sales_extractor.setDelta_FromDt(delta_FromDt);
		sales_extractor.setDelta_ToDt(delta_ToDt);
		sales_extractor.setSysDateTime(sysDateTime);
		sales_extractor.setStart_End_Dt(start_end_dt);
		sales_extractor.init();
		
		msg = sales_extractor.getMsg();
		msg_type = sales_extractor.getMsg_Type();
	} 

	if (checked_ids.contains("TERMINAL OPERATIONS")) {
		termops_extractor.setChecked_Values(checked_values);
		termops_extractor.setMigration_Setup_Dir(migration_setup_dir);
		termops_extractor.setDelta_FromDt(delta_FromDt);
		termops_extractor.setDelta_ToDt(delta_ToDt);
		termops_extractor.setSysDateTime(sysDateTime);
		termops_extractor.setStart_End_Dt(start_end_dt);
		termops_extractor.init();
		
		msg = termops_extractor.getMsg();
		msg_type = termops_extractor.getMsg_Type();
	}  */
}

else if(flag.equals("2")) {	// Read Excel
	if (checked_ids.contains("MASTER")) {
		master_reader.setChecked_Values(checked_values);
		master_reader.setMigration_Setup_Dir(migration_setup_dir);
		master_reader.setSysDateTime(sysDateTime);
		master_reader.setStart_End_Dt(start_end_dt);
		master_reader.init();
		
		msg = master_reader.getMsg();
		msg_type = master_reader.getMsg_Type();
	}

	/* if (checked_ids.contains("PURCHASE")) {
		purchase_reader.setChecked_Values(checked_values);
		purchase_reader.setMigration_Setup_Dir(migration_setup_dir);
		purchase_reader.setSysDateTime(sysDateTime);
		purchase_reader.setStart_End_Dt(start_end_dt);
		purchase_reader.init();
		
		msg = purchase_reader.getMsg();
		msg_type = purchase_reader.getMsg_Type();
	}

	if (checked_ids.contains("SALES")) {
		sales_reader.setChecked_Values(checked_values);
		sales_reader.setMigration_Setup_Dir(migration_setup_dir);
		sales_reader.setSysDateTime(sysDateTime);
		sales_reader.setStart_End_Dt(start_end_dt);
		sales_reader.init();
		
		msg = sales_reader.getMsg();
		msg_type = sales_reader.getMsg_Type();
	}

	if (checked_ids.contains("TERMINAL OPERATIONS")) {
		termops_reader.setChecked_Values(checked_values);
		termops_reader.setMigration_Setup_Dir(migration_setup_dir);
		termops_reader.setSysDateTime(sysDateTime);
		termops_reader.setStart_End_Dt(start_end_dt);
		termops_reader.init();
		
		msg = termops_reader.getMsg();
		msg_type = termops_reader.getMsg_Type();
	} */
}

else if(flag.equals("3")) {	// Rollback
	if (checked_ids.contains("MASTER")) {
		master_rollback.setChecked_Values(checked_values);
		master_rollback.setMigration_Setup_Dir(migration_setup_dir);
		master_rollback.setSysDateTime(sysDateTime);
		master_rollback.setStart_End_Dt(start_end_dt);
		master_rollback.init();
		
		msg = master_rollback.getMsg();
		msg_type = master_rollback.getMsg_Type();
	}
}

String module_names[] = {"MASTER", "PURCHASE", "SALES", "TERMINAL OPERATIONS"}; 

String function_names[] = new String[module_names.length];
function_names[0] = "FMS_COUNTERPARTY_MST, FMS_COUNTERPARTY_ADDR_MST, FMS_ENTITY_REQ_DTL, FMS_SECTOR_MST, FMS_GOVT_STAT_TAX-D, FMS_ENTITY_ADDR_MST, FMS_COMPANY_OWNER_MST, FMS_COMPANY_OWNER_ADDR_MST, FMS_COUNTERPARTY_PLANT_DTL, FMS_COUNTERPARTY_BU_DTL, FMS_INT_RATE_MST-D, FMS_EXCHG_RATE_MST-D, FMS_INT_PAY_RATE_ENTRY, FMS_EXCHG_RATE_ENTRY, FMS_BANK_MST, FMS_ENTITY_TURNOVER_DTL, FMS_SHIP_MST, FMS_ENTITY_CONTACT_MST, FMS_METER_MST, FMS_COUNTERPARTY_PLANT_TAX, FMS_COUNTERPARTY_BU_TAX, FMS_TAX_MST-D, FMS_TAX_STRUCTURE-D, FMS_TAX_STRUCTURE_DTL-D, FMS_ENTITY_TAX_STRUCT_DTL, FMS_ENTITY_SERVICE_TAX_DTL, FMS_ENTITY_BU_SVC_TAX_DTL, FMS_CUSTOM_TAX_STRUCT_DTL, FMS_HOLIDAY_DTL, FMS_PRODUCT_MST, FMS_SAC_MST";
function_names[1] = "FMS_TRADER_AGMT_MST-D, FMS_TRADER_AGMT_BU-D, FMS_TRADER_AGMT_PLANT-D, FMS_TRADER_CN_MST, FMS_TRADER_CARGO_MST, FMS_SECURITY_DEAL_MAP, FMS_SECURITY_MST, LOG_FMS_SECURITY_MST, FMS_CONT_PRICE_DTL, FMS_CONT_PRICE_MIN_DTL, FMS_CARGO_SVC_CONT_MST, FMS_CARGO_SVC_CONT_SVC_BU, FMS_BUY_CARGO_NOM, FMS_BUY_CARGO_NOM_BL, FMS_BUY_CARGO_NOM_BOE, FMS_BUY_CARGO_ALLOC, FMS_BUY_CARGO_ALLOC_BL, FMS_BUY_CARGO_ALLOC_BOE, FMS_TRADER_CONT_MST";
function_names[2] = "FMS_AGMT_MST, FMS_AGMT_TRANSPTR, FMS_AGMT_PLANT";
function_names[3] = "FMS_TANK_MST, FMS_TANK_CONSUMPTION_MST, FMS_TANK_INVENTORY_DTL";

//tables used for each function
String fn_name[][] = new String[function_names.length][];
		
fn_name[0] = new String[function_names[0].split(", ").length];
fn_name[0][0] = "FMS9_COUNTERPARTY_MST, FMS7_CUSTOMER_MST, FMS7_TRADER_MST, FMS7_TRANSPORTER_MST, FMS7_HOUSE_AGENT_MST, FMS7_VESSEL_AGENT_MST, FMS7_SURVEYOR_MST";
fn_name[0][1] = "FMS9_COUNTERPARTY_MST, FMS9_COUNTERPARTY_ADD_MST, FMS7_CUS_HOUSE_AGENT_MST, FMS7_CUS_HOUSE_AGENT_ADDR_MST, FMS7_VESSEL_AGENT_MST, FMS7_VESSEL_AGENT_ADDR_MST, FMS7_SURVEYOR_MST, FMS7_SURVEYOR_ADDRESS_MST";		
fn_name[0][2] = "FMS9_COUNTERPARTY_MST, FMS7_CUSTOMER_MST, FMS7_TRADER_MST, FMS7_TRANSPORTER_MST, FMS7_CUS_HOUSE_AGENT_MST, FMS7_VESSEL_AGENT_MST, FMS7_SURVEYOR_MST";
fn_name[0][3] = "FMS7_SECTOR_MST";
fn_name[0][4] = "FMS7_GOVT_STAT_NO";
fn_name[0][5] = "FMS9_COUNTERPTY_MST, FMS7_CUSTOMER_ADDRESS_MST, FMS7_CUSTOMER_MST, FMS7_TRADER_ADDRESS_MST, FMS7_TRADER_MST, FMS7_TRANSPORTER_ADDRESS_MST, FMS7_TRANSPORTER_MST";
fn_name[0][6] = "FMS7_SUPPLIER_MST";
fn_name[0][7] = "FMS9_COUNTERPTY_MST, FMS7_SUPPLIER_ADDRESS_MST, FMS7_SUPPLIER_MST";
fn_name[0][8] = "FMS9_COUNTERPTY_MST, FMS7_CUSTOMER_PLANT_DTL, FMS7_CUSTOMER_MST, FMS7_SECTOR_MST, FMS7_TRADER_PLANT_DTL, FMS7_TRADER_MST, FMS7_TRANSPORTER_PLANT_DTL, FMS7_TRANSPORTER_MST, FMS7_SUPPLIER_ADDRESS_MST, FMS7_SUPPLIER_MST";
fn_name[0][9] = "FMS7_SURVEYOR_ADDRESS_MST, FMS7_SURVEYOR_MST, FMS7_VESSEL_AGENT_ADDR_MST, FMS7_VESSEL_AGENT_MST, FMS7_CUS_HOUSE_AGENT_ADDR_MST, FMS7_CUS_HOUSE_AGENT_MST";
fn_name[0][10] = "FMS7_CONT_INT_RATE_MST";
fn_name[0][11] = "FMS7_CONT_EXCHG_RATE_MST";
fn_name[0][12] = "FMS7_INT_PAY_RATE_ENTRY, FMS7_CONT_INT_RATE_MST";
fn_name[0][13] = "FMS7_EXCHG_RATE_ENTRY, FMS7_CONT_EXCHG_RATE_MST";
fn_name[0][14] = "FMS7_BANK_MST";
fn_name[0][15] = "FMS9_COUNTERPTY_MST, FMS7_CUSTOMER_TURNOVER_DTL, FMS7_CUSTOMER_MST, FMS7_TRADER_TURNOVER_DTL, FMS7_TRADER_MST, FMS7_SUPPLIER_TURNOVER_DTL, FMS7_SUPPLIER_MST";
fn_name[0][16] = "FMS7_SHIP_MST";
fn_name[0][17] = "FMS9_COUNTERPTY_MST, FMS7_CUSTOMER_CONTACT_MST, FMS7_CUSTOMER_MST, FMS7_TRADER_CONTACT_MST, FMS7_TRADER_MST, FMS7_TRANSPORTER_CONTACT_MST, FMS7_TRANSPORTER_MST, FMS7_CUS_HUS_AGT_CONTACT_MST, FMS7_CUS_HOUSE_AGENT_MST, FMS7_VESSEL_AGENT_CONTACT_MST, FMS7_VESSEL_AGENT_MST, FMS7_SURVEYOR_CONTACT_MST, FMS7_SURVEYOR_MST, FMS7_SUPPLIER_CONTACT_MST, FMS7_SUPPLIER_MST";
fn_name[0][18] = "FMS7_METER_MST, FMS7_TRANSPORTER_MST, FMS7_METER_DTL";
fn_name[0][19] = "FMS7_TRADER_PLANT_DTL, FMS7_TRADER_MST, FMS9_COUNTERPTY_MST, FMS7_CUSTOMER_PLANT_TAX_CDS, FMS7_CUSTOMER_MST, FMS7_GOVT_STAT_NO, FMS7_TRADER_PLANT_TAX_CDS, FMS7_TRADER_MST, FMS7_SUPPLIER_MST, FMS7_SUPPLIER_PLANT_TAX_CDS, FMS7_SUPPLIER_MST";
fn_name[0][20] = "FMS7_SURVEYOR_MST, FMS7_GOVT_STAT_NO, FMS7_VESSEL_AGENT_MST, FMS7_CUS_HOUSE_AGENT_MST";
fn_name[0][21] = "FMS7_TAX_MST";
fn_name[0][22] = "FMS7_TAX_STRUCTURE";
fn_name[0][23] = "FMS7_TAX_STRUCTURE_DTL, FMS7_TAX_MST";
fn_name[0][24] = "FMS7_TRADER_PLANT_DTL, FMS7_TRADER_MST, FMS7_CUSTOMER_TAX_STRUCT_DTL, FMS7_TRADER_TAX_STRUCT_DTL";
fn_name[0][25] = "FMS7_CUSTOMER_SERVICE_TAX_DTL, FMS7_TAX_DTL_INVOICEWISE, FMS7_TRADER_SERVICE_TAX_DTL";
fn_name[0][26] = "FMS7_CARGO_ENTITY_TAX_DTL, FMS7_CUS_HOUSE_AGENT_MST, FMS7_SURVEYOR_MST, FMS7_VESSEL_AGENT_MST";
fn_name[0][27] = "FMS7_CARGO_TAX_MST, FMS7_TAX_STRUCTURE";
fn_name[0][28] = "FMS9_HOLIDAY_DTL";
fn_name[0][29] = "FMS7_PRODUCT_MST";
fn_name[0][30] = "FMS8_SERVICE_MST";

fn_name[1] = new String[function_names[1].split(", ").length];
fn_name[1][0] = "FMS9_COUNTERPTY_MST, FMS7_MAN_REQ_MST, FMS7_TRADER_MST, FMS7_MAN_REQ_CARGO_DTL, FMS7_CARGO_NOMINATION";
fn_name[1][1] = "FMS9_COUNTERPTY_MST, FMS7_MAN_REQ_MST, FMS7_TRADER_MST";
fn_name[1][2] = "FMS9_COUNTERPTY_MST, FMS7_MAN_REQ_MST, FMS7_TRADER_MST";
fn_name[1][3] = "FMS9_COUNTERPTY_MST, FMS7_MAN_REQ_MST, FMS7_TRADER_MST, FMS7_MAN_REQ_CARGO_DTL, FMS7_MAN_CONFIRM_MST, FMS7_MAN_CONFIRM_CARGO_DTL, FMS7_CARGO_NOMINATION";
fn_name[1][4] = "FMS9_COUNTERPTY_MST, FMS7_MAN_REQ_MST, FMS7_TRADER_MST, FMS7_MAN_CONFIRM_CARGO_DTL";
fn_name[1][5] = "FMS9_COUNTERPTY_MST, FMS7_MAN_REQ_MST, FMS7_TRADER_MST, FMS7_MAN_CONFIRM_CARGO_DTL, FMS9_SECURITY_POST_MST";
fn_name[1][6] = "FMS9_COUNTERPTY_MST, FMS7_MAN_REQ_MST, FMS7_TRADER_MST, FMS7_MAN_CONFIRM_CARGO_DTL, FMS9_SECURITY_POST_MST, FMS7_BANK_MST";
fn_name[1][7] = "FMS9_COUNTERPTY_MST, FMS7_MAN_REQ_MST, FMS7_TRADER_MST, FMS7_MAN_CONFIRM_CARGO_DTL, FMS9_SECURITY_POST_MST, FMS7_BANK_MST";
fn_name[1][8] = "FMS9_COUNTERPTY_MST, FMS7_MAN_REQ_MST, FMS7_TRADER_MST, FMS7_MAN_CONFIRM_CARGO_DTL, FMS9_MRCR_CONT_PRICE_DTL";
fn_name[1][9] = "FMS9_COUNTERPTY_MST, FMS7_MAN_REQ_MST, FMS7_TRADER_MST, FMS7_MAN_CONFIRM_CARGO_DTL, FMS9_MRCR_CONT_FIN_PRICE_DTL";
fn_name[1][10] = "FMS7_SURVEYOR_MST, FMS7_CARGO_ENTITY_SERV_CHARGES, FMS7_VESSEL_AGENT_MST, FMS7_CUS_HOUSE_AGENT_MST";
fn_name[1][11] = "FMS7_SURVEYOR_MST, FMS7_CARGO_ENTITY_SERV_CHARGES, FMS7_VESSEL_AGENT_MST, FMS7_CUS_HOUSE_AGENT_MST";
fn_name[1][12] = "FMS9_COUNTERPARTY_MST, FMS7_TRADER_MST, FMS7_CARGO_NOMINATION, FMS7_SHIP_MST, FMS7_CARGO_BL_DTL, FMS7_CARGO_ARRIVAL_DTL, FMS7_SURVEYOR_MST, FMS7_CUS_HOUSE_AGENT_MST, FMS7_VESSEL_AGENT_MST";
fn_name[1][13] = "FMS9_COUNTERPARTY_MST, FMS7_TRADER_MST, FMS7_CARGO_NOMINATION, FMS7_CARGO_BL_DTL";
fn_name[1][14] = "FMS9_COUNTERPARTY_MST, FMS7_TRADER_MST, FMS7_CARGO_NOMINATION, FMS7_CARGO_ARRIVAL_DTL";
fn_name[1][15] = "FMS7_CARGO_ARRIVAL_DTL, FMS7_CARGO_QQ_DTL";
fn_name[1][16] = "FMS9_COUNTERPTY_MST, FMS7_CARGO_NOMINATION, FMS7_TRADER_MST, FMS7_CARGO_BL_DTL";
fn_name[1][17] = "FMS9_COUNTERPTY_MST, FMS7_CARGO_NOMINATION, FMS7_TRADER_MST, FMS7_CARGO_ARRIVAL_DTL";
fn_name[1][18] = "FMS7_TRADER_CONT_MST, FMS7_TRADER_CLAUSE_MST"; 

fn_name[2] = new String[function_names[2].split(", ").length];
fn_name[2][0] = "FMS9_COUNTERPTY_MST, FMS7_FGSA_MST, FMS7_CUSTOMER_MST, FMS7_FGSA_CLAUSE_MST, FMS7_CLAUSE_MST";
fn_name[2][1] = "FMS9_COUNTERPTY_MST, FMS7_FGSA_TRANSPORTER_MST, FMS7_CUSTOMER_MST, FMS7_TRANSPORTER_MST, FMS7_TRANSPORTER_PLANT_DTL";
fn_name[2][2] = "FMS9_COUNTERPTY_MST, FMS7_FGSA_PLANT_MST, FMS7_CUSTOMER_MST";

fn_name[3] = new String[function_names[3].split(", ").length];
fn_name[3][0] = "FMS7_TANK_MASTER_DTL";
fn_name[3][1] = "FMS7_CONSUMPTION_MST";
fn_name[3][2] = "FMS7_INVENTORY_LEVEL_DTL";


String fname = "";                                     
String fromTo = "";  		 	
String total_record = "",execution="",completion="";
String status = "";

String total_count_e[][] = new String[fn_name.length][];
String total_count_r[][] = new String[fn_name.length][];
String total_count_d[][] = new String[fn_name.length][];

for(int i = 0; i<fn_name.length;i++){
	total_count_e[i] = new String[fn_name[i].length];
	total_count_r[i] = new String[fn_name[i].length];
	total_count_d[i] = new String[fn_name[i].length];
}
	
Dictionary<String, String> dir = new Hashtable<>();
String csvFilePath = application.getRealPath("/") + "migration/DataLogs/Script_Status(log).csv";
File csvFile = new File(csvFilePath);

if(csvFile.exists()){
BufferedReader br = null;
String line = "";	
    br = new BufferedReader(new FileReader(csvFilePath));
    List<String[]> rows = new ArrayList<String[]>();

    while ((line = br.readLine()) != null) {      
        String[] data = line.split(",");
        rows.add(data);
    }								
    for (String[] row : rows) {																        		
    		 if (row.length == 5){
    			fname = row[0];   			    	
     		 	fromTo = row[1];
     		 	execution = row[2];
     		 	total_record = row[3];
     		 	completion = row[4];           
     		 	} 
    		 
    		 if (fromTo.equals(start_end_dt)) 
    		 {
    		 	dir.put(fname,total_record);
    		 }            
    }
    if (br != null) {     
        br.close();
	}
       
    for(int j = 0; j < module_names.length;j++){
    	for(int i = 0; i < function_names[j].split(", ").length; i++){ 
    		total_count_e[j][i] = dir.get(function_names[j].split(", ")[i]+"()E") == null ? "" : dir.get(function_names[j].split(", ")[i]+"()E");
    		total_count_r[j][i] = dir.get(function_names[j].split(", ")[i]+"()R") == null ? "" : dir.get(function_names[j].split(", ")[i]+"()R");
    		total_count_d[j][i] = dir.get(function_names[j].split(", ")[i]+"()D") == null ? "" : dir.get(function_names[j].split(", ")[i]+"()D");
    	}
    		
    } 
    
	} else  {
		for(int j = 0; j < module_names.length;j++){
	    	for(int i = 0; i < function_names[j].split(", ").length; i++){ 
	    		total_count_e[j][i] = dir.get(function_names[j].split(", ")[i]+"()E") == null ? "" : dir.get(function_names[j].split(", ")[i]+"()E");
	    		total_count_r[j][i] = dir.get(function_names[j].split(", ")[i]+"()R") == null ? "" : dir.get(function_names[j].split(", ")[i]+"()R");
	    		total_count_d[j][i] = dir.get(function_names[j].split(", ")[i]+"()D") == null ? "" : dir.get(function_names[j].split(", ")[i]+"()D");
	    	}
	    		
	    } 
	}     
    
%>
<body>
<%@ include file="../home/header.jsp"%>
<form>
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<%if(!msg.equals("")){
				if(msg_type.equals("S")){%>
					<div class="fadealert"><%=utilmsg.successMessage(msg)%></div>
				<%}else if(msg_type.equals("E")){%>
					<div class="fadealert"><%= utilmsg.errorMessage(msg)%></div>
				<%}
			} %>
			<div class="card cardmain">
				<div class="card-header cdheader">
					<div class="topheader">
				    	Data Migrator
				    </div>
				</div>
				<div class="card-body cdbody">		
						
					<div class="row">
						<!-- <div class="col-sm-4 col-xs-4 col-md-4"> --> 	
						<div class="col-sm-12 col-xs-12 col-md-12">
				            			
							<div class="form-group row">
								<div class="form-group row justify-content-center">	
									<div class="col-auto">
										<label class="form-label"><b>From</b></label>
									</div>
									<div class="col-auto">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" id="from_dt" value="<%=delta_FromDt%>" size="10" maxLength="10" 
				      						 onchange="validateDate(this);refresh();">
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
									<div class="col-auto">
										<label class="form-label"><b>To</b></label>
									</div>
									<div class="col-auto">
					      				<div class="input-group input-group-sm" >
				      						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" id="to_dt" value="<%=delta_ToDt%>" size="10" maxLength="10" 
				      						 onchange="validateDate(this);refresh();">
				      						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
			      						</div>
					    			</div>
								</div>
							</div>
				        </div>
						<!-- </div> -->
					</div>
				</div>   
				
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">  
						<!--  -->
						
						<% 					
						for (int j = 0; j < module_names.length-3; j++) { %>
								<!-- &nbsp;
								<div class="row m-b-5">
									<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Master Extractor </label>
								</div> -->
								
								<div class="row">
									<div class="col-md-12 col-sm-12 col-xs-12">
									
											<div class="accordion">
												<div class="accordion-item accor_item">
													<h2 class="accordion-header" id="heading<%= j %>">
			    												<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%= j %>" aria-expanded="false" aria-controls="collapse<%= j %>">
											    		<%= module_names[j] %>
											      		</button>
											    	</h2>
											    	<div id="collapse<%= j %>" class="accordion-collapse collapse" aria-labelledby="heading<%= j %>">
											      		<div class="accordion-body accor-body">
											        		<div class="row">
																<div class="table-responsive">
																	<table class="table table-bordered table-hover">
																		<thead>
																			<tr>
																				<th> Check All <br> <input type="checkbox" class="form-check-input" onclick="checkVertical('<%=module_names[j] %>');" name="checkAll" id="checkAll-<%=module_names[j] %>"></th>
																				<th>Sr. No.</th>
																				<th>Extracted Data</th>
																				<th>Inserted Data</th>
																				<th>Deleted Data</th> 
																				<th>EMS Table Name</th>
																				<th>FMS Table Name</th>
																				
																			</tr>
																		</thead>
																		<tbody>																																				
																			<% 																			
																			for(int i=0; i<function_names[j].split(", ").length; i++)
																			{ %>
																			<% 
																			String value = "";
																			if (function_names[j].split(", ")[i].contains("-D")) {
																				value = function_names[j].split(", ")[i].substring(0, function_names[j].split(", ")[i].length()-2);
																			}
																			else {
																				value = function_names[j].split(", ")[i];
																			}
																			
																			
																				
																			
																			%>
																					<tr>
																						<td align="center">
																							<!-- <font title="Click to Edit" style="color:var(--header_color)">
																								<i class="fa fa-edit fa-lg" 
																								data-bs-toggle="modal" data-bs-target="#myModal" 
																								></i>
																							</font> -->
																							<input type="checkbox" class="form-check-input" name="chk" id="<%= module_names[j] %>-<%= (j+1) %>-<%= (i+1) %>" value = "<%= value %>" <% if (function_names[j].split(", ")[i].contains("-D")) { %> disabled <% } %> >
																						</td>
																						<td align="center"> <%= i+1 %> </td>
																						<td align="right"><%= total_count_e[j][i]%></td>
																						<td align="right"><%=total_count_r[j][i]%></td>
																						<td align="right"><%=total_count_d[j][i]%></td> 
																						<td align="left"><%= value%></td>
																						<td align="left"><%= fn_name[j][i]%></td>
																						
																					</tr>
																			<%
																			}
																			%>
																		</tbody>
																	</table>
																</div>
															</div>
														</div>
													</div>
												</div>
											</div>
									</div>
								</div>
							<% } %>
							<!--  -->
						</div>
					</div>
				</div>
				
				  <div class="card-body cdbody">  
				    <div class="row">
				        <div class="col-sm-12 col-xs-12 col-md-12">
				            <div class="form-group row justify-content-center">
				                <div class="col-auto">
				                    <input type="button" class="btn btn-sm request_btn" value="Generate Excel" onclick="generateExcel(); checkboxEnable();">
				                </div>
				                <%if(write_access.equals("Y")){ %>
				                <div class="col-auto">
				                    <input type="button" class="btn btn-sm request_btn" value="Read Excel" onclick="readExcel();">
				                </div>
				                <div class="col-auto">
				                    <input type="button" class="btn btn-sm request_btn" value="RollBack" onclick="rollback();">
				                </div>
				                <% } else { %>
				                <div class="col-auto">
				                    <input type="button" class="btn btn-sm request_btn" value="Read Excel" disabled>
				                </div>
				                <div class="col-auto">
				                    <input type="button" class="btn btn-sm request_btn" value="RollBack" disabled>
				                </div>
				                <% } %>
				            </div>
				        </div>
				    </div>
				</div> 
				<%-- <div class="card-body cdbody">  
					<div class="row">
									<div class="col-md-12 col-sm-12 col-xs-12">
									
											<div class="accordion">
												<div class="accordion-item accor_item">
													<h2 class="accordion-header" id="heading">
			    												<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse" aria-expanded="false" aria-controls="collapse">
											    			MIGRATION REPORT
											      		</button>
											    	</h2>
											    	<div id="collapse" class="accordion-collapse collapse" aria-labelledby="heading">
											      		<div class="accordion-body accor-body">
											        		<div class="row">
																<div class="table-responsive">
																	<table class="table table-bordered table-hover">
																		<thead>
																			<tr>																			
																				<th>Function Name</th>
																				<th>Status</th>
																				<th>From-To</th>
																				<th>Execution</th>
																				<th>Total Records</th>
																				<th>Completion</th>
																				
																			</tr>
																		</thead>
																		<tbody>
																			<%
																			    String csvFilePath = application.getRealPath("/") + "migration/DataLogs/Script_Status(log).csv";
																			    BufferedReader br = null;
																			    String line = "";									    
																			
																			    try {
																			        br = new BufferedReader(new FileReader(csvFilePath));
																			        List<String[]> rows = new ArrayList<String[]>();
																			
																			        while ((line = br.readLine()) != null) {
																			           
																			            String[] data = line.split(",");
																			            rows.add(data);
																			        }								
																			        for (String[] row : rows) {																        		
																			        		 if (row.length == 6){
																			        		  	String fname = row[0];
																			        		 	String status = row[1];
																			        		 	String fromTo = row[2];
																			        		 	String execution = row[3];
																			        		 	String total_record = row[4];
																			        		 	String completion = row[5];
																			        		 	if ("E".equals(status)){
																			        		 		status = "Data Extracted";
																			        		 	}else if ("R".equals(status)){
																			        		 		status = "Data Inserted";
																			        		 	}else {
																			        		 		status = "Data Deleted";
 																			        		 	}
																			        		 	csv_date = fromTo;																        		                                        														
														                                       %>
														                                                            <tr>
														                                                                <td align="left"><%= fname %></td>
														                                                                <td align="left"><%= status %></td>
														                                                                <td align="left"><%= fromTo %></td>
														                                                                <td align="left"><%= execution %></td>
														                                                                <td align="right"><%= total_record %></td>
														                                                               	<td align="left"><%= completion %></td>
														                                                            </tr>
														                                        <%
														                                          } 														                                 
																			   			 }	
																			    }
																			     catch (IOException e) {
																			        e.printStackTrace();
																			        out.println("Error reading file: " + e.getMessage());
																			    } finally {
																			        if (br != null) {
																			            try {
																			                br.close();
																			            } catch (IOException e) {
																			                e.printStackTrace();
																			            }
																			        }
																			    }
																			%>
																		</tbody>
																	</table>
																</div>
															</div>
														</div>
													</div>
												</div>
											</div>
									</div>
								</div>			 
				    
				</div>   --%> 
			</div>
		</div>
	</div>
	
</div>
<input type="hidden" name="read_access" value="<%=read_access%>">
<input type="hidden" name="write_access" value="<%=write_access%>">
<input type="hidden" name="check_access" value="<%=check_access%>">
<input type="hidden" name="print_access" value="<%=print_access%>">
<input type="hidden" name="delete_access" value="<%=delete_access%>">  	
<input type="hidden" name="audit_access" value="<%=audit_access%>">
<input type="hidden" name="authorize_access" value="<%=authorize_access%>">
<input type="hidden" name="approve_access" value="<%=approve_access%>">
<input type="hidden" name="execute_access" value="<%=execute_access%>">
<input type="hidden" name="form_cd" value="<%=formCd%>">
<input type="hidden" name="form_nm" value="<%=formNm%>">
<input type="hidden" name="mod_cd" value="<%=mod_cd%>">
<input type="hidden" name="mod_nm" value="<%=mod_nm%>">
<input type="hidden" name="u" value="<%=u%>">
</form>
</body>

</html>