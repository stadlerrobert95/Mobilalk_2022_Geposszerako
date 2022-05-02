package com.mobilalk.computercomp.ui.builderPage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mobilalk.computercomp.databinding.FragmentBuilderBinding;

public class BuilderPageFragment extends Fragment {

    private FragmentBuilderBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BuilderPageViewModel builderPageViewModel =
                new ViewModelProvider(this).get(BuilderPageViewModel.class);

        binding = FragmentBuilderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        builderPageViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}