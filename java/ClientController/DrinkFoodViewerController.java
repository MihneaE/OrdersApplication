package ClientController;

import CheckoutController.AuthAndSwitchController;
import Model.Drink;
import Model.Food;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

public class DrinkFoodViewerController {
    private TextField searchField;
    private Button searchButton;
    private TableView<Drink> drinkContainer;
    private TableView<Food> foodContainer;
    private Button addButton;
    private Button undoButton;
    private HBox searchBox;
    private HBox buttonsBox;
    private VBox middleBox;
    private YourOrderViewController yourOrderViewController;
    private DrinkMenuController drinkMenuController;
    private FoodMenuController foodMenuController;
    private StartMenuController startMenuController;
    private RateFoodController rateFoodController;
    private List<Drink> orderedDrink;
    private List<Food> orderedFood;
    private boolean displayingDrinks;

    public DrinkFoodViewerController(YourOrderViewController yourOrderViewController, DrinkMenuController drinkMenuController, FoodMenuController foodMenuController, StartMenuController startMenuController, RateFoodController rateFoodController, List<Drink> orderedDrink, List<Food> orderedFood)
    {
        this.searchField = new TextField();
        this.searchButton = new Button("SEARCH");
        this.drinkContainer = new TableView<>();
        this.foodContainer = new TableView<>();
        this.addButton = new Button("ADD");
        this.undoButton = new Button("BACK");
        this.yourOrderViewController = yourOrderViewController;
        this.drinkMenuController = drinkMenuController;
        this.foodMenuController = foodMenuController;
        this.startMenuController = startMenuController;
        this.rateFoodController = rateFoodController;
        this.orderedDrink = orderedDrink;
        this.orderedFood = orderedFood;
        this.middleBox = new VBox(5);

        this.displayingDrinks = false;

        this.setupDrinksContainer();
        this.setupFoodContainer();
        this.setupStylesAndDesign();
        //this.setupContainerCellFactory();
    }

    public void setupStylesAndDesign()
    {
        this.searchBox = new HBox( 5,searchField, searchButton);
        this.buttonsBox = new HBox(6, undoButton, addButton);

        this.drinkContainer.setPrefHeight(606);
        this.drinkContainer.setMaxWidth(540);

        this.foodContainer.setMaxWidth(540);
        this.foodContainer.setPrefHeight(606);

        this.undoButton.setPrefHeight(50);
        this.undoButton.setPrefWidth(267);
        this.addButton.setPrefHeight(50);
        this.addButton.setPrefWidth(267);

        this.searchButton.setPrefHeight(50);
        this.searchButton.setPrefWidth(130);

        this.searchField.setPrefHeight(50);
        this.searchField.setPrefWidth(405);

        Label menuLabel = new Label("Menu");
        drinkContainer.setPlaceholder(menuLabel);
        foodContainer.setPlaceholder(menuLabel);

        {
            this.searchButton.getStyleClass().add("menu-button");
            this.undoButton.getStyleClass().add("menu-button");
            this.addButton.getStyleClass().add("menu-button");

            this.searchField.setPromptText("Search...");
            searchField.setStyle("-fx-font-size: 20px;");
            searchField.getStyleClass().add("drinks-label");

            drinkContainer.setStyle("-fx-background-color: #1a9323;");
            foodContainer.setStyle("-fx-background-color: #1a9323;");

            menuLabel.getStyleClass().add("order-text");
        }
    }

    public void displayFood(List<Food> foods)
    {
        this.foodContainer.getItems().clear();
        this.foodContainer.getItems().addAll(foods);
    }

    public void displayDrink(List<Drink> drinks)
    {
        this.drinkContainer.getItems().clear();
        this.drinkContainer.getItems().addAll(drinks);
    }


    public void addDrinkToOrder()
    {
        Object selectedI = this.drinkContainer.getSelectionModel().getSelectedItem();

        if (selectedI instanceof Drink)
        {
            Drink selectedItem = (Drink) selectedI;
            orderedDrink.add(selectedItem);

            Label nameLabel = new Label(String.valueOf(selectedItem.getName()));
            Label volumeLabel = new Label(String.valueOf(selectedItem.getVolume()));
            Label priceLabel = new Label(String.valueOf(selectedItem.getPrice()));

            Label newNameLabel = new Label(nameLabel.getText());
            Label newVolumeLabel = new Label("Volume: " + volumeLabel.getText() + " ML");
            Label newPriceLabel = new Label("Price: " + priceLabel.getText() + " LEI");

            {
                newNameLabel.getStyleClass().add("drinks-label-color-v4");
                newVolumeLabel.getStyleClass().add("drinks-label-color-v2");
                newPriceLabel.getStyleClass().add("drinks-label-color-v2");
            }

            ImageView imageView = new ImageView();

            if (drinkMenuController.getCurrentCategory().equals("coffees")) {
                imageView = new ImageView(new Image("coffee_its_benefits_898_1_.jpg"));
            } else if (drinkMenuController.getCurrentCategory().equals("teas")) {
                imageView = new ImageView(new Image("scented-teas.jpg"));
            } else if (drinkMenuController.getCurrentCategory().equals("juices")) {
                imageView = new ImageView(new Image("istockphoto-158268808-612x612.jpg"));
            } else if (drinkMenuController.getCurrentCategory().equals("softDrinks")) {
                imageView = new ImageView(new Image("Soft-drink-health-concerns-not-yet-trickled-down-into-social-media-users-mentions-of-brands.jpg"));
            } else if (drinkMenuController.getCurrentCategory().equals("beers")) {
                imageView = new ImageView(new Image("1200px-NCI_Visuals_Food_Beer.jpg"));
            } else if (drinkMenuController.getCurrentCategory().equals("wines")) {
                imageView = new ImageView(new Image("Wine-Guide-Beaujolais-FT-BLOG0722-2000-7f1cac81f5044d3cbfeac708b66c4bea.jpg"));
            } else if (drinkMenuController.getCurrentCategory().equals("cocktails")) {
                imageView = new ImageView(new Image("Zombie_Cocktail_2667x2667_primary-4416b8395efd4a3986c209371e628e63.jpg"));
            } else if (drinkMenuController.getCurrentCategory().equals("strongDrinks")) {
                imageView = new ImageView(new Image("bottle-review_Jack-Daniels-Tennessee-Whiskey_1500x1500-d6c4b98c23d44bc8b3eeaaabb3d6308d.jpg"));
            }

            imageView.setFitWidth(50);
            imageView.setFitHeight(50);

            VBox nameAndImageBox = new VBox(10, newNameLabel, imageView);
            nameAndImageBox.setAlignment(Pos.CENTER);
            HBox volumeAndPriceBox = new HBox(50, newVolumeLabel, newPriceLabel);
            volumeAndPriceBox.setAlignment(Pos.CENTER);

            VBox itemToAdd = new VBox(10, nameAndImageBox, volumeAndPriceBox);

            yourOrderViewController.addItemToOrder(itemToAdd);

            String labelText = newPriceLabel.getText();
            String numericPart = labelText.replace("Price: ", "").replace("LEI", "").trim();

            yourOrderViewController.updateTotalSum(Float.parseFloat(numericPart));

            this.drinkContainer.getSelectionModel().clearSelection();
        }
    }

    public void addFoodToOrder()
    {
        Object selectedI = this.foodContainer.getSelectionModel().getSelectedItem();

        if (selectedI instanceof Food)
        {
            Food selectedItem = (Food) selectedI;
            orderedFood.add(selectedItem);

            Label nameLabel = new Label(selectedItem.getName());
            Label gramsLabel = new Label(String.valueOf(selectedItem.getGrams()));
            Label priceLabel = new Label(String.valueOf(selectedItem.getPrice()));
            Label ratingLabel = new Label(String.valueOf(selectedItem.getRating()));

            Label newNameLabel = new Label(nameLabel.getText());
            Label newGramsLabel = new Label("Volume: " + gramsLabel.getText() + " GR");
            Label newPriceLabel = new Label("Price: " + priceLabel.getText() + " LEI");
            Label newRatingLabel = new Label("Rating: " + ratingLabel.getText() + "☆");

            {
                newNameLabel.getStyleClass().add("drinks-label-color-v4");
                newGramsLabel.getStyleClass().add("drinks-label-color-v2");
                newPriceLabel.getStyleClass().add("drinks-label-color-v2");
                newRatingLabel.getStyleClass().add("drinks-label-color-v2");
            }

            ImageView imageView = new ImageView();

            if (foodMenuController.getCurrentCategory().equals("salad")) {
                imageView = new ImageView(new Image("Big-Italian-Salad.jpg"));
            } else if (foodMenuController.getCurrentCategory().equals("soup")) {
                imageView = new ImageView(new Image("Homemade-Vegetable-Soup-Recipe-2-1200.jpg"));
            } else if (foodMenuController.getCurrentCategory().equals("smallBites")) {
                imageView = new ImageView(new Image("8ed6ae3208df16bda783b803f68666f8.jpg"));
            } else if (foodMenuController.getCurrentCategory().equals("meat")) {
                imageView = new ImageView(new Image("raw-meat.jpg"));
            } else if (foodMenuController.getCurrentCategory().equals("pasta")) {
                imageView = new ImageView(new Image("paste_italiene_cu_ierburi_800.jpg"));
            } else if (foodMenuController.getCurrentCategory().equals("seaFood")) {
                imageView = new ImageView(new Image("photo-1615141982883-c7ad0e69fd62.jpeg"));
            } else if (foodMenuController.getCurrentCategory().equals("pizza")) {
                imageView = new ImageView(new Image("Pizza-3007395.jpg"));
            } else if (foodMenuController.getCurrentCategory().equals("cakes")) {
                imageView = new ImageView(new Image("Lady-Red-Velvet-Cake---London_-Surrey_-Berkshire_1600x.jpg"));
            } else if (foodMenuController.getCurrentCategory().equals("iceScream")) {
                imageView = new ImageView(new Image("Ice_cream_with_whipped_cream,_chocolate_syrup,_and_a_wafer_(cropped).jpg"));
            } else if (foodMenuController.getCurrentCategory().equals("otherDesserts")) {
                imageView = new ImageView(new Image("Desserts.jpg"));
            }

            imageView.setFitWidth(50);
            imageView.setFitHeight(50);

            VBox nameAndImageBox = new VBox(10, newNameLabel, imageView);
            nameAndImageBox.setAlignment(Pos.CENTER);
            HBox gramsAndPriceAndRatingBox = new HBox(20, newGramsLabel, newPriceLabel, newRatingLabel);
            gramsAndPriceAndRatingBox.setAlignment(Pos.CENTER);

            VBox itemToAdd = new VBox(10, nameAndImageBox, gramsAndPriceAndRatingBox);

            yourOrderViewController.addItemToOrder(itemToAdd);

            String labelText = newPriceLabel.getText();
            String numericPart = labelText.replace("Price: ", "").replace("LEI", "").trim();

            yourOrderViewController.updateTotalSum(Float.parseFloat(numericPart));

            this.foodContainer.getSelectionModel().clearSelection();
            rateFoodController.getOrderedFood().add(selectedItem);
        }
    }

    public void showFilteredDrinks(List<Drink> drinks)
    {
        String query = searchField.getText().toLowerCase().trim();

        System.out.println(query);

        List<Drink> filteredDrinks = new ArrayList<>();

        for (Drink drink : drinks)
            if (drink.getName().toLowerCase().contains(query))
                filteredDrinks.add(drink);

        this.displayDrink(filteredDrinks);
    }

    public void showFilteredFood(List<Food> foods)
    {
        String query = searchField.getText().toLowerCase().trim();

        List<Food> filteredFoods = new ArrayList<>();

        for (Food food : foods)
            if (food.getName().toLowerCase().contains(query))
                filteredFoods.add(food);

        this.displayFood(filteredFoods);
    }

    public void setupAddButton(boolean displayingDrinks)
    {
        this.addButton.setOnAction(e -> {

                if (displayingDrinks)
                    addDrinkToOrder();
                else
                    addFoodToOrder();
        });
    }

    public void searchBeers(List<Drink> beers)
    {
        this.showFilteredDrinks(beers);
    }

    public void searchWines(List<Drink> wines)
    {
        this.showFilteredDrinks(wines);
    }

    public void searchCocktails(List<Drink> cocktails)
    {
        this.showFilteredDrinks(cocktails);
    }

    public void searchStrongDrinks(List<Drink> strongDrinks)
    {
        this.showFilteredDrinks(strongDrinks);
    }

    public void searchCoffees(List<Drink> coffees)
    {
        this.showFilteredDrinks(coffees);
    }

    public void searchTeas(List<Drink> teas)
    {
        this.showFilteredDrinks(teas);
    }

    public void searchJuices(List<Drink> juices)
    {
        this.showFilteredDrinks(juices);
    }

    public void searchSoftDrinks(List<Drink> softDrinks)
    {
        this.showFilteredDrinks(softDrinks);
    }

    public void searchSalad(List<Food> salads)
    {
        this.showFilteredFood(salads);
    }

    public void searchSoup(List<Food> soup)
    {
        this.showFilteredFood(soup);
    }

    public void searchSmallBites(List<Food> smallBites)
    {
        this.showFilteredFood(smallBites);
    }

    public void searchMeat(List<Food> meat)
    {
        this.showFilteredFood(meat);
    }

    public void searchPasta(List<Food> pasta)
    {
        this.showFilteredFood(pasta);
    }

    public void searchSeaFood(List<Food> seaFood)
    {
        this.showFilteredFood(seaFood);
    }

    public void searchPizza(List<Food> pizza)
    {
        this.showFilteredFood(pizza);
    }

    public void  searchCakes(List<Food> cakes)
    {
        this.showFilteredFood(cakes);
    }

    public void searchIceScream(List<Food> iceScream)
    {
        this.showFilteredFood(iceScream);
    }

    public void searchOtherDesserts(List<Food> otherDesserts)
    {
        this.showFilteredFood(otherDesserts);
    }

    public void setupSearchButton(List<Drink> beers, List<Drink> wines, List<Drink> cocktails, List<Drink> strongDrinks,
                                        List<Drink> coffees, List<Drink> teas, List<Drink> juices, List<Drink> softDrinks,
                                        List<Food> salads, List<Food> soups, List<Food> smallBites, List<Food> meat, List<Food> pasta,
                                        List<Food> seaFood, List<Food> pizza, List<Food> cakes, List<Food> iceScream, List<Food> otherDesserts)
    {
        this.searchButton.setOnAction(e -> {
            if (startMenuController.getCurrentContext().equals("drinks"))
            {
                if (drinkMenuController.getCurrentCategory().equals("beers"))
                    this.searchBeers(beers);
                else if (drinkMenuController.getCurrentCategory().equals("wines"))
                    this.searchWines(wines);
                else if (drinkMenuController.getCurrentCategory().equals("cocktails"))
                    this.searchCocktails(cocktails);
                else if (drinkMenuController.getCurrentCategory().equals("strongDrinks"))
                    this.searchStrongDrinks(strongDrinks);
                else if (drinkMenuController.getCurrentCategory().equals("coffees"))
                    this.searchCoffees(coffees);
                else if (drinkMenuController.getCurrentCategory().equals("teas"))
                    this.searchTeas(teas);
                else if (drinkMenuController.getCurrentCategory().equals("juices"))
                    this.searchJuices(juices);
                else if (drinkMenuController.getCurrentCategory().equals("softDrinks"))
                    this.searchSoftDrinks(softDrinks);
            }
            else if (startMenuController.getCurrentContext().equals("foods"))
            {
                if (foodMenuController.getCurrentCategory().equals("salad"))
                    this.searchSalad(salads);
                else if (foodMenuController.getCurrentCategory().equals("soup"))
                    this.searchSoup(soups);
                else if (foodMenuController.getCurrentCategory().equals("smallBites"))
                    this.searchSmallBites(smallBites);
                else if (foodMenuController.getCurrentCategory().equals("meat"))
                    this.searchMeat(meat);
                else if (foodMenuController.getCurrentCategory().equals("pasta"))
                    this.searchPasta(pasta);
                else if (foodMenuController.getCurrentCategory().equals("seaFood"))
                    this.searchSeaFood(seaFood);
                else if (foodMenuController.getCurrentCategory().equals("pizza"))
                    this.searchPizza(pizza);
                else if (foodMenuController.getCurrentCategory().equals("cakes"))
                    this.searchCakes(cakes);
                else if (foodMenuController.getCurrentCategory().equals("iceScream"))
                    this.searchIceScream(iceScream);
                else if (foodMenuController.getCurrentCategory().equals("otherDesserts"))
                    this.searchOtherDesserts(otherDesserts);
            }
        });
    }

    public void setupUndoButton(BorderPane borderPane, YourOrderViewController yourOrderViewController)
    {
        this.undoButton.setOnAction(e -> {
            borderPane.setLeft(startMenuController.getLeftMenu());
            borderPane.setRight(yourOrderViewController.getOrderBox());
        });
    }

    public void setupDrinksContainer()
    {
        TableColumn<Drink, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<Drink, Double> volumeColumn = new TableColumn<>("Volume");
        volumeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getVolume()));

        TableColumn<Drink, Integer> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrice()));

        nameColumn.setPrefWidth(300);
        nameColumn.setStyle("-fx-background-color: #df00fe;");
        nameColumn.setStyle("-fx-alignment: CENTER;");
        drinkContainer.getColumns().add(nameColumn);

        volumeColumn.setPrefWidth(120);
        volumeColumn.setStyle("-fx-alignment: CENTER;");
        drinkContainer.getColumns().add(volumeColumn);

        priceColumn.setPrefWidth(120);
        priceColumn.setStyle("-fx-alignment: CENTER;");
        drinkContainer.getColumns().add(priceColumn);

        nameColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    ImageView imageView = new ImageView();

                    if (drinkMenuController.getCurrentCategory().equals("coffees")) {
                        imageView = new ImageView(new Image("coffee_its_benefits_898_1_.jpg"));
                    } else if (drinkMenuController.getCurrentCategory().equals("teas")) {
                        imageView = new ImageView(new Image("scented-teas.jpg"));
                    } else if (drinkMenuController.getCurrentCategory().equals("juices")) {
                        imageView = new ImageView(new Image("istockphoto-158268808-612x612.jpg"));
                    } else if (drinkMenuController.getCurrentCategory().equals("softDrinks")) {
                        imageView = new ImageView(new Image("Soft-drink-health-concerns-not-yet-trickled-down-into-social-media-users-mentions-of-brands.jpg"));
                    } else if (drinkMenuController.getCurrentCategory().equals("beers")) {
                        imageView = new ImageView(new Image("1200px-NCI_Visuals_Food_Beer.jpg"));
                    } else if (drinkMenuController.getCurrentCategory().equals("wines")) {
                        imageView = new ImageView(new Image("Wine-Guide-Beaujolais-FT-BLOG0722-2000-7f1cac81f5044d3cbfeac708b66c4bea.jpg"));
                    } else if (drinkMenuController.getCurrentCategory().equals("cocktails")) {
                        imageView = new ImageView(new Image("Zombie_Cocktail_2667x2667_primary-4416b8395efd4a3986c209371e628e63.jpg"));
                    } else if (drinkMenuController.getCurrentCategory().equals("strongDrinks")) {
                        imageView = new ImageView(new Image("bottle-review_Jack-Daniels-Tennessee-Whiskey_1500x1500-d6c4b98c23d44bc8b3eeaaabb3d6308d.jpg"));
                    }

                    imageView.setFitWidth(50);
                    imageView.setFitHeight(50);

                    Label textLabel = new Label(item);
                    textLabel.getStyleClass().add("drinks-label-color");
                    textLabel.getStyleClass().add("italic-text");
                    textLabel.setStyle("-fx-text-alignment: center; -fx-alignment: center;");

                    VBox cellBox = new VBox(10);
                    cellBox.getChildren().addAll(textLabel, imageView);
                    cellBox.setAlignment(Pos.CENTER);

                    setGraphic(cellBox);
                    setPrefHeight(80);
                    setStyle("-fx-background-color: rgb(213,103,35); -fx-alignment: center;");
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
                    getStyleClass().add("drinks-label-color");
                    getStyleClass().add("italic-text");
                    setPrefHeight(80);
                    setStyle("-fx-background-color: rgb(213,103,35); -fx-alignment: center;");
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
                    getStyleClass().add("drinks-label-color");
                    getStyleClass().add("italic-text");
                    setPrefHeight(80);
                    setStyle("-fx-background-color: rgb(213,103,35); -fx-alignment: center;");
                }
            }
        });

    }

    public void setupFoodContainer()
    {
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

        nameColumn.setPrefWidth(135);
        nameColumn.setStyle("-fx-alignment: CENTER;");
        foodContainer.getColumns().add(nameColumn);

        descriptionColumn.setPrefWidth(135);
        descriptionColumn.setStyle("-fx-alignment: CENTER;");
        foodContainer.getColumns().add(descriptionColumn);

        gramsColumn.setPrefWidth(90);
        gramsColumn.setStyle("-fx-alignment: CENTER;");
        foodContainer.getColumns().add(gramsColumn);

        priceColumn.setPrefWidth(90);
        priceColumn.setStyle("-fx-alignment: CENTER;");
        foodContainer.getColumns().add(priceColumn);

        ratingColumn.setPrefWidth(90);
        ratingColumn.setStyle("-fx-alignment: CENTER;");
        foodContainer.getColumns().add(ratingColumn);

        nameColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {

                    ImageView imageView = new ImageView();

                    if (foodMenuController.getCurrentCategory().equals("salad")) {
                        imageView = new ImageView(new Image("Big-Italian-Salad.jpg"));
                    } else if (foodMenuController.getCurrentCategory().equals("soup")) {
                        imageView = new ImageView(new Image("Homemade-Vegetable-Soup-Recipe-2-1200.jpg"));
                    } else if (foodMenuController.getCurrentCategory().equals("smallBites")) {
                        imageView = new ImageView(new Image("8ed6ae3208df16bda783b803f68666f8.jpg"));
                    } else if (foodMenuController.getCurrentCategory().equals("meat")) {
                        imageView = new ImageView(new Image("raw-meat.jpg"));
                    } else if (foodMenuController.getCurrentCategory().equals("pasta")) {
                        imageView = new ImageView(new Image("paste_italiene_cu_ierburi_800.jpg"));
                    } else if (foodMenuController.getCurrentCategory().equals("seaFood")) {
                        imageView = new ImageView(new Image("photo-1615141982883-c7ad0e69fd62.jpeg"));
                    } else if (foodMenuController.getCurrentCategory().equals("pizza")) {
                        imageView = new ImageView(new Image("Pizza-3007395.jpg"));
                    } else if (foodMenuController.getCurrentCategory().equals("cakes")) {
                        imageView = new ImageView(new Image("Lady-Red-Velvet-Cake---London_-Surrey_-Berkshire_1600x.jpg"));
                    } else if (foodMenuController.getCurrentCategory().equals("iceScream")) {
                        imageView = new ImageView(new Image("Ice_cream_with_whipped_cream,_chocolate_syrup,_and_a_wafer_(cropped).jpg"));
                    } else if (foodMenuController.getCurrentCategory().equals("otherDesserts")) {
                        imageView = new ImageView(new Image("Desserts.jpg"));
                    }

                    imageView.setFitWidth(50);
                    imageView.setFitHeight(50);

                    Label textLabel = new Label(item);
                    textLabel.setWrapText(true);
                    textLabel.setMaxWidth(column.getWidth());
                    textLabel.getStyleClass().add("drinks-label-color-v2");
                    textLabel.getStyleClass().add("italic-text");
                    textLabel.setStyle("-fx-text-alignment: center; -fx-alignment: center;");

                    VBox cellBox = new VBox(10);
                    cellBox.getChildren().addAll(textLabel, imageView);
                    cellBox.setAlignment(Pos.CENTER);

                    setGraphic(cellBox);
                    setPrefHeight(120);
                    setStyle("-fx-background-color: rgb(213,103,35); -fx-alignment: center;");
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
                    Label label = new Label(item);
                    label.setWrapText(true);
                    label.setMaxWidth(column.getWidth());
                    setStyle("-fx-background-color: rgb(213,103,35); -fx-alignment: center;");
                    label.getStyleClass().add("orders-label-v2");
                    label.getStyleClass().add("italic-text");

                    setPrefHeight(120);
                    setGraphic(label);
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
                    getStyleClass().add("drinks-label-color-v3");
                    getStyleClass().add("italic-text");
                    setPrefHeight(120);
                    setStyle("-fx-background-color: rgb(213,103,35); -fx-alignment: center;");
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
                    getStyleClass().add("drinks-label-color-v3");
                    getStyleClass().add("italic-text");
                    setPrefHeight(120);
                    setStyle("-fx-background-color: rgb(213,103,35); -fx-alignment: center;");
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
                    getStyleClass().add("drinks-label-color-v3");
                    getStyleClass().add("italic-text");
                    setPrefHeight(120);
                    setStyle("-fx-background-color: rgb(213,103,35); -fx-alignment: center;");
                }
            }
        });
    }
    public VBox getMiddleBox() {
        return middleBox;
    }

    public HBox getSearchBox() {
        return searchBox;
    }

    public HBox getButtonsBox() {
        return buttonsBox;
    }

    public TableView<Drink> getDrinkContainer() {
        return drinkContainer;
    }

    public TableView<Food> getFoodContainer() {
        return foodContainer;
    }
}
