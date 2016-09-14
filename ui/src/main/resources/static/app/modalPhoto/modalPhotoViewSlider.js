angular.module('modalPhotoViewSilderApp').controller('ModalPhotoViewSliderCtrl', function($scope, $event, $user) {
    var getEventPhotos = function () {
        if (!$scope.pagination.last) {
            $event.getPhotos($scope.parentModelId, $scope.pagination.nextPage, $scope.pagination.size).then(function (data) {
                if (data.content && data.content.length > 0) {
                    Array.prototype.push.apply($scope.photos, data.content);
                }
                $scope.pagination = { 'nextPage': (data.number + 1), 'size': data.size, 'last': data.last, 'totalElements': data.totalElements };
            });
        }
    };

    var getUserPhotos = function () {
        if (!$scope.pagination.last) {
            $user.getPhotos($scope.parentModelId, $scope.pagination.nextPage, $scope.pagination.size).then(function (data) {
                if (data.content && data.content.length > 0) {
                    Array.prototype.push.apply($scope.photos, data.content);
                }
                $scope.pagination = { 'nextPage': (data.number + 1), 'size': data.size, 'last': data.last, 'totalElements': data.totalElements };
            });
        }
    };

    $scope.$watch('index', function (newValue, oldValue) {
        if (newValue == ($scope.photos.length - 2)) {
            if ($scope.view == 'user' || $scope.view == 'profile') {
                getUserPhotos();
            } else if ($scope.view == 'event') {
                getEventPhotos();
            }
        }
    });
});

angular.module('modalPhotoViewSilderApp').service('$modalPhotoViewSlider', function($uibModal, $rootScope) {
    return {
        open: function (properties, opts) {
            var scope = $rootScope.$new();
            angular.extend(scope, properties);

            opts = angular.extend(opts || {}, {
                scope: scope,
                templateUrl: 'app/modalPhoto/modalPhotoViewSlider.html',
                windowClass: 'photo-carousel-modal',
                controller: 'ModalPhotoViewSliderCtrl'
            });

            return $uibModal.open(opts);
        }
    };
});