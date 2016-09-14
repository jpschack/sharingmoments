angular.module('profileApp').controller('ProfileCtrl', function($scope, $rootScope, userData, userPhotoData, userEventData, $account, $user, $uibModal, $modalPhotoViewSlider) {
    $scope.getPhotos = function () {
        if (!$scope.userPhotoPagination.last && !$scope.photosLoading) {
            $scope.photosLoading = true;
            $user.getPhotos($scope.user.id, $scope.userPhotoPagination.nextPage, $scope.userPhotoPagination.size).then(function (data) {
                if (data.content && data.content.length > 0) {
                    Array.prototype.unshift.apply($scope.userPhotos, data.content);
                }
                $scope.userPhotoPagination = { 'nextPage': (data.number + 1), 'size': data.size, 'last': data.last, 'totalElements': data.totalElements };
                $scope.photosLoading = false;
            });
        }
    };

    var init = function () {
        $scope.userImageUpdating = false;
        $scope.photosLoading = false;

        if (userData) {
            if (!userData.userImage) {
                userData.userImage = { url: 'http://placehold.it/200x200' };
            }
            $scope.user = userData;
        }

        if (userPhotoData) {
            if (userPhotoData.content.length > 0) {
                $scope.userPhotos = userPhotoData.content;
            }
            $scope.userPhotoPagination = { 'nextPage': (userPhotoData.number + 1), 'size': userPhotoData.size, 'last': userPhotoData.last, 'totalElements': userPhotoData.totalElements };
        } else {
            $scope.userPhotoPagination = { 'nextPage': null, 'size': null, 'last': null, 'totalElements': 0 };
        }

        if (userEventData) {
            if (userEventData.content.length > 0) {
                $scope.userEvents = userEventData.content;
            }
            $scope.userEventPagination = { 'nextPage': (userEventData.number + 1), 'size': userEventData.size, 'last': userEventData.last, 'totalElements': userEventData.totalElements };
        } else {
            $scope.userEventPagination = { 'nextPage': null, 'size': null, 'last': null, 'totalElements': 0 };
        }
    };
    init();

    $scope.userImageAction = function () {
        var modURL = 'app/profile/update-user-image-modal.html';
        var profileImageActionsModal = $uibModal.open({ scope: $scope, templateUrl: modURL, controller: 'ProfileImageCtrl', windowClass: 'update-user-image-modal' });
    };

    $scope.showUserEvents = function () {
        var modURL = 'app/profile/event-list-modal.html';
        var profileUserEventsModal = $uibModal.open({ 
            scope: $scope, 
            templateUrl: modURL, 
            controller: 'ProfileEventListModalCtrl'
        });
    };

    $scope.photoAction = function (index) {
        $modalPhotoViewSlider.open({
            index: index,
            photos: $scope.userPhotos,
            pagination: $scope.userPhotoPagination,
            view: 'profile',
            parentModelId: $scope.user.id
        });
    };

    $rootScope.$on("userImageStartUpdating", function() {
        $scope.userImageUpdating = true;
    });

    $rootScope.$on("userImageStoppedUpdating", function() {
        $scope.userImageUpdating = false;
    });
});

angular.module('profileApp').controller('ProfileEventListModalCtrl', function($scope, $uibModalInstance, $user, $googleLocationService) {
    var init = function () {
        $scope.eventsLoading = false;
        angular.forEach($scope.userEvents, function(event, key) {
            $googleLocationService.getLocationByID(event.location.googleLocationID).then(function (googleLocation) {
                event.googleLocation = { 'name': googleLocation.name };
            });
        });
    };
    init();

    $scope.close = function () {
        $uibModalInstance.dismiss('cancel');
    };

    $scope.getEvents = function () {
        if (!$scope.userEventPagination.last && !$scope.eventsLoading) {
            $scope.eventsLoading = true;
            $user.getEvents($scope.user.id, $scope.userEventPagination.nextPage, $scope.userEventPagination.size).then(function (data) {
                if (data.content && data.content.length > 0) {
                    angular.forEach(data.content, function(event, key) {
                        $googleLocationService.getLocationByID(event.location.googleLocationID).then(function (googleLocation) {
                            event.googleLocation = { 'name': googleLocation.name };
                        });
                    });
                    Array.prototype.unshift.apply($scope.userEvents, data.content);
                }
                $scope.userEventPagination = { 'nextPage': (data.number + 1), 'size': data.size, 'last': data.last, 'totalElements': data.totalElements };
                $scope.eventsLoading = false;
            });
        }
    };
});

angular.module('profileApp').controller('ProfileImageCtrl', function($scope, $rootScope, $uibModalInstance, $account) {
    $scope.uploadFile = function (event) {
        var files = event.target.files;

        if (files[0].type == 'image/jpeg' || files[0].type == 'image/png') {
            $uibModalInstance.dismiss('cancel');
            $rootScope.$emit("userImageStartUpdating", {});

            $account.uploadUserImage(files[0]).then(function (userImage) {
                $scope.user.userImage = userImage;
            }).finally(function() {
                $rootScope.$emit("userImageStoppedUpdating", {});
            });

        } else {
            //not an image -> modal alert
        }
    };

    $scope.deleteUserImage = function () {
        $uibModalInstance.dismiss('cancel');
        $account.deleteUserImage().then(function () {
            $scope.user.userImage = { url: 'http://placehold.it/200x200' };
        });
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});