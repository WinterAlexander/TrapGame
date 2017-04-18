# TrapGame
A multi-player form-based desktop game about strategy and spam-clicking.
Development is done ! I won't be adding features to this project unless a
miracle attention is given to it. Please send me bug reports if found any.

### How to play:
There's a board with a defined size and each player has his own color.
First you can click any square on the board if the square hasn't already
been clicked by another player. After your first click you can only click
on adjacent squares to make your territory bigger. The winner is the player
with the more territory.

### Development goal :

The goal of this game was to get experience making my own networking engine for a game. The networking engine I've developped with Java.net has been really useful in many project I have done after, notably SocialPlatformer.

### Screenshots (click) :

<img src="https://raw.githubusercontent.com/WinterGuardian/TrapGame/master/dev/screenshot1.png" width="200px" /> <img src="https://raw.githubusercontent.com/WinterGuardian/TrapGame/master/dev/screenshot2.png" width="200px" /> <img src="https://raw.githubusercontent.com/WinterGuardian/TrapGame/master/dev/screenshot3.png" width="200px" />

Current version: 1.1.2

### Official builds:

  - Please download on GameJolt: http://gamejolt.com/games/trapgame/145922

### Development builds:

  - Version 0.9 Client: http://bit.ly/1Z116YU Server: http://bit.ly/21omiK4
  - Version 0.8 Client: http://bit.ly/1TndwsE Server: http://bit.ly/1ppvFLE
  - Version 0.7 Client: http://bit.ly/23zZj3M Server: http://bit.ly/23zZmfY
  - Version 0.6 Client: http://bit.ly/1YbKEor Server: http://bit.ly/1TwHpbI
  - Version 0.5 Client: http://bit.ly/1VoHhfQ Server: http://bit.ly/1Qb1Kww
  - Version 0.4.1 Client: http://bit.ly/1oq9rIT Server: http://bit.ly/1SFmeSH
  - Version 0.4 [BROKEN]
  - Version 0.3 Client: http://bit.ly/1RO7GNp Server: http://bit.ly/235SR0w
  - Version 0.2 Client: http://bit.ly/1VWgMO2 Server: http://bit.ly/1VcelpS
  - Version 0.1 Client: http://bit.ly/1UXf0wJ Server: http://bit.ly/1MX7qKG

From version 1.0, the client can also host a server game.

If any of theses shortcuts are broken, download might be available on
https://dl.dropboxusercontent.com/u/65019675/TrapGame/versionX.X/TrapGame[Server].jar
But you have to specify the version and if you want the Server or not. Please tell  me if any is broken.

The client has been made with Swing and AWT java libraries. I made the images by myself only with Gimp 2 and free of rights images founds on google.com I used java.net to send UDP packets between the client and the server. I first made the game with TCP but changed when the game got more reactive. (Was a mistake but I didn't know at the time)

### Translation system

The game is in french and english and could be easily translated to another language, translations are loaded from a properties file. The game server can log to disk and both client and server are logging to console. No library was used to achieve that.

### Origin

The game was originally made from a Minecraft mod I made called BlockFarmers. With basically the same mechanics, BlockFarmers was asking minecraft players to click on the ground to play instead of directly on a board like with TrapGame. 

This video is a gameplay video of a minecraft server plugin game I made: 
https://www.youtube.com/watch?v=zAfJ4Ot8nkk 

The gameplay is equivalent but the interface if obviously completely different.



