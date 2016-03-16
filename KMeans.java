///////////////////////////////////////////////////////////////////////////////
//
//Title:            KMeans.java
//Files:            KMeans.java, KMeansResult.java, HW3.java
//Semester:         Spring 2016
//
//Author:           Han Jiang - hjiang73@wisc.edu
//CS Login:         hjiang  
//Lecturer's Name:  Collin Engstrom  
//
///////////////////////////////////////////////////////////////////////////////
import java.util.ArrayList;
import java.util.Collections;

/**
 * A k-means clustering algorithm implementation.
 * 
 */

public class KMeans {
	public KMeansResult cluster(double[][] centroids, double[][] instances, double threshold) {
		KMeansResult result = new KMeansResult();
		//An array of the index of the centroid assigned to each row of the instances method argument
		int clusterAssigment[]=new int[instances.length];
		//An ArrayList to store the distortion of each iteration
		ArrayList<Double> distortions = new ArrayList<Double>();
		//The counter of iteration
		int iteration=0;
		//The relative change in distortion between successive iterations, initialize to 0
		double change =0;
		/*if at iteration i the relative change is less than the threshold, 
		 then the algorithm should terminate and return its results.*/
		while(distortions.size()<=1||change>=threshold){

			//Assign instances to its centroid by distance 
			for(int i = 0;i<instances.length;i++){
				ArrayList<Double> distance = new ArrayList<Double> ();
				for(int j =0;j<centroids.length;j++){
					double dis =0;
					for(int l = 0;l<instances[i].length;l++){
						dis = dis+ Math.pow(centroids[j][l]-instances[i][l],2);
					}
					dis = Math.sqrt(dis);
					distance.add(dis);
				}
				//find closest clusters
				clusterAssigment[i]= distance.indexOf(Collections.min(distance));
			}

			//check if there is orphaned centroids and remove it
			//An array to store the number of instances for each centroid
			//if equals to 0, then it is a orphaned centroid
			int[] counter = new int[centroids.length];
			for(int i = 0;i<clusterAssigment.length;i++){
				counter[clusterAssigment[i]] ++;
			}
			for(int j = 0;j<counter.length;j++){
				if(counter[j]==0){
					//Search among all the instances for the instance x whose distance is farthest from its assigned centroid.
					ArrayList<Double> instocen = new ArrayList<Double>();
					for(int i =0; i<instances.length;i++){
						double dis =0;
						for(int l = 0;l<instances[i].length;l++){
							dis = dis+ Math.pow(centroids[clusterAssigment[i]][l]-instances[i][l],2);
						}
						dis = Math.sqrt(dis);
						instocen.add(dis);
					}
					int maxind= instocen.indexOf(Collections.max(instocen));
					//Choose x’s position as the position of c, the orphaned centroid.
					centroids[j]=instances[maxind];
					//Reallocate again the cluster assignments for all x.
					for(int i = 0;i<instances.length;i++){
						ArrayList<Double> distance = new ArrayList<Double> ();
						for(int n =0;n<centroids.length;n++){
							double dis =0.00;
							for(int l = 0;l<instances[i].length;l++){
								dis = dis+ Math.pow(centroids[n][l]-instances[i][l],2);
							}
							dis = Math.sqrt(dis);
							distance.add(dis);

						}
						clusterAssigment[i]= distance.indexOf(Collections.min(distance));

					}
					//Recalculate counter
					for(int i = 0;i<clusterAssigment.length;i++){
						counter[clusterAssigment[i]] ++;
					}
					j=0;
				}

			}

			//Update all cluster centers as the centroids
			//ci = ∑ x / num_points_in(cluster i)

			for(int i = 0;i<centroids.length;i++){
				int count =0;
				double[] sum= new double[centroids[i].length];
				for(int j =0;j<clusterAssigment.length;j++){
					if(clusterAssigment[j]==i){
						count++;
						for(int k=0;k<instances[0].length;k++){
							sum[k] = sum[k] + instances[j][k];
						}
					}  
				}
				for(int m =0;m<sum.length;m++){
					sum[m] = sum[m]/count;
					centroids[i][m]=sum[m];
				}	
			}

			//Calculate distortion which is used to measure the goodness of the cluster.
			double distortion =0;
			for(int i =0; i <instances.length;i++){

				double onedistance = 0;
				for(int j=0;j < instances[i].length;j++){

					onedistance = onedistance+
							Math.pow((instances[i][j]-centroids[clusterAssigment[i]][j]), 2);
				}
				distortion = distortion+onedistance;

			}
			//Add each distortion to the ArrayList
			distortions.add(distortion);
			//Calculate the relative change when there are more than two iterations. 
			if(distortions.size()>1){
				change = 
						Math.abs((distortions.get(iteration)-distortions.get(iteration-1))/distortions.get(iteration-1));

			}
			//Increment iteration
			iteration++;
		}

		//Convert distortion ArrayList to an array
		double[] distortionIterations = new double[distortions.size()];
		for(int i =0;i<distortionIterations.length;i++){
			distortionIterations[i]=distortions.get(i);
		}

		//return a KMeansResult
		result.centroids=centroids;
		result.clusterAssignment=clusterAssigment;
		result.distortionIterations=distortionIterations;
		return result;
	}

}

