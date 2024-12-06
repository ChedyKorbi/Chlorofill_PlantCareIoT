package com.example.plantiotapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private OnboardingAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.viewPager);
        adapter = new OnboardingAdapter(getOnboardingItems());
        viewPager.setAdapter(adapter);

        // Handle button click to skip to main activity after onboarding
        MaterialButton nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() < adapter.getItemCount() - 1) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            } else {
                startActivity(new Intent(OnboardingActivity.this, HomePage.class));
                finish();
            }
        });
    }

    private List<OnboardingItem> getOnboardingItems() {
        List<OnboardingItem> items = new ArrayList<>();
        items.add(new OnboardingItem("Welcome to Plante IoT", "Discover how our smart sensors can optimize your plant growth.", R.drawable.pla));
        items.add(new OnboardingItem("Your Smart Garden", "Tips N Tricks to grow a healthy plant", R.drawable.pl3));
        items.add(new OnboardingItem("Join Our Community", "Share your successes and learn from other plant enthusiasts.", R.drawable.aaaa));
        return items;
    }
}
