package com.example.home_window;

import org.tensorflow.lite.Interpreter;

public class TensorFlowModelHelper {

    private final Interpreter interpreter;

    public TensorFlowModelHelper(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    public float[] runInference(float[] inputData) {
        // Define the output array to store the results
        float[][] outputData = new float[1][3]; // Replace OUTPUT_SIZE with the size of your output tensor

        // Run inference
        interpreter.run(inputData, outputData);

        // Convert the 2D output array to 1D
        float[] output = new float[outputData[0].length];
        System.arraycopy(outputData[0], 0, output, 0, outputData[0].length);

        return output;
    }
}
