
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

package org.scify.memori.interfaces;

/**
 * Represents a game event (vs. user event), i.e. an event that was generated internally by the game and needs
 * to be handled (e.g. by the UI, or by another rule).
 *
 * Created by pisaris on 6/9/2016.
 */
public class GameEvent {
    /**
     * A string representation of the event type.
     */
    public String type;

    /**
     * A generic object, encapsulating all information related to the user event (meant to be type-cast by
     * implementations).
     */
    public Object parameters;

    /**
     * A long number indicating delay in the consumption of the event. A value of zero requires immediate consumption.
     */
    public long delay;

    /**
     * defines whether the event handler should wait until the event is fully processed before continuing to the next event.
     */
    public boolean blocking;

    public GameEvent(String type, Object parameters, long delay, boolean blocking) {
        this.type = type;
        this.parameters = parameters;
        this.delay = delay;
        this.blocking = blocking;
    }

    public GameEvent(String type, Object parameters) {
        this.type = type;
        this.parameters = parameters;
        this.delay = 0;
        this.blocking = false;
    }

    public GameEvent(String type) {
        this.type = type;
        this.parameters = null;
        this.delay = 0;
        this.blocking = false;
    }

}
