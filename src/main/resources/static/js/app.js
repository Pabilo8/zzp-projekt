var app = angular.module('taskManagerApp', []);

app.controller('CategoryController', function($scope, $http, $timeout) {
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
    $timeout(function() {
        editModal = new bootstrap.Modal(document.getElementById('editModal'));
    }, 500);

    // Funkcja sprawdzająca czy nazwa kategorii już istnieje
    $scope.checkDuplicateName = function(name, excludeId) {
        if (!name) return false;

        return $scope.categories.some(function(category) {
            // Przy edycji pomijamy kategorię, którą edytujemy
            if (excludeId && category.id === excludeId) return false;
            return category.name.toLowerCase() === name.toLowerCase();
        });
    };

    // Sprawdzanie duplikatu przy dodawaniu
    $scope.checkAddDuplicate = function() {
        $scope.isDuplicateAdd = $scope.checkDuplicateName($scope.newCategory);
    };

    // Sprawdzanie duplikatu przy edycji
    $scope.checkEditDuplicate = function() {
        $scope.isDuplicateEdit = $scope.checkDuplicateName($scope.editCategoryName, $scope.editCategoryId);
    };

    // Funkcja otwierająca modal edycji
    $scope.openEditModal = function(category) {
        $scope.editCategoryId = category.id;
        $scope.editCategoryName = category.name;
        $scope.isDuplicateEdit = false;

        $timeout(function() {
            editModal.show();
        });
    };

    // Funkcja obsługująca formularz usuwania
    $scope.deleteCategory = function(event, category) {
        event.preventDefault();
        var form = event.target;
        form.action = form.action + category.id;
        form.submit();
    };

    // Funkcja obsługująca formularz edycji
    $scope.updateCategory = function(event) {
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

app.controller('TaskController', function($scope, $timeout) {
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

    // Filtrowanie po kategorii
    $scope.filterByCategory = function(task) {
        if (!$scope.categoryFilter) return true;
        return task.category.id.toString() === $scope.categoryFilter;
    };

    // Filtrowanie po statusie - zaktualizowane
    $scope.filterByStatus = function(task) {
        if (!$scope.statusFilter) return true;
        return task.status && task.status.id.toString() === $scope.statusFilter;
    };

    // Pobieranie nazwy kategorii na podstawie ID
    $scope.getCategoryName = function(categoryId) {
        for (var i = 0; i < $scope.categories.length; i++) {
            if ($scope.categories[i].id === categoryId) {
                return $scope.categories[i].name;
            }
        }
        return "Nieznana kategoria";
    };

    // Określanie klasy CSS na podstawie statusu
    $scope.getStatusClass = function(status) {
        if (!status) return 'bg-light';

        // Sprawdzamy po nazwie statusu
        if (status.name.toLowerCase() === 'new') return 'bg-light';
        if (status.name.toLowerCase() === 'in_progress') return 'bg-warning';
        if (status.name.toLowerCase() === 'completed') return 'bg-success text-white';

        // Domyślnie
        return 'bg-light';
    };

    // Określanie klasy badge na podstawie statusu
    $scope.getStatusBadgeClass = function(status) {
        if (!status) return 'bg-secondary';

        // Sprawdzamy po nazwie statusu
        if (status.name.toLowerCase() === 'new') return 'bg-secondary';
        if (status.name.toLowerCase() === 'in_progress') return 'bg-warning';
        if (status.name.toLowerCase() === 'completed') return 'bg-success';

        // Domyślnie
        return 'bg-secondary';
    };

    // Aktualizacja statusu zadania
    $scope.updateTaskStatus = function(event, task, status) {
        event.preventDefault();
        var form = event.target;
        form.action = form.action + task.id;
        form.submit();
    };

    // Usuwanie zadania
    $scope.deleteTask = function(event, task) {
        event.preventDefault();
        if (confirm('Czy na pewno chcesz usunąć to zadanie?')) {
            var form = event.target;
            form.action = form.action + task.id;
            form.submit();
        }
    };

    // Zmienne do kontrolowania komunikatów o duplikatach
    $scope.isDuplicateAdd = false;

    // Funkcja sprawdzająca czy nazwa zadania już istnieje w dowolnej kategorii
    $scope.checkDuplicateTitle = function(title, categoryId, excludeId) {
        if (!title) return false;

        return $scope.tasks.some(function(task) {
            // Przy edycji pomijamy zadanie, które edytujemy
            if (excludeId && task.id === excludeId) return false;
            // Sprawdzamy duplikat we wszystkich kategoriach
            return task.title.toLowerCase() === title.toLowerCase();
        });
    };

    // Sprawdzanie duplikatu przy dodawaniu
    $scope.checkAddDuplicate = function() {
        $scope.isDuplicateAdd = $scope.checkDuplicateTitle(
                $scope.newTask.title,
                $scope.newTask.categoryId
        );
    };
});

app.controller('StatusController', function($scope, $timeout) {
    // Inicjalizacja zmiennych
    $scope.newStatus = '';
    $scope.editStatusId = '';
    $scope.editStatusName = '';

    // Zmienne do kontrolowania komunikatów o duplikatach
    $scope.isDuplicateAdd = false;
    $scope.isDuplicateEdit = false;

    var editModal;

    // Inicjalizacja modala po załadowaniu DOM z timeoutem
    $timeout(function() {
        editModal = new bootstrap.Modal(document.getElementById('editModal'));
    }, 500);

    // Funkcja sprawdzająca czy nazwa statusu już istnieje
    $scope.checkDuplicateName = function(name, excludeId) {
        if (!name) return false;

        return $scope.statuses.some(function(status) {
            // Przy edycji pomijamy status, który edytujemy
            if (excludeId && status.id === excludeId) return false;
            return status.name.toLowerCase() === name.toLowerCase();
        });
    };

    // Sprawdzanie duplikatu przy dodawaniu
    $scope.checkAddDuplicate = function() {
        $scope.isDuplicateAdd = $scope.checkDuplicateName($scope.newStatus);
    };

    // Sprawdzanie duplikatu przy edycji
    $scope.checkEditDuplicate = function() {
        $scope.isDuplicateEdit = $scope.checkDuplicateName($scope.editStatusName, $scope.editStatusId);
    };

    // Funkcja otwierająca modal edycji
    $scope.openEditModal = function(status) {
        $scope.editStatusId = status.id;
        $scope.editStatusName = status.name;
        $scope.isDuplicateEdit = false;

        $timeout(function() {
            editModal.show();
        });
    };

    // Funkcja obsługująca formularz usuwania
    $scope.deleteStatus = function(event, status) {
        event.preventDefault();
        var form = event.target;
        form.action = form.action + status.id;
        form.submit();
    };

    // Funkcja obsługująca formularz edycji
    $scope.updateStatus = function(event) {
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