package com.mobilalk.computercomp;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mobilalk.computercomp.model.Part;

import java.util.ArrayList;

public class PartListActivity extends AppCompatActivity {
    private static final String LOG_TAG = PartListActivity.class.getName();
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private RecyclerView mRecyclerView;
    private ArrayList<Part> mItemList;
    private PartItemAdapter itemAdapter;
    private int gridNumber = 1;
    private boolean viewRow = true;

    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_list);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null){
            finish();
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_IF_CONTENT_SCROLLS);
        mRecyclerView.setLayoutManager( new GridLayoutManager(this, gridNumber));
        mItemList = new ArrayList<>();

        itemAdapter = new PartItemAdapter(this, mItemList);

        mRecyclerView.setAdapter(itemAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Items");
        System.out.println(mItems.getFirestore());

        queryData();
    }

    private void queryData() {

        mItemList.clear();

        mItems.orderBy("name").limit(10).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                Part item = documentSnapshot.toObject(Part.class);
                mItemList.add(item);
            }

            if (mItemList.size() == 0){
                initializeData();
                queryData();
            }

            itemAdapter.notifyDataSetChanged();
        });

    }

    private void initializeData() {
        String[] itemList = getResources().getStringArray(R.array.part_name_list);
        String[] itemDesc = getResources().getStringArray(R.array.part_type);
        String[] itemWebLink =getResources().getStringArray(R.array.part_weblink);

        for (int i = 0; i< itemList.length; i++){
            mItems.add(new Part(itemList[i], itemDesc[i], itemWebLink[i]));

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.part_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                itemAdapter.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }

    private void changeSpanCount(MenuItem item, int drawableId, int spanCount) {
        viewRow = !viewRow;
        item.setIcon(drawableId);
        GridLayoutManager gridLayoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        gridLayoutManager.setSpanCount(spanCount);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            case R.id.compare:
                Log.d(LOG_TAG, "Cart");
                return true;
            case R.id.view_selector:
                if(viewRow){
                    changeSpanCount(item, R.drawable.ic_baseline_view_module_24, 1);
                } else {
                    changeSpanCount(item, R.drawable.ic_baseline_view_agenda_24, 2);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu)  {
        final MenuItem alertMenuItem = menu.findItem(R.id.compare);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();
        Log.d(LOG_TAG, "asdasjfé " + alertMenuItem);
        try {
            rootView.setOnClickListener(view -> onOptionsItemSelected(alertMenuItem));
        } catch (Exception e){}
        return super.onPrepareOptionsMenu(menu);
    }

}