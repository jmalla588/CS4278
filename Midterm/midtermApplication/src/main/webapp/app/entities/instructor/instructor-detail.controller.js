(function() {
    'use strict';

    angular
        .module('midtermApplicationApp')
        .controller('InstructorDetailController', InstructorDetailController);

    InstructorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Instructor'];

    function InstructorDetailController($scope, $rootScope, $stateParams, previousState, entity, Instructor) {
        var vm = this;

        vm.instructor = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('midtermApplicationApp:instructorUpdate', function(event, result) {
            vm.instructor = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
