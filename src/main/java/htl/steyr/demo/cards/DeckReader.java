package htl.steyr.demo.cards;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DeckReader {

    // Lädt ein Deck aus einem Ressourcen-Pfad
    public static Deck loadDeck(String path) {
        try {
            // path = Pfad zur JSON-Datei im Ressourcenordner
            InputStream stream = DeckReader.class.getResourceAsStream(path);
            return loadDeck(stream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Lädt ein Deck aus einem InputStream (z.B. Datei oder Resource)
    public static Deck loadDeck(InputStream is) {
        try {
            // is = Datenstrom der JSON-Datei
            InputStreamReader reader = new InputStreamReader(is);

            // JSON wird eingelesen und als Objekt gespeichert
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

            // Basisdaten auslesen
            String deckName = json.get("deck_name").getAsString();
            int kartenAnzahl = json.get("karten_anzahl").getAsInt();

            // Statistik-Namen sammeln
            List<String> statNames = new ArrayList<>();
            json.getAsJsonArray("statistiken").forEach(s -> {
                statNames.add(s.getAsJsonObject().get("name").getAsString());
            });

            // Karten erstellen und in Liste speichern
            List<PlayingCard> cards = new ArrayList<>();
            json.getAsJsonArray("cards").forEach(c -> {
                PlayingCard card = new Gson().fromJson(c, PlayingCard.class);
                cards.add(card);
            });

            // Neues Deck-Objekt zurückgeben
            return new Deck(deckName, kartenAnzahl, statNames, cards);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Lädt ein Deck direkt aus einem JSON-String
    public static Deck loadJsonDeck(String deck) {
        try {
            // deck = JSON-String mit Deck-Daten
            JsonObject json = new JsonParser().parse(deck).getAsJsonObject();

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