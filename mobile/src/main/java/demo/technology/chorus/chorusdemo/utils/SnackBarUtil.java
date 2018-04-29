package demo.technology.chorus.chorusdemo.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import demo.technology.chorus.chorusdemo.ChorusApp;
import demo.technology.chorus.chorusdemo.R;

public class SnackBarUtil {

    public static void showSnackBar(View coordinatorLayoutView, String text){
        Snackbar snackbar = Snackbar.make(coordinatorLayoutView, text, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static void showSnackBarCustom(View coordinatorLayoutView, String text){
        Snackbar snackbar = Snackbar.make(coordinatorLayoutView, text, Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        // Hide the text
        TextView textView = layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        // Inflate our custom view
        View snackView = ((LayoutInflater) ChorusApp.getInstance().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.snack, null);
        // Configure the view
        // ImageView imageView = (ImageView) snackView.findViewById(R.id.image);
        // imageView.setImageBitmap(image);
        TextView textViewTop = (TextView) snackView.findViewById(R.id.text);
        textViewTop.setText(text);
        textViewTop.setTextColor(Color.WHITE);

        //If the view is not covering the whole snackbar layout, add this line
        layout.setPadding(0,0,0,0);

        // Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
        // Show the Snackbar
        snackbar.show();
    }

    public static void showSnackBar(View coordinatorLayoutView, String text, String action, View.OnClickListener onAction){
        Snackbar snackbar = Snackbar.make(coordinatorLayoutView, text, Snackbar.LENGTH_LONG);
        snackbar.setAction(action, onAction);
        snackbar.show();
    }
}
