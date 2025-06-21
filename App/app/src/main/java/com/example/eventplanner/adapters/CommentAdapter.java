package com.example.eventplanner.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.dto.comment.GetCommentDTO;
import com.example.eventplanner.dto.comment.UpdateCommentDTO;
import com.example.eventplanner.dto.comment.UpdatedCommentDTO;
import com.example.eventplanner.dto.notification.UpdateNotificationDTO;
import com.example.eventplanner.dto.notification.UpdatedNotificationDTO;
import com.example.eventplanner.dto.report.ReportDTO;
import com.example.eventplanner.dto.report.UpdateReportDTO;
import com.example.eventplanner.dto.report.UpdatedReportDTO;
import com.example.eventplanner.model.Report;
import com.example.eventplanner.model.State;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder>{

    private ArrayList<GetCommentDTO> allComments;
    private Context context;
    private boolean[] itemExpandedState;
    private TextView commentsNumberTextView;

    private View view;

    public CommentAdapter(ArrayList<GetCommentDTO> allComments, Context context, View view, TextView commentsNumberTextView){
        this.allComments = allComments;
        this.context = context;
        this.itemExpandedState = new boolean[allComments.size()];
        this.view = view;
        this.commentsNumberTextView = commentsNumberTextView;
        setCommentsNumberText();
    }

    @Override
    public int getItemViewType(int position){
        if (!itemExpandedState[position]){
            return 1;
        }else{
            return 2;
        }
    }

    @NonNull
    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_card, parent, false);
                return new CommentAdapter.MyViewHolder(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_card_expanded, parent, false);
                return new CommentAdapter.MyViewHolder(view);
            default:
                throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.MyViewHolder holder, int position) {
        GetCommentDTO comment = allComments.get(position);
        holder.fromUser.setText(String.format("FROM: %s %s", comment.getEventOrganizer().getFirstName(), comment.getEventOrganizer().getLastName()));
        holder.commentedServiceProduct.setText(String.format("Service name: %s", comment.getServiceProduct().getName()));
        holder.commentText.setText(String.format("Text: %s",comment.getText()));
        holder.viewMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemExpandedState[holder.getAdapterPosition()] = !itemExpandedState[holder.getAdapterPosition()];
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
        holder.approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateComment(comment, State.APPROVED);
                String snackBarText = String.format("Comment approved!");
                Snackbar snackbar = Snackbar.make(view, snackBarText, Snackbar.LENGTH_LONG);
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
                snackbar.show();
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateComment(comment, State.DISAPPROVED);
                String snackBarText = String.format("Comment disapproved!");
                Snackbar snackbar = Snackbar.make(view, snackBarText, Snackbar.LENGTH_LONG);
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
                snackbar.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return allComments.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView fromUser;
        TextView commentedServiceProduct;
        TextView commentText;
        TextView commentTitle;

        Button viewMoreButton;
        Button approveButton;
        Button deleteButton;


        public MyViewHolder(View itemView) {
            super(itemView);
            fromUser = itemView.findViewById(R.id.commentFrom);
            commentedServiceProduct = itemView.findViewById(R.id.commentServiceProductName);
            commentText = itemView.findViewById(R.id.commentText);
            viewMoreButton = itemView.findViewById(R.id.commentViewMoreButton);
            deleteButton = itemView.findViewById(R.id.commentDeleteButton);
            approveButton = itemView.findViewById(R.id.commentApproveButton);
        }
    }

    private void updateComment(GetCommentDTO comment, State newState){
        UpdateCommentDTO updateComment = new UpdateCommentDTO(comment.getText(), newState);
        Call<UpdatedCommentDTO> call = ClientUtils.commentService.updateComment(updateComment, comment.getId());
        call.enqueue(new Callback<UpdatedCommentDTO>() {

            @Override
            public void onResponse(Call<UpdatedCommentDTO> call, Response<UpdatedCommentDTO> response) {
                if (response.isSuccessful()) {
                    int i = allComments.indexOf(comment);
                    allComments.remove(i);
                    notifyItemRemoved(i);
                    setCommentsNumberText();
                }
            }

            @Override
            public void onFailure(Call<UpdatedCommentDTO> call, Throwable t) {
                Log.i("Comments", t.getMessage());
            }
        });
    }

    private void setCommentsNumberText(){
        if (allComments.isEmpty()){
            commentsNumberTextView.setText("There's no comments!");
        }else if (allComments.size() == 1){
            commentsNumberTextView.setText("There's 1 comment!");
        }else{
            commentsNumberTextView.setText(String.format("There's %d comments!", allComments.size()));
        }
    }
}

