package com.example.eventplanner.viewmodel;

import android.app.Application; // Koristimo AndroidViewModel
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.example.eventplanner.dto.category.GetCategoryDTO;
import com.example.eventplanner.dto.product.GetProductDTO;
import com.example.eventplanner.dto.product.UpdateProductDTO;
import com.example.eventplanner.dto.product.UpdatedProductDTO;
import com.example.eventplanner.dto.serviceProductProvider.GetSPProviderDTO;
import com.example.eventplanner.utils.SingleLiveEvent;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProductViewModel extends AndroidViewModel {

    // LiveData za podatke sa servera
    private final MutableLiveData<GetProductDTO> product = new MutableLiveData<>();
    private final MutableLiveData<List<GetCategoryDTO>> categories = new MutableLiveData<>();

    // LiveData za stanje forme
    public final MutableLiveData<String> description = new MutableLiveData<>();
    public final MutableLiveData<String> price = new MutableLiveData<>();
    public final MutableLiveData<String> discount = new MutableLiveData<>();
    public final MutableLiveData<Integer> selectedCategoryId = new MutableLiveData<>();
    public final MutableLiveData<String> proposedCategory = new MutableLiveData<>();
    public final MutableLiveData<Boolean> isProposingCategory = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> isAvailable = new MutableLiveData<>();
    public final MutableLiveData<Boolean> isVisible = new MutableLiveData<>();

    // LiveData za UI događaje
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final SingleLiveEvent<String> snackbarMessage = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> updateSuccess = new SingleLiveEvent<>();
    public GetAuthenticatedUserDTO user;

    public EditProductViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<GetProductDTO> getProduct() { return product; }
    public LiveData<List<GetCategoryDTO>> getCategories() { return categories; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public SingleLiveEvent<String> getSnackbarMessage() { return snackbarMessage; }
    public SingleLiveEvent<Boolean> getUpdateSuccess() { return updateSuccess; }

    // Glavna metoda za učitavanje svih potrebnih podataka
    public void loadProductAndCategories(int productId) {
        isLoading.setValue(true);
        loadCategories(); // Učitaj kategorije

        ClientUtils.productService.getById(productId).enqueue(new Callback<GetProductDTO>() {
            @Override
            public void onResponse(Call<GetProductDTO> call, Response<GetProductDTO> response) {
                if (response.isSuccessful()) {
                    GetProductDTO fetchedProduct = response.body();
                    product.setValue(fetchedProduct);
                    // Popuni LiveData polja forme sa postojećim podacima
                    populateFormFields(fetchedProduct);
                } else {
                    snackbarMessage.setValue("Failed to load product details.");
                }
                isLoading.setValue(false);
            }
            @Override
            public void onFailure(Call<GetProductDTO> call, Throwable t) {
                snackbarMessage.setValue("Network error: " + t.getMessage());
                isLoading.setValue(false);
            }
        });
    }

    private void populateFormFields(GetProductDTO p) {
        description.setValue(p.getDescription());
        price.setValue(String.valueOf(p.getPrice()));
        discount.setValue(String.valueOf(p.getDiscount()));
        isAvailable.setValue(p.isAvailable());
        isVisible.setValue(p.isVisible());
        if (p.getCategory() != null) {
            selectedCategoryId.setValue(p.getCategory().getId());
            isProposingCategory.setValue(false);
        } else {
            selectedCategoryId.setValue(null);
            // Ako je kategorija null, možda je bio predlog? Za sada ostavljamo prazno.
        }
    }

    public void updateProduct() {
        if (!isFormValid()) { return; }
        isLoading.setValue(true);

        UpdateProductDTO dto = buildUpdateDTO();
        int productId = product.getValue().getId();

        ClientUtils.productService.updateProduct(productId, dto).enqueue(new Callback<UpdatedProductDTO>() {
            @Override
            public void onResponse(Call<UpdatedProductDTO> call, Response<UpdatedProductDTO> response) {
                if (response.isSuccessful()) {
                    snackbarMessage.setValue("Product updated successfully!");
                    updateSuccess.setValue(true);
                } else {
                    snackbarMessage.setValue("Error updating product. Code: " + response.code());
                }
                isLoading.setValue(false);
            }
            @Override
            public void onFailure(Call<UpdatedProductDTO> call, Throwable t) {
                snackbarMessage.setValue("Network error: " + t.getMessage());
                isLoading.setValue(false);
            }
        });
    }

    private UpdateProductDTO buildUpdateDTO() {
        UpdateProductDTO dto = new UpdateProductDTO();
        dto.setDescription(description.getValue());
        dto.setPrice(Double.parseDouble(price.getValue()));
        dto.setDiscount(Integer.parseInt(discount.getValue()));
        dto.setAvailable(Boolean.TRUE.equals(isAvailable.getValue()));
        dto.setVisible(Boolean.TRUE.equals(isVisible.getValue()));
        GetSPProviderDTO providerDTO = new GetSPProviderDTO();
        providerDTO.setId(user.getId());
        dto.setSPProvider(providerDTO);

        if (Boolean.TRUE.equals(isProposingCategory.getValue())) {
            dto.setCategory(null);
            dto.setNewCategory(proposedCategory.getValue());
        } else if (selectedCategoryId.getValue() != null) {
            // Pronađi kategoriju iz liste i postavi je
            GetCategoryDTO cat = findCategoryById(selectedCategoryId.getValue());
            dto.setCategory(cat);
            dto.setNewCategory(null);
        } else {
            dto.setCategory(null);
            dto.setNewCategory(null);
        }
        return dto;
    }

    private GetCategoryDTO findCategoryById(int id) {
        if (categories.getValue() != null) {
            for (GetCategoryDTO category : categories.getValue()) {
                if (category.getId().equals(id)) {
                    return category;
                }
            }
        }
        return null;
    }

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

    private boolean isFormValid() {
        if (selectedCategoryId.getValue() == null && !Boolean.TRUE.equals(isProposingCategory.getValue())) {
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
            if (disc < -0.00001 || disc > 100) {
                snackbarMessage.setValue("Discount must be between 0 and 100.");
                return false;
            }
        } catch (NumberFormatException e) {
            snackbarMessage.setValue("Invalid discount format.");
            return false;
        }
        return true;
    }
}