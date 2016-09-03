angular.module('naviApp').controller('NaviActionsCtrl', function($scope, $rootScope, $state, $auth) {
    $scope.logout = function () {
        $auth.logout();
        $state.go('/');
    };
});

angular.module('naviApp').controller('NaviNewMomentsCtrl', function($scope, $uibModal) {
    $scope.openNewMomentsModal = function () {
        var modURL = 'app/navi/new-moments-modal.html';
        var newMomentsModal = $uibModal.open({ scope: $scope, templateUrl: modURL, controller: 'NewMomentsModalCtrl', windowClass: 'new-moments-modal' });
    };
});

angular.module('naviApp').controller('NewMomentsModalCtrl', function($scope, $rootScope, $uibModal, $uibModalInstance, $search, $user) {
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
            $search.searchForEvents($scope.searchInput, 0, 5).then(function (events) {
                $scope.events = events;
                $scope.showDropdown = true;
            });
            //$scope.events = [{'id': '123123', 'name': 'Event1', 'location': 'Sydney, Pavillon', 'date': '18.09.1978'}, {'id': '345435', 'name': 'Event2', 'location': 'Düsseldorf, Sub', 'date': '20.02.2016'}]
            $scope.showDropdown = true;
        } else {
            $scope.events = null;
            $scope.showDropdown = false;
        }
    };

    $scope.getUsersEvents = function () {
        if ($scope.searchInput === undefined || $scope.searchInput.length == 0) {
            /*
            $user.getEvents('', 0, 5).then(function (events) {
                $scope.events = events;
                $scope.showDropdown = true;
            });
            */
        }
    }

    $scope.clickedSelectDatePicker = function () {
        $scope.showDateDropdown = !$scope.showDateDropdown;
    }

    $scope.toggledSearchDropdown = function (open) {
        $scope.showDropdown = open;
    };

    $scope.toggledDateDropdown = function (open) {
        $scope.showDateDropdown = open;
    };

    $scope.showResultsIfExist = function () {
        if ($scope.events) {
            $scope.showDropdown = true;
        }
    };

    $scope.disableEventDateSearch = function () {
        $scope.startDate = null;
        $scope.endDate = null;
        $scope.eventDateSearchEnabled = false;
    }

    var init = function () {
        $scope.startDate = null;
        $scope.endDate = null;
        $scope.format = 'dd.MM.yyyy';
        $scope.altInputFormats = ['M!/d!/yyyy'];
    };
    init();

    $scope.startDatePopup = {
        opened: false
    };

    $scope.endDatePopup = {
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

    $scope.openStartDatePopup = function () {
        $scope.startDatePopup.opened = true;
    };

    $scope.openEndDatePopup = function () {
        $scope.endDatePopup.opened = true;
    };

    $scope.startDateChanged = function () {
        if (angular.isDate($scope.startDate)) {
            if ($scope.startDate > $scope.endDate) {
                $scope.endDate = $scope.startDate;
            }
            $scope.eventDateSearchEnabled = true;
        }
    };

    $scope.endDateChanged = function () {
        if (angular.isDate($scope.endDate)) {
            if ($scope.startDate > $scope.endDate) {
                $scope.startDate = $scope.endDate;
            }
            $scope.eventDateSearchEnabled = true;
        }
    };

    var isDateValid = function (date) {
        var dateTime = date;

        if (dateTime === null) return false;

        var day = dateTime.getDate();
        var month = dateTime.getMonth();
        var year = dateTime.getFullYear();
        var composedDate = new Date(year, month, day);

        return composedDate.getDate() === day && composedDate.getMonth() === month && composedDate.getFullYear() === year;
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
        $search.searchForEvents(q, 0, 5).then(function (eventList) {
            $scope.events = eventList;
            angular.forEach(eventList, function(event, key) {
                $googleLocationService.getLocationByID(event.location.googleLocationID).then(function (googleLocation) {
                    if (googleLocation) {
                        $scope.events[key].googleLocation = { 'name': googleLocation.name };
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
                userList[key].userImage = {url: (user.userImage ? user.userImage.url : 'http://placehold.it/50x50')};
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