angular.module('userApp').controller('UserProfileCtrl', function ($scope, userData, $user, userPhotoData, userEventData, $user, $uibModal, $modalSinglePhotoView, $modalPhotoViewSlider) {
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

    $scope.showUserEvents = function () {
        var modURL = 'app/profile/event-list-modal.html';
        var profileUserEventsModal = $uibModal.open({ 
            scope: $scope, 
            templateUrl: modURL, 
            controller: 'ProfileEventListModalCtrl'
        });
    };

    $scope.openUserImage = function () {
        if ($scope.user.userImage && $scope.user.userImage.id) {
            $modalSinglePhotoView.open({
                photo: $scope.user.userImage
            });
        }
    };

    $scope.openPhotoSlider = function (index) {
        $modalPhotoViewSlider.open({
            index: index,
            photos: $scope.userPhotos,
            pagination: $scope.userPhotoPagination,
            view: 'user',
            parentModelId: $scope.user.id
        });
    };
});

angular.module('userApp').service('$user', function ($http) {
    return { 
        getById : function(id) {
            var url = '/api/v1/resource/user/' + id;
            return $http.get(url)
                .then(
                    function (response) {
                        if (response.data) {
                            return response.data;
                        } else {
                            return null;
                        }
                },
                function (httpError) {
                    throw { 'status': httpError.status , 'data': httpError.data };
                });
        },
        getPhotos : function(id, page, size) {
            var url = '/api/v1/resource/user/' + id + "/photos" + '?page=' + page + '&size=' + size;
            return $http.get(url)
                .then(
                    function (response) {
                        if (response.data) {
                            return response.data;
                        } else {
                            return null;
                        }
                },
                function (httpError) {
                    throw { 'status': httpError.status , 'data': httpError.data };
                });
        },
        getEvents : function(id, page, size) {
            var url = '/api/v1/resource/user/' + id + "/events" + '?page=' + page + '&size=' + size;
            return $http.get(url)
                .then(
                    function (response) {
                        if (response.data) {
                            return response.data;
                        } else {
                            return null;
                        }
                },
                function (httpError) {
                    throw { 'status': httpError.status , 'data': httpError.data };
                });
        }
    };
});