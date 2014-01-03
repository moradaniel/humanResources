<#import "/WEB-INF/views/spring.ftl" as spring />
<script type="text/javascript">

</script>

<h2>Cambio Estado Movimiento</h2>

<form id="cambioEstadoMovimientoForm" name="cambioEstadoMovimientoForm" action="${requestContext.contextPath}/creditsentries/cambiarEstadoMovimiento" method="post">

	
	<@spring.formHiddenInput "creditsEntryForm.creditsEntryId" />

	<table border="0">
	
		<tr>
			<td><label>Apellido Nombre</label></td>
			<td>
				${creditsEntryForm.employeeName}
			</td>
		</tr>


		<tr>
			<td><label>Estado</label></td>
			<td><@spring.formSingleSelect "creditsEntryForm.grantedStatus", grantedStatuses, "" /></td>
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
