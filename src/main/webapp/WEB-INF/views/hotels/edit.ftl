
<#import "/WEB-INF/views/spring.ftl" as spring />

<div id="hotelDetails">
	<#--spring:url var="hotelsUrl" value="/hotels/{id}">
		<spring:param name="id" value="${hotel.id}"/>
	</spring:url-->
	<#assign hotelsUrl= requestContext.contextPath+"/hotels/"+hotel.id/>
	<#--form:form modelAttribute="hotel" action="${hotelsUrl}" method="post" cssClass="inline ajaxForm"-->
	<form action="${hotelsUrl}" method="post" cssClass="inline ajaxForm">
	    <#-- span><form:errors path="*"/></span -->
		<fieldset>
			<legend>Edit Hotel</legend>
			<div>
				<label for="searchString">Name:</label>
				<#-- form:input id="name" path="name"/ -->
				<@spring.formInput "hotel.name"/>
			</div>
			<div>
				<label for="city">City:</label>
				<#-- form:input id="city" path="city"/ -->
				<@spring.formInput "hotel.city"/>
			</div>
			<div>
				<button id="saveHotel" type="submit">Save</button>
			</div>		
	    </fieldset>
	</form:form>
</div>