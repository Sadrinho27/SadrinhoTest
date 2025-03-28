package com.example.sadrinhotest.Pages;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.sadrinhotest.R;
import com.example.sadrinhotest.databinding.FragmentMenuBinding;
import com.example.sadrinhotest.models.UserViewModel;


public class FragmentMenu extends Fragment {

    private FragmentMenuBinding binding;

    private UserViewModel userViewModel;

    public static FragmentMenu newInstance(String param1, String param2) {
        FragmentMenu fragment = new FragmentMenu();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Récupère le ViewModel partagé
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        if (binding != null) {
            binding.PlayBtn.setOnClickListener(v -> {
                FragmentQuiz fragmentQuiz = new FragmentQuiz();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.conteneur, fragmentQuiz)
                        .addToBackStack(null)
                        .commit();
            });

            binding.LeaderboardBtn.setOnClickListener(v -> {
                FragmentLeaderboard fragmentLeaderboard = new FragmentLeaderboard();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.conteneur, fragmentLeaderboard)
                        .addToBackStack(null)
                        .commit();
            });

            binding.logoutBtn.setOnClickListener(v -> {
                // Retirer les informations de l'utilisateur de SharedPreferences
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("is_logged_in"); // Supprimer l'état de connexion
                editor.remove("user_pseudo"); // Supprimer le pseudo de l'utilisateur, si tu l'avais stocké
                editor.apply(); // Appliquer les changements

                // Mettre l'utilisateur à null dans le ViewModel, si nécessaire
                UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
                userViewModel.setUser(null);

                Log.d("STATE", "Utilisateur déconnecté !");

                // Rediriger vers le fragment d'accueil
                FragmentAccueil fragmentAccueil = new FragmentAccueil();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.conteneur, fragmentAccueil)
                        .addToBackStack(null)
                        .commit();
            });

            userViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
                if (user != null) {
                    if (user.isAdmin()) {
                        binding.adminBtn.setVisibility(View.VISIBLE);
                    } else {
                        binding.adminBtn.setVisibility(View.INVISIBLE);
                    }
                }
            });

            binding.adminBtn.setOnClickListener(v -> {
                FragmentAdmin fragmentAdmin = new FragmentAdmin();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.conteneur, fragmentAdmin)
                        .addToBackStack(null)
                        .commit();
            });
        }

    }
}