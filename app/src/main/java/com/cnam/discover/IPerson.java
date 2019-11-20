package com.cnam.discover;
import android.os.Parcelable;

public interface IPerson extends Parcelable, Comparable<IPerson> {
    String getFirstName();
    String getProfilePicUrl();
    String getDescription();
    String getLastName();
    String getAge();
    String getGender();
}
