package ibt.pahadisabzi.retrofit_provider;



import ibt.pahadisabzi.constant.Constant;
import ibt.pahadisabzi.model.address_add_responce.AddAddressModel;
import ibt.pahadisabzi.model.address_show_responce.AddressShowModel;
import ibt.pahadisabzi.model.app_version_responce.AppversionModel;
import ibt.pahadisabzi.model.banner_responce.BannerModel;
import ibt.pahadisabzi.model.change_password_responce.ChangePasswordModel;
import ibt.pahadisabzi.model.contact_responce.ContcatModel;
import ibt.pahadisabzi.model.contact_us_responce.ContactUsModel;
import ibt.pahadisabzi.model.history_single_order_responce.HistorySingleOrderModel;
import ibt.pahadisabzi.model.login_responce.LoginModel;
import ibt.pahadisabzi.model.order_history_responce.OrderHistoryModel;
import ibt.pahadisabzi.model.order_responce.OrderModel;
import ibt.pahadisabzi.model.productdetail_responce.ProductDetailModel;
import ibt.pahadisabzi.model.productlist_responce.ProductListModel;
import ibt.pahadisabzi.model.signup_responce.SignUpModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitApiClient {

    @GET(Constant.PRODUCTS_API)
    Call<ProductListModel> productData();


    @GET(Constant.BANNER_API)
    Call<BannerModel> bannerImage();


    @GET(Constant.CONTACT_API)
    Call<ContcatModel> contact();

    @GET(Constant.APP_VERSION)
    Call<AppversionModel> getVersion();


    @FormUrlEncoded
    @POST(Constant.SIGNUP_API)
    Call<SignUpModel> signUp(@Field("name") String name,
                             @Field("email") String email,
                             @Field("contact") String contact,
                             @Field("password") String password);


    @FormUrlEncoded
    @POST(Constant.ORDER_CANCEL)
    Call<SignUpModel> orderCancel(@Field("order_id") String order_id,
                                  @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(Constant.TOKEN_UPDATE)
    Call<SignUpModel> updateToken(@Field("user_id") String user_id,
                                   @Field("token") String token,
                                   @Field("user_ip") String user_ip,
                                   @Field("type") String type
    );

    @FormUrlEncoded
    @POST(Constant.PROFILE)
    Call<LoginModel> getprofile(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(Constant.FORGOT_PASSWORD_API)
    Call<SignUpModel> forgotPasswordApi(@Field("contact") String contact);

    @FormUrlEncoded
    @POST(Constant.OTP_API)
    Call<LoginModel> otpApi(@Field("contact") String contact ,
                            @Field("otp_number") String otp_number);

    @FormUrlEncoded
    @POST(Constant.CHANGE_PASSWORD)
    Call<ChangePasswordModel> changePassword(@Field("user_id") String user_id ,
                                             @Field("new_password") String new_password);

    @FormUrlEncoded
    @POST(Constant.LOGIN_WITH_MOBILE_API)
    Call<ResponseBody> loginWithMobile(@Field("contact") String email);

    @FormUrlEncoded
    @POST(Constant.LOGIN_API)
    Call<LoginModel> loginData(@Field("username") String email,
                               @Field("password") String password);


    @FormUrlEncoded
    @POST(Constant.CONTACT_US)
    Call<ResponseBody> contactUs(@Field("name") String name, @Field("email") String email,
                                 @Field("mobile_no") String mobile_no, @Field("subject") String subject
                                 , @Field("message") String message);



   /* @FormUrlEncoded
    @POST(Constant.ORDER_API)
    Call<ResponseBody> order(
            @Field("user_id") String user_id,
            @Field("first_name") String first_name,
            @Field("company_name") String company_name,
            @Field("user_email") String user_email,
            @Field("address") String address,
            @Field("phone_number") String phone_number,
            @Field("state") String state,
            @Field("city") String city,
            @Field("zip_code") String zip_code,
            @Field("product_details") String product_details);*/

    @FormUrlEncoded
    @POST(Constant.PRODUCTS_DETAIL_API)
    Call<ProductDetailModel> getProductDetail(@Field("product_id") String product_id);


    @Multipart
    @POST(Constant.UPDATE_PROFILE)
    Call<LoginModel> profileimage(@Part("user_id") RequestBody user_id,
                                        @Part("user_gendar") RequestBody user_gendar,
                                        @Part("user_name") RequestBody user_name,
                                        @Part("user_dob") RequestBody user_dob,
                                        @Part("user_email") RequestBody user_email,
                                        @Part MultipartBody.Part user_profile_picture);


    @FormUrlEncoded
    @POST(Constant.ADD_ADDRESS)
    Call<AddAddressModel> addAddress(
            @Field("user_id") String user_id,
            @Field("address") String address,
            @Field("location") String location,
            @Field("city") String city,
            @Field("state") String state,
            @Field("address_type") String address_type,
            @Field("long") String longi,
            @Field("lat") String lat,
            @Field("house_number") String house_number,
            @Field("zipcode") String zipcode);


    @FormUrlEncoded
    @POST(Constant.CONTACT_US)
    Call<ContactUsModel> contactUs(
            @Field("name") String name,
            @Field("email") String email,
            @Field("subject") String subject,
            @Field("message") String message);


    @FormUrlEncoded
    @POST(Constant.ORDER_PRODUCT_API)
    Call<HistorySingleOrderModel> orderDetails(
            @Field("order_id") String order_id);


    @FormUrlEncoded
    @POST(Constant.GET_ADDRESS)
    Call<AddressShowModel> getAddress(
            @Field("user_id") String user_id);


    @FormUrlEncoded
    @POST(Constant.UPDATE_ADDRESS)
    Call<SignUpModel> updateAddress(
            @Field("location") String location,
            @Field("lang") String lang,
            @Field("lati") String lati,
            @Field("address_house_number") String address_house_number,
            @Field("address_street_name") String address_street_name,
            @Field("address_city") String address_city,
            @Field("address_state") String address_state,
            @Field("address_country") String address_country,
            @Field("address_zipcode") String address_zipcode,
            @Field("id") String id,
            @Field("address_address_type") String address_address_type,
            @Field("address_status") String address_status,
            @Field("address_id") String address_id
    );

    @FormUrlEncoded
    @POST(Constant.GET_ORDER_HISTORY)
    Call<OrderHistoryModel> getOrderHistory(@Field("user_id") String user_id);


    @FormUrlEncoded
    @POST(Constant.ORDER)
    Call<OrderModel> setOrder(
            @Field("user_id") String user_id,
            @Field("order_payment_method") String order_payment_method,
            @Field("order_transaction_id") String order_transaction_id,
            @Field("delivery_schedule_type") String delivery_schedule_type ,
            @Field("user_address") String user_address,
            @Field("house_number") String house_number,
            @Field("user_landmark") String user_landmark,
            @Field("user_city") String user_city,
            @Field("user_address_type") String user_address_type,
            @Field("order_price") String order_price,
            @Field("order_location") String order_location,
            @Field("order_long") String order_long,
            @Field("order_lat") String order_lat,
            @Field("user_state") String user_state,
            @Field("user_zipcode") String user_zipcode,
            @Field("order_note") String order_note,
            @Field("product_details") String product_details);




}