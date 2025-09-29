package com.example.eventplanner.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.dto.eventType.GetEventTypeDTO;
import com.example.eventplanner.dto.eventType.UpdatedEventTypeDTO;
import com.example.eventplanner.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventTypeViewModel extends ViewModel {

    private final MutableLiveData<List<GetEventTypeDTO>> eventTypes = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final SingleLiveEvent<String> snackbarMessage = new SingleLiveEvent<>();

    // Getteri koje Fragment posmatra
    public LiveData<List<GetEventTypeDTO>> getEventTypes() { return eventTypes; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public SingleLiveEvent<String> getSnackbarMessage() { return snackbarMessage; }

    // Metoda za dobavljanje podataka sa servera
    public void fetchEventTypes() {
        isLoading.setValue(true);
        ClientUtils.eventTypeService.getAllEventTypes().enqueue(new Callback<ArrayList<GetEventTypeDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<GetEventTypeDTO>> call, Response<ArrayList<GetEventTypeDTO>> response) {
                if (response.isSuccessful()) {
                    eventTypes.setValue(response.body());
                } else {
                    snackbarMessage.setValue("Error fetching event types. Code: " + response.code());
                }
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<ArrayList<GetEventTypeDTO>> call, Throwable t) {
                snackbarMessage.setValue("Network error: " + t.getMessage());
                isLoading.setValue(false);
            }
        });
    }

    // Metoda za promenu statusa aktivacije
    public void toggleActivationStatus(GetEventTypeDTO eventType) {
        isLoading.setValue(true);
        ClientUtils.eventTypeService.changeActivation(eventType.getId()).enqueue(new Callback<UpdatedEventTypeDTO>() {
            @Override
            public void onResponse(Call<UpdatedEventTypeDTO> call, Response<UpdatedEventTypeDTO> response) {
                if (response.isSuccessful()) {
                    // Ažuriraj lokalnu listu da se promena odmah vidi u UI
                    List<GetEventTypeDTO> currentList = new ArrayList<>(eventTypes.getValue());
                    for (GetEventTypeDTO et : currentList) {
                        if (et.getId().equals(eventType.getId())) {
                            et.setActive(!et.isActive());
                            break;
                        }
                    }
                    eventTypes.setValue(currentList);
                    snackbarMessage.setValue("Status updated successfully.");
                } else {
                    snackbarMessage.setValue("Failed to update status.");
                }
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<UpdatedEventTypeDTO> call, Throwable t) {
                snackbarMessage.setValue("Network error: " + t.getMessage());
                isLoading.setValue(false);
            }
        });
    }
}