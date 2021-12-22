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
            .when('/cart', {
                templateUrl: 'cart/cart.html',
                controller: 'cartController'
            })
            .when('/login', {
                templateUrl: 'login/login.html',
                controller: 'loginController'
            })
            .when('/orders', {
                templateUrl: 'orders/orders.html',
                controller: 'ordersController'
            })
            .otherwise({
                redirectTo: '/'
            });
    }

    function run($rootScope, $http){

    }
})();

angular.module('weblamp').controller('indexController', function ($rootScope, $http, $localStorage, $location) {
    const contextPath = 'http://localhost:8192/weblamp';

    $rootScope.loadOrder = function () {
        $http.get(contextPath + '/api/v1/orders/active')
            .then(function (response) {
                $rootScope.orderCount = response.data.count;
            });
        }
    $rootScope.loadOrder();

    $rootScope.isUserLoggedIn = function () {
        if ($localStorage.springWebUser) {
            return true;
        } else {
            return false;
        }
    };

    if ($localStorage.springWebUser) {
        $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.springWebUser.token;
    }

    $rootScope.logout = function () {
        $rootScope.clearUser();
        if ($rootScope.user.username) {
            $rootScope.user.username = null;
        }
        if ($rootScope.user.password) {
            $rootScope.user.password = null;
        }
        $rootScope.orderCount = '';
        $location.path('/');
    };

    $rootScope.clearUser = function () {
        delete $localStorage.springWebUser;
        $http.defaults.headers.common.Authorization = '';
    };

    $rootScope.goToLogin = function () {
        $rootScope.wayForAuth = $location.url();
        $location.path('/login');
    };

    $rootScope.$on('orderCount', function (event, data) {
    });

});

