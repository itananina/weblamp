(function() {
    angular
        .module('weblamp', ['ngRoute','ngStorage'])
        .config(config)
        .run(run);

    function config($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'catalog/catalog.html',
                controller: 'catalogController'
            })
            .when('/order', {
                templateUrl: 'order/order.html',
                controller: 'orderController'
            })
            .otherwise({
                redirectTo: '/'
            });
    }

    function run($rootScope, $http){

    }
})();

angular.module('weblamp').controller('indexController', function ($rootScope, $http, $localStorage) {
    const contextPath = 'http://localhost:8192/weblamp';
    $rootScope.loadOrder = function () {
        $http.get(contextPath + '/api/v1/orders')
            .then(function (response) {
                $rootScope.orderCount = response.data.count;
            });
        }
    $rootScope.loadOrder();
});

