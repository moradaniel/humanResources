
<#import "/WEB-INF/views/spring.ftl" as spring />

<#assign reparticionesUrl= requestContext.contextPath+"/reparticiones"/>
<form id="searchForm" action="${reparticionesUrl}" method="get" cssClass="inline ajaxForm">
    <span class="errors span-18">
    	<#-- form:errors path="*"/-->
    </span>
	<fieldset>
		<legend>Buscar Reparticiones</legend>
		<div class="span-8">
			<#--label for="searchString">Search String:</label>
			<@spring.formInput "searchCriteria.searchString"/-->
		</div>
		<div class="span-6">
			<div>
				<#--label for="pageSize">Maximum results:</label>
				<#assign chargeBasisTypes = {"5":"5",
											"10":"10",
											"20":"20"}/>
				<@spring.formSingleSelect "searchCriteria.pageSize", chargeBasisTypes, "" /-->
			</div>
		</div>
		<div class="span-3 last">
			<button id="findReparticiones" type="submit">Buscar</button>
		</div>		
    </fieldset>
</form>
