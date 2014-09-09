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
	 * AngularJS default filter with the following expression:
	 * "person in people | filter: {name: $select.search, age: $select.search}"
	 * performs a AND between 'name: $select.search' and 'age: $select.search'.
	 * We want to perform a OR.
	 */
	as.filter('propsFilter', function() {
	  return function(items, props) {
	    var out = [];

	    if (angular.isArray(items)) {
	      items.forEach(function(item) {
	        var itemMatches = false;

	        var keys = Object.keys(props);
	        for (var i = 0; i < keys.length; i++) {
	          var prop = keys[i];
	          var text = props[prop].toLowerCase();
	          if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
	            itemMatches = true;
	            break;
	          }
	        }

	        if (itemMatches) {
	          out.push(item);
	        }
	      });
	    } else {
	      // Let the output be the input untouched
	      out = items;
	    }

	    return out;
	  };
	});

}());