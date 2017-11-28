package logika;


import java.util.ArrayList;
import java.util.Collection;

/*******************************************************************************
 * Instance třídy {@code PrikazSeber} představují ...
 *
 * @author Anton Kozinov
 * @version 26.05.2015
 */
public class PrikazSeber implements IPrikaz {
    //==Datové atributy (statické a instancí)============================
    public static final String NAZEV = "seber";
    private HerniPlan plan;
    private Batoh batoh;

    //== KONSTRUKTORY A TOVÁRNÍ METODY =========================================

    /***************************************************************************
     * Konsturktor přikazu "seber"
     * @param plan herní plán, na kterém hra beží
     * @param batoh, batoh, do kterého se vkládají věci
     */
    public PrikazSeber(HerniPlan plan, Batoh batoh) {
        this.plan = plan;
        this.batoh = batoh;
    }

    /**
     * Příkaz "seber". Sbírá věci v našem připadě pistole, klíče a nůž, a pak to uloží do batohu.
     *
     * @param parametry - jako  parametr obsahuje nazev veci
     * @return zpráva, kterou vypíše hra hráči
     */
    @Override
    public String proved(String... parametry) {
        if (parametry.length == 0) {
            // pokud chybí nazev veci
            return "Jakou vec musím sebrat? Musíš zadat nazev věci";
        }

        String jmenoVeci = parametry[0];
        Prostor aktualniProstor = plan.getAktualniProstor();
        //kontrolujeme, je v aktuálním prostoru ta věc nebo ne.
        if (aktualniProstor.obsahujeVec(jmenoVeci)) {
            //jestli aktualní prostor obsahuje věc
            //vybereme tu věc z prostoru
            Vec vec = aktualniProstor.vyberVec(jmenoVeci);
            if (vec == null) {
                return "Taková věc přenašet nemůžete!";
            } else {
                // uložíme věc, kterou sebrali do batohu
                batoh.vlozVecDoBatohu(vec);
                return "Sebral jsi " + jmenoVeci;
            }

        }

        //Tento aktualní prostor neobsauje věc
        return "Taková věc tu, bohužel není!";


    }

    /**
     * Metoda vrací název příkazu (slovo které používá hráč pro jeho vyvolání)
     *
     * @ return nazev prikazu
     */
    @Override
    public String getNazev() {
        return NAZEV;
    }

    /**
     * Возвращает список параметров для команды
     * @return Collection<String>
     */

    @Override
    public Collection<String> getParams(IHra hra) {
        return hra.getHerniPlan().getAktualniProstor().getItemNames();
    }

}
