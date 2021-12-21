angular.module('weblamp').controller('orderController', function ($scope, $rootScope, $http, $localStorage) {
    const contextPath = 'http://localhost:8192/weblamp';

    $scope.loadOrder = function () {
            $http.get(contextPath + '/api/v1/orders')
                .then(function (response) {
                    $scope.OrderedList = response.data.productList;
                    console.log(response.data);
                    $rootScope.orderCount = response.data.count;
                    $scope.isOrderEmpty = response.data.productList.length > 0 ? false : true;
                    $scope.total = response.data.total;
                });
            }

    $scope.removeAllFromCart = function (productId) {
            $http.delete(contextPath + '/api/v1/orders/items/')
               .then(function (response) {
                    $scope.OrderedList = response.data.productList;
                    $rootScope.orderCount = response.data.count;
                    $scope.isOrderEmpty = response.data.productList.length > 0 ? false : true;
                    $scope.total = response.data.total;
                });
            }

    $scope.loadOrder();
});