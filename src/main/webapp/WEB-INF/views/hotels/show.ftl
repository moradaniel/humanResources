
<div id="hotelDetails">
<p>
	<#if !hotel?exists>
		Hotel not yet selected.
	<#else>
		
		<h3 class="alt">${hotel.name}</h3>
		<address>
			${hotel.address}
			<br />
			${hotel.city}, ${hotel.state}, ${hotel.zip}
			<br />
			${hotel.country}
		</address>
		<a href="${hotel.id}/edit" class="ajaxLink">Edit Hotel</a> 
	</#if>
</p>
</div>