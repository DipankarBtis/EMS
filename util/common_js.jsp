<%@page import="com.etrm.fms.util.CommonVariable"%>
<%@ include file="../sess/Expire.jsp" %>
<%String COMMlogo="";
if(session.getAttribute("comp_logo")==null||session.getAttribute("comp_logo")==""||session.getAttribute("comp_logo").toString().equals("null"))
{
	COMMlogo="";
}  
else
{
	COMMlogo=""+session.getAttribute("comp_logo");
}

String sel_comp_cd="";
if(session.getAttribute("comp_cd")==null||session.getAttribute("comp_cd")==""||session.getAttribute("comp_cd").toString().equals("null"))
{
	sel_comp_cd="";
}  
else
{
	sel_comp_cd=""+session.getAttribute("comp_cd");
}
String wmk_comp_abbr="";
if(session.getAttribute("comp_abbr")==null||session.getAttribute("comp_abbr")==""||session.getAttribute("comp_abbr").toString().equals("null"))
{
	wmk_comp_abbr="";
}  
else
{
	wmk_comp_abbr=""+session.getAttribute("comp_abbr");
}
%>
<link rel="SHORTCUT ICON" href="../images/<%=COMMlogo%>">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="refresh" content="<%=session.getMaxInactiveInterval()%>;url=../sess/Expire.jsp?expire_msg=Timeout"/>
<title><%=CommonVariable.app_name%></title>

<link rel="stylesheet" type="text/css" href="../bootstrap/bootstrap5/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="../font-awesome-4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="../css/datepicker/bootstrap-datepicker3.css">
<link rel="stylesheet" type="text/css" href="../css/util.css">
<link rel="stylesheet" type="text/css" href="../css/main.css">
<link rel="stylesheet" type="text/css" href="../css/navbar.css">
<link rel="stylesheet" type="text/css" href="../css/responsive.css">
<link rel="stylesheet" type="text/css" href="../css/common.css">

<% if ("2".equals(sel_comp_cd)) { %>
    <link rel="stylesheet" type="text/css" href="../css/common_2.css">  <!-- CSS for comp_cd = "2" -->
<% } else { %>
    <link rel="stylesheet" type="text/css" href="../css/common.css">  <!-- CSS for comp_cd = "1" -->
<% } %>

<script type="text/javascript" src="../js/jquery.min.js"></script>
<script type="text/javascript" src="../bootstrap/bootstrap5/js/bootstrap.bundle.min.js"></script>
<script type="text/javascript" src="../js/datepicker/bootstrap-datepicker.min.js"></script>
<script type="text/javascript" src="../js/datepicker/bootstrap-datepicker.es.min.js"></script>

<!-- HM20251210 : Bootstrap Searchable Select -->
<link href="../css/serchable_select.css" rel="stylesheet" />
<script src="../js/searchable_select.js"></script>

<script type="text/javascript" src="../js/util.js"></script>
<script type="text/javascript" src="../js/tableUtil.js"></script>
<%--Added by Pratham Bhatt 20240617 for session expire  --%>
<script type="text/javascript" src="../js/session.js"></script>

<%@ include file="common_val.jsp" %>

<script type="text/javascript">
document.title = "<%=CommonVariable.app_name%> | <%=owner_abbr%> | <%=formNm%>";
</script>
<script>
$(function () {
    function initSelect2(container) {
        $(container).find('.searchable-select').each(function () {
            if (!$(this).data('select2')) {
                $(this).select2({
                    theme: 'bootstrap-5',
                    width: '100%',
                    minimumResultsForSearch: 0,
                    dropdownParent: $(this).closest('.modal').length 
                        ? $(this).closest('.modal') 
                        : $('body')
                });
            }
        });
    }

    // Initialize non-modal selects
    initSelect2(document);

    // Initialize selects when ANY modal opens
    $(document).on('shown.bs.modal', function (e) {
        initSelect2(e.target);
    });
});
</script>


<!-- TAB Counter -->
<script>
(function() {
    // Always generate a unique tabId for this tab (even if duplicated)
    const tabId = 'tab_' + crypto.randomUUID();
    sessionStorage.setItem('tabId', tabId);

    const servletURL = '<%=request.getContextPath()%>/TabTrackerServlet';

    // Notify server that tab is opened
    fetch(servletURL, {
        method: 'POST',
        body: new URLSearchParams({ action: 'open', tabId: tabId })
    })
    .then(res => res.json())
    .then(data => {
        const tabCountElem = document.getElementById('tabCount');
        if (tabCountElem) tabCountElem.innerText = data.tabCount;
    });

    // Notify server when tab closes
    window.addEventListener('beforeunload', () => {
        navigator.sendBeacon(servletURL, new URLSearchParams({ action: 'close', tabId: tabId }));
    });

})();
</script>
<script>
//HM20260213:Added this Script to handle In-Active/E-Rate Counterparty actions restriction accross system
//Global variable to track Counterparty status and allow or block edit
var editAllowedOnCpStatus= true;
var max_eff_dt  = "";
var enableButton = false;

function fetchCounterpartyDetails()
{
	// Check if form exists
    if (!document.forms || document.forms.length === 0) {
        return;
    }
    var form = document.forms[0];
    // Check if counterparty_cd field exists in form
    if (!form.counterparty_cd) {
        return;
    }

	var counterparty_cd = document.forms[0].counterparty_cd.value;
	if(counterparty_cd != "" && counterparty_cd!="0")
	{
		$.post("../servlet/DBCounterpartyInfo?counterparty_cd=" + counterparty_cd + "&setCallType=fetchCounterpartyDetails",
		function(responseJson) 
		{
			if (responseJson.length > 0) 
			{
				var counterpartyInfo = responseJson[0].COUNTERPARTY_INFO;
				if (counterpartyInfo.length > 0) 
				{
					var status = counterpartyInfo[0].STATUS;
					max_eff_dt = counterpartyInfo[0].EFF_DT;
					
					if(status == "N")//HM20260217: Removed as per Vijay feedback only for deacive CP edit is disabled || status == "E"
					{
						editAllowedOnCpStatus = false;
					} else {
						editAllowedOnCpStatus = true;
					}
					
					// Gettinh today's system date (without time)
					var today = new Date();
					today.setHours(0,0,0,0);

					var isFutureEffective = false;
					if (max_eff_dt && max_eff_dt !== "") {

						// Split DD/MM/YYYY
						var parts = max_eff_dt.split("/");

						var day   = parseInt(parts[0], 10);
						var month = parseInt(parts[1], 10) - 1;
						var year  = parseInt(parts[2], 10);

						var effDate = new Date(year, month, day);
						effDate.setHours(0,0,0,0);

						if (effDate > today) {
							isFutureEffective = true;
						}
					}
					
					if (editAllowedOnCpStatus === false && !isFutureEffective) {
						//disableAllInputs();
						if(!enableButton)
						{
							disableSubmitButtons();
						}
					}
					
					var alertHtml = "";
					if(status == "N")
					{
						alertHtml = 
						'<div class="row m-b-5 dynamic-alert" >' +
							'<div class="col-sm-3 col-xs-3 col-md-3"></div>' +
							'<div class="col-sm-6 col-xs-6 col-md-6">' +
								'<div class="form-group row">' +
									'<div class="col-sm-12 col-xs-12 col-md-12">' +
										'<div class="container">' +
											'<div class="alert alert-danger">' +
												'<i class="fa fa-exclamation-triangle fa-lg"></i>&nbsp;' +
												'<b>Counterparty Status : Deactive (Effective from : '+ max_eff_dt +')</b>' +
											'</div>' +
										'</div>' +
									'</div>' +
								'</div>' +
							'</div>' +
						'</div>';
					}
					else if(status == "E")
					{
						alertHtml = 
						'<div class="row m-b-5 dynamic-alert" >' +
							'<div class="col-sm-3 col-xs-3 col-md-3"></div>' +
							'<div class="col-sm-6 col-xs-6 col-md-6">' +
								'<div class="form-group row">' +
									'<div class="col-sm-12 col-xs-12 col-md-12">' +
										'<div class="container">' +
											'<div class="alert alert-warning">' +
												'<i class="fa fa-exclamation-triangle fa-lg"></i>&nbsp;' +
												'<b>Counterparty Status : E-Rate (Effective from : '+ max_eff_dt +')</b>' +
											'</div>' +
										'</div>' +
									'</div>' +
								'</div>' +
							'</div>' +
						'</div>';
					}
					// Remove old alert if already added
					$(".dynamic-alert").remove();

					// Insert INSIDE as first element
					$(".card-body.cdbody").first().prepend(alertHtml);
				}
			}
		});
	}
}
$(document).ready(function() {
    fetchCounterpartyDetails();
});

function disableSubmitButtons() {
   $("input[type='button'][value='Submit']")
    .prop("disabled", true);
}

/* function disableAllInputs() {
    var form = document.forms[0];
    if (!form) return;

    $(form).find("input, select, textarea").each(function () {

        // Skip hidden fields
        if (this.tagName.toLowerCase() === "input" && this.type === "hidden") {
            return;
        }

        // Skip buttons (button, submit, reset)
        if (this.tagName.toLowerCase() === "input" && 
            (this.type === "button" || this.type === "submit" || this.type === "reset")) {
            return;
        }

        // Skip <button> elements
        if (this.tagName.toLowerCase() === "button") {
            return;
        }

        this.disabled = true;
    });
} */


//Override the native submit function
(function() {
	const originalSubmit = HTMLFormElement.prototype.submit;
	HTMLFormElement.prototype.submit = function() {
		
		// Gettinh today's system date (without time)
		var today = new Date();
		today.setHours(0,0,0,0);

		var isFutureEffective = false;
		if (max_eff_dt && max_eff_dt !== "") {

			// Split DD/MM/YYYY
			var parts = max_eff_dt.split("/");

			var day   = parseInt(parts[0], 10);
			var month = parseInt(parts[1], 10) - 1;
			var year  = parseInt(parts[2], 10);

			var effDate = new Date(year, month, day);
			effDate.setHours(0,0,0,0);

			if (effDate > today) {
				isFutureEffective = true;
			}
		}
		
		if(!editAllowedOnCpStatus && !isFutureEffective) 
		{
			alert("Submission Blocked! Counterparty Status must be Active!!");
			document.getElementById("loading").style.visibility = "hidden";
			return false; // block submission
		} 
		else
		{
			return originalSubmit.apply(this, arguments); // allow submission
		}
	};
})();

</script>

<!-- Watermark CSS -->
<style type="text/css">
    /* Apply the watermark globally on the body */
    body {
        position: relative;
        height: 100vh; /* Ensuring here that body takes the full viewport height */
        margin: 0; /* Remove default margin */
    }

    body::after {
        content: "<%=wmk_comp_abbr%>";  /* Watermark text */
        font-size: 10vw; /* This is used for viewport width for scalability */
        /*color: var(--header_color); */        /* Header color */
    	color: rgba(0, 0, 0, 0.1);
    	opacity: 0.4;                       /* 40% opacity to watermark */
    	position: fixed;  /* Make it fixed so it stays in place */
        top: 50%;  /* Center vertically */
        left: 50%;  /* Center horizontally */
        transform: translate(-50%, -50%) rotate(-45deg); /* Center it properly and rotate */
        pointer-events: none;  /* Make sure it doesn't block any interactions */
        z-index: 9999;  /* Make sure it's on top */
        white-space: nowrap;
        transform-origin: center;
    }

    /* Make the watermark size smaller on mobile devices */
    @media screen and (max-width: 767px) {
        body::after {
            font-size: 20vw; /* Larger watermark on small screens */
        }
    }

    /* Adjust watermark for tablets or medium devices */
    @media screen and (max-width: 1024px) and (min-width: 768px) {
        body::after {
            font-size: 15vw; /* Moderate size for tablet screens */
        }
    }
    
    /* Adjust watermark for larger screens (Desktops) */
    @media screen and (min-width: 1025px) {
        body::after {
            font-size: 10vw; /* Default size for desktops */
        }
    }
</style>