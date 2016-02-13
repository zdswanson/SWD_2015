# 1 Vs 100    

## [Executive Summary](https://class-git.engineering.uiowa.edu/swd-fall-2015/swd_team01/blob/master/1vs100Game/Execute_Summary.docx) 
The game is simple, last longer than your opponent. If you answer incorrectly, you are eliminated. It is based off of the TV show 1 vs 100 where one person ('The One') plays trivia against 100 players collectively called ‘The Mob’. During the game the players will answer a myriad of electrical engineering questions. Each player of the Mob has 15 seconds to select their answer. A player in The Mob cannot change his or her answer once it has been selected, because the time they take to answer determines their score for that question. After the Mob has answered the question the One will have a chance to answer. When the One is selecting their answer they will have an unlimited amount of time after the mob's 15 seconds is up. If the One chooses the correct answer then they will be scored upon how many of the Mob players are eliminated by that question. The players of the mob are scored on how quickly they answered the question as stated above. The One and the Mob scoring are completely separate from each other.

The top ten Mob members are listed in a leaderboard. If the One answers a question incorrectly the top member of the mob who is still will take their place and then the game will restart. Beneath the Mob leaderboard is a panel that displays the score of the person playing, as well as their overall rank. Above it shows the remaining number of people left in the Mob as well as the score of the One.

Each of the squares next to the leaderboard represents a member in the Mob, and the single square below it represents the One. The squares will change color based on if the person has answered and if they answered incorrectly or not. If a member of the Mob has answered incorrectly then they will become an observer of the game, and once the game has ended everyone that is an observer will join the game again for the next round. If the square is black it represent a player that wasn’t initialized, if it is blue it is an initialized and active player. For every player that was initialized their square will turn yellow once they have answered the question, red if the member got the previous question wrong and green if they got it right. If the member has gotten it wrong it will turn from red to gray for the rest of the game. The One's square follows the same color scheme.  

The game will reset once the one gets an question wrong or the entire Mob has been eliminated, and will be replaced by the highest scoring mob member that hasn't been eliminated (or the highest scoring if all are eliminated), without having to re-connect to the server. Games will continue until the "Stop" button is pressed on the server, at which point it will finish the current game then end the game.
 
### Features
* Global live top 10 Mob score Leaderboard with place, name, and score
* When players connect, they are prompted to enter a username that will be used on the leaderboard
* Tens of people can play simultaneously, up to 101
* Continues to start new games until the server chooses to end after the next game
* Pauses between games
* Live representation of each member in the Mob and the One
   * As players join, the square representing that player becomes blue on all clients' GUIs in real time
   * As players answer, their square becomes yellow on all clients' GUIs in real time
   * When answers are checked, all players' resulting color (green for correct, red for incorrect) are displayed on all clients' GUIs in real time
   * When next question is given, eliminated players are permanently turned grey, green (remaining) players are turned back to blue
* Number of people remaining in the mob displayed on scoreboard 
* The One's score is based off the number of mob members eliminated  
* Mob gets 15 seconds to lock in answer 
* Score is based on how long Mob member took to select their answer
* Questions always have three possible answers
* The One chooses answer after the 15 second Mob answer period
* The One has unlimited time to answer
* If a member of the mob does not answer within the 15 seconds they will automatically get the answer wrong and will be eliminated
* An answer is selected by clicking on the answer
* Player is be unable to change their answer once it has been selected
* Changes the player who is 'the One' after the game ends, the next 'One' will be the highest scoring mob member that hasn't been eliminated (or the highest scoring Mob member if they're all eliminated)
* After a member of the mob has been eliminated the player becomes an observer until the end of the game
* When a new game starts all players will automatically be put into the new game
* Each player will have their personal current score and leaderboard ranking listed underneath the top 10 Mob leaderboard
* When answering the question the answer selected will turn yellow
* If the selected answer turns out to be wrong then the yellow will change to red after the one has selected their answer
* The correct answer will appear in green. If the player selects the correct answer the selected answer will change from yellow to green.
* Questions are shuffled and previous questions are not re-used until all questions have been used
* Starts new games without reconnecting all of the clients to the server.  
* Client is asked at start for IP of server via popup, popup defaults to 127.0.0.1

## [User Documentation] (https://class-git.engineering.uiowa.edu/swd-fall-2015/swd_team01/blob/master/1vs100Game/User_Guide.docx)
 
The game is split up into two groups, "The One" and "The Mob". When starting the game, the first person to connect is the first "One". Then the rest of the "Mob" can start their clients whenever they want. When everyone is in the game, the person running the server should press the start button on their server GUI; this starts the game. No users will be able to connect after the server has initiated the first game.

The client's GUI contains a leaderboard that updates after every round with the top ten mob member's scores. Mob member's scores are based off of how quickly they answer each trivia question. Next to that is the map of Mob members. The map starts out with 100 black squares, and as each Mob member joins the game, their square will change to blue. Below the Mob map is the One's square. It behaves identically to the Mob's squares, but it is separate on the GUI. Below that is the countdown timer. Everyone in the mob only gets fifteen seconds to answer a question, and the counter shows how much time is left after the question has been asked. If a mob member doesn't answer in time then they automatically get the question wrong. As each player answers, their square on the map will turn yellow on everyone's screens. When the fifteen seconds runs out, the One gets to answer the same question, except they don't have a time limit. Once the one answers, the maps change to show who got answers correct. Correct answers appear as green and incorrect appear as red on the map. Those that got the question incorrect can no longer participate, but they get to observe each question asked and the current state of the map. Questions are asked repeatedly until either the entire mob is eliminated, or the one is eliminated. In the case of the mob being eliminated, the one receives the maximum score of 1,000,000. The mob member with the highest score becomes the One for the next game, and everyone is reinitialized as a player that can participate, and the game goes on. 

The One gets a certain amount of points based on how many mob members have been eliminated, the lower the number of people remaining, the higher the score, up to 1 million.

When a mob member answers a question, they cannot change it. It is locked in. 

The leaderboard contains info on the top ten, how many mob members are remaining, the one's current score, and each individual's score (so your personal score will be on your version of the leaderboard). 

### Features
* Global live top 10 Mob score Leaderboard with place, name, and score
* When players connect, they are prompted to enter a username that will be used on the leaderboard
* Tens of people can play simultaneously, up to 101
* Continues to start new games until the server chooses to end after the next game
* Pauses between games
* Live representation of each member in the Mob and the One
   * As players join, the square representing that player becomes blue on all clients' GUIs in real time
   * As players answer, their square becomes yellow on all clients' GUIs in real time
   * When answers are checked, all players' resulting color (green for correct, red for incorrect) are displayed on all clients' GUIs in real time
   * When next question is given, eliminated players are permanently turned grey, green (remaining) players are turned back to blue
* Number of people remaining in the mob displayed on scoreboard 
* The One's score is based off the number of mob members eliminated  
* Mob gets 15 seconds to lock in answer 
* Score is based on how long Mob member took to select their answer
* Questions always have three possible answers
* The One chooses answer after the 15 second Mob answer period
* The One has unlimited time to answer
* If a member of the mob does not answer within the 15 seconds they will automatically get the answer wrong and will be eliminated
* An answer is selected by clicking on the answer
* Player is be unable to change their answer once it has been selected
* Changes the player who is 'the One' after the game ends, the next 'One' will be the highest scoring mob member that hasn't been eliminated (or the highest scoring Mob member if they're all eliminated)
* After a member of the mob has been eliminated the player becomes an observer until the end of the game
* When a new game starts all players will automatically be put into the new game
* Each player will have their personal current score and leaderboard ranking listed underneath the top 10 Mob leaderboard
* When answering the question the answer selected will turn yellow
* If the selected answer turns out to be wrong then the yellow will change to red after the one has selected their answer
* The correct answer will appear in green. If the player selects the correct answer the selected answer will change from yellow to green.
* Questions are shuffled and previous questions are not re-used until all questions have been used
* Starts new games without reconnecting all of the clients to the server.  
* Client is asked at start for IP of server via popup, popup defaults to 127.0.0.1

## [Developer Documentation] (https://class-git.engineering.uiowa.edu/swd-fall-2015/swd_team01/blob/master/1vs100Game/Developers_Guide.docx) 

The code for this program is split into four files; two monstrous files each containing one class and one private inner class, and  two test classes used to initialize a GUI with an object of the associated class (`GameServer` for `GameServerTest` and `GameClient` for `GameClientTest`). 

The `GameServer` class runs a host server that facilitates the game. It manages almost everything regarding the game. It manages and distributes questions, determines correctness of client answers, handles live updating of client GUI's any time a change needs to be made, and generally does all the heavy lifting for the game. The other class in the `GameServer` file is `Player`. The `GameServer` maintains an array of `Player` objects representing each player currently in the game. `Player` is a `Runnable` class, and `GameServer` creates a new thread in an `ExecutorService` for each `Player` object created, so that `Player`s can operate without interrupting the main game-facilitation thread. 

Each `Player` object handles variables unique to their single client, including maintaining the socket connecting and handling all I/O with their associated client. Any time the `GameServer` needs to communicate with any clients, it uses functions in the correct `Player` object(s).

After initializing connections with all clients and starting the game by pressing the "start" button on the server GUI interface, the `GameServer` runs two nested while loops until the final game after the server GUI "stop" button is pressed finishes. The outer loop waits for the server GUI's "stop" button to be pressed, while the inner loop runs a single game. When the game ends the program exits the inner loop, resets the game, checks if the server has declared that game the final game, and if not, starts a new game. 

The inner loop runs the game. The basic outline is as follows: Server pushes next question to all clients, the main thread sleeps for 15 seconds (the amount of time the clients in the Mob have to answer), the server informs the One it's their turn and the rest of the clients that it's the One's turn, waits for the One's response, checks all clients' answers and updates their colors based on their responses, updates all clients' scoreboards, sleeps for 5 seconds to allow clients time to view the answer results and new scoreboard, resets the colors to the blue/grey scheme, and if the game still hasn't ended, restarts the loop and pushes another question to the clients. 

GameClient handles all of the client activity. Inside of the constructor contains all of the code for the GUI, such as a mob top ten leaderboard, the questions with three different answers, and the 100 squares that represent the players of the mob and one square that represents the One.
The top ten of Mob leader board shows the One's score the current client's score and the amount of remaining players in the Mob.
The question and the three answers have interesting features. First the client only has fifteen seconds to answer the question. Once the client has selected an answer it will turn yellow as well as the square that the client represents in the Mob or One. If the answer is right it will turn from yellow to green. If the answer selected is wrong then the answer will turn from yellow to red and the correct answer will turn up as green. We do this by changing the color of the panels that each of the answer is contained in. 
Each of the squares represent a player and if they are in the game or not. Each black square represents an uninitialized mob players, each blue player represents an initialized player. Similar to the answer panels it will turn yellow once a player of the mob has selected an answer and if a player gets the question wrong their square will turn red and then to gray because they will become an observer to the game and won’t actually be able to answer the questions. If they get it right it will turn from yellow to green and continue to play the game as a member of the mob.

![1vs100Game](https://class-git.engineering.uiowa.edu/swd-fall-2015/swd_team01/uploads/f57febcad188b7ec936e9c67ce4b5adb/1vs100Game.png)

## [Source Code](https://class-git.engineering.uiowa.edu/swd-fall-2015/swd_team01/tree/master/1vs100Game/src)  