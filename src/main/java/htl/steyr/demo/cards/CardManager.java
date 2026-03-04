package htl.steyr.demo.cards;


import java.util.Collections;
import java.util.List;

public class CardManager {
    public static PlayingCard compare(PlayingCard card1, PlayingCard card2, int statistic) throws Exception {
        switch (statistic) {
            case 1:
                if (card1.getStat1() > card2.getStat1()) {
                    return card1;
                } else if (card1.getStat1() < card2.getStat1()) {
                    return card2;
                }
                break;
            case 2:
                if (card1.getStat2() > card2.getStat2()) {
                    return card1;
                } else if (card1.getStat2() < card2.getStat2()) {
                    return card2;
                }
                break;
            case 3:
                if (card1.getStat3() > card2.getStat3()) {
                    return card1;
                }
                break;
            case 4:
                if (card1.getStat4() > card2.getStat4()) {
                    return card2;
                } else if (card1.getStat4() < card2.getStat4()) {
                    return card1;
                }
                break;
        }
        throw new Exception("Es werden keine Richtigen karten Überliefert.");
    }

    public static void shuffleDeck(Deck deck) {
        List<PlayingCard> cards = deck.getCards();
        deck.getCards().clear();

        Collections.shuffle(cards);

        deck.setCards(cards);
    }
}
