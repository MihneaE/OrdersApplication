package ClientController;

import Model.Food;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class RateFoodController {
    private List<Food> orderedFood;
    private VBox boxOfFoodBoxes;
    private VBox finalBox;
    private Button backButton;
    private Label noFoodLabel;
    private YourOrderViewController yourOrderViewController;
    private boolean isRatedFoodButtonPressed;

    public RateFoodController(YourOrderViewController yourOrderViewController)
    {
        this.orderedFood = new ArrayList<>();
        this.boxOfFoodBoxes = new VBox(10);
        this.finalBox = new VBox(10);
        this.backButton = new Button("BACK");
        this.noFoodLabel = new Label("NO FOOD TO DISPLAY!");
        this.yourOrderViewController = yourOrderViewController;
        this.isRatedFoodButtonPressed = false;

        this.setupBackBtnAndFinalBox();
    }

    public void setupBackBtnAndFinalBox()
    {
        {
            this.boxOfFoodBoxes.getStyleClass().add("header-cell");
            this.noFoodLabel.getStyleClass().add("welcome-label");
            this.backButton.getStyleClass().add("menu-button");
        }

        this.backButton.setPrefSize(300, 50);

        this.finalBox.getChildren().addAll(boxOfFoodBoxes, backButton);
        this.finalBox.setAlignment(Pos.CENTER);
    }

    public void ratingRestrictions()
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("RATING VALUE");
        alert.setHeaderText(null);
        alert.setContentText("Rating must be between 1 and 5 stars!");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

        Label contentLabel = (Label) dialogPane.lookup(".content");
        if (contentLabel != null) {
            contentLabel.getStyleClass().add("alert-content-text");
        }

        alert.showAndWait();
    }

    public void setupFoodBox()
    {
        if (!isRatedFoodButtonPressed)
        {
            if (!orderedFood.isEmpty())
            {
                for (Food food : orderedFood)
                {
                    Label nameLabel = new Label(food.getName() + ": ");
                    Pane labelWrapper = new Pane(nameLabel);
                    TextField ratingField = new TextField();
                    Button submitButton = new Button("SUBMIT");

                    HBox foodBox = new HBox(10, labelWrapper, ratingField, submitButton);
                    foodBox.setAlignment(Pos.CENTER);

                    boxOfFoodBoxes.getChildren().add(foodBox);

                    submitButton.setPrefSize(150, 50);
                    ratingField.setPrefSize(70, 50);
                    labelWrapper.setPrefHeight(50);

                    {
                        labelWrapper.setStyle("-fx-background-color: #3355ff;");

                        submitButton.getStyleClass().add("menu-button");
                        nameLabel.getStyleClass().add("welcome-label");

                        ratingField.setStyle("-fx-font-size: 25px;");
                        ratingField.getStyleClass().add("drinks-label");
                    }

                    submitButton.setOnAction(e -> {
                        float rating;

                        try {
                            rating = Float.parseFloat(ratingField.getText());

                            if (rating < 1 || rating > 5)
                                this.ratingRestrictions();
                            else
                            {
                                food.updateRating(rating);

                                ImageView tickImageView = new ImageView(new Image("green-check-mark-icon-in-a-circle-tick-symbol-in-green-color-2ABNAN7.jpg"));
                                tickImageView.setFitHeight(50);
                                tickImageView.setFitWidth(70);

                                foodBox.getChildren().set(1, tickImageView);
                            }

                        } catch (NumberFormatException ex) {

                            System.out.println("INPUT MUST BE A NUMBER!");
                        }
                    });
                }
            }
            else
                boxOfFoodBoxes.getChildren().add(noFoodLabel);

            boxOfFoodBoxes.setAlignment(Pos.CENTER);
        }
    }

    public void setupFoodBoxAfterModify()
    {
        this.boxOfFoodBoxes.getChildren().clear();
        this.isRatedFoodButtonPressed = false;
    }

    public void resetOrderedFoodList()
    {
        orderedFood.clear();
        this.boxOfFoodBoxes.getChildren().clear();
    }

    public void setupRateFoodButton(BorderPane borderPane, YourOrderViewController yourOrderViewController)
    {
        yourOrderViewController.getRateFoodButton().setOnAction(e -> {
            this.setupFoodBox();
            borderPane.setCenter(this.getFinalBox());
            this.isRatedFoodButtonPressed = true;
        });
    }

    public void setupBackButton(BorderPane borderPane)
    {
        this.backButton.setOnAction(e -> {
            borderPane.setCenter(null);
            borderPane.setCenter(yourOrderViewController.getFinalLabelsBox());
        });
    }

    public List<Food> getOrderedFood() {
        return orderedFood;
    }

    public VBox getFinalBox() {
        return finalBox;
    }

    public void setRatedFoodButtonPressed(boolean ratedFoodButtonPressed) {
        isRatedFoodButtonPressed = ratedFoodButtonPressed;
    }
}
