package ClientController;

import CheckoutController.*;
import Model.*;
import Repository.*;
import SQLDataBase.*;
import Service.DrinkService;
import Service.FoodService;
import Service.IndexManager;
import Service.UserService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.*;

public class InterfaceApplication extends Application {
    @Override
    public void start(Stage stage) throws SQLException {

        DrinkDataBase drinksDataBase = new DrinkDataBase();

        List<Drink> beers = drinksDataBase.loadDrinks("Beers");
        List<Drink> cocktails = drinksDataBase.loadDrinks("Cocktails");
        List<Drink> coffees = drinksDataBase.loadDrinks("Coffees");
        List<Drink> juices = drinksDataBase.loadDrinks("Juices");
        List<Drink> soft_drinks = drinksDataBase.loadDrinks("SoftDrinks");
        List<Drink> teas = drinksDataBase.loadDrinks("Teas");
        List<Drink> wines = drinksDataBase.loadDrinks("Wines");
        List<Drink> strong_drinks = drinksDataBase.loadDrinks("StrongDrinks");

        drinksDataBase.close();

        FoodDataBase foodDataBase = new FoodDataBase();

        List<Food> salads =foodDataBase.loadFoods("Salad");
        List<Food> soups = foodDataBase.loadFoods("Soup");
        List<Food> smallBites = foodDataBase.loadFoods("SmallBites");
        List<Food> meat = foodDataBase.loadFoods("Meat");
        List<Food> pasta = foodDataBase.loadFoods("Pasta");
        List<Food> pizza = foodDataBase.loadFoods("Pizza");
        List<Food> seafood = foodDataBase.loadFoods("Seafood");
        List<Food> cakes = foodDataBase.loadFoods("Cakes");
        List<Food> iceScreams = foodDataBase.loadFoods("IceScream");
        List<Food> otherDesserts = foodDataBase.loadFoods("OtherDesserts");

        foodDataBase.close();

        UserDataBase userDataBase = new UserDataBase();

        List<User> users = userDataBase.loadUsers();

        userDataBase.close();

        OrderDataBase orderDataBase = new OrderDataBase();

        List<Drink> orderedDrink = new ArrayList<>();
        List<Food> orderedFood = new ArrayList<>();
        List<Order> orders = orderDataBase.loadOrders();

        Map<Pair<String, Integer>, Order> ordersMap = orderDataBase.loadOrdersToMap();

        for (int i = 1; i <= 19; i++) {
            Pair<String, Integer> insideKey = new Pair<>("inside", i);

            if (!ordersMap.containsKey(insideKey)) {
                Order order = new Order();
                order.setOrderedDrink(new ArrayList<>());
                order.setOrderedFood(new ArrayList<>());
                ordersMap.put(insideKey, order);
            }
        }

        for (int i = 1; i <= 16; i++) {
            Pair<String, Integer> terraceKey = new Pair<>("terrace", i);

            if (!ordersMap.containsKey(terraceKey)) {
                Order order = new Order();
                order.setOrderedDrink(new ArrayList<>());
                order.setOrderedFood(new ArrayList<>());
                ordersMap.put(terraceKey, order);
            }
        }

        /*
        for (Map.Entry<Pair<String, Integer>, Order> entry : ordersMap.entrySet()) {
            Pair<String, Integer> key = entry.getKey();
            Order value = entry.getValue();

            System.out.println("Key: (" + key.getKey() + ", " + key.getValue() + "), Value: " + value);
        }
        */

        orderDataBase.close();

        PaymentDataBase paymentDataBase = new PaymentDataBase();

        List<Payment> payments = paymentDataBase.loadPayments();

        paymentDataBase.close();

        OrderHistoryDataBase orderHistoryDataBase = new OrderHistoryDataBase();

        List<Order> ordersHistory = orderHistoryDataBase.loadOrders();

        orderHistoryDataBase.close();

        PayOrdersDataBase payOrdersDataBase = new PayOrdersDataBase();

        Map<Pair<String, Integer>, List<Object>> payObjectsMap = payOrdersDataBase.loadPayOrdersToMap();

        payOrdersDataBase.close();

        DeliveryOrderDataBase deliveryOrderDataBase = new DeliveryOrderDataBase();

        List<Order> deliveryOrders = deliveryOrderDataBase.loadDeliveryOrders();

        deliveryOrderDataBase.close();

        OthersDataBase othersDataBase = new OthersDataBase();

        List<Order> restoreOrderItems = othersDataBase.loadRestoreOrders();

        othersDataBase.close();

        for (int i = 1; i <= 19; i++) {
            Pair<String, Integer> insideKey = new Pair<>("inside", i);
            if (!payObjectsMap.containsKey(insideKey))
                payObjectsMap.put(insideKey, new ArrayList<>());
        }

        for (int i = 1; i <= 18; i++) {
            Pair<String, Integer> terraceKey = new Pair<>("terrace", i);
            if (!payObjectsMap.containsKey(terraceKey))
                payObjectsMap.put(terraceKey, new ArrayList<>());
        }

        /*
        for (Map.Entry<Pair<String, Integer>, List<Object>> entry : payObjectsMap.entrySet()) {
            Pair<String, Integer> key = entry.getKey();
            List<Object> values = entry.getValue();

            System.out.print("Key: (" + key.getKey() + ", " + key.getValue() + "), Values: [");
            for (int i = 0; i < values.size(); i++) {
                Object obj = values.get(i);
                System.out.print(obj); // This will use the toString method of the object
                if (i < values.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");
        }
        */

        List<Order> paidPrices = new ArrayList<>();

        List<Drink> allDrinksToSearch = new ArrayList<>();

        allDrinksToSearch.addAll(coffees);
        allDrinksToSearch.addAll(beers);
        allDrinksToSearch.addAll(juices);
        allDrinksToSearch.addAll(soft_drinks);
        allDrinksToSearch.addAll(strong_drinks);
        allDrinksToSearch.addAll(teas);
        allDrinksToSearch.addAll(wines);
        allDrinksToSearch.addAll(cocktails);

        List<Food> allFoodToSearch = new ArrayList<>();

        allFoodToSearch.addAll(salads);
        allFoodToSearch.addAll(soups);
        allFoodToSearch.addAll(smallBites);
        allFoodToSearch.addAll(meat);
        allFoodToSearch.addAll(pasta);
        allFoodToSearch.addAll(seafood);
        allFoodToSearch.addAll(pizza);
        allFoodToSearch.addAll(cakes);
        allFoodToSearch.addAll(iceScreams);
        allFoodToSearch.addAll(otherDesserts);

        IndexManager indexManager = new IndexManager();

        AlcoholicDrinkRepository alcoholicDrinkRepository = new AlcoholicDrinkRepository(beers, wines, cocktails, strong_drinks);
        NonAlcoholicDrinkRepository nonAlcoholicDrinkRepository = new NonAlcoholicDrinkRepository(coffees, teas, juices, soft_drinks);
        DessertFoodRepository dessertFoodRepository = new DessertFoodRepository(cakes, iceScreams, otherDesserts);
        MainFoodRepository mainFoodRepository = new MainFoodRepository(meat, pasta, seafood, pizza);
        StarterFoodRepository starterFoodRepository = new StarterFoodRepository(salads, soups, smallBites);
        UserRepository userRepository = new UserRepository(users);

        DrinkService drinkService = new DrinkService(alcoholicDrinkRepository, nonAlcoholicDrinkRepository, drinksDataBase);
        FoodService foodService = new FoodService(starterFoodRepository, mainFoodRepository, dessertFoodRepository, foodDataBase);
        UserService userService = new UserService(userRepository, userDataBase);

        AuthAndSwitchController authAndSwitchController = new AuthAndSwitchController(users);

        StartMenuController startMenuController = new StartMenuController();
        startMenuController.setupStartMenu(authAndSwitchController);

        YourOrderViewController yourOrderViewController = new YourOrderViewController(orderedDrink, orderedFood, orderDataBase, orderHistoryDataBase, indexManager, deliveryOrders, deliveryOrderDataBase, orders, ordersHistory, ordersMap);

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(startMenuController.getLeftMenu());
        borderPane.setRight(yourOrderViewController.getOrderBox());

        DrinkMenuController drinkMenuController = new DrinkMenuController();
        FoodMenuController foodMenuController = new FoodMenuController();
        RateFoodController rateFoodController = new RateFoodController(yourOrderViewController);
        DrinkFoodViewerController drinkDrinkFoodViewerController = new DrinkFoodViewerController(yourOrderViewController, drinkMenuController, foodMenuController, startMenuController, rateFoodController, orderedDrink, orderedFood);
        DrinkFoodViewerController foodDrinkFoodViewerController = new DrinkFoodViewerController(yourOrderViewController, drinkMenuController, foodMenuController, startMenuController, rateFoodController, orderedDrink, orderedFood);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////

        CRUDOperationsController crudOperations = new CRUDOperationsController(drinkService, foodService);
        UserCRUDOperationsController userCRUDOperationsController = new UserCRUDOperationsController(userService);
        LiveOrdersController liveOrdersController = new LiveOrdersController(orders, orderDataBase, payments, paymentDataBase, paidPrices, othersDataBase, restoreOrderItems, ordersMap);
        ViewerController viewerController = new ViewerController(ordersHistory, users, coffees, juices, soft_drinks, teas, beers, wines, cocktails, strong_drinks, salads, soups, smallBites, meat, pasta, pizza, seafood, cakes, iceScreams, otherDesserts, payments);
        RestaurantController restaurantController = new RestaurantController(payments, ordersMap, orderDataBase, payObjectsMap, orderHistoryDataBase, allDrinksToSearch, allFoodToSearch, indexManager, orderedDrink, orderedFood, payOrdersDataBase, paymentDataBase, deliveryOrders, deliveryOrderDataBase, authAndSwitchController, viewerController, paidPrices, liveOrdersController, orders, ordersHistory);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        HBox drinksActionBox = new HBox(10, drinkMenuController.getDrinksMenuBox(), drinkDrinkFoodViewerController.getMiddleBox());
        HBox foodActionBox = new HBox(10, foodMenuController.getFoodMenuBox(), foodDrinkFoodViewerController.getMiddleBox());

        startMenuController.setupDrinksButton(borderPane, drinksActionBox, yourOrderViewController, drinkDrinkFoodViewerController);
        startMenuController.setupFoodButton(borderPane, foodActionBox, yourOrderViewController, foodDrinkFoodViewerController);

        drinkMenuController.setupBeersButton(drinkDrinkFoodViewerController, beers);
        drinkMenuController.setupWinesButton(drinkDrinkFoodViewerController, wines);
        drinkMenuController.setupCocktailsButton(drinkDrinkFoodViewerController, cocktails);
        drinkMenuController.setupStrongDrinksButton(drinkDrinkFoodViewerController, strong_drinks);
        drinkMenuController.setupCoffeesButton(drinkDrinkFoodViewerController, coffees);
        drinkMenuController.setupTeasButton(drinkDrinkFoodViewerController, teas);
        drinkMenuController.setupJuicesButton(drinkDrinkFoodViewerController, juices);
        drinkMenuController.setupSoftDrinksButton(drinkDrinkFoodViewerController, soft_drinks);

        foodMenuController.setupSaladButton(foodDrinkFoodViewerController, salads);
        foodMenuController.setupSoupButton(foodDrinkFoodViewerController, soups);
        foodMenuController.setupSmallBitesButton(foodDrinkFoodViewerController, smallBites);
        foodMenuController.setupMeatButton(foodDrinkFoodViewerController, meat);
        foodMenuController.setupPastaButton(foodDrinkFoodViewerController, pasta);
        foodMenuController.setupSeaFoodButton(foodDrinkFoodViewerController, seafood);
        foodMenuController.setupPizzaButton(foodDrinkFoodViewerController, pizza);
        foodMenuController.setupCakesButton(foodDrinkFoodViewerController, cakes);
        foodMenuController.setupIceScreamButton(foodDrinkFoodViewerController, iceScreams);
        foodMenuController.setupOtherDessertsButton(foodDrinkFoodViewerController, otherDesserts);

        drinkDrinkFoodViewerController.setupAddButton(true);
        foodDrinkFoodViewerController.setupAddButton(false);

        drinkDrinkFoodViewerController.setupSearchButton(beers, wines, cocktails, strong_drinks, coffees, teas, juices, soft_drinks,
                                                        salads, soups, smallBites, meat, pasta, seafood, pizza, cakes, iceScreams, otherDesserts);
        foodDrinkFoodViewerController.setupSearchButton(beers, wines, cocktails, strong_drinks, coffees, teas, juices, soft_drinks,
                                                        salads, soups, smallBites, meat, pasta, seafood, pizza, cakes, iceScreams, otherDesserts);

        drinkDrinkFoodViewerController.setupUndoButton(borderPane, yourOrderViewController);
        foodDrinkFoodViewerController.setupUndoButton(borderPane, yourOrderViewController);

        yourOrderViewController.setupRemoveButton(rateFoodController);
        yourOrderViewController.setupPlaceOrderButton(borderPane, rateFoodController);
        yourOrderViewController.setupBackButton(borderPane, drinksActionBox, foodActionBox, startMenuController);
        yourOrderViewController.setupCashButton(borderPane);
        yourOrderViewController.setupCardButton(borderPane);
        yourOrderViewController.setupModifyPaymentMethod(borderPane);
        yourOrderViewController.setupViewOrderButton(borderPane);
        yourOrderViewController.setupOkButton(borderPane);
        yourOrderViewController.setupModifyButton(borderPane, startMenuController);
        yourOrderViewController.setupSubmitButton(borderPane, viewerController, restaurantController, liveOrdersController);
        yourOrderViewController.setupBackToMenuButton(borderPane, startMenuController, rateFoodController);
        yourOrderViewController.setupEnterTableButton();
        yourOrderViewController.setupInsideButton();
        yourOrderViewController.setupTerraceButton();
        yourOrderViewController.setupPlaceUndoButton();
        yourOrderViewController.setupTableUndoButton();
        yourOrderViewController.setupDeliveryButton();
        yourOrderViewController.setupDoneDeliveryButton();
        yourOrderViewController.setupCloseDeliveryButton();
        yourOrderViewController.setupCheckButton();

        rateFoodController.setupRateFoodButton(borderPane, yourOrderViewController);
        rateFoodController.setupBackButton(borderPane);

        borderPane.setStyle("-fx-background-color: #01fdcf;");

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        authAndSwitchController.setupSwitchButton(borderPane);
        authAndSwitchController.setupSwitchToClientInterfaceButton(borderPane, startMenuController, yourOrderViewController);
        authAndSwitchController.setupLoginButton(borderPane, crudOperations, userCRUDOperationsController, liveOrdersController, viewerController, restaurantController);
        authAndSwitchController.setupSwitchToClientInterfaceButton2(borderPane, startMenuController, yourOrderViewController);

        crudOperations.setupAddButton();
        crudOperations.setupRemoveButton();
        crudOperations.setupUpdateButton();
        crudOperations.setupFindButton();
        crudOperations.setupAddDrinkButton();
        crudOperations.setupAddFoodButton();
        crudOperations.setupRemoveDrinkButton();
        crudOperations.setupRemoveFoodButton();
        crudOperations.setupUpdateDrinkButton();
        crudOperations.setupUpdateFoodButton();
        crudOperations.setupFindDrinkButton();
        crudOperations.setupFindFoodButton();

        userCRUDOperationsController.setupAddUserButton(viewerController);
        userCRUDOperationsController.setupRemoveUserButton(viewerController);
        userCRUDOperationsController.setupUpdateUserButton(viewerController);
        userCRUDOperationsController.setupFindUserButton();

        liveOrdersController.setupOpenButton();
        liveOrdersController.setupDeleteButton();
        liveOrdersController.setupRestoreButton();
        liveOrdersController.setupFinishedButton();
        liveOrdersController.setupNotFinishedButton();
        liveOrdersController.setupRefreshButton();
        liveOrdersController.setupPaidButton(viewerController, restaurantController);
        liveOrdersController.setupNotPaidButton(viewerController, restaurantController);
        liveOrdersController.setupSetTimeButton();
        liveOrdersController.setupAddRemoveTimeButton();

        viewerController.setupAllOrdersButton();
        viewerController.setupAllUsersButton();
        viewerController.setupAllDrinksButton();
        viewerController.setupAllFoodButton();
        viewerController.setupAllPaymentsButton();

        restaurantController.setupTotalAmountButton();
        restaurantController.setupInsideButton();
        restaurantController.setupTerraceButton();
        restaurantController.setupDeliveryButton();

        Scene scene = new Scene(borderPane, 1200, 720);
        scene.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());
        stage.setTitle("CLIENT APPLICATION");
        stage.setScene(scene);
        stage.show();

        double initialWidth = stage.getWidth();
        double initialHeight = stage.getHeight();

        stage.setMinWidth(initialWidth);
        stage.setMinHeight(initialHeight);
        stage.setMaxHeight(initialHeight);
        stage.setMaxWidth(initialWidth);
    }

    public static void main(String[] args) {
        launch();
    }
}