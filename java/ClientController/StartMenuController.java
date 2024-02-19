package ClientController;

import CheckoutController.AuthAndSwitchController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import javax.xml.stream.events.Namespace;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class StartMenuController {
    private Label welcomeLabel;
    private Button drinksButton;
    private Button foodButton;
    private VBox leftMenu;
    private String currentContext;

    public StartMenuController()
    {
        this.welcomeLabel = new Label("WELCOME!");
        this.drinksButton = new Button("DRINKS");
        this.foodButton = new Button("FOOD");
        this.currentContext = "";
    }

    public void setupStartMenu(AuthAndSwitchController authAndSwitchController)
    {
        {
            welcomeLabel.getStyleClass().add("welcome-label");
            welcomeLabel.setStyle("-fx-font-size: 100px;");

            drinksButton.getStyleClass().add("menu-button");
            foodButton.getStyleClass().add("menu-button");
        }

        drinksButton.setPrefSize(340, 340);
        foodButton.setPrefSize(340, 340);

        HBox menuButtons = new HBox(40, drinksButton, foodButton);
        HBox.setMargin(drinksButton, new Insets(0, 0, 0, 40));
        menuButtons.setAlignment(Pos.CENTER);

        Region spacer = new Region();
        spacer.setPrefHeight(30);

        Region region = new Region();
        region.setPrefWidth(35);

        HBox swtichBox = new HBox(region, authAndSwitchController.getSwtichMenuButton());
        swtichBox.setAlignment(Pos.CENTER);

        leftMenu = new VBox(20, welcomeLabel, menuButtons, spacer,  swtichBox);
        leftMenu.setAlignment(Pos.CENTER);
    }

    public void setupDrinksButton(BorderPane borderPane, HBox drinkActionBox, YourOrderViewController yourOrderViewController, DrinkFoodViewerController drinkFoodViewerController)
    {
        this.drinksButton.setOnAction(e -> {
            borderPane.setLeft(drinkActionBox);
            borderPane.setRight(yourOrderViewController.getOrderBox());
            this.currentContext = "drinks";

            drinkFoodViewerController.getMiddleBox().getChildren().clear();
            drinkFoodViewerController.getMiddleBox().getChildren().addAll(drinkFoodViewerController.getSearchBox(), drinkFoodViewerController.getDrinkContainer(), drinkFoodViewerController.getButtonsBox());
        });
    }

    public void setupFoodButton(BorderPane borderPane, HBox foodActionBox, YourOrderViewController yourOrderViewController, DrinkFoodViewerController drinkFoodViewerController)
    {
        this.foodButton.setOnAction(e -> {
            borderPane.setLeft(foodActionBox);
            borderPane.setRight(yourOrderViewController.getOrderBox());
            this.currentContext = "foods";

            drinkFoodViewerController.getMiddleBox().getChildren().clear();
            drinkFoodViewerController.getMiddleBox().getChildren().addAll(drinkFoodViewerController.getSearchBox(), drinkFoodViewerController.getFoodContainer(), drinkFoodViewerController.getButtonsBox());
        });
    }

    public String getCurrentContext() {
        return currentContext;
    }

    public VBox getLeftMenu() {
        return leftMenu;
    }
}
