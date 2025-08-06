package com.example.eventplanner.fragments.details;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.ProductImageAdapter;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.clients.service.SPImageService;
import com.example.eventplanner.databinding.FragmentProductDetailsBinding;
import com.example.eventplanner.dto.product.GetProductDTO;
import com.example.eventplanner.dto.serviceProductImage.GetSPImageDTO;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.model.ServiceProductImage;
import com.example.eventplanner.model.ServiceProductType;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductDetailsFragment extends Fragment {

    private Integer productId;
    private GetProductDTO foundProduct;
    private FragmentProductDetailsBinding binding;
    private boolean isExpanded = false;

    private Animation fromBottomFabAnim;
    private Animation toBottomFabAnim;
    private Animation rotateClockWiseFabAnim;
    private Animation rotateAntiClockWiseFabAnim;
    private Animation fromBottomBgAnim;
    private Animation toBottomBgAnim;

    public static ProductDetailsFragment newInstance(Integer productId) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("productId", productId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productId = getArguments().getInt("productId");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false);
        getProductDetails();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Animacije
        fromBottomFabAnim = AnimationUtils.loadAnimation(getContext(), R.anim.from_bottom_fab);
        toBottomFabAnim = AnimationUtils.loadAnimation(getContext(), R.anim.to_bottom_fab);
        rotateClockWiseFabAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_clock_wise);
        rotateAntiClockWiseFabAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anti_clock_wise);
        fromBottomBgAnim = AnimationUtils.loadAnimation(getContext(), R.anim.from_bottom_anim);
        toBottomBgAnim = AnimationUtils.loadAnimation(getContext(), R.anim.to_bottom_anim);

        binding.fabMain.setOnClickListener(v -> {
            if (isExpanded) shrinkFab(); else expandFab();
        });

        binding.fabFavouritesP.setOnClickListener(v -> onFavouritesClicked());
        binding.textFavouritesP.setOnClickListener(v -> onFavouritesClicked());

        binding.fabReviews.setOnClickListener(v -> onReviewsClicked());
        binding.textReviews.setOnClickListener(v -> onReviewsClicked());

        binding.fabPProvider.setOnClickListener(v -> onProviderClicked());
        binding.textPProvider.setOnClickListener(v -> onProviderClicked());

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isExpanded) {
                    shrinkFab();
                } else {
                    setEnabled(false);
                    requireActivity().onBackPressed();
                }
            }
        });
    }

    private void onFavouritesClicked() {
        Toast.makeText(getContext(), "Add to favourites", Toast.LENGTH_SHORT).show();
    }

    private void onReviewsClicked() {
        Toast.makeText(getContext(), "Add review", Toast.LENGTH_SHORT).show();
    }

    private void onProviderClicked() {
        if (foundProduct != null && foundProduct.getSpProvider() != null) {
            Integer providerId = foundProduct.getSpProvider().getId();

            FragmentTransition.to(
                    UserDetailsFragment.newInstance(providerId),
                    getActivity(),
                    true,
                    R.id.mainScreenFragment
            );
        } else {
            Toast.makeText(getContext(), "Provider not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void expandFab() {
        binding.transparentBg.setVisibility(View.VISIBLE);
        binding.transparentBg.startAnimation(fromBottomBgAnim);
        binding.fabMain.startAnimation(rotateClockWiseFabAnim);
        binding.fabFavouritesP.setVisibility(View.VISIBLE);
        binding.textFavouritesP.setVisibility(View.VISIBLE);
        binding.fabReviews.setVisibility(View.VISIBLE);
        binding.textReviews.setVisibility(View.VISIBLE);
        binding.fabPProvider.setVisibility(View.VISIBLE);
        binding.textPProvider.setVisibility(View.VISIBLE);

        binding.fabFavouritesP.startAnimation(fromBottomFabAnim);
        binding.textFavouritesP.startAnimation(fromBottomFabAnim);
        binding.fabReviews.startAnimation(fromBottomFabAnim);
        binding.textReviews.startAnimation(fromBottomFabAnim);
        binding.fabPProvider.startAnimation(fromBottomFabAnim);
        binding.textPProvider.startAnimation(fromBottomFabAnim);
        isExpanded = true;
    }

    private void shrinkFab() {
        binding.transparentBg.startAnimation(toBottomBgAnim);
        binding.fabMain.startAnimation(rotateAntiClockWiseFabAnim);
        binding.fabFavouritesP.startAnimation(toBottomFabAnim);
        binding.textFavouritesP.startAnimation(toBottomFabAnim);
        binding.fabReviews.startAnimation(toBottomFabAnim);
        binding.textReviews.startAnimation(toBottomFabAnim);
        binding.fabPProvider.startAnimation(toBottomFabAnim);
        binding.textPProvider.startAnimation(toBottomFabAnim);

        binding.transparentBg.setVisibility(View.GONE);
        binding.fabFavouritesP.setVisibility(View.INVISIBLE);
        binding.textFavouritesP.setVisibility(View.INVISIBLE);
        binding.fabReviews.setVisibility(View.INVISIBLE);
        binding.textReviews.setVisibility(View.INVISIBLE);
        binding.fabPProvider.setVisibility(View.INVISIBLE);
        binding.textPProvider.setVisibility(View.INVISIBLE);
        isExpanded = false;
    }

    private void getProductDetails() {
        ClientUtils.productService.getById(productId).enqueue(new Callback<GetProductDTO>() {
            @Override
            public void onResponse(Call<GetProductDTO> call, Response<GetProductDTO> response) {
                if (response.isSuccessful()) {
                    foundProduct = response.body();
                    setAttributes();
                }
            }

            @Override
            public void onFailure(Call<GetProductDTO> call, Throwable t) {
                Log.e("ProductDetails", "Error fetching product: " + t.getMessage());
            }
        });
    }

    private void setAttributes() {
        binding.productName.setText(foundProduct.getName());
        binding.description.setText(foundProduct.getDescription());
        binding.categoryText.setText(foundProduct.getCategory().getName());
        binding.priceText.setText(String.format("%.2f €", foundProduct.getPrice()));
        binding.discountText.setText(String.format("%.0f%%", foundProduct.getDiscount()));
        String rating = "N/A";
        if (foundProduct.getGradeCount() != 0) {
            rating = String.format("%.2f", ((float) foundProduct.getGradeSum()) / ((float) foundProduct.getGradeCount()));
        }
        binding.ratingText.setText(String.valueOf(rating));
        String ppName = foundProduct.getSpProvider().getFirstName() + " " + foundProduct.getSpProvider().getLastName();
        binding.ppText.setText(ppName);
        if (foundProduct.isAvailable()) {
            binding.availabilityText.setText("Available");
        } else {
            binding.availabilityText.setText("Unavailable");
        }

        RecyclerView recyclerView = binding.productImages;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

// Pretpostavimo da već imaš id proizvoda (npr. iz Bundle-a)
        int productId = getArguments().getInt("productId");

        Call<List<GetSPImageDTO>> call = ClientUtils.spImageService.getImagesByServiceProduct("product", productId);

        call.enqueue(new Callback<List<GetSPImageDTO>>() {
            @Override
            public void onResponse(Call<List<GetSPImageDTO>> call, Response<List<GetSPImageDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ServiceProductImage> imageList = new ArrayList<>();
                    for (GetSPImageDTO dto : response.body()) {

                        String fullUrl = ClientUtils.SERVICE_API_PATH + "service-product-images/" + foundProduct.getId() + "/" + dto.getImageSource();

                        ServiceProductImage image = new ServiceProductImage();
                        image.setId(dto.getId());
                        image.setImageSource(fullUrl);
                        // image.setServiceProduct(dto.getServiceProduct());
                        imageList.add(image);
                    }

                    ProductImageAdapter adapter = new ProductImageAdapter(getContext(), imageList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Failed to load images", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GetSPImageDTO>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
