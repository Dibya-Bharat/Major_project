package com.example.home_window;

import org.tensorflow.lite.Interpreter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TensorFlowModelHelper {
    private static final int INPUT_SIZE = 3; // state, category, rating
    private static final int OUTPUT_CLASSES = 10; // Adjust based on your number of classes

    private static final Map<String, Integer> stateEncoding = new HashMap<>();
    private static final Map<String, Integer> categoryEncoding = new HashMap<>();

    static {
        // Example encodings, should match the encoding used in Python
        stateEncoding.put("Delhi", 0);
        stateEncoding.put("Uttarakhand", 1);
        // Add all other states here

        categoryEncoding.put("Hill Station", 0);
        categoryEncoding.put("Religious", 1);
        categoryEncoding.put("Beach", 2);
        // Add all other categories here
    }

    public static void main(String[] args) {
        try {
            // Load TFLite model
            float[][] output1;
            float[][] output2;
            float[][] output3;
            try (Interpreter tflite = new Interpreter(loadModelFile("my_model.tflite"))) {

                // Example user input
                String state = "Uttarakhand";
                String category = "Hill Station";
                double rating = 4.0;

                // Preprocess input
                float[] input = preprocessInput(state, category, rating);

                // Prepare input buffer
                ByteBuffer inputBuffer = ByteBuffer.allocateDirect(INPUT_SIZE * 4); // float size = 4 bytes
                inputBuffer.order(ByteOrder.nativeOrder());
                for (float val : input) {
                    inputBuffer.putFloat(val);
                }

                // Prepare output buffers
                output1 = new float[1][OUTPUT_CLASSES];
                output2 = new float[1][OUTPUT_CLASSES];
                output3 = new float[1][OUTPUT_CLASSES];

                // Run inference
                Object[] inputs = {inputBuffer};
                Map<Integer, Object> outputs = new HashMap<>();
                outputs.put(0, output1);
                outputs.put(1, output2);
                outputs.put(2, output3);
                tflite.runForMultipleInputsOutputs(inputs, outputs);
            }

            // Decode and print recommendations
            String[] recommendations = decodePredictions(output1[0], output2[0], output3[0]);
            System.out.println("Recommended places: " + Arrays.toString(recommendations));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load model file
    private static MappedByteBuffer loadModelFile(String modelPath) throws IOException {
        File file = new File(modelPath);
        FileInputStream inputStream = new FileInputStream(file);
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = 0;
        long declaredLength = file.length();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    // Preprocess user input
    private static float[] preprocessInput(String state, String category, double rating) {
        int stateEncoded = encodeState(state);
        int categoryEncoded = encodeCategory(category);
        float ratingNormalized = (float) (rating / 5.0); // Assuming rating is between 0 and 5

        return new float[]{stateEncoded, categoryEncoded, ratingNormalized};
    }

    // Encode state using the predefined mapping
    private static int encodeState(String state) {
        return stateEncoding.getOrDefault(state, -1); // Default to -1 if state not found
    }

    // Encode category using the predefined mapping
    private static int encodeCategory(String category) {
        return categoryEncoding.getOrDefault(category, -1); // Default to -1 if category not found
    }

    // Decode predictions to get the actual place names
    private static String[] decodePredictions(float[] output1, float[] output2, float[] output3) {
        String place1 = decodePlace(output1);
        String place2 = decodePlace(output2);
        String place3 = decodePlace(output3);

        return new String[]{place1, place2, place3};
    }

    // Dummy decoding method, replace with actual decoding logic
    private static String decodePlace(float[] output) {
        int index = argMax(output);
        // Implement your decoding logic here
        return "Place " + index; // Dummy value
    }

    // Helper method to find the index of the maximum value in an array
    private static int argMax(float[] array) {
        int maxIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }
}

/*import org.tensorflow.lite.Interpreter;

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
}*/
