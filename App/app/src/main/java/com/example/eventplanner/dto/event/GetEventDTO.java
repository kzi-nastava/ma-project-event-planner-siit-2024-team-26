package com.example.eventplanner.dto.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.eventplanner.dto.address.GetAddressDTO;
import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.example.eventplanner.dto.eventType.GetEventTypeDTO;
import com.example.eventplanner.model.PrivacyType;

public class GetEventDTO implements Parcelable {
    private Integer id;
    private String name;
    private String description;
    private Integer guestsLimit;
    private PrivacyType privacyType;
    private String starts;
    private String ends;
    private Integer gradeSum;
    private Integer gradeCount;
    private Integer numVisitors;
    private GetAuthenticatedUserDTO eventOrganizer;
    private GetAddressDTO address;
    private GetEventTypeDTO eventType;

    public GetEventDTO() {
        super();
    }

    protected GetEventDTO(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            guestsLimit = null;
        } else {
            guestsLimit = in.readInt();
        }
        // PrivacyType - enum, čuvamo kao String i vraćamo nazad
        String privacyTypeName = in.readString();
        privacyType = privacyTypeName == null ? null : PrivacyType.valueOf(privacyTypeName);

        starts = in.readString();
        ends = in.readString();

        if (in.readByte() == 0) {
            gradeSum = null;
        } else {
            gradeSum = in.readInt();
        }
        if (in.readByte() == 0) {
            gradeCount = null;
        } else {
            gradeCount = in.readInt();
        }
        if (in.readByte() == 0) {
            numVisitors = null;
        } else {
            numVisitors = in.readInt();
        }

        eventOrganizer = in.readParcelable(GetAuthenticatedUserDTO.class.getClassLoader());
        address = in.readParcelable(GetAddressDTO.class.getClassLoader());
        eventType = in.readParcelable(GetEventTypeDTO.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeString(description);
        if (guestsLimit == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(guestsLimit);
        }
        dest.writeString(privacyType == null ? null : privacyType.name());

        dest.writeString(starts);
        dest.writeString(ends);

        if (gradeSum == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(gradeSum);
        }
        if (gradeCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(gradeCount);
        }
        if (numVisitors == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(numVisitors);
        }

        dest.writeParcelable(eventOrganizer, flags);
        dest.writeParcelable(address, flags);
        dest.writeParcelable(eventType, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GetEventDTO> CREATOR = new Creator<GetEventDTO>() {
        @Override
        public GetEventDTO createFromParcel(Parcel in) {
            return new GetEventDTO(in);
        }

        @Override
        public GetEventDTO[] newArray(int size) {
            return new GetEventDTO[size];
        }
    };

    // --- getters i setters ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getGuestsLimit() {
        return guestsLimit;
    }

    public void setGuestsLimit(Integer guestsLimit) {
        this.guestsLimit = guestsLimit;
    }

    public PrivacyType getPrivacyType() {
        return privacyType;
    }

    public void setPrivacyType(PrivacyType privacyType) {
        this.privacyType = privacyType;
    }

    public String getStarts() {
        return starts;
    }

    public void setStarts(String starts) {
        this.starts = starts;
    }

    public String getEnds() {
        return ends;
    }

    public void setEnds(String ends) {
        this.ends = ends;
    }

    public Integer getGradeSum() {
        return gradeSum;
    }

    public void setGradeSum(Integer gradeSum) {
        this.gradeSum = gradeSum;
    }

    public Integer getGradeCount() {
        return gradeCount;
    }

    public void setGradeCount(Integer gradeCount) {
        this.gradeCount = gradeCount;
    }

    public Integer getNumVisitors() {
        return numVisitors;
    }

    public void setNumVisitors(Integer numVisitors) {
        this.numVisitors = numVisitors;
    }

    public GetAddressDTO getAddress() {
        return address;
    }

    public void setAddress(GetAddressDTO address) {
        this.address = address;
    }

    public GetEventTypeDTO getEventType() {
        return eventType;
    }

    public void setEventType(GetEventTypeDTO eventType) {
        this.eventType = eventType;
    }

    public GetAuthenticatedUserDTO getEventOrganizer() {
        return eventOrganizer;
    }

    public void setEventOrganizer(GetAuthenticatedUserDTO eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
    }
}
