(function() {
    'use strict';

    angular
        .module('midtermApplicationApp')
        .controller('GradeDeleteController',GradeDeleteController);

    GradeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Grade'];

    function GradeDeleteController($uibModalInstance, entity, Grade) {
        var vm = this;

        vm.grade = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Grade.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
