// ============================================================
// main.dart
// نقطة انطلاق التطبيق
// ============================================================

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'data/check_data.dart';
import 'providers/notes_provider.dart';
import 'screens/notes_screen.dart';
import 'screens/start_screen.dart';
import 'screens/checklist_screen.dart';
import 'screens/update_screen.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  // تحميل بيانات الـ Checklist من الـ assets
  await CheckData.instance.initList();
  runApp(const HospitalApp());
}

class HospitalApp extends StatelessWidget {
  const HospitalApp({super.key});

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (_) => NotesProvider()..loadNotes(),
      child: MaterialApp(
        title: 'Hospital App',
        debugShowCheckedModeBanner: false,
        theme: ThemeData(
          colorScheme: ColorScheme.fromSeed(
            seedColor: const Color(0xFF1565C0),
          ),
          useMaterial3: true,
        ),
        // ---- Navigation ----
        initialRoute: '/notes',
        onGenerateRoute: (settings) {
          switch (settings.name) {
            case '/notes':
              return MaterialPageRoute(
                builder: (_) => const NotesScreen(),
              );
            case '/start':
              return MaterialPageRoute(
                builder: (_) => const StartScreen(),
              );
            case '/checklist':
              final argument = settings.arguments as String;
              return MaterialPageRoute(
                builder: (_) => CheckListScreen(argument: argument),
              );
            case '/update':
              final id = settings.arguments as int;
              return MaterialPageRoute(
                builder: (_) => UpdateScreen(questionId: id),
              );
            default:
              return MaterialPageRoute(
                builder: (_) => const NotesScreen(),
              );
          }
        },
      ),
    );
  }
}
