
/**
 * Copyright 2016 SciFY.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.scify.memori;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class HighScoreHandler {

    public void updateHighScore(TimeWatch watch) {
        long passedTimeInSeconds = watch.time(TimeUnit.SECONDS);
        String highScore = FileHandler.readHighScoreForCurrentLevel();
        if (highScore == null || Objects.equals(highScore, ""))
            highScore = "99:00:00";
        System.out.println("highScore " + highScore);
        System.out.println("highScore " + TimeToSeconds(highScore));
        System.out.println("time: " + passedTimeInSeconds);
        if(passedTimeInSeconds < TimeToSeconds(highScore))
            FileHandler.setHighScoreForLevel(String.valueOf(ConvertSecondToHHMMSSString((int) passedTimeInSeconds)));
    }

    public String getHighScoreForLevel(String level) {
        return FileHandler.readHighScoreForLevel(level);
    }

    private String ConvertSecondToHHMMSSString(int nSecondTime) {
        return LocalTime.MIN.plusSeconds(nSecondTime).toString();
    }

    private long TimeToSeconds(String time) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date reference = null;
        Date scoreDate = null;
        try {
            scoreDate = dateFormat.parse(time);
            reference = dateFormat.parse("00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long seconds = (scoreDate.getTime() - reference.getTime()) / 1000L;
        return seconds;
    }
}
