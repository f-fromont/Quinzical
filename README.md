QUINZICAL

This was a project designed in a virtual image of a ubuntu linux environment, this can still be run on a windows environment however some features like the 'festival' module which is used for the text-to-speech question reader will not work.
This was also created for a university course as a project, and I would like to thank my partner Luxman and our lecturer Nasser for the assistance provided in teaching the course.

TO-RUN
1. Please make sure you have the 'Quinzical.txt'& 'Quinzical-International.txt' text files containing all the question information in the same directory as the 'Quinzical.jar' and 'run.sh'.

2. In order to play the game with the text-to-speech voices (if on a linux device) the following must be done:
Enter in the command line: "sudo apt-get install festival"
Then enter: "sudo apt-get install festlex-oald"

3. At this link you can find other voice options http://festvox.org/packed/festival/2.5/voices/  
To install these voices you can use the following command: sudo copy -r <NAME_OF_VOICE> /usr/share/festival/english/voices
Note: This assumes that you have festival installed in usr/share if you have it installed somewhere else then replace usr/share with the path festival is installed on.

4. To run the program go into the terminal in the directory containing 'Quinzical.jar' and run the script './run.sh'. This will generate a few files in the directory that may be hidden that hold all the data, you do not need to worry about these.

5. The application will start up and generate the required file structure for the program to store information in and start up with the main menu view. 


What I have learnt from this Project

What it is like to work with someone else in developing a project.
How to develop a persistent state between instances of the program to track the users data when the program is shut and opened again.
Using the JavaFX framework to develop a nice GUI and making decisions suitable for the end-user.
Incorporating some common design patterns like the Singleton pattern, as well as the Model-View-Controller for designing gui applications.
learnt more about using java processes and even the system builder for incorporating Linux processes. 
I also learnt more about using Git effectively.

--ff-dev-45



