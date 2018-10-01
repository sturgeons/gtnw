package xiaobaishushop.gtnw.entiy;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xiaobaishushop.gtnw.util.Constant;

public class RetrofitInstanceOne {
    private static Retrofit instance;
    private RetrofitInstanceOne(){}
    public  static Retrofit getInstance(){
        if (instance==null){
            instance= new Retrofit.Builder().baseUrl(Constant.ROOTURL1).
                    addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return  instance;
    }
}
