(function() {
    'use strict';

    angular
        .module('midtermApplicationApp')
        .controller('GradeDetailController', GradeDetailController);

    GradeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Grade', 'Submission', 'Instructor'];

    function GradeDetailController($scope, $rootScope, $stateParams, previousState, entity, Grade, Submission, Instructor) {
        var vm = this;

        vm.grade = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('midtermApplicationApp:gradeUpdate', function(event, result) {
            vm.grade = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
