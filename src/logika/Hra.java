package logika;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.awt.event.MouseEvent;
import java.beans.EventHandler;
import java.util.Collection;
import java.util.function.BiConsumer;

/**
 * Třída Hra - třída představující logiku adventury.
 * <p>
 * Toto je hlavní třída  logiky aplikace.  Tato třída vytváří instanci třídy HerniPlan, která inicializuje mistnosti hry
 * a vytváří seznam platných příkazů a instance tříd provádějící jednotlivé příkazy.
 * Vypisuje uvítací a ukončovací text hry.
 * Také vyhodnocuje jednotlivé příkazy zadané uživatelem.
 *
 * @author Michael Kolling, Lubos Pavlicek, Jarmila Pavlickova
 * @version pro školní rok 2014/2015
 */

public class Hra implements IHra {
    private SeznamPrikazu platnePrikazy;    // obsahuje seznam přípustných příkazů
    private HerniPlan herniPlan;
    private boolean konecHry = false;
    private Batoh batoh;
    private int brilliantsCount = 0; //počet diamantu na začatku hry
    private GridPane countryInventoryPane;
    private GridPane playerInventoryPane;
    private GridPane kurfInventoryPane;
    private TextArea textArea = null;

    /**
     * Vytváří hru a inicializuje místnosti (prostřednictvím třídy HerniPlan) a seznam platných příkazů.
     */
    public Hra() {
        herniPlan = new HerniPlan();
        platnePrikazy = new SeznamPrikazu();
        batoh = new Batoh();
        platnePrikazy.vlozPrikaz(new PrikazNapoveda(platnePrikazy));
        PrikazJdi prikazJdi = new PrikazJdi(herniPlan);
        prikazJdi.setHra(this);
        platnePrikazy.vlozPrikaz(prikazJdi);
        platnePrikazy.vlozPrikaz(new PrikazKonec(this));
        platnePrikazy.vlozPrikaz(new PrikazLearn(herniPlan));
        platnePrikazy.vlozPrikaz(new PrikazTakeBrilliant(this));
        platnePrikazy.vlozPrikaz(new PrikazSeber(herniPlan, batoh));
        platnePrikazy.vlozPrikaz(new PrikazPoloz(herniPlan, batoh));
        Prostor.hra = this;
    }


    public Batoh getBatoh() {
        return batoh;
    }

    @Override
    public void setInventory(GridPane pane1, GridPane pane2, GridPane pane3) {
        countryInventoryPane = pane1;
        playerInventoryPane = pane2;
        kurfInventoryPane = pane3;
    }

    /**
     * @return Prikazy
     */
    public SeznamPrikazu getPlatnePrikazy() {
        return platnePrikazy;
    }

    /**
     * Vrátí úvodní zprávu pro hráče.
     */
    public String vratUvitani() {
        return "Vítejte!\n" +
                "To je špionský příběh o dvou amerických špionech.\n" +
                "Pár let zpátky jeden z nich se ztratil při plněni tajné mise někde v Evropě.\n" +
                "A jeho starý kamarád (Vy) po práce, jakmile se dozvěděl o tom,\n" +
                "okamžitě začal ho hledat. Musíte nejdřiv najit Nůž, Pistole a Klíče a dát to do Kufru\n" +
                "v Něměcku, a už pak hledát kamarada v různých státech.\n" +
                "\n" + herniPlan.getAktualniProstor().dlouhyPopis();
    }

    /**
     * Vrátí závěrečnou zprávu pro hráče.
     */
    public String vratEpilog() {
        return "Dík, že jste si zahráli.  Ahoj.";
    }

    /**
     * Vrací true, pokud hra skončila.
     */
    public boolean konecHry() {
        return konecHry;
    }

    /**
     * Metoda zpracuje řetězec uvedený jako parametr, rozdělí ho na slovo příkazu a další parametry.
     * Pak otestuje zda příkaz je klíčovým slovem  např. jdi.
     * Pokud ano spustí samotné provádění příkazu.
     *
     * @param radek text, který zadal uživatel jako příkaz do hry.
     * @return vrací se řetězec, který se má vypsat na obrazovku
     */
    public String zpracujPrikaz(String radek) {
        String[] slova = radek.split("[ \t]+");
        String slovoPrikazu = slova[0];
        String[] parametry = new String[slova.length - 1];
        for (int i = 0; i < parametry.length; i++) {
            parametry[i] = slova[i + 1];
        }
        String textKVypsani = " .... ";
        if (platnePrikazy.jePlatnyPrikaz(slovoPrikazu)) {
            IPrikaz prikaz = platnePrikazy.vratPrikaz(slovoPrikazu);
            textKVypsani = prikaz.proved(parametry);
//            if (herniPlan.vitezstvi()) {
//                konecHry = true;
//                textKVypsani += " vyhra!!!";
//
//            }
        } else {
            textKVypsani = "Nevím co tím myslíš? Tento příkaz neznám. ";
        }
        return textKVypsani;

    }

    /**
     * Nastaví, že je konec hry, metodu využívá třída PrikazKonec,
     * mohou ji použít i další implementace rozhraní Prikaz.
     *
     * @param konecHry hodnota false= konec hry, true = hra pokračuje
     */
    void setKonecHry(boolean konecHry) {
        this.konecHry = konecHry;
    }

    /**
     * Metoda vrátí odkaz na herní plán, je využita hlavně v testech,
     * kde se jejím prostřednictvím získává aktualní místnost hry.
     *
     * @return odkaz na herní plán
     */
    public HerniPlan getHerniPlan() {
        return herniPlan;
    }

    /**
     * Metoda vrátí počet brilliantů u hrača.
     *
     * @return počet brilliantu u hrača
     */

    public int getBrilliantsCount() {
        return brilliantsCount;
    }


    /**
     * Metoda pomici ni můžeme zadat počet brilliantů u hrača.
     *
     * @return počet brilliantu u hrača
     */
    public void setBrilliantsCount(int brilliantsCount) {
        this.brilliantsCount = brilliantsCount;
    }


    /**
     * Возвращает списко названий команд
     * @return Collection<String>
     */
    public Collection<String> getCommands() {
        return platnePrikazy.getCommands();
    }


    /**
      * Přidá obrázek do vybrané tabulky
      * tabulka tabulky @param (země nebo hráč)
      * @param cestu k obrázku ve složce s prostředky
      * @param jePlayerInventory true, pokud jde o tabulku uživatelů
      */
    public void addInventoryImage(GridPane pane, String path, boolean isPlayerInventory)
    {
       // vytvořte obrázek podle cesty
        ImageView image = new ImageView("file:" + path);
       // přida data uživatele s názvem přidané položky pro pozdější identifikaci
        image.setUserData(path);
       // povolime reagovat na kliknutí na průhlednou oblast obrazu
        image.setPickOnBounds(true);
     // Přidavame posluchače kliknutím na obrázek
        image.setOnMouseClicked(event -> {
            // získáme kliknutí ImageView
            ImageView img = (ImageView) event.getSource();
       // Získat jméno předmětu
            String asset = ((String) img.getUserData()).replace("assets/", "");

            String command = null;
            if (pane.equals(countryInventoryPane)) {
            // Pokud jsou stisknuty obrazky které se nachazi v zěmi a vytvořime příkaz v závislosti na věci


                command = asset.equals("diamond") ? PrikazTakeBrilliant.NAZEV + " 1" : PrikazSeber.NAZEV + " " + asset;
            } else if (pane.equals(playerInventoryPane)) {
           // Pokud kliknete na obrázky uživatele
                if (!asset.equals("diamond")) {
             // a pokud to není diamant
                    command = PrikazPoloz.NAZEV + " " + asset;
                }
            }

            if (command != null) {
               // vytiskne výsledek volaného příkazu v pracovním prostoru
                String odpoved = zpracujPrikaz(command);
                if (textArea != null) {
                    textArea.appendText("\n\n" + command + "\n\n");
                    textArea.appendText("\n\n" + odpoved + "\n\n");
                }
            }

     // upozorní observers
            getHerniPlan().notifyAllObservers();
        });

        image.setFitWidth(50);
        image.setFitHeight(50);


      // vypočítá polohu obrazu
        int size = pane.getChildren().size();
        int row = size / 6;
        int col = size % 6;

    // přidava obrázek do určené pozice
        pane.add(image, col, row);
    }

    /**
      * Smazaní věci z tabulky
      * @param window
      */
    protected void clearInventory(GridPane pane)
    {
   // projděte seznam obrazku a odstranime jeich z paměti
        pane.getChildren().forEach(node -> {
            ImageView view = (ImageView) node;
            view.setOnMouseClicked(null);
        });
// vymažime obrázky z každého kontejneru
        pane.getChildren().clear();
    }

    public void updateState() {

        clearInventory(countryInventoryPane);
        clearInventory(playerInventoryPane);
        clearInventory(kurfInventoryPane);

        // dostavame kurf
        Vec kufr = getHerniPlan().getProstoryCollection().get("Něměcko").najdiVecVProstoru("kufr");
        kufr.getItemNamesInKuhr().forEach(s -> {
            addInventoryImage(kurfInventoryPane, "assets/" + s, false);
        });

       // přidavame obrázky objektů země
        getHerniPlan().getAktualniProstor().getItemNames().forEach(s -> {
            addInventoryImage(countryInventoryPane, "assets/" + s, false);
        });

     // přidavame obrázky z diamantů země
        for (int i = 0; i < getHerniPlan().getAktualniProstor().getBrilliantsCount(); i++) {
            addInventoryImage(countryInventoryPane, "assets/diamond", false);
        }

    // přidat obrázky položek od hrače
        getBatoh().getSeznamVeci().forEach((s, vec) -> {
            addInventoryImage(playerInventoryPane, "assets/" + s, true);
        });

   // přidava obrázky diamantů hráče
        for (int i = 0; i < getBrilliantsCount(); i++){
            addInventoryImage(playerInventoryPane, "assets/diamond", true);
        }

    }

/**
      * Nastaví TextArea pro aktualizace Observru
      * @param textArea
      */
    @Override
    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
    }


}

