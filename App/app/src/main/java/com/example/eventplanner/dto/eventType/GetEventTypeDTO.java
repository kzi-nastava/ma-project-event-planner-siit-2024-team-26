package com.example.eventplanner.dto.eventType;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.eventplanner.dto.category.GetCategoryDTO;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetEventTypeDTO implements Parcelable {

    private Integer id;
    private String name;
    private String description;
    @SerializedName("active")
    private boolean isActive;
    private List<GetCategoryDTO> recommendedCategories;

    public GetEventTypeDTO() {
        super();
    }

    protected GetEventTypeDTO(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        description = in.readString();
        isActive = in.readByte() != 0;

        recommendedCategories = new ArrayList<>();
        in.readTypedList(recommendedCategories, GetCategoryDTO.CREATOR);
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
        dest.writeByte((byte) (isActive ? 1 : 0));

        dest.writeTypedList(recommendedCategories);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GetEventTypeDTO> CREATOR = new Creator<GetEventTypeDTO>() {
        @Override
        public GetEventTypeDTO createFromParcel(Parcel in) {
            return new GetEventTypeDTO(in);
        }

        @Override
        public GetEventTypeDTO[] newArray(int size) {
            return new GetEventTypeDTO[size];
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<GetCategoryDTO> getRecommendedCategories() {
        return recommendedCategories;
    }

    public void setRecommendedCategories(List<GetCategoryDTO> recommendedCategories) {
        this.recommendedCategories = recommendedCategories;
    }
}
