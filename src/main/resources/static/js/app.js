// noinspection t

var app = angular.module('taskManagerApp', []);

app.controller('CategoryController', function ($scope, $http, $timeout) {
    // Dane zostały już wstrzyknięte przez Thymeleaf

    // Inicjalizacja obiektu nowej kategorii
    $scope.newCategory = '';
    $scope.editCategoryId = '';
    $scope.editCategoryName = '';

    // Zmienne do kontrolowania komunikatów o duplikatach
    $scope.isDuplicateAdd = false;
    $scope.isDuplicateEdit = false;

    var editModal;

    // Inicjalizacja modala po załadowaniu DOM z timeoutem
    $timeout(function () {
        editModal = new bootstrap.Modal(document.getElementById('editModal'));
    }, 500);

    // Funkcja sprawdzająca czy nazwa kategorii już istnieje
    $scope.checkDuplicateName = function (name, excludeId) {
        if (!name) return false;

        return $scope.categories.some(function (category) {
            // Przy edycji pomijamy kategorię, którą edytujemy
            if (excludeId && category.id === excludeId) return false;
            return category.name.toLowerCase() === name.toLowerCase();
        });
    };

    // Sprawdzanie duplikatu przy dodawaniu
    $scope.checkAddDuplicate = function () {
        $scope.isDuplicateAdd = $scope.checkDuplicateName($scope.newCategory);
    };

    // Sprawdzanie duplikatu przy edycji
    $scope.checkEditDuplicate = function () {
        $scope.isDuplicateEdit = $scope.checkDuplicateName($scope.editCategoryName, $scope.editCategoryId);
    };

    // Funkcja otwierająca modal edycji
    $scope.openEditModal = function (category) {
        $scope.editCategoryId = category.id;
        $scope.editCategoryName = category.name;
        $scope.isDuplicateEdit = false;

        $timeout(function () {
            editModal.show();
        });
    };

    // Funkcja obsługująca formularz usuwania
    $scope.deleteCategory = function (event, category) {
        event.preventDefault();
        var form = event.target;
        form.action = form.action + category.id;
        form.submit();
    };

    // Funkcja obsługująca formularz edycji
    $scope.updateCategory = function (event) {
        if ($scope.isDuplicateEdit) {
            event.preventDefault();
            return;
        }

        event.preventDefault();
        var form = event.target;
        form.action = form.action + $scope.editCategoryId;
        form.submit();
    };
});

app.controller('TaskController', function ($scope, $timeout) {
    // Inicjalizacja modelu nowego zadania
    $scope.newTask = {
        title: '',
        description: '',
        categoryId: ''
    };

    // Filtry
    $scope.searchText = '';
    $scope.categoryFilter = '';
    $scope.statusFilter = '';

    //Do modal edit
    $scope.editTaskId = '';
    $scope.editTaskTitle = '';
    $scope.editTaskDescription = '';
    $scope.editTaskCategoryId = '';
    $scope.isDuplicateEdit = false;
    var editModal;

    // Inicjalizacja modala po załadowaniu DOM
    $timeout(function () {
        editModal = new bootstrap.Modal(document.getElementById('editTaskModal'));
    }, 500);

    // Filtrowanie po kategorii
    $scope.filterByCategory = function (task) {
        if (!$scope.categoryFilter) return true;
        return task.category.id.toString() === $scope.categoryFilter;
    };

    // Filtrowanie po statusie
    $scope.filterByStatus = function (task) {
        if (!$scope.statusFilter) return true;
        return task.status && task.status.id.toString() === $scope.statusFilter;
    };

    // Pobieranie nazwy kategorii na podstawie ID
    $scope.getCategoryName = function (categoryId) {
        for (var i = 0; i < $scope.categories.length; i++) {
            if ($scope.categories[i].id === categoryId) {
                return $scope.categories[i].name;
            }
        }
        return "Nieznana kategoria";
    };

    // Określanie klasy CSS na podstawie statusu
    $scope.getStatusClass = function (status) {
        if (!status)
            return '';

        // Sprawdzamy po nazwie statusu
        if (status.name.toLowerCase() === 'new') return 'bg-primary';
        if (status.name.toLowerCase() === 'in_progress') return 'bg-warning';
        if (status.name.toLowerCase() === 'completed') return 'bg-success';

        // Domyślnie
        return '';
    };

    // Określanie klasy badge na podstawie statusu
    $scope.getStatusBadgeClass = function (status) {
        if (!status) return 'bg-secondary';

        // Sprawdzamy po nazwie statusu
        if (status.name.toLowerCase() === 'new') return 'bg-secondary';
        if (status.name.toLowerCase() === 'in_progress') return 'bg-warning';
        if (status.name.toLowerCase() === 'completed') return 'bg-success';

        // Domyślnie
        return 'bg-secondary';
    };

    // Aktualizacja statusu zadania
    $scope.updateTaskStatus = function (event, task, status) {
        event.preventDefault();
        var form = event.target;
        form.action = form.action + task.id;
        form.submit();
    };

    // Funkcja otwierająca modal edycji
    $scope.openEditModal = function (task) {
        // Kopiujemy dane zadania do zmiennych edycji
        $scope.editTaskId = task.id;
        $scope.editTaskTitle = task.title;
        $scope.editTaskDescription = task.description;
        $scope.editTaskCategoryId = task.category.id.toString(); // Convert to string for the select element

        // Resetujemy flagę duplikatu
        $scope.isDuplicateEdit = false;

        // Otwieramy modal
        $timeout(function () {
            editModal.show();
        });
    };

    // Funkcja obsługująca formularz edycji zadania
    $scope.updateTask = function (event) {
        if ($scope.isDuplicateEdit) {
            event.preventDefault();
            return;
        }

        event.preventDefault();
        var form = event.target;
        form.action = form.action + $scope.editTaskId;
        form.submit();
    };


    // Usuwanie zadania
    $scope.deleteTask = function (event, task) {
        event.preventDefault();
        if (confirm(`Czy na pewno chcesz usunąć zadanie \"${task.title}\"?`)) {
            var form = event.target;
            form.action = form.action + task.id;
            form.submit();
        }
    };

    // Zmienne do kontrolowania komunikatów o duplikatach
    $scope.isDuplicateAdd = false;

    // Funkcja sprawdzająca czy nazwa zadania już istnieje w dowolnej kategorii
    $scope.checkDuplicateTitle = function (title, categoryId, excludeId) {
        if (!title || !categoryId) return false;

        return $scope.tasks.some(function (task) {
            // Przy edycji pomijamy zadanie, które edytujemy
            if (excludeId && task.id === excludeId) return false;
            return task.title.toLowerCase() === title.toLowerCase()
                    && task.category.id == categoryId;
        });
    };

    // Sprawdzanie duplikatu przy dodawaniu
    $scope.checkAddDuplicate = function () {
        $scope.isDuplicateAdd = $scope.checkDuplicateTitle(
                $scope.newTask.title,
                $scope.newTask.categoryId
        );
    };
});

app.controller('StatusController', function ($scope, $timeout) {
    // Inicjalizacja zmiennych
    $scope.newStatus = '';
    $scope.editStatusId = '';
    $scope.editStatusName = '';

    // Zmienne do kontrolowania komunikatów o duplikatach
    $scope.isDuplicateAdd = false;
    $scope.isDuplicateEdit = false;

    var editModal;

    // Inicjalizacja modala po załadowaniu DOM z timeoutem
    $timeout(function () {
        editModal = new bootstrap.Modal(document.getElementById('editModal'));
    }, 500);

    // Funkcja sprawdzająca czy nazwa statusu już istnieje
    $scope.checkDuplicateName = function (name, excludeId) {
        if (!name) return false;

        return $scope.statuses.some(function (status) {
            // Przy edycji pomijamy status, który edytujemy
            if (excludeId && status.id === excludeId) return false;
            return status.name.toLowerCase() === name.toLowerCase();
        });
    };

    // Sprawdzanie duplikatu przy dodawaniu
    $scope.checkAddDuplicate = function () {
        $scope.isDuplicateAdd = $scope.checkDuplicateName($scope.newStatus);
    };

    // Sprawdzanie duplikatu przy edycji
    $scope.checkEditDuplicate = function () {
        $scope.isDuplicateEdit = $scope.checkDuplicateName($scope.editStatusName, $scope.editStatusId);
    };

    // Funkcja otwierająca modal edycji
    $scope.openEditModal = function (status) {
        $scope.editStatusId = status.id;
        $scope.editStatusName = status.name;
        $scope.isDuplicateEdit = false;

        $timeout(function () {
            editModal.show();
        });
    };

    // Sprawdzanie duplikatu przy edycji
    $scope.checkEditDuplicate = function () {
        $scope.isDuplicateEdit = $scope.checkDuplicateTitle(
                $scope.editTaskTitle,
                $scope.editTaskCategoryId,
                $scope.editTaskId
        );
    };

    // Funkcja obsługująca formularz usuwania
    $scope.deleteStatus = function (event, status) {
        event.preventDefault();
        if (confirm(`Czy na pewno chcesz usunąć status \"${status.name}\"?`)) {
            var form = event.target;
            form.action = form.action + status.id;
            form.submit();
        }
    };

    // Funkcja obsługująca formularz edycji
    $scope.updateStatus = function (event) {
        if ($scope.isDuplicateEdit) {
            event.preventDefault();
            return;
        }

        event.preventDefault();
        var form = event.target;
        form.action = form.action + $scope.editStatusId;
        form.submit();
    };
});

document.addEventListener('DOMContentLoaded', (event) => {
    const htmlElement = document.documentElement;
    const themeButtons = document.querySelectorAll('[data-bs-theme-value]');
    const themeDropdown = document.getElementById('themeDropdown');

    // Function to set active theme
    const setTheme = (theme) => {
        if (theme === 'auto') {
            // Check if user prefers dark mode
            const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
            htmlElement.setAttribute('data-bs-theme', prefersDark ? 'dark' : 'light');
        } else {
            htmlElement.setAttribute('data-bs-theme', theme);
        }

        // Update dropdown button to show current theme
        const themeText = theme === 'light' ? 'Jasny' :
                theme === 'dark' ? 'Ciemny' : 'Auto';

        // Store preference
        localStorage.setItem('bsTheme', theme);
    };

    // Set up event listeners for theme buttons
    themeButtons.forEach(button => {
        button.addEventListener('click', () => {
            const theme = button.getAttribute('data-bs-theme-value');
            setTheme(theme);
        });
    });

    // Set up listener for system preference changes when in auto mode
    window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', e => {
        if (localStorage.getItem('bsTheme') === 'auto') {
            htmlElement.setAttribute('data-bs-theme', e.matches ? 'dark' : 'light');
        }
    });

    // Initialize theme from localStorage or default to auto
    const storedTheme = localStorage.getItem('bsTheme') || 'auto';
    setTheme(storedTheme);
});