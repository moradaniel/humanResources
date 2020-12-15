<#import "/WEB-INF/views/spring.ftl" as spring />

<form id="proposeNewEmploymentForm" name="proposeNewEmploymentForm" action="${requestContext.contextPath}/employments/proposeNewEmploymentForm" method="post">

	<h1>Seleccione Categoria Propuesta</h1>

	<table border="0">
	
		<tr>
			<td>
			
				<select id="proposedCategoryCode" name="proposedCategoryCode" >
                    <option value="">- Elija Categoria -</option>
                    <#list availableCategoriesForNewEmployment as category>
                        <option value="${category.code}">${category.code?default("")}</option>
                    </#list>
                </select>
			
			</td>
		</tr>
			
	</table>			

	
	<br/>
	<br/>
				
	<h1 id='banner'>Seleccione Centro Sector</h1>
	

						<#--span class="searchDropDown">Property: </span-->
						<select id="subDepartmentId" name="subDepartmentId" <#-- onchange="onSelect(this,'${currentURL?default('')}')" --> >
	

								<option value="" >-- Seleccione Centro Sector --</option>
								<#list subDepartmentsOfDepartment as subDepartment>
									<option value="${subDepartment.id}" >${subDepartment.codigoCentro?html} ${subDepartment.nombreCentro?html} ${subDepartment.codigoSector?html} ${subDepartment.nombreSector?html}</option>
								</#list>

						</select>
	<br/>
	<br/>
			
	<input id="saveButton" name="saveButton" type="submit" value="Ingresar Agente">
	
	</form>

<script>
	$(function() {
		$(document).ready(function() { 
			Utils.blockUIonClick($('#saveButton'));
		});
	});
</script>
