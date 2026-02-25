package htl.steyr.demo.cards;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class DeckReader {

    public static Deck loadDeck(String path) {
        try {
            FileReader reader = new FileReader(path);
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

            String deckName = json.get("deck_name").getAsString();
            int kartenAnzahl = json.get("karten_anzahl").getAsInt();

            List<String> statNames = new ArrayList<>();
            json.getAsJsonArray("statistiken").forEach(s -> {
                statNames.add(s.getAsJsonObject().get("name").getAsString());
            });

            List<PlayingCard> cards = new ArrayList<>();
            json.getAsJsonArray("cards").forEach(c -> {
                PlayingCard card = new Gson().fromJson(c, PlayingCard.class);
                cards.add(card);
            });

            return new Deck(deckName, kartenAnzahl, statNames, cards);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
