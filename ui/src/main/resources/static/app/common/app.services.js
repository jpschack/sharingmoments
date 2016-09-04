angular.module('app.services').service('$modalPhotoViewSlider', function($uibModal, $rootScope) {
    return {
        open: function (properties, opts) {
            var scope = $rootScope.$new();
            angular.extend(scope, properties);

            opts = angular.extend(opts || {}, {
                backdrop: false,
                scope: scope,
                templateUrl: 'app/modalPhoto/modalPhotoViewSlider.html',
                windowTemplateUrl: 'app/modalPhoto/modalPhotoView.html'
            });

            return $uibModal.open(opts);
        }
    };
});

angular.module('app.services').service('$modalSinglePhotoView', function($uibModal, $rootScope) {
    return {
        open: function (properties, opts) {
            var scope = $rootScope.$new();
            angular.extend(scope, properties);

            opts = angular.extend(opts || {}, {
                backdrop: false,
                scope: scope,
                templateUrl: 'app/modalPhoto/modalSinglePhotoView.html',
                windowTemplateUrl: 'app/modalPhoto/modalPhotoView.html'
            });

            return $uibModal.open(opts);
        }
    };
});

angular.module('app.services').service('$geoLocationService', function($q, $window) {
    return {
        getCurrentPosition: function () {
            var deferred = $q.defer();

            if (!$window.navigator.geolocation) {
                deferred.reject('Geolocation not supported.');
            } else {
                $window.navigator.geolocation.getCurrentPosition(function (position) {
                    deferred.resolve(position);
                },
                function (err) {
                    deferred.reject(err);
                });
            }

        return deferred.promise;
        }
    };
});

angular.module('app.services').service('$dateEncoder', function() {
    return {
        formatDate: function (date) {
            return date.getFullYear() + '-' + ('0' + (date.getMonth()+1)).slice(-2) + '-' + ('0' + date.getDate()).slice(-2);
        }
    };
});