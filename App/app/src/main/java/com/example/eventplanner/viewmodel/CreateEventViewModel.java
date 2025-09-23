package com.example.eventplanner.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.clients.service.EventService;
import com.example.eventplanner.clients.service.EventTypeService;
import com.example.eventplanner.clients.service.InvitationService;
import com.example.eventplanner.dto.address.CreateAddressDTO;
import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.example.eventplanner.dto.event.CreateEventDTO;
import com.example.eventplanner.dto.event.CreatedEventDTO;
import com.example.eventplanner.dto.event.EventReservationDTO;
import com.example.eventplanner.dto.eventOrganizer.GetEventOrganizerDTO;
import com.example.eventplanner.dto.eventType.GetEventTypeDTO;
import com.example.eventplanner.dto.invitation.CreateInvitationDTO;
import com.example.eventplanner.dto.invitation.CreatedInvitationDTO;
import com.example.eventplanner.dto.location.CreateLocationDTO;
import com.example.eventplanner.model.*;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateEventViewModel extends ViewModel {
    public final EventTypeService eventTypeService = ClientUtils.eventTypeService;
    public final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    public final MutableLiveData<List<GetEventTypeDTO>> eventTypes = new MutableLiveData<>();

    // Data for Step 1
    public final MutableLiveData<String> name = new MutableLiveData<>("");
    public final MutableLiveData<String> description = new MutableLiveData<>("");
    public final MutableLiveData<String> privacyType = new MutableLiveData<>("OPEN");
    public final MutableLiveData<GetEventTypeDTO> eventType = new MutableLiveData<>();
    public final MutableLiveData<String> maxParticipants = new MutableLiveData<>("");

    // Data for Step 2
    public final MutableLiveData<String> country = new MutableLiveData<>("");
    public final MutableLiveData<String> city = new MutableLiveData<>("");
    public final MutableLiveData<String> street = new MutableLiveData<>("");
    public final MutableLiveData<String> number = new MutableLiveData<>("");
    public final MutableLiveData<Calendar> startDate = new MutableLiveData<>();
    public final MutableLiveData<Calendar> endDate = new MutableLiveData<>();

    // Data for Step 3
    public final MutableLiveData<List<String>> emailList = new MutableLiveData<>(new ArrayList<>());

    // UI Events
    public final MutableLiveData<String> snackbarMessage = new MutableLiveData<>();
    public final MutableLiveData<Boolean> eventCreationSuccess = new MutableLiveData<>(false);

    private EventService eventService;
    public GetAuthenticatedUserDTO currentUser;

    public void setCurrentUser(GetAuthenticatedUserDTO user) {
        currentUser = user;
    }

    private ArrayList<String> enteredEmails;

    public void fetchEventTypes() {
        isLoading.setValue(true); // Počelo je učitavanje

        Call<ArrayList<GetEventTypeDTO>> call = eventTypeService.getAllEventTypes();
        call.enqueue(new Callback<ArrayList<GetEventTypeDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<GetEventTypeDTO>> call, Response<ArrayList<GetEventTypeDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    eventTypes.setValue(response.body()); // Uspešno, postavi podatke
                } else {
                    errorMessage.setValue("Failed to load event types. Code: " + response.code());
                }
                isLoading.setValue(false); // Završeno je učitavanje
            }

            @Override
            public void onFailure(Call<ArrayList<GetEventTypeDTO>> call, Throwable t) {
                errorMessage.setValue("Network error: " + t.getMessage());
                isLoading.setValue(false); // Završeno je učitavanje (neuspešno)
            }
        });
    }
    public String[] getMockCountries() {
        return new String[]{"Serbia", "United States", "France", "Japan", "Romania", "Sweden"};
    }


    public boolean isStep1Valid() {
        boolean isNameValid = name.getValue() != null && !name.getValue().trim().isEmpty();
        boolean isDescValid = description.getValue() != null && !description.getValue().trim().isEmpty();
        boolean isTypeValid = eventType.getValue() != null;
        boolean isParticipantsValid = maxParticipants.getValue() != null && !maxParticipants.getValue().isEmpty() && Integer.parseInt(maxParticipants.getValue()) > 0;
        return isNameValid && isDescValid && isTypeValid && isParticipantsValid;
    }

    public boolean isStep2Valid() {
        boolean isCountryValid = country.getValue() != null && !country.getValue().isEmpty();
        boolean isCityValid = city.getValue() != null && !city.getValue().trim().isEmpty();
        boolean isStreetValid = street.getValue() != null && !street.getValue().trim().isEmpty();
        boolean isNumberValid = number.getValue() != null && !number.getValue().isEmpty() && Integer.parseInt(number.getValue()) > 0;
        boolean isStartDateValid = startDate.getValue() != null;
        boolean isEndDateValid = endDate.getValue() != null;
        boolean isDateRangeValid = isStartDateValid && isEndDateValid && !endDate.getValue().before(startDate.getValue());
        return isCountryValid && isCityValid && isStreetValid && isNumberValid && isDateRangeValid;
    }

    public CreateEventViewModel() {
        // Inicijalizacija servisa preko Retrofit klijenta
        eventService = ClientUtils.eventService;
        // activityService = RetrofitClient.getClient().create(ActivityService.class);
        // ... inicijalizujte i ostale servise
    }

    public void submitEvent() {
        if (!isStep1Valid() || !isStep2Valid()) {
            snackbarMessage.setValue("Please ensure all fields are filled correctly.");
            return;
        }

        isLoading.setValue(true);

        // 1. Kreiramo DTO objekat sa podacima iz forme (iz LiveData)
        CreateEventDTO eventDTO = buildCreateEventDTO();

        // 2. Pozivamo prvi servis - za kreiranje samog događaja
        Call<CreatedEventDTO> call = eventService.createEvent(eventDTO);
        call.enqueue(new Callback<CreatedEventDTO>() {
            @Override
            public void onResponse(Call<CreatedEventDTO> call, Response<CreatedEventDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CreatedEventDTO createdEvent = response.body();

                    // 3. AKO JE PRVI POZIV USPEO, POKREĆEMO OSTALE POZIVE
                    // Ovo je replika logike iz vaše web aplikacije
                    createActivities(createdEvent);
                    for (String email : enteredEmails){
                        createInvitations(email, createdEvent);
                    }
                    createReservations(createdEvent);

                    snackbarMessage.postValue("Event created successfully!");
                    eventCreationSuccess.postValue(true);
                } else {
                    snackbarMessage.postValue("Error creating event. Code: " + response.code());
                }
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(Call<CreatedEventDTO> call, Throwable t) {
                snackbarMessage.postValue("Network Error: " + t.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    // Helper metoda za kreiranje DTO objekta
    private CreateEventDTO buildCreateEventDTO() {
        CreateEventDTO dto = new CreateEventDTO();

        // Korišćenje settera za postavljanje vrednosti
        dto.setName(this.name.getValue());
        dto.setDescription(this.description.getValue());
        if (this.privacyType.getValue().toLowerCase().equals("closed")) {
            dto.setPrivacyType(PrivacyType.CLOSED);
        } else {
            dto.setPrivacyType(PrivacyType.OPEN);
        }
        dto.setEventType(this.eventType.getValue());

        // Bezbedno parsiranje broja učesnika
        try {
            dto.setGuestsLimit(Integer.parseInt(this.maxParticipants.getValue()));
        } catch (NumberFormatException e) {
            dto.setGuestsLimit(0); // Podrazumevana vrednost u slučaju greške
        }

        // Kreiranje i popunjavanje CreateAddressDTO
        CreateAddressDTO address = new CreateAddressDTO();
        address.setCountry(this.country.getValue());
        address.setCity(this.city.getValue());
        address.setStreet(this.street.getValue());

        // Bezbedno parsiranje broja ulice
        try {
            address.setNumber(Integer.parseInt(this.number.getValue()));
        } catch (NumberFormatException e) {
            address.setNumber(0); // Podrazumevana vrednost
        }

        // Kreiranje i postavljanje lokacije za Novi Sad
        // Koordinate Novog Sada: Latitude: 45.2671, Longitude: 19.8335
        CreateLocationDTO noviSadLocation = new CreateLocationDTO(45.2671, 19.8335);
        address.setLocation(noviSadLocation);

        // Postavljanje kompletnog objekta adrese u glavni DTO
        dto.setAddress(address);

        // Postavljanje datuma
        if (this.startDate.getValue() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            String isoString = sdf.format(this.startDate.getValue().getTime());
            dto.setStarts(isoString);
        }
        if (this.endDate.getValue() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            String isoString = sdf.format(this.endDate.getValue().getTime());
            dto.setEnds(isoString);
        }

        // Postavljanje organizatora događaja (ulogovanog korisnika)
        GetEventOrganizerDTO organizer = new GetEventOrganizerDTO();
        organizer.setId(currentUser.getId());
        dto.setEventOrganizer(organizer);

        return dto;
    }

    // Placeholder metode za ostale pozive - implementirajte ih po uzoru na submitEvent
    private void createActivities(CreatedEventDTO event) {
        // Logika za prikupljanje podataka o aktivnostima i poziv ActivityService-a
        // List<Activity> activities = activities.getValue();
        // ... for petlja i poziv activityService.addActivity(...)
        System.out.println("Placeholder: Creating activities for event ID: " + event.getId());
    }

    private void createInvitations(String email, CreatedEventDTO createdEvent) {
        EventReservationDTO event = new EventReservationDTO(createdEvent.getId(), createdEvent.getName(), createdEvent.getDescription(), createdEvent.getStarts());

        CreateInvitationDTO invitation =new CreateInvitationDTO(email, event, "Come to our closed type event!" );

        Call<CreatedInvitationDTO> call = ClientUtils.invitationService.createInvitation(invitation);
        call.enqueue(new Callback<CreatedInvitationDTO>() {

            @Override
            public void onResponse(Call<CreatedInvitationDTO> call, Response<CreatedInvitationDTO> response) {
                if (response.isSuccessful()) {
                }
            }
            @Override
            public void onFailure(Call<CreatedInvitationDTO> call, Throwable t) {
                Log.i("POZIV", t.getMessage());
            }
        });
    }

    private void createReservations(CreatedEventDTO event) {
        // Logika za prikupljanje rezervisanih servisa i poziv ReservationService-a
        System.out.println("Placeholder: Creating reservations for event ID: " + event.getId());
    }

    public void clearEmailsList(){
        this.enteredEmails.clear();
    }

    public void instanciateEmailsList(){
        this.enteredEmails = new ArrayList<String>();
    }

    public void addEmailToEmailsList(String email){
        this.enteredEmails.add(email);
    }

    public ArrayList<String> getEmailsList(){
        return this.enteredEmails;
    }
}
