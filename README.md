# Hay Day Keyboard v3

نسخة مطورة من مشروع كيبورد Hay Day لأندرويد.

## ما الجديد
- عربي / إنجليزي داخل نفس الكيبورد.
- الضغط على المنتج يحذف النص الحالي كاملًا ويكتب الاسم فورًا.
- بحث سريع.
- فلترة حسب المستوى.
- تبويب مفضلة.
- حفظ آخر آلة مختارة والمفضلة محليًا.
- تحميل الصور من `assets/product_images/` أولًا ثم fallback إلى الرابط الخارجي.
- قاعدة البيانات أصبحت داخل ملفات JSON:
  - `app/src/main/assets/hayday_products.json`
  - `app/src/main/assets/hayday_machines.json`
- ملف manifest للصور المحلية:
  - `app/src/main/assets/product_images/index.json`
- سكربتات مساعدة داخل `tools/` لإدخال الصور أو تحديث قاعدة البيانات.

## الصور المحلية
ضع الصور داخل:
`app/src/main/assets/product_images/`

واستخدم نفس `product id` مثل:
- `bread.png`
- `feta_pie.png`
- `butter_popcorn.webp`

## ملاحظات مهمة
- هذه البيئة لا تحتوي Android SDK كامل، لذلك لم أستطع إخراج APK جاهز من هنا.
- المشروع جاهز للفتح في Android Studio وبناء APK محليًا.
- بيانات المنتجات الحالية منقولة إلى JSON لتسهيل الوصول إلى نسخة كاملة لاحقًا بدون تعديل الكود.
- حسب Hay Day Wiki، صفحة Products تذكر أن اللعبة تحتوي على **331 منتجًا حاليًا** باستثناء المنتجات المحدودة الوقت، وصفحة Production Buildings category تعرض **63 عنصرًا** ضمن فئة مباني الإنتاج. citeturn264949view0turn791875view0

## البناء
افتح المشروع في Android Studio ثم نفّذ Build > Build APK(s).


## Online APK Build via GitHub

This project includes a GitHub Actions workflow that can build a **debug APK online** without Android Studio.

### Steps
1. Create a new repository on GitHub.
2. Upload all project files to the repository.
3. Open the **Actions** tab.
4. Run **Build Android Debug APK**.
5. After it finishes, open the workflow run and download the artifact named **hayday-keyboard-debug-apk**.

### Notes
- This produces a **debug APK** suitable for testing.
- For Play Store or signed release builds, add a keystore and release signing config later.
