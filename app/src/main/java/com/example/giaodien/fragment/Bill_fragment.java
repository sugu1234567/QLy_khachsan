package com.example.giaodien.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giaodien.Adapters.BillAdapter;
import com.example.giaodien.Model.Bookings;
import com.example.giaodien.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Bill_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Bill_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerViewBill;
    private BillAdapter billAdapter;
    private ArrayList<Bookings> billList;

    public Bill_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Room_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Bill_fragment newInstance(String param1, String param2) {
        Bill_fragment fragment = new Bill_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill, container, false);
        // Inflate the layout for this fragment
        Init(view);
        billList = new ArrayList<>();

        billList.add(new Bookings("102","Ngô Văn Nam","17/6/2024","18/6/2024","Chưa thanh toán","1000000"));
        billList.add(new Bookings("103","Nguyễn Trung Đức","17/6/2024","18/6/2024","Đã thanh toán","1000000"));
        // Thêm nhiều khách hàng khác

        recyclerViewBill.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        billAdapter = new BillAdapter(getContext(), billList);
        recyclerViewBill.setAdapter(billAdapter);
        return view;
    }

    private void Init(View view) {
        recyclerViewBill = view.findViewById(R.id.recyclerViewBill);
    }
}