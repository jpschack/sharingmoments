angular.module('searchApp').controller('SearchListCtrl', function ($scope, $stateParams, $search, $translate, $modalSinglePhotoView) {
    var searchForUsers = function (q) {
        $search.searchForUsers(q, 0, 20).then(function (userList) {
            angular.forEach(userList, function(user, key) {
                userList[key].userImage = {url: (user.userImage ? user.userImage.url : 'http://placehold.it/100x100')};
            });
            $scope.userList = userList;
        });
    };

    var searchForEvents = function (q) {
        $search.searchForEvents(q, 0, 20).then(function (eventList) {
            $scope.eventList = eventList;
        });
    };

    var setSearchInputPlaceholder = function (key) {
        $translate(key)
        .then(function (translatedValue) {
            $scope.searchInputPlaceholder = translatedValue;
        });
    }

    var init = function () {
        if ($stateParams.q) {
            $scope.searchInput = $stateParams.q;
        }
    };
    init();

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

    $scope.search = function () {
        if ($scope.tab == 1) {
            searchForUsers($scope.searchInput);
        } else if ($scope.tab == 2) {
            searchForEvents($scope.searchInput);
        }
    };

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
                    throw httpError.status + " : " + httpError.data;
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
                    throw httpError.status + " : " + httpError.data;
                });
        }
    };
});