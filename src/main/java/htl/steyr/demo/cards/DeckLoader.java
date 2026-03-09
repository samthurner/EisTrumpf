package htl.steyr.demo.cards;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DeckLoader {

    private static Deck loadedDeck;

    public static List<Deck> loadAllDecks() {

        List<Deck> decks = new ArrayList<>();
        Gson gson = new Gson();

        String[] deckFiles = {
                "auto_deck.json",
                "fussballer_deck.json",
                "htl-lehrer_deck.json",
                "tier_deck.json"
        };

        for(String file : deckFiles) {

            InputStream stream = DeckLoader.class.getResourceAsStream("/htl/steyr/demo/carddecks/" + file);

            Deck deck = gson.fromJson(
                    new InputStreamReader(stream),
                    Deck.class
            );

            decks.add(deck);
        }

        return decks;
    }

    public static void setLoadedDeck(Deck deck) {
        loadedDeck = deck;
    }

    public static Deck getLoadedDeck() {
        return loadedDeck;
    }
}
