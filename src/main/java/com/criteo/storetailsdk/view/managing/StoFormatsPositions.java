package com.criteo.storetailsdk.view.managing;

import com.criteo.storetailsdk.view.StoFormatType;
import com.criteo.storetailsdk.view.models.StoFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mikhailpogorelov on 02/02/2018.
 */

public class StoFormatsPositions {

    private final String TAG = "StoFormatsPositions";

    private List<Integer>   finalPositionsList;
    private List<Integer>   fixedPositionsList;
    private int             repeatingPosition;

    public StoFormatsPositions() {
        finalPositionsList = new ArrayList<>();
        fixedPositionsList = new ArrayList<>();
        repeatingPosition = 0;
    }

    public StoFormatsPositions addFixedPositions(Integer... positions) {
        for (Integer position: positions) {
            addFixedPosition(position);
        }
        return this;
    }

    public StoFormatsPositions addFixedPosition(Integer position) {
        if (position > 0) {
            if (!fixedPositionsList.contains(position - 1)) {
                fixedPositionsList.add(position - 1);
                Collections.sort(fixedPositionsList);
            }
        }
        return this;
    }

    public StoFormatsPositions enableRepeatingPositions(int repeat) {
        repeatingPosition = repeat;
        return this;
    }

    public void generatePositions(List<StoFormat> formats, int dataCount) {
        finalPositionsList.clear();
        finalPositionsList.addAll(fixedPositionsList);

        int fixedPositionsSize = finalPositionsList.size();
        int nbFormats = formats.size();

        if (fixedPositionsSize > nbFormats) {
            while (finalPositionsList.size() > nbFormats) {
                finalPositionsList.remove(finalPositionsList.size() - 1);
            }
        } else if (repeatingPosition > 0 && fixedPositionsSize > 0 && nbFormats > fixedPositionsSize) {
            while (nbFormats > fixedPositionsSize) {
                int lastPosition = finalPositionsList.get(fixedPositionsSize - 1) + repeatingPosition;

                if (!finalPositionsList.contains(lastPosition)) {
                    finalPositionsList.add(lastPosition);
                }
                fixedPositionsSize++;
            }
        }
        Collections.sort(finalPositionsList);

        // Update format positions
        for (int i = 0; i < nbFormats; i++) {
            if (finalPositionsList.get(i) < dataCount) {
                formats.get(i).setPosition(finalPositionsList.get(i));
            } else {
                break;
            }
        }

        // Remove all formats that can't be shown
        for (int i = formats.size() - 1; i >= 0; i--) {
            if (formats.get(i).getPosition() == -1) {
                formats.remove(i);
                finalPositionsList.remove(i);
            }
        }
    }

    public void generatePositions(List<StoFormat> formats, int dataCount, final int maxCellPerRow) {
        finalPositionsList.clear();
        finalPositionsList.addAll(fixedPositionsList);

        int fixedPositionsSize = finalPositionsList.size();
        int nbFormats = formats.size();

        if (fixedPositionsSize > nbFormats) {
            while (finalPositionsList.size() > nbFormats) {
                finalPositionsList.remove(finalPositionsList.size() - 1);
            }
        } else if (repeatingPosition > 0 && fixedPositionsSize > 0 && nbFormats > fixedPositionsSize) {
            while (nbFormats > fixedPositionsSize) {
                int lastPosition = finalPositionsList.get(fixedPositionsSize - 1) + repeatingPosition;

                if (!finalPositionsList.contains(lastPosition)) {
                    finalPositionsList.add(lastPosition);
                }
                fixedPositionsSize++;
            }
        }
        Collections.sort(finalPositionsList);

        HashMap<StoFormatType, Integer> retainedSizeFormat = new HashMap<StoFormatType, Integer>() {{
            put(StoFormatType.stoBanner, maxCellPerRow - 1);
            put(StoFormatType.stoButterfly, 1);
            put(StoFormatType.stoVignette, 0);
        }};


        int retainedSize = 0;

        // Update format positions
        for (int i = 0; i < nbFormats; i++) {
            int position = finalPositionsList.get(i);
            int finalPosition = (maxCellPerRow * position) - retainedSize;

            if (finalPosition < dataCount) {
                formats.get(i).setPosition(finalPosition);
            } else {
                break;
            }
            Integer retainedValue = retainedSizeFormat.get(formats.get(i).getFormatType());
            retainedSize += (retainedValue != null) ? retainedValue : 0;
        }

        // Remove all formats that can't be shown
        for (int i = formats.size() - 1; i >= 0; i--) {
            if (formats.get(i).getPosition() == -1) {
                formats.remove(i);
                finalPositionsList.remove(i);
            }
        }
    }

    public int getFormatPositionsBefore(List<StoFormat> stoFormats, int currentPosition) {
        int countFormat = 0;
        for (StoFormat format: stoFormats) {
            if (format.getPosition() < currentPosition) {
                countFormat++;
            } else {
                return countFormat;
            }
        }
        return countFormat;
    }

    public static StoFormatsPositions defaultPosition() {
        return new StoFormatsPositions()
                .addFixedPositions(1, 5, 7)
                .enableRepeatingPositions(10);
    }
}
