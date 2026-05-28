<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script>
/* function refresh() 
{
    var from_dt = document.forms[0].from_dt.value;
    var to_dt = document.forms[0].to_dt.value;
    var temp_from_dt = document.forms[0].temp_from_dt.value;
    var temp_to_dt = document.forms[0].temp_to_dt.value;
    var counterparty_cd = document.forms[0].counterparty_cd.value;
    var plant_seq = document.forms[0].plant_seq.value;
    var deal_no=document.forms[0].deal_no.value;
    var bu_plant_seq = document.forms[0].bu_plant_seq.value;
    var temp_counterparty_cd = document.forms[0].temp_counterparty_cd.value;
    var temp_deal_no = document.forms[0].temp_deal_no.value;
    
    if(temp_counterparty_cd != counterparty_cd && temp_deal_no == deal_no )
    {
    	deal_no = "0";
    }
    else
    {
    	deal_no = document.forms[0].deal_no.value;
    }
    
    var flag = true;
    var msg = "";
    
    if (from_dt != null && to_dt != null) 
    {
        if (trim(from_dt) != "" && trim(to_dt) != "") 
        {

            var partsFrom = from_dt.split("/");
            var partsTo = to_dt.split("/");

            var fromDate = new Date(partsFrom[2], partsFrom[1] - 1, partsFrom[0]);
            var toDate = new Date(partsTo[2], partsTo[1] - 1, partsTo[0]);

            if (toDate < fromDate) 
            {
                alert("To Date cannot be earlier than From Date.");
                flag =  false;
            }

            var diffMs = toDate - fromDate;
            var diffDays = diffMs / (1000 * 60 * 60 * 24);

            if (diffDays > 185) 
            {
                alert("Date range should not exceed 185 days.");

                flag =  false;
            }
            var u = document.forms[0].u.value;

            if (flag) 
            {
            	var url = "rpt_dlng_customer_allocation_contract_plant_wise.jsp?u="+u+"&from_dt="+from_dt+"&to_dt="+to_dt+"&counterparty_cd="+counterparty_cd+"&deal_no="+deal_no+
            	"&bu_plant_seq="+bu_plant_seq+"&plant_seq="+plant_seq;
                document.getElementById("loading").style.visibility = "visible";
                location.replace(url);
            }

        } 
        else 
        {
            alert("Please Select From and To Dates...");
            document.forms[0].from_dt.value = temp_from_dt;
            document.forms[0].to_dt.value = temp_to_dt;
            flag =  false;
        }
    }
   
} */

function refresh() 
{
	var from_dt = document.forms[0].from_dt.value;
    var to_dt = document.forms[0].to_dt.value;
    var temp_from_dt = document.forms[0].temp_from_dt.value;
    var temp_to_dt = document.forms[0].temp_to_dt.value;
    var counterparty_cd = document.forms[0].counterparty_cd.value;
    var plant_seq = document.forms[0].plant_seq.value;
    var deal_no=document.forms[0].deal_no.value;
    var bu_plant_seq = document.forms[0].bu_plant_seq.value;
    var temp_counterparty_cd = document.forms[0].temp_counterparty_cd.value;
    var temp_deal_no = document.forms[0].temp_deal_no.value;
    
    if(temp_counterparty_cd != counterparty_cd && temp_deal_no == deal_no )
    {
    	deal_no = "0";
    }
    else
    {
    	deal_no = document.forms[0].deal_no.value;
    }
    var flag=checkDateRangeOnApply(document.forms[0].from_dt,document.forms[0].to_dt);
    var u = document.forms[0].u.value;

    if(flag)
   	{
   		if(trim(from_dt)!="" && trim(to_dt)!="")
   		{
   			if (flag==true) 
         {
                var url = "rpt_dlng_customer_allocation_contract_plant_wise.jsp?u="+u+"&from_dt="+from_dt+"&to_dt="+to_dt+"&counterparty_cd="+counterparty_cd+"&deal_no="+deal_no+
            	"&bu_plant_seq="+bu_plant_seq+"&plant_seq="+plant_seq;
                document.getElementById("loading").style.visibility = "visible";
                location.replace(url);
         }
   	   }
   	}
}
function exportToXls()
{
	var from_dt = document.forms[0].from_dt.value;
    var to_dt = document.forms[0].to_dt.value;
    var temp_from_dt = document.forms[0].temp_from_dt.value;
    var temp_to_dt = document.forms[0].temp_to_dt.value;
    var counterparty_cd = document.forms[0].counterparty_cd.value;
    var plant_seq = document.forms[0].plant_seq.value;
    var deal_no=document.forms[0].deal_no.value;
    var bu_plant_seq = document.forms[0].bu_plant_seq.value;
    var temp_counterparty_cd = document.forms[0].temp_counterparty_cd.value;
    var temp_deal_no = document.forms[0].temp_deal_no.value;
    var owner_abbr = document.forms[0].owner_abbr.value;
    
    if(temp_counterparty_cd != counterparty_cd && temp_deal_no == deal_no)
    {
    	deal_no = "0";
    }
    else
    {
    	deal_no = document.forms[0].deal_no.value;
    } 
    var filename = owner_abbr+"_"+"DLNG_Customer Allocation (By Contract And Plant)- "+from_dt+"To"+to_dt+".xls";
	var url = "xls_dlng_customer_allocation_contract_plant_wise.jsp?fileName="+filename+"&from_dt="+from_dt+"&to_dt="+to_dt+"&counterparty_cd="+counterparty_cd+"&deal_no="+deal_no+
	"&bu_plant_seq="+bu_plant_seq+"&plant_seq="+plant_seq;
	
	location.replace(url);
} 
</script>
<%@ include file="../util/common_js.jsp"%>
<jsp:useBean class="com.etrm.fms.dlng.DB_DLNG_Report" id="dlng" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
String sysdate=utildate.getSysdate();

String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String deal_no=request.getParameter("deal_no")==null?"0":request.getParameter("deal_no");
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");
String bu_plant_seq=request.getParameter("bu_plant_seq")==null?"0":request.getParameter("bu_plant_seq");
String plant_seq=request.getParameter("plant_seq")==null?"0":request.getParameter("plant_seq");
String agmt_no = "";
String cont_no="";
String cargo_no="";
String contract_type="";
if (deal_no != null && !deal_no.trim().equals("0") && !deal_no.trim().isEmpty()) 
{
     String[] parts = deal_no.split(":");
     agmt_no   = parts[0];
     cont_no   = parts[1];
     contract_type = parts[2];
     cargo_no = parts[3];
}

dlng.setCallFlag("DLNG_ALLOC_TO_CUSTOMER_PLANT_CONTRACT_WISE");
dlng.setComp_cd(owner_cd);
dlng.setFrom_dt(from_dt);
dlng.setTo_dt(to_dt);
dlng.setCounterparty_cd(counterparty_cd);
dlng.setBu_plant_seq(bu_plant_seq);
dlng.setPlant_seq(plant_seq);
dlng.setAgmt_no(agmt_no);
dlng.setCont_no(cont_no);
dlng.setContract_type(contract_type);
dlng.setDeal_no(deal_no);
dlng.setCargo_no(cargo_no);
dlng.init();

Vector VMST_COUNTERPARTY_CD = dlng.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = dlng.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = dlng.getVMST_COUNTERPARTY_ABBR();
Vector VBU_PLANT_ABBR=dlng.getVBU_PLANT_ABBR();
Vector VBU_PLANT_SEQ=dlng.getVBU_PLANT_SEQ();
Vector VDIS_CONT_MAPPING=dlng.getVDIS_CONT_MAPPING();
Vector VCONT_REF=dlng.getVCONT_REF();
Vector VCOUNTERPARTY_CD = dlng.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_ABBR = dlng.getVCOUNTERPARTY_ABBR();
Vector VCOUNTERPARTY_NM = dlng.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_PLANT_SEQ = dlng.getVCOUNTERPARTY_PLANT_SEQ();
Vector VCOUNTERPARTY_PLANT_NM = dlng.getVCOUNTERPARTY_PLANT_NM();

Vector VSEGMENT = dlng.getVSEGMENT();
Vector VSEGMENT_TYPE = dlng.getVSEGMENT_TYPE();
Vector VINDEX = dlng.getVINDEX();
Vector VCONT_NO = dlng.getVCONT_NO();

Vector VGAS_DT = dlng.getVGAS_DT();
Vector VQTY_MMBTU = dlng.getVQTY_MMBTU();
Vector VQTY_SCM = dlng.getVQTY_SCM();
Vector VTOTAL_QTY_MMBTU = dlng.getVTOTAL_QTY_MMBTU();
Vector VTOTAL_QTY_SCM = dlng.getVTOTAL_QTY_SCM();

Vector VPLANT_NM = dlng.getVPLANT_NM();
Vector VPLANT_SEQ_NO = dlng.getVPLANT_SEQ_NO();
Vector VCONTRACT_TYPE = dlng.getVCONTRACT_TYPE();
Vector VAGMT_NO = dlng.getVAGMT_NO();
Vector VCOLOR = dlng.getVCOLOR();
Vector VCARGO_NO = dlng.getVCARGO_NO();

String deal_map = dlng.getDeal_map();
String cont_ref_no = dlng.getCont_ref_no();
int temp_count = dlng.getTemp_count();

%>
<body>
<%@ include file="../home/header.jsp"%>
<form action="">
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
				    		DLNG Customer Allocation (By Contract And Plant)
	   	 				</div>
	   	 				<div>
							<div class="btn-group" onclick="exportToXls();">
								<label><i class="fa fa-file-excel-o fa-2x excel_icon"></i></label>
							</div>
						</div>
				    </div>
				</div>
				<div class="card-body cdbody">
				<div class="row m-b-5">
				  <div class="col-md-2 col-sm-0 col-xs-0"></div>
					<div class="col-md-4 col-sm-6 col-xs-6">
						<div class="form-group row">
							<div class="col-auto">
								<label class="form-label"><b>From</b></label>
							</div>
						<div class="col-auto">
			      	     <div class="input-group input-group-sm" >
     						<input type="text" class="form-control form-control-sm date fmsdtpick" name="from_dt" value="<%=from_dt%>" size="8" maxLength="10" 
     						onblur="validateDate(this);" autocomplete="off">
     						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
	      			     </div>
				    	</div>
						<div class="col-auto">
							<label class="form-label"><b>To</b></label>
						</div>
						<div class="col-auto">
		      			  <div class="input-group input-group-sm" >
	     						<input type="text" class="form-control form-control-sm date fmsdtpick" name="to_dt" value="<%=to_dt%>" size="8" maxLength="10" onblur="validateDate(this);" autocomplete="off">
	     						<span class="input-group-text"><i class="fa fa-calendar fa-lg"></i></span>
	     				  </div>
					    </div>
					  </div>
					</div>
					<div class="col-md-4 col-sm-6 col-xs-6">
					  <div class="form-group row">
						<div class="col-auto">
							<label class="form-label"><b>Customer</b></label>
						</div>
						<div class="col-auto">
							<select class="form-select form-select-sm" name="counterparty_cd" >
								<option value="0">--Select--</option>
								<%for(int i=0;i<VMST_COUNTERPARTY_CD.size();i++){ %>
								<option value="<%=VMST_COUNTERPARTY_CD.elementAt(i)%>"><%=VMST_COUNTERPARTY_ABBR.elementAt(i)%> - <%=VMST_COUNTERPARTY_NM.elementAt(i) %></option>
								<%} %>
							</select>
							<script>document.forms[0].counterparty_cd.value="<%=counterparty_cd%>"</script>
					   </div>
					</div>
				  </div>
				  <div class="col-md-2 col-sm-0 col-xs-0"></div>
				</div>
				<div class="row m-b-5">
					<div class="col-md-3 col-sm-6 col-xs-6">
						<div class="form-group row">
							<div class="col-auto">
								<label class="form-label"><b>Contract#</b></label>
				  			</div>
				  			<div class="col">
				  				<select class="form-select form-select-sm" name="deal_no">
									<option value="0">--All--</option>
									<%for(int i=0;i<VDIS_CONT_MAPPING.size();i++){ %>
									<option value="<%=VAGMT_NO.elementAt(i)%>:<%=VCONT_NO.elementAt(i)%>:<%=VCONTRACT_TYPE.elementAt(i)%>:<%=VCARGO_NO.elementAt(i)%>">
									<%=VDIS_CONT_MAPPING.elementAt(i)%>-[<%=VCONT_REF.elementAt(i)%>]</option>
									<%} %>
						        </select>
								<script>
								    <% if(agmt_no != null && !agmt_no.equals("")) { %>
									   document.forms[0].deal_no.value="<%=agmt_no%>:<%=cont_no%>:<%=contract_type%>:<%=cargo_no%>"
									<% } %>
								</script>
				  			</div>
				  		</div>
				  	</div>
				  	<div class="col-md-3 col-sm-6 col-xs-6">
						<div class="form-group row">
							<div class="col-auto">
								<label class="form-label"><b>BU Unit</b></label>
				  			</div>
				  			<div class="col">
				  				<select class="form-select form-select-sm" name="bu_plant_seq" >
									<option value="0">--All--</option>
									  <%for(int i=0; i<VBU_PLANT_SEQ.size(); i++){ %>
									<option value="<%=VBU_PLANT_SEQ.elementAt(i)%>"><%=VBU_PLANT_ABBR.elementAt(i)%></option>
									<%} %> 
								</select>
						        <script>document.forms[0].bu_plant_seq.value="<%=bu_plant_seq%>"</script> 
				  			</div>
				  		</div>
				  	</div>
				  	<div class="col-md-3 col-sm-6 col-xs-6">
						<div class="form-group row">
							<div class="col-auto">
								<label class="form-label"><b>Plant</b></label>
				  			</div>
				  			<div class="col">
						  		<select class="form-select form-select-sm" name="plant_seq" >
								<option value="0">--All--</option>
								  <%for(int i=0; i<VPLANT_SEQ_NO.size(); i++){ %>
								<option value="<%=VPLANT_SEQ_NO.elementAt(i)%>"><%=VPLANT_NM.elementAt(i)%></option>
								<%} %> 
								</select>
							   <script>document.forms[0].plant_seq.value="<%=plant_seq%>"</script>
				  		  </div>
				  		</div>
				  	</div>
				  	<div class="col-md-3 col-sm-6 col-xs-6">
						<div class="form-group row">
							<div class="col-auto">
								<input type="button" class="btn btn-warning com-btn" value="Apply Filtters" onclick="refresh();">
				  			</div>
				  		</div>
				  	</div>
				</div>
                </div>
				<div class="card-body cdbody">
				 <%if(VMST_COUNTERPARTY_CD.size()>0){ %>
				 <%if(!counterparty_cd.equals("0") && !counterparty_cd.equals("")){ %>
				 <%int k=0,l=0,p=0; %>
						<div class="row">
							<div class="col-md-12 col-sm-12 col-xs-12">
							<%
							for(int i=0;i<temp_count;i++){ 
								int index=0;
								 index=Integer.parseInt(""+VINDEX.elementAt(i));
								int plant_size=((Vector) VCOUNTERPARTY_PLANT_SEQ.elementAt(i)).size();
							%>
								<div class="accordion">
									<div class="accordion-item accor_item">
										<h2 class="accordion-header" id="heading<%=i%>">
											<button class="accordion-button collapsed accor-btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%=i%>" aria-expanded="false" aria-controls="collapse<%=i%>">
								    			<% if(deal_no.equals("0")){ %>
								    			<%=VCOUNTERPARTY_ABBR.elementAt(i)%>&nbsp;-&nbsp;<%=VCOUNTERPARTY_NM.elementAt(i)%>&nbsp;&nbsp;(<%=VDIS_CONT_MAPPING.elementAt(i)%>-[<%=VCONT_REF.elementAt(i)%>])
								    			<% } else { %>
								    			   <%=VCOUNTERPARTY_ABBR.elementAt(i)%>&nbsp;-&nbsp;<%=VCOUNTERPARTY_NM.elementAt(i)%>&nbsp;&nbsp;(<%=deal_map%>-[<%=cont_ref_no%>])
								    			<% } %>
								      		</button>
								    	</h2>
								    	<div id="collapse<%=i%>" class="accordion-collapse collapse" aria-labelledby="heading<%=i%>">
								      		<div class="accordion-body accor-body">
								      			<div class="row">
													<div class="table-responsive">
														<table class="table table-bordered table-hover">
															<thead>
																<tr>
																	<th rowspan="3">Gas Day</th>
																	<th rowspan="2" colspan="2">Total Quantity Supplied</th>
																	<th colspan="<%=plant_size*2%>">Total Quantity Supplied To Plant</th>
																</tr>
																<tr>
																	<%for(int j=0;j<plant_size;j++){ %>
																	<th colspan="2"><%=((Vector) VCOUNTERPARTY_PLANT_NM.elementAt(i)).elementAt(j)%></th>
																	<%} %>
																</tr>
																<tr>
																	<th>MMBTU</th>
																	<th>SCM</th>
																	<%for(int j=0;j<plant_size;j++){ %>
																	<th>MMBTU</th>
																	<th>SCM</th>
																	<%} %>
																</tr>
															</thead>
															<tbody>
															<%int n=0;
															for(k=k;k<VGAS_DT.size();k++){
																n+=1;
															%>
																<tr>
																	<td align="center"><%=VGAS_DT.elementAt(k)%></td>
																	<%int m=0;
																	for(l=l;l<VQTY_MMBTU.size();l++){ 
																		m+=1;
																	%>
																		<td align="right" style="background:<%=VCOLOR.elementAt(l)%>;"><%=VQTY_MMBTU.elementAt(l)%></td>
																		<td align="right" style="background:<%=VCOLOR.elementAt(l)%>;"><%=VQTY_SCM.elementAt(l)%></td>
																		<%if((plant_size+1) == m){
																			l++;
																			break;
																		} %>
																	<%} %>
																</tr>
																<%if(index == n){ %>
																<tr style="font-weight:bold;">
																	<td align="right">Total&nbsp;:&nbsp;</td>
																	<%int o=0;
																	for(p=p;p<VTOTAL_QTY_MMBTU.size();p++){ 
																		o+=1;
																	%>
																		<td align="right"><%=VTOTAL_QTY_MMBTU.elementAt(p)%></td>
																		<td align="right"><%=VTOTAL_QTY_SCM.elementAt(p)%></td>
																		<%if((plant_size+1) == o){
																			p++;
																			break;
																		} %>
																	<%} %>
																</tr>
																	<%k=k+1;
																	break;
																} %>
															<%} %>
															</tbody>
														</table>
													</div>
												</div>
								      		</div>
								      	</div>
									</div>
								</div>
							<%} %>
						   </div>
						</div>
						<% }else{ %>
						    <div align="center">
								<%=utilmsg.infoMessage("<b>Please Select Csutomer</b>")%>
							</div>
						<%} %>
						<% }else{ %>
							<div align="center">
								<%=utilmsg.infoMessage("<b>Allocation not Done for the Selected Date Range!</b>")%>
							</div>
						<%} %>
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
<input type="hidden" name="temp_to_dt" value="<%=to_dt%>">
<input type="hidden" name="temp_from_dt" value="<%=from_dt%>">
<input type="hidden" name="temp_counterparty_cd" value="<%=counterparty_cd%>">
<input type="hidden" name="temp_deal_no" value="<%=deal_no%>">
<input type="hidden" name="u" value="<%=u%>">
<input type="hidden" name="owner_abbr" value="<%=owner_abbr%>">

</form>
</body>
</html>