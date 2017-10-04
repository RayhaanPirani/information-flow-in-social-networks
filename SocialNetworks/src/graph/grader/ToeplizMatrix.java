package graph.grader;

public class ToeplizMatrix {
    public static int toeplizMatrix(int[][] input1) {
	//Write code here
	for(int j = input1[0].length - 1; j >= 0; j--) {
	    int i = 0;
	    int prev = input1[i][j];
            i++;
            j++;
	    while(i < input1.length - 1 && j < input1[0].length - 1) {
	        int m = input1[i][j];
	        if(prev != m) return 0;
                System.out.println(i + "," + j);
                i++; j++;
	    }
	}
	return 1;
    }
    
    public static void main(String[] args) {
        int[][] ip1 = new int[][] {{6, 7, 8, 9, 2},
                                   {4, 6, 7, 8, 9},
                                   {1, 4, 6, 7, 8},
                                   {0, 1, 4, 6, 7}};
        int output = toeplizMatrix(ip1);
        System.out.println(String.valueOf(output));
    }
}
