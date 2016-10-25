(function() {
    'use strict';

    angular
        .module('midtermApplicationApp')
        .controller('InstructorDeleteController',InstructorDeleteController);

    InstructorDeleteController.$inject = ['$uibModalInstance', 'entity', 'Instructor'];

    function InstructorDeleteController($uibModalInstance, entity, Instructor) {
        var vm = this;

        vm.instructor = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Instructor.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
