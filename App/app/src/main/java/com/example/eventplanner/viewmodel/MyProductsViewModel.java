package com.example.eventplanner.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.dto.product.GetProductDTO;
import com.example.eventplanner.dto.product.UpdatedProductDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProductsViewModel extends ViewModel {

    private final MutableLiveData<List<GetProductDTO>> products = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    // Originalna lista, da bismo mogli da uradimo reset pretrage
    private List<GetProductDTO> originalProductList = new ArrayList<>();

    public LiveData<List<GetProductDTO>> getProducts() {
        return products;
    }
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchProducts(Integer providerId) {
        isLoading.setValue(true);
        Call<List<GetProductDTO>> call = ClientUtils.productService.getProductsBySpp(providerId);
        call.enqueue(new Callback<List<GetProductDTO>>() {
            @Override
            public void onResponse(Call<List<GetProductDTO>> call, Response<List<GetProductDTO>> response) {
                if (response.isSuccessful()) {
                    originalProductList = response.body();
                    products.setValue(new ArrayList<>(originalProductList)); // Šaljemo kopiju
                } else {
                    errorMessage.setValue("Error fetching products. Code: " + response.code());
                }
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<List<GetProductDTO>> call, Throwable t) {
                errorMessage.setValue("Network error: " + t.getMessage());
                isLoading.setValue(false);
            }
        });
    }

    public void toggleVisibility(GetProductDTO product) {
        Call<UpdatedProductDTO> call = ClientUtils.productService.toggleVisibility(product.getId());
        call.enqueue(new Callback<UpdatedProductDTO>() {
            @Override
            public void onResponse(Call<UpdatedProductDTO> call, Response<UpdatedProductDTO> response) {
                if (response.isSuccessful()) {
                    // Ažuriraj lokalnu listu da se promena odmah vidi
                    List<GetProductDTO> currentProducts = new ArrayList<>(products.getValue());
                    for (GetProductDTO p : currentProducts) {
                        if (p.getId().equals(product.getId())) {
                            p.setVisible(!p.isVisible());
                            break;
                        }
                    }
                    products.setValue(currentProducts);
                } else {
                    errorMessage.setValue("Failed to update visibility.");
                }
            }

            @Override
            public void onFailure(Call<UpdatedProductDTO> call, Throwable t) {
                errorMessage.setValue("Network error.");
            }
        });
    }

    public void deleteProduct(GetProductDTO product) {
        Call<Void> call = ClientUtils.productService.deleteProduct(product.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Ukloni proizvod iz lokalne liste
                    List<GetProductDTO> currentProducts = new ArrayList<>(products.getValue());
                    currentProducts.removeIf(p -> p.getId().equals(product.getId()));
                    products.setValue(currentProducts);
                } else {
                    errorMessage.setValue("Failed to delete product.");
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMessage.setValue("Network error.");
            }
        });
    }

    public void resetSearch() {
        products.setValue(new ArrayList<>(originalProductList));
    }
}