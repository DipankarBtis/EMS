<%@page import= "java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">

<jsp:useBean class="com.etrm.fms.contract_mgmt.DB_ContractMgmt_Rpt2" id="contract_mgmt" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>

<%
int currentYear = utildate.getCurrentYear();
int currentMonth = utildate.getCurrentMonth();

String sysdate=utildate.getSysdate();
String month = request.getParameter("month")==null?""+currentMonth:request.getParameter("month");
String year = request.getParameter("year")==null?""+currentYear:request.getParameter("year");
if(month.length() == 1)
{
	month="0"+month; 
}
String date = "01/"+month+"/"+year;
String fileName = request.getParameter("fileName")==null?"":request.getParameter("fileName");

String owner_cd="";

if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	owner_cd="";
}  
else
{
	owner_cd=""+session.getAttribute("comp_cd");
}
//System.out.println("dt"+dt+" date "+date);
contract_mgmt.setFrom_dt(date);
contract_mgmt.setComp_cd(owner_cd);
contract_mgmt.setCallFlag("SEND_OUT_SUMMARY");
contract_mgmt.init();


Vector VCOUNTERPARTY_CD = contract_mgmt.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = contract_mgmt.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = contract_mgmt.getVCOUNTERPARTY_NM();
Vector VCONT_REF = contract_mgmt.getVCONT_REF();
Vector VCONT_NO = contract_mgmt.getVCONT_NO();
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
String  volume_expected_sales  = contract_mgmt.getVolume_expected_sales_mcm();
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
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>

  <table width="100%" border="1">
		<tr>
		    <%if(VCOUNTERPARTY_CD.size()>0){ %>
			<th nowrap="nowrap" style="font-size: 21" colspan="<%=((VCOUNTERPARTY_CD.size()*2))%>" rowspan="" align="center">SEND-OUT SUMMARY STATEMENT</th>
			<%}else{ %>
			<th nowrap="nowrap" style="font-size: 21" colspan="4" rowspan="" align="center">SEND-OUT SUMMARY STATEMENT</th>
			<%} %>
		</tr>
	</table>
	<div class="row m-b-5">
			<label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <b>RLNG SALES</b></label>
	   </div>
	  <div class="row">
	      <!-- <thead> -->
	     <%if(VCOUNTERPARTY_CD.size()>0 || VCUSTOMER_CD_LTCORA.size()>0){ %>
	      <div class="table-responsive">
	       <table class="table table-bordered table-hover" width="100%" border="1">
		      <tr>
		      <%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
		       <td colspan="2" align="center"><b><%=VCOUNTERPARTY_NM.elementAt(i) %></b></td>
		       <%} %>
		      </tr>
		      <tr>
		      <%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
		       <td colspan="1"><div><b>&nbsp;</b></div></td>
		       <td colspan="1"><div align="right"><b>MMBTU&nbsp;</b></div></td>
		       <%} %>
		      </tr>
		      <%for(int j=0; j<max_count_for_column; j++){ %>
		         <tr>
		        <%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
		         <%if(((Vector)VREMARK.elementAt(i)).elementAt(j)!=""){ %>
		         <td colspan="1"><div align="left"><b style="color:red;"><%=((Vector)VREMARK.elementAt(i)).elementAt(j)%></b></div></td>
		         <td colspan="1">&nbsp;</td>
		         <%} else{ %>
		         <td colspan="1" style="height:30px;">&nbsp;</td>
		         <td colspan="1" style="height:30px;">&nbsp;</td>
		         <% } %>
		        <%} %>
		       </tr>
		       <tr>
		        <% for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
		        <td><div align="left">
		          <%if((""+((Vector)VCONTRACT_TYPE.elementAt(i)).elementAt(j)).trim().equalsIgnoreCase("S"))
		          { %><span title="<%=VCOUNTERPARTY_ABBR.elementAt(i) %>">Supply Notice&nbsp;</span>
						<% if (!((Vector)VCONT_REF.elementAt(i)).elementAt(j).toString().equals("")){ %>
						 [<%=((Vector)VCONT_REF.elementAt(i)).elementAt(j)%>]
						<%}else{%>
						 [<%=((Vector)VCONT_NO.elementAt(i)).elementAt(j)%>]
					<%		}
		          }
				  else if((""+((Vector)VCONTRACT_TYPE.elementAt(i)).elementAt(j)).trim().equalsIgnoreCase("L"))
				  { %><span title="<%=VCOUNTERPARTY_ABBR.elementAt(i) %>">Letter of Agreement&nbsp;</span>
						<% if (!((Vector)VCONT_REF.elementAt(i)).elementAt(j).toString().equals("")){ %>
						[<%=((Vector)VCONT_REF.elementAt(i)).elementAt(j)%>]
						<%}
						  else{%>
							[<%=((Vector)VCONT_NO.elementAt(i)).elementAt(j)%>]
					<%	  }
				  }
				  else if((""+((Vector)VCONTRACT_TYPE.elementAt(i)).elementAt(j)).trim().equalsIgnoreCase("X"))
				  { %><span title="<%=VCOUNTERPARTY_ABBR.elementAt(i) %>">IGX&nbsp;</span>
						<% if (!((Vector)VCONT_REF.elementAt(i)).elementAt(j).toString().equals("")){ %>
						[<%=((Vector)VCONT_REF.elementAt(i)).elementAt(j)%>]
						<%}
						  else{%>
							[<%=((Vector)VCONT_NO.elementAt(i)).elementAt(j)%>]
					<%	  }
				  }
				  else{%>&nbsp;
				<%}%>				  
		        </div></td>
		        <td ><div align="right"><%=((Vector)VTCQ.elementAt(i)).elementAt(j)%>&nbsp;</div></td>
		      
		      <%} %>
		       </tr>
		       <tr>
		       <%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
		         <td ><div align="left"><%if(!(""+((Vector)VTCQ.elementAt(i)).elementAt(j)).trim().equals("")){%>Qty supplied<%}%></div></td>
				 <td><div align="right"><%=((Vector)VQTY_MMBTU.elementAt(i)).elementAt(j)%></div></td>
		       <%} %>
		       </tr>
		       <tr>
		       <%for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
		         <td ><div align="left"><%if(!(""+((Vector)VTCQ.elementAt(i)).elementAt(j)).trim().equals("")){%><b>Remaining Supply</b><%}%></div></td>
				 <td ><div align="right"><b><%=((Vector)VBALANCE.elementAt(i)).elementAt(j)%></b></div></td>
		       <%} %>
		       </tr>
		       <tr>
					<td colspan="<%=((VCOUNTERPARTY_CD.size()*2))%>">&nbsp;</td>
			   </tr>
		       <% } %>
		       <tr>
		  		 <%	for(int i=0;i<VCOUNTERPARTY_CD.size();i++){ %>
					<td><div align="left">&nbsp;<b>Total Balance</b></div></td>
					<td><div align="right"><b><%=VSN_LOA_TOTAL_BALANCE.elementAt(i)%></b>&nbsp;</div></td>
		  		<%}	%>
			  </tr>
			  </table>
			  </div>
			   <div class="row">&nbsp;</div>
			   <div class="row">&nbsp;</div>
			   <div class="row">&nbsp;</div>
			   <div class="row m-b-5">
			    <label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <b>LTCORA</b></label>
	           </div>
			   <div class="row">
			    <div class="table-responsive">
			     <table class="table table-bordered table-hover" width="100%" border="1">
			        <tr>
			 	 		<td><div align="center" ><b>LTCORA</b></div></td>
			 	 		<td><div align="center"><b>MMBTU</b></div></td>
			 	 		<td ><div align="center"><b>MMSCM</b></div></td>
			 	 	</tr>
			 	 	<%for(int i=0;i<VCUSTOMER_CD_LTCORA.size();i++){ %>
			            <tr>
			                <td><div align="left"> Unloaded LTCORA Volumes of <%=VCUSTOMER_NM_LTCORA.elementAt(i)%> Cargo </div></td>
			 	 		    <td><div align="right"><%=LTCORA_TOTAL_UNLOADED_VOLUME.elementAt(i) %></div></td>
			 	 		    <td><div align="right"><%=LTCORA_TOTAL_UNLOADED_VOLUME_MCM.elementAt(i)%></div></td>
			            </tr>
			            <%} %>
			            <tr>
				 	 		<td><div align="left"> SUG Under LTCORA Agreement </div></td>
				 	 		<td><div align="right"><%=total_sug_lt %></div></td>
				 	 		<td><div align="right"><%=total_sug_lt_mcm %></div></td>
			 	 	    </tr>
			 	 	    <tr>
				 	 		<td><div align="left">Net Committed LTCORA Volume</div></td>
				 	 		<td><div align="right"><%= net_commit_lt_vol%></div></td>
				 	 		<td><div align="right"><%=net_commit_lt_vol_mcm %></div></td>
			 	 	    </tr>
			 	 	    <tr>
				 	 		<td><div align="left">LTCORA Volume Expected</div></td>
				 	 		<td><div align="right"><%=total_expect_vol %></div></td>
				 	 		<td><div align="right"><%=total_expect_vol_mcm %></div></td>
			 	 	    </tr>
			 	 	
			 	 	    <tr>
				 	 		<td><div align="left">LTCORA Volume Supplied</div></td>
				 	 		<td><div align="right"><%=total_vol_supplied %></div></td>
				 	 		<td><div align="right"><%=total_vol_supplied_mcm %></div></td>
			 	 	    </tr>
			 	 	    <tr>
				 	 		<td ><div align="left"><b> OutStanding Commitment</b> </div></td>
				 	 		<td ><div align="right"><b><%=total_outstanding_commitment %></b></div></td>
				 	 		<td><div align="right"><b><%=total_outstanding_commitment_mcm %></b></div></td>
			 	 	    </tr>
			 	 	  </table>
			 	 	</div>
			 	 	   
			 	 	<div class="row">&nbsp;</div>
			        <div class="row">&nbsp;</div>
			        <div class="row m-b-5">
			           <label class="form-label subheader"><i class="fa fa-snowflake-o"></i> <b>Send-Out Summary</b></label>
	                </div>
			 	 	<div class="table-responsive">
			        <table class="table table-bordered table-hover" width="100%" border="1">
			        <tr>
			 	 		<td><div align="left"><b>Approximate nos.</b></div></td>
			 	 		<td><div align="center"><b>MMBTU</b></div></td>
			 	 		<td><div align="center"><b>MMSCM</b></div></td>
			 	  	</tr>
			        <tr>
			 	 		<td><div align="left"><b>Opening tank levels on <%=selected_from_date %></b></div></td>
			 	 		<td><div align="right"><%=month_opening_stock%></div></td>
			 	 		<td><div align="right"><%=month_opening_stock_mcm%></div></td>
			 	 	</tr>
			 	 	<tr>
			 	 		<td><div align="left">&nbsp;<b>Volume received during <%=selected_prev_month_year%></b></div></td>
			 	 		<td><div align="right"><%=volume_received_sales%></div></td>
			 	 		<td><div align="right"><%=volume_received_sales_mcm%></div></td>
			 	 	</tr>
			        <tr>
			 	 		<td><div align="left">&nbsp;<b>Sales &amp;Regas Volume in <%=selected_prev_month_year%></b></div></td>
			 	 		<td><div align="right"><%=month_total_sales%></div></td>
			 	 		<td><div align="right"><%=month_total_sales_mcm%></div></td>
			 	 	</tr>
			 	 	<tr>
			 	 		<td><div align="left"><b>Dead stock</b></div></td>
			 	 		<td><div align="right"><%=dead_stock%></div></td>
			 	 		<td><div align="right"><%=dead_stock_mcm%></div></td>
			 	 	</tr> 
			 	 	<tr>
			 	 		<td><div align="left">&nbsp;<b>Expected own volumes</b></div></td>
			 	 		<td><div align="right"><%=volume_expected_sales%></div></td>
			 	 		<td><div align="right"><%=volume_expected_sales_mcm%></div></td>
			 	 	</tr>
			 	 	<tr class="content1">
			 	 		<td><div align="left"><b>Outstanding commitment</b></div></td>
			 	 		<td><div align="right"><%=outstanding_commitment_total%></div></td>
			 	 		<td><div align="right"><%=outstanding_commitment_total_mcm%></div></td>
			 	 	</tr> 
			 	 	<tr>
			 	 		<td><div align="left"><b>Internal consumption <%=consumption_percentage%>%</b></div></td>
			 	 		<td><div align="right"><%=internal_consumption_total%></div></td>
			 	 		<td><div align="right"><%=internal_consumption_total_mcm%></div></td>
			 	 	</tr>
			 	 	<tr><td colspan="3">&nbsp;</td></tr>
			 	 	<tr>
			 	 		<td><div align="left"><b>Net Uncommitted Volumes</b></div></td>
			 	 		<td><div align="right"><b><%=net_uncommited_total%></b></div></td>
			 	 		<td><div align="right"><b><%=net_uncommited_total_mcm%></b></div></td>
			 	 	</tr>
			 	 	<tr><td colspan="3">&nbsp;</td></tr>
			     </table>
			    </div>
			   </div>
	          <% } else { %>
		      <table class="table table-bordered table-hover" width="100%" border="1">
		         <tr>
					<td align="center"colspan="4"><%=utilmsg.infoMessage("<b>...Send-out summary statement List not present for the selected Date...</b>") %></td>
				 </tr>
			  </table>
	          <%} %>
	    </div>
  </body>
</html>