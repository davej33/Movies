package com.example.android.movieapp2;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movieapp2.data.MovieContract;
import com.example.android.movieapp2.data.MovieDbHelper;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    // the fragment initialization parameters
    private static final String ARG_MOVIE_ID = "movieId";
    private String mMovieID;
    private MovieDbHelper mDbHelper;
    private static Cursor sCursor;

    private OnFragmentInteractionListener mListener;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MovieDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieDetailFragment newInstance(String movieID) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MOVIE_ID, movieID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovieID = getArguments().getString(ARG_MOVIE_ID);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        // Inflate the layout for this fragment
//        Toast.makeText(getContext(), "Movie Id from Frag: " + mMovieID, Toast.LENGTH_LONG).show();
        String[] selectionArgs = {mMovieID};
        mDbHelper = new MovieDbHelper(getContext());
        sCursor = mDbHelper.getReadableDatabase().query(MovieContract.MovieEntry.MOVIE_TABLE,
                null, MovieContract.MovieEntry.MOVIE_ID, selectionArgs, null, null, null);

        // get poster image url
        int posterColId = sCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_POSTER);
        String posterUrl = sCursor.getString(posterColId);

        // get title
        int titleColId = sCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_TITLE);
        String title = sCursor.getString(titleColId);
        TextView titleTV = (TextView) view.findViewById(R.id.movie_title);
        titleTV.setText(title);
//        Log.i(LOG_TAG, "Title: " + title);

        int tmdbIdCol = sCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_TMDB_ID);
        int tmdbId = sCursor.getInt(tmdbIdCol);


        int idCol = sCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_ID);
        int id = sCursor.getInt(idCol);

        ImageView posterView = (ImageView) view.findViewById(R.id.cover_image);
        // picasso library to fetch and display images
        Picasso.with(getContext())
                .load(posterUrl)
                .error(R.drawable.error)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .resize(400, 600)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .into(posterView);

        return inflater.inflate(R.layout.fragment_movie_detail, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
