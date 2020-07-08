// Import libraries
import ddf.minim.*;
import ddf.minim.analysis.FFT;
import processing.core.PApplet;
import processing.core.PImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MusicVisualizer extends PApplet {
    // ========================
    // Declare global variables
    // ========================
    // Minim objects
    private Minim minim;
    private AudioPlayer[] audio;
    private FFT fft;

    // List of songs and counter
    private int counter = 0;
    private String[] mp3Files;
    private int songCount = 0;

    // Vertical center of canvas
    private int yCenter;

    // Horizontal center of canvas
    private int xCenter;

    // Keep track of whether or not the music is playing
    private boolean isPaused;
    private boolean isMuted;

    // Keep track of whether or not a song is repeating
    private boolean isRepeating;

    // Images for play/pause icons
    PImage play;
    PImage pause;

    // Images for mute/unmute icons
    PImage mute;
    PImage unmute;

    // Image for fast-forward icon
    PImage ff;

    // Image for rewind icon
    PImage rr;

    // Image for repeat button
    PImage repeat;

    // Coordinates for play/pause button
    private final int[] pauseCoors = {20, 20};
    private final int[] muteCoors = {90, 20};
    private final int[] ffCoors = {160, 20};
    private final int[] rrCoors = {300, 20};
    private final int[] repeatCoors = {230, 20};

    // Colors for channels
    private final int leftChannelColor = color(37, 152, 230);
    private final int rightChannelColor = color(26, 111, 214);

    // Colors for buttons
    private final int buttonColor = color(255);
    private final int buttonHoverAlpha = 175;

    // ===========
    // Main method
    // ===========
    public static void main(String[] args) {
        // Processing configuration in main class
        PApplet.main("MusicVisualizer");
    }

    // =================
    // Processing Config
    // =================
    public void settings() {
        // Choose the size for the canvas
        size(1000, 500);
    }

    public void setup() {
        // Set the background color of the canvas
        background(0);

        // Determine the vertical & horizontal center of the canvas
        yCenter = height / 2;
        xCenter = width / 2;

        // Load images
        play = loadImage("./icons/play.png");
        pause = loadImage("./icons/pause.png");
        mute = loadImage("./icons/mute.png");
        unmute = loadImage("./icons/unmute.png");
        ff = loadImage("./icons/ff.png");
        rr = loadImage("./icons/rr.png");
        repeat = loadImage("./icons/repeat.png");

        // Fill mp3Files array with song paths from a text file
        try {
            // Open the text file
            File txtFile = new File("./resources/songPaths.txt");
            Scanner reader1 = new Scanner(txtFile);
            Scanner reader2 = new Scanner(txtFile);

            // Find the number of songs in the file
            while (reader1.hasNextLine()) {
                reader1.nextLine();
                // Increment the number of songs
                songCount++;
            }

            // Create space in the mp3Files array
            mp3Files = new String[songCount];
            audio = new AudioPlayer[songCount];

            // Get each line of the file and add it to the mp3Files array
            int i = 0;
            while (reader2.hasNextLine()) {
                String data = reader2.nextLine();
                mp3Files[i] = data;
                i++;
            }

            // Close the file reader
            reader1.close();
            reader2.close();

        } catch (FileNotFoundException e) {
            // Print that an error occurred
            System.out.println("An error occurred when trying to open the text file.");
            e.printStackTrace();
        }

        // Minim configuration
        // Load the mp3 file
        minim = new Minim(this);

        // Create the audio players for each song
        for (int i = 0; i < songCount; i++) {
            audio[i] = minim.loadFile("./resources/" + mp3Files[i] + ".mp3");
        }

        // Play the first song
        if (!isPaused) audio[counter].play();
        else audio[counter].pause();

        // Initialize FFT
        fft = new FFT(audio[counter].bufferSize(), audio[counter].sampleRate());

        // Initially, the music will be playing and not muted
        isPaused = false;
        isMuted = false;
        isRepeating = false;
    }

    // =====================
    // Drawing on the canvas
    // =====================
    public void draw() {
        // Re-draw the background to hide previous frames
        background(0);

        // Check if the next song should be played
        if (!audio[counter].isPlaying() && !isPaused) {
            // Check if the current song should be repeated
            if (!isRepeating) {
                // Increment count, thereby moving to the next song
                counter++;

                if (counter > songCount - 1) {
                    // Return to the first song
                    counter = 0;
                }

                // Play the next song
                audio[counter].play();

                // Rewind the previous audio track
                int idxToRewind = (counter - 1) < 0 ? songCount - 1 : (counter - 1);
                audio[idxToRewind].rewind();
            } else {
                // Rewind the current song
                audio[counter].rewind();

                // Play the current song again
                audio[counter].play();
            }
        }

        // Grab the audio buffers for the left and right channels of the music
        float[] leftChannel = audio[counter].left.toArray();
        float[] rightChannel = audio[counter].right.toArray();

        // Use FFT to process new sample of music
        fft.forward(audio[counter].mix);

        // Draw the play/pause button
        drawPauseButtons();

        // Draw the mute/unmute buttons
        drawUnmuteButtons();

        // Draw the fast-forward button
        drawFFButton();

        // Draw the rewind button
        drawRRButton();

        // Draw the repeat button
        drawRepeatButton();

        // Iterate over every sample in the music
        for (int i = 0; i < leftChannel.length - 1; i++) {
            // Draw the left channel
            drawChannel(leftChannel, i, -1, leftChannelColor);

            // Draw the right channel
            drawChannel(rightChannel, i, 1, rightChannelColor);
        }

        // Iterate over the frequencies
        for (int i = 0; i < fft.specSize(); i++) {
            // Draw the current frequency
            drawFrequency(i);
        }
    }

    // Method to draw the channels of music
    private void drawChannel(float[] channel, int index, int direction, int color) {
        // Make the lines have a thickness of 2
        strokeWeight(2);

        // If you want lines instead of waves, do this line instead
        /* line(index,
                yCenter + (direction * abs(channel[index] * 50)),
                index + 1,
                yCenter + (direction * abs(channel[index + 1] * 50))); */

        // Draw 2 lines representing the channel of music at index
        for (int i = 1; i <= 2; i++) {
            // Set the color and transparency of the line
            stroke(color, (float) 100 / sq(i));

            // Draw the line
            line(index, yCenter, index + 1, yCenter + (direction * abs(channel[index + 1] * (200 * sq(i)))));
        }
    }

    // Method to draw circles that illustrate the frequency of the music
    private void drawFrequency(int index) {
        // Draw 2 circles representing the music's frequency
        for (int i = 1; i < 2; i++) {
            // Determine the color and transparency of the circle
            fill(255, (float) 100 / sq(i));
            stroke(255, (float) 100 / sq(i));

            // Draw the circle
            circle(xCenter, yCenter, fft.getBand(index) * (3 * sq(i)));
        }
    }

    // Method to draw the play/pause buttons
    private void drawPauseButtons() {
        // Check if the mouse is on top of the button
        int btnAlpha = mouseOver(pauseCoors[0], pauseCoors[1], pause.width, pause.height) ? buttonHoverAlpha : 255;

        // Tint the button with appropriate color and transparency
        tint(buttonColor, btnAlpha);

        // Determine which images to draw
        PImage playPauseImageToDraw = isPaused ? play : pause;

        // Draw the images
        image(playPauseImageToDraw, pauseCoors[0], pauseCoors[1]);
    }

    // Method to draw the mute/unmute buttons
    private void drawUnmuteButtons() {
        // Check if the mouse is on top of the button
        int btnAlpha = mouseOver(muteCoors[0], muteCoors[1], mute.width, mute.height) ? buttonHoverAlpha : 255;

        // Tint the button with appropriate color and transparency
        tint(buttonColor, btnAlpha);

        // Resize images
        mute.resize(50, 50);
        unmute.resize(50, 50);

        // Determine which images to draw
        PImage imageToDraw = isMuted ? unmute : mute;

        // Draw the images
        image(imageToDraw, muteCoors[0], muteCoors[1]);
    }

    // Method to draw the fast-forward button
    private void drawFFButton() {
        // Check if the mouse is on top of the button
        int btnAlpha = mouseOver(ffCoors[0], ffCoors[1], ff.width, ff.height) ? buttonHoverAlpha : 255;

        // Tint the button with appropriate color and transparency
        tint(buttonColor, btnAlpha);

        // Resize the image
        ff.resize(50, 50);

        // Draw the fast-forward icon
        image(ff, ffCoors[0], ffCoors[1]);
    }

    // Method to draw the fast-forward button
    private void drawRRButton() {
        // Check if the mouse is on top of the button
        int btnAlpha = mouseOver(rrCoors[0], rrCoors[1], rr.width, rr.height) ? buttonHoverAlpha : 255;

        // Tint the button with appropriate color and transparency
        tint(buttonColor, btnAlpha);

        // Resize the image
        rr.resize(50, 50);

        // Draw the fast-forward icon
        image(rr, rrCoors[0], rrCoors[1]);
    }

    // Method to draw the repeat button
    private void drawRepeatButton() {
        // Check if the mouse is on top of the button
        int btnAlpha = mouseOver(repeatCoors[0], repeatCoors[1], repeat.width, repeat.height) ? buttonHoverAlpha : 255;

        // Determine the color that the button should be
        int btnColor = isRepeating ? color(156, 228, 255): color(255);

        // Tint the button with appropriate color and transparency
        tint(btnColor, btnAlpha);

        // Resize the image
        repeat.resize(50, 50);

        // Draw the repeat button
        image(repeat, repeatCoors[0], repeatCoors[1]);
    }

    // Method to handle the mouse hovering over buttons
    private boolean mouseOver(int x, int y, int width, int height) {
        // Return if the mouse is inside of the given borders
        return (mouseX > x && mouseX < (x + width)) && (mouseY > y && mouseY < (y + height));
    }

    // Method to check if the mouse was pressed
    public void mouseClicked() {
        // If the mouse is over the button, toggle play/pause
        if (mouseOver(pauseCoors[0], pauseCoors[1], pause.width, pause.height)) {
            togglePause();
        }

        // If the mouse is over the mute/unmute buttons, toggle volume on/off
        if (mouseOver(muteCoors[0], muteCoors[1], mute.width, mute.height)) {
            toggleMute();
        }

        // If the mouse is over the fast-forward button, move to the next song
        if (mouseOver(ffCoors[0], ffCoors[1], ff.width, ff.height)) {
            fastForward();
        }

        // If the mouse is over the rewind button, move to the previous song
        if (mouseOver(rrCoors[0], rrCoors[1], rr.width, rr.height)) {
            rewind();
        }

        // If the mouse is over the repeat button, make the current song repeat
        if (mouseOver(repeatCoors[0], repeatCoors[1], repeat.width, repeat.height)) {
            toggleRepeat();
        }
    }

    // Method to toggle the music on/off
    private void togglePause() {
        // If the music is being played, stop it and vice versa
        isPaused = !isPaused;

        if (isPaused) audio[counter].pause();
        else audio[counter].play();
    }

    // Method to toggle the volume on/off
    private void toggleMute() {
        // If the volume is on, mute it and vice versa
        isMuted = !isMuted;

        if (isMuted) audio[counter].mute();
        else audio[counter].unmute();
    }

    // Method to fast-forward to the next song
    private void fastForward() {
        // Stop the current song
        audio[counter].pause();

        // Increment count, thereby moving to the next song
        counter++;

        if (counter > songCount - 1) {
            // Return to the first song
            counter = 0;
        }

        // Play the next song, as long as the player isn't paused
        if (!isPaused) audio[counter].play();
        else audio[counter].pause();

        // Rewind the previous audio track
        int idxToRewind = (counter - 1) < 0 ? songCount - 1 : (counter - 1);
        audio[idxToRewind].rewind();
    }

    // Method to move to the previous song
    private void rewind() {
        // Stop the current song
        audio[counter].pause();

        // Decrement count, thereby moving to the previous song
        counter--;

        if (counter < 0) {
            // Return to the first song
            counter = songCount - 1;
        }

        // Play the next song, as long as the player isn't paused
        if (!isPaused) audio[counter].play();
        else audio[counter].pause();

        // Rewind the previously played audio track
        int idxToRewind = (counter + 1) > songCount - 1 ? 0 : (counter + 1);
        audio[idxToRewind].rewind();
    }

    // Method to toggle repeat on a song on/off
    private void toggleRepeat() {
        // If the song is not repeating make it do so, and vice versa
        isRepeating = !isRepeating;
    }
}
