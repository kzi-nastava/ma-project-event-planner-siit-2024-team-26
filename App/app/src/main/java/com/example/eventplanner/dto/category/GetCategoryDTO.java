package com.example.eventplanner.dto.category;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.eventplanner.model.State;

public class GetCategoryDTO implements Parcelable {

    private Integer id;
    private String name;
    private String description;
    private State state;
    private boolean isDeleted;

    public GetCategoryDTO() {
        super();
    }

    public GetCategoryDTO(Integer id, String name, String description, State state, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.state = state;
        this.isDeleted = isDeleted;
    }

    protected GetCategoryDTO(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        description = in.readString();

        String stateName = in.readString();
        state = stateName == null ? null : State.valueOf(stateName);

        isDeleted = in.readByte() != 0;
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
        dest.writeString(state == null ? null : state.name());
        dest.writeByte((byte) (isDeleted ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GetCategoryDTO> CREATOR = new Creator<GetCategoryDTO>() {
        @Override
        public GetCategoryDTO createFromParcel(Parcel in) {
            return new GetCategoryDTO(in);
        }

        @Override
        public GetCategoryDTO[] newArray(int size) {
            return new GetCategoryDTO[size];
        }
    };

    // Getteri i setteri

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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
