/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import UI.Mapa;
import UI.MenuPole;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logika.Hra;
import logika.IHra;
import logika.UpdateObserver;
import uiText.TextoveRozhrani;

import java.util.Collection;

/**
 * @author koza02
 */
public class Main extends Application {
    private Mapa mapa;
    private MenuPole menu;
    private IHra hra;
    private TextArea centerText;
    private Stage primaryStage;
    private ComboBox<String> commandComboBox;
    private ComboBox<String> paramComboBox = new ComboBox<String>();
    private GridPane inventoryPane;
    private GridPane countryInventoryPane;
    private GridPane kurfPanel;
    private Button runCommandButton = new Button("Run");
    private FlowPane dolniPanel;
    private Text countryInventoryCaption;
    private TextArea countryOutText;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        if (args.length == 0) {
            launch(args);
        } else {

            if (args[0].equals("-text")) {

                IHra hra = new Hra();
                TextoveRozhrani textoveRozhrani = new TextoveRozhrani(hra);
                textoveRozhrani.hraj();
            } else
                System.out.println("Neplatny parametr");
            System.exit(1);
        }
    }

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        hra = new Hra();
        mapa = new Mapa(hra);
        menu = new MenuPole(this);

        // Тестовое поле содержащие выходы из страны
        countryOutText = new TextArea();
        countryOutText.setText("");
        countryOutText.setEditable(false);
        countryOutText.setMaxWidth(100);
        countryOutText.setMaxWidth(100);

        centerText = new TextArea();
        centerText.setText(hra.vratUvitani());
        centerText.setEditable(false);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(centerText);

        Label zadejPrikazLabel = new Label("Zadejte příkaz ");
        zadejPrikazLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        // Заполняем выпадающий список команд
        commandComboBox = new ComboBox<String>();
        hra.getCommands().forEach(s -> {
            commandComboBox.getItems().add(s);
        });

        // При выборе команды заполняем выпадающий список параметро выбранной команды
        commandComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            // очищаяем список параметров
            paramComboBox.getItems().clear();
            paramComboBox.setValue(null);

            // получаем интерфейс выбранной команды
            logika.SeznamPrikazu prikazu = hra.getPlatnePrikazy();
            logika.IPrikaz prikaz = prikazu.vratPrikaz(commandComboBox.getValue());

            if (prikaz == null)
                return;

            // получаем список параметров команды
            Collection<String> params = prikaz.getParams(hra);

            if (params != null) {
                // добавляем параметры в выпадающий список
                params.forEach(s -> {
                    paramComboBox.getItems().add(s);
                });
            }

            // отключаем выпадающий список, если параметров нет
            paramComboBox.setDisable(paramComboBox.getItems().size() == 0);
        });

        dolniPanel = new FlowPane();
        dolniPanel.setAlignment(Pos.CENTER);
        dolniPanel.getChildren().addAll(zadejPrikazLabel, commandComboBox, paramComboBox, runCommandButton);

        // Ловим событие по нажатю кнопки выполнить команду
        runCommandButton.setOnAction((ActionEvent event) -> {
            // Получаем строковое значение команды и ее параметра
            String command = commandComboBox.getValue() + " " + (paramComboBox.getValue() == null ? "" : paramComboBox.getValue());

            // Выполняем команду
            String odpoved = hra.zpracujPrikaz(command);

            // Вывдоим результат в центральное тектовое окно
            centerText.appendText("\n\n" + command + "\n\n");
            centerText.appendText("\n\n" + odpoved + "\n\n");

            if (hra.konecHry()) {
                // если конец игры отключаем элементы управления
                commandComboBox.setDisable(true);
                paramComboBox.setDisable(true);
                runCommandButton.setDisable(true);
            }

            commandComboBox.setValue(null);
            paramComboBox.setValue(null);

            // обновляем выходы из стран
            updateNazev();

            // Вызываем обсервера
            hra.getHerniPlan().notifyAllObservers();

        });


        // обновляем выходы из стран
        updateNazev();

        // Создаем панел с вещами
        inventoryPane = new GridPane();

        // Создаем панел с вещами которые есть в стране
        countryInventoryPane = new GridPane();

        // Создаем панель Kurf
        kurfPanel = new GridPane();

        // Передаем элементы управления в hra для работы из обсервера
        hra.setInventory(countryInventoryPane, inventoryPane, kurfPanel);

        // регистрируем обсервр
        hra.getHerniPlan().registerObserver(new UpdateObserver(hra));
        hra.getHerniPlan().notifyAllObservers();
        hra.setTextArea(centerText);

        // Создаем панель с вещами игрока
        BorderPane playerInventory = new BorderPane();
        playerInventory.setTop(new Text("Batoh"));
        playerInventory.setCenter(inventoryPane);

        countryInventoryCaption = new Text("Ve státě máte:");

        BorderPane countryInventory = new BorderPane();
        countryInventory.setTop(countryInventoryCaption);
        countryInventory.setCenter(countryInventoryPane);

        // Создаем панель с вещами игрока
        BorderPane kurfInventory = new BorderPane();
        kurfInventory.setTop(new Text("Kufr v Německu"));
        kurfInventory.setCenter(kurfPanel);

        // Кнтейнер выхода из страны
        BorderPane countryOutBorderPane = new BorderPane();
        // Выводим заголовок
        countryOutBorderPane.setTop(new Text("Mužete jet do:"));
        // Выводим таблицу
        countryOutBorderPane.setCenter(countryOutText);

        BorderPane mapPane = new BorderPane();
        mapPane.setLeft(mapa);
        // показываем контейнер выходы из страны
        mapPane.setRight(countryOutBorderPane);

        BorderPane inventoryPane = new BorderPane();
        // показываем что есть в стране
        inventoryPane.setTop(countryInventory);
        // показываем инвентарь игрока
        inventoryPane.setCenter(playerInventory);
        // показываем инвентарь kurf
        inventoryPane.setBottom(kurfInventory);

        BorderPane leftPane = new BorderPane();
        leftPane.setMinWidth(100);
        leftPane.setTop(mapPane);
        leftPane.setCenter(inventoryPane);

        //obrazek s mapou
        borderPane.setBottom(dolniPanel);

        // показываем левую панель
        borderPane.setLeft(leftPane);


        //menu adventury
        borderPane.setTop(menu);

        Scene scene = new Scene(borderPane, 800, 650);

        primaryStage.setTitle("Moje Adventura");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Обновление списка выхода из стран
     */
    public void updateNazev() {
        countryOutText.setText("");

        hra.getHerniPlan().getAktualniProstor().getVychody().forEach(prostor -> {
            countryOutText.appendText(prostor.getNazev() + "\n");
        });
    }

    public void novaHra() {
        hra = new Hra();
        centerText.setText(hra.vratUvitani());
        //to dame pro vsechny observery
        mapa.novaHra(hra);

    }

    /**
     * @return the primaryStage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * @param primaryStage the primaryStage to set
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }



}
