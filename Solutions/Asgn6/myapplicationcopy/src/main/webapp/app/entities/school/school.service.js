(function() {
    'use strict';
    angular
        .module('myapplicationApp')
        .factory('School', School);

    School.$inject = ['$resource'];

    function School ($resource) {
        var resourceUrl =  'api/schools/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
