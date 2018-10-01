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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Response;
import xiaobaishushop.gtnw.R;
import xiaobaishushop.gtnw.bean.BeanLPAUnCloseBoservations;
import xiaobaishushop.gtnw.entiy.RetrofitInstanceOne;
import xiaobaishushop.gtnw.service.LPAUncloseObservationsService;
import xiaobaishushop.gtnw.util.Logger;

public class FragmentLPAClosedDashboard extends Fragment {

    public static final int UPDATE_SUCCESS_CODE = 100;
    public static final int UPDATE_FAIL_CODE = 200;
    @BindView(R.id.st_closed_observations)
    SmartTable stClosedObservations;
    @BindView(R.id.btn_lpa_closed_dashboard_refresh)
    Button btnLpaClosedDashboardRefresh;
    @BindView(R.id.btn_lpa_closed_dashboard_pre_page)
    Button btnLpaClosedDashboardPrePage;
    @BindView(R.id.btn_lpa_closed_dashboard_page_view)
    Button btnLpaClosedDashboardPageView;
    @BindView(R.id.btn_lpa_closed_dashboard_next_page)
    Button btnLpaClosedDashboardNextPage;
    Unbinder unbinder;
//  更新 审核信息 Handler
    private UpdateUncloseObservationsHandler handler = new UpdateUncloseObservationsHandler(this);

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                try {
                    LPAUncloseObservationsService lpaUncloseObservationsService = RetrofitInstanceOne.getInstance().create(LPAUncloseObservationsService.class);
                    Call<BeanLPAUnCloseBoservations> call = lpaUncloseObservationsService.getUncloseObservations("1", "10");
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
/**
 * 垃圾收集 解绑信息
 * **/
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_lpa_closed_dashboard_refresh, R.id.btn_lpa_closed_dashboard_pre_page, R.id.btn_lpa_closed_dashboard_next_page})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_lpa_closed_dashboard_refresh:
                break;
            case R.id.btn_lpa_closed_dashboard_pre_page:
                break;
            case R.id.btn_lpa_closed_dashboard_next_page:
                break;
        }
    }

    /***
     * 更新 UI的handler
     * */
    public static class UpdateUncloseObservationsHandler extends Handler {
        final WeakReference<FragmentLPAClosedDashboard> mWeakReference;
        UpdateUncloseObservationsHandler(FragmentLPAClosedDashboard mWeakReference) {
            this.mWeakReference = new WeakReference<>(mWeakReference);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            FragmentLPAClosedDashboard activity = mWeakReference.get();
            switch (msg.what) {
                case UPDATE_SUCCESS_CODE:
                    BeanLPAUnCloseBoservations beanLPAUnCloseBoservations = (BeanLPAUnCloseBoservations) msg.obj;
                    break;
                case UPDATE_FAIL_CODE:
                    Toast.makeText(activity.getActivity().getApplicationContext(), "获取失败了:  " + msg.obj.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lpa_closed_observations, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }
}
