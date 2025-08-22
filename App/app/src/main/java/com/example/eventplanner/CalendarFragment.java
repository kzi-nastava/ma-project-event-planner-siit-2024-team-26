package com.example.eventplanner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.eventplanner.adapters.CalendarEventsAdapter;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.clients.service.AuthenticatedUserService;
import com.example.eventplanner.clients.service.EventService;
import com.example.eventplanner.databinding.FragmentCalendarBinding;
import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.example.eventplanner.dto.chat.GetChatDTO;
import com.example.eventplanner.dto.event.GetEventDTO;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.details.EventDetailsFragment;
import com.example.eventplanner.fragments.details.UserDetailsFragment;
import com.example.eventplanner.utils.MonthViewContainer;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.CalendarMonth;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder;
import com.kizitonwose.calendar.view.ViewContainer;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarFragment extends Fragment {

    private FragmentCalendarBinding binding;
    private GetAuthenticatedUserDTO user;
    private AuthenticatedUserService userService;
    private EventService eventService;
    private List<GetEventDTO> goingToEvents = new ArrayList<>();
    private List<GetEventDTO> organizedEvents = new ArrayList<>();
    private String reason;
    private GetChatDTO chat;

    // >>> dodato
    private CalendarEventsAdapter eventsAdapter;
    // <<<

    public static CalendarFragment newInstance(GetAuthenticatedUserDTO user) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putParcelable("currentUser", user);
        fragment.user = user;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("CalendarFragment", "onCreateView called");

        binding = FragmentCalendarBinding.inflate(inflater, container, false);

        binding.textView8.setTextColor(getResources().getColor(R.color.blackreversable));

        userService = ClientUtils.authenticatedUserService;
        eventService = ClientUtils.eventService;

        // >>> inicijalizacija RecyclerView + adapter
        binding.eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventsAdapter = new CalendarEventsAdapter(new ArrayList<>(), event -> {
            // Klik na event iz liste otvara fragment sa detaljima
            Bundle bundle = new Bundle();
            bundle.putLong("eventId", event.getId());
            Call<GetChatDTO> call = ClientUtils.chatService.getChat(user.getId(), event.getEventOrganizer().getId());
            call.enqueue(new Callback<GetChatDTO>() {

                @Override
                public void onResponse(Call<GetChatDTO> call, Response<GetChatDTO> response) {
                    if (response.isSuccessful()) {
                        chat = response.body();
                        if (user.getId() == chat.getEventOrganizer().getId()){
                            if (chat.isUser_1_blocked()){
                                reason = "You can't see more information because organizer " + event.getEventOrganizer().getFirstName() +
                                        " " + event.getEventOrganizer().getLastName() + " has blocked you!";
                                showBlockedDialog();
                            } else if (chat.isUser_2_blocked()){
                                reason = "You can't see more information because organizer " + event.getEventOrganizer().getFirstName() +
                                        " " + event.getEventOrganizer().getLastName() + " is blocked!";
                                showBlockedDialog();
                            }else{
                                FragmentTransition.to(EventDetailsFragment.newInstance(event.getId(), chat, user), getActivity(), true, R.id.mainScreenFragment);
                            }

                        }else{ // If user is Authenticated user in chat table
                            if (chat.isUser_2_blocked()){
                                reason = "You can't see more information because organizer " + event.getEventOrganizer().getFirstName() +
                                        " " + event.getEventOrganizer().getLastName() + " has blocked you!";
                                showBlockedDialog();
                            } else if (chat.isUser_1_blocked()) {
                                reason = "You can't see more information because organizer " + event.getEventOrganizer().getFirstName() +
                                        " " + event.getEventOrganizer().getLastName() + " is blocked!";
                                showBlockedDialog();
                            } else{
                                FragmentTransition.to(EventDetailsFragment.newInstance(event.getId(), chat, user), getActivity(), true, R.id.mainScreenFragment);
                            }
                        }
                    }else{
                        if (response.code() == 404){
                            FragmentTransition.to(EventDetailsFragment.newInstance(event.getId(), chat, user), getActivity(), true, R.id.mainScreenFragment);
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetChatDTO> call, Throwable t) {
                    Log.i("ChatEventAdapter", t.getMessage());
                }
            });
        });
        binding.eventsRecyclerView.setAdapter(eventsAdapter);
        // <<<

        if (user == null) {
            Log.e("CalendarFragment", "User object is NULL - Calendar will still render empty.");
            showEventsInCalendar();
        } else {
            loadUserAndEvents();
        }

        return binding.getRoot();
    }

    private void showBlockedDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Access denied")
                .setMessage(reason);

        builder.setPositiveButton("I Understand", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void loadUserAndEvents() {
        Log.d("CalendarFragment", "Loading user events...");

        if (user.getGoingToEvents() != null) {
            goingToEvents = user.getGoingToEvents();
            Log.d("CalendarFragment", "Going to events count: " + goingToEvents.size());
        }

        eventService.getEventsByOrganizer(user.getId()).enqueue(new Callback<List<GetEventDTO>>() {
            @Override
            public void onResponse(Call<List<GetEventDTO>> call, Response<List<GetEventDTO>> response2) {
                if (response2.isSuccessful() && response2.body() != null) {
                    organizedEvents = response2.body();
                    Log.d("CalendarFragment", "Organized events count: " + organizedEvents.size());
                }
                showEventsInCalendar();
            }

            @Override
            public void onFailure(Call<List<GetEventDTO>> call, Throwable t) {
                Log.e("CalendarFragment", "Error loading organized events", t);
                showEventsInCalendar();
            }
        });
    }

    private void showEventsInCalendar() {
        YearMonth currentMonth = YearMonth.now();
        YearMonth startMonth = currentMonth.minusMonths(12);
        YearMonth endMonth = currentMonth.plusMonths(12);
        DayOfWeek firstDayOfWeek = DayOfWeek.MONDAY;

        binding.calendarView.setup(startMonth, endMonth, firstDayOfWeek);
        binding.calendarView.scrollToMonth(currentMonth);

        binding.calendarView.setMonthHeaderBinder(new MonthHeaderFooterBinder<MonthViewContainer>() {
            @Override
            public MonthViewContainer create(@NonNull View view) {
                return new MonthViewContainer(view);
            }

            @Override
            public void bind(@NonNull MonthViewContainer container, @NonNull CalendarMonth month) {
                TextView monthYearText = container.getView().findViewById(R.id.monthHeaderText);
                String monthName = month.getYearMonth().getMonth().toString().substring(0, 1).toUpperCase() +
                        month.getYearMonth().getMonth().toString().substring(1).toLowerCase();
                monthYearText.setText(monthName + " " + month.getYearMonth().getYear());
                monthYearText.setTextColor(getResources().getColor(R.color.blackreversable));
            }
        });

        binding.calendarView.setDayBinder(new MonthDayBinder<DayViewContainer>() {
            @NonNull
            @Override
            public DayViewContainer create(@NonNull View view) {
                return new DayViewContainer(view);
            }

            @Override
            public void bind(@NonNull DayViewContainer container, @NonNull CalendarDay day) {
                container.textView.setText(String.valueOf(day.getDate().getDayOfMonth()));
                container.textView.setTextColor(getResources().getColor(R.color.blackreversable));

                if (hasOrganizedEventOnDate(day.getDate()) && hasGoingEventOnDate(day.getDate())) {
                    container.textView.setBackgroundColor(getResources().getColor(R.color.secondary));
                } else if (hasOrganizedEventOnDate(day.getDate())) {
                    container.textView.setBackgroundColor(getResources().getColor(R.color.accent));
                } else if (hasGoingEventOnDate(day.getDate())) {
                    container.textView.setBackgroundColor(getResources().getColor(R.color.primary));
                } else {
                    container.textView.setBackgroundColor(Color.TRANSPARENT);
                }

                container.textView.setOnClickListener(v -> {
                    LocalDate clickedDate = day.getDate();
                    List<GetEventDTO> dayEvents = new ArrayList<>();

                    for (GetEventDTO e : organizedEvents) {
                        LocalDate start = toLocalDate(e.getStarts());
                        LocalDate end = toLocalDate(e.getEnds());
                        if ((clickedDate.isEqual(start) || clickedDate.isAfter(start)) &&
                                (clickedDate.isEqual(end) || clickedDate.isBefore(end))) {
                            dayEvents.add(e);
                        }
                    }

                    for (GetEventDTO e : goingToEvents) {
                        LocalDate start = toLocalDate(e.getStarts());
                        LocalDate end = toLocalDate(e.getEnds());
                        if ((clickedDate.isEqual(start) || clickedDate.isAfter(start)) &&
                                (clickedDate.isEqual(end) || clickedDate.isBefore(end))) {
                            dayEvents.add(e);
                        }
                    }

                    // >>> sad adapter nije null
                    eventsAdapter.updateData(dayEvents);
                });
            }
        });
    }

    static class DayViewContainer extends ViewContainer {
        TextView textView;
        LocalDate date;

        DayViewContainer(View view) {
            super(view);
            textView = view.findViewById(R.id.calendarDayText);
        }
    }

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private LocalDate toLocalDate(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER).toLocalDate();
    }

    private boolean hasOrganizedEventOnDate(LocalDate date) {
        for (GetEventDTO event : organizedEvents) {
            LocalDate start = toLocalDate(event.getStarts());
            LocalDate end = toLocalDate(event.getEnds());
            if ((date.isEqual(start) || date.isAfter(start)) &&
                    (date.isEqual(end) || date.isBefore(end))) {
                return true;
            }
        }
        return false;
    }

    private boolean hasGoingEventOnDate(LocalDate date) {
        for (GetEventDTO event : goingToEvents) {
            LocalDate start = toLocalDate(event.getStarts());
            LocalDate end = toLocalDate(event.getEnds());
            if ((date.isEqual(start) || date.isAfter(start)) &&
                    (date.isEqual(end) || date.isBefore(end))) {
                return true;
            }
        }
        return false;
    }
}
