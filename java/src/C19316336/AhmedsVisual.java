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

        minScore1 = minScore;
        avgScore1 = avgScore;
        maxScore1 = maxScore;
        minScore = 0;
        avgScore = 0;
        maxScore = 0;

        //these loops increment the score variables based on the fft band values
        for (int i = 0; i < fft.specSize() * specLow; i++) {
            minScore += fft.getBand(i);
        }

        for (int i = (int) (fft.specSize() * specLow); i < fft.specSize() * specMid; i++) {
            avgScore += fft.getBand(i);
        }

        for (int i = (int) (fft.specSize() * specMid); i < fft.specSize() * specHi; i++) {
            maxScore += fft.getBand(i);
        }

        if (minScore1 > minScore) {
            minScore = minScore1 - scoreDecreaseRate;
        }

        if (avgScore1 > avgScore) {
            avgScore = avgScore1 - scoreDecreaseRate;
        }

        if (maxScore1 > maxScore) {
            maxScore = maxScore1 - scoreDecreaseRate;
        }
        float scoreGlobal = 0.66f * minScore + 0.8f * avgScore + 1 * maxScore;
        background(minScore / 100, avgScore / 100, maxScore / 100);

        //storing the first band of fft
        float previousBandValue = fft.getBand(0);
        float dist = -25;
        float multiplier = 2;

        for (int i = 1; i < fft.specSize(); i++) {

            float bandValue = fft.getBand(i) * (1 + (i / 50));
            stroke(100 + minScore, 100 + avgScore, 100 + maxScore, 255 - i);
            strokeWeight(1 + (scoreGlobal / 100));

            //line that goes from center to bottom left
            line(0, height - (previousBandValue * multiplier), dist * (i - 1), 0, height - (bandValue * multiplier), dist * i);
            line((previousBandValue * multiplier), height, dist * (i - 1), (bandValue * multiplier), height, dist * i);
            line(0, height - (previousBandValue * multiplier), dist * (i - 1), (bandValue * multiplier), height, dist * i);


            //line that goes from center to top left
            line(0, (previousBandValue * multiplier), dist * (i - 1), 0, (bandValue * multiplier), dist * i);
            line((previousBandValue * multiplier), 0, dist * (i - 1), (bandValue * multiplier), 0, dist * i);
            line(0, (previousBandValue * multiplier), dist * (i - 1), (bandValue * multiplier), 0, dist * i);


            //line that goes from center to bottom right
            line(width, height - (previousBandValue * multiplier), dist * (i - 1), width, height - (bandValue * multiplier), dist * i);
            line(width - (previousBandValue * multiplier), height, dist * (i - 1), width - (bandValue * multiplier), height, dist * i);
            line(width, height - (previousBandValue * multiplier), dist * (i - 1), width - (bandValue * multiplier), height, dist * i);


            //line that goes from center to top right
            line(width, (previousBandValue * multiplier), dist * (i - 1), width, (bandValue * multiplier), dist * i);
            line(width - (previousBandValue * multiplier), 0, dist * (i - 1), width - (bandValue * multiplier), 0, dist * i);
            line(width, (previousBandValue * multiplier), dist * (i - 1), width - (bandValue * multiplier), 0, dist * i);

            previousBandValue = bandValue;
        }

        //invoking display method of each bgbox to draw it on screen
        for (int i = 0; i < noOfBgBoxes; i++) {
            float intensity = fft.getBand(i % ((int) (fft.specSize() * specHi)));
            backgroundBoxes[i].display(minScore, avgScore, maxScore, intensity, scoreGlobal);
        }
    }

    class BackgroundBoxes {
        float startingZ = -10000, maxZ = 50, x, y, z, sizeX, sizeY;

        BackgroundBoxes(float x, float y, float sizeX, float sizeY) {

            this.x = x;
            this.y = y;
            this.z = random(startingZ, maxZ);
            this.sizeX = sizeX;
            this.sizeY = sizeY;
        }