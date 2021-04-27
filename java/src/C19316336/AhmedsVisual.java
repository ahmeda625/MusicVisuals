package C19316336;

import ie.tudublin.Visual;
import ie.tudublin.VisualException;

public class AhmedsVisual extends Visual {

    float specLow = 0.03f, specMid = 0.125f, specHi = 0.20f;
    float minScore = 0, avgScore = 0, maxScore = 0;
    float minScore1 = minScore, avgScore1 = avgScore, maxScore1 = maxScore;
    float scoreDecreaseRate = 25;
    int noOfBgBoxes = 500;
    BackgroundBoxes[] backgroundBoxes;

    public void settings()
    {
        fullScreen(P3D, SPAN);
    }

    public void setup() {

        //first we have to start the minim
        startMinim();

        //loading audio file in audioplayer
        loadAudio("JimJoseph.mp3");

        //making an array of background boxes and populating it
        backgroundBoxes = new BackgroundBoxes[noOfBgBoxes];

        for (int i = 0; i < noOfBgBoxes; i += 4) {
            backgroundBoxes[i] = new BackgroundBoxes(0, height / 2, 10, height);
        }

        for (int i = 1; i < noOfBgBoxes; i += 4) {
            backgroundBoxes[i] = new BackgroundBoxes(width, height / 2, 10, height);
        }
        for (int i = 2; i < noOfBgBoxes; i += 4) {
            backgroundBoxes[i] = new BackgroundBoxes(width / 2, height, width, 10);
        }
        for (int i = 3; i < noOfBgBoxes; i += 4) {
            backgroundBoxes[i] = new BackgroundBoxes(width / 2, 0, width, 10);
        }

        background(0);
    }

    public void keyPressed()
    {
        if (key == ' ')
        {
            getAudioPlayer().cue(0);
            getAudioPlayer().play();
        }
    }

    public void draw() {
        try {
            //making sure the fft is not null whenever draw() is called
            calculateFFT();
        } catch (VisualException e) {
            e.printStackTrace();
        }