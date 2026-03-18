package htl.steyr.demo.controller;

import com.google.gson.Gson;
import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.cards.Deck;
import htl.steyr.demo.cards.DeckLoader;
import htl.steyr.demo.cards.DeckReader;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class CardDeckManagerController implements Initializable {
    public Button addDeckButton;
    public TextArea jsonTextArea;
    public Label selectedDeckLabel;
    public ListView<String> deckListView;
    public Button backButton;

    private final Gson gson = new Gson();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        URL folderUrl = getClass().getResource("/htl/steyr/demo/carddecks");

        if (folderUrl == null) {
            System.out.println("Deck Ordner nicht gefunden.");
            return;
        }

        File folder;
        try {
            folder = new File(folderUrl.toURI());
        } catch (Exception e) {
            System.out.println("Fehler beim Laden des Deck-Ordners.");
            e.printStackTrace();
            return;
        }

        if (!folder.isDirectory()) {
            System.out.println("Deck Ordner ist kein Verzeichnis.");
            return;
        }

        File[] files = folder.listFiles();

        if (files == null) return;

        for (File file : files) {

            if (!file.getName().endsWith(".json")) continue;

            try {
                // FIX: StringBuilder statt String += in der Schleife
                String json = Files.readString(file.toPath());

                Deck deck = DeckReader.loadJsonDeck(json);

                if (deck != null) {
                    deckListView.getItems().add(deck.getDeckName());
                }

            } catch (Exception e) {
                System.out.println("Fehler beim Laden: " + file.getName());
                e.printStackTrace();
            }
        }
    }

    public void onAddDeckClicked(ActionEvent actionEvent) {
        /**
         * @ToDo Logik
         */
    }

    public void onBackClicked(ActionEvent actionEvent) {
        ViewSwitcher.switchTo("menu");
    }
}