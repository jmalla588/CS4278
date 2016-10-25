(function() {
    'use strict';

    angular
        .module('midtermApplicationApp')
        .controller('InstructorDialogController', InstructorDialogController);

    InstructorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Instructor'];

    function InstructorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Instructor) {
        var vm = this;

        vm.instructor = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.instructor.id !== null) {
                Instructor.update(vm.instructor, onSaveSuccess, onSaveError);
            } else {
                Instructor.save(vm.instructor, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('midtermApplicationApp:instructorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
