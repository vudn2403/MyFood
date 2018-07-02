package com.vudn.myfood.view.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vudn.myfood.R;
import com.vudn.myfood.base.Key;
import com.vudn.myfood.view.dialog.CapNhatThongTinDialog;
import com.vudn.myfood.view.dialog.LienHeNhaPhatTrienDialog;
import com.vudn.myfood.view.dialog.ThayDoiMatKhauDialog;
import com.vudn.myfood.view.user.UserActivity;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ProfileFragment";
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 10001;
    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 10002;
    private static final int BACKGROUND_PHOTO_REQUEST = 1001;
    private static final int AVATAR_PHOTO_REQUEST = 1002;

    private ImageView imgAnhDaiDien;
    private TextView txtTenNguoiDung;
    private TextView txtSoDienThoai;
    private TextView btnCapNhatAnh;
    private TextView btnCapNhatThongTin;
    private TextView btnDoiMatKhau;
    private TextView btnLienHe;
    private TextView btnDangXuat;
    private LinearLayout lnlProfile;
    private LinearLayout btnSignIn;
    private ProgressDialog progressDialog;

    private SharedPreferences sharedPreferencesToaDo;
    private SharedPreferences sharedPreferencesDangNhap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initializeComponents(view);
        registerListeners();
    }

    private void initializeComponents(View view) {
        imgAnhDaiDien = view.findViewById(R.id.img_anh_dai_dien);
        txtTenNguoiDung = view.findViewById(R.id.txt_ten_nguoi_dung);
        txtSoDienThoai = view.findViewById(R.id.txt_so_dien_thoai);
        btnCapNhatAnh = view.findViewById(R.id.btn_cap_nhat_anh_dai_dien);
        btnCapNhatThongTin = view.findViewById(R.id.btn_cap_nhat_thong_tin);
        btnDoiMatKhau = view.findViewById(R.id.btn_thay_doi_mat_khau);
        btnLienHe = view.findViewById(R.id.btn_lien_he);
        btnDangXuat = view.findViewById(R.id.btn_dang_xuat);

        lnlProfile = view.findViewById(R.id.lnl_profile);
        btnSignIn = view.findViewById(R.id.btn_sign_in);

        sharedPreferencesToaDo = getActivity().getSharedPreferences("toado", Context.MODE_PRIVATE);
        sharedPreferencesDangNhap = getActivity().getSharedPreferences("luudangnhap", Context.MODE_PRIVATE);
        Location vitrihientai = new Location("");
        vitrihientai.setLatitude(Double.parseDouble(sharedPreferencesToaDo.getString("latitude", "0")));
        vitrihientai.setLongitude(Double.parseDouble(sharedPreferencesToaDo.getString("longitude", "0")));
        updateUI();
        progressDialog = new ProgressDialog(getContext());
    }

    private void registerListeners() {
        btnCapNhatAnh.setOnClickListener(this);
        btnCapNhatThongTin.setOnClickListener(this);
        btnDoiMatKhau.setOnClickListener(this);
        btnLienHe.setOnClickListener(this);
        btnDangXuat.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
    }


    public void updateUI() {
        if (sharedPreferencesDangNhap.getBoolean("islogin", false)) {
            lnlProfile.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.GONE);
            Glide.with(getContext()).load(sharedPreferencesDangNhap.getString("hinhanh", "")).into(imgAnhDaiDien);
            txtTenNguoiDung.setText(sharedPreferencesDangNhap.getString("hoten", ""));
            txtSoDienThoai.setText(sharedPreferencesDangNhap.getString("sodienthoai", ""));
        } else {
            lnlProfile.setVisibility(View.GONE);
            btnSignIn.setVisibility(View.VISIBLE);
        }

        /*try {
            for (Address address : mAddressList = geocoder.getFromLocation(vitrihientai.getLatitude(), vitrihientai.getLongitude(), 1)) {
                Log.d(TAG, "onConnected: Address: " + address.getAddressLine(0));
                String Address = address.getAddressLine(0).replace(", Vietnam", "");
                edtAddress.setText(Address);
            }
        } catch (IOException e) {
            Log.d(TAG, "onConnected: " + e.toString());
        }*/
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cap_nhat_anh_dai_dien:
                selectAvatar();
                break;

            case R.id.btn_cap_nhat_thong_tin:
                capNhatThongTin();
                break;

            case R.id.btn_thay_doi_mat_khau:
                thayDoiMatKhau();
                break;

            case R.id.btn_lien_he:
                lienHe();
                break;

            case R.id.btn_dang_xuat:
                dangXuat();
                break;

            case R.id.btn_sign_in:
                signIn();
                break;

            default:
                break;

        }
    }

    private void signIn() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).signIn();
        }
    }

    private void thayDoiMatKhau() {
        ThayDoiMatKhauDialog thayDoiMatKhauDialog = new ThayDoiMatKhauDialog(getContext(), sharedPreferencesDangNhap.getString("email", ""));
        thayDoiMatKhauDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        thayDoiMatKhauDialog.show();
    }

    private void lienHe() {
        LienHeNhaPhatTrienDialog lienHeNhaPhatTrienDialog = new LienHeNhaPhatTrienDialog(getContext());
        lienHeNhaPhatTrienDialog.show();
    }

    private void dangXuat() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).signOut();
        }
    }

    private void capNhatThongTin() {
        CapNhatThongTinDialog capNhatThongTinDialog = new CapNhatThongTinDialog(getContext(), sharedPreferencesDangNhap.getString("mauser", ""));
        capNhatThongTinDialog.show();
    }

    private void selectAvatar() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, AVATAR_PHOTO_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AVATAR_PHOTO_REQUEST && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "onActivityResult: có intent trả về");
            uploadAnhDaiDien(data);
        }
        /*if (requestCode == Key.RC_SIGN_IN && resultCode == Activity.RESULT_OK){
            updateUI();
        }*/
    }

    private void checkLogin() {
        if (sharedPreferencesDangNhap.getBoolean("islogin", false)) {
            // ...
        } else {
            final android.support.v7.app.AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new android.support.v7.app.AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Light_Dialog_NoActionBar);
            } else {
                builder = new android.support.v7.app.AlertDialog.Builder(getContext());
            }
            builder.setTitle("Vui lòng đăng nhập để thực hiện chức năng này")
                    .setCancelable(false)
                    .setPositiveButton("Đồng ý".toUpperCase(), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent iDangNhap = new Intent(getContext(), UserActivity.class);
                            startActivityForResult(iDangNhap, Key.RC_SIGN_IN);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Hủy".toUpperCase(), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    private void uploadAnhDaiDien(Intent data) {
        Uri uriBitmap = data.getData();
        Glide.with(getContext()).load(uriBitmap).into(imgAnhDaiDien);
        final android.support.v7.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new android.support.v7.app.AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Light_Dialog_NoActionBar);
        } else {
            builder = new android.support.v7.app.AlertDialog.Builder(getContext());
        }
        builder.setTitle("Đồng ý cập nhật ảnh đại diện")
                .setCancelable(false)
                .setPositiveButton("Đồng ý".toUpperCase(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.setMessage(getString(R.string.dangxuly));
                        progressDialog.setCancelable(false);
                        progressDialog.setIndeterminate(true);
                        progressDialog.show();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Hủy".toUpperCase(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        updateUI();
                        return;
                    }
                })
                .show();
        final StorageReference storageAvatar = FirebaseStorage.getInstance().getReference().child("thanhvien/" + uriBitmap.getLastPathSegment());
        UploadTask uploadTask = storageAvatar.putFile(uriBitmap);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageAvatar.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        updateAnhDaiDien(uri);
                    }
                });
            }
        });
        /*Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return storageAvatar.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri dowloadUri = task.getResult();
                    updateAnhDaiDien(dowloadUri);
                } else {
                    // ...
                }
            }
        });*/
    }

    private void updateAnhDaiDien(final Uri dowloadUri) {
        FirebaseDatabase.getInstance().getReference()
                .child("thanhviens")
                .child(sharedPreferencesDangNhap.getString("mauser", ""))
                .child("hinhanh").setValue(dowloadUri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Cập nhật ảnh đại diện thành công", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = sharedPreferencesDangNhap.edit();
                editor.putString("hinhanh", dowloadUri.toString());
                editor.commit();
                if (getActivity() instanceof MainActivity){
                    ((MainActivity) getActivity()).updateUI();
                }
            }
        });
    }
}
