package logika;

import java.util.*;

/**
 * Trida Prostor - popisuje jednotlive prostory (mistnosti) hry
 * <p>
 * Tato trida je soucasti jednoduche textove hry.
 * <p>
 * "Prostor" reprezentuje jedno misto (mistnost, prostor, ..) ve scenari hry.
 * Prostor muze mit sousedni prostory pripojene pres vychody. Pro kazdy vychod
 * si prostor uklada odkaz na sousedici prostor.
 *
 * @author Michael Kolling, Lubos Pavlicek, Jarmila Pavlickova
 * @version pro skolni rok 2014/2015
 */
public class Prostor {

    public static Hra hra;
    private String nazev;
    private String popis;
    private Set<Prostor> vychody;   // obsahuje sousedni mistnosti
    private Map<String, Vec> seznamVeci;
    private int brilliantsCount;
    private int learnTryingsCount = 3;
    private double posX;
    private double posY;


    /**
     * Vytvoreni prostoru se zadanym popisem, napr. "kuchyn", "hala", "travnik
     * pred domem"
     *
     * @param nazev nazev prostoru, jednoznacny identifikator, jedno slovo nebo
     *              viceslovny nazev bez mezer.
     * @param popis Popis prostoru.
     */
    public Prostor(String nazev, String popis, double posX, double posY) {
        this.nazev = nazev;
        this.popis = popis;
        this.posX = posX;
        this.posY = posY;

        vychody = new HashSet<>();
        seznamVeci = new HashMap<>();
        Random random = new Random();
        brilliantsCount = random.nextInt(3) + 1;
    }

    /**
     * Definuje vychod z prostoru (sousedni/vedlejsi prostor). Vzhledem k tomu,
     * ze je pouzit Set pro ulozeni vychodu, muze byt sousedni prostor uveden
     * pouze jednou (tj. nelze mit dvoje dvere do stejne sousedni mistnosti).
     * Druhe zadani stejneho prostoru tise prepise predchozi zadani (neobjevi se
     * zadne chybove hlaseni). Lze zadat tez cestu ze do sebe sama.
     *
     * @param vedlejsi prostor, ktery sousedi s aktualnim prostorem.
     */
    public void setVychod(Prostor vedlejsi) {
        vychody.add(vedlejsi);
    }

    /**
     * Metoda equals pro porovnani dvou prostoru. Prekryva se metoda equals ze
     * tridy Object. Dva prostory jsou shodne, pokud maji stejny nazev. Tato
     * metoda je dulezita z hlediska spravneho fungovani seznamu vychodu (Set).
     * <p>
     * Blizsi popis metody equals je u tridy Object.
     *
     * @param o object, ktery se ma porovnavat s aktualnim
     * @return hodnotu true, pokud ma zadany prostor stejny nazev, jinak false
     */
    @Override
    public boolean equals(Object o) {
        // porovnavame zda se nejedna o dva odkazy na stejnou instanci
        if (this == o) {
            return true;
        }
        // porovnavame jakeho typu je parametr 
        if (!(o instanceof Prostor)) {
            return false;    // pokud parametr neni typu Prostor, vratime false
        }
        // pretypujeme parametr na typ Prostor 
        Prostor druhy = (Prostor) o;

        //metoda equals tridy java.util.Objects porovna hodnoty obou nazvu. 
        //Vrati true pro stejne nazvy a i v pripade, ze jsou oba nazvy null,
        //jinak vrati false.

        return (java.util.Objects.equals(this.nazev, druhy.nazev));
    }

    /**
     * metoda hashCode vraci ciselny identifikator instance, ktery se pouziva
     * pro optimalizaci ukladani v dynamickych datovych strukturach. Pri
     * prekryti metody equals je potreba prekryt i metodu hashCode. Podrobny
     * popis pravidel pro vytvareni metody hashCode je u metody hashCode ve
     * tride Object
     */
    @Override
    public int hashCode() {
        int vysledek = 3;
        int hashNazvu = java.util.Objects.hashCode(this.nazev);
        vysledek = 37 * vysledek + hashNazvu;
        return vysledek;
    }

    /**
     * Vraci nazev prostoru (byl zadan pri vytvareni prostoru jako parametr
     * konstruktoru)
     *
     * @return nazev prostoru
     */
    public String getNazev() {
        return nazev;
    }

    /**
     * Vraci "dlouhy" popis prostoru, ktery muze vypadat nasledovne: Jsi v
     * mistnosti/prostoru vstupni hala budovy VSE na Jiznim meste. vychody:
     * chodba bufet ucebna
     *
     * @return Dlouhy popis prostoru
     */
    public String dlouhyPopis() {
        return "Jsi v statě " + popis + ".\n" + popisVychodu() + "\n" + nazvyVeci() + "\n" + "Tady " + getBrilliantsCount() + " diamantů." + "\n" + "Jste uz mate " + hra.getBrilliantsCount();
    }

    /**
     * Vraci textovy retezec, ktery popisuje sousedni vychody, napriklad:
     * "vychody: hala ".
     *
     * @return Popis vychodu - nazvu sousednich prostoru
     */
    private String popisVychodu() {
        String vracenyText = "Muzete jet do:";
        for (Prostor sousedni : vychody) {
            vracenyText += " " + sousedni.getNazev();
        }
        return vracenyText;
    }

    /**
     * Vraci prostor, ktery sousedi s aktualnim prostorem a jehoz nazev je zadan
     * jako parametr. Pokud prostor s udanym jmenem nesousedi s aktualnim
     * prostorem, vraci se hodnota null.
     *
     * @param nazevSouseda Jmeno sousedniho prostoru (vychodu)
     * @return Prostor, ktery se nachazi za prislusnym vychodem, nebo hodnota
     * null, pokud prostor zadaneho jmena neni sousedem.
     */
    public Prostor vratSousedniProstor(String nazevSouseda) {
        if (nazevSouseda == null) {
            return null;
        }
        for (Prostor sousedni : vychody) {
            if (sousedni.getNazev().equals(nazevSouseda)) {
                return sousedni;
            }
        }
        return null;  // prostor nenalezen
    }

    /**
     * Vraci kolekci obsahujici prostory, se kterymi tento prostor sousedi.
     * Takto ziskany seznam sousednich prostor nelze upravovat (pridavat,
     * odebirat vychody) protoze z hlediska spravneho navrhu je to plne
     * zalezitosti tridy Prostor.
     *
     * @return Nemodifikovatelna kolekce prostoru (vychodu), se kterymi tento
     * prostor sousedi.
     */
    public Collection<Prostor> getVychody() {
        return Collections.unmodifiableCollection(vychody);

    }

    /**
     * Vloži vec do seznamu
     */
    public void vlozVec(Vec vec) {
        seznamVeci.put(vec.getJmeno(), vec);
    }

    /**
     * Vebere vec ze seznamu
     */
    public Vec vyberVec(String jmenoVeci) {
        Vec nalezenaVec;
        if (seznamVeci.containsKey(jmenoVeci)) {
            nalezenaVec = seznamVeci.get(jmenoVeci);
            if (nalezenaVec.jePrenositelna()) {
                seznamVeci.remove(jmenoVeci);
                return nalezenaVec;
            }
            return null;
        }
        return null;
    }

    /**
     * Metoda ObsahujeVec
     */

    public boolean obsahujeVec(String jmenoVeci) {
        return seznamVeci.containsKey(jmenoVeci);
    }

    /**
     * Vratí nazvy věci
     */
    public String nazvyVeci() {
        String nazvy = "Veci: ";
        for (String jmenoVeci : seznamVeci.keySet()) {
            nazvy += jmenoVeci + " ";
        }
        return nazvy;
    }

    public Collection<String> getItemNames() {
        return seznamVeci.keySet();
    }

    /**
     * Pocet kolik je briliant v konkretnim state, kde jsme nachazime. Kdyz zabereme ze statu nekolik brilliant, pocet brilliant zmensi.
     */

    public int getBrilliantsCount() {
        return brilliantsCount;
    }

    /**
     * Pomoci tyto metody, dovame do statu briiliant pomoci fce. Random. (brilliantsCount = random.nextInt(3) + 1;)
     */
    public void setBrilliantsCount(int brilliantsCount) {
        this.brilliantsCount = brilliantsCount;
    }

    /**
     * GET: Pocet moznosti kolik krat hrac muze udelat pokusu
     */

    public int getLearnTryingsCount() {
        return learnTryingsCount;
    }

    /**
     * SET: Pocet moznosti kolik krat hrac muze udelat pokusu, aby naucil jazyk. (private int learnTryingsCount = 3;)
     */
    public void setLearnTryingsCount(int learnTryingsCount) {
        this.learnTryingsCount = learnTryingsCount;
    }

    /**
     * Metoda najde jen veci v prostoru, ne veci ve vecech.
     *
     * @return vrati nalezenou vec v prostoru.
     */

    public Vec najdiVecVProstoru(String jmeno) {
        return seznamVeci.get(jmeno);
    }

    /**
     * @return the posX
     */
    public double getPosX() {
        return posX;
    }

    /**
     * @return the posY
     */
    public double getPosY() {
        return posY;
    }

}
