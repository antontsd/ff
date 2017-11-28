package logika;

import java.util.Collection;

/**
 * Třída PrikazNapoveda implementuje pro hru příkaz napoveda.
 * Tato třída je součástí jednoduché textové hry.
 *
 * @author Jarmila Pavlickova, Luboš Pavlíček
 * @version pro školní rok 2014/2015
 */
class PrikazNapoveda implements IPrikaz {

    private static final String NAZEV = "napoveda";
    private SeznamPrikazu platnePrikazy;


    /**
     * Konstruktor třídy
     *
     * @param platnePrikazy seznam příkazů,
     *                      které je možné ve hře použít,
     *                      aby je nápověda mohla zobrazit uživateli.
     */
    public PrikazNapoveda(SeznamPrikazu platnePrikazy) {
        this.platnePrikazy = platnePrikazy;
    }

    /**
     * Vrací základní nápovědu po zadání příkazu "napoveda". Nyní se vypisuje
     * vcelku primitivní zpráva a seznam dostupných příkazů.
     *
     * @return napoveda ke hre
     */
    @Override
    public String proved(String... parametry) {
        return "Tvým úkolem je najit útraceného špiona v Evropě, ale jěště předtím\n"
                + "musíš najit Nůž, Pistole a kliče, a dat to do kufru v Něměcku.\n"
                + "\n"
                + "Můžeš zadat tyto příkazy:\n"
                + platnePrikazy.vratNazvyPrikazu();
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
