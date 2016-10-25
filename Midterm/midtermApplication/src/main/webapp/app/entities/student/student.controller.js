(function() {
    'use strict';

    angular
        .module('midtermApplicationApp')
        .controller('StudentController', StudentController);

    StudentController.$inject = ['$scope', '$state', 'Student', 'StudentSearch'];

    function StudentController ($scope, $state, Student, StudentSearch) {
        var vm = this;
        
        vm.students = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Student.query(function(result) {
                vm.students = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            StudentSearch.query({query: vm.searchQuery}, function(result) {
                vm.students = result;
            });
        }    }
})();
