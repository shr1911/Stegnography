package com.example.stegnography;


import android.graphics.Bitmap;
import android.util.Log;
import android.widget.EditText;

public class Alogorithm {
    public static String END_MESSAGE_CONSTANT = "#@";
    public static String START_MESSAGE_CONSTANT = "@#";

    public static Bitmap encode(Bitmap bitmap, String InputDataString){

        int pic_width = bitmap.getWidth();
        int pic_height = bitmap.getHeight();

        Log.d("SHRwidth",String.valueOf(pic_width));
        Log.d("SHRheight",String.valueOf(pic_height));

        int[] pixels = new int[pic_width * pic_height];

        bitmap.getPixels(pixels, 0, pic_width, 0,  0, pic_width, pic_height);

        InputDataString = START_MESSAGE_CONSTANT + InputDataString;
        InputDataString += END_MESSAGE_CONSTANT;

        byte[] msgBytes = InputDataString.getBytes();

        boolean msgEnded = false;
        int msgIndex=0, shiftIndex=4;
        int R, G, B;
        int[] toShift = { 6, 4, 2, 0 };

        for(int index=0 ; index<pixels.length ; index++) {
            int msgdata = 0;
            if(index==0){
                Log.d("Pixel "+index,pixels[index]+"");
            }
            //R =   ((pixels[index] >> 16) & 0xff);     //bitwise shifting
            //G =   ((pixels[index] >> 8) & 0xff);
            //B =   (pixels[index] & 0xff);
            int dataToOR = 00;
            for (int j = 0; j < 3; j++) {
                if (!msgEnded) {
                    msgdata = ((msgBytes[msgIndex] >> toShift[(shiftIndex++) % toShift.length]) & 0x3);
                    dataToOR = Integer.parseInt(Integer.toString(dataToOR) + Integer.toString(0) + Integer.toString(msgdata));

                    if (shiftIndex % toShift.length == 0) {
                        msgIndex++;
                    }
                    if (msgIndex == msgBytes.length) {
                        msgEnded = true;
                    }
                }
            }
            String hexDataToOR = "0x"+ dataToOR;
            int hexToInt = Integer.parseInt(hexDataToOR.substring(2), 16);
            if(index==0){
                Log.d("dataToOR", hexDataToOR+"");
            }


            pixels[index] = (pixels[index] & 0xfffcfcfc) | hexToInt;

            if(index==0){
                Log.d("Pixel " + index, pixels[index] + "");
            }
            //pixels[index] = 0xff000000 | (R << 16) | (G << 8) | B;
        }

        bitmap.setPixels(pixels, 0 , pic_width, 0 , 0, pic_width, pic_height );

        return bitmap;
    }

    public static String decode(Bitmap selectedBitmap) {
        StringBuilder dataStringDecoded = new StringBuilder();
        StringBuilder decodedText = new StringBuilder();
        int shiftIndex = 3;
        int[] toShiftDecode = {16, 8, 0};
        int count = 0;
        boolean flagBreak = false;
        byte msgdataString;
        int dataForString;
        boolean checkStart = false;

        int pic_width = selectedBitmap.getWidth();
        int pic_height = selectedBitmap.getHeight();

        int[] pixels = new int[pic_width * pic_height];

        selectedBitmap.getPixels(pixels, 0, pic_width, 0, 0, pic_width, pic_height);


        for (int h = 0; h < 4; h++) {
            dataForString = pixels[h] & 0x00030303;
            for (int j = 0; j < 3; j++) {
                msgdataString = (byte) ((dataForString >> toShiftDecode[(shiftIndex++) % toShiftDecode.length]) & 0x03);
                for (int k = 1; k >= 0; k--) {
                    int app = (msgdataString >> k) & 1;
                    if (app == 1) {
                        dataStringDecoded.append(1);
                        count++;
                    } else {
                        dataStringDecoded.append(0);
                        count++;
                    }
                }
                if (count == 8) {
                    //Log.d("BinaryString:",dataStringDecoded.toString());
                    count = 0;

                    decodedText.append((char) Integer.parseInt(dataStringDecoded.toString(), 2));
                    dataStringDecoded.setLength(0);
                    Log.d("finalString:", decodedText.toString());
                    if (decodedText.length() >= 2) {
                        Log.d("BREAK", decodedText.charAt(decodedText.length() - 2) + "" + decodedText.charAt(decodedText.length() - 1));
                        if ((decodedText.charAt(decodedText.length() - 2) + "" + decodedText.charAt(decodedText.length() - 1)).equals(START_MESSAGE_CONSTANT)) {
                            checkStart = true;
                        }
                    }
                }
            }
        }

        if(checkStart==true){
            decodedText.setLength(0);
            dataStringDecoded.setLength(0);

            for (int index = 0; index <= pixels.length; index++) {
                dataForString = pixels[index] & 0x00030303;
                for (int j = 0; j < 3; j++) {
                    msgdataString = (byte) ((dataForString >> toShiftDecode[(shiftIndex++) % toShiftDecode.length]) & 0x03);
                    for (int k = 1; k >= 0; k--) {
                        int app = (msgdataString >> k) & 1;
                        if (app == 1) {
                            dataStringDecoded.append(1);
                            count++;
                        } else {
                            dataStringDecoded.append(0);
                            count++;
                        }
                    }
                    if (count == 8) {
                        Log.d("BinaryString:", dataStringDecoded.toString());
                        count = 0;

                        decodedText.append((char) Integer.parseInt(dataStringDecoded.toString(), 2));
                        dataStringDecoded.setLength(0);
                        Log.d("finalString:", decodedText.toString());
                        if (decodedText.length() > 2) {
                            Log.d("BREAK", decodedText.charAt(decodedText.length() - 2) + "" + decodedText.charAt(decodedText.length() - 1));
                            if ((decodedText.charAt(decodedText.length() - 2) + "" + decodedText.charAt(decodedText.length() - 1)).equals(END_MESSAGE_CONSTANT)) {
                                Log.d("FlAG before", flagBreak + "");
                                flagBreak = true;
                                break;
                            }
                        }
                    }
                    if (index == 0) {
                        Log.d("BinaryString:", dataStringDecoded.toString());
                        Log.d("Forj=" + j, msgdataString + "");
                    }
                }

                if (index == 0) {
                    Log.d("dataForString:", dataForString + "");
                }
                if (flagBreak == true) {
                    Log.d("I am in", flagBreak + "");
                    break;
                }
            }

            decodedText.deleteCharAt(1);
            decodedText.deleteCharAt(0);
            decodedText.deleteCharAt(decodedText.length() - 1);
            decodedText.deleteCharAt(decodedText.length() - 1);

            Log.d("FINAL VALA STRING :)", decodedText.toString());
            return decodedText.toString();
        }
        else{
            return "There is no to show!";
            }

    }

}
