package io.intrepid.animationexercise.screens.detail;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;
import io.intrepid.animationexercise.R;
import io.intrepid.animationexercise.base.BaseMvpActivity;
import io.intrepid.animationexercise.base.PresenterConfiguration;
import io.intrepid.animationexercise.models.Cat;


public class DetailActivity extends BaseMvpActivity<DetailContract.Presenter> implements DetailContract.View {

    private static final String EXTRA_CAT = "_extra_cat_";

    @BindView(R.id.cat_image)
    ImageView catImageView;

    @BindView(R.id.cat_id)
    TextView catIdView;

    @BindView(R.id.view_cat_button)
    Button viewTapButton;

    @BindView(R.id.power_bar)
    ImageView powerUpBar;

    @BindView(R.id.cat_info_container)
    LinearLayout catContainer;

    public static Intent getStartIntent(Context context, Cat cat) {
        return new Intent(context, DetailActivity.class).putExtra(EXTRA_CAT, cat);
    }

    @NonNull
    @Override
    public DetailContract.Presenter createPresenter(PresenterConfiguration configuration) {
        return new DetailPresenter(this, configuration);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_detail;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        Cat cat = getIntent().getParcelableExtra(EXTRA_CAT);

        Picasso.with(this).load(cat.getUrl()).noPlaceholder().into(catImageView);
        catIdView.setText(getString(R.string.cat_my_name_is, cat.getId()));
        catContainer.setVisibility(View.GONE);

        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.power_bar_clip, getTheme());
        powerUpBar.setImageDrawable(drawable);
    }

    @Override
    public void onBackPressed() {
        presenter.onBackPressed();
    }

    @OnClick(R.id.view_cat_button)
    void onViewCatClicked() {
        presenter.onViewCatClicked();
    }

    @OnClick(R.id.cat_image)
    void onCatImageClicked() {
        presenter.onCatImageClicked();
    }

    @Override
    public void animateImage() {
        catContainer.animate()
                .alpha(0f)
                .setDuration(500)
                .withEndAction(() -> super.onBackPressed())
                .start();
        overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
    }

    @Override
    public void showCat() {
        viewTapButton.animate()
                .scaleX(0f)
                .scaleY(0f)
                .setDuration(250)
                .withEndAction(() -> onButtonGone())
                .start();
    }

    private void onButtonGone() {
        viewTapButton.setVisibility(View.GONE);
        catContainer.setAlpha(0f);
        catContainer.setVisibility(View.VISIBLE);
        catContainer.animate()
                .alpha(1f)
                .setDuration(250)
                .start();
    }

    @Override
    public void animatePowerUp() {
        Drawable drawable = powerUpBar.getDrawable();
        drawable.setLevel(0);
        ObjectAnimator animator = ObjectAnimator.ofInt(drawable, "level", 10000);
        animator.setInterpolator(new BounceInterpolator());
        animator.start();
    }

    @Override
    public void displayInstructions() {
        Toast.makeText(this, R.string.cat_instructions, Toast.LENGTH_SHORT).show();
    }
}
