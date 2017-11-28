package logika;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Instance třídy {@code PrikazTakeBrilliant} představují ...
 *
 * @author Anton Kozinov
 * @version 26.05.2015
 */
public class PrikazTakeBrilliant implements IPrikaz {
    public static final String NAZEV = "vzit";
    private Hra hra;

    /**
     * Konsturktor prikazu "vzit", tento přikaz použivame, aby vzít diamanty.
     *
     * @param hra, ta hra, která pravě beží
     */
    public PrikazTakeBrilliant(Hra hra) {
        this.hra = hra;
    }

    /**
     * Přikaz "vzit" umožnuje sbírat diamanty v ryzných statěch. Tady provademe kontrolu,
     * aby hrač něměl moct sebrat diamantů větší něž stát má.
     * Pokud hrač napiší spravý počet diamantů (může to byt i měnší, něž stát nabizi),
     * které chtěl bych vzit, vypiše mu kolik sebral diamantů a kolik zůstalo ve statě.
     *
     * @param parametry - jako  parametr počet diamantů
     * @return zpráva, kterou vypíše hra hráči
     */

    @Override
    public String proved(String... parametry) {
//        if (parametry.length == 1) {
            HashMap<String, Prostor> prostoryCollection = (HashMap<String, Prostor>) hra.getHerniPlan().getProstoryCollection();
            HerniPlan plan = hra.getHerniPlan();
            Prostor currentProstor = plan.getAktualniProstor();
            String info;
            if (currentProstor != null) {
                String countryName = currentProstor.getNazev();
                Integer countryBrilliantsCount = currentProstor.getBrilliantsCount();
                try {
//                    Integer brilliantsCount = Integer.valueOf(parametry[0]);
                    if (countryBrilliantsCount == 0) {
//                    if (brilliantsCount.compareTo(countryBrilliantsCount) > 0) {
                        info = "Zadali jste počet diamantů větší než tady je!";
                    } else {
                        Integer hraBrilliantsCount = hra.getBrilliantsCount();
                        hra.setBrilliantsCount(Integer.valueOf(countryBrilliantsCount) + Integer.valueOf(hraBrilliantsCount));
                        hraBrilliantsCount = hra.getBrilliantsCount();
                        currentProstor.setBrilliantsCount(0);
//                        currentProstor.setBrilliantsCount(Integer.valueOf(countryBrilliantsCount) - Integer.valueOf(brilliantsCount));
                        info = "Jste mate " + hraBrilliantsCount.toString() + " diamantů, ve statě " + countryName + " zustalo " + currentProstor.getBrilliantsCount();
                    }
                } catch (NumberFormatException e) {
                    info = "Číslo počtu diamantů není správné!";
                }

            } else {
                info = "Stát nebyl nalezen!";
            }

            return info;
//        } else {
//
//            return "Chyba! Počet parametrů není správný!";
//        }
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
        return null;
    }
}
