angular.module('eventApp').controller('NewEventModalCtrl', function($scope, $rootScope, $state, $uibModalInstance, $googleLocationService, $geoLocationService, $event, $dateEncoder) {
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
                    alertService('danger', 'INDEX.NAVIGATION.NEW_EVENT_MODAL.REQUEST_ERROR');
                });
            } else {
                alertService('danger', 'INDEX.NAVIGATION.NEW_EVENT_MODAL.VALIDATION.LOCATION');
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

angular.module('eventApp').controller('EventCtrl', function($scope, eventData, userHasRightsToEdit, googleLocationData, eventPhotoData, $event, $fileHandler, $modalPhotoViewSlider) {
    $scope.getPhotos = function () {
        if(!$scope.photoPagination.last && !$scope.isLoadingNewPhotos) {
            $scope.isLoadingNewPhotos = true;
            $event.getPhotos($scope.event.id, $scope.photoPagination.nextPage, $scope.photoPagination.size).then(function (data) {
                if (data.content && data.content.length > 0) {
                    if (!$scope.event.photos) {
                        $scope.event.photos = [];
                    }
                    Array.prototype.unshift.apply($scope.event.photos, data.content);
                    $scope.photoPagination = { 'nextPage': (data.number + 1), 'size': data.size, 'last': data.last, 'totalElements': data.totalElements };
                } else {
                    $scope.showPhotoPlaceholder = true;
                }
                $scope.isLoadingNewPhotos = false;
            }).catch(function (error) {
                $scope.isLoadingNewPhotos = false;
            });
        }
    };

    var init = function () {
        $scope.photosToUpload = null;
        $scope.invalidPhotos = null;
        $scope.photoPagination = { 'nextPage': 1, 'size': 10, 'lastPage': true };
        $scope.isLoadingNewPhotos = false;
        $scope.photoEditButtonEnabled = false;
        $scope.photosSelected = 0;
        $scope.event = eventData;
        $scope.googleLocation = { 'name': googleLocationData.name, 'address': googleLocationData.formatted_address, 'url': googleLocationData.url };

        if (eventPhotoData) {
            if (eventPhotoData.content.length > 0) {
                $scope.event.photos = eventPhotoData.content;
            } else {
                $scope.showPhotoPlaceholder = true;
            }
            $scope.photoPagination = { 'nextPage': (eventPhotoData.number + 1), 'size': eventPhotoData.size, 'last': eventPhotoData.last, 'totalElements': eventPhotoData.totalElements };
        }

        if (userHasRightsToEdit) {
            $scope.userHasRightsToEdit = true;
        }
    };
    init();

    var handlePhotosToUpload = function () {
        if ($scope.photosToUpload && $scope.photosToUpload.length > 0) {
            if (!$scope.event.photos) {
                $scope.event.photos = [];
            }
            angular.forEach($scope.photosToUpload, function(photoToUpload, key) {
                $scope.event.photos.unshift(photoToUpload);
                var photoInScope = $scope.event.photos[0];
                photoInScope.pending = true;

                $event.uploadPhoto($scope.event.id, photoInScope.file).then(function (photo) {
                    photoInScope.pending = false;
                    if (photo) {
                        angular.extend(photoInScope, photo);
                    }
                }).catch(function (error) {
                    photoInScope.pending = false;
                });
            });
            $scope.photosToUpload = null;
        }
    };
    
    $scope.closeAlert = function () {
        $scope.invalidPhotos = null;
    };

    $scope.filesSelected = function (event) {
        var reader, file, _files = [], files = [], invalidFiles;

        reader = new FileReader();
        reader.onloadend = function(evt) {
            var image = {};
            image.file = file;
            image.name = file.name;
            image.img = evt.target.result;
            image.valid = true;

            files.push(image);
            loadFile();
        };

        var loadFile = function() {
            if (_files.length > 0) {
                file = _files[_files.length - 1];
                var sizeValid = $fileHandler.checkSize(file.size);
                var typeValid = $fileHandler.isTypeValid(file.type);

                if (sizeValid && typeValid) {
                    reader.readAsDataURL(file);
                    _files.pop();
                } else {
                    var image = {};
                    image.name = file.name;
                    image.valid = false;
                    image.sizeValid = sizeValid;
                    image.typeValid = typeValid;

                    if (!invalidFiles) {
                        invalidFiles = [];
                    }
                    invalidFiles.push(image);

                    _files.pop();
                    loadFile();
                }
            } else {
                if (!$scope.photosToUpload) {
                    $scope.photosToUpload = [];
                }
                $scope.photosToUpload = files;
                $scope.invalidPhotos = invalidFiles;
                $scope.$digest()
            }
        };

        if (event.target.files.length > 0) {
            angular.forEach(event.target.files, function(file, key) {
                _files.push(file);
            });
            loadFile();
        }
    };

    $scope.$watch('photosToUpload', function (newValue, oldValue) {
        if (newValue && newValue.length > 0) {
            handlePhotosToUpload();
        }
    });

    $scope.enableEdit = function () {
        $scope.photoEditButtonEnabled = !$scope.photoEditButtonEnabled;
    };

    $scope.deletePhotos = function () {
        $scope.photosSelected = 0;
        angular.forEach($scope.event.photos, function(photo, key) {
            if (photo.isSelected) {
                photo.pending = true;
                $event.deletePhoto($scope.event.id, photo.id).then(function (response) {
                    if (response) {
                        var currentIndex = $scope.event.photos.indexOf(photo);
                        $scope.event.photos.splice(currentIndex, 1);
                    }
                }).catch(function (error) {
                    photo.pending = false;
                });
            }
        });
    };

    $scope.photoAction = function (index) {
        if ($scope.photoEditButtonEnabled && $scope.event.photos[index].id) {
            if (!$scope.event.photos[index].isSelected) {
                $scope.event.photos[index].isSelected = true;
                $scope.photosSelected++;
            } else {
                $scope.event.photos[index].isSelected = false;
                $scope.photosSelected--;
            }
        } else if (!$scope.photoEditButtonEnabled) {
            $modalPhotoViewSlider.open({
                index: index,
                photos: $scope.event.photos,
                pagination: $scope.photoPagination,
                view: 'event',
                parentModelId: $scope.event.id
            });
        }
    };
});

angular.module('eventApp').controller('EditEventCtrl', function($scope, eventData, googleLocationData, geoLocationData, $event, $googleLocationService, $geoLocationService, $dateEncoder, $uibModal) {
    var init = function () {
        $scope.event = eventData;
        $scope.event.location.name = googleLocationData.name;
        $scope.selectedLocation = { 'place_id': eventData.location.googleLocationID };

        if (geoLocationData) {
            $scope.geoPosition = { 'latitude': geoLocationData.coords.latitude, 'longitude': geoLocationData.coords.longitude };
            $scope.geoLocationSearch = true;
        }
    };
    init();

    var initDateInput = function () {
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

    $scope.delete = function () {
        var modURL = 'app/event/delete-event-modal.html';
        var deleteEventModal = $uibModal.open({ scope: $scope, templateUrl: modURL, controller: 'DeleteEventCtrl', size: 'sm' });
    };

    $scope.save = function () {
        if ($scope.editEventForm.$valid) {
            if ($scope.selectedLocation) {
                var location = { 'googleLocationID': $scope.selectedLocation.place_id };
                var event = { 'id': $scope.event.id, 'name': $scope.event.name, 'description': $scope.event.description, 'startDate': $dateEncoder.formatDate($scope.event.startDate), 'endDate': $dateEncoder.formatDate($scope.event.endDate), 'multiDayEvent': $scope.event.multiDayEvent, 'location': location };
                $event.updateEvent(event).then(function (event) {
                    alertService('success', 'EVENT.EDIT.REQUEST_SUCCESS');
                }).catch(function (error) {
                    alertService('danger', 'EVENT.EDIT.REQUEST_ERROR');
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
        if ($scope.event.location.name && $scope.event.location.name.length > 0) {
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

        if ($scope.event && $scope.event.location && $scope.event.location.name && $scope.event.location.name.length > 0) {
            searchInput = $scope.event.location.name;
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
        $googleLocationService.getLocationsByTextSearch($scope.event.location.name).then(function (locations) {
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
        $scope.event.location.name = location.name;
        $scope.selectedLocation = location;
        $scope.showLocationDropdown = false;
    };
});

angular.module('eventApp').controller('DeleteEventCtrl', function($scope, $state, $event, $uibModalInstance) {
    $scope.delete = function() {
        $event.deleteEvent($scope.event.id).then(function () {
            $scope.cancel();
            $state.go('/');
        });
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
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
        updateEvent : function(event) {
            var url = '/api/v1/resource/event/' + event.id;
            return $http.put(url, JSON.stringify(event))
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
        deleteEvent : function(id) {
            var url = '/api/v1/resource/event/' + id;
            return $http.delete(url)
                .then(
                    function (response) {
                        if (response.status === 200) {
                            return true
                        } else {
                            return false;
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
            var url = '/api/v1/resource/event/' + id + "/photo" + '?page=' + page + '&size=' + size;
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
        uploadPhoto : function(id, file) {
            var url = '/api/v1/resource/event/' + id + "/photo";
            var fileData = new FormData();
            fileData.append('file', file);

            return $http.post(url, fileData, {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                })
                .then(
                    function (response) {
                        if (response.status === 200 && response.data) {
                            return response.data;
                        } else {
                            return null;
                        }
                },
                function (httpError) {
                    throw { 'status': httpError.status , 'data': httpError.data };
                });
        },
        deletePhoto : function(eventId, photoId) {
            var url = '/api/v1/resource/event/' + eventId + "/photo/" + photoId;

            return $http.delete(url)
                .then(
                    function (response) {
                        if (response.status === 200) {
                            return true;
                        } else {
                            return false;
                        }
                },
                function (httpError) {
                    throw { 'status': httpError.status , 'data': httpError.data };
                });
        }
    };
});

angular.module('eventApp').directive('imageDropzone', function($fileHandler) {
    return {
      restrict: 'A',
      scope: {
        files: '=',
        invalidFiles: '='
      },
      link: function(scope, element, attrs) {
        var processDragOverOrEnter = function(event) {
            if (event != null) {
                event.preventDefault();
            }
            event.originalEvent.effectAllowed = 'copy';
            return false;
        };

        element.bind('dragover', processDragOverOrEnter);
        element.bind('dragenter', processDragOverOrEnter);

        return element.bind('drop', function(event) {
            var reader, file, _files = [], files = [], invalidFiles;

            reader = new FileReader();
            reader.onloadend = function(evt) {
                var image = {};
                image.file = file;
                image.name = file.name;
                image.img = evt.target.result;
                image.valid = true;

                files.push(image);
                loadFile();
            };

            var loadFile = function() {
                if (_files.length > 0) {
                    file = _files[_files.length - 1];
                    var sizeValid = $fileHandler.checkSize(file.size);
                    var typeValid = $fileHandler.isTypeValid(file.type);

                    if (sizeValid && typeValid) {
                        reader.readAsDataURL(file);
                        _files.pop();
                    } else {
                        var image = {};
                        image.name = file.name;
                        image.valid = false;
                        image.sizeValid = sizeValid;
                        image.typeValid = typeValid;

                        if (!invalidFiles) {
                            invalidFiles = [];
                        }
                        invalidFiles.push(image);

                        _files.pop();
                        loadFile();
                    }
                } else {
                    scope.$apply(function() {
                        scope.files = files;
                        scope.invalidFiles = invalidFiles;
                    });
                }
            };

            if (event != null) {
                event.preventDefault();
                if (event.originalEvent.dataTransfer.files.length > 0) {
                    angular.forEach(event.originalEvent.dataTransfer.files, function(file, key) {
                        _files.push(file);
                    });
                    loadFile();
                }
            }
            return false;
        });
      }
    };
  });