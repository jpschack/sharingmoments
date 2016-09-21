angular.module('searchApp').controller('SearchListCtrl', function ($scope, $rootScope, $stateParams, $search, $modalSinglePhotoView, $googleLocationService) {
    var searchForUsers = function (q) {
        $search.searchForUsers(q, 0, 20).then(function (userList) {
            $scope.userList = userList;
            angular.forEach($scope.userList, function(user, key) {
                user.userImage = {url: (user.userImage ? user.userImage.url : 'http://placehold.it/150x150')};
            });
        });
    };

    var searchForEvents = function (q) {
        var from, to;
        if (angular.isDate($scope.dateFilter.from)) {
            from = $scope.dateFilter.from;
        }
        if (angular.isDate($scope.dateFilter.to)) {
            to = $scope.dateFilter.to;
        }
        $search.searchForEvents(q, 0, 20, from, to).then(function (eventList) {
            $scope.eventList = eventList;
            angular.forEach($scope.eventList, function(event, key) {
                $googleLocationService.getLocationByID(event.location.googleLocationID).then(function (googleLocation) {
                    if (googleLocation) {
                        event.googleLocation = { 'name': googleLocation.name, 'address': googleLocation.formatted_address, 'url': googleLocation.url };
                    }
                }).catch(function (error) {

                });
            });
        });
    };

    $scope.setTab = function (tabId) {
        $scope.tab = tabId;
        if (tabId == 1) {
            searchForUsers($scope.searchInput);
            
        } else if (tabId == 2) {
            searchForEvents($scope.searchInput);
        }
    };

    $scope.isSet = function (tabId) {
        return $scope.tab === tabId;
    };

    var init = function () {
        $scope.isDateFilterCollapsed = true;

        if ($stateParams.q) {
            $scope.searchInput = $stateParams.q;
        }
        if ($stateParams.tab) {
            if ($stateParams.tab == 'user') {
                $scope.activePill = 0;
            } else if ($stateParams.tab == 'event') {
                $scope.activePill = 1;
            }
        }
    };
    init();

    var initDateInput = function () {
        $scope.dateFilter = {};
        $scope.dateFilter.from = null;
        $scope.dateFilter.to = null;
        $scope.format = 'dd.MM.yyyy';
        $scope.altInputFormats = ['M!/d!/yyyy'];
    };
    initDateInput();

    $scope.search = function () {
        if ($scope.tab == 1) {
            searchForUsers($scope.searchInput);
        } else if ($scope.tab == 2) {
            searchForEvents($scope.searchInput);
        }
    };

    $rootScope.$on("searchOnSearchList", function(event, data) {
        $scope.searchInput = data;
        $scope.search();
    });

    $scope.fromDatePopup = {
        opened: false
    };

    $scope.toDatePopup = {
        opened: false
    };

    $scope.inlineOptions = {
        minDate: new Date(),
        showWeeks: true
    };

    $scope.dateOptions = {
        formatYear: 'yy',
        maxDate: new Date(2025, 12, 31),
        minDate: new Date(),
        startingDay: 1
    };

    $scope.toggleMin = function () {
        $scope.inlineOptions.minDate = $scope.inlineOptions.minDate ? null : new Date();
        $scope.dateOptions.minDate = $scope.inlineOptions.minDate;
    };
    $scope.toggleMin();

    $scope.openFromDatePopup = function () {
        $scope.fromDatePopup.opened = true;
    };

    $scope.openToDatePopup = function () {
        $scope.toDatePopup.opened = true;
    };

    $scope.fromDateChanged = function () {
        if (angular.isDate($scope.dateFilter.from)) {
            if (angular.isDate($scope.dateFilter.to) && $scope.dateFilter.from > $scope.dateFilter.to) {
                $scope.dateFilter.to = $scope.dateFilter.from;
            }
            searchForEvents($scope.searchInput);
            $scope.fromDateIsNotValid = false;
        } else {
            $scope.fromDateIsNotValid = true;
        }
    };

    $scope.toDateChanged = function () {
        if (angular.isDate($scope.dateFilter.to)) {
            if (angular.isDate($scope.dateFilter.from) && $scope.dateFilter.from > $scope.dateFilter.to) {
                $scope.dateFilter.from = $scope.dateFilter.to;
            }
            searchForEvents($scope.searchInput);
            $scope.toDateIsNotValid = false;
        } else {
            $scope.toDateIsNotValid = true;
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

angular.module('searchApp').service('$search', function ($http, $dateEncoder) {
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
        searchForEvents : function(searchInput, page, size, from, to) {
            var url = '/api/v1/resource/event?q=' + searchInput + '&page=' + page + '&size=' + size;
            if (from != null) {
                url += '&from=' + $dateEncoder.formatDate(from);
            }
            if (to != null) {
                url += '&to=' + $dateEncoder.formatDate(to);
            }
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