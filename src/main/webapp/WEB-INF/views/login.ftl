<#import "/WEB-INF/views/spring.ftl" as spring />

<br>
<div style="margin-left: 80px">
<form action="${requestContext.contextPath}/j_spring_security_check" method="POST">
	<table>
		<tr><td>Nombre Usuario:</td><td><input style="width:150px" type='text' name='j_username' value='' autofocus></td></tr>
		<tr><td>Password:</td><td><input style="width:150px" type='password' name='j_password'></td></tr>
		<!-- <tr><td><input type="checkbox" name="_spring_security_remember_me"></td><td>Don't ask for my password for two weeks</td></tr> -->
		<tr align="right">
			<td colspan="2" align="right">
				<input name="submit" type="submit" value="Login" >
			</td>
		</tr>
	</table>
	<div style="margin-right: 25px">
		<#-- span id="termsOfUsage"><@spring.message "msg.termsAndAgreements"/></span -->
	</div>
	<div style = "position: absolute; top: 6px; left: 350px;">
		<span>
		</span>
	</div>
<form>
</div>
