<#function firstDisplayableSubNav subNavs >
	<#list subNavs as subNav>
		<#if subNav.doDisplay>
			<#return subNav/>
		</#if>
	</#list>
</#function>

<#assign systemSubNav= [
	 { "URL":"reparticiones",      "label":"Reparticiones",            "requiresReparticion":false, "doDisplay":account?exists && account.hasPermissions("Manage_Reparticiones", "UPDATE") }
	 <#-- { "URL":"reparticiones",      "label":"Reparticiones",            "requiresReparticion":false, "doDisplay":true } -->
	 
	 
	 
	<#-- ,{ "URL":"admin/prefs/",     "label":"My Account",         "requiresReparticion":false, "doDisplay":true }
	,{ "URL":"admin/mycrsaccount/",     "label":"MyCrs Account",         "requiresReparticion":false, "doDisplay":true }
	,{ "URL":"bookadmin/Create", "label":"Create Reservation", "requiresReparticion":true,
		"doDisplay":account?exists && account.hasPermissions("Create_Reservation", "READ") }
	,{ "URL":"admin/users/",             "label":"Manage Users",     "requiresReparticion":false, 
		"doDisplay":account?exists && account.hasPermissions("USER_ACCOUNT", "UPDATE") }
	,{ "URL":"hotel/admin/",             "label":"Manage Properties","requiresReparticion":false, 
		"doDisplay":account?exists && account.hasPermissions("Manage_Properties", "UPDATE") }
	,{ "URL":"admin/AvailMonitorView",   "label":"System Monitor",   "requiresReparticion":false, 
		"doDisplay":account?exists && account.hasPermissions("SYSTEM_MONITOR", "READ") }
	,{ "URL":"admin/MaintainHotelGroup", "label":"Hotel Groups",     "requiresReparticion":false, 
		"doDisplay":account?exists && account.hasPermissions("HOTEL_GROUP", "UPDATE") }
	,{ "URL":"admin/MaintainUserGroup",  "label":"User Groups",      "requiresReparticion":false, 
		"doDisplay":account?exists && account.hasPermissions("USER_GROUP", "UPDATE") }
	,{ "URL":"hotel/property/getHotelCopyForm","label":"Hotel Copy", "requiresReparticion":true,
		"doDisplay":account?exists && account.hasPermissions("Rooms_Rates", "CREATE") } -->
	] >

<#assign navLinks = [
	{ "label":"Updates",
		"doDisplay":account?exists && account.hasPermissions("Inventory_Management", "READ"),
		"subNav": [
			{ "URL":"dailyupdate/inv/",   "label":"Inventory Overview","requiresReparticion":true, "doDisplay":true },
			{ "URL":"hotel/rateholders/", "label":"Source Management", "requiresReparticion":true,
				"doDisplay":account?exists && account.hasPermissions("Source_Management", "UPDATE")
			},
			{ "URL":"admin/import/getImportForm", "label":"Import", "requiresReparticion":true,
				"doDisplay":account?exists && account.hasPermissions("Inventory_Management", "UPDATE")
			}
		]
	},

	{ "label":"Reparticion",
		"doDisplay":account?exists && account.hasPermissions("Reparticion_Info", "READ"),
		"subNav": [
			{ "URL":"reparticiones/reparticion/showCreditos",   "label":"Resumen Creditos",         "requiresReparticion":true, "doDisplay":account?exists && account.hasPermissions("Reparticion_Info", "UPDATE") },
			{ "URL":"reparticiones/reparticion/showEmpleos",   "label":"Agentes Activos",         "requiresReparticion":true, "doDisplay":account?exists && account.hasPermissions("Reparticion_Info", "UPDATE") },
			{ "URL":"reparticiones/reparticion/showMovimientos/2013",   "label":"Movimientos de Credito",         "requiresReparticion":true, "doDisplay":account?exists && account.hasPermissions("Reparticion_Info", "UPDATE") }
		]
	},

	{ "label":"Rooms/Rates",
		"doDisplay":account?exists && account.hasPermissions("Rooms_Rates", "UPDATE"),
		"subNav": [
			{ "URL":"hotel/rateplans/", "label":"Manage Rates","requiresReparticion":true, "doDisplay":true },
			{ "URL":"hotel/roomtypes/", "label":"Manage Rooms","requiresReparticion":true, "doDisplay":true },
			{ "URL":"hotel/addons/",    "label":"Manage Add Ons","requiresReparticion":true, "doDisplay":true }
		]
	},

	{ "label":"Reports",
		"doDisplay":account?exists && account.hasPermissions("Reports", "READ"),
		"subNav": [
			{ "URL":"report/ReportSetup", "label":"Management Reports","requiresReparticion":true,"doDisplay":true },
			{ "URL":"reportadmin/ReportAdminSetup","label":"Administration Reports","requiresReparticion":true,
				"doDisplay":account?exists && account.hasPermissions("ADMINISTRATION_REPORTS", "READ")},
			<#--{ "URL":"report/resSearch",   "label":"Reservation Production","requiresReparticion":true,"doDisplay":true }, -->
			{ "URL":"admin/export/getExportForm", "label":"Export", "requiresReparticion":true,
				"doDisplay":account?exists && account.hasPermissions("Export_Res_Data", "READ")
			}
		]
	},

	{ "label":"Search",
		"doDisplay":account?exists && (
				   account.hasPermissions("Search_Reservations", "READ")
				|| account.hasPermissions("Search_Hotels", "READ")
				|| account.hasPermissions("TRAVEL_AGENCY", "READ")
				|| account.hasPermissions("Search_Error_Queue", "READ")
				|| account.hasPermissions("Search_Prices", "READ")
				|| account.hasPermissions("Call_Wrap", "READ")
				),
		"subNav": [
			{ "URL":"bookadmin/Find",        "label":"Reservations",    "requiresReparticion":true,
				"doDisplay":account?exists && account.hasPermissions("Search_Reservations", "READ")
			},
			{ "URL":"admin/iata",       "label":"Travel Agencies", "requiresReparticion":false,
				"doDisplay":account?exists && account.hasPermissions("TRAVEL_AGENCY", "READ")
			},
			{ "URL":"bookadmin/QueueSearch", "label":"Error Queue",     "requiresReparticion":true,
				"doDisplay":account?exists && account.hasPermissions("Search_Error_Queue", "READ")
			},
			{ "URL":"admin/audit/prices","label":"Price Log",      "requiresReparticion":true,
				"doDisplay":account?exists && account.hasPermissions("Search_Prices", "READ")
			},
			{ "URL":"bookadmin/call",        "label":"Wrap Call",       "requiresReparticion":false,
				"doDisplay":account?exists && account.hasPermissions("Call_Wrap", "READ")
			}
		]
	}<#--,

	{ "label":"Administracion",
		"doDisplay":account?exists && account.hasPermissions("Manage_Reparticiones", "UPDATE"),
		"subNav": systemSubNav
	}-->

] >


<#assign currPageRequiresReparticion=false >
<#assign subNavLinks = [] >
<#assign tabNum = -1 >
<#assign subNum = -1 >
<#assign debugMatching = "">

<#if requestContext.requestUri?exists>
	<#list navLinks as navLink>
		<#list navLink.subNav as subNav>
			<#assign pagePath = "${requestContext.requestUri?substring(requestContext.contextPath?length)}" >
			<#assign subNavPath = "/${subNav.URL}" >
			<#--assign debugMatching = "${debugMatching}Matching: ${pagePath} : ${subNavPath}<br>"-->
			<#if pagePath?starts_with(subNavPath) >
				<#assign tabNum = navLink_index >
				<#assign subNum = subNav_index >
				<#assign subNavLinks = navLink.subNav >
				<#assign currPageRequiresReparticion=subNav.requiresReparticion >
				<#break>
			</#if>
		</#list>
	
		<#if tabNum != -1 >
			<#break>
		</#if>
	</#list>
</#if>>