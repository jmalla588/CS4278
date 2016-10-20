(function() {
    'use strict';

    angular
        .module('myapplicationApp')
        .controller('SchoolController', SchoolController);

    SchoolController.$inject = ['$scope', '$state', 'School'];

    function SchoolController ($scope, $state, School) {
        var vm = this;
        
        vm.schools = [];

        loadAll();

        function loadAll() {
            School.query(function(result) {
                vm.schools = result;
            });
        }
    }
})();
