package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class DefaultTeam {
  public ArrayList<Point> calculDominatingSet(ArrayList<Point> points, int edgeThreshold) {
    ArrayList<Point> result = localSearchingPasNaif(points, edgeThreshold);
    return result;
  }
  
  public ArrayList<Point> glouton(ArrayList<Point> originePoints, int edgeThreshold){
	ArrayList<Point> points = new ArrayList<>(originePoints);
  	ArrayList<Point> result = new ArrayList<>(); 
  	int index = 0;
  	
  	while(!points.isEmpty()){
  		int max = 0;
  		index = 0;
  		for(int i = 0; i < points.size(); i++){
	  		int tmp = neighbor(points.get(i), points, edgeThreshold).size();
	  		if( tmp > max){
	  			max = tmp;
	  			index = i;
	  		}
	  	}
	  	Point point = points.get(index);
	  	result.add(point);
	  	points.removeAll(neighbor(point, points, edgeThreshold));
	  	points.remove(point);
  	}
  	return result;
  }
  
  
  
  public ArrayList<Point> localSearchingPasNaif(ArrayList<Point> points, int edgeThreshold){
	  ArrayList<ArrayList<Point>> map = new ArrayList<>();
	  ArrayList<ArrayList<Point>> results = new ArrayList<>();
	  ArrayList<Point> finalResault = new ArrayList<>();
	  int slices = 25;
	  
	  double maxX = 0, maxY = 0;
	  double minX = points.get(0).getX(), minY = points.get(0).getY();
	  
	  Iterator<Point> iter = points.iterator();
	  while (iter.hasNext()) {
		Point point = (Point) iter.next();
		if(neighbor(point, points, edgeThreshold).size() == 0){
			finalResault.add(point);
			iter.remove();
		}
	  }
	  
	  for(Point p : points){
		  if(p.getX() > maxX){
			  maxX = p.getX();
		  }
		  if(p.getY() > maxY){
			  maxY = p.getY();
		  }
		  if(p.getX() < minX){
			  minX = p.getX();
		  }
		  if(p.getY() < minY){
			  minY = p.getY();
		  }
	  }
	  
	  for(int i = 0; i < slices; i++){
		  map.add(new ArrayList<>());
		  results.add(new ArrayList<>());
	  }
	  
	  double pasX = (maxX - minX) / (Math.sqrt(slices)), pasY = (maxY - minY) / (Math.sqrt(slices));
	  
	  for(int i = 0; i <  Math.sqrt(slices); i++){
		  for(int j = 0; j < Math.sqrt(slices); j++){
			  for(Point p : points){
				  if(p.getX() >= (minX + i * pasX) && p.getX() < (minX + (i + 1) * pasX) && 
						  p.getY() >= (minY + j * pasY) && p.getY() < (minY + (j + 1) * pasY)){
					  map.get(i + j).add((Point)p.clone());
				  }
			  } 
		  }
	  }
	  
	  int somme = 0;
	  for(int i = 0; i < slices; i++){
		  somme += map.get(i).size();
		  System.out.println(map.get(i).size());
		  results.set(i, glouton(map.get(i), edgeThreshold));
	  }
	  System.out.println(somme + finalResault.size());
	  
	  boolean valid;
	  Point a, b, c;
	  /*for(int m = 0; m < map.size(); m++){
		  ArrayList<Point> subSet = map.get(m);
		  ArrayList<Point> copySubSet = new ArrayList<>(subSet);
		  ArrayList<Point> result = results.get(m);
		  do{
			  valid = false;
			  for (int i = 0; i < result.size(); i++) {
				  for(int j = 0; j < result.size(); j++){
					  a = result.get(i);
					  b = result.get(j);
					  if(i != j  && a.distance(b) <= 4*edgeThreshold){
						  for(int k = 0; k < subSet.size(); k++){
							  c = subSet.get(k);
							  if(!c.equals(a) && !c.equals(b)){
								  subSet.remove(c);
								  result.remove(a);
								  result.remove(b);
								  subSet.add(a);
								  subSet.add(b);
								  result.add(c);
								  if(isValide(copySubSet, result, edgeThreshold)){
									  valid = true;
								  }else{
									  valid = false;
									  result.remove(c);
									  subSet.add(c);
									  result.add(a);
									  result.add(b);
									  subSet.remove(a);
									  subSet.remove(b);
								  }
							  }
						  }
					  }
				  }
			  }
		  }while(valid);
		  System.out.println(result.size());
	  }*/
	  
	  for(int i = 0; i < results.size(); i++){
		  for(int j = 0; j < results.get(i).size(); j++){
			  Point p = results.get(i).get(j);
			  if(!finalResault.contains(p)){
				  finalResault.add(p);
			  }
		  }
	  }
	  return finalResault;
  }
  
  public ArrayList<Point> neighbor(Point p, ArrayList<Point> vertices, int edgeThreshold){
    ArrayList<Point> result = new ArrayList<Point>();

    for (Point point:vertices) if (point.distance(p)<edgeThreshold && !point.equals(p)) result.add((Point)point.clone());

    return result;
  }
  
  public boolean isValide(ArrayList<Point> originPoints, ArrayList<Point> result, int edgeThreshold){
	  ArrayList<Point> points = new ArrayList<>(originPoints);
	  for(Point p : result){
		  points.removeAll(neighbor(p, points, edgeThreshold));
		  points.remove(points);
	  }
	  
	  return points.isEmpty();
  }
  
  
  //FILE PRINTER
  private void saveToFile(String filename,ArrayList<Point> result){
    int index=0;
    try {
      while(true){
        BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filename+Integer.toString(index)+".points")));
        try {
          input.close();
        } catch (IOException e) {
          System.err.println("I/O exception: unable to close "+filename+Integer.toString(index)+".points");
        }
        index++;
      }
    } catch (FileNotFoundException e) {
      printToFile(filename+Integer.toString(index)+".points",result);
    }
  }
  private void printToFile(String filename,ArrayList<Point> points){
    try {
      PrintStream output = new PrintStream(new FileOutputStream(filename));
      int x,y;
      for (Point p:points) output.println(Integer.toString((int)p.getX())+" "+Integer.toString((int)p.getY()));
      output.close();
    } catch (FileNotFoundException e) {
      System.err.println("I/O exception: unable to create "+filename);
    }
  }

  //FILE LOADER
  private ArrayList<Point> readFromFile(String filename) {
    String line;
    String[] coordinates;
    ArrayList<Point> points=new ArrayList<Point>();
    try {
      BufferedReader input = new BufferedReader(
          new InputStreamReader(new FileInputStream(filename))
          );
      try {
        while ((line=input.readLine())!=null) {
          coordinates=line.split("\\s+");
          points.add(new Point(Integer.parseInt(coordinates[0]),
                Integer.parseInt(coordinates[1])));
        }
      } catch (IOException e) {
        System.err.println("Exception: interrupted I/O.");
      } finally {
        try {
          input.close();
        } catch (IOException e) {
          System.err.println("I/O exception: unable to close "+filename);
        }
      }
    } catch (FileNotFoundException e) {
      System.err.println("Input file not found.");
    }
    return points;
  }
}