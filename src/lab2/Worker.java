package lab2;

import java.util.List;

/**
 * Класс с публичными, защищёнными и приватными методами. Часть из них помечена {@link Repeat}.
 */
public class Worker {

    // ---------- public ----------

    @Repeat(5)
    public void greet(String name) {
        System.out.println("  public greet: привет, " + name);
    }

    public int sum(int a, int b) {
        return a + b;
    }

    public String describe(double value, boolean precise) {
        return precise ? String.format("%.4f", value) : String.valueOf(Math.round(value));
    }

    // ---------- protected ----------

    @Repeat(2)
    protected double scale(double value, double factor) {
        double result = value * factor;
        System.out.println("  protected scale: " + value + " * " + factor + " = " + result);
        return result;
    }

    @Repeat(1)
    protected String joinWords(String first, String second, String separator) {
        String result = first + separator + second;
        System.out.println("  protected joinWords: " + result);
        return result;
    }

    protected int countItems(List<String> items) {
        System.out.println("  protected countItems: " + items.size());
        return items.size();
    }

    // ---------- private ----------

    @Repeat(3)
    private void repeatChar(char symbol, int count) {
        System.out.println("  private repeatChar: " + String.valueOf(symbol).repeat(count));
    }

    @Repeat(2)
    private Player promote(Player player, int levels) {
        Player promoted = new Player(player.name(), player.level() + levels);
        System.out.println("  private promote: " + player + " -> " + promoted);
        return promoted;
    }

    private boolean isEven(long number) {
        return number % 2 == 0;
    }
}
