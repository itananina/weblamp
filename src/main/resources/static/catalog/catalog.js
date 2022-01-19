angular.module('weblamp').controller('catalogController', function ($scope, $rootScope, $http, $localStorage) {
    const contextPath = 'http://localhost:8192/weblamp';

    $scope.loadProducts = function (pageIndex) {
        $http({
            url: contextPath + '/api/v1/products',
            method: 'GET',
            params: {
                page: pageIndex ? pageIndex : null,
                min_price: $scope.newFilter ? $scope.newFilter.minPrice : null,
                max_price: $scope.newFilter ? $scope.newFilter.maxPrice : null,
                title_part: $scope.newFilter ? $scope.newFilter.titlePart : null
            }
        }).then(function (response) {
            $scope.ProductList = response.data.content; //.content для page !!!
            $scope.currentPage = response.data.number+1;
            $scope.isFirstPage = response.data.first;
            $scope.isLastPage = response.data.last;
        });
    };

    $scope.addToCart = function (productId) {
        $http.get(contextPath + '/api/v1/orders/items/' + productId)
            .then(function (response) {
                $rootScope.orderCount = response.data.count;
                console.log($rootScope.orderCount);
                $rootScope.refresh();
                $scope.isOrderEmpty = response.data.productList.length > 0 ? false : true;
            });
    }

    $scope.loadProducts(); //на лоаде
});