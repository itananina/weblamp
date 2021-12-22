angular.module('weblamp').controller('ordersController', function ($scope, $rootScope, $http, $localStorage, $location) {
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

    $scope.loadConfirmed();
});