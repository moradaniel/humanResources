<#function firstDisplayableSubNav subNavs >
	<#list subNavs as subNav>
		<#if subNav.doDisplay>
			<#return subNav/>
		</#if>
	</#list>
</#function>

<#assign systemSubNav= [
	 { "URL":"departments",      "label":"Reparticiones",            "requiresDepartment":false, "doDisplay":account?exists && account.hasPermissions("Manage_Departments", "READ") }
	 <#-- { "URL":"reparticiones",      "label":"Reparticiones",            "requiresReparticion":false, "doDisplay":true } -->
	 
	 
	 
	<#-- ,{ "URL":"admin/prefs/",     "label":"My Account",         "requiresReparticion":false, "doDisplay":true }
	,{ "URL":"admin/account/",     "label":"Account",         "requiresReparticion":false, "doDisplay":true }
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
			{ "URL":"dailyupdate/inv/",   "label":"Inventory Overview","requiresDepartment":true, "doDisplay":true },
			{ "URL":"hotel/rateholders/", "label":"Source Management", "requiresDepartment":true,
				"doDisplay":account?exists && account.hasPermissions("Source_Management", "UPDATE")
			},
			{ "URL":"admin/import/getImportForm", "label":"Import", "requiresDepartment":true,
				"doDisplay":account?exists && account.hasPermissions("Inventory_Management", "UPDATE")
			}
		]
	},

	{ "label":"Reparticion",
		"doDisplay":account?exists && account.hasPermissions("Department_Info", "READ"),
		"subNav": [
			{ "URL":"departments/department/showCredits",   "label":"Resumen Creditos",         "requiresDepartment":true, "doDisplay":account?exists && account.hasPermissions("Department_Info", "UPDATE") },
			{ "URL":"departments/department/showEmployments",   "label":"Agentes",         "requiresDepartment":true, "doDisplay":account?exists && account.hasPermissions("Manage_Employments", "READ") },
			{ "URL":"departments/department/showCreditEntries/2017",   "label":"Movimientos de Credito",         "requiresDepartment":true, "doDisplay":account?exists && account.hasPermissions("Manage_CreditsEntries", "READ") },
			{ "URL":"departments/department/hierarchicalAccumulatedCredits",   "label":"Creditos Retenidos Acumulados",         "requiresDepartment":true, "doDisplay":account?exists && showHierarchicalAccumulatedCredits }
		]
	},
	{ "label":"Movimientos",
        "doDisplay":account?exists && account.hasPermissions("Manage_CreditsEntries", "UPDATE_STATUS"),
        "subNav": [
            { "URL":"creditsentries/showCreditsEntries",   "label":"Buscar Movimientos de Credito",         "requiresDepartment":false, "doDisplay":account?exists && account.hasPermissions("Manage_CreditsEntries", "UPDATE_STATUS") }
        ]
    },

	{ "label":"Rooms/Rates",
		"doDisplay":account?exists && account.hasPermissions("Rooms_Rates", "UPDATE"),
		"subNav": [
			{ "URL":"hotel/rateplans/", "label":"Manage Rates","requiresDepartment":true, "doDisplay":true },
			{ "URL":"hotel/roomtypes/", "label":"Manage Rooms","requiresDepartment":true, "doDisplay":true },
			{ "URL":"hotel/addons/",    "label":"Manage Add Ons","requiresDepartment":true, "doDisplay":true }
		]
	},

	{ "label":"Reportes",
		"doDisplay":account?exists && account.hasPermissions("Reports", "READ"),
		"subNav": [
			{ "URL":"reports/reportSetup", "label":"Reportes Administrativos","requiresDepartment":true,"doDisplay":true },
			{ "URL":"departments/listDepartmentsCreditsSummary", "label":"Reporte Resumen Creditos Reparticiones","requiresDepartment":false,"doDisplay":true },
			
			{ "URL":"reportadmin/ReportAdminSetup","label":"Administration Reports","requiresDepartment":true,
				"doDisplay":account?exists && account.hasPermissions("ADMINISTRATION_REPORTS", "READ")},
			<#--{ "URL":"report/resSearch",   "label":"Reservation Production","requiresDepartment":true,"doDisplay":true }, -->
			{ "URL":"admin/export/getExportForm", "label":"Export", "requiresDepartment":true,
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
			{ "URL":"bookadmin/Find",        "label":"Reservations",    "requiresDepartment":true,
				"doDisplay":account?exists && account.hasPermissions("Search_Reservations", "READ")
			},
			{ "URL":"admin/iata",       "label":"Travel Agencies", "requiresDepartment":false,
				"doDisplay":account?exists && account.hasPermissions("TRAVEL_AGENCY", "READ")
			},
			{ "URL":"bookadmin/QueueSearch", "label":"Error Queue",     "requiresDepartment":true,
				"doDisplay":account?exists && account.hasPermissions("Search_Error_Queue", "READ")
			},
			{ "URL":"admin/audit/prices","label":"Price Log",      "requiresDepartment":true,
				"doDisplay":account?exists && account.hasPermissions("Search_Prices", "READ")
			},
			{ "URL":"bookadmin/call",        "label":"Wrap Call",       "requiresDepartment":false,
				"doDisplay":account?exists && account.hasPermissions("Call_Wrap", "READ")
			}
		]
	}<#--,

	{ "label":"Administracion",
		"doDisplay":account?exists && account.hasPermissions("Manage_Departments", "UPDATE"),
		"subNav": systemSubNav
	}-->

] >


<#assign currPageRequiresDepartment=false >
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
				<#assign currPageRequiresDepartment=subNav.requiresDepartment >
				<#break>
			</#if>
		</#list>
	
		<#if tabNum != -1 >
			<#break>
		</#if>
	</#list>
</#if>>