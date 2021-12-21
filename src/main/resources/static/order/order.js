angular.module('weblamp').controller('orderController', function ($scope, $rootScope, $http, $localStorage, $location) {
    const contextPath = 'http://localhost:8192/weblamp';

    $scope.loadOrder = function () {
            $http.get(contextPath + '/api/v1/orders')
                .then(function successCallback(response) {
                    $scope.OrderedList = response.data.productList;
                    console.log(response.data);
                    $rootScope.orderCount = response.data.count;
                    $rootScope.$broadcast('orderCount', $rootScope.orderCount);
                    $scope.isOrderEmpty = response.data.productList.length > 0 ? false : true;
                    $scope.total = response.data.total;
                }, function errorCallback(response) {
                        $rootScope.wayForAuth = $location.url();
                        $location.path('/login');
                    });
            }

    $scope.removeAllFromCart = function (productId) {
            $http.delete(contextPath + '/api/v1/orders/items/')
               .then(function (response) {
                    $scope.OrderedList = response.data.productList;
                    $rootScope.orderCount = response.data.count;
                    $rootScope.$broadcast('orderCount', $rootScope.orderCount);
                    $scope.isOrderEmpty = response.data.productList.length > 0 ? false : true;
                    $scope.total = response.data.total;
                });
            }

    $scope.loadOrder();
});