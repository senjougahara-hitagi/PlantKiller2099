package plantkiller.wayne.com.plantskiller2099.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import plantkiller.wayne.com.plantskiller2099.R;
import plantkiller.wayne.com.plantskiller2099.data.model.TreeData;
import plantkiller.wayne.com.plantskiller2099.data.value.Const;
import plantkiller.wayne.com.plantskiller2099.ui.activity.TreeInformation;
import plantkiller.wayne.com.plantskiller2099.ui.activity.UpdateTree;

public class FormFragment extends Fragment {
    TreeData mTreeData;
    ProgressBar mProgressRed;
    boolean isStarted;
    RelativeLayout mRelativeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.custom_view, container, false);
        mTreeData = getArguments().getParcelable("key");
        isStarted = getArguments().getBoolean("isStarted");
        mRelativeLayout = view.findViewById(R.id.relative_custom);
        if(isStarted) mRelativeLayout.setVisibility(View.VISIBLE);
        else mRelativeLayout.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressRed = view.findViewById(R.id.progressRed);
        mProgressRed.setProgress(0);
        mProgressRed.getProgressDrawable().setColorFilter(
            Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN);
        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_tree_infor:
                        getActivity().startActivityForResult(TreeInformation.getInstance
                            (getContext(), mTreeData), Const.RequestCode.REQUEST_CODE_INFOMATION);
                        break;
                    case R.id.btn_tree_change:
                        Intent intent = new Intent(getContext(), UpdateTree.class);
                        startActivity(intent);
                        break;
                    case R.id.btn_water:
                        ValueAnimator animator = ValueAnimator.ofInt(0, mProgressRed.getMax());
                        animator.setDuration(5000);
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                mProgressRed.setProgress((Integer) animation.getAnimatedValue());
                            }
                        });
                        animator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                // start your activity here
                            }
                        });
                        animator.start();
                        if(mProgressRed.getProgress()==mProgressRed.getMax()) mRelativeLayout
                            .setVisibility(View.GONE);
                }
            }
        };
        TextView mTreeDes = view.findViewById(R.id.text_treeDes);
        mTreeDes.setText(mTreeData.getDes());
        TextView mTreeName = view.findViewById(R.id.text_treeName);
        mTreeName.setText(mTreeData.getTreeName());
        view.findViewById(R.id.btn_tree_infor).setOnClickListener(onClickListener);
        view.findViewById(R.id.btn_tree_change).setOnClickListener(onClickListener);
        view.findViewById(R.id.btn_water).setOnClickListener(onClickListener);
    }
}
