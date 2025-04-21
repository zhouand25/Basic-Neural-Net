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
        
        System.out.println("\n\nAt first");
        for(int i=0; i<value.length; ++i) {
            System.out.print(value[i]+" ");
        }
        System.out.println("\n AFTER WEIGHT MULTIPLICATION");
        for(int i=0; i<next.size; ++i) {
            System.out.print(next.val[i]+" ");
        }
        
        //add the biasees
        for(int i=0; i<next.size; ++i) {
            next.val[i] += next.bias[i];
        }
        
        System.out.println("\n AFTER BIAS");
        for(int i=0; i<next.size; ++i) {
            System.out.print(next.val[i]+" ");
        }
        
        //feed into activation function
        for(int i=0; i<next.size; ++i) {
            next.val[i] = 1/(1+Math.exp(-next.val[i]));
        }
        
         System.out.println("\n AFTER Activate");
        for(int i=0; i<next.size; ++i) {
            System.out.print(next.val[i]+" ");
        }
        
    }
    
}
class Main {
    public static void main(String[] args) {
        int[] str = {3, 4, 4, 3};
        Network c1 = new Network(str);
        
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
        
        double[] test = {0.2, 0.3, 0.4};
        c1.fpropogate(test);
        
    }
}
