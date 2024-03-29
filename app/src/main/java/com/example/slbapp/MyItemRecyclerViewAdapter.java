package com.example.slbapp;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;;
import com.example.slbapp.models.Course;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> implements Filterable {

    private final List<Course> courses;
    private final List<Course> allCourses;

    public MyItemRecyclerViewAdapter(List<Course> courses) {
        this.courses = courses;
        this.allCourses = new ArrayList<>(courses);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = courses.get(position).getName();
        holder.mContentView.setText(courses.get(position).getName());
        holder.mYearView.setText( "jaar: " + courses.get(position).getYear());
        holder.mGradeView.setText("cijfer: " + courses.get(position).getGrade());
        holder.mEctsView.setText("punten: " + courses.get(position).getEcts());
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        //runs on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<Course> filteredList = new ArrayList<>();
            if(charSequence.toString().isEmpty()) {
                filteredList.addAll(allCourses);
            } else {
                for (Course course: allCourses) {
                    if (course.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(course);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        // runs on ui thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            courses.clear();
            courses.addAll((Collection<? extends Course>)filterResults.values);
            CoursesService coursesService = new CoursesService();
            coursesService.setFilteredCourses(courses);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final TextView mYearView;
        public final TextView mGradeView;
        public final TextView mEctsView;
        public String mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
            mYearView = view.findViewById(R.id.content_year);
            mGradeView = view.findViewById(R.id.content_Grade);
            mEctsView = view.findViewById(R.id.content_Ects);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}