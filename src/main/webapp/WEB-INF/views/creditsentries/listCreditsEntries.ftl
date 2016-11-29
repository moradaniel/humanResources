
<#import "/WEB-INF/views/spring.ftl" as spring />

<div id="creditsEntriesResults">


<#--     --------------------- Angularjs                        -------------------             -->


<div ng-controller="CreditsEntriesGridCtrl">

<h3>Se encontraron ({{creditsEntriesCount}}) movimientos/s. Pagina {{filterCriteria.pageNumber}} de {{totalPages}}</h3>

<table class="table table-hover table-condensed">
  <thead>
    <tr>
      <th ng-repeat="header in headers" class="{{header.klass}}">
        <sort-by onsort="onSort" sortdir="filterCriteria.sortDir" sortedby="filterCriteria.sortedBy" sortvalue="{{ header.value }}">{{ header.title }}</sort-by>
      </th>
      
    </tr>
  </thead>
  <tbody>
  <tr>
    <td>
      <select   ng-change="filterResult()"
                ng-model="filterCriteria.creditsPeriodName" 
                ng-options="creditsPeriod.name as creditsPeriod.name for creditsPeriod in availableCreditsPeriods | orderBy:'name':true " 
                 > <!-- class="form-control  input-sm " -->
      <!-- orderBy:'name':true (true for descending/reverse)-->
        <!-- option value=""> </option -->
      </select>
    </td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td>
       <select ng-change="filterResult()" ng-model="filterCriteria.creditsEntryType" ng-options="creditsEntryType for creditsEntryType in creditsEntryTypes" class="form-control  input-sm" >
        <option value=""> </option><!-- empty option for selecting all types -->
      </select>
          
    </td>
    <td></td>
    <td></td>
    <td>
    </td>
    
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
  </tr>
  <tr ng-repeat="creditsEntryVO in creditsEntriesVOs">
    <!-- td><a href="/#/customer{{customer.id}}">{{employment.id}}</a></td -->
    <td>{{creditsEntryVO.creditsEntry.creditsPeriod.name}}</td>
    <td>{{creditsEntryVO.creditsEntry.employment.subDepartment.department.name}}</td>
    <td>{{creditsEntryVO.creditsEntry.employment.subDepartment.codigoCentro}}</td>
    <td>{{creditsEntryVO.creditsEntry.employment.subDepartment.nombreCentro}}</td>
    <td>{{creditsEntryVO.creditsEntry.employment.subDepartment.codigoSector}}</td>
    <td>{{creditsEntryVO.creditsEntry.employment.subDepartment.nombreSector}}</td>
    
    <td>{{creditsEntryVO.creditsEntry.employment.person.cuil}}</td>
    <td>{{creditsEntryVO.creditsEntry.employment.person.apellidoNombre}}</td>
    <td>{{creditsEntryVO.creditsEntry.creditsEntryType}}</td>
    <td>{{creditsEntryVO.creditsEntry.grantedStatus}}</td>
    
    <td>{{creditsEntryVO.creditsEntry.employment.startDate}}</td>
    <td>{{creditsEntryVO.creditsEntry.employment.endDate}}</td>
    <td>{{creditsEntryVO.creditsEntry.numberOfCredits}}</td>
    
    <td>{{creditsEntryVO.currentCategory}}</td>
    <td>{{creditsEntryVO.proposedCategory}}</td>
    
    <td>
    	<span ng-if="creditsEntryVO.employment.occupationalGroup" class="small">
				{{creditsEntryVO.creditsEntry.employment.occupationalGroup.name}} - {{creditsEntryVO.employment.occupationalGroup.code}}
		</span>
		<span ng-if="!creditsEntryVO.employment.occupationalGroup">
			<span class="error-text">Error Falta asignar Tramo</span>
		</span>
	</td>				
	
	<td>
    	<span ng-if="creditsEntryVO.creditsEntry.employment.occupationalGroup && creditsEntryVO.creditsEntry.employment.occupationalGroup.parentOccupationalGroup">
				{{creditsEntryVO.creditsEntry.employment.occupationalGroup.parentOccupationalGroup.name}}
		</span>
	</td>		


    <td>{{creditsEntryVO.creditsEntry.notes}}</td>


  </tr>
  </tbody>

</table>
<div ng-show="creditsEntriesCount == 0">
  <h3>No se encontraron movimientos con este criterio de busqueda</h3>
</div>
<div ng-show="totalPages > 1" class="align-center">
            
  <uib-pagination ng-model="filterCriteria.pageNumber" 
  			  ng-change="selectPage()" 
  			  total-items="creditsEntriesCount" 
  			  num-pages="totalPages" 
  			  max-size="10" 
  			  direction-links="true" 
  			  boundary-links="true"
  			  rotate="false" 
  			  previous-text="&lsaquo;" next-text="&rsaquo;" first-text="&laquo;" last-text="&raquo;">
  			  
  </uib-pagination>
</div>



</div>





</div>	

<#-- script>
	$(function() {
		$("input:submit, a, button", ".buttoniseUs").button();
	});
</script -->
