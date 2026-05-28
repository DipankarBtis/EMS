<%@page import= "java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<jsp:useBean class="com.etrm.fms.purchase.DataBean_Trader_Contract_Mst" id="purchaseSum" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.MessageUtil" id="utilmsg" scope="request"></jsp:useBean>
<jsp:useBean class="com.etrm.fms.util.DateUtil" id="utildate" scope="request"></jsp:useBean>
<%
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
String sysdate=utildate.getSysdate();
String segmentType=request.getParameter("segmentType")==null?"0":request.getParameter("segmentType");
String counterparty_cd=request.getParameter("counterparty_cd")==null?"0":request.getParameter("counterparty_cd");
String from_dt=request.getParameter("from_dt")==null?sysdate:request.getParameter("from_dt");
String to_dt=request.getParameter("to_dt")==null?sysdate:request.getParameter("to_dt");

purchaseSum.setCallFlag("PURCHASE_SUMMARY");
purchaseSum.setComp_cd(owner_cd);
purchaseSum.setSegmentType(segmentType);
purchaseSum.setCounterparty_cd(counterparty_cd);
purchaseSum.setFrom_dt(from_dt);
purchaseSum.setTo_dt(to_dt);
purchaseSum.init();

Vector VCOUNTERPARTY_CD = purchaseSum.getVCOUNTERPARTY_CD();
Vector VCOUNTERPARTY_NM = purchaseSum.getVCOUNTERPARTY_NM();
Vector VCOUNTERPARTY_ABBR = purchaseSum.getVCOUNTERPARTY_ABBR();	
Vector VMST_COUNTERPARTY_CD = purchaseSum.getVMST_COUNTERPARTY_CD();
Vector VMST_COUNTERPARTY_NM = purchaseSum.getVMST_COUNTERPARTY_NM();
Vector VMST_COUNTERPARTY_ABBR = purchaseSum.getVMST_COUNTERPARTY_ABBR();
Vector VCONT_NO = purchaseSum.getVCONT_NO();	
Vector VCONT_REV_NO = purchaseSum.getVCONT_REV_NO();
Vector VCONT_NAME = purchaseSum.getVCONT_NAME();	
Vector VSTART_DT = purchaseSum.getVSTART_DT();
Vector VEND_DT = purchaseSum.getVEND_DT();
Vector VRATE = purchaseSum.getVRATE();
Vector VRATE_UNIT = purchaseSum.getVRATE_UNIT();
Vector VCONT_STATUS = purchaseSum.getVCONT_STATUS();
Vector VCONT_STATUS_FLG = purchaseSum.getVCONT_STATUS_FLG();	
Vector VPRICE_TYPE = purchaseSum.getVPRICE_TYPE();
Vector VBOOKED_QTY = purchaseSum.getVBOOKED_QTY();
Vector VAGMT_NO = purchaseSum.getVAGMT_NO();		
Vector VAGMT_REV_NO = purchaseSum.getVAGMT_REV_NO();	
Vector VCONTRACT_TYPE = purchaseSum.getVCONTRACT_TYPE();
Vector VMIN_ALLOC_DT = purchaseSum.getVMIN_ALLOC_DT();
Vector VMAX_ALLOC_DT = purchaseSum.getVMAX_ALLOC_DT();
Vector VUNLOADED_QTY = purchaseSum.getVUNLOADED_QTY();
Vector VAVAILABLE_FOR_SALE = purchaseSum.getVAVAILABLE_FOR_SALE();	
Vector VBU_POINT=purchaseSum.getVBU_POINT();
Vector VTRADER_PLANT=purchaseSum.getVTRADER_PLANT();
Vector VCONT_REF_NO=purchaseSum.getVCONT_REF_NO();

Vector VDISPLAY_SEGMENT = purchaseSum.getVDISPLAY_SEGMENT();
Vector VTEMP_SEGMENT_TYPE = purchaseSum.getVTEMP_SEGMENT_TYPE();
Vector VSEGMENT_TYPE = purchaseSum.getVSEGMENT_TYPE();		
Vector VDISPLAY_SEGMENT_TYP = purchaseSum.getVDISPLAY_SEGMENT_TYP();
Vector VINDEX = purchaseSum.getVINDEX();

//Added by Pratham Bhatt for Cargo name
Vector VCARGO_NAME = purchaseSum.getVCARGO_NAME();
Vector VBALANCE_QTY = purchaseSum.getVBALANCE_QTY();		
Vector VBAL_INFO = purchaseSum.getVBAL_INFO();
Vector VBOOKED_INFO = purchaseSum.getVBOOKED_INFO();
Vector VTOTAL_QUANTITY = purchaseSum.getVTOTAL_QUANTITY();
Vector VTOTAL_UNLOADED_QUANTITY = purchaseSum.getVTOTAL_UNLOADED_QUANTITY();
Vector VTOTAL_BALANCE = purchaseSum.getVTOTAL_BALANCE();

Vector VCOUNTERPARTY_STATUS = purchaseSum.getVCOUNTERPARTY_STATUS();
Vector VSTATUS_EFF_DT = purchaseSum.getVSTATUS_EFF_DT();

double totalQty=purchaseSum.getTotalQty();		
double UnloadedtotalQty=purchaseSum.getUnloadedtotalQty();	
double AvailabletotalQty=purchaseSum.getAvailabletotalQty();	

String balTotalQty_str = purchaseSum.getBalTotalQty_str();
String bookedToolTip = purchaseSum.getBookedToolTip();
%>

<body>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition","inline; filename="+fileName);
%>
	<table  width="100%" border="1">
		<tr>
			<th colspan="16" rowspan="2" align="left">Purchase Summary</th>
		</tr>
	</table >
	<%int i=0;int k=0; int m = 0;
					for(int j=0; j<VTEMP_SEGMENT_TYPE.size(); j++){ 
						int index = Integer.parseInt(""+VINDEX.elementAt(j));
						String seg_type="";
						if(VTEMP_SEGMENT_TYPE.elementAt(j).equals("D")){
							seg_type="Domestic NG Purchase";
						}
						else if(VTEMP_SEGMENT_TYPE.elementAt(j).equals("T"))
						{
							seg_type="In Tank LNG/RLNG Purchase";
						}
						else if(VTEMP_SEGMENT_TYPE.elementAt(j).equals("I"))
						{
							seg_type="IGX Purchase";
						}
						else if(VTEMP_SEGMENT_TYPE.elementAt(j).equals("N"))
						{
							seg_type="LNG Cargo";
						}
						else if(VTEMP_SEGMENT_TYPE.elementAt(j).equals("L"))
						{
							seg_type="LTCORA";
						}
						if(VTEMP_SEGMENT_TYPE.size()==1)
						{
							if(segmentType.equals("D"))
							{
								m=0;
							}
							else if(segmentType.equals("T"))
							{
								m=1;
							}
							else if(segmentType.equals("I"))
							{
								m=2;
							}
							else if(segmentType.equals("N"))
							{
								m=3;
							}
							else if(segmentType.equals("L"))
							{
								m=4;
							}
						}
						else
						{
							m=j;
						}
					if(j!=0)
					{%>
						&nbsp;
					<%} %>

							<table width="100%" border="1">
								<thead>
									<tr bgcolor="gold">
											<th align="left" colspan="16"><%=VDISPLAY_SEGMENT.elementAt(m) %></th>
									</tr>
									<tr>
										<th>Sr#</th>
										<th>Counterparty</th>
										<%if(VDISPLAY_SEGMENT_TYP.elementAt(m).equals("N") || VDISPLAY_SEGMENT_TYP.elementAt(m).equals("L")){%>
										<th>Cargo#</th>
											<%if( VDISPLAY_SEGMENT_TYP.elementAt(m).equals("L")){%>
											<th>Cont Ref#</th>
											<th>Cargo Arrival - Storage Window</th>
											<%}else{ %>
											<th>Cargo Ref#</th>
											<th>Cargo Arrival Window</th>
											<%} %>
										<%}else{ %>
											<th>Contract#</th>
												<%if(VDISPLAY_SEGMENT_TYP.elementAt(m).equals("I")){ %>
													<th>Trade Ref#</th>
												<%}else{ %>
													<th>Cont Ref#</th>
												<%} %>
											<th>Contract Period</th>
										<%} %>
										
										<th>Status</th>
										<th >Price Type</th>
										<th >Price</th>
										<th >Currency/MMBTU</th>
										<th >Business Unit</th>
										<th >Trader Plants</th>
										<%if(VDISPLAY_SEGMENT_TYP.elementAt(m).equals("N")){ %>
										<th>Actual Arrival Date</th>
										<th>Q&Q Certificate Date</th>
										<%}else{%>
											<th >Allocation Start Date</th>
											<th >Last Allocation Date</th>
										<%} %>
										<th <%if(VDISPLAY_SEGMENT_TYP.elementAt(m).equals("L")){%>title="<%=bookedToolTip%>"<%} %>>MMBTU Booked</th>
										<%if(VDISPLAY_SEGMENT_TYP.elementAt(m).equals("L")){ %>
										<th title="Allocated Quantity">Regassified MMBTU </th>
										<th title="Booked Qty = Booked MMBTU - Regassified Qty"> Balance MMBTU </th>
										<%}else{ %>
										<th >MMBTU Unloaded</th>
										<th title="Booked Qty = Booked MMBTU - MMBTU Unloaded"> Balance MMBTU </th>
										<%} %>
									</tr>
								</thead>
								<tbody>
								<%
								k=0;
								if(index > 0){ %>
									<%for(i=i;i<VCOUNTERPARTY_CD.size(); i++){ 
										k+=1;
									%>
										<tr>
											<td align="center"><%=k%></td>
											<td title="<%=VCOUNTERPARTY_CD.elementAt(i)%>">
												<%=VCOUNTERPARTY_NM.elementAt(i)%>
												<span
												<%if(VCOUNTERPARTY_STATUS.elementAt(i).equals("N")){ %>class='alert alert-danger' 
												<%}else if(VCOUNTERPARTY_STATUS.elementAt(i).equals("E")){ %>class='alert alert-warning' 
												<%} %>
												><b><%if(VCOUNTERPARTY_STATUS.elementAt(i).equals("N")){ %>De-active
												<%}else if(VCOUNTERPARTY_STATUS.elementAt(i).equals("E")){ %>E-Rate
												<%} %></b></span>
											</td>
											<%if(VDISPLAY_SEGMENT_TYP.elementAt(m).equals("N")){%>
											<td align="center" title="REV : <%=VCONT_REV_NO.elementAt(i)%>"><%=VCARGO_NAME.elementAt(k-1)%></td>
											<%}else if (VDISPLAY_SEGMENT_TYP.elementAt(m).equals("L")){%>
											<td align="center" title="REV : <%=VCONT_REV_NO.elementAt(i)%>"><%=VCONTRACT_TYPE.elementAt(i)%></td>
											<%}else{ %>
											<td align="center" title="REV : <%=VCONT_REV_NO.elementAt(i)%>"><%=VCONTRACT_TYPE.elementAt(i)%></td>
											<%-- <td align="center" title="REV : <%=VCONT_REV_NO.elementAt(i)%>"><%=VCONTRACT_TYPE.elementAt(i)%><%=VCONT_NO.elementAt(i)%></td> --%>
											<%} %>
											<td style="mso-number-format:'\@';"><%=VCONT_REF_NO.elementAt(i)%></td>
											<td align="center"><%=VSTART_DT.elementAt(i)%> - <%=VEND_DT.elementAt(i)%></td>
											<td align="center">
												<%if(VCONT_STATUS.elementAt(i).equals("Cancelled")){%>
												<font color="red"><%=VCONT_STATUS.elementAt(i)%></font>
												<%}else if(VCONT_STATUS.elementAt(i).equals("Confirmed")){%>
												<font color="green"><%=VCONT_STATUS.elementAt(i)%></font>
												<%}else{ %>
												<%=VCONT_STATUS.elementAt(i)%>
												<%} %>
											</td>
											<td align="center"><%=VPRICE_TYPE.elementAt(i)%></td>
											<td align="right"><%=VRATE.elementAt(i)%></td>
											<td align="center"><%=VRATE_UNIT.elementAt(i)%></td>
											<td align="center"><%=VBU_POINT.elementAt(i)%></td>
											<td align="center"><%=VTRADER_PLANT.elementAt(i)%></td>
											<td align="center"><%=VMIN_ALLOC_DT.elementAt(i) %></td>
											<td align="center"><%=VMAX_ALLOC_DT.elementAt(i) %></td>
											<td align="right" <%if(VDISPLAY_SEGMENT_TYP.elementAt(m).equals("L")){%>title="Booked MMBTU = <%=VBOOKED_INFO.elementAt(k-1)%>"<%} %>><%=VBOOKED_QTY.elementAt(i)%></td>
											<td align="right"><%=VUNLOADED_QTY.elementAt(i)%></td>
<%-- 											<%if(VDISPLAY_SEGMENT_TYP.elementAt(m).equals("L")){%>  --%>
											<td align="right" title="Balance Qty = <%=VBAL_INFO.elementAt(i)%>"><%=VBALANCE_QTY.elementAt(i)%></td>
<%-- 											<%}%>  --%>
										</tr>
											
										<%
											if(k==index)
											{
												i=i+1;
												break;
											}
											%>
									<%}%>
									<tbody>
									<tr>
										<td colspan="13" align="right">Total :</td>
										<td align="right"><%=VTOTAL_QUANTITY.elementAt(j) %></td>
										<td align="right"><%=VTOTAL_UNLOADED_QUANTITY.elementAt(j) %></td>
										<%-- <%if(VDISPLAY_SEGMENT_TYP.elementAt(m).equals("L")){ %> --%>
										<td><%=VTOTAL_BALANCE.elementAt(j)%></td>
										<%-- <%}%> --%>
										<%--<td align="right"><%=UnloadedtotalQty%></td> --%>
										<%-- <td align="right"><%=AvailabletotalQty%></td>--%>
									</tr>
								</tbody>
								<%}else{ %>
									<tr>
										<%if(VDISPLAY_SEGMENT_TYP.elementAt(m).equals("L")){ %>
										<td colspan="16" align="center"><%=utilmsg.infoMessage("<b>No Contract is Available!</b>") %></td>
										<%}else{ %>
										<td colspan="16" align="center"><%=utilmsg.infoMessage("<b>No Contract is Available!</b>") %></td>
										<%}%>
									</tr>
								<%} %>
								</tbody>
								 
							</table>
							<%} %>
</body>
</html>