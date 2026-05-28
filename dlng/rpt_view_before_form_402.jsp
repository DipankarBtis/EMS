<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>

<style>
.accordion {
  background-color: #eee;
  color: #444;
  cursor: pointer;
  padding: 6px;
  width: 100%;
  border: none;
  text-align: left;
  outline: none;
  font-size: 15px;
  transition: 0.4s;
}

 .active, .accordion:hover {
  background-color: #ccc; 
}

.panel {
  padding: 0 18px;
  display: none;
  background-color: white;
  overflow: hidden;
} 

table td {
font-family: Arial;
font-size: 14px;
}
</style>
<style>
.readonly {
  background-color: #99ffcc;
}
</style>
</head>
<jsp:useBean class="com.etrm.fms.dlng.DataBean_DLNG_Invoice" id="dlng_inv" scope="request"></jsp:useBean>
<%
String owner_cd=session.getAttribute("comp_cd").toString().equals("null")?"":""+session.getAttribute("comp_cd");
String owner_abbr=""+session.getAttribute("comp_abbr")==null?"":""+session.getAttribute("comp_abbr");
String owner_nm=""+session.getAttribute("comp_nm")==null?"":""+session.getAttribute("comp_nm");

String counterparty_cd = request.getParameter("counterparty_cd")==null?"":request.getParameter("counterparty_cd");
String cont_no = request.getParameter("cont_no")==null?"":request.getParameter("cont_no");
String cont_rev=request.getParameter("cont_rev")==null?"":request.getParameter("cont_rev");
String agmt_no = request.getParameter("agmt_no")==null?"":request.getParameter("agmt_no");
String agmt_rev=request.getParameter("agmt_rev")==null?"":request.getParameter("agmt_rev");
String contract_type = request.getParameter("contract_type")==null?"":request.getParameter("contract_type");
String plant_seq = request.getParameter("plant_seq")==null?"":request.getParameter("plant_seq");
String bu_plant_seq = request.getParameter("bu_unit")==null?"":request.getParameter("bu_unit");
String period_start_dt=request.getParameter("period_start_dt")==null?"":request.getParameter("period_start_dt");
String period_end_dt=request.getParameter("period_end_dt")==null?"":request.getParameter("period_end_dt");
String inv_type = request.getParameter("inv_type")==null?"":request.getParameter("inv_type");
String bu_unit=request.getParameter("bu_unit")==null?"0":request.getParameter("bu_unit");
String financial_year=request.getParameter("financial_year")==null?"":request.getParameter("financial_year");
String inv_flag=request.getParameter("inv_flag")==null?"":request.getParameter("inv_flag");

String mapping_id=request.getParameter("mapping_id")==null?"":request.getParameter("mapping_id");
String truck_trans_cd=request.getParameter("truck_trans_cd")==null?"":request.getParameter("truck_trans_cd");
String truck_cd=request.getParameter("truck_cd")==null?"":request.getParameter("truck_cd");
String driver_cd=request.getParameter("driver_cd")==null?"":request.getParameter("driver_cd");
String check_post_cd=request.getParameter("check_post_cd")==null?"":request.getParameter("check_post_cd");

dlng_inv.setCallFlag("GET_FORM-402_DETAIL");
dlng_inv.setComp_cd(owner_cd);
dlng_inv.setCounterparty_cd(counterparty_cd);
dlng_inv.setAgmt_no(agmt_no);
dlng_inv.setAgmt_rev_no(agmt_rev);
dlng_inv.setCont_no(cont_no);
dlng_inv.setCont_rev_no(cont_rev);
dlng_inv.setContract_type(contract_type);
dlng_inv.setPlant_seq(plant_seq);
dlng_inv.setPeriod_start_dt(period_start_dt);
dlng_inv.setPeriod_end_dt(period_end_dt);
dlng_inv.setBu_unit(bu_unit);
dlng_inv.setInv_flag(inv_flag);
dlng_inv.setMapping_id(mapping_id);
dlng_inv.setTruck_trans_cd(truck_trans_cd);
dlng_inv.setTruck_cd(truck_cd);
dlng_inv.setDriver_cd(driver_cd);
dlng_inv.setCheck_post_cd(check_post_cd);
dlng_inv.init();

String check_post_nm = dlng_inv.getCheck_post_nm();

String counterparty_nm=dlng_inv.getCounterparty_nm();
String driver_nm=dlng_inv.getDriver_nm();
String driver_addr=dlng_inv.getDriver_addr();
String licence_no=dlng_inv.getLicence_no();
String licence_issue_state=dlng_inv.getLicence_issue_state();
String truck_no=dlng_inv.getTruck_no();
String transporter_name=dlng_inv.getTransporter_name();
String transporter_addr=dlng_inv.getTransporter_addr();

String plantAddress=dlng_inv.getPlantAddress();
String plantCity=dlng_inv.getPlantCity();
String plantState=dlng_inv.getPlantState();
String plantPin=dlng_inv.getPlantPin();
String plantNm=dlng_inv.getPlantNm();

String cust_vat_no=dlng_inv.getCust_vat_no();
String cust_gst_no=dlng_inv.getCust_gst_no();
String cust_cst_no=dlng_inv.getCust_cst_no();

String cust_vat_dt=dlng_inv.getCust_vat_dt();
String cust_gst_dt=dlng_inv.getCust_gst_dt();
String cust_cst_dt=dlng_inv.getCust_cst_dt();

String bu_cst_no=dlng_inv.getBu_cst_no();
String bu_cst_dt=dlng_inv.getBu_cst_dt();
String not=dlng_inv.getNot();
%>
<body>
<form action="">
	<table width="100%">
		<tr>
			<td align="center" colspan="12">
				<h1>FORM-402</h1>
			</td>
		</tr>
		<tr>
			<td colspan="12">
				<h2><button type="button" class="accordion" style="font-size: 20px;">Basic Info</button></h2>
			</td>
		</tr>
		
		<tr>
			<td colspan="8">
				Check Post Name <font color="red">*</font>  
			</td>
			
			<td colspan="4">
				<input type="text" size="60" required="required"   id="checkPost" value="<%=check_post_nm%>" class="readonly" readonly="readonly">  
			</td>
		</tr>
		
		<tr>	
			<td colspan="8">
				Place from Which Goods are Dispatched <font color="red">*</font>  
			</td>
			
			<td colspan="4">
				<input type="text" size="60" value="Gujarat" class="readonly" readonly="readonly">  
			</td>
		</tr>
		
		<tr>
			<td colspan="8">
				District from Which Goods are Dispatched
			</td>
			
			<td colspan="4">
				<input type="text" size="60">  
			</td>
		</tr>
		
		<tr>
			<td colspan="8">
				Place to Which Goods are Dispatched <font color="red">*</font>  
			</td>
			
			<td colspan="4">
				<input type="text" size="60" value="<%=plantState%>" class="readonly" readonly="readonly">  
			</td>
		</tr>
		
		<tr>
			<td colspan="8">
				District to Which Goods are Dispatched
			</td>
			<td colspan="4">
				<input type="text" size="60">  
			</td>
		</tr>	
		
		
		<tr>
			<td colspan="12">
				<h2><button style="font-size: 20px;" type="button" class="accordion">Consignor Details</button></h2>
			</td>
		</tr>
		<tr>
			<td colspan="8">
				CST Certificate Number <font color="red">*</font>  
			</td>
			
			<td colspan="4">
				<input type="text" class="readonly" size="60" value="<%=bu_cst_no%>" readonly="readonly">  
			</td>
		</tr>
		
		<tr>	
			<td colspan="8">
				Nature of Transactions <font color="red">*</font>  
			</td>
			
			<td colspan="4">
				<input type="text" size="60" value="<%=not %>" class="readonly" readonly="readonly">  
			</td>
		</tr>
		
		<tr>
			<td colspan="12">
				<h2><button style="font-size: 20px;" type="button" class="accordion">Consignee Details</button></h2>
			</td>
		</tr>
		<tr>
			<td colspan="8">
				Name <font color="red">*</font>  
			</td>
			
			<td colspan="4">
				<input type="text" size="60" class="readonly"  value="<%=counterparty_nm%>" readonly="readonly" >  
			</td>
		</tr>
		
		<tr>	
			<td colspan="8">
				Address <font color="red">*</font>  
			</td>
			<td colspan="4">
				<textarea rows="3" cols="60" style="text-align: left;" class="readonly" readonly="readonly" ><%=plantAddress +","+plantCity+" - "+plantPin%></textarea>  
			</td>
		</tr>
		
		<tr>
			<td colspan="8">
				VAT Registration Certificate No.   
			</td>
			
			<td colspan="4">
				<input type="text" size="60" value="<%=cust_vat_no %>">  
			</td>
		</tr>
		
		<tr>	
			<td colspan="8">
				VAT Registration Effective date
			</td>
			
			<td colspan="4">
				<input type="text" size="60" value="<%=cust_vat_dt %>">  
			</td>
		</tr>
		
		<tr>
			<td colspan="8">
				CST Registration No.
			</td>
			
			<td colspan="4">
				<input type="text" size="60" value="<%=cust_cst_no%>" class="readonly" readonly="readonly">  
			</td>
		</tr>
		
		<tr>	
			<td colspan="8">
				CST Registration Effective date
			</td>
			
			<td colspan="4">
				<input type="text" size="60" value="<%=cust_cst_dt%>" class="readonly" readonly="readonly">  
			</td>
		</tr>
		
		<tr>
			<td colspan="8">
				GSTIN Registration No.
			</td>
			
			<td colspan="4">
				<input type="text" size="60" value="<%=cust_gst_no%>" class="readonly" readonly="readonly">  
			</td>
		</tr>
		
		<tr>	
			<td colspan="8">
				GSTIN Registration Effective date
			</td>
			
			<td colspan="4">
				<input type="text" size="60" value="<%=cust_gst_dt%>" class="readonly" readonly="readonly">  
			</td>
		</tr>
		
		<tr>
			<td colspan="8">
				Telephone No.
			</td>
			
			<td colspan="4">
				<input type="text" size="60">  
			</td>
		</tr>
		
		<tr>	
			<td colspan="8">
				Consignee Fax No.
			</td>
			
			<td colspan="4">
				<input type="text" size="60">  
			</td>
		</tr>
		
		<tr>
			<td colspan="8">
				Consigned Value Rs. <font color="red">*</font> (Total Value of Invoice)
			</td>
			
			<td colspan="4">
				<input type="text" size="60" value="" class="readonly" readonly="readonly" id="net_amt_inr">
				<script>
					document.getElementById("net_amt_inr").value = window.opener.document.forms[0].net_payable.value;
				</script>  
			</td>
		</tr>
		
		<tr>
			<td colspan="11">
				Whether Shipping Address is within the state of Gujarat and shipping is to dealer other than consignee, as declared above ?
			</td>
			
			<td colspan="1">
				<input type="text" size="60" >  
			</td>
		</tr>
		<tr>
			<td colspan="11">
				Whether this is final shipping to original buyer outside the state of Gujarat
			</td>
			
			<td colspan="1">
				<input type="text" size="60">  
			</td>
		</tr>
		
		<tr>
			<td colspan="12"><h2><button  style="font-size: 20px;" type="button" class="accordion">Commodity Details</button></h2></td>
		</tr>	
		
		<tr>
			<td colspan="8" >Invoice No. <font color="red">*</font></td>
			<td colspan="4"><input type="text" size="60" value="<%//=invno %>" class="readonly" readonly="readonly"></td>
		</tr>
		
		<tr>	
			<td colspan="8"> Invoice Date<font color="red">*</font></td>
			<td colspan="4"><input type="text" size="60" class="readonly" value="<%//=inv_dt %>" readonly="readonly" id="inv_dt"></td>
				
				<script type="text/javascript">
					document.getElementById("inv_dt").value=window.opener.document.forms[0].invoice_dt.value;
				</script>
		</tr>
		<tr>
			<td colspan="8"> Commodity Name<font color="red">*</font></td>
			<td colspan="4"><input type="text" size="60" class="readonly" readonly="readonly" value="Liquified Natural Gas (LNG)"></td>
		</tr>
		 <tr>
			<td colspan="8">Quantity <font color="red">*</font></td>
			<td colspan="4"><input type="text" size="60" class="readonly" readonly="readonly" id="total_gas">
			
				<script type="text/javascript">
					document.getElementById("total_gas").value=window.opener.document.forms[0].alloc_qty.value;
				</script>
			</td>
		</tr>
		<tr>
			<td colspan="8">Unit of Measurement <font color="red">*</font></td>
			<td colspan="4"><input type="text" size="60" class="readonly" readonly="readonly" value="Nos."></td>
		</tr>
		<tr>	
			<td colspan="8"> Rate of tax <font color="red">*</font></td>
			<td colspan="4"><input type="text" size="60" class="readonly" readonly="readonly" id="taxRate">
				<script type="text/javascript">
					document.getElementById("taxRate").value=window.opener.document.forms[0].tax_factor.value;
				</script>
			
			</td>
		</tr>
		
		<tr>	
			<td colspan="8">Amount <font color="red">*</font></td>
			<td colspan="4"><input type="text" size="60" class="readonly" readonly="readonly" id="amt">
			<script>
					document.getElementById("amt").value = window.opener.document.forms[0].net_payable.value;
				</script>  
			
			</td>
			
		</tr> 	
		<tr>
			<td colspan="12">
				<h2><button style="font-size: 20px;" type="button" class="accordion">Transporter Details 
						<span id="msg" style="visibility: hidden;color: red"> No Truck Linked with Transporter-Driver-Truck</span>
					</button>
				</h2>
			
			<script type="text/javascript">
				if(window.opener.document.forms[0].truckLinkedFlg.value=='N'){
					document.getElementById("msg").style.visibility='visible';
				}
				
				</script>
			</td>
		</tr>	
		
		<tr>
			<td colspan="8">Name <font color="red">*</font></td>
			<td colspan="4"><input type="text" size="60" id="truck_trans_nm" value="<%=transporter_name%>" readonly="readonly" class="readonly"> 
			</td>
		</tr>
		<tr>	
			<td colspan="8"> Address</td>
			<td colspan="4">
				<textarea rows="3" cols="60" id="truck_trans_addrs" class="readonly" readonly="readonly"><%=transporter_addr%></textarea>
			</td>
		</tr>
		<tr>	
			<td colspan="8" > Owner / Partner Name </td>
			<td colspan="4"><input type="text" size="60" ></td>
		</tr>
		<tr>
			<td colspan="8" align="left"> Vehicle No. <font color="red">*</font></td>
			<td colspan="4"><input type="text" size="60" id="truck_nm" value="<%=truck_no%>" class="readonly" readonly="readonly"></td>
		</tr>
		<tr>	
			<td colspan="8">LR No.</td>
			<td colspan="4"><input type="text" size="60"></td>
		</tr>
		<tr>	
			<td colspan="8"> LR date</td>
			<td colspan="4"><input type="text" size="60"></td>
		</tr>
		
		
		<tr>
			<td colspan="12"><h2><button style="font-size: 20px;" type="button" class="accordion">Driver Details</button></h2></td>
		</tr>	
		
		<tr>
			<td colspan="8">Name <font color="red">*</font></td>
			<td colspan="4"><input type="text" size="60" id="truck_driver" value="<%=driver_nm%>" class="readonly" readonly="readonly">
			</td>
		</tr>
		<tr>
			<td colspan="4" align="left"> Address</td>
			<td colspan="10">
			<textarea rows="3" cols="60" id="truck_driver_addrs" class="readonly" readonly="readonly"><%=driver_addr%></textarea>
			</td>
		</tr>
		<tr>	
			<td colspan="8"> Driving Licence No. <font color="red">*</font> </td>
			<td colspan="4"><input type="text" size="60" id="truck_driver_lic_no" value="<%=licence_no%>" class="readonly" readonly="readonly">
			</td>
		</tr>
		<tr>	
			<td colspan="8"> Licence Issueing State  <font color="red">*</font> </td>
			<td colspan="4"><input type="text" size="60" id="truck_lic_state" value="<%=licence_issue_state%>" class="readonly" readonly="readonly">
			</td>
		</tr>
	</table>
</form>

</body>
