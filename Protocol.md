Note: curl-file was changed. Most changes were made due to the original not including Cardtype and Element as frontend input and my Profile-Function covering 2 query at once.

Unique Features:
- a user may own more than 1 instance of a card (making collecting more available)
- Necessary Tables and an admin-account for the first use are created automatically (can be removed by removing 1 line of code in main file)

Problems
1.
  Due to windows blocking ports i couldn't access my DB for several days
  Sadly this led to me not beeing able to implement Trades nor battles (battles could be handled by the server, but connection to frontend could not be made)
  
2.
  Due to having never realed used SQL (besides some basic querys), creating tables and querys took longer than expected

Design Timeline:
  Cards and Stacks:
    First I pondered about how cards should be defined and how they would interact with each other (type effectivity, etc.)
    Stacks were the next step. Stacks are made up of a LinkedList (consisting of cards and a String called Owner).
    At first Stacks were only used for the battle engine. Later, they were also used to transfer card lists between backend and frontend (and vice versa)to their simple structure.
    
BattleEngine:
  Next up was the BattleEngine. The package battleTools consists of 3 Classes:
    - Judge (decides which card wins and documents the battle by writing to the console)
    - Engine (checks for deckout, handles decks interacting with each other, calls the judge and manages draws)
    - BattleResult (simple class for transfer of winner and loser of a fight and if a draw happened)
      
    These 3 classes were written without major problems. I had them pretty much figured out as soon as the project was announced.
  
DataBase and Connection to Frontend:
  This part of the project was the biggest hurdle. I had to design every table and interaction from scratch.
  I always wanted users to have their own tables for their deck and collection. This required the creation of these tables as soon as a user was created.
  At this point I had to made changes to the curl file, due to it containing a line to create an admin (which is already created by a initial setup)
  
  Element and CardType of a card are defined by enums. This required me to write a converter, that converted a fitting string caseinsensitive to the according enum.
  
  Table names are handled by the Class TableNames. This class provides table names for the trading table, cards list and other fixed tables.
  It also provides a name generator for user related tables.
  
  Cards are not directly stored in user stacks and packages. Those tables only contain their uids. All data about a specific card is stored in an extra table.
  
  The connection to the Frontend is handled by the class MTCG. This class decides based on input and method of the http-header, which action regarding the database is carried out.
  Serialization and Deserialization is carried out via Jackson.
  
Sadly I could not fully complete the project. I could not connect to the local database due to an apparent conflict between the windows-firewall and my antivirus
(Luckily) this was fixed through a windows update.

Tests and Mocking:
  Tests were few and simple, just some String comparisons
    The whole database interaction relies on the tests.
  I could not mock my application, due to me finishing it rather late
  
  Link to Repository: https://github.com/TheEarlOfTea/MonsterCardGame
