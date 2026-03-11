package htl.steyr.demo.cards;

public class DeckLoader {

    private static Deck loadedDeck;

    public static void setLoadedDeck(Deck deck) {
        loadedDeck = deck;
    }

    public static Deck getLoadedDeck() {
        return loadedDeck;
    }
}
