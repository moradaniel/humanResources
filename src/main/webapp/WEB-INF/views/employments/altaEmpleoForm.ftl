<#import "/WEB-INF/views/spring.ftl" as spring />

<form id="crearEmpleoForm" >

	<h1>Seleccione Fecha de Alta</h1>
	<p>Fecha: <input type="text" id="datePickerFechaAlta"></p>

	<br/>
	<br/>
				
	<h1 id='banner'>Seleccione Agente</h1>
	
	<div id='jqgrid'>
		<table id='gridAgentes'></table>
		<div id='pagerAgentes'></div>
	</div>
	
	<br/>
	<br/>
				
	<h1 id='banner'>Seleccione Centro Sector</h1>
	
	<div id='jqgrid'>
		<table id='gridCentroSectores'></table>
		<div id='pagerCentroSectores'></div>
	</div>
	
	<div id='msgbox' title='' style='display:none'></div>
	
	<br/>
			
	<input id="buttonSubmit" name="buttonSubmit" type="button" value="Crear Empleo">
	
	</form>
	
<script type='text/javascript'>
	$(function() {
	
		//---------Fecha de Alta
		//$("#datePickerFechaAlta").datepicker();
		$( "#datePickerFechaAlta" ).datepicker( $.datepicker.regional[ "es" ] );
		
	
		//--------Lista de Agentes	
		$("#gridAgentes").jqGrid({
		   	url:'${requestContext.contextPath}/agentes/search',
			datatype: 'json',
			mtype: 'GET',
		   	colNames:['Id', 'Apellido y Nombre', 'CUIL'],
		   	colModel:[
		   		{name:'id',index:'id', width:55, editable:false, editoptions:{readonly:true, size:10}, hidden:true},
		   		{name:'apellidoNombre',index:'apellidoNombre', width:100 },
		   		/*{name:'password',index:'password', width:100, editable:true, editrules:{required:true}, editoptions:{size:10}, edittype:'password', hidden:true},*/
		   		{name:'cuil',index:'cuil', width:100, editable:false}
		   	],
		   	rowList:[20,50,100],
		   	height: 240,
		   	autowidth: true,
			rownumbers: true,
		   	pager: '#pagerAgentes',
		   	sortname: 'apellidoNombre',
		    viewrecords: true,
		    sortorder: "asc",
		    caption:"Agentes",
		    emptyrecords: "No hay registros",
		    loadonce: false,
		    jsonReader : {
		        root: "rows",
		        page: "page",
		        total: "total",
		        records: "records",
		        repeatitems: false,
		        cell: "cell",
		        id: "id"
		    }
		});
		
		$("#gridAgentes").jqGrid('setGridParam', { search: true });

		$("#gridAgentes").jqGrid('navGrid','#pagerAgentes',
				{edit:false, add:false, del:false, search:false}
		);


		// Toolbar Search
		$("#gridAgentes").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : true, defaultSearch:"cn"});



		//--------Lista de Centro Sectores	-----------------------------------------------------------------------------------
		$("#gridCentroSectores").jqGrid({
		   	url:'${requestContext.contextPath}/centroSectores/search',
			datatype: 'json',
			mtype: 'GET',
		   	colNames:['Id', 'Codigo Centro', 'Nombre Centro', 'Codigo Sector', 'Nombre Sector'],
		   	colModel:[
		   		{name:'id',index:'id', width:55, editable:false, editoptions:{readonly:true, size:10}, hidden:true},
		   		{name:'codigoCentro',index:'codigoCentro', width:100 },
		   		{name:'nombreCentro',index:'nombreCentro', width:100, editable:false},
		   		{name:'codigoSector',index:'codigoSector', width:100, editable:false},
		   		{name:'nombreSector',index:'nombreSector', width:100, editable:false}
		   	],
		   	rowList:[20,50,100],
		   	height: 240,
		   	autowidth: true,
			rownumbers: true,
		   	pager: '#pagerCentroSectores',
		   	sortname: 'codigoCentro',
		    viewrecords: true,
		    sortorder: "asc",
		    caption:"Centro Sectores",
		    emptyrecords: "No hay registros",
		    loadonce: false,
		    jsonReader : {
		        root: "rows",
		        page: "page",
		        total: "total",
		        records: "records",
		        repeatitems: false,
		        cell: "cell",
		        id: "id"
		    }
		});
		
		$("#gridCentroSectores").jqGrid('setGridParam', { search: true });

		$("#gridCentroSectores").jqGrid('navGrid','#pagerCentroSectores',
				{edit:false, add:false, del:false, search:false}
		);


		// Toolbar Search
		$("#gridCentroSectores").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : true, defaultSearch:"cn"});
		
		
		
		//-----------------
		$("#buttonSubmit").click(
			function() {
				
				
				var gridAgentes = jQuery("#gridAgentes");


				var idAgenteSeleccionado = gridAgentes.getGridParam("selrow"); // returns null if no row is selected
				if(idAgenteSeleccionado===null){
					alert("Seleccione Agente");
					return false;
				}
				
				
				var gridCentroSectores = jQuery("#gridCentroSectores");
				
				var idCentroSectorSeleccionado = gridCentroSectores.getGridParam("selrow"); // returns null if no row is selected
				if(idCentroSectorSeleccionado===null){
					alert("Seleccione Centro Sector");
					return false;
				}

				//$("#crearEmpleoForm").submit();
				
				$.ajax({
				  url: '${requestContext.contextPath}/empleos/crearEmpleo',
				  type: 'post',
				  data: 'idAgente='+idAgenteSeleccionado+'&idCentroSector='+idCentroSectorSeleccionado,
				  success: function(data){
				        var usernameCount = data;
				        /*if(1 == usernameCount){
						            $(‘#penewuser’).next(‘.error’).css(‘display’, ‘inline’);
				        } else {
				            $(‘#penewuser’).next(‘.error’).css(‘display’, ‘none’);
				        }*/
				    },
				  error: function(data){
				        alert("Error al conectar con el servidor. Posiblemente su sesion ha expirado, por favor vuelva a conectarse");
				        window.location='${requestContext.contextPath}';
				        /*if(1 == usernameCount){
						            $(‘#penewuser’).next(‘.error’).css(‘display’, ‘inline’);
				        } else {
				            $(‘#penewuser’).next(‘.error’).css(‘display’, ‘none’);
				        }*/
				    },
				  dataType: 'json'
				});
			}
		);
		
	});



	
	</script>