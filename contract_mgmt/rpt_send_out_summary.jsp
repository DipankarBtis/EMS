<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script>
function refresh()
{
	var date = document.forms[0].date.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var flag=true;
	var msg="";
	
	if(month=='0')
	{
	alert("Please Select Month");
	flag=false;
	}
	if(year=='0')
	{
	alert("Please Select year");
	flag=false;
	}
	
	  if(month!=null && year!=null)
	  {
		if(trim(month)!="" && trim(year)!="")
		{
		    var u = document.forms[0].u.value;
			if(flag==true)
			{
				var url = "rpt_send_out_summary.jsp?u="+u+"&month="+month+
				"&year="+year;
				
				document.getElementById("loading").style.visibility = "visible";
				location.replace(url);
			}
		}
		else
		{
			alert("Please select send out month");
		}
	  }
	  else
	 {
		alert("Please select send out month");
	 }
}

function exportToXls()
{
	var date = document.forms[0].date.value;
	var month = document.forms[0].month.value;
	var year = document.forms[0].year.value;
	var owner_abbr = document.forms[0].owner_abbr.value;
	var mnth = document.forms[0].mnth.value;
	var fileName = owner_abbr+"_Send-Out Summary- ";
	var url = "xls_send_out_summary.jsp?fileName="+fileName +""+mnth+"-"+year+".xls&month="+month+
	"&year="+year;

     location.replace(url);
}

</script>

<%@ include file="../util/common_js.jsp"%>
<jsp:useBean class="com.etrm.fms.contract_mgmt.DB_ContractMgmt_Rpt2" id="contract_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
</head>
<%
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();
int filter_start_year = CommonVariable.filter_start_year;

String sysdate=utildate.getSysdate();
String month = request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year = request.getParameter("year")==null?""+currentYear:request.getParameter("year");
if(month.length() == 1)
{
	month="0"+month; 
}
String date = "01/"+month+"/"+year;
String mnth = utildate.getShortMonthName(date);

contract_mgmt.setFrom_dt(date);
contract_mgmt.setComp_cd(owner_cd);
contract_mgmt.setCallFlag("SEND_OUT_SUMMARY");
contract_mgmt.init();


Vector VCOUNTERPARTY_CD = contract_mgmt.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = contract_mgmt.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = contract_mgmt.getVCOUNTERPARTY_NM();
Vector VCONT_REF = contract_mgmt.getVCONT_REF();
Vector VCONT_NO = contract_mgmt.getVCONT_NO();
Vector VAGMT_BASE = contract_mgmt.getVAGMT_BASE();
Vector VBALANCE = contract_mgmt.getVTOTAL_BALANCE();
Vector VREMARK = contract_mgmt.getVREMARK();
Vector VCONTRACT_TYPE = contract_mgmt.getVCONTRACT_TYPE();
Vector VTCQ = contract_mgmt.getVTCQ();
Vector VQTY_MMBTU = contract_mgmt.getVQTY_MMBTU();
Vector VSN_LOA_TOTAL_BALANCE = contract_mgmt.getVSN_LOA_TOTAL_BALANCE();

Vector VCUSTOMER_CD_LTCORA = contract_mgmt.getVCUSTOMER_CD_LTCORA();
Vector VCUSTOMER_NM_LTCORA = contract_mgmt.getVCUSTOMER_NM_LTCORA();
Vector LTCORA_TOTAL_UNLOADED_VOLUME = contract_mgmt.getVLTCORA_TOTAL_UNLOADED_VOLUME();
Vector LTCORA_TOTAL_UNLOADED_VOLUME_MCM = contract_mgmt.getVLTCORA_TOTAL_UNLOADED_VOLUME_MCM();


date = contract_mgmt.getFrom_date();
int max_count_for_column = contract_mgmt.getMax_count_for_column();
String selected_from_date = contract_mgmt.getSelected_from_date();
String selected_prev_month_year = contract_mgmt.getSelected_prev_month_year();
String  from_dt  = contract_mgmt.getFrom_date();
String  volume_received_sales  = contract_mgmt.getVolume_received_sales();
String  volume_received_sales_mcm  = contract_mgmt.getVolume_received_sales_mcm();
String  month_opening_stock  = contract_mgmt.getMonth_opening_stock();
String  month_opening_stock_mcm  = contract_mgmt.getMonth_opening_stock_mcm();
String  internal_consumption_total  = contract_mgmt.getInternal_consumption_total();
String  internal_consumption_total_mcm  = contract_mgmt.getInternal_consumption_total_mcm();
String  dead_stock  = contract_mgmt.getDead_stock();
String  dead_stock_mcm  = contract_mgmt.getDead_stock_mcm();
String  month_total_sales  = contract_mgmt.getMonth_total_sales();
String  month_total_sales_mcm  = contract_mgmt.getMonth_total_sales_mcm();
String  outstanding_commitment_total  = contract_mgmt.getOutstanding_commitment_total();
String  outstanding_commitment_total_mcm  = contract_mgmt.getOutstanding_commitment_total_mcm();
String  net_uncommited_total  = contract_mgmt.getNet_uncommited_total();
String  net_uncommited_total_mcm  = contract_mgmt.getNet_uncommited_total_mcm();
String  volume_expected_sales  = contract_mgmt.getVolume_expected_sales();
String  volume_expected_sales_mcm  = contract_mgmt.getVolume_expected_sales_mcm();
double  consumption_percentage  = contract_mgmt.getConsumption_percentage();
String  total_sug_lt = contract_mgmt.getTotal_sug_lt();
String  total_sug_lt_mcm = contract_mgmt.getTotal_sug_lt_mcm();
String  net_commit_lt_vol = contract_mgmt.getNet_commit_lt_vol();
String  net_commit_lt_vol_mcm = contract_mgmt.getNet_commit_lt_vol_mcm();
String  total_expect_vol = contract_mgmt.getTotal_expect_vol();
String  total_expect_vol_mcm = contract_mgmt.getTotal_expect_vol_mcm();
String  total_vol_supplied = contract_mgmt.getTotal_vol_supplied();
String  total_vol_supplied_mcm = contract_mgmt.getTotal_vol_supplied_mcm();
String  total_outstanding_commitment = contract_mgmt.getTotal_outstanding_commitment();
String  total_outstanding_commitment_mcm = contract_mgmt.getTotal_outstanding_commitment_mcm();

%>
<body>
<%@ include file="../home/header.jsp"%>
<form>
 <div class="box-body">
  <div class="row">
   <div class="col-sm-12 col-xs-12 col-md-12">
    <div class="card cardmain">
     <div class="card-header cdheader">
      <div class="d-flex  justify-content-between">
        <div class="topheader">
          Send Out Summary Statement
        </div>
        <div class="col-auto" onclick="exportToXls();" style="color:green;">
            <span class="input-group-text" ><i title="Export To Excel" style="color:green;" class="fa fa-file-excel-o fa-2x"></i></span>
        </div>
      </div>
     </div>
     <div class="card-body cdbody">
      <div class="row">
       <div class="col-sm-3 col-xs-3 col-md-3"></div>
        <div class="col-auto">
          <div class="form-group row">
			 <label class="form-label"><b>MONTH</b></label>
		  </div>
         </div>
         <div class="col-sm-2 col-xs-2 col-md-2">
		   <div class="form-group row">
			<div class="col">
				<select class="form-select form-select-sm" name="month" onchange="">
					<option value="01" label="January">January</option>
					<option value="02" label="February">February</option>
					<option value="03" label="March">March</option>
					<option value="04" label="April">April</option>
					<option value="05" label="May">May</option>
					<option value="06" label="June">June</option>
					<option value="07" label="July">July</option>
					<option value="08" label="August">August</option>
					<option value="09" label="September">September</option>
					<option value="10" label="October">October</option>
					<option value="11" label="November">November</option>
					<option value="12" label="December">December</option>
				</select>
				<script>document.forms[0].month.value="<%=month%>"</script>
			</div>
		   </div>
		 </div>
         <div class="col-auto">
           <div class="form-group row">
			  <label class="form-label"><b>YEAR</b></label>
		   </div>
          </div>
		  <div class="col-sm-2 col-xs-2 col-md-2">
		   <div class="form-group row">
		    <div class="col">
  			  <select class="form-select form-select-sm" name="year" onchange="">
  				 <%for(int i=(currentYear+1); i > (filter_start_year);i--){ %>
					<option value="<%=i%>"><%=i%></option>
				 <%} %>
			  </select>
			  <script>document.forms[0].year.value="<%=year%>"</script>
		   </div>
		 </div>
		</div>
        <div class="col-auto">
			<input type="button" class="btn btn-warning com-btn" value="Apply Filter" onclick="refresh();">
		</div>
       </div>
      </div>
	   <!-- DATA -->
	  <div class="card-body cdbody">
	   <div class="row">
	   <div class="row m-b-5">
			<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> RLNG SALES</label>
	   </div>
	    <%if(VCOUNTERPARTY_CD.size()>0 || VCUSTOMER_CD_LTCORA.size()>0){ %>
	      <div class="table-responsive">
	       <table class="table table-bordered table-hover"  >
		      <tr>
		      <%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
		       <th colspan="2" style="<%if(owner_cd.equals("2")){ %>background-color:#336699; color:white;text-align:center;<%}
		        else{ %> background-color:#FFD700; color:black; text-align:center;<%} %>" 
		        title="<%=VCOUNTERPARTY_NM.elementAt(i)%>" > 
		       <%=VCOUNTERPARTY_ABBR.elementAt(i) %></th>
		       <%} %>
		      </tr>
		      <tr>
		      <%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
		       <td colspan="1"><b>&nbsp;</b></td>
		       <td colspan="1" align="right"><b>MMBTU&nbsp;</b></td>
		       <%} %>
		      </tr>
		      <%for(int j=0; j<max_count_for_column; j++){ %>
		        <tr>
		        <%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
		         <%if(((Vector)VREMARK.elementAt(i)).elementAt(j)!=""){ %>
		         <td colspan="1" align="left"><b style="color:red;"><%=((Vector)VREMARK.elementAt(i)).elementAt(j)%></b></td>
		         <td colspan="1">&nbsp;</td>
		         <%} else{ %>
		         <td colspan="1" style="height:30px;">&nbsp;</td>
		         <td colspan="1">&nbsp;</td>
		         <% } %>
		        <%} %>
		       </tr>
		       <tr>
		        <% for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
		        <td align="left">
		          <%if((""+((Vector)VCONTRACT_TYPE.elementAt(i)).elementAt(j)).trim().equalsIgnoreCase("S"))
		          { %><span title="<%=VCOUNTERPARTY_NM.elementAt(i) %> [<%=VAGMT_BASE.elementAt(i) %>]">Supply Notice&nbsp;</span>
						<% if (!((Vector)VCONT_REF.elementAt(i)).elementAt(j).toString().equals("")){ %>
						[ <%=((Vector)VCONT_REF.elementAt(i)).elementAt(j)%>]
						<%}else{%>
						 [<%=((Vector)VCONT_NO.elementAt(i)).elementAt(j)%>]
					<%		}
		          }
				  else if((""+((Vector)VCONTRACT_TYPE.elementAt(i)).elementAt(j)).trim().equalsIgnoreCase("L"))
				  { %><span title="<%=VCOUNTERPARTY_NM.elementAt(i) %> [<%=VAGMT_BASE.elementAt(i) %>]">Letter of Agreement&nbsp;</span>
						<% if (!((Vector)VCONT_REF.elementAt(i)).elementAt(j).toString().equals("")){ %>
						[<%=((Vector)VCONT_REF.elementAt(i)).elementAt(j)%>]
						<%}
						  else{%>
							[<%=((Vector)VCONT_NO.elementAt(i)).elementAt(j)%>]
					<%	  }
				  }
				  else if((""+((Vector)VCONTRACT_TYPE.elementAt(i)).elementAt(j)).trim().equalsIgnoreCase("X"))
				  { %><span title="<%=VCOUNTERPARTY_NM.elementAt(i) %> [<%=VAGMT_BASE.elementAt(i) %>]">IGX&nbsp;</span>
						<% if (!((Vector)VCONT_REF.elementAt(i)).elementAt(j).toString().equals("")){ %>
						[<%=((Vector)VCONT_REF.elementAt(i)).elementAt(j)%>]
						<%}
						  else{%>
							[<%=((Vector)VCONT_NO.elementAt(i)).elementAt(j)%>]
					<%	  }
				  }
				  else{%>&nbsp;
				<%}%>				  
		        </td>
		        <td align="right"><%=((Vector)VTCQ.elementAt(i)).elementAt(j)%>&nbsp;</td>
		        <%} %>
		       </tr>
		       <tr>
		       <%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
		         <td align="left"><%if(!(""+((Vector)VTCQ.elementAt(i)).elementAt(j)).trim().equals("")){%>Qty supplied<%}%></td>
				 <td align="right"><%=((Vector)VQTY_MMBTU.elementAt(i)).elementAt(j)%></td>
		       <%} %>
		       </tr>
		       <tr>
		       <%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
		         <td align="left"><%if(!(""+((Vector)VTCQ.elementAt(i)).elementAt(j)).trim().equals("")){%><b>Remaining Supply</b><%}%></td>
				 <td align="right"><b><%=((Vector)VBALANCE.elementAt(i)).elementAt(j)%></b></td>
		       <%} %>
		       </tr>
		       <tr>
					<td colspan="<%=((VCOUNTERPARTY_CD.size()*2))%>">&nbsp;</td>
			   </tr>
		       <% } %>
		       <tr>
		  		 <%	for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
					<td align="left">&nbsp;<b>Total Balance</b></td>
					<td align="right"><b><%=VSN_LOA_TOTAL_BALANCE.elementAt(i)%></b>&nbsp;</td>
		  		<%}	%>
			  </tr>
			  </table>
			</div>
			<div class="row">&nbsp;</div>
			<div class="row">&nbsp;</div>
			<div class="row">&nbsp;</div>

			<div class="row">
			  <div class="row m-b-5">
				 <label class="form-label subheader"><i class="fa fa-snowflake-o"></i> LTCORA</label>
		      </div>
			  <div class="table-responsive">
			    <table class="table table-bordered table-hover" >
			       <tr style="<%if(owner_cd.equals("2")){ %> background-color:#336699; color:white <%} 
			         else {%>background-color:#FFD700; color:black<% }%>">
			 	 		<th style="text-align:center;">LTCORA</th>
			 	 		<th style="text-align:center;">MMBTU</th>
			 	 		<th style="text-align:center;">MMSCM</th>
			 	 	</tr>
			 	 	<%for(int i=0;i<VCUSTOMER_CD_LTCORA.size();i++){ %>
			            <tr>
			                <td align="left"> Unloaded LTCORA Volumes of <%=VCUSTOMER_NM_LTCORA.elementAt(i)%> Cargo </td>
			 	 		    <td align="right"><%=LTCORA_TOTAL_UNLOADED_VOLUME.elementAt(i) %></td>
			 	 		    <td align="right"><%=LTCORA_TOTAL_UNLOADED_VOLUME_MCM.elementAt(i)%></td>
			            </tr>
			            <%} %>
			            <tr>
				 	 		<td align="left"> SUG Under LTCORA Agreement </td>
				 	 		<td align="right"><%=total_sug_lt %></td>
				 	 		<td align="right"><%=total_sug_lt_mcm %></td>
			 	 	    </tr>
			 	 	    <tr>
				 	 		<td align="left">Net Committed LTCORA Volume</td>
				 	 		<td align="right"><%= net_commit_lt_vol%></td>
				 	 		<td align="right"><%=net_commit_lt_vol_mcm %></td>
			 	 	    </tr>
			 	 	    <%-- <tr>
				 	 		<td align="left">LTCORA Volume Expected</td>
				 	 		<td align="right"><%=total_expect_vol %></td>
				 	 		<td align="right"><%=total_expect_vol_mcm %></td>
			 	 	    </tr> --%>
			 	 	
			 	 	    <tr>
				 	 		<td align="left">LTCORA Volume Supplied</td>
				 	 		<td align="right"><%=total_vol_supplied %></td>
				 	 		<td align="right"><%=total_vol_supplied_mcm %></td>
			 	 	    </tr>
			 	 	    <tr>
				 	 		<td  align="left"><b> OutStanding Commitment</b></td>
				 	 		<td  align="right"><b><%=total_outstanding_commitment %></b></td>
				 	 		<td align="right"><b><%=total_outstanding_commitment_mcm %></b></td>
			 	 	    </tr>
			 	 	   </table>
			 	 	   </div>
			 	 	  
			        
			        <div class="row m-b-5">
			         <label class="form-label subheader"><i class="fa fa-snowflake-o"></i> Send-Out Summary</label>
	                </div>
			 	 	<div class="table-responsive">
			        <table class="table table-bordered table-hover" >
			        
			        <tr style=" <%if(owner_cd.equals("2")){ %> background-color:#336699; color:white <% }
			           else{ %> background-color:#FFD700; color:black <%}%>">
			 	 		<th style="text-align:center;">Approximate nos.</th>
			 	 		<th style="text-align:center;">MMBTU</th>
			 	 		<th style="text-align:center;">MMSCM</th>
			 	  	</tr>
			        <tr>
			 	 		<td align="left">Opening tank levels on <%=selected_from_date %></td>
			 	 		<td align="right"><%=month_opening_stock%></td>
			 	 		<td align="right"><%=month_opening_stock_mcm%></td>
			 	 	</tr>
			 	 	<tr>
			 	 		<td align="left">Volume received during <%=selected_prev_month_year%></td>
			 	 		<td align="right"><%=volume_received_sales%></td>
			 	 		<td align="right"><%=volume_received_sales_mcm%></td>
			 	 	</tr>
			        <tr>
			 	 		<td align="left">Sales &amp;Regas Volume in <%=selected_prev_month_year%></td>
			 	 		<td align="right"><%=month_total_sales%></td>
			 	 		<td align="right"><%=month_total_sales_mcm%></td>
			 	 	</tr>
			 	 	<tr>
			 	 		<td align="left">Dead stock</td>
			 	 		<td align="right"><%=dead_stock%></td>
			 	 		<td align="right"><%=dead_stock_mcm%></td>
			 	 	</tr> 
			 	 	<tr>
			 	 		<td align="left">Expected own volumes</td>
			 	 		<td align="right"><%=volume_expected_sales%></td>
			 	 		<td align="right"><%=volume_expected_sales_mcm%></td>
			 	 	</tr>
			 	 	<tr>
			 	 		<td align="left">Outstanding commitment</td>
			 	 		<td align="right"><%=outstanding_commitment_total%></td>
			 	 		<td align="right"><%=outstanding_commitment_total_mcm%></td>
			 	 	</tr> 
			 	 	<tr>
			 	 		<td align="left">Internal consumption <%=consumption_percentage%>%</td>
			 	 		<td align="right"><%=internal_consumption_total%></td>
			 	 		<td align="right"><%=internal_consumption_total_mcm%></td>
			 	 	</tr>
			 	 	<tr><td colspan="3">&nbsp;</td></tr>
			 	 	<tr>
			 	 		<td align="left"><b>Net Uncommitted Volumes</b></td>
			 	 		<td align="right"><b><%=net_uncommited_total%></b></td>
			 	 		<td align="right"><b><%=net_uncommited_total_mcm%></b></td>
			 	 	</tr>
			 	 	<tr>
                      <td colspan="3" style="height:30px;">&nbsp;</td>
                    </tr>
			     </table>
			    </div>
			   </div>
	        <% } else { %>
	        <table>
	         <tr>
				<td align="center"><%=utilmsg.infoMessage("<b>...Send-out summary statement List not present for the selected Date...</b>") %></td>
			 </tr>
		    </table>
	       <%} %>
	    </div>
	   </div>
      </div>
    </div >
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
<input type="hidden" name="owner_abbr" value="<%=owner_abbr%>">
<input type="hidden" name="date" value="<%=date%>">
<input type="hidden" name="mnth" value="<%=mnth%>">

</form>
</body>
</html>