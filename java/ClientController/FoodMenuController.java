package ClientController;

import Model.Food;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.List;

public class FoodMenuController {
    private Label foodLabel;
    private Label starterFoodLabel;
    private Label mainFoodLabel;
    private Label dessertFoodLabel;
    private Button saladButton;
    private Button soupButton;
    private Button smallBitesButton;
    private Button pizzaButton;
    private Button pastaButton;
    private Button seaFoodButton;
    private Button meatButton;
    private Button cakesButton;
    private Button iceScreamButton;
    private Button otherDessertsButton;
    private Pane foodWrapper;
    private Pane starterFoodWrapper;
    private Pane mainFoodWrapper;
    private Pane dessertFoodWrapper;
    private VBox foodMenuBox;
    private String currentCategory;

    public FoodMenuController()
    {
        this.setupNames();
        this.setupLabelsDesign();
        this.setupButtonsDesign();
        this.setupStyles();

        this.foodMenuBox = new VBox(5, foodWrapper, starterFoodWrapper, saladButton, soupButton, smallBitesButton, mainFoodWrapper,
                            meatButton, pastaButton, seaFoodButton, pizzaButton, dessertFoodWrapper, cakesButton, iceScreamButton, otherDessertsButton);
    }

    public void setupNames()
    {
        this.foodLabel = new Label("FOOD");
        this.starterFoodLabel = new Label("STARTER FOOD");
        this.mainFoodLabel = new Label("MAIN FOOD");
        this.dessertFoodLabel = new Label("DESSERT FOOD");
        this.saladButton = new Button("SALAD");
        this.soupButton = new Button("SOUP");
        this.smallBitesButton = new Button("SMALL BITES");
        this.meatButton = new Button("MEAT");
        this.pizzaButton = new Button("PIZZA");
        this.pastaButton = new Button("PASTA");
        this.seaFoodButton = new Button("SEAFOOD");
        this.cakesButton = new Button("CAKES");
        this.iceScreamButton = new Button("ICE SCREAM");
        this.otherDessertsButton = new Button("OTHER DESSERTS");
        this.currentCategory = "";
    }

    public void setupLabelsDesign()
    {
        foodWrapper = new Pane(foodLabel);
        starterFoodWrapper = new Pane(starterFoodLabel);
        mainFoodWrapper = new Pane(mainFoodLabel);
        dessertFoodWrapper = new Pane(dessertFoodLabel);

        foodWrapper.setPrefHeight(50);
        starterFoodWrapper.setPrefHeight(30);
        mainFoodWrapper.setPrefHeight(30);
        dessertFoodWrapper.setPrefHeight(30);

        {
            foodWrapper.setStyle("-fx-background-color: #b833ff;");
            starterFoodWrapper.setStyle("-fx-background-color: #b833ff;");
            mainFoodWrapper.setStyle("-fx-background-color: #b833ff;");
            dessertFoodWrapper.setStyle("-fx-background-color: #b833ff;");
        }

        foodLabel.layoutXProperty().bind(foodWrapper.widthProperty().subtract(foodLabel.widthProperty()).divide(2));
        foodLabel.layoutYProperty().bind(foodWrapper.heightProperty().subtract(foodLabel.heightProperty()).divide(2));

        starterFoodLabel.layoutXProperty().bind(starterFoodWrapper.widthProperty().subtract(starterFoodLabel.widthProperty()).divide(2));
        starterFoodLabel.layoutYProperty().bind(starterFoodWrapper.heightProperty().subtract(starterFoodLabel.heightProperty()).divide(2));

        mainFoodLabel.layoutXProperty().bind(mainFoodWrapper.widthProperty().subtract(mainFoodLabel.widthProperty()).divide(2));
        mainFoodLabel.layoutYProperty().bind(mainFoodWrapper.heightProperty().subtract(mainFoodLabel.heightProperty()).divide(2));

        dessertFoodLabel.layoutXProperty().bind(dessertFoodWrapper.widthProperty().subtract(dessertFoodLabel.widthProperty()).divide(2));
        dessertFoodLabel.layoutYProperty().bind(dessertFoodWrapper.heightProperty().subtract(dessertFoodLabel.heightProperty()).divide(2));
    }

    public void setupButtonsDesign()
    {
        saladButton.setPrefWidth(240);
        saladButton.setPrefHeight(40);
        soupButton.setMaxWidth(Double.MAX_VALUE);
        soupButton.setPrefHeight(40);
        smallBitesButton.setMaxWidth(Double.MAX_VALUE);
        smallBitesButton.setMaxHeight(40);
        meatButton.setMaxWidth(Double.MAX_VALUE);
        meatButton.setPrefHeight(40);
        pastaButton.setMaxWidth(Double.MAX_VALUE);
        pastaButton.setPrefHeight(40);
        seaFoodButton.setMaxWidth(Double.MAX_VALUE);
        seaFoodButton.setPrefHeight(40);
        pizzaButton.setMaxWidth(Double.MAX_VALUE);
        pizzaButton.setPrefHeight(40);
        cakesButton.setMaxWidth(Double.MAX_VALUE);
        cakesButton.setPrefHeight(40);
        iceScreamButton.setMaxWidth(Double.MAX_VALUE);
        iceScreamButton.setPrefHeight(40);
        otherDessertsButton.setMaxWidth(Double.MAX_VALUE);
        otherDessertsButton.setPrefHeight(40);
    }

    public void setupStyles()
    {
        this.foodLabel.getStyleClass().add("welcome-label");
        this.starterFoodLabel.getStyleClass().add("drinks-label");
        this.mainFoodLabel.getStyleClass().add("drinks-label");
        this.dessertFoodLabel.getStyleClass().add("drinks-label");
        this.saladButton.getStyleClass().add("menu-button");
        this.soupButton.getStyleClass().add("menu-button");
        this.smallBitesButton.getStyleClass().add("menu-button");
        this.meatButton.getStyleClass().add("menu-button");
        this.pastaButton.getStyleClass().add("menu-button");
        this.pizzaButton.getStyleClass().add("menu-button");
        this.seaFoodButton.getStyleClass().add("menu-button");
        this.cakesButton.getStyleClass().add("menu-button");
        this.iceScreamButton.getStyleClass().add("menu-button");
        this.otherDessertsButton.getStyleClass().add("menu-button");
    }

    public void setupSaladButton(DrinkFoodViewerController drinkFoodViewerController, List<Food> salads)
    {
        this.saladButton.setOnAction(e -> {
            drinkFoodViewerController.displayFood(salads);
            this.currentCategory = "salad";
        });
    }

    public void setupSoupButton(DrinkFoodViewerController drinkFoodViewerController, List<Food> soups)
    {
        this.soupButton.setOnAction(e -> {
            drinkFoodViewerController.displayFood(soups);
            this.currentCategory = "soup";
        });
    }

    public void setupSmallBitesButton(DrinkFoodViewerController drinkFoodViewerController, List<Food> smallBites)
    {
        this.smallBitesButton.setOnAction(e -> {
            drinkFoodViewerController.displayFood(smallBites);
            this.currentCategory = "smallBites";
        });
    }

    public void setupMeatButton(DrinkFoodViewerController drinkFoodViewerController, List<Food> meat)
    {
        this.meatButton.setOnAction(e -> {
            drinkFoodViewerController.displayFood(meat);
            this.currentCategory = "meat";
        });
    }

    public void setupPastaButton(DrinkFoodViewerController drinkFoodViewerController, List<Food> pasta)
    {
        this.pastaButton.setOnAction(e -> {
            drinkFoodViewerController.displayFood(pasta);
            this.currentCategory = "pasta";
        });
    }

    public void setupSeaFoodButton(DrinkFoodViewerController drinkFoodViewerController, List<Food> seaFood)
    {
        this.seaFoodButton.setOnAction(e -> {
            drinkFoodViewerController.displayFood(seaFood);
            this.currentCategory = "seaFood";
        });
    }

    public void setupPizzaButton(DrinkFoodViewerController drinkFoodViewerController, List<Food> pizza)
    {
        this.pizzaButton.setOnAction(e -> {
            drinkFoodViewerController.displayFood(pizza);
            this.currentCategory = "pizza";
        });
    }

    public void setupCakesButton(DrinkFoodViewerController drinkFoodViewerController, List<Food> cakes)
    {
        this.cakesButton.setOnAction(e -> {
            drinkFoodViewerController.displayFood(cakes);
            this.currentCategory = "cakes";
        });
    }

    public void setupIceScreamButton(DrinkFoodViewerController drinkFoodViewerController, List<Food> iceScream)
    {
        this.iceScreamButton.setOnAction(e -> {
            drinkFoodViewerController.displayFood(iceScream);
            this.currentCategory = "iceScream";
        });
    }

    public void setupOtherDessertsButton(DrinkFoodViewerController drinkFoodViewerController, List<Food> otherDesserts)
    {
        this.otherDessertsButton.setOnAction(e -> {
            drinkFoodViewerController.displayFood(otherDesserts);
            this.currentCategory = "otherDesserts";
        });
    }

    public String getCurrentCategory() {
        return currentCategory;
    }

    public VBox getFoodMenuBox() {
        return foodMenuBox;
    }
}
