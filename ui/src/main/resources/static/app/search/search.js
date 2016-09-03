angular.module('searchApp').controller('SearchListCtrl', function ($scope, $rootScope, $stateParams, $search, $translate, $modalSinglePhotoView, $googleLocationService) {
    var searchForUsers = function (q) {
        $search.searchForUsers(q, 0, 20).then(function (userList) {
            angular.forEach(userList, function(user, key) {
                userList[key].userImage = {url: (user.userImage ? user.userImage.url : 'http://placehold.it/150x150')};
            });
            $scope.userList = userList;
        });
    };

    var searchForEvents = function (q) {
        $search.searchForEvents(q, 0, 20).then(function (eventList) {
            $scope.eventList = eventList;
            angular.forEach(eventList, function(event, key) {
                $googleLocationService.getLocationByID(event.location.googleLocationID).then(function (googleLocation) {
                    if (googleLocation) {
                        $scope.eventList[key].googleLocation = { 'name': googleLocation.name, 'address': googleLocation.formatted_address, 'url': googleLocation.url };
                    }
                }).catch(function (error) {

                });
            });
        });
    };

    var setSearchInputPlaceholder = function (key) {
        $translate(key)
        .then(function (translatedValue) {
            $scope.searchInputPlaceholder = translatedValue;
        });
    }

    $scope.setTab = function (tabId) {
        $scope.tab = tabId;
        if (tabId == 1) {
            setSearchInputPlaceholder('SEARCH.INPUT.PLACEHOLDER_USERS');
            searchForUsers($scope.searchInput);
            
        } else if (tabId == 2) {
            setSearchInputPlaceholder('SEARCH.INPUT.PLACEHOLDER_EVENTS');
            searchForEvents($scope.searchInput);
        }
    };

    $scope.isSet = function (tabId) {
        return $scope.tab === tabId;
    };

    var init = function () {
        if ($stateParams.q) {
            $scope.searchInput = $stateParams.q;
        }
        if ($stateParams.type) {
            if ($stateParams.type == 'user') {
                $scope.activePill = 0;
            } else if ($stateParams.type == 'event') {
                $scope.activePill = 1;
            }
        }
    };
    init();

    $rootScope.$on("searchOnSearchList", function(event, data) {
        $scope.searchInput = data;

        if ($scope.tab == 1) {
            searchForUsers($scope.searchInput);
        } else if ($scope.tab == 2) {
            searchForEvents($scope.searchInput);
        }
    });

    $scope.openUserImage = function (userImage) {
        if (userImage && userImage.id) {
            $modalSinglePhotoView.open({
                photo: userImage
            });
        }
    };
});

angular.module('searchApp').service('$search', function ($http) {
    return { 
        searchForUsers : function(searchInput, page, size) {
            var url = '/api/v1/resource/user?q=' + searchInput + '&page=' + page + '&size=' + size;
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
        searchForEvents : function(searchInput, page, size) {
            var url = '/api/v1/resource/event?q=' + searchInput + '&page=' + page + '&size=' + size;
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