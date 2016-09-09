angular.module('fileHandler').service('$fileHandler', function() {
    var validMimeTypes = ['image/png', 'image/jpeg'];
    var maxFileSize = 5;

    return {
        checkSize: function (size) {
            var _ref;
            if (((_ref = maxFileSize) === (void 0) || _ref === '') || (size / 1024) / 1024 < maxFileSize) {
                return true;
            } else {
                return false;
            }
        },
        isTypeValid: function (type) {
            if ((validMimeTypes === (void 0) || validMimeTypes === '') || validMimeTypes.indexOf(type) > -1) {
                return true;
            } else {
                return false;
            }
        }
    };
});