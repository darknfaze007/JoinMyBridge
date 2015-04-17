/*
 * Copyright 2015 John Misczak
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.misczak.joinmybridge;

import android.util.Log;

public class ImportUtilities {

    private static final String TAG = "ImportUtilities";
    private String[] numberIndices;
    private String[] toneIndices;
    private int numberPosition = 0, tonePosition = 0;
    private String[] numberComponents;
    private String[] endComponents;

    public String[] getNumberArray(String bridgeNumber) {

        numberComponents = bridgeNumber.split("(?<=[ ,x#*])|(?=[ ,x#*])");
        numberIndices = new String[3];
        toneIndices = new String[2];
        endComponents = new String[5];

        for (int component = 0; component < numberComponents.length; component++){
            if (!Character.isDigit(numberComponents[component].charAt(0))){
                if (numberComponents[component].equals("#") || numberComponents[component].equals("*")) {
                    toneIndices[tonePosition] = numberComponents[component];
                    tonePosition++;
                    continue;
                }
                else{
                    continue;
                }
            }
            else{
                numberIndices[numberPosition] = numberComponents[component];
                numberPosition++;
                continue;
            }
        }

        for (int assignment = 0; assignment < endComponents.length; assignment++) {
            if (assignment < 3) {
                Log.d(TAG, "Number Index #" + assignment + ": " + numberIndices[assignment]);
                endComponents[assignment] = numberIndices[assignment];
            }
            else {
                Log.d(TAG, "Tone Index #" + (assignment-3) + ": " + toneIndices[assignment-3]);

                endComponents[assignment] = toneIndices[assignment-3];
            }
        }

        return endComponents;

    }

}
