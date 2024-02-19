package ClientController;

import CheckoutController.LiveOrdersController;
import CheckoutController.RestaurantController;
import CheckoutController.ViewerController;
import Model.DeliveryInfo;
import Model.Drink;
import Model.Food;
import Model.Order;
import SQLDataBase.DeliveryOrderDataBase;
import SQLDataBase.OrderDataBase;
import SQLDataBase.OrderHistoryDataBase;
import Service.IndexManager;
import javafx.animation.PauseTransition;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class YourOrderViewController {
    private ListView<Object> order;
    private Label totalSumLabel;
    private Label thankYouLabel;
    private Label placedOrderLabel;
    private Label finalLabel;
    private Label paymentMethodLabel;
    private Label emptyOrderLabel;
    private Button placeOrderButton;
    private Button removeButton;
    private Button cashButton;
    private Button cardButton;
    private Button undoButton;
    private Button viewOrderButton;
    private Button modifyPaymentButton;
    private Button submitButton;
    private Button OkButton;
    private Button modifyButton;
    private Button rateFoodButton;
    private Button backToMenuButton;
    private VBox orderBox;
    private HBox payBox;
    private HBox payBoxWithoutBack;
    private VBox finalPayBox;
    private VBox finalLabelsBox;
    private HBox finalOrderButtons;
    private VBox viewFinalOrderBox;
    private VBox submitButtonBox;
    private float totalSum;
    private String paymentType;
    private Pane labelWrapper;
    private boolean isModifyButtonPressed;
    private List<Drink> orderedDrink;
    private List<Food> orderedFood;
    private Order placedOrder;
    private IndexManager indexManager;
    private OrderDataBase orderDataBase;
    private OrderHistoryDataBase orderHistoryDataBase;
    private Label tableNumberLabel;
    private TextField tableNumberField;
    private Button enterTableButton;
    private HBox tableNumberBox;
    private Label placeLabel;
    private Pane placeWrapper;
    private Button terraceButton;
    private Button insideButton;
    private HBox placeBox;
    private boolean placeChoice;
    private boolean isInsideButtonPressed;
    private Label insideLabel;
    private Label terraceLabel;
    private Button tableUndoButton;
    private Button placeUndoButton;
    private Button deliveryButton;
    private Label deliveryTitleLabel;
    private Label nameLabel;
    private Label phoneLabel;
    private Label addressLabel;
    private Label interphoneLabel;
    private Label floorLabel;
    private TextField nameField;
    private TextField phoneField;
    private TextField addressField;
    private TextField interphoneField;
    private TextField floorField;
    private Button closeButton;
    private Button doneButton;
    private HBox buttonsDeliveryBox;
    private VBox labelsDeliveryBox;
    private GridPane gridPane;
    private Scene deliveryScene;
    private Stage deliveryStage;
    private boolean enterButtonPressed;
    private boolean finalStagePressed;
    private boolean deliveryButtonPressed;
    private List<Order> deliveryOrders;
    private List<Order> orders;
    private List<Order> orderHistory;
    private Map<Pair<String, Integer>, Order> ordersMap;
    private DeliveryOrderDataBase deliveryOrderDataBase;
    private DeliveryInfo deliveryInfo;
    private VBox tableNumberAndPlaceBox;
    private HBox checkBox;
    private Button checkButton;
    private Button tickButton;
    private boolean checkButtonPressed;
    private boolean orderIsReady;
    private boolean orderIsPlaced;

    public YourOrderViewController(List<Drink> orderedDrink, List<Food> orderedFood, OrderDataBase orderDataBase, OrderHistoryDataBase orderHistoryDataBase, IndexManager indexManager, List<Order> deliveryOrders, DeliveryOrderDataBase deliveryOrderDataBase, List<Order> orders, List<Order> orderHistory, Map<Pair<String, Integer>, Order> ordersMap)
    {
        this.order = new ListView<>();
        this.totalSumLabel = new Label("0.00 RON");
        this.thankYouLabel = new Label("THANK YOU!");
        this.placedOrderLabel = new Label("THANK YOU! PRESS SUBMIT TO PLACE THE ORDER!");
        this.paymentMethodLabel = new Label();
        this.finalLabel = new Label("THANK YOU FOR CHOOSING OUR RESTAURANT!");
        this.emptyOrderLabel = new Label("Your order");
        this.placeOrderButton = new Button("PLACE ORDER");
        this.removeButton = new Button("REMOVE");
        this.cashButton = new Button("PAY BY CASH");
        this.cardButton = new Button("PAY BY CARD");
        this.undoButton = new Button("BACK");
        this.viewOrderButton = new Button("VIEW YOUR ORDER");
        this.modifyPaymentButton = new Button("MODIFY PAYMENT METHOD");
        this.submitButton = new Button("SUBMIT");
        this.OkButton = new Button("OK");
        this.modifyButton = new Button("MODIFY");
        this.rateFoodButton = new Button("RATE FOOD");
        this.backToMenuButton = new Button("BACK TO START MENU");
        this.enterTableButton = new Button("ENTER");
        this.terraceButton = new Button("Terrace");
        this.insideButton = new Button("Inside");
        this.deliveryButton = new Button("Delivery");
        this.checkButton = new Button("CHECK");
        this.tickButton = new Button();
        this.placeUndoButton = new Button();
        this.tableUndoButton = new Button();
        this.insideLabel= new Label();
        this.terraceLabel = new Label();
        this.totalSum = 0;
        this.paymentType = "";
        this.isModifyButtonPressed = false;
        this.placeChoice = false;
        this.isInsideButtonPressed = false;
        this.enterButtonPressed = false;
        this.finalStagePressed = false;
        this.deliveryButtonPressed = false;
        this.checkButtonPressed = false;
        this.orderIsReady = false;
        this.orderIsPlaced = false;
        this.deliveryInfo = new DeliveryInfo();

        this.placedOrder = new Order();

        this.orderedDrink = orderedDrink;
        this.orderedFood = orderedFood;
        this.orderDataBase = orderDataBase;
        this.orderHistoryDataBase = orderHistoryDataBase;
        this.indexManager = indexManager;
        this.deliveryOrders = deliveryOrders;
        this.deliveryOrderDataBase = deliveryOrderDataBase;
        this.orders = orders;
        this.orderHistory = orderHistory;
        this.ordersMap = ordersMap;

        this.setupDesignAndStylesOfOrderBox();

        this.setupDeliveryStage();
    }

    public void setupDesignAndStylesOfOrderBox()
    {
        labelWrapper = new Pane(totalSumLabel);

        labelWrapper.setPrefHeight(50);
        labelWrapper.setStyle("-fx-background-color: #b833ff;");

        totalSumLabel.layoutXProperty().bind(labelWrapper.widthProperty().subtract(totalSumLabel.widthProperty()).divide(2));
        totalSumLabel.layoutYProperty().bind(labelWrapper.heightProperty().subtract(totalSumLabel.heightProperty()).divide(2));

        this.orderBox = new VBox(5, order, labelWrapper, removeButton , placeOrderButton);

        VBox.setVgrow(orderBox, Priority.ALWAYS);

        labelWrapper.setMaxWidth(Double.MAX_VALUE);

        placeOrderButton.setMaxWidth(Double.MAX_VALUE);
        removeButton.setMaxWidth(Double.MAX_VALUE);

        placeOrderButton.setPrefHeight(50);
        removeButton.setPrefHeight(50);
        totalSumLabel.setPrefHeight(50);
        totalSumLabel.setAlignment(Pos.CENTER);

        order.setPrefHeight(550);
        order.setPrefWidth(400);

        order.setPlaceholder(emptyOrderLabel);

        {
            placeOrderButton.getStyleClass().add("menu-button");
            removeButton.getStyleClass().add("menu-button");

            totalSumLabel.getStyleClass().add("welcome-label");
            totalSumLabel.setStyle("-fx-text-fill: white;");

            order.getStyleClass().add("order-view");

            emptyOrderLabel.getStyleClass().add("order-text");
        }

        order.setCellFactory(lv -> {
            ListCell<Object> cell = new ListCell<>() {
                @Override
                protected void updateItem(Object item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                        getStyleClass().remove("order-view-filled");
                        getStyleClass().add("list-cell-empty");
                    } else if (item instanceof VBox)
                    {
                        VBox cellBox = (VBox) item;
                        cellBox.setAlignment(Pos.CENTER);
                        setGraphic(cellBox);

                        setPrefHeight(150);
                        getStyleClass().add("order-view-filled");
                        getStyleClass().remove("list-cell-empty");
                        getStyleClass().add("italic-text");
                    }
                }
            };
            cell.getStyleClass().add("list-cell");
            return cell;
        });

        order.getStyleClass().add("order-view-empty");

        order.getItems().addListener((ListChangeListener<Object>) c -> {
            if (order.getItems().isEmpty()) {
                order.getStyleClass().remove("order-view-filled");
                order.getStyleClass().add("order-view-empty");
            } else {
                order.getStyleClass().add("order-view-filled");
                order.getStyleClass().remove("order-view-empty");
            }
        });
    }

    public void addItemToOrder(VBox item)
    {
        this.order.getItems().add(item);
    }

    public void updateTotalSum(float sum)
    {
        this.totalSum += sum;
        this.totalSumLabel.setText(totalSum + " LEI");
    }

    public void resetTotalSum()
    {
        this.totalSum = 0;
        this.totalSumLabel.setText(totalSum + " LEI");
    }

    public void setupRemoveButton(RateFoodController rateFoodController)
    {
        this.removeButton.setOnAction(e -> {
            int selectedIndex = this.order.getSelectionModel().getSelectedIndex();

            if (selectedIndex != -1)
            {
                Object selectedItem = this.order.getItems().get(selectedIndex);

                VBox selectedBox = (VBox) selectedItem;

                VBox nameAndImageBox = (VBox) selectedBox.getChildren().get(0);
                Label nameLabel = (Label) nameAndImageBox.getChildren().get(0);
                String name = nameLabel.getText();

                orderedDrink.removeIf(drink -> drink.getName().equals(name));
                orderedFood.removeIf(food -> food.getName().equals(name));

                HBox detailsBox = (HBox) selectedBox.getChildren().get(1);

                if (detailsBox.getChildren().size() == 2)
                {
                    Label priceLabel = (Label) detailsBox.getChildren().get(1);
                    float sumToRemove = Float.parseFloat(priceLabel.getText().replace("LEI", "").replace("Price: ", ""));

                    this.updateTotalSum(-sumToRemove);
                }
                else if (detailsBox.getChildren().size() == 3)
                {
                    Label priceLabel = (Label) detailsBox.getChildren().get(1);
                    float sumToRemove = Float.parseFloat(priceLabel.getText().replace("LEI", "").replace("Price: ", ""));

                    this.updateTotalSum(-sumToRemove);
                }

                this.order.getItems().remove(selectedItem);
                this.order.getSelectionModel().clearSelection();

                rateFoodController.getOrderedFood().removeIf(food -> food.getName().equals(name));
            }
        });
    }

    public void setupPayWithoutBackButton()
    {
        cashButton.setPrefSize(400, 400);
        cardButton.setPrefSize(400,400);

        {
            cashButton.getStyleClass().add("menu-button");
            cardButton.getStyleClass().add("menu-button");
        }

        payBoxWithoutBack = new HBox(40, cashButton, cardButton);
        payBoxWithoutBack.setAlignment(Pos.CENTER);
    }

    public void setPayMethods()
    {
        cashButton.setPrefSize(400, 400);
        cardButton.setPrefSize(400,400);
        undoButton.setPrefSize(840, 100);

        {
            cashButton.getStyleClass().add("menu-button");
            cardButton.getStyleClass().add("menu-button");
            undoButton.getStyleClass().add("menu-button");
        }

        payBox = new HBox(40, cashButton, cardButton);
        payBox.setAlignment(Pos.CENTER);

        finalPayBox = new VBox(40, payBox, undoButton);
        finalPayBox.setAlignment(Pos.CENTER);
    }

    public void setupPaymentLabel()
    {
        if (paymentType.equals("Cash"))
            paymentMethodLabel.setText("PAYMENT METHOD: CASH");
        else if (paymentType.equals("Card"))
            paymentMethodLabel.setText("PAYMENT METHOD: CARD");
    }

    public void setupFinalStage()
    {
        //this.setupPaymentLabel();

        tableNumberLabel = new Label("Table number:");
        tableNumberField = new TextField();
        Pane tableNumberWrapper = new Pane(tableNumberLabel);

        tableNumberField.setPrefSize(100, 50);
        enterTableButton.setPrefSize(100, 50);

        placeLabel = new Label("Place:");
        placeWrapper = new Pane(placeLabel);

        terraceButton.setPrefSize(100, 50);
        insideButton.setPrefSize(100,50);
        deliveryButton.setPrefSize(100, 50);
        checkButton.setPrefSize(350, 50);
        tickButton.setPrefSize(50, 50);

        viewOrderButton.setPrefSize(300, 100);
        modifyPaymentButton.setPrefSize(300, 100);
        submitButton.setPrefSize(300, 100);
        rateFoodButton.setPrefSize(300, 100);

        Image undoImage = new Image("undo-6.PNG");
        ImageView imageView  = new ImageView(undoImage);
        imageView.setFitHeight(30);
        imageView.setFitWidth(30);

        ImageView imageView2  = new ImageView(undoImage);
        imageView2.setFitHeight(30);
        imageView2.setFitWidth(30);

        placeUndoButton.setGraphic(imageView);
        tableUndoButton.setGraphic(imageView2);

        placeUndoButton.setPrefSize(50, 50);
        tableUndoButton.setPrefSize(50, 50);

        {
            viewOrderButton.getStyleClass().add("menu-button");
            modifyPaymentButton.getStyleClass().add("menu-button");
            submitButton.getStyleClass().add("menu-button");
            rateFoodButton.getStyleClass().add("menu-button");
            enterTableButton.getStyleClass().add("menu-button");
            terraceButton.getStyleClass().add("choice-button");
            insideButton.getStyleClass().add("choice-button");
            deliveryButton.getStyleClass().add("choice-button");
            placeUndoButton.getStyleClass().add("live-order-button");
            tableUndoButton.getStyleClass().add("live-order-button");
            checkButton.getStyleClass().add("menu-button");
            tickButton.getStyleClass().add("live-order-button");

            thankYouLabel.getStyleClass().add("thankYou-label");
            placedOrderLabel.getStyleClass().add("drinks-label");
            paymentMethodLabel.getStyleClass().add("drinks-label");
            tableNumberLabel.getStyleClass().add("thankYou-label");
            placeLabel.getStyleClass().add("thankYou-label");
            tableNumberWrapper.setStyle("-fx-background-color: #df00fe;");
            placeWrapper.setStyle("-fx-background-color: #df00fe;");

            tableNumberField.setStyle("-fx-font-size: 20px;");
            tableNumberField.getStyleClass().add("drinks-label");
        }

        HBox orderButtonsBox = new HBox(40, viewOrderButton, modifyPaymentButton);
        orderButtonsBox.setAlignment(Pos.CENTER);

        HBox rateFoodBox = new HBox(40, rateFoodButton, submitButton);
        rateFoodBox.setAlignment(Pos.CENTER);

        tableNumberBox = new HBox(5, tableNumberWrapper, tableNumberField, enterTableButton, tableUndoButton);
        tableNumberBox.setAlignment(Pos.CENTER);

        placeBox = new HBox(5, placeWrapper, insideButton, terraceButton, deliveryButton, placeUndoButton);
        placeBox.setAlignment(Pos.CENTER);

        checkBox = new HBox(10, checkButton, tickButton);
        checkBox.setAlignment(Pos.CENTER);

        tableNumberAndPlaceBox = new VBox(10, tableNumberBox, placeBox, checkBox);
        tableNumberAndPlaceBox.setAlignment(Pos.CENTER);

        finalLabelsBox = new VBox(40, paymentMethodLabel, placedOrderLabel,  orderButtonsBox, tableNumberAndPlaceBox, rateFoodBox);
        finalLabelsBox.setAlignment(Pos.CENTER);
    }

    public void setupViewFinalOrder()
    {
        {
            OkButton.getStyleClass().add("menu-button");
            modifyButton.getStyleClass().add("menu-button");
        }

        OkButton.setPrefSize(150, 50);
        modifyButton.setPrefSize(150, 50);

        finalOrderButtons = new HBox(10, OkButton, modifyButton);
        finalOrderButtons.setAlignment(Pos.CENTER);

        viewFinalOrderBox = new VBox(5, order, labelWrapper,finalOrderButtons);
        viewFinalOrderBox.setAlignment(Pos.CENTER);

        order.setPrefHeight(500);
        order.setPrefWidth(300);
    }

    public void setupSubmitButtonBox()
    {
        {
            finalLabel.getStyleClass().add("welcome-label");
            backToMenuButton.getStyleClass().add("menu-button");
        }

        backToMenuButton.setPrefSize(500, 150);

        submitButtonBox = new VBox(40, finalLabel, backToMenuButton);
        submitButtonBox.setAlignment(Pos.CENTER);
    }

    public void resetOrderBox()
    {
        order.setPrefHeight(550);
        order.setPrefWidth(400);

        labelWrapper.setMaxWidth(Double.MAX_VALUE);

        //totalSumLabel.setMaxWidth(Double.MAX_VALUE);
        placeOrderButton.setMaxWidth(Double.MAX_VALUE);
        removeButton.setMaxWidth(Double.MAX_VALUE);

        this.orderBox.getChildren().clear();
        this.orderBox.getChildren().addAll(order, labelWrapper, removeButton, placeOrderButton);
    }

    public void setupDeliveryStage()
    {
        deliveryTitleLabel = new Label("DELIVERY");
        Pane titleWrapper = new Pane(deliveryTitleLabel);

        deliveryTitleLabel.layoutXProperty().bind(titleWrapper.widthProperty().subtract(deliveryTitleLabel.widthProperty()).divide(2));

        {
            deliveryTitleLabel.getStyleClass().add("welcome-label");
            titleWrapper.setStyle("-fx-background-color: #df00fe;");
        }

        nameLabel = new Label("Name:");
        nameField = new TextField();
        phoneLabel = new Label("Phone:");
        phoneField = new TextField();
        addressLabel = new Label("Address:");
        addressField = new TextField();
        interphoneLabel = new Label("Apartment:");
        interphoneField = new TextField();
        floorLabel = new Label("Floor:");
        floorField = new TextField();

        Pane nameWrapper = new Pane(nameLabel);
        Pane phoneWrapper = new Pane(phoneLabel);
        Pane addressWrapper = new Pane(addressLabel);
        Pane interphoneWrapper = new Pane(interphoneLabel);
        Pane floorWrapper = new Pane(floorLabel);

        {
            nameLabel.getStyleClass().add("drinks-label-color-v4");
            phoneLabel.getStyleClass().add("drinks-label-color-v4");
            addressLabel.getStyleClass().add("drinks-label-color-v4");
            interphoneLabel.getStyleClass().add("drinks-label-color-v4");
            floorLabel.getStyleClass().add("drinks-label-color-v4");

            nameWrapper.setStyle("-fx-background-color: #df00fe;");
            phoneWrapper.setStyle("-fx-background-color: #df00fe;");
            addressWrapper.setStyle("-fx-background-color: #df00fe;");
            interphoneWrapper.setStyle("-fx-background-color: #df00fe;");
            floorWrapper.setStyle("-fx-background-color: #df00fe;");

            nameField.getStyleClass().add("drinks-label");
            phoneField.getStyleClass().add("drinks-label");
            addressField.getStyleClass().add("drinks-label");
            interphoneField.getStyleClass().add("drinks-label");
            floorField.getStyleClass().add("drinks-label");

        }

        nameField.setPrefSize(315, 50);
        phoneField.setPrefSize(315, 50);
        addressField.setPrefSize(315, 50);
        interphoneField.setPrefSize(315, 50);
        floorField.setPrefSize(315, 50);

        doneButton = new Button("DONE");
        closeButton = new Button("CLOSE");

        {
            doneButton.getStyleClass().add("menu-button");
            closeButton.getStyleClass().add("menu-button");
        }

        doneButton.setPrefSize(240,50);
        closeButton.setPrefSize(240, 50);

        gridPane = new GridPane();
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        gridPane.add(nameWrapper, 0,0);
        gridPane.add(nameField, 1, 0);
        gridPane.add(phoneWrapper, 0, 1);
        gridPane.add(phoneField, 1,1);
        gridPane.add(addressWrapper, 0, 2);
        gridPane.add(addressField, 1, 2);
        gridPane.add(interphoneWrapper, 0,3);
        gridPane.add(interphoneField, 1, 3);
        gridPane.add(floorWrapper, 0, 4);
        gridPane.add(floorField, 1, 4);


        buttonsDeliveryBox = new HBox(5, closeButton, doneButton);

        labelsDeliveryBox = new VBox(5, titleWrapper, gridPane, buttonsDeliveryBox);

        deliveryScene = new Scene(labelsDeliveryBox, 485, 385);
        deliveryScene.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

        deliveryStage = new Stage();
        deliveryStage.setScene(deliveryScene);
    }

    public void clearOrder() {
        this.order.getItems().clear();
    }

    public void setupPlaceOrderButton(BorderPane borderPane, RateFoodController rateFoodController)
    {
        this.placeOrderButton.setOnAction(e -> {

            if (!orderIsPlaced)
            {
                if (!isModifyButtonPressed)
                {
                    if (order.getItems().isEmpty())
                    {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("EMPTY ORDER");
                        alert.setHeaderText(null);
                        alert.setContentText("Your order is empty!");

                        DialogPane dialogPane = alert.getDialogPane();
                        dialogPane.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

                        Label contentLabel = (Label) dialogPane.lookup(".content");
                        if (contentLabel != null) {
                            contentLabel.getStyleClass().add("alert-content-text");
                        }

                        alert.showAndWait();
                    }
                    else
                    {
                        this.setPayMethods();
                        borderPane.setLeft(null);
                        borderPane.setRight(null);
                        borderPane.setCenter(this.getFinalPayBox());
                    }
                }
                else
                {
                    borderPane.setLeft(null);
                    borderPane.setRight(null);
                    borderPane.setCenter(this.getFinalLabelsBox());
                    rateFoodController.setupFoodBoxAfterModify();
                }
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("RESTART THE APPLICATION");
                alert.setHeaderText(null);
                alert.setContentText("To place another order, restart the app!");

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

    public void setupBackButton(BorderPane borderPane, HBox drinkActionBox, HBox foodActionBox, StartMenuController startMenuController)
    {
        this.undoButton.setOnAction(e -> {
            borderPane.setCenter(null);

            if (startMenuController.getCurrentContext().equals("drinks"))
            {
                borderPane.setLeft(drinkActionBox);
                borderPane.setRight(this.getOrderBox());
            }
            else if (startMenuController.getCurrentContext().equals("foods"))
            {
                borderPane.setLeft(foodActionBox);
                borderPane.setRight(this.getOrderBox());
            }
        });
    }

    public void setupCashButton(BorderPane borderPane)
    {
        this.cashButton.setOnAction(e -> {
            this.paymentType = "Cash";
            this.placedOrder.setPayMethod("Cash");

            if (!finalStagePressed)
                this.setupFinalStage();

            finalStagePressed = true;

            this.setupPaymentLabel();

            borderPane.setCenter(null);
            borderPane.setCenter(this.getFinalLabelsBox());
        });
    }

    public void setupCardButton(BorderPane borderPane)
    {
        this.cardButton.setOnAction(e -> {
            this.paymentType = "Card";
            this.placedOrder.setPayMethod("Card");

            if (!finalStagePressed)
                this.setupFinalStage();

            finalStagePressed = true;

            this.setupPaymentLabel();

            borderPane.setCenter(null);
            borderPane.setCenter(this.getFinalLabelsBox());
        });
    }

    public void setupViewOrderButton(BorderPane borderPane)
    {
        this.viewOrderButton.setOnAction(e -> {
            this.setupViewFinalOrder();
            borderPane.setCenter(null);
            borderPane.setCenter(this.getViewFinalOrderBox());
        });
    }

    public void setupModifyPaymentMethod(BorderPane borderPane)
    {
        this.modifyPaymentButton.setOnAction(e -> {
            this.setupPayWithoutBackButton();
            borderPane.setCenter(null);
            borderPane.setCenter(this.getPayBoxWithoutBack());
        });
    }

    public void setupOkButton(BorderPane borderPane)
    {
        this.OkButton.setOnAction(e -> {
            borderPane.setCenter(null);
            borderPane.setCenter(this.getFinalLabelsBox());
        });
    }

    public void setupModifyButton(BorderPane borderPane, StartMenuController startMenuController)
    {
        this.modifyButton.setOnAction(e -> {
            borderPane.setCenter(null);
            borderPane.setLeft(startMenuController.getLeftMenu());

            this.resetOrderBox();
            borderPane.setRight(this.getOrderBox());

            this.isModifyButtonPressed = true;
        });
    }

    public void setupSubmitButton(BorderPane borderPane, ViewerController viewerController, RestaurantController restaurantController, LiveOrdersController liveOrdersController)
    {
        this.submitButton.setOnAction(e -> {

            if (enterButtonPressed && placeChoice)
            {
                if (checkButtonPressed)
                {
                    if (orderIsReady)
                    {
                        this.setupSubmitButtonBox();
                        borderPane.setCenter(null);
                        borderPane.setCenter(this.getSubmitButtonBox());

                        BorderPane.setAlignment(this.getSubmitButtonBox(), Pos.CENTER);

                        LocalDateTime now = LocalDateTime.now();

                        placedOrder.setId(indexManager.getCurrentIndex());
                        placedOrder.setName("ORDER " + indexManager.getCurrentIndex());
                        placedOrder.setPrice(totalSum);
                        placedOrder.setLocalDateTime(now);
                        placedOrder.setOrderedDrink(orderedDrink);
                        placedOrder.setOrderedFood(orderedFood);

                        if (!deliveryButtonPressed)
                            placedOrder.setTableNumber(Integer.parseInt(tableNumberField.getText().trim()));
                        else
                            placedOrder.setTableNumber(0);

                        indexManager.increment();

                        if (!deliveryButtonPressed)
                        {
                            if (isInsideButtonPressed)
                                placedOrder.setPlaceName(insideLabel.getText().trim());
                            else
                                placedOrder.setPlaceName(terraceLabel.getText().trim());
                        }
                        else
                            placedOrder.setPlaceName("delivery");

                        if (deliveryButtonPressed)
                            placedOrder.setDeliveryInfo(deliveryInfo);
                        else
                            placedOrder.setDeliveryInfo(null);

                        LocalTime localTime = LocalTime.MIDNIGHT;
                        placedOrder.setFinishedTime(localTime);

                        try {
                            indexManager.saveIndex();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                        if (!deliveryButtonPressed)
                            orderDataBase.addOrderToDataBase(placedOrder);
                        else
                            deliveryOrderDataBase.addOrderToDataBase(placedOrder);

                        Order clonedOrder = placedOrder.clone();

                        orderHistoryDataBase.addOrderToDataBase(clonedOrder);
                        orderHistory.add(clonedOrder);
                        viewerController.getAllOrdersView().getItems().add(clonedOrder);

                        if (deliveryButtonPressed)
                        {
                            deliveryOrders.add(placedOrder);

                            RestaurantController.DeliveryController deliveryController = restaurantController.getDeliveryController();
                            deliveryController.getDeliveryOrdersView().getItems().add(placedOrder);
                        }
                        else
                        {
                            orders.add(placedOrder);

                            LiveOrdersController.LiveOrdersPane liveOrdersPane = liveOrdersController.getLiveOrdersPane();
                            liveOrdersPane.getOrdersList().getItems().add(placedOrder);

                            Pair<String, Integer> key = new Pair<>(placedOrder.getPlaceName(), placedOrder.getTableNumber());
                            ordersMap.put(key, placedOrder);
                        }

                        this.orderIsPlaced = true;
                    }
                    else
                    {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("TABLE");
                        alert.setHeaderText(null);
                        alert.setContentText("This table has another order!");

                        DialogPane dialogPane = alert.getDialogPane();
                        dialogPane.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

                        Label contentLabel = (Label) dialogPane.lookup(".content");
                        if (contentLabel != null) {
                            contentLabel.getStyleClass().add("alert-content-text");
                        }

                        alert.showAndWait();
                    }
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("CHECK");
                    alert.setHeaderText(null);
                    alert.setContentText("Please check the order!");

                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

                    Label contentLabel = (Label) dialogPane.lookup(".content");
                    if (contentLabel != null) {
                        contentLabel.getStyleClass().add("alert-content-text");
                    }

                    alert.showAndWait();
                }
            }
            else if (!enterButtonPressed)
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("EMPTY FIELD");
                alert.setHeaderText(null);
                alert.setContentText("Enter the table number!");

                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

                Label contentLabel = (Label) dialogPane.lookup(".content");
                if (contentLabel != null) {
                    contentLabel.getStyleClass().add("alert-content-text");
                }

                alert.showAndWait();
            }
            else if (!placeChoice)
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("EMPTY FIELD");
                alert.setHeaderText(null);
                alert.setContentText("Enter the place!");

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

    public void setupBackToMenuButton(BorderPane borderPane, StartMenuController startMenuController, RateFoodController rateFoodController)
    {
        this.backToMenuButton.setOnAction(e -> {
            borderPane.setCenter(null);
            borderPane.setLeft(startMenuController.getLeftMenu());

            this.resetTotalSum();
            this.clearOrder();
            this.resetOrderBox();
            rateFoodController.resetOrderedFoodList();
            rateFoodController.setRatedFoodButtonPressed(false);

            borderPane.setRight(this.getOrderBox());

            this.isModifyButtonPressed = false;
            this.placeChoice = false;
            this.isInsideButtonPressed = false;
            this.enterButtonPressed = false;
            this.finalStagePressed = false;
            this.deliveryButtonPressed = false;
            this.checkButtonPressed = false;
            this.orderIsReady = false;
        });
    }

    public void setupEnterTableButton()
    {
        this.enterTableButton.setOnAction(e-> {

            if (tableNumberField.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("EMPTY FIELD");
                alert.setHeaderText(null);
                alert.setContentText("Enter the table number!");

                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

                Label contentLabel = (Label) dialogPane.lookup(".content");
                if (contentLabel != null) {
                    contentLabel.getStyleClass().add("alert-content-text");
                }

                alert.showAndWait();
            }
            else
            {
                ImageView tickImageView = new ImageView(new Image("green-check-mark-icon-in-a-circle-tick-symbol-in-green-color-2ABNAN7.jpg"));
                tickImageView.setFitHeight(50);
                tickImageView.setFitWidth(100);

                tableNumberBox.getChildren().set(1, tickImageView);

                enterButtonPressed = true;
            }
        });
    }

    public void setupInsideButton()
    {
        insideButton.setOnAction(e -> {
            if (placeChoice)
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("EMPTY CHOICE");
                alert.setHeaderText(null);
                alert.setContentText("The place is already entered!");

                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

                Label contentLabel = (Label) dialogPane.lookup(".content");
                if (contentLabel != null) {
                    contentLabel.getStyleClass().add("alert-content-text");
                }

                alert.showAndWait();
            }
            else
            {
                ImageView tickImageView = new ImageView(new Image("green-check-mark-icon-in-a-circle-tick-symbol-in-green-color-2ABNAN7.jpg"));
                tickImageView.setFitHeight(50);
                tickImageView.setFitWidth(100);

                placeBox.getChildren().set(1, tickImageView);

                this.placeChoice = true;
                this.isInsideButtonPressed = true;
                this.deliveryButtonPressed = false;
                insideLabel.setText("inside");
            }
        });
    }

    public void setupTerraceButton()
    {
        terraceButton.setOnAction(e -> {
            if (placeChoice)
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("EMPTY CHOICE");
                alert.setHeaderText(null);
                alert.setContentText("The place is already entered!");

                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

                Label contentLabel = (Label) dialogPane.lookup(".content");
                if (contentLabel != null) {
                    contentLabel.getStyleClass().add("alert-content-text");
                }

                alert.showAndWait();
            }
            else
            {
                ImageView tickImageView = new ImageView(new Image("green-check-mark-icon-in-a-circle-tick-symbol-in-green-color-2ABNAN7.jpg"));
                tickImageView.setFitHeight(50);
                tickImageView.setFitWidth(100);

                placeBox.getChildren().set(2, tickImageView);

                this.placeChoice = true;
                this.isInsideButtonPressed = false;
                this.deliveryButtonPressed = false;
                terraceLabel.setText("terrace");
            }
        });
    }

    public void setupTableUndoButton()
    {
        this.tableUndoButton.setOnAction(e -> {
            tableNumberBox.getChildren().set(1, tableNumberField);
            enterButtonPressed = false;
            checkButtonPressed = false;
        });
    }

    public void setupPlaceUndoButton()
    {
        this.placeUndoButton.setOnAction(e -> {
            placeChoice = false;
            checkButtonPressed = false;

            if (placeBox.getChildren().get(1) != insideButton)
                placeBox.getChildren().set(1, insideButton);
            else if (placeBox.getChildren().get(2) != terraceButton)
                placeBox.getChildren().set(2, terraceButton);
            else if (placeBox.getChildren().get(3) != deliveryButton)
                placeBox.getChildren().set(3, deliveryButton);
        });
    }

    public void setupDeliveryButton()
    {
        this.deliveryButton.setOnAction(e -> {
            if (placeChoice)
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("EMPTY CHOICE");
                alert.setHeaderText(null);
                alert.setContentText("The place is already entered!");

                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

                Label contentLabel = (Label) dialogPane.lookup(".content");
                if (contentLabel != null) {
                    contentLabel.getStyleClass().add("alert-content-text");
                }

                alert.showAndWait();
            }
            else
            {
                deliveryStage.show();
            }
        });
    }

    public void setupDoneDeliveryButton()
    {
        this.doneButton.setOnAction(e -> {
            if (nameField.getText().isEmpty())
            {
                nameField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            }
            else if (phoneField.getText().isEmpty())
            {
                phoneField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                nameField.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
            }
            else if (addressField.getText().isEmpty())
            {
                addressField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                phoneField.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
            }
            else if (interphoneField.getText().isEmpty())
            {
                interphoneField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                addressField.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
            }
            else if (floorField.getText().isEmpty())
            {
                floorField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                interphoneField.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
            }
            else
            {
                placeChoice = true;
                deliveryButtonPressed = true;
                enterButtonPressed = true;

                ImageView tickImageView = new ImageView(new Image("green-check-mark-icon-in-a-circle-tick-symbol-in-green-color-2ABNAN7.jpg"));
                tickImageView.setFitHeight(50);
                tickImageView.setFitWidth(100);

                placeBox.getChildren().set(3, tickImageView);

                deliveryInfo.setName(nameField.getText());
                deliveryInfo.setPhone(phoneField.getText());
                deliveryInfo.setAddress(addressField.getText());
                deliveryInfo.setApartment(interphoneField.getText());
                deliveryInfo.setFloor(floorField.getText());

                deliveryStage.close();
            }
        });
    }

    public void setupCloseDeliveryButton()
    {
        this.closeButton.setOnAction(e -> {
            deliveryStage.close();
        });
    }

    public void setupCheckButton()
    {
        this.checkButton.setOnAction(e -> {

            if (deliveryButtonPressed)
            {
                System.out.println("THE ORDER IS READY!");

                ImageView tickImageView = new ImageView(new Image("tickkk.png"));
                tickImageView.setFitHeight(50);
                tickImageView.setFitWidth(50);

                checkBox.getChildren().set(1, tickImageView);

                orderIsReady = true;
            }
            else if (placeChoice && enterButtonPressed)
            {
                if (isInsideButtonPressed)
                {
                    try
                    {
                        Pair<String, Integer> key = new Pair<>(insideLabel.getText(), Integer.parseInt(tableNumberField.getText()));

                        Order order = ordersMap.get(key);

                        if (order != null) {
                            String name = order.getName();
                            if (name == null || name.equals(""))
                            {
                                System.out.println("THE ORDER IS READY!");
                                orderIsReady = true;

                                ImageView tickImageView = new ImageView(new Image("tickkk.png"));
                                tickImageView.setFitHeight(50);
                                tickImageView.setFitWidth(50);

                                checkBox.getChildren().set(1, tickImageView);
                            }
                            else
                            {
                                orderIsReady = false;
                                System.out.println("THIS KEY HAS ANOTHER ORDER!");

                                ImageView tickImageView = new ImageView(new Image("xxx.png"));
                                tickImageView.setFitHeight(50);
                                tickImageView.setFitWidth(50);

                                checkBox.getChildren().set(1, tickImageView);
                            }
                        } else {
                            System.out.println("Order not found for the key. This is unexpected based on initial conditions.");
                        }
                    }
                    catch (NumberFormatException ex)
                    {
                        System.out.println("The value is not a number!");
                    }
                }
                else
                {
                    try
                    {
                        Pair<String, Integer> key = new Pair<>(terraceLabel.getText(), Integer.parseInt(tableNumberField.getText()));

                        Order order = ordersMap.get(key);

                        if (order != null) {
                            String name = order.getName();
                            if (name == null || name.equals(""))
                            {
                                System.out.println("THE ORDER IS READY!");
                                orderIsReady = true;

                                ImageView tickImageView = new ImageView(new Image("tickkk.png"));
                                tickImageView.setFitHeight(50);
                                tickImageView.setFitWidth(50);

                                checkBox.getChildren().set(1, tickImageView);
                            }
                            else
                            {
                                orderIsReady = false;
                                System.out.println("THIS KEY HAS ANOTHER ORDER!");

                                ImageView tickImageView = new ImageView(new Image("xxx.png"));
                                tickImageView.setFitHeight(50);
                                tickImageView.setFitWidth(50);

                                checkBox.getChildren().set(1, tickImageView);
                            }
                        } else {
                            System.out.println("Order not found for the key. This is unexpected based on initial conditions.");
                        }
                    }
                    catch (NumberFormatException ex)
                    {
                        System.out.println("The value is not a number!");
                    }
                }
            }

            checkButtonPressed = true;
        });
    }

    public VBox getSubmitButtonBox() {
        return submitButtonBox;
    }

    public VBox getViewFinalOrderBox() {
        return viewFinalOrderBox;
    }

    public HBox getPayBoxWithoutBack() {
        return payBoxWithoutBack;
    }

    public VBox getFinalLabelsBox() {
        return finalLabelsBox;
    }

    public VBox getOrderBox() {
        return orderBox;
    }

    public VBox getFinalPayBox() {
        return finalPayBox;
    }

    public Button getRateFoodButton() {
        return rateFoodButton;
    }
}
