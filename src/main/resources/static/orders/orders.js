angular.module('weblamp').controller('ordersController', function ($scope, $rootScope, $http, $localStorage, $window, $location) {
    const contextPath = 'http://localhost:8192/weblamp';

    $scope.loadConfirmed = function (pageIndex) {
            $http({
                url: contextPath + '/api/v1/orders',
                method: 'GET',
                params: {
                    page: pageIndex ? pageIndex : null
                }

            }).then(function successCallback(response) {
                $scope.ConfirmedList = response.data.content;
                $scope.currentPage = response.data.number+1;
                $scope.isFirstPage = response.data.first;
                $scope.isLastPage = response.data.last;
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