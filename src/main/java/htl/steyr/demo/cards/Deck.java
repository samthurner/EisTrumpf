package htl.steyr.demo.cards;

import java.util.List;

public class Deck {

    // Name des Decks (z.B. Thema)
    private String deckName;

    // Anzahl der Karten im Deck
    private int kartenAnzahl;

    // Namen der Statistiken (z.B. Stärke, Geschwindigkeit)
    private List<String> statNames;

    // Liste aller Karten im Deck
    private List<PlayingCard> cards;

    // Konstruktor zum Erstellen eines Decks mit allen Daten
    public Deck(String deckName, int kartenAnzahl, List<String> statNames, List<PlayingCard> cards) {
        this.deckName = deckName;
        this.kartenAnzahl = kartenAnzahl;
        this.statNames = statNames;
        this.cards = cards;
    }

    // Gibt den Namen des Decks zurück
    public String getDeckName() {
        return deckName;
    }

    // Gibt die Anzahl der Karten zurück
    public int getKartenAnzahl() {
        return kartenAnzahl;
    }

    // Gibt die Statistik-Namen zurück
    public List<String> getStatNames() {
        return statNames;
    }

    // Gibt die Kartenliste zurück
    public List<PlayingCard> getCards() {
        return cards;
    }

    // Setzt eine neue Kartenliste
    public void setCards(List<PlayingCard> cards) {
        this.cards = cards;
    }
}