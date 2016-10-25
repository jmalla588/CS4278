(function() {
    'use strict';
    angular
        .module('midtermApplicationApp')
        .factory('Submission', Submission);

    Submission.$inject = ['$resource', 'DateUtils'];

    function Submission ($resource, DateUtils) {
        var resourceUrl =  'api/submissions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertDateTimeFromServer(data.date);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
