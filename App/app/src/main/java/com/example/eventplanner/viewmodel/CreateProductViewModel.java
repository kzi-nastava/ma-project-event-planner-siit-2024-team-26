package com.example.eventplanner.viewmodel;

import android.app.Application;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.dto.category.GetCategoryDTO;
import com.example.eventplanner.dto.product.CreateProductDTO;
import com.example.eventplanner.dto.product.CreatedProductDTO;
import com.example.eventplanner.dto.serviceProductProvider.GetSPProviderDTO;
import com.example.eventplanner.utils.SingleLiveEvent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateProductViewModel extends AndroidViewModel {

    // LiveData za polja forme
    public final MutableLiveData<String> name = new MutableLiveData<>("");
    public final MutableLiveData<String> description = new MutableLiveData<>("");
    public final MutableLiveData<String> price = new MutableLiveData<>("");
    public final MutableLiveData<String> discount = new MutableLiveData<>("0");
    public final MutableLiveData<GetCategoryDTO> selectedCategory = new MutableLiveData<>();
    public final MutableLiveData<String> proposedCategory = new MutableLiveData<>("");
    public final MutableLiveData<Boolean> isProposingCategory = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> isAvailable = new MutableLiveData<>(true);
    public final MutableLiveData<Boolean> isVisible = new MutableLiveData<>(true);
    public GetSPProviderDTO currentUser;

    // LiveData za UI
    private final MutableLiveData<List<GetCategoryDTO>> categories = new MutableLiveData<>();
    private final MutableLiveData<List<Uri>> selectedImageUris = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final SingleLiveEvent<String> snackbarMessage = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> creationSuccess = new SingleLiveEvent<>();

    public CreateProductViewModel(@NonNull Application application) {
        super(application);
    }

    // Getteri za LiveData koje Fragment posmatra
    public LiveData<List<GetCategoryDTO>> getCategories() { return categories; }
    public LiveData<List<Uri>> getSelectedImageUris() { return selectedImageUris; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public SingleLiveEvent<String> getSnackbarMessage() { return snackbarMessage; }
    public SingleLiveEvent<Boolean> getCreationSuccess() { return creationSuccess; }
    public void setSelectedImageUris(List<Uri> uris) { selectedImageUris.setValue(uris); }
    public void loadCurrentUser(Long userId) { /* ... Implementacija poziva za dobavljanje korisnika ... */ }

    public void loadCategories() {
        isLoading.setValue(true);
        ClientUtils.categoryService.getAllCategoriesAlphabetically().enqueue(new Callback<List<GetCategoryDTO>>() {
            @Override
            public void onResponse(Call<List<GetCategoryDTO>> call, Response<List<GetCategoryDTO>> response) {
                if (response.isSuccessful()) {
                    categories.setValue(response.body());
                } else {
                    snackbarMessage.setValue("Failed to load categories.");
                }
                isLoading.setValue(false);
            }
            @Override
            public void onFailure(Call<List<GetCategoryDTO>> call, Throwable t) {
                snackbarMessage.setValue("Network error while fetching categories.");
                isLoading.setValue(false);
            }
        });
    }

    public void saveProduct() {
        if (!isFormValid()) { return; }
        isLoading.setValue(true);

        CreateProductDTO dto = buildProductDTO();
        ClientUtils.productService.createProduct(dto).enqueue(new Callback<CreatedProductDTO>() {
            @Override
            public void onResponse(Call<CreatedProductDTO> call, Response<CreatedProductDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int newProductId = response.body().getId();
                    if (selectedImageUris.getValue() != null && !selectedImageUris.getValue().isEmpty()) {
                        uploadImages(newProductId);
                    } else {
                        isLoading.setValue(false);
                        snackbarMessage.setValue("Product created successfully!");
                        creationSuccess.setValue(true);
                    }
                } else {
                    isLoading.setValue(false);
                    snackbarMessage.setValue("Error creating product. Code: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<CreatedProductDTO> call, Throwable t) {
                isLoading.setValue(false);
                snackbarMessage.setValue("Network error: " + t.getMessage());
            }
        });
    }

    private void uploadImages(int productId) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        ContentResolver resolver = getApplication().getContentResolver();

        for (Uri uri : selectedImageUris.getValue()) {
            try (InputStream inputStream = resolver.openInputStream(uri)) {
                String mimeType = resolver.getType(uri);
                String fileName = getFileNameFromUri(uri);

                // ================== ISPRAVKA OVDE ==================
                // Čitamo InputStream u byte array na način kompatibilan sa starijim API levelima
                byte[] fileBytes = readBytes(inputStream);
                RequestBody requestBody = RequestBody.create(MediaType.parse(mimeType), fileBytes);
                // ===================================================

                parts.add(MultipartBody.Part.createFormData("files", fileName, requestBody));
            } catch (Exception e) {
                Log.e("ViewModel", "Error creating multipart part", e);
            }
        }

        if (parts.isEmpty()) {
            isLoading.setValue(false);
            snackbarMessage.setValue("Product created, but no valid images to upload.");
            creationSuccess.setValue(true);
            return;
        }

        ClientUtils.spImageService.uploadImages(productId, "product", parts).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    snackbarMessage.setValue("Product and images saved successfully!");
                } else {
                    snackbarMessage.setValue("Product saved, but image upload failed. Code: " + response.code());
                }
                creationSuccess.setValue(true);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                isLoading.setValue(false);
                snackbarMessage.setValue("Product saved, but image upload failed: " + t.getMessage());
                creationSuccess.setValue(true);
            }
        });
    }

    // NOVA POMOĆNA METODA za čitanje InputStream-a
    private byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getApplication().getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        result = cursor.getString(index);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private boolean isFormValid() {
        if (TextUtils.isEmpty(name.getValue())) {
            snackbarMessage.setValue("Name is required.");
            return false;
        }
        if (selectedCategory.getValue() == null && !Boolean.TRUE.equals(isProposingCategory.getValue())) {
            snackbarMessage.setValue("Category is required.");
            return false;
        }
        if (Boolean.TRUE.equals(isProposingCategory.getValue()) && TextUtils.isEmpty(proposedCategory.getValue())) {
            snackbarMessage.setValue("Proposed category name is required.");
            return false;
        }
        if (TextUtils.isEmpty(price.getValue())) {
            snackbarMessage.setValue("Price is required.");
            return false;
        }
        try {
            if (Double.parseDouble(price.getValue()) < 0) {
                snackbarMessage.setValue("Price must not be lower than 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            snackbarMessage.setValue("Invalid price format.");
            return false;
        }
        if (TextUtils.isEmpty(discount.getValue())) {
            snackbarMessage.setValue("Discount is required (0-100).");
            return false;
        }
        try {
            int disc = Integer.parseInt(discount.getValue());
            if (disc < 0 || disc > 100) {
                snackbarMessage.setValue("Discount must be between 0 and 100.");
                return false;
            }
        } catch (NumberFormatException e) {
            snackbarMessage.setValue("Invalid discount format.");
            return false;
        }
        return true;
    }

    private CreateProductDTO buildProductDTO() {
        CreateProductDTO dto = new CreateProductDTO();
        dto.setName(name.getValue());
        dto.setDescription(description.getValue());
        dto.setPrice(Double.parseDouble(price.getValue()));
        dto.setDiscount(Integer.parseInt(discount.getValue()));
        dto.setServiceProductProvider(currentUser);

        if (Boolean.TRUE.equals(isProposingCategory.getValue())) {
            dto.setCategory(null);
            dto.setNewCategory(proposedCategory.getValue());
        } else {
            dto.setCategory(selectedCategory.getValue());
            dto.setNewCategory(null);
        }

        return dto;
    }
}