angular.module('googleLocationServices').service('$googleLocationService', function($http) {
    var apiKey = 'AIzaSyAMSzqPNYowT29PJOWWyGS8XsD8EiNRelg';

    return {
        getLocationByID: function (id) {
            var url = '/api/google/maps/places/details/json?placeid=' + id + '&key=' + apiKey;
            return $http.get(url)
                .then(
                    function (response) {
                        if (response.data.result) {
                            return response.data.result;
                        } else {
                            return [];
                        }
                },
                function (httpError) {
                    throw httpError.status + " : " + httpError.data;
                });
        },
        getLocationsByTextSearch: function (searchInput) {
            var url = '/api/google/maps/places/textsearch/json?query=' + escape(searchInput) + '&key=' + apiKey;
            return $http.get(url)
                .then(
                    function (response) {
                        if (response.data.results) {
                            return response.data.results;
                        } else {
                            return [];
                        }
                },
                function (httpError) {
                    throw httpError.status + " : " + httpError.data;
                });
        },
        getLocationsByGeoLocation: function (geoLocation, searchInput) {
            var url = '/api/google/maps/places/nearbysearch/json?location=' + geoLocation.latitude + ',' + geoLocation.longitude + '&radius=20000' + '&key=' + apiKey;
            if (searchInput) {
                url += '&keyword=' + escape(searchInput);
            }

            return $http.get(url)
                .then(
                    function (response) {
                        if (response.data.results) {
                            return response.data.results;
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