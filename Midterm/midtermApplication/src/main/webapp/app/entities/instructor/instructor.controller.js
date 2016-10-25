(function() {
    'use strict';

    angular
        .module('midtermApplicationApp')
        .controller('InstructorController', InstructorController);

    InstructorController.$inject = ['$scope', '$state', 'Instructor', 'InstructorSearch'];

    function InstructorController ($scope, $state, Instructor, InstructorSearch) {
        var vm = this;
        
        vm.instructors = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Instructor.query(function(result) {
                vm.instructors = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            InstructorSearch.query({query: vm.searchQuery}, function(result) {
                vm.instructors = result;
            });
        }    }
})();
