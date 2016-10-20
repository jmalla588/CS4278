(function() {
    'use strict';

    angular
        .module('myapplicationApp')
        .controller('SchoolDeleteController',SchoolDeleteController);

    SchoolDeleteController.$inject = ['$uibModalInstance', 'entity', 'School'];

    function SchoolDeleteController($uibModalInstance, entity, School) {
        var vm = this;

        vm.school = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            School.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
