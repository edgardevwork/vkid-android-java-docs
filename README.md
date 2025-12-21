<div align="center">
  <h1 align="center">
    <img src="logo.svg" width="150" alt="VK ID SDK Logo">
  </h1>
  <p align="center">
    <a href="LICENSE">
      <img src="https://img.shields.io/npm/l/@vkid/sdk?maxAge=3600">
    </a>
  </p>
  <p align="center">
    VK ID ANDROID JAVA DOCS — Документация для использования VK ID One Tap в Java-приложениях для Android.
  </p>
</div>

---
<div align="center">
  <h1 align="center">
    Предыстория
  </h1>
  <p align="center">
    В 14 лет я хотел сделать вход через вк в свое приложение, но столкнулся с тем что я не нашел документации для языка программирование Java. И примерно через 1 год в 15 лет я нашел решение проблемы я хочу помочь всем кто пишет на Ява и поделиться Vk Id One Tap Java Документация.
  </p>
</div>

---
## Предварительно

Чтобы подключить VK ID SDK, сначала получите ID приложения (app_id) и защищенный ключ (client_secret). Для этого создайте приложение
в [кабинете подключения VK ID](https://id.vk.ru/business/go).

## Установка репозитория `VK ID`

<details>
<summary>Для начала работы добавьте репозитории в [settings.gradle](settings.gradle):</summary>

```java
pluginManagement {
    repositories {
        ...
        maven { url 'https://artifactory-external.vkpartner.ru/artifactory/vkid-sdk-android/' }
        maven { url 'https://artifactory-external.vkpartner.ru/artifactory/maven/' }
    }
}
dependencyResolutionManagement {
    repositories {
        ...
        maven { url 'https://artifactory-external.vkpartner.ru/artifactory/vkid-sdk-android/' }
        maven { url 'https://artifactory-external.vkpartner.ru/artifactory/maven/' }
        maven { url 'https://artifactory-external.vkpartner.ru/artifactory/vk-id-captcha/android/' }
    }
}
```
</details>

<details>
<summary>Настройте плагин для плейсхолдеров в [build.gradle](build.gradle):</summary>

```java
// Подключение плагина для Manifest Placeholders.
plugins {
    ...
    id "vkid.manifest.placeholders" version "1.1.0" apply true
}
String clientId = "(ваш ид)12345678"
String clientSecret = "(ваш)Защищенный ключ"
        
// Добавление значений в Manifest Placeholders.
vkidManifestPlaceholders {
    // Добавьте плейсхолдеры сокращенным способом. Например, vkidRedirectHost будет "vk.ru", а vkidRedirectScheme будет "vk$clientId".
    init(clientId, clientSecret)
    vkidRedirectHost = "vk.ru" // C 01.09.2025 vk.ru.
    vkidRedirectScheme = "vk{$clientId}" // Строго в формате vk{ID приложения}.
    vkidClientId = clientId
    vkidClientSecret = clientSecret
}
```
</details>

<details>
<summary>Подключение в [app/build.gradle](app/build.gradle)</summary>

```java
plugins {
    ...
    id "vkid.manifest.placeholders"
}

android {
    signingConfigs {// Не забудьте создать ключ для SHA1
        debug {
            storeFile file('D:\\disk-d\\CRMP\\TestVKID\\app\\test')
            storePassword '123456'
            keyAlias 'key0'
            keyPassword '123456'
        }
        release {
            storeFile file('D:\\disk-d\\CRMP\\TestVKID\\app\\test')
            storePassword '123456'
            keyAlias 'key0'
            keyPassword '123456'
        }
    }
    
    defaultConfig {
        ...
        defaultConfig {
            manifestPlaceholders = [
            VKIDClientID     : "12345678",   // ID вашего приложения (app_id)
                    VKIDClientSecret : "суда", // Ваш защищённый ключ (client_secret)
                    VKIDRedirectHost : "vk.ru",      // Домен перенаправления (обычно vk.ru)
                    VKIDRedirectScheme: "vk12345678"  // Cхема перенаправления (должна начинаться с "vk")
            ]
        }
    }
}

dependencies {
    ...
    // VK ID by EDGAR 3.0

    implementation 'com.vk.id:vkid:2.6.0' // Основной пакет VK ID
    implementation 'com.vk.id:onetap-compose:2.6.0' // Библиотека для компонуемых интерфейсов (Jetpack Compose)
    implementation 'com.vk.id:onetap-xml:2.6.0' // Библиотека для XML-интерфейсов

    /*implementation 'com.vk.id:group-subscription-xml:<LATEST_VERSION>'
    implementation 'androidx.compose.material3:material3-android:1.3.2' // Данная строка добавляет вес, но убирает предупреждение ее можно не добавлять.
    implementation 'com.vk.id.captcha:vkid-captcha:<LATEST_VERSION>'
    implementation 'com.vk.id.captcha:okhttp-interceptors:<LATEST_VERSION>'*/
}
```
</details>

---
## Работа с `VK ID One Tap`

<details>
<summary>Для начала работы создайте кнопку в `layout`</summary>
После добавление может появиться надпись Render Problem, можете не обращять на нее внимание

```xml
    <com.vk.id.onetap.xml.OneTap
        android:id="@+id/vk_one_tap_button"
        android:layout_width="200dp"
        android:layout_height="wrap_content"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:vkid_onetapScenario="SignIn"/>
```

`vkid_onetapScenario` - Отвечает за надпись на кнопке.

<table border="1">
  <tr>
    <th>Класс</th>
    <th>Сценарий</th>
    <th>Авторизованная зона</th>
    <th>Неавторизованная зона</th>
    <th>Описание</th>
  </tr>
  <tr>
    <td>SignIn</td>
    <td>Продолжить</td>
    <td>Продолжить как <strong>имя пользователя</strong></td>
    <td>Войти с VK ID</td>
    <td>Сценарий по умолчанию</td>
  </tr>
  <tr>
    <td>SignUp</td>
    <td>Записаться</td>
    <td>Записаться как <strong>имя пользователя</strong></td>
    <td>Записаться c VK ID</td>
    <td>Для сервисов сферы услуг и образовательных</td>
  </tr>
  <tr>
    <td>Get</td>
    <td>Получить</td>
    <td>Получить для <strong>имя пользователя</strong></td>
    <td>Получить с VK ID</td>
    <td>Для кейсов со скидкой или бонусом</td>
  </tr>
  <tr>
    <td>Open</td>
    <td>Открыть</td>
    <td>Открыть как <strong>имя пользователя</strong></td>
    <td>Открыть с VK ID</td>
    <td>Для финансовой сферы — например, чтобы открыть счёт, карту или вклад</td>
  </tr>
  <tr>
    <td>Calculate</td>
    <td>Рассчитать</td>
    <td>Рассчитать для <strong>имя пользователя</strong></td>
    <td>Рассчитать с VK ID</td>
    <td>Для финансовой сферы и сложных продуктов — например, стоимость проекта, ипотека</td>
  </tr>
  <tr>
    <td>Order</td>
    <td>Заказать</td>
    <td>Заказать как <strong>имя пользователя</strong></td>
    <td>Заказать с VK ID</td>
    <td>Для корзин в e-commerce</td>
  </tr>
  <tr>
    <td>PlaceOrder</td>
    <td>Оформить заказ</td>
    <td>Оформить заказ как <strong>имя пользователя</strong></td>
    <td>Оформить с VK ID</td>
    <td>Для корзин в e-commerce, альтернативный текст</td>
  </tr>
  <tr>
    <td>SendRequest</td>
    <td>Оставить заявку</td>
    <td>Оставить заявку как <strong>имя пользователя</strong></td>
    <td>Оставить заявку c VK ID</td>
    <td>Для e-commerce и сервисов, где требуется оставить заявку на участие</td>
  </tr>
  <tr>
    <td>Participate</td>
    <td>Участвовать</td>
    <td>Участвовать как <strong>имя пользователя</strong></td>
    <td>Участвовать c VK ID</td>
    <td>Для образовательных проектов и участия в тендерах</td>
  </tr>
</table>

Также можно установить програмно (в коде)

```java
    OneTap oneTap = findViewById(R.id.vk_one_tap_button);
    oneTap.setScenario(OneTapTitleScenario.SignIn);
```

</details>

<details>
<summary>Далее инициализируйте ваш VKID в главном файле!</summary>
:information_source: ВАЖНО если в приложении вы два раза инициализируете `VKID` то может выйти ошибка!

```java
...
import com.vk.id.VKID;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        VKID.Companion.init(this);
    }
```
</details>

<details>
<summary>Логи и Смена языка</summary>

```java
    VKID.Companion.setLogsEnabled(true);
    VKID.Companion.getInstance().setLocale(Locale.forLanguageTag("ru")); // Set Language / Настройка Языка
```
</details>

<details>
<summary>Создание обработчика для `OneTap`</summary>

```java
    OneTap oneTapButton = findViewById(R.id.vk_one_tap_button);
    oneTapButton.setScenario(OneTapTitleScenario.SignIn);

    // Регистрация обработчика нажатия
    oneTapButton.setCallbacks(
        (oAuth, accessToken) -> {
            if (accessToken != null/* && !TextUtils.isEmpty(accessToken.getToken())*/) {
                // Здесь можете обрабатывать успешную аутентификацию
                ...
            } else {
                // Если произошла ошибка или токен отсутствует
                ...
                Log.w("VK_AUTH", "Ошибка обработки токена");
                Toast.makeText(getApplicationContext(), "Ошибка токен пустой!", Toast.LENGTH_SHORT).show();
            }
            return null;
        },
        (oAuth, fail) -> {
            ...
            Log.e("VK_AUTH", "Ошибка обработки токена " + fail.getDescription());
            Toast.makeText(getApplicationContext(), "Ошибка: "+ fail.getDescription() + " !", Toast.LENGTH_SHORT).show();
            return null;
        },
        (data, completion) -> { // no error
            //Log.e("VK_AUTH", "Ошибка обработки токена / Не ошибка");
            //Toast.makeText(getApplicationContext(), "Ошибка другая!", Toast.LENGTH_SHORT).show();
            return null;
        }
    );
```

</details>

---
## Вопросы

<details>
<summary>Как выйти из VK ID?</summary>
Надо создать обработчик для сигнала выхода (он может быть пустым)

```java
    VKIDLogoutParams vkidLogoutParams = new VKIDLogoutParams.Builder().build();
    VKIDLogoutCallback vkidLogoutCallback = new VKIDLogoutCallback() {
        @Override
        public void onSuccess() {

        }

        @Override
        public void onFail(@NonNull VKIDLogoutFail vkidLogoutFail) {

        }
    };
```

После чего мы можем вызвать метод `logout`

```java
    VKID.Companion.getInstance().logout(vkidLogoutCallback, this, vkidLogoutParams);
```
</details>

<details>
<summary>Как получить данные?</summary>
Есть два способа, первый:

```java
    oneTapButton.setCallbacks(
        (oAuth, accessToken) -> {
            if (accessToken != null/* && !TextUtils.isEmpty(accessToken.getToken())*/){
                // Данные
                ...
                accessToken.getUserID(); // id user
                accessToken.getUserData().getFirstName();// Имя
                accessToken.getUserData().getLastName();// Фамилия
...
}});
```

Второй способ больше подойдет для получение данных в процессе:

```java
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
```
</details>

---
## ВАЖНО
<details>
<summary>accessToken</summary>

### accessToken - Хранится не более 1 часа.
</details>
<details>
<summary>VKID.Companion.init(this);</summary>

### Нельзя два раза инициализировать `VKID`
</details>
<details>
<summary>Render Problem</summary>

### Render Problem в xml ничего страшного
</details>

# Сделал EDGAR VIKYLIN Спасибо за внимание!
## Просьба указывать автора!
### Подпись: by Edgar 3.0

P.S: Поставь звездочку если было полезно, мне будет приятно