<#import "/WEB-INF/views/spring.ftl" as spring />
<script type="text/javascript">

</script>

<h2>Cambio Estado Movimientop</h2>

<form id="cambioEstadoMovimientoForm" name="ascensoForm" action="${requestContext.contextPath}/empleos/updateAscenso" method="post">


	<input type="hidden" name="idEmpleoActual" value="${empleoActual.id?default('')}"/>

	<table border="0">
	
		<tr>
			<td><label>Apellido Nombre</label></td>
			<td>
				${empleoActual.agente.apellidoNombre}
			</td>
		</tr>
		<tr>
			<td><label>Categoria Actual</label></td>
			<td>
				${empleoActual.categoria.codigo}
			</td>
		</tr>
		<tr>
			<td><label>Categoria Propuesta</label></td>
			<td>
				<select id="codigoCategoriaPropuesta" name="codigoCategoriaPropuesta" >
					<#-- assign keys = dropDownListMap?keys -->
					<option value="">- Elija Categoria -</option>
					<#list categoriasDisponiblesParaAscenso as categoria>
						<#assign selected="">
						<#if (categoriaSeleccionada?exists && key.codigo == categoriaSeleccionada)><#assign selected="selected"/></#if>
						<option value="${categoria.codigo}" ${selected}>${categoria.codigo?default("")}</option>
					</#list>
				</select>
			
			</td>
		</tr>

		<tr><td><br></td></tr>
		<tr>
			<td colspan="3">
				<input id="saveButton" name="saveButton" class="button" type="submit" value="Guardar" />
			</td>
		</tr>
	</table>
</form>

 

<script>
	$(function() {
		$(document).ready(function() { 
			Utils.blockUIonClick($('#saveButton'));
		});
	});
</script>
