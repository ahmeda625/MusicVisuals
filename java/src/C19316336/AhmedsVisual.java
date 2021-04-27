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
    