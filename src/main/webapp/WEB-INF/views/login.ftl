<#import "/WEB-INF/views/spring.ftl" as spring />

<br>
<!-- div style="margin-left: 80px">
<form action="${requestContext.contextPath}/j_spring_security_check" method="POST" ng-non-bindable>
	<table>
		<tr><td>Nombre Usuario:</td><td><input style="width:150px" type='text' name='j_username' value='' autofocus></td></tr>
		<tr><td>Password:</td><td><input style="width:150px" type='password' name='j_password'></td></tr>
		
		<tr align="right">
			<td colspan="2" align="right">
				<input name="submit" type="submit" value="Login" >
			</td>
		</tr>
	</table>

	<div style = "position: absolute; top: 6px; left: 350px;">
		<span>
		</span>
	</div>
<form>
</div -->

<div class="container-fluid">
	<form class="form-horizontal" action="${requestContext.contextPath}/j_spring_security_check" method="POST" ng-non-bindable >
	    <div class="form-group">
	        <label for="inputUsername" class="control-label col-xs-2">Nombre Usuario:</label>
	        <div class="col-xs-3">
	            <input type="text" name='j_username' class="form-control input-mini"  placeholder="" autofocus />
	        </div>
	    </div>
	    <div class="form-group">
	        <label for="inputPassword" class="control-label col-xs-2">Password</label>
	        <div class="col-xs-3">
	            <input type='password' name='j_password' class="form-control" placeholder="" />
	        </div>
	    </div>
	    <!-- div class="form-group">
	        <div class="col-xs-offset-2 col-xs-10">
	            <div class="checkbox">
	                <label><input type="checkbox"> Remember me</label>
	            </div>
	        </div>
	    </div -->
	    <div class="form-group">
	        <div class="col-xs-offset-2 col-xs-10">
	            <button type="submit" class="btn btn-primary">Login</button>
	        </div>
	    </div>
	</form>
</div>