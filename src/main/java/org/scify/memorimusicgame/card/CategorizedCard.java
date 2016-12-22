package org.scify.memorimusicgame.card;


/**
 * CategorizedCard is a class that represents a Card with a category.
 * This card belongs to an equivalence card set
 * Created by pisaris on 10/10/2016.
 */
public class CategorizedCard extends Card {

    /**
     * the category that the cards belongs to
     */
    protected String category;
    /**
     * the equivalence Card Set identifier (hash code) that the card belongs to
     */
    protected String equivalenceCardSetHashCode;

    public String getCategory() {
        return category;
    }

    public String getEquivalenceCardSetHashCode() {
        return equivalenceCardSetHashCode;
    }

    public CategorizedCard(String label, String[] images, String[] sounds, String[] descriptiveSounds, String category, String equivalenceCardSetHashCode, String cardNameSound) {
        super(label, images, sounds, descriptiveSounds, cardNameSound);
        this.category = category;
        this.equivalenceCardSetHashCode = equivalenceCardSetHashCode;
    }
}
