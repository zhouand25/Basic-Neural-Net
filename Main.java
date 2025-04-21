// Online Java Compiler
// Use this editor to write, compile and run your Java code online
import java.util.ArrayList;
class Network {
    int input[];
    ArrayList<Layer> wall = new ArrayList<Layer>();
    
    
    public class Layer {
        //Weights connected backwards
        int size;
        int prev_size;
        int weight[][];
        int bias[];
        int val[];
        
        Layer(int curr, int prev) {
            size = curr;
            prev_size = prev;
            //num of prev must mach the columns of matrix due to dot product
            weight = new int[curr][prev];
            bias = new int[curr];
            val = new int[curr];
        }
    }
    
    Network(int dim[]) {
        //declares size of input layer
        input = new int[dim[0]];
        
        //starts from 2nd layer
        for(int i=1; i<dim.length; ++i) {
            //Creates a new layer based on the dimensions
            Layer temp = new Layer(dim[i], dim[i-1]);
            wall.add(temp);
        }
    }
    
    void fpropogate() {
        //Basically says take the value in the input layer and progress from layer 0 to layer 1
        progress(input, 0);
        
        for(int i=1; i<input.length; ++i) {
            progress(wall.get(i).val, i);
        }
    }
    
    void progress(int value[], curr) {
        Layer next = wall.get(curr+1);
        //matrix multiplication
        for(int i=0; i<next.size; ++i) {
            //dot product time
            //Each loop is for one element of the array
            int sum=0;
            for(int j=0; j<value.length; ++j) {
                //retrieve next layer's weight stuff, row is the same 
                sum+=value[j]*next.weight[i][j];
            }
            next.val[i] = sum;
        }
        
        //add the biasees
        for(int i=0; i<next.size; ++i) {
            val[i] += bias[i];
        }
        
        //feed into activation function
        
    }
    
}
class Main {
    public static void main(String[] args) {
        int[] str = {3, 4, 4, 3};
        Network c1 = new Network(str);
    }
}
