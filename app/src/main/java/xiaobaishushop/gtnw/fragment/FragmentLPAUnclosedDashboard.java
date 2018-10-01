package xiaobaishushop.gtnw.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bin.david.form.core.SmartTable;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Response;
import xiaobaishushop.gtnw.R;
import xiaobaishushop.gtnw.bean.BeanLPAUnCloseBoservations;
import xiaobaishushop.gtnw.entiy.RetrofitInstanceOne;
import xiaobaishushop.gtnw.service.LPAUncloseObservationsService;
import xiaobaishushop.gtnw.table.tableLPAUnclosedObservations;
import xiaobaishushop.gtnw.util.Logger;

public class FragmentLPAUnclosedDashboard extends Fragment {
    public static  int PAGE = 1;
    public static  int COUNT = 10;
    public static int TOTAL_PAGES=1;
    @BindView(R.id.st_unclosed_observations)
    SmartTable stUnclosedObservations;
    @BindView(R.id.btn_lpa_unclosed_dashboard_refresh)
    Button btnLpaUnclosedDashboardRefresh;
    @BindView(R.id.btn_lpa_unclosed_dashboard_pre_page)
    Button btnLpaUnclosedDashboardPrePage;
    @BindView(R.id.btn_lpa_unclosed_dashboard_page_view)
    Button btnLpaUnclosedDashboardPageView;
    @BindView(R.id.btn_lpa_unclosed_dashboard_next_page)
    Button btnLpaUnclosedDashboardNextPage;
    Unbinder unbinder;

    public static final int UPDATE_SUCCESS_CODE = 100;
    public static final int UPDATE_FAIL_CODE = 200;

    private UpdateUncloseObservationsHandler handler = new UpdateUncloseObservationsHandler(this);
    private void initData() {
        new Thread() {
            @Override
            public void run() {
                try {
                    LPAUncloseObservationsService lpaUncloseObservationsService = RetrofitInstanceOne.getInstance().create(LPAUncloseObservationsService.class);
                    retrofit2.Call<BeanLPAUnCloseBoservations> call = lpaUncloseObservationsService.getUncloseObservations(String.valueOf(PAGE), String.valueOf(COUNT));
                    Response<BeanLPAUnCloseBoservations> response = call.execute();
                    Message handlerMessage = Message.obtain();
                    if (response.code() == 200) {
                        handlerMessage.what = UPDATE_SUCCESS_CODE;
                        handlerMessage.obj = response.body();
                        Logger.i(response.body().getCurrent_page());
                    } else {
                        handlerMessage.what = UPDATE_FAIL_CODE;
                        handlerMessage.obj = "code: " + response.code();
                    }
                    handler.sendMessage(handlerMessage);
                } catch (IOException e1) {
                    e1.printStackTrace();
                    Message handlerMessage = Message.obtain();
                    handlerMessage.what = UPDATE_FAIL_CODE;
                    handlerMessage.obj = e1.toString();
                    handler.sendMessage(handlerMessage);
                }
            }
        }.start();
    }
    /***
     * 更新 UI的handler
     * */
    public static class UpdateUncloseObservationsHandler extends Handler {
        final WeakReference<FragmentLPAUnclosedDashboard> mWeakReference;
        UpdateUncloseObservationsHandler(FragmentLPAUnclosedDashboard mWeakReference) {
            this.mWeakReference = new WeakReference<>(mWeakReference);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            FragmentLPAUnclosedDashboard activity = mWeakReference.get();
            switch (msg.what) {
                case UPDATE_SUCCESS_CODE:
                    BeanLPAUnCloseBoservations beanLPAUnCloseBoservations = (BeanLPAUnCloseBoservations) msg.obj;

                    String currentPage= String.valueOf(beanLPAUnCloseBoservations.getCurrent_page());
                    String lastPage= String.valueOf(beanLPAUnCloseBoservations.getLast_page());
                    activity.btnLpaUnclosedDashboardPageView.setText(currentPage+"/"+lastPage);
                    TOTAL_PAGES=beanLPAUnCloseBoservations.getLast_page();
                    if (beanLPAUnCloseBoservations.getCurrent_page()==beanLPAUnCloseBoservations.getLast_page()){
                        activity.btnLpaUnclosedDashboardNextPage.setEnabled(false);
                    }else {
                        activity.btnLpaUnclosedDashboardNextPage.setEnabled(true);
                    }
                    if (beanLPAUnCloseBoservations.getCurrent_page()==1){
                        activity.btnLpaUnclosedDashboardPrePage.setEnabled(false);
                    }else {
                        activity.btnLpaUnclosedDashboardPrePage.setEnabled(true);
                    }

                    List<tableLPAUnclosedObservations> data= new ArrayList<>();
                    for (int i=0;i<beanLPAUnCloseBoservations.getData().size();i++){
                        BeanLPAUnCloseBoservations.DataBean dataBean =beanLPAUnCloseBoservations.getData().get(i);
                        tableLPAUnclosedObservations tableLPAUnclosedObservations=new tableLPAUnclosedObservations();
                        tableLPAUnclosedObservations.setId(String.valueOf(dataBean.getId()));
                        tableLPAUnclosedObservations.setObservation(dataBean.getQuestion());
                        tableLPAUnclosedObservations.setAuditTime(dataBean.getGet_plan().getFinish_date());
                        tableLPAUnclosedObservations.setAction(dataBean.getCommit());
                        tableLPAUnclosedObservations.setCategory(dataBean.getPoint());
                        tableLPAUnclosedObservations.setItemDescibe(dataBean.getChecklist());
                        tableLPAUnclosedObservations.setArea(dataBean.getArea());
                        tableLPAUnclosedObservations.setAuditor(dataBean.getUserName());
                        tableLPAUnclosedObservations.setAuditList(dataBean.getCheckName());
                        tableLPAUnclosedObservations.setOwner(String.valueOf(dataBean.getOwner()));
                        tableLPAUnclosedObservations.setDueDate(String.valueOf(dataBean.getDue_date()));
                        data.add(tableLPAUnclosedObservations);
                    }
                    activity.stUnclosedObservations.setData(data);
                    activity.stUnclosedObservations.getConfig().setShowTableTitle(false)
                            .setShowYSequence(false)
                            .setShowXSequence(false)
                            .setShowTableTitle(false);


                    break;
                case UPDATE_FAIL_CODE:
                    Toast.makeText(activity.getActivity().getApplicationContext(), "获取失败了:  " + msg.obj.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lpa_unclosed_observations, null);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_lpa_unclosed_dashboard_refresh, R.id.btn_lpa_unclosed_dashboard_pre_page, R.id.btn_lpa_unclosed_dashboard_next_page})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_lpa_unclosed_dashboard_refresh:
                initData();
                Toast.makeText(this.getContext(),"刷新数据",Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_lpa_unclosed_dashboard_pre_page:
                PAGE--;
                initData();
                Toast.makeText(this.getContext(),"PRE",Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_lpa_unclosed_dashboard_next_page:
                PAGE++;
                initData();
                Toast.makeText(this.getContext(),"NEXT",Toast.LENGTH_LONG).show();
                break;
        }
    }
}
