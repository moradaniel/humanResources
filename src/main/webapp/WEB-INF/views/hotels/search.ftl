
<#import "/WEB-INF/views/spring.ftl" as spring />

<#assign hotelsUrl= requestContext.contextPath+"/hotels"/>
<form id="searchForm" action="${hotelsUrl}" method="get" cssClass="inline ajaxForm">
    <#-- span class="errors span-18">
    	<form:errors path="*"/>
    </span -->
	<fieldset>
		<legend>Search Hotels</legend>
		<div class="span-8">
			<label for="searchString">Search String:</label>
			<#-- form:input id="searchString" path="searchString"/ -->
			<@spring.formInput "searchCriteria.searchString"/>
		</div>
		<div class="span-6">
			<div>
				<label for="pageSize">Maximum results:</label>
				<#assign chargeBasisTypes = {"5":"5",
											"10":"10",
											"20":"20"}/>
				<@spring.formSingleSelect "searchCriteria.pageSize", chargeBasisTypes, "" />
				<#-- form:select id="pageSize" path="pageSize">
					<form:option label="5" value="5"/>
					<form:option label="10" value="10"/>
					<form:option label="20" value="20"/>
				</form:select -->
			</div>
		</div>
		<div class="span-3 last">
			<button id="findHotels" type="submit">Find Hotels</button>
		</div>		
    </fieldset>
</form>
