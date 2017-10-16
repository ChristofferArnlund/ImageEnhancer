package com.softa.imageenhancer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Bitmap.Config;
import android.util.Log;
import android.widget.EditText;

import static android.R.id.input;

public class TestEnhancer implements ImageEnhancer {
	private static final int ACTION_4 = 4;
    private static final int ACTION_3 = 3;
	private static final int ACTION_2 = 2;
	private static final int ACTION_1 = 1;
	private static final int ACTION_0 = 0;
	private int progress;

	public TestEnhancer() {

	}

	public Bitmap enhanceImageHSV(Bitmap theImage, int action, int N) {

		// Set progress
		progress = 0;
		//int N=8;
		// Get the image pixels
		int height = theImage.getHeight();
		int width = theImage.getWidth();
		Log.d("DEBUG", "Image size is " + width + "px by " + height + "px." );
		int[] pixels = new int[height * width];
		theImage.getPixels(pixels, 0, width,0,0, width, height);

		progress = 5;

		Log.d("DEBUG", "pixels length = " + pixels.length);

		//Convert pixels to brightness values;
		float[][] hsvPixels = convertToHSV(pixels);
		//float[][] hsvPixels= new float[4][3];
		//hsvPixels[0][2]=(float) 0.90;
		//hsvPixels[1][2]=(float) 0.70;
		//hsvPixels[2][2]=(float) 0.40;
		//hsvPixels[3][2]=(float) 0.20;


		progress = 40;

		Log.d("DEBUG", "hsvPixels length = " + hsvPixels.length);

		// Here below some manipulations of the image is made as examples.
		// This should be changed to your image enhancement algorithms.
		if (action == ACTION_0 || action == ACTION_1 || action == ACTION_2) {
            float maxValue = 0;
            for (int i = 0; i < hsvPixels.length; i++) {
                if (maxValue < hsvPixels[i][action]) maxValue = hsvPixels[i][action];
            }
            Log.d("DEBUG", "maxValue of hsvPixels = " + maxValue);
            progress = 60;

            for (int i = 0; i < hsvPixels.length; i++) {
                hsvPixels[i][action] = maxValue - hsvPixels[i][action];
                pixels[i] = Color.HSVToColor(hsvPixels[i]);
            }
        } else if(action == ACTION_3) {
            for (int i = 0; i < hsvPixels.length; i++) {
                hsvPixels[i][1] = 0; // Set color saturation to zero
                pixels[i] = Color.HSVToColor(hsvPixels[i]);
				progress = 60;
            }
            Log.d("DEBUG", "saturation zeroed");
			progress = 65;
        }else{
			//-----------------------------------------------------------
			//V-tranform
			//N=8; //May be able to change it later

			List<Pair> sortedV = new ArrayList<Pair>();
			for(int i=0;i<hsvPixels.length;i++) {
				//adds index,value to SortedV list.(not sorted yet)
				sortedV.add(i,new Pair(i,(double) hsvPixels[i][2]));
			}
			Collections.sort(sortedV);
			progress = 70;


			int size = hsvPixels.length;
			int intervall=size/N;
			if (N>size){
				N=size;
			}
			int n=1;
			double lastWantedValue=0;
			double lastActual=0;
			double m=0;
			while(n<=N) {
				int IntervalStart = (int) Math.floor(intervall * (n - 1));
				int IntervalStop = (int) Math.floor(IntervalStart + intervall-1);
				if (n == N) {
					IntervalStop = hsvPixels.length-1;
				}
				double wantedValue = (double) n / (double) N;
				Pair p = sortedV.get((((int) Math.floor(IntervalStop))));
				double actual = p.getValue();
				//multiplier = the need multiplier to achieve actual wanted value
				double multiplier = (wantedValue-lastWantedValue) / (actual-lastActual);
				//double multiplier = 10;
				if (Double.isInfinite(multiplier)) {
					multiplier=100;
				}
				//update m value
				m=lastWantedValue-multiplier*lastActual;
				//update values
				lastWantedValue=wantedValue;
				lastActual=actual;
				for (int k=IntervalStart;k<=IntervalStop;k++) {
					double newValue=sortedV.get(k).getValue()*multiplier+m;
					sortedV.get(k).setValue(newValue);
				}
				n++;
			}

			//Reverse sort, Sort by original index
			//Pair just mock
			Collections.sort(sortedV, new Pair(1,1));
			for (int i = 0; i < hsvPixels.length; i++) {
				hsvPixels[i][2]=(float) sortedV.get(i).getValue();
				pixels[i] = Color.HSVToColor(hsvPixels[i]);

			}
			progress = 75;
		}

		progress = 80;
		Log.d("DEBUG","creating BITMAP,width x height "+width+" "+height);
        Bitmap modifiedImage = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		modifiedImage.setPixels(pixels, 0, width, 0, 0, width, height);

		progress = 100;
		return modifiedImage;
	}

	private float[][] convertToHSV(int[] pixels) {
		float[][] hsvPixels = new float[pixels.length][3];
		for (int i = 0; i < pixels.length; i++) {
			Color.RGBToHSV(Color.red(pixels[i]), Color.green(pixels[i]), Color.blue(pixels[i]), hsvPixels[i]);

		}
		return hsvPixels;
	}

	public int getProgress() {
		// Log.d("DEBUG", "Progress: "+progress);
		return progress;
	}

	@Override
	public Bitmap enhanceImage(Bitmap bitmap, int configuration, int N) {
		switch (configuration) {
		case ACTION_0:
			return enhanceImageHSV(bitmap, 0,N);
		case ACTION_1:
			return enhanceImageHSV(bitmap, 1,N);
		case ACTION_2:
			return enhanceImageHSV(bitmap, 2,N);
        case ACTION_3:
            return enhanceImageHSV(bitmap, 3,N);
					//own implementation
			case ACTION_4:
				return enhanceImageHSV(bitmap, 4,N);
		default:
			return enhanceImageHSV(bitmap, 0,N);
		}
	}

	@Override
	public String[] getConfigurationOptions() {
		return new String[]{ "Action 0", "Action 1", "Action 2", "Action 3","V-transform"};
	}

}
