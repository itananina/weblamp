angular.module('weblamp').controller('ordersController', function ($scope, $rootScope, $http, $localStorage, $window, $location) {
    const contextPath = 'http://localhost:8192/weblamp';

    $scope.loadConfirmed = function () {
        $http.get(contextPath + '/api/v1/orders/')
            .then(function successCallback(response) {
                $scope.ConfirmedList = response.data;
            }, function errorCallback(response) {
                    $rootScope.wayForAuth = $location.url();
                    $location.path('/login');
            });
    }

    $scope.showDetails = function (orderId) {
        $window.open(contextPath + '/api/v1/orders/' + orderId, 'popup', 'scrollbars=no,resizable=no,top=100,left=500,width=600,height=400');
    }

    $scope.loadConfirmed();
});