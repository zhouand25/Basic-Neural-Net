// Online Java Compiler
// Use this editor to write, compile and run your Java code online
import java.util.ArrayList;
class Network {
    double input[];
    ArrayList<Layer> wall = new ArrayList<Layer>();
    
    
    public class Layer {
        //Weights connected backwards
        int size;
        int prev_size;
        double weight[][];
        double bias[];
        double val[];
        
        Layer(int curr, int prev) {
            size = curr;
            prev_size = prev;
            //num of prev must mach the columns of matrix due to dot product
            weight = new double[curr][prev];
            bias = new double[curr];
            val = new double[curr];
            
            //upon creation, randomize both weights and biases
            //WEIGHTS
            for(int i=0; i<size; ++i) {
                for(int j=0; j<prev_size; ++j) {
                    weight[i][j] = Math.random();
                }
            }
            //BIASES
            for(int i=0; i<size; ++i) {
                bias[i] = Math.random();
            } 
            
        }
    }
    
    Network(int dim[]) {
        //declares size of input layer
        input = new double[dim[0]];
        
        //starts from 2nd layer
        for(int i=1; i<dim.length; ++i) {
            //Creates a new layer based on the dimensions
            Layer temp = new Layer(dim[i], dim[i-1]);
            wall.add(temp);
        }
    }
    
    //-----------------------------------------------------
    
    void fpropogate(double[] startVal) {
        //Basically says take the value in the input layer and progress from layer 0 to layer 1, THEREFORE INTERACT WITH LAYER INDEX 0
        input = startVal;
        progress(input, 0);
        
        for(int i=1; i<input.length; ++i) {
			//Reason for i-1, if you want to progress to the second layer, you need info from the first layer
            progress(wall.get(i-1).val, i);
        }
    }
    
    void progress(double value[], int curr) {
        Layer next = wall.get(curr);
        //matrix multiplication
        for(int i=0; i<next.size; ++i) {
            //dot product time
            //Each loop is for one element of the array
            double sum=0;
            for(int j=0; j<value.length; ++j) {
                //retrieve next layer's weight stuff, row is the same 
                sum+=value[j]*next.weight[i][j];
            }
            next.val[i] = sum;
        }
        
        //add the biasees
        for(int i=0; i<next.size; ++i) {
            next.val[i] += next.bias[i];
        }
        
        
        //feed into activation function
        for(int i=0; i<next.size; ++i) {
            next.val[i] = 1/(1+Math.exp(-next.val[i]));
        }
        
    }
    
    double error(int index) {
		System.out.println("\nFINAL DISTRIBUTION: ");
        Layer last = wall.get(wall.size()-1);
        for(int i=0; i<last.val.length; ++i) {
			System.out.print(last.val[i]+" ");
		}
		double sum=0;
		for(int i=0; i<last.val.length; ++i) {
			if(i == index) {
				sum+=Math.pow((1 - last.val[i]), 2);
				continue;
			}
			sum+=Math.pow(last.val[i], 2);
		}
		sum = sum/last.val.length;
		return sum;
	}
    
    //------------------------------------------------
    
}
class Main {
	static int[] str = {3, 4, 4, 3};
    static Network c1 = new Network(str);
    static Network storage = new Network(str);
    
    public static void main(String[] args) {    
        double[] test = {0.2, 0.3, 0.4};
		//by index
        int id = 2;
        calibrate(test, id);
    }
    
    public static void calibrate(double[] test, int ans) {
		//Print demon DEBUG TIME
        //PRINT ALL WEIGHTS AND BIASES FIRST
        
        for(int i=0; i<c1.wall.size(); ++i) {
         System.out.println("H Layer");
         for(int j=0; j<c1.wall.get(i).weight.length; ++j) {
             for(int k=0; k<c1.wall.get(i).weight[0].length; ++k) {
                 System.out.print(c1.wall.get(i).weight[j][k]+" ");
             }
             System.out.println("");
         }
         System.out.println("\n Bias");
          //biases
          for(int m=0; m<c1.wall.get(i).size; ++m) {
              System.out.print(c1.wall.get(i).bias[m]+" ");
          }
          System.out.println("\n\n\n");
        }
        
        //THIS IS THE MODEL
        c1.fpropogate(test);
        double error = c1.error(ans);
		
		//NOW TIME FOR ACTUAL STUFF, CHANGING EACH ONE
		//Note we will modify original but then restore it
		
	
		for(int i=0; i<c1.wall.size(); ++i) {
			//We first modfiy every single weight value
			for(int j=0; j<c1.wall.get(i).size; ++j) {
				for(int k=0; k<c1.wall.get(i).prev_size; ++k) {
				   //perturb the weight in question by 0.1
				   c1.wall.get(i).weight[j][k] += 0.05;	
				   c1.propogate(test);
				   double dif = c1.error(ans);
				   //We compute the finite difference and store it in the gradient object
				   storage.wall.get(i).weight[j][k] = (dif - error)/0.05;
				   //We restore the c1 object to its original form
				   c1.wall.get(i).weight[j][k] -= 0.05;	
				}
			}
			
			//We now modify biases
			for(int m=0; m<c1.wall.get(i).size; ++m) {
				c1.wall.get(i).bias[m] += 0.05;
				c1.propogate(test);
				double dif = c1.error(ans);
				storage.wall.get(i).bias[m] = (dif - error)/0.05;
				c1.wall.get(i).bias[m] -= 0.05;
			}
		}
		
	}
}
