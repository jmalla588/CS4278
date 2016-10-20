(function() {
    'use strict';

    angular
        .module('myapplicationApp')
        .controller('StudentDetailController', StudentDetailController);

    StudentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Student', 'PreferredContact', 'School', 'Dormitory'];

    function StudentDetailController($scope, $rootScope, $stateParams, previousState, entity, Student, PreferredContact, School, Dormitory) {
        var vm = this;

        vm.student = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myapplicationApp:studentUpdate', function(event, result) {
            vm.student = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
