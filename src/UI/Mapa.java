/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import logika.IHra;
import main.Main;
import utils.Observer;

/**
 * @author koza02
 */
public class Mapa extends AnchorPane implements Observer {

    private IHra hra;
    private Circle tecka;

    public Mapa(IHra hra) {
        this.hra = hra;
        hra.getHerniPlan().registerObserver(this);
        init();
    }

    private void init() {
        ImageView obrazek = new ImageView(new Image(Main.class.getResourceAsStream("/zdroje/mapa.jpg"), 400, 400, false, false));
        tecka = new Circle(10, Paint.valueOf("black"));
        this.getChildren().addAll(obrazek, tecka);
        update();
    }

    @Override
    public void update() {
        this.setTopAnchor(tecka, hra.getHerniPlan().getAktualniProstor().getPosY());
        this.setLeftAnchor(tecka, hra.getHerniPlan().getAktualniProstor().getPosX());
    }

    @Override
    public void novaHra(IHra hra) {
        hra.getHerniPlan().deleteObserver(this);
        this.hra = hra;
        hra.getHerniPlan().registerObserver(this);
        update();

    }

}