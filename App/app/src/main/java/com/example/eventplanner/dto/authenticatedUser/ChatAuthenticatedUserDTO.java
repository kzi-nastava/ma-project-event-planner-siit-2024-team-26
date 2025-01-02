package com.example.eventplanner.dto.authenticatedUser;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatAuthenticatedUserDTO implements Parcelable {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String image;

    public ChatAuthenticatedUserDTO(){super();}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Constructor for parceling
    protected ChatAuthenticatedUserDTO(Parcel in) {
        id = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        image = in.readString();
        email = in.readString();
    }

    @Override
    public int describeContents() {
        return 0; // Return 0 since we don't have any special object types
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id != null ? id : -1); // Write id, use -1 for null
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(image);
        dest.writeString(email);
    }

    public static final Parcelable.Creator<ChatAuthenticatedUserDTO> CREATOR = new Parcelable.Creator<ChatAuthenticatedUserDTO>() {
        @Override
        public ChatAuthenticatedUserDTO createFromParcel(Parcel in) {
            return new ChatAuthenticatedUserDTO(in);
        }

        @Override
        public ChatAuthenticatedUserDTO[] newArray(int size) {
            return new ChatAuthenticatedUserDTO[size];
        }
    };
}
