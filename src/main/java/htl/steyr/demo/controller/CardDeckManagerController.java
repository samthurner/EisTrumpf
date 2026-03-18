package htl.steyr.demo.controller;

import com.google.gson.Gson;
import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.cards.Deck;
import htl.steyr.demo.cards.DeckReader;
import htl.steyr.demo.userdata.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class CardDeckManagerController implements Initializable {
    public Button addDeckButton;
    public TextArea jsonTextArea;
    public Label selectedDeckLabel;
    public ListView<String> deckListView;
    public Button backButton;

    private final Gson gson = new Gson();
    private final Map<String, String> deckNameToFile = new HashMap<>();

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

        if (!folder.isDirectory()) return;

        File[] files = folder.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (!file.getName().endsWith(".json")) continue;

            try {
                String json = Files.readString(file.toPath());
                Deck deck = DeckReader.loadJsonDeck(json);

                if (deck != null) {
                    deckNameToFile.put(deck.getDeckName(), file.getName());
                    deckListView.getItems().add(deck.getDeckName());
                }
            } catch (Exception e) {
                System.out.println("Fehler beim Laden: " + file.getName());
                e.printStackTrace();
            }
        }

        String currentFile = UserSession.getUserData().getSelectedDeck();
        if (currentFile != null && !currentFile.isEmpty()) {

            String displayName = deckNameToFile.entrySet().stream()
                    .filter(e -> e.getValue().equals(currentFile))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(currentFile);
            selectedDeckLabel.setText("Ausgewählt: " + displayName);
        } else {
            selectedDeckLabel.setText("Kein Deck ausgewählt");
        }

        // Doppelklick-Listener
        deckListView.setOnMouseClicked(this::onDeckViewClicked);
    }

    public void onDeckViewClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            String deckName = deckListView.getSelectionModel().getSelectedItem();
            if (deckName != null) {
                String fileName = deckNameToFile.get(deckName);
                UserSession.getUserData().setSelectedDeck(fileName);
                selectedDeckLabel.setText("Ausgewählt: " + deckName);
            }
        }
    }

    public void onAddDeckClicked(ActionEvent actionEvent) {
        // @ToDo Logik
    }

    public void onBackClicked(ActionEvent actionEvent) {
        ViewSwitcher.switchTo("menu");
    }
}