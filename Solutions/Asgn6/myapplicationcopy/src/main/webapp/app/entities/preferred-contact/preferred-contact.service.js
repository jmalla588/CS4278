(function() {
    'use strict';
    angular
        .module('myapplicationApp')
        .factory('PreferredContact', PreferredContact);

    PreferredContact.$inject = ['$resource'];

    function PreferredContact ($resource) {
        var resourceUrl =  'api/preferred-contacts/:id';

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
