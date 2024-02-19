package CheckoutController;

import Model.Drink;
import Model.Food;
import Model.Order;
import Model.Payment;
import SQLDataBase.OrderDataBase;
import SQLDataBase.OthersDataBase;
import SQLDataBase.PaymentDataBase;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.nio.channels.FileLock;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.Handler;

public class LiveOrdersController {
    private VBox allComponentsBox;
    private VBox buttonsBox;
    private VBox buttonsBox2;
    private VBox priceAndOrdersBox;
    private HBox buttonsAndOrdersBox;
    private HBox priceBox;
    private Button deleteButton;
    private Button restoreButton;
    private Button openButton;
    private Button addRemoveTimeButton;
    private Button finishedButton;
    private Button notFinishedButton;
    private Button setTimeButton;
    private Button refreshButton;
    private Button setPaidButton;
    private Button setNotPaidButton;
    private Label liveOrdersLabel;
    private Label totalSumLabel;
    private Label sumPaidLabel;
    private LiveOrdersPane liveOrdersPane;
    private List<Order> orders;
    private OrderDataBase orderDataBase;
    private List<Order> restoredOrder;
    private float paidSum;
    private boolean isOpened;
    private List<Payment> payments;
    private PaymentDataBase paymentDataBase;
    private List<Order> paidPrices;
    private float sum;
    private OthersDataBase othersDataBase;
    private List<Order> restoreOrderItems;
    private Map<Pair<String, Integer>, Order> ordersMap;

    public LiveOrdersController(List<Order> orders, OrderDataBase orderDataBase, List<Payment> payments, PaymentDataBase paymentDataBase, List<Order> paidPrices, OthersDataBase othersDataBase, List<Order> restoreOrderItems, Map<Pair<String, Integer>, Order> ordersMap) {

        this.orders = orders;

        this.liveOrdersPane = new LiveOrdersPane(this.orders);
        this.orderDataBase = orderDataBase;
        this.restoredOrder = new ArrayList<>();
        this.paymentDataBase = paymentDataBase;
        this.payments = payments;
        this.paidPrices = paidPrices;
        this.othersDataBase = othersDataBase;
        this.restoreOrderItems = restoreOrderItems;
        this.ordersMap = ordersMap;

        this.paidSum = 0;
        this.sum = 0;
        this.isOpened = false;

        this.createComponents();
        this.setupPriceBox();
        this.setupPriceAndOrdersBox();
        this.setupButtonsBox();
        this.setupButtonsAndOrdersBox();
        this.setupAllComponentsBox();
        this.setButtonsGraphic();
        this.setupToolTipsForButtons();
    }

    public class LiveOrdersPane {
        private StackPane stackPane;
        private TableView ordersList;
        private VBox detaliedOrderBox;
        private Button undoButton;
        private List<Order> orders;
        private ScrollPane scrollPaneForDetails;
        private TableColumn<Order, Number> idColumn = new TableColumn<>("ID");
        private TableColumn<Order, String> orderColumn = new TableColumn<>("Order");
        private TableColumn<Order, Number> priceColumn = new TableColumn<>("Price");
        private HBox timerBox;
        private Label hourLabel;
        private Label labelBetween;
        private Label minuteLabel;
        private Label labelBetween2;
        private Label secondLabel;

        public LiveOrdersPane(List<Order> orders)
        {
            stackPane = new StackPane();
            ordersList = new TableView();
            detaliedOrderBox = new VBox(5);
            undoButton = new Button();
            this.orders = orders;

            scrollPaneForDetails = new ScrollPane();
            scrollPaneForDetails.setContent(detaliedOrderBox);
            scrollPaneForDetails.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPaneForDetails.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPaneForDetails.setFitToWidth(true);
            scrollPaneForDetails.setPrefSize(415, 265);
            scrollPaneForDetails.setStyle("-fx-background-color: #ffffff;");

            this.setupTableView();
            this.setupUndoButton();
            this.setupTimerBox();
        }

        public void setupTableView()
        {
            ObservableList<Order> data = FXCollections.observableArrayList(orders);

            idColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));

            orderColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getName()));

            priceColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPrice()));

            idColumn.setStyle("-fx-alignment: CENTER;");
            ordersList.getColumns().add(idColumn);
            orderColumn.setStyle("-fx-alignment: CENTER;");
            ordersList.getColumns().add(orderColumn);
            priceColumn.setStyle("-fx-alignment: CENTER;");
            ordersList.getColumns().add(priceColumn);

            idColumn.setPrefWidth(100);
            orderColumn.setPrefWidth(216);
            priceColumn.setPrefWidth(100);

            this.setupCells();

            ordersList.setItems(data);

            ordersList.getStylesheets().add("startMenu.css");

            stackPane.getChildren().add(ordersList);
        }

        public void setupCells()
        {
            idColumn.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(Number item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(String.valueOf(item.intValue()));
                        getStyleClass().add("orders-label");
                        Order order = getTableRow().getItem();

                        if (order != null && order.isFinished())
                            setStyle("-fx-background-color: green; -fx-alignment: center;");
                        else
                            setStyle("-fx-background-color: red; -fx-alignment: center;");
                    }
                }
            });

            orderColumn.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        getStyleClass().add("orders-label");
                        Order order = getTableRow().getItem();

                        if (order != null && order.isFinished())
                            setStyle("-fx-background-color: green; -fx-alignment: center;");
                        else
                            setStyle("-fx-background-color: red; -fx-alignment: center;");
                    }
                }
            });

            priceColumn.setCellFactory(column -> new TableCell<Order, Number>() {
                @Override
                protected void updateItem(Number item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.intValue() + " LEI");
                        getStyleClass().add("orders-label");
                        Order order = getTableRow().getItem();

                        if (order != null && order.isFinished())
                            setStyle("-fx-background-color: green; -fx-alignment: center;");
                        else
                            setStyle("-fx-background-color: red; -fx-alignment: center;");

                        if (order != null && order.isPaid())
                            setText("PAID");
                        else
                            setText(item.intValue() + " LEI");
                    }
                }
            });
        }

        public void setupDetaliedOrderBox(Order selectedOrder)
        {
            detaliedOrderBox.setSpacing(5);

            Label titleLabel = new Label("ORDER INFO");
            Pane titleWrapper = new Pane(titleLabel);

            titleWrapper.setPrefSize(225, 30);
            //274

            titleLabel.layoutXProperty().bind(titleWrapper.widthProperty().subtract(titleLabel.widthProperty()).divide(2));

            {
                titleLabel.getStyleClass().add("drinks-label-color");
                titleWrapper.setStyle("-fx-background-color: #df00fe;");
            }

            undoButton.getStyleClass().add("pay-button");
            undoButton.setPrefSize(50, 35);
            // 420 * 20

            Image undoImage = new Image("undo-6.PNG");
            ImageView imageView  = new ImageView(undoImage);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);

            undoButton.setGraphic(imageView);

            HBox buttonBox = new HBox();
            buttonBox.setAlignment(Pos.BOTTOM_CENTER);
            buttonBox.getChildren().add(undoButton);

            //this.setupTimerBox();

            HBox titleAndTimerBox = new HBox(titleWrapper, timerBox, undoButton);

            detaliedOrderBox.getChildren().add(titleAndTimerBox);

            if (selectedOrder != null)
            {
                for (int i = 0; i < orders.size(); ++i)
                {
                    if (selectedOrder.getName().equals(orders.get(i).getName()))
                    {
                        for (int j = 0; j < orders.get(i).getOrderedDrink().size(); ++j)
                        {
                            Label drinkLabel = new Label("Drink");

                            Label nameLabel = new Label("Name: " + orders.get(i).getOrderedDrink().get(j).getName());
                            Label volumeLabel  = new Label("Volume: " + orders.get(i).getOrderedDrink().get(j).getVolume() + " Ml");
                            Label priceLabel = new Label("Price: " + orders.get(i).getOrderedDrink().get(j).getPrice() + " Lei");

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

                            detaliedOrderBox.getChildren().add(wrapper);
                        }

                        for (int j = 0; j < orders.get(i).getOrderedFood().size(); ++j)
                        {
                            Label drinkLabel = new Label("Food");

                            Label nameLabel = new Label("Name: " + orders.get(i).getOrderedFood().get(j).getName());
                            Label gramsLabel  = new Label("Grams: " + orders.get(i).getOrderedFood().get(j).getGrams() + " Grams");
                            Label priceLabel = new Label("Price: " + orders.get(i).getOrderedFood().get(j).getPrice() + " Lei");
                            Label ratingLabel = new Label("Rating: " + orders.get(i).getOrderedFood().get(j).getRating() + "☆");

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

                            detaliedOrderBox.getChildren().add(wrapper);
                        }
                    }
                }
            }

            /*
            if (detaliedOrderBox.getHeight() < 265)
            {
                Region region = new Region();
                region.setPrefHeight(50);
                detaliedOrderBox.getChildren().addAll(region, buttonBox);
            }
            else
                detaliedOrderBox.getChildren().add(buttonBox);
            //detaliedOrderBox.setAlignment(Pos.BOTTOM_CENTER);
            */
            VBox.setVgrow(buttonBox, Priority.ALWAYS);
        }

        public void setupClearDetailedOrderBox()
        {
            detaliedOrderBox.setSpacing(0);

            Label titleLabel = new Label("ORDER INFO");
            Pane titleWrapper = new Pane(titleLabel);

            titleWrapper.setPrefSize(225, 30);
            //274

            titleLabel.layoutXProperty().bind(titleWrapper.widthProperty().subtract(titleLabel.widthProperty()).divide(2));

            {
                titleLabel.getStyleClass().add("drinks-label-color");
                titleWrapper.setStyle("-fx-background-color: #df00fe;");
            }

            undoButton.getStyleClass().add("pay-button");
            undoButton.setPrefSize(50, 35);
            // 420 * 20

            Image undoImage = new Image("undo-6.PNG");
            ImageView imageView  = new ImageView(undoImage);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);

            undoButton.setGraphic(imageView);

            HBox buttonBox = new HBox();
            buttonBox.setAlignment(Pos.BOTTOM_CENTER);
            buttonBox.getChildren().add(undoButton);

            HBox titleAndTimerBox = new HBox(titleWrapper, timerBox, undoButton);

            detaliedOrderBox.getChildren().add(titleAndTimerBox);

            Label promptLabel = new Label("The order is paid/empty");
            HBox promptBox = new HBox();

            promptBox.setPrefSize(415, 227);
            promptBox.getChildren().add(promptLabel);
            promptBox.setAlignment(Pos.CENTER);

            {
                promptLabel.getStyleClass().add("drinks-label-color");
                promptBox.setStyle("-fx-background-color: #ffffff;");
            }

            detaliedOrderBox.getChildren().add(promptBox);
        }

        public void setupTimerBox()
        {
            timerBox = new HBox(5);
            hourLabel = new Label("00");
            labelBetween = new Label(":");
            minuteLabel = new Label("00");
            labelBetween2 = new Label(":");
            secondLabel = new Label("00");

            {
                hourLabel.getStyleClass().add("timer-label");
                labelBetween.getStyleClass().add("timer-label");
                minuteLabel.getStyleClass().add("timer-label");
                labelBetween2.getStyleClass().add("timer-label");
                secondLabel.getStyleClass().add("timer-label");
            }

            timerBox.getChildren().addAll(hourLabel, labelBetween, minuteLabel, labelBetween2, secondLabel);
            timerBox.setStyle("-fx-background-color: #1201fd;");

            timerBox.setPrefHeight(30);
        }

        public void setupTimerLabels(Order selectedOrder)
        {
            if (selectedOrder.getFinishedTime().getHour() < 10)
                hourLabel.setText("0" + String.valueOf(selectedOrder.getFinishedTime().getHour()));
            else
                hourLabel.setText(String.valueOf(selectedOrder.getFinishedTime().getHour()) );

            if (selectedOrder.getFinishedTime().getMinute() < 10)
                minuteLabel.setText("0" + String.valueOf(selectedOrder.getFinishedTime().getMinute()));
            else
                minuteLabel.setText(String.valueOf(selectedOrder.getFinishedTime().getMinute()));

            if (selectedOrder.getFinishedTime().getSecond() < 10)
                secondLabel.setText("0" + String.valueOf(selectedOrder.getFinishedTime().getSecond()));
            else
                secondLabel.setText(String.valueOf(selectedOrder.getFinishedTime().getSecond()));
        }


            // Ensure the ScrollPane's layout is updated.
            public boolean isVBarVisible() {
                ScrollBar vBar = (ScrollBar) scrollPaneForDetails.lookup(".scroll-bar:vertical");
                return vBar != null && vBar.isVisible();
            }


        public void setupUndoButton()
        {
            this.undoButton.setOnAction(e -> {

                this.stackPane.getChildren().clear();
                this.stackPane.getChildren().add(ordersList);
                this.detaliedOrderBox.getChildren().clear();
                isOpened = false;
            });
        }

        public void refreshTableView()
        {
            this.setupCells();
        }

        public Label getHourLabel() {
            return hourLabel;
        }

        public Label getMinuteLabel() {
            return minuteLabel;
        }

        public Label getSecondLabel() {
            return secondLabel;
        }

        public StackPane getStackPane() {
            return stackPane;
        }

        public TableView getOrdersList() {
            return ordersList;
        }

        public VBox getDetaliedOrderBox() {
            return detaliedOrderBox;
        }

        public ScrollPane getScrollPaneForDetails() {
            return scrollPaneForDetails;
        }

        public Button getUndoButton() {
            return undoButton;
        }
    }

    public void createComponents()
    {
        allComponentsBox = new VBox(5);
        buttonsBox = new VBox(5);
        priceBox = new HBox(5);
        priceAndOrdersBox = new VBox(5);
        deleteButton = new Button();
        finishedButton = new Button();
        setTimeButton = new Button();
        setPaidButton = new Button();
        refreshButton = new Button();
        restoreButton = new Button();
        openButton = new Button();
        notFinishedButton = new Button();
        setNotPaidButton = new Button();
        addRemoveTimeButton = new Button();
        liveOrdersLabel = new Label("LIVE ORDERS");
        totalSumLabel = new Label("T: " + this.getTotalSum() + " LEI");
        sumPaidLabel = new Label("P: " + this.getPaidSum() + " LEI");
    }



    public void setupAllComponentsBox()
    {
        Pane liveOrdersWrapper = new Pane(liveOrdersLabel);

        liveOrdersLabel.layoutXProperty().bind(liveOrdersWrapper.widthProperty().subtract(liveOrdersLabel.widthProperty()).divide(2));

        {
            liveOrdersLabel.getStyleClass().add("welcome-label");
            liveOrdersWrapper.setStyle("-fx-background-color: #df00fe;");
        }

        allComponentsBox.getChildren().add(liveOrdersWrapper);
        allComponentsBox.getChildren().add(buttonsAndOrdersBox);

        allComponentsBox.setPrefWidth(550);
        allComponentsBox.setMaxHeight(385);
        allComponentsBox.setStyle("-fx-background-color: #e2ce4a;");
    }

    public void setupPriceBox()
    {
        Pane totalSumWrapper = new Pane(totalSumLabel);
        Pane sumPaidWrapper = new Pane(sumPaidLabel);

        {
            totalSumLabel.getStyleClass().add("welcome-label-v4");
            sumPaidLabel.getStyleClass().add("welcome-label-v4");
            totalSumWrapper.setStyle("-fx-background-color: #df00fe;");
            sumPaidWrapper.setStyle("-fx-background-color: #df00fe;");
        }

        this.priceBox = new HBox(5, totalSumWrapper, sumPaidWrapper);

        totalSumWrapper.setPrefSize(205, 50);
        sumPaidWrapper.setPrefSize(205, 50);

        totalSumLabel.layoutXProperty().bind(totalSumWrapper.widthProperty().subtract(totalSumLabel.widthProperty()).divide(2));
        sumPaidLabel.layoutXProperty().bind(sumPaidWrapper.widthProperty().subtract(sumPaidLabel.widthProperty()).divide(2));
    }

    public void setupPriceAndOrdersBox()
    {
        this.priceAndOrdersBox = new VBox(5);

        liveOrdersPane.getStackPane().setPrefSize(415, 265);
        liveOrdersPane.getStackPane().setStyle("-fx-background-color: #fd6201;");

        this.priceAndOrdersBox.getChildren().add(liveOrdersPane.getStackPane());
        this.priceAndOrdersBox.getChildren().add(priceBox);

        VBox.setVgrow(priceBox, Priority.ALWAYS);
        this.priceAndOrdersBox.setAlignment(Pos.BOTTOM_CENTER);
    }

    public void setupButtonsBox()
    {
        this.buttonsBox = new VBox(5, deleteButton, finishedButton, setTimeButton, refreshButton, setPaidButton);
        this.buttonsBox2 = new VBox(5, restoreButton, notFinishedButton, addRemoveTimeButton, openButton, setNotPaidButton);

        deleteButton.setPrefSize(60, 60);
        finishedButton.setPrefSize(60, 60);
        setTimeButton.setPrefSize(60, 60);
        setPaidButton.setPrefSize(60, 60);
        refreshButton.setPrefSize(60, 60);

        restoreButton.setPrefSize(60, 60);
        notFinishedButton.setPrefSize(60, 60);
        addRemoveTimeButton.setPrefSize(60, 60);
        openButton.setPrefSize(60, 60);
        setNotPaidButton.setPrefSize(60, 60);

        {
            deleteButton.getStyleClass().add("live-order-button");
            finishedButton.getStyleClass().add("live-order-button");
            setTimeButton.getStyleClass().add("live-order-button");
            setPaidButton.getStyleClass().add("live-order-button");
            refreshButton.getStyleClass().add("live-order-button");

            restoreButton.getStyleClass().add("live-order-button");
            notFinishedButton.getStyleClass().add("live-order-button");
            addRemoveTimeButton.getStyleClass().add("live-order-button");
            openButton.getStyleClass().add("live-order-button");
            setNotPaidButton.getStyleClass().add("live-order-button");
        }
    }

    public void setButtonsGraphic()
    {
        Image deleteImage = new Image("deleteImage.jpg");
        ImageView imageView = new ImageView(deleteImage);
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);

        deleteButton.setGraphic(imageView);

        Image refreshImage = new Image("refreshImage.png");
        ImageView imageView1 = new ImageView(refreshImage);
        imageView1.setFitHeight(40);
        imageView1.setFitWidth(40);

        refreshButton.setGraphic(imageView1);

        Image finishedImage = new Image("process-completed-symbolic-icon-2048x2048-baquwdk1.png");
        ImageView imageView2 = new ImageView(finishedImage);
        imageView2.setFitWidth(40);
        imageView2.setFitHeight(40);

        finishedButton.setGraphic(imageView2);

        Image timeImage = new Image("clock-time-business-money-design-vector-12235835.jpg");
        ImageView imageView3 = new ImageView(timeImage);
        imageView3.setFitWidth(40);
        imageView3.setFitHeight(40);

        setTimeButton.setGraphic(imageView3);

        Image paidImage = new Image("360_F_49064979_nbrrNFtMOFCT5YJrW5LCs8Qgtr0XM6Tz.jpg");
        ImageView imageView4  = new ImageView(paidImage);
        imageView4.setFitHeight(40);
        imageView4.setFitWidth(40);

        setPaidButton.setGraphic(imageView4);

        Image notPaidImage = new Image("notpaidimage.png");
        ImageView imageView5  = new ImageView(notPaidImage);
        imageView5.setFitHeight(40);
        imageView5.setFitWidth(40);

        setNotPaidButton.setGraphic(imageView5);

        Image openImage = new Image("openImage.png");
        ImageView imageView6  = new ImageView(openImage);
        imageView6.setFitHeight(40);
        imageView6.setFitWidth(40);

        openButton.setGraphic(imageView6);

        Image addRemoveImage = new Image("1000_F_197900139_DGXmhx4G1AVurnLyR1wVsKMn6RnBJfRN.jpg");
        ImageView imageView7  = new ImageView(addRemoveImage);
        imageView7.setFitHeight(40);
        imageView7.setFitWidth(40);

        addRemoveTimeButton.setGraphic(imageView7);

        Image restoreImage = new Image("restoreImage.png");
        ImageView imageView8  = new ImageView(restoreImage);
        imageView8.setFitHeight(40);
        imageView8.setFitWidth(40);

        restoreButton.setGraphic(imageView8);

        Image notFinishedImage = new Image("notFinishedIMG.PNG");
        ImageView imageView9  = new ImageView(notFinishedImage);
        imageView9.setFitHeight(40);
        imageView9.setFitWidth(40);

        notFinishedButton.setGraphic(imageView9);
    }

    public void setupToolTipsForButtons()
    {
        Tooltip tooltip = new Tooltip("Delete");
        tooltip.setShowDelay(Duration.seconds(0.1));
        deleteButton.setTooltip(tooltip);

        Tooltip tooltip1 = new Tooltip("Open");
        tooltip1.setShowDelay(Duration.seconds(0.1));
        openButton.setTooltip(tooltip1);

        Tooltip tooltip2 = new Tooltip("Set Paid");
        tooltip2.setShowDelay(Duration.seconds(0.1));
        setPaidButton.setTooltip(tooltip2);

        Tooltip tooltip3 = new Tooltip("Set Not Paid");
        tooltip3.setShowDelay(Duration.seconds(0.1));
        setNotPaidButton.setTooltip(tooltip3);

        Tooltip tooltip4 = new Tooltip("Restore");
        tooltip4.setShowDelay(Duration.seconds(0.1));
        restoreButton.setTooltip(tooltip4);

        Tooltip tooltip5 = new Tooltip("Set Finished");
        tooltip5.setShowDelay(Duration.seconds(0.1));
        finishedButton.setTooltip(tooltip5);

        Tooltip tooltip6 = new Tooltip("Set Not Finished");
        tooltip6.setShowDelay(Duration.seconds(0.1));
        notFinishedButton.setTooltip(tooltip6);

        Tooltip tooltip7 = new Tooltip("Refresh");
        tooltip7.setShowDelay(Duration.seconds(0.1));
        refreshButton.setTooltip(tooltip7);

        Tooltip tooltip8 = new Tooltip("Set Time");
        tooltip8.setShowDelay(Duration.seconds(0.1));
        setTimeButton.setTooltip(tooltip8);

        Tooltip tooltip9 = new Tooltip("Add/Remove Time");
        tooltip9.setShowDelay(Duration.seconds(0.1));
        addRemoveTimeButton.setTooltip(tooltip9);
    }

    public void setupButtonsAndOrdersBox()
    {
        this.buttonsAndOrdersBox = new HBox(5, priceAndOrdersBox, buttonsBox2,buttonsBox);
    }

    public String getTotalSum()
    {
        sum = 0;

        for (int i = 0; i < orders.size(); ++i)
            sum = sum + orders.get(i).getPrice();

        return String.format("%.2f", sum);
    }

    public String getPaidSum()
    {
        paidSum = 0;

        for (int i = 0; i < orders.size(); ++i)
            if (orders.get(i).isPaid())
                paidSum = paidSum + orders.get(i).getPrice();

        return String.format("%.2f", paidSum);
    }

    public void addToPaidSum(float sum)
    {
        this.paidSum += sum;
        sumPaidLabel.setText("PAID: " + paidSum);
    }

    public void lessFromPaidSum(float sum)
    {
        this.paidSum -= sum;
        sumPaidLabel.setText("PAID: " + paidSum);
    }

    public void setupOpenButton()
    {
        this.openButton.setOnAction(e -> {

            if (!isOpened)
            {
                if (liveOrdersPane.getOrdersList().getSelectionModel().getSelectedItem() != null)
                {
                    Order selectedOrder = (Order) liveOrdersPane.getOrdersList().getSelectionModel().getSelectedItem();

                    if (selectedOrder.getOrderedDrink().isEmpty() && selectedOrder.getOrderedFood().isEmpty())
                    {
                        liveOrdersPane.setupClearDetailedOrderBox();
                        liveOrdersPane.setupTimerLabels(selectedOrder);
                        liveOrdersPane.getStackPane().getChildren().clear();
                        liveOrdersPane.getStackPane().getChildren().add(liveOrdersPane.getScrollPaneForDetails());
                    }
                    else
                    {
                        liveOrdersPane.setupDetaliedOrderBox(selectedOrder);
                        liveOrdersPane.setupTimerLabels(selectedOrder);
                        liveOrdersPane.getStackPane().getChildren().clear();
                        liveOrdersPane.getStackPane().getChildren().add(liveOrdersPane.getScrollPaneForDetails());
                    }
                }
                else
                    System.out.println("Please select an order first");

                this.isOpened = true;
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("ALREADY OPENED");
                alert.setHeaderText(null);
                alert.setContentText("The order is already opened!");

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

    public void setupDeleteButton()
    {
        this.deleteButton.setOnAction(e -> {

            Order selectedOrder = (Order) liveOrdersPane.getOrdersList().getSelectionModel().getSelectedItem();

            if (selectedOrder != null && selectedOrder.isPaid())
            {
                restoredOrder.add(selectedOrder);

                liveOrdersPane.getOrdersList().getItems().remove(selectedOrder);
                orders.remove(selectedOrder);
                orderDataBase.deleteOrderFromDataBase(selectedOrder);

                this.totalSumLabel.setText("T: " + this.getTotalSum() + " LEI");
                this.sumPaidLabel.setText("P: " + this.getPaidSum() + " LEI");
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

    public void setupRestoreButton()
    {
        this.restoreButton.setOnAction(e -> {

            if (!restoredOrder.isEmpty())
            {
                Order orderToBeRestored = restoredOrder.get(0);
                liveOrdersPane.getOrdersList().getItems().add(orderToBeRestored);
                orders.add(orderToBeRestored);
                orderDataBase.addOrderToDataBase(orderToBeRestored);

                restoredOrder.clear();

                this.totalSumLabel.setText("T: " + this.getTotalSum() + " LEI");
                this.sumPaidLabel.setText("P: " + this.getPaidSum() + " LEI");
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("NO ORDER TO RESTORE");
                alert.setHeaderText(null);
                alert.setContentText("No order to be restored!");

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

    public void setupFinishedButton()
    {
        this.finishedButton.setOnAction(e -> {
            Order selectedOrder = (Order) liveOrdersPane.getOrdersList().getSelectionModel().getSelectedItem();

            if (selectedOrder != null && selectedOrder.isFinished())
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("FINISHED");
                alert.setHeaderText(null);
                alert.setContentText("The order is already finished!");

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
                selectedOrder.setFinished(true);
                orderDataBase.updateOrderFromDataBase(selectedOrder.getName(), selectedOrder);

                liveOrdersPane.refreshTableView();
            }
        });
    }

    public void setupNotFinishedButton()
    {
        this.notFinishedButton.setOnAction(e -> {
            Order selectedOrder = (Order) liveOrdersPane.getOrdersList().getSelectionModel().getSelectedItem();

            if (selectedOrder != null && !selectedOrder.isFinished())
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("NOT FINISHED");
                alert.setHeaderText(null);
                alert.setContentText("The order is not ready yet!");

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
                selectedOrder.setFinished(false);
                orderDataBase.updateOrderFromDataBase(selectedOrder.getName(), selectedOrder);

                liveOrdersPane.refreshTableView();
            }
        });
    }

    public void setupRefreshButton()
    {
        this.refreshButton.setOnAction(e -> {
            liveOrdersPane.getOrdersList().setStyle("-fx-background-color: blue;");
            new Timeline(new KeyFrame(
                    Duration.millis(500),
                    ae -> liveOrdersPane.getOrdersList().setStyle("")
            )).play();

            liveOrdersPane.getOrdersList().refresh();
        });
    }

    public void setupPaidButton(ViewerController viewerController, RestaurantController restaurantController)
    {
        this.setPaidButton.setOnAction(e -> {
            Order selectedOrder = (Order) liveOrdersPane.getOrdersList().getSelectionModel().getSelectedItem();

            if (selectedOrder != null && selectedOrder.isPaid())
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
            else
            {
                Payment payment = new Payment(selectedOrder.getName(), selectedOrder.getPrice(), selectedOrder.getPayMethod());

                paymentDataBase.addPaymentToDataBase(payment);
                payments.add(payment);

                ViewerController.PaymentViewer paymentViewer = viewerController.getPaymentViewer();
                paymentViewer.getPaymentsList().getItems().add(payment);

                selectedOrder.setPaid(true);
                orderDataBase.updateOrderFromDataBase(selectedOrder.getName(), selectedOrder);

                Order anotherOrder = selectedOrder.clone();

                othersDataBase.addOrderToDataBase(selectedOrder);
                restoreOrderItems.add(anotherOrder);

                Pair<String, Integer> key = new Pair<>(selectedOrder.getPlaceName(),selectedOrder.getTableNumber());
                Order order = ordersMap.get(key);

                order.getOrderedDrink().clear();
                order.getOrderedFood().clear();

                selectedOrder.getOrderedDrink().clear();
                selectedOrder.getOrderedFood().clear();

                orderDataBase.deleteOrderedDrinkFromDataBase(selectedOrder);
                orderDataBase.deleteOrderedFoodFromDataBase(selectedOrder);

                this.sumPaidLabel.setText("P: " + this.getPaidSum() + " LEI");

                RestaurantController.TotalAmountViewer totalAmountViewer = restaurantController.getTotalAmountViewer();

                totalAmountViewer.getTotalAmountLabel().setText("TOTAL AMOUNT: " + totalAmountViewer.getTotalAmount() + " LEI");
                totalAmountViewer.getCashLabel().setText("CASH: " + totalAmountViewer.getTotalCash() + " LEI");
                totalAmountViewer.getCardLabel().setText("CARD: " + totalAmountViewer.getTotalCard() + " LEI");

                liveOrdersPane.refreshTableView();
            }

        });
    }

    public void setupNotPaidButton(ViewerController viewerController, RestaurantController restaurantController)
    {
        this.setNotPaidButton.setOnAction(e -> {
            Order selectedOrder = (Order) liveOrdersPane.getOrdersList().getSelectionModel().getSelectedItem();

            if (selectedOrder != null && !selectedOrder.isPaid())
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
            else
            {
                this.lessFromPaidSum(selectedOrder.getPrice());

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
                orderDataBase.updateOrderFromDataBase(selectedOrder.getName(), selectedOrder);

                Pair<String, Integer> key = new Pair<>(selectedOrder.getPlaceName(),selectedOrder.getTableNumber());
                Order order = ordersMap.get(key);

                for (int i = 0; i < restoreOrderItems.size(); ++i)
                {
                    if (restoreOrderItems.get(i).getName().equals(selectedOrder.getName()))
                    {
                        order.setOrderedDrink(restoreOrderItems.get(i).getOrderedDrink());
                        order.setOrderedFood(restoreOrderItems.get(i).getOrderedFood());

                        selectedOrder.setOrderedDrink(restoreOrderItems.get(i).getOrderedDrink());
                        selectedOrder.setOrderedFood(restoreOrderItems.get(i).getOrderedFood());

                        for (int j = 0; j < restoreOrderItems.get(i).getOrderedDrink().size(); ++j)
                            orderDataBase.addSimpleDrinkToDataBase(restoreOrderItems.get(i), restoreOrderItems.get(i).getOrderedDrink().get(j));

                        for (int j = 0; j < restoreOrderItems.get(i).getOrderedFood().size(); ++j)
                            orderDataBase.addSimpleFoodToDataBase(restoreOrderItems.get(i), restoreOrderItems.get(i).getOrderedFood().get(j));
                    }
                }

                restoreOrderItems.remove(selectedOrder);
                othersDataBase.deleteOrderFromDataBase(selectedOrder);

                this.sumPaidLabel.setText("P: " + this.getPaidSum() + " LEI");

                RestaurantController.TotalAmountViewer totalAmountViewer = restaurantController.getTotalAmountViewer();

                totalAmountViewer.getTotalAmountLabel().setText("TOTAL AMOUNT: " + totalAmountViewer.getTotalAmount() + " LEI");
                totalAmountViewer.getCashLabel().setText("CASH: " + totalAmountViewer.getTotalCash() + " LEI");
                totalAmountViewer.getCardLabel().setText("CARD: " + totalAmountViewer.getTotalCard() + " LEI");

                liveOrdersPane.refreshTableView();
            }
        });
    }

    public void setupSetTimeButton()
    {
        this.setTimeButton.setOnAction(e -> {
            Order selectedOrder = (Order) liveOrdersPane.getOrdersList().getSelectionModel().getSelectedItem();

            if (selectedOrder != null)
            {
                Stage timeStage = new Stage();
                timeStage.setTitle("SET TIME");

                TextField hourField = new TextField();
                hourField.setPromptText("hh");
                TextField minuteField = new TextField();
                minuteField.setPromptText("mm");
                TextField secondField = new TextField();
                secondField.setPromptText("ss");

                hourField.setStyle("-fx-font-size: 18px;");
                minuteField.setStyle("-fx-font-size: 18px;");
                secondField.setStyle("-fx-font-size: 18px;");

                hourField.setPrefSize(100, 50);
                minuteField.setPrefSize(100, 50);
                secondField.setPrefSize(100, 50);

                Button setButton = new Button("Set");
                Button cancelButton = new Button("Cancel");

                setButton.setPrefSize(152, 50);
                cancelButton.setPrefSize(152, 50);

                {
                    setButton.getStyleClass().add("interface-button");
                    cancelButton.getStyleClass().add("interface-button");
                }

                HBox fieldsBox = new HBox(5, hourField, minuteField, secondField);
                HBox buttonsBox = new HBox(6, cancelButton, setButton);
                VBox layoutBox = new VBox(5,fieldsBox, buttonsBox);

                cancelButton.setOnAction(ev -> {
                    timeStage.close();
                });

                setButton.setOnAction(ev -> {

                    try
                    {
                        int hours = Integer.parseInt(hourField.getText());
                        int minutes = Integer.parseInt(minuteField.getText());
                        int seconds = Integer.parseInt(secondField.getText());

                        LocalTime localTime = LocalTime.of(hours, minutes, seconds);
                        selectedOrder.setFinishedTime(localTime);

                        orderDataBase.updateOrderFromDataBase(selectedOrder.getName(), selectedOrder);
                    }
                    catch (NumberFormatException ex)
                    {
                        System.out.println("The value is not a number!");
                    }

                    timeStage.close();
                });

                Scene scene = new Scene(layoutBox);

                scene.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());
                timeStage.setScene(scene);
                timeStage.show();
            }
        });
    }

    public void setupAddRemoveTimeButton()
    {
        this.addRemoveTimeButton.setOnAction(e -> {
            Order selectedOrder = (Order) liveOrdersPane.getOrdersList().getSelectionModel().getSelectedItem();

            if (selectedOrder != null)
            {
                Stage timeStage = new Stage();
                timeStage.setTitle("SET TIME");

                TextField hourField = new TextField();
                hourField.setPromptText("hh");
                TextField minuteField = new TextField();
                minuteField.setPromptText("mm");
                TextField secondField = new TextField();
                secondField.setPromptText("ss");

                hourField.setStyle("-fx-font-size: 18px;");
                minuteField.setStyle("-fx-font-size: 18px;");
                secondField.setStyle("-fx-font-size: 18px;");

                hourField.setPrefSize(100, 50);
                minuteField.setPrefSize(100, 50);
                secondField.setPrefSize(100, 50);

                Button plusButton = new Button("Plus");
                Button minusButton = new Button("Minus");
                Button cancelButton = new Button("Cancel");

                plusButton.setPrefSize(100, 50);
                minusButton.setPrefSize(100, 50);
                cancelButton.setPrefSize(100, 50);

                {
                    plusButton.getStyleClass().add("interface-button");
                    minusButton.getStyleClass().add("interface-button");
                    cancelButton.getStyleClass().add("interface-button");
                }

                HBox fieldsBox = new HBox(5, hourField, minuteField, secondField);
                HBox buttonsBox = new HBox(5, cancelButton, minusButton, plusButton);
                VBox layoutBox = new VBox(5,fieldsBox, buttonsBox);

                cancelButton.setOnAction(ev -> {
                    timeStage.close();
                });

                plusButton.setOnAction(ev -> {

                    try
                    {
                        int hours = Integer.parseInt(hourField.getText());
                        int minutes = Integer.parseInt(minuteField.getText());
                        int seconds = Integer.parseInt(secondField.getText());

                        LocalTime updatedTime = selectedOrder.getFinishedTime()
                                .plusHours(hours)
                                .plusMinutes(minutes)
                                .plusSeconds(seconds);

                        selectedOrder.setFinishedTime(updatedTime);

                        orderDataBase.updateOrderFromDataBase(selectedOrder.getName(), selectedOrder);
                    }
                    catch (NumberFormatException ex)
                    {
                        System.out.println("The value is not a number!");
                    }

                    timeStage.close();
                });

                minusButton.setOnAction(ev -> {

                    try
                    {
                        int hours = Integer.parseInt(hourField.getText());
                        int minutes = Integer.parseInt(minuteField.getText());
                        int seconds = Integer.parseInt(secondField.getText());

                        LocalTime updatedTime = selectedOrder.getFinishedTime()
                                .minusHours(hours)
                                .minusMinutes(minutes)
                                .minusSeconds(seconds);

                        selectedOrder.setFinishedTime(updatedTime);
                    }
                    catch (NumberFormatException ex)
                    {
                        System.out.println("The value is not a number!");
                    }

                    timeStage.close();
                });

                Scene scene = new Scene(layoutBox);

                scene.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());
                timeStage.setScene(scene);
                timeStage.show();
            }
        });
    }

    public VBox getAllComponentsBox() {
        return allComponentsBox;
    }

    public LiveOrdersPane getLiveOrdersPane() {
        return liveOrdersPane;
    }
}
