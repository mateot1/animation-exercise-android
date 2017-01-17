package io.intrepid.animationexercise.screens.overview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import io.intrepid.animationexercise.R;
import io.intrepid.animationexercise.base.BaseMvpActivity;
import io.intrepid.animationexercise.base.PresenterConfiguration;
import io.intrepid.animationexercise.models.Cat;
import io.intrepid.animationexercise.screens.detail.DetailActivity;


public class OverviewActivity extends BaseMvpActivity<OverviewContract.Presenter> implements OverviewContract.View, CatAdapter.CatClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.progress)
    View progressSpinner;

    @BindView(R.id.cat_icon)
    TextView catImageView;

    @NonNull
    @Override
    public OverviewContract.Presenter createPresenter(PresenterConfiguration configuration) {
        return new OverviewPresenter(this, configuration);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_overview;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void toggleLoadingSpinner(boolean visible) {
        progressSpinner.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (catImageView.getAlpha() < 1) {
            catImageView.setAlpha(1f);
            catImageView.setTranslationX(0);
        }
    }

    @Override
    public void goToCatDetail(Cat cat) {
        catImageView.animate()
                .alpha(0f)
                .translationXBy(60)
                .setDuration(500)
                .withEndAction(() -> startActivity(DetailActivity.getStartIntent(this, cat)))
                .start();
    }

    @Override
    public void onCatsLoaded(List<Cat> cats) {
        recyclerView.setAdapter(new CatAdapter(cats, this));
    }

    @Override
    public void onCatClicked(Cat cat) {
        presenter.onCatClicked(cat);
    }
}