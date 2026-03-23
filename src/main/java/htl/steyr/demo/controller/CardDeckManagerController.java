package htl.steyr.demo.controller;

import com.google.gson.Gson;
import htl.steyr.demo.ViewSwitcher;
import htl.steyr.demo.cards.Deck;
import htl.steyr.demo.cards.DeckReader;
import htl.steyr.demo.userdata.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    public URL stdFolderUrl = getClass().getResource("/htl/steyr/demo/carddecks");
    public File userDecksFolder = new File("Json/decks");

    private final Gson gson = new Gson();
    private final Map<String, String> deckNameToFile = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        if (stdFolderUrl == null) {
            System.out.println("Deck Ordner nicht gefunden.");
            return;
        }

        File folder;
        try {
            folder = new File(stdFolderUrl.toURI());
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

        File[] userDecks = userDecksFolder.listFiles();
        if (userDecks == null) return;

        for (File userDeck : userDecks) {
            if (!userDeck.getName().endsWith(".json")) continue;

            try {
                String json = Files.readString(userDeck.toPath());
                Deck deck = DeckReader.loadJsonDeck(json);

                if (deck != null) {
                    deckNameToFile.put(deck.getDeckName(), userDeck.getName());
                    deckListView.getItems().add(deck.getDeckName());
                }
            } catch (Exception e) {
                System.out.println("Fehler beim Laden: " + userDeck.getName());
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
        if (!jsonTextArea.getText().isEmpty()) {
            String userDeckString = jsonTextArea.getText();

            Deck deck = DeckReader.loadJsonDeck(userDeckString);
            if (deck != null) {
                if (deck != null) {
                    try {
                        String fileName = deck.getDeckName().replaceAll("\\s+", "_").toLowerCase() + "_deck.json";


                        File directory = new File("json/decks");

                        if(Files.notExists(directory.toPath())) {
                            Files.createDirectory(directory.toPath());
                        }

                        File newDeckFile = new File(directory, fileName);

                        Files.writeString(newDeckFile.toPath(), userDeckString);

                        if (!deckNameToFile.containsKey(deck.getDeckName())) {
                            deckNameToFile.put(deck.getDeckName(), fileName);
                            deckListView.getItems().add(deck.getDeckName());
                        }

                        jsonTextArea.clear();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Erfolg!");
                        alert.setHeaderText("Deck wurde erfolgreich hinzugefügt.");
                        alert.setContentText("Das Deck \"" + deck.getDeckName() + "\" ist jetzt verfügbar.");
                        alert.showAndWait();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Fehler beim Speichern");
                        alert.setHeaderText("Das Deck konnte nicht gespeichert werden.");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Fehler.");
                    alert.setHeaderText("Die Jsondatei zu deinem Deck funktioniert nicht.");
                    alert.setContentText("In der ReadMe auf GitHub ist eine genauere Erklärung, wie eine Jsondatei für ein Deck aussehen muss.");
                    alert.showAndWait();
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Fehler: Textarea ist leer!");
                alert.setHeaderText("Füge bitte ein Json-Deck in das Textfeld ein.");
                alert.showAndWait();
            }

        }
    }

    public void onBackClicked(ActionEvent actionEvent) {
        ViewSwitcher.switchTo("menu");
    }
}