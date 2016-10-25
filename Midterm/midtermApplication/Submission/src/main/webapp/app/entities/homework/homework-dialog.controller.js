(function() {
    'use strict';

    angular
        .module('midtermApplicationApp')
        .controller('HomeworkDialogController', HomeworkDialogController);

    HomeworkDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Homework'];

    function HomeworkDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Homework) {
        var vm = this;

        vm.homework = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.homework.id !== null) {
                Homework.update(vm.homework, onSaveSuccess, onSaveError);
            } else {
                Homework.save(vm.homework, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('midtermApplicationApp:homeworkUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.due = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
