package ua.edu.nupp.kpp.client;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.nio.file.Path;
import java.util.logging.Logger;

public class MainApp extends Application {
    private static final Logger LOG = Logger.getLogger(MainApp.class.getName());

    private final HttpGreenSpacesClient api = new HttpGreenSpacesClient();

    private final TableView<Tree> treesTable = new TableView<>();
    private final TableView<Inspection> inspectionsTable = new TableView<>();
    private final TableView<WorkOrderRequest> workOrdersTable = new TableView<>();
    private final TableView<CrewAssignment> crewTable = new TableView<>();

    @Override
    public void start(Stage stage) {
        TabPane tabs = new TabPane(
                new Tab("Дерева", treePane()),
                new Tab("Огляди", inspectionPane()),
                new Tab("Заявки на роботи", workOrderPane()),
                new Tab("Наряди", crewPane())
        );
        tabs.getTabs().forEach(t -> t.setClosable(false));

        stage.setScene(new Scene(tabs, 1100, 650));
        stage.setTitle("Система догляду за міськими зеленими насадженнями");
        stage.show();

        refreshAll();
    }

    private Pane treePane() {
        treesTable.getColumns().setAll(
                col("ID", "id"),
                col("Вид", "species"),
                col("Локація", "location"),
                col("Рік садіння", "plantingDate"),
                col("Стан (F1)", "conditionScore")
        );

        TextField species = new TextField(), location = new TextField(),
                plantingDate = new TextField(), conditionScore = new TextField();

        ProgressBar progress = new ProgressBar(0);
        Label status = new Label("CSV-імпорт не запущено");

        Button add = new Button("Додати"), update = new Button("Змінити"),
                delete = new Button("Видалити"), refresh = new Button("Оновити"),
                importCsv = new Button("Імпортувати CSV");

        add.setOnAction(e -> safe(() -> {
            api.addTree(new Tree(null, species.getText(), location.getText(),
                    Integer.parseInt(plantingDate.getText()), Integer.parseInt(conditionScore.getText())));
            refreshTrees();
        }));

        update.setOnAction(e -> safe(() -> {
            Tree t = selected(treesTable);
            t.setSpecies(species.getText());
            t.setLocation(location.getText());
            t.setPlantingDate(Integer.parseInt(plantingDate.getText()));
            t.setConditionScore(Integer.parseInt(conditionScore.getText()));
            api.updateTree(t);
            refreshTrees();
        }));

        delete.setOnAction(e -> safe(() -> {
            api.deleteTree(selected(treesTable).getId());
            refreshAll();
        }));

        refresh.setOnAction(e -> refreshTrees());

        importCsv.setOnAction(e -> {
            ImportTreesTask task = new ImportTreesTask(Path.of("sample-data", "trees.csv"), api);
            progress.progressProperty().bind(task.progressProperty());
            status.textProperty().bind(task.messageProperty());

            task.setOnSucceeded(x -> {
                progress.progressProperty().unbind();
                status.textProperty().unbind();
                status.setText("Готово. Імпортовано записів: " + task.getValue());
                refreshTrees();
            });

            task.setOnFailed(x -> {
                progress.progressProperty().unbind();
                status.textProperty().unbind();
                showError(task.getException());
            });

            new Thread(task, "csv-import").start();
        });

        treesTable.getSelectionModel().selectedItemProperty().addListener((o, a, s) -> {
            if (s != null) {
                species.setText(s.getSpecies());
                location.setText(s.getLocation());
                plantingDate.setText(String.valueOf(s.getPlantingDate()));
                conditionScore.setText(String.valueOf(s.getConditionScore()));
            }
        });

        VBox form = new VBox(6,
                new Label("Вид"), species,
                new Label("Локація"), location,
                new Label("Рік садіння"), plantingDate,
                new Label("Технічний стан (F1: 0-100)"), conditionScore,
                new HBox(8, add, update, delete, refresh),
                importCsv, progress, status
        );
        return layout(treesTable, form);
    }

    private Pane inspectionPane() {
        inspectionsTable.getColumns().setAll(
                col("ID", "id"),
                col("ID дерева", "treeId"),
                col("Дата", "inspectionDate"),
                col("F1 (Стан)", "f1"),
                col("F2 (Люди)", "f2"),
                col("F3 (Терміновість)", "f3"),
                col("Індекс ризику (I)", "riskIndex")
        );

        TextField treeId = new TextField(), date = new TextField(),
                f1 = new TextField(), f2 = new TextField(), f3 = new TextField();

        Button add = new Button("Додати"), update = new Button("Змінити"),
                delete = new Button("Видалити"), refresh = new Button("Оновити");

        add.setOnAction(e -> safe(() -> {
            double valF1 = Double.parseDouble(f1.getText());
            double valF2 = Double.parseDouble(f2.getText());
            double valF3 = Double.parseDouble(f3.getText());
            double calculatedRisk = valF1 * 0.4 + valF2 * 0.3 + valF3 * 0.3;

            api.addInspection(new Inspection(null, Long.parseLong(treeId.getText()), date.getText(),
                    valF1, valF2, valF3, calculatedRisk));
            refreshInspections();
        }));

        update.setOnAction(e -> safe(() -> {
            Inspection i = selected(inspectionsTable);
            double valF1 = Double.parseDouble(f1.getText());
            double valF2 = Double.parseDouble(f2.getText());
            double valF3 = Double.parseDouble(f3.getText());
            double calculatedRisk = valF1 * 0.4 + valF2 * 0.3 + valF3 * 0.3;

            api.updateInspection(new Inspection(i.getId(), Long.parseLong(treeId.getText()), date.getText(),
                    valF1, valF2, valF3, calculatedRisk));
            refreshInspections();
        }));

        delete.setOnAction(e -> safe(() -> {
            api.deleteInspection(selected(inspectionsTable).getId());
            refreshAll();
        }));

        refresh.setOnAction(e -> refreshInspections());

        inspectionsTable.getSelectionModel().selectedItemProperty().addListener((o, a, i) -> {
            if (i != null) {
                treeId.setText(String.valueOf(i.getTreeId()));
                date.setText(i.getInspectionDate());
                f1.setText(String.valueOf(i.getF1()));
                f2.setText(String.valueOf(i.getF2()));
                f3.setText(String.valueOf(i.getF3()));
            }
        });

        VBox form = new VBox(6,
                new Label("ID дерева"), treeId,
                new Label("Дата (YYYY-MM-DD)"), date,
                new Label("F1 (Технічний стан)"), f1,
                new Label("F2 (Перебування людей)"), f2,
                new Label("F3 (Терміновість втручання)"), f3,
                new HBox(8, add, update, delete, refresh)
        );
        return layout(inspectionsTable, form);
    }

    private Pane workOrderPane() {
        workOrdersTable.getColumns().setAll(
                col("ID", "id"),
                col("ID огляду", "inspectionId"),
                col("Стан", "state"),
                col("Опис", "description")
        );

        TextField inspectionId = new TextField(), description = new TextField();
        ComboBox<WorkOrderRequestState> stateCombo = new ComboBox<>(FXCollections.observableArrayList(WorkOrderRequestState.values()));

        Button add = new Button("Створити"), update = new Button("Змінити"),
                changeState = new Button("Змінити стан"), delete = new Button("Видалити"), refresh = new Button("Оновити");

        add.setOnAction(e -> safe(() -> {
            api.addWorkOrder(Long.parseLong(inspectionId.getText()), description.getText());
            refreshWorkOrders();
        }));

        update.setOnAction(e -> safe(() -> {
            // Тепер передається стан (stateCombo.getValue())
            api.updateWorkOrder(selected(workOrdersTable).getId(), Long.parseLong(inspectionId.getText()), stateCombo.getValue(), description.getText());
            refreshWorkOrders();
        }));

        changeState.setOnAction(e -> safe(() -> {
            api.changeWorkOrderState(selected(workOrdersTable).getId(), stateCombo.getValue());
            refreshWorkOrders();
        }));

        delete.setOnAction(e -> safe(() -> {
            api.deleteWorkOrder(selected(workOrdersTable).getId());
            refreshAll();
        }));

        refresh.setOnAction(e -> refreshWorkOrders());

        workOrdersTable.getSelectionModel().selectedItemProperty().addListener((o, a, w) -> {
            if (w != null) {
                inspectionId.setText(String.valueOf(w.getInspectionId()));
                description.setText(w.getDescription());
                stateCombo.setValue(w.getState());
            }
        });

        VBox form = new VBox(6,
                new Label("ID огляду"), inspectionId,
                new Label("Опис робіт"), description,
                new Label("Новий стан життєвого циклу"), stateCombo,
                new HBox(8, add, update, changeState, delete, refresh)
        );
        return layout(workOrdersTable, form);
    }

    private Pane crewPane() {
        crewTable.getColumns().setAll(
                col("ID", "id"),
                col("ID заявки", "requestId"),
                col("Бригада", "crewName"),
                col("Опис", "description")
        );

        TextField requestId = new TextField(), crewName = new TextField(), description = new TextField();
        Button add = new Button("Додати"), update = new Button("Змінити"),
                delete = new Button("Видалити"), refresh = new Button("Оновити");

        add.setOnAction(e -> safe(() -> {
            api.addCrewAssignment(new CrewAssignment(null, Long.parseLong(requestId.getText()), crewName.getText(), description.getText()));
            refreshCrew();
        }));

        update.setOnAction(e -> safe(() -> {
            CrewAssignment c = selected(crewTable);
            api.updateCrewAssignment(new CrewAssignment(c.getId(), Long.parseLong(requestId.getText()), crewName.getText(), description.getText()));
            refreshCrew();
        }));

        delete.setOnAction(e -> safe(() -> {
            api.deleteCrewAssignment(selected(crewTable).getId());
            refreshCrew();
        }));

        refresh.setOnAction(e -> refreshCrew());

        crewTable.getSelectionModel().selectedItemProperty().addListener((o, a, c) -> {
            if (c != null) {
                requestId.setText(String.valueOf(c.getRequestId()));
                crewName.setText(c.getCrewName());
                description.setText(c.getDescription());
            }
        });

        VBox form = new VBox(6,
                new Label("ID заявки на роботи"), requestId,
                new Label("Назва бригади"), crewName,
                new Label("Опис призначення"), description,
                new HBox(8, add, update, delete, refresh)
        );
        return layout(crewTable, form);
    }

    private void refreshAll() { refreshTrees(); refreshInspections(); refreshWorkOrders(); refreshCrew(); }
    private void refreshTrees() { safe(() -> treesTable.setItems(FXCollections.observableArrayList(api.getTrees()))); }
    private void refreshInspections() { safe(() -> inspectionsTable.setItems(FXCollections.observableArrayList(api.getInspections()))); }
    private void refreshWorkOrders() { safe(() -> workOrdersTable.setItems(FXCollections.observableArrayList(api.getWorkOrders()))); }
    private void refreshCrew() { safe(() -> crewTable.setItems(FXCollections.observableArrayList(api.getCrewAssignments()))); }

    private <T> T selected(TableView<T> table) {
        T v = table.getSelectionModel().getSelectedItem();
        if (v == null) throw new IllegalArgumentException("Виберіть запис у таблиці");
        return v;
    }

    private void safe(Runnable action) {
        try {
            action.run();
        } catch (Exception e) {
            LOG.warning(e.getMessage());
            showError(e);
        }
    }

    private void showError(Throwable e) {
        Alert alert = new Alert(Alert.AlertType.ERROR, e == null ? "Помилка виконання" : e.getMessage(), ButtonType.OK);
        alert.showAndWait();
    }

    private Pane layout(TableView<?> table, Pane form) {
        SplitPane p = new SplitPane(table, form);
        p.setDividerPositions(0.68);
        BorderPane root = new BorderPane(p);
        root.setPadding(new Insets(10));
        return root;
    }

    private <S, T> TableColumn<S, T> col(String title, String property) {
        TableColumn<S, T> c = new TableColumn<>(title);
        c.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>(property));
        return c;
    }

    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
        System.setErr(new java.io.PrintStream(System.err, true, java.nio.charset.StandardCharsets.UTF_8));
        launch(args);
    }
}