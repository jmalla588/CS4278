(function() {
    'use strict';

    angular
        .module('midtermApplicationApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('submission', {
            parent: 'entity',
            url: '/submission',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Submissions'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/submission/submissions.html',
                    controller: 'SubmissionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('submission-detail', {
            parent: 'entity',
            url: '/submission/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Submission'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/submission/submission-detail.html',
                    controller: 'SubmissionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Submission', function($stateParams, Submission) {
                    return Submission.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'submission',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('submission-detail.edit', {
            parent: 'submission-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/submission/submission-dialog.html',
                    controller: 'SubmissionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Submission', function(Submission) {
                            return Submission.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('submission.new', {
            parent: 'submission',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/submission/submission-dialog.html',
                    controller: 'SubmissionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('submission', null, { reload: 'submission' });
                }, function() {
                    $state.go('submission');
                });
            }]
        })
        .state('submission.edit', {
            parent: 'submission',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/submission/submission-dialog.html',
                    controller: 'SubmissionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Submission', function(Submission) {
                            return Submission.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('submission', null, { reload: 'submission' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('submission.delete', {
            parent: 'submission',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/submission/submission-delete-dialog.html',
                    controller: 'SubmissionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Submission', function(Submission) {
                            return Submission.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('submission', null, { reload: 'submission' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
