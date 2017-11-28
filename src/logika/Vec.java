/* Soubor je ulozen v kodovani UTF-8.
 * Kontrola kódování: Příliš žluťoučký kůň úpěl ďábelské ódy. */
package logika;

import javafx.print.Collation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/*******************************************************************************
 * Instance třídy {@code Vec} představují ...
 *
 * @author Anton Kozinov  
 * @version 25.05.2015
 */
public class Vec {
    private static final int KAPACITA = 3;
    private String jmeno;
    private boolean prenositelna;
    private Map<String, Vec> seznamVeci;

    /***************************************************************************
     * Konstruktor třidy věc
     * @param jmeno - nazev věci
     * @param prenositelna - přenostilna nebo ne ta věc
     */
    public Vec(String jmeno, boolean prenositelna) {
        this.jmeno = jmeno;
        this.prenositelna = prenositelna;
        this.seznamVeci = new HashMap<>();
    }

    /**
     * Vrací  nazev věci
     */
    public String getJmeno() {
        return jmeno;
    }

    /**
     * Vrací true pokud přenositelna, nebo false pokud ne
     */
    public boolean jePrenositelna() {
        return prenositelna;
    }

    /**
     * Umožnuje vložit jednu věc do jiné.
     */
    public boolean vlozVec(Vec vec) {
        if (seznamVeci.size() < KAPACITA) {
            seznamVeci.put(vec.getJmeno(), vec);
            return true;
        }
        return false;
    }

    /**
     * Zkontroluje, zda se věc nachazí v jiné věci.
     *
     * @return vrací true, pokud je věc v jiné.
     */
    public boolean obsahujeVec(String jmeno) {
        return seznamVeci.containsKey(jmeno);
    }

    /**
     * Vybere věc z jiné věci.
     *
     * @return vrátí vybranou nami věc.
     */
    public Vec vyberVec(String jmeno) {
        Vec vec = null;
        if (seznamVeci.containsKey(jmeno)) {
            vec = seznamVeci.get(jmeno);
            if (vec.jePrenositelna()) {
                seznamVeci.remove(jmeno);
            }
        }


        return vec;
    }

    /**
     * Metoda vratí odkaz na seznam věcí, které daná věc obsahuje.
     *
     * @return seznamVeci
     */
    public String getSeznamVeci() {
        String nazvy = " ";
        for (String jmenoVeci : seznamVeci.keySet()) {
            nazvy += jmenoVeci + " ";
        }

        return nazvy;
    }

    /**
     *  Возвращает список имен вещей
     * @return Collection<String>
     */
    public Collection<String> getItemNamesInKuhr() {
       return seznamVeci.keySet();
    }
}
