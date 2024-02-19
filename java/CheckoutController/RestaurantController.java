package CheckoutController;

import Model.Drink;
import Model.Food;
import Model.Order;
import Model.Payment;
import SQLDataBase.*;
import Service.IndexManager;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RestaurantController {
    private Label titleLabel;
    private Pane titleWrapper;
    private Pane spacePane;
    private Button totalAmountButton;
    private Button terraceButton;
    private Button insideButton;
    private Button deliveryButton;
    private HBox buttonsBox;
    private VBox titleAndButtonsBox;
    private HBox viewerBox;
    private TotalAmountViewer totalAmountViewer;
    private List<Payment> payments;
    private ToolBar toolBar;
    private InsideController insideController;
    private TerraceController terraceController;
    private DeliveryController deliveryController;
    private boolean insideButtonPressed;
    private boolean terraceButtonPressed;
    private Map<Pair<String, Integer>, Order> ordersMap;
    private Map<Pair<String, Integer>, List<Object>> payObjectsMap;
    private OrderDataBase orderDataBase;
    private OrderHistoryDataBase orderHistoryDataBase;
    private List<Drink> allDrinksToSearch;
    private List<Food> allFoodToSearch;
    private IndexManager indexManager;
    private List<Drink> orderedDrink;
    private List<Food> orderedFood;
    private PayOrdersDataBase payOrdersDataBase;
    private PaymentDataBase paymentDataBase;
    private List<Order> deliveryOrders;
    private DeliveryOrderDataBase deliveryOrderDataBase;
    private List<Order> restoreDeliveryOrders;
    private AuthAndSwitchController authAndSwitchController;
    private ViewerController viewerController;
    private List<Order> paidPrices;
    private OthersDataBase othersDataBase;
    private LiveOrdersController liveOrdersController;
    private List<Order> orders;
    private List<Order> ordersHistory;

    public RestaurantController(List<Payment> payments, Map<Pair<String, Integer>, Order> ordersMap, OrderDataBase orderDataBase, Map<Pair<String, Integer>, List<Object>> payObjectsMap, OrderHistoryDataBase orderHistoryDataBase, List<Drink> allDrinksToSearch, List<Food> allFoodToSearch, IndexManager indexManager, List<Drink> orderedDrink, List<Food> orderedFood, PayOrdersDataBase payOrdersDataBase, PaymentDataBase paymentDataBase, List<Order> deliveryOrders, DeliveryOrderDataBase deliveryOrderDataBase, AuthAndSwitchController authAndSwitchController, ViewerController viewerController, List<Order> paidPrices, LiveOrdersController liveOrdersController, List<Order> orders, List<Order> ordersHistory)
    {
        this.setupTitleAndButtons();
        this.setupViewerBox();

        this.allDrinksToSearch = allDrinksToSearch;
        this.allFoodToSearch = allFoodToSearch;

        this.payments = payments;
        this.orders = orders;
        this.ordersHistory = ordersHistory;
        this.ordersMap = ordersMap;
        this.orderDataBase = orderDataBase;
        this.payObjectsMap = payObjectsMap;
        this.orderHistoryDataBase = orderHistoryDataBase;
        this.payOrdersDataBase = payOrdersDataBase;
        this.paymentDataBase = paymentDataBase;
        this.deliveryOrders = deliveryOrders;
        this.deliveryOrderDataBase = deliveryOrderDataBase;
        this.restoreDeliveryOrders = new ArrayList<>();
        this.paidPrices = paidPrices;


        this.totalAmountViewer = new TotalAmountViewer();

        this.terraceController = new TerraceController();
        this.insideController = new InsideController();
        this.deliveryController = new DeliveryController();

        this.authAndSwitchController = authAndSwitchController;
        this.viewerController = viewerController;
        this.liveOrdersController = liveOrdersController;

        this.indexManager = indexManager;
        this.orderedDrink = orderedDrink;
        this.orderedFood = orderedFood;

        this.insideButtonPressed = false;
        this.terraceButtonPressed = false;

        this.setupToolBar();
    }

    public void setupTitleAndButtons()
    {
        titleLabel = new Label("RESTAURANT");
        titleWrapper = new Pane(titleLabel);

        titleWrapper.setPrefSize(535, 50);

        titleLabel.layoutXProperty().bind(titleWrapper.widthProperty().subtract(titleLabel.widthProperty()).divide(2));

        {
            titleWrapper.setStyle("-fx-background-color: #df00fe;");
            titleLabel.getStyleClass().add("welcome-label");
        }

        terraceButton = new Button("TERRACE");
        insideButton = new Button("INSIDE");
        deliveryButton = new Button("DELIVERY");
        totalAmountButton = new Button("TOTAL AMOUNT");

        terraceButton.setPrefSize(150, 50);
        insideButton.setPrefSize(150, 50);
        deliveryButton.setPrefSize(150, 50);
        totalAmountButton.setPrefSize(150, 50);

        {
            terraceButton.getStyleClass().add("interface-button");
            insideButton.getStyleClass().add("interface-button");
            deliveryButton.getStyleClass().add("interface-button");
            totalAmountButton.getStyleClass().add("interface-button");
        }

        spacePane = new Pane();
        spacePane.setStyle("-fx-background-color: #df00fe;");
        spacePane.setPrefSize(30, 55);

        Region spacer = new Region();
        Region spacer2 = new Region();
        spacer.setPrefSize(465, 5);
        spacer2.setPrefSize(155, 5);

        buttonsBox = new HBox(5, terraceButton, insideButton, deliveryButton);

        VBox spacerBox1 = new VBox(spacer, buttonsBox);
        VBox spacerBox2 = new VBox(spacer2, totalAmountButton);
        HBox spacerAndButtonsBox = new HBox(5, spacerBox1, spacePane, spacerBox2);

        titleAndButtonsBox = new VBox(titleWrapper, spacerAndButtonsBox);
    }

    public void setupViewerBox()
    {
        this.viewerBox = new HBox(30);
        viewerBox.setPrefHeight(300);

        viewerBox.setMaxWidth(1200);

        viewerBox.setStyle("-fx-background-color: rgb(145,139,139);");
    }

    public void setupToolBar()
    {
        Button button = new Button("TOOLBAR SETTINGS COMING SOON");
        button.getStyleClass().add("toolbar-button");

        button.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("COMING SOON");
            alert.setHeaderText(null);
            alert.setContentText("Toolbar settings coming soon!");

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

            Label contentLabel = (Label) dialogPane.lookup(".content");
            if (contentLabel != null) {
                contentLabel.getStyleClass().add("alert-content-text");
            }

            alert.showAndWait();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        this.toolBar = new ToolBar(spacer, button, authAndSwitchController.getSwtichToClientInterfaceButton2());

        {
            toolBar.setStyle("-fx-background-color: rgb(114,108,108);");
        }

        toolBar.setPadding(Insets.EMPTY);
        toolBar.setMaxWidth(1200);
        toolBar.setMaxHeight(10);
    }

    class TotalAmountViewer {
        private Label totalAmountLabel;
        private Pane totalAmountWrapper;
        private Label cashLabel;
        private Pane cashWrapper;
        private Label cardLabel;
        private Pane cardWrapper;
        private Button okButton;
        private VBox mainBox;
        private Scene scene;
        private Stage stage;
        private float totalAmount;
        private float cashSum;
        private float cardSum;

        public TotalAmountViewer()
        {
            this.setupTotalAmountViewer();
            this.setupOkButton();

            this.totalAmount = 0;
            this.cashSum = 0;
            this.cardSum = 0;
        }

        public void setupTotalAmountViewer()
        {
            totalAmountLabel = new Label("TOTAL AMOUNT: " + this.getTotalAmount() + " LEI");
            totalAmountWrapper = new Pane(totalAmountLabel);

            totalAmountLabel.layoutXProperty().bind(totalAmountWrapper.widthProperty().subtract(totalAmountLabel.widthProperty()).divide(2));

            {
                totalAmountLabel.getStyleClass().add("welcome-label");
                totalAmountWrapper.setStyle("-fx-background-color: #0e0e0e;");
            }

            cashLabel = new Label("CASH: " + this.getTotalCash() +" LEI");
            cashWrapper = new Pane(cashLabel);

            cashLabel.layoutXProperty().bind(cashWrapper.widthProperty().subtract(cashLabel.widthProperty()).divide(2));

            {
                cashLabel.getStyleClass().add("welcome-label");
                cashWrapper.setStyle("-fx-background-color: #0e0e0e;");
            }

            cardLabel = new Label("CARD: " + this.getTotalCard() + " LEI");
            cardWrapper = new Pane(cardLabel);

            cardLabel.layoutXProperty().bind(cardWrapper.widthProperty().subtract(cardLabel.widthProperty()).divide(2));

            {
                cardLabel.getStyleClass().add("welcome-label");
                cardWrapper.setStyle("-fx-background-color: #0e0e0e;");
            }

            okButton = new Button("OK");
            okButton.setPrefSize(500, 50);

            {
                okButton.getStyleClass().add("interface-button");
            }

            mainBox = new VBox(2, totalAmountWrapper, cashWrapper, cardWrapper, okButton);

            scene = new Scene(mainBox, 500, 206);
            scene.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

            stage = new Stage();
            stage.setScene(scene);
        }

        public void setupOkButton()
        {
            this.okButton.setOnAction(e -> {
                stage.close();
            });
        }

        public String getTotalAmount()
        {
            totalAmount = 0;

            for (int i = 0; i < payments.size(); ++i)
            {
                totalAmount += payments.get(i).getPrice();
            }

            return String.format("%.2f", totalAmount);
        }

        public String getTotalCash()
        {
            cashSum = 0;

            for (int i = 0; i < payments.size(); ++i)
            {
                if (payments.get(i).getPayMethod() != null)
                {
                    if (payments.get(i).getPayMethod().equals("Cash"))
                        cashSum += payments.get(i).getPrice();
                }
            }

            return String.format("%.2f", cashSum);
        }

        public String getTotalCard()
        {
            cardSum = 0;

            for (int i = 0; i < payments.size(); ++i)
            {
                if (payments.get(i).getPayMethod() != null)
                {
                    if (payments.get(i).getPayMethod().equals("Card"))
                        cardSum += payments.get(i).getPrice();
                }
            }

            return String.format("%.2f", cardSum);
        }

        public Stage getStage() {
            return stage;
        }

        public Label getTotalAmountLabel() {
            return totalAmountLabel;
        }

        public Label getCardLabel() {
            return cardLabel;
        }

        public Label getCashLabel() {
            return cashLabel;
        }
    }

    public void setupTotalAmountButton()
    {
        this.totalAmountButton.setOnAction(e -> {
            totalAmountViewer.getStage().show();
        });
    }

    public class InsideController {
        private Button table1;
        private Button table2;
        private Button table3;
        private Button table4;
        private Button table5;
        private Button table6;
        private Button table7;
        private Button table8;
        private Button table9;
        private Button table10;
        private Button table11;
        private Button table12;
        private Button table13;
        private Button table14;
        private Button table15;
        private Button table16;
        private Button table17;
        private Button table18;
        private Button table19;
        private Button bar;
        private HBox tableBox1;
        private VBox tableBox2;
        private HBox tableBox3;
        private HBox tableBox4;
        private VBox tableBox5;
        private HBox tableBox6;
        private VBox tableBox7;
        private HBox tableBox8;
        private HBox tableBox9;
        private HBox tableBox10;
        private VBox tableBox11;
        private HBox tableBox12;
        private HBox tableBox13;
        private VBox tableBox14;
        private HBox tableBox15;
        private HBox barBox;
        private Label titleLabel;
        private Pane titleWrapper;
        private Button okButton;
        private ListView<String> listView;
        private TableView orderView;
        private TableView<Object> payTableView;
        private Button payAllButton;
        private Button payOnlyButton;
        private Button payCashButton;
        private Button payCardButton;
        private Button payAllCashButton;
        private Button payAllCardButton;
        private HBox payButtonsBox1;
        private HBox payButtonsBox2;
        private VBox tablesAndButtonsBox;
        private HBox tablesBox;
        private VBox mainBox;
        private Scene scene;
        private Stage stage;
        private Label totalSumLabel;
        private Pane totalSumWrapper;
        private Label paidSumLabel;
        private Pane paidSumWrapper;
        private TableColumn<Drink, String> nameDrinkColumn = new TableColumn<>("Name");
        private TableColumn<Drink, Double> volumeColumn = new TableColumn<>("Volume");
        private TableColumn<Drink, Integer> priceDrinkColumn = new TableColumn<>("Price");
        private TableColumn<Food, String> nameFoodColumn = new TableColumn<>("Name");
        private TableColumn<Food, Integer> gramsColumn = new TableColumn<>("Grams");
        private TableColumn<Food, Integer> priceFoodColumn = new TableColumn<>("Price");
        private TextField searchDrinkField;
        private Button searchDrinkButton;
        private TableView<Drink> drinkTableView;
        private Button closeDrinkButton;
        private Button addDrinkButton;
        private HBox searchDrinkBox;
        private HBox buttonsDrinkBox;
        private VBox fullDrinkBox;
        private Scene drinkScene;
        private Stage drinkStage;
        private TextField searchFoodField;
        private Button searchFoodButton;
        private TableView<Food> foodTableView;
        private Button closeFoodButton;
        private Button addFoodButton;
        private HBox searchFoodBox;
        private HBox buttonsFoodBox;
        private VBox fullFoodBox;
        private Scene foodScene;
        private Stage foodStage;
        private boolean drinkButtonIsPressed;
        private boolean foodButtonIsPressed;
        private boolean cashButtonIsPressed;
        private boolean cardButtonIsPressed;
        private boolean allCashButtonIsPressed;
        private boolean allCardButtonIsPressed;

        private TableColumn<Object, String> namePayColumn = new TableColumn<>("Name");
        private TableColumn<Object, Integer> pricePayColumn = new TableColumn<>("Price");

        public InsideController()
        {
            this.setupTables();

            this.setupAddDrinkToOrder();
            this.setupCloseDrinkButton();
            this.setupSearchDrinkButton();

            this.setupAddFoodToOrder();
            this.setupCloseFoodButton();
            this.setupSearchFoodButton();

            this.setupTable1();
            this.setupTable2();
            this.setupTable3();
            this.setupTable4();
            this.setupTable5();
            this.setupTable6();
            this.setupTable7();
            this.setupTable8();
            this.setupTable9();
            this.setupTable10();
            this.setupTable11();
            this.setupTable12();
            this.setupTable13();
            this.setupTable14();
            this.setupTable15();
            this.setupTable16();
            this.setupTable17();
            this.setupTable18();
            this.setupTable19();

            this.setupTerraceTable1();
            this.setupTerraceTable2();
            this.setupTerraceTable3();
            this.setupTerraceTable4();
            this.setupTerraceTable5();
            this.setupTerraceTable6();
            this.setupTerraceTable7();
            this.setupTerraceTable8();
            this.setupTerraceTable9();
            this.setupTerraceTable10();
            this.setupTerraceTable11();
            this.setupTerraceTable12();
            this.setupTerraceTable13();
            this.setupTerraceTable14();
            this.setupTerraceTable15();
            this.setupTerraceTable16();
            this.setupTerraceTable17();
            this.setupTerraceTable18();

            this.drinkButtonIsPressed = false;
            this.foodButtonIsPressed = false;
            this.cashButtonIsPressed = false;
            this.cardButtonIsPressed = false;
            this.allCashButtonIsPressed = false;
            this.allCardButtonIsPressed = false;
        }

        public void setupTables()
        {
            table1 = new Button("T1");
            table2 = new Button("T2");
            table3 = new Button("T3");
            table4 = new Button("T4");
            table5 = new Button("T5");
            table6 = new Button("T6");
            table7 = new Button("T7");
            table8 = new Button("T8");
            table9 = new Button("T9");
            table10 = new Button("T10");
            table11 = new Button("T11");
            table12 = new Button("T12");
            table13 = new Button("T13");
            table14 = new Button("T14");
            table15 = new Button("T15");
            table16 = new Button("T16");
            table17 = new Button("T17");
            table18 = new Button("T18");
            table19 = new Button("T19");
            bar = new Button("BAR");

            {
                table1.getStyleClass().add("table-button");
                table2.getStyleClass().add("table-button");
                table3.getStyleClass().add("table-button");
                table4.getStyleClass().add("table-button");
                table5.getStyleClass().add("table-button");
                table6.getStyleClass().add("table-button");
                table7.getStyleClass().add("table-button");
                table8.getStyleClass().add("table-button");
                table9.getStyleClass().add("table-button");
                table10.getStyleClass().add("table-button");
                table11.getStyleClass().add("table-button");
                table12.getStyleClass().add("table-button");
                table13.getStyleClass().add("table-button");
                table14.getStyleClass().add("table-button");
                table15.getStyleClass().add("table-button");
                table16.getStyleClass().add("table-button");
                table17.getStyleClass().add("table-button");
                table18.getStyleClass().add("table-button");
                table19.getStyleClass().add("table-button");
                bar.getStyleClass().add("table-button");
            }

            bar.setPrefSize(650, 100);

            table1.setPrefSize(50, 150);
            table2.setPrefSize(50, 150);
            table3.setPrefSize(150, 50);

            Region region = new Region();
            region.setPrefHeight(20);

            tableBox1 = new HBox(25, table1, table2);
            tableBox2 = new VBox(25, region, tableBox1, table3);

            table4.setPrefSize(50, 50);
            table5.setPrefSize(100, 50);
            table6.setPrefSize(150, 50);
            table7.setPrefSize(150, 50);
            table8.setPrefSize(50, 50);

            Region region1 = new Region();
            region1.setPrefWidth(25);

            Region region3 = new Region();
            region3.setPrefWidth(50);

            table9.setPrefSize(50, 125);
            table10.setPrefSize(50, 125);

            tableBox6 = new HBox(35, table9, table10);

            tableBox3 = new HBox(25, table4, table5, region1, table8);
            tableBox4 = new HBox(25, table6, table7);

            table11.setPrefSize(50,50);
            table12.setPrefSize(100, 50);
            table13.setPrefSize(50, 50);
            table14.setPrefSize(150, 50);

            tableBox9 = new HBox(60, table11, table12);
            tableBox10 = new HBox(25, table13, table14);

            tableBox11 = new VBox(25, tableBox9, tableBox10);

            table15.setPrefSize(50, 50);
            table16.setPrefSize(50, 50);
            table17.setPrefSize(50, 50);
            table18.setPrefSize(50, 50);
            table19.setPrefSize(50, 115);

            Region region4 = new Region();
            region4.setPrefHeight(0);

            Region region6 = new Region();
            region6.setPrefWidth(30);

            tableBox15 = new HBox(region6, table19);

            tableBox12 = new HBox(30, table15, table16);
            tableBox13 = new HBox(30, table17, table18);
            tableBox14 = new VBox(15, region4, tableBox12, tableBox13);

            tableBox7 = new VBox(20, tableBox3, tableBox4);
            tableBox8 = new HBox(50, tableBox7, tableBox6, tableBox11, tableBox15);

            Region region2 = new Region();
            region2.setPrefHeight(130);
            region2.setPrefWidth(100);
            Region region5 = new Region();
            region5.setPrefWidth(50);

            barBox = new HBox(region2, bar, region5, tableBox14);

            tableBox5 = new VBox(20, barBox, tableBox8);
        }

        public void setupAnyTable(Button tableButton, int number, String place)
        {
            tableButton.setOnAction(e -> {

                this.setupAddDrinkButton(number, place);
                this.setupAddFoodButton(number, place);

                System.out.println(number);
                System.out.println(place);

                this.titleLabel = new Label("TABLE " + number);
                this.titleWrapper = new Pane(titleLabel);

                titleLabel.layoutXProperty().bind(titleWrapper.widthProperty().subtract(titleLabel.widthProperty()).divide(2));

                {
                    titleLabel.getStyleClass().add("welcome-label");
                    titleWrapper.setStyle("-fx-background-color: #df00fe;");
                }

                okButton = new Button("OK");
                okButton.setPrefSize(800, 50);

                {
                    okButton.getStyleClass().add("interface-button");
                }

                orderView = new TableView();

                nameDrinkColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

                volumeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getVolume()));

                priceDrinkColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrice()));

                nameFoodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

                gramsColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getGrams()));

                priceFoodColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrice()));

                orderView.setPrefSize(595, 245);

                nameDrinkColumn.setCellFactory(column -> new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setStyle("");
                        } else {
                            setText(item);
                            getStyleClass().add("orders-label");
                            setStyle("-fx-background-color: rgb(0,208,255); -fx-alignment: center;");
                        }
                    }
                });

                volumeColumn.setCellFactory(column -> new TableCell<>() {
                    @Override
                    protected void updateItem(Double item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setStyle("");
                        } else {
                            setText(String.valueOf(item + " ML"));
                            getStyleClass().add("orders-label");
                            setStyle("-fx-background-color: rgb(0,208,255); -fx-alignment: center;");
                        }
                    }
                });

                priceDrinkColumn.setCellFactory(column -> new TableCell<>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setStyle("");
                        } else {
                            setText(String.valueOf(item + " LEI"));
                            getStyleClass().add("orders-label");
                            setStyle("-fx-background-color: rgb(0,208,255); -fx-alignment: center;");
                        }
                    }
                });

                nameFoodColumn.setCellFactory(column -> new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setStyle("");
                        } else {
                            setText(item);
                            getStyleClass().add("orders-label");
                            setStyle("-fx-background-color: rgb(0,208,255); -fx-alignment: center;");
                        }
                    }
                });

                gramsColumn.setCellFactory(column -> new TableCell<>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setStyle("");
                        } else {
                            setText(String.valueOf(item + " GR"));
                            getStyleClass().add("orders-label");
                            setStyle("-fx-background-color: rgb(0,208,255); -fx-alignment: center;");
                        }
                    }
                });

                priceFoodColumn.setCellFactory(column -> new TableCell<>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setStyle("");
                        } else {
                            setText(String.valueOf(item + " LEI"));
                            getStyleClass().add("orders-label");
                            setStyle("-fx-background-color: rgb(0,208,255); -fx-alignment: center;");
                        }
                    }
                });

                listView = new ListView<>();
                listView.getItems().addAll("ORDER", "DRINK", "FOOD", "ADD", "ADD DRINK", "ADD FOOD", "REMOVE", "REMOVE DRINK", "REMOVE FOOD", "RESTORE", "RESTORE ITEM", "RESTORE DRINK", "RESTORE FOOD", "REFRESH");
                listView.setPrefSize(200, 590);

                listView.setCellFactory(param -> new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(null);
                        setGraphic(null);

                        if (item != null && !empty) {
                            setText(item);
                            setAlignment(Pos.CENTER);

                            setPadding(new Insets(5, 10, 5, 10));

                            if (item.equals("ORDER") || item.equals("ADD") || item.equals("REMOVE") || item.equals("RESTORE"))
                            {
                                setStyle("-fx-background-color: #0e0e0e; " +
                                        "-fx-font-weight: bold; " +
                                        "-fx-font-size: 22px; " +
                                        "-fx-text-fill: #ffffff;");
                            }
                            else
                            {
                                setStyle("-fx-background-color: #397a43; " +
                                        "-fx-font-weight: bold; " +
                                        "-fx-font-size: 22px; " +
                                        "-fx-text-fill: #ffffff;");
                            }

                            if (isSelected() && !item.equals("ORDER") && !item.equals("ADD") && ! item.equals("REMOVE") && !item.equals("RESTORE")) {
                                setStyle("-fx-background-color: #fd6201; " +
                                        "-fx-font-weight: bold; " +
                                        "-fx-font-size: 22px; " +
                                        "-fx-text-fill: #ffffff;");
                            }

                            if (item.equals("REFRESH"))
                            {
                                setStyle("-fx-background-color: #333dff; " +
                                        "-fx-font-weight: bold; " +
                                        "-fx-font-size: 22px; " +
                                        "-fx-text-fill: #ffffff;");

                                setOnMouseClicked(event -> {
                                    payTableView.getStyleClass().add("table-view-refreshed");
                                    orderView.getStyleClass().add("table-view-refreshed");

                                    orderView.refresh();
                                    payTableView.refresh();

                                    PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
                                    delay.setOnFinished(event2 -> {
                                        payTableView.getStyleClass().remove("table-view-refreshed");
                                        orderView.getStyleClass().remove("table-view-refreshed");
                                    });

                                    delay.play();
                                });
                            }

                            if (item.equals("DRINK"))
                            {
                                setOnMouseClicked(event -> {
                                    setupTableViewForDrinks(number, place);
                                });

                                drinkButtonIsPressed = true;
                                foodButtonIsPressed = false;
                            }
                            else if (item.equals("FOOD"))
                            {
                                setOnMouseClicked(event -> {
                                    setupTableViewForFood(number, place);
                                });

                                drinkButtonIsPressed = false;
                                foodButtonIsPressed = true;
                            }

                            if (item.equals("REMOVE DRINK"))
                            {
                                try
                                {
                                    Drink selectedItem = (Drink) orderView.getSelectionModel().getSelectedItem();

                                    if (selectedItem != null)
                                    {
                                        orderView.getItems().remove(selectedItem);

                                        Pair<String, Integer> key = new Pair<>(place, number);
                                        Order order = ordersMap.get(key);
                                        order.getOrderedDrink().remove(selectedItem);

                                        orderDataBase.deleteOrderedDrinkByNameFromdataBase(selectedItem.getName());
                                        orderHistoryDataBase.deleteOrderedDrinkByNameFromdataBase(selectedItem.getName());

                                        order.setPrice(order.getPrice() - selectedItem.getPrice());
                                    }
                                }
                                catch (ClassCastException e)
                                {
                                    System.out.println("Selected item is not a drink.");
                                }

                                totalSumLabel.setText("TOTAL: " + getTotalSumToPaid(number, place) + " LEI");
                            }

                            if (item.equals("REMOVE FOOD"))
                            {
                                try
                                {
                                    Food selectedItem = (Food) orderView.getSelectionModel().getSelectedItem();

                                    if (selectedItem != null)
                                    {
                                        orderView.getItems().remove(selectedItem);

                                        Pair<String, Integer> key = new Pair<>(place, number);
                                        Order order = ordersMap.get(key);
                                        order.getOrderedFood().remove(selectedItem);

                                        orderDataBase.deleteOrderedFoodByNameFromDataBase(selectedItem.getName());
                                        orderHistoryDataBase.deleteOrderedFoodByNameFromDataBase(selectedItem.getName());

                                        order.setPrice(order.getPrice() - selectedItem.getPrice());
                                    }
                                }
                                catch (ClassCastException e)
                                {
                                    System.out.println("Selected item is not a food.");
                                }

                                totalSumLabel.setText("TOTAL: " + getTotalSumToPaid(number, place) + " LEI");
                            }

                            if (item.equals("RESTORE ITEM"))
                            {
                                setOnMouseClicked(event -> {
                                    Object selectedItem = payTableView.getSelectionModel().getSelectedItem();

                                    Drink drink = new Drink();
                                    Food food = new Food();

                                    boolean Ok = false;

                                    if (selectedItem != null) {
                                        Pair<String, Integer> key = new Pair<>(place, number);
                                        List<Object> payObjectsList = payObjectsMap.get(key);

                                        payTableView.getItems().remove(selectedItem);
                                        payObjectsList.remove(selectedItem);

                                        if (selectedItem instanceof Drink)
                                        {
                                            setupTableViewForDrinks(number, place);
                                            drink = (Drink) selectedItem;
                                            Ok = true;
                                         }
                                        else if (selectedItem instanceof Food)
                                        {
                                            setupTableViewForFood(number, place);
                                            food = (Food) selectedItem;
                                        }

                                        orderView.getItems().add(selectedItem);

                                        Order order  = ordersMap.get(key);

                                        if (Ok)
                                        {
                                            order.getOrderedDrink().add(drink);

                                            payOrdersDataBase.deletePaidDrinkByNameFromdataBase(drink.getName());
                                            orderDataBase.addSimpleDrinkToDataBase(order, drink);
                                        }
                                        else
                                        {
                                            order.getOrderedFood().add(food);

                                            payOrdersDataBase.deletePaidFoodByNameFromDataBase(food.getName());
                                            orderDataBase.addSimpleFoodToDataBase(order, food);
                                        }
                                    }
                                });
                            }

                            if (item.equals("RESTORE DRINK"))
                            {
                                List<Drink> drinksToRestore = new ArrayList<>();

                                for (Object obj : payTableView.getItems())
                                    if (obj instanceof Drink)
                                        drinksToRestore.add((Drink) obj);

                                Pair<String, Integer> key = new Pair<>(place, number);
                                Order order = ordersMap.get(key);

                                if (!payTableView.getItems().isEmpty())
                                {
                                    setupTableViewForDrinks(number, place);

                                    orderView.getItems().addAll(drinksToRestore);
                                    order.getOrderedDrink().addAll(drinksToRestore);

                                    for (int i = 0; i < drinksToRestore.size(); ++i)
                                    {
                                        orderDataBase.addSimpleDrinkToDataBase(order, drinksToRestore.get(i));
                                        payOrdersDataBase.deletePaidDrinkByNameFromdataBase(drinksToRestore.get(i).getName());
                                    }
                                }

                                List<Object> payObjectsList = payObjectsMap.get(key);

                                payTableView.getItems().removeAll(drinksToRestore);
                                payObjectsList.removeAll(drinksToRestore);
                            }

                            if (item.equals("RESTORE FOOD"))
                            {
                                List<Food> foodToRestore = new ArrayList<>();

                                for (Object obj : payTableView.getItems())
                                    if (obj instanceof Food)
                                        foodToRestore.add((Food) obj);

                                Pair<String, Integer> key = new Pair<>(place, number);
                                Order order = ordersMap.get(key);

                                if (!payTableView.getItems().isEmpty())
                                {
                                    setupTableViewForFood(number, place);

                                    orderView.getItems().addAll(foodToRestore);
                                    order.getOrderedFood().addAll(foodToRestore);

                                    for (int i = 0; i < foodToRestore.size(); ++i)
                                    {
                                        orderDataBase.addSimpleFoodToDataBase(order, foodToRestore.get(i));
                                        payOrdersDataBase.deletePaidFoodByNameFromDataBase(foodToRestore.get(i).getName());
                                    }
                                }

                                List<Object> payObjectsList = payObjectsMap.get(key);

                                payTableView.getItems().removeAll(foodToRestore);
                                payObjectsList.removeAll(foodToRestore);
                            }

                            if (item.equals("ADD DRINK"))
                            {
                                setOnMouseClicked(ev -> {
                                    drinkStage.show();
                                });
                            }

                            if (item.equals("ADD FOOD"))
                            {
                                setOnMouseClicked(ev -> {
                                    foodStage.show();
                                });
                            }

                        } else {
                            setStyle(null);
                            setPadding(Insets.EMPTY);
                            setOnMouseClicked(null);
                        }
                    }
                });


                payTableView = new TableView();

                namePayColumn.setCellValueFactory(cellData -> {
                    Object item = cellData.getValue();

                    if (item instanceof Drink)
                    {
                        Drink drink = (Drink) item;
                        return new SimpleStringProperty(drink.getName());
                    }
                    else if (item instanceof Food)
                    {
                        Food food = (Food) item;
                        return new SimpleStringProperty(food.getName());
                    }
                    else
                    {
                        return new SimpleStringProperty("");
                    }
                });

                pricePayColumn.setCellValueFactory(cellData -> {
                    Object item = cellData.getValue();

                    if (item instanceof Drink)
                    {
                        Drink drink = (Drink) item;
                        return new ReadOnlyObjectWrapper<>(drink.getPrice());
                    }
                    else if (item instanceof Food) {
                        Food food = (Food) item;
                        return new ReadOnlyObjectWrapper<>(food.getPrice());
                    }
                    else {
                        return new ReadOnlyObjectWrapper<>(null);
                    }
                });

                this.setupPayTableViewCells();

                namePayColumn.setPrefWidth(297);
                namePayColumn.setStyle("-fx-alignment: CENTER;");
                payTableView.getColumns().add(namePayColumn);

                pricePayColumn.setPrefWidth(296);
                pricePayColumn.setStyle("-fx-alignment: CENTER;");
                payTableView.getColumns().add(pricePayColumn);

                payTableView.setPrefSize(595, 245);

                payAllButton = new Button("PAY ALL");
                payOnlyButton = new Button("PAY ONLY");
                payCashButton = new Button("PAY CASH");
                payCardButton = new Button("PAY CARD");
                payAllCashButton = new Button("PAY ALL CASH");
                payAllCardButton = new Button("PAY ALL CARD");

                payAllButton.setPrefSize(295, 50);
                payOnlyButton.setPrefSize(295, 50);
                payCashButton.setPrefSize(145, 50);
                payCardButton.setPrefSize(145, 50);
                payAllCashButton.setPrefSize(145, 50);
                payAllCardButton.setPrefSize(145, 50);

                {
                    payAllButton.getStyleClass().add("pay-button");
                    payOnlyButton.getStyleClass().add("pay-button");
                    payCardButton.getStyleClass().add("pay-button");
                    payCashButton.getStyleClass().add("pay-button");
                    payAllCashButton.getStyleClass().add("pay-button");
                    payAllCardButton.getStyleClass().add("pay-button");
                }

                payButtonsBox1 = new HBox(5, payAllButton, payOnlyButton);
                payButtonsBox2 = new HBox(5, payAllCashButton, payAllCardButton, payCashButton, payCardButton);

                tablesAndButtonsBox = new VBox(5, orderView, payButtonsBox1, payTableView, payButtonsBox2);
                tablesBox = new HBox(5, listView, tablesAndButtonsBox);

                this.totalSumLabel = new Label("TOTAL: " + getTotalSumToPaid(number, place) + " LEI");
                this.totalSumWrapper = new Pane(totalSumLabel);

                totalSumWrapper.setPrefWidth(400);

                totalSumLabel.layoutXProperty().bind(totalSumWrapper.widthProperty().subtract(totalSumLabel.widthProperty()).divide(2));

                {
                    totalSumLabel.getStyleClass().add("welcome-label");
                    totalSumWrapper.setStyle("-fx-background-color: #df00fe;");
                }

                this.paidSumLabel = new Label("PAID: 0,00" +  " LEI");
                this.paidSumWrapper = new Pane(paidSumLabel);

                paidSumWrapper.setPrefWidth(400);

                paidSumLabel.layoutXProperty().bind(paidSumWrapper.widthProperty().subtract(paidSumLabel.widthProperty()).divide(2));

                {
                    paidSumLabel.getStyleClass().add("welcome-label");
                    paidSumWrapper.setStyle("-fx-background-color: #df00fe;");
                }

                HBox wrappersBox = new HBox(5, totalSumWrapper, paidSumWrapper);

                mainBox = new VBox(5);
                mainBox.getChildren().addAll(titleWrapper, tablesBox, wrappersBox, okButton);

                scene = new Scene(mainBox, 800, 760);
                scene.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

                stage = new Stage();
                stage.setScene(scene);

                okButton.setOnAction(ev -> {
                    stage.close();
                });

                stage.show();

                payOnlyButton.setOnAction(ev -> {
                    Object selectedItem = orderView.getSelectionModel().getSelectedItem();

                    cardButtonIsPressed = false;
                    cashButtonIsPressed = false;
                    allCardButtonIsPressed = false;
                    allCashButtonIsPressed = false;

                    Drink drink = new Drink();
                    Food food = new Food();

                    boolean Ok = false;

                    if (selectedItem != null)
                    {
                        if (selectedItem instanceof Drink)
                        {
                            drink = (Drink) selectedItem;
                            Ok = true;
                        }
                        else if (selectedItem instanceof Food)
                            food = (Food) selectedItem;

                        orderView.getItems().remove(selectedItem);

                        Pair<String, Integer> key = new Pair<>(place, number);
                        Order order = ordersMap.get(key);

                        if (Ok)
                        {
                            for (int j = 0; j < order.getOrderedDrink().size(); ++j)
                                if (order.getOrderedDrink().get(j).getName().equals(drink.getName()))
                                    order.getOrderedDrink().remove(drink);

                            orderDataBase.deleteOrderedDrinkByNameFromdataBase(drink.getName());
                            payOrdersDataBase.addDrinkToDataBase(order, drink);

                            if (!payOrdersDataBase.doesOrderExist(order.getId()))
                                payOrdersDataBase.addOrderToDataBase(order);
                        }
                        else
                        {
                            for (int j = 0; j < order.getOrderedFood().size(); ++j)
                                if (order.getOrderedFood().get(j).getName().equals(food.getName()))
                                    order.getOrderedFood().remove(food);

                            orderDataBase.deleteOrderedFoodByNameFromDataBase(food.getName());
                            payOrdersDataBase.addFoodToDataBase(order, food);

                            if (!payOrdersDataBase.doesOrderExist(order.getId()))
                                payOrdersDataBase.addOrderToDataBase(order);
                        }

                        /*
                        if (order.getOrderedDrink().isEmpty() && order.getOrderedFood().isEmpty())
                            orderDataBase.deleteOrderFromDataBase(order);


                         */
                        List<Object> payObjectsList = payObjectsMap.get(key);
                        payObjectsList.add(selectedItem);

                        payTableView.getItems().add(selectedItem);
                    }
                });

                payAllButton.setOnAction(ev -> {
                    ObservableList<Object> observableList = FXCollections.observableArrayList();

                    cardButtonIsPressed = false;
                    cashButtonIsPressed = false;
                    allCardButtonIsPressed = false;
                    allCashButtonIsPressed = false;

                    Pair<String, Integer> key = new Pair<>(place, number);
                    Order order = ordersMap.get(key);

                    if (order.getName() != null) {
                        observableList.addAll(order.getOrderedDrink());
                        observableList.addAll(order.getOrderedFood());

                        List<Object> payObjectsList = payObjectsMap.get(key);

                        payObjectsList.addAll(order.getOrderedDrink());
                        payObjectsList.addAll(order.getOrderedFood());

                        payTableView.setItems(observableList);

                        orderView.getItems().clear();

                        for (int i = 0; i < order.getOrderedDrink().size(); ++i)
                        {
                            orderDataBase.deleteOrderedDrinkByNameFromdataBase(order.getOrderedDrink().get(i).getName());
                            payOrdersDataBase.addDrinkToDataBase(order, order.getOrderedDrink().get(i));
                        }

                        for (int i = 0; i < order.getOrderedFood().size(); ++i)
                        {
                            orderDataBase.deleteOrderedFoodByNameFromDataBase(order.getOrderedFood().get(i).getName());
                            payOrdersDataBase.addFoodToDataBase(order, order.getOrderedFood().get(i));
                        }

                        //orderDataBase.deleteOrderFromDataBase(order);

                        if (!payOrdersDataBase.doesOrderExist(order.getId()))
                            payOrdersDataBase.addOrderToDataBase(order);

                        order.getOrderedDrink().clear();
                        order.getOrderedFood().clear();
                    }
                });

                payCashButton.setOnAction(ev -> {
                    Object selectedItem = payTableView.getSelectionModel().getSelectedItem();
                    this.cashButtonIsPressed = true;

                    if (selectedItem != null)
                    {
                        boolean Ok = false;

                        Drink drink = new Drink();
                        Food food = new Food();

                        if (selectedItem instanceof Drink)
                        {
                            drink = (Drink) selectedItem;
                            Ok = true;
                        }
                        else if (selectedItem instanceof Food)
                        {
                            food = (Food) selectedItem;
                        }

                        Pair<String, Integer> key = new Pair<>(place, number);
                        List<Object> payObjectsList = payObjectsMap.get(key);
                        Order order = ordersMap.get(key);

                        this.setupPayTableViewCells();
                        PauseTransition delay = new PauseTransition(Duration.seconds(1));

                        delay.setOnFinished(event -> {
                            ObservableList<Object> items = payTableView.getItems();
                            items.remove(selectedItem);
                            payTableView.refresh();

                            if (order.getOrderedDrink().isEmpty() && order.getOrderedFood().isEmpty() && payTableView.getItems().isEmpty())
                            {
                                orderDataBase.deleteOrderFromDataBase(order);
                                orders.remove(order);

                                LiveOrdersController.LiveOrdersPane liveOrdersPane = liveOrdersController.getLiveOrdersPane();
                                liveOrdersPane.getOrdersList().getItems().remove(order);
                            }
                        });

                        delay.play();

                        if (Ok)
                        {
                            payObjectsList.remove(drink);
                            payOrdersDataBase.deletePaidDrinkByNameFromdataBase(drink.getName());

                            order.setPrice(order.getPrice() - drink.getPrice());

                            if (order.getPrice() == 0)
                                order.setPaid(true);

                            orderDataBase.updateOrderFromDataBase(order.getName(), order);

                            Payment payment = new Payment(order.getName(), drink.getPrice(), "Cash");

                            paymentDataBase.addPaymentToDataBase(payment);
                            payments.add(payment);

                            ViewerController.PaymentViewer paymentViewer = viewerController.getPaymentViewer();
                            paymentViewer.getPaymentsList().getItems().add(payment);
                        }
                        else
                        {
                            payObjectsList.remove(food);
                            payOrdersDataBase.deletePaidFoodByNameFromDataBase(food.getName());

                            order.setPrice(order.getPrice() - food.getPrice());

                            if (order.getPrice() == 0)
                                order.setPaid(true);

                            orderDataBase.updateOrderFromDataBase(order.getName(), order);

                            Payment payment = new Payment(order.getName(), food.getPrice(), "Cash");

                            paymentDataBase.addPaymentToDataBase(payment);
                            payments.add(payment);

                            ViewerController.PaymentViewer paymentViewer = viewerController.getPaymentViewer();
                            paymentViewer.getPaymentsList().getItems().add(payment);
                        }

                        paidSumLabel.setText("PAID: " + this.getSingleItemPaid(selectedItem) + " LEI");
                        totalSumLabel.setText("TOTAL: " + getTotalSumToPaid(number, place) + " LEI");

                        totalAmountViewer.getTotalAmountLabel().setText("TOTAL AMOUNT: " + totalAmountViewer.getTotalAmount() + " LEI");
                        totalAmountViewer.getCashLabel().setText("CASH: " + totalAmountViewer.getTotalCash() + " LEI");
                        totalAmountViewer.getCardLabel().setText("CARD: " + totalAmountViewer.getTotalCard() + " LEI");
                    }
                });

                payCardButton.setOnAction(ev -> {
                    Object selectedItem = payTableView.getSelectionModel().getSelectedItem();
                    this.cardButtonIsPressed = true;

                    if (selectedItem != null)
                    {
                        boolean Ok = false;

                        Drink drink = new Drink();
                        Food food = new Food();

                        if (selectedItem instanceof Drink)
                        {
                            drink = (Drink) selectedItem;
                            Ok = true;
                        }
                        else if (selectedItem instanceof Food)
                        {
                            food = (Food) selectedItem;
                        }

                        Pair<String, Integer> key = new Pair<>(place, number);
                        List<Object> payObjectsList = payObjectsMap.get(key);
                        Order order = ordersMap.get(key);

                        this.setupPayTableViewCells();
                        PauseTransition delay = new PauseTransition(Duration.seconds(1));

                        delay.setOnFinished(event -> {
                            ObservableList<Object> items = payTableView.getItems();
                            items.remove(selectedItem);
                            payTableView.refresh();

                            if (order.getOrderedDrink().isEmpty() && order.getOrderedFood().isEmpty() && payTableView.getItems().isEmpty())
                            {
                                orderDataBase.deleteOrderFromDataBase(order);
                                orders.remove(order);

                                LiveOrdersController.LiveOrdersPane liveOrdersPane = liveOrdersController.getLiveOrdersPane();
                                liveOrdersPane.getOrdersList().getItems().remove(order);
                            }
                        });

                        delay.play();

                        if (Ok)
                        {
                            payObjectsList.remove(drink);
                            payOrdersDataBase.deletePaidDrinkByNameFromdataBase(drink.getName());

                            order.setPrice(order.getPrice() - drink.getPrice());

                            if (order.getPrice() == 0)
                                order.setPaid(true);

                            orderDataBase.updateOrderFromDataBase(order.getName(), order);

                            Payment payment = new Payment(order.getName(), drink.getPrice(), "Card");

                            paymentDataBase.addPaymentToDataBase(payment);
                            payments.add(payment);

                            ViewerController.PaymentViewer paymentViewer = viewerController.getPaymentViewer();
                            paymentViewer.getPaymentsList().getItems().add(payment);
                        }
                        else
                        {
                            payObjectsList.remove(food);
                            payOrdersDataBase.deletePaidFoodByNameFromDataBase(food.getName());

                            order.setPrice(order.getPrice() - food.getPrice());

                            if (order.getPrice() == 0)
                                order.setPaid(true);

                            orderDataBase.updateOrderFromDataBase(order.getName(), order);

                            Payment payment = new Payment(order.getName(), food.getPrice(), "Card");

                            paymentDataBase.addPaymentToDataBase(payment);
                            payments.add(payment);

                            ViewerController.PaymentViewer paymentViewer = viewerController.getPaymentViewer();
                            paymentViewer.getPaymentsList().getItems().add(payment);
                        }

                        paidSumLabel.setText("PAID: " + this.getSingleItemPaid(selectedItem) + " LEI");
                        totalSumLabel.setText("TOTAL: " + getTotalSumToPaid(number, place) + " LEI");

                        totalAmountViewer.getTotalAmountLabel().setText("TOTAL AMOUNT: " + totalAmountViewer.getTotalAmount() + " LEI");
                        totalAmountViewer.getCashLabel().setText("CASH: " + totalAmountViewer.getTotalCash() + " LEI");
                        totalAmountViewer.getCardLabel().setText("CARD: " + totalAmountViewer.getTotalCard() + " LEI");
                    }
                });

                payAllCashButton.setOnAction(ev -> {
                    List<Object> itemsToPay = new ArrayList<>();

                    this.allCashButtonIsPressed = true;

                    itemsToPay.addAll(payTableView.getItems());

                    this.setupPayTableViewCells();

                    Pair<String, Integer> key = new Pair<>(place, number);
                    List<Object> payObjectsList = payObjectsMap.get(key);
                    Order order = ordersMap.get(key);

                    PauseTransition delay = new PauseTransition(Duration.seconds(1));

                    delay.setOnFinished(event -> {
                        payTableView.getItems().clear();
                        payTableView.refresh();

                        if (order.getOrderedDrink().isEmpty() && order.getOrderedFood().isEmpty() && payTableView.getItems().isEmpty())
                        {
                            orderDataBase.deleteOrderFromDataBase(order);
                            orders.remove(order);

                            LiveOrdersController.LiveOrdersPane liveOrdersPane = liveOrdersController.getLiveOrdersPane();
                            liveOrdersPane.getOrdersList().getItems().remove(order);
                        }
                    });

                    delay.play();

                    int price = 0;

                    for (int i = 0; i < payObjectsList.size(); ++i)
                    {
                        if (payObjectsList.get(i) instanceof Drink)
                        {
                            payOrdersDataBase.deletePaidDrinkByNameFromdataBase(((Drink) payObjectsList.get(i)).getName());
                            price += ((Drink) payObjectsList.get(i)).getPrice();
                        }
                        else if (payObjectsList.get(i) instanceof Food)
                        {
                            payOrdersDataBase.deletePaidFoodByNameFromDataBase(((Food) payObjectsList.get(i)).getName());
                            price += ((Food) payObjectsList.get(i)).getPrice();
                        }
                    }

                    payObjectsList.removeAll(itemsToPay);

                    paidSumLabel.setText("PAID: " + this.getAllItemsPaid() + " LEI");
                    totalSumLabel.setText("TOTAL: " + getTotalSumToPaid(number, place) + " LEI");

                    order.setPrice(order.getPrice() - price);

                    if (order.getPrice() == 0)
                        order.setPaid(true);

                    orderDataBase.updateOrderFromDataBase(order.getName(), order);

                    Payment payment = new Payment(order.getName(), price, "Cash");

                    payments.add(payment);
                    paymentDataBase.addPaymentToDataBase(payment);

                    ViewerController.PaymentViewer paymentViewer = viewerController.getPaymentViewer();
                    paymentViewer.getPaymentsList().getItems().add(payment);

                    totalAmountViewer.getTotalAmountLabel().setText("TOTAL AMOUNT: " + totalAmountViewer.getTotalAmount() + " LEI");
                    totalAmountViewer.getCashLabel().setText("CASH: " + totalAmountViewer.getTotalCash() + " LEI");
                    totalAmountViewer.getCardLabel().setText("CARD: " + totalAmountViewer.getTotalCard() + " LEI");
                });

                payAllCardButton.setOnAction(ev -> {
                    List<Object> itemsToPay = new ArrayList<>();

                    this.allCardButtonIsPressed = true;

                    itemsToPay.addAll(payTableView.getItems());

                    this.setupPayTableViewCells();

                    Pair<String, Integer> key = new Pair<>(place, number);
                    List<Object> payObjectsList = payObjectsMap.get(key);
                    Order order = ordersMap.get(key);

                    PauseTransition delay = new PauseTransition(Duration.seconds(1));

                    delay.setOnFinished(event -> {
                        payTableView.getItems().clear();
                        payTableView.refresh();

                        if (order.getOrderedDrink().isEmpty() && order.getOrderedFood().isEmpty() && payTableView.getItems().isEmpty())
                        {
                            orderDataBase.deleteOrderFromDataBase(order);
                            orders.remove(order);

                            LiveOrdersController.LiveOrdersPane liveOrdersPane = liveOrdersController.getLiveOrdersPane();
                            liveOrdersPane.getOrdersList().getItems().remove(order);
                        }
                    });

                    delay.play();

                    int price = 0;

                    for (int i = 0; i < payObjectsList.size(); ++i)
                    {
                        if (payObjectsList.get(i) instanceof Drink)
                        {
                            payOrdersDataBase.deletePaidDrinkByNameFromdataBase(((Drink) payObjectsList.get(i)).getName());
                            price += ((Drink) payObjectsList.get(i)).getPrice();
                        }
                        else if (payObjectsList.get(i) instanceof Food)
                        {
                            payOrdersDataBase.deletePaidFoodByNameFromDataBase(((Food) payObjectsList.get(i)).getName());
                            price += ((Food) payObjectsList.get(i)).getPrice();
                        }
                    }

                    payObjectsList.removeAll(itemsToPay);

                    paidSumLabel.setText("PAID: " + this.getAllItemsPaid() + " LEI");
                    totalSumLabel.setText("TOTAL: " + getTotalSumToPaid(number, place) + " LEI");

                    order.setPrice(order.getPrice() - price);

                    if (order.getPrice() == 0)
                        order.setPaid(true);

                    orderDataBase.updateOrderFromDataBase(order.getName(), order);

                    Payment payment = new Payment(order.getName(), price, "Card");

                    payments.add(payment);
                    paymentDataBase.addPaymentToDataBase(payment);

                    ViewerController.PaymentViewer paymentViewer = viewerController.getPaymentViewer();
                    paymentViewer.getPaymentsList().getItems().add(payment);

                    totalAmountViewer.getTotalAmountLabel().setText("TOTAL AMOUNT: " + totalAmountViewer.getTotalAmount() + " LEI");
                    totalAmountViewer.getCashLabel().setText("CASH: " + totalAmountViewer.getTotalCash() + " LEI");
                    totalAmountViewer.getCardLabel().setText("CARD: " + totalAmountViewer.getTotalCard() + " LEI");
                });

                this.loadPayTableView(number, place);
            });
        }

        public void setupTable1()
        {
            setupAnyTable(table1, 1, "inside");
        }

        public void setupTable2()
        {
            setupAnyTable(table2, 2, "inside");
        }

        public void setupTable3()
        {
            setupAnyTable(table3, 3, "inside");
        }

        public void setupTable4()
        {
            setupAnyTable(table4, 4, "inside");
        }

        public void setupTable5()
        {
            setupAnyTable(table5, 5, "inside");
        }

        public void setupTable6()
        {
            setupAnyTable(table6, 6, "inside");
        }

        public void setupTable7()
        {
            setupAnyTable(table7, 7, "inside");
        }

        public void setupTable8()
        {
            setupAnyTable(table8, 8, "inside");
        }

        public void setupTable9()
        {
            setupAnyTable(table9, 9, "inside");
        }

        public void setupTable10()
        {
            setupAnyTable(table10, 10, "inside");
        }

        public void setupTable11()
        {
            setupAnyTable(table11, 11, "inside");
        }

        public void setupTable12()
        {
            setupAnyTable(table12, 12, "inside");
        }

        public void setupTable13()
        {
            setupAnyTable(table13, 13, "inside");
        }

        public void setupTable14()
        {
            setupAnyTable(table14, 14, "inside");
        }

        public void setupTable15()
        {
            setupAnyTable(table15, 15, "inside");
        }

        public void setupTable16()
        {
            setupAnyTable(table16, 16, "inside");
        }

        public void setupTable17()
        {
            setupAnyTable(table17, 17, "inside");
        }

        public void setupTable18()
        {
            setupAnyTable(table18, 18, "inside");
        }

        public void setupTable19()
        {
            setupAnyTable(table19, 19, "inside");
        }

        public void setupTerraceTable1()
        {
            setupAnyTable(terraceController.getTable1(), 1, "terrace");
        }

        public void setupTerraceTable2()
        {
            setupAnyTable(terraceController.getTable2(), 2, "terrace");
        }

        public void setupTerraceTable3()
        {
            setupAnyTable(terraceController.getTable3(), 3, "terrace");
        }

        public void setupTerraceTable4()
        {
            setupAnyTable(terraceController.getTable4(), 4, "terrace");
        }

        public void setupTerraceTable5()
        {
            setupAnyTable(terraceController.getTable5(), 5, "terrace");
        }

        public void setupTerraceTable6()
        {
            setupAnyTable(terraceController.getTable6(), 6, "terrace");
        }

        public void setupTerraceTable7()
        {
            setupAnyTable(terraceController.getTable7(), 7, "terrace");
        }

        public void setupTerraceTable8()
        {
            setupAnyTable(terraceController.getTable8(), 8, "terrace");
        }

        public void setupTerraceTable9()
        {
            setupAnyTable(terraceController.getTable9(), 9, "terrace");
        }

        public void setupTerraceTable10()
        {
            setupAnyTable(terraceController.getTable10(), 10, "terrace");
        }

        public void setupTerraceTable11()
        {
            setupAnyTable(terraceController.getTable11(), 11, "terrace");
        }

        public void setupTerraceTable12()
        {
            setupAnyTable(terraceController.getTable12(), 12, "terrace");
        }

        public void setupTerraceTable13()
        {
            setupAnyTable(terraceController.getTable13(), 13, "terrace");
        }

        public void setupTerraceTable14()
        {
            setupAnyTable(terraceController.getTable14(), 14, "terrace");
        }

        public void setupTerraceTable15()
        {
            setupAnyTable(terraceController.getTable15(), 15, "terrace");
        }

        public void setupTerraceTable16()
        {
            setupAnyTable(terraceController.getTable16(), 16, "terrace");
        }

        public void setupTerraceTable17()
        {
            setupAnyTable(terraceController.getTable17(), 17, "terrace");
        }

        public void setupTerraceTable18()
        {
            setupAnyTable(terraceController.getTable18(), 18, "terrace");
        }

        public void setupPayTableViewCells()
        {
            namePayColumn.setCellFactory(column -> new TableCell<Object, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        getStyleClass().add("orders-label");

                        if (cashButtonIsPressed || cardButtonIsPressed || allCardButtonIsPressed || allCashButtonIsPressed)
                            setStyle("-fx-background-color: rgb(25,128,41); -fx-alignment: center;");
                        else
                            setStyle("-fx-background-color: rgb(0,208,255); -fx-alignment: center;");
                    }
                }
            });

            pricePayColumn.setCellFactory(column -> new TableCell<Object, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(String.valueOf(item + " LEI"));
                        getStyleClass().add("orders-label");

                        if (cashButtonIsPressed || cardButtonIsPressed || allCardButtonIsPressed || allCashButtonIsPressed)
                            setStyle("-fx-background-color: rgb(25,128,41); -fx-alignment: center;");
                        else
                            setStyle("-fx-background-color: rgb(0,208,255); -fx-alignment: center;");
                    }
                }
            });
        }

        public void setupTableViewForDrinks(int tableNumber, String place)
        {
            orderView.getColumns().clear();

            nameDrinkColumn.setPrefWidth(198);
            nameDrinkColumn.setStyle("-fx-alignment: CENTER;");
            orderView.getColumns().add(nameDrinkColumn);

            volumeColumn.setPrefWidth(198);
            volumeColumn.setStyle("-fx-alignment: CENTER;");
            orderView.getColumns().add(volumeColumn);

            priceDrinkColumn.setPrefWidth(198);
            priceDrinkColumn.setStyle("-fx-alignment: CENTER;");
            orderView.getColumns().add(priceDrinkColumn);

            ObservableList<Drink> drinks = displayOrderedDrinks(tableNumber, place);
            orderView.setItems(drinks);
        }

        public void setupTableViewForFood(int tableNumber, String place)
        {
            orderView.getColumns().clear();

            nameFoodColumn.setPrefWidth(198);
            nameFoodColumn.setStyle("-fx-alignment: CENTER;");
            orderView.getColumns().add(nameFoodColumn);

            gramsColumn.setPrefWidth(198);
            gramsColumn.setStyle("-fx-alignment: CENTER;");
            orderView.getColumns().add(gramsColumn);

            priceFoodColumn.setPrefWidth(198);
            priceFoodColumn.setStyle("-fx-alignment: CENTER;");
            orderView.getColumns().add(priceFoodColumn);

            ObservableList<Food> drinks = displayOrderedFood(tableNumber, place);
            orderView.setItems(drinks);
        }

        public ObservableList<Drink> displayOrderedDrinks(int tableNumber, String place)
        {
            ObservableList<Drink> drinks = FXCollections.observableArrayList();

            Pair<String, Integer> key = new Pair<>(place, tableNumber);
            Order order = ordersMap.get(key);

            if (order != null) {
                drinks.addAll(order.getOrderedDrink());
            }

            return drinks;
        }

        public ObservableList<Food> displayOrderedFood(int tableNumber, String place)
        {
            ObservableList<Food> drinks = FXCollections.observableArrayList();

            Pair<String, Integer> key = new Pair<>(place, tableNumber);
            Order order = ordersMap.get(key);

            if (order != null) {
                drinks.addAll(order.getOrderedFood());
            }

            return drinks;
        }

        public String getTotalSumToPaid(int tableNumber, String place)
        {
            float sum = 0;

            Pair<String, Integer> key = new Pair<>(place, tableNumber);
            Order order = ordersMap.get(key);

            if (order != null)
            {
                if (order.getOrderedDrink() != null)
                {
                    for (int i = 0; i < order.getOrderedDrink().size(); ++i)
                        sum += order.getOrderedDrink().get(i).getPrice();
                }

                if (order.getOrderedFood() != null)
                {
                    for (int j = 0; j < order.getOrderedFood().size(); ++j)
                        sum += order.getOrderedFood().get(j).getPrice();
                }
            }

            for (Object object : payTableView.getItems())
            {
                if (object instanceof Drink)
                    sum += ((Drink) object).getPrice();
                else if (object instanceof Food)
                    sum += ((Food) object).getPrice();
            }

            return String.format("%.2f", sum);
        }

        public String getSingleItemPaid(Object item)
        {
            float sum = 0;

            Drink drink = new Drink();
            Food food = new Food();

            boolean Ok = false;

            if (item instanceof Drink)
            {
                drink = (Drink) item;
                Ok = true;
            }
            else if (item instanceof Food)
                food = (Food) item;

            if (Ok)
                sum += drink.getPrice();
            else
                sum += food.getPrice();

            return String.format("%.2f", sum);
        }

        public String getAllItemsPaid()
        {
            float sum = 0;

            for (Object object : payTableView.getItems())
            {
                if (object instanceof Drink)
                    sum += ((Drink) object).getPrice();
                else if (object instanceof Food)
                    sum += ((Food) object).getPrice();
            }

            return String.format("%.2f", sum);
        }

        private void loadPayTableView(int number, String place)
        {
            Pair<String, Integer> key = new Pair<>(place, number);

            List<Object> payObjectsList = payObjectsMap.get(key);

            if (payObjectsList != null && !payObjectsList.isEmpty())
            {
                ObservableList<Object> observableList = FXCollections.observableArrayList(payObjectsList);
                payTableView.setItems(observableList);

                System.out.println("OK");
            }
        }

        public void setupAddDrinkToOrder()
        {
            searchDrinkField = new TextField();
            searchDrinkField.setPrefSize(350, 50);
            searchDrinkField.setPromptText("Search...");
            searchDrinkField.setStyle("-fx-font-size: 20px;");
            searchDrinkField.getStyleClass().add("drinks-label");

            searchDrinkButton = new Button("SEARCH");
            searchDrinkButton.setPrefSize(100, 50);

            closeDrinkButton = new Button("CLOSE");
            closeDrinkButton.setPrefSize(225, 50);

            addDrinkButton = new Button("ADD");
            addDrinkButton.setPrefSize(225, 50);

            {
                searchDrinkButton.getStyleClass().add("interface-button");
                closeDrinkButton.getStyleClass().add("interface-button");
                addDrinkButton.getStyleClass().add("interface-button");
            }

            drinkTableView = new TableView<>();
            drinkTableView.setPrefSize(455, 450);

            TableColumn<Drink, String> nameDrinkColumn = new TableColumn<>("Name");
            TableColumn<Drink, Double> volumeDrinkColumn = new TableColumn<>("Volume");
            TableColumn<Drink, Integer> priceDrinkColumn = new TableColumn<>("Price");

            nameDrinkColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
            volumeDrinkColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getVolume()));
            priceDrinkColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrice()));

            nameDrinkColumn.setPrefWidth(150);
            nameDrinkColumn.setStyle("-fx-alignment: CENTER;");
            drinkTableView.getColumns().add(nameDrinkColumn);

            volumeDrinkColumn.setPrefWidth(150);
            volumeDrinkColumn.setStyle("-fx-alignment: CENTER;");
            drinkTableView.getColumns().add(volumeDrinkColumn);

            priceDrinkColumn.setPrefWidth(150);
            priceDrinkColumn.setStyle("-fx-alignment: CENTER;");
            drinkTableView.getColumns().add(priceDrinkColumn);

            nameDrinkColumn.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        getStyleClass().add("orders-label");
                        setStyle("-fx-background-color: rgb(0,208,255); -fx-alignment: center;");
                    }
                }
            });

            volumeDrinkColumn.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(String.valueOf(item + " ML"));
                        getStyleClass().add("orders-label");
                        setStyle("-fx-background-color: rgb(0,208,255); -fx-alignment: center;");
                    }
                }
            });

            priceDrinkColumn.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(String.valueOf(item + " LEI"));
                        getStyleClass().add("orders-label");
                        setStyle("-fx-background-color: rgb(0,208,255); -fx-alignment: center;");
                    }
                }
            });

            ObservableList<Drink> observableList = FXCollections.observableArrayList(allDrinksToSearch);
            drinkTableView.setItems(observableList);

            searchDrinkBox = new HBox(5, searchDrinkField, searchDrinkButton);
            buttonsDrinkBox = new HBox(5, closeDrinkButton, addDrinkButton);
            fullDrinkBox = new VBox(5, searchDrinkBox, drinkTableView, buttonsDrinkBox);

            drinkScene = new Scene(fullDrinkBox, 455, 560);
            drinkScene.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

            drinkStage = new Stage();
            drinkStage.setScene(drinkScene);
        }

        public void setupAddFoodToOrder()
        {
            searchFoodField = new TextField();
            searchFoodField.setPrefSize(350, 50);
            searchFoodField.setPromptText("Search...");
            searchFoodField.setStyle("-fx-font-size: 20px;");
            searchFoodField.getStyleClass().add("drinks-label");

            searchFoodButton = new Button("SEARCH");
            searchFoodButton.setPrefSize(100, 50);

            closeFoodButton = new Button("CLOSE");
            closeFoodButton.setPrefSize(225, 50);

            addFoodButton = new Button("ADD");
            addFoodButton.setPrefSize(225, 50);

            {
                searchFoodButton.getStyleClass().add("interface-button");
                closeFoodButton.getStyleClass().add("interface-button");
                addFoodButton.getStyleClass().add("interface-button");
            }

            foodTableView = new TableView<>();
            foodTableView.setPrefSize(455, 450);

            TableColumn<Food, String> nameFoodColumn = new TableColumn<>("Name");
            nameFoodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

            TableColumn<Food, Integer> gramsFoodColumn = new TableColumn<>("Grams");
            gramsFoodColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getGrams()));

            TableColumn<Food, Integer> priceFoodColumn = new TableColumn<>("Price");
            priceFoodColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrice()));

            nameFoodColumn.setPrefWidth(150);
            nameFoodColumn.setStyle("-fx-alignment: CENTER;");
            foodTableView.getColumns().add(nameFoodColumn);

            gramsFoodColumn.setPrefWidth(150);
            gramsFoodColumn.setStyle("-fx-alignment: CENTER;");
            foodTableView.getColumns().add(gramsFoodColumn);

            priceFoodColumn.setPrefWidth(150);
            priceFoodColumn.setStyle("-fx-alignment: CENTER;");
            foodTableView.getColumns().add(priceFoodColumn);

            nameFoodColumn.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        getStyleClass().add("orders-label");
                        setStyle("-fx-background-color: rgb(0,208,255); -fx-alignment: center;");
                    }
                }
            });

            gramsFoodColumn.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(String.valueOf(item + " GR"));
                        getStyleClass().add("orders-label");
                        setStyle("-fx-background-color: rgb(0,208,255); -fx-alignment: center;");
                    }
                }
            });

            priceFoodColumn.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(String.valueOf(item + " LEI"));
                        getStyleClass().add("orders-label");
                        setStyle("-fx-background-color: rgb(0,208,255); -fx-alignment: center;");
                    }
                }
            });

            ObservableList<Food> observableList = FXCollections.observableArrayList(allFoodToSearch);
            foodTableView.setItems(observableList);

            searchFoodBox = new HBox(5, searchFoodField, searchFoodButton);
            buttonsFoodBox = new HBox(5, closeFoodButton, addFoodButton);
            fullFoodBox = new VBox(5, searchFoodBox, foodTableView, buttonsFoodBox);

            foodScene = new Scene(fullFoodBox, 455, 560);
            foodScene.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

            foodStage = new Stage();
            foodStage.setScene(foodScene);
        }

        public List<Drink> searchDrinkByName()
        {
            List<Drink> filteredDrinks = new ArrayList<>();

            String textToSearch = searchDrinkField.getText().trim().toLowerCase();

            for (Drink drink : allDrinksToSearch)
                if (drink.getName().toLowerCase().contains(textToSearch))
                    filteredDrinks.add(drink);

            return filteredDrinks;
        }

        public void setupCloseDrinkButton()
        {
            this.closeDrinkButton.setOnAction(e -> {
                drinkStage.close();
            });
        }

        public void setupSearchDrinkButton()
        {
            this.searchDrinkButton.setOnAction(e -> {
                ObservableList<Drink> observableList = FXCollections.observableArrayList(this.searchDrinkByName());
                drinkTableView.setItems(observableList);
            });
        }

        public void setupAddDrinkButton(int number, String place)
        {
            this.addDrinkButton.setOnAction(e -> {
                Drink selectedDrink = drinkTableView.getSelectionModel().getSelectedItem();

                if (!drinkButtonIsPressed)
                {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("WRONG CATEGORY");
                    alert.setHeaderText(null);
                    alert.setContentText("Please select drink category!");

                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

                    Label contentLabel = (Label) dialogPane.lookup(".content");
                    if (contentLabel != null) {
                        contentLabel.getStyleClass().add("alert-content-text");
                    }

                    alert.showAndWait();
                }

                if (drinkButtonIsPressed)
                {
                    if (selectedDrink != null)
                    {
                        orderView.getItems().add(selectedDrink);

                        Pair<String, Integer> key = new Pair<>(place, number);
                        Order order = ordersMap.get(key);

                        System.out.println(order);

                        if (order.getName() != null)
                        {
                            order.getOrderedDrink().add(selectedDrink);
                            orderDataBase.addSimpleDrinkToDataBase(order, selectedDrink);
                            orderHistoryDataBase.addSimpleDrinkToDataBase(order, selectedDrink);

                            order.setPrice(order.getPrice() + selectedDrink.getPrice());
                        }
                        else
                        {
                            orderedDrink.add(selectedDrink);

                            LocalDateTime now = LocalDateTime.now();
                            order = new Order(indexManager.getCurrentIndex(), "ORDER " + indexManager.getCurrentIndex(), selectedDrink.getPrice(), now, orderedDrink, orderedFood);
                            indexManager.increment();
                            order.setTableNumber(number);
                            order.setPlaceName(place);
                            order.setPayMethod("Card");

                            LiveOrdersController.LiveOrdersPane liveOrdersPane = liveOrdersController.getLiveOrdersPane();
                            liveOrdersPane.getOrdersList().getItems().add(order);

                            ViewerController.OrdersViewer ordersViewer = viewerController.getOrdersViewer();
                            ordersViewer.getAllOrdersView().getItems().add(order);

                            orders.add(order);
                            ordersHistory.add(order);

                            try {
                                indexManager.saveIndex();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }

                            orderDataBase.addOrderToDataBase(order);
                            orderHistoryDataBase.addOrderToDataBase(order);

                            ordersMap.put(key, order);

                            System.out.println("NEW ORDER");
                        }
                    }

                    totalSumLabel.setText("TOTAL: " + getTotalSumToPaid(number, place) + " LEI");

                    drinkStage.close();
                }
            });
        }

        public List<Food> searchFoodByName()
        {
            List<Food> filteredFood = new ArrayList<>();

            String textToSearch = searchFoodField.getText().trim().toLowerCase();

            for (Food food : allFoodToSearch)
                if (food.getName().toLowerCase().contains(textToSearch))
                    filteredFood.add(food);

            return filteredFood;
        }

        public void setupCloseFoodButton()
        {
            this.closeFoodButton.setOnAction(e -> {
                foodStage.close();
            });
        }

        public void setupSearchFoodButton()
        {
            this.searchFoodButton.setOnAction(e -> {
                ObservableList<Food> observableList = FXCollections.observableArrayList(this.searchFoodByName());
                foodTableView.setItems(observableList);
            });
        }

        public void setupAddFoodButton(int number, String place)
        {
            this.addFoodButton.setOnAction(e -> {
                Food selectedFood = foodTableView.getSelectionModel().getSelectedItem();

                if (!foodButtonIsPressed)
                {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("WRONG CATEGORY");
                    alert.setHeaderText(null);
                    alert.setContentText("Please select food category!");

                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

                    Label contentLabel = (Label) dialogPane.lookup(".content");
                    if (contentLabel != null) {
                        contentLabel.getStyleClass().add("alert-content-text");
                    }

                    alert.showAndWait();
                }

                if (foodButtonIsPressed)
                {
                    if (selectedFood != null)
                    {
                        orderView.getItems().add(selectedFood);

                        Pair<String, Integer> key = new Pair<>(place, number);
                        Order order = ordersMap.get(key);

                        System.out.println(order);

                        if (order.getName() != null)
                        {
                            order.getOrderedFood().add(selectedFood);
                            orderDataBase.addSimpleFoodToDataBase(order, selectedFood);
                            orderHistoryDataBase.addSimpleFoodToDataBase(order, selectedFood);

                            order.setPrice(order.getPrice() + selectedFood.getPrice());
                        }
                        else
                        {
                            orderedFood.add(selectedFood);

                            LocalDateTime now = LocalDateTime.now();
                            order = new Order(indexManager.getCurrentIndex(), "ORDER " + indexManager.getCurrentIndex(), selectedFood.getPrice(), now, orderedDrink, orderedFood);
                            indexManager.increment();
                            order.setTableNumber(number);
                            order.setPlaceName(place);
                            order.setPayMethod("Card");

                            LiveOrdersController.LiveOrdersPane liveOrdersPane = liveOrdersController.getLiveOrdersPane();
                            liveOrdersPane.getOrdersList().getItems().add(order);

                            ViewerController.OrdersViewer ordersViewer = viewerController.getOrdersViewer();
                            ordersViewer.getAllOrdersView().getItems().add(order);

                            orders.add(order);
                            ordersHistory.add(order);

                            try {
                                indexManager.saveIndex();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }

                            orderDataBase.addOrderToDataBase(order);
                            orderHistoryDataBase.addOrderToDataBase(order);

                            ordersMap.put(key, order);

                            System.out.println("NEW ORDER");
                        }
                    }

                    totalSumLabel.setText("TOTAL: " + getTotalSumToPaid(number, place) + " LEI");

                    foodStage.close();
                }
            });
        }

        public VBox getTablesBox2() {
            return tableBox2;
        }

        public VBox getTableBox5() {
            return tableBox5;
        }

        public TableView getOrderView() {
            return orderView;
        }
    }

    public class TerraceController {
        private Button table1;
        private Button table2;
        private Button table3;
        private Button table4;
        private Button table5;
        private Button table6;
        private Button table7;
        private Button table8;
        private Button table9;
        private Button table10;
        private Button table11;
        private Button table12;
        private Button table13;
        private Button table14;
        private Button table15;
        private Button table16;
        private Button table17;
        private Button table18;
        private Button door;
        private HBox tableBox1;
        private VBox tableBox2;
        private HBox tableBox3;
        private HBox tableBox4;
        private VBox tableBox5;
        private HBox tableBox6;
        private VBox tableBox7;
        private HBox tableBox8;
        private HBox tableBox9;
        private HBox tableBox10;
        private VBox tableBox11;
        private HBox tableBox12;
        private VBox tableBox13;
        private Pane pane1;
        private Pane pane2;

        public TerraceController()
        {
            this.setupTables();
        }

        public void setupTables()
        {
            door = new Button("DOOR");
            table1 = new Button("T1");
            table2 = new Button("T2");
            table3 = new Button("T3");
            table4 = new Button("T4");
            table5 = new Button("T5");
            table6 = new Button("T6");
            table7 = new Button("T7");
            table8 = new Button("T8");
            table9 = new Button("T9");
            table10 = new Button("T10");
            table11 = new Button("T11");
            table12 = new Button("T12");
            table13 = new Button("T13");
            table14 = new Button("T14");
            table15 = new Button("T15");
            table16 = new Button("T16");
            table17 = new Button("T17");
            table18 = new Button("T18");

            {
                table1.getStyleClass().add("table-button");
                table2.getStyleClass().add("table-button");
                table3.getStyleClass().add("table-button");
                table4.getStyleClass().add("table-button");
                table5.getStyleClass().add("table-button");
                table6.getStyleClass().add("table-button");
                table7.getStyleClass().add("table-button");
                table8.getStyleClass().add("table-button");
                table9.getStyleClass().add("table-button");
                table10.getStyleClass().add("table-button");
                table11.getStyleClass().add("table-button");
                table12.getStyleClass().add("table-button");
                table13.getStyleClass().add("table-button");
                table14.getStyleClass().add("table-button");
                table15.getStyleClass().add("table-button");
                table16.getStyleClass().add("table-button");
                table17.getStyleClass().add("table-button");
                table18.getStyleClass().add("table-button");
                door.getStyleClass().add("table-button");
            }

            table1.setPrefSize(100, 100);
            table2.setPrefSize(100, 100);
            table3.setPrefSize(100, 100);
            table4.setPrefSize(100, 100);
            table5.setPrefSize(100, 100);
            table6.setPrefSize(100, 100);

            Region region = new Region();
            Region region1 = new Region();
            region.setPrefHeight(0);
            region1.setPrefHeight(0);

            tableBox1 = new HBox(40, region, table1, table2, table3);
            tableBox3 = new HBox(40, region1, table4, table5, table6);

            pane1 = new Pane();
            pane2 = new Pane();
            pane1.setPrefSize(50, 130);
            pane2.setPrefSize(50, 130);
            pane1.setMaxHeight(130);
            pane2.setMaxHeight(130);

            {
                pane1.setStyle("-fx-background-color: rgb(14,14,14);");
                pane2.setStyle("-fx-background-color: #0e0e0e;");
            }

            door.setPrefSize(120, 70);
            table7.setPrefSize(200, 50);

            tableBox4 = new HBox(pane1, door, pane2);
            tableBox5 = new VBox(70, tableBox4, table7);


            Region region2 = new Region();
            region2.setPrefHeight(0);
            tableBox2 = new VBox(33, region2, tableBox1, tableBox3);

            table8.setPrefSize(150, 50);
            table9.setPrefSize(150, 50);
            table10.setPrefSize(50, 50);
            table11.setPrefSize(50, 50);
            table12.setPrefSize(50, 50);
            table16.setPrefSize(50, 50);
            table17.setPrefSize(50, 50);
            table18.setPrefSize(50, 50);

            Region region3 = new Region();
            region3.setPrefHeight(0);

            tableBox7 = new VBox(30, table8, table9);
            tableBox8 = new HBox(40, table10, table11, table12);
            tableBox12 = new HBox(40, table16, table17, table18);
            tableBox13 = new VBox(30, tableBox8, tableBox12);
            tableBox9 = new HBox(50, tableBox7, tableBox13);

            table13.setPrefSize(133, 50);
            table14.setPrefSize(133, 50);
            table15.setPrefSize(133, 50);

            tableBox10 = new HBox(25, table13, table14, table15);
            tableBox11 = new VBox(38, region3, tableBox9, tableBox10);

            tableBox6 = new HBox(50, tableBox2, tableBox5, tableBox11);
        }

        public HBox getTableBox6() {
            return tableBox6;
        }

        public Button getTable1() {
            return table1;
        }

        public Button getTable2() {
            return table2;
        }

        public Button getTable3() {
            return table3;
        }

        public Button getTable4() {
            return table4;
        }

        public Button getTable5() {
            return table5;
        }

        public Button getTable6() {
            return table6;
        }

        public Button getTable7() {
            return table7;
        }

        public Button getTable8() {
            return table8;
        }

        public Button getTable9() {
            return table9;
        }

        public Button getTable10() {
            return table10;
        }

        public Button getTable11() {
            return table11;
        }

        public Button getTable12() {
            return table12;
        }

        public Button getTable13() {
            return table13;
        }

        public Button getTable14() {
            return table14;
        }

        public Button getTable15() {
            return table15;
        }

        public Button getTable16() {
            return table16;
        }

        public Button getTable17() {
            return table17;
        }

        public Button getTable18() {
            return table18;
        }
    }

    public class DeliveryController {
        private Label titleLabel;
        private Pane titleWrapper;
        private Label deliveryInfoLabel;
        private Pane deliveryInfoWrapper;
        private Label orderInfoLabel;
        private Pane orderInfoWrapper;
        private TableView<Order> deliveryOrdersView;
        private VBox deliveryInfoBox;
        private VBox orderDetailsBox;
        private VBox mainBox;
        private HBox tablesBox;
        private Button okButton;
        private Button openDeliveryInfoButton;
        private Button openOrderInfoButton;
        private Button deleteOrderButton;
        private Button restoreOrderButton;
        private Button setPaidButton;
        private Button setNotPaidButton;
        private Scene scene;
        private Stage stage;
        private ScrollPane scrollPaneForDetails;
        private boolean openButtonPressed;
        private TableColumn<Order, Integer> idColumn = new TableColumn<>("Id");

        private TableColumn<Order, String> nameColumn = new TableColumn<>("Order");

        private TableColumn<Order, Float> priceColumn = new TableColumn<>("Price");

        public DeliveryController()
        {
            this.openButtonPressed = false;

            this.setupDeliveryStage();

            this.setupOkButton();
            this.setupDeleteOrderButton();
            this.setupRestoreOrderButton();
            this.setupOpenDeliveryInfoButton();
            this.setupOpenOrderInfoButton();
            this.setupSetPaidButton();
            this.setupSetNotPaidButton();
        }
        public void setupDeliveryStage()
        {
            titleLabel = new Label("DELIVERY");
            titleWrapper = new Pane(titleLabel);

            titleLabel.layoutXProperty().bind(titleWrapper.widthProperty().subtract(titleLabel.widthProperty()).divide(2));

            {
                titleLabel.getStyleClass().add("welcome-label");
                titleWrapper.setStyle("-fx-background-color: #df00fe;");
            }

            deliveryInfoLabel = new Label("DELIVERY INFO");
            deliveryInfoWrapper = new Pane(deliveryInfoLabel);

            deliveryInfoWrapper.setMinWidth(300);
            deliveryInfoLabel.layoutXProperty().bind(deliveryInfoWrapper.widthProperty().subtract(deliveryInfoLabel.widthProperty()).divide(2));

            {
                deliveryInfoWrapper.getStyleClass().add("drinks-label-color");
                deliveryInfoLabel.setStyle("-fx-background-color: #df00fe;");
            }

            orderInfoLabel = new Label("ORDER INFO");
            orderInfoWrapper = new Pane(orderInfoLabel);

            orderInfoLabel.layoutXProperty().bind(orderInfoWrapper.widthProperty().subtract(orderInfoLabel.widthProperty()).divide(2));

            {
                orderInfoLabel.getStyleClass().add("drinks-label-color");
                orderInfoWrapper.setStyle("-fx-background-color: #df00fe;");
            }

            deliveryOrdersView = new TableView();

            idColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
            nameColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getName()));
            priceColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPrice()));


            idColumn.setPrefWidth(100);
            idColumn.setStyle("-fx-alignment: CENTER;");
            deliveryOrdersView.getColumns().add(idColumn);

            nameColumn.setPrefWidth(150);
            nameColumn.setStyle("-fx-alignment: CENTER;");
            deliveryOrdersView.getColumns().add(nameColumn);

            priceColumn.setPrefWidth(100);
            priceColumn.setStyle("-fx-alignment: CENTER;");
            deliveryOrdersView.getColumns().add(priceColumn);

            this.setTableCells();

            ObservableList observableList = FXCollections.observableArrayList(deliveryOrders);
            deliveryOrdersView.setItems(observableList);

            deliveryOrdersView.setPrefSize(350, 300);

            deliveryInfoBox = new VBox(5);

            deliveryInfoBox.setPrefSize(300, 300);

            Label promptLabel = new Label("Delivery Info");

            {
                promptLabel.getStyleClass().add("timer-label");
                deliveryInfoBox.setStyle("-fx-background-color: #234829;");
            }

            deliveryInfoBox.getChildren().add(promptLabel);
            deliveryInfoBox.setAlignment(Pos.CENTER);

            orderDetailsBox = new VBox(5);
            orderDetailsBox.setFillWidth(true);

            orderDetailsBox.setMinHeight(300);

            Label promptLabel2 = new Label("Order Info");

            {
                promptLabel2.getStyleClass().add("timer-label");
                orderDetailsBox.setStyle("-fx-background-color: #234829;");
            }

            orderDetailsBox.getChildren().add(promptLabel2);
            orderDetailsBox.setAlignment(Pos.CENTER);

            okButton = new Button("OK");
            openDeliveryInfoButton = new Button();
            openOrderInfoButton = new Button();
            deleteOrderButton = new Button();
            restoreOrderButton = new Button();
            setPaidButton = new Button();
            setNotPaidButton = new Button();

            okButton.setPrefSize(300, 50);
            openDeliveryInfoButton.setPrefSize(105, 50);
            openOrderInfoButton.setPrefSize(105, 50);
            deleteOrderButton.setPrefSize(105, 50);
            restoreOrderButton.setPrefSize(105, 50);
            setPaidButton.setPrefSize(105, 50);
            setNotPaidButton.setPrefSize(105, 50);

            {
                okButton.getStyleClass().add("interface-button");
                openDeliveryInfoButton.getStyleClass().add("live-order-button");
                openOrderInfoButton.getStyleClass().add("live-order-button");
                deleteOrderButton.getStyleClass().add("live-order-button");
                restoreOrderButton.getStyleClass().add("live-order-button");
                setPaidButton.getStyleClass().add("live-order-button");
                setNotPaidButton.getStyleClass().add("live-order-button");
            }

            Image deleteImage = new Image("remove.png");
            ImageView imageView = new ImageView(deleteImage);
            imageView.setFitWidth(40);
            imageView.setFitHeight(40);

            Image openImage = new Image("openImage.png");
            ImageView imageView6  = new ImageView(openImage);
            imageView6.setFitHeight(40);
            imageView6.setFitWidth(40);

            Image openImage2 = new Image("openImage.png");
            ImageView imageView7  = new ImageView(openImage2);
            imageView7.setFitHeight(40);
            imageView7.setFitWidth(40);

            Image restoreImage = new Image("restoreImage.png");
            ImageView imageView8  = new ImageView(restoreImage);
            imageView8.setFitHeight(40);
            imageView8.setFitWidth(40);

            Image paidImage = new Image("360_F_49064979_nbrrNFtMOFCT5YJrW5LCs8Qgtr0XM6Tz.jpg");
            ImageView imageView4  = new ImageView(paidImage);
            imageView4.setFitHeight(40);
            imageView4.setFitWidth(40);

            Image notPaidImage = new Image("notpaidimage.png");
            ImageView imageView5  = new ImageView(notPaidImage);
            imageView5.setFitHeight(40);
            imageView5.setFitWidth(40);

            deleteOrderButton.setContentDisplay(ContentDisplay.CENTER);
            deleteOrderButton.setGraphic(imageView);

            openDeliveryInfoButton.setContentDisplay(ContentDisplay.CENTER);
            openDeliveryInfoButton.setGraphic(imageView6);

            openOrderInfoButton.setContentDisplay(ContentDisplay.CENTER);
            openOrderInfoButton.setGraphic(imageView7);

            restoreOrderButton.setContentDisplay(ContentDisplay.CENTER);
            restoreOrderButton.setGraphic(imageView8);

            setPaidButton.setContentDisplay(ContentDisplay.CENTER);
            setPaidButton.setGraphic(imageView4);

            setNotPaidButton.setContentDisplay(ContentDisplay.CENTER);
            setNotPaidButton.setGraphic(imageView5);

            Tooltip tooltip = new Tooltip("Delete");
            tooltip.setShowDelay(Duration.seconds(0.1));

            deleteOrderButton.setTooltip(tooltip);

            Tooltip tooltip2 = new Tooltip("Restore");
            tooltip.setShowDelay(Duration.seconds(0.1));

            restoreOrderButton.setTooltip(tooltip2);

            Tooltip tooltip3 = new Tooltip("Open Delivery Info");
            tooltip.setShowDelay(Duration.seconds(0.1));

            openDeliveryInfoButton.setTooltip(tooltip3);

            Tooltip tooltip4 = new Tooltip("Open Order Info");
            tooltip.setShowDelay(Duration.seconds(0.1));

            openOrderInfoButton.setTooltip(tooltip4);

            Tooltip tooltip5 = new Tooltip("Set Paid");
            tooltip.setShowDelay(Duration.seconds(0.1));

            setPaidButton.setTooltip(tooltip5);

            Tooltip tooltip6 = new Tooltip("Set Not Paid");
            tooltip.setShowDelay(Duration.seconds(0.1));

            setNotPaidButton.setTooltip(tooltip6);

            HBox buttonsBox1 = new HBox(5, openDeliveryInfoButton, openOrderInfoButton);
            HBox buttonsBox2 = new HBox(5, deleteOrderButton, restoreOrderButton);
            HBox buttonsBox3 = new HBox(5, setPaidButton, setNotPaidButton);
            HBox buttonsBox4 = new HBox(5, buttonsBox1, buttonsBox2, buttonsBox3, okButton);

            scrollPaneForDetails = new ScrollPane();
            scrollPaneForDetails.setContent(orderDetailsBox);
            scrollPaneForDetails.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPaneForDetails.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPaneForDetails.setFitToWidth(true);
            scrollPaneForDetails.setPrefSize(300, 300);

            tablesBox = new HBox(5, deliveryOrdersView, deliveryInfoBox, scrollPaneForDetails);

            mainBox = new VBox(5, titleWrapper, tablesBox, buttonsBox4);

            scene = new Scene(mainBox, 960, 410);
            scene.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

            stage = new Stage();
            stage.setScene(scene);
        }

        public void setTableCells()
        {
            idColumn.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(String.valueOf(item));
                        getStyleClass().add("orders-label");
                        setStyle("-fx-background-color: rgba(231,11,11,0.82); -fx-alignment: center;");
                    }
                }
            });

            nameColumn.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        getStyleClass().add("orders-label");
                        setStyle("-fx-background-color: rgba(231,11,11,0.82); -fx-alignment: center;");
                    }
                }
            });

            priceColumn.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(Float item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {

                        Order order = null;
                        if (getTableRow() != null) {
                            order = getTableRow().getItem();
                        }

                        getStyleClass().add("orders-label");
                        setStyle("-fx-background-color: rgba(231,11,11,0.82); -fx-alignment: center;");

                        if (order != null && order.isPaid())
                            setText("PAID");
                        else
                            setText(item.intValue() + " LEI");
                    }
                }
            });


        }

        public void setupOrderDetailsBox(Order selectedOrder)
        {
            orderDetailsBox.getChildren().clear();
            orderDetailsBox.setAlignment(Pos.TOP_LEFT);
            orderDetailsBox.getChildren().add(orderInfoWrapper);

            for (int i = 0; i < deliveryOrders.size(); ++i)
            {
                if (selectedOrder.getName().equals(deliveryOrders.get(i).getName()))
                {
                    for (int j = 0; j < deliveryOrders.get(i).getOrderedDrink().size(); ++j)
                    {
                        Label drinkLabel = new Label("Drink");

                        Label nameLabel = new Label("Name: " + deliveryOrders.get(i).getOrderedDrink().get(j).getName());
                        Label volumeLabel  = new Label("Volume: " + deliveryOrders.get(i).getOrderedDrink().get(j).getVolume() + " Ml");
                        Label priceLabel = new Label("Price: " + deliveryOrders.get(i).getOrderedDrink().get(j).getPrice() + " Lei");

                        {
                            drinkLabel.getStyleClass().add("description-label-v4");
                            nameLabel.getStyleClass().add("description-label-v2");
                            volumeLabel.getStyleClass().add("description-label-v2");
                            priceLabel.getStyleClass().add("description-label-v2");
                        }

                        VBox drinkBox = new VBox( nameLabel, volumeLabel, priceLabel);
                        VBox finalDrinkBox = new VBox(drinkLabel, drinkBox);

                        Pane wrapper = new Pane(finalDrinkBox);
                        wrapper.setStyle("-fx-background-color: #df00fe;");

                        orderDetailsBox.getChildren().add(wrapper);
                    }

                    for (int j = 0; j < deliveryOrders.get(i).getOrderedFood().size(); ++j)
                    {
                        Label drinkLabel = new Label("Food");

                        Label nameLabel = new Label("Name: " + deliveryOrders.get(i).getOrderedFood().get(j).getName());
                        Label gramsLabel  = new Label("Grams: " + deliveryOrders.get(i).getOrderedFood().get(j).getGrams() + " Grams");
                        Label priceLabel = new Label("Price: " + deliveryOrders.get(i).getOrderedFood().get(j).getPrice() + " Lei");
                        Label ratingLabel = new Label("Rating: " + deliveryOrders.get(i).getOrderedFood().get(j).getRating() + "☆");

                        {
                            drinkLabel.getStyleClass().add("description-label-v4");
                            nameLabel.getStyleClass().add("description-label-v2");
                            gramsLabel.getStyleClass().add("description-label-v2");
                            priceLabel.getStyleClass().add("description-label-v2");
                            ratingLabel.getStyleClass().add("description-label-v2");
                        }

                        VBox foodBox = new VBox(nameLabel, gramsLabel, priceLabel, ratingLabel);
                        VBox finalFoodBox = new VBox(drinkLabel, foodBox);

                        Pane wrapper = new Pane(finalFoodBox);
                        wrapper.setStyle("-fx-background-color: #df00fe;");

                        orderDetailsBox.getChildren().add(wrapper);
                    }
                }
            }
        }

        public void setupOkButton()
        {
            this.okButton.setOnAction(e -> {
                stage.close();
            });
        }

        public void setupDeleteOrderButton()
        {
            this.deleteOrderButton.setOnAction(e -> {
                Order selectedOrder = deliveryOrdersView.getSelectionModel().getSelectedItem();

                if (selectedOrder != null && selectedOrder.isPaid())
                {
                    restoreDeliveryOrders.add(selectedOrder);

                    deliveryOrdersView.getItems().remove(selectedOrder);
                    deliveryOrders.remove(selectedOrder);
                    deliveryOrderDataBase.deleteOrderFromDataBase(selectedOrder);
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("NOT PAID");
                    alert.setHeaderText(null);
                    alert.setContentText("The order must be paid!");

                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

                    Label contentLabel = (Label) dialogPane.lookup(".content");
                    if (contentLabel != null) {
                        contentLabel.getStyleClass().add("alert-content-text");
                    }

                    alert.showAndWait();
                }
            });
        }

        public void setupRestoreOrderButton()
        {
            this.restoreOrderButton.setOnAction(e -> {
                if (!restoreDeliveryOrders.isEmpty())
                {
                    deliveryOrdersView.getItems().add(restoreDeliveryOrders.get(0));
                    deliveryOrders.add(restoreDeliveryOrders.get(0));
                    deliveryOrderDataBase.addOrderToDataBase(restoreDeliveryOrders.get(0));

                    restoreDeliveryOrders.clear();
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("EMPTY");
                    alert.setHeaderText(null);
                    alert.setContentText("No orders to be restored!");

                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

                    Label contentLabel = (Label) dialogPane.lookup(".content");
                    if (contentLabel != null) {
                        contentLabel.getStyleClass().add("alert-content-text");
                    }

                    alert.showAndWait();
                }
            });
        }

        public void setupOpenDeliveryInfoButton()
        {
            this.openDeliveryInfoButton.setOnAction(e -> {
                Order selectedOrder = deliveryOrdersView.getSelectionModel().getSelectedItem();

                if (selectedOrder != null)
                {
                    deliveryInfoBox.getChildren().clear();
                    deliveryInfoBox.getChildren().add(deliveryInfoWrapper);
                    deliveryInfoBox.setAlignment(Pos.TOP_LEFT);

                    LocalDateTime localDateTime = selectedOrder.getLocalDateTime();
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    String currentDate = localDateTime.format(dateFormatter);
                    String currentTime = localDateTime.format(timeFormatter);

                    Label dateLabel = new Label("Date: " + currentDate);
                    Label timeLabel = new Label("Time: " + currentTime);

                    Label nameLabel = new Label("Name: " + selectedOrder.getDeliveryInfo().getName());
                    Label phoneLabel  = new Label("Phone: " + selectedOrder.getDeliveryInfo().getPhone());
                    Label addressLabel = new Label("Address: " + selectedOrder.getDeliveryInfo().getAddress());
                    Label apartmentLabel = new Label("Apartment: " + selectedOrder.getDeliveryInfo().getApartment());
                    Label floorLabel = new Label("Floor: " + selectedOrder.getDeliveryInfo().getFloor());

                    {
                        dateLabel.getStyleClass().add("drinks-label-color-v3");
                        timeLabel.getStyleClass().add("drinks-label-color-v3");
                        nameLabel.getStyleClass().add("drinks-label-color-v3");
                        phoneLabel.getStyleClass().add("drinks-label-color-v3");
                        addressLabel.getStyleClass().add("drinks-label-color-v3");
                        apartmentLabel.getStyleClass().add("drinks-label-color-v3");
                        floorLabel.getStyleClass().add("drinks-label-color-v3");
                    }

                    deliveryInfoBox.getChildren().addAll(dateLabel, timeLabel, nameLabel, phoneLabel, addressLabel, apartmentLabel,floorLabel);
                }

            });
        }

        public void setupOpenOrderInfoButton()
        {
            this.openOrderInfoButton.setOnAction(e -> {
                Order selectedOrder = deliveryOrdersView.getSelectionModel().getSelectedItem();

                if (selectedOrder != null)
                {
                    this.setupOrderDetailsBox(selectedOrder);
                }
            });
        }

        public void setupSetPaidButton()
        {
            this.setPaidButton.setOnAction(e -> {
                Order selectedOrder = deliveryOrdersView.getSelectionModel().getSelectedItem();

                if (selectedOrder != null && !selectedOrder.isPaid())
                {
                    Payment payment = new Payment(selectedOrder.getName(), selectedOrder.getPrice(), selectedOrder.getPayMethod());

                    payments.add(payment);
                    paymentDataBase.addPaymentToDataBase(payment);

                    ViewerController.PaymentViewer paymentViewer = viewerController.getPaymentViewer();
                    paymentViewer.getPaymentsList().getItems().add(payment);

                    selectedOrder.setPaid(true);
                    deliveryOrderDataBase.updateOrderFromDataBase(selectedOrder.getName(), selectedOrder);

                    totalAmountViewer.getTotalAmountLabel().setText("TOTAL AMOUNT: " + totalAmountViewer.getTotalAmount() + " LEI");
                    totalAmountViewer.getCashLabel().setText("CASH: " + totalAmountViewer.getTotalCash() + " LEI");
                    totalAmountViewer.getCardLabel().setText("CARD: " + totalAmountViewer.getTotalCard() + " LEI");

                    this.setTableCells();
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("PAID");
                    alert.setHeaderText(null);
                    alert.setContentText("The order is already paid!");

                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

                    Label contentLabel = (Label) dialogPane.lookup(".content");
                    if (contentLabel != null) {
                        contentLabel.getStyleClass().add("alert-content-text");
                    }

                    alert.showAndWait();
                }
            });
        }

        public void setupSetNotPaidButton()
        {
            this.setNotPaidButton.setOnAction(e -> {
                Order selectedOrder = deliveryOrdersView.getSelectionModel().getSelectedItem();

                if (selectedOrder != null && selectedOrder.isPaid())
                {
                    Payment payment = new Payment();

                    for (int i = 0; i < payments.size(); ++i)
                    {
                        if (payments.get(i).getOrderName().equals(selectedOrder.getName()))
                        {
                            payment = payments.get(i);
                        }
                    }

                    payments.remove(payment);
                    paymentDataBase.deletePaymentFromDataBase(payment.getOrderName());

                    ViewerController.PaymentViewer paymentViewer = viewerController.getPaymentViewer();
                    paymentViewer.getPaymentsList().getItems().remove(payment);

                    selectedOrder.setPaid(false);
                    deliveryOrderDataBase.updateOrderFromDataBase(selectedOrder.getName(), selectedOrder);

                    totalAmountViewer.getTotalAmountLabel().setText("TOTAL AMOUNT: " + totalAmountViewer.getTotalAmount() + " LEI");
                    totalAmountViewer.getCashLabel().setText("CASH: " + totalAmountViewer.getTotalCash() + " LEI");
                    totalAmountViewer.getCardLabel().setText("CARD: " + totalAmountViewer.getTotalCard() + " LEI");

                    this.setTableCells();
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("NOT PAID");
                    alert.setHeaderText(null);
                    alert.setContentText("The order is not paid!");

                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

                    Label contentLabel = (Label) dialogPane.lookup(".content");
                    if (contentLabel != null) {
                        contentLabel.getStyleClass().add("alert-content-text");
                    }

                    alert.showAndWait();
                }
            });
        }

        public Stage getStage() {
            return stage;
        }

        public TableView<Order> getDeliveryOrdersView() {
            return deliveryOrdersView;
        }
    }

    public void setupInsideButton()
    {
        this.insideButton.setOnAction(e -> {
            this.viewerBox.getChildren().clear();

            Region region = new Region();
            region.setPrefWidth(0);

            VBox tableBox2 = insideController.getTablesBox2();
            VBox tableBox5 = insideController.getTableBox5();

            this.viewerBox.getChildren().addAll(region, tableBox2, tableBox5);
        });
    }

    public void setupTerraceButton()
    {
        this.terraceButton.setOnAction(e -> {
            this.viewerBox.getChildren().clear();

            HBox tableBox6 = terraceController.getTableBox6();

            this.viewerBox.getChildren().addAll(tableBox6);
        });
    }

    public void setupDeliveryButton()
    {
        this.deliveryButton.setOnAction(e -> {
            this.deliveryController.getStage().show();
        });
    }

    public VBox getTitleAndButtonsBox() {
        return titleAndButtonsBox;
    }

    public HBox getViewerBox() {
        return viewerBox;
    }

    public ToolBar getToolBar() {
        return toolBar;
    }

    public DeliveryController getDeliveryController() {
        return deliveryController;
    }

    public InsideController getInsideController() {
        return insideController;
    }

    public TotalAmountViewer getTotalAmountViewer() {
        return totalAmountViewer;
    }
}
