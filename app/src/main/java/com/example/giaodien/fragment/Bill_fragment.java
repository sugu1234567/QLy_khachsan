package com.example.giaodien.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giaodien.Adapters.BillAdapter;
import com.example.giaodien.Model.Bookings;
import com.example.giaodien.R;
import com.example.giaodien.Response.BookingDetailsResponse;
import com.example.giaodien.Service.ApiService;
import com.example.giaodien.Service.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Bill_fragment extends Fragment {

    private RecyclerView recyclerViewBill;
    private BillAdapter billAdapter;
    private ArrayList<BookingDetailsResponse> billList;
    private ApiService apiService;
    private SearchView search_view_bill;
    private Spinner spinnerStatus;
    private ActivityResultLauncher<Intent> launcher;
    private String selectedStatus;

    public Bill_fragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Làm mới danh sách khi quay lại
                        fetchBills();
                    }
                }
        );
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill, container, false);

        apiService = RetrofitClient.getClient().create(ApiService.class);
        // Inflate the layout for this fragment
        Init(view);

        billList = new ArrayList<>();
        recyclerViewBill.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        billAdapter = new BillAdapter(billList, launcher);
        recyclerViewBill.setAdapter(billAdapter);
        fetchBills();
        filterRecyclerView();
        SpinnerStatus();
        return view;
    }

    private void SpinnerStatus() {
        selectedStatus = spinnerStatus.getSelectedItem().toString();
        billAdapter.setSelectedStatus(selectedStatus);
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedStatus = adapterView.getItemAtPosition(i).toString();
                billAdapter.setSelectedStatus(selectedStatus);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void filterRecyclerView() {
        search_view_bill.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                billAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                billAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    private void fetchBills() {
        apiService.getBills().enqueue(new Callback<List<BookingDetailsResponse>>() {
            @Override
            public void onResponse(Call<List<BookingDetailsResponse>> call, Response<List<BookingDetailsResponse>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    billList.clear();
                    billList.addAll(response.body());
                    billAdapter.notifyDataSetChanged();
                }
                else{
                    Log.e("CustomerFragment", "Response failed");
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BookingDetailsResponse>> call, Throwable t) {
                Log.e("CustomerFragment", "API call failed: " + t.getMessage());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Init(View view) {
        recyclerViewBill = view.findViewById(R.id.recyclerViewBill);
        search_view_bill = view.findViewById(R.id.search_view_bill);
        spinnerStatus = view.findViewById(R.id.spinnerStatus);
    }
}