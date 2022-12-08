package com.cinestar.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.cinestar.R;
import com.cinestar.fragments.ChatsFragment;
import com.cinestar.fragments.FiltersFragment;
import com.cinestar.fragments.HomeFragment;
import com.cinestar.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GeneroActivity extends AppCompatActivity {
    // Creacion del menu de los fragments
    //https://androidwave.com/bottom-navigation-bar-android-example/
    BottomNavigationView bottonNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genero);

        bottonNavigation = findViewById(R.id.botom_navigation);
        bottonNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(new HomeFragment());
    }

    public void openFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                /*@Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            openFragment(new HomeFragment());
                            //return true;
                        case R.id.navigation_filter:
                            openFragment(new FiltersFragment());
                            //return true;
                        case R.id.navigation_chat:
                            openFragment(new ChatsFragment());
                            //return true;
                        case R.id.navigation_perfil:
                            openFragment(new ProfileFragment());
                    }*/

                    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        if(item.getItemId()==R.id.navigation_home){
                            openFragment(new HomeFragment());
                        }else if(item.getItemId()==R.id.navigation_chat){
                            openFragment(new ChatsFragment());
                        }else if(item.getItemId()==R.id.navigation_filter){
                            openFragment(new FiltersFragment());
                        }else if(item.getItemId()==R.id.navigation_perfil){
                            openFragment(new ProfileFragment());
                        }
                    return true;
                }
            };

}