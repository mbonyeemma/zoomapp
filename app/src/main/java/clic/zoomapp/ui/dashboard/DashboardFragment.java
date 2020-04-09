package clic.zoomapp.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import clic.zoomapp.R;
import clic.zoomapp.utils.CardAdapter;
import clic.zoomapp.utils.Model;

public class DashboardFragment extends Fragment    {
RecyclerView RView;
    private DashboardViewModel dashboardViewModel;
    private ScaleGestureDetector mScaleGestureDetector;
    CardAdapter mAdapter;
    private float mScaleFactor = 1.0f;
    private ImageView mImageView;
    ArrayList<Model> countryList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
         RView =  root.findViewById(R.id.recylerview);


        setUpView();
        InitView();
        return root;

    }
    void setUpView(){
        RView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RView.setHasFixedSize(true);
        mAdapter = new CardAdapter(getActivity(), countryList);
        RView.setAdapter(mAdapter);

    }

    void InitView(){
        for(int i=0;i<10;i++){
            Model data = new Model();
            data.setTitle("Lorem ipsum dolor sit amet");
            data.setDescription("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ");
            data.setImage(R.drawable.wallpaper);
            countryList.add(data);
        }

        mAdapter.notifyDataSetChanged();
    }




}