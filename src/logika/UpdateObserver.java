package logika;

/**
 * Obnoveni observru
 */
public class UpdateObserver implements utils.Observer {
    private IHra hra;

    public UpdateObserver(IHra hra) {
        this.hra = hra;
    }

    @Override
    public void update() {
    // Volání aktualizace stavu hry
        hra.updateState();
    }

    @Override
    public void novaHra(IHra hra) {

    }
}
