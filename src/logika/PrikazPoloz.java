package logika;

import java.util.ArrayList;
import java.util.Collection;

/*******************************************************************************
 * Instance třídy {@code PrikazPoloz} představují ...
 * @author Anton Kozinov
 * @version 26.05.2015
 */
public class PrikazPoloz implements IPrikaz {
    //==Datové atributy (statické a instancí)============================
    public static final String NAZEV = "poloz";
    private HerniPlan plan;
    private Batoh batoh;

    //== KONSTRUKTORY A TOVÁRNÍ METODY =========================================

    /***************************************************************************
     * Konsturktor prikazu "polož", tento přikaz použivame, aby položit zbraň a klíče v kufr.
     * @param plan, herní plán, na kterém hra beží
     * @param batoh, batoh, to je ta třida, pomoci, ktrerý hrač může přenašet zbraň a klíče
     */
    public PrikazPoloz(HerniPlan plan, Batoh batoh) {
        this.plan = plan;
        this.batoh = batoh;
    }

    /**
     * Provádí příkaz "polož", tento přikaz použivame, aby položit zbraň a klíče v kufr.
     * Pokud nejsou parametry, vypíše se chybové hlášení
     *
     * @param parametry - jako  parametr obsahuje jméno veci, kterou checeme položit.
     * @return zpráva, kterou vypíše hra hráči
     */
    @Override
    public String proved(String... parametry) {
        if (parametry.length == 0) {
            // pokud chybí druhé slovo, nazev veci, kterou checeme položit
            return "Co mám vložit v kufr? Musíš zadat jméno věci";
        }

        String jmenoVeci = parametry[0];
        Prostor aktualniProstor = plan.getAktualniProstor();

        //naš kufr se nachazi v Něměcku
        if (aktualniProstor.getNazev().equals("Něměcko")) {
            Vec vec = batoh.vyberVecVBatohu(jmenoVeci);
            if (vec == null) {
                return "Taková věc není v kufru";
            }

            Vec kufr = aktualniProstor.najdiVecVProstoru("kufr");
            kufr.vlozVec(vec);
            //kontrolujeme aby v kufru byl Pistol, Nůž a Klíče.
            if (kufr.getSeznamVeci().contains("Pistole") && kufr.getSeznamVeci().contains("Nůž") && kufr.getSeznamVeci().contains("Klíče")) {
                plan.setKufr(true); //splněni tyto podminky, potřebujeme pro vitezstvi
            }

            return "Dal jste do kufru " + jmenoVeci + ".";
        }
        return "Nejsi v Něměcku, jenom tam se nachazi kufr, ve který musíš dat zbraň";

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
        if (!hra.getHerniPlan().getAktualniProstor().getNazev().equals("Něměcko")) {
            return null;
        }

        return batoh.getSeznamVeci().keySet();

    }

}
