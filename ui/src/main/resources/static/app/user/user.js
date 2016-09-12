angular.module('userApp').controller('UserProfileCtrl', function ($scope, $stateParams, $state, userData, $user) {
    var getPhotos = function () {
        $user.getPhotos($stateParams.id, 0, 10).then(function (photos) {
            $scope.photos = photos;
        });
    };

    var getEvents = function () {
        $user.getEvents($stateParams.id, 0, 10).then(function (events) {
            $scope.events = events;
        });
    };

    var init = function () {
        if (userData) {
            userData.userImage = {url: (userData.userImage ? userData.userImage.url : 'http://placehold.it/150x150')};
            $scope.user = userData;
        }
    };
    init();

    $scope.openUserImage = function (userImage) {
        if (userImage && userImage.id) {
            $modalSinglePhotoView.open({
                photo: userImage
            });
        }
    };

    $scope.setTab = function (tabId) {
        $scope.tab = tabId;

        if (tabId == 1) {
            getPhotos();
        } else if (tabId == 2) {
            getEvents();
        }
    };

    $scope.isSet = function (tabId) {
        return $scope.tab === tabId;
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
            var url = '/api/v1/resource/user/' + id + "/photos" + '?page=' + page + '&size=' + size;;
            return $http.get(url)
                .then(
                    function (response) {
                        if (response.data.content) {
                            return response.data.content;
                        } else {
                            return [];
                        }
                },
                function (httpError) {
                    throw { 'status': httpError.status , 'data': httpError.data };
                });
        },
        getEvents : function(id, page, size) {
            var url = '/api/v1/resource/user/' + id + "/photos" + '?page=' + page + '&size=' + size;;
            return $http.get(url)
                .then(
                    function (response) {
                        if (response.data.content) {
                            return response.data.content;
                        } else {
                            return [];
                        }
                },
                function (httpError) {
                    throw { 'status': httpError.status , 'data': httpError.data };
                });
        }
    };
});