package com.example.objectclassificationapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.Display;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnLayoutChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.camera.core.UseCase;
import androidx.camera.core.CameraX.LensFacing;
import androidx.camera.core.ImageAnalysis.Analyzer;
import androidx.camera.core.ImageAnalysis.ImageReaderMode;
import androidx.camera.core.ImageProxy.PlaneProxy;
import androidx.camera.core.Preview.OnPreviewOutputUpdateListener;
import androidx.camera.core.Preview.PreviewOutput;
import androidx.camera.core.PreviewConfig.Builder;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(
        mv = {1, 1, 15},
        bv = {1, 0, 3},
        k = 1,
        d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0015\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u000e\u001a\u00020\u000fH\u0002J\u0012\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0014J\b\u0010\u0014\u001a\u00020\u0011H\u0014J+\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u00042\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\u0018\u001a\u00020\u0019H\u0016¢\u0006\u0002\u0010\u001aJ\b\u0010\u001b\u001a\u00020\u0011H\u0002J\b\u0010\u001c\u001a\u00020\u0011H\u0002J\n\u0010\u001d\u001a\u00020\u001e*\u00020\u001fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D¢\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\bR\u000e\u0010\t\u001a\u00020\u0007X\u0082D¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006 "},
        d2 = {"Lcom/anupam/androidcameraxtflite/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "REQUEST_CODE_PERMISSIONS", "", "REQUIRED_PERMISSIONS", "", "", "[Ljava/lang/String;", "TAG", "lensFacing", "Landroidx/camera/core/CameraX$LensFacing;", "tfLiteClassifier", "Lcom/anupam/androidcameraxtflite/TFLiteClassifier;", "allPermissionsGranted", "", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onRequestPermissionsResult", "requestCode", "permissions", "grantResults", "", "(I[Ljava/lang/String;[I)V", "startCamera", "updateTransform", "toBitmap", "Landroid/graphics/Bitmap;", "Landroidx/camera/core/ImageProxy;", "app"}
)
public final class MainActivity extends AppCompatActivity {
    private LensFacing lensFacing;
    private final String TAG;
    private final int REQUEST_CODE_PERMISSIONS;
    private final String[] REQUIRED_PERMISSIONS;
    private TFLiteClassifier tfLiteClassifier;
    private HashMap _$_findViewCache;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        if (this.allPermissionsGranted()) {
            ((TextureView)findViewById(R.id.textureView)).post((Runnable)(new Runnable() {
                public final void run() {
                    MainActivity.this.startCamera();
                }
            }));
            ((TextureView)findViewById(R.id.textureView)).addOnLayoutChangeListener((OnLayoutChangeListener)(new OnLayoutChangeListener() {
                public final void onLayoutChange(View $noName_0, int $noName_1, int $noName_2, int $noName_3, int $noName_4, int $noName_5, int $noName_6, int $noName_7, int $noName_8) {
                    MainActivity.this.updateTransform();
                }
            }));
        } else {
            ActivityCompat.requestPermissions((Activity)this, this.REQUIRED_PERMISSIONS, this.REQUEST_CODE_PERMISSIONS);
        }

        this.tfLiteClassifier.initialize().addOnSuccessListener((new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("DDDDDDDDDDDDDDDDD", "Success in setting up the classifier");
            }
        }));
        this.tfLiteClassifier.initialize().addOnFailureListener((OnFailureListener)(new OnFailureListener() {
            public final void onFailure(@NotNull Exception e) {
                Intrinsics.checkParameterIsNotNull(e, "e");
                Log.e(MainActivity.this.TAG, "Error in setting up the classifier.", (Throwable)e);
            }
        }));
    }

    private final void startCamera() {
        DisplayMetrics var2 = new DisplayMetrics();
        boolean var3 = false;
        boolean var4 = false;
        boolean var6 = false;
        TextureView var10000 = (TextureView)findViewById(R.id.textureView);
        Intrinsics.checkExpressionValueIsNotNull(var10000, "textureView");
        var10000.getDisplay().getRealMetrics(var2);
        Size screenSize = new Size(var2.widthPixels, var2.heightPixels);
        Rational screenAspectRatio = new Rational(1, 1);
        Builder var5 = new Builder();
        var6 = false;
        boolean var7 = false;
        boolean var9 = false;
        var5.setLensFacing(this.lensFacing);
        var5.setTargetResolution(screenSize);
        var5.setTargetAspectRatio(screenAspectRatio);
        WindowManager var10001 = this.getWindowManager();
        Intrinsics.checkExpressionValueIsNotNull(var10001, "windowManager");
        Display var25 = var10001.getDefaultDisplay();
        Intrinsics.checkExpressionValueIsNotNull(var25, "windowManager.defaultDisplay");
        var5.setTargetRotation(var25.getRotation());
        final TextureView var26 = (TextureView)findViewById(R.id.textureView);
        Intrinsics.checkExpressionValueIsNotNull(var26, "textureView");
        var25 = var26.getDisplay();
        Intrinsics.checkExpressionValueIsNotNull(var25, "textureView.display");
        var5.setTargetRotation(var25.getRotation());
        PreviewConfig previewConfig = var5.build();
        Preview preview = new Preview(previewConfig);
        preview.setOnPreviewOutputUpdateListener((OnPreviewOutputUpdateListener)(new OnPreviewOutputUpdateListener() {
            public final void onUpdated(PreviewOutput it) {
                TextureView var10000 = (TextureView)findViewById(R.id.textureView);
                Intrinsics.checkExpressionValueIsNotNull(var10000, "textureView");
                Intrinsics.checkExpressionValueIsNotNull(it, "it");
                ViewGroup parent = (ViewGroup) var26.getParent();
                parent.removeView(var26);
                parent.addView(var26, 0);
                SurfaceTexture surfaceTexture = it.getSurfaceTexture();
                var26.setSurfaceTexture(surfaceTexture);
                MainActivity.this.updateTransform();
            }
        }));
        androidx.camera.core.ImageAnalysisConfig.Builder var23 = new androidx.camera.core.ImageAnalysisConfig.Builder();
        boolean var8 = false;
        var9 = false;
        boolean var11 = false;
        HandlerThread var12 = new HandlerThread("AnalysisThread");
        boolean var13 = false;
        boolean var14 = false;
        boolean var16 = false;
        var12.start();
        var23.setCallbackHandler(new Handler(var12.getLooper()));
        var23.setImageReaderMode(ImageReaderMode.ACQUIRE_LATEST_IMAGE);
        ImageAnalysisConfig analyzerConfig = var23.build();
        ImageAnalysis analyzerUseCase = new ImageAnalysis(analyzerConfig);
        analyzerUseCase.setAnalyzer((Analyzer)(new Analyzer() {
            public final void analyze(@NotNull ImageProxy image, int rotationDegrees) {
                Intrinsics.checkParameterIsNotNull(image, "image");
                Bitmap bitmap = MainActivity.this.toBitmap(image);
                MainActivity.this.tfLiteClassifier.classifyAsync(bitmap).addOnSuccessListener((OnSuccessListener)(new OnSuccessListener() {
                    // $FF: synthetic method
                    // $FF: bridge method
                    public void onSuccess(Object var1) {
                        this.onSuccess((String)var1);
                    }

                    public final void onSuccess(String resultText) {

                        TextView var10000 = (TextView)findViewById(R.id.predictedTextView);
                        if (var10000 != null) {
                            var10000.setText((CharSequence)resultText);
                        }

                    }
                }));
                MainActivity.this.tfLiteClassifier.classifyAsync(bitmap).addOnFailureListener((new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                }));
            }
        }));
        CameraX.bindToLifecycle((LifecycleOwner)this, new UseCase[]{(UseCase)preview, (UseCase)analyzerUseCase});
    }

    @NotNull
    public final Bitmap toBitmap(@NotNull ImageProxy $this$toBitmap) {
        Intrinsics.checkParameterIsNotNull($this$toBitmap, "$this$toBitmap");
        PlaneProxy var10000 = $this$toBitmap.getPlanes()[0];
        Intrinsics.checkExpressionValueIsNotNull(var10000, "planes[0]");
        ByteBuffer yBuffer = var10000.getBuffer();
        var10000 = $this$toBitmap.getPlanes()[1];
        Intrinsics.checkExpressionValueIsNotNull(var10000, "planes[1]");
        ByteBuffer uBuffer = var10000.getBuffer();
        var10000 = $this$toBitmap.getPlanes()[2];
        Intrinsics.checkExpressionValueIsNotNull(var10000, "planes[2]");
        ByteBuffer vBuffer = var10000.getBuffer();
        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();
        byte[] nv21 = new byte[ySize + uSize + vSize];
        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);
        YuvImage yuvImage = new YuvImage(nv21, 17, $this$toBitmap.getWidth(), $this$toBitmap.getHeight(), (int[])null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 100, (OutputStream)out);
        byte[] imageBytes = out.toByteArray();
        Bitmap var12 = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        Intrinsics.checkExpressionValueIsNotNull(var12, "BitmapFactory.decodeByte…ytes, 0, imageBytes.size)");
        return var12;
    }

    private final void updateTransform() {
        Matrix matrix = new Matrix();
        TextureView var10000 = (TextureView)findViewById(R.id.textureView);
        Intrinsics.checkExpressionValueIsNotNull(var10000, "textureView");
        float centerX = (float)var10000.getWidth() / 2.0F;
        var10000 = (TextureView)findViewById(R.id.textureView);
        Intrinsics.checkExpressionValueIsNotNull(var10000, "textureView");
        float centerY = (float)var10000.getHeight() / 2.0F;
        var10000 = (TextureView)findViewById(R.id.textureView);
        Intrinsics.checkExpressionValueIsNotNull(var10000, "textureView");
        Display var5 = var10000.getDisplay();
        Intrinsics.checkExpressionValueIsNotNull(var5, "textureView.display");
        short var6;
        switch(var5.getRotation()) {
            case 0:
                var6 = 0;
                break;
            case 1:
                var6 = 90;
                break;
            case 2:
                var6 = 180;
                break;
            case 3:
                var6 = 270;
                break;
            default:
                return;
        }

        int rotationDegrees = var6;
        matrix.postRotate(-((float)rotationDegrees), centerX, centerY);
        ((TextureView)findViewById(R.id.textureView)).setTransform(matrix);
    }

    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        Intrinsics.checkParameterIsNotNull(permissions, "permissions");
        Intrinsics.checkParameterIsNotNull(grantResults, "grantResults");
        if (requestCode == this.REQUEST_CODE_PERMISSIONS) {
            if (this.allPermissionsGranted()) {
                this.startCamera();
            } else {
                Toast.makeText((Context)this, (CharSequence)"Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }

    }

    private final boolean allPermissionsGranted() {
        String[] var3 = this.REQUIRED_PERMISSIONS;
        int var4 = var3.length;

        for(int var2 = 0; var2 < var4; ++var2) {
            String permission = var3[var2];
            if (ContextCompat.checkSelfPermission((Context)this, permission) != 0) {
                return false;
            }
        }

        return true;
    }

    protected void onDestroy() {
        this.tfLiteClassifier.close();
        super.onDestroy();
    }

    public MainActivity() {
        this.lensFacing = LensFacing.BACK;
        this.TAG = "MainActivity";
        this.REQUEST_CODE_PERMISSIONS = 101;
        this.REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA"};
        this.tfLiteClassifier = new TFLiteClassifier((Context)this);
    }

    // $FF: synthetic method
    public static final void access$setTfLiteClassifier$p(MainActivity $this, TFLiteClassifier var1) {
        $this.tfLiteClassifier = var1;
    }

    public View _$_findCachedViewById(int var1) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }

        View var2 = (View)this._$_findViewCache.get(var1);
        if (var2 == null) {
            var2 = this.findViewById(var1);
            this._$_findViewCache.put(var1, var2);
        }

        return var2;
    }

    public void _$_clearFindViewByIdCache() {
        if (this._$_findViewCache != null) {
            this._$_findViewCache.clear();
        }

    }
}
