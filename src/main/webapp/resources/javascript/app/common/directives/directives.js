(function () {

    var as = angular.module('hrangularspring');

as.directive('sortBy', function () {
  return {
    templateUrl: prefixContextPath + '/resources/javascript/app/common/directives/sort-by.html',
    restrict: 'E',
    transclude: true,
    replace: true,
    scope: {
      sortdir: '=',
      sortedby: '=',
      sortvalue: '@',
      onsort: '='
    },
    link: function (scope, element, attrs) {
      scope.sort = function () {
        if (scope.sortedby == scope.sortvalue)
          scope.sortdir = scope.sortdir == 'asc' ? 'desc' : 'asc';
        else {
          scope.sortedby = scope.sortvalue;
          scope.sortdir = 'asc';
        }
        scope.onsort(scope.sortedby, scope.sortdir);
      }
    }
  };
});

as.directive('onBlurChange', function ($parse) {
	  return function (scope, element, attr) {
	    var fn = $parse(attr['onBlurChange']);
	    var hasChanged = false;
	    element.on('change', function (event) {
	      hasChanged = true;
	    });
	 
	    element.on('blur', function (event) {
	      if (hasChanged) {
	        scope.$apply(function () {
	          fn(scope, {$event: event});
	        });
	        hasChanged = false;
	      }
	    });
	  };
	});

as.directive('onEnterBlur', function() {
	  return function(scope, element, attrs) {
	    element.bind("keydown keypress", function(event) {
	      if(event.which === 13) {
	        element.blur();
	        event.preventDefault();
	      }
	    });
	  };
	});



/**
 * http://www.benlesh.com/2012/12/angular-js-custom-validation-via.html
 */
as.directive('cuilValidate', function() {
	
	
	function validate_Cuit_Cuil(sCUIT) 
	{     
	    var aMult = '5432765432'; 
	    var aMult = aMult.split(''); 
	     
	    if (sCUIT && sCUIT.length == 11) 
	    { 
	        aCUIT = sCUIT.split(''); 
	        var iResult = 0; 
	        for(i = 0; i <= 9; i++) 
	        { 
	            iResult += aCUIT[i] * aMult[i]; 
	        } 
	        iResult = (iResult % 11); 
	        iResult = 11 - iResult; 
	         
	        if (iResult == 11) iResult = 0; 
	        if (iResult == 10) iResult = 9; 
	         
	        if (iResult == aCUIT[10]) 
	        { 
	            return true; 
	        } 
	    }     
	    return false; 
	};
		
	
    return {
        // restrict to an attribute type.
        restrict: 'A',
        
        // element must have ng-model attribute.
        require: 'ngModel',
        
        // scope = the parent scope
        // elem = the element the directive is on
        // attr = a dictionary of attributes on the element
        // ctrl = the controller for ngModel.
        link: function(scope, elem, attr, ctrl) {
            
            //get the regex flags from the regex-validate-flags="" attribute (optional)
            //var flags = attr.regexValidateFlags || '';
            
            // create the regex obj.
            //var regex = new RegExp(attr.regexValidate, flags);            
                        
            // add a parser that will process each time the value is 
            // parsed into the model when the user updates it.
            ctrl.$parsers.unshift(function(value) {
                // test and set the validity after update.
                //var valid = regex.test(value);
            	var valid = validate_Cuit_Cuil(value);
                ctrl.$setValidity('cuilValidate', valid);
                
                // if it's valid, return the value to the model, 
                // otherwise return undefined.
                return valid ? value : undefined;
            });
            
            // add a formatter that will process each time the value 
            // is updated on the DOM element.
            ctrl.$formatters.unshift(function(value) {
                // validate.
            	
            	var valid = validate_Cuit_Cuil(value);
                //ctrl.$setValidity('cuilValidate', regex.test(value));
            	ctrl.$setValidity('cuilValidate', valid);
                
                // return the value or nothing will be written to the DOM.
                return value;
            });
        }
    };
});

}());