(function() {
    'use strict';

    angular
        .module('midtermApplicationApp')
        .factory('InstructorSearch', InstructorSearch);

    InstructorSearch.$inject = ['$resource'];

    function InstructorSearch($resource) {
        var resourceUrl =  'api/_search/instructors/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
