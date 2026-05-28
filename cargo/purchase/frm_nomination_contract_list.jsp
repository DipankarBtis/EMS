<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>
<script>
function setValue(countpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,st_dt,end_dt,cont_type,cargo_no)
{
	window.opener.setContDetail(countpty_cd,agmt_no,agmt_rev_no,cont_no,cont_rev_no,st_dt,end_dt,cont_type,cargo_no);
	window.close();	
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.purchase.DataBean_Trader_Contract_Mst" id="purchase" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String gas_dt = request.getParameter("gas_dt")==null?"":request.getParameter("gas_dt");
String nomination_freq=request.getParameter("nomination_freq")==null?"W":request.getParameter("nomination_freq");
String nomination_type=request.getParameter("nomination_type")==null?"B":request.getParameter("nomination_type");

purchase.setCallFlag("NOM_TRADER_CONT_LIST");
purchase.setComp_cd(owner_cd);
purchase.setGas_dt(gas_dt);
purchase.setNomination_freq(nomination_freq);
purchase.setNomination_type(nomination_type);
purchase.init();

Vector VBUYER_CD = purchase.getVBUYER_CD();
Vector VBUYER_NAME = purchase.getVBUYER_NAME();
Vector VBUYER_ABBR = purchase.getVBUYER_ABBR();
Vector VCONT_NO = purchase.getVCONT_NO();
Vector VCONTRACT_TYPE_NM = purchase.getVCONTRACT_TYPE_NM();
Vector VCONT_REV_NO = purchase.getVCONT_REV_NO();
Vector VAGMT_NO = purchase.getVAGMT_NO();
Vector VAGMT_REV_NO = purchase.getVAGMT_REV_NO();
Vector VTCQ = purchase.getVTCQ();
Vector VQTY_UNIT = purchase.getVQTY_UNIT();
Vector VSTART_DT = purchase.getVSTART_DT();
Vector VEND_DT = purchase.getVEND_DT();
Vector VCONT_NAME = purchase.getVCONT_NAME();
Vector VCONT_STATUS = purchase.getVCONT_STATUS();
Vector VCONT_STATUS_FLG = purchase.getVCONT_STATUS_FLG();
Vector VFCC_FLAG = purchase.getVFCC_FLAG();
Vector VCONT_REF_NO = purchase.getVCONT_REF_NO();
Vector VCONTRACT_TYPE = purchase.getVCONTRACT_TYPE();
Vector VCARGO_NO = purchase.getVCARGO_NO();
Vector VDISP_CONT_NO = purchase.getVDISP_CONT_NO();
%>
<body>
<form method="post">
<div class="box-body">
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="card cardmain">
				<div class="card-header cdheader">
					 <div class="topheader">
				    	Trader Contract List
				    </div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="col-sm-12 col-xs-12 col-md-12">
							<div class="table-responsive">
								<table class="table table-bordered" id="example">
									<thead>
										<tr valign="top">
											<th>Select</th>
											<th>Counterparty</th>
											<th>Contact#<br><input class="form-control form-control-sm" type="text" id="table_Contact" onkeyup="Search(this,'2');" placeholder="Search.." style="width:100px"/></th>
											<th>Contact Type<br><input class="form-control form-control-sm" type="text" id="table_Contact_type" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></th>
											<th>Contract Rev#</th>
											<th>Contract Ref#<br><input class="form-control form-control-sm" type="text" id="table_Contract_Ref" onkeyup="Search(this,'5');" placeholder="Search.." style="width:100px"/></th>
											<th>Contract Name</th>
											<th>Start - End Date</th>
											<th>TCQ</th>
											<th>Received Qty</th>
											<th>Status<br><input class="form-control form-control-sm" type="text" id="table_Status" onkeyup="Search(this,'10');" placeholder="Search.." style="width:100px"/></th>
										</tr>
									</thead>
									<tbody>
									<%if(VBUYER_CD.size() > 0){ %>
										<%for(int i=0; i<VBUYER_CD.size(); i++){ %>
										<tr>
											<td align="center">
												<input type="radio" <%if(!VCONT_STATUS_FLG.elementAt(i).equals("Y") && !VFCC_FLAG.elementAt(i).equals("Y")){ %>disabled<%} %>
												onclick="setValue('<%=VBUYER_CD.elementAt(i) %>','<%=VAGMT_NO.elementAt(i)%>','<%=VAGMT_REV_NO.elementAt(i)%>',
												'<%=VCONT_NO.elementAt(i)%>','<%=VCONT_REV_NO.elementAt(i)%>','<%=VSTART_DT.elementAt(i)%>',
												'<%=VEND_DT.elementAt(i)%>','<%=VCONTRACT_TYPE.elementAt(i)%>','<%=VCARGO_NO.elementAt(i)%>')">
											</td>
											<td title="<%=VBUYER_ABBR.elementAt(i)%>"><%=VBUYER_NAME.elementAt(i)%></td>
											<td align="center"><%=VDISP_CONT_NO.elementAt(i)%></td>
											<td align="center"><%=VCONTRACT_TYPE_NM.elementAt(i)%></td>
											<td align="center"><%=VCONT_REV_NO.elementAt(i)%></td>
											<%-- <td><%=VBUYER_NAME.elementAt(i) %></td> --%>
											<td><%=VCONT_REF_NO.elementAt(i) %></td>
											<td><%=VCONT_NAME.elementAt(i) %></td>
											<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
											<td align="right"><%=VTCQ.elementAt(i)%>&nbsp;<%=VQTY_UNIT.elementAt(i)%></td>
											<td></td>
											<td align="center"><%=VCONT_STATUS.elementAt(i)%></td>
										</tr>
										<%} %>
									<%}else{ %>
										<tr>
											<td colspan="10" align="center">
												<%=utilmsg.infoMessage("<b>Trader Contact List is not Available!</b>") %>
											</td>
										</tr>
									<%} %>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				<div class="card-footer cdfooter text-center">
					<div class="" align="right">
						<input type="button" class="btn btn-warning com-btn" value="Close" onclick="window.close();">
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</form>
</body>
<script>
function Search(obj, indx) 
{
  	var input, filter, table, tr, td, i, txtValue,count=0;
  	input = document.getElementById(obj.id);
  	
  	filter = input.value.toLocaleLowerCase();
  	table = document.getElementById("example");
  	
  	tr = table.getElementsByTagName("tr");
  	for (i = 1; i < tr.length; i++) 
  	{
    	td = tr[i].getElementsByTagName("td")[indx];
    	//tbody=tr[i].getElementsByTagName("tbody");alert(tbody)
    	if (td) 
    	{
      		txtValue = td.textContent || td.innerText;
      		if (txtValue.toLocaleLowerCase().indexOf(filter) > -1) {
        		tr[i].style.display = "";
        		count++;
      		} else {
      			tr[i].style.display = "none";
      		}
    	}       
  	}
}
</script>
</html>