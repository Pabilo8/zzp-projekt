// Utility functions and services
var app = angular.module('taskManagerApp', []);

// Shared utilities
app.factory('AppUtils', function () {
    return {
        checkDuplicate: function (collection, property, value, excludeId) {
            if (!value) return false;
            return collection.some(function (item) {
                if (excludeId && item.id === excludeId) return false;
                return item[property].toLowerCase() === value.toLowerCase();
            });
        },
        initializeModal: function (modalId) {
            return new bootstrap.Modal(document.getElementById(modalId));
        },
        openModal: function (modal, data, scope) {
            Object.assign(scope, data);
            modal.show();
        },
        submitForm: function (event, formAction, id) {
            event.preventDefault();
            const form = event.target;
            form.action = formAction + id;
            form.submit();
        }
    };
});

// CategoryController
app.controller('CategoryController', function ($scope, $http, $timeout, AppUtils) {
    $scope.newCategory = '';
    $scope.editCategoryId = '';
    $scope.editCategoryName = '';
    $scope.isDuplicateAdd = false;
    $scope.isDuplicateEdit = false;

    var editModal = AppUtils.initializeModal('editModal');

    $scope.checkAddDuplicate = function () {
        $scope.isDuplicateAdd = AppUtils.checkDuplicate($scope.categories, 'name', $scope.newCategory);
    };

    $scope.checkEditDuplicate = function () {
        $scope.isDuplicateEdit = AppUtils.checkDuplicate($scope.categories, 'name', $scope.editCategoryName, $scope.editCategoryId);
    };

    $scope.openEditModal = function (category) {
        AppUtils.openModal(editModal, {
            editCategoryId: category.id,
            editCategoryName: category.name,
            isDuplicateEdit: false
        }, $scope);
    };

    $scope.updateCategory = function (event) {
        if ($scope.isDuplicateEdit) {
            return event.preventDefault();
        }
        AppUtils.submitForm(event, event.target.action, $scope.editCategoryId);
    };

    $scope.deleteCategory = function (event, category) {
        AppUtils.submitForm(event, event.target.action, category.id);
    };
});

// TaskController
app.controller('TaskController', function ($scope, $timeout, AppUtils) {
    $scope.newTask = {title: '', description: '', categoryId: ''};
    $scope.searchText = '';
    $scope.categoryFilter = '';
    $scope.statusFilter = '';

    var editModal = AppUtils.initializeModal('editTaskModal');

    $scope.filterTasks = function (task) {
        const matchesCategory = !$scope.categoryFilter || task.category.id.toString() === $scope.categoryFilter;
        const matchesStatus = !$scope.statusFilter || (task.status && task.status.id.toString() === $scope.statusFilter);
        //Check for matching search text in title or description
        const matchesSearchText = !$scope.searchText ||
                (task.title && task.title.toLowerCase().includes($scope.searchText.toLowerCase())) ||
                (task.description && task.description.toLowerCase().includes($scope.searchText.toLowerCase()));

        return matchesCategory && matchesStatus && matchesSearchText;
    };

    $scope.getStatusBadgeClass = function (status) {
        const classes = {
            new: 'bg-secondary',
            in_progress: 'bg-warning',
            completed: 'bg-success'
        };
        return classes[status.name.toLowerCase()] || 'bg-secondary';
    };

    $scope.checkAddDuplicate = function () {
        $scope.isDuplicateAdd = AppUtils.checkDuplicate($scope.tasks, 'title', $scope.newTask.title, null, $scope.newTask.categoryId);
    };

    $scope.openEditModal = function (task) {
        AppUtils.openModal(editModal, {
            editTaskId: task.id,
            editTaskTitle: task.title,
            editTaskDescription: task.description,
            editTaskCategoryId: task.category.id.toString(),
            isDuplicateEdit: false
        }, $scope);
    };

    $scope.updateTask = function (event) {
        if ($scope.isDuplicateEdit) {
            return event.preventDefault();
        }
        AppUtils.submitForm(event, event.target.action, $scope.editTaskId);
    };

    $scope.deleteTask = function (event, task) {
        if (confirm(`Czy na pewno chcesz usunąć zadanie "${task.title}"?`)) {
            AppUtils.submitForm(event, event.target.action, task.id);
        }
    };

    // Update the status of a task
    $scope.updateTaskStatus = function (event, task, newStatus) {
        event.preventDefault();

        // Check if the new status is different from the current one
        if (task.status && task.status.id === newStatus.id) {
            alert('Status zadania już jest ustawiony na wybrany.');
            return;
        }

        // Update the task's status
        AppUtils.submitForm(event, event.target.action, task.id);
    };
});

// StatusController
app.controller('StatusController', function ($scope, $timeout, AppUtils) {
    $scope.newStatus = '';
    $scope.editStatusId = '';
    $scope.editStatusName = '';
    $scope.isDuplicateAdd = false;
    $scope.isDuplicateEdit = false;

    var editModal = AppUtils.initializeModal('editModal');

    $scope.checkAddDuplicate = function () {
        $scope.isDuplicateAdd = AppUtils.checkDuplicate($scope.statuses, 'name', $scope.newStatus);
    };

    $scope.checkEditDuplicate = function () {
        $scope.isDuplicateEdit = AppUtils.checkDuplicate($scope.statuses, 'name', $scope.editStatusName, $scope.editStatusId);
    };

    $scope.openEditModal = function (status) {
        AppUtils.openModal(editModal, {
            editStatusId: status.id,
            editStatusName: status.name,
            isDuplicateEdit: false
        }, $scope);
    };

    $scope.updateStatus = function (event) {
        if ($scope.isDuplicateEdit) {
            return event.preventDefault();
        }
        AppUtils.submitForm(event, event.target.action, $scope.editStatusId);
    };

    $scope.deleteStatus = function (event, status) {
        if (confirm(`Czy na pewno chcesz usunąć status "${status.name}"?`)) {
            AppUtils.submitForm(event, event.target.action, status.id);
        }
    };
});

// Theme management (optional: extract into a separate file)
document.addEventListener('DOMContentLoaded', () => {
    const htmlElement = document.documentElement;
    const themeButtons = document.querySelectorAll('[data-bs-theme-value]');

    const setTheme = (theme) => {
        if (theme === 'auto') {
            const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
            htmlElement.setAttribute('data-bs-theme', prefersDark ? 'dark' : 'light');
        } else {
            htmlElement.setAttribute('data-bs-theme', theme);
        }
        localStorage.setItem('bsTheme', theme);
    };

    themeButtons.forEach(button => {
        button.addEventListener('click', () => {
            const theme = button.getAttribute('data-bs-theme-value');
            setTheme(theme);
        });
    });

    const storedTheme = localStorage.getItem('bsTheme') || 'auto';
    setTheme(storedTheme);
});