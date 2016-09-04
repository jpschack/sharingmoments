angular.module('eventApp').controller('NewEventModalCtrl', function($scope, $rootScope, $state, $uibModalInstance, $googleLocationService, $geoLocationService, $event, $translate, $dateEncoder) {
    $uibModalInstance.opened.then(function() {
        $scope.showLocationDropdown = false;
        $rootScope.$emit("newEventModalLoaded", {});

        $geoLocationService.getCurrentPosition().then(function (position) {
            $scope.geoPosition = { 'latitude': position.coords.latitude, 'longitude': position.coords.longitude };
            $scope.geoLocationSearch = true;
        });
    });

    $uibModalInstance.result.catch(function () {
        $rootScope.$emit("newEventModalDismissed", {});
    });

    var initDateInput = function () {
        $scope.event = {};
        $scope.event.startDate = new Date();
        $scope.event.endDate = new Date();
        $scope.event.multiDayEvent = false;
        $scope.format = 'dd.MM.yyyy';
        $scope.altInputFormats = ['M!/d!/yyyy'];
    };
    initDateInput();

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
        if ($scope.event.startDate > $scope.event.endDate) {
            $scope.event.endDate = $scope.event.startDate;
        }
    };

    $scope.endDateChanged = function () {
        if ($scope.event.startDate > $scope.event.endDate) {
            $scope.event.startDate = $scope.event.endDate;
        }
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };

    $scope.createEvent = function () {
        if ($scope.newEventForm.$valid) {
            if ($scope.selectedLocation) {
                var location = { 'googleLocationID': $scope.selectedLocation.place_id };
                var event = { 'name': $scope.event.name, 'description': $scope.event.description, 'startDate': $dateEncoder.formatDate($scope.event.startDate), 'endDate': $dateEncoder.formatDate($scope.event.endDate), 'multiDayEvent': $scope.event.multiDayEvent, 'location': location };
                $event.createEvent(event).then(function (event) {
                    $scope.cancel();
                    $state.go('event', { 'id': event.id });
                }).catch(function (error) {
                    $translate('INDEX.NAVIGATION.NEW_EVENT_MODAL.REQUEST_ERROR')
                    .then(function (translatedValue) {
                        alertService('danger', translatedValue);
                    });
                });
            } else {
                $translate('INDEX.NAVIGATION.NEW_EVENT_MODAL.VALIDATION.LOCATION')
                .then(function (translatedValue) {
                    alertService('danger', translatedValue);
                });
            }
        }
    };

    var alertService = function (type, msg) {
        $scope.alert = { 'type': type, 'msg': msg };
    };
    
    $scope.closeAlert = function () {
        $scope.alert = undefined;
    };

    $scope.searchForLocations = function () {
        if ($scope.event.location.length > 0) {
            if ($scope.geoLocationSearch) {
                getLocationsByGeoLocation();
            } else {
                getLocationsByTextSearch();
            }
        } else {
            getLocationsByGeoLocation();
        }
    };

    var getLocationsByGeoLocation = function () {
        var searchInput = null;

        if ($scope.event && $scope.event.location && $scope.event.location.length > 0) {
            searchInput = $scope.event.location;
        }

        $googleLocationService.getLocationsByGeoLocation($scope.geoPosition, searchInput).then(function (locations) {
            if (locations.length) {
                $scope.locationSuggestions = locations;
                $scope.showLocationDropdown = true;
            } else {
                $scope.geoLocationSearch = false;
                $scope.showLocationDropdown = false;

                if ($scope.event && $scope.event.location.length > 0) {
                    getLocationsByTextSearch();
                }
            }
        });
    };

    var getLocationsByTextSearch = function () {
        $googleLocationService.getLocationsByTextSearch($scope.event.location).then(function (locations) {
            if (locations.length) {
                $scope.locationSuggestions = locations;
                $scope.showLocationDropdown = true;
            } else {
                $scope.showLocationDropdown = false;
            }
        });
    };
    
    $scope.toggled = function (open) {
        $scope.showLocationDropdown = open;
    };

    $scope.clickedLocationSearchInput = function () {
        if ($scope.locationSuggestions) {
            $scope.showLocationDropdown = true;
        } else {
            if ($scope.geoPosition) {
                getLocationsByGeoLocation();
            }
        }
    };

    $scope.selectLocation = function (location) {
        $scope.event.location = location.name;
        $scope.selectedLocation = location;
        $scope.showLocationDropdown = false;
    };
});

angular.module('eventApp').controller('EventCtrl', function($scope, $state, $stateParams, $event, $googleLocationService) {
    $scope.image = null
    $scope.imageFileName = ''

    var init = function () {
        $event.getEventById($stateParams.id).then(function (event) {
            $scope.event = event;
            
            $googleLocationService.getLocationByID(event.location.googleLocationID).then(function (googleLocation) {
                if (googleLocation) {
                    $scope.googleLocation = { 'name': googleLocation.name, 'address': googleLocation.formatted_address, 'url': googleLocation.url };
                }
            }).catch(function (error) {

            });
        }).catch(function (error) {

        });
    }
    init();
});

angular.module('eventApp').service('$event', function ($http) {
    var dateParsing = function (json) {
        if (json.startDate) {
            json.startDate = new Date(json.startDate);
        }
        if (json.endDate) {
            json.endDate = new Date(json.endDate);
        }
        if (json.createdAt) {
            json.createdAt = new Date(json.createdAt);
        }
        if (json.updatedAt) {
            json.updatedAt = new Date(json.updatedAt);
        }
        return json;
    };

    return {
        createEvent : function(event) {
            var url = '/api/v1/resource/event'
            return $http.post(url, JSON.stringify(event))
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
        getEventById : function(id) {
            var url = '/api/v1/resource/event/' + id;
            return $http.get(url)
                .then(
                    function (response) {
                        if (response.data) {
                            
                            return dateParsing(response.data);
                        } else {
                            return null;
                        }
                },
                function (httpError) {
                    throw { 'status': httpError.status , 'data': httpError.data };
                });
        },
        getPhotos : function(id, page, size) {
            var url = '/api/v1/resource/event/' + id + "/photos" + '?page=' + page + '&size=' + size;;
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

angular.module('eventApp').directive('imageDropzone', function() {
    return {
      restrict: 'A',
      scope: {},
      link: function(scope, element, attrs) {
        var processDragOverOrEnter = function(event) {
          if (event != null) {
            event.preventDefault();
          }
          event.originalEvent.effectAllowed = 'copy';
          return false;
        };
        var validMimeTypes = ['image/png', 'image/jpeg'];
        var maxFileSize = 5;
        var checkSize = function(size) {
          var _ref;
          if (((_ref = maxFileSize) === (void 0) || _ref === '') || (size / 1024) / 1024 < maxFileSize) {
            return true;
          } else {
            alert("File must be smaller than " + maxFileSize + " MB");
            return false;
          }
        };
        var isTypeValid = function(type) {
          if ((validMimeTypes === (void 0) || validMimeTypes === '') || validMimeTypes.indexOf(type) > -1) {
            return true;
          } else {
            alert("Invalid file type. File must be one of following types " + validMimeTypes);
            return false;
          }
        };

        element.bind('dragover', processDragOverOrEnter);
        element.bind('dragenter', processDragOverOrEnter);

        return element.bind('drop', function(event) {
          var file, name, reader, size, type;
          if (event != null) {
            event.preventDefault();
          }
          reader = new FileReader();
          reader.onload = function(evt) {
            if (checkSize(size) && isTypeValid(type)) {
              return scope.$apply(function() {
                scope.file = evt.target.result;
                if (angular.isString(scope.fileName)) {
                  return scope.fileName = name;
                }
              });
            }
          };
          file = event.originalEvent.files[0];
          name = file.name;
          type = file.type;
          size = file.size;
          reader.readAsDataURL(file);
          return false;
        });
      }
    };
  });