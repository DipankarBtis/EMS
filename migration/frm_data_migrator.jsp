<%@page import="java.util.prefs.Preferences"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*, java.util.*, javax.servlet.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head> 
<%@ include file="../util/common_js.jsp"%>

<style>


font {  

	font-size: 8px;

}

</style>

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

function rollbackAll() {
	
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
			var url = "frm_data_migrator.jsp?checked_values="+checked_values+"&checked_ids="+checked_ids+"&flag=4&FromDt="+from_dt+"&ToDt="+to_dt+"&u="+u;

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

<jsp:useBean class="com.etrm.fms.migration.Migration_Data_Rollback" id="rollback" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.migration.Migration_Plants_Exceptions" id="mpe" scope="request"></jsp:useBean>

<jsp:useBean class="com.etrm.fms.migration.Master_SEIPL_Data_Extractor" id="master_extractor" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.migration.Master_SEIPL_Data_Reader" id="master_reader" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.migration.Master_SEIPL_Data_RollBack" id="master_rollback" scope="request"></jsp:useBean>

<jsp:useBean class="com.etrm.fms.migration.Purchase_SEIPL_Data_Extractor" id="purchase_extractor" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.migration.Purchase_SEIPL_Data_Reader" id="purchase_reader" scope="request"></jsp:useBean> 
<jsp:useBean class="com.etrm.fms.migration.Purchase_SEIPL_Data_RollBack" id="purchase_rollback" scope="request"></jsp:useBean>

<jsp:useBean class="com.etrm.fms.migration.Sales_SEIPL_Data_Extractor" id="sales_extractor" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.migration.Sales_SEIPL_Data_Reader" id="sales_reader" scope="request"></jsp:useBean>  
<jsp:useBean class="com.etrm.fms.migration.Sales_SEIPL_Data_RollBack" id="sales_rollback" scope="request"></jsp:useBean>

<jsp:useBean class="com.etrm.fms.migration.Transport_SEIPL_Data_Extractor" id="transport_extractor" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.migration.Transport_SEIPL_Data_Reader" id="transport_reader" scope="request"></jsp:useBean>  
<%-- <jsp:useBean class="com.etrm.fms.migration.Transport_SEIPL_Data_RollBack" id="transport_rollback" scope="request"></jsp:useBean> --%>

<jsp:useBean class="com.etrm.fms.migration.TermOps_SEIPL_Data_Extractor" id="termops_extractor" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.migration.TermOps_SEIPL_Data_Reader" id="termops_reader" scope="request"></jsp:useBean>  
<jsp:useBean class="com.etrm.fms.migration.TermOps_SEIPL_Data_Rollback" id="termops_rollback" scope="request"></jsp:useBean>

<jsp:useBean class="com.etrm.fms.migration.RiskMgmt_SEIPL_Data_Extractor" id="riskmgmt_extractor" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.migration.RiskMgmt_SEIPL_Data_Reader" id="riskmgmt_reader" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.migration.RiskMgmt_SEIPL_Data_RollBack" id="riskmgmt_rollback" scope="request"></jsp:useBean>

<jsp:useBean class="com.etrm.fms.migration.DLNG_SEIPL_Data_Extractor" id="dlng_extractor" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.migration.DLNG_SEIPL_Data_Reader" id="dlng_reader" scope="request"></jsp:useBean>

<jsp:useBean class="com.etrm.fms.migration.Derivative_SEIPL_Data_Extractor" id="derivative_extractor" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.migration.Derivative_SEIPL_Data_Reader" id="derivative_reader" scope="request"></jsp:useBean> 

<jsp:useBean class="com.etrm.fms.migration.Interface_SEIPL_Data_Extractor" id="interface_extractor" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.migration.Interface_SEIPL_Data_Reader" id="interface_reader" scope="request"></jsp:useBean>


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

String delta_FromDt = request.getParameter("FromDt") == null ? "01/01/2000" : request.getParameter("FromDt");
String delta_ToDt = request.getParameter("ToDt") == null ? sysdate : request.getParameter("ToDt");
String csv_date = "";

start_end_dt = delta_FromDt.replaceAll("/", "") + "_" + delta_ToDt.replaceAll("/", "");

String migration_setup_dir = request.getSession().getServletContext().getRealPath("/migration/");
String pdf_path = request.getSession().getServletContext().getRealPath("/work_data2/signed_pdf/");

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

	 if (checked_ids.contains("PURCHASE")) {
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
	
	 if (checked_ids.contains("TRANSPORT")) {
		 transport_extractor.setChecked_Values(checked_values);
		 transport_extractor.setMigration_Setup_Dir(migration_setup_dir);
		 transport_extractor.setDelta_FromDt(delta_FromDt);
		 transport_extractor.setDelta_ToDt(delta_ToDt);
		 transport_extractor.setSysDateTime(sysDateTime);
		transport_extractor.setStart_End_Dt(start_end_dt);
		transport_extractor.init();
		
		msg = transport_extractor.getMsg();
		msg_type = transport_extractor.getMsg_Type();
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
	}

	if (checked_ids.contains("RISK MGMT")) {
		riskmgmt_extractor.setChecked_Values(checked_values);
		riskmgmt_extractor.setMigration_Setup_Dir(migration_setup_dir);
		riskmgmt_extractor.setDelta_FromDt(delta_FromDt);
		riskmgmt_extractor.setDelta_ToDt(delta_ToDt);
		sales_reader.setPdf_path(pdf_path);
		riskmgmt_extractor.setSysDateTime(sysDateTime);
		riskmgmt_extractor.setStart_End_Dt(start_end_dt);
		riskmgmt_extractor.init();
		
		msg = riskmgmt_extractor.getMsg();
		msg_type = riskmgmt_extractor.getMsg_Type();
	}
		
	
	if (checked_ids.contains("INTERFACE")) {
		interface_extractor.setChecked_Values(checked_values);
		interface_extractor.setMigration_Setup_Dir(migration_setup_dir);
		interface_extractor.setDelta_FromDt(delta_FromDt);
		interface_extractor.setDelta_ToDt(delta_ToDt);
		interface_extractor.setSysDateTime(sysDateTime);
		interface_extractor.setStart_End_Dt(start_end_dt);
		interface_extractor.init();
		
		msg = interface_extractor.getMsg();
		msg_type = interface_extractor.getMsg_Type();
	}

	 if (checked_ids.contains("DLNG")) {
		dlng_extractor.setChecked_Values(checked_values);
		dlng_extractor.setMigration_Setup_Dir(migration_setup_dir);
		dlng_extractor.setDelta_FromDt(delta_FromDt);
		dlng_extractor.setDelta_ToDt(delta_ToDt);
		dlng_extractor.setSysDateTime(sysDateTime);
		dlng_extractor.setStart_End_Dt(start_end_dt);
		dlng_extractor.init();
		
		msg = dlng_extractor.getMsg();
		msg_type = dlng_extractor.getMsg_Type();
	} 
	 
	 if (checked_ids.contains("DERIVATIVES")) {
		 derivative_extractor.setChecked_Values(checked_values);
		 derivative_extractor.setMigration_Setup_Dir(migration_setup_dir);
		 derivative_extractor.setDelta_FromDt(delta_FromDt);
		 derivative_extractor.setDelta_ToDt(delta_ToDt);
		 derivative_extractor.setSysDateTime(sysDateTime);
		 derivative_extractor.setStart_End_Dt(start_end_dt);
		 derivative_extractor.init();
			
			msg = derivative_extractor.getMsg();
			msg_type = derivative_extractor.getMsg_Type();
	}
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

	 if (checked_ids.contains("PURCHASE")) {
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
		sales_reader.setPdf_path(pdf_path);
		sales_reader.setSysDateTime(sysDateTime);
		sales_reader.setStart_End_Dt(start_end_dt);
		sales_reader.init();
		
		msg = sales_reader.getMsg();
		msg_type = sales_reader.getMsg_Type();
	} 

	if (checked_ids.contains("TRANSPORT")) {
		transport_reader.setChecked_Values(checked_values);
		transport_reader.setMigration_Setup_Dir(migration_setup_dir);
		transport_reader.setSysDateTime(sysDateTime);
		transport_reader.setStart_End_Dt(start_end_dt);
		transport_reader.init();
		
		msg = transport_reader.getMsg();
		msg_type = transport_reader.getMsg_Type();
	} 
	
	if (checked_ids.contains("TERMINAL OPERATIONS")) {
		termops_reader.setChecked_Values(checked_values);
		termops_reader.setMigration_Setup_Dir(migration_setup_dir);
		termops_reader.setSysDateTime(sysDateTime);
		termops_reader.setStart_End_Dt(start_end_dt);
		termops_reader.init();
		
		msg = termops_reader.getMsg();
		msg_type = termops_reader.getMsg_Type();
	} 

	if (checked_ids.contains("RISK MGMT")) {
		riskmgmt_reader.setChecked_Values(checked_values);
		riskmgmt_reader.setMigration_Setup_Dir(migration_setup_dir);
		riskmgmt_reader.setSysDateTime(sysDateTime);
		riskmgmt_reader.setStart_End_Dt(start_end_dt);
		riskmgmt_reader.setDelta_FromDt(delta_FromDt);
		riskmgmt_reader.setDelta_ToDt(delta_ToDt);
		riskmgmt_reader.init();
		
		msg = riskmgmt_reader.getMsg();
		msg_type = riskmgmt_reader.getMsg_Type();
	} 
	
	if (checked_ids.contains("INTERFACE")) {
		interface_reader.setChecked_Values(checked_values);
		interface_reader.setMigration_Setup_Dir(migration_setup_dir);
		interface_reader.setSysDateTime(sysDateTime);
		interface_reader.setStart_End_Dt(start_end_dt);
		interface_reader.init();
		
		msg = interface_reader.getMsg();
		msg_type = interface_reader.getMsg_Type();
	} 

	if (checked_ids.contains("DLNG")) {
		dlng_reader.setChecked_Values(checked_values);
		dlng_reader.setMigration_Setup_Dir(migration_setup_dir);
		dlng_reader.setPdf_path(pdf_path);
		dlng_reader.setSysDateTime(sysDateTime);
		dlng_reader.setStart_End_Dt(start_end_dt);
		dlng_reader.init();
		
		msg = dlng_reader.getMsg();
		msg_type = dlng_reader.getMsg_Type();
	}
 	if (checked_ids.contains("DERIVATIVES")) {
		 derivative_reader.setChecked_Values(checked_values);
		 derivative_reader.setMigration_Setup_Dir(migration_setup_dir);
		 derivative_reader.setSysDateTime(sysDateTime);
		 derivative_reader.setStart_End_Dt(start_end_dt);
		 derivative_reader.setPdf_path(pdf_path);
		 derivative_reader.init();
			
			msg = derivative_reader.getMsg();
			msg_type = derivative_reader.getMsg_Type();
	} 
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

	if (checked_ids.contains("PURCHASE")) {
		purchase_rollback.setChecked_Values(checked_values);
		purchase_rollback.setMigration_Setup_Dir(migration_setup_dir);
		purchase_rollback.setSysDateTime(sysDateTime);
		purchase_rollback.setStart_End_Dt(start_end_dt);
		purchase_rollback.init();
		
		msg = purchase_rollback.getMsg();
		msg_type = purchase_rollback.getMsg_Type();
	}
	
	if (checked_ids.contains("SALES")) {
		sales_rollback.setChecked_Values(checked_values);
		sales_rollback.setMigration_Setup_Dir(migration_setup_dir);
		sales_rollback.setSysDateTime(sysDateTime);
		sales_rollback.setStart_End_Dt(start_end_dt);
		sales_rollback.init();
		
		msg = sales_rollback.getMsg();
		msg_type = sales_rollback.getMsg_Type();
	}
		
	if (checked_ids.contains("TERMINAL OPERATIONS")) {
		termops_rollback.setChecked_Values(checked_values);
		termops_rollback.setMigration_Setup_Dir(migration_setup_dir);
		termops_rollback.setSysDateTime(sysDateTime);
		termops_rollback.setStart_End_Dt(start_end_dt);
		termops_rollback.init();
		
		msg = termops_rollback.getMsg();
		msg_type = termops_rollback.getMsg_Type();
	}   
	
	if (checked_ids.contains("RISK MGMT")) {
		riskmgmt_rollback.setChecked_Values(checked_values);
		riskmgmt_rollback.setMigration_Setup_Dir(migration_setup_dir);
		riskmgmt_rollback.setSysDateTime(sysDateTime);
		riskmgmt_rollback.setStart_End_Dt(start_end_dt);
		riskmgmt_rollback.init();
		
		msg = riskmgmt_rollback.getMsg();
		msg_type = riskmgmt_rollback.getMsg_Type();
	} 
}

else if(flag.equals("4")) {	// Rollback All
	
	rollback.setChecked_Values(checked_values);
	rollback.setChecked_Ids(checked_ids);
	rollback.setMigration_Setup_Dir(migration_setup_dir);
	rollback.setSysDateTime(sysDateTime);
	rollback.setStart_End_Dt(start_end_dt);
	rollback.init();
	
	msg = rollback.getMsg();
	msg_type = rollback.getMsg_Type();
	
}


String fname = "";                                     
String fromTo = "";  		 	
String total_record = "",execution="",completion="";
String status = "";
String lastop = "";
String lastchar = "";

String total_count_e[][] = new String[mpe.function_names.length][];
String total_count_e_dt[][] = new String[mpe.function_names.length][];
String total_count_r[][] = new String[mpe.function_names.length][];
String total_count_r_dt[][] = new String[mpe.function_names.length][];
String total_count_d[][] = new String[mpe.function_names.length][];
String total_count_d_dt[][] = new String[mpe.function_names.length][];

int [][] flags = new int [mpe.function_names.length][];	


for(int i = 0; i < mpe.function_names.length;i++){
	total_count_e[i] = new String[mpe.function_names[i].split(", ").length];
	total_count_e_dt[i] = new String[mpe.function_names[i].split(", ").length];
	total_count_r[i] = new String[mpe.function_names[i].split(", ").length];
	total_count_r_dt[i] = new String[mpe.function_names[i].split(", ").length];
	total_count_d[i] = new String[mpe.function_names[i].split(", ").length];
	total_count_d_dt[i] = new String[mpe.function_names[i].split(", ").length];
	
	flags[i] = new int[mpe.function_names[i].split(", ").length];
}
	
Dictionary<String, String> dir = new Hashtable<>();
Dictionary<String, String> DT = new Hashtable<>();
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
     		 	lastop = "" + fname.substring(0,fname.length()-3);
     		 	lastchar = ""  + fname.charAt(fname.length()-1);
     		 	
     		 	}
    		 
    		 if (fromTo.equals(start_end_dt)) 
    		 {
    		 	dir.put(fname,total_record);
    		 	DT.put(fname,execution);
    		 }            
    }
    
    if (br != null) {     
        br.close();
	}
       
    for(int j = 0; j < mpe.module_names.length;j++){
    	for(int i = 0; i < mpe.function_names[j].split(", ").length; i++){ 
    		total_count_e[j][i] = dir.get(mpe.function_names[j].split(", ")[i]+"()E") == null ? "" : dir.get(mpe.function_names[j].split(", ")[i]+"()E");
    		total_count_e_dt[j][i] = DT.get(mpe.function_names[j].split(", ")[i]+"()E") == null ? "" : DT.get(mpe.function_names[j].split(", ")[i]+"()E");
    		total_count_r[j][i] = dir.get(mpe.function_names[j].split(", ")[i]+"()R") == null ? "" : dir.get(mpe.function_names[j].split(", ")[i]+"()R");
    		total_count_r_dt[j][i] = DT.get(mpe.function_names[j].split(", ")[i]+"()R") == null ? "" : DT.get(mpe.function_names[j].split(", ")[i]+"()R");
    		total_count_d[j][i] = dir.get(mpe.function_names[j].split(", ")[i]+"()D") == null ? "" : dir.get(mpe.function_names[j].split(", ")[i]+"()D");
    		total_count_d_dt[j][i] = DT.get(mpe.function_names[j].split(", ")[i]+"()D") == null ? "" : DT.get(mpe.function_names[j].split(", ")[i]+"()D");
    		if(lastchar.contains("R")){
    			if(lastop.contains(mpe.function_names[j].split(", ")[i])){
					flags[j][i] = 0;
    			}
			}
			else if(lastchar.contains("D")){
				if(lastop.contains(mpe.function_names[j].split(", ")[i])){
					flags[j][i] = 1;
    			}
			}

    	}
    		
    }
    
	} else  {
		for(int j = 0; j < mpe.module_names.length;j++){
	    	for(int i = 0; i < mpe.function_names[j].split(", ").length; i++){ 
	    		total_count_e[j][i] = dir.get(mpe.function_names[j].split(", ")[i]+"()E") == null ? "" : dir.get(mpe.function_names[j].split(", ")[i]+"()E");
	    		total_count_e_dt[j][i] = DT.get(mpe.function_names[j].split(", ")[i]+"()E") == null ? "" : DT.get(mpe.function_names[j].split(", ")[i]+"()E");
	    		total_count_r[j][i] = dir.get(mpe.function_names[j].split(", ")[i]+"()R") == null ? "" : dir.get(mpe.function_names[j].split(", ")[i]+"()R");
	    		total_count_r_dt[j][i] = DT.get(mpe.function_names[j].split(", ")[i]+"()R") == null ? "" : DT.get(mpe.function_names[j].split(", ")[i]+"()R");
	    		total_count_d[j][i] = dir.get(mpe.function_names[j].split(", ")[i]+"()D") == null ? "" : dir.get(mpe.function_names[j].split(", ")[i]+"()D");
	    		total_count_d_dt[j][i] = DT.get(mpe.function_names[j].split(", ")[i]+"()D") == null ? "" : DT.get(mpe.function_names[j].split(", ")[i]+"()D");
	    		if(lastchar.contains("R")){
	    			if(lastop.contains(mpe.function_names[j].split(", ")[i])){
						flags[j][i] = 0;
	    			}
				}
				else if(lastchar.contains("D")){
					if(lastop.contains(mpe.function_names[j].split(", ")[i])){
						flags[j][i] = 1;
	    			}
				}
	    	}	
	    }
	}

	// SagarB20250516 Below block to check if any process is running or not
	Preferences preferences =  Preferences.userRoot().node("/processFlag");
 	preferences.put("Flag", "1");
    String processFlag = preferences.get("Flag", "1");
    
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
						for (int j = 0; j < mpe.module_names.length; j++) { %>
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
											    		<%= mpe.module_names[j] %>
											      		</button>
											    	</h2>
											    	<div id="collapse<%= j %>" class="accordion-collapse collapse" aria-labelledby="heading<%= j %>">
											      		<div class="accordion-body accor-body">
											        		<div class="row">
																<div class="table-responsive">
																	<table class="table table-bordered table-hover">
																		<thead>
																			<tr>
																				<th> Check All <br> <input type="checkbox" class="form-check-input" onclick="checkVertical('<%=mpe.module_names[j] %>');" name="checkAll" id="checkAll-<%=mpe.module_names[j] %>"></th>
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
																			for(int i=0; i < mpe.function_names[j].split(", ").length; i++)
																			{ %>
																			<% 
																			String value = "";
																			//if (function_names[j].split(", ")[i].contains("-D")) {
																			//	value = function_names[j].split(", ")[i].substring(0, function_names[j].split(", ")[i].length()-2);
																			//}
																			//else {
																				value = mpe.function_names[j].split(", ")[i];
																			//}
																			
																			
																				
																			
																			%>
																					<tr>
																						<td align="center">
																							<!-- <font title="Click to Edit" style="color:var(--header_color)">
																								<i class="fa fa-edit fa-lg" 
																								data-bs-toggle="modal" data-bs-target="#myModal" 
																								></i>
																							</font> -->
																							<input type="checkbox" class="form-check-input" name="chk" 
																							id="<%= mpe.module_names[j] %>-<%= (j+1) %>-<%= (i+1) %>" value = "<%= value %>"                                                     
																							
																							<%--  <% 
																							  if(!mpe.independent_function_names.contains(mpe.function_names[j].split(", ")[i])){
																								  if(mpe.depend.containsKey(mpe.function_names[j].split(", ")[i])){
																									  if(mpe.depend.get(mpe.function_names[j].split(", ")[i]).contains(",")){
																										  for(int a =0; a < mpe.depend.get(mpe.function_names[j].split(", ")[i]).split(",").length; a++){
																											  if(total_count_e[j][ArrayUtils.indexOf(mpe.function_names[j].split(", "),mpe.depend.get(mpe.function_names[j].split(", ")[i]).split(",")[a])] != "" && total_count_r[j][ArrayUtils.indexOf(mpe.function_names[j].split(", "),mpe.depend.get(mpe.function_names[j].split(", ")[i]).split(",")[a])] != ""){
																												  if(flags[j][ArrayUtils.indexOf(mpe.function_names[j].split(", "),mpe.depend.get(mpe.function_names[j].split(", ")[i]).split(",")[a])] == 1 ){
																													  //if flag = 0
																													  %> disabled <%
																												  }
																											  }
																											  else{
																												  %> disabled <%
																											  }
																										  }
																									  }
																									  else{
																										  if(total_count_e[j][ArrayUtils.indexOf(mpe.function_names[j].split(", "),mpe.depend.get(mpe.function_names[j].split(", ")[i]))] != "" && total_count_r[j][ArrayUtils.indexOf(mpe.function_names[j].split(", "),mpe.depend.get(mpe.function_names[j].split(", ")[i]))] != ""){
																											  if(flags[j][ArrayUtils.indexOf(mpe.function_names[j].split(", "),mpe.depend.get(mpe.function_names[j].split(", ")[i]))] == 1){
																												  %> disabled <%
																											  }
																										  }
																										  else{
																											  %> disabled <%
																										  }
																									  }
																								  }
																								  else{
																									  %> disabled <%
																								  }
																							 }
																								
																								%>  --%>
																								>
																							
																						</td>
																						<td align="center"> <%= i+1 %> </td>
																						<td <% if (total_count_e[j][i].equals("0")) { %> title="Database has no data." <% } %> align="center"><%= total_count_e[j][i]%><br> <font> <%=  total_count_e_dt[j][i]%> </font> </td>
																						<td align="center"><%=total_count_r[j][i]%><br> <font> <%=  total_count_r_dt[j][i]%> </font> </td>
																						<td align="center"><%=total_count_d[j][i]%><br> <font> <%=  total_count_d_dt[j][i]%> </font> </td> 
																						<td align="left"><%= value%></td>
																						<td align="left"><%= mpe.fn_name[j][i]%></td>
																						
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
				                <%if(write_access.equals("Y")){ %>
					            <div class="col-auto">
					             	<input type="button"  class="btn btn-sm request_btn" value="Generate Excel" <% if (processFlag.contains("0")) { %> title="Process is already Running." onclick="" style="background-color: #E6E6E6; color: black;" <% } else {%> onclick="generateExcel(); checkboxEnable();" <% } %> >
					            </div>
				                <div class="col-auto">
				                    <input type="button" class="btn btn-sm request_btn" value="Read Excel" <% if (processFlag.contains("0")) { %> title="Process is already Running." onclick="" style="background-color: #E6E6E6; color: black;" <% } else {%> onclick="readExcel();" <% } %>>
				                </div>
				                <div class="col-auto">
				                    <input type="button" class="btn btn-sm request_btn" value="RollBack" <% if (processFlag.contains("0")) { %> title="Process is already Running." onclick="" style="background-color: #E6E6E6; color: black;" <% } else {%> onclick="rollback(); " <% } %>>
				                </div>
				                <div class="col-auto">
				                    <input type="button" class="btn btn-sm request_btn" value="RollBack-All" <% if (processFlag.contains("0")) { %> title="Process is already Running." onclick="" style="background-color: #E6E6E6; color: black;" <% } else {%> onclick="rollbackAll(); " <% } %>>
				                </div>
				                <% } else { %>
					            <div class="col-auto">
					            	<input type="button"  class="btn btn-sm request_btn" value="Generate Excel" disabled >
					            </div>
				                <div class="col-auto">
				                    <input type="button" class="btn btn-sm request_btn" value="Read Excel" disabled>
				                </div>
				                <div class="col-auto">
				                    <input type="button" class="btn btn-sm request_btn" value="RollBack" disabled>
				                </div>
				                <div class="col-auto">
				                    <input type="button" class="btn btn-sm request_btn" value="RollBack-All" disabled>
				                </div>
				                <% } %>
				            </div>
				        </div>
				    </div>
				</div> 
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