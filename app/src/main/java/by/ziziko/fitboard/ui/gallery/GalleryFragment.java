package by.ziziko.fitboard.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import by.ziziko.fitboard.JSONHelper;
import by.ziziko.fitboard.Activities.NewCategoryActivity;
import by.ziziko.fitboard.R;
import by.ziziko.fitboard.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ListView listView = root.findViewById(R.id.recyclerview);

        List<String> categories = JSONHelper.importFromJSON(getActivity().getApplicationContext());
        if (categories == null)
            categories = new ArrayList<>();
        ListAdapter adapter = new ArrayAdapter(
                getActivity().getApplicationContext(),
                R.layout.category_item, categories);

        listView.setAdapter(adapter);

        binding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), NewCategoryActivity.class);
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}