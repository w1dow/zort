package project.backend;
import org.opencv.core.*;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgproc.Imgproc;
import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class testmodel {
    private Graph graph;
    private Net faceDetector;

    public testmodel(String modelPath) {
        try {
            byte[] graphDef = Files.readAllBytes(Paths.get(modelPath));
            graph = new Graph();
            graph.importGraphDef(graphDef);
            System.out.println(" >>> Model loaded!");

            // Load OpenCV DNN face detector (res10 model)
            String proto = "models/deploy.prototxt"; // path to deploy.prototxt
            String caffeModel = "models/res10_300x300_ssd_iter_140000.caffemodel"; // path to caffemodel
            faceDetector = Dnn.readNetFromCaffe(proto, caffeModel);
            System.out.println(" >>> Face detector loaded!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Preprocess
    public Mat pp(Mat original) {
        Rect faceRect = detectFace(original);
        if (faceRect != null) {
            original = new Mat(original, faceRect); 
        }

        Mat resized = new Mat();
        Imgproc.resize(original, resized, new Size(160, 160));
        Imgproc.cvtColor(resized, resized, Imgproc.COLOR_BGR2RGB);

   
        resized.convertTo(resized, CvType.CV_32F);
        Core.subtract(resized, new Scalar(127.5, 127.5, 127.5), resized);
        Core.divide(resized, new Scalar(127.5, 127.5, 127.5), resized);

        return resized;
    }

 
    private Rect detectFace(Mat image) {
        Mat blob = Dnn.blobFromImage(image, 1.0, new Size(300, 300),
                new Scalar(104.0, 177.0, 123.0), false, false);
        faceDetector.setInput(blob);
        Mat detections = faceDetector.forward();

        int cols = image.cols();
        int rows = image.rows();
        detections = detections.reshape(1, (int) detections.total() / 7);

        Rect bestFace = null;
        float bestConf = 0;

        for (int i = 0; i < detections.rows(); i++) {
            float confidence = (float) detections.get(i, 2)[0];
            if (confidence > 0.6 && confidence > bestConf) { // threshold 0.6
                int x1 = (int) (detections.get(i, 3)[0] * cols);
                int y1 = (int) (detections.get(i, 4)[0] * rows);
                int x2 = (int) (detections.get(i, 5)[0] * cols);
                int y2 = (int) (detections.get(i, 6)[0] * rows);
                bestFace = new Rect(new Point(x1, y1), new Point(x2, y2));
                bestConf = confidence;
            }
        }
        return bestFace;
    }

    // Convert Mat to float array without re-normalizing
    public float[][][][] getfloat(Mat mat) {
        int height = mat.rows();
        int width = mat.cols();
        int channels = mat.channels();

        float[][][][] input = new float[1][height][width][channels];
        float[] data = new float[(int) (mat.total() * channels)];
        mat.get(0, 0, data);

        int idx = 0;
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                for (int c = 0; c < channels; c++) {
                    input[0][h][w][c] = data[idx++];
                }
            }
        }
        return input;
    }

    // Run FaceNet to get embedding
    public float[] getEmbedding(float[][][][] faceInput) {
        try (Session sess = new Session(graph)) {
            Tensor<Float> inputTensor = Tensor.create(faceInput, Float.class);
            Tensor<Boolean> phaseTrain = Tensor.create(false, Boolean.class);

            List<Tensor<?>> outputs = sess.runner()
                    .feed("input:0", inputTensor)
                    .feed("phase_train:0", phaseTrain)
                    .fetch("embeddings:0")
                    .run();

            Tensor<?> embeddingTensor = outputs.get(0);
            long[] shape = embeddingTensor.shape();
            // System.out.println("Output shape: " + java.util.Arrays.toString(shape));

            float[][] embeddings = new float[1][(int) shape[1]];
            embeddingTensor.copyTo(embeddings);

            return embeddings[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
