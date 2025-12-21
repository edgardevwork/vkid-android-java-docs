package com.edgar.testvkid;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.vk.id.VKID;
import com.vk.id.VKIDUser;
import com.vk.id.auth.VKIDAuthCallback;
import com.vk.id.internal.auth.VKIDAuthProvider;
import com.vk.id.logout.VKIDLogoutCallback;
import com.vk.id.logout.VKIDLogoutFail;
import com.vk.id.logout.VKIDLogoutParams;
import com.vk.id.onetap.compose.onetap.OneTapTitleScenario;
import com.vk.id.onetap.xml.OneTap;
import com.vk.id.refreshuser.VKIDGetUserCallback;
import com.vk.id.refreshuser.VKIDGetUserFail;
import com.vk.id.refreshuser.VKIDGetUserParams;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VKID.Companion.init(this);
        VKID.Companion.setLogsEnabled(true);
        VKID.Companion.getInstance().setLocale(Locale.forLanguageTag("ru")); // Set Language / Настройка Языка

        if (false) {// Выход из аккаунта
            VKIDLogoutParams vkidLogoutParams = new VKIDLogoutParams.Builder().build();
            VKIDLogoutCallback vkidLogoutCallback = new VKIDLogoutCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFail(@NonNull VKIDLogoutFail vkidLogoutFail) {

                }
            };
            VKID.Companion.getInstance().logout(vkidLogoutCallback, this, vkidLogoutParams);
        }

        OneTap oneTapButton = findViewById(R.id.vk_one_tap_button);
        oneTapButton.setScenario(OneTapTitleScenario.SignIn);

        // Регистрация обработчика нажатия
        oneTapButton.setCallbacks(
            (oAuth, accessToken) -> {
                if (accessToken != null/* && !TextUtils.isEmpty(accessToken.getToken())*/) {
                    // Данные
                    accessToken.getUserID(); // id user
                    accessToken.getUserData().getFirstName();// Имя
                    accessToken.getUserData().getLastName();// Фамилия

                    VKIDGetUserCallback myCallback = new VKIDGetUserCallback() {
                        @Override
                        public void onSuccess(@NonNull VKIDUser vkidUser) {
                            // Получили профиль пользователя успешно
                            String fullName = vkidUser.getFirstName() + " " + vkidUser.getLastName();
                            System.out.println("Получен профиль пользователя: " + fullName);
                            Toast.makeText(getApplicationContext(), "Привет " + vkidUser.getFirstName() + ", спасибо что пользуешься приложением.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFail(@NonNull VKIDGetUserFail vkidGetUserFail) {
                            System.out.println(vkidGetUserFail.getDescription());
                            Toast.makeText(getApplicationContext(), "Ошибка получение имени!", Toast.LENGTH_SHORT).show();
                        }
                    };
                    VKIDGetUserParams params = new VKIDGetUserParams.Builder().build(); // Или создайте параметры по желанию

                    VKID.Companion.getInstance().getUserData(this, myCallback, params);

                    // Здесь можете обрабатывать успешную аутентификацию
                } else {
                    // Если произошла ошибка или токен отсутствует
                    Log.w("VK_AUTH", "Ошибка обработки токена1");
                    Toast.makeText(getApplicationContext(), "Ошибка токен пустой!", Toast.LENGTH_SHORT).show();
                }
                return null;
            },
            (oAuth, fail) -> {
                Log.e("VK_AUTH", "Ошибка обработки токена2 " + fail.getDescription());
                Toast.makeText(getApplicationContext(), "Ошибка: "+ fail.getDescription() + " !", Toast.LENGTH_SHORT).show();
                return null;
            },
            (data, completion) -> {
                //Log.e("VK_AUTH", "Ошибка обработки токена / Не ошибка");
                //Toast.makeText(getApplicationContext(), "Ошибка другая!", Toast.LENGTH_SHORT).show();
                return null;
            }
        );
    }
}