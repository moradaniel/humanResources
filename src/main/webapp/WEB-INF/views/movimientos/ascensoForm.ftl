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
					- Minima Categoria:${empleoActual.occupationalGroup.minimumCategory.code}   
					- Maxima Categoria:${empleoActual.occupationalGroup.maximumCategory.code}
				</#if>	
			</td>
		</tr>
		<tr>
			<td><label>Agrupamiento</label></td>
			<td>
				<#if empleoActual.occupationalGroup?exists && empleoActual.occupationalGroup.parentOccupationalGroup?exists>
					${empleoActual.occupationalGroup.parentOccupationalGroup.name} - ${empleoActual.occupationalGroup.parentOccupationalGroup.code} 
					- Minima Categoria:${empleoActual.occupationalGroup.parentOccupationalGroup.minimumCategory.code}   
					- Maxima Categoria:${empleoActual.occupationalGroup.parentOccupationalGroup.maximumCategory.code}
				</#if>	
			</td>
		</tr>
		<tr>
			<td><label>Categoria Actual</label></td>
			<td>
				${empleoActual.category.code}
			</td>
		</tr>
		<tr>
			<td><label>Categoria Propuesta</label></td>
			<td>
				<select id="proposedCategoryCode" name="proposedCategoryCode" >
					<#-- assign keys = dropDownListMap?keys -->
					<option value="">- Elija Categoria -</option>
					<#list availableCategoriesForPromotion as category>
						<#assign selected="">
						<#if (selectedCategory?exists && key.code == selectedCategory)><#assign selected="selected"/></#if>
						<option value="${category.code}" ${selected}>${category.code?default("")}</option>
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
