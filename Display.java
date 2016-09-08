import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.border.TitledBorder;


public class Foothill 
{
   static int NUM_CARDS_PER_HAND = 7;
   static int  NUM_PLAYERS = 2;
   static JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
   static JLabel[] humanLabels = new JLabel[NUM_CARDS_PER_HAND];  
   static JLabel[] playedCardLabels  = new JLabel[NUM_PLAYERS]; 
   static JLabel[] playLabelText  = new JLabel[NUM_PLAYERS]; 
   
   public static void main(String[] args)
   {
      int k;
      Icon tempIcon;
      
      //Instantiate CardGameFramework object
      int numPacksPerDeck = 1;
      int numJokersPerPack = 0;
      int numUnusedCardsPerPack = 0;
      Card[] unusedCardsPerPack = null;
      
      CardGameFramework toyGame = new CardGameFramework( 
            numPacksPerDeck, numJokersPerPack,  
            numUnusedCardsPerPack, unusedCardsPerPack, 
            NUM_PLAYERS, NUM_CARDS_PER_HAND);
      
      //deal
      toyGame.deal();
      
      // establish main frame in which program will run
      CardTable myCardTable 
         = new CardTable("CS 1B CardTable", NUM_CARDS_PER_HAND, NUM_PLAYERS);
      myCardTable.setSize(800, 600);
      myCardTable.setLocationRelativeTo(null);
      myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // show everything to the user
      myCardTable.setVisible(true);

      
      // CREATE LABELS ----------------------------------------------------
      GUICard graphic = new GUICard(); //called to fire constructor
      
      for (int i = 0; i < NUM_CARDS_PER_HAND; i++)
      {
         humanLabels[i] = new JLabel(GUICard.getIcon(toyGame.getHand(1).inspectCard(i)));
         computerLabels[i] = new JLabel(GUICard.getBackCardIcon());
      }
      
      for (int j = 0; j < NUM_PLAYERS; j++)
         playedCardLabels[j] = new JLabel(GUICard.getIcon(generateRandomCard()));
      
      playLabelText[0] = 
            new JLabel("Computer's Played Card", JLabel.CENTER);
      playLabelText[1] = 
            new JLabel("Your Played Card", JLabel.CENTER);
      
      // ADD LABELS TO PANELS -----------------------------------------
      
      //distribute the icons in the elements of each icon array
      //into the corresponding slot in the computer and human player's
      //panels
      for (int i = 0; i < NUM_CARDS_PER_HAND; i++)
      {
         myCardTable.pnlComputerHand.add(computerLabels[i]);
         myCardTable.pnlHumanHand.add(humanLabels[i]);
      }
      
      // and two random cards in the play region (simulating a computer/hum ply)
      for (int i = 0; i < NUM_PLAYERS; i++)
         myCardTable.pnlPlayArea.add(playedCardLabels[i]);
      
      myCardTable.pnlPlayArea.add(playLabelText[0]);
      myCardTable.pnlPlayArea.add(playLabelText[1]);

      // show everything to the user
      myCardTable.setVisible(true);
      
   }
   
   static Card generateRandomCard()
   {
      Card.Suit suit;
      char val;

      int suitSelector, valSelector;

      // get random suit and value
      suitSelector = (int) (Math.random() * 4);
      valSelector = (int) (Math.random() * 14);

      // pick suit
      suit = Card.Suit.values()[suitSelector];
      
      // pick value
      valSelector++;   // put in range 1-14
      switch(valSelector)
      {
         case 1:
            val = 'A';
            break;
         case 10:
            val = 'T';
            break;
         case 11:
            val = 'J';
            break;
         case 12:
            val = 'Q';
            break;
         case 13:
            val = 'K';
            break;
         case 14:
            val = 'X';
            break;
         default:
            val = (char)('0' + valSelector);   // simple way to turn n into 'n'   
      }
 

      return new Card(val, suit);
   }
}


/*
 * An extension of JFrame, consists of simple accessors, some
 * simple member data, and a constructor that does most of the 
 * work in terms of formating for the custom JFrame.
 */
class CardTable extends JFrame
{
   static int MAX_CARDS_PER_HAND = 56;
   static int MAX_PLAYERS = 2;  // for now, we only allow 2 person games
   
   private int numCardsPerHand;
   private int numPlayers;
   public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea;
   
   public CardTable(String title, int numCardsPerHand, int numPlayers)
   {
      super(title);
      
      if (numPlayers < 1 || numPlayers > MAX_PLAYERS)
         numPlayers = MAX_PLAYERS;
      else this.numPlayers = numPlayers;
      
      if (numCardsPerHand < 1 || numCardsPerHand > MAX_CARDS_PER_HAND)
         numCardsPerHand = MAX_CARDS_PER_HAND;
      else this.numCardsPerHand = numCardsPerHand;
      
      pnlHumanHand = new JPanel(new GridLayout(1,numCardsPerHand,10,10));
      pnlPlayArea = new JPanel(new GridLayout(numPlayers,numPlayers,10,10));
      pnlComputerHand = new JPanel(new GridLayout(1,numCardsPerHand,10,10));
      
      setLayout (new BorderLayout(20, 10));
      add(pnlComputerHand, BorderLayout.NORTH);
      add(pnlPlayArea, BorderLayout.CENTER);
      add(pnlHumanHand, BorderLayout.SOUTH);
      
      pnlComputerHand.setBorder(new TitledBorder("Computer's Cards"));
      pnlPlayArea.setBorder(new TitledBorder("Played Cards"));
      pnlHumanHand.setBorder(new TitledBorder("Your Cards"));
   }
   
   public int getNumCards()
   {
      return numCardsPerHand;
   }
   
   public int getNumPlayers()
   {
      return numPlayers;
   }
}

/*
 * Easily the most challenging class to write.  Some specific details
 * about the more particular methods are described above them with comments.
 * 
 * This class loads icons into an array and allows the client to equate icons to
 * their respective cards with a variety of public methods.  It borrows
 * extensively from phase 1.
 */
class GUICard
{
    private static Icon[][] iconCards = new ImageIcon[14][4]; // 14 = A thru K + joker
    private static Icon iconBack;
    static boolean iconsLoaded = false;
    
    //my own helper data
    int cardCounter;
    
    GUICard()
    {
       loadCardIcons();
    }
    
    static public Icon getBackCardIcon()
    {
       return iconBack;
    }
    
    static public Icon getIcon(Card card)
    {
       loadCardIcons();
       return iconCards[valueAsInt(card)][suitAsInt(card)];
    }
    
    /*
     * This method was hard to write.
     * card.getVal() returns a character
     * that determine's the passed in card's value.
     * 
     * If you try to simply return that value, you'll
     * get an overflow because the integer value correspond's
     * to the character's ASCII value (2 has an ASCII value of 50,
     * T [as in 10] has a value of 84, so on...)
     * 
     * So we need a switch statement to convert between returned
     * char values, actual numerical values, and ASCII codes
     * all while cross-referencing the appropriate row location
     * for the character in question.  We use yet another
     * switch statement for that here.
     */
    private static int valueAsInt(Card card)
    {
       int retInt = 0;
       
       switch (card.getVal())
       {
       case 65: //A
          retInt = 0;
          break;
       case 50: //2
          retInt = 1;
          break;
       case 51: //3
          retInt = 2;
          break;
       case 52: //4
          retInt = 3;
          break;
       case 53: //5
          retInt = 4;
          break;
       case 54: //6
          retInt = 5;
          break;
       case 55: //7
          retInt = 6;
          break;
       case 56: //8
          retInt = 7;
          break;
       case 57: //9
          retInt = 8;
          break;
       case 84: //T
          retInt = 9;
          break;
       case 81: //Q
          retInt = 10;
          break;
       case 75: //K
          retInt = 11;
          break;
       case 74: //J
          retInt = 12;
          break;
       case 88: //X, for joker
          retInt = 13;
          break;
       default: 
          retInt = 999; //should never see
          break;
       }
       
       return retInt;
    }
    
    private static int suitAsInt(Card card)
    {
       int emergency = 99;
       
       if (card.getSuit() == Card.Suit.clubs)
             return 0;
       if (card.getSuit() == Card.Suit.diamonds)
             return 1;
       if (card.getSuit() == Card.Suit.hearts)
             return 2;
       if (card.getSuit() == Card.Suit.spades)
          return 3;
       
       else return emergency; 
    }
    
    static void loadCardIcons()
    {
       
       if (iconsLoaded == true)
          return;
       for (int rows = 0; rows < iconCards.length; rows++)
       {
          for (int cols = 0; cols < iconCards[rows].length; cols++)
          {
               iconCards[rows][cols] = new ImageIcon("images/" 
                     + turnIntIntoCardValue(rows)
                     + turnIntIntoCardSuit(cols) + ".gif");
          }
       }
       
       iconBack = new ImageIcon("images/BK.gif");
       iconsLoaded = true;
       return;
    }
    
    private static String turnIntIntoCardValue(int k)
      {

         String retString;
         
         switch (k)
         {
         case 0: retString = "A";
         break;
         case 1: retString = "2";
         break;
         case 2: retString = "3";
         break;
         case 3: retString = "4";
         break;
         case 4: retString = "5";
         break;
         case 5: retString = "6";
         break;
         case 6: retString = "7";
         break;
         case 7: retString = "8";
         break;
         case 8: retString = "9";
         break;
         case 9: retString = "T";
         break;
         case 10: retString = "Q";
         break;
         case 11: retString = "K";
         break;
         case 12: retString = "J";
         break;
         case 13: retString = "X";
         break;
         default: retString = "should never see";
         }
         
           return retString;
      }
    
    private static String turnIntIntoCardSuit(int j)
      {
         // an idea for another helper method (do it differently if you wish)
         String retString;
         
         switch (j)
         {
         case 0: retString = "C";
         break;
         case 1: retString = "D";
         break;
         case 2: retString = "H";
         break;
         case 3: retString = "S";
         break;
         
         default: retString = "should never see";
         break;
         }
         
         return retString;
      }
}


class Card
{
    // type and constants
   public enum State {deleted, active} // not bool because later we may expand
   public enum Suit { clubs, diamonds, hearts, spades }

   // for sort.  
   public static char[] valueRanks = { '2', '3', '4', '5', '6', '7', '8', '9',
      'T', 'J', 'Q', 'K', 'A', 'X'};
   static Suit[] suitRanks = {Suit.clubs, Suit.diamonds, Suit.hearts,
      Suit.spades};
   static int numValsInOrderingArray = 14;  // 'X' = Joker

   // private data
   private char value;
   private Suit suit;
   State state;
   boolean errorFlag;

   // 4 overloaded constructors
   public Card(char value, Suit suit)
   {
      set(value, suit);
   }

   public Card(char value)
   {
      this(value, Suit.spades);
   }
   public Card()
   {
      this('A', Suit.spades);
   }
   // copy constructor
   public Card(Card card)
   {
      this(card.value, card.suit);
   }

   // mutators
   public boolean set(char value, Suit suit)
   {
      char upVal;            // for upcasing char

      // can't really have an error here
      this.suit = suit;  

      // convert to uppercase to simplify
      upVal = Character.toUpperCase(value);

      // check for validity
      if (
            upVal == 'A' || upVal == 'K'
            || upVal == 'Q' || upVal == 'J'
            || upVal == 'T' || upVal == 'X'
            || (upVal >= '2' && upVal <= '9')
            )
      {
         errorFlag = false;
         state = State.active;
         this.value = upVal;
      }
      else
      {
         errorFlag = true;
         return false;
      }

      return !errorFlag;
   }

   public void setState( State state)
   {
      this.state = state;
   }

   // accessors
   public char getVal()
   {
      return value;
   }

   public Suit getSuit()
   {
      return suit;
   }

   public State getState()
   {
      return state;
   }

   public boolean getErrorFlag()
   {
      return errorFlag;
   }

   // stringizer
   public String toString()
   {
      String retVal;

      if (errorFlag)
         return "** illegal **";
      if (state == State.deleted)
         return "( deleted )";

      // else implied

      if (value != 'X')
      {
         // not a joker
         retVal =  String.valueOf(value);
         retVal += " of ";
         retVal += String.valueOf(suit);
      }
      else
      {
         // joker
         retVal = "joker";

         if (suit == Suit.clubs)
            retVal += " 1";
         else if (suit == Suit.diamonds)
            retVal += " 2";
         else if (suit == Suit.hearts)
            retVal += " 3";
         else if (suit == Suit.spades)
            retVal += " 4";
      }

      return retVal;
   }

   public boolean equals(Card card)
   {
      if (this.value != card.value)
         return false;
      if (this.suit != card.suit)
         return false;
      if (this.errorFlag != card.errorFlag)
         return false;
      if (this.state != card.state)
         return false;
      return true;
   }

   // sort member methods
   public int compareTo(Card other)
   {
      if (this.value == other.value)
         return ( getSuitRank(this.suit) - getSuitRank(other.suit) );

      return (
            getValueRank(this.value)
            - getValueRank(other.value)
            );
   }

   public static void setRankingOrder(
         char[] valueOrderArr, Suit[] suitOrdeArr,
         int numValsInOrderingArray )
   {
      int k;

      // expects valueOrderArr[] to contain only cards used per pack,
      // including jokers, needed to define order for the game environment

      if (numValsInOrderingArray < 0 || numValsInOrderingArray > 14)
         return;

      Card.numValsInOrderingArray = numValsInOrderingArray;

      for (k = 0; k < numValsInOrderingArray; k++)
         Card.valueRanks[k] = valueOrderArr[k];

      for (k = 0; k < 4; k++)
         Card.suitRanks[k] = suitOrdeArr[k];
   }

   public static int getSuitRank(Suit st)
   {
      int k;

      for (k = 0; k < 4; k++)
         if (suitRanks[k] == st)
            return k;

      // should not happen
      return 0;
   }

   public  static int getValueRank(char val)
   {
      int k;

      for (k = 0; k < numValsInOrderingArray; k++)
         if (valueRanks[k] == val)
            return k;

      // should not happen
      return 0;
   }

   public static void arraySort(Card[] array, int arraySize)
   {
      for (int k = 0; k < arraySize; k++)
         if (!floatLargestToTop(array, arraySize - 1 - k))
            return;
   }

   private static boolean floatLargestToTop(Card[] array, int top)
   {
      boolean changed = false;
      Card temp;

      for (int k = 0; k < top; k++)
         if (array[k].compareTo(array[k+1]) > 0)
         {
            temp = array[k];
            array[k] = array[k+1];
            array[k+1] = temp;
            changed = true;
         };
         return changed;
   }
}


class Hand
{
   public static final int MAX_CARDS_PER_HAND = 100;  // should cover any game

   private Card[] myCards;
   private int numCards;

   //constructor
   public Hand()
   {
      // careful - we are only allocating the references
      myCards = new Card[MAX_CARDS_PER_HAND];
      resetHand();
   }

   // mutators
   public void resetHand() { numCards = 0; }

   public boolean takeCard(Card card)
   {
      if (numCards >= MAX_CARDS_PER_HAND)
         return false;

      // be frugal - only allocate when needed
      if (myCards[numCards] == null)
         myCards[numCards] = new Card();

      // don't just assign:  mutator assures active/undeleted      
      myCards[numCards++].set( card.getVal(), card.getSuit() );
      return true;
   }

   public Card playCard()
   {
      // always play  highest card in array.  client will prepare this position.
      // in rare case that client tries to play from a spent hand, return error

      Card errorReturn = new Card('E', Card.Suit.spades); // in rare cases

      if (numCards == 0)
         return errorReturn;
      else
         return myCards[--numCards];
   }

   // accessors
   public String toString()
   {
      int k;
      String retVal = "Hand =  ( ";

      for (k = 0; k < numCards; k++)
      {
         retVal += myCards[k].toString();
         if (k < numCards - 1)
            retVal += ", ";
      }
      retVal += " )";
      return retVal;
   }

   int getNumCards()
   {
      return numCards;
   }

   Card inspectCard(int k)
   {
      // return copy of card at position k.
      // if client tries to access out-of-bounds card, return error

      Card errorReturn = new Card('E', Card.Suit.spades); // in rare cases

      if (k < 0 || k >= numCards)
         return errorReturn;
      else
         return myCards[k];
   }

   void sort()
   {
      // assumes that Card class has been sent ordering (if default not correct)
      Card.arraySort(myCards, numCards);
   }
}

class Deck
{
    // six full decks (with jokers) is enough for about any game
   private static final int MAX_CARDS_PER_DECK = 6 * 54;
   private static Card[] masterPack;   // one 52-Card master to use for
   // initializing decks
   private Card[] cards;
   private int topCard;
   private int numPacks;

   private static boolean firstTime = true;  // avoid calling allcMstrPck > once

   public Deck()
   {
      this(1);
   }

   public Deck(int numPacks)
   {
      allocateMasterPack();  // do not call from init()
      cards = new Card[MAX_CARDS_PER_DECK];
      init(numPacks);
   }

   static private void allocateMasterPack()
   {
      int j, k;
      Card.Suit st;
      char val;

      // we're in static method; only need once / program: good for whole class
      if ( !firstTime )
         return;
      firstTime = false;

      // allocate
      masterPack = new Card[52];
      for (k = 0; k < 52; k++)
         masterPack[k] = new Card();

      // next set data
      for (k = 0; k < 4; k++)
      {
         // set the suit for this loop pass
         st = Card.Suit.values()[k];

         // now set all the values for this suit
         masterPack[13*k].set('A', st);
         for (val='2', j = 1; val<='9'; val++, j++)
            masterPack[13*k + j].set(val, st);
         masterPack[13*k+9].set('T', st);
         masterPack[13*k+10].set('J', st);
         masterPack[13*k+11].set('Q', st);
         masterPack[13*k+12].set('K', st);
      }
   }

   // set deck from 1 to 6 packs, perfecly ordered
   public void init(int numPacks)
   {
      int k, pack;

      if (numPacks < 1 || numPacks > 6)
         numPacks = 1;

      // hand over the masterPack cards to our deck
      for (pack = 0; pack < numPacks; pack++)
         for (k = 0; k < 52; k++)
            cards[pack*52 + k] = masterPack[k];

      // this was slightly sloppy:  multiple packs point to same master cards
      // if something modified a card, we would be in trouble.  fortunately,
      // we don't expect a card to ever be modified after instantiated
      // in the context of a deck.

      this.numPacks = numPacks;
      topCard = numPacks * 52;
   }

   public void init()
   {
      init(1);
   }

   public int getNumCards()
   {
      return topCard;
   }

   public void shuffle()
   {
      Card tempCard;
      int k, randInt;

      // topCard is size of deck
      for (k = 0; k < topCard; k++)
      {
         randInt = (int)(Math.random() * topCard);

         // swap cards k and randInt (sometimes k == randInt:  okay)
         tempCard = cards[k];
         cards[k] = cards[randInt];
         cards[randInt] = tempCard;
      }
   }

   public Card takeACard()
   {
      return new Card();
   }

   public Card dealCard()
   {
      // always deal the topCard.  
      Card errorReturn = new Card('E', Card.Suit.spades); //  in rare cases

      if (topCard == 0)
         return errorReturn;
      else
         return cards[--topCard];
   }

   public boolean removeCard(Card card)
   {
      int k;
      boolean foundAtLeastOne;

      foundAtLeastOne = false;
      for (k = 0; k < topCard; k++)
      {
         // care: use while, not if, in case we copy to-be-removed from top to k
         while ( cards[k].equals(card) )
         {
            // overwrite card[k] with top of deck, then decrement topCard
            cards[k] = cards[topCard - 1];
            topCard--;
            foundAtLeastOne = true;
            // test because "while" causes topCard to decrease, possibly below k
            if ( k >= topCard )
               break;
         }
      }
      // did above work if k == topCard-1?  think about it
      return foundAtLeastOne;
   }

   public boolean addCard(Card card)
   {
      // don't allow too many copies of this card in the deck
      if (numOccurrences(card) >= numPacks)
         return false;

      cards[topCard++] = card;
      return true;
   }

   public Card inspectCard(int k)
   {
      // return copy of card at position k.
      // if client tries to access out-of-bounds card, return error

      Card errorReturn = new Card('E', Card.Suit.spades); //  in rare cases

      if (k < 0 || k >= topCard)
         return errorReturn;
      else
         return cards[k];
   }

   public int numOccurrences(Card card)
   {
      int retVal, k;

      retVal = 0;

      // assumption:  card is a default item:  not deleted and state=active)
      for (k = 0; k < topCard; k++)
      {
         if (inspectCard(k).equals(card))
            retVal++;
      }
      return retVal;
   }

   public String toString()
   {
      int k;
      String retString = "\n";

      for (k = 0; k < topCard; k++)
      {
         retString += cards[k].toString();
         if (k < topCard - 1)
            retString += " / ";
      }
      retString += "\n";

      return retString;
   }

   void sort()
   {
      // assumes that Card class has been sent ordering (if default not correct)
      Card.arraySort(cards, topCard);
   }
}

//class CardGameFramework  ----------------------------------------------------
class CardGameFramework
{
 private static final int MAX_PLAYERS = 50;

 private int numPlayers;
 private int numPacks;            // # standard 52-card packs per deck
                                  // ignoring jokers or unused cards
 private int numJokersPerPack;    // if 2 per pack & 3 packs per deck, get 6
 private int numUnusedCardsPerPack;  // # cards removed from each pack
 private int numCardsPerHand;        // # cards to deal each player
 private Deck deck;               // holds the initial full deck and gets
                                  // smaller (usually) during play
 private Hand[] hand;             // one Hand for each player
 private Card[] unusedCardsPerPack;   // an array holding the cards not used
                                      // in the game.  e.g. pinochle does not
                                      // use cards 2-8 of any suit

 public CardGameFramework( int numPacks, int numJokersPerPack,
       int numUnusedCardsPerPack,  Card[] unusedCardsPerPack,
       int numPlayers, int numCardsPerHand)
 {
    int k;

    // filter bad values
    if (numPacks < 1 || numPacks > 6)
       numPacks = 1;
    if (numJokersPerPack < 0 || numJokersPerPack > 4)
       numJokersPerPack = 0;
    if (numUnusedCardsPerPack < 0 || numUnusedCardsPerPack > 50) //  > 1 card
       numUnusedCardsPerPack = 0;
    if (numPlayers < 1 || numPlayers > MAX_PLAYERS)
       numPlayers = 4;
    // one of many ways to assure at least one full deal to all players
    if  (numCardsPerHand < 1 ||
          numCardsPerHand >  numPacks * (52 - numUnusedCardsPerPack)
          / numPlayers )
       numCardsPerHand = numPacks * (52 - numUnusedCardsPerPack) / numPlayers;

    // allocate
    this.unusedCardsPerPack = new Card[numUnusedCardsPerPack];
    this.hand = new Hand[numPlayers];
    for (k = 0; k < numPlayers; k++)
       this.hand[k] = new Hand();
    deck = new Deck(numPacks);

    // assign to members
    this.numPacks = numPacks;
    this.numJokersPerPack = numJokersPerPack;
    this.numUnusedCardsPerPack = numUnusedCardsPerPack;
    this.numPlayers = numPlayers;
    this.numCardsPerHand = numCardsPerHand;
    for (k = 0; k < numUnusedCardsPerPack; k++)
       this.unusedCardsPerPack[k] = unusedCardsPerPack[k];

    // prepare deck and shuffle
    newGame();
 }

 // constructor overload/default for game like bridge
 public CardGameFramework()
 {
    this(1, 0, 0, null, 4, 13);
 }

 public Hand getHand(int k)
 {
    // hands start from 0 like arrays

    // on error return automatic empty hand
    if (k < 0 || k >= numPlayers)
       return new Hand();

    return hand[k];
 }

 public Card getCardFromDeck() { return deck.dealCard(); }

 public int getNumCardsRemainingInDeck() { return deck.getNumCards(); }

 public void newGame()
 {
    int k, j;

    // clear the hands
    for (k = 0; k < numPlayers; k++)
       hand[k].resetHand();

    // restock the deck
    deck.init(numPacks);

    // remove unused cards
    for (k = 0; k < numUnusedCardsPerPack; k++)
       deck.removeCard( unusedCardsPerPack[k] );

    // add jokers
    for (k = 0; k < numPacks; k++)
       for ( j = 0; j < numJokersPerPack; j++)
          deck.addCard( new Card('X', Card.Suit.values()[j]) );

    // shuffle the cards
    deck.shuffle();
 }

 public boolean deal()
 {
    // returns false if not enough cards, but deals what it can
    int k, j;
    boolean enoughCards;

    // clear all hands
    for (j = 0; j < numPlayers; j++)
       hand[j].resetHand();

    enoughCards = true;
    for (k = 0; k < numCardsPerHand && enoughCards ; k++)
    {
       for (j = 0; j < numPlayers; j++)
          if (deck.getNumCards() > 0)
             hand[j].takeCard( deck.dealCard() );
          else
          {
             enoughCards = false;
             break;
          }
    }

    return enoughCards;
 }

 void sortHands()
 {
    int k;

    for (k = 0; k < numPlayers; k++)
       hand[k].sort();
 }
}

