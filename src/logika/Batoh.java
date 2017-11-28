package logika;

import java.util.HashMap;
import java.util.Map;

/*******************************************************************************
 * Instance třídy {@code Batoh} představují ...
 *
 * @author Anton Kozinov
 * @version 26.05.2015
 */
public class Batoh {
    //== Datové atributy (statické i )==========================================
    private static final int KAPACITA = 3;
    private Map<String, Vec> seznamVeci;
    //== KONSTRUKTORY A TOVÁRNÍ METODY =========================================

    /***************************************************************************
     * Konstruktor seznam věci, které jsou v batohu
     */
    public Batoh() {
        seznamVeci = new HashMap<>();
    }

    //== Nesoukromé metody=====================================================

    /**
     * Tento třida umožnuje vložit vecí (pistole, nůž a kliče) do batohu, aby to pak dát do kufru.
     *
     * @param objekt třídy Vec
     * @return true pokus věc vloží, false, pokud ne vloži
     */
    public boolean vlozVecDoBatohu(Vec vec) {

        if (seznamVeci.size() < KAPACITA) {
            seznamVeci.put(vec.getJmeno(), vec);
            return true;
        }
        return false;

    }

    /**
     * Function return player items
     * @return
     */
    public Map<String, Vec> getSeznamVeci() {
        return seznamVeci;
    }


    /**
     * Vratí seznam věci, které už je v batohu
     */
    public boolean obsahujeVecVBatohu(String jmenoVeci) {
        return seznamVeci.containsKey(jmenoVeci);
    }

    /**
     * Vrátí věc z batohu
     */
    public Vec vyberVecVBatohu(String jmenoVeci) {
        Vec nalezenaVec;
        if (seznamVeci.containsKey(jmenoVeci)) {
            nalezenaVec = seznamVeci.get(jmenoVeci);
            seznamVeci.remove(jmenoVeci);
            return nalezenaVec;
        }
        return null;
    }

    /**
     * Vypíše seznam věci, které jsou v batohu
     */
    public String nazvyVeciVBatohu() {
        String nazvy = "věci v batohu: ";
        for (String jmenoVeci : seznamVeci.keySet()) {
            nazvy += jmenoVeci + " ";
        }
        return nazvy;

    }

    /**
     * Vypíše kapacitu batohu
     */
    public int getKapacitaBatohu() {
        return KAPACITA;

    }
}

