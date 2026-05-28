<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../util/common_js.jsp"%>

<script>
function setValue(VTAX_STRUCT_CD,VTAX_STRUCT_NM,VTAX_STRUCT_APP_DT)
{
	window.opener.setTaxStructDetail(VTAX_STRUCT_CD,VTAX_STRUCT_NM,VTAX_STRUCT_APP_DT);
	window.close();	
}
</script>
</head>
<jsp:useBean class="com.etrm.fms.master.DataBean_Master" id="dbmaster" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<%
String type=request.getParameter("type")==null?"":request.getParameter("type");

dbmaster.setCallFlag("TAX_STRUCTURE_LIST");
dbmaster.setComp_cd(owner_cd);
dbmaster.setTax_category(type);
dbmaster.setPay_recv("P");
dbmaster.init();

Vector VTAX_STRUCT_CD = dbmaster.getVTAX_STRUCT_CD();
Vector VTAX_STRUCT_NM = dbmaster.getVTAX_STRUCT_NM();
Vector VTAX_STRUCT_APP_DT = dbmaster.getVTAX_STRUCT_APP_DT();
Vector VTAX_STRUCT_STATUS = dbmaster.getVTAX_STRUCT_STATUS();
Vector VTAX_STRUCT_RMK = dbmaster.getVTAX_STRUCT_RMK();
Vector VSAP_TAX_CODE = dbmaster.getVSAP_TAX_CODE();
Vector VSAP_GL = dbmaster.getVSAP_GL();
Vector VPAY_RECV= dbmaster.getVPAY_RECV();
Vector VPAY_RECV_NM= dbmaster.getVPAY_RECV_NM();
Vector VTAX_CATEGORY = dbmaster.getVTAX_CATEGORY();
Vector VTAX_CATEGORY_NM = dbmaster.getVTAX_CATEGORY_NM();
%>
<body>
<form method="post" action="../servlet/Frm_master">

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
					<div class="d-flex justify-content-between">
						<div class="topheader">
							Tax Structure List
						</div>					  	
					</div>
				</div>
				<div class="card-body cdbody">
					<div class="row">
						<div class="table-responsive">
							<table class="table table-bordered table-hover" id="example">
								<thead>
									<tr>
										<th></th>
										<th>Tax Structure<br>(Internal Code)</th>
										<th>Commencement on</th>										
										<th>
											Tax Structure
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_Description" onkeyup="Search(this,'3');" placeholder="Search.." style="width:100px"/></div>	
										</th>
										<th>Tax Category</th>
										<th>SAP Tax Code</th>
										<th>SAP GL</th>
										<th>Status</th>
										<th>Payable / Receivable
											<br><div align="center"><input class="form-control form-control-sm" type="text" id="table_pay_recv" onkeyup="Search(this,'7');" placeholder="Search.." style="width:100px"/></div>											
										</th>
										<th>Remark</th>
									</tr>
								</thead>
								<tbody>
								<%if(VTAX_STRUCT_CD.size() > 0){ %>
									<%for(int i=0; i<VTAX_STRUCT_CD.size(); i++){ %>
									<tr>
										<td width="5%" align="center">
											<div align="center">
												<input type="radio" onclick="setValue('<%=VTAX_STRUCT_CD.elementAt(i)%>','<%=VTAX_STRUCT_NM.elementAt(i)%>','<%=VTAX_STRUCT_APP_DT.elementAt(i)%>');" <%if(VTAX_STRUCT_STATUS.elementAt(i).equals("0")){%>disabled<%} %>>
											</div>
										</td>
										<td align="center"><%=VTAX_STRUCT_CD.elementAt(i)%></td>
										<td align="center"><%=VTAX_STRUCT_APP_DT.elementAt(i)%></td>									
										<td><%=VTAX_STRUCT_NM.elementAt(i)%></td>
										<td><%=VTAX_CATEGORY_NM.elementAt(i)%></td>
										<td align="center"><%=VSAP_TAX_CODE.elementAt(i)%></td>
										<td align="center"><%=VSAP_GL.elementAt(i)%></td>
										<td align="center">
											<div align="center">
												<font style="color:<%if(VTAX_STRUCT_STATUS.elementAt(i).equals("1")){%>#a6ff4d<%}else{%>red<%}%>">
													<i class="fa fa-circle fa-lg" ></i>
													&nbsp;
												</font>
												<%if(VTAX_STRUCT_STATUS.elementAt(i).equals("1")){%>
												Active
												<%}else{ %>
												In-Active
												<%} %>
											</div>
										</td>
										<td align="center"><%=VPAY_RECV_NM.elementAt(i) %></td>										
										<td><%=VTAX_STRUCT_RMK.elementAt(i)%></td>
									</tr>
									<%} %>
								<%}else{ %>
									<tr>
										<td align="center" colspan="9"><%=utilmsg.infoMessage("<b>No Tax Structure is Configured!</b>") %></td>
									</tr>
								<%} %>
								</tbody>
							</table>
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