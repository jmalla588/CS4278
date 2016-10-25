(function() {
    'use strict';
    angular
        .module('midtermApplicationApp')
        .factory('Grade', Grade);

    Grade.$inject = ['$resource'];

    function Grade ($resource) {
        var resourceUrl =  'api/grades/:id';

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
