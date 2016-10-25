'use strict';

describe('Controller Tests', function() {

    describe('Grade Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockGrade, MockSubmission, MockInstructor;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockGrade = jasmine.createSpy('MockGrade');
            MockSubmission = jasmine.createSpy('MockSubmission');
            MockInstructor = jasmine.createSpy('MockInstructor');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Grade': MockGrade,
                'Submission': MockSubmission,
                'Instructor': MockInstructor
            };
            createController = function() {
                $injector.get('$controller')("GradeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'midtermApplicationApp:gradeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
