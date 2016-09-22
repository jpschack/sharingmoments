angular.module('fileReader').service('$fileReader', function($q) {
    var validMimeTypes = ['image/png', 'image/jpeg'];
    var maxFileSize = 5;

    var checkSize = function (size) {
        var _ref;
        if (((_ref = maxFileSize) === (void 0) || _ref === '') || (size / 1024) / 1024 < maxFileSize) {
            return true;
        } else {
            return false;
        }
    };

    var isTypeValid = function (type) {
        if ((validMimeTypes === (void 0) || validMimeTypes === '') || validMimeTypes.indexOf(type) > -1) {
            return true;
        } else {
            return false;
        }
    };

    var loadFile = function (file) {
        var reader = new FileReader();
        var deferred = $q.defer();

        reader.onloadend = function(evt) {
            var image = { 'name': file.name, 'file': file, 'img': evt.target.result, 'valid': true, 'sizeValid': sizeValid, 'typeValid': typeValid };
            deferred.resolve(image);
        };

        reader.onerror = function(evt) {
            deferred.reject(evt);
        };

        var sizeValid = checkSize(file.size);
        var typeValid = isTypeValid(file.type);

        if (sizeValid && typeValid) {
            reader.readAsDataURL(file);
        } else {
            var image = { 'name': file.name, 'valid': false, 'sizeValid': sizeValid, 'typeValid': typeValid };
            deferred.resolve(image);
        }

        return deferred.promise;
    };

    return { loadFile, checkSize, isTypeValid };
});