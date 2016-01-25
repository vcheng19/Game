###Project Journal
#####Time Review
I spent around 32-36 hours on this porject, starting on the January 20th and finishing on the 23rd. I spent my time at the beginning looking through Prof. Duvall's example code and understanding how JavaFX works. I want to say that I spent around 30 hours testing, debugging, and coding, where I would code a scene or an extra feature and then test and debug right away; so I am not too sure exactly how much time I spent on each one. Once I finished the code, I spent another 6 hours getting rid of code smells and duplicated code. I do not think I managed my code well, considering that I created one big class instead of multiple smaller classes, which is something I would like to improve on in the future. I think starting the project was the hardest for me because I was not sure what I wanted to do at the beginning, for example I did not know how to use JavaFX and I did not know how to start my game; but it got easier as I got more familiar with JavaFX. I think it was good for me to spend the time looking through the example code and experimenting with that before, but I think I wasted a lot of time trying to navigate my class. Since my class was so big, I think it got cluttered so that even though my methods were small and convenient, it was very annoying to search through the entire class for a certain method or variable.

#####Commits
I did not commit at all until I had finished the code, so I went from nothing in my game_vc54 repository to my entire project in it with almost no comments. I committed three times: once with my code, another with my images, and then the third was the updated versions of everything. I was a little confused about the commit messages because although I typed up different commit messages as I went through my terminal, when I checked on github afterwards all my commit messages had said "Updating README.md" and I was not sure why. My first two commits were to upload everything I had and to make sure I remembered how to commit things onto github. The second commits were with my updated code and images after I had gotten rid of a few bugs and had gotten rid of duplicate code. Looking back, I do not think my commit messages accurately represent my project's "story" because they were not very explanatory. One was "Updating README.md" and another one was "Main class file" which does nothing to show the progress of my code. Until now, I had been unaware that we were supposed to keep our github repository up to date with our local files. The purpose of my commits were just to upload my final project to github. I have not been able to find where to find the size of our commits; but considering that I committed everything at once, I am going to say that my commit size was unreasonable for someone else to review it before allowing it into the respository. 

#####Conclusions
I think I worked pretty effectively on the project. There are some design aspects that I would like to add if I had more time, so if anything I wish that I started the project earlier. I also think that I overestimated the size of this project. I thought it was going to be a lot harder than it was because I let everyone else stress me out about it and because JavaFX was a new and foreign thing to me. In the future, I think I should approach it with a much more level head rather than stressing it. The part of my code that required the most editing was near the end when I had to update all the booleans. Because I had to keep adding different booleans as checks in my code so that certain methods called in my step() method would not be run unless I was on a certain level of the game. To be a better designer, I need to stop putting all my code in one big method or one big class. It also took a lot of trial and error to figure out how to make the pizzas move smoothly and on the screen. After a lot of testing and parameter changing, I figured out that they moved the best by not making every step random but by having them walking the same way for X amount of steps and then randomly choosing the next direction. By habit, in my other Computer Science classes I usually would just write one method, but since it was part of the assignment for this project I had to seperate my method into multiple smaller ones; however, I only used one class which I actually did not realized until I finished the assignment. I also need to start using better variable names (which was also a first for me in this assignment since I usually use random names that are completely unrelated to the variable itself). The one part I would like to improve on my project is the organization; like I mentioned before I would prefer to have multiple classes or have the methods in my class in a more organized order.


put this in the design section instead: I would also like to add a restart button so that when there is a game over, the player can restart rather than closing and reopening it; as well as a menu at the beginning with the backstory and the rules of the game.

###Design Review
#####Status
I think my code is readable, but that may be because I wrote it so I know what I was trying to do. However, I do notice some inconsistencies in my code. I know the rule for naming methods that perform an action is to have a verb and then the object, so most of my methods are named liked createPizzas() or setCoordinates(), but sometimes I did not follow that and instead had pizzaDelete() or boxMove(). I think the dependencies in my code are not very clear since I have a lot of global variables, but I also do think they are clear since a lot of methods will call other methods inside it if it is dependent on it. For example, my pizzaMove() method will call the chooseDirection() method at the beginning because it is dependant on the chooseDirection() method to choose the direction the pizza will move. I think the layout is quite consistent, every method, if statement, and for loop is indented and each method has a space in between. The code seems consistent in terms of style because I wrote all of it. I think it is easy to read and to understand what individual methods do in my code, but it may be confusing in terms of the ordering of the methods. An example of the code would be

Reflect on the coding details of the project by reviewing the code.
Is the code generally consistent in its layout, naming conventions and descriptiveness, and style?
Is the code generally readable (i.e., does it do what you expect, does it require comments to understand)?
Are the dependencies in the code clear and easy to find or do they exist through "back channels" (e.g., global variables, order of method call, or get methods instead of parameters)?
Describe two pieces of code in detail:
Describe the purpose of this code in the overall project.
What makes this code easy (or hard) to read and understand?

#####Design
Reflect on how the program is currently organized.
Describe the overall design of the program, without referring to specific data structures or actual code.
As briefly as possible, describe how to add a new level to the game.
Justify why your overall code is designed the way it is or what issues you wrestled with if you think its design is lacking in some way. Are there any assumptions or dependencies in your code that impact the overall design of the program?
Describe two features from the assignment specification in detail:
What classes or resources are required to implement this feature?
Describe the design of this feature in detail (what implementation details are encapsulated? what assumptions are made? do they limit its flexibility?).

#####Alternate Designs
Reflect on alternate designs for the project based on your analysis of the current design.
Describe two design decisions you made, or wish you had done differently, in detail:
What alternate designs were considered?
What are the trade-offs of the design choice (describe the pros and cons of the different designs)?
Which would you prefer and why (it does not have to be the one that is currently implemented)?
What are the three most important bugs that remain in the project's design or implementation?
