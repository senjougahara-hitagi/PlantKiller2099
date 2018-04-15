package plantkiller.wayne.com.plantskiller2099.ui.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import plantkiller.wayne.com.plantskiller2099.R;
import plantkiller.wayne.com.plantskiller2099.data.model.TreeData;
import plantkiller.wayne.com.plantskiller2099.data.value.Const;
import plantkiller.wayne.com.plantskiller2099.ui.activity.TreeInformation;
import plantkiller.wayne.com.plantskiller2099.ui.activity.UpdateTree;

public class FormFragment extends Fragment {
    TreeData mTreeData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.custom_view, container, false);
        mTreeData = getArguments().getParcelable("key");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                }
            }
        };
        TextView mTreeDes = view.findViewById(R.id.text_treeDes);
        mTreeDes.setText(mTreeData.getDes());
        TextView mTreeName = view.findViewById(R.id.text_treeName);
        mTreeName.setText(mTreeData.getTreeName());
        view.findViewById(R.id.btn_tree_infor).setOnClickListener(onClickListener);
        view.findViewById(R.id.btn_tree_change).setOnClickListener(onClickListener);
    }
}
