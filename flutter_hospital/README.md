# Hospital App - Flutter Version

تحويل مشروع Hospital App من Kotlin + Jetpack Compose إلى Flutter + Dart

## هيكل المشروع

```
lib/
├── main.dart                      ← نقطة الانطلاق + الـ Navigation
├── models/
│   ├── check_list_models.dart     ← نماذج بيانات الـ JSON
│   └── question.dart              ← نماذج قاعدة البيانات
├── data/
│   ├── check_data.dart            ← تحميل JSON من assets (مكافئ CheckData.kt)
│   └── question_database.dart     ← قاعدة البيانات sqflite (مكافئ Room)
├── providers/
│   └── notes_provider.dart        ← إدارة الحالة (مكافئ NotesViewModel.kt)
└── screens/
    ├── notes_screen.dart           ← قائمة الـ Checklists المحفوظة
    ├── start_screen.dart           ← اختيار نوع الـ Checklist
    ├── checklist_screen.dart       ← ملء الـ Checklist بـ RadioButtons
    └── update_screen.dart          ← عرض وتعديل Checklist محفوظة

assets/
└── checkList.json                  ← بيانات الـ Checklists (نفس الملف في الأندرويد)
```

## مكافئات الأندرويد

| Android (Kotlin)          | Flutter (Dart)              |
|---------------------------|-----------------------------|
| Jetpack Compose           | Flutter Widgets             |
| Room Database             | sqflite                     |
| ViewModel + StateFlow     | Provider + ChangeNotifier   |
| NavController             | Navigator + onGenerateRoute |
| Context (assets)          | rootBundle.loadString       |
| kotlinx.serialization     | dart:convert (jsonDecode)   |

## طريقة التشغيل

1. استبدل ملف `assets/checkList.json` بالملف الأصلي من مشروع الأندرويد
2. شغّل الأوامر التالية:

```bash
flutter pub get
flutter run
```

## المتطلبات

```yaml
dependencies:
  sqflite: ^2.3.0      # قاعدة البيانات المحلية
  path: ^1.8.3          # مسارات الملفات
  provider: ^6.1.1      # إدارة الحالة
  intl: ^0.18.1         # تنسيق التاريخ
```

## ملاحظة مهمة

تأكد من وضع ملف `checkList.json` الأصلي في مجلد `assets/`
واستبداله بالملف الموجود هناك حالياً (الملف الحالي مثال فقط).
