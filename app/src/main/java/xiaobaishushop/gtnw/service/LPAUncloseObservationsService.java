package xiaobaishushop.gtnw.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import xiaobaishushop.gtnw.bean.BeanLPAUnCloseBoservations;

public interface LPAUncloseObservationsService {
    @GET("LPA/dashboard/getUnCloseObservations")
    Call<BeanLPAUnCloseBoservations> getUncloseObservations(@Query("page") String page,@Query("count") String count);
}
