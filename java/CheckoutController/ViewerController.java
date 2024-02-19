package CheckoutController;

import Model.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.List;
import java.util.Locale;

public class ViewerController {
    private Button allOrdersButton;
    private Button allUsersButton;
    private Button allDrinksButton;
    private Button allFoodButton;
    private Button allPaymentsButton;
    private Label titleLabel;
    private Pane titleWrapper;
    private HBox buttonsBox;
    private VBox titleAndButtonsBox;
    private List<Order> orderHistory;
    private List<User> users;
    private boolean isOpened;
    private UserViewer userViewer;
    private DrinkViewer drinkViewer;
    private FoodViewer foodViewer;
    private PaymentViewer paymentViewer;
    private OrdersViewer ordersViewer;
    private List<Drink> coffees;
    private List<Drink> juices;
    private List<Drink> softDrinks;
    private List<Drink> teas;
    private List<Drink> beers;
    private List<Drink> wines;
    private List<Drink> cocktails;
    private List<Drink> strongDrinks;
    private List<Food> salads;
    private List<Food> soups;
    private List<Food> smallBites;
    private List<Food> meat;
    private List<Food> pasta;
    private List<Food> pizza;
    private List<Food> seaFood;
    private List<Food> cakes;
    private List<Food> iceScream;
    private List<Food> otherDessert;

    private List<Payment> payments;

    public ViewerController(List<Order> orderHistory, List<User> users, List<Drink> coffees, List<Drink> juices, List<Drink> softDrinks, List<Drink> teas, List<Drink> beers, List<Drink> wines, List<Drink> cocktails, List<Drink> strongDrinks,
                            List<Food> salads, List<Food> soups, List<Food> smallBites, List<Food> meat, List<Food> pasta, List<Food> pizza, List<Food> seaFood, List<Food> cakes, List<Food> iceScream, List<Food> otherDessert, List<Payment> payments)
    {
        this.orderHistory = orderHistory;
        this.users = users;
        this.payments = payments;

        this.userViewer = new UserViewer(users);
        this.drinkViewer = new DrinkViewer(coffees, juices, softDrinks, teas, beers, wines, cocktails, strongDrinks);
        this.foodViewer = new FoodViewer(salads, soups, smallBites, meat, pasta, pizza, seaFood, cakes, iceScream, otherDessert);
        this.paymentViewer = new PaymentViewer();
        this.ordersViewer = new OrdersViewer();

        this.setupTitleAndButtons();
    }

    public void setupTitleAndButtons()
    {
        titleLabel = new Label("VIEW");
        titleWrapper = new Pane(titleLabel);
        titleWrapper.setPrefSize(535, 50);

        titleLabel.layoutXProperty().bind(titleWrapper.widthProperty().subtract(titleLabel.widthProperty()).divide(2));

        {
            titleWrapper.setStyle("-fx-background-color: #df00fe;");
            titleLabel.getStyleClass().add("welcome-label");
        }

        allOrdersButton = new Button("VIEW ORDERS");
        allUsersButton = new Button("VIEW USERS");
        allDrinksButton = new Button("VIEW DRINKS");
        allFoodButton = new Button("VIEW FOOD");
        allPaymentsButton = new Button("VIEW PAYMENTS");

        allUsersButton.setPrefSize(127, 50);
        allOrdersButton.setPrefSize(127, 50);
        allFoodButton.setPrefSize(127, 50);
        allPaymentsButton.setPrefSize(127, 50);
        allDrinksButton.setPrefSize(127, 50);

        {
            allDrinksButton.getStyleClass().add("interface-button");
            allPaymentsButton.getStyleClass().add("interface-button");
            allFoodButton.getStyleClass().add("interface-button");
            allOrdersButton.getStyleClass().add("interface-button");
            allUsersButton.getStyleClass().add("interface-button");
        }

        this.buttonsBox = new HBox(5, allOrdersButton, allUsersButton, allDrinksButton, allFoodButton, allPaymentsButton);
        this.titleAndButtonsBox = new VBox(5, titleWrapper, buttonsBox);
    }

    class OrdersViewer {
        private VBox mainOrdersBox;
        private VBox detailedOrdersBox;
        private HBox buttonsBox;
        private HBox tableViewAndDetailedOrdersBox;
        private Button openOrderButton;
        private Button okOrderButton;
        private Button refreshButton;
        private ScrollPane scrollPaneForDetails;
        private Label titleLabel = new Label("ORDERS HISTORY");
        private Pane titleWrapper = new Pane(titleLabel);

        private TableView allOrdersView;

        private Scene scene;
        private Stage stage;

        public OrdersViewer() {
            allOrdersView= new TableView();

            this.setupOrderViewer();
        }

        public void setupOrderViewer()
        {
            titleWrapper.setPrefSize(200, 50);
            titleLabel.layoutXProperty().bind(titleWrapper.widthProperty().subtract(titleLabel.widthProperty()).divide(2));

            {
                titleLabel.getStyleClass().add("welcome-label");
                titleWrapper.setStyle("-fx-background-color: #df00fe;");
            }

            allOrdersView = new TableView();
            detailedOrdersBox = new VBox();
            mainOrdersBox = new VBox(5);
            tableViewAndDetailedOrdersBox = new HBox(5);
            okOrderButton = new Button("OK");
            openOrderButton = new Button("OPEN");
            refreshButton = new Button();

            scrollPaneForDetails = new ScrollPane();
            scrollPaneForDetails.setContent(detailedOrdersBox);
            scrollPaneForDetails.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPaneForDetails.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPaneForDetails.setFitToWidth(true);
            scrollPaneForDetails.setPrefSize(300, 295);

            TableColumn<Order, String> nameColumn = new TableColumn<>("Orders");
            nameColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getName()));

            nameColumn.setPrefWidth(295);
            nameColumn.setStyle("-fx-alignment: CENTER;");
            allOrdersView.getColumns().add(nameColumn);

            ObservableList<Order> data  = FXCollections.observableArrayList(orderHistory);
            allOrdersView.setItems(data);

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
                        setStyle("-fx-background-color: rgba(119,103,103,0.36); -fx-alignment: center;");
                    }
                }
            });

            {
                openOrderButton.getStyleClass().add("interface-button");
                okOrderButton.getStyleClass().add("interface-button");
                refreshButton.getStyleClass().add("clear-order-button");
                detailedOrdersBox.setStyle("-fx-background-color: #234829;");
            }

            allOrdersView.setPrefSize(300, 295);
            detailedOrdersBox.setPrefSize(300, 295);
            okOrderButton.setPrefSize(300, 50);
            openOrderButton.setPrefSize(245, 50);
            refreshButton.setPrefSize(50, 50);

            buttonsBox = new HBox(5, refreshButton,openOrderButton, okOrderButton);

            tableViewAndDetailedOrdersBox.getChildren().addAll(allOrdersView, scrollPaneForDetails);
            mainOrdersBox.getChildren().addAll(titleWrapper, tableViewAndDetailedOrdersBox, buttonsBox);

            mainOrdersBox.setAlignment(Pos.CENTER);

            stage = new Stage();
            scene = new Scene(mainOrdersBox, 605, 400);
            scene.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

            okOrderButton.setOnAction(ev -> {
                stage.close();
            });

            Label promptLabel = new Label("Order Details");
            promptLabel.getStyleClass().add("timer-label");

            detailedOrdersBox.getChildren().addAll(promptLabel);
            detailedOrdersBox.setAlignment(Pos.CENTER);

            openOrderButton.setOnAction(ev -> {

                Order selectedOrder = (Order) allOrdersView.getSelectionModel().getSelectedItem();

                if (selectedOrder != null)
                {
                    detailedOrdersBox.getChildren().clear();
                    detailedOrdersBox.setAlignment(Pos.TOP_LEFT);

                    Label titleOrderLabel = new Label("DETAILS:");

                    {
                        titleOrderLabel.getStyleClass().add("welcome-label");
                    }

                    detailedOrdersBox.getChildren().add(titleOrderLabel);

                    Label idLabel = new Label("Order Id: " + selectedOrder.getId());

                    LocalDateTime localDateTime = selectedOrder.getLocalDateTime();
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    String currentDate = localDateTime.format(dateFormatter);
                    String currentTime = localDateTime.format(timeFormatter);

                    Label dateLabel = new Label("Date: " + currentDate);
                    Label timeLabel = new Label("Time: " + currentTime);

                    Label priceLabel = new Label("Price: " + selectedOrder.getPrice() + " LEI");
                    Label tableNumberLabel = new Label("Table number: " + selectedOrder.getTableNumber());
                    Label placeNameLabel = new Label("Place: " + selectedOrder.getPlaceName());
                    Label payMethodLabel = new Label("Payment method: " + selectedOrder.getPayMethod());

                    {
                        idLabel.getStyleClass().add("drinks-label-color");
                        dateLabel.getStyleClass().add("drinks-label-color");
                        timeLabel.getStyleClass().add("drinks-label-color");
                        priceLabel.getStyleClass().add("drinks-label-color");
                        tableNumberLabel.getStyleClass().add("drinks-label-color");
                        placeNameLabel.getStyleClass().add("drinks-label-color");
                        payMethodLabel.getStyleClass().add("drinks-label-color");
                    }

                    detailedOrdersBox.getChildren().addAll(idLabel, dateLabel, timeLabel, priceLabel, tableNumberLabel, placeNameLabel, payMethodLabel);

                    for (int i = 0; i < orderHistory.size(); ++i)
                    {
                        if (selectedOrder.getName().equals(orderHistory.get(i).getName()))
                        {
                            for (int j = 0; j < orderHistory.get(i).getOrderedDrink().size(); ++j)
                            {
                                Label drinkLabel = new Label("Drink: ");

                                Label nameDLabel = new Label("Name: " + orderHistory.get(i).getOrderedDrink().get(j).getName());
                                Label volumeDLabel = new Label("Volume: " + orderHistory.get(i).getOrderedDrink().get(j).getVolume() + " Ml");
                                Label priceDLabel = new Label("Price: " + orderHistory.get(i).getOrderedDrink().get(j).getPrice() + " Lei");

                                {
                                    drinkLabel.getStyleClass().add("description-label-v5");
                                    nameDLabel.getStyleClass().add("description-label-v4");
                                    volumeDLabel.getStyleClass().add("description-label-v4");
                                    priceDLabel.getStyleClass().add("description-label-v4");
                                }

                                VBox drinkBox = new VBox(drinkLabel, nameDLabel, volumeDLabel, priceDLabel);

                                detailedOrdersBox.getChildren().add(drinkBox);
                            }

                            for (int j = 0; j < orderHistory.get(i).getOrderedFood().size(); ++j)
                            {
                                Label foodLabel = new Label("Food: ");

                                Label nameFLabel = new Label("Name: " + orderHistory.get(i).getOrderedFood().get(j).getName());
                                Label gramsFLabel = new Label("Volume: " + orderHistory.get(i).getOrderedFood().get(j).getGrams() + " Grams");
                                Label priceFLabel = new Label("Price: " + orderHistory.get(i).getOrderedFood().get(j).getPrice() + " Lei");
                                Label ratingFLabel = new Label("Rating: " + orderHistory.get(i).getOrderedFood().get(j).getRating() + "☆");

                                {
                                    foodLabel.getStyleClass().add("description-label-v5");
                                    nameFLabel.getStyleClass().add("description-label-v4");
                                    gramsFLabel.getStyleClass().add("description-label-v4");
                                    priceFLabel.getStyleClass().add("description-label-v4");
                                    ratingFLabel.getStyleClass().add("description-label-v4");
                                }

                                VBox foodBox = new VBox(foodLabel, nameFLabel, gramsFLabel, priceFLabel, ratingFLabel);

                                detailedOrdersBox.getChildren().add(foodBox);
                            }
                        }
                    }
                }

            });

            Image refreshImage = new Image("refreshImage.png");
            ImageView imageView1 = new ImageView(refreshImage);
            imageView1.setFitHeight(40);
            imageView1.setFitWidth(40);

            refreshButton.setGraphic(imageView1);

            refreshButton.setOnAction(ev -> {
                detailedOrdersBox.getChildren().clear();
                detailedOrdersBox.getChildren().addAll(promptLabel);
                detailedOrdersBox.setAlignment(Pos.CENTER);

                allOrdersView.setStyle("-fx-background-color: blue;");
                new Timeline(new KeyFrame(
                        Duration.millis(500),
                        ae -> allOrdersView.setStyle("")
                )).play();

                allOrdersView.refresh();
            });

            stage.setScene(scene);
        }

        public Stage getStage() {
            return stage;
        }

        public TableView getAllOrdersView() {
            return allOrdersView;
        }
    }



    public VBox getTitleAndButtonsBox() {
        return titleAndButtonsBox;
    }

    class UserViewer {
        private Label titleLabel;
        private Pane titleWrapper;
        private TableView usersTableView;
        private VBox detailedUsersBox;
        private HBox tableViewAndUsersBox;
        private Button okButton;
        private Button openButton;
        private Button refreshButton;
        private HBox buttonBox;
        private VBox mainBox;
        private Scene scene;
        private Stage stage;
        private List<User> users;

        public UserViewer(List<User> users)
        {
            this.users = users;

            this.setupUserViewer();
            this.setupOkButton();
            this.setupOpenButton();
            this.setupClearButton();
        }

        public void setupUserViewer()
        {
            titleLabel = new Label("USER DETAILS");
            titleWrapper = new Pane(titleLabel);

            titleLabel.layoutXProperty().bind(titleWrapper.widthProperty().subtract(titleLabel.widthProperty()).divide(2));

            {
                titleLabel.getStyleClass().add("welcome-label");
                titleWrapper.setStyle("-fx-background-color: #df00fe;");
            }

            usersTableView = new TableView();

            TableColumn<User, String> firstNameColumn = new TableColumn<>("First Name");
            firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));

            TableColumn<User, String> lastNameColumn = new TableColumn<>("Last Name");
            lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));

            firstNameColumn.setPrefWidth(150);
            firstNameColumn.setStyle("-fx-alignment: CENTER;");
            usersTableView.getColumns().add(firstNameColumn);

            lastNameColumn.setPrefWidth(150);
            lastNameColumn.setStyle("-fx-alignment: CENTER;");
            usersTableView.getColumns().add(lastNameColumn);

            this.setupObservableList();

            firstNameColumn.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        getStyleClass().add("orders-label");
                        setStyle("-fx-background-color: rgba(119,103,103,0.36); -fx-alignment: center;");
                    }
                }
            });

            lastNameColumn.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        getStyleClass().add("orders-label");
                        setStyle("-fx-background-color: rgba(119,103,103,0.36); -fx-alignment: center;");
                    }
                }
            });

            ObservableList<User> data = FXCollections.observableArrayList(users);
            usersTableView.setItems(data);

            usersTableView.setPrefSize(300, 295);

            detailedUsersBox = new VBox();
            detailedUsersBox.setPrefSize(300, 295);

            Label promptLabel = new Label("User Details");

            {
                promptLabel.getStyleClass().add("timer-label");
                detailedUsersBox.setStyle("-fx-background-color: #234829;");
            }

            detailedUsersBox.getChildren().addAll(promptLabel);
            detailedUsersBox.setAlignment(Pos.CENTER);

            tableViewAndUsersBox = new HBox(5, usersTableView, detailedUsersBox);

            okButton = new Button("OK");
            openButton = new Button("OPEN");
            refreshButton = new Button();

            okButton.setPrefSize(300, 50);
            openButton.setPrefSize(245, 50);
            refreshButton.setPrefSize(50, 50);

            {
                okButton.getStyleClass().add("interface-button");
                openButton.getStyleClass().add("interface-button");
                refreshButton.getStyleClass().add("clear-order-button");
            }

            Image refreshImage = new Image("refreshImage.png");
            ImageView imageView1 = new ImageView(refreshImage);
            imageView1.setFitHeight(40);
            imageView1.setFitWidth(40);

            refreshButton.setGraphic(imageView1);

            buttonBox = new HBox(5, refreshButton, openButton, okButton);
            mainBox = new VBox(5, titleWrapper, tableViewAndUsersBox, buttonBox);

            scene = new Scene(mainBox, 605, 400);
            scene.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

            stage = new Stage();
            stage.setScene(scene);
        }

        public void setupObservableList()
        {
            ObservableList<User> data = FXCollections.observableArrayList(users);
            usersTableView.setItems(data);
        }

        public void setupOkButton()
        {
            this.okButton.setOnAction(e -> {
                stage.close();
            });
        }

        public void setupOpenButton()
        {
            this.openButton.setOnAction(e -> {
                User selectedUser = (User) usersTableView.getSelectionModel().getSelectedItem();

                if (selectedUser != null)
                {
                    detailedUsersBox.getChildren().clear();
                    detailedUsersBox.setAlignment(Pos.TOP_LEFT);

                    Label titleUserLabel = new Label("DETAILS");
                    Pane wrapper = new Pane(titleUserLabel);

                    wrapper.setPrefHeight(35);
                    titleUserLabel.layoutXProperty().bind(wrapper.widthProperty().subtract(titleUserLabel.widthProperty()).divide(2));

                    {
                        titleUserLabel.getStyleClass().add("drinks-label-color");
                        wrapper.setStyle("-fx-background-color: #df00fe;");
                    }

                    Label idLabel = new Label("User id: " + selectedUser.getId());
                    Label usernameLabel = new Label("Username: " + selectedUser.getName());
                    Label passwordLabel = new Label("Password: " + selectedUser.getPassword());
                    Label firstNameLabel = new Label("First name: " + selectedUser.getFirstName());
                    Label lastNameLabel = new Label("Last name: " + selectedUser.getLastName());
                    Label ageLabel = new Label("Age: " + selectedUser.getAge() + " ani");
                    Label townLabel = new Label("Town: " + selectedUser.getTown());
                    Label countryLabel = new Label("Country: " + selectedUser.getCountry());
                    Label addressLabel = new Label("Address: " + selectedUser.getAddress());

                    {
                        idLabel.getStyleClass().add("description-label-v6");
                        usernameLabel.getStyleClass().add("description-label-v6");
                        passwordLabel.getStyleClass().add("description-label-v6");
                        firstNameLabel.getStyleClass().add("description-label-v6");
                        lastNameLabel.getStyleClass().add("description-label-v6");
                        ageLabel.getStyleClass().add("description-label-v6");
                        townLabel.getStyleClass().add("description-label-v6");
                        countryLabel.getStyleClass().add("description-label-v6");
                        addressLabel.getStyleClass().add("description-label-v6");
                    }

                    detailedUsersBox.getChildren().addAll(wrapper, idLabel, usernameLabel, passwordLabel, firstNameLabel, lastNameLabel, ageLabel, townLabel, countryLabel, addressLabel);
                }

            });
        }

        public void setupClearButton()
        {
            this.refreshButton.setOnAction(e -> {
                detailedUsersBox.getChildren().clear();
                Label promptLabel = new Label("User Details");
                promptLabel.getStyleClass().add("timer-label");
                detailedUsersBox.getChildren().add(promptLabel);
                detailedUsersBox.setAlignment(Pos.CENTER);

                usersTableView.setStyle("-fx-background-color: blue;");
                new Timeline(new KeyFrame(
                        Duration.millis(500),
                        ae -> usersTableView.setStyle("")
                )).play();

                usersTableView.refresh();
            });
        }

        public Stage getStage() {
            return stage;
        }

        public TableView getUsersTableView() {
            return usersTableView;
        }
    }

    class DrinkViewer {
        private Label titleLabel;
        private Pane titleWrapper;
        private ListView<String> drinkTypesList;
        private TableView<Drink> drinkTableView;
        private HBox displayBox;
        private VBox mainBox;
        private Button okButton;
        private Stage stage;
        private Scene scene;
        private List<Drink> coffees;
        private List<Drink> juices;
        private List<Drink> softDrinks;
        private List<Drink> teas;
        private List<Drink> beers;
        private List<Drink> wines;
        private List<Drink> cocktails;
        private List<Drink> strongDrinks;
        public DrinkViewer(List<Drink> coffees, List<Drink> juices, List<Drink> softDrinks, List<Drink> teas, List<Drink> beers, List<Drink> wines, List<Drink> cocktails, List<Drink> strongDrinks)
        {
            this.coffees = coffees;
            this.juices = juices;
            this.softDrinks = softDrinks;
            this.teas = teas;
            this.beers = beers;
            this.wines = wines;
            this.cocktails = cocktails;
            this.strongDrinks = strongDrinks;

            this.setupDrinkViewer();
            this.setupOkButton();
        }

        public void setupDrinkViewer()
        {
            titleLabel = new Label("DRINK VIEWER");
            titleWrapper = new Pane(titleLabel);

            titleLabel.layoutXProperty().bind(titleWrapper.widthProperty().subtract(titleLabel.widthProperty()).divide(2));

            {
                titleLabel.getStyleClass().add("welcome-label");
                titleWrapper.setStyle("-fx-background-color: #df00fe;");
            }

            drinkTypesList = new ListView<>();
            drinkTypesList.getItems().addAll("COFFEE", "JUICE", "SOFT DRINKS", "TEA", "BEER", "WINE", "COCKTAILS", "STRONG DRINKS");
            drinkTypesList.setPrefSize(200, 330);

            drinkTypesList.setCellFactory(lv -> new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(null);
                    setGraphic(null);

                    if (item != null && !empty) {
                        setText(item);
                        setAlignment(Pos.CENTER);

                        setPadding(new Insets(5, 10, 5, 10));
                        setStyle("-fx-background-color: #25492a; " +
                                "-fx-font-weight: bold; " +
                                "-fx-font-size: 20px; " +
                                "-fx-text-fill: #ffffff;");

                        setOnMouseClicked(event -> {
                            ObservableList<Drink> drinks = getDrinksOfType(item);
                            drinkTableView.setItems(drinks);
                        });

                    } else {
                        setStyle(null);
                        setPadding(Insets.EMPTY);
                        setOnMouseClicked(null);
                    }

                    if (!empty) {
                        if (isSelected()) {
                            setStyle("-fx-background-color: #ea8342; " +
                                    "-fx-font-weight: bold; " +
                                    "-fx-font-size: 20px; " +
                                    "-fx-text-fill: #ffffff;");
                        } else {
                            setStyle("-fx-background-color: #25492a; " +
                                    "-fx-font-weight: bold; " +
                                    "-fx-font-size: 20px; " +
                                    "-fx-text-fill: #ffffff;");
                        }
                    }
                }
            });

            drinkTableView = new TableView();

            TableColumn<Drink, String> nameColumn = new TableColumn<>("Name");
            nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

            TableColumn<Drink, Double> volumeColumn = new TableColumn<>("Volume");
            volumeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getVolume()));

            TableColumn<Drink, Integer> priceColumn = new TableColumn<>("Price");
            priceColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrice()));

            nameColumn.setPrefWidth(165);
            nameColumn.setStyle("-fx-alignment: CENTER;");
            drinkTableView.getColumns().add(nameColumn);

            volumeColumn.setPrefWidth(165);
            volumeColumn.setStyle("-fx-alignment: CENTER;");
            drinkTableView.getColumns().add(volumeColumn);

            priceColumn.setPrefWidth(165);
            priceColumn.setStyle("-fx-alignment: CENTER;");
            drinkTableView.getColumns().add(priceColumn);

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
                        setStyle("-fx-background-color: rgb(105,159,229); -fx-alignment: center;");
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
                        setStyle("-fx-background-color: rgb(105,159,229); -fx-alignment: center;");
                    }
                }
            });

            priceColumn.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(String.valueOf(item + " LEI"));
                        getStyleClass().add("orders-label");
                        setStyle("-fx-background-color: rgb(105,159,229); -fx-alignment: center;");
                    }
                }
            });



            drinkTableView.setPrefSize(495, 330);

            displayBox = new HBox(5, drinkTypesList, drinkTableView);

            okButton = new Button("OK");
            okButton.setPrefSize(700, 50);
            okButton.getStyleClass().add("interface-button");

            mainBox = new VBox(5, titleWrapper, displayBox, okButton);

            scene = new Scene(mainBox, 700, 440);
            scene.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

            stage = new Stage();
            stage.setScene(scene);
        }

        public ObservableList<Drink> getDrinksOfType(String type) {
            ObservableList<Drink> drinks = FXCollections.observableArrayList();

            if (type.equals("COFFEE"))
                drinks = FXCollections.observableArrayList(coffees);
            else if (type.equals("JUICE"))
                drinks = FXCollections.observableArrayList(juices);
            else if (type.equals("SOFT DRINKS"))
                drinks = FXCollections.observableArrayList(softDrinks);
            else if (type.equals("TEA"))
                drinks = FXCollections.observableArrayList(teas);
            else if (type.equals("BEER"))
                drinks = FXCollections.observableArrayList(beers);
            else if (type.equals("WINE"))
                drinks = FXCollections.observableArrayList(wines);
            else if (type.equals("COCKTAILS"))
                drinks = FXCollections.observableArrayList(cocktails);
            else if (type.equals("STRONG DRINKS"))
                drinks = FXCollections.observableArrayList(strongDrinks);

            return drinks;
        }

        public void setupOkButton()
        {
            this.okButton.setOnAction(e -> {
                stage.close();
            });
        }

        public Stage getStage() {
            return stage;
        }
    }

    class FoodViewer {
        private Label titleLabel;
        private Pane titleWrapper;
        private TableView<Food> foodTableView;
        private ListView<String> foodTypesList;
        private Button okButton;
        private HBox displayBox;
        private VBox mainBox;
        private Scene scene;
        private Stage stage;
        private List<Food> salads;
        private List<Food> soups;
        private List<Food> smallBites;
        private List<Food> meat;
        private List<Food> pasta;
        private List<Food> pizza;
        private List<Food> seaFood;
        private List<Food> cakes;
        private List<Food> iceScream;
        private List<Food> otherDessert;

        public FoodViewer(List<Food> salads, List<Food> soups, List<Food> smallBites, List<Food> meat, List<Food> pasta, List<Food> pizza, List<Food> seaFood, List<Food> cakes, List<Food> iceScream, List<Food> otherDessert)
        {
            this.salads = salads;
            this.soups = soups;
            this.smallBites = smallBites;
            this.meat = meat;
            this.pasta = pasta;
            this.pizza = pizza;
            this.seaFood = seaFood;
            this.cakes = cakes;
            this.iceScream = iceScream;
            this.otherDessert = otherDessert;

            this.setupFoodViewer();
            this.setupOkButton();
        }

        public void setupFoodViewer()
        {
            titleLabel = new Label("FOOD VIEWER");
            titleWrapper = new Pane(titleLabel);

            titleLabel.layoutXProperty().bind(titleWrapper.widthProperty().subtract(titleLabel.widthProperty()).divide(2));

            {
                titleLabel.getStyleClass().add("welcome-label");
                titleWrapper.setStyle("-fx-background-color: #df00fe;");
            }

            foodTypesList = new ListView<>();
            foodTypesList.getItems().addAll("SALAD", "SOUP", "SMALL BITES", "MEAT", "PASTA", "SEAFOOD", "PIZZA", "CAKES", "ICE SCREAM", "OTHER DESSERT");
            foodTypesList.setPrefSize(200, 410);

            foodTypesList.setCellFactory(lv -> new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(null);
                    setGraphic(null);

                    if (item != null && !empty) {
                        setText(item);
                        setAlignment(Pos.CENTER);

                        setPadding(new Insets(5, 10, 5, 10));
                        setStyle("-fx-background-color: #25492a; " +
                                "-fx-font-weight: bold; " +
                                "-fx-font-size: 20px; " +
                                "-fx-text-fill: #ffffff;");

                        setOnMouseClicked(event -> {
                            ObservableList<Food> foods = getFoodOfType(item);
                            foodTableView.setItems(foods);
                        });

                    } else {
                        setStyle(null);
                        setPadding(Insets.EMPTY);
                        setOnMouseClicked(null);
                    }

                    if (!empty) {
                        if (isSelected()) {
                            setStyle("-fx-background-color: #ea8342; " +
                                    "-fx-font-weight: bold; " +
                                    "-fx-font-size: 20px; " +
                                    "-fx-text-fill: #ffffff;");
                        } else {
                            setStyle("-fx-background-color: #25492a; " +
                                    "-fx-font-weight: bold; " +
                                    "-fx-font-size: 20px; " +
                                    "-fx-text-fill: #ffffff;");
                        }
                    }
                }
            });

            foodTableView = new TableView();

            TableColumn<Food, String> nameColumn = new TableColumn<>("Name");
            nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

            TableColumn<Food, String> descriptionColumn = new TableColumn<>("Description");
            descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

            TableColumn<Food, Integer> gramsColumn = new TableColumn<>("Grams");
            gramsColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getGrams()));

            TableColumn<Food, Integer> priceColumn = new TableColumn<>("Price");
            priceColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrice()));

            TableColumn<Food, Float> ratingColumn = new TableColumn<>("Rating");
            ratingColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getRating()));

            nameColumn.setPrefWidth(119);
            nameColumn.setStyle("-fx-alignment: CENTER;");
            foodTableView.getColumns().add(nameColumn);

            descriptionColumn.setPrefWidth(119);
            descriptionColumn.setStyle("-fx-alignment: CENTER;");
            foodTableView.getColumns().add(descriptionColumn);

            gramsColumn.setPrefWidth(119);
            gramsColumn.setStyle("-fx-alignment: CENTER;");
            foodTableView.getColumns().add(gramsColumn);

            priceColumn.setPrefWidth(119);
            priceColumn.setStyle("-fx-alignment: CENTER;");
            foodTableView.getColumns().add(priceColumn);

            ratingColumn.setPrefWidth(119);
            ratingColumn.setStyle("-fx-alignment: CENTER;");
            foodTableView.getColumns().add(ratingColumn);

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
                        setStyle("-fx-background-color: rgb(105,159,229); -fx-alignment: center;");
                    }
                }
            });

            descriptionColumn.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        getStyleClass().add("orders-label");
                        setStyle("-fx-background-color: rgb(105,159,229); -fx-alignment: center;");
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
                        setStyle("-fx-background-color: rgb(105,159,229); -fx-alignment: center;");
                    }
                }
            });

            priceColumn.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(String.valueOf(item + " LEI"));
                        getStyleClass().add("orders-label");
                        setStyle("-fx-background-color: rgb(105,159,229); -fx-alignment: center;");
                    }
                }
            });

            ratingColumn.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(Float item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(String.valueOf(item + "☆"));
                        getStyleClass().add("orders-label");
                        setStyle("-fx-background-color: rgb(105,159,229); -fx-alignment: center;");
                    }
                }
            });

            foodTableView.setPrefSize(595, 410);

            displayBox = new HBox(5, foodTypesList, foodTableView);

            okButton = new Button("OK");
            okButton.setPrefSize(800, 50);
            okButton.getStyleClass().add("interface-button");

            mainBox = new VBox(5, titleWrapper, displayBox, okButton);

            scene = new Scene(mainBox, 800, 520);
            scene.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

            stage = new Stage();
            stage.setScene(scene);
        }

        public ObservableList<Food> getFoodOfType(String type)
        {
            ObservableList<Food> food = FXCollections.observableArrayList();

            if (type.equals("SALAD"))
                food = FXCollections.observableArrayList(salads);
            else if (type.equals("SOUP"))
                food = FXCollections.observableArrayList(soups);
            else if (type.equals("SMALL BITES"))
                food = FXCollections.observableArrayList(smallBites);
            else if (type.equals("MEAT"))
                food = FXCollections.observableArrayList(meat);
            else if (type.equals("PASTA"))
                food = FXCollections.observableArrayList(pasta);
            else if (type.equals("PIZZA"))
                food = FXCollections.observableArrayList(pizza);
            else if (type.equals("SEAFOOD"))
                food = FXCollections.observableArrayList(seaFood);
            else if (type.equals("CAKES"))
                food = FXCollections.observableArrayList(cakes);
            else if (type.equals("ICE SCREAM"))
                food = FXCollections.observableArrayList(iceScream);
            else if (type.equals("OTHER DESSERT"))
                food = FXCollections.observableArrayList(otherDessert);

            return food;
        }

        public void setupOkButton()
        {
            this.okButton.setOnAction(e -> {
                stage.close();
            });
        }

        public Stage getStage() {
            return stage;
        }
    }

    class PaymentViewer {
        private Label titleLabel;
        private Pane titleWrapper;
        private ListView<Payment> paymentsList;
        private Button okButton;
        private VBox mainBox;
        private Scene scene;
        private Stage stage;

        public PaymentViewer()
        {
            this.setupPaymentViewer();
            this.setupOkButton();
        }

        public void setupPaymentViewer()
        {
            titleLabel = new Label("ALL PAYMENTS");
            titleWrapper = new Pane(titleLabel);

            titleLabel.layoutXProperty().bind(titleWrapper.widthProperty().subtract(titleLabel.widthProperty()).divide(2));

            {
                titleLabel.getStyleClass().add("welcome-label");
                titleWrapper.setStyle("-fx-background-color: #0e0e0e;");
            }

            paymentsList = new ListView<>();
            ObservableList<Payment> observableList = FXCollections.observableArrayList(payments);
            paymentsList.setItems(observableList);

            paymentsList.setCellFactory(lv -> new ListCell<Payment>() {
                @Override
                protected void updateItem(Payment item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    }
                    else {
                            setAlignment(Pos.CENTER);
                            setText(item.toString()); // Replace with your desired string representation of Payment
                            setStyle("-fx-background-color: #792d88; " +
                                    "-fx-font-weight: bold; " +
                                    "-fx-font-size: 20px; " +
                                    "-fx-text-fill: #ffffff;");
                        }
                    }

            });


            paymentsList.setPrefSize(300, 300);

            okButton = new Button("OK");
            okButton.setPrefSize(300, 50);

            {
                okButton.getStyleClass().add("interface-button");
            }

            mainBox = new VBox(5, titleWrapper, paymentsList, okButton);

            scene = new Scene(mainBox, 300, 410);
            scene.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

            stage = new Stage();
            stage.setScene(scene);
        }

        public void setupOkButton()
        {
            okButton.setOnAction(e -> {
                stage.close();
            });
        }

        public ListView<Payment> getPaymentsList() {
            return paymentsList;
        }

        public Stage getStage() {
            return stage;
        }
    }

    public void setupAllOrdersButton()
    {
        this.allOrdersButton.setOnAction(e -> {
            this.ordersViewer.getStage().show();
        });
    }

    public void setupAllUsersButton()
    {
        this.allUsersButton.setOnAction(e -> {
            this.userViewer.getStage().show();
        });
    }

    public void setupAllDrinksButton()
    {
        this.allDrinksButton.setOnAction(e -> {
            this.drinkViewer.getStage().show();
        });
    }

    public void setupAllFoodButton()
    {
        this.allFoodButton.setOnAction(e -> {
            this.foodViewer.getStage().show();
        });
    }

    public void setupAllPaymentsButton()
    {
        this.allPaymentsButton.setOnAction(e -> {
            paymentViewer.getStage().show();
        });
    }

    public TableView getAllOrdersView() {
        return ordersViewer.getAllOrdersView();
    }

    public PaymentViewer getPaymentViewer() {
        return paymentViewer;
    }

    public OrdersViewer getOrdersViewer() {
        return ordersViewer;
    }

    public UserViewer getUserViewer() {
        return userViewer;
    }
}
