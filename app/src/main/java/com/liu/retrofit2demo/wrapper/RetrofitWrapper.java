package com.liu.retrofit2demo.wrapper;

import com.liu.retrofit2demo.conf.AppUrl;
import com.liu.retrofit2demo.util.TLSSocketFactory;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Description: 描述
 * @AUTHOR 刘楠  Create By 2016/9/19 0019 17:59
 */
public class RetrofitWrapper {

    private static RetrofitWrapper instance;
    private Retrofit mRetrofit;

    private RetrofitWrapper() {

/*        1 失败 https://github.com/square/okhttp/issues/4053
//        Add legacy cipher suite for Android 4
        List<CipherSuite> cipherSuites = ConnectionSpec.MODERN_TLS.cipherSuites();
        if (!cipherSuites.contains(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA)) {
            cipherSuites = new ArrayList(cipherSuites);
            cipherSuites.add(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA);
        }
        final ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .cipherSuites(cipherSuites.toArray(new CipherSuite[0]))
                .build();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectionSpecs(Collections.singletonList(spec))
                .build();
*/

/*        2 失败 https://github.com/square/okhttp/issues/4053
//        Add legacy cipher suite for Android 4
        List<CipherSuite> cipherSuites = new ArrayList<>();
        cipherSuites.addAll(ConnectionSpec.MODERN_TLS.cipherSuites());
        cipherSuites.add(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA);
        cipherSuites.add(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA);

        ConnectionSpec legacyTls = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .cipherSuites(cipherSuites.toArray(new CipherSuite[0]))
                .build();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectionSpecs(Arrays.asList(legacyTls, ConnectionSpec.CLEARTEXT))
                .build();
*/

/*        3 失败 https://stackoverflow.com/questions/49980508/okhttp-sslhandshakeexception-ssl-handshake-aborted-failure-in-ssl-library-a-pro
//        Add legacy cipher suite for Android 4
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectionSpecs(Collections.singletonList(spec))
                .build();
*/

/*        4 失败 https://stackoverflow.com/questions/49980508/okhttp-sslhandshakeexception-ssl-handshake-aborted-failure-in-ssl-library-a-pro
//        Add legacy cipher suite for Android 4
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectionSpecs(Arrays.asList(ConnectionSpec.CLEARTEXT,
                        new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                                .allEnabledTlsVersions()
                                .allEnabledCipherSuites()
                                .build()))
                .build();
*/


/*        5 失败 https://stackoverflow.com/questions/37417400/sslprotocolexception-with-https-on-retrofit2
//        Add legacy cipher suite for Android 4
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_0)
                .allEnabledCipherSuites()
                .build();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectionSpecs(Collections.singletonList(spec))
                .build();
*/

//        成功
//        https://www.freshbytelabs.com/2018/09/how-to-solve-sslhandshakeexception-in.html
//        https://github.com/square/okhttp/issues/2372
//        sslSocketFactory新版需要TrustManager参数
//        https://www.baeldung.com/okhttp-client-trust-all-certificates
//        https://stackoverflow.com/questions/53304082/adding-a-custom-certificate-to-an-okhttp-client
//        https://stackoverflow.com/questions/40500629/okhttpclient-builder-sslsocketfactory-is-deprecated
//        https://stackoverflow.com/questions/25509296/trusting-all-certificates-with-okhttp
//        https://stackoverflow.com/questions/31002159/now-that-sslsocketfactory-is-deprecated-on-android-what-would-be-the-best-way-t

        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };
        OkHttpClient okHttpClient = new OkHttpClient();
        try {
            okHttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(new TLSSocketFactory(), (X509TrustManager) trustAllCerts[0])
                    .build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //初始化 添加转换工厂
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .callFactory(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public static RetrofitWrapper getInstance() {
        if (instance == null) {
            synchronized (RetrofitWrapper.class) {
                if (instance == null) {
                    instance = new RetrofitWrapper();
                }
            }
        }

        return instance;
    }

    /**
     * 转换为对象的Service
     *
     * @param service
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }

    /**
     * 常量类 基本的URL
     */
    public class Constant {
        //GithubService-base_url
        // public static final String BASE_URL = "https://api.github.com/";

        /**
         * 测试用API路径
         * get 与post 带参数
         */
        public static final String BASE_URL = AppUrl.BASE_URL;
    }
}
