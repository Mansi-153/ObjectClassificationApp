package com.example.objectclassificationapp;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.tensorflow.lite.Delegate;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.Interpreter.Options;
import org.tensorflow.lite.gpu.GpuDelegate;

@Metadata(
        mv = {1, 1, 15},
        bv = {1, 0, 3},
        k = 1,
        d1 = {"\u0000t\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0014\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 /2\u00020\u0001:\u0001/B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u001b\u001a\u00020\u00142\u0006\u0010\u001c\u001a\u00020\u001dH\u0002J\u0014\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00140\u001f2\u0006\u0010\u001c\u001a\u00020\u001dJ\u0006\u0010 \u001a\u00020!J\u0010\u0010\"\u001a\u00020#2\u0006\u0010\u001c\u001a\u00020\u001dH\u0002J\u0010\u0010$\u001a\u00020\n2\u0006\u0010%\u001a\u00020&H\u0002J\f\u0010'\u001a\b\u0012\u0004\u0012\u00020(0\u001fJ\b\u0010)\u001a\u00020!H\u0002J&\u0010*\u001a\u0012\u0012\u0004\u0012\u00020\u00140\u0013j\b\u0012\u0004\u0012\u00020\u0014`\u00152\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010+\u001a\u00020\u0014J\u0018\u0010,\u001a\u00020#2\u0006\u0010-\u001a\u00020.2\u0006\u0010+\u001a\u00020\u0014H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u000e\u001a\u00020\u000f@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R*\u0010\u0012\u001a\u0012\u0012\u0004\u0012\u00020\u00140\u0013j\b\u0012\u0004\u0012\u00020\u0014`\u0015X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u000e\u0010\u001a\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000¨\u00060"},
        d2 = {"Lcom/anupam/androidcameraxtflite/TFLiteClassifier;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "executorService", "Ljava/util/concurrent/ExecutorService;", "gpuDelegate", "Lorg/tensorflow/lite/gpu/GpuDelegate;", "inputImageHeight", "", "inputImageWidth", "interpreter", "Lorg/tensorflow/lite/Interpreter;", "<set-?>", "", "isInitialized", "()Z", "labels", "Ljava/util/ArrayList;", "", "Lkotlin/collections/ArrayList;", "getLabels", "()Ljava/util/ArrayList;", "setLabels", "(Ljava/util/ArrayList;)V", "modelInputSize", "classify", "bitmap", "Landroid/graphics/Bitmap;", "classifyAsync", "Lcom/google/android/gms/tasks/Task;", "close", "", "convertBitmapToByteBuffer", "Ljava/nio/ByteBuffer;", "getMaxResult", "result", "", "initialize", "Ljava/lang/Void;", "initializeInterpreter", "loadLines", "filename", "loadModelFile", "assetManager", "Landroid/content/res/AssetManager;", "Companion", "app"}
)
public final class TFLiteClassifier {
    private boolean isInitialized;
    ByteBuffer byteBuffer;
    private GpuDelegate gpuDelegate;
    private static final String MODEL_PATH = "mobilenet_v1_1.0_224.tflite";
    private static final String LABEL_PATH = "labels.txt";
    @NonNull
    Interpreter tflite;
    private List<String> labelList;
    private final ExecutorService executorService;
    private int inputImageWidth;
    private int inputImageHeight;
    private int modelInputSize;
    private final Context context;
    private static final String TAG = "TfliteClassifier";
    private static final int FLOAT_TYPE_SIZE = 4;
    private static final int CHANNEL_SIZE = 3;
    private static final float IMAGE_MEAN = 127.5F;
    private static final float IMAGE_STD = 127.5F;

    public final boolean isInitialized() {
        return build().isInitialized;
    }

    @NonNull
    public final List<String> getLabels() {
        return this.labelList;
    }

    public final void setLabels(@NonNull ArrayList var1) {
        Intrinsics.checkParameterIsNotNull(var1, "<set-?>");
        this.labelList = var1;
    }

    @NonNull
    public final Task initialize() {
        Task var10000 = Tasks.call((Executor) this.executorService, new Callable() {
            @Override
            public Object call() throws Exception {
                return call1();
            }
            @Nullable
            public final Object call1() {
                try {
                    TFLiteClassifier.this.initializeInterpreter();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        Intrinsics.checkExpressionValueIsNotNull(var10000, "call(\n            execut…l\n            }\n        )");
        return var10000;
    }

    private final void initializeInterpreter() throws IOException {
        AssetManager assetManager = this.context.getAssets();
        Intrinsics.checkExpressionValueIsNotNull(assetManager, "assetManager");
        Options options = new Options();
        this.gpuDelegate = new GpuDelegate();
        options.addDelegate((Delegate)this.gpuDelegate);
        tflite = new Interpreter(loadModelFile(context));
        labelList = loadLabelList(context);
        int[] inputShape = tflite.getInputTensor(0).shape();
        this.inputImageWidth = inputShape[1];
        this.inputImageHeight = inputShape[2];
        this.modelInputSize = 4 * this.inputImageWidth * this.inputImageHeight * 3;
        this.tflite = tflite;
        this.isInitialized = true;
    }

    private MappedByteBuffer loadModelFile(Context activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(MODEL_PATH);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
    @NonNull
    private List<String> loadLabelList(Context activity) throws IOException {
        List<String> labelList = new ArrayList<String>();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(activity.getAssets().open(LABEL_PATH)));
        String line;
        while ((line = reader.readLine()) != null) {
            labelList.add(line);
        }
        reader.close();
        return labelList;
    }

    private final int getMaxResult(float[] result) {
        float probability = result[0];
        int index = 0;
        int i = 0;

        for(int var5 = result.length; i < var5; ++i) {
            if (probability < result[i]) {
                probability = result[i];
                index = i;
            }
        }

        return index;
    }

    private String classify(Bitmap bitmap) {
        boolean var2 = this.isInitialized;
        boolean var3 = false;
        boolean var4 = false;
        String result = "prediction";
        if (!var2) {
            boolean var21 = false;
            String var19 = "TF Lite Interpreter is not initialized yet.";
            try {
                throw (Throwable)(new IllegalStateException(var19.toString()));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else {
            Bitmap resizedImage = Bitmap.createScaledBitmap(bitmap, this.inputImageWidth, this.inputImageHeight, true);
            Intrinsics.checkExpressionValueIsNotNull(resizedImage, "resizedImage");
            byteBuffer = convertBitmapToByteBuffer(resizedImage);
            byte var5 = 1;
            float[][] var6 = new float[var5][];

            for(int var7 = 0; var7 < var5; ++var7) {
                float[] var15 = new float[labelList.size()];
                var6[var7] = var15;
            }
            float [][] output = new float[1][labelList.size()];
            long startTime = SystemClock.uptimeMillis();
            Interpreter var10000 = tflite;
            if (var10000 != null) {
                var10000.run(byteBuffer, output);
            }
            long endTime = SystemClock.uptimeMillis();
            long inferenceTime = endTime - startTime;
            int index = this.getMaxResult(output[0]);
            result = "Prediction is " + (String)this.labelList.get(index) + "\nInference Time " + inferenceTime + " ms";

        }return result;
    }

    @NonNull
    public final Task classifyAsync(@NonNull final Bitmap bitmap) {
        Intrinsics.checkParameterIsNotNull(bitmap, "bitmap");
        Task var10000 = Tasks.call((Executor)this.executorService, new Callable() {
            // $FF: synthetic method
            // $FF: bridge method
            @Override
            public Object call() {
                return this.call1();
            }

            @NonNull
            public final Object call1() {
                return TFLiteClassifier.this.classify(bitmap);
            }
        });
        Intrinsics.checkExpressionValueIsNotNull(var10000, "call(executorService, Ca…ng> { classify(bitmap) })");
        return var10000;
    }

    public final void close() {
        Tasks.call((Executor) this.executorService, new Callable() {
            @Override
            public Object call() {
                return call1();
            }
            @Nullable
            public final Object call1() {
                Interpreter var10000 = TFLiteClassifier.this.tflite;
                if (var10000 != null) {
                    var10000.close();
                }

                if (TFLiteClassifier.this.gpuDelegate != null) {
                    GpuDelegate var1 = TFLiteClassifier.this.gpuDelegate;
                    if (var1 == null) {
                        Intrinsics.throwNpe();
                    }

                    var1.close();
                    TFLiteClassifier.this.gpuDelegate = (GpuDelegate)null;
                }

                Log.d("TfliteClassifier", "Closed TFLite interpreter.");
                return null;
            }

        });
    }

    private final ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        byteBuffer = ByteBuffer.allocateDirect(this.modelInputSize);
        byteBuffer.order(ByteOrder.nativeOrder());
        int[] pixels = new int[this.inputImageWidth * this.inputImageHeight];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int pixel = 0;
        int var5 = 0;

        for(int var6 = this.inputImageWidth; var5 < var6; ++var5) {
            int var7 = 0;

            for(int var8 = this.inputImageHeight; var7 < var8; ++var7) {
                int pixelVal = pixels[pixel++];
                byteBuffer.putFloat(((float)(pixelVal >> 16 & 255) - 127.5F) / 127.5F);
                byteBuffer.putFloat(((float)(pixelVal >> 8 & 255) - 127.5F) / 127.5F);
                byteBuffer.putFloat(((float)(pixelVal & 255) - 127.5F) / 127.5F);
            }
        }
        bitmap.recycle();
        Intrinsics.checkExpressionValueIsNotNull(byteBuffer, "byteBuffer");
        return byteBuffer;
    }

    public TFLiteClassifier(@NonNull Context context) {
        super();
        Intrinsics.checkParameterIsNotNull(context, "context");
        this.context = context;
        this.labelList = new ArrayList();
        ExecutorService var10001 = Executors.newCachedThreadPool();
        Intrinsics.checkExpressionValueIsNotNull(var10001, "Executors.newCachedThreadPool()");
        this.executorService = var10001;
    }

    // $FF: synthetic method
    public static final void access$setInterpreter$p(TFLiteClassifier $this, Interpreter var1) {
        $this.tflite = var1;
    }

    @Metadata(
            mv = {1, 1, 15},
            bv = {1, 0, 3},
            k = 1,
            d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082T¢\u0006\u0002\n\u0000¨\u0006\u000b"},
            d2 = {"Lcom/anupam/androidcameraxtflite/TFLiteClassifier$Companion;", "", "()V", "CHANNEL_SIZE", "", "FLOAT_TYPE_SIZE", "IMAGE_MEAN", "", "IMAGE_STD", "TAG", "", "app"}
    )
    public static final class Companion {
        private Companion() {
        }
        // $FF: synthetic method
    }
    public TFLiteClassifier build(){
        return new TFLiteClassifier(context);
    }

}

