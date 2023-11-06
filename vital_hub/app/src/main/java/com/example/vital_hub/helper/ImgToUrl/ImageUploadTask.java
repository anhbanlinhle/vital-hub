package com.example.vital_hub.helper.ImgToUrl;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import com.example.vital_hub.client.spring.objects.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ImageUploadTask {
    private final String apiKey = "e6337ef66f90fd45b0e3e6d6050230e1";
    private final ImageView imageView;
    private ImageUploadCallback callback;

    public ImageUploadTask(ImageView imageView, ImageUploadCallback callback) {
        this.imageView = imageView;
        this.callback = callback;
    }

    public void execute() {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.imgbb.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ImgBBApi imgBBApi = retrofit.create(ImgBBApi.class);

        Call<UploadResponse> call = imgBBApi.uploadImage(apiKey, base64Image);
        call.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UploadResponse uploadResponse = response.body();
                    ImageData imageData = uploadResponse.getData();
                    String imageUrl = imageData.getUrl();

                    if (callback != null) {
                        callback.onImageUploaded(imageUrl);
                    }
                } else {
                    if (callback != null) {
                        callback.onUploadFailed();
                    }
                }
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                if (callback != null) {
                    callback.onUploadFailed();
                }
            }
        });
    }

    public interface ImageUploadCallback {
        void onImageUploaded(String imageUrl);
        void onUploadFailed();
    }
}
