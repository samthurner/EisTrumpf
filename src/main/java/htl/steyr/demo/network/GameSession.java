package htl.steyr.demo.network;

import htl.steyr.demo.cards.PlayingCard;

import java.util.ArrayList;
import java.util.List;

public class GameSession {

    private static List<PlayingCard> hand = new ArrayList<>();

    public static void addCard(PlayingCard card) {
        hand.add(card);
    }

    public static List<PlayingCard> getHand() {
        return hand;
    }
}
