package com.example.eventplanner.fragments.home_screen_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.CommentAdapter;
import com.example.eventplanner.adapters.ReportAdapter;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.dto.comment.GetCommentDTO;
import com.example.eventplanner.dto.report.ReportDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentsFragment extends Fragment {


    private ArrayList<GetCommentDTO> allComments;
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private TextView commentsNumberTextView;

    public CommentsFragment() {
        // Required empty public constructor
    }

    public static CommentsFragment newInstance() {
        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        allComments = new ArrayList<GetCommentDTO>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container, false);

        commentsNumberTextView = view.findViewById(R.id.commentNumber);

        setUpRecyclerView(view);
        loadComments(view);
        return view;
    }

    private void loadComments(View view){
        Call<ArrayList<GetCommentDTO>> call = ClientUtils.commentService.getPendingComments();
        call.enqueue(new Callback<ArrayList<GetCommentDTO>>() {

            @Override
            public void onResponse(Call<ArrayList<GetCommentDTO>> call, Response<ArrayList<GetCommentDTO>> response) {
                if (response.isSuccessful()) {
                    allComments = response.body();
                    commentAdapter = new CommentAdapter(allComments, getContext(), view, commentsNumberTextView);
                    recyclerView.setAdapter(commentAdapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GetCommentDTO>> call, Throwable t) {
                Log.i("Comments", t.getMessage());
            }
        });
    }

    private void setUpRecyclerView(View view){
        recyclerView = view.findViewById(R.id.commentRecyclerView);
        LinearLayoutManager layoutManagerComment= new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManagerComment);
    }
}