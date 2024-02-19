package CheckoutController;

import ClientController.StartMenuController;
import ClientController.YourOrderViewController;
import Model.User;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class AuthAndSwitchController {
    private List<User> users;
    private Label usernameLabel;
    private Label passwordLabel;
    private TextField usernameField;
    private TextField passwordField;
    private Button swtichMenuButton;
    private Button swtichToClientInterfaceButton;
    private Button swtichToClientInterfaceButton2;
    private Button loginButton;
    private HBox usernameBox;
    private HBox passwordBox;
    private HBox switchButtonBox;
    private HBox switchBoxWithOrderBox;
    private VBox loginBox;
    private StackPane stackPane;

    public AuthAndSwitchController(List<User> users)
    {
        this.users = users;
        this.usernameLabel = new Label("USERNAME:");
        this.passwordLabel = new Label("PASSWORD:");
        this.usernameField = new TextField();
        this.passwordField = new TextField();
        this.loginButton = new Button("LOGIN");
        this.swtichMenuButton = new Button("SWITCH");
        this.swtichToClientInterfaceButton = new Button("SWITCH");
        this.swtichToClientInterfaceButton2 = new Button("SWITCH");
        this.stackPane = new StackPane();

        this.swtichMenuButton.setPrefSize(150, 50);
        this.swtichMenuButton.getStyleClass().add("interface-button");
        this.swtichToClientInterfaceButton2.getStyleClass().add("toolbar-button");

        this.setupAuthBox();
    }

    public void placeSwitchToClientInterfaceButton(BorderPane borderPane)
    {
        this.swtichToClientInterfaceButton.setPrefSize(150,50);
        this.swtichToClientInterfaceButton.getStyleClass().add("interface-button");

        HBox switchToClientBox = new HBox(swtichToClientInterfaceButton);
        switchToClientBox.setAlignment(Pos.TOP_LEFT);

        borderPane.setTop(switchToClientBox);
    }

    public void setupAuthBox()
    {
        Pane usernameWrapper = new Pane(usernameLabel);
        Pane passwordWrapper = new Pane(passwordLabel);

        this.usernameBox = new HBox(10, usernameWrapper, usernameField);
        this.passwordBox = new HBox(10, passwordWrapper, passwordField);
        this.loginBox = new VBox(5, usernameBox, passwordBox, loginButton);

        usernameWrapper.setPrefHeight(50);
        passwordWrapper.setPrefHeight(50);

        Region backgroundRegion = new Region();
        this.stackPane = new StackPane(backgroundRegion, loginBox);

        stackPane.setMaxSize(470, 180);
        stackPane.setAlignment(Pos.CENTER);

        usernameField.setPrefSize(250, 50);
        passwordField.setPrefSize(250,50);

        loginButton.setPrefSize(470, 60);

        usernameField.setPromptText("Username");
        passwordField.setPromptText("Password");

        usernameBox.setAlignment(Pos.CENTER);
        passwordBox.setAlignment(Pos.CENTER);
        loginBox.setAlignment(Pos.CENTER);

        {
            usernameLabel.getStyleClass().add("welcome-label");
            passwordLabel.getStyleClass().add("welcome-label");

            usernameWrapper.setStyle("-fx-background-color: #3355ff;");
            passwordWrapper.setStyle("-fx-background-color: #3355ff;");

            usernameField.setStyle("-fx-font-size: 20px;");
            usernameField.getStyleClass().add("drinks-label");
            passwordField.setStyle("-fx-font-size: 20px;");
            passwordField.getStyleClass().add("drinks-label");

            loginButton.getStyleClass().add("interface-button");
            backgroundRegion.setStyle("-fx-background-color: #df00fe;");
        }
    }

    public void invalidLogin()
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("WRONG USER");
        alert.setHeaderText(null);
        alert.setContentText("Incorrect username or password!");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/startMenu.css").toExternalForm());

        Label contentLabel = (Label) dialogPane.lookup(".content");
        if (contentLabel != null) {
            contentLabel.getStyleClass().add("alert-content-text");
        }

        alert.showAndWait();
    }

    public boolean isValidAuth()
    {
        String username = usernameField.getText();
        String password = passwordField.getText();

        for (int i = 0; i < users.size(); ++i)
            if (username.equals(users.get(i).getName()) && password.equals(users.get(i).getPassword()))
                return true;

        return false;
    }

    public void setupSwitchButton(BorderPane borderPane)
    {
        this.swtichMenuButton.setOnAction(e -> {
            borderPane.setLeft(null);
            borderPane.setRight(null);
            borderPane.setCenter(this.getStackPane());

            this.placeSwitchToClientInterfaceButton(borderPane);
        });
    }

    public void setupSwitchToClientInterfaceButton(BorderPane borderPane, StartMenuController startMenuController, YourOrderViewController yourOrderViewController)
    {
        this.swtichToClientInterfaceButton.setOnAction(e -> {
            borderPane.setCenter(null);
            borderPane.setLeft(startMenuController.getLeftMenu());
            borderPane.setRight(yourOrderViewController.getOrderBox());
            borderPane.setTop(null);
        });
    }

    public void setupSwitchToClientInterfaceButton2(BorderPane borderPane, StartMenuController startMenuController, YourOrderViewController yourOrderViewController)
    {
        this.swtichToClientInterfaceButton2.setOnAction(e -> {
            borderPane.setCenter(null);
            borderPane.setLeft(startMenuController.getLeftMenu());
            borderPane.setRight(yourOrderViewController.getOrderBox());
            borderPane.setTop(null);
        });
    }

    public void setupLoginButton(BorderPane borderPane, CRUDOperationsController crudOperations, UserCRUDOperationsController userCRUDOperationsController, LiveOrdersController liveOrdersController, ViewerController viewerController, RestaurantController restaurantController)
    {
        this.loginButton.setOnAction(e -> {
                if (isValidAuth())
                {
                    borderPane.setCenter(null);
                    crudOperations.createBoxWithViewerBox(userCRUDOperationsController, liveOrdersController, viewerController, restaurantController);
                    borderPane.setTop(crudOperations.getBiggestBox());
                }
                else
                    this.invalidLogin();
        });
    }

    public HBox getSwitchBoxWithOrderBox() {
        return switchBoxWithOrderBox;
    }

    public StackPane getStackPane() {
        return stackPane;
    }

    public Button getSwtichMenuButton() {
        return swtichMenuButton;
    }

    public Button getSwtichToClientInterfaceButton() {
        return swtichToClientInterfaceButton;
    }

    public Button getSwtichToClientInterfaceButton2() {
        return swtichToClientInterfaceButton2;
    }
}
