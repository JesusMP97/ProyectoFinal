package org.izv.proyecto.view.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import org.izv.proyecto.R;
import org.izv.proyecto.model.data.Factura;

public class CustomAnimation {
    public static void animate(final TextView view, final Factura invoice) {
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f);
        oa1.setInterpolator(new DecelerateInterpolator());
        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
        oa1.setDuration(200);
        oa2.setDuration(200);

        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (invoice.isSelected()) {
                    if (view.getId() == R.id.tvItemDestinationImg) {
                        view.setText("\u2713");
                    }
                }
                oa2.start();
            }
        });
        oa1.start();
    }
}
