||      ::::    ::::  ::::::::::: ::::    ::: :::::::::: ::::::::  :::       ::: :::::::::: :::::::::: :::::::::  :::::::::: :::::::::      ||
||      +:+:+: :+:+:+     :+:     :+:+:   :+: :+:       :+:    :+: :+:       :+: :+:        :+:        :+:    :+: :+:        :+:    :+:     ||
||      +:+ +:+:+ +:+     +:+     :+:+:+  +:+ +:+       +:+        +:+       +:+ +:+        +:+        +:+    +:+ +:+        +:+    +:+     ||
||      +#+  +:+  +#+     +#+     +#+ +:+ +#+ +#++:++#  +#++:++#++ +#+  +:+  +#+ +#++:++#   +#++:++#   +#++:++#+  +#++:++#   +#++:++#:      ||
||      +#+       +#+     +#+     +#+  +#+#+# +#+              +#+ +#+ +#+#+ +#+ +#+        +#+        +#+        +#+        +#+    +#+     ||
||      #+#       #+#     #+#     #+#   #+#+# #+#       #+#    #+#  #+#+# #+#+#  #+#        #+#        #+#        #+#        #+#    #+#     ||
||      ###       ### ########### ###    #### ########## ########    ###   ###   ########## ########## ###        ########## ###    ###     ||
||                                                                                                                                          ||	
++==========================================================================================================================================++

  _   _                 _                _                 
 | | | | _____      __ | |_ ___    _ __ | | __ _ _   _   _ 
 | |_| |/ _ \ \ /\ / / | __/ _ \  | '_ \| |/ _` | | | | (_)
 |  _  | (_) \ V  V /  | || (_) | | |_) | | (_| | |_| |  _ 
 |_| |_|\___/ \_/\_/    \__\___/  | .__/|_|\__,_|\__, | (_)
                                  |_|            |___/    
---------------------------------------------------------------

1. RUNNING THE GAME --
   This version of Minesweeper has two game modes. The first is the "default" game mode. If the MineSweeperLauncher 
   is run with no command-line arguments, then this mode is selected by default. In this game mode, the size of the
   minefield is 16x16 spaces and it contains 25 mines. The other is the "custom" game mode.  In this game mode, the
   player may customize the length  and width of the minefield as well as the number of mines  that it contains. To
   launch  the game in this  game mode,  command-line arguments that specify these  measurements must be used.  The
   arguments should be provided in the following format:
   
                                        [length] [width] [mines]
                                              Ex. 20 25 10
   											  
   The "length", "width", and "mines" quantities must be positive integers with spaces between them in order for the
   launcher to load the game properly. The "length" and "width" measurements must be between 4 and 30. The amount of
   "mines" must be greater than 0 and less than half the number of spaces in the minefield (length*width) * 1/2.
   
   If invalid arguments are provided, an error message will be shown in the console. If proper arguments are given,
   then a window will appear, displaying the game. The customization of this game through its command-line arguments
   is its "Wow Factor".
   
   
2. PLAYING THE GAME --
   This game functions like a regular game of Minesweeper. The object of the game is to reveal all non-mine spaces
   either by stepping directly on them or by stepping on spaces that contiguously reveal them. The player left
   clicks to "take a step" and right clicks to place/remove a flag. If the player reveals all of the spaces, they
   win. If the player steps on a mine before they manage to reveal all safe spaces, then they lose. When the player
   steps on a space with no adjacent mines, all safe spaces surrounding that step will be revealed as well as safe
   spaces that are adjacent to mines in that area. These safe spaces that are adjacent to mines are marked by
   numbers. The numbers indicate the number of mines that surround that space. A space can be adjacent to up to 8
   mines (1 for each cardinal and intermediate direction around that space). The player's first step will never be
   a mine or a space that is adjacent to one. Flags should be used to mark spaces that the player thinks contains
   a mine. Be careful! There are a limited amount of flags- 1 for each mine in the minefield. 
   
   
3. SAVING / LOADING THE GAME --
   This game will automatically save the progress the player has made to a file called "saved_game.dat" when the
   game window is closed before the end the game ends. When the player runs the launcher again, this save file
   will be automatically loaded. The save is deleted when the game ends, regardless of whether the player wins
   or loses. The save file created by this application will be located in the program's folder and will be loaded
   automatically (if present) even if the player provides different command-line arguments.
   

4. HAVE FUN! -- 
   
   
   

   ____              _ _ _           
  / ___|_ __ ___  __| (_) |_ ___   _ 
 | |   | '__/ _ \/ _` | | __/ __| (_)
 | |___| | |  __/ (_| | | |_\__ \  _ 
  \____|_|  \___|\__,_|_|\__|___/ (_)                             
---------------------------------------------------------------
 			
        Giang Pham				
 							
                        Eleanor Simon			
 																				
                                            Tanmay Agrawal		
 																						
                                                                Katherine Wilson
   														
   