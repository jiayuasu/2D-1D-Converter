

import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.OutputStreamWriter;

import java.util.Scanner;


/*
 * This file is trying to encode a large set of spatial records with hilbert curve ID. 
 */
public class hilbertMarker {


	/**
	   * Compute Hilbert curve value for a point (x, y) in a square of size
	   * (n*n)
	   * @param n - Size of the square
	   * @param x - x dimension (short)
	   * @param y - y dimension (short)
	   * @return
	   */
	  static int computeHValue(int n, int x, int y) {
	    int h = 0;
	    for (int s = n/2; s > 0; s/=2) {
	      int rx = (x & s) > 0 ? 1 : 0;
	      int ry = (y & s) > 0 ? 1 : 0;
	      h += s * s * ((3 * rx) ^ ry);

	      // Rotate
	      if (ry == 0) {
	        if (rx == 1) {
	          x = n-1 - x;
	          y = n-1 - y;
	        }

	        //Swap x and y
	        int t = x; x = y; y = t;
	      }
	    }
	    return h;
	  }
	   static int locationMapping (double axisMin, double axisLocation,double axisMax,int gridResolution)
	  {
		  Double gridLocation;
		  gridLocation=(axisLocation-axisMin)*gridResolution/(axisMax-axisMin);
		  return gridLocation.intValue();
	  }
	
	public static void main(String[] args) throws IOException
	{
		double minX=0.0;
		double minY=0.0;
		double maxX=0.0;
		double maxY=0.0;
		String inputPath=args[0];
		String outputPath=args[1];
		int longitudeOffset=Integer.parseInt(args[2]);
		int latitudeOffset=Integer.parseInt(args[3]);
		String InplaceGeneration = args[4];
		long counter=0;
		int statusInterval=100000;
	/*
	 * First round, go through the entire dataset and extract the boundary.	
	 */

	    String [] nextLine;
    	double longitude, latitude;
    	FileInputStream inputStream = null;
    	Scanner sc = null;
    	try {
    	    inputStream = new FileInputStream(inputPath);
    	    sc = new Scanner(inputStream, "UTF-8");
    	    while (sc.hasNextLine()) {
    	        nextLine = sc.nextLine().split(",");
    	    	longitude=Double.parseDouble(nextLine[longitudeOffset]);
    	    	latitude=Double.parseDouble(nextLine[latitudeOffset]);
    	    	if(counter==0)
    	    	{
    	    		minX=longitude;
    	    		maxX=longitude;
    	    		minY=latitude;
    	    		maxY=latitude;
    	    	}
    	    	if(longitude<minX)
    	    	{
    	    		minX=longitude;
    	    	}
    	    	 if(longitude>maxX)
    	    	{
    	    		maxX=longitude;
    	    	}
    	    	 if(latitude<minY)
    	    	{
    	    		minY=latitude;
    	    	}
    	    	else if(latitude>maxY)
    	    	{
    	    		maxY=latitude;
    	    	}
		counter++;
    	    }
    	    // note that Scanner suppresses exceptions
    	    if (sc.ioException() != null) {
    	        throw sc.ioException();
    	    }
    	} finally {
    	    if (inputStream != null) {
    	        inputStream.close();
    	    }
    	    if (sc != null) {
    	        sc.close();
    	    }
    	}
	    
	    System.out.println("Found Min Max X and Y.");
	    counter=0;
	    /*
	     * Second round, add Hilbert Curve Value
	     */
		FileOutputStream fos = new FileOutputStream(outputPath);
	 
		OutputStreamWriter osw = new OutputStreamWriter(fos);
	 

	    try {
	        inputStream = new FileInputStream(inputPath);
	        sc = new Scanner(inputStream, "UTF-8");
	        while (sc.hasNextLine()) 
	        {
	          String currentLine=sc.nextLine();
	          nextLine = currentLine.split(",");
	          longitude=Double.parseDouble(nextLine[longitudeOffset]);
		      latitude=Double.parseDouble(nextLine[latitudeOffset]);
			  int gridResolution=10000000;
			  int x=locationMapping(minX,longitude,maxY,gridResolution);
			  int y=locationMapping(minY,latitude,maxY,gridResolution);
			  int hValue = computeHValue(gridResolution+1,x,y);
			  if(InplaceGeneration.equals("true"))
			  {
				  String awaitLine = nextLine[0];
				  for(int i=1;i<longitudeOffset;i++)
				  {
					  awaitLine=awaitLine + "," +nextLine[i];
				  }
				  awaitLine = awaitLine + "," +hValue;
				  for(int i=latitudeOffset+1;i<nextLine.length;i++)
				  {
					  awaitLine=awaitLine+","+nextLine[i];
				  }
				  osw.write(awaitLine+'\n');
			  }
			  else
			  {
				  osw.write(currentLine+','+hValue+'\n');
			  }
			  counter++;
			  if(counter%statusInterval==0)
			  {
				  osw.flush();
				  System.out.println("Converted " + counter + " tuples. Still working..." );  
			  }
	        }
			osw.close();
	        // note that Scanner suppresses exceptions
	        if (sc.ioException() != null) {
	            throw sc.ioException();
	        }
	    } finally {
	        if (inputStream != null) {
	            inputStream.close();
	        }
	        if (sc != null) {
	            sc.close();
	        }
	    }
	    System.out.println("Finished the transformation. Converted " + counter + " tuples in total.");
	    return;
	}
	
}
