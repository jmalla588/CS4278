'use strict';

describe('Controller Tests', function() {

    describe('Submission Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockSubmission, MockHomework, MockGrade, MockStudent;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockSubmission = jasmine.createSpy('MockSubmission');
            MockHomework = jasmine.createSpy('MockHomework');
            MockGrade = jasmine.createSpy('MockGrade');
            MockStudent = jasmine.createSpy('MockStudent');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Submission': MockSubmission,
                'Homework': MockHomework,
                'Grade': MockGrade,
                'Student': MockStudent
            };
            createController = function() {
                $injector.get('$controller')("SubmissionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'midtermApplicationApp:submissionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
