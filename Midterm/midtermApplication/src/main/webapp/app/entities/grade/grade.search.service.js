(function() {
    'use strict';

    angular
        .module('midtermApplicationApp')
        .factory('GradeSearch', GradeSearch);

    GradeSearch.$inject = ['$resource'];

    function GradeSearch($resource) {
        var resourceUrl =  'api/_search/grades/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
