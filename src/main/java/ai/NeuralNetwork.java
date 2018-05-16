package ai;

import org.ejml.simple.SimpleMatrix;

public class NeuralNetwork {
	
	private SimpleMatrix[] weights;
	private SimpleMatrix[] biases;
	
	public NeuralNetwork(double[][][] weights, double[][] biases) {
		this.weights = new SimpleMatrix[weights.length];
		this.biases = new SimpleMatrix[biases.length];
		for (int layer = 0; layer < weights.length; layer++) {
			this.weights[layer] = new SimpleMatrix(weights[layer]);
		}
		
		for (int layer = 0; layer < biases.length; layer++) {
			double[][] biasMatrix = new double[biases[layer].length][1];
			for (int i = 0; i < biasMatrix.length; i++) {
				biasMatrix[i][0] = biases[layer][i];
			}
			this.biases[layer] = new SimpleMatrix(biasMatrix);
		}
		
		assert this.biases.length == weights.length;
	}
	
	public double[] feedForward(double[] input) {
		double[][] inputVector = new double[input.length][1];
		for (int row = 0; row < input.length; row++) {
			inputVector[row][0] = input[row];
		}
		SimpleMatrix res = new SimpleMatrix(inputVector);
		for (int layer = 0; layer < biases.length; layer++) {
			res = sigmoid(weights[layer].mult(res).plus(biases[layer]));
		}
		assert res.numCols() == 1;
		double[] resArray = new double[res.numRows()];
		for (int row = 0; row < res.numRows(); row++) {
			resArray[row] = res.get(row, 0);
		}
		return resArray;
	}
	
	public static double sigmoid(double val) {
		if (val > 10) {
			return 1;
		} else if (val < -10) {
			return 0;
		} else {
			return 1/(1 + Math.exp(-val));
		}
	}
	
	private static SimpleMatrix sigmoid(SimpleMatrix m) {
		for (int row = 0; row < m.numRows(); row++) {
			for (int col = 0; col < m.numCols(); col++) {
				m.set(row, col, sigmoid(m.get(row, col)));
			}
		}
		return m;
	}

}
