angular.module('naviApp').controller('NaviCtrl', function($scope, $state, $auth, $uibModal) {
    $scope.isCollapsed = true;

    $scope.logout = function () {
        $auth.logout();
        $state.go('/');
    };

    $scope.openNewMomentsModal = function () {
        var modURL = 'app/navi/new-moments-modal.html';
        var newMomentsModal = $uibModal.open({ scope: $scope, templateUrl: modURL, controller: 'NewMomentsModalCtrl', windowClass: 'new-moments-modal' });
    };
});

angular.module('naviApp').controller('NewMomentsModalCtrl', function($scope, $rootScope, $uibModal, $uibModalInstance, $search, $googleLocationService, $user) {
    $uibModalInstance.opened.then(function() {
        $scope.showDropdown = false;
        $scope.showDateDropdown = false;
    });

    $rootScope.$on("newEventModalLoaded", function() {
        $scope.newEventModalLoaded = true;
    });

    $rootScope.$on("newEventModalDismissed", function() {
        $scope.cancel();
    });

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };

    $scope.newEvent = function () {
        var modURL = 'app/event/new-event-modal.html';
        var newEventModal = $uibModal.open({ scope: $scope, templateUrl: modURL, controller: 'NewEventModalCtrl' });
    };

    $scope.search = function () {
        if ($scope.searchInput !== undefined && $scope.searchInput.length > 0) {
            var from, to;
            if ($scope.eventDateSearchEnabled) {
                if (angular.isDate($scope.fromDate)) {
                    from = $scope.fromDate;
                }
                if (angular.isDate($scope.toDate)) {
                    to = $scope.toDate;
                }
            }
            $search.searchForEvents($scope.searchInput, 0, 5, from, to).then(function (events) {
                $scope.events = events;
                angular.forEach($scope.events, function(event, key) {
                    $googleLocationService.getLocationByID(event.location.googleLocationID).then(function (googleLocation) {
                        if (googleLocation) {
                            event.googleLocation = { 'name': googleLocation.name };
                        }
                    }).catch(function (error) {

                    });
                });
                $scope.showDropdown = true;
            });
        } else {
            $scope.events = null;
            $scope.showDropdown = false;
        }
    };

    $scope.clickedSelectDatePicker = function () {
        $scope.showDateDropdown = !$scope.showDateDropdown;
    }

    $scope.toggledSearchDropdown = function (open) {
        $scope.showDropdown = open;
    };

    $scope.toggledDateDropdown = function (open) {
        $scope.showDateDropdown = open;

        if (!open && $scope.eventDateSearchEnabled) {
            $scope.search();
        }
    };

    $scope.showResultsIfExist = function () {
        if ($scope.events) {
            $scope.showDropdown = true;
        }
    };

    $scope.disableEventDateSearch = function () {
        $scope.fromDate = null;
        $scope.fromDateIsNotValid = false;
        $scope.toDate = null;
        $scope.toDateIsNotValid = false;
        $scope.eventDateSearchEnabled = false;
    }

    var init = function () {
        $scope.fromDate = null;
        $scope.toDate = null;
        $scope.eventDateSearchEnabled = false;
        $scope.format = 'dd.MM.yyyy';
        $scope.altInputFormats = ['M!/d!/yyyy'];
    };
    init();

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
        if (angular.isDate($scope.fromDate)) {
            if (angular.isDate($scope.toDate) && $scope.fromDate > $scope.toDate) {
                $scope.toDate = $scope.fromDate;
            }
            $scope.toDateIsNotValid = false;
            $scope.eventDateSearchEnabled = true;
        } else {
            if (!angular.isDate($scope.toDate)) {
                $scope.eventDateSearchEnabled = false;
            }
            $scope.toDateIsNotValid = true;
        }
    };

    $scope.toDateChanged = function () {
        if (angular.isDate($scope.toDate)) {
            if (angular.isDate($scope.fromDate) && $scope.fromDate > $scope.toDate) {
                $scope.fromDate = $scope.toDate;
            }
            $scope.fromDateIsNotValid = false;
            $scope.eventDateSearchEnabled = true;
        } else {
            if (!angular.isDate($scope.fromDate)) {
                $scope.eventDateSearchEnabled = false;
            }
            $scope.fromDateIsNotValid = true;
        }
    };
});

angular.module('naviApp').controller('NaviSearchCtrl', function($scope, $rootScope, $search, $state, $stateParams, $googleLocationService) {
    var init = function () {
        $scope.showDropdown = false;
        $scope.state = $state;
    }
    init();

    $scope.$watch('state.current.name', function(newValue, oldValue) {
        if ($state.is('search') && $stateParams.q) {
            $scope.searchInput = $stateParams.q
        }
    });

    var searchForEvents = function (q) {
        $search.searchForEvents(q, 0, 5, null, null).then(function (eventList) {
            $scope.events = eventList;
            angular.forEach(eventList, function(event, key) {
                $googleLocationService.getLocationByID(event.location.googleLocationID).then(function (googleLocation) {
                    if (googleLocation) {
                        event.googleLocation = { 'name': googleLocation.name };
                    }
                }).catch(function (error) {

                });
            });
            $scope.showDropdown = true;
        });
    };

    var searchForUsers = function (q) {
        $search.searchForUsers(q, 0, 5).then(function (userList) {
            angular.forEach(userList, function(user, key) {
                user.userImage = {url: (user.userImage ? user.userImage.url : 'http://placehold.it/50x50')};
            });
            $scope.users = userList;
            $scope.showDropdown = true;
        });
    };

    $scope.search = function () {
        if ($scope.searchInput.length > 0) {
            if ($state.is('search')) {
                $rootScope.$emit('searchOnSearchList', $scope.searchInput);
            } else {
                searchForUsers($scope.searchInput);
                searchForEvents($scope.searchInput);
            }
        } else {
            $scope.users = null;
            $scope.events = null;
            $scope.showDropdown = false;
        }
    };

    $scope.toggled = function (open) {
        $scope.showDropdown = open;
    };

    $scope.showResultsIfExist = function () {
        if ($scope.users || $scope.events) {
            if (!$state.is('search')) {
                $scope.showDropdown = true;
            }
        }
    };
});