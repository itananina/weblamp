angular.module('weblamp').controller('cartController', function ($scope, $rootScope, $http, $localStorage, $location, $window) {
    const contextPath = 'http://localhost:8192/weblamp';

    $scope.loadOrder = function () {
        $http.get(contextPath + '/api/v1/orders/active')
            .then(function successCallback(response) {
                $scope.OrderedList = response.data.productList;
                $rootScope.orderCount = response.data.count;
                $rootScope.refresh();
                $scope.isOrderEmpty = response.data.productList.length > 0 ? false : true;
                $scope.total = response.data.total;
            }, function errorCallback(response) {
                    $rootScope.wayForAuth = $location.url();
                    $location.path('/login');
            });
    }

    $scope.removeFromCart = function (productId) {
        $http.delete(contextPath + '/api/v1/orders/items/' + productId)
           .then(function (response) {
                $scope.OrderedList = response.data.productList;
                $rootScope.orderCount = response.data.count;
                $scope.total = response.data.total;
                $rootScope.refresh();
                $scope.isOrderEmpty = response.data.productList.length > 0 ? false : true;
            });
    }

    $scope.removeAllFromCart = function (productId) {
        $http.delete(contextPath + '/api/v1/orders/items/')
           .then(function (response) {
                $scope.OrderedList = response.data.productList;
                $rootScope.orderCount = response.data.count;
                $rootScope.refresh();
                $scope.isOrderEmpty = response.data.productList.length > 0 ? false : true;
                $scope.total = response.data.total;
        });
    }

    $scope.confirmOrder = function (productId) {
        $http.get(contextPath + '/api/v1/orders/confirm/')
            .then(function successCallback(response) {
                $scope.OrderedList = null;
                $rootScope.orderCount = 0;
                $rootScope.refresh();
                $scope.isOrderEmpty = true;
                $scope.total = 0;
             }, function errorCallback(response) {
                alert("Ошибка при оформлении заказа!");
        });
    }

    $scope.loadOrder();
});