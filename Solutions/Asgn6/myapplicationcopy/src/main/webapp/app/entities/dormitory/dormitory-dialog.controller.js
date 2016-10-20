(function() {
    'use strict';

    angular
        .module('myapplicationApp')
        .controller('DormitoryDialogController', DormitoryDialogController);

    DormitoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Dormitory', 'Student'];

    function DormitoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Dormitory, Student) {
        var vm = this;

        vm.dormitory = entity;
        vm.clear = clear;
        vm.save = save;
        vm.students = Student.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.dormitory.id !== null) {
                Dormitory.update(vm.dormitory, onSaveSuccess, onSaveError);
            } else {
                Dormitory.save(vm.dormitory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myapplicationApp:dormitoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
