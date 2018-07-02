package com.vudn.myfood.model.user;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vudn.myfood.presenter.user.UserPresenter;

public class MyUser implements Parcelable {
    private String name;
    private String id;
    private String phone;
    private String address;
    private String photo;

    private DatabaseReference nodeUser;
    private UserPresenter userPresenter;

    public MyUser(UserPresenter userPresenter) {
        nodeUser = FirebaseDatabase.getInstance().getReference().child("users");
        this.userPresenter = userPresenter;
    }

    public MyUser() {
        nodeUser = FirebaseDatabase.getInstance().getReference().child("users");
    }

    protected MyUser(Parcel in) {
        name = in.readString();
        id = in.readString();
        phone = in.readString();
        address = in.readString();
        photo = in.readString();
    }

    public static final Creator<MyUser> CREATOR = new Creator<MyUser>() {
        @Override
        public MyUser createFromParcel(Parcel in) {
            return new MyUser(in);
        }

        @Override
        public MyUser[] newArray(int size) {
            return new MyUser[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(phone);
        dest.writeString(address);
        dest.writeString(photo);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public static Creator<MyUser> getCREATOR() {
        return CREATOR;
    }

    public void addUser(final FirebaseUser user){
        MyUser myUser = new MyUser();
        myUser.setName("Diners");
        myUser.setPhoto("user.png");
        myUser.setPhone("");
        myUser.setAddress("");
        nodeUser.child(user.getUid()).setValue(myUser);
        nodeUser.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MyUser myUser = dataSnapshot.getValue(MyUser.class);
                myUser.setId(dataSnapshot.getKey());
                //userPresenter.signUp(myUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //userPresenter.onAddUserFailure(databaseError.getMessage());
            }
        });{

        }
    }

    public void updateUser(final FirebaseUser user){
        nodeUser.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(MyUser.class) == null){
                    MyUser myUser = new MyUser();
                    myUser.setName(user.getDisplayName());
                    myUser.setPhoto(user.getPhotoUrl().toString());
                    myUser.setPhone(user.getPhoneNumber());
                    myUser.setAddress("");
                    nodeUser.child(dataSnapshot.getKey()).setValue(myUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
