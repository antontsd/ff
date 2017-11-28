package logika;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

/*******************************************************************************
 * Instance tridy {@code PrikazLearn} predstavuji ...
 *
 * @author Anton Kozinov
 * @version 26.05.2015
 */
public class PrikazLearn implements IPrikaz {
    //==Datove atributy (staticke a instanci)============================
    private static final String NAZEV = "nauč";
    private HerniPlan plan;
    private Boolean isLearnt = false;

    //== KONSTRUKTORY A TOVARNI METODY =========================================

    /***************************************************************************
     * Konsturktor prikazu "nauc"
     * @param plan, herni plan, na kterem hra bezi
     */
    public PrikazLearn(HerniPlan plan) {
        this.plan = plan;
    }

    /**
     * Provadi prikaz "nauc", tento prikaz pouzivame, pro uceni jazyku, aby meli shopnost prochazet staty.
     * Pokud nejsou parametry, vypise se chybove hlaseni
     *
     * @param parametry - jako parametr obsahuje nazev statu, jazyk ktereho checme naucit.
     * @return zprava, kterou vypise hra hraci
     */
    @Override
    public String proved(String... parametry) {

        if (isLearnt == true) {
            return "Uz jste se naucil tento jazyk!";
        }

        if (parametry.length == 0) {
            // pokud chybi nazev statu
            return "Co mam naucit? Musis zadat nazev statu, ktereho jazyk chcete naucit.";
        }
        Random rand = new Random();
        HashMap<String, Prostor> learntProstoryCollection = (HashMap<String, Prostor>) plan.getLearntProstoryCollection();
        HashMap<String, Prostor> prostoryCollection = (HashMap<String, Prostor>) plan.getProstoryCollection();
        Prostor currentProstor = prostoryCollection.get(parametry[0]);

        if (currentProstor != null) {
            if (learntProstoryCollection.get(parametry[0]) != null) {
                return parametry[0] + " jiz naucil!";
            } else {

                //Kontrolujeme aby pocet moznosti naucit jazyk byl vetsi nez 0, jazyk ucime nahodne (rand.nextInt(2) == 1).

                if (parametry.length == 1 && rand.nextInt(2) == 1 && currentProstor.getLearnTryingsCount() > 0) {
                    learntProstoryCollection.put(currentProstor.getNazev(), currentProstor);
                    return "Odpoved je spravná! Ty se naucil " + parametry[0];
                } else {
                    Integer learnTryingsCount = currentProstor.getLearnTryingsCount();
                    if (learnTryingsCount > 0) {
                        learnTryingsCount = currentProstor.getLearnTryingsCount() - 1;
                        currentProstor.setLearnTryingsCount(learnTryingsCount);
                        return "Bohuzel ne naucil jste jazyk. Mas jeste " + learnTryingsCount.toString() + " pokusu aby naucit!";
                    } else {
                        return "Nemate uz pokusu. Muzete si otevrit stat pomoci diamantu!";
                    }

                }
            }
        } else {
            return "Nazev statu neni spravny!";
        }


    }

    /**
     * Metoda vraci nazev prikazu (slovo ktere pouziva hrac pro jeho vyvolani)
     *
     * @ return nazev prikazu
     */
    @Override
    public String getNazev() {
        return NAZEV;
    }

    /**
     * Возвращает список параметров для команды
     * @param IHra
     * @return Collection<String>
     */
    @Override
    public Collection<String> getParams(IHra hra) {
        return hra.getHerniPlan().getProstoryCollection().keySet();
    }

}
