<#import "/WEB-INF/views/spring.ftl" as spring />
<script type="text/javascript">

</script>

<h2>Movimiento Ascenso</h2>

<form id="promotePersonForm" name="promotePersonForm" action="${requestContext.contextPath}/empleos/promotePerson" method="post">


	<input type="hidden" name="idCurrentEmployment" value="${currentEmployment.id?default('')}"/>

	<table border="0">
	
		<tr>
			<td><label>Apellido Nombre</label></td>
			<td>
				${currentEmployment.person.apellidoNombre}
			</td>
		</tr>
		<tr>
			<td><label>Tramo</label></td>
			<td>
				<#if currentEmployment.occupationalGroup?exists >
					${currentEmployment.occupationalGroup.name} -  ${currentEmployment.occupationalGroup.code} 
					- Minima Categoria:${currentEmployment.occupationalGroup.minimumCategory.code}   
					- Maxima Categoria:${currentEmployment.occupationalGroup.maximumCategory.code}
				</#if>	
			</td>
		</tr>
		<tr>
			<td><label>Agrupamiento</label></td>
			<td>
				<#if currentEmployment.occupationalGroup?exists && currentEmployment.occupationalGroup.parentOccupationalGroup?exists>
					${currentEmployment.occupationalGroup.parentOccupationalGroup.name} - ${currentEmployment.occupationalGroup.parentOccupationalGroup.code} 
					- Minima Categoria:${currentEmployment.occupationalGroup.parentOccupationalGroup.minimumCategory.code}   
					- Maxima Categoria:${currentEmployment.occupationalGroup.parentOccupationalGroup.maximumCategory.code}
				</#if>	
			</td>
		</tr>
		<tr>
			<td><label>Categoria Actual</label></td>
			<td>
				${currentEmployment.category.code}
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
