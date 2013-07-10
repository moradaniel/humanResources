

<div id="hotelResults">
<#if !hotelList?has_content>
	<p>No hotels. Please, change your search criteria.</p>
<#else>

	<#assign hotelsUrl= requestContext.contextPath+"/hotels">

	<p>
	<table class="summary">
		<thead>
			<tr>
				<th>Name</th>
				<th>Address</th>
				<th>City, State</th>
				<th>Zip</th>
				<th>Action</th>
			</tr>
		</thead>
		<tbody>
		    <#foreach hotel in hotelList>
				<tr>
					<td>${hotel.name}</td>
					<td>${hotel.address}</td>
					<td>${hotel.city}, ${hotel.state}, ${hotel.country}</td>
					<td>${hotel.zip}</td>
					<td><a href="${hotelsUrl}/${hotel.id}" class="ajaxLink">View Hotel</a></td>
				</tr>
			</#foreach>
			<#if !hotelList?has_content>
				<tr>
					<td colspan="5">No hotels found</td>
				</tr>
			</#if>
		</tbody>
	</table>
	<div class="buttonGroup">
		<#if (searchCriteria.page > 0) >
			<a id="prevResultsLink"  class="ajaxLink"
				href="${hotelsUrl}?searchString=${searchCriteria.searchString}&pageSize=${searchCriteria.pageSize}&page=${searchCriteria.page - 1}">Previous</a>
		</#if>
		<#if hotelList?has_content && (hotelList?size == searchCriteria.pageSize)>
			<a id="moreResultsLink" class="ajaxLink"
				href="${hotelsUrl}?searchString=${searchCriteria.searchString}&pageSize=${searchCriteria.pageSize}&page=${searchCriteria.page + 1}">Next</a>
		</#if>
	</div>
	</p>
</#if>
</div>	

