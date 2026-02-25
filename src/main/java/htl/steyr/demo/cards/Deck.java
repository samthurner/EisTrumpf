package htl.steyr.demo.cards;

import java.util.List;

public class Deck {

    private String deckName;
    private int kartenAnzahl;
    private List<String> statNames;
    private List<PlayingCard> cards;

    public Deck(String deckName, int kartenAnzahl, List<String> statNames, List<PlayingCard> cards) {
        this.deckName = deckName;
        this.kartenAnzahl = kartenAnzahl;
        this.statNames = statNames;
        this.cards = cards;
    }

    public String getDeckName() {
        return deckName;
    }

    public int getKartenAnzahl() {
        return kartenAnzahl;
    }

    public List<String> getStatNames() {
        return statNames;
    }

    public List<PlayingCard> getCards() {
        return cards;
    }
}
