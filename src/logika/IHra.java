/* Soubor je ulozen v kodovani UTF-8.
 * Kontrola kódování: Příliš žluťoučký kůň úpěl ďábelské ódy. */
package logika;


import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import java.util.Collection;

/**
 * Rozhraní které musí implementovat hra, je na ně navázáno uživatelské rozhraní
 *
 * @author Michael Kolling, Lubos Pavlicek, Jarmila Pavlickova
 * @version pro školní rok 2014/2015
 */
public interface IHra {
    //== VEŘEJNÉ KONSTANTY =====================================================
    //== DEKLAROVANÉ METODY ====================================================

    /**
     * Vrátí úvodní zprávu pro hráče.
     */
    public String vratUvitani();

    /**
     * Vrátí závěrečnou zprávu pro hráče.
     */
    public String vratEpilog();

    /**
     * Vrací true, pokud hra skončila.
     */
    public boolean konecHry();

    /**
     * Metoda zpracuje řetězec uvedený jako parametr, rozdělí ho na slovo příkazu a další parametry.
     * Pak otestuje zda příkaz je klíčovým slovem  např. jdi.
     * Pokud ano spustí samotné provádění příkazu.
     *
     * @param radek text, který zadal uživatel jako příkaz do hry.
     * @return vrací se řetězec, který se má vypsat na obrazovku
     */
    public String zpracujPrikaz(String radek);


    /**
     * Metoda vrátí odkaz na herní plán, je využita hlavně v testech,
     * kde se jejím prostřednictvím získává aktualní místnost hry.
     *
     * @return odkaz na herní plán
     */
    public HerniPlan getHerniPlan();

    /**
     * Method return commands name
     * @return
     */
    public Collection<String> getCommands();

    /**
     * Method return commands collection
     * @return
     */
    public SeznamPrikazu getPlatnePrikazy();


    /**
     * Method return batoha
     * @return
     */
    public Batoh getBatoh();

    /**
     * Methos set user interface
     * @param countryPanel
     * @param playerPanel
     * @param kurfPane
     */
    public void setInventory(GridPane countryPanel, GridPane playerPanel, GridPane kurfPane);

    /**
     * Method update inventory
     */
    public void updateState();

    /**
     * Set text area fro show messages
     * @param textArea
     */
    public void setTextArea(TextArea textArea);

    //== ZDĚDĚNÉ METODY ========================================================
    //== INTERNÍ DATOVÉ TYPY ===================================================
}
