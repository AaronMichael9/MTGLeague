# MTGLeague
****WORK IN PROGRESS****

this is an attempt to make a program to track Magic: the Gathering decks with a MMR (match making rating) system. This program has three tabs that allow for editing, viewing, and ranking of deck entities.
Although it is intended for MTG decks nothing about this is specific to MTG, so the framework could be used to evaluate any entities that repeatedly compete against each other.
All records are stored in two files named history.txt and stats.txt currently in the League1 folder.
Currently the program treats the league as a closed system. You cannot run games with decks not entered in stats.txt.

**Edit Tab**

the edit tab allows for the creation of decks and enabling/disabling them. Disabled decks will not appear in the other two tabs.

**Standings Tab**

The standings tab allows for viewing the deck names ranked in order of current mmr (mmr is pulled from stats.txt). Pressing Space will show the mmr.

**Game Tab**

Game tab has two states. State one allows for selecting decks for a game. The Suggest Deck button will use a DeckSuggester object to add one or more decks into participants list. **Currently the
 suggester class being used is hard coded in GameSetupTab and cannot be changed at runtime**. The start game button transitions the tab into state two. In state two results of the game can be entered
or the game can be cancelled. If results are entered the program will use a Commissioner object determine mmr changes for all involved decks and then update stats.txt and history.txt. **Currently the
 commissioner class being used is hard coded in GameTab and cannot be changed at runtime.**
7
 **Suggester Class**
 
All Suggester classes must implement the suggester interface. My current prototype just selects one deck at random. More advanced ones could select decks based on mmr or last played information available
in the Deck class.

**Commissioner Class**

All Commissioner classes must implement the commissioner interface. PrototypeCommissioner, my first attempt, bases mmr change from a payout vector then adjusted for each player based on that player's mmr
relative to the mean mmr. My second attempt, AnteCommissioner, was designed to so that the net sum of all mmr changes is 0, making the league a closed system.

**Reevaluator Class**

Originally my intent was for stats.txt to record current mmr and for that to be the primary source of information. I did not count on how much I would change the mmr algorithm in the Commissioner tab. I
constructed reevaluator class to reevaluate the entirety of history.txt and place stats.txt in such a state as if the specified **(hardcoded)** Commissioner class had been present from the start. 
