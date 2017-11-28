package logika;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Třída PrikazJdi implementuje pro hru příkaz jdi.
 * Tato třída je součástí jednoduché textové hry.
 *
 * @author Jarmila Pavlickova, Luboš Pavlíček
 * @version pro školní rok 2014/2015
 */
class PrikazJdi implements IPrikaz {
    private static final String NAZEV = "jdi";
    private HerniPlan plan;
    private Hra hra;

    /**
     * Konstruktor třídy
     *
     * @param plan herní plán, ve kterém se bude ve hře "chodit"
     */
    public PrikazJdi(HerniPlan plan) {
        this.plan = plan;
    }

    /**
     * Provádí příkaz "jdi". Zkouší se vyjít do zadaného prostoru. Pokud prostor
     * existuje, vstoupí se do nového prostoru. Pokud zadaný sousední prostor
     * (východ) není, vypíše se chybové hlášení.
     *
     * @param parametry - jako  parametr obsahuje jméno prostoru (východu),
     *                  do kterého se má jít.
     * @return zpráva, kterou vypíše hra hráči
     */
    @Override
    public String proved(String... parametry) {
        if (parametry.length == 0) {

            return "Počet argumentů není správný!";
        }

        String smer = parametry[0];

        System.out.println(smer);

        HashMap<String, Prostor> learntProstoryCollection = (HashMap<String, Prostor>) plan.getLearntProstoryCollection();
        HashMap<String, Prostor> prostoryCollection = (HashMap<String, Prostor>) plan.getProstoryCollection();
        Prostor currentProstor = prostoryCollection.get(parametry[0]);
        String info = "";

        if (currentProstor != null) {
            if (learntProstoryCollection.get(smer) == null) {
                int brilliantsCount = hra.getBrilliantsCount();
                if (brilliantsCount > 4) {
                    hra.setBrilliantsCount(brilliantsCount - 5);
                    learntProstoryCollection.put(currentProstor.getNazev(), currentProstor);
                    info += "Jste zaplatil brillianty, aby otevřit " + smer + "!";
                } else {
                    return "Jste jěště nenaučili jazyk pro " + smer + "! Musite jste" +"\n"+ "sebrat 5 brilliantů pro otevřeni statu a nebo naučit jazyk!";
                }
            }
        } else {
            return "Nazev statu není spravý!";
        }


        // zkoušíme přejít do sousedního prostoru
        Prostor sousedniProstor = plan.getAktualniProstor().vratSousedniProstor(smer);

        if (sousedniProstor == null) {
            info += " Tam se odsud jít nedá!";
            return info;
        } else {
            plan.setAktualniProstor(sousedniProstor);
            info += sousedniProstor.dlouhyPopis();
            return info;
        }
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
        Prostor prostor = hra.getHerniPlan().getAktualniProstor();
        Collection<String> countries = new ArrayList<String>();

        if (prostor != null) {
            prostor.getVychody().forEach(item -> {
                countries.add(item.getNazev());
            });
        }

        return countries;
    }

    public Hra getHra() {
        return hra;
    }

    public void setHra(Hra hra) {
        this.hra = hra;
    }

}
