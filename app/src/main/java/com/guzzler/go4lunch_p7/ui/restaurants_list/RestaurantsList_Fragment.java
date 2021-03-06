package com.guzzler.go4lunch_p7.ui.restaurants_list;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.guzzler.go4lunch_p7.R;
import com.guzzler.go4lunch_p7.models.googleplaces_gson.ResultDetails;
import com.guzzler.go4lunch_p7.ui.BaseFragment;
import com.guzzler.go4lunch_p7.ui.MainActivity;
import com.guzzler.go4lunch_p7.ui.restaurant_details.Restaurant_Details;
import com.guzzler.go4lunch_p7.utils.ItemClickSupport;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RestaurantsList_Fragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private MainActivity mMainActivity;
    private RestaurantsList_RecyclerViewAdapter mViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurants_list, container, false);
        Context context = view.getContext();

        // LIVEDATA
        mMainActivity.mLiveData.observe(getViewLifecycleOwner(), resultDetails -> configureRecyclerView());

        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        setHasOptionsMenu(true);
        configureOnClickRecyclerView();
        requireActivity().setTitle(getString(R.string.Titre_Toolbar_hungry));
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.activity_main_appbar, menu);

        SearchManager searchManager = (SearchManager) requireContext().getSystemService(Context.SEARCH_SERVICE);
        MenuItem item = menu.findItem(R.id.menu_activity_main_search);
        SearchView mSearchView = new SearchView(Objects.requireNonNull(((MainActivity) requireContext()).getSupportActionBar()).getThemedContext());
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setActionView(mSearchView);
        mSearchView.setQueryHint(getResources().getString(R.string.search_hint));
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(((MainActivity) requireContext()).getComponentName()));
        mSearchView.setIconifiedByDefault(false);// Do not iconify the widget; expand it by default
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 2) {
                    mMainActivity.googleAutoCompleteSearch(query);
                    mViewAdapter.notifyDataSetChanged();
                    mSearchView.clearFocus();
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.search_too_short), Toast.LENGTH_LONG).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.length() > 2) {
                    mMainActivity.googleAutoCompleteSearch(query);
                    mViewAdapter.notifyDataSetChanged();
                } else if (query.length() == 0) {
                    mMainActivity.searchByCurrentPosition();
                    mViewAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        configureRecyclerView();
    }

    private void configureRecyclerView() {
        this.mViewAdapter = new RestaurantsList_RecyclerViewAdapter(mMainActivity.mLiveData.getValue(), mMainActivity.mShareViewModel.getCurrentUserPositionFormatted());
        List<ResultDetails> mResult = mMainActivity.mLiveData.getValue();
        if (mResult != null) {
            Collections.sort(mResult);
        }
        RestaurantsList_RecyclerViewAdapter mViewAdapter = new RestaurantsList_RecyclerViewAdapter(mResult, mMainActivity.mShareViewModel.getCurrentUserPositionFormatted());
        this.mRecyclerView.setAdapter(mViewAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void configureOnClickRecyclerView() {
        ItemClickSupport.addTo(mRecyclerView, R.layout.fragment_restaurant_item)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    ResultDetails result = mViewAdapter.getRestaurantDetails(position);
                    Intent intent = new Intent(getActivity(), Restaurant_Details.class);
                    intent.putExtra("PlaceDetailResult", result.getPlaceId());
                    startActivity(intent);
                });
    }


}