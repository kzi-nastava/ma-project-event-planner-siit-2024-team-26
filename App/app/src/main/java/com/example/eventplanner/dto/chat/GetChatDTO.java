package com.example.eventplanner.dto.chat;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.eventplanner.dto.authenticatedUser.ChatAuthenticatedUserDTO;
import com.example.eventplanner.model.id.ChatId;

public class GetChatDTO implements Parcelable{

    private ChatId id;
    private ChatAuthenticatedUserDTO eventOrganizer;
    private ChatAuthenticatedUserDTO authenticatedUser;
    private boolean user_1_blocked;
    private boolean user_2_blocked;

    public GetChatDTO(){super();}

    public ChatId getId() {
        return id;
    }

    public void setId(ChatId id) {
        this.id = id;
    }

    public ChatAuthenticatedUserDTO getEventOrganizer() {
        return eventOrganizer;
    }

    public void setEventOrganizer(ChatAuthenticatedUserDTO eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
    }

    public ChatAuthenticatedUserDTO getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void setAuthenticatedUser(ChatAuthenticatedUserDTO authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public boolean isUser_1_blocked() {
        return user_1_blocked;
    }

    public void setUser_1_blocked(boolean user_1_blocked) {
        this.user_1_blocked = user_1_blocked;
    }

    public boolean isUser_2_blocked() {
        return user_2_blocked;
    }

    public void setUser_2_blocked(boolean user_2_blocked) {
        this.user_2_blocked = user_2_blocked;
    }


    protected GetChatDTO(Parcel in) {
        id = in.readParcelable(ChatId.class.getClassLoader());
        eventOrganizer = in.readParcelable(ChatAuthenticatedUserDTO.class.getClassLoader());
        authenticatedUser = in.readParcelable(ChatAuthenticatedUserDTO.class.getClassLoader());
        user_1_blocked = in.readByte() != 0;
        user_2_blocked = in.readByte() != 0;
    }

    public static final Parcelable.Creator<GetChatDTO> CREATOR = new Parcelable.Creator<GetChatDTO>() {
        @Override
        public GetChatDTO createFromParcel(Parcel in) {
            return new GetChatDTO(in);
        }

        @Override
        public GetChatDTO[] newArray(int size) {
            return new GetChatDTO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(id, flags);
        dest.writeParcelable(eventOrganizer, flags);
        dest.writeParcelable(authenticatedUser, flags);
        dest.writeByte((byte) (user_1_blocked ? 1 : 0));
        dest.writeByte((byte) (user_2_blocked ? 1 : 0));
    }
}
