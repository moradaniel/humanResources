<#import "/WEB-INF/views/spring.ftl" as spring />
<script type="text/javascript">

</script>

<h2>Movimiento Ascenso</h2>

<form id="ascensoForm" name="ascensoForm" action="${requestContext.contextPath}/empleos/updateAscenso" method="post">


	<input type="hidden" name="idEmpleoActual" value="${empleoActual.id?default('')}"/>

	<table border="0">
	
		<tr>
			<td><label>Apellido Nombre</label></td>
			<td>
				${empleoActual.agente.apellidoNombre}
			</td>
		</tr>
		<tr>
			<td><label>Tramo</label></td>
			<td>
				<#if empleoActual.occupationalGroup?exists >
					${empleoActual.occupationalGroup.name} -  ${empleoActual.occupationalGroup.code} 
					- Minima Categoria:${empleoActual.occupationalGroup.minimumCategory.codigo}   
					- Maxima Categoria:${empleoActual.occupationalGroup.maximumCategory.codigo}
				</#if>	
			</td>
		</tr>
		<tr>
			<td><label>Agrupamiento</label></td>
			<td>
				<#if empleoActual.occupationalGroup?exists && empleoActual.occupationalGroup.parentOccupationalGroup?exists>
					${empleoActual.occupationalGroup.parentOccupationalGroup.name} - ${empleoActual.occupationalGroup.parentOccupationalGroup.code} 
					- Minima Categoria:${empleoActual.occupationalGroup.parentOccupationalGroup.minimumCategory.codigo}   
					- Maxima Categoria:${empleoActual.occupationalGroup.parentOccupationalGroup.maximumCategory.codigo}
				</#if>	
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
