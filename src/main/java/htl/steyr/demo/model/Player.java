package htl.steyr.demo.model;

import htl.steyr.demo.cards.PlayingCard;

import java.util.List;

public class Player {
    private String name;
    private int cardsOnHand;
    private PlayingCard playingCard;
    private List<PlayingCard> hand;

    public Player(String name, int score, List<PlayingCard> hand) {

        this.hand = hand;
    }

    public void addCard(PlayingCard card) {
        hand.add(card);
    }

    public PlayingCard getPlayingCard() {
        return playingCard;
    }

    public void setPlayingCard(PlayingCard playingCard) {
        this.playingCard = playingCard;
    }

    public int getCardsOnHand() {
        return cardsOnHand;
    }

    public void setCardsOnHand(int cardsOnHand) {
        this.cardsOnHand = cardsOnHand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
