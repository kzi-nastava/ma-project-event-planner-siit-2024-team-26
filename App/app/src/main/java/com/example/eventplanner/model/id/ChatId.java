package com.example.eventplanner.model.id;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatId implements Parcelable {

    private Integer eventOrganizerId;
    private Integer authenticatedUserId;

    public ChatId(Integer eventOrganizerId, Integer authenticatedUserId) {
        this.eventOrganizerId = eventOrganizerId;
        this.authenticatedUserId = authenticatedUserId;
    }

    public Integer getEventOrganizerId() {
        return eventOrganizerId;
    }

    public void setEventOrganizerId(Integer eventOrganizerId) {
        this.eventOrganizerId = eventOrganizerId;
    }

    public Integer getAuthenticatedUserId() {
        return authenticatedUserId;
    }

    public void setAuthenticatedUserId(Integer authenticatedUserId) {
        this.authenticatedUserId = authenticatedUserId;
    }

    protected ChatId(Parcel in) {
        if (in.readByte() == 0) {
            eventOrganizerId = null;
        } else {
            eventOrganizerId = in.readInt();
        }
        if (in.readByte() == 0) {
            authenticatedUserId = null;
        } else {
            authenticatedUserId = in.readInt();
        }
    }

    public static final Parcelable.Creator<ChatId> CREATOR = new Parcelable.Creator<ChatId>() {
        @Override
        public ChatId createFromParcel(Parcel in) {
            return new ChatId(in);
        }

        @Override
        public ChatId[] newArray(int size) {
            return new ChatId[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (eventOrganizerId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(eventOrganizerId);
        }
        if (authenticatedUserId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(authenticatedUserId);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
