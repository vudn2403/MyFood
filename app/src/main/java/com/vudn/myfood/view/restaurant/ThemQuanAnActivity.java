package com.vudn.myfood.view.restaurant;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vudn.myfood.R;
import com.vudn.myfood.base.BaseActivity;
import com.vudn.myfood.model.address.DiaChiModel;
import com.vudn.myfood.model.menu.MonAnModel;
import com.vudn.myfood.model.restaurant.QuanAnModel;
import com.vudn.myfood.model.restaurant.ThemThucDonModel;
import com.vudn.myfood.model.menu.ThucDonModel;
import com.vudn.myfood.model.util.TienIchModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ThemQuanAnActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    final int RESULT_IMG1 = 111;
    final int RESULT_IMG2 = 112;
    final int RESULT_IMG3 = 113;
    final int RESULT_IMG4 = 114;
    final int RESULT_IMG5 = 115;
    final int RESULT_IMG6 = 116;
    final int RESULT_IMGTHUCDON = 117;
    final int RESULT_VIDEO = 200;

    Button btnGioMoCua, btnGioDongCua, btnThemQuanAn;
    Spinner spinnerKhuVuc, spinnerTheLoai;
    LinearLayout khungTienIch, khungChuaThucDon;
    String tenQuanAn, diaChi, soDT, gioiThieu, chiDuong, gioMoCua, gioDongCua, khuvuc, theloai;
    RadioGroup rdgTrangThai;
    EditText edTenQuanAn, edtDiaChi, edtSoDienThoai, edtGioiThieu, edtWebsite, edtChiDuong, edGiaToiDa, edGiaThoiThieu;
    long giaToiDa, giaToiThieu;
    List<ThucDonModel> thucDonModelList;
    List<String> selectedTienIchList;
    List<String> khuVucList, thucDonList, theLoaiList;
    List<String> chiNhanhList;
    List<ThemThucDonModel> themThucDonModelList;
    List<Uri> hinhThucDon;
    List<Uri> hinhQuanAn;
    Uri videoSelected;
    private ProgressDialog progressDialog;

    ArrayAdapter<String> adapterKhuVuc;
    ArrayAdapter<String> adapterTheLoai;
    ImageView imgTam, imgHinhQuan1, imgHinhQuan2, imgHinhQuan3, imgHinhQuan4, imgHinhQuan5, imgHinhQuan6, imgVideo;
    VideoView videoView;

    String maQuanAn;
    private MonAnModel monAnModel;
    private QuanAnModel quanAnModel;

    private DatabaseReference nodeRoot;
    private DatabaseReference nodeQuanAn;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.layout_themquanan;
    }

    @Override
    protected void initializeComponents() {
        btnGioDongCua = (Button) findViewById(R.id.btnGioDongCua);
        btnGioMoCua = (Button) findViewById(R.id.btnGioMoCua);
        spinnerKhuVuc = (Spinner) findViewById(R.id.spinnerKhuVuc);
        spinnerTheLoai = findViewById(R.id.spinnerTheLoai);
        khungTienIch = (LinearLayout) findViewById(R.id.khungTienTich);
        khungChuaThucDon = (LinearLayout) findViewById(R.id.khungChuaThucDon);
        imgHinhQuan1 = (ImageView) findViewById(R.id.imgHinhQuan1);
        imgHinhQuan2 = (ImageView) findViewById(R.id.imgHinhQuan2);
        imgHinhQuan3 = (ImageView) findViewById(R.id.imgHinhQuan3);
        imgHinhQuan4 = (ImageView) findViewById(R.id.imgHinhQuan4);
        imgHinhQuan5 = (ImageView) findViewById(R.id.imgHinhQuan5);
        imgHinhQuan6 = (ImageView) findViewById(R.id.imgHinhQuan6);
        imgVideo = (ImageView) findViewById(R.id.imgVideo);
        videoView = (VideoView) findViewById(R.id.videoView);
        btnThemQuanAn = (Button) findViewById(R.id.btnThemQuanAn);
        rdgTrangThai = (RadioGroup) findViewById(R.id.rdgTrangThai);
        edGiaThoiThieu = (EditText) findViewById(R.id.edGiaToiThieu);
        edGiaToiDa = (EditText) findViewById(R.id.edGiaToiDa);
        edTenQuanAn = (EditText) findViewById(R.id.edTenQuan);
        edtDiaChi = findViewById(R.id.edt_dia_chi);
        edtSoDienThoai = findViewById(R.id.edt_so_dien_thoai);

        quanAnModel = new QuanAnModel();

        thucDonModelList = new ArrayList<>();
        khuVucList = new ArrayList<>();
        theLoaiList = new ArrayList<>();
        thucDonList = new ArrayList<>();
        selectedTienIchList = new ArrayList<>();
        themThucDonModelList = new ArrayList<>();
        hinhThucDon = new ArrayList<>();
        hinhQuanAn = new ArrayList<>();

        adapterKhuVuc = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, khuVucList);
        spinnerKhuVuc.setAdapter(adapterKhuVuc);
        adapterKhuVuc.notifyDataSetChanged();

        adapterTheLoai = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, theLoaiList);
        spinnerTheLoai.setAdapter(adapterTheLoai);
        adapterTheLoai.notifyDataSetChanged();

        CloneThucDon();

        LayDanhSachKhuVuc();
        LayDanhSachTheLoai();
        LayDanhSachTienIch();

        nodeRoot = FirebaseDatabase.getInstance().getReference();
        nodeQuanAn = nodeRoot.child("quanans");

        progressDialog = new ProgressDialog(this);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        uploadHinhQuanAn();
                        break;

                    default:
                        break;
                }
            }
        };
    }

    @Override
    protected void registerListeners() {
        btnGioMoCua.setOnClickListener(this);
        btnGioDongCua.setOnClickListener(this);
        spinnerKhuVuc.setOnItemSelectedListener(this);
        imgHinhQuan1.setOnClickListener(this);
        imgHinhQuan2.setOnClickListener(this);
        imgHinhQuan3.setOnClickListener(this);
        imgHinhQuan4.setOnClickListener(this);
        imgHinhQuan5.setOnClickListener(this);
        imgHinhQuan6.setOnClickListener(this);
        imgVideo.setOnClickListener(this);
        btnThemQuanAn.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_IMG1:
                if (RESULT_OK == resultCode) {
                    Uri uri = data.getData();
                    imgHinhQuan1.setImageURI(uri);
                    hinhQuanAn.add(uri);
                }
                break;

            case RESULT_IMG2:
                if (RESULT_OK == resultCode) {
                    Uri uri = data.getData();
                    imgHinhQuan2.setImageURI(uri);
                    hinhQuanAn.add(uri);
                }
                break;

            case RESULT_IMG3:
                if (RESULT_OK == resultCode) {
                    Uri uri = data.getData();
                    imgHinhQuan3.setImageURI(uri);
                    hinhQuanAn.add(uri);
                }
                break;

            case RESULT_IMG4:
                if (RESULT_OK == resultCode) {
                    Uri uri = data.getData();
                    imgHinhQuan4.setImageURI(uri);
                    hinhQuanAn.add(uri);
                }
                break;

            case RESULT_IMG5:
                if (RESULT_OK == resultCode) {
                    Uri uri = data.getData();
                    imgHinhQuan5.setImageURI(uri);
                    hinhQuanAn.add(uri);
                }
                break;

            case RESULT_IMG6:
                if (RESULT_OK == resultCode) {
                    Uri uri = data.getData();
                    imgHinhQuan6.setImageURI(uri);
                    hinhQuanAn.add(uri);
                }
                break;

            case RESULT_IMGTHUCDON:
                //Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                if (RESULT_OK == resultCode) {
                    Uri uri = data.getData();
                    imgTam.setImageURI(uri);
                    imgTam.setTag(uri);
                    hinhThucDon.add(uri);
                }
                break;

            case RESULT_VIDEO:
                if (RESULT_OK == resultCode) {
                    imgVideo.setVisibility(View.GONE);
                    Uri uri = data.getData();
                    videoSelected = uri;
                    videoView.setVideoURI(uri);
                    videoView.start();
                }
                break;

            default:
                break;
        }

    }

    private void CloneThucDon() {
        View view = LayoutInflater.from(ThemQuanAnActivity.this).inflate(R.layout.layout_clone_thucdon, null);
        final Spinner spinnerThucDon = (Spinner) view.findViewById(R.id.spinnerThucDon);
        Button btnThemThucDOn = (Button) view.findViewById(R.id.btnThemThucDon);
        final EditText edTenMon = (EditText) view.findViewById(R.id.edTenMon);
        final EditText edGiaTien = (EditText) view.findViewById(R.id.edGiaTien);
        ImageView imageChupHinh = (ImageView) view.findViewById(R.id.imgChupHinh);
        imgTam = imageChupHinh;

        ArrayAdapter<String> adapterThucDon = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, thucDonList);
        spinnerThucDon.setAdapter(adapterThucDon);
        adapterThucDon.notifyDataSetChanged();
        if (thucDonModelList.size() == 0) {
            LayDanhSachThucDon(adapterThucDon);
        }

        imageChupHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, RESULT_IMGTHUCDON);
            }
        });

        btnThemThucDOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edTenMon.getText().toString();
                String gia = edGiaTien.getText().toString();
                if (imgTam.getTag() == null || ten.isEmpty() || gia.isEmpty()) {
                    Toast.makeText(ThemQuanAnActivity.this, "Vui lòng nhập dữ liệu", Toast.LENGTH_SHORT).show();
                    return;
                }
                v.setVisibility(View.GONE);
                imgTam.setTag(null);
                long thoigian = Calendar.getInstance().getTimeInMillis();
                String tenhinh = String.valueOf(thoigian) + ".jpg";

                int position = spinnerThucDon.getSelectedItemPosition();
                String maThucDon = thucDonModelList.get(position).getMathucdon();

                MonAnModel monAnModel = new MonAnModel();
                monAnModel.setTenmon(ten);
                monAnModel.setGiatien(Long.parseLong(gia));
                monAnModel.setHinhanh(tenhinh);

                ThemThucDonModel themThucDonModel = new ThemThucDonModel();
                themThucDonModel.setMathucdon(maThucDon);
                themThucDonModel.setMonAnModel(monAnModel);

                themThucDonModelList.add(themThucDonModel);
                CloneThucDon();
            }
        });

        khungChuaThucDon.addView(view);
    }

    private void CloneChiNhanh() {
        /*final View view = LayoutInflater.from(ThemQuanAnActivity.this).inflate(R.layout.layout_clone_chinhanh, null);
        ImageButton btnThemChiNhanh = (ImageButton) view.findViewById(R.id.btnThemChiNhanh);
        final ImageButton btnXoaChiNhanh = (ImageButton) view.findViewById(R.id.btnXoaChiNhanh);

        btnThemChiNhanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edTenChiNhanh = (EditText) view.findViewById(R.id.edTenChiNhanh);
                String tenChiNhanh = edTenChiNhanh.getText().toString();

                v.setVisibility(View.GONE);
                btnXoaChiNhanh.setVisibility(View.VISIBLE);
                btnXoaChiNhanh.setTag(tenChiNhanh);


                chiNhanhList.add(tenChiNhanh);

                CloneChiNhanh();
            }
        });

        btnXoaChiNhanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenChiNhanh = v.getTag().toString();
                chiNhanhList.remove(tenChiNhanh);
                khungChuaChiNhanh.removeView(view);

            }
        });
        khungChuaChiNhanh.addView(view);*/
    }

    private void LayDanhSachKhuVuc() {
        FirebaseDatabase.getInstance().getReference().child("khuvucs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String tenKhuVuc = snapshot.getValue(String.class);
                    khuVucList.add(tenKhuVuc);
                }

                adapterKhuVuc.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void LayDanhSachTheLoai() {
        FirebaseDatabase.getInstance().getReference().child("theloai").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot value : dataSnapshot.getChildren()) {
                    String theloai = value.getValue(String.class);
                    theLoaiList.add(theloai);
                }
                adapterTheLoai.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void LayDanhSachThucDon(final ArrayAdapter<String> adapterThucDon) {
        FirebaseDatabase.getInstance().getReference().child("thucdons").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ThucDonModel thucDonModel = new ThucDonModel();
                    String key = snapshot.getKey();
                    String value = snapshot.getValue(String.class);

                    thucDonModel.setTenthucdon(value);
                    thucDonModel.setMathucdon(key);

                    thucDonModelList.add(thucDonModel);
                    thucDonList.add(value);

                }
                adapterThucDon.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void LayDanhSachTienIch() {
        FirebaseDatabase.getInstance().getReference().child("quanlytienichs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String maTienIch = snapshot.getKey();
                    TienIchModel tienIchModel = snapshot.getValue(TienIchModel.class);
                    tienIchModel.setMaTienIch(maTienIch);

                    CheckBox checkBox = new CheckBox(ThemQuanAnActivity.this);
                    checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    checkBox.setText(tienIchModel.getTentienich());
                    checkBox.setButtonDrawable(R.drawable.checkbox);
                    checkBox.setTag(maTienIch);
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            String maTienIch = buttonView.getTag().toString();
                            if (isChecked) {
                                selectedTienIchList.add(maTienIch);
                            } else {
                                selectedTienIchList.remove(maTienIch);
                            }
                        }
                    });
                    khungTienIch.addView(checkBox);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(final View v) {
        Calendar calendar = Calendar.getInstance();
        int gio = calendar.get(Calendar.HOUR_OF_DAY);
        int phut = calendar.get(Calendar.MINUTE);

        switch (v.getId()) {
            case R.id.btnGioDongCua:

                TimePickerDialog timePickerDialog = new TimePickerDialog(ThemQuanAnActivity.this, android.R.style.Theme_Material_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        gioDongCua = hourOfDay + ":" + minute;
                        ((Button) v).setText(gioDongCua);
                    }
                }, gio, phut, true);

                timePickerDialog.show();
                break;

            case R.id.btnGioMoCua:

                TimePickerDialog moCuaTimePickerDialog = new TimePickerDialog(ThemQuanAnActivity.this, android.R.style.Theme_Material_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        gioMoCua = hourOfDay + ":" + minute;
                        ((Button) v).setText(gioMoCua);
                    }
                }, gio, phut, true);

                moCuaTimePickerDialog.show();
                break;

            case R.id.imgHinhQuan1:
                ChonHinhTuGallary(RESULT_IMG1);
                break;

            case R.id.imgHinhQuan2:
                ChonHinhTuGallary(RESULT_IMG2);
                break;

            case R.id.imgHinhQuan3:
                ChonHinhTuGallary(RESULT_IMG3);
                break;

            case R.id.imgHinhQuan4:
                ChonHinhTuGallary(RESULT_IMG4);
                break;

            case R.id.imgHinhQuan5:
                ChonHinhTuGallary(RESULT_IMG5);
                break;

            case R.id.imgHinhQuan6:
                ChonHinhTuGallary(RESULT_IMG6);
                break;

            case R.id.imgVideo:
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Chọn video..."), RESULT_VIDEO);
                break;

            case R.id.btnThemQuanAn:
                ThemQuanAn();
                break;
        }
    }

    private void ThemQuanAn() {
        tenQuanAn = edTenQuanAn.getText().toString();
        diaChi = edtDiaChi.getText().toString();
        soDT = edtSoDienThoai.getText().toString();
        giaToiDa = Long.parseLong(edGiaToiDa.getText().toString());
        giaToiThieu = Long.parseLong(edGiaThoiThieu.getText().toString());
        if (tenQuanAn.isEmpty()
                || diaChi.isEmpty()
                || soDT.isEmpty()
                || giaToiThieu == 0
                || giaToiDa == 0
                || giaToiThieu >= giaToiDa
                || gioDongCua.isEmpty()
                || gioMoCua.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ và chính xác thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (hinhQuanAn == null || hinhQuanAn.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ảnh giới thiệu về địa điểm đề xuất", Toast.LENGTH_SHORT).show();
            return;
        }

        if (themThucDonModelList == null || themThucDonModelList.isEmpty()) {
            Toast.makeText(this, "Vui lòng thêm thông tin về thực đơn", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage(getString(R.string.dangxuly));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        giaToiDa = Long.parseLong(edGiaToiDa.getText().toString());
        giaToiThieu = Long.parseLong(edGiaThoiThieu.getText().toString());
        int idRadioSelected = rdgTrangThai.getCheckedRadioButtonId();
        boolean giaoHang = false;
        if (idRadioSelected == R.id.rdGiaoHang) {
            giaoHang = true;
        } else {
            giaoHang = false;
        }

        maQuanAn = nodeQuanAn.push().getKey();

        //nodeRoot.child("khuvucs").child(khuvuc).push().setValue(maQuanAn);

        quanAnModel.setTenquanan(tenQuanAn);
        quanAnModel.setSodienthoai(soDT);
        quanAnModel.setGiatoida(giaToiDa);
        quanAnModel.setGiatoithieu(giaToiThieu);
        quanAnModel.setGiomocua(gioMoCua);
        quanAnModel.setGiodongcua(gioDongCua);
        quanAnModel.setGiaohang(giaoHang);
        quanAnModel.setKhuvuc(khuvuc);
        quanAnModel.setTheloai(theloai);
        quanAnModel.setTienich(selectedTienIchList);
        quanAnModel.setLuotluu(0);

        nodeQuanAn.child(maQuanAn).setValue(quanAnModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                String urlGeoCoding = "https://maps.googleapis.com/maps/api/geocode/json?address=" + diaChi.replace(" ", "%20") + "&key=AIzaSyBVd2D3evAh1Ip_f5nuN1P6ad-14G3Ns0g";
                DownloadToaDo downloadToaDo = new DownloadToaDo();
                downloadToaDo.execute(urlGeoCoding);
            }
        });


    }

    private void uploadHinhQuanAn() {
        if (hinhQuanAn.isEmpty())
            for (Uri hinhquan : hinhQuanAn) {
                final StorageReference referenceHinhAnh = FirebaseStorage.getInstance().getReference().child("hinhquanan/" + hinhquan.getLastPathSegment());
                UploadTask uploadTask = referenceHinhAnh.putFile(hinhquan);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        referenceHinhAnh.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                nodeRoot.child("hinhanhquanans").child(maQuanAn).push().setValue(uri.toString());
                                uploadThucDon();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
    }

    private void uploadThucDon() {
        for (int i = 0; i < themThucDonModelList.size(); i++) {
            monAnModel = themThucDonModelList.get(i).getMonAnModel();
            final String maThucDon = themThucDonModelList.get(i).getMathucdon();
            Uri uriMonAn = hinhThucDon.get(i);
            final StorageReference referenceThucDon = FirebaseStorage.getInstance().getReference().child("hinhanh/" + uriMonAn.getLastPathSegment());
            UploadTask uploadTask = referenceThucDon.putFile(uriMonAn);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    referenceThucDon.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            monAnModel.setHinhanh(uri.toString());
                            nodeRoot.child("thucdonquanans").child(maQuanAn).child(maThucDon).push().setValue(monAnModel);
                            progressDialog.dismiss();
                            Toast.makeText(ThemQuanAnActivity.this, "Đề xuất địa điểm thành công!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

            /*Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return referenceThucDon.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri uriMonAn = task.getResult();
                        monAnModel.setHinhanh(uriMonAn.toString());
                        nodeRoot.child("thucdonquanans").child(maQuanAn).child(maThucDon).push().setValue(monAnModel);
                    } else {
                        Toast.makeText(ThemQuanAnActivity.this, "Xảy ra lỗi. Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });*/

            /*nodeRoot.child("thucdonquanans").child(maQuanAn).child(themThucDonModelList.get(i).getMathucdon()).push().setValue(themThucDonModelList.get(i).getMonAnModel());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap bitmap = hinhDaChup.get(i);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            FirebaseStorage.getInstance().getReference().child("hinhanh/" + themThucDonModelList.get(i).getMonAnModel().getHinhanh()).putBytes(data);
*/
        }
    }

    class DownloadToaDo extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray results = jsonObject.getJSONArray("results");
                //for (int i = 0; i < results.length(); i++) {
                JSONObject object = results.getJSONObject(0);
                String address = object.getString("formatted_address");
                JSONObject geometry = object.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");
                double latitude = (double) location.get("lat");
                double longitude = (double) location.get("lng");

                if (!address.contains(khuvuc)) {
                    progressDialog.dismiss();
                    Toast.makeText(ThemQuanAnActivity.this, "Đã xảy ra lỗi xác thực địa chỉ. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    nodeQuanAn.child(maQuanAn).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
                    return;
                }

                DiaChiModel diaChiModel = new DiaChiModel();
                diaChiModel.setAddress(address);
                diaChiModel.setLatitude(latitude);
                diaChiModel.setLongitude(longitude);
                nodeQuanAn.child(maQuanAn).child("diachi").setValue(diaChiModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
                handler.sendEmptyMessage(0);
                //}
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void ChonHinhTuGallary(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn hình..."), requestCode);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinnerKhuVuc:
                khuvuc = khuVucList.get(position);
                break;

            case R.id.spinnerTheLoai:
                theloai = theLoaiList.get(position);
                break;

            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Đề xuất địa điểm");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        } else {
            super.onBackPressed();
        }
    }
}
