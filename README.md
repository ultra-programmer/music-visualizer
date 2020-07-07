# music-visualizer
A program that can play a given list of songs while displaying the song's frequency and channels via shapes and lines.
Utilizes the Processing and Minim libraries (not included). 

Place desired mp3 files in resources folder. Then specify the names of the files excluding the mp3 file extension to 
play them in that order in `resources/songPaths.txt`.

## Installation Instructions
1. Download the Java Development Kit for [Windows](https://drive.google.com/file/d/1vJrxn9tQhOxM_HJVTN0-Uld1aCxeiSKa/view) or [Mac](https://drive.google.com/file/d/1BAnsoIs9-pu2WoZJHUbNSY9c2LQ1-wMe/view).
2. Download [IntelliJ community edition](https://www.jetbrains.com/idea/download/#section=windows), run the exe, and follow the instructions to download the software.
3. Download the [Processing Library](https://processing.org/download/) and unzip the file.
4. Download [Minim](http://code.compartmental.net/tools/minim/) by scrolling down and clicking the Minim zip file, and unzipping it.
5. Download and unzip this GitHub repository's code.

## Running Instructions
1. Open IntelliJ and either
    * Select the open/import option.
    * Click `File > New > Project from Existing Sources`.
2. Select the folder containing the repository's source code and keep clicking `next` until the project is imported.
3. Select `File > Project Structure`.
4. Click the `Libraries` option on the left-hand side.
5. Click the `+` icon on the top bar (it will be on the left of a `-` icon)
6. Click `Java` to choose a Java Library.
7. Navigate to the place where Processing is stored.
8. Open `processing > core > lib` and select the `core.jar` file. 
9. Select okay, okay again, apply, and then okay one final time.
10. Click the same `+` button again and select `Java`.
11. Navigate to the directory Minim is stored.
12. Open `minim > library`.
13. Select all of the `.jar` files by clicking the top one and then shift clicking the last one.
14. Click okay, okay again, apply, and then okay one final time.
15. Close the Project Structure window.
16. On the left-hand side of IntelliJ, expand the `music-visualizer-master` folder, and then the `src` folder.
17. Double click on the `MusicVisualizer` file inside of `src` to open it.
18. If all went well, you should be able to click the ▶ (play) icon on line 10 next to the `public class MusicVisualizer extends PApplet` line to run the code.

**Note:** To change the songs that are being played, you must first download the mp3 file and add it to the resources directory, and then list the file's name excluding the `.mp3` extension in `resources/songPaths.txt` to have it play when the Java class is run.

### Sample song credits:
**Aria of the Soul | Super Smash Bros. Ultimate:** 31 Horas Music
<br>https://www.youtube.com/watch?v=DZUqZluxqDs

**ARMS Grand Prix Official Theme Song | Super Smash Bros. Ultimate:** 31 Horas Music
<br>https://www.youtube.com/watch?v=o0bQdpTkLTc

**K.K. Jazz (Aircheck):** PieJee1
<br>https://www.youtube.com/watch?v=jC2KB0CtXq4&t=8s