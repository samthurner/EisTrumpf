package htl.steyr.demo.cards;

public class CardManager {

    public static PlayingCard compare(PlayingCard card1, PlayingCard card2, int statistic) {
        switch (statistic) {
            case 1:
                if (card1.getStat1() > card2.getStat1()) return card1;
                if (card1.getStat1() < card2.getStat1()) return card2;
                return null;

            case 2:
                if (card1.getStat2() > card2.getStat2()) return card1;
                if (card1.getStat2() < card2.getStat2()) return card2;
                return null;

            case 3:
                if (card1.getStat3() > card2.getStat3()) return card1;
                if (card1.getStat3() < card2.getStat3()) return card2;
                return null;

            case 4:
                if (card1.getStat4() > card2.getStat4()) return card1;
                if (card1.getStat4() < card2.getStat4()) return card2;
                return null;

            default:
                return null;
        }
    }
}